package br.com.carometro.egresso;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EgressoRepository extends JpaRepository<Egresso, Long>{
	
	public Egresso findByEmail(String email);
	
	public Optional<Egresso> findByEmailAndSenha(String email, String senha);
	
	public List<Egresso> findByCursoId(Long cursoId);
	
	public List<Egresso> findBySituacaoCadastro(Boolean situacaoCadastro);
	
	public List<Egresso> findByCursoIdAndSituacaoCadastro(Long cursoId, Boolean situacaoCadastro);
	
	public List<Egresso> findByCursoIdAndSituacaoComentario(Long cursoId, Boolean situacaoComentario);
	
	public List<Egresso> findByAno(Integer ano);
	
	public List<Egresso> findByAnoAndSituacaoCadastro(Integer ano, Boolean situacao);
	
	public List<Egresso> findByCursoIdAndAno(Long cursoId, Integer ano);
	
	public List<Egresso> findByCursoIdAndAnoAndSituacaoCadastro(Long cursoId, Integer ano, Boolean situacao);
}
 