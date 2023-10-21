module Dist (dist, tabulate, fromList) where

import Data.Array

-- we can use dynamic programming to tabulate the function, building up a two
-- dimensional table with the result of tabulate with the last x characters of
-- the first string and that last y characters of the second string being stored
-- at (x, y)
tabulate :: Ix i => (i, i) -> (i -> e) -> Array i e
tabulate (a, b) f = array (a, b) [(i, f i) | i <- range (a, b)]

fromList :: [e] -> Array Int e
fromList xs = array (0, length xs - 1) (zip (iterate (+ 1) 0) xs)

dist :: String -> String -> Int
dist xs ys = table ! (m, n)
  where
    table :: Array (Int, Int) Int
    table = tabulate ((0, 0), (m, n)) memo

    m = length xs
    n = length ys
    xsArr = fromList xs
    ysArr = fromList ys

    memo :: (Int, Int) -> Int
    memo (x, 0) = x
    memo (0, y) = y
    memo (x, y) = minimum [table ! (x - 1, y) + 1,
                           table ! (x, y - 1) + 1,
                           table ! (x - 1, y - 1) +
                             if (xsArr ! (m - x) == ysArr ! (n - y)) then 0
                                                                     else 1]
