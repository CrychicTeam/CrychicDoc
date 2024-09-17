# ProbeJS概述 {#Summary}

ProbeJS是一个`KubeJS`附属模组，它为`KubeJS`脚本的编写带来了`语法补全`，并提供了各种便利的功能。

**该模组可以:**

1. 生成类型补全文件，使`VSCode`能够自动提示对象能够使用的方法，提示方法需要的参数同时支持`方法重载`。
2. 自动生成`Snippets`文件，使用@或#能够自动补全为具体的代码片段，例如键入@Item可以方便地获取到你需要的物品。

> [!TIP] 注意
> 请确保自己使用了正确的版本<br>
> 1.20.1使用6.0.1

## 使用方法 {#Use}

将`KubeJS`与其余前置模组与ProbeJS一同加载，进入Minecraft任一存档后运行
```
/Porbejs dump
```
然后使用VScode打开你的`版本文件夹`[^version]，有多种方式可以参考：
1. 右键点击你的`版本文件夹，选择“在 VSCode 中打开”
2. 打开VSCode，右键`文件`[^Vscode汉化]，选择`打开文件夹`，打开你的`版本文件夹`。

[^version]: 版本文件夹一般是.minecraft，但如果是导入的整合包，则在启动器的version中可以找到<br>
    只要打开的是`kubejs文件夹`的上级目录即可。

[^Vscode汉化]: 如果没有安装请[安装VSCode汉化主题](https://zhuanlan.zhihu.com/p/142083916)：MS-CEINTL.vscode-language-pack-zh-hans。

### 注意 {#Caution}

- 如何判断ProbeJS是否正常生成了配置文件？

检查 **版本文件夹/kubejs/** 是否有着`porbe`文件夹与`jsconfig.json`文件。

- 为什么自动补全里没有我想要的函数？

可以查看[该章](./JSDoc)解决类型问题。<br>
如果未能解决问题文档内也未写明相关内容，请留下讯息来让我们提供帮助或直接[加群](https://qm.qq.com/q/yxOO4x9uQE)请求协助。

### 版本迁移 {#Version}

如果你曾使用过7.0.x版本的ProbeJS。

你应删除原本的`类型文件`与`jsconfig.json`,7.0.x的类型文件位于 **版本文件夹/.probe/** 中,并重新[通过指令](#Use)生成类型文件。