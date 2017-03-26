/**
 * Created by Sijmen on 24-3-2017.
 */

object classes {
  def main(args: Array[String]): Unit = {
    val s = new Person("Sijmen", "Huizenga")
    println(s)

    val s1 = new SimplePerson("Sijmen", "Huizenga")
    val s2 = new SimplePerson("Sijmen", "Huizenga", 20)
    println(s1)
    println(s2)

    var o = new ObjTester
    println(o.A())
    println(o.B())
    println(o.C())
  }
}

class Person(var firstName: String, var lastName: String) {
  override def toString: String = s"$firstName $lastName"
}

class Compass {
  val directions =List("north", "east", "south", "west")
  var bearing = 0

  print("Initial bearing: ")
  println(direction)

  def direction: String = directions(bearing)

  def inform(turnDirection: String) {
    println("Turning " + turnDirection + ". Now bearing " + direction)
  }

  def turnRight() {
    bearing = (bearing + 1) % directions.size
    inform("right")
  }

  def turnLeft() {
    bearing = (bearing + (directions.size - 1)) % directions.size
    inform("left")
  }
}

class SimplePerson(var first_name: String, val last_name: String, val age: Int) {

  def this(first_name: String, last_name: String) {
    this(first_name, last_name, -1)
  }

  def this(first_name: String) {
    this(first_name, null)
  }

  def this(person: Person) {
    this(person.firstName, person.lastName)
  }

  def talk(): Unit = println("Hi")

  override def toString: String = s"$first_name $last_name age:$age"
}

object TrueRing {
  def rule() : Int =  {
    println("To rule them all"); 10
  }
}

class ObjTester {
  def A() = 10 + 20
  def B() = {10 + 20}
  def C(): Unit = {val c = 10 + 20}
}

trait Cool {
   def greet() {println("Hey Man!")}
}
class Character(first_name: String)
  extends SimplePerson(first_name, "test") with Cool{
}