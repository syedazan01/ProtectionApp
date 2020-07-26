package com.example.protectionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.PersonalRecordAdapter;
import com.example.protectionapp.model.PersonalRecord;

import java.util.ArrayList;
import java.util.List;


public class PersonalRecordFragment extends Fragment {

    public PersonalRecordFragment() {
        // Required empty public constructor
    }

    RecyclerView rvpersonalRecords;
    List<PersonalRecord> recordList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_personal_record, container, false);
        rvpersonalRecords=view.findViewById(R.id.recycler_view_records);
        rvpersonalRecords.setHasFixedSize(true);
        rvpersonalRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        initData();
        rvpersonalRecords.setAdapter(new PersonalRecordAdapter(recordList));
        return view;


    }

    private void initData() {
        recordList = new ArrayList<>();
        recordList.add(new PersonalRecord("Aadhar"));
        recordList.add(new PersonalRecord("PAN"));
        recordList.add(new PersonalRecord("Driving License"));
        recordList.add(new PersonalRecord("Bank"));
        recordList.add(new PersonalRecord("ATM"));
        recordList.add(new PersonalRecord("Voter ID"));
    }
}