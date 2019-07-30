package equment;

/**
 * @Author jijl
 * @Description: 设备信息实体类
 * @Date 17:30 2019/7/30
 * @Param
 * @return
 **/
public class ConGasStationMonitoring {

    //id
    private Integer monitoringId;
    //设备ip
    private String deviceIp;
    //通道号
    private Integer deviceChannel;
    //设备密码
    private String devicePassword;
    //设备登录名
    private String deviceUsername;
    //监控名称
    private String monitoringName;


    /**
     * id
     *
     * @return
     */
    public Integer getMonitoringId() {
        return monitoringId;
    }

    /**
     * id
     *
     * @param monitoringId
     */
    public void setMonitoringId(Integer monitoringId) {
        this.monitoringId = monitoringId;
    }

    /**
     * 设备ip
     *
     * @return
     */
    public String getDeviceIp() {
        return deviceIp;
    }

    /**
     * 设备ip
     *
     * @param deviceIp
     */
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    /**
     * 通道号
     *
     * @return
     */
    public Integer getDeviceChannel() {
        return deviceChannel;
    }

    /**
     * 通道号
     *
     * @param deviceChannel
     */
    public void setDeviceChannel(Integer deviceChannel) {
        this.deviceChannel = deviceChannel;
    }

    /**
     * 设备密码
     *
     * @return
     */
    public String getDevicePassword() {
        return devicePassword;
    }

    /**
     * 设备密码
     *
     * @param devicePassword
     */
    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

    /**
     * 设备登录名
     *
     * @return
     */
    public String getDeviceUsername() {
        return deviceUsername;
    }

    /**
     * 设备登录名
     *
     * @param deviceUsername
     */
    public void setDeviceUsername(String deviceUsername) {
        this.deviceUsername = deviceUsername;
    }

    /**
     * 监控名称
     *
     * @return
     */
    public String getMonitoringName() {
        return monitoringName;
    }

    /**
     * 监控名称
     *
     * @param monitoringName
     */
    public void setMonitoringName(String monitoringName) {
        this.monitoringName = monitoringName;
    }


}
