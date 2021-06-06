package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastroPermissaoService;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {
	
	@Autowired
	private CadastroPermissaoService cadastroPermissao;
	
	@GetMapping
	public ResponseEntity<List<Permissao>> listar(){
		List<Permissao> permissoes = cadastroPermissao.listar();
		return ResponseEntity.ok(permissoes);				
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id){				
		Permissao permissao = cadastroPermissao.buscar(id);
		return ResponseEntity.ok(permissao);			
	}
	
	@PostMapping
	public ResponseEntity<Permissao> adicionar(@RequestBody Permissao permissao){
		permissao = cadastroPermissao.salvar(permissao);
		return ResponseEntity.status(HttpStatus.CREATED).body(permissao);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Permissao permissao) {		
		Permissao permissaoAtual = cadastroPermissao.buscar(id);
		BeanUtils.copyProperties(permissao, permissaoAtual,"id");
		permissaoAtual = cadastroPermissao.salvar(permissaoAtual);
		return ResponseEntity.ok(permissaoAtual);		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id){
		cadastroPermissao.excluir(id);
		return ResponseEntity.noContent().build();
	}

}
