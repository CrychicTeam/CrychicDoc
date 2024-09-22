---
authors: ['Wudji']
---

# 12.4 KubeJS UI

***

```
onEvent('ui.main_menu', event => {
  event.replace(ui => {
    ui.tilingBackground('kubejsui:textures/example_background.png', 256)//设置背景
    ui.minecraftLogo(30)//设置logo
    
    ui.button(b => {//新建按钮
      b.name = 'Test'
      b.x = 10 
      b.y = 10
      b.action = 'minecraft:singleplayer'
    })
    
    ui.button(b => {
      b.name = 'Test but in bottom right corner'
      b.x = ui.width - b.width - 10 
      b.y = ui.height - b.height - 10
      b.action = 'https://feed-the-beast.com/'
    })
    
    ui.label(l => {//新建 lable
      l.name = text.yellow('FTB Stranded')
      l.x = 2
      l.y = ui.height - 12
      l.action = 'https://feed-the-beast.com/'
    })

    ui.image(i => {//新建图片
      i.x = (ui.width - 40) / 2
      i.y = (ui.height - 30) / 2
      i.width = 40
      i.height = 30
      i.action = 'https://feed-the-beast.com/'
    })
    
    ui.label(l => {
      l.name = text.aqua('Large label')
      l.x = 100
      l.y = ui.height - 20
      l.height = 15
      l.shadow = true // 字体是否带阴影
    })
  })
})
/*
	常用的值：
	ui.height  <=> 页面高度
	ui.width   <=> 页面宽度
	x.action   <=> 点击时执行的操作
	x.name 	   <=> 该项的显示名称
*/
```

![](https://m1.miaomc.cn/uploads/20220112\_d97e768a1fe60.png)
