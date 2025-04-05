package com.example.caronaSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.caronaSpring.Aluno.Aluno;
import com.example.caronaSpring.Aluno.AlunoRepository;
import com.example.caronaSpring.Aluno.DadosAtualizacaoAluno;
import com.example.caronaSpring.Aluno.DadosCadastroAluno;
import com.example.caronaSpring.historico.HistoricoRepository;
import com.example.caronaSpring.links.LinksRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
	@Autowired
	private AlunoRepository repository;
	@Autowired
	private HistoricoRepository repositoryHistorico;
	@Autowired
	private LinksRepository repositoryLinks;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		System.out.println("Id" + id);
		if (id != null) {
			var aluno = repository.getReferenceById(id);
			model.addAttribute("aluno", aluno);
		}
		return "aluno/formulario";
	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "curso/listagem";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid
			DadosCadastroAluno dados) {
		repository.save(new Aluno(dados));
		//Quando cadastra um aluno, também se cadastra um histórico e um link
		repositoryHistorico.save(dados.historico());
		repositoryLinks.save(dados.links());
		return "redirect:aluno";
	}
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAluno dados) {
		var aluno = repository.getReferenceById(dados.id());
		aluno.atualizarInformacoes(dados);
		return "redirect:aluno";
	}

	@DeleteMapping
	@Transactional
	public String removeCurso(Long id) {
		repository.deleteById(id);
		return "redirect:aluno";
	}
	
	
}
