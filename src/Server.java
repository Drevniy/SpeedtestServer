import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		
		InetAddress serverIP = null;
		int serverPort = 0;
		
		for (int i = 0; i < args.length; i++) {
			if(args[i].matches(".+server-ip")){
				serverIP = InetAddress.getByName(args[i+1]);
			}else if(args[i].matches(".+server-port")){
				serverPort = Integer.parseInt(args[i+1]);
			}
		}
		
        ServerSocket server = new ServerSocket(serverPort,10,serverIP);
        while(true){
	        Socket socket = server.accept();
	        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String request = input.readLine();
	        
	        if(request.equals("download")){
	            Thread tDownloadSpeed =new Thread(new ServerDownloadSpeed(socket));  
	            tDownloadSpeed.start();
	        	
	        }else if(request.equals("upload")){
	            Thread tUploadSpeed =new Thread(new ServerUploadSpeed(socket));  
	            tUploadSpeed.start();
	        }
        }
	}
}

class ServerDownloadSpeed implements Runnable{
	OutputStream output;
	
	public ServerDownloadSpeed(Socket socket) throws IOException {
		 output = socket.getOutputStream();		 
	}
	  public void run(){  
	       byte[] bytes = new byte[32*1024];
	       try {
	       while(true) {	            
					output.write(bytes);				
	        }	       
	       } catch (IOException e) {				
	    	   }
	  } 
}

class ServerUploadSpeed implements Runnable{ 
	PrintWriter output;
	InputStream input;
	byte[] bytes;
	
	public ServerUploadSpeed(Socket socket) throws IOException {
		bytes = new byte[32*1024];
		input = socket.getInputStream();
		output = new PrintWriter(socket.getOutputStream(), true);
	}
	
	  public void run(){	       
	       	long total = 0;
	        long endTime = System.currentTimeMillis() + 5000;	        
	        try{
	        	
	        while(System.currentTimeMillis()< endTime){
	            int read = input.read(bytes);	            
	            total += read;
	        }
	        }catch (Exception e) {				
			}
	        
	        long uploadSpeedResult = total/10/8;
	        output.println(uploadSpeedResult); 
	        
	  } 
}
