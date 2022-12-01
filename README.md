# Spring Boot (3)  Spring Native (GraalVM)
Kubernetes Java Native Application with Spring Boot 3 (Spring Native)

Okay!! so im just going to jump right in. As some of you might know that Spring Boot 3, Spring Framework 6 has just been
a few days ago before writing this article,  here are some of the feature we can get excited about :)
 * Java 17 Baseline
 * Records
 * Native Executables
 * Observability 

In this  read im mainly going to focuss on how we can build Kubernetes Cloud Native Java Applications using Spring
Native (Native Executables) with **GraalVM**.

*  Don't get me wrong Spring support for Native (Experimental) has already been out for a couple of years, but the reason for us to get
excited is the Spring team really took some time to address some issues and bug fixes that Spring Native had prior to 
this release, and at the time of this writing new milestones will be released.
   
Okay enough chit-chat :)

### Kubernetes Cloud Native Development
This is an idea of building applications and deploying them to a **Kubernetes** array short-lived containers while addressing
mainly two fundamental principles among the others which are Memory utilization and Boot up time. This deployment paradim
is what is called the **Container first approach** philosophy.


### Enter Graal VM
Graal VM is a high-performance JDK distribution writen of Java and other JVM langues from Oracle, that provides a
Native Image Builder for building native code and package it together with the VM into a standalone executable.

### How?? 
Staff that used to be done by Java Application at runtime i.e:

* Compiling source code &#8594; Load and parse configurations  &#8594; Analyse dependencies &#8594; Build dependency tree &#8594; Execute Code

is now being done at build time by **Graal VM**  using  a process called **Ahead of Time Compilation** that uses **JIT** (Just In time Compiler)

### Goal :information_desk_person:

This will inturn gives you a native execuatable that has a low memory foot-print and a crazy start up time.


### What is Spring Native





   
