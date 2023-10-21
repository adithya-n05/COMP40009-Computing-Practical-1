# Functional Programming

- [Functional Programming](#functional-programming)
  - [Lecture 1: Functional programming 09-10-2023](#lecture-1-functional-programming-09-10-2023)
    - [Recursive Functions](#recursive-functions)
    - [Mutliple parameters](#mutliple-parameters)
      - [Tuples as inputs](#tuples-as-inputs)
      - [Curried functions](#curried-functions)
    - [Where and Let](#where-and-let)
    - [Polymorphic functions](#polymorphic-functions)
  - [Lecture 2: Functional programming 10-10-2023](#lecture-2-functional-programming-10-10-2023)
    - [Questions](#questions)
    - [Continuing polymporphic functions](#continuing-polymporphic-functions)
    - [Lists](#lists)
      - [Anatomy of a list](#anatomy-of-a-list)
        - [Empty lists](#empty-lists)
      - [What can we do with lists?](#what-can-we-do-with-lists)
        - [Pattern matching](#pattern-matching)
    - [Tuples](#tuples)
      - [A closer look at some tuple examples](#a-closer-look-at-some-tuple-examples)
    - [Recursion over lists](#recursion-over-lists)

## Lecture 1: Functional programming 09-10-2023

### Recursive Functions

- We have already seen functions that are recursive such as factorial and (the value infinity)
- It is also possible for functions to be mutually recursive:

```haskell
even :: Int -> Bool
even 0 = True
even n = odd (n-1)

odd :: Int -> Bool
odd 0 = False
odd n = even(n-1)
```

- These two functions will continuously call each other recursively and it will terminate eventually, thus this will also work

### Mutliple parameters

- Sometimes you want to be able to pass more than one parameter to a function
- Multiple parameters allow the output of a function to be determined by more than one value, such as with addition
- Technically every function in haskell only takes in one input, all this time we've been using this technique called currying to be able to pass more than 1 input
- There are 2 ways to do this, to pass in a tuple as an input or to have a function output another function which outputs another function etc.

#### Tuples as inputs

```haskell
add :: (Int, Int) -> Int
add (x,y) {-This was a pattern match on tuples-} = x + y
-- This function only takes in a tuple of 2 numbers, it doesnt allow for you to just pass in the number
```

#### Curried functions

- This function works but is slightly unidiomatic, it is not what you would normally write as a haskell programmer
- Consider this:

```haskell
plusOne :: Int -> Int
plusOne y = 1 + y

plusTwo :: Int -> Int
plusTwo :: y = 2 + y
```

- We can generalise this to a curried function, where we take in a number and we add to it itself

```haskell
plus 1 = plusOne
plus 2 = PlusTwo
```

- It would be nice if we could perform plus 3, but we haven't defined it
- Let's think about the following:  
  - The domain of the function plus is an `Int`
  - The codomain of the function plus is going to be the same as the type of plusOne, and of plusTwo, so it is `Int -> Int`
  - So now the type of plus is:

```haskell
plus :: Int -> (Int -> Int)
```

- Now the question is, how can we give in a number as an input and have haskell automatically create a function as an output
- To define this function, we need to produce a function, which is where we can use a lambda to create a function

```haskell
-- The following is the math form of the expression
plus x = λy -> x + y
```

```haskell
-- The following is the math form of the expression
plus x = \y -> x + y
```

- The purpose of this function is to add the number that we give it to start with, which we define by having the function output a function

- It is quite annoying to have to write down the lambda expression. But in theory it must be present. Hence we can just write the following

```haskell
plus x y = x + y
```

- This can be quite confusing as the type now remains the same: `Int -> (Int -> Int)`. But the bracket is sort of "being put in between these equal signs".
- Technically, the right arrow always point right, hence you can just not write the brackets as `Int -> Int -> Int`
- However, as the bracket is right binding, they are optional towards the right, however left bounded parenthesis usage means that the bracket cannot be erased
  - Here is an example: `(Int -> Int) -> Int` is a function that takes a function and returns an integer, but the above takes a number and returns a function
  - `a -> b -> c` is the same as `a -> (b -> c)` as the arrow is right associative.
  - `a -> b -> c -> d -> e` is the literal same (i.e. $\cong$) as `a -> (b -> (c -> (d -> e)))`
  
- Functions can even take in functions, this is called a higher order function, will be covered later

You can think of the paranthesis as spanning in between the equal signs as follow:

```haskell
plus x (y = x + y)
-- You cannot actually allowed to put the paranthesis as above in haskell as it is not allowed
```

- However as the above is not allowed, i.e. paranthesis spanning equals signs
- But we can write the following to allow for you as a human to understand it

```haskell
(plus x) y
```

- But the above is not the same as

```haskell
plus (x y)
```

- As the above means take thing x and apply it to y, i.e. x is a function that is applied to y, but x isn't a function, its an integer, hence it makes no sense at all
- The function `plus x y` is a situation where you have a function that pattern matches on the first value of x, and then produces a function that pattern matches on the second value y
- Paranthesis exist to group things together

**Beware of the following**

- `Int -> (Int ->Int)` is the same as `Int -> Int ->Int` but `(Int -> Int) ->Int` is not the same as `Int -> Int ->Int`
- Plus is not a higher order function as it does not take a function as an input

### Where and Let

- Consider the poewr function that raises one input to the power of another. Example: $n^k$
- We know that $n^{2k}$ = $n^k * n^k$
- What about: $n^{2k+1}$ = $n^k * n^k * n$

<p><br></p>

- In Haskell we would write this as:

```haskell
power :: Int -> Int -> Int
power n 0 = 1
power n k
    | even {-Even is a predefined function-} k = power n (k `div` 2) * power n (k `div 2`) 
    | otherwise = n * power n (k `div` 2) * power n (k `div 2`) -- div will truncate, hence we don't need a minus 1. I.e. 5/2 = 2 here 
-- The parenthesis here is required and must be here as function application binds higher than anything
-- This would mean that power n k without brackets would actually mean `((power n) k)`
```

> - Whenever you have a function that takes in 2 arguments, you can put the function in the middle of the argument with backticks : `mod 5 3` is the same as:

>```haskell
>5 `mod` 3
>```

- The problem here is that we have repeated power n (k `div` 2) multiple times. To avoid this we want to name this expression, which we can do in 2 different ways

```haskell
power :: Int -> Int -> Int
power n 0 = 1
power n k
    | even k    = x * x
    | otherwise = n * x * x
    where
        x = power n (k `div` 2)
        -- This where clause introduces a value locally that only exists within this function
        -- The x here is only evaluated once, giving a performance improvement
```

- In a where clause, the values that are defined are evaluated just once and the result is shared

<p><br></p>

- Another variation is to use a let clause:

```haskell
power :: Int -> Int -> Int
power n 0 = 1
power n k
    | even k = let x = power n (k `div` 2) in x * x
    | otherwise = let x = power n (k `div` 2) in n * x *. x
```

<p><br></p>

With where and let w can have multiple bindings, e.g:

```haskell
where x = ...
      y = ...
      z = ...
```

```haskell
let x = ...
    y = ...
    z = ...
```

- The values x, y, z can be mutually recursive, also pay attention to the assignment as tabs are evil.

### Polymorphic functions

- Some functions do not need to pattern match on their input for them to be defined

Here is an example for the identity function, on a given data type, that will give you back the same value:

```haskell
idInt :: Int -> Int
idInt x = x
```

- But you can't use the above for Booleans, so you'll need to rewrite the function as follows for booleans:

```haskell
idInt :: Bool ->Bool
idInt x = x
```

- But what about for chars, you'd have to rewrite the function

```haskell
idInt :: Char -> Char
idInt x = x
```

- It is a hassle to have to rewrite this program to work with every data type
- Hence, there is a way to generalise this as follows:

The following code wouldn't actually work, you'd need to rewrite the ∀ sign:

```haskell
id :: ∀a . a -> a
id x = x
```

- The ∀ is written as `forall` in haskell
- A type that contains a variable like `∀a` is called polymorphic

<br>

- For example, we can identify these functions as:

```haskell
idInt = id@Int
```

- The above would essentially replace those "a's" with a Int or Bool or Char etc.
- But Haskell, allows the user to not write the ∀a. or the @a type applications and will do the right tihng in almost all cases you will encounter

<br>

- Therefore the idiomatic way, i.e. the normal way to define this function is to write:

```haskell
id :: a-> a
id x = x
```

- Here make sure all variables start in lowercase in Haskell, the a, should remain lowercase
  - If you want it to be a specific type like Bool or Int or Char, the first letter would always be capital letter
- If you were to replace the `a` with a `u` or a `x`, the variable below would not be affected. The function would still work.
  - The x's that exist on the type level would not interfere with the x's on the variable level
- All uppercase things would be uppercase only within data definition, the infinity we had above was a result of computation, hence would not be lowercase?

<br>

- Here is another example for the constant function that always returns the second input

```haskell
const 3 "hello" = "hello"
const True 5 = 5
const 42 False = False
```

- This function is polymorphic in both parameters, hence we can write it as follows:

```haskell
const :: a -> b -> a
const x y = x
```

- To write the above with explicit for alls:

```haskell
const :: forall a b . a -> b -> a
const x y = x
```

- For usage of the above using @ signs, you would write

```haskell
const@Int@Char
```

## Lecture 2: Functional programming 10-10-2023

### Questions

- Otherwise has literally been defined as

```haskell
otherwise :: Bool
otherwise = True
```

- Otherwise has been defined as True and can never be changed

<br>

- I would always use a where clause whenever possible, but it scopes over everything that is indented beneath it
  - A let whereas has a more specific scope where you would want it to be over a specific clause, ranging over individual pattern guards
  - A where clause would range across pattern guards

<br>

Meaning of right associative

- Lets consider the functions ⊕ (plus all) and ⊙ (hot dog bun operation)

Right associative would mean that:

x ⊕ y ⊕ z = x ⊕ (y ⊕ z)

Whereas left associative would mean that:

x ⊙ y ⊙ z = (x ⊙ y) ⊙ z

- Whenever someone mentions something is just associative, it means that it is both, like addition, right and left, hence it doesnt matter

### Continuing polymporphic functions

- Convention has it that we tend to use a, b, c for type variables
  - x y and z tend to be used as variables

<br>

- Here is another example of a polymorphic function

```haskell
ignore :: a -> b -> b
ignore x y = y
```

### Lists

- A list is an ordered sequence of elements of the same type

Examples of lists include

```haskell
[3, 5] :: [Int]
```

```haskell
['a', 'b', 'c'] :: [Char]
```

```haskell
[True, True False] :: [Bool]
```

```haskell
[True, False, True] :: [Bool]
```

```haskell
[[7], [3,12], [83]] :: [[Int]]
```

The following are not lists

```haskell
[3, True]
```

```haskell
[[3], [5,8], 7]
-- There is a list of ints within the list, but we just have a regular 7 that isn't in a list
```

#### Anatomy of a list

- Every non empty list has named parts

```haskell
[3, 1, 4, 1, 5]
```

- The 3 is called the head of hte list
- The `1, 4, 1, 5` is called the tail of the list
- The 5 at the end alone is called the last element of the list
- The `3, 1, 4, 1` is called the init of the list

<br>

##### Empty lists

- We also have empty lists:

```haskell
[] :: [Int]
```

```haskell
[] :: [Bool]
```

```haskell
[] : [Char]
```

- It seems that this empty list has 3 types. In fact, it has a single type, because lists are polymorphic
- Thus we could write it as follows to understand what is happening

```haskell
[] :: ∀ a . [a]
```

or as in Haskell:

```haskell
[] :: forall a . [a]
```

- Here we can do type application to instantiate a specific type
- Using this we can see that:

```haskell
[]@Char :: [Char]
[]@Int  :: [Int]
```

- But it can be tedious to write this @ repeatedly
- In Haskell we do not need to write the for all a or the @Char or @Int etc. These are automatically inferred.
  - In fact it is actually normal to do this

<br>

- In fact every list can be decomposed (EVERY)
- Take: `[3, 1, 4, 1, 5]`
- It is the same as: `3 : [1, 4, 1, 5]`. (The colon is actually called "cons")
- But if the above is true, we can actually further decompose it, giving us:

```haskell
[3, 1, 4, 1, 5]
= 3 : [1, 4, 1, 5]
= 3 : 1 : [4, 1, 5]
= 3 : 1 : 4 : [1, 5]
= 3 : 1 : 4 : 1 : [5]
= 3 : 1 : 4 : 1 : 5 : []
```

- Here, (:) is a binary operation that adds an element to a list
  - This is akin to a function

Observe the following:

```haskell
3 : [2, 7]
-- Here the : has a type too, like a function
-- The inputs would be the 3 from the left, and the list of integers [2, 7]
-- The output would be a full list of ints
-- Thus the (:) operation would have type:
    -- (:) :: Int -> [Int] -> Int
```

- Cons will only put things on the front of the list, not the back
  - Cons cannot take a list in the front and an item in the back and add it to the back of the list

What is the type of cons?

- It is polymorphic!:

```haskell
(:) :: ∀. a -> [a] -> [a]
```

or more simply:

```haskell
(:) :: a -> [a] -> [a]
```

If we wanted to define lists ourselves, we would write:

```haskell
data [a] where
    [] :: [a]
    (:) :: a -> [a] -> [a]
```

- The above can also be written as:

```haskell
data [a] = [] | (:) a [a]
```

- You cannot add an item to the end of a list, you will have to do more computation to add it to the back of the list

#### What can we do with lists?

- We can pattern match over lists

##### Pattern matching

```haskell
isEmpty :: [a] -> Bool
isEmpty [] = True
isEmpty _ = True
-- Here, you may have wanted to use otherwise, however you should avoid using that as otherwise literally means True, hence you would be saying True is this
-- It would technically work, hence you should avoid it. Normally we use the word otherwise is used to mean literally True
-- Instead use _ which means for anything else, write False
-- Using otherwise here would literally also mean the same as:
-- isEmpty [] = otherwise
-- isEmpty otherwise = False
-- Which is really confusing!
```

- We can also do a deeper pattern match with a function that checks if a function is only one element

```haskell
isSingle :: [a] -> Bool
isSingle [x] = True
isSingle _ = False
-- Here, [x] = x : []
```

- The following functions are dangerous

```haskell
head :: [a] -> a
head (x:xs) = x
```

```haskell
tail :: [a] -> a
tail (x:xs) = xs
```

- These functions are dangerous as they are **partial**
  - A function is partial if the output is undefined for some input
- A function is total if it is well defined for all inputs  
- We can add a clause such as:

```haskell
head :: [a] -> a
head (x:xs) = x
head [] = undefined
```

- What is (x:xs)?
  - It means a list of x : y. Here we are trying to name different variable names
  - The x is an Int, the y is a list of Ints, i.e. [Int]
  - Hence, we say xs, for the y!
  - So this actually means a list with the con operation
  - But here we must have the parenthesis, as function application is the most tightly binding
    - If we didn't have brackets, it would mean perform cons of (head x) : xs!

- We can define undefined:

```haskell
undefined :: a
undefined = undefined
-- But this would end up with an endless loop, this is not actually the way of defining undefined, instead it is just an explanation
```

- This is similar to:

```haskell
error :: String -> a
-- This will output the given string and fail, such as "Error, you have entered an incorrect input"
```

So we can write:

```haskell
head :: [a] -> a
head (x:xs) = x
head [] = error
```

### Tuples

- Lists in haskell are homogeneous, tuples are heterogeneous
- A tuple is an ordered sequence of possibly different types

```haskell
(True, 5) :: (Bool, Int)
```

```haskell
('x', False) :: (Char, Bool)
```

Note that these are all different:

```haskell
(True, (5, 'x')) :: (Bool, (Int, Char))
```

```haskell
((True, 5), 'x') :: ((Bool, Int), Char)
```

```haskell
(True, 5, 'x') :: (Bool, Int, Char)
```

#### A closer look at some tuple examples

- Like lists, Tuples are polymorphic
- Lets look at tuples from smallest to largest

```haskell
() :: ()
-- Here this is a tuple that has no elements with no types
-- The type is the same as the value here, the only way to distinguish between them is that the value on the right is the type and the left is the name
```

- To define this we would write

```haskell
data () where
    () :: ()
```

- What about a tuple with only one element?

```haskell
(42) :: (Int)
```

however, paranthesis can be erased when there's no ambiguity, but this is literally just:

```haskell
42 :: Int
```

- The constructor for tuples of 2 values is defined as:

```haskell
(,) :: a -> b -> (a,b)
(,) x y = (x,y)
```

where:

```haskell
data (a,b) where
    (,) :: a -> b -> (a,b)
```

- As for 3 values?

```haskell
(,,) :: a -> b -> c -> (a,b,c)
(,,) x y z = (x,y, z)
```

where:

```haskell
data (a,b,c) where
    (,,) :: a -> b -> c -> (a,b,c)
```

- You can see the pattern, this here will extend for up to tuples of 61 elements in length
- These are all pre defined for you hence you dont have to do this yourself

### Recursion over lists

- Recursion is safe when it is done over data, hence we know it will reach a value
- In programming we like to always do structural induction, trying to make sure we perform recursion similar to the process of induction
- Unbounded recursion can be dangerous: For example we saw with infinity and undefined earlier. It is safe to use structural recursion,where the recursion follows the structure of inductive data.
- Lists and natural numbers are examples of inductive data
- Here are some structurally recursive functions

```haskell
sum :: [Int] -> Int
sum [] = 0 -- This is the base case
sum (x:xs) = x + sum xs -- This is the recursive case
-- Here we are ensuring that the list we are recursing over is definitely smaller than the original list, hence we know it will terminate similar to induction
```

- Notice that the recursive call to 'sum' on the right hand side is on xs, which is smaller than the input on the left hand side which is x:xs.
  - This is exactly in correspondence with induction

Another example:

```haskell
Length :: [a] -> a
Length [] = 0
Length (x:xs) = 1 + Length xs
```

Now some functions that return lists

Imagine a function that we can have this happen:

`take 3 ['z', 'a', 'p', 'p', 'y'] = ['z', 'a', 'p']`

```haskell
take :: Int -> [a] -> [a]
take 0 _ = []
take n [] = [] 
take n (x:xs) = x : take (n-1) (xs)
-- Here we have structural on the natural numbers and on the list!
```

**Beware of the following**

- In Haskell [Char] is the same as a String:
  - This is known as a type synonym

```haskell
type String = [Char]
```

Now lets run through this function:

```haskell
take 2 ['z', 'a', 'p', 'p', 'y']
-- Calling definition of {def take}
-- Does not match take 0 _
-- Does not match take n []
-- Thus call take n (x:xs)
= 'z' : take 1 ['a', 'p', 'p', 'y']
-- Calling definition of {def take}
-- Repeat again
= 'z' : 'a' : take 0 ['a', 'p', 'p', 'y']
-- Calling definition of {def take}
-- Using case take 0 _
= 'z' : 'a' : []
```
