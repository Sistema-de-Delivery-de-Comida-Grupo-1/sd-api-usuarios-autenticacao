package br.ifg.urutai.microsservicousuarios.dto;

import java.time.LocalDate;

public record UsuarioCadastroDTO(
        String email,
        String senha,
        String nome,
        LocalDate dataNascimento,
        String endereco
) {
}
