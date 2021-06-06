package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class Endereco {

	@Column(name = "endereco_cep", nullable = false, length = 9)
	private String cep;
	
	@Column(name = "endereco_logradouro", nullable = false, length = 100)
	private String logradouro;
	
	@Column(name = "endereco_numero", nullable = false, length = 20)
	private String numero;
	
	@Column(name = "endereco_complemento", length = 60)
	private String complemento;
	
	@Column(name = "endereco_bairro", nullable = false, length = 60)
	private String bairro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco_cidade_id", nullable = false)
	private Cidade cidade;
	
}
