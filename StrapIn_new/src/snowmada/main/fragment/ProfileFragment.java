package snowmada.main.fragment;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.Util.ImageLoader;
import snowmada.main.adapter.GalleryAdapter;
import snowmada.main.adapter.ProfileDealsAdapter;
import snowmada.main.bean.CommentBean;
import snowmada.main.bean.GoodDeals;
import snowmada.main.bean.ImageBean;
import snowmada.main.bean.ProfileDealsBean;
import snowmada.main.network.HttpClient;
import snowmada.main.view.BaseActivity;
import snowmada.main.view.ProfileEdit;
import snowmada.main.view.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements OnClickListener{
	public BaseActivity base;
	private TextView txt_prof_about_me,txt_profile_location,tv_prof_age,tv_prof_name;
	private TextView txt_profile_name,tv_prof_fev_mountain,txt_profile_shred_style;
	private ImageView iv_profile_image;
	private Button btnGalleryImageUpload ;
	public Gallery fgv_prof_deals_gallery;
	
	public static final int REQUEST_CODE_IMAGE_GALLERY = 0x4;
	protected UiLifecycleHelper uiHelper;
	public HttpEntity resEntity;
	
	public String profile_id = null;
	public String name;
	public String age;
	public String loc;
	public String image;
	public String fav_mountain;
	public String shred_style;
	public String about_me;
	private ImageLoader imageloader;
	private LinearLayout ll_edit_button_layout,ll_icon_prof_loc_edit_save,ll_icon_prof_fav_mountain_edit_save,ll_icon_prof_about_me_edit_save;
	
	
	public boolean isEditButtonEnable = false;
	public GridView gv_image_gallery;
	
	private ArrayList<ProfileDealsBean> profileDealsArr = new ArrayList<ProfileDealsBean>();
	private ArrayList<ImageBean> imageArr = new ArrayList<ImageBean>();
	public ArrayList<GoodDeals> mDealsArr = new ArrayList<GoodDeals>();
	
	private ProfileDealsAdapter mProfileDealsAdapter;
	private GalleryAdapter mGalleryAdapter;
	
	private Button btn_profile_edit;
	
	public ProfileFragment(BaseActivity b,boolean flag, String profile_id){
		base = b;
		isEditButtonEnable = flag;
		this.profile_id = profile_id;
		imageloader = new ImageLoader(base);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_user_profile, null, false);
		
		txt_profile_name = (TextView)mView.findViewById(R.id.txt_profile_name);
		tv_prof_fev_mountain = (TextView)mView.findViewById(R.id.txt_profile_fev_mountain);
		txt_profile_shred_style = (TextView)mView.findViewById(R.id.txt_profile_shred_style);
		txt_profile_location = (TextView)mView.findViewById(R.id.txt_profile_location);
		tv_prof_age = (TextView)mView.findViewById(R.id.txt_profile_age);
		iv_profile_image = (ImageView)mView.findViewById(R.id.profile_page_image);
		txt_prof_about_me = (TextView)mView.findViewById(R.id.txt_prof_about_me);
		
		ll_edit_button_layout = (LinearLayout)mView.findViewById(R.id.ll_edit_button_layout);
		fgv_prof_deals_gallery = (Gallery)mView.findViewById(R.id.gv_deals_images);
		
		ll_edit_button_layout = (LinearLayout)mView.findViewById(R.id.ll_edit_button_layout);
		ll_icon_prof_loc_edit_save = (LinearLayout)mView.findViewById(R.id.ll_prof_location_edit_save);
		ll_icon_prof_fav_mountain_edit_save = (LinearLayout)mView.findViewById(R.id.ll_prof_fav_mountain_edit_save);
		ll_icon_prof_about_me_edit_save = (LinearLayout)mView.findViewById(R.id.ll_prof_about_me_edit_save);
		
		gv_image_gallery = (GridView)mView.findViewById(R.id.gv_gallery_images);
		
		btn_profile_edit = (Button)mView.findViewById(R.id.btn_profile_edit);
		btn_profile_edit.setOnClickListener(this);
		
		btnGalleryImageUpload = (Button)mView.findViewById(R.id.btn_upload_img);
		btnGalleryImageUpload.setOnClickListener(this);
		
		new ProfileWeb().execute(profile_id);
		
		return mView;
	}

	public class ProfileWeb extends AsyncTask<String, Void, Boolean> {
		
		protected void onPreExecute() {
			base.showProgressBar();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			JSONObject json;
			boolean flag = true;
			try {
				JSONObject mJsonObject = new JSONObject();
				if(isEditButtonEnable){
					mJsonObject.put("fbid", base.application.getUserinfo().userId);
				}else{
					mJsonObject.put("fbid", params[0]);
				}
				json = HttpClient.SendHttpPost(UrlCons.PROFILE_WITH_COMMENTS.getUrl(),mJsonObject);
				if (json != null) {
					name     = json.getString("first_name")+" "+json.getString("last_name");
					age      = json.getString("age");
					image    = json.getString("image");
					loc      = json.getString("city");
					fav_mountain  = json.getString("favorite_mountain");
					shred_style   = json.getString("shred_style");
					about_me      = json.getString("about_me");
					JSONArray jArrDeals = json.getJSONArray("purchased_deals");
					profileDealsArr.clear();
					for(int i=0; i<jArrDeals.length(); i++){
						JSONObject c = jArrDeals.getJSONObject(i);
						String deals_name = c.getString("deals_name");
						String deals_id = c.getString("deals_id");
						String deals_image_link = c.getString("image");
						profileDealsArr.add(new ProfileDealsBean(deals_id, deals_name, deals_image_link));
					}
					JSONArray jArrImage = json.getJSONArray("gallery");
					imageArr.clear();
					
					for(int i=0; i<jArrImage.length(); i++){
					    ImageBean bean = new ImageBean();
						JSONObject c = jArrImage.getJSONObject(i);
						String image_id = c.getString("id");
						bean.setImageId(image_id);
						String image_link = UrlCons.GALLERY_IMG_PATH.getUrl()+c.getString("image");
						bean.setImageLink(image_link);
						JSONArray jArray = c.getJSONArray("comments");
						ArrayList<CommentBean> commentArr = new ArrayList<CommentBean>();
						for (int i1 = 0; i1 < jArray.length(); i1++) {
						    JSONObject c1 = jArray.getJSONObject(i1);
						    String fname = c1.getString("first_name");
						    String lname = c1.getString("last_name");
						    String profile_pic = c1.getString("profile_picture");
						    String txt_commets = c1.getString("comment");
						    commentArr.add(new CommentBean(fname, lname,   profile_pic, txt_commets));
						    bean.setCommentArr(commentArr);
						}
						imageArr.add(bean);				
					}
				}
			} catch (Exception e) {
				base.dismissProgressBar();
				return flag;
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			base.dismissProgressBar();
			if (result) {
				txt_profile_name.setText(name);
				tv_prof_age.setText(age);
				txt_profile_location.setText(loc);
				imageloader.DisplayImage(UrlCons.IMAGE_PATH.getUrl()+image, iv_profile_image);
				
				tv_prof_fev_mountain.setText(fav_mountain);
				txt_profile_shred_style.setText(shred_style);
				txt_prof_about_me.setText(about_me);
				
				
				if(isEditButtonEnable){
					iv_profile_image.setClickable(true);
					btnGalleryImageUpload.setVisibility(View.VISIBLE);
					ll_edit_button_layout.setVisibility(View.VISIBLE);
				}else{
					iv_profile_image.setClickable(false);					
					ll_icon_prof_loc_edit_save.setVisibility(View.GONE);
					ll_icon_prof_fav_mountain_edit_save.setVisibility(View.GONE);
					ll_icon_prof_about_me_edit_save.setVisibility(View.GONE);
					btnGalleryImageUpload.setVisibility(View.GONE);
					ll_edit_button_layout.setVisibility(View.GONE);
				}
				
			    mProfileDealsAdapter = new ProfileDealsAdapter(base, R.layout.row_deals, profileDealsArr);
			    fgv_prof_deals_gallery.setAdapter(mProfileDealsAdapter);
			    fgv_prof_deals_gallery.setSelection(profileDealsArr.size()/2);
			    mGalleryAdapter = new GalleryAdapter(base, R.layout.row_image_gallery, imageArr);
			    gv_image_gallery.setAdapter(mGalleryAdapter);	
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_profile_edit:
			
			Intent i = new Intent(base, ProfileEdit.class);
		    i.putExtra("fname", base.application.getUserinfo().first_name);
		    i.putExtra("lname", base.application.getUserinfo().last_name);
		    i.putExtra("age", age);
		    i.putExtra("location", loc);
		    i.putExtra("fav_mountain",fav_mountain);
		    i.putExtra("shred_style", shred_style);
		    i.putExtra("about_me", about_me);
		    startActivity(i);
			
			break;
			
		case R.id.btn_upload_img:
			Intent i1 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i1, REQUEST_CODE_IMAGE_GALLERY);
			break;
		}
	}
	
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_CODE_IMAGE_GALLERY && null != data) {
		    Uri selectedImage = data.getData();
		    String[] filePathColumn = { MediaStore.Images.Media.DATA };
		    Cursor cursor = base.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
		    cursor.moveToFirst();
		    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		    String imagepath = cursor.getString(columnIndex);
		    cursor.close();

		    new GalleryUploadTask().execute(imagepath);
		}
	  }
	 
	 class GalleryUploadTask extends AsyncTask<String, Void, ArrayList<ImageBean>> {
		 @Override
		 protected ArrayList<ImageBean> doInBackground(String... param) {
			 try {
				 DefaultHttpClient httpClient = new DefaultHttpClient();
				 HttpContext localContext = new BasicHttpContext();
				 HttpPost httpPost = new HttpPost(UrlCons.GALLERY_UPLOAD.getUrl());
				 MultipartEntity entity = new MultipartEntity(
				 HttpMultipartMode.BROWSER_COMPATIBLE);
				 entity.addPart("gallery", new FileBody(new File(param[0])));
				 entity.addPart("fbid", new StringBody(base.application.getUserinfo().userId));
				 httpPost.setEntity(entity);
				 HttpResponse response;
				 response = httpClient.execute(httpPost);
				 resEntity = response.getEntity();

				 final String response_str = EntityUtils.toString(resEntity);
				 JSONObject jobj = new JSONObject(response_str);
				 JSONArray jArrImage = jobj.getJSONArray("gallery");
				 imageArr.clear();
				 for (int i = 0; i < jArrImage.length(); i++) {
					 ImageBean bean = new ImageBean();
					 JSONObject c = jArrImage.getJSONObject(i);
					 String image_id = c.getString("id");
					 bean.setImageId(image_id);
					 String image_link = UrlCons.GALLERY_IMG_PATH.getUrl() + c.getString("image");
					 bean.setImageLink(image_link);
					 JSONArray jArray = c.getJSONArray("comments");
					 ArrayList<CommentBean> commentArr = new ArrayList<CommentBean>();
					 for (int i1 = 0; i1 < jArray.length(); i1++) {
						 JSONObject c1 = jArray.getJSONObject(i1);
						 String fname = c1.getString("first_name");
						 String lname = c1.getString("last_name");
						 String profile_pic = c1.getString("profile_picture");
						 String txt_commets = c1.getString("comment");
						 commentArr.add(new CommentBean(fname, lname,profile_pic, txt_commets));
						 bean.setCommentArr(commentArr);
					 }
					 imageArr.add(bean);
				 }
				 return imageArr;
			 } catch (Exception e) {
			return null;
	    }
	}

	@Override
	protected void onProgressUpdate(Void... unsued) {

	}

	@Override
	protected void onPostExecute(ArrayList<ImageBean> sResponse) {
	   
	    if (sResponse != null) {
		txt_profile_location.setText(loc);
		tv_prof_fev_mountain.setText(fav_mountain);
		txt_profile_shred_style.setText(shred_style);
		txt_prof_about_me.setText(about_me);
		tv_prof_age.setText(age);
		txt_profile_name.setText(base.application.getUserinfo().first_name + " "+ base.application.getUserinfo().last_name);
		imageloader.DisplayImage(base.application.getUserinfo().profile_image,iv_profile_image);

	    }
	}
 }
}
