---
authors:
  - Gu-meng
editor: Gu-meng
---
# 世界生成(WorldGen)(非常不建议在1.20使用!1.20建议使用数据包)

> 孤梦注：该文档从wudji的世界生成篇借鉴而来，后续孤梦有空可能会重写

* ## **事件声明**
`WorldgenEvents`事件组可于世界相关修改，其包含`WorldgenEvents.add`和`WorldgenEvents.remove`两个事件
  ```js
  WorldgenEvents.add((event) => {})
  WorldgenEvents.remove((event) => {})
  ```
* ## 事件方法
| 方法                                                                         | 描述                         |
| ---------------------------------------------------------------------------- | ---------------------------- |
| addLake(Consumer 湖)                                                         | 在世界生成中添加湖           |
| addOre(Consumer 矿石)                                                        | 在世界生成中添加湖           |
| getAnchors()                                                                 | 获取高度锚(`VerticalAnchor`) |
| addSpawn(BiomeFilter 群系过滤器, MobCategory 生成类型, 字符串 怪物)          | 添加怪物生成                 |
| addSpawn(MobCategory 生成类型, 字符串 怪物)                                  | 添加怪物生成                 |
| addSpawn(Consumer 属性)                                                      | 添加怪物生成                 |
| addFeatureJson(BiomeFilter 群系过滤器, JsonObject 结构)                      | 添加结构                     |
| addFeatureJson(BiomeFilter 群系过滤器, ResourceLocation ID, JsonObject 结构) | 添加结构                     |

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
| removeFeatureById(DecorationGenerationStep 生成阶段, ResourceLocation[] 结构ID)                          | 根据ID移除结构             |
| removeOres(Consumer 移除生成)                                                                            | 移除所有符合条件的矿石     |
| printFeatures()                                                                                          | 打印所有结构               |
| printFeatures(DecorationGenerationStep 生成阶段, BiomeFilter 群系过滤器)                                 | 打印所有符合条件的结构     |

* ## 常用组件
  * ### 群系过滤器
    你可以使用群系类别(Biome Categories)来作为群系过滤器
	下方示例添加矿石为例展示的群系过滤器的使用方法
	代码来自`wudji`的`Example Code`
    ```js
    WorldgenEvents.add((event) => {
    	// 获取垂直锚
    	const { anchors } = event
    
    	event.addOre((ore) => {
    		ore.id = 'kubejs:glowstone_test_lmao' // 指定当前结构的ID(可选，但推荐指定)
    		ore.biomes = {
    			not: 'minecraft:savanna' // 群系过滤器(可选)
    		}
    
    		// 添加目标示例
    		ore.addTarget('#minecraft:stone_ore_replaceables', 'minecraft:glowstone') // 将目标(#minecraft:stone_ore_replaceables标签下的方块)替换为minecraft:glowstone
    		ore.addTarget('minecraft:deepslate', 'minecraft:nether_wart_block')       // 将目标(minecraft:deepslate)替换为minecraft:nether_wart_block
    		ore.addTarget([
    			'minecraft:gravel',
    			/minecraft:(.*)_dirt/
    		], 'minecraft:tnt')       // 将目标(minecraft:gravel和所有类型的泥土)替换为minecraft:tnt
    
    		ore.count([15, 50])                      // 设置矿簇的大小(15~50中随机选择)，在此处你可以使用单个数字来生成固定数量的方块。
    			.squared()                       // 设置跨区块生成矿石
    			.triangleHeight(				 // (高度提供器)使用Triangular分布模式生成矿石(具体见上文)
    				anchors.aboveBottom(32), // 下边界锚，在世界最低高度(Y=-64)以上32格(即Y=-32)
    				anchors.absolute(96)	 // 上边界锚，固定高度(Y = 96)
    			)								 // 总的来说，矿石可能在Y = -32 ~ 96范围内生成，概率最大高度为Y = (-32 + 96) / 2 = 32
    
    		// 更多可选的参数，以下均为默认值
    		ore.size = 9                            // 最大矿簇大小
    		ore.noSurface = 0.5                     // 暴露于空气中的概率
    		ore.worldgenLayer = 'underground_ores'  // 矿石生成于世界生成中的阶段
    		ore.chance = 0							// 当该选项不为0且ore.count未被设置矿石将有1/n的概率在每个区块中生成
    	})
    
    	// 添加湖
    	// 该功能将要于原版中废弃
    	event.addLake((lake) => {
    		lake.id = 'kubejs:funny_lake' // BlockStatePredicate
    		lake.chance = 4
    		lake.fluid = 'minecraft:lava'
    		lake.barrier = 'minecraft:diamond_block'
    	})
    })
    
    WorldgenEvents.remove(event => {
    	//console.debugEnabled = true;
    
    	// 打印给定群系过滤器中的所有结构
    	event.printFeatures('', 'minecraft:plains')
    
    	event.removeOres((props) => {
    		// 类似于添加矿石，移除矿石同样支持使用世界生成阶段
    		props.worldgenLayer = 'underground_ores'
    		// 使用群系过滤
    		props.biomes = [{
    			category: 'icy',
    		}, {
    			category: 'savanna',
    		}, {
    			category: 'mesa',
    		}];
    
    		// 在上述给定的群系中移除铁矿和铜矿
    		// 注：此处无法使用标签
    		props.blocks = ['minecraft:iron_ore', 'minecraft:copper_ore']
    	})
    
    	// 通过指定ID来移除结构(第一个参数为生成步骤)
    	event.removeFeatureById('underground_ores', ['minecraft:ore_coal_upper', 'minecraft:ore_coal_lower'])
    })
    ```
	![例子](/imgs/worlgen/Example_1.png)
	![例子](/imgs/worlgen/Example_2.png)
