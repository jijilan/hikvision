package equment.hkwslin;


import com.sun.jna.NativeLong;
import com.sun.jna.ptr.ByteByReference;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    // 获取加载库
    public static String getLoadLibraryUrl(String library) {
        String loadLibrary = DeviceEnums.webapp.getValue() + DeviceEnums.libsUrl.getValue();
        return loadLibrary + library;
    }

    /**
     * @return equment.hkwslin.HCNetSDK.NET_DVR_TIME
     * @Author jijl
     * @Description: 日期转化成设备上提供封装类
     * @Date 17:48 2019/7/11
     * @Param [dataStr]
     **/
    public static HCNetSDK.NET_DVR_TIME getDvrTime(String dataStr) {
        HCNetSDK.NET_DVR_TIME time = new HCNetSDK.NET_DVR_TIME();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(dataStr));
            time.dwYear = cal.get(Calendar.YEAR);
            time.dwMonth = cal.get(Calendar.MONTH) + 1;
            time.dwDay = cal.get(Calendar.DAY_OF_MONTH);
            time.dwHour = cal.get(Calendar.HOUR);
            time.dwMinute = cal.get(Calendar.MINUTE);
            time.dwSecond = cal.get(Calendar.SECOND);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @return java.lang.Boolean
     * @Author jijl
     * @Description: 视频流解析
     * @Date 17:37 2019/7/30
     * @Param [lPlayHandle, dwDataType, pBuffer, dwBufSize, m_sDeviceIP, strFileName]
     **/
    public static Boolean createrFile(NativeLong lPlayHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, String m_sDeviceIP, String strFileName) {
        System.out.println("回调==================" + dwDataType);
        if (dwDataType == 2) {
            File file = new File(strFileName);
            //文件不存在则创建新的文件
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 写文件
            try {
                byte[] retBuf = pBuffer.getPointer().getByteArray(0, dwBufSize);
                InputStream in = new ByteArrayInputStream(retBuf);
                FileOutputStream fos = new FileOutputStream(file, true);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = in.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return false;

    }

    /**
     * @return boolean
     * @Author jijl
     * @Description: 删除文件
     **/
    public static boolean deleteFile(String sPath) {
        System.out.println("删除文件---" + sPath);
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
