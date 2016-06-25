package com.simoncherry.averagefaceclient2.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simoncherry.averagefaceclient2.R;
import com.simoncherry.averagefaceclient2.application.MyApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class MergeFragment extends Fragment {

    private TextView tv_process_msg;
    private ImageView img_result;

    public MergeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_merge, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_process_msg = (TextView) getActivity().findViewById(R.id.tv_process_msg);
        img_result = (ImageView) getActivity().findViewById(R.id.img_result);

        registerMessageReceiver();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.simoncherry.averagefaceclient.Receiver.MyReceiver";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                String temp = intent.getStringExtra("message");
                if(temp.contains("output")){

                    OkHttpUtils.get().url(MyApplication.URL_DOWNLOAD + temp).build()
                            .execute(new BitmapCallback()
                            {
                                @Override
                                public void onError(Call call, Exception e) {
                                    tv_process_msg.setText("onError:" + e.getMessage());
                                }
                                @Override
                                public void onResponse(Bitmap bitmap)
                                {
                                    img_result.setImageBitmap(bitmap);
                                }
                            });
                }
                else{
                    tv_process_msg.setText(intent.getStringExtra("message"));
                }
            }
        }

    }
}
