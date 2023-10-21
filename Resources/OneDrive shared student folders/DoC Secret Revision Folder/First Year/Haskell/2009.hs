import Data.List
import Data.Maybe

type Index = Int

data BExp = Prim Bool | IdRef Index | Not BExp | And BExp BExp | Or BExp BExp
            deriving (Eq, Ord, Show)

type Env = [(Index, Bool)]

type NodeId = Int

type BDDNode =  (NodeId, (Index, NodeId, NodeId))

type BDD = (NodeId, [BDDNode])

------------------------------------------------------
-- PART I

-- Pre: The item is in the given table
lookUp :: Eq a => a -> [(a, b)] -> b
lookUp x ys
  = fromJust(lookup x ys) 

checkSat :: BDD -> Env -> Bool
checkSat (0, nodes) env
  = False
checkSat (1, nodes) env
  = True
checkSat (id, nodes) env
  | goRight   = checkSat (rightId, nodes) env
  | otherwise = checkSat (leftId, nodes) env
  where
    (index, leftId, rightId) = lookUp id nodes
    goRight                  = lookUp index env


sat :: BDD -> [[(Index, Bool)]]
sat bdd 
  = sat' bdd [] 
  where
    sat' :: BDD -> [(Index, Bool)] -> [[(Index, Bool)]]
    sat' (0, nodes) env
      = []
    sat' (1, nodes) env
      = [env]
    sat' (id, nodes) env 
      = sat' (leftId, nodes) ((index, False) : env) ++ sat' (rightId, nodes) ((index, True) : env)
      where
        (index, leftId, rightId) = lookUp id nodes

------------------------------------------------------
-- PART II

simplify :: BExp -> BExp
simplify (Not (Prim x))
  = Prim (not x)
simplify (Or (Prim x) (Prim y))
  = Prim (x || y)
simplify (And (Prim x) (Prim y))
  = Prim (x && y)
simplify exp
  = exp

restrict :: BExp -> Index -> Bool -> BExp
restrict (Prim x) _ _
  = (Prim x)
restrict (IdRef x) id bool
  | x == id   = (Prim bool)
  | otherwise = IdRef x
restrict (Not x) id bool
  = simplify (Not (restrict x id bool))
restrict (And x y) id bool
  = simplify (And (restrict x id bool) (restrict y id bool))
restrict (Or x y) id bool
  = simplify (Or (restrict x id bool) (restrict y id bool))

------------------------------------------------------
-- PART III

