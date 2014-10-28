package snowmada.main.view;

import java.math.BigDecimal;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.Util.ImageLoader;
import snowmada.main.network.HttpClient;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class DealsView extends Activity {
    private TextView lbl_merchant,lbl_deals, lbl_address,lbl_description;
    private ImageView iv_dealsImage;
    private String advt_id;
    private RelativeLayout ll_main_layout;
    public ImageLoader    imageLoader;
    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "Ac9lfRBkewp2PdqxKyN5GYdOCOXuKfka6tiOkH01LJfCxxJtp_0eRexqkUnq";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private LinearLayout btn_paypal;
    
    private static PayPalConfiguration config = new PayPalConfiguration()
    	.environment(CONFIG_ENVIRONMENT)
    	.clientId(CONFIG_CLIENT_ID)
    	.defaultUserEmail("");
    
    String merchant;
	String deals;
	String address;
	String descriptin;
	String image;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(DealsView.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        setContentView(R.layout.deals_details);
        
        imageLoader = new ImageLoader(this);
        
        Bundle bundle = getIntent().getExtras();
        advt_id = bundle.getString("advt_id");
        merchant = bundle.getString("name");
        deals = bundle.getString("AdvtName");
        address = bundle.getString("address");
        descriptin = bundle.getString("descriptin");
        image = bundle.getString("advt_image");
        
        lbl_merchant = (TextView)findViewById(R.id.lbl_marchant);
        lbl_merchant.setText("Marchant - "+merchant);
        
        lbl_deals = (TextView)findViewById(R.id.lbl_deals);
        lbl_deals.setText("Deals- "+deals);
        
        lbl_address = (TextView)findViewById(R.id.lbl_address);
        lbl_address.setText("Address- "+address);
        
        lbl_description = (TextView)findViewById(R.id.lbl_description);
        lbl_description.setText(descriptin);
        
        iv_dealsImage = (ImageView)findViewById(R.id.iv_deals);
        imageLoader.DisplayImage(image, iv_dealsImage);
        
        btn_paypal = (LinearLayout)findViewById(R.id.btn_paypal);
       
        ll_main_layout = (RelativeLayout)findViewById(R.id.ll_main_layout);
        
        //new DealsWeb().execute();
    }
    
    class DealsWeb extends AsyncTask<String, Void, Void> {
	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    	}

    	@Override
    	protected Void doInBackground(String... param) {
    		try {
    			JSONObject request = new JSONObject();
    			request.put("advt_id", advt_id);
    			System.out.println("!!-- images "+advt_id);
    			Log.e("req", request.toString());
    			JSONObject response = HttpClient.SendHttpPost(UrlCons.DEALS_DETAILS.getUrl(), request);
    			
    			if(response!=null){
    				merchant = response.getString("name"); 
    				deals = response.getString("advt_name"); 
    				address = response.getString("address"); 
    				descriptin = response.getString("description"); 
    				image = UrlCons.BANNER_ADD.getUrl()+response.getString("advt_image"); 
    				Log.e("Deals Image", image);
		   
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			return null;
    		}
	    return null;
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {

	}

	@Override
	protected void onPostExecute(Void sResponse) {
		
		    
		
	    imageLoader.DisplayImage(image, iv_dealsImage);
	    
	    lbl_deals.setText("Deals- "+deals);
	    lbl_address.setText("Address- "+address);
	    lbl_description.setText(descriptin);
	    ll_main_layout.setVisibility(View.VISIBLE);
	}
    }
    
    public void onBuyPressed(View pressed) {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to PAYMENT_INTENT_AUTHORIZE to only authorize payment and 
        // capture funds later.
    	
    	Log.e("reach here here", "reach here here");
    	
        PayPalPayment thingToBuy =
                new PayPalPayment(new BigDecimal("1.75"), "USD", deals,
                        PayPalPayment.PAYMENT_INTENT_SALE);
        

        Intent intent = new Intent(DealsView.this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                getApplicationContext(),
                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         * 
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         * 
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration.getApplicationCorrelationId(this);

        Log.i("FuturePaymentExample", "Application Correlation ID: " + correlationId);

        // TODO: Send correlationId and transaction details to your server for processing with
        // PayPal...
        Toast.makeText(
                getApplicationContext(), "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    

}
