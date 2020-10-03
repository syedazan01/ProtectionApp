package atoz.protection.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import atoz.protection.R;


public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*if(PrefManager.getBoolean(ISBLUELIGHT))
            getActivity().setTheme(R.style.AppTheme_Base_Night);
        else*/
//            getActivity().setTheme(R.style.AppTheme_Base_Light);
    }
}