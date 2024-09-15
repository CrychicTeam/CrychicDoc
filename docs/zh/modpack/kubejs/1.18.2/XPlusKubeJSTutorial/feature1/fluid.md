# 5 自定义流体

***

KubeJS允许你在startup阶段自定义流体。对应事件：`fluid.registry`

不过到目前为止，Fabric下的KubeJS还不支持自定义流体

~~不得不说KubeJS还是非常良心的，注册流体还免费送你个桶(doge)~~

| 函数                      | 功能                         | 返回值          |
| ----------------------- | -------------------------- | ------------ |
| create(字符串 流体ID)        | 新建流体                       | FluidBuilder |
| color(整形 颜色)            | 设置流体颜色\[1]                 | FluidBuilder |
| bucketColor(整形 颜色)      | 设置流体桶内流体的颜色                | FluidBuilder |
| textureStill(资源位置 ID)   | 设置液体静止时的贴图                 | FluidBuilder |
| textureFlowing(资源位置 ID) | 设置液体流动时的贴图                 | FluidBuilder |
| textureThick(整形 颜色)     | 设置液体静止时的颜色                 | FluidBuilder |
| textureThin(整形 颜色)      | 设置液体流动时的颜色                 | FluidBuilder |
| luminosity(整形 发光度)      | 设置流体亮度(默认值为0)\[2]          | FluidBuilder |
| density(整形 密度)          | 设置流体密度(默认值为1000)           | FluidBuilder |
| temperature(整形 稳定)      | 设置流体温度(默认值为300)            | FluidBuilder |
| viscosity(整形 粘稠度)       | 设置流体粘稠度(默认值为1000)          | FluidBuilder |
| gaseous()               | 设置流体为气体(???¿¿¿)(默认值为false) | FluidBuilder |

\[1]：使用16进制，形如`0x844031`

\[2]：从本行往下，楼主测试时均无法实现预期效果，功能描述为楼主的"臆断"（游戏版本1.16.5 KubeJS版本1605.3.19-build.299），~~但是理论上这些是可以用的，可能和楼主电脑有关系吧~~

例子：

```
onEvent('fluid.registry', event => {
	event.create("test_mud").displayName("泥").bucketColor(0x844031).textureThick(0x844031).textureThin(0x844031);
})
```

![2022-05-03\_20.31.50.png](https://m1.miaomc.cn/uploads/20220503\_0795d7c613651.png)

![2022-05-03\_20.31.55.png](https://m1.miaomc.cn/uploads/20220503\_b75d0f305a728.png)
