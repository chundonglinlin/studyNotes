***
运算符
***

``JavaScript`` 提供了相当丰富的运算符，运算符也是 ``JavaScript`` 语言的基础。通过运算符，可以将变量连接成语句，语句是 ``JavaScript`` 代码中的执行单位。

``JavaScript`` 的运算符并不比其他高级语言的运算符少，它同样提供了算术运算符、逻辑运算符、位运算符。 ``JavaScript`` 支持的运算符，与 ``Java`` 、 ``C`` 支持的运算符非常相似。下面依次介绍 ``JavaScript`` 中的运算符。

赋值运算符
=========
赋值运算符用于为变量指定变量值，与 ``Java`` 、 ``C`` 类似， ``JavaScript`` 也使用 ``=`` 作为赋值运算符。通常，使用赋值运算符将一个常量值赋给变量。见如下示例代码。

.. code-block:: js

	// 将变量str赋值为JavaScript
    var str = "JavaScript";
    // 将变量pi赋值为3.14
    var pi = 3.14;
    // 将变量赋值为true
    var visited = true;

除此之外，也可使用赋值运算符将一个变量的值赋给另一个变量。即如下代码也是正确的：

.. code-block:: js

	// 将变量str赋值为JavaScript
    var str = "JavaScript";
    // 将变量str的值赋给str2
    var str2 = str

与 ``Java`` 类似的是，赋值语句本身是有值的，赋值语句的值就是等号(=)右边被赋的值。因此，赋值运算符支持连续赋值，通过使用多个赋值运算符，可以一次为多个变量赋值，如下代码也是正确的：

.. code-block:: js

	// 为a, b , c ,d赋值，四个变量的值都是7
    var a = b = c = d = 7;
    // 输出四个变量的值。
    alert(a + '\n' + b + '\n' + c + '\n' + d);

赋值运算符还可用于将表达式的值赋给变量，如下代码也是正确的：

.. code-block:: js

	// 将变量x赋值为12.34
    var x = 12.34;
    // 将表达式的值赋给y
    var y = x + 5;
    // 输出y的值
    alert(y);

赋值运算符还可与其他运算符结合后，成为功能更加强大的赋值运算符，参见13.5.4节。

算术运算符
==========
``JavaScript`` 支持所有的基本算术运算符，这些算术运算符用于执行基本的数学运算：加、减、乘、除和求余等。下面是7个基本的算术运算符：

- ``++`` ：自加。这是个单目运算符，运算符既可以出现在操作数的左边，也可以出现在操作数的右边，但出现在左边和右边的效果是不一样的。
- ``--`` ：自减。这也是个单目运算符，效果与 ``++`` 基本相似，只是将操作数的值减1。

``JavaScript`` 并没有提供其他更复杂的运算符，如需要完成乘方、开方等运算，可借助于 ``Math`` 类的方法完成复杂的数学运算，见如下代码。

.. code-block:: js

	// 定义变量a为3.2
    var a = 3.2;
    // 求a的5次方，并将计算结果赋为b。
    var b = Math.pow(a , 5);
    // 输出b的值
    alert(b);
    // 求a的平方根，并将结果赋给c
    var c = Math.sqrt(a);
    // 输出c的值
    alert(c);
    // 计算随机数
    var d = Math.random();
    // 输出随机数d的值
    console.log(d);

``Math`` 类下包含了丰富的静态方法，用于完成各种复杂的数学运算。

- ``-`` : 除了可以作为减法运算符之外，还可以作为求负运算符。例如如下代码：
- ``+`` : 除了可作为数学的加法运算符外，还可作为字符串的连接运算符。

位运算符
========
``JavaScript`` 支持的位运算符与 ``Java`` 支持的位运算符基本相似，大致有如下7个位运算符。

- ``&`` ：按位与。
- ``|`` ：按位或。
- ``~`` ：按位非。
- ``^`` ：按位异或。
- ``<<`` ：左位移运算符。
- ``>>`` ：右位移运算符。
- ``>>>`` ：无符号右移运算符。

位运算符的运算结果表如下表所示。

