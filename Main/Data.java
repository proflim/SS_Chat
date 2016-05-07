package Main;

import java.util.LinkedList;

/**
 * Data class 
 * defines the Object to be passed in communication between Server and Client
 * 
 * Type
 * 0: normal communication (client to server, server to client)
 * 1: update username (client to server)
 * 2: assign unique userID (server to client)
 * 3: terminate connection (separately handled)
 * 4: announce new user (server to client) 
 * 5: announce disconnection (server to client)
 * 
 * @author ÀÓ¼º¼ö
 *
 */
public class Data implements java.io.Serializable {


	  private int type=-1;
	  private String fromName="null";
	  private String toNameIDString="null";
	  private int fromID=-1;
	  private int toIndex=-1;	  
	  private String message="void";
	  private LinkedList<User> userList=null;

	  //Basic constructor
	  //Basic Communication (Client to Server to Client)
	  
	  /**
	   * Basic Constructor
	   * for communication (Client to Server to Client)
	   * @param t
	   * @param fname
	   * @param tnameidstr
	   * @param fid
	   * @param tidx
	   * @param msg
	   */
	  public Data(int t, String fname, String tnameidstr, int fid, int tidx, String msg) {
		  this.type = t;
		  this.fromName = fname;
		  this.toNameIDString = tnameidstr;
		  this.fromID = fid;
		  this.toIndex = tidx;
		  this.message = msg;		  
	  }
	  
	  
	  /**
	   * Constructor for sending initial Username (Client to Server)
	   * @param t
	   * @param fname
	   */
	  public Data(int t, String fname){
		  this.type = t;
		  this.fromName = fname;
	  }
	  
	  /**
	   * Constructor for: 
	   *   userName Update (Client to Server)
	   *   UserList Update (Server to Client)
	   * @param t
	   * @param fname
	   * @param fid
	   */
	  public Data(int t, String fname, int fid){
		  this.type = t;
		  this.fromName = fname;
		  this.fromID = fid;
	  }
	  
	  
	  /**
	   * Constructor for assigning uniqueID (Server to Client)
	   * @param t
	   * @param fid
	   * @param list
	   */
	  public Data(int t, int fid, LinkedList<User> list){
		  this.type = t;
		  this.fromID = fid;
		  this.userList = list;
	  }
	  
	  /**
	   * Constructor for quitting connection (Type = 3)
	   * @param t
	   */
	  public Data(int t){
		  this.type=t;
	  }
	  
	  /**
	   * Constructor for announcing new connection/disconnection (server to client)
	   * @param t
	   * @param fname
	   * @param fid
	   * @param msg
	   * @param list
	   */
	  public Data(int t, String fname, int fid, String msg, LinkedList<User> list){
		  this.type = t;
		  this.fromName = fname;
		  this.fromID = fid;
		  this.message = msg;
		  this.userList=list;
	  }
	  
	  /**
	   * getter for Type
	   * @return int Type
	   */
	  public int getType(){
		  return this.type;
	  }
	  
	  /**
	   * getter for fromName
	   * @return String userName of sender
	   */
	  public String getfromName(){
		  return this.fromName;
	  }
	  
	  /**
	   * getter for toNameIDString
	   * @return String NameIDString of receiver
	   */
	  public String gettoNameIDString(){
		  return this.toNameIDString;
	  }

	  /**
	   * getter for fromID
	   * @return int senderID
	   */
	  public int getfromID(){
		  return this.fromID;
	  }
		
	  /**
	   * getter for toIndex
	   * @return int receiverIndex (normally in the userListDisplay)
	   */
	  public int gettoIndex(){
		  return this.toIndex;
	  }
	  
	  /**
	   * getter for message
	   * @return String message
	   */
	  public String getMessage(){
		  return this.message;
	  }
	  
	  /**
	   * getter for userList
	   * @return LinkedList userList (list of users online)
	   */
	  public LinkedList<User> getUserList(){
		  return this.userList;
	  }
	  
}
