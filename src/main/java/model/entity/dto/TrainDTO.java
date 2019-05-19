package model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainDTO {
    private ObjectId id;
    private ObjectId routeId;

    private Integer compartmentFree;
    private Integer deluxeFree;
    private Integer berthFree;
}
