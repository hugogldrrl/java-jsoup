package com.hgr.jsoup.service;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Bloque de test unitarios del servicio de peticiones de la Xunta.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class XuntaServiceTest {

	/**
	 * Test que comprueba que se realiza correctamente la petición a la web de comisiones.
	 * @throws MalformedURLException        URL malformada.
	 * @throws HttpStatusException          Respuesta no correcta y bandera de ignorar errores HTTP desactivada.
	 * @throws UnsupportedMimeTypeException Tipo de dato en respuesta no soportado.
	 * @throws SocketTimeoutException       Tiempo de conexión agotado.
	 * @throws IOException                  Error desconocido.
	 */
	@Test
	public void xuntaConnectionTest()
			throws MalformedURLException, HttpStatusException, UnsupportedMimeTypeException, SocketTimeoutException, IOException {

		Connection.Response response = Jsoup
				.connect("http://www.xunta.es/comisions")
				.method(Connection.Method.GET)
				.execute();
		assertEquals(HttpStatus.OK.value(), response.statusCode());
	}

	/**
	 * Test que comprueba que se realiza correctamente la petición al servicio de consulta de comisiones.
	 * @throws MalformedURLException        URL malformada.
	 * @throws HttpStatusException          Respuesta no correcta y bandera de ignorar errores HTTP desactivada.
	 * @throws UnsupportedMimeTypeException Tipo de dato en respuesta no soportado.
	 * @throws SocketTimeoutException       Tiempo de conexión agotado.
	 * @throws IOException                  Error desconocido.
	 */
	@Test
	public void xuntaCommisionConnectionTest()
			throws MalformedURLException, HttpStatusException, UnsupportedMimeTypeException, SocketTimeoutException, IOException {

		// Realizamos petición a la página de la xunta para obtener las cookies
		Connection.Response response = Jsoup
				.connect("http://www.xunta.es/comisions")
				.method(Connection.Method.GET)
				.execute();

		// Realizamos petición al servicio de consulta de anuncios
		Document serviceResponse = Jsoup
				.connect("http://www.xunta.es/comisions/ConsultarAnuncios.do")
				.userAgent("Mozilla/5.0")
				.timeout(10 * 1000)
				.cookies(response.cookies())
				.post();

		assertEquals(HttpStatus.OK.value(), response.statusCode());
		assertThat(serviceResponse.outerHtml(), is(not(emptyOrNullString())));
	}

}