package study.sns.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    // Swagger 3.0 적용
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey("ACCESS-TOKEN"), apiKey("REFRESH-TOKEN")))
                .apiInfo(myApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("study.sns.controller.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo myApiInfo() {
        return new ApiInfoBuilder()
                .title("Out Story Swagger Docs")
                .version("1.0")
                .description("[화면 구현 페이지 링크](http://ec2-52-79-82-151.ap-northeast-2.compute.amazonaws.com:8085/)")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
                new SecurityReference("ACCESS-TOKEN", authorizationScopes),
                new SecurityReference("REFRESH-TOKEN", authorizationScopes));
    }

    private ApiKey apiKey(String name) {
        return new ApiKey(name, name, "header");
    }
}
