package com.algaworks.algafood.api.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(path =  "/restaurantes/{restauranteId}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	
	@Autowired
	private GenericInputDisassembler<ProdutoInput, Produto> produtoInputDisassembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public ResponseEntity<CollectionModel<ProdutoModel>> listar(@PathVariable Long restauranteId, 
			@RequestParam(name = "incluirInativos", required = false) Boolean incluirInativos){
		Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);	
		
		List<Produto> produtos = null;
		
		if(incluirInativos)
			produtos = cadastroProdutoService.buscarTodosPorRestaurante(restaurante);
		else
			produtos = cadastroProdutoService.buscarAtivosPorRestaurante(restaurante);
		
		CollectionModel<ProdutoModel> produtosModel = produtoModelAssembler.toCollectionModel(produtos);
		produtosModel.add(algaLinks.linkToProdutos(restauranteId));
		return ResponseEntity.ok(produtosModel);
	}
	
	@GetMapping("/{produtoId}")
	public ResponseEntity<ProdutoModel> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
		Produto produto = cadastroProdutoService.buscar(restauranteId, produtoId);		
		ProdutoModel produtoModel = produtoModelAssembler.toModel(produto);		
		return ResponseEntity.ok(produtoModel);
 	}
	
	@PostMapping
	public ResponseEntity<ProdutoModel> adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {
		Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);		
		Produto produto = produtoInputDisassembler.toDomainObject(produtoInput, Produto.class);
		produto.setRestaurante(restaurante);		
		produto = cadastroProdutoService.salvar(produto);		
		ProdutoModel produtoModel = produtoModelAssembler.toModel(produto);		
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoModel); 		
	}
	
	@PutMapping("/{produtoId}")
	public ResponseEntity<ProdutoModel> atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInput produtoInput){
		Produto produtoAtual = cadastroProdutoService.buscar(restauranteId, produtoId);		
		produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);		
		produtoAtual = cadastroProdutoService.salvar(produtoAtual);		
		ProdutoModel produtoModel = produtoModelAssembler.toModel(produtoAtual);		
		return ResponseEntity.ok(produtoModel);
	}
	
	@DeleteMapping("/{produtoId}")
	public ResponseEntity<Void> remover(@PathVariable Long produtoId){		
		Produto produto = cadastroProdutoService.buscar(produtoId);
		cadastroProdutoService.removerProduto(produto.getId());
		return ResponseEntity.noContent().build();
	}
}
