package br.com.arthur.food.pagamentos.http;

import br.com.arthur.food.pagamentos.config.Configuracao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "pedidos-ms", url = "http://localhost:8082/")
public interface PedidoClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/pedidos-ms/pedidos/{id}/pago")
    void atualizarPagamento(@PathVariable Long id);
}
