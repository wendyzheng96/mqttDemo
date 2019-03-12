package com.zyf.ws.ui;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zyf.ws.R;
import com.zyf.ws.databinding.ActivityWebServiceBinding;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class WebServiceActivity extends AppCompatActivity {

    private ActivityWebServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_service);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("WebService");
        }

        binding.btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = binding.etInput.getText().toString();
                if (input.length() == 0) {
                    showToast("请输入要查询的城市");
                    return;
                }
                QueryAddressTask task = new QueryAddressTask(WebServiceActivity.this);
                task.execute(input);
            }
        });
    }


    static class QueryAddressTask extends AsyncTask<String, Integer, String> {

        private WeakReference<WebServiceActivity> mActivity;

        QueryAddressTask(WebServiceActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            return getRemoteInfo(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            WebServiceActivity activity = mActivity.get();
            activity.binding.tvContent.setText(s);
        }
    }

    //调用webService接口
    private static String getRemoteInfo(String input) {

        String WSDL_URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
        String nameSpace = "http://WebXml.com.cn/";
        String methodName = "getSupportCity";

        // 调用的方法名称
        String action = nameSpace + methodName;

        SoapObject request = new SoapObject(nameSpace, methodName);
        request.addProperty("byProvinceName", input);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request; // 由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true; // 由于是.net开发的webservice，所以这里要设置为true

        try {
            HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URL);
            httpTransportSE.call(action, envelope);

            if (envelope.getResponse() != null) {
                return envelope.getResponse().toString().trim();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void showToast(String msg) {
        Toast.makeText(WebServiceActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
