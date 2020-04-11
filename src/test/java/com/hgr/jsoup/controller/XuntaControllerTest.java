package com.hgr.jsoup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hgr.jsoup.model.CommissionCriteria;
import com.hgr.jsoup.model.CommissionResult;
import com.hgr.jsoup.service.XuntaService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static java.time.LocalDate.of;

/**
 * Bloque de test unitarios del controlador de peticiones de la Xunta.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class XuntaControllerTest {

    /** Realizador de peticiones para test unitarios a nivel controlador. */
    @Autowired
    private MockMvc mockMvc;

    /** Mock del servicio de peticiones de la Xunta. */
    @MockBean
    private XuntaService xuntaServiceMock;

    /** Mock del servicio de envío de correos. */
    @Autowired
    private JavaMailSender mailSender;

    /** Mapeador de objetos. */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Comprueba que la llamada devuelve vacío si el servicio no obtiene resultados.
     * @throws Exception Excepción producida en la llamada al servicio.
     */
    @Test
    public void successCallWithoutResults() throws Exception {
        when(xuntaServiceMock.getCommissions(any(CommissionCriteria.class))).thenReturn(EMPTY_RESULT);
        mockMvc.perform(get("/xunta/commission?startDate=2020-01-01&endDate=2020-01-01", 42L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    /**
     * Comprueba que la llamada devuelve los resultados obtenidos en el servicio.
     * @throws Exception Excepción producida en la llamada al servicio.
     */
    @Test
    public void successCallWithResults() throws Exception {
        when(xuntaServiceMock.getCommissions(any(CommissionCriteria.class))).thenReturn(TWO_RESULTS);
        mockMvc.perform(get("/xunta/commission?startDate=2020-01-01&endDate=2020-01-01", 42L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(TWO_RESULTS)));
    }

    /**
     * Comprueba las validaciones del parámetro destinatario.
     * @throws Exception Excepción producida en la llamada al servicio.
     */
    @Test
    public void parametersValidation() throws Exception {
        when(xuntaServiceMock.getCommissions(any(CommissionCriteria.class))).thenReturn(TWO_RESULTS);

        // Validación de filtros de fechas. Obligatorio y con formato 'yyyy-MM-dd'
        mockMvc.perform(get("/xunta/commission", 42L))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("startDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("startDate", "01-01-2020"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Validación de formato de correo electrónico.
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("recipient", "wrongemail")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("recipient", "wrong@email."))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("recipient", "wrong@email..es")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("recipient", "wrong.email.es")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("recipient", "")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(get("/xunta/commission", 42L)
                .param("recipient", "hugo.goldar@softtek.com")
                .param("startDate", "2020-01-01")
                .param("endDate", "2020-01-01"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /** Resultado de consulta de comisiones: 2 ocurrencias. */
    private static List<CommissionResult> TWO_RESULTS = List.of(
            new CommissionResult(
                    "S. X. DE EXPLOTACIÓNS AGRARIAS",
                    "MRC050000215770011",
                    "XEFATURA SECCIÓN",
                    "C",
                    "A1,A2",
                    "25"),
            new CommissionResult(
                    "SERVIZO DE INFRAESTRUTURAS AGRARIAS (A CORUÑA)",
                    "MRC991040115001124",
                    "XEFATURA SECCIÓN C",
                    "C",
                    "A1,A2",
                    "25")
    );

    /** Resultado de consulta de comisiones: sin ocurrencias */
    private static List<CommissionResult> EMPTY_RESULT = Lists.emptyList();

}