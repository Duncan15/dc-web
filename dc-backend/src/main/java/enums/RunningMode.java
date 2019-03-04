package enums;

public enum  RunningMode {
    structed,unstructed;



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
