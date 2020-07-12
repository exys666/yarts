package exys666.yarts.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

public record Tick (
        @NotEmpty String instrument,
        @NotNull Double price,
        @NotNull Instant timestamp
) {

}
