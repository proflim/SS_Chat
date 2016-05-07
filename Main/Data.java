package Main;

import java.util.LinkedList;

public class Data implements java.io.Serializable {

	  /*
	   * Type
	   * 0: normal communication (client -> server, server-> client)
	   * 1: update username (client -> server)
	   * 2: assign unique userID (server -> client)
	   * 3: terminate connection (separately handled)
	   * 4: announce new user (server -> client) 
	   * 5: announce disconnection (server -> client)
	   */
	
	  private int type=-1;
	  private String fromName="null";
	  private String toNameIDString="null";
	  private int fromID=-1;
	  private int toIndex=-1;	  
	  private String message="void";
	  private LinkedList<User> userList=null;

	  //Basic constructor
	  //Basic Communication (Client -> Server -> Client)
	  public Data(int t, String fname, String tnameidstr, int fid, int tidx, String msg) {
		  this.type = t;
		  this.fromName = fname;
		  this.toNameIDString = tnameidstr;
		  this.fromID = fid;
		  this.toIndex = tidx;
		  this.message = msg;		  
	  }
	  
	  // Send initial Username (Client -> Server)
	  public Data(int t, String fname){
		  this.type = t;
		  this.fromName = fname;
	  }
	  
	  // Update UserName (Client -> Server)
	  //Update UserList (Server -> Client)
	  public Data(int t, String fname, int fid){
		  this.type = t;
		  this.fromName = fname;
		  this.fromID = fid;
	  }
	  
	  
	  //Assign uniqueID (Server -> Client)
	  public Data(int t, int fid, LinkedList<User> list){
		  this.type = t;
		  this.fromID = fid;
		  this.userList = list;
	  }
	  
	  //Quit connection (Type = 3)
	  public Data(int t){
		  this.type=t;
	  }
	  
	  //Announce new connection/disconnection
	  public Data(int t, String fname, int fid, String msg, LinkedList<User> list){
		  this.type = t;
		  this.fromName = fname;
		  this.fromID = fid;
		  this.message = msg;
		  this.userList=list;
	  }
	  
	  
	  public int getType(){
		  return this.type;
	  }
	  
	  public String getfromName(){
		  return this.fromName;
	  }
	  
	  public String gettoNameIDString(){
		  return this.toNameIDString;
	  }

	  public int getfromID(){
		  return this.fromID;
	  }
		
	  public int gettoIndex(){
		  return this.toIndex;
	  }
	  
	  public String getMessage(){
		  return this.message;
	  }
	  
	  public LinkedList<User> getUserList(){
		  return this.userList;
	  }
	  
}
