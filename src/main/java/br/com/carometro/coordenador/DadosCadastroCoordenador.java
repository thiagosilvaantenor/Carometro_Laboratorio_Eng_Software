package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCoordenador(@NotBlank
		String nome,
		String email,
		String senha,
		LocalDate vencimentoMandato,
		Curso curso) {

}
