package com.algaworks.algafood.api.openapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.exception.Problem;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.filter.PedidoFilter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Pedidos")
public interface PedidoControllerOpenApi {
   
    @ApiOperation("Pesquisa os pedidos")
    public ResponseEntity<Page<PedidoResumoModel>> pesquisar(PedidoFilter filtro, Pageable pageable);
    
    @ApiOperation("Busca um pedido por código")
    @ApiResponses({
    	@ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    public ResponseEntity<PedidoModel> buscar(
    		@ApiParam(value = "Código de um pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55")
    		String codigoPedido);
    
    @ApiOperation("Registra um pedido")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Pedido registrado"),
    })
    public ResponseEntity<PedidoModel> adicionar(PedidoInput pedidoInput);    
  
}