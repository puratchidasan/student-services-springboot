echo 'Running Springboot Java application'
echo 'Moving the jar file'
pwd
cp /soft/bap/home/vstsagent/_work/r3/a/student-services-Jenkins-CI/drop/com/in28minutes/springboot/student-services/*/student-services-*.jar /soft/bap/home/vstsagent/_work/r3/a/student-services-Jenkins-CI/drop/student-services.jar
echo 'Moved the jar file.'
#echo 'Killing Springboot Java application if any...'
#pgrep -f student-services | awk '{print $1}' | xargs kill -9 || true
echo 'Starting the springboot application....'
nohup java -jar student-services.jar &>/dev/null &