**************************************************
** Artist Info Service manual
**************************************************
To start service use
 - sh start.sh script or
    mvn spring-boot:run   command

To execute artist information call
  http://localhost:8080/artist?id="ARTIST_ID"
  some ARTIST_ID's for test :
  - "5b11f4ce-a62d-471e-81fc-a69a8278c7da"
  - "f27ec8db-af05-4f36-916e-3d57f91ecf5e"
  - "7944ed53-2a58-4035-9b93-140a71e41c34"
  - "79239441-bfd5-4981-a70c-55c3f15c1287"
  -"b753584a-3664-46f8-bb84-f6deed133c80"
  -"770d28d0-c056-40c3-9533-780a70d03b73"

To stop server on Linux use
  - sh stop.sh
To stop server on Windows use
  - stop.bat

*******************************************************
** Implementation notes
*******************************************************
To implemented executable application I used @SpringBootApplication annotation
for ArtistService class. To support asynchronous requests for REST resources I
execute requests in separate threads.

There is only one resource mapped in the service "artistInfo" with "id" parameter.

Concurrency :

Application has two level of concurrency:
1. asynchronous resource request (CallableRequest thread is created);
2. asynchronous cover arts and portfolio loading (Here I implemented LogicExecutor threads
which execute logic of receiving data pieces in separate threads)

All threads work asynchronously. To track if all logic executors completed their work
I used Semaphore class. It is a kind of thread counter which is shared between CallableRequest class
and all logic executors working for the same resource request. Each logic executor increments
Semaphore value on start and decrements on the end of logic execution transaction.
CallableRequest thread is monitoring Semaphore state and if Semaphore's value becomes zero
it means that all logic executor threads completed their work and we can return filled Artist value
in JSON format. I also set 2 min timeout for the response.

Model :

To work with json format I used gson library. I implemented two models: one is internal model
which fully repeats incoming json objects structure, and second is external model to export
objects into required json structure. JsonConverter class is responsible for reading of json data
into internal model and converting it to external model.


Tests:

I implemented unit tests and flow test to check if application is working as expected.
I also used Jmeter to test application stability in heavy load.
You can find the test plan for jmeter in file  HeavyLoad.jmx

Heavy Load :

According to limitations described here:
https://musicbrainz.org/doc/XML_Web_Service/Rate_Limiting
 musicbarinz recommends to send 1 request per second. Otherwise they return error 503.
Following this recommendation I schedule request callable tasks to keep rate alive.
Normally to keep application reliable we should collect response time statistics and set it's average as delay time
for request handling. In this case user will get information slowly but he will get it.
To increase reliability in case of "too many requests error" request is repeated in 5 seconds.

To test for heavy load I disabled caching and ran same request 10000 times in parallel.
For parallel run I used bmz plugin in Jmeter.
I do not schedule request execution if artist is in cache so result should be returned immediately in this case.
So for 10000 artist request from cache total response time is 32 sec.
If we deactivate cache(lets say cache limit is reached) this test shows that application will
still work correctly returning valid response data with average response time 3.3 sec
You can find additional statistical report in the file summary.cvs


Exceptions :
I handle exceptions and if it is critical I return Bad Request error. All exceptions are logged
by ConsoleLogger.

Logging :

For the testing and debug reason application I activate ConsoleLogger. It will log the trace of
application working process with timings and all kinds of errors.


Documentation :

Project's classes are well documented. You can find in comments even more detailed information about how each class is working.

What could be improved:

There are some things which I would like to do if I had few days more.
1) make cache to support frequency of requests for objects and when the cache is full and we get a new request we should replace the object with the lowest requests frequency value. So we could keep the most relevant Artists information in cache.
2) Cache could be resizable and if we are out of memory we could persist objects on disc because reading from disc is usually faster than average remote request time which is about 5 sec.
3) To hack the musicbarinz requests limitation per second we can build distributed application running on different
machines and sending request from different ip addresses. So if we will use 10 machines for example we can
send 10 unique requests per second without loosing the rate. Machines with installed distributed application can share a common cache storage (like db) and additionally manage their own in memory cache. In this case requested resource first should be searched in local cache and if not found - in shared storage.  

Link :
Project repository link : https://github.com/melenlu/ArtistInfoService
