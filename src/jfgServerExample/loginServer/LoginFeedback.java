package jfgServerExample.loginServer;

import net.jfabricationgames.jfgserver.client.JFGClientMessage;

public class LoginFeedback implements JFGClientMessage {
	
	private static final long serialVersionUID = -1253479429717697886L;
	
	private boolean loginAccepted;
	
	private int errorCode;
	private String error;
	
	public LoginFeedback(boolean loginAccepted) {
		this.loginAccepted = loginAccepted;
		this.errorCode = 0;
		this.error = null;
	}
	
	public LoginFeedback(boolean loginAccepted, int errorCode, String error) {
		this.loginAccepted = loginAccepted;
		this.errorCode = errorCode;
		this.error = error;
	}
	
	public boolean isLoginAccepted() {
		return loginAccepted;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public String getError() {
		return error;
	}
}