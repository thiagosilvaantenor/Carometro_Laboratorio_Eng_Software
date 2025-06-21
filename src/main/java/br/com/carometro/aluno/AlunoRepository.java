package br.com.carometro.aluno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.carometro.curso.Curso;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	Aluno findByEmail(String email);

	List<Aluno> findByNomeAndCurso(String nome, Curso curso);

	List<Aluno> findByNome(String nome);

	List<Aluno> findByCurso(Curso curso);

}
