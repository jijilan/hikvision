package equment.hkwslin;


import equment.ConGasStationMonitoring;

/**
 * @Author jijl
 * @Description: 回放视频播放及下载接口
 * @Date 17:33 2019/7/11
 * @Param
 * @return
 **/
public class Playback extends Thread {
    /**
     * 设备信息  主要用到 账号、密码、通道、
     **/
    private ConGasStationMonitoring drvice;
    /**
     * 0:下载  1：回放  2：停止回放 3 播放
     **/
    private int type;
    /**
     * 根据播放句柄停止播放
     **/
    private long m_lPlayHandleint;
    /**
     * 开始时间
     **/
    private HCNetSDK.NET_DVR_TIME struStartTime;
    /**
     * 结束时间
     **/
    private HCNetSDK.NET_DVR_TIME struStopTime;

    /**
     * 下载文件名称
     **/
    private String strFileName;

    public Playback(int typeb,
                    long m_lPlayHandleintb,
                    ConGasStationMonitoring drviceBackb,
                    HCNetSDK.NET_DVR_TIME struStartTimeb,
                    HCNetSDK.NET_DVR_TIME struStopTimeb, String strFileNameb) {
        this.type = typeb;
        this.m_lPlayHandleint = m_lPlayHandleintb;
        this.drvice = drviceBackb;
        this.struStartTime = struStartTimeb;
        this.struStopTime = struStopTimeb;
        this.strFileName = strFileNameb;
    }

    @Override
    public void run() {
        DeviceClient cli = new DeviceClient();
        //初始化
        if (cli.Init()) {
            //注册
            long userId = cli.Login_V30(drvice);
            if (userId != -1) {
                if (type == 0) {
                    //下载
                    cli.jButtonDownloadActionPerformed(drvice, struStartTime, struStopTime, strFileName);
                } else if (type == 1) {
                    //回放
                    cli.jButtonPlayActionPerformed(drvice, struStartTime, struStopTime, strFileName);
                } else if (type == 2) {
                    //停止回放
                    cli.jButtonStopActionPerformed(m_lPlayHandleint);
                } else if (type == 3) {
                    //播放
                    cli.jButtonRealPlayActionPerformed(drvice, strFileName);
                }
            }
            //注销 cleanup SDK
            cli.Logout_V30(userId);
        }
    }

    public static void main(String[] args) {
        ConGasStationMonitoring drvice = new ConGasStationMonitoring();
        drvice.setDeviceIp("172.17.10.30");
        drvice.setDeviceUsername("test");
        drvice.setDevicePassword("test123456");
        drvice.setDeviceChannel(34);
        Playback playback = new Playback(3, 0L, drvice, Utils.getDvrTime("2019-07-6 00:00:00"), Utils.getDvrTime("2019-07-6 00:00:15"), DeviceEnums.webapp.getValue() + DeviceEnums.playUrl.getValue() + "123.mp4");
        playback.start();
    }
}
