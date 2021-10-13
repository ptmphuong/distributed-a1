# Multithread Client in Java

This is part 1 of my project in [CS6650 - Building Scalable Distributed Systems](https://gortonator.github.io/bsds-6650/).

The goal of this part is to implement an efficient multithreaded client, so that I could later use this to test against a distributed server.

## The scenario
In this project, we are building a lift ticket reader system for a chain of ski resorts. The end goal is to be able to store all ski lift data as a basis for data analysis.

In this part, we are building a multithreaded client that generates requests and sends lift data to a server hosted in AWS, simply set up on AWS with Tomcat for now.

Requests are sent in 3 phases, each phase will spawn an amount of threads (ranging from 32 - 256), and each thread will be assigned to send multiple POST requests to the server.
The whole description of the assignment can be found [here](https://gortonator.github.io/bsds-6650/assignments-2021/Assignment-1).

## The design

- Phases are signaled to start using CountDownLatches.
- Each Phase stores 
  * an ExecutionService to manage and schedule threads.
  * a CompletionService made from this ExecutionService to store the stats of this thread when completed.
  * a list of Client connections for threads to reuse.
- Thread implements Callable<Stats>
  * send out POST requests via OkHttp3 client
  * Update performance stats (total failed/successful requests, latency) and return this result to CompletionService via Future type.
  
## Performance metrics

Program performance:
* Total wall time
* Throughput (request per second)

Response performance:
* Total number of successful and failed requests
* Mean, median, percentile 99th, max latency of POST requests

## Performance results

Target number of requests is 180,000 post requests in total for each run.
Testings are done for number of threads = 32, 64, 128, 256.

#### 1. Throughput
Expected throughput result: (maxNumThreads/latency) * 60
  * Expected throughput due to Little's Law = maxNumThreads/latency
  * Each Phase runs a different amount of Threads, the program in total runs at 60% of maxNumThreads

Actual performance:
![](https://github.com/ptmphuong/distributed-a1/blob/master/ClientES2/plots/throughput_results.png)

#### 2. Wall time

![](https://github.com/ptmphuong/distributed-a1/blob/master/ClientES2/plots/Wall%20time%20by%20number%20of%20threads.png)

#### 3. Response latency 
Requests are grouped by the second-th that request occurs since the program starts, the average values of the groups are plotted.
![](https://github.com/ptmphuong/distributed-a1/blob/master/ClientES2/plots/post_latency_32_legend.png)
![](https://github.com/ptmphuong/distributed-a1/blob/master/ClientES2/plots/post_latency_64.png)
![](https://github.com/ptmphuong/distributed-a1/blob/master/ClientES2/plots/post_latency_128.png)
![](https://github.com/ptmphuong/distributed-a1/blob/master/ClientES2/plots/post_latency_256.png)
