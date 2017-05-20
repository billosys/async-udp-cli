# Tímı

A time tracker written in Clojure(Script).

![Tími screenshot](docs/screenshot-2.png)

This project started as a fork of the
[Infi Alibi Time Tracker](https://github.com/infi-nl/alibi), but has since
undergone major refactorings (with more to come). What started as a
handful of changes for our own needs and aesthetics has evolved into something
that is a new application in its own right. Still, Tímı owes much of its DNA
and all of its beginnings to Unfi's Alibi.


## Features

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


## Dependencies

The prerequisites for this route are:

- Have a Git client installed.
- Make sure Java is installed.
- Make sure [lein](https://leiningen.org/) is installed.
- Have `npm` installed
- Ensure that `make` is installed


## Preparation

### Initialization

Clone the repo. Then, inside the repo, initialize a time tracker:

```
$ make var/data/timi.db
```

This will create a sqlite3 db with filename `var/data/timi.db`, which contains
the right schema, but has no data yet.


### Configuration

Before we create some data, you first need to setup a config file, and for
that you need to create a key for cookie encryption, e.g. using:

```
$ make cookie
```

Then execute the following to create a config file, replacing the `:cookie-
encryption-key` value with your own key and `[:sqlite :subname]` with the
database file you picked in a previous step.

```
$ cat << EOF > config/local/config.edn
{:cookie-encryption-key "xxxxxxxxxxxxxxxx"
 :selmer-caching? false

 :persistence :sqlite
 :persistence-strategies {}
 :sqlite {:subprotocol "sqlite" :subname "var/data/timi.db"}

 :authentication :single-user
 :single-user {:username "Alice"}}
EOF
```


## Build

The Clojure and ClojureScript for the project can be built in one go with the
following:

```
$ make
```

If you just want to do one or the other, you may use `make build-clj` or
`make build-cljs`.


### Projects Population

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


## Startup

Run the server:

```
$ make run
```

If everything went well, there should now be a web server running on
[http://localhost:3000](http://localhost:3000). Navigate to it and have fun!


## License

Copyright © 2017 Infi Holding B.V. and contributers.

Copyright © 2017 Billo Systems, Ltd. Co.

Distributed under the Mozilla Public License Version 2.0
