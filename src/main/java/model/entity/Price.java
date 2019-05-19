package model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Price {
    private Long id;


    private Double compartmentFactor;
    private Double deluxeFactor;
    private Double berthFactor;

    public Price(Double compartmentFactor, Double deluxeFactor, Double berthFactor) {
        this.compartmentFactor = compartmentFactor;
        this.deluxeFactor = deluxeFactor;
        this.berthFactor = berthFactor;
    }
}
