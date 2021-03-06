****************************
请求到console响应的生命周期
****************************

程序启动准备
============
在 Laravel 框架中，所有的请求入口文件是根目录下的 ``artisan.php`` 文件。

文件 ``laravel\public\index.php``

.. code-block:: php

    <?php

    define('LARAVEL_START', microtime(true));

    // 注册自动加载类
    require __DIR__.'/vendor/autoload.php';

    // 实例化app容器对象
    $app = require_once __DIR__.'/bootstrap/app.php';

    // 创建控制台内核实例
    $kernel = $app->make(Illuminate\Contracts\Console\Kernel::class);

    // 处理控制台命令
    $status = $kernel->handle(
        $input = new Symfony\Component\Console\Input\ArgvInput, // 实例化输入对象
        new Symfony\Component\Console\Output\ConsoleOutput // 实例化输出对象
    ); // 返回执行状态

    // 命令执行后的一些处理
    $kernel->terminate($input, $status); // 调用注册到容器的函数

    exit($status); // exit() 函数输出一条消息，并退出当前脚本。该函数是 die() 函数的别名

程序的启动准备阶段是入口文件中的代码 ``require_once __DIR__.'/../bootstrap/app.php'`` 部分，主要实现了服务容器的实例化和基本注册，包括基础路径注册、服务容器本身注册、基础服务提供者注册、核心类别名注册。在注册过程中，服务容器会在对应属性中记录注册的内容，以便在程序运行期间提供对应的服务。

服务容器实例化(同web过程)
-------------------------
入口文件 ``artisan.php`` 的第一句为加载composer的自动加载器。文件的第二句是调用 ``bootstrap`` 文件夹下的 ``app.php`` 中的代码，主要用来实例化服务容器，并注册 ``laravel`` 框架的核心类服务，为后面自动生成 ``$kernel`` 核心类实例提供基础。具体代码和流程请参考？？？

核心类(Kernel类)实例化
----------------------

.. code-block:: php

    <?php
    $app->singleton(
        Illuminate\Contracts\Console\Kernel::class,
        App\Console\Kernel::class
    );

    $kernel = $app->make(Illuminate\Contracts\Console\Kernel::class); // 创建控制台内核实例

服务容器实例化后，就可以通过服务容器来自动实例化对象了。 ``Kernel`` 类就是通过服务容器自动化创建而成的。那么我们又在什么时候绑定了映射？在 ``laravel\bootstrap\app.php`` 文件中，实例化服务容器之后就注册了三个服务，其中就包括这个核心类接口。在注册服务时，服务名一般是接口。在 ``Contracts`` 命名空间下存储的都是接口，而提供的服务则是具体类、实例对象或返回实例对象的回调函数。

由于注册的服务只是具体类名，所以可以通过反射机制来实例化，并通过反射机制自动解决构造函数中的依赖关系。于是，通过服务容器实例化 ``App\Console\Kernel`` 类时，这个类只是定义了 ``$commands`` 数组属性和 ``schedule(Schedule $schedule)`` 以及 ``commands()`` 方法：

- ``$commands`` ： 属性是用来注册自定义命令类的。
- ``commands()`` : 方法是用来自动搜索自定义命令类或者闭包命令的。
- ``schedule()`` : 暂时不知道。

因为 ``App\Console\Kernel`` 类继承了 ``Illuminate\Foundation\Console\Kernel.php`` 类，所以实例化过程中会调用该类中的构造函数，下面是构造函数源码。

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
    public function __construct(Application $app, Dispatcher $events)
    {
      if (! defined('ARTISAN_BINARY')) { // 定义命令名称
        define('ARTISAN_BINARY', 'artisan');
      }

      $this->app = $app;
      $this->events = $events;

      $this->app->booted(function () { // 注册一个应用完全启动后的回调函数
        $this->defineConsoleSchedule();
      });
    }

