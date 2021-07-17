package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.model.CidadesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.EstadosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.FormasPagamentoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.GruposModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.LinksModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PedidosResumoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PermissoesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.ProdutosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.RestauranteBasicoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.RestaurantesBasicoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.UsuariosModelOpenApi;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.openapi.model.CidadesModelV2OpenApi;
import com.algaworks.algafood.api.v2.openapi.model.CozinhasModelV2OpenApi;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SpringFoxConfig implements WebMvcConfigurer {
	
	@Bean
	public Docket apiDocketV1() {
		
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
			.groupName("V1")	
			.select()
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.paths(PathSelectors.ant("/v1/**"))
				.build()
				.apiInfo(apiInfoV1())				
				.tags(tagsV1()[0], tagsV1())		
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class))
				.ignoredParameterTypes(ServletWebRequest.class)
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
				.directModelSubstitute(Links.class, LinksModelOpenApi.class)				
				.alternateTypeRules(newRule(PagedModel.class, CozinhaModel.class, CozinhasModelOpenApi.class))
				.alternateTypeRules(newRule(PagedModel.class, PedidoResumoModel.class, PedidosResumoModelOpenApi.class))
				.alternateTypeRules(newRule(List.class, RestauranteModel.class, RestauranteBasicoModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, CidadeModel.class, CidadesModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, EstadoModel.class, EstadosModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, FormaPagamentoModel.class, FormasPagamentoModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, GrupoModel.class, GruposModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, PermissaoModel.class, PermissoesModelOpenApi.class))
				.alternateTypeRules(newRule(PagedModel.class, PedidoResumoModel.class, PedidosResumoModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, ProdutoModel.class, ProdutosModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, RestauranteBasicoModel.class, RestaurantesBasicoModelOpenApi.class))
				.alternateTypeRules(newRule(CollectionModel.class, UsuarioModel.class, UsuariosModelOpenApi.class))
				.securitySchemes(List.of(authenticationScheme()))
				.securityContexts(List.of(securityContext()));
		
	}
	
	@Bean
	public Docket apiDocketV2() {
		
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
			.groupName("V2")	
			.select()				
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.paths(PathSelectors.ant("/v2/**"))
				.build()
				.apiInfo(apiInfoV2())				
				.tags(tagsV2()[0], tagsV2())		
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class))				
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
				.directModelSubstitute(Links.class, LinksModelOpenApi.class)				
				.alternateTypeRules(newRule(PagedModel.class, CozinhaModelV2.class, CozinhasModelV2OpenApi.class))				
				.alternateTypeRules(newRule(CollectionModel.class, CidadeModelV2.class, CidadesModelV2OpenApi.class))
				.securitySchemes(List.of(authenticationScheme()))
				.securityContexts(List.of(securityContext()));
	}	
	
	private <T, M, K> AlternateTypeRule newRule(Class<T> returnType, Class<M> modelObject, Class<K> modelObjectOpenApi) {
		var typeResolver = new TypeResolver();
		return AlternateTypeRules.newRule(
				typeResolver.resolve(ResponseEntity.class, typeResolver.resolve(returnType, modelObject)),
				typeResolver.resolve(modelObjectOpenApi),
				Ordered.HIGHEST_PRECEDENCE);		
	}
	
	private HttpAuthenticationScheme authenticationScheme() {
		return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("Authorization").build();
	}
	
	private SecurityContext securityContext() {		
		return SecurityContext.builder()
				.securityReferences(securityReference()).build();
	}
	
	 private List<SecurityReference> securityReference() {
	        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
	        authorizationScopes[0] = authorizationScope;
	        return List.of(new SecurityReference("Authorization", authorizationScopes));
	 }
	
	private ApiInfo apiInfoV1() {
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("1.0")
				.contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();
	}
	
	private ApiInfo apiInfoV2() {
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("2.0")
				.contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();
	}
	
	private Tag[] tagsV1() {
		return new Tag[] {
				new Tag("Cidades", "Gerencia as cidades"),
				new Tag("Grupos", "Gerencia os grupos de usuários"),
				new Tag("Cozinhas", "Gerencia as cozinhas"),
				new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
				new Tag("Restaurantes", "Gerencia os restaurantes"),
				new Tag("Estados", "Gerencia os estados"),
				new Tag("Produtos", "Gerencia os produtos de restaurantes"),
				new Tag("Usuários", "Gerencia os usuários"),
				new Tag("Estatísticas", "Estatísticas da AlgaFood"),
				new Tag("Permissões", "Gerencia as permissões")
		};
	}
	
	private Tag[] tagsV2() {
		return new Tag[] {
				new Tag("Cidades", "Gerencia as cidades"),				
				new Tag("Cozinhas", "Gerencia as cozinhas")				
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
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build()
		);			
	}	
	
	private List<Response> globalPostPutResponseMessages() {
		return Arrays.asList(
			new ResponseBuilder()					
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Requisição inválida (erro do cliente)")
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
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
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build(),
			new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.representation(MediaType.APPLICATION_JSON ).apply(builderModelProblema())
				.build()
		);			
	}	
	
	private Consumer<RepresentationBuilder> builderModelProblema() {
		return r -> r.model(m -> m.name("Problema")
				.referenceModel(
					ref -> ref.key(
							k -> k.qualifiedModelName(q -> q.name("Problema").namespace("com.algaworks.algafood.api.exception")
						))));
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("index.html")
         .addResourceLocations("classpath:/META-INF/resources/");
	}
	
}
