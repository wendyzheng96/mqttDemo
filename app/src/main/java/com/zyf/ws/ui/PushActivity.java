package com.zyf.ws.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zyf.ws.R;
import com.zyf.ws.databinding.ActivityPushBinding;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PushActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PushActivity";

    private ActivityPushBinding binding;

    private MqttAndroidClient client;
    private MqttConnectOptions mqttConnectOptions;
    private boolean isReconnect = true;
    private String topic = "test";
    private String imei = "androidID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_push);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MQTT推送消息");
        }

        getIMEI();
        init("tcp://10.0.0.9:1883", imei, "admin123", "public");

        binding.tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        binding.btnSubscribe.setOnClickListener(this);
        binding.btnUnsubscribe.setOnClickListener(this);
        binding.btnPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_subscribe:
                if (client != null && client.isConnected()) {
                    subscribe(topic, 0);
                }
                break;
            case R.id.btn_unsubscribe:
                if (client != null && client.isConnected()) {
                    unSubscribe(topic);
                }
                break;
            case R.id.btn_publish:
                if (client != null && client.isConnected()) {
                    publish(topic,  convertTime(System.currentTimeMillis()));
                }
                break;
        }
    }

    /**
     * 更新log
     * @param msg log信息
     */
    private void refreshLogView(String msg) {
        binding.tvMessage.append(msg);
        int offset = binding.tvMessage.getLineCount() * binding.tvMessage.getLineHeight();
        if (offset > binding.tvMessage.getHeight()) {
            binding.tvMessage.scrollTo(0, offset - binding.tvMessage.getHeight());
        }
    }

    private void init(String serverURI, String androidId, String username, String password) {
        if (client == null || !client.isConnected()) {
            client = new MqttAndroidClient(this, serverURI, androidId);
        }
        //配置连接信息
        //连接选项
        mqttConnectOptions = new MqttConnectOptions();
        //是否清除缓存
        mqttConnectOptions.setCleanSession(true);
        //是否重连
        mqttConnectOptions.setAutomaticReconnect(true);
        //设置心跳
        mqttConnectOptions.setKeepAliveInterval(30);
        //设置登录用户名
        mqttConnectOptions.setUserName(username);
        //设置登录密码
        mqttConnectOptions.setPassword(password.toCharArray());
        //设置超时时间
        mqttConnectOptions.setConnectionTimeout(30);
        //监听服务器发来的信息
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //连接丢失异常
                Log.e(TAG, "connectionLost ");
                refreshLogView("\n连接断开!");
                //重连
                if (isReconnect) {
                    connect();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                //收到服务器推送的消息
                Log.e(TAG, "messageArrived: topic - " + topic + ", message - "
                        + new String(message.getPayload()));
                refreshLogView("\ntopic - " + topic + ", message - "
                        + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.e(TAG, "deliveryComplete");
                refreshLogView("\n消息分发完毕!");
            }
        });

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isReconnect = false;
        if (client != null) {
            disconnect();
            close();
            client = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (client != null) {
            unSubscribe(topic);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_other, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_other:
                Intent intent = new Intent(this, MoreActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 连接MQTT
     */
    private void connect() {
        try {
            if (client != null && !client.isConnected()) {
                client.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.e(TAG, "connect success ");
                        refreshLogView("\n连接成功!");
                        if (client != null && client.isConnected()) {
                            //订阅主题
                            //主题对应的推送策略 分别是0, 1, 2 建议服务端和客户端配置的主题一致
                            // 0 表示只会发送一次推送消息 收到不收到都不关心
                            // 1 保证能收到消息，但不一定只收到一条
                            // 2 保证收到切只能收到一条消息
                            int qos = 0;
                            subscribe(topic, qos);
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "connect failure: " + exception.getLocalizedMessage());
                        refreshLogView("\n连接失败!");
                        //重连
                        if (isReconnect) {
                            connect();
                        }
                    }
                });
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅主题
     *
     * @param topics 主题
     * @param qos 策略
     */
    private void subscribe(final String topics, int qos) {
        try {
            client.subscribe(topics, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "subscribe " + topics + " success");
                    refreshLogView("\n订阅主题" + topic + "成功！");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "subscribe " + topics + " fail, " + exception);
                    refreshLogView("\n订阅主题" + topic + "失败!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅主题
     *
     * @param topic 主题
     */
    private void unSubscribe(final String topic) {
        try {
            client.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "unSubscribe " + topic + " success");
                    refreshLogView("\n取消订阅主题" + topic + "成功!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "unSubscribe " + topic + " fail");
                    refreshLogView("\n取消订阅主题" + topic + "失败!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "unSubscribe " + topic + " MqttException: " + e);
        }
    }

    /**
     * 发布消息
     *
     * @param msg  消息
     * @param topic  主题
     */
    private void publish(String topic, String msg) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(msg.getBytes());
            client.publish(topic, mqttMessage, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    refreshLogView("\n发布消息成功!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    refreshLogView("\n发布消息失败!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            binding.tvMessage.setText(e.getLocalizedMessage());
        }
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        try {
            client.disconnect();
            Log.e(TAG, "connect disconnect");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭客户端
     */
    private void close() {
        try {
            client.unregisterResources();
            client.close();
            Log.e(TAG, "connect close");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "connect close fail " + e.getMessage());
        }
    }

    //获取设备IMEI号
    @SuppressLint("HardwareIds")
    private void getIMEI() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (manager != null) {
                imei = manager.getDeviceId();
                Log.e(TAG, "IMEI: " + imei);
            }
        }
    }

    private String convertTime(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault());
        return format.format(new Date(timeStamp));
    }
}
