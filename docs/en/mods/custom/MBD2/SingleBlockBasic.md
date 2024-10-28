---
description: Simple single block machine design
state: unfinished
progress: 10
backPath: ./Catalogue
---

# Single Block Machine {#SingleBlockMachine}

`MBD2` provides two types of machines. This article briefly explains one of them and focuses on how to simply complete the functionality of a single block machine, without involving complex modules.

## Working State {#WorkingState}

Most effects in `MBD2` are designed and implemented based on the machine's state. You can see the specific logic through the state diagram below.

The state on the left is always the `parent` state of the state on the right. The `parent` state represents an inheritance relationship. Usually, some basic settings of the child class will ==inherit from the `parent class`==, such as model rendering, etc.

::: center

```mermaid
---
title: Machine Operation Cycle
---
flowchart TB
    A[Machine] -->|Starting| B[Base State]
    B -->|Working conditions met| C[Working]
    B -->|Working conditions not met| G[Suspended]
    B -->|Assembled as multiblock| F[Formed]

    subgraph Workflow ["Workflow"]
    direction LR
    C --> D[Waiting]
    C -->|per tick ingredient shortage or recipe condition failure| D
    end

    subgraph Special States ["Special States"]
    direction LR
    G
    F
    end

    classDef working fill:#f9f,stroke:#333,stroke-width:2px,color:#000;
    classDef special fill:#ffcc00,stroke:#333,stroke-width:2px,color:#000;
    class C,D working;
    class G,F special;

    linkStyle 0 stroke:#f66,stroke-width:2px;
    linkStyle 1 stroke:#f66,stroke-width:2px;
    linkStyle 2 stroke:#f66,stroke-width:2px;
    style A fill:#fff,stroke:#333,stroke-width:2px,color:#000;
    style B fill:#fff,stroke:#333,stroke-width:2px,color:#000;
    style D fill:#fff,stroke:#333,stroke-width:2px,color:#000;
```

This can be simply understood as the **`brain`** of this machine.
:::

## Settings {#Options}

:::outlined
This section will focus more on the introduction of various modules and the general process of use. Not all parameters will be mentioned. More specific documentation can be viewed here (currently empty).
:::

Each module has three different `general configurators`:

1. [Basic Configuration](./SingleBlockBasicSettings): Generally the most basic configuration for a certain object, such as the `block properties` of the machine or the `event trigger nodes` are the basic configurations of the module, while basic elements like `UI` are also modified using the basic configurator.

2. [Other Configuration](): Generally the unique configuration of a certain object, for example, when configuring a single working state in the basic settings, other configurations will be opened.

3. [Resource Configuration](): Double-click on `Rendering`, `Texture`, or `Color` in the resource area to configure these resources individually.

Each module has different configurator functions and different configuration items. At the same time, ==when the item the player is configuring does not have a corresponding configurator, the configurator will not display any options==.
