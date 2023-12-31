-- solution very similar to the one made by Tony Field

module Test where

import Data.Maybe
import Data.List

type Id = String

type Function = (Id, [Id], Block)

type Block = [Statement]

data Statement = Assign Id Exp |
                 If Exp Block Block |
                 DoWhile Block Exp
               deriving (Eq, Show)

data Exp = Const Int | Var Id | Apply Op Exp Exp | Phi Exp Exp
         deriving (Eq, Show)

data Op = Add | Mul | Eq | Gtr
        deriving (Eq, Show)

------------------------------------------------------------------------
-- Given functions to support the interpreter...

lookUp :: (Eq a, Show a) => a -> [(a, b)] -> b
lookUp i table
  = fromMaybe (error ("lookup failed on identifier: " ++ show i))
              (lookup i table)

execFun :: Function -> [Int] -> State
execFun (name, args, p) vs
  = execBlock p (zip args vs)

------------------------------------------------------------------------
-- Part I

type State = [(Id, Int)]

update :: (Id, Int) -> State -> State
update (x, y) zs
  = (x, y) : (filter ((/=x) . fst) zs)

apply :: Op -> Int -> Int -> Int
apply op a b
  = (lookUp op ops) a b
  where
    ops = [(Add, (+)), (Mul, (*)), (Eq, eq), (Gtr, gtr)]
    eq  = (fromEnum .) . (==)
    gtr = (fromEnum .) . (>)

eval :: Exp -> State -> Int
-- Pre: the variables in the expression will all be bound in the given state
-- Pre: expressions do not contain phi instructions
eval (Const x) _        = x
eval (Var x) env        = lookUp x env
eval (Apply op x y) env = apply op (eval x env) (eval y env)
eval (Phi x y) env      = error "case not expected"

execStatement :: Statement -> State -> State
execStatement (Assign i e) s
  = update (i, eval e s) s
execStatement (If e b1 b2) s
  | eval e s == 1 = execBlock b1 s
  | otherwise     = execBlock b2 s
execStatement dw@(DoWhile b e) s
  | eval e ns == 1 = execStatement dw ns
  | otherwise      = ns
  where
    ns = execBlock b s

execBlock :: Block -> State -> State
execBlock bl st
  = foldl (flip execStatement) st bl
------------------------------------------------------------------------
-- Given function for testing propagateConstants...

-- Converts a function in SSA form into its optimised SSA form...
applyPropagate :: Function -> Function
applyPropagate (name, args, body)
  = (name, args, propagateConstants body)

------------------------------------------------------------------------
-- PART II

foldConst :: Exp -> Exp
-- Pre: the expression is in SSA form
foldConst (Phi (Const x) (Const y))
  | x == y = Const x
foldConst (Apply op (Const x) (Const y))
  = Const (apply op x y)
foldConst (Apply Add (Const 0) v@(Var y))
  = v
foldConst (Apply Add v@(Var x) (Const 0))
  = v
foldConst e
  = e


sub :: Id -> Int -> Exp -> Exp
-- Pre: the expression is in SSA form
sub i x (Var y)
  | i == y = Const x
sub i x (Apply op a b)
  = foldConst (Apply op (sub i x a) (sub i x b))
sub i x (Phi a b)
  = foldConst (Phi (sub i x a) (sub i x b))
sub _ _ e
  = e

