package br.com.carometro.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
		@NotBlank
		String email,
		@NotBlank
		String senha
		) {

}
