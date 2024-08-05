declare module "packages/lio/playeranimatorapi/compatibility/$PehkuiCompat" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"

export class $PehkuiCompat {

constructor()

public static "getScale"(player: $AbstractClientPlayer$Type, tickDelta: float): $Vec2
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PehkuiCompat$Type = ($PehkuiCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PehkuiCompat_ = $PehkuiCompat$Type;
}}
declare module "packages/lio/playeranimatorapi/misc/$PlayerModelInterface" {
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"

export interface $PlayerModelInterface {

 "setPlayerModel"(arg0: $PlayerModel$Type<(any)>): void

(arg0: $PlayerModel$Type<(any)>): void
}

export namespace $PlayerModelInterface {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerModelInterface$Type = ($PlayerModelInterface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerModelInterface_ = $PlayerModelInterface$Type;
}}
declare module "packages/lio/playeranimatorapi/events/$ClientPlayerTickEvent" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ClientPlayerTickEvent {

constructor()

public static "tick"(player: $Player$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerTickEvent$Type = ($ClientPlayerTickEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerTickEvent_ = $ClientPlayerTickEvent$Type;
}}
declare module "packages/lio/playeranimatorapi/$ResourceReloadListener" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $ResourceReloadListener implements $ResourceManagerReloadListener {

constructor()

public "onResourceManagerReload"(resourceManager: $ResourceManager$Type): void
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceReloadListener$Type = ($ResourceReloadListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceReloadListener_ = $ResourceReloadListener$Type;
}}
declare module "packages/lio/playeranimatorapi/liolib/$ModGeckoLibUtilsClient" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModGeckoLibUtilsClient {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModGeckoLibUtilsClient$Type = ($ModGeckoLibUtilsClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModGeckoLibUtilsClient_ = $ModGeckoLibUtilsClient$Type;
}}
declare module "packages/lio/playeranimatorapi/commands/$StopPlayerAnimationCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $StopPlayerAnimationCommand {

constructor()

public static "register"(dispatcher: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StopPlayerAnimationCommand$Type = ($StopPlayerAnimationCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StopPlayerAnimationCommand_ = $StopPlayerAnimationCommand$Type;
}}
declare module "packages/lio/playeranimatorapi/$ModInitClient" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModInitClient {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModInitClient$Type = ($ModInitClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModInitClient_ = $ModInitClient$Type;
}}
declare module "packages/lio/playeranimatorapi/mixin/$KeyframeAnimationPlayerAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeyframeAnimationPlayerAccessor {

