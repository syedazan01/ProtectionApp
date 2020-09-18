package atoz.protection.RecordsActivites;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import atoz.protection.R;

public class UploadingDialog {

    private Activity activity;
   private AlertDialog alertDialog;

    public UploadingDialog(Activity myactivity) {
        activity = myactivity;
    }
    void  startloadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
    void  dismissdialog(){
        alertDialog.dismiss();
    }
}
