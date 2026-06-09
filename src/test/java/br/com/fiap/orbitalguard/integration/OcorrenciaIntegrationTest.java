package br.com.fiap.orbitalguard.integration;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OcorrenciaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = IntegrationTestSupport.registerAndLogin(mockMvc, objectMapper, "ocorrencia");

        mockMvc.perform(post("/api/v1/regioes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "nome", "Regiao Ocorrencia",
                        "descricao", "Teste",
                        "latitude", -23.55,
                        "longitude", -46.63
                ))));

        mockMvc.perform(post("/api/v1/alertas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "mensagem", "Alerta para ocorrencia",
                        "nivel", "MEDIO",
                        "status", "ATIVO",
                        "dataHora", LocalDateTime.now().toString(),
                        "regiaoId", 1
                ))));
    }

    @Test
    void deveCriarEBuscarOcorrencia() throws Exception {
        Map<String, Object> ocorrencia = Map.of(
                "descricao", "Equipe enviada para inspecao",
                "dataHora", LocalDateTime.now().toString(),
                "status", "EM_ANDAMENTO",
                "alertaId", 1
        );

        mockMvc.perform(post("/api/v1/ocorrencias")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrencia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());

        mockMvc.perform(get("/api/v1/ocorrencias/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Equipe enviada para inspecao"));
    }
}
