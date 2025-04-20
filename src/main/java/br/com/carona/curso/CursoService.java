package br.com.carona.curso;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CursoService {
	@Autowired
	private CursoRepository repository;
	
	public List<Curso> getAllCursos() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Curso getCursoById(Long id) {
		return repository.getReferenceById(id);
	}

	public List<Curso> findAllById(List<Long> cursoIds) {
		return repository.findAllById(cursoIds);
	}

}
