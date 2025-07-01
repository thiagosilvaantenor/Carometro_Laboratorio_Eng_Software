package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.carometro.adm.Administrador;
import br.com.carometro.aluno.Aluno;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.egresso.Egresso;
import br.com.carometro.security.Criptografia;
import br.com.carometro.usuario.UsuarioDTO;
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
		  //Como existem 3 atores, se cria um DTO para lidar com os dados e depois verificar qual o ator
		  model.addAttribute("usuario", new UsuarioDTO(null, null));
	      return "login/login";
	 }
	
	
	@PostMapping
	public String processarLogin(@Valid UsuarioDTO dados,
    		BindingResult bindingResult,
    		HttpSession session, 
    		RedirectAttributes redirectAttributes) 
    				throws NoSuchAlgorithmException{

    	if (bindingResult.hasErrors()) {
    		return "login/login";
    	}
    	// Verifica no banco de dados o email e senha estão cadastrados
    	//se sim verifica qual ator esta logando (admin, coodenador ou egresso)
    	
    	String ator = serviceUsuario.verificaAtor(dados.email());
    	//Transforma a senha enviada em md5 para ser comparada com a senha do banco de dados
    	String senhaCript = Criptografia.md5(dados.senha());
    	switch (ator) {
			case "administrador":
				Optional<Administrador> admin = serviceUsuario.verificaLoginAdmin(dados.email(), senhaCript);
				if (admin.isEmpty()) {
					redirectAttributes.addFlashAttribute("mensagemErro", 
		    				"Senha incorreta. Tente novamente");
					return "redirect:/login";
				} else {
					// Adicione esta linha para identificar o usuário
					Administrador adminEncontrado = admin.get();
					session.setAttribute("usuarioLogado", adminEncontrado);
					session.setAttribute("role", "admin");
					return "redirect:/admin/index";
				}
			case "coordenador":
				Optional<Coordenador> coordenador = serviceUsuario.verificaLoginCoordenador(dados.email(), senhaCript);
				if (coordenador.isEmpty()) {
					redirectAttributes.addFlashAttribute("mensagemErro", 
		    				"Senha incorreta. Tente novamente");
					return "redirect:/login";
				} else {
					// Adicione esta linha para identificar o usuário
					Coordenador cooordenadorEncotrado = coordenador.get();
					session.setAttribute("usuarioLogado", cooordenadorEncotrado);
					session.setAttribute("role", "coordenador");
					return "redirect:/coordenador/index";

				}
			case "egresso":
				Optional<Egresso> egresso = serviceUsuario.verificaLoginEgresso(dados.email(), senhaCript);
				if (egresso.isEmpty()) {
					redirectAttributes.addFlashAttribute("mensagemErro", 
		    				"Senha incorreta. Tente novamente");
					return "redirect:/login";
				}else {
					Egresso egressoEncontrado = egresso.get();
					session.setAttribute("usuarioLogado", egressoEncontrado);
					session.setAttribute("role", "egresso");
					return "redirect:/egresso/index";
				}
			case "aluno":
				Optional<Aluno> aluno = serviceUsuario.verificaLoginAluno(dados.email(), senhaCript);
				if (aluno.isEmpty()) {
					redirectAttributes.addFlashAttribute("mensagemErro", 
		    				"Senha incorreta. Tente novamente");
					return "redirect:/login";
				}else {
					Aluno alunoEncontrado = aluno.get();
					session.setAttribute("usuarioLogado", alunoEncontrado);
					session.setAttribute("role", "aluno");
					return "redirect:/aluno/index";
				}
			default:
				redirectAttributes.addFlashAttribute("mensagemErro", 
	    				"Usuário não encontrado. Tente novamente");
	    		return "redirect:/login";
			}
    }
	
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
