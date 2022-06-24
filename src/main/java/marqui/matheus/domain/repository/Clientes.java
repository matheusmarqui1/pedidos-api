package marqui.matheus.domain.repository;

import marqui.matheus.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Clientes extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNomeContainingIgnoreCase(String needle);

    List<Cliente> findByNomeOrId(String nome, Integer id);

    Cliente findOneByNome(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    @Query("select c from Cliente c left join fetch c.pedidos where c.id = :clienteId")
    Cliente findClienteFetchPedidos(@Param("clienteId") Integer clienteId);
}
