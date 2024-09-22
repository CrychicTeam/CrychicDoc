---
authors: ['Wudji']
---

# 10 世界生成

***

在KubeJS 6中，其世界生成相关内容相比于1.18.2基本没有修改。本节旨在以一种更加详细与清晰的方法讲述如何使用KubeJS修改世界生成。

## 一、世界生成事件

### 1、事件监听

`WorldgenEvents`事件组可于世界相关修改，其包含`WorldgenEvents.add`和`WorldgenEvents.remove`两个事件：

```js
WorldgenEvents.add(event => {
    // code here
})

WorldgenEvents.remove(event => {
    // code here
})
```

### 2、事件方法

`WorldgenEvents.add`：

| 方法                                                                         | 描述                           |
| ---------------------------------------------------------------------------- | ------------------------------ |
| addLake(Consumer 湖)                                                         | 在世界生成中添加湖             |
| addOre(Consumer 矿石)                                                        | 在世界生成中添加湖             |
| getAnchors()                                                                 | 获取高度锚（`VerticalAnchor`） |
| addSpawn(BiomeFilter 群系过滤器, MobCategory 生成类型, 字符串 怪物)          | 添加怪物生成                   |
| addSpawn(MobCategory 生成类型, 字符串 怪物)                                  | 添加怪物生成                   |
| addSpawn(Consumer 属性)                                                      | 添加怪物生成                   |
| addFeatureJson(BiomeFilter 群系过滤器, JsonObject 结构)                      | 添加结构                       |
| addFeatureJson(BiomeFilter 群系过滤器, ResourceLocation ID, JsonObject 结构) | 添加结构                       |

`WorldgenEvents.remove`

| 方法                                                                                                     | 描述                       |
| -------------------------------------------------------------------------------------------------------- | -------------------------- |
| removeSpawns(Consumer 移除生成)                                                                          | 移除怪物生成               |
| removeAllFeatures(BiomeFilter 群系过滤器)                                                                | 移除所有符合条件的结构     |
| printSpawns(MobCategory 生成类型)                                                                        | 打印所有符合条件的怪物生成 |
| printFeatures(DecorationGenerationStep 生成阶段)                                                         | 打印所有符合条件的结构     |
| printSpawns()                                                                                            | 打印所有怪物生成           |
| removeFeatureById(BiomeFilter 群系过滤器, DecorationGenerationStep 生成阶段, ResourceLocation\[] 结构ID) | 根据ID移除结构             |
| printFiltered(DecorationGenerationStep 生成阶段)                                                         | 打印符合条件的被过滤的结构 |
| printFiltered(DecorationGenerationStep 生成阶段, BiomeFilter 群系过滤器)                                 | 打印符合条件的被过滤的结构 |
| printFeaturesForType(DecorationGenerationStep 生成阶段, BiomeFilter 群系过滤器, 布尔值 是否待移除后)     | 打印指定类型的结构         |
| removeAllFeatures()                                                                                      | 移除所有结构               |
| removeAllFeatures(BiomeFilter 群系过滤器, DecorationGenerationStep 生成阶段)                             | 移除所有符合条件的结构     |
| removeAllSpawns()                                                                                        | 移除所有怪物生成           |
| removeFeatureById(DecorationGenerationStep 生成阶段, ResourceLocation\[] 结构ID)                         | 根据ID移除结构             |
| removeOres(Consumer 移除生成)                                                                            | 移除所有符合条件的矿石     |
| printFeatures()                                                                                          | 打印所有结构               |
| printFeatures(DecorationGenerationStep 生成阶段, BiomeFilter 群系过滤器)                                 | 打印所有符合条件的结构     |

MobCatrgory支持以下值：

```
"monster" | "underground_water_creature" | "misc" | "creature" | "water_ambient" | "axolotls" | "ambient" | "water_creature"
```

## 二、常用组件

### 1、群系过滤器

你可以使用群系类别（Biome Categories）来作为群系过滤器。

下方示例以添加矿石为例展示的群系过滤器的使用方法。