 "getTickDelta"(): float

(): float
}

export namespace $KeyframeAnimationPlayerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimationPlayerAccessor$Type = ($KeyframeAnimationPlayerAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimationPlayerAccessor_ = $KeyframeAnimationPlayerAccessor$Type;
}}
declare module "packages/lio/playeranimatorapi/data/$PlayerAnimationData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$PlayerParts, $PlayerParts$Type} from "packages/lio/playeranimatorapi/data/$PlayerParts"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CommonModifier, $CommonModifier$Type} from "packages/lio/playeranimatorapi/modifier/$CommonModifier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlayerAnimationData extends $Record {
static readonly "UUID_CODEC": $Codec<($UUID)>
static readonly "RESOURCE_LOCATION_CODEC": $Codec<($ResourceLocation)>
static readonly "CODEC": $Codec<($PlayerAnimationData)>

constructor(playerUUID: $UUID$Type, animationID: $ResourceLocation$Type, parts: $PlayerParts$Type, modifiers: $List$Type<($CommonModifier$Type)>, fadeLength: integer, easeID: integer, firstPersonEnabled: boolean, important: boolean)

public static "writeUUID"(uuid: $UUID$Type): $List<(long)>
public static "readUUID"(input: $List$Type<(long)>): $DataResult<($UUID)>
public "modifiers"(): $List<($CommonModifier)>
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "parts"(): $PlayerParts
public "fadeLength"(): integer
public "playerUUID"(): $UUID
public "animationID"(): $ResourceLocation
public "easeID"(): integer
public "important"(): boolean
public "firstPersonEnabled"(): boolean
public static "resourceLocationToString"(location: $ResourceLocation$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationData$Type = ($PlayerAnimationData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationData_ = $PlayerAnimationData$Type;
}}
declare module "packages/lio/playeranimatorapi/modifier/$LengthModifier" {
import {$SpeedModifier, $SpeedModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$SpeedModifier"
import {$CustomModifierLayer, $CustomModifierLayer$Type} from "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer"

export class $LengthModifier extends $SpeedModifier {
 "speed": float

constructor(layer: $CustomModifierLayer$Type<(any)>, desiredLength: float)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LengthModifier$Type = ($LengthModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LengthModifier_ = $LengthModifier$Type;
}}
declare module "packages/lio/playeranimatorapi/API/$PlayerAnimAPIClient" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$PlayerAnimationData, $PlayerAnimationData$Type} from "packages/lio/playeranimatorapi/data/$PlayerAnimationData"
import {$PlayerParts, $PlayerParts$Type} from "packages/lio/playeranimatorapi/data/$PlayerParts"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CommonModifier, $CommonModifier$Type} from "packages/lio/playeranimatorapi/modifier/$CommonModifier"

export class $PlayerAnimAPIClient {

constructor()

public static "playPlayerAnim"(player: $AbstractClientPlayer$Type, animationID: $ResourceLocation$Type, parts: $PlayerParts$Type, modifiers: $List$Type<($CommonModifier$Type)>, fadeLength: integer, easeID: integer, firstPersonEnabled: boolean, important: boolean, replaceTick: boolean): void
public static "playPlayerAnim"(player: $AbstractClientPlayer$Type, data: $PlayerAnimationData$Type): void
public static "playPlayerAnim"(player: $AbstractClientPlayer$Type, animationID: $ResourceLocation$Type, parts: $PlayerParts$Type, modifiers: $List$Type<($CommonModifier$Type)>, important: boolean): void
public static "playPlayerAnim"(player: $AbstractClientPlayer$Type, animationID: $ResourceLocation$Type): void
public static "stopPlayerAnim"(player: $AbstractClientPlayer$Type, animationID: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimAPIClient$Type = ($PlayerAnimAPIClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimAPIClient_ = $PlayerAnimAPIClient$Type;
}}
declare module "packages/lio/playeranimatorapi/modifier/$HeadPosBoundCamera" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"
import {$CustomModifierLayer, $CustomModifierLayer$Type} from "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer"
import {$AbstractCameraModifier, $AbstractCameraModifier$Type} from "packages/lio/playeranimatorapi/modifier/$AbstractCameraModifier"

export class $HeadPosBoundCamera extends $AbstractCameraModifier {

constructor(layer: $CustomModifierLayer$Type<(any)>)

public "get3DCameraTransform"(renderer: $GameRenderer$Type, camera: $Camera$Type, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HeadPosBoundCamera$Type = ($HeadPosBoundCamera);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HeadPosBoundCamera_ = $HeadPosBoundCamera$Type;
}}
declare module "packages/lio/playeranimatorapi/data/$PlayerParts" {
import {$PlayerPart, $PlayerPart$Type} from "packages/lio/playeranimatorapi/data/$PlayerPart"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlayerParts {
static "CODEC": $Codec<($PlayerParts)>
static readonly "allEnabled": $PlayerParts
 "body": $PlayerPart
 "head": $PlayerPart
 "torso": $PlayerPart
 "rightArm": $PlayerPart
 "leftArm": $PlayerPart
 "rightLeg": $PlayerPart
 "leftLeg": $PlayerPart
 "rightItem": $PlayerPart
 "leftItem": $PlayerPart

constructor()

public static "toList"(parts: $PlayerParts$Type): $List<($PlayerPart)>
public static "allExceptHeadRot"(): $PlayerParts
public static "readFromList"(list: $List$Type<($PlayerPart$Type)>): $DataResult<($PlayerParts)>
public static "fromBigInteger"(n: $BigInteger$Type): $PlayerParts
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerParts$Type = ($PlayerParts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerParts_ = $PlayerParts$Type;
}}
declare module "packages/lio/playeranimatorapi/modifier/$HeadRotBoundCamera" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"
import {$CustomModifierLayer, $CustomModifierLayer$Type} from "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer"
import {$AbstractCameraModifier, $AbstractCameraModifier$Type} from "packages/lio/playeranimatorapi/modifier/$AbstractCameraModifier"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $HeadRotBoundCamera extends $AbstractCameraModifier {

constructor(layer: $CustomModifierLayer$Type<(any)>)

public "get3DCameraTransform"(renderer: $GameRenderer$Type, camera: $Camera$Type, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public static "isEntityUpsideDown"(entity: $LivingEntity$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HeadRotBoundCamera$Type = ($HeadRotBoundCamera);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HeadRotBoundCamera_ = $HeadRotBoundCamera$Type;
}}
declare module "packages/lio/playeranimatorapi/modifier/$MirrorOnAltHandModifier" {
import {$MirrorModifier, $MirrorModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$MirrorModifier"
import {$CustomModifierLayer, $CustomModifierLayer$Type} from "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MirrorOnAltHandModifier extends $MirrorModifier {
static readonly "mirrorMap": $Map<(string), (string)>

constructor(layer: $CustomModifierLayer$Type<(any)>)

public "isEnabled"(): boolean
get "enabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MirrorOnAltHandModifier$Type = ($MirrorOnAltHandModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MirrorOnAltHandModifier_ = $MirrorOnAltHandModifier$Type;
}}
declare module "packages/lio/playeranimatorapi/forge/$PlayerAnimatorAPIModForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlayerAnimatorAPIModForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimatorAPIModForge$Type = ($PlayerAnimatorAPIModForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimatorAPIModForge_ = $PlayerAnimatorAPIModForge$Type;
}}
declare module "packages/lio/playeranimatorapi/API/$PlayerAnimAPI" {
import {$PlayerAnimationData, $PlayerAnimationData$Type} from "packages/lio/playeranimatorapi/data/$PlayerAnimationData"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PlayerParts, $PlayerParts$Type} from "packages/lio/playeranimatorapi/data/$PlayerParts"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CommonModifier, $CommonModifier$Type} from "packages/lio/playeranimatorapi/modifier/$CommonModifier"

export class $PlayerAnimAPI {
static readonly "playerAnimPacket": $ResourceLocation
static readonly "playerAnimStopPacket": $ResourceLocation
static readonly "MIRROR_ON_ALT_HAND": $ResourceLocation
static readonly "gameplayModifiers": $List<($CommonModifier)>
static "gson": $Gson

constructor()

public static "playPlayerAnim"(level: $ServerLevel$Type, player: $Player$Type, data: $PlayerAnimationData$Type): void
public static "playPlayerAnim"(level: $ServerLevel$Type, player: $Player$Type, animationID: $ResourceLocation$Type, parts: $PlayerParts$Type, modifiers: $List$Type<($CommonModifier$Type)>, fadeLength: integer, easeID: integer, firstPersonEnabled: boolean, important: boolean): void
public static "playPlayerAnim"(level: $ServerLevel$Type, player: $Player$Type, animationID: $ResourceLocation$Type): void
public static "playPlayerAnim"(level: $ServerLevel$Type, player: $Player$Type, animationID: $ResourceLocation$Type, parts: $PlayerParts$Type, modifiers: $List$Type<($CommonModifier$Type)>, important: boolean): void
public static "stopPlayerAnim"(level: $ServerLevel$Type, player: $Player$Type, animationID: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimAPI$Type = ($PlayerAnimAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimAPI_ = $PlayerAnimAPI$Type;
}}
declare module "packages/lio/playeranimatorapi/playeranims/$PlayerAnimations" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$PlayerAnimationData, $PlayerAnimationData$Type} from "packages/lio/playeranimatorapi/data/$PlayerAnimationData"
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$PlayerParts, $PlayerParts$Type} from "packages/lio/playeranimatorapi/data/$PlayerParts"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomModifierLayer, $CustomModifierLayer$Type} from "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer"
import {$CommonModifier, $CommonModifier$Type} from "packages/lio/playeranimatorapi/modifier/$CommonModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlayerAnimations {
static "gson": $Gson
static "animLengthsMap": $Map<($ResourceLocation), (float)>
static "geckoMap": $Map<($ResourceLocation), ($ResourceLocation)>
static readonly "playerAnimPacket": $ResourceLocation
static readonly "playerAnimStopPacket": $ResourceLocation
static readonly "animationLayerId": $ResourceLocation

