package model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    private ObjectId id;
    private ObjectId priceId;

    private ObjectId fromId;
    private ObjectId toId;

    private String fromTime;
    private String toTime;

    private Double distance;

}
