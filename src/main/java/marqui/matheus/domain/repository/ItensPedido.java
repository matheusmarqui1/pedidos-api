package marqui.matheus.domain.repository;

import marqui.matheus.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {

}
