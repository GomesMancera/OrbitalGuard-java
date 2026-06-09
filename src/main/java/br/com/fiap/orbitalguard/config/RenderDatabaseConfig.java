package br.com.fiap.orbitalguard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
@Profile("prod")
public class RenderDatabaseConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "DATABASE_URL")
    public DataSource renderDataSource(
            @Value("${DATABASE_URL}") String databaseUrl,
            @Value("${DATABASE_USERNAME:}") String explicitUsername,
            @Value("${DATABASE_PASSWORD:}") String explicitPassword
    ) {
        try {
            URI uri = new URI(databaseUrl);
            String username = explicitUsername;
            String password = explicitPassword;

            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                String userInfo = uri.getUserInfo();
                if (!StringUtils.hasText(userInfo)) {
                    throw new IllegalStateException("DATABASE_URL sem credenciais e variaveis USERNAME/PASSWORD ausentes");
                }
                int separatorIndex = userInfo.indexOf(':');
                if (separatorIndex < 0) {
                    throw new IllegalStateException("DATABASE_URL com formato de credenciais invalido");
                }
                username = URLDecoder.decode(userInfo.substring(0, separatorIndex), StandardCharsets.UTF_8);
                password = URLDecoder.decode(userInfo.substring(separatorIndex + 1), StandardCharsets.UTF_8);
            }

            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath()
                    + (StringUtils.hasText(uri.getQuery()) ? "?" + uri.getQuery() : "");

            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel configurar o DATABASE_URL do Render", ex);
        }
    }
}
