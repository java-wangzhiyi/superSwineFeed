package bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞IO
 */
public class SocketServerBIO {
    public static void main(String[] args) throws Exception{
        ServerSocket clientSocket = new ServerSocket(8000);
        while (true){
            Socket client = clientSocket.accept();
            System.out.println("有客户端连接了：" + client.getInetAddress().getHostAddress());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (handler(client) == 0){
                        System.out.println("Im connecting now");
                    }
                    try {
                        client.close();
                        System.out.println("客户端连接退出："+client.getInetAddress().getHostAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }

    private static int handler(Socket clientSocket){
        byte[] bytes = new byte[1024];
        System.out.println("准备read");
        try {
            int read = clientSocket.getInputStream().read(bytes);
            if (read != -1){
                System.out.println(new String(bytes, 0, read));
                return 0;
            }else {
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
