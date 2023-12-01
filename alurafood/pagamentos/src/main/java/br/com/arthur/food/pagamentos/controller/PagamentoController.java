package br.com.arthur.food.pagamentos.controller;

import br.com.arthur.food.pagamentos.dto.PagamentoDto;
import br.com.arthur.food.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PagamentoService service;

    @GetMapping
    public Page<PagamentoDto> listar(@PageableDefault Pageable pageable) {
        return service.obterTodos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDto> detalhar(@PathVariable @NotNull Long id) {
        PagamentoDto dto = service.obterPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PagamentoDto> cadastrar(@RequestBody @Valid PagamentoDto dto, UriComponentsBuilder uriComponentsBuilder) {
        PagamentoDto pagamentoDto = service.criarPagamento(dto);
        URI endereco = uriComponentsBuilder.path("/pagamentos/{id}").buildAndExpand(pagamentoDto.getId()).toUri();
        rabbitTemplate.convertAndSend("pagamento.concluido", pagamentoDto);
        return ResponseEntity.created(endereco).body(pagamentoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto) {
        PagamentoDto pagamentoAtualizado = service.atualizarPagamento(id, dto);
        return ResponseEntity.ok(pagamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDto> remover(@PathVariable @NotNull Long id) {
        service.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmarPagamento(@PathVariable @NotNull Long id) {
        service.confirmarPagamento(id);
    }
}
