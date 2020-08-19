package com.example.protectionapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.DocumentList;
import com.example.protectionapp.adapters.PersonalRecordAdapter;
import com.example.protectionapp.model.PersonalRecord;
import com.example.protectionapp.utils.AppConstant;
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
                Intent intent = new Intent(getActivity(), DocumentList.class);
                if (position==0) {
//                    intent = new Intent(getActivity(), Adhaar.class);
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.ADHAAR);
                }
                else if(position==1)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.PAN);
                else if(position==2)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.DRIVING_LICENSE);
                else if(position==3)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.BANK);
                else if(position==4)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.ATM);
                else if (position == 5)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.VOTER_ID);
                else if (position == 6)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.STUDENT_ID);
                else if (position == 7)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.PASSPORT);

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
        recordList.add(new PersonalRecord("Student ID"));
        recordList.add(new PersonalRecord("Passport"));
    }
}