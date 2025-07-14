package co.com.bancolombia.mongo;

import co.com.bancolombia.model.movement.MovementType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("Movements")
@Data
public class MovementData {

    @Id
    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private MovementType type;
    private BigDecimal amount;
    private String currency;
    private String description;

}