这里通过构造函数的类型提示使用容器来进行依赖注入，完成了服务容器和核心类的实例化之后，接下来该处理请求了。

输入、输出实例化
================



处理命令
========
在完成了输入、输出实例化后，将进入对输入实例的处理阶段。命令的加载和处理是控制台应用程序的核心功能，通过不同的处理命令最终返回各种响应，实现不同的功能。如何提供可扩展的命令处理模块是控制台框架程序成功的关键，具体代码如下：

处理准备工作
------------
前面介绍了应用程序运行的准备环节，而要实现命令的处理，还有很多基础工作要做，这里包括环境检测和变量加载、配置加载、异常处理、外观注册、服务提供者注册和启动服务共七个步骤，下面将对其中几个步骤的关键环节进行介绍。首先看一下这七个步骤是如何启动的，具体源码如下：

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
     public function handle($input, $output = null)
    {
      try {
        $this->bootstrap(); // 加载基础服务和闭包命令以及自定义命令

        return $this->getArtisan()->run($input, $output); // 生成Artisan应用并执行命令
      } catch (Exception $e) {
        $this->reportException($e);

        $this->renderException($output, $e);

        return 1;
      } catch (Throwable $e) {
        $e = new FatalThrowableError($e);

        $this->reportException($e);

        $this->renderException($output, $e);

        return 1;
      }
    }

启动应用
--------
启动应用，为命令执行准备好各种环境。

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
    public function bootstrap()
    {
      if (! $this->app->hasBeenBootstrapped()) { // 如果基础应用没有完全启动
        $this->app->bootstrapWith($this->bootstrappers()); // 初始化并启动注册的基础服务
      }

      $this->app->loadDeferredProviders(); // 初始化并启动所有延迟服务，在这些服务中可能需要注册服务提供的命令

      if (! $this->commandsLoaded) {
        $this->commands(); // 如果没有加载闭包命令，则调用app/Console/Kernel.php中commands()方法

        $this->commandsLoaded = true;
      }
    }

    protected $bootstrappers = [
        \Illuminate\Foundation\Bootstrap\LoadEnvironmentVariables::class,
        \Illuminate\Foundation\Bootstrap\LoadConfiguration::class,
        \Illuminate\Foundation\Bootstrap\HandleExceptions::class,
        \Illuminate\Foundation\Bootstrap\RegisterFacades::class,
        \Illuminate\Foundation\Bootstrap\SetRequestForConsole::class,
        \Illuminate\Foundation\Bootstrap\RegisterProviders::class, // 注册配置的所有服务供应器
        \Illuminate\Foundation\Bootstrap\BootProviders::class, // 调用所有立即服务的boot方法,并设置基础应用已经完成启动
    ];

``bootstrappers`` 数组中定义的基础类，都定义了 ``bootstrap()`` 方法，没有 ``register()`` 方法

文件 ``Illuminate\Foundation\Application.php``

.. code-block:: php

    <?php
    public function bootstrapWith(array $bootstrappers)
    {
      $this->hasBeenBootstrapped = true;  // 标识应用已经完全启动

      foreach ($bootstrappers as $bootstrapper) {
        // 启动前的事件触发
        $this['events']->fire('bootstrapping: '.$bootstrapper, [$this]);
        // 创建相应的对象并执行引导操作，在boot()过程中可能会注册自定义命令闭包到Illuminate/Console/Application.php中$bootstrappers数组中
        $this->make($bootstrapper)->bootstrap($this);
        // 启动后的事件触发
        $this['events']->fire('bootstrapped: '.$bootstrapper, [$this]);
      }
    }
    // RegisterProviders类调用该方法来注册配置的服务提供者
    public function registerConfiguredProviders()
    {
        $providers = Collection::make($this->config['app.providers'])
            ->partition(function ($provider) {
                return Str::startsWith($provider, 'Illuminate\\');
            }); // 分组
        // 插入包自动发现的服务提供者到集合中
        $providers->splice(1, 0, [$this->make(PackageManifest::class)->providers()]);
        // 使用缓存中服务清单来初始化一个提供器仓库，并用该对象来加载服务提供者的配置
        (new ProviderRepository($this, new Filesystem, $this->getCachedServicesPath()))
            ->load($providers->collapse()->toArray());
    }

    public function boot()
    {
        if ($this->booted) {
            return;
        }

        /**
         * 一旦应用程序启动，我们还会为任何需要在初始启动完成后工作的侦听器启动一些“booted”回调。
         * 这在定制我们运行的启动过程时非常有用。
         */
        // 服务启动之前的回调
        $this->fireAppCallbacks($this->bootingCallbacks);

        array_walk($this->serviceProviders, function ($p) {
            $this->bootProvider($p); // 调用所有注册的服务提供者boot()方法
        });

        $this->booted = true; // 基础应用完全启动
        // 服务启动之后的回调
        $this->fireAppCallbacks($this->bootedCallbacks);
    }

