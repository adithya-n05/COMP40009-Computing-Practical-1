data SuffixTree = Leaf Int | Node [(String, SuffixTree)] 
                deriving (Eq, Show)

------------------------------------------------------

isPrefix :: String -> String -> Bool
isPrefix pref search = pref == take (length pref) search

removePrefix :: String -> String -> String
removePrefix s s'
--Pre: s is a prefix of s'
  = b
    where 
      (a, b) = splitAt (length s) s'

suffixes :: [a] -> [[a]]
suffixes [] = []
suffixes s' = s' : suffixes (tail s')

isSubstring :: String -> String -> Bool
isSubstring s s' = or (map (isPrefix s) (suffixes s'))

findSubstrings :: String -> String -> [Int]
findSubstrings s s'
  | isSubstring s s' 
    = [ indexOf x (suffixes s') | x <- suffixes s', isPrefix s x]

-- assumed that all items in the list are distinct
-- assumed that item does exist in the list
indexOf :: String -> [String] -> Int
indexOf a (x:xs)
  | a == x = 0
  | otherwise = 1 + indexOf a xs

------------------------------------------------------

getIndices :: SuffixTree -> [Int]

getIndices (Leaf a) 
  = [a]
getIndices (Node n')
  = concatMap (getIndices . snd) n'

partition :: Eq a => [a] -> [a] -> ([a], [a], [a])
partition 
  = undefined

findSubstrings' :: String -> SuffixTree -> [Int]
findSubstrings' "" (Leaf a) = a
findSubstrings' "" (Node b) = getIndices (Node b)
findSubstrings' s' (Leaf a) = []
findSubstrings' s' (Node k@((s, st):ns))
  | isPrefix s s' = findSubstrings (removePrefix s s') (st) ++ findSubstrings s' (Node (tail k))
  | otherwise = []
------------------------------------------------------

insert :: (String, Int) -> SuffixTree -> SuffixTree
insert 
  = undefined

-- This function is given
buildTree :: String -> SuffixTree 
buildTree s
  = foldl (flip insert) (Node []) (zip (suffixes s) [0..])

------------------------------------------------------
-- Part IV

longestRepeatedSubstring :: SuffixTree -> String
longestRepeatedSubstring 
  = undefined

------------------------------------------------------
-- Example strings and suffix trees...

s1 :: String
s1 
  = "banana"

s2 :: String
s2 
  = "mississippi"

t1 :: SuffixTree
t1 
  = Node [("banana", Leaf 0), 
          ("a", Node [("na", Node [("na", Leaf 1), 
                                   ("", Leaf 3)]), 
                     ("", Leaf 5)]), 
          ("na", Node [("na", Leaf 2), 
                       ("", Leaf 4)])]

t2 :: SuffixTree
t2 
  = Node [("mississippi", Leaf 0), 
          ("i", Node [("ssi", Node [("ssippi", Leaf 1), 
                                    ("ppi", Leaf 4)]), 
                      ("ppi", Leaf 7), 
                      ("", Leaf 10)]), 
          ("s", Node [("si", Node [("ssippi", Leaf 2), 
                                   ("ppi", Leaf 5)]), 
                      ("i", Node [("ssippi", Leaf 3), 
                                  ("ppi", Leaf 6)])]), 
          ("p", Node [("pi", Leaf 8), 
                      ("i", Leaf 9)])]


