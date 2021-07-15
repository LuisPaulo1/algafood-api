package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(path = "/v1/usuarios/{usuarioId}/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
			
	@Autowired
	private AlgaLinks algaLinks;  
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<GrupoModel>> listar(@PathVariable Long usuarioId){
		Usuario usuario = cadastroUsuarioService.buscar(usuarioId);

	    CollectionModel<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
	            .removeLinks()
	            .add(algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));
	    
	    gruposModel.getContent().forEach(grupoModel -> {
	        grupoModel.add(algaLinks.linkToUsuarioGrupoDesassociacao(
	                usuarioId, grupoModel.getId(), "desassociar"));
	    });
		
		
		return ResponseEntity.ok(gruposModel);		
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@PutMapping(value = "/{grupoId}")
	public ResponseEntity<Void> associarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){
		cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@DeleteMapping(value = "/{grupoId}")
	public ResponseEntity<Void> desassociarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){
		cadastroUsuarioService.desassociarGrupo(usuarioId, grupoId);
		return ResponseEntity.noContent().build();
	}
	
}
