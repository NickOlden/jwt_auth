package ru.akademit.jwtauth.conf;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akademit.jwtauth.model.SecretDataToken;
import ru.akademit.jwtauth.service.ISecretDataConfigurator;
import ru.akademit.jwtauth.service.MainTokenService;
import ru.akademit.jwtauth.service.SecretDataConfigurator;

@Configuration
@AllArgsConstructor
@ConditionalOnClass(MainTokenService.class)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenGeneratorAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ISecretDataConfigurator secretDataConfigurator() {
        return new SecretDataConfigurator();
    }

    /**
     * default parameters:
     * {@link SecretDataConfigurator#getTokenLifetime()} - default token lifetime = 15 minutes
     * generated with method ${@link SecretDataConfigurator#getSecretKey()} secret key
     * {@link SignatureAlgorithm#HS256} - default algorithm for encrypting
     *
     * @return {@link SecretDataToken} with default parameters
     */
    @Bean
    @ConditionalOnMissingBean
    public SecretDataToken assignSecretData(ISecretDataConfigurator secretDataConfigurator) {
        return SecretDataToken.builder()
                .secretKey(secretDataConfigurator().getSecretKey())
                .liveTime(secretDataConfigurator().getTokenLifetime())
                .signatureAlgorithm(secretDataConfigurator.getAlgorithm())
                .build();
    }

    @Bean
    public MainTokenService<?> getToken(SecretDataToken s) {
        return new MainTokenService<>(s);
    }
}
