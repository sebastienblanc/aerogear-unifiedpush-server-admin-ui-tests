# AeroGear Unified Push Server Admin UI Tests [![Build Status](https://travis-ci.org/aerogear/aerogear-unifiedpush-server-admin-ui-tests.png?branch=master)](https://travis-ci.org/aerogear/aerogear-unifiedpush-server-admin-ui-tests)
This project contains the functional and acceptance tests for the aerogear-unified-push-server-admin-ui.

The [Arquillian](http://arquillian.org/) testing platform is used to enable the testing automation. Arquillian integrates transparently with the testing framework which is JUnit in this case.

## Functional Test Content
The Functional Test defines the three core aspects needed for the execution of an [Arquillian](http://arquillian.org/) test case:

- container — the runtime environment
- deployment — the process of dispatching an artifact to a container
- archive — a packaged assembly of code, configuration and resources

The container's configuration resides in the [Arquillian XML](https://github.com/aerogear/aerogear-unifiedpush-server-admin-ui-tests/blob/master/src/test/resources/arquillian.xml) configuration file while the deployment and the archive are defined in the [Deployments](https://github.com/aerogear/aerogear-unifiedpush-server-admin-ui-tests/blob/master/src/test/java/org/jboss/aerogear/unifiedpush/admin/ui/test/Deployments.java) file.

The test case is dispatched to the container's environment through coordination with ShrinkWrap, which is used to declaratively define a custom Java EE archive that encapsulates the test class and its dependent resources. Arquillian packages the ShrinkWrap defined archive at runtime and deploys it to the target container. It then negotiates the execution of the test methods and captures the test results using remote communication with the server. Finally, Arquillian undeploys the test archive.

The [POM](https://github.com/aerogear/aerogear-unifiedpush-server-admin-ui-tests/blob/master/pom.xml) file contains two profiles:

* arq-jboss-managed — managed container 
* arq-jboss-remote — remote container

By default the arq-jboss-managed (managed container) profile is active. An Arquillian managed container is a remote container whose lifecycle is managed by Arquillian. The specific profile is also configured to download and unpack the JBoss Application Server 7 distribution zip from the Maven Central repository.

## Development approach/methodologies
The development approach is driven from the desire to decouple the testing algorithmic steps / scenarios from the implementation which is tied to a specific DOM structure. For that reason the Page Objects and Page Fragments patterns are used. The Page Objects pattern is used to encapsulate the tested page's structure into one class which contains all the page's parts together with all methods which you will find useful while testing it. The Page Fragments pattern encapsulates parts of the tested page into reusable pieces across all your tests.

## Functional Test Execution
The execution of the functional test is done through maven:

    mvn test

## Documentation

* [Arquillian Guides](http://arquillian.org/guides/)
