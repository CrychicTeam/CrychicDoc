---
title: 44 如何添加模组和lib
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 44 如何添加模组和lib 相关教程
image: ./covers/908b0f9eb44b349604fc2f5b3ff855374bc23664.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 添加使用模组和附属

这次我们怎么给你的工程项目添加一些模组，用于辅助我们的开发，例如添加一个jei，来查看配置文件，或者给jei添加一些支持，让jei显示自己的合成表。

为我们也会添加一个依赖的库lib，我们使用这个库辅助我们的给主世界添加新的生物群系，不过这样也会导致我们的模组以来于该库lib的模组，不过这确实简化了我们的开发，对于游玩该模组的人无非也就是多添加了一个lib模组（不过这确实有时候是烦人的）。

## jei

我们先来看怎么添加jei。

关于jei，github上的wiki上有关于的配置的方法，这里我们安装wiki上的讲解的内容配置一遍。

https://github.com/mezz/JustEnoughItems/wiki/Getting-Started-%5BJEI-for-Minecraft-1.20.4--for-NeoForge,-Forge,-or-Fabric%5D


第一步打开你的build.gradle文件，在其中Repositories下面添加以下的字段，这个表示将jei所作为的仓库添加到我们的工程中。 

```java
repositories {
  maven {
    // location of the maven that hosts JEI files since January 2023
    name = "Jared's maven"
    url = "https://maven.blamejared.com/"
  }
  maven {
    // location of a maven mirror for JEI files, as a fallback
    name = "ModMaven"
    url = "https://modmaven.dev"
  }
}

```

第二步还是在build.gradle文件中，在Dependencies 字段添加对于jei的依赖

```java
dependencies {
  /* other minecraft dependencies are here */

  // compile against the JEI API but do not include it at runtime
  // compile 表示在编译阶段需要该库，而在运行时不需要
  compileOnly("mezz.jei:jei-${mc_version}-common-api:${jei_version}")
  compileOnly("mezz.jei:jei-${mc_version}-neoforge-api:${jei_version}")
  // runtimeOnly 表示在运行时需要该库。
  // at runtime, use the full JEI jar for NeoForge
  runtimeOnly("mezz.jei:jei-${mc_version}-neoforge:${jei_version}")
}
```

其中的mc_version以及jei_version我们等会写在gradle.properties文件下。

显然mc_version就是游戏版本，jei_version就是jei的版本，关于jei的版本可以到curseforge上查看。

```java

mc_version=1.20.4
jei_version=17.0.0.30

```

最后点击reload gradle或者load gradle change，下载jei的依赖。

然后启动游戏到游戏看看即可。和我们平时使用jei没什么区别。对于其他的模组的添加流程大致相同，仓库可以使用curseforge提供的仓库，或者modirth也提供了一个仓库。

这里给出curseforge仓库的使用方法。

https://cursemaven.com/

添加仓库地址

```java
repositories {
    maven {
        url "https://cursemaven.com"
    }
}
```

添加依赖
```java
curse.maven:project-11111:12345
```

其中的project id：11111以及fileids：12345的查看方法去看https://cursemaven.com
下面的图片。

例如jei的,不过这里的字段需要实际上根据你的项目的gradle类型来写，例如neoforge的就安装上面的写。其他的查看cursemaven上面的例子。
```java
dependencies {
    api "curse.maven:jei-238222:2724420"
}
```

## TerraBlender lib

添加仓库
```java
repositories {
    mavenLocal()
    mavenCentral()
    maven { url = 'https://maven.minecraftforge.net/' }
    maven {
        url "https://www.cursemaven.com"
        content {
            includeGroup "curse.maven"
        }
    }
}

```

添加依赖

```java
    compileOnly "com.github.glitchfiend:TerraBlender-neoforge:${minecraft_version}-${terrablender_version}"
    runtimeOnly "com.github.glitchfiend:TerraBlender-neoforge:${minecraft_version}-${terrablender_version}"
```

在build.properties下配置我们的版本,mc_version已经配置了，所以这里直接写terrblender_version既可以了。

```java
terrablender_version=3.3.0.12
```

最后点击reload gradle或者load gradle change，下载对应的依赖。

好了之后的教程我们使用TerraBlender给主世界添加生物群系biome

