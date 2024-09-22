---
authors: ['Wudji']
---

# 15.5 本章例子

***

## 一、使玩家在潜行状态下无法使用指定物品

第一块是对着空气使用物品，第二块是对着方块使用物品

```
onEvent('item.right_click', event => {
    if(event.player.crouching == true && event.item.id == "minecraft:diamond"){
        event.cancel();// 取消该事件
    }
})

onEvent('block.right_click', event => {
    if(event.player.crouching == true && event.item.id == "minecraft:diamond"){
        event.cancel();// 取消该事件
    }
})
```

## 二、随机传送

```
// 最小及最大坐标
const minX = 1000;
const minZ = 1000;
const maxX = 10000;
const maxZ = 10000;
const minY = 100;
const maxY = 200;
// 是否随机取负值
const reverseNumber = true;

// 返回随机数
function getNumber(min,max){
    let randNumber = Math.floor(Math.random() * (max - min + 1) ) + min;
    if(Math.random() > 0.5 && reverseNumber){
        randNumber = -randNumber;
    }
    return randNumber;
}

// 监听聊天事件，如果内容为 "!rtp" 就随机传送并给予缓降效果
onEvent('player.chat',function (event){
	let input = event.message.trim();
	if(input == "!rtp"){
        event.player.setPosition(getNumber(minX,maxX), Math.abs(getNumber(minY,maxY)), getNumber(minZ,maxZ));// 设置坐标
        event.player.statusMessage = `已将你传送到 x: ${event.player.x} y: ${event.player.y} z: ${event.player.z}`;// 玩家状态栏显示文字
        event.player.potionEffects.add('minecraft:slow_falling', 600, 10, false, false);// 给予缓降效果
        /**
         * EntityPotionEffectsJS支持以下函数
         * .clear() 清除药水效果
         * .getActive() 返回具有的效果
         * .isActive(MobEffect 效果) 返回是否具有给定效果
         * .getDuration(MobEffect 效果) 返回给定效果剩余时间
         * .add(MobEffect 效果, 整形 持续时间(tick), 整形 等级, 布尔值 是否为信标产生, 布尔值 是否显示粒子)
         *     其中后三项，后两项，后一项均可省略不写，默认值对应为0，false, true. 
         * .isApplicable(MobEffect 效果) 返回给定效果是否能应用于当前实体上
         */
        event.cancel();// 取消该事件，也就是说玩家发送的消息不会显示在其他玩家的公屏上
    }
})
/**
 * 另一种写法：监听物品右键事件，如果物品id为钻石则消耗一个钻石随机传送并冷却100秒
 * 此处钻石可以替换为你想使用的物品
 * 注：重进世界会刷新冷却时间
 */
onEvent('item.right_click',function (event){
	if(event.player.mainHandItem.id == "minecraft:diamond"){
        event.player.setPosition(getNumber(minX,maxX), Math.abs(getNumber(minY,maxY)), getNumber(minZ,maxZ));
        event.player.statusMessage = `已将你传送到 x: ${event.player.x} y: ${event.player.y} z: ${event.player.z}`;
        event.player.potionEffects.add('minecraft:slow_falling', 600, 10, false, false);
		// 钻石数量减一
		event.item.count--;
        event.player.addItemCooldown('minecraft:diamond', 2000);
    }
})
```

