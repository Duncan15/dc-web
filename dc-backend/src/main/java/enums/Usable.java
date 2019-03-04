package enums;

public enum  Usable {
    have(1),none(0);
    private int value;
    private Usable(int v) {
        this.value = v;
    }
    public int getValue() {
        return this.value;
    }

    /**
     * get the enum by value
     * @param v
     * @return
     */
    public static Usable valueOf(int v) {
        for (Usable d : Usable.values()) {
            if (d.value == v) {
                return d;
            }
        }
        return null;
    }

    /**
     * use this method to replace valueOf method
     * @param name
     * @return
     */
    public static Driver ValueOf(String name) {
        Driver d;
        try {
            d = Driver.valueOf(Driver.class, name);
        } catch (Exception ex) {
            //ignored
            d = null;
        }
        return d;
    }
}
