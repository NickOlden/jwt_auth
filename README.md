# jwt_auth
SpringBoot starter for generating jwt tokens


To connect the project, add to your pom file:

```xml
<dependency>
    <groupId>ru.akademit.jwtauth</groupId>
    <artifactId>jwt_auth</artifactId>
    <version>[RELEASE VERSION (first is '1.0-RELEASE')]</version>
</dependency>
```

Autowire MainTokenService class, and use methods for generate, update, validate token


###Default parameters:
* token life tme == 15 min
* signature algorithm - HS256
* secret keq generating with MacProvider.generateKey() method


If you want to override the default parameters, you need to implement ```ISecretDataConfigurator``` interface, and configure new bean in your contex
