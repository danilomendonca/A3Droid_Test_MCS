package it.polimi.mediasharing.sockets;

import it.polimi.mediasharing.a3.roles.ReceiverSupervisorRole;
import it.polimi.mediasharing.activities.MainActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import a3.a3droid.A3Message;

public class Server extends Thread{
	
	ReceiverSupervisorRole role;
	int port;
	Socket socket;
	
	public Server(int port, ReceiverSupervisorRole role) throws IOException {
		this.role = role;
		this.port = port;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			this.createFileServer(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopServer(){
		if(!socket.isClosed())
			try {
				this.socket.close();
			} catch (IOException e) {
				System.out.println("Socket was already closed");
			}
		this.interrupt();
	}
	
    public void createFileServer(int port) throws IOException {         	    	
    	ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }
        
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        ByteArrayOutputStream baos = null;
        byte[] buffer = new byte[8192];

        int count = 0;
        while(count >= -1){
	        try {
	            socket = serverSocket.accept();
	        } catch (IOException ex) {
	            System.out.println("Can't accept client connection. ");
	        }
	        try {
	            bis = new BufferedInputStream(socket.getInputStream());
	            dis = new DataInputStream(bis);
	            baos = new ByteArrayOutputStream();
	        } catch (IOException ex) {
	            System.out.println("Can't get socket input stream. ");
	        }
	        
	        try{
		        int reason = dis.readInt();
		        switch (reason) {
				case MainActivity.RFS:
					A3Message rfs = new A3Message(MainActivity.RFS, 
							socket.getLocalAddress().getHostAddress() + "#" +
							socket.getRemoteSocketAddress());
			        role.receiveApplicationMessage(rfs);
					break;
				case MainActivity.MC:
					while ((count = bis.read(buffer)) > 0) {
			        	baos.write(buffer, 0, count);
			        }		        
			        A3Message content = new A3Message(MainActivity.MEDIA_DATA, 
			        		socket.getRemoteSocketAddress().toString(), baos.toByteArray());
			        role.receiveApplicationMessage(content);
			        break;
				default:
					break;
				}
	        }catch(IOException ex){
	        	System.out.println("Error parsing the remote message. ");
	        }
	        
        }
        bis.close();
        socket.close();
        serverSocket.close();
    }
}