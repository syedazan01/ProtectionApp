package atoz.protection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import atoz.protection.R;
import atoz.protection.model.Intro;

public class IntroPagerAdapter extends PagerAdapter {
    private Context mContext;

    public IntroPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Intro intro = Intro.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.intro_slide, collection, false);

        ImageView ivImage = layout.findViewById(R.id.ivIntroImage);

        Glide.with(mContext).load(intro.getLayoutResId()).into(ivImage);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return Intro.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
