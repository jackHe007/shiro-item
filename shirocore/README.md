# shiro-core

说明：
Shiro 的架构有 3 个主要的概念： Subject， SecurityManager 和 Realms

1、Subject：
    在我们的教程中已经提到， Subject 实质上是一个当前执行用户的特定的安全“视图”。鉴于"User"
    一词通常意味着一个人，而一个 Subject 可以是一个人，但它还可以代表第三方服务， daemon account， cron job，
    或其他类似的任何东西——基本上是当前正与软件进行交互的任何东西。
    所有 Subject 实例都被绑定到（且这是必须的）一个 SecurityManager 上。当你与一个 Subject 交互时，那些交
    互作用转化为与 SecurityManager 交互的特定 subject 的交互作用。
2、SecurityManager： 
    SecurityManager 是 Shiro 架构的心脏，并作为一种“保护伞”对象来协调内部的安全组件
    共同构成一个对象图。然而，一旦 SecurityManager 和它的内置对象图已经配置给一个应用程序，那么它单独
    留下来，且应用程序开发人员几乎使用他们所有的时间来处理 Subject API。
    我们稍后会更详细地讨论 SecurityManager，但重要的是要认识到，当你正与一个 Subject 进行交互时，实质上
    是幕后的 SecurityManager 处理所有繁重的 Subject 安全操作。这反映在上面的基本流程图。
3、Realms： 
    Realms 担当 Shiro 和你的应用程序的安全数据之间的“桥梁”或“连接器”。当它实际上与安全相
    关的数据如用来执行身份验证（登录）及授权（访问控制）的用户帐户交互时， Shiro 从一个或多个为应用程
    序配置的 Realm 中寻找许多这样的东西。
    在这个意义上说， Realm 本质上是一个特定安全的 DAO：它封装了数据源的连接详细信息，使 Shiro 所需的相
    关的数据可用。当配置 Shiro 时，你必须指定至少一个 Realm 用来进行身份验证和/或授权。 SecurityManager
    可能配置多个 Realms，但至少有一个是必须的。
    Shiro 提供了立即可用的 Realms 来连接一些安全数据源（即目录），如 LDAP，关系数据库（JDBC），文本配
    置源，像 INI 及属性文件，以及更多。你可以插入你自己的 Realm 实现来代表自定义的数据源，如果默认地
    Realm 不符合你的需求。
    像其他内置组件一样， Shiro SecurityManager 控制 Realms 是如何被用来获取安全和身份数据来代表 Subject 实
    例的。
      




Subject(org.apache.shiro.subject.Subject)
当前与软件进行交互的实体（用户，第三方服务， cron job，等等）的安全特定“视图”。
1、 SecurityManager(org.apache.shiro.mgt.SecurityManager)
如上所述， SecurityManager 是 Shiro 架构的心脏。它基本上是一个“保护伞”对象，协调其管理的组件以确保
它们能够一起顺利的工作。它还管理每个应用程序用户的 Shiro 的视图，因此它知道如何执行每个用户的安全
操作。
2、 Authenticator(org.apache.shiro.authc.Authenticator)Authenticator 是一个对执行及对用户的身份验证（登录）尝试负责的组件。当一个用户尝试登录时，该逻辑
被 Authenticator 执行。 Authenticator 知道如何与一个或多个 Realm 协调来存储相关的用户/帐户信息。从这些
Realm 中获得的数据被用来验证用户的身份来保证用户确实是他们所说的他们是谁。
3、 Authentication Strategy(org.apache.shiro.authc.pam.AuthenticationStrategy)
如果不止一个 Realm 被配置，则 AuthenticationStrategy 将会协调这些 Realm 来决定身份认证尝试成功或
失败下的条件（例如，如果一个 Realm 成功，而其他的均失败，是否该尝试成功？ 是否所有的 Realm
必须成功？或只有第一个成功即可？）。
4、 Authorizer(org.apache.shiro.authz.Authorizer)
Authorizer 是负责在应用程序中决定用户的访问控制的组件。它是一种最终判定用户是否被允许做某事的机制。
与 Authenticator 相似， Authorizer 也知道如何协调多个后台数据源来访问角色恶化权限信息。 Authorizer 使用
该信息来准确地决定用户是否被允许执行给定的动作。
5、 SessionManager(org.apache.shiro.session.SessionManager)
SessionManager 知道如何去创建及管理用户 Session 生命周期来为所有环境下的用户提供一个强健的 Session
体验。这在安全框架界是一个独有的特色——Shiro 拥有能够在任何环境下本地化管理用户 Session 的能力，
即使没有可用的 Web/Servlet 或 EJB 容器，它将会使用它内置的企业级会话管理来提供同样的编程体验。
SessionDAO 的存在允许任何数据源能够在持久会话中使用。
6、 SessionDAO(org.apache.shiro.session.mgt.eis.SessionDAO)
SesssionDAO 代表 SessionManager 执行 Session 持久化（CRUD）操作。这允许任何数据存储被插入到会
话管理的基础之中。
7、 CacheManager(org.apahce.shiro.cache.CacheManager)
CacheManager 创建并管理其他 Shiro 组件使用的 Cache 实例生命周期。因为 Shiro 能够访问许多后台数据源，
由于身份验证，授权和会话管理，缓存在框架中一直是一流的架构功能，用来在同时使用这些数据源时提高
性能。任何现代开源和/或企业的缓存产品能够被插入到 Shiro 来提供一个快速及高效的用户体验。
8、 Cryptography(org.apache.shiro.crypto.*)
Cryptography 是对企业安全框架的一个很自然的补充。Shiro 的 crypto 包包含量易于使用和理解的 cryptographic
Ciphers， Hasher（又名 digests）以及不同的编码器实现的代表。所有在这个包中的类都被精心地设计以易于
使用和易于理解。任何使用 Java 的本地密码支持的人都知道它可以是一个难以驯服的具有挑战性的动物。Shiro
的 cryptoAPI 简化了复杂的 Java 机制，并使加密对于普通人也易于使用。
9、 Realms(org.apache.shiro.realm.Realm)
如上所述， Realms 在 Shiro 和你的应用程序的安全数据之间担当“桥梁”或“连接器”。当它实际上与安全
相关的数据如用来执行身份验证（登录）及授权（访问控制）的用户帐户交互时， Shiro 从一个或多个为应用
程序配置的 Realm 中寻找许多这样的东西。你可以按你的需要配置多个 Realm（通常一个数据源一个 Realm），
且 Shiro 将为身份验证和授权对它们进行必要的协调