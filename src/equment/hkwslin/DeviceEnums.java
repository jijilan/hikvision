package equment.hkwslin;

/**
 * @Author jijl
 * @Description: 设备参数枚举
 * @Date 16:56 2019/7/11
 * @Param
 * @return
 **/
public enum DeviceEnums {
    webapp("webapp", "/www/server/tomcat/webapps/", "webapp"),
    playUrl("playUrl", "hkwslin/play/", "播放缓存地址"),
    downloadUrl("downloadUrl", "hkwslin/download/", "下载地址"),
    libsUrl("downloadUrl", "libs/hkwslin/", "库文件地址"),
    projectPath("projectPath", "http://172.17.30.11:8080/", "项目地址");
    private String key;
    private String value;
    private String remark;

    private DeviceEnums(String key, String value, String remark) {
        this.key = key;
        this.value = value;
        this.remark = remark;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getRemark() {
        return remark;
    }
}
