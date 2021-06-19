package com.algaworks.algafood.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.openapi.model.RestauranteBasicoModelOpenApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Restaurantes")
public interface RestauranteControllerOpenApi {

    @ApiOperation(value = "Lista restaurantes", response = RestauranteBasicoModelOpenApi.class)   
    public ResponseEntity<List<RestauranteModel>> listar();   
    
    @ApiOperation("Busca um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 400, message = "ID do restaurante inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<RestauranteModel> buscar(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId);
    
    @ApiOperation("Cadastra um restaurante")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Restaurante cadastrado"),
    })
    public ResponseEntity<RestauranteModel> adicionar(RestauranteInput restauranteInput);
    
    @ApiOperation("Atualiza um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Restaurante atualizado"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<RestauranteModel> atualizar(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId, RestauranteInput restauranteInput);
    
    @ApiOperation("Ativa um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante ativado com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> ativar(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId);
    
    @ApiOperation("Inativa um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante inativado com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> inativar(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId);
    
    @ApiOperation("Ativa múltiplos restaurantes")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurantes ativados com sucesso")
    })
    public ResponseEntity<Void> ativarMultiplos(@ApiParam(name = "corpo", value = "IDs de restaurantes") List<Long> restauranteIds);
    
    @ApiOperation("Inativa múltiplos restaurantes")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurantes ativados com sucesso")
    })
    public ResponseEntity<Void> inativarMultiplos(@ApiParam(name = "corpo", value = "IDs de restaurantes") List<Long> restauranteIds);

    @ApiOperation("Abre um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante aberto com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> abrir(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId);
    
    @ApiOperation("Fecha um restaurante por ID")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Restaurante fechado com sucesso"),
        @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    public ResponseEntity<Void> fechar(@ApiParam(value = "ID de um restaurante", example = "1") Long restauranteId);
    
    @ApiOperation("Exclui um restaurante por ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Restaurante excluído"),
		@ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
	})
    public ResponseEntity<Void> remover(@ApiParam("ID de uma cidade") Long id);

}