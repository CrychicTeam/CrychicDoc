---
progress: 100
state: preliminary
---

# 实用工具

>[!TIP] 介绍
>KubeJS提供的一系列全局函数，可通过Utils.调用。

>[!WARNING] 提示
> 部分示例参考价值较低，需要更好的示例。

## copy

- 该函数尝试复制给定的对象，如果不可复制则返回对象本身。
- 用途待补充。

```js [KubeJS]
let object = {name: "Name"};
let copyObj = Utils.copy(object);
```

## emptyList

- 该函数返回一个Immutable List对象，该列表不可变，无法插入、删除元素。
- 用途待补充。

```js [KubeJS]
let immutableList = Utils.emptyList();
```

## emptyMap

- 该函数返回一个Immutable Map对象，该Map不可变，不可插入、删除键值对。
- 用途待补充。

```js [KubeJS]
let immutableMap = Utils.emptyMap();
```

## expiringLazy

- 该函数接受一个[Internal.Supplier_\<T\>](../miscellaneous/Supplier.md)，一个毫秒数number 返回一个惰性值[Internal.Lazy\<T\>](../miscellaneous/Lazy.md)。
- 当内部的值被创建后，经过number毫秒该值会被丢弃，下次使用会重新创建该值。

```js [KubeJS]
let lazyTime = Utils.expiringLazy(() => Utils.getSystemTime(), 1000);
```

- 在这里传递了一个[Internal.Supplier_\<number\>](../miscellaneous/Supplier.md) 当它被创建时会返回一个代表系统时间(总毫秒数)的数字number，并且在该number创建1s后过期，当过期后再次调用值会被重新创建，未过期时调用始终返回相同的值。

## findCreativeTab

- 这个函数根据传入的ResourceLocation或string返回对应的创造模式物品栏。

```js [KubeJS]
let creativeTab = Utils.findCreativeTab("minecraft:combat");
```

- 这里获取了原版的“战斗用品”创造模式物品栏。

## getRandom

- 该函数始终返回同一个[Random](../miscellaneous/Random.md)对象。
- 可以使用该对象来生成随机数。

```js [KubeJS]
let random = Utils.getRandom();
```

## getRegistry

- 该函数根据传入的ResourceLocation或string返回对应的注册表信息[Internal.RegistryInfo\<T\>](../miscellaneous/RegistryInfo.md)对象。

```js [KubeJS]
let registry = Utils.getRegistry("minecraft:block");
```

## getRegistryIds

- 该函数根据提供的ResourceLocation或string返回对应注册表内注册项的ResourceLocation列表。

```js [KubeJS]
let idList = Utils.getRegistryIds("minecraft:block");
```

## getServer

- 该函数返回服务器对象，如果没有获取到则为null。
- 在Startup与Client脚本一定为null。

```js [KubeJS]
let server = Utils.getServer()
```

## getSound

- 该函数根据传入的ResourceLocation或string返回对应的声音[Internal.SoundEvent](../miscellaneous/SoundEvent.md)对象。

```js [KubeJS]
Utils.getSound("minecraft:entity.player.levelup");
```

- 获取玩家升级的Internal.SoundEvent对象。

## getStat

- 该函数根据传递的ResourceLocation或string返回玩家的统计数据[Internal.Stat\<ResouceLocation\>](../miscellaneous/Stat.md)对象。
a
```js [KubeJS]
Utils.getStat("minecraft:broken")
```

- 返回破坏方块数量的统计数据。

## getSystemTime

- 获取系统时间，返回单位为毫秒的数字。

```js [KubeJS]
let timeNumber = Utils.getSystemTime();
```

- 得到毫秒数可以通过创建Date对象来获取日期。

```js [KubeJS]
let date = new Date(Utils.getSystemTime());
```

- 调用date提供的方法即可获取日月年等信息。

## id

- 该函数根据提供的namespace与path字符串string，返回ResourceLocation对象。
- 等价于使用KubeJS提供的ResourceLocation包装类创建实例。

::: code-group

``` js [KubeJS]
let demoLocation = Utils.id("kubejs", "demo");
```

```js [KubeJS]
let demoLocation = Utils.id("kubejs:demo");
```

:::

## isWrapped

- 该函数检查传入的对象any是否为isWrapped的实例。
- 用途需补充。

```js
let obj = {};
let boo = Utils.isWrapped(obj);
```

## lazy

- 该函数接受一个[Internal.Supplier_\<T\>](../miscellaneous/Supplier.md) 返回一个惰性值[Internal.Lazy\<T\>](../miscellaneous/Lazy.md)对象。
- 可用于延迟对象的实例化。

```js [KubeJS]
let lazyLocation = Utils.lazy(() => new ResourceLocation("kubejs:demo"));
```

## newCountingMap

- 该函数返回一个[Internal.CountingMap](../miscellaneous/CountingMap.md)对象。

