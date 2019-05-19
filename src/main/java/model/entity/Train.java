package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    private Long id;
    private Long routeId;

    private Integer compartmentFree;
    private Integer deluxeFree;
    private Integer berthFree;
}
