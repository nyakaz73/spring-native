# Spring Boot (3)  Spring Native (GraalVM) :fire:
Kubernetes Java Native Application with Spring Boot 3 (Spring Native)

## Show some :heart: and :star: to support this project

Okay!! so im just going to jump right in. As some of you might know that Spring Boot 3, Spring Framework 6 has just released been
a few days ago before writing this article,  here are some of the features we can get excited about :smile:
 * Java 17 Baseline
 * Records
 * Native Executables
 * Observability 

In this  read i'm mainly going to focus on how we can build Kubernetes Cloud Native Java Applications using Spring
Native (Native Executables) with **GraalVM**.

*  Don't get me wrong Spring support for Native (Experimental) has already been out for a couple of years, but the reason for us to get
excited is the Spring team really took some time to address some issues and bug fixes that Spring Native had prior to 
this release, and at the time of this writing new milestones will be released.
   
Okay enough chit-chat :)

### Kubernetes Cloud Native Development
This is an idea of building applications and deploying them to a **Kubernetes** array short-lived containers while addressing
mainly two fundamental principles among the others which are Memory utilization and Boot up time and instant peak performance. This deployment paradim
is what is called the **Container first approach** philosophy.


### Enter Graal VM
Graal VM is an Oracle high-performance JDK distribution written of Java and other JVM languages, that provides a
Native Image Builder for building native code and package it together with the VM into a standalone executable.

### How?? 
Staff that used to be done by Java Applications at runtime i.e:

* Compiling source code &#8594; Load and parse configurations  &#8594; Analyse dependencies &#8594; Build dependency tree &#8594; Execute Code

is now being done at build time by **Graal VM**  using  a process called **Ahead of Time Compilation** that uses **JIT** (Just In time Compiler)

### Goal :information_desk_person:

This will inturn gives you a native executable that has a low memory foot-print and crazy start up time.


### What is Spring Native
Spring Native provides support for compiling Spring application to Native executables using GraalVM native image compiler.

## Get Started
To get started make sure you have Docker and GraalVM installed in your dev machine.

There are two basic ways to build a Spring Boot native application:
 * 1.  You can use [Spring Boot Buildpack](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks)  support to generate a lightweight container containing a native excutable
 * 2.  You can use the [Native Build Tools](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#:~:text=Spring%20Native%20provides%20support%20for,for%20many%20types%20of%20workloads.) to generate a native executable

## Dependencies
Add GraalVM Native Support build plugin in your ***pom.xml*** file.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.graalvm.buildtools</groupId>
            <artifactId>native-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
Setting this build configuration will enable Spring to process the goals required when preparing source code
for native building.
The goals include AOT (by collecting data for the AOT compiler which then prepares/registers classes required for reflection
usage.)

You can see the Metadata by running this command.
```cmd
./mvnw clean package -Pnative
```
If you check the target folder you can see there are a couple of goals that were run:
```cmd
mvn spring-boot:process-aot
mvn spring-boot:process-test-aot
```
Including some graalvm reachability of metadata processing, and of-cause a slightly optimised **fat jar** was also generated.
You can run the fat jar with
```cmd
java -jar target/spring-native-0.0.1-SNAPSHOT.jar.original
```
We can also build an image 
```cmd
./mvnw spring-boot:build-image
```

* **NB** Note that you should have **graalVM** and **docker** installed in your dev machine.

What this does is it will build an image out of the default **base** of [paketobuildpacks](https://paketo.io/docs/concepts/buildpacks/)
which is essentially a slightly optimised **jvm** based **fat jar** as the one above.

```cmd
❯ docker images
REPOSITORY                 TAG              IMAGE ID       CREATED        SIZE
paketobuildpacks/run       base-cnb         9d986bd5e914   2 days ago     88.8MB
paketobuildpacks/builder   base             4d66077a2347   42 years ago   1.32GB
spring-native              0.0.1-SNAPSHOT   1f0ab5db7333   42 years ago   279MB
```
After building the image you can notice that the command has pulled the paketobuildpack  images to create the spring image.
* **NB** The spring image is by default created from the base image of teh buildpack which gives that fat jar to run the spring image

```cmd
docker run --rm -p 8080:8080 spring-native:0.0.1-SNAPSHOT
```
So this whole time we were just demonstrating how you can build spring from GraalVM , and these were essentially 
slighty optimised(with AOT, and Meta data generation) jvm builds.

### Generating Native Images 
How do we build native images ?

* First we need to add the native profile configs:

```xml
<profiles>
    <profile>
        <id>native</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.graalvm.buildtools</groupId>
                    <artifactId>native-maven-plugin</artifactId>
                    <executions>
                        <execution>
                            <id>build-native</id>
                            <goals>
                                <goal>compile-no-fork</goal>
                            </goals>
                            <phase>package</phase>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

As mentioned above there are basically two approached to generate the Native Images /Executables.

* 1. First I'm going to demonstrate using [Native Build Tools]() to generate a native executable, with this approach
    make sure you have GraalVM installed

Let us run the command we ran earlier:

```cmd
./mvnw clean package -Pnative
```
Notice the difference between this run and the previous one is that, Graal has used the generated Meta data to create a
Native executable spring-native, depending with the name of your project in the target build folder

If you check the target folder it now has a native executable generated, to run it:

```cmd
./target/spring-native
```
Notice how fast the application booted, in less than 100ms, pretty wild right??
```cmd
❯ ./target/spring-native 

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.0)

2022-12-04T14:29:10.084+03:00  INFO 11562 --- [           main] c.s.s.SpringNativeApplication            : Starting AOT-processed SpringNativeApplication using Java 19.0.1 with PID 11562 (/Users/lameck/StackDev/spring-native/target/spring-native started by lameck in /Users/lameck/StackDev/spring-native)
2022-12-04T14:29:10.084+03:00  INFO 11562 --- [           main] c.s.s.SpringNativeApplication            : No active profile set, falling back to 1 default profile: "default"
2022-12-04T14:29:10.098+03:00  INFO 11562 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-12-04T14:29:10.099+03:00  INFO 11562 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-12-04T14:29:10.099+03:00  INFO 11562 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.1]
2022-12-04T14:29:10.109+03:00  INFO 11562 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-12-04T14:29:10.109+03:00  INFO 11562 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 25 ms
2022-12-04T14:29:10.127+03:00  INFO 11562 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-12-04T14:29:10.128+03:00  INFO 11562 --- [           main] c.s.s.SpringNativeApplication            : Started SpringNativeApplication in 0.055 seconds (process running for 0.065)
```

As we did before 
* 2. First I'm going to demonstrate using [Spring Boot Buildpack support]() to generate a native docker image