# 添加自定义物品
kubejs可以在startup_scripts文件夹内创建去创建物品，注意所有的添加自定义的操作都是无法热加载的，写完之后需要重启游戏后才会加载进游戏内
## 基础写法
```js
StartupEvents.registry("item",event=>{
    event.create("meng:my_item","basic")
})
```
kjs创建物品是非常简单的，只需要一行就可以解决

`create`里的第一个参数为物品id,这里如果写成"xxx:xxx"，冒号前面的是你的命名空间(类似于模组id),后面的才是你的物品id,如果你只是写成"xxx"并不加冒号也是可以的，kjs为默认为你创建在kubejs下,最后呈现形式就是"kubejs:xxx"

`create`里的第二个参数为物品类型，在下面会给大家列举出来他的物品类型和描述

## 物品类型
|      类型参数       |   作用   |         描述         |                 示例                  |
| :-----------------: | :------: | :------------------: | :-----------------------------------: |
|       `basic`       | 基础物品 |          无          |                   -                   |
|       `basic`       | 基础物品 |          -           |    [添加食物](./item-type/food)    |
|    `music_disc`     |   唱片   | 可以在唱片机里播放的 | [添加唱片](./item-type/chang-pian) |
| `smithing_template` | 锻造模板 |          无          |                   -                   |
|      `helmet`       |   头盔   |          无          |             [添加护甲]()              |
|    `chestplate`     |   胸甲   |          无          |                 同上                  |
|     `leggings`      |   护腿   |          无          |                 同上                  |
|       `boots`       |   鞋子   |          无          |                 同上                  |
|        `axe`        |   斧子   |          无          |             [添加工具](./item-type/tools)              |
|        `hoe`        |   锄头   |          无          |                 同上                  |
|      `pickaxe`      |   镐子   |          无          |                 同上                  |
|      `shovel`       |   铲子   |          无          |                 同上                  |
|       `sword`       |    剑    |          无          |                 同上                  |
|      `shears`       |   剪刀   |          无          |             [添加剪刀]()              |


## 通用方法参数
下面提供的方法为所有物品类型都可以调用到的参数,针对于护甲、工具等物品会在对应的章节提供专有的方法
### 常用方法
|                             方法调用                              | 传入参数 |                   用处                    | 返回类型 |
| :---------------------------------------------------------------: | :------: | :---------------------------------------: | :------: |
|                          maxDamage(int)                           |    -\>    |            设置物品的最大耐久             |   this   |
|                   food(Consumer\<FoodBuilder\>)                    |    -\>    |   [设置物品为食物](./item-type/food)   |   this   |
|                         maxStackSize(int)                         |    -\>    | 设置物品的最大堆叠,虽然能超过64但是不建议 |   this   |
|                        fireResistant(bool)                        |    -\>    |             设置物品是否防火              |   this   |
|                        displayName(string)                        |    -\>    |  设置在没有配置lang文件时直接显示的名称   |   this   |
|                       tag(ResourceLocation)                       |    -\>    |             设置物品的tag标签             |   this   |
|                          texture(string)                          |    -\>    |             设置物品材质路径              |   this   |
|                           unstackable()                           |    -     |            设置物品为不可堆叠             |   this   |
|                           burnTime(int)                           |    -     | 设置物品的可燃烧tick(默认为0则是不可燃烧) |   this   |
| modifyAttribute(ResourceLocation,string,double,AttributeModifier) |    ~     |              物品属性修饰符               |   this   |
|                      createItemProperties()                       |    -     |              创建为物品属性               |   Item   |


### 物品使用时调用的方法
|                    方法调用                    | 传入参数 |                    用处                     | 返回类型 |
| :--------------------------------------------: | :------: | :-----------------------------------------: | :------: |
|          use(ItemBuilder$UseCallback)          |    -\>    |            物品开始被玩家使用时             |   this   |
| releaseUsing(ItemBuilder$ReleaseUsingCallback) |    -\>    | 当玩家没有使用完物品但中途松开鼠标右键时(?) |   this   |
|  finishUsing(ItemBuilder$FinishUsingCallback)  |    -\>    |             当玩家使用完物品时              |   this   |
|     useDuration(ToIntFunction\<ItemStack\>)     |    ~     |             关于物品的使用时间?             |   this   |
|             useAnimation(UseAnim)              |    ~     |             物品在使用时的动画              |   this   |

### 关于渲染的方法
|               方法调用               | 传入参数 |                 用处                 | 返回类型 |
| :----------------------------------: | :------: | :----------------------------------: | :------: |
|        modelJson(JsonObject)         |    -\>    | 设置模型的json(虽然但是不建议这么干) |   this   |
|         parentModel(string)          |    ~     |          设置模型的父模型?           |   this   |
|       textureJson(JsonObject)        |    -\>    |        设置物品的材质json ??         |   this   |
| barWidth(ToIntFunction\<ItemStack\>)  |    ~     |             关于耐久条?              |   this   |
| barColor(Function\<ItemStack\>,Color) |    ~     |           关于耐久条颜色?            |   this   |
|              glow(bool)              |    -\>    |        设置物品是否有附魔光效        |   this   |
|          tooltip(Component)          |    -\>    |             物品提示内容             |   this   |

### 其他方法
|                       方法调用                        |              传入参数              |        用处        |    返回类型     |
| :---------------------------------------------------: | :--------------------------------: | :----------------: | :-------------: |
|        hurtEnemy(Predicate\<HurtEnemyContext\>)        |                 -\>                 | 物品攻击实体生物时 |      this       |
|                    rarity(Rarity)                     | [稀有度](../../ti-wai-hua/xi-you-du) |   设置物品稀有度   |      this       |
|                   getRegistryType()                   |                 -                  |    获取注册类型    | RegistryInfo<T\> |
|        generateAssetJsons(AssetJsonGenerator)         |                 ?                  |         ?          |        -        |
|         generateDataJsons(DataJsonGenerator)          |                 ?                  |         ?          |        -        |
|                  name(NameCallback)                   |                 ~                  | 动态设置物品名字?  |      this       |
|              color(int,ItemTintFunction)              |                 ?                  |         ?          |      this       |
|                color(ItemTintFunction)                |                 ?                  |         ?          |      this       |
|                 transformObject(Item)                 |                 ~                  |         ~          |      this       |
| subtypes(Function\<ItemStack,Collection\<ItemStack\>\>) |                 ~                  |         ?          |      this       |
|            containerItem(ResourceLocation)            |                 ~                  | 设置物品为容器???  |      this       |

## 简单的注册物品轮子

**注:以下内容根据个人习惯选择性使用和更改**

```js
StartupEvents.registry("item", (event) => {
	// ModID声明如果选择不更改ModID(默认即"kubejs")直接把ModID这个变量取消
	const MODID = "meng:"

	/* 
	* 定义物品
	* 在添加下一个物品时要记得在[]后加上逗号
	* 并且一定要严格按照格式进行
	* [物品id, 稀有度类型, 是否有附魔光效]
	*/
	let itemRegisters = [
		["example_item", "common", false],
	]
	itemRegisters.forEach(([name, rarity, glow]) => {
		event.create(MODID + name) // 声明id
			.rarity(rarity) // 稀有度
			.glow(glow) // 是否有附魔光效
			.tag(MODID + "items") // 添加物品tag(可选)
	})
})
```