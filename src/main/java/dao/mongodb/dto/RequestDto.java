package dao.mongodb.dto;

import dao.mysql.TypePlace;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Request;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class RequestDto extends Request {

    private ObjectId objectId;

    public RequestDto(Long id, Long userId, Long trainId, TypePlace type, Double price, ObjectId objectId) {
        super(id, userId, trainId, type, price);
        this.objectId = objectId;
    }

    public RequestDto(Request request) {
        super(request.getId(), request.getUserId(), request.getTrainId(), request.getType(), request.getPrice());

        if (RequestDto.class.equals(request.getClass())) {
            this.objectId = ((RequestDto) request).getObjectId();
        }
    }
}
