package br.com.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carometro.security.Criptografia;

@Service
public class AlunoService {
	@Autowired
	private AlunoRepository repository;

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

	public Optional<Aluno> login(String email, String senha) {
		return repository.findByEmailAndSenha(email,senha);
	}

	public Aluno findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public List<Aluno> filtraAlunosPelaUnidFatecAdmin(Long unidFatecId) {
		
		return repository.findByUnidFatecId(unidFatecId);
	}
	
	public List<Aluno> filtraAlunosPeloCurso(Long cursoId){
		return repository.findByCursoId(cursoId);
	}
}
