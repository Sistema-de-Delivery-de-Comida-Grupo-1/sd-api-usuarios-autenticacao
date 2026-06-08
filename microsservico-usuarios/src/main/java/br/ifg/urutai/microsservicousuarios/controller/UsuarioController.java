package br.ifg.urutai.microsservicousuarios.controller;

import br.ifg.urutai.microsservicousuarios.dto.LoginDTO;
import br.ifg.urutai.microsservicousuarios.dto.UsuarioCadastroDTO;
import br.ifg.urutai.microsservicousuarios.dto.UsuarioResumoDTO;
import br.ifg.urutai.microsservicousuarios.model.Usuario;
import br.ifg.urutai.microsservicousuarios.service.AuthGrpcClient;
import br.ifg.urutai.microsservicousuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final AuthGrpcClient authGrpcClient;


    public UsuarioController(UsuarioService service, AuthGrpcClient authGrpcClient) {
        this.service = service;
        this.authGrpcClient = authGrpcClient;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody UsuarioCadastroDTO dto) {
        Usuario usuarioSalvo = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = service.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioCadastroDTO dto) {
        Usuario usuarioAtualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> realizarLogin(@RequestBody LoginDTO dto) {
        Usuario usuario = service.buscarPorEmail(dto.email());
        String token = authGrpcClient.autenticar(dto.email(), dto.senha(), usuario.getSenha());

        return ResponseEntity.ok(token);
    }
    @GetMapping("/{id}/resumo")
    public ResponseEntity<UsuarioResumoDTO> buscarResumoPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);

        UsuarioResumoDTO resumo = new UsuarioResumoDTO(usuario.getId(), usuario.getNome());
        return ResponseEntity.ok(resumo);
    }
}