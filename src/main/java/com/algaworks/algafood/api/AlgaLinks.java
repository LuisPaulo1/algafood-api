package com.algaworks.algafood.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.controller.EstatisticasController;
import com.algaworks.algafood.api.controller.FluxoPedidoController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.GrupoController;
import com.algaworks.algafood.api.controller.GrupoPermissaoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.PermissaoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.RestauranteFormaPagamentoController;
import com.algaworks.algafood.api.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.controller.RestauranteProdutoFotoController;
import com.algaworks.algafood.api.controller.RestauranteUsuarioController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.controller.UsuarioGrupoController;

@Component
public class AlgaLinks {

	public static final TemplateVariables PAGINACAO_VARIABLES = new TemplateVariables(
			new TemplateVariable("page", VariableType.REQUEST_PARAM),
			new TemplateVariable("size", VariableType.REQUEST_PARAM),
			new TemplateVariable("sort", VariableType.REQUEST_PARAM));

	public Link linkToPedidos(String rel) {
		TemplateVariables filtroVariables = new TemplateVariables(
				new TemplateVariable("clienteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM));

		String pedidosUrl = linkTo(PedidoController.class).toUri().toString();

		return Link.of(UriTemplate.of(pedidosUrl, PAGINACAO_VARIABLES.concat(filtroVariables)), rel);
	}

	//Pedido - confirmação, entrega e cancelamento
	public Link linkToConfirmacaoPedido(String codigoPedido, String rel) {
		return linkTo(methodOn(FluxoPedidoController.class).confirmar(codigoPedido)).withRel(rel);
	}

	public Link linkToEntregaPedido(String codigoPedido, String rel) {
		return linkTo(methodOn(FluxoPedidoController.class).entregar(codigoPedido)).withRel(rel);
	}

	public Link linkToCancelamentoPedido(String codigoPedido, String rel) {
		return linkTo(methodOn(FluxoPedidoController.class).cancelar(codigoPedido)).withRel(rel);
	}
	
	//Restaurantes
	public Link linkToRestaurante(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteController.class).buscar(restauranteId)).withRel(rel);
	}

	public Link linkToRestaurante(Long restauranteId) {
		return linkToRestaurante(restauranteId, IanaLinkRelations.SELF.value());
	}

	public Link linkToRestaurantes(String rel) {
		return linkTo(RestauranteController.class).withRel(rel);
	}

	public Link linkToRestaurantes() {
		return linkToRestaurantes(IanaLinkRelations.SELF.value());
	}
	
