# NetworkEvent

NetworkEvent 用于在服务器与客户端之间发送和接收数据。这在需要同步信息或触发跨端操作时非常有用。

## 发送数据

要发送数据,请使用 `sendData` 方法:

```
sendData(channel: string, data: Internal.CompoundTag_): void;
```

参数说明:
- `channel`: 字符串,用于标识数据通道,接收端将使用此名称来监听数据。
- `data`: CompoundTag 类型,包含要传输的数据。

示例:当玩家在聊天框中输入信息时,向服务器发送玩家主手中的物品数据:

```js
PlayerEvents.chat(event => {
    const { player } = event
    const mainHandItem = player.getMainHandItem();
    player.sendData('item_sync', { item: mainHandItem.getId() })
})
```

## 接收数据

要接收数据,使用 `NetworkEvents.dataReceived` 事件:

```js
const ItemStack = Java.loadClass('net.minecraft.world.item.ItemStack')

NetworkEvents.dataReceived('item_sync', event => {
    const itemId = event.data.get('item')
    const item = new ItemStack(itemId)
    Client.gameRenderer.displayItemActivation(item)
})
```

这个例子展示了如何在客户端接收 'item_sync' 通道的数据,并显示相应的物品。

## 注意事项

1. 确保发送和接收使用相同的通道名称。
2. 数据传输应该尽量精简,只发送必要的信息。
3. 处理接收到的数据时要考虑数据可能不存在或格式不正确的情况。例如在上述示例中，当我们传入一个item变量时，他会被转化为CompoundTag，确保你传入的数据可以帮你获取到实际需要的变量。

通过合理使用 NetworkEvent,你可以实现简单的跨端交互功能。
