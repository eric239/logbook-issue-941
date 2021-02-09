### Demonstrates an issue with logbook when attached to Elastic Search High-Level client.

### Setup

Run elasticsearch locally mapping the default port:

```shell
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.10.2
```

Build the test app

```shell
./gradlew bootJar
```

### Run

Run the app with no options (this will enable logbook logging as configured in `application.properties`)

```shell
java -jar build/libs/logbook-with-elasticsearch-client-1.0.jar
```

The app creates ES client and exposes a simple HTTP GET endpoint at `/cluster-health` which hits ES cluster health API.

Call the endpoint (obviously `| json_pp` part is optional):

```shell
curl localhost:8080/cluster-health | json_pp
```

This fails with
```json
{
   "message" : "",
   "timestamp" : "2021-02-09T04:20:02.856+00:00",
   "status" : 500,
   "path" : "/cluster-health",
   "error" : "Internal Server Error"
}
```

And the app log shows
```text
2021-02-08 20:20:02.619 TRACE 9810 --- [nio-8080-exec-1] org.zalando.logbook.Logbook              : Incoming Request: 9f3dd00b45441068
Remote: 0:0:0:0:0:0:0:1
GET http://localhost:8080/cluster-health HTTP/1.1
accept: */*
host: localhost:8080
user-agent: curl/7.64.1
2021-02-08 20:20:02.667 TRACE 9810 --- [nio-8080-exec-1] org.zalando.logbook.Logbook              : Outgoing Request: 842c66c70a8d77e4
Remote: localhost
GET null://null/_cluster/health?master_timeout=30s&level=cluster&timeout=30s HTTP/1.1
2021-02-08 20:20:02.847 ERROR 9810 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.RuntimeException: Content has not been provided] with root cause

java.lang.IllegalStateException: Content has not been provided
	at org.apache.http.util.Asserts.check(Asserts.java:34) ~[httpcore-4.4.14.jar!/:4.4.14]
	at org.apache.http.entity.BasicHttpEntity.getContent(BasicHttpEntity.java:75) ~[httpcore-4.4.14.jar!/:4.4.14]
	at org.apache.http.util.EntityUtils.toByteArray(EntityUtils.java:124) ~[httpcore-4.4.14.jar!/:4.4.14]
	at org.zalando.logbook.httpclient.HttpEntities.copy(HttpEntities.java:22) ~[logbook-httpclient-2.5.0.jar!/:na]
	at org.zalando.logbook.httpclient.RemoteResponse$Offering.buffer(RemoteResponse.java:76) ~[logbook-httpclient-2.5.0.jar!/:na]
	at org.zalando.logbook.httpclient.RemoteResponse.lambda$getBody$2(RemoteResponse.java:186) ~[logbook-httpclient-2.5.0.jar!/:na]
	at org.zalando.fauxpas.ThrowingFunction.apply(ThrowingFunction.java:19) ~[faux-pas-0.9.0.jar!/:na]
	at java.base/java.util.concurrent.atomic.AtomicReference.updateAndGet(AtomicReference.java:209) ~[na:na]
	at org.zalando.logbook.httpclient.RemoteResponse.getBody(RemoteResponse.java:185) ~[logbook-httpclient-2.5.0.jar!/:na]
	at org.zalando.logbook.HttpMessage.getBodyAsString(HttpMessage.java:28) ~[logbook-api-2.5.0.jar!/:na]
	at org.zalando.logbook.ForwardingHttpMessage.getBodyAsString(ForwardingHttpMessage.java:47) ~[logbook-api-2.5.0.jar!/:na]
	at org.zalando.logbook.FilteredHttpResponse.getBodyAsString(FilteredHttpResponse.java:50) ~[logbook-core-2.5.0.jar!/:na]
	at org.zalando.logbook.DefaultHttpLogFormatter.format(DefaultHttpLogFormatter.java:63) ~[logbook-core-2.5.0.jar!/:na]
	at org.zalando.logbook.DefaultSink.write(DefaultSink.java:26) ~[logbook-core-2.5.0.jar!/:na]
	at org.zalando.logbook.Strategy.write(Strategy.java:119) ~[logbook-api-2.5.0.jar!/:na]
	at org.zalando.logbook.DefaultLogbook$1.lambda$process$0(DefaultLogbook.java:55) ~[logbook-core-2.5.0.jar!/:na]
	at org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor.process(LogbookHttpResponseInterceptor.java:27) ~[logbook-httpclient-2.5.0.jar!/:na]
	at org.apache.http.protocol.ImmutableHttpProcessor.process(ImmutableHttpProcessor.java:142) ~[httpcore-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.client.MainClientExec.responseReceived(MainClientExec.java:290) ~[httpasyncclient-4.1.4.jar!/:4.1.4]
	at org.apache.http.impl.nio.client.DefaultClientExchangeHandlerImpl.responseReceived(DefaultClientExchangeHandlerImpl.java:151) ~[httpasyncclient-4.1.4.jar!/:4.1.4]
	at org.apache.http.nio.protocol.HttpAsyncRequestExecutor.responseReceived(HttpAsyncRequestExecutor.java:315) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.DefaultNHttpClientConnection.consumeInput(DefaultNHttpClientConnection.java:255) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.client.InternalIODispatch.onInputReady(InternalIODispatch.java:81) ~[httpasyncclient-4.1.4.jar!/:4.1.4]
	at org.apache.http.impl.nio.client.InternalIODispatch.onInputReady(InternalIODispatch.java:39) ~[httpasyncclient-4.1.4.jar!/:4.1.4]
	at org.apache.http.impl.nio.reactor.AbstractIODispatch.inputReady(AbstractIODispatch.java:114) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.reactor.BaseIOReactor.readable(BaseIOReactor.java:162) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.reactor.AbstractIOReactor.processEvent(AbstractIOReactor.java:337) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.reactor.AbstractIOReactor.processEvents(AbstractIOReactor.java:315) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.reactor.AbstractIOReactor.execute(AbstractIOReactor.java:276) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.reactor.BaseIOReactor.execute(BaseIOReactor.java:104) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at org.apache.http.impl.nio.reactor.AbstractMultiworkerIOReactor$Worker.run(AbstractMultiworkerIOReactor.java:591) ~[httpcore-nio-4.4.14.jar!/:4.4.14]
	at java.base/java.lang.Thread.run(Thread.java:834) ~[na:na]
```

