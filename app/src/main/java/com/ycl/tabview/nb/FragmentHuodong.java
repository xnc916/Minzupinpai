package com.ycl.tabview.nb;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ycl.tabview.R;
import com.ycl.tabview.RegisterDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentHuodong extends Fragment {

    View mainView = null;
    @Bind(R.id.rl)
    RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.fragment_huodong, container, false);
        }
        ButterKnife.bind(this, mainView);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterDialog.Builder builder = new RegisterDialog.Builder(getActivity());
                builder.setTitle("客服电话");
                builder.setMessage("0352-2060560");
                builder.setcancelText("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setshareText("拨号", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + "0352-2060560");
                        intent.setData(data);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        return mainView;
    }




}
