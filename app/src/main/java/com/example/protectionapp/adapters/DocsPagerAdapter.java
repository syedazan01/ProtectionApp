package com.example.protectionapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.protectionapp.fragments.MediaFragment;
import com.example.protectionapp.fragments.PersonalRecordFragment;

public class DocsPagerAdapter extends FragmentStatePagerAdapter {
    public DocsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new PersonalRecordFragment();
            case 1:
                return new MediaFragment();
            default:
                return null;
        }

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
