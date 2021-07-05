package com.algaworks.algafood.api.v2.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.api.v2.openapi.controller.CidadeControllerV2OpenApi;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(path = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 implements CidadeControllerV2OpenApi {

	@Autowired
	private CadastroCidadeService cadastroCidade;
		
	@Autowired
	private CidadeInputDisassemblerV2 cidadeInputDisassembler;
	
	@Autowired
	private CidadeModelAssemblerV2 cidadeModelAssembler;
		
	@GetMapping
	public ResponseEntity<CollectionModel<CidadeModelV2>> listar() {		
		List<Cidade> todasCidades = cadastroCidade.listar();		
		CollectionModel<CidadeModelV2> cidadesModel = cidadeModelAssembler.toCollectionModel(todasCidades);
		return ResponseEntity.ok(cidadesModel);				
	}

	@GetMapping("/{id}")
	public ResponseEntity<CidadeModelV2> buscar(@PathVariable Long id) {		
		CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cadastroCidade.buscar(id));			
		return ResponseEntity.ok(cidadeModel);		
	}
	
	@PostMapping
	public ResponseEntity<CidadeModelV2> adicionar(@RequestBody @Valid CidadeInputV2 cidadeInputV2) {
		Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInputV2);
		CidadeModelV2 cidadeModelV2 = cidadeModelAssembler.toModel(cadastroCidade.salvar(cidade));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(cidadeModelV2.getId()).toUri();	
		return ResponseEntity.created(uri).body(cidadeModelV2);				
	}
		
	@PutMapping("/{id}")
	public ResponseEntity<CidadeModelV2> atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInputV2 cidadeInputV2) {
		Cidade cidadeAtual = cadastroCidade.buscar(id);		
		cidadeAtual.setEstado(new Estado());
		cidadeInputDisassembler.copyToDomainObject(cidadeInputV2, cidadeAtual);		
		cidadeAtual = cadastroCidade.salvar(cidadeAtual);
		CidadeModelV2 cidadeModelV2 = cidadeModelAssembler.toModel(cidadeAtual);		
		return ResponseEntity.ok(cidadeModelV2);		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroCidade.excluir(id);
		return ResponseEntity.noContent().build();		
	}

}
