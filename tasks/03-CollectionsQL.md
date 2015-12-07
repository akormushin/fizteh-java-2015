## CollectionsQL

Нужно написать библиотеку, которая бы позволяла делать SQL-подобные запросы по коллекциям.

Синтаксис:
```
select_statement := 
from(<collection>)
{.select[Distinct](<result_class>, <functions as constructor args>...) 
 | .select[Distinct](<function>) }
[.where(<predicate>)]
[.groupBy(<function>, ...)]
[.having(<predicate>)]
[.orderBy(<comparator>, ...)]
[.limit(<lines>)]
[.union().<select_statement>]
```

Пример запроса:
```
Iterable<Statistics> statistics =
                from(list(
                        student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                        student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                        student("smith", LocalDate.parse("1986-08-06"), "495"),
                        student("petrov", LocalDate.parse("2006-08-06"), "494")))
                        .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                        .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
                        .groupBy(Student::getName)
                        .having(s -> s.getCount() > 0)
                        .orderBy(asc(Statistics::getGroup), desc(count(Statistics::getCount)))
                        .limit(100)
                        .union()
                        .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                        .selectDistinct(Statistics.class, s -> "all", count(s -> 1), avg(Student::age))
                        .execute();
                        
statistics: [Statistics{group=all,count=1,avg=30},Statistics{group=494,count=1,avg=29},Statistics{group=495,count=1,avg=29}]                        
```
Нужно реализовать всю функциональность из классов Aggregates, Conditions, OrderByConditions, Sources.

Семантика операций как в SQL:
  * select(<class>, <expr>, ...) - для каждой строчки результирующего списка создаёт объект класса, передавая в конструктов результаты вычисления выражений в качестве аргументов
  * select(<expr>) - выводит список результатов выражения
  * selectDistinct(<class>, <expr>, ...) - см. select. Оставляет только уникальные строки. Для результирующего класса должен быть реализован адекватный equals() и hashCode().
  * selectDistinct(<expr>) - выводит список уникальных результатов выражения
  * from(<list>) - указывает, из какого источника брать данные
  * groupBy(<expr>, ...) - группирует результат по заданным выражениям. Как и в SQL, при наличии group by, в select могут быть только выражения, по которым группировали или агрегатные функции.
  * having(<predicate>, ...) - фильтрует результат группировки, то есть предикат применяется к объекту из select
  * orderBy(<comparator>, ...) - упорядочивает результат согласно компаратору
  * limit(<amount>) - оставить только <amount> результатов запроса 
  * union() - объединяет результаты двух запросов в один. Запросы должны иметь одинаковый тип результата.
   
Aгрегатные функции (доп.задание со звёздочкой):
  * min(<expr>) - минимальное значение выражения для элементов списка 
  * max(<expr>) - максимальное значение выражения для элементов списка
  * count(<expr>) - количество элементов из списка, для которых выражение принимает значение отличное от null
  * avg(<expr>) - среднее значение выражения для элементов списка

* Запрос запускается методом execute(). 
* Все запросы должны работать максимально эффективно по времени и памяти (асимптотически).  
* Код нужно покрыть модульными тестами.
* Покрытие тестами должно быть не менее 70% строк кода, лучше более.
* Тесты должны покрывать все требования + возможные исключения. То есть, если у вас какой-нибудь метод при каком-то понятном условии может выкинуть Exception - то этот случай должен быть покрыт тестом.
Тесты можно запускать командой ```mvn test``` или из IDE, так же как и ```main()```.

## Рекомендации:
1. Смотрите примеры: 
    * [CollectionQuery](/akormushin/src/main/java/ru/fizteh/fivt/students/akormushin/collectionquery/CollectionQuery.java)
2. Обращайтесь к исходникам Stream API и Hamcrest Matchers.