+--------------+--------------+--------+--------+----------+
| 第一个运算数 | 第二个运算数 | 按位与 | 按位或 | 按位异或 |
+==============+==============+========+========+==========+
| 0            | 0            | 0      | 0      | 0        |
+--------------+--------------+--------+--------+----------+
| 0            | 1            | 0      | 1      | 1        |
+--------------+--------------+--------+--------+----------+
| 1            | 0            | 0      | 1      | 1        |
+--------------+--------------+--------+--------+----------+
| 1            | 1            | 1      | 1      | 0        |
+--------------+--------------+--------+--------+----------+

加强的赋值运算符
===============
赋值运算符可以与算术运算符、位运算符等结合，从而成为功能更加强大的运算符。当合后的加强运算符如下。

- ``+=`` ：对于 ``x+=y`` ，即对应于 ``x=x+y`` 。
- ``-=`` ：对于 ``x-=y`` ，即对应于 ``x=x-y`` 。
- ``*=`` ：对于 ``x*=y`` ，即对应于 ``x=x*y`` 。
- ``/=`` ：对于 ``x/=y`` ，即对应于 ``x=x/y`` 。
- ``%=`` ：对于 ``x%=y`` ，即对应于 ``x=x%y`` 。
- ``&=`` ：对于 ``x&=y`` ，即对应于 ``x=x&y`` 。
- ``|=`` ：对于 ``x|=y`` ，即对应于 ``x=x|y`` 。
- ``^=`` ：对于 ``x^=y`` ，即对应于 ``x=x^y`` 。
- ``<<=`` ：对于 ``x<<=y`` ，即对应于 ``x=x<<y`` 。
- ``>>=`` ：对于 ``x>>=y`` ，即对应于 ``x=x>>y`` 。
- ``>>>=`` ：对于 ``x>>>=y`` ，即对应于 ``x=x>>>y`` 。

归纳起来，所有的双目运算符都能与赋值运算符结合，从而成为功能更加强大的运算符。


比较运算符
==========
比较运算符用于判断两个变量或常量的大小，比较运算的结果是一个布尔值。 ``JavaScript`` 支持的比较运算符如下。

- ``>`` ：大于，如果前面变量的值大于后面变量的值，则返回 ``true`` 。
- ``>=`` ：大于等于，如果前面变量的值大于等于后面变量的值，则返回 ``true`` 。
- ``<`` ：小于，如果前面变量的值小于后面变量的值，则返回 ``true`` 。
- ``<=`` ：小于等于，如果前面变量的值小于等于后面变量的值，则返回 ``true`` 。
- ``!=`` ：不等于，如果前后两个变量的值不相等，则返回 ``true`` 。
- ``==`` ：等于，如果前后两个变量的值相等，则返回 ``true`` 。
- ``!==`` ：严格不等于，如果前后两个变量的值不相等，或者数据类型不同，都将返回 ``true`` 。
- ``===`` ：严格等于，必须前后两个变量的值相等，数据类型也相同，才会返回 ``true`` 。

在上面的比较运算符中，前面5个比较常见。但后面的严格等于、严格不等于，与普通等于、普通不等于的区别在于是否支持自动类型转换。

正如前面介绍过的， ``JavaScript`` 支持自动类型转换， ``"5"`` 本来是个字符串，但在需要时， ``"5"`` 可以自动转换成数值型。因此，由于自动类型转换的缘故， ``5=="5"`` 将返回 ``true`` 。看如下代码。

.. code-block:: js

	// 判断5是否等于"5"
    console.log(5 == "5");
    // 判断5是否严格等于"5"
    console.log(5 === "5");

其中 ``!=`` 和 ``!==`` 的区别在于： ``!=`` 可以支持自动类型转换，只有前后两个比较变量的值不相等才会返回 ``true`` ，忽略数据类型的比较；而 ``!==`` 则只要两个参与比较的变量的值不同或数据类型不同，就可以返回 ``true`` 。

值得注意的是，比较运算符不仅可以在数值之间进行比较，也可以在字符串之间进行比较。字符串的比较规则是按字母的 ``Unicode`` 值进行比较。对于两个字符串，先比较它们的第一个字母，其 ``Unicode`` 值大的字符串大；如果它们的第一个字母相同，则比较第二个字母……依此类推。看如下代码。

.. code-block:: js

	// 先比较第一个字母，z的Unicode值比a的Unicode值大，返回真
    console.log("z" > "abc");
    // 先比较第一个字母，a的Unicode值比Z的Unicode值大，返回真
    console.log("abc" > "XYZ");
    // 前两个字母相同，比较第三个字母，C的Unicode值比B的Unicode值大，返回真
    console.log("ABC" > "ABB");

