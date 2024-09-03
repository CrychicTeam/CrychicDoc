# 方块注册
kubejs可以在startup_scripts文件夹内创建去创建方块，注意所有的添加自定义的操作都是无法热加载的，写完之后需要重启游戏后才会加载进游戏内
## 基础写法
在kubejs当中注册方块是一件很容易的事情,它只需要下面这一行代码就可以注册好方块
```js
StartupEvents.registry("block", (event) => {
    //event.create(方块id, 方块类型)
    event.create("meng:my_block", "basic")
})
```
上面的代码中我们选择的是方块注册，将方块id设置为`"meng:my_block"`，这里前面的`meng`是[命名空间](/ti-wai-hua/ming-ming-kong-jian.md),主要关系到我们之后去添加[材质贴图](/cai-zhi/tie-tu.md)和[本地化(lang文件)](/cai-zhi/lang.md)的路径

## 方块类型
我们的方块类型设置的为`basic`也就是默认的、基本的方块类型，在kubejs中已经给我们提供了不少的方块类型，如下
|       类型       |            描述            |  示例  |
| :--------------: | :------------------------: | :----: |
|     `basic`      |          基础方块          | 待更新 |
|     `carpet`     |            地毯            | 待更新 |
|      `crop`      |           农作物           | 待更新 |
|     `fence`      |            栅栏            | 待更新 |
|   `fence_gate`   |           栅栏门           | 待更新 |
| `pressure_plate` |           压力板           | 待更新 |
|     `button`     |            按钮            | 待更新 |
|      `slab`      |            台阶            | 待更新 |
|     `stairs`     |            楼梯            | 待更新 |
|      `wall`      |             墙             | 待更新 |
|    `cardinal`    | 朝向方块(类似讲台、熔炉等) | 待更新 |
|    `detector`    |         检测方块?          | 待更新 |
|    `falling`     |      下落方块(类沙子)      | 待更新 |

在kubejs中，你并不用为方块单独去注册物品，kubejs已经帮你把方块物品注册好了，所以你可以直接获取到方块的物品，且物品id和方块id是一样的，在准备材质时，只需要准备方块的材质就可以了，无需单独为方块物品准备材质
## 通用方法参数

