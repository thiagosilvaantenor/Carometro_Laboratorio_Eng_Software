package br.com.carometro.adm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminstradorRepository extends JpaRepository<Administrador, Long> {

	@Query("SELECT e FROM Administrador e WHERE e.email = :email")
	public Administrador findbyEmail(String email);
	
	@Query("select l from Administrador l where l.email = :email and l.senha = :senha")
	public Administrador buscaLogin(String email, String senha);

}
