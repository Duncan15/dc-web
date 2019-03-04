package enums;

public enum  MonitorOption {
    start, stop;
    /**
     * use this method to replace valueOf method
     * @param name
     * @return
     */
    public static MonitorOption ValueOf(String name) {
        MonitorOption d;
        try {
            d = MonitorOption.valueOf(MonitorOption.class, name);
        } catch (Exception ex) {
            //ignored
            d = null;
        }
        return d;
    }
}
