module Tabulate where

import Data.Array

{-
class Ix i where
  range :: (i, i) -> [i]
  index :: (i, i) -> i -> Int
  inRange :: (i, i) -> i -> Bool

array :: Ix i => (i, i) -> [(i, e)] -> Array i e
-}

{-|
This function can be used to construct an array of
given dimension where each element in the array is
determined by some given function on that index type.
-}
tabulate :: Ix i => (i, i) -- ^ the lower and upper bounds of the indices
         -> (i -> e)       -- ^ mapping from index to element
         -> Array i e      -- ^ an array spanning the full bounds
tabulate bounds f =
  array bounds (map (\i -> (i, f i)) (range bounds))