这里加载内核类中定义的基础启动类。 ``RegisterProviders`` 类负责注册配置的服务提供者，在此函数中会调用急切加载的服务提供者的 ``register()`` 方法。在上面代码中当调用 ``BootProviders`` 类的 ``bootstrap()`` 方法时，则会调用用户配置所有急切加载的服务提供者的 ``bootstrap()`` 该方法。

**至此，基础应用完全启动。**

注册延迟服务提供器提供的命令定义
--------------------------------
接下来是加载延迟服务提供器。该方法主要是实例化延迟服务，并调用该服务的 ``register()`` 方法。

延迟服务提供器可以注册命令。主要是通过 ``config\app.php`` 中注册的 ``Illuminate\Foundation\Providers\ConsoleSupportServiceProvider.php`` 聚合服务加载器来注册。

文件 ``Illuminate\Foundation\Application.php``

.. code-block:: php

    <?php
    public function loadDeferredProviders()
    {

        /**
         * 我们将简单地遍历每个延迟提供器并注册每个提供器，并在应用程序启动时启动它们。
         * 这使每个剩余的服务可供此应用程序立即使用。
         */
        foreach ($this->deferredServices as $service => $provider) {
            $this->loadDeferredProvider($service);
        }
        // 该数组存储的是服务名称和实现类名称映射关系
        $this->deferredServices = []; // 清空保存延迟服务类数组
    }

    public function loadDeferredProvider($service)
    {
        if (! isset($this->deferredServices[$service])) { // 如果为空，则返回
            return;
        }

        $provider = $this->deferredServices[$service];

        /**
         * 如果服务提供者尚未加载并注册，我们可以将其注册到应用程序中，即存储在loadedProviders数组中
         * 当延迟服务提供器被加载后则需要从延迟服务列表(即deferredServices数组)中删除该服务。
         */
        if (! isset($this->loadedProviders[$provider])) { // 如果延迟服务没有加载，则需要加载
            $this->registerDeferredProvider($provider, $service);
        }
    }

    public function registerDeferredProvider($provider, $service = null)
    {
        /**
         * 一旦延迟服务提供器已经注册，我们将从延迟服务列表中删除该服务提供器，
         * 以便此容器不会尝试再次解析它。
         */
        if ($service) {
            unset($this->deferredServices[$service]); // 移除延迟服务提供器
        }
        // 调用服务提供者的注册方法
        $this->register($instance = new $provider($this));

        if (! $this->booted) { // 如果应用没有完全启动，则注册服务提供者的boot()回调
            $this->booting(function () use ($instance) {
                $this->bootProvider($instance);
            });
        }
    }

此处为止，应用基本上加载完成立即和延迟服务提供者，即调用了两类服务提供者 ``register()`` 方法和立即服务提供者的 ``boot()`` 方法。但是却没有调用延迟服务提供者的 ``boot()`` 方法？？？？

