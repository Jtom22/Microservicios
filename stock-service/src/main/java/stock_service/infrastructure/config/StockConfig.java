package stock_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stock_service.application.StockService;
import stock_service.domain.port.in.StockUseCase;
import stock_service.domain.port.out.StockMovementRepositoryPort;
import stock_service.domain.port.out.StockRepositoryPort;

@Configuration
public class StockConfig {

    //Registramos que existe el service porque es java puro
    @Bean
    public StockUseCase decreaseStockUseCase(
            StockRepositoryPort stockRepository,
            StockMovementRepositoryPort movementRepository) {

        return new StockService(stockRepository, movementRepository);
    }
}
