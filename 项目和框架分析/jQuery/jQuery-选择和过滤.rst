*********
jQuery选择器
*********

``jQuery`` 的基本设计思想和主要用法，就是"选择某个网页元素，然后对其进行某种操作"。这是它区别于其他 ``Javascript`` 库的根本特点。

使用 ``jQuery`` 的第一步，往往就是将一个选择表达式，放进构造函数 ``jQuery()`` （简写为 ``$`` ），然后得到被选中的元素。

简单选择器
==========
简单选择器主要包括5种类型

+-------------------------------+------------------+------------------------------------------------------------+
| 选择器                        | 实例             | 选取                                                       |
+===============================+==================+============================================================+
| ``*``                         | ``$("*")``       | 所有元素                                                   |
+-------------------------------+------------------+------------------------------------------------------------+
| #id                           | $("#lastname")   | id="lastname" 的元素                                       |
+-------------------------------+------------------+------------------------------------------------------------+
| .class                        | $(".intro")      | 所有 class="intro" 的元素                                  |
+-------------------------------+------------------+------------------------------------------------------------+
| element                       | $("p")           | 所有 <p> 元素                                              |
+-------------------------------+------------------+------------------------------------------------------------+
| .class.class                  | $(".intro.demo") | 所有 class="intro" 且 class="demo" 的元素                  |
+-------------------------------+------------------+------------------------------------------------------------+
| selector1,selector2,selectorN | $("p,.intro")    | 分别选择选择器中每个选择器匹配的元素，然后合并返回所有元素 |
+-------------------------------+------------------+------------------------------------------------------------+

关系选择器
==========
层级选择器能够根据元素之间的关系进行匹配操作，主要包括选择器、子选择器、相邻选择器和兄弟选择器。

+---------------------+------------------------------------------------------+
| 选择器              | 说明                                                 |
+=====================+======================================================+
| ancestor descendant | 在给定的祖先元素下匹配所有的后代元素。               |
+---------------------+------------------------------------------------------+
| parent > child      | 在给定的父元素下匹配所有的子元素。                   |
+---------------------+------------------------------------------------------+
| prev + next         | 匹配所有紧接在 prev 元素后的 next 元素。只选择一个。 |
+---------------------+------------------------------------------------------+
| prev ~ siblings     | 匹配 prev 元素之后的所有 siblings 元素。             |
+---------------------+------------------------------------------------------+

过滤选择器可
==========
定位过滤器主要是根据编号和排位筛选特定位置上的元素，或者过滤掉特定元素。

选择器  说明
:first  匹配找到的第一个元素。
:last  匹配找到的最后一个元素。

属性选择器
==========



表单选择器
==========





