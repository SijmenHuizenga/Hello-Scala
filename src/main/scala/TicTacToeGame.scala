import scala.io.StdIn

/**
  * Command Line Tic Tac Toe game!
  * Created by Sijmen on 26-3-2017.
  */
object TicTacToeGame {

  //row;col
  private var board : Array[Array[Player.Value]] = _
  private var nextPlayer: Player.Value = _

  def main(args: Array[String]): Unit = {
    initGame()
    while(true){
      turnPlayer()

      do{
        printNextTurn()
      }while(!tryDoMove(StdIn.readLine()))

      printBoard()
      if(isGameWon)
        return printWinner()
      if(isGameFinished)
        return printTie()
    }

  }

  def initGame() {
    board = Array.fill[Player.Value](3, 3)(Player.EMPTY)
    nextPlayer = Player.O
  }

  def isGameWon : Boolean = {
    areEqualAndNotNull(board(0)(0), board(0)(1), board(0)(2)) ||
    areEqualAndNotNull(board(1)(0), board(1)(1), board(1)(2)) ||
    areEqualAndNotNull(board(2)(0), board(2)(1), board(2)(2)) ||
    areEqualAndNotNull(board(0)(0), board(1)(0), board(2)(0)) ||
    areEqualAndNotNull(board(0)(1), board(1)(1), board(2)(1)) ||
    areEqualAndNotNull(board(0)(2), board(1)(2), board(2)(2)) ||
    areEqualAndNotNull(board(0)(0), board(1)(1), board(2)(2)) ||
    areEqualAndNotNull(board(0)(2), board(1)(1), board(2)(0))
  }

  def isGameFinished : Boolean = {
      board.forall(row => !row.contains(Player.EMPTY))
  }

  private def areEqualAndNotNull(array : Player.Value*): Boolean = {
    if(array(0) == Player.EMPTY)
      return false
    array.forall(x => x.equals(array(0)))
  }

  def tryDoMove(move: String) : Boolean = {
    val input : Array[String] = move.split(';')
    if(input.length != 2)
      return false
    val row = input(0).toInt
    val col = input(1).toInt
    if(row < 0 || col < 0 || row > 2 || col > 2)
      return false
    if(board(row)(col) != Player.EMPTY)
      return false
    board(row)(col) = nextPlayer
    true
  }

  private def turnPlayer() = {
    if (nextPlayer == Player.X)
      nextPlayer = Player.O
    else
      nextPlayer = Player.X
  }

  def printBoard() {
    board.foreach(f => {
      f.foreach(g => print(s"[$g]"))
      println()
    })
  }

  def printWinner() {
    println(s"Winner is $nextPlayer! Congratiulations!")
  }

  def printNextTurn() {
    println(s"Next turn: $nextPlayer (give input in format row;col)")
  }

  def printTie() {
    println("No winners. Sorry!")
  }

  object Player extends Enumeration {
    val X = Value("X")
    val O = Value("O")
    val EMPTY = Value(" ")
  }

}
