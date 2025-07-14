package co.com.bancolombia.model.movement;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Movement {

    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private MovementType type;
    private BigDecimal amount;
    private String currency;
    private String description;

    public void executeValidations() {
        this.amountValidation();
        this.descriptionValidation();
        this.dateISO8601();
    }

    private void descriptionValidation() {
        if(this.description.isEmpty()) throw new IllegalArgumentException("Description cannot be empty");
    }

    private void amountValidation() {
        if (this.amount.longValue() < 0) throw new IllegalArgumentException("Amount cannot be negative");
    }

    private void dateISO8601() {
        try {
            if (this.date != null) {
                String dateString = this.date.toString();
                LocalDateTime.parse(dateString);
            } else {
                throw new IllegalArgumentException("Date cannot be null");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date must be in ISO-8601 format (yyyy-MM-dd'T'HH:mm:ss)");
        }
    }

}
