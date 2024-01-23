data Tree3 a b = Empty | Node (Tree3 a b) a (Tree3 a b) | Leaf b

map3 :: (a -> a) -> (b -> b) -> Tree3 a b -> Tree3 a b
map3 _ _ Empty = Empty
map3 _ g (Leaf b) = Leaf (g b)
map3 f g (Node left a right) = Node (map3 f g left) (f a) (map3 f g right)
