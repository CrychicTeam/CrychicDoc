---
authors: ['Wudji']
---

# 1.1 常用指令

***

KubeJS添加了一些指令，这可以提高编写脚本的效率：

使用`/kubejs custom_command <command>` 可以执行自定义指令

使用`/kubejs errors` 可以在聊天栏中获取当前脚本的报错

使用`/kubejs export` 可以将游戏内的配方、tags、所有方块、实体类型、流体类型导出到kubejs\exported\kubejs-server-export.json

* 注：你可以将该json文档上传至https://export.kubejs.com/来进行分析(截至编辑本文时该功能不可用)

使用`/kubejs hand` 或 `/kjs_hand` 可以快速获取手中物品信息，这对于配方自定义等非常有帮助(点击文本即可复制)

使用`/kubejs hotbar` 可以将快捷栏中所有物品信息打印到聊天栏(同`/kubejs hand`)

使用`/kubejs inventory` 可以将玩家物品栏的所有物品信息打印到聊天栏(同`/kubejs hand`)

使用`/kubejs offhand` 可以将玩家副手的物品信息打印到聊天栏(同`/kubejs hand`)

使用`/kubejs list_tags <tag> [block|fluid|item|entity_type]` 来将给定标签的内容打印到聊天栏

* 如`/kubejs list_tag minecraft:logs item` 会将`#minecraft:logs`标签下的元素打印出来

使用`/kubejs painter <玩家名称> <PainterJS对象>` 来调用PainterJS(见第十六章)

使用`/kubejs reload [server_scripts|lang|texture|startup_scripts]` 来重载服务器类型脚本、语言文件、纹理资源和启动类型脚本

* 其中`/kubejs reload server_scripts`和`/reload`的效果基本相同
* `/kubejs reload startup_scripts` 并不能重载所有启动脚本事件，如方块注册等

使用`/kubejs stage [add|list|remove|clear] <玩家名称>` 来为指定玩家添加、列出、移除或清除游戏阶段

* 关于Gamestage的详细介绍见11.4和11.5章节

使用`/kubejs warnings` 来查看当前脚本中的警告信息

使用`/kubejs wiki` 来打开官方KubeJS Wiki
