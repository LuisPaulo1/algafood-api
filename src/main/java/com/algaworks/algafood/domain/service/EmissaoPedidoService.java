package com.algaworks.algafood.domain.service;

import static com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs.usandoFiltro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.ItemPedido;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class EmissaoPedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;	
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
		
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired 
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
		
	public Page<Pedido> pesquisar(PedidoFilter filtro, Pageable pageable){
		return pedidoRepository.findAll(usandoFiltro(filtro), pageable);
	}
	
	public Pedido buscar(String codigo) { 
		Pedido pedido = pedidoRepository.findByCodigo(codigo)
				.orElseThrow(() -> new PedidoNaoEncontradoException(codigo));
		return pedido;
	}
	
	@Transactional
	public Pedido emitir(Pedido pedido) {		
		
		validarPedido(pedido);
		normalizarItens(pedido);
		validarItens(pedido);
		
		pedido.definirFrete();
		pedido.calcularValorTotal();
		
		return pedidoRepository.save(pedido);		
	}
	
	private void validarPedido(Pedido pedido) {
		
		Long cidadeId = pedido.getEnderecoEntrega().getCidade().getId();
		Long clienteId = pedido.getCliente().getId();
		Long restauranteId = pedido.getRestaurante().getId();
		Long formaPagamentoId = pedido.getFormaPagamento().getId();		
		
		Cidade cidade = cadastroCidadeService.buscar(cidadeId);
		Usuario cliente = cadastroUsuarioService.buscar(clienteId);
		Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscar(formaPagamentoId);
		
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);		
		
		if(restaurante.naoAceitaFormaPagamento(formaPagamento)) 
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.", formaPagamento.getDescricao()));
	}
	
	private void normalizarItens(Pedido pedido) {
		
		Set<ItemPedido> itensNormalizados = new HashSet<>();

		for (ItemPedido item : pedido.getItens()) {
			Optional<ItemPedido> itemNormalizadoOpt = itensNormalizados.stream()
					.filter(itemNormalizado -> itemNormalizado.getProduto().equals(item.getProduto()))
					.findFirst();

			itemNormalizadoOpt.ifPresentOrElse(
					itemNormalizado -> mesclarItens(itemNormalizado, item), () -> itensNormalizados.add(item));
		}
			
		pedido.setItens(new ArrayList<>(itensNormalizados));
	}
	
	private void mesclarItens(ItemPedido itemNormalizado, ItemPedido itemRepetido) {
		itemNormalizado.setQuantidade(itemNormalizado.getQuantidade() + itemRepetido.getQuantidade());
		itemNormalizado.setObservacao(
				(itemNormalizado.getObservacao() == null ? "" : itemNormalizado.getObservacao()) + 
				(itemRepetido.getObservacao() == null ? "" : " / "+itemRepetido.getObservacao())
				);
	}
	
	private void validarItens(Pedido pedido) {
		Long restauranteId = pedido.getRestaurante().getId();
		
		pedido.getItens().forEach(item -> { 	
			Produto produto = produtoRepository.findById(restauranteId, item.getProduto().getId())
					.orElseThrow(() -> new NegocioException(
			 				String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d", item.getProduto().getId(), restauranteId)));
				
				item.setProduto(produto);
				item.setPrecoUnitario(produto.getPreco());
				item.setPedido(pedido);
				item.calcularPrecoTotal();
			});	
	}
}
