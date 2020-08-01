package com.example.protectionapp.hoverwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.protectionapp.R;

import java.util.Collections;
import java.util.List;

import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.HoverMenu;
import me.samthompson.bubbleactions.BubbleActions;
import me.samthompson.bubbleactions.Callback;

public class MyHoverMenu extends HoverMenu {
    private Context mContext;
    private Section mSection;
    private ViewGroup viewGroup;
     public MyHoverMenu(@NonNull Context context) {
        mContext = context;
         viewGroup=new ViewGroup(mContext) {
             @Override
             protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

             }
         };
        mSection = new Section(
                new SectionId("1"),
                createBubbleView(),
                createScreen()
        );
    }
    private Content createScreen() {
        return new MyContent(mContext, "Screen 1");
    }
    private View createBubbleView() {
        ImageView imageView =new ImageView(mContext);
        imageView.setImageResource(R.drawable.login_logo);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ConstraintLayout constraintLayout=new ConstraintLayout(mContext);
        constraintLayout.addView(imageView);
//        viewGroup.addView(imageView);
        BubbleActions.on(imageView)
                .addAction("Star", R.drawable.aadharlogo, new Callback() {
                    @Override
                    public void doAction() {
                        Toast.makeText(mContext, "Star pressed!", Toast.LENGTH_SHORT).show();
                    }
                })
              .addAction("Star", R.drawable.aadharlogo, new Callback() {
                  @Override
                  public void doAction() {
                      Toast.makeText(mContext, "Star pressed!", Toast.LENGTH_SHORT).show();
                  }
              })
              .addAction("Star", R.drawable.aadharlogo, new Callback() {
                  @Override
                  public void doAction() {
                      Toast.makeText(mContext, "Star pressed!", Toast.LENGTH_SHORT).show();
                  }
              }).show();
        return imageView;
    }
    @Override
    public String getId() {
        return "singlesectionmenu";
    }

    @Override
    public int getSectionCount() {
        return 1;
    }

    @Nullable
    @Override
    public Section getSection(int index) {
        if (0 == index) {
            return mSection;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Section getSection(@NonNull SectionId sectionId) {
        if (sectionId.equals(mSection.getId())) {
            return mSection;
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public List<Section> getSections() {
        return Collections.singletonList(mSection);
    }
}
