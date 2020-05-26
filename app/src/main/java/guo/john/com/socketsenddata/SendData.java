package guo.john.com.socketsenddata;

import java.io.Serializable;

public class SendData implements Serializable {

	private int sendType;
	private String data;

	public SendData(int sendType, String data) {
		super();
		this.sendType = sendType;
		this.data = data;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
