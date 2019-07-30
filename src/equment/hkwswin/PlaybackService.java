

package equment.hkwswin;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import equment.ConGasStationMonitoring;
import equment.hkwslin.HCNetSDK;
import equment.hkwslin.Utils;

import javax.swing.*;
import java.util.Timer;

/**
 * 类 ：PlaybackService
 * 类描述 ：远程按时间回放操作
 **/
public class PlaybackService extends JDialog {

    static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    /**
     * 用户ID
     **/
    NativeLong m_lUserID;
    /**
     * 播放句柄
     **/
    NativeLong m_lPlayHandle;
    /**
     * 下载句柄
     **/
    NativeLong m_lLoadHandle;
    /**
     * 设备IP
     **/
    String m_sDeviceIP;
    /**
     * 回放通道
     **/
    int m_iChanShowNum;
    /**
     * 下载用定时器
     **/
    Timer downloadtimer;
    /**
     * 回放用定时器
     **/
    Timer playbacktimer;
    /**
     * 预览句柄
     **/
    NativeLong lPreviewHandle;

    /**
     * 下载文件名称
     **/
    private String strFileName;

    /**
     * 函数:      PlaybackService
     * 函数描述:  构造函数   Creates new form PlaybackService
     **/
    public PlaybackService(java.awt.Frame parent, boolean modal, NativeLong lUserID, String sIP) {
        super(parent, modal);
        m_lUserID = lUserID;
        m_lPlayHandle = new NativeLong(-1);
        m_lLoadHandle = new NativeLong(-1);
        m_iChanShowNum = 0;
        m_sDeviceIP = sIP;
    }

