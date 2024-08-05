declare module "packages/org/embeddedt/embeddium/api/model/$EmbeddiumBakedModelExtension" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $EmbeddiumBakedModelExtension {

 "useAmbientOcclusionWithLightEmission"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
}

export namespace $EmbeddiumBakedModelExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumBakedModelExtension$Type = ($EmbeddiumBakedModelExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumBakedModelExtension_ = $EmbeddiumBakedModelExtension$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/tab/$TabFrame" {
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Tab, $Tab$Type} from "packages/org/embeddedt/embeddium/gui/frame/tab/$Tab"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$AtomicReference, $AtomicReference$Type} from "packages/java/util/concurrent/atomic/$AtomicReference"
import {$TabFrame$Builder, $TabFrame$Builder$Type} from "packages/org/embeddedt/embeddium/gui/frame/tab/$TabFrame$Builder"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TabFrame extends $AbstractFrame {

constructor(arg0: $Dim2i$Type, arg1: boolean, arg2: $Multimap$Type<(string), ($Tab$Type<(any)>)>, arg3: $Runnable$Type, arg4: $AtomicReference$Type<($Component$Type)>, arg5: $AtomicReference$Type<(integer)>)

public "buildFrame"(): void
public static "createBuilder"(): $TabFrame$Builder
public "setTab"(arg0: $Tab$Type<(any)>): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "setFocused"(arg0: boolean): void
public "isFocused"(): boolean
set "tab"(value: $Tab$Type<(any)>)
set "focused"(value: boolean)
get "focused"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TabFrame$Type = ($TabFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TabFrame_ = $TabFrame$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$ILevelSave" {
import {$LevelStorageSource, $LevelStorageSource$Type} from "packages/net/minecraft/world/level/storage/$LevelStorageSource"

export interface $ILevelSave {

 "runWorldPersistenceHooks"(arg0: $LevelStorageSource$Type): void

(arg0: $LevelStorageSource$Type): void
}

export namespace $ILevelSave {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILevelSave$Type = ($ILevelSave);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILevelSave_ = $ILevelSave$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/classloading/$ModFileScanDataDeduplicator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModFileScanDataDeduplicator {


public static "deduplicate"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModFileScanDataDeduplicator$Type = ($ModFileScanDataDeduplicator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModFileScanDataDeduplicator_ = $ModFileScanDataDeduplicator$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/$ModernFixConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ModernFixConfig {
static "COMMON_CONFIG": $ForgeConfigSpec
static "BLACKLIST_ASYNC_JEI_PLUGINS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>

constructor()

public static "getJeiPluginBlacklist"(): $Set<($ResourceLocation)>
get "jeiPluginBlacklist"(): $Set<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixConfig$Type = ($ModernFixConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixConfig_ = $ModernFixConfig$Type;
}}
declare module "packages/org/embeddedt/modernfix/spark/$SparkLaunchProfiler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SparkLaunchProfiler {

constructor()

public static "start"(key: string): void
public static "stop"(key: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SparkLaunchProfiler$Type = ($SparkLaunchProfiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SparkLaunchProfiler_ = $SparkLaunchProfiler$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$DummyList" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $DummyList<T> implements $List<(T)> {

constructor()

public "add"(t: T): boolean
public "add"(i: integer, t: T): void
public "remove"(i: integer): T
public "remove"(o: any): boolean
public "get"(i: integer): T
public "indexOf"(o: any): integer
public "clear"(): void
public "lastIndexOf"(o: any): integer
public "isEmpty"(): boolean
public "size"(): integer
public "subList"(i: integer, i1: integer): $List<(T)>
public "toArray"<T1>(t1s: (T1)[]): (T1)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(T)>
public "contains"(o: any): boolean
public "addAll"(i: integer, collection: $Collection$Type<(any)>): boolean
public "addAll"(collection: $Collection$Type<(any)>): boolean
public "set"(i: integer, t: T): T
public "removeAll"(collection: $Collection$Type<(any)>): boolean
public "retainAll"(collection: $Collection$Type<(any)>): boolean
public "listIterator"(i: integer): $ListIterator<(T)>
public "listIterator"(): $ListIterator<(T)>
public "containsAll"(collection: $Collection$Type<(any)>): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(T)>
public "replaceAll"(arg0: $UnaryOperator$Type<(T)>): void
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T): $List<(T)>
public static "of"<E>(arg0: T): $List<(T)>
public static "of"<E>(): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T, arg7: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T, arg7: T, arg8: T, arg9: T): $List<(T)>
public static "of"<E>(arg0: T, arg1: T, arg2: T, arg3: T, arg4: T, arg5: T, arg6: T, arg7: T, arg8: T): $List<(T)>
public static "of"<E>(...arg0: (T)[]): $List<(T)>
public "spliterator"(): $Spliterator<(T)>
public "sort"(arg0: $Comparator$Type<(any)>): void
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<(T)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<T>;
[index: number]: T
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DummyList$Type<T> = ($DummyList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DummyList_<T> = $DummyList$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IExtendedModelBakery" {
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $IExtendedModelBakery {

 "bakeDefault"(arg0: $ResourceLocation$Type, arg1: $ModelState$Type): $BakedModel
 "getBlockStatesForMRL"(arg0: $StateDefinition$Type<($Block$Type), ($BlockState$Type)>, arg1: $ModelResourceLocation$Type): $ImmutableList<($BlockState)>
 "mfix$getUnbakedMissingModel"(): $UnbakedModel
}

export namespace $IExtendedModelBakery {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IExtendedModelBakery$Type = ($IExtendedModelBakery);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IExtendedModelBakery_ = $IExtendedModelBakery$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$ChunkDataBuiltEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$EventHandlerRegistrar, $EventHandlerRegistrar$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar"
import {$BuiltSectionInfo$Builder, $BuiltSectionInfo$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo$Builder"

export class $ChunkDataBuiltEvent extends $EmbeddiumEvent {
static readonly "BUS": $EventHandlerRegistrar<($ChunkDataBuiltEvent)>

constructor(arg0: $BuiltSectionInfo$Builder$Type)
constructor()

public "getDataBuilder"(): $BuiltSectionInfo$Builder
public "getListenerList"(): $ListenerList
get "dataBuilder"(): $BuiltSectionInfo$Builder
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkDataBuiltEvent$Type = ($ChunkDataBuiltEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkDataBuiltEvent_ = $ChunkDataBuiltEvent$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$BakeReason" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BakeReason extends $Enum<($BakeReason)> {
static readonly "FREEZE": $BakeReason
static readonly "REMOTE_SNAPSHOT_INJECT": $BakeReason
static readonly "LOCAL_SNAPSHOT_INJECT": $BakeReason
static readonly "REVERT": $BakeReason
static readonly "UNKNOWN": $BakeReason


public static "values"(): ($BakeReason)[]
public static "valueOf"(name: string): $BakeReason
public static "getCurrentBakeReason"(): $BakeReason
public static "setCurrentBakeReason"(reason: $BakeReason$Type): void
get "currentBakeReason"(): $BakeReason
set "currentBakeReason"(value: $BakeReason$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakeReason$Type = (("freeze") | ("revert") | ("remote_snapshot_inject") | ("local_snapshot_inject") | ("unknown")) | ($BakeReason);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakeReason_ = $BakeReason$Type;
}}
declare module "packages/org/embeddedt/modernfix/entity/$ErroredEntityRenderer" {
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ErroredEntityRenderer<T extends $Entity> extends $EntityRenderer<(T)> {
 "shadowRadius": float

constructor(arg: $EntityRendererProvider$Context$Type)

public "shouldRender"(livingEntity: T, camera: $Frustum$Type, camX: double, camY: double, camZ: double): boolean
public "render"(entity: T, entityYaw: float, partialTick: float, poseStack: $PoseStack$Type, buffer: $MultiBufferSource$Type, packedLight: integer): void
public "getTextureLocation"(entity: T): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErroredEntityRenderer$Type<T> = ($ErroredEntityRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErroredEntityRenderer_<T> = $ErroredEntityRenderer$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/forge/rs/$IFluidExternalStorageCache" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"

export interface $IFluidExternalStorageCache {

 "initCache"(arg0: $IFluidHandler$Type): boolean

(arg0: $IFluidHandler$Type): boolean
}

export namespace $IFluidExternalStorageCache {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFluidExternalStorageCache$Type = ($IFluidExternalStorageCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFluidExternalStorageCache_ = $IFluidExternalStorageCache$Type;
}}
declare module "packages/org/embeddedt/modernfix/structure/$CachingStructureManager" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"
import {$HolderGetter, $HolderGetter$Type} from "packages/net/minecraft/core/$HolderGetter"
import {$DataFixer, $DataFixer$Type} from "packages/com/mojang/datafixers/$DataFixer"

export class $CachingStructureManager {

constructor()

public static "readStructureTag"(location: $ResourceLocation$Type, datafixer: $DataFixer$Type, stream: $InputStream$Type): $CompoundTag
public static "readStructure"(location: $ResourceLocation$Type, datafixer: $DataFixer$Type, stream: $InputStream$Type, blockGetter: $HolderGetter$Type<($Block$Type)>): $StructureTemplate
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachingStructureManager$Type = ($CachingStructureManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachingStructureManager_ = $CachingStructureManager$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$DummyServerConfiguration" {
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerLevelData, $ServerLevelData$Type} from "packages/net/minecraft/world/level/storage/$ServerLevelData"
import {$EndDragonFight$Data, $EndDragonFight$Data$Type} from "packages/net/minecraft/world/level/dimension/end/$EndDragonFight$Data"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$WorldData, $WorldData$Type} from "packages/net/minecraft/world/level/storage/$WorldData"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$LevelSettings, $LevelSettings$Type} from "packages/net/minecraft/world/level/$LevelSettings"
import {$GameRules, $GameRules$Type} from "packages/net/minecraft/world/level/$GameRules"
import {$Difficulty, $Difficulty$Type} from "packages/net/minecraft/world/$Difficulty"
import {$WorldOptions, $WorldOptions$Type} from "packages/net/minecraft/world/level/levelgen/$WorldOptions"
import {$GameType, $GameType$Type} from "packages/net/minecraft/world/level/$GameType"
import {$CrashReportCategory, $CrashReportCategory$Type} from "packages/net/minecraft/$CrashReportCategory"
import {$WorldDataConfiguration, $WorldDataConfiguration$Type} from "packages/net/minecraft/world/level/$WorldDataConfiguration"

export class $DummyServerConfiguration implements $WorldData {

constructor()

public "isFlatWorld"(): boolean
public "setDataConfiguration"(arg: $WorldDataConfiguration$Type): void
public "worldGenSettingsLifecycle"(): $Lifecycle
public "isHardcore"(): boolean
public "getGameRules"(): $GameRules
public "getDifficulty"(): $Difficulty
public "getGameType"(): $GameType
public "isDifficultyLocked"(): boolean
public "getLevelName"(): string
public "setGameType"(type: $GameType$Type): void
public "worldGenOptions"(): $WorldOptions
public "endDragonFightData"(): $EndDragonFight$Data
public "setEndDragonFightData"(data: $EndDragonFight$Data$Type): void
public "getDataConfiguration"(): $WorldDataConfiguration
public "createTag"(registries: $RegistryAccess$Type, hostPlayerNBT: $CompoundTag$Type): $CompoundTag
public "getLoadedPlayerTag"(): $CompoundTag
public "getAllowCommands"(): boolean
public "getRemovedFeatureFlags"(): $Set<(string)>
public "getVersion"(): integer
public "getKnownServerBrands"(): $Set<(string)>
public "wasModded"(): boolean
public "setModdedInfo"(name: string, isModded: boolean): void
public "isDebugWorld"(): boolean
public "getCustomBossEvents"(): $CompoundTag
public "overworldData"(): $ServerLevelData
public "setDifficulty"(difficulty: $Difficulty$Type): void
public "setDifficultyLocked"(locked: boolean): void
public "setCustomBossEvents"(nbt: $CompoundTag$Type): void
public "getLevelSettings"(): $LevelSettings
public "enabledFeatures"(): $FeatureFlagSet
public "fillCrashReportCategory"(arg0: $CrashReportCategory$Type): void
public "getStorageVersionName"(arg0: integer): string
get "flatWorld"(): boolean
set "dataConfiguration"(value: $WorldDataConfiguration$Type)
get "hardcore"(): boolean
get "gameRules"(): $GameRules
get "difficulty"(): $Difficulty
get "gameType"(): $GameType
get "difficultyLocked"(): boolean
get "levelName"(): string
set "gameType"(value: $GameType$Type)
get "dataConfiguration"(): $WorldDataConfiguration
get "loadedPlayerTag"(): $CompoundTag
get "allowCommands"(): boolean
get "removedFeatureFlags"(): $Set<(string)>
get "version"(): integer
get "knownServerBrands"(): $Set<(string)>
get "debugWorld"(): boolean
get "customBossEvents"(): $CompoundTag
set "difficulty"(value: $Difficulty$Type)
set "difficultyLocked"(value: boolean)
set "customBossEvents"(value: $CompoundTag$Type)
get "levelSettings"(): $LevelSettings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DummyServerConfiguration$Type = ($DummyServerConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DummyServerConfiguration_ = $DummyServerConfiguration$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$Level" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TranslucentQuadAnalyzer$Level extends $Enum<($TranslucentQuadAnalyzer$Level)> {
static readonly "NONE": $TranslucentQuadAnalyzer$Level
static readonly "STATIC": $TranslucentQuadAnalyzer$Level
static readonly "DYNAMIC": $TranslucentQuadAnalyzer$Level
static readonly "VALUES": ($TranslucentQuadAnalyzer$Level)[]


public static "values"(): ($TranslucentQuadAnalyzer$Level)[]
public static "valueOf"(arg0: string): $TranslucentQuadAnalyzer$Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TranslucentQuadAnalyzer$Level$Type = (("static") | ("dynamic") | ("none")) | ($TranslucentQuadAnalyzer$Level);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TranslucentQuadAnalyzer$Level_ = $TranslucentQuadAnalyzer$Level$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/registry/$ObjectHolderClearer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ObjectHolderClearer {

constructor()

public static "clearThrowables"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectHolderClearer$Type = ($ObjectHolderClearer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectHolderClearer_ = $ObjectHolderClearer$Type;
}}
declare module "packages/org/embeddedt/modernfix/world/gen/$PositionalBiomeGetter" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockPos$MutableBlockPos, $BlockPos$MutableBlockPos$Type} from "packages/net/minecraft/core/$BlockPos$MutableBlockPos"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $PositionalBiomeGetter implements $Supplier<($Holder<($Biome)>)> {

constructor(biomeGetter: $Function$Type<($BlockPos$Type), ($Holder$Type<($Biome$Type)>)>, pos: $BlockPos$MutableBlockPos$Type)

public "update"(nextX: integer, nextY: integer, nextZ: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PositionalBiomeGetter$Type = ($PositionalBiomeGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PositionalBiomeGetter_ = $PositionalBiomeGetter$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$CanonizingStringMap" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Function, $Function$Type} from "packages/com/google/common/base/$Function"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $CanonizingStringMap<T> extends $HashMap<(string), (T)> {

constructor()

public "put"(key: string, value: T): T
public "putAll"(m: $Map$Type<(any), (any)>): void
public static "deepCopy"<T>(incomingMap: $CanonizingStringMap$Type<(T)>, deepCopier: $Function$Type<(T), (T)>): $CanonizingStringMap<(T)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CanonizingStringMap$Type<T> = ($CanonizingStringMap<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CanonizingStringMap_<T> = $CanonizingStringMap$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$JEIBackedSearchTree" {
import {$DummySearchTree, $DummySearchTree$Type} from "packages/org/embeddedt/modernfix/searchtree/$DummySearchTree"
import {$RefreshableSearchTree, $RefreshableSearchTree$Type} from "packages/net/minecraft/client/searchtree/$RefreshableSearchTree"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SearchTreeProviderRegistry$Provider, $SearchTreeProviderRegistry$Provider$Type} from "packages/org/embeddedt/modernfix/searchtree/$SearchTreeProviderRegistry$Provider"

export class $JEIBackedSearchTree extends $DummySearchTree<($ItemStack)> {
static readonly "PROVIDER": $SearchTreeProviderRegistry$Provider

constructor(filteringByTag: boolean)

public "search"(pSearchText: string): $List<($ItemStack)>
public static "empty"<T>(): $RefreshableSearchTree<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIBackedSearchTree$Type = ($JEIBackedSearchTree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIBackedSearchTree_ = $JEIBackedSearchTree$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/registry/$DelegateHolder" {
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $DelegateHolder<T> {

 "mfix$getDelegate"(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>): $Holder$Reference<(T)>
 "mfix$setDelegate"(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>, arg1: $Holder$Reference$Type<(T)>): void
}

export namespace $DelegateHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DelegateHolder$Type<T> = ($DelegateHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DelegateHolder_<T> = $DelegateHolder$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/resources/$NewResourcePackAdapter" {
import {$PackResources$ResourceOutput, $PackResources$ResourceOutput$Type} from "packages/net/minecraft/server/packs/$PackResources$ResourceOutput"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IoSupplier, $IoSupplier$Type} from "packages/net/minecraft/server/packs/resources/$IoSupplier"

export class $NewResourcePackAdapter {

constructor()

public static "sendToOutput"(streamCreator: $Function$Type<($ResourceLocation$Type), ($IoSupplier$Type<($InputStream$Type)>)>, output: $PackResources$ResourceOutput$Type, locations: $Collection$Type<($ResourceLocation$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NewResourcePackAdapter$Type = ($NewResourcePackAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NewResourcePackAdapter_ = $NewResourcePackAdapter$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$MeshAppender" {
import {$MeshAppender$Context, $MeshAppender$Context$Type} from "packages/org/embeddedt/embeddium/api/$MeshAppender$Context"

export interface $MeshAppender {

 "render"(arg0: $MeshAppender$Context$Type): void

(arg0: $MeshAppender$Context$Type): void
}

export namespace $MeshAppender {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeshAppender$Type = ($MeshAppender);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeshAppender_ = $MeshAppender$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/dynresources/$ModelBakeEventHelper" {
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModelBakeEventHelper {

constructor(modelRegistry: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>)

public "wrapRegistry"(modId: string): $Map<($ResourceLocation), ($BakedModel)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelBakeEventHelper$Type = ($ModelBakeEventHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelBakeEventHelper_ = $ModelBakeEventHelper$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/frapi/$FRAPIRenderHandler" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$BlockRenderContext, $BlockRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ChunkBuildBuffers, $ChunkBuildBuffers$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers"

export interface $FRAPIRenderHandler {

 "flush"(arg0: $ChunkBuildBuffers$Type, arg1: $Vector3fc$Type): void
 "reset"(): void
 "renderEmbeddium"(arg0: $BlockRenderContext$Type, arg1: $PoseStack$Type, arg2: $RandomSource$Type): void
}

export namespace $FRAPIRenderHandler {
const INDIGO_PRESENT: boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FRAPIRenderHandler$Type = ($FRAPIRenderHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FRAPIRenderHandler_ = $FRAPIRenderHandler$Type;
}}
declare module "packages/org/embeddedt/modernfix/screen/$OptionList$Entry" {
import {$ContainerObjectSelectionList$Entry, $ContainerObjectSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$ContainerObjectSelectionList$Entry"

export class $OptionList$Entry extends $ContainerObjectSelectionList$Entry<($OptionList$Entry)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionList$Entry$Type = ($OptionList$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionList$Entry_ = $OptionList$Entry$Type;
}}
declare module "packages/org/embeddedt/modernfix/chunk/$SafeBlockGetter" {
import {$ModelDataManager, $ModelDataManager$Type} from "packages/net/minecraftforge/client/model/data/$ModelDataManager"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ClipBlockStateContext, $ClipBlockStateContext$Type} from "packages/net/minecraft/world/level/$ClipBlockStateContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ClipContext, $ClipContext$Type} from "packages/net/minecraft/world/level/$ClipContext"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $SafeBlockGetter implements $BlockGetter {

constructor(wrapped: $ServerLevel$Type)

public "shouldUse"(): boolean
public "getBlockEntity"(pos: $BlockPos$Type): $BlockEntity
public "getFluidState"(pos: $BlockPos$Type): $FluidState
public "getHeight"(): integer
public "getMaxLightLevel"(): integer
public "getMinBuildHeight"(): integer
public "getBlockState"(pos: $BlockPos$Type): $BlockState
public "getMaxBuildHeight"(): integer
public "getBlockEntity"<T extends $BlockEntity>(arg0: $BlockPos$Type, arg1: $BlockEntityType$Type<(T)>): $Optional<(T)>
public "getBlockStates"(arg0: $AABB$Type): $Stream<($BlockState)>
public "isBlockInLine"(arg0: $ClipBlockStateContext$Type): $BlockHitResult
public "clipWithInteractionOverride"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $BlockPos$Type, arg3: $VoxelShape$Type, arg4: $BlockState$Type): $BlockHitResult
public "getLightEmission"(arg0: $BlockPos$Type): integer
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public "getBlockFloorHeight"(arg0: $VoxelShape$Type, arg1: $Supplier$Type<($VoxelShape$Type)>): double
public "getBlockFloorHeight"(arg0: $BlockPos$Type): double
public "clip"(arg0: $ClipContext$Type): $BlockHitResult
public "getSectionsCount"(): integer
public "isOutsideBuildHeight"(arg0: integer): boolean
public "getMinSection"(): integer
public "getMaxSection"(): integer
public "getSectionIndexFromSectionY"(arg0: integer): integer
public "getSectionYFromSectionIndex"(arg0: integer): integer
public "getSectionIndex"(arg0: integer): integer
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
public "isOutsideBuildHeight"(arg0: $BlockPos$Type): boolean
public "getExistingBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getModelDataManager"(): $ModelDataManager
get "height"(): integer
get "maxLightLevel"(): integer
get "minBuildHeight"(): integer
get "maxBuildHeight"(): integer
get "sectionsCount"(): integer
get "minSection"(): integer
get "maxSection"(): integer
get "modelDataManager"(): $ModelDataManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeBlockGetter$Type = ($SafeBlockGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeBlockGetter_ = $SafeBlockGetter$Type;
}}
declare module "packages/org/embeddedt/embeddium/util/sodium/$FlawlessFrames" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FlawlessFrames {

constructor()

public static "isActive"(): boolean
public static "onClientInitialization"(): void
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlawlessFrames$Type = ($FlawlessFrames);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlawlessFrames_ = $FlawlessFrames$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IModelHoldingBlockState" {
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"

export interface $IModelHoldingBlockState {

 "mfix$getModel"(): $BakedModel
 "mfix$setModel"(arg0: $BakedModel$Type): void
}

export namespace $IModelHoldingBlockState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModelHoldingBlockState$Type = ($IModelHoldingBlockState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModelHoldingBlockState_ = $IModelHoldingBlockState$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/tab/$Tab" {
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Tab$Builder, $Tab$Builder$Type} from "packages/org/embeddedt/embeddium/gui/frame/tab/$Tab$Builder"

export class $Tab<T extends $AbstractFrame> extends $Record {

constructor(id: $OptionIdentifier$Type<(void)>, title: $Component$Type, onSelectFunction: $Supplier$Type<(boolean)>, frameFunction: $Function$Type<($Dim2i$Type), (T)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "id"(): $OptionIdentifier<(void)>
public static "createBuilder"(): $Tab$Builder<(any)>
public "title"(): $Component
public "getFrameFunction"(): $Function<($Dim2i), (T)>
public "onSelectFunction"(): $Supplier<(boolean)>
public "frameFunction"(): $Function<($Dim2i), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tab$Type<T> = ($Tab<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tab_<T> = $Tab$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/command/$ModernFixCommands" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $ModernFixCommands {

constructor()

public static "register"(dispatcher: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixCommands$Type = ($ModernFixCommands);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixCommands_ = $ModernFixCommands$Type;
}}
declare module "packages/org/embeddedt/modernfix/api/helpers/$ModelHelpers" {
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModelHelpers {

constructor()

public static "getBlockStateForLocation"(definition: $StateDefinition$Type<($Block$Type), ($BlockState$Type)>, location: $ModelResourceLocation$Type): $ImmutableList<($BlockState)>
public static "getBlockStateForLocation"(location: $ModelResourceLocation$Type): $ImmutableList<($BlockState)>
public static "createFakeTopLevelMap"(modelGetter: $BiFunction$Type<($ResourceLocation$Type), ($ModelState$Type), ($BakedModel$Type)>): $Map<($ResourceLocation), ($BakedModel)>
public static "adaptBakery"(bakery: $ModelBakery$Type): $ModelBaker
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelHelpers$Type = ($ModelHelpers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelHelpers_ = $ModelHelpers$Type;
}}
declare module "packages/org/embeddedt/embeddium/chunk/$MeshAppenderRenderer" {
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MeshAppender, $MeshAppender$Type} from "packages/org/embeddedt/embeddium/api/$MeshAppender"
import {$ChunkBuildBuffers, $ChunkBuildBuffers$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $MeshAppenderRenderer {

constructor()

public static "renderMeshAppenders"(arg0: $List$Type<($MeshAppender$Type)>, arg1: $BlockAndTintGetter$Type, arg2: $SectionPos$Type, arg3: $ChunkBuildBuffers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeshAppenderRenderer$Type = ($MeshAppenderRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeshAppenderRenderer_ = $MeshAppenderRenderer$Type;
}}
declare module "packages/org/embeddedt/embeddium/util/$PlatformUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformUtil {

constructor()

public static "isDevelopmentEnvironment"(): boolean
public static "modPresent"(arg0: string): boolean
public static "isLoadValid"(): boolean
public static "getModName"(arg0: string): string
get "developmentEnvironment"(): boolean
get "loadValid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformUtil$Type = ($PlatformUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformUtil_ = $PlatformUtil$Type;
}}
declare module "packages/org/embeddedt/modernfix/tickables/$TickableObjectManager" {
import {$TickableObject, $TickableObject$Type} from "packages/org/embeddedt/modernfix/tickables/$TickableObject"

export class $TickableObjectManager {

constructor()

public static "register"(object: $TickableObject$Type): void
public static "runTick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickableObjectManager$Type = ($TickableObjectManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickableObjectManager_ = $TickableObjectManager$Type;
}}
declare module "packages/org/embeddedt/modernfix/screen/$ModernFixOptionInfoScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ModernFixOptionInfoScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(lastScreen: $Screen$Type, optionName: string)

public "onClose"(): void
public "render"(guiGraphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, partialTicks: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixOptionInfoScreen$Type = ($ModernFixOptionInfoScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixOptionInfoScreen_ = $ModernFixOptionInfoScreen$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$Renderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$BlockRendererRegistry$RenderResult, $BlockRendererRegistry$RenderResult$Type} from "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$RenderResult"
import {$BlockRenderContext, $BlockRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export interface $BlockRendererRegistry$Renderer {

 "renderBlock"(arg0: $BlockRenderContext$Type, arg1: $RandomSource$Type, arg2: $VertexConsumer$Type): $BlockRendererRegistry$RenderResult

(arg0: $BlockRenderContext$Type, arg1: $RandomSource$Type, arg2: $VertexConsumer$Type): $BlockRendererRegistry$RenderResult
}

export namespace $BlockRendererRegistry$Renderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRendererRegistry$Renderer$Type = ($BlockRendererRegistry$Renderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRendererRegistry$Renderer_ = $BlockRendererRegistry$Renderer$Type;
}}
declare module "packages/org/embeddedt/modernfix/core/config/$Option" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export class $Option {

constructor(name: string, enabled: boolean, userDefined: boolean)

public "getName"(): string
public "getParent"(): $Option
public "setParent"(option: $Option$Type): void
public "isEnabled"(): boolean
public "clearModsDefiningValue"(): void
public "isEffectivelyDisabledByParent"(): boolean
public "isOverridden"(): boolean
public "isModDefined"(): boolean
public "getDefiningMods"(): $Collection<(string)>
public "isUserDefined"(): boolean
public "addModOverride"(enabled: boolean, modId: string): void
public "setEnabled"(enabled: boolean, userDefined: boolean): void
public "getDepth"(): integer
public "getSelfName"(): string
public "clearUserDefined"(): void
get "name"(): string
get "parent"(): $Option
set "parent"(value: $Option$Type)
get "enabled"(): boolean
get "effectivelyDisabledByParent"(): boolean
get "overridden"(): boolean
get "modDefined"(): boolean
get "definingMods"(): $Collection<(string)>
get "userDefined"(): boolean
get "depth"(): integer
get "selfName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Option$Type = ($Option);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Option_ = $Option$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/tab/$TabHeaderWidget" {
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$FlatButtonWidget, $FlatButtonWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$FlatButtonWidget"

export class $TabHeaderWidget extends $FlatButtonWidget {

constructor(arg0: $Dim2i$Type, arg1: string)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public static "getLabel"(arg0: string): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TabHeaderWidget$Type = ($TabHeaderWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TabHeaderWidget_ = $TabHeaderWidget$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame" {
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ContainerEventHandler, $ContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$ContainerEventHandler"

export class $AbstractFrame extends $AbstractWidget implements $ContainerEventHandler {

constructor(arg0: $Dim2i$Type, arg1: boolean)

public "applyScissor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Runnable$Type): void
public "buildFrame"(): void
public "getDimensions"(): $Dim2i
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "children"(): $List<(any)>
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRectangle"(): $ScreenRectangle
public "setDragging"(arg0: boolean): void
public "setFocused"(arg0: $GuiEventListener$Type): void
public "isDragging"(): boolean
public "getFocused"(): $GuiEventListener
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "registerFocusListener"(arg0: $Consumer$Type<($GuiEventListener$Type)>): void
public "getCurrentFocusPath"(): $ComponentPath
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "setFocused"(arg0: boolean): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "getChildAt"(arg0: double, arg1: double): $Optional<($GuiEventListener)>
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "isFocused"(): boolean
public "magicalSpecialHackyFocus"(arg0: $GuiEventListener$Type): void
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
get "dimensions"(): $Dim2i
get "rectangle"(): $ScreenRectangle
set "dragging"(value: boolean)
set "focused"(value: $GuiEventListener$Type)
get "dragging"(): boolean
get "focused"(): $GuiEventListener
get "currentFocusPath"(): $ComponentPath
set "focused"(value: boolean)
get "focused"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractFrame$Type = ($AbstractFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractFrame_ = $AbstractFrame$Type;
}}
declare module "packages/org/embeddedt/embeddium/taint/scanning/$TaintDetector" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TaintDetector {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TaintDetector$Type = ($TaintDetector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TaintDetector_ = $TaintDetector$Type;
}}
declare module "packages/org/embeddedt/modernfix/platform/$PlatformHookLoader" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformHookLoader {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformHookLoader$Type = ($PlatformHookLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformHookLoader_ = $PlatformHookLoader$Type;
}}
declare module "packages/org/embeddedt/embeddium/asm/$AnnotationProcessingEngine" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"

export class $AnnotationProcessingEngine {

constructor()

public static "processClass"(arg0: $ClassNode$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotationProcessingEngine$Type = ($AnnotationProcessingEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotationProcessingEngine_ = $AnnotationProcessingEngine$Type;
}}
declare module "packages/org/embeddedt/embeddium/util/$StringUtils" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $StringUtils {

constructor()

public static "fuzzySearch"<T>(arg0: $Iterable$Type<(T)>, arg1: string, arg2: integer, arg3: $Function$Type<(T), (string)>): $List<(T)>
public static "levenshteinDistance"(arg0: string, arg1: string): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringUtils$Type = ($StringUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringUtils_ = $StringUtils$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IServerLevel" {
import {$StrongholdLocationCache, $StrongholdLocationCache$Type} from "packages/org/embeddedt/modernfix/world/$StrongholdLocationCache"

export interface $IServerLevel {

 "mfix$getStrongholdCache"(): $StrongholdLocationCache

(): $StrongholdLocationCache
}

export namespace $IServerLevel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IServerLevel$Type = ($IServerLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IServerLevel_ = $IServerLevel$Type;
}}
declare module "packages/org/embeddedt/embeddium/compat/ccl/$CCLCompat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CCLCompat {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CCLCompat$Type = ($CCLCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CCLCompat_ = $CCLCompat$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/init/$ModernFixForge" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$ServerStartedEvent, $ServerStartedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartedEvent"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$OnDatapackSyncEvent, $OnDatapackSyncEvent$Type} from "packages/net/minecraftforge/event/$OnDatapackSyncEvent"
import {$ServerStoppedEvent, $ServerStoppedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppedEvent"

export class $ModernFixForge {
static "launchDone": boolean

constructor()

public "onCommandRegister"(event: $RegisterCommandsEvent$Type): void
public "onDatapackSync"(event: $OnDatapackSyncEvent$Type): void
public "onServerDead"(event: $ServerStoppedEvent$Type): void
public "onServerStarted"(event: $ServerStartedEvent$Type): void
public "commonSetup"(event: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixForge$Type = ($ModernFixForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixForge_ = $ModernFixForge$Type;
}}
declare module "packages/org/embeddedt/embeddium/bootstrap/$EmbeddiumEarlyWindowHacks" {
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"

export class $EmbeddiumEarlyWindowHacks {

constructor()

public static "createEarlyLaunchWindow"(arg0: $IntSupplier$Type, arg1: $IntSupplier$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumEarlyWindowHacks$Type = ($EmbeddiumEarlyWindowHacks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumEarlyWindowHacks_ = $EmbeddiumEarlyWindowHacks$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/components/$ScrollBarComponent$Mode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ScrollBarComponent$Mode extends $Enum<($ScrollBarComponent$Mode)> {
static readonly "HORIZONTAL": $ScrollBarComponent$Mode
static readonly "VERTICAL": $ScrollBarComponent$Mode


public static "values"(): ($ScrollBarComponent$Mode)[]
public static "valueOf"(arg0: string): $ScrollBarComponent$Mode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollBarComponent$Mode$Type = (("horizontal") | ("vertical")) | ($ScrollBarComponent$Mode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollBarComponent$Mode_ = $ScrollBarComponent$Mode$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/load/$ModWorkManagerQueue" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$ConcurrentLinkedDeque, $ConcurrentLinkedDeque$Type} from "packages/java/util/concurrent/$ConcurrentLinkedDeque"

export class $ModWorkManagerQueue extends $ConcurrentLinkedDeque<($Runnable)> {

constructor()

public static "replace"(): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModWorkManagerQueue$Type = ($ModWorkManagerQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModWorkManagerQueue_ = $ModWorkManagerQueue$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/util/$ModUtil" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ForkJoinPool, $ForkJoinPool$Type} from "packages/java/util/concurrent/$ForkJoinPool"

export class $ModUtil {
static "commonPool": $ForkJoinPool

constructor()

public static "findAllModsListeningToEvent"(eventClazz: $Class$Type<(any)>): $Collection<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModUtil$Type = ($ModUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModUtil_ = $ModUtil$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$DynamicOverridableMap" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DynamicMap, $DynamicMap$Type} from "packages/org/embeddedt/modernfix/util/$DynamicMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $DynamicOverridableMap<K, V> extends $DynamicMap<(K), (V)> {

constructor(arg0: $Function$Type<(K), (V)>)

public "get"(o: any): V
public "put"(k: K, v: V): V
public "putAll"(map: $Map$Type<(any), (any)>): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicOverridableMap$Type<K, V> = ($DynamicOverridableMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicOverridableMap_<K, V> = $DynamicOverridableMap$Type<(K), (V)>;
}}
declare module "packages/org/embeddedt/modernfix/blockstate/$BlockStateCacheHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BlockStateCacheHandler {

constructor()

public static "rebuildParallel"(force: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateCacheHandler$Type = ($BlockStateCacheHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateCacheHandler_ = $BlockStateCacheHandler$Type;
}}
declare module "packages/org/embeddedt/embeddium/taint/scanning/$ClassConstantPoolParser" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClassConstantPoolParser {

constructor(...arg0: (string)[])

public "find"(arg0: (byte)[], arg1: boolean): boolean
public "find"(arg0: (byte)[]): boolean
public "addString"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassConstantPoolParser$Type = ($ClassConstantPoolParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassConstantPoolParser_ = $ClassConstantPoolParser$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/render/chunk/$RenderSectionDistanceFilterEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$RenderSectionDistanceFilter, $RenderSectionDistanceFilter$Type} from "packages/org/embeddedt/embeddium/api/render/chunk/$RenderSectionDistanceFilter"
import {$EventHandlerRegistrar, $EventHandlerRegistrar$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar"

export class $RenderSectionDistanceFilterEvent extends $EmbeddiumEvent {
static readonly "BUS": $EventHandlerRegistrar<($RenderSectionDistanceFilterEvent)>

constructor()

public "getFilter"(): $RenderSectionDistanceFilter
public "setFilter"(arg0: $RenderSectionDistanceFilter$Type): void
public "getListenerList"(): $ListenerList
get "filter"(): $RenderSectionDistanceFilter
set "filter"(value: $RenderSectionDistanceFilter$Type)
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSectionDistanceFilterEvent$Type = ($RenderSectionDistanceFilterEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSectionDistanceFilterEvent_ = $RenderSectionDistanceFilterEvent$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer" {
import {$TranslucentQuadAnalyzer$SortState, $TranslucentQuadAnalyzer$SortState$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState"
import {$ChunkVertexEncoder$Vertex, $ChunkVertexEncoder$Vertex$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder$Vertex"

export class $TranslucentQuadAnalyzer {

constructor()

public "clear"(): void
public "capture"(arg0: $ChunkVertexEncoder$Vertex$Type): void
public "getSortState"(): $TranslucentQuadAnalyzer$SortState
get "sortState"(): $TranslucentQuadAnalyzer$SortState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TranslucentQuadAnalyzer$Type = ($TranslucentQuadAnalyzer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TranslucentQuadAnalyzer_ = $TranslucentQuadAnalyzer$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/$EmbeddiumVideoOptionsScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$BasicFrame$Builder, $BasicFrame$Builder$Type} from "packages/org/embeddedt/embeddium/gui/frame/$BasicFrame$Builder"

export class $EmbeddiumVideoOptionsScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $List$Type<($OptionPage$Type)>)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "shouldCloseOnEsc"(): boolean
public "onClose"(): void
public "renderBackground"(arg0: $GuiGraphics$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "rebuildUI"(): void
public "parentBasicFrameBuilder"(arg0: $Dim2i$Type, arg1: $Dim2i$Type): $BasicFrame$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumVideoOptionsScreen$Type = ($EmbeddiumVideoOptionsScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumVideoOptionsScreen_ = $EmbeddiumVideoOptionsScreen$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/init/$ModernFixClientForge" {
import {$TickEvent$RenderTickEvent, $TickEvent$RenderTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$RenderTickEvent"
import {$LevelEvent$Unload, $LevelEvent$Unload$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Unload"
import {$ServerStartedEvent, $ServerStartedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartedEvent"
import {$TagsUpdatedEvent, $TagsUpdatedEvent$Type} from "packages/net/minecraftforge/event/$TagsUpdatedEvent"
import {$CustomizeGuiOverlayEvent$DebugText, $CustomizeGuiOverlayEvent$DebugText$Type} from "packages/net/minecraftforge/client/event/$CustomizeGuiOverlayEvent$DebugText"
import {$RecipesUpdatedEvent, $RecipesUpdatedEvent$Type} from "packages/net/minecraftforge/client/event/$RecipesUpdatedEvent"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"
import {$RegisterClientCommandsEvent, $RegisterClientCommandsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientCommandsEvent"

export class $ModernFixClientForge {

constructor()

public "onRenderTickEnd"(event: $TickEvent$RenderTickEvent$Type): void
public "onServerStarting"(event: $ServerStartedEvent$Type): void
public "onRenderOverlay"(event: $CustomizeGuiOverlayEvent$DebugText$Type): void
public "onDisconnect"(event: $LevelEvent$Unload$Type): void
public "onConfigKey"(event: $TickEvent$ClientTickEvent$Type): void
public "onRecipes"(e: $RecipesUpdatedEvent$Type): void
public "onClientChat"(event: $RegisterClientCommandsEvent$Type): void
public "onTags"(e: $TagsUpdatedEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixClientForge$Type = ($ModernFixClientForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixClientForge_ = $ModernFixClientForge$Type;
}}
declare module "packages/org/embeddedt/modernfix/platform/$ModernFixPlatformHooks" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$SearchRegistry, $SearchRegistry$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SearchRegistry$TreeBuilderSupplier, $SearchRegistry$TreeBuilderSupplier$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$TreeBuilderSupplier"
import {$SearchRegistry$Key, $SearchRegistry$Key$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$Key"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export interface $ModernFixPlatformHooks {

 "isDevEnv"(): boolean
 "onLaunchComplete"(): void
 "sendPacket"(arg0: $ServerPlayer$Type, arg1: any): void
 "getPlatformName"(): string
 "getCurrentServer"(): $MinecraftServer
 "isLoadingNormally"(): boolean
 "getGameDirectory"(): $Path
 "modPresent"(arg0: string): boolean
 "getVersionString"(): string
 "isClient"(): boolean
 "isDedicatedServer"(): boolean
 "getCustomModOptions"(): $Multimap<(string), (string)>
 "injectPlatformSpecificHacks"(): void
 "applyASMTransformers"(arg0: string, arg1: $ClassNode$Type): void
 "registerCreativeSearchTrees"(arg0: $SearchRegistry$Type, arg1: $SearchRegistry$TreeBuilderSupplier$Type<($ItemStack$Type)>, arg2: $SearchRegistry$TreeBuilderSupplier$Type<($ItemStack$Type)>, arg3: $BiConsumer$Type<($SearchRegistry$Key$Type<($ItemStack$Type)>), ($List$Type<($ItemStack$Type)>)>): void
 "onServerCommandRegister"(arg0: $Consumer$Type<($CommandDispatcher$Type<($CommandSourceStack$Type)>)>): void
 "isEarlyLoadingNormally"(): boolean
}

export namespace $ModernFixPlatformHooks {
const INSTANCE: $ModernFixPlatformHooks
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixPlatformHooks$Type = ($ModernFixPlatformHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixPlatformHooks_ = $ModernFixPlatformHooks$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/fluid/$EmbeddiumFluidSpriteCache" {
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $EmbeddiumFluidSpriteCache {

constructor()

public "getSprites"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type, arg2: $FluidState$Type): ($TextureAtlasSprite)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumFluidSpriteCache$Type = ($EmbeddiumFluidSpriteCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumFluidSpriteCache_ = $EmbeddiumFluidSpriteCache$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$ChunkMeshEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventHandlerRegistrar, $EventHandlerRegistrar$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar"
import {$MeshAppender, $MeshAppender$Type} from "packages/org/embeddedt/embeddium/api/$MeshAppender"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $ChunkMeshEvent extends $EmbeddiumEvent {
static readonly "BUS": $EventHandlerRegistrar<($ChunkMeshEvent)>

constructor()

public "addMeshAppender"(arg0: $MeshAppender$Type): void
public "getSectionOrigin"(): $SectionPos
public static "post"(arg0: $Level$Type, arg1: $SectionPos$Type): $List<($MeshAppender)>
public "getWorld"(): $Level
public "getListenerList"(): $ListenerList
get "sectionOrigin"(): $SectionPos
get "world"(): $Level
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMeshEvent$Type = ($ChunkMeshEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMeshEvent_ = $ChunkMeshEvent$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/screen/$PromptScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$PromptScreen$Action, $PromptScreen$Action$Type} from "packages/org/embeddedt/embeddium/gui/screen/$PromptScreen$Action"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PromptScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $List$Type<($FormattedText$Type)>, arg2: integer, arg3: integer, arg4: $PromptScreen$Action$Type)

public "onClose"(): void
public "m_7856_"(): void
public "getWidgets"(): $List<($AbstractWidget)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
get "widgets"(): $List<($AbstractWidget)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PromptScreen$Type = ($PromptScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PromptScreen_ = $PromptScreen$Type;
}}
declare module "packages/org/embeddedt/modernfix/packet/$EntityIDSyncPacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $EntityIDSyncPacket {

constructor()
constructor(map: $Map$Type<($Class$Type<(any)>), ($List$Type<($Pair$Type<(string), (integer)>)>)>)

public static "deserialize"(buf: $FriendlyByteBuf$Type): $EntityIDSyncPacket
public "getFieldInfo"(): $Map<($Class<(any)>), ($List<($Pair<(string), (integer)>)>)>
public "serialize"(buf: $FriendlyByteBuf$Type): void
get "fieldInfo"(): $Map<($Class<(any)>), ($List<($Pair<(string), (integer)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityIDSyncPacket$Type = ($EntityIDSyncPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityIDSyncPacket_ = $EntityIDSyncPacket$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/classloading/$FastAccessTransformerList" {
import {$AccessTransformerList, $AccessTransformerList$Type} from "packages/net/minecraftforge/accesstransformer/parser/$AccessTransformerList"
import {$Type, $Type$Type} from "packages/org/objectweb/asm/$Type"

export class $FastAccessTransformerList extends $AccessTransformerList {

constructor()

public static "attemptReplace"(): void
public "containsClassTarget"(type: $Type$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastAccessTransformerList$Type = ($FastAccessTransformerList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastAccessTransformerList_ = $FastAccessTransformerList$Type;
}}
declare module "packages/org/embeddedt/modernfix/resources/$ReloadExecutor" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"

export class $ReloadExecutor {

constructor()

public static "createCustomResourceReloadExecutor"(): $ExecutorService
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadExecutor$Type = ($ReloadExecutor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadExecutor_ = $ReloadExecutor$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry" {
import {$BlockRendererRegistry$RenderPopulator, $BlockRendererRegistry$RenderPopulator$Type} from "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$RenderPopulator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockRenderContext, $BlockRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext"
import {$BlockRendererRegistry$Renderer, $BlockRendererRegistry$Renderer$Type} from "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$Renderer"

export class $BlockRendererRegistry {


public "registerRenderPopulator"(arg0: $BlockRendererRegistry$RenderPopulator$Type): void
public static "instance"(): $BlockRendererRegistry
public "fillCustomRenderers"(arg0: $List$Type<($BlockRendererRegistry$Renderer$Type)>, arg1: $BlockRenderContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRendererRegistry$Type = ($BlockRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRendererRegistry_ = $BlockRendererRegistry$Type;
}}
declare module "packages/org/embeddedt/embeddium/taint/mixin/$MixinTaintDetector$EnforceLevel" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MixinTaintDetector$EnforceLevel extends $Enum<($MixinTaintDetector$EnforceLevel)> {
static readonly "IGNORE": $MixinTaintDetector$EnforceLevel
static readonly "WARN": $MixinTaintDetector$EnforceLevel
static readonly "CRASH": $MixinTaintDetector$EnforceLevel


public static "values"(): ($MixinTaintDetector$EnforceLevel)[]
public static "valueOf"(arg0: string): $MixinTaintDetector$EnforceLevel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinTaintDetector$EnforceLevel$Type = (("warn") | ("ignore") | ("crash")) | ($MixinTaintDetector$EnforceLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinTaintDetector$EnforceLevel_ = $MixinTaintDetector$EnforceLevel$Type;
}}
declare module "packages/org/embeddedt/embeddium/model/$UnwrappableBakedModel" {
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"

export interface $UnwrappableBakedModel {

 "embeddium$getInnerModel"(arg0: $RandomSource$Type): $BakedModel

(arg0: $BakedModel$Type, arg1: $RandomSource$Type): $BakedModel
}

export namespace $UnwrappableBakedModel {
function unwrapIfPossible(arg0: $BakedModel$Type, arg1: $RandomSource$Type): $BakedModel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnwrappableBakedModel$Type = ($UnwrappableBakedModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnwrappableBakedModel_ = $UnwrappableBakedModel$Type;
}}
declare module "packages/org/embeddedt/modernfix/resources/$ICachingResourcePack" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ICachingResourcePack {

 "invalidateCache"(): void

(): void
}

export namespace $ICachingResourcePack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICachingResourcePack$Type = ($ICachingResourcePack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICachingResourcePack_ = $ICachingResourcePack$Type;
}}
declare module "packages/org/embeddedt/embeddium/compat/immersive/$ImmersiveCompat" {
import {$RegisterClientReloadListenersEvent, $RegisterClientReloadListenersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientReloadListenersEvent"

export class $ImmersiveCompat {

constructor()

public static "onResourceReload"(arg0: $RegisterClientReloadListenersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmersiveCompat$Type = ($ImmersiveCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmersiveCompat_ = $ImmersiveCompat$Type;
}}
declare module "packages/org/embeddedt/modernfix/tickables/$LoadableTickableObject" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$TickableObject, $TickableObject$Type} from "packages/org/embeddedt/modernfix/tickables/$TickableObject"

export class $LoadableTickableObject<T> implements $TickableObject {

constructor(timeout: integer, loader: $Supplier$Type<(T)>, finalizer: $Consumer$Type<(T)>)
constructor(timeout: integer, loader: $Supplier$Type<(T)>, finalizer: $Consumer$Type<(T)>, initialValue: T)

public "get"(): T
public "tick"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadableTickableObject$Type<T> = ($LoadableTickableObject<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadableTickableObject_<T> = $LoadableTickableObject$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/util/$CommonModUtil" {
import {$CommonModUtil$SafeRunnable, $CommonModUtil$SafeRunnable$Type} from "packages/org/embeddedt/modernfix/util/$CommonModUtil$SafeRunnable"

export class $CommonModUtil {

constructor()

public static "runWithoutCrash"(r: $CommonModUtil$SafeRunnable$Type, errorMsg: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonModUtil$Type = ($CommonModUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonModUtil_ = $CommonModUtil$Type;
}}
declare module "packages/org/embeddedt/embeddium/tags/$EmbeddiumTags" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $EmbeddiumTags {
static readonly "RENDERS_WITH_VANILLA": $TagKey<($Fluid)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumTags$Type = ($EmbeddiumTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumTags_ = $EmbeddiumTags$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/mixin/bugfix/entity_pose_stack/$PoseStackAccessor" {
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"

export interface $PoseStackAccessor {

 "getPoseStack"(): $Deque<($PoseStack$Pose)>

(): $Deque<($PoseStack$Pose)>
}

export namespace $PoseStackAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PoseStackAccessor$Type = ($PoseStackAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PoseStackAccessor_ = $PoseStackAccessor$Type;
}}
declare module "packages/org/embeddedt/modernfix/annotation/$IgnoreMixin" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $IgnoreMixin extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $IgnoreMixin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IgnoreMixin$Type = ($IgnoreMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IgnoreMixin_ = $IgnoreMixin$Type;
}}
declare module "packages/org/embeddedt/embeddium/config/$ConfigMigrator" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $ConfigMigrator {
static "LOGGER": $Logger

constructor()

public static "handleConfigMigration"(arg0: string): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigMigrator$Type = ($ConfigMigrator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigMigrator_ = $ConfigMigrator$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$ModelMissingException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $ModelMissingException extends $RuntimeException {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelMissingException$Type = ($ModelMissingException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelMissingException_ = $ModelMissingException$Type;
}}
declare module "packages/org/embeddedt/modernfix/dfu/$DFUBlaster" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DFUBlaster {

constructor()

public static "blastMaps"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DFUBlaster$Type = ($DFUBlaster);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DFUBlaster_ = $DFUBlaster$Type;
}}
declare module "packages/org/embeddedt/embeddium/taint/mixin/$MixinTaintDetector" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$MixinTaintDetector$EnforceLevel, $MixinTaintDetector$EnforceLevel$Type} from "packages/org/embeddedt/embeddium/taint/mixin/$MixinTaintDetector$EnforceLevel"
import {$MixinEnvironment, $MixinEnvironment$Type} from "packages/org/spongepowered/asm/mixin/$MixinEnvironment"
import {$ITargetClassContext, $ITargetClassContext$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$ITargetClassContext"
import {$IExtension, $IExtension$Type} from "packages/org/spongepowered/asm/mixin/transformer/ext/$IExtension"

export class $MixinTaintDetector implements $IExtension {
static readonly "ENFORCE_LEVEL": $MixinTaintDetector$EnforceLevel

constructor()

public static "initialize"(): void
public "export"(arg0: $MixinEnvironment$Type, arg1: string, arg2: boolean, arg3: $ClassNode$Type): void
public "postApply"(arg0: $ITargetClassContext$Type): void
public "preApply"(arg0: $ITargetClassContext$Type): void
public "checkActive"(arg0: $MixinEnvironment$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinTaintDetector$Type = ($MixinTaintDetector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinTaintDetector_ = $MixinTaintDetector$Type;
}}
declare module "packages/org/embeddedt/modernfix/textures/$StbStitcher" {
import {$Stitcher$Entry, $Stitcher$Entry$Type} from "packages/net/minecraft/client/renderer/texture/$Stitcher$Entry"
import {$StbStitcher$LoadableSpriteInfo, $StbStitcher$LoadableSpriteInfo$Type} from "packages/org/embeddedt/modernfix/textures/$StbStitcher$LoadableSpriteInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stitcher$Holder, $Stitcher$Holder$Type} from "packages/net/minecraft/client/renderer/texture/$Stitcher$Holder"
import {$STBRPRect, $STBRPRect$Type} from "packages/org/lwjgl/stb/$STBRPRect"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $StbStitcher {

constructor()

public static "packRects"<T extends $Stitcher$Entry>(holders: ($Stitcher$Holder$Type<(T)>)[]): $Pair<($Pair<(integer), (integer)>), ($List<($StbStitcher$LoadableSpriteInfo<(T)>)>)>
public static "setWrapper"(rect: $STBRPRect$Type, id: integer, width: integer, height: integer, x: integer, y: integer, was_packed: boolean): $STBRPRect
public static "getY"(rect: $STBRPRect$Type): integer
public static "getX"(rect: $STBRPRect$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StbStitcher$Type = ($StbStitcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StbStitcher_ = $StbStitcher$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$EmbeddiumConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EmbeddiumConstants {
static readonly "MODID": string
static readonly "MODNAME": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumConstants$Type = ($EmbeddiumConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumConstants_ = $EmbeddiumConstants$Type;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$DummySearchTree" {
import {$RefreshableSearchTree, $RefreshableSearchTree$Type} from "packages/net/minecraft/client/searchtree/$RefreshableSearchTree"
import {$List, $List$Type} from "packages/java/util/$List"

export class $DummySearchTree<T> implements $RefreshableSearchTree<(T)> {

constructor()

public "refresh"(): void
public "search"(pSearchText: string): $List<(T)>
public static "empty"<T>(): $RefreshableSearchTree<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DummySearchTree$Type<T> = ($DummySearchTree<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DummySearchTree_<T> = $DummySearchTree$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/duck/$ICachedMaterialsModel" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ICachedMaterialsModel {

 "clearMaterialsCache"(): void

(): void
}

export namespace $ICachedMaterialsModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICachedMaterialsModel$Type = ($ICachedMaterialsModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICachedMaterialsModel_ = $ICachedMaterialsModel$Type;
}}
declare module "packages/org/embeddedt/modernfix/textures/$StbStitcher$LoadableSpriteInfo" {
import {$Stitcher$Entry, $Stitcher$Entry$Type} from "packages/net/minecraft/client/renderer/texture/$Stitcher$Entry"

export class $StbStitcher$LoadableSpriteInfo<T extends $Stitcher$Entry> {
readonly "info": T
readonly "width": integer
readonly "height": integer
readonly "x": integer
readonly "y": integer


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StbStitcher$LoadableSpriteInfo$Type<T> = ($StbStitcher$LoadableSpriteInfo<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StbStitcher$LoadableSpriteInfo_<T> = $StbStitcher$LoadableSpriteInfo$Type<(T)>;
}}
declare module "packages/org/embeddedt/embeddium/render/entity/$ModelPartExtended" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ModelPartExtended {

 "embeddium$getPartsList"(): $List<($ModelPart)>
 "embeddium$asOptional"(): $Optional<($ModelPart)>
 "embeddium$getDescendantsByName"(): $Map<(string), ($ModelPart)>
}

export namespace $ModelPartExtended {
function of(arg0: $ModelPart$Type): $ModelPartExtended
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelPartExtended$Type = ($ModelPartExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelPartExtended_ = $ModelPartExtended$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/$ShaderModBridge" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ShaderModBridge {

constructor()

public static "emulateLegacyColorBrightnessFormat"(): boolean
public static "isNvidiumEnabled"(): boolean
public static "areShadersEnabled"(): boolean
public static "openShaderScreen"(arg0: $Screen$Type): any
public static "isShaderModPresent"(): boolean
get "nvidiumEnabled"(): boolean
get "shaderModPresent"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderModBridge$Type = ($ShaderModBridge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderModBridge_ = $ShaderModBridge$Type;
}}
declare module "packages/org/embeddedt/modernfix/dedup/$IdentifierCaches" {
import {$DeduplicationCache, $DeduplicationCache$Type} from "packages/org/embeddedt/modernfix/dedup/$DeduplicationCache"

export class $IdentifierCaches {
static readonly "NAMESPACES": $DeduplicationCache<(string)>
static readonly "PATH": $DeduplicationCache<(string)>
static readonly "PROPERTY": $DeduplicationCache<(string)>

constructor()

public static "printDebug"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IdentifierCaches$Type = ($IdentifierCaches);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IdentifierCaches_ = $IdentifierCaches$Type;
}}
declare module "packages/org/embeddedt/modernfix/platform/forge/$ModernFixPlatformHooksImpl" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$SearchRegistry, $SearchRegistry$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SearchRegistry$TreeBuilderSupplier, $SearchRegistry$TreeBuilderSupplier$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$TreeBuilderSupplier"
import {$SearchRegistry$Key, $SearchRegistry$Key$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$Key"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ModernFixPlatformHooks, $ModernFixPlatformHooks$Type} from "packages/org/embeddedt/modernfix/platform/$ModernFixPlatformHooks"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $ModernFixPlatformHooksImpl implements $ModernFixPlatformHooks {

constructor()

public "isDevEnv"(): boolean
public "onLaunchComplete"(): void
public "sendPacket"(player: $ServerPlayer$Type, packet: any): void
public "getPlatformName"(): string
public "getCurrentServer"(): $MinecraftServer
public "isLoadingNormally"(): boolean
public "getGameDirectory"(): $Path
public "modPresent"(modId: string): boolean
public "getVersionString"(): string
public "isClient"(): boolean
public "isDedicatedServer"(): boolean
public "getCustomModOptions"(): $Multimap<(string), (string)>
public "injectPlatformSpecificHacks"(): void
public "applyASMTransformers"(mixinClassName: string, targetClass: $ClassNode$Type): void
public "registerCreativeSearchTrees"(registry: $SearchRegistry$Type, nameSupplier: $SearchRegistry$TreeBuilderSupplier$Type<($ItemStack$Type)>, tagSupplier: $SearchRegistry$TreeBuilderSupplier$Type<($ItemStack$Type)>, populator: $BiConsumer$Type<($SearchRegistry$Key$Type<($ItemStack$Type)>), ($List$Type<($ItemStack$Type)>)>): void
public "onServerCommandRegister"(handler: $Consumer$Type<($CommandDispatcher$Type<($CommandSourceStack$Type)>)>): void
public "isEarlyLoadingNormally"(): boolean
get "devEnv"(): boolean
get "platformName"(): string
get "currentServer"(): $MinecraftServer
get "loadingNormally"(): boolean
get "gameDirectory"(): $Path
get "versionString"(): string
get "client"(): boolean
get "dedicatedServer"(): boolean
get "customModOptions"(): $Multimap<(string), (string)>
get "earlyLoadingNormally"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixPlatformHooksImpl$Type = ($ModernFixPlatformHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixPlatformHooksImpl_ = $ModernFixPlatformHooksImpl$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$ClassInfoManager" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClassInfoManager {

constructor()

public static "clear"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassInfoManager$Type = ($ClassInfoManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassInfoManager_ = $ClassInfoManager$Type;
}}
declare module "packages/org/embeddedt/modernfix/entity/$EntityDataIDSyncHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $EntityDataIDSyncHandler {

constructor()

public static "onDatapackSyncEvent"(targetPlayer: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityDataIDSyncHandler$Type = ($EntityDataIDSyncHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityDataIDSyncHandler_ = $EntityDataIDSyncHandler$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/tab/$TabFrame$Builder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Tab, $Tab$Type} from "packages/org/embeddedt/embeddium/gui/frame/tab/$Tab"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$TabFrame, $TabFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/tab/$TabFrame"
import {$AtomicReference, $AtomicReference$Type} from "packages/java/util/concurrent/atomic/$AtomicReference"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $TabFrame$Builder {

constructor()

public "build"(): $TabFrame
public "setDimension"(arg0: $Dim2i$Type): $TabFrame$Builder
public "shouldRenderOutline"(arg0: boolean): $TabFrame$Builder
public "setTabSectionScrollBarOffset"(arg0: $AtomicReference$Type<(integer)>): $TabFrame$Builder
public "setTabSectionSelectedTab"(arg0: $AtomicReference$Type<($Component$Type)>): $TabFrame$Builder
public "onSetTab"(arg0: $Runnable$Type): $TabFrame$Builder
public "addTabs"(arg0: $Consumer$Type<($Multimap$Type<(string), ($Tab$Type<(any)>)>)>): $TabFrame$Builder
set "dimension"(value: $Dim2i$Type)
set "tabSectionScrollBarOffset"(value: $AtomicReference$Type<(integer)>)
set "tabSectionSelectedTab"(value: $AtomicReference$Type<($Component$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TabFrame$Builder$Type = ($TabFrame$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TabFrame$Builder_ = $TabFrame$Builder$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/classloading/$ClassLoadHack" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClassLoadHack {

constructor()

public static "loadModClasses"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassLoadHack$Type = ($ClassLoadHack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassLoadHack_ = $ClassLoadHack$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar$Handler" {
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"

export interface $EventHandlerRegistrar$Handler<T extends $EmbeddiumEvent> {

 "acceptEvent"(arg0: T): void

(arg0: T): void
}

export namespace $EventHandlerRegistrar$Handler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerRegistrar$Handler$Type<T> = ($EventHandlerRegistrar$Handler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerRegistrar$Handler_<T> = $EventHandlerRegistrar$Handler$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/blockstate/$FerriteCorePostProcess" {
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$StateHolder, $StateHolder$Type} from "packages/net/minecraft/world/level/block/state/$StateHolder"

export class $FerriteCorePostProcess {

constructor()

public static "postProcess"<O, S extends $StateHolder<(O), (S)>>(state: $StateDefinition$Type<(O), (S)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FerriteCorePostProcess$Type = ($FerriteCorePostProcess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FerriteCorePostProcess_ = $FerriteCorePostProcess$Type;
}}
declare module "packages/org/embeddedt/modernfix/render/$UnsafeBufferHelper" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $UnsafeBufferHelper {

constructor()

public static "init"(): void
public static "free"(buf: $ByteBuffer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnsafeBufferHelper$Type = ($UnsafeBufferHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnsafeBufferHelper_ = $UnsafeBufferHelper$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/theme/$DefaultColors" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DefaultColors {
static readonly "ELEMENT_ACTIVATED": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultColors$Type = ($DefaultColors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultColors_ = $DefaultColors$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$OptionPageConstructionEvent" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventHandlerRegistrar, $EventHandlerRegistrar$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar"
import {$OptionGroup, $OptionGroup$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionGroup"

export class $OptionPageConstructionEvent extends $EmbeddiumEvent {
static readonly "BUS": $EventHandlerRegistrar<($OptionPageConstructionEvent)>

constructor(arg0: $OptionIdentifier$Type<(void)>, arg1: $Component$Type)
constructor()

public "getName"(): $Component
public "getId"(): $OptionIdentifier<(void)>
public "addGroup"(arg0: $OptionGroup$Type): void
public "getAdditionalGroups"(): $List<($OptionGroup)>
public "getListenerList"(): $ListenerList
get "name"(): $Component
get "id"(): $OptionIdentifier<(void)>
get "additionalGroups"(): $List<($OptionGroup)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionPageConstructionEvent$Type = ($OptionPageConstructionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionPageConstructionEvent_ = $OptionPageConstructionEvent$Type;
}}
declare module "packages/org/embeddedt/modernfix/$ModernFix" {
import {$ExecutorService, $ExecutorService$Type} from "packages/java/util/concurrent/$ExecutorService"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $ModernFix {
static readonly "LOGGER": $Logger
static readonly "MODID": string
static "NAME": string
static "INSTANCE": $ModernFix
static "runningFirstInjection": boolean

constructor()

public "onServerDead"(server: $MinecraftServer$Type): void
public "onServerStarted"(): void
public static "resourceReloadExecutor"(): $ExecutorService
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFix$Type = ($ModernFix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFix_ = $ModernFix$Type;
}}
declare module "packages/org/embeddedt/modernfix/render/$RenderState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RenderState {
static "IS_RENDERING_LEVEL": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderState$Type = ($RenderState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderState_ = $RenderState$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$BasicFrame" {
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$BasicFrame$Builder, $BasicFrame$Builder$Type} from "packages/org/embeddedt/embeddium/gui/frame/$BasicFrame$Builder"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BasicFrame extends $AbstractFrame {

constructor(arg0: $Dim2i$Type, arg1: boolean, arg2: $List$Type<($Function$Type<($Dim2i$Type), ($AbstractWidget$Type)>)>)

public "buildFrame"(): void
public static "createBuilder"(): $BasicFrame$Builder
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setFocused"(arg0: boolean): void
public "isFocused"(): boolean
set "focused"(value: boolean)
get "focused"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicFrame$Type = ($BasicFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicFrame_ = $BasicFrame$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/dynresources/$IModelBakerImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IModelBakerImpl {

 "mfix$ignoreCache"(): void

(): void
}

export namespace $IModelBakerImpl {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModelBakerImpl$Type = ($IModelBakerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModelBakerImpl_ = $IModelBakerImpl$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$ScrollableFrame" {
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$ScrollableFrame$Builder, $ScrollableFrame$Builder$Type} from "packages/org/embeddedt/embeddium/gui/frame/$ScrollableFrame$Builder"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$AtomicReference, $AtomicReference$Type} from "packages/java/util/concurrent/atomic/$AtomicReference"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ScrollableFrame extends $AbstractFrame {

constructor(arg0: $Dim2i$Type, arg1: $AbstractFrame$Type, arg2: boolean, arg3: $AtomicReference$Type<(integer)>, arg4: $AtomicReference$Type<(integer)>)

public "buildFrame"(): void
public "setupFrame"(arg0: $AtomicReference$Type<(integer)>, arg1: $AtomicReference$Type<(integer)>): void
public static "createBuilder"(): $ScrollableFrame$Builder
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "setFocused"(arg0: boolean): void
public "isFocused"(): boolean
set "focused"(value: boolean)
get "focused"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollableFrame$Type = ($ScrollableFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollableFrame_ = $ScrollableFrame$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$DynamicSoundHelpers" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DynamicSoundHelpers {
static readonly "MAX_SOUND_LIFETIME_SECS": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicSoundHelpers$Type = ($DynamicSoundHelpers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicSoundHelpers_ = $DynamicSoundHelpers$Type;
}}
declare module "packages/org/embeddedt/modernfix/core/config/$OptionCategories" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $OptionCategories {

constructor()

public static "load"(): void
public static "getCategoriesInOrder"(): $List<(string)>
public static "getCategoryForOption"(optionName: string): string
get "categoriesInOrder"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionCategories$Type = ($OptionCategories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionCategories_ = $OptionCategories$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IChunkGenerator" {
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $IChunkGenerator {

 "mfix$setAssociatedServerLevel"(arg0: $ServerLevel$Type): void

(arg0: $ServerLevel$Type): void
}

export namespace $IChunkGenerator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IChunkGenerator$Type = ($IChunkGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IChunkGenerator_ = $IChunkGenerator$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$DynamicModelCache" {
import {$Function, $Function$Type} from "packages/it/unimi/dsi/fastutil/$Function"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"

export class $DynamicModelCache<K> {

constructor(modelRetriever: $Function$Type<(K), ($BakedModel$Type)>, allowNulls: boolean)

public "get"(key: K): $BakedModel
public "clear"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicModelCache$Type<K> = ($DynamicModelCache<(K)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicModelCache_<K> = $DynamicModelCache$Type<(K)>;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$SearchTreeProviderRegistry" {
import {$SearchTreeProviderRegistry$Provider, $SearchTreeProviderRegistry$Provider$Type} from "packages/org/embeddedt/modernfix/searchtree/$SearchTreeProviderRegistry$Provider"

export class $SearchTreeProviderRegistry {

constructor()

public static "register"(p: $SearchTreeProviderRegistry$Provider$Type): void
public static "getSearchTreeProvider"(): $SearchTreeProviderRegistry$Provider
get "searchTreeProvider"(): $SearchTreeProviderRegistry$Provider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchTreeProviderRegistry$Type = ($SearchTreeProviderRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchTreeProviderRegistry_ = $SearchTreeProviderRegistry$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$TranslucentQuadAnalyzer$Level, $TranslucentQuadAnalyzer$Level$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$Level"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"

export class $TranslucentQuadAnalyzer$SortState extends $Record {
static readonly "NONE": $TranslucentQuadAnalyzer$SortState

constructor(level: $TranslucentQuadAnalyzer$Level$Type, centers: (float)[], normalSigns: $BitSet$Type, sharedNormal: $Vector3f$Type)

public "requiresDynamicSorting"(): boolean
public "sharedNormal"(): $Vector3f
public "compactForStorage"(): $TranslucentQuadAnalyzer$SortState
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "level"(): $TranslucentQuadAnalyzer$Level
public "centers"(): (float)[]
public "normalSigns"(): $BitSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TranslucentQuadAnalyzer$SortState$Type = ($TranslucentQuadAnalyzer$SortState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TranslucentQuadAnalyzer$SortState_ = $TranslucentQuadAnalyzer$SortState$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/frapi/$SpriteFinderCache$Finder" {
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $SpriteFinderCache$Finder {

 "findNearestSprite"(arg0: float, arg1: float): $TextureAtlasSprite

(arg0: float, arg1: float): $TextureAtlasSprite
}

export namespace $SpriteFinderCache$Finder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteFinderCache$Finder$Type = ($SpriteFinderCache$Finder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteFinderCache$Finder_ = $SpriteFinderCache$Finder$Type;
}}
declare module "packages/org/embeddedt/modernfix/annotation/$ClientOnlyMixin" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ClientOnlyMixin extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ClientOnlyMixin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientOnlyMixin$Type = ($ClientOnlyMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientOnlyMixin_ = $ClientOnlyMixin$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/reuse_datapacks/$ICachingResourceClient" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ReloadableServerResources, $ReloadableServerResources$Type} from "packages/net/minecraft/server/$ReloadableServerResources"

export interface $ICachingResourceClient {

 "setCachedDataPackConfig"(arg0: $Collection$Type<(string)>): void
 "setCachedResources"(arg0: $ReloadableServerResources$Type): void
}

export namespace $ICachingResourceClient {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICachingResourceClient$Type = ($ICachingResourceClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICachingResourceClient_ = $ICachingResourceClient$Type;
}}
declare module "packages/org/embeddedt/modernfix/resources/$PackResourcesCacheEngine" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ICachingResourcePack, $ICachingResourcePack$Type} from "packages/org/embeddedt/modernfix/resources/$ICachingResourcePack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $PackResourcesCacheEngine {

constructor(namespacesRetriever: $Function$Type<($PackType$Type), ($Set$Type<(string)>)>, basePathRetriever: $BiFunction$Type<($PackType$Type), (string), ($Path$Type)>)

public "getResources"(type: $PackType$Type, resourceNamespace: string, pathIn: string, maxDepth: integer, filter: $Predicate$Type<($ResourceLocation$Type)>): $Collection<($ResourceLocation)>
public static "invalidate"(): void
public "getNamespaces"(type: $PackType$Type): $Set<(string)>
public static "track"(pack: $ICachingResourcePack$Type): void
public "hasResource"(paths: (string)[]): boolean
public "hasResource"(path: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackResourcesCacheEngine$Type = ($PackResourcesCacheEngine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackResourcesCacheEngine_ = $PackResourcesCacheEngine$Type;
}}
declare module "packages/org/embeddedt/modernfix/dedup/$DeduplicationCache" {
import {$Hash$Strategy, $Hash$Strategy$Type} from "packages/it/unimi/dsi/fastutil/$Hash$Strategy"

export class $DeduplicationCache<T> {

constructor(strategy: $Hash$Strategy$Type<(T)>)
constructor()

public "toString"(): string
public "clearCache"(): void
public "deduplicate"(item: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeduplicationCache$Type<T> = ($DeduplicationCache<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeduplicationCache_<T> = $DeduplicationCache$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$RecipeBookSearchTree" {
import {$RecipeCollection, $RecipeCollection$Type} from "packages/net/minecraft/client/gui/screens/recipebook/$RecipeCollection"
import {$DummySearchTree, $DummySearchTree$Type} from "packages/org/embeddedt/modernfix/searchtree/$DummySearchTree"
import {$SearchTree, $SearchTree$Type} from "packages/net/minecraft/client/searchtree/$SearchTree"
import {$RefreshableSearchTree, $RefreshableSearchTree$Type} from "packages/net/minecraft/client/searchtree/$RefreshableSearchTree"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $RecipeBookSearchTree extends $DummySearchTree<($RecipeCollection)> {

constructor(stackCollector: $SearchTree$Type<($ItemStack$Type)>, allCollections: $List$Type<($RecipeCollection$Type)>)

public "refresh"(): void
public "search"(pSearchText: string): $List<($RecipeCollection)>
public static "empty"<T>(): $RefreshableSearchTree<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeBookSearchTree$Type = ($RecipeBookSearchTree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeBookSearchTree_ = $RecipeBookSearchTree$Type;
}}
declare module "packages/org/embeddedt/modernfix/registry/$LifecycleMap" {
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Reference2ReferenceOpenHashMap, $Reference2ReferenceOpenHashMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2ReferenceOpenHashMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $LifecycleMap<T> extends $Reference2ReferenceOpenHashMap<(T), ($Lifecycle)> {

constructor()

public "put"(t: T, lifecycle: $Lifecycle$Type): $Lifecycle
public "defaultReturnValue"(arg0: V): void
public "defaultReturnValue"(): V
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
public static "identity"<T>(): $Function<(T), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LifecycleMap$Type<T> = ($LifecycleMap<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LifecycleMap_<T> = $LifecycleMap$Type<(T)>;
}}
declare module "packages/org/embeddedt/modernfix/forge/packet/$NetworkUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NetworkUtils {
static "isCurrentlyVanilla": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkUtils$Type = ($NetworkUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkUtils_ = $NetworkUtils$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$CommonModUtil$SafeRunnable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CommonModUtil$SafeRunnable {

 "run"(): void

(): void
}

export namespace $CommonModUtil$SafeRunnable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonModUtil$SafeRunnable$Type = ($CommonModUtil$SafeRunnable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonModUtil$SafeRunnable_ = $CommonModUtil$SafeRunnable$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$DynamicModelProvider" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DynamicModelProvider {

constructor(initialModels: $Map$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>)

public "getModel"(location: $ResourceLocation$Type): $UnbakedModel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicModelProvider$Type = ($DynamicModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicModelProvider_ = $DynamicModelProvider$Type;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$SearchTreeProviderRegistry$Provider" {
import {$RefreshableSearchTree, $RefreshableSearchTree$Type} from "packages/net/minecraft/client/searchtree/$RefreshableSearchTree"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $SearchTreeProviderRegistry$Provider {

 "getName"(): string
 "canUse"(): boolean
 "getSearchTree"(arg0: boolean): $RefreshableSearchTree<($ItemStack)>
}

export namespace $SearchTreeProviderRegistry$Provider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchTreeProviderRegistry$Provider$Type = ($SearchTreeProviderRegistry$Provider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchTreeProviderRegistry$Provider_ = $SearchTreeProviderRegistry$Provider$Type;
}}
declare module "packages/org/embeddedt/modernfix/render/$SimpleItemModelView" {
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$ItemTransforms, $ItemTransforms$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemTransforms"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$FastItemRenderType, $FastItemRenderType$Type} from "packages/org/embeddedt/modernfix/render/$FastItemRenderType"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ItemOverrides, $ItemOverrides$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemOverrides"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $SimpleItemModelView implements $BakedModel {

constructor()

public "setType"(type: $FastItemRenderType$Type): void
public "usesBlockLight"(): boolean
public "isGui3d"(): boolean
public "getParticleIcon"(): $TextureAtlasSprite
public "getQuads"(state: $BlockState$Type, side: $Direction$Type, rand: $RandomSource$Type): $List<($BakedQuad)>
public "isCustomRenderer"(): boolean
public "getOverrides"(): $ItemOverrides
public "setItem"(model: $BakedModel$Type): void
public "getTransforms"(): $ItemTransforms
public "useAmbientOcclusion"(): boolean
public "useAmbientOcclusion"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
public "useAmbientOcclusion"(arg0: $BlockState$Type): boolean
public "getRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
public "getRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type, arg2: $ModelData$Type): $ChunkRenderTypeSet
public "getRenderPasses"(arg0: $ItemStack$Type, arg1: boolean): $List<($BakedModel)>
public "applyTransform"(arg0: $ItemDisplayContext$Type, arg1: $PoseStack$Type, arg2: boolean): $BakedModel
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type, arg3: $ModelData$Type, arg4: $RenderType$Type): $List<($BakedQuad)>
public "getParticleIcon"(arg0: $ModelData$Type): $TextureAtlasSprite
public "getModelData"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ModelData$Type): $ModelData
public "useAmbientOcclusionWithLightEmission"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
set "type"(value: $FastItemRenderType$Type)
get "gui3d"(): boolean
get "particleIcon"(): $TextureAtlasSprite
get "customRenderer"(): boolean
get "overrides"(): $ItemOverrides
set "item"(value: $BakedModel$Type)
get "transforms"(): $ItemTransforms
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleItemModelView$Type = ($SimpleItemModelView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleItemModelView_ = $SimpleItemModelView$Type;
}}
declare module "packages/org/embeddedt/modernfix/dfu/$LazyDataFixer" {
import {$DSL$TypeReference, $DSL$TypeReference$Type} from "packages/com/mojang/datafixers/$DSL$TypeReference"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Schema, $Schema$Type} from "packages/com/mojang/datafixers/schemas/$Schema"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$DataFixer, $DataFixer$Type} from "packages/com/mojang/datafixers/$DataFixer"

export class $LazyDataFixer implements $DataFixer {

constructor(dfuSupplier: $Supplier$Type<($DataFixer$Type)>)

public "update"<T>(type: $DSL$TypeReference$Type, input: $Dynamic$Type<(T)>, version: integer, newVersion: integer): $Dynamic<(T)>
public "getSchema"(key: integer): $Schema
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LazyDataFixer$Type = ($LazyDataFixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LazyDataFixer_ = $LazyDataFixer$Type;
}}
declare module "packages/org/embeddedt/modernfix/api/constants/$IntegrationConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $IntegrationConstants {
static readonly "INTEGRATIONS_KEY": string
static readonly "CLIENT_INTEGRATION_CLASS": string
static readonly "INTEGRATION_CLASS": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegrationConstants$Type = ($IntegrationConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegrationConstants_ = $IntegrationConstants$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$FileUtil" {
import {$File, $File$Type} from "packages/java/io/$File"

export class $FileUtil {

constructor()

public static "childFile"(file: $File$Type): $File
public static "normalize"(path: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileUtil$Type = ($FileUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileUtil_ = $FileUtil$Type;
}}
declare module "packages/org/embeddedt/modernfix/entity/$EntityRendererMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $EntityRendererMap implements $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)> {

constructor(rendererProviders: $Map$Type<($EntityType$Type<(any)>), ($EntityRendererProvider$Type<(any)>)>, context: $EntityRendererProvider$Context$Type)

public "remove"(o: any): $EntityRenderer<(any)>
public "get"(o: any): $EntityRenderer<(any)>
public "put"(entityType: $EntityType$Type<(any)>, entityRenderer: $EntityRenderer$Type<(any)>): $EntityRenderer<(any)>
public "values"(): $Collection<($EntityRenderer<(any)>)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<($EntityType<(any)>), ($EntityRenderer<(any)>)>)>
public "putAll"(map: $Map$Type<(any), (any)>): void
public "containsKey"(o: any): boolean
public "keySet"(): $Set<($EntityType<(any)>)>
public "containsValue"(o: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public "replace"(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>): $EntityRenderer<(any)>
public "replace"(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityRenderer$Type<(any)>): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>, arg8: $EntityType$Type<(any)>, arg9: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>, arg8: $EntityType$Type<(any)>, arg9: $EntityRenderer$Type<(any)>, arg10: $EntityType$Type<(any)>, arg11: $EntityRenderer$Type<(any)>, arg12: $EntityType$Type<(any)>, arg13: $EntityRenderer$Type<(any)>, arg14: $EntityType$Type<(any)>, arg15: $EntityRenderer$Type<(any)>, arg16: $EntityType$Type<(any)>, arg17: $EntityRenderer$Type<(any)>, arg18: $EntityType$Type<(any)>, arg19: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>, arg8: $EntityType$Type<(any)>, arg9: $EntityRenderer$Type<(any)>, arg10: $EntityType$Type<(any)>, arg11: $EntityRenderer$Type<(any)>, arg12: $EntityType$Type<(any)>, arg13: $EntityRenderer$Type<(any)>, arg14: $EntityType$Type<(any)>, arg15: $EntityRenderer$Type<(any)>, arg16: $EntityType$Type<(any)>, arg17: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>, arg8: $EntityType$Type<(any)>, arg9: $EntityRenderer$Type<(any)>, arg10: $EntityType$Type<(any)>, arg11: $EntityRenderer$Type<(any)>, arg12: $EntityType$Type<(any)>, arg13: $EntityRenderer$Type<(any)>, arg14: $EntityType$Type<(any)>, arg15: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>, arg8: $EntityType$Type<(any)>, arg9: $EntityRenderer$Type<(any)>, arg10: $EntityType$Type<(any)>, arg11: $EntityRenderer$Type<(any)>, arg12: $EntityType$Type<(any)>, arg13: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public static "of"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $EntityType$Type<(any)>, arg3: $EntityRenderer$Type<(any)>, arg4: $EntityType$Type<(any)>, arg5: $EntityRenderer$Type<(any)>, arg6: $EntityType$Type<(any)>, arg7: $EntityRenderer$Type<(any)>, arg8: $EntityType$Type<(any)>, arg9: $EntityRenderer$Type<(any)>, arg10: $EntityType$Type<(any)>, arg11: $EntityRenderer$Type<(any)>): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public "merge"(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>, arg2: $BiFunction$Type<(any), (any), (any)>): $EntityRenderer<(any)>
public "putIfAbsent"(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>): $EntityRenderer<(any)>
public "compute"(arg0: $EntityType$Type<(any)>, arg1: $BiFunction$Type<(any), (any), (any)>): $EntityRenderer<(any)>
public static "entry"<K, V>(arg0: $EntityType$Type<(any)>, arg1: $EntityRenderer$Type<(any)>): $Map$Entry<($EntityType<(any)>), ($EntityRenderer<(any)>)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: $EntityType$Type<(any)>, arg1: $Function$Type<(any), (any)>): $EntityRenderer<(any)>
public "getOrDefault"(arg0: any, arg1: $EntityRenderer$Type<(any)>): $EntityRenderer<(any)>
public "computeIfPresent"(arg0: $EntityType$Type<(any)>, arg1: $BiFunction$Type<(any), (any), (any)>): $EntityRenderer<(any)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<($EntityType<(any)>), ($EntityRenderer<(any)>)>
[index: string | number]: $EntityRenderer<(any)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRendererMap$Type = ($EntityRendererMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRendererMap_ = $EntityRendererMap$Type;
}}
declare module "packages/org/embeddedt/embeddium/compat/ccl/$CCLCompatBootstrap" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $CCLCompatBootstrap {

constructor()

public static "onClientSetup"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CCLCompatBootstrap$Type = ($CCLCompatBootstrap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CCLCompatBootstrap_ = $CCLCompatBootstrap$Type;
}}
declare module "packages/org/embeddedt/modernfix/api/entrypoint/$ModernFixClientIntegration" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $ModernFixClientIntegration {

 "onUnbakedModelLoad"(location: $ResourceLocation$Type, originalModel: $UnbakedModel$Type, bakery: $ModelBakery$Type): $UnbakedModel
 "onBakedModelLoad"(location: $ResourceLocation$Type, baseModel: $UnbakedModel$Type, originalModel: $BakedModel$Type, state: $ModelState$Type, bakery: $ModelBakery$Type, textureGetter: $Function$Type<($Material$Type), ($TextureAtlasSprite$Type)>): $BakedModel
/**
 * 
 * @deprecated
 */
 "onBakedModelLoad"(location: $ResourceLocation$Type, baseModel: $UnbakedModel$Type, originalModel: $BakedModel$Type, state: $ModelState$Type, bakery: $ModelBakery$Type): $BakedModel
 "onUnbakedModelPreBake"(location: $ResourceLocation$Type, originalModel: $UnbakedModel$Type, bakery: $ModelBakery$Type): $UnbakedModel
 "onDynamicResourcesStatusChange"(enabled: boolean): void
}

export namespace $ModernFixClientIntegration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixClientIntegration$Type = ($ModernFixClientIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixClientIntegration_ = $ModernFixClientIntegration$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/frapi/$SpriteFinderCache" {
import {$SpriteFinderCache$Finder, $SpriteFinderCache$Finder$Type} from "packages/org/embeddedt/embeddium/render/frapi/$SpriteFinderCache$Finder"
import {$RegisterClientReloadListenersEvent, $RegisterClientReloadListenersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientReloadListenersEvent"

export class $SpriteFinderCache {

constructor()

public static "onReload"(arg0: $RegisterClientReloadListenersEvent$Type): void
public static "forBlockAtlas"(): $SpriteFinderCache$Finder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteFinderCache$Type = ($SpriteFinderCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteFinderCache_ = $SpriteFinderCache$Type;
}}
declare module "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdGenerator" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"

export class $OptionIdGenerator {

constructor()

public static "generateId"<T>(arg0: string): $OptionIdentifier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionIdGenerator$Type = ($OptionIdGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionIdGenerator_ = $OptionIdGenerator$Type;
}}
declare module "packages/org/embeddedt/modernfix/core/config/$ModernFixEarlyConfig" {
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Option, $Option$Type} from "packages/org/embeddedt/modernfix/core/config/$Option"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModernFixEarlyConfig {
static readonly "OPTIFINE_PRESENT": boolean
static "isFabric": boolean


public static "load"(file: $File$Type): $ModernFixEarlyConfig
public "save"(): void
public "getOptionCount"(): integer
public "getOptionMap"(): $Map<(string), ($Option)>
public static "sanitize"(mixinClassName: string): string
public "getPermanentlyDisabledMixins"(): $Map<(string), (string)>
public "getOptionOverrideCount"(): integer
public "getEffectiveOptionForMixin"(mixinClassName: string): $Option
public "getOptionCategoryMap"(): $Multimap<(string), ($Option)>
get "optionCount"(): integer
get "optionMap"(): $Map<(string), ($Option)>
get "permanentlyDisabledMixins"(): $Map<(string), (string)>
get "optionOverrideCount"(): integer
get "optionCategoryMap"(): $Multimap<(string), ($Option)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixEarlyConfig$Type = ($ModernFixEarlyConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixEarlyConfig_ = $ModernFixEarlyConfig$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$LambdaMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LambdaMap<K, V> implements $Map<(K), (V)> {

constructor(supplier: $Function$Type<(K), (V)>)

public "remove"(o: any): V
public "get"(o: any): V
public "put"(k: K, v: V): V
public "values"(): $Collection<(V)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(map: $Map$Type<(any), (any)>): void
public "containsKey"(o: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(o: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
[index: string | number]: V
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LambdaMap$Type<K, V> = ($LambdaMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LambdaMap_<K, V> = $LambdaMap$Type<(K), (V)>;
}}
declare module "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"

export class $EmbeddiumEvent extends $Event {

constructor()

public "setCanceled"(arg0: boolean): void
public "isCanceled"(): boolean
public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
set "canceled"(value: boolean)
get "canceled"(): boolean
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumEvent$Type = ($EmbeddiumEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumEvent_ = $EmbeddiumEvent$Type;
}}
declare module "packages/org/embeddedt/modernfix/$ModernFixClient" {
import {$ModernFixClientIntegration, $ModernFixClientIntegration$Type} from "packages/org/embeddedt/modernfix/api/entrypoint/$ModernFixClientIntegration"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$EntityIDSyncPacket, $EntityIDSyncPacket$Type} from "packages/org/embeddedt/modernfix/packet/$EntityIDSyncPacket"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$SynchedEntityData, $SynchedEntityData$Type} from "packages/net/minecraft/network/syncher/$SynchedEntityData"

export class $ModernFixClient {
static "INSTANCE": $ModernFixClient
static "worldLoadStartTime": long
static "gameStartTimeSeconds": float
static "recipesUpdated": boolean
static "tagsUpdated": boolean
 "brandingString": string
static "CLIENT_INTEGRATIONS": $List<($ModernFixClientIntegration)>
static readonly "allEntityDatas": $Set<($SynchedEntityData)>

constructor()

public static "handleEntityIDSync"(packet: $EntityIDSyncPacket$Type): void
public "onGameLaunchFinish"(): void
public "onServerStarted"(server: $MinecraftServer$Type): void
public "onTagsUpdated"(): void
public "onRenderTickEnd"(): void
public "resetWorldLoadStateMachine"(): void
public "onRecipesUpdated"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixClient$Type = ($ModernFixClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixClient_ = $ModernFixClient$Type;
}}
declare module "packages/org/embeddedt/modernfix/world/$StrongholdLocationCache" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DimensionType, $DimensionType$Type} from "packages/net/minecraft/world/level/dimension/$DimensionType"

export class $StrongholdLocationCache extends $SavedData {

constructor()

public static "load"(arg: $CompoundTag$Type): $StrongholdLocationCache
public static "getFileId"(dimensionType: $Holder$Type<($DimensionType$Type)>): string
public "getChunkPosList"(): $List<($ChunkPos)>
public "setChunkPosList"(positions: $List$Type<($ChunkPos$Type)>): void
public "save"(compoundTag: $CompoundTag$Type): $CompoundTag
get "chunkPosList"(): $List<($ChunkPos)>
set "chunkPosList"(value: $List$Type<($ChunkPos$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StrongholdLocationCache$Type = ($StrongholdLocationCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StrongholdLocationCache_ = $StrongholdLocationCache$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$RenderResult" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BlockRendererRegistry$RenderResult extends $Enum<($BlockRendererRegistry$RenderResult)> {
static readonly "OVERRIDE": $BlockRendererRegistry$RenderResult
static readonly "PASS": $BlockRendererRegistry$RenderResult


public static "values"(): ($BlockRendererRegistry$RenderResult)[]
public static "valueOf"(arg0: string): $BlockRendererRegistry$RenderResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRendererRegistry$RenderResult$Type = (("pass") | ("override")) | ($BlockRendererRegistry$RenderResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRendererRegistry$RenderResult_ = $BlockRendererRegistry$RenderResult$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/matrix_stack/$CachingPoseStack" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CachingPoseStack {

 "embeddium$setCachingEnabled"(arg0: boolean): void

(arg0: boolean): void
}

export namespace $CachingPoseStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachingPoseStack$Type = ($CachingPoseStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachingPoseStack_ = $CachingPoseStack$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IBlockState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IBlockState {

 "clearCache"(): void
 "isCacheInvalid"(): boolean
}

export namespace $IBlockState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockState$Type = ($IBlockState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockState_ = $IBlockState$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/config/$ConfigFixer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigFixer {

constructor()

public static "replaceConfigHandlers"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigFixer$Type = ($ConfigFixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigFixer_ = $ConfigFixer$Type;
}}
declare module "packages/org/embeddedt/embeddium/asm/$OptionalInterface" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $OptionalInterface extends $Annotation {

 "value"(): ($Class<(any)>)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $OptionalInterface {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionalInterface$Type = ($OptionalInterface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionalInterface_ = $OptionalInterface$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/config/$NightConfigFixer" {
import {$LinkedHashSet, $LinkedHashSet$Type} from "packages/java/util/$LinkedHashSet"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $NightConfigFixer {
static readonly "configsToReload": $LinkedHashSet<($Runnable)>

constructor()

public static "monitorFileWatcher"(): void
public static "runReloads"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NightConfigFixer$Type = ($NightConfigFixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NightConfigFixer_ = $NightConfigFixer$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar" {
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$EventHandlerRegistrar$Handler, $EventHandlerRegistrar$Handler$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar$Handler"

export class $EventHandlerRegistrar<T extends $EmbeddiumEvent> {

constructor()

public "post"(arg0: T): boolean
public "addListener"(arg0: $EventHandlerRegistrar$Handler$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerRegistrar$Type<T> = ($EventHandlerRegistrar<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerRegistrar_<T> = $EventHandlerRegistrar$Type<(T)>;
}}
declare module "packages/org/embeddedt/embeddium/compat/immersive/$ImmersiveEmptyChunkChecker" {
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"

export class $ImmersiveEmptyChunkChecker {

constructor()

public static "hasWires"(arg0: $SectionPos$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImmersiveEmptyChunkChecker$Type = ($ImmersiveEmptyChunkChecker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImmersiveEmptyChunkChecker_ = $ImmersiveEmptyChunkChecker$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$UVController" {
import {$BlockFaceUV, $BlockFaceUV$Type} from "packages/net/minecraft/client/renderer/block/model/$BlockFaceUV"
import {$ThreadLocal, $ThreadLocal$Type} from "packages/java/lang/$ThreadLocal"

export class $UVController {
static readonly "useDummyUv": $ThreadLocal<(boolean)>
static readonly "dummyUv": $BlockFaceUV

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UVController$Type = ($UVController);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UVController_ = $UVController$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/datagen/$RuntimeDatagen" {
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"

export class $RuntimeDatagen {

constructor()

public static "runRuntimeDatagen"(): void
public static "isDatagenAvailable"(): boolean
public static "onInitTitleScreen"(event: $ScreenEvent$Init$Post$Type): void
get "datagenAvailable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimeDatagen$Type = ($RuntimeDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimeDatagen_ = $RuntimeDatagen$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$OptionGUIConstructionEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$EventHandlerRegistrar, $EventHandlerRegistrar$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar"

export class $OptionGUIConstructionEvent extends $EmbeddiumEvent {
static readonly "BUS": $EventHandlerRegistrar<($OptionGUIConstructionEvent)>

constructor(arg0: $List$Type<($OptionPage$Type)>)
constructor()

public "getPages"(): $List<($OptionPage)>
public "addPage"(arg0: $OptionPage$Type): void
public "getListenerList"(): $ListenerList
get "pages"(): $List<($OptionPage)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionGUIConstructionEvent$Type = ($OptionGUIConstructionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionGUIConstructionEvent_ = $OptionGUIConstructionEvent$Type;
}}
declare module "packages/org/embeddedt/modernfix/world/$ThreadDumper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ThreadDumper {

constructor()

public static "obtainThreadDump"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadDumper$Type = ($ThreadDumper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadDumper_ = $ThreadDumper$Type;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$JEIRuntimeCapturer" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$JeiRuntime, $JeiRuntime$Type} from "packages/mezz/jei/library/runtime/$JeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JEIRuntimeCapturer implements $IModPlugin {

constructor()

public static "runtime"(): $Optional<($JeiRuntime)>
public "onRuntimeUnavailable"(): void
public "getPluginUid"(): $ResourceLocation
public "onRuntimeAvailable"(jeiRuntime: $IJeiRuntime$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIRuntimeCapturer$Type = ($JEIRuntimeCapturer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIRuntimeCapturer_ = $JEIRuntimeCapturer$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$OptionPageFrame" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$OptionPageFrame$Builder, $OptionPageFrame$Builder$Type} from "packages/org/embeddedt/embeddium/gui/frame/$OptionPageFrame$Builder"

export class $OptionPageFrame extends $AbstractFrame {

constructor(arg0: $Dim2i$Type, arg1: boolean, arg2: $OptionPage$Type, arg3: $Predicate$Type<($Option$Type<(any)>)>)

public "buildFrame"(): void
public "setupFrame"(): void
public static "createBuilder"(): $OptionPageFrame$Builder
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setFocused"(arg0: boolean): void
public "isFocused"(): boolean
set "focused"(value: boolean)
get "focused"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionPageFrame$Type = ($OptionPageFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionPageFrame_ = $OptionPageFrame$Type;
}}
declare module "packages/org/embeddedt/modernfix/tickables/$TickableObject" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TickableObject {

 "tick"(): void

(): void
}

export namespace $TickableObject {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickableObject$Type = ($TickableObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickableObject_ = $TickableObject$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/chunk/$ChunkColorWriter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ChunkColorWriter {

 "writeColor"(arg0: integer, arg1: float): integer

(): $ChunkColorWriter
}

export namespace $ChunkColorWriter {
const LEGACY: $ChunkColorWriter
const EMBEDDIUM: $ChunkColorWriter
function get(): $ChunkColorWriter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkColorWriter$Type = ($ChunkColorWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkColorWriter_ = $ChunkColorWriter$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$OptionPageFrame$Builder" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$OptionPageFrame, $OptionPageFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$OptionPageFrame"

export class $OptionPageFrame$Builder {

constructor()

public "build"(): $OptionPageFrame
public "setDimension"(arg0: $Dim2i$Type): $OptionPageFrame$Builder
public "setOptionPage"(arg0: $OptionPage$Type): $OptionPageFrame$Builder
public "setOptionFilter"(arg0: $Predicate$Type<($Option$Type<(any)>)>): $OptionPageFrame$Builder
public "shouldRenderOutline"(arg0: boolean): $OptionPageFrame$Builder
set "dimension"(value: $Dim2i$Type)
set "optionPage"(value: $OptionPage$Type)
set "optionFilter"(value: $Predicate$Type<($Option$Type<(any)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionPageFrame$Builder$Type = ($OptionPageFrame$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionPageFrame$Builder_ = $OptionPageFrame$Builder$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$NamedPreparableResourceListener" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $NamedPreparableResourceListener implements $PreparableReloadListener {

constructor(delegate: $PreparableReloadListener$Type)

public "reload"(stage: $PreparableReloadListener$PreparationBarrier$Type, resourceManager: $ResourceManager$Type, preparationsProfiler: $ProfilerFiller$Type, reloadProfiler: $ProfilerFiller$Type, backgroundExecutor: $Executor$Type, gameExecutor: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NamedPreparableResourceListener$Type = ($NamedPreparableResourceListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NamedPreparableResourceListener_ = $NamedPreparableResourceListener$Type;
}}
declare module "packages/org/embeddedt/embeddium/client/gui/options/$StandardOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StandardOptions {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StandardOptions$Type = ($StandardOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StandardOptions_ = $StandardOptions$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$PackTypeHelper" {
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"

export class $PackTypeHelper {

constructor()

public static "isVanillaPackType"(type: $PackType$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackTypeHelper$Type = ($PackTypeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackTypeHelper_ = $PackTypeHelper$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/rs/$IItemExternalStorageCache" {
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"

export interface $IItemExternalStorageCache {

 "initCache"(arg0: $IItemHandler$Type): boolean

(arg0: $IItemHandler$Type): boolean
}

export namespace $IItemExternalStorageCache {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IItemExternalStorageCache$Type = ($IItemExternalStorageCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IItemExternalStorageCache_ = $IItemExternalStorageCache$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/config/$NightConfigWatchThrottler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NightConfigWatchThrottler {

constructor()

public static "throttle"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NightConfigWatchThrottler$Type = ($NightConfigWatchThrottler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NightConfigWatchThrottler_ = $NightConfigWatchThrottler$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$LayeredForwardingMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $LayeredForwardingMap<K, V> implements $Map<(K), (V)> {

constructor(layers: ($Map$Type<(K), (V)>)[])

public "remove"(key: any): V
public "get"(key: any): V
public "put"(key: K, value: V): V
public "values"(): $Collection<(V)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(m: $Map$Type<(any), (any)>): void
public "containsKey"(key: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(value: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
[index: string | number]: V
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayeredForwardingMap$Type<K, V> = ($LayeredForwardingMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayeredForwardingMap_<K, V> = $LayeredForwardingMap$Type<(K), (V)>;
}}
declare module "packages/org/embeddedt/modernfix/$FileWalker" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$CacheLoader, $CacheLoader$Type} from "packages/com/google/common/cache/$CacheLoader"
import {$List, $List$Type} from "packages/java/util/$List"

export class $FileWalker extends $CacheLoader<($Pair<($Path), (integer)>), ($List<($Path)>)> {
static readonly "INSTANCE": $FileWalker

constructor()

public "load"(key: $Pair$Type<($Path$Type), (integer)>): $List<($Path)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileWalker$Type = ($FileWalker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileWalker_ = $FileWalker$Type;
}}
declare module "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $OptionIdentifier<T> {
static readonly "EMPTY": $OptionIdentifier<(void)>


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "matches"(arg0: $ResourceLocation$Type): boolean
public "matches"(arg0: $OptionIdentifier$Type<(any)>): boolean
public static "isPresent"(arg0: $OptionIdentifier$Type<(any)>): boolean
public "getType"(): $Class<(T)>
public static "create"(arg0: $ResourceLocation$Type): $OptionIdentifier<(void)>
public static "create"<T>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>): $OptionIdentifier<(T)>
public static "create"(arg0: string, arg1: string): $OptionIdentifier<(void)>
public static "create"<T>(arg0: string, arg1: string, arg2: $Class$Type<(T)>): $OptionIdentifier<(T)>
public "getPath"(): string
public "getModId"(): string
get "type"(): $Class<(T)>
get "path"(): string
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionIdentifier$Type<T> = ($OptionIdentifier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionIdentifier_<T> = $OptionIdentifier$Type<(T)>;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$ScrollableFrame$Builder" {
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$ScrollableFrame, $ScrollableFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$ScrollableFrame"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$AtomicReference, $AtomicReference$Type} from "packages/java/util/concurrent/atomic/$AtomicReference"

export class $ScrollableFrame$Builder {

constructor()

public "setHorizontalScrollBarOffset"(arg0: $AtomicReference$Type<(integer)>): $ScrollableFrame$Builder
public "build"(): $ScrollableFrame
public "setFrame"(arg0: $AbstractFrame$Type): $ScrollableFrame$Builder
public "setDimension"(arg0: $Dim2i$Type): $ScrollableFrame$Builder
public "shouldRenderOutline"(arg0: boolean): $ScrollableFrame$Builder
public "setVerticalScrollBarOffset"(arg0: $AtomicReference$Type<(integer)>): $ScrollableFrame$Builder
set "horizontalScrollBarOffset"(value: $AtomicReference$Type<(integer)>)
set "frame"(value: $AbstractFrame$Type)
set "dimension"(value: $Dim2i$Type)
set "verticalScrollBarOffset"(value: $AtomicReference$Type<(integer)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollableFrame$Builder$Type = ($ScrollableFrame$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollableFrame$Builder_ = $ScrollableFrame$Builder$Type;
}}
declare module "packages/org/embeddedt/embeddium/model/$ModelDataSnapshotter" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModelDataSnapshotter {

constructor()

public static "getModelDataForSection"(arg0: $ClientLevel$Type, arg1: $SectionPos$Type): $Map<($BlockPos), ($ModelData)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelDataSnapshotter$Type = ($ModelDataSnapshotter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelDataSnapshotter_ = $ModelDataSnapshotter$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/components/$SearchTextFieldModel" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$EmbeddiumVideoOptionsScreen, $EmbeddiumVideoOptionsScreen$Type} from "packages/org/embeddedt/embeddium/gui/$EmbeddiumVideoOptionsScreen"

export class $SearchTextFieldModel {

constructor(arg0: $Collection$Type<($OptionPage$Type)>, arg1: $EmbeddiumVideoOptionsScreen$Type)

public "write"(arg0: string): void
public "getCursor"(): integer
public "setCursorToEnd"(): void
public "getSelectedText"(): string
public "moveCursor"(arg0: integer): void
public "setSelectionStart"(arg0: integer): void
public "setSelectionEnd"(arg0: integer): void
public "setCursor"(arg0: integer): void
public "setCursorToStart"(): void
public "eraseWords"(arg0: integer): void
public "eraseCharacters"(arg0: integer): void
public "getWordSkipPosition"(arg0: integer): integer
public "getOptionPredicate"(): $Predicate<($Option<(any)>)>
get "cursor"(): integer
get "selectedText"(): string
set "selectionStart"(value: integer)
set "selectionEnd"(value: integer)
set "cursor"(value: integer)
get "optionPredicate"(): $Predicate<($Option<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchTextFieldModel$Type = ($SearchTextFieldModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchTextFieldModel_ = $SearchTextFieldModel$Type;
}}
declare module "packages/org/embeddedt/modernfix/searchtree/$REIBackedSearchTree" {
import {$DummySearchTree, $DummySearchTree$Type} from "packages/org/embeddedt/modernfix/searchtree/$DummySearchTree"
import {$RefreshableSearchTree, $RefreshableSearchTree$Type} from "packages/net/minecraft/client/searchtree/$RefreshableSearchTree"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $REIBackedSearchTree extends $DummySearchTree<($ItemStack)> {

constructor(filteringByTag: boolean)

public static "empty"<T>(): $RefreshableSearchTree<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $REIBackedSearchTree$Type = ($REIBackedSearchTree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $REIBackedSearchTree_ = $REIBackedSearchTree$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$DynamicBakedModelProvider" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$ModelBakery$BakedCacheKey, $ModelBakery$BakedCacheKey$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery$BakedCacheKey"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $DynamicBakedModelProvider implements $Map<($ResourceLocation), ($BakedModel)> {
static "currentInstance": $DynamicBakedModelProvider

constructor(bakery: $ModelBakery$Type, cache: $Map$Type<($ModelBakery$BakedCacheKey$Type), ($BakedModel$Type)>)

public "get"(o: any): $BakedModel
public "put"(resourceLocation: $ResourceLocation$Type, bakedModel: $BakedModel$Type): $BakedModel
public "values"(): $Collection<($BakedModel)>
public "clear"(): void
public "isEmpty"(): boolean
public "replace"(key: $ResourceLocation$Type, value: $BakedModel$Type): $BakedModel
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<($ResourceLocation), ($BakedModel)>)>
public "putAll"(map: $Map$Type<(any), (any)>): void
public "containsKey"(o: any): boolean
public "keySet"(): $Set<($ResourceLocation)>
public "containsValue"(o: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<($ResourceLocation), ($BakedModel)>
public "replace"(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $BakedModel$Type): boolean
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type, arg8: $ResourceLocation$Type, arg9: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type, arg8: $ResourceLocation$Type, arg9: $BakedModel$Type, arg10: $ResourceLocation$Type, arg11: $BakedModel$Type, arg12: $ResourceLocation$Type, arg13: $BakedModel$Type, arg14: $ResourceLocation$Type, arg15: $BakedModel$Type, arg16: $ResourceLocation$Type, arg17: $BakedModel$Type, arg18: $ResourceLocation$Type, arg19: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type, arg8: $ResourceLocation$Type, arg9: $BakedModel$Type, arg10: $ResourceLocation$Type, arg11: $BakedModel$Type, arg12: $ResourceLocation$Type, arg13: $BakedModel$Type, arg14: $ResourceLocation$Type, arg15: $BakedModel$Type, arg16: $ResourceLocation$Type, arg17: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type, arg8: $ResourceLocation$Type, arg9: $BakedModel$Type, arg10: $ResourceLocation$Type, arg11: $BakedModel$Type, arg12: $ResourceLocation$Type, arg13: $BakedModel$Type, arg14: $ResourceLocation$Type, arg15: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type, arg8: $ResourceLocation$Type, arg9: $BakedModel$Type, arg10: $ResourceLocation$Type, arg11: $BakedModel$Type, arg12: $ResourceLocation$Type, arg13: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public static "of"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $ResourceLocation$Type, arg3: $BakedModel$Type, arg4: $ResourceLocation$Type, arg5: $BakedModel$Type, arg6: $ResourceLocation$Type, arg7: $BakedModel$Type, arg8: $ResourceLocation$Type, arg9: $BakedModel$Type, arg10: $ResourceLocation$Type, arg11: $BakedModel$Type): $Map<($ResourceLocation), ($BakedModel)>
public "merge"(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type, arg2: $BiFunction$Type<(any), (any), (any)>): $BakedModel
public "putIfAbsent"(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type): $BakedModel
public "compute"(arg0: $ResourceLocation$Type, arg1: $BiFunction$Type<(any), (any), (any)>): $BakedModel
public static "entry"<K, V>(arg0: $ResourceLocation$Type, arg1: $BakedModel$Type): $Map$Entry<($ResourceLocation), ($BakedModel)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: $ResourceLocation$Type, arg1: $Function$Type<(any), (any)>): $BakedModel
public "getOrDefault"(arg0: any, arg1: $BakedModel$Type): $BakedModel
public "computeIfPresent"(arg0: $ResourceLocation$Type, arg1: $BiFunction$Type<(any), (any), (any)>): $BakedModel
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<($ResourceLocation), ($BakedModel)>
[index: string | number]: $BakedModel
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicBakedModelProvider$Type = ($DynamicBakedModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicBakedModelProvider_ = $DynamicBakedModelProvider$Type;
}}
declare module "packages/org/embeddedt/modernfix/core/$ModernFixMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModernFixEarlyConfig, $ModernFixEarlyConfig$Type} from "packages/org/embeddedt/modernfix/core/config/$ModernFixEarlyConfig"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $ModernFixMixinPlugin implements $IMixinConfigPlugin {
readonly "logger": $Logger
 "config": $ModernFixEarlyConfig
static "instance": $ModernFixMixinPlugin

constructor()

public "isOptionEnabled"(mixin: string): boolean
public "onLoad"(mixinPackage: string): void
public "postApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(targetClassName: string, mixinClassName: string): boolean
public "preApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "acceptTargets"(myTargets: $Set$Type<(string)>, otherTargets: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixMixinPlugin$Type = ($ModernFixMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixMixinPlugin_ = $ModernFixMixinPlugin$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/util/$AsyncLoadingScreen" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"

export class $AsyncLoadingScreen extends $Thread implements $AutoCloseable {
static readonly "MIN_PRIORITY": integer
static readonly "NORM_PRIORITY": integer
static readonly "MAX_PRIORITY": integer

constructor()

public "run"(): void
public "start"(): void
public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsyncLoadingScreen$Type = ($AsyncLoadingScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsyncLoadingScreen_ = $AsyncLoadingScreen$Type;
}}
declare module "packages/org/embeddedt/modernfix/screen/$ModernFixConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ModernFixConfigScreen extends $Screen {
 "madeChanges": boolean
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(lastScreen: $Screen$Type)

public "setLastScrollAmount"(d: double): void
public "onClose"(): void
public "render"(guiGraphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, partialTicks: float): void
set "lastScrollAmount"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModernFixConfigScreen$Type = ($ModernFixConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModernFixConfigScreen_ = $ModernFixConfigScreen$Type;
}}
declare module "packages/org/embeddedt/embeddium/taint/incompats/$IncompatibleModManager" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $IncompatibleModManager {

constructor()

public static "checkMods"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IncompatibleModManager$Type = ($IncompatibleModManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IncompatibleModManager_ = $IncompatibleModManager$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IStructureCheck" {
import {$ChunkGeneratorStructureState, $ChunkGeneratorStructureState$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGeneratorStructureState"

export interface $IStructureCheck {

 "mfix$setStructureState"(arg0: $ChunkGeneratorStructureState$Type): void

(arg0: $ChunkGeneratorStructureState$Type): void
}

export namespace $IStructureCheck {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IStructureCheck$Type = ($IStructureCheck);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IStructureCheck_ = $IStructureCheck$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/components/$ScrollBarComponent" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ScrollBarComponent$Mode, $ScrollBarComponent$Mode$Type} from "packages/org/embeddedt/embeddium/gui/frame/components/$ScrollBarComponent$Mode"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ScrollBarComponent extends $AbstractWidget {

constructor(arg0: $Dim2i$Type, arg1: $ScrollBarComponent$Mode$Type, arg2: integer, arg3: integer, arg4: $Consumer$Type<(integer)>)
constructor(arg0: $Dim2i$Type, arg1: $ScrollBarComponent$Mode$Type, arg2: integer, arg3: integer, arg4: $Consumer$Type<(integer)>, arg5: $Dim2i$Type)

public "getOffset"(): integer
public "setOffset"(arg0: integer): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getRectangle"(): $ScreenRectangle
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "updateThumbPosition"(): void
get "offset"(): integer
set "offset"(value: integer)
get "rectangle"(): $ScreenRectangle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollBarComponent$Type = ($ScrollBarComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollBarComponent_ = $ScrollBarComponent$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/packet/$PacketHandler" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PacketHandler {
static readonly "INSTANCE": $SimpleChannel

constructor()

public static "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketHandler$Type = ($PacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketHandler_ = $PacketHandler$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/world/$WorldSliceLocalGenerator" {
import {$WorldSlice, $WorldSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/$WorldSlice"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $WorldSliceLocalGenerator {

constructor()

public static "testClassGeneration"(): void
public static "generate"(arg0: $WorldSlice$Type): $BlockAndTintGetter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldSliceLocalGenerator$Type = ($WorldSliceLocalGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldSliceLocalGenerator_ = $WorldSliceLocalGenerator$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$ForwardingInclDefaultsMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ForwardingMap, $ForwardingMap$Type} from "packages/com/google/common/collect/$ForwardingMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $ForwardingInclDefaultsMap<K, V> extends $ForwardingMap<(K), (V)> {

constructor()

public "remove"(key: any, value: any): boolean
public "replace"(key: K, value: V): V
public "replace"(key: K, oldValue: V, newValue: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public "merge"(key: K, value: V, remappingFunction: $BiFunction$Type<(any), (any), (any)>): V
public "putIfAbsent"(key: K, value: V): V
public "compute"(key: K, remappingFunction: $BiFunction$Type<(any), (any), (any)>): V
public "forEach"(action: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(key: K, mappingFunction: $Function$Type<(any), (any)>): V
public "getOrDefault"(key: any, defaultValue: V): V
public "computeIfPresent"(key: K, remappingFunction: $BiFunction$Type<(any), (any), (any)>): V
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForwardingInclDefaultsMap$Type<K, V> = ($ForwardingInclDefaultsMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForwardingInclDefaultsMap_<K, V> = $ForwardingInclDefaultsMap$Type<(K), (V)>;
}}
declare module "packages/org/embeddedt/modernfix/annotation/$RequiresMod" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $RequiresMod extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $RequiresMod {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequiresMod$Type = ($RequiresMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequiresMod_ = $RequiresMod$Type;
}}
declare module "packages/org/embeddedt/modernfix/blockstate/$FakeStateMap" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $FakeStateMap<S> implements $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)> {

constructor(numStates: integer)

public "remove"(o: any): S
public "get"(o: any): S
public "put"(propertyComparableMap: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, s: S): S
public "values"(): $Collection<(S)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>)>
public "putAll"(map: $Map$Type<(any), (any)>): void
public "containsKey"(o: any): boolean
public "keySet"(): $Set<($Map<($Property<(any)>), ($Comparable<(any)>)>)>
public "containsValue"(o: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public "replace"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S): S
public "replace"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: S): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S, arg8: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg9: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S, arg8: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg9: S, arg10: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg11: S, arg12: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg13: S, arg14: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg15: S, arg16: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg17: S, arg18: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg19: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S, arg8: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg9: S, arg10: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg11: S, arg12: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg13: S, arg14: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg15: S, arg16: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg17: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S, arg8: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg9: S, arg10: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg11: S, arg12: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg13: S, arg14: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg15: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S, arg8: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg9: S, arg10: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg11: S, arg12: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg13: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public static "of"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg3: S, arg4: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg5: S, arg6: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg7: S, arg8: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg9: S, arg10: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg11: S): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public "merge"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S, arg2: $BiFunction$Type<(any), (any), (any)>): S
public "putIfAbsent"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S): S
public "compute"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: $BiFunction$Type<(any), (any), (any)>): S
public static "entry"<K, V>(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: S): $Map$Entry<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: $Function$Type<(any), (any)>): S
public "getOrDefault"(arg0: any, arg1: S): S
public "computeIfPresent"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>, arg1: $BiFunction$Type<(any), (any), (any)>): S
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (S)>
[index: string | number]: S
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FakeStateMap$Type<S> = ($FakeStateMap<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FakeStateMap_<S> = $FakeStateMap$Type<(S)>;
}}
declare module "packages/org/embeddedt/modernfix/forge/classloading/$ATInjector" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ATInjector {

constructor()

public static "injectModATs"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ATInjector$Type = ($ATInjector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ATInjector_ = $ATInjector$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/$BasicFrame$Builder" {
import {$BasicFrame, $BasicFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$BasicFrame"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"

export class $BasicFrame$Builder {

constructor()

public "build"(): $BasicFrame
public "addChild"(arg0: $Function$Type<($Dim2i$Type), ($AbstractWidget$Type)>): $BasicFrame$Builder
public "setDimension"(arg0: $Dim2i$Type): $BasicFrame$Builder
public "shouldRenderOutline"(arg0: boolean): $BasicFrame$Builder
set "dimension"(value: $Dim2i$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicFrame$Builder$Type = ($BasicFrame$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicFrame$Builder_ = $BasicFrame$Builder$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/components/$SearchTextFieldComponent" {
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$SearchTextFieldModel, $SearchTextFieldModel$Type} from "packages/org/embeddedt/embeddium/gui/frame/components/$SearchTextFieldModel"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SearchTextFieldComponent extends $AbstractWidget {

constructor(arg0: $Dim2i$Type, arg1: $List$Type<($OptionPage$Type)>, arg2: $SearchTextFieldModel$Type)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "isActive"(): boolean
public "getRectangle"(): $ScreenRectangle
public "setFocused"(arg0: boolean): void
public "isVisible"(): boolean
public "isEditable"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getInnerWidth"(): integer
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
get "active"(): boolean
get "rectangle"(): $ScreenRectangle
set "focused"(value: boolean)
get "visible"(): boolean
get "editable"(): boolean
get "innerWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchTextFieldComponent$Type = ($SearchTextFieldComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchTextFieldComponent_ = $SearchTextFieldComponent$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/render/chunk/$RenderSectionDistanceFilter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RenderSectionDistanceFilter {

 "isWithinDistance"(arg0: float, arg1: float, arg2: float, arg3: float): boolean

(arg0: float, arg1: float, arg2: float, arg3: float): boolean
}

export namespace $RenderSectionDistanceFilter {
const DEFAULT: $RenderSectionDistanceFilter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSectionDistanceFilter$Type = ($RenderSectionDistanceFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSectionDistanceFilter_ = $RenderSectionDistanceFilter$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$ITimeTrackingServer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ITimeTrackingServer {

 "mfix$getLastTickStartTime"(): long

(): long
}

export namespace $ITimeTrackingServer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITimeTrackingServer$Type = ($ITimeTrackingServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITimeTrackingServer_ = $ITimeTrackingServer$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/frapi/$FRAPIModelUtils" {
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"

export class $FRAPIModelUtils {

constructor()

public static "isFRAPIModel"(arg0: $BakedModel$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FRAPIModelUtils$Type = ($FRAPIModelUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FRAPIModelUtils_ = $FRAPIModelUtils$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$ItemMesherMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemMesherMap<K> implements $Map<(K), ($ModelResourceLocation)> {

constructor(getLocation: $Function$Type<(K), ($ModelResourceLocation$Type)>)

public "remove"(key: any): $ModelResourceLocation
public "get"(key: any): $ModelResourceLocation
public "put"(key: K, value: $ModelResourceLocation$Type): $ModelResourceLocation
public "values"(): $Collection<($ModelResourceLocation)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), ($ModelResourceLocation)>)>
public "putAll"(m: $Map$Type<(any), (any)>): void
public "containsKey"(key: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(value: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), ($ModelResourceLocation)>
public "replace"(arg0: K, arg1: $ModelResourceLocation$Type): $ModelResourceLocation
public "replace"(arg0: K, arg1: $ModelResourceLocation$Type, arg2: $ModelResourceLocation$Type): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type, arg8: K, arg9: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type, arg8: K, arg9: $ModelResourceLocation$Type, arg10: K, arg11: $ModelResourceLocation$Type, arg12: K, arg13: $ModelResourceLocation$Type, arg14: K, arg15: $ModelResourceLocation$Type, arg16: K, arg17: $ModelResourceLocation$Type, arg18: K, arg19: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type, arg8: K, arg9: $ModelResourceLocation$Type, arg10: K, arg11: $ModelResourceLocation$Type, arg12: K, arg13: $ModelResourceLocation$Type, arg14: K, arg15: $ModelResourceLocation$Type, arg16: K, arg17: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type, arg8: K, arg9: $ModelResourceLocation$Type, arg10: K, arg11: $ModelResourceLocation$Type, arg12: K, arg13: $ModelResourceLocation$Type, arg14: K, arg15: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type, arg8: K, arg9: $ModelResourceLocation$Type, arg10: K, arg11: $ModelResourceLocation$Type, arg12: K, arg13: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public static "of"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type, arg2: K, arg3: $ModelResourceLocation$Type, arg4: K, arg5: $ModelResourceLocation$Type, arg6: K, arg7: $ModelResourceLocation$Type, arg8: K, arg9: $ModelResourceLocation$Type, arg10: K, arg11: $ModelResourceLocation$Type): $Map<(K), ($ModelResourceLocation)>
public "merge"(arg0: K, arg1: $ModelResourceLocation$Type, arg2: $BiFunction$Type<(any), (any), (any)>): $ModelResourceLocation
public "putIfAbsent"(arg0: K, arg1: $ModelResourceLocation$Type): $ModelResourceLocation
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): $ModelResourceLocation
public static "entry"<K, V>(arg0: K, arg1: $ModelResourceLocation$Type): $Map$Entry<(K), ($ModelResourceLocation)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): $ModelResourceLocation
public "getOrDefault"(arg0: any, arg1: $ModelResourceLocation$Type): $ModelResourceLocation
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): $ModelResourceLocation
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), ($ModelResourceLocation)>
[index: string | number]: $ModelResourceLocation
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemMesherMap$Type<K> = ($ItemMesherMap<(K)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemMesherMap_<K> = $ItemMesherMap$Type<(K)>;
}}
declare module "packages/org/embeddedt/modernfix/duck/$IExtendedModelBaker" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IExtendedModelBaker {

 "throwOnMissingModel"(arg0: boolean): boolean

(arg0: boolean): boolean
}

export namespace $IExtendedModelBaker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IExtendedModelBaker$Type = ($IExtendedModelBaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IExtendedModelBaker_ = $IExtendedModelBaker$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/screen/$PromptScreen$Action" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $PromptScreen$Action extends $Record {

constructor(label: $Component$Type, runnable: $Runnable$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "runnable"(): $Runnable
public "label"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PromptScreen$Action$Type = ($PromptScreen$Action);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PromptScreen$Action_ = $PromptScreen$Action$Type;
}}
declare module "packages/org/embeddedt/modernfix/resources/$CachedResourcePath" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Interner, $Interner$Type} from "packages/com/google/common/collect/$Interner"

export class $CachedResourcePath {
static readonly "PATH_COMPONENT_INTERNER": $Interner<(string)>
static readonly "NO_PREFIX": (string)[]

constructor(pathComponents: (string)[])
constructor(prefixElements: (string)[], other: $CachedResourcePath$Type)
constructor<T>(prefixElements: (string)[], path: $Iterable$Type<(T)>, count: integer, intern: boolean)
constructor(prefix: (string)[], path: $Path$Type)
constructor(s: string)
constructor<T>(prefixElements: (string)[], collection: $Collection$Type<(T)>, intern: boolean)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "getFileName"(): string
public "getNameCount"(): integer
public "getFullPath"(startIndex: integer): string
public "getNameAt"(i: integer): string
get "fileName"(): string
get "nameCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedResourcePath$Type = ($CachedResourcePath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedResourcePath_ = $CachedResourcePath$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$ModelBakeryHelpers" {
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$ModernFixClientIntegration, $ModernFixClientIntegration$Type} from "packages/org/embeddedt/modernfix/api/entrypoint/$ModernFixClientIntegration"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ModelBakeryHelpers {
static readonly "MAX_BAKED_MODEL_COUNT": integer
static readonly "MAX_UNBAKED_MODEL_COUNT": integer
static readonly "MAX_MODEL_LIFETIME_SECS": integer

constructor()

public static "bakedModelWrapper"(consumer: $BiFunction$Type<($ResourceLocation$Type), ($Pair$Type<($UnbakedModel$Type), ($BakedModel$Type)>), ($BakedModel$Type)>): $ModernFixClientIntegration
public static "getExtraTextureFolders"(): (string)[]
public static "getBlockStatesForMRL"(stateDefinition: $StateDefinition$Type<($Block$Type), ($BlockState$Type)>, location: $ModelResourceLocation$Type): $ImmutableList<($BlockState)>
get "extraTextureFolders"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelBakeryHelpers$Type = ($ModelBakeryHelpers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelBakeryHelpers_ = $ModelBakeryHelpers$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$DirectExecutorService" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractExecutorService, $AbstractExecutorService$Type} from "packages/java/util/concurrent/$AbstractExecutorService"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $DirectExecutorService extends $AbstractExecutorService {

constructor()

public "shutdown"(): void
public "execute"(command: $Runnable$Type): void
public "isShutdown"(): boolean
public "shutdownNow"(): $List<($Runnable)>
public "isTerminated"(): boolean
public "awaitTermination"(timeout: long, unit: $TimeUnit$Type): boolean
get "terminated"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectExecutorService$Type = ($DirectExecutorService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectExecutorService_ = $DirectExecutorService$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$DynamicInt2ObjectMap" {
import {$Int2ReferenceFunction, $Int2ReferenceFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ReferenceFunction"
import {$Object2DoubleFunction, $Object2DoubleFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2DoubleFunction"
import {$ObjectSet, $ObjectSet$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectSet"
import {$Int2ObjectFunction, $Int2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ObjectFunction"
import {$Double2IntFunction, $Double2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/doubles/$Double2IntFunction"
import {$Float2IntFunction, $Float2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2IntFunction"
import {$Reference2ObjectFunction, $Reference2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2ObjectFunction"
import {$Byte2IntFunction, $Byte2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/bytes/$Byte2IntFunction"
import {$Double2ObjectFunction, $Double2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/doubles/$Double2ObjectFunction"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Reference2IntFunction, $Reference2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2IntFunction"
import {$Long2IntFunction, $Long2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2IntFunction"
import {$Int2CharFunction, $Int2CharFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2CharFunction"
import {$Object2ByteFunction, $Object2ByteFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2ByteFunction"
import {$Int2ShortFunction, $Int2ShortFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ShortFunction"
import {$Short2IntFunction, $Short2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/shorts/$Short2IntFunction"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Object2IntFunction, $Object2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2IntFunction"
import {$ObjectCollection, $ObjectCollection$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectCollection"
import {$Object2LongFunction, $Object2LongFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2LongFunction"
import {$Object2CharFunction, $Object2CharFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2CharFunction"
import {$Object2ObjectFunction, $Object2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2ObjectFunction"
import {$Long2ObjectFunction, $Long2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2ObjectFunction"
import {$Object2ShortFunction, $Object2ShortFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2ShortFunction"
import {$Object2ReferenceFunction, $Object2ReferenceFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2ReferenceFunction"
import {$Short2ObjectFunction, $Short2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/shorts/$Short2ObjectFunction"
import {$Int2ObjectMap, $Int2ObjectMap$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ObjectMap"
import {$Int2LongFunction, $Int2LongFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2LongFunction"
import {$Int2ObjectMap$Entry, $Int2ObjectMap$Entry$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ObjectMap$Entry"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Float2ObjectFunction, $Float2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2ObjectFunction"
import {$Object2FloatFunction, $Object2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2FloatFunction"
import {$Int2IntFunction, $Int2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2IntFunction"
import {$Int2FloatFunction, $Int2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2FloatFunction"
import {$Char2ObjectFunction, $Char2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/chars/$Char2ObjectFunction"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$Int2ByteFunction, $Int2ByteFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ByteFunction"
import {$Int2DoubleFunction, $Int2DoubleFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2DoubleFunction"
import {$DynamicMap, $DynamicMap$Type} from "packages/org/embeddedt/modernfix/util/$DynamicMap"
import {$Char2IntFunction, $Char2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/chars/$Char2IntFunction"
import {$Byte2ObjectFunction, $Byte2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/bytes/$Byte2ObjectFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $DynamicInt2ObjectMap<V> extends $DynamicMap<(integer), (V)> implements $Int2ObjectMap<(V)> {

constructor(arg0: $Function$Type<(integer), (V)>)

public "get"(key: integer): V
public "values"(): $ObjectCollection<(V)>
public "containsKey"(key: integer): boolean
public "getOrDefault"(key: integer, defaultValue: V): V
public "int2ObjectEntrySet"(): $ObjectSet<($Int2ObjectMap$Entry<(V)>)>
public "defaultReturnValue"(rv: V): void
public "defaultReturnValue"(): V
/**
 * 
 * @deprecated
 */
public "remove"(arg0: any): V
public "remove"(arg0: integer, arg1: any): boolean
/**
 * 
 * @deprecated
 */
public "get"(arg0: any): V
public "clear"(): void
public "replace"(arg0: integer, arg1: V, arg2: V): boolean
public "replace"(arg0: integer, arg1: V): V
public "size"(): integer
public "merge"(arg0: integer, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "putIfAbsent"(arg0: integer, arg1: V): V
public "compute"(arg0: integer, arg1: $BiFunction$Type<(any), (any), (any)>): V
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
/**
 * 
 * @deprecated
 */
public "containsKey"(arg0: any): boolean
public "computeIfAbsent"(arg0: integer, arg1: $Int2ObjectFunction$Type<(any)>): V
public "computeIfAbsent"(arg0: integer, arg1: $IntFunction$Type<(any)>): V
/**
 * 
 * @deprecated
 */
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: integer, arg1: $BiFunction$Type<(any), (any), (any)>): V
/**
 * 
 * @deprecated
 */
public "computeIfAbsentPartial"(arg0: integer, arg1: $Int2ObjectFunction$Type<(any)>): V
public "remove"(arg0: integer): V
public "put"(arg0: integer, arg1: V): V
public "apply"(arg0: integer): V
/**
 * 
 * @deprecated
 */
public "compose"<T>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>
public "composeInt"(arg0: $Int2IntFunction$Type): $Int2ObjectFunction<(V)>
public "composeByte"(arg0: $Byte2IntFunction$Type): $Byte2ObjectFunction<(V)>
public "andThenByte"(arg0: $Object2ByteFunction$Type<(V)>): $Int2ByteFunction
public "andThenShort"(arg0: $Object2ShortFunction$Type<(V)>): $Int2ShortFunction
public "composeShort"(arg0: $Short2IntFunction$Type): $Short2ObjectFunction<(V)>
public "andThenInt"(arg0: $Object2IntFunction$Type<(V)>): $Int2IntFunction
public "andThenLong"(arg0: $Object2LongFunction$Type<(V)>): $Int2LongFunction
public "composeLong"(arg0: $Long2IntFunction$Type): $Long2ObjectFunction<(V)>
public "composeObject"<T>(arg0: $Object2IntFunction$Type<(any)>): $Object2ObjectFunction<(T), (V)>
public "andThenChar"(arg0: $Object2CharFunction$Type<(V)>): $Int2CharFunction
public "composeFloat"(arg0: $Float2IntFunction$Type): $Float2ObjectFunction<(V)>
public "andThenFloat"(arg0: $Object2FloatFunction$Type<(V)>): $Int2FloatFunction
public "andThenDouble"(arg0: $Object2DoubleFunction$Type<(V)>): $Int2DoubleFunction
public "composeDouble"(arg0: $Double2IntFunction$Type): $Double2ObjectFunction<(V)>
public "andThenReference"<T>(arg0: $Object2ReferenceFunction$Type<(any), (any)>): $Int2ReferenceFunction<(T)>
public "composeReference"<T>(arg0: $Reference2IntFunction$Type<(any)>): $Reference2ObjectFunction<(T), (V)>
public "andThenObject"<T>(arg0: $Object2ObjectFunction$Type<(any), (any)>): $Int2ObjectFunction<(T)>
public "composeChar"(arg0: $Char2IntFunction$Type): $Char2ObjectFunction<(V)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V, arg8: integer, arg9: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V): $Map<(integer), (V)>
public static "of"<K, V>(): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V, arg8: integer, arg9: V, arg10: integer, arg11: V, arg12: integer, arg13: V, arg14: integer, arg15: V, arg16: integer, arg17: V, arg18: integer, arg19: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V, arg8: integer, arg9: V, arg10: integer, arg11: V, arg12: integer, arg13: V, arg14: integer, arg15: V, arg16: integer, arg17: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V, arg8: integer, arg9: V, arg10: integer, arg11: V, arg12: integer, arg13: V, arg14: integer, arg15: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V, arg8: integer, arg9: V, arg10: integer, arg11: V, arg12: integer, arg13: V): $Map<(integer), (V)>
public static "of"<K, V>(arg0: integer, arg1: V, arg2: integer, arg3: V, arg4: integer, arg5: V, arg6: integer, arg7: V, arg8: integer, arg9: V, arg10: integer, arg11: V): $Map<(integer), (V)>
public static "entry"<K, V>(arg0: integer, arg1: V): $Map$Entry<(integer), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(integer), (V)>
public "apply"(arg0: integer): V
public static "identity"<T>(): $Function<(integer), (integer)>
public "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(integer), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicInt2ObjectMap$Type<V> = ($DynamicInt2ObjectMap<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicInt2ObjectMap_<V> = $DynamicInt2ObjectMap$Type<(V)>;
}}
declare module "packages/org/embeddedt/embeddium/impl/render/chunk/compile/$GlobalChunkBuildContext" {
import {$ChunkBuildContext, $ChunkBuildContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildContext"

export class $GlobalChunkBuildContext {


public static "get"(): $ChunkBuildContext
public static "setMainThread"(): void
public static "bindMainThread"(arg0: $ChunkBuildContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalChunkBuildContext$Type = ($GlobalChunkBuildContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalChunkBuildContext_ = $GlobalChunkBuildContext$Type;
}}
declare module "packages/org/embeddedt/modernfix/forge/load/$ModResourcePackPathFixer" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IModFile, $IModFile$Type} from "packages/net/minecraftforge/forgespi/locating/$IModFile"

export class $ModResourcePackPathFixer {

constructor()

public static "getModFileByRootPath"(path: $Path$Type): $IModFile
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModResourcePackPathFixer$Type = ($ModResourcePackPathFixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModResourcePackPathFixer_ = $ModResourcePackPathFixer$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$RenderPopulator" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockRenderContext, $BlockRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext"
import {$BlockRendererRegistry$Renderer, $BlockRendererRegistry$Renderer$Type} from "packages/org/embeddedt/embeddium/api/$BlockRendererRegistry$Renderer"

export interface $BlockRendererRegistry$RenderPopulator {

 "fillCustomRenderers"(arg0: $List$Type<($BlockRendererRegistry$Renderer$Type)>, arg1: $BlockRenderContext$Type): void

(arg0: $List$Type<($BlockRendererRegistry$Renderer$Type)>, arg1: $BlockRenderContext$Type): void
}

export namespace $BlockRendererRegistry$RenderPopulator {
function forRenderer(arg0: $BlockRendererRegistry$Renderer$Type): $BlockRendererRegistry$RenderPopulator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRendererRegistry$RenderPopulator$Type = ($BlockRendererRegistry$RenderPopulator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRendererRegistry$RenderPopulator_ = $BlockRendererRegistry$RenderPopulator$Type;
}}
declare module "packages/org/embeddedt/modernfix/duck/$ISafeBlockGetter" {
import {$SafeBlockGetter, $SafeBlockGetter$Type} from "packages/org/embeddedt/modernfix/chunk/$SafeBlockGetter"

export interface $ISafeBlockGetter {

 "mfix$getSafeBlockGetter"(): $SafeBlockGetter

(): $SafeBlockGetter
}

export namespace $ISafeBlockGetter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISafeBlockGetter$Type = ($ISafeBlockGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISafeBlockGetter_ = $ISafeBlockGetter$Type;
}}
declare module "packages/org/embeddedt/embeddium/taint/incompats/$ModDeclaration" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ModDeclaration {

 "matches"(): boolean

(): boolean
}

export namespace $ModDeclaration {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModDeclaration$Type = ($ModDeclaration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModDeclaration_ = $ModDeclaration$Type;
}}
declare module "packages/org/embeddedt/embeddium/model/$EpsilonizableBlockElement" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EpsilonizableBlockElement {

 "embeddium$epsilonize"(): void

(): void
}

export namespace $EpsilonizableBlockElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EpsilonizableBlockElement$Type = ($EpsilonizableBlockElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EpsilonizableBlockElement_ = $EpsilonizableBlockElement$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$TimeFormatter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TimeFormatter {

constructor()

public static "formatNanos"(nanos: long): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TimeFormatter$Type = ($TimeFormatter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TimeFormatter_ = $TimeFormatter$Type;
}}
declare module "packages/org/embeddedt/modernfix/render/$FastItemRenderType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FastItemRenderType extends $Enum<($FastItemRenderType)> {
static readonly "SIMPLE_ITEM": $FastItemRenderType
static readonly "SIMPLE_BLOCK": $FastItemRenderType


public static "values"(): ($FastItemRenderType)[]
public static "valueOf"(name: string): $FastItemRenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastItemRenderType$Type = (("simple_block") | ("simple_item")) | ($FastItemRenderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastItemRenderType_ = $FastItemRenderType$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/service/$FlawlessFramesService" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export interface $FlawlessFramesService {

 "acceptController"(arg0: $Function$Type<(string), ($Consumer$Type<(boolean)>)>): void

(arg0: $Function$Type<(string), ($Consumer$Type<(boolean)>)>): void
}

export namespace $FlawlessFramesService {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlawlessFramesService$Type = ($FlawlessFramesService);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlawlessFramesService_ = $FlawlessFramesService$Type;
}}
declare module "packages/org/embeddedt/modernfix/screen/$OptionList" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$AbstractSelectionList$Entry, $AbstractSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList$Entry"
import {$ContainerObjectSelectionList, $ContainerObjectSelectionList$Type} from "packages/net/minecraft/client/gui/components/$ContainerObjectSelectionList"
import {$OptionList$Entry, $OptionList$Entry$Type} from "packages/org/embeddedt/modernfix/screen/$OptionList$Entry"
import {$ModernFixConfigScreen, $ModernFixConfigScreen$Type} from "packages/org/embeddedt/modernfix/screen/$ModernFixConfigScreen"

export class $OptionList extends $ContainerObjectSelectionList<($OptionList$Entry)> {
 "scrolling": boolean
 "hovered": E

constructor(arg: $ModernFixConfigScreen$Type, arg2: $Minecraft$Type)

public "getRowWidth"(): integer
public "updateOptionEntryStatuses"(): void
get "rowWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionList$Type = ($OptionList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionList_ = $OptionList$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$EitherUtil" {
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"

export class $EitherUtil {

constructor()

public static "leftOrNull"<L, R>(either: $Either$Type<(L), (R)>): L
public static "rightOrNull"<L, R>(either: $Either$Type<(L), (R)>): R
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EitherUtil$Type = ($EitherUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EitherUtil_ = $EitherUtil$Type;
}}
declare module "packages/org/embeddedt/modernfix/annotation/$IgnoreOutsideDev" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $IgnoreOutsideDev extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $IgnoreOutsideDev {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IgnoreOutsideDev$Type = ($IgnoreOutsideDev);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IgnoreOutsideDev_ = $IgnoreOutsideDev$Type;
}}
declare module "packages/org/embeddedt/embeddium/api/$OptionGroupConstructionEvent" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$EmbeddiumEvent, $EmbeddiumEvent$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EmbeddiumEvent"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventHandlerRegistrar, $EventHandlerRegistrar$Type} from "packages/org/embeddedt/embeddium/api/eventbus/$EventHandlerRegistrar"

export class $OptionGroupConstructionEvent extends $EmbeddiumEvent {
static readonly "BUS": $EventHandlerRegistrar<($OptionGroupConstructionEvent)>

constructor(arg0: $OptionIdentifier$Type<(void)>, arg1: $List$Type<($Option$Type<(any)>)>)
constructor()

public "getId"(): $OptionIdentifier<(void)>
public "getOptions"(): $List<($Option<(any)>)>
public "getListenerList"(): $ListenerList
get "id"(): $OptionIdentifier<(void)>
get "options"(): $List<($Option<(any)>)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionGroupConstructionEvent$Type = ($OptionGroupConstructionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionGroupConstructionEvent_ = $OptionGroupConstructionEvent$Type;
}}
declare module "packages/org/embeddedt/modernfix/dedup/$ClimateCache" {
import {$Climate$Parameter, $Climate$Parameter$Type} from "packages/net/minecraft/world/level/biome/$Climate$Parameter"
import {$Interner, $Interner$Type} from "packages/com/google/common/collect/$Interner"

export class $ClimateCache {
static readonly "MFIX_INTERNER": $Interner<($Climate$Parameter)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClimateCache$Type = ($ClimateCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClimateCache_ = $ClimateCache$Type;
}}
declare module "packages/org/embeddedt/modernfix/dynamicresources/$ModelLocationCache" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"

export class $ModelLocationCache {

constructor()

public static "get"(state: $BlockState$Type): $ModelResourceLocation
public static "get"(item: $Item$Type): $ModelResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelLocationCache$Type = ($ModelLocationCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelLocationCache_ = $ModelLocationCache$Type;
}}
declare module "packages/org/embeddedt/modernfix/world/$IntegratedWatchdog" {
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $IntegratedWatchdog extends $Thread {
static readonly "MIN_PRIORITY": integer
static readonly "NORM_PRIORITY": integer
static readonly "MAX_PRIORITY": integer

constructor(server: $MinecraftServer$Type)

public "run"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedWatchdog$Type = ($IntegratedWatchdog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedWatchdog_ = $IntegratedWatchdog$Type;
}}
declare module "packages/org/embeddedt/embeddium/gui/frame/tab/$Tab$Builder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Tab, $Tab$Type} from "packages/org/embeddedt/embeddium/gui/frame/tab/$Tab"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AtomicReference, $AtomicReference$Type} from "packages/java/util/concurrent/atomic/$AtomicReference"
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$AbstractFrame, $AbstractFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$AbstractFrame"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ScrollableFrame, $ScrollableFrame$Type} from "packages/org/embeddedt/embeddium/gui/frame/$ScrollableFrame"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $Tab$Builder<T extends $AbstractFrame> {

constructor()

public "from"(arg0: $OptionPage$Type, arg1: $Predicate$Type<($Option$Type<(any)>)>, arg2: $AtomicReference$Type<(integer)>): $Tab<($ScrollableFrame)>
public "build"(): $Tab<(T)>
public "setTitle"(arg0: $Component$Type): $Tab$Builder<(T)>
public "setId"(arg0: $OptionIdentifier$Type<(void)>): $Tab$Builder<(T)>
public "setFrameFunction"(arg0: $Function$Type<($Dim2i$Type), (T)>): $Tab$Builder<(T)>
public "setOnSelectFunction"(arg0: $Supplier$Type<(boolean)>): $Tab$Builder<(T)>
set "title"(value: $Component$Type)
set "id"(value: $OptionIdentifier$Type<(void)>)
set "frameFunction"(value: $Function$Type<($Dim2i$Type), (T)>)
set "onSelectFunction"(value: $Supplier$Type<(boolean)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tab$Builder$Type<T> = ($Tab$Builder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tab$Builder_<T> = $Tab$Builder$Type<(T)>;
}}
declare module "packages/org/embeddedt/embeddium/api/$MeshAppender$Context" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$ChunkBuildBuffers, $ChunkBuildBuffers$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $MeshAppender$Context extends $Record {

constructor(vertexConsumerProvider: $Function$Type<($RenderType$Type), ($VertexConsumer$Type)>, blockRenderView: $BlockAndTintGetter$Type, sectionOrigin: $SectionPos$Type, sodiumBuildBuffers: $ChunkBuildBuffers$Type)

public "sectionOrigin"(): $SectionPos
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "blockRenderView"(): $BlockAndTintGetter
public "sodiumBuildBuffers"(): $ChunkBuildBuffers
public "vertexConsumerProvider"(): $Function<($RenderType), ($VertexConsumer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeshAppender$Context$Type = ($MeshAppender$Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeshAppender$Context_ = $MeshAppender$Context$Type;
}}
declare module "packages/org/embeddedt/embeddium/render/chunk/light/$ForgeLightPipeline" {
import {$LightPipeline, $LightPipeline$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipeline"
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$QuadLightData, $QuadLightData$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$QuadLightData"
import {$QuadLighter, $QuadLighter$Type} from "packages/net/minecraftforge/client/model/lighting/$QuadLighter"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"

export class $ForgeLightPipeline implements $LightPipeline {

constructor(arg0: $LightDataAccess$Type, arg1: $QuadLighter$Type)

public "reset"(): void
public static "smooth"(arg0: $LightDataAccess$Type): $ForgeLightPipeline
public "calculate"(arg0: $ModelQuadView$Type, arg1: $BlockPos$Type, arg2: $QuadLightData$Type, arg3: $Direction$Type, arg4: $Direction$Type, arg5: boolean): void
public static "flat"(arg0: $LightDataAccess$Type): $ForgeLightPipeline
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeLightPipeline$Type = ($ForgeLightPipeline);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeLightPipeline_ = $ForgeLightPipeline$Type;
}}
declare module "packages/org/embeddedt/modernfix/util/$DynamicMap" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DynamicMap<K, V> implements $Map<(K), (V)> {

constructor(arg0: $Function$Type<(K), (V)>)

public "remove"(o: any): V
public "get"(o: any): V
public "put"(k: K, v: V): V
public "values"(): $Collection<(V)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(K), (V)>)>
public "putAll"(map: $Map$Type<(any), (any)>): void
public "containsKey"(o: any): boolean
public "keySet"(): $Set<(K)>
public "containsValue"(o: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public "replace"(arg0: K, arg1: V): V
public "replace"(arg0: K, arg1: V, arg2: V): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public "merge"(arg0: K, arg1: V, arg2: $BiFunction$Type<(any), (any), (any)>): V
public "putIfAbsent"(arg0: K, arg1: V): V
public "compute"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: K, arg1: $Function$Type<(any), (any)>): V
public "getOrDefault"(arg0: any, arg1: V): V
public "computeIfPresent"(arg0: K, arg1: $BiFunction$Type<(any), (any), (any)>): V
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
[index: string | number]: V
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicMap$Type<K, V> = ($DynamicMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicMap_<K, V> = $DynamicMap$Type<(K), (V)>;
}}
