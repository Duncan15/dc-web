package enums;

public enum Driver {
    have(1),none(0);
    private int value;
    private Driver(int v) {
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
    public static Driver valueOf(int v) {
        for (Driver d : Driver.values()) {
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
