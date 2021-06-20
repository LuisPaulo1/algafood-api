package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.api.openapi.controller.EstadoControllerOpenApi;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping(path = "/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EstadoControllerOpenApi {

	@Autowired
	private CadastroEstadoService cadastroEstado;
		
	@Autowired
	private GenericInputDisassembler<EstadoInput, Estado> estadoInputDisassembler;
	
	@Autowired
	private GenericModelAssembler<EstadoModel, Estado> estadoModelAssembler;

	@GetMapping
	public ResponseEntity<List<EstadoModel>> listar() {
		List<EstadoModel> estados = estadoModelAssembler.toCollectionModel(cadastroEstado.listar(), EstadoModel.class);		
		return ResponseEntity.ok(estados);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EstadoModel> buscar(@PathVariable Long id) {
		EstadoModel estado = estadoModelAssembler.toModel(cadastroEstado.buscar(id), EstadoModel.class);		
		return ResponseEntity.ok(estado);
	}

	@PostMapping
	public ResponseEntity<EstadoModel> adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput, Estado.class);
		EstadoModel estadoModel =  estadoModelAssembler.toModel(cadastroEstado.salvar(estado), EstadoModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(estadoModel);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EstadoModel> atualizar(@PathVariable Long id, @RequestBody @Valid EstadoInput estadoInput) {		
		Estado estadoAtual = cadastroEstado.buscar(id);
		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
		EstadoModel estadoModel = estadoModelAssembler.toModel(cadastroEstado.salvar(estadoAtual), EstadoModel.class);
		return ResponseEntity.ok(estadoModel);		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroEstado.excluir(id);
		return ResponseEntity.noContent().build();		
	}
}
