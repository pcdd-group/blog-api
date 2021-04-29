/*
 Navicat Premium Data Transfer

 Source Server         : java_project
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : vueblog

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 15/04/2021 04:25:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL,
  `login_datetime` datetime(0) NOT NULL,
  `ip_addr` char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `browser` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `engine` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `os` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_mobile` tinyint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_userId`(`user_id`) USING BTREE,
  CONSTRAINT `FK_userId` FOREIGN KEY (`user_id`) REFERENCES `m_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES (7, 1, '2021-04-15 04:01:01', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (8, 1, '2021-04-15 04:01:08', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (9, 1, '2021-04-15 04:17:21', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (10, 146, '2021-04-15 04:17:54', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (11, 146, '2021-04-15 04:18:15', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (12, 146, '2021-04-15 04:19:29', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (13, 146, '2021-04-15 04:21:08', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);
INSERT INTO `login_log` VALUES (14, 146, '2021-04-15 04:21:32', '0:0:0:0:0:0:0:1', 'Chrome/89.0.4389.114', 'Webkit/537.36', 'Windows 10 or Windows Server 2016', 0);

-- ----------------------------
-- Table structure for m_blog
-- ----------------------------
DROP TABLE IF EXISTS `m_blog`;
CREATE TABLE `m_blog`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `status` tinyint(0) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog
-- ----------------------------
INSERT INTO `m_blog` VALUES (1, 1, '测试标题1', '测试摘要', 'JAVA诞生时，号称有三大颠覆性创新。即跨平台性，前后端分离，面向对象。但面向对象这个概念，真正火起来，却是因为微软，或者说是因为C#。\r\n因为在JAVA诞生的那个年代。。。。IT行业==微软。。。Unix挂掉了。。Linux已经被打成了残废。。。苹果只是个笑话。。。。其他如Amiga，PALM等公司更是渺小的几乎看不见。。\r\n当时的网页前端语言，是VBscript，微软的；当时的网站后端语言是ASP，微软的；当时的桌面应用程序开发语言是VB，还是微软的。原本，微软可以就这么一直无敌，一直寂寞下去。。。\r\n后来JAVA诞生了。。。像路边的一坨翔一样诞生了。\r\nSUN公司号称它具有颠覆性的三大特性。。。没人在意。。。\r\nSUN公司号称JAVA将改变未来。。。。没人相信。。。\r\n但是，微软在意，并深信不疑。。。于是一个奇妙的现象产生了。。。全世界的程序员，都在研究VB，但微软的程序员却在研究JAVA。。。全世界都是VB的铁杆粉，而微软的工程师们，眼里却只有JAVA。。而当时世界上的JAVA顶尖高手，绝大部分都在微软工作。。甚至可以说，微软对于JAVA的理解，比Sun公司更透彻。\r\n所以微软顺理成章的，进行了一个作死的计划。。。停止VB和ASP的开发。。。转而开发一个比JAVA平台更优秀的“微软版JAVA平台”——.NET。。。以及一门拥有JAVA语言全部特性，又比JAVA更优雅的“微软版JAVA语言”——C#语言。\r\n一个垄断着WEB前端的霸主（VBSCRIPT），一个垄断后端的霸主（ASP），突然一下子都不见了。。。。就好像天上的太阳和月亮突然一下子消失了。。。。那些原本被吊打的半死不活的语言，就像吃了大力丸一样，全都满血复活了。。。JAVASCRIPT取代了VBSCRIPT垄断了WEB前端。。。。。C语言也顺势变身成C++迎来了第二春。。。JAVA更是左右逢源，乐开了花。。。\r\n后来.NET平台终于开发完成了，但发现大部分市场已经被之前那些阿猫阿狗抢了去。。。只好回收一下ASP的那群老粉丝吧。。。。但ASP是直接把代码写在html文件里的，突然搞成了前后端分离，又面向对象啥的。。。那些用惯了ASP的用户适应不了哇。。。屋漏偏逢连夜雨，这时候，一个山寨版的ASP——PHP又冒出来挖人。。。最终搞得微软引以为傲的.NET平台，以及那优雅的C#语言，贴了冷屁股。。。\r\n微软也从一个绝对的霸主，跌落成了三巨头之一。。。。。', '2021-03-07 19:49:37', 1);
INSERT INTO `m_blog` VALUES (2, 1, '测试标题2', '测试摘要', '背景\r\n最近用到前端压缩图片的技术，虽说可以参考原理自己写，但是前辈花了很大精力写出来的插件可以帮助我们避免很多坑，直接拿来用吧。\r\n\r\n插件1 compression.js\r\n优点：使用简单，参数只有输入图片，压缩比例，输出图片。很少的代码量即可实现压缩和预览的效果。', '2021-03-07 19:49:39', 1);
INSERT INTO `m_blog` VALUES (7, 1, '测试标题4', '测试摘要', '在JavaScript中想让一个函数执行完毕之后再执行下一个函数？\n\n小蚍蜉要撼树 2017-06-19 09:03:37  52489  已收藏 3\n分类专栏： 前端知识 文章标签： javascript 异步\n版权\n先说结论：\n\n首先，JavaScript是解释性语言，本来就是顺序执行的！！\n\n所以，如果发现有一段js代码总是在一段代码之前就执行完了，比如说报了null异常，那么有可能是回调函数的异步机制造成的。\n\n此种情形下解决的方法是，把要执行的函数放在回调函数内部就可以了。\n\n举个栗子\n\n例如用JQuery Ajax 的 $.getJSON 方法：\r\n————————————————\r\n版权声明：本文为CSDN博主「小蚍蜉要撼树」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。\r\n原文链接：https://blog.csdn.net/qq_23067521/article/details/73456647', '2021-03-07 19:49:42', 1);
INSERT INTO `m_blog` VALUES (10, 1, 'vbscript', '计算机编程语言', 'VBScript是Visual Basic Script的简称，即 Visual Basic 脚本语言，有时也被缩写为VBS。它是一种微软环境下的轻量级的解释型语言，它使用COM组件、WMI、WSH、ADSI访问系统中的元素，对系统进行管理。同时它又是asp动态网页默认的编程语言，配合asp内建对象和ADO对象，用户很快就能掌握访问数据库的asp动态网页开发技术。', '2021-03-07 19:49:24', 1);
INSERT INTO `m_blog` VALUES (21, 130, 'typescript', '计算机编程语言', 'TypeScript是微软开发的一个开源的编程语言，通过在JavaScript的基础上添加静态类型定义构建而成。TypeScript通过TypeScript编译器或Babel转译为JavaScript代码，可运行在任何浏览器，任何操作系统。 [1] \r\nTypeScript添加了很多尚未正式发布的ECMAScript新特性（如装饰器 [2]  ）。2012年10月，微软发布了首个公开版本的TypeScript，2013年6月19日，在经历了一个预览版之后微软正式发布了正式版TypeScript。当前最新版本为TypeScript 4.0 [3-4]  。', '2021-03-22 19:51:55', 1);
INSERT INTO `m_blog` VALUES (31, 130, 'java2', '计算机编程语言', 'Java是一门面向对象编程语言，不仅吸收了C++语言的各种优点，还摒弃了C++里难以理解的多继承、指针等概念，因此Java语言具有功能强大和简单易用两个特征。Java语言作为静态面向对象编程语言的代表，极好地实现了面向对象理论，允许程序员以优雅的思维方式进行复杂的编程 [1]  。\r\nJava具有简单性、面向对象、分布式、健壮性、安全性、平台独立与可移植性、多线程、动态性等特点 [2]  。Java可以编写桌面应用程序、Web应用程序、分布式系统和嵌入式系统应用程序等 [3]  。', '2021-03-23 00:38:11', 1);
INSERT INTO `m_blog` VALUES (32, 130, 'c#', '计算机编程语言', 'C#是微软公司发布的一种由C和C++衍生出来的面向对象的编程语言、运行于.NET Framework和.NET Core(完全开源，跨平台)之上的高级程序设计语言。并定于在微软职业开发者论坛(PDC)上登台亮相。C#是微软公司研究员Anders Hejlsberg的最新成果。C#看起来与Java有着惊人的相似；它包括了诸如单一继承、接口、与Java几乎同样的语法和编译成中间代码再运行的过程。但是C#与Java有着明显的不同，它借鉴了Delphi的一个特点，与COM（组件对象模型）是直接集成的，而且它是微软公司 .NET windows网络框架的主角。\r\nC#是由C和C++衍生出来的一种安全的、稳定的、简单的、优雅的面向对象编程语言。它在继承C和C++强大功能的同时去掉了一些它们的复杂特性（例如没有宏以及不允许多重继承）。C#综合了VB简单的可视化操作和C++的高运行效率，以其强大的操作能力、优雅的语法风格、创新的语言特性和便捷的面向组件编程的支持成为.NET开发的首选语言。 [1] \r\nC#是面向对象的编程语言。它使得程序员可以快速地编写各种基于MICROSOFT .NET平台的应用程序，MICROSOFT .NET提供了一系列的工具和服务来最大程度地开发利用计算与通讯领域。\r\nC#使得C++程序员可以高效的开发程序，且因可调用由 C/C++ 编写的本机原生函数，而绝不损失C/C++原有的强大的功能。因为这种继承关系，C#与C/C++具有极大的相似性，熟悉类似语言的开发者可以很快的转向C#。', '2021-03-22 19:51:52', 1);
INSERT INTO `m_blog` VALUES (35, 130, 'emscript6', '计算机编程语言', 'ECMAScript 6（简称ES6）是于2015年6月正式发布的JavaScript语言的标准，正式名为ECMAScript 2015（ES2015）。它的目标是使得JavaScript语言可以用来编写复杂的大型应用程序，成为企业级开发语言 [1]  。\r\n另外，一些情况下ES6也泛指ES2015及之后的新增特性，虽然之后的版本应当称为ES7、ES8等。', '2021-03-22 19:51:51', 1);
INSERT INTO `m_blog` VALUES (36, 130, 'Docker 真滴牛！ 哈哈哈哈哈哈哈哈哈哈', '容器技术 666', '# Docker安装\r\n**yum包更新到最新**\r\nyum -y update\r\n\r\n**安装需要的软件包，yum-util提供yum-config-manager功能，另外两个是devicemapper驱动依赖**\r\n```bash\r\nyum install -y yum-utils device-mapper-persistent-data lvm2\r\n```\r\n\r\n**设置yum源**\r\n\r\n```bash\r\nyum-config-manager --add-repo http://mirrors.aliyun.com/dockerce/linux/centos/docker-ce.repo--\r\n```\r\n\r\n**安装docker，出现输入的界面都按y**\r\n\r\n```bash\r\nyum install -y docker-ce docker-ce-cli containerd.io\r\n```\r\n\r\n**查看docker版本，验证是否安装成功**\r\n\r\n```bash\r\ndocker -v\r\n```\r\n\r\n**需要先启动docker才会创建/etc/docker/目录**\r\n\r\n```bash\r\nsystemctl start docker\r\n```\r\n\r\n**修改下载源，否则pull速度很慢**\r\ndaemon.json这个文件默认是没有的，需要手动创建，路径： /etc/docker/daemon.json\r\n\r\n```\r\n{\r\n    \"registry-mirrors\": [\r\n       \"https://registry.docker-cn.com\"\r\n    ]\r\n}\r\n```\r\n\r\n**然后再执行以下命令让改动生效**\r\n\r\n```bash\r\nsystemctl daemon-reload\r\nsystemctl restart docker\r\n```\r\n\r\n\r\n\r\n# Docker镜像相关命令\r\n\r\n```bash\r\n查看本地所有的镜像\r\ndocker images\r\n查看所有镜像的id\r\ndocker images -q\r\n从网络中查找所需要的镜像\r\ndocker search 镜像名称\r\n拉取镜像\r\ndocker pull 镜像名称:版本\r\n删除指定本地镜像\r\ndocker rmi 镜像id\r\n删除所有本地镜像\r\ndocker rmi docker images -q\r\n```\r\n\r\n\r\n\r\n# Docker容器命令\r\n\r\n```bash\r\n创建容器（交互式容器）,并进入命令行，一旦退出命令行容器就关闭了\r\ndocker run -it --name 容器名 镜像名:版本  bash\r\n#创建容器（守护式容器）,后台运行,[]为可选，重启docker时，自动启动相关容器。\r\ndocker run -id --name 容器名 镜像名:版本 [--restart always]\r\n#启动容器\r\ndocker start 容器名\r\n#停止容器\r\ndocker stop 容器名 \r\n#删除容器（先停止运行才行）\r\ndocker rm 容器名|容器id\r\n#删除所有容器\r\ndocker rm `docker ps -aq`\r\n#进入某个运行的容器的命令行（退出不会关闭容器）\r\ndocker exec -it 容器名 bash\r\n#列出运行中的容器\r\ndocker ps\r\n#列出所有容器\r\ndocker ps -a\r\n#查看容器信息\r\ndocker inspect 容器名\r\n#实时日志 最新100条，实时更新\r\ndocker logs -f --tail=100 容器名\r\n\r\n#在启动时如果没有添加这个参数怎么办呢，比如某个容器在启动的时候是没有添加–restart=always参数的，针对这种情况我们可以使用命令进行修改。\r\ndocker container update --restart=always 容器名字\r\n\r\ndocker 支持的restart策略\r\nno - 容器退出时不要自动重启，这个是默认值\r\non-failure 在容器非正常退出时（退出状态非0），才会重启容器\r\non-failure[:max-retries] 当容器非正常退出超过设定的次数才会重启\r\nalways 不管退出状态码是什么，始终重启容器，当指定always时，docker daemon将无数次的重启容器，容器也会在daemon启动时尝试重启，不管容器的状态如何。\r\nnuless-stopped 在容器退出时，总是重启，但不考虑在docker守护进程启动之时，就已经停止了的容器，\r\n\r\n\r\n```\r\n\r\n\r\n\r\n# Docker容器数据卷\r\n\r\n```bash\r\n创建容器时挂载数据卷（让容器和外部宿主机进行数据传递）\r\ndocker run -it --name 容器名 -v 宿主机指定目录的绝对路径:容器指定目录的绝对路径 镜像名 \r\n\r\n一个容器挂载多个目录（~相当于/root）\r\ndocker run -it --name 容器名 -v ~/data2:/root/data2 -v ~/data3:/root/data3 镜像名 \r\n\r\n创建数据卷容器（创建一个容器，挂载一个目录，让其他容器继承该容器）\r\ndocker run -it --name 容器名 -v /volume 镜像名 \r\n容器挂载到数据卷容器\r\ndocker run -it --name 容器名 --volumes-from 数据卷容器名 centos:centos7.9.2009 \r\n\r\n如果想把某个项目及其依赖的运行环境打包，可以使用如下命令\r\n下面这些命令不常用，一般都是通过Dockerfile的形式来完成，见后文\r\ndocker 容器转镜像\r\ndocker commit 容器id 镜像名:版本号\r\ndocker镜像转压缩文件\r\ndocker save -o  压缩文件名（例如xxx.tar）镜像名:版本号\r\n将文件转docker镜像\r\ndocker load -i 压缩文件名\r\n\r\n下面两个命令比较重要\r\n\r\n将nginx容器中的nginx.conf拷贝到/server/nginx/conf目录中:\r\ndocker cp 容器名:/etc/nginx/nginx.conf /server/nginx/conf/\r\n\r\n将宿主机的文件拷贝到容器中\r\ndocker cp 宿主机文件路径 容器名:容器文件路径 \r\n```\r\n\r\n\r\n\r\n# Dockerfile \r\n\r\n```bash\r\nSpringBoot项目部署\r\n\r\n编写Dockerfile文件\r\n将Dockerfile和jar包放在同一目录下\r\n在此目录执行如下命令：\r\n\r\ndocker build -t 镜像名:版本 .\r\ndocker run -id --name=容器名 -p 8083:8181 镜像名:版本\r\n\r\n\r\n```\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n# docker-compose 安装\r\n\r\n```bash\r\n1 curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose\r\n\r\n2 chmod +x /usr/local/bin/docker-compose\r\n\r\n3 docker-compose -v\r\n\r\n命令介绍：\r\ndocker-compose up //启动yml文件定义的 container\r\ndocker-compose up -d //后台运行\r\ndocker-compose up --help //查看up帮助\r\ndocker-compose -f docker-compose.yml up //-f 指定yml文件\r\ndocker-compose stop //停止\r\ndocker-compose start \r\ndocker-compose ls  //查看\r\ndocker-compose down //停止删除\r\ndocker-compose ps\r\ndocker-compose images\r\ndocker-compose exec {service_name} {bash}\r\n```\r\n\r\n\r\n\r\n# Portainer 安装与配置\r\n\r\n [Portainer](https://www.portainer.io/) 是一个开源、轻量级Docker管理用户界面，基于Docker API，提供状态显示面板、应用模板快速部署、容器镜像网络数据卷的基本操作（包括上传下载镜像，创建容器等操作）、事件日志显示、容器控制台操作、Swarm集群和服务等集中管理和操作、登录用户管理和控制等功能。功能十分全面，基本能满足中小型单位对容器管理的全部需求。\r\n\r\n```bash\r\n# 搜索镜像 \r\ndocker search portainer/portainer \r\n\r\n# 拉取镜像 \r\ndocker pull portainer/portainer \r\n\r\n# 运行镜像 密码长度必须大于8位，否则报错\r\ndocker run --name portainer \\\r\n--env ADMIN_USERNAME=你的8位密码 \\\r\n--env ADMIN_PASS=root -d -p 9000:9000 \\\r\n-v /root/portainer:/data \\\r\n-v /var/run/docker.sock:/var/run/docker.sock \\\r\n-v /public:/public  \\\r\nportainer/portainer\r\n```\r\n\r\n注意： 在启动容器时必须挂载本地 /var/run/docker.socker与容器内的/var/run/docker.socker连接。\r\n\r\n如果需要汉化，可以下载[汉化包](https://pan.baidu.com/s/1BLXMSmJFcgESeNMhQL26Mg&shfl=sharepset)（提取码：6vjr），之后解压，并将解压后的public文件夹上传到centos系统的根目录下，如下图所示：\r\n\r\n\r\n\r\n\r\n\r\n', '2021-03-22 19:51:49', 0);
INSERT INTO `m_blog` VALUES (130, 130, 'javascript', '计算机编程语言', 'JavaScript（简称“JS”） 是一种具有函数优先的轻量级，解释型或即时编译型的编程语言。虽然它是作为开发Web页面的脚本语言而出名，但是它也被用到了很多非浏览器环境中，JavaScript 基于原型编程、多范式的动态脚本语言，并且支持面向对象、命令式和声明式（如函数式编程）风格。 [1] \r\nJavaScript在1995年由Netscape公司的Brendan Eich，在网景导航者浏览器上首次设计实现而成。因为Netscape与Sun合作，Netscape管理层希望它外观看起来像Java，因此取名为JavaScript。但实际上它的语法风格与Self及Scheme较为接近。 [2] \r\nJavaScript的标准是ECMAScript 。截至 2012 年，所有浏览器都完整的支持ECMAScript 5.1，旧版本的浏览器至少支持ECMAScript 3 标准。2015年6月17日，ECMA国际组织发布了ECMAScript的第六版，该版本正式名称为 ECMAScript 2015，但通常被称为ECMAScript 6 或者ES2015。 [1]', '2021-03-22 19:51:50', 1);
INSERT INTO `m_blog` VALUES (148, 1, '123', '123', '![qjl.png](1)\n\n123', '2021-04-07 23:57:44', 1);

-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` int(0) NOT NULL DEFAULT 1,
  `role` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'user',
  `created` datetime(0) NULL DEFAULT NULL,
  `last_login` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `UK_USERNAME`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` VALUES (1, 'admin', 'https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg', '1907263405@qq.com', '0cfac8931b3ebec3fb6aec9636e33271', 1, 'admin', '2000-08-30 22:59:00', '2021-04-15 04:17:21');
INSERT INTO `m_user` VALUES (2, 'disable', 'https://space1-1300393241.cos.ap-shanghai.myqcloud.com/qjl.png', '2', '0cfac8931b3ebec3fb6aec9636e33271', 0, 'user', '2021-02-23 20:58:02', '2021-02-23 20:58:06');
INSERT INTO `m_user` VALUES (3, 'ad', '123', '12345@qq.com', '0cfac8931b3ebec3fb6aec9636e33271', 1, 'user', '2021-03-07 00:31:05', NULL);
INSERT INTO `m_user` VALUES (4, '工具人1号', NULL, NULL, '0cfac8931b3ebec3fb6aec9636e33271', 1, 'user', '2020-01-13 19:16:43', NULL);
INSERT INTO `m_user` VALUES (11, '小脑虎下山', NULL, 'xnhxs@qq.com', '0cfac8931b3ebec3fb6aec9636e33271', 1, 'user', '2021-03-07 19:23:00', NULL);
INSERT INTO `m_user` VALUES (123, 'user', NULL, 'xnhxs1@qq.com', '0cfac8931b3ebec3fb6aec9636e33271', 1, 'user', '2021-03-07 22:59:00', NULL);
INSERT INTO `m_user` VALUES (130, 'pcdd', 'https://ivueblog-1300393241.cos.ap-beijing.myqcloud.com/images/RE3KXmY_1920x1080.jpg', 'xxx@qq.com', '0cfac8931b3ebec3fb6aec9636e33271', 1, 'admin', '2021-03-13 19:16:34', NULL);
INSERT INTO `m_user` VALUES (146, '1', NULL, NULL, '0cfac8931b3ebec3fb6aec9636e33271', 1, 'user', '2021-03-21 03:33:19', '2021-04-15 04:21:32');
INSERT INTO `m_user` VALUES (147, 'scott', NULL, NULL, 'e7636b647e582a3e497286dce9334d01', 1, 'user', '2021-03-22 09:25:54', NULL);

SET FOREIGN_KEY_CHECKS = 1;
