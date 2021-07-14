package com.algaworks.algafood.api.v1.controller;

import java.math.BigDecimal;
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

import com.algaworks.algafood.api.v1.assembler.GenericModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
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
	private GenericModelAssembler<RestauranteModel, Restaurante> genericRestauranteModelAssembler; 
	
	@Autowired	
	private RestauranteInputDisassembler restauranteInputDisassembler;	
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<RestauranteBasicoModel>> listar() {
		CollectionModel<RestauranteBasicoModel> restaurantes = restauranteBasicoModelAssembler.toCollectionModel(cadastroRestaurante.listar());		
		return ResponseEntity.ok(restaurantes);
	}

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping(value = "/{id}")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long id) {		
		RestauranteModel restaurante = restauranteModelAssembler.toModel(cadastroRestaurante.buscar(id));
		return ResponseEntity.ok(restaurante);		
	}
	
	@GetMapping(value = "/por-nome")
	public ResponseEntity<List<RestauranteModel>> buscarPorNome(@RequestParam("nome") String nome, @RequestParam("id") Long id) {
		List<RestauranteModel> restaurantes = genericRestauranteModelAssembler.toCollectionModel(cadastroRestaurante.buscarPorNome(nome, id), RestauranteModel.class);
		return ResponseEntity.ok(restaurantes);		
	}
	
	@GetMapping("/por-nome-e-frete")
	public ResponseEntity<List<RestauranteModel>> buscarPorNomeFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {		
		List<RestauranteModel> restaurantes = genericRestauranteModelAssembler.toCollectionModel(cadastroRestaurante.buscarPorNomeFrete(nome, taxaFreteInicial, taxaFreteFinal), RestauranteModel.class);
		return ResponseEntity.ok(restaurantes);
	}
	
	@GetMapping("/por-frete-gratis")
	public ResponseEntity<List<RestauranteModel>> buscarComFreteGratis(@RequestParam("nome") String nome) {
		List<RestauranteModel> restaurantes = genericRestauranteModelAssembler.toCollectionModel(cadastroRestaurante.buscarComFreteGratis(nome), RestauranteModel.class);
		return ResponseEntity.ok(restaurantes);
	}
	
	@GetMapping("/buscar-o-primeiro")
	public ResponseEntity<RestauranteModel> buscarOPrimeiro() {
	 	RestauranteModel restaurante = genericRestauranteModelAssembler.toModel(cadastroRestaurante.buscarOPrimeiro().get(), RestauranteModel.class);  
	 	return ResponseEntity.ok(restaurante);
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PostMapping
	public ResponseEntity<RestauranteModel> adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {	
		Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);		
		RestauranteModel restauranteModel =  restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		return ResponseEntity.status(HttpStatus.CREATED).body(restauranteModel);		
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PutMapping(value = "/{id}")
	public ResponseEntity<RestauranteModel> atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {	
		Restaurante restauranteAtual = cadastroRestaurante.buscar(id);
		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);		
		RestauranteModel restauranteModel = restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		return ResponseEntity.ok(restauranteModel);
	}	
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PutMapping(value = "/{id}/ativo")
	public ResponseEntity<Void> ativar(@PathVariable Long id){
		cadastroRestaurante.ativar(id);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@DeleteMapping(value = "/{id}/ativo")
	public ResponseEntity<Void> inativar(@PathVariable Long id){
		cadastroRestaurante.inativar(id);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PutMapping(value = "/ativacoes")
	public ResponseEntity<Void> ativarMultiplos(@RequestBody List<Long> restauranteIds){
		cadastroRestaurante.ativar(restauranteIds);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@DeleteMapping(value = "/inativacoes")
	public ResponseEntity<Void> inativarMultiplos(@RequestBody List<Long> restauranteIds){
		cadastroRestaurante.inativar(restauranteIds);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PutMapping(value = "/{id}/abertura")
	public ResponseEntity<Void> abrir(@PathVariable Long id){
		cadastroRestaurante.abrirRestaurante(id);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeEditar
	@PutMapping(value = "/{id}/fechamento")
	public ResponseEntity<Void> fechar(@PathVariable Long id){
		cadastroRestaurante.fecharRestaurante(id);
		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.Restaurantes.PodeEditar
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {		
		cadastroRestaurante.excluir(id);
		return ResponseEntity.noContent().build();
	}	
}
