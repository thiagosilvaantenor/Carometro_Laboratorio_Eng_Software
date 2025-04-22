package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCoordenador(@NotBlank
		String matricula,
		String nome,
		String cpf,
		String email,
		String senha,
		String especializacao,
		LocalDate dtNascimento,
		String estadoCivil,
		Curso curso) {

}
