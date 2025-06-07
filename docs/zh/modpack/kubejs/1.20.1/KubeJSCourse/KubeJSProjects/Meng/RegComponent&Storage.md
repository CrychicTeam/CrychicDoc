---
authors:
  - Gu-meng
editor: Gu-meng
---
# 注册AE存储元件和对应的组件
本章涉及内容：`Java.loadClass`的使用、`StartupEvents.registry`里的`createCustom`使用

本章使用模组:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge
6. appliedenergistics2-forge-15.2.13

## 代码部分
下面代码全部都在`startup`文件夹内

```js
const $BasicStorageCell = Java.loadClass("appeng.items.storage.BasicStorageCell")
const $StorageComponentItem = Java.loadClass("appeng.items.materials.StorageComponentItem")
const $AEItems = Java.loadClass("appeng.core.definitions.AEItems")
const $AEKeyType = Java.loadClass("appeng.api.stacks.AEKeyType")
const $Item = Java.loadClass("net.minecraft.world.item.Item");

const namespace = "meng:";

let AECellComponentItems = {
}

/**
 * 注册存储元件和对应的组件
 * @param {Number} byte 最大内存 kb
 * @param {Number} maxItemTypeCount 最大物品类型属性 1~63
 * @param {Number} AE2Energy 消耗的ae能
 * @param {Number} bytesPer 每一个新物品开辟所需的空间byte
 */
function regCellComponent(byte,maxItemTypeCount,AE2Energy,bytesPer){
    let cellComponentId = `${namespace}cell_component_${byte}k`
    let storageCellId = `${namespace}item_storage_cell_${byte}k`
    AECellComponentItems[byte] = {
        byte:byte,
        cellComponent: cellComponentId,
        storageCell:storageCellId,
        maxItemTypeCount:maxItemTypeCount,
        AE2Energy:AE2Energy,
        bytesPer:bytesPer
    }
}

regCellComponent(512,63,3.0,4096)
regCellComponent(1024,63,4.0,4096)
regCellComponent(2048,63,5.0,4096)
regCellComponent(4096,63,6.0,4096)
regCellComponent(8192,63,7.0,4096)

StartupEvents.registry("item", event => {
    for (const key in AECellComponentItems) {
        let aeValue = AECellComponentItems[key];
        event.createCustom(aeValue.cellComponent,
            ()=>new $StorageComponentItem($Item.Properties().stacksTo(1), aeValue.byte))
        event.createCustom(aeValue.storageCell,
            ()=>new $BasicStorageCell(
                $Item.Properties().stacksTo(1), 
                Item.of(aeValue.cellComponent), 
                $AEItems.ITEM_CELL_HOUSING, 
                aeValue.AE2Energy, 
                aeValue.byte, 
                aeValue.bytesPer, 
                aeValue.maxItemTypeCount, 
                $AEKeyType.items()
            )
        )
    }
})
```
上面代码注册了512k到8192k的存储元件和对应的组件

在游戏内也可以和原版一样`shift+右键`将组件和对应的框拆开

汉化和贴图和正常注册物品流程一样

## 我该如何使用上方代码
孤梦已经为你造好了轮子，你只需要调用`regCellComponent`这个方法然后往里面传参，剩下的就是重启游戏就可以了
```js
regCellComponent(512,63,3.0,4096)
```
就跟上方代码一样，直接正常调用然后重启游戏就可以被加载进游戏里了

## 注意事项
1. 该项目只是作为示例，可能有些地方没有解释清除，如需请自己查阅相关资料
2. 如需使用代码请注释表明此文档链接(浏览器链接区)
