package com.algaworks.algafood.api.v2.controller;

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

import com.algaworks.algafood.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.api.v2.openapi.controller.CozinhaControllerV2OpenApi;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(path = "/v2/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaControllerV2 implements CozinhaControllerV2OpenApi {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaInputDisassemblerV2 cozinhaInputDisassembler;
	
	@Autowired
	private CozinhaModelAssemblerV2 cozinhaModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@GetMapping
	public ResponseEntity<PagedModel<CozinhaModelV2>> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = cadastroCozinha.listar(pageable);		
		PagedModel<CozinhaModelV2> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage, cozinhaModelAssembler);	
		return ResponseEntity.ok(cozinhasPagedModel);	
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CozinhaModelV2> buscar(@PathVariable Long id) {		
		CozinhaModelV2 cozinha = cozinhaModelAssembler.toModel(cadastroCozinha.buscar(id));
		return ResponseEntity.ok(cozinha);		
	}

	@PostMapping
	public ResponseEntity<CozinhaModelV2> adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInputV2) {		
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInputV2);		
		CozinhaModelV2 cozinhaModel = cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));		
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaModel);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<CozinhaModelV2> atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInputV2 cozinhaInputV2) {		
		Cozinha cozinhaAtual = cadastroCozinha.buscar(id);		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInputV2, cozinhaAtual);		
		CozinhaModelV2 cozinhaModel = cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual));		
		return ResponseEntity.ok(cozinhaModel);		
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroCozinha.excluir(id);
		return ResponseEntity.noContent().build();
	}
}