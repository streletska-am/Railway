package dao.mongodb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Station;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class StationDto extends Station {

    private ObjectId objectId;

    public StationDto(Long id, String name, ObjectId objectId) {
        super(id, name);
        this.objectId = objectId;
    }

    public StationDto(Station station) {
        super(station.getId(), station.getName());

        if (StationDto.class.equals(station.getClass())) {
            this.objectId = ((StationDto) station).getObjectId();
        }
    }
}