```
WorldgenEvents.add(event => {
	event.addOre((ore) => {
	  // 基础群系过滤器：
      ore.biomes = "minecraft:plains" 		// 仅在单一群系生成
      ore.biomes = /^minecraft:.*/			// 在所有ID与给定格式相符的群系中生成（此处为在所有原版群系中生成）
      ore.biomes = ""#minecraft:is_forest"
      
      // 过滤器可以通过与、或、非三种逻辑任意组合
      ore.biomes = {}						// 空的“与”过滤器（默认为真）
      ore.biomes = []						// 空的“或”过滤器（默认为真）
      ore.biomes = {
        not: "minecraft:ocean"				// “非”过滤器（除"minecaraft:ocean"以外的群系）
      }
      // 因为“与”过滤器可以被表达为map，所以其还可以被表达为以下形式
      ore.biomes = {
        id: "minecraft:plains",
        id: /^minecraft:.*/,			// 正则表达式
        tag: '#minecraft:is_forest'
      }
      // 你可以将以上的各种过滤器组合起来，它们会被独立计算
      // 在下面这种情况中，矿石将在符合以下条件之一的群系中生成：
      // 1、平原 
      // 2、群系类型带有“#minecraft:is_forest”的非下界或海洋类群系。
      ore.biomes = [
        "minecraft:plains",
        {
          tag: '#minecraft:is_forest'
          not: [
            '#nether',
            {
            	id: "minecraft:ocean"
            }
          ]
        },
      ]
	})
})
```

### 2、规则测试和目标方块

自1.18以后，世界生成变更为了“基于目标”的替换系统，也就是说在同一种结构中，你可以指定以特定方块来替换特定的目标方块。（例如在原版的生成矿石中，石头被普通矿石替代，而深板岩则被深板岩矿石所替代）

每个目标方块都有一个“规则测试”（RuleTest）作为输入（用于检测目标方块的方块状态是否应被替换）并生成一个特定的输出方块状态。在KubeJS脚本层面上，这两个概念都被表达为相同的类：_BlockStatePredicate_。

就格式上来说，BlockStatePredicate 与 群系过滤器 非常相似，即它们都能使用AND或者OR合并，并可用于匹配以下三种类型的条件：

1. 方块（该类型被解析为字符串，使用形如 `"minecraft:stone"`来匹配石头)
2. 方块状态（该类型被解析为后加数组形式的方块状态的方块ID字符串，使用形如`"minecraft:furnace[lit=true]"`来匹配燃烧中的熔炉方块。你可以使用F3界面来获得准星的方块状态，或者使用调试棒来获得方块状态可能的值。）
3. 方块标签（该类型被解析为**类似**标签的形式，使用形如`"#minecraft:base_stone_overworld"`来匹配主世界中生成在地下的所有类型的石头。**注意使用方块标签而不是物品标签**，部分情况下他们可能不同，你可以在F3界面获得方块标签。）

注：

* 你可以在方块过滤器中使用正则表达式，`/^mekanism:.*_ore$/`将匹配所有通用机械中以`_ore`结尾的方块。注意，这种写法不会匹配方块状态！
* 你可以使用JavaScript Object来提供规则测试（它将会像原版解析JSON或NBT那样被解析）。例如，你可以通过这种方式设置匹配生效的概率。

### 3、高度提供器

另一个有些复杂的系统可能是“高度提供器”（Height Providers）系统，其被用于判定指定方块可能生成的Y坐标值和生成频率。与该系统同时使用的还有“垂直锚”（Vertical Anchors）概念，其可以用于获取相对于某锚点的相对高度（例如相对于世界最高点或最低点）。

在KubeJS中，该系统被简化以使其更容易在脚本开发中被使用：你可以使用`AddOreProperties`中的`uniformHeight` 和 `traingleHeight`方法来使用两种最常见的矿石生成逻辑（Uniform：在两个锚点间的任意位置有等大的概率生成矿石。Triangle：生成概率从两个锚点的中点向两锚点递减）。垂直锚概念也被简化了，你可以使用`AddOreProperties`中的辅助方法`aboveBottom / belowTop`（在世界最低点以上 / 在世界最高点以下）或新版本KubeJS中的`VerticalAnchor`Wrapper Class；此外，你还可以以数字形式直接指定绝对高度。