逻辑运算符
==========
逻辑运算符用于操作两个布尔型的变量或常量。逻辑运算符主要有如下3个。

- ``&&`` ：与，必须前后两个操作数都为 ``true`` 才返回 ``true`` ，否则返回 ``false`` 。
- ``||`` ：或，只要两个操作数中有一个为 ``true`` ，就可以返回 ``true`` ，否则返回 ``false`` 。
- ``!`` ：非，只操作一个操作数，如果操作数为 ``true`` ，则返回 ``false`` ;如果操作数为 ``false`` ，则返回 ``true`` 。

如下代码示范了逻辑运算符的功能。

.. code-block:: js

	// 直接对false求非运算，将返回true
    console.log(!false);
    // 5>3返回true，’6’自动类型转换为整数6，6>10返回false，求与后返回false
    console.log(5 > 3 && '6' > 10);
    // 4>=5返回false，"abc">"abb"返回true。求或返回true
    console.log(4 >= 5 || "abc" > "abb");

值得指出的是， ``JavaScript`` 虽然没有提供 ``|`` （在 ``Java`` 中被称为不短路或）、 ``&`` （在 ``Java`` 中被称为不短路与）、 ``^`` （异或）等运算符，但实际上我们依然可将它们当成逻辑运算符使用，看如下代码。

.. code-block:: js

	// 使用位运算符代替逻辑运算符
    console.log( 6 > 5 | 3 > 4);
    console.log( true ^ false);

执行上面代码将输出两个 ``l`` ，但根据 ``JavaScript`` 的自动类型转换规则，当数值 ``l`` 转换为布尔类型变量时，将会得到 ``true`` 。从这个意义上来看，我们完全可以将 ``|`` 、 ``&`` 和 ``^`` 当成逻辑运算符使用。

当把 ``|`` 当成逻辑运算符使用时，该运算符将会变成不短路或。看如下代码。

.. code-block:: js

	// 定义变量a, b，并为其赋值
    var a = 5;
    var b = 10
    // 如果a>4，或b>10
    if (a > 4 | b++ > 10)
        console.log(a + '\n' + b);

再看如下代码，只是将上面示例的不短路或( ``|`` )改成了短路或( ``||`` )。

.. code-block:: js

	//定义变量a, b，并为其赋值
    var a = 5;
    var b = 10
    //如果a>4，或b>10
    if (a > 4 || b++ > 10)
        console.log(a + '\n' + b);

仅仅将按位或（不短路或）改成短路的逻辑或，程序最后输出的b值不再相同。因为对于短路的逻辑或( ``||`` )而言，如果第一个操作数返回 ``true`` ， ``||`` 将不再对第二个操作数求值，直接返回 ``true`` 。不会计算 ``b++>10`` 这个逻辑表达式，因而 ``b++`` 没有获得执行的机会。因此，最后输出的 ``b`` 值为 ``10`` 。而按位或( ``|`` )总是执行前后两个操作数。

``&`` 与 ``&&`` 的区别类似： ``&`` 总会计算前后两个操作数，而 ``&&`` 先计算左边的操作数，如果左边的操作数为 ``false`` ，则直接返回 ``false`` ，根本不会计算右边的操作数。


三目运算符
==========

三目运算符只有一个 ``?:`` ，三目运算符的语法格式如下：

.. code-block:: js

    (expression)?：if-true-statement：if-false-statement;

三目运算符的运算规则是：先对逻辑表达式 ``expression`` 求值，如果逻辑表达式返回 ``true`` ，则执行第二部分的语句；如果逻辑表达式返回 ``false`` ，则返回第三部分的语句。

逗号运算符
==========
逗号运算符允许将多个表达式排在一起，整个表达式返回最右边表达式的值。看下面的代码。

.. code-block:: js

	// 声明变量a , b , c , d。
    var a , b , c , d;
    // 使用逗号运算符为a赋值，最右边的表达式为56,因此a的值为56。
    a = (b = 5, c = 7, d = 56);
    // 输出四个变量的值。
    document.write('a = ' + a + ' b = ' + b + ' c = ' + c + ' d = ' + d);

