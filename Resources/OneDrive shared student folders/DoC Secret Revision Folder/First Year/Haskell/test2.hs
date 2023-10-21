import Data.Char
import Data.Maybe
import Data.List

type Operator = Char

data Expr = Num Int | Var String | Op Operator | App Expr Expr Expr
            deriving (Eq,Ord,Show)

type Token = Expr

data Associativity = L | N | R
                     deriving (Eq,Ord,Show)

ops :: [Operator]
ops = "+-*/^()$"

type Precedence = Int

opTable :: [(Operator, (Precedence, Associativity))]
opTable = [('$',(0,N)), ('(',(1,N)), ('+',(6,L)), ('-',(6,L)), 
           ('*',(7,L)), ('/',(7,L)), ('^',(8,R)), (')',(1,N))]

type ExprStack = [Expr]

type OpStack = [Operator]

showExpr :: Expr -> String
showExpr (Num n) 
  = show n 
showExpr (Var s) 
  = s
showExpr (Op c)  
  = [c]
showExpr (App op e e') 
  = "(" ++ showExpr e ++ showExpr op ++ showExpr e' ++ ")"

--------------------------------------------

--
-- Assume throughout that all function arguments are valid, for example:
--   All input expressions are well-formed
--   All Operators (Chars) are members of 'ops' above
--   The stacks passed to buildOpApp and parse constitute valid `state'
--   with respect to the Shunting Yard algorithm
--


-------------------------------------------------------------------
precedence :: Operator -> Precedence
precedence o = p
  where
    (p , a) = fromJust (lookup o opTable)
 
associativity :: Operator -> Associativity
associativity o = a
  where
    (p , a) = fromJust (lookup o opTable)
    
higherPrecedence :: Operator -> Operator -> Bool
higherPrecedence o1 o2 = precedence o1 > precedence o2

eqPrecedence :: Operator -> Operator -> Bool
eqPrecedence o1 o2 = precedence o1 == precedence o2

isRightAssociative :: Operator -> Bool
isRightAssociative o = associativity o == R

supersedes :: Operator -> Operator -> Bool
supersedes o1 o2 = (higherPrecedence o1 o2) || (eqPrecedence o1 o2 && isRightAssociative o1)

stringToInt :: String -> Int
stringToInt s = read s :: Int
--stringToInt s = foldl (\i j-> i * 10 + j) 0 ns
--  where
--    ns = map (\c -> ord c - ord '0') s
  
tokenise :: String -> [Token]
tokenise [] = []
tokenise all @ (x : xs)
  | isSpace x            = tokenise xs
  | elem x ops           = Op x : tokenise xs
  | '0' <= x && x <= '9' = Num (stringToInt n) : tokenise rest
  | otherwise            = Var n : tokenise rest
    where
      (n , rest) = break breakPoint all
   
      breakPoint :: Char -> Bool
      breakPoint c = isSpace c || elem c ops
 
buildExpr :: String -> Expr
buildExpr s = parse (tokenise s) ([], ['$'])

buildOpApp :: (ExprStack, OpStack) -> (ExprStack, OpStack)
buildOpApp ((exp1 : exp2 : expRest) , tokes @ (op : opRest))
  = ((App (head (tokenise tokes)) (exp2) (exp1)) : exp' , op')
  where
    (exp' , op') = buildOpApp (expRest , opRest)
buildOpApp (exp , op) = (exp , op)

parse :: [Token] -> (ExprStack, OpStack) -> Expr
parse [] ([x], _) = x
parse [] stack    = parse [] (buildOpApp stack)
parse t @ (Op o : rest) stack @ (expStack , opStack @ (op : ops))
  | o == '(' || o == ')'    = parse rest (buildOpApp stack) --This is for last exercise
  | supersedes o op         = parse rest (expStack , o : opStack)
  | otherwise               = parse t (buildOpApp stack)
parse (t : rest) (exp , op) = parse rest (t : exp , op)
