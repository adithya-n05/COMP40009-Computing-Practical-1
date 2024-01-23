module HsFin2021 where

--------------------------------------------------------------------------------
-- Fawwaz Abdullah, ffa20                                                     --
--                                                                            --
-- Solutions to the "Graph Colouring and Register Allocation" past Haskell    --
-- exam paper.                                                                --
--                                                                            --
-- Originally taken on 2021/01.                                               --
--                                                                            --
-- NOTE: Rename file to HsFin2021.hs before running to stop GHC from yelling. --
--------------------------------------------------------------------------------

-- Original imports here.
import Data.Maybe
import Data.List

-- Added imports for fixed versions here.
import Control.Monad
import Control.Monad.Trans.State.Lazy
import Data.Map ( Map )
import qualified Data.Map as M
import Data.Tuple ( swap )
import Debug.Trace

--------------------------------------------------------------------------------
-- DISCLAIMER I:                                                              --
--                                                                            --
-- Below is my untouched original solution submitted to the exam. There will  --
-- be another banner somewhere below where working solutions will be placed   --
-- as the original solutions are somewhat flawed and have output errors.      --
--------------------------------------------------------------------------------

------------------------------------------------------
--
-- Part I
--
count :: Eq a => a -> [a] -> Int
count item list
  = length [i | i <- list, i == item]

degrees :: Eq a => Graph a -> [(a, Int)]
degrees (nodes, edges)
  = [(n, length [e | e@(l, r) <- edges, n == l || n == r]) | n <- nodes]

neighbours :: Eq a => a -> Graph a -> [a]
neighbours node (nodes, edges)
  | node `notElem` nodes
    = []
  | otherwise
    = [ if node == l' then r' else l'
      | (l', r') <- [e | e@(l, r) <- edges, node == l || node == r]
      ]

removeNode :: Eq a => a -> Graph a -> Graph a
removeNode node (nodes, edges)
  = (filter (/= node) nodes, [e | e@(l, r) <- edges, node /= l && node /= r])

------------------------------------------------------
--
-- Part II
--
colourGraph :: (Ord a, Show a) => Int -> Graph a -> Colouring a
colourGraph _ ([], _)
  = []
