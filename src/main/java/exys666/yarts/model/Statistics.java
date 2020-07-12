package exys666.yarts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Statistics(
        @JsonProperty("avg")double avg,
        @JsonProperty("max")double max,
        @JsonProperty("min")double min,
        @JsonProperty("count")long count
) {
}
