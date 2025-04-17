package br.com.carona.curso;

public record DadosListagemCurso(long id, String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao) {
	public DadosListagemCurso(Curso curso) {
		this(curso.getId(), curso.getNome(), curso.getDescricao(), curso.getDuracao(), curso.getModalidade(),
				curso.getTurno(), curso.getAreaAtuacao()
			);
	}
}
