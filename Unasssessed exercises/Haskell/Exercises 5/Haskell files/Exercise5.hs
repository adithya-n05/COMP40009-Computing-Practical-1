import Data.Char
import Prelude
import Data.List

-- 1
data Shape = Triangle Float Float Float | Square Float | Circle Float deriving (Eq)

area :: Shape -> Float
area s
    | s == Triangle = 