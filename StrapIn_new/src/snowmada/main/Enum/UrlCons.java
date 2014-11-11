package snowmada.main.Enum;

public enum UrlCons {
SEND_CURRENT_LOCATION("http://106.187.95.65/snowmada/snowmada/send_current_location.php"),
SIGNUP("http://106.187.95.65/snowmada/snowmada/sign_up.php"),
LOGIN("http://106.187.95.65/snowmada/snowmada/login_new.php"),
COUNT_PENDING_FRIEND("http://106.187.95.65/snowmada/snowmada/count_pending_friend.php"),
ADD_FRIEND("http://106.187.95.65/snowmada/snowmada/add_friend_request.php"),
STATUS_TRACK_TOOGLE("http://106.187.95.65/snowmada/snowmada/update_track_status.php"),
ACCEPT_FRIEND_REQ("http://106.187.95.65/snowmada/snowmada/accept_friend.php"),
MESSAGE("http://106.187.95.65/snowmada/snowmada/new_chat_history.php"),
ACTIVE_APP_USERS("http://106.187.95.65/snowmada/snowmada/app_user.php"),
ADD_MEET_UP_LOCATION("http://106.187.95.65/snowmada/snowmada/add_meet_up.php"),
MEET_UP_MERKER_LIST("http://106.187.95.65/snowmada/snowmada/list_meetup.php"),
CHAT("http://106.187.95.65/snowmada/snowmada/chat.php"),
MEET_UP_DETELE("http://106.187.95.65/snowmada/snowmada/del_meet_up.php"),
FRIEND_DELETE("http://106.187.95.65/snowmada/snowmada/delete_friend.php"),
//PENDING_FRIEND_REQUEST("http://106.187.95.65/snowmada/snowmada/pending_friend.php"),
CHAT_HISTORY("http://106.187.95.65/snowmada/snowmada/chat_history.php"),
SEND_MESSAGE("http://106.187.95.65/snowmada/snowmada/send_message.php"),
GET_LOCATION("http://106.187.95.65/snowmada/snowmada/track.php"),
FRIEND_LIST("http://106.187.95.65/snowmada/snowmada/fr_friend_list.php"),
SKI_PATROL("http://106.187.95.65/snowmada/snowmada/ski_patrol.php"),
IMAGE_PATH("http://106.187.95.65/snowmada/snowmada/uploads/"),
GALLERY_IMG_PATH("http://106.187.95.65/snowmada/snowmada/uploads/gallery/"),
UPDATE_PROFILE("http://106.187.95.65/snowmada/snowmada/update_profile_snowmada.php"),
GALLERY_UPLOAD("http://106.187.95.65/snowmada/snowmada/gallery_upload.php"),
PROFILE("http://106.187.95.65/snowmada/snowmada/profile_info.php"),
VIEW_COMMENTS("http://106.187.95.65/snowmada/snowmada/view_comment.php"),
SUBMIT_COMMENTS("http://106.187.95.65/snowmada/snowmada/submit_comment.php"),
PROFILE_WITH_COMMENTS("http://106.187.95.65/snowmada/snowmada/profile_info_comment.php"),
SIGNAL_STATUS("http://106.187.95.65/snowmada/snowmada/device_signal_status.php"),
PROFILE_IMAGE_UPDATE("http://106.187.95.65/snowmada/snowmada/profile_image_update.php"),
PROFILE_INFO_UPDATE("http://106.187.95.65/snowmada/snowmada/profile_update_snowmada.php"),
GOOD_DEALS("http://106.187.95.65/snowmada/snowmada/banner_advertisements.php"),
GCM_REGITER("http://106.187.95.65/snowmada/snowmada/register.php"),
DEALS_DETAILS("http://106.187.95.65/snowmada/snowmada/advt_details.php"),
BANNER_ADD("http://106.187.95.65/snowmada/snowmada/uploads/advertisements/banners/"),

GET_USER_EXCEPT_FRIEND("http://106.187.95.65/snowmada/snowmada/fr_get_all_user_except_friend.php"),
REQUEST_FRIEND("http://106.187.95.65/snowmada/snowmada/fr_add_friend.php"),
ACCEPT_FRIEND_REQUEST("http://106.187.95.65/snowmada/snowmada/fr_friend_request_accept.php"),
PENDING_FRIEND_REQUEST_FROM_ME("http://106.187.95.65/snowmada/snowmada/fr_my_pending_request.php"),
PENDING_FRIEND_REQUEST_TO_ME("http://106.187.95.65/snowmada/snowmada/fr_view_my_friend_req.php"),
DELETE_FRIEND("http://106.187.95.65/snowmada/snowmada/fr_delete_friend.php"),
CHECK_TRACK_STATUS("http://106.187.95.65/snowmada/snowmada/fr_track_status.php");

UrlCons(String ob) {
	this.url = ob;
}
public String getUrl() {
	return url;
}
public String url;

}
