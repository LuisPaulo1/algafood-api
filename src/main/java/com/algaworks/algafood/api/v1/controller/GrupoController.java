package com.algaworks.algafood.api.v1.controller;

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

import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;
import com.algaworks.algafood.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private GenericInputDisassembler<GrupoInput, Grupo> grupoInputDisassembler;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
			
	@GetMapping
	public ResponseEntity<CollectionModel<GrupoModel>> listar() {
		CollectionModel<GrupoModel> grupos = grupoModelAssembler.toCollectionModel(cadastroGrupo.listar());				
		return ResponseEntity.ok(grupos);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<GrupoModel> buscar(@PathVariable Long id) {		
		GrupoModel grupo = grupoModelAssembler.toModel(cadastroGrupo.buscar(id));
		return ResponseEntity.ok(grupo);		
	}

	@PostMapping
	public ResponseEntity<GrupoModel> adicionar(@RequestBody @Valid GrupoInput grupoInput) {		
		Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput, Grupo.class);		
		GrupoModel grupoModel = grupoModelAssembler.toModel(cadastroGrupo.salvar(grupo));		
		return ResponseEntity.status(HttpStatus.CREATED).body(grupoModel);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<GrupoModel> atualizar(@PathVariable Long id, @RequestBody @Valid GrupoInput grupoInput) {		
		Grupo grupoAtual = cadastroGrupo.buscar(id);		
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);		
		GrupoModel grupoModel = grupoModelAssembler.toModel(cadastroGrupo.salvar(grupoAtual));		
		return ResponseEntity.ok(grupoModel);		
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroGrupo.excluir(id);
		return ResponseEntity.noContent().build();
	}
}