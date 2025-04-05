package com.example.caronaSpring.Aluno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

}
