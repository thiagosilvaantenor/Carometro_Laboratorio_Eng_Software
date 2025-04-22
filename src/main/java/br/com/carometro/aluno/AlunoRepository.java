package br.com.carometro.aluno;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	
	public Aluno findByEmail(String email);
	
	Aluno findByEmailAndSenha(String email, String senha);

}
