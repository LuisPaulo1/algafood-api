package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Produtos")
public interface RestauranteProdutoControllerOpenApi {

    @ApiOperation("Lista os produtos de um restaurante")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do restaurante inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<CollectionModel<ProdutoModel>> listar(
            @ApiParam(value = "ID do restaurante")
            Long restauranteId,            
            @ApiParam(value = "Indica se deve ou não incluir produtos inativos no resultado da listagem", defaultValue = "false")
            Boolean incluirInativos);

    @ApiOperation("Busca um produto de um restaurante")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do restaurante ou produto inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<ProdutoModel> buscar(
            @ApiParam(value = "ID do restaurante") Long restauranteId,            
            @ApiParam(value = "ID do produto") Long produtoId);

    @ApiOperation("Cadastra um produto de um restaurante")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Produto cadastrado"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<ProdutoModel> adicionar(
            @ApiParam(value = "ID do restaurante") Long restauranteId,            
            ProdutoInput produtoInput);

    @ApiOperation("Atualiza um produto de um restaurante")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Produto atualizado"),
        @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<ProdutoModel> atualizar(
            @ApiParam(value = "ID do restaurante") Long restauranteId,            
            @ApiParam(value = "ID do produto") Long produtoId,
            ProdutoInput produtoInput);
    
    @ApiOperation("Remover um produto de um restaurante")
 	@ApiResponses({
 		@ApiResponse(code = 204, message = "Produto excluído"),
 		@ApiResponse(code = 404, message = "Produto não encontrado", response = Problem.class)
 	})
    ResponseEntity<Void> remover(@ApiParam(value = "ID do produto") Long produtoId);
}