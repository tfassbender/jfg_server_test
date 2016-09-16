package jfgServerExample.loginServer;

import net.jfabricationgames.jfgserver.client.JFGServerMessage;
import net.jfabricationgames.jfgserver.interpreter.JFGServerInterpreter;
import net.jfabricationgames.jfgserver.server.JFGConnection;
import net.jfabricationgames.jfgserver.server.JFGLoginServer;

public class DefaultLoginServerInterpreter implements JFGServerInterpreter {
	
	private JFGLoginServer server;
	
	private int failedLogins;
	private int loginTries = 3;
	
	private String onlyUser = "theAnswer";
	private String onlyPassword = "42";
	
	private static LoginFeedback loginAccepted = new LoginFeedback(true);
	private static LoginFeedback loginWrong = new LoginFeedback(false, 1, "Wrong username or password");
	private static LoginFeedback loginDenied = new LoginFeedback(false, 2, "To many failed logins. Connection ended.");
	
	public DefaultLoginServerInterpreter(JFGLoginServer server) {
		this.server = server;
	}
	
	@Override
	public void interpreteServerMessage(JFGServerMessage message, JFGConnection connection) {
		if (message instanceof LoginMessage) {
			//login messages
			if (!server.isLoggedIn(connection)) {
				LoginMessage loginMessage = (LoginMessage) message;
				if (loginMessage.getUser().equals(onlyUser) && loginMessage.getPassword().equals(onlyPassword)) {
					//accept the login
					server.acceptLogin(connection);
					connection.sendMessage(loginAccepted);
				}
				else {
					//deny login
					failedLogins++;
					if (loginTries != -1 && failedLogins > loginTries) {
						//to many failed logins
						connection.sendMessage(loginDenied);
						server.denyLogin(connection);
					}
					else {
						connection.sendMessage(loginWrong);
					}
				}
			}
			else {
				System.err.println("DefaultLoginServerInterpreter: User is already logged in.");
			}
		}
		else if (message instanceof DefaultMessage) {
			//normal messages
			DefaultMessage textMessage = (DefaultMessage) message;
			//just broadcast the messages to all logged in users.
			server.sendBroadcast(textMessage);
		}
		else {
			//unknown message type
			System.err.println("The type of the message is unknown");
		}
	}
	
	@Override
	public JFGServerInterpreter getInstance() {
		return new DefaultLoginServerInterpreter(server);
	}
}