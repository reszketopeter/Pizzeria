package tutorial.pizzeria.dto.outgoing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewListItem {

    @Schema(description = "Customer's name", example = "Test Peter")
    private String customerName;

    @Schema(description = "A couple of words or sentences about the product",
            example = "The pizza was awesome. I recommend this pizzeria to everyone.")
    private String content;

    @Schema(description = "The time when the review was written", example = "2025.03.25.")
    private LocalDateTime timeStamp;
}
