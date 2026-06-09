package br.com.fiap.orbitalguard.integration;

import br.com.fiap.orbitalguard.model.enums.TipoSensor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SensorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = IntegrationTestSupport.registerAndLogin(mockMvc, objectMapper, "sensor");

        mockMvc.perform(post("/api/v1/regioes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "nome", "Regiao SP",
                        "descricao", "Grande SP",
                        "latitude", -23.5505,
                        "longitude", -46.6333
                ))));
    }

    @Test
    void deveCriarSensorAguaComHeranca() throws Exception {
        Map<String, Object> sensor = Map.of(
                "nome", "Sensor Rio Tiete",
                "localizacao", "Marginal Tiete",
                "tipo", TipoSensor.AGUA.name(),
                "regiaoId", 1,
                "latitude", -23.5505,
                "longitude", -46.6333,
                "phMinimo", 6.5,
                "phMaximo", 8.5,
                "turbidezMaxima", 50.0
        );

        mockMvc.perform(post("/api/v1/sensores")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("AGUA"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    void deveRetornar404ParaSensorInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/sensores/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
