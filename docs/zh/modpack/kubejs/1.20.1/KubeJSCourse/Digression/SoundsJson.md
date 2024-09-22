---
authors: ['Gu-meng']
---
# sounds.json 结构
**注：以下内容来自mcwiki**
```json
{
    "声音注册id": {
        "replace": (true / false),
        "subtitle": "播放音乐",
        "sounds":[
            {
                "name":"声音路径",
                "volume": (0.0 ~ 1.0),
                "pitch": 1.0,
                "weight": 1,
                "stream": (true / false),
                "attenuation_distance": 32,
                "preload": (true / false),
                "type": ("file" / "event")
            }
        ]
    }
}
```
声音注册id ： 一个声音事件。声音事件名通常是按照类别以点（.）分割的（例如entity.enderman.stare）。下方有表格列出了所有游戏自带的声音事件。（如果你想要为该声音事件设定minecraft以外的命名空间，应当把sounds.json文件放置在相应命名空间的目录下，而不是在这里定义。）

replace ：可选。如果设定为true，在sounds中定义的音效列表会替换掉优先级更低的资源包中为该声音事件定义的音效。设定为false则会补充到原来的音效列表中，而不是直接替换。如果不指定，默认为false。

subtitle ： 可选。如果游戏中开启了“显示字幕”选项，游戏将会在该声音事件被播放时将该字符串翻译为声音字幕。

sounds ： 可选。该声音事件使用的音效文件列表。当该声音事件被触发时，游戏会从此列表定义的音效文件中随机选取一个播放。

name ： 从assets/<命名空间>/sounds文件夹到此声音文件的路径，或者是另一个声音事件的命名空间ID。

volume ： 播放此声音时的音量。值为0.0到1.0的小数。未定义时默认为1.0。

pitch ： 以确定的值播放音调。未定义时默认为1.0。可以调至更高或更低。

weight ：此声音事件触发时此声音被播放的相对权重。默认为1。例如，设为2相当于此文件在列表中出现两次。

stream ：设置为true则此声音会以流式播放。当声音较长时最好设为true来避免卡顿。所有的music和record分类的音效（除了音符盒）都使用流式播放，因为它们的长度都达到一分多钟（最长达八分多钟）。未指定则默认为false。

attenuation_distance ：基于距离的音效大小衰减率。用于传送门、信标和潮涌核心。默认为16。

preload ：若设置为true，则该音效文件会在加载资源包时就加载，而不是在播放音效的时候再加载。用于水下环境音效。默认为false。

type ：可选file或event。file表明name中定义的是文件名，event表明name中定义的是声音事件名。未指定则默认为file。