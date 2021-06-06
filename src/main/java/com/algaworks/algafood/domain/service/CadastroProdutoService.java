package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class CadastroProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
                   
    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
    
    public Produto buscar(Long produtoId) {
        return produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));
    } 
    
    public Produto buscar(Long restauranteId, Long produtoId) {    	
        return produtoRepository.findById(restauranteId, produtoId)
            .orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }  
    
    public List<Produto> buscarTodosPorRestaurante(Restaurante restaurante){
    	List<Produto> produtos = produtoRepository.findAllByRestaurante(restaurante);
    	return produtos;
    }
    
    public List<Produto> buscarAtivosPorRestaurante(Restaurante restaurante){
    	List<Produto> produtos = produtoRepository.findAtivosByRestaurante(restaurante);
    	return produtos;
    }
    
    @Transactional
    public void removerProduto(Long produtoId) {
    	produtoRepository.deleteById(produtoId);
    }
}  
