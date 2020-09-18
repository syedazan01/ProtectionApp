
package atoz.protection.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import atoz.protection.R;

public class CacheCleaner extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(PrefManager.getBoolean(ISBLUELIGHT))
            setTheme(R.style.AppTheme_Base_Night);
        else*/
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_cache_cleaner);
        addViews();
        initActions();
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addViews() {
        toolbar = findViewById(R.id.toolbar);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Cache Cleaner");
    }
}