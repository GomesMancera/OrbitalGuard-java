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

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegiaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = IntegrationTestSupport.registerAndLogin(mockMvc, objectMapper, "regiao");
    }

    @Test
    void deveCriarListarEBuscarRegiao() throws Exception {
        Map<String, Object> regiao = Map.of(
                "nome", "Regiao Amazonia",
                "descricao", "Monitoramento da Amazonia",
                "latitude", -3.4653,
                "longitude", -62.2159
        );

        mockMvc.perform(post("/api/v1/regioes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regiao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Regiao Amazonia"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());

        mockMvc.perform(get("/api/v1/regioes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists());

        mockMvc.perform(get("/api/v1/regioes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Regiao Amazonia"));
    }

    @Test
    void deveRetornar404ParaRegiaoInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/regioes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveRetornar400ParaPayloadInvalido() throws Exception {
        Map<String, Object> regiaoInvalida = Map.of(
                "nome", "",
                "latitude", 200,
                "longitude", 0
        );

        mockMvc.perform(post("/api/v1/regioes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regiaoInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
