package br.com.joao.projeto.repository;

import br.com.joao.projeto.modelo.Curso;
import br.com.joao.projeto.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Curso findByNome(String nomeCurso);
}
