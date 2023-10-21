**ADA Design and Analysis 2023**

> import Control.Exception (assert) -- For checking solutions
> import Dist (dist) -- For dist definition from lectures

1.a.i)

*Define `palindrome xs` to return `True` when `xs` is a palindrome, and `False`
otherwise. State the complexity of your function.*

> palindrome :: String -> Bool
> palindrome xs
>   = xs == (reverse xs)

Complexity is O(n) where n is string length.

1.a.ii)

*Briefly explain why the following properties hold for any string `xs` of length
`n`*

A) *all the characters in a nearest palindrome to `xs` must be from `xs`*

B) *the edit distance between `xs` and its nearest palindrome is at most
floor(`n` / 2)*

C) *the length of a nearest palindrome to `xs` is bounded by `n` plus
floor(`n` / 2)*

1.b.i)

*Define `strings xs n` to produce all the strings of length `n` whose characters
are drawn from `xs`. This need not be efficient but its complexity should be
bounded by $O(m^n)$ where `m = length xs`.*

> strings :: String -> Int -> [String]
> strings _  0 = [""]
> strings xs n = [s : st | s <- xs, st <- strings xs (n - 1)]

1.b.ii)

*Using the `strings` function, define `palindromes xs` to return all the
palindromes than can be formed from `xs`. Hint: consider the properties of
nearest palindromes and filter appropriate strings with the `palindrome`
function.*

> palindromes :: String -> [String]
> palindromes xs
>   = (filter palindrome . concatMap (strings xs)) [0..ml]
>   where ml = n + (n `div` 2)
>         n  = length xs

1.b.iii)

*Using `palindromes`, define `palindist xs` to calculate the edit distance
between `xs` and its nearest palindromes. For example, `palindist "abXcYbZ" = 2`.
You may assume `dist :: String -> String -> Int`. This need not be efficient.*

> palindist :: String -> Int
> palindist xs
>   = minimum (map (dist xs) (palindromes xs))

1.c.i)

*Consider how `palindist "abcba"` relates to the result of applying `palindist`
to the following strings: `"abcbaX"`, `"Xabcba"`, `"XabcbaX"`, `"XabcbaY"`.*

*Using this relationship, define `palindist'` a recursive version of `palindist`.*

I've put a couple tests here to check solutions.

> check = do
>   putStr $ assert (palindrome "abcba") "passed\n"
>   putStr $ assert ((palindrome "abcbef")) "passed\n"
