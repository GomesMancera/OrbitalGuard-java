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

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AlertaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = IntegrationTestSupport.registerAndLogin(mockMvc, objectMapper, "alerta");

        mockMvc.perform(post("/api/v1/regioes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "nome", "Regiao Alerta",
                        "descricao", "Teste",
                        "latitude", -23.55,
                        "longitude", -46.63
                ))));
    }

    @Test
    void deveCriarAtualizarEBuscarAlerta() throws Exception {
        Map<String, Object> alerta = Map.of(
                "mensagem", "Nivel de poluicao elevado",
                "nivel", "ALTO",
                "status", "ATIVO",
                "dataHora", LocalDateTime.now().toString(),
                "regiaoId", 1
        );

        mockMvc.perform(post("/api/v1/alertas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alerta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());

        Map<String, Object> alertaAtualizado = Map.of(
                "mensagem", "Nivel critico detectado",
                "nivel", "CRITICO",
                "status", "EM_ANALISE",
                "dataHora", LocalDateTime.now().toString(),
                "regiaoId", 1
        );

        mockMvc.perform(put("/api/v1/alertas/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alertaAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nivel").value("CRITICO"));

        mockMvc.perform(get("/api/v1/alertas/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Nivel critico detectado"));
    }
}
