package br.com.carometro.controller;

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

import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoRepository;
import br.com.carometro.curso.DadosAtualizacaoCurso;
import br.com.carometro.curso.DadosCadastroCurso;
import br.com.carometro.unidfatec.UnidFatec;
import br.com.carometro.unidfatec.UnidFatecService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoController {
	@Autowired
	private CursoRepository repository;

	@Autowired
	private UnidFatecService unidService;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		//Busca as unidades Fatec
		model.addAttribute("unidades", unidService.buscaTodas());
		Curso curso = null;
		if (id != null) {
			//Verifica se é uma atualização de um curso
			curso = repository.getReferenceById(id);
		}
		model.addAttribute("curso", curso);
		return "curso/formulario";
	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "curso/listagem";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid
			DadosCadastroCurso dados) throws Exception {
		//UnidFatec
		Curso curso = new Curso(dados);
		
		UnidFatec unidFatec = unidService.buscarPorNome(dados.nomeFatec());
		if(unidFatec == null) {
			throw new Exception("Erro ao buscar unidade Fatec");
		}
		curso.setUnidFatec(unidFatec);
		
		repository.save(curso);
		
		return "redirect:curso";
	}
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoCurso dados) {
		
		var curso = repository.getReferenceById(dados.id());
		curso.atualizarInformacoes(dados);
		return "redirect:curso";
	}

	@DeleteMapping
	@Transactional
	public String removeCurso(Long id) {
		repository.deleteById(id);
		return "redirect:curso";
	}

}
