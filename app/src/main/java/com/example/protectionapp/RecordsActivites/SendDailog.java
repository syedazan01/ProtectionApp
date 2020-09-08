package com.example.protectionapp.RecordsActivites;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SendDailog extends BottomSheetDialog {
    private EditText editTextmessage, editTextPassword;
    private SendDialogListener sendDialogListener;
    Activity umActivity;
    boolean isMsgVisible;
    View bootomSheetView;
    Button btnCancel, btnProceed;

    public SendDailog(@NonNull Context context, boolean isMsgVisible, int style) {
        super(context, style);
        this.umActivity = (Activity) context;
        this.isMsgVisible = isMsgVisible;
    }

    @Override
    public void setContentView(View view) {
        bootomSheetView = view;
        super.setContentView(view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            sendDialogListener = (SendDialogListener) umActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(umActivity.toString() + "must implement SendDialogListener");
        }
        editTextmessage = bootomSheetView.findViewById(R.id.editTextmessage);
        if (isMsgVisible)
            editTextmessage.setVisibility(View.VISIBLE);
        else
            editTextmessage.setVisibility(View.GONE);
        editTextPassword = bootomSheetView.findViewById(R.id.editTextPassword);
        btnCancel = bootomSheetView.findViewById(R.id.btnCancel);
        btnProceed = bootomSheetView.findViewById(R.id.btnProceed);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextmessage.getText().toString();
                String password = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Utils.showToast(umActivity, "Password is required", AppConstant.errorColor);
                    return;
                }
                Utils.hideKeyboardFrom(umActivity,view);
                sendDialogListener.applyTexts(message, password);
                dismiss();
            }
        });
    }


   /* public SendDailog(Activity umActivity, boolean isMsgVisible) {
        this.umActivity = umActivity;
        this.isMsgVisible = isMsgVisible;
    }*/

   /* @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(umActivity);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_send_dailog, null);

        builder.setView(view)
                .setTitle("Assign a Password and Text message to secure file")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String message = editTextmessage.getText().toString();
                        String password = editTextPassword.getText().toString();
                        if (TextUtils.isEmpty(password)) {
                            Utils.showToast(umActivity, "Password is required", AppConstant.errorColor);
                            return;
                        }
                        Utils.hideKeyboardFrom(umActivity,view);
                        sendDialogListener.applyTexts(message, password);
                    }
                });
        editTextmessage = view.findViewById(R.id.dialog_messageET);
        editTextPassword = view.findViewById(R.id.dialog_PasswordET);
        if (isMsgVisible)
            editTextmessage.setVisibility(View.VISIBLE);
        else
            editTextmessage.setVisibility(View.GONE);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendDialogListener = (SendDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement SendDialogListener");
        }
    }*/

    public interface SendDialogListener {
        void applyTexts(String message,String password);
    }
}
