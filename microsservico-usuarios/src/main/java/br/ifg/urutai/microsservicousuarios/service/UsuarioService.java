package br.ifg.urutai.microsservicousuarios.service;

import br.ifg.urutai.microsservicousuarios.dto.UsuarioCadastroDTO;
import br.ifg.urutai.microsservicousuarios.model.Usuario;
import br.ifg.urutai.microsservicousuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario cadastrar(UsuarioCadastroDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setEmail(dto.email());
        novoUsuario.setDataNascimento(dto.dataNascimento());
        novoUsuario.setEndereco(dto.endereco());

        novoUsuario.setSenha(dto.senha());

        return repository.save(novoUsuario);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario atualizar(Long id, UsuarioCadastroDTO dto) {
        Usuario usuarioExistente = buscarPorId(id);

        repository.findByEmail(dto.email()).ifPresent(usuario -> {
            if (!usuario.getId().equals(id)) {
                throw new RuntimeException("Este e-mail já está em uso por outro usuário");
            }
        });

        usuarioExistente.setNome(dto.nome());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setDataNascimento(dto.dataNascimento());
        usuarioExistente.setEndereco(dto.endereco());

        if (dto.senha() != null && !dto.senha().isBlank()) {

            usuarioExistente.setSenha(dto.senha());
        }

        return repository.save(usuarioExistente);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        repository.delete(usuario);
    }

    public void buscarPorEmail(String email) {
        repository.findByEmail(email).ifPresent(usuario -> {});
    }
}