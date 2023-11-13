import Practice

import IC.TestSuite

import Data.List (sort)

main :: IO ()
main = runTests tests

tests :: [TestGroup]
tests =
  [ testGroup "isPrefix" isPrefixTests
  , testGroup "removePrefix" removePrefixTests
  , testGroup "suffixes" suffixesTests
  , testGroup "isSubstring" isSubstringTests
  , testGroup "findSubstrings" findSubstringsTests
  ]

-- You can add your own test cases below

isPrefixTests = [ isPrefix "has" "haskell" --> True
                , isPrefix "" "haskell" --> True
                , isPrefix "ask" "haskell" --> False
                ]

removePrefixTests = [ removePrefix "ja" "java" --> "va"
                    , removePrefix "" "java" --> "java"
                    ]

suffixesTests = [ suffixes "perl" --> ["perl", "erl", "rl", "l"]
                ]

isSubstringTests = [ isSubstring "cal" "scala" --> True
                   , isSubstring "california" "scala" --> False
                   ]

findSubstringsTests = [ sort (findSubstrings "an" "banana") --> [1, 3]
                      , sort (findSubstrings "s" "mississippi") --> [2, 3, 5, 6]
                      , findSubstrings "hello" "goodbye" --> []
                      ]
