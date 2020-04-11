package com.hgr.jsoup.controller;

import com.hgr.jsoup.model.CommissionCriteria;
import com.hgr.jsoup.model.CommissionResult;
import com.hgr.jsoup.service.XuntaService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Controlador de peticiones asociadas a la Xunta.
 */
@RestController
@RequestMapping("xunta")
@Validated
public class XuntaController {

    /** Log de la clase */
    protected Logger LOG = LoggerFactory.getLogger(XuntaController.class);

    /** Servicio de consulta de publicaciones de la Xunta. */
    @Autowired
    private XuntaService xuntaService;

    /** Servicio de envío de correos. */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Fachada de consulta de anuncios de comisiones.
     * @param recipient Destinatario para notificación de resultados.
     * @param filters   Filtros de búsqueda.
     * @return Anuncios de comisiones.
     * @throws MalformedURLException        URL malformada.
     * @throws HttpStatusException          Respuesta no correcta y bandera de ignorar errores HTTP desactivada.
     * @throws UnsupportedMimeTypeException Tipo de dato en respuesta no soportado.
     * @throws SocketTimeoutException       Tiempo de conexión agotado.
     * @throws IOException                  Error desconocido.
     * @throws MessagingException           Error en el envío del correo de notificación.
     */
    @GetMapping("commission")
    public List<CommissionResult> getCommissions(@Email String recipient, @Valid CommissionCriteria filters)
            throws MalformedURLException, HttpStatusException, UnsupportedMimeTypeException, SocketTimeoutException, IOException, MessagingException {

        LOG.info("request started -> '/xunta/commission' -> recipient ({}), filters ({})", recipient, filters);
        final List<CommissionResult> commissions = xuntaService.getCommissions(filters);
        if (StringUtils.isNotEmpty(recipient) && commissions.size() > 0) {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            msg.setSubject("Xunta's Commission Results");
            msg.setContent(xuntaService.createCommissionEmailContent(filters, commissions), "text/html;charset=UTF-8");
            javaMailSender.send(msg);
        }
        LOG.info("request finished -> results ({})", commissions.size());
        return commissions;
    }

}