-- Use (by uncommenting) any of the following, as you see fit...
type Worklist = [(Id, Int)]
scan :: Id -> Int -> Block -> (Worklist, Block)
scan v c b
  = foldr scan' ([], []) b
  where
    scan' (Assign "$return" e) (wl, b)
      = (wl, Assign "$return" (sub v c e) : b)
    scan' (Assign v' e) (wl, b)
      = scan'' (sub v c e)
      where
        scan'' (Const c')
          = ((v', c') : wl, b)
        scan'' e'
          = (wl, (Assign v' e') : b)
    scan' (If p q r) (wl, b)
      = (wl ++ wl' ++ wl'', If (sub v c p) q' r' : b)
      where
        (wl', q')  = scan v c q
        (wl'', r') = scan v c r
    scan' (DoWhile db p) (wl, b)
      = (wl ++ wl', DoWhile b' (sub v c p) : b)
      where
        (wl', b') = scan v c db

-- scan :: (Exp -> Exp) -> Block -> (Exp -> Exp, Block)


propagateConstants :: Block -> Block
-- Pre: the block is in SSA form
propagateConstants
    = prop . (scan "$INVALID" 0)
    where
      prop ([], bl)
        = bl
      prop ((v, c) : wl, bl)
        = prop (wl ++ wl', b')
        where
          (wl', b') = scan v c bl


------------------------------------------------------------------------
-- Given functions for testing unPhi...

-- Applies unPhi to a given function...
applyUnPhi :: Function -> Function
applyUnPhi (name, args, body)
  = (name, args, unPhi body)

-- Combines propagation/folding and unPhi to convert a function from its
-- unoptimised SSA form to a final non-SSA form...
optimise :: Function -> Function
optimise (name, args, body)
  = (name, args, unPhi (propagateConstants body))

------------------------------------------------------------------------
-- PART III

unPhi :: Block -> Block
-- Pre: the block is in SSA form
unPhi []
   = []
unPhi (If p q r : b)
   = If p q' r' : unPhi b'
   where
     q' = unPhi q ++ ass
     r' = unPhi r ++ ass'
     (ass, ass', b') = getPhiAssignments b
unPhi (DoWhile db p : b)
   = ass ++ (DoWhile db'' p : unPhi b)
   where
     db'' = unPhi db' ++ ass'
     (ass, ass', db') = getPhiAssignments db
unPhi (assign : b)
   = assign : unPhi b

getPhiAssignments (Assign v (Phi e e') : b)
   = (Assign v e : ass, Assign v e' : ass', unPhi b')
   where
     (ass, ass', b') = getPhiAssignments b
getPhiAssignments b
   = ([], [], b)

------------------------------------------------------------------------
-- Part IV

makeSSA :: Function -> Function
makeSSA
  = undefined


------------------------------------------------------------------------
-- Predefined functions for displaying functions and blocks...

opNames
  = [(Add, "+"), (Mul, "*"), (Eq, "=="), (Gtr, ">")]

precTable
  = [(Add, 1), (Mul, 2), (Eq, 0), (Gtr, 0)]

prec op
  = lookUp op precTable

showArgs []
  = ""
showArgs as
  = foldr1 (\a s -> a ++ (", " ++ s)) as

showExp :: Int -> Exp -> String
showExp _ (Const n)
  = show n
showExp _ (Var id)
  = id
showExp n (Apply op' e e')
  | n > n'    = "(" ++ s ++ ")"
  | otherwise = s
  where
    n' = prec op'
    s = showExp n' e ++ " " ++ fromJust (lookup op' opNames ) ++ " " ++
        showExp n' e'
showExp _ (Phi e e')
  = "PHI(" ++ showArgs (map (showExp 0) [e, e']) ++ ")"

showLine s n k
  =  putStrLn (show n ++ ": " ++ replicate (k + 2 - length (show n)) ' ' ++ s)

showBlock' b n
  = showBlock'' b n 2
  where
    showBlock'' :: Block -> Int -> Int -> IO Int
    showBlock'' [] n k
      = return n
    showBlock'' (s : b) n k
      = do n'  <- showStatement s n k
           n'' <- showBlock'' b n' k
           return n''
    showStatement (Assign id e) n k
      = do showLine (id ++ " = " ++ showExp 0 e) n k
           return (n + 1)
    showStatement (If p q []) n k
      = do showLine ("if " ++ "(" ++ showExp 0 p ++ ") {") n k
           n' <- showBlock'' q (n + 1) (k + 2)
           showLine "}" n' k
           return (n' + 1)
    showStatement (If p q r) n k
      = do showLine ("if " ++ "(" ++ showExp 0 p ++ ") {") n k
           n'  <- showBlock'' q (n + 1) (k + 2)
           showLine "} else {" n' k
           n'' <- showBlock'' r (n' + 1) (k + 2)
           showLine "}" n'' k
           return (n'' + 1)
    showStatement (DoWhile b p) n k
      = do showLine "do {" n k
           n' <- showBlock'' b (n + 1) (k + 2)
           showLine ("} while " ++ showExp 9 p) n' k
           return (n' + 1)

showFun :: Function -> IO()
showFun (name, args, body)
  = do putStrLn ("1:  " ++ name ++ "(" ++ showArgs args ++ ") {")
       n <- showBlock' body 2
       showLine "}" n 0

showBlock ::  Block -> IO()
showBlock b
  = do n <- showBlock' b 1
       return ()

------------------------------------------------------------------------
-- Example state and expressions for testing...

s1 :: State
s1 = [("x", 7), ("y", 8)]

e1, e2, e3, e4, e5 :: Exp
e1 = Var "x"
e2 = Apply Mul (Apply Add (Var "x") (Const 1)) (Var "y")
e3 = Phi (Const 2) (Const 2)
e4 = Apply Add (Const 0) (Var "x")
e5 = Apply Add (Var "a") (Var "x")

------------------------------------------------------------------------
-- Example functions...

-- Figure 1...
example :: Function
example
  = ("example",["x"],[Assign "a" (Const 1),Assign "b" (Apply Add (Var "x")
    (Const 2)),Assign "c" (Const 3),If (Apply Eq (Var "x") (Const 10))
    [Assign "a" (Const 1),Assign "c" (Const 5)] [],Assign "d"
    (Apply Add (Var "a") (Const 3)),Assign "e" (Apply Add (Var "d") (Var "b")),
    Assign "$return" (Apply Add (Var "e") (Var "c"))])

exampleSSA :: Function
exampleSSA
  = ("example",["x"],[Assign "a0" (Const 1),Assign "b0" (Apply Add (Var "x")
    (Const 2)),Assign "c0" (Const 3),If (Apply Eq (Var "x") (Const 10)) [Assign
    "a1" (Const 1),Assign "c1" (Const 5)] [],Assign "a2" (Phi (Var "a1") (Var
    "a0")),Assign "c2" (Phi (Var "c1") (Var "c0")),Assign "d0" (Apply Add (Var
    "a2") (Const 3)),Assign "e0" (Apply Add (Var "d0") (Var "b0")),
    Assign "$return" (Apply Add (Var "e0") (Var "c2"))])

exampleSSAPropagated :: Function
exampleSSAPropagated
  = ("example",["x"],[Assign "b0" (Apply Add (Var "x") (Const 2)),If (Apply Eq
    (Var "x") (Const 10)) [] [],Assign "c2" (Phi (Const 5) (Const 3)),
    Assign "e0" (Apply Add (Const 4) (Var "b0")),Assign "$return"
    (Apply Add (Var "e0") (Var "c2"))])

exampleOptimised :: Function
exampleOptimised
  = ("example",["x"],[Assign "b0" (Apply Add (Var "x") (Const 2)),If (Apply Eq
    (Var "x") (Const 10)) [Assign "c2" (Const 5)] [Assign "c2" (Const 3)],Assign
    "e0" (Apply Add (Const 4) (Var "b0")),Assign "$return" (Apply Add (Var "e0")
    (Var "c2"))])


-- Figure 2 (there is no SSA version of this)...
fact :: Function
fact
  = ("fact",
     ["n"],
     [If (Apply Eq (Var "n") (Const 0))
        [Assign "$return" (Const 1)]
        [Assign "prod" (Const 1),
         Assign "i" (Var "n"),
         DoWhile
           [Assign "prod" (Apply Mul (Var "prod") (Var "i")),
            Assign "i" (Apply Add (Var "i") (Const (-1)))
           ]
           (Apply Gtr (Var "i") (Const 0)),
         Assign "$return" (Var "prod")
        ]
     ]
    )


-- Summation loop, specialised loop for the case k=0...
loop :: Function
loop
  = ("loop",["n"],[Assign "i" (Var "n"),Assign "k" (Const 0),Assign "sum"
    (Const 0),If (Apply Eq (Var "i") (Const 0)) [Assign "$return" (Const 0)]
    [DoWhile [Assign "sum" (Apply Add (Var "sum") (Apply Mul (Apply Add
    (Var "i") (Apply Mul (Const 2) (Var "k"))) (Apply Add (Apply Add (Var "i")
    (Apply Mul (Const 2) (Var "k"))) (Const 1)))),Assign "i" (Apply Add
    (Var "i") (Const (-1)))] (Apply Gtr (Var "i") (Const 0)),
    Assign "$return" (Var "sum")]])

loopSSA :: Function
loopSSA
  = ("loop",["n"],[Assign "i0" (Var "n"),Assign "k0" (Const 0),Assign "sum0"
    (Const 0),If (Apply Eq (Var "i0") (Const 0)) [Assign "$return" (Const 0)]
    [DoWhile [Assign "sum1" (Phi (Var "sum0") (Var "sum2")),Assign "i1"
    (Phi (Var "i0") (Var "i2")),Assign "k1" (Apply Mul (Var "k0") (Const 2)),
    Assign "a0" (Apply Add (Var "i1") (Var "k1")),Assign "k2" (Apply Mul
    (Var "k0") (Const 2)),Assign "b0" (Apply Add (Var "k2") (Const 1)),
    Assign "b1" (Apply Add (Var "i1") (Var "b0")),Assign "m0" (Apply Mul
    (Var "a0") (Var "b1")),Assign "sum2" (Apply Add (Var "sum1") (Var "m0")),
    Assign "i2" (Apply Add (Var "i1") (Const (-1)))] (Apply Gtr (Var "i2")
    (Const 0)),Assign "$return" (Var "sum2")]])

loopSSAPropagated :: Function
loopSSAPropagated
  = ("loop",["n"],[Assign "i0" (Var "n"),If (Apply Eq (Var "i0") (Const 0))
    [Assign "$return" (Const 0)] [DoWhile [Assign "sum1" (Phi (Const 0) (Var
    "sum2")),Assign "i1" (Phi (Var "i0") (Var "i2")),Assign "a0" (Var "i1"),
    Assign "b1" (Apply Add (Var "i1") (Const 1)),Assign "m0" (Apply Mul
    (Var "a0") (Var "b1")),Assign "sum2" (Apply Add (Var "sum1") (Var "m0")),
    Assign "i2" (Apply Add (Var "i1") (Const (-1)))] (Apply Gtr (Var "i2")
    (Const 0)),Assign "$return" (Var "sum2")]])

loopOptimised :: Function
loopOptimised
  = ("loop",["n"],[Assign "i0" (Var "n"),If (Apply Eq (Var "i0") (Const 0))
    [Assign "$return" (Const 0)] [Assign "sum1" (Const 0),Assign "i1" (Var
    "i0"),DoWhile [Assign "a0" (Var "i1"),Assign "b1" (Apply Add (Var "i1")
    (Const 1)),Assign "m0" (Apply Mul (Var "a0") (Var "b1")),Assign "sum2"
    (Apply Add (Var "sum1") (Var "m0")),Assign "i2" (Apply Add (Var "i1")
    (Const (-1))),Assign "sum1" (Var "sum2"),Assign "i1" (Var "i2")]
    (Apply Gtr (Var "i2") (Const 0)),Assign "$return" (Var "sum2")]])


-- Basic block (no conditionals or loops)...
basicBlock :: Function
basicBlock
  = ("basicBlock",[],[Assign "x" (Const 1),Assign "y" (Const 2),Assign "x"
    (Apply Add (Var "x") (Var "y")),Assign "y" (Apply Mul (Var "x") (Const
    3)),Assign "$return" (Var "y")])

basicBlockSSA :: Function
basicBlockSSA
  = ("basicBlock",[],[Assign "x0" (Const 1),Assign "y0" (Const 2),Assign "x1"
    (Apply Add (Var "x0") (Var "y0")),Assign "y1" (Apply Mul (Var "x1") (Const
    3)),Assign "$return" (Var "y1")])

basicBlockSSAPropagated :: Function
basicBlockSSAPropagated
  = ("basicBlock", [], [Assign "$return" (Const 9)])

-- (This is the same as above, as there were no phi functions.)
basicBlockOptimised :: Function
basicBlockOptimised
  = ("basicBlock", [], [Assign "$return" (Const 9)])

-- Computes the maximum of two integers; useful for testing unPhi...
max2 :: Function
max2
  = ("max2",["x","y"],[If (Apply Gtr (Var "x") (Var "y")) [Assign "m" (Var "x")]
    [Assign "m" (Var "y")],Assign "$return" (Var "m")])

max2SSA :: Function
max2SSA
  = ("max2",["x","y"],[If (Apply Gtr (Var "x") (Var "y")) [Assign "m0" (Var
    "x")] [Assign "m1" (Var "y")],Assign "m2" (Phi (Var "m0") (Var "m1")),Assign
    "$return" (Var "m2")])

max2SSAPropagated :: Function
max2SSAPropagated
  = ("max2",["x","y"],[If (Apply Gtr (Var "x") (Var "y")) [Assign "m0" (Var
    "x")] [Assign "m1" (Var "y")],Assign "m2" (Phi (Var "m0") (Var "m1")),Assign
    "$return" (Var "m2")])

max2Optimised :: Function
max2Optimised
  = ("max2",["x","y"],[If (Apply Gtr (Var "x") (Var "y")) [Assign "m0" (Var
    "x"),Assign "m2" (Var "m0")] [Assign "m1" (Var "y"),Assign "m2" (Var
    "m1")],Assign "$return" (Var "m2")])
