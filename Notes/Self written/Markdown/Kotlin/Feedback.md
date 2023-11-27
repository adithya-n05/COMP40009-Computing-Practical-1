# PPT 8

- Use a string builder instead of just adding strings:

```kotlin
val result = StringBuilder()
result.append("start - end")
```

- Use sumOf HOF to add the sum of all items in a list
  - `return segments.sumOf {it.travelTime}`
- Use generate to generate getters, setters, etc.

- Use `zipWithNext()`
  - Returns a list with each item placed in a tuple with the next item
