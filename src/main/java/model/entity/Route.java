package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private String id;
    private String priceId;

    private String fromId;
    private String toId;

    private String fromTime;
    private String toTime;

    private Double distance;


}
