package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.carometro.adm.Administrador;
import br.com.carometro.aluno.Aluno;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.security.Criptografia;
import br.com.carometro.usuario.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

	private final UsuarioService serviceUsuario;
	
	// Injeção por construtor (recomendado em vez de @Autowired)
    public LoginController(UsuarioService serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }
	
	 @GetMapping
	 public String mostrarFormularioLogin(Model model) {
	      //model.addAttribute("usuario", new Usuario());
	      return "login/login";
	 }
	
	
	@PostMapping
	public String processarLogin(@Valid String email,
			@Valid String senha,
    		BindingResult bindingResult,
    		HttpSession session, 
    		RedirectAttributes redirectAttributes) 
    				throws NoSuchAlgorithmException{

    	if (bindingResult.hasErrors()) {
    		return "login/login";
    	}
    	// Verifica no banco de dados o email e senha estão cadastrados
    	//se sim verifica qual ator esta logando (admin, coodenador ou aluno)
    	
    	String ator = serviceUsuario.verificaAtor(email);
    	//Transforma a senha enviada em md5 para ser comparada com a senha do banco de dados
    	senha = Criptografia.md5(senha);
    	switch (ator) {
    	//TODO: verifica se o email e senha estão corretos de acordo com o ator, se sim redireciona para a o indice
		case "administrador":
			Optional<Administrador> admin = serviceUsuario.verificaLoginAdmin(email, senha);
			if (admin.isEmpty()) {
				redirectAttributes.addFlashAttribute("mensagemErro", 
	    				"Senha incorreta. Tente novamente");
			} else {
				// Adicione esta linha para identificar o usuário
				Administrador adminEncontrado = admin.get();
				session.setAttribute("usuarioLogado", adminEncontrado); 				
			}
			break;
		case "coordenador":
			Optional<Coordenador> coordenador = serviceUsuario.verificaLoginCoordenador(email, senha);
			if (coordenador.isEmpty()) {
				redirectAttributes.addFlashAttribute("mensagemErro", 
	    				"Senha incorreta. Tente novamente");
			} else {
				// Adicione esta linha para identificar o usuário
				Coordenador cooordenadorEncotrado = coordenador.get();
				session.setAttribute("usuarioLogado", cooordenadorEncotrado); 				
			}
			break;
		case "aluno":
			Optional<Aluno> aluno = serviceUsuario.verificaLoginAluno(email, senha);
			if (aluno.isEmpty()) {
				redirectAttributes.addFlashAttribute("mensagemErro", 
	    				"Senha incorreta. Tente novamente");
			}else {
				// Adicione esta linha para identificar o usuário
				Aluno alunoEncontrado = aluno.get();
				session.setAttribute("usuarioLogado", alunoEncontrado); 				
			}
			break;

		default:
			redirectAttributes.addFlashAttribute("mensagemErro", 
    				"Usuário não encontrado. Tente novamente");
    		return "redirect:/login";
		}
    	
    	//TODO: redirecionar para o index de acordo com o ator
    	return "redirect:/home/dashboard";
    }
	
	
}
