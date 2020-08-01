package com.example.protectionapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.ATM;
import com.example.protectionapp.RecordsActivites.Adhaar;
import com.example.protectionapp.RecordsActivites.Bank;
import com.example.protectionapp.RecordsActivites.DrivingLicence;
import com.example.protectionapp.RecordsActivites.PAN;
import com.example.protectionapp.RecordsActivites.VoterID;
import com.example.protectionapp.adapters.PersonalRecordAdapter;
import com.example.protectionapp.model.PersonalRecord;
import com.example.protectionapp.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;


public class PersonalRecordFragment extends Fragment {

    RecyclerView rvpersonalRecords;
    List<PersonalRecord> recordList;
    private PersonalRecordAdapter.RecyclerViewClickListener recyclerViewClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_personal_record, container, false);
        rvpersonalRecords=view.findViewById(R.id.recycler_view_records);
        rvpersonalRecords.setHasFixedSize(true);
        rvpersonalRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        initData();
        setOnClickListiner();
//          rvpersonalRecords.addItemDecoration(new DividerItemDecoration(getContext(),RecyclerView.VERTICAL));

        rvpersonalRecords.setAdapter(new PersonalRecordAdapter(recordList,recyclerViewClickListener));
        LayoutAnimationController animation= AnimationUtils.loadLayoutAnimation(getContext(),getActivity().getResources().getIdentifier("layout_slide_down","anim",getActivity().getPackageName()));
        rvpersonalRecords.setLayoutAnimation(animation);
        rvpersonalRecords.getAdapter().notifyDataSetChanged();
        rvpersonalRecords.scheduleLayoutAnimation();
        return view;


    }

    private void setOnClickListiner() {
        recyclerViewClickListener = new PersonalRecordAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = null;
                if (position==0) {
                    intent = new Intent(getActivity(), Adhaar.class);
                }
                else if(position==1)
                    intent = new Intent(getActivity(), PAN.class);
                else if(position==2)
                    intent = new Intent(getActivity(), DrivingLicence.class);
                else if(position==3)
                    intent = new Intent(getActivity(), Bank.class);
                else if(position==4)
                    intent = new Intent(getActivity(), ATM.class);
                else if(position==5)
                    intent = new Intent(getActivity(), VoterID.class);

                startActivity(intent);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else
            getActivity().setTheme(R.style.AppTheme_Base_Light);
    }

    private void initData() {
        recordList = new ArrayList<>();
        recordList.add(new PersonalRecord("AADHAR"));
        recordList.add(new PersonalRecord("PAN"));
        recordList.add(new PersonalRecord("Driving License"));
        recordList.add(new PersonalRecord("Bank"));
        recordList.add(new PersonalRecord("ATM"));
        recordList.add(new PersonalRecord("Voter ID"));
    }
}