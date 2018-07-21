# async-udp-cli

[![Build Status][travis-badge]][travis][![Clojars Project][clojars-badge]][clojars][![Clojure version][clojure-v]](project.clj)

*A Clojure utility library for faster CLIs with async UDP clients/servers*

[![][logo]][logo-large]


#### Contents

* [About](#about-)
* [Usage](#usage-)
* [License](#license-)


## About [&#x219F;](#contents)

This utility was created in order to support CLI tool development against
long-running services without the cost of starting up a JVM instance. The
approach employed here assumes all the logic is on the service and that
in one of the service's libraries we will be able to call a function that
parses the command line options and will perform the desired work for those
options.

So how fast is it?

Here's the output from the example CLI hitting the example CLI server:

```
$ time UDP_PORT=5097 ./bin/example arg1 arg2 arg3
```
```
ERROR [billo.udp.client.util:31] - ENV has no variabel named ''
Simple cli-parser echoing back data: arg1 arg2 arg3


real    0m0.234s
user    0m0.199s
sys     0m0.032s
```

So, pretty fast ;-) By comparison, the time taken to start up and stop the
server JVM (comparable to what a JVM-based CLI tool would do) takes this
lone:

```
real	0m7.405s
user	0m8.822s
sys	0m0.890s
```


## Usage [&#x219F;](#contents)

In one terminal, run the example server:

```
$ UDP_PORT=5097 lein example-server
```

In another terminal, build the example client:

```
$ lein build-cli
```

Then run the example CLI:

```
$ UDP_PORT=5097 ./bin/example arg1 arg2 arg3
```

If you'd like to see the connection debug info, you can set the log level as
an environment variable:

```
$ LOG_LEVEL=debug UDP_PORT=5097 ./bin/example arg1 arg2 arg3
```


## License [&#x219F;](#contents)


Copyright © 2017-2018 Billo Systems, Ltd. Co.

Distributed under the Apache License Version 2.0.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/billosys/async-udp-cli
[travis-badge]: https://travis-ci.org/billosys/async-udp-cli.png?branch=master
[deps]: http://jarkeeper.com/billosys/async-udp
[deps-badge]: http://jarkeeper.com/billosys/async-udp-cli/status.svg
[logo]: resources/public/images/golden-clock-square-250px.png
[logo-large]: resources/public/images/golden-clock-square-2400px.png
[tag-badge]: https://img.shields.io/github/tag/billosys/async-udp.svg
[tag]: https://github.com/billosys/async-udp-cli/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.9.0-blue.svg
[clojars]: https://clojars.org/systems.billo/async-udp
[clojars-badge]: https://img.shields.io/clojars/v/systems.billo/async-udp-cli.svg
