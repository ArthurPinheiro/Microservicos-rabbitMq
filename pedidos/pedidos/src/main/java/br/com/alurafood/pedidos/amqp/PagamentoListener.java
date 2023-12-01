package br.com.alurafood.pedidos.amqp;

import br.com.alurafood.pedidos.dto.PagamentoDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoListener {

    @RabbitListener(queues = "pagamento.concluido")
    public void recebeMensagem(PagamentoDto pagamento) {
        String mensagem = """
                Dados do pagamento: %s
                Cliente: %s
                Número do pedido: %s
                Valor R$: %s
                Status: %s
                """.formatted(pagamento.getId(), pagamento.getNome(), pagamento.getPedidoId(),
                pagamento.getValor(), pagamento.getStatus());
        System.out.println("Recebi a mensagem " + mensagem);
    }
}
