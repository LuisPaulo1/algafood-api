package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.assembler.GenericModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;

@RestController
@RequestMapping(path = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {
	
	@Autowired
	private EmissaoPedidoService emisaoPedidoService;
	
	@Autowired
	private GenericModelAssembler<PedidoModel, Pedido> pedidoModelAssembler;
	
	@Autowired
	private GenericModelAssembler<PedidoResumoModel, Pedido> pedidoResumoModelAssembler;
	
	@Autowired
	private GenericInputDisassembler<PedidoInput, Pedido> pedidoInputDisassembler;	
	
	@GetMapping
	public ResponseEntity<Page<PedidoResumoModel>> pesquisar(PedidoFilter filtro, @PageableDefault(size = 10) Pageable pageable){	
		pageable = traduzirPageable(pageable);		
		Page<Pedido> pedidosPage = emisaoPedidoService.pesquisar(filtro, pageable);		
		List<PedidoResumoModel> pedidosResumoModel = pedidoResumoModelAssembler.toCollectionModel(pedidosPage.getContent(), PedidoResumoModel.class);
		Page<PedidoResumoModel> pedidosResumoModelPage = new PageImpl<>(pedidosResumoModel, pageable, pedidosPage.getTotalElements());	
		return ResponseEntity.ok(pedidosResumoModelPage);
	}
	
	@GetMapping(value = "/{codigo}")
	public ResponseEntity<PedidoModel> buscar(@PathVariable String codigo){
		Pedido pedido = emisaoPedidoService.buscar(codigo);
		PedidoModel pedidoModel = pedidoModelAssembler.toModel(pedido, PedidoModel.class);
		return ResponseEntity.ok(pedidoModel);
	}
	
	@PostMapping
	public ResponseEntity<PedidoModel> adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
		Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput, Pedido.class);
		
        // TODO pegar usuário autenticado
        novoPedido.setCliente(new Usuario());
        novoPedido.getCliente().setId(1L);
		
		novoPedido = emisaoPedidoService.emitir(novoPedido);
		
		PedidoModel pedidoModel = pedidoModelAssembler.toModel(novoPedido, PedidoModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(pedidoModel);
	}
	
	private Pageable traduzirPageable(Pageable apiPageable) {
		var mapeamento = Map.of(
				"codigo", "codigo",
				"subtotal", "subtotal",
				"taxaFrete", "taxaFrete",
				"valorTotal", "valorTotal",
				"dataCriacao", "dataCriacao",
				"restaurante.nome", "restaurante.nome",
				"restaurante.id", "restaurante.id",
				"cliente.id", "cliente.id",
				"cliente.nome", "cliente.nome"
			);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
}
