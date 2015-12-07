Put your twitter4j keys and tokens and Google API keys into twitter4j.properties in a following format :

oauth.consumerKey=******************************
oauth.consumerSecret=******************************
oauth.accessToken=******************************
oauth.accessTokenSecret=******************************
googleApiKey=******************************


To retrieve a Google API Key follow the instructions on https://developers.google.com/maps/web-services/client-library
After compilation you can run program with:

java -jar target/xmanatee-1.0-SNAPSHOT-jar-with-dependencies.jar -q java -p "Moscow" -s
or
mvn exec:java -Dexec.mainClass="ru.fizteh.fivt.students.xmanatee.twittster.Twittster" -Dexec.args="--query java -s --place Moscow"