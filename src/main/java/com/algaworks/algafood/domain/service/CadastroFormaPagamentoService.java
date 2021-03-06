package com.algaworks.algafood.domain.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class CadastroFormaPagamentoService {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;

	public List<FormaPagamento> listar() {
		return formaPagamentoRepository.findAll();
	}

	public FormaPagamento buscar(Long id) {
		FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
				.orElseThrow(() -> new FormaPagamentoNaoEncontradaException(id));
		return formaPagamento;
	}
	
	public OffsetDateTime buscarDataUltimaAtualizacao() {
		return formaPagamentoRepository.getDataUltimaAtualizacao();
	}
	
	public OffsetDateTime getDataAtualizacaoById(Long id) {
		return formaPagamentoRepository.getDataAtualizacaoById(id);
	}	
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formaPagamentoRepository.save(formaPagamento);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			formaPagamentoRepository.deleteById(id);
			formaPagamentoRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new FormaPagamentoNaoEncontradaException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Forma de pagamento de código %d não pode ser removido, pois está em uso", id));
		}
	}
}
