# address
Simple micro-service to demonstrate CRUD, search and bulk load. This project still has a long todo list. Only the basic CRUD is working. I will be implementing the search next. Some of the code for search is currently checked in but not wired up as it is not ready. Some of the items are listed below:
* Fix Java 10 - gradle issues
* Generate documentation on build (java 10 - gradle issue)
* Bundle documentation into jar
* Make documentation available in running jar
* Complete search function
* Integration test for search function (relys on hibernate so impractical to unit test)
* Merge search and CRUD endpoints (different controllers, same endpoint if practical)
* Determine best method for bulk loading, multi-part, modified json, other?