    /*************************************************
     函数:      "下载"  按钮单击相应函数
     函数描述:   开始或停止下载文件
     *************************************************/
    public void jButtonDownloadActionPerformed(ConGasStationMonitoring drvice, HCNetSDK.NET_DVR_TIME struStartTime, HCNetSDK.NET_DVR_TIME struStopTime, String strFileName) {
        this.strFileName = strFileName;
        if (m_lLoadHandle.intValue() == -1) {
            m_lLoadHandle = hCNetSDK.NET_DVR_GetFileByTime(m_lUserID, new NativeLong(drvice.getDeviceChannel()), struStartTime, struStopTime, strFileName);
            if (m_lLoadHandle.intValue() >= 0) {
                hCNetSDK.NET_DVR_PlayBackControl(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);
                downloadtimer = new Timer();
                downloadtimer.schedule(new DownloadTask(), 0, 5000);
            } else {
                System.out.println("按时间下载失败 error " + hCNetSDK.NET_DVR_GetLastError());
                return;
            }
        } else {
            hCNetSDK.NET_DVR_StopGetFile(m_lLoadHandle);
            downloadtimer.cancel();
        }

//        String sFileName = "C:\\Users\\JIJL\\Desktop\\sdk\\video\\3541241.mp4" ;
//        System.out.println(sFileName);
//        HCNetSDK.NET_DVR_PLAYCOND struDownloadCond = new HCNetSDK.NET_DVR_PLAYCOND();
//        struDownloadCond.dwChannel = 34;
//        struDownloadCond.struStartTime = struStartTime;
//        struDownloadCond.struStopTime = struStopTime;
//        m_lLoadHandle = hCNetSDK.NET_DVR_GetFileByTime_V40(m_lUserID, sFileName, struDownloadCond);
//        if (m_lLoadHandle.intValue() >= 0) {
//            hCNetSDK.NET_DVR_PlayBackControl(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);
//            playbacktimer = new Timer();
//            playbacktimer.schedule(new PlaybackTask(), 0, 5000);
//        } else {
//            System.out.println("laste error " + hCNetSDK.NET_DVR_GetLastError());
//            return;
//        }

    }
    /*************************************************
     类:      PlaybackTask
     类描述:  回放定时器响应函数
     *************************************************/
    class PlaybackTask extends java.util.TimerTask {
        //定时器函数
        @Override
        public void run() {
            IntByReference nPos = new IntByReference(0);
            if (m_lPlayHandle.intValue() >= 0) {
                if (hCNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0, nPos)) {
                    System.out.println("回放进度" + nPos.getValue());
                } else {
                    StopPlay(m_lPlayHandle);
                    System.out.println("获取回放进度失败,终止");
                }

                if (nPos.getValue() > 100) {
                    StopPlay(m_lPlayHandle);
                    System.out.println("由于网络原因或DVR忙,回放异常终止!");
                }
                if (nPos.getValue() == 100) {
                    StopPlay(m_lPlayHandle);
                    System.out.println("按时间回放结束");
                }
            }
        }
    }

    /*************************************************
     函数:      StopPlay
     函数描述:  停止回放的相关操作
     *************************************************/
    public void StopPlay(NativeLong m_lPlayHandles) {
        hCNetSDK.NET_DVR_PlayBackControl(m_lPlayHandles, HCNetSDK.NET_DVR_PLAYSTOPAUDIO, 0, null);
        hCNetSDK.NET_DVR_StopPlayBack(m_lPlayHandles);
        m_lPlayHandles.setValue(-1);
    }

    /*************************************************
     类:      DownloadTask
     类描述:  下载定时器响应函数
     *************************************************/
    class DownloadTask extends java.util.TimerTask {
        //定时器函数
        @Override
        public void run() {
            IntByReference nPos = new IntByReference(0);
            hCNetSDK.NET_DVR_PlayBackControl(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0, nPos);
            if (nPos.getValue() > 100) {
                hCNetSDK.NET_DVR_StopGetFile(m_lLoadHandle);
                m_lLoadHandle.setValue(-1);
                downloadtimer.cancel();
                System.out.println("由于网络原因或DVR忙,下载异常终止!");
            }
            if (nPos.getValue() == 100) {
                hCNetSDK.NET_DVR_StopGetFile(m_lLoadHandle);
                m_lLoadHandle.setValue(-1);
                downloadtimer.cancel();
                System.out.println("按时间下载结束！");
            }
        }
    }

    /**
     * 函数:      "播放"  按钮单击相应函数
     **/
    public void jButtonPlayActionPerformed(ConGasStationMonitoring drvice,
                                           HCNetSDK.NET_DVR_TIME struStartTime,
                                           HCNetSDK.NET_DVR_TIME struStopTime, String strFileName) {
        this.strFileName = strFileName;
        HCNetSDK.NET_DVR_VOD_PARA struVoidParam = new HCNetSDK.NET_DVR_VOD_PARA();
        struVoidParam.dwSize = struVoidParam.size();
        struVoidParam.struBeginTime = struStartTime;
        struVoidParam.struEndTime = struStopTime;
        struVoidParam.hWnd = null;
        HCNetSDK.NET_DVR_STREAM_INFO struInfo = new HCNetSDK.NET_DVR_STREAM_INFO();
        struInfo.dwSize = struInfo.size();
        struInfo.dwChannel = drvice.getDeviceChannel();
        struVoidParam.struIDInfo = struInfo;
            m_lPlayHandle = hCNetSDK.NET_DVR_PlayBackByTime_V40(m_lUserID, struVoidParam);
        Boolean black = hCNetSDK.NET_DVR_SetPlayDataCallBack(m_lPlayHandle, fPlayDataCallBack, 0);
        if (m_lPlayHandle.intValue() == -1) {
            return;
        } else {
            hCNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);
        }

        if (hCNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYSTARTAUDIO, 0, null)) {
            hCNetSDK.NET_DVR_PlayBackControl(m_lPlayHandle, HCNetSDK.NET_DVR_PLAYAUDIOVOLUME, (0xffff) / 2, null);
        }

        try {
            Thread.sleep(60000 * 1);
            //结束后删除文件
            Utils.deleteFile(strFileName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 函数:      "Play"  回调函数
     * 函数描述:  开始或暂停按时间回放
     **/
    HCNetSDK.FPlayDataCallBack fPlayDataCallBack = (lPlayHandle, dwDataType, pBuffer, dwBufSize, dwUser) -> {
        System.out.println("回调-------------");
        Utils.createrFile(lPlayHandle, dwDataType, pBuffer, dwBufSize, m_sDeviceIP, strFileName);
    };



    /*************************************************
     函数:      "预览"  按钮单击响应函数
     函数描述:	获取通道号,打开播放窗口,开始此通道的预览
     *************************************************/
    public void jButtonRealPlayActionPerformed(ConGasStationMonitoring drvice, String strFileName) {
        this.strFileName = strFileName;
        if (m_lUserID.intValue() == -1) {
            System.out.println("请先注册");
            return;
        }
        HCNetSDK.NET_DVR_CLIENTINFO m_strClientInfo;
        m_strClientInfo = new HCNetSDK.NET_DVR_CLIENTINFO();
        m_strClientInfo.lChannel = new NativeLong(drvice.getDeviceChannel());
        lPreviewHandle = hCNetSDK.NET_DVR_RealPlay_V30(m_lUserID,
                m_strClientInfo, fRealDataCallBack, null, true);
        long previewSucValue = lPreviewHandle.longValue();
        //预览失败时:
        if (previewSucValue == -1) {
            System.out.println("预览失败");
            return;
        }
        try {
            //10分钟后停止处理  此时文件在500-600兆
            Thread.sleep(60000 * 10);
            //结束后删除文件
            Utils.deleteFile(strFileName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    HCNetSDK.FRealDataCallBack_V30 fRealDataCallBack = (NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) -> {
        Utils.createrFile(lRealHandle, dwDataType, pBuffer, dwBufSize, m_sDeviceIP, strFileName);
    };

}
