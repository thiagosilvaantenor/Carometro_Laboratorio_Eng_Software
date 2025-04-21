package br.com.carona.adm;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAdministrador(
		@NotBlank
		String nome,
		String cpf,
		String unidFATEC,
		String email,
		String senha
		) {

}
