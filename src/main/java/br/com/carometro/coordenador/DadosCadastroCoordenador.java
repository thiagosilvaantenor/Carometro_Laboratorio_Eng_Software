package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroCoordenador(
		@NotBlank(message = "Nome é obrigatório")
		String nome,
		@NotBlank(message = "Email é obrigatória")
		@Email
		String email,
		@NotBlank(message = "Senha é obrigatória")
		@Size(min = 8)
		String senha,
		LocalDate vencimentoMandato,
		@NotBlank(message = "Curso é obrigatória")
		Curso curso) {

}
