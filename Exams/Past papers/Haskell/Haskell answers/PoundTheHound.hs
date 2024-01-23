module PoundTheHound where

type Name = String
type Address = String
type Time = Int
type TimeHM = (Int, Int)
type Request = (Name, Address, Time, Time)

rs :: [Request]
rs = [("Killer","11 Manton Av",1000,1130),
    ("Rex","4 Howard Court",1500,1615),
    ("Jaws","1 West Rd",1030,1115),
    ("Gnasher","16 Park St",1200,1330),
    ("Satan","Hamley Manor",900,1015),
    ("Fangs","19 Clover St",1400,1530),
    ("Preston","44 Main St",1145,1345),
    ("Chomp","9 Radley St",1545,1730)]
    
startTime, finishTime :: Request -> Time
startTime (_, _, x, _)  = x
finishTime (_, _, _, x) = x

convertTime :: Int -> TimeHM
convertTime t = (t `div` 100, t `mod` 100)

subtractTimes :: Int -> Int -> Int
subtractTimes t1 t2 = (h1 - h2) * 60 + (m1 - m2)
  where
    (h1 , m1) = convertTime t1
    (h2 , m2) = convertTime t2
    
sortRequests :: [Request] -> [Request]
sortRequests [] = []
sortRequests (first : rest) = x : sortRequests y
  where
    (x , y) = returnSmallest first rest

returnSmallest :: Request -> [Request] -> (Request , [Request])
returnSmallest fir [] = (fir , [])
returnSmallest (current @ (_,_,_,fir)) (compare @ (_,_,_,sec) : rest)
  | sec < fir = returnSmallest compare (current : rest)
  | otherwise = (x , compare : y)
    where
      (x , y) = returnSmallest current rest
      
schedule :: [Request] -> [Request]
schedule (first @ (_,_,_,t) : rest) = first : schedule' t rest
  where
    schedule' :: Int ->  [Request] -> [Request]
    schedule' t list = x : schedule' timeOfNext y
      where
        (x @ (_,_,_,timeOfNext) , y) = getNextAvailable t list
  
getNextAvailable :: Int -> [Request] -> (Request , [Request])
getNextAvailable _ [x] = (x , [])
getNextAvailable t (first @ (_,_,firstTime,_) : rest)
  | firstTime > t = (first , rest)
  | otherwise     = getNextAvailable t rest