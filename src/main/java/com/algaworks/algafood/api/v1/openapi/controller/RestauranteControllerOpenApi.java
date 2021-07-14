package com.algaworks.algafood.api.v1.openapi.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Restaurantes")
public interface RestauranteControllerOpenApi {

    @ApiOperation(value = "Lista restaurantes")   
    ResponseEntity<CollectionModel<RestauranteBasicoModel>> listar();   
    
    @ApiOperation("Busca um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do restaurante inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<RestauranteModel> buscar(@ApiParam(value = "ID de um restaurante") Long restauranteId);
    
    @ApiOperation(value = "Lista de restaurantes pelo nome", hidden = true)
    ResponseEntity<List<RestauranteModel>> buscarPorNome(String nome, Long id);
    
    @ApiOperation(value = "Lista de restaurantes pelo nome e frete", hidden = true)
    ResponseEntity<List<RestauranteModel>> buscarPorNomeFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
    
    @ApiOperation(value = "Lista de restaurantes com frete grátis", hidden = true)
    ResponseEntity<List<RestauranteModel>> buscarComFreteGratis(String nome);
    
    @ApiOperation(value = "Busca o primeiro restaurante", hidden = true)
    ResponseEntity<RestauranteModel> buscarOPrimeiro();
    
    @ApiOperation("Cadastra um restaurante")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Restaurante cadastrado"),
    })
    ResponseEntity<RestauranteModel> adicionar(RestauranteInput restauranteInput);
    
    @ApiOperation("Atualiza um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Restaurante atualizado"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<RestauranteModel> atualizar(@ApiParam(value = "ID de um restaurante") Long restauranteId, RestauranteInput restauranteInput);
    
    @ApiOperation("Ativa um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante ativado com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> ativar(@ApiParam(value = "ID de um restaurante") Long restauranteId);
    
    @ApiOperation("Inativa um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante inativado com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> inativar(@ApiParam(value = "ID de um restaurante") Long restauranteId);
    
    @ApiOperation("Ativa múltiplos restaurantes")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurantes ativados com sucesso")
    })
    ResponseEntity<Void> ativarMultiplos(@ApiParam(name = "corpo", value = "IDs de restaurantes") List<Long> restauranteIds);
    
    @ApiOperation("Inativa múltiplos restaurantes")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurantes ativados com sucesso")
    })
    ResponseEntity<Void> inativarMultiplos(@ApiParam(name = "corpo", value = "IDs de restaurantes") List<Long> restauranteIds);

    @ApiOperation("Abre um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante aberto com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> abrir(@ApiParam(value = "ID de um restaurante") Long restauranteId);
    
    @ApiOperation("Fecha um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante fechado com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> fechar(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId);
    
    @ApiOperation("Exclui um restaurante por ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Restaurante excluído"),
		@ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
	})
    ResponseEntity<Void> remover(@ApiParam("ID de uma cidade") Long id);

}