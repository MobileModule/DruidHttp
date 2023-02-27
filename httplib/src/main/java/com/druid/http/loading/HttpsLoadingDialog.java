package com.druid.http.loading;

import android.content.DialogInterface;

public interface HttpsLoadingDialog {
    void setDialogCancelable(boolean canCancel);

    void setDialogOnCancelListener(DialogInterface.OnCancelListener listener);

    boolean isDialogShowing();

    void setLoadingTips(String message);

    void dialogShow();

    void dialogStop();
}