文件 ``Illuminate\Foundation\Providers\ConsoleSupportServiceProvider.php``

.. code-block:: php

    <?php
    protected $providers = [
        ArtisanServiceProvider::class, // 一般命令
        MigrationServiceProvider::class, // 迁移命令
        ComposerServiceProvider::class, // composer命令
    ];

``ConsoleSupportServiceProvider`` 类继承了 ``AggregateServiceProvider`` 类。

文件 ``Illuminate\Support\AggregateServiceProvider.php``

.. code-block:: php

    <?php
    // 在基础启动加载配置的时候，就会调用该方法来建立聚合服务提供器中聚合的所有服务提供器类
    public function provides()
    {
        $provides = [];
        // 遍历聚合服务提供器中注册的服务提供器类型
        foreach ($this->providers as $provider) {
            $instance = $this->app->resolveProvider($provider); // 实例化服务提供器

            $provides = array_merge($provides, $instance->provides()); // 合并所有服务提供器中的provides
        }

        return $provides;
    }

    // 当加载延迟服务提供器类时，会调用该方法
    public function register()
    {
        $this->instances = [];

        foreach ($this->providers as $provider) {
            // 分别调用每个在聚合服务提供器中注册的服务提供器
            // 调用Illuminate\Foundation\Application.php中register()方法
            // 实例化该服务提供器并调用它的register()方法，
            // 并保存该服务提供器实例的单例到当前聚合服务提供器中
            $this->instances[] = $this->app->register($provider);
        }
    }

加载延迟服务提供器会实例化该聚合服务提供器，当调用该聚合服务提供器的 ``register()`` 方法时，则会调用 ``Illuminate\Foundation\Application.php`` 中 ``register()`` 方法来实例化延迟服务提供器和调用聚合服务器内的延迟服务提供器的 ``register()`` 。

文件 ``\Illuminate\Foundation\Providers\ArtisanServiceProvider``

.. code-block:: php

    <?php
    public function register()
    {
        // 在注册的服务的时候，就加载所有命令工厂
        $this->registerCommands(array_merge(
            $this->commands, $this->devCommands
        ));
    }

    protected function registerCommands(array $commands)
    {
        foreach (array_keys($commands) as $command) {
            //在基础容器中建立 命令名称和命令单实例的映射
            call_user_func_array([$this, "register{$command}Command"], []);
        }
        // 增加创建命令实例的闭包到artisan容器$bootstrappers数组中
        $this->commands(array_values($commands));
    }

    public function commands($commands)
    {
        $commands = is_array($commands) ? $commands : func_get_args();
        // 当处理命令之前，会把所有自定义命令和闭包命令通过回调方式注册到artisan容器的$bootstrappers数组中
        Artisan::starting(function ($artisan) use ($commands) {
            $artisan->resolveCommands($commands); // 从基础容器中实例化命令对象，然后增加到artisan中
        });
    }

到此为止，服务提供器注册命令的过程已经完成。

加载自定义命令
--------------
当应用服务提供者加载完成后，需要调用 ``Illuminate\Foundation\Console\Kernel.php`` 文件中的 ``commands()`` 方法来加载自定义命令，该函数实际调用的是继承 ``Illuminate\Foundation\Console\Kernel.php`` 的 ``app\Console\Kernel.php`` 类中的 ``commands()`` 方法。

文件 ``app\Console\Kernel.php``

