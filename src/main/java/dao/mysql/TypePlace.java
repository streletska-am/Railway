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

    public static TypePlace get(int index) {
        return TypePlace.values()[index];
    }

    public static int count() {
        return TypePlace.values().length;
    }
}
