package snowmada.main.socket;


public class RefreshUsers extends Thread {
private ConnectionProcess cp;

	public RefreshUsers(ConnectionProcess cp) {
		this.cp = cp;
	}

	public void run() {
		while (true) {
			cp.sendData("U" + ":");
			
			try {
				this.sleep(15000);
			} catch (InterruptedException e) {
			}
		}
	}
}
