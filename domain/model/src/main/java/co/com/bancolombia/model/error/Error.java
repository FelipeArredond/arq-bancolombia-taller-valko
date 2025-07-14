package co.com.bancolombia.model.error;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Error {
    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String errorMessage;
}
