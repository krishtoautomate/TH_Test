Installation:
Install Java jdk 9
set JAVA_HOME
Install Maven
set MAVEN_HOME / M2_HOME

Eclipse/injelliJ: Note: Ignore if using command prompt / terminal for execution
Install Maven and TestNG plugin

Execute:
mvn clean compile test -Dsurefire.suiteXmlFiles=src/test/resources/TestNG.xml

common problems:
for errors like : unable to find valid certification
Please check your vpn or proxy info

Validate Report:
Open link generated after execution