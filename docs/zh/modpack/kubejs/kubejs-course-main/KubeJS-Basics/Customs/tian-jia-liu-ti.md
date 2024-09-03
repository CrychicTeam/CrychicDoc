# 注册流体
在kubejs当中使用下面一行代码进行流体的添加注册
```js
StartupEvents.registry("fluid",event =>{
    event.create("meng:my_fluid")
})
```
在使用kubejs创建流体时，kubejs会默认为你注册一个流体桶

创建流体后我们可以为流体添加上以下属性
## 属性方法
|              方法名              |  方法参数  |                           参数描述                           |                   方法作用                   |          返回类型           | 测试是否成功 |
| :------------------------------: | :--------: | :----------------------------------------------------------: | :------------------------------------------: | :-------------------------: | :----------: |
|            gaseous()             |     -      |                              -                               |                设置流体为气态                |            this             |     ???      |
|        rarity(arityType)         |   稀有度   |                              -                               | 设置[流体的稀有度](../../ti-wai-hua/xi-you-du.md) |            this             |     ???      |
|        getRegistryType()         |     -      |                              -                               |                获取注册表类型                |        RegistryInfo         |      -       |
|           color(Color)           |    颜色    | 可以使用默认的字符串也可以使用16进制颜色代码(0Xffffff--白色) |                给流体设置颜色                |            this             |      -       |
|          createObject()          |     -      |                              -                               |                      ?                       |            this             |     ???      |
|           density(int)           |    整型    |                              -                               |            设置流体的粘稠度/密度             |            this             |     失败     |
|          translucent()           |     -      |                              -                               |              设置流体为半透明的              |            this             |     失败     |
|         luminosity(int)          |    整型    |                             亮度                             |                设置流体的亮度                |            this             |     失败     |
|        bucketColor(Color)        |    颜色    |                             同上                             |                 设置桶的颜色                 |            this             |     成功     |
|        builtinTextures()         |     -      |                              -                               |                      ?                       |            this             |     ???      |
|        createAttributes()        |     -      |                              -                               |                 创建属性???                  | ArchitecturyFluidAttributes |     ???      |
|            noBucket()            |     -      |                              -                               |                 不需要生成桶                 |            this             |     成功     |
|            noBlock()             |     -      |                              -                               |               不需要生成流体块               |            this             |     成功     |
|        thinTexture(Color)        |    颜色    |                             同上                             |    设置全局颜色包括桶和流体颜色(水的材质)    |            this             |     成功     |
|       thickTexture(Color)        |    颜色    |                             同上                             |   设置全局颜色包括桶和流体颜色(岩浆的材质)   |            this             |     成功     |
|  stillTexture(ResourceLocation)  | 材质路径id |                              -                               |             设置流体静止时的材质             |            this             |      -       |
| flowingTexture(ResourceLocation) | 材质路径id |                              -                               |             设置流体流动时的材质             |            this             |      -       |
|          viscosity(int)          |    整型    |                              -                               |      设置流体的粘性程度(类似于岩浆那种)      |            this             |     失败     |
|         renderType(str)          |   字符串   |                              ?                               |                      ?                       |            this             |     ???      |
|    createAdditionalObjects()     |     -      |                              -                               |                      ?                       |            this             |     ???      |
|         temperature(int)         |    整型    |                              -                               |                设置流体的温度                |            this             |     失败     |
|         displayName(str)         |   字符串   |                              -                               |      设置在没有lang文件时直接显示的名字      |            成功             |
|           flowingFluid           |     -      |                         直接调用参数                         |                      ?                       |     FlowingFluidBuilder     |      -       |
|            attributes            |     -      |                         直接调用参数                         |                      ?                       | ArchitecturyFluidAttributes |      -       |
|              block               |     -      |                         直接调用参数                         |                   流体方块                   |      FluidBlockBuilder      |      -       |
|            bucketItem            |     -      |                         直接调用参数                         |                  流体桶物品                  |   FluidBucketItemBuilder    |      -       |

以上属性可能不全，详细以probejs导出为例

# 注册示例
```js
StartupEvents.registry("fluid", (event) =>{
    //设置流体为熔岩的材质颜色为粉色
    event.create("meng:my_fluid")
		.thickTexture(0Xff82e0)
    //设置流体为水分材质颜色为粉色
    event.create("meng:my_fluid2")
		.thinTexture(0Xff82e0)
    //设置流体为绿色不生成流体块
    event.create("meng:my_fluid4")
		.thickTexture("GREEN")
		.noBlock()
    //设置流体为蓝色不生成桶
    event.create("meng:my_fluid5")
		.thinTexture("BLUE")
		.noBucket()
    //设置流体为粉色但是桶的颜色为黄色
    event.create("meng:my_fluid6")
		.thickTexture(0Xff82e0)
		.bucketColor("yellow")
})
```
## lang文件
```json
{
    "fluid.meng.my_fluid": "孤梦的流体",
    "item.meng.my_fluid_bucket": "孤梦的流体桶"
}
```

### 简单的注册流体轮子

**注:以下内容根据个人习惯选择性使用和更改**

```js
StartupEvents.registry("fluid", (event) => {
	// ModID声明如果选择不更改ModID(默认即"kubejs")直接把ModID这个变量取消
	const MODID = "meng:"
	// 路径常量
	const PATH = "block/fluid/"
	
	/* 
	* 定义流体
	* 在添加下一种流体时要记得在[]后加上逗号
	* 并且一定要严格按照格式进行
	* [流体id, 颜色]
	*/
	let fluidRegisters = [
		["example_fluid", 0x808080],
	]
	fluidRegisters.forEach(([name, color]) => {
		event.create(MODID + name) // 声明id
			.thickTexture(color) // 流体颜色
			.bucketColor(color) // 桶内流体颜色
			.flowingTexture(MODID + PATH + "flowing") // 读取本地流体贴图文件,可在(/code/This/kubejs/assets/meng/textures/block/fluid)下载(.mcmeta文件也要一起下载!)
			.stillTexture(MODID + PATH + "still") // 读取本地流体贴图文件,可在(/code/This/kubejs/assets/meng/textures/block/fluid)下载(.mcmeta文件也要一起下载!)
			.tag(MODID + "fluid") // 添加流体tag(可选)
	})
})
```