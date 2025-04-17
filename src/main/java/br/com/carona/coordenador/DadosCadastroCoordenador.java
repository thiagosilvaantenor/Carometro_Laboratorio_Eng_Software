package br.com.carona.coordenador;

import java.time.LocalDate;

import br.com.carona.curso.Curso;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCoordenador(@NotBlank
		String matricula,
		String nome,
		String cpf,
		String email,
		String especializacao,
		LocalDate dtNascimento,
		String estadoCivil,
		Curso curso) {

}
