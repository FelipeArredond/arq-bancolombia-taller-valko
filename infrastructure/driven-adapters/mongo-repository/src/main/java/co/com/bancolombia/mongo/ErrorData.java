package co.com.bancolombia.mongo;

import co.com.bancolombia.model.movement.MovementType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("Errors")
@Data
public class ErrorData {

    @Id
    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String errorMessage;

}
