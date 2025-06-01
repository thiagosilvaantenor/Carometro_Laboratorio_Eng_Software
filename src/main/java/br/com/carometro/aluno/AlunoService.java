package br.com.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carometro.historico.Historico;
import br.com.carometro.historico.HistoricoRepository;
import br.com.carometro.security.Criptografia;

@Service
public class AlunoService {
	@Autowired
	private AlunoRepository repository;
	
	@Autowired
	private HistoricoRepository repositoryHistorico;

	public List<Aluno> getAllAluno() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Aluno getAlunoById(Long id) {
		return repository.getReferenceById(id);
	}

	
	public void salvar(Aluno aluno) throws Exception {
		try {
			if (repository.findByEmail(aluno.getEmail()) != null) {
				throw new Exception("Este email já está cadastrado: " + aluno.getEmail());
			}
	
			aluno.setSenha(Criptografia.md5(aluno.getSenha()));

		} catch (Exception e) {
			throw new Exception("Erro na criptografia da senha");
		}
		repository.save(aluno);
	}

	//Verifica o login(email e senha) do aluno
	public Optional<Aluno> login(String email, String senha) {
		return repository.findByEmailAndSenha(email,senha);
	}

	public Aluno findByEmail(String email) {
		return repository.findByEmail(email);
	}
	
	public List<Aluno> filtraAlunosPeloCurso(Long cursoId){
		return repository.findByCursoId(cursoId);
	}
	
	public List<Aluno> filtraAlunosPeloCursoESituacaoCadastor(Long cursoId, Boolean situacaoCadastro){
		return repository.findByCursoIdAndSituacaoCadastro(cursoId, situacaoCadastro);
	}
	
	public List<Aluno> filtrarAlunosPeloCursoESituacaoComentario(Long cursoId, Boolean situacaoComentario){
		return repository.findByCursoIdAndSituacaoComentario(cursoId, situacaoComentario);
	}
	
	public List<Aluno> filtraAlunosPeloAnoSemestre(Integer ano){
		return repository.findByAno(ano);		
	}
	
	public List<Aluno> buscaAlunosPelaSituacaoCadastro(Boolean situacao){
		if (situacao != null)
			return repository.findBySituacaoCadastro(situacao);
		throw new IllegalArgumentException();
	}
		
	//Filtra os alunos
	public List<Aluno> filtrarAluno(Integer ano, Long cursoId, Boolean situacao){
			//Informou tudo
		 	if (cursoId != null && ano != null && ano > 0 && situacao != null) {
		        return repository.findByCursoIdAndAno(cursoId, ano);
		    } else if (cursoId != null && situacao != null) { // Informou curso e situacao
		        return repository.findByCursoIdAndSituacaoCadastro(cursoId, situacao);
		    } else if (cursoId != null ) { // Informou curso
		        return repository.findByCursoId(cursoId);
		    } else if (ano != null && ano > 0 && situacao != null) { // informou ano e situacao
		        return repository.findByAnoAndSituacaoCadastro(ano, situacao);
		    }  else if (ano != null && ano > 0) { // informou ano
		        return repository.findByAno(ano);
		    }  else if (situacao != null){ // informou apenas situacao
		        return repository.findBySituacaoCadastro(situacao);
		    } else {// Não informou nada
		    	return repository.findAll();
		    }
	}
	
	//Caso o aluno cadastrado for realmente ex-aluno, muda o estado de Situação cadastro e passa a listar ele na postagem
	public void aprovarAluno(Long id) throws IllegalArgumentException {
		Optional<Aluno> alunoBuscado = repository.findById(id);
		if (alunoBuscado.isPresent()) {
			Aluno aluno = alunoBuscado.get();
			aluno.setSituacaoCadastro(true);
			repository.save(aluno);
		} else {
			throw new IllegalArgumentException("Erro ao tentar buscar e aprovar aluno");
		}
		
	}
	//Caso o aluno cadastrado não for ex-aluno, não tem motivo deixar ele no banco de dados, então deleta ele
	public void reprovarAluno(Long id) throws IllegalArgumentException {
		Optional<Aluno> alunoBuscado = repository.findById(id);
		if (alunoBuscado.isPresent()) {
			remover(id);
		} else {
			throw new IllegalArgumentException("Erro ao tentar buscar e aprovar aluno");
		}
		
	}
	
	
	//Caso os comentarios forem aprovados, muda estado de situacaoComentario
		public void aprovarComentario(Long id) throws IllegalArgumentException {
			Optional<Aluno> alunoBuscado = repository.findById(id);
			if (alunoBuscado.isPresent()) {
				Aluno aluno = alunoBuscado.get();
				aluno.setSituacaoComentario(true);
				repository.save(aluno);
			} else {
				throw new IllegalArgumentException("Erro ao tentar buscar e aprovar comentario do aluno");
			}
			
		}
		//Caso pelo menos 1 dos comentarios não foi reprovado, apaga os comentarios reprovados
		public void reprovarComentario(Long id, String tipoComentario) throws IllegalArgumentException {
			Optional<Aluno> alunoBuscado = repository.findById(id);
			if (alunoBuscado.isPresent() && (tipoComentario.equalsIgnoreCase("geral") 
					|| tipoComentario.equalsIgnoreCase("fatec") || tipoComentario.equalsIgnoreCase("comentarios") )) {
				
				Aluno aluno = alunoBuscado.get();
				aluno.setSituacaoComentario(false);
				//verifica qual comentario foi reprovado e atualiza o aluno
				if (tipoComentario.equalsIgnoreCase("geral"))
					aluno.setComentario(null);
				else if (tipoComentario.equalsIgnoreCase("fatec"))
					aluno.setComentarioFATEC(null);
				else {
					aluno.setComentarioFATEC(null);
					aluno.setComentario(null);
				}
				
				repository.save(aluno);
			} else {
				throw new IllegalArgumentException("Erro ao tentar buscar e aprovar aluno");
			}
			
		}

	public void remover(Long id) {
		Optional<Aluno> alunoBuscado = repository.findById(id);
		if (alunoBuscado.isPresent()) {			
			//Remove a relação de aluno e historico
			List<Historico> historicosDoAluno = repositoryHistorico.findByAlunoId(id); 
			historicosDoAluno.forEach(repositoryHistorico::delete);
			repository.deleteById(id);
		}
		
	}
	
	
}
