package co.com.bancolombia.filereader;

import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.MovementType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Component
public class LineParser {

    public Movement parseMovementLine(String line, String boxId) {
        String[] parts = line.split(",");
        try {
            return Movement.builder()
                    .movementId(parts[0])
                    .boxId(parts[1])
                    .date(LocalDateTime.parse(parts[2])) // ISO format: yyyy-MM-dd'T'HH:mm:ss
                    .type(MovementType.valueOf(parts[3].toUpperCase()))
                    .amount(new BigDecimal(parts[4]))
                    .currency(parts[5])
                    .description(parts[6])
                    .build();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected dd/MM/yyyy, got: " + parts[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing line: " + line + ". " + e.getMessage());
        }
    }

}
