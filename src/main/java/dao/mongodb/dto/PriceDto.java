package dao.mongodb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Price;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class PriceDto extends Price {

    private ObjectId objectId;

    public PriceDto(Double compartmentFactor, Double deluxeFactor, Double berthFactor, ObjectId objectId) {
        super(compartmentFactor, deluxeFactor, berthFactor);
        this.objectId = objectId;
    }

    public PriceDto(Price price) {
        super(price.getCompartmentFactor(), price.getDeluxeFactor(), price.getBerthFactor());

        if (PriceDto.class.equals(price.getClass())) {
            this.objectId = ((PriceDto) price).getObjectId();
        }
    }
}