![](https://m1.miaomc.cn/uploads/20220408\_c412408992599.gif)

## 三、爆炸“法杖”

这里的烈焰棒只是一个"占位符"，你可以换成你想使用的任何物品

```
// 绘制粒子函数
function drawParticle(event, intensity, particle) {
    let start = event.getPlayer()
    let target = event.player.rayTrace(1000)
    if(target == null){
        return false;
    }
    let rayx = target.block.x;
    let rayy = target.block.y
    let rayz = target.block.z;
    
    let d = getDistance(start.x, start.y + 1, start.z, rayx, rayy, rayz)
    for (let i = -1; i < d*intensity; i++) {
        let delta = i / d
        let x = (1 - delta) * start.x + delta * (rayx + 0.5)
        let y = (1 - delta) * start.y + delta * (rayy + 0.5)
        let z = (1 - delta) * start.z + delta * (rayz + 0.5)
        event.server.runCommandSilent(`/particle ${particle} ${x} ${y} ${z} 0.1 0.1 0.1 0.01 5 normal`)
    }
    return true;
}
// 生成爆炸(使用火球实体来实现爆炸)
onEvent("item.right_click", event => {
    if(event.player.rayTrace(1000).block != null && event.item.id == "minecraft:blaze_rod"){
        let entity = event.world.createEntity('minecraft:fireball');
        // 合并NBT设置爆炸等级
        entity.mergeFullNBT('{ExplosionPower:2}')
        entity.setPosition(event.player.rayTrace(1000).block.x, event.player.rayTrace(1000).block.y + 1, event.player.rayTrace(1000).block.z);
        entity.setMotion(0, -20, 0);
        entity.spawn();
        drawParticle(event,0.5,"minecraft:spit");
        // 添加物品冷却
 		event.player.addItemCooldown(event.item.id, 1000);
    }
})
// 生成爆炸(使用ExplosionJS)
onEvent("item.right_click", event => {
    if(event.player.rayTrace(1000).block != null && event.item.id == "minecraft:blaze_rod"){
        let explosion = event.player.rayTrace(1000).block.createExplosion();
        // 设置引爆者
        explosion.exploder(event.player);
        // 设置其他属性
        explosion.strength(2.0);
	    explosion.causesFire(true);
	    // 引爆
	    explosion.explode();
        // 绘制粒子
        drawParticle(event,0.5,"minecraft:spit");
        // 添加物品冷却
        event.player.addItemCooldown(event.item.id, 1000);
    }
})
```

![](https://m1.miaomc.cn/uploads/20220416\_2f6b7441fc14d.gif)

## 四、扫地机器人

```
// 物品白名单
const whitelist = Ingredient.matchAny([
	'minecraft:diamond', //单个物品
	'minecraft:gold_ingot',//单个物品
	'@tinkersconstruct', //mod物品示例
	'minecraft:emerald'
])
// 执行一次间隔（注：该值必须大于1）
const minutes = 30;

// ======================================

var lastResult = [];
var totalResult = [];
var lastItemCount = 0;
var totalItemCount = 0;


function clearLag(server){
	lastResult = [];
	lastItemCount = 0;
	server.getEntities("@e[type=item]").forEach(entity => {
		if (!whitelist.test(entity.item.id)){
			lastResult.push([entity.item.id, entity.item.count]);
			totalResult.push([entity.item.id, entity.item.count]);
			lastItemCount += entity.item.count;
			entity.kill();
		}
	});
	lastResult.sort();
	totalResult.sort();
	server.tell([Text.lightPurple('[扫地机器人]'), `本次共清除 ${lastItemCount} 个物品`]);
	server.tell([Text.lightPurple('[扫地机器人]'), "在聊天框中输入 !clearLag last  来获取本次详细信息"]);
	server.tell([Text.lightPurple('[扫地机器人]'), "在聊天框中输入 !clearLag total 来获取全部详细信息"]);
}

function countResult(result,event){
	if(result != []){
		result.forEach((singleResult, index) => {
		event.server.tell([Text.lightPurple(`第 ${index + 1} 项 `), `${singleResult[0]} , 个数为 ${singleResult[1]}`])
		})
	}
}

onEvent('server.load', function (event) {
	event.server.scheduleInTicks(100, event.server, function (callback0) {
		callback0.data.tell([Text.lightPurple('[扫地机器人]'), `加载成功，使用 !clearlag help 查看帮助`]);
	})
	event.server.schedule((minutes - 1) * MINUTE, event.server, function (callback1) {
		callback1.data.tell([Text.lightPurple('[扫地机器人]'), "1分钟后将清理地面掉落物"]);
		callback1.data.schedule(MINUTE, callback1.data, function(callback2) {
			clearLag(callback2.data);
		})
		callback1.reschedule();
	})
})

// 聊天事件，只有有OP权限的文件才能执行查询/扫地
onEvent('player.chat',function (event){
	let input = event.message.trim();
	switch (input) {
		case "!clearlag last":
			if (event.player.op){// 判断玩家权限
				countResult(lastResult,event);
			}else{
				event.player.tell([Text.lightPurple('[扫地机器人]'), "你没有权限这样做"]);
			}
			break;
		case "!clearlag total":
			if (event.player.op){
				countResult(totalResult,event);
			}else{
				event.player.tell([Text.lightPurple('[扫地机器人]'), "你没有权限这样做"]);
			}
			break;
		case "!clearlag help":
			event.player.tell([Text.lightPurple('[扫地机器人]'), "扫地机器人 by Wudji@mcbbbs.net. Powered by KubeJS"]);
			event.player.tell([Text.lightPurple('[扫地机器人]'), "在聊天框中输入 !clearLag last  来获取本次详细信息"]);
			event.player.tell([Text.lightPurple('[扫地机器人]'), "在聊天框中输入 !clearLag total 来获取全部详细信息"]);
			event.player.tell([Text.lightPurple('[扫地机器人]'), "在聊天框中输入 !clearLag 立即清除掉落物"]);
			break;
		case "!clearlag":
			if (event.player.op){
				clearLag(event.server);
			}else{
				event.player.tell([Text.lightPurple('[扫地机器人]'), "你没有权限这样做"]);
			}
			break;
	}
})
```
