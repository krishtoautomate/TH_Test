**Installation:**
Install Java jdk 9
set JAVA_HOME _environment variable_
Install Maven
set MAVEN_HOME / M2_HOME _environment variable_

**Mobile set-up:**
Install appium and set-up WebDriveragent provisioning with xcode(Require mac-os)
Install android-studio
set ANDROID_HOME environment variable

**Eclipse/injelliJ:** 
_Note: Ignore if using command prompt / terminal for execution_
Install Maven and TestNG plugin

**Execute:**
_note: update udid in src/test/resources/TestNG.xml for andorid or ios execution
and start appium in port 4723 before execution_
mvn clean compile test -Dsurefire.suiteXmlFiles=src/test/resources/TestNG.xml

**common problems:**
for errors like : unable to find valid certification
Please check your vpn or proxy info

Validate Report:
Open link generated after execution
