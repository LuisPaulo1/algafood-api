package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Estados")
public interface EstadoControllerOpenApi {

    @ApiOperation("Lista os estados")
    ResponseEntity<CollectionModel<EstadoModel>> listar();

    @ApiOperation("Busca um estado por ID")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do estado inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Estado não encontrado", response = Problem.class)
    })
    ResponseEntity<EstadoModel> buscar(@ApiParam(value = "ID de um estado") Long estadoId);

    @ApiOperation("Cadastra um estado")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Estado cadastrado"),
    })
    ResponseEntity<EstadoModel> adicionar(EstadoInput estadoInput);

    @ApiOperation("Atualiza um estado por ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Estado atualizado"),
        @ApiResponse(code = 404, message = "Estado não encontrado", response = Problem.class)
    })
    ResponseEntity<EstadoModel> atualizar(@ApiParam(value = "ID de um estado") Long estadoId, EstadoInput estadoInput);

    @ApiOperation("Exclui um estado por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Estado excluído"),
        @ApiResponse(code = 404, message = "Estado não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> remover(@ApiParam(value = "ID de um estado") Long estadoId);
}     