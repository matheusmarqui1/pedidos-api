package marqui.matheus.service.impl;

import lombok.RequiredArgsConstructor;
import marqui.matheus.api.dto.UsuarioDTO;
import marqui.matheus.domain.entity.Usuario;
import marqui.matheus.domain.repository.Usuarios;
import marqui.matheus.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UserDetailsService {
    private final Usuarios usuarioRepository;

    public UserDetails autenticar(Usuario usuario) {
        UserDetails userDetails = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem =
                new BCryptPasswordEncoder().matches(usuario.getSenha(), userDetails.getPassword());

        if(senhasBatem) return userDetails;
            else throw new SenhaInvalidaException();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        String roles[] = usuario.isAdmin() ?
                new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User
                .builder()
                    .username(usuario.getLogin())
                    .password(usuario.getSenha())
                    .roles(roles)
                .build();
    }

    public static UsuarioDTO converterUsuarioParaDTO(Usuario usuario) {
        return UsuarioDTO
                .builder()
                    .admin(usuario.isAdmin())
                    .login(usuario.getLogin())
                .build();
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
