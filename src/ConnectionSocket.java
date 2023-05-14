import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionSocket {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ConnectionSocket(String serverAdd, int serverPort){
        try{
            this.socket = new Socket(serverAdd, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectionSocket(Socket socket) {
        try{
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String request) throws IOException{
        System.out.println("Send " + request);
        out.writeUTF(request);
    }

    public String receive() throws IOException {
        String receive = in.readUTF();
        System.out.println("Receive " + receive);
        return receive;
    }
}
