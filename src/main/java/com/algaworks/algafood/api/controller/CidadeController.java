package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
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

import com.algaworks.algafood.api.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.assembler.GenericModelAssembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Cidades")
@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CadastroCidadeService cadastroCidade;
		
	@Autowired
	private GenericInputDisassembler<CidadeInput, Cidade> cidadeInputDisassembler;
	
	@Autowired
	private GenericModelAssembler<CidadeModel, Cidade> cidadeModelAssembler;
	
	@ApiOperation("Lista as cidades")
	@GetMapping
	public ResponseEntity<List<CidadeModel>> listar() {
		List<CidadeModel> cidades = cidadeModelAssembler
				.toCollectionModel(cadastroCidade.listar(), CidadeModel.class);		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
				.body(cidades);
	}

	@ApiOperation("Busca uma cidade por ID")
	@GetMapping("/{id}")
	public ResponseEntity<CidadeModel> buscar(@ApiParam("ID de uma cidade") @PathVariable Long id) {		
		CidadeModel cidade = cidadeModelAssembler.toModel(cadastroCidade.buscar(id), CidadeModel.class);		
		return ResponseEntity.ok(cidade);		
	}
	
	@ApiOperation("Cadastra uma cidade")
	@PostMapping
	public ResponseEntity<CidadeModel> adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput, Cidade.class);
		CidadeModel cidadeModel = cidadeModelAssembler.toModel(cadastroCidade.salvar(cidade), CidadeModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(cidadeModel);		
	}
	
	@ApiOperation("Atualiza uma cidade por ID")
	@PutMapping("/{id}")
	public ResponseEntity<CidadeModel> atualizar(@ApiParam("ID de uma cidade") @PathVariable Long id, @RequestBody @Valid CidadeInput cidadeInput) {
		Cidade cidadeAtual = cadastroCidade.buscar(id);		
		cidadeAtual.setEstado(new Estado());
		cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);		
		cidadeAtual = cadastroCidade.salvar(cidadeAtual);
		CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidadeAtual, CidadeModel.class);		
		return ResponseEntity.ok(cidadeModel);		
	}
	
	@ApiOperation("Exclui uma cidade por ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@ApiParam("ID de uma cidade") @PathVariable Long id) {		
		cadastroCidade.excluir(id);
		return ResponseEntity.noContent().build();		
	}

}
