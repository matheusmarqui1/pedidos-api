package marqui.matheus.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @Column(nullable = false, unique = true, length = 30)
    @NotEmpty(message = "{campo.sku.obrigatorio}")
    private String sku;

    @Column(name = "preco_unitario" , nullable = false)
    @NotNull(message = "{campo.preco.obrigatorio}")
    private Double precoUnitario;
}
