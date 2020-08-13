package com.example.protectionapp.RecordsActivites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterUsers;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SendDailog extends AppCompatDialogFragment {
    private EditText editTextmessage, editTextPassword;
    private SendDialogListener sendDialogListener;
    Activity umActivity;

    public SendDailog(Activity activity) {
    this.umActivity=activity;
    }

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
                        final ProgressDialog pd = Utils.getProgressDialog(umActivity);
                        pd.show();
                        final Dialog dialog = Utils.getRegisteredUserList(umActivity);
                        Button btnSend = dialog.findViewById(R.id.btnSend);
                        Utils.makeButton(btnSend, getResources().getColor(R.color.colorAccent), 40F);
                        final RecyclerView rvUser = dialog.findViewById(R.id.rvUser);
                        rvUser.setLayoutManager(new LinearLayoutManager(umActivity));
                        rvUser.addItemDecoration(new DividerItemDecoration(umActivity, RecyclerView.VERTICAL));
                        Utils.getUserReference(getContext()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                pd.dismiss();
                                List<UserBean> userBeans = new ArrayList<>();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    UserBean userBean = postSnapshot.getValue(UserBean.class);
                                    if (userBean.getMobile() != null) {
                                        if (!userBean.getMobile().equals(PrefManager.getString(AppConstant.USER_MOBILE))) {
                                            userBeans.add(userBean);
                                        }
                                    }
                                }

                                rvUser.setAdapter(new AdapterUsers(umActivity, userBeans, null));
                                dialog.show();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

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
