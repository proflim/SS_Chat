package Main;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * User Class
 * defines User to store in UserList
 * @author ÀÓ¼º¼ö
 *
 */
public class User implements java.io.Serializable{
	private String userName;
	private int userID;
	private transient Socket clientSocket=null;
	private transient ObjectOutputStream outputToClient=null;
	
	/**
	 * User Constructor
	 * @param name
	 * @param id
	 * @param cSocket
	 * @param os
	 */
	public User(String name, int id, Socket cSocket, ObjectOutputStream os){
	
		this.userName = name;
		this.userID = id;
		this.clientSocket = cSocket;
		this.outputToClient = os;
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param id
	 */
	public User(String name, int id){
		this.userName = name;
		this.userID = id;
	}
	
	/**
	 * setter for UserName
	 * @param name
	 */
	public void setUserName(String name){
		this.userName=name;
	}
	
	/**
	 * getter for UserName
	 * @return String UserName
	 */
	public String getUserName(){
		return this.userName;
	}
	
	/**
	 * getter for UserID
	 * @return int userID
	 */
	public int getUserID(){
		return this.userID;
	}
	
	/**
	 * getter for clientSocket
	 * @return Socket clientSocket
	 */
	public Socket getClientSocket(){
		return this.clientSocket;
	}
	
	/**
	 * getter for outputstream
	 * 
	 * @return ObjectOutputStream outputstream
	 */
	public ObjectOutputStream getOutputStream(){
		return this.outputToClient;
	}
	
}
