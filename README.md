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
Graal VM is an Oracle high-performance JDK distribution written for Java and other JVM languages, that provides a
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

* **NB** Note that you should have **graalVM** installed in your dev machine.
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
java -jar target/spring-native-0.0.1-SNAPSHOT.jar
```
We can also build an image 
* **NB** Note that you should have **docker** installed and running in your dev machine.
```cmd
./mvnw spring-boot:build-image
```



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
* **NB** The spring image is by default created from the base image of the buildpack which gives that fat jar to run the spring image

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
    
To generate a docker native image with spring you need to configure you build image to point to the **paketobuildpacks/builder:tiny** instead of the **base** image 
with a **process-aot** goal:

```xml
...
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <image>
            <builder>paketobuildpacks/builder:tiny</builder>
            <env>
                <BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>
            </env>
        </image>
    </configuration>
    <executions>
        <execution>
            <id>process-aot</id>
            <goals>
                <goal>process-aot</goal>
            </goals>
        </execution>
    </executions>
</plugin>
...
```
You need to also make sure you add reachability of metadata goal:
If you don't add the reachability of metadata the generated image wont be able to see some classes which should have been registered for reflection,
and you will start getting some wierd errors eg ```%PARSER_ERROR[d] %PARSER_ERROR[p] 1```
```xml
...
<plugin>
    <groupId>org.graalvm.buildtools</groupId>
    <artifactId>native-maven-plugin</artifactId>
    <configuration>
        <classesDirectory>${project.build.outputDirectory}</classesDirectory>
        <metadataRepository>
            <enabled>true</enabled>
        </metadataRepository>
        <requiredVersion>22.3</requiredVersion>
    </configuration>
    <executions>
        <execution>
            <id>add-reachability-metadata</id>
            <goals>
                <goal>add-reachability-metadata</goal>
            </goals>
        </execution>
    </executions>
</plugin>
...
```
Now you are ready to run the build again :
```cmd
./mvnw spring-boot:build-image 
```
This will generate a native spring image from paketobuildpacks image tiny
```cmd
❯ docker images
REPOSITORY                 TAG              IMAGE ID       CREATED        SIZE
paketobuildpacks/run       tiny-cnb         b708ebf07d16   33 hours ago   17.3MB
paketobuildpacks/builder   tiny             3c7da334a749   42 years ago   590MB
spring-native              0.0.1-SNAPSHOT   af16478763bf   42 years ago   96.8MB

```
From just picking the generated image you can notice the size of the image has greatly reduced from **279MB** to **96.8MB**

To run the new generated image:

```cmd
docker run --rm -p 8080:8080 spring-native:0.0.1-SNAPSHOT
```

```cmd
docker run --rm -p 8080:8080 spring-native:0.0.1-SNAPSHOT

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.0)

2022-12-07T05:55:39.611Z  INFO 1 --- [           main] c.s.s.SpringNativeApplication            : Starting AOT-processed SpringNativeApplication using Java 17.0.5 with PID 1 (/workspace/com.stackdev.springnative.SpringNativeApplication started by cnb in /workspace)
2022-12-07T05:55:39.611Z  INFO 1 --- [           main] c.s.s.SpringNativeApplication            : No active profile set, falling back to 1 default profile: "default"
2022-12-07T05:55:39.621Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-12-07T05:55:39.623Z  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-12-07T05:55:39.623Z  INFO 1 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.1]
2022-12-07T05:55:39.627Z  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-12-07T05:55:39.627Z  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 16 ms
2022-12-07T05:55:39.646Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-12-07T05:55:39.648Z  INFO 1 --- [           main] c.s.s.SpringNativeApplication            : Started SpringNativeApplication in 0.044 seconds (process running for 0.048)
```

Boom the image has booted in less than 100ms powerful staff!!

## Deploying to Kubernetes 

First we need to prepare the Dockerfile

```dockerfile
FROM gcr.io/distroless/base
ARG APP_FILE
EXPOSE 8080
COPY target/${APP_FILE} app
ENTRYPOINT ["/spring-native"]
```
To build an image out of the docker file you need to pass the spring-native executable as the argument.

```shell
docker build -f Dockerfile --build-arg APP_FILE=./target/spring-native -t spring/mynative .
```
You can now run the image
```shell
docker run -it --rm -p 8080:8080 spring/mynative 
```
For non Linux users im going to use a multistage docker file : 

```dockerfile
FROM ghcr.io/graalvm/native-image:ol8-java17-22 AS builder

# Install tar and gzip to extract the Maven binaries
RUN microdnf update \
 && microdnf install --nodocs \
    tar \
    gzip \
 && microdnf clean all \
 && rm -rf /var/cache/yum

# Install Maven
# Source:
# 1) https://github.com/carlossg/docker-maven/blob/925e49a1d0986070208e3c06a11c41f8f2cada82/openjdk-17/Dockerfile
# 2) https://maven.apache.org/download.cgi
ARG USER_HOME_DIR="/root"
ARG SHA=f790857f3b1f90ae8d16281f902c689e4f136ebe584aba45e4b1fa66c80cba826d3e0e52fdd04ed44b4c66f6d3fe3584a057c26dfcac544a60b301e6d0f91c26
ARG MAVEN_DOWNLOAD_URL=https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${MAVEN_DOWNLOAD_URL} \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Set the working directory to /home/app
WORKDIR /build

# Copy the source code into the image for building
COPY . /build

# Build
RUN mvn --no-transfer-progress clean package -Pnative

# The deployment Image
FROM docker.io/oraclelinux:8-slim

EXPOSE 8080

# Copy the native executable into the containers
COPY --from=builder /build/target/spring-native .
ENTRYPOINT ["/spring-native"]
```
This Dockerfile worked for me in my case since i'm on MacOS (not M1 chip), the other one were giving me some binary distro issues
since by default the Docker build command targets arm64 machine. There is a work-around  you can always specify the machine name
in you docker build command eg:
```shell
# Build for ARM64 (default)
docker build -t <image-name>:<version>-arm64 .

# Build for ARM64 
docker build --platform=linux/arm64 -t <image-name>:<version>-arm64 .

# Build for AMD64
docker build --platform=linux/amd64 -t <image-name>:<version>-amd64 .
```

If you check your docker images you can see the native image was generated
```cmd
❯ docker images
REPOSITORY                     TAG             IMAGE ID       CREATED             SIZE
spring/mynative                latest          418e6aafdebf   32 minutes ago      191MB
ghcr.io/graalvm/native-image   ol8-java17-22   ba9db7c19687   6 days ago          957MB
```
and you can run it as above.

* Okay enough of docker now let deploy this badboy to Kubernetes
