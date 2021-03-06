sql优化工具使用之explain
=======================

关于 ``sql`` 优化，这个话题太大，我怕我说不好，因此本文仅以 ``sql`` 优化工具为题，如果对 ``sql`` 优化有兴趣，我建议去阅读下高性能 ``Mysql`` 这本书，我最近也在读，此文也算是我在阅读此书过程中的一些收获总结。

对于大部分开发人员来说，平常接触的无非就是增删改查这些基本操作，创建存储过程，视图等等都是 ``DBA`` 该干的活，但是想要把这些基本操作写的近乎完美也是一件难事。而 ``explain`` 显示了 ``MySQL`` 如何使用索引来处理 ``select`` 语句以及连接表。可以通过模拟 ``mysql`` 的优化器帮助选择更好的索引和写出更优化的查询语句。

首先，我们来明确下 ``explain`` 能干嘛 ?

- 表的读取顺序
- 数据读取操作的操作类型
- 哪些索引可以使用
- 哪些索引被实际使用
- 表之间的引用
- 每张表有多少行被优化器查询

说了这么多使用 ``explain`` 的好处,那么实际上到底该怎么玩? 答案： ``explain`` + 待执行的sql

- ``id`` ：决定表的读取顺序

  执行 ``select`` 语句查询的序列号，包含一组数字，表示查询中执行 ``select`` 子句或操作表的顺序

  它有三种情况：

  - ``id`` 相同，执行顺序由上至下；
  - ``id`` 不同，如果是子查询， ``id`` 的序号会递增， ``id`` 值越大优先级越高，越先被执行；
  - ``id`` 相同不同，同时存在，如果 ``id`` 相同，可以认为是一组，从上往下顺序执行，在所有组中， ``id`` 值越大，优先级越高，越先执行；

- ``select_type`` ：查询的类型，也就是数据读取操作的操作类型，他一共有以下 5 种：

  - ``simple`` ：简单的 ``select`` 查询，查询中不包含子查询或者 ``union`` ；
  - ``primary`` ：查询中若包含任何复杂的子查询，最外层查询则被标记；
  - ``subquery`` ：在 ``select`` 或者 ``where`` 列表中包含了子查询；
  - ``derived`` ：在 ``from`` 列表中包含的子查询被标记为 ``DERIVEDN`` (衍生表)， ``mysql`` 会递归执行这些子查询，把结果放临时表中；
  - ``union`` ：若第二个 ``select`` 出现在 ``union`` 之后，则被标记为 ``union`` ，若 ``union`` 包含在 ``from`` 子句的子查询中，外层 ``select`` 将被标记为： ``DERIVED`` ；
  - ``union result`` ：从 ``union`` 匿名临时表(即 ``union`` 合并的结果集)中获取 ``select`` 查询的结果；

- table ：显示了对应行正在访问那个表。一般为表名称或者表别名。

  当在 ``FROM`` 子句中有子查询时， ``table`` 列是 ``<derivedN>`` 的形式，其中 ``N`` 是子查询的 ``id`` 。看到的是derivedx(x是个数字,我的理解是第几步执行的结果)

- ``partitions`` ：显示查询将访问的分区；

