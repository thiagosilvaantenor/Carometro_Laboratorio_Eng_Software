package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.carometro.adm.Administrador;
import br.com.carometro.adm.AdministradorService;
import br.com.carometro.adm.AdministradorRepository;
import br.com.carometro.adm.DadosAtualizacaoAdministrador;
import br.com.carometro.adm.DadosCadastroAdministrador;
import br.com.carometro.security.Criptografia;
import br.com.carometro.unidfatec.UnidFatec;
import br.com.carometro.unidfatec.UnidFatecService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdministradorRepository repository;
	
	@Autowired
	private AdministradorService service;

	@Autowired
	private UnidFatecService unidFatecService;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("unidades", unidFatecService.buscaTodas());
		if (id != null) {
			var admin = repository.getReferenceById(id);
			model.addAttribute("admin", admin);
		} else {
			model.addAttribute("admin", new Administrador());

		}
		return "admin/formulario";
	}
  
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "admin/listagem"; 
	}

	@PostMapping
	@Transactional
	public String cadastrar(@Valid DadosCadastroAdministrador dados, @RequestParam("nomeFatec") String nomeFatec) throws Exception {
		if (dados == null || dados.email().isBlank() || dados.nome().isBlank() || dados.senha().isBlank()) {
			throw new Exception("Dados invalidos, cadastro não realizado");
		}
		UnidFatec unidade = unidFatecService.buscarPorNome(nomeFatec);
		//Caso o nome da unidade não foi encontrado então cria essa unidadeFatec
		if (unidade == null) {
			    unidade = new UnidFatec();
			    unidade.setNome(nomeFatec);
			    unidFatecService.salvar(unidade);
		}
		Administrador admin = new Administrador(dados);
		admin.setUnidFatec(unidade);
		service.salvarAdmin(admin);
		return "redirect:admin"; 
	}
 
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAdministrador dados) throws NoSuchAlgorithmException {
		var admin = repository.getReferenceById(dados.id());
		if (dados.senha() != null) {
			admin.setSenha(Criptografia.md5(admin.getSenha()));
		}
		admin.atualizarInformacoes(dados);
		return "redirect:admin";
	}

	@DeleteMapping
	@Transactional
	public String removeAdmin(Long id) throws Exception {
		service.remover(id);
		return "redirect:admin";
	}
	
	
	//Mapeamento da pagina inicio do administrador, para chegar lá é necessario ele se logar pelo LoginController
	@GetMapping("/index")
	public ModelAndView index(HttpSession session) {
		//Pega o administrador recebido do login
		Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/index");
		modelAndView.addObject("admin", adminLogado);
		return modelAndView;
	}
	
// FIXME: linhas comentadas para testar o LoginController, caso não funcione verifique as linhas abaixo
//	@PostMapping("/login")
//    public ModelAndView login(@Valid Administrador admin, BindingResult br,
//                              HttpSession session) throws NoSuchAlgorithmException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("admin", new Administrador());
//        if(br.hasErrors()) {
//            modelAndView.setViewName("/login");
//        }
//
//        //Busca no banco de dados se este email e senha estão cadastrado
//        Administrador adminLogin = service.loginAdmin(admin.getEmail(), Criptografia.md5(admin.getSenha()));
//        if(adminLogin == null) {
//            modelAndView.addObject("msg","Administrador não encontrado. Tente novamente");
//        } else {
//            session.setAttribute("adminLogado", adminLogin);
//            return index();
//        }
//
//        return modelAndView;
//    }

//    @PostMapping("/logout")
//    public ModelAndView logout(HttpSession session) {
//        session.invalidate();
//        return login();
//    }
}
