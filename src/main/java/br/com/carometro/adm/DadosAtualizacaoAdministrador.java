package br.com.carometro.adm;

import jakarta.validation.constraints.Size;

public record DadosAtualizacaoAdministrador(
		Long id,
		String nome,
		String email,
		@Size(min = 8)
		String senha
		) {

}
