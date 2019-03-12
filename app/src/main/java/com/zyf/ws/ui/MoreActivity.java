package com.zyf.ws.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zyf.ws.R;
import com.zyf.ws.bean.GetClientsBean;
import com.zyf.ws.bean.HttpResult;
import com.zyf.ws.databinding.ActivityMoreBinding;
import com.zyf.ws.http.RetrofitHelper;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoreActivity extends AppCompatActivity {

    private ActivityMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("在线连接客户端数");
        }
        getData();
    }

    private void getData() {
        RetrofitHelper.newInstance().getService().getClient("emq@127.0.0.1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<GetClientsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<GetClientsBean> result) {
                        if (result.getResult().getClients() != null) {
                            for (GetClientsBean.ClientBean bean : result.getResult().getClients()) {
                                binding.tvClients.append(bean.toString() + "\n");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
