## 物品提示
在游戏内可以看到锻造模板又或者附魔书下面会有一行文本用于提示，这个是什么什么附魔书或者什么什么模板，本章将会教大家如何去实现该效果
### 实现操作
首先这个属于渲染事件所以需要写在`client_scripts`文件夹内

```js
ItemEvents.tooltip((event) =>{
    //添加一个最普通的文本，这个文本是在最下面进行显示的
    event.add('diamond', "添加一个普通文本")
    //用数组添加文本，在游戏内数组的每个文本都独占一行
    event.add('diamond', ["数组文本1","数组文本2"])
    //当你需要拼接字符串时可以使用下面方法
    event.add('diamond', Text.of("该物品现属于").append(Client.player.username))
    //当你需要改变文本颜色时,只需要在后面添加一个颜色参数就可以
    event.add('diamond', Text.of("该物品现属于").append(Client.player.username).red())
})
```


上面只是最简单的操作实现，当你需要一些特殊按键时才能看到的文本就需要用到下面方法
```js
ItemEvents.tooltip((event) =>{
    event.addAdvanced("diamond", (item,advanced,text) =>{
        //我们也可以直接删掉位置的文本，直接替代它！
        //这里删除第0位是物品名的位置，所以删掉之后没有名字了
        //这里添加一个新的文本在第0的位置上，这样就有名字了
        text.remove(0)
        text.add(0,"普通的钻石")
        //下面只是一个简单的彩色文本拼接
        text.add(1,Text.red("当").append(Text.gold("你")).append(Text.darkBlue("需")).append(Text.blue("要")).append(Text.white("彩")).append(Text.green("色")).append(Text.gray("文")).append(Text.yellow("本")))

        //当按住shift时看到的文本
        if (event.shift){
            text.add(Text.red("你终于按住shift看我啦"))
        //是的,可以写成组合按键，但是得注意，组合按键得写在单个按键的上面，不然会优先处理单个按键
        }else if(event.alt && event.ctrl){
            text.add(Text.darkPurple("你发现了一个新的组合?!"))
        //当按住ctrl时看到的文本
        }else if(event.ctrl){
            text.add(Text.yellow("你终于按住ctrl看我啦"))
        //当按住alt时看到的文本
        }else if(event.alt){
            text.add(Text.blue("你终于按住alt看我啦"))
        }
    })
})
```



### 本地化
可以发现**tooltip**的可玩性非常的高,当然，如果你的整合包准备走向国际化，我们的本地化也是可以实现的

首先你得像写材质包一样在`assets`文件夹下创建一个材质包路径，然后创建一个`lang`文件夹,然后再进行翻译的书写,我这里的路径为：`assets/meng/lang`

这里我的`zh_cn.json`是这样写的
```json
{
    "meng.lang.wenben.test" : "这是一个测试文本"
}
```
`en_us.json`是这样写的
```json
{
    "meng.lang.wenben.test" : "This is a test text"
}
```

代码是下面这样写的
```js
ItemEvents.tooltip((event) =>{
    event.add("diamond",Text.translate("meng.lang.wenben.test"))
})
```

这样就可以在切换不同语言时，显示的文本就是不一样的