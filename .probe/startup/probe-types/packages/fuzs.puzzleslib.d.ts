declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerXpEvents$PickupXp" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ExperienceOrb, $ExperienceOrb$Type} from "packages/net/minecraft/world/entity/$ExperienceOrb"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $PlayerXpEvents$PickupXp {

 "onPickupXp"(arg0: $Player$Type, arg1: $ExperienceOrb$Type): $EventResult

(arg0: $Player$Type, arg1: $ExperienceOrb$Type): $EventResult
}

export namespace $PlayerXpEvents$PickupXp {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerXpEvents$PickupXp$Type = ($PlayerXpEvents$PickupXp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerXpEvents$PickupXp_ = $PlayerXpEvents$PickupXp$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntityRenderersContext" {
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityRenderersContext {

 "registerEntityRenderer"<T extends $Entity>(arg0: $EntityType$Type<(any)>, arg1: $EntityRendererProvider$Type<(T)>): void

(arg0: $EntityType$Type<(any)>, arg1: $EntityRendererProvider$Type<(T)>): void
}

export namespace $EntityRenderersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRenderersContext$Type = ($EntityRenderersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRenderersContext_ = $EntityRenderersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$RenderTypesContext" {
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $RenderTypesContext<T> {

 "registerRenderType"(arg0: $RenderType$Type, ...arg1: (T)[]): void
 "getRenderType"(arg0: T): $RenderType
}

export namespace $RenderTypesContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypesContext$Type<T> = ($RenderTypesContext<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypesContext_<T> = $RenderTypesContext$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeModConstructor" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ModConstructor, $ModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModConstructor"

export class $ForgeModConstructor {


public static "construct"(arg0: $ModConstructor$Type, arg1: string, arg2: $Set$Type<($ContentRegistrationFlags$Type)>, arg3: $Set$Type<($ContentRegistrationFlags$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModConstructor$Type = ($ForgeModConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModConstructor_ = $ForgeModConstructor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents$LoggedIn" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$MultiPlayerGameMode, $MultiPlayerGameMode$Type} from "packages/net/minecraft/client/multiplayer/$MultiPlayerGameMode"

export interface $ClientPlayerEvents$LoggedIn {

 "onLoggedIn"(arg0: $LocalPlayer$Type, arg1: $MultiPlayerGameMode$Type, arg2: $Connection$Type): void

(arg0: $LocalPlayer$Type, arg1: $MultiPlayerGameMode$Type, arg2: $Connection$Type): void
}

export namespace $ClientPlayerEvents$LoggedIn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvents$LoggedIn$Type = ($ClientPlayerEvents$LoggedIn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvents$LoggedIn_ = $ClientPlayerEvents$LoggedIn$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/serialization/$ConfigDataSet" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ConfigDataSet<T> extends $Collection<(T)> {

/**
 * 
 * @deprecated
 */
 "add"(arg0: T): boolean
/**
 * 
 * @deprecated
 */
 "remove"(arg0: any): boolean
 "get"<V>(arg0: T, arg1: integer): V
 "get"(arg0: T): (any)[]
/**
 * 
 * @deprecated
 */
 "clear"(): void
/**
 * 
 * @deprecated
 */
 "addAll"(arg0: $Collection$Type<(any)>): boolean
 "toMap"(): $Map<(T), ((any)[])>
 "toSet"(): $Set<(T)>
/**
 * 
 * @deprecated
 */
 "removeAll"(arg0: $Collection$Type<(any)>): boolean
/**
 * 
 * @deprecated
 */
 "retainAll"(arg0: $Collection$Type<(any)>): boolean
 "getOptional"<V>(arg0: T, arg1: integer): $Optional<(V)>
 "equals"(arg0: any): boolean
 "hashCode"(): integer
 "isEmpty"(): boolean
 "size"(): integer
 "toArray"<T>(arg0: (T)[]): (T)[]
 "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
 "toArray"(): (any)[]
 "iterator"(): $Iterator<(T)>
 "stream"(): $Stream<(T)>
 "contains"(arg0: any): boolean
 "spliterator"(): $Spliterator<(T)>
 "removeIf"(arg0: $Predicate$Type<(any)>): boolean
 "containsAll"(arg0: $Collection$Type<(any)>): boolean
 "parallelStream"(): $Stream<(T)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $ConfigDataSet {
const CONFIG_DESCRIPTION: string
function toString<T>(arg0: $ResourceKey$Type<(any)>, ...arg1: (T)[]): $List<(string)>
function from<T>(arg0: $ResourceKey$Type<(any)>, arg1: $List$Type<(string)>, ...arg2: ($Class$Type<(any)>)[]): $ConfigDataSet<(T)>
function from<T>(arg0: $ResourceKey$Type<(any)>, arg1: $List$Type<(string)>, arg2: $BiPredicate$Type<(integer), (any)>, ...arg3: ($Class$Type<(any)>)[]): $ConfigDataSet<(T)>
function from<T>(arg0: $ResourceKey$Type<(any)>, ...arg1: (string)[]): $ConfigDataSet<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigDataSet$Type<T> = ($ConfigDataSet<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigDataSet_<T> = $ConfigDataSet$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/shapes/v1/$ShapesHelper" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ShapesHelper {

constructor()

public static "box"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $VoxelShape
public static "rotate"(arg0: $Direction$Type, arg1: $VoxelShape$Type, arg2: $Vector3d$Type): $VoxelShape
public static "rotate"(arg0: $Direction$Type, arg1: $VoxelShape$Type): $VoxelShape
public static "rotate"(arg0: $VoxelShape$Type): $Map<($Direction), ($VoxelShape)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapesHelper$Type = ($ShapesHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapesHelper_ = $ShapesHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$SyncDataPackContentsCallback" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $SyncDataPackContentsCallback {

 "onSyncDataPackContents"(arg0: $ServerPlayer$Type, arg1: boolean): void

(arg0: $ServerPlayer$Type, arg1: boolean): void
}

export namespace $SyncDataPackContentsCallback {
const EVENT: $EventInvoker<($SyncDataPackContentsCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncDataPackContentsCallback$Type = ($SyncDataPackContentsCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncDataPackContentsCallback_ = $SyncDataPackContentsCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $MutableValue<T> extends $Consumer<(T)>, $Supplier<(T)> {

 "map"(arg0: $UnaryOperator$Type<(T)>): void
 "accept"(arg0: T): void
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
 "get"(): T
}

export namespace $MutableValue {
function fromValue<T>(arg0: T): $MutableValue<(T)>
function fromEvent<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>): $MutableValue<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableValue$Type<T> = ($MutableValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableValue_<T> = $MutableValue$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ContainerScreenEvents$Foreground" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $ContainerScreenEvents$Foreground {

 "onDrawForeground"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void

(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
}

export namespace $ContainerScreenEvents$Foreground {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerScreenEvents$Foreground$Type = ($ContainerScreenEvents$Foreground);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerScreenEvents$Foreground_ = $ContainerScreenEvents$Foreground$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelTickEvents$Start" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerLevelTickEvents$Start {

 "onStartLevelTick"(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void

(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void
}

export namespace $ServerLevelTickEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelTickEvents$Start$Type = ($ServerLevelTickEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelTickEvents$Start_ = $ServerLevelTickEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractModelProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ModelFile$ExistingModelFile, $ModelFile$ExistingModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile$ExistingModelFile"
import {$ItemModelBuilder, $ItemModelBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelBuilder"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WallSide, $WallSide$Type} from "packages/net/minecraft/world/level/block/state/properties/$WallSide"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockStateProvider, $BlockStateProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockStateProvider"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"

export class $AbstractModelProvider extends $BlockStateProvider {
static readonly "WALL_PROPS": $ImmutableMap<($Direction), ($Property<($WallSide)>)>

constructor(arg0: $GatherDataEvent$Type, arg1: string)
/**
 * 
 * @deprecated
 */
constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type)
constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type, arg2: string)

public "handheldItem"(arg0: $ResourceLocation$Type): $ItemModelBuilder
public "handheldItem"(arg0: $Item$Type): $ItemModelBuilder
public "name"(arg0: $Block$Type): string
public "key"(arg0: $Block$Type): $ResourceLocation
public "spawnEgg"(arg0: $ResourceLocation$Type): $ItemModelBuilder
public "spawnEgg"(arg0: $Item$Type): $ItemModelBuilder
public "itemName"(arg0: $Item$Type): string
public "basicItem"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "basicItem"(arg0: $ResourceLocation$Type): $ItemModelBuilder
public "basicItem"(arg0: $ResourceLocation$Type, arg1: $Item$Type): $ItemModelBuilder
public "basicItem"(arg0: $Item$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "basicItem"(arg0: $Item$Type): $ItemModelBuilder
public "extend"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
public "extendKey"(arg0: $Block$Type, ...arg1: (string)[]): $ResourceLocation
public "builtInItem"(arg0: $Item$Type, arg1: $Block$Type, arg2: $ResourceLocation$Type): $ItemModelBuilder
public "builtInItem"(arg0: $Item$Type, arg1: $Block$Type): $ItemModelBuilder
public "builtInBlock"(arg0: $Block$Type, arg1: $ResourceLocation$Type): void
public "builtInBlock"(arg0: $Block$Type, arg1: $Block$Type): void
public "existingBlockModel"(arg0: $Block$Type): $ModelFile$ExistingModelFile
public "cubeBottomTopBlock"(arg0: $Block$Type): void
public "cubeBottomTopBlock"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): void
public "simpleExistingBlockWithItem"(arg0: $Block$Type): void
public "simpleExistingBlock"(arg0: $Block$Type): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModelProvider$Type = ($AbstractModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModelProvider_ = $AbstractModelProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStarting" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerLifecycleEvents$ServerStarting {

 "onServerStarting"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerLifecycleEvents$ServerStarting {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLifecycleEvents$ServerStarting$Type = ($ServerLifecycleEvents$ServerStarting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLifecycleEvents$ServerStarting_ = $ServerLifecycleEvents$ServerStarting$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$Load" {
import {$MobSpawnType, $MobSpawnType$Type} from "packages/net/minecraft/world/entity/$MobSpawnType"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

/**
 * 
 * @deprecated
 */
export interface $ServerEntityLevelEvents$Load {

 "onEntityLoad"(arg0: $Entity$Type, arg1: $ServerLevel$Type, arg2: $MobSpawnType$Type): $EventResult

(arg0: $Entity$Type, arg1: $ServerLevel$Type, arg2: $MobSpawnType$Type): $EventResult
}

export namespace $ServerEntityLevelEvents$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityLevelEvents$Load$Type = ($ServerEntityLevelEvents$Load);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityLevelEvents$Load_ = $ServerEntityLevelEvents$Load$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$BeforeKeyAction" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $InputEvents$BeforeKeyAction {

 "onBeforeKeyAction"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $EventResult

(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $EventResult
}

export namespace $InputEvents$BeforeKeyAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$BeforeKeyAction$Type = ($InputEvents$BeforeKeyAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents$BeforeKeyAction_ = $InputEvents$BeforeKeyAction$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents" {
import {$UseItemEvents$Stop, $UseItemEvents$Stop$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Stop"
import {$UseItemEvents$Tick, $UseItemEvents$Tick$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Tick"
import {$UseItemEvents$Finish, $UseItemEvents$Finish$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Finish"
import {$UseItemEvents$Start, $UseItemEvents$Start$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Start"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $UseItemEvents {
static readonly "START": $EventInvoker<($UseItemEvents$Start)>
static readonly "TICK": $EventInvoker<($UseItemEvents$Tick)>
static readonly "STOP": $EventInvoker<($UseItemEvents$Stop)>
static readonly "FINISH": $EventInvoker<($UseItemEvents$Finish)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemEvents$Type = ($UseItemEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemEvents_ = $UseItemEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$ColorProvidersContext" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ColorProvidersContext<T, P> {

 "getProvider"(arg0: T): P
 "registerColorProvider"(arg0: P, ...arg1: (T)[]): void
}

export namespace $ColorProvidersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorProvidersContext$Type<T, P> = ($ColorProvidersContext<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorProvidersContext_<T, P> = $ColorProvidersContext$Type<(T), (P)>;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingContext" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$ConfiguredFeature, $ConfiguredFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$ConfiguredFeature"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$LevelStem, $LevelStem$Type} from "packages/net/minecraft/world/level/dimension/$LevelStem"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export interface $BiomeLoadingContext {

 "getPlacedFeatureKey"(arg0: $PlacedFeature$Type): $Optional<($ResourceKey<($PlacedFeature)>)>
 "is"(arg0: $Holder$Type<($Biome$Type)>): boolean
 "is"(arg0: $Biome$Type): boolean
 "is"(arg0: $TagKey$Type<($Biome$Type)>): boolean
 "is"(arg0: $ResourceKey$Type<($Biome$Type)>): boolean
 "holder"(): $Holder<($Biome)>
 "hasFeature"(arg0: $ResourceKey$Type<($ConfiguredFeature$Type<(any), (any)>)>): boolean
 "getBiome"(): $Biome
 "getResourceKey"(): $ResourceKey<($Biome)>
 "getStructureKey"(arg0: $Structure$Type): $Optional<($ResourceKey<($Structure)>)>
 "hasPlacedFeature"(arg0: $ResourceKey$Type<($PlacedFeature$Type)>): boolean
 "validForStructure"(arg0: $ResourceKey$Type<($Structure$Type)>): boolean
 "getFeatureKey"(arg0: $ConfiguredFeature$Type<(any), (any)>): $Optional<($ResourceKey<($ConfiguredFeature<(any), (any)>)>)>
 "canGenerateIn"(arg0: $ResourceKey$Type<($LevelStem$Type)>): boolean
}

export namespace $BiomeLoadingContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeLoadingContext$Type = ($BiomeLoadingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeLoadingContext_ = $BiomeLoadingContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$CoreShadersContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ResourceProvider, $ResourceProvider$Type} from "packages/net/minecraft/server/packs/resources/$ResourceProvider"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$CoreShadersContext, $CoreShadersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$CoreShadersContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export class $CoreShadersContextForgeImpl extends $Record implements $CoreShadersContext {

constructor(consumer: $BiConsumer$Type<($ShaderInstance$Type), ($Consumer$Type<($ShaderInstance$Type)>)>, resourceManager: $ResourceProvider$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $BiConsumer<($ShaderInstance), ($Consumer<($ShaderInstance)>)>
public "resourceManager"(): $ResourceProvider
public "registerCoreShader"(arg0: $ResourceLocation$Type, arg1: $VertexFormat$Type, arg2: $Consumer$Type<($ShaderInstance$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreShadersContextForgeImpl$Type = ($CoreShadersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreShadersContextForgeImpl_ = $CoreShadersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingExperienceDropCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$DefaultedInt, $DefaultedInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedInt"

export interface $LivingExperienceDropCallback {

 "onLivingExperienceDrop"(arg0: $LivingEntity$Type, arg1: $Player$Type, arg2: $DefaultedInt$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $Player$Type, arg2: $DefaultedInt$Type): $EventResult
}

export namespace $LivingExperienceDropCallback {
const EVENT: $EventInvoker<($LivingExperienceDropCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingExperienceDropCallback$Type = ($LivingExperienceDropCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingExperienceDropCallback_ = $LivingExperienceDropCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents" {
import {$PlayerEvents$BreakSpeed, $PlayerEvents$BreakSpeed$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$BreakSpeed"
import {$PlayerEvents$Copy, $PlayerEvents$Copy$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$Copy"
import {$PlayerEvents$ItemPickup, $PlayerEvents$ItemPickup$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$ItemPickup"
import {$PlayerEvents$Respawn, $PlayerEvents$Respawn$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$Respawn"
import {$PlayerEvents$LoggedIn, $PlayerEvents$LoggedIn$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$LoggedIn"
import {$PlayerEvents$AfterChangeDimension, $PlayerEvents$AfterChangeDimension$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$AfterChangeDimension"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$PlayerEvents$StopTracking, $PlayerEvents$StopTracking$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$StopTracking"
import {$PlayerEvents$StartTracking, $PlayerEvents$StartTracking$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$StartTracking"
import {$PlayerEvents$LoggedOut, $PlayerEvents$LoggedOut$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$LoggedOut"

export class $PlayerEvents {
static readonly "BREAK_SPEED": $EventInvoker<($PlayerEvents$BreakSpeed)>
static readonly "COPY": $EventInvoker<($PlayerEvents$Copy)>
static readonly "RESPAWN": $EventInvoker<($PlayerEvents$Respawn)>
static readonly "START_TRACKING": $EventInvoker<($PlayerEvents$StartTracking)>
static readonly "STOP_TRACKING": $EventInvoker<($PlayerEvents$StopTracking)>
static readonly "LOGGED_IN": $EventInvoker<($PlayerEvents$LoggedIn)>
static readonly "LOGGED_OUT": $EventInvoker<($PlayerEvents$LoggedOut)>
static readonly "AFTER_CHANGE_DIMENSION": $EventInvoker<($PlayerEvents$AfterChangeDimension)>
static readonly "ITEM_PICKUP": $EventInvoker<($PlayerEvents$ItemPickup)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$Type = ($PlayerEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents_ = $PlayerEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$RegisterCommandsCallback" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$Commands$CommandSelection, $Commands$CommandSelection$Type} from "packages/net/minecraft/commands/$Commands$CommandSelection"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export interface $RegisterCommandsCallback {

 "onRegisterCommands"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type, arg2: $Commands$CommandSelection$Type): void

(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type, arg2: $Commands$CommandSelection$Type): void
}

export namespace $RegisterCommandsCallback {
const EVENT: $EventInvoker<($RegisterCommandsCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterCommandsCallback$Type = ($RegisterCommandsCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterCommandsCallback_ = $RegisterCommandsCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$BiomeLoadingHandler$BiomeModification" {
import {$BiomeModificationContext, $BiomeModificationContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeModificationContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BiomeLoadingContext, $BiomeLoadingContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingContext"

export class $BiomeLoadingHandler$BiomeModification extends $Record {

constructor(selector: $Predicate$Type<($BiomeLoadingContext$Type)>, modifier: $Consumer$Type<($BiomeModificationContext$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "selector"(): $Predicate<($BiomeLoadingContext)>
public "tryApply"(arg0: $BiomeLoadingContext$Type, arg1: $BiomeModificationContext$Type): void
public "modifier"(): $Consumer<($BiomeModificationContext)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeLoadingHandler$BiomeModification$Type = ($BiomeLoadingHandler$BiomeModification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeLoadingHandler$BiomeModification_ = $BiomeLoadingHandler$BiomeModification$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v2/$PotionBrewingRegistry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$PotionItem, $PotionItem$Type} from "packages/net/minecraft/world/item/$PotionItem"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export interface $PotionBrewingRegistry {

 "registerContainerRecipe"(arg0: $PotionItem$Type, arg1: $Ingredient$Type, arg2: $PotionItem$Type): void
 "registerContainerRecipe"(arg0: $PotionItem$Type, arg1: $Item$Type, arg2: $PotionItem$Type): void
 "registerPotionContainer"(arg0: $PotionItem$Type): void
 "registerPotionRecipe"(arg0: $Potion$Type, arg1: $Ingredient$Type, arg2: $Potion$Type): void
 "registerPotionRecipe"(arg0: $Potion$Type, arg1: $Item$Type, arg2: $Potion$Type): void
}

export namespace $PotionBrewingRegistry {
const INSTANCE: $PotionBrewingRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionBrewingRegistry$Type = ($PotionBrewingRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionBrewingRegistry_ = $PotionBrewingRegistry$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$ForgeEventInvokerRegistry$ForgeEventContextConsumer" {
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"

export interface $ForgeEventInvokerRegistry$ForgeEventContextConsumer<T, E extends $Event> {

 "accept"(arg0: T, arg1: E, arg2: any): void

(arg0: T, arg1: E, arg2: any): void
}

export namespace $ForgeEventInvokerRegistry$ForgeEventContextConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type<T, E> = ($ForgeEventInvokerRegistry$ForgeEventContextConsumer<(T), (E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventInvokerRegistry$ForgeEventContextConsumer_<T, E> = $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type<(T), (E)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Expire" {
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $MobEffectEvents$Expire {

 "onMobEffectExpire"(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type): void

(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type): void
}

export namespace $MobEffectEvents$Expire {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobEffectEvents$Expire$Type = ($MobEffectEvents$Expire);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobEffectEvents$Expire_ = $MobEffectEvents$Expire$Type;
}}
declare module "packages/fuzs/puzzleslib/api/item/v2/$CreativeModeTabConfigurator" {
import {$CreativeModeTab$DisplayItemsGenerator, $CreativeModeTab$DisplayItemsGenerator$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$DisplayItemsGenerator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $CreativeModeTabConfigurator {

 "displayItems"(arg0: $CreativeModeTab$DisplayItemsGenerator$Type): $CreativeModeTabConfigurator
 "withSearchBar"(): $CreativeModeTabConfigurator
 "icon"(arg0: $Supplier$Type<($ItemStack$Type)>): $CreativeModeTabConfigurator
 "icons"(arg0: $Supplier$Type<(($ItemStack$Type)[])>): $CreativeModeTabConfigurator
 "appendEnchantmentsAndPotions"(): $CreativeModeTabConfigurator
}

export namespace $CreativeModeTabConfigurator {
function from(arg0: $ResourceLocation$Type): $CreativeModeTabConfigurator
function from(arg0: string, arg1: string): $CreativeModeTabConfigurator
function from(arg0: string): $CreativeModeTabConfigurator
function from(arg0: string, arg1: $Supplier$Type<($ItemStack$Type)>): $CreativeModeTabConfigurator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeModeTabConfigurator$Type = ($CreativeModeTabConfigurator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeModeTabConfigurator_ = $CreativeModeTabConfigurator$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$ForgeRegistryReference" {
import {$RegistryReference, $RegistryReference$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryReference"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ForgeRegistryReference<T> implements $RegistryReference<(T)> {

constructor(arg0: $RegistryObject$Type<(T)>, arg1: $ResourceKey$Type<(any)>)

public "get"(): T
public "isPresent"(): boolean
public "holder"(): $Holder<(T)>
public "getResourceKey"(): $ResourceKey<(T)>
public "getResourceLocation"(): $ResourceLocation
public "getRegistryKey"(): $ResourceKey<(any)>
public "isEmpty"(): boolean
public static "placeholder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: string): $RegistryReference<(T)>
public static "placeholder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: $ResourceLocation$Type): $RegistryReference<(T)>
get "present"(): boolean
get "resourceKey"(): $ResourceKey<(T)>
get "resourceLocation"(): $ResourceLocation
get "registryKey"(): $ResourceKey<(any)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeRegistryReference$Type<T> = ($ForgeRegistryReference<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeRegistryReference_<T> = $ForgeRegistryReference$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Jump" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$DefaultedDouble, $DefaultedDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedDouble"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingEvents$Jump {

 "onLivingJump"(arg0: $LivingEntity$Type, arg1: $DefaultedDouble$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $DefaultedDouble$Type): $EventResult
}

export namespace $LivingEvents$Jump {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEvents$Jump$Type = ($LivingEvents$Jump);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEvents$Jump_ = $LivingEvents$Jump$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/resources/$ForwardingResourceManagerReloadListener" {
import {$ForwardingReloadListener, $ForwardingReloadListener$Type} from "packages/fuzs/puzzleslib/impl/core/resources/$ForwardingReloadListener"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $ForwardingResourceManagerReloadListener extends $ForwardingReloadListener<($ResourceManagerReloadListener)> implements $ResourceManagerReloadListener {

constructor(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($Collection$Type<($ResourceManagerReloadListener$Type)>)>)

public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForwardingResourceManagerReloadListener$Type = ($ForwardingResourceManagerReloadListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForwardingResourceManagerReloadListener_ = $ForwardingResourceManagerReloadListener$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$AdditionalBakedModel" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ModelEvents$AdditionalBakedModel {

 "onAdditionalBakedModel"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg1: $Function$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg2: $Supplier$Type<($ModelBaker$Type)>): void

(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg1: $Function$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg2: $Supplier$Type<($ModelBaker$Type)>): void
}

export namespace $ModelEvents$AdditionalBakedModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$AdditionalBakedModel$Type = ($ModelEvents$AdditionalBakedModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents$AdditionalBakedModel_ = $ModelEvents$AdditionalBakedModel$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLivingEvents" {
import {$RenderLivingEvents$After, $RenderLivingEvents$After$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLivingEvents$After"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$RenderLivingEvents$Before, $RenderLivingEvents$Before$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLivingEvents$Before"

export class $RenderLivingEvents {
static readonly "BEFORE": $EventInvoker<($RenderLivingEvents$Before)>
static readonly "AFTER": $EventInvoker<($RenderLivingEvents$After)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLivingEvents$Type = ($RenderLivingEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLivingEvents_ = $RenderLivingEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStarted" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerLifecycleEvents$ServerStarted {

 "onServerStarted"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerLifecycleEvents$ServerStarted {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLifecycleEvents$ServerStarted$Type = ($ServerLifecycleEvents$ServerStarted);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLifecycleEvents$ServerStarted_ = $ServerLifecycleEvents$ServerStarted$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/resources/$ForwardingReloadListener" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$NamedReloadListener, $NamedReloadListener$Type} from "packages/fuzs/puzzleslib/api/core/v1/resources/$NamedReloadListener"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $ForwardingReloadListener<T extends $PreparableReloadListener> implements $NamedReloadListener {

constructor(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($Collection$Type<(T)>)>)

public "toString"(): string
public "identifier"(): $ResourceLocation
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForwardingReloadListener$Type<T> = ($ForwardingReloadListener<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForwardingReloadListener_<T> = $ForwardingReloadListener$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicModifyBakingResultContext" {
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * 
 * @deprecated
 */
export interface $DynamicModifyBakingResultContext {

 "models"(): $Map<($ResourceLocation), ($BakedModel)>
 "modelBakery"(): $ModelBakery
}

export namespace $DynamicModifyBakingResultContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicModifyBakingResultContext$Type = ($DynamicModifyBakingResultContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicModifyBakingResultContext_ = $DynamicModifyBakingResultContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEquipmentChangeCallback" {
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingEquipmentChangeCallback {

 "onLivingEquipmentChange"(arg0: $LivingEntity$Type, arg1: $EquipmentSlot$Type, arg2: $ItemStack$Type, arg3: $ItemStack$Type): void

(arg0: $LivingEntity$Type, arg1: $EquipmentSlot$Type, arg2: $ItemStack$Type, arg3: $ItemStack$Type): void
}

export namespace $LivingEquipmentChangeCallback {
const EVENT: $EventInvoker<($LivingEquipmentChangeCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEquipmentChangeCallback$Type = ($LivingEquipmentChangeCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEquipmentChangeCallback_ = $LivingEquipmentChangeCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractBuiltInDataProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $AbstractBuiltInDataProvider<T> implements $DataProvider {

constructor(arg0: $ResourceKey$Type<(any)>, arg1: $GatherDataEvent$Type, arg2: string)
constructor(arg0: $ResourceKey$Type<(any)>, arg1: $PackOutput$Type, arg2: string, arg3: $CompletableFuture$Type<($HolderLookup$Provider$Type)>)

public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBuiltInDataProvider$Type<T> = ($AbstractBuiltInDataProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBuiltInDataProvider_<T> = $AbstractBuiltInDataProvider$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/data/$ForgeCapabilityKey" {
import {$ForgeCapabilityKey$CapabilityTokenFactory, $ForgeCapabilityKey$CapabilityTokenFactory$Type} from "packages/fuzs/puzzleslib/impl/capability/data/$ForgeCapabilityKey$CapabilityTokenFactory"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$CapabilityToken, $CapabilityToken$Type} from "packages/net/minecraftforge/common/capabilities/$CapabilityToken"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$CapabilityKey, $CapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeCapabilityKey<C extends $CapabilityComponent> implements $CapabilityKey<(C)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Class$Type<(C)>, arg2: $ForgeCapabilityKey$CapabilityTokenFactory$Type<(C)>)

public "get"<V>(arg0: V): C
public "getId"(): $ResourceLocation
public "getComponentClass"(): $Class<(C)>
public "createCapability"(arg0: $CapabilityToken$Type<(C)>): void
public "maybeGet"<V>(arg0: V): $Optional<(C)>
public "isProvidedBy"<V>(arg0: V): boolean
public "orThrow"<V>(arg0: V): C
get "id"(): $ResourceLocation
get "componentClass"(): $Class<(C)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCapabilityKey$Type<C> = ($ForgeCapabilityKey<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCapabilityKey_<C> = $ForgeCapabilityKey$Type<(C)>;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$BlockAccessor" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"

export interface $BlockAccessor {

 "puzzleslib$setItem"(arg0: $Item$Type): void

(arg0: $Item$Type): void
}

export namespace $BlockAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockAccessor$Type = ($BlockAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockAccessor_ = $BlockAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v2/$GameRulesFactory" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$GameRules$Value, $GameRules$Value$Type} from "packages/net/minecraft/world/level/$GameRules$Value"
import {$GameRules$Type, $GameRules$Type$Type} from "packages/net/minecraft/world/level/$GameRules$Type"
import {$GameRules$IntegerValue, $GameRules$IntegerValue$Type} from "packages/net/minecraft/world/level/$GameRules$IntegerValue"
import {$GameRules$Key, $GameRules$Key$Type} from "packages/net/minecraft/world/level/$GameRules$Key"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$GameRules$BooleanValue, $GameRules$BooleanValue$Type} from "packages/net/minecraft/world/level/$GameRules$BooleanValue"
import {$GameRules$Category, $GameRules$Category$Type} from "packages/net/minecraft/world/level/$GameRules$Category"

export interface $GameRulesFactory {

 "register"<T extends $GameRules$Value<(T)>>(arg0: string, arg1: $GameRules$Category$Type, arg2: $GameRules$Type$Type<(T)>): $GameRules$Key<(T)>
 "createIntRule"(arg0: integer, arg1: integer): $GameRules$Type<($GameRules$IntegerValue)>
 "createIntRule"(arg0: integer, arg1: integer, arg2: integer): $GameRules$Type<($GameRules$IntegerValue)>
 "createIntRule"(arg0: integer, arg1: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$IntegerValue$Type)>): $GameRules$Type<($GameRules$IntegerValue)>
 "createIntRule"(arg0: integer, arg1: integer, arg2: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$IntegerValue$Type)>): $GameRules$Type<($GameRules$IntegerValue)>
 "createIntRule"(arg0: integer, arg1: integer, arg2: integer, arg3: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$IntegerValue$Type)>): $GameRules$Type<($GameRules$IntegerValue)>
 "createIntRule"(arg0: integer): $GameRules$Type<($GameRules$IntegerValue)>
 "createBooleanRule"(arg0: boolean): $GameRules$Type<($GameRules$BooleanValue)>
 "createBooleanRule"(arg0: boolean, arg1: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$BooleanValue$Type)>): $GameRules$Type<($GameRules$BooleanValue)>
 "registerIntRule"(arg0: string, arg1: $GameRules$Category$Type, arg2: integer): $GameRules$Key<($GameRules$IntegerValue)>
 "registerBooleanRule"(arg0: string, arg1: $GameRules$Category$Type, arg2: boolean): $GameRules$Key<($GameRules$BooleanValue)>
}

export namespace $GameRulesFactory {
const INSTANCE: $GameRulesFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameRulesFactory$Type = ($GameRulesFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameRulesFactory_ = $GameRulesFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/$AbstractBuiltInDataProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$DataProviderContext, $DataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $AbstractBuiltInDataProvider<T> implements $DataProvider {

constructor(arg0: $ResourceKey$Type<(any)>, arg1: $DataProviderContext$Type)
constructor(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: $PackOutput$Type, arg3: $CompletableFuture$Type<($HolderLookup$Provider$Type)>)

public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBuiltInDataProvider$Type<T> = ($AbstractBuiltInDataProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBuiltInDataProvider_<T> = $AbstractBuiltInDataProvider$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker" {
import {$EventPhase, $EventPhase$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventPhase"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $EventInvoker<T> {

 "register"(arg0: $EventPhase$Type, arg1: T): void
 "register"(arg0: T): void

(arg0: $Class$Type<(T)>): $EventInvoker<(T)>
}

export namespace $EventInvoker {
function lookup<T>(arg0: $Class$Type<(T)>): $EventInvoker<(T)>
function lookup<T>(arg0: $Class$Type<(T)>, arg1: any): $EventInvoker<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventInvoker$Type<T> = ($EventInvoker<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventInvoker_<T> = $EventInvoker$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ServiceProviderHelper" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ServiceProviderHelper {

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServiceProviderHelper$Type = ($ServiceProviderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServiceProviderHelper_ = $ServiceProviderHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$EntityRenderersContextForgeImpl" {
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EntityRenderersContext, $EntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntityRenderersContext"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityRenderersContextForgeImpl extends $Record implements $EntityRenderersContext {

constructor(context: $EntityRenderersContext$Type)

public "context"(): $EntityRenderersContext
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerEntityRenderer"<T extends $Entity>(arg0: $EntityType$Type<(any)>, arg1: $EntityRendererProvider$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRenderersContextForgeImpl$Type = ($EntityRenderersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRenderersContextForgeImpl_ = $EntityRenderersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$CopyTagShapelessRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$CopyTagRecipe, $CopyTagRecipe$Type} from "packages/fuzs/puzzleslib/impl/item/$CopyTagRecipe"
import {$ShapelessRecipe, $ShapelessRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapelessRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export class $CopyTagShapelessRecipe extends $ShapelessRecipe implements $CopyTagRecipe {
readonly "group": string
readonly "result": $ItemStack
readonly "ingredients": $NonNullList<($Ingredient)>

constructor(arg0: $ShapelessRecipe$Type, arg1: $Ingredient$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getCopyTagSource"(): $Ingredient
public static "registerSerializers"(arg0: $BiConsumer$Type<(string), ($Supplier$Type<($RecipeSerializer$Type<(any)>)>)>): void
public static "getModSerializer"(arg0: string, arg1: string): $RecipeSerializer<(any)>
public "tryCopyTagToResult"(arg0: $ItemStack$Type, arg1: $CraftingContainer$Type): void
get "serializer"(): $RecipeSerializer<(any)>
get "copyTagSource"(): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CopyTagShapelessRecipe$Type = ($CopyTagShapelessRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CopyTagShapelessRecipe_ = $CopyTagShapelessRecipe$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableInt" {
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export class $ValueMutableInt implements $MutableInt {

constructor(arg0: integer)

public "accept"(arg0: integer): void
public "getAsInt"(): integer
public "mapInt"(arg0: $IntUnaryOperator$Type): void
public static "fromValue"(arg0: integer): $MutableInt
public static "fromEvent"(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type): $MutableInt
public "andThen"(arg0: $IntConsumer$Type): $IntConsumer
get "asInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueMutableInt$Type = ($ValueMutableInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueMutableInt_ = $ValueMutableInt$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$Spawn" {
import {$MobSpawnType, $MobSpawnType$Type} from "packages/net/minecraft/world/entity/$MobSpawnType"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ServerEntityLevelEvents$Spawn {

 "onEntitySpawn"(arg0: $Entity$Type, arg1: $ServerLevel$Type, arg2: $MobSpawnType$Type): $EventResult

(arg0: $Entity$Type, arg1: $ServerLevel$Type, arg2: $MobSpawnType$Type): $EventResult
}

export namespace $ServerEntityLevelEvents$Spawn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityLevelEvents$Spawn$Type = ($ServerEntityLevelEvents$Spawn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityLevelEvents$Spawn_ = $ServerEntityLevelEvents$Spawn$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$EntityAttributesCreateContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$AttributeSupplier, $AttributeSupplier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$EntityAttributesCreateContext, $EntityAttributesCreateContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesCreateContext"

export class $EntityAttributesCreateContextForgeImpl extends $Record implements $EntityAttributesCreateContext {

constructor(consumer: $BiConsumer$Type<($EntityType$Type<(any)>), ($AttributeSupplier$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $BiConsumer<($EntityType<(any)>), ($AttributeSupplier)>
public "registerEntityAttributes"(arg0: $EntityType$Type<(any)>, arg1: $AttributeSupplier$Builder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributesCreateContextForgeImpl$Type = ($EntityAttributesCreateContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributesCreateContextForgeImpl_ = $EntityAttributesCreateContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Load" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerChunkEvents$Load {

 "onChunkLoad"(arg0: $ServerLevel$Type, arg1: $LevelChunk$Type): void

(arg0: $ServerLevel$Type, arg1: $LevelChunk$Type): void
}

export namespace $ServerChunkEvents$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerChunkEvents$Load$Type = ($ServerChunkEvents$Load);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerChunkEvents$Load_ = $ServerChunkEvents$Load$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$ArrowLooseCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"

export interface $ArrowLooseCallback {

 "onArrowLoose"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $MutableInt$Type, arg4: boolean): $EventResult

(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $MutableInt$Type, arg4: boolean): $EventResult
}

export namespace $ArrowLooseCallback {
const EVENT: $EventInvoker<($ArrowLooseCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrowLooseCallback$Type = ($ArrowLooseCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrowLooseCallback_ = $ArrowLooseCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/client/$AbstractParticleDescriptionProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ForgeDataProviderContext, $ForgeDataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$JsonCodecProvider, $JsonCodecProvider$Type} from "packages/net/minecraftforge/common/data/$JsonCodecProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AbstractParticleDescriptionProvider extends $JsonCodecProvider<($List<($ResourceLocation)>)> {

constructor(arg0: string, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type)
constructor(arg0: $ForgeDataProviderContext$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractParticleDescriptionProvider$Type = ($AbstractParticleDescriptionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractParticleDescriptionProvider_ = $AbstractParticleDescriptionProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$BabyEntitySpawnCallback" {
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$AgeableMob, $AgeableMob$Type} from "packages/net/minecraft/world/entity/$AgeableMob"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $BabyEntitySpawnCallback {

 "onBabyEntitySpawn"(arg0: $Mob$Type, arg1: $Mob$Type, arg2: $MutableValue$Type<($AgeableMob$Type)>): $EventResult

(arg0: $Mob$Type, arg1: $Mob$Type, arg2: $MutableValue$Type<($AgeableMob$Type)>): $EventResult
}

export namespace $BabyEntitySpawnCallback {
const EVENT: $EventInvoker<($BabyEntitySpawnCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BabyEntitySpawnCallback$Type = ($BabyEntitySpawnCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BabyEntitySpawnCallback_ = $BabyEntitySpawnCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$ClientTooltipComponentsContext" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ClientTooltipComponentsContext {

 "registerClientTooltipComponent"<T extends $TooltipComponent>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(any), (any)>): void

(arg0: $Class$Type<(T)>, arg1: $Function$Type<(any), (any)>): void
}

export namespace $ClientTooltipComponentsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipComponentsContext$Type = ($ClientTooltipComponentsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipComponentsContext_ = $ClientTooltipComponentsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$ItemModelDisplayOverrides" {
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"

export interface $ItemModelDisplayOverrides {

 "register"(arg0: $ModelResourceLocation$Type, arg1: $ModelResourceLocation$Type, ...arg2: ($ItemDisplayContext$Type)[]): void

(arg0: $ModelResourceLocation$Type, arg1: $ModelResourceLocation$Type, ...arg2: ($ItemDisplayContext$Type)[]): void
}

export namespace $ItemModelDisplayOverrides {
const INSTANCE: $ItemModelDisplayOverrides
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelDisplayOverrides$Type = ($ItemModelDisplayOverrides);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelDisplayOverrides_ = $ItemModelDisplayOverrides$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/event/$ForgeModelBakerImpl" {
import {$ForgeModelBakerImpl$BakedCacheKey, $ForgeModelBakerImpl$BakedCacheKey$Type} from "packages/fuzs/puzzleslib/impl/client/event/$ForgeModelBakerImpl$BakedCacheKey"
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$AtlasSet$StitchResult, $AtlasSet$StitchResult$Type} from "packages/net/minecraft/client/resources/model/$AtlasSet$StitchResult"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ForgeModelBakerImpl extends $Record implements $ModelBaker {

constructor(arg0: $ResourceLocation$Type, arg1: $Map$Type<($ForgeModelBakerImpl$BakedCacheKey$Type), ($BakedModel$Type)>, arg2: $Function$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($Material$Type)>, arg4: $BakedModel$Type)
constructor(bakedCache: $Map$Type<($ForgeModelBakerImpl$BakedCacheKey$Type), ($BakedModel$Type)>, unbakedModelGetter: $Function$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>, modelTextureGetter: $Function$Type<($Material$Type), ($TextureAtlasSprite$Type)>, missingModel: $BakedModel$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getModel"(arg0: $ResourceLocation$Type): $UnbakedModel
public "getModelTextureGetter"(): $Function<($Material), ($TextureAtlasSprite)>
public "unbakedModelGetter"(): $Function<($ResourceLocation), ($UnbakedModel)>
public "modelTextureGetter"(): $Function<($Material), ($TextureAtlasSprite)>
public static "setAtlasPreparations"(arg0: $Map$Type<($ResourceLocation$Type), ($AtlasSet$StitchResult$Type)>): void
public "missingModel"(): $BakedModel
public "bakedCache"(): $Map<($ForgeModelBakerImpl$BakedCacheKey), ($BakedModel)>
public "bake"(arg0: $ResourceLocation$Type, arg1: $ModelState$Type): $BakedModel
public "bake"(arg0: $UnbakedModel$Type, arg1: $ResourceLocation$Type): $BakedModel
public "bake"(arg0: $ResourceLocation$Type, arg1: $ModelState$Type, arg2: $Function$Type<($Material$Type), ($TextureAtlasSprite$Type)>): $BakedModel
set "atlasPreparations"(value: $Map$Type<($ResourceLocation$Type), ($AtlasSet$StitchResult$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModelBakerImpl$Type = ($ForgeModelBakerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModelBakerImpl_ = $ForgeModelBakerImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v2/$MessageDirection" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * 
 * @deprecated
 */
export class $MessageDirection extends $Enum<($MessageDirection)> {
static readonly "TO_CLIENT": $MessageDirection
static readonly "TO_SERVER": $MessageDirection


public static "values"(): ($MessageDirection)[]
public static "valueOf"(arg0: string): $MessageDirection
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageDirection$Type = (("to_server") | ("to_client")) | ($MessageDirection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageDirection_ = $MessageDirection$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ModContainer" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ModContainer {

 "findResource"(...arg0: (string)[]): $Optional<($Path)>
 "getParent"(): $ModContainer
 "getDisplayName"(): string
 "getVersion"(): string
 "getChildren"(): $Collection<($ModContainer)>
 "getDescription"(): string
 "getAuthors"(): $Collection<(string)>
 "getCredits"(): $Collection<(string)>
 "getContactTypes"(): $Map<(string), (string)>
 "getLicenses"(): $Collection<(string)>
 "getAllChildren"(): $Stream<($ModContainer)>
 "getModId"(): string
}

export namespace $ModContainer {
function toModList(arg0: $Supplier$Type<($Stream$Type<(any)>)>): $Map<(string), ($ModContainer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModContainer$Type = ($ModContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModContainer_ = $ModContainer$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$AdditionalModelsContext" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $AdditionalModelsContext {

 "registerAdditionalModel"(...arg0: ($ResourceLocation$Type)[]): void

(...arg0: ($ResourceLocation$Type)[]): void
}

export namespace $AdditionalModelsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionalModelsContext$Type = ($AdditionalModelsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionalModelsContext_ = $AdditionalModelsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingAttackCallback" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingAttackCallback {

 "onLivingAttack"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float): $EventResult
}

export namespace $LivingAttackCallback {
const EVENT: $EventInvoker<($LivingAttackCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingAttackCallback$Type = ($LivingAttackCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingAttackCallback_ = $LivingAttackCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/$PuzzlesLibForge" {
import {$FMLConstructModEvent, $FMLConstructModEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLConstructModEvent"

export class $PuzzlesLibForge {

constructor()

public static "onConstructMod"(arg0: $FMLConstructModEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PuzzlesLibForge$Type = ($PuzzlesLibForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PuzzlesLibForge_ = $PuzzlesLibForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerCapabilityKey" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$CapabilityKey, $CapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SyncStrategy, $SyncStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncStrategy"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerCapabilityKey<C extends $CapabilityComponent> extends $CapabilityKey<(C)> {

 "syncToRemote"(arg0: $ServerPlayer$Type): void
 "get"<V>(arg0: V): C
 "getId"(): $ResourceLocation
 "getComponentClass"(): $Class<(C)>
 "isProvidedBy"<V>(arg0: V): boolean
 "maybeGet"<V>(arg0: V): $Optional<(C)>
 "orThrow"<V>(arg0: V): C
}

export namespace $PlayerCapabilityKey {
function syncCapabilityToRemote<C>(arg0: $Entity$Type, arg1: $ServerPlayer$Type, arg2: $SyncStrategy$Type, arg3: C, arg4: $ResourceLocation$Type, arg5: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerCapabilityKey$Type<C> = ($PlayerCapabilityKey<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerCapabilityKey_<C> = $PlayerCapabilityKey$Type<(C)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$ModifyUnbakedModel" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ModelEvents$ModifyUnbakedModel {

 "onModifyUnbakedModel"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($UnbakedModel$Type)>, arg2: $Function$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>): $EventResultHolder<($UnbakedModel)>

(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($UnbakedModel$Type)>, arg2: $Function$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>): $EventResultHolder<($UnbakedModel)>
}

export namespace $ModelEvents$ModifyUnbakedModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$ModifyUnbakedModel$Type = ($ModelEvents$ModifyUnbakedModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents$ModifyUnbakedModel_ = $ModelEvents$ModifyUnbakedModel$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerImplHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NetworkHandlerImplHelper {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerImplHelper$Type = ($NetworkHandlerImplHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerImplHelper_ = $NetworkHandlerImplHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/particle/$ClientParticleTypesManager" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$ParticleProvider, $ParticleProvider$Type} from "packages/net/minecraft/client/particle/$ParticleProvider"
import {$Particle, $Particle$Type} from "packages/net/minecraft/client/particle/$Particle"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ParticleEngine$SpriteParticleRegistration, $ParticleEngine$SpriteParticleRegistration$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$SpriteParticleRegistration"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ParticleProvider$Sprite, $ParticleProvider$Sprite$Type} from "packages/net/minecraft/client/particle/$ParticleProvider$Sprite"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $ClientParticleTypesManager implements $PreparableReloadListener {

constructor()

public "register"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleProvider$Type<(T)>): void
public "register"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
public "register"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleProvider$Sprite$Type<(T)>): void
public "createParticle"(arg0: $ResourceLocation$Type, arg1: $ParticleOptions$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Particle
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientParticleTypesManager$Type = ($ClientParticleTypesManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientParticleTypesManager_ = $ClientParticleTypesManager$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ModConstructor" {
import {$FlammableBlocksContext, $FlammableBlocksContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FlammableBlocksContext"
import {$CreativeModeTabContext, $CreativeModeTabContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$CreativeModeTabContext"
import {$BlockInteractionsContext, $BlockInteractionsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BlockInteractionsContext"
import {$BiomeModificationsContext, $BiomeModificationsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BiomeModificationsContext"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$BuildCreativeModeTabContentsContext, $BuildCreativeModeTabContentsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BuildCreativeModeTabContentsContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EntityAttributesCreateContext, $EntityAttributesCreateContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesCreateContext"
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$EntityAttributesModifyContext, $EntityAttributesModifyContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesModifyContext"
import {$SpawnPlacementsContext, $SpawnPlacementsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$SpawnPlacementsContext"
import {$FuelBurnTimesContext, $FuelBurnTimesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FuelBurnTimesContext"
import {$ModLifecycleContext, $ModLifecycleContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$BaseModConstructor, $BaseModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$BaseModConstructor"

export interface $ModConstructor extends $BaseModConstructor {

 "onConstructMod"(): void
 "onBuildCreativeModeTabContents"(arg0: $BuildCreativeModeTabContentsContext$Type): void
 "onCommonSetup"(): void
/**
 * 
 * @deprecated
 */
 "onCommonSetup"(arg0: $ModLifecycleContext$Type): void
 "onAddDataPackFinders"(arg0: $PackRepositorySourcesContext$Type): void
 "onRegisterCreativeModeTabs"(arg0: $CreativeModeTabContext$Type): void
 "onRegisterFuelBurnTimes"(arg0: $FuelBurnTimesContext$Type): void
 "onRegisterSpawnPlacements"(arg0: $SpawnPlacementsContext$Type): void
 "onRegisterFlammableBlocks"(arg0: $FlammableBlocksContext$Type): void
 "onRegisterBlockInteractions"(arg0: $BlockInteractionsContext$Type): void
 "onRegisterDataPackReloadListeners"(arg0: $AddReloadListenersContext$Type): void
 "onEntityAttributeModification"(arg0: $EntityAttributesModifyContext$Type): void
 "onEntityAttributeCreation"(arg0: $EntityAttributesCreateContext$Type): void
 "onRegisterBiomeModifications"(arg0: $BiomeModificationsContext$Type): void
 "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
 "getPairingIdentifier"(): $ResourceLocation
}

export namespace $ModConstructor {
function construct(arg0: string, arg1: $Supplier$Type<($ModConstructor$Type)>): void
function construct(arg0: string, arg1: $Supplier$Type<($ModConstructor$Type)>, ...arg2: ($ContentRegistrationFlags$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModConstructor$Type = ($ModConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModConstructor_ = $ModConstructor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents" {
import {$ScreenKeyboardEvents$AfterKeyPress, $ScreenKeyboardEvents$AfterKeyPress$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$AfterKeyPress"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScreenKeyboardEvents$AfterKeyRelease, $ScreenKeyboardEvents$AfterKeyRelease$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$AfterKeyRelease"
import {$ScreenKeyboardEvents$BeforeKeyPress, $ScreenKeyboardEvents$BeforeKeyPress$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$BeforeKeyPress"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ScreenKeyboardEvents$BeforeKeyRelease, $ScreenKeyboardEvents$BeforeKeyRelease$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$BeforeKeyRelease"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ScreenKeyboardEvents {


public static "afterKeyRelease"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenKeyboardEvents$AfterKeyRelease<(T)>)>
public static "afterKeyPress"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenKeyboardEvents$AfterKeyPress<(T)>)>
public static "beforeKeyRelease"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenKeyboardEvents$BeforeKeyRelease<(T)>)>
public static "beforeKeyPress"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenKeyboardEvents$BeforeKeyPress<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenKeyboardEvents$Type = ($ScreenKeyboardEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenKeyboardEvents_ = $ScreenKeyboardEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$AdditionalModelsContextForgeImpl" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$AdditionalModelsContext, $AdditionalModelsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$AdditionalModelsContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AdditionalModelsContextForgeImpl extends $Record implements $AdditionalModelsContext {

constructor(consumer: $Consumer$Type<($ResourceLocation$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $Consumer<($ResourceLocation)>
public "registerAdditionalModel"(...arg0: ($ResourceLocation$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionalModelsContextForgeImpl$Type = ($AdditionalModelsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionalModelsContextForgeImpl_ = $AdditionalModelsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ProxyImpl" {
import {$Proxy, $Proxy$Type} from "packages/fuzs/puzzleslib/api/core/v1/$Proxy"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"

export interface $ProxyImpl extends $Proxy {

 "hasShiftDown"(): boolean
 "getClientLevel"(): $Level
 "hasControlDown"(): boolean
 "hasAltDown"(): boolean
 "getClientPlayer"(): $Player
 "getClientPacketListener"(): $ClientPacketListener
 "getKeyMappingComponent"(arg0: string): $Component
/**
 * 
 * @deprecated
 */
 "getGameServer"(): $MinecraftServer
}

export namespace $ProxyImpl {
const INSTANCE: $ProxyImpl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProxyImpl$Type = ($ProxyImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProxyImpl_ = $ProxyImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/$ConfigDataHolderImpl" {
import {$ConfigDataHolder, $ConfigDataHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigDataHolder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ValueCallback, $ValueCallback$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ValueCallback"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ConfigDataHolderImpl<T extends $ConfigCore> implements $ConfigDataHolder<(T)>, $ValueCallback {


public "accept"<S, V extends $ForgeConfigSpec$ConfigValue<(S)>>(arg0: V, arg1: $Consumer$Type<(S)>): V
public "accept"(arg0: $Consumer$Type<(T)>): void
public "isAvailable"(): boolean
public "getConfig"(): T
public "accept"(arg0: $Runnable$Type): void
get "available"(): boolean
get "config"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigDataHolderImpl$Type<T> = ($ConfigDataHolderImpl<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigDataHolderImpl_<T> = $ConfigDataHolderImpl$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingDropsCallback" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingDropsCallback {

 "onLivingDrops"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $Collection$Type<($ItemEntity$Type)>, arg3: integer, arg4: boolean): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $Collection$Type<($ItemEntity$Type)>, arg3: integer, arg4: boolean): $EventResult
}

export namespace $LivingDropsCallback {
const EVENT: $EventInvoker<($LivingDropsCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingDropsCallback$Type = ($LivingDropsCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingDropsCallback_ = $LivingDropsCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeClientProxy" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ClientProxyImpl, $ClientProxyImpl$Type} from "packages/fuzs/puzzleslib/impl/core/$ClientProxyImpl"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"
import {$ForgeServerProxy, $ForgeServerProxy$Type} from "packages/fuzs/puzzleslib/impl/core/$ForgeServerProxy"

export class $ForgeClientProxy extends $ForgeServerProxy implements $ClientProxyImpl {

constructor()

public "registerClientReceiverV2"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "hasShiftDown"(): boolean
public "getClientLevel"(): $Level
public "hasControlDown"(): boolean
public "hasAltDown"(): boolean
public "getClientPlayer"(): $Player
public "getClientPacketListener"(): $ClientPacketListener
public "getKeyMappingComponent"(arg0: string): $Component
get "clientLevel"(): $Level
get "clientPlayer"(): $Player
get "clientPacketListener"(): $ClientPacketListener
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientProxy$Type = ($ForgeClientProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientProxy_ = $ForgeClientProxy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$AttackBlock" {
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

/**
 * 
 * @deprecated
 */
export interface $PlayerInteractEvents$AttackBlock {

 "onAttackBlock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockPos$Type, arg4: $Direction$Type): $EventResultHolder<($InteractionResult)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockPos$Type, arg4: $Direction$Type): $EventResultHolder<($InteractionResult)>
}

export namespace $PlayerInteractEvents$AttackBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$AttackBlock$Type = ($PlayerInteractEvents$AttackBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$AttackBlock_ = $PlayerInteractEvents$AttackBlock$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$BeforeKeyPress" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ScreenKeyboardEvents$BeforeKeyPress<T extends $Screen> {

 "onBeforeKeyPress"(arg0: T, arg1: integer, arg2: integer, arg3: integer): $EventResult

(arg0: T, arg1: integer, arg2: integer, arg3: integer): $EventResult
}

export namespace $ScreenKeyboardEvents$BeforeKeyPress {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenKeyboardEvents$BeforeKeyPress$Type<T> = ($ScreenKeyboardEvents$BeforeKeyPress<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenKeyboardEvents$BeforeKeyPress_<T> = $ScreenKeyboardEvents$BeforeKeyPress$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$CopyTagShapedRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ShapedRecipe, $ShapedRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapedRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$CopyTagRecipe, $CopyTagRecipe$Type} from "packages/fuzs/puzzleslib/impl/item/$CopyTagRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export class $CopyTagShapedRecipe extends $ShapedRecipe implements $CopyTagRecipe {
readonly "width": integer
readonly "height": integer
readonly "result": $ItemStack

constructor(arg0: $ShapedRecipe$Type, arg1: $Ingredient$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getCopyTagSource"(): $Ingredient
public static "registerSerializers"(arg0: $BiConsumer$Type<(string), ($Supplier$Type<($RecipeSerializer$Type<(any)>)>)>): void
public static "getModSerializer"(arg0: string, arg1: string): $RecipeSerializer<(any)>
public "tryCopyTagToResult"(arg0: $ItemStack$Type, arg1: $CraftingContainer$Type): void
get "serializer"(): $RecipeSerializer<(any)>
get "copyTagSource"(): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CopyTagShapedRecipe$Type = ($CopyTagShapedRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CopyTagShapedRecipe_ = $CopyTagShapedRecipe$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/entity/$ClientboundAddEntityDataMessage" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$ClientboundAddEntityPacket, $ClientboundAddEntityPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundAddEntityPacket"

export class $ClientboundAddEntityDataMessage extends $Record implements $ClientboundMessage<($ClientboundAddEntityDataMessage)> {

constructor(vanillaPacket: $ClientboundAddEntityPacket$Type, additionalData: $FriendlyByteBuf$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "vanillaPacket"(): $ClientboundAddEntityPacket
public "additionalData"(): $FriendlyByteBuf
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundAddEntityDataMessage$Type = ($ClientboundAddEntityDataMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundAddEntityDataMessage_ = $ClientboundAddEntityDataMessage$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $EventResultHolder<T> {


public "map"<U>(arg0: $Function$Type<(any), (any)>): $EventResultHolder<(U)>
public "filter"(arg0: $Predicate$Type<(any)>): $EventResultHolder<(T)>
public static "allow"<T>(arg0: T): $EventResultHolder<(T)>
public "flatMap"<U>(arg0: $Function$Type<(any), (any)>): $EventResultHolder<(U)>
public static "interrupt"<T>(arg0: T): $EventResultHolder<(T)>
public static "pass"<T>(): $EventResultHolder<(T)>
public static "deny"<T>(arg0: T): $EventResultHolder<(T)>
public "isInterrupt"(): boolean
public "getInterrupt"(): $Optional<(T)>
public "isPass"(): boolean
public "ifDeny"(arg0: $Consumer$Type<(any)>): $EventResultHolder<(T)>
public "ifInterrupt"(arg0: $Consumer$Type<(any)>): $EventResultHolder<(T)>
public "getDeny"(): $Optional<(T)>
public "getAllow"(): $Optional<(T)>
public "ifAllow"(arg0: $Consumer$Type<(any)>): $EventResultHolder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventResultHolder$Type<T> = ($EventResultHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventResultHolder_<T> = $EventResultHolder$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$LootTableLoadEvents" {
import {$LootTableLoadEvents$Modify, $LootTableLoadEvents$Modify$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$LootTableLoadEvents$Modify"
import {$LootTableLoadEvents$Replace, $LootTableLoadEvents$Replace$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$LootTableLoadEvents$Replace"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $LootTableLoadEvents {
static readonly "REPLACE": $EventInvoker<($LootTableLoadEvents$Replace)>
static readonly "MODIFY": $EventInvoker<($LootTableLoadEvents$Modify)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableLoadEvents$Type = ($LootTableLoadEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableLoadEvents_ = $LootTableLoadEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MessageV3, $MessageV3$Type} from "packages/fuzs/puzzleslib/api/network/v3/$MessageV3"
import {$ClientMessageListener, $ClientMessageListener$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientMessageListener"

export interface $ClientboundMessage<T extends $Record> extends $MessageV3<($ClientMessageListener<(T)>)> {

 "getHandler"(): $ClientMessageListener<(T)>

(): $ClientMessageListener<(T)>
}

export namespace $ClientboundMessage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundMessage$Type<T> = ($ClientboundMessage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundMessage_<T> = $ClientboundMessage$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$Remove" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenEvents$Remove<T extends $Screen> {

 "onRemove"(arg0: T): void

(arg0: T): void
}

export namespace $ScreenEvents$Remove {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$Remove$Type<T> = ($ScreenEvents$Remove<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$Remove_<T> = $ScreenEvents$Remove$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$CoreShadersContext" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export interface $CoreShadersContext {

 "registerCoreShader"(arg0: $ResourceLocation$Type, arg1: $VertexFormat$Type, arg2: $Consumer$Type<($ShaderInstance$Type)>): void

(arg0: $ResourceLocation$Type, arg1: $VertexFormat$Type, arg2: $Consumer$Type<($ShaderInstance$Type)>): void
}

export namespace $CoreShadersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreShadersContext$Type = ($CoreShadersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreShadersContext_ = $CoreShadersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$ModifyBakedModel" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ModelEvents$ModifyBakedModel {

 "onModifyBakedModel"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($BakedModel$Type)>, arg2: $Supplier$Type<($ModelBaker$Type)>, arg3: $Function$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg4: $BiConsumer$Type<($ResourceLocation$Type), ($BakedModel$Type)>): $EventResultHolder<($BakedModel)>

(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($BakedModel$Type)>, arg2: $Supplier$Type<($ModelBaker$Type)>, arg3: $Function$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg4: $BiConsumer$Type<($ResourceLocation$Type), ($BakedModel$Type)>): $EventResultHolder<($BakedModel)>
}

export namespace $ModelEvents$ModifyBakedModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$ModifyBakedModel$Type = ($ModelEvents$ModifyBakedModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents$ModifyBakedModel_ = $ModelEvents$ModifyBakedModel$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$ItemPickup" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export interface $PlayerEvents$ItemPickup {

 "onItemPickup"(arg0: $Player$Type, arg1: $ItemEntity$Type, arg2: $ItemStack$Type): void

(arg0: $Player$Type, arg1: $ItemEntity$Type, arg2: $ItemStack$Type): void
}

export namespace $PlayerEvents$ItemPickup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$ItemPickup$Type = ($PlayerEvents$ItemPickup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$ItemPickup_ = $PlayerEvents$ItemPickup$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$BiomeSpecialEffectsBuilderForgeAccessor" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Music, $Music$Type} from "packages/net/minecraft/sounds/$Music"
import {$AmbientMoodSettings, $AmbientMoodSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientMoodSettings"
import {$AmbientParticleSettings, $AmbientParticleSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientParticleSettings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AmbientAdditionsSettings, $AmbientAdditionsSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientAdditionsSettings"

export interface $BiomeSpecialEffectsBuilderForgeAccessor {

 "puzzleslib$setBackgroundMusic"(arg0: $Optional$Type<($Music$Type)>): void
 "puzzleslib$setGrassColorOverride"(arg0: $Optional$Type<(integer)>): void
 "puzzleslib$setAmbientMoodSettings"(arg0: $Optional$Type<($AmbientMoodSettings$Type)>): void
 "puzzleslib$setFoliageColorOverride"(arg0: $Optional$Type<(integer)>): void
 "puzzleslib$setAmbientParticle"(arg0: $Optional$Type<($AmbientParticleSettings$Type)>): void
 "puzzleslib$setAmbientAdditionsSettings"(arg0: $Optional$Type<($AmbientAdditionsSettings$Type)>): void
 "puzzleslib$setAmbientLoopSoundEvent"(arg0: $Optional$Type<($Holder$Type<($SoundEvent$Type)>)>): void
}

export namespace $BiomeSpecialEffectsBuilderForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeSpecialEffectsBuilderForgeAccessor$Type = ($BiomeSpecialEffectsBuilderForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeSpecialEffectsBuilderForgeAccessor_ = $BiomeSpecialEffectsBuilderForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ModLoaderEnvironment" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ModLoader, $ModLoader$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoader"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ObjectShareAccess, $ObjectShareAccess$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ObjectShareAccess"
import {$ModContainer, $ModContainer$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModContainer"
import {$DistType, $DistType$Type} from "packages/fuzs/puzzleslib/api/core/v1/$DistType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ModLoaderEnvironment {

 "isModPresentServerside"(arg0: string): boolean
 "getObjectShareAccess"(): $ObjectShareAccess
 "isDevelopmentEnvironment"(): boolean
 "getGameDirectory"(): $Path
 "isFabric"(): boolean
 "isClient"(): boolean
 "getConfigDirectory"(): $Path
/**
 * 
 * @deprecated
 */
 "getModName"(arg0: string): $Optional<(string)>
/**
 * 
 * @deprecated
 */
 "getEnvironmentType"(): $DistType
 "getModList"(): $Map<(string), ($ModContainer)>
 "getModLoader"(): $ModLoader
 "isForge"(): boolean
 "isQuilt"(): boolean
/**
 * 
 * @deprecated
 */
 "isEnvironmentType"(arg0: $DistType$Type): boolean
/**
 * 
 * @deprecated
 */
 "findModResource"(arg0: string, ...arg1: (string)[]): $Optional<($Path)>
/**
 * 
 * @deprecated
 */
 "isModLoadedSafe"(arg0: string): boolean
 "getModContainer"(arg0: string): $Optional<($ModContainer)>
 "isServer"(): boolean
 "getModsDirectory"(): $Path
 "isModLoaded"(arg0: string): boolean
}

export namespace $ModLoaderEnvironment {
const INSTANCE: $ModLoaderEnvironment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoaderEnvironment$Type = ($ModLoaderEnvironment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoaderEnvironment_ = $ModLoaderEnvironment$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncedCapabilityComponent" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"

export interface $SyncedCapabilityComponent extends $CapabilityComponent {

 "isDirty"(): boolean
 "markClean"(): void
 "markDirty"(): void
 "write"(arg0: $CompoundTag$Type): void
 "read"(arg0: $CompoundTag$Type): void
 "toCompoundTag"(): $CompoundTag
}

export namespace $SyncedCapabilityComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncedCapabilityComponent$Type = ($SyncedCapabilityComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncedCapabilityComponent_ = $SyncedCapabilityComponent$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

/**
 * 
 * @deprecated
 */
export interface $ModLifecycleContext {

 "enqueueWork"(arg0: $Runnable$Type): void

(arg0: $Runnable$Type): void
}

export namespace $ModLifecycleContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLifecycleContext$Type = ($ModLifecycleContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLifecycleContext_ = $ModLifecycleContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/data/$ForgeCapabilityKey$CapabilityTokenFactory" {
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$CapabilityToken, $CapabilityToken$Type} from "packages/net/minecraftforge/common/capabilities/$CapabilityToken"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export interface $ForgeCapabilityKey$CapabilityTokenFactory<C extends $CapabilityComponent> {

 "apply"(arg0: $CapabilityToken$Type<(C)>): $Capability<(C)>

(arg0: $CapabilityToken$Type<(C)>): $Capability<(C)>
}

export namespace $ForgeCapabilityKey$CapabilityTokenFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCapabilityKey$CapabilityTokenFactory$Type<C> = ($ForgeCapabilityKey$CapabilityTokenFactory<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCapabilityKey$CapabilityTokenFactory_<C> = $ForgeCapabilityKey$CapabilityTokenFactory$Type<(C)>;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$BuiltinModelItemRendererContextForgeImpl" {
import {$DynamicBuiltinItemRenderer, $DynamicBuiltinItemRenderer$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicBuiltinItemRenderer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$BuiltinModelItemRendererContext, $BuiltinModelItemRendererContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BuiltinModelItemRendererContext"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $BuiltinModelItemRendererContextForgeImpl extends $Record implements $BuiltinModelItemRendererContext {

constructor(modId: string, dynamicRenderers: $List$Type<($ResourceManagerReloadListener$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerItemRenderer"(arg0: $DynamicBuiltinItemRenderer$Type, ...arg1: ($ItemLike$Type)[]): void
public "dynamicRenderers"(): $List<($ResourceManagerReloadListener)>
public "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltinModelItemRendererContextForgeImpl$Type = ($BuiltinModelItemRendererContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltinModelItemRendererContextForgeImpl_ = $BuiltinModelItemRendererContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$AfterInit" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

/**
 * 
 * @deprecated
 */
export interface $ScreenEvents$AfterInit {

 "onAfterInit"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>, arg5: $Consumer$Type<($AbstractWidget$Type)>, arg6: $Consumer$Type<($AbstractWidget$Type)>): void

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>, arg5: $Consumer$Type<($AbstractWidget$Type)>, arg6: $Consumer$Type<($AbstractWidget$Type)>): void
}

export namespace $ScreenEvents$AfterInit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$AfterInit$Type = ($ScreenEvents$AfterInit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$AfterInit_ = $ScreenEvents$AfterInit$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Start" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $UseItemEvents$Start {

 "onUseItemStart"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: $MutableInt$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: $MutableInt$Type): $EventResult
}

export namespace $UseItemEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemEvents$Start$Type = ($UseItemEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemEvents$Start_ = $UseItemEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemModelPropertiesContext" {
import {$ClampedItemPropertyFunction, $ClampedItemPropertyFunction$Type} from "packages/net/minecraft/client/renderer/item/$ClampedItemPropertyFunction"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ItemModelPropertiesContext {

 "registerGlobalProperty"(arg0: $ResourceLocation$Type, arg1: $ClampedItemPropertyFunction$Type): void
 "registerItemProperty"(arg0: $ResourceLocation$Type, arg1: $ClampedItemPropertyFunction$Type, ...arg2: ($ItemLike$Type)[]): void
}

export namespace $ItemModelPropertiesContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelPropertiesContext$Type = ($ItemModelPropertiesContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelPropertiesContext_ = $ItemModelPropertiesContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/$ForgeCapabilityHelper" {
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$CapabilityToken, $CapabilityToken$Type} from "packages/net/minecraftforge/common/capabilities/$CapabilityToken"
import {$CapabilityKey, $CapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityKey"

export class $ForgeCapabilityHelper {


public static "setCapabilityToken"<C extends $CapabilityComponent>(arg0: $CapabilityKey$Type<(C)>, arg1: $CapabilityToken$Type<(C)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCapabilityHelper$Type = ($ForgeCapabilityHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCapabilityHelper_ = $ForgeCapabilityHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext" {
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$DataProviderContext, $DataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $ForgeDataProviderContext extends $DataProviderContext {

constructor(arg0: string, arg1: $PackOutput$Type, arg2: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg3: $ExistingFileHelper$Type)

public static "fromEvent"(arg0: string, arg1: $GatherDataEvent$Type): $ForgeDataProviderContext
public "getFileHelper"(): $ExistingFileHelper
get "fileHelper"(): $ExistingFileHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeDataProviderContext$Type = ($ForgeDataProviderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeDataProviderContext_ = $ForgeDataProviderContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents$GuiOverlay" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RenderGuiElementEvents$GuiOverlay extends $Record {

constructor(id: $ResourceLocation$Type, filter: $Predicate$Type<($Minecraft$Type)>)
constructor(arg0: string, arg1: $Predicate$Type<($Minecraft$Type)>)
constructor(arg0: $ResourceLocation$Type)
constructor(arg0: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "filter"(): $Predicate<($Minecraft)>
public "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderGuiElementEvents$GuiOverlay$Type = ($RenderGuiElementEvents$GuiOverlay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderGuiElementEvents$GuiOverlay_ = $RenderGuiElementEvents$GuiOverlay$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$CommonFactories" {
import {$PotionBrewingRegistry, $PotionBrewingRegistry$Type} from "packages/fuzs/puzzleslib/api/init/v2/$PotionBrewingRegistry"
import {$ProxyImpl, $ProxyImpl$Type} from "packages/fuzs/puzzleslib/impl/core/$ProxyImpl"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ToolTypeHelper, $ToolTypeHelper$Type} from "packages/fuzs/puzzleslib/api/item/v2/$ToolTypeHelper"
import {$GameRulesFactory, $GameRulesFactory$Type} from "packages/fuzs/puzzleslib/api/init/v2/$GameRulesFactory"
import {$CombinedIngredients, $CombinedIngredients$Type} from "packages/fuzs/puzzleslib/api/item/v2/crafting/$CombinedIngredients"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ModConstructor, $ModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModConstructor"
import {$ModContext, $ModContext$Type} from "packages/fuzs/puzzleslib/impl/core/$ModContext"

export interface $CommonFactories {

 "getModContext"(arg0: string): $ModContext
 "getToolTypeHelper"(): $ToolTypeHelper
 "getClientProxy"(): $ProxyImpl
 "getServerProxy"(): $ProxyImpl
 "constructMod"(arg0: string, arg1: $ModConstructor$Type, arg2: $Set$Type<($ContentRegistrationFlags$Type)>, arg3: $Set$Type<($ContentRegistrationFlags$Type)>): void
 "getPotionBrewingRegistry"(): $PotionBrewingRegistry
 "getGameRulesFactory"(): $GameRulesFactory
 "registerLoadingHandlers"(): void
 "registerEventHandlers"(): void
 "getCombinedIngredients"(): $CombinedIngredients
}

export namespace $CommonFactories {
const INSTANCE: $CommonFactories
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonFactories$Type = ($CommonFactories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonFactories_ = $CommonFactories$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/item/crafting/$ForgeCombinedIngredients" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$CombinedIngredients, $CombinedIngredients$Type} from "packages/fuzs/puzzleslib/api/item/v2/crafting/$CombinedIngredients"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $ForgeCombinedIngredients implements $CombinedIngredients {

constructor()

public "difference"(arg0: $Ingredient$Type, arg1: $Ingredient$Type): $Ingredient
public "all"(...arg0: ($Ingredient$Type)[]): $Ingredient
public "any"(...arg0: ($Ingredient$Type)[]): $Ingredient
public "nbt"(arg0: $ItemStack$Type, arg1: boolean): $Ingredient
public "nbt"(arg0: $ItemLike$Type, arg1: $CompoundTag$Type, arg2: boolean): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCombinedIngredients$Type = ($ForgeCombinedIngredients);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCombinedIngredients_ = $ForgeCombinedIngredients$Type;
}}
declare module "packages/fuzs/puzzleslib/api/entity/v1/$DamageSourcesHelper" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DamageSourcesHelper {


public static "source"(arg0: $RegistryAccess$Type, arg1: $ResourceKey$Type<($DamageType$Type)>, arg2: $Entity$Type, arg3: $Entity$Type): $DamageSource
public static "source"(arg0: $LevelReader$Type, arg1: $ResourceKey$Type<($DamageType$Type)>, arg2: $Entity$Type, arg3: $Entity$Type): $DamageSource
public static "source"(arg0: $LevelReader$Type, arg1: $ResourceKey$Type<($DamageType$Type)>, arg2: $Entity$Type): $DamageSource
public static "source"(arg0: $LevelReader$Type, arg1: $ResourceKey$Type<($DamageType$Type)>): $DamageSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DamageSourcesHelper$Type = ($DamageSourcesHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DamageSourcesHelper_ = $DamageSourcesHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLivingEvents$After" {
import {$LivingEntityRenderer, $LivingEntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$LivingEntityRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $RenderLivingEvents$After {

 "onAfterRenderEntity"<T extends $LivingEntity, M extends $EntityModel<(T)>>(arg0: T, arg1: $LivingEntityRenderer$Type<(T), (M)>, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void

(arg0: T, arg1: $LivingEntityRenderer$Type<(T), (M)>, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void
}

export namespace $RenderLivingEvents$After {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLivingEvents$After$Type = ($RenderLivingEvents$After);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLivingEvents$After_ = $RenderLivingEvents$After$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ContentRegistrationFlags extends $Enum<($ContentRegistrationFlags)> {
static readonly "BIOME_MODIFICATIONS": $ContentRegistrationFlags
static readonly "DYNAMIC_RENDERERS": $ContentRegistrationFlags
/**
 * 
 * @deprecated
 */
static readonly "LEGACY_SMITHING": $ContentRegistrationFlags
static readonly "COPY_TAG_RECIPES": $ContentRegistrationFlags
static readonly "CLIENT_PARTICLE_TYPES": $ContentRegistrationFlags


public static "values"(): ($ContentRegistrationFlags)[]
public static "valueOf"(arg0: string): $ContentRegistrationFlags
public static "throwForFlag"(arg0: $ContentRegistrationFlags$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContentRegistrationFlags$Type = (("biome_modifications") | ("legacy_smithing") | ("copy_tag_recipes") | ("client_particle_types") | ("dynamic_renderers")) | ($ContentRegistrationFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContentRegistrationFlags_ = $ContentRegistrationFlags$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$CreativeModeTabContext" {
import {$CreativeModeTabConfigurator, $CreativeModeTabConfigurator$Type} from "packages/fuzs/puzzleslib/api/item/v2/$CreativeModeTabConfigurator"

export interface $CreativeModeTabContext {

 "registerCreativeModeTab"(arg0: $CreativeModeTabConfigurator$Type): void

(arg0: $CreativeModeTabConfigurator$Type): void
}

export namespace $CreativeModeTabContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeModeTabContext$Type = ($CreativeModeTabContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeModeTabContext_ = $CreativeModeTabContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/$ForgeConfigHolderImpl" {
import {$ConfigHolder$Builder, $ConfigHolder$Builder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder"
import {$ConfigHolderImpl, $ConfigHolderImpl$Type} from "packages/fuzs/puzzleslib/impl/config/$ConfigHolderImpl"

export class $ForgeConfigHolderImpl extends $ConfigHolderImpl {

constructor(arg0: string)

public static "builder"(arg0: string): $ConfigHolder$Builder
public static "simpleName"(arg0: string): string
public static "moveToDir"(arg0: string, arg1: string): string
public static "defaultName"(arg0: string, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfigHolderImpl$Type = ($ForgeConfigHolderImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfigHolderImpl_ = $ForgeConfigHolderImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/data/$CapabilityHolder" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $CapabilityHolder<T extends $CapabilityComponent> implements $ICapabilityProvider, $INBTSerializable<($CompoundTag)> {

constructor(arg0: $Capability$Type<(T)>, arg1: T)

public "getCapability"<S>(arg0: $Capability$Type<(S)>, arg1: $Direction$Type): $LazyOptional<(S)>
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityHolder$Type<T> = ($CapabilityHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityHolder_<T> = $CapabilityHolder$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/client/init/$ItemDisplayOverridesImpl" {
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemModelDisplayOverrides, $ItemModelDisplayOverrides$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$ItemModelDisplayOverrides"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"

export class $ItemDisplayOverridesImpl implements $ItemModelDisplayOverrides {

constructor()

public "register"(arg0: $ModelResourceLocation$Type, arg1: $ModelResourceLocation$Type, ...arg2: ($ItemDisplayContext$Type)[]): void
public "getItemModelDisplayOverride"(arg0: $BakedModel$Type, arg1: $ItemDisplayContext$Type): $BakedModel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemDisplayOverridesImpl$Type = ($ItemDisplayOverridesImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemDisplayOverridesImpl_ = $ItemDisplayOverridesImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventDefaultedBoolean" {
import {$DefaultedBoolean, $DefaultedBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedBoolean"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MutableBoolean, $MutableBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean"
import {$EventMutableBoolean, $EventMutableBoolean$Type} from "packages/fuzs/puzzleslib/impl/event/data/$EventMutableBoolean"

export class $EventDefaultedBoolean extends $EventMutableBoolean implements $DefaultedBoolean {

constructor(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>, arg2: $Supplier$Type<(boolean)>)

public "accept"(arg0: boolean): void
public "getAsDefaultBoolean"(): boolean
public "getAsOptionalBoolean"(): $Optional<(boolean)>
public "mapDefaultBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
public static "fromValue"(arg0: boolean): $DefaultedBoolean
public static "fromEvent"(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>, arg2: $Supplier$Type<(boolean)>): $DefaultedBoolean
public "applyDefaultBoolean"(): void
public static "fromEvent"(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>): $MutableBoolean
get "asDefaultBoolean"(): boolean
get "asOptionalBoolean"(): $Optional<(boolean)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventDefaultedBoolean$Type = ($EventDefaultedBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventDefaultedBoolean_ = $EventDefaultedBoolean$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$TagsUpdatedCallback" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $TagsUpdatedCallback {

 "onTagsUpdated"(arg0: $RegistryAccess$Type, arg1: boolean): void

(arg0: $RegistryAccess$Type, arg1: boolean): void
}

export namespace $TagsUpdatedCallback {
const EVENT: $EventInvoker<($TagsUpdatedCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagsUpdatedCallback$Type = ($TagsUpdatedCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagsUpdatedCallback_ = $TagsUpdatedCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/$ConfigHolderRegistry" {
import {$ConfigDataHolder, $ConfigDataHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigDataHolder"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigHolder$Builder, $ConfigHolder$Builder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder"
import {$ConfigHolder, $ConfigHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder"

export interface $ConfigHolderRegistry extends $ConfigHolder {

/**
 * 
 * @deprecated
 */
 "get"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): T
/**
 * 
 * @deprecated
 */
 "getHolder"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigDataHolder<(T)>

(arg0: $Class$Type<(T)>): T
}

export namespace $ConfigHolderRegistry {
function builder(arg0: string): $ConfigHolder$Builder
function simpleName(arg0: string): string
function moveToDir(arg0: string, arg1: string): string
function defaultName(arg0: string, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHolderRegistry$Type = ($ConfigHolderRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHolderRegistry_ = $ConfigHolderRegistry$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$BuildCreativeModeTabContentsContext" {
import {$CreativeModeTab$DisplayItemsGenerator, $CreativeModeTab$DisplayItemsGenerator$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$DisplayItemsGenerator"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BuildCreativeModeTabContentsContext {

 "registerBuildListener"(arg0: string, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void
 "registerBuildListener"(arg0: $ResourceLocation$Type, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void
 "registerBuildListener"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void

(arg0: string, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void
}

export namespace $BuildCreativeModeTabContentsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuildCreativeModeTabContentsContext$Type = ($BuildCreativeModeTabContentsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuildCreativeModeTabContentsContext_ = $BuildCreativeModeTabContentsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/resources/$ForwardingReloadListenerHelper" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$NamedReloadListener, $NamedReloadListener$Type} from "packages/fuzs/puzzleslib/api/core/v1/resources/$NamedReloadListener"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForwardingReloadListenerHelper {


public static "fromResourceManagerReloadListeners"<T extends ($ResourceManagerReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($Collection$Type<($ResourceManagerReloadListener$Type)>)>): T
public static "fromResourceManagerReloadListeners"<T extends ($ResourceManagerReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $Collection$Type<($ResourceManagerReloadListener$Type)>): T
public static "fromReloadListener"<T extends ($PreparableReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($PreparableReloadListener$Type)>): T
public static "fromReloadListener"<T extends ($PreparableReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $PreparableReloadListener$Type): T
public static "fromReloadListeners"<T extends ($PreparableReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $Collection$Type<($PreparableReloadListener$Type)>): T
public static "fromReloadListeners"<T extends ($PreparableReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($Collection$Type<($PreparableReloadListener$Type)>)>): T
public static "fromResourceManagerReloadListener"<T extends ($ResourceManagerReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($ResourceManagerReloadListener$Type)>): T
public static "fromResourceManagerReloadListener"<T extends ($ResourceManagerReloadListener) & ($NamedReloadListener)>(arg0: $ResourceLocation$Type, arg1: $ResourceManagerReloadListener$Type): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForwardingReloadListenerHelper$Type = ($ForwardingReloadListenerHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForwardingReloadListenerHelper_ = $ForwardingReloadListenerHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$AfterMouseScroll" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $InputEvents$AfterMouseScroll {

 "onAfterMouseScroll"(arg0: boolean, arg1: boolean, arg2: boolean, arg3: double, arg4: double): void

(arg0: boolean, arg1: boolean, arg2: boolean, arg3: double, arg4: double): void
}

export namespace $InputEvents$AfterMouseScroll {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$AfterMouseScroll$Type = ($InputEvents$AfterMouseScroll);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents$AfterMouseScroll_ = $InputEvents$AfterMouseScroll$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderHighlightCallback" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

export interface $RenderHighlightCallback {

 "onRenderHighlight"(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: $HitResult$Type, arg4: float, arg5: $PoseStack$Type, arg6: $MultiBufferSource$Type, arg7: $ClientLevel$Type): $EventResult

(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: $HitResult$Type, arg4: float, arg5: $PoseStack$Type, arg6: $MultiBufferSource$Type, arg7: $ClientLevel$Type): $EventResult
}

export namespace $RenderHighlightCallback {
const EVENT: $EventInvoker<($RenderHighlightCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHighlightCallback$Type = ($RenderHighlightCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHighlightCallback_ = $RenderHighlightCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractTagProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AbstractTagProvider {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractTagProvider$Type = ($AbstractTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractTagProvider_ = $AbstractTagProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/$AbstractTagProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AbstractTagProvider {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractTagProvider$Type = ($AbstractTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractTagProvider_ = $AbstractTagProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseScroll" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ScreenMouseEvents$BeforeMouseScroll<T extends $Screen> {

 "onBeforeMouseScroll"(arg0: T, arg1: double, arg2: double, arg3: double, arg4: double): $EventResult

(arg0: T, arg1: double, arg2: double, arg3: double, arg4: double): $EventResult
}

export namespace $ScreenMouseEvents$BeforeMouseScroll {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$BeforeMouseScroll$Type<T> = ($ScreenMouseEvents$BeforeMouseScroll<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$BeforeMouseScroll_<T> = $ScreenMouseEvents$BeforeMouseScroll$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$AnvilRepairCallback" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $AnvilRepairCallback {

 "onAnvilRepair"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $ItemStack$Type, arg3: $ItemStack$Type, arg4: $MutableFloat$Type): void

(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $ItemStack$Type, arg3: $ItemStack$Type, arg4: $MutableFloat$Type): void
}

export namespace $AnvilRepairCallback {
const EVENT: $EventInvoker<($AnvilRepairCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilRepairCallback$Type = ($AnvilRepairCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilRepairCallback_ = $AnvilRepairCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$BlockEntityRenderersContext" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityRendererProvider, $BlockEntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"

export interface $BlockEntityRenderersContext {

 "registerBlockEntityRenderer"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockEntityRendererProvider$Type<(T)>): void

(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockEntityRendererProvider$Type<(T)>): void
}

export namespace $BlockEntityRenderersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityRenderersContext$Type = ($BlockEntityRenderersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityRenderersContext_ = $BlockEntityRenderersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicBakingCompletedContext" {
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$ModelManager, $ModelManager$Type} from "packages/net/minecraft/client/resources/model/$ModelManager"

/**
 * 
 * @deprecated
 */
export interface $DynamicBakingCompletedContext {

 "models"(): $Map<($ResourceLocation), ($BakedModel)>
 "getModel"(arg0: $ResourceLocation$Type): $BakedModel
 "modelManager"(): $ModelManager
 "modelBakery"(): $ModelBakery
}

export namespace $DynamicBakingCompletedContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicBakingCompletedContext$Type = ($DynamicBakingCompletedContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicBakingCompletedContext_ = $DynamicBakingCompletedContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeAbstractions" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CommonAbstractions, $CommonAbstractions$Type} from "packages/fuzs/puzzleslib/api/core/v1/$CommonAbstractions"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$MobSpawnType, $MobSpawnType$Type} from "packages/net/minecraft/world/entity/$MobSpawnType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Pack$Info, $Pack$Info$Type} from "packages/net/minecraft/server/packs/repository/$Pack$Info"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeAbstractions implements $CommonAbstractions {

constructor()

public "openMenu"(arg0: $ServerPlayer$Type, arg1: $MenuProvider$Type, arg2: $BiConsumer$Type<($ServerPlayer$Type), ($FriendlyByteBuf$Type)>): void
public "getMinecraftServer"(): $MinecraftServer
public "getEnchantPowerBonus"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): float
public "canApplyAtEnchantingTable"(arg0: $Enchantment$Type, arg1: $ItemStack$Type): boolean
public "onPlayerDestroyItem"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $InteractionHand$Type): void
public "canEquip"(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type, arg2: $Entity$Type): boolean
public "createPackInfo"(arg0: $ResourceLocation$Type, arg1: $Component$Type, arg2: integer, arg3: $FeatureFlagSet$Type, arg4: boolean): $Pack$Info
public "getMobLootingLevel"(arg0: $Entity$Type, arg1: $Entity$Type, arg2: $DamageSource$Type): integer
public "getMobSpawnType"(arg0: $Mob$Type): $MobSpawnType
public "isBossMob"(arg0: $EntityType$Type<(any)>): boolean
public "getMobGriefingRule"(arg0: $Level$Type, arg1: $Entity$Type): boolean
public "isAllowedOnBooks"(arg0: $Enchantment$Type): boolean
public "openMenu"(arg0: $ServerPlayer$Type, arg1: $MenuProvider$Type): void
get "minecraftServer"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeAbstractions$Type = ($ForgeAbstractions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeAbstractions_ = $ForgeAbstractions$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents" {
import {$ServerEntityLevelEvents$LoadV2, $ServerEntityLevelEvents$LoadV2$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$LoadV2"
import {$ServerEntityLevelEvents$Remove, $ServerEntityLevelEvents$Remove$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$Remove"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ServerEntityLevelEvents$Load, $ServerEntityLevelEvents$Load$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$Load"
import {$ServerEntityLevelEvents$Spawn, $ServerEntityLevelEvents$Spawn$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$Spawn"

export class $ServerEntityLevelEvents {
/**
 * 
 * @deprecated
 */
static readonly "LOAD": $EventInvoker<($ServerEntityLevelEvents$Load)>
static readonly "LOAD_V2": $EventInvoker<($ServerEntityLevelEvents$LoadV2)>
static readonly "SPAWN": $EventInvoker<($ServerEntityLevelEvents$Spawn)>
static readonly "REMOVE": $EventInvoker<($ServerEntityLevelEvents$Remove)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityLevelEvents$Type = ($ServerEntityLevelEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityLevelEvents_ = $ServerEntityLevelEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientChunkEvents" {
import {$ClientChunkEvents$Load, $ClientChunkEvents$Load$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientChunkEvents$Load"
import {$ClientChunkEvents$Unload, $ClientChunkEvents$Unload$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientChunkEvents$Unload"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ClientChunkEvents {
static readonly "LOAD": $EventInvoker<($ClientChunkEvents$Load)>
static readonly "UNLOAD": $EventInvoker<($ClientChunkEvents$Unload)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChunkEvents$Type = ($ClientChunkEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChunkEvents_ = $ClientChunkEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$SpawnPlacementsContextForgeImpl" {
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$SpawnPlacementRegisterEvent, $SpawnPlacementRegisterEvent$Type} from "packages/net/minecraftforge/event/entity/$SpawnPlacementRegisterEvent"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$SpawnPlacementsContext, $SpawnPlacementsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$SpawnPlacementsContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SpawnPlacements$SpawnPredicate, $SpawnPlacements$SpawnPredicate$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$SpawnPredicate"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"

export class $SpawnPlacementsContextForgeImpl extends $Record implements $SpawnPlacementsContext {

constructor(evt: $SpawnPlacementRegisterEvent$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerSpawnPlacement"<T extends $Mob>(arg0: $EntityType$Type<(T)>, arg1: $SpawnPlacements$Type$Type, arg2: $Heightmap$Types$Type, arg3: $SpawnPlacements$SpawnPredicate$Type<(T)>): void
public "evt"(): $SpawnPlacementRegisterEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnPlacementsContextForgeImpl$Type = ($SpawnPlacementsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnPlacementsContextForgeImpl_ = $SpawnPlacementsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$PlayLevelSoundEvents" {
import {$PlayLevelSoundEvents$AtPosition, $PlayLevelSoundEvents$AtPosition$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$PlayLevelSoundEvents$AtPosition"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$PlayLevelSoundEvents$AtEntity, $PlayLevelSoundEvents$AtEntity$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$PlayLevelSoundEvents$AtEntity"

export class $PlayLevelSoundEvents {
static readonly "POSITION": $EventInvoker<($PlayLevelSoundEvents$AtPosition)>
static readonly "ENTITY": $EventInvoker<($PlayLevelSoundEvents$AtEntity)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayLevelSoundEvents$Type = ($PlayLevelSoundEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayLevelSoundEvents_ = $PlayLevelSoundEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$AttackBlockV2" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $PlayerInteractEvents$AttackBlockV2 {

 "onAttackBlock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockPos$Type, arg4: $Direction$Type): $EventResult

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockPos$Type, arg4: $Direction$Type): $EventResult
}

export namespace $PlayerInteractEvents$AttackBlockV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$AttackBlockV2$Type = ($PlayerInteractEvents$AttackBlockV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$AttackBlockV2_ = $PlayerInteractEvents$AttackBlockV2$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$SkullRenderersContext" {
import {$SkullRenderersFactory, $SkullRenderersFactory$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$SkullRenderersFactory"

export interface $SkullRenderersContext {

 "registerSkullRenderer"(arg0: $SkullRenderersFactory$Type): void

(arg0: $SkullRenderersFactory$Type): void
}

export namespace $SkullRenderersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkullRenderersContext$Type = ($SkullRenderersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkullRenderersContext_ = $SkullRenderersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderHelper" {
import {$ForgeDataProviderContext$Factory, $ForgeDataProviderContext$Factory$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext$Factory"
import {$ForgeDataProviderContext$LegacyFactory, $ForgeDataProviderContext$LegacyFactory$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext$LegacyFactory"

export class $DataProviderHelper {


public static "registerDataProviders"(arg0: string, ...arg1: ($ForgeDataProviderContext$LegacyFactory$Type)[]): void
public static "registerDataProviders"(arg0: string, ...arg1: ($ForgeDataProviderContext$Factory$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataProviderHelper$Type = ($DataProviderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataProviderHelper_ = $DataProviderHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/$AbstractRecipeProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$RecipeProvider, $RecipeProvider$Type} from "packages/net/minecraft/data/recipes/$RecipeProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$DataProviderContext, $DataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $AbstractRecipeProvider extends $RecipeProvider {

constructor(arg0: $DataProviderContext$Type)
constructor(arg0: string, arg1: $PackOutput$Type)

public "m_245200_"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public "addRecipes"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRecipeProvider$Type = ($AbstractRecipeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRecipeProvider_ = $AbstractRecipeProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractRecipeProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$RecipeProvider, $RecipeProvider$Type} from "packages/net/minecraft/data/recipes/$RecipeProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"

export class $AbstractRecipeProvider extends $RecipeProvider {

constructor(arg0: $GatherDataEvent$Type, arg1: string)
constructor(arg0: $PackOutput$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRecipeProvider$Type = ($AbstractRecipeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRecipeProvider_ = $AbstractRecipeProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$ClientMessageListener" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"

export class $ClientMessageListener<T extends $Record> {

constructor()

public "handle"(arg0: T, arg1: $Minecraft$Type, arg2: $ClientPacketListener$Type, arg3: $LocalPlayer$Type, arg4: $ClientLevel$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientMessageListener$Type<T> = ($ClientMessageListener<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientMessageListener_<T> = $ClientMessageListener$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$ModelLayerFactory" {
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"

export interface $ModelLayerFactory {

 "registerOuterArmor"(arg0: string): $ModelLayerLocation
 "registerInnerArmor"(arg0: string): $ModelLayerLocation
 "register"(arg0: string): $ModelLayerLocation
 "register"(arg0: string, arg1: string): $ModelLayerLocation
}

export namespace $ModelLayerFactory {
function from(arg0: string): $ModelLayerFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelLayerFactory$Type = ($ModelLayerFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelLayerFactory_ = $ModelLayerFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseScroll" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenMouseEvents$AfterMouseScroll<T extends $Screen> {

 "onAfterMouseScroll"(arg0: T, arg1: double, arg2: double, arg3: double, arg4: double): void

(arg0: T, arg1: double, arg2: double, arg3: double, arg4: double): void
}

export namespace $ScreenMouseEvents$AfterMouseScroll {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$AfterMouseScroll$Type<T> = ($ScreenMouseEvents$AfterMouseScroll<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$AfterMouseScroll_<T> = $ScreenMouseEvents$AfterMouseScroll$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ExplosionEvents$Start" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ExplosionEvents$Start {

 "onExplosionStart"(arg0: $Level$Type, arg1: $Explosion$Type): $EventResult

(arg0: $Level$Type, arg1: $Explosion$Type): $EventResult
}

export namespace $ExplosionEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionEvents$Start$Type = ($ExplosionEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionEvents$Start_ = $ExplosionEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/client/model/$ModItemModelProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ItemModelBuilder, $ItemModelBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelBuilder"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ItemModelProvider, $ItemModelProvider$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockStateProvider, $BlockStateProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockStateProvider"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModItemModelProvider extends $ItemModelProvider {
static readonly "BLOCK_FOLDER": string
static readonly "ITEM_FOLDER": string
readonly "generatedModels": $Map<($ResourceLocation), (T)>
readonly "existingFileHelper": $ExistingFileHelper

constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type, arg3: $BlockStateProvider$Type)

public "handheldItem"(arg0: $ResourceLocation$Type): $ItemModelBuilder
public "handheldItem"(arg0: $Item$Type): $ItemModelBuilder
public "name"(arg0: $Item$Type): string
public "key"(arg0: $Item$Type): $ResourceLocation
public "spawnEgg"(arg0: $Item$Type): $ItemModelBuilder
public "spawnEgg"(arg0: $ResourceLocation$Type): $ItemModelBuilder
public "basicItem"(arg0: $Item$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "basicItem"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "basicItem"(arg0: $ResourceLocation$Type, arg1: $Item$Type): $ItemModelBuilder
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "builtInItem"(arg0: $Item$Type, arg1: $Block$Type): $ItemModelBuilder
public "builtInItem"(arg0: $Item$Type, arg1: $Block$Type, arg2: $ResourceLocation$Type): $ItemModelBuilder
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModItemModelProvider$Type = ($ModItemModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModItemModelProvider_ = $ModItemModelProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientTickEvents$Start" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientTickEvents$Start {

 "onStartClientTick"(arg0: $Minecraft$Type): void

(arg0: $Minecraft$Type): void
}

export namespace $ClientTickEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickEvents$Start$Type = ($ClientTickEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickEvents$Start_ = $ClientTickEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Breathe" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$DefaultedInt, $DefaultedInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedInt"

export interface $LivingEvents$Breathe {

 "onLivingBreathe"(arg0: $LivingEntity$Type, arg1: $DefaultedInt$Type, arg2: boolean, arg3: boolean): $EventResult

(arg0: $LivingEntity$Type, arg1: $DefaultedInt$Type, arg2: boolean, arg3: boolean): $EventResult
}

export namespace $LivingEvents$Breathe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEvents$Breathe$Type = ($LivingEvents$Breathe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEvents$Breathe_ = $LivingEvents$Breathe$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityKey" {
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $CapabilityKey<C extends $CapabilityComponent> {

 "get"<V>(arg0: V): C
 "getId"(): $ResourceLocation
 "getComponentClass"(): $Class<(C)>
 "isProvidedBy"<V>(arg0: V): boolean
 "maybeGet"<V>(arg0: V): $Optional<(C)>
 "orThrow"<V>(arg0: V): C
}

export namespace $CapabilityKey {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityKey$Type<C> = ($CapabilityKey<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityKey_<C> = $CapabilityKey$Type<(C)>;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext$LegacyFactory" {
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export interface $ForgeDataProviderContext$LegacyFactory extends $BiFunction<($GatherDataEvent), (string), ($DataProvider)> {

 "apply"(arg0: $GatherDataEvent$Type, arg1: string): $DataProvider
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $BiFunction<($GatherDataEvent), (string), (V)>

(arg0: $GatherDataEvent$Type, arg1: string): $DataProvider
}

export namespace $ForgeDataProviderContext$LegacyFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeDataProviderContext$LegacyFactory$Type = ($ForgeDataProviderContext$LegacyFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeDataProviderContext$LegacyFactory_ = $ForgeDataProviderContext$LegacyFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractLootProvider" {
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$LootTableProvider, $LootTableProvider$Type} from "packages/net/minecraft/data/loot/$LootTableProvider"
import {$LootTableSubProvider, $LootTableSubProvider$Type} from "packages/net/minecraft/data/loot/$LootTableSubProvider"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"

export class $AbstractLootProvider {

constructor()

public static "createProvider"(arg0: $PackOutput$Type, arg1: $LootTableSubProvider$Type, arg2: $LootContextParamSet$Type): $LootTableProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractLootProvider$Type = ($AbstractLootProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractLootProvider_ = $AbstractLootProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/$AbstractLootProvider" {
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$LootTableProvider, $LootTableProvider$Type} from "packages/net/minecraft/data/loot/$LootTableProvider"
import {$LootTableSubProvider, $LootTableSubProvider$Type} from "packages/net/minecraft/data/loot/$LootTableSubProvider"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"

export class $AbstractLootProvider {

constructor()

public static "createProvider"(arg0: $PackOutput$Type, arg1: $LootTableSubProvider$Type, arg2: $LootContextParamSet$Type): $LootTableProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractLootProvider$Type = ($AbstractLootProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractLootProvider_ = $AbstractLootProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Watch" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerChunkEvents$Watch {

 "onChunkWatch"(arg0: $ServerPlayer$Type, arg1: $LevelChunk$Type, arg2: $ServerLevel$Type): void

(arg0: $ServerPlayer$Type, arg1: $LevelChunk$Type, arg2: $ServerLevel$Type): void
}

export namespace $ServerChunkEvents$Watch {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerChunkEvents$Watch$Type = ($ServerChunkEvents$Watch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerChunkEvents$Watch_ = $ServerChunkEvents$Watch$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeModContext" {
import {$RegistryManager, $RegistryManager$Type} from "packages/fuzs/puzzleslib/api/init/v3/$RegistryManager"
import {$RegistryManager as $RegistryManager$0, $RegistryManager$Type as $RegistryManager$0$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryManager"
import {$NetworkHandlerV3$Builder, $NetworkHandlerV3$Builder$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder"
import {$NetworkHandlerV2, $NetworkHandlerV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$NetworkHandlerV2"
import {$ConfigHolder$Builder, $ConfigHolder$Builder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder"
import {$CapabilityController, $CapabilityController$Type} from "packages/fuzs/puzzleslib/api/capability/v2/$CapabilityController"
import {$ModContext, $ModContext$Type} from "packages/fuzs/puzzleslib/impl/core/$ModContext"

export class $ForgeModContext extends $ModContext {

constructor(arg0: string)

public "getRegistryManagerV3"(): $RegistryManager
public "getCapabilityController"(): $CapabilityController
public "getRegistryManagerV2"(): $RegistryManager$0
public "getNetworkHandlerV3$Builder"(arg0: string): $NetworkHandlerV3$Builder
public "getNetworkHandlerV2"(arg0: string, arg1: boolean, arg2: boolean): $NetworkHandlerV2
public "getConfigHolder$Builder"(): $ConfigHolder$Builder
get "registryManagerV3"(): $RegistryManager
get "capabilityController"(): $CapabilityController
get "registryManagerV2"(): $RegistryManager$0
get "configHolder$Builder"(): $ConfigHolder$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModContext$Type = ($ForgeModContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModContext_ = $ForgeModContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenOpeningCallback" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$DefaultedValue, $DefaultedValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue"

export interface $ScreenOpeningCallback {

 "onScreenOpening"(arg0: $Screen$Type, arg1: $DefaultedValue$Type<($Screen$Type)>): $EventResult

(arg0: $Screen$Type, arg1: $DefaultedValue$Type<($Screen$Type)>): $EventResult
}

export namespace $ScreenOpeningCallback {
const EVENT: $EventInvoker<($ScreenOpeningCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenOpeningCallback$Type = ($ScreenOpeningCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenOpeningCallback_ = $ScreenOpeningCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$ClientTooltipComponentsContextForgeImpl" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$ClientTooltipComponentsContext, $ClientTooltipComponentsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ClientTooltipComponentsContext"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ClientTooltipComponentsContextForgeImpl extends $Record implements $ClientTooltipComponentsContext {

constructor(context: $ClientTooltipComponentsContext$Type)

public "context"(): $ClientTooltipComponentsContext
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerClientTooltipComponent"<T extends $TooltipComponent>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(any), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipComponentsContextForgeImpl$Type = ($ClientTooltipComponentsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipComponentsContextForgeImpl_ = $ClientTooltipComponentsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext$Factory" {
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DataProviderContext, $DataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext"

export interface $DataProviderContext$Factory extends $Function<($DataProviderContext), ($DataProvider)> {

 "apply"(arg0: $DataProviderContext$Type): $DataProvider
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($DataProvider)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($DataProviderContext), (V)>

(arg0: $DataProviderContext$Type): $DataProvider
}

export namespace $DataProviderContext$Factory {
function identity<T>(): $Function<($DataProviderContext), ($DataProviderContext)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataProviderContext$Factory$Type = ($DataProviderContext$Factory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataProviderContext$Factory_ = $DataProviderContext$Factory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$Remove" {
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ServerEntityLevelEvents$Remove {

 "onEntityRemove"(arg0: $Entity$Type, arg1: $ServerLevel$Type): void

(arg0: $Entity$Type, arg1: $ServerLevel$Type): void
}

export namespace $ServerEntityLevelEvents$Remove {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityLevelEvents$Remove$Type = ($ServerEntityLevelEvents$Remove);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityLevelEvents$Remove_ = $ServerEntityLevelEvents$Remove$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$LivingEntityRenderLayersContext" {
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingEntityRenderLayersContext {

 "registerRenderLayer"<E extends $LivingEntity, T extends E, M extends $EntityModel<(T)>>(arg0: $EntityType$Type<(E)>, arg1: $BiFunction$Type<($RenderLayerParent$Type<(T), (M)>), ($EntityRendererProvider$Context$Type), ($RenderLayer$Type<(T), (M)>)>): void

(arg0: $EntityType$Type<(E)>, arg1: $BiFunction$Type<($RenderLayerParent$Type<(T), (M)>), ($EntityRendererProvider$Context$Type), ($RenderLayer$Type<(T), (M)>)>): void
}

export namespace $LivingEntityRenderLayersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEntityRenderLayersContext$Type = ($LivingEntityRenderLayersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEntityRenderLayersContext_ = $LivingEntityRenderLayersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/$PotentialSpawnsList" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $PotentialSpawnsList<E> extends $AbstractList<(E)> {

constructor(arg0: $Supplier$Type<($List$Type<(E)>)>, arg1: $Predicate$Type<(E)>, arg2: $Predicate$Type<(E)>)

public "add"(arg0: integer, arg1: E): void
public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "remove"(arg0: integer): E
public "get"(arg0: integer): E
public "size"(): integer
public "set"(arg0: integer, arg1: E): E
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotentialSpawnsList$Type<E> = ($PotentialSpawnsList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotentialSpawnsList_<E> = $PotentialSpawnsList$Type<(E)>;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$RegistryManagerV2Impl" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$RegistryReference, $RegistryReference$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryReference"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ModLoader, $ModLoader$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoader"
import {$RegistryManager, $RegistryManager$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryManager"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$MenuType$MenuSupplier, $MenuType$MenuSupplier$Type} from "packages/net/minecraft/world/inventory/$MenuType$MenuSupplier"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SimpleParticleType, $SimpleParticleType$Type} from "packages/net/minecraft/core/particles/$SimpleParticleType"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$PoiTypeBuilder, $PoiTypeBuilder$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$PoiTypeBuilder"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$BlockEntityType$Builder, $BlockEntityType$Builder$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$Builder"
import {$ExtendedMenuSupplier, $ExtendedMenuSupplier$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $RegistryManagerV2Impl implements $RegistryManager {


public "register"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: $Supplier$Type<(T)>): $RegistryReference<(T)>
public "makeKey"(arg0: string): $ResourceLocation
public "whenOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
public static "instant"(arg0: string): $RegistryManager
public "registerBlockEntityType"<T extends $BlockEntity>(arg0: string, arg1: $Supplier$Type<($BlockEntityType$Builder$Type<(T)>)>): $RegistryReference<($BlockEntityType<(T)>)>
public "registerEnchantment"(arg0: string, arg1: $Supplier$Type<($Enchantment$Type)>): $RegistryReference<($Enchantment)>
public "registerBlock"(arg0: string, arg1: $Supplier$Type<($Block$Type)>): $RegistryReference<($Block)>
public "registerRecipeType"<T extends $Recipe<(any)>>(arg0: string): $RegistryReference<($RecipeType<(T)>)>
public "registerMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($MenuType$MenuSupplier$Type<(T)>)>): $RegistryReference<($MenuType<(T)>)>
public "registerGameEvent"(arg0: string, arg1: integer): $RegistryReference<($GameEvent)>
public "whenNotOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
public "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($Block$Type)>)>): $RegistryReference<($PoiType)>
public "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($BlockState$Type)>)>, arg2: integer, arg3: integer): $RegistryReference<($PoiType)>
public "registerDamageType"(arg0: string): $ResourceKey<($DamageType)>
public "registerBiomeTag"(arg0: string): $TagKey<($Biome)>
public "registerBlockTag"(arg0: string): $TagKey<($Block)>
public "registerTag"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $TagKey<(T)>
public "registerItemTag"(arg0: string): $TagKey<($Item)>
public "registerFluid"(arg0: string, arg1: $Supplier$Type<($Fluid$Type)>): $RegistryReference<($Fluid)>
public "registerItem"(arg0: string, arg1: $Supplier$Type<($Item$Type)>): $RegistryReference<($Item)>
public "registerEntityType"<T extends $Entity>(arg0: string, arg1: $Supplier$Type<($EntityType$Builder$Type<(T)>)>): $RegistryReference<($EntityType<(T)>)>
public "registerMobEffect"(arg0: string, arg1: $Supplier$Type<($MobEffect$Type)>): $RegistryReference<($MobEffect)>
public "registerPotion"(arg0: string, arg1: $Supplier$Type<($Potion$Type)>): $RegistryReference<($Potion)>
public "registerSoundEvent"(arg0: string): $RegistryReference<($SoundEvent)>
public "registerBlockItem"(arg0: $RegistryReference$Type<($Block$Type)>, arg1: $Item$Properties$Type): $RegistryReference<($Item)>
public "registerBlockItem"(arg0: $RegistryReference$Type<($Block$Type)>): $RegistryReference<($Item)>
/**
 * 
 * @deprecated
 */
public "registerPoiTypeBuilder"(arg0: string, arg1: $Supplier$Type<($PoiTypeBuilder$Type)>): $RegistryReference<($PoiType)>
public "registerSpawnEggItem"(arg0: $RegistryReference$Type<(any)>, arg1: integer, arg2: integer, arg3: $Item$Properties$Type): $RegistryReference<($Item)>
public "registerSpawnEggItem"(arg0: $RegistryReference$Type<(any)>, arg1: integer, arg2: integer): $RegistryReference<($Item)>
public "registerExtendedMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($ExtendedMenuSupplier$Type<(T)>)>): $RegistryReference<($MenuType<(T)>)>
public "registerResourceKey"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $ResourceKey<(T)>
public "registerEnchantmentTag"(arg0: string): $TagKey<($Enchantment)>
public "registerDamageTypeTag"(arg0: string): $TagKey<($DamageType)>
public "registerGameEventTag"(arg0: string): $TagKey<($GameEvent)>
public "registerEntityTypeTag"(arg0: string): $TagKey<($EntityType<(any)>)>
public "registerParticleType"(arg0: string): $RegistryReference<($SimpleParticleType)>
public "placeholder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $RegistryReference<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryManagerV2Impl$Type = ($RegistryManagerV2Impl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryManagerV2Impl_ = $RegistryManagerV2Impl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/core/$EventInvokerImpl" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$EventInvokerImpl$EventInvokerLike, $EventInvokerImpl$EventInvokerLike$Type} from "packages/fuzs/puzzleslib/impl/event/core/$EventInvokerImpl$EventInvokerLike"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $EventInvokerImpl {


public static "initialize"(): void
public static "register"<T>(arg0: $Class$Type<(T)>, arg1: $EventInvokerImpl$EventInvokerLike$Type<(T)>, arg2: boolean): void
public static "softLookup"<T>(arg0: $Class$Type<(T)>, arg1: any): $EventInvoker<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventInvokerImpl$Type = ($EventInvokerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventInvokerImpl_ = $EventInvokerImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$ForgeRegistryForgeAccessor" {
import {$IForgeRegistry$AddCallback, $IForgeRegistry$AddCallback$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry$AddCallback"

export interface $ForgeRegistryForgeAccessor<V> {

 "puzzleslib$getAdd"(): $IForgeRegistry$AddCallback<(V)>
 "puzzleslib$setAdd"(arg0: $IForgeRegistry$AddCallback$Type<(V)>): void
}

export namespace $ForgeRegistryForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeRegistryForgeAccessor$Type<V> = ($ForgeRegistryForgeAccessor<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeRegistryForgeAccessor_<V> = $ForgeRegistryForgeAccessor$Type<(V)>;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeServerProxy" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"
import {$ForgeProxy, $ForgeProxy$Type} from "packages/fuzs/puzzleslib/impl/core/$ForgeProxy"

export class $ForgeServerProxy implements $ForgeProxy {

constructor()

public "registerServerReceiverV2"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "registerClientReceiverV2"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "hasShiftDown"(): boolean
public "getClientLevel"(): $Level
public "hasControlDown"(): boolean
public "hasAltDown"(): boolean
public "getClientPlayer"(): $Player
public "getClientPacketListener"(): $ClientPacketListener
public "getKeyMappingComponent"(arg0: string): $Component
/**
 * 
 * @deprecated
 */
public "getGameServer"(): $MinecraftServer
get "clientLevel"(): $Level
get "clientPlayer"(): $Player
get "clientPacketListener"(): $ClientPacketListener
get "gameServer"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeServerProxy$Type = ($ForgeServerProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeServerProxy_ = $ForgeServerProxy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $MutableBoolean extends $BooleanSupplier {

 "accept"(arg0: boolean): void
 "mapBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
 "getAsBoolean"(): boolean
}

export namespace $MutableBoolean {
function fromValue(arg0: boolean): $MutableBoolean
function fromEvent(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>): $MutableBoolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableBoolean$Type = ($MutableBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableBoolean_ = $MutableBoolean$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$TierImpl" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"

export class $TierImpl extends $Record implements $Tier {

constructor(miningLevel: integer, itemDurability: integer, miningSpeed: float, attackDamage: float, enchantability: integer, repairIngredient: $Supplier$Type<($Ingredient$Type)>)

public "miningSpeed"(): float
public "miningLevel"(): integer
public "itemDurability"(): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "attackDamage"(): float
public "repairIngredient"(): $Supplier<($Ingredient)>
public "enchantability"(): integer
public "getEnchantmentValue"(): integer
public "getSpeed"(): float
public "getUses"(): integer
public "getAttackDamageBonus"(): float
public "getLevel"(): integer
public "getRepairIngredient"(): $Ingredient
public "getTag"(): $TagKey<($Block)>
get "enchantmentValue"(): integer
get "speed"(): float
get "uses"(): integer
get "attackDamageBonus"(): float
get "level"(): integer
get "tag"(): $TagKey<($Block)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TierImpl$Type = ($TierImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TierImpl_ = $TierImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStopping" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerLifecycleEvents$ServerStopping {

 "onServerStopping"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerLifecycleEvents$ServerStopping {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLifecycleEvents$ServerStopping$Type = ($ServerLifecycleEvents$ServerStopping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLifecycleEvents$ServerStopping_ = $ServerLifecycleEvents$ServerStopping$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$CreativeModeTabConfiguratorImpl" {
import {$CreativeModeTab$DisplayItemsGenerator, $CreativeModeTab$DisplayItemsGenerator$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$DisplayItemsGenerator"
import {$CreativeModeTabConfigurator, $CreativeModeTabConfigurator$Type} from "packages/fuzs/puzzleslib/api/item/v2/$CreativeModeTabConfigurator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CreativeModeTab$Builder, $CreativeModeTab$Builder$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Builder"

export class $CreativeModeTabConfiguratorImpl implements $CreativeModeTabConfigurator {

constructor(arg0: $ResourceLocation$Type)

public "configure"(arg0: $CreativeModeTab$Builder$Type): void
public "getIdentifier"(): $ResourceLocation
public "displayItems"(arg0: $CreativeModeTab$DisplayItemsGenerator$Type): $CreativeModeTabConfigurator
public "isHasSearchBar"(): boolean
public "getIcons"(): $Supplier<(($ItemStack)[])>
public "withSearchBar"(): $CreativeModeTabConfigurator
public "icon"(arg0: $Supplier$Type<($ItemStack$Type)>): $CreativeModeTabConfigurator
public "icons"(arg0: $Supplier$Type<(($ItemStack$Type)[])>): $CreativeModeTabConfigurator
public "appendEnchantmentsAndPotions"(): $CreativeModeTabConfigurator
public static "from"(arg0: $ResourceLocation$Type): $CreativeModeTabConfigurator
public static "from"(arg0: string, arg1: string): $CreativeModeTabConfigurator
public static "from"(arg0: string): $CreativeModeTabConfigurator
public static "from"(arg0: string, arg1: $Supplier$Type<($ItemStack$Type)>): $CreativeModeTabConfigurator
get "identifier"(): $ResourceLocation
get "hasSearchBar"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeModeTabConfiguratorImpl$Type = ($CreativeModeTabConfiguratorImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeModeTabConfiguratorImpl_ = $CreativeModeTabConfiguratorImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ModContext" {
import {$RegistryManager, $RegistryManager$Type} from "packages/fuzs/puzzleslib/api/init/v3/$RegistryManager"
import {$RegistryManager as $RegistryManager$0, $RegistryManager$Type as $RegistryManager$0$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryManager"
import {$NetworkHandlerV3$Builder, $NetworkHandlerV3$Builder$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CapabilityController, $CapabilityController$Type} from "packages/fuzs/puzzleslib/api/capability/v2/$CapabilityController"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$NetworkHandlerV2, $NetworkHandlerV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$NetworkHandlerV2"
import {$ConfigHolder$Builder, $ConfigHolder$Builder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder"
import {$BaseModConstructor, $BaseModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$BaseModConstructor"

export class $ModContext {


public static "get"(arg0: string): $ModContext
public static "isPresentServerside"(arg0: string): boolean
public "getFlagsToHandle"(arg0: $Set$Type<($ContentRegistrationFlags$Type)>): $Set<($ContentRegistrationFlags)>
public static "registerHandlers"(): void
public static "acceptServersideMods"(arg0: $Collection$Type<(string)>): void
public "getRegistryManagerV3"(): $RegistryManager
public static "getCapabilityControllers"(): $Stream<($CapabilityController)>
public "getCapabilityController"(): $CapabilityController
public "getRegistryManagerV2"(): $RegistryManager$0
public "getNetworkHandlerV3$Builder"(arg0: string): $NetworkHandlerV3$Builder
public "beforeModConstruction"(): void
public "scheduleClientModConstruction"(arg0: $ResourceLocation$Type, arg1: $Runnable$Type): void
public "afterModConstruction"(arg0: $ResourceLocation$Type): void
public static "getPairingIdentifier"(arg0: string, arg1: $BaseModConstructor$Type): $ResourceLocation
public "getNetworkHandlerV2"(arg0: string, arg1: boolean, arg2: boolean): $NetworkHandlerV2
public "getConfigHolder$Builder"(): $ConfigHolder$Builder
get "registryManagerV3"(): $RegistryManager
get "capabilityControllers"(): $Stream<($CapabilityController)>
get "capabilityController"(): $CapabilityController
get "registryManagerV2"(): $RegistryManager$0
get "configHolder$Builder"(): $ConfigHolder$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModContext$Type = ($ModContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModContext_ = $ModContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$EntityRidingEvents" {
import {$EntityRidingEvents$Start, $EntityRidingEvents$Start$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/$EntityRidingEvents$Start"
import {$EntityRidingEvents$Stop, $EntityRidingEvents$Stop$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/$EntityRidingEvents$Stop"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $EntityRidingEvents {
static readonly "START": $EventInvoker<($EntityRidingEvents$Start)>
static readonly "STOP": $EventInvoker<($EntityRidingEvents$Stop)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRidingEvents$Type = ($EntityRidingEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRidingEvents_ = $EntityRidingEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$BiomeModificationContext" {
import {$SpecialEffectsContext, $SpecialEffectsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$SpecialEffectsContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GenerationSettingsContext, $GenerationSettingsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$GenerationSettingsContext"
import {$MobSpawnSettingsContext, $MobSpawnSettingsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$MobSpawnSettingsContext"
import {$ClimateSettingsContext, $ClimateSettingsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$ClimateSettingsContext"

export class $BiomeModificationContext extends $Record {

constructor(climateSettings: $ClimateSettingsContext$Type, specialEffects: $SpecialEffectsContext$Type, generationSettings: $GenerationSettingsContext$Type, mobSpawnSettings: $MobSpawnSettingsContext$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "mobSpawnSettings"(): $MobSpawnSettingsContext
public "generationSettings"(): $GenerationSettingsContext
public "climateSettings"(): $ClimateSettingsContext
public "specialEffects"(): $SpecialEffectsContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModificationContext$Type = ($BiomeModificationContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModificationContext_ = $BiomeModificationContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelTickEvents$End" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientLevelTickEvents$End {

 "onEndLevelTick"(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void

(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void
}

export namespace $ClientLevelTickEvents$End {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelTickEvents$End$Type = ($ClientLevelTickEvents$End);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelTickEvents$End_ = $ClientLevelTickEvents$End$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/client/$AbstractSoundDefinitionProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ForgeDataProviderContext, $ForgeDataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$SoundDefinitionsProvider, $SoundDefinitionsProvider$Type} from "packages/net/minecraftforge/common/data/$SoundDefinitionsProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $AbstractSoundDefinitionProvider extends $SoundDefinitionsProvider {

constructor(arg0: $ForgeDataProviderContext$Type)
constructor(arg0: string, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type)

public "registerSounds"(): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSoundDefinitionProvider$Type = ($AbstractSoundDefinitionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSoundDefinitionProvider_ = $AbstractSoundDefinitionProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingChangeTargetCallback" {
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$DefaultedValue, $DefaultedValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue"

export interface $LivingChangeTargetCallback {

 "onLivingChangeTarget"(arg0: $LivingEntity$Type, arg1: $DefaultedValue$Type<($LivingEntity$Type)>): $EventResult

(arg0: $LivingEntity$Type, arg1: $DefaultedValue$Type<($LivingEntity$Type)>): $EventResult
}

export namespace $LivingChangeTargetCallback {
const EVENT: $EventInvoker<($LivingChangeTargetCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingChangeTargetCallback$Type = ($LivingChangeTargetCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingChangeTargetCallback_ = $LivingChangeTargetCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesCreateContext" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"

export interface $EntityAttributesCreateContext {

 "registerEntityAttributes"(arg0: $EntityType$Type<(any)>, arg1: $AttributeSupplier$Builder$Type): void

(arg0: $EntityType$Type<(any)>, arg1: $AttributeSupplier$Builder$Type): void
}

export namespace $EntityAttributesCreateContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributesCreateContext$Type = ($EntityAttributesCreateContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributesCreateContext_ = $EntityAttributesCreateContext$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$PotionBrewingForgeAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PotionBrewingForgeAccessor {

}

export namespace $PotionBrewingForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionBrewingForgeAccessor$Type = ($PotionBrewingForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionBrewingForgeAccessor_ = $PotionBrewingForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents" {
import {$ClientPlayerEvents$Copy, $ClientPlayerEvents$Copy$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents$Copy"
import {$ClientPlayerEvents$LoggedOut, $ClientPlayerEvents$LoggedOut$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents$LoggedOut"
import {$ClientPlayerEvents$LoggedIn, $ClientPlayerEvents$LoggedIn$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents$LoggedIn"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ClientPlayerEvents {
static readonly "LOGGED_IN": $EventInvoker<($ClientPlayerEvents$LoggedIn)>
static readonly "LOGGED_OUT": $EventInvoker<($ClientPlayerEvents$LoggedOut)>
static readonly "COPY": $EventInvoker<($ClientPlayerEvents$Copy)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvents$Type = ($ClientPlayerEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvents_ = $ClientPlayerEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$ProjectileImpactCallback" {
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Projectile, $Projectile$Type} from "packages/net/minecraft/world/entity/projectile/$Projectile"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ProjectileImpactCallback {

 "onProjectileImpact"(arg0: $Projectile$Type, arg1: $HitResult$Type): $EventResult

(arg0: $Projectile$Type, arg1: $HitResult$Type): $EventResult
}

export namespace $ProjectileImpactCallback {
const EVENT: $EventInvoker<($ProjectileImpactCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProjectileImpactCallback$Type = ($ProjectileImpactCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProjectileImpactCallback_ = $ProjectileImpactCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$ItemModelPropertiesContextForgeImpl" {
import {$ClampedItemPropertyFunction, $ClampedItemPropertyFunction$Type} from "packages/net/minecraft/client/renderer/item/$ClampedItemPropertyFunction"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemModelPropertiesContext, $ItemModelPropertiesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemModelPropertiesContext"

export class $ItemModelPropertiesContextForgeImpl implements $ItemModelPropertiesContext {

constructor()

public "registerGlobalProperty"(arg0: $ResourceLocation$Type, arg1: $ClampedItemPropertyFunction$Type): void
public "registerItemProperty"(arg0: $ResourceLocation$Type, arg1: $ClampedItemPropertyFunction$Type, ...arg2: ($ItemLike$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelPropertiesContextForgeImpl$Type = ($ItemModelPropertiesContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelPropertiesContextForgeImpl_ = $ItemModelPropertiesContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$SkullRenderersContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$SkullModelBase, $SkullModelBase$Type} from "packages/net/minecraft/client/model/$SkullModelBase"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$SkullRenderersContext, $SkullRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SkullRenderersContext"
import {$SkullRenderersFactory, $SkullRenderersFactory$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$SkullRenderersFactory"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$SkullBlock$Type, $SkullBlock$Type$Type} from "packages/net/minecraft/world/level/block/$SkullBlock$Type"

export class $SkullRenderersContextForgeImpl extends $Record implements $SkullRenderersContext {

constructor(entityModelSet: $EntityModelSet$Type, consumer: $BiConsumer$Type<($SkullBlock$Type$Type), ($SkullModelBase$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $BiConsumer<($SkullBlock$Type), ($SkullModelBase)>
public "registerSkullRenderer"(arg0: $SkullRenderersFactory$Type): void
public "entityModelSet"(): $EntityModelSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkullRenderersContextForgeImpl$Type = ($SkullRenderersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkullRenderersContextForgeImpl_ = $SkullRenderersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/$ForgeClientAbstractions" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$SearchRegistry, $SearchRegistry$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ClientTooltipComponent, $ClientTooltipComponent$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipComponent"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ClientAbstractions, $ClientAbstractions$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/$ClientAbstractions"
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ModelManager, $ModelManager$Type} from "packages/net/minecraft/client/resources/model/$ModelManager"

export class $ForgeClientAbstractions implements $ClientAbstractions {

constructor()

public "getPartialTick"(): float
public "registerRenderType"(arg0: $Block$Type, arg1: $RenderType$Type): void
public "registerRenderType"(arg0: $Fluid$Type, arg1: $RenderType$Type): void
public "getRenderType"(arg0: $Block$Type): $RenderType
public "getBakedModel"(arg0: $ResourceLocation$Type): $BakedModel
public "getSearchRegistry"(): $SearchRegistry
public "createImageComponent"(arg0: $TooltipComponent$Type): $ClientTooltipComponent
public "isKeyActiveAndMatches"(arg0: $KeyMapping$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "getPartialTick"(arg0: $Minecraft$Type): float
public "getRenderType"(arg0: $Fluid$Type): $RenderType
/**
 * 
 * @deprecated
 */
public "getBakedModel"(arg0: $ModelManager$Type, arg1: $ResourceLocation$Type): $BakedModel
/**
 * 
 * @deprecated
 */
public "getSearchRegistry"(arg0: $Minecraft$Type): $SearchRegistry
get "partialTick"(): float
get "searchRegistry"(): $SearchRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientAbstractions$Type = ($ForgeClientAbstractions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientAbstractions_ = $ForgeClientAbstractions$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$ForgeGameRulesFactory" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$GameRules$Value, $GameRules$Value$Type} from "packages/net/minecraft/world/level/$GameRules$Value"
import {$GameRules$Type, $GameRules$Type$Type} from "packages/net/minecraft/world/level/$GameRules$Type"
import {$GameRulesFactory, $GameRulesFactory$Type} from "packages/fuzs/puzzleslib/api/init/v2/$GameRulesFactory"
import {$GameRules$IntegerValue, $GameRules$IntegerValue$Type} from "packages/net/minecraft/world/level/$GameRules$IntegerValue"
import {$GameRules$Key, $GameRules$Key$Type} from "packages/net/minecraft/world/level/$GameRules$Key"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$GameRules$BooleanValue, $GameRules$BooleanValue$Type} from "packages/net/minecraft/world/level/$GameRules$BooleanValue"
import {$GameRules$Category, $GameRules$Category$Type} from "packages/net/minecraft/world/level/$GameRules$Category"

export class $ForgeGameRulesFactory implements $GameRulesFactory {

constructor()

public "register"<T extends $GameRules$Value<(T)>>(arg0: string, arg1: $GameRules$Category$Type, arg2: $GameRules$Type$Type<(T)>): $GameRules$Key<(T)>
public "createIntRule"(arg0: integer, arg1: integer, arg2: integer, arg3: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$IntegerValue$Type)>): $GameRules$Type<($GameRules$IntegerValue)>
public "createBooleanRule"(arg0: boolean, arg1: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$BooleanValue$Type)>): $GameRules$Type<($GameRules$BooleanValue)>
public "createIntRule"(arg0: integer, arg1: integer): $GameRules$Type<($GameRules$IntegerValue)>
public "createIntRule"(arg0: integer, arg1: integer, arg2: integer): $GameRules$Type<($GameRules$IntegerValue)>
public "createIntRule"(arg0: integer, arg1: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$IntegerValue$Type)>): $GameRules$Type<($GameRules$IntegerValue)>
public "createIntRule"(arg0: integer, arg1: integer, arg2: $BiConsumer$Type<($MinecraftServer$Type), ($GameRules$IntegerValue$Type)>): $GameRules$Type<($GameRules$IntegerValue)>
public "createIntRule"(arg0: integer): $GameRules$Type<($GameRules$IntegerValue)>
public "createBooleanRule"(arg0: boolean): $GameRules$Type<($GameRules$BooleanValue)>
public "registerIntRule"(arg0: string, arg1: $GameRules$Category$Type, arg2: integer): $GameRules$Key<($GameRules$IntegerValue)>
public "registerBooleanRule"(arg0: string, arg1: $GameRules$Category$Type, arg2: boolean): $GameRules$Key<($GameRules$BooleanValue)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeGameRulesFactory$Type = ($ForgeGameRulesFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeGameRulesFactory_ = $ForgeGameRulesFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$GrindstoneEvents$Update" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"

export interface $GrindstoneEvents$Update {

 "onGrindstoneUpdate"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $MutableValue$Type<($ItemStack$Type)>, arg3: $MutableInt$Type, arg4: $Player$Type): $EventResult

(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $MutableValue$Type<($ItemStack$Type)>, arg3: $MutableInt$Type, arg4: $Player$Type): $EventResult
}

export namespace $GrindstoneEvents$Update {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrindstoneEvents$Update$Type = ($GrindstoneEvents$Update);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrindstoneEvents$Update_ = $GrindstoneEvents$Update$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/$ForgeAttributeModifiersMultimap" {
import {$ForwardingMultimap, $ForwardingMultimap$Type} from "packages/com/google/common/collect/$ForwardingMultimap"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ForgeAttributeModifiersMultimap extends $ForwardingMultimap<($Attribute), ($AttributeModifier)> {

constructor(arg0: $Supplier$Type<($Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>)>, arg1: $BiPredicate$Type<($Attribute$Type), ($AttributeModifier$Type)>, arg2: $BiPredicate$Type<($Attribute$Type), ($AttributeModifier$Type)>, arg3: $Function$Type<($Attribute$Type), ($Collection$Type<($AttributeModifier$Type)>)>, arg4: $Runnable$Type)

public "remove"(arg0: any, arg1: any): boolean
public "put"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type): boolean
public "clear"(): void
public "putAll"(arg0: $Multimap$Type<(any), (any)>): boolean
public "putAll"(arg0: $Attribute$Type, arg1: $Iterable$Type<(any)>): boolean
public "removeAll"(arg0: any): $Collection<($AttributeModifier)>
public "replaceValues"(arg0: $Attribute$Type, arg1: $Iterable$Type<(any)>): $Collection<($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeAttributeModifiersMultimap$Type = ($ForgeAttributeModifiersMultimap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeAttributeModifiersMultimap_ = $ForgeAttributeModifiersMultimap$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$DynamicBakingCompletedContextForgeImpl" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$DynamicBakingCompletedContext, $DynamicBakingCompletedContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicBakingCompletedContext"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$ModelManager, $ModelManager$Type} from "packages/net/minecraft/client/resources/model/$ModelManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DynamicBakingCompletedContextForgeImpl extends $Record implements $DynamicBakingCompletedContext {

constructor(modelManager: $ModelManager$Type, models: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, modelBakery: $ModelBakery$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "models"(): $Map<($ResourceLocation), ($BakedModel)>
public "modelManager"(): $ModelManager
public "modelBakery"(): $ModelBakery
public "getModel"(arg0: $ResourceLocation$Type): $BakedModel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicBakingCompletedContextForgeImpl$Type = ($DynamicBakingCompletedContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicBakingCompletedContextForgeImpl_ = $DynamicBakingCompletedContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$AfterModelLoading" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ModelManager, $ModelManager$Type} from "packages/net/minecraft/client/resources/model/$ModelManager"

export interface $ModelEvents$AfterModelLoading {

 "onAfterModelLoading"(arg0: $Supplier$Type<($ModelManager$Type)>): void

(arg0: $Supplier$Type<($ModelManager$Type)>): void
}

export namespace $ModelEvents$AfterModelLoading {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$AfterModelLoading$Type = ($ModelEvents$AfterModelLoading);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents$AfterModelLoading_ = $ModelEvents$AfterModelLoading$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseItemV2" {
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export interface $PlayerInteractEvents$UseItemV2 {

 "onUseItem"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type): $EventResultHolder<($InteractionResult)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type): $EventResultHolder<($InteractionResult)>
}

export namespace $PlayerInteractEvents$UseItemV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$UseItemV2$Type = ($PlayerInteractEvents$UseItemV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$UseItemV2_ = $PlayerInteractEvents$UseItemV2$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$GameRenderEvents$Before" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"

export interface $GameRenderEvents$Before {

 "onBeforeGameRender"(arg0: $Minecraft$Type, arg1: $GameRenderer$Type, arg2: float): void

(arg0: $Minecraft$Type, arg1: $GameRenderer$Type, arg2: float): void
}

export namespace $GameRenderEvents$Before {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameRenderEvents$Before$Type = ($GameRenderEvents$Before);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameRenderEvents$Before_ = $GameRenderEvents$Before$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/data/v2/$AbstractModelProvider" {
import {$BlockModelBuilder, $BlockModelBuilder$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$BlockModelBuilder"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ItemModelBuilder, $ItemModelBuilder$Type} from "packages/fuzs/puzzlesaccessapi/api/client/data/v2/$ItemModelBuilder"
import {$DataProviderContext, $DataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext"
import {$AbstractModelProvider$ItemOverride$Factory, $AbstractModelProvider$ItemOverride$Factory$Type} from "packages/fuzs/puzzleslib/api/client/data/v2/$AbstractModelProvider$ItemOverride$Factory"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelTemplate, $ModelTemplate$Type} from "packages/net/minecraft/data/models/model/$ModelTemplate"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ModelTemplate$JsonFactory, $ModelTemplate$JsonFactory$Type} from "packages/net/minecraft/data/models/model/$ModelTemplate$JsonFactory"

export class $AbstractModelProvider implements $DataProvider {
static readonly "BLOCK_PATH": string
static readonly "ITEM_PATH": string

constructor(arg0: $DataProviderContext$Type)
constructor(arg0: string, arg1: $PackOutput$Type)

public static "getName"(arg0: $Block$Type): string
public static "getName"(arg0: $Item$Type): string
public static "getLocation"(arg0: $Block$Type): $ResourceLocation
public static "getLocation"(arg0: $Item$Type): $ResourceLocation
public static "overrides"(arg0: $ModelTemplate$Type, ...arg1: ($AbstractModelProvider$ItemOverride$Factory$Type)[]): $ModelTemplate$JsonFactory
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "decorateItemModelLocation"(arg0: $ResourceLocation$Type): $ResourceLocation
public static "decorateBlockModelLocation"(arg0: $ResourceLocation$Type): $ResourceLocation
public "addBlockModels"(arg0: $BlockModelBuilder$Type): void
public "addItemModels"(arg0: $ItemModelBuilder$Type): void
public static "stripUntil"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
public static "getModelLocation"(arg0: $Item$Type): $ResourceLocation
public static "getModelLocation"(arg0: $Block$Type): $ResourceLocation
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModelProvider$Type = ($AbstractModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModelProvider_ = $AbstractModelProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Drown" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingEvents$Drown {

 "onLivingDrown"(arg0: $LivingEntity$Type, arg1: integer, arg2: boolean): $EventResult

(arg0: $LivingEntity$Type, arg1: integer, arg2: boolean): $EventResult
}

export namespace $LivingEvents$Drown {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEvents$Drown$Type = ($LivingEvents$Drown);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEvents$Drown_ = $LivingEvents$Drown$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/biome/$BiomeLoadingContextForge" {
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$BiomeLoadingContext, $BiomeLoadingContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingContext"
import {$LevelStem, $LevelStem$Type} from "packages/net/minecraft/world/level/dimension/$LevelStem"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$ConfiguredFeature, $ConfiguredFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$ConfiguredFeature"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $BiomeLoadingContextForge extends $Record implements $BiomeLoadingContext {

constructor(registryAccess: $RegistryAccess$Type, holder: $Holder$Type<($Biome$Type)>)
constructor(arg0: $Holder$Type<($Biome$Type)>)

public "getPlacedFeatureKey"(arg0: $PlacedFeature$Type): $Optional<($ResourceKey<($PlacedFeature)>)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "is"(arg0: $TagKey$Type<($Biome$Type)>): boolean
public "holder"(): $Holder<($Biome)>
public "getBiome"(): $Biome
public "getResourceKey"(): $ResourceKey<($Biome)>
public "registryAccess"(): $RegistryAccess
public "getStructureKey"(arg0: $Structure$Type): $Optional<($ResourceKey<($Structure)>)>
public "validForStructure"(arg0: $ResourceKey$Type<($Structure$Type)>): boolean
public "getFeatureKey"(arg0: $ConfiguredFeature$Type<(any), (any)>): $Optional<($ResourceKey<($ConfiguredFeature<(any), (any)>)>)>
public "canGenerateIn"(arg0: $ResourceKey$Type<($LevelStem$Type)>): boolean
public "is"(arg0: $Holder$Type<($Biome$Type)>): boolean
public "is"(arg0: $Biome$Type): boolean
public "is"(arg0: $ResourceKey$Type<($Biome$Type)>): boolean
public "hasFeature"(arg0: $ResourceKey$Type<($ConfiguredFeature$Type<(any), (any)>)>): boolean
public "hasPlacedFeature"(arg0: $ResourceKey$Type<($PlacedFeature$Type)>): boolean
get "biome"(): $Biome
get "resourceKey"(): $ResourceKey<($Biome)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeLoadingContextForge$Type = ($BiomeLoadingContextForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeLoadingContextForge_ = $BiomeLoadingContextForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerTickEvents$End" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerTickEvents$End {

 "onEndServerTick"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerTickEvents$End {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerTickEvents$End$Type = ($ServerTickEvents$End);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerTickEvents$End_ = $ServerTickEvents$End$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$FlammableBlocksContext" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $FlammableBlocksContext {

 "registerFlammable"(arg0: integer, arg1: integer, ...arg2: ($Block$Type)[]): void

(arg0: integer, arg1: integer, ...arg2: ($Block$Type)[]): void
}

export namespace $FlammableBlocksContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlammableBlocksContext$Type = ($FlammableBlocksContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlammableBlocksContext_ = $FlammableBlocksContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/$RegistryEntryAddedCallback" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $RegistryEntryAddedCallback<T> {

 "onRegistryEntryAdded"(arg0: $Registry$Type<(T)>, arg1: $ResourceLocation$Type, arg2: T, arg3: $BiConsumer$Type<($ResourceLocation$Type), ($Supplier$Type<(T)>)>): void

(arg0: $ResourceKey$Type<(any)>): $EventInvoker<($RegistryEntryAddedCallback<(T)>)>
}

export namespace $RegistryEntryAddedCallback {
function registryEntryAdded<T>(arg0: $ResourceKey$Type<(any)>): $EventInvoker<($RegistryEntryAddedCallback<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEntryAddedCallback$Type<T> = ($RegistryEntryAddedCallback<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEntryAddedCallback_<T> = $RegistryEntryAddedCallback$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents" {
import {$MobEffectEvents$Expire, $MobEffectEvents$Expire$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Expire"
import {$MobEffectEvents$Remove, $MobEffectEvents$Remove$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Remove"
import {$MobEffectEvents$Affects, $MobEffectEvents$Affects$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Affects"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$MobEffectEvents$Apply, $MobEffectEvents$Apply$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Apply"

export class $MobEffectEvents {
static readonly "AFFECTS": $EventInvoker<($MobEffectEvents$Affects)>
static readonly "APPLY": $EventInvoker<($MobEffectEvents$Apply)>
static readonly "REMOVE": $EventInvoker<($MobEffectEvents$Remove)>
static readonly "EXPIRE": $EventInvoker<($MobEffectEvents$Expire)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobEffectEvents$Type = ($MobEffectEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobEffectEvents_ = $MobEffectEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventMutableValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EventMutableValue<T> implements $MutableValue<(T)> {

constructor(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>)

public "get"(): T
public "accept"(arg0: T): void
public "map"(arg0: $UnaryOperator$Type<(T)>): void
public static "fromValue"<T>(arg0: T): $MutableValue<(T)>
public static "fromEvent"<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>): $MutableValue<(T)>
public "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventMutableValue$Type<T> = ($EventMutableValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventMutableValue_<T> = $EventMutableValue$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventDefaultedValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EventMutableValue, $EventMutableValue$Type} from "packages/fuzs/puzzleslib/impl/event/data/$EventMutableValue"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DefaultedValue, $DefaultedValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue"

export class $EventDefaultedValue<T> extends $EventMutableValue<(T)> implements $DefaultedValue<(T)> {

constructor(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>, arg2: $Supplier$Type<(T)>)

public "accept"(arg0: T): void
public "getAsOptional"(): $Optional<(T)>
public "getAsDefault"(): T
public static "fromValue"<T>(arg0: T): $DefaultedValue<(T)>
public static "fromEvent"<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>, arg2: $Supplier$Type<(T)>): $DefaultedValue<(T)>
public "mapDefault"(arg0: $UnaryOperator$Type<(T)>): void
public "applyDefault"(): void
public static "fromEvent"<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>): $MutableValue<(T)>
get "asOptional"(): $Optional<(T)>
get "asDefault"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventDefaultedValue$Type<T> = ($EventDefaultedValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventDefaultedValue_<T> = $EventDefaultedValue$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$AddReloadListenersContextForgeImpl" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$SimplePreparableReloadListener, $SimplePreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimplePreparableReloadListener"

export class $AddReloadListenersContextForgeImpl extends $Record implements $AddReloadListenersContext {

constructor(modId: string, consumer: $Consumer$Type<($PreparableReloadListener$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $Consumer<($PreparableReloadListener)>
public "registerReloadListener"(arg0: string, arg1: $PreparableReloadListener$Type): void
public "modId"(): string
public "registerReloadListener"(arg0: string, arg1: $ResourceManagerReloadListener$Type): void
public "registerReloadListener"<T>(arg0: string, arg1: $SimplePreparableReloadListener$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddReloadListenersContextForgeImpl$Type = ($AddReloadListenersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddReloadListenersContextForgeImpl_ = $AddReloadListenersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ValueMutableValue<T> implements $MutableValue<(T)> {

constructor(arg0: T)

public "get"(): T
public "accept"(arg0: T): void
public "map"(arg0: $UnaryOperator$Type<(T)>): void
public static "fromValue"<T>(arg0: T): $MutableValue<(T)>
public static "fromEvent"<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>): $MutableValue<(T)>
public "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueMutableValue$Type<T> = ($ValueMutableValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueMutableValue_<T> = $ValueMutableValue$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventMutableDouble" {
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$MutableDouble, $MutableDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble"

export class $EventMutableDouble implements $MutableDouble {

constructor(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type)

public "accept"(arg0: double): void
public "getAsDouble"(): double
public static "fromValue"(arg0: double): $MutableDouble
public static "fromEvent"(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type): $MutableDouble
public "mapDouble"(arg0: $DoubleUnaryOperator$Type): void
public "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
get "asDouble"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventMutableDouble$Type = ($EventMutableDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventMutableDouble_ = $EventMutableDouble$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/core/$EventPhaseImpl$Ordering" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $EventPhaseImpl$Ordering {

 "apply"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($ResourceLocation$Type)>, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): void

(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($ResourceLocation$Type)>, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): void
}

export namespace $EventPhaseImpl$Ordering {
const BEFORE: $EventPhaseImpl$Ordering
const AFTER: $EventPhaseImpl$Ordering
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventPhaseImpl$Ordering$Type = ($EventPhaseImpl$Ordering);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventPhaseImpl$Ordering_ = $EventPhaseImpl$Ordering$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$FlammableBlocksContextForgeImpl" {
import {$FlammableBlocksContext, $FlammableBlocksContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FlammableBlocksContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $FlammableBlocksContextForgeImpl implements $FlammableBlocksContext {

constructor()

public "registerFlammable"(arg0: integer, arg1: integer, ...arg2: ($Block$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlammableBlocksContextForgeImpl$Type = ($FlammableBlocksContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlammableBlocksContextForgeImpl_ = $FlammableBlocksContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$ReloadingBuiltInItemRenderer" {
import {$DynamicBuiltinItemRenderer, $DynamicBuiltinItemRenderer$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicBuiltinItemRenderer"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $ReloadingBuiltInItemRenderer implements $DynamicBuiltinItemRenderer, $ResourceManagerReloadListener {

constructor()

public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public "renderByItem"(arg0: $ItemStack$Type, arg1: $ItemDisplayContext$Type, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadingBuiltInItemRenderer$Type = ($ReloadingBuiltInItemRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadingBuiltInItemRenderer_ = $ReloadingBuiltInItemRenderer$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext" {
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"

export interface $PackRepositorySourcesContext {

 "addRepositorySource"(...arg0: ($RepositorySource$Type)[]): void

(...arg0: ($RepositorySource$Type)[]): void
}

export namespace $PackRepositorySourcesContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackRepositorySourcesContext$Type = ($PackRepositorySourcesContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackRepositorySourcesContext_ = $PackRepositorySourcesContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/container/v1/$ContainerImpl" {
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $ContainerImpl extends $Container {

 "setChanged"(): void
 "getItem"(arg0: integer): $ItemStack
 "getContainerSize"(): integer
 "removeItemNoUpdate"(arg0: integer): $ItemStack
 "removeItem"(arg0: integer, arg1: integer): $ItemStack
 "clearContent"(): void
 "isEmpty"(): boolean
 "stillValid"(arg0: $Player$Type): boolean
 "getItems"(): $NonNullList<($ItemStack)>
 "setItem"(arg0: integer, arg1: $ItemStack$Type): void
 "kjs$self"(): $Container
 "getBlock"(level: $Level$Type): $BlockContainerJS
 "startOpen"(arg0: $Player$Type): void
 "getMaxStackSize"(): integer
 "stopOpen"(arg0: $Player$Type): void
 "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
 "countItem"(arg0: $Item$Type): integer
 "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
 "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
 "getSlots"(): integer
 "getStackInSlot"(slot: integer): $ItemStack
 "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "isMutable"(): boolean
 "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
 "setChanged"(): void
 "asContainer"(): $Container
 "getHeight"(): integer
 "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
 "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
 "getWidth"(): integer
 "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
 "getSlotLimit"(slot: integer): integer
 "clear"(): void
 "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "countNonEmpty"(ingredient: $Ingredient$Type): integer
 "countNonEmpty"(): integer
 "getAllItems"(): $List<($ItemStack)>
 "find"(ingredient: $Ingredient$Type): integer
 "find"(): integer
 "clear"(ingredient: $Ingredient$Type): void
 "count"(ingredient: $Ingredient$Type): integer
 "count"(): integer
 "isEmpty"(): boolean

(arg0: $NonNullList$Type<($ItemStack$Type)>): $ContainerImpl
}

export namespace $ContainerImpl {
function of(arg0: $NonNullList$Type<($ItemStack$Type)>): $ContainerImpl
function of(arg0: integer): $ContainerImpl
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
function tryClear(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerImpl$Type = ($ContainerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerImpl_ = $ContainerImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper" {
import {$KeyMappingActivationHelper$KeyActivationContext, $KeyMappingActivationHelper$KeyActivationContext$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper$KeyActivationContext"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"

export interface $KeyMappingActivationHelper {

 "getKeyActivationContext"(arg0: $KeyMapping$Type): $KeyMappingActivationHelper$KeyActivationContext
 "hasConflictWith"(arg0: $KeyMapping$Type, arg1: $KeyMapping$Type): boolean

(arg0: $KeyMapping$Type): $KeyMappingActivationHelper$KeyActivationContext
}

export namespace $KeyMappingActivationHelper {
const INSTANCE: $KeyMappingActivationHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingActivationHelper$Type = ($KeyMappingActivationHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingActivationHelper_ = $KeyMappingActivationHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$BiomeModificationsContext" {
import {$BiomeModificationContext, $BiomeModificationContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeModificationContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BiomeLoadingContext, $BiomeLoadingContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingContext"
import {$BiomeLoadingPhase, $BiomeLoadingPhase$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingPhase"

export interface $BiomeModificationsContext {

 "register"(arg0: $BiomeLoadingPhase$Type, arg1: $Predicate$Type<($BiomeLoadingContext$Type)>, arg2: $Consumer$Type<($BiomeModificationContext$Type)>): void

(arg0: $BiomeLoadingPhase$Type, arg1: $Predicate$Type<($BiomeLoadingContext$Type)>, arg2: $Consumer$Type<($BiomeModificationContext$Type)>): void
}

export namespace $BiomeModificationsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModificationsContext$Type = ($BiomeModificationsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModificationsContext_ = $BiomeModificationsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ExplosionEvents$Detonate" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ExplosionEvents$Detonate {

 "onExplosionDetonate"(arg0: $Level$Type, arg1: $Explosion$Type, arg2: $List$Type<($BlockPos$Type)>, arg3: $List$Type<($Entity$Type)>): void

(arg0: $Level$Type, arg1: $Explosion$Type, arg2: $List$Type<($BlockPos$Type)>, arg3: $List$Type<($Entity$Type)>): void
}

export namespace $ExplosionEvents$Detonate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionEvents$Detonate$Type = ($ExplosionEvents$Detonate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionEvents$Detonate_ = $ExplosionEvents$Detonate$Type;
}}
declare module "packages/fuzs/puzzleslib/api/block/v1/$BlockConversionHelper" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockConversionHelper {


public static "setBlockForItem"(arg0: $BlockItem$Type, arg1: $Block$Type): void
public static "setBlockItemBlock"(arg0: $BlockItem$Type, arg1: $Block$Type): void
public static "setItemForBlock"(arg0: $Block$Type, arg1: $Item$Type): void
public static "copyBoundTags"(arg0: $Block$Type, arg1: $Block$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockConversionHelper$Type = ($BlockConversionHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockConversionHelper_ = $BlockConversionHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelEvents$Unload" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerLevelEvents$Unload {

 "onLevelUnload"(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void

(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void
}

export namespace $ServerLevelEvents$Unload {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelEvents$Unload$Type = ($ServerLevelEvents$Unload);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelEvents$Unload_ = $ServerLevelEvents$Unload$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/json/$GsonEnumHelper" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $GsonEnumHelper {

constructor()

public static "getAsEnum"<T extends $Enum<(T)>>(arg0: $JsonObject$Type, arg1: string, arg2: $Class$Type<(T)>, arg3: T): T
public static "getAsEnum"<T extends $Enum<(T)>>(arg0: $JsonObject$Type, arg1: string, arg2: $Class$Type<(T)>): T
public static "convertToEnum"<T extends $Enum<(T)>>(arg0: string, arg1: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GsonEnumHelper$Type = ($GsonEnumHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GsonEnumHelper_ = $GsonEnumHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$AfterMouseAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $InputEvents$AfterMouseAction {

 "onAfterMouseAction"(arg0: integer, arg1: integer, arg2: integer): void

(arg0: integer, arg1: integer, arg2: integer): void
}

export namespace $InputEvents$AfterMouseAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$AfterMouseAction$Type = ($InputEvents$AfterMouseAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents$AfterMouseAction_ = $InputEvents$AfterMouseAction$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$ForgeEventInvokerRegistry" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$ForgeEventInvokerRegistry$ForgeEventContextConsumer, $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$ForgeEventInvokerRegistry$ForgeEventContextConsumer"
import {$EventInvokerRegistry, $EventInvokerRegistry$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvokerRegistry"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ForgeEventInvokerRegistry extends $EventInvokerRegistry {

 "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $BiConsumer$Type<(T), (E)>): void
 "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type<(T), (E)>, arg3: boolean): void
 "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $BiConsumer$Type<(T), (E)>, arg3: boolean): void
 "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type<(T), (E)>): void
 "register"<T>(arg0: $Class$Type<(T)>, arg1: $BiConsumer$Type<(T), (any)>): void
 "register"<T>(arg0: $Class$Type<(T)>, arg1: $BiConsumer$Type<(T), (any)>, arg2: boolean): void

(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $BiConsumer$Type<(T), (E)>): void
}

export namespace $ForgeEventInvokerRegistry {
const INSTANCE: $ForgeEventInvokerRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventInvokerRegistry$Type = ($ForgeEventInvokerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventInvokerRegistry_ = $ForgeEventInvokerRegistry$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$ItemColorProvidersContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ColorProvidersContext, $ColorProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ColorProvidersContext"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$ItemColors, $ItemColors$Type} from "packages/net/minecraft/client/color/item/$ItemColors"

export class $ItemColorProvidersContextForgeImpl extends $Record implements $ColorProvidersContext<($Item), ($ItemColor)> {

constructor(consumer: $BiConsumer$Type<($ItemColor$Type), ($ItemLike$Type)>, itemColors: $ItemColors$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getProvider"(arg0: $Item$Type): $ItemColor
public "consumer"(): $BiConsumer<($ItemColor), ($ItemLike)>
public "registerColorProvider"(arg0: $ItemColor$Type, ...arg1: ($Item$Type)[]): void
public "itemColors"(): $ItemColors
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemColorProvidersContextForgeImpl$Type = ($ItemColorProvidersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemColorProvidersContextForgeImpl_ = $ItemColorProvidersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$ShieldBlockCallback" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"

export interface $ShieldBlockCallback {

 "onShieldBlock"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $DefaultedFloat$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $DefaultedFloat$Type): $EventResult
}

export namespace $ShieldBlockCallback {
const EVENT: $EventInvoker<($ShieldBlockCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShieldBlockCallback$Type = ($ShieldBlockCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShieldBlockCallback_ = $ShieldBlockCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder" {
import {$NetworkHandlerRegistry, $NetworkHandlerRegistry$Type} from "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerRegistry"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$Buildable, $Buildable$Type} from "packages/fuzs/puzzleslib/api/core/v1/$Buildable"
import {$FriendlyByteBuf$Reader, $FriendlyByteBuf$Reader$Type} from "packages/net/minecraft/network/$FriendlyByteBuf$Reader"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$FriendlyByteBuf$Writer, $FriendlyByteBuf$Writer$Type} from "packages/net/minecraft/network/$FriendlyByteBuf$Writer"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MessageSerializer, $MessageSerializer$Type} from "packages/fuzs/puzzleslib/api/network/v3/serialization/$MessageSerializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $NetworkHandlerV3$Builder extends $NetworkHandlerRegistry, $Buildable {

 "registerContainerProvider"<T>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(($Type$Type)[]), ($MessageSerializer$Type<(any)>)>): $NetworkHandlerV3$Builder
 "registerSerializer"<T>(arg0: $Class$Type<(T)>, arg1: $FriendlyByteBuf$Writer$Type<(T)>, arg2: $FriendlyByteBuf$Reader$Type<(T)>): $NetworkHandlerV3$Builder
 "registerSerializer"<T>(arg0: $Class$Type<(any)>, arg1: $ResourceKey$Type<($Registry$Type<(T)>)>): $NetworkHandlerV3$Builder
 "clientAcceptsVanillaOrMissing"(): $NetworkHandlerV3$Builder
 "serverAcceptsVanillaOrMissing"(): $NetworkHandlerV3$Builder
 "registerClientbound"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Class$Type<(T)>): $NetworkHandlerV3$Builder
 "registerServerbound"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: $Class$Type<(T)>): $NetworkHandlerV3$Builder
 "allAcceptVanillaOrMissing"(): $NetworkHandlerV3$Builder
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockEntity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $LevelChunk$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: $ChunkPos$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T, arg2: boolean): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: $ServerLevel$Type, arg4: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Vec3i$Type, arg1: $ServerLevel$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $ServerLevel$Type, arg6: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Collection$Type<($ServerPlayer$Type)>, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendTo"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "toServerboundPacket"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): $Packet<($ServerGamePacketListener)>
/**
 * 
 * @deprecated
 */
 "toClientboundPacket"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): $Packet<($ClientGamePacketListener)>
/**
 * 
 * @deprecated
 */
 "sendToServer"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): void
 "build"(): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ResourceKey$Type<($Level$Type)>, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Level$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockPos$Type, arg1: $Level$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Level$Type, arg5: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTrackingAndSelf"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNearExcept"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Level$Type, arg6: T): void
}

export namespace $NetworkHandlerV3$Builder {
function builder(arg0: string): $NetworkHandlerV3$Builder
function builder(arg0: string, arg1: string): $NetworkHandlerV3$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerV3$Builder$Type = ($NetworkHandlerV3$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerV3$Builder_ = $NetworkHandlerV3$Builder$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/chat/$FormattedContentSink" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FormattedCharSink, $FormattedCharSink$Type} from "packages/net/minecraft/util/$FormattedCharSink"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$FormattedText$StyledContentConsumer, $FormattedText$StyledContentConsumer$Type} from "packages/net/minecraft/network/chat/$FormattedText$StyledContentConsumer"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$Unit, $Unit$Type} from "packages/net/minecraft/util/$Unit"

export class $FormattedContentSink implements $FormattedText$StyledContentConsumer<($Unit)>, $FormattedCharSink {

constructor(arg0: $FormattedText$Type)
constructor(arg0: $FormattedCharSequence$Type)

public "getComponent"(): $Component
public "accept"(arg0: integer, arg1: $Style$Type, arg2: integer): boolean
public "accept"(arg0: $Style$Type, arg1: string): $Optional<($Unit)>
get "component"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FormattedContentSink$Type = ($FormattedContentSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FormattedContentSink_ = $FormattedContentSink$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Remove" {
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $MobEffectEvents$Remove {

 "onMobEffectRemove"(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type): $EventResult
}

export namespace $MobEffectEvents$Remove {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobEffectEvents$Remove$Type = ($MobEffectEvents$Remove);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobEffectEvents$Remove_ = $MobEffectEvents$Remove$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractSpriteSourceProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$SpriteSourceProvider, $SpriteSourceProvider$Type} from "packages/net/minecraftforge/common/data/$SpriteSourceProvider"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $AbstractSpriteSourceProvider extends $SpriteSourceProvider {

/**
 * 
 * @deprecated
 */
constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type)
/**
 * 
 * @deprecated
 */
constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type)
constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type, arg2: string)
constructor(arg0: $GatherDataEvent$Type, arg1: string)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSpriteSourceProvider$Type = ($AbstractSpriteSourceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSpriteSourceProvider_ = $AbstractSpriteSourceProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext" {
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $DataProviderContext {

constructor(arg0: string, arg1: $PackOutput$Type, arg2: $Supplier$Type<($CompletableFuture$Type<($HolderLookup$Provider$Type)>)>)

public "getLookupProvider"(): $CompletableFuture<($HolderLookup$Provider)>
public "getPackOutput"(): $PackOutput
public static "fromModId"(arg0: string): $DataProviderContext
public "getModId"(): string
get "lookupProvider"(): $CompletableFuture<($HolderLookup$Provider)>
get "packOutput"(): $PackOutput
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataProviderContext$Type = ($DataProviderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataProviderContext_ = $DataProviderContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext$Factory" {
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$ForgeDataProviderContext, $ForgeDataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"

export interface $ForgeDataProviderContext$Factory extends $Function<($ForgeDataProviderContext), ($DataProvider)> {

 "apply"(arg0: $ForgeDataProviderContext$Type): $DataProvider
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($DataProvider)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($ForgeDataProviderContext), (V)>

(arg0: $ForgeDataProviderContext$Type): $DataProvider
}

export namespace $ForgeDataProviderContext$Factory {
function identity<T>(): $Function<($ForgeDataProviderContext), ($ForgeDataProviderContext)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeDataProviderContext$Factory$Type = ($ForgeDataProviderContext$Factory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeDataProviderContext$Factory_ = $ForgeDataProviderContext$Factory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/particle/v1/$ClientParticleTypes" {
import {$Particle, $Particle$Type} from "packages/net/minecraft/client/particle/$Particle"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export interface $ClientParticleTypes {

 "createParticle"(arg0: $ResourceLocation$Type, arg1: $ParticleOptions$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Particle
 "createParticle"(arg0: $ResourceLocation$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Particle

(arg0: $ResourceLocation$Type, arg1: $ParticleOptions$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Particle
}

export namespace $ClientParticleTypes {
const INSTANCE: $ClientParticleTypes
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientParticleTypes$Type = ($ClientParticleTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientParticleTypes_ = $ClientParticleTypes$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStopped" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerLifecycleEvents$ServerStopped {

 "onServerStopped"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerLifecycleEvents$ServerStopped {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLifecycleEvents$ServerStopped$Type = ($ServerLifecycleEvents$ServerStopped);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLifecycleEvents$ServerStopped_ = $ServerLifecycleEvents$ServerStopped$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$ConsumingOperator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export class $ScreenEvents$ConsumingOperator<T> {

constructor(arg0: $Consumer$Type<(T)>)

public "apply"<S extends T>(arg0: S): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$ConsumingOperator$Type<T> = ($ScreenEvents$ConsumingOperator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$ConsumingOperator_<T> = $ScreenEvents$ConsumingOperator$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/item/v2/crafting/$CombinedIngredients" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export interface $CombinedIngredients {

 "difference"(arg0: $Ingredient$Type, arg1: $Ingredient$Type): $Ingredient
 "all"(...arg0: ($Ingredient$Type)[]): $Ingredient
 "any"(...arg0: ($Ingredient$Type)[]): $Ingredient
 "nbt"(arg0: $ItemStack$Type, arg1: boolean): $Ingredient
 "nbt"(arg0: $ItemLike$Type, arg1: $CompoundTag$Type, arg2: boolean): $Ingredient
}

export namespace $CombinedIngredients {
const INSTANCE: $CombinedIngredients
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CombinedIngredients$Type = ($CombinedIngredients);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CombinedIngredients_ = $CombinedIngredients$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$SpawnPlacementsContext" {
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$SpawnPlacements$SpawnPredicate, $SpawnPlacements$SpawnPredicate$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$SpawnPredicate"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"

export interface $SpawnPlacementsContext {

 "registerSpawnPlacement"<T extends $Mob>(arg0: $EntityType$Type<(T)>, arg1: $SpawnPlacements$Type$Type, arg2: $Heightmap$Types$Type, arg3: $SpawnPlacements$SpawnPredicate$Type<(T)>): void

(arg0: $EntityType$Type<(T)>, arg1: $SpawnPlacements$Type$Type, arg2: $Heightmap$Types$Type, arg3: $SpawnPlacements$SpawnPredicate$Type<(T)>): void
}

export namespace $SpawnPlacementsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnPlacementsContext$Type = ($SpawnPlacementsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnPlacementsContext_ = $SpawnPlacementsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v2/$RegistryManager" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$RegistryReference, $RegistryReference$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryReference"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ModLoader, $ModLoader$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoader"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$MenuType$MenuSupplier, $MenuType$MenuSupplier$Type} from "packages/net/minecraft/world/inventory/$MenuType$MenuSupplier"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SimpleParticleType, $SimpleParticleType$Type} from "packages/net/minecraft/core/particles/$SimpleParticleType"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$PoiTypeBuilder, $PoiTypeBuilder$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$PoiTypeBuilder"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$BlockEntityType$Builder, $BlockEntityType$Builder$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$Builder"
import {$ExtendedMenuSupplier, $ExtendedMenuSupplier$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $RegistryManager {

 "register"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: $Supplier$Type<(T)>): $RegistryReference<(T)>
 "registerBlockEntityType"<T extends $BlockEntity>(arg0: string, arg1: $Supplier$Type<($BlockEntityType$Builder$Type<(T)>)>): $RegistryReference<($BlockEntityType<(T)>)>
 "registerEnchantment"(arg0: string, arg1: $Supplier$Type<($Enchantment$Type)>): $RegistryReference<($Enchantment)>
 "makeKey"(arg0: string): $ResourceLocation
 "registerBlock"(arg0: string, arg1: $Supplier$Type<($Block$Type)>): $RegistryReference<($Block)>
 "registerRecipeType"<T extends $Recipe<(any)>>(arg0: string): $RegistryReference<($RecipeType<(T)>)>
 "registerMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($MenuType$MenuSupplier$Type<(T)>)>): $RegistryReference<($MenuType<(T)>)>
 "registerGameEvent"(arg0: string, arg1: integer): $RegistryReference<($GameEvent)>
 "whenNotOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
 "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($Block$Type)>)>): $RegistryReference<($PoiType)>
 "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($BlockState$Type)>)>, arg2: integer, arg3: integer): $RegistryReference<($PoiType)>
 "whenOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
 "registerDamageType"(arg0: string): $ResourceKey<($DamageType)>
 "registerBiomeTag"(arg0: string): $TagKey<($Biome)>
 "registerBlockTag"(arg0: string): $TagKey<($Block)>
 "registerTag"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $TagKey<(T)>
 "registerItemTag"(arg0: string): $TagKey<($Item)>
 "registerFluid"(arg0: string, arg1: $Supplier$Type<($Fluid$Type)>): $RegistryReference<($Fluid)>
 "registerItem"(arg0: string, arg1: $Supplier$Type<($Item$Type)>): $RegistryReference<($Item)>
 "registerEntityType"<T extends $Entity>(arg0: string, arg1: $Supplier$Type<($EntityType$Builder$Type<(T)>)>): $RegistryReference<($EntityType<(T)>)>
 "registerMobEffect"(arg0: string, arg1: $Supplier$Type<($MobEffect$Type)>): $RegistryReference<($MobEffect)>
 "registerPotion"(arg0: string, arg1: $Supplier$Type<($Potion$Type)>): $RegistryReference<($Potion)>
 "registerSoundEvent"(arg0: string): $RegistryReference<($SoundEvent)>
 "registerBlockItem"(arg0: $RegistryReference$Type<($Block$Type)>, arg1: $Item$Properties$Type): $RegistryReference<($Item)>
 "registerBlockItem"(arg0: $RegistryReference$Type<($Block$Type)>): $RegistryReference<($Item)>
/**
 * 
 * @deprecated
 */
 "registerPoiTypeBuilder"(arg0: string, arg1: $Supplier$Type<($PoiTypeBuilder$Type)>): $RegistryReference<($PoiType)>
 "registerSpawnEggItem"(arg0: $RegistryReference$Type<(any)>, arg1: integer, arg2: integer, arg3: $Item$Properties$Type): $RegistryReference<($Item)>
 "registerSpawnEggItem"(arg0: $RegistryReference$Type<(any)>, arg1: integer, arg2: integer): $RegistryReference<($Item)>
 "registerExtendedMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($ExtendedMenuSupplier$Type<(T)>)>): $RegistryReference<($MenuType<(T)>)>
 "registerResourceKey"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $ResourceKey<(T)>
 "registerEnchantmentTag"(arg0: string): $TagKey<($Enchantment)>
 "registerDamageTypeTag"(arg0: string): $TagKey<($DamageType)>
 "registerGameEventTag"(arg0: string): $TagKey<($GameEvent)>
 "registerEntityTypeTag"(arg0: string): $TagKey<($EntityType<(any)>)>
 "registerParticleType"(arg0: string): $RegistryReference<($SimpleParticleType)>
 "placeholder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $RegistryReference<(T)>
}

export namespace $RegistryManager {
function instant(arg0: string): $RegistryManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryManager$Type = ($RegistryManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryManager_ = $RegistryManager$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v3/$RegistryManager" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ModLoader, $ModLoader$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoader"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$MenuType$MenuSupplier, $MenuType$MenuSupplier$Type} from "packages/net/minecraft/world/inventory/$MenuType$MenuSupplier"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SimpleParticleType, $SimpleParticleType$Type} from "packages/net/minecraft/core/particles/$SimpleParticleType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$BlockEntityType$Builder, $BlockEntityType$Builder$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$Builder"
import {$ExtendedMenuSupplier, $ExtendedMenuSupplier$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $RegistryManager {

 "register"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: $Supplier$Type<(T)>): $Holder$Reference<(T)>
 "registerBlockEntityType"<T extends $BlockEntity>(arg0: string, arg1: $Supplier$Type<($BlockEntityType$Builder$Type<(T)>)>): $Holder$Reference<($BlockEntityType<(T)>)>
 "registerEnchantment"(arg0: string, arg1: $Supplier$Type<($Enchantment$Type)>): $Holder$Reference<($Enchantment)>
 "makeKey"(arg0: string): $ResourceLocation
 "registerBlock"(arg0: string, arg1: $Supplier$Type<($Block$Type)>): $Holder$Reference<($Block)>
 "registerRecipeType"<T extends $Recipe<(any)>>(arg0: string): $Holder$Reference<($RecipeType<(T)>)>
 "registerMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($MenuType$MenuSupplier$Type<(T)>)>): $Holder$Reference<($MenuType<(T)>)>
 "makeResourceKey"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $ResourceKey<(T)>
 "registerGameEvent"(arg0: string, arg1: integer): $Holder$Reference<($GameEvent)>
 "whenNotOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
 "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Block$Type)>): $Holder$Reference<($PoiType)>
 "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($BlockState$Type)>)>, arg2: integer, arg3: integer): $Holder$Reference<($PoiType)>
 "whenOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
 "registerDamageType"(arg0: string): $ResourceKey<($DamageType)>
 "registerFluid"(arg0: string, arg1: $Supplier$Type<($Fluid$Type)>): $Holder$Reference<($Fluid)>
 "registerItem"(arg0: string, arg1: $Supplier$Type<($Item$Type)>): $Holder$Reference<($Item)>
 "registerEntityType"<T extends $Entity>(arg0: string, arg1: $Supplier$Type<($EntityType$Builder$Type<(T)>)>): $Holder$Reference<($EntityType<(T)>)>
 "registerMobEffect"(arg0: string, arg1: $Supplier$Type<($MobEffect$Type)>): $Holder$Reference<($MobEffect)>
 "registerPotion"(arg0: string, arg1: $Supplier$Type<($Potion$Type)>): $Holder$Reference<($Potion)>
 "registerSoundEvent"(arg0: string): $Holder$Reference<($SoundEvent)>
 "registerBlockItem"(arg0: $Holder$Reference$Type<($Block$Type)>): $Holder$Reference<($Item)>
 "registerBlockItem"(arg0: $Holder$Reference$Type<($Block$Type)>, arg1: $Item$Properties$Type): $Holder$Reference<($Item)>
 "registerSpawnEggItem"(arg0: $Holder$Reference$Type<(any)>, arg1: integer, arg2: integer): $Holder$Reference<($Item)>
 "registerSpawnEggItem"(arg0: $Holder$Reference$Type<(any)>, arg1: integer, arg2: integer, arg3: $Item$Properties$Type): $Holder$Reference<($Item)>
 "registerExtendedMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($ExtendedMenuSupplier$Type<(T)>)>): $Holder$Reference<($MenuType<(T)>)>
 "registerParticleType"(arg0: string): $Holder$Reference<($SimpleParticleType)>
 "getHolder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $Holder$Reference<(T)>
}

export namespace $RegistryManager {
function from(arg0: string): $RegistryManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryManager$Type = ($RegistryManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryManager_ = $RegistryManager$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$Use" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $InteractionInputEvents$Use {

 "onUseInteraction"(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type, arg2: $InteractionHand$Type, arg3: $HitResult$Type): $EventResult

(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type, arg2: $InteractionHand$Type, arg3: $HitResult$Type): $EventResult
}

export namespace $InteractionInputEvents$Use {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionInputEvents$Use$Type = ($InteractionInputEvents$Use);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionInputEvents$Use_ = $InteractionInputEvents$Use$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v2/$RegistryReference" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $RegistryReference<T> {

 "get"(): T
 "isEmpty"(): boolean
 "isPresent"(): boolean
 "holder"(): $Holder<(T)>
 "getResourceKey"(): $ResourceKey<(T)>
 "getResourceLocation"(): $ResourceLocation
 "getRegistryKey"(): $ResourceKey<(any)>
}

export namespace $RegistryReference {
function placeholder<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: string): $RegistryReference<(T)>
function placeholder<T>(arg0: $ResourceKey$Type<(any)>, arg1: $ResourceLocation$Type): $RegistryReference<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryReference$Type<T> = ($RegistryReference<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryReference_<T> = $RegistryReference$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$CopyTagRecipe" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export interface $CopyTagRecipe {

 "tryCopyTagToResult"(arg0: $ItemStack$Type, arg1: $CraftingContainer$Type): void
 "getCopyTagSource"(): $Ingredient

(arg0: $BiConsumer$Type<(string), ($Supplier$Type<($RecipeSerializer$Type<(any)>)>)>): void
}

export namespace $CopyTagRecipe {
const SHAPED_RECIPE_SERIALIZER_ID: string
const SHAPELESS_RECIPE_SERIALIZER_ID: string
function registerSerializers(arg0: $BiConsumer$Type<(string), ($Supplier$Type<($RecipeSerializer$Type<(any)>)>)>): void
function getModSerializer(arg0: string, arg1: string): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CopyTagRecipe$Type = ($CopyTagRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CopyTagRecipe_ = $CopyTagRecipe$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$GrindstoneEvents$Use" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$DefaultedValue, $DefaultedValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue"

export interface $GrindstoneEvents$Use {

 "onGrindstoneUse"(arg0: $DefaultedValue$Type<($ItemStack$Type)>, arg1: $DefaultedValue$Type<($ItemStack$Type)>, arg2: $Player$Type): void

(arg0: $DefaultedValue$Type<($ItemStack$Type)>, arg1: $DefaultedValue$Type<($ItemStack$Type)>, arg2: $Player$Type): void
}

export namespace $GrindstoneEvents$Use {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrindstoneEvents$Use$Type = ($GrindstoneEvents$Use);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrindstoneEvents$Use_ = $GrindstoneEvents$Use$Type;
}}
declare module "packages/fuzs/puzzleslib/api/item/v2/$LegacySmithingTransformRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$CustomRecipe, $CustomRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CustomRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CraftingBookCategory, $CraftingBookCategory$Type} from "packages/net/minecraft/world/item/crafting/$CraftingBookCategory"

/**
 * 
 * @deprecated
 */
export class $LegacySmithingTransformRecipe extends $CustomRecipe {
static readonly "RECIPE_SERIALIZER_ID": string

constructor(arg0: $ResourceLocation$Type, arg1: $CraftingBookCategory$Type, arg2: $Ingredient$Type, arg3: $Ingredient$Type, arg4: $ItemStack$Type)

public "matches"(arg0: $CraftingContainer$Type, arg1: $Level$Type): boolean
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getIngredients"(): $NonNullList<($Ingredient)>
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
public static "getModSerializer"(arg0: string): $RecipeSerializer<(any)>
public "isSpecial"(): boolean
get "ingredients"(): $NonNullList<($Ingredient)>
get "serializer"(): $RecipeSerializer<(any)>
get "special"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LegacySmithingTransformRecipe$Type = ($LegacySmithingTransformRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LegacySmithingTransformRecipe_ = $LegacySmithingTransformRecipe$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/$ClientModConstructor" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ClientTooltipComponentsContext, $ClientTooltipComponentsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ClientTooltipComponentsContext"
import {$LayerDefinitionsContext, $LayerDefinitionsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LayerDefinitionsContext"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$AdditionalModelsContext, $AdditionalModelsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$AdditionalModelsContext"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SearchRegistryContext, $SearchRegistryContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SearchRegistryContext"
import {$ItemDecorationContext, $ItemDecorationContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemDecorationContext"
import {$DynamicModifyBakingResultContext, $DynamicModifyBakingResultContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicModifyBakingResultContext"
import {$SkullRenderersContext, $SkullRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SkullRenderersContext"
import {$CoreShadersContext, $CoreShadersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$CoreShadersContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ColorProvidersContext, $ColorProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ColorProvidersContext"
import {$BlockEntityRenderersContext, $BlockEntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BlockEntityRenderersContext"
import {$BaseModConstructor, $BaseModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$BaseModConstructor"
import {$DynamicBakingCompletedContext, $DynamicBakingCompletedContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicBakingCompletedContext"
import {$BuiltinModelItemRendererContext, $BuiltinModelItemRendererContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BuiltinModelItemRendererContext"
import {$EntityRenderersContext, $EntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntityRenderersContext"
import {$RenderTypesContext, $RenderTypesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$RenderTypesContext"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KeyMappingsContext, $KeyMappingsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$KeyMappingsContext"
import {$ParticleProvidersContext, $ParticleProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ParticleProvidersContext"
import {$ModLifecycleContext, $ModLifecycleContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$EntitySpectatorShaderContext, $EntitySpectatorShaderContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntitySpectatorShaderContext"
import {$LivingEntityRenderLayersContext, $LivingEntityRenderLayersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LivingEntityRenderLayersContext"
import {$ItemModelPropertiesContext, $ItemModelPropertiesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemModelPropertiesContext"

export interface $ClientModConstructor extends $BaseModConstructor {

 "onConstructMod"(): void
 "onRegisterParticleProviders"(arg0: $ParticleProvidersContext$Type): void
/**
 * 
 * @deprecated
 */
 "onBakingCompleted"(arg0: $DynamicBakingCompletedContext$Type): void
/**
 * 
 * @deprecated
 */
 "onModifyBakingResult"(arg0: $DynamicModifyBakingResultContext$Type): void
 "onRegisterBuiltinModelItemRenderers"(arg0: $BuiltinModelItemRendererContext$Type): void
 "onRegisterResourcePackReloadListeners"(arg0: $AddReloadListenersContext$Type): void
/**
 * 
 * @deprecated
 */
 "onClientSetup"(arg0: $ModLifecycleContext$Type): void
 "onClientSetup"(): void
 "onRegisterFluidRenderTypes"(arg0: $RenderTypesContext$Type<($Fluid$Type)>): void
 "onRegisterEntitySpectatorShaders"(arg0: $EntitySpectatorShaderContext$Type): void
 "onRegisterLivingEntityRenderLayers"(arg0: $LivingEntityRenderLayersContext$Type): void
 "onRegisterEntityRenderers"(arg0: $EntityRenderersContext$Type): void
 "onRegisterLayerDefinitions"(arg0: $LayerDefinitionsContext$Type): void
 "onRegisterItemModelProperties"(arg0: $ItemModelPropertiesContext$Type): void
 "onRegisterBlockColorProviders"(arg0: $ColorProvidersContext$Type<($Block$Type), ($BlockColor$Type)>): void
 "onRegisterCoreShaders"(arg0: $CoreShadersContext$Type): void
 "onRegisterItemDecorations"(arg0: $ItemDecorationContext$Type): void
/**
 * 
 * @deprecated
 */
 "onRegisterSearchTrees"(arg0: $SearchRegistryContext$Type): void
 "onRegisterSkullRenderers"(arg0: $SkullRenderersContext$Type): void
 "onAddResourcePackFinders"(arg0: $PackRepositorySourcesContext$Type): void
 "onRegisterItemColorProviders"(arg0: $ColorProvidersContext$Type<($Item$Type), ($ItemColor$Type)>): void
 "onRegisterBlockEntityRenderers"(arg0: $BlockEntityRenderersContext$Type): void
 "onRegisterClientTooltipComponents"(arg0: $ClientTooltipComponentsContext$Type): void
 "onRegisterBlockRenderTypes"(arg0: $RenderTypesContext$Type<($Block$Type)>): void
 "onRegisterKeyMappings"(arg0: $KeyMappingsContext$Type): void
 "onRegisterAdditionalModels"(arg0: $AdditionalModelsContext$Type): void
 "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
 "getPairingIdentifier"(): $ResourceLocation
}

export namespace $ClientModConstructor {
function construct(arg0: string, arg1: $Supplier$Type<($ClientModConstructor$Type)>): void
function construct(arg0: string, arg1: $Supplier$Type<($ClientModConstructor$Type)>, ...arg2: ($ContentRegistrationFlags$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientModConstructor$Type = ($ClientModConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientModConstructor_ = $ClientModConstructor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$BeforeKeyRelease" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ScreenKeyboardEvents$BeforeKeyRelease<T extends $Screen> {

 "onBeforeKeyRelease"(arg0: T, arg1: integer, arg2: integer, arg3: integer): $EventResult

(arg0: T, arg1: integer, arg2: integer, arg3: integer): $EventResult
}

export namespace $ScreenKeyboardEvents$BeforeKeyRelease {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenKeyboardEvents$BeforeKeyRelease$Type<T> = ($ScreenKeyboardEvents$BeforeKeyRelease<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenKeyboardEvents$BeforeKeyRelease_<T> = $ScreenKeyboardEvents$BeforeKeyRelease$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterTranslucent" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

export interface $RenderLevelEvents$AfterTranslucent {

 "onRenderLevelAfterTranslucent"(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void

(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void
}

export namespace $RenderLevelEvents$AfterTranslucent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEvents$AfterTranslucent$Type = ($RenderLevelEvents$AfterTranslucent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEvents$AfterTranslucent_ = $RenderLevelEvents$AfterTranslucent$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$CustomizeChatPanelCallback" {
import {$Window, $Window$Type} from "packages/com/mojang/blaze3d/platform/$Window"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $CustomizeChatPanelCallback {

 "onRenderChatPanel"(arg0: $Window$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: $MutableInt$Type, arg4: $MutableInt$Type): void

(arg0: $Window$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: $MutableInt$Type, arg4: $MutableInt$Type): void
}

export namespace $CustomizeChatPanelCallback {
const EVENT: $EventInvoker<($CustomizeChatPanelCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomizeChatPanelCallback$Type = ($CustomizeChatPanelCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomizeChatPanelCallback_ = $CustomizeChatPanelCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterLevel" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

export interface $RenderLevelEvents$AfterLevel {

 "onRenderLevelAfterLevel"(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void

(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void
}

export namespace $RenderLevelEvents$AfterLevel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEvents$AfterLevel$Type = ($RenderLevelEvents$AfterLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEvents$AfterLevel_ = $RenderLevelEvents$AfterLevel$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$AddToastCallback" {
import {$Toast, $Toast$Type} from "packages/net/minecraft/client/gui/components/toasts/$Toast"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$ToastComponent, $ToastComponent$Type} from "packages/net/minecraft/client/gui/components/toasts/$ToastComponent"

export interface $AddToastCallback {

 "onAddToast"(arg0: $ToastComponent$Type, arg1: $Toast$Type): $EventResult

(arg0: $ToastComponent$Type, arg1: $Toast$Type): $EventResult
}

export namespace $AddToastCallback {
const EVENT: $EventInvoker<($AddToastCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddToastCallback$Type = ($AddToastCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddToastCallback_ = $AddToastCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/particle/$ClientParticleTypesImpl" {
import {$ClientParticleTypes, $ClientParticleTypes$Type} from "packages/fuzs/puzzleslib/api/client/particle/v1/$ClientParticleTypes"
import {$Particle, $Particle$Type} from "packages/net/minecraft/client/particle/$Particle"
import {$ClientParticleTypesManager, $ClientParticleTypesManager$Type} from "packages/fuzs/puzzleslib/impl/client/particle/$ClientParticleTypesManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export class $ClientParticleTypesImpl implements $ClientParticleTypes {

constructor()

public "getParticleTypesManager"(arg0: string): $ClientParticleTypesManager
public "createParticle"(arg0: $ResourceLocation$Type, arg1: $ParticleOptions$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Particle
public "createParticle"(arg0: $ResourceLocation$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Particle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientParticleTypesImpl$Type = ($ClientParticleTypesImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientParticleTypesImpl_ = $ClientParticleTypesImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingFallCallback" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingFallCallback {

 "onLivingFall"(arg0: $LivingEntity$Type, arg1: $MutableFloat$Type, arg2: $MutableFloat$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $MutableFloat$Type, arg2: $MutableFloat$Type): $EventResult
}

export namespace $LivingFallCallback {
const EVENT: $EventInvoker<($LivingFallCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingFallCallback$Type = ($LivingFallCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingFallCallback_ = $LivingFallCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/$PuzzlesLibMod" {
import {$FlammableBlocksContext, $FlammableBlocksContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FlammableBlocksContext"
import {$CreativeModeTabContext, $CreativeModeTabContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$CreativeModeTabContext"
import {$BlockInteractionsContext, $BlockInteractionsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BlockInteractionsContext"
import {$BiomeModificationsContext, $BiomeModificationsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BiomeModificationsContext"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$BuildCreativeModeTabContentsContext, $BuildCreativeModeTabContentsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BuildCreativeModeTabContentsContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EntityAttributesCreateContext, $EntityAttributesCreateContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesCreateContext"
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$EntityAttributesModifyContext, $EntityAttributesModifyContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesModifyContext"
import {$SpawnPlacementsContext, $SpawnPlacementsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$SpawnPlacementsContext"
import {$FuelBurnTimesContext, $FuelBurnTimesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FuelBurnTimesContext"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$ModLifecycleContext, $ModLifecycleContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext"
import {$NetworkHandlerV3, $NetworkHandlerV3$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$ModConstructor, $ModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModConstructor"
import {$PuzzlesLib, $PuzzlesLib$Type} from "packages/fuzs/puzzleslib/impl/$PuzzlesLib"

export class $PuzzlesLibMod extends $PuzzlesLib implements $ModConstructor {
static readonly "NETWORK": $NetworkHandlerV3
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOGGER": $Logger

constructor()

public "onConstructMod"(): void
public static "construct"(arg0: string, arg1: $Supplier$Type<($ModConstructor$Type)>): void
/**
 * 
 * @deprecated
 */
public static "construct"(arg0: string, arg1: $Supplier$Type<($ModConstructor$Type)>, ...arg2: ($ContentRegistrationFlags$Type)[]): void
public "onBuildCreativeModeTabContents"(arg0: $BuildCreativeModeTabContentsContext$Type): void
public "onCommonSetup"(): void
/**
 * 
 * @deprecated
 */
public "onCommonSetup"(arg0: $ModLifecycleContext$Type): void
public "onAddDataPackFinders"(arg0: $PackRepositorySourcesContext$Type): void
public "onRegisterCreativeModeTabs"(arg0: $CreativeModeTabContext$Type): void
public "onRegisterFuelBurnTimes"(arg0: $FuelBurnTimesContext$Type): void
public "onRegisterSpawnPlacements"(arg0: $SpawnPlacementsContext$Type): void
public "onRegisterFlammableBlocks"(arg0: $FlammableBlocksContext$Type): void
public "onRegisterBlockInteractions"(arg0: $BlockInteractionsContext$Type): void
public "onRegisterDataPackReloadListeners"(arg0: $AddReloadListenersContext$Type): void
public "onEntityAttributeModification"(arg0: $EntityAttributesModifyContext$Type): void
public "onEntityAttributeCreation"(arg0: $EntityAttributesCreateContext$Type): void
public "onRegisterBiomeModifications"(arg0: $BiomeModificationsContext$Type): void
public "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
public "getPairingIdentifier"(): $ResourceLocation
get "contentRegistrationFlags"(): ($ContentRegistrationFlags)[]
get "pairingIdentifier"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PuzzlesLibMod$Type = ($PuzzlesLibMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PuzzlesLibMod_ = $PuzzlesLibMod$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/$PuzzlesLibForgeClient" {
import {$FMLConstructModEvent, $FMLConstructModEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLConstructModEvent"

export class $PuzzlesLibForgeClient {

constructor()

public static "onConstructMod"(arg0: $FMLConstructModEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PuzzlesLibForgeClient$Type = ($PuzzlesLibForgeClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PuzzlesLibForgeClient_ = $PuzzlesLibForgeClient$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseDrag" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenMouseEvents$AfterMouseDrag<T extends $Screen> {

 "onAfterMouseDrag"(arg0: T, arg1: double, arg2: double, arg3: integer, arg4: double, arg5: double): void

(arg0: T, arg1: double, arg2: double, arg3: integer, arg4: double, arg5: double): void
}

export namespace $ScreenMouseEvents$AfterMouseDrag {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$AfterMouseDrag$Type<T> = ($ScreenMouseEvents$AfterMouseDrag<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$AfterMouseDrag_<T> = $ScreenMouseEvents$AfterMouseDrag$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenTooltipEvents" {
import {$ScreenTooltipEvents$Render, $ScreenTooltipEvents$Render$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenTooltipEvents$Render"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ScreenTooltipEvents {
static readonly "RENDER": $EventInvoker<($ScreenTooltipEvents$Render)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenTooltipEvents$Type = ($ScreenTooltipEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenTooltipEvents_ = $ScreenTooltipEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$FogEvents" {
import {$FogEvents$Render, $FogEvents$Render$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$FogEvents$Render"
import {$FogEvents$ComputeColor, $FogEvents$ComputeColor$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$FogEvents$ComputeColor"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $FogEvents {
static readonly "RENDER": $EventInvoker<($FogEvents$Render)>
static readonly "COMPUTE_COLOR": $EventInvoker<($FogEvents$ComputeColor)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FogEvents$Type = ($FogEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FogEvents_ = $FogEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/$ForgeClientFactories" {
import {$KeyMappingActivationHelper, $KeyMappingActivationHelper$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper"
import {$ItemDisplayOverridesImpl, $ItemDisplayOverridesImpl$Type} from "packages/fuzs/puzzleslib/impl/client/init/$ItemDisplayOverridesImpl"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ScreenHelper, $ScreenHelper$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$ScreenHelper"
import {$ClientFactories, $ClientFactories$Type} from "packages/fuzs/puzzleslib/impl/client/core/$ClientFactories"
import {$ClientModConstructor, $ClientModConstructor$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/$ClientModConstructor"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"

export class $ForgeClientFactories implements $ClientFactories {

constructor()

public "getKeyMappingActivationHelper"(): $KeyMappingActivationHelper
public "getItemModelDisplayOverrides"(): $ItemDisplayOverridesImpl
public "constructClientMod"(arg0: string, arg1: $ClientModConstructor$Type, arg2: $Set$Type<($ContentRegistrationFlags$Type)>, arg3: $Set$Type<($ContentRegistrationFlags$Type)>): void
public "getScreenHelper"(): $ScreenHelper
get "keyMappingActivationHelper"(): $KeyMappingActivationHelper
get "itemModelDisplayOverrides"(): $ItemDisplayOverridesImpl
get "screenHelper"(): $ScreenHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientFactories$Type = ($ForgeClientFactories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientFactories_ = $ForgeClientFactories$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble" {
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"

export interface $MutableDouble extends $DoubleConsumer, $DoubleSupplier {

 "mapDouble"(arg0: $DoubleUnaryOperator$Type): void
 "accept"(arg0: double): void
 "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
 "getAsDouble"(): double
}

export namespace $MutableDouble {
function fromValue(arg0: double): $MutableDouble
function fromEvent(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type): $MutableDouble
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableDouble$Type = ($MutableDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableDouble_ = $MutableDouble$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerTickEvents" {
import {$ServerTickEvents$Start, $ServerTickEvents$Start$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$ServerTickEvents$Start"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ServerTickEvents$End, $ServerTickEvents$End$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$ServerTickEvents$End"

export class $ServerTickEvents {
static readonly "START": $EventInvoker<($ServerTickEvents$Start)>
static readonly "END": $EventInvoker<($ServerTickEvents$End)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerTickEvents$Type = ($ServerTickEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerTickEvents_ = $ServerTickEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$KeyMappingsContext" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyMappingActivationHelper$KeyActivationContext, $KeyMappingActivationHelper$KeyActivationContext$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper$KeyActivationContext"

export interface $KeyMappingsContext {

/**
 * 
 * @deprecated
 */
 "registerKeyMapping"(...arg0: ($KeyMapping$Type)[]): void
 "registerKeyMapping"(arg0: $KeyMapping$Type, arg1: $KeyMappingActivationHelper$KeyActivationContext$Type): void

(...arg0: ($KeyMapping$Type)[]): void
}

export namespace $KeyMappingsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingsContext$Type = ($KeyMappingsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingsContext_ = $KeyMappingsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/json/$JsonSerializationUtil" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$FileReader, $FileReader$Type} from "packages/java/io/$FileReader"

export class $JsonSerializationUtil {
static readonly "FILE_FORMAT_STRING": string
static readonly "COMMENT_STRING": string

constructor()

public static "readFileFormat"(arg0: $JsonObject$Type): integer
public static "getConfigBase"(arg0: integer, ...arg1: (string)[]): $JsonObject
public static "getConfigBase"(...arg0: (string)[]): $JsonObject
public static "readJsonObject"(arg0: $FileReader$Type): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonSerializationUtil$Type = ($JsonSerializationUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonSerializationUtil_ = $JsonSerializationUtil$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Stop" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $UseItemEvents$Stop {

 "onUseItemStop"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: integer): $EventResult

(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: integer): $EventResult
}

export namespace $UseItemEvents$Stop {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemEvents$Stop$Type = ($UseItemEvents$Stop);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemEvents$Stop_ = $UseItemEvents$Stop$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ModContainerHelper" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$ModContainer, $ModContainer$Type} from "packages/net/minecraftforge/fml/$ModContainer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $ModContainerHelper {


public static "getOptionalModEventBus"(arg0: string): $Optional<($IEventBus)>
public static "getOptionalModContainer"(arg0: string): $Optional<(any)>
public static "getOptionalActiveModEventBus"(): $Optional<($IEventBus)>
public static "getActiveModEventBus"(): $IEventBus
/**
 * 
 * @deprecated
 */
public static "findModContainer"(arg0: string): $ModContainer
/**
 * 
 * @deprecated
 */
public static "findModEventBus"(arg0: string): $Optional<($IEventBus)>
public static "getModEventBus"(arg0: string): $IEventBus
public static "getModContainer"(arg0: string): $ModContainer
get "optionalActiveModEventBus"(): $Optional<($IEventBus)>
get "activeModEventBus"(): $IEventBus
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModContainerHelper$Type = ($ModContainerHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModContainerHelper_ = $ModContainerHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$AfterChangeDimension" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $PlayerEvents$AfterChangeDimension {

 "onAfterChangeDimension"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: $ServerLevel$Type): void

(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: $ServerLevel$Type): void
}

export namespace $PlayerEvents$AfterChangeDimension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$AfterChangeDimension$Type = ($PlayerEvents$AfterChangeDimension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$AfterChangeDimension_ = $PlayerEvents$AfterChangeDimension$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents" {
import {$ScreenEvents$AfterRender, $ScreenEvents$AfterRender$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$AfterRender"
import {$ScreenEvents$BeforeInitV2, $ScreenEvents$BeforeInitV2$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$BeforeInitV2"
import {$ScreenEvents$AfterInit, $ScreenEvents$AfterInit$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$AfterInit"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ScreenEvents$Remove, $ScreenEvents$Remove$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$Remove"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ScreenEvents$AfterInitV2, $ScreenEvents$AfterInitV2$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$AfterInitV2"
import {$ScreenEvents$BeforeRender, $ScreenEvents$BeforeRender$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$BeforeRender"
import {$ScreenEvents$BeforeInit, $ScreenEvents$BeforeInit$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$BeforeInit"

export class $ScreenEvents {
/**
 * 
 * @deprecated
 */
static readonly "BEFORE_INIT": $EventInvoker<($ScreenEvents$BeforeInit)>
/**
 * 
 * @deprecated
 */
static readonly "AFTER_INIT": $EventInvoker<($ScreenEvents$AfterInit)>


public static "beforeInit"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenEvents$BeforeInitV2<(T)>)>
public static "remove"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenEvents$Remove<(T)>)>
public static "afterInit"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenEvents$AfterInitV2<(T)>)>
public static "beforeRender"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenEvents$BeforeRender<(T)>)>
public static "afterRender"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenEvents$AfterRender<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$Type = ($ScreenEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents_ = $ScreenEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/data/$ExistingFileHelperHolder" {
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export interface $ExistingFileHelperHolder {

 "puzzleslib$setExistingFileHelper"(arg0: $ExistingFileHelper$Type): void

(arg0: $ExistingFileHelper$Type): void
}

export namespace $ExistingFileHelperHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExistingFileHelperHolder$Type = ($ExistingFileHelperHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExistingFileHelperHolder_ = $ExistingFileHelperHolder$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$AnimalTameCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Animal, $Animal$Type} from "packages/net/minecraft/world/entity/animal/$Animal"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $AnimalTameCallback {

 "onAnimalTame"(arg0: $Animal$Type, arg1: $Player$Type): $EventResult

(arg0: $Animal$Type, arg1: $Player$Type): $EventResult
}

export namespace $AnimalTameCallback {
const EVENT: $EventInvoker<($AnimalTameCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimalTameCallback$Type = ($AnimalTameCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimalTameCallback_ = $AnimalTameCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueDefaultedDouble" {
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$ValueMutableDouble, $ValueMutableDouble$Type} from "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableDouble"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$DefaultedDouble, $DefaultedDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedDouble"
import {$MutableDouble, $MutableDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble"

export class $ValueDefaultedDouble extends $ValueMutableDouble implements $DefaultedDouble {

constructor(arg0: double)

public "accept"(arg0: double): void
public "getAsDefaultDouble"(): double
public "getAsOptionalDouble"(): $OptionalDouble
public static "fromValue"(arg0: double): $DefaultedDouble
public static "fromEvent"(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type, arg2: $DoubleSupplier$Type): $DefaultedDouble
public "mapDefaultDouble"(arg0: $DoubleUnaryOperator$Type): void
public "applyDefaultDouble"(): void
public static "fromEvent"(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type): $MutableDouble
get "asDefaultDouble"(): double
get "asOptionalDouble"(): $OptionalDouble
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueDefaultedDouble$Type = ($ValueDefaultedDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueDefaultedDouble_ = $ValueDefaultedDouble$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents$FarmlandTrample" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BlockEvents$FarmlandTrample {

 "onFarmlandTrample"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: float, arg4: $Entity$Type): $EventResult

(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: float, arg4: $Entity$Type): $EventResult
}

export namespace $BlockEvents$FarmlandTrample {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvents$FarmlandTrample$Type = ($BlockEvents$FarmlandTrample);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvents$FarmlandTrample_ = $BlockEvents$FarmlandTrample$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$LayerDefinitionsContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$LayerDefinitionsContext, $LayerDefinitionsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LayerDefinitionsContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"

export class $LayerDefinitionsContextForgeImpl extends $Record implements $LayerDefinitionsContext {

constructor(consumer: $BiConsumer$Type<($ModelLayerLocation$Type), ($Supplier$Type<($LayerDefinition$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $BiConsumer<($ModelLayerLocation), ($Supplier<($LayerDefinition)>)>
public "registerLayerDefinition"(arg0: $ModelLayerLocation$Type, arg1: $Supplier$Type<($LayerDefinition$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayerDefinitionsContextForgeImpl$Type = ($LayerDefinitionsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayerDefinitionsContextForgeImpl_ = $LayerDefinitionsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $ExtendedMenuSupplier<T extends $AbstractContainerMenu> {

 "create"(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type): T

(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type): T
}

export namespace $ExtendedMenuSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedMenuSupplier$Type<T> = ($ExtendedMenuSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedMenuSupplier_<T> = $ExtendedMenuSupplier$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/$Config" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Config extends $Annotation {

 "name"(): string
 "description"(): (string)[]
 "category"(): (string)[]
 "worldRestart"(): boolean
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Config {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Config$Type = ($Config);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Config_ = $Config$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore" {
import {$ValueCallback, $ValueCallback$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ValueCallback"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export interface $ConfigCore {

 "addToBuilder"(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ValueCallback$Type): void
 "afterConfigReload"(): void
}

export namespace $ConfigCore {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCore$Type = ($ConfigCore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCore_ = $ConfigCore$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $DefaultedFloat extends $MutableFloat {

 "applyDefaultFloat"(): void
 "getAsOptionalFloat"(): $Optional<(float)>
 "mapDefaultFloat"(arg0: $UnaryOperator$Type<(float)>): void
 "getAsDefaultFloat"(): float
 "accept"(arg0: float): void
 "getAsFloat"(): float
 "mapFloat"(arg0: $UnaryOperator$Type<(float)>): void
}

export namespace $DefaultedFloat {
function fromValue(arg0: float): $DefaultedFloat
function fromEvent(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>, arg2: $Supplier$Type<(float)>): $DefaultedFloat
function fromEvent(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>): $MutableFloat
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedFloat$Type = ($DefaultedFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedFloat_ = $DefaultedFloat$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerXpEvents" {
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$PlayerXpEvents$PickupXp, $PlayerXpEvents$PickupXp$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerXpEvents$PickupXp"

export class $PlayerXpEvents {
static readonly "PICKUP_XP": $EventInvoker<($PlayerXpEvents$PickupXp)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerXpEvents$Type = ($PlayerXpEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerXpEvents_ = $PlayerXpEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/$ForgeEventInvokerRegistryImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$ForgeEventInvokerRegistry$ForgeEventContextConsumer, $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$ForgeEventInvokerRegistry$ForgeEventContextConsumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ForgeEventInvokerRegistry, $ForgeEventInvokerRegistry$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$ForgeEventInvokerRegistry"

export class $ForgeEventInvokerRegistryImpl implements $ForgeEventInvokerRegistry {

constructor()

public "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type<(T), (E)>, arg3: boolean): void
public static "registerLoadingHandlers"(): void
public static "registerEventHandlers"(): void
public "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $BiConsumer$Type<(T), (E)>): void
public "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $BiConsumer$Type<(T), (E)>, arg3: boolean): void
public "register"<T, E extends $Event>(arg0: $Class$Type<(T)>, arg1: $Class$Type<(E)>, arg2: $ForgeEventInvokerRegistry$ForgeEventContextConsumer$Type<(T), (E)>): void
public "register"<T>(arg0: $Class$Type<(T)>, arg1: $BiConsumer$Type<(T), (any)>): void
public "register"<T>(arg0: $Class$Type<(T)>, arg1: $BiConsumer$Type<(T), (any)>, arg2: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventInvokerRegistryImpl$Type = ($ForgeEventInvokerRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventInvokerRegistryImpl_ = $ForgeEventInvokerRegistryImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Unload" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerChunkEvents$Unload {

 "onChunkUnload"(arg0: $ServerLevel$Type, arg1: $LevelChunk$Type): void

(arg0: $ServerLevel$Type, arg1: $LevelChunk$Type): void
}

export namespace $ServerChunkEvents$Unload {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerChunkEvents$Unload$Type = ($ServerChunkEvents$Unload);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerChunkEvents$Unload_ = $ServerChunkEvents$Unload$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$ResourcePackSourcesContextForgeImpl" {
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"

export class $ResourcePackSourcesContextForgeImpl extends $Record implements $PackRepositorySourcesContext {

constructor(consumer: $Consumer$Type<($RepositorySource$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $Consumer<($RepositorySource)>
public "addRepositorySource"(...arg0: ($RepositorySource$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcePackSourcesContextForgeImpl$Type = ($ResourcePackSourcesContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcePackSourcesContextForgeImpl_ = $ResourcePackSourcesContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$GenerationSettingsContext" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ConfiguredWorldCarver, $ConfiguredWorldCarver$Type} from "packages/net/minecraft/world/level/levelgen/carver/$ConfiguredWorldCarver"
import {$GenerationStep$Carving, $GenerationStep$Carving$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Carving"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $GenerationSettingsContext {

 "getFeatures"(arg0: $GenerationStep$Decoration$Type): $Iterable<($Holder<($PlacedFeature)>)>
 "removeFeature"(arg0: $ResourceKey$Type<($PlacedFeature$Type)>): boolean
 "removeFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $ResourceKey$Type<($PlacedFeature$Type)>): boolean
 "addFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $ResourceKey$Type<($PlacedFeature$Type)>): void
 "getCarvers"(arg0: $GenerationStep$Carving$Type): $Iterable<($Holder<($ConfiguredWorldCarver<(any)>)>)>
 "addCarver"(arg0: $GenerationStep$Carving$Type, arg1: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): void
 "removeCarver"(arg0: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): boolean
 "removeCarver"(arg0: $GenerationStep$Carving$Type, arg1: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): boolean
}

export namespace $GenerationSettingsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenerationSettingsContext$Type = ($GenerationSettingsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenerationSettingsContext_ = $GenerationSettingsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ReflectionHelper" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ReflectionHelper {

constructor()

public static "getValue"<T>(arg0: $Field$Type, arg1: any): $Optional<(T)>
public static "getValue"<T, E>(arg0: string, arg1: string, arg2: E): $Optional<(T)>
public static "getValue"<T, E>(arg0: $Class$Type<(any)>, arg1: string, arg2: E): $Optional<(T)>
public static "newInstance"<T>(arg0: $Constructor$Type<(T)>, ...arg1: (any)[]): $Optional<(T)>
public static "newInstance"<T, E>(arg0: $Class$Type<(any)>, arg1: ($Class$Type<(any)>)[], arg2: (any)[]): $Optional<(T)>
public static "setValue"<T, E>(arg0: string, arg1: string, arg2: E, arg3: T): boolean
public static "setValue"<T>(arg0: $Field$Type, arg1: any, arg2: T): boolean
public static "setValue"<T, E>(arg0: $Class$Type<(any)>, arg1: string, arg2: E, arg3: T): boolean
public static "findConstructor"<T>(arg0: string, arg1: boolean, ...arg2: ($Class$Type<(any)>)[]): $Constructor<(T)>
public static "findConstructor"<T>(arg0: $Class$Type<(any)>, ...arg1: ($Class$Type<(any)>)[]): $Constructor<(T)>
public static "findConstructor"<T>(arg0: $Class$Type<(any)>, arg1: boolean, ...arg2: ($Class$Type<(any)>)[]): $Constructor<(T)>
public static "findMethod"(arg0: string, arg1: string, arg2: boolean, ...arg3: ($Class$Type<(any)>)[]): $Method
public static "findMethod"(arg0: $Class$Type<(any)>, arg1: string, ...arg2: ($Class$Type<(any)>)[]): $Method
public static "findMethod"(arg0: $Class$Type<(any)>, arg1: string, arg2: boolean, ...arg3: ($Class$Type<(any)>)[]): $Method
public static "invokeMethod"<T>(arg0: $Method$Type, arg1: any, ...arg2: (any)[]): $Optional<(T)>
public static "invokeMethod"<T, E>(arg0: $Class$Type<(any)>, arg1: string, arg2: ($Class$Type<(any)>)[], arg3: E, arg4: (any)[]): $Optional<(T)>
public static "findField"(arg0: string, arg1: string, arg2: boolean): $Field
public static "findField"(arg0: $Class$Type<(any)>, arg1: string, arg2: boolean): $Field
public static "findField"(arg0: $Class$Type<(any)>, arg1: string): $Field
public static "newInstanceFactory"<T>(arg0: $Class$Type<(T)>, arg1: ($Class$Type<(any)>)[], arg2: (any)[]): $Supplier<($Optional<(T)>)>
public static "newInstanceFactory"<T>(arg0: $Constructor$Type<(T)>, ...arg1: (any)[]): $Supplier<($Optional<(T)>)>
public static "newDefaultInstanceFactory"<T>(arg0: $Class$Type<(T)>): $Supplier<($Optional<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReflectionHelper$Type = ($ReflectionHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReflectionHelper_ = $ReflectionHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$SpecialEffectsContext" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Music, $Music$Type} from "packages/net/minecraft/sounds/$Music"
import {$AmbientParticleSettings, $AmbientParticleSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientParticleSettings"
import {$AmbientMoodSettings, $AmbientMoodSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientMoodSettings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AmbientAdditionsSettings, $AmbientAdditionsSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientAdditionsSettings"
import {$BiomeSpecialEffects$GrassColorModifier, $BiomeSpecialEffects$GrassColorModifier$Type} from "packages/net/minecraft/world/level/biome/$BiomeSpecialEffects$GrassColorModifier"

export interface $SpecialEffectsContext {

 "getGrassColorOverride"(): $Optional<(integer)>
 "getFoliageColorOverride"(): $Optional<(integer)>
 "getGrassColorModifier"(): $BiomeSpecialEffects$GrassColorModifier
 "setGrassColorOverride"(arg0: integer): void
 "setGrassColorOverride"(arg0: $Optional$Type<(integer)>): void
 "setGrassColorModifier"(arg0: $BiomeSpecialEffects$GrassColorModifier$Type): void
 "setFoliageColorOverride"(arg0: $Optional$Type<(integer)>): void
 "setFoliageColorOverride"(arg0: integer): void
 "clearFoliageColorOverride"(): void
 "setAmbientParticleSettings"(arg0: $AmbientParticleSettings$Type): void
 "setAmbientParticleSettings"(arg0: $Optional$Type<($AmbientParticleSettings$Type)>): void
 "getAmbientParticleSettings"(): $Optional<($AmbientParticleSettings)>
 "clearGrassColorOverride"(): void
 "setAmbientLoopSoundEvent"(arg0: $Optional$Type<($Holder$Type<($SoundEvent$Type)>)>): void
 "setAmbientLoopSoundEvent"(arg0: $Holder$Type<($SoundEvent$Type)>): void
 "getAmbientLoopSoundEvent"(): $Optional<($Holder<($SoundEvent)>)>
 "clearAmbientParticleSettings"(): void
 "setAmbientMoodSettings"(arg0: $Optional$Type<($AmbientMoodSettings$Type)>): void
 "setAmbientMoodSettings"(arg0: $AmbientMoodSettings$Type): void
 "clearAmbientLoopSoundEvent"(): void
 "clearAmbientMoodSettings"(): void
 "getAmbientMoodSettings"(): $Optional<($AmbientMoodSettings)>
 "clearAmbientAdditionsSettings"(): void
 "setAmbientAdditionsSettings"(arg0: $AmbientAdditionsSettings$Type): void
 "setAmbientAdditionsSettings"(arg0: $Optional$Type<($AmbientAdditionsSettings$Type)>): void
 "getAmbientAdditionsSettings"(): $Optional<($AmbientAdditionsSettings)>
 "clearBackgroundMusic"(): void
 "getSkyColor"(): integer
 "getWaterFogColor"(): integer
 "getBackgroundMusic"(): $Optional<($Music)>
 "setWaterFogColor"(arg0: integer): void
 "setSkyColor"(arg0: integer): void
 "setWaterColor"(arg0: integer): void
 "setFogColor"(arg0: integer): void
 "setBackgroundMusic"(arg0: $Optional$Type<($Music$Type)>): void
 "setBackgroundMusic"(arg0: $Music$Type): void
 "getWaterColor"(): integer
 "getFogColor"(): integer
}

export namespace $SpecialEffectsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialEffectsContext$Type = ($SpecialEffectsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialEffectsContext_ = $SpecialEffectsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/client/accessor/$TooltipAccessor" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $TooltipAccessor {

 "puzzleslib$setCachedTooltip"(arg0: $List$Type<($FormattedCharSequence$Type)>): void

(arg0: $List$Type<($FormattedCharSequence$Type)>): void
}

export namespace $TooltipAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipAccessor$Type = ($TooltipAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipAccessor_ = $TooltipAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseBlock" {
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export interface $PlayerInteractEvents$UseBlock {

 "onUseBlock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $EventResultHolder<($InteractionResult)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $EventResultHolder<($InteractionResult)>
}

export namespace $PlayerInteractEvents$UseBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$UseBlock$Type = ($PlayerInteractEvents$UseBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$UseBlock_ = $PlayerInteractEvents$UseBlock$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/screen/v2/$ScreenElementPositioner" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LayoutElement, $LayoutElement$Type} from "packages/net/minecraft/client/gui/layouts/$LayoutElement"

export class $ScreenElementPositioner {


public static "tryPositionElement"(arg0: $LayoutElement$Type, arg1: $List$Type<(any)>, arg2: boolean, arg3: integer, ...arg4: (string)[]): boolean
public static "tryPositionElement"(arg0: $LayoutElement$Type, arg1: $List$Type<(any)>, arg2: boolean, ...arg3: (string)[]): boolean
public static "tryPositionElement"(arg0: $LayoutElement$Type, arg1: $List$Type<(any)>, ...arg2: (string)[]): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenElementPositioner$Type = ($ScreenElementPositioner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenElementPositioner_ = $ScreenElementPositioner$Type;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$MobSpawnSettingsContext" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$MobSpawnSettings$SpawnerData, $MobSpawnSettings$SpawnerData$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$SpawnerData"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobSpawnSettings$MobSpawnCost, $MobSpawnSettings$MobSpawnCost$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$MobSpawnCost"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"

export interface $MobSpawnSettingsContext {

 "addSpawn"(arg0: $MobCategory$Type, arg1: $MobSpawnSettings$SpawnerData$Type): void
 "removeSpawns"(arg0: $BiPredicate$Type<($MobCategory$Type), ($MobSpawnSettings$SpawnerData$Type)>): boolean
 "getSpawnerData"(arg0: $MobCategory$Type): $List<($MobSpawnSettings$SpawnerData)>
 "getSpawnCost"(arg0: $EntityType$Type<(any)>): $MobSpawnSettings$MobSpawnCost
 "clearSpawns"(arg0: $MobCategory$Type): void
 "clearSpawns"(): void
 "clearSpawnCost"(arg0: $EntityType$Type<(any)>): boolean
 "setSpawnCost"(arg0: $EntityType$Type<(any)>, arg1: double, arg2: double): void
 "getCreatureGenerationProbability"(): float
 "removeSpawnsOfEntityType"(arg0: $EntityType$Type<(any)>): boolean
 "getMobCategoriesWithSpawns"(): $Set<($MobCategory)>
 "getEntityTypesWithSpawnCost"(): $Set<($EntityType<(any)>)>
 "setCreatureGenerationProbability"(arg0: float): void
}

export namespace $MobSpawnSettingsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnSettingsContext$Type = ($MobSpawnSettingsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnSettingsContext_ = $MobSpawnSettingsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelEvents$Load" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientLevelEvents$Load {

 "onLevelLoad"(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void

(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void
}

export namespace $ClientLevelEvents$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelEvents$Load$Type = ($ClientLevelEvents$Load);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelEvents$Load_ = $ClientLevelEvents$Load$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelEvents" {
import {$ServerLevelEvents$Unload, $ServerLevelEvents$Unload$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelEvents$Unload"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ServerLevelEvents$Load, $ServerLevelEvents$Load$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelEvents$Load"

export class $ServerLevelEvents {
static readonly "LOAD": $EventInvoker<($ServerLevelEvents$Load)>
static readonly "UNLOAD": $EventInvoker<($ServerLevelEvents$Unload)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelEvents$Type = ($ServerLevelEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelEvents_ = $ServerLevelEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$AttackEntity" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerInteractEvents$AttackEntity {

 "onAttackEntity"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type): $EventResult

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type): $EventResult
}

export namespace $PlayerInteractEvents$AttackEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$AttackEntity$Type = ($PlayerInteractEvents$AttackEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$AttackEntity_ = $PlayerInteractEvents$AttackEntity$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$PlayLevelSoundEvents$AtEntity" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$SoundSource, $SoundSource$Type} from "packages/net/minecraft/sounds/$SoundSource"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayLevelSoundEvents$AtEntity {

 "onPlaySoundAtEntity"(arg0: $Level$Type, arg1: $Entity$Type, arg2: $MutableValue$Type<($Holder$Type<($SoundEvent$Type)>)>, arg3: $MutableValue$Type<($SoundSource$Type)>, arg4: $DefaultedFloat$Type, arg5: $DefaultedFloat$Type): $EventResult

(arg0: $Level$Type, arg1: $Entity$Type, arg2: $MutableValue$Type<($Holder$Type<($SoundEvent$Type)>)>, arg3: $MutableValue$Type<($SoundSource$Type)>, arg4: $DefaultedFloat$Type, arg5: $DefaultedFloat$Type): $EventResult
}

export namespace $PlayLevelSoundEvents$AtEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayLevelSoundEvents$AtEntity$Type = ($PlayLevelSoundEvents$AtEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayLevelSoundEvents$AtEntity_ = $PlayLevelSoundEvents$AtEntity$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractSoundDefinitionProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$SoundDefinitionsProvider, $SoundDefinitionsProvider$Type} from "packages/net/minecraftforge/common/data/$SoundDefinitionsProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $AbstractSoundDefinitionProvider extends $SoundDefinitionsProvider {

/**
 * 
 * @deprecated
 */
constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type)
constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type, arg2: string)
constructor(arg0: $GatherDataEvent$Type, arg1: string)

public "registerSounds"(): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSoundDefinitionProvider$Type = ($AbstractSoundDefinitionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSoundDefinitionProvider_ = $AbstractSoundDefinitionProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ExplosionEvents" {
import {$ExplosionEvents$Detonate, $ExplosionEvents$Detonate$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ExplosionEvents$Detonate"
import {$ExplosionEvents$Start, $ExplosionEvents$Start$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ExplosionEvents$Start"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ExplosionEvents {
static readonly "START": $EventInvoker<($ExplosionEvents$Start)>
static readonly "DETONATE": $EventInvoker<($ExplosionEvents$Detonate)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionEvents$Type = ($ExplosionEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionEvents_ = $ExplosionEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LootingLevelCallback" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LootingLevelCallback {

 "onLootingLevel"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $MutableInt$Type): void

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $MutableInt$Type): void
}

export namespace $LootingLevelCallback {
const EVENT: $EventInvoker<($LootingLevelCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootingLevelCallback$Type = ($LootingLevelCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootingLevelCallback_ = $LootingLevelCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/$LootTableModifyEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$LootPool, $LootPool$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool"
import {$LootDataManager, $LootDataManager$Type} from "packages/net/minecraft/world/level/storage/loot/$LootDataManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"

export class $LootTableModifyEvent extends $Event {

constructor()
constructor(arg0: $LootDataManager$Type, arg1: $ResourceLocation$Type, arg2: $LootTable$Type)

public "getIdentifier"(): $ResourceLocation
public "isCancelable"(): boolean
public "addPool"(arg0: $LootPool$Type): void
public "removePool"(arg0: integer): boolean
public "getLootDataManager"(): $LootDataManager
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "identifier"(): $ResourceLocation
get "cancelable"(): boolean
get "lootDataManager"(): $LootDataManager
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableModifyEvent$Type = ($LootTableModifyEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableModifyEvent_ = $LootTableModifyEvent$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents" {
import {$ScreenMouseEvents$BeforeMouseRelease, $ScreenMouseEvents$BeforeMouseRelease$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseRelease"
import {$ScreenMouseEvents$BeforeMouseScroll, $ScreenMouseEvents$BeforeMouseScroll$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseScroll"
import {$ScreenMouseEvents$AfterMouseDrag, $ScreenMouseEvents$AfterMouseDrag$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseDrag"
import {$ScreenMouseEvents$BeforeMouseDrag, $ScreenMouseEvents$BeforeMouseDrag$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseDrag"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScreenMouseEvents$AfterMouseClick, $ScreenMouseEvents$AfterMouseClick$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseClick"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ScreenMouseEvents$AfterMouseScroll, $ScreenMouseEvents$AfterMouseScroll$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseScroll"
import {$ScreenMouseEvents$BeforeMouseClick, $ScreenMouseEvents$BeforeMouseClick$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseClick"
import {$ScreenMouseEvents$AfterMouseRelease, $ScreenMouseEvents$AfterMouseRelease$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseRelease"

export class $ScreenMouseEvents {


public static "afterMouseClick"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$AfterMouseClick<(T)>)>
public static "afterMouseDrag"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$AfterMouseDrag<(T)>)>
public static "beforeMouseScroll"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$BeforeMouseScroll<(T)>)>
public static "afterMouseScroll"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$AfterMouseScroll<(T)>)>
public static "beforeMouseClick"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$BeforeMouseClick<(T)>)>
public static "afterMouseRelease"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$AfterMouseRelease<(T)>)>
public static "beforeMouseRelease"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$BeforeMouseRelease<(T)>)>
public static "beforeMouseDrag"<T extends $Screen>(arg0: $Class$Type<(T)>): $EventInvoker<($ScreenMouseEvents$BeforeMouseDrag<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$Type = ($ScreenMouseEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents_ = $ScreenMouseEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$BeforeMouseAction" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $InputEvents$BeforeMouseAction {

 "onBeforeMouseAction"(arg0: integer, arg1: integer, arg2: integer): $EventResult

(arg0: integer, arg1: integer, arg2: integer): $EventResult
}

export namespace $InputEvents$BeforeMouseAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$BeforeMouseAction$Type = ($InputEvents$BeforeMouseAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents$BeforeMouseAction_ = $InputEvents$BeforeMouseAction$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$LootTableLoadEvents$Replace" {
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"

export interface $LootTableLoadEvents$Replace {

 "onReplaceLootTable"(arg0: $ResourceLocation$Type, arg1: $MutableValue$Type<($LootTable$Type)>): void

(arg0: $ResourceLocation$Type, arg1: $MutableValue$Type<($LootTable$Type)>): void
}

export namespace $LootTableLoadEvents$Replace {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableLoadEvents$Replace$Type = ($LootTableLoadEvents$Replace);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableLoadEvents$Replace_ = $LootTableLoadEvents$Replace$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$RegistryManagerV3Impl" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ModLoader, $ModLoader$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoader"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$MenuType$MenuSupplier, $MenuType$MenuSupplier$Type} from "packages/net/minecraft/world/inventory/$MenuType$MenuSupplier"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SimpleParticleType, $SimpleParticleType$Type} from "packages/net/minecraft/core/particles/$SimpleParticleType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$BlockEntityType$Builder, $BlockEntityType$Builder$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$Builder"
import {$RegistryManager, $RegistryManager$Type} from "packages/fuzs/puzzleslib/api/init/v3/$RegistryManager"
import {$ExtendedMenuSupplier, $ExtendedMenuSupplier$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $RegistryManagerV3Impl implements $RegistryManager {


public "register"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string, arg2: $Supplier$Type<(T)>): $Holder$Reference<(T)>
public "makeKey"(arg0: string): $ResourceLocation
public "whenOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
public static "from"(arg0: string): $RegistryManager
public "registerBlockEntityType"<T extends $BlockEntity>(arg0: string, arg1: $Supplier$Type<($BlockEntityType$Builder$Type<(T)>)>): $Holder$Reference<($BlockEntityType<(T)>)>
public "registerEnchantment"(arg0: string, arg1: $Supplier$Type<($Enchantment$Type)>): $Holder$Reference<($Enchantment)>
public "registerBlock"(arg0: string, arg1: $Supplier$Type<($Block$Type)>): $Holder$Reference<($Block)>
public "registerRecipeType"<T extends $Recipe<(any)>>(arg0: string): $Holder$Reference<($RecipeType<(T)>)>
public "registerMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($MenuType$MenuSupplier$Type<(T)>)>): $Holder$Reference<($MenuType<(T)>)>
public "makeResourceKey"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $ResourceKey<(T)>
public "registerGameEvent"(arg0: string, arg1: integer): $Holder$Reference<($GameEvent)>
public "whenNotOn"(...arg0: ($ModLoader$Type)[]): $RegistryManager
public "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Block$Type)>): $Holder$Reference<($PoiType)>
public "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($BlockState$Type)>)>, arg2: integer, arg3: integer): $Holder$Reference<($PoiType)>
public "registerDamageType"(arg0: string): $ResourceKey<($DamageType)>
public "registerFluid"(arg0: string, arg1: $Supplier$Type<($Fluid$Type)>): $Holder$Reference<($Fluid)>
public "registerItem"(arg0: string, arg1: $Supplier$Type<($Item$Type)>): $Holder$Reference<($Item)>
public "registerEntityType"<T extends $Entity>(arg0: string, arg1: $Supplier$Type<($EntityType$Builder$Type<(T)>)>): $Holder$Reference<($EntityType<(T)>)>
public "registerMobEffect"(arg0: string, arg1: $Supplier$Type<($MobEffect$Type)>): $Holder$Reference<($MobEffect)>
public "registerPotion"(arg0: string, arg1: $Supplier$Type<($Potion$Type)>): $Holder$Reference<($Potion)>
public "registerSoundEvent"(arg0: string): $Holder$Reference<($SoundEvent)>
public "registerBlockItem"(arg0: $Holder$Reference$Type<($Block$Type)>): $Holder$Reference<($Item)>
public "registerBlockItem"(arg0: $Holder$Reference$Type<($Block$Type)>, arg1: $Item$Properties$Type): $Holder$Reference<($Item)>
public "registerSpawnEggItem"(arg0: $Holder$Reference$Type<(any)>, arg1: integer, arg2: integer): $Holder$Reference<($Item)>
public "registerSpawnEggItem"(arg0: $Holder$Reference$Type<(any)>, arg1: integer, arg2: integer, arg3: $Item$Properties$Type): $Holder$Reference<($Item)>
public "registerExtendedMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($ExtendedMenuSupplier$Type<(T)>)>): $Holder$Reference<($MenuType<(T)>)>
public "registerParticleType"(arg0: string): $Holder$Reference<($SimpleParticleType)>
public "getHolder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $Holder$Reference<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryManagerV3Impl$Type = ($RegistryManagerV3Impl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryManagerV3Impl_ = $RegistryManagerV3Impl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/$ConfigHolderImpl" {
import {$ConfigDataHolder, $ConfigDataHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigDataHolder"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigHolder$Builder, $ConfigHolder$Builder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder"

export class $ConfigHolderImpl implements $ConfigHolder$Builder {


public "build"(): void
public "common"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigHolder$Builder
public "client"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigHolder$Builder
public "server"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigHolder$Builder
public "setFileName"<T extends $ConfigCore>(arg0: $Class$Type<(T)>, arg1: $UnaryOperator$Type<(string)>): $ConfigHolder$Builder
public "getHolder"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigDataHolder<(T)>
/**
 * 
 * @deprecated
 */
public "get"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): T
public static "builder"(arg0: string): $ConfigHolder$Builder
public static "simpleName"(arg0: string): string
public static "moveToDir"(arg0: string, arg1: string): string
public static "defaultName"(arg0: string, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHolderImpl$Type = ($ConfigHolderImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHolderImpl_ = $ConfigHolderImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiCallback" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $RenderGuiCallback {

 "onRenderGui"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void

(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void
}

export namespace $RenderGuiCallback {
const EVENT: $EventInvoker<($RenderGuiCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderGuiCallback$Type = ($RenderGuiCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderGuiCallback_ = $RenderGuiCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$GatherPotentialSpawnsCallback" {
import {$MobSpawnSettings$SpawnerData, $MobSpawnSettings$SpawnerData$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$SpawnerData"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $GatherPotentialSpawnsCallback {

 "onGatherPotentialSpawns"(arg0: $ServerLevel$Type, arg1: $StructureManager$Type, arg2: $ChunkGenerator$Type, arg3: $MobCategory$Type, arg4: $BlockPos$Type, arg5: $List$Type<($MobSpawnSettings$SpawnerData$Type)>): void

(arg0: $ServerLevel$Type, arg1: $StructureManager$Type, arg2: $ChunkGenerator$Type, arg3: $MobCategory$Type, arg4: $BlockPos$Type, arg5: $List$Type<($MobSpawnSettings$SpawnerData$Type)>): void
}

export namespace $GatherPotentialSpawnsCallback {
const EVENT: $EventInvoker<($GatherPotentialSpawnsCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherPotentialSpawnsCallback$Type = ($GatherPotentialSpawnsCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherPotentialSpawnsCallback_ = $GatherPotentialSpawnsCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/client/accessor/$BlockColorsForgeAccessor" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $BlockColorsForgeAccessor {

 "puzzleslib$getBlockColors"(): $Map<($Holder$Reference<($Block)>), ($BlockColor)>

(): $Map<($Holder$Reference<($Block)>), ($BlockColor)>
}

export namespace $BlockColorsForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockColorsForgeAccessor$Type = ($BlockColorsForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockColorsForgeAccessor_ = $BlockColorsForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents" {
import {$RenderLevelEvents$AfterTerrain, $RenderLevelEvents$AfterTerrain$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterTerrain"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$RenderLevelEvents$AfterTranslucent, $RenderLevelEvents$AfterTranslucent$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterTranslucent"
import {$RenderLevelEvents$AfterLevel, $RenderLevelEvents$AfterLevel$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterLevel"
import {$RenderLevelEvents$AfterEntities, $RenderLevelEvents$AfterEntities$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterEntities"

export class $RenderLevelEvents {
static readonly "AFTER_TERRAIN": $EventInvoker<($RenderLevelEvents$AfterTerrain)>
static readonly "AFTER_ENTITIES": $EventInvoker<($RenderLevelEvents$AfterEntities)>
static readonly "AFTER_TRANSLUCENT": $EventInvoker<($RenderLevelEvents$AfterTranslucent)>
static readonly "AFTER_LEVEL": $EventInvoker<($RenderLevelEvents$AfterLevel)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEvents$Type = ($RenderLevelEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEvents_ = $RenderLevelEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeProxy" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ProxyImpl, $ProxyImpl$Type} from "packages/fuzs/puzzleslib/impl/core/$ProxyImpl"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"

export interface $ForgeProxy extends $ProxyImpl {

 "registerServerReceiverV2"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
 "registerClientReceiverV2"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
 "hasShiftDown"(): boolean
 "getClientLevel"(): $Level
 "hasControlDown"(): boolean
 "hasAltDown"(): boolean
 "getClientPlayer"(): $Player
 "getClientPacketListener"(): $ClientPacketListener
 "getKeyMappingComponent"(arg0: string): $Component
/**
 * 
 * @deprecated
 */
 "getGameServer"(): $MinecraftServer
}

export namespace $ForgeProxy {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeProxy$Type = ($ForgeProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeProxy_ = $ForgeProxy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$FogEvents$ComputeColor" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"

export interface $FogEvents$ComputeColor {

 "onComputeFogColor"(arg0: $GameRenderer$Type, arg1: $Camera$Type, arg2: float, arg3: $MutableFloat$Type, arg4: $MutableFloat$Type, arg5: $MutableFloat$Type): void

(arg0: $GameRenderer$Type, arg1: $Camera$Type, arg2: float, arg3: $MutableFloat$Type, arg4: $MutableFloat$Type, arg5: $MutableFloat$Type): void
}

export namespace $FogEvents$ComputeColor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FogEvents$ComputeColor$Type = ($FogEvents$ComputeColor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FogEvents$ComputeColor_ = $FogEvents$ComputeColor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderPlayerEvents$After" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerRenderer, $PlayerRenderer$Type} from "packages/net/minecraft/client/renderer/entity/player/$PlayerRenderer"

export interface $RenderPlayerEvents$After {

 "onAfterRenderPlayer"(arg0: $Player$Type, arg1: $PlayerRenderer$Type, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void

(arg0: $Player$Type, arg1: $PlayerRenderer$Type, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void
}

export namespace $RenderPlayerEvents$After {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderPlayerEvents$After$Type = ($RenderPlayerEvents$After);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderPlayerEvents$After_ = $RenderPlayerEvents$After$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/client/accessor/$ItemColorsForgeAccessor" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ItemColorsForgeAccessor {

 "puzzleslib$getItemColors"(): $Map<($Holder$Reference<($Item)>), ($ItemColor)>

(): $Map<($Holder$Reference<($Item)>), ($ItemColor)>
}

export namespace $ItemColorsForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemColorsForgeAccessor$Type = ($ItemColorsForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemColorsForgeAccessor_ = $ItemColorsForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/screen/v2/$TooltipRenderHelper" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ClientTooltipComponent, $ClientTooltipComponent$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipComponent"

export class $TooltipRenderHelper {


public static "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $List$Type<($Component$Type)>, arg4: $TooltipComponent$Type): void
public static "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Component$Type, arg4: $TooltipComponent$Type): void
public static "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $ItemStack$Type): void
public static "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $List$Type<($Component$Type)>): void
public static "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $List$Type<($Component$Type)>, arg4: $List$Type<($TooltipComponent$Type)>): void
public static "getTooltipLines"(arg0: $ItemStack$Type): $List<($Component)>
public static "getTooltipLines"(arg0: $ItemStack$Type, arg1: $TooltipFlag$Type): $List<($Component)>
public static "getTooltip"(arg0: $ItemStack$Type, arg1: $TooltipFlag$Type): $List<($ClientTooltipComponent)>
public static "getTooltip"(arg0: $ItemStack$Type): $List<($ClientTooltipComponent)>
public static "renderTooltipInternal"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $List$Type<($ClientTooltipComponent$Type)>): void
public static "createClientComponents"(arg0: $List$Type<($Component$Type)>, arg1: $List$Type<($TooltipComponent$Type)>): $List<($ClientTooltipComponent)>
public static "createClientComponents"(arg0: $List$Type<($Component$Type)>, arg1: $List$Type<($TooltipComponent$Type)>, arg2: integer): $List<($ClientTooltipComponent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipRenderHelper$Type = ($TooltipRenderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipRenderHelper_ = $TooltipRenderHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueDefaultedFloat" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$ValueMutableFloat, $ValueMutableFloat$Type} from "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableFloat"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"

export class $ValueDefaultedFloat extends $ValueMutableFloat implements $DefaultedFloat {

constructor(arg0: float)

public "accept"(arg0: float): void
public "getAsOptionalFloat"(): $Optional<(float)>
public "getAsDefaultFloat"(): float
public static "fromValue"(arg0: float): $DefaultedFloat
public "applyDefaultFloat"(): void
public "mapDefaultFloat"(arg0: $UnaryOperator$Type<(float)>): void
public static "fromEvent"(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>, arg2: $Supplier$Type<(float)>): $DefaultedFloat
public static "fromEvent"(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>): $MutableFloat
get "asOptionalFloat"(): $Optional<(float)>
get "asDefaultFloat"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueDefaultedFloat$Type = ($ValueDefaultedFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueDefaultedFloat_ = $ValueDefaultedFloat$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientEntityLevelEvents$Unload" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ClientEntityLevelEvents$Unload {

 "onEntityUnload"(arg0: $Entity$Type, arg1: $ClientLevel$Type): void

(arg0: $Entity$Type, arg1: $ClientLevel$Type): void
}

export namespace $ClientEntityLevelEvents$Unload {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEntityLevelEvents$Unload$Type = ($ClientEntityLevelEvents$Unload);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEntityLevelEvents$Unload_ = $ClientEntityLevelEvents$Unload$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$ItemModelOverrides" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$ModelResourceLocation, $ModelResourceLocation$Type} from "packages/net/minecraft/client/resources/model/$ModelResourceLocation"

/**
 * 
 * @deprecated
 */
export interface $ItemModelOverrides {

 "register"(arg0: $Item$Type, arg1: $ModelResourceLocation$Type, arg2: $ModelResourceLocation$Type, ...arg3: ($ItemDisplayContext$Type)[]): void

(arg0: $Item$Type, arg1: $ModelResourceLocation$Type, arg2: $ModelResourceLocation$Type, ...arg3: ($ItemDisplayContext$Type)[]): void
}

export namespace $ItemModelOverrides {
const INSTANCE: $ItemModelOverrides
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelOverrides$Type = ($ItemModelOverrides);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelOverrides_ = $ItemModelOverrides$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/$SyncStrategyImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$SyncStrategy, $SyncStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncStrategy"

export class $SyncStrategyImpl implements $SyncStrategy {

constructor<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BiConsumer$Type<(T), ($ServerPlayer$Type)>)

public "sendTo"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T, arg1: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncStrategyImpl$Type = ($SyncStrategyImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncStrategyImpl_ = $SyncStrategyImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicItemDecorator" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $DynamicItemDecorator {

 "renderItemDecorations"(arg0: $GuiGraphics$Type, arg1: $Font$Type, arg2: $ItemStack$Type, arg3: integer, arg4: integer): boolean

(arg0: $GuiGraphics$Type, arg1: $Font$Type, arg2: $ItemStack$Type, arg3: integer, arg4: integer): boolean
}

export namespace $DynamicItemDecorator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicItemDecorator$Type = ($DynamicItemDecorator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicItemDecorator_ = $DynamicItemDecorator$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$ServerEntityLevelEvents$LoadV2" {
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ServerEntityLevelEvents$LoadV2 {

 "onEntityLoad"(arg0: $Entity$Type, arg1: $ServerLevel$Type): $EventResult

(arg0: $Entity$Type, arg1: $ServerLevel$Type): $EventResult
}

export namespace $ServerEntityLevelEvents$LoadV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityLevelEvents$LoadV2$Type = ($ServerEntityLevelEvents$LoadV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityLevelEvents$LoadV2_ = $ServerEntityLevelEvents$LoadV2$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents" {
import {$RenderGuiElementEvents$GuiOverlay, $RenderGuiElementEvents$GuiOverlay$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents$GuiOverlay"
import {$RenderGuiElementEvents$After, $RenderGuiElementEvents$After$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents$After"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$RenderGuiElementEvents$Before, $RenderGuiElementEvents$Before$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents$Before"

export class $RenderGuiElementEvents {
static readonly "VIGNETTE": $RenderGuiElementEvents$GuiOverlay
static readonly "SPYGLASS": $RenderGuiElementEvents$GuiOverlay
static readonly "HELMET": $RenderGuiElementEvents$GuiOverlay
static readonly "FROSTBITE": $RenderGuiElementEvents$GuiOverlay
static readonly "PORTAL": $RenderGuiElementEvents$GuiOverlay
static readonly "HOTBAR": $RenderGuiElementEvents$GuiOverlay
static readonly "CROSSHAIR": $RenderGuiElementEvents$GuiOverlay
static readonly "BOSS_EVENT_PROGRESS": $RenderGuiElementEvents$GuiOverlay
static readonly "PLAYER_HEALTH": $RenderGuiElementEvents$GuiOverlay
static readonly "ARMOR_LEVEL": $RenderGuiElementEvents$GuiOverlay
static readonly "FOOD_LEVEL": $RenderGuiElementEvents$GuiOverlay
static readonly "MOUNT_HEALTH": $RenderGuiElementEvents$GuiOverlay
static readonly "AIR_LEVEL": $RenderGuiElementEvents$GuiOverlay
static readonly "JUMP_BAR": $RenderGuiElementEvents$GuiOverlay
static readonly "EXPERIENCE_BAR": $RenderGuiElementEvents$GuiOverlay
static readonly "ITEM_NAME": $RenderGuiElementEvents$GuiOverlay
static readonly "SLEEP_FADE": $RenderGuiElementEvents$GuiOverlay
static readonly "DEBUG_TEXT": $RenderGuiElementEvents$GuiOverlay
static readonly "FPS_GRAPH": $RenderGuiElementEvents$GuiOverlay
static readonly "POTION_ICONS": $RenderGuiElementEvents$GuiOverlay
static readonly "RECORD_OVERLAY": $RenderGuiElementEvents$GuiOverlay
static readonly "SUBTITLES": $RenderGuiElementEvents$GuiOverlay
static readonly "TITLE_TEXT": $RenderGuiElementEvents$GuiOverlay
static readonly "SCOREBOARD": $RenderGuiElementEvents$GuiOverlay
static readonly "CHAT_PANEL": $RenderGuiElementEvents$GuiOverlay
static readonly "PLAYER_LIST": $RenderGuiElementEvents$GuiOverlay


public static "before"(arg0: $RenderGuiElementEvents$GuiOverlay$Type): $EventInvoker<($RenderGuiElementEvents$Before)>
public static "after"(arg0: $RenderGuiElementEvents$GuiOverlay$Type): $EventInvoker<($RenderGuiElementEvents$After)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderGuiElementEvents$Type = ($RenderGuiElementEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderGuiElementEvents_ = $RenderGuiElementEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedBoolean" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MutableBoolean, $MutableBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean"

export interface $DefaultedBoolean extends $MutableBoolean {

 "mapDefaultBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
 "getAsDefaultBoolean"(): boolean
 "getAsOptionalBoolean"(): $Optional<(boolean)>
 "applyDefaultBoolean"(): void
 "accept"(arg0: boolean): void
 "mapBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
 "getAsBoolean"(): boolean
}

export namespace $DefaultedBoolean {
function fromValue(arg0: boolean): $DefaultedBoolean
function fromEvent(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>, arg2: $Supplier$Type<(boolean)>): $DefaultedBoolean
function fromEvent(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>): $MutableBoolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedBoolean$Type = ($DefaultedBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedBoolean_ = $DefaultedBoolean$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export interface $CapabilityComponent {

 "write"(arg0: $CompoundTag$Type): void
 "read"(arg0: $CompoundTag$Type): void
 "toCompoundTag"(): $CompoundTag
}

export namespace $CapabilityComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityComponent$Type = ($CapabilityComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityComponent_ = $CapabilityComponent$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$GatherDebugTextEvents$Right" {
import {$List, $List$Type} from "packages/java/util/$List"

export interface $GatherDebugTextEvents$Right {

 "onGatherRightDebugText"(arg0: $List$Type<(string)>): void

(arg0: $List$Type<(string)>): void
}

export namespace $GatherDebugTextEvents$Right {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherDebugTextEvents$Right$Type = ($GatherDebugTextEvents$Right);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherDebugTextEvents$Right_ = $GatherDebugTextEvents$Right$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvokerRegistry" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $EventInvokerRegistry {

 "register"<T>(arg0: $Class$Type<(T)>, arg1: $BiConsumer$Type<(T), (any)>): void
 "register"<T>(arg0: $Class$Type<(T)>, arg1: $BiConsumer$Type<(T), (any)>, arg2: boolean): void
}

export namespace $EventInvokerRegistry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventInvokerRegistry$Type = ($EventInvokerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventInvokerRegistry_ = $EventInvokerRegistry$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/$EventImplHelper" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEvents$Jump, $LivingEvents$Jump$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Jump"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $EventImplHelper {


public static "getGrindstoneUsingPlayer"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): $Optional<($Player)>
public static "onLivingJump"(arg0: $LivingEvents$Jump$Type, arg1: $LivingEntity$Type): void
public static "getPlayerFromContainerMenu"(arg0: $AbstractContainerMenu$Type): $Optional<($Player)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventImplHelper$Type = ($EventImplHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventImplHelper_ = $EventImplHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderNameTagCallback" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$DefaultedValue, $DefaultedValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $RenderNameTagCallback {

 "onRenderNameTag"(arg0: $Entity$Type, arg1: $DefaultedValue$Type<($Component$Type)>, arg2: $EntityRenderer$Type<(any)>, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer, arg6: float): $EventResult

(arg0: $Entity$Type, arg1: $DefaultedValue$Type<($Component$Type)>, arg2: $EntityRenderer$Type<(any)>, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer, arg6: float): $EventResult
}

export namespace $RenderNameTagCallback {
const EVENT: $EventInvoker<($RenderNameTagCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderNameTagCallback$Type = ($RenderNameTagCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderNameTagCallback_ = $RenderNameTagCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$SearchRegistryContextForgeImpl" {
import {$SearchRegistryContext, $SearchRegistryContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SearchRegistryContext"
import {$SearchRegistry$TreeBuilderSupplier, $SearchRegistry$TreeBuilderSupplier$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$TreeBuilderSupplier"
import {$SearchRegistry$Key, $SearchRegistry$Key$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$Key"

export class $SearchRegistryContextForgeImpl implements $SearchRegistryContext {

constructor()

public "registerSearchTree"<T>(arg0: $SearchRegistry$Key$Type<(T)>, arg1: $SearchRegistry$TreeBuilderSupplier$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchRegistryContextForgeImpl$Type = ($SearchRegistryContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchRegistryContextForgeImpl_ = $SearchRegistryContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueDefaultedInt" {
import {$ValueMutableInt, $ValueMutableInt$Type} from "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableInt"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$DefaultedInt, $DefaultedInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedInt"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export class $ValueDefaultedInt extends $ValueMutableInt implements $DefaultedInt {

constructor(arg0: integer)

public "accept"(arg0: integer): void
public "getAsOptionalInt"(): $OptionalInt
public "getAsDefaultInt"(): integer
public static "fromValue"(arg0: integer): $DefaultedInt
public static "fromEvent"(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type, arg2: $IntSupplier$Type): $DefaultedInt
public "applyDefaultInt"(): void
public "mapDefaultInt"(arg0: $IntUnaryOperator$Type): void
public static "fromEvent"(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type): $MutableInt
get "asOptionalInt"(): $OptionalInt
get "asDefaultInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueDefaultedInt$Type = ($ValueDefaultedInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueDefaultedInt_ = $ValueDefaultedInt$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ObjectShareAccess" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ObjectShareAccess {

 "remove"(arg0: string): any
 "remove"(arg0: $ResourceLocation$Type): any
 "get"(arg0: string): any
 "get"(arg0: $ResourceLocation$Type): any
 "put"(arg0: string, arg1: any): any
 "put"(arg0: $ResourceLocation$Type, arg1: any): any
 "putIfAbsent"(arg0: string, arg1: any): any
 "putIfAbsent"(arg0: $ResourceLocation$Type, arg1: any): any
 "getOptional"<T>(arg0: string): $Optional<(T)>
 "getOptional"<T>(arg0: $ResourceLocation$Type): $Optional<(T)>
}

export namespace $ObjectShareAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectShareAccess$Type = ($ObjectShareAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectShareAccess_ = $ObjectShareAccess$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventDefaultedDouble" {
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$DefaultedDouble, $DefaultedDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedDouble"
import {$MutableDouble, $MutableDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble"
import {$EventMutableDouble, $EventMutableDouble$Type} from "packages/fuzs/puzzleslib/impl/event/data/$EventMutableDouble"

export class $EventDefaultedDouble extends $EventMutableDouble implements $DefaultedDouble {

constructor(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type, arg2: $DoubleSupplier$Type)

public "accept"(arg0: double): void
public "getAsDefaultDouble"(): double
public "getAsOptionalDouble"(): $OptionalDouble
public static "fromValue"(arg0: double): $DefaultedDouble
public static "fromEvent"(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type, arg2: $DoubleSupplier$Type): $DefaultedDouble
public "mapDefaultDouble"(arg0: $DoubleUnaryOperator$Type): void
public "applyDefaultDouble"(): void
public static "fromEvent"(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type): $MutableDouble
get "asDefaultDouble"(): double
get "asOptionalDouble"(): $OptionalDouble
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventDefaultedDouble$Type = ($EventDefaultedDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventDefaultedDouble_ = $EventDefaultedDouble$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelEvents$Unload" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientLevelEvents$Unload {

 "onLevelUnload"(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void

(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void
}

export namespace $ClientLevelEvents$Unload {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelEvents$Unload$Type = ($ClientLevelEvents$Unload);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelEvents$Unload_ = $ClientLevelEvents$Unload$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$CommonAbstractions" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$MobSpawnType, $MobSpawnType$Type} from "packages/net/minecraft/world/entity/$MobSpawnType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Pack$Info, $Pack$Info$Type} from "packages/net/minecraft/server/packs/repository/$Pack$Info"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $CommonAbstractions {

 "openMenu"(arg0: $ServerPlayer$Type, arg1: $MenuProvider$Type, arg2: $BiConsumer$Type<($ServerPlayer$Type), ($FriendlyByteBuf$Type)>): void
 "openMenu"(arg0: $ServerPlayer$Type, arg1: $MenuProvider$Type): void
 "getMinecraftServer"(): $MinecraftServer
 "getEnchantPowerBonus"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): float
 "canApplyAtEnchantingTable"(arg0: $Enchantment$Type, arg1: $ItemStack$Type): boolean
 "onPlayerDestroyItem"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $InteractionHand$Type): void
 "canEquip"(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type, arg2: $Entity$Type): boolean
 "createPackInfo"(arg0: $ResourceLocation$Type, arg1: $Component$Type, arg2: integer, arg3: $FeatureFlagSet$Type, arg4: boolean): $Pack$Info
 "getMobLootingLevel"(arg0: $Entity$Type, arg1: $Entity$Type, arg2: $DamageSource$Type): integer
 "getMobSpawnType"(arg0: $Mob$Type): $MobSpawnType
 "isBossMob"(arg0: $EntityType$Type<(any)>): boolean
 "getMobGriefingRule"(arg0: $Level$Type, arg1: $Entity$Type): boolean
 "isAllowedOnBooks"(arg0: $Enchantment$Type): boolean
}

export namespace $CommonAbstractions {
const INSTANCE: $CommonAbstractions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonAbstractions$Type = ($CommonAbstractions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonAbstractions_ = $CommonAbstractions$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$Buildable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Buildable {

 "build"(): void

(): void
}

export namespace $Buildable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Buildable$Type = ($Buildable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Buildable_ = $Buildable$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/serialization/$ConfigDataSetImpl" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$ConfigDataSet, $ConfigDataSet$Type} from "packages/fuzs/puzzleslib/api/config/v3/serialization/$ConfigDataSet"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigDataSetImpl<T> implements $ConfigDataSet<(T)> {

constructor(arg0: $Registry$Type<(T)>, arg1: $List$Type<(string)>, arg2: $BiPredicate$Type<(integer), (any)>, ...arg3: ($Class$Type<(any)>)[])

public "add"(arg0: T): boolean
public "remove"(arg0: any): boolean
public "get"(arg0: T): (any)[]
public "get"<V>(arg0: T, arg1: integer): V
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "toArray"<T1>(arg0: (T1)[]): (T1)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(T)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "toMap"(): $Map<(T), ((any)[])>
public "toSet"(): $Set<(T)>
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
public "getOptional"<V>(arg0: T, arg1: integer): $Optional<(V)>
public static "toString"<T>(arg0: $ResourceKey$Type<(any)>, ...arg1: (T)[]): $List<(string)>
public static "from"<T>(arg0: $ResourceKey$Type<(any)>, arg1: $List$Type<(string)>, ...arg2: ($Class$Type<(any)>)[]): $ConfigDataSet<(T)>
public static "from"<T>(arg0: $ResourceKey$Type<(any)>, arg1: $List$Type<(string)>, arg2: $BiPredicate$Type<(integer), (any)>, ...arg3: ($Class$Type<(any)>)[]): $ConfigDataSet<(T)>
public static "from"<T>(arg0: $ResourceKey$Type<(any)>, ...arg1: (string)[]): $ConfigDataSet<(T)>
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<(T)>
public "spliterator"(): $Spliterator<(T)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<T>;
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigDataSetImpl$Type<T> = ($ConfigDataSetImpl<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigDataSetImpl_<T> = $ConfigDataSetImpl$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MessageV3, $MessageV3$Type} from "packages/fuzs/puzzleslib/api/network/v3/$MessageV3"
import {$ServerMessageListener, $ServerMessageListener$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerMessageListener"

export interface $ServerboundMessage<T extends $Record> extends $MessageV3<($ServerMessageListener<(T)>)> {

 "getHandler"(): $ServerMessageListener<(T)>

(): $ServerMessageListener<(T)>
}

export namespace $ServerboundMessage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerboundMessage$Type<T> = ($ServerboundMessage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerboundMessage_<T> = $ServerboundMessage$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ClientProxyImpl" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ProxyImpl, $ProxyImpl$Type} from "packages/fuzs/puzzleslib/impl/core/$ProxyImpl"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"

export interface $ClientProxyImpl extends $ProxyImpl {

 "hasShiftDown"(): boolean
 "getClientLevel"(): $Level
 "hasControlDown"(): boolean
 "hasAltDown"(): boolean
 "getClientPlayer"(): $Player
 "getClientPacketListener"(): $ClientPacketListener
 "getKeyMappingComponent"(arg0: string): $Component
/**
 * 
 * @deprecated
 */
 "getGameServer"(): $MinecraftServer
}

export namespace $ClientProxyImpl {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientProxyImpl$Type = ($ClientProxyImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientProxyImpl_ = $ClientProxyImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$BakingCompleted" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$ModelManager, $ModelManager$Type} from "packages/net/minecraft/client/resources/model/$ModelManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * 
 * @deprecated
 */
export interface $ModelEvents$BakingCompleted {

 "onBakingCompleted"(arg0: $Supplier$Type<($ModelManager$Type)>, arg1: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg2: $Supplier$Type<($ModelBakery$Type)>): void

(arg0: $Supplier$Type<($ModelManager$Type)>, arg1: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg2: $Supplier$Type<($ModelBakery$Type)>): void
}

export namespace $ModelEvents$BakingCompleted {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$BakingCompleted$Type = ($ModelEvents$BakingCompleted);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents$BakingCompleted_ = $ModelEvents$BakingCompleted$Type;
}}
declare module "packages/fuzs/puzzleslib/api/entity/v1/$AdditionalAddEntityData" {
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $AdditionalAddEntityData {

 "writeAdditionalAddEntityData"(arg0: $FriendlyByteBuf$Type): void
 "readAdditionalAddEntityData"(arg0: $FriendlyByteBuf$Type): void
}

export namespace $AdditionalAddEntityData {
function getPacket<T>(arg0: T): $Packet<($ClientGamePacketListener)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionalAddEntityData$Type = ($AdditionalAddEntityData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionalAddEntityData_ = $AdditionalAddEntityData$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/resources/$NamedReloadListener" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export interface $NamedReloadListener extends $PreparableReloadListener {

 "identifier"(): $ResourceLocation
 "getName"(): string
 "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
}

export namespace $NamedReloadListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NamedReloadListener$Type = ($NamedReloadListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NamedReloadListener_ = $NamedReloadListener$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$FluidRenderTypesContextImpl" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$RenderTypesContext, $RenderTypesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$RenderTypesContext"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $FluidRenderTypesContextImpl implements $RenderTypesContext<($Fluid)> {

constructor()

public "registerRenderType"(arg0: $RenderType$Type, ...arg1: ($Fluid$Type)[]): void
public "getRenderType"(arg0: $Fluid$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidRenderTypesContextImpl$Type = ($FluidRenderTypesContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidRenderTypesContextImpl_ = $FluidRenderTypesContextImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$BuildCreativeModeTabContentsContextForgeImpl" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$CreativeModeTab$ItemDisplayParameters, $CreativeModeTab$ItemDisplayParameters$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$ItemDisplayParameters"
import {$CreativeModeTab$DisplayItemsGenerator, $CreativeModeTab$DisplayItemsGenerator$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$DisplayItemsGenerator"
import {$BuildCreativeModeTabContentsContext, $BuildCreativeModeTabContentsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BuildCreativeModeTabContentsContext"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$CreativeModeTab$Output, $CreativeModeTab$Output$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Output"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $BuildCreativeModeTabContentsContextForgeImpl extends $Record implements $BuildCreativeModeTabContentsContext {

constructor(resourceKey: $ResourceKey$Type<($CreativeModeTab$Type)>, parameters: $CreativeModeTab$ItemDisplayParameters$Type, output: $CreativeModeTab$Output$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "parameters"(): $CreativeModeTab$ItemDisplayParameters
public "output"(): $CreativeModeTab$Output
public "resourceKey"(): $ResourceKey<($CreativeModeTab)>
public "registerBuildListener"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void
public "registerBuildListener"(arg0: string, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void
public "registerBuildListener"(arg0: $ResourceLocation$Type, arg1: $CreativeModeTab$DisplayItemsGenerator$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuildCreativeModeTabContentsContextForgeImpl$Type = ($BuildCreativeModeTabContentsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuildCreativeModeTabContentsContextForgeImpl_ = $BuildCreativeModeTabContentsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/recipes/$ForwardingFinishedRecipe" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"

export class $ForwardingFinishedRecipe implements $FinishedRecipe {

constructor(arg0: $FinishedRecipe$Type, arg1: $Consumer$Type<($JsonObject$Type)>, arg2: $RecipeSerializer$Type<(any)>)
constructor(arg0: $FinishedRecipe$Type, arg1: $Consumer$Type<($JsonObject$Type)>)
constructor(arg0: $FinishedRecipe$Type)

public "serializeRecipeData"(arg0: $JsonObject$Type): void
public "getType"(): $RecipeSerializer<(any)>
public "getAdvancementId"(): $ResourceLocation
public "getId"(): $ResourceLocation
public "serializeAdvancement"(): $JsonObject
public "serializeRecipe"(): $JsonObject
get "type"(): $RecipeSerializer<(any)>
get "advancementId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForwardingFinishedRecipe$Type = ($ForwardingFinishedRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForwardingFinishedRecipe_ = $ForwardingFinishedRecipe$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterTerrain" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

export interface $RenderLevelEvents$AfterTerrain {

 "onRenderLevelAfterTerrain"(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void

(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void
}

export namespace $RenderLevelEvents$AfterTerrain {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEvents$AfterTerrain$Type = ($RenderLevelEvents$AfterTerrain);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEvents$AfterTerrain_ = $RenderLevelEvents$AfterTerrain$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerRegistryImpl" {
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$FriendlyByteBuf$Reader, $FriendlyByteBuf$Reader$Type} from "packages/net/minecraft/network/$FriendlyByteBuf$Reader"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$FriendlyByteBuf$Writer, $FriendlyByteBuf$Writer$Type} from "packages/net/minecraft/network/$FriendlyByteBuf$Writer"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$NetworkHandlerV3$Builder, $NetworkHandlerV3$Builder$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$MessageSerializer, $MessageSerializer$Type} from "packages/fuzs/puzzleslib/api/network/v3/serialization/$MessageSerializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $NetworkHandlerRegistryImpl implements $NetworkHandlerV3$Builder {
 "clientAcceptsVanillaOrMissing": boolean
 "serverAcceptsVanillaOrMissing": boolean


public "build"(): void
public "clientAcceptsVanillaOrMissing"(): $NetworkHandlerV3$Builder
public "serverAcceptsVanillaOrMissing"(): $NetworkHandlerV3$Builder
public "registerClientbound"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Class$Type<(T)>): $NetworkHandlerV3$Builder
public "registerServerbound"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: $Class$Type<(T)>): $NetworkHandlerV3$Builder
public "registerContainerProvider"<T>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(($Type$Type)[]), ($MessageSerializer$Type<(any)>)>): $NetworkHandlerV3$Builder
public "registerSerializer"<T>(arg0: $Class$Type<(T)>, arg1: $FriendlyByteBuf$Writer$Type<(T)>, arg2: $FriendlyByteBuf$Reader$Type<(T)>): $NetworkHandlerV3$Builder
public "registerSerializer"<T>(arg0: $Class$Type<(any)>, arg1: $ResourceKey$Type<($Registry$Type<(T)>)>): $NetworkHandlerV3$Builder
public "allAcceptVanillaOrMissing"(): $NetworkHandlerV3$Builder
/**
 * 
 * @deprecated
 */
public "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockEntity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $LevelChunk$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: $ChunkPos$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T, arg2: boolean): void
/**
 * 
 * @deprecated
 */
public "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: $ServerLevel$Type, arg4: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Vec3i$Type, arg1: $ServerLevel$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $ServerLevel$Type, arg6: T): void
/**
 * 
 * @deprecated
 */
public "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
public "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Collection$Type<($ServerPlayer$Type)>, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
public "sendTo"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "toServerboundPacket"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): $Packet<($ServerGamePacketListener)>
/**
 * 
 * @deprecated
 */
public "toClientboundPacket"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): $Packet<($ClientGamePacketListener)>
/**
 * 
 * @deprecated
 */
public "sendToServer"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): void
public static "builder"(arg0: string): $NetworkHandlerV3$Builder
public static "builder"(arg0: string, arg1: string): $NetworkHandlerV3$Builder
/**
 * 
 * @deprecated
 */
public "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ResourceKey$Type<($Level$Type)>, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Level$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockPos$Type, arg1: $Level$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Level$Type, arg5: T): void
/**
 * 
 * @deprecated
 */
public "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): void
/**
 * 
 * @deprecated
 */
public "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllTrackingAndSelf"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
public "sendToAllNearExcept"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Level$Type, arg6: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerRegistryImpl$Type = ($NetworkHandlerRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerRegistryImpl_ = $NetworkHandlerRegistryImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/biome/$MobSpawnSettingsContextForge" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$MobSpawnSettings$SpawnerData, $MobSpawnSettings$SpawnerData$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$SpawnerData"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MobSpawnSettingsContext, $MobSpawnSettingsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$MobSpawnSettingsContext"
import {$MobSpawnSettingsBuilder, $MobSpawnSettingsBuilder$Type} from "packages/net/minecraftforge/common/world/$MobSpawnSettingsBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobSpawnSettings$MobSpawnCost, $MobSpawnSettings$MobSpawnCost$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$MobSpawnCost"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"

export class $MobSpawnSettingsContextForge extends $Record implements $MobSpawnSettingsContext {

constructor(context: $MobSpawnSettingsBuilder$Type)

public "context"(): $MobSpawnSettingsBuilder
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "addSpawn"(arg0: $MobCategory$Type, arg1: $MobSpawnSettings$SpawnerData$Type): void
public "removeSpawns"(arg0: $BiPredicate$Type<($MobCategory$Type), ($MobSpawnSettings$SpawnerData$Type)>): boolean
public "getSpawnerData"(arg0: $MobCategory$Type): $List<($MobSpawnSettings$SpawnerData)>
public "getSpawnCost"(arg0: $EntityType$Type<(any)>): $MobSpawnSettings$MobSpawnCost
public "clearSpawnCost"(arg0: $EntityType$Type<(any)>): boolean
public "setSpawnCost"(arg0: $EntityType$Type<(any)>, arg1: double, arg2: double): void
public "getCreatureGenerationProbability"(): float
public "getMobCategoriesWithSpawns"(): $Set<($MobCategory)>
public "getEntityTypesWithSpawnCost"(): $Set<($EntityType<(any)>)>
public "setCreatureGenerationProbability"(arg0: float): void
public "clearSpawns"(arg0: $MobCategory$Type): void
public "clearSpawns"(): void
public "removeSpawnsOfEntityType"(arg0: $EntityType$Type<(any)>): boolean
get "creatureGenerationProbability"(): float
get "mobCategoriesWithSpawns"(): $Set<($MobCategory)>
get "entityTypesWithSpawnCost"(): $Set<($EntityType<(any)>)>
set "creatureGenerationProbability"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnSettingsContextForge$Type = ($MobSpawnSettingsContextForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnSettingsContextForge_ = $MobSpawnSettingsContextForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseDrag" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ScreenMouseEvents$BeforeMouseDrag<T extends $Screen> {

 "onBeforeMouseDrag"(arg0: T, arg1: double, arg2: double, arg3: integer, arg4: double, arg5: double): $EventResult

(arg0: T, arg1: double, arg2: double, arg3: integer, arg4: double, arg5: double): $EventResult
}

export namespace $ScreenMouseEvents$BeforeMouseDrag {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$BeforeMouseDrag$Type<T> = ($ScreenMouseEvents$BeforeMouseDrag<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$BeforeMouseDrag_<T> = $ScreenMouseEvents$BeforeMouseDrag$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelTickEvents$End" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerLevelTickEvents$End {

 "onEndLevelTick"(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void

(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void
}

export namespace $ServerLevelTickEvents$End {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelTickEvents$End$Type = ($ServerLevelTickEvents$End);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelTickEvents$End_ = $ServerLevelTickEvents$End$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderHandCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $RenderHandCallback {

 "onRenderHand"(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $ItemStack$Type, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): $EventResult

(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $ItemStack$Type, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): $EventResult
}

export namespace $RenderHandCallback {
const EVENT: $EventInvoker<($RenderHandCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHandCallback$Type = ($RenderHandCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHandCallback_ = $RenderHandCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/item/v2/$ItemEquipmentFactories" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"

export class $ItemEquipmentFactories {


public static "registerTier"(arg0: integer, arg1: integer, arg2: float, arg3: float, arg4: integer, arg5: $Supplier$Type<($Ingredient$Type)>): $Tier
public static "registerArmorMaterial"(arg0: $ResourceLocation$Type, arg1: integer, arg2: (integer)[], arg3: integer, arg4: $Supplier$Type<($Ingredient$Type)>): $ArmorMaterial
public static "registerArmorMaterial"(arg0: $ResourceLocation$Type, arg1: integer, arg2: (integer)[], arg3: integer, arg4: $Supplier$Type<($SoundEvent$Type)>, arg5: float, arg6: float, arg7: $Supplier$Type<($Ingredient$Type)>): $ArmorMaterial
public static "registerArmorMaterial"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($Ingredient$Type)>): $ArmorMaterial
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEquipmentFactories$Type = ($ItemEquipmentFactories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEquipmentFactories_ = $ItemEquipmentFactories$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$AfterKeyPress" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenKeyboardEvents$AfterKeyPress<T extends $Screen> {

 "onAfterKeyPress"(arg0: T, arg1: integer, arg2: integer, arg3: integer): void

(arg0: T, arg1: integer, arg2: integer, arg3: integer): void
}

export namespace $ScreenKeyboardEvents$AfterKeyPress {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenKeyboardEvents$AfterKeyPress$Type<T> = ($ScreenKeyboardEvents$AfterKeyPress<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenKeyboardEvents$AfterKeyPress_<T> = $ScreenKeyboardEvents$AfterKeyPress$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueDefaultedValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$ValueMutableValue, $ValueMutableValue$Type} from "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableValue"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DefaultedValue, $DefaultedValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue"

export class $ValueDefaultedValue<T> extends $ValueMutableValue<(T)> implements $DefaultedValue<(T)> {

constructor(arg0: T)

public "accept"(arg0: T): void
public "getAsOptional"(): $Optional<(T)>
public "getAsDefault"(): T
public static "fromValue"<T>(arg0: T): $DefaultedValue<(T)>
public static "fromEvent"<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>, arg2: $Supplier$Type<(T)>): $DefaultedValue<(T)>
public "mapDefault"(arg0: $UnaryOperator$Type<(T)>): void
public "applyDefault"(): void
public static "fromEvent"<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>): $MutableValue<(T)>
get "asOptional"(): $Optional<(T)>
get "asDefault"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueDefaultedValue$Type<T> = ($ValueDefaultedValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueDefaultedValue_<T> = $ValueDefaultedValue$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ContainerScreenEvents" {
import {$ContainerScreenEvents$Foreground, $ContainerScreenEvents$Foreground$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ContainerScreenEvents$Foreground"
import {$ContainerScreenEvents$Background, $ContainerScreenEvents$Background$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ContainerScreenEvents$Background"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ContainerScreenEvents {
static readonly "BACKGROUND": $EventInvoker<($ContainerScreenEvents$Background)>
static readonly "FOREGROUND": $EventInvoker<($ContainerScreenEvents$Foreground)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerScreenEvents$Type = ($ContainerScreenEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerScreenEvents_ = $ContainerScreenEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeFactories" {
import {$PotionBrewingRegistry, $PotionBrewingRegistry$Type} from "packages/fuzs/puzzleslib/api/init/v2/$PotionBrewingRegistry"
import {$ProxyImpl, $ProxyImpl$Type} from "packages/fuzs/puzzleslib/impl/core/$ProxyImpl"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ToolTypeHelper, $ToolTypeHelper$Type} from "packages/fuzs/puzzleslib/api/item/v2/$ToolTypeHelper"
import {$GameRulesFactory, $GameRulesFactory$Type} from "packages/fuzs/puzzleslib/api/init/v2/$GameRulesFactory"
import {$CombinedIngredients, $CombinedIngredients$Type} from "packages/fuzs/puzzleslib/api/item/v2/crafting/$CombinedIngredients"
import {$CommonFactories, $CommonFactories$Type} from "packages/fuzs/puzzleslib/impl/core/$CommonFactories"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ModConstructor, $ModConstructor$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModConstructor"
import {$ModContext, $ModContext$Type} from "packages/fuzs/puzzleslib/impl/core/$ModContext"

export class $ForgeFactories implements $CommonFactories {

constructor()

public "getModContext"(arg0: string): $ModContext
public "getToolTypeHelper"(): $ToolTypeHelper
public "getClientProxy"(): $ProxyImpl
public "getServerProxy"(): $ProxyImpl
public "constructMod"(arg0: string, arg1: $ModConstructor$Type, arg2: $Set$Type<($ContentRegistrationFlags$Type)>, arg3: $Set$Type<($ContentRegistrationFlags$Type)>): void
public "getPotionBrewingRegistry"(): $PotionBrewingRegistry
public "getGameRulesFactory"(): $GameRulesFactory
public "registerLoadingHandlers"(): void
public "registerEventHandlers"(): void
public "getCombinedIngredients"(): $CombinedIngredients
get "toolTypeHelper"(): $ToolTypeHelper
get "clientProxy"(): $ProxyImpl
get "serverProxy"(): $ProxyImpl
get "potionBrewingRegistry"(): $PotionBrewingRegistry
get "gameRulesFactory"(): $GameRulesFactory
get "combinedIngredients"(): $CombinedIngredients
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeFactories$Type = ($ForgeFactories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeFactories_ = $ForgeFactories$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$ForgeRegistryManagerV2" {
import {$RegistryReference, $RegistryReference$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryReference"
import {$ExtendedMenuSupplier, $ExtendedMenuSupplier$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier"
import {$RegistryManager, $RegistryManager$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryManager"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$RegistryManagerV2Impl, $RegistryManagerV2Impl$Type} from "packages/fuzs/puzzleslib/impl/init/$RegistryManagerV2Impl"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$PoiTypeBuilder, $PoiTypeBuilder$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$PoiTypeBuilder"

export class $ForgeRegistryManagerV2 extends $RegistryManagerV2Impl {

constructor(arg0: string)

public "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($BlockState$Type)>)>, arg2: integer, arg3: integer): $RegistryReference<($PoiType)>
public "registerPoiTypeBuilder"(arg0: string, arg1: $Supplier$Type<($PoiTypeBuilder$Type)>): $RegistryReference<($PoiType)>
public "registerSpawnEggItem"(arg0: $RegistryReference$Type<(any)>, arg1: integer, arg2: integer, arg3: $Item$Properties$Type): $RegistryReference<($Item)>
public "registerExtendedMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($ExtendedMenuSupplier$Type<(T)>)>): $RegistryReference<($MenuType<(T)>)>
public static "instant"(arg0: string): $RegistryManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeRegistryManagerV2$Type = ($ForgeRegistryManagerV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeRegistryManagerV2_ = $ForgeRegistryManagerV2$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$ForgeRegistryManagerV3" {
import {$RegistryManager, $RegistryManager$Type} from "packages/fuzs/puzzleslib/api/init/v3/$RegistryManager"
import {$ExtendedMenuSupplier, $ExtendedMenuSupplier$Type} from "packages/fuzs/puzzleslib/api/init/v2/builder/$ExtendedMenuSupplier"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$RegistryManagerV3Impl, $RegistryManagerV3Impl$Type} from "packages/fuzs/puzzleslib/impl/init/$RegistryManagerV3Impl"

export class $ForgeRegistryManagerV3 extends $RegistryManagerV3Impl {

constructor(arg0: string)

public "registerPoiType"(arg0: string, arg1: $Supplier$Type<($Set$Type<($BlockState$Type)>)>, arg2: integer, arg3: integer): $Holder$Reference<($PoiType)>
public "registerSpawnEggItem"(arg0: $Holder$Reference$Type<(any)>, arg1: integer, arg2: integer, arg3: $Item$Properties$Type): $Holder$Reference<($Item)>
public "registerExtendedMenuType"<T extends $AbstractContainerMenu>(arg0: string, arg1: $Supplier$Type<($ExtendedMenuSupplier$Type<(T)>)>): $Holder$Reference<($MenuType<(T)>)>
public "getHolder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $Holder$Reference<(T)>
public static "from"(arg0: string): $RegistryManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeRegistryManagerV3$Type = ($ForgeRegistryManagerV3);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeRegistryManagerV3_ = $ForgeRegistryManagerV3$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$KeyMappingsContextForgeImpl" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyMappingActivationHelper$KeyActivationContext, $KeyMappingActivationHelper$KeyActivationContext$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper$KeyActivationContext"
import {$KeyMappingsContext, $KeyMappingsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$KeyMappingsContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $KeyMappingsContextForgeImpl extends $Record implements $KeyMappingsContext {

constructor(consumer: $Consumer$Type<($KeyMapping$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $Consumer<($KeyMapping)>
public "registerKeyMapping"(arg0: $KeyMapping$Type, arg1: $KeyMappingActivationHelper$KeyActivationContext$Type): void
/**
 * 
 * @deprecated
 */
public "registerKeyMapping"(...arg0: ($KeyMapping$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingsContextForgeImpl$Type = ($KeyMappingsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingsContextForgeImpl_ = $KeyMappingsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerTickEvents$End" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export interface $PlayerTickEvents$End {

 "onEndPlayerTick"(arg0: $Player$Type): void

(arg0: $Player$Type): void
}

export namespace $PlayerTickEvents$End {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerTickEvents$End$Type = ($PlayerTickEvents$End);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerTickEvents$End_ = $PlayerTickEvents$End$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/event/$ForgeModelBakerImpl$BakedCacheKey" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Transformation, $Transformation$Type} from "packages/com/mojang/math/$Transformation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeModelBakerImpl$BakedCacheKey extends $Record {

constructor(resourceLocation: $ResourceLocation$Type, rotation: $Transformation$Type, isUvLocked: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isUvLocked"(): boolean
public "rotation"(): $Transformation
public "resourceLocation"(): $ResourceLocation
get "uvLocked"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModelBakerImpl$BakedCacheKey$Type = ($ForgeModelBakerImpl$BakedCacheKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModelBakerImpl$BakedCacheKey_ = $ForgeModelBakerImpl$BakedCacheKey$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$EntityAttributesModifyContextForgeImpl" {
import {$EntityAttributesModifyContext, $EntityAttributesModifyContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesModifyContext"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"

export class $EntityAttributesModifyContextForgeImpl extends $Record implements $EntityAttributesModifyContext {

constructor(context: $EntityAttributesModifyContext$Type)

public "context"(): $EntityAttributesModifyContext
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerAttributeModification"(arg0: $EntityType$Type<(any)>, arg1: $Attribute$Type, arg2: double): void
public "registerAttributeModification"(arg0: $EntityType$Type<(any)>, arg1: $Attribute$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributesModifyContextForgeImpl$Type = ($EntityAttributesModifyContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributesModifyContextForgeImpl_ = $EntityAttributesModifyContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/resources/v1/$AbstractModPackResources" {
import {$PackResources$ResourceOutput, $PackResources$ResourceOutput$Type} from "packages/net/minecraft/server/packs/$PackResources$ResourceOutput"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$MetadataSectionSerializer, $MetadataSectionSerializer$Type} from "packages/net/minecraft/server/packs/metadata/$MetadataSectionSerializer"
import {$IoSupplier, $IoSupplier$Type} from "packages/net/minecraft/server/packs/resources/$IoSupplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PackResources, $PackResources$Type} from "packages/net/minecraft/server/packs/$PackResources"

export class $AbstractModPackResources implements $PackResources {


public "close"(): void
public "getResource"(arg0: $PackType$Type, arg1: $ResourceLocation$Type): $IoSupplier<($InputStream)>
public "listResources"(arg0: $PackType$Type, arg1: string, arg2: string, arg3: $PackResources$ResourceOutput$Type): void
public "getRootResource"(...arg0: (string)[]): $IoSupplier<($InputStream)>
public "isBuiltin"(): boolean
public "packId"(): string
public "getMetadataSection"<T>(arg0: $MetadataSectionSerializer$Type<(T)>): T
public "getNamespaces"(arg0: $PackType$Type): $Set<(string)>
public "isHidden"(): boolean
public "getChildren"(): $Collection<($PackResources)>
get "builtin"(): boolean
get "hidden"(): boolean
get "children"(): $Collection<($PackResources)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModPackResources$Type = ($AbstractModPackResources);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModPackResources_ = $AbstractModPackResources$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/event/$ForgeButtonList" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"

export class $ForgeButtonList extends $AbstractList<($AbstractWidget)> {


public "size"(): integer
public "remove"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "contains"(arg0: any): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeButtonList$Type = ($ForgeButtonList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeButtonList_ = $ForgeButtonList$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/screen/v2/$ScreenTooltipFactory" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Tooltip, $Tooltip$Type} from "packages/net/minecraft/client/gui/components/$Tooltip"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ScreenTooltipFactory {


public static "create"(arg0: $List$Type<(any)>): $Tooltip
/**
 * 
 * @deprecated
 */
public static "create"(arg0: $Font$Type, arg1: $List$Type<(any)>): $Tooltip
public static "create"(...arg0: ($FormattedText$Type)[]): $Tooltip
/**
 * 
 * @deprecated
 */
public static "create"(arg0: $Font$Type, ...arg1: ($FormattedText$Type)[]): $Tooltip
public static "createTooltip"(arg0: $List$Type<($FormattedCharSequence$Type)>): $Tooltip
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenTooltipFactory$Type = ($ScreenTooltipFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenTooltipFactory_ = $ScreenTooltipFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$LayerDefinitionsContext" {
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"

export interface $LayerDefinitionsContext {

 "registerLayerDefinition"(arg0: $ModelLayerLocation$Type, arg1: $Supplier$Type<($LayerDefinition$Type)>): void

(arg0: $ModelLayerLocation$Type, arg1: $Supplier$Type<($LayerDefinition$Type)>): void
}

export namespace $LayerDefinitionsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayerDefinitionsContext$Type = ($LayerDefinitionsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayerDefinitionsContext_ = $LayerDefinitionsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseRelease" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenMouseEvents$AfterMouseRelease<T extends $Screen> {

 "onAfterMouseRelease"(arg0: T, arg1: double, arg2: double, arg3: integer): void

(arg0: T, arg1: double, arg2: double, arg3: integer): void
}

export namespace $ScreenMouseEvents$AfterMouseRelease {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$AfterMouseRelease$Type<T> = ($ScreenMouseEvents$AfterMouseRelease<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$AfterMouseRelease_<T> = $ScreenMouseEvents$AfterMouseRelease$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$AfterMouseClick" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenMouseEvents$AfterMouseClick<T extends $Screen> {

 "onAfterMouseClick"(arg0: T, arg1: double, arg2: double, arg3: integer): void

(arg0: T, arg1: double, arg2: double, arg3: integer): void
}

export namespace $ScreenMouseEvents$AfterMouseClick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$AfterMouseClick$Type<T> = ($ScreenMouseEvents$AfterMouseClick<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$AfterMouseClick_<T> = $ScreenMouseEvents$AfterMouseClick$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents" {
import {$InputEvents$BeforeMouseAction, $InputEvents$BeforeMouseAction$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$BeforeMouseAction"
import {$InputEvents$AfterMouseScroll, $InputEvents$AfterMouseScroll$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$AfterMouseScroll"
import {$InputEvents$BeforeKeyAction, $InputEvents$BeforeKeyAction$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$BeforeKeyAction"
import {$InputEvents$AfterMouseAction, $InputEvents$AfterMouseAction$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$AfterMouseAction"
import {$InputEvents$BeforeMouseScroll, $InputEvents$BeforeMouseScroll$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$BeforeMouseScroll"
import {$InputEvents$AfterKeyAction, $InputEvents$AfterKeyAction$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$AfterKeyAction"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $InputEvents {
static readonly "BEFORE_MOUSE_ACTION": $EventInvoker<($InputEvents$BeforeMouseAction)>
static readonly "AFTER_MOUSE_ACTION": $EventInvoker<($InputEvents$AfterMouseAction)>
static readonly "BEFORE_MOUSE_SCROLL": $EventInvoker<($InputEvents$BeforeMouseScroll)>
static readonly "AFTER_MOUSE_SCROLL": $EventInvoker<($InputEvents$AfterMouseScroll)>
static readonly "BEFORE_KEY_ACTION": $EventInvoker<($InputEvents$BeforeKeyAction)>
static readonly "AFTER_KEY_ACTION": $EventInvoker<($InputEvents$AfterKeyAction)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$Type = ($InputEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents_ = $InputEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$ParticleProvidersContextForgeImpl" {
import {$ParticleProvider, $ParticleProvider$Type} from "packages/net/minecraft/client/particle/$ParticleProvider"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$ParticleProvidersContext, $ParticleProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ParticleProvidersContext"
import {$ParticleEngine$SpriteParticleRegistration, $ParticleEngine$SpriteParticleRegistration$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$SpriteParticleRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegisterParticleProvidersEvent, $RegisterParticleProvidersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterParticleProvidersEvent"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"
import {$ParticleProvider$Sprite, $ParticleProvider$Sprite$Type} from "packages/net/minecraft/client/particle/$ParticleProvider$Sprite"

export class $ParticleProvidersContextForgeImpl extends $Record implements $ParticleProvidersContext {

constructor(evt: $RegisterParticleProvidersEvent$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerParticleProvider"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleProvider$Type<(T)>): void
public "registerParticleProvider"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
public "registerParticleProvider"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleProvider$Sprite$Type<(T)>): void
public "evt"(): $RegisterParticleProvidersEvent
public "registerClientParticleProvider"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleProvider$Sprite$Type<(T)>): void
public "registerClientParticleProvider"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
public "registerClientParticleProvider"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleProvider$Type<(T)>): void
/**
 * 
 * @deprecated
 */
public "registerParticleFactory"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleProvidersContextForgeImpl$Type = ($ParticleProvidersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleProvidersContextForgeImpl_ = $ParticleProvidersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/recipes/$CopyTagShapelessRecipeBuilder" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Advancement$Builder, $Advancement$Builder$Type} from "packages/net/minecraft/advancements/$Advancement$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShapelessRecipeBuilder, $ShapelessRecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$ShapelessRecipeBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeCategory, $RecipeCategory$Type} from "packages/net/minecraft/data/recipes/$RecipeCategory"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $CopyTagShapelessRecipeBuilder extends $ShapelessRecipeBuilder {
readonly "result": $Item
readonly "count": integer
readonly "ingredients": $List<($Ingredient)>
readonly "advancement": $Advancement$Builder
 "group": string

constructor(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: integer)

public "group"(arg0: string): $CopyTagShapelessRecipeBuilder
public "requires"(arg0: $Ingredient$Type): $CopyTagShapelessRecipeBuilder
public "requires"(arg0: $ItemLike$Type, arg1: integer): $CopyTagShapelessRecipeBuilder
public "requires"(arg0: $Ingredient$Type, arg1: integer): $CopyTagShapelessRecipeBuilder
public "requires"(arg0: $ItemLike$Type): $CopyTagShapelessRecipeBuilder
public "requires"(arg0: $TagKey$Type<($Item$Type)>): $CopyTagShapelessRecipeBuilder
public "copyFrom"(arg0: $ItemLike$Type): $CopyTagShapelessRecipeBuilder
public "copyFrom"(arg0: $Ingredient$Type): $CopyTagShapelessRecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "unlockedBy"(arg0: string, arg1: $CriterionTriggerInstance$Type): $CopyTagShapelessRecipeBuilder
public static "shapeless"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type): $CopyTagShapelessRecipeBuilder
public static "shapeless"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: integer): $CopyTagShapelessRecipeBuilder
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CopyTagShapelessRecipeBuilder$Type = ($CopyTagShapelessRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CopyTagShapelessRecipeBuilder_ = $CopyTagShapelessRecipeBuilder$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$AttackV2" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $InteractionInputEvents$AttackV2 {

 "onAttackInteraction"(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type, arg2: $HitResult$Type): $EventResult

(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type, arg2: $HitResult$Type): $EventResult
}

export namespace $InteractionInputEvents$AttackV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionInputEvents$AttackV2$Type = ($InteractionInputEvents$AttackV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionInputEvents$AttackV2_ = $InteractionInputEvents$AttackV2$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$ModifyBakingResult" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"

/**
 * 
 * @deprecated
 */
export interface $ModelEvents$ModifyBakingResult {

 "onModifyBakingResult"(arg0: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg1: $Supplier$Type<($ModelBakery$Type)>): void

(arg0: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg1: $Supplier$Type<($ModelBakery$Type)>): void
}

export namespace $ModelEvents$ModifyBakingResult {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$ModifyBakingResult$Type = ($ModelEvents$ModifyBakingResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents$ModifyBakingResult_ = $ModelEvents$ModifyBakingResult$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/core/$ForgeConfigFileTypeHandler" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CommentedFileConfig, $CommentedFileConfig$Type} from "packages/com/electronwill/nightconfig/core/file/$CommentedFileConfig"
import {$ModConfig, $ModConfig$Type} from "packages/net/minecraftforge/fml/config/$ModConfig"
import {$ConfigFileTypeHandler, $ConfigFileTypeHandler$Type} from "packages/net/minecraftforge/fml/config/$ConfigFileTypeHandler"

export class $ForgeConfigFileTypeHandler extends $ConfigFileTypeHandler {

constructor()

public "reader"(arg0: $Path$Type): $Function<($ModConfig), ($CommentedFileConfig)>
public "unload"(arg0: $Path$Type, arg1: $ModConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfigFileTypeHandler$Type = ($ForgeConfigFileTypeHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfigFileTypeHandler_ = $ForgeConfigFileTypeHandler$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingHurtCallback" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingHurtCallback {

 "onLivingHurt"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $MutableFloat$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: $MutableFloat$Type): $EventResult
}

export namespace $LivingHurtCallback {
const EVENT: $EventInvoker<($LivingHurtCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingHurtCallback$Type = ($LivingHurtCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingHurtCallback_ = $LivingHurtCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$AnvilUpdateCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"

export interface $AnvilUpdateCallback {

 "onAnvilUpdate"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $MutableValue$Type<($ItemStack$Type)>, arg3: string, arg4: $MutableInt$Type, arg5: $MutableInt$Type, arg6: $Player$Type): $EventResult

(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $MutableValue$Type<($ItemStack$Type)>, arg3: string, arg4: $MutableInt$Type, arg5: $MutableInt$Type, arg6: $Player$Type): $EventResult
}

export namespace $AnvilUpdateCallback {
const EVENT: $EventInvoker<($AnvilUpdateCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilUpdateCallback$Type = ($AnvilUpdateCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilUpdateCallback_ = $AnvilUpdateCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EventResult extends $Enum<($EventResult)> {
static readonly "PASS": $EventResult
static readonly "INTERRUPT": $EventResult
static readonly "ALLOW": $EventResult
static readonly "DENY": $EventResult


public static "values"(): ($EventResult)[]
public static "valueOf"(arg0: string): $EventResult
public "getAsBoolean"(): boolean
public "isInterrupt"(): boolean
public "isPass"(): boolean
get "asBoolean"(): boolean
get "interrupt"(): boolean
get "pass"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventResult$Type = (("allow") | ("deny") | ("pass") | ("interrupt")) | ($EventResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventResult_ = $EventResult$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$EntityAttributesModifyContext" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"

export interface $EntityAttributesModifyContext {

 "registerAttributeModification"(arg0: $EntityType$Type<(any)>, arg1: $Attribute$Type): void
 "registerAttributeModification"(arg0: $EntityType$Type<(any)>, arg1: $Attribute$Type, arg2: double): void

(arg0: $EntityType$Type<(any)>, arg1: $Attribute$Type): void
}

export namespace $EntityAttributesModifyContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributesModifyContext$Type = ($EntityAttributesModifyContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributesModifyContext_ = $EntityAttributesModifyContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$BeforeInitV2" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenEvents$BeforeInitV2<T extends $Screen> {

 "onBeforeInit"(arg0: $Minecraft$Type, arg1: T, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>): void

(arg0: $Minecraft$Type, arg1: T, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>): void
}

export namespace $ScreenEvents$BeforeInitV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$BeforeInitV2$Type<T> = ($ScreenEvents$BeforeInitV2<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$BeforeInitV2_<T> = $ScreenEvents$BeforeInitV2$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/item/v2/$ToolTypeHelper" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ToolTypeHelper {

 "isShield"(arg0: $ItemStack$Type): boolean
 "isSword"(arg0: $ItemStack$Type): boolean
 "isAxe"(arg0: $ItemStack$Type): boolean
 "isMeleeWeapon"(arg0: $ItemStack$Type): boolean
 "isTool"(arg0: $ItemStack$Type): boolean
 "isMiningTool"(arg0: $ItemStack$Type): boolean
 "isCrossbow"(arg0: $ItemStack$Type): boolean
 "isTridentLike"(arg0: $ItemStack$Type): boolean
 "isRangedWeapon"(arg0: $ItemStack$Type): boolean
 "isShovel"(arg0: $ItemStack$Type): boolean
 "isBow"(arg0: $ItemStack$Type): boolean
 "isHoe"(arg0: $ItemStack$Type): boolean
 "isWeapon"(arg0: $ItemStack$Type): boolean
 "isFishingRod"(arg0: $ItemStack$Type): boolean
 "isPickaxe"(arg0: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
 "isTrident"(arg0: $ItemStack$Type): boolean
 "isShears"(arg0: $ItemStack$Type): boolean
}

export namespace $ToolTypeHelper {
const INSTANCE: $ToolTypeHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToolTypeHelper$Type = ($ToolTypeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToolTypeHelper_ = $ToolTypeHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Unwatch" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerChunkEvents$Unwatch {

 "onChunkUnwatch"(arg0: $ServerPlayer$Type, arg1: $ChunkPos$Type, arg2: $ServerLevel$Type): void

(arg0: $ServerPlayer$Type, arg1: $ChunkPos$Type, arg2: $ServerLevel$Type): void
}

export namespace $ServerChunkEvents$Unwatch {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerChunkEvents$Unwatch$Type = ($ServerChunkEvents$Unwatch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerChunkEvents$Unwatch_ = $ServerChunkEvents$Unwatch$Type;
}}
declare module "packages/fuzs/puzzleslib/api/resources/v1/$DynamicPackResources" {
import {$PackResources$ResourceOutput, $PackResources$ResourceOutput$Type} from "packages/net/minecraft/server/packs/$PackResources$ResourceOutput"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractModPackResources, $AbstractModPackResources$Type} from "packages/fuzs/puzzleslib/api/resources/v1/$AbstractModPackResources"
import {$IoSupplier, $IoSupplier$Type} from "packages/net/minecraft/server/packs/resources/$IoSupplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DataProviderContext$Factory, $DataProviderContext$Factory$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext$Factory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DynamicPackResources extends $AbstractModPackResources {
static readonly "PATHS_FOR_TYPE": $Map<(string), ($PackType)>


public static "create"(...arg0: ($DataProviderContext$Factory$Type)[]): $Supplier<($AbstractModPackResources)>
public "getResource"(arg0: $PackType$Type, arg1: $ResourceLocation$Type): $IoSupplier<($InputStream)>
public "listResources"(arg0: $PackType$Type, arg1: string, arg2: string, arg3: $PackResources$ResourceOutput$Type): void
public "getNamespaces"(arg0: $PackType$Type): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicPackResources$Type = ($DynamicPackResources);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicPackResources_ = $DynamicPackResources$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/data/v2/$AbstractModelProvider$ItemOverride$Factory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$AbstractModelProvider$ItemOverride, $AbstractModelProvider$ItemOverride$Type} from "packages/fuzs/puzzleslib/api/client/data/v2/$AbstractModelProvider$ItemOverride"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $AbstractModelProvider$ItemOverride$Factory extends $Function<($ResourceLocation), ($AbstractModelProvider$ItemOverride)> {

 "apply"(arg0: $ResourceLocation$Type): $AbstractModelProvider$ItemOverride
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($AbstractModelProvider$ItemOverride)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($ResourceLocation), (V)>

(arg0: $ResourceLocation$Type): $AbstractModelProvider$ItemOverride
}

export namespace $AbstractModelProvider$ItemOverride$Factory {
function identity<T>(): $Function<($ResourceLocation), ($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModelProvider$ItemOverride$Factory$Type = ($AbstractModelProvider$ItemOverride$Factory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModelProvider$ItemOverride$Factory_ = $AbstractModelProvider$ItemOverride$Factory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$StopTracking" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerEvents$StopTracking {

 "onStopTracking"(arg0: $Entity$Type, arg1: $ServerPlayer$Type): void

(arg0: $Entity$Type, arg1: $ServerPlayer$Type): void
}

export namespace $PlayerEvents$StopTracking {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$StopTracking$Type = ($PlayerEvents$StopTracking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$StopTracking_ = $PlayerEvents$StopTracking$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ComputeFovModifierCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"

export interface $ComputeFovModifierCallback {

 "onComputeFovModifier"(arg0: $Player$Type, arg1: $DefaultedFloat$Type): void

(arg0: $Player$Type, arg1: $DefaultedFloat$Type): void
}

export namespace $ComputeFovModifierCallback {
const EVENT: $EventInvoker<($ComputeFovModifierCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComputeFovModifierCallback$Type = ($ComputeFovModifierCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComputeFovModifierCallback_ = $ComputeFovModifierCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$BeforeInit" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

/**
 * 
 * @deprecated
 */
export interface $ScreenEvents$BeforeInit {

 "onBeforeInit"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>): void

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>): void
}

export namespace $ScreenEvents$BeforeInit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$BeforeInit$Type = ($ScreenEvents$BeforeInit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$BeforeInit_ = $ScreenEvents$BeforeInit$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$SimplePreparableReloadListener, $SimplePreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimplePreparableReloadListener"

export interface $AddReloadListenersContext {

 "registerReloadListener"(arg0: string, arg1: $PreparableReloadListener$Type): void
 "registerReloadListener"(arg0: string, arg1: $ResourceManagerReloadListener$Type): void
 "registerReloadListener"<T>(arg0: string, arg1: $SimplePreparableReloadListener$Type<(T)>): void

(arg0: string, arg1: $PreparableReloadListener$Type): void
}

export namespace $AddReloadListenersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddReloadListenersContext$Type = ($AddReloadListenersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddReloadListenersContext_ = $AddReloadListenersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/$ConfigDataHolder" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $ConfigDataHolder<T extends $ConfigCore> {

 "accept"(arg0: $Runnable$Type): void
 "accept"(arg0: $Consumer$Type<(T)>): void
 "isAvailable"(): boolean
 "getConfig"(): T
}

export namespace $ConfigDataHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigDataHolder$Type<T> = ($ConfigDataHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigDataHolder_<T> = $ConfigDataHolder$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$DistType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

/**
 * 
 * @deprecated
 */
export class $DistType extends $Enum<($DistType)> {
static readonly "CLIENT": $DistType
static readonly "SERVER": $DistType


public static "values"(): ($DistType)[]
public static "valueOf"(arg0: string): $DistType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DistType$Type = (("server") | ("client")) | ($DistType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DistType_ = $DistType$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntitySpectatorShaderContext" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $EntitySpectatorShaderContext {

 "registerSpectatorShader"(arg0: $ResourceLocation$Type, ...arg1: ($EntityType$Type<(any)>)[]): void

(arg0: $ResourceLocation$Type, ...arg1: ($EntityType$Type<(any)>)[]): void
}

export namespace $EntitySpectatorShaderContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntitySpectatorShaderContext$Type = ($EntitySpectatorShaderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntitySpectatorShaderContext_ = $EntitySpectatorShaderContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v3/tags/$TypedTagFactory" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $TypedTagFactory<T> {
static readonly "BLOCK": $TypedTagFactory<($Block)>
static readonly "ITEM": $TypedTagFactory<($Item)>
static readonly "FLUID": $TypedTagFactory<($Fluid)>
static readonly "ENTITY_TYPE": $TypedTagFactory<($EntityType<(any)>)>
static readonly "ENCHANTMENT": $TypedTagFactory<($Enchantment)>
static readonly "BIOME": $TypedTagFactory<($Biome)>
static readonly "GAME_EVENT": $TypedTagFactory<($GameEvent)>
static readonly "DAMAGE_TYPE": $TypedTagFactory<($DamageType)>


public static "make"<T>(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>): $TypedTagFactory<(T)>
public "make"(arg0: $ResourceLocation$Type): $TagKey<(T)>
public "make"(arg0: string, arg1: string): $TagKey<(T)>
public "common"(arg0: string): $TagKey<(T)>
public "forge"(arg0: string): $TagKey<(T)>
public "minecraft"(arg0: string): $TagKey<(T)>
public "fabric"(arg0: string): $TagKey<(T)>
public "trinkets"(arg0: string): $TagKey<(T)>
public "curios"(arg0: string): $TagKey<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypedTagFactory$Type<T> = ($TypedTagFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypedTagFactory_<T> = $TypedTagFactory$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/biome/$ClimateSettingsContextForge" {
import {$ClimateSettingsBuilder, $ClimateSettingsBuilder$Type} from "packages/net/minecraftforge/common/world/$ClimateSettingsBuilder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Biome$TemperatureModifier, $Biome$TemperatureModifier$Type} from "packages/net/minecraft/world/level/biome/$Biome$TemperatureModifier"
import {$ClimateSettingsContext, $ClimateSettingsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$ClimateSettingsContext"

export class $ClimateSettingsContextForge extends $Record implements $ClimateSettingsContext {

constructor(context: $ClimateSettingsBuilder$Type)

public "context"(): $ClimateSettingsBuilder
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "setTemperatureModifier"(arg0: $Biome$TemperatureModifier$Type): void
public "getTemperature"(): float
public "setDownfall"(arg0: float): void
public "setTemperature"(arg0: float): void
public "hasPrecipitation"(arg0: boolean): void
public "hasPrecipitation"(): boolean
set "temperatureModifier"(value: $Biome$TemperatureModifier$Type)
get "temperature"(): float
set "downfall"(value: float)
set "temperature"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClimateSettingsContextForge$Type = ($ClimateSettingsContextForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClimateSettingsContextForge_ = $ClimateSettingsContextForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractLanguageProvider" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$RegistryReference, $RegistryReference$Type} from "packages/fuzs/puzzleslib/api/init/v2/$RegistryReference"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$LanguageProvider, $LanguageProvider$Type} from "packages/net/minecraftforge/common/data/$LanguageProvider"
import {$GameRules$Key, $GameRules$Key$Type} from "packages/net/minecraft/world/level/$GameRules$Key"
import {$StatType, $StatType$Type} from "packages/net/minecraft/stats/$StatType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $AbstractLanguageProvider extends $LanguageProvider {

constructor(arg0: $GatherDataEvent$Type, arg1: string)
constructor(arg0: $PackOutput$Type, arg1: string)

/**
 * 
 * @deprecated
 */
public "addAdditional"(arg0: $EntityType$Type<(any)>, arg1: string, arg2: string): void
/**
 * 
 * @deprecated
 */
public "addAdditional"(arg0: $MobEffect$Type, arg1: string, arg2: string): void
/**
 * 
 * @deprecated
 */
public "addAdditional"(arg0: $Enchantment$Type, arg1: string, arg2: string): void
/**
 * 
 * @deprecated
 */
public "addAdditional"(arg0: $Block$Type, arg1: string, arg2: string): void
/**
 * 
 * @deprecated
 */
public "addAdditional"(arg0: $Item$Type, arg1: string, arg2: string): void
public "add"(arg0: string, arg1: $ResourceLocation$Type, arg2: string): void
public "add"(arg0: $Enchantment$Type, arg1: string, arg2: string): void
public "add"(arg0: string, arg1: $ResourceKey$Type<(any)>, arg2: string): void
public "add"(arg0: $Item$Type, arg1: string, arg2: string): void
public "add"(arg0: $EntityType$Type<(any)>, arg1: string, arg2: string): void
public "add"(arg0: string, arg1: string, arg2: string): void
public "add"(arg0: $StatType$Type<(any)>, arg1: string, arg2: string): void
public "add"(arg0: $Attribute$Type, arg1: string, arg2: string): void
public "add"(arg0: $ResourceLocation$Type, arg1: string, arg2: string): void
public "add"(arg0: string, arg1: $RegistryReference$Type<(any)>, arg2: string): void
public "add"(arg0: $MobEffect$Type, arg1: string, arg2: string): void
public "add"(arg0: $SoundEvent$Type, arg1: string): void
public "add"(arg0: $Potion$Type, arg1: string): void
public "add"(arg0: $StatType$Type<(any)>, arg1: string): void
public "add"(arg0: $Attribute$Type, arg1: string): void
public "add"(arg0: $CreativeModeTab$Type, arg1: string): void
public "add"(arg0: $Block$Type, arg1: string, arg2: string): void
public "add"(arg0: $GameRules$Key$Type<(any)>, arg1: string): void
public "add"(arg0: $ResourceLocation$Type, arg1: string): void
public "add"(arg0: $KeyMapping$Type, arg1: string): void
public "addCreativeModeTab"(arg0: string): void
public "addCreativeModeTab"(arg0: string, arg1: string): void
public "addCreativeModeTab"(arg0: $ResourceLocation$Type, arg1: string): void
public "addCreativeModeTab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, arg1: string): void
/**
 * 
 * @deprecated
 */
public "addDamageType"(arg0: $ResourceKey$Type<($DamageType$Type)>, arg1: string): void
/**
 * 
 * @deprecated
 */
public "addDamageSource"(arg0: string, arg1: string): void
public "addItemDamageType"(arg0: $ResourceKey$Type<($DamageType$Type)>, arg1: string): void
public "addGenericDamageType"(arg0: $ResourceKey$Type<($DamageType$Type)>, arg1: string): void
public "addGameRuleDescription"(arg0: $GameRules$Key$Type<(any)>, arg1: string): void
public "addPlayerDamageType"(arg0: $ResourceKey$Type<($DamageType$Type)>, arg1: string): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractLanguageProvider$Type = ($AbstractLanguageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractLanguageProvider_ = $AbstractLanguageProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeModContainer" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IModInfo, $IModInfo$Type} from "packages/net/minecraftforge/forgespi/language/$IModInfo"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$URI, $URI$Type} from "packages/java/net/$URI"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ModContainer, $ModContainer$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModContainer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ForgeModContainer implements $ModContainer {

constructor(arg0: $IModInfo$Type)

public "findResource"(...arg0: (string)[]): $Optional<($Path)>
public "getParent"(): $ModContainer
public "setParent"(arg0: $ForgeModContainer$Type): void
public "getDisplayName"(): string
public "getVersion"(): string
public "getChildren"(): $Collection<($ModContainer)>
public "getDescription"(): string
public "getURI"(): $URI
public "getAuthors"(): $Collection<(string)>
public "getCredits"(): $Collection<(string)>
public "getContactTypes"(): $Map<(string), (string)>
public "getLicenses"(): $Collection<(string)>
public "getModId"(): string
public static "toModList"(arg0: $Supplier$Type<($Stream$Type<(any)>)>): $Map<(string), ($ModContainer)>
public "getAllChildren"(): $Stream<($ModContainer)>
get "parent"(): $ModContainer
set "parent"(value: $ForgeModContainer$Type)
get "displayName"(): string
get "version"(): string
get "children"(): $Collection<($ModContainer)>
get "description"(): string
get "uRI"(): $URI
get "authors"(): $Collection<(string)>
get "credits"(): $Collection<(string)>
get "contactTypes"(): $Map<(string), (string)>
get "licenses"(): $Collection<(string)>
get "modId"(): string
get "allChildren"(): $Stream<($ModContainer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModContainer$Type = ($ForgeModContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModContainer_ = $ForgeModContainer$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueDefaultedBoolean" {
import {$DefaultedBoolean, $DefaultedBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedBoolean"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ValueMutableBoolean, $ValueMutableBoolean$Type} from "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableBoolean"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MutableBoolean, $MutableBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean"

export class $ValueDefaultedBoolean extends $ValueMutableBoolean implements $DefaultedBoolean {

constructor(arg0: boolean)

public "accept"(arg0: boolean): void
public "getAsDefaultBoolean"(): boolean
public "getAsOptionalBoolean"(): $Optional<(boolean)>
public "mapDefaultBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
public static "fromValue"(arg0: boolean): $DefaultedBoolean
public static "fromEvent"(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>, arg2: $Supplier$Type<(boolean)>): $DefaultedBoolean
public "applyDefaultBoolean"(): void
public static "fromEvent"(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>): $MutableBoolean
get "asDefaultBoolean"(): boolean
get "asOptionalBoolean"(): $Optional<(boolean)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueDefaultedBoolean$Type = ($ValueDefaultedBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueDefaultedBoolean_ = $ValueDefaultedBoolean$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/$CapabilityController" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerRespawnCopyStrategy, $PlayerRespawnCopyStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerRespawnCopyStrategy"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$PlayerCapabilityKey, $PlayerCapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerCapabilityKey"
import {$CapabilityKey, $CapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityKey"
import {$SyncStrategy, $SyncStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncStrategy"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $CapabilityController {

 "registerBlockEntityCapability"<T extends $BlockEntity, C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<(T), (C)>, arg3: $Class$Type<(T)>): $CapabilityKey<(C)>
 "registerEntityCapability"<T extends $Entity, C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<(T), (C)>, arg3: $Class$Type<(T)>): $CapabilityKey<(C)>
 "registerLevelChunkCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($ChunkAccess$Type), (C)>): $CapabilityKey<(C)>
 "registerPlayerCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($Player$Type), (C)>, arg3: $PlayerRespawnCopyStrategy$Type, arg4: $SyncStrategy$Type): $PlayerCapabilityKey<(C)>
 "registerPlayerCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($Player$Type), (C)>, arg3: $PlayerRespawnCopyStrategy$Type): $PlayerCapabilityKey<(C)>
 "registerLevelCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($Level$Type), (C)>): $CapabilityKey<(C)>
}

export namespace $CapabilityController {
const CAPABILITY_KEY_REGISTRY: $Map<($ResourceLocation), ($CapabilityKey<(any)>)>
const VALID_CAPABILITY_TYPES: $Set<($Class<(any)>)>
function from(arg0: string): $CapabilityController
function retrieve(arg0: $ResourceLocation$Type): $CapabilityKey<(any)>
function submit<T>(arg0: $CapabilityKey$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CapabilityController$Type = ($CapabilityController);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CapabilityController_ = $CapabilityController$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$BiomeLoadingHandler" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"
import {$BiomeLoadingHandler$BiomeModification, $BiomeLoadingHandler$BiomeModification$Type} from "packages/fuzs/puzzleslib/impl/core/$BiomeLoadingHandler$BiomeModification"
import {$BiomeLoadingPhase, $BiomeLoadingPhase$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingPhase"

export class $BiomeLoadingHandler {

constructor()

public static "register"(arg0: string, arg1: $IEventBus$Type, arg2: $Multimap$Type<($BiomeLoadingPhase$Type), ($BiomeLoadingHandler$BiomeModification$Type)>): void
public static "buildPack"(arg0: string): $RepositorySource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeLoadingHandler$Type = ($BiomeLoadingHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeLoadingHandler_ = $BiomeLoadingHandler$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelTickEvents$Start" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientLevelTickEvents$Start {

 "onStartLevelTick"(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void

(arg0: $Minecraft$Type, arg1: $ClientLevel$Type): void
}

export namespace $ClientLevelTickEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelTickEvents$Start$Type = ($ClientLevelTickEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelTickEvents$Start_ = $ClientLevelTickEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v2/$WritableMessage" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MessageV2, $MessageV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageV2"
import {$MessageV2$MessageHandler, $MessageV2$MessageHandler$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageV2$MessageHandler"

export interface $WritableMessage<T extends $MessageV2<(T)>> extends $MessageV2<(T)> {

 "read"(arg0: $FriendlyByteBuf$Type): void
 "getHandler"(): $MessageV2$MessageHandler<(T)>
/**
 * 
 * @deprecated
 */
 "makeHandler"(): $MessageV2$MessageHandler<(T)>
 "write"(arg0: $FriendlyByteBuf$Type): void
}

export namespace $WritableMessage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WritableMessage$Type<T> = ($WritableMessage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WritableMessage_<T> = $WritableMessage$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventMutableInt" {
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export class $EventMutableInt implements $MutableInt {

constructor(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type)

public "accept"(arg0: integer): void
public "getAsInt"(): integer
public "mapInt"(arg0: $IntUnaryOperator$Type): void
public static "fromValue"(arg0: integer): $MutableInt
public static "fromEvent"(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type): $MutableInt
public "andThen"(arg0: $IntConsumer$Type): $IntConsumer
get "asInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventMutableInt$Type = ($EventMutableInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventMutableInt_ = $EventMutableInt$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$LivingEntityRenderLayersContextForgeImpl" {
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EntityRenderersEvent$AddLayers, $EntityRenderersEvent$AddLayers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$AddLayers"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$LivingEntityRenderLayersContext, $LivingEntityRenderLayersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LivingEntityRenderLayersContext"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LivingEntityRenderLayersContextForgeImpl extends $Record implements $LivingEntityRenderLayersContext {

constructor(evt: $EntityRenderersEvent$AddLayers$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerRenderLayer"<E extends $LivingEntity, T extends E, M extends $EntityModel<(T)>>(arg0: $EntityType$Type<(E)>, arg1: $BiFunction$Type<($RenderLayerParent$Type<(T), (M)>), ($EntityRendererProvider$Context$Type), ($RenderLayer$Type<(T), (M)>)>): void
public "evt"(): $EntityRenderersEvent$AddLayers
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEntityRenderLayersContextForgeImpl$Type = ($LivingEntityRenderLayersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEntityRenderLayersContextForgeImpl_ = $LivingEntityRenderLayersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderPlayerEvents" {
import {$RenderPlayerEvents$Before, $RenderPlayerEvents$Before$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderPlayerEvents$Before"
import {$RenderPlayerEvents$After, $RenderPlayerEvents$After$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$RenderPlayerEvents$After"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $RenderPlayerEvents {
static readonly "BEFORE": $EventInvoker<($RenderPlayerEvents$Before)>
static readonly "AFTER": $EventInvoker<($RenderPlayerEvents$After)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderPlayerEvents$Type = ($RenderPlayerEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderPlayerEvents_ = $RenderPlayerEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$ItemAttributeModifiersCallback" {
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $ItemAttributeModifiersCallback {

 "onItemAttributeModifiers"(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type, arg2: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>): void

(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type, arg2: $Multimap$Type<($Attribute$Type), ($AttributeModifier$Type)>): void
}

export namespace $ItemAttributeModifiersCallback {
const EVENT: $EventInvoker<($ItemAttributeModifiersCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemAttributeModifiersCallback$Type = ($ItemAttributeModifiersCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemAttributeModifiersCallback_ = $ItemAttributeModifiersCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderBlockOverlayCallback" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $RenderBlockOverlayCallback {

 "onRenderBlockOverlay"(arg0: $LocalPlayer$Type, arg1: $PoseStack$Type, arg2: $BlockState$Type): $EventResult

(arg0: $LocalPlayer$Type, arg1: $PoseStack$Type, arg2: $BlockState$Type): $EventResult
}

export namespace $RenderBlockOverlayCallback {
const EVENT: $EventInvoker<($RenderBlockOverlayCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderBlockOverlayCallback$Type = ($RenderBlockOverlayCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderBlockOverlayCallback_ = $RenderBlockOverlayCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$GameRenderEvents" {
import {$GameRenderEvents$Before, $GameRenderEvents$Before$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$GameRenderEvents$Before"
import {$GameRenderEvents$After, $GameRenderEvents$After$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$GameRenderEvents$After"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $GameRenderEvents {
static readonly "BEFORE": $EventInvoker<($GameRenderEvents$Before)>
static readonly "AFTER": $EventInvoker<($GameRenderEvents$After)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameRenderEvents$Type = ($GameRenderEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameRenderEvents_ = $GameRenderEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$BlockEntityRenderersContextForgeImpl" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityRendererProvider, $BlockEntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockEntityRenderersContext, $BlockEntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BlockEntityRenderersContext"

export class $BlockEntityRenderersContextForgeImpl extends $Record implements $BlockEntityRenderersContext {

constructor(context: $BlockEntityRenderersContext$Type)

public "context"(): $BlockEntityRenderersContext
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerBlockEntityRenderer"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockEntityRendererProvider$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityRenderersContextForgeImpl$Type = ($BlockEntityRenderersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityRenderersContextForgeImpl_ = $BlockEntityRenderersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents" {
import {$PlayerInteractEvents$AttackEntity, $PlayerInteractEvents$AttackEntity$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$AttackEntity"
import {$PlayerInteractEvents$UseItemV2, $PlayerInteractEvents$UseItemV2$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseItemV2"
import {$PlayerInteractEvents$AttackBlock, $PlayerInteractEvents$AttackBlock$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$AttackBlock"
import {$PlayerInteractEvents$UseEntityAt, $PlayerInteractEvents$UseEntityAt$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseEntityAt"
import {$PlayerInteractEvents$UseEntity, $PlayerInteractEvents$UseEntity$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseEntity"
import {$PlayerInteractEvents$UseBlock, $PlayerInteractEvents$UseBlock$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseBlock"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$PlayerInteractEvents$UseItem, $PlayerInteractEvents$UseItem$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseItem"
import {$PlayerInteractEvents$AttackBlockV2, $PlayerInteractEvents$AttackBlockV2$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$AttackBlockV2"

export class $PlayerInteractEvents {
static readonly "USE_BLOCK": $EventInvoker<($PlayerInteractEvents$UseBlock)>
/**
 * 
 * @deprecated
 */
static readonly "ATTACK_BLOCK": $EventInvoker<($PlayerInteractEvents$AttackBlock)>
static readonly "ATTACK_BLOCK_V2": $EventInvoker<($PlayerInteractEvents$AttackBlockV2)>
/**
 * 
 * @deprecated
 */
static readonly "USE_ITEM": $EventInvoker<($PlayerInteractEvents$UseItem)>
static readonly "USE_ITEM_V2": $EventInvoker<($PlayerInteractEvents$UseItemV2)>
static readonly "USE_ENTITY": $EventInvoker<($PlayerInteractEvents$UseEntity)>
static readonly "USE_ENTITY_AT": $EventInvoker<($PlayerInteractEvents$UseEntityAt)>
static readonly "ATTACK_ENTITY": $EventInvoker<($PlayerInteractEvents$AttackEntity)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$Type = ($PlayerInteractEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents_ = $PlayerInteractEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$GrindstoneEvents" {
import {$GrindstoneEvents$Update, $GrindstoneEvents$Update$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$GrindstoneEvents$Update"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$GrindstoneEvents$Use, $GrindstoneEvents$Use$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$GrindstoneEvents$Use"

export class $GrindstoneEvents {
static readonly "UPDATE": $EventInvoker<($GrindstoneEvents$Update)>
static readonly "USE": $EventInvoker<($GrindstoneEvents$Use)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrindstoneEvents$Type = ($GrindstoneEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrindstoneEvents_ = $GrindstoneEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$ServerMessageListener" {
import {$ServerGamePacketListenerImpl, $ServerGamePacketListenerImpl$Type} from "packages/net/minecraft/server/network/$ServerGamePacketListenerImpl"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $ServerMessageListener<T extends $Record> {

constructor()

public "handle"(arg0: T, arg1: $MinecraftServer$Type, arg2: $ServerGamePacketListenerImpl$Type, arg3: $ServerPlayer$Type, arg4: $ServerLevel$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerMessageListener$Type<T> = ($ServerMessageListener<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerMessageListener_<T> = $ServerMessageListener$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Tick" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $UseItemEvents$Tick {

 "onUseItemTick"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: $MutableInt$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: $MutableInt$Type): $EventResult
}

export namespace $UseItemEvents$Tick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemEvents$Tick$Type = ($UseItemEvents$Tick);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemEvents$Tick_ = $UseItemEvents$Tick$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$MovementInputUpdateCallback" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$Input, $Input$Type} from "packages/net/minecraft/client/player/$Input"

export interface $MovementInputUpdateCallback {

 "onMovementInputUpdate"(arg0: $LocalPlayer$Type, arg1: $Input$Type): void

(arg0: $LocalPlayer$Type, arg1: $Input$Type): void
}

export namespace $MovementInputUpdateCallback {
const EVENT: $EventInvoker<($MovementInputUpdateCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MovementInputUpdateCallback$Type = ($MovementInputUpdateCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MovementInputUpdateCallback_ = $MovementInputUpdateCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ClientboundModListMessage" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ClientMessageListener, $ClientMessageListener$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientMessageListener"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"

export class $ClientboundModListMessage extends $Record implements $ClientboundMessage<($ClientboundModListMessage)> {

constructor(modList: $Collection$Type<(string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getHandler"(): $ClientMessageListener<($ClientboundModListMessage)>
public "modList"(): $Collection<(string)>
get "handler"(): $ClientMessageListener<($ClientboundModListMessage)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundModListMessage$Type = ($ClientboundModListMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundModListMessage_ = $ClientboundModListMessage$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractParticleDescriptionProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$JsonCodecProvider, $JsonCodecProvider$Type} from "packages/net/minecraftforge/common/data/$JsonCodecProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AbstractParticleDescriptionProvider extends $JsonCodecProvider<($List<($ResourceLocation)>)> {

constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type, arg2: string)
constructor(arg0: $GatherDataEvent$Type, arg1: string)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractParticleDescriptionProvider$Type = ($AbstractParticleDescriptionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractParticleDescriptionProvider_ = $AbstractParticleDescriptionProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ItemTooltipCallback" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $ItemTooltipCallback {

 "onItemTooltip"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void

(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
}

export namespace $ItemTooltipCallback {
const EVENT: $EventInvoker<($ItemTooltipCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTooltipCallback$Type = ($ItemTooltipCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTooltipCallback_ = $ItemTooltipCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents$LoggedOut" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$MultiPlayerGameMode, $MultiPlayerGameMode$Type} from "packages/net/minecraft/client/multiplayer/$MultiPlayerGameMode"

export interface $ClientPlayerEvents$LoggedOut {

 "onLoggedOut"(arg0: $LocalPlayer$Type, arg1: $MultiPlayerGameMode$Type, arg2: $Connection$Type): void

(arg0: $LocalPlayer$Type, arg1: $MultiPlayerGameMode$Type, arg2: $Connection$Type): void
}

export namespace $ClientPlayerEvents$LoggedOut {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvents$LoggedOut$Type = ($ClientPlayerEvents$LoggedOut);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvents$LoggedOut_ = $ClientPlayerEvents$LoggedOut$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/biome/$GenerationSettingsContextForge" {
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$GenerationSettingsContext, $GenerationSettingsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$GenerationSettingsContext"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BiomeGenerationSettingsBuilder, $BiomeGenerationSettingsBuilder$Type} from "packages/net/minecraftforge/common/world/$BiomeGenerationSettingsBuilder"
import {$ConfiguredWorldCarver, $ConfiguredWorldCarver$Type} from "packages/net/minecraft/world/level/levelgen/carver/$ConfiguredWorldCarver"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$GenerationStep$Carving, $GenerationStep$Carving$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Carving"

export class $GenerationSettingsContextForge extends $Record implements $GenerationSettingsContext {

constructor(carvers: $Registry$Type<($ConfiguredWorldCarver$Type<(any)>)>, features: $Registry$Type<($PlacedFeature$Type)>, context: $BiomeGenerationSettingsBuilder$Type)
constructor(arg0: $BiomeGenerationSettingsBuilder$Type)
constructor(arg0: $RegistryAccess$Type, arg1: $BiomeGenerationSettingsBuilder$Type)

public "context"(): $BiomeGenerationSettingsBuilder
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "features"(): $Registry<($PlacedFeature)>
public "getFeatures"(arg0: $GenerationStep$Decoration$Type): $Iterable<($Holder<($PlacedFeature)>)>
public "carvers"(): $Registry<($ConfiguredWorldCarver<(any)>)>
public "removeFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $ResourceKey$Type<($PlacedFeature$Type)>): boolean
public "addFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $ResourceKey$Type<($PlacedFeature$Type)>): void
public "getCarvers"(arg0: $GenerationStep$Carving$Type): $Iterable<($Holder<($ConfiguredWorldCarver<(any)>)>)>
public "addCarver"(arg0: $GenerationStep$Carving$Type, arg1: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): void
public "removeCarver"(arg0: $GenerationStep$Carving$Type, arg1: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): boolean
public "removeFeature"(arg0: $ResourceKey$Type<($PlacedFeature$Type)>): boolean
public "removeCarver"(arg0: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenerationSettingsContextForge$Type = ($GenerationSettingsContextForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenerationSettingsContextForge_ = $GenerationSettingsContextForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$UseItemEvents$Finish" {
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $UseItemEvents$Finish {

 "onUseItemFinish"(arg0: $LivingEntity$Type, arg1: $MutableValue$Type<($ItemStack$Type)>, arg2: integer, arg3: $ItemStack$Type): void

(arg0: $LivingEntity$Type, arg1: $MutableValue$Type<($ItemStack$Type)>, arg2: integer, arg3: $ItemStack$Type): void
}

export namespace $UseItemEvents$Finish {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemEvents$Finish$Type = ($UseItemEvents$Finish);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemEvents$Finish_ = $UseItemEvents$Finish$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$NetworkHandlerV3$Builder, $NetworkHandlerV3$Builder$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $NetworkHandlerV3 {

 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $LevelChunk$Type, arg1: T): void
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: $ChunkPos$Type, arg2: T): void
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockEntity$Type, arg1: T): void
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T, arg2: boolean): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ResourceKey$Type<($Level$Type)>, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Level$Type, arg1: T): void
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $ServerLevel$Type, arg6: T): void
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: $ServerLevel$Type, arg4: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockPos$Type, arg1: $Level$Type, arg2: T): void
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Vec3i$Type, arg1: $ServerLevel$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Level$Type, arg5: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): void
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Collection$Type<($ServerPlayer$Type)>, arg1: $ServerPlayer$Type, arg2: T): void
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: T): void
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: T): void
 "sendTo"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
 "toServerboundPacket"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): $Packet<($ServerGamePacketListener)>
 "toClientboundPacket"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): $Packet<($ClientGamePacketListener)>
/**
 * 
 * @deprecated
 */
 "sendToAllTrackingAndSelf"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNearExcept"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Level$Type, arg6: T): void
 "sendToServer"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): void
}

export namespace $NetworkHandlerV3 {
function builder(arg0: string): $NetworkHandlerV3$Builder
function builder(arg0: string, arg1: string): $NetworkHandlerV3$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerV3$Type = ($NetworkHandlerV3);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerV3_ = $NetworkHandlerV3$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v2/$NetworkHandlerV2" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MessageDirection, $MessageDirection$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageDirection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$MessageV2, $MessageV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageV2"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $NetworkHandlerV2 {

/**
 * 
 * @deprecated
 */
 "register"<T extends $MessageV2<(T)>>(arg0: $Class$Type<(any)>, arg1: $Supplier$Type<(T)>, arg2: $MessageDirection$Type): void
 "sendToAllTracking"(arg0: $MessageV2$Type<(any)>, arg1: $Entity$Type): void
 "sendToAllExcept"(arg0: $MessageV2$Type<(any)>, arg1: $ServerPlayer$Type): void
 "sendToDimension"(arg0: $MessageV2$Type<(any)>, arg1: $Level$Type): void
 "sendToDimension"(arg0: $MessageV2$Type<(any)>, arg1: $ResourceKey$Type<($Level$Type)>): void
 "sendToAllNear"(arg0: $MessageV2$Type<(any)>, arg1: $BlockPos$Type, arg2: $Level$Type): void
 "sendToAllNear"(arg0: $MessageV2$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Level$Type): void
 "sendToAll"(arg0: $MessageV2$Type<(any)>): void
 "sendTo"(arg0: $MessageV2$Type<(any)>, arg1: $ServerPlayer$Type): void
 "toServerboundPacket"(arg0: $MessageV2$Type<(any)>): $Packet<($ServerGamePacketListener)>
 "toClientboundPacket"(arg0: $MessageV2$Type<(any)>): $Packet<($ClientGamePacketListener)>
 "registerClientbound"<T extends $MessageV2<(T)>>(arg0: $Class$Type<(T)>): $NetworkHandlerV2
 "sendToAllTrackingAndSelf"(arg0: $MessageV2$Type<(any)>, arg1: $Entity$Type): void
 "registerServerbound"<T extends $MessageV2<(T)>>(arg0: $Class$Type<(T)>): $NetworkHandlerV2
 "sendToAllNearExcept"(arg0: $MessageV2$Type<(any)>, arg1: $ServerPlayer$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Level$Type): void
 "sendToServer"(arg0: $MessageV2$Type<(any)>): void
}

export namespace $NetworkHandlerV2 {
function build(arg0: string): $NetworkHandlerV2
function build(arg0: string, arg1: string, arg2: boolean, arg3: boolean): $NetworkHandlerV2
function build(arg0: string, arg1: boolean, arg2: boolean): $NetworkHandlerV2
function build(arg0: string, arg1: string): $NetworkHandlerV2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerV2$Type = ($NetworkHandlerV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerV2_ = $NetworkHandlerV2$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/serialization/$MessageSerializers" {
import {$FriendlyByteBuf$Reader, $FriendlyByteBuf$Reader$Type} from "packages/net/minecraft/network/$FriendlyByteBuf$Reader"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$FriendlyByteBuf$Writer, $FriendlyByteBuf$Writer$Type} from "packages/net/minecraft/network/$FriendlyByteBuf$Writer"
import {$MessageSerializer, $MessageSerializer$Type} from "packages/fuzs/puzzleslib/api/network/v3/serialization/$MessageSerializer"

export class $MessageSerializers {


public static "findByType"<T>(arg0: $Class$Type<(T)>): $MessageSerializer<(T)>
public static "findByGenericType"(arg0: $Type$Type): $MessageSerializer<(any)>
public static "registerContainerProvider"<T>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(($Type$Type)[]), ($MessageSerializer$Type<(any)>)>): void
public static "registerSerializer"<T>(arg0: $Class$Type<(any)>, arg1: $ResourceKey$Type<($Registry$Type<(T)>)>): void
public static "registerSerializer"<T>(arg0: $Class$Type<(T)>, arg1: $FriendlyByteBuf$Writer$Type<(T)>, arg2: $FriendlyByteBuf$Reader$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageSerializers$Type = ($MessageSerializers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageSerializers_ = $MessageSerializers$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelEvents" {
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ClientLevelEvents$Unload, $ClientLevelEvents$Unload$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelEvents$Unload"
import {$ClientLevelEvents$Load, $ClientLevelEvents$Load$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelEvents$Load"

export class $ClientLevelEvents {
static readonly "LOAD": $EventInvoker<($ClientLevelEvents$Load)>
static readonly "UNLOAD": $EventInvoker<($ClientLevelEvents$Unload)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelEvents$Type = ($ClientLevelEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelEvents_ = $ClientLevelEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderPlayerEvents$Before" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$PlayerRenderer, $PlayerRenderer$Type} from "packages/net/minecraft/client/renderer/entity/player/$PlayerRenderer"

export interface $RenderPlayerEvents$Before {

 "onBeforeRenderPlayer"(arg0: $Player$Type, arg1: $PlayerRenderer$Type, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): $EventResult

(arg0: $Player$Type, arg1: $PlayerRenderer$Type, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): $EventResult
}

export namespace $RenderPlayerEvents$Before {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderPlayerEvents$Before$Type = ($RenderPlayerEvents$Before);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderPlayerEvents$Before_ = $RenderPlayerEvents$Before$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$BonemealCallback" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BonemealCallback {

 "onBonemeal"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ItemStack$Type): $EventResult

(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ItemStack$Type): $EventResult
}

export namespace $BonemealCallback {
const EVENT: $EventInvoker<($BonemealCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BonemealCallback$Type = ($BonemealCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BonemealCallback_ = $BonemealCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$DataPackSourcesContextForgeImpl" {
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"

export class $DataPackSourcesContextForgeImpl extends $Record implements $PackRepositorySourcesContext {

constructor(consumer: $Consumer$Type<($RepositorySource$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $Consumer<($RepositorySource)>
public "addRepositorySource"(...arg0: ($RepositorySource$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataPackSourcesContextForgeImpl$Type = ($DataPackSourcesContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataPackSourcesContextForgeImpl_ = $DataPackSourcesContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Tick" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingEvents$Tick {

 "onLivingTick"(arg0: $LivingEntity$Type): $EventResult

(arg0: $LivingEntity$Type): $EventResult
}

export namespace $LivingEvents$Tick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEvents$Tick$Type = ($LivingEvents$Tick);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEvents$Tick_ = $LivingEvents$Tick$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$CreativeModeTabContextForgeImpl" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CreativeModeTabContext, $CreativeModeTabContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$CreativeModeTabContext"
import {$CreativeModeTabConfigurator, $CreativeModeTabConfigurator$Type} from "packages/fuzs/puzzleslib/api/item/v2/$CreativeModeTabConfigurator"

export class $CreativeModeTabContextForgeImpl extends $Record implements $CreativeModeTabContext {

constructor(modEventBus: $IEventBus$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "registerCreativeModeTab"(arg0: $CreativeModeTabConfigurator$Type): void
public "modEventBus"(): $IEventBus
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeModeTabContextForgeImpl$Type = ($CreativeModeTabContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeModeTabContextForgeImpl_ = $CreativeModeTabContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientChunkEvents$Load" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"

export interface $ClientChunkEvents$Load {

 "onChunkLoad"(arg0: $ClientLevel$Type, arg1: $LevelChunk$Type): void

(arg0: $ClientLevel$Type, arg1: $LevelChunk$Type): void
}

export namespace $ClientChunkEvents$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChunkEvents$Load$Type = ($ClientChunkEvents$Load);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChunkEvents$Load_ = $ClientChunkEvents$Load$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$Proxy" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"

export interface $Proxy {

 "hasShiftDown"(): boolean
 "getClientLevel"(): $Level
/**
 * 
 * @deprecated
 */
 "getGameServer"(): $MinecraftServer
 "hasControlDown"(): boolean
 "hasAltDown"(): boolean
 "getClientPlayer"(): $Player
 "getClientPacketListener"(): $ClientPacketListener
 "getKeyMappingComponent"(arg0: string): $Component
}

export namespace $Proxy {
const INSTANCE: $Proxy
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Proxy$Type = ($Proxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Proxy_ = $Proxy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder" {
import {$ConfigDataHolder, $ConfigDataHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigDataHolder"
import {$ConfigHolderRegistry, $ConfigHolderRegistry$Type} from "packages/fuzs/puzzleslib/impl/config/$ConfigHolderRegistry"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Buildable, $Buildable$Type} from "packages/fuzs/puzzleslib/api/core/v1/$Buildable"

export interface $ConfigHolder$Builder extends $ConfigHolderRegistry, $Buildable {

 "common"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigHolder$Builder
 "client"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigHolder$Builder
 "server"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigHolder$Builder
 "setFileName"<T extends $ConfigCore>(arg0: $Class$Type<(T)>, arg1: $UnaryOperator$Type<(string)>): $ConfigHolder$Builder
/**
 * 
 * @deprecated
 */
 "get"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): T
/**
 * 
 * @deprecated
 */
 "getHolder"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigDataHolder<(T)>
 "build"(): void
}

export namespace $ConfigHolder$Builder {
function builder(arg0: string): $ConfigHolder$Builder
function simpleName(arg0: string): string
function moveToDir(arg0: string, arg1: string): string
function defaultName(arg0: string, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHolder$Builder$Type = ($ConfigHolder$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHolder$Builder_ = $ConfigHolder$Builder$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/client/accessor/$ModelBakeryAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ModelBakeryAccessor {

}

export namespace $ModelBakeryAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelBakeryAccessor$Type = ($ModelBakeryAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelBakeryAccessor_ = $ModelBakeryAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$ModLoader" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ModLoader extends $Enum<($ModLoader)> {
static readonly "FABRIC": $ModLoader
static readonly "NEOFORGE": $ModLoader
static readonly "FORGE": $ModLoader
static readonly "QUILT": $ModLoader


public static "values"(): ($ModLoader)[]
public static "valueOf"(arg0: string): $ModLoader
public "isNeoForge"(): boolean
public "isFabric"(): boolean
public "isFabricLike"(): boolean
public "isForge"(): boolean
public "isQuilt"(): boolean
public static "getForgeLike"(): ($ModLoader)[]
public "isForgeLike"(): boolean
public static "getFabricLike"(): ($ModLoader)[]
get "neoForge"(): boolean
get "fabric"(): boolean
get "fabricLike"(): boolean
get "forge"(): boolean
get "quilt"(): boolean
get "forgeLike"(): ($ModLoader)[]
get "forgeLike"(): boolean
get "fabricLike"(): ($ModLoader)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoader$Type = (("quilt") | ("fabric") | ("forge") | ("neoforge")) | ($ModLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoader_ = $ModLoader$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Affects" {
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $MobEffectEvents$Affects {

 "onMobEffectAffects"(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type): $EventResult
}

export namespace $MobEffectEvents$Affects {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobEffectEvents$Affects$Type = ($MobEffectEvents$Affects);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobEffectEvents$Affects_ = $MobEffectEvents$Affects$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$EventHandlerProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EventHandlerProvider {

 "registerHandlers"(): void

(arg0: any): void
}

export namespace $EventHandlerProvider {
function tryRegister(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerProvider$Type = ($EventHandlerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerProvider_ = $EventHandlerProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventMutableBoolean" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MutableBoolean, $MutableBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean"

export class $EventMutableBoolean implements $MutableBoolean {

constructor(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>)

public "accept"(arg0: boolean): void
public "getAsBoolean"(): boolean
public static "fromValue"(arg0: boolean): $MutableBoolean
public static "fromEvent"(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>): $MutableBoolean
public "mapBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
get "asBoolean"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventMutableBoolean$Type = ($EventMutableBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventMutableBoolean_ = $EventMutableBoolean$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelEvents$Load" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ServerLevelEvents$Load {

 "onLevelLoad"(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void

(arg0: $MinecraftServer$Type, arg1: $ServerLevel$Type): void
}

export namespace $ServerLevelEvents$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelEvents$Load$Type = ($ServerLevelEvents$Load);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelEvents$Load_ = $ServerLevelEvents$Load$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/$AbstractDamageTypeProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$JsonCodecProvider, $JsonCodecProvider$Type} from "packages/net/minecraftforge/common/data/$JsonCodecProvider"

/**
 * 
 * @deprecated
 */
export class $AbstractDamageTypeProvider extends $JsonCodecProvider<($DamageType)> {

constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type, arg2: string)
constructor(arg0: $GatherDataEvent$Type, arg1: string)
/**
 * 
 * @deprecated
 */
constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type)
/**
 * 
 * @deprecated
 */
constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractDamageTypeProvider$Type = ($AbstractDamageTypeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractDamageTypeProvider_ = $AbstractDamageTypeProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/resources/v1/$PackResourcesHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractModPackResources, $AbstractModPackResources$Type} from "packages/fuzs/puzzleslib/api/resources/v1/$AbstractModPackResources"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PackResourcesHelper {


/**
 * 
 * @deprecated
 */
public static "buildServerPack"(arg0: $Supplier$Type<($AbstractModPackResources$Type)>, arg1: string, arg2: $Component$Type, arg3: $Component$Type, arg4: boolean, arg5: boolean): $RepositorySource
public static "buildServerPack"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($AbstractModPackResources$Type)>, arg2: boolean): $RepositorySource
public static "buildServerPack"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($AbstractModPackResources$Type)>, arg2: $Component$Type, arg3: $Component$Type, arg4: boolean, arg5: boolean, arg6: boolean): $RepositorySource
public static "buildClientPack"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($AbstractModPackResources$Type)>, arg2: $Component$Type, arg3: $Component$Type, arg4: boolean, arg5: boolean, arg6: boolean): $RepositorySource
/**
 * 
 * @deprecated
 */
public static "buildClientPack"(arg0: $Supplier$Type<($AbstractModPackResources$Type)>, arg1: string, arg2: $Component$Type, arg3: $Component$Type, arg4: boolean, arg5: boolean): $RepositorySource
public static "buildClientPack"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($AbstractModPackResources$Type)>, arg2: boolean): $RepositorySource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackResourcesHelper$Type = ($PackResourcesHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackResourcesHelper_ = $PackResourcesHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$ArmorMaterialImpl" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ArmorItem$Type, $ArmorItem$Type$Type} from "packages/net/minecraft/world/item/$ArmorItem$Type"

export class $ArmorMaterialImpl extends $Record implements $ArmorMaterial {

constructor(name: string, durabilityMultiplier: integer, protectionAmounts: (integer)[], enchantability: integer, equipSound: $Supplier$Type<($SoundEvent$Type)>, toughness: float, knockbackResistance: float, repairIngredient: $Supplier$Type<($Ingredient$Type)>)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "protectionAmounts"(): (integer)[]
public "durabilityMultiplier"(): integer
public "knockbackResistance"(): float
public "equipSound"(): $Supplier<($SoundEvent)>
public "toughness"(): float
public "repairIngredient"(): $Supplier<($Ingredient)>
public "enchantability"(): integer
public "getDefenseForType"(arg0: $ArmorItem$Type$Type): integer
public "getEnchantmentValue"(): integer
public "getName"(): string
public "getToughness"(): float
public "getKnockbackResistance"(): float
public "getEquipSound"(): $SoundEvent
public "getRepairIngredient"(): $Ingredient
public "getDurabilityForType"(arg0: $ArmorItem$Type$Type): integer
get "enchantmentValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArmorMaterialImpl$Type = ($ArmorMaterialImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArmorMaterialImpl_ = $ArmorMaterialImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v3/tags/$BoundTagFactory" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BoundTagFactory {
static readonly "MINECRAFT": $BoundTagFactory
static readonly "COMMON": $BoundTagFactory
static readonly "FABRIC": $BoundTagFactory
static readonly "FORGE": $BoundTagFactory
static readonly "CURIOS": $BoundTagFactory
static readonly "TRINKETS": $BoundTagFactory


public static "make"(arg0: string): $BoundTagFactory
public "registerBiomeTag"(arg0: string): $TagKey<($Biome)>
public "registerBlockTag"(arg0: string): $TagKey<($Block)>
public "registerItemTag"(arg0: string): $TagKey<($Item)>
public "registerTagKey"<T>(arg0: $ResourceKey$Type<(any)>, arg1: string): $TagKey<(T)>
public "registerFluidTag"(arg0: string): $TagKey<($Fluid)>
public "registerEnchantmentTag"(arg0: string): $TagKey<($Enchantment)>
public "registerDamageTypeTag"(arg0: string): $TagKey<($DamageType)>
public "registerGameEventTag"(arg0: string): $TagKey<($GameEvent)>
public "registerEntityTypeTag"(arg0: string): $TagKey<($EntityType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoundTagFactory$Type = ($BoundTagFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoundTagFactory_ = $BoundTagFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v3/$RegistryHelper" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Holder$Reference, $Holder$Reference$Type} from "packages/net/minecraft/core/$Holder$Reference"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $RegistryHelper {


public static "is"<T>(arg0: $TagKey$Type<(T)>, arg1: T): boolean
public static "findRegistry"<T>(arg0: $ResourceKey$Type<(any)>): $Registry<(T)>
public static "getResourceKey"<T>(arg0: $ResourceKey$Type<(any)>, arg1: T): $Optional<($ResourceKey<(T)>)>
public static "getHolder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: T): $Optional<($Holder$Reference<(T)>)>
public static "findBuiltInRegistry"<T>(arg0: $ResourceKey$Type<(any)>): $Registry<(T)>
public static "getResourceKeyOrThrow"<T>(arg0: $ResourceKey$Type<(any)>, arg1: T): $ResourceKey<(T)>
public static "wrapAsHolder"<T>(arg0: $ResourceKey$Type<(any)>, arg1: T): $Holder<(T)>
public static "getHolderOrThrow"<T>(arg0: $ResourceKey$Type<(any)>, arg1: T): $Holder$Reference<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryHelper$Type = ($RegistryHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryHelper_ = $RegistryHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventDefaultedInt" {
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$EventMutableInt, $EventMutableInt$Type} from "packages/fuzs/puzzleslib/impl/event/data/$EventMutableInt"
import {$DefaultedInt, $DefaultedInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedInt"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export class $EventDefaultedInt extends $EventMutableInt implements $DefaultedInt {

constructor(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type, arg2: $IntSupplier$Type)

public "accept"(arg0: integer): void
public "getAsOptionalInt"(): $OptionalInt
public "getAsDefaultInt"(): integer
public static "fromValue"(arg0: integer): $DefaultedInt
public static "fromEvent"(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type, arg2: $IntSupplier$Type): $DefaultedInt
public "applyDefaultInt"(): void
public "mapDefaultInt"(arg0: $IntUnaryOperator$Type): void
public static "fromEvent"(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type): $MutableInt
get "asOptionalInt"(): $OptionalInt
get "asDefaultInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventDefaultedInt$Type = ($EventDefaultedInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventDefaultedInt_ = $EventDefaultedInt$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$MobSpawnSettingsBuilderForgeAccessor" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$MobSpawnSettings$MobSpawnCost, $MobSpawnSettings$MobSpawnCost$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$MobSpawnCost"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $MobSpawnSettingsBuilderForgeAccessor {

 "puzzleslib$getMobSpawnCosts"(): $Map<($EntityType<(any)>), ($MobSpawnSettings$MobSpawnCost)>

(): $Map<($EntityType<(any)>), ($MobSpawnSettings$MobSpawnCost)>
}

export namespace $MobSpawnSettingsBuilderForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnSettingsBuilderForgeAccessor$Type = ($MobSpawnSettingsBuilderForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnSettingsBuilderForgeAccessor_ = $MobSpawnSettingsBuilderForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$BiomeModificationsContextForgeImpl" {
import {$BiomeModificationContext, $BiomeModificationContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeModificationContext"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BiomeLoadingContext, $BiomeLoadingContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingContext"
import {$BiomeModificationsContext, $BiomeModificationsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BiomeModificationsContext"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$BiomeLoadingHandler$BiomeModification, $BiomeLoadingHandler$BiomeModification$Type} from "packages/fuzs/puzzleslib/impl/core/$BiomeLoadingHandler$BiomeModification"
import {$BiomeLoadingPhase, $BiomeLoadingPhase$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingPhase"

export class $BiomeModificationsContextForgeImpl extends $Record implements $BiomeModificationsContext {

constructor(biomeEntries: $Multimap$Type<($BiomeLoadingPhase$Type), ($BiomeLoadingHandler$BiomeModification$Type)>, availableFlags: $Set$Type<($ContentRegistrationFlags$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "register"(arg0: $BiomeLoadingPhase$Type, arg1: $Predicate$Type<($BiomeLoadingContext$Type)>, arg2: $Consumer$Type<($BiomeModificationContext$Type)>): void
public "availableFlags"(): $Set<($ContentRegistrationFlags)>
public "biomeEntries"(): $Multimap<($BiomeLoadingPhase), ($BiomeLoadingHandler$BiomeModification)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModificationsContextForgeImpl$Type = ($BiomeModificationsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModificationsContextForgeImpl_ = $BiomeModificationsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientEntityLevelEvents" {
import {$ClientEntityLevelEvents$Load, $ClientEntityLevelEvents$Load$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientEntityLevelEvents$Load"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ClientEntityLevelEvents$Unload, $ClientEntityLevelEvents$Unload$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientEntityLevelEvents$Unload"

export class $ClientEntityLevelEvents {
static readonly "LOAD": $EventInvoker<($ClientEntityLevelEvents$Load)>
static readonly "UNLOAD": $EventInvoker<($ClientEntityLevelEvents$Unload)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEntityLevelEvents$Type = ($ClientEntityLevelEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEntityLevelEvents_ = $ClientEntityLevelEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/core/$ForgeModConfig" {
import {$IConfigSpec, $IConfigSpec$Type} from "packages/net/minecraftforge/fml/config/$IConfigSpec"
import {$ModContainer, $ModContainer$Type} from "packages/net/minecraftforge/fml/$ModContainer"
import {$ConfigFileTypeHandler, $ConfigFileTypeHandler$Type} from "packages/net/minecraftforge/fml/config/$ConfigFileTypeHandler"
import {$ModConfig, $ModConfig$Type} from "packages/net/minecraftforge/fml/config/$ModConfig"
import {$ModConfig$Type, $ModConfig$Type$Type} from "packages/net/minecraftforge/fml/config/$ModConfig$Type"

export class $ForgeModConfig extends $ModConfig {

constructor(arg0: $ModConfig$Type$Type, arg1: $IConfigSpec$Type<(any)>, arg2: $ModContainer$Type, arg3: string)

public "getHandler"(): $ConfigFileTypeHandler
get "handler"(): $ConfigFileTypeHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModConfig$Type = ($ForgeModConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModConfig_ = $ForgeModConfig$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$ItemDecorationContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ItemDecorationContext, $ItemDecorationContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemDecorationContext"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$IItemDecorator, $IItemDecorator$Type} from "packages/net/minecraftforge/client/$IItemDecorator"
import {$DynamicItemDecorator, $DynamicItemDecorator$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicItemDecorator"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $ItemDecorationContextForgeImpl extends $Record implements $ItemDecorationContext {

constructor(consumer: $BiConsumer$Type<($ItemLike$Type), ($IItemDecorator$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $BiConsumer<($ItemLike), ($IItemDecorator)>
public "registerItemDecorator"(arg0: $DynamicItemDecorator$Type, ...arg1: ($ItemLike$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemDecorationContextForgeImpl$Type = ($ItemDecorationContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemDecorationContextForgeImpl_ = $ItemDecorationContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/client/$AbstractModelProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ModelFile$ExistingModelFile, $ModelFile$ExistingModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile$ExistingModelFile"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WallSide, $WallSide$Type} from "packages/net/minecraft/world/level/block/state/properties/$WallSide"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockStateProvider, $BlockStateProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockStateProvider"
import {$ForgeDataProviderContext, $ForgeDataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"

/**
 * 
 * @deprecated
 */
export class $AbstractModelProvider extends $BlockStateProvider {
static readonly "WALL_PROPS": $ImmutableMap<($Direction), ($Property<($WallSide)>)>

constructor(arg0: $ForgeDataProviderContext$Type)
constructor(arg0: string, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type)

public "name"(arg0: $Block$Type): string
public "key"(arg0: $Block$Type): $ResourceLocation
public "extend"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
public "extendKey"(arg0: $Block$Type, ...arg1: (string)[]): $ResourceLocation
public "builtInBlock"(arg0: $Block$Type, arg1: $ResourceLocation$Type): void
public "builtInBlock"(arg0: $Block$Type, arg1: $Block$Type): void
public "existingBlockModel"(arg0: $Block$Type): $ModelFile$ExistingModelFile
public "cubeBottomTopBlock"(arg0: $Block$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): void
public "cubeBottomTopBlock"(arg0: $Block$Type): void
public "simpleExistingBlockWithItem"(arg0: $Block$Type): void
public "simpleExistingBlock"(arg0: $Block$Type): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModelProvider$Type = ($AbstractModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModelProvider_ = $AbstractModelProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelTickEvents" {
import {$ServerLevelTickEvents$End, $ServerLevelTickEvents$End$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelTickEvents$End"
import {$ServerLevelTickEvents$Start, $ServerLevelTickEvents$Start$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerLevelTickEvents$Start"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ServerLevelTickEvents {
static readonly "START": $EventInvoker<($ServerLevelTickEvents$Start)>
static readonly "END": $EventInvoker<($ServerLevelTickEvents$End)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelTickEvents$Type = ($ServerLevelTickEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelTickEvents_ = $ServerLevelTickEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $MutableFloat {

 "accept"(arg0: float): void
 "getAsFloat"(): float
 "mapFloat"(arg0: $UnaryOperator$Type<(float)>): void
}

export namespace $MutableFloat {
function fromValue(arg0: float): $MutableFloat
function fromEvent(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>): $MutableFloat
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableFloat$Type = ($MutableFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableFloat_ = $MutableFloat$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$EntitySpectatorShaderContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EntitySpectatorShaderContext, $EntitySpectatorShaderContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntitySpectatorShaderContext"

export class $EntitySpectatorShaderContextForgeImpl extends $Record implements $EntitySpectatorShaderContext {

constructor(consumer: $BiConsumer$Type<($EntityType$Type<(any)>), ($ResourceLocation$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "consumer"(): $BiConsumer<($EntityType<(any)>), ($ResourceLocation)>
public "registerSpectatorShader"(arg0: $ResourceLocation$Type, ...arg1: ($EntityType$Type<(any)>)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntitySpectatorShaderContextForgeImpl$Type = ($EntitySpectatorShaderContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntitySpectatorShaderContextForgeImpl_ = $EntitySpectatorShaderContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeEnvironment" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ModLoader, $ModLoader$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoader"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ObjectShareAccess, $ObjectShareAccess$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ObjectShareAccess"
import {$ModContainer, $ModContainer$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModContainer"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$DistType, $DistType$Type} from "packages/fuzs/puzzleslib/api/core/v1/$DistType"
import {$ModLoaderEnvironment, $ModLoaderEnvironment$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ModLoaderEnvironment"

export class $ForgeEnvironment implements $ModLoaderEnvironment {

constructor()

public "getObjectShareAccess"(): $ObjectShareAccess
public "isDevelopmentEnvironment"(): boolean
public "getGameDirectory"(): $Path
public "isClient"(): boolean
public "getConfigDirectory"(): $Path
public "getModList"(): $Map<(string), ($ModContainer)>
public "getModLoader"(): $ModLoader
public "isServer"(): boolean
public "getModsDirectory"(): $Path
public "isModPresentServerside"(arg0: string): boolean
public "isFabric"(): boolean
/**
 * 
 * @deprecated
 */
public "getModName"(arg0: string): $Optional<(string)>
/**
 * 
 * @deprecated
 */
public "getEnvironmentType"(): $DistType
public "isForge"(): boolean
public "isQuilt"(): boolean
/**
 * 
 * @deprecated
 */
public "isEnvironmentType"(arg0: $DistType$Type): boolean
/**
 * 
 * @deprecated
 */
public "findModResource"(arg0: string, ...arg1: (string)[]): $Optional<($Path)>
/**
 * 
 * @deprecated
 */
public "isModLoadedSafe"(arg0: string): boolean
public "getModContainer"(arg0: string): $Optional<($ModContainer)>
public "isModLoaded"(arg0: string): boolean
get "objectShareAccess"(): $ObjectShareAccess
get "developmentEnvironment"(): boolean
get "gameDirectory"(): $Path
get "client"(): boolean
get "configDirectory"(): $Path
get "modList"(): $Map<(string), ($ModContainer)>
get "modLoader"(): $ModLoader
get "server"(): boolean
get "modsDirectory"(): $Path
get "fabric"(): boolean
get "environmentType"(): $DistType
get "forge"(): boolean
get "quilt"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEnvironment$Type = ($ForgeEnvironment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEnvironment_ = $ForgeEnvironment$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/$DynamicModifyBakingResultContextImpl" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$DynamicModifyBakingResultContext, $DynamicModifyBakingResultContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicModifyBakingResultContext"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DynamicModifyBakingResultContextImpl extends $Record implements $DynamicModifyBakingResultContext {

constructor(models: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, modelBakery: $ModelBakery$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "models"(): $Map<($ResourceLocation), ($BakedModel)>
public "modelBakery"(): $ModelBakery
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicModifyBakingResultContextImpl$Type = ($DynamicModifyBakingResultContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicModifyBakingResultContextImpl_ = $DynamicModifyBakingResultContextImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingKnockBackCallback" {
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$DefaultedDouble, $DefaultedDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedDouble"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingKnockBackCallback {

 "onLivingKnockBack"(arg0: $LivingEntity$Type, arg1: $DefaultedDouble$Type, arg2: $DefaultedDouble$Type, arg3: $DefaultedDouble$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $DefaultedDouble$Type, arg2: $DefaultedDouble$Type, arg3: $DefaultedDouble$Type): $EventResult
}

export namespace $LivingKnockBackCallback {
const EVENT: $EventInvoker<($LivingKnockBackCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingKnockBackCallback$Type = ($LivingKnockBackCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingKnockBackCallback_ = $LivingKnockBackCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/container/v1/$ContainerSerializationHelper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $ContainerSerializationHelper {

constructor()

public static "loadAllItems"(arg0: $CompoundTag$Type, arg1: $Container$Type): void
public static "saveAllItems"(arg0: $CompoundTag$Type, arg1: $Container$Type): $CompoundTag
public static "saveAllItems"(arg0: $CompoundTag$Type, arg1: $Container$Type, arg2: boolean): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerSerializationHelper$Type = ($ContainerSerializationHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerSerializationHelper_ = $ContainerSerializationHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents$After" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $RenderGuiElementEvents$After {

 "onAfterRenderGuiElement"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void

(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): void
}

export namespace $RenderGuiElementEvents$After {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderGuiElementEvents$After$Type = ($RenderGuiElementEvents$After);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderGuiElementEvents$After_ = $RenderGuiElementEvents$After$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableBoolean" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MutableBoolean, $MutableBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean"

export class $ValueMutableBoolean implements $MutableBoolean {

constructor(arg0: boolean)

public "accept"(arg0: boolean): void
public "getAsBoolean"(): boolean
public static "fromValue"(arg0: boolean): $MutableBoolean
public static "fromEvent"(arg0: $Consumer$Type<(boolean)>, arg1: $Supplier$Type<(boolean)>): $MutableBoolean
public "mapBoolean"(arg0: $UnaryOperator$Type<(boolean)>): void
get "asBoolean"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueMutableBoolean$Type = ($ValueMutableBoolean);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueMutableBoolean_ = $ValueMutableBoolean$Type;
}}
declare module "packages/fuzs/puzzleslib/api/chat/v1/$ComponentHelper" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export class $ComponentHelper {

constructor()

public static "toComponent"(arg0: $FormattedText$Type): $Component
public static "toComponent"(arg0: $FormattedCharSequence$Type): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentHelper$Type = ($ComponentHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentHelper_ = $ComponentHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseItem" {
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

/**
 * 
 * @deprecated
 */
export interface $PlayerInteractEvents$UseItem {

 "onUseItem"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type): $EventResultHolder<($InteractionResultHolder<($ItemStack)>)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type): $EventResultHolder<($InteractionResultHolder<($ItemStack)>)>
}

export namespace $PlayerInteractEvents$UseItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$UseItem$Type = ($PlayerInteractEvents$UseItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$UseItem_ = $PlayerInteractEvents$UseItem$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/data/$ForgePlayerCapabilityKey" {
import {$ForgeCapabilityKey$CapabilityTokenFactory, $ForgeCapabilityKey$CapabilityTokenFactory$Type} from "packages/fuzs/puzzleslib/impl/capability/data/$ForgeCapabilityKey$CapabilityTokenFactory"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerRespawnCopyStrategy, $PlayerRespawnCopyStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerRespawnCopyStrategy"
import {$ForgeCapabilityKey, $ForgeCapabilityKey$Type} from "packages/fuzs/puzzleslib/impl/capability/data/$ForgeCapabilityKey"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$PlayerCapabilityKey, $PlayerCapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerCapabilityKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SyncStrategy, $SyncStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncStrategy"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgePlayerCapabilityKey<C extends $CapabilityComponent> extends $ForgeCapabilityKey<(C)> implements $PlayerCapabilityKey<(C)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Class$Type<(C)>, arg2: $ForgeCapabilityKey$CapabilityTokenFactory$Type<(C)>)

public "setRespawnStrategy"(arg0: $PlayerRespawnCopyStrategy$Type): $ForgePlayerCapabilityKey<(C)>
public "syncToRemote"(arg0: $ServerPlayer$Type): void
public "setSyncStrategy"(arg0: $SyncStrategy$Type): $ForgePlayerCapabilityKey<(C)>
public static "syncCapabilityToRemote"<C extends $CapabilityComponent>(arg0: $Entity$Type, arg1: $ServerPlayer$Type, arg2: $SyncStrategy$Type, arg3: C, arg4: $ResourceLocation$Type, arg5: boolean): void
set "respawnStrategy"(value: $PlayerRespawnCopyStrategy$Type)
set "syncStrategy"(value: $SyncStrategy$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePlayerCapabilityKey$Type<C> = ($ForgePlayerCapabilityKey<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePlayerCapabilityKey_<C> = $ForgePlayerCapabilityKey$Type<(C)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ContainerScreenEvents$Background" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $ContainerScreenEvents$Background {

 "onDrawBackground"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void

(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
}

export namespace $ContainerScreenEvents$Background {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerScreenEvents$Background$Type = ($ContainerScreenEvents$Background);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerScreenEvents$Background_ = $ContainerScreenEvents$Background$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$ItemTouchCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export interface $ItemTouchCallback {

 "onItemTouch"(arg0: $Player$Type, arg1: $ItemEntity$Type): $EventResult

(arg0: $Player$Type, arg1: $ItemEntity$Type): $EventResult
}

export namespace $ItemTouchCallback {
const EVENT: $EventInvoker<($ItemTouchCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTouchCallback$Type = ($ItemTouchCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTouchCallback_ = $ItemTouchCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$BaseModConstructor" {
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BaseModConstructor {

 "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
 "getPairingIdentifier"(): $ResourceLocation
}

export namespace $BaseModConstructor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseModConstructor$Type = ($BaseModConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseModConstructor_ = $BaseModConstructor$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/init/$ModelLayerFactoryImpl" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$ModelLayerFactory, $ModelLayerFactory$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$ModelLayerFactory"

export class $ModelLayerFactoryImpl extends $Record implements $ModelLayerFactory {

constructor(namespace: string)

public "registerOuterArmor"(arg0: string): $ModelLayerLocation
public "registerInnerArmor"(arg0: string): $ModelLayerLocation
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "register"(arg0: string, arg1: string): $ModelLayerLocation
public "register"(arg0: string): $ModelLayerLocation
public "namespace"(): string
public static "from"(arg0: string): $ModelLayerFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelLayerFactoryImpl$Type = ($ModelLayerFactoryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelLayerFactoryImpl_ = $ModelLayerFactoryImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/$ForwardingClientItemExtensions" {
import {$BlockEntityWithoutLevelRenderer, $BlockEntityWithoutLevelRenderer$Type} from "packages/net/minecraft/client/renderer/$BlockEntityWithoutLevelRenderer"
import {$HumanoidArm, $HumanoidArm$Type} from "packages/net/minecraft/world/entity/$HumanoidArm"
import {$HumanoidModel, $HumanoidModel$Type} from "packages/net/minecraft/client/model/$HumanoidModel"
import {$HumanoidModel$ArmPose, $HumanoidModel$ArmPose$Type} from "packages/net/minecraft/client/model/$HumanoidModel$ArmPose"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$IClientItemExtensions$FontContext, $IClientItemExtensions$FontContext$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions$FontContext"
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Model, $Model$Type} from "packages/net/minecraft/client/model/$Model"

export class $ForwardingClientItemExtensions implements $IClientItemExtensions {

constructor(arg0: $IClientItemExtensions$Type)

public "renderHelmetOverlay"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: integer, arg3: integer, arg4: float): void
public "getFont"(arg0: $ItemStack$Type, arg1: $IClientItemExtensions$FontContext$Type): $Font
public "getCustomRenderer"(): $BlockEntityWithoutLevelRenderer
public "getArmPose"(arg0: $LivingEntity$Type, arg1: $InteractionHand$Type, arg2: $ItemStack$Type): $HumanoidModel$ArmPose
public "getHumanoidArmorModel"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: $EquipmentSlot$Type, arg3: $HumanoidModel$Type<(any)>): $HumanoidModel<(any)>
public "applyForgeHandTransform"(arg0: $PoseStack$Type, arg1: $LocalPlayer$Type, arg2: $HumanoidArm$Type, arg3: $ItemStack$Type, arg4: float, arg5: float, arg6: float): boolean
public "getGenericArmorModel"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type, arg2: $EquipmentSlot$Type, arg3: $HumanoidModel$Type<(any)>): $Model
public static "of"(arg0: $ItemStack$Type): $IClientItemExtensions
public static "of"(arg0: $Item$Type): $IClientItemExtensions
get "customRenderer"(): $BlockEntityWithoutLevelRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForwardingClientItemExtensions$Type = ($ForwardingClientItemExtensions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForwardingClientItemExtensions_ = $ForwardingClientItemExtensions$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/core/$EventPhase" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $EventPhase {

 "parent"(): $EventPhase
 "identifier"(): $ResourceLocation
 "applyOrdering"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($ResourceLocation$Type)>): void
}

export namespace $EventPhase {
const DEFAULT: $EventPhase
const BEFORE: $EventPhase
const AFTER: $EventPhase
const FIRST: $EventPhase
const LAST: $EventPhase
function early(arg0: $EventPhase$Type): $EventPhase
function late(arg0: $EventPhase$Type): $EventPhase
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventPhase$Type = ($EventPhase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventPhase_ = $EventPhase$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseEntity" {
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerInteractEvents$UseEntity {

 "onUseEntity"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type): $EventResultHolder<($InteractionResult)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type): $EventResultHolder<($InteractionResult)>
}

export namespace $PlayerInteractEvents$UseEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$UseEntity$Type = ($PlayerInteractEvents$UseEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$UseEntity_ = $PlayerInteractEvents$UseEntity$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerInteractEvents$UseEntityAt" {
import {$EventResultHolder, $EventResultHolder$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerInteractEvents$UseEntityAt {

 "onUseEntityAt"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type, arg4: $Vec3$Type): $EventResultHolder<($InteractionResult)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $Entity$Type, arg4: $Vec3$Type): $EventResultHolder<($InteractionResult)>
}

export namespace $PlayerInteractEvents$UseEntityAt {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerInteractEvents$UseEntityAt$Type = ($PlayerInteractEvents$UseEntityAt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerInteractEvents$UseEntityAt_ = $PlayerInteractEvents$UseEntityAt$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents" {
import {$LivingEvents$Visibility, $LivingEvents$Visibility$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Visibility"
import {$LivingEvents$Breathe, $LivingEvents$Breathe$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Breathe"
import {$LivingEvents$Drown, $LivingEvents$Drown$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Drown"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$LivingEvents$Jump, $LivingEvents$Jump$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Jump"
import {$LivingEvents$Tick, $LivingEvents$Tick$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Tick"

export class $LivingEvents {
static readonly "TICK": $EventInvoker<($LivingEvents$Tick)>
static readonly "JUMP": $EventInvoker<($LivingEvents$Jump)>
static readonly "VISIBILITY": $EventInvoker<($LivingEvents$Visibility)>
static readonly "BREATHE": $EventInvoker<($LivingEvents$Breathe)>
static readonly "DROWN": $EventInvoker<($LivingEvents$Drown)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEvents$Type = ($LivingEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEvents_ = $LivingEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$EntityRidingEvents$Start" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityRidingEvents$Start {

 "onStartRiding"(arg0: $Level$Type, arg1: $Entity$Type, arg2: $Entity$Type): $EventResult

(arg0: $Level$Type, arg1: $Entity$Type, arg2: $Entity$Type): $EventResult
}

export namespace $EntityRidingEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRidingEvents$Start$Type = ($EntityRidingEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRidingEvents$Start_ = $EntityRidingEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemDecorationContext" {
import {$DynamicItemDecorator, $DynamicItemDecorator$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicItemDecorator"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export interface $ItemDecorationContext {

 "registerItemDecorator"(arg0: $DynamicItemDecorator$Type, ...arg1: ($ItemLike$Type)[]): void

(arg0: $DynamicItemDecorator$Type, ...arg1: ($ItemLike$Type)[]): void
}

export namespace $ItemDecorationContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemDecorationContext$Type = ($ItemDecorationContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemDecorationContext_ = $ItemDecorationContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents" {
import {$BlockEvents$FarmlandTrample, $BlockEvents$FarmlandTrample$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents$FarmlandTrample"
import {$BlockEvents$Break, $BlockEvents$Break$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents$Break"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$BlockEvents$DropExperience, $BlockEvents$DropExperience$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents$DropExperience"

export class $BlockEvents {
static readonly "BREAK": $EventInvoker<($BlockEvents$Break)>
static readonly "DROP_EXPERIENCE": $EventInvoker<($BlockEvents$DropExperience)>
static readonly "FARMLAND_TRAMPLE": $EventInvoker<($BlockEvents$FarmlandTrample)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvents$Type = ($BlockEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvents_ = $BlockEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents$Break" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BlockEvents$Break {

 "onBreakBlock"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type, arg4: $ItemStack$Type): $EventResult

(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type, arg4: $ItemStack$Type): $EventResult
}

export namespace $BlockEvents$Break {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvents$Break$Type = ($BlockEvents$Break);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvents$Break_ = $BlockEvents$Break$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$Respawn" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvents$Respawn {

 "onRespawn"(arg0: $ServerPlayer$Type, arg1: boolean): void

(arg0: $ServerPlayer$Type, arg1: boolean): void
}

export namespace $PlayerEvents$Respawn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$Respawn$Type = ($PlayerEvents$Respawn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$Respawn_ = $PlayerEvents$Respawn$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientTickEvents$End" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientTickEvents$End {

 "onEndClientTick"(arg0: $Minecraft$Type): void

(arg0: $Minecraft$Type): void
}

export namespace $ClientTickEvents$End {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickEvents$End$Type = ($ClientTickEvents$End);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickEvents$End_ = $ClientTickEvents$End$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/screen/$ForgeKeyMappingActivationHelper" {
import {$KeyMappingActivationHelper$KeyActivationContext, $KeyMappingActivationHelper$KeyActivationContext$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper$KeyActivationContext"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyMappingActivationHelper, $KeyMappingActivationHelper$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$IKeyConflictContext, $IKeyConflictContext$Type} from "packages/net/minecraftforge/client/settings/$IKeyConflictContext"

export class $ForgeKeyMappingActivationHelper implements $KeyMappingActivationHelper {
static readonly "KEY_CONTEXTS": $BiMap<($KeyMappingActivationHelper$KeyActivationContext), ($IKeyConflictContext)>

constructor()

public "getKeyActivationContext"(arg0: $KeyMapping$Type): $KeyMappingActivationHelper$KeyActivationContext
public "hasConflictWith"(arg0: $KeyMapping$Type, arg1: $KeyMapping$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeKeyMappingActivationHelper$Type = ($ForgeKeyMappingActivationHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeKeyMappingActivationHelper_ = $ForgeKeyMappingActivationHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedDouble" {
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$MutableDouble, $MutableDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble"

export interface $DefaultedDouble extends $MutableDouble {

 "getAsDefaultDouble"(): double
 "mapDefaultDouble"(arg0: $DoubleUnaryOperator$Type): void
 "applyDefaultDouble"(): void
 "getAsOptionalDouble"(): $OptionalDouble
 "mapDouble"(arg0: $DoubleUnaryOperator$Type): void
 "accept"(arg0: double): void
 "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
 "getAsDouble"(): double
}

export namespace $DefaultedDouble {
function fromValue(arg0: double): $DefaultedDouble
function fromEvent(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type, arg2: $DoubleSupplier$Type): $DefaultedDouble
function fromEvent(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type): $MutableDouble
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedDouble$Type = ($DefaultedDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedDouble_ = $DefaultedDouble$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/network/serialization/$RecordSerializer" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MessageSerializer, $MessageSerializer$Type} from "packages/fuzs/puzzleslib/api/network/v3/serialization/$MessageSerializer"

export class $RecordSerializer<T extends $Record> implements $MessageSerializer<(T)> {


public "write"(arg0: $FriendlyByteBuf$Type, arg1: T): void
public "read"(arg0: $FriendlyByteBuf$Type): T
public "getRecordType"(): $Class<(T)>
public static "createRecordSerializer"<T extends $Record>(arg0: $Class$Type<(T)>): $MessageSerializer<(T)>
get "recordType"(): $Class<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordSerializer$Type<T> = ($RecordSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordSerializer_<T> = $RecordSerializer$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$BeforeRender" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ScreenEvents$BeforeRender<T extends $Screen> {

 "onBeforeRender"(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void

(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
}

export namespace $ScreenEvents$BeforeRender {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$BeforeRender$Type<T> = ($ScreenEvents$BeforeRender<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$BeforeRender_<T> = $ScreenEvents$BeforeRender$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/client/screen/$ForgeScreenHelper" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ScreenHelper, $ScreenHelper$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$ScreenHelper"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $ForgeScreenHelper implements $ScreenHelper {

constructor()

public "getMinecraft"(arg0: $Screen$Type): $Minecraft
public "getFont"(arg0: $Screen$Type): $Font
public "getLeftPos"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getImageWidth"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getTopPos"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getHoveredSlot"(arg0: $AbstractContainerScreen$Type<(any)>): $Slot
public "getImageHeight"(arg0: $AbstractContainerScreen$Type<(any)>): integer
public "getMouseX"(arg0: $Minecraft$Type): integer
public "getMouseX"(arg0: $Screen$Type): integer
public "getMouseY"(arg0: $Screen$Type): integer
public "getMouseY"(arg0: $Minecraft$Type): integer
public "findSlot"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: double, arg2: double): $Slot
public "isHovering"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $Slot$Type, arg2: double, arg3: double): boolean
public "isHovering"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: double, arg5: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeScreenHelper$Type = ($ForgeScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeScreenHelper_ = $ForgeScreenHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerRespawnCopyStrategy" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"

export class $PlayerRespawnCopyStrategy extends $Enum<($PlayerRespawnCopyStrategy)> {
static readonly "ALWAYS": $PlayerRespawnCopyStrategy
static readonly "KEEP_INVENTORY": $PlayerRespawnCopyStrategy
static readonly "RETURNING_FROM_END": $PlayerRespawnCopyStrategy
static readonly "NEVER": $PlayerRespawnCopyStrategy


public static "values"(): ($PlayerRespawnCopyStrategy)[]
public static "valueOf"(arg0: string): $PlayerRespawnCopyStrategy
public "copy"(arg0: $CapabilityComponent$Type, arg1: $CapabilityComponent$Type, arg2: boolean, arg3: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRespawnCopyStrategy$Type = (("always") | ("never") | ("returning_from_end") | ("keep_inventory")) | ($PlayerRespawnCopyStrategy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRespawnCopyStrategy_ = $PlayerRespawnCopyStrategy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseRelease" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ScreenMouseEvents$BeforeMouseRelease<T extends $Screen> {

 "onBeforeMouseRelease"(arg0: T, arg1: double, arg2: double, arg3: integer): $EventResult

(arg0: T, arg1: double, arg2: double, arg3: integer): $EventResult
}

export namespace $ScreenMouseEvents$BeforeMouseRelease {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$BeforeMouseRelease$Type<T> = ($ScreenMouseEvents$BeforeMouseRelease<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$BeforeMouseRelease_<T> = $ScreenMouseEvents$BeforeMouseRelease$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/core/$ForgeObjectShareAccess" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ObjectShareAccess, $ObjectShareAccess$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ObjectShareAccess"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeObjectShareAccess implements $ObjectShareAccess {
static readonly "INSTANCE": $ObjectShareAccess


public "remove"(arg0: string): any
public "get"(arg0: string): any
public "put"(arg0: string, arg1: any): any
public "putIfAbsent"(arg0: string, arg1: any): any
public "remove"(arg0: $ResourceLocation$Type): any
public "get"(arg0: $ResourceLocation$Type): any
public "put"(arg0: $ResourceLocation$Type, arg1: any): any
public "putIfAbsent"(arg0: $ResourceLocation$Type, arg1: any): any
public "getOptional"<T>(arg0: string): $Optional<(T)>
public "getOptional"<T>(arg0: $ResourceLocation$Type): $Optional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeObjectShareAccess$Type = ($ForgeObjectShareAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeObjectShareAccess_ = $ForgeObjectShareAccess$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicBuiltinItemRenderer" {
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export interface $DynamicBuiltinItemRenderer extends $ResourceManagerReloadListener {

 "renderByItem"(arg0: $ItemStack$Type, arg1: $ItemDisplayContext$Type, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
 "onResourceManagerReload"(arg0: $ResourceManager$Type): void
 "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
 "getName"(): string

(arg0: $ItemStack$Type, arg1: $ItemDisplayContext$Type, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
}

export namespace $DynamicBuiltinItemRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicBuiltinItemRenderer$Type = ($DynamicBuiltinItemRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicBuiltinItemRenderer_ = $DynamicBuiltinItemRenderer$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/serialization/$MessageSerializer" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export interface $MessageSerializer<T> {

 "write"(arg0: $FriendlyByteBuf$Type, arg1: T): void
 "read"(arg0: $FriendlyByteBuf$Type): T
}

export namespace $MessageSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageSerializer$Type<T> = ($MessageSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageSerializer_<T> = $MessageSerializer$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerTickEvents$Start" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export interface $PlayerTickEvents$Start {

 "onStartPlayerTick"(arg0: $Player$Type): void

(arg0: $Player$Type): void
}

export namespace $PlayerTickEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerTickEvents$Start$Type = ($PlayerTickEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerTickEvents$Start_ = $PlayerTickEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/config/$AnnotatedConfigBuilder" {
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$ConfigDataHolderImpl, $ConfigDataHolderImpl$Type} from "packages/fuzs/puzzleslib/impl/config/$ConfigDataHolderImpl"

export class $AnnotatedConfigBuilder {

constructor()

public static "serialize"<T extends $ConfigCore>(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ConfigDataHolderImpl$Type<(any)>, arg2: $Class$Type<(any)>, arg3: T): void
public static "serialize"<T extends $ConfigCore>(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ConfigDataHolderImpl$Type<(any)>, arg2: T): void
public static "serialize"<T extends $ConfigCore>(arg0: $ForgeConfigSpec$Builder$Type, arg1: $ConfigDataHolderImpl$Type<(any)>, arg2: $Class$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedConfigBuilder$Type = ($AnnotatedConfigBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedConfigBuilder_ = $AnnotatedConfigBuilder$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$LootTableLoadEvents$Modify" {
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LootPool, $LootPool$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool"
import {$LootDataManager, $LootDataManager$Type} from "packages/net/minecraft/world/level/storage/loot/$LootDataManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $LootTableLoadEvents$Modify {

 "onModifyLootTable"(arg0: $LootDataManager$Type, arg1: $ResourceLocation$Type, arg2: $Consumer$Type<($LootPool$Type)>, arg3: $IntPredicate$Type): void

(arg0: $LootDataManager$Type, arg1: $ResourceLocation$Type, arg2: $Consumer$Type<($LootPool$Type)>, arg3: $IntPredicate$Type): void
}

export namespace $LootTableLoadEvents$Modify {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableLoadEvents$Modify$Type = ($LootTableLoadEvents$Modify);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableLoadEvents$Modify_ = $LootTableLoadEvents$Modify$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$BlockEvents$DropExperience" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"

export interface $BlockEvents$DropExperience {

 "onDropExperience"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type, arg4: $ItemStack$Type, arg5: $MutableInt$Type): void

(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type, arg4: $ItemStack$Type, arg5: $MutableInt$Type): void
}

export namespace $BlockEvents$DropExperience {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvents$DropExperience$Type = ($BlockEvents$DropExperience);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvents$DropExperience_ = $BlockEvents$DropExperience$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerForgeV3" {
import {$NetworkHandlerRegistryImpl, $NetworkHandlerRegistryImpl$Type} from "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerRegistryImpl"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$NetworkHandlerV3$Builder, $NetworkHandlerV3$Builder$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $NetworkHandlerForgeV3 extends $NetworkHandlerRegistryImpl {
 "clientAcceptsVanillaOrMissing": boolean
 "serverAcceptsVanillaOrMissing": boolean

constructor(arg0: $ResourceLocation$Type)

public "build"(): void
public "registerClientbound$Internal"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Class$Type<(any)>): void
public "registerServerbound$Internal"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: $Class$Type<(any)>): void
public "toServerboundPacket"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): $Packet<($ServerGamePacketListener)>
public "toClientboundPacket"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): $Packet<($ClientGamePacketListener)>
public static "builder"(arg0: string): $NetworkHandlerV3$Builder
public static "builder"(arg0: string, arg1: string): $NetworkHandlerV3$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerForgeV3$Type = ($NetworkHandlerForgeV3);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerForgeV3_ = $NetworkHandlerForgeV3$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerForgeV2" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MessageDirection, $MessageDirection$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageDirection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$MessageV2, $MessageV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageV2"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$NetworkHandlerV2, $NetworkHandlerV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$NetworkHandlerV2"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $NetworkHandlerForgeV2 implements $NetworkHandlerV2 {
readonly "clientAcceptsVanillaOrMissing": boolean
readonly "serverAcceptsVanillaOrMissing": boolean

constructor(arg0: $ResourceLocation$Type, arg1: boolean, arg2: boolean)

public "register"<T extends $MessageV2<(T)>>(arg0: $Class$Type<(any)>, arg1: $Supplier$Type<(T)>, arg2: $MessageDirection$Type): void
public "toServerboundPacket"(arg0: $MessageV2$Type<(any)>): $Packet<($ServerGamePacketListener)>
public "toClientboundPacket"(arg0: $MessageV2$Type<(any)>): $Packet<($ClientGamePacketListener)>
public "registerClientbound"<T extends $MessageV2<(T)>>(arg0: $Class$Type<(T)>): $NetworkHandlerV2
public "registerServerbound"<T extends $MessageV2<(T)>>(arg0: $Class$Type<(T)>): $NetworkHandlerV2
public static "build"(arg0: string): $NetworkHandlerV2
public static "build"(arg0: string, arg1: string, arg2: boolean, arg3: boolean): $NetworkHandlerV2
public static "build"(arg0: string, arg1: boolean, arg2: boolean): $NetworkHandlerV2
public static "build"(arg0: string, arg1: string): $NetworkHandlerV2
public "sendToAllTracking"(arg0: $MessageV2$Type<(any)>, arg1: $Entity$Type): void
public "sendToAllExcept"(arg0: $MessageV2$Type<(any)>, arg1: $ServerPlayer$Type): void
public "sendToDimension"(arg0: $MessageV2$Type<(any)>, arg1: $Level$Type): void
public "sendToDimension"(arg0: $MessageV2$Type<(any)>, arg1: $ResourceKey$Type<($Level$Type)>): void
public "sendToAllNear"(arg0: $MessageV2$Type<(any)>, arg1: $BlockPos$Type, arg2: $Level$Type): void
public "sendToAllNear"(arg0: $MessageV2$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Level$Type): void
public "sendToAll"(arg0: $MessageV2$Type<(any)>): void
public "sendTo"(arg0: $MessageV2$Type<(any)>, arg1: $ServerPlayer$Type): void
public "sendToAllTrackingAndSelf"(arg0: $MessageV2$Type<(any)>, arg1: $Entity$Type): void
public "sendToAllNearExcept"(arg0: $MessageV2$Type<(any)>, arg1: $ServerPlayer$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Level$Type): void
public "sendToServer"(arg0: $MessageV2$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerForgeV2$Type = ($NetworkHandlerForgeV2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerForgeV2_ = $NetworkHandlerForgeV2$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/init/v1/$SkullRenderersFactory" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$SkullModelBase, $SkullModelBase$Type} from "packages/net/minecraft/client/model/$SkullModelBase"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$SkullBlock$Type, $SkullBlock$Type$Type} from "packages/net/minecraft/world/level/block/$SkullBlock$Type"

export interface $SkullRenderersFactory {

 "createSkullRenderers"(arg0: $EntityModelSet$Type, arg1: $BiConsumer$Type<($SkullBlock$Type$Type), ($SkullModelBase$Type)>): void

(arg0: $EntityModelSet$Type, arg1: $BiConsumer$Type<($SkullBlock$Type$Type), ($SkullModelBase$Type)>): void
}

export namespace $SkullRenderersFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkullRenderersFactory$Type = ($SkullRenderersFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkullRenderersFactory_ = $SkullRenderersFactory$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$BlockColorProvidersContextForgeImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$BlockColors, $BlockColors$Type} from "packages/net/minecraft/client/color/block/$BlockColors"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ColorProvidersContext, $ColorProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ColorProvidersContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockColorProvidersContextForgeImpl extends $Record implements $ColorProvidersContext<($Block), ($BlockColor)> {

constructor(consumer: $BiConsumer$Type<($BlockColor$Type), ($Block$Type)>, blockColors: $BlockColors$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getProvider"(arg0: $Block$Type): $BlockColor
public "consumer"(): $BiConsumer<($BlockColor), ($Block)>
public "registerColorProvider"(arg0: $BlockColor$Type, ...arg1: ($Block$Type)[]): void
public "blockColors"(): $BlockColors
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockColorProvidersContextForgeImpl$Type = ($BlockColorProvidersContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockColorProvidersContextForgeImpl_ = $BlockColorProvidersContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/$DistTypeExecutor" {
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$DistType, $DistType$Type} from "packages/fuzs/puzzleslib/api/core/v1/$DistType"

/**
 * 
 * @deprecated
 */
export class $DistTypeExecutor {

constructor()

public static "callWhenOn"<T>(arg0: $DistType$Type, arg1: $Supplier$Type<($Callable$Type<(T)>)>): T
public static "runWhenOn"(arg0: $DistType$Type, arg1: $Supplier$Type<($Runnable$Type)>): void
public static "getForDistType"<T>(arg0: $Supplier$Type<($Supplier$Type<(T)>)>, arg1: $Supplier$Type<($Supplier$Type<(T)>)>): T
public static "getWhenOn"<T>(arg0: $DistType$Type, arg1: $Supplier$Type<($Supplier$Type<(T)>)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DistTypeExecutor$Type = ($DistTypeExecutor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DistTypeExecutor_ = $DistTypeExecutor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder" {
import {$ConfigDataHolder, $ConfigDataHolder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigDataHolder"
import {$ConfigCore, $ConfigCore$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigCore"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigHolder$Builder, $ConfigHolder$Builder$Type} from "packages/fuzs/puzzleslib/api/config/v3/$ConfigHolder$Builder"

export interface $ConfigHolder {

 "get"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): T
 "getHolder"<T extends $ConfigCore>(arg0: $Class$Type<(T)>): $ConfigDataHolder<(T)>

(arg0: $Class$Type<(T)>): T
}

export namespace $ConfigHolder {
function builder(arg0: string): $ConfigHolder$Builder
function simpleName(arg0: string): string
function moveToDir(arg0: string, arg1: string): string
function defaultName(arg0: string, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHolder$Type = ($ConfigHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHolder_ = $ConfigHolder$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt" {
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export interface $MutableInt extends $IntConsumer, $IntSupplier {

 "mapInt"(arg0: $IntUnaryOperator$Type): void
 "accept"(arg0: integer): void
 "andThen"(arg0: $IntConsumer$Type): $IntConsumer
 "getAsInt"(): integer
}

export namespace $MutableInt {
function fromValue(arg0: integer): $MutableInt
function fromEvent(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type): $MutableInt
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableInt$Type = ($MutableInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableInt_ = $MutableInt$Type;
}}
declare module "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncStrategy" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"

export interface $SyncStrategy {

 "sendTo"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T, arg1: $ServerPlayer$Type): void

(arg0: T, arg1: $ServerPlayer$Type): void
}

export namespace $SyncStrategy {
const MANUAL: $SyncStrategy
const SELF: $SyncStrategy
const SELF_AND_TRACKING: $SyncStrategy
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncStrategy$Type = ($SyncStrategy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncStrategy_ = $SyncStrategy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/$LoadCompleteCallback" {
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $LoadCompleteCallback {

 "onLoadComplete"(): void

(): void
}

export namespace $LoadCompleteCallback {
const EVENT: $EventInvoker<($LoadCompleteCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoadCompleteCallback$Type = ($LoadCompleteCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoadCompleteCallback_ = $LoadCompleteCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v3/$MessageV3" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MessageV3<T> {

 "getHandler"(): T

(): T
}

export namespace $MessageV3 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageV3$Type<T> = ($MessageV3<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageV3_<T> = $MessageV3$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventMutableFloat" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EventMutableFloat implements $MutableFloat {

constructor(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>)

public "accept"(arg0: float): void
public "getAsFloat"(): float
public static "fromValue"(arg0: float): $MutableFloat
public "mapFloat"(arg0: $UnaryOperator$Type<(float)>): void
public static "fromEvent"(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>): $MutableFloat
get "asFloat"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventMutableFloat$Type = ($EventMutableFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventMutableFloat_ = $EventMutableFloat$Type;
}}
declare module "packages/fuzs/puzzleslib/api/network/v2/$MessageV2" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MessageV2$MessageHandler, $MessageV2$MessageHandler$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageV2$MessageHandler"

export interface $MessageV2<T extends $MessageV2<(T)>> {

 "write"(arg0: $FriendlyByteBuf$Type): void
 "read"(arg0: $FriendlyByteBuf$Type): void
 "makeHandler"(): $MessageV2$MessageHandler<(T)>
}

export namespace $MessageV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageV2$Type<T> = ($MessageV2<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageV2_<T> = $MessageV2$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$FuelBurnTimesContext" {
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export interface $FuelBurnTimesContext {

 "registerFuel"(arg0: integer, ...arg1: ($ItemLike$Type)[]): void

(arg0: integer, ...arg1: ($ItemLike$Type)[]): void
}

export namespace $FuelBurnTimesContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FuelBurnTimesContext$Type = ($FuelBurnTimesContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FuelBurnTimesContext_ = $FuelBurnTimesContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper$KeyActivationContext" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KeyMappingActivationHelper$KeyActivationContext extends $Enum<($KeyMappingActivationHelper$KeyActivationContext)> {
static readonly "UNIVERSAL": $KeyMappingActivationHelper$KeyActivationContext
static readonly "GAME": $KeyMappingActivationHelper$KeyActivationContext
static readonly "SCREEN": $KeyMappingActivationHelper$KeyActivationContext


public static "values"(): ($KeyMappingActivationHelper$KeyActivationContext)[]
public static "valueOf"(arg0: string): $KeyMappingActivationHelper$KeyActivationContext
public "hasConflictWith"(arg0: $KeyMappingActivationHelper$KeyActivationContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingActivationHelper$KeyActivationContext$Type = (("game") | ("screen") | ("universal")) | ($KeyMappingActivationHelper$KeyActivationContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingActivationHelper$KeyActivationContext_ = $KeyMappingActivationHelper$KeyActivationContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/data/v1/recipes/$CopyTagShapedRecipeBuilder" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ShapedRecipeBuilder, $ShapedRecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$ShapedRecipeBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Advancement$Builder, $Advancement$Builder$Type} from "packages/net/minecraft/advancements/$Advancement$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecipeCategory, $RecipeCategory$Type} from "packages/net/minecraft/data/recipes/$RecipeCategory"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $CopyTagShapedRecipeBuilder extends $ShapedRecipeBuilder {
readonly "result": $Item
readonly "count": integer
readonly "rows": $List<(string)>
readonly "key": $Map<(character), ($Ingredient)>
readonly "advancement": $Advancement$Builder
 "group": string

constructor(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: integer)

public "group"(arg0: string): $CopyTagShapedRecipeBuilder
public "pattern"(arg0: string): $CopyTagShapedRecipeBuilder
public "copyFrom"(arg0: $ItemLike$Type): $CopyTagShapedRecipeBuilder
public "copyFrom"(arg0: $Ingredient$Type): $CopyTagShapedRecipeBuilder
public "save"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "define"(arg0: character, arg1: $ItemLike$Type): $CopyTagShapedRecipeBuilder
public "define"(arg0: character, arg1: $Ingredient$Type): $CopyTagShapedRecipeBuilder
public "define"(arg0: character, arg1: $TagKey$Type<($Item$Type)>): $CopyTagShapedRecipeBuilder
public "showNotification"(arg0: boolean): $CopyTagShapedRecipeBuilder
public "unlockedBy"(arg0: string, arg1: $CriterionTriggerInstance$Type): $CopyTagShapedRecipeBuilder
public static "shaped"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type): $CopyTagShapedRecipeBuilder
public static "shaped"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: integer): $CopyTagShapedRecipeBuilder
public static "getDefaultRecipeId"(arg0: $ItemLike$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CopyTagShapedRecipeBuilder$Type = ($CopyTagShapedRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CopyTagShapedRecipeBuilder_ = $CopyTagShapedRecipeBuilder$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/data/v2/$AbstractLanguageProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$DataProviderContext, $DataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$DataProviderContext"

export class $AbstractLanguageProvider implements $DataProvider {

constructor(arg0: string, arg1: string, arg2: $PackOutput$Type)
constructor(arg0: string, arg1: $PackOutput$Type)
constructor(arg0: string, arg1: $DataProviderContext$Type)
constructor(arg0: $DataProviderContext$Type)

public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractLanguageProvider$Type = ($AbstractLanguageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractLanguageProvider_ = $AbstractLanguageProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLevelEvents$AfterEntities" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

export interface $RenderLevelEvents$AfterEntities {

 "onRenderLevelAfterEntities"(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void

(arg0: $LevelRenderer$Type, arg1: $Camera$Type, arg2: $GameRenderer$Type, arg3: float, arg4: $PoseStack$Type, arg5: $Matrix4f$Type, arg6: $Frustum$Type, arg7: $ClientLevel$Type): void
}

export namespace $RenderLevelEvents$AfterEntities {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEvents$AfterEntities$Type = ($RenderLevelEvents$AfterEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEvents$AfterEntities_ = $RenderLevelEvents$AfterEntities$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$BreakSpeed" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"

export interface $PlayerEvents$BreakSpeed {

 "onBreakSpeed"(arg0: $Player$Type, arg1: $BlockState$Type, arg2: $DefaultedFloat$Type): $EventResult

(arg0: $Player$Type, arg1: $BlockState$Type, arg2: $DefaultedFloat$Type): $EventResult
}

export namespace $PlayerEvents$BreakSpeed {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$BreakSpeed$Type = ($PlayerEvents$BreakSpeed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$BreakSpeed_ = $PlayerEvents$BreakSpeed$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/network/$NetworkHandlerRegistry" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$NetworkHandlerV3$Builder, $NetworkHandlerV3$Builder$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3$Builder"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$ServerboundMessage, $ServerboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ServerboundMessage"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ServerGamePacketListener, $ServerGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ServerGamePacketListener"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$NetworkHandlerV3, $NetworkHandlerV3$Type} from "packages/fuzs/puzzleslib/api/network/v3/$NetworkHandlerV3"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $NetworkHandlerRegistry extends $NetworkHandlerV3 {

/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockEntity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $LevelChunk$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: $ChunkPos$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T, arg2: boolean): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: $ServerLevel$Type, arg4: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Vec3i$Type, arg1: $ServerLevel$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $ServerLevel$Type, arg6: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerLevel$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $MinecraftServer$Type, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Collection$Type<($ServerPlayer$Type)>, arg1: $ServerPlayer$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendTo"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "toServerboundPacket"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): $Packet<($ServerGamePacketListener)>
/**
 * 
 * @deprecated
 */
 "toClientboundPacket"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): $Packet<($ClientGamePacketListener)>
/**
 * 
 * @deprecated
 */
 "sendToServer"<T extends ($Record) & ($ServerboundMessage<(T)>)>(arg0: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTracking"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ResourceKey$Type<($Level$Type)>, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToDimension"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Level$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $BlockPos$Type, arg1: $Level$Type, arg2: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNear"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Level$Type, arg5: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: T): void
/**
 * 
 * @deprecated
 */
 "sendToAll"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllTrackingAndSelf"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $Entity$Type, arg1: T): void
/**
 * 
 * @deprecated
 */
 "sendToAllNearExcept"<T extends ($Record) & ($ClientboundMessage<(T)>)>(arg0: $ServerPlayer$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Level$Type, arg6: T): void
}

export namespace $NetworkHandlerRegistry {
function builder(arg0: string): $NetworkHandlerV3$Builder
function builder(arg0: string, arg1: string): $NetworkHandlerV3$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandlerRegistry$Type = ($NetworkHandlerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandlerRegistry_ = $NetworkHandlerRegistry$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InventoryMobEffectsCallback" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$MutableBoolean, $MutableBoolean$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableBoolean"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"

export interface $InventoryMobEffectsCallback {

 "onInventoryMobEffects"(arg0: $Screen$Type, arg1: integer, arg2: $MutableBoolean$Type, arg3: $MutableInt$Type): $EventResult

(arg0: $Screen$Type, arg1: integer, arg2: $MutableBoolean$Type, arg3: $MutableInt$Type): $EventResult
}

export namespace $InventoryMobEffectsCallback {
const EVENT: $EventInvoker<($InventoryMobEffectsCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryMobEffectsCallback$Type = ($InventoryMobEffectsCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryMobEffectsCallback_ = $InventoryMobEffectsCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$ParticleProvidersContext" {
import {$ParticleProvider, $ParticleProvider$Type} from "packages/net/minecraft/client/particle/$ParticleProvider"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$ParticleEngine$SpriteParticleRegistration, $ParticleEngine$SpriteParticleRegistration$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$SpriteParticleRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"
import {$ParticleProvider$Sprite, $ParticleProvider$Sprite$Type} from "packages/net/minecraft/client/particle/$ParticleProvider$Sprite"

export interface $ParticleProvidersContext {

 "registerParticleProvider"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
 "registerParticleProvider"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleProvider$Sprite$Type<(T)>): void
 "registerParticleProvider"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleProvider$Type<(T)>): void
 "registerClientParticleProvider"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleProvider$Sprite$Type<(T)>): void
 "registerClientParticleProvider"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
 "registerClientParticleProvider"<T extends $ParticleOptions>(arg0: $ResourceLocation$Type, arg1: $ParticleProvider$Type<(T)>): void
/**
 * 
 * @deprecated
 */
 "registerParticleFactory"<T extends $ParticleOptions>(arg0: $ParticleType$Type<(T)>, arg1: $ParticleEngine$SpriteParticleRegistration$Type<(T)>): void
}

export namespace $ParticleProvidersContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleProvidersContext$Type = ($ParticleProvidersContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleProvidersContext_ = $ParticleProvidersContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/$ForgeCapabilityController" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CapabilityController, $CapabilityController$Type} from "packages/fuzs/puzzleslib/api/capability/v2/$CapabilityController"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerRespawnCopyStrategy, $PlayerRespawnCopyStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerRespawnCopyStrategy"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$CapabilityComponent, $CapabilityComponent$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityComponent"
import {$PlayerCapabilityKey, $PlayerCapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$PlayerCapabilityKey"
import {$CapabilityKey, $CapabilityKey$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$CapabilityKey"
import {$SyncStrategy, $SyncStrategy$Type} from "packages/fuzs/puzzleslib/api/capability/v2/data/$SyncStrategy"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeCapabilityController implements $CapabilityController {

constructor(arg0: string)

public "registerBlockEntityCapability"<T extends $BlockEntity, C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<(T), (C)>, arg3: $Class$Type<(T)>): $CapabilityKey<(C)>
public "registerEntityCapability"<T extends $Entity, C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<(T), (C)>, arg3: $Class$Type<(T)>): $CapabilityKey<(C)>
public "registerLevelChunkCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($ChunkAccess$Type), (C)>): $CapabilityKey<(C)>
public "registerPlayerCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($Player$Type), (C)>, arg3: $PlayerRespawnCopyStrategy$Type, arg4: $SyncStrategy$Type): $PlayerCapabilityKey<(C)>
public "registerPlayerCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($Player$Type), (C)>, arg3: $PlayerRespawnCopyStrategy$Type): $PlayerCapabilityKey<(C)>
public "registerLevelCapability"<C extends $CapabilityComponent>(arg0: string, arg1: $Class$Type<(C)>, arg2: $Function$Type<($Level$Type), (C)>): $CapabilityKey<(C)>
public "onAttachCapabilities"<T>(arg0: $AttachCapabilitiesEvent$Type<(any)>): void
public static "from"(arg0: string): $CapabilityController
public static "retrieve"(arg0: $ResourceLocation$Type): $CapabilityKey<(any)>
public static "submit"<T extends $CapabilityComponent>(arg0: $CapabilityKey$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCapabilityController$Type = ($ForgeCapabilityController);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCapabilityController_ = $ForgeCapabilityController$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientPlayerEvents$Copy" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$MultiPlayerGameMode, $MultiPlayerGameMode$Type} from "packages/net/minecraft/client/multiplayer/$MultiPlayerGameMode"

export interface $ClientPlayerEvents$Copy {

 "onCopy"(arg0: $LocalPlayer$Type, arg1: $LocalPlayer$Type, arg2: $MultiPlayerGameMode$Type, arg3: $Connection$Type): void

(arg0: $LocalPlayer$Type, arg1: $LocalPlayer$Type, arg2: $MultiPlayerGameMode$Type, arg3: $Connection$Type): void
}

export namespace $ClientPlayerEvents$Copy {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvents$Copy$Type = ($ClientPlayerEvents$Copy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvents$Copy_ = $ClientPlayerEvents$Copy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/$EntityRidingEvents$Stop" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityRidingEvents$Stop {

 "onStopRiding"(arg0: $Level$Type, arg1: $Entity$Type, arg2: $Entity$Type): $EventResult

(arg0: $Level$Type, arg1: $Entity$Type, arg2: $Entity$Type): $EventResult
}

export namespace $EntityRidingEvents$Stop {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRidingEvents$Stop$Type = ($EntityRidingEvents$Stop);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRidingEvents$Stop_ = $EntityRidingEvents$Stop$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$PlayLevelSoundEvents$AtPosition" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$SoundSource, $SoundSource$Type} from "packages/net/minecraft/sounds/$SoundSource"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"

export interface $PlayLevelSoundEvents$AtPosition {

 "onPlaySoundAtPosition"(arg0: $Level$Type, arg1: $Vec3$Type, arg2: $MutableValue$Type<($Holder$Type<($SoundEvent$Type)>)>, arg3: $MutableValue$Type<($SoundSource$Type)>, arg4: $DefaultedFloat$Type, arg5: $DefaultedFloat$Type): $EventResult

(arg0: $Level$Type, arg1: $Vec3$Type, arg2: $MutableValue$Type<($Holder$Type<($SoundEvent$Type)>)>, arg3: $MutableValue$Type<($SoundSource$Type)>, arg4: $DefaultedFloat$Type, arg5: $DefaultedFloat$Type): $EventResult
}

export namespace $PlayLevelSoundEvents$AtPosition {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayLevelSoundEvents$AtPosition$Type = ($PlayLevelSoundEvents$AtPosition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayLevelSoundEvents$AtPosition_ = $PlayLevelSoundEvents$AtPosition$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/capability/$ClientboundSyncCapabilityMessage" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ClientboundMessage, $ClientboundMessage$Type} from "packages/fuzs/puzzleslib/api/network/v3/$ClientboundMessage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ClientboundSyncCapabilityMessage extends $Record implements $ClientboundMessage<($ClientboundSyncCapabilityMessage)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Entity$Type, arg2: $CompoundTag$Type)
constructor(id: $ResourceLocation$Type, holderId: integer, tag: $CompoundTag$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "id"(): $ResourceLocation
public "tag"(): $CompoundTag
public "holderId"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundSyncCapabilityMessage$Type = ($ClientboundSyncCapabilityMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundSyncCapabilityMessage_ = $ClientboundSyncCapabilityMessage$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedInt" {
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$MutableInt, $MutableInt$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableInt"
import {$IntUnaryOperator, $IntUnaryOperator$Type} from "packages/java/util/function/$IntUnaryOperator"

export interface $DefaultedInt extends $MutableInt {

 "applyDefaultInt"(): void
 "getAsOptionalInt"(): $OptionalInt
 "getAsDefaultInt"(): integer
 "mapDefaultInt"(arg0: $IntUnaryOperator$Type): void
 "mapInt"(arg0: $IntUnaryOperator$Type): void
 "accept"(arg0: integer): void
 "andThen"(arg0: $IntConsumer$Type): $IntConsumer
 "getAsInt"(): integer
}

export namespace $DefaultedInt {
function fromValue(arg0: integer): $DefaultedInt
function fromEvent(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type, arg2: $IntSupplier$Type): $DefaultedInt
function fromEvent(arg0: $IntConsumer$Type, arg1: $IntSupplier$Type): $MutableInt
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedInt$Type = ($DefaultedInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedInt_ = $DefaultedInt$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/$ClientAbstractions" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$SearchRegistry, $SearchRegistry$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ClientTooltipComponent, $ClientTooltipComponent$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipComponent"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ModelManager, $ModelManager$Type} from "packages/net/minecraft/client/resources/model/$ModelManager"

export interface $ClientAbstractions {

 "getPartialTick"(): float
/**
 * 
 * @deprecated
 */
 "getPartialTick"(arg0: $Minecraft$Type): float
 "registerRenderType"(arg0: $Fluid$Type, arg1: $RenderType$Type): void
 "registerRenderType"(arg0: $Block$Type, arg1: $RenderType$Type): void
 "getRenderType"(arg0: $Fluid$Type): $RenderType
 "getRenderType"(arg0: $Block$Type): $RenderType
 "getBakedModel"(arg0: $ResourceLocation$Type): $BakedModel
/**
 * 
 * @deprecated
 */
 "getBakedModel"(arg0: $ModelManager$Type, arg1: $ResourceLocation$Type): $BakedModel
/**
 * 
 * @deprecated
 */
 "getSearchRegistry"(arg0: $Minecraft$Type): $SearchRegistry
 "getSearchRegistry"(): $SearchRegistry
 "createImageComponent"(arg0: $TooltipComponent$Type): $ClientTooltipComponent
 "isKeyActiveAndMatches"(arg0: $KeyMapping$Type, arg1: integer, arg2: integer): boolean
}

export namespace $ClientAbstractions {
const INSTANCE: $ClientAbstractions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientAbstractions$Type = ($ClientAbstractions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientAbstractions_ = $ClientAbstractions$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/data/v2/$AbstractModelProvider$ItemOverride" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AbstractModelProvider$ItemOverride extends $Record {

constructor(model: $ResourceLocation$Type, predicates: $Map$Type<($ResourceLocation$Type), (float)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: float): $AbstractModelProvider$ItemOverride
public static "of"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: float, arg3: $ResourceLocation$Type, arg4: float, arg5: $ResourceLocation$Type, arg6: float): $AbstractModelProvider$ItemOverride
public static "of"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: float, arg3: $ResourceLocation$Type, arg4: float): $AbstractModelProvider$ItemOverride
public "predicates"(): $Map<($ResourceLocation), (float)>
public "model"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModelProvider$ItemOverride$Type = ($AbstractModelProvider$ItemOverride);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModelProvider$ItemOverride_ = $AbstractModelProvider$ItemOverride$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$GatherDebugTextEvents$Left" {
import {$List, $List$Type} from "packages/java/util/$List"

export interface $GatherDebugTextEvents$Left {

 "onGatherLeftDebugText"(arg0: $List$Type<(string)>): void

(arg0: $List$Type<(string)>): void
}

export namespace $GatherDebugTextEvents$Left {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherDebugTextEvents$Left$Type = ($GatherDebugTextEvents$Left);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherDebugTextEvents$Left_ = $GatherDebugTextEvents$Left$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableDouble" {
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/java/util/function/$DoubleConsumer"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$MutableDouble, $MutableDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble"

export class $ValueMutableDouble implements $MutableDouble {

constructor(arg0: double)

public "accept"(arg0: double): void
public "getAsDouble"(): double
public static "fromValue"(arg0: double): $MutableDouble
public static "fromEvent"(arg0: $DoubleConsumer$Type, arg1: $DoubleSupplier$Type): $MutableDouble
public "mapDouble"(arg0: $DoubleUnaryOperator$Type): void
public "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
get "asDouble"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueMutableDouble$Type = ($ValueMutableDouble);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueMutableDouble_ = $ValueMutableDouble$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$GameRenderEvents$After" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"

export interface $GameRenderEvents$After {

 "onAfterGameRender"(arg0: $Minecraft$Type, arg1: $GameRenderer$Type, arg2: float): void

(arg0: $Minecraft$Type, arg1: $GameRenderer$Type, arg2: float): void
}

export namespace $GameRenderEvents$After {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameRenderEvents$After$Type = ($GameRenderEvents$After);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameRenderEvents$After_ = $GameRenderEvents$After$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$BuiltinModelItemRendererContext" {
import {$DynamicBuiltinItemRenderer, $DynamicBuiltinItemRenderer$Type} from "packages/fuzs/puzzleslib/api/client/init/v1/$DynamicBuiltinItemRenderer"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export interface $BuiltinModelItemRendererContext {

 "registerItemRenderer"(arg0: $DynamicBuiltinItemRenderer$Type, ...arg1: ($ItemLike$Type)[]): void

(arg0: $DynamicBuiltinItemRenderer$Type, ...arg1: ($ItemLike$Type)[]): void
}

export namespace $BuiltinModelItemRendererContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltinModelItemRendererContext$Type = ($BuiltinModelItemRendererContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltinModelItemRendererContext_ = $BuiltinModelItemRendererContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$AfterRender" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ScreenEvents$AfterRender<T extends $Screen> {

 "onAfterRender"(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void

(arg0: T, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
}

export namespace $ScreenEvents$AfterRender {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$AfterRender$Type<T> = ($ScreenEvents$AfterRender<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$AfterRender_<T> = $ScreenEvents$AfterRender$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingDeathCallback" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $LivingDeathCallback {

 "onLivingDeath"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type): $EventResult
}

export namespace $LivingDeathCallback {
const EVENT: $EventInvoker<($LivingDeathCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingDeathCallback$Type = ($LivingDeathCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingDeathCallback_ = $LivingDeathCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/$ModMixinConfigPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ModMixinConfigPlugin implements $IMixinConfigPlugin {

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
export type $ModMixinConfigPlugin$Type = ($ModMixinConfigPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMixinConfigPlugin_ = $ModMixinConfigPlugin$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$MobEffectEvents$Apply" {
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $MobEffectEvents$Apply {

 "onMobEffectApply"(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type, arg2: $MobEffectInstance$Type, arg3: $Entity$Type): void

(arg0: $LivingEntity$Type, arg1: $MobEffectInstance$Type, arg2: $MobEffectInstance$Type, arg3: $Entity$Type): void
}

export namespace $MobEffectEvents$Apply {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobEffectEvents$Apply$Type = ($MobEffectEvents$Apply);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobEffectEvents$Apply_ = $MobEffectEvents$Apply$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerTickEvents$Start" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerTickEvents$Start {

 "onStartServerTick"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerTickEvents$Start {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerTickEvents$Start$Type = ($ServerTickEvents$Start);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerTickEvents$Start_ = $ServerTickEvents$Start$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents" {
import {$ServerLifecycleEvents$ServerStopping, $ServerLifecycleEvents$ServerStopping$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStopping"
import {$ServerLifecycleEvents$ServerStarting, $ServerLifecycleEvents$ServerStarting$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStarting"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ServerLifecycleEvents$ServerStarted, $ServerLifecycleEvents$ServerStarted$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStarted"
import {$ServerLifecycleEvents$ServerStopped, $ServerLifecycleEvents$ServerStopped$Type} from "packages/fuzs/puzzleslib/api/event/v1/server/$ServerLifecycleEvents$ServerStopped"

export class $ServerLifecycleEvents {
static readonly "STARTING": $EventInvoker<($ServerLifecycleEvents$ServerStarting)>
static readonly "STARTED": $EventInvoker<($ServerLifecycleEvents$ServerStarted)>
static readonly "STOPPING": $EventInvoker<($ServerLifecycleEvents$ServerStopping)>
static readonly "STOPPED": $EventInvoker<($ServerLifecycleEvents$ServerStopped)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLifecycleEvents$Type = ($ServerLifecycleEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLifecycleEvents_ = $ServerLifecycleEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$LivingEvents$Visibility" {
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$MutableDouble, $MutableDouble$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableDouble"

export interface $LivingEvents$Visibility {

 "onLivingVisibility"(arg0: $LivingEntity$Type, arg1: $Entity$Type, arg2: $MutableDouble$Type): void

(arg0: $LivingEntity$Type, arg1: $Entity$Type, arg2: $MutableDouble$Type): void
}

export namespace $LivingEvents$Visibility {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingEvents$Visibility$Type = ($LivingEvents$Visibility);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingEvents$Visibility_ = $LivingEvents$Visibility$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$BlockItemAccessor" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BlockItemAccessor {

 "puzzleslib$setBlock"(arg0: $Block$Type): void

(arg0: $Block$Type): void
}

export namespace $BlockItemAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockItemAccessor$Type = ($BlockItemAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockItemAccessor_ = $BlockItemAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/$ValueCallback" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"

export interface $ValueCallback {

 "accept"<S, V extends $ForgeConfigSpec$ConfigValue<(S)>>(arg0: V, arg1: $Consumer$Type<(S)>): V

(arg0: V, arg1: $Consumer$Type<(S)>): V
}

export namespace $ValueCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueCallback$Type = ($ValueCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueCallback_ = $ValueCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/living/$CheckMobDespawnCallback" {
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $CheckMobDespawnCallback {

 "onCheckMobDespawn"(arg0: $Mob$Type, arg1: $ServerLevel$Type): $EventResult

(arg0: $Mob$Type, arg1: $ServerLevel$Type): $EventResult
}

export namespace $CheckMobDespawnCallback {
const EVENT: $EventInvoker<($CheckMobDespawnCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckMobDespawnCallback$Type = ($CheckMobDespawnCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckMobDespawnCallback_ = $CheckMobDespawnCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$Copy" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvents$Copy {

 "onCopy"(arg0: $ServerPlayer$Type, arg1: $ServerPlayer$Type, arg2: boolean): void

(arg0: $ServerPlayer$Type, arg1: $ServerPlayer$Type, arg2: boolean): void
}

export namespace $PlayerEvents$Copy {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$Copy$Type = ($PlayerEvents$Copy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$Copy_ = $PlayerEvents$Copy$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $DefaultedValue<T> extends $MutableValue<(T)> {

 "getAsOptional"(): $Optional<(T)>
 "getAsDefault"(): T
 "mapDefault"(arg0: $UnaryOperator$Type<(T)>): void
 "applyDefault"(): void
 "map"(arg0: $UnaryOperator$Type<(T)>): void
 "accept"(arg0: T): void
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
 "get"(): T
}

export namespace $DefaultedValue {
function fromValue<T>(arg0: T): $DefaultedValue<(T)>
function fromEvent<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>, arg2: $Supplier$Type<(T)>): $DefaultedValue<(T)>
function fromEvent<T>(arg0: $Consumer$Type<(T)>, arg1: $Supplier$Type<(T)>): $MutableValue<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedValue$Type<T> = ($DefaultedValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedValue_<T> = $DefaultedValue$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/network/v2/$MessageV2$MessageHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MessageV2, $MessageV2$Type} from "packages/fuzs/puzzleslib/api/network/v2/$MessageV2"

export class $MessageV2$MessageHandler<T extends $MessageV2<(T)>> {

constructor()

public "handle"(arg0: T, arg1: $Player$Type, arg2: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageV2$MessageHandler$Type<T> = ($MessageV2$MessageHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageV2$MessageHandler_<T> = $MessageV2$MessageHandler$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$FireBlockForgeAccessor" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $FireBlockForgeAccessor {

 "puzzleslib$setFlammable"(arg0: $Block$Type, arg1: integer, arg2: integer): void

(arg0: $Block$Type, arg1: integer, arg2: integer): void
}

export namespace $FireBlockForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireBlockForgeAccessor$Type = ($FireBlockForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireBlockForgeAccessor_ = $FireBlockForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenTooltipEvents$Render" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ClientTooltipComponent, $ClientTooltipComponent$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipComponent"
import {$ClientTooltipPositioner, $ClientTooltipPositioner$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipPositioner"

export interface $ScreenTooltipEvents$Render {

 "onRenderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Font$Type, arg6: $List$Type<($ClientTooltipComponent$Type)>, arg7: $ClientTooltipPositioner$Type): $EventResult

(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Font$Type, arg6: $List$Type<($ClientTooltipComponent$Type)>, arg7: $ClientTooltipPositioner$Type): $EventResult
}

export namespace $ScreenTooltipEvents$Render {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenTooltipEvents$Render$Type = ($ScreenTooltipEvents$Render);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenTooltipEvents$Render_ = $ScreenTooltipEvents$Render$Type;
}}
declare module "packages/fuzs/puzzleslib/api/config/v3/json/$JsonConfigFileUtil" {
import {$FileReader, $FileReader$Type} from "packages/java/io/$FileReader"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $JsonConfigFileUtil {
static readonly "GSON": $Gson

constructor()

public static "getAllAndLoad"(arg0: string, arg1: $Consumer$Type<($File$Type)>, arg2: $Consumer$Type<($FileReader$Type)>, arg3: $Runnable$Type): void
public static "getAndLoad"(arg0: string, arg1: $Consumer$Type<($File$Type)>, arg2: $Consumer$Type<($FileReader$Type)>): void
public static "getAndLoad"(arg0: string, arg1: string, arg2: $Consumer$Type<($File$Type)>, arg3: $Consumer$Type<($FileReader$Type)>): void
public static "mkdirs"(arg0: string): boolean
public static "mkdirs"(arg0: $File$Type): boolean
public static "getConfigPath"(arg0: string): $File
public static "getSpecialConfigPath"(arg0: string, arg1: string): $File
public static "copyToFile"(arg0: $File$Type): boolean
public static "saveToFile"(arg0: $File$Type, arg1: $JsonElement$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonConfigFileUtil$Type = ($JsonConfigFileUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonConfigFileUtil_ = $JsonConfigFileUtil$Type;
}}
declare module "packages/fuzs/puzzleslib/api/block/v1/$TickingBlockEntity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TickingBlockEntity {

 "clientTick"(): void
 "serverTick"(): void
}

export namespace $TickingBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingBlockEntity$Type = ($TickingBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingBlockEntity_ = $TickingBlockEntity$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/core/$EventInvokerImpl$EventInvokerLike" {
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $EventInvokerImpl$EventInvokerLike<T> {

 "asEventInvoker"(arg0: any): $EventInvoker<(T)>

(arg0: any): $EventInvoker<(T)>
}

export namespace $EventInvokerImpl$EventInvokerLike {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventInvokerImpl$EventInvokerLike$Type<T> = ($EventInvokerImpl$EventInvokerLike<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventInvokerImpl$EventInvokerLike_<T> = $EventInvokerImpl$EventInvokerLike$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$EventDefaultedFloat" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$EventMutableFloat, $EventMutableFloat$Type} from "packages/fuzs/puzzleslib/impl/event/data/$EventMutableFloat"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DefaultedFloat, $DefaultedFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$DefaultedFloat"

export class $EventDefaultedFloat extends $EventMutableFloat implements $DefaultedFloat {

constructor(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>, arg2: $Supplier$Type<(float)>)

public "accept"(arg0: float): void
public "getAsOptionalFloat"(): $Optional<(float)>
public "getAsDefaultFloat"(): float
public static "fromValue"(arg0: float): $DefaultedFloat
public "applyDefaultFloat"(): void
public "mapDefaultFloat"(arg0: $UnaryOperator$Type<(float)>): void
public static "fromEvent"(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>, arg2: $Supplier$Type<(float)>): $DefaultedFloat
public static "fromEvent"(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>): $MutableFloat
get "asOptionalFloat"(): $Optional<(float)>
get "asDefaultFloat"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventDefaultedFloat$Type = ($EventDefaultedFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventDefaultedFloat_ = $EventDefaultedFloat$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/accessor/$LootTableForgeAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootPool, $LootPool$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool"

export interface $LootTableForgeAccessor {

 "puzzleslib$getPools"(): $List<($LootPool)>

(): $List<($LootPool)>
}

export namespace $LootTableForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableForgeAccessor$Type = ($LootTableForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableForgeAccessor_ = $LootTableForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/block/v1/$TickingEntityBlock" {
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$TickingBlockEntity, $TickingBlockEntity$Type} from "packages/fuzs/puzzleslib/api/block/v1/$TickingBlockEntity"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export interface $TickingEntityBlock extends $EntityBlock {

 "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
 "getBlockEntityType"<T extends ($BlockEntity) & ($TickingBlockEntity)>(): $BlockEntityType<(T)>
 "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
 "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
}

export namespace $TickingEntityBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingEntityBlock$Type = ($TickingEntityBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingEntityBlock_ = $TickingEntityBlock$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ComputeCameraAnglesCallback" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export interface $ComputeCameraAnglesCallback {

 "onComputeCameraAngles"(arg0: $GameRenderer$Type, arg1: $Camera$Type, arg2: float, arg3: $MutableFloat$Type, arg4: $MutableFloat$Type, arg5: $MutableFloat$Type): void

(arg0: $GameRenderer$Type, arg1: $Camera$Type, arg2: float, arg3: $MutableFloat$Type, arg4: $MutableFloat$Type, arg5: $MutableFloat$Type): void
}

export namespace $ComputeCameraAnglesCallback {
const EVENT: $EventInvoker<($ComputeCameraAnglesCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComputeCameraAnglesCallback$Type = ($ComputeCameraAnglesCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComputeCameraAnglesCallback_ = $ComputeCameraAnglesCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenMouseEvents$BeforeMouseClick" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $ScreenMouseEvents$BeforeMouseClick<T extends $Screen> {

 "onBeforeMouseClick"(arg0: T, arg1: double, arg2: double, arg3: integer): $EventResult

(arg0: T, arg1: double, arg2: double, arg3: integer): $EventResult
}

export namespace $ScreenMouseEvents$BeforeMouseClick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvents$BeforeMouseClick$Type<T> = ($ScreenMouseEvents$BeforeMouseClick<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvents$BeforeMouseClick_<T> = $ScreenMouseEvents$BeforeMouseClick$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/data/v2/client/$AbstractSpriteSourceProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ForgeDataProviderContext, $ForgeDataProviderContext$Type} from "packages/fuzs/puzzleslib/api/data/v2/core/$ForgeDataProviderContext"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$SpriteSourceProvider, $SpriteSourceProvider$Type} from "packages/net/minecraftforge/common/data/$SpriteSourceProvider"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $AbstractSpriteSourceProvider extends $SpriteSourceProvider {

constructor(arg0: $ForgeDataProviderContext$Type)
constructor(arg0: string, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSpriteSourceProvider$Type = ($AbstractSpriteSourceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSpriteSourceProvider_ = $AbstractSpriteSourceProvider$Type;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$ClimateSettingsContext" {
import {$Biome$TemperatureModifier, $Biome$TemperatureModifier$Type} from "packages/net/minecraft/world/level/biome/$Biome$TemperatureModifier"

export interface $ClimateSettingsContext {

 "setTemperatureModifier"(arg0: $Biome$TemperatureModifier$Type): void
 "getTemperature"(): float
 "setDownfall"(arg0: float): void
 "setTemperature"(arg0: float): void
 "hasPrecipitation"(): boolean
 "hasPrecipitation"(arg0: boolean): void
}

export namespace $ClimateSettingsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClimateSettingsContext$Type = ($ClimateSettingsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClimateSettingsContext_ = $ClimateSettingsContext$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/$ForgeClientModConstructor" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ClientModConstructor, $ClientModConstructor$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/$ClientModConstructor"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"

export class $ForgeClientModConstructor {


public static "construct"(arg0: $ClientModConstructor$Type, arg1: string, arg2: $Set$Type<($ContentRegistrationFlags$Type)>, arg3: $Set$Type<($ContentRegistrationFlags$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientModConstructor$Type = ($ForgeClientModConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientModConstructor_ = $ForgeClientModConstructor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerTickEvents" {
import {$PlayerTickEvents$End, $PlayerTickEvents$End$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerTickEvents$End"
import {$PlayerTickEvents$Start, $PlayerTickEvents$Start$Type} from "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerTickEvents$Start"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $PlayerTickEvents {
static readonly "START": $EventInvoker<($PlayerTickEvents$Start)>
static readonly "END": $EventInvoker<($PlayerTickEvents$End)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerTickEvents$Type = ($PlayerTickEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerTickEvents_ = $PlayerTickEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/biome/v1/$BiomeLoadingPhase" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BiomeLoadingPhase extends $Enum<($BiomeLoadingPhase)> {
static readonly "ADDITIONS": $BiomeLoadingPhase
static readonly "REMOVALS": $BiomeLoadingPhase
static readonly "MODIFICATIONS": $BiomeLoadingPhase
static readonly "POST_PROCESSING": $BiomeLoadingPhase


public static "values"(): ($BiomeLoadingPhase)[]
public static "valueOf"(arg0: string): $BiomeLoadingPhase
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeLoadingPhase$Type = (("additions") | ("removals") | ("post_processing") | ("modifications")) | ($BiomeLoadingPhase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeLoadingPhase_ = $BiomeLoadingPhase$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/item/$ForgeToolTypeHelper" {
import {$ToolTypeHelper, $ToolTypeHelper$Type} from "packages/fuzs/puzzleslib/api/item/v2/$ToolTypeHelper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ForgeToolTypeHelper implements $ToolTypeHelper {

constructor()

public "isShield"(arg0: $ItemStack$Type): boolean
public "isSword"(arg0: $ItemStack$Type): boolean
public "isAxe"(arg0: $ItemStack$Type): boolean
public "isCrossbow"(arg0: $ItemStack$Type): boolean
public "isTridentLike"(arg0: $ItemStack$Type): boolean
public "isShovel"(arg0: $ItemStack$Type): boolean
public "isBow"(arg0: $ItemStack$Type): boolean
public "isHoe"(arg0: $ItemStack$Type): boolean
public "isFishingRod"(arg0: $ItemStack$Type): boolean
public "isPickaxe"(arg0: $ItemStack$Type): boolean
public "isShears"(arg0: $ItemStack$Type): boolean
public "isMeleeWeapon"(arg0: $ItemStack$Type): boolean
public "isTool"(arg0: $ItemStack$Type): boolean
public "isMiningTool"(arg0: $ItemStack$Type): boolean
public "isRangedWeapon"(arg0: $ItemStack$Type): boolean
public "isWeapon"(arg0: $ItemStack$Type): boolean
/**
 * 
 * @deprecated
 */
public "isTrident"(arg0: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeToolTypeHelper$Type = ($ForgeToolTypeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeToolTypeHelper_ = $ForgeToolTypeHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/data/$ValueMutableFloat" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ValueMutableFloat implements $MutableFloat {

constructor(arg0: float)

public "accept"(arg0: float): void
public "getAsFloat"(): float
public static "fromValue"(arg0: float): $MutableFloat
public "mapFloat"(arg0: $UnaryOperator$Type<(float)>): void
public static "fromEvent"(arg0: $Consumer$Type<(float)>, arg1: $Supplier$Type<(float)>): $MutableFloat
get "asFloat"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueMutableFloat$Type = ($ValueMutableFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueMutableFloat_ = $ValueMutableFloat$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelTickEvents" {
import {$ClientLevelTickEvents$End, $ClientLevelTickEvents$End$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelTickEvents$End"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ClientLevelTickEvents$Start, $ClientLevelTickEvents$Start$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientLevelTickEvents$Start"

export class $ClientLevelTickEvents {
static readonly "START": $EventInvoker<($ClientLevelTickEvents$Start)>
static readonly "END": $EventInvoker<($ClientLevelTickEvents$End)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelTickEvents$Type = ($ClientLevelTickEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelTickEvents_ = $ClientLevelTickEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$LoggedIn" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvents$LoggedIn {

 "onLoggedIn"(arg0: $ServerPlayer$Type): void

(arg0: $ServerPlayer$Type): void
}

export namespace $PlayerEvents$LoggedIn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$LoggedIn$Type = ($PlayerEvents$LoggedIn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$LoggedIn_ = $PlayerEvents$LoggedIn$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/event/core/$EventPhaseImpl" {
import {$EventPhase, $EventPhase$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventPhase"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EventPhaseImpl$Ordering, $EventPhaseImpl$Ordering$Type} from "packages/fuzs/puzzleslib/impl/event/core/$EventPhaseImpl$Ordering"

export class $EventPhaseImpl extends $Record implements $EventPhase {

constructor(identifier: $ResourceLocation$Type, parent: $EventPhase$Type, ordering: $EventPhaseImpl$Ordering$Type)

public "parent"(): $EventPhase
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "identifier"(): $ResourceLocation
public "ordering"(): $EventPhaseImpl$Ordering
public "applyOrdering"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($ResourceLocation$Type)>): void
public static "early"(arg0: $EventPhase$Type): $EventPhase
public static "late"(arg0: $EventPhase$Type): $EventPhase
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventPhaseImpl$Type = ($EventPhaseImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventPhaseImpl_ = $EventPhaseImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$FogEvents$Render" {
import {$MutableFloat, $MutableFloat$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableFloat"
import {$FogShape, $FogShape$Type} from "packages/com/mojang/blaze3d/shaders/$FogShape"
import {$FogType, $FogType$Type} from "packages/net/minecraft/world/level/material/$FogType"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$MutableValue, $MutableValue$Type} from "packages/fuzs/puzzleslib/api/event/v1/data/$MutableValue"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$FogRenderer$FogMode, $FogRenderer$FogMode$Type} from "packages/net/minecraft/client/renderer/$FogRenderer$FogMode"

export interface $FogEvents$Render {

 "onRenderFog"(arg0: $GameRenderer$Type, arg1: $Camera$Type, arg2: float, arg3: $FogRenderer$FogMode$Type, arg4: $FogType$Type, arg5: $MutableFloat$Type, arg6: $MutableFloat$Type, arg7: $MutableValue$Type<($FogShape$Type)>): void

(arg0: $GameRenderer$Type, arg1: $Camera$Type, arg2: float, arg3: $FogRenderer$FogMode$Type, arg4: $FogType$Type, arg5: $MutableFloat$Type, arg6: $MutableFloat$Type, arg7: $MutableValue$Type<($FogShape$Type)>): void
}

export namespace $FogEvents$Render {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FogEvents$Render$Type = ($FogEvents$Render);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FogEvents$Render_ = $FogEvents$Render$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientTickEvents" {
import {$ClientTickEvents$End, $ClientTickEvents$End$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientTickEvents$End"
import {$ClientTickEvents$Start, $ClientTickEvents$Start$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ClientTickEvents$Start"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ClientTickEvents {
static readonly "START": $EventInvoker<($ClientTickEvents$Start)>
static readonly "END": $EventInvoker<($ClientTickEvents$End)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickEvents$Type = ($ClientTickEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickEvents_ = $ClientTickEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/context/$BlockRenderTypesContextImpl" {
import {$RenderTypesContext, $RenderTypesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$RenderTypesContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BlockRenderTypesContextImpl implements $RenderTypesContext<($Block)> {

constructor()

public "registerRenderType"(arg0: $RenderType$Type, ...arg1: ($Block$Type)[]): void
public "getRenderType"(arg0: $Block$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRenderTypesContextImpl$Type = ($BlockRenderTypesContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRenderTypesContextImpl_ = $BlockRenderTypesContextImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderGuiElementEvents$Before" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $RenderGuiElementEvents$Before {

 "onBeforeRenderGuiElement"(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): $EventResult

(arg0: $Minecraft$Type, arg1: $GuiGraphics$Type, arg2: float, arg3: integer, arg4: integer): $EventResult
}

export namespace $RenderGuiElementEvents$Before {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderGuiElementEvents$Before$Type = ($RenderGuiElementEvents$Before);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderGuiElementEvents$Before_ = $RenderGuiElementEvents$Before$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/init/$ForgeItemDisplayOverrides" {
import {$ItemDisplayOverridesImpl, $ItemDisplayOverridesImpl$Type} from "packages/fuzs/puzzleslib/impl/client/init/$ItemDisplayOverridesImpl"

export class $ForgeItemDisplayOverrides extends $ItemDisplayOverridesImpl {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeItemDisplayOverrides$Type = ($ForgeItemDisplayOverrides);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeItemDisplayOverrides_ = $ForgeItemDisplayOverrides$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents" {
import {$ServerChunkEvents$Load, $ServerChunkEvents$Load$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Load"
import {$ServerChunkEvents$Unload, $ServerChunkEvents$Unload$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Unload"
import {$ServerChunkEvents$Unwatch, $ServerChunkEvents$Unwatch$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Unwatch"
import {$ServerChunkEvents$Watch, $ServerChunkEvents$Watch$Type} from "packages/fuzs/puzzleslib/api/event/v1/level/$ServerChunkEvents$Watch"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $ServerChunkEvents {
static readonly "LOAD": $EventInvoker<($ServerChunkEvents$Load)>
static readonly "UNLOAD": $EventInvoker<($ServerChunkEvents$Unload)>
static readonly "WATCH": $EventInvoker<($ServerChunkEvents$Watch)>
static readonly "UNWATCH": $EventInvoker<($ServerChunkEvents$Unwatch)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerChunkEvents$Type = ($ServerChunkEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerChunkEvents_ = $ServerChunkEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents" {
import {$ModelEvents$ModifyBakingResult, $ModelEvents$ModifyBakingResult$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$ModifyBakingResult"
import {$ModelEvents$AfterModelLoading, $ModelEvents$AfterModelLoading$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$AfterModelLoading"
import {$ModelEvents$BakingCompleted, $ModelEvents$BakingCompleted$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$BakingCompleted"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$ModelEvents$AdditionalBakedModel, $ModelEvents$AdditionalBakedModel$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$AdditionalBakedModel"
import {$ModelEvents$ModifyUnbakedModel, $ModelEvents$ModifyUnbakedModel$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$ModifyUnbakedModel"
import {$ModelEvents$ModifyBakedModel, $ModelEvents$ModifyBakedModel$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ModelEvents$ModifyBakedModel"

export class $ModelEvents {
static readonly "MODIFY_UNBAKED_MODEL": $EventInvoker<($ModelEvents$ModifyUnbakedModel)>
static readonly "MODIFY_BAKED_MODEL": $EventInvoker<($ModelEvents$ModifyBakedModel)>
static readonly "ADDITIONAL_BAKED_MODEL": $EventInvoker<($ModelEvents$AdditionalBakedModel)>
static readonly "AFTER_MODEL_LOADING": $EventInvoker<($ModelEvents$AfterModelLoading)>
/**
 * 
 * @deprecated
 */
static readonly "MODIFY_BAKING_RESULT": $EventInvoker<($ModelEvents$ModifyBakingResult)>
/**
 * 
 * @deprecated
 */
static readonly "BAKING_COMPLETED": $EventInvoker<($ModelEvents$BakingCompleted)>


/**
 * 
 * @deprecated
 */
public static "modifyBakingResult"(arg0: string): $EventInvoker<($ModelEvents$ModifyBakingResult)>
/**
 * 
 * @deprecated
 */
public static "bakingCompleted"(arg0: string): $EventInvoker<($ModelEvents$BakingCompleted)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelEvents$Type = ($ModelEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelEvents_ = $ModelEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents" {
import {$InteractionInputEvents$AttackV2, $InteractionInputEvents$AttackV2$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$AttackV2"
import {$InteractionInputEvents$Pick, $InteractionInputEvents$Pick$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$Pick"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$InteractionInputEvents$Use, $InteractionInputEvents$Use$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$Use"
import {$InteractionInputEvents$Attack, $InteractionInputEvents$Attack$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$Attack"

export class $InteractionInputEvents {
/**
 * 
 * @deprecated
 */
static readonly "ATTACK": $EventInvoker<($InteractionInputEvents$Attack)>
static readonly "ATTACK_V2": $EventInvoker<($InteractionInputEvents$AttackV2)>
static readonly "USE": $EventInvoker<($InteractionInputEvents$Use)>
static readonly "PICK": $EventInvoker<($InteractionInputEvents$Pick)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionInputEvents$Type = ($InteractionInputEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionInputEvents_ = $InteractionInputEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/init/$PotionBrewingRegistryForge" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$PotionBrewingRegistry, $PotionBrewingRegistry$Type} from "packages/fuzs/puzzleslib/api/init/v2/$PotionBrewingRegistry"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$PotionItem, $PotionItem$Type} from "packages/net/minecraft/world/item/$PotionItem"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $PotionBrewingRegistryForge implements $PotionBrewingRegistry {

constructor()

public "registerContainerRecipe"(arg0: $PotionItem$Type, arg1: $Ingredient$Type, arg2: $PotionItem$Type): void
public "registerPotionContainer"(arg0: $PotionItem$Type): void
public "registerPotionRecipe"(arg0: $Potion$Type, arg1: $Ingredient$Type, arg2: $Potion$Type): void
public "registerContainerRecipe"(arg0: $PotionItem$Type, arg1: $Item$Type, arg2: $PotionItem$Type): void
public "registerPotionRecipe"(arg0: $Potion$Type, arg1: $Item$Type, arg2: $Potion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionBrewingRegistryForge$Type = ($PotionBrewingRegistryForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionBrewingRegistryForge_ = $PotionBrewingRegistryForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientChunkEvents$Unload" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"

export interface $ClientChunkEvents$Unload {

 "onChunkUnload"(arg0: $ClientLevel$Type, arg1: $LevelChunk$Type): void

(arg0: $ClientLevel$Type, arg1: $LevelChunk$Type): void
}

export namespace $ClientChunkEvents$Unload {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChunkEvents$Unload$Type = ($ClientChunkEvents$Unload);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChunkEvents$Unload_ = $ClientChunkEvents$Unload$Type;
}}
declare module "packages/fuzs/puzzleslib/api/init/v2/builder/$PoiTypeBuilder" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

/**
 * 
 * @deprecated
 */
export class $PoiTypeBuilder extends $Record {

constructor(ticketCount: integer, searchDistance: integer, blocks: $Iterable$Type<($BlockState$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: integer, arg1: integer, ...arg2: ($Block$Type)[]): $PoiTypeBuilder
public static "of"(arg0: integer, arg1: integer, arg2: $Iterable$Type<($BlockState$Type)>): $PoiTypeBuilder
public "blocks"(): $Iterable<($BlockState)>
public "searchDistance"(): integer
public "ticketCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PoiTypeBuilder$Type = ($PoiTypeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PoiTypeBuilder_ = $PoiTypeBuilder$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/$PuzzlesLibClient" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$ClientTooltipComponentsContext, $ClientTooltipComponentsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ClientTooltipComponentsContext"
import {$LayerDefinitionsContext, $LayerDefinitionsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LayerDefinitionsContext"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$AdditionalModelsContext, $AdditionalModelsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$AdditionalModelsContext"
import {$ClientModConstructor, $ClientModConstructor$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/$ClientModConstructor"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SearchRegistryContext, $SearchRegistryContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SearchRegistryContext"
import {$ItemDecorationContext, $ItemDecorationContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemDecorationContext"
import {$DynamicModifyBakingResultContext, $DynamicModifyBakingResultContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicModifyBakingResultContext"
import {$SkullRenderersContext, $SkullRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$SkullRenderersContext"
import {$CoreShadersContext, $CoreShadersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$CoreShadersContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ColorProvidersContext, $ColorProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ColorProvidersContext"
import {$BlockEntityRenderersContext, $BlockEntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BlockEntityRenderersContext"
import {$DynamicBakingCompletedContext, $DynamicBakingCompletedContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$DynamicBakingCompletedContext"
import {$BuiltinModelItemRendererContext, $BuiltinModelItemRendererContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$BuiltinModelItemRendererContext"
import {$EntityRenderersContext, $EntityRenderersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntityRenderersContext"
import {$RenderTypesContext, $RenderTypesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$RenderTypesContext"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$PackRepositorySourcesContext, $PackRepositorySourcesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$PackRepositorySourcesContext"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$KeyMappingsContext, $KeyMappingsContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$KeyMappingsContext"
import {$ParticleProvidersContext, $ParticleProvidersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ParticleProvidersContext"
import {$ModLifecycleContext, $ModLifecycleContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$ModLifecycleContext"
import {$AddReloadListenersContext, $AddReloadListenersContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$AddReloadListenersContext"
import {$EntitySpectatorShaderContext, $EntitySpectatorShaderContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$EntitySpectatorShaderContext"
import {$LivingEntityRenderLayersContext, $LivingEntityRenderLayersContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$LivingEntityRenderLayersContext"
import {$ItemModelPropertiesContext, $ItemModelPropertiesContext$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/context/$ItemModelPropertiesContext"

export class $PuzzlesLibClient implements $ClientModConstructor {

constructor()

public static "construct"(arg0: string, arg1: $Supplier$Type<($ClientModConstructor$Type)>): void
/**
 * 
 * @deprecated
 */
public static "construct"(arg0: string, arg1: $Supplier$Type<($ClientModConstructor$Type)>, ...arg2: ($ContentRegistrationFlags$Type)[]): void
public "onConstructMod"(): void
public "onRegisterParticleProviders"(arg0: $ParticleProvidersContext$Type): void
/**
 * 
 * @deprecated
 */
public "onBakingCompleted"(arg0: $DynamicBakingCompletedContext$Type): void
/**
 * 
 * @deprecated
 */
public "onModifyBakingResult"(arg0: $DynamicModifyBakingResultContext$Type): void
public "onRegisterBuiltinModelItemRenderers"(arg0: $BuiltinModelItemRendererContext$Type): void
public "onRegisterResourcePackReloadListeners"(arg0: $AddReloadListenersContext$Type): void
/**
 * 
 * @deprecated
 */
public "onClientSetup"(arg0: $ModLifecycleContext$Type): void
public "onClientSetup"(): void
public "onRegisterFluidRenderTypes"(arg0: $RenderTypesContext$Type<($Fluid$Type)>): void
public "onRegisterEntitySpectatorShaders"(arg0: $EntitySpectatorShaderContext$Type): void
public "onRegisterLivingEntityRenderLayers"(arg0: $LivingEntityRenderLayersContext$Type): void
public "onRegisterEntityRenderers"(arg0: $EntityRenderersContext$Type): void
public "onRegisterLayerDefinitions"(arg0: $LayerDefinitionsContext$Type): void
public "onRegisterItemModelProperties"(arg0: $ItemModelPropertiesContext$Type): void
public "onRegisterBlockColorProviders"(arg0: $ColorProvidersContext$Type<($Block$Type), ($BlockColor$Type)>): void
public "onRegisterCoreShaders"(arg0: $CoreShadersContext$Type): void
public "onRegisterItemDecorations"(arg0: $ItemDecorationContext$Type): void
/**
 * 
 * @deprecated
 */
public "onRegisterSearchTrees"(arg0: $SearchRegistryContext$Type): void
public "onRegisterSkullRenderers"(arg0: $SkullRenderersContext$Type): void
public "onAddResourcePackFinders"(arg0: $PackRepositorySourcesContext$Type): void
public "onRegisterItemColorProviders"(arg0: $ColorProvidersContext$Type<($Item$Type), ($ItemColor$Type)>): void
public "onRegisterBlockEntityRenderers"(arg0: $BlockEntityRenderersContext$Type): void
public "onRegisterClientTooltipComponents"(arg0: $ClientTooltipComponentsContext$Type): void
public "onRegisterBlockRenderTypes"(arg0: $RenderTypesContext$Type<($Block$Type)>): void
public "onRegisterKeyMappings"(arg0: $KeyMappingsContext$Type): void
public "onRegisterAdditionalModels"(arg0: $AdditionalModelsContext$Type): void
public "getContentRegistrationFlags"(): ($ContentRegistrationFlags)[]
public "getPairingIdentifier"(): $ResourceLocation
get "contentRegistrationFlags"(): ($ContentRegistrationFlags)[]
get "pairingIdentifier"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PuzzlesLibClient$Type = ($PuzzlesLibClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PuzzlesLibClient_ = $PuzzlesLibClient$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$StartTracking" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerEvents$StartTracking {

 "onStartTracking"(arg0: $Entity$Type, arg1: $ServerPlayer$Type): void

(arg0: $Entity$Type, arg1: $ServerPlayer$Type): void
}

export namespace $PlayerEvents$StartTracking {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$StartTracking$Type = ($PlayerEvents$StartTracking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$StartTracking_ = $PlayerEvents$StartTracking$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$BeforeMouseScroll" {
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $InputEvents$BeforeMouseScroll {

 "onBeforeMouseScroll"(arg0: boolean, arg1: boolean, arg2: boolean, arg3: double, arg4: double): $EventResult

(arg0: boolean, arg1: boolean, arg2: boolean, arg3: double, arg4: double): $EventResult
}

export namespace $InputEvents$BeforeMouseScroll {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$BeforeMouseScroll$Type = ($InputEvents$BeforeMouseScroll);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents$BeforeMouseScroll_ = $InputEvents$BeforeMouseScroll$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/$PuzzlesLib" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PuzzlesLib {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOGGER": $Logger

constructor()

public static "id"(arg0: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PuzzlesLib$Type = ($PuzzlesLib);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PuzzlesLib_ = $PuzzlesLib$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ClientEntityLevelEvents$Load" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ClientEntityLevelEvents$Load {

 "onEntityLoad"(arg0: $Entity$Type, arg1: $ClientLevel$Type): $EventResult

(arg0: $Entity$Type, arg1: $ClientLevel$Type): $EventResult
}

export namespace $ClientEntityLevelEvents$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEntityLevelEvents$Load$Type = ($ClientEntityLevelEvents$Load);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEntityLevelEvents$Load_ = $ClientEntityLevelEvents$Load$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/event/$ForgeClientEventInvokers" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ForgeClientEventInvokers {

constructor()

public static "register"(): void
public static "registerLoadingHandlers"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientEventInvokers$Type = ($ForgeClientEventInvokers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientEventInvokers_ = $ForgeClientEventInvokers$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/client/core/$ClientFactories" {
import {$KeyMappingActivationHelper, $KeyMappingActivationHelper$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$KeyMappingActivationHelper"
import {$ItemDisplayOverridesImpl, $ItemDisplayOverridesImpl$Type} from "packages/fuzs/puzzleslib/impl/client/init/$ItemDisplayOverridesImpl"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ScreenHelper, $ScreenHelper$Type} from "packages/fuzs/puzzleslib/api/client/screen/v2/$ScreenHelper"
import {$ClientModConstructor, $ClientModConstructor$Type} from "packages/fuzs/puzzleslib/api/client/core/v1/$ClientModConstructor"
import {$ContentRegistrationFlags, $ContentRegistrationFlags$Type} from "packages/fuzs/puzzleslib/api/core/v1/$ContentRegistrationFlags"

export interface $ClientFactories {

 "getKeyMappingActivationHelper"(): $KeyMappingActivationHelper
 "getItemModelDisplayOverrides"(): $ItemDisplayOverridesImpl
 "constructClientMod"(arg0: string, arg1: $ClientModConstructor$Type, arg2: $Set$Type<($ContentRegistrationFlags$Type)>, arg3: $Set$Type<($ContentRegistrationFlags$Type)>): void
 "getScreenHelper"(): $ScreenHelper
}

export namespace $ClientFactories {
const INSTANCE: $ClientFactories
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientFactories$Type = ($ClientFactories);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientFactories_ = $ClientFactories$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$Attack" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

/**
 * 
 * @deprecated
 */
export interface $InteractionInputEvents$Attack {

 "onAttackInteraction"(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type): $EventResult

(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type): $EventResult
}

export namespace $InteractionInputEvents$Attack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionInputEvents$Attack$Type = ($InteractionInputEvents$Attack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionInputEvents$Attack_ = $InteractionInputEvents$Attack$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/biome/$SpecialEffectsContextForge" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$SpecialEffectsContext, $SpecialEffectsContext$Type} from "packages/fuzs/puzzleslib/api/biome/v1/$SpecialEffectsContext"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Music, $Music$Type} from "packages/net/minecraft/sounds/$Music"
import {$AmbientParticleSettings, $AmbientParticleSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientParticleSettings"
import {$AmbientMoodSettings, $AmbientMoodSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientMoodSettings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AmbientAdditionsSettings, $AmbientAdditionsSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientAdditionsSettings"
import {$BiomeSpecialEffectsBuilder, $BiomeSpecialEffectsBuilder$Type} from "packages/net/minecraftforge/common/world/$BiomeSpecialEffectsBuilder"
import {$BiomeSpecialEffects$GrassColorModifier, $BiomeSpecialEffects$GrassColorModifier$Type} from "packages/net/minecraft/world/level/biome/$BiomeSpecialEffects$GrassColorModifier"

export class $SpecialEffectsContextForge extends $Record implements $SpecialEffectsContext {

constructor(context: $BiomeSpecialEffectsBuilder$Type)

public "context"(): $BiomeSpecialEffectsBuilder
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getGrassColorOverride"(): $Optional<(integer)>
public "getFoliageColorOverride"(): $Optional<(integer)>
public "getGrassColorModifier"(): $BiomeSpecialEffects$GrassColorModifier
public "setGrassColorOverride"(arg0: $Optional$Type<(integer)>): void
public "setGrassColorModifier"(arg0: $BiomeSpecialEffects$GrassColorModifier$Type): void
public "setFoliageColorOverride"(arg0: $Optional$Type<(integer)>): void
public "setAmbientParticleSettings"(arg0: $Optional$Type<($AmbientParticleSettings$Type)>): void
public "getAmbientParticleSettings"(): $Optional<($AmbientParticleSettings)>
public "setAmbientLoopSoundEvent"(arg0: $Optional$Type<($Holder$Type<($SoundEvent$Type)>)>): void
public "getAmbientLoopSoundEvent"(): $Optional<($Holder<($SoundEvent)>)>
public "setAmbientMoodSettings"(arg0: $Optional$Type<($AmbientMoodSettings$Type)>): void
public "getAmbientMoodSettings"(): $Optional<($AmbientMoodSettings)>
public "setAmbientAdditionsSettings"(arg0: $Optional$Type<($AmbientAdditionsSettings$Type)>): void
public "getAmbientAdditionsSettings"(): $Optional<($AmbientAdditionsSettings)>
public "getSkyColor"(): integer
public "getWaterFogColor"(): integer
public "getBackgroundMusic"(): $Optional<($Music)>
public "setWaterFogColor"(arg0: integer): void
public "setSkyColor"(arg0: integer): void
public "setWaterColor"(arg0: integer): void
public "setFogColor"(arg0: integer): void
public "setBackgroundMusic"(arg0: $Optional$Type<($Music$Type)>): void
public "getWaterColor"(): integer
public "getFogColor"(): integer
public "setGrassColorOverride"(arg0: integer): void
public "setFoliageColorOverride"(arg0: integer): void
public "clearFoliageColorOverride"(): void
public "setAmbientParticleSettings"(arg0: $AmbientParticleSettings$Type): void
public "clearGrassColorOverride"(): void
public "setAmbientLoopSoundEvent"(arg0: $Holder$Type<($SoundEvent$Type)>): void
public "clearAmbientParticleSettings"(): void
public "setAmbientMoodSettings"(arg0: $AmbientMoodSettings$Type): void
public "clearAmbientLoopSoundEvent"(): void
public "clearAmbientMoodSettings"(): void
public "clearAmbientAdditionsSettings"(): void
public "setAmbientAdditionsSettings"(arg0: $AmbientAdditionsSettings$Type): void
public "clearBackgroundMusic"(): void
public "setBackgroundMusic"(arg0: $Music$Type): void
get "grassColorOverride"(): $Optional<(integer)>
get "foliageColorOverride"(): $Optional<(integer)>
get "grassColorModifier"(): $BiomeSpecialEffects$GrassColorModifier
set "grassColorOverride"(value: $Optional$Type<(integer)>)
set "grassColorModifier"(value: $BiomeSpecialEffects$GrassColorModifier$Type)
set "foliageColorOverride"(value: $Optional$Type<(integer)>)
set "ambientParticleSettings"(value: $Optional$Type<($AmbientParticleSettings$Type)>)
get "ambientParticleSettings"(): $Optional<($AmbientParticleSettings)>
set "ambientLoopSoundEvent"(value: $Optional$Type<($Holder$Type<($SoundEvent$Type)>)>)
get "ambientLoopSoundEvent"(): $Optional<($Holder<($SoundEvent)>)>
set "ambientMoodSettings"(value: $Optional$Type<($AmbientMoodSettings$Type)>)
get "ambientMoodSettings"(): $Optional<($AmbientMoodSettings)>
set "ambientAdditionsSettings"(value: $Optional$Type<($AmbientAdditionsSettings$Type)>)
get "ambientAdditionsSettings"(): $Optional<($AmbientAdditionsSettings)>
get "skyColor"(): integer
get "waterFogColor"(): integer
get "backgroundMusic"(): $Optional<($Music)>
set "waterFogColor"(value: integer)
set "skyColor"(value: integer)
set "waterColor"(value: integer)
set "fogColor"(value: integer)
set "backgroundMusic"(value: $Optional$Type<($Music$Type)>)
get "waterColor"(): integer
get "fogColor"(): integer
set "grassColorOverride"(value: integer)
set "foliageColorOverride"(value: integer)
set "ambientParticleSettings"(value: $AmbientParticleSettings$Type)
set "ambientLoopSoundEvent"(value: $Holder$Type<($SoundEvent$Type)>)
set "ambientMoodSettings"(value: $AmbientMoodSettings$Type)
set "ambientAdditionsSettings"(value: $AmbientAdditionsSettings$Type)
set "backgroundMusic"(value: $Music$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialEffectsContextForge$Type = ($SpecialEffectsContextForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialEffectsContextForge_ = $SpecialEffectsContextForge$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenKeyboardEvents$AfterKeyRelease" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenKeyboardEvents$AfterKeyRelease<T extends $Screen> {

 "onAfterKeyRelease"(arg0: T, arg1: integer, arg2: integer, arg3: integer): void

(arg0: T, arg1: integer, arg2: integer, arg3: integer): void
}

export namespace $ScreenKeyboardEvents$AfterKeyRelease {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenKeyboardEvents$AfterKeyRelease$Type<T> = ($ScreenKeyboardEvents$AfterKeyRelease<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenKeyboardEvents$AfterKeyRelease_<T> = $ScreenKeyboardEvents$AfterKeyRelease$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$BlockInteractionsContextForgeImpl" {
import {$BlockInteractionsContext, $BlockInteractionsContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$BlockInteractionsContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockInteractionsContextForgeImpl implements $BlockInteractionsContext {

constructor()

public "registerFlattenable"(arg0: $BlockState$Type, ...arg1: ($Block$Type)[]): void
public "registerWaxable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
public "registerStrippable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
public "registerScrapeable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
public "registerTillable"(arg0: $BlockState$Type, arg1: $ItemLike$Type, arg2: boolean, ...arg3: ($Block$Type)[]): void
public "registerFlattenable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
public "registerTillable"(arg0: $Block$Type, arg1: $ItemLike$Type, ...arg2: ($Block$Type)[]): void
public "registerTillable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInteractionsContextForgeImpl$Type = ($BlockInteractionsContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInteractionsContextForgeImpl_ = $BlockInteractionsContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InputEvents$AfterKeyAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $InputEvents$AfterKeyAction {

 "onAfterKeyAction"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void

(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
}

export namespace $InputEvents$AfterKeyAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvents$AfterKeyAction$Type = ($InputEvents$AfterKeyAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvents$AfterKeyAction_ = $InputEvents$AfterKeyAction$Type;
}}
declare module "packages/fuzs/puzzleslib/impl/core/context/$FuelBurnTimesContextForgeImpl" {
import {$FuelBurnTimesContext, $FuelBurnTimesContext$Type} from "packages/fuzs/puzzleslib/api/core/v1/context/$FuelBurnTimesContext"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $FuelBurnTimesContextForgeImpl implements $FuelBurnTimesContext {

constructor()

public "registerFuel"(arg0: integer, ...arg1: ($ItemLike$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FuelBurnTimesContextForgeImpl$Type = ($FuelBurnTimesContextForgeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FuelBurnTimesContextForgeImpl_ = $FuelBurnTimesContextForgeImpl$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/screen/v2/$ScreenHelper" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $ScreenHelper {

 "getMouseX"(arg0: $Minecraft$Type): integer
 "getMouseX"(arg0: $Screen$Type): integer
 "getMouseY"(arg0: $Screen$Type): integer
 "getMouseY"(arg0: $Minecraft$Type): integer
 "getMinecraft"(arg0: $Screen$Type): $Minecraft
 "getFont"(arg0: $Screen$Type): $Font
 "getLeftPos"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getImageWidth"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getTopPos"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "getHoveredSlot"(arg0: $AbstractContainerScreen$Type<(any)>): $Slot
 "findSlot"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: double, arg2: double): $Slot
 "getImageHeight"(arg0: $AbstractContainerScreen$Type<(any)>): integer
 "isHovering"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $Slot$Type, arg2: double, arg3: double): boolean
 "isHovering"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: double, arg5: double): boolean
}

export namespace $ScreenHelper {
const INSTANCE: $ScreenHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenHelper$Type = ($ScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenHelper_ = $ScreenHelper$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$PlayerEvents$LoggedOut" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvents$LoggedOut {

 "onLoggedOut"(arg0: $ServerPlayer$Type): void

(arg0: $ServerPlayer$Type): void
}

export namespace $PlayerEvents$LoggedOut {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvents$LoggedOut$Type = ($PlayerEvents$LoggedOut);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvents$LoggedOut_ = $PlayerEvents$LoggedOut$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$AfterInitV2" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ScreenEvents$ConsumingOperator, $ScreenEvents$ConsumingOperator$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$ScreenEvents$ConsumingOperator"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenEvents$AfterInitV2<T extends $Screen> {

 "onAfterInit"(arg0: $Minecraft$Type, arg1: T, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>, arg5: $ScreenEvents$ConsumingOperator$Type<($AbstractWidget$Type)>, arg6: $ScreenEvents$ConsumingOperator$Type<($AbstractWidget$Type)>): void

(arg0: $Minecraft$Type, arg1: T, arg2: integer, arg3: integer, arg4: $List$Type<($AbstractWidget$Type)>, arg5: $ScreenEvents$ConsumingOperator$Type<($AbstractWidget$Type)>, arg6: $ScreenEvents$ConsumingOperator$Type<($AbstractWidget$Type)>): void
}

export namespace $ScreenEvents$AfterInitV2 {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenEvents$AfterInitV2$Type<T> = ($ScreenEvents$AfterInitV2<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenEvents$AfterInitV2_<T> = $ScreenEvents$AfterInitV2$Type<(T)>;
}}
declare module "packages/fuzs/puzzleslib/api/client/core/v1/context/$SearchRegistryContext" {
import {$SearchRegistry$TreeBuilderSupplier, $SearchRegistry$TreeBuilderSupplier$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$TreeBuilderSupplier"
import {$SearchRegistry$Key, $SearchRegistry$Key$Type} from "packages/net/minecraft/client/searchtree/$SearchRegistry$Key"

/**
 * 
 * @deprecated
 */
export interface $SearchRegistryContext {

 "registerSearchTree"<T>(arg0: $SearchRegistry$Key$Type<(T)>, arg1: $SearchRegistry$TreeBuilderSupplier$Type<(T)>): void
}

export namespace $SearchRegistryContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchRegistryContext$Type = ($SearchRegistryContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchRegistryContext_ = $SearchRegistryContext$Type;
}}
declare module "packages/fuzs/puzzleslib/api/event/v1/entity/player/$ItemTossCallback" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export interface $ItemTossCallback {

 "onItemToss"(arg0: $ItemEntity$Type, arg1: $Player$Type): $EventResult

(arg0: $ItemEntity$Type, arg1: $Player$Type): $EventResult
}

export namespace $ItemTossCallback {
const EVENT: $EventInvoker<($ItemTossCallback)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTossCallback$Type = ($ItemTossCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTossCallback_ = $ItemTossCallback$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$RenderLivingEvents$Before" {
import {$LivingEntityRenderer, $LivingEntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$LivingEntityRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $RenderLivingEvents$Before {

 "onBeforeRenderEntity"<T extends $LivingEntity, M extends $EntityModel<(T)>>(arg0: T, arg1: $LivingEntityRenderer$Type<(T), (M)>, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): $EventResult

(arg0: T, arg1: $LivingEntityRenderer$Type<(T), (M)>, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): $EventResult
}

export namespace $RenderLivingEvents$Before {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLivingEvents$Before$Type = ($RenderLivingEvents$Before);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLivingEvents$Before_ = $RenderLivingEvents$Before$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$InteractionInputEvents$Pick" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$EventResult, $EventResult$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventResult"

export interface $InteractionInputEvents$Pick {

 "onPickInteraction"(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type, arg2: $HitResult$Type): $EventResult

(arg0: $Minecraft$Type, arg1: $LocalPlayer$Type, arg2: $HitResult$Type): $EventResult
}

export namespace $InteractionInputEvents$Pick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionInputEvents$Pick$Type = ($InteractionInputEvents$Pick);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionInputEvents$Pick_ = $InteractionInputEvents$Pick$Type;
}}
declare module "packages/fuzs/puzzleslib/mixin/client/accessor/$ItemForgeAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ItemForgeAccessor {

 "puzzleslib$setRenderProperties"(arg0: any): void
 "puzzleslib$getRenderProperties"(): any
}

export namespace $ItemForgeAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemForgeAccessor$Type = ($ItemForgeAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemForgeAccessor_ = $ItemForgeAccessor$Type;
}}
declare module "packages/fuzs/puzzleslib/api/client/event/v1/$GatherDebugTextEvents" {
import {$GatherDebugTextEvents$Left, $GatherDebugTextEvents$Left$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$GatherDebugTextEvents$Left"
import {$GatherDebugTextEvents$Right, $GatherDebugTextEvents$Right$Type} from "packages/fuzs/puzzleslib/api/client/event/v1/$GatherDebugTextEvents$Right"
import {$EventInvoker, $EventInvoker$Type} from "packages/fuzs/puzzleslib/api/event/v1/core/$EventInvoker"

export class $GatherDebugTextEvents {
static readonly "LEFT": $EventInvoker<($GatherDebugTextEvents$Left)>
static readonly "RIGHT": $EventInvoker<($GatherDebugTextEvents$Right)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherDebugTextEvents$Type = ($GatherDebugTextEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherDebugTextEvents_ = $GatherDebugTextEvents$Type;
}}
declare module "packages/fuzs/puzzleslib/api/core/v1/context/$BlockInteractionsContext" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BlockInteractionsContext {

 "registerFlattenable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
 "registerFlattenable"(arg0: $BlockState$Type, ...arg1: ($Block$Type)[]): void
 "registerWaxable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
 "registerStrippable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
 "registerScrapeable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
 "registerTillable"(arg0: $BlockState$Type, arg1: $ItemLike$Type, arg2: boolean, ...arg3: ($Block$Type)[]): void
 "registerTillable"(arg0: $Block$Type, arg1: $ItemLike$Type, ...arg2: ($Block$Type)[]): void
 "registerTillable"(arg0: $Block$Type, ...arg1: ($Block$Type)[]): void
}

export namespace $BlockInteractionsContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockInteractionsContext$Type = ($BlockInteractionsContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockInteractionsContext_ = $BlockInteractionsContext$Type;
}}
