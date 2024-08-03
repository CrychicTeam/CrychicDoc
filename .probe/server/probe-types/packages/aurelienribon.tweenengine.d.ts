declare module "packages/aurelienribon/tweenengine/$BaseTween" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$TweenCallback, $TweenCallback$Type} from "packages/aurelienribon/tweenengine/$TweenCallback"
import {$TweenManager, $TweenManager$Type} from "packages/aurelienribon/tweenengine/$TweenManager"

export class $BaseTween<T> {

constructor()

public "update"(arg0: float): void
public "repeat"(arg0: integer, arg1: float): T
public "start"(arg0: $TweenManager$Type): T
public "start"(): T
public "resume"(): void
public "delay"(arg0: float): T
public "build"(): T
public "getDuration"(): float
public "free"(): void
public "getUserData"(): any
public "setUserData"(arg0: any): T
public "isPaused"(): boolean
public "getRepeatCount"(): integer
public "pause"(): void
public "getDelay"(): float
public "getCurrentTime"(): float
public "setCallback"(arg0: $TweenCallback$Type): T
public "isStarted"(): boolean
public "isInitialized"(): boolean
public "getStep"(): integer
public "kill"(): void
public "isFinished"(): boolean
public "setCallbackTriggers"(arg0: integer): T
public "addCallback"(arg0: $TweenCallback$Type): T
public "addCallback"(arg0: integer, arg1: $Consumer$Type<($BaseTween$Type<(any)>)>): T
public "getFullDuration"(): float
public "repeatYoyo"(arg0: integer, arg1: float): T
public "isYoyo"(): boolean
public "getRepeatDelay"(): float
get "duration"(): float
get "userData"(): any
set "userData"(value: any)
get "paused"(): boolean
get "repeatCount"(): integer
get "currentTime"(): float
set "callback"(value: $TweenCallback$Type)
get "started"(): boolean
get "initialized"(): boolean
get "step"(): integer
get "finished"(): boolean
set "callbackTriggers"(value: integer)
get "fullDuration"(): float
get "yoyo"(): boolean
get "repeatDelay"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseTween$Type<T> = ($BaseTween<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseTween_<T> = $BaseTween$Type<(T)>;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Back" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Back extends $TweenEquation {
static readonly "IN": $Back
static readonly "OUT": $Back
static readonly "INOUT": $Back

constructor()

public "s"(arg0: float): $Back
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Back$Type = ($Back);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Back_ = $Back$Type;
}}
declare module "packages/aurelienribon/tweenengine/$TweenPaths" {
import {$Linear, $Linear$Type} from "packages/aurelienribon/tweenengine/paths/$Linear"
import {$CatmullRom, $CatmullRom$Type} from "packages/aurelienribon/tweenengine/paths/$CatmullRom"

export interface $TweenPaths {

}

export namespace $TweenPaths {
const linear: $Linear
const catmullRom: $CatmullRom
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenPaths$Type = ($TweenPaths);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenPaths_ = $TweenPaths$Type;
}}
declare module "packages/aurelienribon/tweenengine/$Tween" {
import {$TweenPath, $TweenPath$Type} from "packages/aurelienribon/tweenengine/$TweenPath"
import {$BaseTween, $BaseTween$Type} from "packages/aurelienribon/tweenengine/$BaseTween"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TweenCallback, $TweenCallback$Type} from "packages/aurelienribon/tweenengine/$TweenCallback"
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"
import {$TweenAccessor, $TweenAccessor$Type} from "packages/aurelienribon/tweenengine/$TweenAccessor"