函数的参数列表也使用逗号作为分隔符，但参数列表中的逗号并不是运算符。


void运算符
==========
``void`` 运算符用于强行指定表达式不会返回值。看如下代码。

.. code-block:: js

	// 声明变量a,b,c,d。
    var a , b , c , d;
    // 虽然最右边的表达式为56，
    // 但由于使用了void强制取消返回值，因此a的值为undefined。
    a = void(b = 5, c = 7, d = 56);
    // 输出四个变量的值。
    document.write('a = ' + a + ' b = ' + b + ' c = ' + c + ' d = ' + d);

对 ``(b=5，c=7，d=56)`` 表达式使用了 ``void`` 运算符，强制指定该表达式没有返回值。因此 ``a`` 变量没有被赋值。

``void 0`` 表示 ``undefined`` 值。

.. code-block:: js

    typeof void 0 //得到"undefined"
    console.log(void 0) //输出undefined

为什么要用void
--------------
因为 ``undefined`` 在javascript中不是保留字。换言之，你可以写出：

.. code-block:: js

    function joke() {
        var undefined = "hello world";
        console.log(undefined); //会输出"hello world"
    }
    console.log(undefined); //输出undefined

你可以在一个函数上下文内以 ``undefined`` 做为变量名，于是在这个上下文写的代码便只能通过从全局作用域来取到 ``undefined`` ，如：

.. code-block:: js

    window.undefined //浏览器环境
    GLOBAL.undefined //Node环境

但要注意的是，即便 ``window`` ,  ``GLOBAL`` 仍然可以在函数上下文被定义，故从 ``window/GLOBAL`` 上取 ``undefined`` 并不是 100% 可靠的做法。如：

.. code-block:: js

    function x() {
       var undefined = 'hello world',
           f = {},
           window = {
               'undefined': 'joke'
           };
       console.log(undefined);// hello world
       console.log(window.undefined); //joke
       console.log(f.a === undefined); //false
       console.log(f.a === void 0); //true
    }

于是，采用 ``void`` 方式获取 ``undefined`` 便成了通用准则。如 ``underscore.js`` 里的 ``isUndefined`` 便是这么写的：

.. code-block:: js

    _.isUndefined = function(obj) {
        return obj === void 0;
    }

除了采用 ``void`` 能保证取到 ``undefined`` 值以外，还有其它方法吗？有的，还有一种方式是通过函数调用。如 ``AngularJS`` 的源码里就用这样的方式：

.. code-block:: js

    (function(window, document, undefined) {
        //.....
    })(window, document);

通过不传参数，确保了 ``undefined`` 参数的值是一个 ``undefined`` 。

``href="javascript:void(0)`` 的方式会阻止刷新整个页面。

typeof和instanceof运算符
========================
 ``typeof`` 运算符用于判断某个变量的数据类型，它既可作为函数来使用，例如 ``typeof(a)`` 可返回变量 ``a`` 的数据类型；也可作为一个运算符来使用，例如 ``typeof a`` 也可返回变量 ``a`` 的数据类型。

不同类型参数使用 ``typeof`` 运算符的返回值类型如下

- ``undefined`` 值： ``undefined`` 。
- ``null`` 值： ``object`` 。??
- 布尔型值： ``boolean`` 。
- 数字型值： ``number`` 。
- 字符串值： ``string`` 。
- 对象： ``object`` 。
- 函数： ``function`` 。

下面代码演示了 ``typeof`` 运算符的作用。

.. code-block:: js

	var a = 5;
    var b = true;
    var str = "hello javascript";
    console.log(typeof(a) + "\n" + typeof(b) + "\n" + typeof(str));

上面代码使用 ``typeof`` 运算符分别判断3个变量的数据类型。

与 ``typeof`` 类似的运算符还有 ``instanceof`` ，该运算符用于判断某个变量是否为指定类的实例，如果是，则返回 ``true`` ，否则返回 ``false`` 。例如如下代码。

.. code-block:: js

	// 定义一个数组
    var a = [4, 5];
    // 判断a变量是否为Array的实例
    console.log(a instanceof Array);
    // 判断a变量是否为Object的实例
    console.log(a instanceof Object);

 ``JavaScript`` 中所有的类都是 ``Object`` 的子类， ``a`` 变量是一个数组，因此运行上面程序在控制台上显示 ``true`` 。
