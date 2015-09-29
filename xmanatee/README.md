Put your twitter4j keys and tokens and Google API keys into mykeys.properties in a following format :

consumerKey=**************************
consumerSecret=**************************
accessToken=**************************
accessTokenSecret=**************************
googleApiKey=**************************

After compilation you can run program with:

java -jar target/xmanatee-1.0-SNAPSHOT-jar-with-dependencies.jar -q java -p "Moscow" -s
or
mvn exec:java -Dexec.mainClass="ru.fizteh.fivt.students.xmanatee.twittster.Twittster" -Dexec.args="--query java -s --place Moscow"