Lambda 表达式 又称为 闭包，它是推动 Java 8 发布的最重要新特性。
Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）；
使用 Lambda 表达式可以使代码变得更加简洁紧凑。

* 语法：

  ```java
  (parameters) -> expression
  或
  (parameters) ->{ statements; }
  ```

### 特性

以下是 Lambda 表达式的重要特性：

* **可选类型声明：**不需要声明参数类型，编译器可以统一识别参数值。
* **可选的参数圆括号：**一个参数无需定义圆括号，但多个参数需要定义圆括号。
* **可选的大括号：**如果主体包含了一个语句，就不需要使用大括号。
* **可选的返回关键字：**如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定表达式返回了一个数值。

### 例子

Lambda 表达式的简单例子

```java
// 1、不需要参数、返回值为 5
() > 5
    
// 2、接收一个参数（数字类型），返回其 2 倍的值
x -> 2 * x

// 3、接收2个参数（数字），并返回他们的差值
(x,y) -> x - y

// 4、接受2个int型整数，返回他们的和
(int x, int y) -> x + y

// 5、接受一个 String 对象，并在控制台上打印，不返回任何值
(String str) -> System.out.print(str);
```

```java
public class Hello{
	public static void main(String args[]){
		Hello hello = new Hello();
		A a1 = (a,b) -> a + b;
		A a2 = (a,b) -> a - b;
		A a3 = (int a,int b) -> {return a * b;};
		A a4 = (int a,int b) -> a / b;
		System.out.println("10 + 5 = " + hello.operate(10, 5, a1));
		System.out.println("10 - 5 = " + hello.operate(10, 5, a2));
		System.out.println("10 x 5 = " + hello.operate(10, 5, a3));
		System.out.println("10 / 5 = " + hello.operate(10, 5, a4));
        // 不用括号
      	GreetingService greetService1 = message ->
        System.out.println("Hello " + message);
        
        // 用括号
        GreetingService greetService2 = (message) ->
        System.out.println("Hello " + message);
        
        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");
	}
	
	private int operate(int a,int b,A aClass){
		return aClass.a(a,b);
	}
	
	interface A{
		int a(int a,int b);
	}
	
    interface GreetingService {
      void sayMessage(String message);
    }
	
}
```

### 注意

使用 Lambda 表达式需要注意以下两点：

* Lambda 表达式主要用来定义行内执行的方法类型的接口，例如，一个简单的方法接口。上面的例子，我们可以使用 Lambda 表达式来定义一个接口 内部单个方法的 各种实现方式。然后定义了 sayMessage 执行。
* Lambda 表达式避免了使用匿名方法的麻烦，并且给予 java 简单但强大的函数化的编程能力。

### 变量作用域

lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误。

```java
public class java8Tester{
    final static String salutation = "Hello! ";
    public static void main(String args[]){
      GreetingService greetService1 = message -> 
      System.out.println(salutation + message);
      greetService1.sayMessage("Runoob");
    }
    
    interface GreetingService {
      void sayMessage(String message);
   }
}
```

我们也可以直接在 lambda 表达式中访问外层的局部变量：

```java
public class Java8Tester {
    public static void main(String args[]) {
        final int num = 1;
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
        s.convert(2);  // 输出结果为 3
    }
 
    public interface Converter<T1, T2> {
        void convert(int i);
    }
}
```

lambda 表达式的局部变量可以不必声明没 final，但是必须不能被后面的代码修改（即隐性的具有 final 语义）

```java
int num = 1;  
Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
s.convert(2);
num = 5;  
//报错信息：Local variable num defined in an enclosing scope must be final or effectively 
 final
```

在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量。

```java
String first = "";  
Comparator<String> comparator = (first, second) -> Integer.compare(first.length(), second.length());  //编译会出错 
```



