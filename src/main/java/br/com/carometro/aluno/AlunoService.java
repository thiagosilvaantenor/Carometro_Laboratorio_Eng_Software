package br.com.carometro.aluno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.carometro.security.Criptografia;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository repository;
	
	public List<Aluno> getAll(){
		return repository.findAll();
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
}
