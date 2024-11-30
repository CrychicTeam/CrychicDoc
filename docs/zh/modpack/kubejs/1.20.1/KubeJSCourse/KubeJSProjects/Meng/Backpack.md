# 注册背包
本章涉及内容：物品注册、nbt操作，饰品处理，按键注册
涉及模组及版本:
1. rhino-forge-2001.2.3-build.6
2. architectury-9.2.14-forge
3. kubejs-forge-2001.6.5-build.16
4. probejs-6.0.1-forge
5. curios-forge-5.10.0+1.20.1

## startup_scripts 代码
### 注册物品
```js
event.create("meng:backpack")
    .maxStackSize(1)
    .tag("curios:back"); // 这一行是饰品，如果不需要可以删掉
```
### 注册按键
```js
ClientEvents.init(() => {
  global.regKeyB = new $KeyMapping(
    "key.meng.packsack",
    $GLFWkey.GLFW_KEY_B,
    "key.keybinding.packsack"
  );
  $KeyMappingRegistry.register(global.regKeyB);
}); 
```

### 按键处理 client_scripts
```js
ClientEvents.tick(event => {
    const key = global.regKeyB;
    if (key.isDown()) {
        if (!event.player.getPersistentData().getBoolean("openBackpack")) {
            event.player.sendData("openBackpack")
            event.player.getPersistentData().putBoolean("openBackpack", true);
        }
    } else {
        if (event.player.getPersistentData().getBoolean("openBackpack")) {
            event.player.getPersistentData().putBoolean("openBackpack", false);
        }
    }
})
```

## server_scripts 代码
### 打开背包和背包关闭时的方法
```js
// priority: 5

const backpack = "meng:backpack";
const dataBackpack = "backpack";
const dataBackpackItem = dataBackpack + "Item";
/**
 * 背包关闭，将背包里的物品写入到背包nbt
 * @param {*} inventoryContainer 
 * @param {Internal.ItemStack} backpackItem 
 */
function backpackFunc(inventoryContainer,backpackItem) {
    let n = 0;
    if (inventoryContainer.getItems().size() == 90) n = 54;
    let list = [];
    for (let i = 0; i < n; i++) {
        let item = inventoryContainer.getSlot(i).getItem();
        if (item.is("air")) continue;
        list.push({ item: item.id, count: item.count, slot: i, nbt: item.nbt })
    }
    backpackItem.nbt.merge({ "items": list })
}
/**
 * 打开背包的函数
 *
 */
function openBackpackFunc(player, item) {
    if (!item.hasNBT()) item.setNbt({ items: [] })
    let nbt = item.getNbt();
    let itemList = nbt.get("items");
    if (itemList == undefined) item.nbt.merge({ items: [] });

    player.openMenu(new $SimpleMenuProvider((i, inv, p) => {
        let chest = $ChestMenu.sixRows(i, inv)
        for (let i = 0; i < itemList.size(); i++) {
            let value = itemList.get(i)
            chest.getSlot(value.slot).set(Item.of(value.item, value.count, value.nbt));
        }
        player.data.add(dataBackpack, chest.hashCode().toString())
        return chest
    }, Text.of(item.displayName).yellow()))
}
```
### 打开背包的代码
```js
ItemEvents.firstRightClicked(backpack, event => {
    let { player, item } = event
    openBackpackFunc(player,item);
})


NetworkEvents.dataReceived("openBackpack", event => {
    const player = event.player
    if (player.data.get(dataBackpack) == undefined) {
        let opItem = $CuriosApi
            .getCuriosHelper()
        ["findFirstCurio(net.minecraft.world.entity.LivingEntity,net.minecraft.world.item.Item)"](player, backpack);
        try {
            let item = opItem.get().stack();
            openBackpackFunc(player,item);
        } catch (err) {
            console.warn(err);
            player.tell(Text.translate("tell.meng.openBackpack.curiosapi"))
        }
    }
})
```
### 关闭背包的代码
```js
PlayerEvents.chestClosed(event => {
    let { player, inventoryContainer } = event
    if (player.data.get(dataBackpack) == inventoryContainer.hashCode().toString()) {
        let handItem = player.getMainHandItem();
        player.data.remove(dataBackpack)
        // player.data.remove(dataBackpackItem)
        if (handItem.is(backpack)) {
            backpackFunc(inventoryContainer, handItem)
            return;
        }
        let opItem = $CuriosApi
            .getCuriosHelper()
            ["findFirstCurio(net.minecraft.world.entity.LivingEntity,net.minecraft.world.item.Item)"]
                (player, backpack);
        let curiosItem = opItem.get().stack();
        if (curiosItem.is(backpack)) {
            backpackFunc(inventoryContainer, curiosItem)
            return;
        }
    }
})
```

## 一些注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
3. 注册按键不要打包给服务器(具体看关于按键注册的注释)