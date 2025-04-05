package com.example.caronaSpring.curso;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.example.caronaSpring.Aluno.Aluno;

public class CursoService {
	@Autowired
	private CursoRepository repository;
	
	public List<Curso> getAllCursos() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Curso getCursoById(Long id) {
		return repository.getReferenceById(id);
	}

}
