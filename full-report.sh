#!/bin/bash
EMMA_JAR=~/.m2/repository/emma/emma/2.1.5320/emma-2.1.5320.jar

cd demo-note/target/emma

java -cp $EMMA_JAR emma report -r html -in coverage.em,coverage.ec -sp ../../src/main/java/

open coverage/index.html
