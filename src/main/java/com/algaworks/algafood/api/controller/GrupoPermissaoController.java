package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.GenericModelAssembler;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/grupos/{grupoId}/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {
	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Autowired
	private GenericModelAssembler<PermissaoModel, Permissao> permissaoModelAssembler; 
	
	@GetMapping
	public ResponseEntity<List<PermissaoModel>> listar(@PathVariable Long grupoId){
		Grupo grupo = cadastroGrupoService.buscar(grupoId);		
		List<PermissaoModel> permissoes = permissaoModelAssembler.toCollectionModel(grupo.getPermissoes(), PermissaoModel.class);
		return ResponseEntity.ok(permissoes);
	}
	
	@PutMapping(value = "/{permissaoId}")
	public ResponseEntity<Void> associarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {		
		cadastroGrupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{permissaoId}")
	public ResponseEntity<Void> desassociarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {	
		cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}

}
