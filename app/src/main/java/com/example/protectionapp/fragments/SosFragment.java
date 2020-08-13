package com.example.protectionapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.AdapterSos;
import com.example.protectionapp.model.FetchNotification;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rvSos;
    AdapterSos adapterSos;
    List<FetchNotification> fetchNotifications = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SosFragment newInstance(String param1, String param2) {
        SosFragment fragment = new SosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rvSos = view.findViewById(R.id.rvSos);
        rvSos.setLayoutManager(new LinearLayoutManager(getContext()));
        final ProgressDialog pd = Utils.getProgressDialog(getActivity());
        pd.show();
        Utils.getNotificationReference(getContext()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();
                fetchNotifications.clear();
                for (DataSnapshot subDataSnap : dataSnapshot.getChildren()) {
                    FetchNotification fetchNotification = subDataSnap.getValue(FetchNotification.class);

                    if (fetchNotification.getTo_mobile().equals(PrefManager.getString(AppConstant.USER_MOBILE)) || fetchNotification.getFrom_mobile().equals(PrefManager.getString(AppConstant.USER_MOBILE)))
                        fetchNotifications.add(fetchNotification);
                }
                adapterSos = new AdapterSos(getActivity(), fetchNotifications);
                rvSos.setAdapter(adapterSos);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("ffebebdbdfvb", firebaseError.getMessage());
                pd.dismiss();
            }
        });
    }
}