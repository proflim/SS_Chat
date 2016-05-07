package Main;

import java.util.LinkedList;

public class Data implements java.io.Serializable {

	  /*
	   * Type
	   * 0: normal communication
	   * 1: update username (client -> server)
	   * 2: assign unique userID (server -> client)
	   * 3: terminate connection
	   * 4: announce new user (server -> client) 
	   * 5: announce disconnection (server -> client)
	   */
	
	  private int type=-1;
	  private String fromName="null";
	  private String toName="null";
	  private int fromID=-1;
	  private int toID=-1;	  
	  private String message="void";
	  private LinkedList<User> userList=null;

	  //Basic constructor
	  //Basic Communication (Client -> Server -> Client)
	  public Data(int t, String fname, String tname, int fid, int tid, String msg, LinkedList<User> list) {
		  this.type = t;
		  this.fromName = fname;
		  this.toName = tname;
		  this.fromID = fid;
		  this.toID = tid;
		  this.message = msg;
		  this.userList = list;
	  }
	  
	  //Notify/Update Username (Client -> Server)
	  public Data(int t, String fname){
		  this.type = t;
		  this.fromName = fname;
	  }
	  
	  //Assign uniqueID (Server -> Client)
	  public Data(int t, int tid, LinkedList<User> list){
		  this.type = t;
		  this.toID = tid;
		  this.userList = list;
	  }
	  
	  //Quit connection (Type = 3)
	  public Data(int t){
		  this.type=t;
	  }
	  
	  //Announce new connection/disconnection
	  public Data(int t, int fid, String msg, LinkedList<User> list){
		  this.type = t;
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
	  
	  public String gettoName(){
		  return this.toName;
	  }

	  public int getfromID(){
		  return this.fromID;
	  }
		
	  public int gettoID(){
		  return this.toID;
	  }
	  
	  public String getMessage(){
		  return this.message;
	  }
	  
	  public LinkedList<User> getUserList(){
		  return this.userList;
	  }
	  
}
