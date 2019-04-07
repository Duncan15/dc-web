package enums;

public enum  Base {
    urlBased(0), apiBased(1), jsonBase(2);
    private int v;
    private Base(int v) {
        this.v = v;
    }
    public int getValue() {
        return this.v;
    }

    public static Base valueOf(int v) {
        for (Base b : Base.values()) {
            if (b.v == v) {
                return b;
            }
        }
        return null;
    }
    public static Base ValueOf(String v) {
        Base b;
        try {
            b = Base.valueOf(v);
        } catch (Exception ex) {
            //ignored
            b = null;
        }
        return  b;
    }
}
