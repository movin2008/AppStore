package com.shuiyes.appstore.ui.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.shuiyes.appstore.log.SLog;
import com.shuiyes.appstore.model.BindingAdapterItem;
import com.shuiyes.appstore.model.SoftAppModel;
import com.shuiyes.appstore.util.OkHttpManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SoftViewModel extends BaseViewModel implements Callback {

    public static final int K1 = 1024;

    private SparseArray<Call> mRequestCalls = new SparseArray<>();
    private Call mInitCall = null;

    public ObservableField<Integer> loadingVisible = new ObservableField<>(View.VISIBLE);
    public ObservableList<BindingAdapterItem> listItems = new ObservableArrayList<>();

    @Override
    public void start() {
        Request.Builder builder = new Request.Builder();
        builder.url("http://shuiyes.com/test/header.php");
        builder.header("uuid", Build.SERIAL);
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "debug=1&rows=5000&startRow=0"));
        mInitCall = OkHttpManager.get().newCall(builder.build());
        mInitCall.enqueue(this);
        Log.e(TAG, "start");
    }

    @Override
    public void destroy() {
        mInitCall.cancel();
        for (int i = 0; i < mRequestCalls.size(); i++) {
            Call call = mRequestCalls.valueAt(i);
            call.cancel();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "onFailure: " + call.request().url().url());
        Log.e(TAG, "onFailure: " + e);
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {

            // mock loading
            SystemClock.sleep(1234);

            String action = call.request().url().url().getPath();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                Log.e(TAG, "onResponse(null): " + action);
                return;
            } else {
//                Log.e(TAG, "onResponse: " + action);
            }

            String result = responseBody.string();
            Log.e(TAG, result.replace("<br>", "\n"));

            List<BindingAdapterItem> itemList = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 100; i++) {

                SoftAppModel app = new SoftAppModel("应用 " + i, "简介： 这里是应用的简介 " + i, 1024 * 1024 + random.nextInt(Integer.MAX_VALUE), random.nextInt(2 * 10000 * 10000));
//                if (android.system.Os.access("/sdcard/test" + i + ".apk", android.system.OsConstants.F_OK)) {


                File file = new File("/sdcard/test" + i + ".apk");
                if (file.exists()) {

                    // 测试下载 100M 文件
                    if (file.length() == 100 * 1024 * 1024) {
                        app.setState(SoftAppModel.STATE_DOWN_SUCCESS, null);
                    } else {
                        app.setState(SoftAppModel.STATE_DOWN_PAUSED, String.format("%.1f", file.length() / 1024 / 1024f));
                    }
                }
                itemList.add(app);
            }

            listItems.clear();
            if (!itemList.isEmpty()) {
                listItems.addAll(itemList);
            }
            loadingVisible.set(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadAppIfNeed(Handler handler, int position){
        Call call = mRequestCalls.get(position);
        if (call != null) {
            mRequestCalls.remove(position);
            call.cancel();

            sendDownStateMsg(handler, SoftAppModel.STATE_DOWN_PAUSED, position, position);
        } else {
            downloadApp(handler, position);
        }
    }

    private Call downloadApp(final Handler handler, final int position) {

        final File file = new File("/sdcard/test" + position + ".apk");
        final boolean renewal = file.exists() && file.length() > K1;

        String url = "http://cachefly.cachefly.net/100mb.test";

        Request.Builder request = new Request.Builder().url(url).get();
        if (renewal) {
            request.header("RANGE", "bytes=" + (file.length() - K1) + "-");
        }
        Call call = OkHttpManager.get().newCall(request.build());
        mRequestCalls.put(position, call);
        handler.sendMessage(handler.obtainMessage(SoftAppModel.STATE_DOWN_STARTED, position, position));

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure " + call.request().url().url().getPath());
                Log.e(TAG, "onFailure " + e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse " + call.request().url().url());

                String notice = null;
                long totalLength = 0;
                double downloadLen = 0;
                InputStream in = null;
                FileOutputStream out = null;
                RandomAccessFile out2 = null;
                try {
                    ResponseBody responseBody = response.body();
                    totalLength = responseBody.contentLength();
                    in = responseBody.byteStream();
                    int rang = (int) (totalLength / 100);
                    Log.e(TAG, "onResponse rang " + rang);

                    if (renewal) {
                        out2 = new RandomAccessFile(file, "rw");
                        out2.seek(file.length() - K1);
                        downloadLen = file.length() - K1;
                        totalLength += downloadLen;
                    } else {
                        out = new FileOutputStream(file);
                    }

                    Log.e(TAG, "onResponse start " + downloadLen + "/" + totalLength);

                    int len;
                    String prevNotice = null;
                    byte[] bytes = new byte[rang];
                    while ((len = in.read(bytes)) != -1) {
                        if (renewal) {
                            out2.write(bytes, 0, len);
                        } else {
                            out.write(bytes, 0, len);
                        }
                        downloadLen += len;

                        notice = String.format("%.1f", downloadLen * 100 / totalLength);
                        if (!notice.equals(prevNotice)) {
                            prevNotice = notice;
                            SLog.e(TAG, position + ", " + notice + "/" + downloadLen);
                            sendDownStateMsg(handler, SoftAppModel.STATE_DOWN_PROCESS, position, position, notice);
                        }

                        if (call.isCanceled()) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    if (mRequestCalls.get(position) != null) {
                        sendDownStateMsg(handler, SoftAppModel.STATE_DOWN_FAILURE, position, position);
                    } else {
                        sendDownStateMsg(handler, SoftAppModel.STATE_DOWN_PAUSED, position, position, notice);
                    }
                } finally {
                    try {
                        if (out != null) out.close();
                        if (out2 != null) out2.close();
                        if (in != null) in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.e(TAG, "onResponse end " + downloadLen + "/" + totalLength);
                    if (mRequestCalls.get(position) != null) {
                        mRequestCalls.remove(position);
                        sendDownStateMsg(handler, SoftAppModel.STATE_DOWN_SUCCESS, position, position);
                    }
                }
            }
        });
        return call;
    }

    private void sendDownStateMsg(Handler handler, int what, int arg1, int arg2) {
        this.sendDownStateMsg(handler, what, arg1, arg2, null);
    }

    private void sendDownStateMsg(Handler handler, int what, int arg1, int arg2, Object obj) {
        handler.removeMessages(SoftAppModel.STATE_DOWN_PROCESS);
        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
    }
}
