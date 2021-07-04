package scalaSynx


// https://blog.csdn.net/weixin_42181200/article/details/80324801?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-2.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-2.control
class Person3 {}
class Student3 extends Person3
object Student3 {
  def main(args: Array[String]) {
    // p 实例化了子类的对象，但是将其赋予了父类类型的变量
    val p: Person3 = new Student3
    var s: Student3 = null
    //如果对象是 null，则 isInstanceOf 一定返回 false
    println(s.isInstanceOf[Student3])
    // 判断 p 是否为 Student3 对象的实例
    if (p.isInstanceOf[Student3]) {
      //把 p 转换成 Student3 对象的实例
      // 将父类类型的变量转换为子类类型的变量
      s = p.asInstanceOf[Student3]
    }
    println(s.isInstanceOf[Student3])
  }
}

