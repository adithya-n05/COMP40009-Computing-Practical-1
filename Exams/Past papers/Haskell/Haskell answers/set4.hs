data Shape = Triangle Float Float Float | Square Float 
                                        | Circle Float 

--Date Day Month Year
data Date = Date Int Int Int

data Possibly a = Success a | Failure
  deriving (Show)
                                       
area :: Shape -> Float
area (Triangle x y z) = sqrt(s * (s - x) * (s - y) * (s - z))
  where
    s = (1 / 2) * (x + y + z)
area (Square x) = x ^ 2
area (Circle r) = pi * r ^ 2

age :: Date -> Date -> Int
age (Date d m y) (Date d' m' y')
  | m' > m || m' == m && d' >= d = y' - y
  | otherwise                     = y' - y - 1

tableLookUp :: Eq a =>  a -> [(a , b)] -> Possibly b
tableLookUp _ [] = Failure
tableLookUp i ((x , y) : rest)
  | i == x    = Success y
  | otherwise = tableLookUp i rest