package com.algaworks.algafood.api.v1.model.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PedidoInput {

	@NotNull
	private IdInput<Long> restaurante;
	
	@Valid
	@NotNull
	private EnderecoInput enderecoEntrega;
	
	@NotNull
	private IdInput<Long> formaPagamento;
	
	@Valid
	@Size(min = 1)
	@NotNull
	private List<ItemPedidoInput> itens;
	
}