package Main;

public class Data implements java.io.Serializable {

	  private String fromName;
	  private String toName;
	  private int fromID;
	  private int toID;
	  private int numUser=0; //used only for communication from server->client
	  private String message;

	  
	  //Client -> Server -> Client
	  //Default Data constructor for communication
	  //toID = 0 for sending to all user
	  public Data(String fname, String tname, int fid, int tid, String msg) {

	    this.fromName = fname;
	    this.toName = tname;
	    this.fromID = fid;
	    this.toID = tid;
	    this.message = msg;
	  }
	  
	  //Client -> Server -> Client
	  //Default Data constructor for communication
	  //toID = 0 for sending to all user
	  public Data(String fname, String tname, int fid, int tid, int num,String msg) {

	    this.fromName = fname;
	    this.toName = tname;
	    this.fromID = fid;
	    this.toID = tid;
	    this.numUser = num;
	    this.message = msg;
	  }
	  
	  //Client -> Server
	  //Data constructor when updating username
	  //For updating username (fromName -> toName) on the server
	  //toID=-1
	  public Data(String fname, String tname, int fid) {

		    this.fromName = fname;
		    this.toName = tname;
		    this.fromID = fid;
		    this.toID = -1;
		    this.message = "Updated username " + fname + " to " + tname+"\n";
	  }
	  
	  //Client -> Server
	  //When establishing connection
	  //Tells server the username of the client
	  //toID=-1
	  //fromID=-1
	  public Data(String fname) {

		    this.fromName = fname;
		    this.toName = "";
		    this.fromID = -1;
		    this.toID = -1;
		    this.message = "User " + fname + " is online.\n";
	  }
	  
	  //Server -> Client
	  //Data constructor when assigning unique IDs to clients
	  //The server sends unique ID to the client when connecting
	  //fromID = -1
	  public Data(int tid, String msg) {

		    this.fromName = "";
		    this.toName = "";
		    this.fromID = -1;
		    this.toID = tid;
		    this.message = msg;	//msg is the system level message
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
	  
}
