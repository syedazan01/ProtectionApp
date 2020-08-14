package com.example.protectionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> FragmentsList = new ArrayList<>();
    private final ArrayList<String> FragmentsTitle = new ArrayList<>();

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentsList.size();
    }

    public void addFragment(Fragment fragment,String title){
        FragmentsList.add(fragment);
        FragmentsTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentsTitle.get(position);
    }
}
