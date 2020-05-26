package guo.john.com.socketsenddata;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Socket Manager
 * @author guo.john.com
 */
public class SocketManager {

	private static SocketManager mSocketManager;
	private static Socket clientSocket;
	private static PrintWriter writer;

	/**
	 * 获取实体
	 * @return
	 */
	public static SocketManager getInstance() {
		if (mSocketManager == null) {
			mSocketManager = new SocketManager();
		}
		return mSocketManager;
	}

	/**
	 * 初始化socket
	 */
	public void initSocket() {
		try {
			clientSocket = new Socket("192.168.0.104", 8888);
			writer = new PrintWriter(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取socket的输出
	 */
	public PrintWriter getWrite() {
		return writer;
	}

}
