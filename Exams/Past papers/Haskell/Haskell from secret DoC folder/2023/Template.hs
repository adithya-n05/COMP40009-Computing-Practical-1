module Solver where

import Data.List
import Data.Char

import Types
import WordData
import Clues
import Examples

------------------------------------------------------
-- Part I

punctuation :: String
punctuation 
  = "';.,-!?"

cleanUp :: String -> String
cleanUp y
  = [ toLower x | x <- y, notElem x punctuation]

split2 :: [a] -> [([a], [a])]
split2 [] 
  = []
split2 y@(x:xs) 
  = [ splitAt z y | z <- [1..(length y)-1]]

split2EmptyFirst :: [a] -> [([a], [a])]
split2EmptyFirst [] 
  = []
split2EmptyFirst y
  = [ splitAt z y | z <- [0..(length y)-1]]

split3 :: [a] -> [([a], [a], [a])]
split3 l
  = concat [ [ (a, c, d) | (c, d) <- split2EmptyFirst b] | (a, b) <- split2 l]

uninsert :: [a] -> [([a], [a])]
uninsert l
  = [ (b, a ++ c) | (a, b, c) <- split3 l, not (null b)]

split2M :: [a] -> [([a], [a])]
split2M xs
  = sxs ++ [(y, x) | (x, y) <- sxs] 
  where
    sxs = split2 xs

split3M :: [a] -> [([a], [a], [a])]
split3M xs
  = sxs ++ [(z, y, x) | (x, y, z) <- sxs]
  where
    sxs = split3 xs

------------------------------------------------------
-- Part II


matches :: String -> ParseTree -> Bool
matches s (Synonym s')
  = elem s (synonyms s')
matches s (Anagram _ s') 
  = sort s == sort s'
matches s (Reversal _ t)
  = matches (reverse s) t
matches s (Insertion _ t1 t2)
  = not (null (filter (\(s1,s2) -> ((matches s1 t1) && (matches s2 t2))) (uninsert s)))
matches s (Charade _ t1 t2)
 = or [ True | (a, b) <- split2 s, (matches a t1) && (matches b t2)]


evaluate :: Parse -> Int -> [String]
evaluate (def, lk, t) enum
  = [ syn | syn <- syndeffil, matches syn t]
    where
      syndef    = synonyms (unwords def)
      syndeffil = filter (\x -> length x == enum) syndef

------------------------------------------------------
-- Part III

-- Given...
parseWordplay :: [String] -> [ParseTree]
parseWordplay ws
  = concat [parseSynonym ws,
            parseAnagram ws,
            parseReversal ws,
            parseInsertion ws,
            parseCharade ws]
    
parseSynonym :: [String] -> [ParseTree]
parseSynonym [] = []
parseSynonym s'@(s:strs)
  | not (null (synonyms s)) = [Synonym (unwords s')]
  | otherwise = parseSynonym strs

parseAnagram :: [String] -> [ParseTree]
parseAnagram strs
  | null parsedList = []
  | otherwise       = let h = head parsedList
                      in [Anagram (fst (h)) (concat (snd (h)))]
  where frags = split2M strs
        parsedList = filter (\(ind, s) -> elem (unwords ind) anagramIndicators) frags

parseReversal :: [String] -> [ParseTree]
parseReversal strs
  | null parsedList = []
  | otherwise       = [Reversal (fst (head parsedList)) (head ((parseWordplay (snd (head parsedList)))))]
  where frags = split2M strs
        parsedList = filter (\(ind, s) -> elem (unwords ind) reversalIndicators) frags

parseInsertion :: [String] -> [ParseTree]
parseInsertion strs
  = const []
  where frags = split3 strs
        parsedList = filter (\(arg, ws, arg') -> elem (unwords ws) insertionIndicators) frags

parseCharade :: [String] -> [ParseTree]
parseCharade 
  = const []

-- Given...
parseClue :: Clue -> [Parse]
parseClue clue@(s, n)
  = parseClueText (words (cleanUp s))

parseClueText :: [String] -> [Parse]
parseClueText
  = undefined

solve :: Clue -> [Solution]
solve 
  = undefined


------------------------------------------------------
-- Some additional test functions

-- Returns the solution(s) to the first k clues.
-- The nub removes duplicate solutions arising from the
-- charade parsing rule.
solveAll :: Int -> [[String]]
solveAll k
  = map (nub . map getSol . solve . (clues !!)) [0..k-1]

getSol :: Solution -> String
getSol (_, _, sol) = sol

showAll
  = mapM_ (showSolutions . solve . (clues !!)) [0..23]