### 常用
|                       方法                        |                    参数                     |                      描述                      | 返回类型 |
| :-----------------------------------------------: | :-----------------------------------------: | :--------------------------------------------: | :------: |
|   `randomTick(Consumer\<RandomTickCallbackJS>)`   |                      -                      |                  方块随机tick                  |   this   |
|        `lootTable(Consumer\<LootBuilder>)`        |                      -                      |                 方块战利品构建                 |    ~     |
|           `tagBlock(ResourceLocation)`            | [常用参数](#chang-yong-de-tagblock-can-shu) | 设置方块的标签(如可被挖掘的工具类型和挖掘等级) |   this   |
|            `tagItem(ResourceLocation)`            |                     ->                      |               设置方块物品的tag                |   this   |
|                    `noItem()`                     |                      -                      |                不生成对应的物品                |   this   |
|             `displayName(Component)`              |                     ->                      |                  设置显示名字                  |   this   |
|                `lightLevel(float)`                |                     ->                      |                  设置光照等级                  |   this   |
|     `blockEntity(Consumer\<BlockEntityInfo>)`     |                      -                      |                 创建为方块实体                 |   this   |
| `rightClick(Consumer\<BlockRightClickedEventJS>)` |                      -                      |                  方块右键事件                  |   this   |
|                    `noDrops()`                    |                      -                      |                该方块的无掉落物                |   this   |
|                 `hardness(float)`                 |                     ->                      |             设置方块硬度(默认1.5)              |   this   |
|               `speedFactor(float)`                |                     ->                      |         设置方块速度(高于1会速度很快)          |   this   |
|                `jumpFactor(float)`                |                     ->                      |                设置方块跳跃高度                |   this   |
|               `noValidSpawns(bool)`               |                     ->                      |             该方块上是否会生成生物             |   this   |
|                   `notSolid()`                    |                      -                      |            设置方块像沙子一样会下落            |   this   |
|                  `unbreakable()`                  |                      -                      |                 使方块无法破坏                 |   this   |
|                `resistance(float)`                |                     ->                      |          设置方块的耐爆炸性(默认为3)           |   this   |
|                 `requiresTool()`                  |                      -                      |       设置方块需要对应的工具挖掘才会掉落       |   this   |

#### 常用的tagBlock()参数
* 挖掘需要的对应工具

> 如果方块没有requiresTool()，则使用对应工具可以加快挖掘速度
> 
> 如果方块有requiresTool()，则需使用对应工具才可掉落

|              参数              | 对应工具类型 |
| :----------------------------: | :----------: |
|  `"minecraft:mineable/sword"`  |      剑      |
| `"minecraft:mineable/pickaxe"` |      镐      |
|   `"minecraft:mineable/axe"`   |      斧      |
| `"minecraft:mineable/shovel"`  |      锹      |
|   `"minecraft:mineable/hoe"`   |      锄      |

* 挖掘需要的工具品质

|               参数               |                需要的工具品质                |
| :------------------------------: | :------------------------------------------: |
| `"minecraft:needs_wooden_tool"`  |                      木                      |
|  `"minecraft:needs_stone_tool"`  |                      石                      |
|  `"minecraft:needs_iron_tool"`   |                      铁                      |
| `"minecraft:needs_golden_tool"`  |                      金                      |
| `"minecraft:needs_diamond_tool"` |                     钻石                     |
|  `"forge:needs_netherite_tool"`  | 下界合金(由`Forge`提供的标签,fabric版本未知) |

### 关于渲染相关
|                            方法                             |                      参数                      |                描述                | 返回类型 |
| :---------------------------------------------------------: | :--------------------------------------------: | :--------------------------------: | :------: |
|    `box(double, double, double, double, double, double)`    |                       ~                        |                 ~                  |   this   |
| `box(double, double, double, double, double, double, bool)` |                       ~                        |                 ~                  |   this   |
|                      `defaultCutout()`                      |                       -                        |                 ~                  |   this   |
|                   `defaultTranslucent()`                    |                       -                        |                 ~                  |   this   |
|                     `transparent(bool)`                     |                       ->                       |            方块是否透明            |   this   |
|                       `noCollision()`                       |                       -                        |          设置方块无碰撞箱          |   this   |
|                    `renderType(string)`                     | "cutout"/"cutout_mipped"/"translucent"/"basic" |    选择渲染类型，一共就前面四种    |   this   |
|                       `model(string)`                       |                       ->                       |           模型的位置路径           |   this   |
|                    `viewBlocking(bool)`                     |                       ~                        |                 ~                  |   this   |
|                      `fullBlock(bool)`                      |                       ->                       |       设置方块是否为完整的块       |   this   |
|                       `opaque(bool)`                        |                       ->                       | 设置方块是否透明(光线是否能够穿过) |   this   |
|                     `material(string)`                      |                       ->                       |                 ?                  |   this   |

### 声音类型
|          方法          | 参数  |    描述    | 返回类型 |
| :--------------------: | :---: | :--------: | :------: |
|   `glassSoundType()`   |   -   |  玻璃音效  |   this   |
|   `grassSoundType()`   |   -   |  草地音效  |   this   |
|   `sandSoundType()`    |   -   |  沙子音效  |   this   |
|   `stoneSoundType()`   |   -   |  石头音效  |   this   |
|  `gravelSoundType()`   |   -   |  沙砾音效  |   this   |
|   `cropSoundType()`    |   -   |  作物音效  |   this   |
|   `woodSoundType()`    |   -   |  木头音效  |   this   |
|    `noSoundType()`     |   -   |  没有音效  |   this   |
| `soundType(SoundType)` |  ->   | 自定义音效 |   this   |

### 其他
|                               方法                               | 参数  |                 描述                 |         返回类型         |
| :--------------------------------------------------------------: | :---: | :----------------------------------: | :----------------------: |
|       `mirrorState(Consumer\<BlockStateMirrorCallbackJS>)`       |   ~   |                  ~                   |           this           |
|       `rotateState(Consumer\<BlockStateRotateCallbackJS>)`       |   ~   |                  ~                   |           this           |
|                       `bounciness(float)`                        |   ~   |                  ~                   |           this           |
|       `canBeReplaced(Predicate\<CanBeReplacedCallbackJS>)`       |   ~   |                  ~                   |           this           |
| `placementState(Consumer\<BlockStateModifyPlacementCallbackJS>)` |   ~   |             放置方块事件             |           this           |
|      `steppedOn(Consumer\<EntitySteppedOnBlockCallbackJS>)`      |   ~   |             方块踩踏事件             |           this           |
|  `afterFallenOn(Consumer\<AfterEntityFallenOnBlockCallbackJS>)`  |   -   |                  ~                   |           this           |
|       `fallenOn(Consumer\<EntityFallenOnBlockCallbackJS>)`       |   -   |            方块下落时事件            |           this           |
|                   `tagBoth(ResourceLocation)`                    |   ~   |                  ~                   |           this           |
|      `defaultState(Consumer\<BlockStateModifyCallbackJS>)`       |   -   |            方块的默认状态            |           this           |
|          `exploded(Consumer\<BlockExplodedCallbackJS>)`          |   ~   | 方块爆炸后的事件(此时方块已经被摧毁) |           this           |
|                       `canBeWaterlogged()`                       |   -   |           方块是否被水淹没           |           bool           |
|                 `textureSide(Direction,string)`                  |   ~   |                  ~                   |           this           |
|                       `mapColor(MapColor)`                       |  ->   |        设置方块在地图上的颜色        |           this           |
|                    `redstoneConductor(bool)`                     |  ->   |        设置方块是否是红石导体        |           this           |
|                       `textureAll(string)`                       |   ~   |                  ~                   |           this           |
|                       `suffocating(bool)`                        |  ->   |       设置方块是否会让生物窒息       |           this           |
|                      `slipperiness(float)`                       |   ~   |                  ~                   |           this           |
|                     `transformObject(Block)`                     |   ~   |                  ~                   |          Block           |
|                     `texture(string,string)`                     |   ~   |                  ~                   |           this           |
|                   `property(BlockProperties)`                    |  ->   |             设置方块属性             |           this           |
|               `item(Consumer\<BlockItemBuilder>)`                |   -   |            方块的物品构建            |           this           |
|                     `tag(ResourceLocation)`                      |   ~   |                  ~                   |           this           |
|                  `color(int,BlockTintFunction)`                  |   ~   |                  ~                   |           this           |
|                    `color(BlockTintFunction)`                    |   ~   |                  ~                   |           this           |
|                       `createProperties()`                       |   -   |                  ?                   | BlockBehaviourProperties |
|              `generateDataJsons(DataJsonGenerator)`              |   ~   |                  ~                   |            -             |
|             `generateAssetJsons(AssetJsonGenerator)`             |   ~   |                  ~                   |            -             |
|                       `getRegistryType()`                        |   -   |                  ~                   |      RegistryInfo<>      |
|                         `waterlogged()`                          |   -   |                  ~                   |           this           |
|                `instrument(NoteBlockInstrument)`                 |   ~   |                  ~                   |           this           |
|                   `createAdditionalObjects()`                    |   -   |                  ~                   |            -             |


### 简单的注册方块轮子

**注:以下内容根据个人习惯选择性使用和更改**

```js
StartupEvents.registry("block", (event) => {
	// ModID声明如果选择不更改ModID(默认即"kubejs")直接把ModID这个变量取消
	const MODID = "meng:"

	// 工具类型
	const toolType = {
		sword: "minecraft:mineable/sword",
		pickaxe: "minecraft:mineable/pickaxe",
		axe: "minecraft:mineable/axe",
		shovel: "minecraft:mineable/shovel",
		hoe: "minecraft:mineable/hoe"
	}

	// 挖掘等级
	const miningLevel = {
		wooden: "minecraft:needs_wooden_tool",
		stone: "minecraft:needs_stone_tool",
		iron: "minecraft:needs_iron_tool",
		gold: "minecraft:needs_gold_tool",
		diamond: "minecraft:needs_diamond_tool",
		nether: "forge:needs_netherite_tool"
	}

	/* 
	* 定义方块
	* 在添加下一个方块时要记得在[]后加上逗号
	* 并且一定要严格按照格式进行
	* [方块id, 声音类型, 硬度和爆炸抗性(这里我选择让他们共用一个数值), 工具类型, 挖掘等级]
	*/
	let blockRegisters = [
		// 示例
		["example_block", "stone", 3, "pickaxe", "wooden"],
	]
	blockRegisters.forEach(([name, soundType, hardness, tool, level]) => {
		event.create(MODID + name) // 声明方块id
			.soundType(soundType) // 声音类型
			.hardness(hardness) // 硬度
			.resistance(hardness) // 方块的耐爆炸性
			.tagBlock(toolType[tool]) // 工具类型
			.tagBlock(miningLevel[level])  // 挖掘等级
			// .tagItem(MODID + "items") // 添加物品tag(可选)
			// .tagItem(MODID + "blocks") // 添加物品tag(可选)
			.requiresTool(true) // 必须要工具挖掘
	})
})
```