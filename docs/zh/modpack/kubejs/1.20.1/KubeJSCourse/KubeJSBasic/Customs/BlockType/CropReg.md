---
authors:
  - Gu-meng
editor: Gu-meng
---
# 农作物注册
在kubejs当中是可以直接来进行农作物注册的，所提供的方法也是非常的多，下面就是全部方法和使用示例

## 可使用方法
|                       方法名                        | 参数  |                作用                |      返回类型      |
| :-------------------------------------------------: | :---: | :--------------------------------: | :----------------: |
|                     age(number)                     |  -\>   |           设置作物的阶段           |        this        |
| age(number,Consumer\<CropBlockBuilder$ShapeBuilder\>) |  -\>   |     设置作物的每个阶段的碰撞箱     |        this        |
|                      crop(any)                      |   ?   |     添加作物为百分百概率输出?      |        this        |
|                  crop(any,number)                   |  -\>   |          添加作物掉落概率          |        this        |
|    bonemeal(ToIntFunction\<RandomTickCallbackJS\>)    |   ?   |            骨粉催化事件            |        this        |
|  growTick(ToDoubleFunction\<RandomTickCallbackJS\>)   |  -\>   |           随机刻选中事件           |        this        |
|     survive(CropBlockBuilder$SurviveCallback_)      |  -\>   |          设置生长条件事件          |        this        |
|                   dropSeed(bool)                    |  -\>   |    设置作物收割时候是否掉落种子    |        this        |
|               translationKey(string)                |  -\>   |       设置对象的对应翻译key        | BuilderBase\<Block\> |
|               formattedDisplayName()                |   -   |     使displayName覆盖语言文件      | BuilderBase\<Block\> |
|           formattedDisplayName(Component)           |   -   |     直接设置覆盖语言文件的文本     | BuilderBase\<Block\> |
|      dynamicMapColor(Function\<BlockState,any\>)      |   ~   | 设置块的每个状态在地图上的表现形式 |    BlockBuilder    |
|                  toString(string)                   |  -\>   |      表面意思(大概率不常调用)      |       string       |
|                   createObject()                    |   -   |                 ?                  |         ?          |
|              createAdditionalObjects()              |   -   |           创建额外对象?            |        void        |
|                     notifyAll()                     |   -   |                 ?                  |        void        |
|                    wait(number)                     |   -   |                 ?                  |        void        |
|              getTranslationKeyGroup()               |   -   |                 ~                  |        void        |
|             getBuilderTranslationKey()              |   -   |                 ~                  |        void        |
|         【static】 createShape(List\<AABB\>)          |   ~   |                 ?                  |     VoxelShape     |
|                        get()                        |   -   |                 ?                  |       Block        |
|                     getClass()                      |   -   |               获取类               |     typeof any     |
|                setWaterlogged(bool)                 |  -\>   |             **已过时**             |         -          |
|                  getWaterlogged()                   |   -   |             **已过时**             |         -          |

## 示例
```js
StartupEvents.registry("block", event => {
    event.create("test_crop", "crop")
        .age(3,cbb=>{
            cbb.shape(0,0,0,0,16,2,16)
            cbb.shape(1,0,0,0,16,8,16)
            cbb.shape(2,0,0,0,16,12,16)
            cbb.shape(3,0,0,0,16,16,16)
        })
        .growTick((blockstate, random) => {
            return 25;
        })
        .bonemeal(rtc => {     
            return rtc.random.nextInt(2)
        })
        .crop(Item.of("stone"))
        .survive((blockstate, level, pos) => {
            // 判断区块是否被加载
            if (level.isAreaLoaded(pos,1)){
                // 判断是否能够看到天空
                if(level.canSeeSky(pos)){
                    // 获取天空对作物的光照等级是否小于等于8
                    if (level.getBrightness("sky",pos) <= 8){
                        // 判断方块下面是否为下界合金块
                        if (level.getBlockState(pos.below()).is(Blocks.NETHERITE_BLOCK)){
                            return true
                        }
                    }
                }else{
                    // 判断方块产生的光照等级是否小于等于8
                    if (level.getBrightness("block",pos) <= 8){
                        if (level.getBlockState(pos.below()).is(Blocks.NETHERITE_BLOCK)){
                            return true
                        }
                    }
                }
            }
            return false
        })
        .texture("0", "minecraft:block/wheat_stage0")
        .texture("1", "minecraft:block/wheat_stage3")
        .texture("2", "minecraft:block/wheat_stage5")
        .texture("3", "minecraft:block/wheat_stage7")
        .item(seed => seed.texture("minecraft:wheat_seeds"))
})
```
这里直接借用了一下原版的部分小麦种子贴图(包括物品贴图)

`age(3,cbb=>{})` 在方法使用中已经有用法讲解，我们主要是调用里面的`shape`方法，该方法是设置物品的碰撞箱大小，简单哪一组示例`cbb.shape(2,0,0,0,16,12,16)`来解释

其中**2**代表到达age，前面三个**0**代表着最小的`x,y,z`，后面的`16,12,16`代表着最大的碰撞箱`x,y,z`，这里的碰撞下不同于方块里的`box`，设置`box`是为了将**方块建模**进行设置碰撞，在这里是为了设置植物的高度和大小的显示碰撞

`growTick` 代表被随机刻选中时，作物是否能够成长一级，计算方式为 `random.nextInt((25 / 返回值) + 1)` 进行计算随机数 在这里传上25也就是 随机数最大生成0和1，在原版的逻辑中除了0以外的数值都无法生长

`bonemeal` 代表着骨粉催植物事件，返回参数为作物被催熟的等级 返回1则作物age增加1 返回0则不变 这里用`rtc.random.nextInt(2)`来随机0或者1来作为返回值

`crop` 设置作物的产物 类似小麦作物会掉落小麦种子和小麦一样

`survive` 处理各种环境逻辑

* 比如：在哪个方块上可以种植、所需要的亮度
* 再比如：是否在末地种植、是否在地狱种植
* 返回true 表明可以种植
* 返回flase 则无法种植且如果被种植则会自己破坏掉自己
* 该事件会在每次growTick时不会检测到该事件（不知道是不是bug，在测试时并没有被检测到）
* 只有方块周围更新通知它的时候才会检测被检测到该事件
* 被检测时如果不在逻辑中就会被返回false然后作物就会被破坏
* 这里的逻辑可以参考原版种子无法在亮度不足的地方生长且会自我破坏成掉落物

`texture`设置各个age阶段的材质，这里简单引用了一下原版的
