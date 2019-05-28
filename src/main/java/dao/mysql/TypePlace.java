package dao.mysql;

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
}
