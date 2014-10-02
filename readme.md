Hibernate Versioning Example
============================
Simple example that shows how Hibernate uses version numbers rather than locking to control concurrent access to data. It uses Maven and sets up an HSQL database to run, so if you have maven installed, to run it, just type:

    mvn test

It uses two threads. We create a customer object, which we pass to thread 1 to update. In the meantime, we start thread 2, which creates its own Hibernate session, loads the customer object from the database, and updates it. Then thread 1 attempts its update, which fails with a stale object exception. If you look at the output, you will see that the update statement Hibernate uses includes both the id and the version number in the where clause, so that the update will do nothing if the version number does not match. Hibernate then checks how many rows were updated. If the number is zero, it knows something has gone wrong.
