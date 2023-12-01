package br.com.arthur.food.pagamentos.service;

import br.com.arthur.food.pagamentos.dto.PagamentoDto;
import br.com.arthur.food.pagamentos.http.PedidoClient;
import br.com.arthur.food.pagamentos.model.Pagamento;
import br.com.arthur.food.pagamentos.model.Status;
import br.com.arthur.food.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMappper;

    @Autowired
    private PedidoClient pedidoClient;

    public Page<PagamentoDto> obterTodos(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(p -> modelMappper.map(p, PagamentoDto.class));
    }

    public PagamentoDto obterPorId(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return modelMappper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto criarPagamento(PagamentoDto dto) {
        Pagamento pagamento = modelMappper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);
        return modelMappper.map(pagamento, PagamentoDto.class);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto) {
        Pagamento pagamento = modelMappper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMappper.map(pagamento, PagamentoDto.class);
    }

    public void excluirPagamento(Long id) {
        repository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);

        if(pagamento.isEmpty()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedidoClient.atualizarPagamento(pagamento.get().getPedidoId());
    }
}
