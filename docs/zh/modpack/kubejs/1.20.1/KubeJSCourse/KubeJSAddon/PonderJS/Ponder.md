---
authors:
  - Gu-meng
  - Qi-Month
editor: Gu-meng
---
# Pobder基础内容
本文中未提及到在哪个文件夹新建，则全部在`client_scripts`下

本文代码位置 [KubeJS](https://gitee.com/gumengmengs/kubejs-course/tree/main/Code/Ponder/kubejs/) 如需可参考

## 正式开始
由于`Ponder`属于客户端脚本, 除了使用原版的`F3+T`进行重载外, 同时也可以使用指令`kubejs reload client_script`进行重载

```js
Ponder.registry(event => {});
```

## 场景创建

下面的脚本可以创建一个场景:

```js
Ponder.registry(event => {
  event
    .create("kubejs:submarine_core") // 填入需要添加 Ponder 的 Item/Tag, 填入多个时要用 [ ] 包裹
    .scene(
      "kubejs:submarine", // Ponder ID
      "潜水艇 ", // 侧边显示的标题
      (scene, utils) => {
        // 在 { } 内的即是此场景的内容
      }
    );
});
```

一个 Item 可以添加多个 Ponder, 具体效果可以看游戏内 Ponder 界面的翻页

> 多场景有几种创建方法

```js
// 1 .scene() 后面直接在接一个 .scene()
Ponder.registry(event => {
  event
    .create("kubejs:submarine_core")
    .scene("kubejs:submarine_1", "潜水艇", (scene, utils) => {
      // 场景1
    })
    .scene("kubejs:submarine_2", "潜水艇 ", (scene, utils) => {
      // 场景2
    });

  // 2 另外开一个,  实际上, 即使是在另一个档案中创建也行
  event
    .create("kubejs:submarine_core")
    .scene("kubejs:submarine_3", "潜水艇", (scene, utils) => {
      // 场景3
    });
});
```

- 注意要点 :
  > 会依读取的顺序成为第 1 ~ n 个场景, 透过翻页来切换
  >
  > 不同场景的 Ponder ID 一样并不会导致报错, 但会产生`标题错误`或者`文本错误`等问题

# 配置起始结构

首先, 我们需要一个结构(例如地板), 你有两种方法配置起始结构

1. 这是机械动力作者用来直接创建地板的方法

```js
// 在 { } 内使用这个函数, 将会生成 1 个 n * n, 西移 x 格, 北移 z 格的地板(开启开发者模式后便可查看各个方块的坐标及方位)
// 其三个参数分别用于配置: X偏移, Z偏移以及地板大小(x < 0 即是东移, z 同理)
// P.S 内建地板最多5x5, n>5时, 范围会出现, 但只会设置5x5的方块
scene.configureBasePlate(x, z, n);
```

1. 自己准备起始结构

首先, 你需要一个 `.nbt` 文件

可以用机械动力的 [**蓝图与笔**](https://www.mcmod.cn/item/347848.html) 或者原版的 [**结构方块**](https://www.mcmod.cn/item/35469.html) 获取 NBT 的结构(这俩自己去学习用法, 本教程并不会提及)

**这边提一个至关重要的点, 如果你想要获取的结构仅仅只是一个地板, 并且想要在这个地板的基础上做出很多 `Ponder` 的话, 那地板上面所预留的空气方块也一样要足够!如果没有足够的空气方块那么普通方块也不会正常的显示!**

![图片](/imgs/PonderJS/jie-gou-fang-kuai.png)

**最推荐的就是像上图这样直接搞一个正方体的结构, 包括空气方块!(经实际测试发现, 想要搞一个像这样空白空气方块的结构, 使用`蓝图与笔`并不能做到, 只能使用原版的`结构方块`)**
在获取一个完整的 nbt 结构文件时, 最好把地板也一起搭建并打包好

可以打开 Ponder 的开发者模式, 用于显示坐标(限制存档, 新存档需要再次开启)

![配置](/imgs/PonderJS/pei-zhi.gif)

开启开发者模式后便可查看各个方块的坐标

![坐标](/imgs/PonderJS/zuo-biao.gif)

> 将 nbt 文件存储在 [kubejs/assets/kubejs/ponder/](https://gitee.com/gumengmengs/kubejs-course/tree/main/Code/Ponder/kubejs/assets/kubejs/ponder)

在创建场景时额外填入参数来读取自己准备的起始结构

```js
Ponder.registry(event => {
  event.create("kubejs:submarine_core").scene(
    "kubejs:submarine",
    "潜水艇 ",
    "kubejs:submarine", // 读取的结构文件名称, 可于 kubejs/assets/kubejs/ponder/自行下载
    (scene, utils) => {}
  );
});
```

# 显示起始结构

> 在最开始, 我们需要显示部分的结构(例如地板), 你有一些选择, 在 `{}` 内输入

```js
// 显示读取的nbt文件中, y = 0 的部分
scene.showBasePlate();
// 显示读取的nbt文件中, y = 0 ~ n 的部分，也可不填参数, 那个场合将整个nbt文件的结构显示
scene.showStructure(n);
```

> P.S : 即使选用作者自带的生成地板方法, 也是要手动显示地板滴
>
> showBasePlate() 和 showStructure(0) 完全相等

# 适当的等待

> 显示完底盘直接开始动画你可能会觉得稍显快速, 此时你可以使用

```js
// 等待 20 Tick
scene.idle(20);
// 等待 1 秒
scene.idleSeconds(1);
```

> 这边提一嘴, 在 Ponder 编写完成后一定要停顿至少一秒

> 20 Tick = 1 秒, 关于 Tick 更多信息可以到 👉[刻#游戏刻与计算速率 @ Minecraft 中文 wiki](https://zh.minecraft.wiki/w/刻#游戏刻与计算速率)👈 进行查看, 这里不做过多的赘述
> 
此时你的代码应该如下面所示

```js
Ponder.registry(event => {
  event
    .create("kubejs:submarine_core")
    .scene("kubejs:submarine", "潜水艇", "kubejs:submarine", (scene, utils) => {
      // 显示底盘, 同时等待 20 Tick
      scene.showBasePlate();
      scene.idle(20);
    });
});
```

# 关键帧

在 Ponder 场景的演示动画中, 我们难免会遇见动画太长而我们只看某一段的场面, Ponder 却不像视频那样可以直接拖动进度条, 但是却有一个接近的东西, 在某些关键的地方创建一个个的跳转点

![关键帧](/imgs/PonderJS/guan-jian-zhen.gif)

```js
// 在 当前 Tick 创造一个关键帧
scene.addKeyframe();

// 在 5 Tick 后(当前Tick往后数的第六个Tick)创造一个关键帧
scene.addLazyKeyframe();
```

经过测试

```js
// 连续2个 addKeyframe 只会创造一个关键帧
scene.addKeyframe();
scene.addKeyframe();

// 如此则会创造两个关键帧, 其间隔 5 Tick
scene.addKeyframe();
scene.addLazyKeyframe();
```

# 放置方块

> 下一步, 我们想要把鼓风机显示出来, 我们可以用 Ponder 场景中的地板坐标, 大致推算出各个方块的位置

![结构展示](/imgs/PonderJS/jie-gou-zhan-shi.png)

- 注意 : 离你最近的地板方块的坐标是 `[0, 0, 0]`, 往`左`、`上`、`右`分别对应 `x, y, z`

> 根据上图的结构我们得知右边的鼓风机的位置在 `[2, 　1, 　1]`, 那我们接着写

```js
// 在 [2, 1, 2] 放置鼓风机方块, 若该位置原本有方块, 则破坏该原本方块
scene.world.setBlocks([2, 1, 1], "create:encased_fan");

// 同上, 若第三个参数为 false 则不显示破坏方块时的粒子效果
scene.world.setBlocks([2, 1, 1], "create:encased_fan", true);
```

> 值得一提的是, 后两个参数似乎是可以对调的, 即
>
> scene.world.setBlocks([2, 1, 1], "create:encased_fan", false); = scene.world.setBlocks([2, 1, 1], false, "create:encased_fan");

# 显示方块

仅仅放置方块并不够, 你还需要将它显示出来

```js
// 以从上面下落到坐标 [2, 1, 1] 的动画形式显示出这一格的方块
scene.world.showSection([2, 1, 1], Direction.down);
```

如果我们想要显示某区域的方块可以直接写

```js
scene.world.showSection([3, 1, 1, 1, 1, 3], Direction.down);
```

这样以 `[3, 1, 1]` 及 `[1, 1, 3]` 为对角组成的`矩形区域内`的方块全部都会以下落的方式展现出来

同时上面的`setBlocks`也可以通过这样的方式以达到快速放置方块的效果

若是某格方块是`已显示状态`, 此时在该格放置方块时, 该方块会直接显示出来, 不必再显示一次

例如: 使用 `showStructure(n)` 时, y = 0 ~ n 的个格子全部变为`已显示状态`, 即使是空气方块

### 注意
>
> - `[3, 1, 1, 1, 1, 3]`同样可以写为`[1, 1, 1, 3, 1, 3]`
> - 并且无论哪种写法, `showSection`动画都将持续 15 Tick
> - `showSection`方法将会把显示的区域合并到`baseWorldSection`中, 即`scene.ponderjs$getPonderScene().baseWorldSection`

# 修改方块状态
很多时候会遇到需要指定方块的朝向,类型(如上/下/满半砖)的情况, 可以使用如下方法来修改方块状态:

`scene.world.modifyBlock(方块位置, state => state.with("要修改的状态名", "要修改的状态值"), 是否有粒子效果);`

举例：
```js

//将1,1,1处的方块的面朝向改为向下,无破坏粒子
scene.world.modifyBlock([1, 1, 1], state => state.with("facing", "down"), false);

//将2,2,2处的方块的Eye属性改为True(让末地传送门框架放上末影之眼),产生破坏粒子
scene.world.modifyBlock([2, 2, 2], state => state.with("Eye", "true"), true);

```
# 修改方块nbt
![盛水的流体储罐](/imgs/PonderJS/liu-ti-chu-guan-nbt.png)
![流体储罐的方块状态](/imgs/PonderJS/liu-ti-chu-guan-fang-kuai-zhuang-tai.png)
如图，这个流体储罐里有1000mB水，但是方块状态里并没有存储相关信息。

F3+i(复制指向的方块的信息)看看：

`/setblock 24 56 -57 create:fluid_tank[bottom=true,shape=window,top=false]{Boiler:{ActiveHeat:0,Engines:0,PassiveHeat:0b,Supply:0.0f,Update:1b,Whistles:0},Height:2,LastKnownPos:{X:24,Y:56,Z:-57},Luminosity:0,Owner:[(所有者的UUID)],Size:1,TankContent:{Amount:1000,FluidName:"minecraft:water"},Window:1b}`

可以看到，内含的流体信息(TankContent)并不在方块状态(方括号括起来的内容)里,而是在nbt信息(花括号括起来的内容)里。
那该如何修改呢？

```js
modifyTileNBT(选区,  nbt => {nbt内容}, 是否重绘方块(可选))
```
举例:

```js
scene.world.modifyTileNBT([2, 3, 3], (nbt) => {
    nbt.Patterns = [
        {
            Color: 0,
            Pattern: "pig"
        }
    ]
})

scene.world.modifyTileNBT([3, 3, 2], (nbt) => {
    nbt.Patterns = [        {
            Color: 0,
            Pattern: "cre"
        }
    ]
})
```

> 方块nbt的修改实际上涉及到了方块实体,SNBT相关知识.
> 
> 进阶教程中会有更详细的讲述.

# 文本显示

文本显示很简单, 这里我不会讲的特别详细(因为也没东西可以讲呀…awa)

文本显示有两种, 第一种是这种从某个坐标延伸出来的文本框

![文本1](/imgs/PonderJS/wen-ben-1.png)

```js
// 显示 40 Tick "文本"
scene.text(40, "文本", [4.5, 3.5, 2]);
```

第二种则是直接在右上角显示的文本框

![文本2](/imgs/PonderJS/wen-ben-2.png)

```js
//  显示 30 Tick "文本"
scene.text(30, "文本");
```

结合上文的关键帧知识点, 我们也可以在文本处直接创建一个关键帧

```js
scene.text(30, "文本").attachKeyFrame();
```

> 根据对 Minecraft 的了解, 在文本前输入不同的代码可以使其呈现出不一样的效果, 例如加粗以及图中的蓝色文本
> 具体请到 👉[Minecraft wiki](https://zh.minecraft.wiki/w/格式化代码?variant=zh-cn#颜色代码)👈 进行查看

# 包边

和文本显示一样, 都没什么可以讲的, 所以挺短的

![包边](/imgs/PonderJS/bao-bian.png)

```js
/*
 *red 指的是边框的颜色, 但是Ponder场景自身似乎支持的颜色并不多
 *坐标的选取可以和上面的一样直接选择一个区域, 就如同图里的一样
 */
scene.overlay.showOutline("red", {}, [7, 1, 3, 3, 5, 7], 30);
```

# 操作交互

这种就是典型的一种右键操作示例图

![右键](/imgs/PonderJS/you-jian.png)

交互显示并不会帮你实现任何操作, 它能做的仅仅只有显示一个小框框在你的屏幕上, `告诉看的人这里的交互方式是左键亦或是右键乃至其它方式`, 想要显示需要额外的操作, 需要将不同的效果在一起呈现出来, 这里就是最基础连接的开始了, 好好运用前面的知识, 尝试将两种效果放在一起. 先来看一段 GIF(这个是我在很久以前另一个包录的)

![右键操作](/imgs/PonderJS/you-jian-cao-zuo.gif)

这里就很经典的运用了两个知识点, `右键`和`替换方块`, 我们先看看`右键`的代码

```js
scene.showControls(30, [3, 1, 5], "left") // 在 [3, 1, 5] 的右方创建一个向左指的框, 时长为 30 Tick
  .rightClick() // 在框内显示 鼠标右键 的图示
  .withItem("immersiveengineering:hammer"); // 在框内显示 "immersiveengineering:hammer" 的图示
```

没错, 就这么简单, 一行行拆开来看也是非常的简单易懂

如果搭配上`方块替换`来看, 整串的代码就是这样

```js
// 在 [2, 1, 2] 的上方创建一个向下指的框, 时长为 80 Tick, 框内显示 鼠标右键 及 "kubejs:sturdy_sheet_block"
scene.showControls(80, [2, 1, 2], "down")
  .rightClick()
  .withItem("kubejs:sturdy_sheet_block");

// 替换方块
scene.world.setBlocks([2, 1, 2], "mekanism:cardboard_box");
```

> 此处额外列举其他接在 showControls 后面的方法

```js
scene.showControls(80, [2, 1, 2], "down")
 .clone(); // 暂时不知道功能
 .scroll(); // 在框内显示 鼠标中键(滚轮) 的图示
 .whileCTRL(); // 在框内显示 CTRL 的图示
 .withWrench(); // 在框内显示 机械动力的扳手 的图示
 .showing(picon); // 在框内显示 picon 对应的的图示, 所有 picon 可于 kubejs/constant/PonderIcons.md 确认
 .leftClick(); // 在框内显示 鼠标左键 的图示
 .whileSneaking(); // 在框内显示 潜行 的图示
```

[PonderIcons.md 跳转连接](./Internal/PonderIcons.md).
