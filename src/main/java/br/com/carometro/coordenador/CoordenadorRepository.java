package br.com.carometro.coordenador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoordenadorRepository  extends JpaRepository<Coordenador, Long>{
	
	@Query("SELECT c FROM Coordenador c WHERE c.email = :email")
	public Coordenador findbyEmail(String email);

	@Query("select l from Coordenador l where l.email = :email AND l.senha = :senha")
	public Coordenador buscaLogin(String email, String senha);
	
		/* Query para filtrar por curso do coordenador as postagens
		 * SELECT 
			A.foto as 'Foto', 
			A.nome as 'Nome Completo', 
			A.ano as 'Ano de Conclusão', 
			A.comentariofatec as 'Comentário Fatec', 
			A.comentario as 'Comentário Livre', 
			H.descricao_trabalho as 'Atividade',
			H.empresa_trabalho as 'Empresa',
			H.tempo_trabalho as 'Duração',
			L.git_hub as 'GitHub',
			L.lattescnpq as 'Lattes',
			L.linked_in as 'Linked In'
			FROM aluno A
			INNER JOIN historico H ON A.aluno_id = H.aluno_id
			INNER JOIN links L ON A.aluno_id = L.aluno_id
			WHERE A.curso_id IN (SELECT curso_id FROM curso WHERE curso_id = 1) -- :curso_id)
			ORDER BY ano ASC
	
		 * */
	

}