colourGraph colours graph
  = (minNode, findColour) : coloured
    where
      (minNode, _)
        = minimumBy (\(_, d) (_, d') -> compare d d') $ degrees graph
      coloured
        = colourGraph colours (removeNode minNode graph)
      
      findColour :: Colour
      findColour
        = minColour
          where
            neighbours'
              = neighbours minNode graph
            neighbourColours
              = map snd $ filter (flip elem neighbours' . fst) coloured
            minColour
              = let available = [ i
                                | i <- [1 .. colours]
                                , i `notElem` neighbourColours
                                ]
                in if null available then 0 else head available

------------------------------------------------------
--
-- Part III
--
buildIdMap :: Colouring Id -> IdMap
buildIdMap []
  = [("return", "return")]
buildIdMap ((id, colour) : colourings)
  | colour == 0    = (id, id) : buildIdMap colourings
  | otherwise      = (id, 'R' : show colour) : buildIdMap colourings

buildArgAssignments :: [Id] -> IdMap -> [Statement]
-- Pre: Every argument must have a key-value pair in the id map
buildArgAssignments arguments idMap
  = [ let ids = filter ((== arg) . fst) idMap
      in Assign (snd $ head ids) (Var arg)
    | arg <- arguments
    ]

renameExp :: Exp -> IdMap -> Exp
renameExp c@(Const _) _
  = c
renameExp v@(Var v') idMap
  = let mapping = filter (\(k, _) -> k == v') idMap
    in if null mapping then v else Var (snd $ head mapping)
renameExp (Apply op exp exp') idMap
  = Apply op (renameExp exp idMap) (renameExp exp' idMap)

renameId :: Id -> IdMap -> Id
renameId id idMap
  = let mapping = filter (\(k, _) -> k == id) idMap
    in if null mapping then id else snd $ head mapping

renameStatement :: Statement -> IdMap -> Statement
renameStatement (Assign id exp) idMap
  = Assign (renameId id idMap) (renameExp exp idMap)
renameStatement (If exp blk blk') idMap
  = If (renameExp exp idMap) (renameBlock blk idMap) (renameBlock blk' idMap)
renameStatement (While exp blk) idMap
  = While (renameExp exp idMap) (renameBlock blk idMap)

renameBlock :: Block -> IdMap -> Block
renameBlock blk idMap
  = filter (not . isRedundantAssign) (map (`renameStatement` idMap) blk)
    where
      isRedundantAssign :: Statement -> Bool
      isRedundantAssign (Assign id (Var id'))
        = id == id'
      isRedundantAssign _
        = False

renameFun :: Function -> IdMap -> Function
renameFun (f, as, b) idMap
  = (f, as, buildArgAssignments as idMap ++ renameBlock b idMap)

-----------------------------------------------------
--
-- Part IV
--
buildIG :: [[Id]] -> IG
buildIG idLists
  = (ids, nub $ concat edges)
    where
      ids         = nub $ concat idLists
      liveVarSets = filter ((>= 2) . length) idLists
      edges       = [ [ (liveVars !! i, liveVars !! j)
                      | i <- [0 .. length liveVars - 1]
                      , j <- [i + 1 .. length liveVars - 1]
                      ]
                    | liveVars <- liveVarSets
                    ]

-----------------------------------------------------
--
-- Part V
--

{-
 - Notes:
 - > A CFG in list form has the same number of items as lines in a function's
 -   block. This can be used to our advantage.
 - > Transforming a function into its interference graph is most likely a good
 -   intermediate when turning it into its CFG. We can first find the live
 -   variables for each graph and turn that into an interference graph using
 -   buildIG.
 -}

type CFGEntry = ((Id, [Id]), [Int])

liveVars :: CFG -> [[Id]]
liveVars cfg
  = [ filter keep (liveAtLine line cfg [])
    | line <- cfg
    ]
    where
      liveAtLine :: CFGEntry -> CFG -> [Int] -> [Id]
      liveAtLine ((def, use), []) _ _
        = use \\ [def]
      liveAtLine ((def, use), succs) cfg visited
        = nub (
            ( use ++
              concat
                [ liveAtLine (cfg !! scc) cfg (scc : visited)
                | scc <- succs \\ visited
                ]
            ) \\ [def]
          )

keep :: Id -> Bool
keep "_"      = False
keep "return" = False
keep _        = True

{-
 - Unnecessary.
 - buildFuncIG :: Function -> IG
 - buildFuncIG (_, _, blk)
 -   = bfig blk
 -     where
 -       bflv :: Block -> [[Id]]
 -       bflv []
 -         = []
 -       bflv ((Assign id exp) : ss)
 -         = (id : grabbed exp) : bflv ss
 -       bflv ((If exp b b') : ss)
 -         = grabbed exp : bflv b ++ bflv b' ++ bflv ss
 -       bflv ((While exp b) : ss)
 -         = grabbed exp : bflv b ++ bflv ss
 -
 -       bfig :: Block -> IG
 -       bfig b
 -         = buildIG $ [filter keep $ nub ids | ids <- bflv b]
 -}

grabbed :: Exp -> [Id]
grabbed (Const _)      = []
grabbed (Var v)        = [v]
grabbed (Apply _ e e') = grabbed e ++ grabbed e'

blockLength :: Block -> Int
blockLength []
  = 0
blockLength (s : ss)
  = case s of
    Assign _ _ -> 1 + blockLength ss
    If _ b b'  -> 1 + blockLength b + blockLength b' + blockLength ss
    While _ b  -> 1 + blockLength b + blockLength ss

purgeReturnSuccs :: CFGEntry -> CFGEntry
purgeReturnSuccs (("return", u), _)
  = (("return", u), [])
purgeReturnSuccs cfge
  = cfge

fixCondCFG :: CFG -> Int -> CFG
fixCondCFG [] _
  = []
fixCondCFG condBlockCFG exitLine
  = let (blkInit, succs) = splitAt (length condBlockCFG - 1) condBlockCFG
    in case succs of
      []           -> blkInit
      [(vs, sccs)] -> blkInit ++ [(vs, exitLine : sccs)]
      _            -> error "This should be unreachable."

buildCFG :: Function -> CFG
buildCFG (_, _, blk)
  = map purgeReturnSuccs $ bfcfg blk 0
    where
      bfcfg :: Block -> Int -> CFG
      bfcfg [] _
        = []
      bfcfg ((Assign id exp) : ss) lineNum
        = ((id, nub $ grabbed exp), succs) :
          bfcfg ss (succ lineNum)
          where
            succs
              | id == "return" = []
              | otherwise      = [succ lineNum | not (null ss)]
      bfcfg ((If exp b b') : ss) lineNum
        = ( ("_", nub $ grabbed exp),
            let s1 = [succ lineNum | not (null b)]
                s2 =
                  if null b'
                  then [exitLine | not (null b)]
                  else [succ lineNum + blockLength b]
            in s1 ++ s2
          ) :
          fixCondCFG (bfcfg b (succ lineNum)) exitLine ++
          fixCondCFG (bfcfg b' (succ lineNum + blockLength b)) exitLine ++
          bfcfg ss exitLine
          where
            exitLine = succ lineNum + blockLength b + blockLength b'
      bfcfg ((While exp b) : ss) lineNum
        = ( ("_", nub $ grabbed exp),
            if null b
            then [lineNum]
            else [succ lineNum, succ lineNum + blockLength b]
          ) : fixCondCFG (bfcfg b (succ lineNum)) lineNum ++
          bfcfg ss (succ lineNum + blockLength b)

--------------------------------------------------------------------------------
-- DISCLAIMER II:                                                             --
--                                                                            --
-- Below are some fixed solutions written on 2023/01/07.                      --
-- All functions will be suffixed with "Fx" to indicate that they are fixed.  --
--------------------------------------------------------------------------------

------------------------------------------------------
--
-- Part I
--
countFx :: Eq a => a -> [a] -> Int
-- The original implementation was O(2n). This is now O(n).
countFx item = go 0
  where
    go cnt [] = cnt
    go cnt (x : xs)
      | x == item = go (cnt + 1) xs
      | otherwise = go cnt xs

degreesFx :: Eq a => Graph a -> [(a, Int)]
-- The fold family of functions exists for this reason!
degreesFx (nodes, edges) =
  [ (n, foldr (\ (l, r) d -> if n `elem` [l, r] then d + 1 else d) 0 edges)
  | n <- nodes
  ]

cond :: a -> [(Bool, a)] -> a
-- Similar to "cond" in Lisps, where the first condition that returns true
-- will return the result of its body.
-- This does not need extra syntax due to Haskell's laziness.
cond def [] = def
cond def ((c, b) : cs)
  | c         = b
  | otherwise = cond def cs

neighboursFx :: Eq a => a -> Graph a -> [a]
-- I had a redundant check for non-membership of the node.
-- Using concatMap also removes the awkward double equality checking in my
-- original list comprehension method.
neighboursFx node (nodes, edges)
  = concatMap
    (\ (l, r) -> cond []
      [ (node == l, [r])
      , (node == r, [l])
      ])
    edges

removeNodeFx :: Eq a => a -> Graph a -> Graph a
-- \\ exists as a function. D'oh!
removeNodeFx node (nodes, edges) =
  ( nodes \\ [node]
  , filter (\ (l, r) -> l /= node && r /= node) edges
  )

------------------------------------------------------
--
-- Part II
--
colourGraphFx :: (Ord a, Show a) => Int -> Graph a -> Colouring a
-- Oh boy. Imagine having a redundant function.
colourGraphFx _ ([], _)
  = []
colourGraphFx colours graph
  = (minNode, minColour) : rest
    where
      (minNode, _)
        = minimumBy (\ (_, d) (_, d') -> compare d d') (degreesFx graph)

      rest
        = colourGraphFx colours (removeNodeFx minNode graph)

      neighbours'
        = neighboursFx minNode graph

      neighbourColours
        = [col | (ng, col) <- rest, ng `elem` neighbours']

      minColour
        = fromMaybe 0 (find (`notElem` neighbourColours) [1 .. colours])

------------------------------------------------------
--
-- Part III
--
buildIdMapFx :: Colouring Id -> IdMap
-- A bit more concise than the original.
buildIdMapFx colourings =
  [ (id, if colour == 0 then id else 'R' : show colour)
  | (id, colour) <- colourings
  ] ++ [("return", "return")]

buildArgAssignmentsFx :: [Id] -> IdMap -> [Statement]
-- Imagine adding a pre-condition that doesn't even exist.
-- Also, Maybe can interact with do-notation!
-- If you're wondering why, Maybe is a Monad too, just like IO!
buildArgAssignmentsFx arguments idMap
  = concatMap
    (\ arg -> fromMaybe [] (do
      id' <- lookup arg idMap
      return [Assign id' (Var arg)]))
    arguments

renameExpFx :: Exp -> IdMap -> Exp
-- lookup exists for a reason!
renameExpFx v@(Var v') idMap
  = maybe v Var (lookup v' idMap)
renameExpFx (Apply op exp exp') idMap
  = Apply op (renameExpFx exp idMap) (renameExpFx exp' idMap)
renameExpFx c@(Const _) _
  = c

renameIdFx :: Id -> IdMap -> Id
-- lookup exists for a- wait a minute I said that already.
renameIdFx id idMap
  = fromMaybe id (lookup id idMap)

renameWith :: IdMap -> Statement -> Statement
renameWith idMap (Assign id exp)
  = Assign (renameIdFx id idMap) (renameExpFx exp idMap)
renameWith idMap (If exp blk blk')
  = If (renameExpFx exp idMap) (renameBlockFx blk idMap) (renameBlockFx blk' idMap)
renameWith idMap (While exp blk)
  = While (renameExpFx exp idMap) (renameBlockFx blk idMap)

renameBlockFx :: Block -> IdMap -> Block
renameBlockFx blk idMap
  = filter (not . isRedundantAssign) (map (renameWith idMap) blk)
    where
      isRedundantAssign (Assign id (Var id'))
        = id == id'
      isRedundantAssign _
        = False

-- renameFun was already good enough.

-----------------------------------------------------
--
-- Part IV
--
combs :: [a] -> [(a, a)]
-- This list generates the pairwise combinations of a list.
-- Note that unlike the mathematical definition, this function does not care
-- whether the list already contains duplicates or not.
combs []       = []
combs (x : xs) = zip (repeat x) xs ++ combs xs

buildIGFx :: [[Id]] -> IG
-- Just make a helper.
buildIGFx idLists
  = (ids, nub $ concatMap combs lvss)
    where
      ids   = nub (concat idLists)
      lvss  = filter ((>= 2) . length) idLists

-----------------------------------------------------
--
-- Part V - TODO: VERY INCOMPLETE AND BROKEN!
--
generateLineMap :: Block -> Map Int Statement
-- The State Monad is generally useful for faking single-variables.
generateLineMap blk
  = evalState (enumBlock blk) 0
    where
      enumBlock :: Block -> State Int (Map Int Statement)
      enumBlock []         = return M.empty
      enumBlock (st : sts) = do
        mst  <- enumStatement st
        rest <- enumBlock sts
        return (mst `M.union` rest)

      nextLine :: State Int Int
      nextLine = get <* modify (1 +)

      enumStatement :: Statement -> State Int (Map Int Statement)
      enumStatement st = do
        line <- nextLine

        case st of
          Assign _ _ -> return (M.singleton line st)
          While _ b  -> (M.singleton line st `M.union`) <$> enumBlock b
          If _ tb fb -> do
            tbm <- enumBlock tb
            fbm <- enumBlock fb
            return (M.singleton line st `M.union` tbm `M.union` fbm)

defUse :: Statement -> (Id, [Id])
defUse st = case st of
  Assign d exp -> (d, findInExp exp)
  If cond _ _  -> ("_", findInExp cond)
  While cond _ -> ("_", findInExp cond)
  where
    findInExp (Var id)      = [id]
    findInExp (Apply _ l r) = findInExp l ++ findInExp r
    findInExp (Const _)     = []

buildCFGFx :: Function -> CFG
buildCFGFx (_, _, b) = descend b [] []
  where
    -- We need to keep track of loop locations in order to know if we need to
    -- loop at the end of a block.
    descend [] _ _
      = []
    descend (s : ss) loopStk retStk
      -- Ifs are special. They either:
      --   - Contain both cases. Then it can either go to [T, F, next].
      --   - Contain only the true case. Then it can either go to [T, next].
      = let anl@((d, u), nxt) = analyseStatement s
            Just line         = lookup s lmap'
            anl'              = if   null ss
                                then ( (d, u)
                                     , nxt
                                     ++ fromMaybe [] (sequence [head' loopStk])
                                     ++ fromMaybe [] (sequence [head' retStk])
                                     )
                                else ( (d, u)
                                     , nxt ++ fromMaybe
                                         []
                                         (sequence [lookup (head ss) lmap'])
                                     )
            pnext = fromMaybe [] (sequence [head' ss >>= (`lookup` lmap')])
        in  case s of
          Assign _ _ -> anl' : descend ss loopStk retStk
          While _ b  -> anl'
            : descend b (line : loopStk) (pnext ++ retStk)
            ++ descend ss loopStk retStk
          If _ tb fb -> anl'
            : descend tb loopStk (pnext ++ retStk)
            ++ descend fb loopStk (pnext ++ retStk)
            ++ descend ss loopStk retStk

    analyseStatement st = case st of
      Assign _ _ -> (defUse st, [])
      If _ tb fb ->
        ( defUse st
        , concatMap
          (\ b -> fromMaybe [] (sequence [head' b >>= (`lookup` lmap')]))
          [tb, fb]
        )
      While _ b  ->
        ( defUse st
        , fromMaybe [] (sequence [head' b >>= (`lookup` lmap')])
        )

    head' []      = Nothing
    head' (x : _) = Just x

    lmap  = generateLineMap b
    lmap' = map swap (M.assocs lmap)

--------------------------------------------------------------------------------
-- Below this banner is code copied from Types.hs and Examples.hs, for easier --
-- downloading and usage of this file.                                        --
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- TYPES                                                                      --
--------------------------------------------------------------------------------

type Id = String

data Op = Add | Mul | Eq | GEq
        deriving (Eq, Show)

data Exp = Const Int | Var Id | Apply Op Exp Exp 
         deriving (Eq, Show)

data Statement = Assign Id Exp |
                 If Exp Block Block |
                 While Exp Block
               deriving (Eq, Show)

type Block = [Statement]

type Function = (Id, [Id], Block)


type Edge a = (a, a)

type Graph a = ([a], [Edge a])

type IG = Graph Id

type Colour = Int 

type Colouring a = [(a, Colour)]

type IdMap = [(Id, Id)]


type CFG = [((Id, [Id]), [Int])]

------------------------------------------------------------------------
-- Predefined functions...

lookUp :: (Eq a, Show a, Show b) => a -> [(a, b)] -> b
lookUp x t
  = fromMaybe (error ("LookUp failed\n  Search key = " ++ show x ++
                      "\n  Table = " ++ show t))
              (lookup x t)

sortGraph :: Ord a => Graph a -> Graph a
sortGraph (ns, es) 
  = (sort ns, sort (map order es))
  where
    order (n, n') = (min n n', max n n')

sortCFG :: CFG -> CFG
sortCFG cfg
  = [((d, sort u), sort ss) | ((d, u), ss) <- cfg]

opNames
  = [(Add, "+"), (Mul, "*"), (Eq, "=="), (GEq, ">=")]

precTable
  = [(Add, 1), (Mul, 2), (Eq, 0), (GEq, 0)]

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

showLine :: String -> Maybe Int -> Int -> IO ()
showLine s n k
  =  putStrLn (label ++ replicate (k + 3 - length label) ' ' ++ s)
  where label = case n of
          Just n -> show n ++ ": "
          Nothing -> ""

showBlock' b n offset
  = showBlock'' b n offset
  where
    showBlock'' :: Block -> Int -> Int -> IO Int
    showBlock'' [] n k
      = return n
    showBlock'' (s : b) n k
      = do n'  <- showStatement s n k
           n'' <- showBlock'' b n' k
           return n''
    showStatement (Assign id e) n k
      = do showLine (id ++ " = " ++ showExp 0 e ++ ";") (Just n) k
           return (n + 1)
    showStatement (If p q []) n k
      = do showLine ("if " ++ "(" ++ showExp 0 p ++ ") {") (Just n) k
           n' <- showBlock'' q (n + 1) (k + 2)
           showLine "}" Nothing k
           return n'
    showStatement (If p q r) n k
      = do showLine ("if " ++ "(" ++ showExp 0 p ++ ") {") (Just n) k
           n'  <- showBlock'' q (n + 1) (k + 2)
           showLine "} else {" Nothing k
           n'' <- showBlock'' r n' (k + 2)
           showLine "}" Nothing k
           return n''
    showStatement (While p b) n k
      = do showLine ("while " ++ showExp 9 p ++ " {") (Just n) k
           n' <- showBlock'' b (n + 1) (k + 2)
           showLine "}" Nothing k
           return n'

showFun :: Function -> IO()
showFun (name, args, body)
  = do putStrLn ("   fun " ++ name ++ "(" ++ showArgs args ++ ") {")
       n <- showBlock' body 0 2
       showLine "}" Nothing 0

showBlock ::  Block -> IO()
showBlock b
  = do n <- showBlock' b 0 0
       return ()

--------------------------------------------------------------------------------
-- EXAMPLES                                                                   --
--------------------------------------------------------------------------------

------------------------------------------------------------------
-- Example graphs from the spec...

fig1Left :: Graph Int
fig1Left 
  = ([1,2,3,4],[(1,2),(1,3),(2,4),(3,4)])

fig1Middle:: Graph Int
fig1Middle 
  = ([1,2,3,4],[(1,2),(1,3),(1,4),(2,4),(3,4)])

------------------------------------------------------------------
-- Example expressions...

e1, e2 :: Exp
e1 
  = Apply Add (Var "a") (Var "b")
e2
  = Apply Mul (Apply Add (Var "x") (Const 2)) (Var "y")

------------------------------------------------------------------
-- Example Id map...

idMap1 :: IdMap
idMap1
  = [("a", "a"),("b", "R1"),("y","R1"),("x","R6")]

------------------------------------------------------------------
-- Example functions and associated structures from the spec...

-- Factorial...
fact :: Function
fact
  = ("fact",
     ["n"],
     [If (Apply Eq (Var "n") (Const 0))
         [Assign "return" (Const 1)]
         [Assign "prod" (Const 1),
          Assign "i" (Var "n"),
          While (Apply GEq (Var "i") (Const 1))
                [Assign "prod" (Apply Mul (Var "prod") (Var "i")),
                 Assign "i" (Apply Add (Var "i") (Const (-1)))
                ],
          Assign "return" (Var "prod")
         ]
     ]
    )

factB :: Block
factB 
  = let (_, _, b) = fact in b

factIG :: IG
factIG
  = (["i","n","prod"],[("i","prod"),("n","prod")])

-- Assumes two registers, [1,2]...
factColouring :: Colouring Id
factColouring
  = [("i",2),("n",2),("prod",1)] 

factIdMap :: IdMap
factIdMap
  = [("return","return"),("i","R2"),("n","R2"),("prod","R1")] 

-- Assuming two registers, [1,2]...
factTransformed :: Function
factTransformed
  = ("fact",["n"],
     [Assign "R2" (Var "n"),
      If (Apply Eq (Var "R2") (Const 0))
         [Assign "return" (Const 1)] 
         [Assign "R1" (Const 1),
          While (Apply GEq (Var "R2") (Const 1)) 
                [Assign "R1" (Apply Mul (Var "R1") (Var "R2")),
                 Assign "R2" (Apply Add (Var "R2") (Const (-1)))
                ],
          Assign "return" (Var "R1")
         ]
     ])

factLiveVars :: [[Id]]
factLiveVars
  = [["n"],[],["n"],["n","prod"],["i","prod"],["i","prod"],
     ["i","prod"],["prod"]]

factCFG :: CFG
factCFG
  = [(("_",["n"]),[1,2]),
     (("return",[]),[]),
     (("prod",[]),[3]),
     (("i",["n"]),[4]),
     (("_",["i"]),[5,7]),
     (("prod",["i","prod"]),[6]),
     (("i",["i"]),[4]),
     (("return",["prod"]),[])]

-- Figure 3
fig3
  = ("f", ["a", "n"],
     [Assign "b" (Const 0),
      While (Apply GEq (Var "n") (Const 1))
            [Assign "c" (Apply Add (Var "a") (Var "b")),
             Assign "d" (Apply Add (Var "c") (Var "b")),
             If (Apply GEq (Var "d") (Const 20))
                [Assign "b" (Apply Add (Var "c") (Var "d")),
                 Assign "d" (Apply Add (Var "d") (Const 1))
                ]
                [],
             Assign "a" (Apply Mul (Var "c") (Var "d")),
             Assign "n" (Apply Add (Var "n") (Const (-1)))
            ],
      Assign "return" (Var "d")
     ])

fig3B :: Block
fig3B
  = let (_, _, b) = fig3 in b

fig3IG
  = (["a","b","c","d","n"],
     [("a","b"),("a","d"),("a","n"),
      ("b","c"),("b","d"),("b","n"),
      ("c","d"),("c","n"),
      ("d","n")])

-- Assuming three registers, [1,2,3]...
fig3Colouring :: Colouring Id
fig3Colouring
  = [("a",3),("b",0),("c",3),("d",2),("n",1)]

fig3IdMap :: IdMap
fig3IdMap
  = [("return","return"),("a","R3"),("b","b"),("c","R3"),("d","R2"),("n","R1")]

fig3Transformed :: Function
fig3Transformed
  = ("f",["a","n"],
     [Assign "R3" (Var "a"),
      Assign "R1" (Var "n"),
      Assign "b" (Const 0),
      While (Apply GEq (Var "R1") (Const 1)) 
            [Assign "R3" (Apply Add (Var "R3") (Var "b")),
             Assign "R2" (Apply Add (Var "R3") (Var "b")),
             If (Apply GEq (Var "R2") (Const 20)) 
                [Assign "b" (Apply Add (Var "R3") (Var "R2")),
                 Assign "R2" (Apply Add (Var "R2") (Const 1))
                ] 
                [],
             Assign "R3" (Apply Mul (Var "R3") (Var "R2")),
             Assign "R1" (Apply Add (Var "R1") (Const (-1)))
            ],
      Assign "return" (Var "R2")
      ]
     )

fig3LiveVars :: [[Id]]
fig3LiveVars
  = [["a","d","n"],["a","b","d","n"],["a","b","n"],["b","c","n"],
     ["b","c","d","n"],["c","d","n"],["b","c","d","n"],
     ["b","c","d","n"],["a","b","d","n"],["d"]]

fig3CFG :: CFG
fig3CFG
  = [(("b",[]),[1]),
     (("_",["n"]),[2,9]),
     (("c",["a","b"]),[3]),
     (("d",["b","c"]),[4]),
     (("_",["d"]),[5,7]),
     (("b",["c","d"]),[6]),
     (("d",["d"]),[7]),
     (("a",["c","d"]),[8]),
     (("n",["n"]),[1]),
     (("return",["d"]),[])]
