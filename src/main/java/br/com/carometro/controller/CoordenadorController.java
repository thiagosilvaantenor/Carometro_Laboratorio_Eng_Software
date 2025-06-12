package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

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

import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.coordenador.CoordenadorRepository;
import br.com.carometro.coordenador.CoordenadorService;
import br.com.carometro.coordenador.DadosAtualizacaoCoordenador;
import br.com.carometro.coordenador.DadosCadastroCoordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.egresso.Egresso;
import br.com.carometro.egresso.EgressoService;
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
	
	
	@Autowired
	private EgressoService alunoService;

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
	public String atualizar(DadosAtualizacaoCoordenador dados ) throws NoSuchAlgorithmException {
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
			coordenador.setSenha(Criptografia.md5(dados.senha()));
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
		modelAndView.addObject("role", "coordenador");
		return modelAndView;
	}
	
	//Mapeamento da pagina de posts para validaçao dos alunos 
	//Validação de Ex-aluno:Quando aluno é cadastrado, para ser exibido na listagem é necessario o coordenador verificar se ele é ex-aluno
	@GetMapping("/validarAluno")
	public ModelAndView paginaExibicaoPosts(HttpSession session) throws Exception {
		//Busca o coordenador logado
	    Coordenador coordenadorLogado = (Coordenador) session.getAttribute("usuarioLogado");
	    if (coordenadorLogado != null) {
	    	//Busca os alunos do curso do coordenador com situação de cadastro false para ele realizar a validação
	        List<Egresso> alunos = alunoService.filtraAlunosPeloCursoESituacaoCadastor
	        		(coordenadorLogado.getCurso().getId(), false);
	        
	        ModelAndView modelAndView = new ModelAndView();
	        modelAndView.addObject("alunos", alunos);
	        modelAndView.setViewName("coordenador/validarAluno");
	        return modelAndView;
	    } else {
	        throw new Exception("Usuário não está logado.");
	    }
	}

	//Caso seja ex-aluno é aprovado, estado de situaçãoCadastro é mudado para TRUE
	 @PostMapping("/aprovarAluno")
	    public String aprovarAluno(@RequestParam("id") Long id,  Model model, HttpSession session) {
		 
	        try {
				alunoService.aprovarAluno(id);
				//Busca o coordenador logado
	    	    Coordenador coordenadorLogado = (Coordenador) session.getAttribute("usuarioLogado");
	    	    List<Egresso> alunos = alunoService.filtraAlunosPeloCursoESituacaoCadastor
		        		(coordenadorLogado.getCurso().getId(), false);
	    	    model.addAttribute("alunos", alunos);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return "redirect:/coordenador/validarAluno";
	    }
	 
	//Caso não seja ex-aluno é reprovado e deletado do banco de dados 
    @DeleteMapping("/reprovarAluno")
	    public String reprovarAluno(@RequestParam("id") Long id, Model model, HttpSession session) {
	        try{
	        	alunoService.reprovarAluno(id);
	        	//Busca o coordenador logado
	    	    Coordenador coordenadorLogado = (Coordenador) session.getAttribute("usuarioLogado");
	    	    List<Egresso> alunos = alunoService.filtraAlunosPeloCursoESituacaoCadastor
		        		(coordenadorLogado.getCurso().getId(), false);		
	 	        model.addAttribute("alunos", alunos);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        return "redirect:/coordenador/validarAluno";
	    }

	
}
