package br.com.carometro.adm;

import java.util.List;

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

	public Administrador getAdministradorById(Long id) {
		return repository.getReferenceById(id);
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

}
