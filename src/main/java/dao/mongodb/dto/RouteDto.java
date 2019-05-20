package dao.mongodb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Route;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class RouteDto extends Route {

    private ObjectId objectId;

    public RouteDto(Long id, Long priceId, Long fromId, Long toId, String fromTime, String toTime, Double distance, ObjectId objectId) {
        super(id, priceId, fromId, toId, fromTime, toTime, distance);
        this.objectId = objectId;
    }

    public RouteDto(Route route) {
        super(route.getId(), route.getPriceId(), route.getFromId(), route.getToId(), route.getFromTime(), route.getToTime(), route.getDistance());

        if (RouteDto.class.equals(route.getClass())) {
            this.objectId = ((RouteDto) route).getObjectId();
        }
    }
}
