---
layout: doc
title: Samples for coding CrychicDoc
order : 6
prev:
  text: ts-check Sample
  link: './test'
next:
  text: Project Collaboration Tutorial
  link: './docDevelop'
authors:
  - M1hono
---

## Mermaid Sample

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

## Timeline plugin sample.

</ClientOnly>

::: timeline 2023-05-24
- **do some thing1**
- do some thing2
:::

::: timeline 2023-05-23
do some thing3
do some thing4
:::

## Youtube Video sample

<YoutubeVideo videoId="IL7J9ueYRYc" />

## Damage Static chart

<ClientOnly>
<!--  -->
<DamageChart
  mode="static"
  :incomingDamage="20"
  :armorToughness="5"
  :minDamage="4"
  :maxDamage="20"
  :maxArmorPoints="20"
  :isJavaEdition="true"
/>
</ClientOnly>