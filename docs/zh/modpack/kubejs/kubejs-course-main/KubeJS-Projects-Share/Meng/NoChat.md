# 玩家禁言
本章涉及内容：玩家聊天事件、指令注册、JsonIO，本章所有代码部分都在`server_scripts`里

本章使用模组:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-7.0.1-forge

## 指令注册
```js
// 注册给玩家禁言的指令
ServerEvents.commandRegistry(event => {
    const { commands: Commands, arguments: Arguments } = event
    event.register(
        Commands.literal("jy")
            .then(
                Commands.argument('playerName', Arguments.PLAYER.create(event))
                    .then(
                        Commands.argument('minute', Arguments.INTEGER.create(event))
                            .executes(value => {
                                const player = Arguments.PLAYER.getResult(value, "playerName")
                                const minute = Arguments.INTEGER.getResult(value, "minute")
                                console.log(player + minute);
                                setPlayerNoChat(player.username,minute)
                                player.tell("你被管理员禁言 " + minute + "分钟")
                                value.source.player.tell(player.username + "被您禁言 " + minute + " 分钟" )
                                return 1
                            })
                    )
            )
    )
})
```

## 禁言逻辑性代码
```js
const { $JsonArray } = require("packages/com/google/gson/$JsonArray")
const { $JsonObject } = require("packages/com/google/gson/$JsonObject")
//文件路径
const fileUrl = "./meng/jy.json"

function setPlayerNoChat(player, time) {
    let json = JsonIO.readJson(fileUrl)
    let arr = json.get("data").getAsJsonArray()
    /**
     * @type {$JsonObject}
     */
    let playerValue = getFilePlayer(arr,player)
    console.log(playerValue == null);
    if (playerValue == null) {
        addNoChat(player, time, json, new Date().getTime().toString())
    } else {
        time += playerValue.get("time").asInt
        console.log(time);
        updateNoChat(json,player, time)
    }
}

function getPlayerChatState(player){
    let json = JsonIO.readJson(fileUrl)
    let arr = json.get("data").getAsJsonArray()
    let playerValue = getFilePlayer(arr,player)
    if (playerValue != null) {
        if (playerValue.get("isNoChat").asBoolean){
            let value = compareTimestamps(
                playerValue.get("NoChatTime").asString,
                playerValue.get("time").asInt,
            )
            if (value){
                return true
            }else{
                updateNoChat(json,player,-1)
            }
        }
    }
    return false
}
/**
 * 判断是否可以解除禁言
 * @param {*} currentTimestamp 时间戳
 * @param {*} minutes 分钟
 * @returns true为未来 false为过去
 */
function compareTimestamps(currentTimestamp, minutes) {
    const currentDate = new Date(Number(currentTimestamp));
    const futureDate = new Date(currentDate.getTime() + minutes * 60000);
    const futureTimestamp = futureDate.getTime();
    const newDate = new Date().getTime()
    return newDate < futureTimestamp ? true : false;
}

/**
 * 设置禁言
 */
function addNoChat(player, time, file, dateString) {
    let arr = file.get("data").getAsJsonArray()
    /**
     * @type {$JsonArray}
     */
    arr["add(com.google.gson.JsonElement)"]({
        playerName: player,
        isNoChat: true,
        time: time,
        NoChatTime: dateString
    })
    JsonIO.write(fileUrl, file)
}

/**
 * 更新禁言
 */
function updateNoChat(file, player, time) {
    let arr = file.get("data").getAsJsonArray()
    for (let index = 0; index < arr.size(); index++) {
        /**
         * @type {$JsonObject}
         */
        let obj = arr.get(index)
        if (obj.get("playerName").asString == player) {
            if (time == -1) {
                obj.add("time", 0)
                obj.add("isNoChat", false)
            } else {
                if (!obj.get("isNoChat").asBoolean){
                    obj.add("NoChatTime",new Date().getTime().toString())
                }
                obj.add("time", time)
                obj.add("isNoChat", true)
            }
        }
    }
    JsonIO.write(fileUrl, file)
}

/**
 * 获取禁言表里玩家是否存在
 * @returns 返回禁言列表
 */
function getFilePlayer(arr,playerName) {
    for (let index = 0; index < arr.size(); index++) {
        if (arr.get(index).get("playerName").asString == playerName){
            return arr.get(index)
        }
    }
    return null
}
```

## 玩家聊天禁言
```js
PlayerEvents.chat(event=>{
    if (getPlayerChatState(event.getUsername())){
        event.getPlayer().tell("你不许说话,你被禁言了")
        event.cancel()
    }
})
```

## 注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)