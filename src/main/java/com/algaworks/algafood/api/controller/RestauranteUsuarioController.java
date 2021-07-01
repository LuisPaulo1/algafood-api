package com.algaworks.algafood.api.controller;

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

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/responsaveis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioController implements RestauranteUsuarioResponsavelControllerOpenApi {
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
    private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public ResponseEntity<CollectionModel<UsuarioModel>> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);
		CollectionModel<UsuarioModel> responsaveis = usuarioModelAssembler.toCollectionModel(restaurante.getResponsaveis());		
		responsaveis.removeLinks().add(algaLinks.linkToResponsaveisRestaurante(restauranteId));
		return ResponseEntity.ok(responsaveis);
	}
	
	@PutMapping(value = "/{usuarioId}")
	public ResponseEntity<Void> associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
		cadastroRestauranteService.associarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{usuarioId}")
	public ResponseEntity<Void> desassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
		cadastroRestauranteService.desassociarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}

}
