package snowmada.main.model;

public interface OnFriendDialogListener {
	public void onTack(String id);
	public void onViewProfile(String id);
	public void onChat(String id);
	public void onDelete(String id);
}
