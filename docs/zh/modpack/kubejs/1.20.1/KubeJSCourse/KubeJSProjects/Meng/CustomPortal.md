---
authors:
  - Gu-meng
editor: Gu-meng
---
# 自定义传送门
该项目使用[Custom Portal API [Forge]](https://www.mcmod.cn/class/10354.html)作为前置，在使用时需要安装该模组

涉及模组及版本:
1. rhino-forge-2001.2.2-build.18
2. architectury-9.2.14-forge
3. kubejs-forge-2001.6.5-build.14
4. probejs-6.0.1-forge
5. customportalapi-0.0.1-forge-1.20

## 项目代码
该示例代码需要写进`startup_scripts`文件夹内

```js
const $CustomPortalBuilder = Java.loadClass("net.kyrptonaught.customportalapi.api.CustomPortalBuilder")
const $BuiltinDimensionTypes = Java.loadClass("net.minecraft.world.level.dimension.BuiltinDimensionTypes")

StartupEvents.postInit(e=>{
    $CustomPortalBuilder
        .beginPortal() //开始构建自定义传送门
        ["frameBlock(net.minecraft.world.level.block.Block)"](Blocks.STONE) //传送门的框架方块
        .destDimID($BuiltinDimensionTypes.NETHER_EFFECTS) //传送维度
        .tintColor(131, 66, 184) // 传送门的RGB颜色
        .registerPortal(); //注册自定义传送门

    $CustomPortalBuilder
        .beginPortal() //开始构建自定义传送门
        ["frameBlock(net.minecraft.world.level.block.Block)"](Blocks.DIAMOND_BLOCK) //传送门的框架方块
        .destDimID($BuiltinDimensionTypes.NETHER_EFFECTS) //传送维度
        .lightWithItem(Items.DIAMOND) //激活传送门的物品
        .flatPortal() // 传送门为平面的
        .tintColor(131, 133, 184) // 传送门的RGB颜色
        .registerPortal(); //注册自定义传送门
})
```

## CustomPortalBuilder可调用方法
参考开源路径:[https://github.com/kyrptonaught/customportalapi/blob/1.20/src/main/java/net/kyrptonaught/customportalapi/api/CustomPortalBuilder.java](https://github.com/kyrptonaught/customportalapi/blob/1.20/src/main/java/net/kyrptonaught/customportalapi/api/CustomPortalBuilder.java)
|                                  方法名                                   |      方法参数       |                         方法作用                         |
| :-----------------------------------------------------------------------: | :-----------------: | :------------------------------------------------------: |
|                               beginPortal()                               |          -          |                    创建一个新的传送门                    |
|                             registerPortal()                              |          -          |            注册传送门，在所有参数传完之后调用            |
|                       frameBlock(ResourceLocation)                        |       方块id        |                    设置传送门框架方块                    |
|                             frameBlock(Block)                             |       Block类       |                    设置传送门框架方块                    |
|                        destDimID(ResourceLocation)                        |       纬度id        |                    设置传送门前往纬度                    |
|                              tintColor(int)                               |     16进制颜色      |                     设置传送门的颜色                     |
|                       tintColor(int r,int g,int b)                        |     传入RGB数字     |                     设置传送门的颜色                     |
|                             lightWithWater()                              |          -          |                    设置传送门被水激活                    |
|                            lightWithItem(Item)                            |       Item类        |                   设置传送门被物品激活                   |
|                           lightWithFluid(Fluid)                           |       Fluid类       |                   设置传送门被流体激活                   |
|                  customIgnitionSource(ResourceLocation)                   |          ~          |                            a1                            |
|                customIgnitionSource(PortalIgnitionSource)                 |          ~          |                            a1                            |
|                          forcedSize(int w,int h)                          |    w 宽度,h 高度    |                   强制设置传送门的大小                   |
|                   customPortalBlock(CustomPortalBlock)                    | CustomPortalBlock类 |                     自定义传送门方块                     |
|                    returnDim(ResourceLocation,boolean)                    |      纬度id,b1      |                       设置返回纬度                       |
|                          onlyLightInOverworld()                           |          -          |                            a2                            |
|                               flatPortal()                                |          -          |           设置为平面传送门(末地传送门呈现形式)           |
|                    customFrameTester(ResourceLocation)                    |          ~          |                            a3                            |
|            registerBeforeTPEvent(Function\<Entity,SHOULDTP\>)             |          ~          | 注册一个在实体传送前调用事件，可以通过SHOULDTP来取消事件 |
| registerInPortalAmbienceSound(Function\<PlayerEntity,CPASoundEventData\>) |          ~          |                 注册玩家在传送门内的声音                 |
| registerPostTPPortalAmbience(Function\<PlayerEntity,CPASoundEventData\>)  |          ~          |                  注册玩家在传送时的声音                  |
|                  registerPostTPEvent(Consumer\<Entity\>)                  |          ~          |                      注册传送后事件                      |

### fabric 版本独占
这个地方根据可调用情况推测下面只有[fabric版本](https://www.mcmod.cn/class/15391.html)的开源调用
|                      方法名                       |   方法参数   |                            方法作用                             |
| :-----------------------------------------------: | :----------: | :-------------------------------------------------------------: |
|              beginPortal(PortalLink)              | PortalLink类 |             使用自定义的PortalLink类创建新的传送门              |
|              registerPortalForced()               |      -       |                         强制注册传送门                          |
|   registerPreIgniteEvent(PortalPreIgniteEvent)    |      ~       | 传送门激活之前事件，如果PortalPreIgniteEvent返回false则激活失败 |
|      registerIgniteEvent(PortalIgniteEvent)       |      ~       |                        传送门激活后事件                         |
|    setPortalSearchYRange(int bottomY,int topY)    |      -       |      设置传送门搜索有效生成y轴，默认为世界的最高点和最低点      |
| setReturnPortalSearchYRange(int bottomY,int topY) |      -       |  设置传送门返回时搜索有效生成的y轴，默认为世界的最高点和最低点  |

### 关于官方的解释
a1: Specify a Custom Ignition Source to be used to ignite the portal. You must manually trigger the ignition yourself.

a2: Specify that this portal can only be ignited in the Overworld.Attempting to light it in other dimensions will fail.

a3: Specify a custom portal frame tester to be used.

b1: Should this portal only be ignitable in returnDimID.

## 关于注册传送门方块
```js
const $CustomPortalsMod = Java.loadClass("net.kyrptonaught.customportalapi.CustomPortalsMod")
const $CustomPortalBlock = Java.loadClass("net.kyrptonaught.customportalapi.CustomPortalBlock")
const $BlockBehaviourProperties = Java.loadClass("net.minecraft.world.level.block.state.BlockBehaviour$Properties")
const $SoundType = Java.loadClass("net.minecraft.world.level.block.SoundType")
const $EventBuses = Java.loadClass("dev.architectury.platform.forge.EventBuses");

let test;

StartupEvents.init(e => {
    test = $CustomPortalsMod.registerOnlyBlock("test",()=>{
        return new $CustomPortalBlock(
            $BlockBehaviourProperties.copy(Blocks.NETHER_PORTAL)
            .lightLevel(() => 11)
            .noCollission()
            .strength(-1)
            .sound($SoundType.GLASS)
        )
    })

    $CustomPortalsMod.BLOCKS.register($EventBuses.getModEventBus("kubejs").get());
})

StartupEvents.postInit(e=>{
    $CustomPortalBuilder
        .beginPortal()
        ["frameBlock(net.minecraft.world.level.block.Block)"](Blocks.STONE)
        .customPortalBlock(test)
        .destDimID($BuiltinDimensionTypes.NETHER_EFFECTS) 
        .tintColor(131, 66, 184)
        .registerPortal();
})
```

## 一些注意事项
1. 该项目主要测试版本为`1.20.1`模组为[Custom Portal API [Forge]](https://www.mcmod.cn/class/10354.html)
2. 如果因为模组版本跟该项目模组版本不同导致报错请自行解决
3. 该项目主要分享主要写法，更多内容请自行查看开源项目后进行编写