- ``type`` ：访问类型排列

  显示查询使用了何种类型，即 ``MySQL`` 决定如何查找表中的行。从最好到最差依次是： ``system > const > eq_ref > ref > range > index > all``

  - ``system`` ：表只有一行记录(等于系统表)，这是 ``const`` 类型的特例，平时不会出现，这个也可忽略不计；
  - ``const`` ：表示通过索引一次就找到了， ``const`` 用于比较 ``primary key`` 或者 ``unique`` 索引。因为只匹配一行记录，所以很快。如果将主键置于 ``where`` 列表中， ``mysql`` 就能将该查询转换成一个常量；
  - ``eq_ref`` ：唯一性索引扫描，对于每一个索引键，表中只有一条记录与之匹配，常用于主键或唯一索引扫描；
  - ``ref`` ：非唯一性索引扫描，返回匹配某个单独值得所有行，本质上也是一种索引访问，它返回所有匹配某个单独值的行，然而，它可能会找到多个符合条件的行，所以它应该属于查找和扫描的混合体；
  - ``range`` ：就是一个有限制的索引扫描。只检索给定范围的行，使用一个索引来选择行， ``key`` 列显示使用哪个索引，一般就是在你的 ``where`` 语句中出现了 ``between`` 、 ``<`` 、 ``>`` 、 ``in`` 等的查询；这种范围索引扫描比全表扫描要好，因为它只需要开始于索引的某一个点，结束于另一个点，不用扫描全部索引；
  - ``index`` ： ``index`` 与 ``all`` 区别为 ``index`` 类型只遍历索引树，这通常比 ``all`` 快，因为索引文件通常比数据文件小；也就是说虽然 ``all`` 和 ``index`` 都是读写表，但 ``index`` 是从索引中读取的，而 ``all`` 是从硬盘中读的；
  - ``all`` ：也就是全表扫描；

  备注：一般来说,得保证查询至少达到range级别,最好能达到ref.

- ``possible_keys`` ：显示可能会被应用到这张表的索引，一个或者多个；查询涉及到的字段上若存在索引，则该索引将被列出，但不一定被查询实际使用到；
- ``key`` ：实际使用到的索引。如果为 ``null`` ，则没有使用索引；查询中若使用了覆盖索引，则该索引仅出现在 ``key`` 列表中；
- ``key_len`` ：表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度，在不损失精确性的情况下，长度越短越好； ``key_len`` 显示的值为索引字段的最大可能长度，并非实际使用长度，即 ``key_len`` 是根据表定义计算而得，不是通过表内检索出的；如果 ``MySQL`` 正在使用的只是索引里的某些列，那么就可以用这个值来算出具体是哪些列。 http://www.cnblogs.com/xuanzhi201111/p/4554769.html
- ``ref`` ：显示索引的哪一列被使用了，如果可能的话，是一个常数，表示上述表的连接匹配条件，即哪些列或常量用于查找索引列上的值；
- ``rows`` ：根据表统计信息及索引选用情况，大致估算出找到所需的记录所需要读取的行数；
- ``filtered`` ：它显示的是针对表里符合某个条件( ``WHERE`` 子句或连接条件)的记录数的百分比所做的一个悲观估算。如果你把 ``rows`` 列和这个百分比相乘，就能看到 ``MySQL`` 估算它将和查询计划里前一个表关联的行数。
- ``Extra`` ：包含不适合在其它列中显示但十分重要的额外信息：

  - ``using filesort`` (出现这个东西不好)：说明 ``mysql`` 会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取， ``mysql`` 中无法利用索引完成的排序操作称为"文件排序"；

文件排序是通过相应的排序算法,将取得的数据在内存中进行排序: MySQL需要将数据在内存中进行排序，
所使用的内存区域也就是我们通过sort_buffer_size 系统变量所设置的排序区。
这个排序区是每个Thread 独享的，所以说可能在同一时刻在MySQL 中可能存在多个
 sort buffer 内存区域。
在MySQL中filesort 的实现算法实际上是有两种：
双路排序：是首先根据相应的条件取出相应的排序字段和可以直接定位行数据的行指针信息，然后在sort buffer 中进行排序。
单路排序：是一次性取出满足条件行的所有字段，然后在sort buffer中进行排序。
在MySQL4.1版本之前只有第一种排序算法双路排序，第二种算法是从MySQL4.1开始的改进算法，
主要目的是为了减少第一次算法中需要两次访问表数
据的 IO 操作，将两次变成了一次，但相应也会耗用更多的sortbuffer 空间。
当然，MySQL4.1开始的以后所有版本同时也支持第一种算法，
MySQL主要通过比较我们所设定的系统参数 max_length_for_sort_data的大小和Query 语句所取出的字段类型大小总和来判定需要使用哪一种排序算法。
如果 max_length_for_sort_data更大，则使用第二种优化后的算法，反之使用第一种算法。所以如果希望 ORDER BY 操作的效率尽可能的高，
一定要主义max_length_for_sort_data 参数的设置。曾经就有同事的数据库出现大量的排序等待，造成系统负载很高，而且响应时间变得很长，
最后查出正是因为MySQL 使用了传统的第一种排序算法而导致，在加大了max_length_for_sort_data 参数值之后，
系统负载马上得到了大的缓解，响应也快了很多。

