# IIIF Bookshelf Webapp

This is a webapp for collecting <a href="http://iiif.io">IIIF</a> representations of books.
It is based on the functionality of the <a href="http://iiif.io/api/presentation/2.0/">IIIF Presentation API</a> for modelling books.
You can add books to your bookshelf by loading the manifest.json of the book.

## Requirements

* Java 8: You will need the Java Runtime Environment (JRE) version 1.8 or higher. At a command line, check your Java version with "java -version".
* MongoDB Version: 3.2.4+
* Apache Solr Version: 5.4.1+
* Apache Tomcat 8.0

### Mongo DB

#### Installation

1. Download the binary files for the desired release of Mongo DB from [official Mongo DB Downloads Page](https://www.mongodb.org/downloads). 
   To download the latest release through the shell, type the following:

        $ curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.2.4.tgz

2. Extract the files from the downloaded archive.
   For example, from a system shell, you can extract with the tar command:

        $ tar -zxvf mongodb-linux-x86_64-3.2.4.tgz

3. Copy the extracted archive to the target directory.
   To copy the extracted folder to the location from which MongoDB will run:

        $ mkdir -p /opt
        $ mv mongodb-linux-x86_64-3.2.4/ /opt

#### Configuration

Ensure the location of the binaries is in the PATH variable.

* SuSE Linux:

```shell
# vi /etc/profile.d/mongodb.sh

# Add paths for mongo db
if [ -d /opt/mongodb-linux-x86_64-3.2.4/bin ]; then
    COUNT=`ls -1 /opt/mongodb-linux-x86_64-3.2.4/bin/ | wc -l`
    if [ $COUNT -gt 0 ]; then
        PATH="$PATH:/opt/mongodb-linux-x86_64-3.2.4/bin"
    fi
fi
export PATH=$PATH
```

```shell
# vi /etc/profile.d/mongodb.csh

# Add paths for mongo db
if ( -d /opt/mongodb-linux-x86_64-3.2.4/bin ) then
    set COUNT=`ls -1 /opt/mongodb-linux-x86_64-3.2.4/bin/ | wc -l`
    if ( $COUNT > 0 ) then
        setenv PATH "${PATH}:/opt/mongodb-linux-x86_64-3.2.4/bin"
    endif
endif
```

* Ubuntu Linux:

```shell
# vi /etc/environment
...
PATH="/opt/mongodb-linux-x86_64-3.2.4/bin:$PATH"
...
```

Test it (after reboot or sourcing of file):

```shell
mongod --version
db version v3.2.4
git version: e2ee9ffcf9f5a94fad76802e28cc978718bb7a30
allocator: tcmalloc
modules: none
build environment:
    distarch: x86_64
    target_arch: x86_64

# vi /etc/environment
...
PATH="/opt/mongodb-linux-x86_64-3.2.4/bin:$PATH"
...
```

#### Running Mongo DB

To run MongoDB, run the mongod process at the system prompt. If you do not use the default data directory (i.e., /data/db), specify the path of the data directory using the --dbpath option.

```shell
$ mongod --dbpath <path to data directory>
```

To shutdown:

```shell
$ mongod --dbpath <path to data directory> --shutdown
```

If you specify the logpath option, then logging will direct to that log file instead of showing up on standard console:

    > mongod --dbpath ```<path to data directory>``` --logpath ```<path to logs>```/mongod.log

By default logs are written into /var/log/mongodb/.


Apache Solr Installation Steps
------------
Homepage: http://lucene.apache.org/solr/
Version: 5.4

### Installation on Linux

To install run the following commands:

      $ cd /opt
      $ sudo wget http://archive.apache.org/dist/lucene/solr/5.4.1/solr-5.4.1.tgz
      $ sudo tar -xzvf solr-5.4.1.tgz

### Installation on Windows

1. Download the .zip file. .zip archive from one of the mirrows at [Solr Download](http://www.apache.org/dyn/closer.lua/lucene/solr/5.4.1)
2. Extract the Solr distribution archive to a directory of your choosing. 

Apache Tomcat Installation Steps
------------

1. Download Tomcat 8 (the tar.gz file) and decompress it. 
2. Create a new directory /opt/tomcat
3. Move the files in step 2 to the directory /opt/tomcat
4. Run in /opt/tomcat/apache-tomcat-8.0.35

        $ bin/startup.sh   

Deploy Bookshelf WAR
------------

Copy iiif-bookshelf.war into webapps subdir in tomcat

## Usage
-------

To run iiif-bookshelf run local Mongo DB, Solr and Tomcat.

### Running Solr

#### On Linux

      $ cd /opt/solr-5.4.1
      $ sudo ./bin/solr start

After 30 seconds check at http://localhost:8983/ , whether the server has started.

Use  option -s for pointing custom path to located created cores. It sets the solr home system property.
Solr will create core directories under specified directory. This allows you to run multiple Solr instances on the same host.

    $ /opt/solr-5.4.1$ bin/solr start -s ~/```<custom-core-path>```/

For exampple, use /home/username/solrcores/ in place of ```<custom-core-path>```.
Copy solr.xml from /opt/solr-5.4.1/server/solr/ to your ```<custom-core-path>``` directory in order to create cores there later.
 
#### Creating cores

      $ bin/solr create_core -c ```<core-name>```

use -d option to specify configuration , if you have started solr with custom solr home as above.
See bin/solr create_core -help for options description.

####Adding/Deleting indexes

Post  to core .json documents from example directory:

    $ curl 'http/solr/```<core-name>```/update?commit=true' --data-binary example/exampledocs/books.json -H 'Content-type/application/json'

To delete certain document with id 20 use:

    $ java -Ddata=args -Dcommit=true -Durl=http://localhost:8983/solr/```<core-name>```/update -jar example/exampledocs/post.jar >```'<delete><id>20</id></delete>'```

#### On Windows

    > bin\solr.cmd start

### Stopping Solr

    $ sudo ./bin/solr stop

### Running Bookshelf webapp

1. To start Tomcat run start.sh within its bin directory.
2. Check on http://localhost:8080 whether it has started
3. Open webapp http://localhost:8080/iiif-bookshelf
 
* Running from cloned code:

        $ mvn clean install jetty:run
        http://localhost:9898

* Accessing data from 'mongo' client:

        $ mongo
        > use iiif-bookshelf
        switched to db iiif-bookshelf

    * Getting all data:

        > db.getCollection('iiif-manifest-summaries').find()

    * Deleting all data (why do you want to do this?):

        > db.getCollection('iiif-manifest-summaries').drop()
        true

TODO
----

* UUID: use BSON4 instead BSON3