export class $Tween extends $BaseTween<($Tween)> {
static readonly "INFINITY": integer


public "target"(arg0: float): $Tween
public "target"(arg0: float, arg1: float, arg2: float): $Tween
public "target"(...arg0: (float)[]): $Tween
public "target"(arg0: float, arg1: float): $Tween
public "cast"(arg0: $Class$Type<(any)>): $Tween
public static "to"(arg0: any, arg1: integer, arg2: float): $Tween
public static "from"(arg0: any, arg1: integer, arg2: float): $Tween
public static "set"(arg0: any, arg1: integer): $Tween
public "getType"(): integer
public "getTarget"(): any
public static "mark"(): $Tween
public "path"(arg0: $TweenPath$Type): $Tween
public "getAccessor"(): $TweenAccessor<(any)>
public "build"(): $Tween
public static "getPoolSize"(): integer
public "free"(): void
public static "getVersion"(): string
public static "call"(arg0: $TweenCallback$Type): $Tween
public "ease"(arg0: $TweenEquation$Type): $Tween
public static "registerAccessor"(arg0: $Class$Type<(any)>, arg1: $TweenAccessor$Type<(any)>): void
public "getTargetClass"(): $Class<(any)>
public "waypoint"(...arg0: (float)[]): $Tween
public "waypoint"(arg0: float, arg1: float, arg2: float): $Tween
public "waypoint"(arg0: float, arg1: float): $Tween
public "waypoint"(arg0: float): $Tween
public static "setCombinedAttributesLimit"(arg0: integer): void
public "getCombinedAttributesCount"(): integer
public static "getRegisteredAccessor"(arg0: $Class$Type<(any)>): $TweenAccessor<(any)>
public static "ensurePoolCapacity"(arg0: integer): void
public "getTargetValues"(): (float)[]
public static "setWaypointsLimit"(arg0: integer): void
public "targetRelative"(...arg0: (float)[]): $Tween
public "targetRelative"(arg0: float, arg1: float, arg2: float): $Tween
public "targetRelative"(arg0: float, arg1: float): $Tween
public "targetRelative"(arg0: float): $Tween
public "getEasing"(): $TweenEquation
get "type"(): integer
get "accessor"(): $TweenAccessor<(any)>
get "poolSize"(): integer
get "version"(): string
get "targetClass"(): $Class<(any)>
set "combinedAttributesLimit"(value: integer)
get "combinedAttributesCount"(): integer
get "targetValues"(): (float)[]
set "waypointsLimit"(value: integer)
get "easing"(): $TweenEquation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tween$Type = ($Tween);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tween_ = $Tween$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Linear" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Linear extends $TweenEquation {
static readonly "INOUT": $Linear

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Linear$Type = ($Linear);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Linear_ = $Linear$Type;
}}
declare module "packages/aurelienribon/tweenengine/$Timeline" {
import {$BaseTween, $BaseTween$Type} from "packages/aurelienribon/tweenengine/$BaseTween"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Tween, $Tween$Type} from "packages/aurelienribon/tweenengine/$Tween"

