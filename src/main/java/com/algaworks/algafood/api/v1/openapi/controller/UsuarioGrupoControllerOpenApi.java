package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.v1.model.GrupoModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Usuários")
public interface UsuarioGrupoControllerOpenApi {

    @ApiOperation("Lista os grupos associados a um usuário")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Usuário não encontrado", response = Problem.class)
    })
    ResponseEntity<CollectionModel<GrupoModel>> listar(@ApiParam(value = "ID do usuário") Long usuarioId);

    @ApiOperation("Associação de grupo com usuário")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Associação realizada com sucesso"),
        @ApiResponse(code = 404, message = "Usuário ou grupo não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> associarGrupo(@ApiParam(value = "ID do usuário") Long usuarioId, @ApiParam(value = "ID do grupo") Long grupoId);
    
    @ApiOperation("Desassociação de grupo com usuário")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Desassociação realizada com sucesso"),
    	@ApiResponse(code = 404, message = "Usuário ou grupo não encontrado", 
    	response = Problem.class)
    })
    ResponseEntity<Void> desassociarGrupo(@ApiParam(value = "ID do usuário") Long usuarioId, @ApiParam(value = "ID do grupo") Long grupoId);
}