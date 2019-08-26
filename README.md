A simple Kotlin SAT solver framework.

Use `parseFile()` to read a CNF file and get a SATSolver object.
Call `solve()` and hand it an algorithm to use.

Provided:
```
Greedy BFS
Random Search
```
Example run:

```
parsing: zebra_v155_c1135.cnf
parsed in: 29 ms
literals: 155
clauses: 1160
distance to goal:
261 TTFTTFTFTTFFFTTTTFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTTTTTTTFFFFFTFFFTFTTTTFFTFTTFTFTTFTTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
246 TTFTTFTFTTFFFTTTFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTTTTTTTFFFFFTFFFTFTTTTFFTFTTFTFTTFTTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
234 TTFTTFTFTTFFFTTFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTTTTTTTFFFFFTFFFTFTTTTFFTFTTFTFTTFTTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
224 TTFTTFTFTTFFFTTFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTTTTFFFFFTFFFTFTTTTFFTFTTFTFTTFTTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
214 TTFTTFTFTTFFFTTFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTTFFFFFTFFFTFTTTTFFTFTTFTFTTFTTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
205 TTFTFFTFTTFFFTTFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTTFFFFFTFFFTFTTTTFFTFTTFTFTTFTTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
196 TTFTFFTFTTFFFTTFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTTFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
188 TTFTFFTFTTFFFTFFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTTFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
180 TTFTFFTFTTFFFTFFFFTFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTFFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
173 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFTTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTFFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
166 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFFTTTTTFTFTTTTFTFFTFTTTFFFFFFTFTFTFTTFTTFTFFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
159 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFFTTTTTFTFTTTTFTFFTFTFTFFFFFFTFTFTFTTFTTFTFFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
152 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFFTTTTTFTFTTTTFTFFTFTFTFFFFFFTFTFFFTTFTTFTFFFFFFTFFFTFTTTTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
145 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFFTTTTTFTFTTTTFTFFTFTFTFFFFFFTFTFFFTTFTTFTFFFFFFTFFFTFTTFTFFTFTTFTFTTFFTTFTFFFTFTTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
138 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFFTTTTTFTFTTTTFTFFTFTFTFFFFFFTFTFFFTTFTTFTFFFFFFTFFFTFTTFTFFTFTTFTFTTFFTTFTFFFTFFTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
132 TTFTFFTFTTFFFTFFFFFFFFFFFTFFTFTTFFTFFFTTTTFFTFTTTFTFTTTTFTFFTFTFTFFFFFFTFTFFFTTFTTFTFFFFFFTFFFTFTTFTFFTFTTFTFTTFFTTFTFFFTFFTTFTTFFFFTFTTTTTTFFFTFFFFFFTFTFT
(truncated)
```
