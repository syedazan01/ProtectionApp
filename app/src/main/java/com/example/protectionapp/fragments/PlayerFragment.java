package com.example.protectionapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.RecordingFileAdapter;
import com.example.protectionapp.interfacecallbacks.onRecordFileSave;
import com.example.protectionapp.model.RecordingFileData;
import com.example.protectionapp.room.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class PlayerFragment extends Fragment implements onRecordFileSave {

RecyclerView rvRecordFiles;
    List<RecordingFileData> recordingFileData=new ArrayList<>();
RecordingFileAdapter recordingFileAdapter;
    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_player, container, false);
        rvRecordFiles=view.findViewById(R.id.rvRecordFiles);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Recording_fragment.onRecordFileSave=this;
        recordingFileAdapter =new RecordingFileAdapter(recordingFileData);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
              recordingFileData= AppDatabase.getAppDataBase(getContext()).getRecordFileDao().getRecordingFiles();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recordingFileData!=null) {
                            rvRecordFiles.setLayoutManager(new LinearLayoutManager(getContext()));
                            rvRecordFiles.setAdapter(recordingFileAdapter);
                            recordingFileAdapter.updateList(recordingFileData);
                            recordingFileAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onSave() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                recordingFileData= AppDatabase.getAppDataBase(getContext()).getRecordFileDao().getRecordingFiles();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recordingFileData!=null) {
                            Log.e("fbfdbgfweg",recordingFileData.size()+"");
                            recordingFileAdapter.updateList(recordingFileData);
                            recordingFileAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });

    }
}