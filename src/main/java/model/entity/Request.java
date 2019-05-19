package model.entity;

import dao.mysql.TypePlace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Long id;
    private Long userId;
    private Long trainId;

    private TypePlace type;

    private Double price;


    public static class RequestBuilder {
        private Request request;

        public RequestBuilder() {
            request = new Request();
        }

        public RequestBuilder setUserId(Long id) {
            request.setUserId(id);
            return this;
        }

        public RequestBuilder setTrainId(Long id) {
            request.setTrainId(id);
            return this;
        }

        public RequestBuilder setPrice(Double price) {
            request.setPrice(price);
            return this;
        }

        public RequestBuilder setType(TypePlace type) {
            request.setType(type);
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
