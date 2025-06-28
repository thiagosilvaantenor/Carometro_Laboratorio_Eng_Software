package br.com.carometro.curso;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	
	Optional<Curso> findByNomeContains(String nome);

}
