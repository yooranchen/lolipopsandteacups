package snowmada.main.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import android.widget.Toast;

import snowmada.main.model.OnMessageProcess;
import snowmada.main.view.SocketService;

public class ConnectionProcess extends Thread {

	private static final int SERVER_PORT = 2000;
	private Socket socket;
	private DataInputStream dataIn;
	private PrintStream dataOut;
	private OnMessageProcess listener;
	private Queue<String> messageQueue;

	public ConnectionProcess( SocketService service) {
		this.listener = (OnMessageProcess) service;
		messageQueue = new LinkedList<String>();
		//Toast.makeText(service.getApplicationContext(), "AAA", 1000).show();
	}

	/*public void sendData(String message) {

		if (socket != null) {

			try {
				dataOut.println(message);
				System.out.println("Sent: " + message);
				dataOut.flush();
			} catch (Exception e) {
				System.out.println("Dataout Exception :" + e.toString());
			}
		} else
			System.out.println("Socket not connected");
	}*/
	
	
	public void sendData(String message) {
	// Toast.makeText(client, "ping", 1000).show();
	if (socket != null) {
		if (socket.isConnected()) {
			try {
				String sss;
				while ((sss = messageQueue.poll()) != null) {
					dataOut.println(sss);
					System.out.println("Sent: " + sss);
					dataOut.flush();
				}
				dataOut.println(message);
				System.out.println("Sent: " + message);
				dataOut.flush();
			} catch (Exception e) {
				System.out.println("Dataout Exception :" + e.toString());
			}

		} else {
			messageQueue.add(message);
		}
	} else {
		messageQueue.add(message);
		System.out.println("Socket not connected, added to queue");

	}

}

	public void closeSocket() {
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public synchronized void run() {
		try {
			socket = new Socket("192.168.0.108", SERVER_PORT);
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new PrintStream(socket.getOutputStream());
			
			System.out.println("Socket connected");
			//listener.socketConnected();
			StringBuffer buffer = new StringBuffer();
			char tmp = ' ';
			while (true) {
				tmp = dataIn.readChar();
				if (tmp == '\n') {
					if (buffer.toString() != null) {
						listener.processValue(buffer.toString());
						System.out.println(buffer.toString());
					}
					buffer = new StringBuffer();
				} else {
					// System.out.println("char===>>>>>> "+tmp);
					buffer.append(tmp);
				}
			}

		} catch (UnknownHostException e) {
			System.err.println("ERROR 111" + e.getMessage() + "\n");
		} catch (IOException e) {
			System.err.println("ERROR 222" + e.getMessage() + "\n");
		}
	}

}
