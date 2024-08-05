declare module "packages/software/bernie/geckolib/resource/$GeoGlowingTextureMeta$Pixel" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $GeoGlowingTextureMeta$Pixel extends $Record {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "x"(): integer
public "y"(): integer
public "alpha"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoGlowingTextureMeta$Pixel$Type = ($GeoGlowingTextureMeta$Pixel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoGlowingTextureMeta$Pixel_ = $GeoGlowingTextureMeta$Pixel$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationController$AnimationStateHandler" {
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PlayState, $PlayState$Type} from "packages/software/bernie/geckolib/core/object/$PlayState"

export interface $AnimationController$AnimationStateHandler<A extends $GeoAnimatable> {

 "handle"(arg0: $AnimationState$Type<(A)>): $PlayState

(arg0: $AnimationState$Type<(A)>): $PlayState
}

export namespace $AnimationController$AnimationStateHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationController$AnimationStateHandler$Type<A> = ($AnimationController$AnimationStateHandler<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationController$AnimationStateHandler_<A> = $AnimationController$AnimationStateHandler$Type<(A)>;
}}
declare module "packages/software/bernie/geckolib/$GeckoLib" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $GeckoLib {
static readonly "LOGGER": $Logger
static readonly "MOD_ID": string
static "hasInitialized": boolean

constructor()

public static "initialize"(): void
public static "shadowInit"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLib$Type = ($GeckoLib);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLib_ = $GeckoLib$Type;
}}
declare module "packages/software/bernie/example/item/$GeckoArmorItem" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Equipable, $Equipable$Type} from "packages/net/minecraft/world/item/$Equipable"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ArmorItem$Type, $ArmorItem$Type$Type} from "packages/net/minecraft/world/item/$ArmorItem$Type"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$GeoItem, $GeoItem$Type} from "packages/software/bernie/geckolib/animatable/$GeoItem"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$ArmorItem, $ArmorItem$Type} from "packages/net/minecraft/world/item/$ArmorItem"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$DispenseItemBehavior, $DispenseItemBehavior$Type} from "packages/net/minecraft/core/dispenser/$DispenseItemBehavior"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GeckoArmorItem extends $ArmorItem implements $GeoItem {
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

public "initializeClient"(arg0: $Consumer$Type<($IClientItemExtensions$Type)>): void
public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public static "getId"(arg0: $ItemStack$Type): long
public static "registerSyncedAnimatable"(arg0: $GeoAnimatable$Type): void
public static "getOrAssignId"(arg0: $ItemStack$Type, arg1: $ServerLevel$Type): long
public "isPerspectiveAware"(): boolean
public "getTick"(arg0: any): double
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
public "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
public "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
public "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
public "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
public static "get"(arg0: $ItemStack$Type): $Equipable
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "perspectiveAware"(): boolean
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoArmorItem$Type = ($GeckoArmorItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoArmorItem_ = $GeckoArmorItem$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$ReplacedCreeperRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$Creeper, $Creeper$Type} from "packages/net/minecraft/world/entity/monster/$Creeper"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoReplacedEntityRenderer, $GeoReplacedEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoReplacedEntityRenderer"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ReplacedCreeperEntity, $ReplacedCreeperEntity$Type} from "packages/software/bernie/example/entity/$ReplacedCreeperEntity"

export class $ReplacedCreeperRenderer extends $GeoReplacedEntityRenderer<($Creeper), ($ReplacedCreeperEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "preRender"(arg0: $PoseStack$Type, arg1: $ReplacedCreeperEntity$Type, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getPackedOverlay"(arg0: $ReplacedCreeperEntity$Type, arg1: float, arg2: float): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplacedCreeperRenderer$Type = ($ReplacedCreeperRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplacedCreeperRenderer_ = $ReplacedCreeperRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$PolyMesh" {
import {$PolysUnion, $PolysUnion$Type} from "packages/software/bernie/geckolib/loading/json/raw/$PolysUnion"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $PolyMesh extends $Record {

constructor(normalizedUVs: boolean, normals: (double)[], polysUnion: $PolysUnion$Type, positions: (double)[], uvs: (double)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "positions"(): (double)[]
public static "deserializer"(): $JsonDeserializer<($PolyMesh)>
public "normals"(): (double)[]
public "polysUnion"(): $PolysUnion
public "normalizedUVs"(): boolean
public "uvs"(): (double)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolyMesh$Type = ($PolyMesh);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolyMesh_ = $PolyMesh$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/functions/$SinDegrees" {
import {$Function, $Function$Type} from "packages/com/eliotlash/mclib/math/functions/$Function"
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $SinDegrees extends $Function {

constructor(arg0: ($IValue$Type)[], arg1: string)

public "get"(): double
public "getRequiredArguments"(): integer
get "requiredArguments"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinDegrees$Type = ($SinDegrees);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinDegrees_ = $SinDegrees$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$UVUnion" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$UVFaces, $UVFaces$Type} from "packages/software/bernie/geckolib/loading/json/raw/$UVFaces"

export class $UVUnion extends $Record {

constructor(boxUVCoords: (double)[], faceUV: $UVFaces$Type, isBoxUV: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "deserializer"(): $JsonDeserializer<($UVUnion)>
public "boxUVCoords"(): (double)[]
public "isBoxUV"(): boolean
public "faceUV"(): $UVFaces
get "boxUV"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UVUnion$Type = ($UVUnion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UVUnion_ = $UVUnion$Type;
}}
declare module "packages/software/bernie/example/registry/$SoundRegistry" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $SoundRegistry {
static readonly "SOUNDS": $DeferredRegister<($SoundEvent)>
static "JACK_MUSIC": $RegistryObject<($SoundEvent)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoundRegistry$Type = ($SoundRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoundRegistry_ = $SoundRegistry$Type;
}}
declare module "packages/software/bernie/example/client/model/block/$GeckoHabitatModel" {
import {$GeckoHabitatBlockEntity, $GeckoHabitatBlockEntity$Type} from "packages/software/bernie/example/block/entity/$GeckoHabitatBlockEntity"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DefaultedBlockGeoModel, $DefaultedBlockGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedBlockGeoModel"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $GeckoHabitatModel extends $DefaultedBlockGeoModel<($GeckoHabitatBlockEntity)> {

constructor()

public "getRenderType"(arg0: $GeckoHabitatBlockEntity$Type, arg1: $ResourceLocation$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoHabitatModel$Type = ($GeckoHabitatModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoHabitatModel_ = $GeckoHabitatModel$Type;
}}
declare module "packages/software/bernie/example/client/model/entity/$GremlinModel" {
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$DynamicExampleEntity, $DynamicExampleEntity$Type} from "packages/software/bernie/example/entity/$DynamicExampleEntity"

export class $GremlinModel extends $DefaultedEntityGeoModel<($DynamicExampleEntity)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GremlinModel$Type = ($GremlinModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GremlinModel_ = $GremlinModel$Type;
}}
declare module "packages/software/bernie/geckolib/loading/object/$GeometryTree" {
import {$BoneStructure, $BoneStructure$Type} from "packages/software/bernie/geckolib/loading/object/$BoneStructure"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ModelProperties, $ModelProperties$Type} from "packages/software/bernie/geckolib/loading/json/raw/$ModelProperties"
import {$Model, $Model$Type} from "packages/software/bernie/geckolib/loading/json/raw/$Model"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GeometryTree extends $Record {

constructor(topLevelBones: $Map$Type<(string), ($BoneStructure$Type)>, properties: $ModelProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "properties"(): $ModelProperties
public static "fromModel"(arg0: $Model$Type): $GeometryTree
public "topLevelBones"(): $Map<(string), ($BoneStructure)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeometryTree$Type = ($GeometryTree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeometryTree_ = $GeometryTree$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/$KeyFrameEvent" {
import {$KeyFrameData, $KeyFrameData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$KeyFrameData"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"

export class $KeyFrameEvent<T extends $GeoAnimatable, E extends $KeyFrameData> {

constructor(arg0: T, arg1: double, arg2: $AnimationController$Type<(T)>, arg3: E)

public "getAnimationTick"(): double
public "getKeyframeData"(): E
public "getController"(): $AnimationController<(T)>
public "getAnimatable"(): T
get "animationTick"(): double
get "keyframeData"(): E
get "controller"(): $AnimationController<(T)>
get "animatable"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyFrameEvent$Type<T, E> = ($KeyFrameEvent<(T), (E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyFrameEvent_<T, E> = $KeyFrameEvent$Type<(T), (E)>;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$MutantZombieRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$DynamicGeoEntityRenderer, $DynamicGeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$DynamicGeoEntityRenderer"
import {$DynamicExampleEntity, $DynamicExampleEntity$Type} from "packages/software/bernie/example/entity/$DynamicExampleEntity"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $MutantZombieRenderer extends $DynamicGeoEntityRenderer<($DynamicExampleEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "preRender"(arg0: $PoseStack$Type, arg1: $DynamicExampleEntity$Type, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutantZombieRenderer$Type = ($MutantZombieRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutantZombieRenderer_ = $MutantZombieRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/loading/object/$BakedModelFactory$VertexSet" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GeoVertex, $GeoVertex$Type} from "packages/software/bernie/geckolib/cache/object/$GeoVertex"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $BakedModelFactory$VertexSet extends $Record {

constructor(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: double)
constructor(bottomLeftBack: $GeoVertex$Type, bottomRightBack: $GeoVertex$Type, topLeftBack: $GeoVertex$Type, topRightBack: $GeoVertex$Type, topLeftFront: $GeoVertex$Type, topRightFront: $GeoVertex$Type, bottomLeftFront: $GeoVertex$Type, bottomRightFront: $GeoVertex$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "verticesForQuad"(arg0: $Direction$Type, arg1: boolean, arg2: boolean): ($GeoVertex)[]
public "quadNorth"(): ($GeoVertex)[]
public "bottomRightFront"(): $GeoVertex
public "topLeftBack"(): $GeoVertex
public "topLeftFront"(): $GeoVertex
public "quadWest"(): ($GeoVertex)[]
public "bottomRightBack"(): $GeoVertex
public "quadEast"(): ($GeoVertex)[]
public "bottomLeftFront"(): $GeoVertex
public "quadUp"(): ($GeoVertex)[]
public "topRightBack"(): $GeoVertex
public "quadDown"(): ($GeoVertex)[]
public "quadSouth"(): ($GeoVertex)[]
public "bottomLeftBack"(): $GeoVertex
public "topRightFront"(): $GeoVertex
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedModelFactory$VertexSet$Type = ($BakedModelFactory$VertexSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedModelFactory$VertexSet_ = $BakedModelFactory$VertexSet$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$Animation$LoopType" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Animation, $Animation$Type} from "packages/software/bernie/geckolib/core/animation/$Animation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $Animation$LoopType {

 "shouldPlayAgain"(arg0: $GeoAnimatable$Type, arg1: $AnimationController$Type<(any)>, arg2: $Animation$Type): boolean

(arg0: string, arg1: $Animation$LoopType$Type): $Animation$LoopType
}

export namespace $Animation$LoopType {
const LOOP_TYPES: $Map<(string), ($Animation$LoopType)>
const DEFAULT: $Animation$LoopType
const PLAY_ONCE: $Animation$LoopType
const HOLD_ON_LAST_FRAME: $Animation$LoopType
const LOOP: $Animation$LoopType
function register(arg0: string, arg1: $Animation$LoopType$Type): $Animation$LoopType
function fromString(arg0: string): $Animation$LoopType
function fromJson(arg0: $JsonElement$Type): $Animation$LoopType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Animation$LoopType$Type = ($Animation$LoopType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Animation$LoopType_ = $Animation$LoopType$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoModel" {
import {$CoreBakedGeoModel, $CoreBakedGeoModel$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreBakedGeoModel"
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Animation, $Animation$Type} from "packages/software/bernie/geckolib/core/animation/$Animation"
import {$AnimationProcessor, $AnimationProcessor$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationProcessor"

export interface $CoreGeoModel<E extends $GeoAnimatable> {

 "getAnimation"(arg0: E, arg1: string): $Animation
 "handleAnimations"(arg0: E, arg1: long, arg2: $AnimationState$Type<(E)>): void
 "getBone"(arg0: string): $Optional<(any)>
 "getBakedGeoModel"(arg0: string): $CoreBakedGeoModel
 "applyMolangQueries"(arg0: E, arg1: double): void
 "getAnimationProcessor"(): $AnimationProcessor<(E)>
 "setCustomAnimations"(arg0: E, arg1: long, arg2: $AnimationState$Type<(E)>): void
}

export namespace $CoreGeoModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreGeoModel$Type<E> = ($CoreGeoModel<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreGeoModel_<E> = $CoreGeoModel$Type<(E)>;
}}
declare module "packages/software/bernie/geckolib/model/$DefaultedItemGeoModel" {
import {$DefaultedGeoModel, $DefaultedGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedGeoModel"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DefaultedItemGeoModel<T extends $GeoAnimatable> extends $DefaultedGeoModel<(T)> {

constructor(arg0: $ResourceLocation$Type)

public "withAltTexture"(arg0: $ResourceLocation$Type): $DefaultedItemGeoModel<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedItemGeoModel$Type<T> = ($DefaultedItemGeoModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedItemGeoModel_<T> = $DefaultedItemGeoModel$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/model/$GeoModel" {
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CoreGeoModel, $CoreGeoModel$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoModel"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$AnimationProcessor, $AnimationProcessor$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationProcessor"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Animation, $Animation$Type} from "packages/software/bernie/geckolib/core/animation/$Animation"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"

export class $GeoModel<T extends $GeoAnimatable> implements $CoreGeoModel<(T)> {

constructor()

public "getAnimation"(arg0: T, arg1: string): $Animation
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type): $RenderType
public "addAdditionalStateData"(arg0: T, arg1: long, arg2: $BiConsumer$Type<($DataTicket$Type<(T)>), (T)>): void
public "getTextureResource"(arg0: T): $ResourceLocation
public "handleAnimations"(arg0: T, arg1: long, arg2: $AnimationState$Type<(T)>): void
public "getBakedModel"(arg0: $ResourceLocation$Type): $BakedGeoModel
public "getModelResource"(arg0: T): $ResourceLocation
public "getBone"(arg0: string): $Optional<($GeoBone)>
public "crashIfBoneMissing"(): boolean
public "applyMolangQueries"(arg0: T, arg1: double): void
public "getAnimationResource"(arg0: T): $ResourceLocation
public "getAnimationProcessor"(): $AnimationProcessor<(T)>
public "setCustomAnimations"(arg0: T, arg1: long, arg2: $AnimationState$Type<(T)>): void
get "animationProcessor"(): $AnimationProcessor<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoModel$Type<T> = ($GeoModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoModel_<T> = $GeoModel$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/constant/$DefaultAnimations" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$RawAnimation, $RawAnimation$Type} from "packages/software/bernie/geckolib/core/animation/$RawAnimation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DefaultAnimations {
static readonly "ITEM_ON_USE": $RawAnimation
static readonly "IDLE": $RawAnimation
static readonly "LIVING": $RawAnimation
static readonly "SPAWN": $RawAnimation
static readonly "DIE": $RawAnimation
static readonly "INTERACT": $RawAnimation
static readonly "DEPLOY": $RawAnimation
static readonly "REST": $RawAnimation
static readonly "SIT": $RawAnimation
static readonly "WALK": $RawAnimation
static readonly "SWIM": $RawAnimation
static readonly "RUN": $RawAnimation
static readonly "DRIVE": $RawAnimation
static readonly "FLY": $RawAnimation
static readonly "CRAWL": $RawAnimation
static readonly "JUMP": $RawAnimation
static readonly "SNEAK": $RawAnimation
static readonly "ATTACK_CAST": $RawAnimation
static readonly "ATTACK_SWING": $RawAnimation
static readonly "ATTACK_THROW": $RawAnimation
static readonly "ATTACK_BITE": $RawAnimation
static readonly "ATTACK_SLAM": $RawAnimation
static readonly "ATTACK_STOMP": $RawAnimation
static readonly "ATTACK_STRIKE": $RawAnimation
static readonly "ATTACK_FLYING_ATTACK": $RawAnimation
static readonly "ATTACK_SHOOT": $RawAnimation
static readonly "ATTACK_BLOCK": $RawAnimation
static readonly "ATTACK_CHARGE": $RawAnimation
static readonly "ATTACK_CHARGE_END": $RawAnimation
static readonly "ATTACK_POWERUP": $RawAnimation

constructor()

public static "genericLivingController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericWalkIdleController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericAttackAnimation"<T extends ($LivingEntity) & ($GeoAnimatable)>(arg0: T, arg1: $RawAnimation$Type): $AnimationController<(T)>
public static "getSpawnController"<T extends $GeoAnimatable>(arg0: T, arg1: $Function$Type<($AnimationState$Type<(T)>), (any)>, arg2: integer): $AnimationController<(T)>
public static "genericIdleController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "basicPredicateController"<T extends $GeoAnimatable>(arg0: T, arg1: $RawAnimation$Type, arg2: $RawAnimation$Type, arg3: $BiFunction$Type<(T), ($AnimationState$Type<(T)>), (boolean)>): $AnimationController<(T)>
public static "genericSwimController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericFlyIdleController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericWalkController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericFlyController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericWalkRunIdleController"<T extends ($Entity) & ($GeoAnimatable)>(arg0: T): $AnimationController<(T)>
public static "genericSwimIdleController"<T extends $GeoAnimatable>(arg0: T): $AnimationController<(T)>
public static "genericDeathController"<T extends ($LivingEntity) & ($GeoAnimatable)>(arg0: T): $AnimationController<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultAnimations$Type = ($DefaultAnimations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultAnimations_ = $DefaultAnimations$Type;
}}
declare module "packages/software/bernie/geckolib/cache/object/$GeoVertex" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"

export class $GeoVertex extends $Record {

constructor(arg0: double, arg1: double, arg2: double)
constructor(position: $Vector3f$Type, texU: float, texV: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "position"(): $Vector3f
public "texV"(): float
public "texU"(): float
public "withUVs"(arg0: float, arg1: float): $GeoVertex
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoVertex$Type = ($GeoVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoVertex_ = $GeoVertex$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"

export interface $GeoRenderer<T extends $GeoAnimatable> {

 "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
 "getMotionAnimThreshold"(arg0: T): float
 "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
 "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
 "updateAnimatedTextureFrame"(arg0: T): void
 "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
 "fireCompileRenderLayersEvent"(): void
 "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
 "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
 "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
 "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
 "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
 "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
 "getInstanceId"(arg0: T): long
 "getTextureLocation"(arg0: T): $ResourceLocation
 "getAnimatable"(): T
 "getGeoModel"(): $GeoModel<(T)>
 "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
 "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
 "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
 "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
 "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
 "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
 "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
 "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
/**
 * 
 * @deprecated
 */
 "getPackedOverlay"(arg0: T, arg1: float): integer
 "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
 "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
 "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
}

export namespace $GeoRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoRenderer$Type<T> = ($GeoRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoRenderer_<T> = $GeoRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$Cube" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$UVUnion, $UVUnion$Type} from "packages/software/bernie/geckolib/loading/json/raw/$UVUnion"

export class $Cube extends $Record {

constructor(inflate: double, mirror: boolean, origin: (double)[], pivot: (double)[], rotation: (double)[], size: (double)[], uv: $UVUnion$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "inflate"(): double
public "size"(): (double)[]
public "origin"(): (double)[]
public "pivot"(): (double)[]
public static "deserializer"(): $JsonDeserializer<($Cube)>
public "uv"(): $UVUnion
public "rotation"(): (double)[]
public "mirror"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cube$Type = ($Cube);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cube_ = $Cube$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$KeyframeStack" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Keyframe, $Keyframe$Type} from "packages/software/bernie/geckolib/core/keyframe/$Keyframe"

export class $KeyframeStack<T extends $Keyframe<(any)>> extends $Record {

constructor()
constructor(xKeyframes: $List$Type<(T)>, yKeyframes: $List$Type<(T)>, zKeyframes: $List$Type<(T)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "from"<F extends $Keyframe<(any)>>(arg0: $KeyframeStack$Type<(F)>): $KeyframeStack<(F)>
public "xKeyframes"(): $List<(T)>
public "getLastKeyframeTime"(): double
public "yKeyframes"(): $List<(T)>
public "zKeyframes"(): $List<(T)>
get "lastKeyframeTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeStack$Type<T> = ($KeyframeStack<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeStack_<T> = $KeyframeStack$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/cache/texture/$AutoGlowingTexture" {
import {$GeoAbstractTexture, $GeoAbstractTexture$Type} from "packages/software/bernie/geckolib/cache/texture/$GeoAbstractTexture"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $AutoGlowingTexture extends $GeoAbstractTexture {
static readonly "NOT_ASSIGNED": integer

constructor(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type)

public static "getRenderType"(arg0: $ResourceLocation$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoGlowingTexture$Type = ($AutoGlowingTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoGlowingTexture_ = $AutoGlowingTexture$Type;
}}
declare module "packages/software/bernie/example/client/renderer/block/$GeckoHabitatBlockRenderer" {
import {$GeckoHabitatBlockEntity, $GeckoHabitatBlockEntity$Type} from "packages/software/bernie/example/block/entity/$GeckoHabitatBlockEntity"
import {$GeoBlockRenderer, $GeoBlockRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoBlockRenderer"

export class $GeckoHabitatBlockRenderer extends $GeoBlockRenderer<($GeckoHabitatBlockEntity)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoHabitatBlockRenderer$Type = ($GeckoHabitatBlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoHabitatBlockRenderer_ = $GeckoHabitatBlockRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/expressions/$MolangValue" {
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $MolangValue implements $IValue {

constructor(arg0: $IValue$Type)
constructor(arg0: $IValue$Type, arg1: boolean)

public "get"(): double
public "toString"(): string
public "getValueHolder"(): $IValue
public "isReturnValue"(): boolean
public "isConstant"(): boolean
get "valueHolder"(): $IValue
get "returnValue"(): boolean
get "constant"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MolangValue$Type = ($MolangValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MolangValue_ = $MolangValue$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$MinecraftGeometry" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ModelProperties, $ModelProperties$Type} from "packages/software/bernie/geckolib/loading/json/raw/$ModelProperties"
import {$Bone, $Bone$Type} from "packages/software/bernie/geckolib/loading/json/raw/$Bone"

export class $MinecraftGeometry extends $Record {

constructor(bones: ($Bone$Type)[], cape: string, modelProperties: $ModelProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "deserializer"(): $JsonDeserializer<($MinecraftGeometry)>
public "cape"(): string
public "modelProperties"(): $ModelProperties
public "bones"(): ($Bone)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinecraftGeometry$Type = ($MinecraftGeometry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinecraftGeometry_ = $MinecraftGeometry$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$BlockAndItemGeoLayer" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BlockAndItemGeoLayer<T extends $GeoAnimatable> extends $GeoRenderLayer<(T)> {

constructor(arg0: $GeoRenderer$Type<(T)>)
constructor(arg0: $GeoRenderer$Type<(T)>, arg1: $BiFunction$Type<($GeoBone$Type), (T), ($ItemStack$Type)>, arg2: $BiFunction$Type<($GeoBone$Type), (T), ($BlockState$Type)>)

public "renderForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockAndItemGeoLayer$Type<T> = ($BlockAndItemGeoLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockAndItemGeoLayer_<T> = $BlockAndItemGeoLayer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationController$ParticleKeyframeHandler" {
import {$ParticleKeyframeEvent, $ParticleKeyframeEvent$Type} from "packages/software/bernie/geckolib/core/keyframe/event/$ParticleKeyframeEvent"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"

export interface $AnimationController$ParticleKeyframeHandler<A extends $GeoAnimatable> {

 "handle"(arg0: $ParticleKeyframeEvent$Type<(A)>): void

(arg0: $ParticleKeyframeEvent$Type<(A)>): void
}

export namespace $AnimationController$ParticleKeyframeHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationController$ParticleKeyframeHandler$Type<A> = ($AnimationController$ParticleKeyframeHandler<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationController$ParticleKeyframeHandler_<A> = $AnimationController$ParticleKeyframeHandler$Type<(A)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$ModelProperties" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $ModelProperties extends $Record {

constructor(animationArmsDown: boolean, animationArmsOutFront: boolean, animationDontShowArmor: boolean, animationInvertedCrouch: boolean, animationNoHeadBob: boolean, animationSingleArmAnimation: boolean, animationSingleLegAnimation: boolean, animationStationaryLegs: boolean, animationStatueOfLibertyArms: boolean, animationUpsideDown: boolean, identifier: string, preserveModelPose: boolean, textureHeight: double, textureWidth: double, visibleBoundsHeight: double, visibleBoundsOffset: (double)[], visibleBoundsWidth: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "deserializer"(): $JsonDeserializer<($ModelProperties)>
public "textureWidth"(): double
public "textureHeight"(): double
public "identifier"(): string
public "preserveModelPose"(): boolean
public "animationArmsDown"(): boolean
public "visibleBoundsWidth"(): double
public "animationNoHeadBob"(): boolean
public "animationInvertedCrouch"(): boolean
public "animationStatueOfLibertyArms"(): boolean
public "animationUpsideDown"(): boolean
public "visibleBoundsHeight"(): double
public "animationDontShowArmor"(): boolean
public "animationStationaryLegs"(): boolean
public "visibleBoundsOffset"(): (double)[]
public "animationSingleArmAnimation"(): boolean
public "animationArmsOutFront"(): boolean
public "animationSingleLegAnimation"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelProperties$Type = ($ModelProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelProperties_ = $ModelProperties$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationProcessor" {
import {$CoreBakedGeoModel, $CoreBakedGeoModel$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreBakedGeoModel"
import {$AnimatableManager, $AnimatableManager$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager"
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"
import {$CoreGeoModel, $CoreGeoModel$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoModel"
import {$RawAnimation, $RawAnimation$Type} from "packages/software/bernie/geckolib/core/animation/$RawAnimation"
import {$Queue, $Queue$Type} from "packages/java/util/$Queue"
import {$AnimationProcessor$QueuedAnimation, $AnimationProcessor$QueuedAnimation$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationProcessor$QueuedAnimation"

export class $AnimationProcessor<T extends $GeoAnimatable> {
 "reloadAnimations": boolean

constructor(arg0: $CoreGeoModel$Type<(T)>)

public "tickAnimation"(arg0: T, arg1: $CoreGeoModel$Type<(T)>, arg2: $AnimatableManager$Type<(T)>, arg3: double, arg4: $AnimationState$Type<(T)>, arg5: boolean): void
public "registerGeoBone"(arg0: $CoreGeoBone$Type): void
public "buildAnimationQueue"(arg0: T, arg1: $RawAnimation$Type): $Queue<($AnimationProcessor$QueuedAnimation)>
public "setActiveModel"(arg0: $CoreBakedGeoModel$Type): void
public "getBone"(arg0: string): $CoreGeoBone
public "getRegisteredBones"(): $Collection<($CoreGeoBone)>
public "preAnimationSetup"(arg0: T, arg1: double): void
set "activeModel"(value: $CoreBakedGeoModel$Type)
get "registeredBones"(): $Collection<($CoreGeoBone)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationProcessor$Type<T> = ($AnimationProcessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationProcessor_<T> = $AnimationProcessor$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/typeadapter/$KeyFramesAdapter" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Animation$Keyframes, $Animation$Keyframes$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$Keyframes"

export class $KeyFramesAdapter implements $JsonDeserializer<($Animation$Keyframes)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyFramesAdapter$Type = ($KeyFramesAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyFramesAdapter_ = $KeyFramesAdapter$Type;
}}
declare module "packages/software/bernie/example/item/$WolfArmorItem" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Equipable, $Equipable$Type} from "packages/net/minecraft/world/item/$Equipable"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ArmorItem$Type, $ArmorItem$Type$Type} from "packages/net/minecraft/world/item/$ArmorItem$Type"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$GeoItem, $GeoItem$Type} from "packages/software/bernie/geckolib/animatable/$GeoItem"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$ArmorMaterial, $ArmorMaterial$Type} from "packages/net/minecraft/world/item/$ArmorMaterial"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$ArmorItem, $ArmorItem$Type} from "packages/net/minecraft/world/item/$ArmorItem"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$DispenseItemBehavior, $DispenseItemBehavior$Type} from "packages/net/minecraft/core/dispenser/$DispenseItemBehavior"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $WolfArmorItem extends $ArmorItem implements $GeoItem {
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

public "initializeClient"(arg0: $Consumer$Type<($IClientItemExtensions$Type)>): void
public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public static "getId"(arg0: $ItemStack$Type): long
public static "registerSyncedAnimatable"(arg0: $GeoAnimatable$Type): void
public static "getOrAssignId"(arg0: $ItemStack$Type, arg1: $ServerLevel$Type): long
public "isPerspectiveAware"(): boolean
public "getTick"(arg0: any): double
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
public "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
public "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
public "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
public "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
public static "get"(arg0: $ItemStack$Type): $Equipable
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "perspectiveAware"(): boolean
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WolfArmorItem$Type = ($WolfArmorItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WolfArmorItem_ = $WolfArmorItem$Type;
}}
declare module "packages/software/bernie/geckolib/model/$DefaultedBlockGeoModel" {
import {$DefaultedGeoModel, $DefaultedGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedGeoModel"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DefaultedBlockGeoModel<T extends $GeoAnimatable> extends $DefaultedGeoModel<(T)> {

constructor(arg0: $ResourceLocation$Type)

public "withAltTexture"(arg0: $ResourceLocation$Type): $DefaultedBlockGeoModel<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedBlockGeoModel$Type<T> = ($DefaultedBlockGeoModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedBlockGeoModel_<T> = $DefaultedBlockGeoModel$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/cache/object/$BakedGeoModel" {
import {$CoreBakedGeoModel, $CoreBakedGeoModel$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreBakedGeoModel"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ModelProperties, $ModelProperties$Type} from "packages/software/bernie/geckolib/loading/json/raw/$ModelProperties"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"

export class $BakedGeoModel extends $Record implements $CoreBakedGeoModel {

constructor(topLevelBones: $List$Type<($GeoBone$Type)>, properties: $ModelProperties$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "properties"(): $ModelProperties
public "getBones"(): $List<(any)>
public "topLevelBones"(): $List<($GeoBone)>
public "getBone"(arg0: string): $Optional<($GeoBone)>
public "searchForChildBone"(arg0: $CoreGeoBone$Type, arg1: string): $CoreGeoBone
get "bones"(): $List<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedGeoModel$Type = ($BakedGeoModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedGeoModel_ = $BakedGeoModel$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$BikeRenderer" {
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$BikeEntity, $BikeEntity$Type} from "packages/software/bernie/example/entity/$BikeEntity"

export class $BikeRenderer extends $GeoEntityRenderer<($BikeEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BikeRenderer$Type = ($BikeRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BikeRenderer_ = $BikeRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$TextureMesh" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $TextureMesh extends $Record {

constructor(localPivot: (double)[], position: (double)[], rotation: (double)[], scale: (double)[], texture: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "scale"(): (double)[]
public "position"(): (double)[]
public static "deserializer"(): $JsonDeserializer<($TextureMesh)>
public "rotation"(): (double)[]
public "texture"(): string
public "localPivot"(): (double)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureMesh$Type = ($TextureMesh);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureMesh_ = $TextureMesh$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/instance/$InstancedAnimatableInstanceCache" {
import {$AnimatableManager, $AnimatableManager$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"

export class $InstancedAnimatableInstanceCache extends $AnimatableInstanceCache {

constructor(arg0: $GeoAnimatable$Type)

public "getManagerForId"(arg0: long): $AnimatableManager<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstancedAnimatableInstanceCache$Type = ($InstancedAnimatableInstanceCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstancedAnimatableInstanceCache_ = $InstancedAnimatableInstanceCache$Type;
}}
declare module "packages/software/bernie/geckolib/animatable/$SingletonGeoAnimatable" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $SingletonGeoAnimatable extends $GeoAnimatable {

 "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
 "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
 "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
 "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
 "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
 "animatableCacheOverride"(): $AnimatableInstanceCache
 "getBoneResetTime"(): double
 "getAnimatableInstanceCache"(): $AnimatableInstanceCache
 "getTick"(arg0: any): double
 "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
 "shouldPlayAnimsWhileGamePaused"(): boolean
}

export namespace $SingletonGeoAnimatable {
function registerSyncedAnimatable(arg0: $GeoAnimatable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingletonGeoAnimatable$Type = ($SingletonGeoAnimatable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingletonGeoAnimatable_ = $SingletonGeoAnimatable$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BoneSnapshot, $BoneSnapshot$Type} from "packages/software/bernie/geckolib/core/state/$BoneSnapshot"

export interface $CoreGeoBone {

 "getName"(): string
 "isHidden"(): boolean
 "getParent"(): $CoreGeoBone
 "setPosX"(arg0: float): void
 "setPosY"(arg0: float): void
 "updatePosition"(arg0: float, arg1: float, arg2: float): void
 "getPosY"(): float
 "getPosX"(): float
 "getPivotX"(): float
 "getPivotY"(): float
 "setPivotY"(arg0: float): void
 "setScaleX"(arg0: float): void
 "getScaleX"(): float
 "setScaleY"(arg0: float): void
 "setPivotX"(arg0: float): void
 "getScaleY"(): float
 "saveInitialSnapshot"(): void
 "markPositionAsChanged"(): void
 "markRotationAsChanged"(): void
 "updateRotation"(arg0: float, arg1: float, arg2: float): void
 "setPosZ"(arg0: float): void
 "setChildrenHidden"(arg0: boolean): void
 "markScaleAsChanged"(): void
 "getRotX"(): float
 "getScaleZ"(): float
 "setRotY"(arg0: float): void
 "updatePivot"(arg0: float, arg1: float, arg2: float): void
 "getRotY"(): float
 "setScaleZ"(arg0: float): void
 "updateScale"(arg0: float, arg1: float, arg2: float): void
 "getRotZ"(): float
 "setPivotZ"(arg0: float): void
 "getPivotZ"(): float
 "setRotX"(arg0: float): void
 "setRotZ"(arg0: float): void
 "hasScaleChanged"(): boolean
 "saveSnapshot"(): $BoneSnapshot
 "hasRotationChanged"(): boolean
 "resetStateChanges"(): void
 "getInitialSnapshot"(): $BoneSnapshot
 "hasPositionChanged"(): boolean
 "setHidden"(arg0: boolean): void
 "isHidingChildren"(): boolean
 "getChildBones"(): $List<(any)>
 "getPosZ"(): float
}

export namespace $CoreGeoBone {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreGeoBone$Type = ($CoreGeoBone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreGeoBone_ = $CoreGeoBone$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/model/$CoreBakedGeoModel" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"

export interface $CoreBakedGeoModel {

 "searchForChildBone"(arg0: $CoreGeoBone$Type, arg1: string): $CoreGeoBone
 "getBones"(): $List<(any)>
 "getBone"(arg0: string): $Optional<(any)>
}

export namespace $CoreBakedGeoModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoreBakedGeoModel$Type = ($CoreBakedGeoModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoreBakedGeoModel_ = $CoreBakedGeoModel$Type;
}}
declare module "packages/software/bernie/example/block/$FertilizerBlock" {
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$RenderShape, $RenderShape$Type} from "packages/net/minecraft/world/level/block/$RenderShape"
import {$DirectionalBlock, $DirectionalBlock$Type} from "packages/net/minecraft/world/level/block/$DirectionalBlock"

export class $FertilizerBlock extends $DirectionalBlock implements $EntityBlock {
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

constructor()

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $BlockGetter$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "getRenderShape"(arg0: $BlockState$Type): $RenderShape
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FertilizerBlock$Type = ($FertilizerBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FertilizerBlock_ = $FertilizerBlock$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$FastBoneFilterGeoLayer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$BoneFilterGeoLayer, $BoneFilterGeoLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$BoneFilterGeoLayer"
import {$TriConsumer, $TriConsumer$Type} from "packages/org/apache/logging/log4j/util/$TriConsumer"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $FastBoneFilterGeoLayer<T extends $GeoAnimatable> extends $BoneFilterGeoLayer<(T)> {

constructor(arg0: $GeoRenderer$Type<(T)>, arg1: $Supplier$Type<($List$Type<(string)>)>, arg2: $TriConsumer$Type<($GeoBone$Type), (T), (float)>)
constructor(arg0: $GeoRenderer$Type<(T)>, arg1: $Supplier$Type<($List$Type<(string)>)>)
constructor(arg0: $GeoRenderer$Type<(T)>)

public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastBoneFilterGeoLayer$Type<T> = ($FastBoneFilterGeoLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastBoneFilterGeoLayer_<T> = $FastBoneFilterGeoLayer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$ItemArmorGeoLayer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ItemArmorGeoLayer<T extends ($LivingEntity) & ($GeoAnimatable)> extends $GeoRenderLayer<(T)> {

constructor(arg0: $GeoRenderer$Type<(T)>)

public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "getVanillaArmorResource"(arg0: $Entity$Type, arg1: $ItemStack$Type, arg2: $EquipmentSlot$Type, arg3: string): $ResourceLocation
public "renderForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemArmorGeoLayer$Type<T> = ($ItemArmorGeoLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemArmorGeoLayer_<T> = $ItemArmorGeoLayer$Type<(T)>;
}}
declare module "packages/software/bernie/example/client/model/entity/$ParasiteModel" {
import {$ParasiteEntity, $ParasiteEntity$Type} from "packages/software/bernie/example/entity/$ParasiteEntity"
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $ParasiteModel extends $DefaultedEntityGeoModel<($ParasiteEntity)> {

constructor()

public "getRenderType"(arg0: $ParasiteEntity$Type, arg1: $ResourceLocation$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParasiteModel$Type = ($ParasiteModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParasiteModel_ = $ParasiteModel$Type;
}}
declare module "packages/software/bernie/example/registry/$ItemRegistry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$JackInTheBoxItem, $JackInTheBoxItem$Type} from "packages/software/bernie/example/item/$JackInTheBoxItem"
import {$WolfArmorItem, $WolfArmorItem$Type} from "packages/software/bernie/example/item/$WolfArmorItem"
import {$ForgeSpawnEggItem, $ForgeSpawnEggItem$Type} from "packages/net/minecraftforge/common/$ForgeSpawnEggItem"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$GeckoArmorItem, $GeckoArmorItem$Type} from "packages/software/bernie/example/item/$GeckoArmorItem"

export class $ItemRegistry {
static readonly "ITEMS": $DeferredRegister<($Item)>
static readonly "TABS": $DeferredRegister<($CreativeModeTab)>
static readonly "GECKO_HABITAT": $RegistryObject<($BlockItem)>
static readonly "FERTILIZER": $RegistryObject<($BlockItem)>
static readonly "JACK_IN_THE_BOX": $RegistryObject<($JackInTheBoxItem)>
static readonly "WOLF_ARMOR_HELMET": $RegistryObject<($WolfArmorItem)>
static readonly "WOLF_ARMOR_CHESTPLATE": $RegistryObject<($WolfArmorItem)>
static readonly "WOLF_ARMOR_LEGGINGS": $RegistryObject<($WolfArmorItem)>
static readonly "WOLF_ARMOR_BOOTS": $RegistryObject<($WolfArmorItem)>
static readonly "GECKO_ARMOR_HELMET": $RegistryObject<($GeckoArmorItem)>
static readonly "GECKO_ARMOR_CHESTPLATE": $RegistryObject<($GeckoArmorItem)>
static readonly "GECKO_ARMOR_LEGGINGS": $RegistryObject<($GeckoArmorItem)>
static readonly "GECKO_ARMOR_BOOTS": $RegistryObject<($GeckoArmorItem)>
static readonly "BAT_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "BIKE_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "RACE_CAR_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "PARASITE_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "MUTANT_ZOMBIE_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "FAKE_GLASS_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "COOL_KID_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "GREMLIN_SPAWN_EGG": $RegistryObject<($ForgeSpawnEggItem)>
static readonly "GECKOLIB_TAB": $RegistryObject<($CreativeModeTab)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemRegistry$Type = ($ItemRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemRegistry_ = $ItemRegistry$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$EasingType" {
import {$Double2DoubleFunction, $Double2DoubleFunction$Type} from "packages/it/unimi/dsi/fastutil/doubles/$Double2DoubleFunction"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$AnimationPoint, $AnimationPoint$Type} from "packages/software/bernie/geckolib/core/keyframe/$AnimationPoint"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $EasingType {

 "apply"(arg0: $AnimationPoint$Type): double
 "apply"(arg0: $AnimationPoint$Type, arg1: double, arg2: double): double
 "buildTransformer"(arg0: double): $Double2DoubleFunction

(arg0: double): $Double2DoubleFunction
}

export namespace $EasingType {
const EASING_TYPES: $Map<(string), ($EasingType)>
const LINEAR: $EasingType
const STEP: $EasingType
const EASE_IN_SINE: $EasingType
const EASE_OUT_SINE: $EasingType
const EASE_IN_OUT_SINE: $EasingType
const EASE_IN_QUAD: $EasingType
const EASE_OUT_QUAD: $EasingType
const EASE_IN_OUT_QUAD: $EasingType
const EASE_IN_CUBIC: $EasingType
const EASE_OUT_CUBIC: $EasingType
const EASE_IN_OUT_CUBIC: $EasingType
const EASE_IN_QUART: $EasingType
const EASE_OUT_QUART: $EasingType
const EASE_IN_OUT_QUART: $EasingType
const EASE_IN_QUINT: $EasingType
const EASE_OUT_QUINT: $EasingType
const EASE_IN_OUT_QUINT: $EasingType
const EASE_IN_EXPO: $EasingType
const EASE_OUT_EXPO: $EasingType
const EASE_IN_OUT_EXPO: $EasingType
const EASE_IN_CIRC: $EasingType
const EASE_OUT_CIRC: $EasingType
const EASE_IN_OUT_CIRC: $EasingType
const EASE_IN_BACK: $EasingType
const EASE_OUT_BACK: $EasingType
const EASE_IN_OUT_BACK: $EasingType
const EASE_IN_ELASTIC: $EasingType
const EASE_OUT_ELASTIC: $EasingType
const EASE_IN_OUT_ELASTIC: $EasingType
const EASE_IN_BOUNCE: $EasingType
const EASE_OUT_BOUNCE: $EasingType
const EASE_IN_OUT_BOUNCE: $EasingType
const CATMULLROM: $EasingType
function pow(arg0: double): $Double2DoubleFunction
function exp(arg0: double): double
function register(arg0: string, arg1: $EasingType$Type): $EasingType
function step(arg0: double): $Double2DoubleFunction
function back(arg0: double): $Double2DoubleFunction
function fromString(arg0: string): $EasingType
function fromJson(arg0: $JsonElement$Type): $EasingType
function stepPositive(arg0: $Double2DoubleFunction$Type): $Double2DoubleFunction
function lerpWithOverride(arg0: $AnimationPoint$Type, arg1: $EasingType$Type): double
function easeIn(arg0: $Double2DoubleFunction$Type): $Double2DoubleFunction
function easeOut(arg0: $Double2DoubleFunction$Type): $Double2DoubleFunction
function catmullRom(arg0: double): double
function easeInOut(arg0: $Double2DoubleFunction$Type): $Double2DoubleFunction
function stepNonNegative(arg0: $Double2DoubleFunction$Type): $Double2DoubleFunction
function quadratic(arg0: double): double
function cubic(arg0: double): double
function elastic(arg0: double): $Double2DoubleFunction
function sine(arg0: double): double
function bounce(arg0: double): $Double2DoubleFunction
function circle(arg0: double): double
function linear(arg0: double): double
function linear(arg0: $Double2DoubleFunction$Type): $Double2DoubleFunction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EasingType$Type = ($EasingType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EasingType_ = $EasingType$Type;
}}
declare module "packages/software/bernie/geckolib/loading/$FileLoader" {
import {$BakedAnimations, $BakedAnimations$Type} from "packages/software/bernie/geckolib/loading/object/$BakedAnimations"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Model, $Model$Type} from "packages/software/bernie/geckolib/loading/json/raw/$Model"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $FileLoader {

constructor()

public static "getFileContents"(arg0: $ResourceLocation$Type, arg1: $ResourceManager$Type): string
public static "loadFile"(arg0: $ResourceLocation$Type, arg1: $ResourceManager$Type): $JsonObject
public static "loadAnimationsFile"(arg0: $ResourceLocation$Type, arg1: $ResourceManager$Type): $BakedAnimations
public static "loadModelFile"(arg0: $ResourceLocation$Type, arg1: $ResourceManager$Type): $Model
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FileLoader$Type = ($FileLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FileLoader_ = $FileLoader$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationController$State" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $AnimationController$State extends $Enum<($AnimationController$State)> {
static readonly "RUNNING": $AnimationController$State
static readonly "TRANSITIONING": $AnimationController$State
static readonly "PAUSED": $AnimationController$State
static readonly "STOPPED": $AnimationController$State


public static "values"(): ($AnimationController$State)[]
public static "valueOf"(arg0: string): $AnimationController$State
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationController$State$Type = (("running") | ("paused") | ("stopped") | ("transitioning")) | ($AnimationController$State);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationController$State_ = $AnimationController$State$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/$LazyVariable" {
import {$Variable, $Variable$Type} from "packages/com/eliotlash/mclib/math/$Variable"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"

export class $LazyVariable extends $Variable {

constructor(arg0: string, arg1: double)
constructor(arg0: string, arg1: $DoubleSupplier$Type)

public "get"(): double
public static "from"(arg0: $Variable$Type): $LazyVariable
public "set"(arg0: $DoubleSupplier$Type): void
public "set"(arg0: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LazyVariable$Type = ($LazyVariable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LazyVariable_ = $LazyVariable$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$KeyframeLocation" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Keyframe, $Keyframe$Type} from "packages/software/bernie/geckolib/core/keyframe/$Keyframe"

export class $KeyframeLocation<T extends $Keyframe<(any)>> extends $Record {

constructor(keyframe: T, startTick: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "startTick"(): double
public "keyframe"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeLocation$Type<T> = ($KeyframeLocation<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeLocation_<T> = $KeyframeLocation$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$LocatorValue" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$LocatorClass, $LocatorClass$Type} from "packages/software/bernie/geckolib/loading/json/raw/$LocatorClass"

export class $LocatorValue extends $Record {

constructor(locatorClass: $LocatorClass$Type, values: (double)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): (double)[]
public "hashCode"(): integer
public static "deserializer"(): $JsonDeserializer<($LocatorValue)>
public "locatorClass"(): $LocatorClass
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocatorValue$Type = ($LocatorValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocatorValue_ = $LocatorValue$Type;
}}
declare module "packages/software/bernie/geckolib/loading/object/$BakedAnimations" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Animation, $Animation$Type} from "packages/software/bernie/geckolib/core/animation/$Animation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BakedAnimations extends $Record {

constructor(animations: $Map$Type<(string), ($Animation$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getAnimation"(arg0: string): $Animation
public "animations"(): $Map<(string), ($Animation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedAnimations$Type = ($BakedAnimations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedAnimations_ = $BakedAnimations$Type;
}}
declare module "packages/software/bernie/geckolib/cache/object/$GeoCube" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $GeoCube extends $Record {

constructor(quads: ($GeoQuad$Type)[], pivot: $Vec3$Type, rotation: $Vec3$Type, size: $Vec3$Type, inflate: double, mirror: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "inflate"(): double
public "size"(): $Vec3
public "pivot"(): $Vec3
public "quads"(): ($GeoQuad)[]
public "rotation"(): $Vec3
public "mirror"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoCube$Type = ($GeoCube);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoCube_ = $GeoCube$Type;
}}
declare module "packages/software/bernie/geckolib/core/object/$Axis" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Axis extends $Enum<($Axis)> {
static readonly "X": $Axis
static readonly "Y": $Axis
static readonly "Z": $Axis


public static "values"(): ($Axis)[]
public static "valueOf"(arg0: string): $Axis
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Axis$Type = (("x") | ("y") | ("z")) | ($Axis);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Axis_ = $Axis$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoArmorRenderer" {
import {$HumanoidModel$ArmPose, $HumanoidModel$ArmPose$Type} from "packages/net/minecraft/client/model/$HumanoidModel$ArmPose"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$HumanoidModel, $HumanoidModel$Type} from "packages/net/minecraft/client/model/$HumanoidModel"
import {$GeoItem, $GeoItem$Type} from "packages/software/bernie/geckolib/animatable/$GeoItem"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"

export class $GeoArmorRenderer<T extends ($Item) & ($GeoItem)> extends $HumanoidModel<(any)> implements $GeoRenderer<(T)> {
static readonly "OVERLAY_SCALE": float
static readonly "HAT_OVERLAY_SCALE": float
static readonly "LEGGINGS_OVERLAY_SCALE": float
static readonly "TOOT_HORN_XROT_BASE": float
static readonly "TOOT_HORN_YROT_BASE": float
readonly "head": $ModelPart
readonly "hat": $ModelPart
readonly "body": $ModelPart
readonly "rightArm": $ModelPart
readonly "leftArm": $ModelPart
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

constructor(arg0: $GeoModel$Type<(T)>)

public "getCurrentEntity"(): $Entity
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
public "setAllVisible"(arg0: boolean): void
public "updateAnimatedTextureFrame"(arg0: T): void
public "fireCompileRenderLayersEvent"(): void
public "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
public "getInstanceId"(arg0: T): long
public "getGeoModel"(): $GeoModel<(T)>
public "addRenderLayer"(arg0: $GeoRenderLayer$Type<(T)>): $GeoArmorRenderer<(T)>
public "withScale"(arg0: float): $GeoArmorRenderer<(T)>
public "withScale"(arg0: float, arg1: float): $GeoArmorRenderer<(T)>
public "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderToBuffer"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: integer, arg4: float, arg5: float, arg6: float, arg7: float): void
public "getCurrentSlot"(): $EquipmentSlot
public "getCurrentStack"(): $ItemStack
public "scaleModelForBaby"(arg0: $PoseStack$Type, arg1: T, arg2: float, arg3: boolean): void
public "getLeftBootBone"(): $GeoBone
public "getRightArmBone"(): $GeoBone
public "getRightBootBone"(): $GeoBone
public "prepForRender"(arg0: $Entity$Type, arg1: $ItemStack$Type, arg2: $EquipmentSlot$Type, arg3: $HumanoidModel$Type<(any)>): void
public "getRightLegBone"(): $GeoBone
public "getBodyBone"(): $GeoBone
public "getHeadBone"(): $GeoBone
public "getLeftArmBone"(): $GeoBone
public "getLeftLegBone"(): $GeoBone
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
public "applyBoneVisibilityByPart"(arg0: $EquipmentSlot$Type, arg1: $ModelPart$Type, arg2: $HumanoidModel$Type<(any)>): void
public "getMotionAnimThreshold"(arg0: T): float
public "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
public "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
public "getTextureLocation"(arg0: T): $ResourceLocation
public "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
/**
 * 
 * @deprecated
 */
public "getPackedOverlay"(arg0: T, arg1: float): integer
public "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
get "currentEntity"(): $Entity
set "allVisible"(value: boolean)
get "geoModel"(): $GeoModel<(T)>
get "currentSlot"(): $EquipmentSlot
get "currentStack"(): $ItemStack
get "leftBootBone"(): $GeoBone
get "rightArmBone"(): $GeoBone
get "rightBootBone"(): $GeoBone
get "rightLegBone"(): $GeoBone
get "bodyBone"(): $GeoBone
get "headBone"(): $GeoBone
get "leftArmBone"(): $GeoBone
get "leftLegBone"(): $GeoBone
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoArmorRenderer$Type<T> = ($GeoArmorRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoArmorRenderer_<T> = $GeoArmorRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/network/packet/$EntityAnimTriggerPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EntityAnimTriggerPacket<D> {

constructor(arg0: integer, arg1: string, arg2: string)
constructor(arg0: integer, arg1: boolean, arg2: string, arg3: string)

public static "decode"<D>(arg0: $FriendlyByteBuf$Type): $EntityAnimTriggerPacket<(D)>
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "receivePacket"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAnimTriggerPacket$Type<D> = ($EntityAnimTriggerPacket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAnimTriggerPacket_<D> = $EntityAnimTriggerPacket$Type<(D)>;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/data/$ParticleKeyframeData" {
import {$KeyFrameData, $KeyFrameData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$KeyFrameData"

export class $ParticleKeyframeData extends $KeyFrameData {

constructor(arg0: double, arg1: string, arg2: string, arg3: string)

public "hashCode"(): integer
public "script"(): string
public "getLocator"(): string
public "getEffect"(): string
get "locator"(): string
get "effect"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleKeyframeData$Type = ($ParticleKeyframeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleKeyframeData_ = $ParticleKeyframeData$Type;
}}
declare module "packages/software/bernie/geckolib/model/data/$EntityModelData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $EntityModelData extends $Record {

constructor(isSitting: boolean, isChild: boolean, netHeadYaw: float, headPitch: float)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isSitting"(): boolean
public "headPitch"(): float
public "netHeadYaw"(): float
public "isChild"(): boolean
get "sitting"(): boolean
get "child"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityModelData$Type = ($EntityModelData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityModelData_ = $EntityModelData$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$AnimationPointQueue" {
import {$LinkedList, $LinkedList$Type} from "packages/java/util/$LinkedList"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$AnimationPoint, $AnimationPoint$Type} from "packages/software/bernie/geckolib/core/keyframe/$AnimationPoint"

export class $AnimationPointQueue extends $LinkedList<($AnimationPoint)> {

constructor()

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "isEmpty"(): boolean
public "subList"(arg0: integer, arg1: integer): $List<(E)>
public "iterator"(): $Iterator<(E)>
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
public "removeAll"(arg0: $Collection$Type<(any)>): boolean
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "listIterator"(): $ListIterator<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationPointQueue$Type = ($AnimationPointQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationPointQueue_ = $AnimationPointQueue$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$GremlinRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$DynamicGeoEntityRenderer, $DynamicGeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$DynamicGeoEntityRenderer"
import {$DynamicExampleEntity, $DynamicExampleEntity$Type} from "packages/software/bernie/example/entity/$DynamicExampleEntity"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $GremlinRenderer extends $DynamicGeoEntityRenderer<($DynamicExampleEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "preRender"(arg0: $PoseStack$Type, arg1: $DynamicExampleEntity$Type, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GremlinRenderer$Type = ($GremlinRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GremlinRenderer_ = $GremlinRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/animatable/$GeoBlockEntity" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"

export interface $GeoBlockEntity extends $GeoAnimatable {

 "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
 "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
 "getTick"(arg0: any): double
 "triggerAnim"(arg0: string, arg1: string): void
 "getBoneResetTime"(): double
 "getAnimatableInstanceCache"(): $AnimatableInstanceCache
 "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
 "shouldPlayAnimsWhileGamePaused"(): boolean
 "animatableCacheOverride"(): $AnimatableInstanceCache
}

export namespace $GeoBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoBlockEntity$Type = ($GeoBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoBlockEntity_ = $GeoBlockEntity$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$ParasiteRenderer" {
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$ParasiteEntity, $ParasiteEntity$Type} from "packages/software/bernie/example/entity/$ParasiteEntity"

export class $ParasiteRenderer extends $GeoEntityRenderer<($ParasiteEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParasiteRenderer$Type = ($ParasiteRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParasiteRenderer_ = $ParasiteRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$PolysUnion" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PolysUnion$Type, $PolysUnion$Type$Type} from "packages/software/bernie/geckolib/loading/json/raw/$PolysUnion$Type"

export class $PolysUnion extends $Record {

constructor(union: (((double)[])[])[], type: $PolysUnion$Type$Type)

public "type"(): $PolysUnion$Type
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "union"(): (((double)[])[])[]
public static "deserializer"(): $JsonDeserializer<($PolysUnion)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolysUnion$Type = ($PolysUnion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolysUnion_ = $PolysUnion$Type;
}}
declare module "packages/software/bernie/example/entity/$ParasiteEntity" {
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Monster, $Monster$Type} from "packages/net/minecraft/world/entity/monster/$Monster"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ParasiteEntity extends $Monster implements $GeoEntity {
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParasiteEntity$Type = ($ParasiteEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParasiteEntity_ = $ParasiteEntity$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/$MolangQueries" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MolangQueries {
static readonly "ANIM_TIME": string
static readonly "LIFE_TIME": string
static readonly "ACTOR_COUNT": string
static readonly "TIME_OF_DAY": string
static readonly "MOON_PHASE": string
static readonly "DISTANCE_FROM_CAMERA": string
static readonly "IS_ON_GROUND": string
static readonly "IS_IN_WATER": string
static readonly "IS_IN_WATER_OR_RAIN": string
static readonly "HEALTH": string
static readonly "MAX_HEALTH": string
static readonly "IS_ON_FIRE": string
static readonly "GROUND_SPEED": string
static readonly "YAW_SPEED": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MolangQueries$Type = ($MolangQueries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MolangQueries_ = $MolangQueries$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/data/$SoundKeyframeData" {
import {$KeyFrameData, $KeyFrameData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$KeyFrameData"

export class $SoundKeyframeData extends $KeyFrameData {

constructor(arg0: double, arg1: string)

public "hashCode"(): integer
public "getSound"(): string
get "sound"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoundKeyframeData$Type = ($SoundKeyframeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoundKeyframeData_ = $SoundKeyframeData$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$AnimationPoint" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Keyframe, $Keyframe$Type} from "packages/software/bernie/geckolib/core/keyframe/$Keyframe"

export class $AnimationPoint extends $Record {

constructor(keyFrame: $Keyframe$Type<(any)>, currentTick: double, transitionLength: double, animationStartValue: double, animationEndValue: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "animationEndValue"(): double
public "keyFrame"(): $Keyframe<(any)>
public "transitionLength"(): double
public "currentTick"(): double
public "animationStartValue"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationPoint$Type = ($AnimationPoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationPoint_ = $AnimationPoint$Type;
}}
declare module "packages/software/bernie/geckolib/util/$GeckoLibUtil" {
import {$EasingType, $EasingType$Type} from "packages/software/bernie/geckolib/core/animation/$EasingType"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$Animation$LoopType, $Animation$LoopType$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$LoopType"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$BakedModelFactory, $BakedModelFactory$Type} from "packages/software/bernie/geckolib/loading/object/$BakedModelFactory"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"

export class $GeckoLibUtil {

constructor()

public static "addDataTicket"<D>(arg0: $SerializableDataTicket$Type<(D)>): $SerializableDataTicket<(D)>
public static "addCustomLoopType"(arg0: string, arg1: $Animation$LoopType$Type): $Animation$LoopType
public static "addCustomBakedModelFactory"(arg0: string, arg1: $BakedModelFactory$Type): void
public static "addCustomEasingType"(arg0: string, arg1: $EasingType$Type): $EasingType
public static "createInstanceCache"(arg0: $GeoAnimatable$Type): $AnimatableInstanceCache
public static "createInstanceCache"(arg0: $GeoAnimatable$Type, arg1: boolean): $AnimatableInstanceCache
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLibUtil$Type = ($GeckoLibUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLibUtil_ = $GeckoLibUtil$Type;
}}
declare module "packages/software/bernie/geckolib/cache/$GeckoLibCache" {
import {$BakedAnimations, $BakedAnimations$Type} from "packages/software/bernie/geckolib/loading/object/$BakedAnimations"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GeckoLibCache {

constructor()

public static "registerReloadListener"(): void
public static "getBakedModels"(): $Map<($ResourceLocation), ($BakedGeoModel)>
public static "getBakedAnimations"(): $Map<($ResourceLocation), ($BakedAnimations)>
get "bakedModels"(): $Map<($ResourceLocation), ($BakedGeoModel)>
get "bakedAnimations"(): $Map<($ResourceLocation), ($BakedAnimations)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLibCache$Type = ($GeckoLibCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLibCache_ = $GeckoLibCache$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$FakeGlassRenderer" {
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$DynamicGeoEntityRenderer, $DynamicGeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$DynamicGeoEntityRenderer"
import {$FakeGlassEntity, $FakeGlassEntity$Type} from "packages/software/bernie/example/entity/$FakeGlassEntity"

export class $FakeGlassRenderer extends $DynamicGeoEntityRenderer<($FakeGlassEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FakeGlassRenderer$Type = ($FakeGlassRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FakeGlassRenderer_ = $FakeGlassRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$DyeableGeoArmorRenderer" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoArmorRenderer, $GeoArmorRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoArmorRenderer"
import {$HumanoidModel$ArmPose, $HumanoidModel$ArmPose$Type} from "packages/net/minecraft/client/model/$HumanoidModel$ArmPose"
import {$GeoItem, $GeoItem$Type} from "packages/software/bernie/geckolib/animatable/$GeoItem"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"

export class $DyeableGeoArmorRenderer<T extends ($Item) & ($GeoItem)> extends $GeoArmorRenderer<(T)> {
static readonly "OVERLAY_SCALE": float
static readonly "HAT_OVERLAY_SCALE": float
static readonly "LEGGINGS_OVERLAY_SCALE": float
static readonly "TOOT_HORN_XROT_BASE": float
static readonly "TOOT_HORN_YROT_BASE": float
readonly "head": $ModelPart
readonly "hat": $ModelPart
readonly "body": $ModelPart
readonly "rightArm": $ModelPart
readonly "leftArm": $ModelPart
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

constructor(arg0: $GeoModel$Type<(T)>)

public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DyeableGeoArmorRenderer$Type<T> = ($DyeableGeoArmorRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DyeableGeoArmorRenderer_<T> = $DyeableGeoArmorRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoReplacedEntityRenderer" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Operation, $Operation$Type} from "packages/com/llamalad7/mixinextras/injector/wrapoperation/$Operation"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LocalFloatRef, $LocalFloatRef$Type} from "packages/com/llamalad7/mixinextras/sugar/ref/$LocalFloatRef"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $GeoReplacedEntityRenderer<E extends $Entity, T extends $GeoAnimatable> extends $EntityRenderer<(E)> implements $GeoRenderer<(T)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type, arg1: $GeoModel$Type<(T)>, arg2: T)

public "getCurrentEntity"(): E
public "getNameRenderCutoffDistance"(arg0: E, arg1: T): double
public "updateAnimatedTextureFrame"(arg0: T): void
public "fireCompileRenderLayersEvent"(): void
public "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "render"(arg0: E, arg1: float, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void
public "m_6512_"(arg0: E): boolean
public "getTextureLocation"(arg0: E): $ResourceLocation
public "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
public "getInstanceId"(arg0: T): long
public "getAnimatable"(): T
public "getGeoModel"(): $GeoModel<(T)>
public "addRenderLayer"(arg0: $GeoRenderLayer$Type<(T)>): $GeoReplacedEntityRenderer<(E), (T)>
public "withScale"(arg0: float, arg1: float): $GeoReplacedEntityRenderer<(E), (T)>
public "withScale"(arg0: float): $GeoReplacedEntityRenderer<(E), (T)>
public "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "isShaking"(arg0: T): boolean
public "renderLeash"<H extends $Entity, M extends $Mob>(arg0: M, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: H): void
public "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
public "getPackedOverlay"(arg0: T, arg1: float): integer
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
public "wrapOperation$dhd001$l2hostility$isInvisibleTo$geckoFix"(arg0: $Entity$Type, arg1: $Player$Type, arg2: $Operation$Type<(any)>, arg3: $LocalFloatRef$Type): boolean
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
public "getMotionAnimThreshold"(arg0: T): float
public "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
public "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
public "getTextureLocation"(arg0: T): $ResourceLocation
public "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
get "currentEntity"(): E
get "animatable"(): T
get "geoModel"(): $GeoModel<(T)>
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoReplacedEntityRenderer$Type<E, T> = ($GeoReplacedEntityRenderer<(E), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoReplacedEntityRenderer_<E, T> = $GeoReplacedEntityRenderer$Type<(E), (T)>;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/$ParticleKeyframeEvent" {
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$ParticleKeyframeData, $ParticleKeyframeData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$ParticleKeyframeData"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$KeyFrameEvent, $KeyFrameEvent$Type} from "packages/software/bernie/geckolib/core/keyframe/event/$KeyFrameEvent"

export class $ParticleKeyframeEvent<T extends $GeoAnimatable> extends $KeyFrameEvent<(T), ($ParticleKeyframeData)> {

constructor(arg0: T, arg1: double, arg2: $AnimationController$Type<(T)>, arg3: $ParticleKeyframeData$Type)

public "getKeyframeData"(): $ParticleKeyframeData
get "keyframeData"(): $ParticleKeyframeData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleKeyframeEvent$Type<T> = ($ParticleKeyframeEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleKeyframeEvent_<T> = $ParticleKeyframeEvent$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/molang/functions/$CosDegrees" {
import {$Function, $Function$Type} from "packages/com/eliotlash/mclib/math/functions/$Function"
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $CosDegrees extends $Function {

constructor(arg0: ($IValue$Type)[], arg1: string)

public "get"(): double
public "getRequiredArguments"(): integer
get "requiredArguments"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CosDegrees$Type = ($CosDegrees);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CosDegrees_ = $CosDegrees$Type;
}}
declare module "packages/software/bernie/geckolib/core/state/$BoneSnapshot" {
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"

export class $BoneSnapshot {

constructor(arg0: $CoreGeoBone$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copy"(arg0: $BoneSnapshot$Type): $BoneSnapshot
public "getScaleX"(): float
public "getScaleY"(): float
public "updateOffset"(arg0: float, arg1: float, arg2: float): void
public "updateRotation"(arg0: float, arg1: float, arg2: float): void
public "getRotX"(): float
public "getScaleZ"(): float
public "getRotY"(): float
public "updateScale"(arg0: float, arg1: float, arg2: float): void
public "getRotZ"(): float
public "startPosAnim"(): void
public "startRotAnim"(): void
public "stopScaleAnim"(arg0: double): void
public "stopRotAnim"(arg0: double): void
public "startScaleAnim"(): void
public "stopPosAnim"(arg0: double): void
public "getOffsetX"(): float
public "getOffsetZ"(): float
public "getOffsetY"(): float
public "getBone"(): $CoreGeoBone
public "isRotAnimInProgress"(): boolean
public "isScaleAnimInProgress"(): boolean
public "getLastResetScaleTick"(): double
public "isPosAnimInProgress"(): boolean
public "getLastResetRotationTick"(): double
public "getLastResetPositionTick"(): double
get "scaleX"(): float
get "scaleY"(): float
get "rotX"(): float
get "scaleZ"(): float
get "rotY"(): float
get "rotZ"(): float
get "offsetX"(): float
get "offsetZ"(): float
get "offsetY"(): float
get "bone"(): $CoreGeoBone
get "rotAnimInProgress"(): boolean
get "scaleAnimInProgress"(): boolean
get "lastResetScaleTick"(): double
get "posAnimInProgress"(): boolean
get "lastResetRotationTick"(): double
get "lastResetPositionTick"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoneSnapshot$Type = ($BoneSnapshot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoneSnapshot_ = $BoneSnapshot$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$BoneFilterGeoLayer" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$TriConsumer, $TriConsumer$Type} from "packages/org/apache/logging/log4j/util/$TriConsumer"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BoneFilterGeoLayer<T extends $GeoAnimatable> extends $GeoRenderLayer<(T)> {

constructor(arg0: $GeoRenderer$Type<(T)>)
constructor(arg0: $GeoRenderer$Type<(T)>, arg1: $TriConsumer$Type<($GeoBone$Type), (T), (float)>)

public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoneFilterGeoLayer$Type<T> = ($BoneFilterGeoLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoneFilterGeoLayer_<T> = $BoneFilterGeoLayer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/animatable/$GeoItem" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$SingletonGeoAnimatable, $SingletonGeoAnimatable$Type} from "packages/software/bernie/geckolib/animatable/$SingletonGeoAnimatable"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $GeoItem extends $SingletonGeoAnimatable {

 "isPerspectiveAware"(): boolean
 "getTick"(arg0: any): double
 "animatableCacheOverride"(): $AnimatableInstanceCache
 "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
 "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
 "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
 "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
 "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
 "getBoneResetTime"(): double
 "getAnimatableInstanceCache"(): $AnimatableInstanceCache
 "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
 "shouldPlayAnimsWhileGamePaused"(): boolean
}

export namespace $GeoItem {
const ID_NBT_KEY: string
function getId(arg0: $ItemStack$Type): long
function registerSyncedAnimatable(arg0: $GeoAnimatable$Type): void
function getOrAssignId(arg0: $ItemStack$Type, arg1: $ServerLevel$Type): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoItem$Type = ($GeoItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoItem_ = $GeoItem$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoItemRenderer" {
import {$BlockEntityWithoutLevelRenderer, $BlockEntityWithoutLevelRenderer$Type} from "packages/net/minecraft/client/renderer/$BlockEntityWithoutLevelRenderer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$BlockEntityRenderDispatcher, $BlockEntityRenderDispatcher$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderDispatcher"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $GeoItemRenderer<T extends ($Item) & ($GeoAnimatable)> extends $BlockEntityWithoutLevelRenderer implements $GeoRenderer<(T)> {

constructor(arg0: $GeoModel$Type<(T)>)
constructor(arg0: $BlockEntityRenderDispatcher$Type, arg1: $EntityModelSet$Type, arg2: $GeoModel$Type<(T)>)

public "updateAnimatedTextureFrame"(arg0: T): void
public "fireCompileRenderLayersEvent"(): void
public "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
public "getInstanceId"(arg0: T): long
public "getTextureLocation"(arg0: T): $ResourceLocation
public "getAnimatable"(): T
public "getGeoModel"(): $GeoModel<(T)>
public "addRenderLayer"(arg0: $GeoRenderLayer$Type<(T)>): $GeoItemRenderer<(T)>
public "withScale"(arg0: float, arg1: float): $GeoItemRenderer<(T)>
public "withScale"(arg0: float): $GeoItemRenderer<(T)>
public "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderByItem"(arg0: $ItemStack$Type, arg1: $ItemDisplayContext$Type, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
public "getCurrentItemStack"(): $ItemStack
public "setupLightingForGuiRender"(): void
public "useAlternateGuiLighting"(): $GeoItemRenderer<(T)>
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
public "getMotionAnimThreshold"(arg0: T): float
public "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
public "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
public "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
/**
 * 
 * @deprecated
 */
public "getPackedOverlay"(arg0: T, arg1: float): integer
public "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
get "animatable"(): T
get "geoModel"(): $GeoModel<(T)>
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
get "currentItemStack"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoItemRenderer$Type<T> = ($GeoItemRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoItemRenderer_<T> = $GeoItemRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$LocatorClass" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $LocatorClass extends $Record {

constructor(ignoreInheritedScale: boolean, offset: (double)[], rotation: (double)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "offset"(): (double)[]
public static "deserializer"(): $JsonDeserializer<($LocatorClass)>
public "rotation"(): (double)[]
public "ignoreInheritedScale"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocatorClass$Type = ($LocatorClass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocatorClass_ = $LocatorClass$Type;
}}
declare module "packages/software/bernie/example/entity/$DynamicExampleEntity" {
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$PathfinderMob, $PathfinderMob$Type} from "packages/net/minecraft/world/entity/$PathfinderMob"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DynamicExampleEntity extends $PathfinderMob implements $GeoEntity {
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "isWieldingTwoHandedWeapon"(): boolean
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "wieldingTwoHandedWeapon"(): boolean
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicExampleEntity$Type = ($DynamicExampleEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicExampleEntity_ = $DynamicExampleEntity$Type;
}}
declare module "packages/software/bernie/geckolib/$GeckoLibException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GeckoLibException extends $RuntimeException {

constructor(arg0: $ResourceLocation$Type, arg1: string)
constructor(arg0: $ResourceLocation$Type, arg1: string, arg2: $Throwable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLibException$Type = ($GeckoLibException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLibException_ = $GeckoLibException$Type;
}}
declare module "packages/software/bernie/geckolib/core/object/$PlayState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PlayState extends $Enum<($PlayState)> {
static readonly "CONTINUE": $PlayState
static readonly "STOP": $PlayState


public static "values"(): ($PlayState)[]
public static "valueOf"(arg0: string): $PlayState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayState$Type = (("stop") | ("continue")) | ($PlayState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayState_ = $PlayState$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/expressions/$MolangVariableHolder" {
import {$Variable, $Variable$Type} from "packages/com/eliotlash/mclib/math/$Variable"
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"
import {$MolangValue, $MolangValue$Type} from "packages/software/bernie/geckolib/core/molang/expressions/$MolangValue"

export class $MolangVariableHolder extends $MolangValue {
 "variable": $Variable

constructor(arg0: $Variable$Type, arg1: $IValue$Type)

public "get"(): double
public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MolangVariableHolder$Type = ($MolangVariableHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MolangVariableHolder_ = $MolangVariableHolder$Type;
}}
declare module "packages/software/bernie/example/client/model/entity/$BikeModel" {
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$BikeEntity, $BikeEntity$Type} from "packages/software/bernie/example/entity/$BikeEntity"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $BikeModel extends $DefaultedEntityGeoModel<($BikeEntity)> {

constructor()

public "getRenderType"(arg0: $BikeEntity$Type, arg1: $ResourceLocation$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BikeModel$Type = ($BikeModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BikeModel_ = $BikeModel$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$Animation$Keyframes" {
import {$SoundKeyframeData, $SoundKeyframeData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$SoundKeyframeData"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CustomInstructionKeyframeData, $CustomInstructionKeyframeData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$CustomInstructionKeyframeData"
import {$ParticleKeyframeData, $ParticleKeyframeData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$ParticleKeyframeData"

export class $Animation$Keyframes extends $Record {

constructor(sounds: ($SoundKeyframeData$Type)[], particles: ($ParticleKeyframeData$Type)[], customInstructions: ($CustomInstructionKeyframeData$Type)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "particles"(): ($ParticleKeyframeData)[]
public "customInstructions"(): ($CustomInstructionKeyframeData)[]
public "sounds"(): ($SoundKeyframeData)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Animation$Keyframes$Type = ($Animation$Keyframes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Animation$Keyframes_ = $Animation$Keyframes$Type;
}}
declare module "packages/software/bernie/example/client/renderer/block/$FertilizerBlockRenderer" {
import {$GeoBlockRenderer, $GeoBlockRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoBlockRenderer"
import {$FertilizerBlockEntity, $FertilizerBlockEntity$Type} from "packages/software/bernie/example/block/entity/$FertilizerBlockEntity"

export class $FertilizerBlockRenderer extends $GeoBlockRenderer<($FertilizerBlockEntity)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FertilizerBlockRenderer$Type = ($FertilizerBlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FertilizerBlockRenderer_ = $FertilizerBlockRenderer$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$RaceCarRenderer" {
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$RaceCarEntity, $RaceCarEntity$Type} from "packages/software/bernie/example/entity/$RaceCarEntity"

export class $RaceCarRenderer extends $GeoEntityRenderer<($RaceCarEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RaceCarRenderer$Type = ($RaceCarRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RaceCarRenderer_ = $RaceCarRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/cache/$AnimatableIdCache" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $AnimatableIdCache extends $SavedData {


public static "getFreeId"(arg0: $ServerLevel$Type): long
public "save"(arg0: $CompoundTag$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatableIdCache$Type = ($AnimatableIdCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatableIdCache_ = $AnimatableIdCache$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/instance/$SingletonAnimatableInstanceCache" {
import {$AnimatableManager, $AnimatableManager$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"

export class $SingletonAnimatableInstanceCache extends $AnimatableInstanceCache {

constructor(arg0: $GeoAnimatable$Type)

public "getManagerForId"(arg0: long): $AnimatableManager<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingletonAnimatableInstanceCache$Type = ($SingletonAnimatableInstanceCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingletonAnimatableInstanceCache_ = $SingletonAnimatableInstanceCache$Type;
}}
declare module "packages/software/bernie/example/client/model/entity/$MutantZombieModel" {
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$DynamicExampleEntity, $DynamicExampleEntity$Type} from "packages/software/bernie/example/entity/$DynamicExampleEntity"

export class $MutantZombieModel extends $DefaultedEntityGeoModel<($DynamicExampleEntity)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutantZombieModel$Type = ($MutantZombieModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutantZombieModel_ = $MutantZombieModel$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimatableManager" {
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$BoneSnapshot, $BoneSnapshot$Type} from "packages/software/bernie/geckolib/core/state/$BoneSnapshot"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnimatableManager<T extends $GeoAnimatable> {

constructor(arg0: $GeoAnimatable$Type)

public "getData"<D>(arg0: $DataTicket$Type<(D)>): D
public "setData"<D>(arg0: $DataTicket$Type<(D)>, arg1: D): void
public "tryTriggerAnimation"(arg0: string): void
public "tryTriggerAnimation"(arg0: string, arg1: string): void
public "removeController"(arg0: string): void
public "getLastUpdateTime"(): double
public "startedAt"(arg0: double): void
public "isFirstTick"(): boolean
public "updatedAt"(arg0: double): void
public "getFirstTickTime"(): double
public "getAnimationControllers"(): $Map<(string), ($AnimationController<(T)>)>
public "getBoneSnapshotCollection"(): $Map<(string), ($BoneSnapshot)>
public "addController"(arg0: $AnimationController$Type<(any)>): void
public "clearSnapshotCache"(): void
get "lastUpdateTime"(): double
get "firstTick"(): boolean
get "firstTickTime"(): double
get "animationControllers"(): $Map<(string), ($AnimationController<(T)>)>
get "boneSnapshotCollection"(): $Map<(string), ($BoneSnapshot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatableManager$Type<T> = ($AnimatableManager<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatableManager_<T> = $AnimatableManager$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/network/packet/$BlockEntityAnimDataSyncPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockEntityAnimDataSyncPacket<D> {

constructor(arg0: $BlockPos$Type, arg1: $SerializableDataTicket$Type<(D)>, arg2: D)

public static "decode"<D>(arg0: $FriendlyByteBuf$Type): $BlockEntityAnimDataSyncPacket<(D)>
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "receivePacket"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityAnimDataSyncPacket$Type<D> = ($BlockEntityAnimDataSyncPacket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityAnimDataSyncPacket_<D> = $BlockEntityAnimDataSyncPacket$Type<(D)>;
}}
declare module "packages/software/bernie/geckolib/util/$ClientUtils" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $ClientUtils {

constructor()

public static "getLevel"(): $Level
public static "getClientPlayer"(): $Player
get "level"(): $Level
get "clientPlayer"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientUtils$Type = ($ClientUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientUtils_ = $ClientUtils$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"

export interface $GeoAnimatable {

 "getBoneResetTime"(): double
 "getAnimatableInstanceCache"(): $AnimatableInstanceCache
 "getTick"(arg0: any): double
 "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
 "shouldPlayAnimsWhileGamePaused"(): boolean
 "animatableCacheOverride"(): $AnimatableInstanceCache
}

export namespace $GeoAnimatable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoAnimatable$Type = ($GeoAnimatable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoAnimatable_ = $GeoAnimatable$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/$MolangParser" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$LazyVariable, $LazyVariable$Type} from "packages/software/bernie/geckolib/core/molang/$LazyVariable"
import {$MathBuilder, $MathBuilder$Type} from "packages/com/eliotlash/mclib/math/$MathBuilder"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Variable, $Variable$Type} from "packages/com/eliotlash/mclib/math/$Variable"
import {$MolangCompoundValue, $MolangCompoundValue$Type} from "packages/software/bernie/geckolib/core/molang/expressions/$MolangCompoundValue"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"
import {$MolangVariableHolder, $MolangVariableHolder$Type} from "packages/software/bernie/geckolib/core/molang/expressions/$MolangVariableHolder"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$MolangValue, $MolangValue$Type} from "packages/software/bernie/geckolib/core/molang/expressions/$MolangValue"

export class $MolangParser extends $MathBuilder {
static readonly "VARIABLES": $Map<(string), ($LazyVariable)>
static readonly "ZERO": $MolangVariableHolder
static readonly "ONE": $MolangVariableHolder
static readonly "RETURN": string
static readonly "INSTANCE": $MolangParser
 "variables": $Map<(string), ($Variable)>
 "functions": $Map<(string), ($Class<(any)>)>


public "register"(arg0: $Variable$Type): void
public "setValue"(arg0: string, arg1: $DoubleSupplier$Type): void
public static "parseExpression"(arg0: string): $MolangValue
public "getVariable"(arg0: string, arg1: $MolangCompoundValue$Type): $LazyVariable
public "getVariable"(arg0: string): $LazyVariable
public static "parseJson"(arg0: $JsonElement$Type): $MolangValue
public "remap"(arg0: string, arg1: string): void
public "setMemoizedValue"(arg0: string, arg1: $DoubleSupplier$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MolangParser$Type = ($MolangParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MolangParser_ = $MolangParser$Type;
}}
declare module "packages/software/bernie/example/client/model/block/$FertilizerModel" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DefaultedBlockGeoModel, $DefaultedBlockGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedBlockGeoModel"
import {$FertilizerBlockEntity, $FertilizerBlockEntity$Type} from "packages/software/bernie/example/block/entity/$FertilizerBlockEntity"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $FertilizerModel extends $DefaultedBlockGeoModel<($FertilizerBlockEntity)> {

constructor()

public "getRenderType"(arg0: $FertilizerBlockEntity$Type, arg1: $ResourceLocation$Type): $RenderType
public "getTextureResource"(arg0: $FertilizerBlockEntity$Type): $ResourceLocation
public "getModelResource"(arg0: $FertilizerBlockEntity$Type): $ResourceLocation
public "getAnimationResource"(arg0: $FertilizerBlockEntity$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FertilizerModel$Type = ($FertilizerModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FertilizerModel_ = $FertilizerModel$Type;
}}
declare module "packages/software/bernie/example/entity/$ReplacedCreeperEntity" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$GeoReplacedEntity, $GeoReplacedEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoReplacedEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ReplacedCreeperEntity implements $GeoReplacedEntity {

constructor()

public "getReplacingEntityType"(): $EntityType<(any)>
public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $Entity$Type, arg1: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $Entity$Type, arg1: $SerializableDataTicket$Type<(D)>, arg2: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: $Entity$Type, arg1: string, arg2: string): void
public "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
public static "registerSyncedAnimatable"(arg0: $GeoAnimatable$Type): void
public "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
public "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
public "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
public "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
get "replacingEntityType"(): $EntityType<(any)>
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplacedCreeperEntity$Type = ($ReplacedCreeperEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplacedCreeperEntity_ = $ReplacedCreeperEntity$Type;
}}
declare module "packages/software/bernie/example/client/model/entity/$FakeGlassModel" {
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$FakeGlassEntity, $FakeGlassEntity$Type} from "packages/software/bernie/example/entity/$FakeGlassEntity"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $FakeGlassModel extends $DefaultedEntityGeoModel<($FakeGlassEntity)> {

constructor()

public "getRenderType"(arg0: $FakeGlassEntity$Type, arg1: $ResourceLocation$Type): $RenderType
public "getTextureResource"(arg0: $FakeGlassEntity$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FakeGlassModel$Type = ($FakeGlassModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FakeGlassModel_ = $FakeGlassModel$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoBlockRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $GeoBlockRenderer<T extends ($BlockEntity) & ($GeoAnimatable)> implements $GeoRenderer<(T)>, $BlockEntityRenderer<(T)> {

constructor(arg0: $GeoModel$Type<(T)>)

public "render"(arg0: $BlockEntity$Type, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "updateAnimatedTextureFrame"(arg0: T): void
public "fireCompileRenderLayersEvent"(): void
public "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
public "getInstanceId"(arg0: T): long
public "getGeoModel"(): $GeoModel<(T)>
public "addRenderLayer"(arg0: $GeoRenderLayer$Type<(T)>): $GeoBlockRenderer<(T)>
public "withScale"(arg0: float): $GeoBlockRenderer<(T)>
public "withScale"(arg0: float, arg1: float): $GeoBlockRenderer<(T)>
public "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
public "getMotionAnimThreshold"(arg0: T): float
public "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
public "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
public "getTextureLocation"(arg0: T): $ResourceLocation
public "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
/**
 * 
 * @deprecated
 */
public "getPackedOverlay"(arg0: T, arg1: float): integer
public "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "shouldRender"(arg0: T, arg1: $Vec3$Type): boolean
public "shouldRenderOffScreen"(arg0: T): boolean
public "getViewDistance"(): integer
get "geoModel"(): $GeoModel<(T)>
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
get "viewDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoBlockRenderer$Type<T> = ($GeoBlockRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoBlockRenderer_<T> = $GeoBlockRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/$FormatVersion" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FormatVersion extends $Enum<($FormatVersion)> {
static readonly "V_1_12_0": $FormatVersion
static readonly "V_1_14_0": $FormatVersion


public static "values"(): ($FormatVersion)[]
public static "valueOf"(arg0: string): $FormatVersion
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FormatVersion$Type = (("v_1_12_0") | ("v_1_14_0")) | ($FormatVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FormatVersion_ = $FormatVersion$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/$CustomInstructionKeyframeEvent" {
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$CustomInstructionKeyframeData, $CustomInstructionKeyframeData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$CustomInstructionKeyframeData"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$KeyFrameEvent, $KeyFrameEvent$Type} from "packages/software/bernie/geckolib/core/keyframe/event/$KeyFrameEvent"

export class $CustomInstructionKeyframeEvent<T extends $GeoAnimatable> extends $KeyFrameEvent<(T), ($CustomInstructionKeyframeData)> {

constructor(arg0: T, arg1: double, arg2: $AnimationController$Type<(T)>, arg3: $CustomInstructionKeyframeData$Type)

public "getKeyframeData"(): $CustomInstructionKeyframeData
get "keyframeData"(): $CustomInstructionKeyframeData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomInstructionKeyframeEvent$Type<T> = ($CustomInstructionKeyframeEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomInstructionKeyframeEvent_<T> = $CustomInstructionKeyframeEvent$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/object/$Color" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $Color extends $Record {
static readonly "WHITE": $Color
static readonly "LIGHT_GRAY": $Color
static readonly "GRAY": $Color
static readonly "DARK_GRAY": $Color
static readonly "BLACK": $Color
static readonly "RED": $Color
static readonly "PINK": $Color
static readonly "ORANGE": $Color
static readonly "YELLOW": $Color
static readonly "GREEN": $Color
static readonly "MAGENTA": $Color
static readonly "CYAN": $Color
static readonly "BLUE": $Color

constructor(argbInt: integer)

public static "HSBtoARGB"(arg0: float, arg1: float, arg2: float): integer
public "argbInt"(): integer
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getGreenFloat"(): float
public static "ofRGBA"(arg0: float, arg1: float, arg2: float, arg3: float): $Color
public static "ofRGBA"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Color
public "getRedFloat"(): float
public static "ofOpaque"(arg0: integer): $Color
public static "ofRGB"(arg0: integer, arg1: integer, arg2: integer): $Color
public static "ofRGB"(arg0: float, arg1: float, arg2: float): $Color
public static "ofHSB"(arg0: float, arg1: float, arg2: float): $Color
public "getRed"(): integer
public "getGreen"(): integer
public "getBlue"(): integer
public "getAlpha"(): integer
public "getColor"(): integer
public "brighter"(arg0: double): $Color
public "darker"(arg0: float): $Color
public "getAlphaFloat"(): float
public "getBlueFloat"(): float
get "greenFloat"(): float
get "redFloat"(): float
get "red"(): integer
get "green"(): integer
get "blue"(): integer
get "alpha"(): integer
get "color"(): integer
get "alphaFloat"(): float
get "blueFloat"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Color$Type = ($Color);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Color_ = $Color$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoObjectRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"

export class $GeoObjectRenderer<T extends $GeoAnimatable> implements $GeoRenderer<(T)> {

constructor(arg0: $GeoModel$Type<(T)>)

public "render"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: integer): void
public "updateAnimatedTextureFrame"(arg0: T): void
public "fireCompileRenderLayersEvent"(): void
public "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
public "getTextureLocation"(arg0: T): $ResourceLocation
public "getAnimatable"(): T
public "getGeoModel"(): $GeoModel<(T)>
public "addRenderLayer"(arg0: $GeoRenderLayer$Type<(T)>): $GeoObjectRenderer<(T)>
public "withScale"(arg0: float): $GeoObjectRenderer<(T)>
public "withScale"(arg0: float, arg1: float): $GeoObjectRenderer<(T)>
public "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
public "getMotionAnimThreshold"(arg0: T): float
public "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
public "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
public "getInstanceId"(arg0: T): long
public "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
/**
 * 
 * @deprecated
 */
public "getPackedOverlay"(arg0: T, arg1: float): integer
public "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
get "animatable"(): T
get "geoModel"(): $GeoModel<(T)>
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoObjectRenderer$Type<T> = ($GeoObjectRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoObjectRenderer_<T> = $GeoObjectRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/example/$CommonListener" {
import {$EntityAttributeCreationEvent, $EntityAttributeCreationEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityAttributeCreationEvent"

export class $CommonListener {

constructor()

public static "registerEntityAttributes"(arg0: $EntityAttributeCreationEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonListener$Type = ($CommonListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonListener_ = $CommonListener$Type;
}}
declare module "packages/software/bernie/example/client/model/entity/$RaceCarModel" {
import {$RaceCarEntity, $RaceCarEntity$Type} from "packages/software/bernie/example/entity/$RaceCarEntity"
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $RaceCarModel extends $DefaultedEntityGeoModel<($RaceCarEntity)> {

constructor()

public "getRenderType"(arg0: $RaceCarEntity$Type, arg1: $ResourceLocation$Type): $RenderType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RaceCarModel$Type = ($RaceCarModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RaceCarModel_ = $RaceCarModel$Type;
}}
declare module "packages/software/bernie/example/client/renderer/armor/$GeckoArmorRenderer" {
import {$GeoArmorRenderer, $GeoArmorRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoArmorRenderer"
import {$HumanoidModel$ArmPose, $HumanoidModel$ArmPose$Type} from "packages/net/minecraft/client/model/$HumanoidModel$ArmPose"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$GeckoArmorItem, $GeckoArmorItem$Type} from "packages/software/bernie/example/item/$GeckoArmorItem"

export class $GeckoArmorRenderer extends $GeoArmorRenderer<($GeckoArmorItem)> {
static readonly "OVERLAY_SCALE": float
static readonly "HAT_OVERLAY_SCALE": float
static readonly "LEGGINGS_OVERLAY_SCALE": float
static readonly "TOOT_HORN_XROT_BASE": float
static readonly "TOOT_HORN_YROT_BASE": float
readonly "head": $ModelPart
readonly "hat": $ModelPart
readonly "body": $ModelPart
readonly "rightArm": $ModelPart
readonly "leftArm": $ModelPart
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

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoArmorRenderer$Type = ($GeckoArmorRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoArmorRenderer_ = $GeckoArmorRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$AutoGlowingGeoLayer" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $AutoGlowingGeoLayer<T extends $GeoAnimatable> extends $GeoRenderLayer<(T)> {

constructor(arg0: $GeoRenderer$Type<(T)>)

public "render"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoGlowingGeoLayer$Type<T> = ($AutoGlowingGeoLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoGlowingGeoLayer_<T> = $AutoGlowingGeoLayer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$BoneAnimation" {
import {$KeyframeStack, $KeyframeStack$Type} from "packages/software/bernie/geckolib/core/keyframe/$KeyframeStack"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Keyframe, $Keyframe$Type} from "packages/software/bernie/geckolib/core/keyframe/$Keyframe"
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $BoneAnimation extends $Record {

constructor(boneName: string, rotationKeyFrames: $KeyframeStack$Type<($Keyframe$Type<($IValue$Type)>)>, positionKeyFrames: $KeyframeStack$Type<($Keyframe$Type<($IValue$Type)>)>, scaleKeyFrames: $KeyframeStack$Type<($Keyframe$Type<($IValue$Type)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "boneName"(): string
public "scaleKeyFrames"(): $KeyframeStack<($Keyframe<($IValue)>)>
public "rotationKeyFrames"(): $KeyframeStack<($Keyframe<($IValue)>)>
public "positionKeyFrames"(): $KeyframeStack<($Keyframe<($IValue)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoneAnimation$Type = ($BoneAnimation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoneAnimation_ = $BoneAnimation$Type;
}}
declare module "packages/software/bernie/geckolib/network/packet/$AnimTriggerPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $AnimTriggerPacket<D> {

constructor(arg0: string, arg1: long, arg2: string, arg3: string)

public static "decode"<D>(arg0: $FriendlyByteBuf$Type): $AnimTriggerPacket<(D)>
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "receivePacket"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimTriggerPacket$Type<D> = ($AnimTriggerPacket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimTriggerPacket_<D> = $AnimTriggerPacket$Type<(D)>;
}}
declare module "packages/software/bernie/example/entity/$FakeGlassEntity" {
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$PathfinderMob, $PathfinderMob$Type} from "packages/net/minecraft/world/entity/$PathfinderMob"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $FakeGlassEntity extends $PathfinderMob implements $GeoEntity {
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FakeGlassEntity$Type = ($FakeGlassEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FakeGlassEntity_ = $FakeGlassEntity$Type;
}}
declare module "packages/software/bernie/example/client/model/entity/$BatModel" {
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$BatEntity, $BatEntity$Type} from "packages/software/bernie/example/entity/$BatEntity"

export class $BatModel extends $DefaultedEntityGeoModel<($BatEntity)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatModel$Type = ($BatModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatModel_ = $BatModel$Type;
}}
declare module "packages/software/bernie/example/registry/$BlockRegistry" {
import {$GeckoHabitatBlock, $GeckoHabitatBlock$Type} from "packages/software/bernie/example/block/$GeckoHabitatBlock"
import {$FertilizerBlock, $FertilizerBlock$Type} from "packages/software/bernie/example/block/$FertilizerBlock"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $BlockRegistry {
static readonly "BLOCKS": $DeferredRegister<($Block)>
static readonly "GECKO_HABITAT": $RegistryObject<($GeckoHabitatBlock)>
static readonly "FERTILIZER": $RegistryObject<($FertilizerBlock)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRegistry$Type = ($BlockRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRegistry_ = $BlockRegistry$Type;
}}
declare module "packages/software/bernie/geckolib/util/$JsonUtil" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonDeserializationContext, $JsonDeserializationContext$Type} from "packages/com/google/gson/$JsonDeserializationContext"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JsonUtil {
static readonly "GEO_GSON": $Gson

constructor()

public static "getOptionalLong"(arg0: $JsonObject$Type, arg1: string): long
public static "jsonArrayToList"<T>(arg0: $JsonArray$Type, arg1: $Function$Type<($JsonElement$Type), (T)>): $List<(T)>
public static "jsonObjToMap"<T>(arg0: $JsonObject$Type, arg1: $JsonDeserializationContext$Type, arg2: $Class$Type<(T)>): $Map<(string), (T)>
public static "getOptionalDouble"(arg0: $JsonObject$Type, arg1: string): double
public static "getOptionalInteger"(arg0: $JsonObject$Type, arg1: string): integer
public static "getOptionalBoolean"(arg0: $JsonObject$Type, arg1: string): boolean
public static "getOptionalFloat"(arg0: $JsonObject$Type, arg1: string): float
public static "jsonArrayToDoubleArray"(arg0: $JsonArray$Type): (double)[]
public static "jsonArrayToObjectArray"<T>(arg0: $JsonArray$Type, arg1: $JsonDeserializationContext$Type, arg2: $Class$Type<(T)>): (T)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonUtil$Type = ($JsonUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonUtil_ = $JsonUtil$Type;
}}
declare module "packages/software/bernie/geckolib/resource/$GeoGlowingTextureMeta" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$MetadataSectionSerializer, $MetadataSectionSerializer$Type} from "packages/net/minecraft/server/packs/metadata/$MetadataSectionSerializer"
import {$GeoGlowingTextureMeta$Pixel, $GeoGlowingTextureMeta$Pixel$Type} from "packages/software/bernie/geckolib/resource/$GeoGlowingTextureMeta$Pixel"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"

export class $GeoGlowingTextureMeta {
static readonly "DESERIALIZER": $MetadataSectionSerializer<($GeoGlowingTextureMeta)>

constructor(arg0: $List$Type<($GeoGlowingTextureMeta$Pixel$Type)>)

public "createImageMask"(arg0: $NativeImage$Type, arg1: $NativeImage$Type): void
public static "fromExistingImage"(arg0: $NativeImage$Type): $GeoGlowingTextureMeta
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoGlowingTextureMeta$Type = ($GeoGlowingTextureMeta);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoGlowingTextureMeta_ = $GeoGlowingTextureMeta$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationController$SoundKeyframeHandler" {
import {$SoundKeyframeEvent, $SoundKeyframeEvent$Type} from "packages/software/bernie/geckolib/core/keyframe/event/$SoundKeyframeEvent"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"

export interface $AnimationController$SoundKeyframeHandler<A extends $GeoAnimatable> {

 "handle"(arg0: $SoundKeyframeEvent$Type<(A)>): void

(arg0: $SoundKeyframeEvent$Type<(A)>): void
}

export namespace $AnimationController$SoundKeyframeHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationController$SoundKeyframeHandler$Type<A> = ($AnimationController$SoundKeyframeHandler<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationController$SoundKeyframeHandler_<A> = $AnimationController$SoundKeyframeHandler$Type<(A)>;
}}
declare module "packages/software/bernie/example/$ClientListener" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$EntityRenderersEvent$RegisterRenderers, $EntityRenderersEvent$RegisterRenderers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterRenderers"

export class $ClientListener {

constructor()

public static "registerRenderers"(arg0: $FMLClientSetupEvent$Type): void
public static "registerRenderers"(arg0: $EntityRenderersEvent$RegisterRenderers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientListener$Type = ($ClientListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientListener_ = $ClientListener$Type;
}}
declare module "packages/software/bernie/geckolib/model/$DefaultedGeoModel" {
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DefaultedGeoModel<T extends $GeoAnimatable> extends $GeoModel<(T)> {

constructor(arg0: $ResourceLocation$Type)

public "getTextureResource"(arg0: T): $ResourceLocation
public "withAltModel"(arg0: $ResourceLocation$Type): $DefaultedGeoModel<(T)>
public "withAltTexture"(arg0: $ResourceLocation$Type): $DefaultedGeoModel<(T)>
public "withAltAnimations"(arg0: $ResourceLocation$Type): $DefaultedGeoModel<(T)>
public "getModelResource"(arg0: T): $ResourceLocation
public "getAnimationResource"(arg0: T): $ResourceLocation
public "buildFormattedModelPath"(arg0: $ResourceLocation$Type): $ResourceLocation
public "buildFormattedTexturePath"(arg0: $ResourceLocation$Type): $ResourceLocation
public "buildFormattedAnimationPath"(arg0: $ResourceLocation$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedGeoModel$Type<T> = ($DefaultedGeoModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedGeoModel_<T> = $DefaultedGeoModel$Type<(T)>;
}}
declare module "packages/software/bernie/example/client/model/entity/$ReplacedCreeperModel" {
import {$DefaultedEntityGeoModel, $DefaultedEntityGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel"
import {$ReplacedCreeperEntity, $ReplacedCreeperEntity$Type} from "packages/software/bernie/example/entity/$ReplacedCreeperEntity"

export class $ReplacedCreeperModel extends $DefaultedEntityGeoModel<($ReplacedCreeperEntity)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplacedCreeperModel$Type = ($ReplacedCreeperModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplacedCreeperModel_ = $ReplacedCreeperModel$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$UVFaces" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$FaceUV, $FaceUV$Type} from "packages/software/bernie/geckolib/loading/json/raw/$FaceUV"

export class $UVFaces extends $Record {

constructor(north: $FaceUV$Type, south: $FaceUV$Type, east: $FaceUV$Type, west: $FaceUV$Type, up: $FaceUV$Type, down: $FaceUV$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "up"(): $FaceUV
public "down"(): $FaceUV
public "fromDirection"(arg0: $Direction$Type): $FaceUV
public static "deserializer"(): $JsonDeserializer<($UVFaces)>
public "east"(): $FaceUV
public "north"(): $FaceUV
public "west"(): $FaceUV
public "south"(): $FaceUV
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UVFaces$Type = ($UVFaces);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UVFaces_ = $UVFaces$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/typeadapter/$BakedAnimationsAdapter" {
import {$BakedAnimations, $BakedAnimations$Type} from "packages/software/bernie/geckolib/loading/object/$BakedAnimations"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"

export class $BakedAnimationsAdapter implements $JsonDeserializer<($BakedAnimations)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedAnimationsAdapter$Type = ($BakedAnimationsAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedAnimationsAdapter_ = $BakedAnimationsAdapter$Type;
}}
declare module "packages/software/bernie/example/block/$GeckoHabitatBlock" {
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BaseEntityBlock, $BaseEntityBlock$Type} from "packages/net/minecraft/world/level/block/$BaseEntityBlock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$RenderShape, $RenderShape$Type} from "packages/net/minecraft/world/level/block/$RenderShape"

export class $GeckoHabitatBlock extends $BaseEntityBlock implements $EntityBlock {
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

constructor()

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "getRenderShape"(arg0: $BlockState$Type): $RenderShape
public "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoHabitatBlock$Type = ($GeckoHabitatBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoHabitatBlock_ = $GeckoHabitatBlock$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/$MolangException" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $MolangException extends $Exception {

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MolangException$Type = ($MolangException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MolangException_ = $MolangException$Type;
}}
declare module "packages/software/bernie/geckolib/network/packet/$AnimDataSyncPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $AnimDataSyncPacket<D> {

constructor(arg0: string, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D)

public static "decode"<D>(arg0: $FriendlyByteBuf$Type): $AnimDataSyncPacket<(D)>
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "receivePacket"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimDataSyncPacket$Type<D> = ($AnimDataSyncPacket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimDataSyncPacket_<D> = $AnimDataSyncPacket$Type<(D)>;
}}
declare module "packages/software/bernie/geckolib/network/$SerializableDataTicket" {
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SerializableDataTicket<D> extends $DataTicket<(D)> {

constructor(arg0: string, arg1: $Class$Type<(any)>)

public "decode"(arg0: $FriendlyByteBuf$Type): D
public "encode"(arg0: D, arg1: $FriendlyByteBuf$Type): void
public static "ofString"(arg0: $ResourceLocation$Type): $SerializableDataTicket<(string)>
public static "ofInt"(arg0: $ResourceLocation$Type): $SerializableDataTicket<(integer)>
public static "ofDouble"(arg0: $ResourceLocation$Type): $SerializableDataTicket<(double)>
public static "ofBoolean"(arg0: $ResourceLocation$Type): $SerializableDataTicket<(boolean)>
public static "ofFloat"(arg0: $ResourceLocation$Type): $SerializableDataTicket<(float)>
public static "ofEnum"<E extends $Enum<(E)>>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(E)>): $SerializableDataTicket<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializableDataTicket$Type<D> = ($SerializableDataTicket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializableDataTicket_<D> = $SerializableDataTicket$Type<(D)>;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$Bone" {
import {$PolyMesh, $PolyMesh$Type} from "packages/software/bernie/geckolib/loading/json/raw/$PolyMesh"
import {$TextureMesh, $TextureMesh$Type} from "packages/software/bernie/geckolib/loading/json/raw/$TextureMesh"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Cube, $Cube$Type} from "packages/software/bernie/geckolib/loading/json/raw/$Cube"
import {$LocatorValue, $LocatorValue$Type} from "packages/software/bernie/geckolib/loading/json/raw/$LocatorValue"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Bone extends $Record {

constructor(bindPoseRotation: (double)[], cubes: ($Cube$Type)[], debug: boolean, inflate: double, locators: $Map$Type<(string), ($LocatorValue$Type)>, mirror: boolean, name: string, neverRender: boolean, parent: string, pivot: (double)[], polyMesh: $PolyMesh$Type, renderGroupId: long, reset: boolean, rotation: (double)[], textureMeshes: ($TextureMesh$Type)[])

public "name"(): string
public "parent"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "inflate"(): double
public "debug"(): boolean
public "reset"(): boolean
public "pivot"(): (double)[]
public "cubes"(): ($Cube)[]
public static "deserializer"(): $JsonDeserializer<($Bone)>
public "rotation"(): (double)[]
public "mirror"(): boolean
public "neverRender"(): boolean
public "bindPoseRotation"(): (double)[]
public "renderGroupId"(): long
public "textureMeshes"(): ($TextureMesh)[]
public "polyMesh"(): $PolyMesh
public "locators"(): $Map<(string), ($LocatorValue)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bone$Type = ($Bone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bone_ = $Bone$Type;
}}
declare module "packages/software/bernie/geckolib/core/molang/expressions/$MolangCompoundValue" {
import {$LazyVariable, $LazyVariable$Type} from "packages/software/bernie/geckolib/core/molang/$LazyVariable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MolangValue, $MolangValue$Type} from "packages/software/bernie/geckolib/core/molang/expressions/$MolangValue"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MolangCompoundValue extends $MolangValue {
readonly "values": $List<($MolangValue)>
readonly "locals": $Map<(string), ($LazyVariable)>

constructor(arg0: $MolangValue$Type)

public "get"(): double
public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MolangCompoundValue$Type = ($MolangCompoundValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MolangCompoundValue_ = $MolangCompoundValue$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$CoolKidRenderer" {
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$CoolKidEntity, $CoolKidEntity$Type} from "packages/software/bernie/example/entity/$CoolKidEntity"

export class $CoolKidRenderer extends $GeoEntityRenderer<($CoolKidEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoolKidRenderer$Type = ($CoolKidRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoolKidRenderer_ = $CoolKidRenderer$Type;
}}
declare module "packages/software/bernie/example/client/renderer/entity/$BatRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BatEntity, $BatEntity$Type} from "packages/software/bernie/example/entity/$BatEntity"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $BatRenderer extends $GeoEntityRenderer<($BatEntity)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type)

public "renderFinal"(arg0: $PoseStack$Type, arg1: $BatEntity$Type, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatRenderer$Type = ($BatRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatRenderer_ = $BatRenderer$Type;
}}
declare module "packages/software/bernie/example/item/$JackInTheBoxItem" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$GeoItem, $GeoItem$Type} from "packages/software/bernie/geckolib/animatable/$GeoItem"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $JackInTheBoxItem extends $Item implements $GeoItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "initializeClient"(arg0: $Consumer$Type<($IClientItemExtensions$Type)>): void
public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public static "getId"(arg0: $ItemStack$Type): long
public static "registerSyncedAnimatable"(arg0: $GeoAnimatable$Type): void
public static "getOrAssignId"(arg0: $ItemStack$Type, arg1: $ServerLevel$Type): long
public "isPerspectiveAware"(): boolean
public "getTick"(arg0: any): double
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
public "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
public "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
public "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
public "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "perspectiveAware"(): boolean
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JackInTheBoxItem$Type = ($JackInTheBoxItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JackInTheBoxItem_ = $JackInTheBoxItem$Type;
}}
declare module "packages/software/bernie/example/block/entity/$GeckoHabitatBlockEntity" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoBlockEntity, $GeoBlockEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $GeckoHabitatBlockEntity extends $BlockEntity implements $GeoBlockEntity {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoHabitatBlockEntity$Type = ($GeckoHabitatBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoHabitatBlockEntity_ = $GeckoHabitatBlockEntity$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationController$CustomKeyframeHandler" {
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$CustomInstructionKeyframeEvent, $CustomInstructionKeyframeEvent$Type} from "packages/software/bernie/geckolib/core/keyframe/event/$CustomInstructionKeyframeEvent"

export interface $AnimationController$CustomKeyframeHandler<A extends $GeoAnimatable> {

 "handle"(arg0: $CustomInstructionKeyframeEvent$Type<(A)>): void

(arg0: $CustomInstructionKeyframeEvent$Type<(A)>): void
}

export namespace $AnimationController$CustomKeyframeHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationController$CustomKeyframeHandler$Type<A> = ($AnimationController$CustomKeyframeHandler<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationController$CustomKeyframeHandler_<A> = $AnimationController$CustomKeyframeHandler$Type<(A)>;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$BoneAnimationQueue" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Keyframe, $Keyframe$Type} from "packages/software/bernie/geckolib/core/keyframe/$Keyframe"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"
import {$BoneSnapshot, $BoneSnapshot$Type} from "packages/software/bernie/geckolib/core/state/$BoneSnapshot"
import {$AnimationPointQueue, $AnimationPointQueue$Type} from "packages/software/bernie/geckolib/core/keyframe/$AnimationPointQueue"
import {$AnimationPoint, $AnimationPoint$Type} from "packages/software/bernie/geckolib/core/keyframe/$AnimationPoint"

export class $BoneAnimationQueue extends $Record {

constructor(arg0: $CoreGeoBone$Type)
constructor(bone: $CoreGeoBone$Type, rotationXQueue: $AnimationPointQueue$Type, rotationYQueue: $AnimationPointQueue$Type, rotationZQueue: $AnimationPointQueue$Type, positionXQueue: $AnimationPointQueue$Type, positionYQueue: $AnimationPointQueue$Type, positionZQueue: $AnimationPointQueue$Type, scaleXQueue: $AnimationPointQueue$Type, scaleYQueue: $AnimationPointQueue$Type, scaleZQueue: $AnimationPointQueue$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "addPositions"(arg0: $AnimationPoint$Type, arg1: $AnimationPoint$Type, arg2: $AnimationPoint$Type): void
public "addScales"(arg0: $AnimationPoint$Type, arg1: $AnimationPoint$Type, arg2: $AnimationPoint$Type): void
public "addRotations"(arg0: $AnimationPoint$Type, arg1: $AnimationPoint$Type, arg2: $AnimationPoint$Type): void
public "addNextScale"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: $BoneSnapshot$Type, arg4: $AnimationPoint$Type, arg5: $AnimationPoint$Type, arg6: $AnimationPoint$Type): void
public "addNextRotation"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: $BoneSnapshot$Type, arg4: $BoneSnapshot$Type, arg5: $AnimationPoint$Type, arg6: $AnimationPoint$Type, arg7: $AnimationPoint$Type): void
public "positionYQueue"(): $AnimationPointQueue
public "rotationXQueue"(): $AnimationPointQueue
public "scaleZQueue"(): $AnimationPointQueue
public "rotationZQueue"(): $AnimationPointQueue
public "positionXQueue"(): $AnimationPointQueue
public "rotationYQueue"(): $AnimationPointQueue
public "scaleYQueue"(): $AnimationPointQueue
public "positionZQueue"(): $AnimationPointQueue
public "scaleXQueue"(): $AnimationPointQueue
public "addNextPosition"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: $BoneSnapshot$Type, arg4: $AnimationPoint$Type, arg5: $AnimationPoint$Type, arg6: $AnimationPoint$Type): void
public "bone"(): $CoreGeoBone
public "addPosZPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addRotationYPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addPosYPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addScaleXPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addScaleYPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addScaleZPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addPosXPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addRotationZPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
public "addRotationXPoint"(arg0: $Keyframe$Type<(any)>, arg1: double, arg2: double, arg3: double, arg4: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoneAnimationQueue$Type = ($BoneAnimationQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoneAnimationQueue_ = $BoneAnimationQueue$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/$DynamicGeoEntityRenderer" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DynamicGeoEntityRenderer<T extends ($Entity) & ($GeoAnimatable)> extends $GeoEntityRenderer<(T)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type, arg1: $GeoModel$Type<(T)>)

public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicGeoEntityRenderer$Type<T> = ($DynamicGeoEntityRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicGeoEntityRenderer_<T> = $DynamicGeoEntityRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/loading/object/$BoneStructure" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Bone, $Bone$Type} from "packages/software/bernie/geckolib/loading/json/raw/$Bone"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BoneStructure extends $Record {

constructor(arg0: $Bone$Type)
constructor(self: $Bone$Type, children: $Map$Type<(string), ($BoneStructure$Type)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "self"(): $Bone
public "children"(): $Map<(string), ($BoneStructure)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoneStructure$Type = ($BoneStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoneStructure_ = $BoneStructure$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $GeoRenderLayer<T extends $GeoAnimatable> {

constructor(arg0: $GeoRenderer$Type<(T)>)

public "render"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "getDefaultBakedModel"(arg0: T): $BakedGeoModel
public "getGeoModel"(): $GeoModel<(T)>
public "renderForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "getRenderer"(): $GeoRenderer<(T)>
get "geoModel"(): $GeoModel<(T)>
get "renderer"(): $GeoRenderer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoRenderLayer$Type<T> = ($GeoRenderLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoRenderLayer_<T> = $GeoRenderLayer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar" {
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"

export class $AnimatableManager$ControllerRegistrar {

constructor()

public "add"(...arg0: ($AnimationController$Type<(any)>)[]): $AnimatableManager$ControllerRegistrar
public "remove"(arg0: string): $AnimatableManager$ControllerRegistrar
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatableManager$ControllerRegistrar$Type = ($AnimatableManager$ControllerRegistrar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatableManager$ControllerRegistrar_ = $AnimatableManager$ControllerRegistrar$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationProcessor$QueuedAnimation" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Animation$LoopType, $Animation$LoopType$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$LoopType"
import {$Animation, $Animation$Type} from "packages/software/bernie/geckolib/core/animation/$Animation"

export class $AnimationProcessor$QueuedAnimation extends $Record {

constructor(animation: $Animation$Type, loopType: $Animation$LoopType$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "loopType"(): $Animation$LoopType
public "animation"(): $Animation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationProcessor$QueuedAnimation$Type = ($AnimationProcessor$QueuedAnimation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationProcessor$QueuedAnimation_ = $AnimationProcessor$QueuedAnimation$Type;
}}
declare module "packages/software/bernie/example/entity/$CoolKidEntity" {
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$PathfinderMob, $PathfinderMob$Type} from "packages/net/minecraft/world/entity/$PathfinderMob"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CoolKidEntity extends $PathfinderMob implements $GeoEntity {
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoolKidEntity$Type = ($CoolKidEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoolKidEntity_ = $CoolKidEntity$Type;
}}
declare module "packages/software/bernie/geckolib/util/$RenderUtils" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$IntIntPair, $IntIntPair$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntIntPair"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $RenderUtils {

constructor()

public static "booleanToFloat"(arg0: boolean): float
public static "arrayToVec"(arg0: (double)[]): $Vec3
public static "getCurrentTick"(): double
public static "invertAndMultiplyMatrices"(arg0: $Matrix4f$Type, arg1: $Matrix4f$Type): $Matrix4f
public static "rotateMatrixAroundBone"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "fixInvertedFlatCube"(arg0: $GeoCube$Type, arg1: $Vector3f$Type): void
public static "translateMatrixToBone"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "translateToPivotPoint"(arg0: $PoseStack$Type, arg1: $GeoCube$Type): void
public static "translateToPivotPoint"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "rotateMatrixAroundCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type): void
public static "translateAwayFromPivotPoint"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "translateAwayFromPivotPoint"(arg0: $PoseStack$Type, arg1: $GeoCube$Type): void
public static "translateAndRotateMatrixForBone"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "scaleMatrixForBone"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "getDirectionAngle"(arg0: $Direction$Type): float
public static "translateMatrix"(arg0: $Matrix4f$Type, arg1: $Vector3f$Type): $Matrix4f
public static "prepMatrixForBone"(arg0: $PoseStack$Type, arg1: $CoreGeoBone$Type): void
public static "getGeoModelForItem"(arg0: $Item$Type): $GeoModel<(any)>
public static "faceRotation"(arg0: $PoseStack$Type, arg1: $Entity$Type, arg2: float): void
public static "getCurrentSystemTick"(): double
public static "getGeoModelForEntityType"(arg0: $EntityType$Type<(any)>): $GeoModel<(any)>
public static "getGeoModelForEntity"(arg0: $Entity$Type): $GeoModel<(any)>
public static "getGeoModelForBlock"(arg0: $BlockEntity$Type): $GeoModel<(any)>
public static "getGeoModelForArmor"(arg0: $ItemStack$Type): $GeoModel<(any)>
public static "getTextureDimensions"(arg0: $ResourceLocation$Type): $IntIntPair
public static "getReplacedAnimatable"(arg0: $EntityType$Type<(any)>): $GeoAnimatable
public static "matchModelPartRot"(arg0: $ModelPart$Type, arg1: $CoreGeoBone$Type): void
get "currentTick"(): double
get "currentSystemTick"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderUtils$Type = ($RenderUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderUtils_ = $RenderUtils$Type;
}}
declare module "packages/software/bernie/geckolib/network/$GeckoLibNetwork" {
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"

export class $GeckoLibNetwork {

constructor()

public static "init"(): void
public static "registerSyncedAnimatable"(arg0: $GeoAnimatable$Type): void
public static "getSyncedAnimatable"(arg0: string): $GeoAnimatable
public static "send"<M>(arg0: M, arg1: $PacketDistributor$PacketTarget$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLibNetwork$Type = ($GeckoLibNetwork);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLibNetwork_ = $GeckoLibNetwork$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$RawAnimation$Stage" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Animation$LoopType, $Animation$LoopType$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$LoopType"

export class $RawAnimation$Stage extends $Record {

constructor(arg0: string, arg1: $Animation$LoopType$Type)
constructor(animationName: string, loopType: $Animation$LoopType$Type, additionalTicks: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "loopType"(): $Animation$LoopType
public "additionalTicks"(): integer
public "animationName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RawAnimation$Stage$Type = ($RawAnimation$Stage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RawAnimation$Stage_ = $RawAnimation$Stage$Type;
}}
declare module "packages/software/bernie/geckolib/cache/object/$GeoBone" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$BoneSnapshot, $BoneSnapshot$Type} from "packages/software/bernie/geckolib/core/state/$BoneSnapshot"

export class $GeoBone implements $CoreGeoBone {

constructor(arg0: $GeoBone$Type, arg1: string, arg2: boolean, arg3: double, arg4: boolean, arg5: boolean)

public "getName"(): string
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "isHidden"(): boolean
public "setPosX"(arg0: float): void
public "setPosY"(arg0: float): void
public "getWorldPosition"(): $Vector3d
public "getPosY"(): float
public "getPosX"(): float
public "getPivotX"(): float
public "getPivotY"(): float
public "setPivotY"(arg0: float): void
public "setScaleX"(arg0: float): void
public "getScaleX"(): float
public "setScaleY"(arg0: float): void
public "setPivotX"(arg0: float): void
public "getScaleY"(): float
public "getInflate"(): double
public "setLocalSpaceMatrix"(arg0: $Matrix4f$Type): void
public "setWorldSpaceMatrix"(arg0: $Matrix4f$Type): void
public "setModelSpaceMatrix"(arg0: $Matrix4f$Type): void
public "setModelPosition"(arg0: $Vector3d$Type): void
public "getScaleVector"(): $Vector3d
public "getLocalPosition"(): $Vector3d
public "getReset"(): boolean
public "getPositionVector"(): $Vector3d
public "getModelPosition"(): $Vector3d
public "shouldNeverRender"(): boolean
public "getRotationVector"(): $Vector3d
public "saveInitialSnapshot"(): void
public "markPositionAsChanged"(): void
public "markRotationAsChanged"(): void
public "setTrackingMatrices"(arg0: boolean): void
public "addRotationOffsetFromBone"(arg0: $GeoBone$Type): void
public "getModelSpaceMatrix"(): $Matrix4f
public "setWorldSpaceNormal"(arg0: $Matrix3f$Type): void
public "getWorldSpaceMatrix"(): $Matrix4f
public "getModelRotationMatrix"(): $Matrix4f
public "getLocalSpaceMatrix"(): $Matrix4f
public "getWorldSpaceNormal"(): $Matrix3f
public "isTrackingMatrices"(): boolean
public "setPosZ"(arg0: float): void
public "setChildrenHidden"(arg0: boolean): void
public "markScaleAsChanged"(): void
public "getRotX"(): float
public "getScaleZ"(): float
public "setRotY"(arg0: float): void
public "getRotY"(): float
public "setScaleZ"(arg0: float): void
public "getRotZ"(): float
public "setPivotZ"(arg0: float): void
public "getPivotZ"(): float
public "setRotX"(arg0: float): void
public "setRotZ"(arg0: float): void
public "hasScaleChanged"(): boolean
public "hasRotationChanged"(): boolean
public "resetStateChanges"(): void
public "getInitialSnapshot"(): $BoneSnapshot
public "hasPositionChanged"(): boolean
public "setHidden"(arg0: boolean): void
public "getMirror"(): boolean
public "isHidingChildren"(): boolean
public "getCubes"(): $List<($GeoCube)>
public "getChildBones"(): $List<($GeoBone)>
public "getPosZ"(): float
public "updatePosition"(arg0: float, arg1: float, arg2: float): void
public "updateRotation"(arg0: float, arg1: float, arg2: float): void
public "updatePivot"(arg0: float, arg1: float, arg2: float): void
public "updateScale"(arg0: float, arg1: float, arg2: float): void
public "saveSnapshot"(): $BoneSnapshot
get "name"(): string
get "hidden"(): boolean
set "posX"(value: float)
set "posY"(value: float)
get "worldPosition"(): $Vector3d
get "posY"(): float
get "posX"(): float
get "pivotX"(): float
get "pivotY"(): float
set "pivotY"(value: float)
set "scaleX"(value: float)
get "scaleX"(): float
set "scaleY"(value: float)
set "pivotX"(value: float)
get "scaleY"(): float
get "inflate"(): double
set "localSpaceMatrix"(value: $Matrix4f$Type)
set "worldSpaceMatrix"(value: $Matrix4f$Type)
set "modelSpaceMatrix"(value: $Matrix4f$Type)
set "modelPosition"(value: $Vector3d$Type)
get "scaleVector"(): $Vector3d
get "localPosition"(): $Vector3d
get "reset"(): boolean
get "positionVector"(): $Vector3d
get "modelPosition"(): $Vector3d
get "rotationVector"(): $Vector3d
set "trackingMatrices"(value: boolean)
get "modelSpaceMatrix"(): $Matrix4f
set "worldSpaceNormal"(value: $Matrix3f$Type)
get "worldSpaceMatrix"(): $Matrix4f
get "modelRotationMatrix"(): $Matrix4f
get "localSpaceMatrix"(): $Matrix4f
get "worldSpaceNormal"(): $Matrix3f
get "trackingMatrices"(): boolean
set "posZ"(value: float)
set "childrenHidden"(value: boolean)
get "rotX"(): float
get "scaleZ"(): float
set "rotY"(value: float)
get "rotY"(): float
set "scaleZ"(value: float)
get "rotZ"(): float
set "pivotZ"(value: float)
get "pivotZ"(): float
set "rotX"(value: float)
set "rotZ"(value: float)
get "initialSnapshot"(): $BoneSnapshot
set "hidden"(value: boolean)
get "mirror"(): boolean
get "hidingChildren"(): boolean
get "cubes"(): $List<($GeoCube)>
get "childBones"(): $List<($GeoBone)>
get "posZ"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoBone$Type = ($GeoBone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoBone_ = $GeoBone$Type;
}}
declare module "packages/software/bernie/example/entity/$RaceCarEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity$MoveFunction, $Entity$MoveFunction$Type} from "packages/net/minecraft/world/entity/$Entity$MoveFunction"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Animal, $Animal$Type} from "packages/net/minecraft/world/entity/animal/$Animal"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$AgeableMob, $AgeableMob$Type} from "packages/net/minecraft/world/entity/$AgeableMob"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RaceCarEntity extends $Animal implements $GeoEntity {
static readonly "BABY_START_AGE": integer
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "travel"(arg0: $Vec3$Type): void
public "isControlledByLocalInstance"(): boolean
public "getControllingPassenger"(): $LivingEntity
public "mobInteract"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "getBreedOffspring"(arg0: $ServerLevel$Type, arg1: $AgeableMob$Type): $AgeableMob
public "m_19956_"(arg0: $Entity$Type, arg1: $Entity$MoveFunction$Type): void
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "controlledByLocalInstance"(): boolean
get "controllingPassenger"(): $LivingEntity
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RaceCarEntity$Type = ($RaceCarEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RaceCarEntity_ = $RaceCarEntity$Type;
}}
declare module "packages/software/bernie/geckolib/event/$GeoRenderEvent$Entity$Post" {
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$GeoRenderEvent$Entity, $GeoRenderEvent$Entity$Type} from "packages/software/bernie/geckolib/event/$GeoRenderEvent$Entity"

export class $GeoRenderEvent$Entity$Post extends $GeoRenderEvent$Entity {

constructor()
constructor(arg0: $GeoEntityRenderer$Type<(any)>, arg1: $PoseStack$Type, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: float, arg5: integer)

public "getBufferSource"(): $MultiBufferSource
public "getPartialTick"(): float
public "getPoseStack"(): $PoseStack
public "getModel"(): $BakedGeoModel
public "getPackedLight"(): integer
public "getListenerList"(): $ListenerList
get "bufferSource"(): $MultiBufferSource
get "partialTick"(): float
get "poseStack"(): $PoseStack
get "model"(): $BakedGeoModel
get "packedLight"(): integer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoRenderEvent$Entity$Post$Type = ($GeoRenderEvent$Entity$Post);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoRenderEvent$Entity$Post_ = $GeoRenderEvent$Entity$Post$Type;
}}
declare module "packages/software/bernie/geckolib/model/$DefaultedEntityGeoModel" {
import {$DefaultedGeoModel, $DefaultedGeoModel$Type} from "packages/software/bernie/geckolib/model/$DefaultedGeoModel"
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DefaultedEntityGeoModel<T extends $GeoAnimatable> extends $DefaultedGeoModel<(T)> {

constructor(arg0: $ResourceLocation$Type)
constructor(arg0: $ResourceLocation$Type, arg1: boolean)

public "setCustomAnimations"(arg0: T, arg1: long, arg2: $AnimationState$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultedEntityGeoModel$Type<T> = ($DefaultedEntityGeoModel<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultedEntityGeoModel_<T> = $DefaultedEntityGeoModel$Type<(T)>;
}}
declare module "packages/software/bernie/example/item/$GeckoHabitatItem" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$GeoItem, $GeoItem$Type} from "packages/software/bernie/geckolib/animatable/$GeoItem"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $GeckoHabitatItem extends $BlockItem implements $GeoItem {
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

constructor(arg0: $Block$Type, arg1: $Item$Properties$Type)

public "initializeClient"(arg0: $Consumer$Type<($IClientItemExtensions$Type)>): void
public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public static "getId"(arg0: $ItemStack$Type): long
public static "registerSyncedAnimatable"(arg0: $GeoAnimatable$Type): void
public static "getOrAssignId"(arg0: $ItemStack$Type, arg1: $ServerLevel$Type): long
public "isPerspectiveAware"(): boolean
public "getTick"(arg0: any): double
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
public "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
public "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
public "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
public "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "perspectiveAware"(): boolean
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoHabitatItem$Type = ($GeckoHabitatItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoHabitatItem_ = $GeckoHabitatItem$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$ContextAwareAnimatableManager" {
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$AnimatableManager, $AnimatableManager$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$BoneSnapshot, $BoneSnapshot$Type} from "packages/software/bernie/geckolib/core/state/$BoneSnapshot"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ContextAwareAnimatableManager<T extends $GeoAnimatable, C> extends $AnimatableManager<(T)> {

constructor(arg0: $GeoAnimatable$Type)

public "getData"<D>(arg0: $DataTicket$Type<(D)>): D
public "setData"<D>(arg0: $DataTicket$Type<(D)>, arg1: D): void
public "getCurrentContext"(): C
public "tryTriggerAnimation"(arg0: string): void
public "tryTriggerAnimation"(arg0: string, arg1: string): void
public "removeController"(arg0: string): void
public "getLastUpdateTime"(): double
public "startedAt"(arg0: double): void
public "isFirstTick"(): boolean
public "updatedAt"(arg0: double): void
public "getFirstTickTime"(): double
public "getAnimationControllers"(): $Map<(string), ($AnimationController<(T)>)>
public "getBoneSnapshotCollection"(): $Map<(string), ($BoneSnapshot)>
public "getManagerForContext"(arg0: C): $AnimatableManager<(T)>
public "addController"(arg0: $AnimationController$Type<(any)>): void
public "clearSnapshotCache"(): void
get "currentContext"(): C
get "lastUpdateTime"(): double
get "firstTick"(): boolean
get "firstTickTime"(): double
get "animationControllers"(): $Map<(string), ($AnimationController<(T)>)>
get "boneSnapshotCollection"(): $Map<(string), ($BoneSnapshot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextAwareAnimatableManager$Type<T, C> = ($ContextAwareAnimatableManager<(T), (C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextAwareAnimatableManager_<T, C> = $ContextAwareAnimatableManager$Type<(T), (C)>;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationController" {
import {$EasingType, $EasingType$Type} from "packages/software/bernie/geckolib/core/animation/$EasingType"
import {$AnimationController$State, $AnimationController$State$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController$State"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$AnimationController$CustomKeyframeHandler, $AnimationController$CustomKeyframeHandler$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController$CustomKeyframeHandler"
import {$BoneSnapshot, $BoneSnapshot$Type} from "packages/software/bernie/geckolib/core/state/$BoneSnapshot"
import {$CoreGeoModel, $CoreGeoModel$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoModel"
import {$RawAnimation, $RawAnimation$Type} from "packages/software/bernie/geckolib/core/animation/$RawAnimation"
import {$AnimationController$AnimationStateHandler, $AnimationController$AnimationStateHandler$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController$AnimationStateHandler"
import {$AnimationProcessor$QueuedAnimation, $AnimationProcessor$QueuedAnimation$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationProcessor$QueuedAnimation"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$AnimationController$ParticleKeyframeHandler, $AnimationController$ParticleKeyframeHandler$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController$ParticleKeyframeHandler"
import {$AnimationState, $AnimationState$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationState"
import {$AnimationController$SoundKeyframeHandler, $AnimationController$SoundKeyframeHandler$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController$SoundKeyframeHandler"
import {$CoreGeoBone, $CoreGeoBone$Type} from "packages/software/bernie/geckolib/core/animatable/model/$CoreGeoBone"
import {$BoneAnimationQueue, $BoneAnimationQueue$Type} from "packages/software/bernie/geckolib/core/keyframe/$BoneAnimationQueue"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnimationController<T extends $GeoAnimatable> {

constructor(arg0: T, arg1: $AnimationController$AnimationStateHandler$Type<(T)>)
constructor(arg0: T, arg1: string, arg2: integer, arg3: $AnimationController$AnimationStateHandler$Type<(T)>)
constructor(arg0: T, arg1: string, arg2: $AnimationController$AnimationStateHandler$Type<(T)>)
constructor(arg0: T, arg1: integer, arg2: $AnimationController$AnimationStateHandler$Type<(T)>)

public "getName"(): string
public "stop"(): void
public "process"(arg0: $CoreGeoModel$Type<(T)>, arg1: $AnimationState$Type<(T)>, arg2: $Map$Type<(string), ($CoreGeoBone$Type)>, arg3: $Map$Type<(string), ($BoneSnapshot$Type)>, arg4: double, arg5: boolean): void
public "getAnimationState"(): $AnimationController$State
public "transitionLength"(arg0: integer): $AnimationController<(T)>
public "setCustomInstructionKeyframeHandler"(arg0: $AnimationController$CustomKeyframeHandler$Type<(T)>): $AnimationController<(T)>
public "getAnimationSpeed"(): double
public "tryTriggerAnimation"(arg0: string): boolean
public "setAnimationSpeed"(arg0: double): $AnimationController<(T)>
public "triggerableAnim"(arg0: string, arg1: $RawAnimation$Type): $AnimationController<(T)>
public "forceAnimationReset"(): void
public "setAnimation"(arg0: $RawAnimation$Type): void
public "setAnimationSpeedHandler"(arg0: $Function$Type<(T), (double)>): $AnimationController<(T)>
public "setOverrideEasingTypeFunction"(arg0: $Function$Type<(T), ($EasingType$Type)>): $AnimationController<(T)>
public "getBoneAnimationQueues"(): $Map<(string), ($BoneAnimationQueue)>
public "receiveTriggeredAnimations"(): $AnimationController<(T)>
public "setSoundKeyframeHandler"(arg0: $AnimationController$SoundKeyframeHandler$Type<(T)>): $AnimationController<(T)>
/**
 * 
 * @deprecated
 */
public "setTransitionLength"(arg0: integer): void
public "setOverrideEasingType"(arg0: $EasingType$Type): $AnimationController<(T)>
public "setParticleKeyframeHandler"(arg0: $AnimationController$ParticleKeyframeHandler$Type<(T)>): $AnimationController<(T)>
public "getCurrentAnimation"(): $AnimationProcessor$QueuedAnimation
public "hasAnimationFinished"(): boolean
public "isPlayingTriggeredAnimation"(): boolean
public "getCurrentRawAnimation"(): $RawAnimation
get "name"(): string
get "animationState"(): $AnimationController$State
set "customInstructionKeyframeHandler"(value: $AnimationController$CustomKeyframeHandler$Type<(T)>)
get "animationSpeed"(): double
set "animationSpeed"(value: double)
set "animation"(value: $RawAnimation$Type)
set "animationSpeedHandler"(value: $Function$Type<(T), (double)>)
set "overrideEasingTypeFunction"(value: $Function$Type<(T), ($EasingType$Type)>)
get "boneAnimationQueues"(): $Map<(string), ($BoneAnimationQueue)>
set "soundKeyframeHandler"(value: $AnimationController$SoundKeyframeHandler$Type<(T)>)
set "overrideEasingType"(value: $EasingType$Type)
set "particleKeyframeHandler"(value: $AnimationController$ParticleKeyframeHandler$Type<(T)>)
get "currentAnimation"(): $AnimationProcessor$QueuedAnimation
get "playingTriggeredAnimation"(): boolean
get "currentRawAnimation"(): $RawAnimation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationController$Type<T> = ($AnimationController<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationController_<T> = $AnimationController$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Operation, $Operation$Type} from "packages/com/llamalad7/mixinextras/injector/wrapoperation/$Operation"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LocalFloatRef, $LocalFloatRef$Type} from "packages/com/llamalad7/mixinextras/sugar/ref/$LocalFloatRef"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$Color, $Color$Type} from "packages/software/bernie/geckolib/core/object/$Color"
import {$GeoModel, $GeoModel$Type} from "packages/software/bernie/geckolib/model/$GeoModel"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $GeoEntityRenderer<T extends ($Entity) & ($GeoAnimatable)> extends $EntityRenderer<(T)> implements $GeoRenderer<(T)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type, arg1: $GeoModel$Type<(T)>)

public "wrapOperation$dhc000$l2hostility$isInvisibleTo$geckoFix"(arg0: $Entity$Type, arg1: $Player$Type, arg2: $Operation$Type<(any)>, arg3: $LocalFloatRef$Type): boolean
public "getNameRenderCutoffDistance"(arg0: T): double
public "updateAnimatedTextureFrame"(arg0: T): void
public "fireCompileRenderLayersEvent"(): void
public "firePostRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): void
public "preRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "render"(arg0: T, arg1: float, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void
public "m_6512_"(arg0: T): boolean
public "getTextureLocation"(arg0: T): $ResourceLocation
public "firePreRenderEvent"(arg0: $PoseStack$Type, arg1: $BakedGeoModel$Type, arg2: $MultiBufferSource$Type, arg3: float, arg4: integer): boolean
public "getInstanceId"(arg0: T): long
public "getGeoModel"(): $GeoModel<(T)>
public "addRenderLayer"(arg0: $GeoRenderLayer$Type<(T)>): $GeoEntityRenderer<(T)>
public "withScale"(arg0: float): $GeoEntityRenderer<(T)>
public "withScale"(arg0: float, arg1: float): $GeoEntityRenderer<(T)>
public "applyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "actuallyRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderRecursively"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "isShaking"(arg0: T): boolean
public "renderLeash"<E extends $Entity, M extends $Mob>(arg0: M, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: E): void
public "renderFinal"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: integer, arg7: integer, arg8: float, arg9: float, arg10: float, arg11: float): void
public "getPackedOverlay"(arg0: T, arg1: float): integer
public "getPackedOverlay"(arg0: T, arg1: float, arg2: float): integer
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
public "getRenderType"(arg0: T, arg1: $ResourceLocation$Type, arg2: $MultiBufferSource$Type, arg3: float): $RenderType
public "getMotionAnimThreshold"(arg0: T): float
public "scaleModelForRender"(arg0: float, arg1: float, arg2: $PoseStack$Type, arg3: T, arg4: $BakedGeoModel$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer): void
public "preApplyRenderLayers"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "createVerticesOfQuad"(arg0: $GeoQuad$Type, arg1: $Matrix4f$Type, arg2: $Vector3f$Type, arg3: $VertexConsumer$Type, arg4: integer, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float): void
public "applyRenderLayersForBone"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
public "postRender"(arg0: $PoseStack$Type, arg1: T, arg2: $BakedGeoModel$Type, arg3: $MultiBufferSource$Type, arg4: $VertexConsumer$Type, arg5: boolean, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
public "getRenderColor"(arg0: T, arg1: float, arg2: integer): $Color
public "defaultRender"(arg0: $PoseStack$Type, arg1: T, arg2: $MultiBufferSource$Type, arg3: $RenderType$Type, arg4: $VertexConsumer$Type, arg5: float, arg6: float, arg7: integer): void
public "renderCubesOfBone"(arg0: $PoseStack$Type, arg1: $GeoBone$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "renderChildBones"(arg0: $PoseStack$Type, arg1: T, arg2: $GeoBone$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: boolean, arg7: float, arg8: integer, arg9: integer, arg10: float, arg11: float, arg12: float, arg13: float): void
public "renderCube"(arg0: $PoseStack$Type, arg1: $GeoCube$Type, arg2: $VertexConsumer$Type, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: float, arg8: float): void
public "reRender"(arg0: $BakedGeoModel$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: T, arg4: $RenderType$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer, arg9: float, arg10: float, arg11: float, arg12: float): void
get "geoModel"(): $GeoModel<(T)>
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoEntityRenderer$Type<T> = ($GeoEntityRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoEntityRenderer_<T> = $GeoEntityRenderer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/data/$CustomInstructionKeyframeData" {
import {$KeyFrameData, $KeyFrameData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$KeyFrameData"

export class $CustomInstructionKeyframeData extends $KeyFrameData {

constructor(arg0: double, arg1: string)

public "hashCode"(): integer
public "getInstructions"(): string
get "instructions"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomInstructionKeyframeData$Type = ($CustomInstructionKeyframeData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomInstructionKeyframeData_ = $CustomInstructionKeyframeData$Type;
}}
declare module "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayersContainer" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"

export class $GeoRenderLayersContainer<T extends $GeoAnimatable> {

constructor(arg0: $GeoRenderer$Type<(T)>)

public "addLayer"(arg0: $GeoRenderLayer$Type<(T)>): void
public "fireCompileRenderLayersEvent"(): void
public "getRenderLayers"(): $List<($GeoRenderLayer<(T)>)>
get "renderLayers"(): $List<($GeoRenderLayer<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoRenderLayersContainer$Type<T> = ($GeoRenderLayersContainer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoRenderLayersContainer_<T> = $GeoRenderLayersContainer$Type<(T)>;
}}
declare module "packages/software/bernie/geckolib/core/animation/$Animation" {
import {$BoneAnimation, $BoneAnimation$Type} from "packages/software/bernie/geckolib/core/keyframe/$BoneAnimation"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Animation$LoopType, $Animation$LoopType$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$LoopType"
import {$Animation$Keyframes, $Animation$Keyframes$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$Keyframes"

export class $Animation extends $Record {

constructor(name: string, length: double, loopType: $Animation$LoopType$Type, boneAnimations: ($BoneAnimation$Type)[], keyFrames: $Animation$Keyframes$Type)

public "name"(): string
public "equals"(arg0: any): boolean
public "length"(): double
public "toString"(): string
public "hashCode"(): integer
public "loopType"(): $Animation$LoopType
public "keyFrames"(): $Animation$Keyframes
public "boneAnimations"(): ($BoneAnimation)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Animation$Type = ($Animation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Animation_ = $Animation$Type;
}}
declare module "packages/software/bernie/example/entity/$BikeEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Animal, $Animal$Type} from "packages/net/minecraft/world/entity/animal/$Animal"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$AgeableMob, $AgeableMob$Type} from "packages/net/minecraft/world/entity/$AgeableMob"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BikeEntity extends $Animal implements $GeoEntity {
static readonly "BABY_START_AGE": integer
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "travel"(arg0: $Vec3$Type): void
public "isControlledByLocalInstance"(): boolean
public "getControllingPassenger"(): $LivingEntity
public "mobInteract"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "getBreedOffspring"(arg0: $ServerLevel$Type, arg1: $AgeableMob$Type): $AgeableMob
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "controlledByLocalInstance"(): boolean
get "controllingPassenger"(): $LivingEntity
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BikeEntity$Type = ($BikeEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BikeEntity_ = $BikeEntity$Type;
}}
declare module "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache" {
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$AnimatableManager, $AnimatableManager$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"

export class $AnimatableInstanceCache {

constructor(arg0: $GeoAnimatable$Type)

public "getManagerForId"<T extends $GeoAnimatable>(arg0: long): $AnimatableManager<(T)>
public "addDataPoint"<D>(arg0: long, arg1: $DataTicket$Type<(D)>, arg2: D): void
public "getDataPoint"<D>(arg0: long, arg1: $DataTicket$Type<(D)>): D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatableInstanceCache$Type = ($AnimatableInstanceCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatableInstanceCache_ = $AnimatableInstanceCache$Type;
}}
declare module "packages/software/bernie/geckolib/event/$GeoRenderEvent$Entity" {
import {$GeoEntityRenderer, $GeoEntityRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoEntityRenderer"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$GeoRenderEvent, $GeoRenderEvent$Type} from "packages/software/bernie/geckolib/event/$GeoRenderEvent"

export class $GeoRenderEvent$Entity extends $Event implements $GeoRenderEvent {

constructor()
constructor(arg0: $GeoEntityRenderer$Type<(any)>)

public "isCancelable"(): boolean
public "getEntity"(): $Entity
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "entity"(): $Entity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoRenderEvent$Entity$Type = ($GeoRenderEvent$Entity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoRenderEvent$Entity_ = $GeoRenderEvent$Entity$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$FaceUV" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $FaceUV extends $Record {

constructor(materialInstance: string, uv: (double)[], uvSize: (double)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "deserializer"(): $JsonDeserializer<($FaceUV)>
public "uv"(): (double)[]
public "uvSize"(): (double)[]
public "materialInstance"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FaceUV$Type = ($FaceUV);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FaceUV_ = $FaceUV$Type;
}}
declare module "packages/software/bernie/geckolib/event/$GeoRenderEvent" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"

export interface $GeoRenderEvent {

 "getRenderer"(): $GeoRenderer<(any)>

(): $GeoRenderer<(any)>
}

export namespace $GeoRenderEvent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoRenderEvent$Type = ($GeoRenderEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoRenderEvent_ = $GeoRenderEvent$Type;
}}
declare module "packages/software/bernie/example/entity/$BatEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$WalkAnimationState, $WalkAnimationState$Type} from "packages/net/minecraft/world/entity/$WalkAnimationState"
import {$PathNavigation, $PathNavigation$Type} from "packages/net/minecraft/world/entity/ai/navigation/$PathNavigation"
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$GeoEntity, $GeoEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoEntity"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$PathfinderMob, $PathfinderMob$Type} from "packages/net/minecraft/world/entity/$PathfinderMob"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$GoalSelector, $GoalSelector$Type} from "packages/net/minecraft/world/entity/ai/goal/$GoalSelector"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BatEntity extends $PathfinderMob implements $GeoEntity {
 "entityJs$builder": any
static readonly "MAX_WEARING_ARMOR_CHANCE": float
static readonly "MAX_PICKUP_LOOT_CHANCE": float
static readonly "MAX_ENCHANTED_ARMOR_CHANCE": float
static readonly "MAX_ENCHANTED_WEAPON_CHANCE": float
static readonly "LEASH_TAG": string
static readonly "DEFAULT_EQUIPMENT_DROP_CHANCE": float
static readonly "PRESERVE_ITEM_DROP_CHANCE": integer
static readonly "UPDATE_GOAL_SELECTOR_EVERY_N_TICKS": integer
 "ambientSoundTime": integer
 "navigation": $PathNavigation
readonly "goalSelector": $GoalSelector
readonly "targetSelector": $GoalSelector
 "leashInfoTag": $CompoundTag
static readonly "HAND_SLOTS": integer
static readonly "ARMOR_SLOTS": integer
static readonly "EQUIPMENT_SLOT_OFFSET": integer
static readonly "ARMOR_SLOT_OFFSET": integer
static readonly "SWING_DURATION": integer
static readonly "PLAYER_HURT_EXPERIENCE_TIME": integer
static readonly "MIN_MOVEMENT_DISTANCE": double
static readonly "DEFAULT_BASE_GRAVITY": double
static readonly "DEATH_DURATION": integer
static readonly "USE_ITEM_INTERVAL": integer
static readonly "EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT": float
 "attributes": $AttributeMap
readonly "activeEffects": $Map<($MobEffect), ($MobEffectInstance)>
 "swinging": boolean
 "swingingArm": $InteractionHand
 "swingTime": integer
 "removeArrowTime": integer
 "removeStingerTime": integer
 "hurtTime": integer
 "hurtDuration": integer
 "deathTime": integer
 "oAttackAnim": float
 "attackAnim": float
 "attackStrengthTicker": integer
readonly "walkAnimation": $WalkAnimationState
readonly "invulnerableDuration": integer
readonly "timeOffs": float
readonly "rotA": float
 "yBodyRot": float
 "yBodyRotO": float
 "yHeadRot": float
 "yHeadRotO": float
 "dead": boolean
 "jumping": boolean
 "xxa": float
 "yya": float
 "zza": float
 "effectsDirty": boolean
 "noJumpDelay": integer
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

constructor(arg0: $EntityType$Type<(any)>, arg1: $Level$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "interactAt"(arg0: $Player$Type, arg1: $Vec3$Type, arg2: $InteractionHand$Type): $InteractionResult
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
public "lithiumOnEquipmentChanged"(): void
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public "getCachedFeetBlockState"(): $BlockState
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
get "cachedFeetBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BatEntity$Type = ($BatEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BatEntity_ = $BatEntity$Type;
}}
declare module "packages/software/bernie/geckolib/animatable/$GeoReplacedEntity" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$SingletonGeoAnimatable, $SingletonGeoAnimatable$Type} from "packages/software/bernie/geckolib/animatable/$SingletonGeoAnimatable"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $GeoReplacedEntity extends $SingletonGeoAnimatable {

 "getReplacingEntityType"(): $EntityType<(any)>
 "getAnimData"<D>(arg0: $Entity$Type, arg1: $SerializableDataTicket$Type<(D)>): D
 "setAnimData"<D>(arg0: $Entity$Type, arg1: $SerializableDataTicket$Type<(D)>, arg2: D): void
 "getTick"(arg0: any): double
 "triggerAnim"(arg0: $Entity$Type, arg1: string, arg2: string): void
 "getAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>): D
 "syncAnimData"<D>(arg0: long, arg1: $SerializableDataTicket$Type<(D)>, arg2: D, arg3: $PacketDistributor$PacketTarget$Type): void
 "setAnimData"<D>(arg0: $Entity$Type, arg1: long, arg2: $SerializableDataTicket$Type<(D)>, arg3: D): void
 "triggerAnim"<D>(arg0: $Entity$Type, arg1: long, arg2: string, arg3: string): void
 "triggerAnim"<D>(arg0: long, arg1: string, arg2: string, arg3: $PacketDistributor$PacketTarget$Type): void
 "animatableCacheOverride"(): $AnimatableInstanceCache
 "getBoneResetTime"(): double
 "getAnimatableInstanceCache"(): $AnimatableInstanceCache
 "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
 "shouldPlayAnimsWhileGamePaused"(): boolean
}

export namespace $GeoReplacedEntity {
function registerSyncedAnimatable(arg0: $GeoAnimatable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoReplacedEntity$Type = ($GeoReplacedEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoReplacedEntity_ = $GeoReplacedEntity$Type;
}}
declare module "packages/software/bernie/geckolib/loading/object/$BakedModelFactory" {
import {$BoneStructure, $BoneStructure$Type} from "packages/software/bernie/geckolib/loading/object/$BoneStructure"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Cube, $Cube$Type} from "packages/software/bernie/geckolib/loading/json/raw/$Cube"
import {$GeoBone, $GeoBone$Type} from "packages/software/bernie/geckolib/cache/object/$GeoBone"
import {$BakedModelFactory$VertexSet, $BakedModelFactory$VertexSet$Type} from "packages/software/bernie/geckolib/loading/object/$BakedModelFactory$VertexSet"
import {$GeometryTree, $GeometryTree$Type} from "packages/software/bernie/geckolib/loading/object/$GeometryTree"
import {$ModelProperties, $ModelProperties$Type} from "packages/software/bernie/geckolib/loading/json/raw/$ModelProperties"
import {$UVUnion, $UVUnion$Type} from "packages/software/bernie/geckolib/loading/json/raw/$UVUnion"
import {$GeoQuad, $GeoQuad$Type} from "packages/software/bernie/geckolib/cache/object/$GeoQuad"
import {$GeoCube, $GeoCube$Type} from "packages/software/bernie/geckolib/cache/object/$GeoCube"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $BakedModelFactory {

 "constructBone"(arg0: $BoneStructure$Type, arg1: $ModelProperties$Type, arg2: $GeoBone$Type): $GeoBone
 "buildQuads"(arg0: $UVUnion$Type, arg1: $BakedModelFactory$VertexSet$Type, arg2: $Cube$Type, arg3: float, arg4: float, arg5: boolean): ($GeoQuad)[]
 "constructCube"(arg0: $Cube$Type, arg1: $ModelProperties$Type, arg2: $GeoBone$Type): $GeoCube
 "buildQuad"(arg0: $BakedModelFactory$VertexSet$Type, arg1: $Cube$Type, arg2: $UVUnion$Type, arg3: float, arg4: float, arg5: boolean, arg6: $Direction$Type): $GeoQuad
 "constructGeoModel"(arg0: $GeometryTree$Type): $BakedGeoModel
}

export namespace $BakedModelFactory {
const FACTORIES: $Map<(string), ($BakedModelFactory)>
const DEFAULT_FACTORY: $BakedModelFactory
function register(arg0: string, arg1: $BakedModelFactory$Type): void
function getForNamespace(arg0: string): $BakedModelFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BakedModelFactory$Type = ($BakedModelFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BakedModelFactory_ = $BakedModelFactory$Type;
}}
declare module "packages/software/bernie/geckolib/cache/texture/$GeoAbstractTexture" {
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$AbstractTexture, $AbstractTexture$Type} from "packages/net/minecraft/client/renderer/texture/$AbstractTexture"

export class $GeoAbstractTexture extends $AbstractTexture {
static readonly "NOT_ASSIGNED": integer

constructor()

public static "uploadSimple"(arg0: integer, arg1: $NativeImage$Type, arg2: boolean, arg3: boolean): void
public "load"(arg0: $ResourceManager$Type): void
public static "appendToPath"(arg0: $ResourceLocation$Type, arg1: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoAbstractTexture$Type = ($GeoAbstractTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoAbstractTexture_ = $GeoAbstractTexture$Type;
}}
declare module "packages/software/bernie/example/client/renderer/item/$JackInTheBoxRenderer" {
import {$JackInTheBoxItem, $JackInTheBoxItem$Type} from "packages/software/bernie/example/item/$JackInTheBoxItem"
import {$GeoItemRenderer, $GeoItemRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoItemRenderer"

export class $JackInTheBoxRenderer extends $GeoItemRenderer<($JackInTheBoxItem)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JackInTheBoxRenderer$Type = ($JackInTheBoxRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JackInTheBoxRenderer_ = $JackInTheBoxRenderer$Type;
}}
declare module "packages/software/bernie/example/$GeckoLibMod" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GeckoLibMod {
static readonly "DISABLE_EXAMPLES_PROPERTY_KEY": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLibMod$Type = ($GeckoLibMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLibMod_ = $GeckoLibMod$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$RawAnimation" {
import {$Animation$LoopType, $Animation$LoopType$Type} from "packages/software/bernie/geckolib/core/animation/$Animation$LoopType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RawAnimation$Stage, $RawAnimation$Stage$Type} from "packages/software/bernie/geckolib/core/animation/$RawAnimation$Stage"

export class $RawAnimation {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"(arg0: $RawAnimation$Type): $RawAnimation
public static "begin"(): $RawAnimation
public "then"(arg0: string, arg1: $Animation$LoopType$Type): $RawAnimation
public "thenLoop"(arg0: string): $RawAnimation
public "thenPlay"(arg0: string): $RawAnimation
public "thenPlayAndHold"(arg0: string): $RawAnimation
public "thenPlayXTimes"(arg0: string, arg1: integer): $RawAnimation
public "thenWait"(arg0: integer): $RawAnimation
public "getAnimationStages"(): $List<($RawAnimation$Stage)>
get "animationStages"(): $List<($RawAnimation$Stage)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RawAnimation$Type = ($RawAnimation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RawAnimation_ = $RawAnimation$Type;
}}
declare module "packages/software/bernie/example/client/renderer/armor/$WolfArmorRenderer" {
import {$GeoArmorRenderer, $GeoArmorRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoArmorRenderer"
import {$WolfArmorItem, $WolfArmorItem$Type} from "packages/software/bernie/example/item/$WolfArmorItem"
import {$HumanoidModel$ArmPose, $HumanoidModel$ArmPose$Type} from "packages/net/minecraft/client/model/$HumanoidModel$ArmPose"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"

export class $WolfArmorRenderer extends $GeoArmorRenderer<($WolfArmorItem)> {
static readonly "OVERLAY_SCALE": float
static readonly "HAT_OVERLAY_SCALE": float
static readonly "LEGGINGS_OVERLAY_SCALE": float
static readonly "TOOT_HORN_XROT_BASE": float
static readonly "TOOT_HORN_YROT_BASE": float
readonly "head": $ModelPart
readonly "hat": $ModelPart
readonly "body": $ModelPart
readonly "rightArm": $ModelPart
readonly "leftArm": $ModelPart
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

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WolfArmorRenderer$Type = ($WolfArmorRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WolfArmorRenderer_ = $WolfArmorRenderer$Type;
}}
declare module "packages/software/bernie/geckolib/animatable/$GeoEntity" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"

export interface $GeoEntity extends $GeoAnimatable {

 "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
 "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
 "getTick"(arg0: any): double
 "triggerAnim"(arg0: string, arg1: string): void
 "getBoneResetTime"(): double
 "getAnimatableInstanceCache"(): $AnimatableInstanceCache
 "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
 "shouldPlayAnimsWhileGamePaused"(): boolean
 "animatableCacheOverride"(): $AnimatableInstanceCache
}

export namespace $GeoEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoEntity$Type = ($GeoEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoEntity_ = $GeoEntity$Type;
}}
declare module "packages/software/bernie/geckolib/cache/texture/$AnimatableTexture" {
import {$SimpleTexture, $SimpleTexture$Type} from "packages/net/minecraft/client/renderer/texture/$SimpleTexture"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export class $AnimatableTexture extends $SimpleTexture {
static readonly "NOT_ASSIGNED": integer

constructor(arg0: $ResourceLocation$Type)

public "load"(arg0: $ResourceManager$Type): void
public "setAnimationFrame"(arg0: integer): void
public static "setAndUpdate"(arg0: $ResourceLocation$Type): void
public static "setAndUpdate"(arg0: $ResourceLocation$Type, arg1: integer): void
set "animationFrame"(value: integer)
set "andUpdate"(value: $ResourceLocation$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimatableTexture$Type = ($AnimatableTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimatableTexture_ = $AnimatableTexture$Type;
}}
declare module "packages/software/bernie/geckolib/cache/object/$GeoQuad" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$GeoVertex, $GeoVertex$Type} from "packages/software/bernie/geckolib/cache/object/$GeoVertex"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $GeoQuad extends $Record {

constructor(vertices: ($GeoVertex$Type)[], normal: $Vector3f$Type, direction: $Direction$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "direction"(): $Direction
public static "build"(arg0: ($GeoVertex$Type)[], arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: boolean, arg8: $Direction$Type): $GeoQuad
public static "build"(arg0: ($GeoVertex$Type)[], arg1: (double)[], arg2: (double)[], arg3: float, arg4: float, arg5: boolean, arg6: $Direction$Type): $GeoQuad
public "normal"(): $Vector3f
public "vertices"(): ($GeoVertex)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeoQuad$Type = ($GeoQuad);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeoQuad_ = $GeoQuad$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/$SoundKeyframeEvent" {
import {$SoundKeyframeData, $SoundKeyframeData$Type} from "packages/software/bernie/geckolib/core/keyframe/event/data/$SoundKeyframeData"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$KeyFrameEvent, $KeyFrameEvent$Type} from "packages/software/bernie/geckolib/core/keyframe/event/$KeyFrameEvent"

export class $SoundKeyframeEvent<T extends $GeoAnimatable> extends $KeyFrameEvent<(T), ($SoundKeyframeData)> {

constructor(arg0: T, arg1: double, arg2: $AnimationController$Type<(T)>, arg3: $SoundKeyframeData$Type)

public "getKeyframeData"(): $SoundKeyframeData
get "keyframeData"(): $SoundKeyframeData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoundKeyframeEvent$Type<T> = ($SoundKeyframeEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoundKeyframeEvent_<T> = $SoundKeyframeEvent$Type<(T)>;
}}
declare module "packages/software/bernie/example/client/renderer/entity/layer/$CoolKidGlassesLayer" {
import {$GeoRenderer, $GeoRenderer$Type} from "packages/software/bernie/geckolib/renderer/$GeoRenderer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$GeoRenderLayer, $GeoRenderLayer$Type} from "packages/software/bernie/geckolib/renderer/layer/$GeoRenderLayer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BakedGeoModel, $BakedGeoModel$Type} from "packages/software/bernie/geckolib/cache/object/$BakedGeoModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$CoolKidEntity, $CoolKidEntity$Type} from "packages/software/bernie/example/entity/$CoolKidEntity"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $CoolKidGlassesLayer extends $GeoRenderLayer<($CoolKidEntity)> {

constructor(arg0: $GeoRenderer$Type<($CoolKidEntity$Type)>)

public "render"(arg0: $PoseStack$Type, arg1: $CoolKidEntity$Type, arg2: $BakedGeoModel$Type, arg3: $RenderType$Type, arg4: $MultiBufferSource$Type, arg5: $VertexConsumer$Type, arg6: float, arg7: integer, arg8: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoolKidGlassesLayer$Type = ($CoolKidGlassesLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoolKidGlassesLayer_ = $CoolKidGlassesLayer$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$Model" {
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MinecraftGeometry, $MinecraftGeometry$Type} from "packages/software/bernie/geckolib/loading/json/raw/$MinecraftGeometry"
import {$FormatVersion, $FormatVersion$Type} from "packages/software/bernie/geckolib/loading/json/$FormatVersion"

export class $Model extends $Record {

constructor(formatVersion: $FormatVersion$Type, minecraftGeometry: ($MinecraftGeometry$Type)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "formatVersion"(): $FormatVersion
public static "deserializer"(): $JsonDeserializer<($Model)>
public "minecraftGeometry"(): ($MinecraftGeometry)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Model$Type = ($Model);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Model_ = $Model$Type;
}}
declare module "packages/software/bernie/geckolib/loading/json/raw/$PolysUnion$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PolysUnion$Type extends $Enum<($PolysUnion$Type)> {
static readonly "QUAD": $PolysUnion$Type
static readonly "TRI": $PolysUnion$Type


public static "values"(): ($PolysUnion$Type)[]
public static "valueOf"(arg0: string): $PolysUnion$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PolysUnion$Type$Type = (("quad") | ("tri")) | ($PolysUnion$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PolysUnion$Type_ = $PolysUnion$Type$Type;
}}
declare module "packages/software/bernie/geckolib/core/animation/$AnimationState" {
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$AnimationController, $AnimationController$Type} from "packages/software/bernie/geckolib/core/animation/$AnimationController"
import {$GeoAnimatable, $GeoAnimatable$Type} from "packages/software/bernie/geckolib/core/animatable/$GeoAnimatable"
import {$RawAnimation, $RawAnimation$Type} from "packages/software/bernie/geckolib/core/animation/$RawAnimation"
import {$PlayState, $PlayState$Type} from "packages/software/bernie/geckolib/core/object/$PlayState"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnimationState<T extends $GeoAnimatable> {
 "animationTick": double

constructor(arg0: T, arg1: float, arg2: float, arg3: float, arg4: boolean)

public "getData"<D>(arg0: $DataTicket$Type<(D)>): D
public "resetCurrentAnimation"(): void
public "isCurrentAnimationStage"(arg0: string): boolean
public "getPartialTick"(): float
public "setData"<D>(arg0: $DataTicket$Type<(D)>, arg1: D): void
public "getLimbSwingAmount"(): float
public "getLimbSwing"(): float
public "getExtraData"(): $Map<($DataTicket<(any)>), (any)>
public "getAnimationTick"(): double
public "isMoving"(): boolean
public "setAnimation"(arg0: $RawAnimation$Type): void
public "getController"(): $AnimationController<(T)>
public "getAnimatable"(): T
public "withController"(arg0: $AnimationController$Type<(T)>): $AnimationState<(T)>
public "isCurrentAnimation"(arg0: $RawAnimation$Type): boolean
public "setAndContinue"(arg0: $RawAnimation$Type): $PlayState
public "setControllerSpeed"(arg0: float): void
get "partialTick"(): float
get "limbSwingAmount"(): float
get "limbSwing"(): float
get "extraData"(): $Map<($DataTicket<(any)>), (any)>
get "animationTick"(): double
get "moving"(): boolean
set "animation"(value: $RawAnimation$Type)
get "controller"(): $AnimationController<(T)>
get "animatable"(): T
set "andContinue"(value: $RawAnimation$Type)
set "controllerSpeed"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationState$Type<T> = ($AnimationState<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationState_<T> = $AnimationState$Type<(T)>;
}}
declare module "packages/software/bernie/example/registry/$BlockEntityRegistry" {
import {$GeckoHabitatBlockEntity, $GeckoHabitatBlockEntity$Type} from "packages/software/bernie/example/block/entity/$GeckoHabitatBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$FertilizerBlockEntity, $FertilizerBlockEntity$Type} from "packages/software/bernie/example/block/entity/$FertilizerBlockEntity"

export class $BlockEntityRegistry {
static readonly "TILES": $DeferredRegister<($BlockEntityType<(any)>)>
static readonly "GECKO_HABITAT": $RegistryObject<($BlockEntityType<($GeckoHabitatBlockEntity)>)>
static readonly "FERTILIZER_BLOCK": $RegistryObject<($BlockEntityType<($FertilizerBlockEntity)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityRegistry$Type = ($BlockEntityRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityRegistry_ = $BlockEntityRegistry$Type;
}}
declare module "packages/software/bernie/geckolib/network/packet/$BlockEntityAnimTriggerPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockEntityAnimTriggerPacket<D> {

constructor(arg0: $BlockPos$Type, arg1: string, arg2: string)

public static "decode"<D>(arg0: $FriendlyByteBuf$Type): $BlockEntityAnimTriggerPacket<(D)>
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "receivePacket"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityAnimTriggerPacket$Type<D> = ($BlockEntityAnimTriggerPacket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityAnimTriggerPacket_<D> = $BlockEntityAnimTriggerPacket$Type<(D)>;
}}
declare module "packages/software/bernie/example/block/entity/$FertilizerBlockEntity" {
import {$AnimatableManager$ControllerRegistrar, $AnimatableManager$ControllerRegistrar$Type} from "packages/software/bernie/geckolib/core/animation/$AnimatableManager$ControllerRegistrar"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$GeoBlockEntity, $GeoBlockEntity$Type} from "packages/software/bernie/geckolib/animatable/$GeoBlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AnimatableInstanceCache, $AnimatableInstanceCache$Type} from "packages/software/bernie/geckolib/core/animatable/instance/$AnimatableInstanceCache"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $FertilizerBlockEntity extends $BlockEntity implements $GeoBlockEntity {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getAnimatableInstanceCache"(): $AnimatableInstanceCache
public "registerControllers"(arg0: $AnimatableManager$ControllerRegistrar$Type): void
public "getAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>): D
public "setAnimData"<D>(arg0: $SerializableDataTicket$Type<(D)>, arg1: D): void
public "getTick"(arg0: any): double
public "triggerAnim"(arg0: string, arg1: string): void
public "getBoneResetTime"(): double
public "shouldPlayAnimsWhileGamePaused"(): boolean
public "animatableCacheOverride"(): $AnimatableInstanceCache
get "animatableInstanceCache"(): $AnimatableInstanceCache
get "boneResetTime"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FertilizerBlockEntity$Type = ($FertilizerBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FertilizerBlockEntity_ = $FertilizerBlockEntity$Type;
}}
declare module "packages/software/bernie/geckolib/core/object/$DataTicket" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DataTicket<D> {

constructor(arg0: string, arg1: $Class$Type<(any)>)

public "hashCode"(): integer
public "id"(): string
public "getData"<D>(arg0: $Map$Type<(any), (any)>): D
public "objectType"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataTicket$Type<D> = ($DataTicket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataTicket_<D> = $DataTicket$Type<(D)>;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/event/data/$KeyFrameData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $KeyFrameData {

constructor(arg0: double)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getStartTick"(): double
get "startTick"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyFrameData$Type = ($KeyFrameData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyFrameData_ = $KeyFrameData$Type;
}}
declare module "packages/software/bernie/geckolib/core/keyframe/$Keyframe" {
import {$EasingType, $EasingType$Type} from "packages/software/bernie/geckolib/core/animation/$EasingType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $Keyframe<T extends $IValue> extends $Record {

constructor(length: double, startValue: T, endValue: T, easingType: $EasingType$Type, easingArgs: $List$Type<(T)>)
constructor(arg0: double, arg1: T, arg2: T, arg3: $EasingType$Type)
constructor(arg0: double, arg1: T, arg2: T)

public "equals"(arg0: any): boolean
public "length"(): double
public "toString"(): string
public "hashCode"(): integer
public "endValue"(): T
public "easingArgs"(): $List<(T)>
public "startValue"(): T
public "easingType"(): $EasingType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Keyframe$Type<T> = ($Keyframe<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Keyframe_<T> = $Keyframe$Type<(T)>;
}}
declare module "packages/software/bernie/example/registry/$EntityRegistry" {
import {$RaceCarEntity, $RaceCarEntity$Type} from "packages/software/bernie/example/entity/$RaceCarEntity"
import {$EntityType$EntityFactory, $EntityType$EntityFactory$Type} from "packages/net/minecraft/world/entity/$EntityType$EntityFactory"
import {$BikeEntity, $BikeEntity$Type} from "packages/software/bernie/example/entity/$BikeEntity"
import {$DynamicExampleEntity, $DynamicExampleEntity$Type} from "packages/software/bernie/example/entity/$DynamicExampleEntity"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$ParasiteEntity, $ParasiteEntity$Type} from "packages/software/bernie/example/entity/$ParasiteEntity"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$BatEntity, $BatEntity$Type} from "packages/software/bernie/example/entity/$BatEntity"
import {$FakeGlassEntity, $FakeGlassEntity$Type} from "packages/software/bernie/example/entity/$FakeGlassEntity"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$CoolKidEntity, $CoolKidEntity$Type} from "packages/software/bernie/example/entity/$CoolKidEntity"

export class $EntityRegistry {
static readonly "ENTITIES": $DeferredRegister<($EntityType<(any)>)>
static readonly "BAT": $RegistryObject<($EntityType<($BatEntity)>)>
static readonly "BIKE": $RegistryObject<($EntityType<($BikeEntity)>)>
static readonly "RACE_CAR": $RegistryObject<($EntityType<($RaceCarEntity)>)>
static readonly "PARASITE": $RegistryObject<($EntityType<($ParasiteEntity)>)>
static readonly "MUTANT_ZOMBIE": $RegistryObject<($EntityType<($DynamicExampleEntity)>)>
static readonly "FAKE_GLASS": $RegistryObject<($EntityType<($FakeGlassEntity)>)>
static readonly "COOL_KID": $RegistryObject<($EntityType<($CoolKidEntity)>)>
static readonly "GREMLIN": $RegistryObject<($EntityType<($DynamicExampleEntity)>)>

constructor()

public static "registerMob"<T extends $Mob>(arg0: string, arg1: $EntityType$EntityFactory$Type<(T)>, arg2: float, arg3: float, arg4: integer, arg5: integer): $RegistryObject<($EntityType<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRegistry$Type = ($EntityRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRegistry_ = $EntityRegistry$Type;
}}
declare module "packages/software/bernie/geckolib/constant/$DataTickets" {
import {$DataTicket, $DataTicket$Type} from "packages/software/bernie/geckolib/core/object/$DataTicket"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$EntityModelData, $EntityModelData$Type} from "packages/software/bernie/geckolib/model/data/$EntityModelData"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $DataTickets {
static readonly "BLOCK_ENTITY": $DataTicket<($BlockEntity)>
static readonly "ITEMSTACK": $DataTicket<($ItemStack)>
static readonly "ENTITY": $DataTicket<($Entity)>
static readonly "EQUIPMENT_SLOT": $DataTicket<($EquipmentSlot)>
static readonly "ENTITY_MODEL_DATA": $DataTicket<($EntityModelData)>
static readonly "TICK": $DataTicket<(double)>
static readonly "ITEM_RENDER_PERSPECTIVE": $DataTicket<($ItemDisplayContext)>
static readonly "ANIM_STATE": $SerializableDataTicket<(integer)>
static readonly "ANIM": $SerializableDataTicket<(string)>
static readonly "USE_TICKS": $SerializableDataTicket<(integer)>
static readonly "ACTIVE": $SerializableDataTicket<(boolean)>
static readonly "OPEN": $SerializableDataTicket<(boolean)>
static readonly "CLOSED": $SerializableDataTicket<(boolean)>
static readonly "DIRECTION": $SerializableDataTicket<($Direction)>

constructor()

public static "byName"(arg0: string): $SerializableDataTicket<(any)>
public static "registerSerializable"<D>(arg0: $SerializableDataTicket$Type<(D)>): $SerializableDataTicket<(D)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataTickets$Type = ($DataTickets);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataTickets_ = $DataTickets$Type;
}}
declare module "packages/software/bernie/geckolib/network/packet/$EntityAnimDataSyncPacket" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$SerializableDataTicket, $SerializableDataTicket$Type} from "packages/software/bernie/geckolib/network/$SerializableDataTicket"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EntityAnimDataSyncPacket<D> {

constructor(arg0: integer, arg1: $SerializableDataTicket$Type<(D)>, arg2: D)

public static "decode"<D>(arg0: $FriendlyByteBuf$Type): $EntityAnimDataSyncPacket<(D)>
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "receivePacket"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAnimDataSyncPacket$Type<D> = ($EntityAnimDataSyncPacket<(D)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAnimDataSyncPacket_<D> = $EntityAnimDataSyncPacket$Type<(D)>;
}}
