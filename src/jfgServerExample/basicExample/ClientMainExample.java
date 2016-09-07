package jfgServerExample.basicExample;

import net.jfabricationgames.jfgserver.client.JFGClient;
import net.jfabricationgames.jfgserver.client.JFGClientMessage;
import net.jfabricationgames.jfgserver.client.JFGServerMessage;
import net.jfabricationgames.jfgserver.interpreter.JFGClientInterpreter;

public class ClientMainExample {
	
	/**
	 * The client side of the implementation starts here.
	 * 
	 * The connection doesn't need to be set up directly after starting the program.
	 * You can do this any time you want.
	 */
	public static void main(String[] args) {
		new ClientMainExample();
	}
	
	/**
	 * Create the ClientMainExample that contains the client side connection to the server.
	 */
	public ClientMainExample() {
		//create a new JFGClient that connects to the echo server from the example in basicExample.ServerMainExample
		String host = "jfabricationgames.ddns.net";//could be any other port also
		int port = 4711;//the port needs to be the same as set in the server to correctly connect.
		JFGClientInterpreter interpreter = new JFGClientInterpreter() {
			@Override
			public void interpreteClientMessage(JFGClientMessage message, JFGClient connection) {
				//react on the message you received from the server here.
				if (message instanceof BasicMessage) {
					BasicMessage basic = (BasicMessage) message;
					//just print the data on the terminal for this test
					System.out.println(basic.getMessage());
					System.out.println(basic.getFirstInt());
				}
			}
		};
		JFGClient client = new JFGClient(host, port, interpreter);
		System.out.println("client started");
		//using the JFGClient(String, int, JFGClientInterpreter) constructor the client is created and started directly.
		//by using another constructor without the JFGClientInterpreter the client would not be started until it gets
		//a client interpreter by the JFGClient.setClientInterpreter(JFGClientInterpreter) method.
		
		//after the client is created and started you can send messages to the server.
		//the messages sent have to implement JFGServerMessage to be interpreted by the server (like BasicMessage in this example).
		BasicMessage message = new BasicMessage("The Answer is:", 42);
		client.sendMessage(message);
		
		//the message is send to the server and interpreted (and in this example echoed).
		//after the server answered to the message (what doesn't need to happen depending on your implementation of the 
		//JFGServerInterpreter) the message this client received from the server is printed on the screen because that's what
		//our implementation of the JFGClientInterpreter does in this example.
		
		//If a message is send to the server that couldn't be interpreted correctly by the servers interpreter, in this example
		//the server will just print a message and don't send anything back to the client:
		class ExampleMessage implements JFGServerMessage {
			private static final long serialVersionUID = 6781435771156169802L;
			private String foo = "A message that can't be interpreted";
		}
		//client.sendMessage(new ExampleMessage());
		
		//after the messages are sent and the connection to the server is no longer needed (probably when closing the program)
		//the client should be closed:
		client.closeConnection();
		
		//depending on your implementation you could also first send a message to the server that informs the server that this
		//connection is closed now. But the server also realizes that the connection was closed and removes the connection from
		//it's list of active connections.
	}
}