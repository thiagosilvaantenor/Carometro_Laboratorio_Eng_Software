package com.example.caronaSpring.curso;

public record DadosListagemCurso(long id, String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao) {
	public DadosListagemCurso(Curso curso) {
		// Erro do lombok, pois ele vai criar getters em tempo de execução
		this(curso.getId(), curso.getNome, curso.getDescricao(), curso.getDuracao, curso.getModalidade(),
				curso.getTurno(), curso.getAreaAtuacao());
	}
}
