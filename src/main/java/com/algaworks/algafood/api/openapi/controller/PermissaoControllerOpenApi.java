package com.algaworks.algafood.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.input.PermissaoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Permissões")
public interface PermissaoControllerOpenApi {

	@ApiOperation("Lista as permissões")
	ResponseEntity<List<PermissaoModel>> listar();
	
	@ApiOperation("Busca uma permissão por ID")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID da permissão inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Permissão não encontrada", response = Problem.class)
	})	
	ResponseEntity<PermissaoModel> buscar(@ApiParam(value = "ID de uma permissão") Long permissaoId);
	
	@ApiOperation("Cadastrar uma permissão")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Permissão cadastrada"),
	})
	public ResponseEntity<PermissaoModel> adicionar(PermissaoInput permissaoInput);
	
	@ApiOperation("Atualiza uma permissão por ID")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Permissão atualizada"),
		@ApiResponse(code = 404, message = "Permissão não encontrada", response = Problem.class)
	})
	ResponseEntity<PermissaoModel> atualizar(@ApiParam(value = "ID de uma permissão") Long id, PermissaoInput permissaoInput);
	
	@ApiOperation("Exclui uma permissão por ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Permissão excluída"),
		@ApiResponse(code = 404, message = "Permissão não encontrada", response = Problem.class)
	})
	ResponseEntity<Void> remover(@ApiParam(value = "ID de uma permissão") Long permissaoId);
	
}