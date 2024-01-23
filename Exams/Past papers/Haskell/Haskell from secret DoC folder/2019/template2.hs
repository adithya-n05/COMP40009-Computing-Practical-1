module SOL where

import Data.List
import Data.Maybe

import Types
import TestData

printF :: Formula -> IO()
printF
  = putStrLn . showF
  where
    showF (Var v)
      = v
    showF (Not f)
      = '!' : showF f
    showF (And f f')
      = "(" ++ showF f ++ " & " ++ showF f' ++ ")"
    showF (Or f f')
      = "(" ++ showF f ++ " | " ++ showF f' ++ ")"

--------------------------------------------------------------------------
-- Part I

-- 1 mark
lookUp :: Eq a => a -> [(a, b)] -> b
-- Pre: The item being looked up has a unique binding in the list
lookUp query ps
  = head [ value | (item, value) <- ps, query == item]


-- 3 marks
vars :: Formula -> [Id]
vars (Var base)
  = [base]
vars (Not f)
  = sort (nub (vars f))
vars (And f1 f2)
  = sort (nub (vars f1 ++ vars f2))
vars (Or f1 f2)
  = sort (nub (vars f1 ++ vars f2))

-- 1 mark
idMap :: Formula -> IdMap
idMap form
  = zip (vars form) [1..] 

--------------------------------------------------------------------------
-- Part II

-- An encoding of the Or distribution rules.
-- Both arguments are assumed to be in CNF, so the
-- arguments of all And nodes will also be in CNF.
distribute :: CNF -> CNF -> CNF
distribute a (And b c)
  = And (distribute a b) (distribute a c)
distribute (And a b) c
  = And (distribute a c) (distribute b c)
distribute a b
  = Or a b


-- 4 marks
toNNF :: Formula -> NNF
toNNF (Not (Not f))
  = f
toNNF (Not (And f1 f2))
  = Or (toNNF (Not f1)) (toNNF (Not f2))
toNNF (Not (Or f1 f2))
  = And (toNNF (Not f1)) (toNNF (Not f2))
toNNF (Var f)
  = Var f
toNNF (And f1 f2)
  = And (toNNF f1) (toNNF f2)
toNNF (Or f1 f2)
  = Or (toNNF f1) (toNNF f2)
toNNF (Not f)
  = Not f

-- 3 marks
toCNF :: Formula -> CNF
toCNF f' = toCNF' nnf
  where
    nnf = toNNF f'

toCNF' :: NNF -> CNF
toCNF' (Or f1 f2) = distribute f1 f2
toCNF' (And f1 f2) = (And f1 f2)
toCNF' (Not f) = (Not f) 

-- 4 marks
flatten :: CNF -> CNFRep
flatten f'
  = flattenHelper f' mappedvars
  where mappedvars = idMap f'

flattenHelper :: CNF -> IdMap -> CNFRep
flattenHelper (Or f1 f2) maps
  = [concat ((flattenHelper f1 maps) ++ (flattenHelper f2 maps))]
flattenHelper (And f1 f2) maps
  = flattenHelper f1 maps ++ flattenHelper f2 maps
flattenHelper (Var f) maps
  = [[lookUp f maps]]
flattenHelper (Not (Var f)) maps
  = [[-(lookUp f maps)]]

--------------------------------------------------------------------------
-- Part III

-- 5 marks
propUnits :: CNFRep -> (CNFRep, [Int])
propUnits c'
  = (
    [ c | c <- c', not (any (\i -> elem i negatedunits) c)] ++ 
    [ del c negatedunits | c <- c', any (\i -> elem i negatedunits) c] 
    nub (concat [ c \\ units | c <- c', any (\i -> elem i negatedunits) c])
    )
  where
    units = propUnitsCheck c'
    negatedunits = negates units

propUnitsCheck :: CNFRep -> [Int]
propUnitsCheck [] = []
propUnitsCheck (c:cs)
  | length c == 1 = head c : propUnitsCheck cs
  | otherwise = propUnitsCheck cs

negates :: [Int] -> [Int]
negates k = nub (k ++ [-i | i <- k])

del :: [Int] -> [Int] -> [Int]
del c@(a:as) c'@(b:bs)
  | elem a c' = del as c'
  | otherwise = a : (del as c')

-- 4 marks
dp :: CNFRep -> [[Int]]
dp 
  = undefined

--------------------------------------------------------------------------
-- Part IV

-- Bonus 2 marks
allSat :: Formula -> [[(Id, Bool)]]
allSat
  = undefined


