package marqui.matheus.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoItemPedidoDTO {
    private String sku;
    private String descricaoProduto;
    private Double precoUnitario;
    private Integer quantidade;
}
