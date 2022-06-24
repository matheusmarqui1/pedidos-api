package marqui.matheus.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesPedidoDTO {
    private Integer id;
    private String cpf;
    private String nomeCliente;
    private Double total;
    private LocalDate dataPedido;
    private List<InformacaoItemPedidoDTO> itens;
    private String status;
    private Integer quantidadeTotalItens;
}
