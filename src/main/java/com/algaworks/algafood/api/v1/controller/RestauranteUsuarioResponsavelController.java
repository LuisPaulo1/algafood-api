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
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/responsaveis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioResponsavelController implements RestauranteUsuarioResponsavelControllerOpenApi {
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
    private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<UsuarioModel>> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);
		
		CollectionModel<UsuarioModel> usuariosModel = usuarioModelAssembler
				.toCollectionModel(restaurante.getResponsaveis())
				.removeLinks();
		
		usuariosModel.add(algaLinks.linkToRestauranteResponsaveis(restauranteId));
		
		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			usuariosModel.add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));
	
			usuariosModel.getContent().stream().forEach(usuarioModel -> {
				usuarioModel.add(algaLinks.linkToRestauranteResponsavelDesassociacao(
						restauranteId, usuarioModel.getId(), "desassociar"));
			});
		}
		
		return ResponseEntity.ok(usuariosModel);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping(value = "/{usuarioId}")
	public ResponseEntity<Void> associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
		cadastroRestauranteService.associarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping(value = "/{usuarioId}")
	public ResponseEntity<Void> desassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
		cadastroRestauranteService.desassociarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}

}
