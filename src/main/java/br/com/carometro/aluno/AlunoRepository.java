package br.com.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	
	public Aluno findByEmail(String email);
	
	public Optional<Aluno> findByEmailAndSenha(String email, String senha);
	
	public List<Aluno> findByCursoId(Long cursoId);



}
