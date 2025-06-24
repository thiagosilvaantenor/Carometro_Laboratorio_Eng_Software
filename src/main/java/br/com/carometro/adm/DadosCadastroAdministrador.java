package br.com.carometro.adm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroAdministrador(
		@NotBlank(message = "Nome é obrigatório")
		String nome,
		@NotBlank(message = "Email é obrigatório")
		String email,
		@NotBlank(message = "Senha é obrigatória")
		@Size(min = 8)
		String senha) {

}
