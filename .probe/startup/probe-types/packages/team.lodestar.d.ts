declare module "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure$StructurePiece" {
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $MultiBlockStructure$StructurePiece {
readonly "offset": $Vec3i
readonly "state": $BlockState

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: $BlockState$Type)

public "place"(arg0: $BlockPos$Type, arg1: $Level$Type, arg2: $BlockState$Type): void
public "place"(arg0: $BlockPos$Type, arg1: $Level$Type): void
public "canPlace"(arg0: $BlockPlaceContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiBlockStructure$StructurePiece$Type = ($MultiBlockStructure$StructurePiece);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiBlockStructure$StructurePiece_ = $MultiBlockStructure$StructurePiece$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$AbstractItemModelSmith" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AbstractItemModelSmith {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractItemModelSmith$Type = ($AbstractItemModelSmith);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractItemModelSmith_ = $AbstractItemModelSmith$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeToken" {
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RenderTypeToken implements $Supplier<($RenderStateShard$EmptyTextureStateShard)> {


public static "createCachedToken"(arg0: $RenderStateShard$EmptyTextureStateShard$Type): $RenderTypeToken
public static "createCachedToken"(arg0: $ResourceLocation$Type): $RenderTypeToken
public "hashCode"(): integer
public static "createToken"(arg0: $ResourceLocation$Type): $RenderTypeToken
public static "createToken"(arg0: $RenderStateShard$EmptyTextureStateShard$Type): $RenderTypeToken
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypeToken$Type = ($RenderTypeToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypeToken_ = $RenderTypeToken$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/trail/$TrailRenderPoint" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$VFXBuilders$WorldVFXBuilder, $VFXBuilders$WorldVFXBuilder$Type} from "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$WorldVFXBuilder"

export class $TrailRenderPoint {
readonly "xp": float
readonly "xn": float
readonly "yp": float
readonly "yn": float
readonly "z": float

constructor(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float)
constructor(arg0: $Vector4f$Type, arg1: $Vec2$Type)

public "renderEnd"(arg0: $VertexConsumer$Type, arg1: $VFXBuilders$WorldVFXBuilder$Type, arg2: float, arg3: float, arg4: float, arg5: float): void
public "renderMid"(arg0: $VertexConsumer$Type, arg1: $VFXBuilders$WorldVFXBuilder$Type, arg2: float, arg3: float, arg4: float, arg5: float): void
public "renderStart"(arg0: $VertexConsumer$Type, arg1: $VFXBuilders$WorldVFXBuilder$Type, arg2: float, arg3: float, arg4: float, arg5: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrailRenderPoint$Type = ($TrailRenderPoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrailRenderPoint_ = $TrailRenderPoint$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleDataBuilder" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"

export class $GenericParticleDataBuilder {


public "build"(): $GenericParticleData
public "setEasing"(arg0: $Easing$Type, arg1: $Easing$Type): $GenericParticleDataBuilder
public "setEasing"(arg0: $Easing$Type): $GenericParticleDataBuilder
public "setCoefficient"(arg0: float): $GenericParticleDataBuilder
set "easing"(value: $Easing$Type)
set "coefficient"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericParticleDataBuilder$Type = ($GenericParticleDataBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericParticleDataBuilder_ = $GenericParticleDataBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockModelProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$LodestoneBlockStateProvider, $LodestoneBlockStateProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockStateProvider"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$BlockModelBuilder, $BlockModelBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$BlockModelBuilder"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$BlockModelProvider, $BlockModelProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockModelProvider"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneBlockModelProvider extends $BlockModelProvider {
static readonly "BLOCK_TEXTURE_CACHE": $HashMap<(string), ($ResourceLocation)>
static readonly "BLOCK_FOLDER": string
static readonly "ITEM_FOLDER": string
readonly "generatedModels": $Map<($ResourceLocation), (T)>
readonly "existingFileHelper": $ExistingFileHelper

constructor(arg0: $LodestoneBlockStateProvider$Type, arg1: $PackOutput$Type, arg2: string, arg3: $ExistingFileHelper$Type)

public "nested"(): $BlockModelBuilder
public "getBuilder"(arg0: string): $BlockModelBuilder
public "extendWithFolder"(arg0: $ResourceLocation$Type): $ResourceLocation
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockModelProvider$Type = ($LodestoneBlockModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockModelProvider_ = $LodestoneBlockModelProvider$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/container/$ItemInventory" {
import {$SimpleContainer, $SimpleContainer$Type} from "packages/net/minecraft/world/$SimpleContainer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemInventory extends $SimpleContainer {

constructor(arg0: $ItemStack$Type, arg1: integer)

public "setChanged"(): void
public "stillValid"(arg0: $Player$Type): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemInventory$Type = ($ItemInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemInventory_ = $ItemInventory$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/client/$LodestoneRenderTypeRegistry" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder, $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type} from "packages/team/lodestar/lodestone/registry/client/$LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ColorLogicStateShard, $RenderStateShard$ColorLogicStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ColorLogicStateShard"
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard, $RenderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$RenderStateShard$TextureStateShard, $RenderStateShard$TextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$LodestoneRenderType, $LodestoneRenderType$Type} from "packages/team/lodestar/lodestone/systems/rendering/$LodestoneRenderType"
import {$RenderTypeData, $RenderTypeData$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeData"
import {$ShaderUniformHandler, $ShaderUniformHandler$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$ShaderUniformHandler"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$RenderTypeProvider, $RenderTypeProvider$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeProvider"

export class $LodestoneRenderTypeRegistry extends $RenderStateShard {
static readonly "TRANSPARENT_FUNCTION": $Runnable
static readonly "ADDITIVE_FUNCTION": $Runnable
static readonly "NO_TEXTURE": $RenderStateShard$EmptyTextureStateShard
static readonly "LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "NO_LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "CULL": $RenderStateShard$CullStateShard
static readonly "NO_CULL": $RenderStateShard$CullStateShard
static readonly "COPIES": $HashMap<($Pair<(any), ($LodestoneRenderType)>), ($LodestoneRenderType)>
static readonly "GENERIC": $Function<($RenderTypeData), ($LodestoneRenderType)>
static readonly "ADDITIVE_PARTICLE": $LodestoneRenderType
static readonly "ADDITIVE_BLOCK_PARTICLE": $LodestoneRenderType
static readonly "ADDITIVE_BLOCK": $LodestoneRenderType
static readonly "ADDITIVE_SOLID": $LodestoneRenderType
static readonly "TRANSPARENT_PARTICLE": $LodestoneRenderType
static readonly "TRANSPARENT_BLOCK_PARTICLE": $LodestoneRenderType
static readonly "TRANSPARENT_BLOCK": $LodestoneRenderType
static readonly "TRANSPARENT_SOLID": $LodestoneRenderType
static readonly "LUMITRANSPARENT_PARTICLE": $LodestoneRenderType
static readonly "LUMITRANSPARENT_BLOCK_PARTICLE": $LodestoneRenderType
static readonly "LUMITRANSPARENT_BLOCK": $LodestoneRenderType
static readonly "LUMITRANSPARENT_SOLID": $LodestoneRenderType
static readonly "TEXTURE": $RenderTypeProvider
static readonly "TRANSPARENT_TEXTURE": $RenderTypeProvider
static readonly "TRANSPARENT_TEXTURE_TRIANGLE": $RenderTypeProvider
static readonly "TRANSPARENT_SCROLLING_TEXTURE_TRIANGLE": $RenderTypeProvider
static readonly "TRANSPARENT_TEXT": $RenderTypeProvider
static readonly "ADDITIVE_TEXTURE": $RenderTypeProvider
static readonly "ADDITIVE_TEXTURE_TRIANGLE": $RenderTypeProvider
static readonly "ADDITIVE_SCROLLING_TEXTURE_TRIANGLE": $RenderTypeProvider
static readonly "ADDITIVE_TEXT": $RenderTypeProvider
static readonly "VIEW_SCALE_Z_EPSILON": float
static readonly "MAX_ENCHANTMENT_GLINT_SPEED_MILLIS": double
readonly "name": string
 "setupState": $Runnable
static readonly "NO_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "ADDITIVE_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "LIGHTNING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "GLINT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "CRUMBLING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "TRANSLUCENT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "NO_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_MIPPED_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_MOVING_BLOCK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_NO_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_BEACON_BEAM_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_DECAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_NO_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SHADOW_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_ALPHA_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_EYES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENERGY_SWIRL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LEASH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_WATER_MASK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LIGHTNING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRIPWIRE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_PORTAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_GATEWAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LINES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "BLOCK_SHEET_MIPPED": $RenderStateShard$TextureStateShard
static readonly "BLOCK_SHEET": $RenderStateShard$TextureStateShard
static readonly "DEFAULT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "ENTITY_GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "NO_OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "NO_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "EQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "LEQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "GREATER_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "COLOR_DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "COLOR_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "NO_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "POLYGON_OFFSET_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "VIEW_OFFSET_Z_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "MAIN_TARGET": $RenderStateShard$OutputStateShard
static readonly "OUTLINE_TARGET": $RenderStateShard$OutputStateShard
static readonly "TRANSLUCENT_TARGET": $RenderStateShard$OutputStateShard
static readonly "PARTICLES_TARGET": $RenderStateShard$OutputStateShard
static readonly "WEATHER_TARGET": $RenderStateShard$OutputStateShard
static readonly "CLOUDS_TARGET": $RenderStateShard$OutputStateShard
static readonly "ITEM_ENTITY_TARGET": $RenderStateShard$OutputStateShard
static readonly "DEFAULT_LINE": $RenderStateShard$LineStateShard
static readonly "NO_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard
static readonly "OR_REVERSE_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard

constructor(arg0: string, arg1: $Runnable$Type, arg2: $Runnable$Type)

public static "builder"(): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public static "copy"(arg0: string, arg1: $LodestoneRenderType$Type): $LodestoneRenderType
public static "copy"(arg0: $LodestoneRenderType$Type): $LodestoneRenderType
public static "copyWithUniformChanges"(arg0: string, arg1: $LodestoneRenderType$Type, arg2: $ShaderUniformHandler$Type): $LodestoneRenderType
public static "copyWithUniformChanges"(arg0: $LodestoneRenderType$Type, arg1: $ShaderUniformHandler$Type): $LodestoneRenderType
public static "createGenericRenderType"(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: $RenderStateShard$ShaderStateShard$Type, arg4: $RenderStateShard$TransparencyStateShard$Type, arg5: $RenderStateShard$EmptyTextureStateShard$Type, arg6: $RenderStateShard$CullStateShard$Type): $LodestoneRenderType
public static "createGenericRenderType"(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type, arg4: $ShaderUniformHandler$Type): $LodestoneRenderType
public static "createGenericRenderType"(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type): $LodestoneRenderType
/**
 * 
 * @deprecated
 */
public static "createGenericRenderType"(arg0: string, arg1: $VertexFormat$Type, arg2: $RenderStateShard$ShaderStateShard$Type, arg3: $RenderStateShard$TransparencyStateShard$Type, arg4: $ResourceLocation$Type): $LodestoneRenderType
public static "copyAndStore"(arg0: any, arg1: $LodestoneRenderType$Type): $LodestoneRenderType
public static "copyAndStore"(arg0: any, arg1: $LodestoneRenderType$Type, arg2: $ShaderUniformHandler$Type): $LodestoneRenderType
public static "addRenderTypeModifier"(arg0: $Consumer$Type<($LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type)>): void
public static "applyUniformChanges"(arg0: $LodestoneRenderType$Type, arg1: $ShaderUniformHandler$Type): $LodestoneRenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneRenderTypeRegistry$Type = ($LodestoneRenderTypeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneRenderTypeRegistry_ = $LodestoneRenderTypeRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneWorldEventTypeRegistry" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$WorldEventType, $WorldEventType$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $LodestoneWorldEventTypeRegistry {
static "EVENT_TYPES": $HashMap<($ResourceLocation), ($WorldEventType)>

constructor()

public static "registerEventType"(arg0: $WorldEventType$Type): $WorldEventType
public static "postRegistryEvent"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWorldEventTypeRegistry$Type = ($LodestoneWorldEventTypeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWorldEventTypeRegistry_ = $LodestoneWorldEventTypeRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/data/builder/$NBTCarryRecipeBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"
import {$CriterionTriggerInstance, $CriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/$CriterionTriggerInstance"

export class $NBTCarryRecipeBuilder {

constructor(arg0: $ItemLike$Type, arg1: integer, arg2: $Ingredient$Type)

public "patternLine"(arg0: string): $NBTCarryRecipeBuilder
public "key"(arg0: character, arg1: $Ingredient$Type): $NBTCarryRecipeBuilder
public "key"(arg0: character, arg1: $ItemLike$Type): $NBTCarryRecipeBuilder
public "key"(arg0: character, arg1: $TagKey$Type<($Item$Type)>): $NBTCarryRecipeBuilder
public "build"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public "build"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: string): void
public "build"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ResourceLocation$Type): void
public "setGroup"(arg0: string): $NBTCarryRecipeBuilder
public static "shapedRecipe"(arg0: $ItemLike$Type, arg1: integer, arg2: $Ingredient$Type): $NBTCarryRecipeBuilder
public static "shapedRecipe"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $NBTCarryRecipeBuilder
public "addCriterion"(arg0: string, arg1: $CriterionTriggerInstance$Type): $NBTCarryRecipeBuilder
set "group"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTCarryRecipeBuilder$Type = ($NBTCarryRecipeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTCarryRecipeBuilder_ = $NBTCarryRecipeBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODStrategy" {
import {$LODBuilder, $LODBuilder$Type} from "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODBuilder"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LevelOfDetail, $LevelOfDetail$Type} from "packages/team/lodestar/lodestone/systems/model/obj/lod/$LevelOfDetail"
import {$LODStrategy$LODBuilderSetup, $LODStrategy$LODBuilderSetup$Type} from "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODStrategy$LODBuilderSetup"

export class $LODStrategy<T> implements $LODBuilder<(T)> {
 "levelsOfDetail": $List<($LevelOfDetail<(T)>)>

constructor(arg0: $LODStrategy$LODBuilderSetup$Type<(T)>)

public static "Performance"(arg0: $LODStrategy$LODBuilderSetup$Type<(integer)>): $LODStrategy<(integer)>
public static "Distance"(arg0: $LODStrategy$LODBuilderSetup$Type<(float)>): $LODStrategy<(float)>
public "getLODLevel"(arg0: $Vector3f$Type): $LevelOfDetail<(T)>
public "create"(arg0: T, arg1: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LODStrategy$Type<T> = ($LODStrategy<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LODStrategy_<T> = $LODStrategy$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent" {
import {$DirectionalBehaviorComponent, $DirectionalBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$DirectionalBehaviorComponent"
import {$SparkBehaviorComponent, $SparkBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$SparkBehaviorComponent"
import {$ExtrudingSparkBehaviorComponent, $ExtrudingSparkBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$ExtrudingSparkBehaviorComponent"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"

export interface $LodestoneBehaviorComponent {

 "tick"(arg0: $LodestoneWorldParticle$Type): void
 "getBehaviorType"(): $LodestoneParticleBehavior

(arg0: $LodestoneWorldParticle$Type): void
}

export namespace $LodestoneBehaviorComponent {
const DIRECTIONAL: $DirectionalBehaviorComponent
const SPARK: $SparkBehaviorComponent
const EXTRUDING_SPARK: $ExtrudingSparkBehaviorComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBehaviorComponent$Type = ($LodestoneBehaviorComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBehaviorComponent_ = $LodestoneBehaviorComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/capability/$LodestoneCapability" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"

export interface $LodestoneCapability extends $INBTSerializable<($CompoundTag)> {

 "deserializeNBT"(arg0: $CompoundTag$Type): void
 "serializeNBT"(): $CompoundTag
}

export namespace $LodestoneCapability {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneCapability$Type = ($LodestoneCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneCapability_ = $LodestoneCapability$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/$BlockStateSmithTypes" {
import {$BlockStateSmith, $BlockStateSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$BlockStateSmith"
import {$FenceGateBlock, $FenceGateBlock$Type} from "packages/net/minecraft/world/level/block/$FenceGateBlock"
import {$TrapDoorBlock, $TrapDoorBlock$Type} from "packages/net/minecraft/world/level/block/$TrapDoorBlock"
import {$WallTorchBlock, $WallTorchBlock$Type} from "packages/net/minecraft/world/level/block/$WallTorchBlock"
import {$PressurePlateBlock, $PressurePlateBlock$Type} from "packages/net/minecraft/world/level/block/$PressurePlateBlock"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$WallBlock, $WallBlock$Type} from "packages/net/minecraft/world/level/block/$WallBlock"
import {$RotatedPillarBlock, $RotatedPillarBlock$Type} from "packages/net/minecraft/world/level/block/$RotatedPillarBlock"
import {$TorchBlock, $TorchBlock$Type} from "packages/net/minecraft/world/level/block/$TorchBlock"
import {$FenceBlock, $FenceBlock$Type} from "packages/net/minecraft/world/level/block/$FenceBlock"
import {$ButtonBlock, $ButtonBlock$Type} from "packages/net/minecraft/world/level/block/$ButtonBlock"
import {$DoorBlock, $DoorBlock$Type} from "packages/net/minecraft/world/level/block/$DoorBlock"
import {$StairBlock, $StairBlock$Type} from "packages/net/minecraft/world/level/block/$StairBlock"
import {$SignBlock, $SignBlock$Type} from "packages/net/minecraft/world/level/block/$SignBlock"
import {$ModularBlockStateSmith, $ModularBlockStateSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith"
import {$SlabBlock, $SlabBlock$Type} from "packages/net/minecraft/world/level/block/$SlabBlock"
import {$HorizontalDirectionalBlock, $HorizontalDirectionalBlock$Type} from "packages/net/minecraft/world/level/block/$HorizontalDirectionalBlock"
import {$DirectionalBlock, $DirectionalBlock$Type} from "packages/net/minecraft/world/level/block/$DirectionalBlock"

export class $BlockStateSmithTypes {
static "CUSTOM_MODEL": $ModularBlockStateSmith<($Block)>
static "FULL_BLOCK": $BlockStateSmith<($Block)>
static "GRASS_BLOCK": $BlockStateSmith<($Block)>
static "CROSS_MODEL_BLOCK": $BlockStateSmith<($Block)>
static "TALL_CROSS_MODEL_BLOCK": $BlockStateSmith<($Block)>
static "LEAVES_BLOCK": $BlockStateSmith<($Block)>
static "LOG_BLOCK": $BlockStateSmith<($RotatedPillarBlock)>
static "AXIS_BLOCK": $BlockStateSmith<($RotatedPillarBlock)>
static "WOOD_BLOCK": $BlockStateSmith<($RotatedPillarBlock)>
static "DIRECTIONAL_BLOCK": $BlockStateSmith<($DirectionalBlock)>
static "HORIZONTAL_BLOCK": $BlockStateSmith<($HorizontalDirectionalBlock)>
static "STAIRS_BLOCK": $BlockStateSmith<($StairBlock)>
static "SLAB_BLOCK": $BlockStateSmith<($SlabBlock)>
static "WALL_BLOCK": $BlockStateSmith<($WallBlock)>
static "FENCE_BLOCK": $BlockStateSmith<($FenceBlock)>
static "FENCE_GATE_BLOCK": $BlockStateSmith<($FenceGateBlock)>
static "PRESSURE_PLATE_BLOCK": $BlockStateSmith<($PressurePlateBlock)>
static "BUTTON_BLOCK": $BlockStateSmith<($ButtonBlock)>
static "DOOR_BLOCK": $BlockStateSmith<($DoorBlock)>
static "TRAPDOOR_BLOCK": $BlockStateSmith<($TrapDoorBlock)>
static "TORCH_BLOCK": $BlockStateSmith<($TorchBlock)>
static "WALL_TORCH_BLOCK": $BlockStateSmith<($WallTorchBlock)>
static "WOODEN_SIGN_BLOCK": $BlockStateSmith<($SignBlock)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateSmithTypes$Type = ($BlockStateSmithTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateSmithTypes_ = $BlockStateSmithTypes$Type;
}}
declare module "packages/team/lodestar/lodestone/command/$RemoveActiveWorldEventsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $RemoveActiveWorldEventsCommand {

constructor()

public static "register"(): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemoveActiveWorldEventsCommand$Type = ($RemoveActiveWorldEventsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemoveActiveWorldEventsCommand_ = $RemoveActiveWorldEventsCommand$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/lod/$MultiLODModel" {
import {$ObjModel, $ObjModel$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$ObjModel"
import {$Face, $Face$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$Face"
import {$LODStrategy, $LODStrategy$Type} from "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODStrategy"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $MultiLODModel extends $ObjModel {
 "faces": $ArrayList<($Face)>
 "modelLocation": $ResourceLocation

constructor(arg0: $LODStrategy$Type<(any)>)

public "loadModel"(): void
public "renderModel"(arg0: $PoseStack$Type, arg1: $RenderType$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiLODModel$Type = ($MultiLODModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiLODModel_ = $MultiLODModel$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$PlacementAssistantHandler" {
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$IPlacementAssistant, $IPlacementAssistant$Type} from "packages/team/lodestar/lodestone/systems/placementassistance/$IPlacementAssistant"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$PlayerInteractEvent$RightClickBlock, $PlayerInteractEvent$RightClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickBlock"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $PlacementAssistantHandler {
static readonly "ASSISTANTS": $ArrayList<($IPlacementAssistant)>
static "animationTick": integer
static "target": $BlockHitResult

constructor()

public static "tick"(arg0: $Player$Type, arg1: $HitResult$Type): void
public static "registerPlacementAssistants"(arg0: $FMLCommonSetupEvent$Type): void
public static "placeBlock"(arg0: $PlayerInteractEvent$RightClickBlock$Type): void
public static "getCurrentAlpha"(): float
get "currentAlpha"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlacementAssistantHandler$Type = ($PlacementAssistantHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlacementAssistantHandler_ = $PlacementAssistantHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/tag/$LodestoneItemTags" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $LodestoneItemTags {
static readonly "NUGGETS_COPPER": $TagKey<($Item)>
static readonly "INGOTS_COPPER": $TagKey<($Item)>
static readonly "NUGGETS_LEAD": $TagKey<($Item)>
static readonly "INGOTS_LEAD": $TagKey<($Item)>
static readonly "NUGGETS_SILVER": $TagKey<($Item)>
static readonly "INGOTS_SILVER": $TagKey<($Item)>
static readonly "NUGGETS_ALUMINUM": $TagKey<($Item)>
static readonly "INGOTS_ALUMINUM": $TagKey<($Item)>
static readonly "NUGGETS_NICKEL": $TagKey<($Item)>
static readonly "INGOTS_NICKEL": $TagKey<($Item)>
static readonly "NUGGETS_URANIUM": $TagKey<($Item)>
static readonly "INGOTS_URANIUM": $TagKey<($Item)>
static readonly "NUGGETS_OSMIUM": $TagKey<($Item)>
static readonly "INGOTS_OSMIUM": $TagKey<($Item)>
static readonly "NUGGETS_ZINC": $TagKey<($Item)>
static readonly "INGOTS_ZINC": $TagKey<($Item)>
static readonly "NUGGETS_TIN": $TagKey<($Item)>
static readonly "INGOTS_TIN": $TagKey<($Item)>

constructor()

public static "forgeTag"(arg0: string): $TagKey<($Item)>
public static "modTag"(arg0: string): $TagKey<($Item)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemTags$Type = ($LodestoneItemTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemTags_ = $LodestoneItemTags$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith$StateFunction" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ModelFile, $ModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile"

export interface $AbstractBlockStateSmith$StateFunction<T extends $Block> {

 "act"(arg0: T, arg1: $ModelFile$Type): void

(arg0: T, arg1: $ModelFile$Type): void
}

export namespace $AbstractBlockStateSmith$StateFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBlockStateSmith$StateFunction$Type<T> = ($AbstractBlockStateSmith$StateFunction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBlockStateSmith$StateFunction_<T> = $AbstractBlockStateSmith$StateFunction$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/network/$ClearFireEffectInstancePacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $ClearFireEffectInstancePacket extends $LodestoneClientPacket {

constructor(arg0: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ClearFireEffectInstancePacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClearFireEffectInstancePacket$Type = ($ClearFireEffectInstancePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClearFireEffectInstancePacket_ = $ClearFireEffectInstancePacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SimpleParticleOptions$ParticleDiscardFunctionType extends $Enum<($SimpleParticleOptions$ParticleDiscardFunctionType)> {
static readonly "NONE": $SimpleParticleOptions$ParticleDiscardFunctionType
static readonly "INVISIBLE": $SimpleParticleOptions$ParticleDiscardFunctionType
static readonly "ENDING_CURVE_INVISIBLE": $SimpleParticleOptions$ParticleDiscardFunctionType


public static "values"(): ($SimpleParticleOptions$ParticleDiscardFunctionType)[]
public static "valueOf"(arg0: string): $SimpleParticleOptions$ParticleDiscardFunctionType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleParticleOptions$ParticleDiscardFunctionType$Type = (("ending_curve_invisible") | ("invisible") | ("none")) | ($SimpleParticleOptions$ParticleDiscardFunctionType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleParticleOptions$ParticleDiscardFunctionType_ = $SimpleParticleOptions$ParticleDiscardFunctionType$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestonePacketRegistry" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $LodestonePacketRegistry {
static readonly "PROTOCOL_VERSION": string
static "LODESTONE_CHANNEL": $SimpleChannel

constructor()

public static "registerPackets"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestonePacketRegistry$Type = ($LodestonePacketRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestonePacketRegistry_ = $LodestonePacketRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$FireEffectHandler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LodestoneEntityDataCapability, $LodestoneEntityDataCapability$Type} from "packages/team/lodestar/lodestone/capability/$LodestoneEntityDataCapability"
import {$FireEffectInstance, $FireEffectInstance$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectInstance"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $FireEffectHandler {

constructor()

public static "getFireEffectInstance"(arg0: $Entity$Type): $FireEffectInstance
public static "setCustomFireInstance"(arg0: $Entity$Type, arg1: $FireEffectInstance$Type): void
public static "onVanillaFireTimeUpdate"(arg0: $Entity$Type): void
public static "entityUpdate"(arg0: $Entity$Type): void
public static "deserializeNBT"(arg0: $LodestoneEntityDataCapability$Type, arg1: $CompoundTag$Type): void
public static "serializeNBT"(arg0: $LodestoneEntityDataCapability$Type, arg1: $CompoundTag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireEffectHandler$Type = ($FireEffectHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireEffectHandler_ = $FireEffectHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/network/capability/$SyncLodestoneEntityCapabilityPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LodestoneTwoWayNBTPacket, $LodestoneTwoWayNBTPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneTwoWayNBTPacket"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $SyncLodestoneEntityCapabilityPacket extends $LodestoneTwoWayNBTPacket {
static readonly "ENTITY_ID": string

constructor(arg0: $CompoundTag$Type)
constructor(arg0: integer, arg1: $CompoundTag$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SyncLodestoneEntityCapabilityPacket
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "serverExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public "clientExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public static "handleTag"(arg0: integer, arg1: $CompoundTag$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncLodestoneEntityCapabilityPacket$Type = ($SyncLodestoneEntityCapabilityPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncLodestoneEntityCapabilityPacket_ = $SyncLodestoneEntityCapabilityPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/network/interaction/$UpdateLeftClickPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$LodestoneServerPacket, $LodestoneServerPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneServerPacket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $UpdateLeftClickPacket extends $LodestoneServerPacket {

constructor(arg0: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $UpdateLeftClickPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateLeftClickPacket$Type = ($UpdateLeftClickPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateLeftClickPacket_ = $UpdateLeftClickPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneWorldParticleType" {
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$AbstractLodestoneParticleType, $AbstractLodestoneParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$AbstractLodestoneParticleType"

export class $LodestoneWorldParticleType extends $AbstractLodestoneParticleType<($WorldParticleOptions)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWorldParticleType$Type = ($LodestoneWorldParticleType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWorldParticleType_ = $LodestoneWorldParticleType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$BedrockDirectionalBehaviorComponent" {
import {$AbstractParticleBuilder, $AbstractParticleBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/builder/$AbstractParticleBuilder"
import {$DirectionalBehaviorComponent, $DirectionalBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$DirectionalBehaviorComponent"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"

export class $BedrockDirectionalBehaviorComponent extends $DirectionalBehaviorComponent {
 "pitch": float
 "yaw": float

constructor(arg0: $SpinParticleData$Type)
constructor()
constructor(arg0: $SpinParticleData$Type, arg1: $SpinParticleData$Type)

public "tick"(arg0: $LodestoneWorldParticle$Type): void
public "getDirection"(arg0: $LodestoneWorldParticle$Type): $Vec3
public "getPitchData"(arg0: $SpinParticleData$Type): $SpinParticleData
public "getPitchData"(arg0: $AbstractParticleBuilder$Type<($WorldParticleOptions$Type)>): $SpinParticleData
public "getYawData"(arg0: $SpinParticleData$Type): $SpinParticleData
public "getYawData"(arg0: $AbstractParticleBuilder$Type<($WorldParticleOptions$Type)>): $SpinParticleData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BedrockDirectionalBehaviorComponent$Type = ($BedrockDirectionalBehaviorComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BedrockDirectionalBehaviorComponent_ = $BedrockDirectionalBehaviorComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/particle/$LodestoneScreenParticleRegistry" {
import {$ScreenParticleType, $ScreenParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$ScreenParticleType$ParticleProvider, $ScreenParticleType$ParticleProvider$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType$ParticleProvider"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegisterParticleProvidersEvent, $RegisterParticleProvidersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterParticleProvidersEvent"
import {$SpriteSet, $SpriteSet$Type} from "packages/net/minecraft/client/particle/$SpriteSet"

export class $LodestoneScreenParticleRegistry {
static readonly "PARTICLE_TYPES": $ArrayList<($ScreenParticleType<(any)>)>
static readonly "WISP": $ScreenParticleType<($ScreenParticleOptions)>
static readonly "SMOKE": $ScreenParticleType<($ScreenParticleOptions)>
static readonly "SPARKLE": $ScreenParticleType<($ScreenParticleOptions)>
static readonly "TWINKLE": $ScreenParticleType<($ScreenParticleOptions)>
static readonly "STAR": $ScreenParticleType<($ScreenParticleOptions)>

constructor()

public static "registerProvider"<T extends $ScreenParticleOptions>(arg0: $ScreenParticleType$Type<(T)>, arg1: $ScreenParticleType$ParticleProvider$Type<(T)>): void
public static "registerType"<T extends $ScreenParticleOptions>(arg0: $ScreenParticleType$Type<(T)>): $ScreenParticleType<(T)>
public static "registerParticleFactory"(arg0: $RegisterParticleProvidersEvent$Type): void
public static "getSpriteSet"(arg0: $ResourceLocation$Type): $SpriteSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneScreenParticleRegistry$Type = ($LodestoneScreenParticleRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneScreenParticleRegistry_ = $LodestoneScreenParticleRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/network/screenshake/$PositionedScreenshakePacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ScreenshakePacket, $ScreenshakePacket$Type} from "packages/team/lodestar/lodestone/network/screenshake/$ScreenshakePacket"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PositionedScreenshakePacket extends $ScreenshakePacket {
readonly "position": $Vec3
readonly "falloffDistance": float
readonly "maxDistance": float
readonly "falloffEasing": $Easing
readonly "duration": integer
 "intensity1": float
 "intensity2": float
 "intensity3": float
 "intensityCurveStartEasing": $Easing
 "intensityCurveEndEasing": $Easing

constructor(arg0: integer, arg1: $Vec3$Type, arg2: float, arg3: float, arg4: $Easing$Type)
constructor(arg0: integer, arg1: $Vec3$Type, arg2: float, arg3: float)

public static "decode"(arg0: $FriendlyByteBuf$Type): $PositionedScreenshakePacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PositionedScreenshakePacket$Type = ($PositionedScreenshakePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PositionedScreenshakePacket_ = $PositionedScreenshakePacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$ScreenVFXBuilder" {
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier, $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier$Type} from "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export class $VFXBuilders$ScreenVFXBuilder {

constructor()

public "setZLevel"(arg0: integer): $VFXBuilders$ScreenVFXBuilder
public "setUVWithWidth"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $VFXBuilders$ScreenVFXBuilder
public "setUVWithWidth"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$ScreenVFXBuilder
public "setUVWithWidth"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float): $VFXBuilders$ScreenVFXBuilder
public "end"(): $VFXBuilders$ScreenVFXBuilder
public "begin"(): $VFXBuilders$ScreenVFXBuilder
public "setFormat"(arg0: $VertexFormat$Type): $VFXBuilders$ScreenVFXBuilder
public "setColor"(arg0: $Color$Type, arg1: float): $VFXBuilders$ScreenVFXBuilder
public "setColor"(arg0: $Color$Type): $VFXBuilders$ScreenVFXBuilder
public "setColor"(arg0: float, arg1: float, arg2: float): $VFXBuilders$ScreenVFXBuilder
public "setColor"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$ScreenVFXBuilder
public "setShaderTexture"(arg0: $ResourceLocation$Type): $VFXBuilders$ScreenVFXBuilder
public "setShader"(arg0: $ShaderInstance$Type): $VFXBuilders$ScreenVFXBuilder
public "setShader"(arg0: $Supplier$Type<($ShaderInstance$Type)>): $VFXBuilders$ScreenVFXBuilder
public "draw"(arg0: $PoseStack$Type): $VFXBuilders$ScreenVFXBuilder
public "blit"(arg0: $PoseStack$Type): $VFXBuilders$ScreenVFXBuilder
public "setAlpha"(arg0: float): $VFXBuilders$ScreenVFXBuilder
public "setPosition"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$ScreenVFXBuilder
public "setLight"(arg0: integer): $VFXBuilders$ScreenVFXBuilder
public "setPositionWithWidth"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$ScreenVFXBuilder
public "setPosTexDefaultFormat"(): $VFXBuilders$ScreenVFXBuilder
public "setPosColorDefaultFormat"(): $VFXBuilders$ScreenVFXBuilder
public "overrideBufferBuilder"(arg0: $BufferBuilder$Type): $VFXBuilders$ScreenVFXBuilder
public "setPosColorTexDefaultFormat"(): $VFXBuilders$ScreenVFXBuilder
public "endAndProceed"(): $VFXBuilders$ScreenVFXBuilder
public "setUV"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$ScreenVFXBuilder
public "setUV"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float): $VFXBuilders$ScreenVFXBuilder
public "setUV"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $VFXBuilders$ScreenVFXBuilder
public "setColorRaw"(arg0: float, arg1: float, arg2: float): $VFXBuilders$ScreenVFXBuilder
public "setVertexSupplier"(arg0: $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier$Type): $VFXBuilders$ScreenVFXBuilder
public "setPosColorTexLightmapDefaultFormat"(): $VFXBuilders$ScreenVFXBuilder
set "zLevel"(value: integer)
set "format"(value: $VertexFormat$Type)
set "color"(value: $Color$Type)
set "shaderTexture"(value: $ResourceLocation$Type)
set "shader"(value: $ShaderInstance$Type)
set "shader"(value: $Supplier$Type<($ShaderInstance$Type)>)
set "alpha"(value: float)
set "light"(value: integer)
set "vertexSupplier"(value: $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VFXBuilders$ScreenVFXBuilder$Type = ($VFXBuilders$ScreenVFXBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VFXBuilders$ScreenVFXBuilder_ = $VFXBuilders$ScreenVFXBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/postprocess/$DynamicShaderFxInstance" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"

export class $DynamicShaderFxInstance {

constructor()

public "remove"(): void
public "update"(arg0: double): void
public "getTime"(): float
public "isRemoved"(): boolean
public "writeDataToBuffer"(arg0: $BiConsumer$Type<(integer), (float)>): void
get "time"(): float
get "removed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicShaderFxInstance$Type = ($DynamicShaderFxInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicShaderFxInstance_ = $DynamicShaderFxInstance$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $SimpleParticleOptions {
static readonly "DEFAULT_COLOR": $ColorParticleData
static readonly "DEFAULT_SPIN": $SpinParticleData
static readonly "DEFAULT_GENERIC": $GenericParticleData
 "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
 "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
 "colorData": $ColorParticleData
 "transparencyData": $GenericParticleData
 "scaleData": $GenericParticleData
 "spinData": $SpinParticleData
 "lifetimeSupplier": $Supplier<(integer)>
 "lifeDelaySupplier": $Supplier<(integer)>
 "gravityStrengthSupplier": $Supplier<(float)>
 "frictionStrengthSupplier": $Supplier<(float)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleParticleOptions$Type = ($SimpleParticleOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleParticleOptions_ = $SimpleParticleOptions$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$EntityHelper$PastPosition" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $EntityHelper$PastPosition {
 "position": $Vec3
 "time": integer

constructor(arg0: $Vec3$Type, arg1: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHelper$PastPosition$Type = ($EntityHelper$PastPosition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHelper$PastPosition_ = $EntityHelper$PastPosition$Type;
}}
declare module "packages/team/lodestar/lodestone/data/$LodestoneItemTagDatagen" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$TagsProvider$TagLookup, $TagsProvider$TagLookup$Type} from "packages/net/minecraft/data/tags/$TagsProvider$TagLookup"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ItemTagsProvider, $ItemTagsProvider$Type} from "packages/net/minecraft/data/tags/$ItemTagsProvider"

export class $LodestoneItemTagDatagen extends $ItemTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $CompletableFuture$Type<($TagsProvider$TagLookup$Type<($Block$Type)>)>, arg3: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemTagDatagen$Type = ($LodestoneItemTagDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemTagDatagen_ = $LodestoneItemTagDatagen$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODBuilder" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $LODBuilder<T> {

 "create"(arg0: T, arg1: $ResourceLocation$Type): void

(arg0: T, arg1: $ResourceLocation$Type): void
}

export namespace $LODBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LODBuilder$Type<T> = ($LODBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LODBuilder_<T> = $LODBuilder$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MultiBlockItem extends $BlockItem {
readonly "structure": $Supplier<(any)>
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type, arg1: $Item$Properties$Type, arg2: $Supplier$Type<(any)>)

public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiBlockItem$Type = ($MultiBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiBlockItem_ = $MultiBlockItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneDirectionalBlock" {
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$DirectionalBlock, $DirectionalBlock$Type} from "packages/net/minecraft/world/level/block/$DirectionalBlock"

export class $LodestoneDirectionalBlock extends $DirectionalBlock {
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneDirectionalBlock$Type = ($LodestoneDirectionalBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneDirectionalBlock_ = $LodestoneDirectionalBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$ChancePlacementFilter" {
import {$PlacementFilter, $PlacementFilter$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementFilter"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $ChancePlacementFilter extends $PlacementFilter {
static readonly "CODEC": $Codec<($ChancePlacementFilter)>

constructor(arg0: float)

public "type"(): $PlacementModifierType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChancePlacementFilter$Type = ($ChancePlacementFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChancePlacementFilter_ = $ChancePlacementFilter$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/entity/$LodestoneBoatEntity" {
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Boat$Status, $Boat$Status$Type} from "packages/net/minecraft/world/entity/vehicle/$Boat$Status"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Boat, $Boat$Type} from "packages/net/minecraft/world/entity/vehicle/$Boat"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LodestoneBoatEntity extends $Boat {
static readonly "PADDLE_LEFT": integer
static readonly "PADDLE_RIGHT": integer
static readonly "PADDLE_SOUND_TIME": double
static readonly "BUBBLE_TIME": integer
 "status": $Boat$Status
 "lastYd": double
static readonly "ID_TAG": string
static readonly "PASSENGERS_TAG": string
static readonly "BOARDING_COOLDOWN": integer
static readonly "TOTAL_AIR_SUPPLY": integer
static readonly "MAX_ENTITY_TAG_COUNT": integer
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2": float
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5": double
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0": double
static readonly "BREATHING_DISTANCE_BELOW_EYES": float
static readonly "BASE_TICKS_REQUIRED_TO_FREEZE": integer
static readonly "FREEZE_HURT_FREQUENCY": integer
static readonly "UUID_TAG": string
 "blocksBuilding": boolean
 "level": $Level
 "xo": double
 "yo": double
 "zo": double
 "blockPosition": $BlockPos
 "yRot": float
 "xRot": float
 "yRotO": float
 "xRotO": float
 "horizontalCollision": boolean
 "verticalCollision": boolean
 "verticalCollisionBelow": boolean
 "minorHorizontalCollision": boolean
 "hurtMarked": boolean
 "removalReason": $Entity$RemovalReason
static readonly "DEFAULT_BB_WIDTH": float
static readonly "DEFAULT_BB_HEIGHT": float
 "walkDistO": float
 "walkDist": float
 "moveDist": float
 "flyDist": float
 "fallDistance": float
 "xOld": double
 "yOld": double
 "zOld": double
 "noPhysics": boolean
 "age": integer
 "wasTouchingWater": boolean
 "wasEyeInWater": boolean
 "invulnerableTime": integer
 "levelCallback": $EntityInLevelCallback
 "noCulling": boolean
 "hasImpulse": boolean
 "portalCooldown": integer
 "isInsidePortal": boolean
 "dimensions": $EntityDimensions
 "eyeHeight": float
 "isInPowderSnow": boolean
 "wasInPowderSnow": boolean
 "wasOnFire": boolean
 "mainSupportingBlockPos": $Optional<($BlockPos)>

/**
 * 
 * @deprecated
 */
constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type, arg2: $RegistryObject$Type<($Item$Type)>, arg3: $RegistryObject$Type<($Item$Type)>)
constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type, arg2: $RegistryObject$Type<($Item$Type)>)

public "getDropItem"(): $Item
public "getAddEntityPacket"(): $Packet<($ClientGamePacketListener)>
get "dropItem"(): $Item
get "addEntityPacket"(): $Packet<($ClientGamePacketListener)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBoatEntity$Type = ($LodestoneBoatEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBoatEntity_ = $LodestoneBoatEntity$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ClientboundBlockEntityDataPacket, $ClientboundBlockEntityDataPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundBlockEntityDataPacket"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LodestoneBlockEntity extends $BlockEntity {
 "needsSync": boolean
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "init"(): void
public "tick"(): void
public "onUse"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "onClone"(arg0: $BlockState$Type, arg1: $HitResult$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: $Player$Type): $ItemStack
public "onPlace"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): void
public "getUpdatePacket"(): $ClientboundBlockEntityDataPacket
public "load"(arg0: $CompoundTag$Type): void
public "onNeighborUpdate"(arg0: $BlockState$Type, arg1: $BlockPos$Type, arg2: $BlockPos$Type): void
public "onEntityInside"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): void
public "onBreak"(arg0: $Player$Type): void
public "getUpdateTag"(): $CompoundTag
public "onDataPacket"(arg0: $Connection$Type, arg1: $ClientboundBlockEntityDataPacket$Type): void
public "handleUpdateTag"(arg0: $CompoundTag$Type): void
get "updatePacket"(): $ClientboundBlockEntityDataPacket
get "updateTag"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockEntity$Type = ($LodestoneBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockEntity_ = $LodestoneBlockEntity$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$GhostBlockHandler" {
import {$GhostBlockRenderer, $GhostBlockRenderer$Type} from "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GhostBlockOptions, $GhostBlockOptions$Type} from "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockOptions"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$GhostBlockHandler$GhostBlockEntry, $GhostBlockHandler$GhostBlockEntry$Type} from "packages/team/lodestar/lodestone/handlers/$GhostBlockHandler$GhostBlockEntry"

export class $GhostBlockHandler {
static readonly "GHOSTS": $Map<(any), ($GhostBlockHandler$GhostBlockEntry)>

constructor()

public static "addGhost"(arg0: any, arg1: $GhostBlockRenderer$Type, arg2: $GhostBlockOptions$Type, arg3: integer): $GhostBlockHandler$GhostBlockEntry
public static "renderGhosts"(arg0: $PoseStack$Type): void
public static "tickGhosts"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostBlockHandler$Type = ($GhostBlockHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostBlockHandler_ = $GhostBlockHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntityInventory" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStackHandler, $ItemStackHandler$Type} from "packages/net/minecraftforge/items/$ItemStackHandler"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LodestoneBlockEntityInventory extends $ItemStackHandler {
readonly "slotCount": integer
readonly "allowedItemSize": integer
 "inputPredicate": $Predicate<($ItemStack)>
 "outputPredicate": $Predicate<($ItemStack)>
readonly "inventoryOptional": $LazyOptional<($IItemHandler)>
 "nonEmptyItemStacks": $ArrayList<($ItemStack)>
 "emptyItemAmount": integer
 "nonEmptyItemAmount": integer
 "firstEmptyItemIndex": integer

constructor(arg0: integer, arg1: integer)
constructor(arg0: integer, arg1: integer, arg2: $Predicate$Type<($ItemStack$Type)>)
constructor(arg0: integer, arg1: integer, arg2: $Predicate$Type<($ItemStack$Type)>, arg3: $Predicate$Type<($ItemStack$Type)>)

public "load"(arg0: $CompoundTag$Type, arg1: string): void
public "load"(arg0: $CompoundTag$Type): void
public "clear"(): void
public "isEmpty"(): boolean
public "save"(arg0: $CompoundTag$Type): void
public "save"(arg0: $CompoundTag$Type, arg1: string): void
public "onContentsChanged"(arg0: integer): void
public "getSlots"(): integer
public "interact"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $ItemStack
public "insertItem"(arg0: $ItemStack$Type, arg1: boolean): $ItemStack
public "insertItem"(arg0: $Player$Type, arg1: $ItemStack$Type): $ItemStack
public "insertItem"(arg0: $ItemStack$Type): $ItemStack
public "getSlotLimit"(arg0: integer): integer
public "extractItem"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: integer): void
public "extractItem"(arg0: $Level$Type, arg1: $ItemStack$Type, arg2: $Player$Type): $ItemStack
public "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
public "isItemValid"(arg0: integer, arg1: $ItemStack$Type): boolean
public "getStacks"(): $NonNullList<($ItemStack)>
public "updateData"(): void
public "dumpItems"(arg0: $Level$Type, arg1: $Vec3$Type): void
public "dumpItems"(arg0: $Level$Type, arg1: $BlockPos$Type): void
get "empty"(): boolean
get "slots"(): integer
get "stacks"(): $NonNullList<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockEntityInventory$Type = ($LodestoneBlockEntityInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockEntityInventory_ = $LodestoneBlockEntityInventory$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/easing/$Easing$Back" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$Easing$Elastic, $Easing$Elastic$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing$Elastic"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"

export class $Easing$Back extends $Easing {
static readonly "DEFAULT_OVERSHOOT": float
static readonly "EASINGS": $HashMap<(string), ($Easing)>
readonly "name": string
static readonly "LINEAR": $Easing
static readonly "QUAD_IN": $Easing
static readonly "QUAD_OUT": $Easing
static readonly "QUAD_IN_OUT": $Easing
static readonly "CUBIC_IN": $Easing
static readonly "CUBIC_OUT": $Easing
static readonly "CUBIC_IN_OUT": $Easing
static readonly "QUARTIC_IN": $Easing
static readonly "QUARTIC_OUT": $Easing
static readonly "QUARTIC_IN_OUT": $Easing
static readonly "QUINTIC_IN": $Easing
static readonly "QUINTIC_OUT": $Easing
static readonly "QUINTIC_IN_OUT": $Easing
static readonly "SINE_IN": $Easing
static readonly "SINE_OUT": $Easing
static readonly "SINE_IN_OUT": $Easing
static readonly "EXPO_IN": $Easing
static readonly "EXPO_OUT": $Easing
static readonly "EXPO_IN_OUT": $Easing
static readonly "CIRC_IN": $Easing
static readonly "CIRC_OUT": $Easing
static readonly "CIRC_IN_OUT": $Easing
static readonly "ELASTIC_IN": $Easing$Elastic
static readonly "ELASTIC_OUT": $Easing$Elastic
static readonly "ELASTIC_IN_OUT": $Easing$Elastic
static readonly "BACK_IN": $Easing$Back
static readonly "BACK_OUT": $Easing$Back
static readonly "BACK_IN_OUT": $Easing$Back
static readonly "BOUNCE_IN": $Easing
static readonly "BOUNCE_OUT": $Easing
static readonly "BOUNCE_IN_OUT": $Easing

constructor(arg0: string)
constructor(arg0: string, arg1: float)

public "setOvershoot"(arg0: float): void
public "getOvershoot"(): float
set "overshoot"(value: float)
get "overshoot"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Easing$Back$Type = ($Easing$Back);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Easing$Back_ = $Easing$Back$Type;
}}
declare module "packages/team/lodestar/lodestone/events/$ClientSetupEvents" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$RegisterParticleProvidersEvent, $RegisterParticleProvidersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterParticleProvidersEvent"

export class $ClientSetupEvents {

constructor()

public static "clientSetup"(arg0: $FMLClientSetupEvent$Type): void
public static "registerParticleFactory"(arg0: $RegisterParticleProvidersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientSetupEvents$Type = ($ClientSetupEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientSetupEvents_ = $ClientSetupEvents$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/postprocess/$LodestoneGlslPreprocessor" {
import {$GlslPreprocessor, $GlslPreprocessor$Type} from "packages/com/mojang/blaze3d/preprocessor/$GlslPreprocessor"

export class $LodestoneGlslPreprocessor extends $GlslPreprocessor {
static readonly "PREPROCESSOR": $LodestoneGlslPreprocessor

constructor()

public "applyImport"(arg0: boolean, arg1: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneGlslPreprocessor$Type = ($LodestoneGlslPreprocessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneGlslPreprocessor_ = $LodestoneGlslPreprocessor$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockComponentEntity" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"

export class $MultiBlockComponentEntity extends $LodestoneBlockEntity {
 "corePos": $BlockPos
 "needsSync": boolean
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)
constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "onUse"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
public "load"(arg0: $CompoundTag$Type): void
public "onBreak"(arg0: $Player$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiBlockComponentEntity$Type = ($MultiBlockComponentEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiBlockComponentEntity_ = $MultiBlockComponentEntity$Type;
}}
declare module "packages/team/lodestar/lodestone/events/$ClientRuntimeEvents" {
import {$TickEvent$RenderTickEvent, $TickEvent$RenderTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$RenderTickEvent"
import {$ViewportEvent$RenderFog, $ViewportEvent$RenderFog$Type} from "packages/net/minecraftforge/client/event/$ViewportEvent$RenderFog"
import {$RenderLevelStageEvent, $RenderLevelStageEvent$Type} from "packages/net/minecraftforge/client/event/$RenderLevelStageEvent"
import {$ViewportEvent$ComputeFogColor, $ViewportEvent$ComputeFogColor$Type} from "packages/net/minecraftforge/client/event/$ViewportEvent$ComputeFogColor"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $ClientRuntimeEvents {

constructor()

public static "renderTick"(arg0: $TickEvent$RenderTickEvent$Type): void
public static "clientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
public static "fogColors"(arg0: $ViewportEvent$ComputeFogColor$Type): void
public static "renderFog"(arg0: $ViewportEvent$RenderFog$Type): void
public static "renderStages"(arg0: $RenderLevelStageEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRuntimeEvents$Type = ($ClientRuntimeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRuntimeEvents_ = $ClientRuntimeEvents$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/sign/$LodestoneWallSignBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$WoodType, $WoodType$Type} from "packages/net/minecraft/world/level/block/state/properties/$WoodType"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$WallSignBlock, $WallSignBlock$Type} from "packages/net/minecraft/world/level/block/$WallSignBlock"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $LodestoneWallSignBlock extends $WallSignBlock implements $EntityBlock {
static readonly "FACING": $DirectionProperty
static readonly "WATERLOGGED": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type, arg1: $WoodType$Type)

public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWallSignBlock$Type = ($LodestoneWallSignBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWallSignBlock_ = $LodestoneWallSignBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/command/arguments/$WorldEventInstanceArgument" {
import {$Suggestions, $Suggestions$Type} from "packages/com/mojang/brigadier/suggestion/$Suggestions"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SuggestionsBuilder, $SuggestionsBuilder$Type} from "packages/com/mojang/brigadier/suggestion/$SuggestionsBuilder"
import {$ArgumentType, $ArgumentType$Type} from "packages/com/mojang/brigadier/arguments/$ArgumentType"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $WorldEventInstanceArgument implements $ArgumentType<($WorldEventInstance)> {


public static "worldEventInstance"(): $WorldEventInstanceArgument
public "listSuggestions"<S>(arg0: $CommandContext$Type<(S)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
public static "getEventInstance"(arg0: $CommandContext$Type<(any)>, arg1: string): $WorldEventInstance
public "getExamples"(): $Collection<(string)>
get "examples"(): $Collection<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventInstanceArgument$Type = ($WorldEventInstanceArgument);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventInstanceArgument_ = $WorldEventInstanceArgument$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleDataBuilder" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $ColorParticleDataBuilder {


public "build"(): $ColorParticleData
public "setEasing"(arg0: $Easing$Type): $ColorParticleDataBuilder
public "setCoefficient"(arg0: float): $ColorParticleDataBuilder
set "easing"(value: $Easing$Type)
set "coefficient"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorParticleDataBuilder$Type = ($ColorParticleDataBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorParticleDataBuilder_ = $ColorParticleDataBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/easing/$Easing" {
import {$Easing$Back, $Easing$Back$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing$Back"
import {$Easing$Elastic, $Easing$Elastic$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing$Elastic"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"

export class $Easing {
static readonly "EASINGS": $HashMap<(string), ($Easing)>
readonly "name": string
static readonly "LINEAR": $Easing
static readonly "QUAD_IN": $Easing
static readonly "QUAD_OUT": $Easing
static readonly "QUAD_IN_OUT": $Easing
static readonly "CUBIC_IN": $Easing
static readonly "CUBIC_OUT": $Easing
static readonly "CUBIC_IN_OUT": $Easing
static readonly "QUARTIC_IN": $Easing
static readonly "QUARTIC_OUT": $Easing
static readonly "QUARTIC_IN_OUT": $Easing
static readonly "QUINTIC_IN": $Easing
static readonly "QUINTIC_OUT": $Easing
static readonly "QUINTIC_IN_OUT": $Easing
static readonly "SINE_IN": $Easing
static readonly "SINE_OUT": $Easing
static readonly "SINE_IN_OUT": $Easing
static readonly "EXPO_IN": $Easing
static readonly "EXPO_OUT": $Easing
static readonly "EXPO_IN_OUT": $Easing
static readonly "CIRC_IN": $Easing
static readonly "CIRC_OUT": $Easing
static readonly "CIRC_IN_OUT": $Easing
static readonly "ELASTIC_IN": $Easing$Elastic
static readonly "ELASTIC_OUT": $Easing$Elastic
static readonly "ELASTIC_IN_OUT": $Easing$Elastic
static readonly "BACK_IN": $Easing$Back
static readonly "BACK_OUT": $Easing$Back
static readonly "BACK_IN_OUT": $Easing$Back
static readonly "BOUNCE_IN": $Easing
static readonly "BOUNCE_OUT": $Easing
static readonly "BOUNCE_IN_OUT": $Easing

constructor(arg0: string)

public static "valueOf"(arg0: string): $Easing
public "ease"(arg0: float, arg1: float, arg2: float, arg3: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Easing$Type = ($Easing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Easing_ = $Easing$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$FrameSetScreenParticle" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ParticleEngine$MutableSpriteSet, $ParticleEngine$MutableSpriteSet$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$MutableSpriteSet"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$GenericScreenParticle, $GenericScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$GenericScreenParticle"

export class $FrameSetScreenParticle extends $GenericScreenParticle {
 "frameSet": $ArrayList<(integer)>
readonly "level": $ClientLevel
 "xOld": double
 "yOld": double
 "x": double
 "y": double
 "xMotion": double
 "yMotion": double
 "xMoved": double
 "yMoved": double
 "removed": boolean
readonly "random": $RandomSource
 "age": integer
 "lifetime": integer
 "gravity": float
 "size": float
 "rCol": float
 "gCol": float
 "bCol": float
 "alpha": float
 "roll": float
 "oRoll": float
 "friction": float

constructor(arg0: $ClientLevel$Type, arg1: $ScreenParticleOptions$Type, arg2: $ParticleEngine$MutableSpriteSet$Type, arg3: double, arg4: double, arg5: double, arg6: double)

public "tick"(): void
public "setSprite"(arg0: integer): void
set "sprite"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrameSetScreenParticle$Type = ($FrameSetScreenParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrameSetScreenParticle_ = $FrameSetScreenParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$BlockStateSmith" {
import {$ItemModelSmith, $ItemModelSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$ItemModelSmith"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockStateSmith$SmithStateSupplier, $BlockStateSmith$SmithStateSupplier$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$BlockStateSmith$SmithStateSupplier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$AbstractBlockStateSmith$StateSmithData, $AbstractBlockStateSmith$StateSmithData$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith$StateSmithData"
import {$AbstractBlockStateSmith, $AbstractBlockStateSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith"

export class $BlockStateSmith<T extends $Block> extends $AbstractBlockStateSmith<(T)> {
readonly "stateSupplier": $BlockStateSmith$SmithStateSupplier<(T)>
readonly "itemModelSmith": $ItemModelSmith
readonly "blockClass": $Class<(T)>

constructor(arg0: $Class$Type<(T)>, arg1: $BlockStateSmith$SmithStateSupplier$Type<(T)>)
constructor(arg0: $Class$Type<(T)>, arg1: $ItemModelSmith$Type, arg2: $BlockStateSmith$SmithStateSupplier$Type<(T)>)

public "act"(arg0: $AbstractBlockStateSmith$StateSmithData$Type, arg1: $Collection$Type<($Supplier$Type<(any)>)>): void
public "act"(arg0: $AbstractBlockStateSmith$StateSmithData$Type, ...arg1: ($Supplier$Type<(any)>)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateSmith$Type<T> = ($BlockStateSmith<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateSmith_<T> = $BlockStateSmith$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/$LodestoneDatagenBlockData" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LodestoneDatagenBlockData {
static readonly "EMPTY": $LodestoneDatagenBlockData
 "hasInheritedLootTable": boolean

constructor()

public "needsIron"(): $LodestoneDatagenBlockData
public "needsAxe"(): $LodestoneDatagenBlockData
public "needsHoe"(): $LodestoneDatagenBlockData
public "needsDiamond"(): $LodestoneDatagenBlockData
public "needsPickaxe"(): $LodestoneDatagenBlockData
public "needsShovel"(): $LodestoneDatagenBlockData
public "needsStone"(): $LodestoneDatagenBlockData
public "hasInheritedLoot"(): $LodestoneDatagenBlockData
public "addTag"(arg0: $TagKey$Type<($Block$Type)>): $LodestoneDatagenBlockData
public "getTags"(): $List<($TagKey<($Block)>)>
public "addTags"(...arg0: ($TagKey$Type<($Block$Type)>)[]): $LodestoneDatagenBlockData
get "tags"(): $List<($TagKey<($Block)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneDatagenBlockData$Type = ($LodestoneDatagenBlockData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneDatagenBlockData_ = $LodestoneDatagenBlockData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$SimpleParticleOptions, $SimpleParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $WorldParticleOptions extends $SimpleParticleOptions implements $ParticleOptions {
readonly "type": $ParticleType<(any)>
 "behavior": $LodestoneParticleBehavior
 "behaviorComponent": $LodestoneBehaviorComponent
 "renderType": $ParticleRenderType
 "renderLayer": $RenderHandler$LodestoneRenderLayer
 "shouldCull": boolean
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "spawnActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "noClip": boolean
static readonly "DEFAULT_COLOR": $ColorParticleData
static readonly "DEFAULT_SPIN": $SpinParticleData
static readonly "DEFAULT_GENERIC": $GenericParticleData
 "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
 "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
 "colorData": $ColorParticleData
 "transparencyData": $GenericParticleData
 "scaleData": $GenericParticleData
 "spinData": $SpinParticleData
 "lifetimeSupplier": $Supplier<(integer)>
 "lifeDelaySupplier": $Supplier<(integer)>
 "gravityStrengthSupplier": $Supplier<(float)>
 "frictionStrengthSupplier": $Supplier<(float)>

constructor(arg0: $ParticleType$Type<(any)>)
constructor(arg0: $RegistryObject$Type<(any)>)

public "setBehaviorIfDefault"(arg0: $LodestoneBehaviorComponent$Type): $WorldParticleOptions
public "getType"(): $ParticleType<(any)>
public "writeToString"(): string
public "writeToNetwork"(arg0: $FriendlyByteBuf$Type): void
public "setBehavior"(arg0: $LodestoneBehaviorComponent$Type): $WorldParticleOptions
set "behaviorIfDefault"(value: $LodestoneBehaviorComponent$Type)
get "type"(): $ParticleType<(any)>
set "behavior"(value: $LodestoneBehaviorComponent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldParticleOptions$Type = ($WorldParticleOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldParticleOptions_ = $WorldParticleOptions$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$LodestoneScreenParticleType" {
import {$ScreenParticleType, $ScreenParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$ScreenParticleType$ParticleProvider, $ScreenParticleType$ParticleProvider$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType$ParticleProvider"

export class $LodestoneScreenParticleType extends $ScreenParticleType<($ScreenParticleOptions)> {
 "provider": $ScreenParticleType$ParticleProvider<(T)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneScreenParticleType$Type = ($LodestoneScreenParticleType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneScreenParticleType_ = $LodestoneScreenParticleType$Type;
}}
declare module "packages/team/lodestar/lodestone/capability/$LodestoneWorldDataCapability" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LodestoneCapability, $LodestoneCapability$Type} from "packages/team/lodestar/lodestone/systems/capability/$LodestoneCapability"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $LodestoneWorldDataCapability implements $LodestoneCapability {
static "CAPABILITY": $Capability<($LodestoneWorldDataCapability)>
readonly "activeWorldEvents": $ArrayList<($WorldEventInstance)>
readonly "inboundWorldEvents": $ArrayList<($WorldEventInstance)>

constructor()

public static "getCapabilityOptional"(arg0: $Level$Type): $LazyOptional<($LodestoneWorldDataCapability)>
public static "attachWorldCapability"(arg0: $AttachCapabilitiesEvent$Type<($Level$Type)>): void
public static "getCapability"(arg0: $Level$Type): $LodestoneWorldDataCapability
public static "registerCapabilities"(arg0: $RegisterCapabilitiesEvent$Type): void
public "deserializeNBT"(arg0: $CompoundTag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWorldDataCapability$Type = ($LodestoneWorldDataCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWorldDataCapability_ = $LodestoneWorldDataCapability$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockCoreEntity" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$IMultiBlockCore, $IMultiBlockCore$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$IMultiBlockCore"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"
import {$MultiBlockStructure, $MultiBlockStructure$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure"

export class $MultiBlockCoreEntity extends $LodestoneBlockEntity implements $IMultiBlockCore {
readonly "structure": $MultiBlockStructure
 "needsSync": boolean
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $MultiBlockStructure$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type)

public "getStructure"(): $MultiBlockStructure
public "onBreak"(arg0: $Player$Type): void
public "getComponentPositions"(): $ArrayList<($BlockPos)>
public "isModular"(): boolean
public "destroyMultiblock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
public "setupMultiblock"(arg0: $BlockPos$Type): void
get "structure"(): $MultiBlockStructure
get "componentPositions"(): $ArrayList<($BlockPos)>
get "modular"(): boolean
set "upMultiblock"(value: $BlockPos$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiBlockCoreEntity$Type = ($MultiBlockCoreEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiBlockCoreEntity_ = $MultiBlockCoreEntity$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LodestoneBlockFiller$BlockStateEntryBuilder, $LodestoneBlockFiller$BlockStateEntryBuilder$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$BlockStateEntryBuilder"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$LodestoneBlockFiller$LodestoneLayerToken, $LodestoneBlockFiller$LodestoneLayerToken$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$LodestoneLayerToken"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LodestoneBlockFiller$LodestoneBlockFillerLayer, $LodestoneBlockFiller$LodestoneBlockFillerLayer$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$LodestoneBlockFillerLayer"

export class $LodestoneBlockFiller extends $ArrayList<($LodestoneBlockFiller$LodestoneBlockFillerLayer)> {
static readonly "MAIN": $LodestoneBlockFiller$LodestoneLayerToken

constructor(arg0: $Collection$Type<($LodestoneBlockFiller$LodestoneBlockFillerLayer$Type)>)
constructor(...arg0: ($LodestoneBlockFiller$LodestoneBlockFillerLayer$Type)[])
constructor()

public "fill"(arg0: $LevelAccessor$Type): $LodestoneBlockFiller$LodestoneBlockFillerLayer
public "getLayer"(arg0: $LodestoneBlockFiller$LodestoneLayerToken$Type): $LodestoneBlockFiller$LodestoneBlockFillerLayer
public static "create"(arg0: $BlockState$Type): $LodestoneBlockFiller$BlockStateEntryBuilder
public "addLayers"(...arg0: ($LodestoneBlockFiller$LodestoneBlockFillerLayer$Type)[]): $LodestoneBlockFiller
public "addLayers"(...arg0: ($LodestoneBlockFiller$LodestoneLayerToken$Type)[]): $LodestoneBlockFiller
public "getMainLayer"(): $LodestoneBlockFiller$LodestoneBlockFillerLayer
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
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "mainLayer"(): $LodestoneBlockFiller$LodestoneBlockFillerLayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockFiller$Type = ($LodestoneBlockFiller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller_ = $LodestoneBlockFiller$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/screenshake/$PositionedScreenshakeInstance" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$ScreenshakeInstance, $ScreenshakeInstance$Type} from "packages/team/lodestar/lodestone/systems/screenshake/$ScreenshakeInstance"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $PositionedScreenshakeInstance extends $ScreenshakeInstance {
readonly "position": $Vec3
readonly "falloffDistance": float
readonly "maxDistance": float
readonly "falloffEasing": $Easing
 "progress": integer
readonly "duration": integer
 "intensity1": float
 "intensity2": float
 "intensity3": float
 "intensityCurveStartEasing": $Easing
 "intensityCurveEndEasing": $Easing

constructor(arg0: integer, arg1: $Vec3$Type, arg2: float, arg3: float, arg4: $Easing$Type)
constructor(arg0: integer, arg1: $Vec3$Type, arg2: float, arg3: float)

public "updateIntensity"(arg0: $Camera$Type, arg1: $RandomSource$Type): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PositionedScreenshakeInstance$Type = ($PositionedScreenshakeInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PositionedScreenshakeInstance_ = $PositionedScreenshakeInstance$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$RenderHandler" {
import {$ViewportEvent$RenderFog, $ViewportEvent$RenderFog$Type} from "packages/net/minecraftforge/client/event/$ViewportEvent$RenderFog"
import {$FogShape, $FogShape$Type} from "packages/com/mojang/blaze3d/shaders/$FogShape"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$RenderTarget, $RenderTarget$Type} from "packages/com/mojang/blaze3d/pipeline/$RenderTarget"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$ShaderUniformHandler, $ShaderUniformHandler$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$ShaderUniformHandler"
import {$ViewportEvent$ComputeFogColor, $ViewportEvent$ComputeFogColor$Type} from "packages/net/minecraftforge/client/event/$ViewportEvent$ComputeFogColor"

export class $RenderHandler {
static readonly "BUFFERS": $HashMap<($RenderType), ($BufferBuilder)>
static readonly "PARTICLE_BUFFERS": $HashMap<($RenderType), ($BufferBuilder)>
static readonly "LATE_BUFFERS": $HashMap<($RenderType), ($BufferBuilder)>
static readonly "LATE_PARTICLE_BUFFERS": $HashMap<($RenderType), ($BufferBuilder)>
static readonly "UNIFORM_HANDLERS": $HashMap<($RenderType), ($ShaderUniformHandler)>
static readonly "TRANSPARENT_RENDER_TYPES": $Collection<($RenderType)>
static "LODESTONE_DEPTH_CACHE": $RenderTarget
static "DELAYED_RENDER": $RenderHandler$LodestoneRenderLayer
static "LATE_DELAYED_RENDER": $RenderHandler$LodestoneRenderLayer
static "MAIN_POSE_STACK": $PoseStack
static "MATRIX4F": $Matrix4f
static "FOG_NEAR": float
static "FOG_FAR": float
static "FOG_SHAPE": $FogShape
static "FOG_RED": float
static "FOG_GREEN": float
static "FOG_BLUE": float

constructor()

public static "resize"(arg0: integer, arg1: integer): void
public static "copyDepthBuffer"(arg0: $RenderTarget$Type): void
public static "addRenderType"(arg0: $RenderType$Type): void
public static "onClientSetup"(arg0: $FMLClientSetupEvent$Type): void
public static "renderBufferedParticles"(arg0: $RenderHandler$LodestoneRenderLayer$Type, arg1: boolean): void
public static "beginBufferedRendering"(): void
public static "renderBufferedBatches"(arg0: $RenderHandler$LodestoneRenderLayer$Type, arg1: boolean): void
public static "endBufferedRendering"(): void
public static "cacheFogData"(arg0: $ViewportEvent$RenderFog$Type): void
public static "cacheFogData"(arg0: $ViewportEvent$ComputeFogColor$Type): void
public static "endBatches"(arg0: $RenderHandler$LodestoneRenderLayer$Type): void
public static "endBatches"(): void
public static "endBatches"(arg0: $MultiBufferSource$BufferSource$Type, arg1: $Collection$Type<($RenderType$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHandler$Type = ($RenderHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHandler_ = $RenderHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/network/screenshake/$ScreenshakePacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $ScreenshakePacket extends $LodestoneClientPacket {
readonly "duration": integer
 "intensity1": float
 "intensity2": float
 "intensity3": float
 "intensityCurveStartEasing": $Easing
 "intensityCurveEndEasing": $Easing

constructor(arg0: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ScreenshakePacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "setIntensity"(arg0: float, arg1: float): $ScreenshakePacket
public "setIntensity"(arg0: float, arg1: float, arg2: float): $ScreenshakePacket
public "setIntensity"(arg0: float): $ScreenshakePacket
public "setEasing"(arg0: $Easing$Type): $ScreenshakePacket
public "setEasing"(arg0: $Easing$Type, arg1: $Easing$Type): $ScreenshakePacket
set "intensity"(value: float)
set "easing"(value: $Easing$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenshakePacket$Type = ($ScreenshakePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenshakePacket_ = $ScreenshakePacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$LodestoneBufferWrapper" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$RenderTypeProvider, $RenderTypeProvider$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeProvider"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneBufferWrapper implements $MultiBufferSource {
readonly "provider": $RenderTypeProvider
readonly "buffer": $MultiBufferSource

constructor(arg0: $RenderTypeProvider$Type, arg1: $MultiBufferSource$Type)

public "getBuffer"(arg0: $RenderType$Type): $VertexConsumer
public static "immediateWithBuffers"(layerBuffers: $Map$Type<(any), (any)>, fallbackBuffer: $BufferBuilder$Type): $MultiBufferSource$BufferSource
public static "immediate"(arg0: $BufferBuilder$Type): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBufferWrapper$Type = ($LodestoneBufferWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBufferWrapper_ = $LodestoneBufferWrapper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/lod/$LevelOfDetail" {
import {$ObjModel, $ObjModel$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$ObjModel"

export class $LevelOfDetail<T> {

constructor(arg0: $ObjModel$Type, arg1: T)

public "getModel"(): $ObjModel
public "getArgument"(): T
get "model"(): $ObjModel
get "argument"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LevelOfDetail$Type<T> = ($LevelOfDetail<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LevelOfDetail_<T> = $LevelOfDetail$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/base/$TextureSheetScreenParticle" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$QuadScreenParticle, $QuadScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/base/$QuadScreenParticle"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$SpriteSet, $SpriteSet$Type} from "packages/net/minecraft/client/particle/$SpriteSet"

export class $TextureSheetScreenParticle extends $QuadScreenParticle {
readonly "level": $ClientLevel
 "xOld": double
 "yOld": double
 "x": double
 "y": double
 "xMotion": double
 "yMotion": double
 "xMoved": double
 "yMoved": double
 "removed": boolean
readonly "random": $RandomSource
 "age": integer
 "lifetime": integer
 "gravity": float
 "size": float
 "rCol": float
 "gCol": float
 "bCol": float
 "alpha": float
 "roll": float
 "oRoll": float
 "friction": float


public "pickSprite"(arg0: $SpriteSet$Type): void
public "setSpriteFromAge"(arg0: $SpriteSet$Type): void
set "spriteFromAge"(value: $SpriteSet$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureSheetScreenParticle$Type = ($TextureSheetScreenParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureSheetScreenParticle_ = $TextureSheetScreenParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/tag/$LodestoneDamageTypeTags" {
import {$DamageType, $DamageType$Type} from "packages/net/minecraft/world/damagesource/$DamageType"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $LodestoneDamageTypeTags {
static readonly "IS_MAGIC": $TagKey<($DamageType)>

constructor()

public static "forgeTag"(arg0: string): $TagKey<($DamageType)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneDamageTypeTags$Type = ($LodestoneDamageTypeTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneDamageTypeTags_ = $LodestoneDamageTypeTags$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$ConfigValueHolder" {
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$LodestoneConfig$BuilderSupplier, $LodestoneConfig$BuilderSupplier$Type} from "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$BuilderSupplier"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $LodestoneConfig$ConfigValueHolder<T> {

constructor(arg0: string, arg1: string, arg2: $LodestoneConfig$BuilderSupplier$Type<(T)>)

public "getConfigValue"(): T
public "setConfigValue"(arg0: T): void
public "getConfig"(): $ForgeConfigSpec$ConfigValue<(T)>
public "setConfig"(arg0: $ForgeConfigSpec$Builder$Type): void
get "configValue"(): T
set "configValue"(value: T)
get "config"(): $ForgeConfigSpec$ConfigValue<(T)>
set "config"(value: $ForgeConfigSpec$Builder$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneConfig$ConfigValueHolder$Type<T> = ($LodestoneConfig$ConfigValueHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneConfig$ConfigValueHolder_<T> = $LodestoneConfig$ConfigValueHolder$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SimpleParticleOptions$ParticleSpritePicker extends $Enum<($SimpleParticleOptions$ParticleSpritePicker)> {
static readonly "FIRST_INDEX": $SimpleParticleOptions$ParticleSpritePicker
static readonly "LAST_INDEX": $SimpleParticleOptions$ParticleSpritePicker
static readonly "WITH_AGE": $SimpleParticleOptions$ParticleSpritePicker
static readonly "RANDOM_SPRITE": $SimpleParticleOptions$ParticleSpritePicker


public static "values"(): ($SimpleParticleOptions$ParticleSpritePicker)[]
public static "valueOf"(arg0: string): $SimpleParticleOptions$ParticleSpritePicker
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleParticleOptions$ParticleSpritePicker$Type = (("last_index") | ("with_age") | ("random_sprite") | ("first_index")) | ($SimpleParticleOptions$ParticleSpritePicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleParticleOptions$ParticleSpritePicker_ = $SimpleParticleOptions$ParticleSpritePicker$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneAttributeRegistry" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$EntityAttributeModificationEvent, $EntityAttributeModificationEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityAttributeModificationEvent"

export class $LodestoneAttributeRegistry {
static readonly "ATTRIBUTES": $DeferredRegister<($Attribute)>
static readonly "UUIDS": $HashMap<($RegistryObject<($Attribute)>), ($UUID)>
static readonly "MAGIC_RESISTANCE": $RegistryObject<($Attribute)>
static readonly "MAGIC_PROFICIENCY": $RegistryObject<($Attribute)>
static readonly "MAGIC_DAMAGE": $RegistryObject<($Attribute)>

constructor()

public static "registerAttribute"(arg0: $DeferredRegister$Type<($Attribute$Type)>, arg1: string, arg2: string, arg3: $Function$Type<(string), ($Attribute$Type)>): $RegistryObject<($Attribute)>
public static "modifyEntityAttributes"(arg0: $EntityAttributeModificationEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneAttributeRegistry$Type = ($LodestoneAttributeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneAttributeRegistry_ = $LodestoneAttributeRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/$RightClickEmptyServer" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"

export class $RightClickEmptyServer extends $PlayerEvent {

constructor()
constructor(arg0: $Player$Type)

public static "onRightClickEmptyServer"(arg0: $Player$Type): void
public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RightClickEmptyServer$Type = ($RightClickEmptyServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RightClickEmptyServer_ = $RightClickEmptyServer$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/network/$LodestoneServerNBTPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$LodestoneServerPacket, $LodestoneServerPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneServerPacket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LodestoneServerNBTPacket extends $LodestoneServerPacket {

constructor(arg0: $CompoundTag$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneServerNBTPacket$Type = ($LodestoneServerNBTPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneServerNBTPacket_ = $LodestoneServerNBTPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/screenshake/$ScreenshakeInstance" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $ScreenshakeInstance {
 "progress": integer
readonly "duration": integer
 "intensity1": float
 "intensity2": float
 "intensity3": float
 "intensityCurveStartEasing": $Easing
 "intensityCurveEndEasing": $Easing

constructor(arg0: integer)

public "updateIntensity"(arg0: $Camera$Type, arg1: $RandomSource$Type): float
public "setIntensity"(arg0: float, arg1: float, arg2: float): $ScreenshakeInstance
public "setIntensity"(arg0: float): $ScreenshakeInstance
public "setIntensity"(arg0: float, arg1: float): $ScreenshakeInstance
public "setEasing"(arg0: $Easing$Type, arg1: $Easing$Type): $ScreenshakeInstance
public "setEasing"(arg0: $Easing$Type): $ScreenshakeInstance
set "intensity"(value: float)
set "easing"(value: $Easing$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenshakeInstance$Type = ($ScreenshakeInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenshakeInstance_ = $ScreenshakeInstance$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneLogBlock" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$RotatedPillarBlock, $RotatedPillarBlock$Type} from "packages/net/minecraft/world/level/block/$RotatedPillarBlock"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$ToolAction, $ToolAction$Type} from "packages/net/minecraftforge/common/$ToolAction"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LodestoneLogBlock extends $RotatedPillarBlock {
readonly "stripped": $Supplier<($Block)>
static readonly "AXIS": $EnumProperty<($Direction$Axis)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type, arg1: $Supplier$Type<($Block$Type)>)

public "getToolModifiedState"(arg0: $BlockState$Type, arg1: $UseOnContext$Type, arg2: $ToolAction$Type, arg3: boolean): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneLogBlock$Type = ($LodestoneLogBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneLogBlock_ = $LodestoneLogBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/screenparticle/$ScreenParticleHandler" {
import {$TickEvent$RenderTickEvent, $TickEvent$RenderTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$RenderTickEvent"
import {$Tesselator, $Tesselator$Type} from "packages/com/mojang/blaze3d/vertex/$Tesselator"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ParticleEmitterHandler$ItemParticleSupplier, $ParticleEmitterHandler$ItemParticleSupplier$Type} from "packages/team/lodestar/lodestone/handlers/screenparticle/$ParticleEmitterHandler$ItemParticleSupplier"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ScreenParticleItemStackRetrievalKey, $ScreenParticleItemStackRetrievalKey$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleItemStackRetrievalKey"
import {$ScreenParticle, $ScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/base/$ScreenParticle"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ScreenParticleHolder, $ScreenParticleHolder$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleHolder"
import {$ScreenParticleItemStackKey, $ScreenParticleItemStackKey$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleItemStackKey"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ScreenParticleHandler {
static readonly "ITEM_PARTICLES": $Map<($ScreenParticleItemStackKey), ($ScreenParticleHolder)>
static readonly "ITEM_STACK_CACHE": $Map<($ScreenParticleItemStackRetrievalKey), ($ItemStack)>
static readonly "ACTIVELY_ACCESSED_KEYS": $Collection<($ScreenParticleItemStackRetrievalKey)>
static "cachedItemParticles": $ScreenParticleHolder
static "currentItemX": integer
static "currentItemY": integer
static readonly "TESSELATOR": $Tesselator
static "canSpawnParticles": boolean
static "renderingHotbar": boolean

constructor()

public static "renderTick"(arg0: $TickEvent$RenderTickEvent$Type): void
public static "addParticle"<T extends $ScreenParticleOptions>(arg0: $ScreenParticleHolder$Type, arg1: T, arg2: double, arg3: double, arg4: double, arg5: double): $ScreenParticle
public static "spawnAndPullParticles"(arg0: $ClientLevel$Type, arg1: $ParticleEmitterHandler$ItemParticleSupplier$Type, arg2: $ItemStack$Type, arg3: boolean): $ScreenParticleHolder
public static "pullFromParticleVault"(arg0: $ScreenParticleItemStackRetrievalKey$Type, arg1: $ItemStack$Type, arg2: $ScreenParticleHolder$Type, arg3: boolean): void
public static "renderItemStackEarly"(arg0: $PoseStack$Type, arg1: $ItemStack$Type, arg2: integer, arg3: integer): void
public static "renderItemStackLate"(): void
public static "renderParticles"(arg0: $ScreenParticleHolder$Type): void
public static "clearParticles"(): void
public static "clearParticles"(arg0: $ScreenParticleHolder$Type): void
public static "tickParticles"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleHandler$Type = ($ScreenParticleHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleHandler_ = $ScreenParticleHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleDataBuilder" {
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$GenericParticleDataBuilder, $GenericParticleDataBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleDataBuilder"

export class $SpinParticleDataBuilder extends $GenericParticleDataBuilder {


public "build"(): $SpinParticleData
public "setSpinOffset"(arg0: float): $SpinParticleDataBuilder
public "randomSpinOffset"(arg0: $RandomSource$Type): $SpinParticleDataBuilder
public "setCoefficient"(arg0: float): $SpinParticleDataBuilder
set "spinOffset"(value: float)
set "coefficient"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpinParticleDataBuilder$Type = ($SpinParticleDataBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpinParticleDataBuilder_ = $SpinParticleDataBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/placementassistance/$IPlacementAssistant" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IPlacementAssistant {

 "onObserveBlock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $BlockHitResult$Type, arg3: $BlockState$Type, arg4: $ItemStack$Type): void
 "canAssist"(): $Predicate<($ItemStack)>
 "onPlaceBlock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $BlockHitResult$Type, arg3: $BlockState$Type, arg4: $ItemStack$Type): void
}

export namespace $IPlacementAssistant {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlacementAssistant$Type = ($IPlacementAssistant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlacementAssistant_ = $IPlacementAssistant$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType" {
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$ScreenParticleType$ParticleProvider, $ScreenParticleType$ParticleProvider$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType$ParticleProvider"

export class $ScreenParticleType<T extends $ScreenParticleOptions> {
 "provider": $ScreenParticleType$ParticleProvider<(T)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleType$Type<T> = ($ScreenParticleType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleType_<T> = $ScreenParticleType$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/base/$ScreenParticle" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$LodestoneScreenParticleRenderType, $LodestoneScreenParticleRenderType$Type} from "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneScreenParticleRenderType"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"

export class $ScreenParticle {
readonly "level": $ClientLevel
 "xOld": double
 "yOld": double
 "x": double
 "y": double
 "xMotion": double
 "yMotion": double
 "xMoved": double
 "yMoved": double
 "removed": boolean
readonly "random": $RandomSource
 "age": integer
 "lifetime": integer
 "gravity": float
 "size": float
 "rCol": float
 "gCol": float
 "bCol": float
 "alpha": float
 "roll": float
 "oRoll": float
 "friction": float

constructor(arg0: $ClientLevel$Type, arg1: double, arg2: double, arg3: double, arg4: double)

public "remove"(): void
public "isAlive"(): boolean
public "tick"(): void
public "setColor"(arg0: float, arg1: float, arg2: float): void
public "setSize"(arg0: float): $ScreenParticle
public "render"(arg0: $BufferBuilder$Type): void
public "getLifetime"(): integer
public "getRenderType"(): $LodestoneScreenParticleRenderType
public "setLifetime"(arg0: integer): void
public "setParticleSpeed"(arg0: double, arg1: double): void
get "alive"(): boolean
set "size"(value: float)
get "lifetime"(): integer
get "renderType"(): $LodestoneScreenParticleRenderType
set "lifetime"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticle$Type = ($ScreenParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticle_ = $ScreenParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$GenericParticleDataBuilder, $GenericParticleDataBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleDataBuilder"

export class $GenericParticleData {
readonly "startingValue": float
readonly "middleValue": float
readonly "endingValue": float
readonly "coefficient": float
readonly "startToMiddleEasing": $Easing
readonly "middleToEndEasing": $Easing
 "valueMultiplier": float
 "coefficientMultiplier": float


public "getValue"(arg0: float, arg1: float): float
public "copy"(): $GenericParticleData
public static "create"(arg0: float): $GenericParticleDataBuilder
public static "create"(arg0: float, arg1: float): $GenericParticleDataBuilder
public static "create"(arg0: float, arg1: float, arg2: float): $GenericParticleDataBuilder
public static "constrictTransparency"(arg0: $GenericParticleData$Type): $GenericParticleData
public "getProgress"(arg0: float, arg1: float): float
public "overrideCoefficientMultiplier"(arg0: float): $GenericParticleData
public "overrideValueMultiplier"(arg0: float): $GenericParticleData
public "multiplyCoefficient"(arg0: float): $GenericParticleData
public "isTrinary"(): boolean
public "multiplyValue"(arg0: float): $GenericParticleData
public "bake"(): $GenericParticleData
get "trinary"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericParticleData$Type = ($GenericParticleData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericParticleData_ = $GenericParticleData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectType" {
import {$FireEffectInstance, $FireEffectInstance$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectInstance"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $FireEffectType {
readonly "id": string

constructor(arg0: string, arg1: integer, arg2: integer)

public "tick"(arg0: $FireEffectInstance$Type, arg1: $Entity$Type): void
public "isValid"(arg0: $FireEffectInstance$Type): boolean
public "extinguish"(arg0: $FireEffectInstance$Type, arg1: $Entity$Type): void
public "getDamage"(arg0: $FireEffectInstance$Type): integer
public "getTickInterval"(arg0: $FireEffectInstance$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireEffectType$Type = ($FireEffectType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireEffectType_ = $FireEffectType$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$ColorHelper" {
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ColorHelper {

constructor()

public static "getColor"(arg0: integer, arg1: integer, arg2: integer): integer
public static "getColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public static "getColor"(arg0: float, arg1: float, arg2: float, arg3: float): integer
public static "getColor"(arg0: integer): $Color
public static "getColor"(arg0: $Color$Type): integer
public static "brighter"(arg0: $Color$Type, arg1: integer): $Color
public static "brighter"(arg0: $Color$Type, arg1: integer, arg2: float): $Color
public static "darker"(arg0: $Color$Type, arg1: integer, arg2: float): $Color
public static "darker"(arg0: $Color$Type, arg1: integer): $Color
public static "colorLerp"(arg0: $Easing$Type, arg1: float, arg2: float, arg3: float, arg4: $Color$Type, arg5: $Color$Type): $Color
public static "colorLerp"(arg0: $Easing$Type, arg1: float, arg2: $Color$Type, arg3: $Color$Type): $Color
public static "multicolorLerp"(arg0: $Easing$Type, arg1: float, arg2: float, arg3: float, arg4: $List$Type<($Color$Type)>): $Color
public static "multicolorLerp"(arg0: $Easing$Type, arg1: float, arg2: float, arg3: float, ...arg4: ($Color$Type)[]): $Color
public static "multicolorLerp"(arg0: $Easing$Type, arg1: float, ...arg2: ($Color$Type)[]): $Color
public static "multicolorLerp"(arg0: $Easing$Type, arg1: float, arg2: $List$Type<($Color$Type)>): $Color
public static "RGBToHSV"(arg0: $Color$Type, arg1: (float)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorHelper$Type = ($ColorHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorHelper_ = $ColorHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/$LodestoneLib" {
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $LodestoneLib {
static readonly "LOGGER": $Logger
static readonly "LODESTONE": string
static readonly "RANDOM": $RandomSource

constructor()

public "gatherData"(arg0: $GatherDataEvent$Type): void
public static "lodestonePath"(arg0: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneLib$Type = ($LodestoneLib);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneLib_ = $LodestoneLib$Type;
}}
declare module "packages/team/lodestar/lodestone/network/interaction/$ResetRightClickDelayPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $ResetRightClickDelayPacket extends $LodestoneClientPacket {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $ResetRightClickDelayPacket
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResetRightClickDelayPacket$Type = ($ResetRightClickDelayPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResetRightClickDelayPacket_ = $ResetRightClickDelayPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/screenparticle/$ParticleEmitterHandler$ItemParticleSupplier" {
import {$ScreenParticleHolder, $ScreenParticleHolder$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleHolder"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ParticleEmitterHandler$ItemParticleSupplier {

 "spawnEarlyParticles"(arg0: $ScreenParticleHolder$Type, arg1: $Level$Type, arg2: float, arg3: $ItemStack$Type, arg4: float, arg5: float): void
 "spawnLateParticles"(arg0: $ScreenParticleHolder$Type, arg1: $Level$Type, arg2: float, arg3: $ItemStack$Type, arg4: float, arg5: float): void
}

export namespace $ParticleEmitterHandler$ItemParticleSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleEmitterHandler$ItemParticleSupplier$Type = ($ParticleEmitterHandler$ItemParticleSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleEmitterHandler$ItemParticleSupplier_ = $ParticleEmitterHandler$ItemParticleSupplier$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/magic/$MagicHoeItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$LodestoneHoeItem, $LodestoneHoeItem$Type} from "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneHoeItem"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MagicHoeItem extends $LodestoneHoeItem {
readonly "magicDamage": float
/**
 * 
 * @deprecated
 */
static "TILLABLES": $Map<($Block), ($Pair<($Predicate<($UseOnContext)>), ($Consumer<($UseOnContext)>)>)>
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: float, arg4: $Item$Properties$Type)

public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MagicHoeItem$Type = ($MagicHoeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MagicHoeItem_ = $MagicHoeItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/recipe/$WrappedIngredient" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$IRecipeComponent, $IRecipeComponent$Type} from "packages/team/lodestar/lodestone/systems/recipe/$IRecipeComponent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $WrappedIngredient implements $IRecipeComponent {
readonly "ingredient": $Ingredient

constructor(arg0: $Ingredient$Type)

public "matches"(arg0: $ItemStack$Type): boolean
public "getStack"(): $ItemStack
public "getCount"(): integer
public "getItem"(): $Item
public "getStacks"(): $List<($ItemStack)>
public "isValid"(): boolean
get "stack"(): $ItemStack
get "count"(): integer
get "item"(): $Item
get "stacks"(): $List<($ItemStack)>
get "valid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrappedIngredient$Type = ($WrappedIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrappedIngredient_ = $WrappedIngredient$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/recipe/$ILodestoneRecipe" {
import {$InputReplacement, $InputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$InputReplacement"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ReplacementMatch, $ReplacementMatch$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ReplacementMatch"
import {$OutputReplacement, $OutputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$OutputReplacement"

export class $ILodestoneRecipe implements $Recipe<($Container)> {

constructor()

/**
 * 
 * @deprecated
 */
public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
/**
 * 
 * @deprecated
 */
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
/**
 * 
 * @deprecated
 */
public "assemble"(arg0: $Container$Type, arg1: $RegistryAccess$Type): $ItemStack
/**
 * 
 * @deprecated
 */
public "matches"(arg0: $Container$Type, arg1: $Level$Type): boolean
public "isSpecial"(): boolean
public "getRemainingItems"(arg0: $Container$Type): $NonNullList<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "isIncomplete"(): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "showNotification"(): boolean
public "getId"(): $ResourceLocation
public "getType"(): $ResourceLocation
public "replaceOutput"(match: $ReplacementMatch$Type, arg1: $OutputReplacement$Type): boolean
public "setGroup"(group: string): void
public "hasInput"(match: $ReplacementMatch$Type): boolean
public "getOrCreateId"(): $ResourceLocation
public "getSchema"(): $RecipeSchema
public "replaceInput"(match: $ReplacementMatch$Type, arg1: $InputReplacement$Type): boolean
public "hasOutput"(match: $ReplacementMatch$Type): boolean
public "getGroup"(): string
public "getMod"(): string
get "special"(): boolean
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "incomplete"(): boolean
get "serializer"(): $RecipeSerializer<(any)>
get "id"(): $ResourceLocation
get "type"(): $ResourceLocation
set "group"(value: string)
get "orCreateId"(): $ResourceLocation
get "schema"(): $RecipeSchema
get "group"(): string
get "mod"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILodestoneRecipe$Type = ($ILodestoneRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILodestoneRecipe_ = $ILodestoneRecipe$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeProvider" {
import {$RenderTypeToken, $RenderTypeToken$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeToken"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LodestoneRenderType, $LodestoneRenderType$Type} from "packages/team/lodestar/lodestone/systems/rendering/$LodestoneRenderType"
import {$LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder, $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type} from "packages/team/lodestar/lodestone/registry/client/$LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder"
import {$ShaderUniformHandler, $ShaderUniformHandler$Type} from "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$ShaderUniformHandler"

export class $RenderTypeProvider {

constructor(arg0: $Function$Type<($RenderTypeToken$Type), ($LodestoneRenderType$Type)>)

public "applyAndCache"(arg0: $RenderTypeToken$Type, arg1: $ShaderUniformHandler$Type): $LodestoneRenderType
public "applyAndCache"(arg0: $RenderTypeToken$Type): $LodestoneRenderType
public "applyWithModifier"(arg0: $RenderTypeToken$Type, arg1: $Consumer$Type<($LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type)>): $LodestoneRenderType
public "applyWithModifier"(arg0: $RenderTypeToken$Type, arg1: $ShaderUniformHandler$Type, arg2: $Consumer$Type<($LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type)>): $LodestoneRenderType
public "apply"(arg0: $RenderTypeToken$Type, arg1: $ShaderUniformHandler$Type): $LodestoneRenderType
public "apply"(arg0: $RenderTypeToken$Type): $LodestoneRenderType
public "applyWithModifierAndCache"(arg0: $RenderTypeToken$Type, arg1: $ShaderUniformHandler$Type, arg2: $Consumer$Type<($LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type)>): $LodestoneRenderType
public "applyWithModifierAndCache"(arg0: $RenderTypeToken$Type, arg1: $Consumer$Type<($LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type)>): $LodestoneRenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypeProvider$Type = ($RenderTypeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypeProvider_ = $RenderTypeProvider$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$ScreenshakeHandler" {
import {$ScreenshakeInstance, $ScreenshakeInstance$Type} from "packages/team/lodestar/lodestone/systems/screenshake/$ScreenshakeInstance"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $ScreenshakeHandler {
static readonly "INSTANCES": $ArrayList<($ScreenshakeInstance)>
static "intensity": float
static "yawOffset": float
static "pitchOffset": float

constructor()

public static "clientTick"(arg0: $Camera$Type, arg1: $RandomSource$Type): void
public static "addScreenshake"(arg0: $ScreenshakeInstance$Type): void
public static "randomizeOffset"(arg0: $RandomSource$Type): float
public static "cameraTick"(arg0: $Camera$Type, arg1: $RandomSource$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenshakeHandler$Type = ($ScreenshakeHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenshakeHandler_ = $ScreenshakeHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneThrowawayBlockData" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $LodestoneThrowawayBlockData {
static readonly "EMPTY": $LodestoneThrowawayBlockData

constructor()

public "hasCustomRenderType"(): boolean
public "getRenderType"(): $Supplier<($Supplier<($RenderType)>)>
public "setRenderType"(arg0: $Supplier$Type<($Supplier$Type<($RenderType$Type)>)>): $LodestoneThrowawayBlockData
get "renderType"(): $Supplier<($Supplier<($RenderType)>)>
set "renderType"(value: $Supplier$Type<($Supplier$Type<($RenderType$Type)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneThrowawayBlockData$Type = ($LodestoneThrowawayBlockData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneThrowawayBlockData_ = $LodestoneThrowawayBlockData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/network/$LodestoneTwoWayNBTPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LodestoneTwoWayPacket, $LodestoneTwoWayPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneTwoWayPacket"

export class $LodestoneTwoWayNBTPacket extends $LodestoneTwoWayPacket {

constructor(arg0: $CompoundTag$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "serverExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public "serverExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "clientExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public "clientExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTwoWayNBTPacket$Type = ($LodestoneTwoWayNBTPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTwoWayNBTPacket_ = $LodestoneTwoWayNBTPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$AngleHelper" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $AngleHelper {

constructor()

public static "deg"(arg0: double): float
public static "getShortestAngleDiff"(arg0: double, arg1: double, arg2: float): float
public static "getShortestAngleDiff"(arg0: double, arg1: double): float
public static "angleLerp"(arg0: double, arg1: double, arg2: double): float
public static "rad"(arg0: double): float
public static "verticalAngle"(arg0: $Direction$Type): float
public static "horizontalAngle"(arg0: $Direction$Type): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AngleHelper$Type = ($AngleHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AngleHelper_ = $AngleHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneScreenParticleRenderType" {
import {$Tesselator, $Tesselator$Type} from "packages/com/mojang/blaze3d/vertex/$Tesselator"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$TextureManager, $TextureManager$Type} from "packages/net/minecraft/client/renderer/texture/$TextureManager"

export interface $LodestoneScreenParticleRenderType {

 "end"(arg0: $Tesselator$Type): void
 "begin"(arg0: $BufferBuilder$Type, arg1: $TextureManager$Type): void
}

export namespace $LodestoneScreenParticleRenderType {
const ADDITIVE: $LodestoneScreenParticleRenderType
const TRANSPARENT: $LodestoneScreenParticleRenderType
const LUMITRANSPARENT: $LodestoneScreenParticleRenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneScreenParticleRenderType$Type = ($LodestoneScreenParticleRenderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneScreenParticleRenderType_ = $LodestoneScreenParticleRenderType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureLoader$ColorLerp" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LodestoneTextureLoader$ColorLerp {

 "lerp"(arg0: integer, arg1: integer, arg2: integer, arg3: float, arg4: float): float

(arg0: integer, arg1: integer, arg2: integer, arg3: float, arg4: float): float
}

export namespace $LodestoneTextureLoader$ColorLerp {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTextureLoader$ColorLerp$Type = ($LodestoneTextureLoader$ColorLerp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTextureLoader$ColorLerp_ = $LodestoneTextureLoader$ColorLerp$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/sound/$LodestoneBlockEntitySoundInstance" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"
import {$AbstractTickableSoundInstance, $AbstractTickableSoundInstance$Type} from "packages/net/minecraft/client/resources/sounds/$AbstractTickableSoundInstance"

export class $LodestoneBlockEntitySoundInstance<T extends $LodestoneBlockEntity> extends $AbstractTickableSoundInstance {
 "blockEntity": T

constructor(arg0: T, arg1: $SoundEvent$Type, arg2: float, arg3: float)

public "tick"(): void
public static "createUnseededRandom"(): $RandomSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockEntitySoundInstance$Type<T> = ($LodestoneBlockEntitySoundInstance<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockEntitySoundInstance_<T> = $LodestoneBlockEntitySoundInstance$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/blockentity/$ItemHolderBlockEntity" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$LodestoneBlockEntityInventory, $LodestoneBlockEntityInventory$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntityInventory"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ItemHolderBlockEntity extends $LodestoneBlockEntity {
 "inventory": $LodestoneBlockEntityInventory
 "needsSync": boolean
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "onUse"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
public "load"(arg0: $CompoundTag$Type): void
public "onBreak"(arg0: $Player$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemHolderBlockEntity$Type = ($ItemHolderBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemHolderBlockEntity_ = $ItemHolderBlockEntity$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/shader/$ShaderHolder" {
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$ResourceProvider, $ResourceProvider$Type} from "packages/net/minecraft/server/packs/resources/$ResourceProvider"
import {$ExtendedShaderInstance, $ExtendedShaderInstance$Type} from "packages/team/lodestar/lodestone/systems/rendering/shader/$ExtendedShaderInstance"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export class $ShaderHolder {
readonly "shaderLocation": $ResourceLocation
readonly "shaderFormat": $VertexFormat
 "uniformsToCache": $Collection<(string)>

constructor(arg0: $ResourceLocation$Type, arg1: $VertexFormat$Type, ...arg2: (string)[])

public "getInstance"(): $Supplier<($ShaderInstance)>
public "createInstance"(arg0: $ResourceProvider$Type): $ExtendedShaderInstance
public "setShaderInstance"(arg0: $ShaderInstance$Type): void
public "getShard"(): $RenderStateShard$ShaderStateShard
get "instance"(): $Supplier<($ShaderInstance)>
set "shaderInstance"(value: $ShaderInstance$Type)
get "shard"(): $RenderStateShard$ShaderStateShard
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderHolder$Type = ($ShaderHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderHolder_ = $ShaderHolder$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/particle/$LodestoneParticleRegistry" {
import {$LodestoneTerrainParticleType, $LodestoneTerrainParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneTerrainParticleType"
import {$LodestoneItemCrumbsParticleType, $LodestoneItemCrumbsParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneItemCrumbsParticleType"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$LodestoneWorldParticleType, $LodestoneWorldParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneWorldParticleType"

export class $LodestoneParticleRegistry {
static "PARTICLES": $DeferredRegister<($ParticleType<(any)>)>
static "WISP_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "SMOKE_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "SPARKLE_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "TWINKLE_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "STAR_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "SPARK_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "EXTRUDING_SPARK_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "THIN_EXTRUDING_SPARK_PARTICLE": $RegistryObject<($LodestoneWorldParticleType)>
static "TERRAIN_PARTICLE": $RegistryObject<($LodestoneTerrainParticleType)>
static "ITEM_PARTICLE": $RegistryObject<($LodestoneItemCrumbsParticleType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneParticleRegistry$Type = ($LodestoneParticleRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneParticleRegistry_ = $LodestoneParticleRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/network/interaction/$RightClickEmptyPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$LodestoneServerPacket, $LodestoneServerPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneServerPacket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $RightClickEmptyPacket extends $LodestoneServerPacket {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $RightClickEmptyPacket
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RightClickEmptyPacket$Type = ($RightClickEmptyPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RightClickEmptyPacket_ = $RightClickEmptyPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/network/$LodestoneClientNBTPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LodestoneClientNBTPacket extends $LodestoneClientPacket {

constructor(arg0: $CompoundTag$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneClientNBTPacket$Type = ($LodestoneClientNBTPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneClientNBTPacket_ = $LodestoneClientNBTPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/easing/$Easing$Elastic" {
import {$Easing$Back, $Easing$Back$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing$Back"
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"

export class $Easing$Elastic extends $Easing {
static readonly "EASINGS": $HashMap<(string), ($Easing)>
readonly "name": string
static readonly "LINEAR": $Easing
static readonly "QUAD_IN": $Easing
static readonly "QUAD_OUT": $Easing
static readonly "QUAD_IN_OUT": $Easing
static readonly "CUBIC_IN": $Easing
static readonly "CUBIC_OUT": $Easing
static readonly "CUBIC_IN_OUT": $Easing
static readonly "QUARTIC_IN": $Easing
static readonly "QUARTIC_OUT": $Easing
static readonly "QUARTIC_IN_OUT": $Easing
static readonly "QUINTIC_IN": $Easing
static readonly "QUINTIC_OUT": $Easing
static readonly "QUINTIC_IN_OUT": $Easing
static readonly "SINE_IN": $Easing
static readonly "SINE_OUT": $Easing
static readonly "SINE_IN_OUT": $Easing
static readonly "EXPO_IN": $Easing
static readonly "EXPO_OUT": $Easing
static readonly "EXPO_IN_OUT": $Easing
static readonly "CIRC_IN": $Easing
static readonly "CIRC_OUT": $Easing
static readonly "CIRC_IN_OUT": $Easing
static readonly "ELASTIC_IN": $Easing$Elastic
static readonly "ELASTIC_OUT": $Easing$Elastic
static readonly "ELASTIC_IN_OUT": $Easing$Elastic
static readonly "BACK_IN": $Easing$Back
static readonly "BACK_OUT": $Easing$Back
static readonly "BACK_IN_OUT": $Easing$Back
static readonly "BOUNCE_IN": $Easing
static readonly "BOUNCE_OUT": $Easing
static readonly "BOUNCE_IN_OUT": $Easing

constructor(arg0: string, arg1: float, arg2: float)
constructor(arg0: string)

public "getPeriod"(): float
public "getAmplitude"(): float
public "setPeriod"(arg0: float): void
public "setAmplitude"(arg0: float): void
get "period"(): float
get "amplitude"(): float
set "period"(value: float)
set "amplitude"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Easing$Elastic$Type = ($Easing$Elastic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Easing$Elastic_ = $Easing$Elastic$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$CurioHelper" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SlotResult, $SlotResult$Type} from "packages/top/theillusivec4/curios/api/$SlotResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$ImmutableTriple, $ImmutableTriple$Type} from "packages/org/apache/commons/lang3/tuple/$ImmutableTriple"

export class $CurioHelper {

constructor()

public static "getEquippedCurio"(arg0: $LivingEntity$Type, arg1: $Item$Type): $Optional<($SlotResult)>
public static "getEquippedCurio"(arg0: $LivingEntity$Type, arg1: $Predicate$Type<($ItemStack$Type)>): $Optional<($SlotResult)>
public static "hasCurioEquipped"(arg0: $LivingEntity$Type, arg1: $Item$Type): boolean
public static "findCosmeticCurio"(arg0: $Predicate$Type<($ItemStack$Type)>, arg1: $LivingEntity$Type): $Optional<($ImmutableTriple<(string), (integer), ($ItemStack)>)>
public static "getEquippedCurios"(arg0: $LivingEntity$Type, arg1: $Predicate$Type<($ItemStack$Type)>): $ArrayList<($ItemStack)>
public static "getEquippedCurios"(arg0: $LivingEntity$Type): $ArrayList<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CurioHelper$Type = ($CurioHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CurioHelper_ = $CurioHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith$ModelFileSupplier" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ModelFile, $ModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile"

export interface $ModularBlockStateSmith$ModelFileSupplier {

 "generateModel"(arg0: $Block$Type): $ModelFile

(arg0: $Block$Type): $ModelFile
}

export namespace $ModularBlockStateSmith$ModelFileSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModularBlockStateSmith$ModelFileSupplier$Type = ($ModularBlockStateSmith$ModelFileSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModularBlockStateSmith$ModelFileSupplier_ = $ModularBlockStateSmith$ModelFileSupplier$Type;
}}
declare module "packages/team/lodestar/lodestone/network/worldevent/$SyncWorldEventPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $SyncWorldEventPacket extends $LodestoneClientPacket {
 "start": boolean
 "eventData": $CompoundTag

constructor(arg0: $ResourceLocation$Type, arg1: boolean, arg2: $CompoundTag$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SyncWorldEventPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncWorldEventPacket$Type = ($SyncWorldEventPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncWorldEventPacket_ = $SyncWorldEventPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/$ParticleEffectSpawner" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$WorldParticleBuilder, $WorldParticleBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/builder/$WorldParticleBuilder"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $ParticleEffectSpawner {

constructor(arg0: $WorldParticleBuilder$Type, arg1: $Consumer$Type<($WorldParticleBuilder$Type)>, arg2: $WorldParticleBuilder$Type, arg3: $Consumer$Type<($WorldParticleBuilder$Type)>)
constructor(arg0: $Level$Type, arg1: $Vec3$Type, arg2: $WorldParticleBuilder$Type, arg3: $WorldParticleBuilder$Type)
constructor(arg0: $WorldParticleBuilder$Type, arg1: $Consumer$Type<($WorldParticleBuilder$Type)>)
constructor(arg0: $Level$Type, arg1: $Vec3$Type, arg2: $WorldParticleBuilder$Type)

public "actRaw"(arg0: $Consumer$Type<($WorldParticleBuilder$Type)>): $ParticleEffectSpawner
public "getBloomBuilder"(): $WorldParticleBuilder
public "spawnParticlesRaw"(): void
public "getBuilder"(): $WorldParticleBuilder
public "spawnParticles"(): void
public "act"(arg0: $Consumer$Type<($WorldParticleBuilder$Type)>): $ParticleEffectSpawner
get "bloomBuilder"(): $WorldParticleBuilder
get "builder"(): $WorldParticleBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleEffectSpawner$Type = ($ParticleEffectSpawner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleEffectSpawner_ = $ParticleEffectSpawner$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $RenderHandler$LodestoneRenderLayer {

constructor(arg0: $HashMap$Type<($RenderType$Type), ($BufferBuilder$Type)>, arg1: $HashMap$Type<($RenderType$Type), ($BufferBuilder$Type)>)
constructor(arg0: $HashMap$Type<($RenderType$Type), ($BufferBuilder$Type)>, arg1: $HashMap$Type<($RenderType$Type), ($BufferBuilder$Type)>, arg2: integer)

public "getTarget"(): $MultiBufferSource$BufferSource
public "getBuffers"(): $HashMap<($RenderType), ($BufferBuilder)>
public "getParticleBuffers"(): $HashMap<($RenderType), ($BufferBuilder)>
public "getParticleTarget"(): $MultiBufferSource$BufferSource
get "target"(): $MultiBufferSource$BufferSource
get "buffers"(): $HashMap<($RenderType), ($BufferBuilder)>
get "particleBuffers"(): $HashMap<($RenderType), ($BufferBuilder)>
get "particleTarget"(): $MultiBufferSource$BufferSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHandler$LodestoneRenderLayer$Type = ($RenderHandler$LodestoneRenderLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHandler$LodestoneRenderLayer_ = $RenderHandler$LodestoneRenderLayer$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureAtlasSpriteLoader" {
import {$ForgeTextureMetadata, $ForgeTextureMetadata$Type} from "packages/net/minecraftforge/client/textures/$ForgeTextureMetadata"
import {$AnimationMetadataSection, $AnimationMetadataSection$Type} from "packages/net/minecraft/client/resources/metadata/animation/$AnimationMetadataSection"
import {$ITextureAtlasSpriteLoader, $ITextureAtlasSpriteLoader$Type} from "packages/net/minecraftforge/client/textures/$ITextureAtlasSpriteLoader"
import {$Resource, $Resource$Type} from "packages/net/minecraft/server/packs/resources/$Resource"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LodestoneTextureLoader$TextureModifier, $LodestoneTextureLoader$TextureModifier$Type} from "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureLoader$TextureModifier"
import {$FrameSize, $FrameSize$Type} from "packages/net/minecraft/client/resources/metadata/animation/$FrameSize"
import {$SpriteContents, $SpriteContents$Type} from "packages/net/minecraft/client/renderer/texture/$SpriteContents"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $LodestoneTextureAtlasSpriteLoader implements $ITextureAtlasSpriteLoader {

constructor(arg0: $LodestoneTextureLoader$TextureModifier$Type)

public "loadContents"(arg0: $ResourceLocation$Type, arg1: $Resource$Type, arg2: $FrameSize$Type, arg3: $NativeImage$Type, arg4: $AnimationMetadataSection$Type, arg5: $ForgeTextureMetadata$Type): $SpriteContents
public "makeSprite"(arg0: $ResourceLocation$Type, arg1: $SpriteContents$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $TextureAtlasSprite
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTextureAtlasSpriteLoader$Type = ($LodestoneTextureAtlasSpriteLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTextureAtlasSpriteLoader_ = $LodestoneTextureAtlasSpriteLoader$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/$ObjParser" {
import {$Face, $Face$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$Face"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Resource, $Resource$Type} from "packages/net/minecraft/server/packs/resources/$Resource"

export class $ObjParser {
 "vertices": $List<($Vector3f)>
 "normals": $List<($Vector3f)>
 "uvs": $List<($Vec2)>
 "faces": $List<($Face)>

constructor()

public "getFaces"(): $ArrayList<($Face)>
public "parseObjFile"(arg0: $Resource$Type): void
get "faces"(): $ArrayList<($Face)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjParser$Type = ($ObjParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjParser_ = $ObjParser$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/sound/$SoundMotifSoundType" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$SoundSource, $SoundSource$Type} from "packages/net/minecraft/sounds/$SoundSource"
import {$ExtendedSoundType, $ExtendedSoundType$Type} from "packages/team/lodestar/lodestone/systems/sound/$ExtendedSoundType"

export class $SoundMotifSoundType extends $ExtendedSoundType {
readonly "motifSound": $Supplier<($SoundEvent)>
static readonly "EMPTY": $SoundType
static readonly "WOOD": $SoundType
static readonly "GRAVEL": $SoundType
static readonly "GRASS": $SoundType
static readonly "LILY_PAD": $SoundType
static readonly "STONE": $SoundType
static readonly "METAL": $SoundType
static readonly "GLASS": $SoundType
static readonly "WOOL": $SoundType
static readonly "SAND": $SoundType
static readonly "SNOW": $SoundType
static readonly "POWDER_SNOW": $SoundType
static readonly "LADDER": $SoundType
static readonly "ANVIL": $SoundType
static readonly "SLIME_BLOCK": $SoundType
static readonly "HONEY_BLOCK": $SoundType
static readonly "WET_GRASS": $SoundType
static readonly "CORAL_BLOCK": $SoundType
static readonly "BAMBOO": $SoundType
static readonly "BAMBOO_SAPLING": $SoundType
static readonly "SCAFFOLDING": $SoundType
static readonly "SWEET_BERRY_BUSH": $SoundType
static readonly "CROP": $SoundType
static readonly "HARD_CROP": $SoundType
static readonly "VINE": $SoundType
static readonly "NETHER_WART": $SoundType
static readonly "LANTERN": $SoundType
static readonly "STEM": $SoundType
static readonly "NYLIUM": $SoundType
static readonly "FUNGUS": $SoundType
static readonly "ROOTS": $SoundType
static readonly "SHROOMLIGHT": $SoundType
static readonly "WEEPING_VINES": $SoundType
static readonly "TWISTING_VINES": $SoundType
static readonly "SOUL_SAND": $SoundType
static readonly "SOUL_SOIL": $SoundType
static readonly "BASALT": $SoundType
static readonly "WART_BLOCK": $SoundType
static readonly "NETHERRACK": $SoundType
static readonly "NETHER_BRICKS": $SoundType
static readonly "NETHER_SPROUTS": $SoundType
static readonly "NETHER_ORE": $SoundType
static readonly "BONE_BLOCK": $SoundType
static readonly "NETHERITE_BLOCK": $SoundType
static readonly "ANCIENT_DEBRIS": $SoundType
static readonly "LODESTONE": $SoundType
static readonly "CHAIN": $SoundType
static readonly "NETHER_GOLD_ORE": $SoundType
static readonly "GILDED_BLACKSTONE": $SoundType
static readonly "CANDLE": $SoundType
static readonly "AMETHYST": $SoundType
static readonly "AMETHYST_CLUSTER": $SoundType
static readonly "SMALL_AMETHYST_BUD": $SoundType
static readonly "MEDIUM_AMETHYST_BUD": $SoundType
static readonly "LARGE_AMETHYST_BUD": $SoundType
static readonly "TUFF": $SoundType
static readonly "CALCITE": $SoundType
static readonly "DRIPSTONE_BLOCK": $SoundType
static readonly "POINTED_DRIPSTONE": $SoundType
static readonly "COPPER": $SoundType
static readonly "CAVE_VINES": $SoundType
static readonly "SPORE_BLOSSOM": $SoundType
static readonly "AZALEA": $SoundType
static readonly "FLOWERING_AZALEA": $SoundType
static readonly "MOSS_CARPET": $SoundType
static readonly "PINK_PETALS": $SoundType
static readonly "MOSS": $SoundType
static readonly "BIG_DRIPLEAF": $SoundType
static readonly "SMALL_DRIPLEAF": $SoundType
static readonly "ROOTED_DIRT": $SoundType
static readonly "HANGING_ROOTS": $SoundType
static readonly "AZALEA_LEAVES": $SoundType
static readonly "SCULK_SENSOR": $SoundType
static readonly "SCULK_CATALYST": $SoundType
static readonly "SCULK": $SoundType
static readonly "SCULK_VEIN": $SoundType
static readonly "SCULK_SHRIEKER": $SoundType
static readonly "GLOW_LICHEN": $SoundType
static readonly "DEEPSLATE": $SoundType
static readonly "DEEPSLATE_BRICKS": $SoundType
static readonly "DEEPSLATE_TILES": $SoundType
static readonly "POLISHED_DEEPSLATE": $SoundType
static readonly "FROGLIGHT": $SoundType
static readonly "FROGSPAWN": $SoundType
static readonly "MANGROVE_ROOTS": $SoundType
static readonly "MUDDY_MANGROVE_ROOTS": $SoundType
static readonly "MUD": $SoundType
static readonly "MUD_BRICKS": $SoundType
static readonly "PACKED_MUD": $SoundType
static readonly "HANGING_SIGN": $SoundType
static readonly "NETHER_WOOD_HANGING_SIGN": $SoundType
static readonly "BAMBOO_WOOD_HANGING_SIGN": $SoundType
static readonly "BAMBOO_WOOD": $SoundType
static readonly "NETHER_WOOD": $SoundType
static readonly "CHERRY_WOOD": $SoundType
static readonly "CHERRY_SAPLING": $SoundType
static readonly "CHERRY_LEAVES": $SoundType
static readonly "CHERRY_WOOD_HANGING_SIGN": $SoundType
static readonly "CHISELED_BOOKSHELF": $SoundType
static readonly "SUSPICIOUS_SAND": $SoundType
static readonly "SUSPICIOUS_GRAVEL": $SoundType
static readonly "DECORATED_POT": $SoundType
static readonly "DECORATED_POT_CRACKED": $SoundType
readonly "volume": float
readonly "pitch": float

constructor(arg0: $Supplier$Type<($SoundEvent$Type)>, arg1: float, arg2: float, arg3: $Supplier$Type<($SoundEvent$Type)>, arg4: $Supplier$Type<($SoundEvent$Type)>, arg5: $Supplier$Type<($SoundEvent$Type)>, arg6: $Supplier$Type<($SoundEvent$Type)>, arg7: $Supplier$Type<($SoundEvent$Type)>)

public "getMotifVolume"(): float
public "getMotifPitch"(): float
public "onPlayHitSound"(arg0: $BlockPos$Type): void
public "onPlayBreakSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "onPlayPlaceSound"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Player$Type): void
public "onPlayStepSound"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $SoundSource$Type): void
public "onPlayFallSound"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $SoundSource$Type): void
get "motifVolume"(): float
get "motifPitch"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoundMotifSoundType$Type = ($SoundMotifSoundType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoundMotifSoundType_ = $SoundMotifSoundType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$SparkBehaviorComponent" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $SparkBehaviorComponent implements $LodestoneBehaviorComponent {

constructor(arg0: $GenericParticleData$Type)
constructor()

public "tick"(arg0: $LodestoneWorldParticle$Type): void
public "getCachedDirection"(): $Vec3
public "getBehaviorType"(): $LodestoneParticleBehavior
public "sparkEnd"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public "sparkStart"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public "getLengthData"(arg0: $LodestoneWorldParticle$Type): $GenericParticleData
public "getLengthData"(): $GenericParticleData
public "getDirection"(arg0: $LodestoneWorldParticle$Type): $Vec3
get "cachedDirection"(): $Vec3
get "behaviorType"(): $LodestoneParticleBehavior
get "lengthData"(): $GenericParticleData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SparkBehaviorComponent$Type = ($SparkBehaviorComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SparkBehaviorComponent_ = $SparkBehaviorComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/recipe/$NBTCarryRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$NBTCarryRecipe, $NBTCarryRecipe$Type} from "packages/team/lodestar/lodestone/recipe/$NBTCarryRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $NBTCarryRecipe$Serializer implements $RecipeSerializer<($NBTCarryRecipe)> {

constructor()

public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $NBTCarryRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $NBTCarryRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $NBTCarryRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $NBTCarryRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTCarryRecipe$Serializer$Type = ($NBTCarryRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTCarryRecipe$Serializer_ = $NBTCarryRecipe$Serializer$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LodestoneClientPacket {

constructor()

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneClientPacket$Type = ($LodestoneClientPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneClientPacket_ = $LodestoneClientPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/network/$TotemOfUndyingEffectPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $TotemOfUndyingEffectPacket extends $LodestoneClientPacket {

constructor(arg0: $Entity$Type, arg1: $ItemStack$Type)
constructor(arg0: integer, arg1: $ItemStack$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $TotemOfUndyingEffectPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TotemOfUndyingEffectPacket$Type = ($TotemOfUndyingEffectPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TotemOfUndyingEffectPacket_ = $TotemOfUndyingEffectPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$ItemModelSmith$ItemModelSupplier" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$LodestoneItemModelProvider, $LodestoneItemModelProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneItemModelProvider"

export interface $ItemModelSmith$ItemModelSupplier {

 "act"(arg0: $Item$Type, arg1: $LodestoneItemModelProvider$Type): void

(arg0: $Item$Type, arg1: $LodestoneItemModelProvider$Type): void
}

export namespace $ItemModelSmith$ItemModelSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelSmith$ItemModelSupplier$Type = ($ItemModelSmith$ItemModelSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelSmith$ItemModelSupplier_ = $ItemModelSmith$ItemModelSupplier$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventCreationEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$WorldEventInstanceEvent, $WorldEventInstanceEvent$Type} from "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventInstanceEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $WorldEventCreationEvent extends $WorldEventInstanceEvent {

constructor()
constructor(arg0: $WorldEventInstance$Type, arg1: $Level$Type)

public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventCreationEvent$Type = ($WorldEventCreationEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventCreationEvent_ = $WorldEventCreationEvent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneItemModelProvider" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ItemModelProvider, $ItemModelProvider$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneItemModelProvider extends $ItemModelProvider {
static readonly "BLOCK_FOLDER": string
static readonly "ITEM_FOLDER": string
readonly "generatedModels": $Map<($ResourceLocation), (T)>
readonly "existingFileHelper": $ExistingFileHelper

constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type)

public "getItemName"(arg0: $Item$Type): string
public "getTexturePath"(): string
public "createGenericModel"(arg0: $Item$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): void
public "getBlockTexture"(arg0: string): $ResourceLocation
public "getItemTexture"(arg0: string): $ResourceLocation
public "setTexturePath"(arg0: string): void
public "getBlockTextureFromCache"(arg0: string): $ResourceLocation
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "texturePath"(): string
set "texturePath"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemModelProvider$Type = ($LodestoneItemModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemModelProvider_ = $LodestoneItemModelProvider$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODStrategy$LODBuilderSetup" {
import {$LODBuilder, $LODBuilder$Type} from "packages/team/lodestar/lodestone/systems/model/obj/lod/$LODBuilder"

export interface $LODStrategy$LODBuilderSetup<T> {

 "lodBuilder"(arg0: $LODBuilder$Type<(T)>): void

(arg0: $LODBuilder$Type<(T)>): void
}

export namespace $LODStrategy$LODBuilderSetup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LODStrategy$LODBuilderSetup$Type<T> = ($LODStrategy$LODBuilderSetup<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LODStrategy$LODBuilderSetup_<T> = $LODStrategy$LODBuilderSetup$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockStateProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WallSide, $WallSide$Type} from "packages/net/minecraft/world/level/block/state/properties/$WallSide"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LodestoneItemModelProvider, $LodestoneItemModelProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneItemModelProvider"
import {$ModelFile, $ModelFile$Type} from "packages/net/minecraftforge/client/model/generators/$ModelFile"
import {$BlockStateProvider, $BlockStateProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockStateProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ModularBlockStateSmith$ModelFileSupplier, $ModularBlockStateSmith$ModelFileSupplier$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith$ModelFileSupplier"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"

export class $LodestoneBlockStateProvider extends $BlockStateProvider {
readonly "staticTextures": $Set<($ResourceLocation)>
readonly "itemModelProvider": $LodestoneItemModelProvider
static readonly "WALL_PROPS": $ImmutableMap<($Direction), ($Property<($WallSide)>)>

constructor(arg0: $PackOutput$Type, arg1: string, arg2: $ExistingFileHelper$Type, arg3: $LodestoneItemModelProvider$Type)

public "varyingRotationBlock"(arg0: $Block$Type, arg1: $ModelFile$Type): void
public "cubeModelAirTexture"(arg0: $Block$Type): $ModelFile
public "markTextureAsStatic"(arg0: $ResourceLocation$Type): $ResourceLocation
public "getStaticBlockTexture"(arg0: string): $ResourceLocation
public "getBlockName"(arg0: $Block$Type): string
public "fromFunction"(arg0: $BiFunction$Type<(string), ($ResourceLocation$Type), ($ModelFile$Type)>): $ModularBlockStateSmith$ModelFileSupplier
public "extend"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
public static "getTexturePath"(): string
public "getBlockTexture"(arg0: string): $ResourceLocation
public "setTexturePath"(arg0: string): void
public "leavesBlockModel"(arg0: $Block$Type): $ModelFile
public "airModel"(arg0: $Block$Type): $ModelFile
public "predefinedModel"(arg0: $Block$Type): $ModelFile
public "predefinedModel"(arg0: $Block$Type, arg1: string): $ModelFile
public "grassBlockModel"(arg0: $Block$Type): $ModelFile
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "texturePath"(): string
set "texturePath"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockStateProvider$Type = ($LodestoneBlockStateProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockStateProvider_ = $LodestoneBlockStateProvider$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneSignBlockEntity" {
import {$SignBlockEntity, $SignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SignBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LodestoneSignBlockEntity extends $SignBlockEntity {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getType"(): $BlockEntityType<(any)>
get "type"(): $BlockEntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneSignBlockEntity$Type = ($LodestoneSignBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneSignBlockEntity_ = $LodestoneSignBlockEntity$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$ColorParticleDataBuilder, $ColorParticleDataBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleDataBuilder"

export class $ColorParticleData {
readonly "r1": float
readonly "g1": float
readonly "b1": float
readonly "r2": float
readonly "g2": float
readonly "b2": float
readonly "colorCoefficient": float
readonly "colorCurveEasing": $Easing
 "coefficientMultiplier": float


public "copy"(): $ColorParticleDataBuilder
public static "create"(arg0: $Color$Type, arg1: $Color$Type): $ColorParticleDataBuilder
public static "create"(arg0: float, arg1: float, arg2: float): $ColorParticleDataBuilder
public static "create"(arg0: $Color$Type): $ColorParticleDataBuilder
public static "create"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $ColorParticleDataBuilder
public "getProgress"(arg0: float, arg1: float): float
public "overrideCoefficientMultiplier"(arg0: float): $ColorParticleData
public "multiplyCoefficient"(arg0: float): $ColorParticleData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorParticleData$Type = ($ColorParticleData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorParticleData_ = $ColorParticleData$Type;
}}
declare module "packages/team/lodestar/lodestone/data/$LodestoneDamageTypeDatagen" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$DamageTypeTagsProvider, $DamageTypeTagsProvider$Type} from "packages/net/minecraft/data/tags/$DamageTypeTagsProvider"

export class $LodestoneDamageTypeDatagen extends $DamageTypeTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneDamageTypeDatagen$Type = ($LodestoneDamageTypeDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneDamageTypeDatagen_ = $LodestoneDamageTypeDatagen$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventDiscardEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$WorldEventInstanceEvent, $WorldEventInstanceEvent$Type} from "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventInstanceEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $WorldEventDiscardEvent extends $WorldEventInstanceEvent {

constructor()
constructor(arg0: $WorldEventInstance$Type, arg1: $Level$Type)

public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventDiscardEvent$Type = ($WorldEventDiscardEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventDiscardEvent_ = $WorldEventDiscardEvent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$BlockStateEntry" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LodestoneBlockFiller$BlockStateEntry {


public "getState"(): $BlockState
public "place"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): void
public "canPlace"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): boolean
public "tryDiscard"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type): boolean
get "state"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockFiller$BlockStateEntry$Type = ($LodestoneBlockFiller$BlockStateEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller$BlockStateEntry_ = $LodestoneBlockFiller$BlockStateEntry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$ConfigPath" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $LodestoneConfig$ConfigPath extends $Record {

constructor(...strings: (string)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "strings"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneConfig$ConfigPath$Type = ($LodestoneConfig$ConfigPath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneConfig$ConfigPath_ = $LodestoneConfig$ConfigPath$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$DimensionPlacementFilter" {
import {$PlacementFilter, $PlacementFilter$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementFilter"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $DimensionPlacementFilter extends $PlacementFilter {
static readonly "CODEC": $Codec<($DimensionPlacementFilter)>


public static "of"(arg0: $Set$Type<($ResourceKey$Type<($Level$Type)>)>): $DimensionPlacementFilter
public static "fromStrings"(arg0: $List$Type<(any)>): $Set<($ResourceKey<($Level)>)>
public "type"(): $PlacementModifierType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionPlacementFilter$Type = ($DimensionPlacementFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionPlacementFilter_ = $DimensionPlacementFilter$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$MultiBlockStructure$StructurePiece, $MultiBlockStructure$StructurePiece$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure$StructurePiece"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $MultiBlockStructure {
readonly "structurePieces": $ArrayList<($MultiBlockStructure$StructurePiece)>

constructor(arg0: $ArrayList$Type<($MultiBlockStructure$StructurePiece$Type)>)

public static "of"(...arg0: ($MultiBlockStructure$StructurePiece$Type)[]): $MultiBlockStructure
public "place"(arg0: $BlockPlaceContext$Type): void
public "canPlace"(arg0: $BlockPlaceContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiBlockStructure$Type = ($MultiBlockStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiBlockStructure_ = $MultiBlockStructure$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$WorldVFXBuilder" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$TrailPoint, $TrailPoint$Type} from "packages/team/lodestar/lodestone/systems/rendering/trail/$TrailPoint"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor, $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor$Type} from "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$TrailRenderPoint, $TrailRenderPoint$Type} from "packages/team/lodestar/lodestone/systems/rendering/trail/$TrailRenderPoint"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $VFXBuilders$WorldVFXBuilder {
static readonly "CONSUMER_INFO_MAP": $HashMap<($VertexFormatElement), ($VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor)>

constructor()

public "setFormat"(arg0: $VertexFormat$Type): $VFXBuilders$WorldVFXBuilder
public "getFormat"(): $VertexFormat
public "setColor"(arg0: float, arg1: float, arg2: float): $VFXBuilders$WorldVFXBuilder
public "setColor"(arg0: $Color$Type, arg1: float): $VFXBuilders$WorldVFXBuilder
public "setColor"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$WorldVFXBuilder
public "setColor"(arg0: $Color$Type): $VFXBuilders$WorldVFXBuilder
public "getBufferSource"(): $MultiBufferSource
public "getRenderType"(): $RenderType
public "setRenderType"(arg0: $RenderType$Type): $VFXBuilders$WorldVFXBuilder
public "setAlpha"(arg0: float): $VFXBuilders$WorldVFXBuilder
public "renderBeam"(arg0: $Matrix4f$Type, arg1: $BlockPos$Type, arg2: $BlockPos$Type, arg3: float): $VFXBuilders$WorldVFXBuilder
public "renderBeam"(arg0: $Matrix4f$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: float): $VFXBuilders$WorldVFXBuilder
public "renderBeam"(arg0: $Matrix4f$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: float, arg4: $Vec3$Type, arg5: $Consumer$Type<($VFXBuilders$WorldVFXBuilder$Type)>): $VFXBuilders$WorldVFXBuilder
public "renderBeam"(arg0: $Matrix4f$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: float, arg4: $Vec3$Type): $VFXBuilders$WorldVFXBuilder
public "renderBeam"(arg0: $Matrix4f$Type, arg1: $Vec3$Type, arg2: $Vec3$Type, arg3: float, arg4: $Consumer$Type<($VFXBuilders$WorldVFXBuilder$Type)>): $VFXBuilders$WorldVFXBuilder
public "setLight"(arg0: integer): $VFXBuilders$WorldVFXBuilder
public "renderQuad"(arg0: $PoseStack$Type, arg1: ($Vector3f$Type)[], arg2: float): $VFXBuilders$WorldVFXBuilder
public "renderQuad"(arg0: $PoseStack$Type, arg1: float, arg2: float): $VFXBuilders$WorldVFXBuilder
public "renderQuad"(arg0: $PoseStack$Type, arg1: float): $VFXBuilders$WorldVFXBuilder
public "renderQuad"(arg0: $PoseStack$Type, arg1: ($Vector3f$Type)[], arg2: float, arg3: float): $VFXBuilders$WorldVFXBuilder
public "renderQuad"(arg0: $Matrix4f$Type, arg1: ($Vector3f$Type)[]): $VFXBuilders$WorldVFXBuilder
public "getSupplier"(): $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor
public "getVertexConsumer"(): $VertexConsumer
public "setUV"(arg0: float, arg1: float, arg2: float, arg3: float): $VFXBuilders$WorldVFXBuilder
public "setColorRaw"(arg0: float, arg1: float, arg2: float): $VFXBuilders$WorldVFXBuilder
public "setVertexConsumer"(arg0: $VertexConsumer$Type): $VFXBuilders$WorldVFXBuilder
public "addModularActor"(arg0: any, arg1: $Consumer$Type<($VFXBuilders$WorldVFXBuilder$Type)>): $VFXBuilders$WorldVFXBuilder
public "addModularActor"(arg0: $Consumer$Type<($VFXBuilders$WorldVFXBuilder$Type)>): $VFXBuilders$WorldVFXBuilder
public "setVertexSupplier"(arg0: $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor$Type): $VFXBuilders$WorldVFXBuilder
public "setRenderTypeRaw"(arg0: $RenderType$Type): $VFXBuilders$WorldVFXBuilder
public "setFormatRaw"(arg0: $VertexFormat$Type): $VFXBuilders$WorldVFXBuilder
public "getModularActors"(): $Optional<($HashMap<(any), ($Consumer<($VFXBuilders$WorldVFXBuilder)>)>)>
public "renderPoints"(arg0: ($TrailRenderPoint$Type)[], arg1: float, arg2: float, arg3: float, arg4: float, arg5: $Consumer$Type<(float)>): $VFXBuilders$WorldVFXBuilder
public "renderSphere"(arg0: $PoseStack$Type, arg1: float, arg2: integer, arg3: integer): $VFXBuilders$WorldVFXBuilder
public "renderTrail"(arg0: $Matrix4f$Type, arg1: $List$Type<($TrailPoint$Type)>, arg2: $Function$Type<(float), (float)>, arg3: $Consumer$Type<(float)>): $VFXBuilders$WorldVFXBuilder
public "renderTrail"(arg0: $PoseStack$Type, arg1: $List$Type<($TrailPoint$Type)>, arg2: $Function$Type<(float), (float)>, arg3: $Consumer$Type<(float)>): $VFXBuilders$WorldVFXBuilder
public "renderTrail"(arg0: $PoseStack$Type, arg1: $List$Type<($TrailPoint$Type)>, arg2: float): $VFXBuilders$WorldVFXBuilder
public "renderTrail"(arg0: $PoseStack$Type, arg1: $List$Type<($TrailPoint$Type)>, arg2: $Function$Type<(float), (float)>): $VFXBuilders$WorldVFXBuilder
public "replaceBufferSource"(arg0: $MultiBufferSource$Type): $VFXBuilders$WorldVFXBuilder
public "replaceBufferSource"(arg0: $RenderHandler$LodestoneRenderLayer$Type): $VFXBuilders$WorldVFXBuilder
public "getNextModularActor"(): $Optional<($Consumer<($VFXBuilders$WorldVFXBuilder)>)>
set "format"(value: $VertexFormat$Type)
get "format"(): $VertexFormat
set "color"(value: $Color$Type)
get "bufferSource"(): $MultiBufferSource
get "renderType"(): $RenderType
set "renderType"(value: $RenderType$Type)
set "alpha"(value: float)
set "light"(value: integer)
get "supplier"(): $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor
get "vertexConsumer"(): $VertexConsumer
set "vertexConsumer"(value: $VertexConsumer$Type)
set "vertexSupplier"(value: $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor$Type)
set "renderTypeRaw"(value: $RenderType$Type)
set "formatRaw"(value: $VertexFormat$Type)
get "modularActors"(): $Optional<($HashMap<(any), ($Consumer<($VFXBuilders$WorldVFXBuilder)>)>)>
get "nextModularActor"(): $Optional<($Consumer<($VFXBuilders$WorldVFXBuilder)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VFXBuilders$WorldVFXBuilder$Type = ($VFXBuilders$WorldVFXBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VFXBuilders$WorldVFXBuilder_ = $VFXBuilders$WorldVFXBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/recipe/$IRecipeComponent" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IRecipeComponent {

 "matches"(arg0: $ItemStack$Type): boolean
 "getStack"(): $ItemStack
 "getCount"(): integer
 "isValid"(): boolean
 "getItem"(): $Item
 "getStacks"(): $List<($ItemStack)>
}

export namespace $IRecipeComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IRecipeComponent$Type = ($IRecipeComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IRecipeComponent_ = $IRecipeComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$WaterLoggedEntityBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$SimpleWaterloggedBlock, $SimpleWaterloggedBlock$Type} from "packages/net/minecraft/world/level/block/$SimpleWaterloggedBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$LodestoneEntityBlock, $LodestoneEntityBlock$Type} from "packages/team/lodestar/lodestone/systems/block/$LodestoneEntityBlock"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $WaterLoggedEntityBlock<T extends $LodestoneBlockEntity> extends $LodestoneEntityBlock<(T)> implements $SimpleWaterloggedBlock {
static readonly "WATERLOGGED": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "getFluidState"(arg0: $BlockState$Type): $FluidState
public "getPickupSound"(): $Optional<($SoundEvent)>
public "canPlaceLiquid"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Fluid$Type): boolean
public "placeLiquid"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $FluidState$Type): boolean
public "pickupBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ItemStack
public "getPickupSound"(arg0: $BlockState$Type): $Optional<($SoundEvent)>
get "pickupSound"(): $Optional<($SoundEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaterLoggedEntityBlock$Type<T> = ($WaterLoggedEntityBlock<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaterLoggedEntityBlock_<T> = $WaterLoggedEntityBlock$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/handlers/$ThrowawayBlockDataHandler" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$LodestoneDatagenBlockData, $LodestoneDatagenBlockData$Type} from "packages/team/lodestar/lodestone/systems/datagen/$LodestoneDatagenBlockData"
import {$InterModEnqueueEvent, $InterModEnqueueEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$InterModEnqueueEvent"
import {$LodestoneThrowawayBlockData, $LodestoneThrowawayBlockData$Type} from "packages/team/lodestar/lodestone/systems/block/$LodestoneThrowawayBlockData"
import {$LodestoneBlockProperties, $LodestoneBlockProperties$Type} from "packages/team/lodestar/lodestone/systems/block/$LodestoneBlockProperties"

export class $ThrowawayBlockDataHandler {
static "THROWAWAY_DATA_CACHE": $HashMap<($LodestoneBlockProperties), ($LodestoneThrowawayBlockData)>
static "DATAGEN_DATA_CACHE": $HashMap<($LodestoneBlockProperties), ($LodestoneDatagenBlockData)>

constructor()

public static "wipeCache"(arg0: $InterModEnqueueEvent$Type): void
public static "setRenderLayers"(arg0: $FMLClientSetupEvent$Type): void
set "renderLayers"(value: $FMLClientSetupEvent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThrowawayBlockDataHandler$Type = ($ThrowawayBlockDataHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThrowawayBlockDataHandler_ = $ThrowawayBlockDataHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventTypeRegistryEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$IModBusEvent, $IModBusEvent$Type} from "packages/net/minecraftforge/fml/event/$IModBusEvent"
import {$WorldEventType, $WorldEventType$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WorldEventType$EventInstanceSupplier, $WorldEventType$EventInstanceSupplier$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType$EventInstanceSupplier"

export class $WorldEventTypeRegistryEvent extends $Event implements $IModBusEvent {

constructor()

public "create"(arg0: $ResourceLocation$Type, arg1: $WorldEventType$EventInstanceSupplier$Type, arg2: boolean): $WorldEventType
public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventTypeRegistryEvent$Type = ($WorldEventTypeRegistryEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventTypeRegistryEvent_ = $WorldEventTypeRegistryEvent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneHorizontalBlock" {
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$HorizontalDirectionalBlock, $HorizontalDirectionalBlock$Type} from "packages/net/minecraft/world/level/block/$HorizontalDirectionalBlock"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $LodestoneHorizontalBlock extends $HorizontalDirectionalBlock {
static readonly "FACING": $DirectionProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneHorizontalBlock$Type = ($LodestoneHorizontalBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneHorizontalBlock_ = $LodestoneHorizontalBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneCommandRegistry" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"

export class $LodestoneCommandRegistry {

constructor()

public static "registerCommands"(arg0: $RegisterCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneCommandRegistry$Type = ($LodestoneCommandRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneCommandRegistry_ = $LodestoneCommandRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/builder/$AbstractParticleBuilder" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$SimpleParticleOptions, $SimpleParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $AbstractParticleBuilder<T extends $SimpleParticleOptions> {

constructor()

public "modifyColorData"(arg0: $Consumer$Type<($ColorParticleData$Type)>): $AbstractParticleBuilder<(T)>
public "setColorData"(arg0: $ColorParticleData$Type): $AbstractParticleBuilder<(T)>
public "modifyGravity"(arg0: $Function$Type<(float), ($Supplier$Type<(float)>)>): $AbstractParticleBuilder<(T)>
public "getScaleData"(): $GenericParticleData
public "setSpinData"(arg0: $SpinParticleData$Type): $AbstractParticleBuilder<(T)>
public "multiplyFriction"(arg0: float): $AbstractParticleBuilder<(T)>
public "multiplyGravity"(arg0: float): $AbstractParticleBuilder<(T)>
public "setScaleData"(arg0: $GenericParticleData$Type): $AbstractParticleBuilder<(T)>
public "getSpinData"(): $SpinParticleData
public "setGravityStrength"(arg0: $Supplier$Type<(float)>): $AbstractParticleBuilder<(T)>
public "setGravityStrength"(arg0: float): $AbstractParticleBuilder<(T)>
public "modifyLifetime"(arg0: $Function$Type<(integer), ($Supplier$Type<(integer)>)>): $AbstractParticleBuilder<(T)>
public "modifyFriction"(arg0: $Function$Type<(float), ($Supplier$Type<(float)>)>): $AbstractParticleBuilder<(T)>
public "multiplyLifetime"(arg0: float): $AbstractParticleBuilder<(T)>
public "setDiscardFunction"(arg0: $SimpleParticleOptions$ParticleDiscardFunctionType$Type): $AbstractParticleBuilder<(T)>
public "setLifeDelay"(arg0: integer): $AbstractParticleBuilder<(T)>
public "setLifeDelay"(arg0: $Supplier$Type<(integer)>): $AbstractParticleBuilder<(T)>
public "modifyLifeDelay"(arg0: $Function$Type<(integer), ($Supplier$Type<(integer)>)>): $AbstractParticleBuilder<(T)>
public "setSpritePicker"(arg0: $SimpleParticleOptions$ParticleSpritePicker$Type): $AbstractParticleBuilder<(T)>
public "multiplyLifeDelay"(arg0: float): $AbstractParticleBuilder<(T)>
public "getColorData"(): $ColorParticleData
public "setLifetime"(arg0: $Supplier$Type<(integer)>): $AbstractParticleBuilder<(T)>
public "setLifetime"(arg0: integer): $AbstractParticleBuilder<(T)>
public "getParticleOptions"(): T
public "setTransparencyData"(arg0: $GenericParticleData$Type): $AbstractParticleBuilder<(T)>
public "setFrictionStrength"(arg0: $Supplier$Type<(float)>): $AbstractParticleBuilder<(T)>
public "setFrictionStrength"(arg0: float): $AbstractParticleBuilder<(T)>
public "getTransparencyData"(): $GenericParticleData
set "colorData"(value: $ColorParticleData$Type)
get "scaleData"(): $GenericParticleData
set "spinData"(value: $SpinParticleData$Type)
set "scaleData"(value: $GenericParticleData$Type)
get "spinData"(): $SpinParticleData
set "gravityStrength"(value: $Supplier$Type<(float)>)
set "gravityStrength"(value: float)
set "discardFunction"(value: $SimpleParticleOptions$ParticleDiscardFunctionType$Type)
set "lifeDelay"(value: integer)
set "lifeDelay"(value: $Supplier$Type<(integer)>)
set "spritePicker"(value: $SimpleParticleOptions$ParticleSpritePicker$Type)
get "colorData"(): $ColorParticleData
set "lifetime"(value: $Supplier$Type<(integer)>)
set "lifetime"(value: integer)
get "particleOptions"(): T
set "transparencyData"(value: $GenericParticleData$Type)
set "frictionStrength"(value: $Supplier$Type<(float)>)
set "frictionStrength"(value: float)
get "transparencyData"(): $GenericParticleData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractParticleBuilder$Type<T> = ($AbstractParticleBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractParticleBuilder_<T> = $AbstractParticleBuilder$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$AbstractItemModelSmith$ItemModelSmithData" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LodestoneItemModelProvider, $LodestoneItemModelProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneItemModelProvider"

export class $AbstractItemModelSmith$ItemModelSmithData {
readonly "provider": $LodestoneItemModelProvider
readonly "consumer": $Consumer<($Supplier<(any)>)>

constructor(arg0: $LodestoneItemModelProvider$Type, arg1: $Consumer$Type<($Supplier$Type<(any)>)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractItemModelSmith$ItemModelSmithData$Type = ($AbstractItemModelSmith$ItemModelSmithData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractItemModelSmith$ItemModelSmithData_ = $AbstractItemModelSmith$ItemModelSmithData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/capability/$LodestoneCapabilityProvider" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/net/minecraftforge/common/util/$NonNullSupplier"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $LodestoneCapabilityProvider<C extends $INBTSerializable<($CompoundTag)>> implements $ICapabilityProvider, $INBTSerializable<($CompoundTag)> {

constructor(arg0: $Capability$Type<(C)>, arg1: $NonNullSupplier$Type<(C)>)

public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
public "getCapability"<T>(arg0: $Capability$Type<(T)>): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneCapabilityProvider$Type<C> = ($LodestoneCapabilityProvider<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneCapabilityProvider_<C> = $LodestoneCapabilityProvider$Type<(C)>;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$LodestoneFuelBlockItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneFuelBlockItem extends $BlockItem {
readonly "fuel": integer
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type, arg1: $Item$Properties$Type, arg2: integer)

public "getBurnTime"(arg0: $ItemStack$Type, arg1: $RecipeType$Type<(any)>): integer
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneFuelBlockItem$Type = ($LodestoneFuelBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneFuelBlockItem_ = $LodestoneFuelBlockItem$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestonePlacementFillerRegistry" {
import {$DimensionPlacementFilter, $DimensionPlacementFilter$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$DimensionPlacementFilter"
import {$ChancePlacementFilter, $ChancePlacementFilter$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$ChancePlacementFilter"
import {$PlacementModifier, $PlacementModifier$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifier"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $LodestonePlacementFillerRegistry {
static "CHANCE": $PlacementModifierType<($ChancePlacementFilter)>
static "DIMENSION": $PlacementModifierType<($DimensionPlacementFilter)>

constructor()

public static "register"<P extends $PlacementModifier>(arg0: string, arg1: $Codec$Type<(P)>): $PlacementModifierType<(P)>
public static "registerTypes"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestonePlacementFillerRegistry$Type = ($LodestonePlacementFillerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestonePlacementFillerRegistry_ = $LodestonePlacementFillerRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/command/arguments/$WorldEventTypeArgument" {
import {$Suggestions, $Suggestions$Type} from "packages/com/mojang/brigadier/suggestion/$Suggestions"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SuggestionsBuilder, $SuggestionsBuilder$Type} from "packages/com/mojang/brigadier/suggestion/$SuggestionsBuilder"
import {$WorldEventType, $WorldEventType$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType"
import {$ArgumentType, $ArgumentType$Type} from "packages/com/mojang/brigadier/arguments/$ArgumentType"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $WorldEventTypeArgument implements $ArgumentType<($WorldEventType)> {


public static "getEventType"(arg0: $CommandContext$Type<(any)>, arg1: string): $WorldEventType
public static "worldEventType"(): $WorldEventTypeArgument
public "listSuggestions"<S>(arg0: $CommandContext$Type<(S)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
public "getExamples"(): $Collection<(string)>
get "examples"(): $Collection<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventTypeArgument$Type = ($WorldEventTypeArgument);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventTypeArgument_ = $WorldEventTypeArgument$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneAxeItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$AxeItem, $AxeItem$Type} from "packages/net/minecraft/world/item/$AxeItem"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneAxeItem extends $AxeItem {
static "STRIPPABLES": $Map<($Block), ($Block)>
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: float, arg2: float, arg3: $Item$Properties$Type)

public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneAxeItem$Type = ($LodestoneAxeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneAxeItem_ = $LodestoneAxeItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleItemStackRetrievalKey" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ScreenParticleItemStackRetrievalKey {
readonly "isHotbarItem": boolean
readonly "isRenderedAfterItem": boolean
readonly "x": integer
readonly "y": integer

constructor(arg0: boolean, arg1: boolean, arg2: integer, arg3: integer)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleItemStackRetrievalKey$Type = ($ScreenParticleItemStackRetrievalKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleItemStackRetrievalKey_ = $ScreenParticleItemStackRetrievalKey$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$WorldEventType, $WorldEventType$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType"

export class $WorldEventInstance {
 "uuid": $UUID
 "type": $WorldEventType
 "level": $Level
 "discarded": boolean
 "dirty": boolean
 "frozen": boolean

constructor(arg0: $WorldEventType$Type)

public "start"(arg0: $Level$Type): void
public "end"(arg0: $Level$Type): void
public "isFrozen"(): boolean
public "sync"(arg0: $Level$Type): void
public static "sync"<T extends $WorldEventInstance>(arg0: T): void
public static "sync"<T extends $WorldEventInstance>(arg0: T, arg1: $ServerPlayer$Type): void
public "tick"(arg0: $Level$Type): void
public "getLevel"(): $Level
public "setDirty"(): void
public "synchronizeNBT"(): $CompoundTag
public "deserializeNBT"(arg0: $CompoundTag$Type): $WorldEventInstance
public "serializeNBT"(arg0: $CompoundTag$Type): $CompoundTag
get "frozen"(): boolean
get "level"(): $Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventInstance$Type = ($WorldEventInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventInstance_ = $WorldEventInstance$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectRenderer" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$FireEffectInstance, $FireEffectInstance$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectInstance"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $FireEffectRenderer<T extends $FireEffectInstance> {

constructor()

public "getFirstFlame"(): $Material
public "getSecondFlame"(): $Material
public "renderWorld"(arg0: T, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: $Camera$Type, arg4: $Entity$Type): void
public "renderScreen"(arg0: T, arg1: $Minecraft$Type, arg2: $PoseStack$Type): void
public "canRender"(arg0: T): boolean
get "firstFlame"(): $Material
get "secondFlame"(): $Material
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireEffectRenderer$Type<T> = ($FireEffectRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireEffectRenderer_<T> = $FireEffectRenderer$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleHolder" {
import {$ScreenParticle, $ScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/base/$ScreenParticle"
import {$LodestoneScreenParticleRenderType, $LodestoneScreenParticleRenderType$Type} from "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneScreenParticleRenderType"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ScreenParticleHolder {
readonly "particles": $Map<($LodestoneScreenParticleRenderType), ($ArrayList<($ScreenParticle)>)>

constructor()

public "isEmpty"(): boolean
public "tick"(): void
public "addFrom"(arg0: $ScreenParticleHolder$Type): void
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleHolder$Type = ($ScreenParticleHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleHolder_ = $ScreenParticleHolder$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestonePaintingRegistry" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$PaintingVariant, $PaintingVariant$Type} from "packages/net/minecraft/world/entity/decoration/$PaintingVariant"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"

export class $LodestonePaintingRegistry {
static readonly "PAINTING_MOTIVES": $DeferredRegister<($PaintingVariant)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestonePaintingRegistry$Type = ($LodestonePaintingRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestonePaintingRegistry_ = $LodestonePaintingRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$TextureSheetParticle, $TextureSheetParticle$Type} from "packages/net/minecraft/client/particle/$TextureSheetParticle"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$ParticleEngine$MutableSpriteSet, $ParticleEngine$MutableSpriteSet$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$MutableSpriteSet"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $LodestoneWorldParticle extends $TextureSheetParticle {
readonly "renderType": $ParticleRenderType
readonly "behavior": $LodestoneParticleBehavior
readonly "behaviorComponent": $LodestoneBehaviorComponent
readonly "renderLayer": $RenderHandler$LodestoneRenderLayer
readonly "shouldCull": boolean
readonly "spriteSet": $ParticleEngine$MutableSpriteSet
readonly "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
readonly "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
readonly "colorData": $ColorParticleData
readonly "transparencyData": $GenericParticleData
readonly "scaleData": $GenericParticleData
readonly "spinData": $SpinParticleData
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "lifeDelay": integer
 "x": double
 "y": double
 "z": double
 "xd": double
 "yd": double
 "zd": double
 "age": integer
 "rCol": float
 "gCol": float
 "bCol": float

constructor(arg0: $ClientLevel$Type, arg1: $WorldParticleOptions$Type, arg2: $ParticleEngine$MutableSpriteSet$Type, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double)

public "tick"(arg0: integer): void
public "setParticlePosition"(arg0: $Vec3$Type): void
public "getParticlePosition"(): $Vec3
public "getY"(): double
public "getZ"(): double
public "getX"(): double
public "m_6355_"(arg0: float): integer
public "getLifetime"(): integer
public "m_5970_"(): float
public "m_5951_"(): float
public "m_5950_"(): float
public "m_5952_"(): float
public "getParticleSpeed"(): $Vec3
public "getQuadSize"(arg0: float): float
public "getAge"(): integer
public "getRoll"(): float
public "getRenderType"(): $ParticleRenderType
public "tick"(): void
public "shouldCull"(): boolean
public "render"(arg0: $VertexConsumer$Type, arg1: $Camera$Type, arg2: float): void
public "getRed"(): float
public "getGreen"(): float
public "getBlue"(): float
public "getAlpha"(): float
public "pickSprite"(arg0: integer): void
public "getRandom"(): $RandomSource
public "getSpritePicker"(): $SimpleParticleOptions$ParticleSpritePicker
public "getVertexConsumer"(arg0: $VertexConsumer$Type): $VertexConsumer
public "pickColor"(arg0: float): void
public "getZMotion"(): double
public "getXOld"(): double
public "getYOld"(): double
public "getXMotion"(): double
public "setParticleSpeed"(arg0: $Vec3$Type): void
public "getORoll"(): float
public "getYMotion"(): double
public "getZOld"(): double
set "particlePosition"(value: $Vec3$Type)
get "particlePosition"(): $Vec3
get "y"(): double
get "z"(): double
get "x"(): double
get "lifetime"(): integer
get "particleSpeed"(): $Vec3
get "age"(): integer
get "roll"(): float
get "renderType"(): $ParticleRenderType
get "red"(): float
get "green"(): float
get "blue"(): float
get "alpha"(): float
get "random"(): $RandomSource
get "spritePicker"(): $SimpleParticleOptions$ParticleSpritePicker
get "zMotion"(): double
get "xOld"(): double
get "yOld"(): double
get "xMotion"(): double
set "particleSpeed"(value: $Vec3$Type)
get "oRoll"(): float
get "yMotion"(): double
get "zOld"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWorldParticle$Type = ($LodestoneWorldParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWorldParticle_ = $LodestoneWorldParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$RenderHelper" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $RenderHelper {
static readonly "FULL_BRIGHT": integer

constructor()

public static "midpoint"(arg0: $Vector4f$Type, arg1: $Vector4f$Type): $Vector4f
public static "getShader"(arg0: $RenderType$Type): $ShaderInstance
public static "getTransparencyShard"(arg0: $RenderType$Type): $RenderStateShard$TransparencyStateShard
public static "vertexPosColorUVLight"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: integer): void
public static "drawSteppedLineBetween"(arg0: $MultiBufferSource$Type, arg1: $PoseStack$Type, arg2: $List$Type<($Vec3$Type)>, arg3: float, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "drawSteppedLineBetween"(arg0: $MultiBufferSource$Type, arg1: $PoseStack$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: integer, arg5: float, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $Consumer$Type<($Vec3$Type)>): void
public static "vertexPosUV"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): void
public static "vertexPos"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float): void
public static "vertexPosUVLight"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: integer): void
public static "vertexPosColor"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): void
public static "vertexPosColorUV"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float): void
public static "worldPosToTexCoord"(arg0: $Vector3f$Type, arg1: $PoseStack$Type): $Vec2
public static "drawLineBetween"(arg0: $MultiBufferSource$Type, arg1: $PoseStack$Type, arg2: $Vec3$Type, arg3: $Vec3$Type, arg4: float, arg5: integer, arg6: integer, arg7: integer, arg8: integer): void
public static "parametricSphere"(arg0: float, arg1: float, arg2: float): $Vector3f
public static "perpendicularTrailPoints"(arg0: $Vector4f$Type, arg1: $Vector4f$Type, arg2: float): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHelper$Type = ($RenderHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHelper_ = $RenderHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneOptionRegistry" {
import {$OptionInstance, $OptionInstance$Type} from "packages/net/minecraft/client/$OptionInstance"
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"

export class $LodestoneOptionRegistry {
static readonly "SCREENSHAKE_INTENSITY": $OptionInstance<(double)>
static readonly "FIRE_OFFSET": $OptionInstance<(double)>

constructor()

public static "addOption"(arg0: $ScreenEvent$Init$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneOptionRegistry$Type = ($LodestoneOptionRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneOptionRegistry_ = $LodestoneOptionRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneSwordItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$SwordItem, $SwordItem$Type} from "packages/net/minecraft/world/item/$SwordItem"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneSwordItem extends $SwordItem {
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: $Item$Properties$Type)

public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneSwordItem$Type = ($LodestoneSwordItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneSwordItem_ = $LodestoneSwordItem$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/client/$LodestoneWorldEventRendererRegistry" {
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$WorldEventRenderer, $WorldEventRenderer$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventRenderer"
import {$WorldEventType, $WorldEventType$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType"

export class $LodestoneWorldEventRendererRegistry {
static "RENDERERS": $HashMap<($WorldEventType), ($WorldEventRenderer<($WorldEventInstance)>)>

constructor()

public static "registerRenderer"(arg0: $WorldEventType$Type, arg1: $WorldEventRenderer$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWorldEventRendererRegistry$Type = ($LodestoneWorldEventRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWorldEventRendererRegistry_ = $LodestoneWorldEventRendererRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/screenparticle/$ParticleEmitterHandler" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ParticleEmitterHandler$ItemParticleSupplier, $ParticleEmitterHandler$ItemParticleSupplier$Type} from "packages/team/lodestar/lodestone/handlers/screenparticle/$ParticleEmitterHandler$ItemParticleSupplier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ParticleEmitterHandler {
static readonly "EMITTERS": $Map<($Item), ($List<($ParticleEmitterHandler$ItemParticleSupplier)>)>

constructor()

public static "registerItemParticleEmitter"(arg0: $ParticleEmitterHandler$ItemParticleSupplier$Type, ...arg1: ($Item$Type)[]): void
public static "registerItemParticleEmitter"(arg0: $Item$Type, arg1: $ParticleEmitterHandler$ItemParticleSupplier$Type): void
public static "registerParticleEmitters"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleEmitterHandler$Type = ($ParticleEmitterHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleEmitterHandler_ = $ParticleEmitterHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$SparkParticleBehavior" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"

export class $SparkParticleBehavior implements $LodestoneParticleBehavior {


public "render"(arg0: $LodestoneWorldParticle$Type, arg1: $VertexConsumer$Type, arg2: $Camera$Type, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SparkParticleBehavior$Type = ($SparkParticleBehavior);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SparkParticleBehavior_ = $SparkParticleBehavior$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventRenderEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$WorldEventInstanceEvent, $WorldEventInstanceEvent$Type} from "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventInstanceEvent"
import {$WorldEventRenderer, $WorldEventRenderer$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $WorldEventRenderEvent extends $WorldEventInstanceEvent {

constructor()
constructor(arg0: $WorldEventInstance$Type, arg1: $WorldEventRenderer$Type<($WorldEventInstance$Type)>, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: float)

public "isCancelable"(): boolean
public "getPoseStack"(): $PoseStack
public "getMultiBufferSource"(): $MultiBufferSource
public "getRenderer"(): $WorldEventRenderer<($WorldEventInstance)>
public "getListenerList"(): $ListenerList
public "getPartialTicks"(): float
get "cancelable"(): boolean
get "poseStack"(): $PoseStack
get "multiBufferSource"(): $MultiBufferSource
get "renderer"(): $WorldEventRenderer<($WorldEventInstance)>
get "listenerList"(): $ListenerList
get "partialTicks"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventRenderEvent$Type = ($WorldEventRenderEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventRenderEvent_ = $WorldEventRenderEvent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockOptions" {
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $GhostBlockOptions {


public "at"(arg0: $BlockPos$Type): $GhostBlockOptions
public "at"(arg0: integer, arg1: integer, arg2: integer): $GhostBlockOptions
public static "create"(arg0: $Block$Type): $GhostBlockOptions
public static "create"(arg0: $BlockState$Type, arg1: $BlockPos$Type): $GhostBlockOptions
public static "create"(arg0: $BlockState$Type): $GhostBlockOptions
public static "create"(arg0: $Block$Type, arg1: $BlockPos$Type): $GhostBlockOptions
public "withColor"(arg0: float, arg1: float, arg2: float): $GhostBlockOptions
public "withColor"(arg0: $Color$Type): $GhostBlockOptions
public "withScale"(arg0: $Supplier$Type<(float)>): $GhostBlockOptions
public "withAlpha"(arg0: $Supplier$Type<(float)>): $GhostBlockOptions
public "withRenderType"(arg0: $RenderType$Type): $GhostBlockOptions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostBlockOptions$Type = ($GhostBlockOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostBlockOptions_ = $GhostBlockOptions$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneFireEffectRegistry" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$FireEffectType, $FireEffectType$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectType"

export class $LodestoneFireEffectRegistry {
static readonly "FIRE_TYPES": $HashMap<(string), ($FireEffectType)>

constructor()

public static "registerType"(arg0: $FireEffectType$Type): $FireEffectType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneFireEffectRegistry$Type = ($LodestoneFireEffectRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneFireEffectRegistry_ = $LodestoneFireEffectRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/network/worldevent/$UpdateWorldEventPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LodestoneClientPacket, $LodestoneClientPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneClientPacket"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $UpdateWorldEventPacket extends $LodestoneClientPacket {

constructor(arg0: $UUID$Type, arg1: $CompoundTag$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $UpdateWorldEventPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateWorldEventPacket$Type = ($UpdateWorldEventPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateWorldEventPacket_ = $UpdateWorldEventPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$ShaderUniformHandler" {
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"

export interface $ShaderUniformHandler {

 "updateShaderData"(arg0: $ShaderInstance$Type): void

(arg0: $ShaderInstance$Type): void
}

export namespace $ShaderUniformHandler {
const LUMITRANSPARENT: $ShaderUniformHandler
const DEPTH_FADE: $ShaderUniformHandler
const LUMITRANSPARENT_DEPTH_FADE: $ShaderUniformHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderUniformHandler$Type = ($ShaderUniformHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderUniformHandler_ = $ShaderUniformHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/recipe/$WrappedItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IRecipeComponent, $IRecipeComponent$Type} from "packages/team/lodestar/lodestone/systems/recipe/$IRecipeComponent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $WrappedItem implements $IRecipeComponent {
readonly "stack": $ItemStack

constructor(arg0: $ItemStack$Type)

public "matches"(arg0: $ItemStack$Type): boolean
public "getStack"(): $ItemStack
public "getCount"(): integer
public "getItem"(): $Item
public "getStacks"(): $List<($ItemStack)>
public "isValid"(): boolean
get "stack"(): $ItemStack
get "count"(): integer
get "item"(): $Item
get "stacks"(): $List<($ItemStack)>
get "valid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrappedItem$Type = ($WrappedItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrappedItem_ = $WrappedItem$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$EntityHelper" {
import {$EntityHelper$PastPosition, $EntityHelper$PastPosition$Type} from "packages/team/lodestar/lodestone/helpers/$EntityHelper$PastPosition"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $EntityHelper {

constructor()

public static "extendEffect"(arg0: $MobEffectInstance$Type, arg1: $LivingEntity$Type, arg2: integer): void
public static "extendEffect"(arg0: $MobEffectInstance$Type, arg1: $LivingEntity$Type, arg2: integer, arg3: integer): void
public static "shortenEffect"(arg0: $MobEffectInstance$Type, arg1: $LivingEntity$Type, arg2: integer): void
public static "syncEffect"(arg0: $MobEffectInstance$Type, arg1: $LivingEntity$Type): void
public static "trackPastPositions"(arg0: $ArrayList$Type<($EntityHelper$PastPosition$Type)>, arg1: $Vec3$Type, arg2: float): void
public static "amplifyEffect"(arg0: $MobEffectInstance$Type, arg1: $LivingEntity$Type, arg2: integer, arg3: integer): void
public static "amplifyEffect"(arg0: $MobEffectInstance$Type, arg1: $LivingEntity$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHelper$Type = ($EntityHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHelper_ = $EntityHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/type/$AbstractLodestoneParticleType" {
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AbstractLodestoneParticleType<T extends $WorldParticleOptions> extends $ParticleType<(T)> {

constructor()

public static "genericCodec"<K extends $WorldParticleOptions>(arg0: $ParticleType$Type<(K)>): $Codec<(K)>
public "codec"(): $Codec<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractLodestoneParticleType$Type<T> = ($AbstractLodestoneParticleType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractLodestoneParticleType_<T> = $AbstractLodestoneParticleType$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/postprocess/$ShaderDataBuffer" {
import {$EffectInstance, $EffectInstance$Type} from "packages/net/minecraft/client/renderer/$EffectInstance"

export class $ShaderDataBuffer {

constructor()

public "apply"(arg0: $EffectInstance$Type, arg1: string): void
public "destroy"(): void
public "generate"(arg0: long): void
public "upload"(arg0: (float)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderDataBuffer$Type = ($ShaderDataBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderDataBuffer_ = $ShaderDataBuffer$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions" {
import {$ScreenParticleType, $ScreenParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneScreenParticleRenderType, $LodestoneScreenParticleRenderType$Type} from "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneScreenParticleRenderType"
import {$SimpleParticleOptions, $SimpleParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$GenericScreenParticle, $GenericScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$GenericScreenParticle"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $ScreenParticleOptions extends $SimpleParticleOptions {
readonly "type": $ScreenParticleType<(any)>
 "renderType": $LodestoneScreenParticleRenderType
 "actor": $Consumer<($GenericScreenParticle)>
 "tracksStack": boolean
 "stackTrackXOffset": double
 "stackTrackYOffset": double
static readonly "DEFAULT_COLOR": $ColorParticleData
static readonly "DEFAULT_SPIN": $SpinParticleData
static readonly "DEFAULT_GENERIC": $GenericParticleData
 "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
 "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
 "colorData": $ColorParticleData
 "transparencyData": $GenericParticleData
 "scaleData": $GenericParticleData
 "spinData": $SpinParticleData
 "lifetimeSupplier": $Supplier<(integer)>
 "lifeDelaySupplier": $Supplier<(integer)>
 "gravityStrengthSupplier": $Supplier<(float)>
 "frictionStrengthSupplier": $Supplier<(float)>

constructor(arg0: $ScreenParticleType$Type<(any)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleOptions$Type = ($ScreenParticleOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleOptions_ = $ScreenParticleOptions$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/client/$LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$ShaderHolder, $ShaderHolder$Type} from "packages/team/lodestar/lodestone/systems/rendering/shader/$ShaderHolder"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$RenderType$CompositeState$CompositeStateBuilder, $RenderType$CompositeState$CompositeStateBuilder$Type} from "packages/net/minecraft/client/renderer/$RenderType$CompositeState$CompositeStateBuilder"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"

export class $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder extends $RenderType$CompositeState$CompositeStateBuilder {


public "setTransparencyState"(arg0: $RenderStateShard$TransparencyStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "replaceVertexFormat"(arg0: $VertexFormat$Mode$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setLightmapState"(arg0: $RenderStateShard$LightmapStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setTextureState"(arg0: $ResourceLocation$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setTextureState"(arg0: $RenderStateShard$EmptyTextureStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setShaderState"(arg0: $ShaderHolder$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setShaderState"(arg0: $RenderStateShard$ShaderStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setCullState"(arg0: $RenderStateShard$CullStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setLayeringState"(arg0: $RenderStateShard$LayeringStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setOverlayState"(arg0: $RenderStateShard$OverlayStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setOutputState"(arg0: $RenderStateShard$OutputStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setDepthTestState"(arg0: $RenderStateShard$DepthTestStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setTexturingState"(arg0: $RenderStateShard$TexturingStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setLineState"(arg0: $RenderStateShard$LineStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
public "setWriteMaskState"(arg0: $RenderStateShard$WriteMaskStateShard$Type): $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder
set "transparencyState"(value: $RenderStateShard$TransparencyStateShard$Type)
set "lightmapState"(value: $RenderStateShard$LightmapStateShard$Type)
set "textureState"(value: $ResourceLocation$Type)
set "textureState"(value: $RenderStateShard$EmptyTextureStateShard$Type)
set "shaderState"(value: $ShaderHolder$Type)
set "shaderState"(value: $RenderStateShard$ShaderStateShard$Type)
set "cullState"(value: $RenderStateShard$CullStateShard$Type)
set "layeringState"(value: $RenderStateShard$LayeringStateShard$Type)
set "overlayState"(value: $RenderStateShard$OverlayStateShard$Type)
set "outputState"(value: $RenderStateShard$OutputStateShard$Type)
set "depthTestState"(value: $RenderStateShard$DepthTestStateShard$Type)
set "texturingState"(value: $RenderStateShard$TexturingStateShard$Type)
set "lineState"(value: $RenderStateShard$LineStateShard$Type)
set "writeMaskState"(value: $RenderStateShard$WriteMaskStateShard$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type = ($LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder_ = $LodestoneRenderTypeRegistry$LodestoneCompositeStateBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/base/$QuadScreenParticle" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ScreenParticle, $ScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/base/$ScreenParticle"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"

export class $QuadScreenParticle extends $ScreenParticle {
readonly "level": $ClientLevel
 "xOld": double
 "yOld": double
 "x": double
 "y": double
 "xMotion": double
 "yMotion": double
 "xMoved": double
 "yMoved": double
 "removed": boolean
readonly "random": $RandomSource
 "age": integer
 "lifetime": integer
 "gravity": float
 "size": float
 "rCol": float
 "gCol": float
 "bCol": float
 "alpha": float
 "roll": float
 "oRoll": float
 "friction": float


public "render"(arg0: $BufferBuilder$Type): void
public "getQuadSize"(arg0: float): float
public "getQuadZPosition"(): float
get "quadZPosition"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuadScreenParticle$Type = ($QuadScreenParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuadScreenParticle_ = $QuadScreenParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"

export interface $LodestoneParticleBehavior {

 "render"(arg0: $LodestoneWorldParticle$Type, arg1: $VertexConsumer$Type, arg2: $Camera$Type, arg3: float): void
 "getComponent"(arg0: $LodestoneBehaviorComponent$Type): $LodestoneBehaviorComponent
}

export namespace $LodestoneParticleBehavior {
const BILLBOARD: $LodestoneParticleBehavior
const SPARK: $LodestoneParticleBehavior
const DIRECTIONAL: $LodestoneParticleBehavior
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneParticleBehavior$Type = ($LodestoneParticleBehavior);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneParticleBehavior_ = $LodestoneParticleBehavior$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$LodestoneFuelItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneFuelItem extends $Item {
readonly "fuel": integer
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type, arg1: integer)

public "getBurnTime"(arg0: $ItemStack$Type, arg1: $RecipeType$Type<(any)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneFuelItem$Type = ($LodestoneFuelItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneFuelItem_ = $LodestoneFuelItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/magic/$MagicSwordItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$LodestoneSwordItem, $LodestoneSwordItem$Type} from "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneSwordItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MagicSwordItem extends $LodestoneSwordItem {
readonly "magicDamage": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: float, arg4: $Item$Properties$Type)

public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MagicSwordItem$Type = ($MagicSwordItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MagicSwordItem_ = $MagicSwordItem$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$WorldEventHandler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$LodestoneWorldDataCapability, $LodestoneWorldDataCapability$Type} from "packages/team/lodestar/lodestone/capability/$LodestoneWorldDataCapability"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"

export class $WorldEventHandler {

constructor()

public static "tick"(arg0: $Level$Type): void
public static "addWorldEvent"<T extends $WorldEventInstance>(arg0: $Level$Type, arg1: T): T
public static "addWorldEvent"<T extends $WorldEventInstance>(arg0: $Level$Type, arg1: boolean, arg2: T): T
public static "worldTick"(arg0: $TickEvent$LevelTickEvent$Type): void
public static "playerJoin"(arg0: $EntityJoinLevelEvent$Type): void
public static "deserializeNBT"(arg0: $LodestoneWorldDataCapability$Type, arg1: $CompoundTag$Type): void
public static "serializeNBT"(arg0: $LodestoneWorldDataCapability$Type, arg1: $CompoundTag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventHandler$Type = ($WorldEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventHandler_ = $WorldEventHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneTerrainParticleType" {
import {$LodestoneTerrainParticleOptions, $LodestoneTerrainParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$LodestoneTerrainParticleOptions"
import {$AbstractLodestoneParticleType, $AbstractLodestoneParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$AbstractLodestoneParticleType"

export class $LodestoneTerrainParticleType extends $AbstractLodestoneParticleType<($LodestoneTerrainParticleOptions)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTerrainParticleType$Type = ($LodestoneTerrainParticleType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTerrainParticleType_ = $LodestoneTerrainParticleType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$DirectionalBehaviorComponent" {
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $DirectionalBehaviorComponent implements $LodestoneBehaviorComponent {

constructor()
constructor(arg0: $Vec3$Type)

public "getBehaviorType"(): $LodestoneParticleBehavior
public "getDirection"(arg0: $LodestoneWorldParticle$Type): $Vec3
public "tick"(arg0: $LodestoneWorldParticle$Type): void
get "behaviorType"(): $LodestoneParticleBehavior
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionalBehaviorComponent$Type = ($DirectionalBehaviorComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionalBehaviorComponent_ = $DirectionalBehaviorComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$StateShards" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$TextureStateShard, $RenderStateShard$TextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TextureStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ColorLogicStateShard, $RenderStateShard$ColorLogicStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ColorLogicStateShard"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard, $RenderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"

export class $StateShards extends $RenderStateShard {
static readonly "ADDITIVE_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "NORMAL_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "VIEW_SCALE_Z_EPSILON": float
static readonly "MAX_ENCHANTMENT_GLINT_SPEED_MILLIS": double
readonly "name": string
 "setupState": $Runnable
static readonly "NO_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "LIGHTNING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "GLINT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "CRUMBLING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "TRANSLUCENT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "NO_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_MIPPED_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_MOVING_BLOCK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_NO_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_BEACON_BEAM_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_DECAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_NO_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SHADOW_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_ALPHA_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_EYES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENERGY_SWIRL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LEASH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_WATER_MASK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LIGHTNING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRIPWIRE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_PORTAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_GATEWAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LINES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "BLOCK_SHEET_MIPPED": $RenderStateShard$TextureStateShard
static readonly "BLOCK_SHEET": $RenderStateShard$TextureStateShard
static readonly "NO_TEXTURE": $RenderStateShard$EmptyTextureStateShard
static readonly "DEFAULT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "ENTITY_GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "NO_LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "NO_OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "CULL": $RenderStateShard$CullStateShard
static readonly "NO_CULL": $RenderStateShard$CullStateShard
static readonly "NO_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "EQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "LEQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "GREATER_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "COLOR_DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "COLOR_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "NO_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "POLYGON_OFFSET_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "VIEW_OFFSET_Z_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "MAIN_TARGET": $RenderStateShard$OutputStateShard
static readonly "OUTLINE_TARGET": $RenderStateShard$OutputStateShard
static readonly "TRANSLUCENT_TARGET": $RenderStateShard$OutputStateShard
static readonly "PARTICLES_TARGET": $RenderStateShard$OutputStateShard
static readonly "WEATHER_TARGET": $RenderStateShard$OutputStateShard
static readonly "CLOUDS_TARGET": $RenderStateShard$OutputStateShard
static readonly "ITEM_ENTITY_TARGET": $RenderStateShard$OutputStateShard
static readonly "DEFAULT_LINE": $RenderStateShard$LineStateShard
static readonly "NO_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard
static readonly "OR_REVERSE_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard

constructor(arg0: string, arg1: $Runnable$Type, arg2: $Runnable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StateShards$Type = ($StateShards);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StateShards_ = $StateShards$Type;
}}
declare module "packages/team/lodestar/lodestone/command/$UnfreezeActiveWorldEventsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $UnfreezeActiveWorldEventsCommand {

constructor()

public static "register"(): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnfreezeActiveWorldEventsCommand$Type = ($UnfreezeActiveWorldEventsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnfreezeActiveWorldEventsCommand_ = $UnfreezeActiveWorldEventsCommand$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith$ModularSmithStateSupplier" {
import {$LodestoneBlockStateProvider, $LodestoneBlockStateProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockStateProvider"
import {$ModularBlockStateSmith$ModelFileSupplier, $ModularBlockStateSmith$ModelFileSupplier$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith$ModelFileSupplier"
import {$AbstractBlockStateSmith$StateFunction, $AbstractBlockStateSmith$StateFunction$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith$StateFunction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $ModularBlockStateSmith$ModularSmithStateSupplier<T extends $Block> {

 "act"(arg0: T, arg1: $LodestoneBlockStateProvider$Type, arg2: $AbstractBlockStateSmith$StateFunction$Type<(T)>, arg3: $ModularBlockStateSmith$ModelFileSupplier$Type): void

(arg0: T, arg1: $LodestoneBlockStateProvider$Type, arg2: $AbstractBlockStateSmith$StateFunction$Type<(T)>, arg3: $ModularBlockStateSmith$ModelFileSupplier$Type): void
}

export namespace $ModularBlockStateSmith$ModularSmithStateSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModularBlockStateSmith$ModularSmithStateSupplier$Type<T> = ($ModularBlockStateSmith$ModularSmithStateSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModularBlockStateSmith$ModularSmithStateSupplier_<T> = $ModularBlockStateSmith$ModularSmithStateSupplier$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/$FrameSetParticle" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$ParticleEngine$MutableSpriteSet, $ParticleEngine$MutableSpriteSet$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$MutableSpriteSet"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $FrameSetParticle extends $LodestoneWorldParticle {
 "frameSet": $ArrayList<(integer)>
readonly "renderType": $ParticleRenderType
readonly "behavior": $LodestoneParticleBehavior
readonly "behaviorComponent": $LodestoneBehaviorComponent
readonly "renderLayer": $RenderHandler$LodestoneRenderLayer
readonly "shouldCull": boolean
readonly "spriteSet": $ParticleEngine$MutableSpriteSet
readonly "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
readonly "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
readonly "colorData": $ColorParticleData
readonly "transparencyData": $GenericParticleData
readonly "scaleData": $GenericParticleData
readonly "spinData": $SpinParticleData
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "lifeDelay": integer
 "x": double
 "y": double
 "z": double
 "xd": double
 "yd": double
 "zd": double
 "age": integer
 "rCol": float
 "gCol": float
 "bCol": float

constructor(arg0: $ClientLevel$Type, arg1: $WorldParticleOptions$Type, arg2: $ParticleEngine$MutableSpriteSet$Type, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double)

public "tick"(): void
public "getSpritePicker"(): $SimpleParticleOptions$ParticleSpritePicker
get "spritePicker"(): $SimpleParticleOptions$ParticleSpritePicker
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrameSetParticle$Type = ($FrameSetParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrameSetParticle_ = $FrameSetParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$SpinParticleDataBuilder, $SpinParticleDataBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleDataBuilder"

export class $SpinParticleData extends $GenericParticleData {
readonly "spinOffset": float
readonly "startingValue": float
readonly "middleValue": float
readonly "endingValue": float
readonly "coefficient": float
readonly "startToMiddleEasing": $Easing
readonly "middleToEndEasing": $Easing
 "valueMultiplier": float
 "coefficientMultiplier": float


public static "create"(arg0: float, arg1: float): $SpinParticleDataBuilder
public static "create"(arg0: float): $SpinParticleDataBuilder
public static "create"(arg0: float, arg1: float, arg2: float): $SpinParticleDataBuilder
public "overrideCoefficientMultiplier"(arg0: float): $SpinParticleData
public static "createRandomDirection"(arg0: $RandomSource$Type, arg1: float, arg2: float, arg3: float): $SpinParticleDataBuilder
public static "createRandomDirection"(arg0: $RandomSource$Type, arg1: float, arg2: float): $SpinParticleDataBuilder
public static "createRandomDirection"(arg0: $RandomSource$Type, arg1: float): $SpinParticleDataBuilder
public "overrideValueMultiplier"(arg0: float): $SpinParticleData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpinParticleData$Type = ($SpinParticleData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpinParticleData_ = $SpinParticleData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureLoader" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$RegisterTextureAtlasSpriteLoadersEvent, $RegisterTextureAtlasSpriteLoadersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterTextureAtlasSpriteLoadersEvent"
import {$LodestoneTextureLoader$ColorLerp, $LodestoneTextureLoader$ColorLerp$Type} from "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureLoader$ColorLerp"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LodestoneTextureLoader$TextureModifier, $LodestoneTextureLoader$TextureModifier$Type} from "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureLoader$TextureModifier"

export class $LodestoneTextureLoader {

constructor()

public static "applyGrayscale"(arg0: $NativeImage$Type): $NativeImage
public static "applyMultiColorGradient"(arg0: $Easing$Type, arg1: $NativeImage$Type, arg2: $LodestoneTextureLoader$ColorLerp$Type, ...arg3: ($Color$Type)[]): $NativeImage
public static "registerTextureLoader"(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $LodestoneTextureLoader$TextureModifier$Type, arg4: $RegisterTextureAtlasSpriteLoadersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTextureLoader$Type = ($LodestoneTextureLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTextureLoader_ = $LodestoneTextureLoader$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/trail/$TrailPointBuilder" {
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$TrailPoint, $TrailPoint$Type} from "packages/team/lodestar/lodestone/systems/rendering/trail/$TrailPoint"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $TrailPointBuilder {
readonly "trailLength": $Supplier<(integer)>

constructor(arg0: $Supplier$Type<(integer)>)

public static "create"(arg0: $Supplier$Type<(integer)>): $TrailPointBuilder
public static "create"(arg0: integer): $TrailPointBuilder
public "build"(): $List<($Vector4f)>
public "tickTrailPoints"(): $TrailPointBuilder
public "getTrailPoints"(): $List<($TrailPoint)>
public "getTrailPoints"(arg0: float): $List<($TrailPoint)>
public "addTrailPoint"(arg0: $Vec3$Type): $TrailPointBuilder
public "addTrailPoint"(arg0: $TrailPoint$Type): $TrailPointBuilder
get "trailPoints"(): $List<($TrailPoint)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrailPointBuilder$Type = ($TrailPointBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrailPointBuilder_ = $TrailPointBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/postprocess/$PostProcessor" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Uniform, $Uniform$Type} from "packages/com/mojang/blaze3d/shaders/$Uniform"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PostProcessor {
static readonly "COMMON_UNIFORMS": $Collection<($Pair<(string), ($Consumer<($Uniform)>)>)>
static "viewModelStack": $PoseStack

constructor()

public "init"(): void
public "isActive"(): boolean
public "resize"(arg0: integer, arg1: integer): void
public "loadPostChain"(): void
public "setActive"(arg0: boolean): void
public "afterProcess"(): void
public "beforeProcess"(arg0: $PoseStack$Type): void
public "copyDepthBuffer"(): void
public "getPostChainLocation"(): $ResourceLocation
public "applyPostProcess"(): void
get "active"(): boolean
set "active"(value: boolean)
get "postChainLocation"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostProcessor$Type = ($PostProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostProcessor_ = $PostProcessor$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/options/$LodestoneTerrainParticleOptions" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $LodestoneTerrainParticleOptions extends $WorldParticleOptions {
readonly "blockState": $BlockState
readonly "blockPos": $BlockPos
readonly "type": $ParticleType<(any)>
 "behavior": $LodestoneParticleBehavior
 "behaviorComponent": $LodestoneBehaviorComponent
 "renderType": $ParticleRenderType
 "renderLayer": $RenderHandler$LodestoneRenderLayer
 "shouldCull": boolean
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "spawnActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "noClip": boolean
static readonly "DEFAULT_COLOR": $ColorParticleData
static readonly "DEFAULT_SPIN": $SpinParticleData
static readonly "DEFAULT_GENERIC": $GenericParticleData
 "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
 "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
 "colorData": $ColorParticleData
 "transparencyData": $GenericParticleData
 "scaleData": $GenericParticleData
 "spinData": $SpinParticleData
 "lifetimeSupplier": $Supplier<(integer)>
 "lifeDelaySupplier": $Supplier<(integer)>
 "gravityStrengthSupplier": $Supplier<(float)>
 "frictionStrengthSupplier": $Supplier<(float)>

constructor(arg0: $RegistryObject$Type<(any)>, arg1: $BlockState$Type)
constructor(arg0: $ParticleType$Type<($LodestoneTerrainParticleOptions$Type)>, arg1: $BlockState$Type)
constructor(arg0: $RegistryObject$Type<(any)>, arg1: $BlockState$Type, arg2: $BlockPos$Type)
constructor(arg0: $ParticleType$Type<($LodestoneTerrainParticleOptions$Type)>, arg1: $BlockState$Type, arg2: $BlockPos$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTerrainParticleOptions$Type = ($LodestoneTerrainParticleOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTerrainParticleOptions_ = $LodestoneTerrainParticleOptions$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$ExtrudingSparkBehaviorComponent" {
import {$SparkBehaviorComponent, $SparkBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$SparkBehaviorComponent"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $ExtrudingSparkBehaviorComponent extends $SparkBehaviorComponent {

constructor(arg0: $GenericParticleData$Type)
constructor()

public "sparkEnd"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
public "sparkStart"(arg0: $Vec3$Type, arg1: $Vec3$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtrudingSparkBehaviorComponent$Type = ($ExtrudingSparkBehaviorComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtrudingSparkBehaviorComponent_ = $ExtrudingSparkBehaviorComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$ItemEventHandler" {
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"

export class $ItemEventHandler {

constructor()

public static "respondToHurt"(arg0: $LivingHurtEvent$Type): void
public static "respondToDeath"(arg0: $LivingDeathEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEventHandler$Type = ($ItemEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEventHandler_ = $ItemEventHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockRenderer" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GhostBlockOptions, $GhostBlockOptions$Type} from "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockOptions"

export class $GhostBlockRenderer {
static readonly "STANDARD": $GhostBlockRenderer
static readonly "TRANSPARENT": $GhostBlockRenderer

constructor()

public static "standard"(): $GhostBlockRenderer
public "render"(arg0: $PoseStack$Type, arg1: $GhostBlockOptions$Type): void
public static "transparent"(): $GhostBlockRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostBlockRenderer$Type = ($GhostBlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostBlockRenderer_ = $GhostBlockRenderer$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$EntryPlacementPredicate" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $LodestoneBlockFiller$EntryPlacementPredicate {

 "canPlace"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean

(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
}

export namespace $LodestoneBlockFiller$EntryPlacementPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockFiller$EntryPlacementPredicate$Type = ($LodestoneBlockFiller$EntryPlacementPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller$EntryPlacementPredicate_ = $LodestoneBlockFiller$EntryPlacementPredicate$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneLeavesBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LeavesBlock, $LeavesBlock$Type} from "packages/net/minecraft/world/level/block/$LeavesBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockColors, $BlockColors$Type} from "packages/net/minecraft/client/color/block/$BlockColors"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$IForgeBlock, $IForgeBlock$Type} from "packages/net/minecraftforge/common/extensions/$IForgeBlock"
import {$IPlantable, $IPlantable$Type} from "packages/net/minecraftforge/common/$IPlantable"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $LodestoneLeavesBlock extends $LeavesBlock implements $IForgeBlock {
readonly "minColor": $Color
readonly "maxColor": $Color
static readonly "DECAY_DISTANCE": integer
static readonly "DISTANCE": $IntegerProperty
static readonly "PERSISTENT": $BooleanProperty
static readonly "WATERLOGGED": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type, arg1: $Color$Type, arg2: $Color$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "getColorProperty"(): $IntegerProperty
public static "registerSimpleGradientColors"(arg0: $BlockColors$Type, arg1: $LodestoneLeavesBlock$Type): void
public "canSustainPlant"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type, arg4: $IPlantable$Type): boolean
public "onNeighborChange"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $BlockPos$Type): void
get "colorProperty"(): $IntegerProperty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneLeavesBlock$Type = ($LodestoneLeavesBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneLeavesBlock_ = $LodestoneLeavesBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$BlockStateEntryBuilder" {
import {$LodestoneBlockFiller$BlockStateEntry, $LodestoneBlockFiller$BlockStateEntry$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$BlockStateEntry"
import {$LodestoneBlockFiller$EntryDiscardPredicate, $LodestoneBlockFiller$EntryDiscardPredicate$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$EntryDiscardPredicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LodestoneBlockFiller$EntryPlacementPredicate, $LodestoneBlockFiller$EntryPlacementPredicate$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$EntryPlacementPredicate"

export class $LodestoneBlockFiller$BlockStateEntryBuilder {

constructor(arg0: $BlockState$Type)

public "build"(): $LodestoneBlockFiller$BlockStateEntry
public "setPlacementPredicate"(arg0: $LodestoneBlockFiller$EntryPlacementPredicate$Type): $LodestoneBlockFiller$BlockStateEntryBuilder
public "setDiscardPredicate"(arg0: $LodestoneBlockFiller$EntryDiscardPredicate$Type): $LodestoneBlockFiller$BlockStateEntryBuilder
public "setForcePlace"(): $LodestoneBlockFiller$BlockStateEntryBuilder
public "setForcePlace"(arg0: boolean): $LodestoneBlockFiller$BlockStateEntryBuilder
set "placementPredicate"(value: $LodestoneBlockFiller$EntryPlacementPredicate$Type)
set "discardPredicate"(value: $LodestoneBlockFiller$EntryDiscardPredicate$Type)
set "forcePlace"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockFiller$BlockStateEntryBuilder$Type = ($LodestoneBlockFiller$BlockStateEntryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller$BlockStateEntryBuilder_ = $LodestoneBlockFiller$BlockStateEntryBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders" {
import {$VFXBuilders$ScreenVFXBuilder, $VFXBuilders$ScreenVFXBuilder$Type} from "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$ScreenVFXBuilder"
import {$VFXBuilders$WorldVFXBuilder, $VFXBuilders$WorldVFXBuilder$Type} from "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$WorldVFXBuilder"

export class $VFXBuilders {

constructor()

public static "createScreen"(): $VFXBuilders$ScreenVFXBuilder
public static "createWorld"(): $VFXBuilders$WorldVFXBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VFXBuilders$Type = ($VFXBuilders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VFXBuilders_ = $VFXBuilders$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/textureloader/$LodestoneTextureLoader$TextureModifier" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export interface $LodestoneTextureLoader$TextureModifier {

 "modifyTexture"(arg0: $NativeImage$Type): $NativeImage

(arg0: $NativeImage$Type): $NativeImage
}

export namespace $LodestoneTextureLoader$TextureModifier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTextureLoader$TextureModifier$Type = ($LodestoneTextureLoader$TextureModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTextureLoader$TextureModifier_ = $LodestoneTextureLoader$TextureModifier$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/sound/$CachedBlockEntitySoundInstance" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$LodestoneBlockEntitySoundInstance, $LodestoneBlockEntitySoundInstance$Type} from "packages/team/lodestar/lodestone/systems/sound/$LodestoneBlockEntitySoundInstance"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"

export class $CachedBlockEntitySoundInstance<T extends $LodestoneBlockEntity> extends $LodestoneBlockEntitySoundInstance<(T)> {
 "blockEntity": T

constructor(arg0: T, arg1: $Supplier$Type<($SoundEvent$Type)>, arg2: float, arg3: float)

public "tick"(): void
public static "playSound"(arg0: $LodestoneBlockEntity$Type, arg1: $CachedBlockEntitySoundInstance$Type<(any)>): void
public static "createUnseededRandom"(): $RandomSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedBlockEntitySoundInstance$Type<T> = ($CachedBlockEntitySoundInstance<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedBlockEntitySoundInstance_<T> = $CachedBlockEntitySoundInstance$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$LodestoneArmorItem" {
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Equipable, $Equipable$Type} from "packages/net/minecraft/world/item/$Equipable"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$ArmorItem, $ArmorItem$Type} from "packages/net/minecraft/world/item/$ArmorItem"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$DispenseItemBehavior, $DispenseItemBehavior$Type} from "packages/net/minecraft/core/dispenser/$DispenseItemBehavior"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$ArmorItem$Type, $ArmorItem$Type$Type} from "packages/net/minecraft/world/item/$ArmorItem$Type"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LodestoneArmorItem extends $ArmorItem {
static readonly "DISPENSE_ITEM_BEHAVIOR": $DispenseItemBehavior
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $ArmorMaterial$Type, arg1: $ArmorItem$Type$Type, arg2: $Item$Properties$Type)

public "getTexture"(): string
public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "getArmorTexture"(arg0: $ItemStack$Type, arg1: $Entity$Type, arg2: $EquipmentSlot$Type, arg3: string): string
public "getTextureLocation"(): string
public "createExtraAttributes"(arg0: $ArmorItem$Type$Type): $Multimap<($Attribute), ($AttributeModifier)>
public static "get"(arg0: $ItemStack$Type): $Equipable
get "texture"(): string
get "textureLocation"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneArmorItem$Type = ($LodestoneArmorItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneArmorItem_ = $LodestoneArmorItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$EntryDiscardPredicate" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $LodestoneBlockFiller$EntryDiscardPredicate {

 "shouldDiscard"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean

(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): boolean
}

export namespace $LodestoneBlockFiller$EntryDiscardPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockFiller$EntryDiscardPredicate$Type = ($LodestoneBlockFiller$EntryDiscardPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller$EntryDiscardPredicate_ = $LodestoneBlockFiller$EntryDiscardPredicate$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventTickEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$WorldEventInstanceEvent, $WorldEventInstanceEvent$Type} from "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventInstanceEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $WorldEventTickEvent extends $WorldEventInstanceEvent {

constructor()
constructor(arg0: $WorldEventInstance$Type, arg1: $Level$Type)

public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventTickEvent$Type = ($WorldEventTickEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventTickEvent_ = $WorldEventTickEvent$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$NBTHelper$TagFilter" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

export class $NBTHelper$TagFilter {
readonly "filters": $ArrayList<(string)>
 "isWhitelist": boolean

constructor(...arg0: (string)[])

public "setWhitelist"(): $NBTHelper$TagFilter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTHelper$TagFilter$Type = ($NBTHelper$TagFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTHelper$TagFilter_ = $NBTHelper$TagFilter$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/shader/$ExtendedShaderInstance" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ResourceProvider, $ResourceProvider$Type} from "packages/net/minecraft/server/packs/resources/$ResourceProvider"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShaderHolder, $ShaderHolder$Type} from "packages/team/lodestar/lodestone/systems/rendering/shader/$ShaderHolder"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Uniform, $Uniform$Type} from "packages/com/mojang/blaze3d/shaders/$Uniform"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ExtendedShaderInstance extends $ShaderInstance {
static readonly "SHADER_PATH": string
readonly "uniforms": $List<($Uniform)>
readonly "uniformMap": $Map<(string), ($Uniform)>
readonly "MODEL_VIEW_MATRIX": $Uniform
readonly "PROJECTION_MATRIX": $Uniform
readonly "INVERSE_VIEW_ROTATION_MATRIX": $Uniform
readonly "TEXTURE_MATRIX": $Uniform
readonly "SCREEN_SIZE": $Uniform
readonly "COLOR_MODULATOR": $Uniform
readonly "LIGHT0_DIRECTION": $Uniform
readonly "LIGHT1_DIRECTION": $Uniform
readonly "GLINT_ALPHA": $Uniform
readonly "FOG_START": $Uniform
readonly "FOG_END": $Uniform
readonly "FOG_COLOR": $Uniform
readonly "FOG_SHAPE": $Uniform
readonly "LINE_WIDTH": $Uniform
readonly "GAME_TIME": $Uniform
readonly "CHUNK_OFFSET": $Uniform

constructor(arg0: $ResourceProvider$Type, arg1: $ResourceLocation$Type, arg2: $VertexFormat$Type)

public "getDefaultUniformData"(): $Map<(string), ($Consumer<($Uniform)>)>
public "parseUniformNode"(arg0: $JsonElement$Type): void
public "setUniformDefaults"(): void
public "getShaderHolder"(): $ShaderHolder
get "defaultUniformData"(): $Map<(string), ($Consumer<($Uniform)>)>
get "shaderHolder"(): $ShaderHolder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedShaderInstance$Type = ($ExtendedShaderInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedShaderInstance_ = $ExtendedShaderInstance$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/options/$LodestoneItemCrumbsParticleOptions" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $LodestoneItemCrumbsParticleOptions extends $WorldParticleOptions {
readonly "stack": $ItemStack
readonly "type": $ParticleType<(any)>
 "behavior": $LodestoneParticleBehavior
 "behaviorComponent": $LodestoneBehaviorComponent
 "renderType": $ParticleRenderType
 "renderLayer": $RenderHandler$LodestoneRenderLayer
 "shouldCull": boolean
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "spawnActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "noClip": boolean
static readonly "DEFAULT_COLOR": $ColorParticleData
static readonly "DEFAULT_SPIN": $SpinParticleData
static readonly "DEFAULT_GENERIC": $GenericParticleData
 "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
 "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
 "colorData": $ColorParticleData
 "transparencyData": $GenericParticleData
 "scaleData": $GenericParticleData
 "spinData": $SpinParticleData
 "lifetimeSupplier": $Supplier<(integer)>
 "lifeDelaySupplier": $Supplier<(integer)>
 "gravityStrengthSupplier": $Supplier<(float)>
 "frictionStrengthSupplier": $Supplier<(float)>

constructor(arg0: $ParticleType$Type<($LodestoneItemCrumbsParticleOptions$Type)>, arg1: $ItemStack$Type)
constructor(arg0: $RegistryObject$Type<(any)>, arg1: $ItemStack$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemCrumbsParticleOptions$Type = ($LodestoneItemCrumbsParticleOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemCrumbsParticleOptions_ = $LodestoneItemCrumbsParticleOptions$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$MultiblockComponentBlock" {
import {$ILodestoneMultiblockComponent, $ILodestoneMultiblockComponent$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$ILodestoneMultiblockComponent"
import {$LodestoneEntityBlock, $LodestoneEntityBlock$Type} from "packages/team/lodestar/lodestone/systems/block/$LodestoneEntityBlock"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$MultiBlockComponentEntity, $MultiBlockComponentEntity$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockComponentEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MultiblockComponentBlock extends $LodestoneEntityBlock<($MultiBlockComponentEntity)> implements $ILodestoneMultiblockComponent {
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiblockComponentBlock$Type = ($MultiblockComponentBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiblockComponentBlock_ = $MultiblockComponentBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/command/$FreezeActiveWorldEventsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $FreezeActiveWorldEventsCommand {

constructor()

public static "register"(): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FreezeActiveWorldEventsCommand$Type = ($FreezeActiveWorldEventsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FreezeActiveWorldEventsCommand_ = $FreezeActiveWorldEventsCommand$Type;
}}
declare module "packages/team/lodestar/lodestone/events/$RuntimeEvents" {
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"
import {$PlayerEvent$Clone, $PlayerEvent$Clone$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$Clone"
import {$PlayerEvent$StartTracking, $PlayerEvent$StartTracking$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$StartTracking"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$PlayerInteractEvent$RightClickBlock, $PlayerInteractEvent$RightClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickBlock"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $RuntimeEvents {

constructor()

public static "startTracking"(arg0: $PlayerEvent$StartTracking$Type): void
public static "worldTick"(arg0: $TickEvent$LevelTickEvent$Type): void
public static "playerTick"(arg0: $TickEvent$PlayerTickEvent$Type): void
public static "attachWorldCapability"(arg0: $AttachCapabilitiesEvent$Type<($Level$Type)>): void
public static "attachEntityCapability"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
public static "placeBlock"(arg0: $PlayerInteractEvent$RightClickBlock$Type): void
public static "playerClone"(arg0: $PlayerEvent$Clone$Type): void
public static "onDeath"(arg0: $LivingDeathEvent$Type): void
public static "onHurt"(arg0: $LivingHurtEvent$Type): void
public static "entityJoin"(arg0: $EntityJoinLevelEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimeEvents$Type = ($RuntimeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimeEvents_ = $RuntimeEvents$Type;
}}
declare module "packages/team/lodestar/lodestone/network/capability/$SyncLodestonePlayerCapabilityPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LodestoneTwoWayNBTPacket, $LodestoneTwoWayNBTPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneTwoWayNBTPacket"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $SyncLodestonePlayerCapabilityPacket extends $LodestoneTwoWayNBTPacket {
static readonly "PLAYER_UUID": string

constructor(arg0: $CompoundTag$Type)
constructor(arg0: $UUID$Type, arg1: $CompoundTag$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SyncLodestonePlayerCapabilityPacket
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "serverExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public "clientExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>, arg1: $CompoundTag$Type): void
public static "handleTag"(arg0: $UUID$Type, arg1: $CompoundTag$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncLodestonePlayerCapabilityPacket$Type = ($SyncLodestonePlayerCapabilityPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncLodestonePlayerCapabilityPacket_ = $SyncLodestonePlayerCapabilityPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/events/types/worldevent/$WorldEventInstanceEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $WorldEventInstanceEvent extends $Event {

constructor()
constructor(arg0: $WorldEventInstance$Type, arg1: $Level$Type)

public "getWorldEvent"(): $WorldEventInstance
public "getLevel"(): $Level
public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "worldEvent"(): $WorldEventInstance
get "level"(): $Level
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventInstanceEvent$Type = ($WorldEventInstanceEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventInstanceEvent_ = $WorldEventInstanceEvent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneWorldParticleRenderType" {
import {$Tesselator, $Tesselator$Type} from "packages/com/mojang/blaze3d/vertex/$Tesselator"
import {$GlStateManager$DestFactor, $GlStateManager$DestFactor$Type} from "packages/com/mojang/blaze3d/platform/$GlStateManager$DestFactor"
import {$ShaderHolder, $ShaderHolder$Type} from "packages/team/lodestar/lodestone/systems/rendering/shader/$ShaderHolder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$TextureManager, $TextureManager$Type} from "packages/net/minecraft/client/renderer/texture/$TextureManager"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"
import {$LodestoneRenderType, $LodestoneRenderType$Type} from "packages/team/lodestar/lodestone/systems/rendering/$LodestoneRenderType"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GlStateManager$SourceFactor, $GlStateManager$SourceFactor$Type} from "packages/com/mojang/blaze3d/platform/$GlStateManager$SourceFactor"

export class $LodestoneWorldParticleRenderType implements $ParticleRenderType {
static readonly "DEPTH_FADE": $Function<($LodestoneWorldParticleRenderType), ($LodestoneWorldParticleRenderType)>
static readonly "ADDITIVE": $LodestoneWorldParticleRenderType
static readonly "TRANSPARENT": $LodestoneWorldParticleRenderType
static readonly "LUMITRANSPARENT": $LodestoneWorldParticleRenderType
static readonly "TERRAIN_SHEET": $LodestoneWorldParticleRenderType
static readonly "ADDITIVE_TERRAIN_SHEET": $LodestoneWorldParticleRenderType
readonly "renderType": $LodestoneRenderType

constructor(arg0: $LodestoneRenderType$Type, arg1: $Supplier$Type<($ShaderInstance$Type)>, arg2: $ResourceLocation$Type, arg3: $Runnable$Type)
constructor(arg0: $LodestoneRenderType$Type, arg1: $ShaderHolder$Type, arg2: $ResourceLocation$Type, arg3: $Runnable$Type)
constructor(arg0: $LodestoneRenderType$Type, arg1: $Supplier$Type<($ShaderInstance$Type)>, arg2: $ResourceLocation$Type, arg3: $GlStateManager$SourceFactor$Type, arg4: $GlStateManager$DestFactor$Type)
constructor(arg0: $LodestoneRenderType$Type, arg1: $ShaderHolder$Type, arg2: $ResourceLocation$Type, arg3: $GlStateManager$SourceFactor$Type, arg4: $GlStateManager$DestFactor$Type)

public "begin"(arg0: $BufferBuilder$Type, arg1: $TextureManager$Type): void
public "end"(arg0: $Tesselator$Type): void
public "withDepthFade"(): $LodestoneWorldParticleRenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneWorldParticleRenderType$Type = ($LodestoneWorldParticleRenderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneWorldParticleRenderType_ = $LodestoneWorldParticleRenderType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/$Vertex" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"

export class $Vertex extends $Record {

constructor(position: $Vector3f$Type, normal: $Vector3f$Type, uv: $Vec2$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "position"(): $Vector3f
public "normal"(): $Vector3f
public "uv"(): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vertex$Type = ($Vertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vertex_ = $Vertex$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$DirectionalParticleBehavior" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$DirectionalBehaviorComponent, $DirectionalBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$DirectionalBehaviorComponent"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export class $DirectionalParticleBehavior implements $LodestoneParticleBehavior {


public "render"(arg0: $LodestoneWorldParticle$Type, arg1: $VertexConsumer$Type, arg2: $Camera$Type, arg3: float): void
public "getComponent"(arg0: $LodestoneBehaviorComponent$Type): $DirectionalBehaviorComponent
public "oldSchool"(arg0: float, arg1: float, arg2: float): $Quaternionf
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectionalParticleBehavior$Type = ($DirectionalParticleBehavior);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectionalParticleBehavior_ = $DirectionalParticleBehavior$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$LodestoneRenderType" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ColorLogicStateShard, $RenderStateShard$ColorLogicStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ColorLogicStateShard"
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$DrawBuffer, $DrawBuffer$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$DrawBuffer"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$RenderStateShard$TextureStateShard, $RenderStateShard$TextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$RenderType$CompositeRenderType, $RenderType$CompositeRenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType$CompositeRenderType"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$RenderType$CompositeState, $RenderType$CompositeState$Type} from "packages/net/minecraft/client/renderer/$RenderType$CompositeState"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$VertexSorting, $VertexSorting$Type} from "packages/com/mojang/blaze3d/vertex/$VertexSorting"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $LodestoneRenderType extends $RenderType {
readonly "state": $RenderType$CompositeState
static readonly "BIG_BUFFER_SIZE": integer
static readonly "MEDIUM_BUFFER_SIZE": integer
static readonly "SMALL_BUFFER_SIZE": integer
static readonly "TRANSIENT_BUFFER_SIZE": integer
static readonly "LINES": $RenderType$CompositeRenderType
static readonly "LINE_STRIP": $RenderType$CompositeRenderType
 "sortOnUpload": boolean
static readonly "VIEW_SCALE_Z_EPSILON": float
static readonly "MAX_ENCHANTMENT_GLINT_SPEED_MILLIS": double
readonly "name": string
 "setupState": $Runnable
static readonly "NO_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "ADDITIVE_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "LIGHTNING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "GLINT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "CRUMBLING_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "TRANSLUCENT_TRANSPARENCY": $RenderStateShard$TransparencyStateShard
static readonly "NO_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_TEX_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_TEX_LIGHTMAP_SHADER": $RenderStateShard$ShaderStateShard
static readonly "POSITION_COLOR_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_MIPPED_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_MOVING_BLOCK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRANSLUCENT_NO_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SOLID_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_BEACON_BEAM_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_DECAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_NO_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_SHADOW_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_ALPHA_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_EYES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENERGY_SWIRL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LEASH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_WATER_MASK_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_OUTLINE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_TRANSLUCENT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_CRUMBLING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LIGHTNING_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_TRIPWIRE_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_PORTAL_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_END_GATEWAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_LINES_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER": $RenderStateShard$ShaderStateShard
static readonly "RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER": $RenderStateShard$ShaderStateShard
static readonly "BLOCK_SHEET_MIPPED": $RenderStateShard$TextureStateShard
static readonly "BLOCK_SHEET": $RenderStateShard$TextureStateShard
static readonly "NO_TEXTURE": $RenderStateShard$EmptyTextureStateShard
static readonly "DEFAULT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "ENTITY_GLINT_TEXTURING": $RenderStateShard$TexturingStateShard
static readonly "LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "NO_LIGHTMAP": $RenderStateShard$LightmapStateShard
static readonly "OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "NO_OVERLAY": $RenderStateShard$OverlayStateShard
static readonly "CULL": $RenderStateShard$CullStateShard
static readonly "NO_CULL": $RenderStateShard$CullStateShard
static readonly "NO_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "EQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "LEQUAL_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "GREATER_DEPTH_TEST": $RenderStateShard$DepthTestStateShard
static readonly "COLOR_DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "COLOR_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "DEPTH_WRITE": $RenderStateShard$WriteMaskStateShard
static readonly "NO_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "POLYGON_OFFSET_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "VIEW_OFFSET_Z_LAYERING": $RenderStateShard$LayeringStateShard
static readonly "MAIN_TARGET": $RenderStateShard$OutputStateShard
static readonly "OUTLINE_TARGET": $RenderStateShard$OutputStateShard
static readonly "TRANSLUCENT_TARGET": $RenderStateShard$OutputStateShard
static readonly "PARTICLES_TARGET": $RenderStateShard$OutputStateShard
static readonly "WEATHER_TARGET": $RenderStateShard$OutputStateShard
static readonly "CLOUDS_TARGET": $RenderStateShard$OutputStateShard
static readonly "ITEM_ENTITY_TARGET": $RenderStateShard$OutputStateShard
static readonly "DEFAULT_LINE": $RenderStateShard$LineStateShard
static readonly "NO_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard
static readonly "OR_REVERSE_COLOR_LOGIC": $RenderStateShard$ColorLogicStateShard

constructor(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: integer, arg4: boolean, arg5: boolean, arg6: $RenderType$CompositeState$Type)

public static "createRenderType"(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: integer, arg4: boolean, arg5: boolean, arg6: $RenderType$CompositeState$Type): $LodestoneRenderType
public "toString"(): string
public "end"(arg0: $BufferBuilder$Type, arg1: $VertexSorting$Type): void
public "outline"(): $Optional<($RenderType)>
public "isOutline"(): boolean
public static "getDrawBuffer"(arg0: $RenderType$Type): $DrawBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneRenderType$Type = ($LodestoneRenderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneRenderType_ = $LodestoneRenderType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleItemStackKey" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ScreenParticleItemStackKey {
readonly "isHotbarItem": boolean
readonly "isRenderedAfterItem": boolean
readonly "itemStack": $ItemStack

constructor(arg0: boolean, arg1: boolean, arg2: $ItemStack$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleItemStackKey$Type = ($ScreenParticleItemStackKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleItemStackKey_ = $ScreenParticleItemStackKey$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/client/$LodestoneOBJModelRegistry" {
import {$ObjModel, $ObjModel$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$ObjModel"
import {$MultiLODModel, $MultiLODModel$Type} from "packages/team/lodestar/lodestone/systems/model/obj/lod/$MultiLODModel"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$List, $List$Type} from "packages/java/util/$List"

export class $LodestoneOBJModelRegistry {
static "OBJ_MODELS": $List<($ObjModel)>
static "LOD_MODELS": $List<($MultiLODModel)>

constructor()

public static "onClientSetup"(arg0: $FMLClientSetupEvent$Type): void
public static "loadModels"(): void
public static "registerObjModel"(arg0: $ObjModel$Type): $ObjModel
public static "registerObjModel"(arg0: $MultiLODModel$Type): $MultiLODModel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneOBJModelRegistry$Type = ($LodestoneOBJModelRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneOBJModelRegistry_ = $LodestoneOBJModelRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$ItemHelper" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ItemHelper {

constructor()

public static "getEventResponders"(arg0: $LivingEntity$Type): $ArrayList<($ItemStack)>
public static "damageItem"<T extends $LivingEntity>(arg0: $ItemStack$Type, arg1: integer, arg2: T, arg3: $Consumer$Type<(T)>): boolean
public static "getClosestEntity"<T extends $Entity>(arg0: $List$Type<(T)>, arg1: $Vec3$Type): $Entity
public static "quietlyGiveItemToPlayer"(arg0: $Player$Type, arg1: $ItemStack$Type): void
public static "copyWithNewCount"(arg0: $List$Type<($ItemStack$Type)>, arg1: integer): $ArrayList<($ItemStack)>
public static "copyWithNewCount"(arg0: $ItemStack$Type, arg1: integer): $ItemStack
public static "spawnItemOnEntity"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): void
public static "giveItemToEntity"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): void
public static "nonEmptyStackList"(arg0: $ArrayList$Type<($ItemStack$Type)>): $ArrayList<($ItemStack)>
public static "applyEnchantments"(arg0: $LivingEntity$Type, arg1: $Entity$Type, arg2: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemHelper$Type = ($ItemHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemHelper_ = $ItemHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$VecHelper" {
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $VecHelper {
static readonly "CENTER_OF_ORIGIN": $Vec3

constructor()

public static "rotate"(arg0: $Vec3$Type, arg1: double, arg2: $Direction$Axis$Type): $Vec3
public static "radialOffset"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float): $Vec3
public static "rotatingRadialOffsets"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float, arg4: long, arg5: float): $ArrayList<($Vec3)>
public static "rotatingRadialOffsets"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: long, arg4: float): $ArrayList<($Vec3)>
public static "blockOutlinePositions"(arg0: $Level$Type, arg1: $BlockPos$Type): $ArrayList<($Vec3)>
public static "rotatingRadialOffset"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float, arg4: long, arg5: float): $Vec3
public static "rotatingRadialOffset"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: long, arg6: float): $Vec3
public static "getCenterOf"(arg0: $Vec3i$Type): $Vec3
public static "projectToPlayerView"(arg0: $Vec3$Type, arg1: float): $Vec3
public static "axisAlignedPlaneOf"(arg0: $Vec3$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VecHelper$Type = ($VecHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VecHelper_ = $VecHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/command/$ListActiveWorldEventsCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $ListActiveWorldEventsCommand {

constructor()

public static "register"(): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListActiveWorldEventsCommand$Type = ($ListActiveWorldEventsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListActiveWorldEventsCommand_ = $ListActiveWorldEventsCommand$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType$EventInstanceSupplier" {
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"

export interface $WorldEventType$EventInstanceSupplier {

 "getInstance"(): $WorldEventInstance

(): $WorldEventInstance
}

export namespace $WorldEventType$EventInstanceSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventType$EventInstanceSupplier$Type = ($WorldEventType$EventInstanceSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventType$EventInstanceSupplier_ = $WorldEventType$EventInstanceSupplier$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$BlockHelper" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $BlockHelper {

constructor()

public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $Stream<($BlockPos)>
public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer): $Stream<($BlockPos)>
public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float, arg2: float): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float, arg2: float, arg3: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float, arg2: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getNeighboringBlocksStream"(arg0: $BlockPos$Type): $Stream<($BlockPos)>
public static "getNeighboringBlocks"(arg0: $BlockPos$Type): $Collection<($BlockPos)>
public static "getPath"(arg0: $BlockPos$Type, arg1: $BlockPos$Type, arg2: integer, arg3: boolean, arg4: $Level$Type): $Collection<($BlockPos)>
public static "getBlockStateWithExistingProperties"(arg0: $BlockState$Type, arg1: $BlockState$Type): $BlockState
public static "setBlockStateWithExistingProperties"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: integer): $BlockState
public static "updateState"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public static "updateState"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
public static "saveBlockPos"(arg0: $CompoundTag$Type, arg1: $BlockPos$Type): $CompoundTag
public static "saveBlockPos"(arg0: $CompoundTag$Type, arg1: $BlockPos$Type, arg2: string): $CompoundTag
public static "loadBlockPos"(arg0: $CompoundTag$Type, arg1: string): $BlockPos
public static "loadBlockPos"(arg0: $CompoundTag$Type): $BlockPos
public static "fromBlockPos"(arg0: $BlockPos$Type): $Vec3
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer): $Collection<($BlockPos)>
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $Collection<($BlockPos)>
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer): $Stream<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $Stream<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "withinBlock"(arg0: $Random$Type, arg1: $BlockPos$Type): $Vec3
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer): $Collection<($BlockPos)>
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $Collection<($BlockPos)>
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float, arg2: float, arg3: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float, arg2: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float, arg2: float): $Collection<($BlockPos)>
public static "updateAndNotifyState"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public static "updateAndNotifyState"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
public static "newStateWithOldProperty"<T extends $Comparable<(T)>>(arg0: $BlockState$Type, arg1: $BlockState$Type, arg2: $Property$Type<(T)>): $BlockState
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: $Predicate$Type<(T)>): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: $Predicate$Type<(T)>): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $AABB$Type): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: $Predicate$Type<(T)>): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer): $Collection<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: $Predicate$Type<(T)>): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $AABB$Type): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: $Predicate$Type<(T)>): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: $Predicate$Type<(T)>): $Stream<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockHelper$Type = ($BlockHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockHelper_ = $BlockHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneBlockEntityRegistry" {
import {$LodestoneSignBlockEntity, $LodestoneSignBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneSignBlockEntity"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MultiBlockComponentEntity, $MultiBlockComponentEntity$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockComponentEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LodestoneBlockEntityRegistry {
static readonly "BLOCK_ENTITY_TYPES": $DeferredRegister<($BlockEntityType<(any)>)>
static readonly "MULTIBLOCK_COMPONENT": $RegistryObject<($BlockEntityType<($MultiBlockComponentEntity)>)>
static readonly "SIGN": $RegistryObject<($BlockEntityType<($LodestoneSignBlockEntity)>)>

constructor()

public static "getBlocksExact"(arg0: $Class$Type<(any)>): ($Block)[]
public static "getBlocks"(...arg0: ($Class$Type<(any)>)[]): ($Block)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockEntityRegistry$Type = ($LodestoneBlockEntityRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockEntityRegistry_ = $LodestoneBlockEntityRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneBlockProperties" {
import {$MapColor, $MapColor$Type} from "packages/net/minecraft/world/level/material/$MapColor"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PushReaction, $PushReaction$Type} from "packages/net/minecraft/world/level/material/$PushReaction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$BlockBehaviour$OffsetFunction, $BlockBehaviour$OffsetFunction$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$OffsetFunction"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$LodestoneDatagenBlockData, $LodestoneDatagenBlockData$Type} from "packages/team/lodestar/lodestone/systems/datagen/$LodestoneDatagenBlockData"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockBehaviour$StateArgumentPredicate, $BlockBehaviour$StateArgumentPredicate$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$StateArgumentPredicate"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$BlockBehaviour$OffsetType, $BlockBehaviour$OffsetType$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$OffsetType"
import {$NoteBlockInstrument, $NoteBlockInstrument$Type} from "packages/net/minecraft/world/level/block/state/properties/$NoteBlockInstrument"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$LodestoneThrowawayBlockData, $LodestoneThrowawayBlockData$Type} from "packages/team/lodestar/lodestone/systems/block/$LodestoneThrowawayBlockData"
import {$BlockBehaviour, $BlockBehaviour$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockBehaviour$StatePredicate, $BlockBehaviour$StatePredicate$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$StatePredicate"
import {$FeatureFlag, $FeatureFlag$Type} from "packages/net/minecraft/world/flag/$FeatureFlag"

export class $LodestoneBlockProperties extends $BlockBehaviour$Properties {
 "mapColor": $Function<($BlockState), ($MapColor)>
 "hasCollision": boolean
 "soundType": $SoundType
 "lightEmission": $ToIntFunction<($BlockState)>
 "explosionResistance": float
 "destroyTime": float
 "requiresCorrectToolForDrops": boolean
 "isRandomlyTicking": boolean
 "friction": float
 "speedFactor": float
 "jumpFactor": float
 "drops": $ResourceLocation
 "canOcclude": boolean
 "isAir": boolean
 "ignitedByLava": boolean
/**
 * 
 * @deprecated
 */
 "liquid": boolean
/**
 * 
 * @deprecated
 */
 "forceSolidOff": boolean
 "forceSolidOn": boolean
 "pushReaction": $PushReaction
 "spawnParticlesOnBreak": boolean
 "instrument": $NoteBlockInstrument
 "replaceable": boolean
 "lootTableSupplier": $Supplier<($ResourceLocation)>
 "isValidSpawn": $BlockBehaviour$StateArgumentPredicate<($EntityType<(any)>)>
 "isRedstoneConductor": $BlockBehaviour$StatePredicate
 "isSuffocating": $BlockBehaviour$StatePredicate
 "isViewBlocking": $BlockBehaviour$StatePredicate
 "hasPostProcess": $BlockBehaviour$StatePredicate
 "emissiveRendering": $BlockBehaviour$StatePredicate
 "dynamicShape": boolean
 "requiredFeatures": $FeatureFlagSet
 "offsetFunction": $Optional<($BlockBehaviour$OffsetFunction)>

constructor()

public static "of"(): $LodestoneBlockProperties
public static "copy"(arg0: $BlockBehaviour$Type): $LodestoneBlockProperties
public "setCutoutRenderType"(): $LodestoneBlockProperties
public "isRedstoneConductor"(arg0: $BlockBehaviour$StatePredicate$Type): $LodestoneBlockProperties
public "instrument"(arg0: $NoteBlockInstrument$Type): $LodestoneBlockProperties
public "lightLevel"(arg0: $ToIntFunction$Type<($BlockState$Type)>): $LodestoneBlockProperties
public "air"(): $LodestoneBlockProperties
public "isViewBlocking"(arg0: $BlockBehaviour$StatePredicate$Type): $LodestoneBlockProperties
public "ignitedByLava"(): $LodestoneBlockProperties
public "needsIron"(): $LodestoneBlockProperties
public "noLootTable"(): $LodestoneBlockProperties
public "needsAxe"(): $LodestoneBlockProperties
public "randomTicks"(): $LodestoneBlockProperties
public "hasPostProcess"(arg0: $BlockBehaviour$StatePredicate$Type): $LodestoneBlockProperties
public "forceSolidOn"(): $LodestoneBlockProperties
public "offsetType"(arg0: $BlockBehaviour$OffsetType$Type): $LodestoneBlockProperties
public "instabreak"(): $LodestoneBlockProperties
public "needsHoe"(): $LodestoneBlockProperties
public "needsDiamond"(): $LodestoneBlockProperties
public "dropsLike"(arg0: $Block$Type): $LodestoneBlockProperties
public "noCollission"(): $LodestoneBlockProperties
public "needsPickaxe"(): $LodestoneBlockProperties
public "emissiveRendering"(arg0: $BlockBehaviour$StatePredicate$Type): $LodestoneBlockProperties
public "dynamicShape"(): $LodestoneBlockProperties
public "needsShovel"(): $LodestoneBlockProperties
public "pushReaction"(arg0: $PushReaction$Type): $LodestoneBlockProperties
public "noOcclusion"(): $LodestoneBlockProperties
public "isSuffocating"(arg0: $BlockBehaviour$StatePredicate$Type): $LodestoneBlockProperties
public "destroyTime"(arg0: float): $LodestoneBlockProperties
public "needsStone"(): $LodestoneBlockProperties
public "requiredFeatures"(...arg0: ($FeatureFlag$Type)[]): $LodestoneBlockProperties
public "noParticlesOnBreak"(): $LodestoneBlockProperties
public "addThrowawayData"(arg0: $Function$Type<($LodestoneThrowawayBlockData$Type), ($LodestoneThrowawayBlockData$Type)>): $LodestoneBlockProperties
public "getDatagenData"(): $LodestoneDatagenBlockData
public "hasInheritedLoot"(): $LodestoneBlockProperties
public "getThrowawayData"(): $LodestoneThrowawayBlockData
public "addDatagenData"(arg0: $Function$Type<($LodestoneDatagenBlockData$Type), ($LodestoneDatagenBlockData$Type)>): $LodestoneBlockProperties
public "sound"(arg0: $SoundType$Type): $LodestoneBlockProperties
public "isValidSpawn"(arg0: $BlockBehaviour$StateArgumentPredicate$Type<($EntityType$Type<(any)>)>): $LodestoneBlockProperties
public "setRenderType"(arg0: $Supplier$Type<($Supplier$Type<($RenderType$Type)>)>): $LodestoneBlockProperties
public "replaceable"(): $LodestoneBlockProperties
public "explosionResistance"(arg0: float): $LodestoneBlockProperties
public "requiresCorrectToolForDrops"(): $LodestoneBlockProperties
public "strength"(arg0: float): $LodestoneBlockProperties
public "strength"(arg0: float, arg1: float): $LodestoneBlockProperties
public "friction"(arg0: float): $LodestoneBlockProperties
public "jumpFactor"(arg0: float): $LodestoneBlockProperties
public "speedFactor"(arg0: float): $LodestoneBlockProperties
public "lootFrom"(arg0: $Supplier$Type<(any)>): $LodestoneBlockProperties
public "liquid"(): $LodestoneBlockProperties
public "addTag"(arg0: $TagKey$Type<($Block$Type)>): $LodestoneBlockProperties
public "mapColor"(arg0: $Function$Type<($BlockState$Type), ($MapColor$Type)>): $LodestoneBlockProperties
public "mapColor"(arg0: $DyeColor$Type): $LodestoneBlockProperties
public "mapColor"(arg0: $MapColor$Type): $LodestoneBlockProperties
public "addTags"(...arg0: ($TagKey$Type<($Block$Type)>)[]): $LodestoneBlockProperties
get "datagenData"(): $LodestoneDatagenBlockData
get "throwawayData"(): $LodestoneThrowawayBlockData
set "renderType"(value: $Supplier$Type<($Supplier$Type<($RenderType$Type)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockProperties$Type = ($LodestoneBlockProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockProperties_ = $LodestoneBlockProperties$Type;
}}
declare module "packages/team/lodestar/lodestone/config/$ClientConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$LodestoneConfig$ConfigPath, $LodestoneConfig$ConfigPath$Type} from "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$ConfigPath"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$LodestoneConfig$ConfigValueHolder, $LodestoneConfig$ConfigValueHolder$Type} from "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$ConfigValueHolder"
import {$LodestoneConfig, $LodestoneConfig$Type} from "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig"

export class $ClientConfig extends $LodestoneConfig {
static "DELAYED_PARTICLE_RENDERING": $LodestoneConfig$ConfigValueHolder<(boolean)>
static "EXPERIMENTAL_FABULOUS_LAYERING": $LodestoneConfig$ConfigValueHolder<(boolean)>
static "FIRE_OVERLAY_OFFSET": $LodestoneConfig$ConfigValueHolder<(double)>
static "SCREENSHAKE_INTENSITY": $LodestoneConfig$ConfigValueHolder<(double)>
static "ENABLE_SCREEN_PARTICLES": $LodestoneConfig$ConfigValueHolder<(boolean)>
static readonly "INSTANCE": $ClientConfig
static readonly "SPEC": $ForgeConfigSpec
static readonly "VALUE_HOLDERS": $ConcurrentHashMap<($Pair<(string), ($LodestoneConfig$ConfigPath)>), ($ArrayList<($LodestoneConfig$ConfigValueHolder)>)>

constructor(arg0: $ForgeConfigSpec$Builder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfig$Type = ($ClientConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfig_ = $ClientConfig$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $AbstractBlockStateSmith<T extends $Block> {
readonly "blockClass": $Class<(T)>

constructor(arg0: $Class$Type<(T)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBlockStateSmith$Type<T> = ($AbstractBlockStateSmith<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBlockStateSmith_<T> = $AbstractBlockStateSmith$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/helpers/$NBTHelper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$NBTHelper$TagFilter, $NBTHelper$TagFilter$Type} from "packages/team/lodestar/lodestone/helpers/$NBTHelper$TagFilter"

export class $NBTHelper {

constructor()

public static "create"(...arg0: (string)[]): $NBTHelper$TagFilter
public static "removeTags"(arg0: $CompoundTag$Type, arg1: $NBTHelper$TagFilter$Type): $CompoundTag
public static "filterTag"(arg0: $CompoundTag$Type, arg1: $NBTHelper$TagFilter$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTHelper$Type = ($NBTHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTHelper_ = $NBTHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/builder/$WorldParticleBuilder" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$WorldParticleOptions, $WorldParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$WorldParticleOptions"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$LodestoneWorldParticleType, $LodestoneWorldParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneWorldParticleType"
import {$AbstractParticleBuilder, $AbstractParticleBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/builder/$AbstractParticleBuilder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $WorldParticleBuilder extends $AbstractParticleBuilder<($WorldParticleOptions)> {


public "setRandomOffset"(arg0: double, arg1: double, arg2: double): $WorldParticleBuilder
public "setRandomOffset"(arg0: double): $WorldParticleBuilder
public "setRandomOffset"(arg0: double, arg1: double): $WorldParticleBuilder
public "modifyData"(arg0: $GenericParticleData$Type, arg1: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "modifyData"(arg0: $Collection$Type<($Supplier$Type<($GenericParticleData$Type)>)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "modifyData"(arg0: $Function$Type<($WorldParticleBuilder$Type), ($GenericParticleData$Type)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "modifyData"(arg0: $Supplier$Type<($GenericParticleData$Type)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "setRandomMotion"(arg0: double, arg1: double): $WorldParticleBuilder
public "setRandomMotion"(arg0: double): $WorldParticleBuilder
public "setRandomMotion"(arg0: double, arg1: double, arg2: double): $WorldParticleBuilder
public "repeat"(arg0: $Level$Type, arg1: double, arg2: double, arg3: double, arg4: integer): $WorldParticleBuilder
public static "create"(arg0: $LodestoneWorldParticleType$Type): $WorldParticleBuilder
public static "create"(arg0: $WorldParticleOptions$Type): $WorldParticleBuilder
public static "create"(arg0: $RegistryObject$Type<(any)>): $WorldParticleBuilder
public static "create"(arg0: $LodestoneWorldParticleType$Type, arg1: $LodestoneBehaviorComponent$Type): $WorldParticleBuilder
public static "create"(arg0: $RegistryObject$Type<(any)>, arg1: $LodestoneBehaviorComponent$Type): $WorldParticleBuilder
public "spawn"(arg0: $Level$Type, arg1: double, arg2: double, arg3: double): $WorldParticleBuilder
public "enableCull"(): $WorldParticleBuilder
public "setRenderType"(arg0: $ParticleRenderType$Type): $WorldParticleBuilder
public "modifyColorData"(arg0: $Consumer$Type<($ColorParticleData$Type)>): $WorldParticleBuilder
public "modifyGravity"(arg0: $Function$Type<(float), ($Supplier$Type<(float)>)>): $WorldParticleBuilder
public "setSpinData"(arg0: $SpinParticleData$Type): $WorldParticleBuilder
public "setScaleData"(arg0: $GenericParticleData$Type): $WorldParticleBuilder
public "modifyFriction"(arg0: $Function$Type<(float), ($Supplier$Type<(float)>)>): $WorldParticleBuilder
public "multiplyLifetime"(arg0: float): $WorldParticleBuilder
public "setDiscardFunction"(arg0: $SimpleParticleOptions$ParticleDiscardFunctionType$Type): $WorldParticleBuilder
public "setLifeDelay"(arg0: $Supplier$Type<(integer)>): $WorldParticleBuilder
public "setLifeDelay"(arg0: integer): $WorldParticleBuilder
public "modifyLifeDelay"(arg0: $Function$Type<(integer), ($Supplier$Type<(integer)>)>): $WorldParticleBuilder
public "setSpritePicker"(arg0: $SimpleParticleOptions$ParticleSpritePicker$Type): $WorldParticleBuilder
public "multiplyLifeDelay"(arg0: float): $WorldParticleBuilder
public "disableCull"(): $WorldParticleBuilder
public "addMotion"(arg0: $Vec3$Type): $WorldParticleBuilder
public "addMotion"(arg0: double, arg1: double, arg2: double): $WorldParticleBuilder
public "addMotion"(arg0: $Vector3f$Type): $WorldParticleBuilder
public "setMotion"(arg0: $Vector3f$Type): $WorldParticleBuilder
public "setMotion"(arg0: double, arg1: double, arg2: double): $WorldParticleBuilder
public "setMotion"(arg0: $Vec3$Type): $WorldParticleBuilder
public "act"(arg0: $Consumer$Type<($WorldParticleBuilder$Type)>): $WorldParticleBuilder
public "setLifetime"(arg0: integer): $WorldParticleBuilder
public "setLifetime"(arg0: $Supplier$Type<(integer)>): $WorldParticleBuilder
public "modifyBehaviorData"<T extends $LodestoneBehaviorComponent>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(T), ($GenericParticleData$Type)>, arg2: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "getBehaviorData"<T extends $LodestoneBehaviorComponent>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(T), ($GenericParticleData$Type)>): $Optional<($GenericParticleData)>
public "modifyBehavior"<T extends $LodestoneBehaviorComponent>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): $WorldParticleBuilder
public "disableNoClip"(): $WorldParticleBuilder
public "setNoClip"(arg0: boolean): $WorldParticleBuilder
public "setRenderTarget"(arg0: $RenderHandler$LodestoneRenderLayer$Type): $WorldParticleBuilder
public "setShouldCull"(arg0: boolean): $WorldParticleBuilder
public "setForceSpawn"(arg0: boolean): $WorldParticleBuilder
public "enableNoClip"(): $WorldParticleBuilder
public "enableForcedSpawn"(): $WorldParticleBuilder
public "addTickActor"(arg0: $Consumer$Type<($LodestoneWorldParticle$Type)>): $WorldParticleBuilder
public "addSpawnActor"(arg0: $Consumer$Type<($LodestoneWorldParticle$Type)>): $WorldParticleBuilder
public "clearTickActor"(): $WorldParticleBuilder
public "disableForcedSpawn"(): $WorldParticleBuilder
public "addRenderActor"(arg0: $Consumer$Type<($LodestoneWorldParticle$Type)>): $WorldParticleBuilder
public "clearSpawnActors"(): $WorldParticleBuilder
public "clearRenderActors"(): $WorldParticleBuilder
public "modifyOptionalData"(arg0: $Function$Type<($WorldParticleBuilder$Type), ($Optional$Type<($GenericParticleData$Type)>)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "modifyOptionalData"(arg0: $Optional$Type<($GenericParticleData$Type)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $WorldParticleBuilder
public "surroundBlock"(arg0: $Level$Type, arg1: $BlockPos$Type, ...arg2: ($Direction$Type)[]): $WorldParticleBuilder
public "clearActors"(): $WorldParticleBuilder
public "repeatCircle"(arg0: $Level$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: integer): $WorldParticleBuilder
public "spawnLine"(arg0: $Level$Type, arg1: $Vec3$Type, arg2: $Vec3$Type): $WorldParticleBuilder
public "createCircle"(arg0: $Level$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $WorldParticleBuilder
public "spawnAtRandomFace"(arg0: $Level$Type, arg1: $BlockPos$Type): $WorldParticleBuilder
public "surroundVoxelShape"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: integer): $WorldParticleBuilder
public "surroundVoxelShape"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $VoxelShape$Type, arg3: integer): $WorldParticleBuilder
public "repeatRandomFace"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer): $WorldParticleBuilder
public "createBlockOutline"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $WorldParticleBuilder
public "setBehavior"(arg0: $LodestoneBehaviorComponent$Type): $WorldParticleBuilder
public "getParticleOptions"(): $WorldParticleOptions
public "replaceExistingBehavior"<T extends $LodestoneBehaviorComponent>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(T), ($LodestoneBehaviorComponent$Type)>): $WorldParticleBuilder
public "getBehaviorComponent"<T extends $LodestoneBehaviorComponent>(arg0: $Class$Type<(T)>, arg1: $Function$Type<($WorldParticleOptions$Type), (T)>): $Optional<($LodestoneBehaviorComponent)>
public "getBehaviorComponent"<T extends $LodestoneBehaviorComponent>(arg0: $Class$Type<(T)>): $Optional<(T)>
public "repeatSurroundBlock"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, ...arg3: ($Direction$Type)[]): $WorldParticleBuilder
public "repeatSurroundBlock"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer): $WorldParticleBuilder
public "setTransparencyData"(arg0: $GenericParticleData$Type): $WorldParticleBuilder
set "randomOffset"(value: double)
set "randomMotion"(value: double)
set "renderType"(value: $ParticleRenderType$Type)
set "spinData"(value: $SpinParticleData$Type)
set "scaleData"(value: $GenericParticleData$Type)
set "discardFunction"(value: $SimpleParticleOptions$ParticleDiscardFunctionType$Type)
set "lifeDelay"(value: $Supplier$Type<(integer)>)
set "lifeDelay"(value: integer)
set "spritePicker"(value: $SimpleParticleOptions$ParticleSpritePicker$Type)
set "motion"(value: $Vector3f$Type)
set "motion"(value: $Vec3$Type)
set "lifetime"(value: integer)
set "lifetime"(value: $Supplier$Type<(integer)>)
set "noClip"(value: boolean)
set "renderTarget"(value: $RenderHandler$LodestoneRenderLayer$Type)
set "shouldCull"(value: boolean)
set "forceSpawn"(value: boolean)
set "behavior"(value: $LodestoneBehaviorComponent$Type)
get "particleOptions"(): $WorldParticleOptions
set "transparencyData"(value: $GenericParticleData$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldParticleBuilder$Type = ($WorldParticleBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldParticleBuilder_ = $WorldParticleBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/type/$LodestoneItemCrumbsParticleType" {
import {$LodestoneItemCrumbsParticleOptions, $LodestoneItemCrumbsParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$LodestoneItemCrumbsParticleOptions"
import {$AbstractLodestoneParticleType, $AbstractLodestoneParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/world/type/$AbstractLodestoneParticleType"

export class $LodestoneItemCrumbsParticleType extends $AbstractLodestoneParticleType<($LodestoneItemCrumbsParticleOptions)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemCrumbsParticleType$Type = ($LodestoneItemCrumbsParticleType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemCrumbsParticleType_ = $LodestoneItemCrumbsParticleType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/entityrenderer/$LodestoneBoatRenderer" {
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$ListModel, $ListModel$Type} from "packages/net/minecraft/client/model/$ListModel"
import {$Boat, $Boat$Type} from "packages/net/minecraft/world/entity/vehicle/$Boat"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BoatRenderer, $BoatRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$BoatRenderer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $LodestoneBoatRenderer extends $BoatRenderer {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type, arg1: $ResourceLocation$Type, arg2: boolean)

public "getModelWithLocation"(arg0: $Boat$Type): $Pair<($ResourceLocation), ($ListModel<($Boat)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBoatRenderer$Type = ($LodestoneBoatRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBoatRenderer_ = $LodestoneBoatRenderer$Type;
}}
declare module "packages/team/lodestar/lodestone/command/$GetDataWorldEventCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $GetDataWorldEventCommand {

constructor()

public static "register"(): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GetDataWorldEventCommand$Type = ($GetDataWorldEventCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GetDataWorldEventCommand_ = $GetDataWorldEventCommand$Type;
}}
declare module "packages/team/lodestar/lodestone/recipe/$NBTCarryRecipe" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$ShapedRecipe, $ShapedRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapedRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CraftingContainer, $CraftingContainer$Type} from "packages/net/minecraft/world/inventory/$CraftingContainer"

export class $NBTCarryRecipe extends $ShapedRecipe {
static readonly "NAME": string
readonly "nbtCarry": $Ingredient
readonly "width": integer
readonly "height": integer
readonly "result": $ItemStack

constructor(arg0: $ShapedRecipe$Type, arg1: $Ingredient$Type, arg2: $ItemStack$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $CraftingContainer$Type, arg1: $RegistryAccess$Type): $ItemStack
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTCarryRecipe$Type = ($NBTCarryRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTCarryRecipe_ = $NBTCarryRecipe$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$BlockTagsProvider, $BlockTagsProvider$Type} from "packages/net/minecraftforge/common/data/$BlockTagsProvider"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LodestoneBlockTagsProvider extends $BlockTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: string, arg3: $ExistingFileHelper$Type)

public "addTagsFromBlockProperties"(arg0: $Collection$Type<($Block$Type)>): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockTagsProvider$Type = ($LodestoneBlockTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockTagsProvider_ = $LodestoneBlockTagsProvider$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/recipe/$IngredientWithCount" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$IRecipeComponent, $IRecipeComponent$Type} from "packages/team/lodestar/lodestone/systems/recipe/$IRecipeComponent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $IngredientWithCount implements $IRecipeComponent {
readonly "ingredient": $Ingredient
readonly "count": integer

constructor(arg0: $Ingredient$Type, arg1: integer)

public "matches"(arg0: $ItemStack$Type): boolean
public "write"(arg0: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $IngredientWithCount
public "getStack"(): $ItemStack
public "getCount"(): integer
public "getItem"(): $Item
public static "deserialize"(arg0: $JsonObject$Type): $IngredientWithCount
public "serialize"(): $JsonObject
public "getStacks"(): $List<($ItemStack)>
public "isValid"(): boolean
get "stack"(): $ItemStack
get "count"(): integer
get "item"(): $Item
get "stacks"(): $List<($ItemStack)>
get "valid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientWithCount$Type = ($IngredientWithCount);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientWithCount_ = $IngredientWithCount$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$LodestoneAttributeEventHandler" {
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"

export class $LodestoneAttributeEventHandler {

constructor()

public static "applyMagicResistance"(arg0: double): double
public static "processAttributes"(arg0: $LivingHurtEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneAttributeEventHandler$Type = ($LodestoneAttributeEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneAttributeEventHandler_ = $LodestoneAttributeEventHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/capability/$LodestoneEntityDataCapability" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$LodestoneCapability, $LodestoneCapability$Type} from "packages/team/lodestar/lodestone/systems/capability/$LodestoneCapability"
import {$NBTHelper$TagFilter, $NBTHelper$TagFilter$Type} from "packages/team/lodestar/lodestone/helpers/$NBTHelper$TagFilter"
import {$PlayerEvent$StartTracking, $PlayerEvent$StartTracking$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$StartTracking"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$FireEffectInstance, $FireEffectInstance$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectInstance"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LodestoneEntityDataCapability implements $LodestoneCapability {
static "CAPABILITY": $Capability<($LodestoneEntityDataCapability)>
 "fireEffectInstance": $FireEffectInstance

constructor()

public static "sync"(arg0: $Entity$Type, arg1: $PacketDistributor$PacketTarget$Type): void
public static "sync"(arg0: $Entity$Type, arg1: $PacketDistributor$PacketTarget$Type, arg2: $NBTHelper$TagFilter$Type): void
public static "getCapabilityOptional"(arg0: $Entity$Type): $LazyOptional<($LodestoneEntityDataCapability)>
public static "syncTrackingAndSelf"(arg0: $Entity$Type, arg1: $NBTHelper$TagFilter$Type): void
public static "syncTrackingAndSelf"(arg0: $Entity$Type): void
public static "attachEntityCapability"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
public static "syncEntityCapability"(arg0: $PlayerEvent$StartTracking$Type): void
public static "getCapability"(arg0: $Entity$Type): $LodestoneEntityDataCapability
public static "syncTracking"(arg0: $Entity$Type, arg1: $NBTHelper$TagFilter$Type): void
public static "syncTracking"(arg0: $Entity$Type): void
public static "registerCapabilities"(arg0: $RegisterCapabilitiesEvent$Type): void
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneEntityDataCapability$Type = ($LodestoneEntityDataCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneEntityDataCapability_ = $LodestoneEntityDataCapability$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$ModCombatItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$TieredItem, $TieredItem$Type} from "packages/net/minecraft/world/item/$TieredItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ToolAction, $ToolAction$Type} from "packages/net/minecraftforge/common/$ToolAction"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModCombatItem extends $TieredItem {
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: float, arg2: float, arg3: $Item$Properties$Type)

public "canPerformAction"(arg0: $ItemStack$Type, arg1: $ToolAction$Type): boolean
public "getDestroySpeed"(arg0: $ItemStack$Type, arg1: $BlockState$Type): float
public "canAttackBlock"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type): boolean
public "hurtEnemy"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type, arg2: $LivingEntity$Type): boolean
public "isCorrectToolForDrops"(arg0: $BlockState$Type): boolean
public "mineBlock"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $BlockState$Type, arg3: $BlockPos$Type, arg4: $LivingEntity$Type): boolean
public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "getDamage"(): float
public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
get "damage"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModCombatItem$Type = ($ModCombatItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModCombatItem_ = $ModCombatItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/$Face" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vertex, $Vertex$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$Vertex"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $Face extends $Record {

constructor(vertices: $List$Type<($Vertex$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "vertices"(): $List<($Vertex)>
public "renderQuad"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer): void
public "renderFace"(arg0: $PoseStack$Type, arg1: $RenderType$Type, arg2: integer): void
public "renderTriangle"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer): void
public "getCentroid"(): $Vector3f
get "centroid"(): $Vector3f
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Face$Type = ($Face);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Face_ = $Face$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/$LodestoneArmorModel" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$HumanoidModel, $HumanoidModel$Type} from "packages/net/minecraft/client/model/$HumanoidModel"
import {$HumanoidModel$ArmPose, $HumanoidModel$ArmPose$Type} from "packages/net/minecraft/client/model/$HumanoidModel$ArmPose"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$PartDefinition, $PartDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$PartDefinition"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$MeshDefinition, $MeshDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$MeshDefinition"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LodestoneArmorModel extends $HumanoidModel<($LivingEntity)> {
 "slot": $EquipmentSlot
 "root": $ModelPart
 "head": $ModelPart
 "body": $ModelPart
 "leftArm": $ModelPart
 "rightArm": $ModelPart
 "leggings": $ModelPart
 "leftLegging": $ModelPart
 "rightLegging": $ModelPart
 "leftFoot": $ModelPart
 "rightFoot": $ModelPart
static readonly "OVERLAY_SCALE": float
static readonly "HAT_OVERLAY_SCALE": float
static readonly "LEGGINGS_OVERLAY_SCALE": float
static readonly "TOOT_HORN_XROT_BASE": float
static readonly "TOOT_HORN_YROT_BASE": float
readonly "hat": $ModelPart
readonly "rightLeg": $ModelPart
readonly "leftLeg": $ModelPart
 "leftArmPose": $HumanoidModel$ArmPose
 "rightArmPose": $HumanoidModel$ArmPose
 "crouching": boolean
 "swimAmount": float
readonly "scaleHead": boolean
readonly "babyYHeadOffset": float
readonly "babyZHeadOffset": float
readonly "babyHeadScale": float
readonly "babyBodyScale": float
readonly "bodyYOffset": float
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $ModelPart$Type)

public "copyFromDefault"(arg0: $HumanoidModel$Type<(any)>): void
public "renderToBuffer"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: integer, arg4: float, arg5: float, arg6: float, arg7: float): void
public static "createHumanoidAlias"(arg0: $MeshDefinition$Type): $PartDefinition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneArmorModel$Type = ($LodestoneArmorModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneArmorModel_ = $LodestoneArmorModel$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$LodestoneBlockFillerLayer" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$LodestoneBlockFiller$BlockStateEntry, $LodestoneBlockFiller$BlockStateEntry$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$BlockStateEntry"
import {$LodestoneBlockFiller$BlockStateEntryBuilder, $LodestoneBlockFiller$BlockStateEntryBuilder$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$BlockStateEntryBuilder"
import {$LodestoneBlockFiller$LodestoneLayerToken, $LodestoneBlockFiller$LodestoneLayerToken$Type} from "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$LodestoneLayerToken"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $LodestoneBlockFiller$LodestoneBlockFillerLayer extends $HashMap<($BlockPos), ($LodestoneBlockFiller$BlockStateEntry)> {
readonly "layerToken": $LodestoneBlockFiller$LodestoneLayerToken

constructor(arg0: $LodestoneBlockFiller$LodestoneLayerToken$Type)

public "put"(arg0: $BlockPos$Type, arg1: $LodestoneBlockFiller$BlockStateEntryBuilder$Type): $LodestoneBlockFiller$BlockStateEntry
public "replace"(arg0: $BlockPos$Type, arg1: $Function$Type<($LodestoneBlockFiller$BlockStateEntry$Type), ($LodestoneBlockFiller$BlockStateEntry$Type)>): void
public "fill"(arg0: $LevelAccessor$Type): void
public "putIfAbsent"(arg0: $BlockPos$Type, arg1: $LodestoneBlockFiller$BlockStateEntryBuilder$Type): $LodestoneBlockFiller$BlockStateEntry
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
export type $LodestoneBlockFiller$LodestoneBlockFillerLayer$Type = ($LodestoneBlockFiller$LodestoneBlockFillerLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller$LodestoneBlockFillerLayer_ = $LodestoneBlockFiller$LodestoneBlockFillerLayer$Type;
}}
declare module "packages/team/lodestar/lodestone/data/$LodestoneBlockTagDatagen" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$BlockTagsProvider, $BlockTagsProvider$Type} from "packages/net/minecraftforge/common/data/$BlockTagsProvider"

export class $LodestoneBlockTagDatagen extends $BlockTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockTagDatagen$Type = ($LodestoneBlockTagDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockTagDatagen_ = $LodestoneBlockTagDatagen$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/postprocess/$PostProcessHandler" {
import {$PostProcessor, $PostProcessor$Type} from "packages/team/lodestar/lodestone/systems/postprocess/$PostProcessor"
import {$RenderLevelStageEvent, $RenderLevelStageEvent$Type} from "packages/net/minecraftforge/client/event/$RenderLevelStageEvent"

export class $PostProcessHandler {

constructor()

public static "resize"(arg0: integer, arg1: integer): void
public static "onWorldRenderLast"(arg0: $RenderLevelStageEvent$Type): void
public static "copyDepthBuffer"(): void
public static "addInstance"(arg0: $PostProcessor$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostProcessHandler$Type = ($PostProcessHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostProcessHandler_ = $PostProcessHandler$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventRenderer" {
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $WorldEventRenderer<T extends $WorldEventInstance> {

constructor()

public "render"(arg0: T, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: float): void
public "canRender"(arg0: T): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventRenderer$Type<T> = ($WorldEventRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventRenderer_<T> = $WorldEventRenderer$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith" {
import {$ModularBlockStateSmith$ModularSmithStateSupplier, $ModularBlockStateSmith$ModularSmithStateSupplier$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith$ModularSmithStateSupplier"
import {$ItemModelSmith, $ItemModelSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$ItemModelSmith"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ModularBlockStateSmith$ModelFileSupplier, $ModularBlockStateSmith$ModelFileSupplier$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$ModularBlockStateSmith$ModelFileSupplier"
import {$AbstractBlockStateSmith$StateFunction, $AbstractBlockStateSmith$StateFunction$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith$StateFunction"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$AbstractBlockStateSmith$StateSmithData, $AbstractBlockStateSmith$StateSmithData$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith$StateSmithData"
import {$AbstractBlockStateSmith, $AbstractBlockStateSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith"

export class $ModularBlockStateSmith<T extends $Block> extends $AbstractBlockStateSmith<(T)> {
readonly "stateSupplier": $ModularBlockStateSmith$ModularSmithStateSupplier<(T)>
readonly "blockClass": $Class<(T)>

constructor(arg0: $Class$Type<(T)>, arg1: $ModularBlockStateSmith$ModularSmithStateSupplier$Type<(T)>)

public "act"(arg0: $AbstractBlockStateSmith$StateSmithData$Type, arg1: $AbstractBlockStateSmith$StateFunction$Type<(T)>, arg2: $ModularBlockStateSmith$ModelFileSupplier$Type, arg3: $Collection$Type<($Supplier$Type<(any)>)>): void
public "act"(arg0: $AbstractBlockStateSmith$StateSmithData$Type, arg1: $ItemModelSmith$Type, arg2: $AbstractBlockStateSmith$StateFunction$Type<(T)>, arg3: $ModularBlockStateSmith$ModelFileSupplier$Type, arg4: $Collection$Type<($Supplier$Type<(any)>)>): void
public "act"(arg0: $AbstractBlockStateSmith$StateSmithData$Type, arg1: $ItemModelSmith$Type, arg2: $AbstractBlockStateSmith$StateFunction$Type<(T)>, arg3: $ModularBlockStateSmith$ModelFileSupplier$Type, ...arg4: ($Supplier$Type<(any)>)[]): void
public "act"(arg0: $AbstractBlockStateSmith$StateSmithData$Type, arg1: $AbstractBlockStateSmith$StateFunction$Type<(T)>, arg2: $ModularBlockStateSmith$ModelFileSupplier$Type, ...arg3: ($Supplier$Type<(any)>)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModularBlockStateSmith$Type<T> = ($ModularBlockStateSmith<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModularBlockStateSmith_<T> = $ModularBlockStateSmith$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/capability/$LodestonePlayerDataCapability" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerEvent$Clone, $PlayerEvent$Clone$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$Clone"
import {$AttachCapabilitiesEvent, $AttachCapabilitiesEvent$Type} from "packages/net/minecraftforge/event/$AttachCapabilitiesEvent"
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"
import {$LodestoneCapability, $LodestoneCapability$Type} from "packages/team/lodestar/lodestone/systems/capability/$LodestoneCapability"
import {$NBTHelper$TagFilter, $NBTHelper$TagFilter$Type} from "packages/team/lodestar/lodestone/helpers/$NBTHelper$TagFilter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PlayerEvent$StartTracking, $PlayerEvent$StartTracking$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$StartTracking"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $LodestonePlayerDataCapability implements $LodestoneCapability {
static "CAPABILITY": $Capability<($LodestonePlayerDataCapability)>
 "hasJoinedBefore": boolean
 "rightClickHeld": boolean
 "rightClickTime": integer
 "leftClickHeld": boolean
 "leftClickTime": integer

constructor()

public static "sync"(arg0: $Player$Type, arg1: $PacketDistributor$PacketTarget$Type): void
public static "sync"(arg0: $Player$Type, arg1: $PacketDistributor$PacketTarget$Type, arg2: $NBTHelper$TagFilter$Type): void
public static "getCapabilityOptional"(arg0: $Player$Type): $LazyOptional<($LodestonePlayerDataCapability)>
public static "syncTrackingAndSelf"(arg0: $Player$Type, arg1: $NBTHelper$TagFilter$Type): void
public static "syncTrackingAndSelf"(arg0: $Player$Type): void
public static "playerJoin"(arg0: $EntityJoinLevelEvent$Type): void
public static "playerTick"(arg0: $TickEvent$PlayerTickEvent$Type): void
public static "syncPlayerCapability"(arg0: $PlayerEvent$StartTracking$Type): void
public static "getCapability"(arg0: $Player$Type): $LodestonePlayerDataCapability
public static "playerClone"(arg0: $PlayerEvent$Clone$Type): void
public static "syncTracking"(arg0: $Player$Type, arg1: $NBTHelper$TagFilter$Type): void
public static "syncTracking"(arg0: $Player$Type): void
public static "syncServer"(arg0: $Player$Type, arg1: $NBTHelper$TagFilter$Type): void
public static "syncServer"(arg0: $Player$Type): void
public static "syncSelf"(arg0: $ServerPlayer$Type): void
public static "syncSelf"(arg0: $ServerPlayer$Type, arg1: $NBTHelper$TagFilter$Type): void
public static "attachPlayerCapability"(arg0: $AttachCapabilitiesEvent$Type<($Entity$Type)>): void
public static "registerCapabilities"(arg0: $RegisterCapabilitiesEvent$Type): void
public "deserializeNBT"(arg0: $CompoundTag$Type): void
public "serializeNBT"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestonePlayerDataCapability$Type = ($LodestonePlayerDataCapability);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestonePlayerDataCapability_ = $LodestonePlayerDataCapability$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/magic/$MagicShovelItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$LodestoneShovelItem, $LodestoneShovelItem$Type} from "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneShovelItem"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MagicShovelItem extends $LodestoneShovelItem {
readonly "magicDamage": float
static "FLATTENABLES": $Map<($Block), ($BlockState)>
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: float, arg4: $Item$Properties$Type)

public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MagicShovelItem$Type = ($MagicShovelItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MagicShovelItem_ = $MagicShovelItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneItemCrumbParticle" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneItemCrumbsParticleOptions, $LodestoneItemCrumbsParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$LodestoneItemCrumbsParticleOptions"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$ParticleEngine$MutableSpriteSet, $ParticleEngine$MutableSpriteSet$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$MutableSpriteSet"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $LodestoneItemCrumbParticle extends $LodestoneWorldParticle {
readonly "renderType": $ParticleRenderType
readonly "behavior": $LodestoneParticleBehavior
readonly "behaviorComponent": $LodestoneBehaviorComponent
readonly "renderLayer": $RenderHandler$LodestoneRenderLayer
readonly "shouldCull": boolean
readonly "spriteSet": $ParticleEngine$MutableSpriteSet
readonly "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
readonly "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
readonly "colorData": $ColorParticleData
readonly "transparencyData": $GenericParticleData
readonly "scaleData": $GenericParticleData
readonly "spinData": $SpinParticleData
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "lifeDelay": integer
 "x": double
 "y": double
 "z": double
 "xd": double
 "yd": double
 "zd": double
 "age": integer
 "rCol": float
 "gCol": float
 "bCol": float

constructor(arg0: $ClientLevel$Type, arg1: $LodestoneItemCrumbsParticleOptions$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double)

public "m_6355_"(arg0: float): integer
public "m_5970_"(): float
public "m_5951_"(): float
public "m_5950_"(): float
public "m_5952_"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemCrumbParticle$Type = ($LodestoneItemCrumbParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemCrumbParticle_ = $LodestoneItemCrumbParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/rendeertype/$RenderTypeData" {
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$LodestoneRenderType, $LodestoneRenderType$Type} from "packages/team/lodestar/lodestone/systems/rendering/$LodestoneRenderType"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"

export class $RenderTypeData {
readonly "name": string
readonly "format": $VertexFormat
readonly "mode": $VertexFormat$Mode
readonly "shader": $RenderStateShard$ShaderStateShard
readonly "texture": $RenderStateShard$EmptyTextureStateShard
readonly "cull": $RenderStateShard$CullStateShard
readonly "lightmap": $RenderStateShard$LightmapStateShard
 "transparency": $RenderStateShard$TransparencyStateShard

constructor(arg0: $LodestoneRenderType$Type)
constructor(arg0: string, arg1: $LodestoneRenderType$Type)
constructor(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: $RenderStateShard$ShaderStateShard$Type, arg4: $RenderStateShard$TransparencyStateShard$Type, arg5: $RenderStateShard$EmptyTextureStateShard$Type, arg6: $RenderStateShard$CullStateShard$Type, arg7: $RenderStateShard$LightmapStateShard$Type)
constructor(arg0: string, arg1: $VertexFormat$Type, arg2: $VertexFormat$Mode$Type, arg3: $RenderStateShard$ShaderStateShard$Type, arg4: $RenderStateShard$EmptyTextureStateShard$Type, arg5: $RenderStateShard$CullStateShard$Type, arg6: $RenderStateShard$LightmapStateShard$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypeData$Type = ($RenderTypeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypeData_ = $RenderTypeData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig" {
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$LodestoneConfig$ConfigPath, $LodestoneConfig$ConfigPath$Type} from "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$ConfigPath"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$LodestoneConfig$ConfigValueHolder, $LodestoneConfig$ConfigValueHolder$Type} from "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$ConfigValueHolder"

export class $LodestoneConfig {
static readonly "VALUE_HOLDERS": $ConcurrentHashMap<($Pair<(string), ($LodestoneConfig$ConfigPath)>), ($ArrayList<($LodestoneConfig$ConfigValueHolder)>)>

constructor(arg0: string, arg1: string, arg2: $ForgeConfigSpec$Builder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneConfig$Type = ($LodestoneConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneConfig_ = $LodestoneConfig$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$BillboardParticleBehavior" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"

export class $BillboardParticleBehavior implements $LodestoneParticleBehavior {


public "render"(arg0: $LodestoneWorldParticle$Type, arg1: $VertexConsumer$Type, arg2: $Camera$Type, arg3: float): void
public "getComponent"(arg0: $LodestoneBehaviorComponent$Type): $LodestoneBehaviorComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BillboardParticleBehavior$Type = ($BillboardParticleBehavior);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BillboardParticleBehavior_ = $BillboardParticleBehavior$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WorldEventInstance, $WorldEventInstance$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventInstance"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WorldEventType$EventInstanceSupplier, $WorldEventType$EventInstanceSupplier$Type} from "packages/team/lodestar/lodestone/systems/worldevent/$WorldEventType$EventInstanceSupplier"

export class $WorldEventType {
readonly "id": $ResourceLocation
readonly "supplier": $WorldEventType$EventInstanceSupplier
readonly "clientSynced": boolean

constructor(arg0: $ResourceLocation$Type, arg1: $WorldEventType$EventInstanceSupplier$Type, arg2: boolean)
constructor(arg0: $ResourceLocation$Type, arg1: $WorldEventType$EventInstanceSupplier$Type)

public "isClientSynced"(): boolean
public "createInstance"(arg0: $CompoundTag$Type): $WorldEventInstance
get "clientSynced"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldEventType$Type = ($WorldEventType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldEventType_ = $WorldEventType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/config/$LodestoneConfig$BuilderSupplier" {
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export interface $LodestoneConfig$BuilderSupplier<T> {

 "createBuilder"(arg0: $ForgeConfigSpec$Builder$Type): $ForgeConfigSpec$ConfigValue<(T)>

(arg0: $ForgeConfigSpec$Builder$Type): $ForgeConfigSpec$ConfigValue<(T)>
}

export namespace $LodestoneConfig$BuilderSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneConfig$BuilderSupplier$Type<T> = ($LodestoneConfig$BuilderSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneConfig$BuilderSupplier_<T> = $LodestoneConfig$BuilderSupplier$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/magic/$MagicPickaxeItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LodestonePickaxeItem, $LodestonePickaxeItem$Type} from "packages/team/lodestar/lodestone/systems/item/tools/$LodestonePickaxeItem"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MagicPickaxeItem extends $LodestonePickaxeItem {
readonly "magicDamage": float
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: float, arg4: $Item$Properties$Type)

public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MagicPickaxeItem$Type = ($MagicPickaxeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MagicPickaxeItem_ = $MagicPickaxeItem$Type;
}}
declare module "packages/team/lodestar/lodestone/network/interaction/$UpdateRightClickPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$LodestoneServerPacket, $LodestoneServerPacket$Type} from "packages/team/lodestar/lodestone/systems/network/$LodestoneServerPacket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $UpdateRightClickPacket extends $LodestoneServerPacket {

constructor(arg0: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $UpdateRightClickPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "register"(arg0: $SimpleChannel$Type, arg1: integer): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateRightClickPacket$Type = ($UpdateRightClickPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateRightClickPacket_ = $UpdateRightClickPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType$ParticleProvider" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ScreenParticle, $ScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/base/$ScreenParticle"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"

export interface $ScreenParticleType$ParticleProvider<T extends $ScreenParticleOptions> {

 "createParticle"(arg0: $ClientLevel$Type, arg1: T, arg2: double, arg3: double, arg4: double, arg5: double): $ScreenParticle

(arg0: $ClientLevel$Type, arg1: T, arg2: double, arg3: double, arg4: double, arg5: double): $ScreenParticle
}

export namespace $ScreenParticleType$ParticleProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleType$ParticleProvider$Type<T> = ($ScreenParticleType$ParticleProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleType$ParticleProvider_<T> = $ScreenParticleType$ParticleProvider$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/$LodestonePickaxeItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$PickaxeItem, $PickaxeItem$Type} from "packages/net/minecraft/world/item/$PickaxeItem"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestonePickaxeItem extends $PickaxeItem {
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: $Item$Properties$Type)

public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestonePickaxeItem$Type = ($LodestonePickaxeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestonePickaxeItem_ = $LodestonePickaxeItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/$ItemModelSmithTypes" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ItemModelSmith, $ItemModelSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$ItemModelSmith"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ItemModelSmithTypes {
static readonly "GENERATED": $ResourceLocation
static readonly "HANDHELD": $ResourceLocation
static "NO_MODEL": $ItemModelSmith
static "HANDHELD_ITEM": $ItemModelSmith
static "GENERATED_ITEM": $ItemModelSmith
static "UNIQUE_ITEM_MODEL": $ItemModelSmith
static "BLOCK_TEXTURE_ITEM": $ItemModelSmith
static "AFFIXED_BLOCK_TEXTURE_MODEL": $Function<(string), ($ItemModelSmith)>
static "BLOCK_MODEL_ITEM": $ItemModelSmith
static "AFFIXED_BLOCK_MODEL": $Function<(string), ($ItemModelSmith)>
static "CROSS_MODEL_ITEM": $ItemModelSmith
static "WALL_ITEM": $ItemModelSmith
static "FENCE_ITEM": $ItemModelSmith
static "BUTTON_ITEM": $ItemModelSmith
static "TRAPDOOR_ITEM": $ItemModelSmith

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelSmithTypes$Type = ($ItemModelSmithTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelSmithTypes_ = $ItemModelSmithTypes$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneHoeItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$HoeItem, $HoeItem$Type} from "packages/net/minecraft/world/item/$HoeItem"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneHoeItem extends $HoeItem {
/**
 * 
 * @deprecated
 */
static "TILLABLES": $Map<($Block), ($Pair<($Predicate<($UseOnContext)>), ($Consumer<($UseOnContext)>)>)>
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: integer, arg2: float, arg3: $Item$Properties$Type)

public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneHoeItem$Type = ($LodestoneHoeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneHoeItem_ = $LodestoneHoeItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/client/$ClientTickCounter" {
import {$TickEvent$RenderTickEvent, $TickEvent$RenderTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$RenderTickEvent"

export class $ClientTickCounter {
static "ticksInGame": long
static "partialTicks": float

constructor()

public static "renderTick"(arg0: $TickEvent$RenderTickEvent$Type): void
public static "clientTick"(): void
public static "getTotal"(): float
get "total"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickCounter$Type = ($ClientTickCounter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickCounter_ = $ClientTickCounter$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$VFXBuilders$WorldVFXBuilder, $VFXBuilders$WorldVFXBuilder$Type} from "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$WorldVFXBuilder"

export interface $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor {

 "placeVertex"(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: $VFXBuilders$WorldVFXBuilder$Type, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): void

(arg0: $VertexConsumer$Type, arg1: $Matrix4f$Type, arg2: $VFXBuilders$WorldVFXBuilder$Type, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): void
}

export namespace $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor$Type = ($VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor_ = $VFXBuilders$WorldVFXBuilder$WorldVertexConsumerActor$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneTerrainParticle" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$LodestoneTerrainParticleOptions, $LodestoneTerrainParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/world/options/$LodestoneTerrainParticleOptions"
import {$LodestoneBehaviorComponent, $LodestoneBehaviorComponent$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/components/$LodestoneBehaviorComponent"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ParticleRenderType, $ParticleRenderType$Type} from "packages/net/minecraft/client/particle/$ParticleRenderType"
import {$ParticleEngine$MutableSpriteSet, $ParticleEngine$MutableSpriteSet$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$MutableSpriteSet"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SpinParticleData, $SpinParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/spin/$SpinParticleData"
import {$LodestoneParticleBehavior, $LodestoneParticleBehavior$Type} from "packages/team/lodestar/lodestone/systems/particle/world/behaviors/$LodestoneParticleBehavior"
import {$RenderHandler$LodestoneRenderLayer, $RenderHandler$LodestoneRenderLayer$Type} from "packages/team/lodestar/lodestone/handlers/$RenderHandler$LodestoneRenderLayer"
import {$SimpleParticleOptions$ParticleDiscardFunctionType, $SimpleParticleOptions$ParticleDiscardFunctionType$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleDiscardFunctionType"
import {$LodestoneWorldParticle, $LodestoneWorldParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/world/$LodestoneWorldParticle"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $LodestoneTerrainParticle extends $LodestoneWorldParticle {
readonly "renderType": $ParticleRenderType
readonly "behavior": $LodestoneParticleBehavior
readonly "behaviorComponent": $LodestoneBehaviorComponent
readonly "renderLayer": $RenderHandler$LodestoneRenderLayer
readonly "shouldCull": boolean
readonly "spriteSet": $ParticleEngine$MutableSpriteSet
readonly "spritePicker": $SimpleParticleOptions$ParticleSpritePicker
readonly "discardFunctionType": $SimpleParticleOptions$ParticleDiscardFunctionType
readonly "colorData": $ColorParticleData
readonly "transparencyData": $GenericParticleData
readonly "scaleData": $GenericParticleData
readonly "spinData": $SpinParticleData
readonly "tickActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
readonly "renderActors": $Collection<($Consumer<($LodestoneWorldParticle)>)>
 "lifeDelay": integer
 "x": double
 "y": double
 "z": double
 "xd": double
 "yd": double
 "zd": double
 "age": integer
 "rCol": float
 "gCol": float
 "bCol": float

constructor(arg0: $ClientLevel$Type, arg1: $LodestoneTerrainParticleOptions$Type, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double)

public "m_6355_"(arg0: float): integer
public "m_5970_"(): float
public "m_5951_"(): float
public "m_5950_"(): float
public "m_5952_"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTerrainParticle$Type = ($LodestoneTerrainParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTerrainParticle_ = $LodestoneTerrainParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/sound/$ExtendedSoundType" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$SoundType, $SoundType$Type} from "packages/net/minecraft/world/level/block/$SoundType"
import {$ForgeSoundType, $ForgeSoundType$Type} from "packages/net/minecraftforge/common/util/$ForgeSoundType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$SoundSource, $SoundSource$Type} from "packages/net/minecraft/sounds/$SoundSource"

export class $ExtendedSoundType extends $ForgeSoundType {
static readonly "EMPTY": $SoundType
static readonly "WOOD": $SoundType
static readonly "GRAVEL": $SoundType
static readonly "GRASS": $SoundType
static readonly "LILY_PAD": $SoundType
static readonly "STONE": $SoundType
static readonly "METAL": $SoundType
static readonly "GLASS": $SoundType
static readonly "WOOL": $SoundType
static readonly "SAND": $SoundType
static readonly "SNOW": $SoundType
static readonly "POWDER_SNOW": $SoundType
static readonly "LADDER": $SoundType
static readonly "ANVIL": $SoundType
static readonly "SLIME_BLOCK": $SoundType
static readonly "HONEY_BLOCK": $SoundType
static readonly "WET_GRASS": $SoundType
static readonly "CORAL_BLOCK": $SoundType
static readonly "BAMBOO": $SoundType
static readonly "BAMBOO_SAPLING": $SoundType
static readonly "SCAFFOLDING": $SoundType
static readonly "SWEET_BERRY_BUSH": $SoundType
static readonly "CROP": $SoundType
static readonly "HARD_CROP": $SoundType
static readonly "VINE": $SoundType
static readonly "NETHER_WART": $SoundType
static readonly "LANTERN": $SoundType
static readonly "STEM": $SoundType
static readonly "NYLIUM": $SoundType
static readonly "FUNGUS": $SoundType
static readonly "ROOTS": $SoundType
static readonly "SHROOMLIGHT": $SoundType
static readonly "WEEPING_VINES": $SoundType
static readonly "TWISTING_VINES": $SoundType
static readonly "SOUL_SAND": $SoundType
static readonly "SOUL_SOIL": $SoundType
static readonly "BASALT": $SoundType
static readonly "WART_BLOCK": $SoundType
static readonly "NETHERRACK": $SoundType
static readonly "NETHER_BRICKS": $SoundType
static readonly "NETHER_SPROUTS": $SoundType
static readonly "NETHER_ORE": $SoundType
static readonly "BONE_BLOCK": $SoundType
static readonly "NETHERITE_BLOCK": $SoundType
static readonly "ANCIENT_DEBRIS": $SoundType
static readonly "LODESTONE": $SoundType
static readonly "CHAIN": $SoundType
static readonly "NETHER_GOLD_ORE": $SoundType
static readonly "GILDED_BLACKSTONE": $SoundType
static readonly "CANDLE": $SoundType
static readonly "AMETHYST": $SoundType
static readonly "AMETHYST_CLUSTER": $SoundType
static readonly "SMALL_AMETHYST_BUD": $SoundType
static readonly "MEDIUM_AMETHYST_BUD": $SoundType
static readonly "LARGE_AMETHYST_BUD": $SoundType
static readonly "TUFF": $SoundType
static readonly "CALCITE": $SoundType
static readonly "DRIPSTONE_BLOCK": $SoundType
static readonly "POINTED_DRIPSTONE": $SoundType
static readonly "COPPER": $SoundType
static readonly "CAVE_VINES": $SoundType
static readonly "SPORE_BLOSSOM": $SoundType
static readonly "AZALEA": $SoundType
static readonly "FLOWERING_AZALEA": $SoundType
static readonly "MOSS_CARPET": $SoundType
static readonly "PINK_PETALS": $SoundType
static readonly "MOSS": $SoundType
static readonly "BIG_DRIPLEAF": $SoundType
static readonly "SMALL_DRIPLEAF": $SoundType
static readonly "ROOTED_DIRT": $SoundType
static readonly "HANGING_ROOTS": $SoundType
static readonly "AZALEA_LEAVES": $SoundType
static readonly "SCULK_SENSOR": $SoundType
static readonly "SCULK_CATALYST": $SoundType
static readonly "SCULK": $SoundType
static readonly "SCULK_VEIN": $SoundType
static readonly "SCULK_SHRIEKER": $SoundType
static readonly "GLOW_LICHEN": $SoundType
static readonly "DEEPSLATE": $SoundType
static readonly "DEEPSLATE_BRICKS": $SoundType
static readonly "DEEPSLATE_TILES": $SoundType
static readonly "POLISHED_DEEPSLATE": $SoundType
static readonly "FROGLIGHT": $SoundType
static readonly "FROGSPAWN": $SoundType
static readonly "MANGROVE_ROOTS": $SoundType
static readonly "MUDDY_MANGROVE_ROOTS": $SoundType
static readonly "MUD": $SoundType
static readonly "MUD_BRICKS": $SoundType
static readonly "PACKED_MUD": $SoundType
static readonly "HANGING_SIGN": $SoundType
static readonly "NETHER_WOOD_HANGING_SIGN": $SoundType
static readonly "BAMBOO_WOOD_HANGING_SIGN": $SoundType
static readonly "BAMBOO_WOOD": $SoundType
static readonly "NETHER_WOOD": $SoundType
static readonly "CHERRY_WOOD": $SoundType
static readonly "CHERRY_SAPLING": $SoundType
static readonly "CHERRY_LEAVES": $SoundType
static readonly "CHERRY_WOOD_HANGING_SIGN": $SoundType
static readonly "CHISELED_BOOKSHELF": $SoundType
static readonly "SUSPICIOUS_SAND": $SoundType
static readonly "SUSPICIOUS_GRAVEL": $SoundType
static readonly "DECORATED_POT": $SoundType
static readonly "DECORATED_POT_CRACKED": $SoundType
readonly "volume": float
readonly "pitch": float

constructor(arg0: float, arg1: float, arg2: $Supplier$Type<($SoundEvent$Type)>, arg3: $Supplier$Type<($SoundEvent$Type)>, arg4: $Supplier$Type<($SoundEvent$Type)>, arg5: $Supplier$Type<($SoundEvent$Type)>, arg6: $Supplier$Type<($SoundEvent$Type)>)

public "onPlayHitSound"(arg0: $BlockPos$Type): void
public "onPlayBreakSound"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public "onPlayPlaceSound"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Player$Type): void
public "onPlayStepSound"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $SoundSource$Type): void
public "onPlayFallSound"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $SoundSource$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedSoundType$Type = ($ExtendedSoundType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedSoundType_ = $ExtendedSoundType$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/$LodestoneEntityBlock" {
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$LodestoneBlockEntity, $LodestoneBlockEntity$Type} from "packages/team/lodestar/lodestone/systems/blockentity/$LodestoneBlockEntity"
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $LodestoneEntityBlock<T extends $LodestoneBlockEntity> extends $Block implements $EntityBlock {
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getTicker"<Y extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(Y)>): $BlockEntityTicker<(Y)>
public "setBlockEntity"(arg0: $Supplier$Type<($BlockEntityType$Type<(T)>)>): $LodestoneEntityBlock<(T)>
public "setPlacedBy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $LivingEntity$Type, arg4: $ItemStack$Type): void
public "playerWillDestroy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type): void
public "getCloneItemStack"(arg0: $BlockState$Type, arg1: $HitResult$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: $Player$Type): $ItemStack
public "onBlockExploded"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Explosion$Type): void
public "neighborChanged"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Block$Type, arg4: $BlockPos$Type, arg5: boolean): void
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "entityInside"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): void
public "onBlockBroken"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Player$Type): void
public "hasTileEntity"(arg0: $BlockState$Type): boolean
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
set "blockEntity"(value: $Supplier$Type<($BlockEntityType$Type<(T)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneEntityBlock$Type<T> = ($LodestoneEntityBlock<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneEntityBlock_<T> = $LodestoneEntityBlock$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$ItemModelSmith" {
import {$ItemModelSmith$ItemModelSupplier, $ItemModelSmith$ItemModelSupplier$Type} from "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$ItemModelSmith$ItemModelSupplier"
import {$AbstractItemModelSmith$ItemModelSmithData, $AbstractItemModelSmith$ItemModelSmithData$Type} from "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$AbstractItemModelSmith$ItemModelSmithData"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractItemModelSmith, $AbstractItemModelSmith$Type} from "packages/team/lodestar/lodestone/systems/datagen/itemsmith/$AbstractItemModelSmith"
import {$LodestoneItemModelProvider, $LodestoneItemModelProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneItemModelProvider"

export class $ItemModelSmith extends $AbstractItemModelSmith {
readonly "modelSupplier": $ItemModelSmith$ItemModelSupplier

constructor(arg0: $ItemModelSmith$ItemModelSupplier$Type)

public "act"(arg0: $Supplier$Type<(any)>, arg1: $LodestoneItemModelProvider$Type): void
public "act"(arg0: $AbstractItemModelSmith$ItemModelSmithData$Type, arg1: $Collection$Type<($Supplier$Type<(any)>)>): void
public "act"(arg0: $AbstractItemModelSmith$ItemModelSmithData$Type, ...arg1: ($Supplier$Type<(any)>)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModelSmith$Type = ($ItemModelSmith);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModelSmith_ = $ItemModelSmith$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/client/$LodestoneFireEffectRendererRegistry" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$FireEffectRenderer, $FireEffectRenderer$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectRenderer"
import {$FireEffectType, $FireEffectType$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectType"
import {$FireEffectInstance, $FireEffectInstance$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectInstance"

export class $LodestoneFireEffectRendererRegistry {
static "RENDERERS": $HashMap<($FireEffectType), ($FireEffectRenderer<($FireEffectInstance)>)>

constructor()

public static "registerRenderer"(arg0: $FireEffectType$Type, arg1: $FireEffectRenderer$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneFireEffectRendererRegistry$Type = ($LodestoneFireEffectRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneFireEffectRendererRegistry_ = $LodestoneFireEffectRendererRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/magic/$MagicAxeItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$LodestoneAxeItem, $LodestoneAxeItem$Type} from "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneAxeItem"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MagicAxeItem extends $LodestoneAxeItem {
readonly "magicDamage": float
static "STRIPPABLES": $Map<($Block), ($Block)>
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: float, arg2: float, arg3: float, arg4: $Item$Properties$Type)

public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MagicAxeItem$Type = ($MagicAxeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MagicAxeItem_ = $MagicAxeItem$Type;
}}
declare module "packages/team/lodestar/lodestone/handlers/$GhostBlockHandler$GhostBlockEntry" {
import {$GhostBlockRenderer, $GhostBlockRenderer$Type} from "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockRenderer"
import {$GhostBlockOptions, $GhostBlockOptions$Type} from "packages/team/lodestar/lodestone/systems/rendering/ghost/$GhostBlockOptions"

export class $GhostBlockHandler$GhostBlockEntry {

constructor(arg0: $GhostBlockRenderer$Type, arg1: $GhostBlockOptions$Type, arg2: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GhostBlockHandler$GhostBlockEntry$Type = ($GhostBlockHandler$GhostBlockEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GhostBlockHandler$GhostBlockEntry_ = $GhostBlockHandler$GhostBlockEntry$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$DataHelper" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $DataHelper {

constructor()

public static "toTitleCase"(arg0: string, arg1: string): string
public static "distance"(...arg0: (float)[]): float
public static "reverseOrder"<T, K extends $Collection<(T)>>(arg0: K, arg1: $Collection$Type<(T)>): K
public static "getAll"<T>(arg0: $Collection$Type<(any)>, ...arg1: (T)[]): $Collection<(T)>
public static "getAll"<T>(arg0: $Collection$Type<(T)>, arg1: $Predicate$Type<(T)>): $Collection<(T)>
public static "take"<T>(arg0: $Collection$Type<(any)>, arg1: T): T
public static "takeAll"<T>(arg0: $Collection$Type<(any)>, ...arg1: (T)[]): $Collection<(T)>
public static "takeAll"<T>(arg0: $Collection$Type<(T)>, arg1: $Predicate$Type<(T)>): $Collection<(T)>
public static "radialOffset"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float): $Vec3
public static "hasDuplicate"<T>(arg0: (T)[]): boolean
public static "nextInts"(arg0: integer, arg1: integer): (integer)[]
public static "distSqr"(...arg0: (float)[]): float
public static "rotatingRadialOffsets"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: long, arg4: float): $ArrayList<($Vec3)>
public static "rotatingRadialOffsets"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float, arg4: long, arg5: float): $ArrayList<($Vec3)>
public static "blockOutlinePositions"(arg0: $Level$Type, arg1: $BlockPos$Type): $ArrayList<($Vec3)>
public static "rotatingRadialOffset"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float, arg4: long, arg5: float): $Vec3
public static "rotatingRadialOffset"(arg0: $Vec3$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: long, arg6: float): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataHelper$Type = ($DataHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataHelper_ = $DataHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneRecipeSerializerRegistry" {
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$NBTCarryRecipe, $NBTCarryRecipe$Type} from "packages/team/lodestar/lodestone/recipe/$NBTCarryRecipe"

export class $LodestoneRecipeSerializerRegistry {
static readonly "RECIPE_SERIALIZERS": $DeferredRegister<($RecipeSerializer<(any)>)>
static readonly "NBT_CARRY_RECIPE_SERIALIZER": $RegistryObject<($RecipeSerializer<($NBTCarryRecipe)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneRecipeSerializerRegistry$Type = ($LodestoneRecipeSerializerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneRecipeSerializerRegistry_ = $LodestoneRecipeSerializerRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/postprocess/$MultiInstancePostProcessor" {
import {$DynamicShaderFxInstance, $DynamicShaderFxInstance$Type} from "packages/team/lodestar/lodestone/systems/postprocess/$DynamicShaderFxInstance"
import {$PostProcessor, $PostProcessor$Type} from "packages/team/lodestar/lodestone/systems/postprocess/$PostProcessor"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Uniform, $Uniform$Type} from "packages/com/mojang/blaze3d/shaders/$Uniform"

export class $MultiInstancePostProcessor<I extends $DynamicShaderFxInstance> extends $PostProcessor {
static readonly "COMMON_UNIFORMS": $Collection<($Pair<(string), ($Consumer<($Uniform)>)>)>
static "viewModelStack": $PoseStack

constructor()

public "init"(): void
public "beforeProcess"(arg0: $PoseStack$Type): void
public "addFxInstance"(arg0: I): I
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiInstancePostProcessor$Type<I> = ($MultiInstancePostProcessor<(I)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiInstancePostProcessor_<I> = $MultiInstancePostProcessor$Type<(I)>;
}}
declare module "packages/team/lodestar/lodestone/registry/client/$LodestoneShaderRegistry" {
import {$RegisterShadersEvent, $RegisterShadersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterShadersEvent"
import {$ShaderHolder, $ShaderHolder$Type} from "packages/team/lodestar/lodestone/systems/rendering/shader/$ShaderHolder"

export class $LodestoneShaderRegistry {
static "LODESTONE_TEXTURE": $ShaderHolder
static "LODESTONE_TEXT": $ShaderHolder
static "PARTICLE": $ShaderHolder
static "SCREEN_PARTICLE": $ShaderHolder
static "DISTORTED_TEXTURE": $ShaderHolder
static "SCROLLING_TEXTURE": $ShaderHolder
static "TRIANGLE_TEXTURE": $ShaderHolder
static "SCROLLING_TRIANGLE_TEXTURE": $ShaderHolder

constructor()

public static "registerShader"(arg0: $RegisterShadersEvent$Type, arg1: $ShaderHolder$Type): void
public static "shaderRegistry"(arg0: $RegisterShadersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneShaderRegistry$Type = ($LodestoneShaderRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneShaderRegistry_ = $LodestoneShaderRegistry$Type;
}}
declare module "packages/team/lodestar/lodestone/command/$DevWorldSetupCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $DevWorldSetupCommand {

constructor()

public static "register"(): $LiteralArgumentBuilder<($CommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DevWorldSetupCommand$Type = ($DevWorldSetupCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DevWorldSetupCommand_ = $DevWorldSetupCommand$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/block/sign/$LodestoneStandingSignBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$WoodType, $WoodType$Type} from "packages/net/minecraft/world/level/block/state/properties/$WoodType"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StandingSignBlock, $StandingSignBlock$Type} from "packages/net/minecraft/world/level/block/$StandingSignBlock"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LodestoneStandingSignBlock extends $StandingSignBlock implements $EntityBlock {
static readonly "ROTATION": $IntegerProperty
static readonly "WATERLOGGED": $BooleanProperty
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type, arg1: $WoodType$Type)

public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneStandingSignBlock$Type = ($LodestoneStandingSignBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneStandingSignBlock_ = $LodestoneStandingSignBlock$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$LodestoneBoatItem" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LodestoneBoatEntity, $LodestoneBoatEntity$Type} from "packages/team/lodestar/lodestone/systems/entity/$LodestoneBoatEntity"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneBoatItem extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type, arg1: $RegistryObject$Type<($EntityType$Type<($LodestoneBoatEntity$Type)>)>)

public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBoatItem$Type = ($LodestoneBoatItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBoatItem_ = $LodestoneBoatItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$IModularMultiBlockCore" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$IMultiBlockCore, $IMultiBlockCore$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$IMultiBlockCore"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MultiBlockStructure, $MultiBlockStructure$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure"

export interface $IModularMultiBlockCore extends $IMultiBlockCore {

 "connect"(arg0: $BlockPos$Type): void
 "reset"(): void
 "separate"(arg0: $BlockPos$Type): void
 "isModular"(): boolean
 "getStructure"(): $MultiBlockStructure
 "getComponentPositions"(): $ArrayList<($BlockPos)>
 "destroyMultiblock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
 "setupMultiblock"(arg0: $BlockPos$Type): void
}

export namespace $IModularMultiBlockCore {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModularMultiBlockCore$Type = ($IModularMultiBlockCore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModularMultiBlockCore_ = $IModularMultiBlockCore$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$AbstractBlockStateSmith$StateSmithData" {
import {$LodestoneBlockStateProvider, $LodestoneBlockStateProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockStateProvider"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $AbstractBlockStateSmith$StateSmithData {
readonly "provider": $LodestoneBlockStateProvider
readonly "consumer": $Consumer<($Supplier<(any)>)>

constructor(arg0: $LodestoneBlockStateProvider$Type, arg1: $Consumer$Type<($Supplier$Type<(any)>)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBlockStateSmith$StateSmithData$Type = ($AbstractBlockStateSmith$StateSmithData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBlockStateSmith$StateSmithData_ = $AbstractBlockStateSmith$StateSmithData$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$LodestoneItemProperties" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$BuildCreativeModeTabContentsEvent, $BuildCreativeModeTabContentsEvent$Type} from "packages/net/minecraftforge/event/$BuildCreativeModeTabContentsEvent"

export class $LodestoneItemProperties extends $Item$Properties {
static readonly "TAB_SORTING": $Map<($ResourceKey<($CreativeModeTab)>), ($List<($ResourceLocation)>)>
readonly "tab": $ResourceKey<($CreativeModeTab)>

constructor(arg0: $RegistryObject$Type<($CreativeModeTab$Type)>)
constructor(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>)

public static "populateItemGroups"(arg0: $BuildCreativeModeTabContentsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneItemProperties$Type = ($LodestoneItemProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneItemProperties_ = $LodestoneItemProperties$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$ILodestoneMultiblockComponent" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ILodestoneMultiblockComponent {

}

export namespace $ILodestoneMultiblockComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILodestoneMultiblockComponent$Type = ($ILodestoneMultiblockComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILodestoneMultiblockComponent_ = $ILodestoneMultiblockComponent$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$IMultiBlockCore" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MultiBlockStructure, $MultiBlockStructure$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure"

export interface $IMultiBlockCore {

 "getStructure"(): $MultiBlockStructure
 "getComponentPositions"(): $ArrayList<($BlockPos)>
 "isModular"(): boolean
 "destroyMultiblock"(arg0: $Player$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
 "setupMultiblock"(arg0: $BlockPos$Type): void
}

export namespace $IMultiBlockCore {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMultiBlockCore$Type = ($IMultiBlockCore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMultiBlockCore_ = $IMultiBlockCore$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/network/$LodestoneServerPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LodestoneServerPacket {

constructor()

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "execute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneServerPacket$Type = ($LodestoneServerPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneServerPacket_ = $LodestoneServerPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/data/$LodestoneLangDatagen" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$LanguageProvider, $LanguageProvider$Type} from "packages/net/minecraftforge/common/data/$LanguageProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"

export class $LodestoneLangDatagen extends $LanguageProvider {

constructor(arg0: $PackOutput$Type)

public static "getCommand"(arg0: string): string
public static "getOption"(arg0: string): string
public "getName"(): string
public "addOption"(arg0: string, arg1: string): void
public static "getOptionTooltip"(arg0: string): string
public "addCommand"(arg0: string, arg1: string): void
public "addOptionTooltip"(arg0: string, arg1: string): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneLangDatagen$Type = ($LodestoneLangDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneLangDatagen_ = $LodestoneLangDatagen$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/model/obj/$ObjModel" {
import {$Face, $Face$Type} from "packages/team/lodestar/lodestone/systems/model/obj/$Face"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $ObjModel {
 "faces": $ArrayList<($Face)>
 "modelLocation": $ResourceLocation

constructor(arg0: $ResourceLocation$Type)

public "loadModel"(): void
public "renderModel"(arg0: $PoseStack$Type, arg1: $RenderType$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjModel$Type = ($ObjModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjModel_ = $ObjModel$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/$VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier" {
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"

export interface $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier {

 "placeVertex"(arg0: $BufferBuilder$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float): void

(arg0: $BufferBuilder$Type, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: float): void
}

export namespace $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier$Type = ($VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier_ = $VFXBuilders$ScreenVFXBuilder$ScreenVertexPlacementSupplier$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$EasingHelper" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"

export class $EasingHelper {

constructor()

public static "weightedEasingLerp"(arg0: $Easing$Type, arg1: double, arg2: double, arg3: double): float
public static "weightedEasingLerp"(arg0: $Easing$Type, arg1: float, arg2: float, arg3: float): float
public static "weightedEasingLerp"(arg0: $Easing$Type, arg1: float, arg2: float, arg3: float, arg4: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EasingHelper$Type = ($EasingHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EasingHelper_ = $EasingHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/worldgen/$LodestoneBlockFiller$LodestoneLayerToken" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"

export class $LodestoneBlockFiller$LodestoneLayerToken {
readonly "index": $UUID

constructor(arg0: $UUID$Type)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockFiller$LodestoneLayerToken$Type = ($LodestoneBlockFiller$LodestoneLayerToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockFiller$LodestoneLayerToken_ = $LodestoneBlockFiller$LodestoneLayerToken$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectInstance" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$FireEffectType, $FireEffectType$Type} from "packages/team/lodestar/lodestone/systems/fireeffect/$FireEffectType"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $FireEffectInstance {
 "duration": integer
readonly "type": $FireEffectType

constructor(arg0: $FireEffectType$Type)

public "sync"(arg0: $Entity$Type): $FireEffectInstance
public "tick"(arg0: $Entity$Type): void
public "isValid"(): boolean
public "setDuration"(arg0: integer): $FireEffectInstance
public "syncDuration"(arg0: $Entity$Type): $FireEffectInstance
public "canDamageTarget"(arg0: $Entity$Type): boolean
public "extendDuration"(arg0: integer): $FireEffectInstance
public "entityAttack"(): void
public static "deserializeNBT"(arg0: $CompoundTag$Type): $FireEffectInstance
public "serializeNBT"(arg0: $CompoundTag$Type): void
get "valid"(): boolean
set "duration"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FireEffectInstance$Type = ($FireEffectInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FireEffectInstance_ = $FireEffectInstance$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/network/$LodestoneTwoWayPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LodestoneTwoWayPacket {

constructor()

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "serverExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public "clientExecute"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneTwoWayPacket$Type = ($LodestoneTwoWayPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneTwoWayPacket_ = $LodestoneTwoWayPacket$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/tag/$LodestoneBlockTags" {
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LodestoneBlockTags {

constructor()

public static "forgeTag"(arg0: string): $TagKey<($Block)>
public static "modTag"(arg0: string): $TagKey<($Block)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneBlockTags$Type = ($LodestoneBlockTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneBlockTags_ = $LodestoneBlockTags$Type;
}}
declare module "packages/team/lodestar/lodestone/events/$SetupEvents" {
import {$RegisterCapabilitiesEvent, $RegisterCapabilitiesEvent$Type} from "packages/net/minecraftforge/common/capabilities/$RegisterCapabilitiesEvent"
import {$InterModEnqueueEvent, $InterModEnqueueEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$InterModEnqueueEvent"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $SetupEvents {

constructor()

public static "registerCommon"(arg0: $FMLCommonSetupEvent$Type): void
public static "registerCapabilities"(arg0: $RegisterCapabilitiesEvent$Type): void
public static "lateSetup"(arg0: $InterModEnqueueEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SetupEvents$Type = ($SetupEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SetupEvents_ = $SetupEvents$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/tools/$LodestoneShovelItem" {
import {$ImmutableMultimap$Builder, $ImmutableMultimap$Builder$Type} from "packages/com/google/common/collect/$ImmutableMultimap$Builder"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$ShovelItem, $ShovelItem$Type} from "packages/net/minecraft/world/item/$ShovelItem"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Tier, $Tier$Type} from "packages/net/minecraft/world/item/$Tier"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LodestoneShovelItem extends $ShovelItem {
static "FLATTENABLES": $Map<($Block), ($BlockState)>
 "speed": float
 "defaultModifiers": $Multimap<($Attribute), ($AttributeModifier)>
 "tier": $Tier
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Tier$Type, arg1: float, arg2: float, arg3: $Item$Properties$Type)

public "getDefaultAttributeModifiers"(arg0: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public "createExtraAttributes"(): $ImmutableMultimap$Builder<($Attribute), ($AttributeModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneShovelItem$Type = ($LodestoneShovelItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneShovelItem_ = $LodestoneShovelItem$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/item/$IEventResponderItem" {
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $IEventResponderItem {

 "hurtEvent"(arg0: $LivingEntity$Type, arg1: $LivingEntity$Type, arg2: $ItemStack$Type): void
 "hurtEvent"(arg0: $LivingHurtEvent$Type, arg1: $LivingEntity$Type, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "takeDamageEvent"(arg0: $LivingHurtEvent$Type, arg1: $LivingEntity$Type, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "takeDamageEvent"(arg0: $LivingEntity$Type, arg1: $LivingEntity$Type, arg2: $ItemStack$Type): void
 "killEvent"(arg0: $LivingDeathEvent$Type, arg1: $LivingEntity$Type, arg2: $LivingEntity$Type, arg3: $ItemStack$Type): void
 "killEvent"(arg0: $LivingEntity$Type, arg1: $LivingEntity$Type, arg2: $ItemStack$Type): void
}

export namespace $IEventResponderItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEventResponderItem$Type = ($IEventResponderItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEventResponderItem_ = $IEventResponderItem$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/block/$BlockEntityHelper" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $BlockEntityHelper {

constructor()

public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: $Predicate$Type<(T)>): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: $Predicate$Type<(T)>): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: $Predicate$Type<(T)>): $Collection<(T)>
public static "getBlockEntities"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $AABB$Type): $Collection<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: $Predicate$Type<(T)>): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: $Predicate$Type<(T)>): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer, arg5: $Predicate$Type<(T)>): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $AABB$Type): $Stream<(T)>
public static "getBlockEntitiesStream"<T>(arg0: $Class$Type<(T)>, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer): $Stream<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityHelper$Type = ($BlockEntityHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityHelper_ = $BlockEntityHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/block/$BlockPosHelper" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockPosHelper {

constructor()

public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer): $Stream<($BlockPos)>
public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getPlaneOfBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float, arg2: float): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float, arg2: float, arg3: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getSphereOfBlocksStream"(arg0: $BlockPos$Type, arg1: float, arg2: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getNeighboringBlocksStream"(arg0: $BlockPos$Type): $Stream<($BlockPos)>
public static "getNeighboringBlocks"(arg0: $BlockPos$Type): $Collection<($BlockPos)>
public static "fromBlockPos"(arg0: $BlockPos$Type): $Vec3
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer): $Collection<($BlockPos)>
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $Collection<($BlockPos)>
public static "getBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer): $Stream<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): $Stream<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "getBlocksStream"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Stream<($BlockPos)>
public static "withinBlock"(arg0: $RandomSource$Type, arg1: $BlockPos$Type): $Vec3
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer): $Collection<($BlockPos)>
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $Collection<($BlockPos)>
public static "getPlaneOfBlocks"(arg0: $BlockPos$Type, arg1: integer, arg2: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float, arg2: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float, arg2: float, arg3: $Predicate$Type<($BlockPos$Type)>): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float): $Collection<($BlockPos)>
public static "getSphereOfBlocks"(arg0: $BlockPos$Type, arg1: float, arg2: float): $Collection<($BlockPos)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockPosHelper$Type = ($BlockPosHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockPosHelper_ = $BlockPosHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/datagen/statesmith/$BlockStateSmith$SmithStateSupplier" {
import {$LodestoneBlockStateProvider, $LodestoneBlockStateProvider$Type} from "packages/team/lodestar/lodestone/systems/datagen/providers/$LodestoneBlockStateProvider"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BlockStateSmith$SmithStateSupplier<T extends $Block> {

 "act"(arg0: T, arg1: $LodestoneBlockStateProvider$Type): void

(arg0: T, arg1: $LodestoneBlockStateProvider$Type): void
}

export namespace $BlockStateSmith$SmithStateSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateSmith$SmithStateSupplier$Type<T> = ($BlockStateSmith$SmithStateSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateSmith$SmithStateSupplier_<T> = $BlockStateSmith$SmithStateSupplier$Type<(T)>;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/screen/$GenericScreenParticle" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$TextureSheetScreenParticle, $TextureSheetScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/base/$TextureSheetScreenParticle"
import {$ParticleEngine$MutableSpriteSet, $ParticleEngine$MutableSpriteSet$Type} from "packages/net/minecraft/client/particle/$ParticleEngine$MutableSpriteSet"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$LodestoneScreenParticleRenderType, $LodestoneScreenParticleRenderType$Type} from "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneScreenParticleRenderType"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"

export class $GenericScreenParticle extends $TextureSheetScreenParticle {
readonly "level": $ClientLevel
 "xOld": double
 "yOld": double
 "x": double
 "y": double
 "xMotion": double
 "yMotion": double
 "xMoved": double
 "yMoved": double
 "removed": boolean
readonly "random": $RandomSource
 "age": integer
 "lifetime": integer
 "gravity": float
 "size": float
 "rCol": float
 "gCol": float
 "bCol": float
 "alpha": float
 "roll": float
 "oRoll": float
 "friction": float

constructor(arg0: $ClientLevel$Type, arg1: $ScreenParticleOptions$Type, arg2: $ParticleEngine$MutableSpriteSet$Type, arg3: double, arg4: double, arg5: double, arg6: double)

public "tick"(): void
public "getCurve"(arg0: float): float
public "render"(arg0: $BufferBuilder$Type): void
public "getRenderType"(): $LodestoneScreenParticleRenderType
public "pickSprite"(arg0: integer): void
public "getSpritePicker"(): $SimpleParticleOptions$ParticleSpritePicker
public "pickColor"(arg0: float): void
public "setParticleSpeed"(arg0: $Vector3d$Type): void
get "renderType"(): $LodestoneScreenParticleRenderType
get "spritePicker"(): $SimpleParticleOptions$ParticleSpritePicker
set "particleSpeed"(value: $Vector3d$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericScreenParticle$Type = ($GenericScreenParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericScreenParticle_ = $GenericScreenParticle$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/multiblock/$HorizontalDirectionStructure" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$MultiBlockStructure$StructurePiece, $MultiBlockStructure$StructurePiece$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure$StructurePiece"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$MultiBlockStructure, $MultiBlockStructure$Type} from "packages/team/lodestar/lodestone/systems/multiblock/$MultiBlockStructure"

export class $HorizontalDirectionStructure extends $MultiBlockStructure {
readonly "structurePieces": $ArrayList<($MultiBlockStructure$StructurePiece)>

constructor(arg0: $ArrayList$Type<($MultiBlockStructure$StructurePiece$Type)>)

public static "of"(...arg0: ($MultiBlockStructure$StructurePiece$Type)[]): $HorizontalDirectionStructure
public "place"(arg0: $BlockPlaceContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HorizontalDirectionStructure$Type = ($HorizontalDirectionStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HorizontalDirectionStructure_ = $HorizontalDirectionStructure$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/particle/builder/$ScreenParticleBuilder" {
import {$GenericParticleData, $GenericParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/$GenericParticleData"
import {$ScreenParticleOptions, $ScreenParticleOptions$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleOptions"
import {$LodestoneScreenParticleRenderType, $LodestoneScreenParticleRenderType$Type} from "packages/team/lodestar/lodestone/systems/particle/render_types/$LodestoneScreenParticleRenderType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SimpleParticleOptions$ParticleSpritePicker, $SimpleParticleOptions$ParticleSpritePicker$Type} from "packages/team/lodestar/lodestone/systems/particle/$SimpleParticleOptions$ParticleSpritePicker"
import {$AbstractParticleBuilder, $AbstractParticleBuilder$Type} from "packages/team/lodestar/lodestone/systems/particle/builder/$AbstractParticleBuilder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ScreenParticleType, $ScreenParticleType$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ScreenParticleHolder, $ScreenParticleHolder$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$ScreenParticleHolder"
import {$GenericScreenParticle, $GenericScreenParticle$Type} from "packages/team/lodestar/lodestone/systems/particle/screen/$GenericScreenParticle"
import {$ColorParticleData, $ColorParticleData$Type} from "packages/team/lodestar/lodestone/systems/particle/data/color/$ColorParticleData"

export class $ScreenParticleBuilder extends $AbstractParticleBuilder<($ScreenParticleOptions)> {


public "setRandomOffset"(arg0: double): $ScreenParticleBuilder
public "setRandomOffset"(arg0: double, arg1: double): $ScreenParticleBuilder
public "modifyData"(arg0: $Function$Type<($ScreenParticleBuilder$Type), ($GenericParticleData$Type)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $ScreenParticleBuilder
public "modifyData"(arg0: $Optional$Type<($GenericParticleData$Type)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $ScreenParticleBuilder
public "modifyData"(arg0: $Collection$Type<($Supplier$Type<($GenericParticleData$Type)>)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $ScreenParticleBuilder
public "modifyData"(arg0: $Supplier$Type<($GenericParticleData$Type)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $ScreenParticleBuilder
public "modifyDataOptional"(arg0: $Function$Type<($ScreenParticleBuilder$Type), ($Optional$Type<($GenericParticleData$Type)>)>, arg1: $Consumer$Type<($GenericParticleData$Type)>): $ScreenParticleBuilder
public "setRandomMotion"(arg0: double, arg1: double): $ScreenParticleBuilder
public "setRandomMotion"(arg0: double): $ScreenParticleBuilder
public "addActor"(arg0: $Consumer$Type<($GenericScreenParticle$Type)>): $ScreenParticleBuilder
public "repeatOnStack"(arg0: double, arg1: double, arg2: integer): $ScreenParticleBuilder
public "spawnOnStack"(arg0: double, arg1: double): $ScreenParticleBuilder
public "repeat"(arg0: double, arg1: double, arg2: integer): $ScreenParticleBuilder
public static "create"(arg0: $ScreenParticleType$Type<(any)>, arg1: $ScreenParticleHolder$Type): $ScreenParticleBuilder
public "spawn"(arg0: double, arg1: double): $ScreenParticleBuilder
public "setRenderType"(arg0: $LodestoneScreenParticleRenderType$Type): $ScreenParticleBuilder
public "modifyColorData"(arg0: $Consumer$Type<($ColorParticleData$Type)>): $ScreenParticleBuilder
public "setColorData"(arg0: $ColorParticleData$Type): $ScreenParticleBuilder
public "modifyGravity"(arg0: $Function$Type<(float), ($Supplier$Type<(float)>)>): $ScreenParticleBuilder
public "multiplyGravity"(arg0: float): $ScreenParticleBuilder
public "modifyLifeDelay"(arg0: $Function$Type<(integer), ($Supplier$Type<(integer)>)>): $ScreenParticleBuilder
public "setSpritePicker"(arg0: $SimpleParticleOptions$ParticleSpritePicker$Type): $ScreenParticleBuilder
public "multiplyLifeDelay"(arg0: float): $ScreenParticleBuilder
public "addMotion"(arg0: double, arg1: double): $ScreenParticleBuilder
public "setMotion"(arg0: double, arg1: double): $ScreenParticleBuilder
public "act"(arg0: $Consumer$Type<($ScreenParticleBuilder$Type)>): $ScreenParticleBuilder
public "setLifetime"(arg0: $Supplier$Type<(integer)>): $ScreenParticleBuilder
set "randomOffset"(value: double)
set "randomMotion"(value: double)
set "renderType"(value: $LodestoneScreenParticleRenderType$Type)
set "colorData"(value: $ColorParticleData$Type)
set "spritePicker"(value: $SimpleParticleOptions$ParticleSpritePicker$Type)
set "lifetime"(value: $Supplier$Type<(integer)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenParticleBuilder$Type = ($ScreenParticleBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenParticleBuilder_ = $ScreenParticleBuilder$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/$RandomHelper" {
import {$Easing, $Easing$Type} from "packages/team/lodestar/lodestone/systems/easing/$Easing"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $RandomHelper {

constructor()

public static "randomBetween"(arg0: $RandomSource$Type, arg1: float, arg2: float): float
public static "randomBetween"(arg0: $RandomSource$Type, arg1: $Easing$Type, arg2: float, arg3: float): float
public static "randomBetween"(arg0: $RandomSource$Type, arg1: double, arg2: double): double
public static "randomBetween"(arg0: $RandomSource$Type, arg1: $Easing$Type, arg2: double, arg3: double): double
public static "randomBetween"(arg0: $RandomSource$Type, arg1: $Easing$Type, arg2: integer, arg3: integer): integer
public static "randomBetween"(arg0: $RandomSource$Type, arg1: integer, arg2: integer): integer
public static "interpolateWithEasing"(arg0: $Easing$Type, arg1: double, arg2: double, arg3: double): float
public static "weightedEasingLerp"(arg0: $Easing$Type, arg1: float, arg2: float, arg3: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomHelper$Type = ($RandomHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomHelper_ = $RandomHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/systems/rendering/trail/$TrailPoint" {
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $TrailPoint {

constructor(arg0: $Vec3$Type, arg1: integer)
constructor(arg0: $Vec3$Type)

public "tick"(): void
public "getPosition"(): $Vec3
public "getMatrixPosition"(): $Vector4f
public "lerp"(arg0: $TrailPoint$Type, arg1: float): $TrailPoint
public "getTimeActive"(): integer
get "position"(): $Vec3
get "matrixPosition"(): $Vector4f
get "timeActive"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrailPoint$Type = ($TrailPoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrailPoint_ = $TrailPoint$Type;
}}
declare module "packages/team/lodestar/lodestone/helpers/block/$BlockStateHelper" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockStateHelper {

constructor()

public static "getBlockStateWithExistingProperties"(arg0: $BlockState$Type, arg1: $BlockState$Type): $BlockState
public static "setBlockStateWithExistingProperties"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: integer): $BlockState
public static "updateState"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
public static "updateState"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public static "updateAndNotifyState"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public static "updateAndNotifyState"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
public static "newStateWithOldProperty"<T extends $Comparable<(T)>>(arg0: $BlockState$Type, arg1: $BlockState$Type, arg2: $Property$Type<(T)>): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateHelper$Type = ($BlockStateHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateHelper_ = $BlockStateHelper$Type;
}}
declare module "packages/team/lodestar/lodestone/compability/$CuriosCompat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CuriosCompat {
static "LOADED": boolean

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosCompat$Type = ($CuriosCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosCompat_ = $CuriosCompat$Type;
}}
declare module "packages/team/lodestar/lodestone/registry/common/$LodestoneArgumentTypeRegistry" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$WorldEventInstanceArgument, $WorldEventInstanceArgument$Type} from "packages/team/lodestar/lodestone/command/arguments/$WorldEventInstanceArgument"
import {$WorldEventTypeArgument, $WorldEventTypeArgument$Type} from "packages/team/lodestar/lodestone/command/arguments/$WorldEventTypeArgument"
import {$ArgumentTypeInfo, $ArgumentTypeInfo$Type} from "packages/net/minecraft/commands/synchronization/$ArgumentTypeInfo"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $LodestoneArgumentTypeRegistry {
static readonly "COMMAND_ARGUMENT_TYPES": $DeferredRegister<($ArgumentTypeInfo<(any), (any)>)>
static readonly "WORLD_EVENT_TYPE_ARG": $RegistryObject<($ArgumentTypeInfo<($WorldEventTypeArgument), (any)>)>
static readonly "WORLD_EVENT_INSTANCE_ARG": $RegistryObject<($ArgumentTypeInfo<($WorldEventInstanceArgument), (any)>)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
public static "registerArgumentTypes"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LodestoneArgumentTypeRegistry$Type = ($LodestoneArgumentTypeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LodestoneArgumentTypeRegistry_ = $LodestoneArgumentTypeRegistry$Type;
}}
