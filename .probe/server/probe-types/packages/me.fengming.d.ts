declare module "packages/me/fengming/renderjs/$RenderJsPlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ClassFilter, $ClassFilter$Type} from "packages/dev/latvian/mods/kubejs/util/$ClassFilter"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"

export class $RenderJsPlugin extends $KubeJSPlugin {

constructor()

public "registerBindings"(event: $BindingsEvent$Type): void
public "afterInit"(): void
public "registerEvents"(): void
public "registerClasses"(type: $ScriptType$Type, filter: $ClassFilter$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderJsPlugin$Type = ($RenderJsPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderJsPlugin_ = $RenderJsPlugin$Type;
}}
declare module "packages/me/fengming/renderjs/events/level/$RenderLevelEventJS" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RenderEventJS, $RenderEventJS$Type} from "packages/me/fengming/renderjs/events/$RenderEventJS"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"

/**
 * Invoked on rendering the world.
 */
export class $RenderLevelEventJS extends $RenderEventJS {


public "renderInWorldXyz"(id: string, x: float, y: float, z: float): void
public "getPartialTick"(): float
public "getPoseStack"(): $PoseStack
public "getFrustum"(): $Frustum
public "getRenderTick"(): integer
public "getCamera"(): $Camera
public "getLevelRenderer"(): $LevelRenderer
public "getProjectionMatrix"(): $Matrix4f
get "partialTick"(): float
get "poseStack"(): $PoseStack
get "frustum"(): $Frustum
get "renderTick"(): integer
get "camera"(): $Camera
get "levelRenderer"(): $LevelRenderer
get "projectionMatrix"(): $Matrix4f
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEventJS$Type = ($RenderLevelEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEventJS_ = $RenderLevelEventJS$Type;
}}
declare module "packages/me/fengming/renderjs/core/$RenderObject$ObjectType" {
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RenderObject$ObjectType extends $Enum<($RenderObject$ObjectType)> {
static readonly "LINES": $RenderObject$ObjectType
static readonly "LINE_STRIP": $RenderObject$ObjectType
static readonly "TRIANGLES": $RenderObject$ObjectType
static readonly "TRIANGLE_STRIP": $RenderObject$ObjectType
static readonly "TRIANGLE_FAN": $RenderObject$ObjectType
static readonly "QUADS": $RenderObject$ObjectType
static readonly "RECTANGLES": $RenderObject$ObjectType
static readonly "BLOCKS": $RenderObject$ObjectType
static readonly "ITEMS": $RenderObject$ObjectType
static readonly "ICONS": $RenderObject$ObjectType
static readonly "OVERLAYS": $RenderObject$ObjectType
static readonly "MODELS": $RenderObject$ObjectType


public static "values"(): ($RenderObject$ObjectType)[]
public static "valueOf"(name: string): $RenderObject$ObjectType
public "getMode"(): $VertexFormat$Mode
get "mode"(): $VertexFormat$Mode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderObject$ObjectType$Type = (("triangles") | ("models") | ("triangle_fan") | ("quads") | ("rectangles") | ("line_strip") | ("blocks") | ("overlays") | ("lines") | ("icons") | ("items") | ("triangle_strip")) | ($RenderObject$ObjectType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderObject$ObjectType_ = $RenderObject$ObjectType$Type;
}}
declare module "packages/me/fengming/renderjs/core/$RenderObjectManager" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$RenderObject, $RenderObject$Type} from "packages/me/fengming/renderjs/core/$RenderObject"

