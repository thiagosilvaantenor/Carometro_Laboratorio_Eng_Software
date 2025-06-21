package br.com.carometro.aluno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.carometro.curso.Curso;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	Aluno findByEmail(String email);

	List<Aluno> findByNomeAndCursoId(String nome, Long cursoId);
	
	@Query("SELECT l FROM Aluno l WHERE l.nome LIKE %:nome%")
	List<Aluno> findByNome(String nome);
	
	//@Query("SELECT l FROM Aluno l WHERE l.curso =:cursoId")
	List<Aluno> findByCursoId(Long cursoId); 
	

}
