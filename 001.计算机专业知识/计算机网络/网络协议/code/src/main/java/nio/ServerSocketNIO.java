package nio;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerSocketNIO {
    //保存客户端连接
    static List<SocketChannel> channelList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        //设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);
        System.out.println("服务器启动成功");

        while (true){
            //非阻塞模式下的accept()方法不会阻塞,否则会阻塞
            //NIO的非阻塞是由操作系统内部实现的,调用的是操作系统内核的accept函数
            SocketChannel clientSocketChannel = serverSocketChannel.accept();
            if (clientSocketChannel != null){
                System.out.println(clientSocketChannel.getRemoteAddress() + "连接成功");
                // 设置clientSocketChannel为非阻塞
                clientSocketChannel.configureBlocking(false);
                channelList.add(clientSocketChannel);
            }

            //遍历连接进行数据读取
            Iterator<SocketChannel> iterator = channelList.iterator();
            while (iterator.hasNext()){
                SocketChannel sc = iterator.next();
                SocketAddress remoteAddress = sc.getRemoteAddress();
                ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                //非阻塞模式的read不会阻塞,否则会阻塞
                int len = -1;
                try {
                    len = sc.read(byteBuffer);
                }catch (Exception e){
                    System.out.println("客户端异常!!! >> "+remoteAddress);
                }
                if (len > 0){
                    System.out.println("接收到消息 from："+ remoteAddress + "：" + new String(byteBuffer.array(),0,len));
                }else if (len == -1){ //如果客户端断开连接, 把Socket从集合中去除
                    sc.close();
                    iterator.remove();
                    System.out.println("主机" + remoteAddress + "断开连接");
                }
            }
        }
    }
}
