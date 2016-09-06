# CSC488H1S Winter 2016 Assignment 5 - Team 11

Total: **80.5/100**

## Packaging (2/2)


## Expressions (9/13)

 * Extraneous AST pretty-print output
 * A lot of unhealthy mixing of semantics processing (with respect to routines and parameter symbols) and code generation
 * Defaulting `IdentExpn` and `SubsExpn` to only push a computed address leads to many repetitive instanceof checks in unrelated places, making for very decentralized code that is easy to get wrong
    - Why special case these two? Instead, default to pushing values (not addresses) unless some kind of `isLvalue` flag is set
 * (**-2**) Boolean expressions broken if operand is array due to missing LOAD, ex: `b[1] and true`
    - Same for conditional, ex: `( b[1] ? 1 : 0 )` or `( true ? v[0] : v[1] )`
 * (**-2**) Short-circuiting of AND is broken, ex: `p := false and false`


## Statements (12/13)

 * (**-1**) Write statement misses a variety of cases, such as conditional expressions, function calls (especially function calls without arguments)


## Functions and procedures (13/17)

 * Evaluating arguments right-to-left is atypical
 * (**-2**) Returns do not branch
 * (**-2**) Storage for embedded minor scopes allocated on the fly and with poor cleanup


## Documentation (20/20)

 * Who have appreciated some discussion on why symbols and symbol tables were still being created at code gen time


## Team testing (18/20)

 * PDM: Unfortunately I could not run your JUnit suite via `ant runtests`, but the output capture method looks good
 * (**-1**) No procedure return's or multiple function returns (hence why the implementation doing no branching didn't appear to break anything)
 * (**-1**) No larger test cases or sample programs that combine multiple language constructs in realistic ways


## Official testing (6.5/15)

 * `A5e01.488`: array declaration exceeds machine memory, and this is knowable at compile time
 * `A5t02.488`: progressive breakdown of parameter passing
 * `A5t03.488`: broken conditional loop exits
 * `A5t04.488`: compiler crash due semantic analysis
 * `A5t05.488`, `A5t06.488`: compiler crash due code gen
 * `A5t07.488`, `A5t08.488`: array reference breaks

```
                       11    11   11
    Test       Tests Pass Crash  Mark
A5e01.488       2             1   0.0
A5t00.488       1       1         1.0
A5t01a.488     12      12         1.0
A5t01b.488     20      20         1.0
A5t02.488      14       4         0.3
A5t03.488       7       1         0.1
A5t04.488       7             1   0.0
A5t05.488       2             1   0.0
A5t06.488       3             1   0.0
A5t07.488       5       1         0.2
A5t08.488       2       0         0.0
A5t09.488       1       1         1.0
A5t10.488       2       2         1.0

Raw score      13.00             5.63

Fraction Score                  0.433

Testing Mark      15              6.5
```
