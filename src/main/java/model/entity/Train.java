package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    private String id;
    private String routeId;

    private Integer compartmentFree;
    private Integer deluxeFree;
    private Integer berthFree;
}
