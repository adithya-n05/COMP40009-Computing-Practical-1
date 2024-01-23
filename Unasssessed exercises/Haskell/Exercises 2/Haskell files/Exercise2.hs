import Data.Char
import Prelude

addDigit :: Int -> Int -> Int
addDigit m n = (m * 10) + n

celciusToFahrenheit :: Float -> Float
celciusToFahrenheit f = (f - 32) * (5 / 9)

type Vertex = (Float, Float)

distance :: Vertex -> Vertex -> Float
distance (x1, y1) (x2, y2) = sqrt ((x1-x2)^2 - (y1-y2)^2)

triangleArea :: Vertex -> Vertex -> Vertex -> Float
triangleArea p1 p2 p3 = sqrt (s * (s - a) * (s - b) * (s - c))
    where a = distance p1 p2
          b = distance p2 p3
          c = distance p3 p1
          s = (a + b + c) / 2

turns :: Float -> Float -> Float -> Float
turns start end r = end - start / perimeter
    where perimeter = 2 * pi * r

{-
isPrime :: Int -> Bool
isPrime a
    | a <= 1 = False
    | length divisors == 0 = True
    | otherwise = False
    where divisors = [x | x <- [2..ceiling (sqrt (fromIntegral a))], mod a x == 0]
-}

isPrime :: Int -> Bool
isPrime a = a>1 && null [x | x <- [2 .. ceiling (sqrt (fromIntegral a))], mod a x == 0]

fact :: Integer -> Integer
fact 0 = 1
fact a = a * fact (a - 1)

perm :: Int -> Int -> Int
perm n 1 = 1
perm n r = n * perm (n - 1) (n - r)

--10
remainder :: Int -> Int -> Int
remainder a b
    | a > b     = remainder (a-b) b
    | otherwise = a

--11
quotient :: Int -> Int -> Int
quotient a b
    | a > b     = 1 + quotient (a-b) b
    | otherwise = 0

-- 12
binary :: Int -> Int
binary a
    | a < 2     = a
    | otherwise = (binary (div a 2)) * 10 + (mod a 2)

-- 13 a
add :: Int -> Int -> Int
add 0 b = b
add a 0 = a
add a b = add (succ a) (pred b)

-- 13 b
larger :: Int -> Int -> Int
larger 0 b = b
larger a 0 = a
larger a b = succ (larger (pred a) (pred b))

-- 14
chop :: Int -> (Int, Int)
chop a = 

-- 15
concatenate :: Int -> Int -> Int
concatenate a b = read (show a ++ show b) :: Int

-- 16
fib :: Int -> Int
fib 0 = 1
fib 1 = 1
fib n = fib (n-1) + fib (n-2)