---
authors: ['Wudji']
---

# 18.2 罐头示例(包含物品注册, 配方添加, 玩家交互和persistentData的使用)

***

在这个例子中，我们将：

* 添加3个物品：密封的牛肉罐头，打开的牛肉罐头，罐头盒
* 添加配方：8个玻璃板合成3个罐头盒，罐头盒+两个牛肉无序合成密封的牛肉罐头
* 添加事件：当玩家手持密封的牛肉罐头长按右键时打开罐头
* 添加事件：当玩家吃完罐头后给予玩家一个空罐头盒

## 1、物品注册

`kubejs\startup_scripts\item_registry.js`

```
onEvent('item.registry', event => { // 监听物品注册事件
	event.create('can_empty').displayName('空罐头盒').maxStackSize(32);// 最大堆叠数：32
	event.create('can').displayName('密封的牛肉罐头').maxStackSize(8);// 最大堆叠数：8
	event.create('can_open').displayName('打开的牛肉罐头').maxStackSize(1).food(food => {
		food.hunger(18);// 设置食物信息
		food.saturation(18);
	});
})
```

## 2、配方修改

`kubejs\server_scripts\recipe.js`

```
onEvent('recipes', event => { // 监听配方事件

	// 空罐头盒合成
    event.shaped('8x kubejs:can_empty', [
            'GGG',
            'G G',
            'GGG'
      ], {
            G: 'minecraft:glass_pane'
    })
    
    // 罐头合成
    event.shapeless('kubejs:can', ['kubejs:can_empty', '2x minecraft:cooked_beef'])
})
```

## 3、罐头事件系统

`kubejs\server_scripts\can_events.js`

```
onEvent("item.right_click", event => {// 监听物品右键事件
    if(event.player.mainHandItem.id == "kubejs:can"){ // 判断物品是否为给定物品
        if(event.player.persistentData.canholdtime / 10 >= 1){
        	// 当时间达到一定值时就执行物品交换
        	
        	// 给予玩家空罐头盒
            event.player.give("kubejs:can_open");
            
            // 重置persistentData
            event.player.persistentData.canholdtime = 0;
            
            // 密封罐头数-1
            event.item.count--;
        }
        // 若时间未达到一定值则自增1
        event.player.persistentData.canholdtime++;

    	// 刷新罐头打开进度条
        event.player.statusMessage = `罐头打开进度：${event.player.persistentData.canholdtime * 10}%`;
    }
})

onEvent("player.logged_in", event =>{// 监听玩家登录事件

	// 玩家加入世界时初始化 persistentData 否则其值将为NAN
    event.player.persistentData.canholdtime = 0;
})

onEvent("item.food_eaten", event =>{// 监听吃食物事件

	// 如果物品id为打开的罐头就给予玩家一个罐头盒
    if(event.item.id == "kubejs:can_open"){
        event.player.give("kubejs:can_empty")
    }
})
```

## 4、修改展示

(图片较大，请耐心等待)

![2022-05-09\_22.50.20.png](https://m1.miaomc.cn/uploads/20220509\_9be568769fad2.png)

![2022-05-09\_22.50.35.png](https://m1.miaomc.cn/uploads/20220509\_de72468c663a2.png)

![2022-05-09\_22.51.07.png](https://m1.miaomc.cn/uploads/20220509\_765843f2162a8.png)

![](https://m1.miaomc.cn/uploads/20220509\_17accde033a92.gif)

![](https://m1.miaomc.cn/uploads/20220509\_13b127ea703dd.gif)
