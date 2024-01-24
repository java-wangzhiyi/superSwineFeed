# 1、UML

**英译**
(Unified Modeling Language)

**什么是UML**
1、是用来设计软件的可视化建模语言，又称统一建模语言或标准建模语言。
2、UML从目标系统的不同角度出发，定义了【用例图、类图、对象图、状态图、活动图、时序图、协作图、构件图、部署图】等9种图。

**UML的作用**
为了简单、统一，以图形化的方式表达软件设计中的动态与静态信息。
通过使用 UML 的面向对象图的方式，可以更明确、清晰的表达项目中的架设思想、项目结构、执行顺序等一些逻辑思维。



# 2、类图

**英译**
(Class Diagram)

**什么是类图**
类图是显示了模型的静态结构，特别是模型中存在的类、类的内部结构以及它们与其他类的关系等。类图不显示暂时性的信息。类图是面向对象建模的主要组成部分。

**类图的作用**
1、软件工程中，类图是一种静态的结构图，描述了系统的类的集合，类的属性和类之间的关系，可以简化了人们对系统的理解；
2、类图是系统分析和设计阶段的重要产物，是系统编码和测试的重要模型。

**类图中类的表示方式**
在UML类图中，类使用包含类名、属性(field) 和方法(method) 且带有分割线的矩形来表示，比如下图表示一个Employee类，它包含name,age和address这3个属性，以及work()方法。
![img](PlantUML.assets/ffff97fd01b34c5d8f1e388de61cf2ba.png)

属性/方法名称前加的加号和减号表示了这个属性/方法的可见性，UML类图中表示可见性的符号有三种：

* +：表示public
* -：表示private
* ：表示protected

属性的完整表示方式是： **可见性 名称 ：类型 [ = 缺省值]**
方法的完整表示方式是： **可见性 名称(参数列表) [ ： 返回类型]**

>  1，中括号中的内容表示是可选的
>  2，也有将类型放在变量名前面，返回值类型放在方法名前面

![img](PlantUML.assets/3ce9406d9b0a42d69c240012e034e66a.png)

上图Demo类定义了三个方法：
method()方法：修饰符为public，没有参数，没有返回值。
method1()方法：修饰符为private，没有参数，返回值类型为String。
method2()方法：修饰符为protected，接收两个参数，第一个参数类型为int，第二个参数类型为String，返回值类型是int。



# 3、类图-关联关系

关联关系是对象之间的一种引用关系，用于表示一类对象与另一类对象之间的联系，如老师和学生、师傅和徒弟、丈夫和妻子等。关联关系是类与类之间最常用的一种关系，分为一般关联关系、聚合关系和组合关系。

## 1、单向关联

![img](PlantUML.assets/1a44d417a5de441aafa6098938b3848a.png)

在UML类图中单向关联用一个带箭头的实线表示。上图表示每个顾客都有一个地址，这通过让Customer类持有一个类型为Address的成员变量类实现。

## 2、双向关联

![img](PlantUML.assets/4a410f38494c4695b3e359bc836252af.png)

从上图中我们很容易看出，所谓的双向关联就是双方各自持有对方类型的成员变量。

在UML类图中，双向关联用一个不带箭头的直线表示。上图中在Customer类中维护一个List\<Product>，表示一个顾客可以购买多个商品；在Product类中维护一个Customer类型的成员变量表示这个产品被哪个顾客所购买。

## 3、自关联

![img](PlantUML.assets/08312305d6514eaab76eab94689da794.png)

自关联在UML类图中用一个带有箭头且指向自身的线表示。上图的意思就是Node类包含类型为Node的成员变量，也就是“自己包含自己”。
如其成员变量：private List\<Node> subNode;



# 4、类图-聚合关系

聚合关系是关联关系的一种，是强关联关系，是整体和部分之间的关系。

聚合关系也是通过成员对象来实现的，其中成员对象是整体对象的一部分，但是成员对象可以脱离整体对象而独立存在。例如，学校与老师的关系，学校包含老师，但如果学校停办了，老师依然存在。

在 UML 类图中，聚合关系可以用带空心菱形的实线来表示，菱形指向整体。下图所示是大学和教师的关系图：

