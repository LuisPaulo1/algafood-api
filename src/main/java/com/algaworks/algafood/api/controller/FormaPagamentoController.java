package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formaPagamentos")
public class FormaPagamentoController {

	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private GenericInputDisassembler<FormaPagamentoInput, FormaPagamento> formaPagamentoInputDisassembler;
	
	@Autowired
	private GenericModelAssembler<FormaPagamentoModel, FormaPagamento> formaPagamentoModelAssembler;
	
	@GetMapping
	public ResponseEntity<List<FormaPagamentoModel>> listar(){
		List<FormaPagamentoModel> formaPagamentos = formaPagamentoModelAssembler.toCollectionModel(cadastroFormaPagamento.listar(), FormaPagamentoModel.class);		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.body(formaPagamentos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long id){
		FormaPagamentoModel formaPagamento = formaPagamentoModelAssembler.toModel(cadastroFormaPagamento.buscar(id), FormaPagamentoModel.class);		
		return ResponseEntity.ok()				
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))			
				.body(formaPagamento);		
	}
	
	@PostMapping	
	public ResponseEntity<FormaPagamentoModel> adicionar(@RequestBody FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput, FormaPagamento.class);
		FormaPagamentoModel formaPagamentoModel =  formaPagamentoModelAssembler.toModel(cadastroFormaPagamento.salvar(formaPagamento), FormaPagamentoModel.class); 
		return ResponseEntity.status(HttpStatus.CREATED).body(formaPagamentoModel);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FormaPagamentoModel> atualizar(@PathVariable Long id, @RequestBody FormaPagamentoInput formaPagamentoInput){		
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscar(id);		
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);				
		FormaPagamentoModel formaPagamento = formaPagamentoModelAssembler.toModel(cadastroFormaPagamento.salvar(formaPagamentoAtual), FormaPagamentoModel.class);		
		return ResponseEntity.ok(formaPagamento);					
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroFormaPagamento.excluir(id);
		return ResponseEntity.noContent().build();		
	}
}
