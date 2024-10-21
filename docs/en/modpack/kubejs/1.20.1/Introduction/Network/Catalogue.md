# NetworkEvent

NetworkEvent is used for sending and receiving data between the server and client. This is particularly useful when you need to synchronize information or trigger cross-side operations.

## Sending Data

To send data, use the `sendData` method:

```d.ts
sendData(channel: string, data: Internal.CompoundTag_): void;
```

Parameters:
- `channel`: A string that identifies the data channel. The receiving end will use this name to listen for data.
- `data`: A CompoundTag type containing the data to be transmitted.

Example: Sending data about the player's main hand item to the server when the player types in the chat:

```js
PlayerEvents.chat(event => {
    const { player } = event
    const mainHandItem = player.getMainHandItem();
    player.sendData('item_sync', { item: mainHandItem.getId() })
})
```

## Receiving Data

To receive data, use the `NetworkEvents.dataReceived` event:

```js
const ItemStack = Java.loadClass('net.minecraft.world.item.ItemStack')

NetworkEvents.dataReceived('item_sync', event => {
    const itemId = event.data.get('item')
    const item = new ItemStack(itemId)
    Client.gameRenderer.displayItemActivation(item)
})
```

This example demonstrates how to receive data on the 'item_sync' channel on the client side and display the corresponding item.

## Important Considerations

1. Ensure that the sending and receiving sides use the same channel name.
2. Data transmission should be as concise as possible, only sending necessary information.
3. When handling received data, consider cases where the data might not exist or could be in an incorrect format.

By effectively using NetworkEvent, you can implement complex cross-side interaction features.
