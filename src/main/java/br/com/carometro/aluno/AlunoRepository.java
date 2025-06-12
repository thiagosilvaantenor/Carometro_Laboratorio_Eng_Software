package br.com.carometro.aluno;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	Aluno findByEmail(String email);

}
