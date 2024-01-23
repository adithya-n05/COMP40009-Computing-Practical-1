import Data.Char
import Prelude
import Data.List

-- 1 a
depunctuate :: String -> String
depunctuate s = filter (\x -> not (elem x ['.', ',',':'])) s

-- 1 b
makeString :: [Int] -> String
makeString k = map chr k

-- 1 c
enpower :: [Int] -> Int
enpower k = foldr1 (flip (^)) k

-- 1 e
dezip :: [(a,b)] -> ([a], [b])
dezip k = ((map (\(x,y) -> x) k), (map (\(x,y) -> y) k))

-- 2
allSame :: [Int] -> Bool
allSame s@(_:xs) = and (zipWith (==) xs s)

-- 3 a
factScan :: [Integer]
factScan = scanl' (*) 1 [1..]

-- 3 b
eApprox :: Double
eApprox = sum (map (\x -> 1 / (fromIntegral x)) (take 5 factScan))

-- 3 c
mystery = 1 : scanl (+) 1 mystery
-- list of fibonacci

-- 4 a
squash :: (a-> a-> b) -> [a] -> [b]
squash _ [] = []
squash f (x1:x2:xs) = f x1 x2 : squash f (xs)

-- 4 b
squash' :: (a-> a-> b) -> [a] -> [b]
squash' f s = zipWith f s (tail s)

-- 5
converge :: (a -> a -> Bool) -> [a] -> a
converge _ [a] = a
converge f s@(x1:x2:xs)
    | f x1 x2   = x1
    | otherwise = converge f (x2:xs)

constant :: Double
constant = 

-- 6
