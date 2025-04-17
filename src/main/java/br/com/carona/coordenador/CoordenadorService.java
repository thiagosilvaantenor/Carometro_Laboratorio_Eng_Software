package br.com.carona.coordenador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carona.aluno.Aluno;
import br.com.carona.aluno.AlunoRepository;

@Service
public class CoordenadorService {
	@Autowired
	private CoordenadorRepository repository;

	public List<Coordenador> getAllCoordenador() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Coordenador getCoordenadorById(String matricula) {
		return repository.getReferenceById(matricula);
	}
}
