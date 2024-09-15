# 前言

![](https://m1.miaomc.cn/uploads/20220507\_7fc6e1d8f48a4.png)

## 简要介绍

KubeJS是一个于1.16.5版本开始兴起的基于JavaScript的魔改核心模组，同时支持Fabric和Forge环境。在热重载的加持下，您可以很便捷地修改游戏中的绝大多数内容。从新增/修改物品，方块，配方到自定义游戏内逻辑、修改战利品表，自定义世界生成......没有什么是不能借助KubeJS轻松实现的。除此以外，KubeJS还可以用于管理服务器，修改客户端显示内容等。

该教程由[Wudji (github.com)](https://github.com/Wudji)编写，自MCBBS关站后，整理至Gitbook。

该教程的markdown源文件请见[https://github.com/Wudji/XPlus-KubeJS-Tutorial](https://github.com/Wudji/XPlus-KubeJS-Tutorial)

Wudji的爱发电链接：[https://afdian.net/a/Wudji\_XPlusmodpack](https://afdian.net/a/Wudji\_XPlusmodpack)

## 本文相关信息

本文将翻译，讲解部分该mod的常用功能及其附属mod，并提供多个实例以供参考

本文内没有特别注明的原创内容，均根据[知识共享署名-非商业性使用-相同方式共享 4.0 国际许可协议](https://www.mcbbs.net/plugin.php?id=link\_redirect\&target=https%3A%2F%2Fcreativecommons.org%2Flicenses%2Fby-nc-sa%2F4.0%2F)进行许可

受本人水平，时间因素及KubeJS自身版本更新的影响，本文中难免还存在一些问题/过时内容，还请各位读者斧正，谢谢！

## 实用链接

KubeJS 官方Wiki：[https://mods.latvian.dev](https://mods.latvian.dev)

KubeJS MCMOD搬运贴：[https://www.mcmod.cn/class/2450.html](https://www.mcmod.cn/class/2450.html)

KubeJS 源代码(1.16.5分支)：https://github.com/KubeJS-Mods/KubeJS/tree/1.16/main/common/src/main/java/dev/latvian/kubejs

KubeJS 官方Discord交流频道：https://discord.gg/hCVTFHKE

本文部分内容参考/翻译自以上内容，在这里表示感谢

## 修改内容展示

自定义合成，详见本文 第2章配方修改

![](https://i1.mcobj.com/imgb/u18prz/20240225\_65db06a132e01.png)

针对机械动力配方的修改，详见本文 第12章附属mod

![](https://i1.mcobj.com/imgb/u18prz/20240225\_65db06c38ea35.png)

自定义世界生成（自定义矿石），详见本文 第6章自定义世界生成

![](https://i1.mcobj.com/imgb/u18prz/20240225\_65db06e27819f.png)

自定义聊天内容前缀(doge)，详见本文 第8章聊天信息修改

![](https://m1.miaomc.cn/uploads/20220407\_b9de677546c5d.png)

随机传送，详见本文 第15章玩家事件，信息获取及操作 & 方块、物品、实体信息获取及操作 & 玩家交互

![](https://m1.miaomc.cn/uploads/20220408\_c412408992599.gif)
