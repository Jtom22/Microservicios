package stock_service.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
 
import org.junit.jupiter.api.Test;
 
import stock_service.domain.exception.InsufficientStockException;
 

    //Test de dominio PURO: no hay @SpringBootTest, sin mocks, sin Mongo.
    // Porque el dominio desacoplado de Spring/Mongo/Kafka —
    // este test corre en milisegundos, sin levantar ningún contexto.

class StockItemTest {
 
    @Test
    void decrease_deberia_restar_la_cantidad_cuando_hay_stock_suficiente() {
        StockItem stockItem = new StockItem("prod-1", 100);
 
        stockItem.decrease(30);
 
        assertThat(stockItem.getQuantity()).isEqualTo(70);
    }
 
    @Test
    void decrease_deberia_lanzar_InsufficientStockException_si_no_alcanza_el_stock() {
        StockItem stockItem = new StockItem("prod-1", 10);
 
        assertThatThrownBy(() -> stockItem.decrease(50))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("prod-1")
                .hasMessageContaining("disponible=10")
                .hasMessageContaining("solicitado=50");
 
        // Importante: verificar que el stock NO cambió cuando la operación falla.
        // Si esto no se cumple, el dominio quedaría en un estado inconsistente.
        assertThat(stockItem.getQuantity()).isEqualTo(10);
    }
 
    @Test
    void decrease_deberia_permitir_dejar_el_stock_exactamente_en_cero() {
        StockItem stockItem = new StockItem("prod-1", 20);
 
        stockItem.decrease(20);
 
        assertThat(stockItem.getQuantity()).isZero();
    }
 
    @Test
    void decrease_deberia_rechazar_una_cantidad_negativa_o_cero() {
        StockItem stockItem = new StockItem("prod-1", 100);
 
        assertThatThrownBy(() -> stockItem.decrease(0))
                .isInstanceOf(IllegalArgumentException.class);
 
        assertThatThrownBy(() -> stockItem.decrease(-5))
                .isInstanceOf(IllegalArgumentException.class);
    }
 
    @Test
    void increase_deberia_sumar_la_cantidad() {
        StockItem stockItem = new StockItem("prod-1", 50);
 
        stockItem.increase(25);
 
        assertThat(stockItem.getQuantity()).isEqualTo(75);
    }
 
    @Test
    void increase_deberia_rechazar_una_cantidad_negativa_o_cero() {
        StockItem stockItem = new StockItem("prod-1", 50);
 
        assertThatThrownBy(() -> stockItem.increase(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}