Note that

- the incoming request is logged perfectly
- the outgoing request into ES is logged a bit weirdly (`null`s)
- the incoming response from ES produces the error in logbook

Now stop the app and run it with logbook turned off:

```shell
java -Dlogging.level.org.zalando.logbook=info -jar build/libs/logbook-with-elasticsearch-client-1.0.jar
```

Hit the endpoint again

```shell
curl localhost:8080/cluster-health | json_pp
```

Now everything's fine:

```json
{
   "relocatingShards" : 0,
   "clusterName" : "docker-cluster",
   "taskMaxWaitingTime" : {
      "millisFrac" : 0,
      "nanos" : 0,
      "micros" : 0,
      "stringRep" : "0ms",
      "hours" : 0,
      "millis" : 0,
      "secondsFrac" : 0,
      "minutes" : 0,
      "daysFrac" : 0,
      "microsFrac" : 0,
      "days" : 0,
      "hoursFrac" : 0,
      "seconds" : 0,
      "minutesFrac" : 0
   },
   "activeShardsPercent" : 100,
   "activeShards" : 0,
   "unassignedShards" : 0,
   "activePrimaryShards" : 0,
   "fragment" : false,
   "indices" : {},
   "initializingShards" : 0,
   "timedOut" : false,
   "delayedUnassignedShards" : 0,
   "numberOfPendingTasks" : 0,
   "numberOfInFlightFetch" : 0,
   "status" : "GREEN",
   "numberOfNodes" : 1,
   "numberOfDataNodes" : 1
}
```
