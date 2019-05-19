package model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private ObjectId id;
    private ObjectId userId;
    private ObjectId trainId;

    private String type;

    private Double price;


    public static class RequestBuilder {
        private model.entity.dto.RequestDTO request;

        public RequestBuilder() {
            request = new  model.entity.dto.RequestDTO();
        }

        public  model.entity.dto.RequestDTO.RequestBuilder setUserId(ObjectId id) {
            request.setUserId(id);
            return this;
        }

        public  model.entity.dto.RequestDTO.RequestBuilder setTrainId(ObjectId id) {
            request.setTrainId(id);
            return this;
        }

        public model.entity.dto.RequestDTO.RequestBuilder setPrice(Double price) {
            request.setPrice(price);
            return this;
        }

        public  model.entity.dto.RequestDTO.RequestBuilder setType(String type) {
            request.setType(type);
            return this;
        }

        public  model.entity.dto.RequestDTO build() {
            return request;
        }
    }
}

