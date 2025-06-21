package br.com.carometro.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.security.Criptografia;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository repository;
	
	@Autowired
	private CursoService cursoService;
	
	public List<Aluno> getAll(){
		return repository.findAll();
	}
	
	public void salvar(Aluno aluno) {
		try {
			if (repository.findByEmail(aluno.getEmail()) != null) {
				throw new Exception("Este email já está cadastrado: " + aluno.getEmail());
			}
	
			aluno.setSenha(Criptografia.md5(aluno.getSenha()));

		} catch (Exception e) {
			new Exception("Erro na criptografia da senha");
		}
		repository.save(aluno);
	}

	public Optional<Aluno> getById(Long id) {
		return repository.findById(id);
	}

	public List<Aluno> filtarPorNomeECurso(String nome, Long cursoId) {
		Curso curso = cursoService.getCursoById(cursoId);
		return repository.findByNomeAndCurso(nome, curso);
	}

	public List<Aluno> filtarPorNome(String nome) {
		return repository.findByNome(nome);
	}

	public List<Aluno> filtarPorCurso(Long cursoId) {
		Curso curso = cursoService.getCursoById(cursoId);
		return repository.findByCurso(curso);
	}
}