```js [KubeJS]
let countingMap = Utils.newCountingMap();
```

## newList

- 该函数返回一个新的可变列表(mutable list)，[Internal.List\<T\>](../miscellaneous/List.md)对象。

```js [KubeJS]
let list = Utils.newList();
```

## newMap

- 该函数返回一个新的可变Map(mutable map)，[Internal.Map\<K, V\>](../miscellaneous/Map.md)对象

```js [KubeJS]
let map = Utils.newMap();
```

## newRandom

- 该函数接受给定的种子number，返回一个新的[Random](../miscellaneous/Random.md)对象

```js [KubeJS]
let newRandom = Utils.newRandom(112233445566);
```

## parseBlockState

- 该函数接受string参数(string JSON)，解析BlockState，可能抛出无效输入Error。

```js [KubeJS]
let blockState = Utils.parseBlockState('{snowy: false}');
```

- 该示例仅供参考，未测试该表达式是否正确。

## parseDouble

- 尝试将第一个参数解析为Double类型，如果解析成功则返回，如果解析失败返回第二个参数。

```js [KubeJS]
let doubleNum = Utils.parseDouble(0, 0.00);
```

## parseInt

- 尝试将第一个参数解析为int类型，如果解析成功则返回，如果解析失败返回第二个参数。

```js [KubeJS]
let intNum = Utils.parseInt(0.0, 0);
```

## particleOptions

- 意义未明，接受参数any，返回[Internal.ParticleOptions](../miscellaneous/ParticleOptions.md)对象。

```js [KubeJS]
// 暂无可参考示例
```

## queueIO

- 立即在try-catch中运行传递的函数[Internal.Runnable_](../miscellaneous/Runnable.md)记录并抛出异常

```js [KubeJS]
Utils.queueIO(() => {console.log("message")});
```

## randomOf

- 使用传入的第一个参数[Internal.Random_](../miscellaneous/Random.md)在列表[Internal.Collection_\<T\>](../miscellaneous/Collection.md)(第二个参数)中随机获取一个对象

```js [KubeJS]
let randomElement = Utils.randomOf(Utils.getRandom(), [1, 2, 3, 4]);
```

## regex

- 该函数返回字符串的正则表达式模板[Internal.Pattern](../miscellaneous/Pattern.md)。

::: code-group

```js [KubeJS]
let pattern = Utils.regex("hello");
```

```js [KubeJS]
let pattern = Utils.regex("hello", 0);
```

:::

1. 返回匹配hello字符串的正则表达式模板。
2. 返回带有正则表达式标志（number参数类型）的正则表达式模板，待补充。

## rollChestLoot

1. 根据给定的ResourceLocation或string参数触发战利品表，返回物品列表。[Internal.List\<Internal.ItemStack\>](../miscellaneous/List.md)对象。
2. 根据给定的ResourceLocation或string参数以及一个实体参数触发战利品表，返回物品列表。[Internal.List\<Internal.ItemStack\>](../miscellaneous/List.md)对象。

::: code-group

```js [KubeJS]
let itemList = Utils.rollChestLoot("minecraft:chests/village/village_armorer");
```

```js [KubeJS]
// 假设这里的上下文可以获取player
let itemList = Utils.rollChestLoot("minecraft:chests/village/village_armorer", player);
```

:::

## runAsync

- 在KubeJS的后台线程中运行提供的可运行函数，并返回它的[CompletableFuture](../miscellaneous/CompletableFuture.md)对象。

```js [KubeJS]
let compleTableFuture = Utils.runAsync(() => console.log("message"));
```

## snakeCaseToCamelCase

- 该函数将蛇形命名法字符串string_name转换为驼峰命名法字符串stringName。

```js [KubeJS]
let stringName = Utils.snakeCaseToCamelCase("string_name");
```

## snakeCaseToTitleCase

- 该函数将蛇形命名法字符串string_name转换为标题大小写字符串String Name。

```js [KubeJS]
let stringName = Utils.snakeCaseToCamelCase("string_name");
```

##

- 在KubeJS的后台线程中运行提供的[Internal.Supplier_\<T\>](../miscellaneous/Supplier.md)函数，并返回它的[CompletableFuture\<T\>](../miscellaneous/CompletableFuture.md)

```js [KubeJS]
let compleTableFuture = Utils.supplyAsync(() => 1);
```

## toTitleCase

1. 将字符串的第一个字母大写，除非它是“a”， “an”， “the”， “of”， “on”， “in”， “and”， “or”， “but”或“for”
2. 将字符串的第一个字母大写。如果第二个参数为true，它也会将冠词和介词大写

::: code-group

```js [KubeJS]
let str = Utils.toTitleCase("steve in minecraft!");
```

```js [KubeJS]
let str = Utils.toTitleCase("steve in minecraft!", true);
```

:::
