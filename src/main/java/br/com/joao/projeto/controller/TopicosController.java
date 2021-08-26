package br.com.joao.projeto.controller;

import br.com.joao.projeto.controller.dto.DetalhesDoTopicoDto;
import br.com.joao.projeto.controller.dto.TopicoDto;
import br.com.joao.projeto.controller.form.AtualizacaoTopicoForm;
import br.com.joao.projeto.controller.form.TopicoForm;
import br.com.joao.projeto.repository.CursoRepository;
import br.com.joao.projeto.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import br.com.joao.projeto.modelo.Topico;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaTopicos")
    public Page<TopicoDto> lista(
            @RequestParam(required = false) String nomeCurso,
            @PageableDefault(page = 0,size = 1) Pageable paginacao
    ){
        return nomeCurso == null ? TopicoDto.converter(topicoRepository.findAll(paginacao)):
                TopicoDto.converter(topicoRepository.findByCursoNome(nomeCurso,paginacao));
    }

    @PostMapping
    @CacheEvict(value = "listaTopicos",allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(
            @RequestBody @Valid TopicoForm form,
            UriComponentsBuilder uriBuilder
    ){
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public DetalhesDoTopicoDto detalhar(@PathVariable Long id){
        return new DetalhesDoTopicoDto(topicoRepository.getById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos",allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizacaoTopicoForm form
    ){
        Topico topico = form.atualizar(id,topicoRepository);
        return ResponseEntity.ok(new TopicoDto(topico));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaTopicos",allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id){
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
