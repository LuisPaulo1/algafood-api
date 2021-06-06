
package com.algaworks.algafood.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.core.util.ClassNameForException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
	@Autowired
	private RestauranteRepository restauranteRepository; 
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	public List<Restaurante> listar(){
		 return restauranteRepository.findAll();		
	}
	
	public Restaurante buscar(Long id) {
		Restaurante restaurante = restauranteRepository.findById(id)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(id));
		return restaurante;
	}
	
	public List<Restaurante> buscarPorNome(String nome, Long id){
		 return restauranteRepository.consultarPorNome(nome, id);		
	}
	
	public List<Restaurante> buscarPorNomeFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		 return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
	}
	
	public List<Restaurante> buscarComFreteGratis(String nome){
		return restauranteRepository.findComFreteGratis(nome);
	}
	
	public Optional<Restaurante> buscarOPrimeiro(){
		return restauranteRepository.buscarPrimeiro();
	}
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
				.orElseThrow(() -> new NegocioException(ClassNameForException.getNameClass(restaurante.getCozinha()), cozinhaId));
		
		Cidade cidade = cidadeRepository.findById(cidadeId)
				.orElseThrow(() -> new NegocioException(ClassNameForException.getNameClass(restaurante.getEndereco().getCidade()), cidadeId));
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		
		return restauranteRepository.save(restaurante);
	}	
	
	
	@Transactional
	public void ativar(Long id) {
		Restaurante restauranteAtual = buscar(id);		
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long id) {
		Restaurante restauranteAtual = buscar(id);		
		restauranteAtual.inativar();
	}
	
	@Transactional
	public void ativar(List<Long> restauranteIds) {
		try {
			restauranteIds.forEach(this::ativar);
		}catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@Transactional
	public void inativar(List<Long> restauranteIds) {
		try {
			restauranteIds.forEach(this::inativar);
		}catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}	
	
	@Transactional
	public void abrirRestaurante(Long id) {
		Restaurante restaurante = buscar(id);
		restaurante.abrir();
	}
	
	@Transactional
	public void fecharRestaurante(Long id) {
		Restaurante restaurante = buscar(id);
		restaurante.fechar();
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscar(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
		
		restaurante.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscar(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
		
		restaurante.adicionarFormaPagamento(formaPagamento);
	}	
	
	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscar(restauranteId);
		Usuario usuario = cadastroUsuarioService.buscar(usuarioId);
		restaurante.associar(usuario);
	}
	
	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscar(restauranteId);
		Usuario usuario = cadastroUsuarioService.buscar(usuarioId);
		restaurante.desassociar(usuario);
	}
		
	@Transactional
	public void excluir(Long id) {
		try {
			restauranteRepository.deleteById(id);
			restauranteRepository.flush();
		}catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(id);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Restaurante de código %d não pode ser removido, pois está em uso", id));
		}
	}
	
}
