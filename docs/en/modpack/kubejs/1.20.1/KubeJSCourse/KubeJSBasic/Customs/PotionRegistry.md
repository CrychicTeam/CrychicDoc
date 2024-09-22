# 注册药水效果
如果原版目前提供的药水效果并不能满足你的需求，你现在想写一些自己的特殊药水效果，本章内容在startup_scripts文件夹下创建
```js
StartupEvents.registry("mob_effect",event=>{
    event.create("meng:my_mob_effect")
})
```
上面代码只是注册了药水效果，但是药水效果并没有实质性的作用，我们可以给药水效果加上以下的属性方法
## 常用属性方法
|                               方法名                                |     方法作用     |            备注            |
| :-----------------------------------------------------------------: | :--------------: | :------------------------: |
|                            `harmful() `                             |    负面的药水    |          显示作用          |
|                           `beneficial()`                           |    增益的药水    |          显示作用          |
|                  `effectTick(EffectTickCallback)`                   |  药水的tick效果  | 主要用于药水提供的效果编写 |
|                           `color(Color)`                            | 设置药水粒子颜色 |             -              |
|                    `category(MobEffectCategory)`                    |    药水种类？    |             -              |
| `modifyAttribute(ResourceLocation,string,double,AttributeModifier)` |     修改属性     |             -              |