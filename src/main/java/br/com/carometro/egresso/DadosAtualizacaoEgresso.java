package br.com.carometro.egresso;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
import br.com.carometro.historico.DadosCadastroHistorico;

public record DadosAtualizacaoEgresso(Long id, 
		String nome, 
		String email, 
		String senha, 
		LocalDate dtNascimento,
		String comentarioFATEC, 
		String comentario,
		Integer ano,
		String sobre,
		String sobreProfissional,
		String sobreFatec,
		Curso curso,
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		List<DadosCadastroHistorico> historico,
		MultipartFile foto
		) {

}
