*****
变量作用域
*****

PHP中变量的作用域可以分为:

- 超全局变量: 在一个脚本的任何作用域里都可以被访问，可直接在局部范围里使用，不需要用 ``global`` 声明。比如 ``$GLOBALS`` , ``$_ENV`` , ``$_SERVER`` , ``$_GET`` , ``$_POST`` , ``$_FILES`` , ``$_SESSION`` , ``$_COOKIE`` 等。
- 全局变量: 声明的变量不在 ``class`` , ``function`` 等语言结构内部.如果要在 ``class`` , ``function`` 等内部使用全局变量，需要用关键词 ``global`` 或者超全局变量 ``$GLOBALS`` 。
- 局部变量: 在 ``class`` ， ``function`` 等结构语句内部声明的变量。
- 静态变量: 在 ``function`` 中使用关键词 ``static`` 声明的变量，静态变量的值保留直至当前请求的脚本运行结束，比如可以用来保存数据库连接对象。

php中include文件变量作用域
=========================

第一种情况：A文件include B文件，在B文件中可以调用A中的变量
-------------------------------------------------------

A文件代码：

.. code-block:: php

	<?php
	 $aaa = '123';
	 
	 include "B.php";

B文件代码:

.. code-block:: php

	<?php

	echo $aaa;

可以正常输出内容。

第二种情况：A文件include B文件，然后在A文件中可以调用B文件的变量
------------------------------------------------------------

A文件代码：

.. code-block:: php

	<?php

	include "B.php";

	echo $fff;

B文件代码：

.. code-block:: php

	<?php

	$fff = 'i am f';

这个时候是可以正常输出内容的。

第三种情况：B文件include A文件，然后在B文件函数中不能调用A文件的变量
---------------------------------------------------------------

A文件代码：

.. code-block:: php

	<?php

	$aa = 1;

B文件代码：

.. code-block:: php

	<?php

	include 'a.php';

	echo $aa; // 值为1
	function show(){
	    global $aa;
	    var_dump($aa); // 没有定义
	}

函数中的$aa没有值。

第四种情况：A文件的某个类的某个方法中调用B文件，然后在B文件中可以调用该方法中的变量
----------------------------------------------------------------------------

A文件代码：

.. code-block:: php

	<?php

	class test{
	  public function show(){
	    $bbb = 'abc';
	    include "B.php";
	  }
	}

	$t = new test;
	$t->show();

B文件的代码：

.. code-block:: php

	<?php

	echo $bbb;

这个时候是可以正常输出内容的。

第五种情况：A文件通过定义的一个函数引入B文件，在B文件中无法使用A中的变量，但是可以使用A文件中调用函数(display)中的变量
-------------------------------------------------------------------------------------------------------------

A文件代码：

.. code-block:: php

	<?php
	$aaa = '123';

	function display($file){
	  $bbb= 'asdasdas';
	  include $file;
	}

	display("B.php");

B文件代码：

.. code-block:: php

	<?php
	echo $aaa;
	echo $bbb;

运行后$aaa提示未定义，$bbb可以正常输出。


在PHP脚本中变量主要有：内置超级全局变量，一般的变量，常量，全局变量，静态变量等等，我们在使用它们的时候除了要正确地知道它们的语法以外，更重要的是，我们要知道它们在本质上的区别与联系—即它们的作用域的问题。

1.内置超级全局变量可以在脚本的任何地方使用和可见。即是说，如果我们在一个PHP页面中改变了其中的一个值，那么在其他PHP页面中使用时，它的值也会发生改变。

2.常量一旦被声明将可以在全局可见，也就是说，它们可以函数内外使用，但是这仅仅限于一个页面之中（包含我们通过include和include_once）包含进来的PHP脚本，但是在其他的页面之中就不能使用了。

3.在一个脚本中声明的全局变量在整个脚本中是可见的，但不是在函数内部，在函数内部的变量如果与全局变量名称相同，以函数内部的变量为准。

4.函数内部使用的变量声明为全局变量时，其名称要与全局变量的名称一致，在这样的情况下，我们就可以在函数中使用函数外部的全局变量了，这样就可以避免上一种因为函数内部的变量与外部的全局变量名称相同而覆盖了外部变量这样的情况。

5.在函数内部创建并声明为静态的变量无法在函数外部可见，但是可以在函数的多次执行过程中保持该值，最常见的情况就是在函数的递归执行的过程之中。
6.在函数内部创建的变量对函数来说是本地的，而当函数终止时，该变量也就不存在了。

超级全局变量的完整列表如下：
1.$GOBALS  所有全局变量数组
2.$_SERVER  服务器环境变量数组
3.$_POST  通过POST方法传递给该脚本的变量数组
4.$_GET  通过GET方法传递给该脚本的变量数组
5.$_COOKIE  cookie变量数组
6.$_FILES  与文件上传相关的变量数组
7.$ENV 环境变量数组
8.$_REQUEST 所有用户输入的变量数组包括$_GET $_POST $_COOKIE 所包含的输入内容
9.$_SESSION  会话变量数组

我们要注意的是：变量和常量另一个重要的差异是：常量只可以定义boolean(布尔型)，integer(整型)，float(浮点型) 和 string(字符串型)数据，但不能定义 resource类型的数据。



