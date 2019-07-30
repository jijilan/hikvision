package equment.hkwslin;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import equment.ConGasStationMonitoring;

import javax.swing.*;

/**
 * @Author jijl
 * @Description: 用户注册
 * @Date 17:20 2019/7/11
 * @Param
 * @return
 **/
public class DeviceClient extends JFrame {
    /**
     * 函数:      主类构造函数
     * 函数描述:	初始化成员
     **/
    public DeviceClient() {
        lUserID = new NativeLong(-1);
        lPreviewHandle = new NativeLong(-1);
        m_lPort = new NativeLongByReference(new NativeLong(-1));
        m_iTreeNodeNum = 0;
    }

    static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    /**
     * 设备信息
     **/
    HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;
    /**
     * 已登录设备的IP地址
     **/
    String m_sDeviceIP;
    /**
     * 用户句柄
     **/
    NativeLong lUserID;
    /**
     * 预览句柄
     **/
    NativeLong lPreviewHandle;
    /**
     * 回调预览时播放库端口指针
     **/
    NativeLongByReference m_lPort;
    /**
     * 通道树节点数目
     **/
    int m_iTreeNodeNum;


    /**
     * @return boolean
     * @Author jijl
     * @Description: 注册
     * @Date 9:08 2019/7/12
     * @Param [drvice 设备信息]
     **/
    public long Login_V30(ConGasStationMonitoring drvice) {
        //注册
        m_sDeviceIP = drvice.getDeviceIp();
        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP, (short) 8000, drvice.getDeviceUsername(), drvice.getDevicePassword(), m_strDeviceInfo);
        long userID = lUserID.longValue();
        if (userID == -1) {
            System.out.println("注册失败");
            return -1;
        } else {
            System.out.println("注册成功");
            return userID;

        }
    }


    /**
     * @return boolean
     * @Author jijl
     * @Description: 注销
     * @Date 9:08 2019/7/12
     * @Param [drvice 设备信息]
     **/
    public boolean Logout_V30(long userId) {
        //如果已经注册,注销
        if (lUserID.longValue() > -1 && hCNetSDK.NET_DVR_Logout_V30(lUserID)) {
            //cleanup SDK
            return hCNetSDK.NET_DVR_Cleanup();
        }
        return false;
    }


    /**
     * @return boolean
     * @Author jijl
     * @Description: 初始化
     * @Date 17:21 2019/7/11
     **/
    public boolean Init() {
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true) {
            System.out.println("初始化失败");
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return void
     * @Author jijl
     * @Description: 下载
     * @Date 9:10 2019/7/12
     * @Param [drvice, struStartTime, struStopTime]
     **/
    public void jButtonDownloadActionPerformed(ConGasStationMonitoring drvice, HCNetSDK.NET_DVR_TIME struStartTime, HCNetSDK.NET_DVR_TIME struStopTime, String strFileName) {
        PlaybackService dlgPlayTime = new PlaybackService(this, false, lUserID, m_sDeviceIP);
        dlgPlayTime.jButtonDownloadActionPerformed(drvice, struStartTime, struStopTime, strFileName);
    }

    /**
     * @return
     * @Author jijl
     * @Description: 播放
     * @Date 9:10 2019/7/12
     * @Param
     **/
    public void jButtonPlayActionPerformed(ConGasStationMonitoring drvice,
                                           HCNetSDK.NET_DVR_TIME struStartTime,
                                           HCNetSDK.NET_DVR_TIME struStopTime, String strFileName) {
        PlaybackService dlgPlayTime = new PlaybackService(this, false, lUserID, m_sDeviceIP);
        dlgPlayTime.jButtonPlayActionPerformed(drvice, struStartTime, struStopTime, strFileName);
    }

    /**
     * @return void
     * @Author jijl
     * @Description: 停止
     * @Date 9:10 2019/7/12
     * @Param [value]
     **/
    public void jButtonStopActionPerformed(long value) {
        NativeLong m_lPlayHandleint = new NativeLong(value);
        PlaybackService dlgPlayTime = new PlaybackService(this, false, lUserID, m_sDeviceIP);
        dlgPlayTime.StopPlay(m_lPlayHandleint);
    }

    /**
     * @return void
     * @Author jijl
     * @Description: 播放
     * @Date 9:22 2019/7/26
     * @Param [drvice]
     **/
    public void jButtonRealPlayActionPerformed(ConGasStationMonitoring drvice, String strFileName) {
        PlaybackService playbackService = new PlaybackService(this, false, lUserID, m_sDeviceIP);
        playbackService.jButtonRealPlayActionPerformed(drvice, strFileName);
    }


}


