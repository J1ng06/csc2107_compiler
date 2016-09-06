# CSC488H1S Winter 2016 Assignment 4 - Team 11

Total: **97/100**

## Storage (15/15)

 * Could have used a more specifics on how minor scopes are collapsed into enclosing major scope (and specifically if adjacent minor scopes share starting offsets)


## Expressions (25/25)

 * For arrays, the values of `ON` and `lb_index` (or `lb_index_1D`) are known at compile-time, so you could skip the `SUB` by combining them directly into the `ADDR` instruction
 * Codegen templates as presented for Boolean operators (including lines like "emit code for LHS evaluation") and elsewhere should have been used throughout, instead of specific examples
    - Adding a small mention in part c at the very end of the document isn't helpful!


## Functions and procedures (24/25)

 * (b) Consider using `DUPN` to allocate space en masse
 * (**-1**) `n` is not the number of locals, but rather the number of words of all local storage (think of arrays, or nested minor scopes)
 * (d) The result of running the code necessary to evaluate a parameter should leave a value on the top of the stack, at which point why would you need the `PUSH`?


## Statements (16/18)

 * Don't forget that assignments and read's can refer to arrays
 * (**-1**) Procedures are absolutely allowed to do early returns
 * (**-1**) Routine `return` does not show statement branching to epilogue
    - Also, are you _sure_ that a function return value is stored at display-relative offset of `-n`?
 * How are loop exit labels handled?
 * Consider implementing a text printing helper routine to save on code space


## Everything else (5/5)


## Sample programs (12/12)

 * Good reference comments; indentation also helped a lot!
 * `A4-3.488`: I am doubtful as to whether or not you are actually saving the previous display value
    - For example, `Q.G`: the final `SETD 2` will restore the display, but where is there a `ADDR 2 k` either in the caller or callee prologue that pushed it?
