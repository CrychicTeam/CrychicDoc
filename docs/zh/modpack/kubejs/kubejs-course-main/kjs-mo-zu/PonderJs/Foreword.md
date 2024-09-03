# 前言

> 什么是 Ponder ?

为物品创建沉浸式场景，让玩家更容易理解其用途及细枝末节

> 为什么教程中的图会有紫黑色的方块

因为在本篇教程的同时我也在制作[整合包](https://www.mcmod.cn/modpack/709.html), 图中的紫黑色方块是我使用`KubeJS`注册的一个新方块.由于录制的时间不同, 紫黑色是还未有贴图的时候, Minecraft 自身的规则是没有贴图会自动渲染成紫黑色的方块, 后面有了贴图自然也就不一样了

# 开始之前

Ciallo ～(∠·ω< )⌒☆ 这里是**柒星月**~, 你也可以叫我**柒月**, 那么在开始之前呢, 我们先来看一段完整的`Ponder`演示, 以方便了解一下`Ponder`究竟可以做什么

![完整gif](/imgs/PonderJs/wan-zheng.gif)

> 本处使用的范例文件为 [Submarine.js](https://gitee.com/gumengmengs/kubejs-course/tree/main/code/Ponder/kubejs/client_scripts/Ponder/Submarine.js)
>
> 其调用的 nbt 文件在 [submarine.nbt](https://gitee.com/gumengmengs/kubejs-course/tree/main/code/Ponder/kubejs/assets/kubejs/ponder/submarine.nbt)

可以看到 `Ponder` 除了 `Create` 自带的用法外, 我们还可以使用它制作出 `Modpack` 内某些 `多方块结构` 以及 `世界合成` 等涉及到多个方块的工作方式
