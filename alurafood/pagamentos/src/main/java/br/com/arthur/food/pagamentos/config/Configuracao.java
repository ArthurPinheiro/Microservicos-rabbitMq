package br.com.arthur.food.pagamentos.config;

import feign.Contract;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuracao {

    @Bean
    public ModelMapper obterModelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
//    }
}
