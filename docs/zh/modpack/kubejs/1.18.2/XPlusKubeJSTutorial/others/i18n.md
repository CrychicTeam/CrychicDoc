# 17 本地化相关

***

KubeJS的本地化相关内容非常人性化，你可以直接使用现有的本地化键名来翻译新建的物品，方块和流体而不用手动新建。

对于方块，你需要使用本地化键名`block.kubejs.<自定义方块ID>`

对于物品，你需要使用本地化键名`item.kubejs.<自定义物品ID>`

对于流体，你需要使用本地化键名`fluid.kubejs.<自定义流体ID>`

下面我们来看一个例子。

假设你的脚本文件是这样的：

`kubejs\startup_scripts\registry.js`

```
onEvent('block.registry', event => {
	event.create('super_tnt').material('wood').hardness(1.0).displayName('超级TNT');
})

onEvent('item.registry', event => {
	event.create('can').displayName('罐头').maxStackSize(8);
})

onEvent('fluid.registry', event => {
	event.create("test_mud").displayName("泥").bucketColor(0x844031).textureThick(0x844031).textureThin(0x844031);
})
```

那么你的语言文件应该按以下格式书写：

`kubejs\assets\kubejs\lang\en_us.json`

```
{
    "block.kubejs.super_tnt":"Super TNT",
    "item.kubejs.can":"Can",
    "fluid.kubejs.mud":"Mud"
}
```

语言文件内翻译项目的顺序无所谓，只要都有就行
