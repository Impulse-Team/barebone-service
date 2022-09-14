package es.fujitsu.barebone.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Reference api versioning:
 * https://dzone.com/articles/versioning-rest-api-with-spring-boot-and-swagger
 * 
 * @author
 *
 */
@Configuration
@Slf4j
public class SwaggerConfig {

	private static final String API_TITLE = "Barebone API";
	private static final String BAREBONE_API = "barebone-api";
	// vnd is a convention "vendor"
	private static final String VENDOR_PACKAGE = "vnd.fujitsu.barebone";

	@Bean
	public Docket api() {
		log.info("configuring swagger 2");
		log.warn("api securization is pending");
		// if securization is needed follow example in
		// https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("es.fujitsu.barebone.controller")).paths(PathSelectors.any())
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("barebone-service public API", "TODO: description.", "v1.0", "Terms of service",
				new springfox.documentation.service.Contact("Mr Developer", "http://www.changeit.com",
						"changeit@changeit.com"),
				"License of API", "API license URL", Collections.emptyList());
	}

	private String getHeader(String version) {
		return "application/" + VENDOR_PACKAGE + "-v" + version + "+json";
	}

	private Docket swaggerBareboneApiCommon(String version, String description) {
		String header = getHeader(version);
		return new Docket(DocumentationType.SWAGGER_2).groupName(BAREBONE_API + "-" + version).select().apis(p -> {
			if (p.produces() != null) {
				for (MediaType mt : p.produces()) {
					if (mt.toString().equals(header)) {
						return true;
					}
				}
			}
			return false;
		}).build().produces(Collections.singleton(header))
				.apiInfo(new ApiInfoBuilder().version(version).title(API_TITLE).description(description).build());
	}

	@Bean
	public Docket swaggerBareboneApi10() {
		String version = "1.0";
		String description = "Descripción ejemplo";
		return swaggerBareboneApiCommon(version, description);
	}

	@Bean
	public Docket swaggerBareboneApi11() {
		String version = "1.1";
		String description = "Otra descripción ejemplo";
		return swaggerBareboneApiCommon(version, description);
	}

	@Bean
	public Docket swaggerBareboneApi12() {
		String version = "1.2";
		String description = "Otro ejemplo más";
		return swaggerBareboneApiCommon(version, description);
	}
}
