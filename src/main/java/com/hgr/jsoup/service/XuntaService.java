package com.hgr.jsoup.service;

import com.hgr.jsoup.model.CommissionCriteria;
import com.hgr.jsoup.model.CommissionResult;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servicios de consulta de publicaciones de la Xunta.
 */
@Service
public class XuntaService {

    /** Log de la clase */
    private static Logger LOG = LoggerFactory.getLogger(XuntaService.class);

    /** URL de la web de consulta de publicaciones de la Xunta. */
    private static final String URL_COMMISIONS_WEB = "http://www.xunta.es/comisions";

    /** URL del servicio de consulta de publicaciones de la Xunta. */
    private static final String URL_COMMISIONS_SERVICE = "http://www.xunta.es/comisions/ConsultarAnuncios.do";

    /** Motor de búsqueda y procesado de plantillas */
    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * Realiza una búsqueda de anuncios de comisiones de servicio.
     * @param criteria Filtros de búsqueda.
     * @return Listado con los resultados de la búsqueda (comisiones).
     * @throws MalformedURLException        URL malformada.
     * @throws HttpStatusException          Respuesta no correcta y bandera de ignorar errores HTTP desactivada.
     * @throws UnsupportedMimeTypeException Tipo de dato en respuesta no soportado.
     * @throws SocketTimeoutException       Tiempo de conexión agotado.
     * @throws IOException                  Error desconocido.
     */
    public List<CommissionResult> getCommissions(CommissionCriteria criteria)
            throws MalformedURLException, HttpStatusException, UnsupportedMimeTypeException, SocketTimeoutException, IOException {

        List<CommissionResult> commissions = new ArrayList<CommissionResult>();

        // Realizamos petición a la página de la xunta para obtener las cookies
        Connection.Response response = Jsoup
                .connect(URL_COMMISIONS_WEB)
                .method(Connection.Method.GET)
                .execute();

        // Realizamos petición al servicio de consulta de anuncios
        Document serviceResponse = Jsoup
                .connect(URL_COMMISIONS_SERVICE)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .cookies(response.cookies())
                .data("aberto", toString(criteria.getFinalUsers()))
                .data("conselleria", toString(criteria.getCounselling()))
                .data("desde", toString(formatDate(criteria.getStartDate())))
                .data("ata", toString(formatDate(criteria.getEndDate())))
                .data("grupo", toString(criteria.getGroup()))
                .data("nivel", toString(criteria.getLevel()))
                .data("provincia", toString(criteria.getProvinceCode()))
                .data("concello", toString(criteria.getDistrict()))
                .data("descricion", toString(criteria.getDescription()))
                .post();

        // Validación de errores.
        LOG.debug(serviceResponse.outerHtml());
        for(Element script : serviceResponse.getElementsByTag("script")) {
            Optional<DataNode> errorDataNode = script.dataNodes().stream().filter(node -> node.getWholeData().contains(".error")).findFirst();
            if (errorDataNode.isPresent()) {
                Pattern p = Pattern.compile("toastr.error\\('(.*?)'\\)");
                Matcher m = p.matcher(errorDataNode.get().getWholeData());
                if (m.find()) {
                    throw new HttpStatusException(m.group(1), HttpStatus.INTERNAL_SERVER_ERROR.value(), URL_COMMISIONS_SERVICE);
                }
            }
        }

        // Procesamos la tabla de resultados: formulario + resultados.
        Elements tables = serviceResponse.select("table");
        if (tables.size() > 1) {
            Elements rows = tables.get(1).select("tr");

            // Eliminamos la última columna de la tabla (uris relativas).
            for (int i = 1; i < rows.size(); i++) {
                Elements values = rows.get(i).select("td");
                String destinyCenter = values.get(0).text();
                String jobCode = values.get(1).text();
                String denomination = values.get(2).text();
                String provision = values.get(3).text();
                String group = values.get(4).text();
                String level = values.get(5).text();

                commissions.add(new CommissionResult(destinyCenter, jobCode, denomination, provision, group, level));
            }
        }

        return commissions;
    }

    /**
     * Genera el contenido del correo de notificación con los resultados de la búsqueda de comisiones.
     * @param criteria Filtros de la búsqueda.
     * @param commissions Resultados de la búsqueda.
     * @return Cuerpo del correo.
     */
    public String createCommissionEmailContent(CommissionCriteria criteria, Collection<CommissionResult> commissions) {

        // Contexto de la plantilla
        final Context ctx = new Context();
        ctx.setVariable("criteria", criteria);
        ctx.setVariable("results", commissions);

        // Generación del cuerpo del correo.
        return templateEngine.process("commissions.html", ctx);
    }

    /**
     * Obtiene la cadena de texto con formato "dd/MM/yyyy" para la fecha especificada.
     * @param date Fecha a formatear.
     * @return Fecha con formato texto "dd/MM/yyyy".
     */
    private static String formatDate(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    /**
     * Obtiene la cadena de texto asociado para el filtro especificado (vacío si no aplica).
     * @param filter Filtro a formatear.
     * @return Filtro en formato cadena de texto.
     */
    private static String toString(Object filter) {
        return Objects.toString(filter, "");
    }

}