/**
 * Created by Sijmen on 24-3-2017.
 */

val a = 2
val b = 2

if (b < a) {
  println("true")
} else {
  println("false")
}

def whileLoop(): Unit = {
  var i = 1
  println( "While Loop" )
  while(i <= 10) {
    println(i)
    i+=1
  }
}
whileLoop()

def forLoop(args: Range) {
  println( "for loop " )
  for(i <- 0 until args.length)
    println(args(i))
}
forNiceLoop(0 until 5)
def forNiceLoop(args: Range) {
  println( "for loop nice way" )
  args.foreach { arg =>
    println(arg)
  }
}
val range05 = 0 until 5
forNiceLoop(range05)

def forCoolLoop(args: Range) {
  println( "for loop even better way" )
  args.foreach { println(_) }
}
forNiceLoop(0 until 5)

val familie = ("ik", "broertje", "papa", "mama")
val c= familie._1

val (ik, broertje, papa, mama) = familie
println(ik);