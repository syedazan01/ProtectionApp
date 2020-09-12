package com.example.protectionapp.fragments;

import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.CallRecordingList;
import com.example.protectionapp.adapters.RecordingFileAdapter;
import com.example.protectionapp.interfacecallbacks.onRecordFileSave;
import com.example.protectionapp.model.RecordingFileData;
import com.example.protectionapp.room.AppDatabase;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static com.example.protectionapp.utils.AppConstant.ISBLUELIGHT;


public class PlayerFragment extends Fragment implements onRecordFileSave {

RecyclerView rvRecordFiles;
RelativeLayout rltCallRecording;
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
        rltCallRecording=view.findViewById(R.id.rltCallRecording);
        rvRecordFiles=view.findViewById(R.id.rvRecordFiles);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*if(PrefManager.getBoolean(ISBLUELIGHT))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else*/
            getActivity().setTheme(R.style.AppTheme_Base_Light);
        Recording_fragment.onRecordFileSave=this;
        rltCallRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CallRecordingList.class));
            }
        });
        recordingFileAdapter =new RecordingFileAdapter(recordingFileData,getActivity());
       /* rvRecordFiles.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvRecordFiles, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));*/
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