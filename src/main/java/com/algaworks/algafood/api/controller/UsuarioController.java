package com.algaworks.algafood.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.api.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi {
	@Autowired
	private CadastroUsuarioService cadastroUsuario;
				
	@Autowired
	private GenericInputDisassembler<UsuarioInput, Usuario> usuarioInputDisassembler;
			
	@Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

	@GetMapping
	public CollectionModel<UsuarioModel> listar() {
		CollectionModel<UsuarioModel> usuarios = usuarioModelAssembler.toCollectionModel(cadastroUsuario.listar());		
		return usuarios;
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioModel> buscar(@PathVariable Long id) {
		UsuarioModel usuario = usuarioModelAssembler.toModel(cadastroUsuario.buscar(id));		
		return ResponseEntity.ok(usuario);
	}

	@PostMapping
	public ResponseEntity<UsuarioModel> adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput, Usuario.class);
		UsuarioModel usuarioModel =  usuarioModelAssembler.toModel(cadastroUsuario.salvar(usuario));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioModel);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioModel> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioInput atualizarUsuarioInput) {		
		Usuario usuarioAtual = cadastroUsuario.buscar(id);
		usuarioInputDisassembler.copyToDomainObject(atualizarUsuarioInput, usuarioAtual);
		UsuarioModel usuarioModel = usuarioModelAssembler.toModel(cadastroUsuario.salvar(usuarioAtual));
		return ResponseEntity.ok(usuarioModel);		
	}
	
	@PutMapping("/{id}/senha")
	public ResponseEntity<Void> atualizarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInput senhaInput) {		
		cadastroUsuario.alterarSenha(id, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());		
		return ResponseEntity.noContent().build();		
	}	

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroUsuario.excluir(id);
		return ResponseEntity.noContent().build();		
	}
}
