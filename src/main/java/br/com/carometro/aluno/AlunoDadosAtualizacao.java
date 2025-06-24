package br.com.carometro.aluno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AlunoDadosAtualizacao(
		@NotBlank(message = "Id é necesssario para atualização")
		Long id,
		@NotBlank
		String nome,
		@NotBlank
		@Email
		String email,
		String senha,
		String telefone
		
		) {

}