export class $Timeline extends $BaseTween<($Timeline)> {


public "end"(): $Timeline
public "build"(): $Timeline
public "push"(arg0: $Tween$Type): $Timeline
public "push"(arg0: $Timeline$Type): $Timeline
public static "getPoolSize"(): integer
public "free"(): void
public "getChildren"(): $List<($BaseTween<(any)>)>
public static "createSequence"(): $Timeline
public static "createParallel"(): $Timeline
public "pushPause"(arg0: float): $Timeline
public "beginParallel"(): $Timeline
public "beginSequence"(): $Timeline
public static "ensurePoolCapacity"(arg0: integer): void
get "poolSize"(): integer
get "children"(): $List<($BaseTween<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Timeline$Type = ($Timeline);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Timeline_ = $Timeline$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Quad" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Quad extends $TweenEquation {
static readonly "IN": $Quad
static readonly "OUT": $Quad
static readonly "INOUT": $Quad

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quad$Type = ($Quad);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quad_ = $Quad$Type;
}}
declare module "packages/aurelienribon/tweenengine/paths/$Linear" {
import {$TweenPath, $TweenPath$Type} from "packages/aurelienribon/tweenengine/$TweenPath"

export class $Linear implements $TweenPath {

constructor()

public "compute"(arg0: float, arg1: (float)[], arg2: integer): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Linear$Type = ($Linear);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Linear_ = $Linear$Type;
}}
declare module "packages/aurelienribon/tweenengine/$TweenUtils" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $TweenUtils {

constructor()

public static "parseEasing"(arg0: string): $TweenEquation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenUtils$Type = ($TweenUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenUtils_ = $TweenUtils$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Cubic" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Cubic extends $TweenEquation {
static readonly "IN": $Cubic
static readonly "OUT": $Cubic
static readonly "INOUT": $Cubic

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cubic$Type = ($Cubic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cubic_ = $Cubic$Type;
}}
declare module "packages/aurelienribon/tweenengine/primitives/$MutableInteger" {
import {$TweenAccessor, $TweenAccessor$Type} from "packages/aurelienribon/tweenengine/$TweenAccessor"

export class $MutableInteger extends number implements $TweenAccessor<($MutableInteger)> {

constructor(arg0: integer)

public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "setValue"(arg0: integer): void
public "setValues"(arg0: $MutableInteger$Type, arg1: integer, arg2: (float)[]): void
public "getValues"(arg0: $MutableInteger$Type, arg1: integer, arg2: (float)[]): integer
set "value"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MutableInteger$Type = ($MutableInteger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MutableInteger_ = $MutableInteger$Type;
}}
declare module "packages/aurelienribon/tweenengine/$Pool" {
import {$Pool$Callback, $Pool$Callback$Type} from "packages/aurelienribon/tweenengine/$Pool$Callback"

export class $Pool<T> {

constructor(arg0: integer, arg1: $Pool$Callback$Type<(T)>)

public "get"(): T
public "clear"(): void
public "size"(): integer
public "ensureCapacity"(arg0: integer): void
public "free"(arg0: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pool$Type<T> = ($Pool<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pool_<T> = $Pool$Type<(T)>;
}}
declare module "packages/aurelienribon/tweenengine/$TweenEquations" {
import {$Bounce, $Bounce$Type} from "packages/aurelienribon/tweenengine/equations/$Bounce"
import {$Quad, $Quad$Type} from "packages/aurelienribon/tweenengine/equations/$Quad"
import {$Cubic, $Cubic$Type} from "packages/aurelienribon/tweenengine/equations/$Cubic"
import {$Quart, $Quart$Type} from "packages/aurelienribon/tweenengine/equations/$Quart"
import {$Circ, $Circ$Type} from "packages/aurelienribon/tweenengine/equations/$Circ"
import {$Back, $Back$Type} from "packages/aurelienribon/tweenengine/equations/$Back"
import {$Elastic, $Elastic$Type} from "packages/aurelienribon/tweenengine/equations/$Elastic"
import {$Quint, $Quint$Type} from "packages/aurelienribon/tweenengine/equations/$Quint"
import {$Expo, $Expo$Type} from "packages/aurelienribon/tweenengine/equations/$Expo"
import {$Linear, $Linear$Type} from "packages/aurelienribon/tweenengine/equations/$Linear"
import {$Sine, $Sine$Type} from "packages/aurelienribon/tweenengine/equations/$Sine"

export interface $TweenEquations {

}

export namespace $TweenEquations {
const easeNone: $Linear
const easeInQuad: $Quad
const easeOutQuad: $Quad
const easeInOutQuad: $Quad
const easeInCubic: $Cubic
const easeOutCubic: $Cubic
const easeInOutCubic: $Cubic
const easeInQuart: $Quart
const easeOutQuart: $Quart
const easeInOutQuart: $Quart
const easeInQuint: $Quint
const easeOutQuint: $Quint
const easeInOutQuint: $Quint
const easeInCirc: $Circ
const easeOutCirc: $Circ
const easeInOutCirc: $Circ
const easeInSine: $Sine
const easeOutSine: $Sine
const easeInOutSine: $Sine
const easeInExpo: $Expo
const easeOutExpo: $Expo
const easeInOutExpo: $Expo
const easeInBack: $Back
const easeOutBack: $Back
const easeInOutBack: $Back
const easeInBounce: $Bounce
const easeOutBounce: $Bounce
const easeInOutBounce: $Bounce
const easeInElastic: $Elastic
const easeOutElastic: $Elastic
const easeInOutElastic: $Elastic
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenEquations$Type = ($TweenEquations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenEquations_ = $TweenEquations$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Sine" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Sine extends $TweenEquation {
static readonly "IN": $Sine
static readonly "OUT": $Sine
static readonly "INOUT": $Sine

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Sine$Type = ($Sine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Sine_ = $Sine$Type;
}}
declare module "packages/aurelienribon/tweenengine/$Pool$Callback" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Pool$Callback<T> {

 "onUnPool"(arg0: T): void
 "onPool"(arg0: T): void
}

export namespace $Pool$Callback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pool$Callback$Type<T> = ($Pool$Callback<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pool$Callback_<T> = $Pool$Callback$Type<(T)>;
}}
declare module "packages/aurelienribon/tweenengine/$TweenAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TweenAccessor<T> {

 "setValues"(arg0: T, arg1: integer, arg2: (float)[]): void
 "getValues"(arg0: T, arg1: integer, arg2: (float)[]): integer
}

export namespace $TweenAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenAccessor$Type<T> = ($TweenAccessor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenAccessor_<T> = $TweenAccessor$Type<(T)>;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Bounce" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Bounce extends $TweenEquation {
static readonly "IN": $Bounce
static readonly "OUT": $Bounce
static readonly "INOUT": $Bounce

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bounce$Type = ($Bounce);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bounce_ = $Bounce$Type;
}}
declare module "packages/aurelienribon/tweenengine/$TweenPath" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TweenPath {

