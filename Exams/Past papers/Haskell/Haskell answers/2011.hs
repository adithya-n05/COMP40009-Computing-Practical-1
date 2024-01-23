import Data.Maybe

data Expr = Number Int |
            Boolean Bool |
            Id String  |
            Prim String |
            Cond Expr Expr Expr |
            App Expr Expr |
            Fun String Expr
          deriving (Eq, Show)

data Type = TInt |
            TBool |
            TFun Type Type |
            TVar String |
            TErr 
          deriving (Eq, Show)

showT :: Type -> String
showT TInt  
  = "Int"
showT TBool 
  = "Bool"
showT (TFun t t') 
  = "(" ++ showT t ++ " -> " ++ showT t' ++ ")"
showT (TVar a) 
  = a
showT TErr  
  = "Type error"

type TypeTable = [(String, Type)]

type TEnv 
  = TypeTable    -- i.e. [(String, Type)]

type Sub 
  = TypeTable    -- i.e. [(String, Type)]  

-- Built-in function types...
primTypes :: TypeTable
primTypes 
  = [("+", TFun TInt (TFun TInt TInt)),
     (">", TFun TInt (TFun TInt TBool)),
     ("==", TFun TInt (TFun TInt TBool)),
     ("not", TFun TBool TBool)]

------------------------------------------------------
-- PART I

-- Pre: The search item is in the table
lookUp :: Eq a => a -> [(a, b)] -> b
lookUp x y
  = fromJust (lookup x y)

tryToLookUp :: Eq a => a -> b -> [(a, b)] -> b
tryToLookUp x y zs
  = maybe y id (lookup x zs)

-- Pre: The given value is in the table
reverseLookUp :: Eq b => b -> [(a, b)] -> [a]
reverseLookUp x ys
  = [a | (a, b) <- ys, b == x]

occurs :: String -> Type -> Bool
occurs x (TFun t1 t2)
  = occurs x t1 || occurs x t2
occurs x (TVar s)
  = x == s
occurs _ _
  = False

------------------------------------------------------
-- PART II

-- Pre: There are no user-defined functions (constructor Fun)
-- Pre: All type variables in the expression have a binding in the given 
--      type environment
inferType :: Expr -> TEnv -> Type
inferType (Number _) _ 
  = TInt
inferType (Boolean _) _ 
  = TBool
inferType (Id x) env
  = lookUp x env
inferType (Prim x) _
  = lookUp x primTypes
inferType (Cond ex1 ex2 ex3) env
  | inferType ex1 env /= TBool             = TErr
  | inferType ex2 env /= inferType ex3 env = TErr
  | otherwise                              = inferType ex2 env
inferType app @ (App _ _) env
  = inferApp app env
  
inferApp :: Expr -> TEnv -> Type
inferApp (App ex1 ex2) env
  | inferType ex1 env == TErr = TErr
  | inferType ex2 env == t1   = t2
  | otherwise                 = TErr
  where
    (TFun t1 t2) = inferType ex1 env
  
------------------------------------------------------
-- PART III

applySub :: Sub -> Type -> Type
applySub sub (TFun t1 t2)
  = TFun (applySub sub t1) (applySub sub t2)
applySub sub t @ (TVar x)
  = tryToLookUp x t sub
applySub sub t
  = t

unify :: Type -> Type -> Maybe Sub
unify t t'
  = unifyPairs [(t, t')] []

unifyPairs :: [(Type, Type)] -> Sub -> Maybe Sub
unifyPairs [] s
  = Just s
unifyPairs ((TVar v, t) : rest) sub
  | TVar v == t = unifyPairs rest sub
  | occurs v t  = Nothing
  | otherwise   = unifyPairs (substitute rest sub) ((v,t) : sub)
unifyPairs ((t, TVar v) : rest) sub
  | TVar v == t = unifyPairs rest sub
  | occurs v t  = Nothing
  | otherwise   = unifyPairs (substitute rest sub) ((v,t) : sub)
unifyPairs ((TFun t1 t2, TFun t1' t2') : rest) sub  
  = unifyPairs ((t1, t1') : (t2, t2') : rest) sub
unifyPairs ((t, t') : rest) sub
  | t == t'   = unifyPairs rest sub
  | otherwise = Nothing

substitute :: [(Type, Type)] -> Sub -> [(Type, Type)]
substitute xs sub 
  = map (\(t, t') -> (applySub sub t, applySub sub t')) xs

------------------------------------------------------
-- PART IV
-- PART IV

updateTEnv :: TEnv -> Sub -> TEnv
updateTEnv tenv tsub
  = map modify tenv
  where
    modify (v, t) = (v, applySub tsub t)

combine :: Sub -> Sub -> Sub
combine sNew sOld
  = sNew ++ updateTEnv sOld sNew

-- In combineSubs [s1, s2,..., sn], s1 should be the *most recent* substitution
-- and will be applied *last*
combineSubs :: [Sub] -> Sub
combineSubs 
  = foldr1 combine

inferPolyType :: Expr -> Type
inferPolyType e
  = (\(_, x, _) -> x) (inferPolyType' e [] initialLabels)

-- You may optionally wish to use one of the following helper function declarations
-- as suggested in the specification. 

inferPolyType' :: Expr -> TEnv -> [String] -> (Sub, Type, [String])
inferPolyType' (Number _) tenv labels
  = (tenv, TInt, labels)
inferPolyType' (Boolean _) tenv labels
  = (tenv, TBool, labels)
inferPolyType' (Prim id) tenv labels
  = (tenv, lookUp id primTypes, labels)
inferPolyType' (Id id) tenv labels
  | t == (TVar "$Dummy")
    = (tenv, TVar id, labels)
  | otherwise
    = (tenv, t, labels)
  where
    t = tryToLookUp id (TVar "$Dummy") tenv
inferPolyType' (Fun id expr) tenv labels
  | t == TErr
    = (newSub, t, newLabels)
  | otherwise
    = (newSub', TFun (applySub newSub (TVar (head labels))) t, newLabels)
  where
    (newSub, t, newLabels) 
      = inferPolyType' expr ((id, TVar (head labels)) : tenv) (tail labels)
    newSub' = [(id', val) | (id', val) <- newSub, id /= id']
inferPolyType' (App fun exp) tenv labels
  | unisubs == Nothing
    = ([], TErr, [])
  | otherwise
    = (finsubs, applySub finsubs (TVar (head newLabels')), tail newLabels' )
  where
    (newSub, funType, newLabels)
      = inferPolyType' fun tenv labels
    (newSub', t, newLabels')
      = inferPolyType' exp (updateTEnv tenv newSub) newLabels
    unisubs = unify funType (TFun t (TVar (head newLabels')))
    finsubs = combineSubs [fromJust unisubs, newSub', newSub]
inferPolyType' (Cond exp exp1 exp2) tenv labels
  | unisubs == Nothing || unisubs' == Nothing
    = ([], TErr, [])
  | otherwise
    = (finsubs', applySub finsubs' t1, newLabels'')
  where
    (newSubs, t, newLabels) 
      = inferPolyType' exp tenv labels
    (newSubs', t1, newLabels') 
      = inferPolyType' exp1 (updateTEnv tenv newSubs) newLabels
    (newSubs'', t2, newLabels'')
      = inferPolyType' exp2 (updateTEnv (updateTEnv tenv newSubs) newSubs') newLabels'
    unisubs = unify TBool t
    unisubs' = unify t1 t2
    finsubs = combineSubs [fromJust unisubs', fromJust unisubs, newSubs'', newSubs', newSubs, tenv]
    finsubs' = getBest (iterate (updateTEnv finsubs) finsubs)
    getBest (h1 : h2 : t)
      | h1 == h2
        = h2
      | otherwise
        = getBest (h2 : t)

--   = undefined

-- inferPolyType' :: Expr -> TEnv -> Int -> (Sub, Type, Int)
-- inferPolyType' 
--   = undefined
{-data Expr = Number Int |
            Boolean Bool |
            Id String  |
            Prim String |
            Cond Expr Expr Expr |
            App Expr Expr |
            Fun String Expr
          deriving (Eq, Show)-}
------------------------------------------------------
-- Monomorphic type inference test cases from Table 1...

env :: TEnv
env = [("x",TInt),("y",TInt),("b",TBool),("c",TBool)]

ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8 :: Expr
type1, type2, type3, type4, type5, type6, type7, type8 :: Type

ex1 = Number 9
type1 = TInt

ex2 = Boolean False
type2 = TBool

ex3 = Prim "not"
type3 =  TFun TBool TBool

ex4 = App (Prim "not") (Boolean True)
type4 = TBool

ex5 = App (Prim ">") (Number 0)
type5 = TFun TInt TBool

ex6 = App (App (Prim "+") (Boolean True)) (Number 5)
type6 = TErr

ex7 = Cond (Boolean True) (Boolean False) (Id "c")
type7 = TBool

ex8 = Cond (App (Prim "==") (Number 4)) (Id "b") (Id "c")
type8 = TErr

------------------------------------------------------
-- Unification test cases from Table 2...

u1a, u1b, u2a, u2b, u3a, u3b, u4a, u4b, u5a, u5b, u6a, u6b :: Type
sub1, sub2, sub3, sub4, sub5, sub6 :: Maybe Sub

u1a = TFun (TVar "a") TInt
u1b = TVar "b"
sub1 = Just [("b",TFun (TVar "a") TInt)]

u2a = TFun TBool TBool
u2b = TFun TBool TBool
sub2 = Just []

u3a = TFun (TVar "a") TInt
u3b = TFun TBool TInt
sub3 = Just [("a",TBool)]

u4a = TBool
u4b = TFun TInt TBool
sub4 = Nothing

u5a = TFun (TVar "a") TInt
u5b = TFun TBool (TVar "b")
sub5 = Just [("b",TInt),("a",TBool)]

u6a = TFun (TVar "a") (TVar "a")
u6b = TVar "a"
sub6 = Nothing

------------------------------------------------------
-- Polymorphic type inference test cases from Table 3...

ex9, ex10, ex11, ex12, ex13, ex14 :: Expr
type9, type10, type11, type12, type13, type14 :: Type

ex9 = Fun "x" (Boolean True)
type9 = TFun (TVar "a1") TBool

ex10 = Fun "x" (Id "x")
type10 = TFun (TVar "a1") (TVar "a1")

ex11 = Fun "x" (App (Prim "not") (Id "x"))
type11 = TFun TBool TBool

ex12 = Fun "x" (Fun "y" (App (Id "y") (Id "x")))
type12 = TFun (TVar "a1") (TFun (TFun (TVar "a1") (TVar "a3")) (TVar "a3"))

ex13 = Fun "x" (Fun "y" (App (App (Id "y") (Id "x")) (Number 7)))
type13 = TFun (TVar "a1") (TFun (TFun (TVar "a1") (TFun TInt (TVar "a3"))) 
              (TVar "a3"))

ex14 = Fun "x" (Fun "y" (App (Id "x") (Prim "+"))) 
type14 = TFun (TFun (TFun TInt (TFun TInt TInt)) (TVar "a3")) 
              (TFun (TVar "a2") (TVar "a3"))
