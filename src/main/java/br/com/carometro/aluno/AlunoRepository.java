package br.com.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.carometro.egresso.Egresso;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	Aluno findByEmail(String email);

	List<Aluno> findByNomeContainsAndCursoId(String nome, Long cursoId);
	
	@Query("SELECT l FROM Aluno l WHERE l.nome LIKE %:nome%")
	List<Aluno> findByNome(String nome);
	
	List<Aluno> findByCursoId(Long cursoId);

	Optional<Aluno> findByEmailAndSenha(String email, String senha); 
	

}
