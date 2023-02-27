package com.druid.http;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.druid.http.loading.HttpsLoadingDialog;

import java.lang.ref.WeakReference;

public class HttpReqDialog extends Dialog implements HttpsLoadingDialog {
    private WeakReference<Context> mContext;

    public HttpReqDialog(Context context) {
        super(context);
        mContext = new WeakReference<>(context);
    }

    public HttpReqDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = new WeakReference<>(context);
    }

    public HttpReqDialog(Context context, String message) {
        super(context, R.style.HttpProgressDialog);
        mContext = new WeakReference<>(context);
        setView(message);
    }

    private ProgressBar img_progress = null;
    private TextView tv_http_req_status;

    private void setView(String content) {
        setContentView(R.layout.dialog_http_load_blank);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        tv_http_req_status = (TextView) this.findViewById(R.id.tv_http_req_status);
        if (TextUtils.isEmpty(content)) {
            tv_http_req_status.setVisibility(View.GONE);
        } else {
            tv_http_req_status.setVisibility(View.GONE);
        }
        tv_http_req_status.setText(content);
    }

    @Override
    public void setDialogCancelable(boolean canCancel) {
        super.setCancelable(canCancel);
    }

    @Override
    public void setDialogOnCancelListener(DialogInterface.OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    @Override
    public boolean isDialogShowing() {
        return super.isShowing();
    }

    @Override
    public void setLoadingTips(String message) {
        tv_http_req_status.setText(message);
    }

    @Override
    public void dialogShow() {
        if(mContext!=null) {
            if (mContext.get() != null) {
                super.show();
            }
        }
    }

    @Override
    public void dialogStop() {
        if(mContext!=null) {
            if (mContext.get() != null) {
                super.dismiss();
            }
        }
    }
}
