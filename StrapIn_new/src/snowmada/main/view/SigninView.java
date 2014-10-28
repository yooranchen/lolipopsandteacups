package snowmada.main.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

public class SigninView extends BaseActivity {

	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private LoginButton loginButton;
	private PendingAction pendingAction = PendingAction.NONE;
	private GraphUser user;
	private boolean isUiUpdateCall = false;
	private ProgressBar signin_progress;
	private String first_name;
	private String last_name;
	private String user_id;
	private String dob;
	private final String TAG = "snomada";
	private UiLifecycleHelper uiHelper;
	private String image = "";

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}
		setContentView(R.layout.signin);
		loginButton = (LoginButton) findViewById(R.id.login_button);
		signin_progress = (ProgressBar) findViewById(R.id.signin_progress);

		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			public void onUserInfoFetched(GraphUser user) {
				SigninView.this.user = user;
				isUiUpdateCall = true;
				updateUI();
				handlePendingAction();
			}
		});
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			break;
		case POST_STATUS_UPDATE:
			break;
		}
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (pendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(SigninView.this).setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted).setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}

	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void updateUI() {
		if (isUiUpdateCall) {

			isUiUpdateCall = false;
			Session session = Session.getActiveSession();
			boolean enableButtons = (session != null && session.isOpened());

			if (enableButtons && user != null) {

				signin_progress.setVisibility(View.VISIBLE);
				first_name = user.getFirstName();
				last_name = user.getLastName();
				dob = user.getBirthday();
				user_id = user.getId();
				image = "https://graph.facebook.com/" + user_id + "/picture";
				application.getUserinfo().setUser(first_name, last_name, user_id, image, dob);
				application.getUserinfo().setSession(true);
				startActivity(new Intent(SigninView.this, HomeView.class));
				SigninView.this.finish();
			}
		}
	}
}
