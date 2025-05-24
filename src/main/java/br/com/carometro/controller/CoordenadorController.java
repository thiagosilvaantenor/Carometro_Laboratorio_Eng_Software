package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.carometro.aluno.Aluno;
import br.com.carometro.aluno.AlunoService;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.coordenador.CoordenadorRepository;
import br.com.carometro.coordenador.CoordenadorService;
import br.com.carometro.coordenador.DadosAtualizacaoCoordenador;
import br.com.carometro.coordenador.DadosCadastroCoordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.security.Criptografia;
import br.com.carometro.unidfatec.UnidFatec;
import br.com.carometro.unidfatec.UnidFatecService;
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
	
	@Autowired
	private UnidFatecService unidFatecService;
	
	@Autowired
	private AlunoService alunoService;

	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("unidades", unidFatecService.buscaTodas());
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
			DadosCadastroCoordenador dados, @RequestParam("cursoId") Long cursoId,
			@RequestParam("fatecId") Long fatecId) throws Exception {
		
		Coordenador coordenador = new Coordenador(dados);
		// Busca o curso selecionado no formulario
		Curso curso = cursoService.getCursoById(cursoId);
		coordenador.setCurso(curso);
		
		//UnidFATEC
		Optional<UnidFatec> unidFatec = unidFatecService.getUnidFatecById(fatecId);
		
		if (unidFatec.isEmpty()) {
			throw new Exception("Unidade fatec não encontrada");
		}
		//Se encontrou
		UnidFatec unidFatecEncontrada = unidFatec.get();
		// adiciona a relação coordenador e unidFatec
		coordenador.setUnidFatec(unidFatecEncontrada);
		unidFatecEncontrada.getCoordenadores().add(coordenador);	
		
		
		service.salvar(coordenador); 
		return "redirect:coordenador";
	} 
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoCoordenador dados,
			@RequestParam("fatecId") Long fatecId ) throws NoSuchAlgorithmException {
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
		//Encripta a senha
		if (dados.senha() != null) {
			coordenador.setSenha(Criptografia.md5(coordenador.getSenha()));
		}
		//Unid fatec
		//Busca no banco de dados a unid fatec escolhida, se for diferente realiza a troca
		UnidFatec fatecRecebida = unidFatecService.getUnidFatecById(fatecId).get();
		if ( fatecRecebida.getId() != coordenador.getUnidFatec().getId()) {
			UnidFatec unidAntiga = coordenador.getUnidFatec();
			unidAntiga.getCoordenadores().remove(coordenador);
			coordenador.setUnidFatec(fatecRecebida);
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
			Curso curso = coordenador.getCurso();
		    coordenador.getCurso().setCoordenador(null);
		    coordenador.setCurso(null);
		    curso.setCoordenador(null);
		    
		}
		//Desfaz o vinculo de unidFATEC
		if (coordenador.getUnidFatec() != null) {
			Optional<UnidFatec> unidFatec = unidFatecService.getUnidFatecById
					(coordenador.getUnidFatec().getId());
			if (unidFatec.isPresent()) {
				unidFatec.get().getCoordenadores().remove(coordenador);
				coordenador.setUnidFatec(null);
			}
		}
		repository.delete(coordenador);
		return "redirect:coordenador";
	}
	

	//Mapeamento da pagina inicio do administrador, para chegar lá é necessario ele se logar pelo LoginController
	@GetMapping("/index")
	public ModelAndView index(HttpSession session) {
		//Pega o coordenador recebido do login
		Coordenador coordenadorLogado = (Coordenador) session.getAttribute("usuarioLogado");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("coordenador/index");
		modelAndView.addObject("coordenador", coordenadorLogado);
		return modelAndView;
	}
	
	//Mapeamento da pagina de posts para validaçao dos alunos 
	@GetMapping("/validarAluno")
	public ModelAndView paginaExibicaoPosts(HttpSession session) throws Exception {
		//Busca o coordenador logado
	    Coordenador coordenadorLogado = (Coordenador) session.getAttribute("usuarioLogado");
	    if (coordenadorLogado != null) {
	    	//Busca os alunos do curso do coordenador para exibir
	        List<Aluno> alunos = alunoService.filtraAlunosPeloCurso(coordenadorLogado.getCurso().getId());			
	        ModelAndView modelAndView = new ModelAndView();
	        modelAndView.addObject("alunos", alunos);
	        modelAndView.setViewName("coordenador/validarAluno");
	        return modelAndView;
	    } else {
	        throw new Exception("Usuário não está logado.");
	    }
	}

// FIXME: Corrigir método de logout
    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login/login");
        return modelAndView;
    }
	
}
