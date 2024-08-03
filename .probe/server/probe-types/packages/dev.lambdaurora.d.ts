declare module "packages/dev/lambdaurora/lambdynlights/api/$DynamicLightsInitializer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DynamicLightsInitializer {

 "onInitializeDynamicLights"(): void

(): void
}

export namespace $DynamicLightsInitializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicLightsInitializer$Type = ($DynamicLightsInitializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicLightsInitializer_ = $DynamicLightsInitializer$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/config/$DynamicLightsConfig" {
import {$ForgeConfigSpec$EnumValue, $ForgeConfigSpec$EnumValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$EnumValue"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$QualityMode, $QualityMode$Type} from "packages/dev/lambdaurora/lambdynlights/config/$QualityMode"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"

export class $DynamicLightsConfig {
static "ConfigSpec": $ForgeConfigSpec
static "Quality": $ForgeConfigSpec$EnumValue<($QualityMode)>
static "EntityLighting": $ForgeConfigSpec$ConfigValue<(boolean)>
static "TileEntityLighting": $ForgeConfigSpec$ConfigValue<(boolean)>
static "OnlyUpdateOnPositionChange": $ForgeConfigSpec$ConfigValue<(boolean)>

constructor()

public static "loadConfig"(arg0: $Path$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicLightsConfig$Type = ($DynamicLightsConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicLightsConfig_ = $DynamicLightsConfig$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/config/$QualityMode" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $QualityMode extends $Enum<($QualityMode)> {
static readonly "OFF": $QualityMode
static readonly "SLOW": $QualityMode
static readonly "FAST": $QualityMode
static readonly "REALTIME": $QualityMode


public static "values"(): ($QualityMode)[]
public static "valueOf"(arg0: string): $QualityMode
public "getLocalizedName"(): $Component
get "localizedName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QualityMode$Type = (("realtime") | ("fast") | ("slow") | ("off")) | ($QualityMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QualityMode_ = $QualityMode$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/$DynamicLightSource" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

export interface $DynamicLightSource {

 "tdv$getLuminance"(): integer
 "tdv$getDynamicLightX"(): double
 "tdv$getDynamicLightZ"(): double
 "tdv$getDynamicLightWorld"(): $Level
 "tdv$setDynamicLightEnabled"(arg0: boolean): void
 "tdv$shouldUpdateDynamicLight"(): boolean
 "tdv$isDynamicLightEnabled"(): boolean
 "tdv$resetDynamicLight"(): void
 "tdv$getDynamicLightY"(): double
 "tdv$dynamicLightTick"(): void
 "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
 "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
}

export namespace $DynamicLightSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicLightSource$Type = ($DynamicLightSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicLightSource_ = $DynamicLightSource$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/$SodiumDynamicLightHandler" {
import {$BlockPos$MutableBlockPos, $BlockPos$MutableBlockPos$Type} from "packages/net/minecraft/core/$BlockPos$MutableBlockPos"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ThreadLocal, $ThreadLocal$Type} from "packages/java/lang/$ThreadLocal"

export interface $SodiumDynamicLightHandler {

}

export namespace $SodiumDynamicLightHandler {
const lambdynlights$pos: $ThreadLocal<($BlockPos$MutableBlockPos)>
function lambdynlights$getLightmap(arg0: $BlockPos$Type, arg1: integer, arg2: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumDynamicLightHandler$Type = ($SodiumDynamicLightHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumDynamicLightHandler_ = $SodiumDynamicLightHandler$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/accessor/$WorldRendererAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $WorldRendererAccessor {

 "dynlights_setSectionDirty"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void

(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
}

export namespace $WorldRendererAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldRendererAccessor$Type = ($WorldRendererAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldRendererAccessor_ = $WorldRendererAccessor$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/api/item/$ItemLightSources" {
import {$ItemLightSource, $ItemLightSource$Type} from "packages/dev/lambdaurora/lambdynlights/api/item/$ItemLightSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $ItemLightSources {


public static "load"(arg0: $ResourceManager$Type): void
public static "getLuminance"(arg0: $ItemStack$Type, arg1: boolean): integer
public static "registerItemLightSource"(arg0: $ItemLightSource$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemLightSources$Type = ($ItemLightSources);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemLightSources_ = $ItemLightSources$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/$ExecutorHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ExecutorHelper {


public static "onInitializeClient"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExecutorHelper$Type = ($ExecutorHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExecutorHelper_ = $ExecutorHelper$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/api/item/$ItemLightSource" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ItemLightSource {

constructor(arg0: $ResourceLocation$Type, arg1: $Item$Type, arg2: boolean)
constructor(arg0: $ResourceLocation$Type, arg1: $Item$Type)

public "toString"(): string
public "id"(): $ResourceLocation
public "item"(): $Item
public static "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $Optional<($ItemLightSource)>
public "getLuminance"(arg0: $ItemStack$Type, arg1: boolean): integer
public "getLuminance"(arg0: $ItemStack$Type): integer
public "waterSensitive"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemLightSource$Type = ($ItemLightSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemLightSource_ = $ItemLightSource$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/api/$DynamicLightHandler" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Creeper, $Creeper$Type} from "packages/net/minecraft/world/entity/monster/$Creeper"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $DynamicLightHandler<T> {

 "getLuminance"(arg0: T): integer
 "isWaterSensitive"(arg0: T): boolean

(arg0: $DynamicLightHandler$Type<(T)>): $DynamicLightHandler<(T)>
}

export namespace $DynamicLightHandler {
function makeCreeperEntityHandler<T>(arg0: $DynamicLightHandler$Type<(T)>): $DynamicLightHandler<(T)>
function makeLivingEntityHandler<T>(arg0: $DynamicLightHandler$Type<(T)>): $DynamicLightHandler<(T)>
function makeHandler<T>(arg0: $Function$Type<(T), (integer)>, arg1: $Function$Type<(T), (boolean)>): $DynamicLightHandler<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicLightHandler$Type<T> = ($DynamicLightHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicLightHandler_<T> = $DynamicLightHandler$Type<(T)>;
}}
declare module "packages/dev/lambdaurora/lambdynlights/$DynLightsResourceListener" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $DynLightsResourceListener implements $ResourceManagerReloadListener {

constructor()

public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynLightsResourceListener$Type = ($DynLightsResourceListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynLightsResourceListener_ = $DynLightsResourceListener$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/$DynLightsMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $DynLightsMixinPlugin implements $IMixinConfigPlugin {

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
export type $DynLightsMixinPlugin$Type = ($DynLightsMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynLightsMixinPlugin_ = $DynLightsMixinPlugin$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/api/$DynamicLightHandlers" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DynamicLightHandler, $DynamicLightHandler$Type} from "packages/dev/lambdaurora/lambdynlights/api/$DynamicLightHandler"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DynamicLightHandlers {


public static "canLightUp"<T extends $Entity>(arg0: T): boolean
public static "canLightUp"<T extends $BlockEntity>(arg0: T): boolean
public static "getLuminanceFrom"<T extends $Entity>(arg0: T): integer
public static "getLuminanceFrom"<T extends $BlockEntity>(arg0: T): integer
public static "registerDynamicLightHandler"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(T)>, arg1: $DynamicLightHandler$Type<(T)>): void
public static "registerDynamicLightHandler"<T extends $Entity>(arg0: $EntityType$Type<(T)>, arg1: $DynamicLightHandler$Type<(T)>): void
public static "getDynamicLightHandler"<T extends $Entity>(arg0: $EntityType$Type<(T)>): $DynamicLightHandler<(T)>
public static "getDynamicLightHandler"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(T)>): $DynamicLightHandler<(T)>
public static "registerDefaultHandlers"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicLightHandlers$Type = ($DynamicLightHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicLightHandlers_ = $DynamicLightHandlers$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/config/$ConfigBuilder" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $ConfigBuilder {

constructor(arg0: string)

public "Block"(arg0: string, arg1: $Consumer$Type<($ForgeConfigSpec$Builder$Type)>): void
public "Save"(): $ForgeConfigSpec
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigBuilder$Type = ($ConfigBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigBuilder_ = $ConfigBuilder$Type;
}}
declare module "packages/dev/lambdaurora/lambdynlights/accessor/$DynamicLightHandlerHolder" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DynamicLightHandler, $DynamicLightHandler$Type} from "packages/dev/lambdaurora/lambdynlights/api/$DynamicLightHandler"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $DynamicLightHandlerHolder<T> {

 "lambdynlights$getDynamicLightHandler"(): $DynamicLightHandler<(T)>
 "lambdynlights$setDynamicLightHandler"(arg0: $DynamicLightHandler$Type<(T)>): void
}

export namespace $DynamicLightHandlerHolder {
function cast<T>(arg0: $BlockEntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
function cast<T>(arg0: $EntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicLightHandlerHolder$Type<T> = ($DynamicLightHandlerHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicLightHandlerHolder_<T> = $DynamicLightHandlerHolder$Type<(T)>;
}}
declare module "packages/dev/lambdaurora/lambdynlights/$LambDynLights" {
import {$LongOpenHashSet, $LongOpenHashSet$Type} from "packages/it/unimi/dsi/fastutil/longs/$LongOpenHashSet"
import {$DynamicLightSource, $DynamicLightSource$Type} from "packages/dev/lambdaurora/lambdynlights/$DynamicLightSource"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LambDynLights {
static readonly "MODID": string
readonly "logger": $Logger

constructor()

public static "get"(): $LambDynLights
public "log"(arg0: string): void
public "warn"(arg0: string): void
public static "isEnabled"(): boolean
public "clearLightSources"(): void
public "removeLightSource"(arg0: $DynamicLightSource$Type): void
public "addLightSource"(arg0: $DynamicLightSource$Type): void
public "updateAll"(arg0: $LevelRenderer$Type): void
public "getLastUpdateCount"(): integer
public "removeLightSources"(arg0: $Predicate$Type<($DynamicLightSource$Type)>): void
public static "scheduleChunkRebuild"(arg0: $LevelRenderer$Type, arg1: integer, arg2: integer, arg3: integer): void
public static "scheduleChunkRebuild"(arg0: $LevelRenderer$Type, arg1: $BlockPos$Type): void
public static "scheduleChunkRebuild"(arg0: $LevelRenderer$Type, arg1: long): void
public static "updateTrackedChunks"(arg0: $BlockPos$Type, arg1: $LongOpenHashSet$Type, arg2: $LongOpenHashSet$Type): void
public static "getLuminanceFromItemStack"(arg0: $ItemStack$Type, arg1: boolean): integer
public "containsLightSource"(arg0: $DynamicLightSource$Type): boolean
public static "updateTracking"(arg0: $DynamicLightSource$Type): void
public "removeBlockEntitiesLightSource"(): void
public "getDynamicLightLevel"(arg0: $BlockPos$Type): double
public "getLightSourcesCount"(): integer
public "getLightmapWithDynamicLight"(arg0: $BlockPos$Type, arg1: integer): integer
public "getLightmapWithDynamicLight"(arg0: $Entity$Type, arg1: integer): integer
public "getLightmapWithDynamicLight"(arg0: double, arg1: integer): integer
public static "maxDynamicLightLevel"(arg0: $BlockPos$Type, arg1: $DynamicLightSource$Type, arg2: double): double
public "removeTntLightSources"(): void
public "removeCreeperLightSources"(): void
public "removeEntitiesLightSource"(): void
get "enabled"(): boolean
get "lastUpdateCount"(): integer
get "lightSourcesCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LambDynLights$Type = ($LambDynLights);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LambDynLights_ = $LambDynLights$Type;
}}
