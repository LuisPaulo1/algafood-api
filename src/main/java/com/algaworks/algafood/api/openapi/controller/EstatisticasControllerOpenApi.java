package com.algaworks.algafood.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.controller.EstatisticasController.EstatisticasModel;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Estatísticas", hidden = true)
public interface EstatisticasControllerOpenApi {
	
	@ApiOperation(value = "Estatísticas", hidden = true)
	EstatisticasModel estatisticas();

    @ApiOperation("Consulta estatísticas de vendas diárias")
    @ApiImplicitParams({
        	@ApiImplicitParam(name = "restauranteId", value = "ID do restaurante"),
        	@ApiImplicitParam(name = "dataCriacaoInicio", value = "Data/hora inicial da criação do pedido"),
        	@ApiImplicitParam(name = "dataCriacaoFim", value = "Data/hora final da criação do pedido") 
        })
    List<VendaDiaria> consultarVendasDiarias(
    		VendaDiariaFilter filtro,            
            @ApiParam(value = "Deslocamento de horário a ser considerado na consulta em relação ao UTC", defaultValue = "+00:00") String timeOffset);
    
    ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter filtro, String timeOffset);
}