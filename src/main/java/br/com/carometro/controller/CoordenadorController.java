package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.coordenador.CoordenadorRepository;
import br.com.carometro.coordenador.CoordenadorService;
import br.com.carometro.coordenador.DadosAtualizacaoCoordenador;
import br.com.carometro.coordenador.DadosCadastroCoordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.security.Criptografia;
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
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("cursos", cursoService.getAllCursos());
		if(id != null) {
	        var coordenador = repository.getReferenceById(id);
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
		// Busca o curso selecionado no formulario
		Curso curso = cursoService.getCursoById(cursoId);
		
		coordenador.setCurso(curso);
		
		service.salvar(coordenador); 
		return "redirect:coordenador";
	} 
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoCoordenador dados) throws NoSuchAlgorithmException {
		var coordenador = repository.getReferenceById(dados.id());
		//Atualiza curso
		if (dados.curso() != null) {
			//Remove o curso antigo
			coordenador.getCurso().setCoordenador(null);
			coordenador.setCurso(null);
			//Adiciona o curso novo
			coordenador.setCurso(dados.curso());
			dados.curso().setCoordenador(coordenador);
		}
		if (dados.senha() != null) {
			coordenador.setSenha(Criptografia.md5(coordenador.getSenha()));
		}
		coordenador.atualizarInformacoes(dados);
		repository.save(coordenador);
	    return "redirect:coordenador";
	}

	@DeleteMapping
	@Transactional
	public String removeCoordenador(Long id) {
		var coordenador = repository.getReferenceById(id);
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
        Optional<Coordenador> coordenadorLogin = service.login(coordenador.getEmail(), Criptografia.md5(coordenador.getSenha()));
        if(coordenadorLogin.isEmpty()) {
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
