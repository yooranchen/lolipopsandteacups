package snowmada.main.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.adapter.DealsAdapter;
import snowmada.main.bean.GoodDeals;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.R;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.Marker;

@SuppressLint("ValidFragment")
public class DealFragment extends Fragment{
	
	public interface setDealsOnMapListener{
		public void loadDealsonMap(ArrayList<GoodDeals> mDealsArr);
	}
	
	public BaseActivity base;
	public ListView lv_deal_list;
	
	private Marker marker;
	public ArrayList<GoodDeals> mDealsArr = new ArrayList<GoodDeals>();
	private DealsAdapter mAdapter;
	
	public setDealsOnMapListener listener;
	
	public DealFragment(BaseActivity b,ArrayList<GoodDeals> mDealsArr){
		base = b;
		listener = (setDealsOnMapListener) base;
		this.mDealsArr = mDealsArr;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_deals, null, false);
		lv_deal_list = (ListView)mView.findViewById(R.id.lv_deal);
		mAdapter = new DealsAdapter(base, R.layout.deals_row, mDealsArr);
		lv_deal_list.setAdapter(mAdapter);
		listener.loadDealsonMap(mDealsArr);
		//new GoodDealsWeb().execute();
		return mView;
	}
	
	/*public class GoodDealsWeb extends AsyncTask<Boolean, Void, Boolean> {
		
		@Override
		public void onPreExecute(){
			base.showProgressBar();
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
		    boolean flag = false;
			try {		
			
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("user_id", base.application.getUserinfo().userId);
				JSONObject json = HttpClient.SendHttpPost(UrlCons.GOOD_DEALS.getUrl(), jsonObject);
				if(json!=null){
				    flag = json.getBoolean("status");
				    
				    if(flag){
					JSONArray arr = json.getJSONArray("data");
					for(int i=0; i<arr.length(); i++){
					    JSONObject c = arr.getJSONObject(i);
					    long markerid = System.currentTimeMillis()+ new Random().nextInt(1000);
					    	String id = c.getString("maid");
					    String id = "11";
						String name = c.getString("name");
						String advt_name = c.getString("advt_name");
						String address = c.getString("address");
						Double lat = Double.valueOf(c.getString("lat"));					
						Double lng = Double.valueOf(c.getString("lng"));
						
						String url = UrlCons.BANNER_ADD.getUrl()+c.getString("advt_image");
						Bitmap bitmap; 
						    bitmap = getBitmapFromURL(url);
						
						String desc = c.getString("description");
						mDealsArr.add(new GoodDeals(""+markerid,id, name, advt_name, address, lat, lng, bitmap,url,desc)); 
					}
					
				    }
				}			
			
			} catch (JSONException e) {
				e.printStackTrace();
				return flag;
			}catch(NumberFormatException e){
				e.printStackTrace();
				return flag;
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean status) {
			base.dismissProgressBar();
		    if(status){		
		    	System.out.println("!!--reach here"+mDealsArr.size());
		    	
		    	mAdapter = new DealsAdapter(base, R.layout.deals_row, mDealsArr);
				lv_deal_list.setAdapter(mAdapter);
				listener.loadDealsonMap(mDealsArr);
		    }
		}
	}*/
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	     InputStream in = new java.net.URL(src).openStream();
	    return BitmapFactory.decodeStream(in);
	    } catch (Exception e) {
	      e.printStackTrace();
	     return null;
	    }
	}
}
