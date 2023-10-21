data Tree a = Leaf a | Node (Tree a) (Tree a)
  deriving Show

type Queue a = [a]
  
aTree = build [1..10]

build :: [a] -> Tree a
build [] = error "empty tree"
build [x] = Leaf x
build xs = Node (build left) (build right)
 where
  (left, right) = splitAt (length xs `div` 2) xs
  
ends :: Tree a -> [a]
ends (Leaf x)          = [x] 
ends (Node left right) = (ends left) ++ (ends right)

swap :: Tree a -> Tree a
swap (Leaf x) = Leaf x
swap (Node left right) = Node (swap right) (swap left)

nobody :: Queue a
nobody = []

arrive :: a -> Queue a -> Queue a
arrive x xs = xs ++ xs

first :: Queue a -> a
first xs = head xs

serve :: Queue a -> Queue a
serve xs = drop 1 xs