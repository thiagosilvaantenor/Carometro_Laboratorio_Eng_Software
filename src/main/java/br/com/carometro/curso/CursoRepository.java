package br.com.carometro.curso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	List<Curso> findByUnidFatecId(Long unidFatecId);


}
