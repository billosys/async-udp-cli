# async-udp-cli

[![Build Status][travis-badge]][travis][![Clojars Project][clojars-badge]][clojars][![Clojure version][clojure-v]](project.clj)

*A Clojure utility library for faster CLIs with async UDP clients/servers*

[![][logo]][logo-large]


#### Contents

* [About](#about-)
* [Usage](#usage-)
   * Example
   * Example Code
   * General Steps Required
* [Libraries of Interest](#libraries-of-interest-)
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
Simple cli-parser echoing back data: arg1 arg2 arg3


real    0m0.234s
user    0m0.199s
sys     0m0.032s
```

So, pretty fast ;-) 

By comparison, here's how long it takes to start up and stop the
server JVM (comparable to what a JVM-based CLI tool would do):

```
real	0m7.405s
user	0m8.822s
sys	0m0.890s
```


## Usage [&#x219F;](#contents)

### Example [&#x219F;](#contents)

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

### Example Code [&#x219F;](#contents)

The above usage demonstration is run against the following example server and client:

* [Server-side example](https://github.com/billosys/async-udp-cli/blob/master/src/clj/billo/example/cli_server.clj)
* [Client-side example](https://github.com/billosys/async-udp-cli/blob/master/src/cljs/billo/example/cli.cljs)

Note that `cli.cljs` gets compiled to `./bin/example` and is used from the system shell like a regular executable.

### General Steps Required [&#x219F;](#contents)

To use async-udp-cli in your own project, your server code will need to:

 * define your own parser function that takes two args: data and an options map (which may be empty)
 * define a hash-map with values for the keys `:port`, `:parser-fn`, and `:parser-opts`
 * pass this hash map when starting the UDP server with `billo.udp.server.core/run`
 
Your client code will need to:

 * be ClojureScript with the build target of `:nodejs`
 * connect to the UDP server you started
 * convert the args to strings
 * handle the response from the server
 * exit the Node.js process

That's all there is to it!


## Libraries of Interest [&#x219F;](#contents)

This project makes use of the following two libraries which may be of more general interest:

* [sockets](https://github.com/billosys/sockets) ([docs](http://billo.systems/sockets/current/)) - A UDP socket Clojure library (that wraps a number of `java.net` datagram classes); a wrapper for TCP sockets is in the works.
* [inet-address](https://github.com/billosys/inet-address) ([docs](http://billo.systems/inet-address/current/)) - A Clojure wrapper for the following `java.net` classes: `InetAddress`, `Inet4Address`, `Inet6Address`, and `NetworkInterface`


## License [&#x219F;](#contents)


Copyright © 2017-2018 Billo Systems, Ltd. Co.

Distributed under the Apache License Version 2.0.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/billosys/async-udp-cli
[travis-badge]: https://travis-ci.org/billosys/async-udp-cli.png?branch=master
[deps]: http://jarkeeper.com/billosys/async-udp
[deps-badge]: http://jarkeeper.com/billosys/async-udp-cli/status.svg
[logo]: https://avatars1.githubusercontent.com/u/8921204?&u=228b6e7c36c0b51e08e45a006c73cbdf4fa76ee1&s=250
[logo-large]: https://avatars1.githubusercontent.com/u/8921204?&u=228b6e7c36c0b51e08e45a006c73cbdf4fa76ee1
[tag-badge]: https://img.shields.io/github/tag/billosys/async-udp.svg
[tag]: https://github.com/billosys/async-udp-cli/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.9.0-blue.svg
[clojars]: https://clojars.org/systems.billo/async-udp
[clojars-badge]: https://img.shields.io/clojars/v/systems.billo/async-udp-cli.svg
