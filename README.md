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

### How :question:
Staff that used to be done by Java Applications at runtime i.e:

* Compiling source code &#8594; Load and parse configurations  &#8594; Analyse dependencies &#8594; Build dependency tree &#8594; Execute Code

is now being done at build time by **Graal VM**  using  a process called **Ahead of Time Compilation** that uses **JIT** (Just In time Compiler)

### Goal :information_desk_person:

This will in turn gives you a native executable that has a low memory foot-print and crazy start up time.


### What is Spring Native
Spring Native provides support for compiling Spring application to Native executables using GraalVM native image compiler.

## Get Started
To get started you can clone the code repository [here](https://github.com/nyakaz73/spring-native) the repo has a basic spring Webflux API with a single endpoint.

If you want to learn more about Spring webflux you can check out my youtube course [here](https://youtu.be/PecY7og5KyI)

Also make sure you have Docker and GraalVM installed in your dev machine.

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

As mentioned above there are basically two approaches to generate the Native Images /Executables.

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
* 2. Secondly I'm going to demonstrate using [Spring Boot Buildpack support]() to generate a native docker image
    
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
From just peeking the generated image you can notice the size of the image has greatly reduced from **279MB** to **96.8MB**

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
This *Dockerfile.native* worked for me in my case since i'm on MacOS (not M1 chip), the other one were giving me some binary distro issues
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

* Okay enough of docker now let's deploy this bad boy to Kubernetes.

### Install minikube and VM
You can follow the installation guide [here](https://minikube.sigs.k8s.io/docs/start/).

On MacOS simply run:
```shell
brew install minikube
```
To install the hypervisor VM:
```shell
brew install hyperkit
```
Once installed you can start using the VM env in my case im using hyperkit you can follow the installation guide [here](https://minikube.sigs.k8s.io/docs/drivers/hyperkit/)


```shell
minikube start --driver=hyperkit
```

### Install Istio-ingress (Network gateway)
Istio is an open-source implementation of the service mesh originally developed by IBM, Google, and Lyft. It can layer transparently onto a distributed application and provide all the benefits of a service mesh like traffic management, security, and observability.


To install istio you can follow this [link](https://istio.io/latest/docs/setup/getting-started/)

```shell
curl -L https://istio.io/downloadIstio | sh -
```
```shell
cd istio-1.16.0  
```
```shell
export PATH=$PWD/bin:$PATH 
```
To install
```shell
istioctl install --set profile=demo -y
```
If you face resource allocation issues try to increase the memory set to minikube and reinstall

```shell
minikube config set memory 4096
```
Or alternatively start it with a resource allocation:

```shell
 minikube start --driver=hyperkit --memory 8192 --cpus 4
```

If everything went well you should be able to see the default ***istio-system*** namespace created you can confirm by get all namespaces:
```shell
kubectl get ns
```

To check the installed istio components:

```shell
kubectl get all -n istio-system
```
* You can see the control plane ***istiod*** has been configured including the ***egress***(exit traffic points from the mesh) and 
and ***ingress*** (allows you to define entry points into the mesh for incoming traffic) gateways.

### Application Ks8 resources (Kubernetes resources)

Let's create the namespace for our app called backend-services

```shell
kubectl create namespace backend-services
```

For the istio to be able to inject envoy proxies to our pods we need to inject istio-inject to our namespae:

To check the labels associated to a namespace:

```shell
kubectl get ns backend-services --show-labels
```

To enable istio injection to our backend-services namespace:

```shell
kubectl label namespace backend-services istio-injection=enabled
```
### Application Ks8 resources (Kubernetes resources)
Now create a folder called **k8s** in you root folder with a spring-native.yaml file with the following configs:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-native
  namespace: backend-services
  labels:
    app: spring-native
spec:
  replicas: 1
  selector:
    matchLabels:
      app: spring-native
  template:
    metadata:
      labels:
        app: spring-native
    spec:
      containers:
        - name: spring-native
          image: spring/mynative:latest
          ports:
            - containerPort: 8080
          imagePullPolicy: Never #we want to pull the image locally

---
apiVersion: v1
kind: Service
metadata:
  name: spring-native
  namespace: backend-services
spec:
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
  selector:
    app: spring-native
  type: ClusterIP
```
Notice in this file i have created the Kubernetes deployment and service configs with 1 replica/container.

* **NB** ImagePolicy pull policy configs
If your image is in a registry somewhere you can skip this section


Notice the imagePullPolicy is set to Never, this is because we don't want to pull the image from a public registry since it 
is available locally.

To fix this, I use the minikube docker-env command that outputs environment variables needed to point the local Docker daemon to the minikube internal Docker registry:
```shell
minikube docker-env
export DOCKER_TLS_VERIFY="1"
export DOCKER_HOST="tcp://192.168.64.2:2376"
export DOCKER_CERT_PATH="/Users/lameck/.minikube/certs"
export MINIKUBE_ACTIVE_DOCKERD="minikube"

# To point your shell to minikube's docker-daemon, run:
# eval $(minikube -p minikube docker-env)
```
To apply these variables run the proposed command:

```shell
eval $(minikube -p minikube docker-env)
```

I now let's  build the image once again, so that it’s installed in the minikube registry, instead of the local one:
```shell
docker build -f Dockerfile.native  -t spring/mynative .
```
* Now that we have the image is build in our minikube local registry let try to spin the pods up


### Spinning the pods
To deploy the service to our minikube cluster run the following command:
```shell
kubectl apply -f k8s/spring-native.yaml -n backend-services
```
Notice we have specified the namespace we want to deploy to.

You can check all of our application resources deployed by running:

```shell
kubectl get all -n backend-services
```
```shell
 kubectl get all -n backend-services
NAME                                 READY   STATUS    RESTARTS   AGE
pod/spring-native-7cf9f8d8f6-hn4gd   2/2     Running   0          3m16s

NAME                    TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
service/spring-native   LoadBalancer   10.106.28.73   <pending>     8080:32000/TCP   3m16s

NAME                            READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/spring-native   1/1     1            1           3m16s

NAME                                       DESIRED   CURRENT   READY   AGE
replicaset.apps/spring-native-7cf9f8d8f6   1         1         1       3m16s

```
Notice how we have two running pods 

Let's try to describe that pod and see whats going on:

```shell
kubectl describe pod spring-native-7cf9f8d8f6-hn4gd -n backend-services
```
If you check the running container section you can see the istio-proxy was injected on our running pod, remember 
we enabled istio-injection to our backend-services namespace.

So far so good if you ask me , now let's try to test our service :

### Accessing this Application (Gateway and VirtualService)

Since we are using istio as our service mesh, istio by default does not allow traffic in and out of our cluster by default. Istio uses gateways to manage inbound and outbound traffic.

Istio by default comes with preconfigured gateway proxy deployments i.e istio-ingressgateway and istio-egressgateway.
We need to create our own Gateway and Virtual Service to allow traffic in to our spring-native service.
Inside the k8s service create another folder called istio with the following files , gateway.yaml and virtual-service.yaml respectively:

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: spring-native-gateway
  namespace: backend-services
spec:
  selector:
    istio: ingressgateway
  servers:
    - port:
        number: 80
        name: http
        protocol: HTTP
      hosts:
        - "*"
```
```yaml
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: spring-native
  namespace: backend-services
spec:
  hosts:
    - "*"
  gateways:
    - spring-native-gateway
  http:
    - match:
        - uri:
            prefix: /api/customers
      route:
        - destination:
            host: spring-native
            port:
              number: 8080
```
The above scripts create a Gateway called spring-native-gateway inside our backend-services namespace. Inside the Virtual Service we have defined the url we want to allow access to (api/customers) and a destination route which allows us to point to a specific service i.e spring-native service.
You can apply the files:
```shell
kubectl apply -f k8s/istio/gateway.yaml
kubectl apply -f k8s/istio/virtual-service.yaml
```
Since we are using our local minikube cluster and we don't have an external IP allocated we can demonstrate the ingressgateway by port-forwarding the gateway LoadBalancer.
```shell
kubectl port-forward service/istio-ingressgateway 8002:80 -n istio-system
```
You can always port-forward the service directly but kinda defeats the istio demo purpose.

Lets curl our endpoint:
```shell
> curl http://localhost:8080/api/customers
data:{"id":1,"firstName":"Daniel","lastName":null,"email":null}

data:{"id":2,"firstName":"Peter","lastName":null,"email":null}

data:{"id":3,"firstName":"Mary","lastName":null,"email":null}

data:{"id":4,"firstName":"Terryn","lastName":null,"email":null}
```
Viola!! Congratulations you have successfully deployed a spring native application to kubernetes.

## Metrics and Tracing
The beauty of using istio as a service mesh is it comes with addons that allows you to do monitoring and data visualisation.

To install these addons just navigate to the istio installation folder we downloaded earlier:
```shell
cd ~/istio-1.16.0/samples/addons
```
In that folder you can see we have a couple of components including:
* grafana : open source data visual tool for metrics data
* prometheus: for monitoring anything in the cluster including memory , cpu, and other kubernetes components
* kiali : It helps you understand the structure and health of your service mesh by monitoring traffic flow to infer the topology and report errors.

To install these different addons metrics and tracing component:
```shell
kubectl apply -f .
```
You can check the installed components
```shell
kubectl get all -n istio-system
```
Lets port forward grafana
```shell
 kubectl port-forward svc/grafana 3000 -n istio-system
```
Now visit your localhost:3000 you should be able to see the dash.

Okay this has been a very long one i'm planning to do more tutorials on kubernetes and microserves, if you enjoy
this kind of staff don't forget to drop a follow and like, also you can subscribe to my youtube channel [@stackdev-io](https://www.youtube.com/channel/UCacNBWW7T2j_St593VHvulg).

Happy Coding !!

### Code Repo
Code Github Repo :point_right: [here](https://github.com/nyakaz73/spring-native)

## Created and Mantained by
* :computer: Github: [Tafadzwa Lameck Nyamukapa](https://github.com/nyakaz73) :
* :memo: Email:  [tafadzwalnyamukapa@gmail.com]
* :tv: Youtube: [@stackdev-io](https://www.youtube.com/channel/UCacNBWW7T2j_St593VHvulg)

### License

```
MIT License

Copyright (c) 2022 Tafadzwa Lameck Nyamukapa
```

