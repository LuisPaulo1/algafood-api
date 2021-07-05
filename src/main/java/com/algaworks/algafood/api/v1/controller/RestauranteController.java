package com.algaworks.algafood.api.v1.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler; 
	
	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;  
	
	@Autowired	
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@GetMapping
	public ResponseEntity<CollectionModel<RestauranteBasicoModel>> listar() {
		CollectionModel<RestauranteBasicoModel> restaurantes = restauranteBasicoModelAssembler.toCollectionModel(cadastroRestaurante.listar());		
		return ResponseEntity.ok(restaurantes);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long id) {		
		RestauranteModel restaurante = restauranteModelAssembler.toModel(cadastroRestaurante.buscar(id));
		return ResponseEntity.ok(restaurante);		
	}
	
	@GetMapping(value = "/por-nome")
	public ResponseEntity<List<Restaurante>> buscarPorNome(@RequestParam("nome") String nome, @RequestParam("id") Long id) {
		List<Restaurante> restaurantes = cadastroRestaurante.buscarPorNome(nome, id);
		return ResponseEntity.ok(restaurantes);		
	}
	
	@GetMapping("/por-nome-e-frete")
	public List<Restaurante> buscarPorNomeFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		return cadastroRestaurante.buscarPorNomeFrete(nome, taxaFreteInicial, taxaFreteFinal);
	}
	
	@GetMapping("/por-frete-gratis")
	public List<Restaurante> buscarComFreteGratis(@RequestParam("nome") String nome) {
		return cadastroRestaurante.buscarComFreteGratis(nome);
	}
	
	@GetMapping("/buscar-o-primeiro")
	public Optional<Restaurante> buscarOPrimeiro() {
		return cadastroRestaurante.buscarOPrimeiro();
	}
	
	@PostMapping
	public ResponseEntity<RestauranteModel> adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {	
		Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);		
		RestauranteModel restauranteModel =  restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		return ResponseEntity.status(HttpStatus.CREATED).body(restauranteModel);		
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<RestauranteModel> atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {	
		Restaurante restauranteAtual = cadastroRestaurante.buscar(id);
		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);		
		RestauranteModel restauranteModel = restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		return ResponseEntity.ok(restauranteModel);
	}	
	
	@PutMapping(value = "/{id}/ativo")
	public ResponseEntity<Void> ativar(@PathVariable Long id){
		cadastroRestaurante.ativar(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{id}/ativo")
	public ResponseEntity<Void> inativar(@PathVariable Long id){
		cadastroRestaurante.inativar(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/ativacoes")
	public ResponseEntity<Void> ativarMultiplos(@RequestBody List<Long> restauranteIds){
		cadastroRestaurante.ativar(restauranteIds);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/inativacoes")
	public ResponseEntity<Void> inativarMultiplos(@RequestBody List<Long> restauranteIds){
		cadastroRestaurante.inativar(restauranteIds);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}/abertura")
	public ResponseEntity<Void> abrir(@PathVariable Long id){
		cadastroRestaurante.abrirRestaurante(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}/fechamento")
	public ResponseEntity<Void> fechar(@PathVariable Long id){
		cadastroRestaurante.fecharRestaurante(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroRestaurante.excluir(id);
		return ResponseEntity.noContent().build();
	}	
}
