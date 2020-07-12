package exys666.yarts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record Tick(
        @JsonProperty("instrument") @NotEmpty String instrument,
        @JsonProperty("price") @NotNull Double price,
        @JsonProperty("timestamp") @NotNull Long timestamp
) {

}
