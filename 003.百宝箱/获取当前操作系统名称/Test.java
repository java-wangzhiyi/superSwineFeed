package nio;

import com.sun.corba.se.impl.orbutil.GetPropertyAction;

import java.security.AccessController;

public class Test {
    public static void main(String[] args) {
        System.out.println(AccessController.doPrivileged(new GetPropertyAction("os.name")));
    }
}
