package guo.john.com.socketsenddata;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private String userName;
	private String userSex;
	
	public UserInfo(String userName, String userSex) {
		super();
		this.userName = userName;
		this.userSex = userSex;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

}
