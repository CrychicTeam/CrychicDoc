# TaCZ的无限子弹
本章涉及内容：按键注册和使用、forgeevent、loadclass、paint、玩家数据
涉及模组及版本:
1. rhino-forge-2001.2.3-build.6
2. tacz-1.20.1-1.0.3-all
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.16
5. probejs-6.0.1-forge

## 完整代码 startup_scripts
### 主要部分
```js
const $GunShootEvent = Java.loadClass("com.tacz.guns.api.event.common.GunShootEvent")
const $Integer = Java.loadClass("java.lang.Integer")
const $ModSyncedEntityData = Java.loadClass("com.tacz.guns.entity.sync.ModSyncedEntityData")
const $AbstractGunItem = Java.loadClass("com.tacz.guns.api.item.gun.AbstractGunItem")

ForgeEvents.onEvent($GunShootEvent,event=>{global.gse(event)})
global.gse = event =>{
    try{
        // 处理事件，如果为客户端则不处理
        if(event.logicalSide.isClient()) return;
        let item = event.getGunItemStack();
        // 获取玩家数据，判断玩家是否处于无限子弹模式
        if(event.getShooter().data.get("frenzyOpen"))
            item.nbt.merge({GunCurrentAmmoCount:$Integer.decode​(
                item.nbt.getInt("GunCurrentAmmoCount")+1+""
            )});
    }catch(err){
        console.warn(err);
    }
}
```
该代码获取枪械物品的nbt，然后将子弹改为不消耗

### 注册按键
```js
const $KeyMappingRegistry = Java.loadClass("dev.architectury.registry.client.keymappings.KeyMappingRegistry");
const $KeyMapping = Java.loadClass("net.minecraft.client.KeyMapping");
const $GLFWkey = Java.loadClass("org.lwjgl.glfw.GLFW");

ClientEvents.init(event=>{
    global.regKeyFrenzy = new $KeyMapping(
        "key.meng.frenzy",
        $GLFWkey.GLFW_KEY_GRAVE_ACCENT,
        "key.keybinding.meng.special_abilities"
    );
    $KeyMappingRegistry.register(global.regKeyFrenzy)
})
```
[关于注册按键](./RegKey.md)

### 注册按键的汉化和处理 client_scripts
```js
ClientEvents.lang("zh_cn",e=>{
    e.add("key.meng.frenzy","狂暴模式")
    e.add("key.keybinding.meng.special_abilities","枪械技能")
})

ClientEvents.tick(event => {
    const key = global.regKeyFrenzy;
    if (key.isDown()) {
        if (!event.player.getPersistentData().getBoolean("frenzy")) {
            event.player.sendData("frenzy")
            event.player.getPersistentData().putBoolean("frenzy", true);
        }
    } else {
        if (event.player.getPersistentData().getBoolean("frenzy")) {
            event.player.getPersistentData().putBoolean("frenzy", false);
        }
    }
})
```

## 完整代码 server_scripts
```js
// 进度条的坐标
const x = "$screenW/1.4";
const textX = "$screenW/1.288"
const y = "$screenH/1.05";
const textY = "$screenH/1.083";
// 进度条的宽度
const h = 10;
// 技能cd
const cd = 20*8;
// 技能倍率
const cdMultiples = 2;
// 技能充能cd
const chargeCd = cd * cdMultiples;

PlayerEvents.tick(e=>{
    let cdn = 0;
    let player = e.player;
    let open = player.data.get("frenzyOpen");
    let n = player.data.get("frenzyCount");
    if (n <= chargeCd && !open){
        n++;
        cdn = (n / chargeCd) * 100;
    }else if(open && n > 0){
        n--;
        cdn = (n / cd) * 100;
    }else if(n <= 0){
        player.data.put("frenzyOpen",false)
    }

    if (cdn == 0 && n > 0){
        cdn = 100;
    }
    
    e.player.paint({
        // 背景色
        back_show: {
            type: 'rectangle',
            x: x, y: y, w: 100, h: h,
            color: '#f20c00'
        },
        // 背景上的颜色
        top_show: {
            type: 'rectangle',
            x: x, y: y, w: cdn, h: h,
            color: '#7ef218'
        },
        // 文本
        text_show: {
            type: 'text',
            text: "狂暴模式",
            x: textX, y: textY,
            
        }
    })

    player.data.put("frenzyCount",n);
})

// 处理按键
NetworkEvents.dataReceived("frenzy",event=>{
    let open = event.player.data.get("frenzyOpen")
    let n = event.player.data.get("frenzyCount");
    if (open){
        if (n > 0){
            n*= cdMultiples;
        }
    }else{
        n /= cdMultiples;
    }
    event.player.data.put("frenzyCount",n);
    event.player.data.add("frenzyOpen",!open)
})

// 在退出时获取到玩家的cd时间记录到持久化数据
PlayerEvents.loggedOut(event=>{
    event.player.persistentData.putInt("frenzyCount",event.player.data.get("frenzyCount"));
})

//在进入游戏时读取玩家持久化数据的cd时间
PlayerEvents.loggedIn(event=>{
    event.player.data.add("frenzyCount",event.player.persistentData.getInt("frenzyCount"));
})
```

## 一些注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
3. 注册按键不要打包给服务器(具体看关于按键注册的注释)