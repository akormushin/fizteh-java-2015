## Workflow работы с github

Каждый студент делает [fork](https://help.github.com/articles/fork-a-repo) репозитория [fizteh-java-2015](https://github.com/akormushin/fizteh-java-2015). Работа
ведётся в собственном репозитории, в директории ```<Your github login>/src/main/java/ru/fizteh/fivt/students/<Your github login>/<task>```.

Добавление своего модуля:
# В корневой директории нужно создать maven модуль командой
```
mvn -B archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DgroupId=ru.fizteh.fivt.students -DartifactId=<Your github login>
```
# Добавить свой модуль в родительский pom.xml.
# Добавить в свой pom.xml
```
<parent>
    <groupId>ru.fizteh.fivt.students</groupId>
    <artifactId>parent</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
```
Смотрите в качестве примера модуль akormushin.

После того, как задание выполнено и протестировано в собственном репозитории, необходимо создать
[pull request](https://help.github.com/articles/using-pull-requests) в репозиторий [fizteh-java-2015](https://github.com/akormushin/fizteh-java-2015). В заголовке
к pull request необходимо написать: ```Имя Фамилия, № группы, задание``` (например, ```Василий Иванов, 123, Shell```).
Также необходимо назначить pull request на своего семинариста.

В одном pull request должно быть решение только одной задачи. Если хочется сдавать параллельно несколько заданий,
необходимо создавать бранчи и делать pull request из бранчей.

Чтобы сократить количество итераций на проверку задания, полезно самостоятельно удостовериться, что Code Conventions
соблюдены.
