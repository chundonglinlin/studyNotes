*****
控制台
*****

``console`` 模块提供了一个简单的调试控制台，类似于 ``Web`` 浏览器提供的 ``JavaScript`` 控制台。

该模块导出了两个特定的组件：

- 一个 ``Console`` 类，包含 ``console.log()`` 、 ``console.error()`` 和 ``console.warn()`` 等方法，可以被用于写入到任何 ``Node.js`` 流。
- 一个全局的 ``console`` 实例，可被用于写入到 ``process.stdout`` 和 ``process.stderr`` 。 全局的 ``console`` 使用时无需调用 ``require('console')`` 。

.. note:: 全局的 ``console`` 对象的方法既不总是同步的（如浏览器中类似的 ``API`` ），也不总是异步的（如其他 ``Node.js`` 流）。

例子，使用全局的 ``console`` ：

.. code-block:: js

	console.log('你好世界'); // 打印: '你好世界'到 stdout。
	console.log('你好%s', '世界'); // 打印: '你好世界'到 stdout。
	console.error(new Error('错误信息')); // 打印: [Error: 错误信息]到 stderr。

	const name = '描述';
	console.warn(`警告${name}`); // 打印: '警告描述'到 stderr。

全局符号 ``console`` 是一个特殊的 ``Console`` 实例，其输出会被送往 ``process.stdout`` 和 ``process.stderr`` 。它等价于调用：

.. code-block:: js

    new Console({ stdout: process.stdout, stderr: process.stderr });

例子，使用 ``Console`` 类：

.. code-block:: js

	const out = getStreamSomehow(); // 返回输出流函数
	const err = getStreamSomehow(); // 返回输出流函数
	const myConsole = new console.Console(out, err);

	myConsole.log('你好世界'); // 打印: '你好世界'到 out。
	myConsole.log('你好%s', '世界'); // 打印: '你好世界'到 out。
	myConsole.error(new Error('错误信息')); // 打印: [Error: 错误信息]到 err。

	const name = '描述';
	myConsole.warn(`警告${name}`); // 打印: '警告描述'到 err。

``Console`` 类可用于创建一个具有可配置的输出流的简单记录器，可以通过  ``require('console').Console`` 或 ``console.Console`` 使用：

.. code-block:: js

	const { Console } = require('console');
	const { Console } = console;






































