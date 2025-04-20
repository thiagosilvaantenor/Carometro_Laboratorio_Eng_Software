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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.carona.adm.Administrador;
import br.com.carona.coordenador.Coordenador;
import br.com.carona.coordenador.CoordenadorRepository;
import br.com.carona.coordenador.CoordenadorService;
import br.com.carona.coordenador.DadosAtualizacaoCoordenador;
import br.com.carona.coordenador.DadosCadastroCoordenador;
import br.com.carona.curso.Curso;
import br.com.carona.curso.CursoService;
import br.com.carona.security.Criptografia;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/coordenador")
public class CoordenadorController {
	@Autowired
	private CoordenadorRepository repository;
	
	@Autowired
	private CursoService cursoService;
	
	@Autowired
	private CoordenadorService service;

	@GetMapping("/formulario")
	public String carregaPaginaFormulario(String matricula, Model model) {
		//System.out.println("matricula" + matricula);
		model.addAttribute("cursos", cursoService.getAllCursos());
		if(matricula != null) {
	        var coordenador = repository.getReferenceById(matricula);
	        model.addAttribute("coordenador", coordenador);
	    } else {
	    	model.addAttribute("coordenador", new Coordenador());
	    }
		return "coordenador/formulario";

	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "coordenador/listagem";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid
			DadosCadastroCoordenador dados, @RequestParam("cursoId") Long cursoId) throws Exception {
		Coordenador coordenador = new Coordenador(dados);
		// Busca os Cursos pelos IDs cria uma lista de cursos 
		Curso curso = cursoService.getCursoById(cursoId);
		
		coordenador.setCurso(curso);
		
		service.salvar(coordenador); 
		return "redirect:coordenador/login";
	} 
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoCoordenador dados) {
		var coordenador = repository.getReferenceById(dados.matricula());
		coordenador.atualizarInformacoes(dados);
		
	    return "redirect:coordenador";
	}

	@DeleteMapping
	@Transactional
	public String removeCoordenador(String matricula) {
		var coordenador = repository.getReferenceById(matricula);
		// Desfaz o vinculo entre curso e coordenador
		if (coordenador.getCurso() != null) {
		    coordenador.getCurso().setCoordenador(null);
		    coordenador.setCurso(null);
		}
		repository.delete(coordenador);
		return "redirect:coordenador";
	}
	
	@GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("coordenador/login");
        modelAndView.addObject("coordenador", new Coordenador());
        return modelAndView;
    }


	@GetMapping("/index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("coordenador/index");
		modelAndView.addObject("coordenador", new Coordenador());
		return modelAndView;
	}
	
	
	@PostMapping("/login")
    public ModelAndView login(@Valid Coordenador coordenador, BindingResult br,
                              HttpSession session) throws NoSuchAlgorithmException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("coordenador", new Coordenador());
        if(br.hasErrors()) {
            modelAndView.setViewName("coordenador/login");
            return modelAndView;
        }

        //Busca no banco de dados se este email e senha estão cadastrado
        Coordenador coordenadorLogin = service.login(coordenador.getEmail(), Criptografia.md5(coordenador.getSenha()));
        if(coordenadorLogin == null) {
            modelAndView.addObject("msg","Coordenador não encontrado. Tente novamente");
        } else {
            session.setAttribute("CoordenadorLogado", coordenadorLogin);
            return new ModelAndView("redirect:/index");
        }

        return modelAndView;
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return login();
    }
	
}
