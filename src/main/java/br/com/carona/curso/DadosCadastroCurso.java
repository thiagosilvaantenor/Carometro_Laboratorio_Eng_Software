package br.com.carona.curso;

import java.util.List;

import br.com.carona.aluno.Aluno;
import br.com.carona.coordenador.Coordenador;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;


public record DadosCadastroCurso(@NotBlank 
		String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, Coordenador coordenador, List<Aluno> aluno) {

}
