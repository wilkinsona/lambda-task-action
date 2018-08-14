# Up-to-date checks broken by lambda task action

This project attempts to reproduce the problem described in
[Gradle 5510](https://github.com/gradle/gradle/issues/5510). The problem only appears to
occur when building without the daemon and when changing the command line arguments used
to launch the build. For example, adding and removing `--info` triggers the problem.

## Environment

```
------------------------------------------------------------
Gradle 4.9
------------------------------------------------------------

Build time:   2018-07-16 08:14:03 UTC
Revision:     efcf8c1cf533b03c70f394f270f46a174c738efc

Kotlin DSL:   0.18.4
Kotlin:       1.2.41
Groovy:       2.4.12
Ant:          Apache Ant(TM) version 1.9.11 compiled on March 23 2018
JVM:          1.8.0_151 (Oracle Corporation 25.151-b12)
OS:           Mac OS X 10.12.6 x86_64
```

## Steps to reproduce

The following examples use `--console=plain` to make the behaviour clearer. The bug also
occurs without it.

First, run a clean build to ensure a known starting point:

```
./gradlew clean --no-daemon --console=plain
> Task :buildSrc:compileJava UP-TO-DATE
> Task :buildSrc:compileGroovy NO-SOURCE
> Task :buildSrc:processResources UP-TO-DATE
> Task :buildSrc:classes UP-TO-DATE
> Task :buildSrc:jar UP-TO-DATE
> Task :buildSrc:assemble UP-TO-DATE
> Task :buildSrc:compileTestJava NO-SOURCE
> Task :buildSrc:compileTestGroovy NO-SOURCE
> Task :buildSrc:processTestResources NO-SOURCE
> Task :buildSrc:testClasses UP-TO-DATE
> Task :buildSrc:test NO-SOURCE
> Task :buildSrc:check UP-TO-DATE
> Task :buildSrc:build UP-TO-DATE
> Task :clean

BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```

Now run the `compileJava` task:

```
./gradlew compileJava --no-daemon --console=plain
> Task :buildSrc:compileJava UP-TO-DATE
> Task :buildSrc:compileGroovy NO-SOURCE
> Task :buildSrc:processResources UP-TO-DATE
> Task :buildSrc:classes UP-TO-DATE
> Task :buildSrc:jar UP-TO-DATE
> Task :buildSrc:assemble UP-TO-DATE
> Task :buildSrc:compileTestJava NO-SOURCE
> Task :buildSrc:compileTestGroovy NO-SOURCE
> Task :buildSrc:processTestResources NO-SOURCE
> Task :buildSrc:testClasses UP-TO-DATE
> Task :buildSrc:test NO-SOURCE
> Task :buildSrc:check UP-TO-DATE
> Task :buildSrc:build UP-TO-DATE
> Task :compileJava

BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```

The task was executed as expected.

Next, run `compileJava` again:

```
./gradlew compileJava --no-daemon --console=plain
> Task :buildSrc:compileJava UP-TO-DATE
> Task :buildSrc:compileGroovy NO-SOURCE
> Task :buildSrc:processResources UP-TO-DATE
> Task :buildSrc:classes UP-TO-DATE
> Task :buildSrc:jar UP-TO-DATE
> Task :buildSrc:assemble UP-TO-DATE
> Task :buildSrc:compileTestJava NO-SOURCE
> Task :buildSrc:compileTestGroovy NO-SOURCE
> Task :buildSrc:processTestResources NO-SOURCE
> Task :buildSrc:testClasses UP-TO-DATE
> Task :buildSrc:test NO-SOURCE
> Task :buildSrc:check UP-TO-DATE
> Task :buildSrc:build UP-TO-DATE
> Task :compileJava UP-TO-DATE

BUILD SUCCESSFUL in 0s
1 actionable task: 1 up-to-date
```

The task was up-to-date as expected.

Next, run `compileJava` again but also with `--info`:

```
./gradlew compileJava --no-daemon --console=plain --info
Initialized native services in: /Users/awilkinson/.gradle/native
Using 8 worker leases.
Starting Build

> Configure project :buildSrc
Evaluating project ':buildSrc' using build file '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/build.gradle'.
Selected primary task 'build' from project :
:buildSrc:compileJava (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:compileJava UP-TO-DATE
Skipping task ':buildSrc:compileJava' as it is up-to-date.
:buildSrc:compileJava (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.112 secs.
:buildSrc:compileGroovy (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:compileGroovy NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/main/groovy', not found
Skipping task ':buildSrc:compileGroovy' as it has no source files and no previous output files.
:buildSrc:compileGroovy (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.002 secs.
:buildSrc:processResources (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:processResources UP-TO-DATE
Skipping task ':buildSrc:processResources' as it is up-to-date.
:buildSrc:processResources (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.006 secs.
:buildSrc:classes (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:classes UP-TO-DATE
Skipping task ':buildSrc:classes' as it has no actions.
:buildSrc:classes (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.0 secs.
:buildSrc:jar (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:jar UP-TO-DATE
Skipping task ':buildSrc:jar' as it is up-to-date.
:buildSrc:jar (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.008 secs.
:buildSrc:assemble (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:assemble UP-TO-DATE
Skipping task ':buildSrc:assemble' as it has no actions.
:buildSrc:assemble (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.0 secs.
:buildSrc:compileTestJava (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:compileTestJava NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/test/java', not found
Skipping task ':buildSrc:compileTestJava' as it has no source files and no previous output files.
:buildSrc:compileTestJava (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.001 secs.
:buildSrc:compileTestGroovy (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:compileTestGroovy NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/test/groovy', not found
Skipping task ':buildSrc:compileTestGroovy' as it has no source files and no previous output files.
:buildSrc:compileTestGroovy (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.001 secs.
:buildSrc:processTestResources (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:processTestResources NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/test/resources', not found
Skipping task ':buildSrc:processTestResources' as it has no source files and no previous output files.
:buildSrc:processTestResources (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.001 secs.
:buildSrc:testClasses (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:testClasses UP-TO-DATE
Skipping task ':buildSrc:testClasses' as it has no actions.
:buildSrc:testClasses (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.0 secs.
:buildSrc:test (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:test NO-SOURCE
Skipping task ':buildSrc:test' as it has no source files and no previous output files.
:buildSrc:test (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.003 secs.
:buildSrc:check (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:check UP-TO-DATE
Skipping task ':buildSrc:check' as it has no actions.
:buildSrc:check (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.0 secs.
:buildSrc:build (Thread[Task worker for ':buildSrc',5,main]) started.

> Task :buildSrc:build UP-TO-DATE
Skipping task ':buildSrc:build' as it has no actions.
:buildSrc:build (Thread[Task worker for ':buildSrc',5,main]) completed. Took 0.0 secs.
Settings evaluated using settings file '/Users/awilkinson/dev/temp/lambda-task-action/settings.gradle'.
Projects loaded. Root project using build file '/Users/awilkinson/dev/temp/lambda-task-action/build.gradle'.
Included projects: [root project 'lambda-task-action']

> Configure project :
Evaluating root project 'lambda-task-action' using build file '/Users/awilkinson/dev/temp/lambda-task-action/build.gradle'.
All projects evaluated.
Selected primary task 'compileJava' from project :
Tasks to be executed: [task ':compileJava']
:compileJava (Thread[Task worker for ':',5,main]) started.

> Task :compileJava
Task ':compileJava' is not up-to-date because:
  Task ':compileJava' has additional actions that have changed
All input files are considered out-of-date for incremental task ':compileJava'.
Compiling with JDK Java compiler API.
:compileJava (Thread[Task worker for ':',5,main]) completed. Took 0.296 secs.

BUILD SUCCESSFUL in 2s
1 actionable task: 1 executed
```

Unexpected, `compileJava` was executed.

Next, run `compileJava` with `--info` again:

```
./gradlew compileJava --no-daemon --console=plain --info
Initialized native services in: /Users/awilkinson/.gradle/native
Using 8 worker leases.
Starting Build

> Configure project :buildSrc
Evaluating project ':buildSrc' using build file '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/build.gradle'.
Selected primary task 'build' from project :
:buildSrc:compileJava (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:compileJava UP-TO-DATE
Skipping task ':buildSrc:compileJava' as it is up-to-date.
:buildSrc:compileJava (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.112 secs.
:buildSrc:compileGroovy (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:compileGroovy NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/main/groovy', not found
Skipping task ':buildSrc:compileGroovy' as it has no source files and no previous output files.
:buildSrc:compileGroovy (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.003 secs.
:buildSrc:processResources (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:processResources UP-TO-DATE
Skipping task ':buildSrc:processResources' as it is up-to-date.
:buildSrc:processResources (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.006 secs.
:buildSrc:classes (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:classes UP-TO-DATE
Skipping task ':buildSrc:classes' as it has no actions.
:buildSrc:classes (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.0 secs.
:buildSrc:jar (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:jar UP-TO-DATE
Skipping task ':buildSrc:jar' as it is up-to-date.
:buildSrc:jar (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.007 secs.
:buildSrc:assemble (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:assemble UP-TO-DATE
Skipping task ':buildSrc:assemble' as it has no actions.
:buildSrc:assemble (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.0 secs.
:buildSrc:compileTestJava (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:compileTestJava NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/test/java', not found
Skipping task ':buildSrc:compileTestJava' as it has no source files and no previous output files.
:buildSrc:compileTestJava (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.001 secs.
:buildSrc:compileTestGroovy (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:compileTestGroovy NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/test/groovy', not found
Skipping task ':buildSrc:compileTestGroovy' as it has no source files and no previous output files.
:buildSrc:compileTestGroovy (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.001 secs.
:buildSrc:processTestResources (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:processTestResources NO-SOURCE
file or directory '/Users/awilkinson/dev/temp/lambda-task-action/buildSrc/src/test/resources', not found
Skipping task ':buildSrc:processTestResources' as it has no source files and no previous output files.
:buildSrc:processTestResources (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.001 secs.
:buildSrc:testClasses (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:testClasses UP-TO-DATE
Skipping task ':buildSrc:testClasses' as it has no actions.
:buildSrc:testClasses (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.0 secs.
:buildSrc:test (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:test NO-SOURCE
Skipping task ':buildSrc:test' as it has no source files and no previous output files.
:buildSrc:test (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.002 secs.
:buildSrc:check (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:check UP-TO-DATE
Skipping task ':buildSrc:check' as it has no actions.
:buildSrc:check (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.0 secs.
:buildSrc:build (Thread[Task worker for ':buildSrc' Thread 2,5,main]) started.

> Task :buildSrc:build UP-TO-DATE
Skipping task ':buildSrc:build' as it has no actions.
:buildSrc:build (Thread[Task worker for ':buildSrc' Thread 2,5,main]) completed. Took 0.0 secs.
Settings evaluated using settings file '/Users/awilkinson/dev/temp/lambda-task-action/settings.gradle'.
Projects loaded. Root project using build file '/Users/awilkinson/dev/temp/lambda-task-action/build.gradle'.
Included projects: [root project 'lambda-task-action']

> Configure project :
Evaluating root project 'lambda-task-action' using build file '/Users/awilkinson/dev/temp/lambda-task-action/build.gradle'.
All projects evaluated.
Selected primary task 'compileJava' from project :
Tasks to be executed: [task ':compileJava']
:compileJava (Thread[Task worker for ':',5,main]) started.

> Task :compileJava UP-TO-DATE
Skipping task ':compileJava' as it is up-to-date.
:compileJava (Thread[Task worker for ':',5,main]) completed. Took 0.009 secs.

BUILD SUCCESSFUL in 2s
1 actionable task: 1 up-to-date
```

This time it was correctly identified as being up-to-date.

Now run `compileJava` again, but without `--info` this time:

```
 ./gradlew compileJava --no-daemon --console=plain
> Task :buildSrc:compileJava UP-TO-DATE
> Task :buildSrc:compileGroovy NO-SOURCE
> Task :buildSrc:processResources UP-TO-DATE
> Task :buildSrc:classes UP-TO-DATE
> Task :buildSrc:jar UP-TO-DATE
> Task :buildSrc:assemble UP-TO-DATE
> Task :buildSrc:compileTestJava NO-SOURCE
> Task :buildSrc:compileTestGroovy NO-SOURCE
> Task :buildSrc:processTestResources NO-SOURCE
> Task :buildSrc:testClasses UP-TO-DATE
> Task :buildSrc:test NO-SOURCE
> Task :buildSrc:check UP-TO-DATE
> Task :buildSrc:build UP-TO-DATE
> Task :compileJava

BUILD SUCCESSFUL in 2s
1 actionable task: 1 executed
```

It has been executed unexpectedly once again.

Lastly, run `compileJava` without `--info` again:

```
./gradlew compileJava --no-daemon --console=plain
> Task :buildSrc:compileJava UP-TO-DATE
> Task :buildSrc:compileGroovy NO-SOURCE
> Task :buildSrc:processResources UP-TO-DATE
> Task :buildSrc:classes UP-TO-DATE
> Task :buildSrc:jar UP-TO-DATE
> Task :buildSrc:assemble UP-TO-DATE
> Task :buildSrc:compileTestJava NO-SOURCE
> Task :buildSrc:compileTestGroovy NO-SOURCE
> Task :buildSrc:processTestResources NO-SOURCE
> Task :buildSrc:testClasses UP-TO-DATE
> Task :buildSrc:test NO-SOURCE
> Task :buildSrc:check UP-TO-DATE
> Task :buildSrc:build UP-TO-DATE
> Task :compileJava UP-TO-DATE

BUILD SUCCESSFUL in 2s
1 actionable task: 1 up-to-date
```

This time it is up-to-date as expected.