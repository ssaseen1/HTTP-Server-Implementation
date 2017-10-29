import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

public class ConnectionHandler implements Runnable {

	Socket sock = null;

	public ConnectionHandler(Socket s) {
		// TODO Auto-generated constructor stub
		sock = s;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {

			OutputStream os = sock.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);

			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			String str;
			str = in.readLine();
			/*
			 * while(str!= null){ System.out.println(str); str = in.readLine();
			 * }
			 */
			//System.out.println(str);
			// String path = null;
			String filename = null;
			filename = str.substring(4, str.length() - 9);
			if (filename.length() == 1 && filename.contains("/")) {
				String s = "<html><h1> File is not Requested </h1></html>";
				String error = "HTTP/1.1 404\n\n";

				// String errorMessage = "HTTP/1.1 404 Not Found\n\n";
				os.write(error.getBytes("UTF-8"));
				os.write(s.getBytes("UTF-8"));
				in.close();
				bw.close();
				osw.close();
				os.close();
				sock.close();
			}

			else {
				File d = null;
				int count = 0;
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) == '/')
						count++;

				}

				if (count > 1) {
					if (filename.contains("%20"))
						filename = filename.replaceAll("\\%20", " ");

					d = new File("www" + filename);
				} else {

					//System.out.println(filename);
					d = new File("www" + filename);
				}
				 //System.out.println("File name "+filename);

				String fileExe = null;

				fileExe = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
				// System.out.println("File extension is "+fileExe);

				if (d.exists() && !d.isDirectory()) {
					Calendar c = Calendar.getInstance();
					Date date = new Date();

					SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
					sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
					sdf.format(c.getTime());

					String actualcontent = null;

					if (Server.filemap.containsKey(fileExe)) {
						actualcontent = Server.filemap.get(fileExe);
					}

					else
						actualcontent = new String("application/octet-stream");

					String httpR = "HTTP/1.1 200 OK\r\nDate: " + sdf.format(c.getTime())
							+ "\r\nServer: Rohan/PL-TA0.5\r\nLast-Modified: " + sdf.format(d.lastModified())
							+ "\r\nContent-Type: " + actualcontent + "\r\nContent-Length: " + d.length() + "\r\n\r\n";
					os.write(httpR.getBytes("UTF-8"));

					byte[] arr = new byte[(int) d.length()];
					FileInputStream f = new FileInputStream(d);
					BufferedInputStream bf = new BufferedInputStream(f);

					int readc = 0;
					while ((readc = bf.read(arr, 0, arr.length)) != -1) {
						os.write(arr, 0, arr.length);
						os.flush();
					}
					synchronized (this) {
						InetSocketAddress saddr = (InetSocketAddress) sock.getRemoteSocketAddress();
						InetAddress inaddr = saddr.getAddress();
						String clientip = inaddr.toString().substring(1);

						// System.out.println("content type "+actualcontent);

						System.out.print(filename + "|" + clientip + "|");
						if (Server.m.containsKey(filename)) {
							int count1 = Server.m.get(filename);
							count1++;
							Server.m.put(filename, count1);
							System.out.println(Server.m.get(filename));
						} else {
							Server.m.put(filename, 1);
							System.out.println(Server.m.get(filename));
						}
					}
				}
				else {
					String s = "<html><h1>The page was not found</h1></html>";
					String error = "HTTP/1.1 404\n\n";
					os.write(error.getBytes("UTF-8"));
					os.write(s.getBytes("UTF-8"));
				}
				os.close();
				bw.flush();
				bw.close();
				osw.close();
				os.close();
				sock.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Interrupted exception");
			System.exit(1);
		}
	}
}
