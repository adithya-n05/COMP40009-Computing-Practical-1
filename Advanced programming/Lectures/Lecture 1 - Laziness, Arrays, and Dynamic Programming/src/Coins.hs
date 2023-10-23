module Coins where

import Data.Array
import Tabulate

type Pence = Int

coins :: [Pence]
coins = [1, 2, 5, 10, 20, 50, 100, 200]

{-|
This function determines the minimum number of
coins required to offer exact change for the given
number of pence.

change 4 = 2, since 2p + 2p = 4p
change 26 = 3, since 20p + 5p + 1p = 26p
-}
change :: Pence -> Int
change g = arr ! g
  where
    arr = tabulate (0, g) change'
    change' 0 = 0
    change' g = minimum [(arr ! (g - coin)) + 1 | coin <- coins
                                                , coin <= g]

{- Old exponential complexity function
change :: Pence -> Int
change 0 = 0
change g = minimum [change (g - coin) + 1 | coin <- coins, coin <= g]
-}
