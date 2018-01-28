1、什么是热更新
    当一个App发布之后，突然发现了一个严重bug需要进行紧急修复，这时候公司各方就会忙得焦头烂额：重新打包App、测试、向各个应用市场和渠道换包、提示用户升级、用户下载、覆盖安装。有时候仅仅是为了修改了一行代码，也要付出巨大的成本进行换包和重新发布。
这时候就提出一个问题：有没有办法以补丁的方式动态修复紧急Bug，不再需要重新发布App，不再需要用户重新下载，覆盖安装？
线上程序出现Bug,在不想重新发布包让用户更新安装的情况下,可以使用热修复,让用户在不知不觉就修复了程序的问题.

2、TinkerPatch的官方网站：（TinkerPatch的SDK完全集成了Tinker的SDK，我们直接使用TinkerPatch就行，不用依赖任何Tinker的SDK）
http://www.tinkerpatch.com/Docs/intro

3、集成流程：
完全按照步骤2中的官网流程来就行
备注：
根据 tinkerpatch.gradle 文件中
 /** 是否使用一键接入功能  **/
    reflectApplication = true
是否为true，当为true是使用自己的Application，当为false使用TinkerSDK中的application，见文档。
  备注：箭头指向的appkey从TinkerPatch后台创建应用是获取，appVersion和应用版本号保持一致，这两个参数必须使用我们自己的

4、参考Demo，参见

https://github.com/TinkerPatch/tinkerpatch-easy-sample （ reflectApplication = true）这Demo是重点；

https://github.com/TinkerPatch/tinkerpatch-sample（reflectApplication = true）

5、生成包的
TinkerPatch 的使用步骤非常简单，一般来说可以参考以下几个步骤：
  1. 运行 gradlew assembleRelease 生成旧代码的apk包，每次编译都会生成对应的文件夹

  2. 然后修改bug
  3. 修改 tinkerpatch.gradle中的appVersion变量 和我们的旧代码的应用版本保持一致
  4.修改tinkerpatch.gradle中的baseInfo变量
  4. 运行  gradlew  tinkerPatchRelease 或者  gradlew  tinkerPatchDegbug 构建补丁包，补丁包将位于 build/outputs/tinkerPatch下。
  5. 然后去TinkerPatch的官方网址上创建应用，然后添加版本，这个版本和 tinkerpatch.gradle中的appVersion保持一致，如1.0.0

备注：
如果涉及到多渠道的话，那这个添加版本的版本号就必须和tinkerpatch.gradle中的对应的渠道的
appVersion保持一致，如箭头中，是按版本号加下划线加渠道名字来的
如:     1.0.0_xiaomi

6、关于支持多渠道：
在app的build.gradle中正常使用多渠道分包

然后只需要在tinkerpatch.gradle中添加下图中的内容即可，有多少个渠道添加多少个，

备注：
gradlew tinkerPatchAllFlavorsDebug 和 gradlew tinkerPatchAllFlavorsRelease 用于一次性生成所有flavors的Patch包。
然后在 后台创建补丁版本的时候，需要为每个需要修复的渠道创建一个版本如 : 1.0.0_xiaomi

-----------------------------------------------------------------------
整体的一个备注：
1、因为默认Tinker3个小时才访问一次去更新补丁，如果我们测试的话，可以使用TinkerPatch.with().fetchPatchUpdate(true);这种方式，立即更新补丁，然后只需要
重启应用就可以修复补丁
关闭应用的api：
ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
android.os.Process.killProcess(android.os.Process.myPid());/
2、TinkerPatch的后台有预览模式，可以测试release包
3、可以通过修复背景颜色或者其他方式，来做Demo演示热修复
对TinkerPatch的一个分析：
其实只需要备份老的代码的apk，然后通过配置baseInfo和老的apk的所在文件夹名字保持一致，配置appversion和老的apk的应用版本号名字一致如1.0.0. 然后修复bug后，通过新旧代码的比较，可以生成对应的patch补丁包，将这个patch补丁包以appVersion为版本名字配置到后台（多渠道是以具体渠道中的appVersion为准，如1.0.0.xiaomi），这样用户的老的apk就可以获取到版本号对应的patch补丁包，就可以在合并后重启的时候实现bug的修复，
简单的来说就是通过新旧代码的对比生成patch补丁包，然后根据版本号下载对应版本号的补丁，实现bug修复

