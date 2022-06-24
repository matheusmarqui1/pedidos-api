package marqui.matheus.domain.repository;

import marqui.matheus.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Integer> {
    public Optional<Usuario> findByLogin(String login);
}
