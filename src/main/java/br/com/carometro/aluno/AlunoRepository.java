package br.com.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	
	public Aluno findByEmail(String email);
	
	public Optional<Aluno> findByEmailAndSenha(String email, String senha);
	
	public List<Aluno> findByCursoId(Long cursoId);
	
	public List<Aluno> findBySituacaoCadastro(Boolean situacaoCadastro);
	
	public List<Aluno> findByCursoIdAndSituacaoCadastro(Long cursoId, Boolean situacaoCadastro);

	public List<Aluno> findByAno(Integer ano);
	
	public List<Aluno> findByAnoAndSituacaoCadastro(Integer ano, Boolean situacao);
	
	public List<Aluno> findByCursoIdAndAno(Long cursoId, Integer ano);
	
	public List<Aluno> findByCursoIdAndAnoAndSituacaoCadastro(Long cursoId, Integer ano, Boolean situacao);
}
 