## Порядок сдачи заданий

## Инициализация репозитория на GitHub
1. Создайте [fork](https://help.github.com/articles/fork-a-repo) репозитория [fizteh-java-2015](https://github.com/akormushin/fizteh-java-2015). 
2. Создайте локальный клон своего репозитория
3. В корневой директории нужно создать maven модуль командой
  
  ```
  mvn -B archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart \
  -DgroupId=ru.fizteh.fivt.students -DartifactId=<Your github login>
  ```
4. Добавить свой модуль в родительский [pom.xml](pom.xml). Нужно, чтобы можно было из корня собирать все модули разом.
  
  ```
  <modules>
      <module>akormushin</module>
      <!--Add you module here-->
  </modules>
  ```
5. Добавить в свой [pom.xml](akormushin/pom.xml) ссылку на родительский модуль, если её там ещё не было. Нужно, чтобы унаследовать общие параметры сборки проекта.
  
  ```
  <parent>
      <groupId>ru.fizteh.fivt.students</groupId>
      <artifactId>parent</artifactId>
      <version>1.0-SNAPSHOT</version>
  </parent>
  ```
6. Смотрите в качестве примера модуль [akormushin](akormushin). 

## Работа над заданием
1. Свои классы нужно добавлять в директории ```<Your github login>/src/main/java/ru/fizteh/fivt/students/<Your github login>/<task>```.
2. Сборка модуля производится командой ```mvn package```.
3. Запускать приложение можно прямо из maven командой ```mvn exec:java -Dexec.mainClass=<Fully qualified main class name> -Dexec.args="<arguments>"```. Она сама добавит в classpath транзативно все необходимые библиотеки из pom.xml dependencies. 
  
  Также можно запускать с помощью команды java. Для этого нужно: 
  1. Выполнить единожды ```mvn dependency:copy-dependencies```. Эта команда скопирует все необходимые зависимости в target/dependency.
  2. Далее запускать командой ```java -cp "target/*:target/dependency/*" <Your main class name> <arguments>```
3. После того, как задание выполнено и протестировано в собственном репозитории, необходимо создать
[pull request](https://help.github.com/articles/using-pull-requests) в репозиторий [fizteh-java-2015](https://github.com/akormushin/fizteh-java-2015). В заголовке
к pull request необходимо написать: ```Имя Фамилия, № группы, задание``` (например, ```Василий Иванов, 123, Shell```).
Также необходимо назначить pull request на своего семинариста.
В одном pull request должно быть решение только одной задачи. Если хочется сдавать параллельно несколько заданий,
необходимо создавать бранчи и делать pull request из бранчей.
4. Результаты сборки можно смотреть [тут](https://travis-ci.org/akormushin/fizteh-java-2015)
5. Если pull-request не мержится "This branch has conflicts that must be resolved", надо смержить себе мои изменения.
5. Далее нужно провести ревью пулл реквестов как минимум 2х (двух) ваших одногруппников или ребят из параллельной группы, как вам удобно. 
  1. Отметить в [журнале успеваемости](https://docs.google.com/spreadsheets/d/1LhwKlMmQbG2aIBT0FmUS8HMmd5pcpWr0bnlDw7Ypkt4/edit?usp=sharing) в колонек Ревьюверы себя через запятую напротив фамилии того, кого ревьювите, например,  Каргальцев Степан хочет проверить задание 2 у Зерцалова Андрея: в Е9 нужно вписать Каргальцев Степан.
  1. Нужно себе в локальную ветку смержить пулл реквест. Например, для thefacetakt это:
  
    ```
    git checkout -b thefacetakt-master master
    git pull https://github.com/thefacetakt/fizteh-java-2015.git master
    ```
  2. Запустить приложение с разными параметрами и убедиться, что оно удовлетворяет спецификации (заданию) или указать на ошибки.
  3. Посмотреть код и попытаться выявить возможные нефункциональные ошибки и недочёты.
  4. После исправлений вашего коллеги - проверить ещё раз.
6. Задание считается выполненным, если:
  1. Приложение функционально соответствует спецификации
  2. Код прошёл ревью и не содержит неустранённых замечаний. 
  3. Вы провели ревью pull request'ов как минимум 2х коллег и нашли как минимум 10 существенных замечаний. Hack: если вы или я обнаружили у вас в коде какую-то проблему - с большой вероятностью она будет у многих. Можно пройтись по 10ти pull request'ам и всем указать на неё же. PROFIT.
  
## Синхронизация с базовым репозиторием
Периодически синхронизируйтесь с базовым репозиторием, чтобы получать актуальные версии скриптов для сборки и примеров. 

1. Если в списке репозиториев, возвращаемых `git remote` у вас нет upstream, то добавьте себе удаленный репозиторий: 

  ```
  git remote add upstream https://github.com/akormushin/fizteh-java-2015.git
  ```
2. Чтобы подхватить изменения из базового репозитория, в своём master делайте

  ```
  git pull upstream master
  ```
