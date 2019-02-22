package enums;

public enum  RunningMode {
    structed(1),unstructed(0);
    private int value;
    private RunningMode(int v) {
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
    public static RunningMode valueOf(int v) {
        for (RunningMode d : RunningMode.values()) {
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
    public static RunningMode ValueOf(String name) {
        RunningMode d;
        try {
            d = RunningMode.valueOf(RunningMode.class, name);
        } catch (Exception ex) {
            //ignored
            d = null;
        }
        return d;
    }
}
