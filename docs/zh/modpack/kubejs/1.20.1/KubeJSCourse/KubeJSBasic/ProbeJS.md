---
editor: Gu-meng
---
# ProbeJS的基础使用
> 孤梦注：本篇不包含vscode的使用教程，如不会使用vscode请自行百度

首先我们需要[下载ProbeJS](https://www.mcmod.cn/class/6486.html)这个模组，然后需要使用[软件vscode(Visual Studio Code)](https://code.visualstudio.com)，我们需要在`vscode`中去搜索插件：[ProbeJS](https://marketplace.visualstudio.com/items?itemName=Prunoideae.probejs)

**声明：本篇以ProbeJS 6作为示例，在ProbeJS其他版本问题产生问题，本篇无法解决**

## ProbeJS是做什么的
来自mc百科的解释：
**它的功能是收集你的整合包中的方块/物品等信息，供 VS Code 文本编辑器使用。**

**只需要装上模组，简单运行一条命令，VS Code 就能为你的 KubeJS 脚本与其它配置文件提供代码补全与信息提示功能**

简单点说就是能够方便你更好的去使用KubeJS模组

## ProbeJS模组的使用
在百科的[ProbeJS](https://www.mcmod.cn/class/6486.html)的介绍页已经非常详细的写出来了如何使用该模组

下面简单说明一下如何使用：

在第一次使用ProbeJS的时候需要进入一遍游戏，然后在游戏里输入指令`/probejs dump`，然后只需要稍等片刻，模组会自己生成文件，然后可以在对应的`KubeJS文件夹`里使用`vscode`打开(这里也可以直接将文件夹拖进`vscode`里)
> 孤梦注：也可以在对应的游戏安装位置打开

如果你之后对模组有删减的操作，需要在游戏里重新输入指令`/probejs dump`，模组会自动重新生成新的文件

> 来自百科：假如 VSCode 没有在你重新运行命令后刷新自动补全功能，在 VSCode 中按下 F1 键，执行“TypeScript: Restart TS server”功能就可以强制其刷新了

## 在vscode中的常见使用

### 代码补齐
在前面步骤都正常的情况下，`vscode`可以根据`probejs`模组生成的文件进行代码补齐

就比如在平时没有补齐的情况下去打`ServerEvents.recipes`，如果不注意很容易将里面的字母打错，导致代码无法运行后报错

如果你在正常使用该模组和插件的情况下，只需要打`ser`，vscode会列举出来`ServerEvents`...等内容，然后再输入`.`，vscode就会将`ServerEvents`下面的所用方法都列举出来，我们可以找到需要的`recipes`（也可以主动打`re`或者更多字母去匹配这个单词），减少手打时产生的错误

除了这种方法的补齐，vscode还可以进行代码参数的补齐，在我们需要传入物品id或者方块id或者更多其他东西的时候，vscode会根据代码方法所需要的传参类型自动匹配你需要的内容

### 查看方法
在写代码时，我们不知道这个类里有哪些方法或者我需要查看这个类下面有没有我需要的返回类型的方法，这个时候我们可以在vscode中按住`ctrl`然后点进这个方法中去查看，下面是使用场景：

```js
ItemEvents.firstLeftClicked(event=>{})
```
在这个代码中，我并不知道这个`event`里可以获取一些什么值，这个时候，就可以按住ctrl然后用鼠标左键点击`firstLeftClicked`，然后可以会进入下面这个环境中

```js
/**
 * Invoked when a player right clicks with an item **without targeting anything**.
 *
 * Not to be confused with `BlockEvents.rightClick` or `ItemEvents.entityInteracted`.
 * 
 * @at *server, client*
 */
firstLeftClicked(extra: Internal.Item_, handler: (event: Internal.ItemClickedEventJS) => void):void,
firstLeftClicked(handler: (event: Internal.ItemClickedEventJS) => void):void,
```
首先解析一下这个代码
* 首先`firstLeftClicked`这个方法是物品首次左键事件
* 这个方法有两种，第一种是直接检测哪个物品触发这个事件，第二个方法是任意物品都可以触发这个事件
* `handler: (event: Internal.ItemClickedEventJS)`这里代表这里面执行的一个事件内容，就是你需要根据这个事件去执行什么
* 在代码最后面的`void`代表这个方法没有返回值（何为返回值，得自学一下js）
* 这里注意一下注释里的`@at`，这里代表这个方法使用的文件夹，比如这个地方使用在`server`和`client`

这个地方的`ItemClickedEventJS`就是我们下一个需要查看的参数了，我们上面的`event=>{}`里的`event`就是这里的`ItemClickedEventJS`

这里点进去后会有很多内容，我们这里就来看几个主要的
```ts
class ItemClickedEventJS extends Internal.PlayerEventJS {
    constructor(player: Internal.Player_, hand: Internal.InteractionHand_, item: Internal.ItemStack_)
    /**
     * The hand that the item was clicked with.
     */
    getHand(): Internal.InteractionHand;
    get hand(): Internal.InteractionHand
}
```
再来慢慢解析这几行代码
* 首先第一行的`extends Internal.PlayerEventJS`代表除了`ItemClickedEventJS`下面的这些方法可以使用外，还可以使用`PlayerEventJS`下面的方法，这里可以使用同样的方法点进`PlayerEventJS`里，这个地方的`extends`代表继承，这里不做过多解释，只需要知道这个地方可以用`extends`后面的类里面的方法
* 第二行的`constructor`这个在我们使用这个事件时，不必理会，这个是代码这个类的构造方法(这里属于Java的代码部分，不做过多赘述，只需要知道这个地方你不用管就好了)
* 再来看看下面的`getHand()`和`get hand()`的区别，在很多时候，我们再看别人的代码时，发现他们有时候是`event.hand`但是有时候是`event.getHand()`，在这里 `event.hand`等同于`event.getHand()`
* 这里的`getHand()` 是一个方法，所以就不做过多解释，来看看让人看不懂的`get hand()`这里为什么不和`getHand()`一样的写成`event.get hand()`而是`event.hand`，首先后面的`hand`并不是一个方法而是一个属性、参数，而前面的`get`代表这个是可以直接获取的，除了`get`外还有`set`，举个例子`set hand()`这个时候我们就可以这样写`event.hand = xxx`，在ItemClickedEventJS并没有提供设置这个属性，所以你无法直接写成`event.hand = xxx`这样
* 在`getHand()`和`get hand()`的冒号后面跟着`Internal.InteractionHand`代表这个获取到的是一个`InteractionHand`类型

然后这个地方我们`ctrl+右键`点击`InteractionHand`里又会有下面这些东西，这里只提取出之前没见过的
```js
compareTo(arg0: Internal.InteractionHand_): number;
"compareTo(net.minecraft.world.InteractionHand)"(arg0: Internal.InteractionHand_): number;
"compareTo(java.lang.Object)"(arg0: any): number;
static valueOf(arg0: string): Internal.InteractionHand;
static readonly MAIN_HAND: (Internal.InteractionHand) & (Internal.InteractionHand);
```
来一行行解析这些代码
* 首先可以看到这前三行的代码基本上都差不多，那为什么二三行是用引号引起来呢，这是因为kubejs在处理这种一个方法名但是传入的参数不同时，无法正确的做出选择，这个时候就得人为选择你调用的方法和里面所需要的传参，这个地方举个例子，如果你的方法里需要传入一个`InteractionHand`那么我们就可以这样写`event.hand["compareTo(net.minecraft.world.InteractionHand)"](你的参数)`
* 下面的`static valueOf(arg0: string)`是一个静态的方法，这个和一起讲
* `static readonly MAIN_HAND` 这里是一个静态的属性，并且只能读，就是说只能调用获取，返回的是一个InteractionHand
* 这种静态的一般是如何使用的呢？这里需要有一定的Java基础，我们会用到`Java.loadClass()`去获取到这个类，比如`Java.loadClass("net.minecraft.world.InteractionHand")`这样获取到这个类，然后我们就可以进行使用静态方法和静态属性
```js
const $InteractionHand = Java.loadClass("net.minecraft.world.InteractionHand")
ItemEvents.firstLeftClicked(event=>{
    let hand = event.hand
    if ($InteractionHand.MAIN_HAND == hand){
        // code
    }
})
```
这个代码就是利用`InteractionHand`里的静态属性`MAIN_HAND`来比较我们从事件里获取的是主手还是副手，因为这些属于loadclass的知识，并不是本篇主要展示的内容

### 快速插入
在使用插件后，我们可以直接使用`@`来获取很多内容，我们常用的有：@item、@dimension、@block、@entity...这些在输入后，vscode会在你输入的地方开始补齐你要的内容，比如你输入`@item`后，vscode就会在你`@`的地方生成字符串的物品id，其他的也是差不多的逻辑

### JSDoc
该内容本不该出现在该篇章，但是还是可以简单提一嘴，因为probejs在少数情况下给的类型不太对，导致我们无法正常的补齐需要的内容，这个时候我们就需要主动告诉vscode，我们需要的类型是什么，就比如下面这个代码
```js
ItemEvents.firstLeftClicked(event=>{
    event.player
})
```
我们进入到player里`get player(): Internal.Player`，可以看到返回的是Player类型，那实际是不是player呢？

我们可以输出一下

```js
ItemEvents.firstLeftClicked(event=>{
    console.log(event.player);
})
```
发现输出的是`ServerPlayer['gu__meng'/179, l='ServerLevel[新的世界]', x=7.68, y=71.02, z=-12.04] [net.minecraft.server.level.ServerPlayer]`，我们只需要看类型，前面的内容都不需要查看，类型为`net.minecraft.server.level.ServerPlayer`,也就是`ServerPlayer`，并不是ProbeJS提供的`Player`，就导致我们很多`ServerPlayer`下面的方法无法被补齐，这个时候我们可以这样写

```js
ItemEvents.firstLeftClicked(event=>{
    /**
     * @type {Internal.ServerPlayer}
     */
    let serverPlayer = event.player;
})
```
使用jsdoc的@type 的方法指定serverPlayer为`Internal.ServerPlayer`类型

在ProbeJS中，大多数类都是在`Internal`下面`.`出来的，如果点不出来所需要的类，说明probejs没有找到，这个时候就得去找对应类的Java源码去找方法了(这个情况在原版的情况下很少出现)

[点击查看更多JSDoc使用场景](https://docs.mihono.cn/zh/modpack/kubejs/1.20.1/Introduction/Addon/ProbeJS/JSDoc)
