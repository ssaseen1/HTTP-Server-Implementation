import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Server {

	static Map<String, String> filemap = null;
	static Map<String, Integer> m = new HashMap<String, Integer>(); 
	public static void main(String[] args) {
		ServerSocket server = null;
		
		try {
			
			server = new ServerSocket(0);
			System.out.println(("Server host name :"+InetAddress.getLocalHost().getHostName())+"  Port :"+server.getLocalPort());
			
			File d = new File("www");
			if (d.isDirectory()) {
				//System.out.println("Directory Found");
				filemap = new HashMap<String, String>();
				
				BufferedReader b = new BufferedReader(new FileReader("/etc/mime.types"));
				String content;	
				String contentType[] = null;
				
				while ((content = b.readLine())!= null) {
					if(!content.startsWith("#") && !content.startsWith(" ")){
						contentType = content.split("\\s+");
						
						for(int i = 1; i < contentType.length; i++ ){
							
							filemap.put(contentType[i], contentType[0]);
						}
						
					}
				}	
				b.close();
				
				while(true){
				
					Socket soc = server.accept();
					
					Runnable connection = new ConnectionHandler(soc);
					Thread t = new Thread(connection);
					t.start();
						
				}	
			}
			else {
				System.out.println("ERROR ! Directory Not Found");
				System.exit(1);
			}
			
		server.close();	
		}catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Interrupted exception");
			System.exit(1);
		}	
	}
}
