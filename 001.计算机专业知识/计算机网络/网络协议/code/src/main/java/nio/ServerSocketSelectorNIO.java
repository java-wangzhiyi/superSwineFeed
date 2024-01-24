package nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerSocketSelectorNIO {

    public static void main(String[] args) throws Exception{

        // 创建 NIO ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8000));
        // 设置 ServerSocketChannel 为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 打开多路复用器 epoll
        Selector selector = Selector.open();
        // 将 ServerSocketChannel 注册到 selector上, 并且 selector 监听客户端的 accept 操作
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");

        while (true){
            // 阻塞等待需要处理的事件发生
            selector.select();

            // 获取 selector 中注册的所有 SelectionKey 实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            // 遍历 selectionKeys 处理事件
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                // 如果是 OP_ACCEPT 事件, 则进行连接获取和事件注册
                if (key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功>>" +socketChannel.getRemoteAddress()+" <<");
                }else if (key.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = -1;
                    try {
                        len = socketChannel.read(byteBuffer);
                    }catch (Exception e){
                        System.out.println("客户端异常");
                    }
                    if (len > 0){
                        System.out.println("收到 "+ socketChannel.getRemoteAddress() +"消息 >>" + new String(byteBuffer.array(),0, len) + "<<");
                    }else if (len == -1){
                        System.out.println("客户端断开连接>>" +socketChannel.getRemoteAddress()+" <<");
                        socketChannel.close();
                    }
                }
                iterator.remove();
            }

        }
    }

}