-- Pre: Each variable index in the BExp appears exactly once
--     in the Index list; there are no other elements
-- The question suggests the following definition (in terms of buildBDD')
-- but you are free to implement the function differently if you wish.
buildBDD :: BExp -> [Index] -> BDD
buildBDD e xs
  = buildBDD' e 2 xs

-- Potential helper function for buildBDD which you are free
-- to define/modify/ignore/delete/embed as you see fit.
buildBDD' :: BExp -> NodeId -> [Index] -> BDD
buildBDD' e _ []
  | e == Prim False = (0, [])
  | e == Prim True  = (1, [])
buildBDD' e n (x : xs)
  = (n, (n, (x, leftId, rightId)) : leftNodes ++ rightNodes)
  where 
    (leftId, leftNodes)   = buildBDD' (restrict e x False) (2 * n) xs
    (rightId, rightNodes) = buildBDD' (restrict e x True) (2 * n + 1) xs

------------------------------------------------------
-- PART IV

-- Pre: Each variable index in the BExp appears exactly once
--      in the Index list; there are no other elements
buildROBDD :: BExp -> [Index] -> BDD
buildROBDD exp idxs
  = (root, reverse (curate list))
  where
    (root, list) = buildROBDD' exp 2 idxs []

-- LeftId' and rightId' are the original IDs if they are new, or 
-- the ID of a previous node if it was already constructed.
-- The find functions are obnoxiously long, a length reduction 
-- using the predefined 'find' would be extremely helpful
buildROBDD' :: BExp -> NodeId -> [Index] -> [BDDNode] -> BDD
buildROBDD' exp _ [] _
  | exp == (Prim True)
    = (1, [])
  | otherwise
    = (0, [])
buildROBDD' exp nodeId (nextId : ids) list
  | leftId' == rightId'
    = (leftId', leftList)
  | otherwise
    = (nodeId, nub ((nodeId, (nextId, leftId', rightId')) : (leftList ++ rightList)))
  where
    exp1 = restrict exp nextId False
    exp2 = restrict exp nextId True
    (leftId, leftList) = buildROBDD' exp1 (2 * nodeId) ids list
    (rightId, rightList) = buildROBDD' exp2 (2 * nodeId + 1) ids (leftList ++ list)
    leftId' = findLeft leftId
    rightId' = findRight rightId
    findLeft id
      | id <= 1 = id
      | maybe == Nothing
        = id
      | otherwise
        = fromJust maybe
      where
        maybe = findin (leftId, lookUp leftId leftList) list
    findRight id
      | id <= 1 = id
      | maybe == Nothing
        = id
      | otherwise
        = fromJust maybe
      where
        maybe = findin (rightId, lookUp rightId rightList) (leftList ++ list)
    findin _ [] = Nothing
    findin node (h : t)
      | sameChilds node list h = Just (fst h)
      | otherwise = findin node t

-- Given 2 nodes, checks if they have the same subtrees 
-- (Check can't be made based on IDs only because every node
-- has a different ID)
sameChilds :: BDDNode -> [BDDNode] -> BDDNode  -> Bool
sameChilds (n1, (i1, l1, r1)) list' (n2, (i2, l2, r2))
  | n1 == n2 = True
  | n1 /= n2 && n1 <= 1 && n2 <= 1 = False
  | i1 /= i2 = False
  | l1 == l2 && r1 == r2 = True
  | minimum [l1, l2, r1, r2] <= 1 = False
  | otherwise = (sameChilds (l1, (lookUp l1 list')) list' (l2, (lookUp l2 list'))) && 
                (sameChilds (r1, (lookUp r1 list'))list' (r2, (lookUp r2 list')) ) 

-- This function removes some extra nodes that are unreachable (don't ask me
-- how they are in there, but sometimes they are)
curate :: [BDDNode] -> [BDDNode]
curate l
  = curate' l [fst (head l)] []
  where
    curate' :: [BDDNode] -> [Int] -> [BDDNode] -> [BDDNode]
    curate' [] _ sol
      = sol
    curate' (n@(id, (_, l, r)) : t) ids sol
      | elem id ids = curate' t (l : r : ids) (n : sol)
      | otherwise = curate' t ids sol

root :: (Int, Int, Int) -> Int
root (x, _, _) = x
------------------------------------------------------
-- Examples for testing...

b1, b2, b3, b4, b5, b6, b7, b8 :: BExp
b1 = Prim False
b2 = Not (And (IdRef 1) (Or (Prim False) (IdRef 2)))
b3 = And (IdRef 1) (Prim True)
b4 = And (IdRef 7) (Or (IdRef 2) (Not (IdRef 3)))
b5 = Not (And (IdRef 7) (Or (IdRef 2) (Not (IdRef 3))))
b6 = Or (And (IdRef 1) (IdRef 2)) (And (IdRef 3) (IdRef 4))
b7 = Or (Not (IdRef 3)) (Or (IdRef 2) (Not (IdRef 9)))
b8 = Or (IdRef 1) (Not (IdRef 1))

bdd1, bdd2, bdd3, bdd4, bdd5, bdd6, bdd7, bdd8 :: BDD
bdd1 = (0,[])
bdd2 = (2,[(4,(2,1,1)),(5,(2,1,0)),(2,(1,4,5))])
bdd3 = (5,[(5,(1,0,1))])
bdd4 = (2,[(2,(2,4,5)),(4,(3,8,9)),(8,(7,0,1)),(9,(7,0,0)),
           (5,(3,10,11)),(10,(7,0,1)),(11,(7,0,1))])
bdd5 = (3,[(4,(3,8,9)),(3,(2,4,5)),(8,(7,1,0)),(9,(7,1,1)),
           (5,(3,10,11)),(10,(7,1,0)),(11,(7,1,0))])
bdd6 = (2,[(2,(1,4,5)),(4,(2,8,9)),(8,(3,16,17)),(16,(4,0,0)),
           (17,(4,0,1)),(9,(3,18,19)),(18,(4,0,0)),(19,(4,0,1)),
           (5,(2,10,11)),(10,(3,20,21)),(20,(4,0,0)),(21,(4,0,1)),
           (11,(3,22,23)),(22,(4,1,1)),(23,(4,1,1))])
bdd7 = (6,[(6,(2,4,5)),(4,(3,8,9)),(8,(9,1,1)),(9,(9,1,0)),
           (5,(3,10,11)),(10,(9,1,1)),(11,(9,1,1))])
bdd8 = (2,[(2,(1,1,1))])

