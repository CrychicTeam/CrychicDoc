# 11.1 KubeJS Create

***

{% hint style="info" %}
This page is currently under construction and does not yet cover all aspects or information. We are working on it. Thank you for your understanding.
{% endhint %}

## 一、事件监听

和其他配方事件一样，要修改机械动力配方，你需要监听`ServerEvents.recipes`事件

```js
ServerEvents.recipes(event => {
  // code here
})
```

## 二、配方修改

简要地说，你需要将以下表格中的内容接在`event.recipe`后面，例如：

```js
ServerEvents.recipes(event => {
  event.recipes.create.crushing(['2x minecraft:cobblestone','minecraft:redstone',Item.of('minecraft:redstone').withChance(0.5)], 'minecraft:redstone_ore')
})
```

以下为简写方法：

| 格式                                                                                      | 类型       | 补充说明                                                                  |
| ----------------------------------------------------------------------------------------- | ---------- | ------------------------------------------------------------------------- |
| `create.conversion(output[], input[])`                                                    | 转化       | 如将竖直十字齿轮箱变为十字齿轮箱                                          |
| `create.crushing(output[], input[])`                                                      | 粉碎轮     | 输入和输出不一定为数组，输入可以为ingredients或流体，输出可以为物品或流体 |
| `create.cutting(output[], input[])`                                                       | 动力锯     | -                                                                         |
| `create.milling(output[], input[])`                                                       | 石磨       | -                                                                         |
| `create.basin(output[], input[])`                                                         | 工作盆     | -                                                                         |
| `create.mixing(output[], input[])`                                                        | 动力搅拌器 | 在其后可加 .heated() 和.superheated() (是否加热及其程度(加热和高温))      |
| `create.compacting(output[], input[])`                                                    | 压块塑形   | 在其后可加 .heated() 和.superheated() (是否加热及其程度(加热和高温))      |
| `create.pressing(output[], input[])`                                                      | 动力辊压机 | -                                                                         |
| `create.sandpaperPolishing(output[], input[])`                                            | 砂纸打磨   | -                                                                         |
| `create.splashing(output[], input[])`                                                     | 洗涤       | -                                                                         |
| `create.deploying(output[], input[])`                                                     | 机械手     | -                                                                         |
| `create.filling(output[], input[])`                                                       | 注液       | -                                                                         |
| `create.emptying(output[], input[])`                                                      | 倒出液体   | -                                                                         |
| `create.mechanicalCrafting(output, pattern[], {合成键值})`                                | 动力合成   | 详见下方示例                                                              |
| `create.sequencedAssembly(output[], input, sequence[]).transitionalItem(item).loops(int)` | 装配       | 详见下方示例及说明                                                        |

值得注意的是，在部分需要时间进行合成的配方中，你可以在配方后面加`.processingTime(整形 时间)`来修改合成时间。

## 三、分类示例

### 1、压块塑形