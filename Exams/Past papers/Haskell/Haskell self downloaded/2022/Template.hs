module SC where

import Data.List
import Data.Maybe
import Debug.Trace


import Types
import Examples

---------------------------------------------------------

prims :: [Id]
prims
  = ["+", "-", "*", "<=", "ite"]

lookUp :: Id -> [(Id, a)] -> a
lookUp v env
  = fromMaybe (error ("lookUp failed with search key " ++ v))
              (lookup v env)

---------------------------------------------------------
-- Part I



isFun :: Exp -> Bool
isFun (Fun _ _)
  = True
isFun _
  = False

splitDefs :: [Binding] -> ([Binding], [Binding])
splitDefs bnds
  = partition (\(a,b) -> isFun b) bnds

topLevelFunctions :: Exp -> Int
topLevelFunctions (Let bnds exp1) 
  = length (filter (\a -> isFun (snd a)) bnds)

---------------------------------------------------------
-- Part II


unionAll :: Eq a => [[a]] -> [a]
unionAll l
  = foldl1 (union) l

freeVars :: Exp -> [Id]
freeVars (Const i)
  = []
freeVars (Var v)
  | elem v prims = []
  | otherwise       = [v]
freeVars (Fun ids e1)
  = (freeVars e1) \\ ids
freeVars (App e1 e2l)
  = nub (concatMap (freeVars) e2l) \\ nub (freeVars e1)
freeVars (Let binds e1)
  =  nub (concatMap (\a -> freeVars (snd a)) binds) \\ nub (freeVars e1)
---------------------------------------------------------
-- Part III

-- Given...
lambdaLift :: Exp -> Exp
lambdaLift e
  = lift (modifyFunctions (buildFVMap e) e)

buildFVMap :: Exp -> [(Id, [Id])]
buildFVMap 
  = undefined

modifyFunctions :: [(Id, [Id])] -> Exp -> Exp
-- Pre: The mapping table contains a binding for every function
-- named in the expression.
modifyFunctions 
  = undefined

-- The default definition here is id.
-- If you implement the above two functions but not this one
-- then lambdaLift above will remove all the free variables
-- in functions; it just won't do any lifting.
lift :: Exp -> Exp
lift 
  = id

-- You may wish to use this...
lift' :: Exp -> (Exp, [Supercombinator])
lift' 
  = undefined
