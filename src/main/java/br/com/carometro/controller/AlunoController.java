package br.com.carometro.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.carometro.aluno.Aluno;
import br.com.carometro.aluno.AlunoDadosCadastro;
import br.com.carometro.aluno.AlunoService;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
	@Autowired
	private AlunoService service;
	@Autowired
	private CursoService cursoService;
	
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("cursos", cursoService.getAllCursos());
		if(id != null && service.getById(id).isPresent()) {
	        var aluno = service.getById(id).get();
	        model.addAttribute("aluno", aluno);
	    } else {
	    	model.addAttribute("aluno", new Aluno());
	    }
		return "aluno/formulario";

	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		//Envia a lista de cursos da seleção de filtro
		List<Curso> cursos = cursoService.getAllCursos();
		//Envia todos os alunos
		model.addAttribute("lista", service.getAll());
		return "aluno/listagem";
	} 
	
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid AlunoDadosCadastro dados, Model model, 
			@RequestParam("cursoId") Long cursoId) {
			//busca o curso
			Curso curso = cursoService.getCursoById(cursoId);
			Aluno aluno = new Aluno(dados);
			aluno.setCurso(curso);
			service.salvar(aluno);
			return "redirect:aluno";		
	}
	
	@PutMapping
	@Transactional
	public String atualizar(@Valid AlunoDadosCadastro dados, Model model, 
			@RequestParam("cursoId") Long cursoId) {
			//busca o curso
			Curso curso = cursoService.getCursoById(cursoId);
			Aluno aluno = new Aluno(dados);
			aluno.setCurso(curso);
			service.salvar(aluno);
			return "redirect:aluno";		
	}
	
	//Método para filtrar a listagem
	@GetMapping("/filtrar")
	public String filtrar(@RequestParam(name = "nome", required = false) String nome, 
				@RequestParam(name = "nome", required = false)  Long cursoId, Model model) {
		List<Aluno> alunos = new ArrayList<>();
		//Envia a lista de cursos da seleção de filtro
		List<Curso> cursos = cursoService.getAllCursos();
		model.addAttribute("cursos", cursos);
		if (nome != null && cursoId != null)
			alunos = service.filtarPorNomeECurso(nome, cursoId);
		else if (nome != null && cursoId == null)
			alunos = service.filtarPorNome(nome);
		else if (cursoId != null && nome == null)
			alunos = service.filtarPorCurso(cursoId);
		else
			return "redirect:aluno";
		
		model.addAttribute("lista", alunos);
		return "aluno/listagem";
			
	}
	
	
	
}
