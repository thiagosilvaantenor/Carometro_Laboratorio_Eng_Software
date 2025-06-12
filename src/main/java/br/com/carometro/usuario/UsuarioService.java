package br.com.carometro.usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.carometro.adm.Administrador;
import br.com.carometro.adm.AdministradorRepository;
import br.com.carometro.adm.AdministradorService;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.coordenador.CoordenadorRepository;
import br.com.carometro.coordenador.CoordenadorService;
import br.com.carometro.egresso.Egresso;
import br.com.carometro.egresso.EgressoRepository;
import br.com.carometro.egresso.EgressoService;

@Service
public class UsuarioService {
	
	//Classe service para verificar o login e logout, podendo ser coordenador, admin ou aluno

	//FIXME: Em vez de usar repository usar service
	@Autowired
	private CoordenadorService coordenadorService;

	@Autowired
	private AdministradorService adminService;
	
	@Autowired
	private EgressoService alunoService;
	
	
	public String verificaAtor(String email) {

	            if(adminService.findByEmail(email) != null) {
	               return "administrador";
	            }
	            if (coordenadorService.findbyEmail(email).isPresent()) {
	            	return "coordenador";
	            }
	            if (alunoService.findByEmail(email) != null) {
	            	return "aluno";
	            }
	            //Se não encontrar é por que o email não esta cadastrado
	            return "não encontrado";

	}
	
	//Verifica se o login do admin esta correto
	public Optional<Administrador> verificaLoginAdmin(String email, String senha) {
		return adminService.loginAdmin(email, senha);
	}
	//Verifica se o login do coordenador esta correto
	public Optional<Coordenador> verificaLoginCoordenador(String email, String senha) {
		return coordenadorService.login(email, senha);
	}
	//Verifica se o login do admin esta correto
	public Optional<Egresso> verificaLoginAluno(String email, String senha) {
		return alunoService.login(email, senha);
	}
	
	
}
