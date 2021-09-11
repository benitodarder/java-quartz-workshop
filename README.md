# quartz-workshop

Examples:

* quartz-hello-world simple clusterable example

Quirks & feaures:

* Quartz 2.3.0, quartz-2.3.0-distribution.tar.gz, did not include the /docs/dbTables, so quartz-2.2.3-distribution.tar.gz was used.
* Some simple quartz examples, builts using HSQLDB, if not stated contrary.
    - Tested with HSQLDB 2.6.0 & 2.4.1
    - HSQLDB schema does not work directly and BINARY type must be changed to BLOB.
    - User creation:
        * CREATE USER QUARTZ PASSWORD QUARTZ ADMIN;
        * DROP USER SA;

