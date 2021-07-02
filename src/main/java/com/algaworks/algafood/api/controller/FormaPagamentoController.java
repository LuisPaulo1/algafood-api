package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formaPagamentos")
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private GenericInputDisassembler<FormaPagamentoInput, FormaPagamento> formaPagamentoInputDisassembler;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@GetMapping
	public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request){
		
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = cadastroFormaPagamento.buscarDataUltimaAtualizacao();
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return null;
		}		
		
		CollectionModel<FormaPagamentoModel> formaPagamentos = formaPagamentoModelAssembler
				.toCollectionModel(cadastroFormaPagamento.listar());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.eTag(eTag)
				.body(formaPagamentos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long id, ServletWebRequest request) {		
		
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

		String eTag = "0";
	
		OffsetDateTime dataUltimaAtualizacao = cadastroFormaPagamento.getDataAtualizacaoById(id);
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return null;
		}		
		
		FormaPagamentoModel formaPagamento = formaPagamentoModelAssembler.toModel(cadastroFormaPagamento.buscar(id));		
		
		return ResponseEntity.ok()				
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))			
				.body(formaPagamento);		
	}
	
	@PostMapping	
	public ResponseEntity<FormaPagamentoModel> adicionar(@RequestBody FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput, FormaPagamento.class);
		FormaPagamentoModel formaPagamentoModel =  formaPagamentoModelAssembler.toModel(cadastroFormaPagamento.salvar(formaPagamento)); 
		return ResponseEntity.status(HttpStatus.CREATED).body(formaPagamentoModel);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FormaPagamentoModel> atualizar(@PathVariable Long id, @RequestBody FormaPagamentoInput formaPagamentoInput){		
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscar(id);		
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);				
		FormaPagamentoModel formaPagamento = formaPagamentoModelAssembler.toModel(cadastroFormaPagamento.salvar(formaPagamentoAtual));		
		return ResponseEntity.ok(formaPagamento);					
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroFormaPagamento.excluir(id);
		return ResponseEntity.noContent().build();		
	}
}
