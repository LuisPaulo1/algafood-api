package com.algaworks.algafood.api.v1.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeInput {
	
	@NotBlank
	private String nome;
	
	@Valid
	@NotNull
	private IdInput<Long> estado;

}
