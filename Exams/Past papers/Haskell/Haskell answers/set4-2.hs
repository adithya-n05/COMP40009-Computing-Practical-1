data StaffType = Teaching Section [Course] | Support SupportType
data Section = Systems | Software | Theory
data SupportType = Administrative | Technical
data Sex = Male | Female

type Database = [Ustaff]
type Course = Int
type Ustaff = (Empdata, StaffType)
type Empdata = (String, Sex, Date, Int)
type Date = (Int , Int , Int)

name :: Ustaff -> String
name ((n , s , d , c) , t) = n

salary :: Ustaff -> Int
salary ((n, sex, dob, sal), t) = sal

teaches :: Course -> Ustaff -> Bool
teaches c (e, Support t) = False
teaches c (e, Teaching s courses) = c `elem` courses

isSupport :: Ustaff -> Bool
isSupport (_, Support _) = True
isSupport _              = False

supportStaff :: Database -> Int
supportStaff db = length (filter isSupport db)

teachesCourse :: Course -> Database -> String
teachesCourse c db =  name (head (filter (teaches c) db))

salaries :: Database -> Int
salaries db = sum (map salary db)