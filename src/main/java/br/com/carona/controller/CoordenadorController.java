package br.com.carona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.carona.coordenador.Coordenador;
import br.com.carona.coordenador.CoordenadorRepository;
import br.com.carona.coordenador.DadosAtualizacaoCoordenador;
import br.com.carona.coordenador.DadosCadastroCoordenador;
import br.com.carona.curso.Curso;
import br.com.carona.curso.CursoRepository;
import br.com.carona.curso.DadosAtualizacaoCurso;
import br.com.carona.curso.DadosCadastroCurso;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/coordenador")
public class CoordenadorController {
	@Autowired
	private CoordenadorRepository repository;

	@GetMapping("/formulario")
	public String carregaPaginaFormulario(String matricula, Model model) {
		System.out.println("matricula" + matricula);
		if (matricula != null) {
			var coordenador = repository.getReferenceById(matricula);
			model.addAttribute("curso", coordenador);
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
			DadosCadastroCoordenador dados) {
			repository.save(new Coordenador(dados));
		return "redirect:coordenador";
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
	public String removeCurso(String matricula) {
		repository.deleteById(matricula);
		return "redirect:coordenador";
	}
	
}
