package ru.akademit.jwtauth.conf;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.akademit.jwtauth.model.SecretDataToken;
import ru.akademit.jwtauth.service.TokenService;

import java.security.Key;

@Configuration
@AllArgsConstructor
@ConditionalOnClass(TokenService.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenGeneratorAutoConfig {
    static final Long LIFE_TIME = 1000L * 60 * 15;
    static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Key generateSecretKey() {
        return Keys.secretKeyFor(ALGORITHM);
    }

    /**
     * default parameters:
     * {@link TokenGeneratorAutoConfig#LIFE_TIME} - default token lifetime = 15 minutes
     * generated with method ${@link TokenGeneratorAutoConfig#generateSecretKey()} secret key (singletone)
     * {@link SignatureAlgorithm#HS256} - default algorithm for encrypting
     *
     * @return {@link SecretDataToken} with default parameters
     */
    @Bean
    @ConditionalOnMissingBean
    public SecretDataToken assignSecretData() {
        return SecretDataToken.builder()
                .secretKey(generateSecretKey())
                .liveTime(LIFE_TIME)
                .signatureAlgorithm(ALGORITHM)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenService<?> getToken(SecretDataToken s) {
        return new TokenService<>(s);
    }
}
