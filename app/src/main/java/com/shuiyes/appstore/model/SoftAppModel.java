package com.shuiyes.appstore.model;

import com.shuiyes.appstore.R;
import com.shuiyes.appstore.util.Util;

public class SoftAppModel implements BindingAdapterItem {

    public static final int STATE_DOWN_DEFAULT = 100;
    public static final int STATE_DOWN_STARTED = 101;
    public static final int STATE_DOWN_PROCESS = 102;
    public static final int STATE_DOWN_PAUSED = 103;
    public static final int STATE_DOWN_SUCCESS = 104;
    public static final int STATE_DOWN_FAILURE = 105;

    private int state;
    private String title, text, stateExt, downPercent;
    private long size, download;

    @Override
    public int getViewType() {
        return R.layout.view_item_soft;
    }

    public SoftAppModel(String title, String text, long size, long download) {
        this.title = title;
        this.text = text;
        this.size = size;
        this.download = download;
        this.state = STATE_DOWN_DEFAULT;
        this.stateExt = "下载";
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return Util.getFormatDownload(download) + " | " + Util.getFormatSize(size) + "\n" + text;
    }

    public String getState() {
        return this.stateExt;
    }

    public String getDownPercent() {
        return downPercent;
    }

    public void setState(int state, String downPercent) {
        this.state = state;
        this.downPercent = downPercent;
        switch (this.state) {
            case STATE_DOWN_DEFAULT:
                this.stateExt = "下载";
                break;
            case STATE_DOWN_STARTED:
                this.stateExt = "连接中";
                break;
            case STATE_DOWN_PROCESS:
                this.stateExt = downPercent + "%";
                break;
            case STATE_DOWN_PAUSED:
                this.stateExt = "继续";
                break;
            case STATE_DOWN_SUCCESS:
                this.stateExt = "安装";
                break;
            case STATE_DOWN_FAILURE:
                this.stateExt = "重试";
                break;
        }
    }

    @Override
    public String toString() {
        return "SoftAppModel{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", size=" + size +
                ", download=" + download +
                '}';
    }
}
