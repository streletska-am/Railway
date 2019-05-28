package dao.mysql;

import model.entity.Price;
import model.entity.Route;

import java.math.BigDecimal;

public enum TypePlace {
    B {
        @Override
        public String toString() {
            return "B";
        }
    },
    C {
        @Override
        public String toString() {
            return "C";
        }
    },
    L {
        @Override
        public String toString() {
            return "L";
        }
    };

    public static final int SIZE = 3;

    private static final TypePlace[] VALUES = TypePlace.values();

    public static TypePlace get(int index) {
        return VALUES[index];
    }

    public double calculatePrice(Price price, Route route) {
        double value;
        switch (this) {
            case C:
                value = price.getCompartmentFactor() * route.getDistance();
                break;
            case B:
                value = price.getBerthFactor() * route.getDistance();
                break;
            default:
                value = price.getDeluxeFactor() * route.getDistance();
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
