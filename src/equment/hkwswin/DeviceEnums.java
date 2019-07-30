package equment.hkwswin;

/**
 * @Author jijl
 * @Description: 设备参数枚举
 * @Date 16:56 2019/7/11
 * @Param
 * @return
 **/
public enum DeviceEnums {

    playUrl("playUrl", "C:\\Users\\JIJL\\Desktop\\sdk\\video\\", "播放缓存地址"),
    downloadUrl("downloadUrl", "C:\\Users\\JIJL\\Desktop\\sdk\\lib\\", "下载地址"),
    libsUrl("downloadUrl", "C:\\Users\\JIJL\\Desktop\\sdk\\CH-HCNetSDKV6.0.2.35_build20190411_Win64\\库文件\\", "库文件地址"),
    projectPath("projectPath", "http://127.0.0.1:8080/", "项目地址");
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