export class $RenderObjectManager {

constructor()

public static "get"(id: string): $RenderObject
public static "register"(tag: $CompoundTag$Type): void
public static "remove"(id: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderObjectManager$Type = ($RenderObjectManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderObjectManager_ = $RenderObjectManager$Type;
}}
declare module "packages/me/fengming/renderjs/core/render/$CustomRenderType" {
import {$RenderStateShard$OverlayStateShard, $RenderStateShard$OverlayStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OverlayStateShard"
import {$RenderStateShard$TexturingStateShard, $RenderStateShard$TexturingStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TexturingStateShard"
import {$RenderStateShard$LineStateShard, $RenderStateShard$LineStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LineStateShard"
import {$RenderStateShard$TextureStateShard, $RenderStateShard$TextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TextureStateShard"
import {$RenderStateShard$EmptyTextureStateShard, $RenderStateShard$EmptyTextureStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$EmptyTextureStateShard"
import {$RenderStateShard$LightmapStateShard, $RenderStateShard$LightmapStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LightmapStateShard"
import {$RenderStateShard$LayeringStateShard, $RenderStateShard$LayeringStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$LayeringStateShard"
import {$RenderStateShard$WriteMaskStateShard, $RenderStateShard$WriteMaskStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$WriteMaskStateShard"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$RenderType$CompositeRenderType, $RenderType$CompositeRenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType$CompositeRenderType"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$RenderStateShard$OutputStateShard, $RenderStateShard$OutputStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$OutputStateShard"
import {$RenderStateShard$ColorLogicStateShard, $RenderStateShard$ColorLogicStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ColorLogicStateShard"
import {$VertexFormat$Mode, $VertexFormat$Mode$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat$Mode"
import {$RenderStateShard$ShaderStateShard, $RenderStateShard$ShaderStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$ShaderStateShard"
import {$RenderStateShard$DepthTestStateShard, $RenderStateShard$DepthTestStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$DepthTestStateShard"
import {$DrawBuffer, $DrawBuffer$Type} from "packages/com/jozufozu/flywheel/backend/instancing/$DrawBuffer"
import {$RenderStateShard$TransparencyStateShard, $RenderStateShard$TransparencyStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$TransparencyStateShard"
import {$RenderStateShard$CullStateShard, $RenderStateShard$CullStateShard$Type} from "packages/net/minecraft/client/renderer/$RenderStateShard$CullStateShard"

export class $CustomRenderType extends $RenderType {
static readonly "BLOCK_LAYER_TOP": $RenderType
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

constructor(string: string, vertexFormat: $VertexFormat$Type, mode: $VertexFormat$Mode$Type, i: integer, bl: boolean, bl2: boolean, runnable: $Runnable$Type, runnable2: $Runnable$Type)

public static "getDrawBuffer"(arg0: $RenderType$Type): $DrawBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRenderType$Type = ($CustomRenderType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRenderType_ = $CustomRenderType$Type;
}}
declare module "packages/me/fengming/renderjs/events/level/$RenderEntityEventJS$After" {
import {$LivingEntityRenderer, $LivingEntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$LivingEntityRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$RenderEntityEventJS, $RenderEntityEventJS$Type} from "packages/me/fengming/renderjs/events/level/$RenderEntityEventJS"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

/**
 * Invoked after rendering a entity.
 */
export class $RenderEntityEventJS$After extends $RenderEntityEventJS {
 "poseStack": $PoseStack
 "multiBufferSource": $MultiBufferSource
 "entity": $LivingEntity
 "renderer": $LivingEntityRenderer<(any), (any)>
 "partialTick": float
 "packedLight": integer

constructor(entity: $LivingEntity$Type, renderer: $LivingEntityRenderer$Type<(any), (any)>, partialTick: float, poseStack: $PoseStack$Type, multiBufferSource: $MultiBufferSource$Type, packedLight: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderEntityEventJS$After$Type = ($RenderEntityEventJS$After);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderEntityEventJS$After_ = $RenderEntityEventJS$After$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$ItemsDisplay" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$RenderObject, $RenderObject$Type} from "packages/me/fengming/renderjs/core/$RenderObject"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemsDisplay extends $RenderObject {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "loadInner"(object: $CompoundTag$Type): void
public "renderInner"(): void
public "setLight"(light: integer): void
public "setItem"(item: $ItemStack$Type): void
public "setLeftHand"(leftHand: boolean): void
public "setItemDisplayContext"(context: $ItemDisplayContext$Type): void
set "light"(value: integer)
set "item"(value: $ItemStack$Type)
set "leftHand"(value: boolean)
set "itemDisplayContext"(value: $ItemDisplayContext$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemsDisplay$Type = ($ItemsDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemsDisplay_ = $ItemsDisplay$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$Draw" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$RenderObject, $RenderObject$Type} from "packages/me/fengming/renderjs/core/$RenderObject"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"

export class $Draw extends $RenderObject {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "render"(): void
public "loadInner"(object: $CompoundTag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Draw$Type = ($Draw);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Draw_ = $Draw$Type;
}}
declare module "packages/me/fengming/renderjs/$RenderJs" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RenderJs {
static readonly "MOD_ID": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderJs$Type = ($RenderJs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderJs_ = $RenderJs$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$Triangles" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"
import {$Draw, $Draw$Type} from "packages/me/fengming/renderjs/core/objects/$Draw"

export class $Triangles extends $Draw {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "renderInner"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Triangles$Type = ($Triangles);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Triangles_ = $Triangles$Type;
}}
declare module "packages/me/fengming/renderjs/events/level/$RenderLevelEventJS$After" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Frustum, $Frustum$Type} from "packages/net/minecraft/client/renderer/culling/$Frustum"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$RenderLevelEventJS, $RenderLevelEventJS$Type} from "packages/me/fengming/renderjs/events/level/$RenderLevelEventJS"

/**
 * Invoked after rendering the world.
 */
export class $RenderLevelEventJS$After extends $RenderLevelEventJS {

constructor(renderer: $LevelRenderer$Type, poseStack: $PoseStack$Type, matrix4f: $Matrix4f$Type, renderTick: integer, partialTick: float, camera: $Camera$Type, frustum: $Frustum$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderLevelEventJS$After$Type = ($RenderLevelEventJS$After);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderLevelEventJS$After_ = $RenderLevelEventJS$After$Type;
}}
declare module "packages/me/fengming/renderjs/events/level/$RenderEntityEventJS$Before" {
import {$LivingEntityRenderer, $LivingEntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$LivingEntityRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$RenderEntityEventJS, $RenderEntityEventJS$Type} from "packages/me/fengming/renderjs/events/level/$RenderEntityEventJS"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

/**
 * Invoked before rendering a entity.
 */
export class $RenderEntityEventJS$Before extends $RenderEntityEventJS {
 "poseStack": $PoseStack
 "multiBufferSource": $MultiBufferSource
 "entity": $LivingEntity
 "renderer": $LivingEntityRenderer<(any), (any)>
 "partialTick": float
 "packedLight": integer

constructor(entity: $LivingEntity$Type, renderer: $LivingEntityRenderer$Type<(any), (any)>, partialTick: float, poseStack: $PoseStack$Type, multiBufferSource: $MultiBufferSource$Type, packedLight: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderEntityEventJS$Before$Type = ($RenderEntityEventJS$Before);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderEntityEventJS$Before_ = $RenderEntityEventJS$Before$Type;
}}
declare module "packages/me/fengming/renderjs/events/$RenderJsEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $RenderJsEvents {

}

export namespace $RenderJsEvents {
const GROUP_LEVEL: $EventGroup
const GROUP_ENTITY: $EventGroup
const AFTER_RENDER_SOLID_BLOCK: $EventHandler
const BEFORE_RENDER_ENTITY: $EventHandler
const AFTER_RENDER_ENTITY: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderJsEvents$Type = ($RenderJsEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderJsEvents_ = $RenderJsEvents$Type;
}}
declare module "packages/me/fengming/renderjs/events/$RenderEventJS" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$RenderObject, $RenderObject$Type} from "packages/me/fengming/renderjs/core/$RenderObject"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"

/**
 * Base render event.
 */
export class $RenderEventJS extends $EventJS {

constructor()

/**
 * Getting RenderObject from RenderObjectManager, equals to RenderObjectManager#get.
 * If you only want to render a object, use RenderEventJS#render.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 */
public "getObject"(id: string): $RenderObject
/**
 * Apply offset to the given objects and then render it.
 * Equals to RenderObject#offset.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 * @param offset - Given the amount to be offset, in the order xyz.
 */
public "renderWithOffset"(id: string, offset: (float)[]): void
public "getMainCamera"(): $Camera
/**
 * Render a object.
 * 
 * @param object - Create a temporary object and render it.
 */
public "renderByObject"(object: $CompoundTag$Type): void
/**
 * Renders the given object in world.
 * Actually it is converting vertices to world position.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 */
public "renderInWorld"(id: string): void
/**
 * Renders the given object with new vertices.
 * Equals to RenderObject#setVertices.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 * @param vertices - The new vertices.
 */
public "renderWithVertices"(id: string, vertices: (float)[]): void
/**
 * Render a object by id.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 */
public "render"(id: string): void
public "getPoseStack"(): $PoseStack
/**
 * Renders the given object in world.
 * Actually it is converting vertices to world position.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 * @param camera - Convert world position based on this camera.
 */
public "renderInWorldCamera"(id: string, camera: $Camera$Type): void
/**
 * Modify the given vertex and then render the given object.
 * Equals to RenderObject#modifyVertices.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 * @param index - The index of the vertex to be modified.
 * @param value - The value of the vertex to be modified.
 */
public "renderWithModification"(id: string, index: integer, value: float): void
/**
 * Modify the given vertex and then render the given object.
 * Equals to RenderObject#transform.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 * @param index - The index of the vertex to be modified.
 * @param value - The value of the vertex to be modified.
 */
public "renderWithTransform"(id: string, transformation: $CompoundTag$Type): void
/**
 * Renders the given object in world.
 * Actually it is converting vertices to world position.
 * 
 * @param id - The identity of the object to be rendered. Be sure that the object has been registered.
 * @param camera - Convert world position based on this camera.
 * @param x - Based on the offset in the x-direction of the origin.
 * @param y - Based on the offset in the y-direction of the origin.
 * @param z - Based on the offset in the z-direction of the origin.
 */
public "renderInWorldCameraXYZ"(id: string, camera: $Camera$Type, x: float, y: float, z: float): void
get "mainCamera"(): $Camera
get "poseStack"(): $PoseStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderEventJS$Type = ($RenderEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderEventJS_ = $RenderEventJS$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$BlocksDisplay" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$RenderObject, $RenderObject$Type} from "packages/me/fengming/renderjs/core/$RenderObject"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BlocksDisplay extends $RenderObject {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "loadInner"(object: $CompoundTag$Type): void
public "renderInner"(): void
public "setBlockLight"(blockLight: integer): void
public "setRenderType"(renderType: $RenderType$Type): void
public "setWorldLight"(worldLight: integer): void
public "setBlockState"(blockState: $BlockState$Type): void
set "blockLight"(value: integer)
set "renderType"(value: $RenderType$Type)
set "worldLight"(value: integer)
set "blockState"(value: $BlockState$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlocksDisplay$Type = ($BlocksDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlocksDisplay_ = $BlocksDisplay$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$Quads" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"
import {$Draw, $Draw$Type} from "packages/me/fengming/renderjs/core/objects/$Draw"

export class $Quads extends $Draw {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "renderInner"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quads$Type = ($Quads);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quads_ = $Quads$Type;
}}
declare module "packages/me/fengming/renderjs/core/$RenderObject" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Tag, $Tag$Type} from "packages/net/minecraft/nbt/$Tag"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Transformation, $Transformation$Type} from "packages/com/mojang/math/$Transformation"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"

export class $RenderObject {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "load"(object: $CompoundTag$Type): void
public "setPoseStack"(poseStack: $PoseStack$Type): void
public static "loadFromNbt"(object: $CompoundTag$Type): $RenderObject
/**
 * Render this object.
 */
public "render"(): void
/**
 * Directly modify the value of a given vertex.
 */
public "modifyVertices"(index: integer, value: float): void
/**
 * Directly modify vertices to new ones
 */
public "setVertices"(vertices: (float)[]): void
public "addInnerOffsets"(i: integer, x: float, y: float, z: float): void
public "setTransformation"(transformation: $Transformation$Type): void
/**
 * Offset vertices by given values.
 * 
 * @param i - The specified offset index starts from 0. If multiple offsets are to be applied, the index value is increased sequentially. Max to 100.
 * @param x - Offset in the x-direction.
 * @param y - Offset in the y-direction.
 * @param z - Offset in the z-direction.
 */
public "addOffset"(i: integer, x: float, y: float, z: float): void
public "loadInner"(arg0: $CompoundTag$Type): void
/**
 * Get this the type of this object.
 */
public "getType"(): $RenderObject$ObjectType
public "renderInner"(): void
/**
 * Transform vertex matrix.
 */
public "setTransformation"(tag: $Tag$Type): void
set "poseStack"(value: $PoseStack$Type)
set "vertices"(value: (float)[])
set "transformation"(value: $Transformation$Type)
get "type"(): $RenderObject$ObjectType
set "transformation"(value: $Tag$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderObject$Type = ($RenderObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderObject_ = $RenderObject$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$Lines" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"
import {$Draw, $Draw$Type} from "packages/me/fengming/renderjs/core/objects/$Draw"

export class $Lines extends $Draw {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "loadInner"(object: $CompoundTag$Type): void
public "renderInner"(): void
public "setLineWidth"(lineWidth: float): void
set "lineWidth"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Lines$Type = ($Lines);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Lines_ = $Lines$Type;
}}
declare module "packages/me/fengming/renderjs/core/$Utils" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $Utils {

constructor()

public static "parseItem"(s: string, count: integer): $ItemStack
public static "getRenderTypeById"(id: string): $RenderType
public static "parseBlock"(s: string, allowNbt: boolean): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Utils$Type = ($Utils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Utils_ = $Utils$Type;
}}
declare module "packages/me/fengming/renderjs/events/level/$RenderEntityEventJS" {
import {$LivingEntityRenderer, $LivingEntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$LivingEntityRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RenderEventJS, $RenderEventJS$Type} from "packages/me/fengming/renderjs/events/$RenderEventJS"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

/**
 * Invoked on rendering a living entity.
 */
export class $RenderEntityEventJS extends $RenderEventJS {
 "poseStack": $PoseStack
 "multiBufferSource": $MultiBufferSource
 "entity": $LivingEntity
 "renderer": $LivingEntityRenderer<(any), (any)>
 "partialTick": float
 "packedLight": integer


public "getPartialTick"(): float
public "getPoseStack"(): $PoseStack
public "getMultiBufferSource"(): $MultiBufferSource
public "getEntity"(): $LivingEntity
public "getPackedLight"(): integer
public "getRenderer"(): $LivingEntityRenderer<(any), (any)>
get "partialTick"(): float
get "poseStack"(): $PoseStack
get "multiBufferSource"(): $MultiBufferSource
get "entity"(): $LivingEntity
get "packedLight"(): integer
get "renderer"(): $LivingEntityRenderer<(any), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderEntityEventJS$Type = ($RenderEntityEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderEntityEventJS_ = $RenderEntityEventJS$Type;
}}
declare module "packages/me/fengming/renderjs/core/objects/$IconsDisplay" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$RenderObject, $RenderObject$Type} from "packages/me/fengming/renderjs/core/$RenderObject"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$RenderObject$ObjectType, $RenderObject$ObjectType$Type} from "packages/me/fengming/renderjs/core/$RenderObject$ObjectType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $IconsDisplay extends $RenderObject {
static "mc": $Minecraft
static "camera": $Camera

constructor(vertices: (float)[], type: $RenderObject$ObjectType$Type)

public "render"(): void
public "loadInner"(object: $CompoundTag$Type): void
public "renderInner"(): void
public "setLocation"(location: $ResourceLocation$Type): void
public "setWidth"(width: float): void
public "setHeight"(height: float): void
public "setU"(u: float): void
public "setV"(v: float): void
public "setTextureWidth"(textureWidth: float): void
public "setTextureHeight"(textureHeight: float): void
set "location"(value: $ResourceLocation$Type)
set "width"(value: float)
set "height"(value: float)
set "u"(value: float)
set "v"(value: float)
set "textureWidth"(value: float)
set "textureHeight"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IconsDisplay$Type = ($IconsDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IconsDisplay_ = $IconsDisplay$Type;
}}
