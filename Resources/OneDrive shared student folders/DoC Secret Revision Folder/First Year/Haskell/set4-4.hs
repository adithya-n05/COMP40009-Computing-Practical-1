data AmPm = AM | PM
  deriving (Show, Eq)
data Time = Conv Int Int AmPm | TwentyFour Int Int
  
instance Eq Time where
  (==) = equalTime
  
instance Show Time where
 show (Conv 12 0 AM) = "Midday"
 show (Conv 12 0 PM) = "Midnight"
 show (Conv h m AM) = show' h ++ ":" ++ show' m ++ "AM"
 show (Conv h m PM) = show' h ++ ":" ++ show' m ++ "PM"
 show (TwentyFour h m) = show' h ++ show' m ++ "HRS"
 
show' :: Int -> String
show' n
 | n < 10 = "0" ++ show n
 | otherwise = show n


to24 :: Time -> Time
to24 (TwentyFour hh mm)  = (TwentyFour hh mm)
to24 (Conv hh mm t)
  | t == AM   = (TwentyFour hh mm)
  | t == PM && hh == 12 = (TwentyFour 0 mm)
  | otherwise = (TwentyFour (hh + 12) mm)
  
equalTime :: Time -> Time -> Bool
equalTime t1 t2 
  = same24 (to24 t1) (to24 t2)

same24 (TwentyFour h1 m1) (TwentyFour h2 m2)
  = h1 == h2 && m1 == m2