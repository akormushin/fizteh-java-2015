## CollectionsQL

Нужно написать библиотеку, которая бы позволяла делать SQL-подобные запросы по коллекциям.

Пример запроса:
```
Iterable<Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                        student("ivanov", LocalDate.parse("1986-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                        .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
                        .groupBy(Student::getName)
                        .having(s -> s.getCount() > 0)
                        .orderBy(asc(Student::getGroup), desc(count(Student::getGroup)))
                        .limit(100)
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                        .selectDistinct(Statistics.class, "all", count(s -> 1), avg(Student::age))
                        .execute();
```
Нужно реализовать всю функциональность из классов Aggregates, Conditions, OrderByConditions, Sources.

* Покрытие тестами должно быть не менее 70% строк кода, лучше более.
* Тесты должны покрывать все требования + возможные исключения. То есть, если у вас какой-нибудь метод при каком-то понятном условии может выкинуть Exception - то этот случай должен быть покрыт тестом.
Тесты можно запускать командой ```mvn test``` или из IDE, так же как и ```main()```.

## Рекомендации:
1. Смотрите примеры: 
    * [CollectionQuery](/akormushin/src/main/java/ru/fizteh/fivt/students/akormushin/collectionquery/CollectionQuery.java)



