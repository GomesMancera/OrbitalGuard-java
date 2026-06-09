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

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LeituraSensorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private LocalDateTime dataHoraLeitura;

    @BeforeEach
    void setUp() throws Exception {
        token = IntegrationTestSupport.registerAndLogin(mockMvc, objectMapper, "leitura");
        dataHoraLeitura = LocalDateTime.of(2026, 6, 5, 10, 0);

        mockMvc.perform(post("/api/v1/regioes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "nome", "Regiao Leitura",
                        "descricao", "Teste",
                        "latitude", -23.55,
                        "longitude", -46.63
                ))));

        mockMvc.perform(post("/api/v1/sensores")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "nome", "Sensor Teste",
                        "localizacao", "Marginal",
                        "tipo", TipoSensor.AGUA.name(),
                        "regiaoId", 1,
                        "latitude", -23.55,
                        "longitude", -46.63,
                        "phMinimo", 6.5,
                        "phMaximo", 8.5,
                        "turbidezMaxima", 50.0
                ))));
    }

    @Test
    void deveCriarEAtualizarLeituraComChaveComposta() throws Exception {
        mockMvc.perform(post("/api/v1/leituras")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "sensorId", 1,
                                "dataHoraLeitura", dataHoraLeitura.toString(),
                                "valor", 7.2,
                                "unidade", "pH",
                                "observacao", "Leitura inicial"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());

        mockMvc.perform(put("/api/v1/leituras/1/" + dataHoraLeitura)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "valor", 8.1,
                                "unidade", "pH",
                                "observacao", "Leitura atualizada"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(8.1))
                .andExpect(jsonPath("$.observacao").value("Leitura atualizada"));
    }
}