![img](PlantUML.assets/bd4fa73abae1444194da3005d3f0f7b9.png)



# 5、类图-组合关系

组合表示类之间的整体与部分的关系，但它是一种更强烈的聚合关系。

在组合关系中，整体对象可以控制部分对象的生命周期，一旦整体对象不存在，部分对象也将不存在，部分对象不能脱离整体对象而存在。例如，头和嘴的关系，没有了头，嘴也就不存在了。

在 UML 类图中，组合关系用带实心菱形的实线来表示，菱形指向整体。下图所示是头和嘴的关系图：

![img](PlantUML.assets/7a35f9a3fdaf41429175bf40941e1900.png)



# 6、类图-依赖关系

依赖关系是一种使用关系，它是对象之间耦合度最弱的一种关联方式，是临时性的关联。在代码中，某个类的方法通过局部变量、方法的参数或者对静态方法的调用来访问另一个类（被依赖类）中的某些方法来完成一些职责。

在 UML 类图中，依赖关系使用带箭头的虚线来表示，箭头从使用类指向被依赖的类。下图所示是司机和汽车的关系图，司机驾驶汽车：

![img](PlantUML.assets/d065e9a27b8e4946982a587614d7bceb.png)



# 7、类图-继承关系

继承关系是对象之间耦合度最大的一种关系，表示一般与特殊的关系，是父类与子类之间的关系，是一种继承关系。

在 UML 类图中，泛化关系用带空心三角箭头的实线来表示，箭头从子类指向父类。在代码实现时，使用面向对象的继承机制来实现泛化关系。例如，Student 类和 Teacher 类都是 Person 类的子类，其类图如下图所示：

![img](PlantUML.assets/e25c22a6970b4e71a192dced7be60710.png)



# 8、类图-实现关系

实现关系是接口与实现类之间的关系。在这种关系中，类实现了接口，类中的操作实现了接口中所声明的所有的抽象操作。

在 UML 类图中，实现关系使用带空心三角箭头的虚线来表示，箭头从实现类指向接口。例如，汽车和船实现了交通工具，其类图如图所示。

![img](PlantUML.assets/aa8358dd2b50478caba192aa1991f80e.png)



# 9、plantUML

**简介**
plantUML是一个开源绘图工具，一个用来绘制UML图的Java类库，支持通过文本来生成图形。
plantUML是一种UML建模语言，可以让我们以写代码的方式画出UML建模图，同时使用文本也方便版本控制。
plantUML具备相对完善的中文支持网站：https://plantuml.com/zh/class-diagram

**插件**
在插件市场搜索plantUML并安装即可。

**创建puml文件**
在IDEA中，插件安装完毕后，右键即可新建puml文件。

![image-20230306093231163](PlantUML.assets/image-20230306093231163.png)

**使用**
puml类图语法以 @startuml开始、@enduml结束，中间穿插其自身语法，常用语法如下。



# 10、puml-关联关系

## 1、单向关联

```puml
@startuml
'https://plantuml.com/class-diagram
class Customer{
    - address: Address
}
class Address


'单向关联
note top of Customer: * 单向关联\nUML类图中单向关联用一个带箭头的实线表示
note right of Customer::"address: Address"
    每个顾客都有一个地址，这通过让Customer类持
    有一个类型为Address的成员变量类实现
end note
Customer --> Address

@enduml
```

<img src="PlantUML.assets/image-20230306114124344.png" alt="image-20230306114124344" style="zoom:67%;" />

## 2、双向关联

```puml
@startuml
'https://plantuml.com/class-diagram

class Customer{
    - products: List<Product>
}
class Product{
    - customers: List<Customer>
}

note top of Customer: * 双向关联\nUML类图中，双向关联用一个不带箭头的直线表示
note left of Customer::"products: List<Product>"
    Customer类中维护一个List<Product>
    表示一个顾客可以购买多个商品
end note

note left of Product::"customers: List<Customer>"
    在Product类中维护一个Customer类型的成员变量
    表示这个产品被哪个顾客所购买
end note

Customer -- Product

@enduml
```

<img src="PlantUML.assets/image-20230306115238871.png" alt="image-20230306115238871" style="zoom:67%;" />

