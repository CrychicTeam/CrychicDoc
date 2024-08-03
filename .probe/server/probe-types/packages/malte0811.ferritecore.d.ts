declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/fastmap/immutable/$FastMapValueSet" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$FerriteCoreImmutableCollectionAccess, $FerriteCoreImmutableCollectionAccess$Type} from "packages/com/google/common/collect/$FerriteCoreImmutableCollectionAccess"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"

export class $FastMapValueSet extends $FerriteCoreImmutableCollectionAccess<($Comparable<(any)>)> {

constructor(arg0: $FastMapStateHolder$Type<(any)>)

public "size"(): integer
public "contains"(arg0: any): boolean
public "isPartialView"(): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "partialView"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMapValueSet$Type = ($FastMapValueSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMapValueSet_ = $FastMapValueSet$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$SubShapeAccess" {
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$DiscreteVSAccess, $DiscreteVSAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$DiscreteVSAccess"

export interface $SubShapeAccess extends $DiscreteVSAccess {

 "getParent"(): $DiscreteVoxelShape
 "getEndX"(): integer
 "getStartX"(): integer
 "getStartY"(): integer
 "getEndZ"(): integer
 "getEndY"(): integer
 "getStartZ"(): integer
 "getZSize"(): integer
 "getXSize"(): integer
 "getYSize"(): integer
}

export namespace $SubShapeAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubShapeAccess$Type = ($SubShapeAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubShapeAccess_ = $SubShapeAccess$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/config/$FerriteConfig" {
import {$FerriteConfig$Option, $FerriteConfig$Option$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteConfig$Option"

export class $FerriteConfig {
static readonly "NEIGHBOR_LOOKUP": $FerriteConfig$Option
static readonly "PROPERTY_MAP": $FerriteConfig$Option
static readonly "PREDICATES": $FerriteConfig$Option
static readonly "MRL_CACHE": $FerriteConfig$Option
static readonly "DEDUP_MULTIPART": $FerriteConfig$Option
static readonly "DEDUP_BLOCKSTATE_CACHE": $FerriteConfig$Option
static readonly "DEDUP_QUADS": $FerriteConfig$Option
static readonly "COMPACT_FAST_MAP": $FerriteConfig$Option
static readonly "POPULATE_NEIGHBOR_TABLE": $FerriteConfig$Option
static readonly "THREADING_DETECTOR": $FerriteConfig$Option
static readonly "MODEL_SIDES": $FerriteConfig$Option

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FerriteConfig$Type = ($FerriteConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FerriteConfig_ = $FerriteConfig$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/$FastMap" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$ImmutableSet, $ImmutableSet$Type} from "packages/com/google/common/collect/$ImmutableSet"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$FastMapKey, $FastMapKey$Type} from "packages/malte0811/ferritecore/fastmap/$FastMapKey"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $FastMap<Value> {

constructor(arg0: $Collection$Type<($Property$Type<(any)>)>, arg1: $Map$Type<($Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>), (Value)>, arg2: boolean)

public "getValue"(arg0: integer, arg1: any): $Comparable<(any)>
public "getValue"<T extends $Comparable<(T)>>(arg0: integer, arg1: $Property$Type<(T)>): T
public "getKey"(arg0: integer): $FastMapKey<(any)>
public "getEntry"(arg0: integer, arg1: integer): $Map$Entry<($Property<(any)>), ($Comparable<(any)>)>
public "with"<T extends $Comparable<(T)>>(arg0: integer, arg1: $Property$Type<(T)>, arg2: T): Value
public "withUnsafe"<T extends $Comparable<(T)>>(arg0: integer, arg1: $Property$Type<(T)>, arg2: any): Value
public "getIndexOf"(arg0: $Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>): integer
public "getPropertySet"(): $ImmutableSet<($Property<(any)>)>
public "numProperties"(): integer
public "isSingleState"(): boolean
get "propertySet"(): $ImmutableSet<($Property<(any)>)>
get "singleState"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMap$Type<Value> = ($FastMap<(Value)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMap_<Value> = $FastMap$Type<(Value)>;
}}
declare module "packages/malte0811/ferritecore/hash/$ArrayVoxelShapeHash" {
import {$ArrayVSAccess, $ArrayVSAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$ArrayVSAccess"
import {$Hash$Strategy, $Hash$Strategy$Type} from "packages/it/unimi/dsi/fastutil/$Hash$Strategy"

export class $ArrayVoxelShapeHash implements $Hash$Strategy<($ArrayVSAccess)> {
static readonly "INSTANCE": $ArrayVoxelShapeHash

constructor()

public "equals"(arg0: $ArrayVSAccess$Type, arg1: $ArrayVSAccess$Type): boolean
public "hashCode"(arg0: $ArrayVSAccess$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayVoxelShapeHash$Type = ($ArrayVoxelShapeHash);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayVoxelShapeHash_ = $ArrayVoxelShapeHash$Type;
}}
declare module "packages/malte0811/ferritecore/$IPlatformHooks" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPlatformHooks {

 "computeBlockstateCacheFieldName"(): string

(): string
}

export namespace $IPlatformHooks {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformHooks$Type = ($IPlatformHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformHooks_ = $IPlatformHooks$Type;
}}
declare module "packages/malte0811/ferritecore/ducks/$FastMapStateHolder" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Table, $Table$Type} from "packages/com/google/common/collect/$Table"
import {$FastMap, $FastMap$Type} from "packages/malte0811/ferritecore/fastmap/$FastMap"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"

export interface $FastMapStateHolder<S> {

 "getStateIndex"(): integer
 "getStateMap"(): $FastMap<(S)>
 "setStateMap"(arg0: $FastMap$Type<(S)>): void
 "setStateIndex"(arg0: integer): void
 "getNeighborTable"(): $Table<($Property<(any)>), ($Comparable<(any)>), (S)>
 "setNeighborTable"(arg0: $Table$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>), (S)>): void
 "getVanillaPropertyMap"(): $ImmutableMap<($Property<(any)>), ($Comparable<(any)>)>
 "replacePropertyMap"(arg0: $ImmutableMap$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>): void
}

export namespace $FastMapStateHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMapStateHolder$Type<S> = ($FastMapStateHolder<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMapStateHolder_<S> = $FastMapStateHolder$Type<(S)>;
}}
declare module "packages/malte0811/ferritecore/fastmap/$FastMapKey" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"

export class $FastMapKey<T extends $Comparable<(T)>> {


public "getProperty"(): $Property<(T)>
public "getValue"(arg0: integer): T
public "numValues"(): integer
get "property"(): $Property<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMapKey$Type<T> = ($FastMapKey<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMapKey_<T> = $FastMapKey$Type<(T)>;
}}
declare module "packages/malte0811/ferritecore/mixin/mrl/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/util/$SmallThreadingDetector" {
import {$SmallThreadDetectable, $SmallThreadDetectable$Type} from "packages/malte0811/ferritecore/ducks/$SmallThreadDetectable"

export class $SmallThreadingDetector {

constructor()

public static "release"(arg0: $SmallThreadDetectable$Type): void
public static "acquire"(arg0: $SmallThreadDetectable$Type, arg1: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmallThreadingDetector$Type = ($SmallThreadingDetector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmallThreadingDetector_ = $SmallThreadingDetector$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/table/$FastmapNeighborTable" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Table$Cell, $Table$Cell$Type} from "packages/com/google/common/collect/$Table$Cell"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$NeighborTableBase, $NeighborTableBase$Type} from "packages/malte0811/ferritecore/fastmap/table/$NeighborTableBase"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FastmapNeighborTable<S> extends $NeighborTableBase<(S)> {

constructor(arg0: $FastMapStateHolder$Type<(S)>)

public "get"(arg0: any, arg1: any): S
public "values"(): $Collection<(S)>
public "isEmpty"(): boolean
public "size"(): integer
public "contains"(arg0: any, arg1: any): boolean
public "containsValue"(arg0: any): boolean
public "column"(arg0: $Comparable$Type<(any)>): $Map<($Property<(any)>), (S)>
public "row"(arg0: $Property$Type<(any)>): $Map<($Comparable<(any)>), (S)>
public "containsColumn"(arg0: any): boolean
public "cellSet"(): $Set<($Table$Cell<($Property<(any)>), ($Comparable<(any)>), (S)>)>
public "containsRow"(arg0: any): boolean
public "rowMap"(): $Map<($Property<(any)>), ($Map<($Comparable<(any)>), (S)>)>
public "columnKeySet"(): $Set<($Comparable<(any)>)>
public "rowKeySet"(): $Set<($Property<(any)>)>
public "columnMap"(): $Map<($Comparable<(any)>), ($Map<($Property<(any)>), (S)>)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastmapNeighborTable$Type<S> = ($FastmapNeighborTable<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastmapNeighborTable_<S> = $FastmapNeighborTable$Type<(S)>;
}}
declare module "packages/malte0811/ferritecore/mixin/mrl/$ResourceLocationAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ResourceLocationAccess {

 "setNamespace"(arg0: string): void
 "setPath"(arg0: string): void
}

export namespace $ResourceLocationAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceLocationAccess$Type = ($ResourceLocationAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceLocationAccess_ = $ResourceLocationAccess$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$BitSetDVSAccess" {
import {$DiscreteVSAccess, $DiscreteVSAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$DiscreteVSAccess"
import {$BitSet, $BitSet$Type} from "packages/java/util/$BitSet"

export interface $BitSetDVSAccess extends $DiscreteVSAccess {

 "getYMin"(): integer
 "getStorage"(): $BitSet
 "getXMin"(): integer
 "getZMin"(): integer
 "getZMax"(): integer
 "getXMax"(): integer
 "getYMax"(): integer
 "getZSize"(): integer
 "getXSize"(): integer
 "getYSize"(): integer
}

export namespace $BitSetDVSAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BitSetDVSAccess$Type = ($BitSetDVSAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BitSetDVSAccess_ = $BitSetDVSAccess$Type;
}}
declare module "packages/malte0811/ferritecore/$ModMainForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModMainForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModMainForge$Type = ($ModMainForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMainForge_ = $ModMainForge$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/$CompactFastMapKey" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$FastMapKey, $FastMapKey$Type} from "packages/malte0811/ferritecore/fastmap/$FastMapKey"

export class $CompactFastMapKey<T extends $Comparable<(T)>> extends $FastMapKey<(T)> {


public "getValue"(arg0: integer): T
public "replaceIn"(arg0: integer, arg1: T): integer
public "getFactorToNext"(): integer
public "toPartialMapIndex"(arg0: $Comparable$Type<(any)>): integer
get "factorToNext"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactFastMapKey$Type<T> = ($CompactFastMapKey<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactFastMapKey_<T> = $CompactFastMapKey$Type<(T)>;
}}
declare module "packages/malte0811/ferritecore/ducks/$SmallThreadDetectable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SmallThreadDetectable {

 "ferritecore$getState"(): byte
 "ferritecore$setState"(arg0: byte): void
}

export namespace $SmallThreadDetectable {
const UNLOCKED: byte
const LOCKED: byte
const CRASHING: byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmallThreadDetectable$Type = ($SmallThreadDetectable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmallThreadDetectable_ = $SmallThreadDetectable$Type;
}}
declare module "packages/malte0811/ferritecore/hash/$VoxelShapeHash" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$VoxelShapeAccess, $VoxelShapeAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$VoxelShapeAccess"
import {$Hash$Strategy, $Hash$Strategy$Type} from "packages/it/unimi/dsi/fastutil/$Hash$Strategy"

export class $VoxelShapeHash implements $Hash$Strategy<($VoxelShape)> {
static readonly "INSTANCE": $VoxelShapeHash

constructor()

public "equals"(arg0: $VoxelShape$Type, arg1: $VoxelShape$Type): boolean
public "equals"(arg0: $VoxelShapeAccess$Type, arg1: $VoxelShapeAccess$Type): boolean
public "hashCode"(arg0: $VoxelShapeAccess$Type): integer
public "hashCode"(arg0: $VoxelShape$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeHash$Type = ($VoxelShapeHash);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeHash_ = $VoxelShapeHash$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/$PropertyIndexer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"

export class $PropertyIndexer<T extends $Comparable<(T)>> {


public "getProperty"(): $Property<(T)>
public "toIndex"(arg0: T): integer
public "numValues"(): integer
public static "makeIndexer"<T extends $Comparable<(T)>>(arg0: $Property$Type<(T)>): $PropertyIndexer<(T)>
public "byIndex"(arg0: integer): T
get "property"(): $Property<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyIndexer$Type<T> = ($PropertyIndexer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyIndexer_<T> = $PropertyIndexer$Type<(T)>;
}}
declare module "packages/malte0811/ferritecore/mixin/threaddetec/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $FerriteMixinConfig implements $IMixinConfigPlugin {


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
export type $FerriteMixinConfig$Type = ($FerriteMixinConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FerriteMixinConfig_ = $FerriteMixinConfig$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/immutable/$FastMapEntryEntrySet" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$FerriteCoreEntrySetAccess, $FerriteCoreEntrySetAccess$Type} from "packages/com/google/common/collect/$FerriteCoreEntrySetAccess"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"

export class $FastMapEntryEntrySet extends $FerriteCoreEntrySetAccess<($Property<(any)>), ($Comparable<(any)>)> {

constructor(arg0: $FastMapStateHolder$Type<(any)>)

public "size"(): integer
public "contains"(arg0: any): boolean
public "isPartialView"(): boolean
public "add"(arg0: E): boolean
public "remove"(arg0: any): boolean
public "clear"(): void
public "isEmpty"(): boolean
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "partialView"(): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMapEntryEntrySet$Type = ($FastMapEntryEntrySet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMapEntryEntrySet_ = $FastMapEntryEntrySet$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$ArrayVSAccess" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$VoxelShapeAccess, $VoxelShapeAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$VoxelShapeAccess"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"

export interface $ArrayVSAccess extends $VoxelShapeAccess {

 "setXPoints"(arg0: $DoubleList$Type): void
 "setZPoints"(arg0: $DoubleList$Type): void
 "setYPoints"(arg0: $DoubleList$Type): void
 "getXPoints"(): $DoubleList
 "getZPoints"(): $DoubleList
 "getYPoints"(): $DoubleList
 "getShape"(): $DiscreteVoxelShape
 "setShape"(arg0: $DiscreteVoxelShape$Type): void
 "setFaces"(arg0: ($VoxelShape$Type)[]): void
 "getFaces"(): ($VoxelShape)[]
}

export namespace $ArrayVSAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayVSAccess$Type = ($ArrayVSAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayVSAccess_ = $ArrayVSAccess$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$DiscreteVSAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DiscreteVSAccess {

 "getZSize"(): integer
 "getXSize"(): integer
 "getYSize"(): integer
}

export namespace $DiscreteVSAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DiscreteVSAccess$Type = ($DiscreteVSAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DiscreteVSAccess_ = $DiscreteVSAccess$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/table/$CrashNeighborTable" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Table$Cell, $Table$Cell$Type} from "packages/com/google/common/collect/$Table$Cell"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$NeighborTableBase, $NeighborTableBase$Type} from "packages/malte0811/ferritecore/fastmap/table/$NeighborTableBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrashNeighborTable<S> extends $NeighborTableBase<(S)> {


public "get"(arg0: any, arg1: any): S
public "values"(): $Collection<(S)>
public "isEmpty"(): boolean
public "size"(): integer
public "contains"(arg0: any, arg1: any): boolean
public static "getInstance"<S>(): $CrashNeighborTable<(S)>
public "containsValue"(arg0: any): boolean
public "column"(arg0: $Comparable$Type<(any)>): $Map<($Property<(any)>), (S)>
public "row"(arg0: $Property$Type<(any)>): $Map<($Comparable<(any)>), (S)>
public "containsColumn"(arg0: any): boolean
public "cellSet"(): $Set<($Table$Cell<($Property<(any)>), ($Comparable<(any)>), (S)>)>
public "containsRow"(arg0: any): boolean
public "rowMap"(): $Map<($Property<(any)>), ($Map<($Comparable<(any)>), (S)>)>
public "columnKeySet"(): $Set<($Comparable<(any)>)>
public "rowKeySet"(): $Set<($Property<(any)>)>
public "columnMap"(): $Map<($Comparable<(any)>), ($Map<($Property<(any)>), (S)>)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "empty"(): boolean
get "instance"(): $CrashNeighborTable<(S)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrashNeighborTable$Type<S> = ($CrashNeighborTable<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrashNeighborTable_<S> = $CrashNeighborTable$Type<(S)>;
}}
declare module "packages/malte0811/ferritecore/$PlatformHooks" {
import {$IPlatformHooks, $IPlatformHooks$Type} from "packages/malte0811/ferritecore/$IPlatformHooks"

export class $PlatformHooks implements $IPlatformHooks {

constructor()

public "computeBlockstateCacheFieldName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformHooks$Type = ($PlatformHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformHooks_ = $PlatformHooks$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$VoxelShapeAccess" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"

export interface $VoxelShapeAccess {

 "getShape"(): $DiscreteVoxelShape
 "setShape"(arg0: $DiscreteVoxelShape$Type): void
 "setFaces"(arg0: ($VoxelShape$Type)[]): void
 "getFaces"(): ($VoxelShape)[]
}

export namespace $VoxelShapeAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeAccess$Type = ($VoxelShapeAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeAccess_ = $VoxelShapeAccess$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/table/$NeighborTableBase" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Table, $Table$Type} from "packages/com/google/common/collect/$Table"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Table$Cell, $Table$Cell$Type} from "packages/com/google/common/collect/$Table$Cell"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $NeighborTableBase<S> implements $Table<($Property<(any)>), ($Comparable<(any)>), (S)> {

constructor()

public "remove"(arg0: any, arg1: any): S
public "put"(arg0: $Property$Type<(any)>, arg1: $Comparable$Type<(any)>, arg2: S): S
public "clear"(): void
public "putAll"(arg0: $Table$Type<(any), (any), (any)>): void
public "get"(arg0: any, arg1: any): S
public "equals"(arg0: any): boolean
public "values"(): $Collection<(S)>
public "hashCode"(): integer
public "isEmpty"(): boolean
public "size"(): integer
public "contains"(arg0: any, arg1: any): boolean
public "containsValue"(arg0: any): boolean
public "column"(arg0: $Comparable$Type<(any)>): $Map<($Property<(any)>), (S)>
public "row"(arg0: $Property$Type<(any)>): $Map<($Comparable<(any)>), (S)>
public "containsColumn"(arg0: any): boolean
public "cellSet"(): $Set<($Table$Cell<($Property<(any)>), ($Comparable<(any)>), (S)>)>
public "containsRow"(arg0: any): boolean
public "rowMap"(): $Map<($Property<(any)>), ($Map<($Comparable<(any)>), (S)>)>
public "columnKeySet"(): $Set<($Comparable<(any)>)>
public "rowKeySet"(): $Set<($Property<(any)>)>
public "columnMap"(): $Map<($Comparable<(any)>), ($Map<($Property<(any)>), (S)>)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NeighborTableBase$Type<S> = ($NeighborTableBase<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NeighborTableBase_<S> = $NeighborTableBase$Type<(S)>;
}}
declare module "packages/malte0811/ferritecore/ducks/$BlockStateCacheAccess" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"

export interface $BlockStateCacheAccess {

 "getCollisionShape"(): $VoxelShape
 "setCollisionShape"(arg0: $VoxelShape$Type): void
 "getFaceSturdy"(): (boolean)[]
 "setFaceSturdy"(arg0: (boolean)[]): void
 "getOcclusionShapes"(): ($VoxelShape)[]
 "setOcclusionShapes"(arg0: ($VoxelShape$Type)[]): void
}

export namespace $BlockStateCacheAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateCacheAccess$Type = ($BlockStateCacheAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateCacheAccess_ = $BlockStateCacheAccess$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/dedupbakedquad/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/util/$PredicateHelper" {
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $PredicateHelper {

constructor()

public static "or"<T>(arg0: $List$Type<($Predicate$Type<(T)>)>): $Predicate<(T)>
public static "and"<T>(arg0: $List$Type<($Predicate$Type<(T)>)>): $Predicate<(T)>
public static "canonize"<T>(arg0: $List$Type<($Predicate$Type<(T)>)>): void
public static "toCanonicalList"(arg0: $Iterable$Type<(any)>, arg1: $StateDefinition$Type<($Block$Type), ($BlockState$Type)>): $List<($Predicate<($BlockState)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PredicateHelper$Type = ($PredicateHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PredicateHelper_ = $PredicateHelper$Type;
}}
declare module "packages/malte0811/ferritecore/util/$Constants" {
import {$IPlatformHooks, $IPlatformHooks$Type} from "packages/malte0811/ferritecore/$IPlatformHooks"

export class $Constants {
static readonly "MODID": string
static readonly "PLATFORM_HOOKS": $IPlatformHooks

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
declare module "packages/malte0811/ferritecore/classloading/$FastImmutableMapDefiner" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"

export class $FastImmutableMapDefiner {
static "GOOGLE_ACCESS_PREFIX": string
static "GOOGLE_ACCESS_SUFFIX": string

constructor()

public static "makeMap"(arg0: $FastMapStateHolder$Type<(any)>): $ImmutableMap<($Property<(any)>), ($Comparable<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastImmutableMapDefiner$Type = ($FastImmutableMapDefiner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastImmutableMapDefiner_ = $FastImmutableMapDefiner$Type;
}}
declare module "packages/malte0811/ferritecore/impl/$ModelSidesImpl" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModelSidesImpl {

constructor()

public static "minimizeUnculled"(arg0: $List$Type<($BakedQuad$Type)>): $List<($BakedQuad)>
public static "minimizeCulled"(arg0: $Map$Type<($Direction$Type), ($List$Type<($BakedQuad$Type)>)>): $Map<($Direction), ($List<($BakedQuad)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelSidesImpl$Type = ($ModelSidesImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelSidesImpl_ = $ModelSidesImpl$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/config/$FerriteConfig$Option" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"

export class $FerriteConfig$Option {

constructor(arg0: string, arg1: string, arg2: boolean, ...arg3: ($FerriteConfig$Option$Type)[])

public "getName"(): string
public "set"(arg0: $Predicate$Type<(string)>): void
public "getDefaultValue"(): boolean
public "getComment"(): string
public "isEnabled"(): boolean
get "name"(): string
get "defaultValue"(): boolean
get "comment"(): string
get "enabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FerriteConfig$Option$Type = ($FerriteConfig$Option);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FerriteConfig$Option_ = $FerriteConfig$Option$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/dedupbakedquad/$BakedQuadAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BakedQuadAccess {

 "setVertices"(arg0: (integer)[]): void

(arg0: (integer)[]): void
}

export namespace $BakedQuadAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedQuadAccess$Type = ($BakedQuadAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedQuadAccess_ = $BakedQuadAccess$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/dedupmultipart/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/fastmap/immutable/$FastMapEntryImmutableMap" {
import {$ImmutableSet, $ImmutableSet$Type} from "packages/com/google/common/collect/$ImmutableSet"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$FerriteCoreImmutableMapAccess, $FerriteCoreImmutableMapAccess$Type} from "packages/com/google/common/collect/$FerriteCoreImmutableMapAccess"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$ImmutableCollection, $ImmutableCollection$Type} from "packages/com/google/common/collect/$ImmutableCollection"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $FastMapEntryImmutableMap extends $FerriteCoreImmutableMapAccess<($Property<(any)>), ($Comparable<(any)>)> {

constructor(arg0: $FastMapStateHolder$Type<(any)>)

public "size"(): integer
public "isPartialView"(): boolean
public "createEntrySet"(): $ImmutableSet<($Map$Entry<($Property<(any)>), ($Comparable<(any)>)>)>
public "createValues"(): $ImmutableCollection<($Comparable<(any)>)>
public "createKeySet"(): $ImmutableSet<($Property<(any)>)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
get "partialView"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMapEntryImmutableMap$Type = ($FastMapEntryImmutableMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMapEntryImmutableMap_ = $FastMapEntryImmutableMap$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/immutable/$FastMapEntryIterator" {
import {$UnmodifiableIterator, $UnmodifiableIterator$Type} from "packages/com/google/common/collect/$UnmodifiableIterator"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"

export class $FastMapEntryIterator<T> extends $UnmodifiableIterator<(T)> {

constructor(arg0: $FastMapStateHolder$Type<(any)>)

public "hasNext"(): boolean
public "next"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastMapEntryIterator$Type<T> = ($FastMapEntryIterator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastMapEntryIterator_<T> = $FastMapEntryIterator$Type<(T)>;
}}
declare module "packages/malte0811/ferritecore/mixin/platform/$ConfigFileHandler" {
import {$FerriteConfig$Option, $FerriteConfig$Option$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteConfig$Option"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ConfigFileHandler {

constructor()

public static "finish"(arg0: $List$Type<($FerriteConfig$Option$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigFileHandler$Type = ($ConfigFileHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigFileHandler_ = $ConfigFileHandler$Type;
}}
declare module "packages/malte0811/ferritecore/impl/$BlockStateCacheImpl" {
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$ArrayVoxelShape, $ArrayVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$ArrayVoxelShape"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ArrayVSAccess, $ArrayVSAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$ArrayVSAccess"
import {$BlockBehaviour$BlockStateBase, $BlockBehaviour$BlockStateBase$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$BlockStateBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BlockStateCacheImpl {
static readonly "CACHE_COLLIDE": $Map<($ArrayVSAccess), ($ArrayVSAccess)>
static readonly "CACHE_PROJECT": $Map<($VoxelShape), ($Pair<($VoxelShape), (($VoxelShape)[])>)>
static readonly "CACHE_FACE_STURDY": $Map<((boolean)[]), ((boolean)[])>

constructor()

public static "deduplicateCachePre"(arg0: $BlockBehaviour$BlockStateBase$Type): void
public static "deduplicateCachePost"(arg0: $BlockBehaviour$BlockStateBase$Type): void
public static "replaceInternals"(arg0: $ArrayVoxelShape$Type, arg1: $ArrayVoxelShape$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateCacheImpl$Type = ($BlockStateCacheImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateCacheImpl_ = $BlockStateCacheImpl$Type;
}}
declare module "packages/malte0811/ferritecore/hash/$DiscreteVSHash" {
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$DiscreteVSAccess, $DiscreteVSAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$DiscreteVSAccess"
import {$Hash$Strategy, $Hash$Strategy$Type} from "packages/it/unimi/dsi/fastutil/$Hash$Strategy"

export class $DiscreteVSHash implements $Hash$Strategy<($DiscreteVoxelShape)> {
static readonly "INSTANCE": $DiscreteVSHash

constructor()

public "equals"(arg0: $DiscreteVSAccess$Type, arg1: $DiscreteVSAccess$Type): boolean
public "equals"(arg0: $DiscreteVoxelShape$Type, arg1: $DiscreteVoxelShape$Type): boolean
public "hashCode"(arg0: $DiscreteVSAccess$Type): integer
public "hashCode"(arg0: $DiscreteVoxelShape$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DiscreteVSHash$Type = ($DiscreteVSHash);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DiscreteVSHash_ = $DiscreteVSHash$Type;
}}
declare module "packages/malte0811/ferritecore/$ModClientForge" {
import {$RenderLevelStageEvent$RegisterStageEvent, $RenderLevelStageEvent$RegisterStageEvent$Type} from "packages/net/minecraftforge/client/event/$RenderLevelStageEvent$RegisterStageEvent"

export class $ModClientForge {

constructor()

public static "registerReloadListener"(arg0: $RenderLevelStageEvent$RegisterStageEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModClientForge$Type = ($ModClientForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModClientForge_ = $ModClientForge$Type;
}}
declare module "packages/malte0811/ferritecore/mixin/predicates/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/mixin/blockstatecache/$SliceShapeAccess" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$VoxelShapeAccess, $VoxelShapeAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$VoxelShapeAccess"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"

export interface $SliceShapeAccess extends $VoxelShapeAccess {

 "getDelegate"(): $VoxelShape
 "getAxis"(): $Direction$Axis
 "getShape"(): $DiscreteVoxelShape
 "setShape"(arg0: $DiscreteVoxelShape$Type): void
 "setFaces"(arg0: ($VoxelShape$Type)[]): void
 "getFaces"(): ($VoxelShape)[]
}

export namespace $SliceShapeAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SliceShapeAccess$Type = ($SliceShapeAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SliceShapeAccess_ = $SliceShapeAccess$Type;
}}
declare module "packages/malte0811/ferritecore/impl/$KeyValueConditionImpl" {
import {$Splitter, $Splitter$Type} from "packages/com/google/common/base/$Splitter"
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $KeyValueConditionImpl {

constructor()

public static "getPredicate"(arg0: $StateDefinition$Type<($Block$Type), ($BlockState$Type)>, arg1: string, arg2: string, arg3: $Splitter$Type): $Predicate<($BlockState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyValueConditionImpl$Type = ($KeyValueConditionImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyValueConditionImpl_ = $KeyValueConditionImpl$Type;
}}
declare module "packages/malte0811/ferritecore/fastmap/$BinaryFastMapKey" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$FastMapKey, $FastMapKey$Type} from "packages/malte0811/ferritecore/fastmap/$FastMapKey"

export class $BinaryFastMapKey<T extends $Comparable<(T)>> extends $FastMapKey<(T)> {

constructor(arg0: $Property$Type<(T)>, arg1: integer)

public "getValue"(arg0: integer): T
public "replaceIn"(arg0: integer, arg1: T): integer
public "getFactorToNext"(): integer
public "toPartialMapIndex"(arg0: $Comparable$Type<(any)>): integer
get "factorToNext"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BinaryFastMapKey$Type<T> = ($BinaryFastMapKey<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BinaryFastMapKey_<T> = $BinaryFastMapKey$Type<(T)>;
}}
declare module "packages/malte0811/ferritecore/mixin/fastmap/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/mixin/modelsides/$Config" {
import {$FerriteMixinConfig, $FerriteMixinConfig$Type} from "packages/malte0811/ferritecore/mixin/config/$FerriteMixinConfig"

export class $Config extends $FerriteMixinConfig {

constructor()

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
declare module "packages/malte0811/ferritecore/impl/$Deduplicator" {
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$MultiPartBakedModel, $MultiPartBakedModel$Type} from "packages/net/minecraft/client/resources/model/$MultiPartBakedModel"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $Deduplicator {

constructor()

public static "or"(arg0: $List$Type<($Predicate$Type<($BlockState$Type)>)>): $Predicate<($BlockState)>
public static "and"(arg0: $List$Type<($Predicate$Type<($BlockState$Type)>)>): $Predicate<($BlockState)>
public static "deduplicateVariant"(arg0: string): string
public static "registerReloadListener"(): void
public static "makeMultipartModel"(arg0: $List$Type<($Pair$Type<($Predicate$Type<($BlockState$Type)>), ($BakedModel$Type)>)>): $MultiPartBakedModel
public static "deduplicate"(arg0: $BakedQuad$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Deduplicator$Type = ($Deduplicator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Deduplicator_ = $Deduplicator$Type;
}}
declare module "packages/malte0811/ferritecore/impl/$StateHolderImpl" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$FastMap, $FastMap$Type} from "packages/malte0811/ferritecore/fastmap/$FastMap"
import {$ThreadLocal, $ThreadLocal$Type} from "packages/java/lang/$ThreadLocal"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$FastMapStateHolder, $FastMapStateHolder$Type} from "packages/malte0811/ferritecore/ducks/$FastMapStateHolder"

export class $StateHolderImpl {
static readonly "LAST_STATE_MAP": $ThreadLocal<($Map<($Map<($Property<(any)>), ($Comparable<(any)>)>), (any)>)>
static readonly "LAST_FAST_STATE_MAP": $ThreadLocal<($FastMap<(any)>)>

constructor()

public static "populateNeighbors"<S>(arg0: $Map$Type<($Map$Type<($Property$Type<(any)>), ($Comparable$Type<(any)>)>), (S)>, arg1: $FastMapStateHolder$Type<(S)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StateHolderImpl$Type = ($StateHolderImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StateHolderImpl_ = $StateHolderImpl$Type;
}}
declare module "packages/malte0811/ferritecore/hash/$SliceShapeHash" {
import {$SliceShapeAccess, $SliceShapeAccess$Type} from "packages/malte0811/ferritecore/mixin/blockstatecache/$SliceShapeAccess"
import {$Hash$Strategy, $Hash$Strategy$Type} from "packages/it/unimi/dsi/fastutil/$Hash$Strategy"

export class $SliceShapeHash implements $Hash$Strategy<($SliceShapeAccess)> {
static readonly "INSTANCE": $SliceShapeHash

constructor()

public "equals"(arg0: $SliceShapeAccess$Type, arg1: $SliceShapeAccess$Type): boolean
public "hashCode"(arg0: $SliceShapeAccess$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SliceShapeHash$Type = ($SliceShapeHash);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SliceShapeHash_ = $SliceShapeHash$Type;
}}
declare module "packages/malte0811/ferritecore/hash/$LambdaBasedHash" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$Hash$Strategy, $Hash$Strategy$Type} from "packages/it/unimi/dsi/fastutil/$Hash$Strategy"

export class $LambdaBasedHash<T> implements $Hash$Strategy<(T)> {

constructor(arg0: $ToIntFunction$Type<(T)>, arg1: $BiPredicate$Type<(T), (T)>)

public "equals"(arg0: T, arg1: T): boolean
public "hashCode"(arg0: T): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LambdaBasedHash$Type<T> = ($LambdaBasedHash<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LambdaBasedHash_<T> = $LambdaBasedHash$Type<(T)>;
}}
