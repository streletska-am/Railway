package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private Long id;
    private Long priceId;

    private Long fromId;
    private Long toId;

    private String fromTime;
    private String toTime;

    private Double distance;


}
