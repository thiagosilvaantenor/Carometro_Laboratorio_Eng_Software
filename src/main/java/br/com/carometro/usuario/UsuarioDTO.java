package br.com.carometro.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
		@NotBlank(message = "Email é necessario para logar")
		String email,
		@NotBlank(message = "Senha é necessaria para logar")
		@Size(min = 8)
		String senha
		) {

}
