import Data.Char
import Prelude
import Data.List

-- 1a
-- Cause double quotes for H, should be single quotes
-- 'H' : ['a', 's', 'k', 'e', 'l', 'l']

-- 1b
-- Is fine, works well

-- 1c
-- Is fine, works well

-- 1d
-- Is fine, works well

-- 1e
-- It is a list of one list, hence the size is 1
-- let xs = "let xs" in length xs
-- This gives 6 as the output

-- 1f
-- Can't use ":" to add two lists together. Must use ++ over :
-- tail "emu" ++ drop 2 "much"

-- 1g
-- It is a list of a list, hence if you want the head of the word gasket, do:
-- head "gasket"

-- 1h
-- Is fine, works well

-- 1i
-- Can't use tail on a tuple, must use it on a list
-- tail [(1,1), (2,2), (3,3)]
-- returns [(2,2),(3,3)]

-- 1j
-- Can't compare two elements of different types, and list is not homogenous

-- 1k
-- Is fine, works well

-- 1l
-- Is fine, works well

-- 1m
-- Is fine, works well

-- 1n
-- Is fine, works well

-- 1o
-- Is fine, works well

-- 1p 
-- Is fine, works well

-- 1q
-- Is fine, works well

-- 1r
-- Need to add paranthesis around the zip
-- maximum (zip "abc" [1, 2, 3])

-- 1s 
-- Is fine, works well

-- 1t
-- you can't apply the head function to each element in the list, when each element is a separate integer

-- 2
precedes :: String -> String -> Bool
precedes a b = a < b

-- 3
pos :: Int -> [Int] -> Int
pos intToFind (x:xs)
    | intToFind == x = 0
    | otherwise = 1 + pos intToFind xs

-- 4
twoSame :: [Int] -> Bool
twoSame [] = False
twoSame (x:xs) = elem x xs || twoSame xs

-- 5
rev :: [a] -> [a]
rev [] = []
rev (x:xs) = (rev xs) ++ [x]

-- 6
substring :: String -> String -> Bool
substring a (y:ys)
    | length a <= length (y:ys) = take (length a) (y:ys) == a || substring a ys
    | otherwise = False

-- 6 
isPrefix :: String -> String -> Bool
isPrefix [] _ = True
isPrefix _ [] = False
isPrefix (x:xs) (y:ys) = x == y && isPrefix xs ys


substring' :: String -> String -> Bool
substring' query search = any (isPrefix query) (tails search)

-- 7
posString :: Char -> String -> Int
posString strToFind (x:xs)
    | strToFind == x = 0
    | otherwise      = 1 + posString strToFind xs

indicesList :: String -> String -> [Int]
indicesList [] []                  = []
indicesList (x:xs) anagram2@(y:ys) = (posString x anagram2) : indicesList xs ys


--transpose :: String -> String -> String -> String
--transpose word anagram1@(x:xs) anagram2 = word !! posString x anagram2 : transpose word xs anagram2

-- 8 
trimWhitespace :: String -> String
trimWhitespace "" = ""
trimWhitespace (x:xs)
    | isSpace x = trimWhitespace xs
    | otherwise = (x:xs)

trimWhitespace' :: String -> String
trimWhitespace' [] = []
trimWhitespace' s@(x:xs) = dropWhile (isSpace) s

-- 9
currentWord' :: String -> String
currentWord' "" = ""
currentWord' s@(x:xs)
    | isSpace x = []
    | otherwise = x : currentWord' xs

nextWord :: String -> (String, String)
nextWord s@(x:xs)
    | isSpace x = (currentWord' xs, xs)
    | otherwise = nextWord xs

-- 10
-- splitUp :: String -> [String]
-- splitUp "" = [""]
-- splitUp s@(x:xs) = 

-- Part 2

-- 1
findAll k t = [y | (x,y) <- t, x == k]

-- 3
allSplits :: [a] -> [([a], [a])]
allSplits input = [ splitAt x input | x <- [1..(length input) - 1]] 

-- 4
prefixes :: [a] -> [[a]]
prefixes values = [ prefixOfLength x values| x <- [1..(length values)]]

prefixOfLength :: Int -> [a] -> [a]
prefixOfLength 0 _      = []
prefixOfLength _ []     = []
prefixOfLength k (x:xs) = x : prefixOfLength (k-1) xs

-- 5
substrings :: String -> [String]
substrings [] = []
substrings s@(x:xs) = prefixes s ++ substrings xs