package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {
			
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	
	public Page<Cozinha> listar(Pageable pageable) {
		return cozinhaRepository.findAll(pageable);
	}
	
	public Cozinha buscar(Long id) {
		Cozinha cozinha = cozinhaRepository.findById(id)
				 .orElseThrow(() -> new CozinhaNaoEncontradaException(id));
		return cozinha;		
	}
	
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}	
	
	@Transactional
	public void excluir(Long id) {
		try {
			cozinhaRepository.deleteById(id);		
			cozinhaRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(id);		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Cozinha de código %d não pode ser removida, pois está em uso", id));
		}
	}

}
