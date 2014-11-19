package snowmada.main.socket;


public class PingChatServer extends Thread {
	private ConnectionProcess cp;

	public PingChatServer(ConnectionProcess cp) {

		
		this.cp = cp;
	}

	public void run() {
		while (true) {
			System.out.println("PingChatServer" + "->P");
			cp.sendData("P");
			try {
				this.sleep(15000);
			} catch (InterruptedException e) {
				System.out.println("PingClient interrupted " + e.getMessage());

			}
		}
	}
}
