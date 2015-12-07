## CollectionsQL Ex

Добавить join'ы и подзапросы к collections ql.

Синтаксис:
```
select_statement := 
from(<collection>)
[.join(<collection>).{on(<predicate>) | .on(<key1_expr>, <key2_expr>)}]
{.select[Distinct](<result_class>, <functions as constructor args>...) 
 | .select[Distinct](<function>) }
[.where(<predicate>)]
[.groupBy(<function>, ...)]
[.having(<predicate>)]
[.orderBy(<comparator>, ...)]
[.limit(<lines>)]
[.union().<select_statement>]

collection := Iterable | Stream | <select_statement> 
```

Пример запроса:
```
Iterable<Tuple<String, String>> mentorsByStudent =
                from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                .join(list(new Group("494", "mr.sidorov")))
                .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                .execute();
                        
statistics: [Statistics{group=all,count=3,27},Statistics{group=494,count=2,avg=24},Statistics{group=495,count=1,avg=29}]                        
```

Новые операции:
  * select(<expr>) - выводит список результатов выражения
  * selectDistinct(<expr>) - выводит список уникальных результатов выражения
  * from(<list>|<select_statement>) - указывает, из какого источника брать данные
  * join(<list>).on(<predicate>) - inner join коллекций по условию.
  * join(<list>).on(<key1_expr>, <key2_expr>) - inner join коллекций по ключу. Должен работать за O(n) (hash-join)
   
## Рекомендации:
1. Смотрите примеры: 
    * [CollectionQuery](/akormushin/src/main/java/ru/fizteh/fivt/students/akormushin/collectionquery/CollectionQuery.java)
2. Обращайтесь к исходникам Stream API и Hamcrest Matchers.



