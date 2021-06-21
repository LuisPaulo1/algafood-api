package com.algaworks.algafood.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.model.PermissaoModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Grupos")
public interface GrupoPermissaoControllerOpenApi {
    
    @ApiOperation("Lista as permissões associadas a um grupo")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do grupo inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    ResponseEntity<List<PermissaoModel>> listar(@ApiParam(value = "ID do grupo") Long grupoId);

    @ApiOperation("Associação de permissão com grupo")
    @ApiResponses({
    	@ApiResponse(code = 204, message = "Associação realizada com sucesso"),
    	@ApiResponse(code = 404, message = "Grupo ou permissão não encontrada", 
    	response = Problem.class)
    })
    ResponseEntity<Void> associarPermissao(@ApiParam(value = "ID do grupo") Long grupoId, @ApiParam(value = "ID da permissão") Long permissaoId);
    
    @ApiOperation("Desassociação de permissão com grupo")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Desassociação realizada com sucesso"),
        @ApiResponse(code = 404, message = "Grupo ou permissão não encontrada", response = Problem.class)
    })
    ResponseEntity<Void> desassociarPermissao(@ApiParam(value = "ID do grupo") Long grupoId, @ApiParam(value = "ID da permissão") Long permissaoId);

}   