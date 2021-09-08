package ru.akademit.jwtauth.conf;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import ru.akademit.jwtauth.model.SecretData;
import ru.akademit.jwtauth.service.GetToken;

@Configuration
@AllArgsConstructor
@ConditionalOnClass(GetToken.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenGeneratorAutoConfig {
    static final Long lifeTime = 60L * 1000L * 15L;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public String generateSecretKey() {
        return RandomStringUtils.randomAlphanumeric(30);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecretData assignSecretData() {
        System.out.println(lifeTime);
        return SecretData.builder()
                .secretKey(generateSecretKey())
                .liveTime(lifeTime)
                .signatureAlgorithm(SignatureAlgorithm.HS256)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GetToken<?> getToken(SecretData s) {
        return new GetToken<>(s);
    }
}
