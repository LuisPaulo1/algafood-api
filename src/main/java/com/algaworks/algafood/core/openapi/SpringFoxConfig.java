package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SpringFoxConfig implements WebMvcConfigurer {

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.OAS_30)
			.select()
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.build()
				.apiInfo(apiInfo())
				.tags(tags()[0], tags())		
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, globalPostResponseMessages())
				.globalResponses(HttpMethod.PUT, globalPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages());
	}	
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("1.0")
				.contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();
	}
	
	private Tag[] tags() {
		return new Tag[] {
				new Tag("Cidades", "Gerencia as cidades")	        
		};
	}
	
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.build()
		);			
	}	
	
	private List<Response> globalPostResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Requisição inválida (erro do cliente)")
				.build(),
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),				
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Requisição recusada porque o corpo está em um formato não suportado")
				.build(),		
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.build()				
		);			
	}		
	
	private List<Response> globalPutResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Requisição inválida (erro do cliente)")
				.build(),
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Requisição recusada porque o corpo está em um formato não suportado")
				.build(),		
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.build()				
			);					
	}		
	
	private List<Response> globalDeleteResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Requisição inválida (erro do cliente)")
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.build()
		);			
	}	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("index.html")
         .addResourceLocations("classpath:/META-INF/resources/");
	}
	
}
