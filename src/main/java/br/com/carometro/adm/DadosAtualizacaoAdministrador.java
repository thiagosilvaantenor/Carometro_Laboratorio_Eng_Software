package br.com.carometro.adm;

public record DadosAtualizacaoAdministrador(
		Long id,
		String nome,
		String cpf,
//		String unidFATEC,
		String email,
		String senha
		) {

}
