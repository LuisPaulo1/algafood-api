package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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

import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(path = "/v1/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private GenericInputDisassembler<CozinhaInput, Cozinha> cozinhaInputDisassembler;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping
	public ResponseEntity<PagedModel<CozinhaModel>> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cadastroCozinha.listar(pageable);		
		PagedModel<CozinhaModel> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage, cozinhaModelAssembler);	
		return ResponseEntity.ok(cozinhasPagedModel);	
	}
	
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping(value = "/{id}")
	public ResponseEntity<CozinhaModel> buscar(@PathVariable Long id) {		
		CozinhaModel cozinha = cozinhaModelAssembler.toModel(cadastroCozinha.buscar(id));
		return ResponseEntity.ok(cozinha);		
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@PostMapping
	public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {		
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput, Cozinha.class);		
		CozinhaModel cozinhaModel = cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));		
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaModel);
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@PutMapping(value = "/{id}")
	public ResponseEntity<CozinhaModel> atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput cozinhaInput) {		
		Cozinha cozinhaAtual = cadastroCozinha.buscar(id);		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);		
		CozinhaModel cozinhaModel = cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual));		
		return ResponseEntity.ok(cozinhaModel);		
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroCozinha.excluir(id);
		return ResponseEntity.noContent().build();
	}
}