## 3、自关联

```puml
@startuml
'https://plantuml.com/class-diagram

class Node{
    - subNode: Node
}

note top of Node
    * 自关联
    在UML类图中用一个带有箭头且指向自身的线表示
end note

note left of Node::"subNode: Node"
    Node类包含类型为Node的成员变量
    也就是“自己包含自己”
end note


Node --> Node

@enduml
```

<img src="PlantUML.assets/image-20230306120407400.png" alt="image-20230306120407400" style="zoom:67%;" />



# 11、puml-聚合关系 

```puml
@startuml
class University{
    - teas: List<Teacher>
}

class Teacher{
    - name: String
}

note top of University
    <b>聚合关系</b>
     UML 类图中，聚合关系可以用带空心菱形的实线来表示
     菱形指向整体
end note

note left of University::"teas: List<Teacher>"
    * 成员对象是整体对象的一部分
end note

note right of University::"teas: List<Teacher>"
    学校包含老师
end note

note left of Teacher
    * 但是成员对象可以脱离整体对象而独立存在
end note

note right of Teacher
    但如果学校停办了，老师依然存在
end note

University o-- Teacher

@enduml
```

<img src="PlantUML.assets/image-20230306121957491.png" alt="image-20230306121957491" style="zoom:67%;" />



# 12、puml-组合关系

```puml
@startuml
class Head{
    - mouth: Mouth
}
class Mouth{
    + eat(): void
}

Head *-- Mouth

note top of Head
    <b>组合关系</b>
    UML 类图中，组合关系用带实心菱形的实线来表示，菱形指向整体
end note

note left of Head::"mouth: Mouth"
    头包含嘴巴
end note

note left of Mouth::"eat(): void"
    嘴巴能吃东西
end note

note right of Head
    但是若头没了
end note

note right of Mouth
    嘴巴也就不存在了
end note

@enduml
```

<img src="PlantUML.assets/image-20230306122802037.png" alt="image-20230306122802037" style="zoom:67%;" />



# 13、puml-依赖关系

```puml
@startuml
class Driver{
    - name: String
    + drive(Car car): void
}

class Car{
    + move(): void
}

Driver ..> Car

note top of Driver
    <b>依赖关系</b>
    UML 类图中，依赖关系使用带箭头的虚线来表示，箭头从使用类指向被依赖的类
end note

note left of Driver::"drive(Car car): void"
    public void drive(Car car){
        car.move();//汽车移动
    }
end note
@enduml
```

<img src="PlantUML.assets/image-20230306131731878.png" alt="image-20230306131731878" style="zoom:67%;" />



# 14、puml-泛化关系

```puml
@startuml
'https://plantuml.com/class-diagram

class Person{
    - name: String
    - age: int
    + speak(): void
}
class Student{
    - studentNo: String
    + study(): void
}
class Teacher{
    - teacherNo: String
    + teach(): void
}

Person <|-- Student
Person <|-- Teacher

note top of Person
    <b>泛化关系</b>
    UML 类图中,泛化关系用带空心三角箭头的实线来表示,箭头从子类指向父类.
    在代码实现时，使用面向对象的继承机制来实现泛化关系
end note

note top of Student
    Student类继承自Person类
end note

note top of Teacher
    Student类继承自Person类
end note

@enduml
```

<img src="PlantUML.assets/image-20230306132349279.png" alt="image-20230306132349279" style="zoom:67%;" />



# 15、puml-实现关系

```puml
@startuml
'https://plantuml.com/class-diagram

interface Vehicle{
    + move(): void
}

class Car{
    + move(): void
}

class Ship{
    + move(): void
}

Vehicle <|.. Car
Vehicle <|.. Ship

note top of Vehicle
    <b>实现关系</b>
    UML 类图中，实现关系使用带空心三角箭头的虚线来表示，箭头从实现类指向接口。
end note

note top of Car
    汽车类实现了交通工具类
end note

note top of Ship
    轮船类实现了交通工具类
end note

@enduml
```

<img src="PlantUML.assets/image-20230306132856161.png" alt="image-20230306132856161" style="zoom:67%;" />





