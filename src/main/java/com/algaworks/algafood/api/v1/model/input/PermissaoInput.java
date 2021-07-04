package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PermissaoInput {

	@ApiModelProperty(example = "CONSULTAR_COZINHAS", required = true)
	@NotBlank
	private String nome;

	@ApiModelProperty(example = "Permite consultar cozinhas", required = true)
	@NotBlank
	private String descricao;
	
}