constructor()

public static "init"(): void
public static "getEase"(ID: integer): $Ease
public static "getModifierLayer"(player: $AbstractClientPlayer$Type): $CustomModifierLayer<(any)>
public static "stopAnimation"(playerUUID: $UUID$Type, animationID: $ResourceLocation$Type): void
public static "stopAnimation"(player: $AbstractClientPlayer$Type, animationID: $ResourceLocation$Type): void
public static "receivePacket"(jsonData: string): void
public static "playAnimation"(player: $AbstractClientPlayer$Type, data: $PlayerAnimationData$Type, parts: $PlayerParts$Type, modifiers: $List$Type<($CommonModifier$Type)>, fadeLength: integer, easeID: integer, firstPersonEnabled: boolean, replaceTick: boolean): void
public static "playAnimation"(player: $AbstractClientPlayer$Type, data: $PlayerAnimationData$Type, replaceTick: boolean): void
public static "playAnimation"(player: $AbstractClientPlayer$Type, data: $PlayerAnimationData$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimations$Type = ($PlayerAnimations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimations_ = $PlayerAnimations$Type;
}}
declare module "packages/lio/playeranimatorapi/liolib/$ModGeckoLibUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModGeckoLibUtils {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModGeckoLibUtils$Type = ($ModGeckoLibUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModGeckoLibUtils_ = $ModGeckoLibUtils$Type;
}}
declare module "packages/lio/playeranimatorapi/forge/$ClientEvents" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClientEvents {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEvents$Type = ($ClientEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEvents_ = $ClientEvents$Type;
}}
declare module "packages/lio/playeranimatorapi/utils/$CameraUtils" {
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $CameraUtils {

constructor()

public static "computeCameraAngles"(renderer: $GameRenderer$Type, camera: $Camera$Type, partialTicks: double): $Vec3f
public static "computeCameraLocation"(renderer: $GameRenderer$Type, camera: $Camera$Type, partialTicks: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CameraUtils$Type = ($CameraUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CameraUtils_ = $CameraUtils$Type;
}}
declare module "packages/lio/playeranimatorapi/forge/$ModEvents" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModEvents {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModEvents$Type = ($ModEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModEvents_ = $ModEvents$Type;
}}
declare module "packages/lio/playeranimatorapi/modifier/$CommonModifier" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CommonModifier {
 "ID": $ResourceLocation
 "data": $JsonObject
static "gson": $Gson
static readonly "CODEC": $Codec<($CommonModifier)>
static readonly "LIST_CODEC": $Codec<($List<($CommonModifier)>)>
static readonly "nullModifer": $CommonModifier
static readonly "emptyList": $List<($CommonModifier)>

constructor(ID: $ResourceLocation$Type, json: $JsonObject$Type)

public static "decode"(data: $List$Type<(string)>): $DataResult<($CommonModifier)>
public static "encode"(modifier: $CommonModifier$Type): $List<(string)>
public static "encodeList"(list: $List$Type<($CommonModifier$Type)>): $List<($CommonModifier)>
public static "decodeList"(list: $List$Type<($CommonModifier$Type)>): $DataResult<($List<($CommonModifier)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonModifier$Type = ($CommonModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonModifier_ = $CommonModifier$Type;
}}
declare module "packages/lio/playeranimatorapi/$ModMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ModMixinPlugin implements $IMixinConfigPlugin {

constructor()

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
export type $ModMixinPlugin$Type = ($ModMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMixinPlugin_ = $ModMixinPlugin$Type;
}}
declare module "packages/lio/playeranimatorapi/$ModInit" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $ModInit {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger
static "liosplayeranimationapiloaded": boolean

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModInit$Type = ($ModInit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModInit_ = $ModInit$Type;
}}
declare module "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer" {
import {$PlayerAnimationData, $PlayerAnimationData$Type} from "packages/lio/playeranimatorapi/data/$PlayerAnimationData"
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$ModifierLayer, $ModifierLayer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$ModifierLayer"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$KeyframeAnimationPlayer, $KeyframeAnimationPlayer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer"
import {$AbstractFadeModifier, $AbstractFadeModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractFadeModifier"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$AbstractCameraModifier, $AbstractCameraModifier$Type} from "packages/lio/playeranimatorapi/modifier/$AbstractCameraModifier"

export class $CustomModifierLayer<T extends $IAnimation> extends $ModifierLayer<(any)> implements $IAnimation {
static readonly "voidVector": $Vec3f
 "cameraModifiers": $List<($AbstractCameraModifier)>
 "modifierCount": integer
 "hasModifier": boolean
 "cameraAnimEnabled": boolean
 "important": boolean
 "data": $PlayerAnimationData
 "currentAnim": $ResourceLocation
 "animPlayer": $KeyframeAnimationPlayer
 "player": $AbstractClientPlayer

constructor(animation: T, player: $AbstractClientPlayer$Type, ...modifiers: ($AbstractModifier$Type)[])
constructor(player: $AbstractClientPlayer$Type)

public "tick"(): void
public "replaceAnimation"(newAnimation: $KeyframeAnimationPlayer$Type): void
public "removeAllModifiers"(): void
public "setAnimationData"(data: $PlayerAnimationData$Type): void
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "replaceAnimationWithFade"(fadeModifier: $AbstractFadeModifier$Type, newAnimation: $KeyframeAnimationPlayer$Type): void
public "setCurrentAnimationLocation"(animation: $ResourceLocation$Type): void
public "getSpeed"(): float
public "setAnimPlayer"(animPlayer: $KeyframeAnimationPlayer$Type): void
public "addModifier"(modifier: $AbstractModifier$Type): void
public "isActive"(): boolean
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "setupAnim"(arg0: float): void
public "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
set "animationData"(value: $PlayerAnimationData$Type)
set "currentAnimationLocation"(value: $ResourceLocation$Type)
get "speed"(): float
set "animPlayer"(value: $KeyframeAnimationPlayer$Type)
get "active"(): boolean
set "upAnim"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomModifierLayer$Type<T> = ($CustomModifierLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomModifierLayer_<T> = $CustomModifierLayer$Type<(T)>;
}}
declare module "packages/lio/playeranimatorapi/registry/$PlayerEffectsRendererRegistry" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"

export class $PlayerEffectsRendererRegistry {

constructor()

public static "register"(renderer: $EntityRenderer$Type<(any)>): void
public static "getRenderers"(): $List<($EntityRenderer)>
get "renderers"(): $List<($EntityRenderer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEffectsRendererRegistry$Type = ($PlayerEffectsRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEffectsRendererRegistry_ = $PlayerEffectsRendererRegistry$Type;
}}
declare module "packages/lio/playeranimatorapi/mixin/$CameraAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CameraAccessor {

 "getEyeHeightOld"(): float
 "callSetPosition"(arg0: double, arg1: double, arg2: double): void
 "getEyeHeight"(): float
}

export namespace $CameraAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CameraAccessor$Type = ($CameraAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CameraAccessor_ = $CameraAccessor$Type;
}}
declare module "packages/lio/playeranimatorapi/forge/$ModMixinPluginForge" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ModMixinPluginForge implements $IMixinConfigPlugin {

constructor()

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
export type $ModMixinPluginForge$Type = ($ModMixinPluginForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMixinPluginForge_ = $ModMixinPluginForge$Type;
}}
declare module "packages/lio/playeranimatorapi/registry/$AnimModifierRegistry" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CustomModifierLayer, $CustomModifierLayer$Type} from "packages/lio/playeranimatorapi/playeranims/$CustomModifierLayer"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AnimModifierRegistry {

constructor()

public static "getModifiers"(): $Map<($ResourceLocation), ($BiFunction<($CustomModifierLayer), ($JsonObject), ($AbstractModifier)>)>
public static "register"(): void
public static "registerModifier"(ID: $ResourceLocation$Type, arg1: $BiFunction$Type<($CustomModifierLayer$Type), ($JsonObject$Type), ($AbstractModifier$Type)>): void
get "modifiers"(): $Map<($ResourceLocation), ($BiFunction<($CustomModifierLayer), ($JsonObject), ($AbstractModifier)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimModifierRegistry$Type = ($AnimModifierRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimModifierRegistry_ = $AnimModifierRegistry$Type;
}}
declare module "packages/lio/playeranimatorapi/utils/$ModMath" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $ModMath {

constructor()

public static "moveInLocalSpace"(input: $Vec3$Type, xRot: float, yRot: float): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModMath$Type = ($ModMath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMath_ = $ModMath$Type;
}}
declare module "packages/lio/playeranimatorapi/playeranims/$ConditionalAnimations" {
import {$PlayerAnimationData, $PlayerAnimationData$Type} from "packages/lio/playeranimatorapi/data/$PlayerAnimationData"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ConditionalAnimations {

constructor()

public static "getAnimationForCurrentConditions"(data: $PlayerAnimationData$Type): $ResourceLocation
public static "addModConditions"(namespace: string, arg1: $Function$Type<($PlayerAnimationData$Type), ($ResourceLocation$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConditionalAnimations$Type = ($ConditionalAnimations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConditionalAnimations_ = $ConditionalAnimations$Type;
}}
declare module "packages/lio/playeranimatorapi/misc/$IsVisibleAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IsVisibleAccessor {

 "zigysPlayerAnimatorAPI$setIsVisible"(value: boolean): void
 "zigysPlayerAnimatorAPI$getIsVisible"(): boolean
}

export namespace $IsVisibleAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IsVisibleAccessor$Type = ($IsVisibleAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IsVisibleAccessor_ = $IsVisibleAccessor$Type;
}}
declare module "packages/lio/playeranimatorapi/modifier/$AbstractCameraModifier" {
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AbstractCameraModifier extends $AbstractModifier {

constructor()

public "get3DCameraTransform"(renderer: $GameRenderer$Type, camera: $Camera$Type, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCameraModifier$Type = ($AbstractCameraModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCameraModifier_ = $AbstractCameraModifier$Type;
}}
declare module "packages/lio/playeranimatorapi/commands/$CommandState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CommandState extends $Enum<($CommandState)> {
static readonly "Minimal": $CommandState
static readonly "Advanced": $CommandState
static readonly "Complete": $CommandState


public static "values"(): ($CommandState)[]
public static "valueOf"(name: string): $CommandState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandState$Type = (("minimal") | ("advanced") | ("complete")) | ($CommandState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandState_ = $CommandState$Type;
}}
declare module "packages/lio/playeranimatorapi/utils/$CommonPlayerLookup" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $CommonPlayerLookup {

constructor()

public static "tracking"(world: $ServerLevel$Type, pos: $ChunkPos$Type): $Collection<($ServerPlayer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonPlayerLookup$Type = ($CommonPlayerLookup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonPlayerLookup_ = $CommonPlayerLookup$Type;
}}
declare module "packages/lio/playeranimatorapi/data/$PlayerPart" {
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PlayerPart {
static "CODEC": $Codec<($PlayerPart)>
 "x": boolean
 "y": boolean
 "z": boolean
 "pitch": boolean
 "yaw": boolean
 "roll": boolean
 "bend": boolean
 "bendDirection": boolean
 "isVisible": boolean

constructor()

public static "toList"(part: $PlayerPart$Type): $List<(boolean)>
public "setEnabled"(enabled: boolean): void
public "setX"(x: boolean): void
public "setY"(y: boolean): void
public "setZ"(z: boolean): void
public "isVisible"(isVisible: boolean): void
public "setRoll"(roll: boolean): void
public "setPitch"(pitch: boolean): void
public "setYaw"(yaw: boolean): void
public static "readFromList"(list: $List$Type<(boolean)>): $DataResult<($PlayerPart)>
public "setBendDirection"(bendDirection: boolean): void
public "setBend"(bend: boolean): void
set "enabled"(value: boolean)
set "x"(value: boolean)
set "y"(value: boolean)
set "z"(value: boolean)
set "roll"(value: boolean)
set "pitch"(value: boolean)
set "yaw"(value: boolean)
set "bendDirection"(value: boolean)
set "bend"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerPart$Type = ($PlayerPart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerPart_ = $PlayerPart$Type;
}}
declare module "packages/lio/playeranimatorapi/misc/$ModEntityDataSerializers" {
import {$EntityDataSerializer, $EntityDataSerializer$Type} from "packages/net/minecraft/network/syncher/$EntityDataSerializer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ModEntityDataSerializers {
static readonly "RESOURCE_LOCATION": $EntityDataSerializer<($ResourceLocation)>

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModEntityDataSerializers$Type = ($ModEntityDataSerializers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModEntityDataSerializers_ = $ModEntityDataSerializers$Type;
}}
declare module "packages/lio/playeranimatorapi/commands/$PlayPlayerAnimationCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$CommonModifier, $CommonModifier$Type} from "packages/lio/playeranimatorapi/modifier/$CommonModifier"

export class $PlayPlayerAnimationCommand {

constructor()

public static "register"(dispatcher: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
public static "playerPartsIntFromString"(string: string): $BigInteger
public static "modifierList"(input: string): $List<($CommonModifier)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayPlayerAnimationCommand$Type = ($PlayPlayerAnimationCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayPlayerAnimationCommand_ = $PlayPlayerAnimationCommand$Type;
}}
