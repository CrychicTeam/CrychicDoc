declare module "packages/com/structureessentials/config/$CommonConfiguration" {
import {$ICommonConfig, $ICommonConfig$Type} from "packages/com/cupboard/config/$ICommonConfig"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export class $CommonConfiguration implements $ICommonConfig {
 "structurePlacementLogging": boolean
 "structureSearchTimeout": integer
 "useFastStructureLookup": boolean
 "warnMissingRegistryEntry": boolean
 "disableLegacyRandomCrashes": boolean

constructor()

public "deserialize"(arg0: $JsonObject$Type): void
public "serialize"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonConfiguration$Type = ($CommonConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonConfiguration_ = $CommonConfiguration$Type;
}}
declare module "packages/com/structureessentials/$StructureEssentials" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$CupboardConfig, $CupboardConfig$Type} from "packages/com/cupboard/config/$CupboardConfig"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$CommonConfiguration, $CommonConfiguration$Type} from "packages/com/structureessentials/config/$CommonConfiguration"

export class $StructureEssentials {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static "config": $CupboardConfig<($CommonConfiguration)>
static "rand": $Random

constructor()

public "commandRegister"(arg0: $RegisterCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureEssentials$Type = ($StructureEssentials);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureEssentials_ = $StructureEssentials$Type;
}}
declare module "packages/com/structureessentials/command/$Command" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"

export class $Command {

constructor()

public "build"(arg0: $CommandBuildContext$Type): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Command$Type = ($Command);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Command_ = $Command$Type;
}}
declare module "packages/com/structureessentials/mixin/$MixinConfig" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MixinConfig implements $IMixinConfigPlugin {

constructor()

public "onLoad"(arg0: string): void
public "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(arg0: string, arg1: string): boolean
public "preApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "acceptTargets"(arg0: $Set$Type<(string)>, arg1: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinConfig$Type = ($MixinConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinConfig_ = $MixinConfig$Type;
}}
