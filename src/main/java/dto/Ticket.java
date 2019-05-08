package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String requestId;
    private String trainId;
    private String userId;

    private String name;
    private String surname;

    private String fromDate;
    private String toDate;
    private String fromCity;
    private String toCity;

    private String typePlace;
    private Long max;

    private Double price;
}