.. code-block:: php

    <?php
    protected function commands()
    {
        $this->load(__DIR__.'/Commands'); // 加载以类形式定义的命令

        require base_path('routes/console.php'); // 加载以闭包形式定义的命令
    }

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
    protected function load($paths)
    {
        $paths = array_unique(is_array($paths) ? $paths : (array) $paths);

        $paths = array_filter($paths, function ($path) {
            return is_dir($path);
        });

        if (empty($paths)) {
            return;
        }

        $namespace = $this->app->getNamespace();

        foreach ((new Finder)->in($paths)->files() as $command) {
            $command = $namespace.str_replace(
                    ['/', '.php'],
                    ['\\', ''],
                    Str::after($command->getPathname(), app_path().DIRECTORY_SEPARATOR)
                );

            if (is_subclass_of($command, Command::class) &&
                ! (new ReflectionClass($command))->isAbstract()) {
                Artisan::starting(function ($artisan) use ($command) { // 把自定义命令初始化闭包注册到artisan容器的$bootstrappers数组中
                    $artisan->resolve($command); // 实例化命令对象然后添加到artisan容器中
                });
            }
        }
    }

文件 ``Illuminate\Console\Application.php``

.. code-block:: php

    <?php
    public function resolve($command)
    {
        return $this->add($this->laravel->make($command)); // 实例化命令对象然后添加到artisan容器中
    }

    public function add(SymfonyCommand $command)
    {
        if ($command instanceof Command) { // 如果是laravel继承类即用户自定义的类
            $command->setLaravel($this->laravel); // 则设置命令容器为laravel容器
        }

        return $this->addToParent($command); // 增加到symfony容器中
    }

此处的 ``resolve`` 方法不会立即调用，先是以包含该函数的闭包形式注册到 ``Illuminate\Console\Application.php`` 类的静态数组 ``$bootstrappers`` 中。

加载完类形式自定义命令后，需要加载以闭包形式定义的自定义命令。

文件 ``routes\console.php``

.. code-block:: php

    <?php
    Artisan::command('inspire', function () { // 通过闭包来注册命令
        $this->comment(Inspiring::quote());
    })->describe('Display an inspiring quote');

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
    public function command($signature, Closure $callback)
    {
        $command = new ClosureCommand($signature, $callback); // 生成闭包命令

        Artisan::starting(function ($artisan) use ($command) { // 把该命令初始化闭包注册到到artisan类的$bootstrappers静态数组中
            $artisan->add($command); // 把命令实例注册到artisan实例中
        });

        return $command;
    }

此处的 ``ClosureCommand`` 类继承 ``command`` 类，用来封装闭包。

此处的 ``add`` 方法不会立即调用，先是以包含该函数的闭包形式注册到 ``Illuminate\Console\Application.php`` 类的静态数组 ``$bootstrappers`` 中。

**至此，自定义命令加载完成。**

由上面的分析可知，注册自定义命令存在三种方式：

- 通过延迟服务提供器来注册；
- 直接把自定义命令类放在 ``app/Console/Commands`` 目录下；
- 在 ``routes/console.php`` 文件中定义命令闭包；


生成artisan应用实例并解析自定义命令
-----------------------------------

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
    protected function getArtisan()
    {
        if (is_null($this->artisan)) {
            // 创建artisan容器并利用静态bootStrapper属性中注册的命令来初始化所有命令对象实例
            return $this->artisan = (new Artisan($this->app, $this->events, $this->app->version())) // laravel框架的版本
            ->resolveCommands($this->commands); // 这里优先解析console/Kernel中的commands属性中定义的命令类
        }

        return $this->artisan;
    }

文件 ``Illuminate\Console\Application.php``

.. code-block:: php

    <?php
    public function __construct(Container $laravel, Dispatcher $events, $version)
    {
        parent::__construct('Laravel Framework', $version);

        $this->laravel = $laravel;
        $this->events = $events;
        $this->setAutoExit(false);
        $this->setCatchExceptions(false);

        $this->events->dispatch(new Events\ArtisanStarting($this)); // 分发artisan启动事件并调用监听器

        $this->bootstrap(); // 实例化闭包命令和自定义命令，并添加到容器中
    }

    protected function bootstrap()
    {
        // 这些启动器是各个服务加载时注册的闭包命令和自定义命令，现在开始回调这些闭包来把他们实例化到artisan应用中
        foreach (static::$bootstrappers as $bootstrapper) {
            $bootstrapper($this);
        }
    }

