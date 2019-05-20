package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainRoute {
    private Long trainId;
    private Long routeId;

    private Integer compartmentFree;
    private Integer deluxeFree;
    private Integer berthFree;

    private String fromDate;
    private String toDate;

    private String fromCity;
    private String toCity;

    private Double compartmentPrice;
    private Double deluxePrice;
    private Double berthPrice;

    private Double distance;
}
