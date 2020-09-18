package atoz.protection.billing;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class GooglePaySetup {
    public PaymentsClient paymentsClient;
    private Wallet.WalletOptions walletOptions;
    private Context context;

    public GooglePaySetup(Context context) {
        this.context = context;
        walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build();
        paymentsClient = Wallet.getPaymentsClient(context, walletOptions);
    }

    private JSONObject baseCardPaymentMethod() {
        try {
            return new JSONObject()
                    .put("type", "CARD")
                    .put("parameters", new JSONObject()
                            .put("allowedCardNetworks", new JSONArray(Arrays.asList("VISA", "MASTERCARD")))
                            .put("allowedAuthMethods", new JSONArray(Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS"))));
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    public JSONObject baseConfigurationJson() throws JSONException {
        return new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0)
                .put("allowedPaymentMethods", new JSONArray().put(baseCardPaymentMethod()));
    }

    public Task<Boolean> readyToPay() throws JSONException {
        IsReadyToPayRequest isReadyToPayRequest = IsReadyToPayRequest.fromJson(baseConfigurationJson().toString());
        return paymentsClient.isReadyToPay(isReadyToPayRequest);
//    task.addOnCompleteListener(new OnCompleteListener<Boolean>() {
//        @Override
//        public void onComplete(@NonNull Task<Boolean> task) {
//            if(task.isSuccessful())
//            {
//                return true;
//            }
//            else
//            {
//                return;
//            }
//        }
//    })
    }
}
