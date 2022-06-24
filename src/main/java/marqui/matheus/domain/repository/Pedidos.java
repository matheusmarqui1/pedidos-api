package marqui.matheus.domain.repository;

import marqui.matheus.domain.entity.Cliente;
import marqui.matheus.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface Pedidos extends JpaRepository<Pedido, Integer> {
    @Query(" from Pedido p where p.cliente = :cliente ")
    List<Pedido> findPedidosByCliente(@Param("cliente") Cliente cliente);

    Set<Pedido> findByCliente(Cliente cliente);
    @Query(" from Pedido p left join fetch p.itens where p.id = :pedidoId")
    Optional<Pedido> findByIdComItens(@Param("pedidoId") Integer pedidoId);
}
