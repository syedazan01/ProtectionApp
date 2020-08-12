package com.example.protectionapp.RecordsActivites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.protectionapp.R;

public class SendDailog extends AppCompatDialogFragment {
    private EditText editTextmessage, editTextPassword;
    private SendDialogListener sendDialogListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_send_dailog,null);

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
                        sendDialogListener.applyTexts(message,password);

                    }
                });
        editTextmessage = view.findViewById(R.id.dialog_messageET);
        editTextPassword = view.findViewById(R.id.dialog_PasswordET);
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
    }

    public interface SendDialogListener {
        void applyTexts(String message,String password);
    }
}
