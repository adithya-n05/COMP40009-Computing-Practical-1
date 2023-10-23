module Matrix where

import Data.Array
import Tabulate

matrix :: Array Int Int -> Int
matrix dims = arr ! (0, n - 1)
  where
    n = length dims
    arr = tabulate ((0, 0), (n-1, n-1)) (uncurry matrix')
    matrix' :: Int -> Int -> Int
    matrix' i j
      | i == j - 1 = 0
      | otherwise  = minimum [ arr ! (i, k)
                             + arr ! (k, j)
                             + (dims ! i * dims ! k * dims ! j)
                             | k <- [i + 1 .. j - 1]]
