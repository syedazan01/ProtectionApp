package atoz.protection.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.RecordsActivites.DocumentList;
import atoz.protection.adapters.PersonalRecordAdapter;
import atoz.protection.model.PersonalRecord;
import atoz.protection.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;


public class PersonalRecordFragment extends Fragment {

    RecyclerView rvpersonalRecords;
    List<PersonalRecord> recordList;
    private PersonalRecordAdapter.RecyclerViewClickListener recyclerViewClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_record, container, false);
        rvpersonalRecords = view.findViewById(R.id.recycler_view_records);
        rvpersonalRecords.setHasFixedSize(true);
        rvpersonalRecords.setLayoutManager(new GridLayoutManager(getContext(), 2));
        initData();
        setOnClickListiner();
//          rvpersonalRecords.addItemDecoration(new DividerItemDecoration(getContext(),RecyclerView.VERTICAL));

        rvpersonalRecords.setAdapter(new PersonalRecordAdapter(recordList, recyclerViewClickListener));
//        LayoutAnimationController animation= AnimationUtils.loadLayoutAnimation(getContext(),getActivity().getResources().getIdentifier("layout_slide_down","anim",getActivity().getPackageName()));
//        rvpersonalRecords.setLayoutAnimation(animation);
//        rvpersonalRecords.getAdapter().notifyDataSetChanged();
//        rvpersonalRecords.scheduleLayoutAnimation();
        return view;


    }

    private void setOnClickListiner() {
        recyclerViewClickListener = new PersonalRecordAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), DocumentList.class);
                if (position == 0) {
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.MEDIA_DOC);
                } else if (position == 1) {
//                    intent = new Intent(getActivity(), Adhaar.class);
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.ADHAAR);
                } else if (position == 2)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.PAN);
                else if (position == 3)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.DRIVING_LICENSE);
                else if (position == 4)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.BANK);
                else if (position == 5)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.ATM);
                else if (position == 6)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.VOTER_ID);
                else if (position == 7)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.STUDENT_ID);
                else if (position == 8)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.BIRTH_CERTIFICATE);
                else if (position == 9)
                    intent.putExtra(AppConstant.PERSONAL_DOCUMENT, AppConstant.PASSPORT);


                startActivity(intent);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* if(PrefManager.getBoolean(ISBLUELIGHT))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else*/
//        getActivity().setTheme(R.style.AppTheme_Base_Light);
    }

    private void initData() {
        recordList = new ArrayList<>();
        recordList.add(new PersonalRecord("PDF AND \nCAMERA", R.drawable.ic_pdf));
        recordList.add(new PersonalRecord("ADHAAR \nCard", R.drawable.aadharlogo));
        recordList.add(new PersonalRecord("PAN \nCARD", R.drawable.panlogo));
        recordList.add(new PersonalRecord("DRIVING \nLICENSE", R.drawable.drivinglogo));
        recordList.add(new PersonalRecord("BANK \nACCOUNT", R.drawable.banknew));
        recordList.add(new PersonalRecord("DEBIT \nCARD", R.drawable.atmlogo));
        recordList.add(new PersonalRecord("VOTER \nID", R.drawable.voteridlogo));
        recordList.add(new PersonalRecord("STUDENT \nID", R.drawable.student_id));
        recordList.add(new PersonalRecord("BIRTH CERTIFICATE", R.drawable.birthlogo));
        recordList.add(new PersonalRecord("PASSPORT", R.drawable.passportlogo));
    }
}