### 4、生成阶段：DecorationGenerationStep

在原版游戏中，世界生成阶段按顺序如下：

1. `raw_generation`
2. `lakes`
3. `local_modifications`
4. `underground_structures`
5. `surface_structures`
6. `strongholds`
7. `underground_ores`
8. `underground_decoration`
9. `fluid_springs`
10. `vegetal_decoration`
11. `top_layer_modification`

## 三、属性设置

### 1、`AddOreProperties`：矿石添加属性设置

| 方法                                                       | 描述                       | 补充说明                    |
| ---------------------------------------------------------- | -------------------------- | --------------------------- |
| aboveBottom(整形 距离)                                     | 设置距离世界底部的高度     | -                           |
| size(s: any)                                               | 设置最大矿簇大小           | -                           |
| bottom()                                                   | 设置生成在世界底部         | 注：未经测试                |
| belowTop(整形 距离)                                        | 设置距离世界顶部的高度     |                             |
| count(整形 数字)                                           | 设置矿簇数量               | 接受单个数字                |
| addTarget(RuleTest 规则测试, BlockStatePredicate 目标状态) | 添加目标方块               | 使用方法见下                |
| squared()                                                  | 设置为方形布局             | -                           |
| triangleHeight(整形 最小值, 整形 最大值)                   | 设置traingleHeight生成类型 | 见`高度提供器`              |
| triangleHeight(垂直锚 位置1, 垂直锚 位置2)                 | 设置traingleHeight生成类型 | 见`高度提供器`              |
| top()                                                      | 设置生成在世界顶部         | 注：未经测试                |
| count(IntProvider 整形提供器)                              | 设置矿簇数量               | 接受区间（形如\[15,20]）    |
| count(整形 最小值, 整形 最大值)                            | 设置矿簇数量范围           | -                           |
| chance(c: any)                                             | 设置生成几率               | 默认为0，详细信息见示例脚本 |
| uniformHeight(垂直锚 位置1, 垂直锚 位置2)                  | 设置uniformHeight生成类型  | 见`高度提供器`              |
| uniformHeight(整形 最小值, 整形 最大值)                    | 设置uniformHeight生成类型  | 见`高度提供器`              |
| 属性 "height": HeightRangePlacement                        | 设置生成高度范围           | 见`高度提供器`              |
| 属性 "retrogen"                                            | 是否在已生成区块中重复生成 | -                           |
| 属性 "count": IntProvider                                  | 设置矿簇数量               | -                           |
| 属性 "worldgenLayer": DecorationGenerationStep             | 设置所属生成步骤           | -                           |
| 属性 "chance": any                                         | 设置几率                   | 默认为0，详细信息见示例脚本 |
| 属性 "squared": 布尔值                                     | 设置为平方形布局           | -                           |
| 属性 "noSurface": 浮点型                                   | 设置暴露于空气中的概率     | 0\~1                        |
| 属性 "biomes": BiomeFilter                                 | 设置群系过滤器             | -                           |
| 属性 "size": any                                           | 设置矿簇最大大小           | -                           |
| 属性 "id": ResourceLocation                                | 设置ID                     | -                           |
| 属性 "targets": List\<OreConfiguration$TargetBlockState>   | 设置目标列表               | -                           |

### 2、`AddSpawnProperties`：实体添加属性设置

| 方法                                | 描述                        |
| ----------------------------------- | --------------------------- |
| setEntity(s: string)                | 设置实体ID                  |
| setCategory(s: string)              | 设置实体类别（MobCatrgory） |
| 属性 "minCount": any                | 设置实体最小数量            |
| 属性 "biomes": Internal.BiomeFilter | 设置群系过滤器              |
| 属性 "maxCount": any                | 设置最大数量                |
| 属性 "weight": any                  | 设置生成权重                |

