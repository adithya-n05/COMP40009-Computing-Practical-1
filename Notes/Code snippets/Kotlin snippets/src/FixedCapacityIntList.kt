package collections

class FixedCapacityIntList(capacity: Int) {
    var size: Int = 0
        private set

    // The above means that setting the property is private and only code within the class can set the value

    val elements =
        if (capacity < 0) {
            throw IllegalArgumentException("List capacity cannot be negative")
        } else {
            Array(capacity) { -1 }
        }


    fun add(index: Int, element: Int) {
        if (size >= elements.size || index !in 0..size){
            throw IndexOutOfBoundsException()
        }w
        for (i in size downTo index + 1) {
            elements [i] = elements [i - 1]
        }
        elements[index] = element
        size++
    }

    override fun toString(): String = elements.slice(0..<size).joinToString(prefix = "[", postfix = "]")
}

fun main() {
    val myList = FixedCapacityIntList(10)
    println(myList)
}
