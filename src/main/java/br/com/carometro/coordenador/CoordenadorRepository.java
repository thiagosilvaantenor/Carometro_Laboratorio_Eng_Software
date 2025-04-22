package br.com.carometro.coordenador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoordenadorRepository  extends JpaRepository<Coordenador, String>{
	
	@Query("SELECT c FROM Coordenador c WHERE c.email = :email")
	public Coordenador findbyEmail(String email);

	@Query("select l from Coordenador l where l.email = :email AND l.senha = :senha")
	public Coordenador buscaLogin(String email, String senha);

}
