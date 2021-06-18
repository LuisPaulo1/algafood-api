package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.assembler.GenericModelAssembler;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(path = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private GenericInputDisassembler<CozinhaInput, Cozinha> cozinhaInputDisassembler;
	
	@Autowired
	private GenericModelAssembler<CozinhaModel, Cozinha> cozinhaModelAssembler;
	
	@GetMapping
	public ResponseEntity<Page<CozinhaModel>> listar(Pageable pageable) {
		Page<Cozinha> cozinhasPage = cadastroCozinha.listar(pageable);
		List<CozinhaModel> cozinhasModel = cozinhaModelAssembler.toCollectionModel(cozinhasPage.getContent(), CozinhaModel.class);				
		Page<CozinhaModel> CozinhasModelPage = new PageImpl<>(cozinhasModel, pageable, cozinhasPage.getTotalElements());
		return ResponseEntity.ok(CozinhasModelPage);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CozinhaModel> buscar(@PathVariable Long id) {		
		CozinhaModel cozinha = cozinhaModelAssembler.toModel(cadastroCozinha.buscar(id), CozinhaModel.class);
		return ResponseEntity.ok(cozinha);		
	}

	@PostMapping
	public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {		
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput, Cozinha.class);		
		CozinhaModel cozinhaModel = cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha), CozinhaModel.class);		
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaModel);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<CozinhaModel> atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput cozinhaInput) {		
		Cozinha cozinhaAtual = cadastroCozinha.buscar(id);		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);		
		CozinhaModel cozinhaModel = cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual), CozinhaModel.class);		
		return ResponseEntity.ok(cozinhaModel);		
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroCozinha.excluir(id);
		return ResponseEntity.noContent().build();
	}
}