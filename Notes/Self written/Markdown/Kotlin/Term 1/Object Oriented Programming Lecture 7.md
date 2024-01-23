# Lecture 7: Introduction to Object Oriented Programming with Kotlin 04-12-2023

- [Lecture 7: Introduction to Object Oriented Programming with Kotlin 04-12-2023](#lecture-7-introduction-to-object-oriented-programming-with-kotlin-04-12-2023)
  - [Arrays in Kotlin](#arrays-in-kotlin)
    - [Creating a go board](#creating-a-go-board)
    - [Specialising with int arrays](#specialising-with-int-arrays)
  - [Reading data as an input](#reading-data-as-an-input)
  - [More loops](#more-loops)

## Arrays in Kotlin

- Here is an example of an array in Kotlin

```kotlin
fun main(){
  val a = arrayOf(1,2,3,4,5)

  println(a[3]) // This would print 4
}
```

- You must populate all the elements of an array in order to initialise it
- You can initialise an array with a function as follows:

```kotlin
val aa = Array(100) {i -> i + 1}
```

- We can print its outputs as follows:

```kotlin
println(aa[0]) // This would print 1
println(aa[1]) // This would print 2
println(aa[2]) // This would print 3
println(aa[3]) // This would print 4
```

- We can update individual values as follows:

```kotlin
aa[3] = 9
```

- We can print every element with:

```kotlin
aa.forEach(::println)
// You cannot add to an array with aa.add(...)
```

### Creating a go board

- Imagine we want to create a go board with cooardinates containing stones
- We can create an enumerated class for a stone as follows:

```
enum class Stone {BLACK, WHITE, NONE}
```

- We can write a class for coordinates as follows:

```kotlin
data class Coordinate(val x: Int, val y: Int)
```

- We can now create a go board as follows

```kotlin
class GoBoard {
  private val board: Array<Array<Stone>> = Array(19) {Array(19) {Stone.NONE}}

  fun playAt(coord: Coordinate, stone: Stone) {
    val (x, y) = coord
    board[x][y] = stone
  }
}
```

### Specialising with int arrays

- We can create faster arrays as follows, that only contain ints

```kotlin
fun specialisedExamples() {
  val ints: Array<Int> = arrayOf(1,2,3,4)
  val intArray = IntArray(4) {i -> i}
}
```

## Reading data as an input

- We can print data in a file as follows:

```kotlin
val out: PrintStream = PrintStream(FileOutputStream("data.txt"))

out.flush() // This prints out everything
out.close() // This closes the object that prints items out
```

- We can use input streams and buffered readers to read data

```kotlin
val input: Input Stream = FileInputStream("data.txt")

val reader = BufferedReader(InputStreamReader(input))

reader.readline()
```

## More loops

- Types of for loops:

```kotlin
for (i in 0..10) {
  println(i)
}

for (i in 0 until 10) { // This does not include 10
  println(i)
}

for (i in 0..10 step 2) { // This goes by the steps you select
  println(i)
}
```


- Types of while loops

```kotlin
while (x < y) {
  //do things
}

do { // This will do the do part first and repeat if the condition is met
  // do things
} while (x < y)
```