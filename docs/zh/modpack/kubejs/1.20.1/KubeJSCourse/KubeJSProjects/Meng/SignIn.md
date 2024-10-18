---
authors: ['Gu-meng']
---
# 服务器签到
本章涉及内容：玩家聊天事件、指令注册、JsonIO，本章所有代码部分都在`server_scripts`里

本章使用模组:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-7.0.1-forge

## 完整代码
这里得提前在对应的游戏文件路径下创建`meng`文件夹，然后创建一个空的`qd.json`
```js
ServerEvents.commandRegistry(event =>{
    const { commands: Commands, arguments: Arguments } = event
    event.register(
        Commands.literal("qd").executes(value =>{
            let username = value.source.getPlayer().username
            let qdJson = JsonIO.readJson("./meng/qd.json").getAsJsonObject()
            let time = qdJson.get("time").getAsString()
            let timeF = qdJson.get("timeF").getAsString()
            let players = qdJson.get("players").getAsJsonArray()
            let i = 0
            for (let index = 0; index < players.size(); index++) {
                let player = players.get(index).getAsJsonObject()
                let playerName = player.get("name").getAsString()
                if (playerName == username){
                    let endQdTime = player.get("endQdTime").getAsString()
                    if (timeCompare(endQdTime)){
                        let qdDay = player.get("qdDay").getAsNumber()
                        let newQdDay = qdDay + 1
                        player.addProperty("qdDay",newQdDay)
                        player.addProperty("endQdTime",new Date().getTime().toString())
                        let strDay = newQdDay.toString().split(".")[0]
                        Utils.server.tell("玩家 " + username + "在" + timeF + "签到成功，已经成功签到 " + strDay + "天")
                    }else{
                        value.source.getPlayer().tell("你已经在今天签到过了，无需重复签到")
                    }
                    i = 1
                    break
                }
            }
            if(i == 0){
                players["add(com.google.gson.JsonElement)"]({name:username,qdDay:"1.0",endQdTime:new Date().getTime().toString()})
                Utils.server.tell("玩家 " + username + "在" + timeF + "签到成功，已经成功签到 1 天" )
            }
            JsonIO.write("./meng/qd.json",qdJson)
            return 1
        })
    )
})


function getTime(str) {
    let currentTime = new Date(Number(str))
    return currentTime.getFullYear() + "年" + (currentTime.getMonth() + 1) + "月" + currentTime.getDate() + "日"
}

function timeCompare(str){
    let oldTime = new Date(Number(str))
    let newTime = new Date()
    return oldTime.getFullYear() < newTime.getFullYear() || 
            oldTime.getMonth() < newTime.getMonth() ||
            oldTime.getDate() < newTime.getDate()
}
```

## 注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)