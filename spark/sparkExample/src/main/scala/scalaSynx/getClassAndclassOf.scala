package scalaSynx

// isInstanceOf 只能判断出对象是否为指定类以及其子类的对象，而不能精确的判断出，对象就是指定类的对象
// p.getClass 可以精确地获取对象的类，classOf[XX]可以精确的获取类，然后使用 == 操作符即可判断；

class Person4 {}
class Student4 extends Person4
object Student4 {
  def main(args: Array[String]) {
    val p: Person4 = new Student4
    //判断p是否为Person4类的实例
    println(p.isInstanceOf[Person4]) //true
    //判断p的类型是否为Person4类
    println(p.getClass == classOf[Person4]) //false
    //判断p的类型是否为Student4类
    println(p.getClass == classOf[Student4]) //true
  }
}