package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long requestId;
    private Long trainId;
    private Long userId;

    private String name;
    private String surname;

    private String fromDate;
    private String toDate;
    private String fromCity;
    private String toCity;

    private String typePlace;
    private Integer max;

    private Double price;
}
