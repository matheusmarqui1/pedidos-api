package marqui.matheus.service;

import marqui.matheus.api.dto.PedidoDTO;
import marqui.matheus.domain.entity.Pedido;
import marqui.matheus.domain.enums.StatusPedido;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO pedidoDTO);

    Optional<Pedido> obterPedidoCompleto(Integer pedidoId);

    Pedido atualizaStatus(Integer pedidoId, StatusPedido statusPedido);
}
