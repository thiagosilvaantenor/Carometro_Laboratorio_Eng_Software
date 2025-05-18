package br.com.carometro.adm;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carometro.security.Criptografia;

@Service
public class AdministradorService {
	@Autowired
	private AdminstradorRepository repository;

	public List<Administrador> getAllAdministrador() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Optional<Administrador> getAdministradorById(Long id) {
		return repository.findById(id);
	}

	public void salvarAdmin(Administrador admin) throws Exception {
		try {
			if (repository.findbyEmail(admin.getEmail()) != null) {
				throw new Exception("Este email já está cadastrado: " + admin.getEmail());
			}

			admin.setSenha(Criptografia.md5(admin.getSenha()));

		} catch (Exception e) {
			throw new Exception("Erro na criptografia da senha");
		}
		repository.save(admin);
	}

	public Administrador loginAdmin(String email, String senha) {
		return repository.buscaLogin(email, senha);
	}
	
	public void remover(Long id) throws Exception {
		if (getAdministradorById(id).isEmpty()) {
			throw new Exception("Administrador não encontrado!");
		}
		//Caso tenha encontrado o administrador
		repository.deleteById(id);
	}
}
