package com.example.protectionapp.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.WalletHistoryAdapter;
import com.example.protectionapp.utils.Utils;

public class Wallet extends AppCompatActivity {
    private ImageView ivBack;
    private RecyclerView rvWalletHistory;
    private CardView cardMyWallet;
    private WalletHistoryAdapter walletHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initViews();
        initActions();
    }


    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        cardMyWallet = findViewById(R.id.cardMyWallet);
        rvWalletHistory = findViewById(R.id.rvWalletHistory);
        walletHistoryAdapter = new WalletHistoryAdapter();
        rvWalletHistory.setAdapter(walletHistoryAdapter);
        cardMyWallet.setBackground(Utils.getThemeGradient(50F));
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}