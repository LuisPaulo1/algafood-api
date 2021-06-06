package com.algaworks.algafood.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {

	
	// Errata: se um restaurante não tiver nenhuma forma de pagamento associada a ele,
	// esse restaurante não será retornado usando JOIN FETCH r.formasPagamento.
	// Para resolver isso, temos que usar LEFT JOIN FETCH r.formasPagamento
	//@Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
	@Query("select distinct r from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();
	
	//@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
	//A consulta tá no orm.xml
	List<Restaurante> consultarPorNome(@Param("nome") String nome, @Param("id") Long id);	
}
