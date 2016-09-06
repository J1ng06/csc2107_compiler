# CSC488H1S Winter 2016 Assignment 3 - Team 11

Total: **87.4/100**

## Packaging (3/3)

 * Missing `build.xml`


## Design and implementation (27/30)

 * Effective, minimalist AST modifications
 * Good implementation of visitor pattern as applied to semantic analysis
    - But some additional commentary describing what is going on would have been appreciated
 * (**-3**) Symbol table has reasonable interface, but fragile design
    - Beware of the singleton pattern (`static` class fields); entering a scope should really be returning a _new_ table that internally contains the parent link, instead of a globally fixed enter/exit ordering
    - The visitor pattern lends itself to encapsulating this kind of state into the visitor itself, ex:

```java
            protected SymbolTable activeScope = ...; // initialized in `visit(Program)`

            public void visit(Scope scope) {
                SymbolTable previous = activeScope;

                // Effectively "pushing" something new onto a stack
                activeScope = activeScope.enterScope();

                // ... visit declarations and statements in scope ...

                // Effectively "popping" the new table off returning back to the previously active scope
                activeScope = previous;
            }

            public void visit(IdentifierExpn ident) {
                ...
                activeScope.getSymbol(ident.getIdentifier());
                ...
            }
```


## Documentation (21/25)

 * (**-2**) Symbol table design does not discuss major/minor scoping or routine arguments
    - It is especially not clear if routines get a `SymbolTableEntry`, and if so, what `SymbolEntryType`
 * (**-1**) No discussion of source location tracking in AST
 * (**-1**) Semantic analysis design did not discuss how scopes and types are tracked as AST is walked


## Team testing (27/30)

 * Large collection of expected-to-fail test...
 * (**-2**) However, missing combinations like `return with` in procedures, `return` in functions, `exit N` within `< N` loops, `exit when EXPN`, 1D arrays used as though they were 2D (and vice versa), scalars used as functions (and vice versa)
 * (**-1**) Not enough new passing testcases to help raise confidence in symbol table implementation and semantic analysis
 * Test runner output does not do a good job of indicating whether the tests are behaving as expected
    - This perhaps contributed to the hard compiler crash on `tests/failing/st1101.488` not being detected?
    - Consider identifying and reporting the "success" or "failure" of an expected-to-fail testcase


## Official testing (9.4/12)

 * `A3-05a.488`: handling of nested scopes is broken
 * `A3-07a.488`: cannot `exit` out of procedures
 * `A3-07b.488`, `A3-07c.488`: presence or absence of `return`/`return with` in procedures or functions or top-level is fragile and sometimes causes crashes
 * `A3-08e.488`, `A3-09.488`: array identifiers must be used with indices (`SubsExpn` vs `IdentExpn`)
 * `A3-10.488`: incorrect procedure argument count

```
                      11    11   11
   Test    Errors   Errs Crash  Mark
A3-00.488      0       0         1.0
A3-01.488      0       0         1.0
A3-02.488      0       0         1.0
A3-03.488     15      15         1.0
A3-04.488      6       6         1.0
A3-05a.488     0      -8         0.0
A3-05b.488     0       0         1.0
A3-05c.488    10      10         1.0
A3-06a.488     0       0         1.0
A3-06b.488     0       0         1.0
A3-07a.488     2       1         0.5
A3-07b.488     5             1   0.0
A3-07c.488     5       0         0.0
A3-07d.488     0       0         1.0
A3-08a.488     0       0         1.0
A3-08b.488     6       6         1.0
A3-08c.488     0       0         1.0
A3-08d.488     0       0         1.0
A3-08e.488     6       1         0.2
A3-09.488      8       7         0.9
A3-10.488      8       4         0.5
A3-11a.488     0       0         1.0
A3-11b.488     5       5         1.0

Raw score     23.00            18.04

Fraction Score                 0.784

Testing Mark     12              9.4
```
