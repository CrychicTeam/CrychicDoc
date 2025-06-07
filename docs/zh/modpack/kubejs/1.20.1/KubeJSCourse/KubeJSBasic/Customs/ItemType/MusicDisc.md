---
authors:
  - Gu-meng
  - QiHuang02
editor: Gu-meng
---
# 添加唱片

在本章中会举例如何创建一个可以播放音乐的唱片，本章所有js代码将会存放于在文件夹`startup_scripts`和`server_scripts`下

## 添加声音

在添加唱片之前，我们需要先添加声音，不然我们的唱片就是一个哑巴唱片，播放不了我们自己的音乐

```js
StartupEvents.registry("sound_event", (event) => {
    event.create("meng:music.my_music")
})
```

只需要上面这一行就可以将音乐注册上去，参数为id

### 注意：在 1.21.1+ 时

在 1.21.1+ 时我们想要注册一个唱片，我们还得在 `server` 事件中使用以下语句，将刚才注册的音频注册为唱片音频

```js
ServerEvents.registry('jukebox_song', (event) => {
    event.create('music_id')
         .song('sound_event_id', time)

    // 例子
    event.create('meng:my_music')
         .song("meng:music.my_music", 103)
})
```

`music_id` 为这段音频名字，可以是任何你想填的，不过得保持和之后的一致性

`sound_event_id` 就是你在启动事件中注册的音频的id

`time` 很好理解就是指插片的时间。

## 注册物品

接下来我们就可以来注册唱片了

```js
StartupEvents.registry("item", (event) => {
    event.create("meng:my_music_disc", "music_disc")
        .song("meng:music.my_music", 103)
        .tag("music_discs")
})
```

`.song()`里的第一个参数为 注册声音的id,第二个参数为 音乐的时长，这里是用秒计算

`.tag("music_discs")` 这个是**一定要写**的，告诉游戏这个物品属于唱片tag，这样唱片机才能正常的被使用

### 注意：在 1.21.1+ 时

```js
StartupEvents.registry('item', (event) => {
    event.create('item_id')
         .jukeboxPlayable('music_id', true)

    // 例子
    event.create('meng:my_music')
         .jukeboxPlayable('meng:my_music', true)
})
```

`item_id` 是物品的id，请不要与之前的唱片音频id混淆，但是我建议都设置为一样，就不用在意使用混乱的问题了

`music_id` 就是刚才注册唱片音频时的 `music_id`

因为自 Minecraft 1.20.5 开始, 使用了[物品堆叠组件](https://minecraft.wiki/w/Data_component_format)的缘故，基本上所有物品都可以注册为唱片（大概）

## 准备音乐

在注册完唱片和声音后，我们需要开始将音乐添加进来了

首先我们需要进入这个网站 [mp3->ogg](https://audio.online-convert.com/convert-to-ogg)

我的世界的声音文件都是`.ogg`格式的，所以需要将mp3转换为ogg格式

### 创建声音文件夹

之后我们需要创建文件夹将音乐存入进来

将ogg文件存入路径为`kubejs/assets/meng/sounds`里，这里`meng`就是上面冒号前的字符，我这边是`meng`，所以实际路径为`kubejs/assets/meng/sounds`,如果没有写`meng`，那么路径则为`kubejs/assets/kubejs/sounds`

需要将文件重命名，这里最好是和音乐id的`music.`后面相同(当然这里可以自定义，但是不建议),所里这里我的文件名为`my_music.ogg`,完整路径为`kubejs/assets/meng/sounds/my_music.ogg`

### 编写寻找声音文件

返回到路径为`kubejs/assets/<namespace>`下，创建一个名为`sounds.json`的文件，打开进行编辑

[详细编写可以查看mcwiki](https://zh.minecraft.wiki/w/Sounds.json?variant=zh-cn)

```json
{
    // 注册声音id
    "music.my_music": {
        // 固定格式
        "sounds": [
            {
                // 创建声音的文件夹 也就是我们上面的 kubejs/assets/<namespace>/sounds
                "name": "meng:my_music",
                // 以声音流输出，建议填写true(唱片)
                "stream": true
            }
        ]
    },
}
```

### 在 1.21.1+ 时

```json
{
    // 这里的 'meng:my_music' 指的是刚才在注册唱片音频那里的 `music_id`
    "music.my_music": {
        // 固定格式
        "sounds": [
            {
                // 创建声音的文件夹 也就是我们上面的 kubejs/assets/meng/sounds
                "name": "meng:my_music",
                // 以声音流输出，建议填写true(唱片)
                "stream": true
            }
        ]
    },
}
```

[关于sounds.json的文件结构](../../../Digression/SoundsJson)

## 本地化和材质

接下来就是最简单的对物品进行汉化，让物品显示中文文本

进入路径`kubejs/assets/meng/lang` 创建文件`zh_ch.json` (如果有直接打开就行)

编写或添加文本

```json
{
    "item.meng.my_music": "音乐唱片",
    "item.meng.my_music.desc": "我的音乐 - 私人歌手"
}
```

### 在 1.21.1+ 时

```json
{
    "item.meng.mymusic": "音乐唱片",
    "jukebox_song.meng.mymusic": "我的音乐 - 私人歌手",
    "sound.meng.mymusic": "我的音乐"
}
```

第一个为唱片本身的文本信息

第二个为唱片的音乐信息

材质路径为`kubejs/assets/meng/textures/item` 将已经画好的唱片材质放在该路径下并命名为`my_music.png` 这里和物品id名称一样

## 注意事项和音乐下载

如果你上面做的都没问题，唱片也能成功放进唱片机，代码也没报错，但是就是没声音，建议先用孤梦[提供的音乐](https://gitee.com/gumengmengs/kubejs-course/blob/main/files/my_music.ogg) 如果链接打不开或者无法下载，在[哔哩哔哩私信孤梦](https://space.bilibili.com/16632546)