前面各个阶段注册到 ``Illuminate\Console\Application.php`` 类的静态数组 ``$bootstrappers`` 中的闭包开始执行。目的就是把命令实例化并注册到artisan应用中。

文件 ``symfony\console\Application.php``

.. code-block:: php

    <?php
    public function add(Command $command)
    {
        $this->init(); // 加载symfony中定义help和list命令

        $command->setApplication($this);

        if (!$command->isEnabled()) {
            $command->setApplication(null);

            return;
        }

        if (null === $command->getDefinition()) {
            throw new LogicException(sprintf('Command class "%s" is not correctly initialized. You probably forgot to call the parent constructor.', get_class($command)));
        }

        if (!$command->getName()) {
            throw new LogicException(sprintf('The command defined in "%s" cannot have an empty name.', get_class($command)));
        }

        $this->commands[$command->getName()] = $command; // 保存命令名称和实例的映射关系

        foreach ($command->getAliases() as $alias) {
            $this->commands[$alias] = $command;
        }

        return $command;
    }

    private function init()
    {
        if ($this->initialized) {
            return;
        }
        $this->initialized = true;

        foreach ($this->getDefaultCommands() as $command) {
            $this->add($command); // 把symfony中默认命令注册到artisan应用中
        }
    }

    protected function getDefaultCommands()
    {
        return array(new HelpCommand(), new ListCommand());
    }

上面的代码是会自动加载 ``symfony`` 中定义的 ``list`` 和 ``help`` 命令。


初始完成 ``artisan`` 应用实例后，需要调用 ``resolveCommands($this->commands)`` 方法。 ``commands`` 属性来自于 ``app\Console\Kernel.php`` 类的 ``commands`` 属性。

文件 ``Illuminate\Console\Application.php``

.. code-block:: php

    <?php
    public function resolveCommands($commands)
    {
        $commands = is_array($commands) ? $commands : func_get_args();

        foreach ($commands as $command) {
            $this->resolve($command); // 实例化命令对象并添加到artisan应用实例中
        }

        return $this;
    }

至此，把 ``app\Console\Kernel.php`` 类的 ``commands`` 属性中定义的自定义命令实例也添加到artisan应用实例中。

**到此为止，artisan实例初始化和命令解析完成。为接下来的命令执行准备好了所有环境。**

命令执行
--------

文件 ``Illuminate\Console\Application.php``

.. code-block:: php

    <?php
    public function run(InputInterface $input = null, OutputInterface $output = null)
    {
        $commandName = $this->getCommandName( // 从输入中获取命令
            $input = $input ?: new ArgvInput
        );

        $this->events->fire( // 触发命令开始执行的事件
            new Events\CommandStarting(
                $commandName, $input, $output = $output ?: new ConsoleOutput
            )
        );

        $exitCode = parent::run($input, $output); // Symfony中执行该命令

        $this->events->fire( // 触发命令执行结束的事件
            new Events\CommandFinished($commandName, $input, $output, $exitCode)
        );

        return $exitCode;
    }

文件 ``symfony\console\Application.php``

.. code-block:: php

<?php



命令返回和程序终止
==================

文件 ``Illuminate\Foundation\Console\Kernel.php``

.. code-block:: php

    <?php
    public function terminate($input, $status)
    {
        $this->app->terminate();
    }

文件 ``Illuminate\Foundation\Application.php``

.. code-block:: php

    <?php
    public function terminate()
    {
        foreach ($this->terminatingCallbacks as $terminating) {
            $this->call($terminating);
        }
    }

脚本结束的时候会回调自定义的接收闭包。可以在服务提供者的 ``register()`` 或 ``boot()`` 方法中定义脚本结束回调函数。如下所示：

.. code-block:: php

    <?php
    public function boot()
    {
        // 定义一个结束方法
        $this->app->terminating(function() {
            print '测试结束方法';
        });
    }
