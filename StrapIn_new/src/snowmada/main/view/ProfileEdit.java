package snowmada.main.view;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import snowmada.main.Enum.UrlCons;
import snowmada.main.Util.ImageLoader;
import snowmada.main.constant.Constant;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Global;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressWarnings("unused")
public class ProfileEdit extends BaseActivity {
	private Button btn_submit;
	private Button btn_cancel;

	private EditText et_fname;
	private EditText et_lname;
	private EditText et_age;
	private ImageView iv_user_image;
	private EditText et_user_location;
	private EditText et_fav_mountain;
	private EditText et_about_me;
	private Spinner spinner;
	String fname;
	String lname;
	String age;
	String location;
	String fav_montain;
	String shred_style;
	String about_me;
	public ImageLoader imageLoader;
	public int Spinner_pos = 0;
	public HttpEntity resEntity;
	public String imagepath = null;

	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	private final int PIC_CROP = 0x3;
	public static final int REQUEST_CODE_IMAGE_GALLERY = 0x4;
	private static final int ACTION_REQUEST_FEATHER = 5;
	public String filepath = null;
	final String mountain[] = { "All Mountain", "Park Bum", "Pow Pow Bum", "GNAR" };

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.profile_update);

		Constant.isEditedProfile = true;

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		location = bundle.getString("location");
		fav_montain = bundle.getString("fav_mountain");
		shred_style = bundle.getString("shred_style");
		about_me = bundle.getString("about_me");
		fname = bundle.getString("fname");
		lname = bundle.getString("lname");
		age = bundle.getString("age");

		imageLoader = new ImageLoader(this);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setText(Html.fromHtml("<font color=\"#ffffff\">SUB</font><font color=\"#28b6ff\">MIT</font>"));
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setText(Html.fromHtml("<font color=\"#ffffff\">CAN</font><font color=\"#28b6ff\">CEL</font>"));
		iv_user_image = (ImageView) findViewById(R.id.iv_profile_img);

		et_fname = (EditText) findViewById(R.id.et_fname);
		et_lname = (EditText) findViewById(R.id.et_lname);
		et_age = (EditText) findViewById(R.id.et_age);
		et_age.setText(age);
		et_user_location = (EditText) findViewById(R.id.et_location);
		et_user_location.setText(location);
		et_fav_mountain = (EditText) findViewById(R.id.et_fav_mountain);
		et_fav_mountain.setText(fav_montain);
		et_about_me = (EditText) findViewById(R.id.et_about_me);
		et_about_me.setText(about_me);
		spinner = (Spinner) findViewById(R.id.spinner_shared_style);

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mountain);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
																									// drop
																									// down
																									// view

		spinner.setAdapter(spinnerArrayAdapter);
		if (shred_style.equalsIgnoreCase(mountain[0])) {
			spinner.setSelection(0);
		} else if (shred_style.equalsIgnoreCase(mountain[1])) {
			spinner.setSelection(1);
		} else if (shred_style.equalsIgnoreCase(mountain[2])) {
			spinner.setSelection(2);
		} else if (shred_style.equalsIgnoreCase(mountain[3])) {
			spinner.setSelection(3);
		} else {
			spinner.setSelection(0);
		}

		iv_user_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEdit.this);
				builder.setCancelable(true);
				builder.setTitle("Updolad Image");
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(i, REQUEST_CODE_TAKE_PICTURE);
					}
				});
				builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, REQUEST_CODE_GALLERY);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				spinner.setSelection(arg2);
				Spinner_pos = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValid())
					new ProfileInfoUpload().execute(et_user_location.getText().toString().trim(), et_fav_mountain.getText().toString().trim(), mountain[Spinner_pos], et_about_me.getText().toString().trim());

			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProfileEdit.this.finish();

			}
		});

	}

	class ImageUploadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... param) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(UrlCons.PROFILE_IMAGE_UPDATE.getUrl());
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				entity.addPart("fbid", new StringBody(application.getUserinfo().userId));
				entity.addPart("file", new FileBody(new File(filepath)));
				httpPost.setEntity(entity);

				HttpResponse response;
				response = httpClient.execute(httpPost);
				resEntity = response.getEntity();

				final String response_str = EntityUtils.toString(resEntity);
				Log.e("TAG12", "Response " + response_str);
				return response_str;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressBar();
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (sResponse != null) {
				dismissProgressBar();

				try {
					JSONObject obj = new JSONObject(sResponse);
					System.out.println("!-- response11" + sResponse.toString());
					if (obj.getBoolean("status")) {
						filepath = null;
						if (!obj.isNull("image")) {
							application.getUserinfo().profile_image = UrlCons.IMAGE_PATH.getUrl() + obj.getString("image");
							Toast.makeText(getApplicationContext(), "Profile image updated successfully", Toast.LENGTH_LONG).show();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class ProfileInfoUpload extends AsyncTask<String, Void, Boolean> {
		String loc;
		String favorite_mountain;
		String shred_style;
		String about_me;

		@Override
		protected Boolean doInBackground(String... param) {
			loc = param[0];
			favorite_mountain = param[1];
			shred_style = param[2];
			System.out.println("!-- Shred style param " + param[2]);
			about_me = param[3];
			try {
				JSONObject req = new JSONObject();
				req.put("fbid", application.getUserinfo().userId);
				req.put("fname", et_fname.getText().toString().trim());
				req.put("lname", et_lname.getText().toString().trim());
				req.put("age", et_age.getText().toString().trim());
				req.put("city", param[0]);
				req.put("favorite_mountain", param[1]);
				req.put("shred_style", param[2]);
				req.put("about_me", param[3]);
				System.out.println("!-- Request" + req.toString());
				JSONObject res = snowmada.main.network.HttpClient.SendHttpPost(UrlCons.PROFILE_INFO_UPDATE.getUrl(), req);
				System.out.println("!-- response" + res.toString());
				if (res != null) {
					return res.getBoolean("status");
				} else {
					return false;
				}

			} catch (Exception e) {
				dismissProgressBar();
				return false;
			}
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressBar();
		}

		@Override
		protected void onPostExecute(Boolean sResponse) {
			dismissProgressBar();
			if (sResponse) {

				application.getUserinfo().first_name = et_fname.getText().toString().trim();
				application.getUserinfo().last_name = et_lname.getText().toString().trim();
				Toast.makeText(getApplicationContext(), "Profile update successfully", Toast.LENGTH_LONG).show();
				ProfileEdit.this.finish();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String imagepath = cursor.getString(columnIndex);
			cursor.close();
			performCrop(getImageContentUri(ProfileEdit.this, new File(imagepath)));
		}

		if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK && null != data) {

			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
			int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToLast();
			imagepath = cursor.getString(column_index_data);
			performCrop(getImageContentUri(ProfileEdit.this, new File(imagepath)));
		}

		if (requestCode == PIC_CROP && resultCode == RESULT_OK && null != data) {
			Bundle extras = data.getExtras();
			Bitmap yourSelectedImage = extras.getParcelable("data");
			doSaveNewImage(yourSelectedImage);
			iv_user_image.setImageBitmap(yourSelectedImage);
			new ImageUploadTask().execute();

		}

	}

	private void performCrop(Uri picUri) {
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			cropIntent.setDataAndType(picUri, "image/*");
			cropIntent.putExtra("crop", "true");
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			cropIntent.putExtra("outputX", 160);
			cropIntent.putExtra("outputY", 120);
			cropIntent.putExtra("return-data", true);
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void doSaveNewImage(Bitmap bitmap) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/snomada");
		myDir.mkdirs();
		String fname = "" + System.currentTimeMillis() + ".jpg";
		filepath = root + "/snomada/" + fname;
		Log.e("TAG", root + "/snomada/" + fname);
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ", new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	public boolean isValid() {
		boolean flag = true;
		if (!(et_fname.getText().toString().trim().length() > 0)) {
			et_fname.setError("Please enter First Name");
			flag = false;
		}
		if (!(et_lname.getText().toString().trim().length() > 0)) {
			et_lname.setError("Please enter Last Name");
			flag = false;
		}
		if (!(et_age.getText().toString().trim().length() > 0)) {
			et_age.setError("Please enter Age");
			flag = false;
		}
		if (!(et_user_location.getText().toString().trim().length() > 0)) {
			et_user_location.setError("Please enter location");
			flag = false;
		}

		if (!(et_fav_mountain.getText().toString().trim().length() > 0)) {
			et_fav_mountain.setError("Please enter favorite mountain");
			flag = false;
		}

		if (!(et_about_me.getText().toString().trim().length() > 0)) {
			et_about_me.setError("Please enter about me");
			flag = false;
		}
		return flag;
	}
}
