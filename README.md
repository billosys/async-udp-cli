# Tímı
[![Build Status][travis-badge]][travis][![Clojars Project][clojars-badge]][clojars][![Clojure version][clojure-v]](project.clj)

*A time tracker written in Clojure(Script)*

[![][logo]][logo-large]


#### Contents

* [About](#about-)
* [Screenshot](#screenshot-)
* [Features](#features-)
* [Dependencies](#dependencies-)
* [Documentation](#documentation-)
* [Usage](#usage-)
  * [Build](#build-)
  * [Configuration](#configuration-)
  * [Starting](#starting-)
  * [CLI Server](#cli-server-)
  * [Data Initialization](#data-initialization-)
  * [Projects and Tasks Population](#projects-and-tasks-population-)
* [History](#history-)
* [License](#license-)


## About [&#x219F;](#contents)

Tímı is a project time-tracking tool intended for use by individuals,
non-profits, and small companies, originally based on
[Infi Alibi Time Tracker](https://github.com/infi-nl/alibi) (more on
that in the [History](#history-) section below.)


## Screenshot [&#x219F;](#contents)

Tímı in action:

![Tími screenshot](resources/docs/images/screenshot-2.png)


## Features [&#x219F;](#contents)

This is far from a fully fledged time tracker, yet it has the following
features, and more is on the way!

* Categorize time entries by project and task.
* Graphical overview of your weekly activity.
* Indicate whether entries are billable or not.
* Tasks have an associated billing method, either fixed price, overhead or hourly.
* Create projects and tasks via a CLI, and the REPL.
* Extension points for a different data source implementations.
* OpenID Connect and single user authentication.
* Out of the box Sqlite3 database support.


## Dependencies [&#x219F;](#contents)

The prerequisites for this route are:

- Have a Git client installed.
- Make sure Java is installed.
- Make sure [lein](https://leiningen.org/) is installed.
- Have `npm` installed
- Ensure that `make` is installed


## Documentation [&#x219F;](#contents)

In addition to this README, there is documentation availble once the
application is running:

* http://localhost:5099/docs


## Usage [&#x219F;](#contents)


### Build [&#x219F;](#contents)

Before you can do much with Tímı, you'll need to build it. As long as you
have all the dependencies install on your system (see above), it's as simple
as this:

```
$ make
```

This will do a lot, but can be summarized with the following:

* Download all the Clojure and JavaScript (Node.js) dependencies
* Build the server code
* Build the client code
* Build the CLI tool


### Configuration [&#x219F;](#contents)

Before we create some data, you first need to setup a config file. For local
development, copy the sample config:

```
$ cp config/sample/config.edn config/local
```

You'll want to change the encryption cookie, though. Generate a new one with:

```
$ make cookie
```

Then update your new `config/local/config.edn` file, replacing the same value
for `:cookie-encryption-key` with the one `make cookie` generated for you.


### Starting [&#x219F;](#contents)

With the application built, you're ready to start it. If you just want to
use the application and not do any development work, simply do:

```
$ make run
```

If you'll be doing server-side development and want access to the REPL:

```
$ make repl
```

Once the REPL finishes loading, start the application with `(run)`. You can
reload new code and restart the application by typing `(reset)`. The server
running in the REPL will be available on
[http://localhost:5098](http://localhost:5098)

If you'll be doing client-side development, then do:

```
$ make dev
```

If you then load up the application in your browser at
[http://localhost:5099](http://localhost:5099),
you'll see a prompt appear back in your terminal: this is ClojureScript, and
you have access to the client-side of the application, as it's running in your
browser.


### CLI Server  [&#x219F;](#contents)

One of the services that is started when the app comes up is a command-line
server, listening on port 5097. The Tímı cli tool that gets installed to
`./bin/timi` connects to this server to issue your commands and return results.
This feature allows for command-line operations that are much, much faster
than usual JVM commands (since there's no start-up cost for every command).

Now make sure you have easy access to `timi` by adding its location to
your `PATH`:

```
$ export PATH=$PATH:`pwd`/bin
```


### Data Initialization [&#x219F;](#contents)

Database setup is done using the `timi` CLI tool:

```
$ timi db init
```

If you are using the values defined for you in the sample config, this will
create a sqlite3 db with filename `var/data/timi.db`, which contains
the right schema, but has no data yet.

If you would like to override your configuration value. you can do the
following:

```
$ timi db init my/path/timi.db
```

Note, however, that you will need to update your configuration file to point
to the new location of the database.


### Projects and Tasks Population [&#x219F;](#contents)

Now we can populate the database with some projects and tasks. Here's how you
create a project:

```
$ lein timi-create-project "Tími" :billing-method :hourly
```
```clj
Creating new project:
{:project-name "Tími", :billing-method :hourly}
Done. Id for project is 1
```

The project ID that is returned is what you will use when creating tasks for
that project. For example:

```
$ lein timi-create-task "Project management" :for-project 1 :billing-method :overhead
$ lein timi-create-task "Programming" :for-project 1
```

At this point, you're ready to start tracking time! Visit either
[http://localhost:5098](http://localhost:5098) or
[http://localhost:5099](http://localhost:5099), depending upon how you have
started Tímı.


## History [&#x219F;](#contents)

This project started as a fork of the
[Infi Alibi Time Tracker](https://github.com/infi-nl/alibi), but has since
undergone major refactorings (with more to come). What started as a
handful of changes for our own needs and aesthetics has evolved into something
that is a new application in its own right. Still, Tímı owes much of its DNA
and all of its beginnings to Unfi's Alibi.


## License [&#x219F;](#contents)

Copyright © 2017 Infi Holding B.V. and contributers.

Copyright © 2017 Billo Systems, Ltd. Co.

Distributed under the Mozilla Public License Version 2.0


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/billosys/timi
[travis-badge]: https://travis-ci.org/billosys/timi.png?branch=master
[deps]: http://jarkeeper.com/billosys/timi
[deps-badge]: http://jarkeeper.com/billosys/timi/status.svg
[logo]: resources/public/images/golden-clock-square-250px.png
[logo-large]: resources/public/images/golden-clock-square-2400px.png
[tag-badge]: https://img.shields.io/github/tag/billosys/timi.svg
[tag]: https://github.com/billosys/timi/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[clojars]: https://clojars.org/systems.billo/timi
[clojars-badge]: https://img.shields.io/clojars/v/systems.billo/timi.svg
