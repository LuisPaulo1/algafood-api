package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.input.PermissaoInput;
import com.algaworks.algafood.api.openapi.controller.PermissaoControllerOpenApi;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastroPermissaoService;

@RestController
@RequestMapping(path = "/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissaoController implements PermissaoControllerOpenApi {
	
	@Autowired
	private CadastroPermissaoService cadastroPermissao;
	
	@Autowired
	private GenericModelAssembler<PermissaoModel, Permissao> permissaoModelAssembler;
	
	@Autowired
	private GenericInputDisassembler<PermissaoInput, Permissao> permissaoInputAssembler;
	
	@GetMapping
	public ResponseEntity<List<PermissaoModel>> listar() {
		List<Permissao> permissoes = cadastroPermissao.listar();
		List<PermissaoModel> permissoesModel = permissaoModelAssembler.toCollectionModel(permissoes, PermissaoModel.class);
		return ResponseEntity.ok(permissoesModel);				
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PermissaoModel> buscar(@PathVariable Long id){				
		Permissao permissao = cadastroPermissao.buscar(id);
		PermissaoModel permissaoModel = permissaoModelAssembler.toModel(permissao, PermissaoModel.class);
		return ResponseEntity.ok(permissaoModel);			
	}
	
	@PostMapping
	public ResponseEntity<PermissaoModel> adicionar(@RequestBody PermissaoInput permissaoInput){
		Permissao permissao = permissaoInputAssembler.toDomainObject(permissaoInput, Permissao.class);
		permissao = cadastroPermissao.salvar(permissao);
		PermissaoModel permissaoModel = permissaoModelAssembler.toModel(permissao, PermissaoModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(permissaoModel);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PermissaoModel> atualizar(@PathVariable Long id, @RequestBody PermissaoInput permissaoInput) {		
		Permissao permissaoAtual = cadastroPermissao.buscar(id);	
		permissaoInputAssembler.copyToDomainObject(permissaoInput, permissaoAtual);		
		permissaoAtual = cadastroPermissao.salvar(permissaoAtual);
		PermissaoModel permissaoModel = permissaoModelAssembler.toModel(permissaoAtual, PermissaoModel.class);
		return ResponseEntity.ok(permissaoModel);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id){
		cadastroPermissao.excluir(id);
		return ResponseEntity.noContent().build();
	}

}
