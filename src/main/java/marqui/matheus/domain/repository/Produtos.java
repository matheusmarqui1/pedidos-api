package marqui.matheus.domain.repository;

import marqui.matheus.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Produtos extends JpaRepository<Produto, Integer> {
    public Optional<Produto> findBySkuIgnoreCase(String sku);
}