	//Responsaveis restaurante	
	public Link linkToResponsaveisRestaurante(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteUsuarioController.class).listar(restauranteId)).withRel(rel);
	}

	public Link linkToResponsaveisRestaurante(Long restauranteId) {
		return linkToResponsaveisRestaurante(restauranteId, IanaLinkRelations.SELF.value());
	}

	public Link linkToResponsaveisRestauranteDesassociacao(Long restauranteId, Long usuarioId, String rel) {
		return linkTo(methodOn(RestauranteUsuarioController.class).desassociarResponsavel(restauranteId, usuarioId)).withRel(rel);
	}
	
	public Link linkToResponsaveisRestauranteAssociacao(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteUsuarioController.class).associarResponsavel(restauranteId, null)).withRel(rel);
	}
	
	//Restaurante - Forma de Pagamento
	public Link linkToRestauranteFormasPagamento(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteFormaPagamentoController.class).listar(restauranteId)).withRel(rel);
	}

	public Link linkToRestauranteFormasPagamento(Long restauranteId) {
		return linkToRestauranteFormasPagamento(restauranteId, IanaLinkRelations.SELF.value());
	}

	public Link linkToRestauranteFormasPagamentoAssociacao(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteFormaPagamentoController.class).associar(restauranteId, null)).withRel(rel);
	}

	public Link linkToRestauranteFormasPagamentoDesassociacao(Long restauranteId, Long formaPagamentoId, String rel) {
		return linkTo(methodOn(RestauranteFormaPagamentoController.class).desassociar(restauranteId, formaPagamentoId))
				.withRel(rel);
	}
	
	//Forma de Pagamento
	public Link linkToFormasPagamento(String rel) {
		return linkTo(FormaPagamentoController.class).withRel(rel);
	}

	public Link linkToFormasPagamento() {
		return linkToFormasPagamento(IanaLinkRelations.SELF.value());
	}

	public Link linkToFormaPagamento(Long formaPagamentoId, String rel) {
		return linkTo(FormaPagamentoController.class).slash(formaPagamentoId).withRel(rel);
	}

	public Link linkToFormaPagamento(Long formaPagamentoId) {
		return linkToFormaPagamento(formaPagamentoId, IanaLinkRelations.SELF.value());
	}

	//Restaurantes - abrir, fechar, ativa, inativar
	public Link linkToRestauranteAbertura(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteController.class).abrir(restauranteId)).withRel(rel);
	}

	public Link linkToRestauranteFechamento(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteController.class).fechar(restauranteId)).withRel(rel);
	}

	public Link linkToRestauranteInativacao(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteController.class).inativar(restauranteId)).withRel(rel);
	}

	public Link linkToRestauranteAtivacao(Long restauranteId, String rel) {
		return linkTo(methodOn(RestauranteController.class).ativar(restauranteId)).withRel(rel);
	}
	
	//Cozinhas
	public Link linkToCozinhas() {
		return linkToCozinhas(IanaLinkRelations.SELF.value());
	}

	public Link linkToCozinhas(String rel) {
		return linkTo(CozinhaController.class).withRel(rel);
	}

	public Link linkToCozinha(Long cozinhaId, String rel) {
		return linkTo(methodOn(CozinhaController.class).buscar(cozinhaId)).withRel(rel);
	}

	public Link linkToCozinha(Long cozinhaId) {
		return linkToCozinha(cozinhaId, IanaLinkRelations.SELF.value());
	}
	
	//Usuarios
	public Link linkToUsuario(Long usuarioId) {
		return linkToUsuario(usuarioId, IanaLinkRelations.SELF.value());
	}

	public Link linkToUsuario(Long usuarioId, String rel) {
		return linkTo(methodOn(UsuarioController.class).buscar(usuarioId)).withRel(rel);
	}

	public Link linkToUsuarios(String rel) {
		return linkTo(UsuarioController.class).withRel(rel);
	}

	public Link linkToUsuarios() {
		return linkToUsuarios(IanaLinkRelations.SELF.value());
	}
	
	//GrupoUsuario
	public Link linkToGruposUsuario(Long usuarioId, String rel) {
		return linkTo(methodOn(UsuarioGrupoController.class).listar(usuarioId)).withRel(rel);
	}

	public Link linkToGruposUsuario(Long usuarioId) {
		return linkToGruposUsuario(usuarioId, IanaLinkRelations.SELF.value());
	}
	
	public Link linkToUsuarioGrupoAssociacao(Long usuarioId, String rel) {
	    return linkTo(methodOn(UsuarioGrupoController.class)
	            .associarGrupo(usuarioId, null)).withRel(rel);
	}

	public Link linkToUsuarioGrupoDesassociacao(Long usuarioId, Long grupoId, String rel) {
	    return linkTo(methodOn(UsuarioGrupoController.class)
	            .desassociarGrupo(usuarioId, grupoId)).withRel(rel);
	}  
	
	//Grupos
	public Link linkToGrupos(String rel) {
		return linkTo(GrupoController.class).withRel(rel);
	}
	
	public Link linkToGrupos() {
		return linkTo(GrupoController.class).withRel(IanaLinkRelations.SELF.value());
	}
	
	public Link linkToGrupo(Long grupoId, String rel) {
		return linkTo(methodOn(GrupoController.class).buscar(grupoId)).withRel(rel);
	}
	
	public Link linkToGrupo(Long grupoId) {
		return linkTo(methodOn(GrupoController.class).buscar(grupoId)).withRel(IanaLinkRelations.SELF.value());
	}
	
	//Grupo Permissão
	public Link linkToGrupoPermissoes(Long grupoId, String rel) {
		return linkTo(methodOn(GrupoPermissaoController.class).listar(grupoId)).withRel(rel);
	}
	
	public Link linkToGrupoPermissoes(Long grupoId) {
	    return linkToGrupoPermissoes(grupoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToGrupoPermissaoAssociacao(Long grupoId, String rel) {
	    return linkTo(methodOn(GrupoPermissaoController.class)
	            .associarPermissao(grupoId, null)).withRel(rel);
	}

	public Link linkToGrupoPermissaoDesassociacao(Long grupoId, Long permissaoId, String rel) {
	    return linkTo(methodOn(GrupoPermissaoController.class)
	            .desassociarPermissao(grupoId, permissaoId)).withRel(rel);
	}
	
	//Permissões
	public Link linkToPermissoes(String rel) {
	    return linkTo(PermissaoController.class).withRel(rel);
	}

	public Link linkToPermissoes() {
	    return linkToPermissoes(IanaLinkRelations.SELF.value());
	}
	
	//Cidades
	public Link linkToCidades(String rel) {
		return linkTo(CidadeController.class).withRel(rel);
	}

	public Link linkToCidades() {
		return linkToCidades(IanaLinkRelations.SELF.value());
	}

	public Link linkToCidade(Long cidadeId) {
		return linkToCidade(cidadeId, IanaLinkRelations.SELF.value());
	}

	public Link linkToCidade(Long cidadeId, String rel) {
		return linkTo(methodOn(CidadeController.class).buscar(cidadeId)).withRel(rel);
	}
	
	//Estados
	public Link linkToEstados() {
		return linkToEstados(IanaLinkRelations.SELF.value());
	}

	public Link linkToEstados(String rel) {
		return linkTo(EstadoController.class).withRel(rel);
	}

	public Link linkToEstado(Long estadoId) {
		return linkToEstado(estadoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToEstado(Long estadoId, String rel) {
		return linkTo(methodOn(EstadoController.class).buscar(estadoId)).withRel(rel);
	}

	//Produtos
	public Link linkToProduto(Long restauranteId, Long produtoId, String rel) {
		return linkTo(methodOn(RestauranteProdutoController.class).buscar(restauranteId, produtoId)).withRel(rel);
	}

	public Link linkToProduto(Long restauranteId, Long produtoId) {
		return linkToProduto(restauranteId, produtoId, IanaLinkRelations.SELF.value());
	}
	
	public Link linkToProdutos(Long restauranteId, String rel) {
	    return linkTo(methodOn(RestauranteProdutoController.class)
	            .listar(restauranteId, null)).withRel(rel);
	}

	public Link linkToProdutos(Long restauranteId) {
	    return linkToProdutos(restauranteId, IanaLinkRelations.SELF.value());
	}
	
	//FotoProduto
	public Link linkToFotoProduto(Long restauranteId, Long produtoId, String rel) {
	    return linkTo(methodOn(RestauranteProdutoFotoController.class)
	            .buscarFoto(restauranteId, produtoId)).withRel(rel);
	}

	public Link linkToFotoProduto(Long restauranteId, Long produtoId) {
	    return linkToFotoProduto(restauranteId, produtoId, IanaLinkRelations.SELF.value());
	}
	
	//Estatísticas
	public Link linkToEstatisticas(String rel) {
	    return linkTo(EstatisticasController.class).withRel(rel);
	}

	public Link linkToEstatisticasVendasDiarias(String rel) {
	    TemplateVariables filtroVariables = new TemplateVariables(
	            new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
	            new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
	            new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM),
	            new TemplateVariable("timeOffset", VariableType.REQUEST_PARAM));
	    
	    String pedidosUrl = linkTo(methodOn(EstatisticasController.class)
	            .consultarVendasDiarias(null, null)).toUri().toString();
	    
	    return Link.of(UriTemplate.of(pedidosUrl, filtroVariables), rel);
	}       

}