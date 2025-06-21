package br.com.carometro.curso;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Optional<Curso> findByNome(String nome);

}
