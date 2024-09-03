# 模型
## 一、关于Blockbench

* [**Blockbench**](https://www.blockbench.net)可以说是Minecraft界的建模神器了,你们所见到的奇奇怪怪的方块基本都是Blockbench做的(但也有少数[`obj`](/ti-wai-hua/ForgeReadObjModel.md)格式的进阶模型),例如这个锻造台\
  ![smithing\_table_1.png](/imgs/Blockbench/smithing_table_1.png)![smithing\_table_2.png](/imgs/Blockbench/smithing_table_2.png)
* **文件路径**
  * 模型文件往往存放于`assets/${modid}/models`
  * 和`textures`文件夹一样,`models`文件夹内一般分为`block`和`item`两个文件夹
  * 取决于你做的模型类型如果是方块就放入`block`内,如果是物品就放入`item`内
  * **保存时一定得是`json`格式的模型文件**
  * 而贴图则是置于`assets/${modid}/textures`内,和上面一样,方块放`block`,物品放`item`,可以的话最好和模型存放于一个`${modid}`下

## 二、使用

(本教程只教最简单的部分,惊喜的部分请自己另找教程)

### **1、创建项目**

* 首先在选择模型时请选择他已经给出的Java版模型,这个类型所导出的模型都是json格式,也正好是Minecraft所需要的格式\
  ![menu.png](/imgs/Blockbench/menu.png)
* 选择模型后他会还有一个弹窗,文件名写你的物品或方块ID即可,其它不用管\
  ![project.png](/imgs/Blockbench/project.png)

### **2、创建第一个模型**

* 在进入工作区后我们会看到下面的UI,通过下图来了解一下工作区最常用的几个按钮所相对应的功能(块其实就是模型)\
  ![ui.png](/imgs/Blockbench/ui.png)
* 我们先一顿操作猛如虎随便搞个模型出来\
  ![model\_1.png](/imgs/Blockbench/model_1.png)
* 随后我们保存模型,保存到上面所说的`assets/${modid}/models/block`\
  ![model\_2.png](/imgs/Blockbench/save.png)\
  写代码...
```js
StartupEvents.registry("block", (event) => {
	// 方块ID要和模型文件名一样
    event.create("test_block")
})
```

### **3、为模型设置贴图**

* 这个时候如果注册进游戏显示的将会是下图...紫黑块,并且手上的模型和物品栏模型的方向以及大小看起来和别的方块很不一样...\
  ![model\_1.png](/imgs/Blockbench/game/model_1.png)
* 我们先来解决一下贴图的问题,我们将贴图放在上面提到的`assets/${modid}/textures/block`中,然后打开blockbench点击左下方的导入纹理(也可以导入多个纹理),或是自己创建一个纹理(这里不教)\
  ![textures\_1.png](/imgs/Blockbench/textures/textures_1.png)![textures\_2.png](/imgs/Blockbench/textures/textures_2.png)
* 导入纹理后我们还要给模型贴上贴图,有两种方式
  * 第一种是直接将贴图给拖到模型上\
    ![textures\_3.png](/imgs/Blockbench/textures/textures_3.png)
  * 第二种是右键模型`选择纹理`\
    ![textures\_4.png](/imgs/Blockbench/textures/textures_4.png)
  * 有时候贴上贴图后会有下面这奇怪的现象\
    ![textures\_5.png](/imgs/Blockbench/textures/textures_5.png)
  * 把视角放到左上角的贴图处理的地方,这里的贴图处理可以选择贴图的所选位置,在这里进行选取的贴图区域在模型上也会同步展示
  * 至于下面那一排按钮...为什么不自己动手试试呢? Ciallo ～(∠·ω< )⌒☆\
    ![textures\_6.png](/imgs/Blockbench/textures/textures_6.png)
  * 一个模型一共有6个面,每个面都可以是不同的贴图,而每个模型都可以使用不同的贴图,还是一顿操作猛如虎我们把纹理都处理好后保存,然后启动游戏(如果开着游戏`F3+T`也可以)\
    ![model\_2.png](/imgs/Blockbench/model_2.png) 
	**嘿咻嘿咻~ 游戏启动中\~**
  * 如果你的贴图成功加载出来了那恭喜你成功了
  * 现在!去楼下小卖部给自己买一根5块钱以上的雪糕好好犒劳一下自己
  * ![model\_2.png](/imgs/Blockbench/model_2.png)
**物品栏模型**
  * 不难看到我们物品栏中的和手上的模型看起来非常奇怪,这个也很简单,我们点开工作区右上角的显示调整,然后点击左上角的3条横,选择`默认方块/三个点/应用于所有槽位`,保存!(当然你也可以选择自己调整)\
    ![display\_1.png](/imgs/Blockbench/display_1.png) ![display\_2.png](/imgs/Blockbench/display_2.png)
  * 和刚才一样的步骤,`F3+T`重载一下 ![model\_3.png](/imgs/Blockbench/game/model_3.png)
  * 可以看到物品栏中和手上的模型都和正常的方块一样了
  * 如果在破坏方块时他的粒子效果也是紫黑色,可以右键你需要的纹理,然后选择`作为粒子纹理`