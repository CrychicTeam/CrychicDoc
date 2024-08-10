---
layout: doc
title: Crychic文档编写示例
order : 6
prev:
  text: 类型检查示例
  link: './test'
next:
  text: 项目合作教程
  link: './docDevelop'
authors:
  - M1hono
---

## Mermaid示例

<ClientOnly>

```mermaid
journey
    title My working day
    section Go to work
      Make tea: 5: Me
      Go upstairs: 3: Me
      Do work: 1: Me, Cat
    section Go home
      Go downstairs: 5: Me
      Sit down: 5: Me
```

## 时间线插件示例

</ClientOnly>

::: timeline 2023-05-24
- **do some thing1**
- do some thing2
:::

::: timeline 2023-05-23
do some thing3
do some thing4
:::

## B站视频示例

<BilibiliVideo bvid="BV1rC4y1C7z2" />

## 伤害静态图示例

<ClientOnly>
<DamageChart
  mod="static"
  :incomingDamage="20"
  :armorToughness="5"
  :minDamage="4"
  :maxDamage="20"
  :maxArmorPoints="20"
  :isJavaEdition="true"
/>
</ClientOnly>