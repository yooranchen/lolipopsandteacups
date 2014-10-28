package snowmada.main.view;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.adapter.CommentsAdapter;
import snowmada.main.bean.CommentBean;
import snowmada.main.bean.ImageBean;
import snowmada.main.constant.Constant;
import snowmada.main.network.HttpClient;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImagePagerActivity extends BaseActivity {
  
    DisplayImageOptions options;
    com.nostra13.universalimageloader.core.ImageLoader imageloader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    ArrayList<ImageBean> imageUrls;
    ImageLoaderConfiguration config;
    ViewPager pager;
    int select_positon;
    ListView lv_comments;
    ArrayList<CommentBean> commentArr = new ArrayList<CommentBean>();
   
    @SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.ac_image_pager);
    	select_positon = Integer.parseInt(getIntent().getExtras().getString("position"));
    	imageUrls = Constant.mItemsList;
    	
    	pager = (ViewPager) findViewById(R.id.pager);
    	pager.setAdapter(new ImagePagerAdapter(imageUrls));
    	pager.setCurrentItem(select_positon);
    }

    private class ImagePagerAdapter extends PagerAdapter {

	private ArrayList<ImageBean> images;
	private LayoutInflater inflater;

	ImagePagerAdapter(ArrayList<ImageBean> images) {
	    this.images = images;
	    inflater = getLayoutInflater();
	    
	    config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.threadPoolSize(2)
		.memoryCacheExtraOptions(480, 800)
  		.denyCacheImageMultipleSizesInMemory()		
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.memoryCache(new WeakMemoryCache()) 		
		.writeDebugLogs() 
		.build();
	    
	    imageloader.init(config);
		
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.app_logo)
		.showImageOnFail(R.drawable.app_logo)
		.resetViewBeforeLoading()
		.cacheOnDisc(true)
		
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    container.removeView((View) object);
	}

	@Override
	public int getCount() {
	    return images.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
	    View imageLayout = inflater.inflate(R.layout.item_pager_image,  view, false);
	    assert imageLayout != null;
	    ImageView imageView = (ImageView) imageLayout .findViewById(R.id.image);
	    final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
	    Button btn_submit = (Button) imageLayout .findViewById(R.id.btn_submit);
	    final EditText et_comments = (EditText) imageLayout .findViewById(R.id.et_comments);	    
	    lv_comments = (ListView) imageLayout.findViewById(R.id.lv_comments);
	      
	    btn_submit.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    String comment = et_comments.getText().toString().trim();
		    if (comment.length() > 0) {
			et_comments.setText("");
			select_positon = position;
			imageUrls.get(position).commentArr.add(new CommentBean(application.getUserinfo().first_name,application.getUserinfo().last_name, "", comment));
			new SubmitComments().execute(application.getUserinfo().userId,images.get(position).getImageId(), comment);
			notifyDataSetChanged();
		    }
		}
	    });

	    imageloader.displayImage(images.get(position).getImageLink(),imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			    spinner.setVisibility(View.VISIBLE);
			    Log.e("TAG5", "Reachhere "+position);			  
			    commentArr = imageUrls.get(position).getCommentArr();
			    for(int i=0; i<commentArr.size(); i++){
				Log.e("TAG5", commentArr.get(i).getComments());
			    }
			    CommentsAdapter commentsAdapter = new CommentsAdapter(ImagePagerActivity.this, R.layout.row_comments, commentArr);
			    lv_comments.setAdapter(commentsAdapter);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,	FailReason failReason) {
			    String message = null;
			    switch (failReason.getType()) {
			    case IO_ERROR:
				message = "Input/Output error";
				break;
			    case DECODING_ERROR:
				message = "Image can't be decoded";
				break;
			    case NETWORK_DENIED:
				message = "Downloads are denied";
				break;
			    case OUT_OF_MEMORY:
				message = "Out Of Memory error";
				break;
			    case UNKNOWN:
				message = "Unknown error";
				break;
			    }
			    Toast.makeText(ImagePagerActivity.this, message,Toast.LENGTH_SHORT).show();
			    spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri,
				View view, Bitmap loadedImage) {
			    spinner.setVisibility(View.GONE);
			}
		    });

	    view.addView(imageLayout, 0);
	    return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
	    return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {

	}

	@Override
	public Parcelable saveState() {
	    return null;
		}
    }
    public class SubmitComments extends AsyncTask<String, String, Boolean> {

	String comments;

	@Override
	protected Boolean doInBackground(String... params) {
	    boolean flg = false;
	    comments = params[2];
	    try {
		JSONObject request = new JSONObject();
		request.put("user_id", params[0]);
		request.put("image_id", params[1]);
		request.put("comment", params[2]);

		JSONObject response = HttpClient.SendHttpPost(UrlCons.SUBMIT_COMMENTS.getUrl(), request);
		Log.e("snomada", response.toString());
		if (response != null) {
		    flg = response.getBoolean("status");

		}
	    } catch (JSONException e) {
		e.printStackTrace();
		return null;
	    }
	    return flg;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    super.onPostExecute(result);
	    if (result != null) {
		
	    }
	}
   }
}
