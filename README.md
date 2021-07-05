# Multithreaded Socket Server

In this project, we simulate the basic functionality of a distributed processing system, using sockets and threads, following master-slave architecture. 
Specifically, there will be three different entities in the system: a) Client, b) Master and c) Slave.

The multithreaded Master server listens to two different ports, one for the clients and one for the Slave server.

After connecting to the Master server, clients start sending random integers (0-100) periodically and they receive answers. Slave server and all slaves that are connected to it are responsible for checking received numbers and respond whether they have already received them in the past or not.