 "compute"(arg0: float, arg1: (float)[], arg2: integer): float

(arg0: float, arg1: (float)[], arg2: integer): float
}

export namespace $TweenPath {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenPath$Type = ($TweenPath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenPath_ = $TweenPath$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Circ" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Circ extends $TweenEquation {
static readonly "IN": $Circ
static readonly "OUT": $Circ
static readonly "INOUT": $Circ

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Circ$Type = ($Circ);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Circ_ = $Circ$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Quart" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Quart extends $TweenEquation {
static readonly "IN": $Quart
static readonly "OUT": $Quart
static readonly "INOUT": $Quart

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quart$Type = ($Quart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quart_ = $Quart$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Quint" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Quint extends $TweenEquation {
static readonly "IN": $Quint
static readonly "OUT": $Quint
static readonly "INOUT": $Quint

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quint$Type = ($Quint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quint_ = $Quint$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Expo" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Expo extends $TweenEquation {
static readonly "IN": $Expo
static readonly "OUT": $Expo
static readonly "INOUT": $Expo

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expo$Type = ($Expo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expo_ = $Expo$Type;
}}
declare module "packages/aurelienribon/tweenengine/$TweenEquation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TweenEquation {

constructor()

public "compute"(arg0: float): float
public "isValueOf"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenEquation$Type = ($TweenEquation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenEquation_ = $TweenEquation$Type;
}}
declare module "packages/aurelienribon/tweenengine/primitives/$MutableFloat" {
import {$TweenAccessor, $TweenAccessor$Type} from "packages/aurelienribon/tweenengine/$TweenAccessor"

export class $MutableFloat extends number implements $TweenAccessor<($MutableFloat)> {

constructor(arg0: float)

public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "setValue"(arg0: float): void
public "setValues"(arg0: $MutableFloat$Type, arg1: integer, arg2: (float)[]): void
public "getValues"(arg0: $MutableFloat$Type, arg1: integer, arg2: (float)[]): integer
set "value"(value: float)
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
declare module "packages/aurelienribon/tweenengine/$TweenCallback" {
import {$BaseTween, $BaseTween$Type} from "packages/aurelienribon/tweenengine/$BaseTween"

export interface $TweenCallback {

 "andThen"(arg0: $TweenCallback$Type): $TweenCallback
 "onEvent"(arg0: integer, arg1: $BaseTween$Type<(any)>): void

(arg0: $TweenCallback$Type): $TweenCallback
}

export namespace $TweenCallback {
const BEGIN: integer
const START: integer
const END: integer
const COMPLETE: integer
const BACK_BEGIN: integer
const BACK_START: integer
const BACK_END: integer
const BACK_COMPLETE: integer
const ANY_FORWARD: integer
const ANY_BACKWARD: integer
const ANY: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenCallback$Type = ($TweenCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenCallback_ = $TweenCallback$Type;
}}
declare module "packages/aurelienribon/tweenengine/equations/$Elastic" {
import {$TweenEquation, $TweenEquation$Type} from "packages/aurelienribon/tweenengine/$TweenEquation"

export class $Elastic extends $TweenEquation {
static readonly "IN": $Elastic
static readonly "OUT": $Elastic
static readonly "INOUT": $Elastic

constructor()

public "a"(arg0: float): $Elastic
public "p"(arg0: float): $Elastic
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Elastic$Type = ($Elastic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Elastic_ = $Elastic$Type;
}}
declare module "packages/aurelienribon/tweenengine/$TweenManager" {
import {$BaseTween, $BaseTween$Type} from "packages/aurelienribon/tweenengine/$BaseTween"
import {$List, $List$Type} from "packages/java/util/$List"

export class $TweenManager {

constructor()

public "getRunningTimelinesCount"(): integer
public "getRunningTweensCount"(): integer
public "add"(arg0: $BaseTween$Type<(any)>): $TweenManager
public "update"(arg0: float): void
public "size"(): integer
public "resume"(): void
public "ensureCapacity"(arg0: integer): void
public "getObjects"(): $List<($BaseTween<(any)>)>
public "pause"(): void
public static "setAutoRemove"(arg0: $BaseTween$Type<(any)>, arg1: boolean): void
public "killAll"(): void
public static "setAutoStart"(arg0: $BaseTween$Type<(any)>, arg1: boolean): void
public "killTarget"(arg0: any): void
public "killTarget"(arg0: any, arg1: integer): void
public "containsTarget"(arg0: any, arg1: integer): boolean
public "containsTarget"(arg0: any): boolean
get "runningTimelinesCount"(): integer
get "runningTweensCount"(): integer
get "objects"(): $List<($BaseTween<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweenManager$Type = ($TweenManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweenManager_ = $TweenManager$Type;
}}
declare module "packages/aurelienribon/tweenengine/paths/$CatmullRom" {
import {$TweenPath, $TweenPath$Type} from "packages/aurelienribon/tweenengine/$TweenPath"

export class $CatmullRom implements $TweenPath {

constructor()

public "compute"(arg0: float, arg1: (float)[], arg2: integer): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CatmullRom$Type = ($CatmullRom);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CatmullRom_ = $CatmullRom$Type;
}}