优化Filesort：

- 加大 max_length_for_sort_data 参数的设置
- 去掉不必要的返回字段
- 增大 sort_buffer_size 参数设置

  - ``using temporary`` (出现这个东西更不好，使用到了临时表)：使用了临时表保存中间结果， ``Mysql`` 在对查询结果排序时使用临时表，常见于排序 ``order by`` 和分组查询 ``group by`` 。
  - ``using index`` ：表示相应的 ``select`` 操作中使用了覆盖索引(Covering Index)，避免了访问了表的数据行，效率不错！如果同时出现 ``using where`` ，表明索引被用来执行索引键值的查找；如果没有同时出现 ``using where`` ，表明索引用来读取数据而非执行查找操作；

    覆盖索引：理解方式一：就是 ``select`` 的数据列只用从索引列中就能取得，不必读取数据行， ``Mysql`` 可以利用索引返回 ``select`` 列表中的字段，而不必根据索引再次读取数据文件，换句话说查询列要被所建的索引列覆盖；理解方式二：索引是高效找到行的一个方法，但是一般数据库也能使用索引找到一个列的数据，因此它不必读取整个行，毕竟索引的叶子节点存储了索引数据；当能通过读取索引就可以得到想要的数据，那就不需要读取行了；一个索引包含了(或者覆盖了)满足查询结果的数据就叫做覆盖索引。

    注意：如果要使用覆盖索引，一定要注意 ``select`` 列表中只取出需要的列，不可 ``select *;`` 因为如果将所有的字段一起做索引会导致索引文件过大，查询性能下降；

  - ``using where`` ：使用了 ``where``
  - ``using join buffer`` ：使用了链接缓存；强调了在获取连接条件时没有使用索引，并且需要连接缓冲区来存储中间结果。如果出现了这个值，那应该注意，根据查询的具体情况可能需要添加索引来改进能。
  - ``impossible where`` ： ``where`` 子句的值总是 ``false`` ，不能用来获取任何元素；
  - ``select tables optimized away`` ： 在没有 ``group by`` 子句的情况下，基于索引优化 ``MIN/MAX`` 操作或者对于 ``MyISAM`` 存储引擎优化 ``count(*)`` 操作，不必等到执行阶段再进行计算，查询执行计划生成的阶段即完成优化；
  - ``distinct``

查看优化后的sql语句： ``explain extended sql`` 语句，然后 ``show warnings`` 查看。

分析 ``GROUP BY`` 与临时表的关系 :

- 如果 ``GROUP BY`` 的列没有索引,产生临时表；
- 如果 ``GROUP BY`` 时， ``SELECT`` 的列不止 ``GROUP BY`` 列一个，并且 ``GROUP BY`` 的列不是主键，产生临时表；
- 如果 ``GROUP BY`` 的列有索引， ``ORDER BY`` 的列没索引，产生临时表；
- 如果 ``GROUP BY`` 的列和 ``ORDER BY`` 的列不一样，即使都有索引也会产生临时表；
- 如果 ``GROUP BY`` 或 ``ORDER BY`` 的列不是来自 ``JOIN`` 语句第一个表，会产生临时表；
- 如果 ``DISTINCT`` 和 ``ORDER BY`` 的列没有索引，产生临时表；


explain https://juejin.im/post/5babb52c6fb9a05cd676c0f7
索引 https://juejin.im/entry/590427815c497d005832dab9
语句执行顺序 https://blog.csdn.net/u014044812/article/details/51004754