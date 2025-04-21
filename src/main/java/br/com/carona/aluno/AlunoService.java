package br.com.carona.aluno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carona.security.Criptografia;

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

	public Aluno login(String email, String senha) {
		return repository.findByEmailAndSenha(email,senha);
	}
}
