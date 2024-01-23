# Lecture 1: Introduction to Object Oriented Programming with Kotlin 04-12-2023

- [Lecture 1: Introduction to Object Oriented Programming with Kotlin 04-12-2023](#lecture-1-introduction-to-object-oriented-programming-with-kotlin-04-12-2023)
  - [Why are we using Kotlin](#why-are-we-using-kotlin)
  - [What about Java](#what-about-java)
  - [Course format](#course-format)
  - [Resources](#resources)
  - [Recap on Kotlin](#recap-on-kotlin)
    - [Goal of this lecture](#goal-of-this-lecture)
    - [Fixed capacity list of integers](#fixed-capacity-list-of-integers)
      - [Writing a class](#writing-a-class)
      - [Syntax of Kotlin](#syntax-of-kotlin)
    - [Resizing array list](#resizing-array-list)

## Why are we using Kotlin

- It is a really well designed language
- Designed by Java experts to keep many elegant features of Java
- Address a number of shortcomings of Java
- Maintain interoperability with Java
- Excellent IDE support and widely used in the Android industry

## What about Java

- Small amounts of Java will be covered later in the term
- Lays the groundwork for future courses that will make use of Java

## Course format

- First couple of weeks will be slide based
- All slots are lecture slots and are a mix of slides and interactive lie coading
- Exercises will be interspersed through the slides to do for yourself in your own time
  - Solutions to exercises will be provided in due course
- All content is examinable (including all exercises and all material in labs, excluding lab extensions)
- Interim and final test will both cover the same material
  - Interim and final test will examine the full course
- Example tests will be provided later in the term

## Resources

- Book: Kotlin in action - Dmitry Jemerov and Svetlana Isakova

## Recap on Kotlin

### Goal of this lecture

- Fixed capacity list classes
- Resizing array list and a singly linked list
- Unifying them using an interface
- Use the any type

### Fixed capacity list of integers

- Let's write a class representing a list of integers
- How will this be represented
  - Array elements of N integers, N is the capacity of the list
  - Integer size indicating which elements are part of the list
  - List contents: elements 0 ... (size - 1)

- If we remove an item from the list, the items of the list must move back down to fill the gap, and we won't reset any values that are empty
- If we add an item at a given index, the values will be bumped up
- If we want to clear the list, we set its size to be 0

#### Writing a class

- Construct an empty list with a given capacity
- Add an integer at a given list index (shifting remaining elements up)
- Add an integer to the end of the list
- Get the item at a given index
- !! The capacity and size of a list is not the same
  - The capacity represents the possible max value, where size is the current number of items

#### Syntax of Kotlin

- Array(capacity) {-1}
- Array(capacity, {-1})
- Array(capacity, {index -> -1})

### Resizing array list

- Showing how to implement a resizign array list
- How to implement a singly linked lists
- Discuss pros and cosn of these kins fo lists
  - Fixed capacity list
    - If capacity is too small, we run out of space
    - If capacity is too large, we waste memory
  - Resizing array list
    - Stat with an array of
