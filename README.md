# clj-wikicats

Clojure code to play with the german wikipedia categories, and [loom](https://github.com/aysylu/loom).
Also start a http component to serve the shortest path algorithms over http.

## Do not use this it's lispy

    * BUILD: lein deps
    * RUN:  lein run

look around in core.clj

### Example requests

        http://localhost:3000/astar-path?from=Berlin

        http://localhost:3000/astar-path?from=Angela_Merkel&to=Sachsystematik

        http://localhost:3000/astar-path?from=Angela_Merkel&to=Barack_Obama

        http://localhost:3000/shortest-path?from=Wald&to=Umwelt_und_Natur

        http://localhost:3000/shortest-path?from=Wald


## License

Copyright © 2014 Sojoner

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
