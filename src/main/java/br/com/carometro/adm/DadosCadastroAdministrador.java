package br.com.carometro.adm;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAdministrador(
		@NotBlank
		String nome,
		String unidFATEC,
		String email,
		String senha
		) {

}
