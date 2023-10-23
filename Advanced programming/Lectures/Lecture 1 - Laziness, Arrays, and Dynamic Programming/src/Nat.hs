module Nat where

data Nat = Z | S Nat  deriving (Eq, Ord, Show)

zero = Z
one = S Z
two = S (S Z)
inf = S inf

less :: Nat -> Nat -> Bool
less Z Z = False
less Z _ = True
less _ Z = False
less (S n) (S m) = n < m
