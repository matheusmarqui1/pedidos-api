package marqui.matheus.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemPedidoDTO {
    @NotEmpty(message = "O id do produto deve estar presente no item.")
    private Integer produto;
    @NotNull(message = "A quantidade do produto deve estar presente no item.")
    private Integer quantidade;
}
