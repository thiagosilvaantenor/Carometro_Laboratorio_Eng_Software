package br.com.carometro.adm;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAdministrador(
		@NotBlank(message = "Nome é obrigatório")
		String nome,
		@NotBlank(message = "Email é obrigatório")
		String email,
		@NotBlank(message = "Senha é obrigatória")
		String senha) {

}
