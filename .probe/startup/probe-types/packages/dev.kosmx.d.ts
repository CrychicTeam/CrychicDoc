declare module "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationAccess$AnimationRegister" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$AnimationStack, $AnimationStack$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack"

export interface $PlayerAnimationAccess$AnimationRegister {

 "registerAnimation"(arg0: $AbstractClientPlayer$Type, arg1: $AnimationStack$Type): void

(arg0: $AbstractClientPlayer$Type, arg1: $AnimationStack$Type): void
}

export namespace $PlayerAnimationAccess$AnimationRegister {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationAccess$AnimationRegister$Type = ($PlayerAnimationAccess$AnimationRegister);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationAccess$AnimationRegister_ = $PlayerAnimationAccess$AnimationRegister$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/$IAnimatedPlayer" {
import {$AnimationApplier, $AnimationApplier$Type} from "packages/dev/kosmx/playerAnim/impl/animation/$AnimationApplier"
import {$IPlayer, $IPlayer$Type} from "packages/dev/kosmx/playerAnim/api/$IPlayer"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AnimationStack, $AnimationStack$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack"

export interface $IAnimatedPlayer extends $IPlayer {

/**
 * 
 * @deprecated
 */
 "getAnimation"(): $AnimationApplier
 "playerAnimator_getAnimation"(arg0: $ResourceLocation$Type): $IAnimation
 "playerAnimator_getAnimation"(): $AnimationApplier
 "playerAnimator_setAnimation"(arg0: $ResourceLocation$Type, arg1: $IAnimation$Type): $IAnimation
 "getAnimationStack"(): $AnimationStack
}

export namespace $IAnimatedPlayer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAnimatedPlayer$Type = ($IAnimatedPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAnimatedPlayer_ = $IAnimatedPlayer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/compat/skinLayers/$SkinLayersTransformer" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $SkinLayersTransformer {

constructor()

public static "init"(logger: $Logger$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SkinLayersTransformer$Type = ($SkinLayersTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SkinLayersTransformer_ = $SkinLayersTransformer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$Ease" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Ease extends $Enum<($Ease)> {
static readonly "LINEAR": $Ease
static readonly "CONSTANT": $Ease
static readonly "INSINE": $Ease
static readonly "OUTSINE": $Ease
static readonly "INOUTSINE": $Ease
static readonly "INCUBIC": $Ease
static readonly "OUTCUBIC": $Ease
static readonly "INOUTCUBIC": $Ease
static readonly "INQUAD": $Ease
static readonly "OUTQUAD": $Ease
static readonly "INOUTQUAD": $Ease
static readonly "INQUART": $Ease
static readonly "OUTQUART": $Ease
static readonly "INOUTQUART": $Ease
static readonly "INQUINT": $Ease
static readonly "OUTQUINT": $Ease
static readonly "INOUTQUINT": $Ease
static readonly "INEXPO": $Ease
static readonly "OUTEXPO": $Ease
static readonly "INOUTEXPO": $Ease
static readonly "INCIRC": $Ease
static readonly "OUTCIRC": $Ease
static readonly "INOUTCIRC": $Ease
static readonly "INBACK": $Ease
static readonly "OUTBACK": $Ease
static readonly "INOUTBACK": $Ease
static readonly "INELASTIC": $Ease
static readonly "OUTELASTIC": $Ease
static readonly "INOUTELASTIC": $Ease
static readonly "INBOUNCE": $Ease
static readonly "OUTBOUNCE": $Ease
static readonly "INOUTBOUNCE": $Ease


public "invoke"(f: float): float
public static "values"(): ($Ease)[]
public static "valueOf"(name: string): $Ease
public "getId"(): byte
public static "getEase"(b: byte): $Ease
get "id"(): byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Ease$Type = (("outquint") | ("insine") | ("constant") | ("inoutbounce") | ("outquad") | ("outsine") | ("incubic") | ("outcubic") | ("outquart") | ("outexpo") | ("inelastic") | ("outelastic") | ("inquint") | ("inoutquart") | ("incirc") | ("inoutelastic") | ("inquad") | ("inoutcubic") | ("outbounce") | ("inoutquint") | ("linear") | ("inoutexpo") | ("inback") | ("inquart") | ("outcirc") | ("inoutback") | ("inoutsine") | ("inoutquad") | ("inbounce") | ("inexpo") | ("inoutcirc") | ("outback")) | ($Ease);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Ease_ = $Ease$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Playable {

 "playBackward"(arg0: integer): integer
 "playForward"(arg0: integer): integer
}

export namespace $Playable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Playable$Type = ($Playable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Playable_ = $Playable$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$Pair" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Pair<L, R> {

constructor(left: L, right: R)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getRight"(): R
public "getLeft"(): L
get "right"(): R
get "left"(): L
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pair$Type<L, R> = ($Pair<(L), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pair_<L, R> = $Pair$Type<(L), (R)>;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier" {
import {$ModifierLayer, $ModifierLayer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$ModifierLayer"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$AnimationContainer, $AnimationContainer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationContainer"

export class $AbstractModifier extends $AnimationContainer<($IAnimation)> {

constructor()

public "canRemove"(): boolean
public "setHost"(host: $ModifierLayer$Type<(any)>): void
set "host"(value: $ModifierLayer$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractModifier$Type = ($AbstractModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractModifier_ = $AbstractModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/$Helper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Helper {

constructor()

public static "isBendEnabled"(): boolean
public static "isBendyLibPresent"(): boolean
get "bendEnabled"(): boolean
get "bendyLibPresent"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Helper$Type = ($Helper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Helper_ = $Helper$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$AdjustmentModifier" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AdjustmentModifier$PartModifier, $AdjustmentModifier$PartModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AdjustmentModifier$PartModifier"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AdjustmentModifier extends $AbstractModifier {
 "enabled": boolean

constructor(source: $Function$Type<(string), ($Optional$Type<($AdjustmentModifier$PartModifier$Type)>)>)

public "tick"(): void
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "fadeOut"(fadeOut: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdjustmentModifier$Type = ($AdjustmentModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdjustmentModifier_ = $AdjustmentModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation" {
import {$KeyframeAnimation$StateCollection$State, $KeyframeAnimation$StateCollection$State$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection$State"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$KeyframeAnimation$AnimationBuilder, $KeyframeAnimation$AnimationBuilder$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$AnimationBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AnimationFormat, $AnimationFormat$Type} from "packages/dev/kosmx/playerAnim/core/data/$AnimationFormat"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$KeyframeAnimation$StateCollection, $KeyframeAnimation$StateCollection$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $KeyframeAnimation implements $Supplier<($UUID)> {
static readonly "EMPTY_STATE": $KeyframeAnimation$StateCollection$State
readonly "beginTick": integer
readonly "endTick": integer
readonly "stopTick": integer
readonly "isInfinite": boolean
readonly "returnToTick": integer
readonly "isEasingBefore": boolean
readonly "nsfw": boolean
readonly "isUUIDGenerated": boolean
readonly "extraData": $HashMap<(string), (any)>
readonly "animationFormat": $AnimationFormat


public "get"(): $UUID
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getLength"(): integer
public "copy"(): $KeyframeAnimation
public "isInfinite"(): boolean
public "mutableCopy"(): $KeyframeAnimation$AnimationBuilder
public "getUuid"(): $UUID
public "getBodyParts"(): $Map<(string), ($KeyframeAnimation$StateCollection)>
public "getPartOptional"(id: string): $Optional<($KeyframeAnimation$StateCollection)>
public "isPlayingAt"(tick: integer): boolean
public "getPart"(partID: string): $KeyframeAnimation$StateCollection
get "length"(): integer
get "infinite"(): boolean
get "uuid"(): $UUID
get "bodyParts"(): $Map<(string), ($KeyframeAnimation$StateCollection)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimation$Type = ($KeyframeAnimation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimation_ = $KeyframeAnimation$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$SpeedModifier" {
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $SpeedModifier extends $AbstractModifier {
 "speed": float

constructor()
constructor(speed: float)

public "tick"(): void
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "setupAnim"(tickDelta: float): void
set "upAnim"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpeedModifier$Type = ($SpeedModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpeedModifier_ = $SpeedModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/$IPlayer" {
import {$AnimationStack, $AnimationStack$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack"

export interface $IPlayer {

 "getAnimationStack"(): $AnimationStack

(): $AnimationStack
}

export namespace $IPlayer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlayer$Type = ($IPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlayer_ = $IPlayer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$SetableSupplier" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SetableSupplier<T> implements $Supplier<(T)> {

constructor()

public "get"(): T
public "set"(object: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SetableSupplier$Type<T> = ($SetableSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SetableSupplier_<T> = $SetableSupplier$Type<(T)>;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$Vector3" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Vector3<N extends number> {

constructor(x: N, y: N, z: N)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getY"(): N
public "getZ"(): N
public "getX"(): N
get "y"(): N
get "z"(): N
get "x"(): N
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3$Type<N> = ($Vector3<(N)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3_<N> = $Vector3$Type<(N)>;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection" {
import {$KeyframeAnimation$StateCollection$State, $KeyframeAnimation$StateCollection$State$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection$State"

export class $KeyframeAnimation$StateCollection {
readonly "x": $KeyframeAnimation$StateCollection$State
readonly "y": $KeyframeAnimation$StateCollection$State
readonly "z": $KeyframeAnimation$StateCollection$State
readonly "pitch": $KeyframeAnimation$StateCollection$State
readonly "yaw": $KeyframeAnimation$StateCollection$State
readonly "roll": $KeyframeAnimation$StateCollection$State
readonly "bend": $KeyframeAnimation$StateCollection$State
readonly "bendDirection": $KeyframeAnimation$StateCollection$State
readonly "isBendable": boolean

constructor(threshold: float)
constructor(stateCollection: $KeyframeAnimation$StateCollection$Type)
constructor(x: float, y: float, z: float, pitch: float, yaw: float, roll: float, translationThreshold: float, bendable: boolean)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "copy"(): $KeyframeAnimation$StateCollection
public "isEnabled"(): boolean
public "setEnabled"(enabled: boolean): void
public "isBendable"(): boolean
public "verifyAndLock"(maxLength: integer): void
public "fullyEnablePart"(always: boolean): void
get "enabled"(): boolean
set "enabled"(value: boolean)
get "bendable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimation$StateCollection$Type = ($KeyframeAnimation$StateCollection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimation$StateCollection_ = $KeyframeAnimation$StateCollection$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$NetworkHelper" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $NetworkHelper {

constructor()

public static "writeUUID"(buf: $ByteBuffer$Type, uuid: $UUID$Type): void
public static "readUUID"(buf: $ByteBuffer$Type): $UUID
public static "readString"(buf: $ByteBuffer$Type): string
public static "writeString"(buf: $ByteBuffer$Type, str: string): void
public static "writeVarInt"(buf: $ByteBuffer$Type, i: integer): void
public static "readVarInt"(buf: $ByteBuffer$Type): integer
public static "writeVarString"(buf: $ByteBuffer$Type, str: string): void
public static "readVarString"(buf: $ByteBuffer$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHelper$Type = ($NetworkHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHelper_ = $NetworkHelper$Type;
}}
declare module "packages/dev/kosmx/playerAnim/mixin/firstPerson/$CameraAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CameraAccessor {

 "setDetached"(arg0: boolean): void

(arg0: boolean): void
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
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Move" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"
import {$PartMap$PartValue, $PartMap$PartValue$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$PartMap$PartValue"
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"

export class $Move implements $Playable {

constructor(part: $PartMap$PartValue$Type, value: float, length: integer, ease: $Ease$Type)

public "playBackward"(time: integer): integer
public "playForward"(time: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Move$Type = ($Move);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Move_ = $Move$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$RotationAxis" {
import {$KeyframeAnimationPlayer$Axis, $KeyframeAnimationPlayer$Axis$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$Axis"
import {$KeyframeAnimation$StateCollection$State, $KeyframeAnimation$StateCollection$State$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection$State"
import {$KeyframeAnimationPlayer, $KeyframeAnimationPlayer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer"

export class $KeyframeAnimationPlayer$RotationAxis extends $KeyframeAnimationPlayer$Axis {

constructor(this$0: $KeyframeAnimationPlayer$Type, keyframes: $KeyframeAnimation$StateCollection$State$Type)

public "getValueAtCurrentTick"(currentValue: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimationPlayer$RotationAxis$Type = ($KeyframeAnimationPlayer$RotationAxis);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimationPlayer$RotationAxis_ = $KeyframeAnimationPlayer$RotationAxis$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/impl/event/$EventResult" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EventResult extends $Enum<($EventResult)> {
static readonly "PASS": $EventResult
static readonly "SUCCESS": $EventResult
static readonly "FAIL": $EventResult
static readonly "CONSUME": $EventResult


public static "values"(): ($EventResult)[]
public static "valueOf"(name: string): $EventResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventResult$Type = (("fail") | ("pass") | ("success") | ("consume")) | ($EventResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventResult_ = $EventResult$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Header" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Header {
 "NBS_version": byte
 "Vanilla_instrument_count": byte
/**
 * 
 * @deprecated
 */
 "Song_length": short
 "Layer_count": short
 "Song_name": string
 "Song_author": string
 "Song_original_author": string
 "Song_description": string
 "Song_tempo": short
/**
 * 
 * @deprecated
 */
 "Auto_saving": byte
/**
 * 
 * @deprecated
 */
 "Auto_saving_duration": byte
 "Time_signature": byte
 "Minutes_spent": integer
 "Left_clicks": integer
 "Right_clicks": integer
 "Note_blocks_added": integer
 "Note_blocks_removed": integer
 "MIDI_Schematic_file_name": string
 "Loop_on_off": byte
 "Max_loop_count": byte
 "Loop_start_tick": short

constructor()

public "Auto_saving"(): boolean
public "Loop_on_off"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Header$Type = ($Header);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Header_ = $Header$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/network/$NBSPacket" {
import {$Layer, $Layer$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer"
import {$NBS, $NBS$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/$NBS"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $NBSPacket {

constructor(song: $NBS$Type)
constructor()

public "write"(buf: $ByteBuffer$Type): void
public "read"(buf: $ByteBuffer$Type): boolean
public static "getLayerMessageSize"(layer: $Layer$Type): integer
public static "calculateMessageSize"(song: $NBS$Type): integer
public "writeLayersAndNotes"(buf: $ByteBuffer$Type): void
public "getSong"(): $NBS
get "song"(): $NBS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBSPacket$Type = ($NBSPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBSPacket_ = $NBSPacket$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AnimationStack implements $IAnimation {

constructor()

public "isActive"(): boolean
public "tick"(): void
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "setupAnim"(tickDelta: float): void
public "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
public "addAnimLayer"(priority: integer, layer: $IAnimation$Type): void
public "removeLayer"(layer: $IAnimation$Type): boolean
public "removeLayer"(layerLevel: integer): boolean
get "active"(): boolean
set "upAnim"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationStack$Type = ($AnimationStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationStack_ = $AnimationStack$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Repeat" {
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"

export class $Repeat implements $Playable {

constructor(parent: $Playable$Type, delay: integer, count: integer)

public "playBackward"(time: integer): integer
public "playForward"(time: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Repeat$Type = ($Repeat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Repeat_ = $Repeat$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$MathHelper" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $MathHelper {

constructor()

public static "colorHelper"(r: integer, g: integer, b: integer, a: integer): integer
public static "clampToRadian"(f: float): float
public static "lerp"(delta: double, start: double, end: double): double
public static "lerp"(delta: float, start: float, end: float): float
public static "readFromIStream"(stream: $InputStream$Type): $ByteBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathHelper$Type = ($MathHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathHelper_ = $MathHelper$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Yoyo" {
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"
import {$Repeat, $Repeat$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Repeat"

export class $Yoyo extends $Repeat {

constructor(parent: $Playable$Type, delay: integer, count: integer)

public "playForward"(time: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Yoyo$Type = ($Yoyo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Yoyo_ = $Yoyo$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$PartMap" {
import {$PartMap$PartValue, $PartMap$PartValue$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$PartMap$PartValue"
import {$KeyframeAnimation$StateCollection, $KeyframeAnimation$StateCollection$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection"

export class $PartMap {
 "part": $KeyframeAnimation$StateCollection
 "x": $PartMap$PartValue
 "y": $PartMap$PartValue
 "z": $PartMap$PartValue

constructor(part: $KeyframeAnimation$StateCollection$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PartMap$Type = ($PartMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PartMap_ = $PartMap$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$ModifierLayer" {
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$AbstractFadeModifier, $AbstractFadeModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractFadeModifier"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $ModifierLayer<T extends $IAnimation> implements $IAnimation {

constructor(animation: T, ...modifiers: ($AbstractModifier$Type)[])
constructor()

public "size"(): integer
public "isActive"(): boolean
public "tick"(): void
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "getAnimation"(): T
public "removeModifier"(idx: integer): void
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "setupAnim"(tickDelta: float): void
public "addModifierLast"(modifier: $AbstractModifier$Type): void
public "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
public "replaceAnimationWithFade"(fadeModifier: $AbstractFadeModifier$Type, newAnimation: T): void
public "replaceAnimationWithFade"(fadeModifier: $AbstractFadeModifier$Type, newAnimation: T, fadeFromNothing: boolean): void
public "setAnimation"(animation: T): void
public "addModifierBefore"(modifier: $AbstractModifier$Type): void
public "addModifier"(modifier: $AbstractModifier$Type, idx: integer): void
get "active"(): boolean
get "animation"(): T
set "upAnim"(value: float)
set "animation"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifierLayer$Type<T> = ($ModifierLayer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifierLayer_<T> = $ModifierLayer$Type<(T)>;
}}
declare module "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FirstPersonMode extends $Enum<($FirstPersonMode)> {
static readonly "NONE": $FirstPersonMode
static readonly "VANILLA": $FirstPersonMode
static readonly "THIRD_PERSON_MODEL": $FirstPersonMode
static readonly "DISABLED": $FirstPersonMode


public static "values"(): ($FirstPersonMode)[]
public static "valueOf"(name: string): $FirstPersonMode
public "isEnabled"(): boolean
public static "setFirstPersonPass"(newValue: boolean): void
public static "isFirstPersonPass"(): boolean
get "enabled"(): boolean
set "firstPersonPass"(value: boolean)
get "firstPersonPass"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FirstPersonMode$Type = (("third_person_model") | ("disabled") | ("none") | ("vanilla")) | ($FirstPersonMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FirstPersonMode_ = $FirstPersonMode$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/$AnimUtils" {
import {$AnimationStack, $AnimationStack$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack"

export class $AnimUtils {
/**
 * 
 * @deprecated
 */
static "disableFirstPersonAnim": boolean

constructor()

public static "getPlayerAnimLayer"(player: any): $AnimationStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimUtils$Type = ($AnimUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimUtils_ = $AnimUtils$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/$IUpperPartHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IUpperPartHelper {

 "isUpperPart"(): boolean
 "setUpperPart"(arg0: boolean): void
}

export namespace $IUpperPartHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IUpperPartHelper$Type = ($IUpperPartHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IUpperPartHelper_ = $IUpperPartHelper$Type;
}}
declare module "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationAccess$PlayerAssociatedAnimationData" {
import {$IAnimatedPlayer, $IAnimatedPlayer$Type} from "packages/dev/kosmx/playerAnim/impl/$IAnimatedPlayer"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PlayerAnimationAccess$PlayerAssociatedAnimationData {

constructor(player: $IAnimatedPlayer$Type)

public "get"(id: $ResourceLocation$Type): $IAnimation
public "set"(id: $ResourceLocation$Type, animation: $IAnimation$Type): $IAnimation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationAccess$PlayerAssociatedAnimationData$Type = ($PlayerAnimationAccess$PlayerAssociatedAnimationData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationAccess$PlayerAssociatedAnimationData_ = $PlayerAnimationAccess$PlayerAssociatedAnimationData$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer" {
import {$KeyframeAnimationPlayerAccessor, $KeyframeAnimationPlayerAccessor$Type} from "packages/lio/playeranimatorapi/mixin/$KeyframeAnimationPlayerAccessor"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"
import {$KeyframeAnimationPlayer$BodyPart, $KeyframeAnimationPlayer$BodyPart$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$BodyPart"

export class $KeyframeAnimationPlayer implements $IAnimation, $KeyframeAnimationPlayerAccessor {
readonly "bodyParts": $HashMap<(string), ($KeyframeAnimationPlayer$BodyPart)>
 "perspective": integer

constructor(animation: $KeyframeAnimation$Type)
constructor(animation: $KeyframeAnimation$Type, t: integer)
constructor(animation: $KeyframeAnimation$Type, t: integer, mutable: boolean)

public "isLoopStarted"(): boolean
public "stop"(): void
public "isActive"(): boolean
public "isInfinite"(): boolean
public "tick"(): void
public "getData"(): $KeyframeAnimation
public "setFirstPersonMode"(firstPersonMode: $FirstPersonMode$Type): $KeyframeAnimationPlayer
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "setupAnim"(tickDelta: float): void
public "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
public "getCurrentTick"(): integer
public "getTick"(): integer
public "getPart"(string: string): $KeyframeAnimationPlayer$BodyPart
public "getStopTick"(): integer
public "setFirstPersonConfiguration"(firstPersonConfiguration: $FirstPersonConfiguration$Type): $KeyframeAnimationPlayer
get "loopStarted"(): boolean
get "active"(): boolean
get "infinite"(): boolean
get "data"(): $KeyframeAnimation
set "firstPersonMode"(value: $FirstPersonMode$Type)
set "upAnim"(value: float)
get "currentTick"(): integer
get "stopTick"(): integer
set "firstPersonConfiguration"(value: $FirstPersonConfiguration$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimationPlayer$Type = ($KeyframeAnimationPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimationPlayer_ = $KeyframeAnimationPlayer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$Easing" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"

export class $Easing {

constructor()

public static "inOutBack"(x: float): float
public static "outBack"(x: float): float
public static "inOutBounce"(x: float): float
public static "outElastic"(x: float): float
public static "outBounce"(x: float): float
public static "inBounce"(x: float): float
public static "outCirc"(x: float): float
public static "inBack"(x: float): float
public static "inElastic"(x: float): float
public static "inOutElastic"(x: float): float
/**
 * 
 * @deprecated
 */
public static "easingFromEnum"(type: $Ease$Type, f: float): float
public static "inOutCirc"(x: float): float
public static "easeFromString"(string: string): $Ease
public static "inCirc"(x: float): float
public static "outSine"(f: float): float
public static "outCubic"(f: float): float
public static "inOutCubic"(x: float): float
public static "inQuad"(x: float): float
public static "inOutQuart"(x: float): float
public static "outQuint"(x: float): float
public static "inQuart"(x: float): float
public static "inOutQuint"(x: float): float
public static "inSine"(f: float): float
public static "outQuad"(x: float): float
public static "inCubic"(f: float): float
public static "inOutSine"(f: float): float
public static "inOutQuad"(x: float): float
public static "inExpo"(x: float): float
public static "outQuart"(x: float): float
public static "outExpo"(x: float): float
public static "inOutExpo"(x: float): float
public static "inQuint"(x: float): float
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
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$MirrorModifier" {
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MirrorModifier extends $AbstractModifier {
static readonly "mirrorMap": $Map<(string), (string)>

constructor(enabled: boolean)
constructor()

public "isEnabled"(): boolean
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "setEnabled"(enabled: boolean): void
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
get "enabled"(): boolean
set "enabled"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MirrorModifier$Type = ($MirrorModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MirrorModifier_ = $MirrorModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/$NBS" {
import {$Layer, $Layer$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$CustomInstrument, $CustomInstrument$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$CustomInstrument"
import {$Header, $Header$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Header"

export class $NBS {
readonly "header": $Header

constructor(header: $Header$Type, layers: $ArrayList$Type<($Layer$Type)>, customInstruments: $ArrayList$Type<($CustomInstrument$Type)>)

public "getLength"(): integer
public "setLength"(length: integer): void
public "getLayers"(): $ArrayList<($Layer)>
get "length"(): integer
set "length"(value: integer)
get "layers"(): $ArrayList<($Layer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBS$Type = ($NBS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBS_ = $NBS$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractFadeModifier" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"
import {$AbstractModifier, $AbstractModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractModifier"
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$AbstractFadeModifier$EasingFunction, $AbstractFadeModifier$EasingFunction$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractFadeModifier$EasingFunction"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AbstractFadeModifier extends $AbstractModifier {


public "isActive"(): boolean
public "tick"(): void
public static "standardFadeIn"(length: integer, ease: $Ease$Type): $AbstractFadeModifier
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "setupAnim"(tickDelta: float): void
public "canRemove"(): boolean
public static "functionalFadeIn"(length: integer, arg1: $AbstractFadeModifier$EasingFunction$Type): $AbstractFadeModifier
public "setBeginAnimation"(beginAnimation: $IAnimation$Type): void
get "active"(): boolean
set "upAnim"(value: float)
set "beginAnimation"(value: $IAnimation$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractFadeModifier$Type = ($AbstractFadeModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractFadeModifier_ = $AbstractFadeModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FirstPersonConfiguration {

constructor(showRightArm: boolean, showLeftArm: boolean, showRightItem: boolean, showLeftItem: boolean)
constructor()

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "setShowLeftArm"(showLeftArm: boolean): $FirstPersonConfiguration
public "isShowRightItem"(): boolean
public "setShowRightArm"(showRightArm: boolean): $FirstPersonConfiguration
public "setShowLeftItem"(showLeftItem: boolean): $FirstPersonConfiguration
public "isShowLeftItem"(): boolean
public "setShowRightItem"(showRightItem: boolean): $FirstPersonConfiguration
public "isShowRightArm"(): boolean
public "isShowLeftArm"(): boolean
set "showLeftArm"(value: boolean)
get "showRightItem"(): boolean
set "showRightArm"(value: boolean)
set "showLeftItem"(value: boolean)
get "showLeftItem"(): boolean
set "showRightItem"(value: boolean)
get "showRightArm"(): boolean
get "showLeftArm"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FirstPersonConfiguration$Type = ($FirstPersonConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FirstPersonConfiguration_ = $FirstPersonConfiguration$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$UUIDMap" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $UUIDMap<T extends $Supplier<($UUID)>> extends $HashMap<($UUID), (T)> implements $Iterable<(T)> {

constructor()

public "add"(value: T): void
public "put"(v: T): T
public "iterator"(): $Iterator<(T)>
public "contains"(value: T): boolean
public "addAll"(m: $Collection$Type<(T)>): void
public "removeIf"(predicate: $Predicate$Type<(T)>): void
public "spliterator"(): $Spliterator<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
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
[Symbol.iterator](): IterableIterator<T>;
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UUIDMap$Type<T> = ($UUIDMap<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UUIDMap_<T> = $UUIDMap$Type<(T)>;
}}
declare module "packages/dev/kosmx/playerAnim/impl/animation/$AnimationApplier" {
import {$AnimationProcessor, $AnimationProcessor$Type} from "packages/dev/kosmx/playerAnim/core/impl/$AnimationProcessor"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"

export class $AnimationApplier extends $AnimationProcessor {

constructor(animation: $IAnimation$Type)

public "updatePart"(partName: string, part: $ModelPart$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationApplier$Type = ($AnimationApplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationApplier_ = $AnimationApplier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$Axis" {
import {$KeyframeAnimation$StateCollection$State, $KeyframeAnimation$StateCollection$State$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection$State"
import {$KeyframeAnimationPlayer, $KeyframeAnimationPlayer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer"

export class $KeyframeAnimationPlayer$Axis {

constructor(this$0: $KeyframeAnimationPlayer$Type, keyframes: $KeyframeAnimation$StateCollection$State$Type)

public "getValueAtCurrentTick"(currentValue: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimationPlayer$Axis$Type = ($KeyframeAnimationPlayer$Axis);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimationPlayer$Axis_ = $KeyframeAnimationPlayer$Axis$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/$IPlayerModel" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPlayerModel {

 "playerAnimator_prepForFirstPersonRender"(): void

(): void
}

export namespace $IPlayerModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlayerModel$Type = ($IPlayerModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlayerModel_ = $IPlayerModel$Type;
}}
declare module "packages/dev/kosmx/playerAnim/forge/$ForgeClientEvent" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$RegisterClientReloadListenersEvent, $RegisterClientReloadListenersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientReloadListenersEvent"

export class $ForgeClientEvent {

constructor()

public static "clientSetup"(event: $FMLClientSetupEvent$Type): void
public static "resourceLoadingListener"(event: $RegisterClientReloadListenersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientEvent$Type = ($ForgeClientEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientEvent_ = $ForgeClientEvent$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/gson/$GeckoLibSerializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"

export class $GeckoLibSerializer {

constructor()

public static "serialize"(node: $JsonObject$Type): $List<($KeyframeAnimation)>
public static "getVector"(element: $JsonElement$Type): $JsonArray
public static "snake2Camel"(original: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeckoLibSerializer$Type = ($GeckoLibSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeckoLibSerializer_ = $GeckoLibSerializer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/$TransformType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TransformType extends $Enum<($TransformType)> {
static readonly "POSITION": $TransformType
static readonly "ROTATION": $TransformType
static readonly "BEND": $TransformType


public static "values"(): ($TransformType)[]
public static "valueOf"(name: string): $TransformType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransformType$Type = (("rotation") | ("bend") | ("position")) | ($TransformType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransformType_ = $TransformType$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/mixin/$MixinConfig" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MixinConfig implements $IMixinConfigPlugin {

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
export type $MixinConfig$Type = ($MixinConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinConfig_ = $MixinConfig$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$CustomInstrument" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CustomInstrument {

 "setName"(arg0: string): void
 "setToSongFile"(arg0: string): void
 "setsetSoundPitch"(arg0: byte): void
 "setPressKey"(arg0: boolean): void
}

export namespace $CustomInstrument {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomInstrument$Type = ($CustomInstrument);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomInstrument_ = $CustomInstrument$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$QuarkReader" {
import {$PartMap$PartValue, $PartMap$PartValue$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$PartMap$PartValue"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BufferedReader, $BufferedReader$Type} from "packages/java/io/$BufferedReader"
import {$PartMap, $PartMap$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$PartMap"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"

export class $QuarkReader {

constructor()

public "getBPFromStr"(inf: (string)[]): $PartMap
public "getMethod"(str: $List$Type<(string)>, i: integer, strings: $List$Type<($List$Type<(string)>)>): integer
public static "read"(s: string): $List<(string)>
public "deserialize"(reader: $BufferedReader$Type, name: string): void
public "getPFromStr"(str: string): $PartMap$PartValue
public "getEmote"(): $KeyframeAnimation
get "emote"(): $KeyframeAnimation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuarkReader$Type = ($QuarkReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuarkReader_ = $QuarkReader$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/gson/$AnimationSerializing" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $AnimationSerializing {
static readonly "SERIALIZER": $Gson

constructor()

public static "deserializeAnimation"(stream: $InputStream$Type): $List<($KeyframeAnimation)>
public static "deserializeAnimation"(stream: $Reader$Type): $List<($KeyframeAnimation)>
public static "serializeAnimation"(animation: $KeyframeAnimation$Type): string
public static "writeAnimation"(animation: $KeyframeAnimation$Type, writer: $Writer$Type): $Writer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationSerializing$Type = ($AnimationSerializing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationSerializing_ = $AnimationSerializing$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$AnimationBuilder" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$NBS, $NBS$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/$NBS"
import {$AnimationFormat, $AnimationFormat$Type} from "packages/dev/kosmx/playerAnim/core/data/$AnimationFormat"
import {$KeyframeAnimation$StateCollection, $KeyframeAnimation$StateCollection$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"

export class $KeyframeAnimation$AnimationBuilder {
static "staticThreshold": float
readonly "head": $KeyframeAnimation$StateCollection
readonly "body": $KeyframeAnimation$StateCollection
readonly "rightArm": $KeyframeAnimation$StateCollection
readonly "leftArm": $KeyframeAnimation$StateCollection
readonly "rightLeg": $KeyframeAnimation$StateCollection
readonly "leftLeg": $KeyframeAnimation$StateCollection
readonly "leftItem": $KeyframeAnimation$StateCollection
readonly "rightItem": $KeyframeAnimation$StateCollection
readonly "torso": $KeyframeAnimation$StateCollection
 "isEasingBefore": boolean
 "nsfw": boolean
 "uuid": $UUID
 "beginTick": integer
 "endTick": integer
 "stopTick": integer
 "isLooped": boolean
 "returnTick": integer
 "name": string
 "description": string
 "author": string
 "song": $NBS
 "iconData": $ByteBuffer
 "extraData": $HashMap<(string), (any)>

constructor(validationThreshold: float, emoteFormat: $AnimationFormat$Type)
constructor(source: $AnimationFormat$Type)

public "toString"(): string
public "setName"(s: string): $KeyframeAnimation$AnimationBuilder
public "build"(): $KeyframeAnimation
public "setDescription"(s: string): $KeyframeAnimation$AnimationBuilder
public "optimizeEmote"(): $KeyframeAnimation$AnimationBuilder
public "fullyEnableParts"(): $KeyframeAnimation$AnimationBuilder
public "getOrCreatePart"(name: string): $KeyframeAnimation$StateCollection
public "getOrCreateNewPart"(name: string, x: float, y: float, z: float, pitch: float, yaw: float, roll: float, bendable: boolean): $KeyframeAnimation$StateCollection
public "setUuid"(uuid: $UUID$Type): $KeyframeAnimation$AnimationBuilder
public "setAuthor"(s: string): $KeyframeAnimation$AnimationBuilder
public "getPart"(name: string): $KeyframeAnimation$StateCollection
set "name"(value: string)
set "description"(value: string)
set "uuid"(value: $UUID$Type)
set "author"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimation$AnimationBuilder$Type = ($KeyframeAnimation$AnimationBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimation$AnimationBuilder_ = $KeyframeAnimation$AnimationBuilder$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$IAnimation" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export interface $IAnimation {

 "isActive"(): boolean
 "tick"(): void
 "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
 "get3DTransform"(arg0: string, arg1: $TransformType$Type, arg2: float, arg3: $Vec3f$Type): $Vec3f
 "setupAnim"(arg0: float): void
 "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
}

export namespace $IAnimation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAnimation$Type = ($IAnimation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAnimation_ = $IAnimation$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer$Note" {
import {$Layer, $Layer$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer"

export class $Layer$Note {
 "instrument": byte
 "key": byte
 "velocity": byte
 "panning": byte
 "pitch": short
readonly "tick": integer

constructor(this$0: $Layer$Type, tick: integer)

public "getVolume"(): float
public "getPitch"(): float
get "volume"(): float
get "pitch"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Layer$Note$Type = ($Layer$Note);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Layer$Note_ = $Layer$Note$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$AbstractFadeModifier$EasingFunction" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"

export interface $AbstractFadeModifier$EasingFunction {

 "ease"(arg0: string, arg1: $TransformType$Type, arg2: float): float

(arg0: string, arg1: $TransformType$Type, arg2: float): float
}

export namespace $AbstractFadeModifier$EasingFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractFadeModifier$EasingFunction$Type = ($AbstractFadeModifier$EasingFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractFadeModifier$EasingFunction_ = $AbstractFadeModifier$EasingFunction$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/$IMutableModel" {
import {$AnimationProcessor, $AnimationProcessor$Type} from "packages/dev/kosmx/playerAnim/core/impl/$AnimationProcessor"
import {$SetableSupplier, $SetableSupplier$Type} from "packages/dev/kosmx/playerAnim/core/util/$SetableSupplier"

export interface $IMutableModel {

 "getEmoteSupplier"(): $SetableSupplier<($AnimationProcessor)>
 "setEmoteSupplier"(arg0: $SetableSupplier$Type<($AnimationProcessor$Type)>): void
}

export namespace $IMutableModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMutableModel$Type = ($IMutableModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMutableModel_ = $IMutableModel$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$PartMap$PartValue" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"

export class $PartMap$PartValue {


public "getValue"(): float
public "setValue"(valueAfter: float): void
public "addValue"(tick: integer, value: float, ease: $Ease$Type): void
public "addValue"(tickFrom: integer, tickTo: integer, value: float, ease: $Ease$Type): void
public "hold"(): void
get "value"(): float
set "value"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PartMap$PartValue$Type = ($PartMap$PartValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PartMap$PartValue_ = $PartMap$PartValue$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$PlayerAnimationFrame" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $PlayerAnimationFrame implements $IAnimation {

constructor()

public "isActive"(): boolean
public "tick"(): void
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "resetPose"(): void
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "setupAnim"(arg0: float): void
public "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
get "active"(): boolean
set "upAnim"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationFrame$Type = ($PlayerAnimationFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationFrame_ = $PlayerAnimationFrame$Type;
}}
declare module "packages/dev/kosmx/playerAnim/forge/$ForgeModInterface" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $ForgeModInterface {
static readonly "LOGGER": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModInterface$Type = ($ForgeModInterface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModInterface_ = $ForgeModInterface$Type;
}}
declare module "packages/dev/kosmx/playerAnim/minecraftApi/layers/$LeftHandedHelperModifier" {
import {$MirrorModifier, $MirrorModifier$Type} from "packages/dev/kosmx/playerAnim/api/layered/modifier/$MirrorModifier"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LeftHandedHelperModifier extends $MirrorModifier {
static readonly "mirrorMap": $Map<(string), (string)>

constructor(player: $Player$Type)

public "isEnabled"(): boolean
get "enabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LeftHandedHelperModifier$Type = ($LeftHandedHelperModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LeftHandedHelperModifier_ = $LeftHandedHelperModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/impl/$AnimationProcessor" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$Pair, $Pair$Type} from "packages/dev/kosmx/playerAnim/core/util/$Pair"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AnimationProcessor {

constructor(animation: $IAnimation$Type)

public "isActive"(): boolean
public "tick"(): void
public "getFirstPersonConfiguration"(): $FirstPersonConfiguration
public "get3DTransform"(modelName: string, type: $TransformType$Type, value0: $Vec3f$Type): $Vec3f
public "isFirstPersonAnimationDisabled"(): boolean
public "getFirstPersonMode"(): $FirstPersonMode
public "setTickDelta"(tickDelta: float): void
public "getBend"(modelName: string): $Pair<(float), (float)>
get "active"(): boolean
get "firstPersonConfiguration"(): $FirstPersonConfiguration
get "firstPersonAnimationDisabled"(): boolean
get "firstPersonMode"(): $FirstPersonMode
set "tickDelta"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationProcessor$Type = ($AnimationProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationProcessor_ = $AnimationProcessor$Type;
}}
declare module "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationRegistry" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlayerAnimationRegistry {

constructor()

public static "serializeTextToString"(arg: string): string
public static "getAnimationOptional"(identifier: $ResourceLocation$Type): $Optional<($KeyframeAnimation)>
public static "getAnimation"(identifier: $ResourceLocation$Type): $KeyframeAnimation
public static "getAnimations"(): $Map<($ResourceLocation), ($KeyframeAnimation)>
public static "getModAnimations"(modid: string): $Map<(string), ($KeyframeAnimation)>
public static "resourceLoaderCallback"(manager: $ResourceManager$Type, logger: $Logger$Type): void
get "animations"(): $Map<($ResourceLocation), ($KeyframeAnimation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationRegistry$Type = ($PlayerAnimationRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationRegistry_ = $PlayerAnimationRegistry$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/forge/$HelperImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $HelperImpl {

constructor()

public static "isBendyLibPresent"(): boolean
get "bendyLibPresent"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HelperImpl$Type = ($HelperImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HelperImpl_ = $HelperImpl$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/$SoundPlayer" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$NBS, $NBS$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/$NBS"
import {$Layer$Note, $Layer$Note$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer$Note"

export class $SoundPlayer {

constructor(song: $NBS$Type, soundPlayer: $Consumer$Type<($Layer$Note$Type)>, tickToBegin: integer)

public "stop"(): void
public "tick"(): void
public static "isPlayingSong"(player: $SoundPlayer$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoundPlayer$Type = ($SoundPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoundPlayer_ = $SoundPlayer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/impl/event/$Event$Invoker" {
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $Event$Invoker<T> {

 "invoker"(arg0: $Iterable$Type<(T)>): T

(arg0: $Iterable$Type<(T)>): T
}

export namespace $Event$Invoker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Invoker$Type<T> = ($Event$Invoker<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event$Invoker_<T> = $Event$Invoker$Type<(T)>;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$KeyFrame" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"

export class $KeyframeAnimation$KeyFrame {
readonly "tick": integer
readonly "value": float
readonly "ease": $Ease

constructor(tick: integer, value: float)
constructor(tick: integer, value: float, ease: $Ease$Type)

public "equals"(other: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimation$KeyFrame$Type = ($KeyframeAnimation$KeyFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimation$KeyFrame_ = $KeyframeAnimation$KeyFrame$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/impl/event/$Event" {
import {$Event$Invoker, $Event$Invoker$Type} from "packages/dev/kosmx/playerAnim/core/impl/event/$Event$Invoker"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Event<T> {

constructor(clazz: $Class$Type<(T)>, invoker: $Event$Invoker$Type<(T)>)

public "register"(listener: T): void
public "invoker"(): T
public "unregister"(listener: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Type<T> = ($Event<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event_<T> = $Event$Type<(T)>;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$AnimationBinary" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"

export class $AnimationBinary {

constructor()

public static "getBoolean"(buf: $ByteBuffer$Type): boolean
public static "putBoolean"(byteBuffer: $ByteBuffer$Type, bl: boolean): void
public static "write"<T extends $ByteBuffer>(animation: $KeyframeAnimation$Type, buf: T, version: integer): T
public static "write"<T extends $ByteBuffer>(animation: $KeyframeAnimation$Type, buf: T): T
public static "read"(buf: $ByteBuffer$Type, version: integer): $KeyframeAnimation
public static "getString"(buf: $ByteBuffer$Type): string
public static "putString"(buf: $ByteBuffer$Type, str: string): void
public static "calculateSize"(animation: $KeyframeAnimation$Type, version: integer): integer
public static "getCurrentVersion"(): integer
get "currentVersion"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationBinary$Type = ($AnimationBinary);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationBinary_ = $AnimationBinary$Type;
}}
declare module "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationFactory" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$PlayerAnimationFactory$FactoryHolder, $PlayerAnimationFactory$FactoryHolder$Type} from "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationFactory$FactoryHolder"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"

export interface $PlayerAnimationFactory {

 "invoke"(arg0: $AbstractClientPlayer$Type): $IAnimation

(arg0: $AbstractClientPlayer$Type): $IAnimation
}

export namespace $PlayerAnimationFactory {
const ANIMATION_DATA_FACTORY: $PlayerAnimationFactory$FactoryHolder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationFactory$Type = ($PlayerAnimationFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationFactory_ = $PlayerAnimationFactory$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Pause" {
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"

export class $Pause implements $Playable {

constructor(len: integer)

public "playBackward"(time: integer): integer
public "playForward"(time: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pause$Type = ($Pause);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pause_ = $Pause$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/modifier/$AdjustmentModifier$PartModifier" {
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AdjustmentModifier$PartModifier {

constructor(rotation: $Vec3f$Type, offset: $Vec3f$Type)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "offset"(): $Vec3f
public "rotation"(): $Vec3f
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdjustmentModifier$PartModifier$Type = ($AdjustmentModifier$PartModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdjustmentModifier$PartModifier_ = $AdjustmentModifier$PartModifier$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$QuarkParsingError" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $QuarkParsingError extends $Exception {

constructor(message: string, i: integer)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuarkParsingError$Type = ($QuarkParsingError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuarkParsingError_ = $QuarkParsingError$Type;
}}
declare module "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationAccess" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$Event, $Event$Type} from "packages/dev/kosmx/playerAnim/core/impl/event/$Event"
import {$PlayerAnimationAccess$PlayerAssociatedAnimationData, $PlayerAnimationAccess$PlayerAssociatedAnimationData$Type} from "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationAccess$PlayerAssociatedAnimationData"
import {$PlayerAnimationAccess$AnimationRegister, $PlayerAnimationAccess$AnimationRegister$Type} from "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationAccess$AnimationRegister"
import {$AnimationStack, $AnimationStack$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack"

export class $PlayerAnimationAccess {
static readonly "REGISTER_ANIMATION_EVENT": $Event<($PlayerAnimationAccess$AnimationRegister)>

constructor()

public static "getPlayerAssociatedData"(player: $AbstractClientPlayer$Type): $PlayerAnimationAccess$PlayerAssociatedAnimationData
public static "getPlayerAnimLayer"(player: $AbstractClientPlayer$Type): $AnimationStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationAccess$Type = ($PlayerAnimationAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationAccess_ = $PlayerAnimationAccess$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$AnimationContainer" {
import {$TransformType, $TransformType$Type} from "packages/dev/kosmx/playerAnim/api/$TransformType"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$FirstPersonMode, $FirstPersonMode$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonMode"
import {$FirstPersonConfiguration, $FirstPersonConfiguration$Type} from "packages/dev/kosmx/playerAnim/api/firstPerson/$FirstPersonConfiguration"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $AnimationContainer<T extends $IAnimation> implements $IAnimation {

constructor(anim: T)
constructor()

public "isActive"(): boolean
public "tick"(): void
public "getFirstPersonConfiguration"(tickDelta: float): $FirstPersonConfiguration
public "get3DTransform"(modelName: string, type: $TransformType$Type, tickDelta: float, value0: $Vec3f$Type): $Vec3f
public "setupAnim"(tickDelta: float): void
public "getFirstPersonMode"(tickDelta: float): $FirstPersonMode
public "setAnim"(newAnim: T): void
public "getAnim"(): T
get "active"(): boolean
set "upAnim"(value: float)
set "anim"(value: T)
get "anim"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationContainer$Type<T> = ($AnimationContainer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationContainer_<T> = $AnimationContainer$Type<(T)>;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection$State" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"
import {$KeyframeAnimation$KeyFrame, $KeyframeAnimation$KeyFrame$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$KeyFrame"
import {$List, $List$Type} from "packages/java/util/$List"

export class $KeyframeAnimation$StateCollection$State {
readonly "defaultValue": float
readonly "threshold": float
readonly "name": string

constructor(state: $KeyframeAnimation$StateCollection$State$Type)

public "equals"(o: any): boolean
public "length"(): integer
public "hashCode"(): integer
public "replace"(keyFrame: $KeyframeAnimation$KeyFrame$Type, pos: integer): void
public "copy"(): $KeyframeAnimation$StateCollection$State
public "isEnabled"(): boolean
public "setEnabled"(newValue: boolean): void
public "addKeyFrame"(tick: integer, value: float, ease: $Ease$Type): boolean
public "addKeyFrame"(tick: integer, value: float, ease: $Ease$Type, rotate: integer, degrees: boolean): boolean
public "getKeyFrames"(): $List<($KeyframeAnimation$KeyFrame)>
public "replaceEase"(pos: integer, ease: $Ease$Type): void
public "lockAndVerify"(maxLength: integer): void
public "findAtTick"(tick: integer): integer
get "enabled"(): boolean
set "enabled"(value: boolean)
get "keyFrames"(): $List<($KeyframeAnimation$KeyFrame)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimation$StateCollection$State$Type = ($KeyframeAnimation$StateCollection$State);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimation$StateCollection$State_ = $KeyframeAnimation$StateCollection$State$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$InverseEase" {
import {$Ease, $Ease$Type} from "packages/dev/kosmx/playerAnim/core/util/$Ease"

export class $InverseEase {

constructor()

public static "inverse"(ease: $Ease$Type): $Ease
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InverseEase$Type = ($InverseEase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InverseEase_ = $InverseEase$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/gson/$AnimationJson" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$JsonSerializer, $JsonSerializer$Type} from "packages/com/google/gson/$JsonSerializer"
import {$JsonSerializationContext, $JsonSerializationContext$Type} from "packages/com/google/gson/$JsonSerializationContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"
import {$KeyframeAnimation, $KeyframeAnimation$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation"

export class $AnimationJson implements $JsonDeserializer<($List<($KeyframeAnimation)>)>, $JsonSerializer<($KeyframeAnimation)> {

constructor()

public "serialize"(emote: $KeyframeAnimation$Type, typeOfSrc: $Type$Type, context: $JsonSerializationContext$Type): $JsonElement
public static "getListedTypeToken"(): $Type
public static "asJson"(str: string): $JsonElement
public static "emoteSerializer"(emote: $KeyframeAnimation$Type): $JsonObject
public static "moveSerializer"(emote: $KeyframeAnimation$Type): $JsonArray
get "listedTypeToken"(): $Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationJson$Type = ($AnimationJson);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationJson_ = $AnimationJson$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Section" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$QuarkReader, $QuarkReader$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$QuarkReader"
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"

export class $Section implements $Playable {
 "isForward": boolean
readonly "elements": $List<($Playable)>

constructor(animData: $QuarkReader$Type, line: integer, text: $List$Type<($List$Type<(string)>)>)

public "playBackward"(time: integer): integer
public "playForward"(time: integer): integer
public "getLine"(): integer
public "setMoveOperator"(object: $Playable$Type): void
public "getMoveOperator"(): $Playable
get "line"(): integer
set "moveOperator"(value: $Playable$Type)
get "moveOperator"(): $Playable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Section$Type = ($Section);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Section_ = $Section$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Pauseable" {
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"

export class $Pauseable implements $Playable {

constructor(playable: $Playable$Type, len: integer)

public "playBackward"(time: integer): integer
public "playForward"(time: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pauseable$Type = ($Pauseable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pauseable_ = $Pauseable$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/quarktool/$Reset" {
import {$QuarkReader, $QuarkReader$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$QuarkReader"
import {$Playable, $Playable$Type} from "packages/dev/kosmx/playerAnim/core/data/quarktool/$Playable"

export class $Reset implements $Playable {

constructor(reader: $QuarkReader$Type, all: string, len: integer)

public "playBackward"(time: integer): integer
public "playForward"(time: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reset$Type = ($Reset);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reset_ = $Reset$Type;
}}
declare module "packages/dev/kosmx/playerAnim/impl/animation/$IBendHelper" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Pair, $Pair$Type} from "packages/dev/kosmx/playerAnim/core/util/$Pair"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"

export interface $IBendHelper {

 "bend"(arg0: $ModelPart$Type, arg1: $Pair$Type<(float), (float)>): void
 "bend"(arg0: $ModelPart$Type, arg1: float, arg2: float): void
 "initBend"(arg0: $ModelPart$Type, arg1: $Direction$Type): void
}

export namespace $IBendHelper {
const INSTANCE: $IBendHelper
function rotateMatrixStack(matrices: $PoseStack$Type, pair: $Pair$Type<(float), (float)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBendHelper$Type = ($IBendHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBendHelper_ = $IBendHelper$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/$AnimationFormat" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $AnimationFormat extends $Enum<($AnimationFormat)> {
static readonly "JSON_EMOTECRAFT": $AnimationFormat
static readonly "JSON_MC_ANIM": $AnimationFormat
static readonly "QUARK": $AnimationFormat
static readonly "BINARY": $AnimationFormat
static readonly "SERVER": $AnimationFormat
static readonly "UNKNOWN": $AnimationFormat


public static "values"(): ($AnimationFormat)[]
public static "valueOf"(name: string): $AnimationFormat
public "getExtension"(): string
get "extension"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnimationFormat$Type = (("server") | ("binary") | ("quark") | ("json_mc_anim") | ("json_emotecraft") | ("unknown")) | ($AnimationFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnimationFormat_ = $AnimationFormat$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer" {
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Layer$Note, $Layer$Note$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/format/$Layer$Note"

export class $Layer {
 "name": string
 "lock": byte
 "volume": byte
 "stereo": byte
readonly "notes": $ArrayList<($Layer$Note)>

constructor()

public "getLock"(): boolean
public "addNote"(tick: integer): $Layer$Note
public "setLock"(newValue: boolean): void
public "findAtTick"(tick: integer): integer
public "getNotesFrom"(fromTick: integer, toTick: integer): $ArrayList<($Layer$Note)>
public "getNotesFrom"(toTick: integer): $ArrayList<($Layer$Note)>
get "lock"(): boolean
set "lock"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Layer$Type = ($Layer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Layer_ = $Layer$Type;
}}
declare module "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$BodyPart" {
import {$KeyframeAnimationPlayer$Axis, $KeyframeAnimationPlayer$Axis$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$Axis"
import {$KeyframeAnimationPlayer$RotationAxis, $KeyframeAnimationPlayer$RotationAxis$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer$RotationAxis"
import {$Pair, $Pair$Type} from "packages/dev/kosmx/playerAnim/core/util/$Pair"
import {$KeyframeAnimationPlayer, $KeyframeAnimationPlayer$Type} from "packages/dev/kosmx/playerAnim/api/layered/$KeyframeAnimationPlayer"
import {$KeyframeAnimation$StateCollection, $KeyframeAnimation$StateCollection$Type} from "packages/dev/kosmx/playerAnim/core/data/$KeyframeAnimation$StateCollection"
import {$Vec3f, $Vec3f$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3f"

export class $KeyframeAnimationPlayer$BodyPart {
readonly "part": $KeyframeAnimation$StateCollection
readonly "x": $KeyframeAnimationPlayer$Axis
readonly "y": $KeyframeAnimationPlayer$Axis
readonly "z": $KeyframeAnimationPlayer$Axis
readonly "pitch": $KeyframeAnimationPlayer$RotationAxis
readonly "yaw": $KeyframeAnimationPlayer$RotationAxis
readonly "roll": $KeyframeAnimationPlayer$RotationAxis
readonly "bendAxis": $KeyframeAnimationPlayer$RotationAxis
readonly "bend": $KeyframeAnimationPlayer$RotationAxis

constructor(this$0: $KeyframeAnimationPlayer$Type, part: $KeyframeAnimation$StateCollection$Type)

public "getBodyOffset"(value0: $Vec3f$Type): $Vec3f
public "getBodyRotation"(value0: $Vec3f$Type): $Vec3f
public "getBend"(value0: $Pair$Type<(float), (float)>): $Pair<(float), (float)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyframeAnimationPlayer$BodyPart$Type = ($KeyframeAnimationPlayer$BodyPart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyframeAnimationPlayer$BodyPart_ = $KeyframeAnimationPlayer$BodyPart$Type;
}}
declare module "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationFactory$FactoryHolder" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$IAnimation, $IAnimation$Type} from "packages/dev/kosmx/playerAnim/api/layered/$IAnimation"
import {$AnimationStack, $AnimationStack$Type} from "packages/dev/kosmx/playerAnim/api/layered/$AnimationStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PlayerAnimationFactory, $PlayerAnimationFactory$Type} from "packages/dev/kosmx/playerAnim/minecraftApi/$PlayerAnimationFactory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlayerAnimationFactory$FactoryHolder {


public "prepareAnimations"(player: $AbstractClientPlayer$Type, playerStack: $AnimationStack$Type, animationMap: $Map$Type<($ResourceLocation$Type), ($IAnimation$Type)>): void
public "registerFactory"(id: $ResourceLocation$Type, priority: integer, factory: $PlayerAnimationFactory$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAnimationFactory$FactoryHolder$Type = ($PlayerAnimationFactory$FactoryHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAnimationFactory$FactoryHolder_ = $PlayerAnimationFactory$FactoryHolder$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/data/opennbs/$NBSFileUtils" {
import {$DataInputStream, $DataInputStream$Type} from "packages/java/io/$DataInputStream"
import {$NBS, $NBS$Type} from "packages/dev/kosmx/playerAnim/core/data/opennbs/$NBS"

export class $NBSFileUtils {

constructor()

public static "read"(stream: $DataInputStream$Type): $NBS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBSFileUtils$Type = ($NBSFileUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBSFileUtils_ = $NBSFileUtils$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$Vec3f" {
import {$Vector3, $Vector3$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vector3"
import {$Vec3d, $Vec3d$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vec3d"

export class $Vec3f extends $Vector3<(float)> {
static readonly "ZERO": $Vec3f

constructor(x: float, y: float, z: float)

public "add"(other: $Vec3f$Type): $Vec3f
public "scale"(scalar: float): $Vec3f
public "subtract"(rhs: $Vec3f$Type): $Vec3f
public "distanceTo"(vec3d: $Vec3d$Type): double
public "crossProduct"(other: $Vec3f$Type): $Vec3f
public "squaredDistanceTo"(vec3d: $Vec3d$Type): double
public "dotProduct"(other: $Vec3f$Type): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vec3f$Type = ($Vec3f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vec3f_ = $Vec3f$Type;
}}
declare module "packages/dev/kosmx/playerAnim/core/util/$Vec3d" {
import {$Vector3, $Vector3$Type} from "packages/dev/kosmx/playerAnim/core/util/$Vector3"

export class $Vec3d extends $Vector3<(double)> {

constructor(x: double, y: double, z: double)

public "add"(other: $Vec3d$Type): $Vec3d
public "scale"(scalar: double): $Vec3d
public "subtract"(rhs: $Vec3d$Type): $Vec3d
public "distanceTo"(vec3d: $Vec3d$Type): double
public "crossProduct"(other: $Vec3d$Type): $Vec3d
public "squaredDistanceTo"(vec3d: $Vec3d$Type): double
public "dotProduct"(other: $Vec3d$Type): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vec3d$Type = ($Vec3d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vec3d_ = $Vec3d$Type;
}}
