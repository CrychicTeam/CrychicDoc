# 16 网络包和Painter API

***

## 一、网络包

事件：

客户端从服务端侧接收脚本`player.data_from_server.<信道ID>`

服务端从客户端侧接收脚本`player.data_from_server.<信道ID>`

```
// 监听玩家物品右键事件
// 你可以把下面这段脚本放到服务端或客户端脚本中，取决于你想从哪一侧发包
onEvent('item.right_click', event => {
  // 监听物品右键事件
  if (event.server) {
    // 发送 {test: 123} 到信道 "test_channel_1". 信道ID可以为任何字符串, 但还是推荐使用蛇形命名方法
    // 接收端为客户端 (发送端为服务端).
    event.player.sendData('test_channel_1', { test: 123 });// 发包
  } else {
    // 推荐使用不同信道 尽管这不是必须的
    // 收包 (发送端为客户端).
    event.player.sendData('test_channel_2', { test: 456 })// 发包
  }
})

// 监听从服务端收到网络包时的事件
// 这段是客户端脚本
onEvent('player.data_from_server.test_channel_1', event => {
  log.info(event.data.test) // 打印网络包信息到日志（123）
  event.player.tell(`来自客户端的网络包信息：${event.data.test}`)
})

// 监听从客户端收到网络包时的事件
// 这段是服务端脚本
onEvent('player.data_from_client.test_channel_2', event => {
  log.info(event.data.test) // 打印网络包信息到日志（456）
  event.server.runCommand(`say 来自服务端的网络包信息：${event.data.test}`)
})
```

## 二、Painter API

Painter API允许你在客户端屏幕上绘制内容，其可以从服务端控制也可以从客户端直接触发

目前（2022年5月10日）它不支持玩家输入/交互，但以后可能会支持游戏内菜单甚至是渲染引擎

Painter对象是以NBT/Json对象建立的，并且每一个都有自己的ID. 如果你没有提供一个的话，KubeJS就会随机生成一个. `x` 和 `z` 是对象在屏幕上的的绝对坐标, 但你能将元素对齐到屏幕的给定位置。你可以在一个json对象中添加多个对象. 所有的属性都是可选的，但是显然你至少应该填写坐标和大小等信息。

`paint({...})`遵守更新插入法则，如果对象不存在则创建，若存在则更新其属性：

```
event.player.paint({example: {type: 'rectangle', x: 10, y: 10, w: 20, h: 20}}); // 新建矩形
event.player.paint({example: {x: 50}}); // 更新先前ID为example的Painter对象的x属性
```

你可以批量新建/修改Painter对象：

```
event.player.paint({a: {x: 10}, b: {x: 30}, c: {type: 'rectangle', x: 10}});
```

你可以使用`remove`来移除Painter对象：

```
event.player.paint({a: {remove: true}}); // 移除ID为a的Painter对象
event.player.paint({a: {remove: true}, b: {remove: true}}); // 批量移除多个对象
event.player.paint({'*': {remove: true}}); //移除全部
```

还记得在1.1中提到的`/kubejs painter <玩家名称> <PainterJS对象>`指令吗？它和上述的函数功能是一样的

```
/kubejs painter @p {example: {type: 'rectangle', x: 10, y: 10, w: 20, h: 20}}
```

如果创建对象时多次重复出现，推荐在玩家登录时创建对象并为其设置属性`visible: false`，之后在合适时间将该属性改为true以取消隐藏。值得注意的是，Painter对象在玩家离开服务器时会被清除，所以你需要在每次玩家登录时重新为其添加Painter对象。

## 可用对象

Underlined objects are not something you can create

### 根

(可用于所有对象)

* Boolean visible

### 屏幕对象

(可用于所有屏幕/2D内容)

* Unit x // x坐标
* Unit y // y坐标
* Unit z // z坐标
* Unit w // 宽度
* Unit h // 高度
* Enum alignX (支持 'left', 'center', 'right') // x方向对齐参数
* Enum alignY (支持 'top', 'center', 'bottom') // y方向对齐参数
* Enum draw (支持 'ingame', 'gui', 'always') // 绘制场景
* Unit moveX
* Unit moveY
* Unit expandW
* Unit expandH

### 正方形

* Color color
* String texture
* Float u0
* Float v0
* Float u1
* Float v1

### 渐变（gradient）

* Color color
* Color colorT
* Color colorB
* Color colorL
* Color colorR
* Color colorTL
* Color colorTR
* Color colorBL
* Color colorBR
* String texture
* Float u0
* Float v0
* Float u1
* Float v1

### 文本

* Text text
* Boolean shadow
* Float scale
* Color color
* Boolean centered

**官方示例**

```
onEvent('player.logged_in', event => {
	// 初始化Painter对象
	event.player.paint({
		example_rectangle: {
			type: 'rectangle',
			x: 10,
			y: 10,
			w: 50,
			h: 20,
			color: '#00FF00',
			draw: 'always'
		},
		last_message: {
			type: 'text',
			text: 'No last message',
			scale: 1.5,
			x: -4,
			y: -4,
			alignX: 'right',
			alignY: 'bottom',
			draw: 'always'
		}
	})
})

onEvent('player.chat', event => {
	// 更新 example_rectangle 的 x 值 和 last_message 文本的值为上次聊天文本
	event.player.paint({example_rectangle: {x: '(sin((time() * 1.1)) * (($screenW - 32) / 2))', w: 32, h: 32, alignX: 'center', texture: 'kubejs:textures/item/diamond_ore.png'}})
	event.player.paint({last_message: {text: `Last message: ${event.message}`}})
	// 一次更新两个对象，这两种方式没有区别，你想用哪个都行
	// event.player.paint({example_rectangle: {x: 120}, last_message: {text: `Last message: ${event.message}`}})
	event.player.paint({lava: {type: 'atlas_texture', texture: 'minecraft:block/lava_flow'}})
})
```

![](https://m1.miaomc.cn/uploads/20220510\_5389abd49fab4.gif)

// TODO: 18.3 Painter API示例：服务器挖掘排行榜（显示挖掘方块最多的人）
