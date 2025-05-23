package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCoordenador(@NotBlank
		String nome,
		
		@Email
		String email,
		
		String senha,
		LocalDate vencimentoMandato,
		Curso curso) {

}
