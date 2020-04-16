package com.bdstar.multiscreen.navi;

import android.content.Context;
import android.text.TextUtils;

import com.bdstar.mcu.util.McuUtils;
import com.bdstar.multiscreen.manager.CarManager;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NaviSender {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    public static final byte FF = (byte) 0xff;

    private CarManager carManager;

    public NaviSender(Context context) {
        mContext = context;

        carManager = CarManager.getInstance(mContext);
        carManager.bindService();
    }

    private byte mTurnType, mGuideType = -1;
    private int mRemainTime = -1;

    /**
     * 导航信息 时间信息（CORE主机） 0x0001:
     * 主机发送不带参数为请求数据
     * <p>
     * 3	0x14	导航信息命令
     * 4	0x00	命令高字节
     * 5	0x01	命令低字节
     * <p>
     * Byte12 Turn type 转向路口类型（众泰使用）
     * Byte15 Guide type 引导点类型（众泰使用）
     */
    public void sendTurnInfo(byte turnType, byte guideType) {
        sendTurnInfo(mRemainTime, turnType, guideType);
    }

    public void sendTurnInfo(int remainTime, byte turnType, byte guideType) {
        if (mTurnType == turnType && guideType == mGuideType) {
            return;
        }
        mTurnType = turnType;
        mGuideType = guideType;

        if (turnType != FF || guideType != FF) {
            clearRoundaboutInfo();
        }

        byte[] data = new byte[12];
        Arrays.fill(data, (byte) FF);

        if(remainTime != -1){
            byte[] timeBytes = McuUtils.intToByteArray(remainTime);
            data[3] = timeBytes[3];
            data[4] = timeBytes[2];
            data[5] = timeBytes[1];
        }

        data[6] = turnType;
        data[9] = guideType;
        carManager.sendDataToMcu((byte) 0x14, 0x01, data);
    }

    private int mDisstance = -1;

    /**
     * 导航信息 距离信息（CORE主机） 0x0002:
     * 主机发送不带参数为请求数据
     * <p>
     * 3	0x14	导航信息命令
     * 4	0x00	命令高字节
     * 5	0x02	命令