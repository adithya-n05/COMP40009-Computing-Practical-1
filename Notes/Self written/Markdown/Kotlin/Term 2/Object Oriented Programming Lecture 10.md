# Lecture 4: Linked lists in Kotlin 04-12-2023

- [Lecture 4: Linked lists in Kotlin 04-12-2023](#lecture-4-linked-lists-in-kotlin-04-12-2023)
  - [Strings and manipulation](#strings-and-manipulation)


## Strings and manipulation

- Strings can't change once they are made to be a certain variable
- Checking if two objects are referentially equal to one another can be done using `===`

```kotlin
val s1 = "Hello"
val s2 = "Hello"
val s3 = "Hell"
val s4 = "o"
val s5 = s3 + s4

println(s1 === s2)
println(s1 == s2)
```

- The `===` checks if two values are referentially the same, i.e. they point to the same pointer

- Data classes are immutable


- Copy function - The copy function allows for you to copy a value while changing a certain value, which makes it very useful on data classes as they are immutable

```kotlin
fun main(){
  println(Point(5, 6).add(Point(10, 20)))
}
```

- We can rewrite the above using the `+` symbol, making it into an operator

```kotlin
data class Point(val x: Int, val y: Int) {
  operator fun plus(other: Point): Point = Point(this.x + other.x, this.y + other.y)
}
```

- Then, we can rewrite the function under main as follows:


```kotlin
println(Point(5, 6) + (Point(10, 20)))
```

- We can also do this for times

```kotlin
data class Point(val x: Int, val y: Int) {
  operator fun plus(other: Point): Point = Point(this.x + other.x, this.y + other.y)

  operator fun times(other: Point): Point = Point(this.x * other.x, this.y * other.y)
}
```

- Which allows us to do

```kotlin
println(Point(5, 6) * (Point(10, 20)))
```

- We can overload the plus method in the class to be able to add a scalar to a point as follows:

```kotlin
data class Point(val x: Int, val y: Int) {
  operator fun plus(other: Point): Point = Point(this.x + other.x, this.y + other.y)

  operator fun plus(other: Int): Point = Point(this.x + other, this.y + other)

  operator fun times(other: Point): Point = Point(this.x * other.x, this.y * other.y)
}
```

- Which allows us to do:


```kotlin
println(Point(5, 6) + 300)
```

- However, we cannot do the following, where the int is the first argument and the point is the second
  - This assumes commutativity which may not be the way we would want the operator to function
  - Hence we cannot do the following:

```kotlin
println(300 + Point(5,6))
```

- We can write a function to get the values out of a point by writing a get function

```kotlin
operator fun get(index: Int): Int =
  when (index) {
    0 -> first
    1 -> second
    else -> throw IndexOutOfBoundsException()
  }
```

- The get method overloads the `[]` operator
  - This allows us to do things like `val x = p[0]`

```kotlin
operator fun set(index: Int, value: Int): Int =
  when (index) {
    0 -> first = value
    1 -> second = value
    else -> throw IndexOutOfBoundsException()
  }
```

- The set operator allows us to be able to do things such as `p[0] = 10`
  - i.e.e to write to an object at a given index