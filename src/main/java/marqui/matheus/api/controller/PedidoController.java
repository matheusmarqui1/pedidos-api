package marqui.matheus.api.controller;

import marqui.matheus.api.dto.AtualizacaoStatusPedidoDTO;
import marqui.matheus.api.dto.InformacaoItemPedidoDTO;
import marqui.matheus.api.dto.InformacoesPedidoDTO;
import marqui.matheus.api.dto.PedidoDTO;
import marqui.matheus.domain.entity.ItemPedido;
import marqui.matheus.domain.entity.Pedido;
import marqui.matheus.domain.entity.Produto;
import marqui.matheus.domain.enums.StatusPedido;
import marqui.matheus.domain.repository.Pedidos;
import marqui.matheus.exception.RegraNegocioException;
import marqui.matheus.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private Pedidos pedidosRepository;
    private PedidoService service;

    @Autowired
    public PedidoController(PedidoService service, Pedidos pedidosRepository) {
        this.service = service;
        this.pedidosRepository = pedidosRepository;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer salvarPedido( @RequestBody @Valid PedidoDTO pedidoDTO ){
        Pedido pedido = service.salvar(pedidoDTO);
        return pedido.getId();
    }

//    @GetMapping("/{id}")
//    public Pedido getPedidoById(@PathVariable Integer id) {
//        Pedido pedido = pedidosRepository.findById(id).orElseThrow(
//                () -> new RegraNegocioException("O id do pedido não existe."));
//        pedido.setQuantidadeTotalDeItens(pedido.getItens()
//                .parallelStream()
//                .reduce(0,
//                        (soma, itemPedido) -> soma + itemPedido.getQuantidade(),
//                        Integer::sum)
//        );
//        return pedido;
//    }

    @GetMapping("/{pedidoId}")
    public InformacoesPedidoDTO getById(@PathVariable Integer pedidoId) {
        return service.obterPedidoCompleto(pedidoId)
                .map(pedido -> converterPedidoParaDTO(pedido))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    private InformacoesPedidoDTO converterPedidoParaDTO(Pedido pedido) {
        return InformacoesPedidoDTO.builder()
                .id(pedido.getId())
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .dataPedido(pedido.getDataPedido())
                .quantidadeTotalItens(pedido.getItens().size())
                .status(pedido.getStatus().name())
                .itens(converterItensPedidoParaDTO(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converterItensPedidoParaDTO(List<ItemPedido> itens) {
        if(CollectionUtils.isEmpty(itens)) {
            return Collections.EMPTY_LIST;
        }
        return itens.stream()
                .map(itemPedido -> {
                    return InformacaoItemPedidoDTO.builder()
                            .descricaoProduto(itemPedido.getProduto().getDescricao())
                            .precoUnitario(itemPedido.getProduto().getPrecoUnitario())
                            .quantidade(itemPedido.getQuantidade())
                            .sku(itemPedido.getProduto().getSku())
                            .build();
                }).collect(Collectors.toList());
    }

    @PatchMapping("/{pedidoId}")
    public InformacoesPedidoDTO updateStatus(
            @PathVariable Integer pedidoId,
            @RequestBody AtualizacaoStatusPedidoDTO atualizacaoStatusPedidoDTO) {
        try{
            return converterPedidoParaDTO(service.atualizaStatus(pedidoId,
                    StatusPedido.valueOf(atualizacaoStatusPedidoDTO
                            .getNovoStatus().toUpperCase(Locale.ROOT))
            ));
        }catch (IllegalArgumentException ex){
            throw new ResponseStatusException(BAD_REQUEST, "O código de status não existe.");
        }
    }
}
