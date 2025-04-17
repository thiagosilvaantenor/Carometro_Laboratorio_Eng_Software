package br.com.carona.controller;

import java.security.NoSuchAlgorithmException;

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
import org.springframework.web.servlet.ModelAndView;

import br.com.carona.adm.Administrador;
import br.com.carona.adm.AdministradorService;
import br.com.carona.adm.AdminstradorRepository;
import br.com.carona.adm.DadosAtualizacaoAdministrador;
import br.com.carona.adm.DadosCadastroAdministrador;
import br.com.carona.security.Criptografia;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdminstradorRepository repository;
	
	@Autowired
	private AdministradorService service;

	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		System.out.println("id" + id);
		if (id != null) {
			var admin = repository.getReferenceById(id);
			model.addAttribute("admin", admin);
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
	public String cadastrar(@Valid DadosCadastroAdministrador dados) {
		repository.save(new Administrador(dados));
		return "redirect:admin";
	}

	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAdministrador dados) {
		var admin = repository.getReferenceById(dados.id());
		admin.atualizarInformacoes(dados);
		return "redirect:admin";
	}

	@DeleteMapping
	@Transactional
	public String removeCurso(Long id) {
		repository.deleteById(id);
		return "redirect:admin";
	}
	
	
	@GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/login");
        modelAndView.addObject("admin", new Administrador());
        return modelAndView;
    }


	@GetMapping("/index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/index");
		modelAndView.addObject("admin", new Administrador());
		return modelAndView;
	}
	
	
	@PostMapping("/login")
    public ModelAndView login(@Valid Administrador admin, BindingResult br,
                              HttpSession session) throws NoSuchAlgorithmException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("admin", new Administrador());
        if(br.hasErrors()) {
            modelAndView.setViewName("/login");
        }

        //Busca no banco de dados se este email e senha estão cadastrado
        Administrador adminLogin = service.loginAdmin(admin.getEmail(), Criptografia.md5(admin.getSenha()));
        if(adminLogin == null) {
            modelAndView.addObject("msg","Administrador não encontrado. Tente novamente");
        } else {
            session.setAttribute("adminLogado", adminLogin);
            return index();
        }

        return modelAndView;
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return login();
    }
}
