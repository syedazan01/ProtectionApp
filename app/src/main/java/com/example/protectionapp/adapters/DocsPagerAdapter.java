package com.example.protectionapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.protectionapp.fragments.MediaFragment;
import com.example.protectionapp.fragments.PersonalRecordFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocsPagerAdapter extends FragmentStatePagerAdapter {
    public List<Fragment> fragmentList=new ArrayList<>(Arrays.asList(new PersonalRecordFragment(),new MediaFragment()));
    public DocsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Document";
            case 1:
                return "Media Document";
            default:
                return super.getPageTitle(position);
        }
    }
}
