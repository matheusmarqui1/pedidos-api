package marqui.matheus.service.impl;

import lombok.RequiredArgsConstructor;
import marqui.matheus.api.dto.ItemPedidoDTO;
import marqui.matheus.api.dto.PedidoDTO;
import marqui.matheus.domain.entity.Cliente;
import marqui.matheus.domain.entity.ItemPedido;
import marqui.matheus.domain.entity.Pedido;
import marqui.matheus.domain.entity.Produto;
import marqui.matheus.domain.enums.StatusPedido;
import marqui.matheus.domain.repository.Clientes;
import marqui.matheus.domain.repository.ItensPedido;
import marqui.matheus.domain.repository.Pedidos;
import marqui.matheus.domain.repository.Produtos;
import marqui.matheus.exception.PedidoNaoEncontradoException;
import marqui.matheus.exception.RegraNegocioException;
import marqui.matheus.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos pedidosRepository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    /*
        Transactional pois se ocorrer um erro nada é salvo
        ou commitado.
     */
    @Override
    @Transactional
    public Pedido salvar(PedidoDTO pedidoDTO) {
        Cliente cliente = clientesRepository
                .findById(pedidoDTO.getCliente())
                .orElseThrow(() -> new RegraNegocioException("Código do cliente é inexistente."));

        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedido = convertDtoItemsToItems(pedido, pedidoDTO.getItems());
        pedidosRepository.save(pedido);
        itensPedidoRepository.saveAll(itensPedido);
        pedido.setItens(itensPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer pedidoId) {
        return pedidosRepository.findByIdComItens(pedidoId);
    }

    @Override
    @Transactional
    public Pedido atualizaStatus(Integer pedidoId, StatusPedido statusPedido) {
        return pedidosRepository
                .findById(pedidoId)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidosRepository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> convertDtoItemsToItems(Pedido pedido, List<ItemPedidoDTO> itens) {
        if(itens.isEmpty())
            throw new RegraNegocioException("Os pedidos devem conter itens de pedido.");
        return itens
                .stream()
                .map(itemPedidoDTO -> {
                    Produto produtoOfCurrentItem = produtosRepository
                            .findById(itemPedidoDTO.getProduto())
                            .orElseThrow(
                                    () -> new RegraNegocioException("Código de produto inválido: " +
                                            itemPedidoDTO.getProduto()));
                    ItemPedido itemPedido =  new ItemPedido();
                    itemPedido.setQuantidade(itemPedidoDTO.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produtoOfCurrentItem);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
