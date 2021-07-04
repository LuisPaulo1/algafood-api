package com.algaworks.algafood.api.v1.openapi.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Produtos")
public interface RestauranteProdutoFotoControllerOpenApi {

    @ApiOperation("Atualiza a foto do produto de um restaurante")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Foto do produto atualizada"),
        @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<FotoProdutoModel> atualizarFoto(
            @ApiParam(value = "ID do restaurante") Long restauranteId,            
            @ApiParam(value = "ID do produto") Long produtoId,            
            FotoProdutoInput fotoProdutoInput,            
            @ApiParam(value = "Arquivo da foto do produto (máximo 500KB, apenas JPG e PNG)", required = true) 
            MultipartFile arquivo) throws IOException;

    @ApiOperation(value = "Busca a foto do produto de um restaurante", produces = "application/json, image/jpeg, image/png")
    @ApiResponses({
    	@ApiResponse(code = 400, message = "ID do restaurante ou produto inválido", response = Problem.class),
    	@ApiResponse(code = 404, message = "Foto de produto não encontrada", response = Problem.class)
    })
    ResponseEntity<FotoProdutoModel> buscarFoto(@ApiParam(value = "ID do restaurante") Long restauranteId,            
    		@ApiParam(value = "ID do produto") Long produtoId);
    
    @ApiOperation(value = "Busca a foto do produto de um restaurante", hidden = true)
    ResponseEntity<?> servirFoto(Long restauranteId, Long produtoId, String acceptHeader) 
    		throws HttpMediaTypeNotAcceptableException;
    
    @ApiOperation("Exclui a foto do produto de um restaurante")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Foto do produto excluída"),
        @ApiResponse(code = 400, message = "ID do restaurante ou produto inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Foto de produto não encontrada", response = Problem.class)
    })
    ResponseEntity<Void> remover(
            @ApiParam(value = "ID do restaurante") Long restauranteId,            
            @ApiParam(value = "ID do produto") Long produtoId);


}