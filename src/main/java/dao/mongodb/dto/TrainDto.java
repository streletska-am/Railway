package dao.mongodb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Train;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class TrainDto extends Train {

    private ObjectId objectId;

    public TrainDto(Long id, Long routeId, Integer compartmentFree, Integer deluxeFree, Integer berthFree, ObjectId objectId) {
        super(id, routeId, compartmentFree, deluxeFree, berthFree);
        this.objectId = objectId;
    }

    public TrainDto(Train train) {
        super(train.getId(), train.getRouteId(), train.getCompartmentFree(), train.getDeluxeFree(), train.getBerthFree());

        if (TrainDto.class.equals(train.getClass())) {
            this.objectId = ((TrainDto) train).getObjectId();
        }
    }
}
