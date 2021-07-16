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
import com.algaworks.algafood.api.v1.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/v1/grupos/{grupoId}/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {
	
	@Autowired
	private AlgaLinks algaLinks;	
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
		
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<PermissaoModel>> listar(@PathVariable Long grupoId){
		Grupo grupo = cadastroGrupoService.buscar(grupoId);		
		
		CollectionModel<PermissaoModel> permissoesModel 
		= permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
			.removeLinks();
	
		permissoesModel.add(algaLinks.linkToGrupoPermissoes(grupoId));
		
		if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
			permissoesModel.add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
		
			permissoesModel.getContent().forEach(permissaoModel -> {
				permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
						grupoId, permissaoModel.getId(), "desassociar"));
			});
		}
		
		return ResponseEntity.ok(permissoesModel);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@PutMapping(value = "/{permissaoId}")
	public ResponseEntity<Void> associarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {		
		cadastroGrupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@DeleteMapping(value = "/{permissaoId}")
	public ResponseEntity<Void> desassociarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {	
		cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}

}