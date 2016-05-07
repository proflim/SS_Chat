package Main;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class User implements java.io.Serializable{
	private String userName;
	private int userID;
	private transient Socket clientSocket=null;
	private transient ObjectOutputStream outputToClient=null;
	
	public User(String name, int id, Socket cSocket, ObjectOutputStream os){
	
		this.userName = name;
		this.userID = id;
		this.clientSocket = cSocket;
		this.outputToClient = os;
	}
	
	public User(String name, int id){
		this.userName = name;
		this.userID = id;
	}
	
	public void setUserName(String name){
		this.userName=name;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public int getUserID(){
		return this.userID;
	}
	
	public Socket getClientSocket(){
		return this.clientSocket;
	}
	
	public ObjectOutputStream getOutputStream(){
		return this.outputToClient;
	}
	
}