### 3、`RemoveOresProperties`：矿石移除属性

| 方法                                           | 描述             |
| ---------------------------------------------- | ---------------- |
| 属性 "worldgenLayer": DecorationGenerationStep | 匹配所属生成步骤 |
| 属性 "biomes": BiomeFilter                     | 匹配群系过滤器   |
| 属性 "blocks": BlockStatePredicate             | 匹配方块状态谓词 |

### 4、`RemoveSpawnsProperties`：实体生成移除属性

| 方法                       | 描述               |
| -------------------------- | ------------------ |
| 属性 "mobs": MobFilter     | 匹配生物实体过滤器 |
| 属性 "biomes": BiomeFilter | 匹配生物群系过滤器 |

## 四、示例脚本

```
WorldgenEvents.add(event => {
	// 获取垂直锚
	const {anchors} = event

	event.addOre((ore) => {
		ore.id = "kubejs:glowstone_test_lmao" // 指定当前结构的ID（可选，但推荐指定）
		ore.biomes = {
			not: 'minecraft:savanna' // 群系过滤器（可选）
		}

		// 添加目标示例
		ore.addTarget('#minecraft:stone_ore_replaceables', 'minecraft:glowstone') // 将目标（#minecraft:stone_ore_replaceables标签下的方块）替换为minecraft:glowstone
		ore.addTarget('minecraft:deepslate', 'minecraft:nether_wart_block')       // 将目标（minecraft:deepslate）替换为minecraft:nether_wart_block
		ore.addTarget([
			'minecraft:gravel',
			/minecraft:(.*)_dirt/
		], 'minecraft:tnt')       // 将目标（minecraft:gravel和所有类型的泥土）替换为minecraft:tnt

		ore.count([15, 50])                      // 设置矿簇的大小（15~50中随机选择），在此处你可以使用单个数字来生成固定数量的方块。
				.squared()                       // 设置跨区块生成矿石
				.triangleHeight(				 // （高度提供器）使用Triangular分布模式生成矿石（具体见上文）
						anchors.aboveBottom(32), // 下边界锚，在世界最低高度（Y=-64）以上32格（即Y=-32）
						anchors.absolute(96)	 // 上边界锚，固定高度（Y = 96）
				)								 // 总的来说，矿石可能在Y = -32 ~ 96范围内生成，概率最大高度为Y = (-32 + 96) / 2 = 32
	    
        // 更多可选的参数，以下均为默认值
        ore.size = 9                            // 最大矿簇大小
        ore.noSurface = 0.5                     // 暴露于空气中的概率
        ore.worldgenLayer = "underground_ores"  // 矿石生成于世界生成中的阶段
      	ore.chance = 0							// 当该选项不为0且ore.count未被设置矿石将有1/n的概率在每个区块中生成
    })

	// 添加湖
	// 该功能将要于原版中废弃
	event.addLake((lake) => {
		lake.id = "kubejs:funny_lake" // BlockStatePredicate
		lake.chance = 4
		lake.fluid = "minecraft:lava"
		lake.barrier = "minecraft:diamond_block"
	})
})

WorldgenEvents.remove(event => {
	console.info("HELP")
	//console.debugEnabled = true;

	// 打印给定群系过滤器中的所有结构
	event.printFeatures('', 'minecraft:plains')

	event.removeOres((props) => {
		// 类似于添加矿石，移除矿石同样支持使用世界生成阶段
		props.worldgenLayer = 'underground_ores'
		// 使用群系过滤
		props.biomes = [{
			category: "icy",
		}, {
			category: "savanna",
		}, {
			category: "mesa",
		}];

		// 在上述给定的群系中移除铁矿和铜矿
		// 注：此处无法使用标签
		props.blocks = ["minecraft:iron_ore", "minecraft:copper_ore"]
	})

	// 通过指定ID来移除结构（第一个参数为生成步骤）
	event.removeFeatureById('underground_ores', ['minecraft:ore_coal_upper', 'minecraft:ore_coal_lower'])
})
```
