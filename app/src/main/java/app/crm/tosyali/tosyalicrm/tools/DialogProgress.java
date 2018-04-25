package app.crm.tosyali.tosyalicrm.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import app.crm.tosyali.tosyalicrm.R;

public class DialogProgress {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Context context;
    private View view;

    public DialogProgress(Context context){
        this.context = context;
    }

    public void initDialog(){
        this.dialogBuilder = new AlertDialog.Builder(context);
        this.view = LayoutInflater.from(context).inflate(R.layout.dialog_progress,null);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissDialog(){
        if (dialog != null)
            dialog.dismiss();
    }

}
