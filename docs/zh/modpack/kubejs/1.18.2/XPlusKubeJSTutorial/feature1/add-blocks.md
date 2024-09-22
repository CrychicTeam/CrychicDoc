---
authors: ['Wudji']
---

# 7 添加方块

***

以下是格式示例，你应将其放在startup脚本文件夹内。

```
// 此写法被弃用
onEvent('block.registry', event => {
  event.create('test_block', block => {
  	  block.material('glass')//设置方块材质
      block.hardness(0.5)//设置方块硬度
      block.displayName('Test Block')
  })
})

onEvent('block.registry', event => {
  event.create('test_block')
  	   .material('glass')
       .hardness(0.5)
       .displayName('Test Block')
})

//你还可以把他们在一行中连着写
onEvent('block.registry', event => {
	event.create('example_block').material('wood').hardness(1.0).displayName('Example Block')
})
```

该方块的纹理必须被放于`kubejs/assets/kubejs/textures/block/[方块名]`，如`kubejs/assets/kubejs/textures/block/test_block.png`

如果你想要使用自定义的模型，你可以使用Blockbench制作并把它放于 `kubejs/assets/kubejs/models/block/[方块名].json` , 如 `kubejs/assets/kubejs/models/block/test_block.json`.

其他支持的方法：

* material('material')// 设置方块的“质地”，可用的参数见下
* type('basic') // 设置方块类型，可用的参数见下
* hardness(float) // 设置方块硬度，值需要大于 >= 0.0；若要使方块无法被破坏，详见下方。
* resistance(float) // 爆炸抗性，值需要大于 >= 0.0
* unbreakable() // 设置方块无法被破坏
* lightLevel(等级) // 方块光照等级，值属于\[0 , 1]（需要为整形）
* harvestTool('工具', 工具等级) // 可以破坏该方块的工具。工具参数可以为 pickaxe, axe, hoe, shovel，工具等级需 >= 0
* opaque(布尔值) // 方块是否透明
*   fullBlock(布尔值) // 设置是否为完整方块，与notSolid()共同使用可以实现如下图所示效果（即镂空方块）

    _图片被mcbbs吃掉了_
* requiresTool(布尔值) // 是否需要对应工具才能掉落
* renderType('类型') // 设置方块的渲染类型，可用的选项有solid(实心), cutout(镂刻), translucent(半透明), 其中 cutout 可用于类似于玻璃的方块，translucent 可用于类似于染色玻璃的方块
* color(tintindex, color)// 修改方块颜色，具体可参考[此处介绍](https://www.mcbbs.net/forum.php?mod=redirect\&goto=findpost\&ptid=1112082\&pid=20118507)
* texture('纹理路径')// 设置方块纹理
* texture('方向', '纹理路径')// 设置方块纹理(可以单独设置每个朝向的纹理)
* model('模型路径')// 设置方块使用的模型
* noItem()// 添加方块而不添加方块对应的物品
* box(x0, y0, z0, x1, y1, z1, true) // 设置方块碰撞箱(0\~16)，默认值为(0,0,0,16,16,16, true)
*   box(x0, y0, z0, x1, y1, z1, false) // 设置方块碰撞箱(0\~1)，默认值为(0,0,0,1,1,1, false)

    下图即为使用box方法设置的自定义方块

    ![](https://m1.miaomc.cn/uploads/20220130\_1ec4430bb7573.png)
* noCollision() // 设置方块是否有碰撞箱
* notSolid() // 见上
* waterlogged() // 是否可以被充水
* noDrops() // 被破坏是否掉落自身
* slipperiness(浮点型) // 设置打滑程度
* speedFactor(浮点型) // 玩家在上方移动速度倍率
* jumpFactor(浮点型)
*   randomTick(randomTickEvent => {}) // 当方块被随机刻选中时发生的事件

    支持以下函数：

    BlockContainerJS block

    Random random

    下面是一个简单的例子

    ```
    //当test_block_randomTickEvent被随机刻选中时将其换为minecraft:dirt
    onEvent('block.registry', event => {
      event.create('test_block_randomTickEvent').material('glass').hardness(1.0).displayName('Test Block randomTickEvent').randomTick(randomTickEvent => {
            randomTickEvent.block.set('minecraft:dirt')// BlockContainerJS
          })

    })
    ```

    注：BlockContainerJS会在后面讲到（大概在15或16章，尚未更新）
* item([itemBuilder](https://mods.latvian.dev/books/kubejs/page/custom-items) => {}) // 掉落物设置
* setLootTableJson(json) // 设置战利品表（原版json格式）
* setBlockstateJson(json) // 设置方块状态（原版json格式）
* setModelJson(json) // 设置模型
* noValidSpawns(布尔值) // 是否可以生成怪物
* suffocating(布尔值) // 是否可以使玩家窒息
* viewBlocking(布尔值)
* redstoneConductor(布尔值) // 是否传导红石信号
* transparent(布尔值) // 是否透明
* defaultCutout() // 用来制作类似于玻璃的方块的方法的集合
* defaultTranslucent() // 与defaultCutout()类似，但是使用透明层
* tag('forge:exampletag') // 添加block tag
* tagBlockAndItem('forge:exampletag') // 为方块和物品添加tag

### 可用的方块类型(type() 方法)

* basic
* slab
* stairs
* fence
* fence\_gate
* wall
* wooden\_pressure\_plate
* stone\_pressure\_plate
* wooden\_button
* stone\_button

可用的材质的值( material('material') ) - 该参数会改变方块被破坏和玩家行走在上面的声音并且预设一些属性(如破坏用的工具等)：

| 材质          |
| ----------- |
| air         |
| wood        |
| rock        |
| iron        |
| organic     |
| earth       |
| water       |
| lava        |
| leaves      |
| plants      |
| sponge      |
| wool        |
| sand        |
| glass       |
| tnt         |
| coral       |
| ice         |
| snow        |
| clay        |
| groud       |
| dragon\_egg |
| portal      |
| cake        |
| web         |
| slime       |
| honey       |
| berry\_bush |
| lantern     |
