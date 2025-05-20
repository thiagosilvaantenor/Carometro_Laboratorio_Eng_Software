package br.com.carometro.adm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

	@Query("SELECT e FROM Administrador e WHERE e.email = :email")
	public Administrador findByEmail(String email);
	
	@Query("select l from Administrador l where l.email = :email and l.senha = :senha")
	public Optional<Administrador> buscaLogin(String email, String senha);


}
