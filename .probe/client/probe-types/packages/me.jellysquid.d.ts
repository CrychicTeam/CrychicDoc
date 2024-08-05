declare module "packages/me/jellysquid/mods/lithium/common/world/$ClimbingMobCachingSection" {
import {$AbortableIterationConsumer$Continuation, $AbortableIterationConsumer$Continuation$Type} from "packages/net/minecraft/util/$AbortableIterationConsumer$Continuation"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EntityPushablePredicate, $EntityPushablePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/entity/pushable/$EntityPushablePredicate"
import {$BlockCachingEntity, $BlockCachingEntity$Type} from "packages/me/jellysquid/mods/lithium/common/entity/pushable/$BlockCachingEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ClimbingMobCachingSection {

 "collectPushableEntities"(arg0: $Level$Type, arg1: $Entity$Type, arg2: $AABB$Type, arg3: $EntityPushablePredicate$Type<(any)>, arg4: $ArrayList$Type<($Entity$Type)>): $AbortableIterationConsumer$Continuation
 "onEntityModifiedCachedBlock"(arg0: $BlockCachingEntity$Type, arg1: $BlockState$Type): void
}

export namespace $ClimbingMobCachingSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClimbingMobCachingSection$Type = ($ClimbingMobCachingSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClimbingMobCachingSection_ = $ClimbingMobCachingSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipelineProvider" {
import {$LightPipeline, $LightPipeline$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipeline"
import {$LightMode, $LightMode$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightMode"
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"

export class $LightPipelineProvider {

constructor(arg0: $LightDataAccess$Type)

public "reset"(): void
public "getLightData"(): $LightDataAccess
public "getLighter"(arg0: $LightMode$Type): $LightPipeline
get "lightData"(): $LightDataAccess
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightPipelineProvider$Type = ($LightPipelineProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightPipelineProvider_ = $LightPipelineProvider$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/smooth/$AoNeighborInfo" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $AoNeighborInfo extends $Enum<($AoNeighborInfo)> {
static readonly "DOWN": $AoNeighborInfo
static readonly "UP": $AoNeighborInfo
static readonly "NORTH": $AoNeighborInfo
static readonly "SOUTH": $AoNeighborInfo
static readonly "WEST": $AoNeighborInfo
static readonly "EAST": $AoNeighborInfo
readonly "faces": ($Direction)[]
readonly "strength": float


public static "get"(arg0: $Direction$Type): $AoNeighborInfo
public static "values"(): ($AoNeighborInfo)[]
public static "valueOf"(arg0: string): $AoNeighborInfo
public "getDepth"(arg0: float, arg1: float, arg2: float): float
public "mapCorners"(arg0: (integer)[], arg1: (float)[], arg2: (integer)[], arg3: (float)[]): void
public "calculateCornerWeights"(arg0: float, arg1: float, arg2: float, arg3: (float)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AoNeighborInfo$Type = (("east") | ("south") | ("north") | ("west") | ("up") | ("down")) | ($AoNeighborInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AoNeighborInfo_ = $AoNeighborInfo$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/data/$HashLightDataCache" {
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $HashLightDataCache extends $LightDataAccess {

constructor(arg0: $BlockAndTintGetter$Type)

public "get"(arg0: integer, arg1: integer, arg2: integer): integer
public "clearCache"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HashLightDataCache$Type = ($HashLightDataCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HashLightDataCache_ = $HashLightDataCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeColorView" {
import {$BiomeColorSource, $BiomeColorSource$Type} from "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeColorSource"

export interface $BiomeColorView {

 "getColor"(arg0: $BiomeColorSource$Type, arg1: integer, arg2: integer, arg3: integer): integer

(arg0: $BiomeColorSource$Type, arg1: integer, arg2: integer, arg3: integer): integer
}

export namespace $BiomeColorView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeColorView$Type = ($BiomeColorView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeColorView_ = $BiomeColorView$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeColorCache" {
import {$ChunkRenderContext, $ChunkRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/world/cloned/$ChunkRenderContext"
import {$BiomeColorSource, $BiomeColorSource$Type} from "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeColorSource"
import {$BiomeSlice, $BiomeSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeSlice"
import {$ColorResolver, $ColorResolver$Type} from "packages/net/minecraft/world/level/$ColorResolver"

export class $BiomeColorCache {

constructor(arg0: $BiomeSlice$Type, arg1: integer)

public "update"(arg0: $ChunkRenderContext$Type): void
public "getColor"(arg0: $ColorResolver$Type, arg1: integer, arg2: integer, arg3: integer): integer
public "getColor"(arg0: $BiomeColorSource$Type, arg1: integer, arg2: integer, arg3: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeColorCache$Type = ($BiomeColorCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeColorCache_ = $BiomeColorCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/quirks/$QuirkManager" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $QuirkManager {
static readonly "REBIND_LIGHTMAP_TEXTURE": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuirkManager$Type = ($QuirkManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuirkManager_ = $QuirkManager$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$SortedRenderLists" {
import {$ChunkRenderList, $ChunkRenderList$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderList"
import {$ReversibleObjectArrayIterator, $ReversibleObjectArrayIterator$Type} from "packages/me/jellysquid/mods/sodium/client/util/iterator/$ReversibleObjectArrayIterator"
import {$ChunkRenderListIterable, $ChunkRenderListIterable$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderListIterable"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $SortedRenderLists implements $ChunkRenderListIterable {


public "iterator"(arg0: boolean): $ReversibleObjectArrayIterator<($ChunkRenderList)>
public static "empty"(): $SortedRenderLists
public "iterator"(): $Iterator<($ChunkRenderList)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortedRenderLists$Type = ($SortedRenderLists);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortedRenderLists_ = $SortedRenderLists$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$GraphDirectionSet" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GraphDirectionSet {
static readonly "NONE": integer
static readonly "ALL": integer

constructor()

public static "of"(arg0: integer): integer
public static "contains"(arg0: integer, arg1: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphDirectionSet$Type = ($GraphDirectionSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphDirectionSet_ = $GraphDirectionSet$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$OcclusionCuller$Visitor" {
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"

export interface $OcclusionCuller$Visitor {

 "visit"(arg0: $RenderSection$Type, arg1: boolean): void

(arg0: $RenderSection$Type, arg1: boolean): void
}

export namespace $OcclusionCuller$Visitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OcclusionCuller$Visitor$Type = ($OcclusionCuller$Visitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OcclusionCuller$Visitor_ = $OcclusionCuller$Visitor$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/chunk/$ChunkHolderExtended" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ChunkHolder$ChunkLoadingFailure, $ChunkHolder$ChunkLoadingFailure$Type} from "packages/net/minecraft/server/level/$ChunkHolder$ChunkLoadingFailure"

export interface $ChunkHolderExtended {

 "setFutureForStatus"(arg0: integer, arg1: $CompletableFuture$Type<($Either$Type<($ChunkAccess$Type), ($ChunkHolder$ChunkLoadingFailure$Type)>)>): void
 "getFutureByStatus"(arg0: integer): $CompletableFuture<($Either<($ChunkAccess), ($ChunkHolder$ChunkLoadingFailure)>)>
 "getCurrentlyLoading"(): $LevelChunk
 "updateLastAccessTime"(arg0: long): boolean
}

export namespace $ChunkHolderExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkHolderExtended$Type = ($ChunkHolderExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkHolderExtended_ = $ChunkHolderExtended$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/tuples/$SortedPointOfInterest" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PoiRecord, $PoiRecord$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiRecord"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $SortedPointOfInterest extends $Record {

constructor(poi: $PoiRecord$Type, origin: $BlockPos$Type)
constructor(poi: $PoiRecord$Type, distanceSq: double)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "poi"(): $PoiRecord
public "getY"(): integer
public "getZ"(): integer
public "getX"(): integer
public "getPos"(): $BlockPos
public "distanceSq"(): double
get "y"(): integer
get "z"(): integer
get "x"(): integer
get "pos"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortedPointOfInterest$Type = ($SortedPointOfInterest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortedPointOfInterest_ = $SortedPointOfInterest$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/impl/$CompactChunkVertex" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$ChunkMeshAttribute, $ChunkMeshAttribute$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkMeshAttribute"
import {$GlVertexFormat, $GlVertexFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat"
import {$ChunkVertexEncoder, $ChunkVertexEncoder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder"

export class $CompactChunkVertex implements $ChunkVertexType {
static readonly "VERTEX_FORMAT": $GlVertexFormat<($ChunkMeshAttribute)>
static readonly "STRIDE": integer

constructor()

public "getEncoder"(): $ChunkVertexEncoder
public "getPositionOffset"(): float
public static "decodePosition"(arg0: short): float
public "getPositionScale"(): float
public "getTextureScale"(): float
public "getVertexFormat"(): $GlVertexFormat<($ChunkMeshAttribute)>
get "encoder"(): $ChunkVertexEncoder
get "positionOffset"(): float
get "positionScale"(): float
get "textureScale"(): float
get "vertexFormat"(): $GlVertexFormat<($ChunkMeshAttribute)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactChunkVertex$Type = ($CompactChunkVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactChunkVertex_ = $CompactChunkVertex$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/iterator/$ReversibleByteArrayIterator" {
import {$ByteIterator, $ByteIterator$Type} from "packages/me/jellysquid/mods/sodium/client/util/iterator/$ByteIterator"

export class $ReversibleByteArrayIterator implements $ByteIterator {

constructor(arg0: (byte)[], arg1: integer, arg2: boolean)

public "hasNext"(): boolean
public "nextByteAsInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReversibleByteArrayIterator$Type = ($ReversibleByteArrayIterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReversibleByteArrayIterator_ = $ReversibleByteArrayIterator$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/iterator/$ReversibleObjectArrayIterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$ObjectArrayList, $ObjectArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectArrayList"

export class $ReversibleObjectArrayIterator<T> implements $Iterator<(T)> {

constructor(arg0: $ObjectArrayList$Type<(T)>, arg1: boolean)
constructor(arg0: (T)[], arg1: integer, arg2: integer, arg3: boolean)

public "hasNext"(): boolean
public "next"(): T
public "remove"(): void
public "forEachRemaining"(arg0: $Consumer$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReversibleObjectArrayIterator$Type<T> = ($ReversibleObjectArrayIterator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReversibleObjectArrayIterator_<T> = $ReversibleObjectArrayIterator$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/$PendingUpload" {
import {$NativeBuffer, $NativeBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/util/$NativeBuffer"
import {$GlBufferSegment, $GlBufferSegment$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferSegment"

export class $PendingUpload {

constructor(arg0: $NativeBuffer$Type)

public "getLength"(): integer
public "getResult"(): $GlBufferSegment
public "getDataBuffer"(): $NativeBuffer
get "length"(): integer
get "result"(): $GlBufferSegment
get "dataBuffer"(): $NativeBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PendingUpload$Type = ($PendingUpload);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PendingUpload_ = $PendingUpload$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProviderRegistry" {
import {$BlockColors, $BlockColors$Type} from "packages/net/minecraft/client/color/block/$BlockColors"
import {$ColorProvider, $ColorProvider$Type} from "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProvider"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $ColorProviderRegistry {

constructor(arg0: $BlockColors$Type)

public "getColorProvider"(arg0: $Block$Type): $ColorProvider<($BlockState)>
public "getColorProvider"(arg0: $Fluid$Type): $ColorProvider<($FluidState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorProviderRegistry$Type = ($ColorProviderRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorProviderRegistry_ = $ColorProviderRegistry$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/parameters/$MaterialParameters" {
import {$AlphaCutoffParameter, $AlphaCutoffParameter$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/parameters/$AlphaCutoffParameter"

export class $MaterialParameters {
static readonly "OFFSET_USE_MIP": integer
static readonly "OFFSET_ALPHA_CUTOFF": integer

constructor()

public static "pack"(arg0: $AlphaCutoffParameter$Type, arg1: boolean): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaterialParameters$Type = ($MaterialParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaterialParameters_ = $MaterialParameters$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/chunk/entity_class_groups/$ServerEntityManagerAccessor" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$EntityAccess, $EntityAccess$Type} from "packages/net/minecraft/world/level/entity/$EntityAccess"

export interface $ServerEntityManagerAccessor<T extends $EntityAccess> {

 "getCache"(): $EntitySectionStorage<(T)>

(): $EntitySectionStorage<(T)>
}

export namespace $ServerEntityManagerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityManagerAccessor$Type<T> = ($ServerEntityManagerAccessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityManagerAccessor_<T> = $ServerEntityManagerAccessor$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuad" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ModelQuadViewMutable, $ModelQuadViewMutable$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadViewMutable"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ModelQuad implements $ModelQuadViewMutable {

constructor()

public "setColor"(arg0: integer, arg1: integer): void
public "getFlags"(): integer
public "setFlags"(arg0: integer): void
public "getY"(arg0: integer): float
public "getColorIndex"(): integer
public "getSprite"(): $TextureAtlasSprite
public "getLightFace"(): $Direction
public "getForgeNormal"(arg0: integer): integer
public "getLight"(arg0: integer): integer
public "getTexV"(arg0: integer): float
public "getTexU"(arg0: integer): float
public "hasAmbientOcclusion"(): boolean
public "setX"(arg0: integer, arg1: float): void
public "getZ"(arg0: integer): float
public "setY"(arg0: integer, arg1: float): void
public "setZ"(arg0: integer, arg1: float): void
public "getX"(arg0: integer): float
public "getColor"(arg0: integer): integer
public "setLightFace"(arg0: $Direction$Type): void
public "setTexU"(arg0: integer, arg1: float): void
public "setTexV"(arg0: integer, arg1: float): void
public "setColorIndex"(arg0: integer): void
public "setLight"(arg0: integer, arg1: integer): void
public "setHasAmbientOcclusion"(arg0: boolean): void
public "setSprite"(arg0: $TextureAtlasSprite$Type): void
public "hasColor"(): boolean
get "flags"(): integer
set "flags"(value: integer)
get "colorIndex"(): integer
get "sprite"(): $TextureAtlasSprite
get "lightFace"(): $Direction
set "lightFace"(value: $Direction$Type)
set "colorIndex"(value: integer)
set "sprite"(value: $TextureAtlasSprite$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuad$Type = ($ModelQuad);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuad_ = $ModelQuad$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/core/render/$VertexFormatAccessor" {
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"

export interface $VertexFormatAccessor {

 "getOffsets"(): $IntList

(): $IntList
}

export namespace $VertexFormatAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexFormatAccessor$Type = ($VertexFormatAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexFormatAccessor_ = $VertexFormatAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformMatrix4f" {
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export class $GlUniformMatrix4f extends $GlUniform<($Matrix4fc)> {

constructor(arg0: integer)

public "set"(arg0: $Matrix4fc$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniformMatrix4f$Type = ($GlUniformMatrix4f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniformMatrix4f_ = $GlUniformMatrix4f$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderParser" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ShaderConstants, $ShaderConstants$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants"

export class $ShaderParser {

constructor()

public static "parseShader"(arg0: string, arg1: $ShaderConstants$Type): string
public static "parseShader"(arg0: string): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderParser$Type = ($ShaderParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderParser_ = $ShaderParser$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$SectionRenderDataStorage" {
import {$VertexRange, $VertexRange$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$VertexRange"
import {$GlBufferSegment, $GlBufferSegment$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferSegment"

export class $SectionRenderDataStorage {

constructor()

public "onBufferResized"(): void
public "delete"(): void
public "removeMeshes"(arg0: integer): void
public "replaceIndexBuffer"(arg0: integer, arg1: $GlBufferSegment$Type): void
public "removeIndexBuffer"(arg0: integer): void
public "setMeshes"(arg0: integer, arg1: $GlBufferSegment$Type, arg2: $GlBufferSegment$Type, arg3: ($VertexRange$Type)[]): void
public "getDataPointer"(arg0: integer): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionRenderDataStorage$Type = ($SectionRenderDataStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionRenderDataStorage_ = $SectionRenderDataStorage$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$ShaderChunkRenderer" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$CameraTransform, $CameraTransform$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$CameraTransform"
import {$ChunkRenderListIterable, $ChunkRenderListIterable$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderListIterable"
import {$ChunkRenderer, $ChunkRenderer$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderer"
import {$RenderDevice, $RenderDevice$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$ChunkRenderMatrices, $ChunkRenderMatrices$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderMatrices"

export class $ShaderChunkRenderer implements $ChunkRenderer {

constructor(arg0: $RenderDevice$Type, arg1: $ChunkVertexType$Type)

public "delete"(arg0: $CommandList$Type): void
public "getVertexType"(): $ChunkVertexType
public "render"(arg0: $ChunkRenderMatrices$Type, arg1: $CommandList$Type, arg2: $ChunkRenderListIterable$Type, arg3: $TerrainRenderPass$Type, arg4: $CameraTransform$Type): void
get "vertexType"(): $ChunkVertexType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderChunkRenderer$Type = ($ShaderChunkRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderChunkRenderer_ = $ShaderChunkRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/$RubidiumStub" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RubidiumStub {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RubidiumStub$Type = ($RubidiumStub);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RubidiumStub_ = $RubidiumStub$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/$SodiumPreLaunch" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SodiumPreLaunch {

constructor()

public static "onPreLaunch"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumPreLaunch$Type = ($SodiumPreLaunch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumPreLaunch_ = $SodiumPreLaunch$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/chunk/heightmap/$CombinedHeightmapUpdate" {
import {$Heightmap, $Heightmap$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $CombinedHeightmapUpdate {

constructor()

public static "updateHeightmaps"(heightmap0: $Heightmap$Type, heightmap1: $Heightmap$Type, heightmap2: $Heightmap$Type, heightmap3: $Heightmap$Type, worldChunk: $LevelChunk$Type, x: integer, y: integer, z: integer, state: $BlockState$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CombinedHeightmapUpdate$Type = ($CombinedHeightmapUpdate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CombinedHeightmapUpdate_ = $CombinedHeightmapUpdate$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/environment/$GLContextInfo" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $GLContextInfo extends $Record {

constructor(vendor: string, renderer: string, version: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): string
public "hashCode"(): integer
public static "create"(): $GLContextInfo
public "renderer"(): string
public "vendor"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLContextInfo$Type = ($GLContextInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLContextInfo_ = $GLContextInfo$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/util/$VertexRange" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $VertexRange extends $Record {

constructor(vertexStart: integer, vertexCount: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "vertexCount"(): integer
public "vertexStart"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexRange$Type = ($VertexRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexRange_ = $VertexRange$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/$SleepingBlockEntity" {
import {$TickingBlockEntity, $TickingBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TickingBlockEntity"

export interface $SleepingBlockEntity {

}

export namespace $SleepingBlockEntity {
const SLEEPING_BLOCK_ENTITY_TICKER: $TickingBlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SleepingBlockEntity$Type = ($SleepingBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SleepingBlockEntity_ = $SleepingBlockEntity$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderMeshingTask" {
import {$ChunkRenderContext, $ChunkRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/world/cloned/$ChunkRenderContext"
import {$ChunkBuildOutput, $ChunkBuildOutput$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildOutput"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ChunkBuilderTask, $ChunkBuilderTask$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderTask"

export class $ChunkBuilderMeshingTask extends $ChunkBuilderTask<($ChunkBuildOutput)> {

constructor(arg0: $RenderSection$Type, arg1: $ChunkRenderContext$Type, arg2: integer)

public "withCameraPosition"(arg0: $Vec3$Type): $ChunkBuilderMeshingTask
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuilderMeshingTask$Type = ($ChunkBuilderMeshingTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuilderMeshingTask_ = $ChunkBuilderMeshingTask$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/api/inventory/$LithiumInventory" {
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

export interface $LithiumInventory extends $Container {

 "setInventoryLithium"(arg0: $NonNullList$Type<($ItemStack$Type)>): void
 "generateLootLithium"(): void
 "getInventoryLithium"(): $NonNullList<($ItemStack)>
 "kjs$self"(): $Container
 "setChanged"(): void
 "getBlock"(level: $Level$Type): $BlockContainerJS
 "getItem"(arg0: integer): $ItemStack
 "getContainerSize"(): integer
 "removeItemNoUpdate"(arg0: integer): $ItemStack
 "removeItem"(arg0: integer, arg1: integer): $ItemStack
 "isEmpty"(): boolean
 "startOpen"(arg0: $Player$Type): void
 "getMaxStackSize"(): integer
 "stillValid"(arg0: $Player$Type): boolean
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
 "setItem"(arg0: integer, arg1: $ItemStack$Type): void
 "clearContent"(): void
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
}

export namespace $LithiumInventory {
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
function tryClear(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumInventory$Type = ($LithiumInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumInventory_ = $LithiumInventory$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/environment/probe/$GraphicsAdapterProbe" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GraphicsAdapterInfo, $GraphicsAdapterInfo$Type} from "packages/me/jellysquid/mods/sodium/client/compatibility/environment/probe/$GraphicsAdapterInfo"

export class $GraphicsAdapterProbe {

constructor()

public static "findAdapters"(): void
public static "getAdapters"(): $Collection<($GraphicsAdapterInfo)>
public static "findAdaptersCrossPlatform"(): $List<($GraphicsAdapterInfo)>
get "adapters"(): $Collection<($GraphicsAdapterInfo)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsAdapterProbe$Type = ($GraphicsAdapterProbe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsAdapterProbe_ = $GraphicsAdapterProbe$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/$BlockStateFlags" {
import {$ListeningBlockStatePredicate, $ListeningBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$ListeningBlockStatePredicate"
import {$TrackedBlockStatePredicate, $TrackedBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$TrackedBlockStatePredicate"

export class $BlockStateFlags {
static readonly "ENABLED": boolean
static readonly "NUM_LISTENING_FLAGS": integer
static readonly "LISTENING_FLAGS": ($ListeningBlockStatePredicate)[]
static readonly "LISTENING_MASK_OR": integer
static readonly "ANY": $ListeningBlockStatePredicate
static readonly "NUM_TRACKED_FLAGS": integer
static readonly "TRACKED_FLAGS": ($TrackedBlockStatePredicate)[]
static readonly "OVERSIZED_SHAPE": $TrackedBlockStatePredicate
static readonly "PATH_NOT_OPEN": $TrackedBlockStatePredicate
static readonly "ANY_FLUID": $TrackedBlockStatePredicate
static readonly "FLAGS": ($TrackedBlockStatePredicate)[]
static readonly "ENTITY_TOUCHABLE": $TrackedBlockStatePredicate

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateFlags$Type = ($BlockStateFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateFlags_ = $BlockStateFlags$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/interests/iterator/$NearbyPointOfInterestStream" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$PoiRecord, $PoiRecord$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiRecord"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$RegionBasedStorageSectionExtended, $RegionBasedStorageSectionExtended$Type} from "packages/me/jellysquid/mods/lithium/common/world/interests/$RegionBasedStorageSectionExtended"
import {$Spliterators$AbstractSpliterator, $Spliterators$AbstractSpliterator$Type} from "packages/java/util/$Spliterators$AbstractSpliterator"
import {$PoiSection, $PoiSection$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiSection"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PoiManager$Occupancy, $PoiManager$Occupancy$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiManager$Occupancy"

export class $NearbyPointOfInterestStream extends $Spliterators$AbstractSpliterator<($PoiRecord)> {

constructor(typeSelector: $Predicate$Type<($Holder$Type<($PoiType$Type)>)>, status: $PoiManager$Occupancy$Type, useSquareDistanceLimit: boolean, preferNegativeY: boolean, afterSortingPredicate: $Predicate$Type<($PoiRecord$Type)>, origin: $BlockPos$Type, radius: integer, storage: $RegionBasedStorageSectionExtended$Type<($PoiSection$Type)>)

public "tryAdvance"(action: $Consumer$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NearbyPointOfInterestStream$Type = ($NearbyPointOfInterestStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NearbyPointOfInterestStream_ = $NearbyPointOfInterestStream$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/device/$MultiDrawBatch" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MultiDrawBatch {
readonly "pElementPointer": long
readonly "pElementCount": long
readonly "pBaseVertex": long
 "size": integer

constructor(arg0: integer)

public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "delete"(): void
public "capacity"(): integer
public "getIndexBufferSize"(): integer
get "empty"(): boolean
get "indexBufferSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiDrawBatch$Type = ($MultiDrawBatch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiDrawBatch_ = $MultiDrawBatch$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/$WorldHelper" {
import {$EntityGetter, $EntityGetter$Type} from "packages/net/minecraft/world/level/$EntityGetter"
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityClassGroup$NoDragonClassGroup, $EntityClassGroup$NoDragonClassGroup$Type} from "packages/me/jellysquid/mods/lithium/common/entity/$EntityClassGroup$NoDragonClassGroup"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EntityPushablePredicate, $EntityPushablePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/entity/pushable/$EntityPushablePredicate"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $WorldHelper {
static readonly "CUSTOM_TYPE_FILTERABLE_LIST_DISABLED": boolean

constructor()

public static "getPushableEntities"(world: $Level$Type, cache: $EntitySectionStorage$Type<($Entity$Type)>, except: $Entity$Type, box: $AABB$Type, entityPushablePredicate: $EntityPushablePredicate$Type<(any)>): $List<($Entity)>
public static "getEntityCacheOrNull"(world: $Level$Type): $EntitySectionStorage<($Entity)>
public static "areNeighborsWithinSameChunkSection"(pos: $BlockPos$Type): boolean
public static "getEntitiesForCollision"(entityView: $EntityGetter$Type, box: $AABB$Type, collidingEntity: $Entity$Type): $List<($Entity)>
public static "getEntitiesOfClassGroup"(cache: $EntitySectionStorage$Type<($Entity$Type)>, collidingEntity: $Entity$Type, entityClassGroup: $EntityClassGroup$NoDragonClassGroup$Type, box: $AABB$Type): $List<($Entity)>
public static "areNeighborsWithinSameChunk"(pos: $BlockPos$Type): boolean
public static "arePosWithinSameChunk"(pos1: $BlockPos$Type, pos2: $BlockPos$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldHelper$Type = ($WorldHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldHelper_ = $WorldHelper$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$UpdateReceiver" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export interface $UpdateReceiver {

 "invalidateCacheOnNeighborUpdate"(arg0: boolean): void
 "invalidateCacheOnNeighborUpdate"(arg0: $Direction$Type): void
}

export namespace $UpdateReceiver {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateReceiver$Type = ($UpdateReceiver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateReceiver_ = $UpdateReceiver$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/texture/$SpriteUtil" {
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $SpriteUtil {

constructor()

public static "hasAnimation"(arg0: $TextureAtlasSprite$Type): boolean
public static "markSpriteActive"(arg0: $TextureAtlasSprite$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteUtil$Type = ($SpriteUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteUtil_ = $SpriteUtil$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlIndexType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GlIndexType extends $Enum<($GlIndexType)> {
static readonly "UNSIGNED_BYTE": $GlIndexType
static readonly "UNSIGNED_SHORT": $GlIndexType
static readonly "UNSIGNED_INT": $GlIndexType


public "getFormatId"(): integer
public static "values"(): ($GlIndexType)[]
public static "valueOf"(arg0: string): $GlIndexType
public "getStride"(): integer
get "formatId"(): integer
get "stride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlIndexType$Type = (("unsigned_short") | ("unsigned_int") | ("unsigned_byte")) | ($GlIndexType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlIndexType_ = $GlIndexType$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumDoubleInventory" {
import {$InventoryChangeEmitter, $InventoryChangeEmitter$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeEmitter"
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$CompoundContainer, $CompoundContainer$Type} from "packages/net/minecraft/world/$CompoundContainer"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LithiumInventory, $LithiumInventory$Type} from "packages/me/jellysquid/mods/lithium/api/inventory/$LithiumInventory"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ComparatorTracker, $ComparatorTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_comparator_tracking/$ComparatorTracker"
import {$InventoryChangeListener, $InventoryChangeListener$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $LithiumDoubleInventory extends $CompoundContainer implements $LithiumInventory, $InventoryChangeTracker, $InventoryChangeEmitter, $InventoryChangeListener, $ComparatorTracker {


public "setInventoryLithium"(inventory: $NonNullList$Type<($ItemStack$Type)>): void
public "getInventoryLithium"(): $NonNullList<($ItemStack)>
public "stopForwardingMajorInventoryChanges"(inventoryChangeListener: $InventoryChangeListener$Type): void
public "hasAnyComparatorNearby"(): boolean
public "emitFirstComparatorAdded"(): void
public static "getLithiumInventory"(doubleInventory: $CompoundContainer$Type): $LithiumDoubleInventory
public "forwardMajorInventoryChanges"(inventoryChangeListener: $InventoryChangeListener$Type): void
public "emitContentModified"(): void
public "emitStackListReplaced"(): void
public "forwardContentChangeOnce"(inventoryChangeListener: $InventoryChangeListener$Type, stackList: $LithiumStackList$Type, thisTracker: $InventoryChangeTracker$Type): void
public "handleInventoryRemoved"(inventory: $Container$Type): void
public "handleInventoryContentModified"(inventory: $Container$Type): void
public "handleComparatorAdded"(inventory: $Container$Type): boolean
public "emitRemoved"(): void
public "onComparatorAdded"(direction: $Direction$Type, offset: integer): void
public "generateLootLithium"(): void
public "listenForMajorInventoryChanges"(inventoryChangeListener: $InventoryChangeListener$Type): void
public "stopListenForMajorInventoryChanges"(inventoryChangeListener: $InventoryChangeListener$Type): void
public "listenForContentChangesOnce"(stackList: $LithiumStackList$Type, inventoryChangeListener: $InventoryChangeListener$Type): void
public "emitCallbackReplaced"(): void
public "handleStackListReplaced"(inventory: $Container$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
set "inventoryLithium"(value: $NonNullList$Type<($ItemStack$Type)>)
get "inventoryLithium"(): $NonNullList<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumDoubleInventory$Type = ($LithiumDoubleInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumDoubleInventory_ = $LithiumDoubleInventory$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$EntityClassGroup$NoDragonClassGroup" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$EntityClassGroup, $EntityClassGroup$Type} from "packages/me/jellysquid/mods/lithium/common/entity/$EntityClassGroup"

export class $EntityClassGroup$NoDragonClassGroup extends $EntityClassGroup {
static readonly "BOAT_SHULKER_LIKE_COLLISION": $EntityClassGroup$NoDragonClassGroup
static readonly "MINECART_BOAT_LIKE_COLLISION": $EntityClassGroup

constructor(classFitEvaluator: $Predicate$Type<($Class$Type<(any)>)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityClassGroup$NoDragonClassGroup$Type = ($EntityClassGroup$NoDragonClassGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityClassGroup$NoDragonClassGroup_ = $EntityClassGroup$NoDragonClassGroup$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/impl/$VanillaLikeChunkVertex" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$ChunkMeshAttribute, $ChunkMeshAttribute$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkMeshAttribute"
import {$GlVertexFormat, $GlVertexFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat"
import {$ChunkVertexEncoder, $ChunkVertexEncoder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder"

export class $VanillaLikeChunkVertex implements $ChunkVertexType {
static readonly "STRIDE": integer
static readonly "VERTEX_FORMAT": $GlVertexFormat<($ChunkMeshAttribute)>

constructor()

public "getEncoder"(): $ChunkVertexEncoder
public "getPositionOffset"(): float
public "getPositionScale"(): float
public "getTextureScale"(): float
public "getVertexFormat"(): $GlVertexFormat<($ChunkMeshAttribute)>
get "encoder"(): $ChunkVertexEncoder
get "positionOffset"(): float
get "positionScale"(): float
get "textureScale"(): float
get "vertexFormat"(): $GlVertexFormat<($ChunkMeshAttribute)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaLikeChunkVertex$Type = ($VanillaLikeChunkVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaLikeChunkVertex_ = $VanillaLikeChunkVertex$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$ListeningLong2ObjectOpenHashMap$Callback" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ListeningLong2ObjectOpenHashMap$Callback<V> {

 "apply"(arg0: long, arg1: V): void

(arg0: long, arg1: V): void
}

export namespace $ListeningLong2ObjectOpenHashMap$Callback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListeningLong2ObjectOpenHashMap$Callback$Type<V> = ($ListeningLong2ObjectOpenHashMap$Callback<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListeningLong2ObjectOpenHashMap$Callback_<V> = $ListeningLong2ObjectOpenHashMap$Callback$Type<(V)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferSegment" {
import {$GlBufferArena, $GlBufferArena$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferArena"

export class $GlBufferSegment {

constructor(arg0: $GlBufferArena$Type, arg1: integer, arg2: integer)

public "getLength"(): integer
public "delete"(): void
public "getOffset"(): integer
get "length"(): integer
get "offset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferSegment$Type = ($GlBufferSegment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferSegment_ = $GlBufferSegment$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformFloat" {
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export class $GlUniformFloat extends $GlUniform<(float)> {

constructor(arg0: integer)

public "set"(arg0: float): void
public "setFloat"(arg0: float): void
set "float"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniformFloat$Type = ($GlUniformFloat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniformFloat_ = $GlUniformFloat$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/screen/$ConfigCorruptedScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ConfigCorruptedScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Supplier$Type<($Screen$Type)>)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCorruptedScreen$Type = ($ConfigCorruptedScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCorruptedScreen_ = $ConfigCorruptedScreen$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$GraphDirection" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $GraphDirection {
static readonly "DOWN": integer
static readonly "UP": integer
static readonly "NORTH": integer
static readonly "SOUTH": integer
static readonly "WEST": integer
static readonly "EAST": integer
static readonly "COUNT": integer

constructor()

public static "toEnum"(arg0: integer): $Direction
public static "x"(arg0: integer): integer
public static "z"(arg0: integer): integer
public static "y"(arg0: integer): integer
public static "opposite"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphDirection$Type = ($GraphDirection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphDirection_ = $GraphDirection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/checks/$ResourcePackScanner" {
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $ResourcePackScanner {

constructor()

public static "checkIfCoreShaderLoaded"(arg0: $ResourceManager$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcePackScanner$Type = ($ResourcePackScanner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcePackScanner_ = $ResourcePackScanner$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/$BiomeSeedProvider" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"

export interface $BiomeSeedProvider {

 "sodium$getBiomeSeed"(): long

(): long
}

export namespace $BiomeSeedProvider {
function getBiomeSeed(arg0: $ClientLevel$Type): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeSeedProvider$Type = ($BiomeSeedProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeSeedProvider_ = $BiomeSeedProvider$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ChunkTracker" {
import {$ClientChunkEventListener, $ClientChunkEventListener$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ClientChunkEventListener"
import {$ChunkTracker$ChunkEventHandler, $ChunkTracker$ChunkEventHandler$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ChunkTracker$ChunkEventHandler"
import {$LongCollection, $LongCollection$Type} from "packages/it/unimi/dsi/fastutil/longs/$LongCollection"

export class $ChunkTracker implements $ClientChunkEventListener {

constructor()

public "updateMapCenter"(arg0: integer, arg1: integer): void
public "updateLoadDistance"(arg0: integer): void
public "onChunkStatusRemoved"(arg0: integer, arg1: integer, arg2: integer): void
public "forEachEvent"(arg0: $ChunkTracker$ChunkEventHandler$Type, arg1: $ChunkTracker$ChunkEventHandler$Type): void
public static "forEachChunk"(arg0: $LongCollection$Type, arg1: $ChunkTracker$ChunkEventHandler$Type): void
public "getReadyChunks"(): $LongCollection
public "onChunkStatusAdded"(arg0: integer, arg1: integer, arg2: integer): void
get "readyChunks"(): $LongCollection
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkTracker$Type = ($ChunkTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkTracker_ = $ChunkTracker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlProgram$Builder" {
import {$GlProgram, $GlProgram$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlProgram"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$GlShader, $GlShader$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlShader"
import {$ShaderBindingContext, $ShaderBindingContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ShaderBindingContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GlProgram$Builder {

constructor(arg0: $ResourceLocation$Type)

public "link"<U>(arg0: $Function$Type<($ShaderBindingContext$Type), (U)>): $GlProgram<(U)>
public "attachShader"(arg0: $GlShader$Type): $GlProgram$Builder
public "bindAttribute"(arg0: string, arg1: integer): $GlProgram$Builder
public "bindFragmentData"(arg0: string, arg1: integer): $GlProgram$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlProgram$Builder$Type = ($GlProgram$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlProgram$Builder_ = $GlProgram$Builder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$LocalSectionIndex" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LocalSectionIndex {

constructor()

public static "incZ"(arg0: integer): integer
public static "decY"(arg0: integer): integer
public static "decX"(arg0: integer): integer
public static "decZ"(arg0: integer): integer
public static "pack"(arg0: integer, arg1: integer, arg2: integer): integer
public static "unpackX"(arg0: integer): integer
public static "unpackY"(arg0: integer): integer
public static "unpackZ"(arg0: integer): integer
public static "incX"(arg0: integer): integer
public static "incY"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalSectionIndex$Type = ($LocalSectionIndex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalSectionIndex_ = $LocalSectionIndex$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$PerformanceSettings" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SodiumGameOptions$PerformanceSettings {
 "chunkBuilderThreads": integer
 "alwaysDeferChunkUpdates": boolean
 "animateOnlyVisibleTextures": boolean
 "useEntityCulling": boolean
 "useFogOcclusion": boolean
 "useBlockFaceCulling": boolean
 "useCompactVertexFormat": boolean
 "useTranslucentFaceSorting": boolean
 "useNoErrorGLContext": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptions$PerformanceSettings$Type = ($SodiumGameOptions$PerformanceSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptions$PerformanceSettings_ = $SodiumGameOptions$PerformanceSettings$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$BucketedList" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $BucketedList<T> extends $AbstractList<(T)> {

constructor(numBuckets: integer)

public "get"(index: integer): T
public "size"(): integer
public "iterator"(): $Iterator<(T)>
public "addToBucket"(bucket: integer, element: T): void
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
export type $BucketedList$Type<T> = ($BucketedList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BucketedList_<T> = $BucketedList$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/api/inventory/$LithiumCooldownReceivingInventory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LithiumCooldownReceivingInventory {

 "setTransferCooldown"(currentTime: long): void
 "canReceiveTransferCooldown"(): boolean
}

export namespace $LithiumCooldownReceivingInventory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumCooldownReceivingInventory$Type = ($LithiumCooldownReceivingInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumCooldownReceivingInventory_ = $LithiumCooldownReceivingInventory$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/immediate/$CloudRenderer" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$ResourceProvider, $ResourceProvider$Type} from "packages/net/minecraft/server/packs/resources/$ResourceProvider"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"

export class $CloudRenderer {

constructor(arg0: $ResourceProvider$Type)

public "destroy"(): void
public "reloadTextures"(arg0: $ResourceProvider$Type): void
public "render"(arg0: $ClientLevel$Type, arg1: $LocalPlayer$Type, arg2: $PoseStack$Type, arg3: $Matrix4f$Type, arg4: float, arg5: float, arg6: double, arg7: double, arg8: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CloudRenderer$Type = ($CloudRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CloudRenderer_ = $CloudRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ControlElement, $ControlElement$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlElement"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"

export interface $Control<T> {

 "getMaxWidth"(): integer
 "createElement"(arg0: $Dim2i$Type): $ControlElement<(T)>
 "getOption"(): $Option<(T)>
}

export namespace $Control {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Control$Type<T> = ($Control<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Control_<T> = $Control$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/$MixinOption" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export class $MixinOption {

constructor(arg0: string, arg1: boolean, arg2: boolean)

public "getName"(): string
public "isEnabled"(): boolean
public "clearModsDefiningValue"(): void
public "isOverridden"(): boolean
public "isModDefined"(): boolean
public "getDefiningMods"(): $Collection<(string)>
public "isUserDefined"(): boolean
public "addModOverride"(arg0: boolean, arg1: string): void
public "setEnabled"(arg0: boolean, arg1: boolean): void
get "name"(): string
get "enabled"(): boolean
get "overridden"(): boolean
get "modDefined"(): boolean
get "definingMods"(): $Collection<(string)>
get "userDefined"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinOption$Type = ($MixinOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinOption_ = $MixinOption$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$HopperHelper" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$Hopper, $Hopper$Type} from "packages/net/minecraft/world/level/block/entity/$Hopper"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$WorldlyContainer, $WorldlyContainer$Type} from "packages/net/minecraft/world/$WorldlyContainer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$ComparatorUpdatePattern, $ComparatorUpdatePattern$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$ComparatorUpdatePattern"

export class $HopperHelper {

constructor()

public static "tryMoveSingleItem"(to: $Container$Type, stack: $ItemStack$Type, fromDirection: $Direction$Type): boolean
public static "tryMoveSingleItem"(to: $Container$Type, toSided: $WorldlyContainer$Type, transferStack: $ItemStack$Type, targetSlot: integer, fromDirection: $Direction$Type): boolean
public static "getHopperPickupVolumeBoxes"(hopper: $Hopper$Type): ($AABB)[]
public static "determineComparatorUpdatePattern"(from: $Container$Type, fromStackList: $LithiumStackList$Type): $ComparatorUpdatePattern
public static "replaceDoubleInventory"(blockInventory: $Container$Type): $Container
public static "vanillaGetBlockInventory"(world: $Level$Type, blockPos: $BlockPos$Type): $Container
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HopperHelper$Type = ($HopperHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HopperHelper_ = $HopperHelper$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderTask" {
import {$ChunkBuildContext, $ChunkBuildContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildContext"
import {$CancellationToken, $CancellationToken$Type} from "packages/me/jellysquid/mods/sodium/client/util/task/$CancellationToken"

export class $ChunkBuilderTask<OUTPUT> {

constructor()

public "execute"(arg0: $ChunkBuildContext$Type, arg1: $CancellationToken$Type): OUTPUT
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuilderTask$Type<OUTPUT> = ($ChunkBuilderTask<(OUTPUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuilderTask_<OUTPUT> = $ChunkBuilderTask$Type<(OUTPUT)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/control/$SliderControl" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ControlElement, $ControlElement$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlElement"
import {$Control, $Control$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$ControlValueFormatter, $ControlValueFormatter$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlValueFormatter"

export class $SliderControl implements $Control<(integer)> {

constructor(arg0: $Option$Type<(integer)>, arg1: integer, arg2: integer, arg3: integer, arg4: $ControlValueFormatter$Type)

public "getMaxWidth"(): integer
public "createElement"(arg0: $Dim2i$Type): $ControlElement<(integer)>
public "getOption"(): $Option<(integer)>
get "maxWidth"(): integer
get "option"(): $Option<(integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SliderControl$Type = ($SliderControl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SliderControl_ = $SliderControl$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/version/$LanguageCodePage" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $LanguageCodePage extends $Record {

constructor(languageId: integer, codePage: integer)

public "languageId"(): integer
public "codePage"(): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LanguageCodePage$Type = ($LanguageCodePage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LanguageCodePage_ = $LanguageCodePage$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/device/$GLRenderDevice" {
import {$DeviceFunctions, $DeviceFunctions$Type} from "packages/me/jellysquid/mods/sodium/client/gl/functions/$DeviceFunctions"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$RenderDevice, $RenderDevice$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice"
import {$GLCapabilities, $GLCapabilities$Type} from "packages/org/lwjgl/opengl/$GLCapabilities"

export class $GLRenderDevice implements $RenderDevice {

constructor()

public "getDeviceFunctions"(): $DeviceFunctions
public "getCapabilities"(): $GLCapabilities
public "createCommandList"(): $CommandList
public "makeActive"(): void
public "makeInactive"(): void
public static "enterManagedCode"(): void
public static "exitManagedCode"(): void
get "deviceFunctions"(): $DeviceFunctions
get "capabilities"(): $GLCapabilities
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLRenderDevice$Type = ($GLRenderDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLRenderDevice_ = $GLRenderDevice$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexConsumerTracker" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"

export class $VertexConsumerTracker {

constructor()

public static "logBadConsumer"(arg0: $VertexConsumer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexConsumerTracker$Type = ($VertexConsumerTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexConsumerTracker_ = $VertexConsumerTracker$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$ComparatorUpdatePattern" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"

export class $ComparatorUpdatePattern extends $Enum<($ComparatorUpdatePattern)> {
static readonly "NO_UPDATE": $ComparatorUpdatePattern
static readonly "UPDATE": $ComparatorUpdatePattern
static readonly "DECREMENT_UPDATE_INCREMENT_UPDATE": $ComparatorUpdatePattern
static readonly "UPDATE_DECREMENT_UPDATE_INCREMENT_UPDATE": $ComparatorUpdatePattern


public "thenDecrementUpdateIncrementUpdate"(): $ComparatorUpdatePattern
public static "values"(): ($ComparatorUpdatePattern)[]
public static "valueOf"(name: string): $ComparatorUpdatePattern
public "apply"(blockEntity: $BlockEntity$Type, stackList: $LithiumStackList$Type): void
public "thenUpdate"(): $ComparatorUpdatePattern
public "isChainable"(): boolean
get "chainable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComparatorUpdatePattern$Type = (("decrement_update_increment_update") | ("update") | ("update_decrement_update_increment_update") | ("no_update")) | ($ComparatorUpdatePattern);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComparatorUpdatePattern_ = $ComparatorUpdatePattern$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderInterface" {
import {$ChunkShaderOptions, $ChunkShaderOptions$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderOptions"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$ShaderBindingContext, $ShaderBindingContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ShaderBindingContext"

export class $ChunkShaderInterface {

constructor(arg0: $ShaderBindingContext$Type, arg1: $ChunkShaderOptions$Type)

/**
 * 
 * @deprecated
 */
public "setupState"(): void
public "setProjectionMatrix"(arg0: $Matrix4fc$Type): void
public "setModelViewMatrix"(arg0: $Matrix4fc$Type): void
public "setRegionOffset"(arg0: float, arg1: float, arg2: float): void
set "projectionMatrix"(value: $Matrix4fc$Type)
set "modelViewMatrix"(value: $Matrix4fc$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkShaderInterface$Type = ($ChunkShaderInterface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkShaderInterface_ = $ChunkShaderInterface$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeColorSource" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ColorResolver, $ColorResolver$Type} from "packages/net/minecraft/world/level/$ColorResolver"

export class $BiomeColorSource extends $Enum<($BiomeColorSource)> {
static readonly "GRASS": $BiomeColorSource
static readonly "FOLIAGE": $BiomeColorSource
static readonly "WATER": $BiomeColorSource
static readonly "VALUES": ($BiomeColorSource)[]
static readonly "COUNT": integer


public static "values"(): ($BiomeColorSource)[]
public static "valueOf"(arg0: string): $BiomeColorSource
public static "from"(arg0: $ColorResolver$Type): $BiomeColorSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeColorSource$Type = (("foliage") | ("grass") | ("water")) | ($BiomeColorSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeColorSource_ = $BiomeColorSource$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/console/$ConsoleRenderer" {
import {$Console, $Console$Type} from "packages/me/jellysquid/mods/sodium/client/gui/console/$Console"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ConsoleRenderer {

constructor()

public "update"(arg0: $Console$Type, arg1: double): void
public "draw"(arg0: $GuiGraphics$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConsoleRenderer$Type = ($ConsoleRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConsoleRenderer_ = $ConsoleRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionMeshParts" {
import {$TranslucentQuadAnalyzer$SortState, $TranslucentQuadAnalyzer$SortState$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState"
import {$VertexRange, $VertexRange$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$VertexRange"
import {$NativeBuffer, $NativeBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/util/$NativeBuffer"

export class $BuiltSectionMeshParts {

constructor(arg0: $NativeBuffer$Type, arg1: $NativeBuffer$Type, arg2: $TranslucentQuadAnalyzer$SortState$Type, arg3: ($VertexRange$Type)[])

public "getVertexData"(): $NativeBuffer
public "getVertexRanges"(): ($VertexRange)[]
public "getSortState"(): $TranslucentQuadAnalyzer$SortState
public "getIndexData"(): $NativeBuffer
get "vertexData"(): $NativeBuffer
get "vertexRanges"(): ($VertexRange)[]
get "sortState"(): $TranslucentQuadAnalyzer$SortState
get "indexData"(): $NativeBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltSectionMeshParts$Type = ($BuiltSectionMeshParts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltSectionMeshParts_ = $BuiltSectionMeshParts$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$SectionedBlockChangeTracker" {
import {$WorldSectionBox, $WorldSectionBox$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$WorldSectionBox"
import {$BlockListeningSection, $BlockListeningSection$Type} from "packages/me/jellysquid/mods/lithium/common/block/$BlockListeningSection"
import {$ListeningBlockStatePredicate, $ListeningBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$ListeningBlockStatePredicate"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $SectionedBlockChangeTracker {
readonly "trackedWorldSections": $WorldSectionBox
readonly "blockGroup": $ListeningBlockStatePredicate

constructor(trackedWorldSections: $WorldSectionBox$Type, blockGroup: $ListeningBlockStatePredicate$Type)

public "equals"(obj: any): boolean
public "hashCode"(): integer
public "register"(): void
public "unregister"(): void
public "matchesMovedBox"(box: $AABB$Type): boolean
public static "registerAt"(world: $Level$Type, entityBoundingBox: $AABB$Type, blockGroup: $ListeningBlockStatePredicate$Type): $SectionedBlockChangeTracker
public "isUnchangedSince"(lastCheckedTime: long): boolean
public "setChanged"(atTime: long): void
public "setChanged"(section: $BlockListeningSection$Type): void
public "listenToAllSections"(): void
set "changed"(value: long)
set "changed"(value: $BlockListeningSection$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionedBlockChangeTracker$Type = ($SectionedBlockChangeTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionedBlockChangeTracker_ = $SectionedBlockChangeTracker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferMapping" {
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GlBufferMapping {

constructor(arg0: $GlBuffer$Type, arg1: $ByteBuffer$Type)

public "isDisposed"(): boolean
public "getBufferObject"(): $GlBuffer
public "write"(arg0: $ByteBuffer$Type, arg1: integer): void
public "dispose"(): void
public "getMemoryBuffer"(): $ByteBuffer
get "disposed"(): boolean
get "bufferObject"(): $GlBuffer
get "memoryBuffer"(): $ByteBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferMapping$Type = ($GlBufferMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferMapping_ = $GlBufferMapping$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$PredicateFilterableList" {
import {$PredicateFilterableList$PredicateFilteredList, $PredicateFilterableList$PredicateFilteredList$Type} from "packages/me/jellysquid/mods/lithium/common/util/collections/$PredicateFilterableList$PredicateFilteredList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ListeningList, $ListeningList$Type} from "packages/me/jellysquid/mods/lithium/common/util/collections/$ListeningList"

export class $PredicateFilterableList<T> extends $ListeningList<(T)> {

constructor()

public "getFiltered"(predicate: $Predicate$Type<(T)>): $PredicateFilterableList$PredicateFilteredList<>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
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
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PredicateFilterableList$Type<T> = ($PredicateFilterableList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PredicateFilterableList_<T> = $PredicateFilterableList$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$ListeningLong2ObjectOpenHashMap" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ListeningLong2ObjectOpenHashMap$Callback, $ListeningLong2ObjectOpenHashMap$Callback$Type} from "packages/me/jellysquid/mods/lithium/common/util/collections/$ListeningLong2ObjectOpenHashMap$Callback"
import {$Long2ObjectOpenHashMap, $Long2ObjectOpenHashMap$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2ObjectOpenHashMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $ListeningLong2ObjectOpenHashMap<V> extends $Long2ObjectOpenHashMap<(V)> {

constructor(addCallback: $ListeningLong2ObjectOpenHashMap$Callback$Type<(V)>, removeCallback: $ListeningLong2ObjectOpenHashMap$Callback$Type<(V)>)

public "remove"(k: long): V
public "put"(k: long, v: V): V
public "defaultReturnValue"(): V
public "defaultReturnValue"(arg0: V): void
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
export type $ListeningLong2ObjectOpenHashMap$Type<V> = ($ListeningLong2ObjectOpenHashMap<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListeningLong2ObjectOpenHashMap_<V> = $ListeningLong2ObjectOpenHashMap$Type<(V)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderTextureSlot" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ChunkShaderTextureSlot extends $Enum<($ChunkShaderTextureSlot)> {
static readonly "BLOCK": $ChunkShaderTextureSlot
static readonly "LIGHT": $ChunkShaderTextureSlot
static readonly "VALUES": ($ChunkShaderTextureSlot)[]


public static "values"(): ($ChunkShaderTextureSlot)[]
public static "valueOf"(arg0: string): $ChunkShaderTextureSlot
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkShaderTextureSlot$Type = (("light") | ("block")) | ($ChunkShaderTextureSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkShaderTextureSlot_ = $ChunkShaderTextureSlot$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/chunk/$LithiumHashPalette" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IdMap, $IdMap$Type} from "packages/net/minecraft/core/$IdMap"
import {$PaletteResize, $PaletteResize$Type} from "packages/net/minecraft/world/level/chunk/$PaletteResize"
import {$Palette, $Palette$Type} from "packages/net/minecraft/world/level/chunk/$Palette"
import {$Reference2IntMap, $Reference2IntMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2IntMap"

export class $LithiumHashPalette<T> implements $Palette<(T)> {

constructor(idList: $IdMap$Type<(T)>, bits: integer, resizeHandler: $PaletteResize$Type<(T)>, list: $List$Type<(T)>)
constructor(idList: $IdMap$Type<(T)>, resizeHandler: $PaletteResize$Type<(T)>, indexBits: integer, entries: (T)[], table: $Reference2IntMap$Type<(T)>, size: integer)
constructor(idList: $IdMap$Type<(T)>, bits: integer, resizeHandler: $PaletteResize$Type<(T)>)

public "maybeHas"(predicate: $Predicate$Type<(T)>): boolean
public "getSerializedSize"(): integer
public "write"(buf: $FriendlyByteBuf$Type): void
public "read"(buf: $FriendlyByteBuf$Type): void
public "copy"(): $Palette<(T)>
public static "create"<A>(bits: integer, idList: $IdMap$Type<(A)>, listener: $PaletteResize$Type<(A)>, list: $List$Type<(A)>): $Palette<(A)>
public "getElements"(): $List<(T)>
public "idFor"(obj: T): integer
public "getSize"(): integer
public "valueFor"(id: integer): T
get "serializedSize"(): integer
get "elements"(): $List<(T)>
get "size"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumHashPalette$Type<T> = ($LithiumHashPalette<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumHashPalette_<T> = $LithiumHashPalette$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/$NativeImageAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $NativeImageAccessor {

 "getPointer"(): long

(): long
}

export namespace $NativeImageAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NativeImageAccessor$Type = ($NativeImageAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NativeImageAccessor_ = $NativeImageAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$ToggleableMovementTracker" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ToggleableMovementTracker {

 "setNotificationMask"(arg0: integer): integer

(arg0: integer): integer
}

export namespace $ToggleableMovementTracker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToggleableMovementTracker$Type = ($ToggleableMovementTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToggleableMovementTracker_ = $ToggleableMovementTracker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$Dim2i" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Point2i, $Point2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Point2i"

export class $Dim2i extends $Record implements $Point2i {

constructor(x: integer, y: integer, width: integer, height: integer)

public "withWidth"(arg0: integer): $Dim2i
public "canFitDimension"(arg0: $Dim2i$Type): boolean
public "withHeight"(arg0: integer): $Dim2i
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "x"(): integer
public "y"(): integer
public "width"(): integer
public "height"(): integer
public "getCenterX"(): integer
public "withParentOffset"(arg0: $Point2i$Type): $Dim2i
public "getLimitX"(): integer
public "getLimitY"(): integer
public "containsCursor"(arg0: double, arg1: double): boolean
public "overlapsWith"(arg0: $Dim2i$Type): boolean
public "getCenterY"(): integer
public "withX"(arg0: integer): $Dim2i
public "withY"(arg0: integer): $Dim2i
get "centerX"(): integer
get "limitX"(): integer
get "limitY"(): integer
get "centerY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dim2i$Type = ($Dim2i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dim2i_ = $Dim2i$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderBindingPoints" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ChunkShaderBindingPoints {
static readonly "ATTRIBUTE_POSITION_ID": integer
static readonly "ATTRIBUTE_COLOR": integer
static readonly "ATTRIBUTE_BLOCK_TEXTURE": integer
static readonly "ATTRIBUTE_LIGHT_TEXTURE": integer
static readonly "FRAG_COLOR": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkShaderBindingPoints$Type = ($ChunkShaderBindingPoints);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkShaderBindingPoints_ = $ChunkShaderBindingPoints$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/sorting/$InsertionSort" {
import {$AbstractSort, $AbstractSort$Type} from "packages/me/jellysquid/mods/sodium/client/util/sorting/$AbstractSort"

export class $InsertionSort extends $AbstractSort {

constructor()

public static "insertionSort"(arg0: (integer)[], arg1: integer, arg2: integer, arg3: (float)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InsertionSort$Type = ($InsertionSort);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InsertionSort_ = $InsertionSort$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/workarounds/nvidia/$NvidiaDriverVersion" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GLContextInfo, $GLContextInfo$Type} from "packages/me/jellysquid/mods/sodium/client/compatibility/environment/$GLContextInfo"

export class $NvidiaDriverVersion extends $Record {

constructor(major: integer, minor: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "major"(): integer
public "minor"(): integer
public "isWithinRange"(arg0: $NvidiaDriverVersion$Type, arg1: $NvidiaDriverVersion$Type): boolean
public static "tryParse"(arg0: $GLContextInfo$Type): $NvidiaDriverVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NvidiaDriverVersion$Type = ($NvidiaDriverVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NvidiaDriverVersion_ = $NvidiaDriverVersion$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/api/pathing/$BlockPathing" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BlockPathing {

 "needsDynamicNodeTypeCheck"(): boolean
 "needsDynamicBurningCheck"(): boolean
}

export namespace $BlockPathing {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockPathing$Type = ($BlockPathing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockPathing_ = $BlockPathing$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $BuiltSectionInfo {
static readonly "EMPTY": $BuiltSectionInfo
readonly "flags": integer
readonly "visibilityData": long
readonly "globalBlockEntities": ($BlockEntity)[]
readonly "culledBlockEntities": ($BlockEntity)[]
readonly "animatedSprites": ($TextureAtlasSprite)[]


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltSectionInfo$Type = ($BuiltSectionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltSectionInfo_ = $BuiltSectionInfo$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobQueue" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ChunkJob, $ChunkJob$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJob"

export class $ChunkJobQueue {


public "add"(arg0: $ChunkJob$Type, arg1: boolean): void
public "shutdown"(): $Collection<($ChunkJob)>
public "isEmpty"(): boolean
public "size"(): integer
public "isRunning"(): boolean
public "stealJob"(arg0: $ChunkJob$Type): boolean
public "waitForNextJob"(): $ChunkJob
get "empty"(): boolean
get "running"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkJobQueue$Type = ($ChunkJobQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkJobQueue_ = $ChunkJobQueue$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkUpdateType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ChunkUpdateType extends $Enum<($ChunkUpdateType)> {
static readonly "INITIAL_BUILD": $ChunkUpdateType
static readonly "SORT": $ChunkUpdateType
static readonly "IMPORTANT_SORT": $ChunkUpdateType
static readonly "REBUILD": $ChunkUpdateType
static readonly "IMPORTANT_REBUILD": $ChunkUpdateType


public static "getPromotionUpdateType"(arg0: $ChunkUpdateType$Type, arg1: $ChunkUpdateType$Type): $ChunkUpdateType
public static "values"(): ($ChunkUpdateType)[]
public static "valueOf"(arg0: string): $ChunkUpdateType
/**
 * 
 * @deprecated
 */
public static "canPromote"(arg0: $ChunkUpdateType$Type, arg1: $ChunkUpdateType$Type): boolean
public "isImportant"(): boolean
public "isSort"(): boolean
public "getMaximumQueueSize"(): integer
get "important"(): boolean
get "sort"(): boolean
get "maximumQueueSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkUpdateType$Type = (("initial_build") | ("rebuild") | ("important_rebuild") | ("important_sort") | ("sort")) | ($ChunkUpdateType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkUpdateType_ = $ChunkUpdateType$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/$MixinConfig" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$MixinOption, $MixinOption$Type} from "packages/me/jellysquid/mods/sodium/mixin/$MixinOption"

export class $MixinConfig {


public static "load"(arg0: $File$Type): $MixinConfig
public "getOptionCount"(): integer
public "getOptionOverrideCount"(): integer
public "getEffectiveOptionForMixin"(arg0: string): $MixinOption
get "optionCount"(): integer
get "optionOverrideCount"(): integer
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
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderLoader" {
import {$ShaderType, $ShaderType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderType"
import {$GlShader, $GlShader$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlShader"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ShaderConstants, $ShaderConstants$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants"

export class $ShaderLoader {

constructor()

public static "loadShader"(arg0: $ShaderType$Type, arg1: $ResourceLocation$Type, arg2: $ShaderConstants$Type): $GlShader
public static "getShaderSource"(arg0: $ResourceLocation$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderLoader$Type = ($ShaderLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderLoader_ = $ShaderLoader$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListenerProvider" {
import {$NearbyEntityTracker, $NearbyEntityTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityTracker"
import {$NearbyEntityListenerMulti, $NearbyEntityListenerMulti$Type} from "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListenerMulti"

export interface $NearbyEntityListenerProvider {

 "getListener"(): $NearbyEntityListenerMulti
 "addListener"(arg0: $NearbyEntityTracker$Type<(any)>): void
}

export namespace $NearbyEntityListenerProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NearbyEntityListenerProvider$Type = ($NearbyEntityListenerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NearbyEntityListenerProvider_ = $NearbyEntityListenerProvider$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/deduplication/$LithiumInternerWrapper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LithiumInternerWrapper<T> {

 "deleteCanonical"(arg0: T): void
 "getCanonical"(arg0: T): T
}

export namespace $LithiumInternerWrapper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumInternerWrapper$Type<T> = ($LithiumInternerWrapper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumInternerWrapper_<T> = $LithiumInternerWrapper$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSectionFlags" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RenderSectionFlags {
static readonly "HAS_BLOCK_GEOMETRY": integer
static readonly "HAS_BLOCK_ENTITIES": integer
static readonly "HAS_ANIMATED_SPRITES": integer
static readonly "HAS_TRANSLUCENT_DATA": integer
static readonly "NONE": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSectionFlags$Type = ($RenderSectionFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSectionFlags_ = $RenderSectionFlags$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/workarounds/$Workarounds$Reference" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Workarounds$Reference extends $Enum<($Workarounds$Reference)> {
static readonly "NVIDIA_THREADED_OPTIMIZATIONS": $Workarounds$Reference
static readonly "NO_ERROR_CONTEXT_UNSUPPORTED": $Workarounds$Reference


public static "values"(): ($Workarounds$Reference)[]
public static "valueOf"(arg0: string): $Workarounds$Reference
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Workarounds$Reference$Type = (("nvidia_threaded_optimizations") | ("no_error_context_unsupported")) | ($Workarounds$Reference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Workarounds$Reference_ = $Workarounds$Reference$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/functions/$BufferStorageFunctions" {
import {$GlBufferTarget, $GlBufferTarget$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferTarget"
import {$EnumBitField, $EnumBitField$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBitField"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$RenderDevice, $RenderDevice$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice"
import {$GlBufferStorageFlags, $GlBufferStorageFlags$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferStorageFlags"

export class $BufferStorageFunctions extends $Enum<($BufferStorageFunctions)> {
static readonly "NONE": $BufferStorageFunctions
static readonly "CORE": $BufferStorageFunctions
static readonly "ARB": $BufferStorageFunctions


public static "pickBest"(arg0: $RenderDevice$Type): $BufferStorageFunctions
public static "values"(): ($BufferStorageFunctions)[]
public static "valueOf"(arg0: string): $BufferStorageFunctions
public "createBufferStorage"(arg0: $GlBufferTarget$Type, arg1: long, arg2: $EnumBitField$Type<($GlBufferStorageFlags$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferStorageFunctions$Type = (("arb") | ("core") | ("none")) | ($BufferStorageFunctions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferStorageFunctions_ = $BufferStorageFunctions$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlImmutableBuffer" {
import {$EnumBitField, $EnumBitField$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBitField"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$GlBufferStorageFlags, $GlBufferStorageFlags$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferStorageFlags"

export class $GlImmutableBuffer extends $GlBuffer {

constructor(arg0: $EnumBitField$Type<($GlBufferStorageFlags$Type)>)

public "getFlags"(): $EnumBitField<($GlBufferStorageFlags)>
get "flags"(): $EnumBitField<($GlBufferStorageFlags)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlImmutableBuffer$Type = ($GlImmutableBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlImmutableBuffer_ = $GlImmutableBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/model/$SimpleBakedModelAccessor" {
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"

export interface $SimpleBakedModelAccessor {

 "getBlockRenderTypes"(): $ChunkRenderTypeSet

(): $ChunkRenderTypeSet
}

export namespace $SimpleBakedModelAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleBakedModelAccessor$Type = ($SimpleBakedModelAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleBakedModelAccessor_ = $SimpleBakedModelAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/widgets/$FlatButtonWidget$Style" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FlatButtonWidget$Style {
 "bgHovered": integer
 "bgDefault": integer
 "bgDisabled": integer
 "textDefault": integer
 "textDisabled": integer

constructor()

public static "defaults"(): $FlatButtonWidget$Style
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlatButtonWidget$Style$Type = ($FlatButtonWidget$Style);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlatButtonWidget$Style_ = $FlatButtonWidget$Style$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderer" {
import {$LightPipelineProvider, $LightPipelineProvider$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipelineProvider"
import {$BlockRenderContext, $BlockRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext"
import {$ColorProviderRegistry, $ColorProviderRegistry$Type} from "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProviderRegistry"
import {$ChunkBuildBuffers, $ChunkBuildBuffers$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers"

export class $BlockRenderer {

constructor(arg0: $ColorProviderRegistry$Type, arg1: $LightPipelineProvider$Type)

public "renderModel"(arg0: $BlockRenderContext$Type, arg1: $ChunkBuildBuffers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRenderer$Type = ($BlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRenderer_ = $BlockRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegion" {
import {$ChunkRenderList, $ChunkRenderList$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderList"
import {$StagingBuffer, $StagingBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer"
import {$SectionRenderDataStorage, $SectionRenderDataStorage$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$SectionRenderDataStorage"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$RenderRegion$DeviceResources, $RenderRegion$DeviceResources$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegion$DeviceResources"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"

export class $RenderRegion {
static readonly "REGION_WIDTH": integer
static readonly "REGION_HEIGHT": integer
static readonly "REGION_LENGTH": integer
static readonly "REGION_SIZE": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: $StagingBuffer$Type)

public "update"(arg0: $CommandList$Type): void
public "isEmpty"(): boolean
public "getResources"(): $RenderRegion$DeviceResources
public static "key"(arg0: integer, arg1: integer, arg2: integer): long
public "delete"(arg0: $CommandList$Type): void
public "addSection"(arg0: $RenderSection$Type): void
public "refresh"(arg0: $CommandList$Type): void
public "getSection"(arg0: integer): $RenderSection
public "getChunkX"(): integer
public "getChunkZ"(): integer
public "getChunkY"(): integer
public "getOriginX"(): integer
public "getOriginY"(): integer
public "getCenterX"(): integer
public "createResources"(arg0: $CommandList$Type): $RenderRegion$DeviceResources
public "createStorage"(arg0: $TerrainRenderPass$Type): $SectionRenderDataStorage
public "getCenterZ"(): integer
public "removeSection"(arg0: $RenderSection$Type): void
public "getOriginZ"(): integer
public "getCenterY"(): integer
public "getStorage"(arg0: $TerrainRenderPass$Type): $SectionRenderDataStorage
public "getRenderList"(): $ChunkRenderList
get "empty"(): boolean
get "resources"(): $RenderRegion$DeviceResources
get "chunkX"(): integer
get "chunkZ"(): integer
get "chunkY"(): integer
get "originX"(): integer
get "originY"(): integer
get "centerX"(): integer
get "centerZ"(): integer
get "originZ"(): integer
get "centerY"(): integer
get "renderList"(): $ChunkRenderList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderRegion$Type = ($RenderRegion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderRegion_ = $RenderRegion$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/$BlockStateFlagHolder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BlockStateFlagHolder {

 "getAllFlags"(): integer

(): integer
}

export namespace $BlockStateFlagHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateFlagHolder$Type = ($BlockStateFlagHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateFlagHolder_ = $BlockStateFlagHolder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo$Builder" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$VisibilitySet, $VisibilitySet$Type} from "packages/net/minecraft/client/renderer/chunk/$VisibilitySet"
import {$BuiltSectionInfo, $BuiltSectionInfo$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $BuiltSectionInfo$Builder {

constructor()

public "build"(): $BuiltSectionInfo
public "addBlockEntity"(arg0: $BlockEntity$Type, arg1: boolean): void
public "addRenderPass"(arg0: $TerrainRenderPass$Type): void
public "setOcclusionData"(arg0: $VisibilitySet$Type): void
public "addSprite"(arg0: $TextureAtlasSprite$Type): void
set "occlusionData"(value: $VisibilitySet$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltSectionInfo$Builder$Type = ($BuiltSectionInfo$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltSectionInfo$Builder_ = $BuiltSectionInfo$Builder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ShaderBindingContext" {
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$GlUniformBlock, $GlUniformBlock$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformBlock"
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export interface $ShaderBindingContext {

 "bindUniformBlock"(arg0: string, arg1: integer): $GlUniformBlock
 "bindUniform"<U extends $GlUniform<(any)>>(arg0: string, arg1: $IntFunction$Type<(U)>): U
}

export namespace $ShaderBindingContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderBindingContext$Type = ($ShaderBindingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderBindingContext_ = $ShaderBindingContext$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/listeners/$WorldBorderListenerOnceMulti" {
import {$WorldBorder, $WorldBorder$Type} from "packages/net/minecraft/world/level/border/$WorldBorder"
import {$BorderChangeListener, $BorderChangeListener$Type} from "packages/net/minecraft/world/level/border/$BorderChangeListener"
import {$WorldBorderListenerOnce, $WorldBorderListenerOnce$Type} from "packages/me/jellysquid/mods/lithium/common/world/listeners/$WorldBorderListenerOnce"

export class $WorldBorderListenerOnceMulti implements $BorderChangeListener {

constructor()

public "add"(listener: $WorldBorderListenerOnce$Type): void
public "onBorderCenterSet"(border: $WorldBorder$Type, centerX: double, centerZ: double): void
public "onBorderSetDamageSafeZOne"(border: $WorldBorder$Type, safeZoneRadius: double): void
public "onBorderSizeLerping"(border: $WorldBorder$Type, fromSize: double, toSize: double, time: long): void
public "onBorderSetWarningTime"(border: $WorldBorder$Type, warningTime: integer): void
public "onBorderSizeSet"(border: $WorldBorder$Type, size: double): void
public "onBorderSetDamagePerBlock"(border: $WorldBorder$Type, damagePerBlock: double): void
public "onBorderSetWarningBlocks"(border: $WorldBorder$Type, warningBlockDistance: integer): void
public "onAreaReplaced"(border: $WorldBorder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldBorderListenerOnceMulti$Type = ($WorldBorderListenerOnceMulti);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldBorderListenerOnceMulti_ = $WorldBorderListenerOnceMulti$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/transform/$CommonVertexElement" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export class $CommonVertexElement extends $Enum<($CommonVertexElement)> {
static readonly "POSITION": $CommonVertexElement
static readonly "COLOR": $CommonVertexElement
static readonly "TEXTURE": $CommonVertexElement
static readonly "OVERLAY": $CommonVertexElement
static readonly "LIGHT": $CommonVertexElement
static readonly "NORMAL": $CommonVertexElement
static readonly "COUNT": integer


public static "values"(): ($CommonVertexElement)[]
public static "valueOf"(arg0: string): $CommonVertexElement
public static "getOffsets"(arg0: $VertexFormat$Type): (integer)[]
public static "getCommonType"(arg0: $VertexFormatElement$Type): $CommonVertexElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonVertexElement$Type = (("normal") | ("color") | ("overlay") | ("light") | ("texture") | ("position")) | ($CommonVertexElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonVertexElement_ = $CommonVertexElement$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/interests/$PointOfInterestSetExtended" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$PoiRecord, $PoiRecord$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiRecord"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$PoiManager$Occupancy, $PoiManager$Occupancy$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiManager$Occupancy"

export interface $PointOfInterestSetExtended {

 "collectMatchingPoints"(arg0: $Predicate$Type<($Holder$Type<($PoiType$Type)>)>, arg1: $PoiManager$Occupancy$Type, arg2: $Consumer$Type<($PoiRecord$Type)>): void

(arg0: $Predicate$Type<($Holder$Type<($PoiType$Type)>)>, arg1: $PoiManager$Occupancy$Type, arg2: $Consumer$Type<($PoiRecord$Type)>): void
}

export namespace $PointOfInterestSetExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PointOfInterestSetExtended$Type = ($PointOfInterestSetExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PointOfInterestSetExtended_ = $PointOfInterestSetExtended$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/msgbox/$MsgBoxParamSw" {
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$MsgBoxCallbackI, $MsgBoxCallbackI$Type} from "packages/me/jellysquid/mods/sodium/client/platform/windows/api/msgbox/$MsgBoxCallbackI"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $MsgBoxParamSw extends $Struct {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "OFFSET_CB_SIZE": integer
static readonly "OFFSET_HWND_OWNER": integer
static readonly "OFFSET_HINSTANCE": integer
static readonly "OFFSET_LPSZ_TEXT": integer
static readonly "OFFSET_LPSZ_CAPTION": integer
static readonly "OFFSET_DW_STYLE": integer
static readonly "OFFSET_LPSZ_ICON": integer
static readonly "OFFSET_DW_CONTEXT_HELP_ID": integer
static readonly "OFFSET_LPFN_MSG_BOX_CALLBACK": integer
static readonly "OFFSET_DW_LANGUAGE_ID": integer


public static "allocate"(arg0: $MemoryStack$Type): $MsgBoxParamSw
public "setText"(arg0: $ByteBuffer$Type): void
public "sizeof"(): integer
public "setCbSize"(arg0: integer): void
public "setHWndOwner"(arg0: long): void
public "setCaption"(arg0: $ByteBuffer$Type): void
public "setCallback"(arg0: $MsgBoxCallbackI$Type): void
public "setStyle"(arg0: integer): void
set "text"(value: $ByteBuffer$Type)
set "cbSize"(value: integer)
set "hWndOwner"(value: long)
set "caption"(value: $ByteBuffer$Type)
set "callback"(value: $MsgBoxCallbackI$Type)
set "style"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MsgBoxParamSw$Type = ($MsgBoxParamSw);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MsgBoxParamSw_ = $MsgBoxParamSw$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions" {
import {$SodiumGameOptions$QualitySettings, $SodiumGameOptions$QualitySettings$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$QualitySettings"
import {$SodiumGameOptions$AdvancedSettings, $SodiumGameOptions$AdvancedSettings$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$AdvancedSettings"
import {$SodiumGameOptions$PerformanceSettings, $SodiumGameOptions$PerformanceSettings$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$PerformanceSettings"
import {$SodiumGameOptions$NotificationSettings, $SodiumGameOptions$NotificationSettings$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$NotificationSettings"

export class $SodiumGameOptions {
readonly "quality": $SodiumGameOptions$QualitySettings
readonly "advanced": $SodiumGameOptions$AdvancedSettings
readonly "performance": $SodiumGameOptions$PerformanceSettings
readonly "notifications": $SodiumGameOptions$NotificationSettings

constructor()

public static "load"(): $SodiumGameOptions
public static "load"(arg0: string): $SodiumGameOptions
public "setReadOnly"(): void
public static "defaults"(): $SodiumGameOptions
public "getFileName"(): string
public "isReadOnly"(): boolean
/**
 * 
 * @deprecated
 */
public "writeChanges"(): void
public static "writeToDisk"(arg0: $SodiumGameOptions$Type): void
get "fileName"(): string
get "readOnly"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptions$Type = ($SodiumGameOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptions_ = $SodiumGameOptions$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/array/$GlVertexArray" {
import {$GlObject, $GlObject$Type} from "packages/me/jellysquid/mods/sodium/client/gl/$GlObject"

export class $GlVertexArray extends $GlObject {
static readonly "NULL_ARRAY_ID": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexArray$Type = ($GlVertexArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexArray_ = $GlVertexArray$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/interests/iterator/$SphereChunkOrderedPoiSetSpliterator" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RegionBasedStorageSectionExtended, $RegionBasedStorageSectionExtended$Type} from "packages/me/jellysquid/mods/lithium/common/world/interests/$RegionBasedStorageSectionExtended"
import {$Spliterators$AbstractSpliterator, $Spliterators$AbstractSpliterator$Type} from "packages/java/util/$Spliterators$AbstractSpliterator"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$PoiSection, $PoiSection$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiSection"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $SphereChunkOrderedPoiSetSpliterator extends $Spliterators$AbstractSpliterator<($Stream<($PoiSection)>)> {

constructor(radius: integer, origin: $BlockPos$Type, storage: $RegionBasedStorageSectionExtended$Type<($PoiSection$Type)>)

public "tryAdvance"(action: $Consumer$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SphereChunkOrderedPoiSetSpliterator$Type = ($SphereChunkOrderedPoiSetSpliterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SphereChunkOrderedPoiSetSpliterator_ = $SphereChunkOrderedPoiSetSpliterator$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/world/combined_heightmap_update/$HeightmapAccessor" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export interface $HeightmapAccessor {

 "callSet"(arg0: integer, arg1: integer, arg2: integer): void
 "getBlockPredicate"(): $Predicate<($BlockState)>
}

export namespace $HeightmapAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HeightmapAccessor$Type = ($HeightmapAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HeightmapAccessor_ = $HeightmapAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/ai/pathing/$PathNodeCache" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$LevelChunkSection, $LevelChunkSection$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunkSection"
import {$BlockPathTypes, $BlockPathTypes$Type} from "packages/net/minecraft/world/level/pathfinder/$BlockPathTypes"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos$MutableBlockPos, $BlockPos$MutableBlockPos$Type} from "packages/net/minecraft/core/$BlockPos$MutableBlockPos"
import {$BlockBehaviour$BlockStateBase, $BlockBehaviour$BlockStateBase$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$BlockStateBase"

export class $PathNodeCache {

constructor()

public static "getPathNodeType"(state: $BlockState$Type): $BlockPathTypes
public static "getNeighborPathNodeType"(state: $BlockBehaviour$BlockStateBase$Type): $BlockPathTypes
public static "getNodeTypeFromNeighbors"(world: $BlockGetter$Type, pos: $BlockPos$MutableBlockPos$Type, type: $BlockPathTypes$Type): $BlockPathTypes
public static "isSectionSafeAsNeighbor"(section: $LevelChunkSection$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathNodeCache$Type = ($PathNodeCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathNodeCache_ = $PathNodeCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatDescriptionImpl" {
import {$CommonVertexAttribute, $CommonVertexAttribute$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/$CommonVertexAttribute"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export class $VertexFormatDescriptionImpl implements $VertexFormatDescription {

constructor(arg0: $VertexFormat$Type, arg1: integer)

/**
 * 
 * @deprecated
 */
public "format"(): $VertexFormat
public "id"(): integer
public "stride"(): integer
public static "getOffsets"(arg0: $VertexFormat$Type): (integer)[]
public "isSimpleFormat"(): boolean
public "containsElement"(arg0: $CommonVertexAttribute$Type): boolean
public "getElementOffset"(arg0: $CommonVertexAttribute$Type): integer
get "simpleFormat"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexFormatDescriptionImpl$Type = ($VertexFormatDescriptionImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexFormatDescriptionImpl_ = $VertexFormatDescriptionImpl$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$Point2i" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Point2i {

 "x"(): integer
 "y"(): integer
}

export namespace $Point2i {
const ZERO: $Point2i
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Point2i$Type = ($Point2i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Point2i_ = $Point2i$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GlUniform<T> {


public "set"(arg0: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniform$Type<T> = ($GlUniform<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniform_<T> = $GlUniform$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/$LithiumMod" {
import {$LithiumConfig, $LithiumConfig$Type} from "packages/me/jellysquid/mods/lithium/common/config/$LithiumConfig"

export class $LithiumMod {
static readonly "MODID": string
static "CONFIG": $LithiumConfig

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumMod$Type = ($LithiumMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumMod_ = $LithiumMod$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/$BakedQuadView" {
import {$ModelQuadFacing, $ModelQuadFacing$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadFacing"
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $BakedQuadView extends $ModelQuadView {

 "getNormalFace"(): $ModelQuadFacing
 "hasShade"(): boolean
 "getFlags"(): integer
 "getY"(arg0: integer): float
 "getColorIndex"(): integer
 "getSprite"(): $TextureAtlasSprite
 "getLightFace"(): $Direction
 "getForgeNormal"(arg0: integer): integer
 "getLight"(arg0: integer): integer
 "getTexV"(arg0: integer): float
 "getTexU"(arg0: integer): float
 "hasAmbientOcclusion"(): boolean
 "getZ"(arg0: integer): float
 "getX"(arg0: integer): float
 "getColor"(arg0: integer): integer
 "hasColor"(): boolean
}

export namespace $BakedQuadView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedQuadView$Type = ($BakedQuadView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedQuadView_ = $BakedQuadView$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkFogMode" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ChunkShaderFogComponent, $ChunkShaderFogComponent$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderFogComponent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ShaderBindingContext, $ShaderBindingContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ShaderBindingContext"

export class $ChunkFogMode extends $Enum<($ChunkFogMode)> {
static readonly "NONE": $ChunkFogMode
static readonly "SMOOTH": $ChunkFogMode


public static "values"(): ($ChunkFogMode)[]
public static "valueOf"(arg0: string): $ChunkFogMode
public "getFactory"(): $Function<($ShaderBindingContext), ($ChunkShaderFogComponent)>
public "getDefines"(): $List<(string)>
get "factory"(): $Function<($ShaderBindingContext), ($ChunkShaderFogComponent)>
get "defines"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkFogMode$Type = (("none") | ("smooth")) | ($ChunkFogMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkFogMode_ = $ChunkFogMode$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$DefaultChunkRenderer" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$CameraTransform, $CameraTransform$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$CameraTransform"
import {$ChunkRenderListIterable, $ChunkRenderListIterable$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderListIterable"
import {$RenderDevice, $RenderDevice$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$ChunkRenderMatrices, $ChunkRenderMatrices$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderMatrices"
import {$ShaderChunkRenderer, $ShaderChunkRenderer$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ShaderChunkRenderer"

export class $DefaultChunkRenderer extends $ShaderChunkRenderer {

constructor(arg0: $RenderDevice$Type, arg1: $ChunkVertexType$Type)

public "delete"(arg0: $CommandList$Type): void
public "render"(arg0: $ChunkRenderMatrices$Type, arg1: $CommandList$Type, arg2: $ChunkRenderListIterable$Type, arg3: $TerrainRenderPass$Type, arg4: $CameraTransform$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultChunkRenderer$Type = ($DefaultChunkRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultChunkRenderer_ = $DefaultChunkRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$Option" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Control, $Control$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$OptionFlag, $OptionFlag$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionFlag"
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"
import {$OptionImpact, $OptionImpact$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpact"

export interface $Option<T> {

 "getName"(): $Component
 "getValue"(): T
 "getId"(): $OptionIdentifier<(T)>
 "setValue"(arg0: T): void
 "reset"(): void
 "getControl"(): $Control<(T)>
 "getFlags"(): $Collection<($OptionFlag)>
 "isAvailable"(): boolean
 "getImpact"(): $OptionImpact
 "applyChanges"(): void
 "getTooltip"(): $Component
 "hasChanged"(): boolean
 "getStorage"(): $OptionStorage<(any)>
}

export namespace $Option {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Option$Type<T> = ($Option<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Option_<T> = $Option$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/iterator/$ByteIterator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ByteIterator {

 "hasNext"(): boolean
 "nextByteAsInt"(): integer
}

export namespace $ByteIterator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ByteIterator$Type = ($ByteIterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ByteIterator_ = $ByteIterator$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderList" {
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$ByteIterator, $ByteIterator$Type} from "packages/me/jellysquid/mods/sodium/client/util/iterator/$ByteIterator"
import {$RenderRegion, $RenderRegion$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegion"

export class $ChunkRenderList {

constructor(arg0: $RenderRegion$Type)

public "sectionsWithSpritesIterator"(): $ByteIterator
public "getLastVisibleFrame"(): integer
public "getSectionsWithGeometryCount"(): integer
public "sectionsWithGeometryIterator"(arg0: boolean): $ByteIterator
public "getSectionsWithSpritesCount"(): integer
public "getSectionsWithEntitiesCount"(): integer
public "add"(arg0: $RenderSection$Type): void
public "size"(): integer
public "reset"(arg0: integer): void
public "getRegion"(): $RenderRegion
public "sectionsWithEntitiesIterator"(): $ByteIterator
get "lastVisibleFrame"(): integer
get "sectionsWithGeometryCount"(): integer
get "sectionsWithSpritesCount"(): integer
get "sectionsWithEntitiesCount"(): integer
get "region"(): $RenderRegion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRenderList$Type = ($ChunkRenderList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRenderList_ = $ChunkRenderList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget" {
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $AbstractWidget implements $Renderable, $GuiEventListener, $NarratableEntry {


public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "updateNarration"(arg0: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "setFocused"(arg0: boolean): void
public "isHovered"(): boolean
public "isFocused"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getCurrentFocusPath"(): $ComponentPath
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRectangle"(): $ScreenRectangle
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "isActive"(): boolean
public "getTabOrderGroup"(): integer
set "focused"(value: boolean)
get "hovered"(): boolean
get "focused"(): boolean
get "currentFocusPath"(): $ComponentPath
get "rectangle"(): $ScreenRectangle
get "active"(): boolean
get "tabOrderGroup"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractWidget$Type = ($AbstractWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractWidget_ = $AbstractWidget$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferStorageFlags" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EnumBit, $EnumBit$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBit"

export class $GlBufferStorageFlags extends $Enum<($GlBufferStorageFlags)> implements $EnumBit {
static readonly "PERSISTENT": $GlBufferStorageFlags
static readonly "MAP_READ": $GlBufferStorageFlags
static readonly "MAP_WRITE": $GlBufferStorageFlags
static readonly "CLIENT_STORAGE": $GlBufferStorageFlags
static readonly "COHERENT": $GlBufferStorageFlags


public static "values"(): ($GlBufferStorageFlags)[]
public static "valueOf"(arg0: string): $GlBufferStorageFlags
public "getBits"(): integer
get "bits"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferStorageFlags$Type = (("map_write") | ("map_read") | ("client_storage") | ("coherent") | ("persistent")) | ($GlBufferStorageFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferStorageFlags_ = $GlBufferStorageFlags$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/$TrackedBlockStatePredicate" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AtomicBoolean, $AtomicBoolean$Type} from "packages/java/util/concurrent/atomic/$AtomicBoolean"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $TrackedBlockStatePredicate implements $Predicate<($BlockState)> {
static readonly "FULLY_INITIALIZED": $AtomicBoolean

constructor(index: integer)

public "getIndex"(): integer
public "test"(arg0: $BlockState$Type): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public "negate"(): $Predicate<($BlockState)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public static "isEqual"<T>(arg0: any): $Predicate<($BlockState)>
get "index"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrackedBlockStatePredicate$Type = ($TrackedBlockStatePredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrackedBlockStatePredicate_ = $TrackedBlockStatePredicate$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$PositionedEntityTrackingSection" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PositionedEntityTrackingSection {

 "setPos"(arg0: long): void
 "getPos"(): long
}

export namespace $PositionedEntityTrackingSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PositionedEntityTrackingSection$Type = ($PositionedEntityTrackingSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PositionedEntityTrackingSection_ = $PositionedEntityTrackingSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlElement" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ControlElement<T> extends $AbstractWidget {

constructor(arg0: $Option$Type<(T)>, arg1: $Dim2i$Type)

public "getDimensions"(): $Dim2i
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRectangle"(): $ScreenRectangle
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getOption"(): $Option<(T)>
get "dimensions"(): $Dim2i
get "rectangle"(): $ScreenRectangle
get "option"(): $Option<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ControlElement$Type<T> = ($ControlElement<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ControlElement_<T> = $ControlElement$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$GlVertexAttribute, $GlVertexAttribute$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttribute"
import {$GlVertexFormat$Builder, $GlVertexFormat$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat$Builder"
import {$EnumMap, $EnumMap$Type} from "packages/java/util/$EnumMap"

export class $GlVertexFormat<T extends $Enum<(T)>> {

constructor(arg0: $Class$Type<(T)>, arg1: $EnumMap$Type<(T), ($GlVertexAttribute$Type)>, arg2: integer)

public "toString"(): string
public static "builder"<T extends $Enum<(T)>>(arg0: $Class$Type<(T)>, arg1: integer): $GlVertexFormat$Builder<(T)>
public "getAttribute"(arg0: T): $GlVertexAttribute
public "getStride"(): integer
get "stride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexFormat$Type<T> = ($GlVertexFormat<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexFormat_<T> = $GlVertexFormat$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPrompt$Action" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ScreenPrompt$Action extends $Record {

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
export type $ScreenPrompt$Action$Type = ($ScreenPrompt$Action);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenPrompt$Action_ = $ScreenPrompt$Action$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedInventoryEntityMovementTracker" {
import {$WorldSectionBox, $WorldSectionBox$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$WorldSectionBox"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$SectionedEntityMovementTracker, $SectionedEntityMovementTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementTracker"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $SectionedInventoryEntityMovementTracker<S> extends $SectionedEntityMovementTracker<($Entity), (S)> {

constructor(entityAccessBox: $WorldSectionBox$Type, clazz: $Class$Type<(S)>)

public "getEntities"(box: $AABB$Type): $List<(S)>
public static "registerAt"<S>(world: $ServerLevel$Type, interactionArea: $AABB$Type, clazz: $Class$Type<(S)>): $SectionedInventoryEntityMovementTracker<(S)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionedInventoryEntityMovementTracker$Type<S> = ($SectionedInventoryEntityMovementTracker<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionedInventoryEntityMovementTracker_<S> = $SectionedInventoryEntityMovementTracker$Type<(S)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBitField" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EnumBit, $EnumBit$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBit"

export class $EnumBitField<T extends ($Enum<(T)>) & ($EnumBit)> {


public "getBitField"(): integer
public static "of"<T extends ($Enum<(T)>) & ($EnumBit)>(...arg0: (T)[]): $EnumBitField<(T)>
public "contains"(arg0: T): boolean
get "bitField"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumBitField$Type<T> = ($EnumBitField<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumBitField_<T> = $EnumBitField$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ShaderConstants$Builder, $ShaderConstants$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants$Builder"

export class $ShaderConstants {


public static "builder"(): $ShaderConstants$Builder
public "getDefineStrings"(): $List<(string)>
get "defineStrings"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderConstants$Type = ($ShaderConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderConstants_ = $ShaderConstants$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadOrientation" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ModelQuadOrientation extends $Enum<($ModelQuadOrientation)> {
static readonly "NORMAL": $ModelQuadOrientation
static readonly "FLIP": $ModelQuadOrientation


public static "values"(): ($ModelQuadOrientation)[]
public static "valueOf"(arg0: string): $ModelQuadOrientation
public "getVertexIndex"(arg0: integer): integer
public static "orientByBrightness"(arg0: (float)[], arg1: (integer)[]): $ModelQuadOrientation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadOrientation$Type = (("normal") | ("flip")) | ($ModelQuadOrientation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadOrientation_ = $ModelQuadOrientation$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkMeshAttribute" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ChunkMeshAttribute extends $Enum<($ChunkMeshAttribute)> {
static readonly "POSITION_MATERIAL_MESH": $ChunkMeshAttribute
static readonly "COLOR_SHADE": $ChunkMeshAttribute
static readonly "BLOCK_TEXTURE": $ChunkMeshAttribute
static readonly "LIGHT_TEXTURE": $ChunkMeshAttribute


public static "values"(): ($ChunkMeshAttribute)[]
public static "valueOf"(arg0: string): $ChunkMeshAttribute
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMeshAttribute$Type = (("color_shade") | ("block_texture") | ("position_material_mesh") | ("light_texture")) | ($ChunkMeshAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMeshAttribute_ = $ChunkMeshAttribute$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$TextureUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TextureUtil {

constructor()

public static "getLightTextureId"(): integer
public static "getBlockTextureId"(): integer
get "lightTextureId"(): integer
get "blockTextureId"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureUtil$Type = ($TextureUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureUtil_ = $TextureUtil$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/color/$ColorSRGB" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ColorSRGB {

constructor()

public static "srgbToLinear"(arg0: integer): float
public static "linearToSrgb"(arg0: float, arg1: float, arg2: float, arg3: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSRGB$Type = ($ColorSRGB);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSRGB_ = $ColorSRGB$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/viewport/$CameraTransform" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CameraTransform {
readonly "intX": integer
readonly "intY": integer
readonly "intZ": integer
readonly "fracX": float
readonly "fracY": float
readonly "fracZ": float
readonly "x": double
readonly "y": double
readonly "z": double

constructor(arg0: double, arg1: double, arg2: double)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CameraTransform$Type = ($CameraTransform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CameraTransform_ = $CameraTransform$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/$WindowsCommandLine" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WindowsCommandLine {

constructor()

public static "setCommandLine"(arg0: string): void
public static "resetCommandLine"(): void
set "commandLine"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindowsCommandLine$Type = ($WindowsCommandLine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindowsCommandLine_ = $WindowsCommandLine$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/task/$CancellationToken" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CancellationToken {

 "isCancelled"(): boolean
 "setCancelled"(): void
}

export namespace $CancellationToken {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CancellationToken$Type = ($CancellationToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CancellationToken_ = $CancellationToken$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/smooth/$SmoothLightPipeline" {
import {$LightPipeline, $LightPipeline$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipeline"
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$QuadLightData, $QuadLightData$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$QuadLightData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"

export class $SmoothLightPipeline implements $LightPipeline {

constructor(arg0: $LightDataAccess$Type)

public "reset"(): void
public "calculate"(arg0: $ModelQuadView$Type, arg1: $BlockPos$Type, arg2: $QuadLightData$Type, arg3: $Direction$Type, arg4: $Direction$Type, arg5: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmoothLightPipeline$Type = ($SmoothLightPipeline);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmoothLightPipeline_ = $SmoothLightPipeline$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformFloat4v" {
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export class $GlUniformFloat4v extends $GlUniform<((float)[])> {

constructor(arg0: integer)

public "set"(arg0: (float)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniformFloat4v$Type = ($GlUniformFloat4v);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniformFloat4v_ = $GlUniformFloat4v$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/binding/$GenericBinding" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$OptionBinding, $OptionBinding$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/binding/$OptionBinding"

export class $GenericBinding<S, T> implements $OptionBinding<(S), (T)> {

constructor(arg0: $BiConsumer$Type<(S), (T)>, arg1: $Function$Type<(S), (T)>)

public "getValue"(arg0: S): T
public "setValue"(arg0: S, arg1: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericBinding$Type<S, T> = ($GenericBinding<(S), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericBinding_<S, T> = $GenericBinding$Type<(S), (T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformFloat3v" {
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export class $GlUniformFloat3v extends $GlUniform<((float)[])> {

constructor(arg0: integer)

public "set"(arg0: float, arg1: float, arg2: float): void
public "set"(arg0: (float)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniformFloat3v$Type = ($GlUniformFloat3v);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniformFloat3v_ = $GlUniformFloat3v$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/$DirectionConstants" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $DirectionConstants {
static readonly "ALL": ($Direction)[]
static readonly "VERTICAL": ($Direction)[]
static readonly "HORIZONTAL": ($Direction)[]


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionConstants$Type = ($DirectionConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionConstants_ = $DirectionConstants$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/version/$VersionInfo" {
import {$Closeable, $Closeable$Type} from "packages/java/io/$Closeable"
import {$LanguageCodePage, $LanguageCodePage$Type} from "packages/me/jellysquid/mods/sodium/client/platform/windows/api/version/$LanguageCodePage"

export class $VersionInfo implements $Closeable {


public "close"(): void
public static "allocate"(arg0: integer): $VersionInfo
public "queryValue"(arg0: string, arg1: $LanguageCodePage$Type): string
public "queryEnglishTranslation"(): $LanguageCodePage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VersionInfo$Type = ($VersionInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VersionInfo_ = $VersionInfo$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compat/forge/$ForgeBlockRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$BlockOcclusionCache, $BlockOcclusionCache$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockOcclusionCache"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$LightMode, $LightMode$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightMode"
import {$BlockRenderContext, $BlockRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ChunkModelBuilder, $ChunkModelBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/buffers/$ChunkModelBuilder"

export class $ForgeBlockRenderer {

constructor()

public static "useForgeLightingPipeline"(): boolean
public static "init"(): void
public "renderBlock"(arg0: $LightMode$Type, arg1: $BlockRenderContext$Type, arg2: $VertexConsumer$Type, arg3: $PoseStack$Type, arg4: $RandomSource$Type, arg5: $BlockOcclusionCache$Type, arg6: $ChunkModelBuilder$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBlockRenderer$Type = ($ForgeBlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBlockRenderer_ = $ForgeBlockRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlShader" {
import {$ShaderType, $ShaderType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderType"
import {$GlObject, $GlObject$Type} from "packages/me/jellysquid/mods/sodium/client/gl/$GlObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GlShader extends $GlObject {

constructor(arg0: $ShaderType$Type, arg1: $ResourceLocation$Type, arg2: string)

public "getName"(): $ResourceLocation
public "delete"(): void
get "name"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlShader$Type = ($GlShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlShader_ = $GlShader$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/msgbox/$MsgBoxCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $MsgBoxCallbackI extends $CallbackI {

 "invoke"(arg0: long): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long): void
}

export namespace $MsgBoxCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MsgBoxCallbackI$Type = ($MsgBoxCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MsgBoxCallbackI_ = $MsgBoxCallbackI$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/checks/$EarlyDriverScanner" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EarlyDriverScanner {

constructor()

public static "scanDrivers"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EarlyDriverScanner$Type = ($EarlyDriverScanner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EarlyDriverScanner_ = $EarlyDriverScanner$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/data/fingerprint/$HashedFingerprint" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $HashedFingerprint extends $Record {
static readonly "CURRENT_VERSION": integer

constructor(version: integer, saltHex: string, uuidHashHex: string, pathHashHex: string, timestamp: long)

public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): integer
public "hashCode"(): integer
public "timestamp"(): long
public static "loadFromDisk"(): $HashedFingerprint
public static "writeToDisk"(arg0: $HashedFingerprint$Type): void
public "uuidHashHex"(): string
public "saltHex"(): string
public "pathHashHex"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HashedFingerprint$Type = ($HashedFingerprint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HashedFingerprint_ = $HashedFingerprint$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildOutput" {
import {$BuiltSectionMeshParts, $BuiltSectionMeshParts$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionMeshParts"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$BuiltSectionInfo, $BuiltSectionInfo$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ChunkBuildOutput {
readonly "render": $RenderSection
readonly "info": $BuiltSectionInfo
readonly "meshes": $Map<($TerrainRenderPass), ($BuiltSectionMeshParts)>
readonly "buildTime": integer

constructor(arg0: $RenderSection$Type, arg1: $BuiltSectionInfo$Type, arg2: $Map$Type<($TerrainRenderPass$Type), ($BuiltSectionMeshParts$Type)>, arg3: integer)

public "delete"(): void
public "isIndexOnlyUpload"(): boolean
public "getMesh"(arg0: $TerrainRenderPass$Type): $BuiltSectionMeshParts
public "setIndexOnlyUpload"(arg0: boolean): void
get "indexOnlyUpload"(): boolean
set "indexOnlyUpload"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuildOutput$Type = ($ChunkBuildOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuildOutput_ = $ChunkBuildOutput$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer" {
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $StagingBuffer {

 "flush"(arg0: $CommandList$Type): void
 "delete"(arg0: $CommandList$Type): void
 "flip"(): void
 "enqueueCopy"(arg0: $CommandList$Type, arg1: $ByteBuffer$Type, arg2: $GlBuffer$Type, arg3: long): void
}

export namespace $StagingBuffer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StagingBuffer$Type = ($StagingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StagingBuffer_ = $StagingBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass" {
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $TerrainRenderPass {

constructor(arg0: $RenderType$Type, arg1: boolean, arg2: boolean)

public "supportsFragmentDiscard"(): boolean
public "isSorted"(): boolean
/**
 * 
 * @deprecated
 */
public "endDrawing"(): void
/**
 * 
 * @deprecated
 */
public "startDrawing"(): void
public "isReverseOrder"(): boolean
get "sorted"(): boolean
get "reverseOrder"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TerrainRenderPass$Type = ($TerrainRenderPass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TerrainRenderPass_ = $TerrainRenderPass$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/console/$ConsoleHooks" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ConsoleHooks {

constructor()

public static "render"(arg0: $GuiGraphics$Type, arg1: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConsoleHooks$Type = ($ConsoleHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConsoleHooks_ = $ConsoleHooks$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$Material" {
import {$AlphaCutoffParameter, $AlphaCutoffParameter$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/parameters/$AlphaCutoffParameter"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"

export class $Material {
readonly "pass": $TerrainRenderPass
readonly "packed": integer
readonly "alphaCutoff": $AlphaCutoffParameter
readonly "mipped": boolean

constructor(arg0: $TerrainRenderPass$Type, arg1: $AlphaCutoffParameter$Type, arg2: boolean)

public "bits"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Material$Type = ($Material);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Material_ = $Material$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/functions/$DeviceFunctions" {
import {$BufferStorageFunctions, $BufferStorageFunctions$Type} from "packages/me/jellysquid/mods/sodium/client/gl/functions/$BufferStorageFunctions"
import {$RenderDevice, $RenderDevice$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice"

export class $DeviceFunctions {

constructor(arg0: $RenderDevice$Type)

public "getBufferStorageFunctions"(): $BufferStorageFunctions
get "bufferStorageFunctions"(): $BufferStorageFunctions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeviceFunctions$Type = ($DeviceFunctions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeviceFunctions_ = $DeviceFunctions$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/workarounds/nvidia/$NvidiaWorkarounds" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NvidiaWorkarounds {

constructor()

public static "install"(): void
public static "uninstall"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NvidiaWorkarounds$Type = ($NvidiaWorkarounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NvidiaWorkarounds_ = $NvidiaWorkarounds$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/blockview/$SingleBlockBlockView" {
import {$ModelDataManager, $ModelDataManager$Type} from "packages/net/minecraftforge/client/model/data/$ModelDataManager"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ClipContext, $ClipContext$Type} from "packages/net/minecraft/world/level/$ClipContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$WorldBorder, $WorldBorder$Type} from "packages/net/minecraft/world/level/border/$WorldBorder"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$ClipBlockStateContext, $ClipBlockStateContext$Type} from "packages/net/minecraft/world/level/$ClipBlockStateContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$CollisionGetter, $CollisionGetter$Type} from "packages/net/minecraft/world/level/$CollisionGetter"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $SingleBlockBlockView extends $Record implements $BlockGetter, $CollisionGetter {

constructor(state: $BlockState$Type, blockPos: $BlockPos$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(blockState: $BlockState$Type, blockPos: $BlockPos$Type): $SingleBlockBlockView
public "state"(): $BlockState
public "getBlockEntity"(pos: $BlockPos$Type): $BlockEntity
public "getFluidState"(pos: $BlockPos$Type): $FluidState
public "noCollision"(box: $AABB$Type): boolean
public "isUnobstructed"(entity: $Entity$Type): boolean
public "isUnobstructed"(state: $BlockState$Type, pos: $BlockPos$Type, context: $CollisionContext$Type): boolean
public "getBlockCollisions"(entity: $Entity$Type, box: $AABB$Type): $Iterable<($VoxelShape)>
public "getChunkForCollisions"(chunkX: integer, chunkZ: integer): $BlockGetter
public "getHeight"(): integer
public "isUnobstructed"(except: $Entity$Type, shape: $VoxelShape$Type): boolean
public "getEntityCollisions"(entity: $Entity$Type, box: $AABB$Type): $List<($VoxelShape)>
public "noCollision"(entity: $Entity$Type, box: $AABB$Type): boolean
public "getMinBuildHeight"(): integer
public "noCollision"(entity: $Entity$Type): boolean
public "getBlockState"(pos: $BlockPos$Type): $BlockState
public "getWorldBorder"(): $WorldBorder
public "getCollisions"(entity: $Entity$Type, box: $AABB$Type): $Iterable<($VoxelShape)>
public "blockPos"(): $BlockPos
public "findFreePosition"(entity: $Entity$Type, shape: $VoxelShape$Type, target: $Vec3$Type, x: double, y: double, z: double): $Optional<($Vec3)>
public "collidesWithSuffocatingBlock"(entity: $Entity$Type, box: $AABB$Type): boolean
public "getBlockEntity"<T extends $BlockEntity>(arg0: $BlockPos$Type, arg1: $BlockEntityType$Type<(T)>): $Optional<(T)>
public "getBlockStates"(arg0: $AABB$Type): $Stream<($BlockState)>
public "getMaxLightLevel"(): integer
public "isBlockInLine"(arg0: $ClipBlockStateContext$Type): $BlockHitResult
public "clipWithInteractionOverride"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $BlockPos$Type, arg3: $VoxelShape$Type, arg4: $BlockState$Type): $BlockHitResult
public "getLightEmission"(arg0: $BlockPos$Type): integer
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public "getBlockFloorHeight"(arg0: $VoxelShape$Type, arg1: $Supplier$Type<($VoxelShape$Type)>): double
public "getBlockFloorHeight"(arg0: $BlockPos$Type): double
public "clip"(arg0: $ClipContext$Type): $BlockHitResult
public "findSupportingBlock"(arg0: $Entity$Type, arg1: $AABB$Type): $Optional<($BlockPos)>
public "getSectionsCount"(): integer
public "isOutsideBuildHeight"(arg0: integer): boolean
public "getMinSection"(): integer
public "getMaxSection"(): integer
public "getSectionIndexFromSectionY"(arg0: integer): integer
public "getSectionYFromSectionIndex"(arg0: integer): integer
public "getSectionIndex"(arg0: integer): integer
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
public "isOutsideBuildHeight"(arg0: $BlockPos$Type): boolean
public "getMaxBuildHeight"(): integer
public "getExistingBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getModelDataManager"(): $ModelDataManager
get "height"(): integer
get "minBuildHeight"(): integer
get "worldBorder"(): $WorldBorder
get "maxLightLevel"(): integer
get "sectionsCount"(): integer
get "minSection"(): integer
get "maxSection"(): integer
get "maxBuildHeight"(): integer
get "modelDataManager"(): $ModelDataManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingleBlockBlockView$Type = ($SingleBlockBlockView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingleBlockBlockView_ = $SingleBlockBlockView$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$ModelPartData" {
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$ModelCuboid, $ModelCuboid$Type} from "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$ModelCuboid"

export interface $ModelPartData {

 "isHidden"(): boolean
/**
 * 
 * @deprecated
 */
 "getChildren"(): ($ModelPart)[]
/**
 * 
 * @deprecated
 */
 "getCuboids"(): ($ModelCuboid)[]
 "isVisible"(): boolean
}

export namespace $ModelPartData {
function from(arg0: $ModelPart$Type): $ModelPartData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelPartData$Type = ($ModelPartData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelPartData_ = $ModelPartData$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadViewMutable" {
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $ModelQuadViewMutable extends $ModelQuadView {

 "setColor"(arg0: integer, arg1: integer): void
 "setFlags"(arg0: integer): void
 "setX"(arg0: integer, arg1: float): void
 "setY"(arg0: integer, arg1: float): void
 "setZ"(arg0: integer, arg1: float): void
 "setLightFace"(arg0: $Direction$Type): void
 "setTexU"(arg0: integer, arg1: float): void
 "setTexV"(arg0: integer, arg1: float): void
 "setColorIndex"(arg0: integer): void
 "setLight"(arg0: integer, arg1: integer): void
 "setHasAmbientOcclusion"(arg0: boolean): void
 "setSprite"(arg0: $TextureAtlasSprite$Type): void
 "getFlags"(): integer
 "getY"(arg0: integer): float
 "getColorIndex"(): integer
 "getSprite"(): $TextureAtlasSprite
 "getLightFace"(): $Direction
 "getForgeNormal"(arg0: integer): integer
 "getLight"(arg0: integer): integer
 "getTexV"(arg0: integer): float
 "getTexU"(arg0: integer): float
 "hasAmbientOcclusion"(): boolean
 "getZ"(arg0: integer): float
 "getX"(arg0: integer): float
 "getColor"(arg0: integer): integer
 "hasColor"(): boolean
}

export namespace $ModelQuadViewMutable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadViewMutable$Type = ($ModelQuadViewMutable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadViewMutable_ = $ModelQuadViewMutable$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlPrimitiveType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GlPrimitiveType extends $Enum<($GlPrimitiveType)> {
static readonly "TRIANGLES": $GlPrimitiveType


public static "values"(): ($GlPrimitiveType)[]
public static "valueOf"(arg0: string): $GlPrimitiveType
public "getId"(): integer
get "id"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlPrimitiveType$Type = (("triangles")) | ($GlPrimitiveType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlPrimitiveType_ = $GlPrimitiveType$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compat/ccl/$SinkingVertexBuilder" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$ChunkModelBuilder, $ChunkModelBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/buffers/$ChunkModelBuilder"
import {$Material, $Material$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$Material"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $SinkingVertexBuilder implements $VertexConsumer {

constructor()

public "flush"(arg0: $ChunkModelBuilder$Type, arg1: $Material$Type, arg2: float, arg3: float, arg4: float): boolean
public "flush"(arg0: $ChunkModelBuilder$Type, arg1: $Material$Type, arg2: $Vector3fc$Type): boolean
public "isEmpty"(): boolean
public "reset"(): void
public "vertex"(arg0: double, arg1: double, arg2: double): $VertexConsumer
public "uv"(arg0: float, arg1: float): $VertexConsumer
public "color"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $VertexConsumer
public "normal"(arg0: float, arg1: float, arg2: float): $VertexConsumer
public "overlayCoords"(arg0: integer, arg1: integer): $VertexConsumer
public "uv2"(arg0: integer, arg1: integer): $VertexConsumer
public "defaultColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "unsetDefaultColor"(): void
public "endVertex"(): void
public "overlayCoords"(arg0: integer): $VertexConsumer
public "uv2"(arg0: integer): $VertexConsumer
public "vertex"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: integer, arg10: integer, arg11: float, arg12: float, arg13: float): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: float, arg7: (integer)[], arg8: integer, arg9: boolean): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: (integer)[], arg7: integer, arg8: boolean): void
public "color"(arg0: integer): $VertexConsumer
public "normal"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "vertex"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "color"(arg0: float, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer, arg8: boolean): void
public "applyBakedLighting"(arg0: integer, arg1: $ByteBuffer$Type): integer
public "applyBakedNormals"(arg0: $Vector3f$Type, arg1: $ByteBuffer$Type, arg2: $Matrix3f$Type): void
public "misc"(arg0: $VertexFormatElement$Type, ...arg1: (integer)[]): $VertexConsumer
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinkingVertexBuilder$Type = ($SinkingVertexBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinkingVertexBuilder_ = $SinkingVertexBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$BuiltSectionMeshParts, $BuiltSectionMeshParts$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionMeshParts"
import {$ChunkModelBuilder, $ChunkModelBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/buffers/$ChunkModelBuilder"
import {$Material, $Material$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$Material"
import {$BuiltSectionInfo$Builder, $BuiltSectionInfo$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo$Builder"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"

export class $ChunkBuildBuffers {

constructor(arg0: $ChunkVertexType$Type)

public "get"(arg0: $Material$Type): $ChunkModelBuilder
public "get"(arg0: $TerrainRenderPass$Type): $ChunkModelBuilder
public "init"(arg0: $BuiltSectionInfo$Builder$Type, arg1: integer): void
public "destroy"(): void
public "createMesh"(arg0: $TerrainRenderPass$Type): $BuiltSectionMeshParts
public "getVertexType"(): $ChunkVertexType
get "vertexType"(): $ChunkVertexType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuildBuffers$Type = ($ChunkBuildBuffers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuildBuffers_ = $ChunkBuildBuffers$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockOcclusionCache" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockOcclusionCache {

constructor()

public "shouldDrawSide"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockOcclusionCache$Type = ($BlockOcclusionCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockOcclusionCache_ = $BlockOcclusionCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/$WorldRendererExtended" {
import {$SodiumWorldRenderer, $SodiumWorldRenderer$Type} from "packages/me/jellysquid/mods/sodium/client/render/$SodiumWorldRenderer"

export interface $WorldRendererExtended {

 "sodium$getWorldRenderer"(): $SodiumWorldRenderer

(): $SodiumWorldRenderer
}

export namespace $WorldRendererExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldRendererExtended$Type = ($WorldRendererExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldRendererExtended_ = $WorldRendererExtended$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$BitwiseMath" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BitwiseMath {

constructor()

public static "greaterThan"(arg0: integer, arg1: integer): integer
public static "lessThan"(arg0: integer, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BitwiseMath$Type = ($BitwiseMath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BitwiseMath_ = $BitwiseMath$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/texture/$SpriteContentsExtended" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SpriteContentsExtended {

 "sodium$isActive"(): boolean
 "sodium$setActive"(arg0: boolean): void
 "sodium$hasAnimation"(): boolean
}

export namespace $SpriteContentsExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsExtended$Type = ($SpriteContentsExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsExtended_ = $SpriteContentsExtended$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/checks/$LateDriverScanner" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LateDriverScanner {

constructor()

public static "onContextInitialized"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LateDriverScanner$Type = ($LateDriverScanner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LateDriverScanner_ = $LateDriverScanner$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListener" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Range6Int, $Range6Int$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$Range6Int"
import {$ClassInstanceMultiMap, $ClassInstanceMultiMap$Type} from "packages/net/minecraft/util/$ClassInstanceMultiMap"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $NearbyEntityListener {

 "onSectionEnteredRange"<T>(entityTrackingSection: any, collection: $ClassInstanceMultiMap$Type<(T)>): void
 "getEntityClass"(): $Class<(any)>
 "getChunkRange"(): $Range6Int
 "onEntityLeftRange"(arg0: $Entity$Type): void
 "removeFromAllChunksInRange"(entityCache: $EntitySectionStorage$Type<(any)>, prevCenterPos: $SectionPos$Type): void
 "onEntityEnteredRange"(arg0: $Entity$Type): void
 "updateChunkRegistrations"(entityCache: $EntitySectionStorage$Type<(any)>, prevCenterPos: $SectionPos$Type, prevChunkRange: $Range6Int$Type, newCenterPos: $SectionPos$Type, newChunkRange: $Range6Int$Type): void
 "addToAllChunksInRange"(entityCache: $EntitySectionStorage$Type<(any)>, newCenterPos: $SectionPos$Type): void
 "onSectionLeftRange"<T>(entityTrackingSection: any, collection: $ClassInstanceMultiMap$Type<(T)>): void
}

export namespace $NearbyEntityListener {
const EMPTY_RANGE: $Range6Int
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NearbyEntityListener$Type = ($NearbyEntityListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NearbyEntityListener_ = $NearbyEntityListener$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/animations/upload/$SpriteContentsAnimationAccessor" {
import {$SpriteContents$FrameInfo, $SpriteContents$FrameInfo$Type} from "packages/net/minecraft/client/renderer/texture/$SpriteContents$FrameInfo"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $SpriteContentsAnimationAccessor {

 "getFrames"(): $List<($SpriteContents$FrameInfo)>
 "getFrameRowSize"(): integer
}

export namespace $SpriteContentsAnimationAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsAnimationAccessor$Type = ($SpriteContentsAnimationAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsAnimationAccessor_ = $SpriteContentsAnimationAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$ReferenceMaskedList" {
import {$ReferenceArrayList, $ReferenceArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ReferenceArrayList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $ReferenceMaskedList<E> extends $AbstractList<(E)> {

constructor(allElements: $ReferenceArrayList$Type<(E)>, defaultVisibility: boolean)
constructor()

public "add"(e: E): boolean
public "remove"(o: any): boolean
public "get"(index: integer): E
public "size"(): integer
public "iterator"(): $Iterator<(E)>
public "spliterator"(): $Spliterator<(E)>
public "totalSize"(): integer
public "addOrSet"(element: E, visible: boolean): void
public "setVisible"(element: E, visible: boolean): void
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
export type $ReferenceMaskedList$Type<E> = ($ReferenceMaskedList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReferenceMaskedList_<E> = $ReferenceMaskedList$Type<(E)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/$PendingBufferCopyCommand" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PendingBufferCopyCommand {
readonly "readOffset": integer
readonly "writeOffset": integer
 "length": integer


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PendingBufferCopyCommand$Type = ($PendingBufferCopyCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PendingBufferCopyCommand_ = $PendingBufferCopyCommand$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPrompt" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$ScreenPromptable, $ScreenPromptable$Type} from "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPromptable"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$ScreenPrompt$Action, $ScreenPrompt$Action$Type} from "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPrompt$Action"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

/**
 * 
 * @deprecated
 */
export class $ScreenPrompt implements $GuiEventListener, $Renderable {

constructor(arg0: $ScreenPromptable$Type, arg1: $List$Type<($FormattedText$Type)>, arg2: integer, arg3: integer, arg4: $ScreenPrompt$Action$Type)

public "init"(): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "setFocused"(arg0: boolean): void
public "getWidgets"(): $List<($AbstractWidget)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "isFocused"(): boolean
public "getCurrentFocusPath"(): $ComponentPath
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "isMouseOver"(arg0: double, arg1: double): boolean
public "getRectangle"(): $ScreenRectangle
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "getTabOrderGroup"(): integer
set "focused"(value: boolean)
get "widgets"(): $List<($AbstractWidget)>
get "focused"(): boolean
get "currentFocusPath"(): $ComponentPath
get "rectangle"(): $ScreenRectangle
get "tabOrderGroup"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenPrompt$Type = ($ScreenPrompt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenPrompt_ = $ScreenPrompt$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/$BlockListeningSection" {
import {$ListeningBlockStatePredicate, $ListeningBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$ListeningBlockStatePredicate"
import {$SectionedBlockChangeTracker, $SectionedBlockChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$SectionedBlockChangeTracker"

export interface $BlockListeningSection {

 "addToCallback"(arg0: $ListeningBlockStatePredicate$Type, arg1: $SectionedBlockChangeTracker$Type): void
 "removeFromCallback"(arg0: $ListeningBlockStatePredicate$Type, arg1: $SectionedBlockChangeTracker$Type): void
}

export namespace $BlockListeningSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockListeningSection$Type = ($BlockListeningSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockListeningSection_ = $BlockListeningSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/workarounds/$Workarounds" {
import {$Workarounds$Reference, $Workarounds$Reference$Type} from "packages/me/jellysquid/mods/sodium/client/compatibility/workarounds/$Workarounds$Reference"

export class $Workarounds {

constructor()

public static "init"(): void
public static "isWorkaroundEnabled"(arg0: $Workarounds$Reference$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Workarounds$Type = ($Workarounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Workarounds_ = $Workarounds$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/$WindowsDriverStoreVersion" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $WindowsDriverStoreVersion extends $Record {

constructor(driverModel: integer, featureLevel: integer, major: integer, minor: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "major"(): integer
public "minor"(): integer
public static "parse"(arg0: string): $WindowsDriverStoreVersion
public "featureLevel"(): integer
public "driverModel"(): integer
public "getFriendlyString"(): string
get "friendlyString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindowsDriverStoreVersion$Type = ($WindowsDriverStoreVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindowsDriverStoreVersion_ = $WindowsDriverStoreVersion$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/environment/probe/$GraphicsAdapterVendor" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GraphicsAdapterVendor extends $Enum<($GraphicsAdapterVendor)> {
static readonly "NVIDIA": $GraphicsAdapterVendor
static readonly "AMD": $GraphicsAdapterVendor
static readonly "INTEL": $GraphicsAdapterVendor
static readonly "UNKNOWN": $GraphicsAdapterVendor


public static "values"(): ($GraphicsAdapterVendor)[]
public static "valueOf"(arg0: string): $GraphicsAdapterVendor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsAdapterVendor$Type = (("amd") | ("intel") | ("nvidia") | ("unknown")) | ($GraphicsAdapterVendor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsAdapterVendor_ = $GraphicsAdapterVendor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatRegistry" {
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatDescription"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

/**
 * 
 * @deprecated
 */
export class $VertexFormatRegistry {

constructor()

public static "get"(arg0: $VertexFormat$Type): $VertexFormatDescription
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexFormatRegistry$Type = ($VertexFormatRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexFormatRegistry_ = $VertexFormatRegistry$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/buffers/$ChunkModelBuilder" {
import {$ModelQuadFacing, $ModelQuadFacing$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadFacing"
import {$ChunkMeshBufferBuilder, $ChunkMeshBufferBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/builder/$ChunkMeshBufferBuilder"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $ChunkModelBuilder {

 "getVertexBuffer"(arg0: $ModelQuadFacing$Type): $ChunkMeshBufferBuilder
 "addSprite"(arg0: $TextureAtlasSprite$Type): void
}

export namespace $ChunkModelBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkModelBuilder$Type = ($ChunkModelBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkModelBuilder_ = $ChunkModelBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$FluidRenderer" {
import {$LightPipelineProvider, $LightPipelineProvider$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipelineProvider"
import {$WorldSlice, $WorldSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/$WorldSlice"
import {$ColorProviderRegistry, $ColorProviderRegistry$Type} from "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProviderRegistry"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ChunkBuildBuffers, $ChunkBuildBuffers$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $FluidRenderer {

constructor(arg0: $ColorProviderRegistry$Type, arg1: $LightPipelineProvider$Type)

public "render"(arg0: $WorldSlice$Type, arg1: $FluidState$Type, arg2: $BlockPos$Type, arg3: $BlockPos$Type, arg4: $ChunkBuildBuffers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidRenderer$Type = ($FluidRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidRenderer_ = $FluidRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpact" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$TextProvider, $TextProvider$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$TextProvider"

export class $OptionImpact extends $Enum<($OptionImpact)> implements $TextProvider {
static readonly "LOW": $OptionImpact
static readonly "MEDIUM": $OptionImpact
static readonly "HIGH": $OptionImpact
static readonly "VARIES": $OptionImpact


public static "values"(): ($OptionImpact)[]
public static "valueOf"(arg0: string): $OptionImpact
public "getLocalizedName"(): $Component
get "localizedName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionImpact$Type = (("high") | ("low") | ("varies") | ("medium")) | ($OptionImpact);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionImpact_ = $OptionImpact$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/device/$DrawCommandList" {
import {$GlIndexType, $GlIndexType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlIndexType"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$MultiDrawBatch, $MultiDrawBatch$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$MultiDrawBatch"

export interface $DrawCommandList extends $AutoCloseable {

 "endTessellating"(): void
 "flush"(): void
 "close"(): void
 "multiDrawElementsBaseVertex"(arg0: $MultiDrawBatch$Type, arg1: $GlIndexType$Type): void
}

export namespace $DrawCommandList {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawCommandList$Type = ($DrawCommandList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawCommandList_ = $DrawCommandList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer" {
import {$GlObject, $GlObject$Type} from "packages/me/jellysquid/mods/sodium/client/gl/$GlObject"
import {$GlBufferMapping, $GlBufferMapping$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferMapping"

export class $GlBuffer extends $GlObject {


public "getActiveMapping"(): $GlBufferMapping
public "setActiveMapping"(arg0: $GlBufferMapping$Type): void
get "activeMapping"(): $GlBufferMapping
set "activeMapping"(value: $GlBufferMapping$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBuffer$Type = ($GlBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBuffer_ = $GlBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$TextProvider" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export interface $TextProvider {

 "getLocalizedName"(): $Component

(): $Component
}

export namespace $TextProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextProvider$Type = ($TextProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextProvider_ = $TextProvider$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$NotificationSettings" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SodiumGameOptions$NotificationSettings {
 "forceDisableDonationPrompts": boolean
 "hasClearedDonationButton": boolean
 "hasSeenDonationPrompt": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptions$NotificationSettings$Type = ($SodiumGameOptions$NotificationSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptions$NotificationSettings_ = $SodiumGameOptions$NotificationSettings$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$QualitySettings" {
import {$SodiumGameOptions$GraphicsQuality, $SodiumGameOptions$GraphicsQuality$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$GraphicsQuality"

export class $SodiumGameOptions$QualitySettings {
 "weatherQuality": $SodiumGameOptions$GraphicsQuality
 "leavesQuality": $SodiumGameOptions$GraphicsQuality
 "enableVignette": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptions$QualitySettings$Type = ($SodiumGameOptions$QualitySettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptions$QualitySettings_ = $SodiumGameOptions$QualitySettings$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeAlignedCuboid" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$VoxelShapeSimpleCube, $VoxelShapeSimpleCube$Type} from "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeSimpleCube"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"
import {$AxisCycle, $AxisCycle$Type} from "packages/net/minecraft/core/$AxisCycle"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $VoxelShapeAlignedCuboid extends $VoxelShapeSimpleCube {
readonly "isTiny": boolean
 "shape": $DiscreteVoxelShape

constructor(minX: double, minY: double, minZ: double, maxX: double, maxY: double, maxZ: double, xRes: integer, yRes: integer, zRes: integer)
constructor(voxels: $DiscreteVoxelShape$Type, xSegments: integer, ySegments: integer, zSegments: integer, minX: double, minY: double, minZ: double, maxX: double, maxY: double, maxZ: double)

public "getCoords"(axis: $Direction$Axis$Type): $DoubleList
public "collideX"(cycleDirection: $AxisCycle$Type, box: $AABB$Type, maxDist: double): double
public "move"(x: double, y: double, z: double): $VoxelShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeAlignedCuboid$Type = ($VoxelShapeAlignedCuboid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeAlignedCuboid_ = $VoxelShapeAlignedCuboid$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$PredicateFilterableList$PredicateFilteredList" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"

export class $PredicateFilterableList$PredicateFilteredList extends $AbstractList<(T)> {


public "get"(index: integer): T
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
export type $PredicateFilterableList$PredicateFilteredList$Type = ($PredicateFilterableList$PredicateFilteredList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PredicateFilterableList$PredicateFilteredList_ = $PredicateFilterableList$PredicateFilteredList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlProgram" {
import {$GlProgram$Builder, $GlProgram$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$GlProgram$Builder"
import {$GlObject, $GlObject$Type} from "packages/me/jellysquid/mods/sodium/client/gl/$GlObject"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$GlUniformBlock, $GlUniformBlock$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformBlock"
import {$ShaderBindingContext, $ShaderBindingContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ShaderBindingContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export class $GlProgram<T> extends $GlObject implements $ShaderBindingContext {


public static "builder"(arg0: $ResourceLocation$Type): $GlProgram$Builder
public "delete"(): void
public "bind"(): void
public "getInterface"(): T
public "unbind"(): void
public "bindUniformBlock"(arg0: string, arg1: integer): $GlUniformBlock
public "bindUniform"<U extends $GlUniform<(any)>>(arg0: string, arg1: $IntFunction$Type<(U)>): U
get "interface"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlProgram$Type<T> = ($GlProgram<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlProgram_<T> = $GlProgram$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttributeBinding" {
import {$GlVertexAttribute, $GlVertexAttribute$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttribute"

export class $GlVertexAttributeBinding extends $GlVertexAttribute {

constructor(arg0: integer, arg1: $GlVertexAttribute$Type)

public "getIndex"(): integer
get "index"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexAttributeBinding$Type = ($GlVertexAttributeBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexAttributeBinding_ = $GlVertexAttributeBinding$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/compat/worldedit/$WorldEditCompat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WorldEditCompat {
static readonly "WORLD_EDIT_PRESENT": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEditCompat$Type = ($WorldEditCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEditCompat_ = $WorldEditCompat$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$EntityClassGroup" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $EntityClassGroup {
static readonly "MINECART_BOAT_LIKE_COLLISION": $EntityClassGroup

constructor(classFitEvaluator: $Predicate$Type<($Class$Type<(any)>)>)

public "clear"(): void
public "contains"(entityClass: $Class$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityClassGroup$Type = ($EntityClassGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityClassGroup_ = $EntityClassGroup$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/serializers/$VertexSerializerRegistryImpl" {
import {$VertexSerializerRegistry, $VertexSerializerRegistry$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/serializer/$VertexSerializerRegistry"
import {$VertexSerializer, $VertexSerializer$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/serializer/$VertexSerializer"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $VertexSerializerRegistryImpl implements $VertexSerializerRegistry {

constructor()

public "get"(arg0: $VertexFormatDescription$Type, arg1: $VertexFormatDescription$Type): $VertexSerializer
public static "instance"(): $VertexSerializerRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexSerializerRegistryImpl$Type = ($VertexSerializerRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexSerializerRegistryImpl_ = $VertexSerializerRegistryImpl$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/$ChunkRandomSource" {
import {$BlockPos$MutableBlockPos, $BlockPos$MutableBlockPos$Type} from "packages/net/minecraft/core/$BlockPos$MutableBlockPos"

export interface $ChunkRandomSource {

 "getRandomPosInChunk"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $BlockPos$MutableBlockPos$Type): void

(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $BlockPos$MutableBlockPos$Type): void
}

export namespace $ChunkRandomSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRandomSource$Type = ($ChunkRandomSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRandomSource_ = $ChunkRandomSource$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$DirectionUtil" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $DirectionUtil {
static readonly "ALL_DIRECTIONS": ($Direction)[]
static readonly "HORIZONTAL_DIRECTIONS": ($Direction)[]

constructor()

public static "getOpposite"(arg0: $Direction$Type): $Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionUtil$Type = ($DirectionUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionUtil_ = $DirectionUtil$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/viewport/frustum/$Frustum" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Frustum {

 "testAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean

(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean
}

export namespace $Frustum {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Frustum$Type = ($Frustum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Frustum_ = $Frustum$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/$POIRegistryEntries" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"

export class $POIRegistryEntries {
static readonly "NETHER_PORTAL_ENTRY": $Holder<($PoiType)>
static readonly "HOME_ENTRY": $Holder<($PoiType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $POIRegistryEntries$Type = ($POIRegistryEntries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $POIRegistryEntries_ = $POIRegistryEntries$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumDoubleStackList" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$LithiumDoubleInventory, $LithiumDoubleInventory$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumDoubleInventory"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $LithiumDoubleStackList extends $LithiumStackList {

constructor(doubleInventory: $LithiumDoubleInventory$Type, first: $LithiumStackList$Type, second: $LithiumStackList$Type, maxCountPerStack: integer)

public "add"(slot: integer, element: $ItemStack$Type): void
public "remove"(index: integer): $ItemStack
public "get"(index: integer): $ItemStack
public "clear"(): void
public "size"(): integer
public "set"(index: integer, element: $ItemStack$Type): $ItemStack
public "changed"(): void
public static "getOrCreate"(doubleInventory: $LithiumDoubleInventory$Type, first: $LithiumStackList$Type, second: $LithiumStackList$Type, maxCountPerStack: integer): $LithiumDoubleStackList
public "getModCount"(): long
public "runComparatorUpdatePatternOnFailedExtract"(masterStackList: $LithiumStackList$Type, inventory: $Container$Type): void
public "changedALot"(): void
public "getSignalStrength"(inventory: $Container$Type): integer
public "removeInventoryModificationCallback"(inventoryModificationCallback: $InventoryChangeTracker$Type): void
public "setInventoryModificationCallback"(inventoryModificationCallback: $InventoryChangeTracker$Type): void
public "clearSignalStrengthOverride"(): void
public "setReducedSignalStrengthOverride"(): void
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
get "modCount"(): long
set "inventoryModificationCallback"(value: $InventoryChangeTracker$Type)
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumDoubleStackList$Type = ($LithiumDoubleStackList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumDoubleStackList_ = $LithiumDoubleStackList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/color/$BoxBlur" {
import {$BoxBlur$ColorBuffer, $BoxBlur$ColorBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/util/color/$BoxBlur$ColorBuffer"

export class $BoxBlur {

constructor()

/**
 * 
 * @deprecated
 */
public static "blur"(arg0: (integer)[], arg1: (integer)[], arg2: integer, arg3: integer, arg4: integer): void
public static "blur"(arg0: $BoxBlur$ColorBuffer$Type, arg1: $BoxBlur$ColorBuffer$Type, arg2: integer): void
public static "averageRGB"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoxBlur$Type = ($BoxBlur);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoxBlur_ = $BoxBlur$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/ai/pathing/$BlockStatePathingCache" {
import {$BlockPathTypes, $BlockPathTypes$Type} from "packages/net/minecraft/world/level/pathfinder/$BlockPathTypes"

export interface $BlockStatePathingCache {

 "getPathNodeType"(): $BlockPathTypes
 "getNeighborPathNodeType"(): $BlockPathTypes
}

export namespace $BlockStatePathingCache {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStatePathingCache$Type = ($BlockStatePathingCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStatePathingCache_ = $BlockStatePathingCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexBufferWriter" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$VertexBufferWriter as $VertexBufferWriter$0, $VertexBufferWriter$Type as $VertexBufferWriter$0$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter"
import {$VertexFormatDescription as $VertexFormatDescription$0, $VertexFormatDescription$Type as $VertexFormatDescription$0$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatDescription"

/**
 * 
 * @deprecated
 */
export interface $VertexBufferWriter extends $VertexBufferWriter$0 {

 "push"(arg0: $MemoryStack$Type, arg1: long, arg2: integer, arg3: $VertexFormatDescription$Type): void
/**
 * 
 * @deprecated
 */
 "push"(arg0: $MemoryStack$Type, arg1: long, arg2: integer, arg3: $VertexFormatDescription$0$Type): void
 "canUseIntrinsics"(): boolean
/**
 * 
 * @deprecated
 */
 "isFullWriter"(): boolean

(arg0: $VertexConsumer$Type): $VertexBufferWriter
}

export namespace $VertexBufferWriter {
function of(arg0: $VertexConsumer$Type): $VertexBufferWriter
function copyInto(arg0: $VertexBufferWriter$0$Type, arg1: $MemoryStack$Type, arg2: long, arg3: integer, arg4: $VertexFormatDescription$Type): void
function tryOf(arg0: $VertexConsumer$Type): $VertexBufferWriter$0
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexBufferWriter$Type = ($VertexBufferWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexBufferWriter_ = $VertexBufferWriter$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/listeners/$WorldBorderListenerOnce" {
import {$WorldBorder, $WorldBorder$Type} from "packages/net/minecraft/world/level/border/$WorldBorder"
import {$BorderChangeListener, $BorderChangeListener$Type} from "packages/net/minecraft/world/level/border/$BorderChangeListener"

export interface $WorldBorderListenerOnce extends $BorderChangeListener {

 "onBorderCenterSet"(border: $WorldBorder$Type, centerX: double, centerZ: double): void
 "onBorderSetDamageSafeZOne"(border: $WorldBorder$Type, safeZoneRadius: double): void
 "onBorderSizeLerping"(border: $WorldBorder$Type, fromSize: double, toSize: double, time: long): void
 "onBorderSetWarningTime"(border: $WorldBorder$Type, warningTime: integer): void
 "onBorderSizeSet"(border: $WorldBorder$Type, size: double): void
 "onBorderSetDamagePerBlock"(border: $WorldBorder$Type, damagePerBlock: double): void
 "onBorderSetWarningBlocks"(border: $WorldBorder$Type, warningBlockDistance: integer): void
 "onAreaReplaced"(border: $WorldBorder$Type): void
 "onWorldBorderShapeChange"(arg0: $WorldBorder$Type): void

(border: $WorldBorder$Type, centerX: double, centerZ: double): void
}

export namespace $WorldBorderListenerOnce {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldBorderListenerOnce$Type = ($WorldBorderListenerOnce);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldBorderListenerOnce_ = $WorldBorderListenerOnce$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$OffsetVoxelShapeCache" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export interface $OffsetVoxelShapeCache {

 "getOffsetSimplifiedShape"(arg0: float, arg1: $Direction$Type): $VoxelShape
 "setShape"(arg0: float, arg1: $Direction$Type, arg2: $VoxelShape$Type): void
}

export namespace $OffsetVoxelShapeCache {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OffsetVoxelShapeCache$Type = ($OffsetVoxelShapeCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OffsetVoxelShapeCache_ = $OffsetVoxelShapeCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/state/$GlStateTracker" {
import {$GlVertexArray, $GlVertexArray$Type} from "packages/me/jellysquid/mods/sodium/client/gl/array/$GlVertexArray"
import {$GlBufferTarget, $GlBufferTarget$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferTarget"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"

export class $GlStateTracker {

constructor()

public "makeBufferActive"(arg0: $GlBufferTarget$Type, arg1: $GlBuffer$Type): boolean
public "clear"(): void
public "makeVertexArrayActive"(arg0: $GlVertexArray$Type): boolean
public "notifyBufferDeleted"(arg0: $GlBuffer$Type): void
public "notifyVertexArrayDeleted"(arg0: $GlVertexArray$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlStateTracker$Type = ($GlStateTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlStateTracker_ = $GlStateTracker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/data/$ArrayLightDataCache" {
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $ArrayLightDataCache extends $LightDataAccess {

constructor(arg0: $BlockAndTintGetter$Type)

public "get"(arg0: integer, arg1: integer, arg2: integer): integer
public "reset"(arg0: $SectionPos$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayLightDataCache$Type = ($ArrayLightDataCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayLightDataCache_ = $ArrayLightDataCache$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$EntityMovementTrackerSection" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$EntityAccess, $EntityAccess$Type} from "packages/net/minecraft/world/level/entity/$EntityAccess"
import {$SectionedEntityMovementTracker, $SectionedEntityMovementTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementTracker"

export interface $EntityMovementTrackerSection {

 "listenToMovementOnce"<S, E extends $EntityAccess>(arg0: $SectionedEntityMovementTracker$Type<(E), (S)>, arg1: integer): void
 "trackEntityMovement"(arg0: integer, arg1: long): void
 "removeListenToMovementOnce"<S, E extends $EntityAccess>(arg0: $SectionedEntityMovementTracker$Type<(E), (S)>, arg1: integer): void
 "removeListener"(arg0: $EntitySectionStorage$Type<(any)>, arg1: $SectionedEntityMovementTracker$Type<(any), (any)>): void
 "addListener"(arg0: $SectionedEntityMovementTracker$Type<(any), (any)>): void
 "getChangeTime"(arg0: integer): long
}

export namespace $EntityMovementTrackerSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityMovementTrackerSection$Type = ($EntityMovementTrackerSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityMovementTrackerSection_ = $EntityMovementTrackerSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder" {
import {$Material, $Material$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$Material"
import {$ChunkVertexEncoder$Vertex, $ChunkVertexEncoder$Vertex$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder$Vertex"

export interface $ChunkVertexEncoder {

 "write"(arg0: long, arg1: $Material$Type, arg2: $ChunkVertexEncoder$Vertex$Type, arg3: integer): long

(arg0: long, arg1: $Material$Type, arg2: $ChunkVertexEncoder$Vertex$Type, arg3: integer): long
}

export namespace $ChunkVertexEncoder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkVertexEncoder$Type = ($ChunkVertexEncoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkVertexEncoder_ = $ChunkVertexEncoder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlValueFormatter" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export interface $ControlValueFormatter {

 "format"(arg0: integer): $Component

(arg0: integer): $Component
}

export namespace $ControlValueFormatter {
function number(): $ControlValueFormatter
function multiplier(): $ControlValueFormatter
function percentage(): $ControlValueFormatter
function brightness(): $ControlValueFormatter
function guiScale(): $ControlValueFormatter
function fpsLimit(): $ControlValueFormatter
function biomeBlend(): $ControlValueFormatter
function translateVariable(arg0: string): $ControlValueFormatter
function quantityOrDisabled(arg0: string, arg1: string): $ControlValueFormatter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ControlValueFormatter$Type = ($ControlValueFormatter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ControlValueFormatter_ = $ControlValueFormatter$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/$SpriteContentsInvoker" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export interface $SpriteContentsInvoker {

 "invokeUpload"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: ($NativeImage$Type)[]): void

(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: ($NativeImage$Type)[]): void
}

export namespace $SpriteContentsInvoker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsInvoker$Type = ($SpriteContentsInvoker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsInvoker_ = $SpriteContentsInvoker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$DefaultTerrainRenderPasses" {
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"

export class $DefaultTerrainRenderPasses {
static readonly "SOLID": $TerrainRenderPass
static readonly "CUTOUT": $TerrainRenderPass
static readonly "TRANSLUCENT": $TerrainRenderPass
static readonly "ALL": ($TerrainRenderPass)[]

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultTerrainRenderPasses$Type = ($DefaultTerrainRenderPasses);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultTerrainRenderPasses_ = $DefaultTerrainRenderPasses$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ChunkTracker$ChunkEventHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ChunkTracker$ChunkEventHandler {

 "apply"(arg0: integer, arg1: integer): void

(arg0: integer, arg1: integer): void
}

export namespace $ChunkTracker$ChunkEventHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkTracker$ChunkEventHandler$Type = ($ChunkTracker$ChunkEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkTracker$ChunkEventHandler_ = $ChunkTracker$ChunkEventHandler$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexConsumerUtils" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$VertexBufferWriter, $VertexBufferWriter$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter"

export class $VertexConsumerUtils {

constructor()

public static "convertOrLog"(arg0: $VertexConsumer$Type): $VertexBufferWriter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexConsumerUtils$Type = ($VertexConsumerUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexConsumerUtils_ = $VertexConsumerUtils$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $ModelQuadView {

 "getFlags"(): integer
 "getY"(arg0: integer): float
 "getColorIndex"(): integer
 "getSprite"(): $TextureAtlasSprite
 "getLightFace"(): $Direction
 "getForgeNormal"(arg0: integer): integer
 "getLight"(arg0: integer): integer
 "getTexV"(arg0: integer): float
 "getTexU"(arg0: integer): float
 "hasAmbientOcclusion"(): boolean
 "getZ"(arg0: integer): float
 "getX"(arg0: integer): float
 "getColor"(arg0: integer): integer
 "hasColor"(): boolean
}

export namespace $ModelQuadView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadView$Type = ($ModelQuadView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadView_ = $ModelQuadView$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/$MessageBox$IconType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MessageBox$IconType extends $Enum<($MessageBox$IconType)> {
static readonly "INFO": $MessageBox$IconType
static readonly "WARNING": $MessageBox$IconType
static readonly "ERROR": $MessageBox$IconType


public static "values"(): ($MessageBox$IconType)[]
public static "valueOf"(arg0: string): $MessageBox$IconType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageBox$IconType$Type = (("warning") | ("error") | ("info")) | ($MessageBox$IconType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageBox$IconType_ = $MessageBox$IconType$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/buffer/$SodiumBufferBuilder" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$ExtendedBufferBuilder, $ExtendedBufferBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/buffer/$ExtendedBufferBuilder"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$VertexBufferWriter, $VertexBufferWriter$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $SodiumBufferBuilder implements $VertexConsumer, $VertexBufferWriter {

constructor(arg0: $ExtendedBufferBuilder$Type)

public "reset"(): void
public "push"(arg0: $MemoryStack$Type, arg1: long, arg2: integer, arg3: $VertexFormatDescription$Type): void
public "vertex"(arg0: double, arg1: double, arg2: double): $VertexConsumer
public "uv"(arg0: float, arg1: float): $VertexConsumer
public "canUseIntrinsics"(): boolean
public "color"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $VertexConsumer
public "normal"(arg0: float, arg1: float, arg2: float): $VertexConsumer
public "overlayCoords"(arg0: integer, arg1: integer): $VertexConsumer
public "uv2"(arg0: integer, arg1: integer): $VertexConsumer
public "overlayCoords"(arg0: integer): $VertexConsumer
public "uv2"(arg0: integer): $VertexConsumer
public "defaultColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "unsetDefaultColor"(): void
public "vertex"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: integer, arg10: integer, arg11: float, arg12: float, arg13: float): void
public "color"(arg0: integer): $VertexConsumer
public "endVertex"(): void
public "getOriginalBufferBuilder"(): $BufferBuilder
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: float, arg7: (integer)[], arg8: integer, arg9: boolean): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: (integer)[], arg7: integer, arg8: boolean): void
public "normal"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "vertex"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "color"(arg0: float, arg1: float, arg2: float, arg3: float): $VertexConsumer
public static "of"(arg0: $VertexConsumer$Type): $VertexBufferWriter
public static "copyInto"(arg0: $VertexBufferWriter$Type, arg1: $MemoryStack$Type, arg2: long, arg3: integer, arg4: $VertexFormatDescription$Type): void
public static "tryOf"(arg0: $VertexConsumer$Type): $VertexBufferWriter
/**
 * 
 * @deprecated
 */
public "isFullWriter"(): boolean
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer, arg8: boolean): void
public "applyBakedLighting"(arg0: integer, arg1: $ByteBuffer$Type): integer
public "applyBakedNormals"(arg0: $Vector3f$Type, arg1: $ByteBuffer$Type, arg2: $Matrix3f$Type): void
public "misc"(arg0: $VertexFormatElement$Type, ...arg1: (integer)[]): $VertexConsumer
get "originalBufferBuilder"(): $BufferBuilder
get "fullWriter"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumBufferBuilder$Type = ($SodiumBufferBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumBufferBuilder_ = $SodiumBufferBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/render/gui/debug/$DebugScreenOverlayAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $DebugScreenOverlayAccessor {

 "invokeRenderLines"(arg0: $GuiGraphics$Type, arg1: $List$Type<(string)>, arg2: boolean): void

(arg0: $GuiGraphics$Type, arg1: $List$Type<(string)>, arg2: boolean): void
}

export namespace $DebugScreenOverlayAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugScreenOverlayAccessor$Type = ($DebugScreenOverlayAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugScreenOverlayAccessor_ = $DebugScreenOverlayAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/checks/$Configuration" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Configuration {
static readonly "WIN32_RTSS_HOOKS": boolean
static readonly "WIN32_DRIVER_INTEL_GEN7": boolean


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Configuration$Type = ($Configuration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Configuration_ = $Configuration$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeColorMaps" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BiomeColorMaps {

constructor()

public static "getIndex"(arg0: double, arg1: double): integer
public static "getGrassColor"(arg0: integer): integer
public static "getFoliageColor"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeColorMaps$Type = ($BiomeColorMaps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeColorMaps_ = $BiomeColorMaps$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSectionManager" {
import {$ChunkBuilderSortTask, $ChunkBuilderSortTask$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderSortTask"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$Viewport, $Viewport$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$Viewport"
import {$ChunkBuilder, $ChunkBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkBuilder"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$SortedRenderLists, $SortedRenderLists$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$SortedRenderLists"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$ChunkBuilderMeshingTask, $ChunkBuilderMeshingTask$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderMeshingTask"
import {$ChunkRenderMatrices, $ChunkRenderMatrices$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderMatrices"

export class $RenderSectionManager {

constructor(arg0: $ClientLevel$Type, arg1: integer, arg2: $CommandList$Type)

public "update"(arg0: $Camera$Type, arg1: $Viewport$Type, arg2: integer, arg3: boolean): void
public "destroy"(): void
public "getBuilder"(): $ChunkBuilder
public "markGraphDirty"(): void
public "tickVisibleRenders"(): void
public "updateChunks"(arg0: boolean): void
public "runAsyncTasks"(): void
public "uploadChunks"(): void
public "onChunkRemoved"(arg0: integer, arg1: integer): void
public "getRenderLists"(): $SortedRenderLists
public "needsUpdate"(): boolean
public "onChunkAdded"(arg0: integer, arg1: integer): void
public "isSectionVisible"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getTotalSections"(): integer
public "scheduleRebuild"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
public "getDebugStrings"(): $Collection<(string)>
public "isSectionBuilt"(arg0: integer, arg1: integer, arg2: integer): boolean
public "renderLayer"(arg0: $ChunkRenderMatrices$Type, arg1: $TerrainRenderPass$Type, arg2: double, arg3: double, arg4: double): void
public "getVisibleChunkCount"(): integer
public "getSectionsWithGlobalEntities"(): $Collection<($RenderSection)>
public "onSectionAdded"(arg0: integer, arg1: integer, arg2: integer): void
public "onSectionRemoved"(arg0: integer, arg1: integer, arg2: integer): void
public "createSortTask"(arg0: $RenderSection$Type, arg1: integer): $ChunkBuilderSortTask
public "createRebuildTask"(arg0: $RenderSection$Type, arg1: integer): $ChunkBuilderMeshingTask
public "getVertexType"(): $ChunkVertexType
get "builder"(): $ChunkBuilder
get "renderLists"(): $SortedRenderLists
get "totalSections"(): integer
get "debugStrings"(): $Collection<(string)>
get "visibleChunkCount"(): integer
get "sectionsWithGlobalEntities"(): $Collection<($RenderSection)>
get "vertexType"(): $ChunkVertexType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSectionManager$Type = ($RenderSectionManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSectionManager_ = $RenderSectionManager$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$TessellationBinding" {
import {$GlBufferTarget, $GlBufferTarget$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferTarget"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$GlVertexAttributeBinding, $GlVertexAttributeBinding$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttributeBinding"

export class $TessellationBinding extends $Record {

constructor(target: $GlBufferTarget$Type, buffer: $GlBuffer$Type, attributeBindings: ($GlVertexAttributeBinding$Type)[])

public "attributeBindings"(): ($GlVertexAttributeBinding)[]
public "equals"(arg0: any): boolean
public "target"(): $GlBufferTarget
public "toString"(): string
public "hashCode"(): integer
public "buffer"(): $GlBuffer
public static "forElementBuffer"(arg0: $GlBuffer$Type): $TessellationBinding
public static "forVertexBuffer"(arg0: $GlBuffer$Type, arg1: ($GlVertexAttributeBinding$Type)[]): $TessellationBinding
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TessellationBinding$Type = ($TessellationBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TessellationBinding_ = $TessellationBinding$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$NativeImageHelper" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $NativeImageHelper {

constructor()

public static "getPointerRGBA"(arg0: $NativeImage$Type): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NativeImageHelper$Type = ($NativeImageHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NativeImageHelper_ = $NativeImageHelper$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/state/$FastImmutableTable" {
import {$Table, $Table$Type} from "packages/com/google/common/collect/$Table"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$FastImmutableTableCache, $FastImmutableTableCache$Type} from "packages/me/jellysquid/mods/lithium/common/state/$FastImmutableTableCache"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Table$Cell, $Table$Cell$Type} from "packages/com/google/common/collect/$Table$Cell"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FastImmutableTable<R, C, V> implements $Table<(R), (C), (V)> {

constructor(table: $Table$Type<(R), (C), (V)>, cache: $FastImmutableTableCache$Type<(R), (C), (V)>)

public "remove"(rowKey: any, columnKey: any): V
public "get"(rowKey: any, columnKey: any): V
public "put"(rowKey: R, columnKey: C, val: V): V
public "values"(): $Collection<(V)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "contains"(rowKey: any, columnKey: any): boolean
public "putAll"(table: $Table$Type<(any), (any), (any)>): void
public "containsValue"(value: any): boolean
public "column"(columnKey: C): $Map<(R), (V)>
public "row"(rowKey: R): $Map<(C), (V)>
public "containsColumn"(columnKey: any): boolean
public "cellSet"(): $Set<($Table$Cell<(R), (C), (V)>)>
public "containsRow"(rowKey: any): boolean
public "rowMap"(): $Map<(R), ($Map<(C), (V)>)>
public "columnKeySet"(): $Set<(C)>
public "rowKeySet"(): $Set<(R)>
public "columnMap"(): $Map<(C), ($Map<(R), (V)>)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastImmutableTable$Type<R, C, V> = ($FastImmutableTable<(R), (C), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastImmutableTable_<R, C, V> = $FastImmutableTable$Type<(R), (C), (V)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/$Distances" {
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $Distances {

constructor()

public static "isWithinCircleRadius"(origin: $BlockPos$Type, radiusSq: double, pos: $BlockPos$Type): boolean
public static "getMinChunkToBlockDistanceL2Sq"(origin: $BlockPos$Type, chunkX: integer, chunkZ: integer): double
public static "isWithinSquareRadius"(origin: $BlockPos$Type, radius: integer, pos: $BlockPos$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Distances$Type = ($Distances);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Distances_ = $Distances$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlAbstractTessellation" {
import {$GlPrimitiveType, $GlPrimitiveType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlPrimitiveType"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$GlTessellation, $GlTessellation$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlTessellation"

export class $GlAbstractTessellation implements $GlTessellation {


public "getPrimitiveType"(): $GlPrimitiveType
public "delete"(arg0: $CommandList$Type): void
public "bind"(arg0: $CommandList$Type): void
public "unbind"(arg0: $CommandList$Type): void
get "primitiveType"(): $GlPrimitiveType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlAbstractTessellation$Type = ($GlAbstractTessellation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlAbstractTessellation_ = $GlAbstractTessellation$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/lock/$NullLock" {
import {$Condition, $Condition$Type} from "packages/java/util/concurrent/locks/$Condition"
import {$Lock, $Lock$Type} from "packages/java/util/concurrent/locks/$Lock"
import {$TimeUnit, $TimeUnit$Type} from "packages/java/util/concurrent/$TimeUnit"

export class $NullLock implements $Lock {

constructor()

public "lock"(): void
public "lockInterruptibly"(): void
public "tryLock"(time: long, unit: $TimeUnit$Type): boolean
public "tryLock"(): boolean
public "newCondition"(): $Condition
public "unlock"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullLock$Type = ($NullLock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullLock_ = $NullLock$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/deduplication/$LithiumInterner" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LithiumInterner<T> {

constructor()

public "deleteCanonical"(value: T): void
public "getCanonical"(value: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumInterner$Type<T> = ($LithiumInterner<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumInterner_<T> = $LithiumInterner$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/ai/$MemoryModificationCounter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MemoryModificationCounter {

 "getModCount"(): long

(): long
}

export namespace $MemoryModificationCounter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryModificationCounter$Type = ($MemoryModificationCounter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryModificationCounter_ = $MemoryModificationCounter$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compat/modernui/$MuiGuiScaleHook" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MuiGuiScaleHook {

constructor()

public static "getMaxGuiScale"(): integer
get "maxGuiScale"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MuiGuiScaleHook$Type = ($MuiGuiScaleHook);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MuiGuiScaleHook_ = $MuiGuiScaleHook$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformBlock" {
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"

export class $GlUniformBlock {

constructor(arg0: integer)

public "bindBuffer"(arg0: $GlBuffer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniformBlock$Type = ($GlUniformBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniformBlock_ = $GlUniformBlock$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/reflection/$ReflectionUtil" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ReflectionUtil {

constructor()

public static "hasMethodOverride"(clazz: $Class$Type<(any)>, superclass: $Class$Type<(any)>, fallbackResult: boolean, methodName: string, ...methodArgs: ($Class$Type<(any)>)[]): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReflectionUtil$Type = ($ReflectionUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReflectionUtil_ = $ReflectionUtil$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$BlockCacheProvider" {
import {$BlockCache, $BlockCache$Type} from "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$BlockCache"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BlockCacheProvider {

 "getUpdatedBlockCache"(entity: $Entity$Type): $BlockCache
 "getBlockCache"(): $BlockCache

(entity: $Entity$Type): $BlockCache
}

export namespace $BlockCacheProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCacheProvider$Type = ($BlockCacheProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCacheProvider_ = $BlockCacheProvider$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants$Builder" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ShaderConstants, $ShaderConstants$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants"

export class $ShaderConstants$Builder {


public "add"(arg0: string, arg1: string): void
public "add"(arg0: string): void
public "addAll"(arg0: $List$Type<(string)>): void
public "build"(): $ShaderConstants
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderConstants$Builder$Type = ($ShaderConstants$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderConstants$Builder_ = $ShaderConstants$Builder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpl$Builder" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Control, $Control$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control"
import {$OptionImpl, $OptionImpl$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpl"
import {$OptionBinding, $OptionBinding$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/binding/$OptionBinding"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionFlag, $OptionFlag$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionFlag"
import {$OptionImpact, $OptionImpact$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpact"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $OptionImpl$Builder<S, T> {


public "setName"(arg0: $Component$Type): $OptionImpl$Builder<(S), (T)>
public "build"(): $OptionImpl<(S), (T)>
public "setFlags"(...arg0: ($OptionFlag$Type)[]): $OptionImpl$Builder<(S), (T)>
public "setEnabled"(arg0: boolean): $OptionImpl$Builder<(S), (T)>
public "setTooltip"(arg0: $Component$Type): $OptionImpl$Builder<(S), (T)>
public "setControl"(arg0: $Function$Type<($OptionImpl$Type<(S), (T)>), ($Control$Type<(T)>)>): $OptionImpl$Builder<(S), (T)>
public "setImpact"(arg0: $OptionImpact$Type): $OptionImpl$Builder<(S), (T)>
public "setBinding"(arg0: $BiConsumer$Type<(S), (T)>, arg1: $Function$Type<(S), (T)>): $OptionImpl$Builder<(S), (T)>
public "setBinding"(arg0: $OptionBinding$Type<(S), (T)>): $OptionImpl$Builder<(S), (T)>
public "setId"(arg0: $ResourceLocation$Type): $OptionImpl$Builder<(S), (T)>
public "setId"(arg0: $OptionIdentifier$Type<(T)>): $OptionImpl$Builder<(S), (T)>
set "name"(value: $Component$Type)
set "flags"(value: ($OptionFlag$Type)[])
set "enabled"(value: boolean)
set "tooltip"(value: $Component$Type)
set "control"(value: $Function$Type<($OptionImpl$Type<(S), (T)>), ($Control$Type<(T)>)>)
set "impact"(value: $OptionImpact$Type)
set "binding"(value: $OptionBinding$Type<(S), (T)>)
set "id"(value: $ResourceLocation$Type)
set "id"(value: $OptionIdentifier$Type<(T)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionImpl$Builder$Type<S, T> = ($OptionImpl$Builder<(S), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionImpl$Builder_<S, T> = $OptionImpl$Builder$Type<(S), (T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/serializers/generated/$VertexSerializerFactory" {
import {$VertexSerializerFactory$Bytecode, $VertexSerializerFactory$Bytecode$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/serializers/generated/$VertexSerializerFactory$Bytecode"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $VertexSerializerFactory {

constructor()

public static "generate"(arg0: $VertexFormatDescription$Type, arg1: $VertexFormatDescription$Type, arg2: string): $VertexSerializerFactory$Bytecode
public static "define"(arg0: $VertexSerializerFactory$Bytecode$Type): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexSerializerFactory$Type = ($VertexSerializerFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexSerializerFactory_ = $VertexSerializerFactory$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/viewport/$ViewportProvider" {
import {$Viewport, $Viewport$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$Viewport"

export interface $ViewportProvider {

 "sodium$createViewport"(): $Viewport

(): $Viewport
}

export namespace $ViewportProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ViewportProvider$Type = ($ViewportProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ViewportProvider_ = $ViewportProvider$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/tuples/$Range6Int" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $Range6Int extends $Record {

constructor(negativeX: integer, negativeY: integer, negativeZ: integer, positiveX: integer, positiveY: integer, positiveZ: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "positiveX"(): integer
public "positiveY"(): integer
public "positiveZ"(): integer
public "negativeX"(): integer
public "negativeZ"(): integer
public "negativeY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Range6Int$Type = ($Range6Int);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Range6Int_ = $Range6Int$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedItemEntityMovementTracker" {
import {$WorldSectionBox, $WorldSectionBox$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$WorldSectionBox"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$SectionedEntityMovementTracker, $SectionedEntityMovementTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementTracker"

export class $SectionedItemEntityMovementTracker<S extends $Entity> extends $SectionedEntityMovementTracker<($Entity), (S)> {

constructor(worldSectionBox: $WorldSectionBox$Type, clazz: $Class$Type<(S)>)

public "getEntities"(areas: ($AABB$Type)[]): $List<(S)>
public static "registerAt"<S extends $Entity>(world: $ServerLevel$Type, encompassingBox: $AABB$Type, clazz: $Class$Type<(S)>): $SectionedItemEntityMovementTracker<(S)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionedItemEntityMovementTracker$Type<S> = ($SectionedItemEntityMovementTracker<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionedItemEntityMovementTracker_<S> = $SectionedItemEntityMovementTracker$Type<(S)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionGroup, $OptionGroup$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionGroup"

export class $OptionPage {
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

/**
 * 
 * @deprecated
 */
constructor(arg0: $Component$Type, arg1: $ImmutableList$Type<($OptionGroup$Type)>)
constructor(arg0: $OptionIdentifier$Type<(void)>, arg1: $Component$Type, arg2: $ImmutableList$Type<($OptionGroup$Type)>)

public "getName"(): $Component
public "getId"(): $OptionIdentifier<(void)>
public "getOptions"(): $ImmutableList<($Option<(any)>)>
public "getGroups"(): $ImmutableList<($OptionGroup)>
get "name"(): $Component
get "id"(): $OptionIdentifier<(void)>
get "options"(): $ImmutableList<($Option<(any)>)>
get "groups"(): $ImmutableList<($OptionGroup)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionPage$Type = ($OptionPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionPage_ = $OptionPage$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/client/$ClientWorldAccessor" {
import {$TransientEntitySectionManager, $TransientEntitySectionManager$Type} from "packages/net/minecraft/world/level/entity/$TransientEntitySectionManager"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ClientWorldAccessor {

 "getEntityManager"(): $TransientEntitySectionManager<($Entity)>

(): $TransientEntitySectionManager<($Entity)>
}

export namespace $ClientWorldAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientWorldAccessor$Type = ($ClientWorldAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientWorldAccessor_ = $ClientWorldAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpl" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Control, $Control$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$OptionFlag, $OptionFlag$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionFlag"
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"
import {$OptionImpact, $OptionImpact$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpact"
import {$OptionImpl$Builder, $OptionImpl$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionImpl$Builder"

export class $OptionImpl<S, T> implements $Option<(T)> {


public "getName"(): $Component
public "getValue"(): T
public "getId"(): $OptionIdentifier<(T)>
public "setValue"(arg0: T): void
public "reset"(): void
public "getControl"(): $Control<(T)>
public "getFlags"(): $Collection<($OptionFlag)>
public "isAvailable"(): boolean
public static "createBuilder"<S, T>(arg0: $Class$Type<(T)>, arg1: $OptionStorage$Type<(S)>): $OptionImpl$Builder<(S), (T)>
public "getImpact"(): $OptionImpact
public "applyChanges"(): void
public "getTooltip"(): $Component
public "hasChanged"(): boolean
public "getStorage"(): $OptionStorage<(any)>
get "name"(): $Component
get "value"(): T
get "id"(): $OptionIdentifier<(T)>
set "value"(value: T)
get "control"(): $Control<(T)>
get "flags"(): $Collection<($OptionFlag)>
get "available"(): boolean
get "impact"(): $OptionImpact
get "tooltip"(): $Component
get "storage"(): $OptionStorage<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionImpl$Type<S, T> = ($OptionImpl<(S), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionImpl_<S, T> = $OptionImpl$Type<(S), (T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$HopperCachingState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $HopperCachingState {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HopperCachingState$Type = ($HopperCachingState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HopperCachingState_ = $HopperCachingState$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_comparator_tracking/$ComparatorTracking" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ComparatorTracking {

constructor()

public static "findNearbyComparators"(world: $Level$Type, pos: $BlockPos$Type): boolean
public static "notifyNearbyBlockEntitiesAboutNewComparator"(world: $Level$Type, pos: $BlockPos$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComparatorTracking$Type = ($ComparatorTracking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComparatorTracking_ = $ComparatorTracking$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$Object2BooleanCacheTable" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"

export class $Object2BooleanCacheTable<T> {

constructor(capacity: integer, operator: $Predicate$Type<(T)>)

public "get"(key: T): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Object2BooleanCacheTable$Type<T> = ($Object2BooleanCacheTable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Object2BooleanCacheTable_<T> = $Object2BooleanCacheTable$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$OcclusionCuller" {
import {$OcclusionCuller$Visitor, $OcclusionCuller$Visitor$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$OcclusionCuller$Visitor"
import {$Long2ReferenceMap, $Long2ReferenceMap$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2ReferenceMap"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Viewport, $Viewport$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$Viewport"

export class $OcclusionCuller {

constructor(arg0: $Long2ReferenceMap$Type<($RenderSection$Type)>, arg1: $Level$Type)

public static "isWithinFrustum"(arg0: $Viewport$Type, arg1: $RenderSection$Type): boolean
public "findVisible"(arg0: $OcclusionCuller$Visitor$Type, arg1: $Viewport$Type, arg2: float, arg3: boolean, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OcclusionCuller$Type = ($OcclusionCuller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OcclusionCuller_ = $OcclusionCuller$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderFogComponent" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ChunkShaderFogComponent {

constructor()

public "setup"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkShaderFogComponent$Type = ($ChunkShaderFogComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkShaderFogComponent_ = $ChunkShaderFogComponent$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBufferSorter" {
import {$TranslucentQuadAnalyzer$SortState, $TranslucentQuadAnalyzer$SortState$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState"
import {$NativeBuffer, $NativeBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/util/$NativeBuffer"

export class $ChunkBufferSorter {

constructor()

public static "generateSimpleIndexBuffer"(arg0: $NativeBuffer$Type, arg1: integer, arg2: integer): $NativeBuffer
public static "sort"(arg0: $NativeBuffer$Type, arg1: $TranslucentQuadAnalyzer$SortState$Type, arg2: float, arg3: float, arg4: float): $NativeBuffer
public static "getIndexBufferSize"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBufferSorter$Type = ($ChunkBufferSorter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBufferSorter_ = $ChunkBufferSorter$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/blockentity/$SupportCache" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SupportCache {

 "isSupported"(): boolean

(): boolean
}

export namespace $SupportCache {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SupportCache$Type = ($SupportCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SupportCache_ = $SupportCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferArena" {
import {$StagingBuffer, $StagingBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$PendingUpload, $PendingUpload$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/$PendingUpload"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$GlBufferSegment, $GlBufferSegment$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferSegment"

export class $GlBufferArena {

constructor(arg0: $CommandList$Type, arg1: integer, arg2: integer, arg3: $StagingBuffer$Type)

public "getBufferObject"(): $GlBuffer
public "getDeviceAllocatedMemoryL"(): long
public "getDeviceUsedMemoryL"(): long
public "isEmpty"(): boolean
public "delete"(arg0: $CommandList$Type): void
public "ensureCapacity"(arg0: $CommandList$Type, arg1: integer): void
public "free"(arg0: $GlBufferSegment$Type): void
/**
 * 
 * @deprecated
 */
public "getDeviceAllocatedMemory"(): integer
/**
 * 
 * @deprecated
 */
public "getDeviceUsedMemory"(): integer
public "upload"(arg0: $CommandList$Type, arg1: $Stream$Type<($PendingUpload$Type)>): boolean
get "bufferObject"(): $GlBuffer
get "deviceAllocatedMemoryL"(): long
get "deviceUsedMemoryL"(): long
get "empty"(): boolean
get "deviceAllocatedMemory"(): integer
get "deviceUsedMemory"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferArena$Type = ($GlBufferArena);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferArena_ = $GlBufferArena$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$InventoryHelper" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$LithiumInventory, $LithiumInventory$Type} from "packages/me/jellysquid/mods/lithium/api/inventory/$LithiumInventory"

export class $InventoryHelper {

constructor()

public static "getLithiumStackListOrNull"(inventory: $LithiumInventory$Type): $LithiumStackList
public static "getLithiumStackList"(inventory: $LithiumInventory$Type): $LithiumStackList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryHelper$Type = ($InventoryHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryHelper_ = $InventoryHelper$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$VisibleChunkCollector" {
import {$ArrayDeque, $ArrayDeque$Type} from "packages/java/util/$ArrayDeque"
import {$OcclusionCuller$Visitor, $OcclusionCuller$Visitor$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$OcclusionCuller$Visitor"
import {$ChunkUpdateType, $ChunkUpdateType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkUpdateType"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$SortedRenderLists, $SortedRenderLists$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$SortedRenderLists"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VisibleChunkCollector implements $OcclusionCuller$Visitor {

constructor(arg0: integer)

public "visit"(arg0: $RenderSection$Type, arg1: boolean): void
public "createRenderLists"(): $SortedRenderLists
public "getRebuildLists"(): $Map<($ChunkUpdateType), ($ArrayDeque<($RenderSection)>)>
get "rebuildLists"(): $Map<($ChunkUpdateType), ($ArrayDeque<($RenderSection)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VisibleChunkCollector$Type = ($VisibleChunkCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VisibleChunkCollector_ = $VisibleChunkCollector$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionGroup" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionGroup$Builder, $OptionGroup$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionGroup$Builder"

export class $OptionGroup {
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>
readonly "id": $OptionIdentifier<(void)>


public "getId"(): $OptionIdentifier<(void)>
public static "createBuilder"(): $OptionGroup$Builder
public "getOptions"(): $ImmutableList<($Option<(any)>)>
get "id"(): $OptionIdentifier<(void)>
get "options"(): $ImmutableList<($Option<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionGroup$Type = ($OptionGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionGroup_ = $OptionGroup$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener" {
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $InventoryChangeListener {

 "handleInventoryRemoved"(arg0: $Container$Type): void
 "handleStackListReplaced"(inventory: $Container$Type): void
 "handleInventoryContentModified"(arg0: $Container$Type): void
 "handleComparatorAdded"(arg0: $Container$Type): boolean
}

export namespace $InventoryChangeListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryChangeListener$Type = ($InventoryChangeListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryChangeListener_ = $InventoryChangeListener$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadFlags" {
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $ModelQuadFlags {
static readonly "IS_PARTIAL": integer
static readonly "IS_PARALLEL": integer
static readonly "IS_ALIGNED": integer

constructor()

public static "contains"(arg0: integer, arg1: integer): boolean
public static "getQuadFlags"(arg0: $ModelQuadView$Type, arg1: $Direction$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadFlags$Type = ($ModelQuadFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadFlags_ = $ModelQuadFlags$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/lists/$OffsetFractionalDoubleList" {
import {$AbstractDoubleList, $AbstractDoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$AbstractDoubleList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"

export class $OffsetFractionalDoubleList extends $AbstractDoubleList {

constructor(numSections: integer, offset: double)

public "getDouble"(position: integer): double
public "size"(): integer
/**
 * 
 * @deprecated
 */
public "add"(arg0: double): boolean
/**
 * 
 * @deprecated
 */
public "remove"(arg0: any): boolean
public static "of"(...arg0: (double)[]): $DoubleList
public static "of"(): $DoubleList
public static "of"(arg0: double, arg1: double): $DoubleList
public static "of"(arg0: double): $DoubleList
public static "of"(arg0: double, arg1: double, arg2: double): $DoubleList
/**
 * 
 * @deprecated
 */
public "contains"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OffsetFractionalDoubleList$Type = ($OffsetFractionalDoubleList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OffsetFractionalDoubleList_ = $OffsetFractionalDoubleList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$SectionRenderDataUnsafe" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SectionRenderDataUnsafe {

constructor()

public static "setIndexOffset"(arg0: long, arg1: integer, arg2: integer): void
public static "setSliceMask"(arg0: long, arg1: integer): void
public static "setVertexOffset"(arg0: long, arg1: integer, arg2: integer): void
public static "setElementCount"(arg0: long, arg1: integer, arg2: integer): void
public static "heapPointer"(arg0: long, arg1: integer): long
public static "freeHeap"(arg0: long): void
public static "allocateHeap"(arg0: integer): long
public static "clear"(arg0: long): void
public static "getElementCount"(arg0: long, arg1: integer): integer
public static "getIndexOffset"(arg0: long, arg1: integer): integer
public static "getVertexOffset"(arg0: long, arg1: integer): integer
public static "getSliceMask"(arg0: long): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionRenderDataUnsafe$Type = ($SectionRenderDataUnsafe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionRenderDataUnsafe_ = $SectionRenderDataUnsafe$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderMatrices" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"

export class $ChunkRenderMatrices extends $Record {

constructor(projection: $Matrix4fc$Type, modelView: $Matrix4fc$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "from"(arg0: $PoseStack$Type): $ChunkRenderMatrices
public "projection"(): $Matrix4fc
public "modelView"(): $Matrix4fc
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRenderMatrices$Type = ($ChunkRenderMatrices);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRenderMatrices_ = $ChunkRenderMatrices$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderWorkarounds" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ShaderWorkarounds {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderWorkarounds$Type = ($ShaderWorkarounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderWorkarounds_ = $ShaderWorkarounds$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobCollector" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ChunkBuildOutput, $ChunkBuildOutput$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildOutput"
import {$ChunkJob, $ChunkJob$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJob"
import {$ChunkBuilder, $ChunkBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkBuilder"
import {$ChunkJobResult, $ChunkJobResult$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobResult"

export class $ChunkJobCollector {

constructor(arg0: integer, arg1: $Consumer$Type<($ChunkJobResult$Type<($ChunkBuildOutput$Type)>)>)

public "awaitCompletion"(arg0: $ChunkBuilder$Type): void
public "onJobFinished"(arg0: $ChunkJobResult$Type<($ChunkBuildOutput$Type)>): void
public "addSubmittedJob"(arg0: $ChunkJob$Type): void
public "canOffer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkJobCollector$Type = ($ChunkJobCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkJobCollector_ = $ChunkJobCollector$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/$SleepUntilTimeBlockEntityTickInvoker" {
import {$TickingBlockEntity, $TickingBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TickingBlockEntity"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $SleepUntilTimeBlockEntityTickInvoker extends $Record implements $TickingBlockEntity {

constructor(sleepingBlockEntity: $BlockEntity$Type, sleepUntilTickExclusive: long, delegate: $TickingBlockEntity$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "delegate"(): $TickingBlockEntity
public "getType"(): string
public "isRemoved"(): boolean
public "getPos"(): $BlockPos
public "tick"(): void
public "sleepingBlockEntity"(): $BlockEntity
public "sleepUntilTickExclusive"(): long
get "type"(): string
get "removed"(): boolean
get "pos"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SleepUntilTimeBlockEntityTickInvoker$Type = ($SleepUntilTimeBlockEntityTickInvoker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SleepUntilTimeBlockEntityTickInvoker_ = $SleepUntilTimeBlockEntityTickInvoker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatRegistryImpl" {
import {$VertexFormatRegistry, $VertexFormatRegistry$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatRegistry"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export class $VertexFormatRegistryImpl implements $VertexFormatRegistry {

constructor()

public "get"(arg0: $VertexFormat$Type): $VertexFormatDescription
public static "instance"(): $VertexFormatRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexFormatRegistryImpl$Type = ($VertexFormatRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexFormatRegistryImpl_ = $VertexFormatRegistryImpl$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$SodiumOptionsStorage" {
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"
import {$SodiumGameOptions, $SodiumGameOptions$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions"

export class $SodiumOptionsStorage implements $OptionStorage<($SodiumGameOptions)> {

constructor()

public "save"(): void
public "getData"(): $SodiumGameOptions
get "data"(): $SodiumGameOptions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumOptionsStorage$Type = ($SodiumOptionsStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumOptionsStorage_ = $SodiumOptionsStorage$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$SharedQuadIndexBuffer$IndexType" {
import {$GlIndexType, $GlIndexType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlIndexType"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $SharedQuadIndexBuffer$IndexType extends $Enum<($SharedQuadIndexBuffer$IndexType)> {
static readonly "SHORT": $SharedQuadIndexBuffer$IndexType
static readonly "INTEGER": $SharedQuadIndexBuffer$IndexType
static readonly "VALUES": ($SharedQuadIndexBuffer$IndexType)[]


public "getMaxPrimitiveCount"(): integer
public static "values"(): ($SharedQuadIndexBuffer$IndexType)[]
public static "valueOf"(arg0: string): $SharedQuadIndexBuffer$IndexType
public "getFormat"(): $GlIndexType
public "getMaxElementCount"(): integer
public "createIndexBuffer"(arg0: $ByteBuffer$Type, arg1: integer): void
public "getBytesPerElement"(): integer
get "maxPrimitiveCount"(): integer
get "format"(): $GlIndexType
get "maxElementCount"(): integer
get "bytesPerElement"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharedQuadIndexBuffer$IndexType$Type = (("short") | ("integer")) | ($SharedQuadIndexBuffer$IndexType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharedQuadIndexBuffer$IndexType_ = $SharedQuadIndexBuffer$IndexType$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/color/$FastCubicSampler$ColorFetcher" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FastCubicSampler$ColorFetcher {

 "fetch"(arg0: integer, arg1: integer, arg2: integer): integer

(arg0: integer, arg1: integer, arg2: integer): integer
}

export namespace $FastCubicSampler$ColorFetcher {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastCubicSampler$ColorFetcher$Type = ($FastCubicSampler$ColorFetcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastCubicSampler$ColorFetcher_ = $FastCubicSampler$ColorFetcher$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderListIterable" {
import {$ChunkRenderList, $ChunkRenderList$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderList"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export interface $ChunkRenderListIterable {

 "iterator"(arg0: boolean): $Iterator<($ChunkRenderList)>
 "iterator"(): $Iterator<($ChunkRenderList)>

(arg0: boolean): $Iterator<($ChunkRenderList)>
}

export namespace $ChunkRenderListIterable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRenderListIterable$Type = ($ChunkRenderListIterable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRenderListIterable_ = $ChunkRenderListIterable$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$StorableItemStack" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"

export interface $StorableItemStack {

 "registerToInventory"(arg0: $LithiumStackList$Type, arg1: integer): void
 "unregisterFromInventory"(arg0: $LithiumStackList$Type): void
 "unregisterFromInventory"(arg0: $LithiumStackList$Type, arg1: integer): void
}

export namespace $StorableItemStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StorableItemStack$Type = ($StorableItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StorableItemStack_ = $StorableItemStack$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/serializers/generated/$VertexSerializerFactory$Bytecode" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $VertexSerializerFactory$Bytecode {


public "copy"(): (byte)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexSerializerFactory$Bytecode$Type = ($VertexSerializerFactory$Bytecode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexSerializerFactory$Bytecode_ = $VertexSerializerFactory$Bytecode$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniformInt" {
import {$GlUniform, $GlUniform$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/uniform/$GlUniform"

export class $GlUniformInt extends $GlUniform<(integer)> {

constructor(arg0: integer)

public "set"(arg0: integer): void
public "setInt"(arg0: integer): void
set "int"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlUniformInt$Type = ($GlUniformInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlUniformInt_ = $GlUniformInt$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/sorting/$AbstractSort" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AbstractSort {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSort$Type = ($AbstractSort);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSort_ = $AbstractSort$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$MovementTrackerHelper" {
import {$Reference2IntOpenHashMap, $Reference2IntOpenHashMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2IntOpenHashMap"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $MovementTrackerHelper {
static readonly "MOVEMENT_NOTIFYING_ENTITY_CLASSES": $List<($Class<(any)>)>
static "CLASS_2_NOTIFY_MASK": $Reference2IntOpenHashMap<($Class<(any)>)>
static readonly "NUM_MOVEMENT_NOTIFYING_CLASSES": integer

constructor()

public static "getNotificationMask"(entityClass: $Class$Type<(any)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MovementTrackerHelper$Type = ($MovementTrackerHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MovementTrackerHelper_ = $MovementTrackerHelper$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/version/$Version" {
import {$VersionInfo, $VersionInfo$Type} from "packages/me/jellysquid/mods/sodium/client/platform/windows/api/version/$VersionInfo"

export class $Version {

constructor()

public static "getModuleFileVersion"(arg0: string): $VersionInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Version$Type = ($Version);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Version_ = $Version$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$ModelCuboid" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $ModelCuboid {
readonly "x1": float
readonly "y1": float
readonly "z1": float
readonly "x2": float
readonly "y2": float
readonly "z2": float
readonly "u0": float
readonly "u1": float
readonly "u2": float
readonly "u3": float
readonly "u4": float
readonly "u5": float
readonly "v0": float
readonly "v1": float
readonly "v2": float
readonly "mirror": boolean

constructor(arg0: integer, arg1: integer, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: boolean, arg12: float, arg13: float, arg14: $Set$Type<($Direction$Type)>)

public "shouldDrawFace"(arg0: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelCuboid$Type = ($ModelCuboid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelCuboid_ = $ModelCuboid$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$FileUtil" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"

export class $FileUtil {

constructor()

public static "writeTextRobustly"(arg0: string, arg1: $Path$Type): void
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
declare module "packages/me/jellysquid/mods/sodium/client/buffer/$ExtendedVertexFormat$Element" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"

export class $ExtendedVertexFormat$Element {
readonly "actual": $VertexFormatElement
readonly "increment": integer
readonly "byteLength": integer

constructor(arg0: $VertexFormatElement$Type, arg1: integer, arg2: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedVertexFormat$Element$Type = ($ExtendedVertexFormat$Element);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedVertexFormat$Element_ = $ExtendedVertexFormat$Element$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatDescription" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CommonVertexElement, $CommonVertexElement$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/transform/$CommonVertexElement"
import {$VertexFormatDescriptionImpl, $VertexFormatDescriptionImpl$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/$VertexFormatDescriptionImpl"
import {$VertexFormatDescription as $VertexFormatDescription$0, $VertexFormatDescription$Type as $VertexFormatDescription$0$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

/**
 * 
 * @deprecated
 */
export class $VertexFormatDescription {
readonly "id": integer
readonly "stride": integer
readonly "elementCount": integer
readonly "elementOffsets": (integer)[]

constructor(arg0: $VertexFormat$Type, arg1: integer)
constructor(arg0: $VertexFormatDescriptionImpl$Type)

public "toString"(): string
public "getOffset"(arg0: $CommonVertexElement$Type): integer
public "getOffsets"(): $IntList
public "getElements"(): $List<($VertexFormatElement)>
public static "translateModern"(arg0: $VertexFormatDescription$0$Type): $VertexFormatDescription
get "offsets"(): $IntList
get "elements"(): $List<($VertexFormatElement)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexFormatDescription$Type = ($VertexFormatDescription);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexFormatDescription_ = $VertexFormatDescription$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptionPages" {
import {$Options, $Options$Type} from "packages/net/minecraft/client/$Options"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"
import {$SodiumGameOptions, $SodiumGameOptions$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions"

export class $SodiumGameOptionPages {

constructor()

public static "getSodiumOpts"(): $OptionStorage<($SodiumGameOptions)>
public static "general"(): $OptionPage
public static "quality"(): $OptionPage
public static "getVanillaOpts"(): $OptionStorage<($Options)>
public static "performance"(): $OptionPage
public static "advanced"(): $OptionPage
get "sodiumOpts"(): $OptionStorage<($SodiumGameOptions)>
get "vanillaOpts"(): $OptionStorage<($Options)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptionPages$Type = ($SodiumGameOptionPages);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptionPages_ = $SodiumGameOptionPages$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlMutableBuffer" {
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"

export class $GlMutableBuffer extends $GlBuffer {

constructor()

public "getSize"(): long
public "setSize"(arg0: long): void
get "size"(): long
set "size"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlMutableBuffer$Type = ($GlMutableBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlMutableBuffer_ = $GlMutableBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/biome/$BiomeSlice" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ChunkRenderContext, $ChunkRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/world/cloned/$ChunkRenderContext"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"

export class $BiomeSlice {

constructor()

public "update"(arg0: $ClientLevel$Type, arg1: $ChunkRenderContext$Type): void
public "getBiome"(arg0: integer, arg1: integer, arg2: integer): $Holder<($Biome)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeSlice$Type = ($BiomeSlice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeSlice_ = $BiomeSlice$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/animations/upload/$SpriteContentsAnimationFrameAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SpriteContentsAnimationFrameAccessor {

 "getIndex"(): integer
 "getTime"(): integer
}

export namespace $SpriteContentsAnimationFrameAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsAnimationFrameAccessor$Type = ($SpriteContentsAnimationFrameAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsAnimationFrameAccessor_ = $SpriteContentsAnimationFrameAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/cloned/$ClonedChunkSection" {
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$DataLayer, $DataLayer$Type} from "packages/net/minecraft/world/level/chunk/$DataLayer"
import {$PalettedContainerRO, $PalettedContainerRO$Type} from "packages/net/minecraft/world/level/chunk/$PalettedContainerRO"
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$LevelChunkSection, $LevelChunkSection$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunkSection"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Int2ReferenceMap, $Int2ReferenceMap$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ReferenceMap"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"

export class $ClonedChunkSection {

constructor(arg0: $Level$Type, arg1: $LevelChunk$Type, arg2: $LevelChunkSection$Type, arg3: $SectionPos$Type)

public "getBlockEntityRenderDataMap"(): $Int2ReferenceMap<(any)>
public "getPosition"(): $SectionPos
public "getBlockData"(): $PalettedContainerRO<($BlockState)>
public "getLightArray"(arg0: $LightLayer$Type): $DataLayer
public "getBlockEntityMap"(): $Int2ReferenceMap<($BlockEntity)>
public "getBiomeData"(): $PalettedContainerRO<($Holder<($Biome)>)>
public "setLastUsedTimestamp"(arg0: long): void
public "getLastUsedTimestamp"(): long
get "blockEntityRenderDataMap"(): $Int2ReferenceMap<(any)>
get "position"(): $SectionPos
get "blockData"(): $PalettedContainerRO<($BlockState)>
get "blockEntityMap"(): $Int2ReferenceMap<($BlockEntity)>
get "biomeData"(): $PalettedContainerRO<($Holder<($Biome)>)>
set "lastUsedTimestamp"(value: long)
get "lastUsedTimestamp"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClonedChunkSection$Type = ($ClonedChunkSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClonedChunkSection_ = $ClonedChunkSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadWinding" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ModelQuadWinding extends $Enum<($ModelQuadWinding)> {
static readonly "CLOCKWISE": $ModelQuadWinding
static readonly "COUNTERCLOCKWISE": $ModelQuadWinding


public static "values"(): ($ModelQuadWinding)[]
public static "valueOf"(arg0: string): $ModelQuadWinding
public "getIndices"(): (integer)[]
get "indices"(): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadWinding$Type = (("clockwise") | ("counterclockwise")) | ($ModelQuadWinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadWinding_ = $ModelQuadWinding$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionFlag" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $OptionFlag extends $Enum<($OptionFlag)> {
static readonly "REQUIRES_RENDERER_RELOAD": $OptionFlag
static readonly "REQUIRES_RENDERER_UPDATE": $OptionFlag
static readonly "REQUIRES_ASSET_RELOAD": $OptionFlag
static readonly "REQUIRES_GAME_RESTART": $OptionFlag


public static "values"(): ($OptionFlag)[]
public static "valueOf"(arg0: string): $OptionFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionFlag$Type = (("requires_renderer_reload") | ("requires_game_restart") | ("requires_renderer_update") | ("requires_asset_reload")) | ($OptionFlag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionFlag_ = $OptionFlag$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPromptable" {
import {$ScreenPrompt, $ScreenPrompt$Type} from "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPrompt"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"

/**
 * 
 * @deprecated
 */
export interface $ScreenPromptable {

 "setPrompt"(arg0: $ScreenPrompt$Type): void
 "getPrompt"(): $ScreenPrompt
 "getDimensions"(): $Dim2i
}

export namespace $ScreenPromptable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenPromptable$Type = ($ScreenPromptable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenPromptable_ = $ScreenPromptable$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProvider" {
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$WorldSlice, $WorldSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/$WorldSlice"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $ColorProvider<T> {

 "getColors"(arg0: $WorldSlice$Type, arg1: $BlockPos$Type, arg2: T, arg3: $ModelQuadView$Type, arg4: (integer)[]): void

(arg0: $WorldSlice$Type, arg1: $BlockPos$Type, arg2: T, arg3: $ModelQuadView$Type, arg4: (integer)[]): void
}

export namespace $ColorProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorProvider$Type<T> = ($ColorProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorProvider_<T> = $ColorProvider$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/viewport/$Viewport" {
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$Frustum, $Frustum$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/frustum/$Frustum"
import {$CameraTransform, $CameraTransform$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$CameraTransform"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $Viewport {

constructor(arg0: $Frustum$Type, arg1: $Vector3d$Type)

public "isBoxVisible"(arg0: integer, arg1: integer, arg2: integer, arg3: float, arg4: float, arg5: float): boolean
public "isBoxVisible"(arg0: integer, arg1: integer, arg2: integer, arg3: float): boolean
public "isBoxVisible"(arg0: $AABB$Type): boolean
public "getChunkCoord"(): $SectionPos
public "getTransform"(): $CameraTransform
public "getBlockCoord"(): $BlockPos
get "chunkCoord"(): $SectionPos
get "transform"(): $CameraTransform
get "blockCoord"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Viewport$Type = ($Viewport);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Viewport_ = $Viewport$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/occlusion/$VisibilityEncoding" {
import {$VisibilitySet, $VisibilitySet$Type} from "packages/net/minecraft/client/renderer/chunk/$VisibilitySet"

export class $VisibilityEncoding {
static readonly "NULL": long

constructor()

public static "encode"(arg0: $VisibilitySet$Type): long
public static "getConnections"(arg0: long, arg1: integer): integer
public static "getConnections"(arg0: long): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VisibilityEncoding$Type = ($VisibilityEncoding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VisibilityEncoding_ = $VisibilityEncoding$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$NativeBuffer" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $NativeBuffer {

constructor(arg0: integer)

public "getLength"(): integer
public static "copy"(arg0: $ByteBuffer$Type): $NativeBuffer
public "free"(): void
public static "reclaim"(arg0: boolean): void
public "getDirectBuffer"(): $ByteBuffer
public static "getTotalAllocated"(): long
get "length"(): integer
get "directBuffer"(): $ByteBuffer
get "totalAllocated"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NativeBuffer$Type = ($NativeBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NativeBuffer_ = $NativeBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/$SodiumClientMod" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$SodiumGameOptions, $SodiumGameOptions$Type} from "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions"

export class $SodiumClientMod {
static readonly "MODID": string
static readonly "MODNAME": string

constructor()

public static "options"(): $SodiumGameOptions
public static "logger"(): $Logger
public static "getVersion"(): string
public static "canUseVanillaVertices"(): boolean
public static "canApplyTranslucencySorting"(): boolean
public "onClientSetup"(arg0: $FMLClientSetupEvent$Type): void
public static "restoreDefaultOptions"(): void
get "version"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumClientMod$Type = ($SodiumClientMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumClientMod_ = $SodiumClientMod$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList" {
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$LithiumDefaultedList, $LithiumDefaultedList$Type} from "packages/me/jellysquid/mods/lithium/api/inventory/$LithiumDefaultedList"

export class $LithiumStackList extends $NonNullList<($ItemStack)> implements $LithiumDefaultedList {

constructor(original: $NonNullList$Type<($ItemStack$Type)>, maxCountPerStack: integer)
constructor(maxCountPerStack: integer)

public "add"(slot: integer, element: $ItemStack$Type): void
public "clear"(): void
public "set"(index: integer, element: $ItemStack$Type): $ItemStack
public "changed"(): void
public "getModCount"(): long
public "runComparatorUpdatePatternOnFailedExtract"(masterStackList: $LithiumStackList$Type, inventory: $Container$Type): void
public "maybeSendsComparatorUpdatesOnFailedExtract"(): boolean
public "changedALot"(): void
public "getSignalStrength"(inventory: $Container$Type): integer
public "getOccupiedSlots"(): integer
public "getFullSlots"(): integer
public "removeInventoryModificationCallback"(inventoryModificationCallback: $InventoryChangeTracker$Type): void
public "setInventoryModificationCallback"(inventoryModificationCallback: $InventoryChangeTracker$Type): void
public "beforeSlotCountChange"(slot: integer, newCount: integer): void
public "clearSignalStrengthOverride"(): void
public "setReducedSignalStrengthOverride"(): void
public "hasSignalStrengthOverride"(): boolean
public "changedInteractionConditions"(): void
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
get "modCount"(): long
get "occupiedSlots"(): integer
get "fullSlots"(): integer
set "inventoryModificationCallback"(value: $InventoryChangeTracker$Type)
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumStackList$Type = ($LithiumStackList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumStackList_ = $LithiumStackList$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementListener" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SectionedEntityMovementListener {

 "handleEntityMovement"(arg0: $Class$Type<(any)>): void

(arg0: $Class$Type<(any)>): void
}

export namespace $SectionedEntityMovementListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionedEntityMovementListener$Type = ($SectionedEntityMovementListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionedEntityMovementListener_ = $SectionedEntityMovementListener$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttribute" {
import {$GlVertexAttributeFormat, $GlVertexAttributeFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttributeFormat"

export class $GlVertexAttribute {

constructor(arg0: $GlVertexAttributeFormat$Type, arg1: integer, arg2: boolean, arg3: integer, arg4: integer, arg5: boolean)

public "getSize"(): integer
public "getCount"(): integer
public "isNormalized"(): boolean
public "getFormat"(): integer
public "getPointer"(): integer
public "isIntType"(): boolean
public "getStride"(): integer
get "size"(): integer
get "count"(): integer
get "normalized"(): boolean
get "format"(): integer
get "pointer"(): integer
get "intType"(): boolean
get "stride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexAttribute$Type = ($GlVertexAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexAttribute_ = $GlVertexAttribute$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/ai/pathing/$BlockClassChecker" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $BlockClassChecker {

constructor()

public static "shouldUseDynamicBurningCheck"(blockClass: $Class$Type<(any)>): boolean
public static "shouldUseDynamicTypeCheck"(blockClass: $Class$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockClassChecker$Type = ($BlockClassChecker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockClassChecker_ = $BlockClassChecker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$ModelQuadUtil" {
import {$ModelQuadFacing, $ModelQuadFacing$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadFacing"
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"

export class $ModelQuadUtil {
static readonly "POSITION_INDEX": integer
static readonly "COLOR_INDEX": integer
static readonly "TEXTURE_INDEX": integer
static readonly "LIGHT_INDEX": integer
static readonly "NORMAL_INDEX": integer
static readonly "VERTEX_SIZE": integer

constructor()

public static "mergeBakedLight"(arg0: integer, arg1: integer): integer
public static "mergeNormal"(arg0: integer, arg1: integer): integer
public static "mixARGBColors"(arg0: integer, arg1: integer): integer
public static "findNormalFace"(arg0: integer): $ModelQuadFacing
public static "findNormalFace"(arg0: float, arg1: float, arg2: float): $ModelQuadFacing
public static "calculateNormal"(arg0: $ModelQuadView$Type): integer
public static "vertexOffset"(arg0: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadUtil$Type = ($ModelQuadUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadUtil_ = $ModelQuadUtil$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$EquipmentEntity$EquipmentTrackingEntity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EquipmentEntity$EquipmentTrackingEntity {

}

export namespace $EquipmentEntity$EquipmentTrackingEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EquipmentEntity$EquipmentTrackingEntity$Type = ($EquipmentEntity$EquipmentTrackingEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EquipmentEntity$EquipmentTrackingEntity_ = $EquipmentEntity$EquipmentTrackingEntity$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ChunkTrackerHolder" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ChunkTracker, $ChunkTracker$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ChunkTracker"

export interface $ChunkTrackerHolder {

 "sodium$getTracker"(): $ChunkTracker

(arg0: $ClientLevel$Type): $ChunkTracker
}

export namespace $ChunkTrackerHolder {
function get(arg0: $ClientLevel$Type): $ChunkTracker
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkTrackerHolder$Type = ($ChunkTrackerHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkTrackerHolder_ = $ChunkTrackerHolder$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeSimpleCube" {
import {$VoxelShapeCaster, $VoxelShapeCaster$Type} from "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeCaster"
import {$Shapes$DoubleLineConsumer, $Shapes$DoubleLineConsumer$Type} from "packages/net/minecraft/world/phys/shapes/$Shapes$DoubleLineConsumer"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"
import {$AxisCycle, $AxisCycle$Type} from "packages/net/minecraft/core/$AxisCycle"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $VoxelShapeSimpleCube extends $VoxelShape implements $VoxelShapeCaster {
readonly "isTiny": boolean
 "shape": $DiscreteVoxelShape

constructor(voxels: $DiscreteVoxelShape$Type, minX: double, minY: double, minZ: double, maxX: double, maxY: double, maxZ: double)

public "intersects"(box: $AABB$Type, blockX: double, blockY: double, blockZ: double): boolean
public "bounds"(): $AABB
public "isEmpty"(): boolean
public "max"(axis: $Direction$Axis$Type): double
public "toAabbs"(): $List<($AABB)>
public "min"(axis: $Direction$Axis$Type): double
public "forAllBoxes"(boxConsumer: $Shapes$DoubleLineConsumer$Type): void
public "getCoords"(axis: $Direction$Axis$Type): $DoubleList
public "collideX"(cycleDirection: $AxisCycle$Type, box: $AABB$Type, maxDist: double): double
public "move"(x: double, y: double, z: double): $VoxelShape
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeSimpleCube$Type = ($VoxelShapeSimpleCube);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeSimpleCube_ = $VoxelShapeSimpleCube$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/color/interop/$BlockColorsExtended" {
import {$BlockColors, $BlockColors$Type} from "packages/net/minecraft/client/color/block/$BlockColors"
import {$ReferenceSet, $ReferenceSet$Type} from "packages/it/unimi/dsi/fastutil/objects/$ReferenceSet"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$Reference2ReferenceMap, $Reference2ReferenceMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2ReferenceMap"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BlockColorsExtended {

 "embeddium$getOverridenVanillaBlocks"(): $ReferenceSet<($Block)>
 "sodium$getProviders"(): $Reference2ReferenceMap<($Block), ($BlockColor)>
}

export namespace $BlockColorsExtended {
function getProviders(arg0: $BlockColors$Type): $Reference2ReferenceMap<($Block), ($BlockColor)>
function getOverridenVanillaBlocks(arg0: $BlockColors$Type): $ReferenceSet<($Block)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockColorsExtended$Type = ($BlockColorsExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockColorsExtended_ = $BlockColorsExtended$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListenerSection" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$NearbyEntityListener, $NearbyEntityListener$Type} from "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListener"

export interface $NearbyEntityListenerSection {

 "removeListener"(arg0: $EntitySectionStorage$Type<(any)>, arg1: $NearbyEntityListener$Type): void
 "addListener"(arg0: $NearbyEntityListener$Type): void
}

export namespace $NearbyEntityListenerSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NearbyEntityListenerSection$Type = ($NearbyEntityListenerSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NearbyEntityListenerSection_ = $NearbyEntityListenerSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/$SodiumWorldRenderer" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$BlockDestructionProgress, $BlockDestructionProgress$Type} from "packages/net/minecraft/server/level/$BlockDestructionProgress"
import {$Viewport, $Viewport$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$Viewport"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Long2ObjectMap, $Long2ObjectMap$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2ObjectMap"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SortedSet, $SortedSet$Type} from "packages/java/util/$SortedSet"
import {$RenderBuffers, $RenderBuffers$Type} from "packages/net/minecraft/client/renderer/$RenderBuffers"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $SodiumWorldRenderer {

constructor(arg0: $Minecraft$Type)

public static "instance"(): $SodiumWorldRenderer
public "reload"(): void
/**
 * 
 * @deprecated
 */
public "onChunkRemoved"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "onChunkAdded"(arg0: integer, arg1: integer): void
public "isBoxVisible"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): boolean
public "getDebugStrings"(): $Collection<(string)>
/**
 * 
 * @deprecated
 */
public "onChunkLightAdded"(arg0: integer, arg1: integer): void
public "isSectionReady"(arg0: integer, arg1: integer, arg2: integer): boolean
public "setWorld"(arg0: $ClientLevel$Type): void
public "setupTerrain"(arg0: $Camera$Type, arg1: $Viewport$Type, arg2: integer, arg3: boolean, arg4: boolean): void
public "drawChunkLayer"(arg0: $RenderType$Type, arg1: $PoseStack$Type, arg2: double, arg3: double, arg4: double): void
public "isEntityVisible"(arg0: $Entity$Type): boolean
public static "instanceNullable"(): $SodiumWorldRenderer
public "getVisibleChunkCount"(): integer
public "isTerrainRenderComplete"(): boolean
public "scheduleTerrainUpdate"(): void
public "scheduleRebuildForChunks"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: boolean): void
public "didBlockEntityRequestOutline"(): boolean
public "scheduleRebuildForChunk"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean): void
public "getChunksDebugString"(): string
public "scheduleRebuildForBlockArea"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: boolean): void
public "renderBlockEntities"(arg0: $PoseStack$Type, arg1: $RenderBuffers$Type, arg2: $Long2ObjectMap$Type<($SortedSet$Type<($BlockDestructionProgress$Type)>)>, arg3: $Camera$Type, arg4: float): void
public "forEachVisibleBlockEntity"(arg0: $Consumer$Type<($BlockEntity$Type)>): void
public "blockEntityIterator"(): $Iterator<($BlockEntity)>
get "debugStrings"(): $Collection<(string)>
set "world"(value: $ClientLevel$Type)
get "visibleChunkCount"(): integer
get "terrainRenderComplete"(): boolean
get "chunksDebugString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumWorldRenderer$Type = ($SodiumWorldRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumWorldRenderer_ = $SodiumWorldRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$BakedModelEncoder" {
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$VertexBufferWriter, $VertexBufferWriter$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter"

export class $BakedModelEncoder {

constructor()

public static "writeQuadVertices"(arg0: $VertexBufferWriter$Type, arg1: $PoseStack$Pose$Type, arg2: $ModelQuadView$Type, arg3: float, arg4: float, arg5: float, arg6: (float)[], arg7: boolean, arg8: (integer)[], arg9: integer): void
public static "writeQuadVertices"(arg0: $VertexBufferWriter$Type, arg1: $PoseStack$Pose$Type, arg2: $ModelQuadView$Type, arg3: integer, arg4: integer, arg5: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedModelEncoder$Type = ($BakedModelEncoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedModelEncoder_ = $BakedModelEncoder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder$Vertex" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ChunkVertexEncoder$Vertex {
 "x": float
 "y": float
 "z": float
 "color": integer
 "u": float
 "v": float
 "light": integer

constructor()

public static "uninitializedQuad"(): ($ChunkVertexEncoder$Vertex)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkVertexEncoder$Vertex$Type = ($ChunkVertexEncoder$Vertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkVertexEncoder$Vertex_ = $ChunkVertexEncoder$Vertex$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/console/$ConsoleSink" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MessageLevel, $MessageLevel$Type} from "packages/me/jellysquid/mods/sodium/client/gui/console/message/$MessageLevel"

export interface $ConsoleSink {

 "logMessage"(arg0: $MessageLevel$Type, arg1: $Component$Type, arg2: double): void

(arg0: $MessageLevel$Type, arg1: $Component$Type, arg2: double): void
}

export namespace $ConsoleSink {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConsoleSink$Type = ($ConsoleSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConsoleSink_ = $ConsoleSink$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/core/render/$MinecraftAccessor" {
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"

export interface $MinecraftAccessor {

 "embeddium$getGameThread"(): $Thread

(): $Thread
}

export namespace $MinecraftAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinecraftAccessor$Type = ($MinecraftAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinecraftAccessor_ = $MinecraftAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/math/$MutableVec3d" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $MutableVec3d {

constructor()

public "add"(vec: $Vec3$Type): void
public "getY"(): double
public "getZ"(): double
public "getX"(): double
public "toImmutable"(): $Vec3
get "y"(): double
get "z"(): double
get "x"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableVec3d$Type = ($MutableVec3d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableVec3d_ = $MutableVec3d$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$EntityRenderer" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$VertexBufferWriter, $VertexBufferWriter$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$ModelCuboid, $ModelCuboid$Type} from "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$ModelCuboid"

export class $EntityRenderer {

constructor()

public static "renderCuboidFast"(arg0: $PoseStack$Pose$Type, arg1: $VertexBufferWriter$Type, arg2: $ModelCuboid$Type, arg3: integer, arg4: integer, arg5: integer): void
public static "prepareNormals"(arg0: $PoseStack$Pose$Type): void
/**
 * 
 * @deprecated
 */
public static "render"(arg0: $PoseStack$Type, arg1: $VertexBufferWriter$Type, arg2: $ModelPart$Type, arg3: integer, arg4: integer, arg5: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRenderer$Type = ($EntityRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRenderer_ = $EntityRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipeline" {
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$QuadLightData, $QuadLightData$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$QuadLightData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $LightPipeline {

 "reset"(): void
 "calculate"(arg0: $ModelQuadView$Type, arg1: $BlockPos$Type, arg2: $QuadLightData$Type, arg3: $Direction$Type, arg4: $Direction$Type, arg5: boolean): void

(): void
}

export namespace $LightPipeline {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightPipeline$Type = ($LightPipeline);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightPipeline_ = $LightPipeline$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/cloned/$ClonedChunkSectionCache" {
import {$ClonedChunkSection, $ClonedChunkSection$Type} from "packages/me/jellysquid/mods/sodium/client/world/cloned/$ClonedChunkSection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $ClonedChunkSectionCache {

constructor(arg0: $Level$Type)

public "cleanup"(): void
public "acquire"(arg0: integer, arg1: integer, arg2: integer): $ClonedChunkSection
public "invalidate"(arg0: integer, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClonedChunkSectionCache$Type = ($ClonedChunkSectionCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClonedChunkSectionCache_ = $ClonedChunkSectionCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ShaderType extends $Enum<($ShaderType)> {
static readonly "VERTEX": $ShaderType
static readonly "FRAGMENT": $ShaderType
readonly "id": integer


public static "values"(): ($ShaderType)[]
public static "valueOf"(arg0: string): $ShaderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderType$Type = (("fragment") | ("vertex")) | ($ShaderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderType_ = $ShaderType$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/binding/$OptionBinding" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $OptionBinding<S, T> {

 "getValue"(arg0: S): T
 "setValue"(arg0: S, arg1: T): void
}

export namespace $OptionBinding {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionBinding$Type<S, T> = ($OptionBinding<(S), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionBinding_<S, T> = $OptionBinding$Type<(S), (T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/$ModelCuboidAccessor" {
import {$ModelCuboid, $ModelCuboid$Type} from "packages/me/jellysquid/mods/sodium/client/render/immediate/model/$ModelCuboid"

export interface $ModelCuboidAccessor {

 "embeddium$getSimpleCuboid"(): $ModelCuboid
 "sodium$copy"(): $ModelCuboid
}

export namespace $ModelCuboidAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelCuboidAccessor$Type = ($ModelCuboidAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelCuboidAccessor_ = $ModelCuboidAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/data/$QuadLightData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $QuadLightData {
readonly "br": (float)[]
readonly "lm": (integer)[]

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuadLightData$Type = ($QuadLightData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuadLightData_ = $QuadLightData$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice" {
import {$DeviceFunctions, $DeviceFunctions$Type} from "packages/me/jellysquid/mods/sodium/client/gl/functions/$DeviceFunctions"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$GLCapabilities, $GLCapabilities$Type} from "packages/org/lwjgl/opengl/$GLCapabilities"

export interface $RenderDevice {

 "getDeviceFunctions"(): $DeviceFunctions
 "getCapabilities"(): $GLCapabilities
 "createCommandList"(): $CommandList
 "makeActive"(): void
 "makeInactive"(): void
}

export namespace $RenderDevice {
const INSTANCE: $RenderDevice
function enterManagedCode(): void
function exitManagedCode(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderDevice$Type = ($RenderDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderDevice_ = $RenderDevice$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/environment/probe/$GraphicsAdapterInfo" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GraphicsAdapterVendor, $GraphicsAdapterVendor$Type} from "packages/me/jellysquid/mods/sodium/client/compatibility/environment/probe/$GraphicsAdapterVendor"

export class $GraphicsAdapterInfo extends $Record {

constructor(vendor: $GraphicsAdapterVendor$Type, name: string, version: string)

public "name"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "version"(): string
public "hashCode"(): integer
public "vendor"(): $GraphicsAdapterVendor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsAdapterInfo$Type = ($GraphicsAdapterInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsAdapterInfo_ = $GraphicsAdapterInfo$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/$User32" {
import {$MsgBoxParamSw, $MsgBoxParamSw$Type} from "packages/me/jellysquid/mods/sodium/client/platform/windows/api/msgbox/$MsgBoxParamSw"

export class $User32 {

constructor()

public static "callMessageBoxIndirectW"(arg0: $MsgBoxParamSw$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $User32$Type = ($User32);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $User32_ = $User32$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/$EquipmentSlotConstants" {
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"

export class $EquipmentSlotConstants {
static readonly "ALL": ($EquipmentSlot)[]


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EquipmentSlotConstants$Type = ($EquipmentSlotConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EquipmentSlotConstants_ = $EquipmentSlotConstants$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/core/model/$BakedModelMixin" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EmbeddiumBakedModelExtension, $EmbeddiumBakedModelExtension$Type} from "packages/org/embeddedt/embeddium/api/model/$EmbeddiumBakedModelExtension"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $BakedModelMixin extends $EmbeddiumBakedModelExtension {

 "useAmbientOcclusionWithLightEmission"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
}

export namespace $BakedModelMixin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedModelMixin$Type = ($BakedModelMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedModelMixin_ = $BakedModelMixin$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/environment/$OSInfo$OS" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $OSInfo$OS extends $Enum<($OSInfo$OS)> {
static readonly "WINDOWS": $OSInfo$OS
static readonly "LINUX": $OSInfo$OS
static readonly "UNKNOWN": $OSInfo$OS


public static "values"(): ($OSInfo$OS)[]
public static "valueOf"(arg0: string): $OSInfo$OS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OSInfo$OS$Type = (("linux") | ("windows") | ("unknown")) | ($OSInfo$OS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OSInfo$OS_ = $OSInfo$OS$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_comparator_tracking/$ComparatorTracker" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export interface $ComparatorTracker {

 "hasAnyComparatorNearby"(): boolean
 "onComparatorAdded"(arg0: $Direction$Type, arg1: integer): void
}

export namespace $ComparatorTracker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComparatorTracker$Type = ($ComparatorTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComparatorTracker_ = $ComparatorTracker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJob" {
import {$ChunkBuildContext, $ChunkBuildContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildContext"
import {$CancellationToken, $CancellationToken$Type} from "packages/me/jellysquid/mods/sodium/client/util/task/$CancellationToken"

export interface $ChunkJob extends $CancellationToken {

 "execute"(arg0: $ChunkBuildContext$Type): void
 "isStarted"(): boolean
 "isCancelled"(): boolean
 "setCancelled"(): void
}

export namespace $ChunkJob {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkJob$Type = ($ChunkJob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkJob_ = $ChunkJob$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/checks/$ModuleScanner" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModuleScanner {

constructor()

public static "checkModules"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleScanner$Type = ($ModuleScanner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleScanner_ = $ModuleScanner$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/$MessageBox" {
import {$Window, $Window$Type} from "packages/com/mojang/blaze3d/platform/$Window"
import {$MessageBox$IconType, $MessageBox$IconType$Type} from "packages/me/jellysquid/mods/sodium/client/platform/$MessageBox$IconType"

export class $MessageBox {

constructor()

public static "showMessageBox"(arg0: $Window$Type, arg1: $MessageBox$IconType$Type, arg2: string, arg3: string, arg4: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageBox$Type = ($MessageBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageBox_ = $MessageBox$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/version/$QueryResult" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $QueryResult extends $Record {

constructor(address: long, length: integer)

public "equals"(arg0: any): boolean
public "length"(): integer
public "toString"(): string
public "hashCode"(): integer
public "address"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QueryResult$Type = ($QueryResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QueryResult_ = $QueryResult$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$DefaultMaterials" {
import {$Material, $Material$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$Material"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $DefaultMaterials {
static readonly "SOLID": $Material
static readonly "CUTOUT": $Material
static readonly "CUTOUT_MIPPED": $Material
static readonly "TRANSLUCENT": $Material

constructor()

public static "forRenderLayer"(arg0: $RenderType$Type): $Material
public static "forFluidState"(arg0: $FluidState$Type): $Material
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultMaterials$Type = ($DefaultMaterials);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultMaterials_ = $DefaultMaterials$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/tuples/$RefIntPair" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $RefIntPair<A> extends $Record {

constructor(left: A, right: integer)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "left"(): A
public "right"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RefIntPair$Type<A> = ($RefIntPair<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RefIntPair_<A> = $RefIntPair$Type<(A)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/pushable/$BlockCachingEntity" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export interface $BlockCachingEntity {

 "lithiumOnBlockCacheDeleted"(): void
 "lithiumOnBlockCacheSet"(newState: $BlockState$Type): void
 "getCachedFeetBlockState"(): $BlockState
 "lithiumSetClimbingMobCachingSectionUpdateBehavior"(listening: boolean): void

(): void
}

export namespace $BlockCachingEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCachingEntity$Type = ($BlockCachingEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCachingEntity_ = $BlockCachingEntity$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferTarget" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GlBufferTarget extends $Enum<($GlBufferTarget)> {
static readonly "ARRAY_BUFFER": $GlBufferTarget
static readonly "ELEMENT_BUFFER": $GlBufferTarget
static readonly "COPY_READ_BUFFER": $GlBufferTarget
static readonly "COPY_WRITE_BUFFER": $GlBufferTarget
static readonly "VALUES": ($GlBufferTarget)[]
static readonly "COUNT": integer


public "getTargetParameter"(): integer
public static "values"(): ($GlBufferTarget)[]
public static "valueOf"(arg0: string): $GlBufferTarget
public "getBindingParameter"(): integer
get "targetParameter"(): integer
get "bindingParameter"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferTarget$Type = (("element_buffer") | ("copy_read_buffer") | ("array_buffer") | ("copy_write_buffer")) | ($GlBufferTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferTarget_ = $GlBufferTarget$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$FallbackStagingBuffer" {
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$StagingBuffer, $StagingBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $FallbackStagingBuffer implements $StagingBuffer {

constructor(arg0: $CommandList$Type)

public "toString"(): string
public "flush"(arg0: $CommandList$Type): void
public "delete"(arg0: $CommandList$Type): void
public "flip"(): void
public "enqueueCopy"(arg0: $CommandList$Type, arg1: $ByteBuffer$Type, arg2: $GlBuffer$Type, arg3: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FallbackStagingBuffer$Type = ($FallbackStagingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FallbackStagingBuffer_ = $FallbackStagingBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$ListUtil" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export class $ListUtil {

constructor()

public static "updateList"<T>(arg0: $Collection$Type<(T)>, arg1: $Collection$Type<(T)>, arg2: $Collection$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListUtil$Type = ($ListUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListUtil_ = $ListUtil$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlTessellation" {
import {$GlPrimitiveType, $GlPrimitiveType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlPrimitiveType"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"

export interface $GlTessellation {

 "delete"(arg0: $CommandList$Type): void
 "bind"(arg0: $CommandList$Type): void
 "getPrimitiveType"(): $GlPrimitiveType
 "unbind"(arg0: $CommandList$Type): void
}

export namespace $GlTessellation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlTessellation$Type = ($GlTessellation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlTessellation_ = $GlTessellation$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/lock/$NullReadWriteLock" {
import {$ReadWriteLock, $ReadWriteLock$Type} from "packages/java/util/concurrent/locks/$ReadWriteLock"
import {$Lock, $Lock$Type} from "packages/java/util/concurrent/locks/$Lock"

export class $NullReadWriteLock implements $ReadWriteLock {

constructor()

public "readLock"(): $Lock
public "writeLock"(): $Lock
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullReadWriteLock$Type = ($NullReadWriteLock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullReadWriteLock_ = $NullReadWriteLock$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeEmitter" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$InventoryChangeListener, $InventoryChangeListener$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener"

export interface $InventoryChangeEmitter {

 "stopForwardingMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
 "emitFirstComparatorAdded"(): void
 "forwardMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
 "emitContentModified"(): void
 "emitCallbackReplaced"(): void
 "emitStackListReplaced"(): void
 "forwardContentChangeOnce"(arg0: $InventoryChangeListener$Type, arg1: $LithiumStackList$Type, arg2: $InventoryChangeTracker$Type): void
 "emitRemoved"(): void
}

export namespace $InventoryChangeEmitter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryChangeEmitter$Type = ($InventoryChangeEmitter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryChangeEmitter_ = $InventoryChangeEmitter$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$MinecraftOptionsStorage" {
import {$Options, $Options$Type} from "packages/net/minecraft/client/$Options"
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"

export class $MinecraftOptionsStorage implements $OptionStorage<($Options)> {

constructor()

public "save"(): void
public "getData"(): $Options
get "data"(): $Options
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinecraftOptionsStorage$Type = ($MinecraftOptionsStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinecraftOptionsStorage_ = $MinecraftOptionsStorage$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttributeFormat" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $GlVertexAttributeFormat extends $Record {
static readonly "FLOAT": $GlVertexAttributeFormat
static readonly "UNSIGNED_SHORT": $GlVertexAttributeFormat
static readonly "UNSIGNED_BYTE": $GlVertexAttributeFormat
static readonly "UNSIGNED_INT": $GlVertexAttributeFormat

constructor(typeId: integer, size: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "size"(): integer
public "typeId"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexAttributeFormat$Type = ($GlVertexAttributeFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexAttributeFormat_ = $GlVertexAttributeFormat$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/smooth/$AoFaceData" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"

export class $AoFaceData {
readonly "lm": (integer)[]
readonly "ao": (float)[]
readonly "bl": (float)[]
readonly "sl": (float)[]


public "reset"(): void
public "initLightData"(arg0: $LightDataAccess$Type, arg1: $BlockPos$Type, arg2: $Direction$Type, arg3: boolean): void
public "getBlendedSkyLight"(arg0: (float)[]): float
public "getBlendedShade"(arg0: (float)[]): float
public "hasLightData"(): boolean
public "unpackLightData"(): void
public "getBlendedBlockLight"(arg0: (float)[]): float
public "hasUnpackedLightData"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AoFaceData$Type = ($AoFaceData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AoFaceData_ = $AoFaceData$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/api/inventory/$LithiumDefaultedList" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LithiumDefaultedList {

 "changedInteractionConditions"(): void

(): void
}

export namespace $LithiumDefaultedList {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumDefaultedList$Type = ($LithiumDefaultedList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumDefaultedList_ = $LithiumDefaultedList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildContext" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$BlockRenderCache, $BlockRenderCache$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderCache"
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ChunkBuildBuffers, $ChunkBuildBuffers$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildBuffers"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ChunkBuildContext {
readonly "buffers": $ChunkBuildBuffers
readonly "cache": $BlockRenderCache

constructor(arg0: $ClientLevel$Type, arg1: $ChunkVertexType$Type)

public "cleanup"(): void
public "captureAdditionalSprite"(arg0: $TextureAtlasSprite$Type): void
public "getAdditionalCapturedSprites"(): $Iterable<($TextureAtlasSprite)>
public "setCaptureAdditionalSprites"(arg0: boolean): void
get "additionalCapturedSprites"(): $Iterable<($TextureAtlasSprite)>
set "captureAdditionalSprites"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuildContext$Type = ($ChunkBuildContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuildContext_ = $ChunkBuildContext$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/util/entity_movement_tracking/$ServerEntityManagerAccessor" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$EntityAccess, $EntityAccess$Type} from "packages/net/minecraft/world/level/entity/$EntityAccess"

export interface $ServerEntityManagerAccessor<T extends $EntityAccess> {

 "getCache"(): $EntitySectionStorage<(T)>

(): $EntitySectionStorage<(T)>
}

export namespace $ServerEntityManagerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEntityManagerAccessor$Type<T> = ($ServerEntityManagerAccessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEntityManagerAccessor_<T> = $ServerEntityManagerAccessor$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$MaskedList" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractList, $AbstractList$Type} from "packages/java/util/$AbstractList"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$ObjectArrayList, $ObjectArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectArrayList"

export class $MaskedList<E> extends $AbstractList<(E)> {

constructor(allElements: $ObjectArrayList$Type<(E)>, defaultVisibility: boolean)
constructor()

public "add"(e: E): boolean
public "remove"(o: any): boolean
public "get"(index: integer): E
public "size"(): integer
public "iterator"(): $Iterator<(E)>
public "spliterator"(): $Spliterator<(E)>
public "totalSize"(): integer
public "addOrSet"(element: E, visible: boolean): void
public "setVisible"(element: E, visible: boolean): void
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
export type $MaskedList$Type<E> = ($MaskedList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaskedList_<E> = $MaskedList$Type<(E)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/$MathUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MathUtil {

constructor()

public static "isPowerOfTwo"(arg0: integer): boolean
public static "toMib"(arg0: long): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathUtil$Type = ($MathUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathUtil_ = $MathUtil$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderSortTask" {
import {$TranslucentQuadAnalyzer$SortState, $TranslucentQuadAnalyzer$SortState$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState"
import {$ChunkBuildOutput, $ChunkBuildOutput$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildOutput"
import {$RenderSection, $RenderSection$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection"
import {$ChunkBuilderTask, $ChunkBuilderTask$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderTask"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ChunkBuilderSortTask extends $ChunkBuilderTask<($ChunkBuildOutput)> {

constructor(arg0: $RenderSection$Type, arg1: float, arg2: float, arg3: float, arg4: integer, arg5: $Map$Type<($TerrainRenderPass$Type), ($TranslucentQuadAnalyzer$SortState$Type)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuilderSortTask$Type = ($ChunkBuilderSortTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuilderSortTask_ = $ChunkBuilderSortTask$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$CuboidVoxelSet" {
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"

export class $CuboidVoxelSet extends $DiscreteVoxelShape {


public "lastFull"(axis: $Direction$Axis$Type): integer
public "isEmpty"(): boolean
public "firstFull"(axis: $Direction$Axis$Type): integer
public "fill"(x: integer, y: integer, z: integer): void
public "isFull"(x: integer, y: integer, z: integer): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuboidVoxelSet$Type = ($CuboidVoxelSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuboidVoxelSet_ = $CuboidVoxelSet$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/buffers/$BakedChunkModelBuilder" {
import {$ModelQuadFacing, $ModelQuadFacing$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadFacing"
import {$ChunkMeshBufferBuilder, $ChunkMeshBufferBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/builder/$ChunkMeshBufferBuilder"
import {$ChunkModelBuilder, $ChunkModelBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/buffers/$ChunkModelBuilder"
import {$BuiltSectionInfo$Builder, $BuiltSectionInfo$Builder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo$Builder"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $BakedChunkModelBuilder implements $ChunkModelBuilder {

constructor(arg0: ($ChunkMeshBufferBuilder$Type)[], arg1: boolean)

public "begin"(arg0: $BuiltSectionInfo$Builder$Type, arg1: integer): void
public "destroy"(): void
public "getVertexBuffer"(arg0: $ModelQuadFacing$Type): $ChunkMeshBufferBuilder
public "addSprite"(arg0: $TextureAtlasSprite$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedChunkModelBuilder$Type = ($BakedChunkModelBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedChunkModelBuilder_ = $BakedChunkModelBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/color/$DefaultColorProviders" {
import {$ColorProvider, $ColorProvider$Type} from "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProvider"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $DefaultColorProviders {

constructor()

public static "adapt"(arg0: $BlockColor$Type): $ColorProvider<($BlockState)>
public static "getFluidProvider"(): $ColorProvider<($FluidState)>
get "fluidProvider"(): $ColorProvider<($FluidState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultColorProviders$Type = ($DefaultColorProviders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultColorProviders_ = $DefaultColorProviders$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/color/interop/$ItemColorsExtended" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"

export interface $ItemColorsExtended {

 "sodium$getColorProvider"(arg0: $ItemStack$Type): $ItemColor

(arg0: $ItemStack$Type): $ItemColor
}

export namespace $ItemColorsExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemColorsExtended$Type = ($ItemColorsExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemColorsExtended_ = $ItemColorsExtended$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/state/$StatePropertyTableCache" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$FastImmutableTableCache, $FastImmutableTableCache$Type} from "packages/me/jellysquid/mods/lithium/common/state/$FastImmutableTableCache"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $StatePropertyTableCache {
static readonly "BLOCK_STATE_TABLE": $FastImmutableTableCache<($Property<(any)>), ($Comparable<(any)>), ($BlockState)>
static readonly "FLUID_STATE_TABLE": $FastImmutableTableCache<($Property<(any)>), ($Comparable<(any)>), ($FluidState)>

constructor()

public static "getTableCache"<S, O>(owner: O): $FastImmutableTableCache<($Property<(any)>), ($Comparable<(any)>), (S)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StatePropertyTableCache$Type = ($StatePropertyTableCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StatePropertyTableCache_ = $StatePropertyTableCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/parameters/$AlphaCutoffParameter" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $AlphaCutoffParameter extends $Enum<($AlphaCutoffParameter)> {
static readonly "ZERO": $AlphaCutoffParameter
static readonly "ONE_TENTH": $AlphaCutoffParameter
static readonly "HALF": $AlphaCutoffParameter
static readonly "ONE": $AlphaCutoffParameter


public static "values"(): ($AlphaCutoffParameter)[]
public static "valueOf"(arg0: string): $AlphaCutoffParameter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlphaCutoffParameter$Type = (("zero") | ("half") | ("one") | ("one_tenth")) | ($AlphaCutoffParameter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlphaCutoffParameter_ = $AlphaCutoffParameter$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/environment/$OSInfo" {
import {$OSInfo$OS, $OSInfo$OS$Type} from "packages/me/jellysquid/mods/sodium/client/compatibility/environment/$OSInfo$OS"

export class $OSInfo {

constructor()

public static "getOS"(): $OSInfo$OS
get "oS"(): $OSInfo$OS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OSInfo$Type = ($OSInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OSInfo_ = $OSInfo$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkMeshFormats" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"

export class $ChunkMeshFormats {
static readonly "COMPACT": $ChunkVertexType
static readonly "VANILLA_LIKE": $ChunkVertexType

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMeshFormats$Type = ($ChunkMeshFormats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMeshFormats_ = $ChunkMeshFormats$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$EquipmentEntity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EquipmentEntity {

 "lithiumOnEquipmentChanged"(): void
}

export namespace $EquipmentEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EquipmentEntity$Type = ($EquipmentEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EquipmentEntity_ = $EquipmentEntity$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/color/$BoxBlur$ColorBuffer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BoxBlur$ColorBuffer {

constructor(arg0: integer, arg1: integer)

public "get"(arg0: integer, arg1: integer): integer
public "set"(arg0: integer, arg1: integer, arg2: integer): void
public static "getIndex"(arg0: integer, arg1: integer, arg2: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoxBlur$ColorBuffer$Type = ($BoxBlur$ColorBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoxBlur$ColorBuffer_ = $BoxBlur$ColorBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionGroup$Builder" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionGroup, $OptionGroup$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionGroup"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $OptionGroup$Builder {

constructor()

public "add"(arg0: $Option$Type<(any)>): $OptionGroup$Builder
public "build"(): $OptionGroup
public "setId"(arg0: $ResourceLocation$Type): $OptionGroup$Builder
public "setId"(arg0: $OptionIdentifier$Type<(void)>): $OptionGroup$Builder
set "id"(value: $ResourceLocation$Type)
set "id"(value: $OptionIdentifier$Type<(void)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionGroup$Builder$Type = ($OptionGroup$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionGroup$Builder_ = $OptionGroup$Builder$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$NavigatingEntity" {
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"

export interface $NavigatingEntity {

 "getRegisteredNavigation"(): $PathNavigation
 "isRegisteredToWorld"(): boolean
 "setRegisteredToWorld"(arg0: $PathNavigation$Type): void
 "updateNavigationRegistration"(): void
}

export namespace $NavigatingEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NavigatingEntity$Type = ($NavigatingEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NavigatingEntity_ = $NavigatingEntity$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/msgbox/$MsgBoxCallback" {
import {$Callback, $Callback$Type} from "packages/org/lwjgl/system/$Callback"
import {$MsgBoxCallbackI, $MsgBoxCallbackI$Type} from "packages/me/jellysquid/mods/sodium/client/platform/windows/api/msgbox/$MsgBoxCallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export class $MsgBoxCallback extends $Callback implements $MsgBoxCallbackI {


public static "create"(arg0: $MsgBoxCallbackI$Type): $MsgBoxCallback
public "invoke"(arg0: long): void
public "callback"(arg0: long, arg1: long): void
public "getCallInterface"(): $FFICIF
get "callInterface"(): $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MsgBoxCallback$Type = ($MsgBoxCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MsgBoxCallback_ = $MsgBoxCallback$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/smooth/$AoCompletionFlags" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AoCompletionFlags {
static readonly "HAS_LIGHT_DATA": integer
static readonly "HAS_UNPACKED_LIGHT_DATA": integer


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AoCompletionFlags$Type = ($AoCompletionFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AoCompletionFlags_ = $AoCompletionFlags$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/$Pos" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Pos {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pos$Type = ($Pos);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pos_ = $Pos$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/compat/fabric_transfer_api_v1/$FabricTransferApiCompat" {
import {$HopperBlockEntity, $HopperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HopperBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $FabricTransferApiCompat {
static readonly "FABRIC_TRANSFER_API_V_1_PRESENT": boolean

constructor()

public static "canHopperInteractWithApiInventory"(hopperBlockEntity: $HopperBlockEntity$Type, hopperState: $BlockState$Type, extracting: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FabricTransferApiCompat$Type = ($FabricTransferApiCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FabricTransferApiCompat_ = $FabricTransferApiCompat$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/blender/$BlendedColorProvider" {
import {$ColorProvider, $ColorProvider$Type} from "packages/me/jellysquid/mods/sodium/client/model/color/$ColorProvider"
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$WorldSlice, $WorldSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/$WorldSlice"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlendedColorProvider<T> implements $ColorProvider<(T)> {

constructor()

public "getColors"(arg0: $WorldSlice$Type, arg1: $BlockPos$Type, arg2: T, arg3: $ModelQuadView$Type, arg4: (integer)[]): void
public static "checkBlendingEnabled"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlendedColorProvider$Type<T> = ($BlendedColorProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlendedColorProvider_<T> = $BlendedColorProvider$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat$Builder" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$GlVertexFormat, $GlVertexFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat"
import {$GlVertexAttributeFormat, $GlVertexAttributeFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexAttributeFormat"

export class $GlVertexFormat$Builder<T extends $Enum<(T)>> {

constructor(arg0: $Class$Type<(T)>, arg1: integer)

public "build"(): $GlVertexFormat<(T)>
public "addElement"(arg0: T, arg1: integer, arg2: $GlVertexAttributeFormat$Type, arg3: integer, arg4: boolean, arg5: boolean): $GlVertexFormat$Builder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexFormat$Builder$Type<T> = ($GlVertexFormat$Builder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexFormat$Builder_<T> = $GlVertexFormat$Builder$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$MappedStagingBuffer" {
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$StagingBuffer, $StagingBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$RenderDevice, $RenderDevice$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$RenderDevice"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $MappedStagingBuffer implements $StagingBuffer {

constructor(arg0: $CommandList$Type)
constructor(arg0: $CommandList$Type, arg1: integer)

public "toString"(): string
public "flush"(arg0: $CommandList$Type): void
public "delete"(arg0: $CommandList$Type): void
public "flip"(): void
public static "isSupported"(arg0: $RenderDevice$Type): boolean
public "enqueueCopy"(arg0: $CommandList$Type, arg1: $ByteBuffer$Type, arg2: $GlBuffer$Type, arg3: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappedStagingBuffer$Type = ($MappedStagingBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappedStagingBuffer_ = $MappedStagingBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegionManager" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ChunkBuildOutput, $ChunkBuildOutput$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildOutput"
import {$StagingBuffer, $StagingBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$RenderRegion, $RenderRegion$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegion"

export class $RenderRegionManager {

constructor(arg0: $CommandList$Type)

public "update"(): void
public "delete"(arg0: $CommandList$Type): void
public "createForChunk"(arg0: integer, arg1: integer, arg2: integer): $RenderRegion
public "uploadMeshes"(arg0: $CommandList$Type, arg1: $Collection$Type<($ChunkBuildOutput$Type)>): void
public "getLoadedRegions"(): $Collection<($RenderRegion)>
public "getStagingBuffer"(): $StagingBuffer
get "loadedRegions"(): $Collection<($RenderRegion)>
get "stagingBuffer"(): $StagingBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderRegionManager$Type = ($RenderRegionManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderRegionManager_ = $RenderRegionManager$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkShaderOptions" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$ChunkFogMode, $ChunkFogMode$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/shader/$ChunkFogMode"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$ShaderConstants, $ShaderConstants$Type} from "packages/me/jellysquid/mods/sodium/client/gl/shader/$ShaderConstants"

export class $ChunkShaderOptions extends $Record {

/**
 * 
 * @deprecated
 */
constructor(arg0: $ChunkFogMode$Type, arg1: $TerrainRenderPass$Type)
constructor(fog: $ChunkFogMode$Type, pass: $TerrainRenderPass$Type, vertexType: $ChunkVertexType$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "constants"(): $ShaderConstants
public "pass"(): $TerrainRenderPass
public "vertexType"(): $ChunkVertexType
public "fog"(): $ChunkFogMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkShaderOptions$Type = ($ChunkShaderOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkShaderOptions_ = $ChunkShaderOptions$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/control/$TickBoxControl" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ControlElement, $ControlElement$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlElement"
import {$Control, $Control$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"

export class $TickBoxControl implements $Control<(boolean)> {

constructor(arg0: $Option$Type<(boolean)>)

public "getMaxWidth"(): integer
public "createElement"(arg0: $Dim2i$Type): $ControlElement<(boolean)>
public "getOption"(): $Option<(boolean)>
get "maxWidth"(): integer
get "option"(): $Option<(boolean)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickBoxControl$Type = ($TickBoxControl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickBoxControl_ = $TickBoxControl$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderContext" {
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$WorldSlice, $WorldSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/$WorldSlice"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BlockRenderContext {

constructor(arg0: $WorldSlice$Type)

public "update"(arg0: $BlockPos$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BakedModel$Type, arg4: long, arg5: $ModelData$Type, arg6: $RenderType$Type): void
public "stack"(): $PoseStack
public "pos"(): $BlockPos
public "state"(): $BlockState
public "seed"(): long
public "origin"(): $Vector3fc
public "world"(): $WorldSlice
public "renderLayer"(): $RenderType
public "model"(): $BakedModel
public "localSlice"(): $BlockAndTintGetter
public "modelData"(): $ModelData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRenderContext$Type = ($BlockRenderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRenderContext_ = $BlockRenderContext$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/util/$RenderAsserts" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RenderAsserts {

constructor()

public static "validateCurrentThread"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderAsserts$Type = ($RenderAsserts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderAsserts_ = $RenderAsserts$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/tuples/$WorldSectionBox" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $WorldSectionBox extends $Record {

constructor(world: $Level$Type, chunkX1: integer, chunkY1: integer, chunkZ1: integer, chunkX2: integer, chunkY2: integer, chunkZ2: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "world"(): $Level
public "chunkX1"(): integer
public "chunkZ1"(): integer
public "chunkX2"(): integer
public "chunkZ2"(): integer
public "numSections"(): integer
public "chunkY1"(): integer
public "chunkY2"(): integer
public "matchesRelevantBlocksBox"(box: $AABB$Type): boolean
public static "relevantExpandedBlocksBox"(world: $Level$Type, box: $AABB$Type): $WorldSectionBox
public static "entityAccessBox"(world: $Level$Type, box: $AABB$Type): $WorldSectionBox
public static "relevantFluidBox"(world: $Level$Type, box: $AABB$Type): $WorldSectionBox
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldSectionBox$Type = ($WorldSectionBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldSectionBox_ = $WorldSectionBox$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityTracker" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$TargetingConditions, $TargetingConditions$Type} from "packages/net/minecraft/world/entity/ai/targeting/$TargetingConditions"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$NearbyEntityListener, $NearbyEntityListener$Type} from "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListener"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Range6Int, $Range6Int$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$Range6Int"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$ClassInstanceMultiMap, $ClassInstanceMultiMap$Type} from "packages/net/minecraft/util/$ClassInstanceMultiMap"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $NearbyEntityTracker<T extends $LivingEntity> implements $NearbyEntityListener {

constructor(clazz: $Class$Type<(T)>, entity: $LivingEntity$Type, boxRadius: $Vec3i$Type)

public "toString"(): string
public "getEntityClass"(): $Class<(any)>
public "getChunkRange"(): $Range6Int
public "onEntityLeftRange"(entity: $Entity$Type): void
public "getClosestEntity"(box: $AABB$Type, targetPredicate: $TargetingConditions$Type, x: double, y: double, z: double): T
public "onEntityEnteredRange"(entity: $Entity$Type): void
public "onSectionEnteredRange"<T>(entityTrackingSection: any, collection: $ClassInstanceMultiMap$Type<(T)>): void
public "removeFromAllChunksInRange"(entityCache: $EntitySectionStorage$Type<(any)>, prevCenterPos: $SectionPos$Type): void
public "updateChunkRegistrations"(entityCache: $EntitySectionStorage$Type<(any)>, prevCenterPos: $SectionPos$Type, prevChunkRange: $Range6Int$Type, newCenterPos: $SectionPos$Type, newChunkRange: $Range6Int$Type): void
public "addToAllChunksInRange"(entityCache: $EntitySectionStorage$Type<(any)>, newCenterPos: $SectionPos$Type): void
public "onSectionLeftRange"<T>(entityTrackingSection: any, collection: $ClassInstanceMultiMap$Type<(T)>): void
get "entityClass"(): $Class<(any)>
get "chunkRange"(): $Range6Int
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NearbyEntityTracker$Type<T> = ($NearbyEntityTracker<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NearbyEntityTracker_<T> = $NearbyEntityTracker$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$GraphicsQuality" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$TextProvider, $TextProvider$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$TextProvider"
import {$GraphicsStatus, $GraphicsStatus$Type} from "packages/net/minecraft/client/$GraphicsStatus"

export class $SodiumGameOptions$GraphicsQuality extends $Enum<($SodiumGameOptions$GraphicsQuality)> implements $TextProvider {
static readonly "DEFAULT": $SodiumGameOptions$GraphicsQuality
static readonly "FANCY": $SodiumGameOptions$GraphicsQuality
static readonly "FAST": $SodiumGameOptions$GraphicsQuality


public static "values"(): ($SodiumGameOptions$GraphicsQuality)[]
public static "valueOf"(arg0: string): $SodiumGameOptions$GraphicsQuality
public "getLocalizedName"(): $Component
public "isFancy"(arg0: $GraphicsStatus$Type): boolean
get "localizedName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptions$GraphicsQuality$Type = (("default") | ("fancy") | ("fast")) | ($SodiumGameOptions$GraphicsQuality);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptions$GraphicsQuality_ = $SodiumGameOptions$GraphicsQuality$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/vertex/buffer/$ExtendedBufferBuilder" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$SodiumBufferBuilder, $SodiumBufferBuilder$Type} from "packages/me/jellysquid/mods/sodium/client/render/vertex/buffer/$SodiumBufferBuilder"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$VertexBufferWriter, $VertexBufferWriter$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter"

export interface $ExtendedBufferBuilder extends $VertexBufferWriter {

 "sodium$getBuffer"(): $ByteBuffer
 "sodium$getDelegate"(): $SodiumBufferBuilder
 "sodium$moveToNextVertex"(): void
 "sodium$usingFixedColor"(): boolean
 "sodium$getFormatDescription"(): $VertexFormatDescription
 "sodium$getElementOffset"(): integer
 "push"(arg0: $MemoryStack$Type, arg1: long, arg2: integer, arg3: $VertexFormatDescription$Type): void
 "canUseIntrinsics"(): boolean
/**
 * 
 * @deprecated
 */
 "isFullWriter"(): boolean
}

export namespace $ExtendedBufferBuilder {
function of(arg0: $VertexConsumer$Type): $VertexBufferWriter
function copyInto(arg0: $VertexBufferWriter$Type, arg1: $MemoryStack$Type, arg2: long, arg3: integer, arg4: $VertexFormatDescription$Type): void
function tryOf(arg0: $VertexConsumer$Type): $VertexBufferWriter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedBufferBuilder$Type = ($ExtendedBufferBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedBufferBuilder_ = $ExtendedBufferBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/pushable/$EntityPushablePredicate" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"

export class $EntityPushablePredicate<S> implements $Predicate<(S)> {

constructor()

public static "and"<T>(first: $Predicate$Type<(any)>, second: $Predicate$Type<(any)>): $Predicate<(T)>
public "test"(arg0: S): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<(S)>
public "negate"(): $Predicate<(S)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<(S)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(S)>
public static "isEqual"<T>(arg0: any): $Predicate<(S)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityPushablePredicate$Type<S> = ($EntityPushablePredicate<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityPushablePredicate_<S> = $EntityPushablePredicate$Type<(S)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$HashedReferenceList" {
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

export class $HashedReferenceList<T> implements $List<(T)> {

constructor(list: $List$Type<(T)>)

public "add"(t: T): boolean
public "add"(index: integer, element: T): void
public "remove"(o: any): boolean
public "remove"(index: integer): T
public "get"(index: integer): T
public "indexOf"(o: any): integer
public "clear"(): void
public "lastIndexOf"(o: any): integer
public "isEmpty"(): boolean
public "size"(): integer
public "subList"(fromIndex: integer, toIndex: integer): $List<(T)>
public "toArray"<T1>(a: (T1)[]): (T1)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(T)>
public "contains"(o: any): boolean
public "addAll"(index: integer, c: $Collection$Type<(any)>): boolean
public "addAll"(c: $Collection$Type<(any)>): boolean
public "set"(index: integer, element: T): T
public "removeAll"(c: $Collection$Type<(any)>): boolean
public "retainAll"(c: $Collection$Type<(any)>): boolean
public "listIterator"(index: integer): $ListIterator<(T)>
public "listIterator"(): $ListIterator<(T)>
public "containsAll"(c: $Collection$Type<(any)>): boolean
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
export type $HashedReferenceList$Type<T> = ($HashedReferenceList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HashedReferenceList_<T> = $HashedReferenceList$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferMapFlags" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$EnumBit, $EnumBit$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBit"

export class $GlBufferMapFlags extends $Enum<($GlBufferMapFlags)> implements $EnumBit {
static readonly "READ": $GlBufferMapFlags
static readonly "WRITE": $GlBufferMapFlags
static readonly "PERSISTENT": $GlBufferMapFlags
static readonly "INVALIDATE_BUFFER": $GlBufferMapFlags
static readonly "INVALIDATE_RANGE": $GlBufferMapFlags
static readonly "EXPLICIT_FLUSH": $GlBufferMapFlags
static readonly "COHERENT": $GlBufferMapFlags
static readonly "UNSYNCHRONIZED": $GlBufferMapFlags


public static "values"(): ($GlBufferMapFlags)[]
public static "valueOf"(arg0: string): $GlBufferMapFlags
public "getBits"(): integer
get "bits"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferMapFlags$Type = (("unsynchronized") | ("read") | ("invalidate_buffer") | ("coherent") | ("explicit_flush") | ("persistent") | ("write") | ("invalidate_range")) | ($GlBufferMapFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferMapFlags_ = $GlBufferMapFlags$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/config/$LithiumConfig" {
import {$AbstractCaffeineConfigMixinPlugin, $AbstractCaffeineConfigMixinPlugin$Type} from "packages/net/caffeinemc/caffeineconfig/$AbstractCaffeineConfigMixinPlugin"

export class $LithiumConfig extends $AbstractCaffeineConfigMixinPlugin {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumConfig$Type = ($LithiumConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumConfig_ = $LithiumConfig$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/$ChunkView" {
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"

export interface $ChunkView {

 "getLoadedChunk"(arg0: integer, arg1: integer): $ChunkAccess

(arg0: integer, arg1: integer): $ChunkAccess
}

export namespace $ChunkView {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkView$Type = ($ChunkView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkView_ = $ChunkView$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$FluidCachingEntity" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FluidCachingEntity {

}

export namespace $FluidCachingEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidCachingEntity$Type = ($FluidCachingEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidCachingEntity_ = $FluidCachingEntity$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/$LightMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $LightMode extends $Enum<($LightMode)> {
static readonly "SMOOTH": $LightMode
static readonly "FLAT": $LightMode


public static "values"(): ($LightMode)[]
public static "valueOf"(arg0: string): $LightMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightMode$Type = (("flat") | ("smooth")) | ($LightMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightMode_ = $LightMode$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferUsage" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GlBufferUsage extends $Enum<($GlBufferUsage)> {
static readonly "STREAM_DRAW": $GlBufferUsage
static readonly "STREAM_READ": $GlBufferUsage
static readonly "STREAM_COPY": $GlBufferUsage
static readonly "STATIC_DRAW": $GlBufferUsage
static readonly "STATIC_READ": $GlBufferUsage
static readonly "STATIC_COPY": $GlBufferUsage
static readonly "DYNAMIC_DRAW": $GlBufferUsage
static readonly "DYNAMIC_READ": $GlBufferUsage
static readonly "DYNAMIC_COPY": $GlBufferUsage


public static "values"(): ($GlBufferUsage)[]
public static "valueOf"(arg0: string): $GlBufferUsage
public "getId"(): integer
get "id"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlBufferUsage$Type = (("static_draw") | ("dynamic_copy") | ("stream_read") | ("dynamic_draw") | ("static_copy") | ("stream_draw") | ("stream_copy") | ("dynamic_read") | ("static_read")) | ($GlBufferUsage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlBufferUsage_ = $GlBufferUsage$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/builder/$ChunkMeshBufferBuilder" {
import {$TranslucentQuadAnalyzer$SortState, $TranslucentQuadAnalyzer$SortState$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState"
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$Material, $Material$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/material/$Material"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ChunkVertexEncoder$Vertex, $ChunkVertexEncoder$Vertex$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder$Vertex"

export class $ChunkMeshBufferBuilder {

constructor(arg0: $ChunkVertexType$Type, arg1: integer, arg2: boolean)

public "isEmpty"(): boolean
public "count"(): integer
public "start"(arg0: integer): void
public "destroy"(): void
public "slice"(): $ByteBuffer
public "push"(arg0: ($ChunkVertexEncoder$Vertex$Type)[], arg1: $Material$Type): void
public "getSortState"(): $TranslucentQuadAnalyzer$SortState
get "empty"(): boolean
get "sortState"(): $TranslucentQuadAnalyzer$SortState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkMeshBufferBuilder$Type = ($ChunkMeshBufferBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkMeshBufferBuilder_ = $ChunkMeshBufferBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementTracker" {
import {$WorldSectionBox, $WorldSectionBox$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$WorldSectionBox"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$EntityAccess, $EntityAccess$Type} from "packages/net/minecraft/world/level/entity/$EntityAccess"
import {$SectionedEntityMovementListener, $SectionedEntityMovementListener$Type} from "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementListener"
import {$EntityMovementTrackerSection, $EntityMovementTrackerSection$Type} from "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$EntityMovementTrackerSection"

export class $SectionedEntityMovementTracker<E extends $EntityAccess, S> {

constructor(interactionChunks: $WorldSectionBox$Type, clazz: $Class$Type<(S)>)

public "listenToEntityMovementOnce"(listener: $SectionedEntityMovementListener$Type): void
public "equals"(obj: any): boolean
public "hashCode"(): integer
public "register"(world: $ServerLevel$Type): void
public "onSectionEnteredRange"(section: $EntityMovementTrackerSection$Type): void
public "isUnchangedSince"(lastCheckedTime: long): boolean
public "unRegister"(world: $ServerLevel$Type): void
public "onSectionLeftRange"(section: $EntityMovementTrackerSection$Type): void
public "emitEntityMovement"(classMask: integer, section: $EntityMovementTrackerSection$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SectionedEntityMovementTracker$Type<E, S> = ($SectionedEntityMovementTracker<(E), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SectionedEntityMovementTracker_<E, S> = $SectionedEntityMovementTracker$Type<(E), (S)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ChunkStatus" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ChunkStatus {
static readonly "FLAG_HAS_BLOCK_DATA": integer
static readonly "FLAG_HAS_LIGHT_DATA": integer
static readonly "FLAG_ALL": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkStatus$Type = ($ChunkStatus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkStatus_ = $ChunkStatus$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/binding/compat/$VanillaBooleanOptionBinding" {
import {$OptionInstance, $OptionInstance$Type} from "packages/net/minecraft/client/$OptionInstance"
import {$Options, $Options$Type} from "packages/net/minecraft/client/$Options"
import {$OptionBinding, $OptionBinding$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/binding/$OptionBinding"

export class $VanillaBooleanOptionBinding implements $OptionBinding<($Options), (boolean)> {

constructor(arg0: $OptionInstance$Type<(boolean)>)

public "getValue"(arg0: $Options$Type): boolean
public "setValue"(arg0: $Options$Type, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaBooleanOptionBinding$Type = ($VanillaBooleanOptionBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaBooleanOptionBinding_ = $VanillaBooleanOptionBinding$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/$MixinClassValidator" {
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"

export class $MixinClassValidator {

constructor()

public static "isMixinClass"(arg0: $ClassNode$Type): boolean
public static "isMixinClass"(arg0: $Path$Type): boolean
public static "fromBytecode"(arg0: (byte)[]): $ClassNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinClassValidator$Type = ($MixinClassValidator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinClassValidator_ = $MixinClassValidator$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkBuilder" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$ChunkJob, $ChunkJob$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJob"
import {$ChunkBuilderTask, $ChunkBuilderTask$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderTask"
import {$ChunkJobTyped, $ChunkJobTyped$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobTyped"
import {$ChunkJobResult, $ChunkJobResult$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobResult"

export class $ChunkBuilder {

constructor(arg0: $ClientLevel$Type, arg1: $ChunkVertexType$Type)

public "getSchedulingBudget"(): integer
public "getScheduledJobCount"(): integer
public "shutdown"(): void
public "getTotalThreadCount"(): integer
public "isBuildQueueEmpty"(): boolean
public static "getMaxThreadCount"(): integer
public "scheduleTask"<TASK extends $ChunkBuilderTask<(OUTPUT)>, OUTPUT>(arg0: TASK, arg1: boolean, arg2: $Consumer$Type<($ChunkJobResult$Type<(OUTPUT)>)>): $ChunkJobTyped<(TASK), (OUTPUT)>
public "getBusyThreadCount"(): integer
public "tryStealTask"(arg0: $ChunkJob$Type): void
get "schedulingBudget"(): integer
get "scheduledJobCount"(): integer
get "totalThreadCount"(): integer
get "buildQueueEmpty"(): boolean
get "maxThreadCount"(): integer
get "busyThreadCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkBuilder$Type = ($ChunkBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkBuilder_ = $ChunkBuilder$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/$PotentialSpawnsExtended" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PotentialSpawnsExtended {

 "radium$wasListModified"(): boolean

(): boolean
}

export namespace $PotentialSpawnsExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotentialSpawnsExtended$Type = ($PotentialSpawnsExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotentialSpawnsExtended_ = $PotentialSpawnsExtended$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$ListeningList" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $ListeningList<T> implements $List<(T)> {

constructor(delegate: $List$Type<(T)>, changeCallback: $Runnable$Type)

public "add"(t: T): boolean
public "add"(i: integer, t: T): void
public "remove"(i: integer): T
public "remove"(o: any): boolean
public "get"(i: integer): T
public "indexOf"(o: any): integer
public "clear"(): void
public "lastIndexOf"(o: any): integer
public "isEmpty"(): boolean
public "replaceAll"(unaryOperator: $UnaryOperator$Type<(T)>): void
public "size"(): integer
public "subList"(i: integer, i1: integer): $List<(T)>
public "toArray"<T1>(t1s: (T1)[]): (T1)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<(T)>
public "stream"(): $Stream<(T)>
public "contains"(o: any): boolean
public "spliterator"(): $Spliterator<(T)>
public "addAll"(i: integer, collection: $Collection$Type<(any)>): boolean
public "addAll"(collection: $Collection$Type<(any)>): boolean
public "set"(i: integer, t: T): T
public "forEach"(consumer: $Consumer$Type<(any)>): void
public "removeIf"(predicate: $Predicate$Type<(any)>): boolean
public "sort"(comparator: $Comparator$Type<(any)>): void
public "removeAll"(collection: $Collection$Type<(any)>): boolean
public "retainAll"(collection: $Collection$Type<(any)>): boolean
public "listIterator"(): $ListIterator<(T)>
public "listIterator"(i: integer): $ListIterator<(T)>
public "containsAll"(collection: $Collection$Type<(any)>): boolean
public "parallelStream"(): $Stream<(T)>
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(T)>
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
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
[Symbol.iterator](): IterableIterator<T>;
[index: number]: T
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListeningList$Type<T> = ($ListeningList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListeningList_<T> = $ListeningList$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderer" {
import {$ChunkVertexType, $ChunkVertexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType"
import {$CameraTransform, $CameraTransform$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/$CameraTransform"
import {$ChunkRenderListIterable, $ChunkRenderListIterable$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/lists/$ChunkRenderListIterable"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$TerrainRenderPass, $TerrainRenderPass$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/terrain/$TerrainRenderPass"
import {$ChunkRenderMatrices, $ChunkRenderMatrices$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkRenderMatrices"

export interface $ChunkRenderer {

 "delete"(arg0: $CommandList$Type): void
 "render"(arg0: $ChunkRenderMatrices$Type, arg1: $CommandList$Type, arg2: $ChunkRenderListIterable$Type, arg3: $TerrainRenderPass$Type, arg4: $CameraTransform$Type): void
 "getVertexType"(): $ChunkVertexType
}

export namespace $ChunkRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRenderer$Type = ($ChunkRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRenderer_ = $ChunkRenderer$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/collections/$ReadQueue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ReadQueue<E> {

 "dequeue"(): E

(): E
}

export namespace $ReadQueue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReadQueue$Type<E> = ($ReadQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReadQueue_<E> = $ReadQueue$Type<(E)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $LightDataAccess {

constructor()

public static "getEmissiveLightmap"(arg0: integer): integer
public "get"(arg0: $BlockPos$Type): integer
public "get"(arg0: integer, arg1: integer, arg2: integer): integer
public "get"(arg0: integer, arg1: integer, arg2: integer, arg3: $Direction$Type, arg4: $Direction$Type): integer
public "get"(arg0: integer, arg1: integer, arg2: integer, arg3: $Direction$Type): integer
public "get"(arg0: $BlockPos$Type, arg1: $Direction$Type): integer
public "getWorld"(): $BlockAndTintGetter
public static "packOP"(arg0: boolean): integer
public static "packAO"(arg0: float): integer
public static "packSL"(arg0: integer): integer
public static "unpackSL"(arg0: integer): integer
public static "unpackFO"(arg0: integer): boolean
public static "unpackBL"(arg0: integer): integer
public static "unpackFC"(arg0: integer): boolean
public static "getLightmap"(arg0: integer): integer
public static "unpackEM"(arg0: integer): boolean
public static "unpackAO"(arg0: integer): float
public static "packFC"(arg0: boolean): integer
public static "packBL"(arg0: integer): integer
public static "packEM"(arg0: boolean): integer
public static "unpackLU"(arg0: integer): integer
public static "unpackOP"(arg0: integer): boolean
public static "packFO"(arg0: boolean): integer
public static "packLU"(arg0: integer): integer
get "world"(): $BlockAndTintGetter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightDataAccess$Type = ($LightDataAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightDataAccess_ = $LightDataAccess$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/util/$DeferredRenderTask" {
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $DeferredRenderTask {

constructor()

public static "schedule"(arg0: $Runnable$Type): void
public static "runAll"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeferredRenderTask$Type = ($DeferredRenderTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeferredRenderTask_ = $DeferredRenderTask$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/interests/iterator/$SinglePointOfInterestTypeFilter" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"

export class $SinglePointOfInterestTypeFilter extends $Record implements $Predicate<($Holder<($PoiType)>)> {

constructor(type: $Holder$Type<($PoiType$Type)>)

public "type"(): $Holder<($PoiType)>
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(other: $Holder$Type<($PoiType$Type)>): boolean
public "getType"(): $Holder<($PoiType)>
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($Holder<($PoiType)>)>
public "negate"(): $Predicate<($Holder<($PoiType)>)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($Holder<($PoiType)>)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($Holder<($PoiType)>)>
public static "isEqual"<T>(arg0: any): $Predicate<($Holder<($PoiType)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinglePointOfInterestTypeFilter$Type = ($SinglePointOfInterestTypeFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinglePointOfInterestTypeFilter_ = $SinglePointOfInterestTypeFilter$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/$ReadableContainerExtended" {
import {$PalettedContainerRO, $PalettedContainerRO$Type} from "packages/net/minecraft/world/level/chunk/$PalettedContainerRO"

export interface $ReadableContainerExtended<T> {

 "sodium$unpack"(arg0: (T)[]): void
 "sodium$unpack"(arg0: (T)[], arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
 "sodium$copy"(): $PalettedContainerRO<(T)>
}

export namespace $ReadableContainerExtended {
function clone<T>(arg0: $PalettedContainerRO$Type<(T)>): $PalettedContainerRO<(T)>
function of<T>(arg0: $PalettedContainerRO$Type<(T)>): $ReadableContainerExtended<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReadableContainerExtended$Type<T> = ($ReadableContainerExtended<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReadableContainerExtended_<T> = $ReadableContainerExtended$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement/$ChunkAwareBlockCollisionSweeper" {
import {$AbstractIterator, $AbstractIterator$Type} from "packages/com/google/common/collect/$AbstractIterator"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ChunkAwareBlockCollisionSweeper extends $AbstractIterator<($VoxelShape)> {

constructor(world: $Level$Type, entity: $Entity$Type, box: $AABB$Type)

public "collectAll"(): $List<($VoxelShape)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkAwareBlockCollisionSweeper$Type = ($ChunkAwareBlockCollisionSweeper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkAwareBlockCollisionSweeper_ = $ChunkAwareBlockCollisionSweeper$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeEmpty" {
import {$VoxelShapeCaster, $VoxelShapeCaster$Type} from "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeCaster"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $VoxelShapeEmpty extends $VoxelShape implements $VoxelShapeCaster {
 "shape": $DiscreteVoxelShape

constructor(voxels: $DiscreteVoxelShape$Type)

public "intersects"(box: $AABB$Type, blockX: double, blockY: double, blockZ: double): boolean
public "isEmpty"(): boolean
public "max"(axis: $Direction$Axis$Type): double
public "min"(axis: $Direction$Axis$Type): double
public "getCoords"(axis: $Direction$Axis$Type): $DoubleList
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeEmpty$Type = ($VoxelShapeEmpty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeEmpty_ = $VoxelShapeEmpty$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/iterator/$ByteArrayIterator" {
import {$ByteIterator, $ByteIterator$Type} from "packages/me/jellysquid/mods/sodium/client/util/iterator/$ByteIterator"

export class $ByteArrayIterator implements $ByteIterator {

constructor(arg0: (byte)[], arg1: integer)

public "hasNext"(): boolean
public "nextByteAsInt"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ByteArrayIterator$Type = ($ByteArrayIterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ByteArrayIterator_ = $ByteArrayIterator$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/$SodiumMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $SodiumMixinPlugin implements $IMixinConfigPlugin {

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
export type $SodiumMixinPlugin$Type = ($SodiumMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumMixinPlugin_ = $SodiumMixinPlugin$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$RenderSection" {
import {$TranslucentQuadAnalyzer$SortState, $TranslucentQuadAnalyzer$SortState$Type} from "packages/org/embeddedt/embeddium/render/chunk/sorting/$TranslucentQuadAnalyzer$SortState"
import {$ChunkUpdateType, $ChunkUpdateType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$ChunkUpdateType"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$CancellationToken, $CancellationToken$Type} from "packages/me/jellysquid/mods/sodium/client/util/task/$CancellationToken"
import {$RenderRegion, $RenderRegion$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegion"
import {$BuiltSectionInfo, $BuiltSectionInfo$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/data/$BuiltSectionInfo"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $RenderSection {
 "adjacentDown": $RenderSection
 "adjacentUp": $RenderSection
 "adjacentNorth": $RenderSection
 "adjacentSouth": $RenderSection
 "adjacentWest": $RenderSection
 "adjacentEast": $RenderSection
 "lastCameraX": double
 "lastCameraY": double
 "lastCameraZ": double

constructor(arg0: $RenderRegion$Type, arg1: integer, arg2: integer, arg3: integer)

public "isDisposed"(): boolean
public "setLastSubmittedFrame"(arg0: integer): void
public "getBuildCancellationToken"(): $CancellationToken
public "getLastVisibleFrame"(): integer
public "setBuildCancellationToken"(arg0: $CancellationToken$Type): void
public "containsTranslucentGeometry"(): boolean
public "isAlignedWithSectionOnGrid"(arg0: integer, arg1: integer, arg2: integer): boolean
public "getLastSubmittedFrame"(): integer
public "toString"(): string
public "delete"(): void
public "getRegion"(): $RenderRegion
public "getFlags"(): integer
public "getPosition"(): $SectionPos
public "setInfo"(arg0: $BuiltSectionInfo$Type): void
public "getChunkX"(): integer
public "getChunkZ"(): integer
public "getChunkY"(): integer
public "getOriginX"(): integer
public "getOriginY"(): integer
public "getCenterX"(): integer
public "getVisibilityData"(): long
public "getAdjacentMask"(): integer
public "getCenterZ"(): integer
public "getCulledBlockEntities"(): ($BlockEntity)[]
public "getGlobalBlockEntities"(): ($BlockEntity)[]
public "getAdjacent"(arg0: integer): $RenderSection
public "getPendingUpdate"(): $ChunkUpdateType
public "setPendingUpdate"(arg0: $ChunkUpdateType$Type): void
public "getSortState"(): $TranslucentQuadAnalyzer$SortState
public "getAnimatedSprites"(): ($TextureAtlasSprite)[]
public "getLastBuiltFrame"(): integer
public "setSortState"(arg0: $TranslucentQuadAnalyzer$SortState$Type): void
public "setLastBuiltFrame"(arg0: integer): void
public "getSquaredDistance"(arg0: $BlockPos$Type): float
public "getSquaredDistance"(arg0: float, arg1: float, arg2: float): float
public "setAdjacentNode"(arg0: integer, arg1: $RenderSection$Type): void
public "getOriginZ"(): integer
public "getSectionIndex"(): integer
public "getCenterY"(): integer
public "getIncomingDirections"(): integer
public "setLastVisibleFrame"(arg0: integer): void
public "addIncomingDirections"(arg0: integer): void
public "setIncomingDirections"(arg0: integer): void
public "isBuilt"(): boolean
get "disposed"(): boolean
set "lastSubmittedFrame"(value: integer)
get "buildCancellationToken"(): $CancellationToken
get "lastVisibleFrame"(): integer
set "buildCancellationToken"(value: $CancellationToken$Type)
get "lastSubmittedFrame"(): integer
get "region"(): $RenderRegion
get "flags"(): integer
get "position"(): $SectionPos
set "info"(value: $BuiltSectionInfo$Type)
get "chunkX"(): integer
get "chunkZ"(): integer
get "chunkY"(): integer
get "originX"(): integer
get "originY"(): integer
get "centerX"(): integer
get "visibilityData"(): long
get "adjacentMask"(): integer
get "centerZ"(): integer
get "culledBlockEntities"(): ($BlockEntity)[]
get "globalBlockEntities"(): ($BlockEntity)[]
get "pendingUpdate"(): $ChunkUpdateType
set "pendingUpdate"(value: $ChunkUpdateType$Type)
get "sortState"(): $TranslucentQuadAnalyzer$SortState
get "animatedSprites"(): ($TextureAtlasSprite)[]
get "lastBuiltFrame"(): integer
set "sortState"(value: $TranslucentQuadAnalyzer$SortState$Type)
set "lastBuiltFrame"(value: integer)
get "originZ"(): integer
get "sectionIndex"(): integer
get "centerY"(): integer
get "incomingDirections"(): integer
set "lastVisibleFrame"(value: integer)
set "incomingDirections"(value: integer)
get "built"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderSection$Type = ($RenderSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderSection_ = $RenderSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/animations/tracking/$SpriteContentsAnimationAccessor" {
import {$SpriteContents$FrameInfo, $SpriteContents$FrameInfo$Type} from "packages/net/minecraft/client/renderer/texture/$SpriteContents$FrameInfo"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $SpriteContentsAnimationAccessor {

 "getFrames"(): $List<($SpriteContents$FrameInfo)>

(): $List<($SpriteContents$FrameInfo)>
}

export namespace $SpriteContentsAnimationAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsAnimationAccessor$Type = ($SpriteContentsAnimationAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsAnimationAccessor_ = $SpriteContentsAnimationAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumGameOptions$AdvancedSettings" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SodiumGameOptions$AdvancedSettings {
 "enableMemoryTracing": boolean
 "useAdvancedStagingBuffers": boolean
 "disableIncompatibleModWarnings": boolean
 "cpuRenderAheadLimit": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumGameOptions$AdvancedSettings$Type = ($SodiumGameOptions$AdvancedSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumGameOptions$AdvancedSettings_ = $SodiumGameOptions$AdvancedSettings$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/interests/$PointOfInterestStorageExtended" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$PoiRecord, $PoiRecord$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiRecord"
import {$WorldBorder, $WorldBorder$Type} from "packages/net/minecraft/world/level/border/$WorldBorder"
import {$PoiType, $PoiType$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiType"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PoiManager$Occupancy, $PoiManager$Occupancy$Type} from "packages/net/minecraft/world/entity/ai/village/poi/$PoiManager$Occupancy"

export interface $PointOfInterestStorageExtended {

 "findNearestForPortalLogic"(arg0: $BlockPos$Type, arg1: integer, arg2: $Holder$Type<($PoiType$Type)>, arg3: $PoiManager$Occupancy$Type, arg4: $Predicate$Type<($PoiRecord$Type)>, arg5: $WorldBorder$Type): $Optional<($PoiRecord)>

(arg0: $BlockPos$Type, arg1: integer, arg2: $Holder$Type<($PoiType$Type)>, arg3: $PoiManager$Occupancy$Type, arg4: $Predicate$Type<($PoiRecord$Type)>, arg5: $WorldBorder$Type): $Optional<($PoiRecord)>
}

export namespace $PointOfInterestStorageExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PointOfInterestStorageExtended$Type = ($PointOfInterestStorageExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PointOfInterestStorageExtended_ = $PointOfInterestStorageExtended$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/sorting/$MergeSort" {
import {$AbstractSort, $AbstractSort$Type} from "packages/me/jellysquid/mods/sodium/client/util/sorting/$AbstractSort"

export class $MergeSort extends $AbstractSort {

constructor()

public static "mergeSort"(arg0: (float)[]): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MergeSort$Type = ($MergeSort);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MergeSort_ = $MergeSort$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/animations/tracking/$SpriteContentsAnimationFrameAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SpriteContentsAnimationFrameAccessor {

 "getTime"(): integer

(): integer
}

export namespace $SpriteContentsAnimationFrameAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsAnimationFrameAccessor$Type = ($SpriteContentsAnimationFrameAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsAnimationFrameAccessor_ = $SpriteContentsAnimationFrameAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexType" {
import {$ChunkMeshAttribute, $ChunkMeshAttribute$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkMeshAttribute"
import {$ChunkVertexEncoder, $ChunkVertexEncoder$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/vertex/format/$ChunkVertexEncoder"
import {$GlVertexFormat, $GlVertexFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat"

export interface $ChunkVertexType {

 "getEncoder"(): $ChunkVertexEncoder
 "getPositionOffset"(): float
 "getPositionScale"(): float
 "getTextureScale"(): float
 "getVertexFormat"(): $GlVertexFormat<($ChunkMeshAttribute)>
}

export namespace $ChunkVertexType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkVertexType$Type = ($ChunkVertexType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkVertexType_ = $ChunkVertexType$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/region/$RenderRegion$DeviceResources" {
import {$GlBufferArena, $GlBufferArena$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/$GlBufferArena"
import {$StagingBuffer, $StagingBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/arena/staging/$StagingBuffer"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$GlTessellation, $GlTessellation$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlTessellation"

export class $RenderRegion$DeviceResources {

constructor(arg0: $CommandList$Type, arg1: $StagingBuffer$Type)

public "getIndexedTessellation"(): $GlTessellation
public "updateIndexedTessellation"(arg0: $CommandList$Type, arg1: $GlTessellation$Type): void
public "deleteTessellations"(arg0: $CommandList$Type): void
public "shouldDelete"(): boolean
public "delete"(arg0: $CommandList$Type): void
public "getIndexArena"(): $GlBufferArena
public "getGeometryArena"(): $GlBufferArena
public "getVertexBuffer"(): $GlBuffer
public "getTessellation"(): $GlTessellation
public "getIndexBuffer"(): $GlBuffer
public "updateTessellation"(arg0: $CommandList$Type, arg1: $GlTessellation$Type): void
get "indexedTessellation"(): $GlTessellation
get "indexArena"(): $GlBufferArena
get "geometryArena"(): $GlBufferArena
get "vertexBuffer"(): $GlBuffer
get "tessellation"(): $GlTessellation
get "indexBuffer"(): $GlBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderRegion$DeviceResources$Type = ($RenderRegion$DeviceResources);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderRegion$DeviceResources_ = $RenderRegion$DeviceResources$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeAlignedCuboidOffset" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$DiscreteVoxelShape, $DiscreteVoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$DiscreteVoxelShape"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"
import {$AxisCycle, $AxisCycle$Type} from "packages/net/minecraft/core/$AxisCycle"
import {$VoxelShapeAlignedCuboid, $VoxelShapeAlignedCuboid$Type} from "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeAlignedCuboid"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $VoxelShapeAlignedCuboidOffset extends $VoxelShapeAlignedCuboid {
readonly "isTiny": boolean
 "shape": $DiscreteVoxelShape

constructor(originalShape: $VoxelShapeAlignedCuboid$Type, voxels: $DiscreteVoxelShape$Type, xOffset: double, yOffset: double, zOffset: double)

public "getCoords"(axis: $Direction$Axis$Type): $DoubleList
public "collideX"(cycleDirection: $AxisCycle$Type, box: $AABB$Type, maxDist: double): double
public "move"(x: double, y: double, z: double): $VoxelShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeAlignedCuboidOffset$Type = ($VoxelShapeAlignedCuboidOffset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeAlignedCuboidOffset_ = $VoxelShapeAlignedCuboidOffset$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$ChunkSectionChangeCallback" {
import {$BlockListeningSection, $BlockListeningSection$Type} from "packages/me/jellysquid/mods/lithium/common/block/$BlockListeningSection"
import {$ListeningBlockStatePredicate, $ListeningBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$ListeningBlockStatePredicate"
import {$SectionedBlockChangeTracker, $SectionedBlockChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$SectionedBlockChangeTracker"

export class $ChunkSectionChangeCallback {

constructor()

public "addTracker"(tracker: $SectionedBlockChangeTracker$Type, blockGroup: $ListeningBlockStatePredicate$Type): short
public "onBlockChange"(flagIndex: integer, section: $BlockListeningSection$Type): short
public "removeTracker"(tracker: $SectionedBlockChangeTracker$Type, blockGroup: $ListeningBlockStatePredicate$Type): short
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkSectionChangeCallback$Type = ($ChunkSectionChangeCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkSectionChangeCallback_ = $ChunkSectionChangeCallback$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/buffer/$IndexedVertexData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$NativeBuffer, $NativeBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/util/$NativeBuffer"
import {$GlVertexFormat, $GlVertexFormat$Type} from "packages/me/jellysquid/mods/sodium/client/gl/attribute/$GlVertexFormat"

export class $IndexedVertexData extends $Record {

constructor(vertexFormat: $GlVertexFormat$Type<(any)>, vertexBuffer: $NativeBuffer$Type, indexBuffer: $NativeBuffer$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "delete"(): void
public "vertexFormat"(): $GlVertexFormat<(any)>
public "indexBuffer"(): $NativeBuffer
public "vertexBuffer"(): $NativeBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IndexedVertexData$Type = ($IndexedVertexData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IndexedVertexData_ = $IndexedVertexData$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/block_tracking/$BlockCache" {
import {$Object2DoubleMap, $Object2DoubleMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2DoubleMap"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$FluidType, $FluidType$Type} from "packages/net/minecraftforge/fluids/$FluidType"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $BlockCache {

constructor()

public "remove"(): void
public "isTracking"(): boolean
public "updateCache"(entity: $Entity$Type): void
public "getIsSuffocating"(): byte
public "initTracking"(entity: $Entity$Type): void
public "resetCachedInfo"(): void
public "resetTrackedPos"(boundingBox: $AABB$Type): void
public "getIsTouchingFireLava"(): byte
public "setCachedTouchingFireLava"(b: boolean): void
public "setCanSkipSupportingBlockSearch"(canSkip: boolean): void
public "setCanSkipBlockTouching"(value: boolean): void
public "getStationaryFluidHeightOrDefault"(fluid: $FluidType$Type, defaultValue: double): double
public "setCachedFluidHeight"(fluid: $FluidType$Type, fluidHeight: double): void
public "canSkipSupportingBlockSearch"(): boolean
public "getCachedFluidHeightMap"(): $Object2DoubleMap<($FluidType)>
public "setCachedIsSuffocating"(b: boolean): void
public "canSkipBlockTouching"(): boolean
get "tracking"(): boolean
get "isSuffocating"(): byte
get "isTouchingFireLava"(): byte
set "cachedTouchingFireLava"(value: boolean)
get "cachedFluidHeightMap"(): $Object2DoubleMap<($FluidType)>
set "cachedIsSuffocating"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCache$Type = ($BlockCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCache_ = $BlockCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList" {
import {$DrawCommandList, $DrawCommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$DrawCommandList"
import {$TessellationBinding, $TessellationBinding$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$TessellationBinding"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GlBufferStorageFlags, $GlBufferStorageFlags$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferStorageFlags"
import {$GlVertexArray, $GlVertexArray$Type} from "packages/me/jellysquid/mods/sodium/client/gl/array/$GlVertexArray"
import {$GlPrimitiveType, $GlPrimitiveType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlPrimitiveType"
import {$GlMutableBuffer, $GlMutableBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlMutableBuffer"
import {$EnumBitField, $EnumBitField$Type} from "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBitField"
import {$GlBufferMapFlags, $GlBufferMapFlags$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferMapFlags"
import {$GlBufferTarget, $GlBufferTarget$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferTarget"
import {$GlImmutableBuffer, $GlImmutableBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlImmutableBuffer"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$GlFence, $GlFence$Type} from "packages/me/jellysquid/mods/sodium/client/gl/sync/$GlFence"
import {$GlTessellation, $GlTessellation$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlTessellation"
import {$GlBufferMapping, $GlBufferMapping$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferMapping"
import {$GlBufferUsage, $GlBufferUsage$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBufferUsage"

export interface $CommandList extends $AutoCloseable {

 "allocateStorage"(arg0: $GlMutableBuffer$Type, arg1: long, arg2: $GlBufferUsage$Type): void
 "unbindVertexArray"(): void
 "beginTessellating"(arg0: $GlTessellation$Type): $DrawCommandList
 "copyBufferSubData"(arg0: $GlBuffer$Type, arg1: $GlBuffer$Type, arg2: long, arg3: long, arg4: long): void
 "deleteVertexArray"(arg0: $GlVertexArray$Type): void
 "createTessellation"(arg0: $GlPrimitiveType$Type, arg1: ($TessellationBinding$Type)[]): $GlTessellation
 "uploadData"(arg0: $GlMutableBuffer$Type, arg1: $ByteBuffer$Type, arg2: $GlBufferUsage$Type): void
 "deleteBuffer"(arg0: $GlBuffer$Type): void
 "mapBuffer"(arg0: $GlBuffer$Type, arg1: long, arg2: long, arg3: $EnumBitField$Type<($GlBufferMapFlags$Type)>): $GlBufferMapping
 "flushMappedRange"(arg0: $GlBufferMapping$Type, arg1: integer, arg2: integer): void
 "deleteTessellation"(arg0: $GlTessellation$Type): void
 "createFence"(): $GlFence
 "flush"(): void
 "close"(): void
 "unmap"(arg0: $GlBufferMapping$Type): void
 "bindBuffer"(arg0: $GlBufferTarget$Type, arg1: $GlBuffer$Type): void
 "bindVertexArray"(arg0: $GlVertexArray$Type): void
 "createImmutableBuffer"(arg0: long, arg1: $EnumBitField$Type<($GlBufferStorageFlags$Type)>): $GlImmutableBuffer
 "createMutableBuffer"(): $GlMutableBuffer
}

export namespace $CommandList {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandList$Type = ($CommandList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandList_ = $CommandList$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/collections/$ChunkTicketSortedArraySet" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SortedArraySet, $SortedArraySet$Type} from "packages/net/minecraft/util/$SortedArraySet"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Ticket, $Ticket$Type} from "packages/net/minecraft/server/level/$Ticket"

export class $ChunkTicketSortedArraySet<T> extends $SortedArraySet<($Ticket<(any)>)> {

constructor(initialCapacity: integer)

public "remove"(object: any): boolean
public "addExpireTime"(time: long): void
public "getMinExpireTime"(): long
public "recalculateExpireTime"(): void
public "invalidateExpireTime"(): void
public "hashCode"(): integer
public "isEmpty"(): boolean
public "spliterator"(): $Spliterator<(E)>
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
get "minExpireTime"(): long
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkTicketSortedArraySet$Type<T> = ($ChunkTicketSortedArraySet<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkTicketSortedArraySet_<T> = $ChunkTicketSortedArraySet$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/console/$Console" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MessageLevel, $MessageLevel$Type} from "packages/me/jellysquid/mods/sodium/client/gui/console/message/$MessageLevel"
import {$Message, $Message$Type} from "packages/me/jellysquid/mods/sodium/client/gui/console/message/$Message"
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$ConsoleSink, $ConsoleSink$Type} from "packages/me/jellysquid/mods/sodium/client/gui/console/$ConsoleSink"

export class $Console implements $ConsoleSink {

constructor()

public static "instance"(): $ConsoleSink
public "getMessageDrain"(): $Deque<($Message)>
public "logMessage"(arg0: $MessageLevel$Type, arg1: $Component$Type, arg2: double): void
get "messageDrain"(): $Deque<($Message)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Console$Type = ($Console);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Console_ = $Console$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/color/$FastCubicSampler" {
import {$FastCubicSampler$ColorFetcher, $FastCubicSampler$ColorFetcher$Type} from "packages/me/jellysquid/mods/sodium/client/util/color/$FastCubicSampler$ColorFetcher"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $FastCubicSampler {

constructor()

public static "sampleColor"(arg0: $Vec3$Type, arg1: $FastCubicSampler$ColorFetcher$Type, arg2: $Function$Type<($Vec3$Type), ($Vec3$Type)>): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastCubicSampler$Type = ($FastCubicSampler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastCubicSampler_ = $FastCubicSampler$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/util/entity_movement_tracking/$ServerWorldAccessor" {
import {$PersistentEntitySectionManager, $PersistentEntitySectionManager$Type} from "packages/net/minecraft/world/level/entity/$PersistentEntitySectionManager"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ServerWorldAccessor {

 "getEntityManager"(): $PersistentEntitySectionManager<($Entity)>

(): $PersistentEntitySectionManager<($Entity)>
}

export namespace $ServerWorldAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerWorldAccessor$Type = ($ServerWorldAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerWorldAccessor_ = $ServerWorldAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/chunk/entity_class_groups/$ClientEntityManagerAccessor" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$EntityAccess, $EntityAccess$Type} from "packages/net/minecraft/world/level/entity/$EntityAccess"

export interface $ClientEntityManagerAccessor<T extends $EntityAccess> {

 "getCache"(): $EntitySectionStorage<(T)>

(): $EntitySectionStorage<(T)>
}

export namespace $ClientEntityManagerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEntityManagerAccessor$Type<T> = ($ClientEntityManagerAccessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEntityManagerAccessor_<T> = $ClientEntityManagerAccessor$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/$BlockCountingSection" {
import {$TrackedBlockStatePredicate, $TrackedBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$TrackedBlockStatePredicate"

export interface $BlockCountingSection {

 "mayContainAny"(arg0: $TrackedBlockStatePredicate$Type): boolean

(arg0: $TrackedBlockStatePredicate$Type): boolean
}

export namespace $BlockCountingSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockCountingSection$Type = ($BlockCountingSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockCountingSection_ = $BlockCountingSection$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/mixin/features/textures/animations/upload/$SpriteContentsAccessor" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export interface $SpriteContentsAccessor {

 "getImages"(): ($NativeImage)[]

(): ($NativeImage)[]
}

export namespace $SpriteContentsAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpriteContentsAccessor$Type = ($SpriteContentsAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpriteContentsAccessor_ = $SpriteContentsAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/widgets/$FlatButtonWidget" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$AbstractWidget"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$FlatButtonWidget$Style, $FlatButtonWidget$Style$Type} from "packages/me/jellysquid/mods/sodium/client/gui/widgets/$FlatButtonWidget$Style"

export class $FlatButtonWidget extends $AbstractWidget implements $Renderable {

constructor(arg0: $Dim2i$Type, arg1: $Component$Type, arg2: $Runnable$Type)

public "getDimensions"(): $Dim2i
public "setLeftAligned"(arg0: boolean): void
public "setEnabled"(arg0: boolean): void
public "setVisible"(arg0: boolean): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "getRectangle"(): $ScreenRectangle
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setLabel"(arg0: $Component$Type): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "setSelected"(arg0: boolean): void
public "setStyle"(arg0: $FlatButtonWidget$Style$Type): void
public "getLabel"(): $Component
get "dimensions"(): $Dim2i
set "leftAligned"(value: boolean)
set "enabled"(value: boolean)
set "visible"(value: boolean)
get "rectangle"(): $ScreenRectangle
set "label"(value: $Component$Type)
set "selected"(value: boolean)
set "style"(value: $FlatButtonWidget$Style$Type)
get "label"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlatButtonWidget$Type = ($FlatButtonWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlatButtonWidget_ = $FlatButtonWidget$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/math/$CompactSineLUT" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CompactSineLUT {

constructor()

public static "sin"(f: float): float
public static "cos"(f: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactSineLUT$Type = ($CompactSineLUT);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactSineLUT_ = $CompactSineLUT$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/hopper/$BlockStateOnlyInventory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BlockStateOnlyInventory {

}

export namespace $BlockStateOnlyInventory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateOnlyInventory$Type = ($BlockStateOnlyInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateOnlyInventory_ = $BlockStateOnlyInventory$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/$GlObject" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GlObject {


public "handle"(): integer
public "invalidateHandle"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlObject$Type = ($GlObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlObject_ = $GlObject$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/chunk/entity_class_groups/$ServerWorldAccessor" {
import {$PersistentEntitySectionManager, $PersistentEntitySectionManager$Type} from "packages/net/minecraft/world/level/entity/$PersistentEntitySectionManager"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ServerWorldAccessor {

 "getEntityManager"(): $PersistentEntitySectionManager<($Entity)>

(): $PersistentEntitySectionManager<($Entity)>
}

export namespace $ServerWorldAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerWorldAccessor$Type = ($ServerWorldAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerWorldAccessor_ = $ServerWorldAccessor$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/light/flat/$FlatLightPipeline" {
import {$LightPipeline, $LightPipeline$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/$LightPipeline"
import {$ModelQuadView, $ModelQuadView$Type} from "packages/me/jellysquid/mods/sodium/client/model/quad/$ModelQuadView"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$QuadLightData, $QuadLightData$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$QuadLightData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LightDataAccess, $LightDataAccess$Type} from "packages/me/jellysquid/mods/sodium/client/model/light/data/$LightDataAccess"

export class $FlatLightPipeline implements $LightPipeline {

constructor(arg0: $LightDataAccess$Type)

public "calculate"(arg0: $ModelQuadView$Type, arg1: $BlockPos$Type, arg2: $QuadLightData$Type, arg3: $Direction$Type, arg4: $Direction$Type, arg5: boolean): void
public "reset"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlatLightPipeline$Type = ($FlatLightPipeline);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlatLightPipeline_ = $FlatLightPipeline$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/$LithiumEntityCollisions" {
import {$EntityGetter, $EntityGetter$Type} from "packages/net/minecraft/world/level/$EntityGetter"
import {$WorldBorder, $WorldBorder$Type} from "packages/net/minecraft/world/level/border/$WorldBorder"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$CollisionGetter, $CollisionGetter$Type} from "packages/net/minecraft/world/level/$CollisionGetter"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LithiumEntityCollisions {
static readonly "EPSILON": double

constructor()

public static "getEntityWorldBorderCollisionIterable"(view: $EntityGetter$Type, entity: $Entity$Type, box: $AABB$Type, includeWorldBorder: boolean): $Iterable<($VoxelShape)>
public static "getCollisionShapeBelowEntity"(world: $Level$Type, entity: $Entity$Type, entityBoundingBox: $AABB$Type): $VoxelShape
public static "getEntityWorldBorderCollisions"(world: $Level$Type, entity: $Entity$Type, box: $AABB$Type, includeWorldBorder: boolean): $List<($VoxelShape)>
public static "doesEntityCollideWithWorldBorder"(collisionView: $CollisionGetter$Type, entity: $Entity$Type): boolean
public static "doesBoxCollideWithHardEntities"(view: $EntityGetter$Type, entity: $Entity$Type, box: $AABB$Type): boolean
public static "doesBoxCollideWithBlocks"(world: $Level$Type, entity: $Entity$Type, box: $AABB$Type): boolean
public static "getWorldBorderCollision"(collisionView: $CollisionGetter$Type, entity: $Entity$Type): $VoxelShape
public static "isWithinWorldBorder"(border: $WorldBorder$Type, box: $AABB$Type): boolean
public static "getBlockCollisions"(world: $Level$Type, entity: $Entity$Type, box: $AABB$Type): $List<($VoxelShape)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumEntityCollisions$Type = ($LithiumEntityCollisions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumEntityCollisions_ = $LithiumEntityCollisions$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/map/$ClientChunkEventListener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ClientChunkEventListener {

 "updateMapCenter"(arg0: integer, arg1: integer): void
 "updateLoadDistance"(arg0: integer): void
 "onChunkStatusRemoved"(arg0: integer, arg1: integer, arg2: integer): void
 "onChunkStatusAdded"(arg0: integer, arg1: integer, arg2: integer): void
}

export namespace $ClientChunkEventListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChunkEventListener$Type = ($ClientChunkEventListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChunkEventListener_ = $ClientChunkEventListener$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/pairs/$LithiumDoublePairList" {
import {$IndexMerger$IndexConsumer, $IndexMerger$IndexConsumer$Type} from "packages/net/minecraft/world/phys/shapes/$IndexMerger$IndexConsumer"
import {$IndexMerger, $IndexMerger$Type} from "packages/net/minecraft/world/phys/shapes/$IndexMerger"
import {$DoubleList, $DoubleList$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleList"

export class $LithiumDoublePairList implements $IndexMerger {

constructor(aPoints: $DoubleList$Type, bPoints: $DoubleList$Type, flag1: boolean, flag2: boolean)

public "size"(): integer
public "getList"(): $DoubleList
public "forMergedIndexes"(predicate: $IndexMerger$IndexConsumer$Type): boolean
get "list"(): $DoubleList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LithiumDoublePairList$Type = ($LithiumDoublePairList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LithiumDoublePairList_ = $LithiumDoublePairList$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/collections/$DoubleBufferedQueue" {
import {$WriteQueue, $WriteQueue$Type} from "packages/me/jellysquid/mods/sodium/client/util/collections/$WriteQueue"
import {$ReadQueue, $ReadQueue$Type} from "packages/me/jellysquid/mods/sodium/client/util/collections/$ReadQueue"

export class $DoubleBufferedQueue<E> {

constructor()

public "write"(): $WriteQueue<(E)>
public "read"(): $ReadQueue<(E)>
public "reset"(): void
public "flip"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleBufferedQueue$Type<E> = ($DoubleBufferedQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleBufferedQueue_<E> = $DoubleBufferedQueue$Type<(E)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/$SharedQuadIndexBuffer" {
import {$GlIndexType, $GlIndexType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlIndexType"
import {$SharedQuadIndexBuffer$IndexType, $SharedQuadIndexBuffer$IndexType$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/$SharedQuadIndexBuffer$IndexType"
import {$GlBuffer, $GlBuffer$Type} from "packages/me/jellysquid/mods/sodium/client/gl/buffer/$GlBuffer"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"

export class $SharedQuadIndexBuffer {

constructor(arg0: $CommandList$Type, arg1: $SharedQuadIndexBuffer$IndexType$Type)

public "getBufferObject"(): $GlBuffer
public "delete"(arg0: $CommandList$Type): void
public "ensureCapacity"(arg0: $CommandList$Type, arg1: integer): void
public "getIndexType"(): $SharedQuadIndexBuffer$IndexType
public "getIndexFormat"(): $GlIndexType
get "bufferObject"(): $GlBuffer
get "indexType"(): $SharedQuadIndexBuffer$IndexType
get "indexFormat"(): $GlIndexType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharedQuadIndexBuffer$Type = ($SharedQuadIndexBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharedQuadIndexBuffer_ = $SharedQuadIndexBuffer$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/blockentity/$BlockEntityGetter" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BlockEntityGetter {

 "getLoadedExistingBlockEntity"(arg0: $BlockPos$Type): $BlockEntity

(arg0: $BlockPos$Type): $BlockEntity
}

export namespace $BlockEntityGetter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityGetter$Type = ($BlockEntityGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityGetter_ = $BlockEntityGetter$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/util/$ArrayConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ArrayConstants {
static readonly "EMPTY": (integer)[]
static readonly "ZERO": (integer)[]


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayConstants$Type = ($ArrayConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayConstants_ = $ArrayConstants$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderCache" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$FluidRenderer, $FluidRenderer$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$FluidRenderer"
import {$ChunkRenderContext, $ChunkRenderContext$Type} from "packages/me/jellysquid/mods/sodium/client/world/cloned/$ChunkRenderContext"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$WorldSlice, $WorldSlice$Type} from "packages/me/jellysquid/mods/sodium/client/world/$WorldSlice"
import {$BlockModelShaper, $BlockModelShaper$Type} from "packages/net/minecraft/client/renderer/block/$BlockModelShaper"
import {$BlockRenderer, $BlockRenderer$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/$BlockRenderer"

export class $BlockRenderCache {

constructor(arg0: $Minecraft$Type, arg1: $ClientLevel$Type)

public "init"(arg0: $ChunkRenderContext$Type): void
public "cleanup"(): void
public "getWorldSlice"(): $WorldSlice
public "getBlockRenderer"(): $BlockRenderer
public "getFluidRenderer"(): $FluidRenderer
public "getBlockModels"(): $BlockModelShaper
get "worldSlice"(): $WorldSlice
get "blockRenderer"(): $BlockRenderer
get "fluidRenderer"(): $FluidRenderer
get "blockModels"(): $BlockModelShaper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRenderCache$Type = ($BlockRenderCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRenderCache_ = $BlockRenderCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $OptionStorage<T> {

 "save"(): void
 "getData"(): T
}

export namespace $OptionStorage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionStorage$Type<T> = ($OptionStorage<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionStorage_<T> = $OptionStorage$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/sorting/$VertexSorters" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$VertexSorting, $VertexSorting$Type} from "packages/com/mojang/blaze3d/vertex/$VertexSorting"

export class $VertexSorters {

constructor()

public static "sortByDistance"(arg0: $Vector3f$Type): $VertexSorting
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexSorters$Type = ($VertexSorters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexSorters_ = $VertexSorters$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/cloned/$ChunkRenderContext" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$ClonedChunkSection, $ClonedChunkSection$Type} from "packages/me/jellysquid/mods/sodium/client/world/cloned/$ClonedChunkSection"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MeshAppender, $MeshAppender$Type} from "packages/org/embeddedt/embeddium/api/$MeshAppender"

export class $ChunkRenderContext {

constructor(arg0: $SectionPos$Type, arg1: ($ClonedChunkSection$Type)[], arg2: $BoundingBox$Type)

public "getSections"(): ($ClonedChunkSection)[]
public "getVolume"(): $BoundingBox
public "withMeshAppenders"(arg0: $List$Type<($MeshAppender$Type)>): $ChunkRenderContext
public "getOrigin"(): $SectionPos
public "getMeshAppenders"(): $List<($MeshAppender)>
get "sections"(): ($ClonedChunkSection)[]
get "volume"(): $BoundingBox
get "origin"(): $SectionPos
get "meshAppenders"(): $List<($MeshAppender)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkRenderContext$Type = ($ChunkRenderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkRenderContext_ = $ChunkRenderContext$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/mixin/chunk/entity_class_groups/$EntityTrackingSectionAccessor" {
import {$ClassInstanceMultiMap, $ClassInstanceMultiMap$Type} from "packages/net/minecraft/util/$ClassInstanceMultiMap"

export interface $EntityTrackingSectionAccessor<T> {

 "getCollection"(): $ClassInstanceMultiMap<(T)>

(): $ClassInstanceMultiMap<(T)>
}

export namespace $EntityTrackingSectionAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTrackingSectionAccessor$Type<T> = ($EntityTrackingSectionAccessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTrackingSectionAccessor_<T> = $EntityTrackingSectionAccessor$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobResult" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"

export class $ChunkJobResult<OUTPUT> {


public "unwrap"(): OUTPUT
public static "exceptionally"<OUTPUT>(arg0: $Throwable$Type): $ChunkJobResult<(OUTPUT)>
public static "successfully"<OUTPUT>(arg0: OUTPUT): $ChunkJobResult<(OUTPUT)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkJobResult$Type<OUTPUT> = ($ChunkJobResult<(OUTPUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkJobResult_<OUTPUT> = $ChunkJobResult$Type<(OUTPUT)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/model/quad/properties/$ModelQuadFacing" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $ModelQuadFacing extends $Enum<($ModelQuadFacing)> {
static readonly "POS_X": $ModelQuadFacing
static readonly "POS_Y": $ModelQuadFacing
static readonly "POS_Z": $ModelQuadFacing
static readonly "NEG_X": $ModelQuadFacing
static readonly "NEG_Y": $ModelQuadFacing
static readonly "NEG_Z": $ModelQuadFacing
static readonly "UNASSIGNED": $ModelQuadFacing
static readonly "VALUES": ($ModelQuadFacing)[]
static readonly "COUNT": integer
static readonly "NONE": integer
static readonly "ALL": integer


public static "values"(): ($ModelQuadFacing)[]
public static "valueOf"(arg0: string): $ModelQuadFacing
public static "fromDirection"(arg0: $Direction$Type): $ModelQuadFacing
public "getOpposite"(): $ModelQuadFacing
get "opposite"(): $ModelQuadFacing
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelQuadFacing$Type = (("pos_y") | ("pos_z") | ("pos_x") | ("neg_x") | ("neg_z") | ("neg_y") | ("unassigned")) | ($ModelQuadFacing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelQuadFacing_ = $ModelQuadFacing$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker" {
import {$InventoryChangeEmitter, $InventoryChangeEmitter$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeEmitter"
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$InventoryChangeListener, $InventoryChangeListener$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener"

export interface $InventoryChangeTracker extends $InventoryChangeEmitter {

 "listenForMajorInventoryChanges"(inventoryChangeListener: $InventoryChangeListener$Type): void
 "stopListenForMajorInventoryChanges"(inventoryChangeListener: $InventoryChangeListener$Type): void
 "listenForContentChangesOnce"(stackList: $LithiumStackList$Type, inventoryChangeListener: $InventoryChangeListener$Type): void
 "stopForwardingMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
 "emitFirstComparatorAdded"(): void
 "forwardMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
 "emitContentModified"(): void
 "emitCallbackReplaced"(): void
 "emitStackListReplaced"(): void
 "forwardContentChangeOnce"(arg0: $InventoryChangeListener$Type, arg1: $LithiumStackList$Type, arg2: $InventoryChangeTracker$Type): void
 "emitRemoved"(): void
}

export namespace $InventoryChangeTracker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryChangeTracker$Type = ($InventoryChangeTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryChangeTracker_ = $InventoryChangeTracker$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJobTyped" {
import {$ChunkBuildContext, $ChunkBuildContext$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/$ChunkBuildContext"
import {$ChunkJob, $ChunkJob$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/executor/$ChunkJob"
import {$ChunkBuilderTask, $ChunkBuilderTask$Type} from "packages/me/jellysquid/mods/sodium/client/render/chunk/compile/tasks/$ChunkBuilderTask"

export class $ChunkJobTyped<TASK extends $ChunkBuilderTask<(OUTPUT)>, OUTPUT> implements $ChunkJob {


public "execute"(arg0: $ChunkBuildContext$Type): void
public "isCancelled"(): boolean
public "isStarted"(): boolean
public "setCancelled"(): void
get "cancelled"(): boolean
get "started"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkJobTyped$Type<TASK, OUTPUT> = ($ChunkJobTyped<(TASK), (OUTPUT)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkJobTyped_<TASK, OUTPUT> = $ChunkJobTyped$Type<(TASK), (OUTPUT)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/chunk/$CompactingPackedIntegerArray" {
import {$Palette, $Palette$Type} from "packages/net/minecraft/world/level/chunk/$Palette"

export interface $CompactingPackedIntegerArray {

 "compact"<T>(arg0: $Palette$Type<(T)>, arg1: $Palette$Type<(T)>, arg2: (short)[]): void

(arg0: $Palette$Type<(T)>, arg1: $Palette$Type<(T)>, arg2: (short)[]): void
}

export namespace $CompactingPackedIntegerArray {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactingPackedIntegerArray$Type = ($CompactingPackedIntegerArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactingPackedIntegerArray_ = $CompactingPackedIntegerArray$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/interests/$RegionBasedStorageSectionExtended" {
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $RegionBasedStorageSectionExtended<R> {

 "getWithinChunkColumn"(arg0: integer, arg1: integer): $Stream<(R)>
 "getInChunkColumn"(arg0: integer, arg1: integer): $Iterable<(R)>
}

export namespace $RegionBasedStorageSectionExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionBasedStorageSectionExtended$Type<R> = ($RegionBasedStorageSectionExtended<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionBasedStorageSectionExtended_<R> = $RegionBasedStorageSectionExtended$Type<(R)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/block/$ListeningBlockStatePredicate" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$AtomicBoolean, $AtomicBoolean$Type} from "packages/java/util/concurrent/atomic/$AtomicBoolean"
import {$TrackedBlockStatePredicate, $TrackedBlockStatePredicate$Type} from "packages/me/jellysquid/mods/lithium/common/block/$TrackedBlockStatePredicate"

export class $ListeningBlockStatePredicate extends $TrackedBlockStatePredicate {
static "LISTENING_MASK": integer
static readonly "FULLY_INITIALIZED": $AtomicBoolean


public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListeningBlockStatePredicate$Type = ($ListeningBlockStatePredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListeningBlockStatePredicate_ = $ListeningBlockStatePredicate$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeMatchesAnywhere" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BooleanOp, $BooleanOp$Type} from "packages/net/minecraft/world/phys/shapes/$BooleanOp"
import {$CallbackInfoReturnable, $CallbackInfoReturnable$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfoReturnable"

export class $VoxelShapeMatchesAnywhere {

constructor()

public static "cuboidMatchesAnywhere"(shapeA: $VoxelShape$Type, shapeB: $VoxelShape$Type, predicate: $BooleanOp$Type, cir: $CallbackInfoReturnable$Type<(boolean)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeMatchesAnywhere$Type = ($VoxelShapeMatchesAnywhere);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeMatchesAnywhere_ = $VoxelShapeMatchesAnywhere$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/shapes/$VoxelShapeCaster" {
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export interface $VoxelShapeCaster {

 "intersects"(arg0: $AABB$Type, arg1: double, arg2: double, arg3: double): boolean

(arg0: $AABB$Type, arg1: double, arg2: double, arg3: double): boolean
}

export namespace $VoxelShapeCaster {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VoxelShapeCaster$Type = ($VoxelShapeCaster);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VoxelShapeCaster_ = $VoxelShapeCaster$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/platform/windows/api/$Kernel32" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Kernel32 {

constructor()

public static "getModuleFileName"(arg0: long): string
public static "getCommandLine"(): long
public static "getLastError"(): integer
public static "getModuleHandleByNames"(arg0: (string)[]): long
public static "setEnvironmentVariable"(arg0: string, arg1: string): void
public static "getModuleHandleByName"(arg0: string): long
get "commandLine"(): long
get "lastError"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Kernel32$Type = ($Kernel32);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Kernel32_ = $Kernel32$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/compatibility/checks/$SodiumResourcePackMetadata" {
import {$MetadataSectionType, $MetadataSectionType$Type} from "packages/net/minecraft/server/packs/metadata/$MetadataSectionType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SodiumResourcePackMetadata extends $Record {
static readonly "CODEC": $Codec<($SodiumResourcePackMetadata)>
static readonly "SERIALIZER": $MetadataSectionType<($SodiumResourcePackMetadata)>

constructor(ignoredShaders: $List$Type<(string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "ignoredShaders"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumResourcePackMetadata$Type = ($SodiumResourcePackMetadata);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumResourcePackMetadata_ = $SodiumResourcePackMetadata$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/console/message/$Message" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MessageLevel, $MessageLevel$Type} from "packages/me/jellysquid/mods/sodium/client/gui/console/message/$MessageLevel"

export class $Message extends $Record {

constructor(level: $MessageLevel$Type, text: $Component$Type, duration: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "text"(): $Component
public "level"(): $MessageLevel
public "duration"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Message$Type = ($Message);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Message_ = $Message$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/console/message/$MessageLevel" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MessageLevel extends $Enum<($MessageLevel)> {
static readonly "INFO": $MessageLevel
static readonly "WARN": $MessageLevel
static readonly "SEVERE": $MessageLevel


public static "values"(): ($MessageLevel)[]
public static "valueOf"(arg0: string): $MessageLevel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageLevel$Type = (("warn") | ("severe") | ("info")) | ($MessageLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageLevel_ = $MessageLevel$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/util/collections/$WriteQueue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $WriteQueue<E> {

 "enqueue"(arg0: E): void
 "ensureCapacity"(arg0: integer): void
}

export namespace $WriteQueue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WriteQueue$Type<E> = ($WriteQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WriteQueue_<E> = $WriteQueue$Type<(E)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/$ChunkAwareEntityIterable" {
import {$EntityAccess, $EntityAccess$Type} from "packages/net/minecraft/world/level/entity/$EntityAccess"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $ChunkAwareEntityIterable<T extends $EntityAccess> {

 "lithiumIterateEntitiesInTrackedSections"(): $Iterable<(T)>

(): $Iterable<(T)>
}

export namespace $ChunkAwareEntityIterable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkAwareEntityIterable$Type<T> = ($ChunkAwareEntityIterable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkAwareEntityIterable_<T> = $ChunkAwareEntityIterable$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/ai/$WeightedListIterable" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ShufflingList, $ShufflingList$Type} from "packages/net/minecraft/world/entity/ai/behavior/$ShufflingList"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export interface $WeightedListIterable<U> extends $Iterable<(U)> {

 "iterator"(): $Iterator<(U)>
 "spliterator"(): $Spliterator<(U)>
 "forEach"(arg0: $Consumer$Type<(any)>): void

(list: $ShufflingList$Type<(T)>): $Iterable<(any)>
}

export namespace $WeightedListIterable {
function cast<T>(list: $ShufflingList$Type<(T)>): $Iterable<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedListIterable$Type<U> = ($WeightedListIterable<(U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedListIterable_<U> = $WeightedListIterable$Type<(U)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/state/$FastImmutableTableCache" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FastImmutableTableCache<R, C, V> {

constructor()

public "dedupValues"(values: (V)[]): (V)[]
public "dedupColumns"(columns: (C)[]): (C)[]
public "dedupRows"(rows: (R)[]): (R)[]
public "dedupIndices"(ints: (integer)[]): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastImmutableTableCache$Type<R, C, V> = ($FastImmutableTableCache<(R), (C), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastImmutableTableCache_<R, C, V> = $FastImmutableTableCache$Type<(R), (C), (V)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/util/$EnumBit" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EnumBit {

 "getBits"(): integer

(): integer
}

export namespace $EnumBit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumBit$Type = ($EnumBit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumBit_ = $EnumBit$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$MovementTrackerCache" {
import {$SectionedEntityMovementTracker, $SectionedEntityMovementTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/movement_tracker/$SectionedEntityMovementTracker"

export interface $MovementTrackerCache {

 "remove"(arg0: $SectionedEntityMovementTracker$Type<(any), (any)>): void
 "deduplicate"<S extends $SectionedEntityMovementTracker<(any), (any)>>(arg0: S): S
}

export namespace $MovementTrackerCache {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MovementTrackerCache$Type = ($MovementTrackerCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MovementTrackerCache_ = $MovementTrackerCache$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/data/fingerprint/$FingerprintMeasure" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$HashedFingerprint, $HashedFingerprint$Type} from "packages/me/jellysquid/mods/sodium/client/data/fingerprint/$HashedFingerprint"

export class $FingerprintMeasure extends $Record {

constructor(uuid: string, path: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "path"(): string
public static "create"(): $FingerprintMeasure
public "looselyMatches"(arg0: $HashedFingerprint$Type): boolean
public "hashed"(): $HashedFingerprint
public "uuid"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FingerprintMeasure$Type = ($FingerprintMeasure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FingerprintMeasure_ = $FingerprintMeasure$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/options/control/$CyclingControl" {
import {$Option, $Option$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$Option"
import {$ControlElement, $ControlElement$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$ControlElement"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Control, $Control$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/control/$Control"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"

export class $CyclingControl<T extends $Enum<(T)>> implements $Control<(T)> {

constructor(arg0: $Option$Type<(T)>, arg1: $Class$Type<(T)>, arg2: (T)[])
constructor(arg0: $Option$Type<(T)>, arg1: $Class$Type<(T)>, arg2: ($Component$Type)[])
constructor(arg0: $Option$Type<(T)>, arg1: $Class$Type<(T)>)

public "getMaxWidth"(): integer
public "createElement"(arg0: $Dim2i$Type): $ControlElement<(T)>
public "getOption"(): $Option<(T)>
public "getNames"(): ($Component)[]
get "maxWidth"(): integer
get "option"(): $Option<(T)>
get "names"(): ($Component)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CyclingControl$Type<T> = ($CyclingControl<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CyclingControl_<T> = $CyclingControl$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListenerMulti" {
import {$EntitySectionStorage, $EntitySectionStorage$Type} from "packages/net/minecraft/world/level/entity/$EntitySectionStorage"
import {$SectionPos, $SectionPos$Type} from "packages/net/minecraft/core/$SectionPos"
import {$NearbyEntityListener, $NearbyEntityListener$Type} from "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityListener"
import {$NearbyEntityTracker, $NearbyEntityTracker$Type} from "packages/me/jellysquid/mods/lithium/common/entity/nearby_tracker/$NearbyEntityTracker"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Range6Int, $Range6Int$Type} from "packages/me/jellysquid/mods/lithium/common/util/tuples/$Range6Int"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$ClassInstanceMultiMap, $ClassInstanceMultiMap$Type} from "packages/net/minecraft/util/$ClassInstanceMultiMap"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $NearbyEntityListenerMulti implements $NearbyEntityListener {

constructor()

public "toString"(): string
public "removeListener"<T extends $LivingEntity>(tracker: $NearbyEntityTracker$Type<(T)>): void
public "getChunkRange"(): $Range6Int
public "onEntityLeftRange"(entity: $Entity$Type): void
public "addListener"<T extends $LivingEntity>(tracker: $NearbyEntityTracker$Type<(T)>): void
public "onEntityEnteredRange"(entity: $Entity$Type): void
public "onSectionEnteredRange"<T>(entityTrackingSection: any, collection: $ClassInstanceMultiMap$Type<(T)>): void
public "getEntityClass"(): $Class<(any)>
public "removeFromAllChunksInRange"(entityCache: $EntitySectionStorage$Type<(any)>, prevCenterPos: $SectionPos$Type): void
public "updateChunkRegistrations"(entityCache: $EntitySectionStorage$Type<(any)>, prevCenterPos: $SectionPos$Type, prevChunkRange: $Range6Int$Type, newCenterPos: $SectionPos$Type, newChunkRange: $Range6Int$Type): void
public "addToAllChunksInRange"(entityCache: $EntitySectionStorage$Type<(any)>, newCenterPos: $SectionPos$Type): void
public "onSectionLeftRange"<T>(entityTrackingSection: any, collection: $ClassInstanceMultiMap$Type<(T)>): void
get "chunkRange"(): $Range6Int
get "entityClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NearbyEntityListenerMulti$Type = ($NearbyEntityListenerMulti);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NearbyEntityListenerMulti_ = $NearbyEntityListenerMulti$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/scheduler/$OrderedTickQueue" {
import {$AbstractQueue, $AbstractQueue$Type} from "packages/java/util/$AbstractQueue"
import {$ScheduledTick, $ScheduledTick$Type} from "packages/net/minecraft/world/ticks/$ScheduledTick"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $OrderedTickQueue<T> extends $AbstractQueue<($ScheduledTick<(T)>)> {

constructor(capacity: integer)
constructor()

public "setTickAtIndex"(index: integer, tick: $ScheduledTick$Type<(T)>): void
public "getTickAtIndex"(index: integer): $ScheduledTick<(T)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "iterator"(): $Iterator<($ScheduledTick<(T)>)>
public "poll"(): $ScheduledTick<(T)>
public "sort"(): void
public "offer"(tick: $ScheduledTick$Type<(T)>): boolean
public "removeNullsAndConsumed"(): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OrderedTickQueue$Type<T> = ($OrderedTickQueue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OrderedTickQueue_<T> = $OrderedTickQueue$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/sync/$GlFence" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GlFence {

constructor(arg0: long)

public "delete"(): void
public "sync"(arg0: long): void
public "sync"(): void
public "isCompleted"(): boolean
get "completed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlFence$Type = ($GlFence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlFence_ = $GlFence$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/$ServerWorldExtended" {
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"

export interface $ServerWorldExtended {

 "setNavigationActive"(arg0: $Mob$Type): void
 "setNavigationInactive"(arg0: $Mob$Type): void
}

export namespace $ServerWorldExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerWorldExtended$Type = ($ServerWorldExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerWorldExtended_ = $ServerWorldExtended$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gui/$SodiumOptionsGUI" {
import {$ScreenPrompt, $ScreenPrompt$Type} from "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPrompt"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ScreenPromptable, $ScreenPromptable$Type} from "packages/me/jellysquid/mods/sodium/client/gui/prompt/$ScreenPromptable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"
import {$Dim2i, $Dim2i$Type} from "packages/me/jellysquid/mods/sodium/client/util/$Dim2i"

/**
 * 
 * @deprecated
 */
export class $SodiumOptionsGUI extends $Screen implements $ScreenPromptable {
static readonly "DONATION_PROMPT_MESSAGE": $List<($FormattedText)>
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type)

public "setPrompt"(arg0: $ScreenPrompt$Type): void
public "getPrompt"(): $ScreenPrompt
public "getDimensions"(): $Dim2i
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "shouldCloseOnEsc"(): boolean
public "onClose"(): void
public "children"(): $List<(any)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "setPage"(arg0: $OptionPage$Type): void
set "prompt"(value: $ScreenPrompt$Type)
get "prompt"(): $ScreenPrompt
get "dimensions"(): $Dim2i
set "page"(value: $OptionPage$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SodiumOptionsGUI$Type = ($SodiumOptionsGUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SodiumOptionsGUI_ = $SodiumOptionsGUI$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/world/chunk/$ClassGroupFilterableList" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$EntityClassGroup, $EntityClassGroup$Type} from "packages/me/jellysquid/mods/lithium/common/entity/$EntityClassGroup"

export interface $ClassGroupFilterableList<T> {

 "getAllOfGroupType"(arg0: $EntityClassGroup$Type): $Collection<(T)>

(arg0: $EntityClassGroup$Type): $Collection<(T)>
}

export namespace $ClassGroupFilterableList {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClassGroupFilterableList$Type<T> = ($ClassGroupFilterableList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClassGroupFilterableList_<T> = $ClassGroupFilterableList$Type<(T)>;
}}
declare module "packages/me/jellysquid/mods/sodium/client/buffer/$ExtendedVertexFormat" {
import {$ExtendedVertexFormat$Element, $ExtendedVertexFormat$Element$Type} from "packages/me/jellysquid/mods/sodium/client/buffer/$ExtendedVertexFormat$Element"

export interface $ExtendedVertexFormat {

 "embeddium$getExtendedElements"(): ($ExtendedVertexFormat$Element)[]

(): ($ExtendedVertexFormat$Element)[]
}

export namespace $ExtendedVertexFormat {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedVertexFormat$Type = ($ExtendedVertexFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedVertexFormat_ = $ExtendedVertexFormat$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/world/$PaletteStorageExtended" {
import {$Palette, $Palette$Type} from "packages/net/minecraft/world/level/chunk/$Palette"

export interface $PaletteStorageExtended {

 "sodium$unpack"<T>(arg0: (T)[], arg1: $Palette$Type<(T)>): void

(arg0: (T)[], arg1: $Palette$Type<(T)>): void
}

export namespace $PaletteStorageExtended {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaletteStorageExtended$Type = ($PaletteStorageExtended);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaletteStorageExtended_ = $PaletteStorageExtended$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlVertexArrayTessellation" {
import {$GlVertexArray, $GlVertexArray$Type} from "packages/me/jellysquid/mods/sodium/client/gl/array/$GlVertexArray"
import {$GlPrimitiveType, $GlPrimitiveType$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlPrimitiveType"
import {$TessellationBinding, $TessellationBinding$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$TessellationBinding"
import {$CommandList, $CommandList$Type} from "packages/me/jellysquid/mods/sodium/client/gl/device/$CommandList"
import {$GlAbstractTessellation, $GlAbstractTessellation$Type} from "packages/me/jellysquid/mods/sodium/client/gl/tessellation/$GlAbstractTessellation"

export class $GlVertexArrayTessellation extends $GlAbstractTessellation {

constructor(arg0: $GlVertexArray$Type, arg1: $GlPrimitiveType$Type, arg2: ($TessellationBinding$Type)[])

public "init"(arg0: $CommandList$Type): void
public "delete"(arg0: $CommandList$Type): void
public "bind"(arg0: $CommandList$Type): void
public "unbind"(arg0: $CommandList$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlVertexArrayTessellation$Type = ($GlVertexArrayTessellation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlVertexArrayTessellation_ = $GlVertexArrayTessellation$Type;
}}
declare module "packages/me/jellysquid/mods/lithium/common/entity/pushable/$PushableEntityClassGroup" {
import {$EntityClassGroup, $EntityClassGroup$Type} from "packages/me/jellysquid/mods/lithium/common/entity/$EntityClassGroup"

export class $PushableEntityClassGroup {
static readonly "CACHABLE_UNPUSHABILITY": $EntityClassGroup
static readonly "MAYBE_PUSHABLE": $EntityClassGroup

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PushableEntityClassGroup$Type = ($PushableEntityClassGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PushableEntityClassGroup_ = $PushableEntityClassGroup$Type;
}}
declare module "packages/me/jellysquid/mods/sodium/client/render/viewport/frustum/$SimpleFrustum" {
import {$FrustumIntersection, $FrustumIntersection$Type} from "packages/org/joml/$FrustumIntersection"
import {$Frustum, $Frustum$Type} from "packages/me/jellysquid/mods/sodium/client/render/viewport/frustum/$Frustum"

export class $SimpleFrustum implements $Frustum {

constructor(arg0: $FrustumIntersection$Type)

public "testAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleFrustum$Type = ($SimpleFrustum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleFrustum_ = $SimpleFrustum$Type;
}}
