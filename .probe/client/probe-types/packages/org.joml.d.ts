declare module "packages/org/joml/$Vector4f" {
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Vector4i, $Vector4i$Type} from "packages/org/joml/$Vector4i"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$Vector4ic, $Vector4ic$Type} from "packages/org/joml/$Vector4ic"
import {$Vector3ic, $Vector3ic$Type} from "packages/org/joml/$Vector3ic"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector4f implements $Externalizable, $Cloneable, $Vector4fc {
 "x": float
 "y": float
 "z": float
 "w": float

constructor(arg0: $ByteBuffer$Type)
constructor(arg0: (float)[])
constructor(arg0: float, arg1: float, arg2: float, arg3: float)
constructor(arg0: float)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: integer, arg1: $FloatBuffer$Type)
constructor(arg0: $Vector3fc$Type, arg1: float)
constructor(arg0: $Vector4ic$Type)
constructor(arg0: $Vector4fc$Type)
constructor()
constructor(arg0: $Vector2ic$Type, arg1: float, arg2: float)
constructor(arg0: $Vector2fc$Type, arg1: float, arg2: float)
constructor(arg0: $Vector3ic$Type, arg1: float)

public "add"(arg0: $Vector4fc$Type): $Vector4f
public "add"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "add"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector4f
public "add"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer): float
public "get"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
public "get"(arg0: $Vector4f$Type): $Vector4f
public "get"(arg0: $Vector4d$Type): $Vector4d
public "equals"(arg0: $Vector4fc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "equals"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "length"(): float
public static "length"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector4fc$Type): $Vector4f
public "min"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "max"(arg0: $Vector4fc$Type): $Vector4f
public "max"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "floor"(arg0: $Vector4f$Type): $Vector4f
public "floor"(): $Vector4f
public "ceil"(arg0: $Vector4f$Type): $Vector4f
public "ceil"(): $Vector4f
public "fma"(arg0: float, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
public "fma"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
public "fma"(arg0: float, arg1: $Vector4fc$Type): $Vector4f
public "fma"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type): $Vector4f
public "x"(): float
public "dot"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "dot"(arg0: $Vector4fc$Type): float
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector4f
public "set"(arg0: $ByteBuffer$Type): $Vector4f
public "set"(arg0: $Vector2ic$Type, arg1: float, arg2: float): $Vector4f
public "set"(arg0: $Vector2fc$Type, arg1: float, arg2: float): $Vector4f
public "set"(arg0: $Vector3ic$Type, arg1: float): $Vector4f
public "set"(arg0: $Vector4ic$Type): $Vector4f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector4f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Vector4f
public "set"(arg0: $FloatBuffer$Type): $Vector4f
public "set"(arg0: float): $Vector4f
public "set"(arg0: $Vector4dc$Type): $Vector4f
public "set"(arg0: double, arg1: double, arg2: double, arg3: double): $Vector4f
public "set"(arg0: (float)[]): $Vector4f
public "set"(arg0: $Vector3fc$Type, arg1: float): $Vector4f
public "set"(arg0: $Vector4fc$Type): $Vector4f
public "set"(arg0: double): $Vector4f
public "set"(arg0: float, arg1: float, arg2: float): $Vector4f
public "z"(): float
public "normalize"(arg0: float, arg1: $Vector4f$Type): $Vector4f
public "normalize"(): $Vector4f
public "normalize"(arg0: $Vector4f$Type): $Vector4f
public "normalize"(arg0: float): $Vector4f
public "zero"(): $Vector4f
public "w"(): float
public "y"(): float
public "isFinite"(): boolean
public "distance"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "distance"(arg0: $Vector4fc$Type): float
public static "distance"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): float
public "round"(arg0: $Vector4f$Type): $Vector4f
public "round"(): $Vector4f
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Vector4f$Type): $Vector4f
public "rotate"(arg0: $Quaternionfc$Type): $Vector4f
public "absolute"(arg0: $Vector4f$Type): $Vector4f
public "absolute"(): $Vector4f
public "sub"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "sub"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "sub"(arg0: $Vector4fc$Type): $Vector4f
public "sub"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector4f
public "negate"(arg0: $Vector4f$Type): $Vector4f
public "negate"(): $Vector4f
public "mulAdd"(arg0: float, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
public "mulAdd"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
public "mulAdd"(arg0: float, arg1: $Vector4fc$Type): $Vector4f
public "mulAdd"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type): $Vector4f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: float, arg1: $Vector4f$Type): $Vector4f
public "div"(arg0: float): $Vector4f
public "div"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "div"(arg0: $Vector4fc$Type): $Vector4f
public "div"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "div"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector4f
public "mul"(arg0: float): $Vector4f
public "mul"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector4f
public "mul"(arg0: float, arg1: $Vector4f$Type): $Vector4f
public "mul"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "mul"(arg0: $Matrix4x3fc$Type): $Vector4f
public "mul"(arg0: $Matrix4fc$Type): $Vector4f
public "mul"(arg0: $Matrix4x3fc$Type, arg1: $Vector4f$Type): $Vector4f
public "mul"(arg0: $Vector4fc$Type): $Vector4f
public "mul"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "mulAffine"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "mulAffineTranspose"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "normalize3"(arg0: $Vector4f$Type): $Vector4f
public "normalize3"(): $Vector4f
public "rotateAbout"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector4f
public "angle"(arg0: $Vector4fc$Type): float
public "setComponent"(arg0: integer, arg1: float): $Vector4f
public "setFromAddress"(arg0: long): $Vector4f
public "getToAddress"(arg0: long): $Vector4fc
public "mulProject"(arg0: $Matrix4fc$Type): $Vector4f
public "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulTranspose"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "mulTranspose"(arg0: $Matrix4fc$Type): $Vector4f
public "rotateZ"(arg0: float): $Vector4f
public "rotateZ"(arg0: float, arg1: $Vector4f$Type): $Vector4f
public "angleCos"(arg0: $Vector4fc$Type): float
public "rotateX"(arg0: float, arg1: $Vector4f$Type): $Vector4f
public "rotateX"(arg0: float): $Vector4f
public "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "lengthSquared"(): float
public static "lengthSquared"(arg0: float, arg1: float, arg2: float, arg3: float): float
public static "lengthSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): float
public "distanceSquared"(arg0: $Vector4fc$Type): float
public static "distanceSquared"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): float
public "distanceSquared"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "rotateY"(arg0: float, arg1: $Vector4f$Type): $Vector4f
public "rotateY"(arg0: float): $Vector4f
public "smoothStep"(arg0: $Vector4fc$Type, arg1: float, arg2: $Vector4f$Type): $Vector4f
public "minComponent"(): integer
public "maxComponent"(): integer
public "hermite"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4fc$Type, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "lerp"(arg0: $Vector4fc$Type, arg1: float): $Vector4f
public "lerp"(arg0: $Vector4fc$Type, arg1: float, arg2: $Vector4f$Type): $Vector4f
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4f$Type = ($Vector4f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4f_ = $Vector4f$Type;
}}
declare module "packages/org/joml/$Vector4i" {
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Vector4ic, $Vector4ic$Type} from "packages/org/joml/$Vector4ic"
import {$Vector3ic, $Vector3ic$Type} from "packages/org/joml/$Vector3ic"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector4i implements $Externalizable, $Cloneable, $Vector4ic {
 "x": integer
 "y": integer
 "z": integer
 "w": integer

constructor(arg0: $ByteBuffer$Type)
constructor(arg0: (integer)[])
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $IntBuffer$Type)
constructor(arg0: integer, arg1: $IntBuffer$Type)
constructor(arg0: $Vector2ic$Type, arg1: integer, arg2: integer)
constructor(arg0: $Vector3ic$Type, arg1: integer)
constructor(arg0: $Vector4ic$Type)
constructor()
constructor(arg0: $Vector4dc$Type, arg1: integer)
constructor(arg0: $Vector4fc$Type, arg1: integer)
constructor(arg0: $Vector3fc$Type, arg1: float, arg2: integer)

public "add"(arg0: $Vector4ic$Type): $Vector4i
public "add"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
public "add"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Vector4i
public "add"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Vector4i$Type): $Vector4i
public "get"(arg0: $IntBuffer$Type): $IntBuffer
public "get"(arg0: integer, arg1: $IntBuffer$Type): $IntBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer): integer
public "equals"(arg0: any): boolean
public "equals"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): boolean
public "length"(): double
public static "length"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): double
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
public "min"(arg0: $Vector4ic$Type): $Vector4i
public "max"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
public "max"(arg0: $Vector4ic$Type): $Vector4i
public "x"(): integer
public "dot"(arg0: $Vector4ic$Type): integer
public "set"(arg0: $Vector2ic$Type, arg1: integer, arg2: integer): $Vector4i
public "set"(arg0: integer): $Vector4i
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Vector4i
public "set"(arg0: $Vector4dc$Type, arg1: integer): $Vector4i
public "set"(arg0: $Vector3ic$Type, arg1: integer): $Vector4i
public "set"(arg0: $Vector4fc$Type, arg1: integer): $Vector4i
public "set"(arg0: $Vector4dc$Type): $Vector4i
public "set"(arg0: (integer)[]): $Vector4i
public "set"(arg0: $ByteBuffer$Type): $Vector4i
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector4i
public "set"(arg0: integer, arg1: $IntBuffer$Type): $Vector4i
public "set"(arg0: $IntBuffer$Type): $Vector4i
public "set"(arg0: $Vector4ic$Type): $Vector4i
public "z"(): integer
public "zero"(): $Vector4i
public "w"(): integer
public "y"(): integer
public static "distance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): double
public "distance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): double
public "distance"(arg0: $Vector4ic$Type): double
public "absolute"(): $Vector4i
public "absolute"(arg0: $Vector4i$Type): $Vector4i
public "sub"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
public "sub"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Vector4i
public "sub"(arg0: $Vector4ic$Type): $Vector4i
public "sub"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Vector4i$Type): $Vector4i
public "negate"(): $Vector4i
public "negate"(arg0: $Vector4i$Type): $Vector4i
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: float): $Vector4i
public "div"(arg0: $Vector4ic$Type): $Vector4i
public "div"(arg0: float, arg1: $Vector4i$Type): $Vector4i
public "div"(arg0: integer): $Vector4i
public "div"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
public "div"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
public "mul"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
public "mul"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
public "mul"(arg0: $Vector4ic$Type): $Vector4i
public "mul"(arg0: integer): $Vector4i
public "gridDistance"(arg0: $Vector4ic$Type): long
public "gridDistance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): long
public "setComponent"(arg0: integer, arg1: integer): $Vector4i
public "setFromAddress"(arg0: long): $Vector4i
public "getToAddress"(arg0: long): $Vector4ic
public static "lengthSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): long
public "lengthSquared"(): long
public static "distanceSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): long
public "distanceSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public "distanceSquared"(arg0: $Vector4ic$Type): integer
public "minComponent"(): integer
public "maxComponent"(): integer
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4i$Type = ($Vector4i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4i_ = $Vector4i$Type;
}}
declare module "packages/org/joml/$Vector4d" {
import {$Vector4ic, $Vector4ic$Type} from "packages/org/joml/$Vector4ic"
import {$Vector3ic, $Vector3ic$Type} from "packages/org/joml/$Vector3ic"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Vector4i, $Vector4i$Type} from "packages/org/joml/$Vector4i"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Vector4d implements $Externalizable, $Cloneable, $Vector4dc {
 "x": double
 "y": double
 "z": double
 "w": double

constructor(arg0: (double)[])
constructor(arg0: (float)[])
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor(arg0: $Vector2fc$Type, arg1: double, arg2: double)
constructor(arg0: $Vector3fc$Type, arg1: double)
constructor(arg0: integer, arg1: $DoubleBuffer$Type)
constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $ByteBuffer$Type)
constructor(arg0: $Vector3ic$Type, arg1: double)
constructor(arg0: $Vector3dc$Type, arg1: double)
constructor(arg0: $Vector4ic$Type)
constructor(arg0: $Vector4dc$Type)
constructor()
constructor(arg0: $Vector4fc$Type)
constructor(arg0: double)
constructor(arg0: $Vector2ic$Type, arg1: double, arg2: double)
constructor(arg0: $Vector2dc$Type, arg1: double, arg2: double)

public "add"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "add"(arg0: $Vector4fc$Type, arg1: $Vector4d$Type): $Vector4d
public "add"(arg0: $Vector4dc$Type): $Vector4d
public "add"(arg0: double, arg1: double, arg2: double, arg3: double): $Vector4d
public "add"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "add"(arg0: $Vector4fc$Type): $Vector4d
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer): double
public "get"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
public "get"(arg0: $Vector4f$Type): $Vector4f
public "get"(arg0: $Vector4d$Type): $Vector4d
public "equals"(arg0: $Vector4dc$Type, arg1: double): boolean
public "equals"(arg0: any): boolean
public "equals"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "length"(): double
public static "length"(arg0: double, arg1: double, arg2: double, arg3: double): double
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "min"(arg0: $Vector4dc$Type): $Vector4d
public "max"(arg0: $Vector4dc$Type): $Vector4d
public "max"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "floor"(arg0: $Vector4d$Type): $Vector4d
public "floor"(): $Vector4d
public "ceil"(arg0: $Vector4d$Type): $Vector4d
public "ceil"(): $Vector4d
public "fma"(arg0: double, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
public "fma"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
public "fma"(arg0: double, arg1: $Vector4dc$Type): $Vector4d
public "fma"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type): $Vector4d
public "x"(): double
public "dot"(arg0: double, arg1: double, arg2: double, arg3: double): double
public "dot"(arg0: $Vector4dc$Type): double
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector4d
public "set"(arg0: $Vector2dc$Type, arg1: double, arg2: double): $Vector4d
public "set"(arg0: $Vector3dc$Type, arg1: double): $Vector4d
public "set"(arg0: $Vector3ic$Type, arg1: double): $Vector4d
public "set"(arg0: double): $Vector4d
public "set"(arg0: $Vector3fc$Type, arg1: double): $Vector4d
public "set"(arg0: $Vector4ic$Type): $Vector4d
public "set"(arg0: $Vector4fc$Type): $Vector4d
public "set"(arg0: $Vector4dc$Type): $Vector4d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Vector4d
public "set"(arg0: $DoubleBuffer$Type): $Vector4d
public "set"(arg0: $Vector2ic$Type, arg1: double, arg2: double): $Vector4d
public "set"(arg0: $ByteBuffer$Type): $Vector4d
public "set"(arg0: double, arg1: double, arg2: double): $Vector4d
public "set"(arg0: (float)[]): $Vector4d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double): $Vector4d
public "set"(arg0: $Vector2fc$Type, arg1: double, arg2: double): $Vector4d
public "set"(arg0: (double)[]): $Vector4d
public "z"(): double
public "normalize"(arg0: double, arg1: $Vector4d$Type): $Vector4d
public "normalize"(arg0: double): $Vector4d
public "normalize"(): $Vector4d
public "normalize"(arg0: $Vector4d$Type): $Vector4d
public "zero"(): $Vector4d
public "w"(): double
public "y"(): double
public "isFinite"(): boolean
public "distance"(arg0: $Vector4dc$Type): double
public "distance"(arg0: double, arg1: double, arg2: double, arg3: double): double
public static "distance"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): double
public "round"(arg0: $Vector4d$Type): $Vector4d
public "round"(): $Vector4d
public "rotate"(arg0: $Quaterniondc$Type, arg1: $Vector4d$Type): $Vector4d
public "rotate"(arg0: $Quaterniondc$Type): $Vector4d
public "absolute"(): $Vector4d
public "absolute"(arg0: $Vector4d$Type): $Vector4d
public "sub"(arg0: $Vector4dc$Type): $Vector4d
public "sub"(arg0: $Vector4fc$Type, arg1: $Vector4d$Type): $Vector4d
public "sub"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "sub"(arg0: double, arg1: double, arg2: double, arg3: double): $Vector4d
public "sub"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "sub"(arg0: $Vector4fc$Type): $Vector4d
public "negate"(): $Vector4d
public "negate"(arg0: $Vector4d$Type): $Vector4d
public "mulAdd"(arg0: double, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
public "mulAdd"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type): $Vector4d
public "mulAdd"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
public "mulAdd"(arg0: double, arg1: $Vector4dc$Type): $Vector4d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: double, arg1: $Vector4d$Type): $Vector4d
public "div"(arg0: $Vector4dc$Type): $Vector4d
public "div"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "div"(arg0: double): $Vector4d
public "mul"(arg0: $Matrix4x3dc$Type, arg1: $Vector4d$Type): $Vector4d
public "mul"(arg0: $Matrix4x3fc$Type): $Vector4d
public "mul"(arg0: $Vector4fc$Type, arg1: $Vector4d$Type): $Vector4d
public "mul"(arg0: $Vector4fc$Type): $Vector4d
public "mul"(arg0: $Matrix4x3dc$Type): $Vector4d
public "mul"(arg0: $Matrix4dc$Type): $Vector4d
public "mul"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "mul"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "mul"(arg0: $Vector4dc$Type): $Vector4d
public "mul"(arg0: double): $Vector4d
public "mul"(arg0: double, arg1: $Vector4d$Type): $Vector4d
public "mul"(arg0: $Matrix4fc$Type, arg1: $Vector4d$Type): $Vector4d
public "mul"(arg0: $Matrix4fc$Type): $Vector4d
public "mul"(arg0: $Matrix4x3fc$Type, arg1: $Vector4d$Type): $Vector4d
public "mulAffine"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "mulAffineTranspose"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "normalize3"(): $Vector4d
public "normalize3"(arg0: $Vector4d$Type): $Vector4d
public "getf"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getf"(arg0: $ByteBuffer$Type): $ByteBuffer
public "angle"(arg0: $Vector4dc$Type): double
public "setComponent"(arg0: integer, arg1: double): $Vector4d
public "setFromAddress"(arg0: long): $Vector4d
public "getToAddress"(arg0: long): $Vector4dc
public "mulProject"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulProject"(arg0: $Matrix4dc$Type): $Vector4d
public "mulProject"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "mulTranspose"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "mulTranspose"(arg0: $Matrix4dc$Type): $Vector4d
public "rotateZ"(arg0: double, arg1: $Vector4d$Type): $Vector4d
public "rotateZ"(arg0: double): $Vector4d
public "angleCos"(arg0: $Vector4dc$Type): double
public "rotateX"(arg0: double): $Vector4d
public "rotateX"(arg0: double, arg1: $Vector4d$Type): $Vector4d
public "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double): $Vector4d
public "lengthSquared"(): double
public static "lengthSquared"(arg0: double, arg1: double, arg2: double, arg3: double): double
public static "distanceSquared"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): double
public "distanceSquared"(arg0: double, arg1: double, arg2: double, arg3: double): double
public "distanceSquared"(arg0: $Vector4dc$Type): double
public "rotateY"(arg0: double, arg1: $Vector4d$Type): $Vector4d
public "rotateY"(arg0: double): $Vector4d
public "smoothStep"(arg0: $Vector4dc$Type, arg1: double, arg2: $Vector4d$Type): $Vector4d
public "minComponent"(): integer
public "maxComponent"(): integer
public "hermite"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type, arg2: $Vector4dc$Type, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "lerp"(arg0: $Vector4dc$Type, arg1: double, arg2: $Vector4d$Type): $Vector4d
public "lerp"(arg0: $Vector4dc$Type, arg1: double): $Vector4d
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4d$Type = ($Vector4d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4d_ = $Vector4d$Type;
}}
declare module "packages/org/joml/$Vector3i" {
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$Vector3ic, $Vector3ic$Type} from "packages/org/joml/$Vector3ic"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector3i implements $Externalizable, $Cloneable, $Vector3ic {
 "x": integer
 "y": integer
 "z": integer

constructor(arg0: (integer)[])
constructor(arg0: $Vector3dc$Type, arg1: integer)
constructor(arg0: $Vector2dc$Type, arg1: float, arg2: integer)
constructor(arg0: $Vector3fc$Type, arg1: integer)
constructor(arg0: integer, arg1: $IntBuffer$Type)
constructor(arg0: $IntBuffer$Type)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $ByteBuffer$Type)
constructor(arg0: $Vector3ic$Type)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: integer)
constructor()
constructor(arg0: $Vector2fc$Type, arg1: float, arg2: integer)
constructor(arg0: double, arg1: double, arg2: double, arg3: integer)
constructor(arg0: float, arg1: float, arg2: float, arg3: integer)
constructor(arg0: $Vector2ic$Type, arg1: integer)

public "add"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
public "add"(arg0: $Vector3ic$Type): $Vector3i
public "add"(arg0: integer, arg1: integer, arg2: integer): $Vector3i
public "add"(arg0: integer, arg1: integer, arg2: integer, arg3: $Vector3i$Type): $Vector3i
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer): integer
public "get"(arg0: $IntBuffer$Type): $IntBuffer
public "get"(arg0: integer, arg1: $IntBuffer$Type): $IntBuffer
public "equals"(arg0: integer, arg1: integer, arg2: integer): boolean
public "equals"(arg0: any): boolean
public "length"(): double
public static "length"(arg0: integer, arg1: integer, arg2: integer): double
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
public "min"(arg0: $Vector3ic$Type): $Vector3i
public "max"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
public "max"(arg0: $Vector3ic$Type): $Vector3i
public "x"(): integer
public "set"(arg0: $Vector3dc$Type): $Vector3i
public "set"(arg0: $Vector3ic$Type): $Vector3i
public "set"(arg0: integer): $Vector3i
public "set"(arg0: integer, arg1: $IntBuffer$Type): $Vector3i
public "set"(arg0: $Vector2ic$Type, arg1: integer): $Vector3i
public "set"(arg0: $Vector3fc$Type, arg1: integer): $Vector3i
public "set"(arg0: integer, arg1: integer, arg2: integer): $Vector3i
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector3i
public "set"(arg0: $ByteBuffer$Type): $Vector3i
public "set"(arg0: $IntBuffer$Type): $Vector3i
public "set"(arg0: (integer)[]): $Vector3i
public "set"(arg0: $Vector3dc$Type, arg1: integer): $Vector3i
public "z"(): integer
public "zero"(): $Vector3i
public "y"(): integer
public "distance"(arg0: integer, arg1: integer, arg2: integer): double
public "distance"(arg0: $Vector3ic$Type): double
public static "distance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): double
public "absolute"(arg0: $Vector3i$Type): $Vector3i
public "absolute"(): $Vector3i
public "sub"(arg0: $Vector3ic$Type): $Vector3i
public "sub"(arg0: integer, arg1: integer, arg2: integer, arg3: $Vector3i$Type): $Vector3i
public "sub"(arg0: integer, arg1: integer, arg2: integer): $Vector3i
public "sub"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
public "negate"(): $Vector3i
public "negate"(arg0: $Vector3i$Type): $Vector3i
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: float, arg1: $Vector3i$Type): $Vector3i
public "div"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
public "div"(arg0: integer): $Vector3i
public "div"(arg0: float): $Vector3i
public "mul"(arg0: integer, arg1: integer, arg2: integer): $Vector3i
public "mul"(arg0: integer, arg1: integer, arg2: integer, arg3: $Vector3i$Type): $Vector3i
public "mul"(arg0: integer): $Vector3i
public "mul"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
public "mul"(arg0: $Vector3ic$Type): $Vector3i
public "mul"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
public "gridDistance"(arg0: integer, arg1: integer, arg2: integer): long
public "gridDistance"(arg0: $Vector3ic$Type): long
public "setComponent"(arg0: integer, arg1: integer): $Vector3i
public "setFromAddress"(arg0: long): $Vector3i
public "getToAddress"(arg0: long): $Vector3ic
public "lengthSquared"(): long
public static "lengthSquared"(arg0: integer, arg1: integer, arg2: integer): long
public "distanceSquared"(arg0: $Vector3ic$Type): long
public "distanceSquared"(arg0: integer, arg1: integer, arg2: integer): long
public static "distanceSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): long
public "minComponent"(): integer
public "maxComponent"(): integer
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3i$Type = ($Vector3i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3i_ = $Vector3i$Type;
}}
declare module "packages/org/joml/$Vector3f" {
import {$Vector3i, $Vector3i$Type} from "packages/org/joml/$Vector3i"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$Vector3ic, $Vector3ic$Type} from "packages/org/joml/$Vector3ic"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector3f implements $Externalizable, $Cloneable, $Vector3fc {
 "x": float
 "y": float
 "z": float

constructor(arg0: $Vector2ic$Type, arg1: float)
constructor(arg0: (float)[])
constructor(arg0: $ByteBuffer$Type)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: integer, arg1: $FloatBuffer$Type)
constructor()
constructor(arg0: float)
constructor(arg0: float, arg1: float, arg2: float)
constructor(arg0: $Vector2fc$Type, arg1: float)
constructor(arg0: $Vector3ic$Type)
constructor(arg0: $Vector3fc$Type)

public "add"(arg0: $Vector3fc$Type): $Vector3f
public "add"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "add"(arg0: float, arg1: float, arg2: float): $Vector3f
public "add"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer): float
public "get"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
public "get"(arg0: $Vector3f$Type): $Vector3f
public "get"(arg0: $Vector3d$Type): $Vector3d
public "equals"(arg0: any): boolean
public "equals"(arg0: float, arg1: float, arg2: float): boolean
public "equals"(arg0: $Vector3fc$Type, arg1: float): boolean
public static "length"(arg0: float, arg1: float, arg2: float): float
public "length"(): float
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector3fc$Type): $Vector3f
public "min"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "max"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "max"(arg0: $Vector3fc$Type): $Vector3f
public "floor"(): $Vector3f
public "floor"(arg0: $Vector3f$Type): $Vector3f
public "ceil"(arg0: $Vector3f$Type): $Vector3f
public "ceil"(): $Vector3f
public "fma"(arg0: float, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
public "fma"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
public "fma"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Vector3f
public "fma"(arg0: float, arg1: $Vector3fc$Type): $Vector3f
public "x"(): float
public "dot"(arg0: float, arg1: float, arg2: float): float
public "dot"(arg0: $Vector3fc$Type): float
public "set"(arg0: float, arg1: float, arg2: float): $Vector3f
public "set"(arg0: $Vector3dc$Type): $Vector3f
public "set"(arg0: double): $Vector3f
public "set"(arg0: $Vector3fc$Type): $Vector3f
public "set"(arg0: float): $Vector3f
public "set"(arg0: $Vector2dc$Type, arg1: float): $Vector3f
public "set"(arg0: $FloatBuffer$Type): $Vector3f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector3f
public "set"(arg0: $Vector2ic$Type, arg1: float): $Vector3f
public "set"(arg0: double, arg1: double, arg2: double): $Vector3f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Vector3f
public "set"(arg0: $Vector2fc$Type, arg1: float): $Vector3f
public "set"(arg0: (float)[]): $Vector3f
public "set"(arg0: $Vector3ic$Type): $Vector3f
public "set"(arg0: $ByteBuffer$Type): $Vector3f
public "z"(): float
public "normalize"(arg0: float, arg1: $Vector3f$Type): $Vector3f
public "normalize"(): $Vector3f
public "normalize"(arg0: $Vector3f$Type): $Vector3f
public "normalize"(arg0: float): $Vector3f
public "zero"(): $Vector3f
public "y"(): float
public "isFinite"(): boolean
public "distance"(arg0: float, arg1: float, arg2: float): float
public "distance"(arg0: $Vector3fc$Type): float
public static "distance"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): float
public "round"(arg0: $Vector3f$Type): $Vector3f
public "round"(): $Vector3f
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Vector3f$Type): $Vector3f
public "rotate"(arg0: $Quaternionfc$Type): $Vector3f
public "absolute"(arg0: $Vector3f$Type): $Vector3f
public "absolute"(): $Vector3f
public "sub"(arg0: float, arg1: float, arg2: float): $Vector3f
public "sub"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "sub"(arg0: $Vector3fc$Type): $Vector3f
public "sub"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "negate"(): $Vector3f
public "negate"(arg0: $Vector3f$Type): $Vector3f
public "mulAdd"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Vector3f
public "mulAdd"(arg0: float, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
public "mulAdd"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
public "mulAdd"(arg0: float, arg1: $Vector3fc$Type): $Vector3f
public "half"(arg0: float, arg1: float, arg2: float): $Vector3f
public "half"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "half"(arg0: $Vector3fc$Type): $Vector3f
public "half"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: float): $Vector3f
public "div"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "div"(arg0: float, arg1: float, arg2: float): $Vector3f
public "div"(arg0: $Vector3fc$Type): $Vector3f
public "div"(arg0: float, arg1: $Vector3f$Type): $Vector3f
public "div"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "mul"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "mul"(arg0: $Matrix3fc$Type): $Vector3f
public "mul"(arg0: $Matrix3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mul"(arg0: float): $Vector3f
public "mul"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mul"(arg0: $Vector3fc$Type): $Vector3f
public "mul"(arg0: float, arg1: $Vector3f$Type): $Vector3f
public "mul"(arg0: float, arg1: float, arg2: float): $Vector3f
public "mul"(arg0: $Matrix3x2fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mul"(arg0: $Matrix3dc$Type): $Vector3f
public "mul"(arg0: $Matrix3dc$Type, arg1: $Vector3f$Type): $Vector3f
public "mul"(arg0: $Matrix3x2fc$Type): $Vector3f
public "angle"(arg0: $Vector3fc$Type): float
public "setComponent"(arg0: integer, arg1: float): $Vector3f
public "setFromAddress"(arg0: long): $Vector3f
public "getToAddress"(arg0: long): $Vector3fc
public "mulPosition"(arg0: $Matrix4x3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulPosition"(arg0: $Matrix4fc$Type): $Vector3f
public "mulPosition"(arg0: $Matrix4x3fc$Type): $Vector3f
public "mulPosition"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulProject"(arg0: $Matrix4fc$Type, arg1: float, arg2: $Vector3f$Type): $Vector3f
public "mulProject"(arg0: $Matrix4fc$Type): $Vector3f
public "mulDirection"(arg0: $Matrix4x3fc$Type): $Vector3f
public "mulDirection"(arg0: $Matrix4fc$Type): $Vector3f
public "mulDirection"(arg0: $Matrix4dc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulDirection"(arg0: $Matrix4x3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulDirection"(arg0: $Matrix4dc$Type): $Vector3f
public "mulPositionW"(arg0: $Matrix4fc$Type): float
public "mulPositionW"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): float
public "mulTranspose"(arg0: $Matrix3fc$Type): $Vector3f
public "mulTranspose"(arg0: $Matrix3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "rotationTo"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
public "rotationTo"(arg0: $Vector3fc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "rotateZ"(arg0: float, arg1: $Vector3f$Type): $Vector3f
public "rotateZ"(arg0: float): $Vector3f
public "angleCos"(arg0: $Vector3fc$Type): float
public "rotateX"(arg0: float, arg1: $Vector3f$Type): $Vector3f
public "rotateX"(arg0: float): $Vector3f
public "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float): $Vector3f
public "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector3f$Type): $Vector3f
public static "lengthSquared"(arg0: float, arg1: float, arg2: float): float
public "lengthSquared"(): float
public "cross"(arg0: $Vector3fc$Type): $Vector3f
public "cross"(arg0: float, arg1: float, arg2: float): $Vector3f
public "cross"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "cross"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "distanceSquared"(arg0: float, arg1: float, arg2: float): float
public "distanceSquared"(arg0: $Vector3fc$Type): float
public static "distanceSquared"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): float
public "rotateY"(arg0: float): $Vector3f
public "rotateY"(arg0: float, arg1: $Vector3f$Type): $Vector3f
public "angleSigned"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): float
public "angleSigned"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): float
public "smoothStep"(arg0: $Vector3fc$Type, arg1: float, arg2: $Vector3f$Type): $Vector3f
public "orthogonalize"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "orthogonalize"(arg0: $Vector3fc$Type): $Vector3f
public "reflect"(arg0: float, arg1: float, arg2: float): $Vector3f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "reflect"(arg0: $Vector3fc$Type): $Vector3f
public "minComponent"(): integer
public "maxComponent"(): integer
public "orthogonalizeUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "orthogonalizeUnit"(arg0: $Vector3fc$Type): $Vector3f
public "hermite"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: float, arg4: $Vector3f$Type): $Vector3f
public "lerp"(arg0: $Vector3fc$Type, arg1: float, arg2: $Vector3f$Type): $Vector3f
public "lerp"(arg0: $Vector3fc$Type, arg1: float): $Vector3f
public "mulTransposeDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
public "mulTransposeDirection"(arg0: $Matrix4fc$Type): $Vector3f
public "mulTransposePosition"(arg0: $Matrix4fc$Type): $Vector3f
public "mulTransposePosition"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3f$Type = ($Vector3f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3f_ = $Vector3f$Type;
}}
declare module "packages/org/joml/$Vector3d" {
import {$Vector3i, $Vector3i$Type} from "packages/org/joml/$Vector3i"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3ic, $Vector3ic$Type} from "packages/org/joml/$Vector3ic"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Vector3d implements $Externalizable, $Cloneable, $Vector3dc {
 "x": double
 "y": double
 "z": double

constructor(arg0: (float)[])
constructor(arg0: (double)[])
constructor(arg0: $Vector2dc$Type, arg1: double)
constructor(arg0: $Vector3dc$Type)
constructor(arg0: integer, arg1: $DoubleBuffer$Type)
constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $ByteBuffer$Type)
constructor(arg0: $Vector3fc$Type)
constructor(arg0: double, arg1: double, arg2: double)
constructor(arg0: double)
constructor()
constructor(arg0: $Vector2ic$Type, arg1: double)
constructor(arg0: $Vector2fc$Type, arg1: double)
constructor(arg0: $Vector3ic$Type)

public "add"(arg0: $Vector3fc$Type): $Vector3d
public "add"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "add"(arg0: $Vector3dc$Type): $Vector3d
public "add"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "add"(arg0: double, arg1: double, arg2: double): $Vector3d
public "add"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer): double
public "get"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
public "get"(arg0: $Vector3f$Type): $Vector3f
public "get"(arg0: $Vector3d$Type): $Vector3d
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "equals"(arg0: $Vector3dc$Type, arg1: double): boolean
public "equals"(arg0: double, arg1: double, arg2: double): boolean
public "equals"(arg0: any): boolean
public static "length"(arg0: double, arg1: double, arg2: double): double
public "length"(): double
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector3dc$Type): $Vector3d
public "min"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "max"(arg0: $Vector3dc$Type): $Vector3d
public "max"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "floor"(): $Vector3d
public "floor"(arg0: $Vector3d$Type): $Vector3d
public "ceil"(): $Vector3d
public "ceil"(arg0: $Vector3d$Type): $Vector3d
public "fma"(arg0: double, arg1: $Vector3fc$Type): $Vector3d
public "fma"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Vector3d
public "fma"(arg0: double, arg1: $Vector3dc$Type): $Vector3d
public "fma"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Vector3d
public "fma"(arg0: double, arg1: $Vector3fc$Type, arg2: $Vector3d$Type): $Vector3d
public "fma"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3d$Type): $Vector3d
public "fma"(arg0: double, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
public "fma"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
public "fma"(arg0: $Vector3dc$Type, arg1: $Vector3fc$Type, arg2: $Vector3d$Type): $Vector3d
public "x"(): double
public "dot"(arg0: $Vector3dc$Type): double
public "dot"(arg0: double, arg1: double, arg2: double): double
public "set"(arg0: (float)[]): $Vector3d
public "set"(arg0: $Vector3ic$Type): $Vector3d
public "set"(arg0: double): $Vector3d
public "set"(arg0: $Vector2fc$Type, arg1: double): $Vector3d
public "set"(arg0: double, arg1: double, arg2: double): $Vector3d
public "set"(arg0: (double)[]): $Vector3d
public "set"(arg0: $Vector2dc$Type, arg1: double): $Vector3d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Vector3d
public "set"(arg0: $DoubleBuffer$Type): $Vector3d
public "set"(arg0: $Vector2ic$Type, arg1: double): $Vector3d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector3d
public "set"(arg0: $ByteBuffer$Type): $Vector3d
public "set"(arg0: $Vector3fc$Type): $Vector3d
public "set"(arg0: $Vector3dc$Type): $Vector3d
public "z"(): double
public "normalize"(arg0: double): $Vector3d
public "normalize"(arg0: double, arg1: $Vector3d$Type): $Vector3d
public "normalize"(arg0: $Vector3d$Type): $Vector3d
public "normalize"(): $Vector3d
public "zero"(): $Vector3d
public "y"(): double
public "isFinite"(): boolean
public "distance"(arg0: double, arg1: double, arg2: double): double
public "distance"(arg0: $Vector3dc$Type): double
public static "distance"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "round"(): $Vector3d
public "round"(arg0: $Vector3d$Type): $Vector3d
public "rotate"(arg0: $Quaterniondc$Type): $Vector3d
public "rotate"(arg0: $Quaterniondc$Type, arg1: $Vector3d$Type): $Vector3d
public "absolute"(arg0: $Vector3d$Type): $Vector3d
public "absolute"(): $Vector3d
public "sub"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "sub"(arg0: $Vector3fc$Type): $Vector3d
public "sub"(arg0: double, arg1: double, arg2: double): $Vector3d
public "sub"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "sub"(arg0: $Vector3dc$Type): $Vector3d
public "sub"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "negate"(arg0: $Vector3d$Type): $Vector3d
public "negate"(): $Vector3d
public "mulAdd"(arg0: double, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
public "mulAdd"(arg0: $Vector3fc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
public "mulAdd"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Vector3d
public "mulAdd"(arg0: double, arg1: $Vector3dc$Type): $Vector3d
public "mulAdd"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
public "half"(arg0: double, arg1: double, arg2: double): $Vector3d
public "half"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "half"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "half"(arg0: $Vector3dc$Type): $Vector3d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "div"(arg0: double, arg1: $Vector3d$Type): $Vector3d
public "div"(arg0: double, arg1: double, arg2: double): $Vector3d
public "div"(arg0: double): $Vector3d
public "div"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "div"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "div"(arg0: $Vector3fc$Type): $Vector3d
public "div"(arg0: $Vector3d$Type): $Vector3d
public "mul"(arg0: double, arg1: double, arg2: double): $Vector3d
public "mul"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "mul"(arg0: $Vector3dc$Type): $Vector3d
public "mul"(arg0: $Vector3fc$Type): $Vector3d
public "mul"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mul"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mul"(arg0: $Matrix3x2dc$Type): $Vector3d
public "mul"(arg0: $Matrix3dc$Type, arg1: $Vector3f$Type): $Vector3f
public "mul"(arg0: $Matrix3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mul"(arg0: $Matrix3x2dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mul"(arg0: $Matrix3x2fc$Type): $Vector3d
public "mul"(arg0: $Matrix3x2fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mul"(arg0: double, arg1: $Vector3d$Type): $Vector3d
public "mul"(arg0: double): $Vector3d
public "mul"(arg0: $Matrix3fc$Type): $Vector3d
public "mul"(arg0: $Matrix3dc$Type): $Vector3d
public "mul"(arg0: $Matrix3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "getf"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getf"(arg0: $ByteBuffer$Type): $ByteBuffer
public "angle"(arg0: $Vector3dc$Type): double
public "setComponent"(arg0: integer, arg1: double): $Vector3d
public "setFromAddress"(arg0: long): $Vector3d
public "getToAddress"(arg0: long): $Vector3dc
public "mulPosition"(arg0: $Matrix4x3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4fc$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4x3fc$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4dc$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4x3dc$Type): $Vector3d
public "mulPosition"(arg0: $Matrix4x3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulProject"(arg0: $Matrix4dc$Type, arg1: double, arg2: $Vector3d$Type): $Vector3d
public "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulProject"(arg0: $Matrix4dc$Type): $Vector3d
public "mulProject"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulProject"(arg0: $Matrix4fc$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4x3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4dc$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4x3dc$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4x3fc$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4fc$Type): $Vector3d
public "mulDirection"(arg0: $Matrix4x3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulPositionW"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): double
public "mulPositionW"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): double
public "mulPositionW"(arg0: $Matrix4dc$Type): double
public "mulPositionW"(arg0: $Matrix4fc$Type): double
public "mulTranspose"(arg0: $Matrix3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulTranspose"(arg0: $Matrix3fc$Type): $Vector3d
public "mulTranspose"(arg0: $Matrix3fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulTranspose"(arg0: $Matrix3dc$Type): $Vector3d
public "rotationTo"(arg0: $Vector3dc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "rotationTo"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
public "rotateZ"(arg0: double): $Vector3d
public "rotateZ"(arg0: double, arg1: $Vector3d$Type): $Vector3d
public "angleCos"(arg0: $Vector3dc$Type): double
public "rotateX"(arg0: double, arg1: $Vector3d$Type): $Vector3d
public "rotateX"(arg0: double): $Vector3d
public "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double): $Vector3d
public "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector3d$Type): $Vector3d
public static "lengthSquared"(arg0: double, arg1: double, arg2: double): double
public "lengthSquared"(): double
public "cross"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "cross"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "cross"(arg0: double, arg1: double, arg2: double): $Vector3d
public "cross"(arg0: $Vector3dc$Type): $Vector3d
public "distanceSquared"(arg0: $Vector3dc$Type): double
public static "distanceSquared"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "distanceSquared"(arg0: double, arg1: double, arg2: double): double
public "rotateY"(arg0: double, arg1: $Vector3d$Type): $Vector3d
public "rotateY"(arg0: double): $Vector3d
public "angleSigned"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "angleSigned"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): double
public "smoothStep"(arg0: $Vector3dc$Type, arg1: double, arg2: $Vector3d$Type): $Vector3d
public "orthogonalize"(arg0: $Vector3dc$Type): $Vector3d
public "orthogonalize"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "reflect"(arg0: double, arg1: double, arg2: double): $Vector3d
public "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "reflect"(arg0: $Vector3dc$Type): $Vector3d
public "minComponent"(): integer
public "maxComponent"(): integer
public "orthogonalizeUnit"(arg0: $Vector3dc$Type): $Vector3d
public "orthogonalizeUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "hermite"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: double, arg4: $Vector3d$Type): $Vector3d
public "lerp"(arg0: $Vector3dc$Type, arg1: double): $Vector3d
public "lerp"(arg0: $Vector3dc$Type, arg1: double, arg2: $Vector3d$Type): $Vector3d
public "mulTransposeDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulTransposeDirection"(arg0: $Matrix4dc$Type): $Vector3d
public "mulTransposeDirection"(arg0: $Matrix4fc$Type): $Vector3d
public "mulTransposeDirection"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulTransposePosition"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
public "mulTransposePosition"(arg0: $Matrix4fc$Type): $Vector3d
public "mulTransposePosition"(arg0: $Matrix4dc$Type): $Vector3d
public "mulTransposePosition"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3d$Type = ($Vector3d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3d_ = $Vector3d$Type;
}}
declare module "packages/org/joml/$Vector2i" {
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector2i implements $Externalizable, $Cloneable, $Vector2ic {
 "x": integer
 "y": integer

constructor(arg0: $ByteBuffer$Type)
constructor(arg0: (integer)[])
constructor(arg0: $Vector2dc$Type, arg1: integer)
constructor(arg0: $Vector2fc$Type, arg1: integer)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $IntBuffer$Type)
constructor(arg0: integer, arg1: $IntBuffer$Type)
constructor()
constructor(arg0: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: $Vector2ic$Type)
constructor(arg0: double, arg1: double, arg2: integer)
constructor(arg0: float, arg1: float, arg2: integer)

public "add"(arg0: integer, arg1: integer, arg2: $Vector2i$Type): $Vector2i
public "add"(arg0: integer, arg1: integer): $Vector2i
public "add"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
public "add"(arg0: $Vector2ic$Type): $Vector2i
public "get"(arg0: $IntBuffer$Type): $IntBuffer
public "get"(arg0: integer, arg1: $IntBuffer$Type): $IntBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer): integer
public "equals"(arg0: integer, arg1: integer): boolean
public "equals"(arg0: any): boolean
public static "length"(arg0: integer, arg1: integer): double
public "length"(): double
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector2ic$Type): $Vector2i
public "min"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
public "max"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
public "max"(arg0: $Vector2ic$Type): $Vector2i
public "x"(): integer
public "set"(arg0: $Vector2dc$Type): $Vector2i
public "set"(arg0: $Vector2fc$Type, arg1: integer): $Vector2i
public "set"(arg0: $Vector2dc$Type, arg1: integer): $Vector2i
public "set"(arg0: $IntBuffer$Type): $Vector2i
public "set"(arg0: integer, arg1: $IntBuffer$Type): $Vector2i
public "set"(arg0: integer): $Vector2i
public "set"(arg0: (integer)[]): $Vector2i
public "set"(arg0: $ByteBuffer$Type): $Vector2i
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector2i
public "set"(arg0: integer, arg1: integer): $Vector2i
public "set"(arg0: $Vector2ic$Type): $Vector2i
public "zero"(): $Vector2i
public "y"(): integer
public "distance"(arg0: integer, arg1: integer): double
public "distance"(arg0: $Vector2ic$Type): double
public static "distance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): double
public "absolute"(arg0: $Vector2i$Type): $Vector2i
public "absolute"(): $Vector2i
public "sub"(arg0: $Vector2ic$Type): $Vector2i
public "sub"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
public "sub"(arg0: integer, arg1: integer, arg2: $Vector2i$Type): $Vector2i
public "sub"(arg0: integer, arg1: integer): $Vector2i
public "negate"(arg0: $Vector2i$Type): $Vector2i
public "negate"(): $Vector2i
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: float): $Vector2i
public "div"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
public "div"(arg0: integer): $Vector2i
public "div"(arg0: float, arg1: $Vector2i$Type): $Vector2i
public "mul"(arg0: integer): $Vector2i
public "mul"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
public "mul"(arg0: $Vector2ic$Type): $Vector2i
public "mul"(arg0: integer, arg1: integer, arg2: $Vector2i$Type): $Vector2i
public "mul"(arg0: integer, arg1: integer): $Vector2i
public "mul"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
public "gridDistance"(arg0: $Vector2ic$Type): long
public "gridDistance"(arg0: integer, arg1: integer): long
public "setComponent"(arg0: integer, arg1: integer): $Vector2i
public "setFromAddress"(arg0: long): $Vector2i
public "getToAddress"(arg0: long): $Vector2ic
public "lengthSquared"(): long
public static "lengthSquared"(arg0: integer, arg1: integer): long
public "distanceSquared"(arg0: $Vector2ic$Type): long
public static "distanceSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): long
public "distanceSquared"(arg0: integer, arg1: integer): long
public "minComponent"(): integer
public "maxComponent"(): integer
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2i$Type = ($Vector2i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2i_ = $Vector2i$Type;
}}
declare module "packages/org/joml/$Vector2d" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Vector2i, $Vector2i$Type} from "packages/org/joml/$Vector2i"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector2d implements $Externalizable, $Cloneable, $Vector2dc {
 "x": double
 "y": double

constructor(arg0: (double)[])
constructor(arg0: (float)[])
constructor(arg0: $ByteBuffer$Type)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: integer, arg1: $DoubleBuffer$Type)
constructor()
constructor(arg0: double)
constructor(arg0: double, arg1: double)
constructor(arg0: $Vector2ic$Type)
constructor(arg0: $Vector2fc$Type)
constructor(arg0: $Vector2dc$Type)

public "add"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "add"(arg0: $Vector2fc$Type): $Vector2d
public "add"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "add"(arg0: $Vector2fc$Type, arg1: $Vector2d$Type): $Vector2d
public "add"(arg0: $Vector2dc$Type): $Vector2d
public "add"(arg0: double, arg1: double): $Vector2d
public "get"(arg0: integer): double
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $Vector2d$Type): $Vector2d
public "get"(arg0: $Vector2f$Type): $Vector2f
public "get"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "equals"(arg0: any): boolean
public "equals"(arg0: $Vector2dc$Type, arg1: double): boolean
public "equals"(arg0: double, arg1: double): boolean
public "length"(): double
public static "length"(arg0: double, arg1: double): double
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector2dc$Type): $Vector2d
public "min"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "max"(arg0: $Vector2dc$Type): $Vector2d
public "max"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "floor"(arg0: $Vector2d$Type): $Vector2d
public "floor"(): $Vector2d
public "ceil"(arg0: $Vector2d$Type): $Vector2d
public "ceil"(): $Vector2d
public "fma"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type, arg2: $Vector2d$Type): $Vector2d
public "fma"(arg0: double, arg1: $Vector2dc$Type, arg2: $Vector2d$Type): $Vector2d
public "fma"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type): $Vector2d
public "fma"(arg0: double, arg1: $Vector2dc$Type): $Vector2d
public "x"(): double
public "dot"(arg0: $Vector2dc$Type): double
public "set"(arg0: $Vector2dc$Type): $Vector2d
public "set"(arg0: double, arg1: double): $Vector2d
public "set"(arg0: double): $Vector2d
public "set"(arg0: $Vector2fc$Type): $Vector2d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Vector2d
public "set"(arg0: (float)[]): $Vector2d
public "set"(arg0: $DoubleBuffer$Type): $Vector2d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector2d
public "set"(arg0: $ByteBuffer$Type): $Vector2d
public "set"(arg0: (double)[]): $Vector2d
public "set"(arg0: $Vector2ic$Type): $Vector2d
public "normalize"(arg0: double, arg1: $Vector2d$Type): $Vector2d
public "normalize"(): $Vector2d
public "normalize"(arg0: double): $Vector2d
public "normalize"(arg0: $Vector2d$Type): $Vector2d
public "zero"(): $Vector2d
public "y"(): double
public "isFinite"(): boolean
public "distance"(arg0: $Vector2fc$Type): double
public "distance"(arg0: $Vector2dc$Type): double
public "distance"(arg0: double, arg1: double): double
public static "distance"(arg0: double, arg1: double, arg2: double, arg3: double): double
public "round"(arg0: $Vector2d$Type): $Vector2d
public "round"(): $Vector2d
public "absolute"(): $Vector2d
public "absolute"(arg0: $Vector2d$Type): $Vector2d
public "sub"(arg0: $Vector2fc$Type): $Vector2d
public "sub"(arg0: $Vector2dc$Type): $Vector2d
public "sub"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "sub"(arg0: double, arg1: double): $Vector2d
public "sub"(arg0: $Vector2fc$Type, arg1: $Vector2d$Type): $Vector2d
public "sub"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "negate"(arg0: $Vector2d$Type): $Vector2d
public "negate"(): $Vector2d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: double, arg1: double): $Vector2d
public "div"(arg0: double, arg1: $Vector2d$Type): $Vector2d
public "div"(arg0: $Vector2fc$Type): $Vector2d
public "div"(arg0: double): $Vector2d
public "div"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "div"(arg0: $Vector2fc$Type, arg1: $Vector2d$Type): $Vector2d
public "div"(arg0: $Vector2d$Type): $Vector2d
public "div"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "mul"(arg0: $Vector2dc$Type): $Vector2d
public "mul"(arg0: $Matrix2fc$Type): $Vector2d
public "mul"(arg0: $Matrix2dc$Type): $Vector2d
public "mul"(arg0: $Matrix2fc$Type, arg1: $Vector2d$Type): $Vector2d
public "mul"(arg0: $Matrix2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "mul"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "mul"(arg0: double): $Vector2d
public "mul"(arg0: double, arg1: $Vector2d$Type): $Vector2d
public "mul"(arg0: double, arg1: double): $Vector2d
public "mul"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "perpendicular"(): $Vector2d
public "angle"(arg0: $Vector2dc$Type): double
public "setComponent"(arg0: integer, arg1: double): $Vector2d
public "setFromAddress"(arg0: long): $Vector2d
public "getToAddress"(arg0: long): $Vector2dc
public "mulPosition"(arg0: $Matrix3x2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "mulPosition"(arg0: $Matrix3x2dc$Type): $Vector2d
public "mulDirection"(arg0: $Matrix3x2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "mulDirection"(arg0: $Matrix3x2dc$Type): $Vector2d
public "mulTranspose"(arg0: $Matrix2fc$Type): $Vector2d
public "mulTranspose"(arg0: $Matrix2fc$Type, arg1: $Vector2d$Type): $Vector2d
public "mulTranspose"(arg0: $Matrix2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "mulTranspose"(arg0: $Matrix2dc$Type): $Vector2d
public static "lengthSquared"(arg0: double, arg1: double): double
public "lengthSquared"(): double
public "distanceSquared"(arg0: double, arg1: double): double
public "distanceSquared"(arg0: $Vector2dc$Type): double
public static "distanceSquared"(arg0: double, arg1: double, arg2: double, arg3: double): double
public "distanceSquared"(arg0: $Vector2fc$Type): double
public "minComponent"(): integer
public "maxComponent"(): integer
public "lerp"(arg0: $Vector2dc$Type, arg1: double): $Vector2d
public "lerp"(arg0: $Vector2dc$Type, arg1: double, arg2: $Vector2d$Type): $Vector2d
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2d$Type = ($Vector2d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2d_ = $Vector2d$Type;
}}
declare module "packages/org/joml/$Vector2f" {
import {$Vector2i, $Vector2i$Type} from "packages/org/joml/$Vector2i"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector2ic, $Vector2ic$Type} from "packages/org/joml/$Vector2ic"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Vector2f implements $Externalizable, $Cloneable, $Vector2fc {
 "x": float
 "y": float

constructor(arg0: (float)[])
constructor(arg0: $ByteBuffer$Type)
constructor(arg0: integer, arg1: $ByteBuffer$Type)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: integer, arg1: $FloatBuffer$Type)
constructor()
constructor(arg0: float)
constructor(arg0: float, arg1: float)
constructor(arg0: $Vector2fc$Type)
constructor(arg0: $Vector2ic$Type)

public "add"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "add"(arg0: $Vector2fc$Type): $Vector2f
public "add"(arg0: float, arg1: float): $Vector2f
public "add"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "get"(arg0: integer): float
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
public "get"(arg0: $Vector2f$Type): $Vector2f
public "get"(arg0: $Vector2d$Type): $Vector2d
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "equals"(arg0: any): boolean
public "equals"(arg0: $Vector2fc$Type, arg1: float): boolean
public "equals"(arg0: float, arg1: float): boolean
public static "length"(arg0: float, arg1: float): float
public "length"(): float
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "min"(arg0: $Vector2fc$Type): $Vector2f
public "min"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "max"(arg0: $Vector2fc$Type): $Vector2f
public "max"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "floor"(): $Vector2f
public "floor"(arg0: $Vector2f$Type): $Vector2f
public "ceil"(arg0: $Vector2f$Type): $Vector2f
public "ceil"(): $Vector2f
public "fma"(arg0: float, arg1: $Vector2fc$Type): $Vector2f
public "fma"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type, arg2: $Vector2f$Type): $Vector2f
public "fma"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type): $Vector2f
public "fma"(arg0: float, arg1: $Vector2fc$Type, arg2: $Vector2f$Type): $Vector2f
public "x"(): float
public "dot"(arg0: $Vector2fc$Type): float
public "set"(arg0: double): $Vector2f
public "set"(arg0: float): $Vector2f
public "set"(arg0: float, arg1: float): $Vector2f
public "set"(arg0: $Vector2fc$Type): $Vector2f
public "set"(arg0: $ByteBuffer$Type): $Vector2f
public "set"(arg0: (float)[]): $Vector2f
public "set"(arg0: $Vector2dc$Type): $Vector2f
public "set"(arg0: $Vector2ic$Type): $Vector2f
public "set"(arg0: $FloatBuffer$Type): $Vector2f
public "set"(arg0: double, arg1: double): $Vector2f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Vector2f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Vector2f
public "normalize"(arg0: $Vector2f$Type): $Vector2f
public "normalize"(): $Vector2f
public "normalize"(arg0: float): $Vector2f
public "normalize"(arg0: float, arg1: $Vector2f$Type): $Vector2f
public "zero"(): $Vector2f
public "y"(): float
public "isFinite"(): boolean
public static "distance"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "distance"(arg0: $Vector2fc$Type): float
public "distance"(arg0: float, arg1: float): float
public "round"(arg0: $Vector2f$Type): $Vector2f
public "round"(): $Vector2f
public "absolute"(arg0: $Vector2f$Type): $Vector2f
public "absolute"(): $Vector2f
public "sub"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "sub"(arg0: float, arg1: float): $Vector2f
public "sub"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "sub"(arg0: $Vector2fc$Type): $Vector2f
public "negate"(arg0: $Vector2f$Type): $Vector2f
public "negate"(): $Vector2f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "div"(arg0: float): $Vector2f
public "div"(arg0: float, arg1: $Vector2f$Type): $Vector2f
public "div"(arg0: $Vector2fc$Type): $Vector2f
public "div"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "div"(arg0: float, arg1: float): $Vector2f
public "mul"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "mul"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "mul"(arg0: $Vector2fc$Type): $Vector2f
public "mul"(arg0: $Matrix2dc$Type, arg1: $Vector2f$Type): $Vector2f
public "mul"(arg0: $Matrix2dc$Type): $Vector2f
public "mul"(arg0: $Matrix2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "mul"(arg0: $Matrix2fc$Type): $Vector2f
public "mul"(arg0: float, arg1: float): $Vector2f
public "mul"(arg0: float, arg1: $Vector2f$Type): $Vector2f
public "mul"(arg0: float): $Vector2f
public "perpendicular"(): $Vector2f
public "angle"(arg0: $Vector2fc$Type): float
public "setComponent"(arg0: integer, arg1: float): $Vector2f
public "setFromAddress"(arg0: long): $Vector2f
public "getToAddress"(arg0: long): $Vector2fc
public "mulPosition"(arg0: $Matrix3x2fc$Type): $Vector2f
public "mulPosition"(arg0: $Matrix3x2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "mulDirection"(arg0: $Matrix3x2fc$Type): $Vector2f
public "mulDirection"(arg0: $Matrix3x2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "mulTranspose"(arg0: $Matrix2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "mulTranspose"(arg0: $Matrix2fc$Type): $Vector2f
public static "lengthSquared"(arg0: float, arg1: float): float
public "lengthSquared"(): float
public "distanceSquared"(arg0: $Vector2fc$Type): float
public static "distanceSquared"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "distanceSquared"(arg0: float, arg1: float): float
public "minComponent"(): integer
public "maxComponent"(): integer
public "lerp"(arg0: $Vector2fc$Type, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "lerp"(arg0: $Vector2fc$Type, arg1: float): $Vector2f
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2f$Type = ($Vector2f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2f_ = $Vector2f$Type;
}}
declare module "packages/org/joml/$FrustumIntersection" {
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"

export class $FrustumIntersection {
static readonly "PLANE_NX": integer
static readonly "PLANE_PX": integer
static readonly "PLANE_NY": integer
static readonly "PLANE_PY": integer
static readonly "PLANE_NZ": integer
static readonly "PLANE_PZ": integer
static readonly "INTERSECT": integer
static readonly "INSIDE": integer
static readonly "OUTSIDE": integer
static readonly "PLANE_MASK_NX": integer
static readonly "PLANE_MASK_PX": integer
static readonly "PLANE_MASK_NY": integer
static readonly "PLANE_MASK_PY": integer
static readonly "PLANE_MASK_NZ": integer
static readonly "PLANE_MASK_PZ": integer

constructor(arg0: $Matrix4fc$Type, arg1: boolean)
constructor(arg0: $Matrix4fc$Type)
constructor()

public "set"(arg0: $Matrix4fc$Type, arg1: boolean): $FrustumIntersection
public "set"(arg0: $Matrix4fc$Type): $FrustumIntersection
public "testPoint"(arg0: float, arg1: float, arg2: float): boolean
public "testPoint"(arg0: $Vector3fc$Type): boolean
public "testSphere"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "testSphere"(arg0: $Vector3fc$Type, arg1: float): boolean
public "testAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean
public "testAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): boolean
public "intersectAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: integer, arg3: integer): integer
public "intersectAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): integer
public "intersectAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer): integer
public "intersectAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: integer): integer
public "intersectAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer): integer
public "intersectAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): integer
public "intersectSphere"(arg0: float, arg1: float, arg2: float, arg3: float): integer
public "intersectSphere"(arg0: $Vector3fc$Type, arg1: float): integer
public "testPlaneXZ"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "testPlaneXY"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "testPlaneXY"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type): boolean
public "distanceToPlane"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer): float
public "testLineSegment"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): boolean
public "testLineSegment"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrustumIntersection$Type = ($FrustumIntersection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrustumIntersection_ = $FrustumIntersection$Type;
}}
declare module "packages/org/joml/$AxisAngle4f" {
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $AxisAngle4f implements $Externalizable, $Cloneable {
 "angle": float
 "x": float
 "y": float
 "z": float

constructor(arg0: float, arg1: $Vector3fc$Type)
constructor(arg0: float, arg1: float, arg2: float, arg3: float)
constructor(arg0: $Quaternionfc$Type)
constructor(arg0: $AxisAngle4f$Type)
constructor()

public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: $Matrix3f$Type): $Matrix3f
public "get"(arg0: $Quaterniond$Type): $Quaterniond
public "get"(arg0: $Quaternionf$Type): $Quaternionf
public "get"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "get"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "get"(arg0: $Matrix3d$Type): $Matrix3d
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "equals"(arg0: any): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector4f$Type): $Vector4f
public "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "set"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "set"(arg0: $Matrix4fc$Type): $AxisAngle4f
public "set"(arg0: float, arg1: $Vector3fc$Type): $AxisAngle4f
public "set"(arg0: $Matrix4x3fc$Type): $AxisAngle4f
public "set"(arg0: $Quaterniondc$Type): $AxisAngle4f
public "set"(arg0: $Matrix3fc$Type): $AxisAngle4f
public "set"(arg0: $Matrix3dc$Type): $AxisAngle4f
public "set"(arg0: $Matrix4dc$Type): $AxisAngle4f
public "set"(arg0: $AxisAngle4d$Type): $AxisAngle4f
public "set"(arg0: $Quaternionfc$Type): $AxisAngle4f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float): $AxisAngle4f
public "normalize"(): $AxisAngle4f
public "rotate"(arg0: float): $AxisAngle4f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AxisAngle4f$Type = ($AxisAngle4f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AxisAngle4f_ = $AxisAngle4f$Type;
}}
declare module "packages/org/joml/$AxisAngle4d" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $AxisAngle4d implements $Externalizable, $Cloneable {
 "angle": double
 "x": double
 "y": double
 "z": double

constructor(arg0: $Quaterniondc$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor(arg0: double, arg1: $Vector3dc$Type)
constructor(arg0: double, arg1: $Vector3f$Type)
constructor()
constructor(arg0: $AxisAngle4d$Type)
constructor(arg0: $AxisAngle4f$Type)
constructor(arg0: $Quaternionfc$Type)

public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: $Matrix3f$Type): $Matrix3f
public "get"(arg0: $Quaterniond$Type): $Quaterniond
public "get"(arg0: $Quaternionf$Type): $Quaternionf
public "get"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "get"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "get"(arg0: $Matrix3d$Type): $Matrix3d
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "equals"(arg0: any): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "transform"(arg0: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "set"(arg0: $Matrix3dc$Type): $AxisAngle4d
public "set"(arg0: $Quaterniondc$Type): $AxisAngle4d
public "set"(arg0: $Quaternionfc$Type): $AxisAngle4d
public "set"(arg0: $Matrix4fc$Type): $AxisAngle4d
public "set"(arg0: double, arg1: $Vector3dc$Type): $AxisAngle4d
public "set"(arg0: $Matrix3fc$Type): $AxisAngle4d
public "set"(arg0: $Matrix4dc$Type): $AxisAngle4d
public "set"(arg0: $Matrix4x3fc$Type): $AxisAngle4d
public "set"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "set"(arg0: double, arg1: $Vector3f$Type): $AxisAngle4d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double): $AxisAngle4d
public "set"(arg0: $AxisAngle4f$Type): $AxisAngle4d
public "normalize"(): $AxisAngle4d
public "rotate"(arg0: double): $AxisAngle4d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AxisAngle4d$Type = ($AxisAngle4d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AxisAngle4d_ = $AxisAngle4d$Type;
}}
declare module "packages/org/joml/$Matrix3x2d" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Matrix3x2d implements $Matrix3x2dc, $Cloneable, $Externalizable {
 "m00": double
 "m01": double
 "m10": double
 "m11": double
 "m20": double
 "m21": double

constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double)
constructor(arg0: $Matrix3x2dc$Type)
constructor()
constructor(arg0: $Matrix2dc$Type)
constructor(arg0: $Matrix2fc$Type)

public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: (double)[], arg1: integer): (double)[]
public "get"(arg0: (double)[]): (double)[]
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $Matrix3x2d$Type): $Matrix3x2d
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "equals"(arg0: $Matrix3x2dc$Type, arg1: double): boolean
public "equals"(arg0: any): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: $Vector2fc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "scale"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "scale"(arg0: double): $Matrix3x2d
public "scale"(arg0: $Vector2fc$Type): $Matrix3x2d
public "scale"(arg0: double, arg1: double): $Matrix3x2d
public "scale"(arg0: $Vector2dc$Type): $Matrix3x2d
public "scale"(arg0: $Vector2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "scale"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3d$Type): $Vector3d
public "identity"(): $Matrix3x2d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix3x2d
public "set"(arg0: $Matrix3x2dc$Type): $Matrix3x2d
public "set"(arg0: $ByteBuffer$Type): $Matrix3x2d
public "set"(arg0: $DoubleBuffer$Type): $Matrix3x2d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Matrix3x2d
public "set"(arg0: $Matrix2dc$Type): $Matrix3x2d
public "set"(arg0: $Matrix2fc$Type): $Matrix3x2d
public "set"(arg0: (double)[]): $Matrix3x2d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix3x2d
public "zero"(): $Matrix3x2d
public "isFinite"(): boolean
public "rotate"(arg0: double): $Matrix3x2d
public "rotate"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "origin"(arg0: $Vector2d$Type): $Vector2d
public "view"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix3x2d
public "view"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3x2d$Type): $Matrix3x2d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix3x2dc$Type): $Matrix3x2d
public "mul"(arg0: $Matrix3x2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "m10"(): double
public "m11"(): double
public "translation"(arg0: $Vector2dc$Type): $Matrix3x2d
public "translation"(arg0: double, arg1: double): $Matrix3x2d
public "scaling"(arg0: double): $Matrix3x2d
public "scaling"(arg0: double, arg1: double): $Matrix3x2d
public "invert"(arg0: $Matrix3x2d$Type): $Matrix3x2d
public "invert"(): $Matrix3x2d
public "translate"(arg0: $Vector2dc$Type): $Matrix3x2d
public "translate"(arg0: $Vector2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "translate"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
public "translate"(arg0: double, arg1: double): $Matrix3x2d
public "rotation"(arg0: double): $Matrix3x2d
public "positiveX"(arg0: $Vector2d$Type): $Vector2d
public "rotateTo"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type): $Matrix3x2d
public "rotateTo"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type, arg2: $Matrix3x2d$Type): $Matrix3x2d
public "positiveY"(arg0: $Vector2d$Type): $Vector2d
public "testPoint"(arg0: double, arg1: double): boolean
public "mulLocal"(arg0: $Matrix3x2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "mulLocal"(arg0: $Matrix3x2dc$Type): $Matrix3x2d
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "determinant"(): double
public "setTranslation"(arg0: double, arg1: double): $Matrix3x2d
public "setTranslation"(arg0: $Vector2dc$Type): $Matrix3x2d
public "get3x3"(arg0: (double)[], arg1: integer): (double)[]
public "get3x3"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get3x3"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get3x3"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get3x3"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get3x3"(arg0: (double)[]): (double)[]
public "scaleAround"(arg0: double, arg1: double, arg2: double): $Matrix3x2d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix3x2d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3x2d$Type): $Matrix3x2d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3x2d$Type): $Matrix3x2d
public "transformPosition"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "transformPosition"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "transformPosition"(arg0: $Vector2d$Type): $Vector2d
public "transformDirection"(arg0: $Vector2d$Type): $Vector2d
public "transformDirection"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "transformDirection"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "scaleLocal"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
public "scaleLocal"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "scaleLocal"(arg0: double, arg1: double): $Matrix3x2d
public "scaleLocal"(arg0: double): $Matrix3x2d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double): $Matrix3x2d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix3x2d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3x2d$Type): $Matrix3x2d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3x2d$Type): $Matrix3x2d
public "rotateLocal"(arg0: double): $Matrix3x2d
public "rotateLocal"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "translateLocal"(arg0: double, arg1: double): $Matrix3x2d
public "translateLocal"(arg0: $Vector2dc$Type): $Matrix3x2d
public "translateLocal"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
public "translateLocal"(arg0: $Vector2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
public "unproject"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector2d$Type): $Vector2d
public "unprojectInv"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector2d$Type): $Vector2d
public "rotateAbout"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3x2d$Type): $Matrix3x2d
public "rotateAbout"(arg0: double, arg1: double, arg2: double): $Matrix3x2d
public "viewArea"(arg0: (double)[]): (double)[]
public "testCircle"(arg0: double, arg1: double, arg2: double): boolean
public "testAar"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "get4x4"(arg0: (double)[], arg1: integer): (double)[]
public "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get4x4"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get4x4"(arg0: (double)[]): (double)[]
public "m20"(): double
public "m21"(): double
public "setView"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix3x2d
public "span"(arg0: $Vector2d$Type, arg1: $Vector2d$Type, arg2: $Vector2d$Type): $Matrix3x2d
public "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "setFromAddress"(arg0: long): $Matrix3x2d
public "getToAddress"(arg0: long): $Matrix3x2dc
public "m00"(): double
public "m01"(): double
public "normalizedPositiveY"(arg0: $Vector2d$Type): $Vector2d
public "normalizedPositiveX"(arg0: $Vector2d$Type): $Vector2d
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3x2d$Type = ($Matrix3x2d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3x2d_ = $Matrix3x2d$Type;
}}
declare module "packages/org/joml/$Matrix3x2f" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Matrix3x2f implements $Matrix3x2fc, $Externalizable, $Cloneable {
 "m00": float
 "m01": float
 "m10": float
 "m11": float
 "m20": float
 "m21": float

constructor(arg0: $FloatBuffer$Type)
constructor(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float)
constructor(arg0: $Matrix2fc$Type)
constructor(arg0: $Matrix3x2fc$Type)
constructor()

public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $Matrix3x2f$Type): $Matrix3x2f
public "get"(arg0: (float)[]): (float)[]
public "equals"(arg0: $Matrix3x2fc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "scale"(arg0: $Vector2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "scale"(arg0: $Vector2fc$Type): $Matrix3x2f
public "scale"(arg0: float, arg1: float): $Matrix3x2f
public "scale"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
public "scale"(arg0: float): $Matrix3x2f
public "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3f$Type, arg1: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "identity"(): $Matrix3x2f
public "set"(arg0: $Matrix3x2fc$Type): $Matrix3x2f
public "set"(arg0: $FloatBuffer$Type): $Matrix3x2f
public "set"(arg0: $ByteBuffer$Type): $Matrix3x2f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix3x2f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix3x2f
public "set"(arg0: (float)[]): $Matrix3x2f
public "set"(arg0: $Matrix2fc$Type): $Matrix3x2f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix3x2f
public "zero"(): $Matrix3x2f
public "isFinite"(): boolean
public "rotate"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "rotate"(arg0: float): $Matrix3x2f
public "origin"(arg0: $Vector2f$Type): $Vector2f
public "view"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix3x2f
public "view"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3x2f$Type): $Matrix3x2f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix3x2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "mul"(arg0: $Matrix3x2fc$Type): $Matrix3x2f
public "m10"(): float
public "m11"(): float
public "translation"(arg0: $Vector2fc$Type): $Matrix3x2f
public "translation"(arg0: float, arg1: float): $Matrix3x2f
public "scaling"(arg0: float, arg1: float): $Matrix3x2f
public "scaling"(arg0: float): $Matrix3x2f
public "invert"(): $Matrix3x2f
public "invert"(arg0: $Matrix3x2f$Type): $Matrix3x2f
public "translate"(arg0: $Vector2fc$Type): $Matrix3x2f
public "translate"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
public "translate"(arg0: $Vector2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "translate"(arg0: float, arg1: float): $Matrix3x2f
public "rotation"(arg0: float): $Matrix3x2f
public "positiveX"(arg0: $Vector2f$Type): $Vector2f
public "rotateTo"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type, arg2: $Matrix3x2f$Type): $Matrix3x2f
public "rotateTo"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type): $Matrix3x2f
public "positiveY"(arg0: $Vector2f$Type): $Vector2f
public "testPoint"(arg0: float, arg1: float): boolean
public "mulLocal"(arg0: $Matrix3x2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "mulLocal"(arg0: $Matrix3x2fc$Type): $Matrix3x2f
public "determinant"(): float
public "setTranslation"(arg0: $Vector2f$Type): $Matrix3x2f
public "setTranslation"(arg0: float, arg1: float): $Matrix3x2f
public "get3x3"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get3x3"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get3x3"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get3x3"(arg0: (float)[]): (float)[]
public "get3x3"(arg0: (float)[], arg1: integer): (float)[]
public "get3x3"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "scaleAround"(arg0: float, arg1: float, arg2: float): $Matrix3x2f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3x2f$Type): $Matrix3x2f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3x2f$Type): $Matrix3x2f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix3x2f
public "transformPosition"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "transformPosition"(arg0: $Vector2f$Type): $Vector2f
public "transformPosition"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "transformDirection"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "transformDirection"(arg0: $Vector2f$Type): $Vector2f
public "transformDirection"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "scaleLocal"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
public "scaleLocal"(arg0: float): $Matrix3x2f
public "scaleLocal"(arg0: float, arg1: float): $Matrix3x2f
public "scaleLocal"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3x2f$Type): $Matrix3x2f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float): $Matrix3x2f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3x2f$Type): $Matrix3x2f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix3x2f
public "rotateLocal"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "rotateLocal"(arg0: float): $Matrix3x2f
public "translateLocal"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
public "translateLocal"(arg0: $Vector2fc$Type): $Matrix3x2f
public "translateLocal"(arg0: float, arg1: float): $Matrix3x2f
public "translateLocal"(arg0: $Vector2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "unproject"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector2f$Type): $Vector2f
public "unprojectInv"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector2f$Type): $Vector2f
public "rotateAbout"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3x2f$Type): $Matrix3x2f
public "rotateAbout"(arg0: float, arg1: float, arg2: float): $Matrix3x2f
public "viewArea"(arg0: (float)[]): (float)[]
public "testCircle"(arg0: float, arg1: float, arg2: float): boolean
public "shearY"(arg0: float): $Matrix3x2f
public "shearY"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "shearX"(arg0: float): $Matrix3x2f
public "shearX"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
public "testAar"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "get4x4"(arg0: (float)[], arg1: integer): (float)[]
public "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: (float)[]): (float)[]
public "get4x4"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "m20"(): float
public "m21"(): float
public "setView"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix3x2f
public "span"(arg0: $Vector2f$Type, arg1: $Vector2f$Type, arg2: $Vector2f$Type): $Matrix3x2f
public "setFromAddress"(arg0: long): $Matrix3x2f
public "getToAddress"(arg0: long): $Matrix3x2fc
public "m00"(): float
public "m01"(): float
public "normalizedPositiveY"(arg0: $Vector2f$Type): $Vector2f
public "normalizedPositiveX"(arg0: $Vector2f$Type): $Vector2f
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3x2f$Type = ($Matrix3x2f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3x2f_ = $Matrix3x2f$Type;
}}
declare module "packages/org/joml/$Matrix4fc" {
import {$Matrix4x3f, $Matrix4x3f$Type} from "packages/org/joml/$Matrix4x3f"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Matrix4fc {

 "add"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: (float)[]): (float)[]
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: $Matrix4f$Type): $Matrix4f
 "get"(arg0: $Matrix4d$Type): $Matrix4d
 "get"(arg0: integer, arg1: integer): float
 "equals"(arg0: $Matrix4fc$Type, arg1: float): boolean
 "scale"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "scale"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "scale"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "transform"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector4f$Type): $Vector4f
 "properties"(): integer
 "pick"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: (integer)[], arg5: $Matrix4f$Type): $Matrix4f
 "isFinite"(): boolean
 "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4f$Type): $Matrix4f
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "rotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "rotate"(arg0: float, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
 "normal"(arg0: $Matrix3f$Type): $Matrix3f
 "normal"(arg0: $Matrix4f$Type): $Matrix4f
 "sub"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "origin"(arg0: $Vector3f$Type): $Vector3f
 "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float, arg12: float, arg13: float, arg14: float, arg15: float, arg16: $Matrix4f$Type): $Matrix4f
 "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mul"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mul"(arg0: $Matrix3x2fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: $Matrix4f$Type): $Matrix4f
 "shadow"(arg0: $Vector4f$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: $Matrix4f$Type): $Matrix4f
 "shadow"(arg0: $Vector4f$Type, arg1: $Matrix4fc$Type, arg2: $Matrix4f$Type): $Matrix4f
 "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4fc$Type, arg5: $Matrix4f$Type): $Matrix4f
 "m12"(): float
 "m10"(): float
 "m13"(): float
 "m11"(): float
 "invert"(arg0: $Matrix4f$Type): $Matrix4f
 "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "translate"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
 "getRotation"(arg0: $AxisAngle4d$Type): $AxisAngle4d
 "frustumAabb"(arg0: $Vector3f$Type, arg1: $Vector3f$Type): $Matrix4f
 "projectedGridRange"(arg0: $Matrix4fc$Type, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "orthoCrop"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
 "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
 "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
 "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "positiveX"(arg0: $Vector3f$Type): $Vector3f
 "rotateLocalZ"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "rotateLocalY"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "positiveY"(arg0: $Vector3f$Type): $Vector3f
 "rotateLocalX"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "positiveZ"(arg0: $Vector3f$Type): $Vector3f
 "transformAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
 "transformAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Vector3f$Type, arg7: $Vector3f$Type): $Matrix4f
 "mulAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
 "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "testPoint"(arg0: float, arg1: float, arg2: float): boolean
 "mapZYnX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZnXnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapXZY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZXY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZnXY"(arg0: $Matrix4f$Type): $Matrix4f
 "testSphere"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
 "mapYZX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapYXZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapYXnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapYnXnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapYnZX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapYnZnX"(arg0: $Matrix4f$Type): $Matrix4f
 "withLookAtUp"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "withLookAtUp"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "obliqueZ"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
 "mapXZnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapXnZY"(arg0: $Matrix4f$Type): $Matrix4f
 "testAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean
 "mapYZnX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZXnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapXnZnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapXnYnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZYX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapYnXZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXnYnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXnYZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXnZY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYXnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYXZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYnXZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYnZnX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZYX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXZnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZnYX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZnXY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXYnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXZY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZnYnX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZXnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYnZX"(arg0: $Matrix4f$Type): $Matrix4f
 "negateY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYZnX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZnXnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnXnZnY"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZXY"(arg0: $Matrix4f$Type): $Matrix4f
 "negateX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZnYnX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYnXnZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnZYnX"(arg0: $Matrix4f$Type): $Matrix4f
 "negateZ"(arg0: $Matrix4f$Type): $Matrix4f
 "mapZnYX"(arg0: $Matrix4f$Type): $Matrix4f
 "mapnYZX"(arg0: $Matrix4f$Type): $Matrix4f
 "mul0"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mulAffineR"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mulLocal"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mulLocalAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mul3x3"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
 "invertAffine"(arg0: $Matrix4f$Type): $Matrix4f
 "mulOrthoAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "add4x3"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "invertPerspective"(arg0: $Matrix4f$Type): $Matrix4f
 "determinant"(): float
 "determinant3x3"(): float
 "determinantAffine"(): float
 "fma4x3"(arg0: $Matrix4fc$Type, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
 "mulComponentWise"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "sub4x3"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "invertOrtho"(arg0: $Matrix4f$Type): $Matrix4f
 "invertFrustum"(arg0: $Matrix4f$Type): $Matrix4f
 "transpose3x3"(arg0: $Matrix4f$Type): $Matrix4f
 "transpose3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "get4x3"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get4x3"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get4x3"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get4x3"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get4x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "get3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "get3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "getTranslation"(arg0: $Vector3f$Type): $Vector3f
 "getScale"(arg0: $Vector3f$Type): $Vector3f
 "get4x3Transposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get4x3Transposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get4x3Transposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get4x3Transposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get3x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get3x4"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get3x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get3x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "rotateAffineXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "transformPosition"(arg0: $Vector3f$Type): $Vector3f
 "transformPosition"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transformPosition"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformTranspose"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformTranspose"(arg0: $Vector4f$Type): $Vector4f
 "transformTranspose"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "transformDirection"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "scaleXY"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
 "scaleLocal"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "scaleLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "transformProject"(arg0: $Vector3f$Type): $Vector3f
 "transformProject"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector3f$Type): $Vector3f
 "transformProject"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transformProject"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformProject"(arg0: $Vector4fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformProject"(arg0: $Vector4f$Type): $Vector4f
 "transformProject"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "transformProject"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformAffine"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "transformAffine"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformAffine"(arg0: $Vector4f$Type): $Vector4f
 "rotateTowardsXY"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
 "rotateTranslation"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "rotateAffineYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "translateLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "rotateAffine"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "rotateAffine"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
 "rotateAffineZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
 "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "tile"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Matrix4f$Type): $Matrix4f
 "perspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "perspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
 "ortho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "lookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4f$Type): $Matrix4f
 "lookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
 "lookAtPerspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
 "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
 "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "ortho2D"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "lookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
 "lookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4f$Type): $Matrix4f
 "frustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "frustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "perspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "perspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
 "frustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "frustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "perspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "perspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
 "rotateAroundAffine"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "unproject"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector3f$Type): $Vector3f
 "unproject"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector4f$Type): $Vector4f
 "unproject"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector3f$Type): $Vector3f
 "unproject"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector4f$Type): $Vector4f
 "rotateAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "rotateAroundLocal"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "unprojectInv"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector3f$Type): $Vector3f
 "unprojectInv"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector3f$Type): $Vector3f
 "unprojectInv"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector4f$Type): $Vector4f
 "unprojectInv"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector4f$Type): $Vector4f
 "unprojectInvRay"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector3f$Type, arg4: $Vector3f$Type): $Matrix4f
 "unprojectInvRay"(arg0: $Vector2fc$Type, arg1: (integer)[], arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
 "project"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector4f$Type): $Vector4f
 "project"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector3f$Type): $Vector3f
 "project"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector3f$Type): $Vector3f
 "project"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector4f$Type): $Vector4f
 "unprojectRay"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector3f$Type, arg4: $Vector3f$Type): $Matrix4f
 "unprojectRay"(arg0: $Vector2fc$Type, arg1: (integer)[], arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
 "frustumCorner"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
 "getRow"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
 "getRow"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
 "cofactor3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "cofactor3x3"(arg0: $Matrix4f$Type): $Matrix4f
 "normalize3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "normalize3x3"(arg0: $Matrix4f$Type): $Matrix4f
 "frustumPlane"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
 "perspectiveOrigin"(arg0: $Vector3f$Type): $Vector3f
 "perspectiveNear"(): float
 "perspectiveFar"(): float
 "frustumRayDir"(arg0: float, arg1: float, arg2: $Vector3f$Type): $Vector3f
 "perspectiveFov"(): float
 "arcball"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "arcball"(arg0: float, arg1: $Vector3fc$Type, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "isAffine"(): boolean
 "originAffine"(arg0: $Vector3f$Type): $Vector3f
 "m23"(): float
 "m31"(): float
 "m20"(): float
 "m21"(): float
 "m33"(): float
 "m30"(): float
 "m22"(): float
 "m32"(): float
 "transpose"(arg0: $Matrix4f$Type): $Matrix4f
 "getRowColumn"(arg0: integer, arg1: integer): float
 "getToAddress"(arg0: long): $Matrix4fc
 "m03"(): float
 "m00"(): float
 "m01"(): float
 "m02"(): float
 "rotateZ"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "rotateX"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "rotateY"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
 "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
 "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
 "reflect"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
 "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "lerp"(arg0: $Matrix4fc$Type, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
 "getColumn"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
 "getColumn"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
 "perspectiveFrustumSlice"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
 "perspectiveInvOrigin"(arg0: $Vector3f$Type): $Vector3f
 "lookAtPerspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
 "perspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "perspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "perspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "perspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "perspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
 "perspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
 "mulTranslationAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mul4x3ComponentWise"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "mulPerspectiveAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "mulPerspectiveAffine"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "invertPerspectiveView"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "invertPerspectiveView"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
 "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
}

export namespace $Matrix4fc {
const PLANE_NX: integer
const PLANE_PX: integer
const PLANE_NY: integer
const PLANE_PY: integer
const PLANE_NZ: integer
const PLANE_PZ: integer
const CORNER_NXNYNZ: integer
const CORNER_PXNYNZ: integer
const CORNER_PXPYNZ: integer
const CORNER_NXPYNZ: integer
const CORNER_PXNYPZ: integer
const CORNER_NXNYPZ: integer
const CORNER_NXPYPZ: integer
const CORNER_PXPYPZ: integer
const PROPERTY_PERSPECTIVE: byte
const PROPERTY_AFFINE: byte
const PROPERTY_IDENTITY: byte
const PROPERTY_TRANSLATION: byte
const PROPERTY_ORTHONORMAL: byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4fc$Type = ($Matrix4fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4fc_ = $Matrix4fc$Type;
}}
declare module "packages/org/joml/$Matrix4dc" {
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Matrix4dc {

 "add"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "get"(arg0: (double)[], arg1: integer): (double)[]
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: (double)[]): (double)[]
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: (float)[]): (float)[]
 "get"(arg0: $Matrix4d$Type): $Matrix4d
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: integer): double
 "equals"(arg0: $Matrix4dc$Type, arg1: double): boolean
 "scale"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "scale"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "scale"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "transform"(arg0: $Vector4d$Type): $Vector4d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "properties"(): integer
 "pick"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: (integer)[], arg5: $Matrix4d$Type): $Matrix4d
 "isFinite"(): boolean
 "rotate"(arg0: double, arg1: $Vector3fc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "rotate"(arg0: double, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "rotate"(arg0: $AxisAngle4d$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotate"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4d$Type): $Matrix4d
 "normal"(arg0: $Matrix4d$Type): $Matrix4d
 "normal"(arg0: $Matrix3d$Type): $Matrix3d
 "sub"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "origin"(arg0: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Matrix3x2dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: double, arg14: double, arg15: double, arg16: $Matrix4d$Type): $Matrix4d
 "mul"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul"(arg0: $Matrix3x2fc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul"(arg0: $Matrix4fc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "shadow"(arg0: $Vector4dc$Type, arg1: $Matrix4dc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4dc$Type, arg5: $Matrix4d$Type): $Matrix4d
 "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: $Matrix4d$Type): $Matrix4d
 "shadow"(arg0: $Vector4dc$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Matrix4d$Type): $Matrix4d
 "m12"(): double
 "m10"(): double
 "m13"(): double
 "m11"(): double
 "invert"(arg0: $Matrix4d$Type): $Matrix4d
 "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "translate"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "translate"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "projectedGridRange"(arg0: $Matrix4dc$Type, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "orthoCrop"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
 "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
 "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "positiveX"(arg0: $Vector3d$Type): $Vector3d
 "rotateLocalZ"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "rotateLocalY"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "positiveY"(arg0: $Vector3d$Type): $Vector3d
 "rotateLocalX"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "positiveZ"(arg0: $Vector3d$Type): $Vector3d
 "transformAab"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Vector3d$Type, arg7: $Vector3d$Type): $Matrix4d
 "transformAab"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
 "mulAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "testPoint"(arg0: double, arg1: double, arg2: double): boolean
 "mapZYnX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZnXnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapXZY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZXY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZnXY"(arg0: $Matrix4d$Type): $Matrix4d
 "testSphere"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
 "mapYZX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapYXZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapYXnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapYnXnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapYnZX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapYnZnX"(arg0: $Matrix4d$Type): $Matrix4d
 "withLookAtUp"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "withLookAtUp"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "obliqueZ"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
 "mapXZnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapXnZY"(arg0: $Matrix4d$Type): $Matrix4d
 "testAab"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): boolean
 "mapYZnX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZXnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapXnZnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapXnYnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZYX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapYnXZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXnYnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXnYZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXnZY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYXnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYXZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYnXZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYnZnX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZYX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXZnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZnYX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZnXY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXYnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXZY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZnYnX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZXnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYnZX"(arg0: $Matrix4d$Type): $Matrix4d
 "negateY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYZnX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZnXnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnXnZnY"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZXY"(arg0: $Matrix4d$Type): $Matrix4d
 "negateX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZnYnX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYnXnZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnZYnX"(arg0: $Matrix4d$Type): $Matrix4d
 "negateZ"(arg0: $Matrix4d$Type): $Matrix4d
 "mapZnYX"(arg0: $Matrix4d$Type): $Matrix4d
 "mapnYZX"(arg0: $Matrix4d$Type): $Matrix4d
 "mul0"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mulAffineR"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mulLocal"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mulLocalAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul3x3"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
 "invertAffine"(arg0: $Matrix4d$Type): $Matrix4d
 "mulOrthoAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "add4x3"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "add4x3"(arg0: $Matrix4fc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "invertPerspective"(arg0: $Matrix4d$Type): $Matrix4d
 "determinant"(): double
 "determinant3x3"(): double
 "determinantAffine"(): double
 "fma4x3"(arg0: $Matrix4dc$Type, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
 "mulComponentWise"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "sub4x3"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "invertOrtho"(arg0: $Matrix4d$Type): $Matrix4d
 "invertFrustum"(arg0: $Matrix4d$Type): $Matrix4d
 "transpose3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "transpose3x3"(arg0: $Matrix4d$Type): $Matrix4d
 "get4x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "get3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "getTranslation"(arg0: $Vector3d$Type): $Vector3d
 "getScale"(arg0: $Vector3d$Type): $Vector3d
 "get4x3Transposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get4x3Transposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get4x3Transposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get4x3Transposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "rotateAffineXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "transformPosition"(arg0: $Vector3d$Type): $Vector3d
 "transformPosition"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformPosition"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformTranspose"(arg0: $Vector4d$Type): $Vector4d
 "transformTranspose"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "transformTranspose"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformDirection"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformDirection"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformDirection"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: $Vector3d$Type): $Vector3d
 "scaleXY"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
 "scaleLocal"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "scaleLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "transformProject"(arg0: $Vector3d$Type): $Vector3d
 "transformProject"(arg0: $Vector4d$Type): $Vector4d
 "transformProject"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformProject"(arg0: $Vector4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformProject"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "transformProject"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformProject"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformProject"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector3d$Type): $Vector3d
 "transformAffine"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "transformAffine"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformAffine"(arg0: $Vector4d$Type): $Vector4d
 "rotateTowardsXY"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
 "rotateTranslation"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateTranslation"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "rotateLocal"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "rotateAffineYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "translateLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "translateLocal"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateAffine"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "rotateAffine"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "rotateAffine"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
 "rotateAffineZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
 "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "tile"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Matrix4d$Type): $Matrix4d
 "perspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
 "perspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "ortho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "lookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
 "lookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4d$Type): $Matrix4d
 "lookAtPerspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
 "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
 "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "ortho2D"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "lookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
 "lookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4d$Type): $Matrix4d
 "frustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "frustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "perspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "perspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
 "frustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "frustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "perspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
 "perspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "rotateAroundAffine"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "unproject"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector3d$Type): $Vector3d
 "unproject"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector4d$Type): $Vector4d
 "unproject"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector4d$Type): $Vector4d
 "unproject"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector3d$Type): $Vector3d
 "rotateAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "rotateAroundLocal"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "unprojectInv"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector3d$Type): $Vector3d
 "unprojectInv"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector4d$Type): $Vector4d
 "unprojectInv"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector3d$Type): $Vector3d
 "unprojectInv"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector4d$Type): $Vector4d
 "unprojectInvRay"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector3d$Type, arg4: $Vector3d$Type): $Matrix4d
 "unprojectInvRay"(arg0: $Vector2dc$Type, arg1: (integer)[], arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
 "project"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector3d$Type): $Vector3d
 "project"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector4d$Type): $Vector4d
 "project"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector4d$Type): $Vector4d
 "project"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector3d$Type): $Vector3d
 "unprojectRay"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector3d$Type, arg4: $Vector3d$Type): $Matrix4d
 "unprojectRay"(arg0: $Vector2dc$Type, arg1: (integer)[], arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
 "frustumCorner"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
 "getRow"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
 "getRow"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
 "cofactor3x3"(arg0: $Matrix4d$Type): $Matrix4d
 "cofactor3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "normalize3x3"(arg0: $Matrix4d$Type): $Matrix4d
 "normalize3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "frustumPlane"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
 "perspectiveOrigin"(arg0: $Vector3d$Type): $Vector3d
 "perspectiveNear"(): double
 "perspectiveFar"(): double
 "frustumRayDir"(arg0: double, arg1: double, arg2: $Vector3d$Type): $Vector3d
 "perspectiveFov"(): double
 "arcball"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "arcball"(arg0: double, arg1: $Vector3dc$Type, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "isAffine"(): boolean
 "originAffine"(arg0: $Vector3d$Type): $Vector3d
 "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "m23"(): double
 "m31"(): double
 "m20"(): double
 "m21"(): double
 "m33"(): double
 "m30"(): double
 "m22"(): double
 "m32"(): double
 "transpose"(arg0: $Matrix4d$Type): $Matrix4d
 "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getRowColumn"(arg0: integer, arg1: integer): double
 "getToAddress"(arg0: long): $Matrix4dc
 "m03"(): double
 "m00"(): double
 "m01"(): double
 "m02"(): double
 "rotateZ"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "rotateX"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "rotateY"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
 "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "reflect"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
 "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
 "lerp"(arg0: $Matrix4dc$Type, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
 "getColumn"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
 "getColumn"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
 "perspectiveFrustumSlice"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
 "perspectiveInvOrigin"(arg0: $Vector3d$Type): $Vector3d
 "lookAtPerspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
 "perspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "perspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "perspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "perspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "perspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
 "perspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
 "mulTranslationAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mul4x3ComponentWise"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "mulPerspectiveAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "mulPerspectiveAffine"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "invertPerspectiveView"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "invertPerspectiveView"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
 "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
}

export namespace $Matrix4dc {
const PLANE_NX: integer
const PLANE_PX: integer
const PLANE_NY: integer
const PLANE_PY: integer
const PLANE_NZ: integer
const PLANE_PZ: integer
const CORNER_NXNYNZ: integer
const CORNER_PXNYNZ: integer
const CORNER_PXPYNZ: integer
const CORNER_NXPYNZ: integer
const CORNER_PXNYPZ: integer
const CORNER_NXNYPZ: integer
const CORNER_NXPYPZ: integer
const CORNER_PXPYPZ: integer
const PROPERTY_PERSPECTIVE: byte
const PROPERTY_AFFINE: byte
const PROPERTY_IDENTITY: byte
const PROPERTY_TRANSLATION: byte
const PROPERTY_ORTHONORMAL: byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4dc$Type = ($Matrix4dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4dc_ = $Matrix4dc$Type;
}}
declare module "packages/org/joml/$Matrix3dc" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Matrix3dc {

 "add"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: (double)[], arg1: integer): (double)[]
 "get"(arg0: integer, arg1: integer): double
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Matrix3d$Type): $Matrix3d
 "get"(arg0: (float)[]): (float)[]
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: (double)[]): (double)[]
 "equals"(arg0: $Matrix3dc$Type, arg1: double): boolean
 "scale"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "scale"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
 "scale"(arg0: $Vector3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "transform"(arg0: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "isFinite"(): boolean
 "rotate"(arg0: $Quaterniondc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "rotate"(arg0: $AxisAngle4d$Type, arg1: $Matrix3d$Type): $Matrix3d
 "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix3d$Type): $Matrix3d
 "rotate"(arg0: double, arg1: $Vector3fc$Type, arg2: $Matrix3d$Type): $Matrix3d
 "rotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3d$Type): $Matrix3d
 "rotate"(arg0: double, arg1: $Vector3dc$Type, arg2: $Matrix3d$Type): $Matrix3d
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "normal"(arg0: $Matrix3d$Type): $Matrix3d
 "sub"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "mul"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "mul"(arg0: $Matrix3fc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "m12"(): double
 "m10"(): double
 "m11"(): double
 "invert"(arg0: $Matrix3d$Type): $Matrix3d
 "quadraticFormProduct"(arg0: double, arg1: double, arg2: double): double
 "quadraticFormProduct"(arg0: $Vector3dc$Type): double
 "quadraticFormProduct"(arg0: $Vector3fc$Type): double
 "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
 "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
 "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
 "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
 "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
 "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
 "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix3d$Type): $Matrix3d
 "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix3d$Type): $Matrix3d
 "positiveX"(arg0: $Vector3d$Type): $Vector3d
 "rotateLocalZ"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "rotateLocalY"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "positiveY"(arg0: $Vector3d$Type): $Vector3d
 "rotateLocalX"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "positiveZ"(arg0: $Vector3d$Type): $Vector3d
 "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix3d$Type): $Matrix3d
 "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix3d$Type): $Matrix3d
 "mapZYnX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZnXnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapXZY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZXY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZnXY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYZX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYXZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYXnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYnXnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYnZX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYnZnX"(arg0: $Matrix3d$Type): $Matrix3d
 "obliqueZ"(arg0: double, arg1: double, arg2: $Matrix3d$Type): $Matrix3d
 "mapXZnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapXnZY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYZnX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZXnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapXnZnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapXnYnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZYX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapYnXZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXnYnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXnYZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXnZY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYXnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYXZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYnXZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYnZnX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZYX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXZnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZnYX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZnXY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXYnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXZY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZnYnX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZXnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYnZX"(arg0: $Matrix3d$Type): $Matrix3d
 "negateY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYZnX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZnXnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnXnZnY"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZXY"(arg0: $Matrix3d$Type): $Matrix3d
 "negateX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZnYnX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYnXnZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnZYnX"(arg0: $Matrix3d$Type): $Matrix3d
 "negateZ"(arg0: $Matrix3d$Type): $Matrix3d
 "mapZnYX"(arg0: $Matrix3d$Type): $Matrix3d
 "mapnYZX"(arg0: $Matrix3d$Type): $Matrix3d
 "mulLocal"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "determinant"(): double
 "mulComponentWise"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "getScale"(arg0: $Vector3d$Type): $Vector3d
 "transformTranspose"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformTranspose"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformTranspose"(arg0: $Vector3d$Type): $Vector3d
 "scaleLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
 "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3d$Type): $Matrix3d
 "rotateLocal"(arg0: $Quaterniondc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "getRow"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
 "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "m20"(): double
 "m21"(): double
 "m22"(): double
 "transpose"(arg0: $Matrix3d$Type): $Matrix3d
 "cofactor"(arg0: $Matrix3d$Type): $Matrix3d
 "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getRowColumn"(arg0: integer, arg1: integer): double
 "getToAddress"(arg0: long): $Matrix3dc
 "m00"(): double
 "m01"(): double
 "m02"(): double
 "rotateZ"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "rotateX"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "rotateY"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
 "reflect"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
 "reflect"(arg0: $Quaterniondc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "reflect"(arg0: $Vector3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
 "lerp"(arg0: $Matrix3dc$Type, arg1: double, arg2: $Matrix3d$Type): $Matrix3d
 "getColumn"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
 "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
}

export namespace $Matrix3dc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3dc$Type = ($Matrix3dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3dc_ = $Matrix3dc$Type;
}}
declare module "packages/org/joml/$Matrix3fc" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Matrix3fc {

 "add"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Matrix3f$Type): $Matrix3f
 "get"(arg0: $Matrix4f$Type): $Matrix4f
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: integer, arg1: integer): float
 "get"(arg0: (float)[]): (float)[]
 "equals"(arg0: $Matrix3fc$Type, arg1: float): boolean
 "scale"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "scale"(arg0: $Vector3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "scale"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
 "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3f$Type): $Vector3f
 "isFinite"(): boolean
 "rotate"(arg0: float, arg1: $Vector3fc$Type, arg2: $Matrix3f$Type): $Matrix3f
 "rotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3f$Type): $Matrix3f
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix3f$Type): $Matrix3f
 "normal"(arg0: $Matrix3f$Type): $Matrix3f
 "sub"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "mul"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "m12"(): float
 "m10"(): float
 "m11"(): float
 "invert"(arg0: $Matrix3f$Type): $Matrix3f
 "quadraticFormProduct"(arg0: float, arg1: float, arg2: float): float
 "quadraticFormProduct"(arg0: $Vector3fc$Type): float
 "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
 "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
 "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
 "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
 "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
 "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
 "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix3f$Type): $Matrix3f
 "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix3f$Type): $Matrix3f
 "positiveX"(arg0: $Vector3f$Type): $Vector3f
 "rotateLocalZ"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "rotateLocalY"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "positiveY"(arg0: $Vector3f$Type): $Vector3f
 "rotateLocalX"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "positiveZ"(arg0: $Vector3f$Type): $Vector3f
 "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix3f$Type): $Matrix3f
 "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix3f$Type): $Matrix3f
 "mapZYnX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZnXnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapXZY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZXY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZnXY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYZX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYXZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYXnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYnXnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYnZX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYnZnX"(arg0: $Matrix3f$Type): $Matrix3f
 "obliqueZ"(arg0: float, arg1: float, arg2: $Matrix3f$Type): $Matrix3f
 "mapXZnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapXnZY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYZnX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZXnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapXnZnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapXnYnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZYX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapYnXZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXnYnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXnYZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXnZY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYXnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYXZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYnXZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYnZnX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZYX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXZnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZnYX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZnXY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXYnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXZY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZnYnX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZXnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYnZX"(arg0: $Matrix3f$Type): $Matrix3f
 "negateY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYZnX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZnXnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnXnZnY"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZXY"(arg0: $Matrix3f$Type): $Matrix3f
 "negateX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZnYnX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYnXnZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnZYnX"(arg0: $Matrix3f$Type): $Matrix3f
 "negateZ"(arg0: $Matrix3f$Type): $Matrix3f
 "mapZnYX"(arg0: $Matrix3f$Type): $Matrix3f
 "mapnYZX"(arg0: $Matrix3f$Type): $Matrix3f
 "mulLocal"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "determinant"(): float
 "mulComponentWise"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "getScale"(arg0: $Vector3f$Type): $Vector3f
 "get3x4"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get3x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get3x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get3x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "transformTranspose"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transformTranspose"(arg0: $Vector3f$Type): $Vector3f
 "transformTranspose"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "scaleLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
 "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3f$Type): $Matrix3f
 "getRow"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
 "m20"(): float
 "m21"(): float
 "m22"(): float
 "transpose"(arg0: $Matrix3f$Type): $Matrix3f
 "cofactor"(arg0: $Matrix3f$Type): $Matrix3f
 "getRowColumn"(arg0: integer, arg1: integer): float
 "getToAddress"(arg0: long): $Matrix3fc
 "m00"(): float
 "m01"(): float
 "m02"(): float
 "rotateZ"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "rotateX"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "rotateY"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
 "reflect"(arg0: $Quaternionfc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "reflect"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
 "reflect"(arg0: $Vector3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
 "lerp"(arg0: $Matrix3fc$Type, arg1: float, arg2: $Matrix3f$Type): $Matrix3f
 "getColumn"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
 "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
}

export namespace $Matrix3fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3fc$Type = ($Matrix3fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3fc_ = $Matrix3fc$Type;
}}
declare module "packages/org/joml/$Matrix2fc" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix2f, $Matrix2f$Type} from "packages/org/joml/$Matrix2f"
import {$Matrix3x2f, $Matrix3x2f$Type} from "packages/org/joml/$Matrix3x2f"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Matrix2fc {

 "add"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $Matrix3f$Type): $Matrix3f
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: $Matrix3x2f$Type): $Matrix3x2f
 "get"(arg0: (float)[]): (float)[]
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $Matrix2f$Type): $Matrix2f
 "get"(arg0: integer, arg1: integer): float
 "equals"(arg0: $Matrix2fc$Type, arg1: float): boolean
 "scale"(arg0: float, arg1: float, arg2: $Matrix2f$Type): $Matrix2f
 "scale"(arg0: $Vector2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
 "scale"(arg0: float, arg1: $Matrix2f$Type): $Matrix2f
 "transform"(arg0: $Vector2f$Type): $Vector2f
 "transform"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "transform"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "isFinite"(): boolean
 "rotate"(arg0: float, arg1: $Matrix2f$Type): $Matrix2f
 "normal"(arg0: $Matrix2f$Type): $Matrix2f
 "sub"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
 "mul"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
 "m10"(): float
 "m11"(): float
 "invert"(arg0: $Matrix2f$Type): $Matrix2f
 "getRotation"(): float
 "positiveX"(arg0: $Vector2f$Type): $Vector2f
 "positiveY"(arg0: $Vector2f$Type): $Vector2f
 "mulLocal"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "determinant"(): float
 "mulComponentWise"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
 "getScale"(arg0: $Vector2f$Type): $Vector2f
 "transformTranspose"(arg0: $Vector2f$Type): $Vector2f
 "transformTranspose"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "transformTranspose"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "scaleLocal"(arg0: float, arg1: float, arg2: $Matrix2f$Type): $Matrix2f
 "rotateLocal"(arg0: float, arg1: $Matrix2f$Type): $Matrix2f
 "getRow"(arg0: integer, arg1: $Vector2f$Type): $Vector2f
 "transpose"(arg0: $Matrix2f$Type): $Matrix2f
 "getToAddress"(arg0: long): $Matrix2fc
 "m00"(): float
 "m01"(): float
 "lerp"(arg0: $Matrix2fc$Type, arg1: float, arg2: $Matrix2f$Type): $Matrix2f
 "getColumn"(arg0: integer, arg1: $Vector2f$Type): $Vector2f
 "normalizedPositiveY"(arg0: $Vector2f$Type): $Vector2f
 "normalizedPositiveX"(arg0: $Vector2f$Type): $Vector2f
}

export namespace $Matrix2fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix2fc$Type = ($Matrix2fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix2fc_ = $Matrix2fc$Type;
}}
declare module "packages/org/joml/$Matrix2dc" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2d, $Matrix3x2d$Type} from "packages/org/joml/$Matrix3x2d"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Matrix2d, $Matrix2d$Type} from "packages/org/joml/$Matrix2d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Matrix2dc {

 "add"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "get"(arg0: (double)[]): (double)[]
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: $Matrix3d$Type): $Matrix3d
 "get"(arg0: $Matrix2d$Type): $Matrix2d
 "get"(arg0: $Matrix3x2d$Type): $Matrix3x2d
 "get"(arg0: (double)[], arg1: integer): (double)[]
 "get"(arg0: integer, arg1: integer): double
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "equals"(arg0: $Matrix2dc$Type, arg1: double): boolean
 "scale"(arg0: double, arg1: $Matrix2d$Type): $Matrix2d
 "scale"(arg0: double, arg1: double, arg2: $Matrix2d$Type): $Matrix2d
 "scale"(arg0: $Vector2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "transform"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "transform"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "transform"(arg0: $Vector2d$Type): $Vector2d
 "isFinite"(): boolean
 "rotate"(arg0: double, arg1: $Matrix2d$Type): $Matrix2d
 "normal"(arg0: $Matrix2d$Type): $Matrix2d
 "sub"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "mul"(arg0: $Matrix2fc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "mul"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "m10"(): double
 "m11"(): double
 "invert"(arg0: $Matrix2d$Type): $Matrix2d
 "getRotation"(): double
 "positiveX"(arg0: $Vector2d$Type): $Vector2d
 "positiveY"(arg0: $Vector2d$Type): $Vector2d
 "mulLocal"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "determinant"(): double
 "mulComponentWise"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
 "getScale"(arg0: $Vector2d$Type): $Vector2d
 "transformTranspose"(arg0: $Vector2d$Type): $Vector2d
 "transformTranspose"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "transformTranspose"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "scaleLocal"(arg0: double, arg1: double, arg2: $Matrix2d$Type): $Matrix2d
 "rotateLocal"(arg0: double, arg1: $Matrix2d$Type): $Matrix2d
 "getRow"(arg0: integer, arg1: $Vector2d$Type): $Vector2d
 "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "transpose"(arg0: $Matrix2d$Type): $Matrix2d
 "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getToAddress"(arg0: long): $Matrix2dc
 "m00"(): double
 "m01"(): double
 "lerp"(arg0: $Matrix2dc$Type, arg1: double, arg2: $Matrix2d$Type): $Matrix2d
 "getColumn"(arg0: integer, arg1: $Vector2d$Type): $Vector2d
 "normalizedPositiveY"(arg0: $Vector2d$Type): $Vector2d
 "normalizedPositiveX"(arg0: $Vector2d$Type): $Vector2d
}

export namespace $Matrix2dc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix2dc$Type = ($Matrix2dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix2dc_ = $Matrix2dc$Type;
}}
declare module "packages/org/joml/$Matrix2f" {
import {$Matrix3x2f, $Matrix3x2f$Type} from "packages/org/joml/$Matrix3x2f"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Matrix2f implements $Externalizable, $Cloneable, $Matrix2fc {
 "m00": float
 "m01": float
 "m10": float
 "m11": float

constructor(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: float, arg1: float, arg2: float, arg3: float)
constructor()
constructor(arg0: $Matrix2fc$Type)
constructor(arg0: $Matrix3fc$Type)

public "add"(arg0: $Matrix2fc$Type): $Matrix2f
public "add"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $Matrix3f$Type): $Matrix3f
public "get"(arg0: $Matrix3x2f$Type): $Matrix3x2f
public "get"(arg0: integer, arg1: integer): float
public "get"(arg0: $Matrix2f$Type): $Matrix2f
public "get"(arg0: (float)[]): (float)[]
public "equals"(arg0: $Matrix2fc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: float, arg1: float, arg2: $Matrix2f$Type): $Matrix2f
public "scale"(arg0: $Vector2fc$Type): $Matrix2f
public "scale"(arg0: float): $Matrix2f
public "scale"(arg0: float, arg1: $Matrix2f$Type): $Matrix2f
public "scale"(arg0: float, arg1: float): $Matrix2f
public "scale"(arg0: $Vector2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
public "transform"(arg0: $Vector2f$Type): $Vector2f
public "transform"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "transform"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "identity"(): $Matrix2f
public "set"(arg0: $Matrix3fc$Type): $Matrix2f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix2f
public "set"(arg0: $Matrix2fc$Type): $Matrix2f
public "set"(arg0: $Matrix3x2fc$Type): $Matrix2f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix2f
public "set"(arg0: $FloatBuffer$Type): $Matrix2f
public "set"(arg0: $ByteBuffer$Type): $Matrix2f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix2f
public "set"(arg0: (float)[]): $Matrix2f
public "set"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type): $Matrix2f
public "set"(arg0: integer, arg1: integer, arg2: float): $Matrix2f
public "zero"(): $Matrix2f
public "isFinite"(): boolean
public "swap"(arg0: $Matrix2f$Type): $Matrix2f
public "rotate"(arg0: float): $Matrix2f
public "rotate"(arg0: float, arg1: $Matrix2f$Type): $Matrix2f
public "normal"(): $Matrix2f
public "normal"(arg0: $Matrix2f$Type): $Matrix2f
public "sub"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
public "sub"(arg0: $Matrix2fc$Type): $Matrix2f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix2fc$Type): $Matrix2f
public "mul"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
public "m10"(): float
public "m10"(arg0: float): $Matrix2f
public "m11"(): float
public "m11"(arg0: float): $Matrix2f
public "scaling"(arg0: $Vector2fc$Type): $Matrix2f
public "scaling"(arg0: float, arg1: float): $Matrix2f
public "scaling"(arg0: float): $Matrix2f
public "invert"(): $Matrix2f
public "invert"(arg0: $Matrix2f$Type): $Matrix2f
public "getRotation"(): float
public "rotation"(arg0: float): $Matrix2f
public "positiveX"(arg0: $Vector2f$Type): $Vector2f
public "positiveY"(arg0: $Vector2f$Type): $Vector2f
public "mulLocal"(arg0: $Matrix2fc$Type): $Matrix2f
public "mulLocal"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "determinant"(): float
public "mulComponentWise"(arg0: $Matrix2fc$Type, arg1: $Matrix2f$Type): $Matrix2f
public "mulComponentWise"(arg0: $Matrix2fc$Type): $Matrix2f
public "getScale"(arg0: $Vector2f$Type): $Vector2f
public "transformTranspose"(arg0: $Vector2f$Type): $Vector2f
public "transformTranspose"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
public "transformTranspose"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
public "scaleLocal"(arg0: float, arg1: float, arg2: $Matrix2f$Type): $Matrix2f
public "scaleLocal"(arg0: float, arg1: float): $Matrix2f
public "rotateLocal"(arg0: float, arg1: $Matrix2f$Type): $Matrix2f
public "rotateLocal"(arg0: float): $Matrix2f
public "getRow"(arg0: integer, arg1: $Vector2f$Type): $Vector2f
public "setColumn"(arg0: integer, arg1: $Vector2fc$Type): $Matrix2f
public "setColumn"(arg0: integer, arg1: float, arg2: float): $Matrix2f
public "setRow"(arg0: integer, arg1: float, arg2: float): $Matrix2f
public "setRow"(arg0: integer, arg1: $Vector2fc$Type): $Matrix2f
public "transpose"(): $Matrix2f
public "transpose"(arg0: $Matrix2f$Type): $Matrix2f
public "setFromAddress"(arg0: long): $Matrix2f
public "getToAddress"(arg0: long): $Matrix2fc
public "m00"(arg0: float): $Matrix2f
public "m00"(): float
public "m01"(): float
public "m01"(arg0: float): $Matrix2f
public "lerp"(arg0: $Matrix2fc$Type, arg1: float, arg2: $Matrix2f$Type): $Matrix2f
public "lerp"(arg0: $Matrix2fc$Type, arg1: float): $Matrix2f
public "getColumn"(arg0: integer, arg1: $Vector2f$Type): $Vector2f
public "normalizedPositiveY"(arg0: $Vector2f$Type): $Vector2f
public "normalizedPositiveX"(arg0: $Vector2f$Type): $Vector2f
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix2f$Type = ($Matrix2f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix2f_ = $Matrix2f$Type;
}}
declare module "packages/org/joml/$Matrix2d" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix3x2d, $Matrix3x2d$Type} from "packages/org/joml/$Matrix3x2d"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Matrix2d implements $Externalizable, $Cloneable, $Matrix2dc {
 "m00": double
 "m01": double
 "m10": double
 "m11": double

constructor(arg0: $Matrix3fc$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type)
constructor()
constructor(arg0: $Matrix2dc$Type)
constructor(arg0: $Matrix2fc$Type)
constructor(arg0: $Matrix3dc$Type)

public "add"(arg0: $Matrix2dc$Type): $Matrix2d
public "add"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $Matrix3d$Type): $Matrix3d
public "get"(arg0: $Matrix3x2d$Type): $Matrix3x2d
public "get"(arg0: $Matrix2d$Type): $Matrix2d
public "get"(arg0: (double)[]): (double)[]
public "get"(arg0: (double)[], arg1: integer): (double)[]
public "get"(arg0: integer, arg1: integer): double
public "equals"(arg0: any): boolean
public "equals"(arg0: $Matrix2dc$Type, arg1: double): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: double): $Matrix2d
public "scale"(arg0: double, arg1: $Matrix2d$Type): $Matrix2d
public "scale"(arg0: double, arg1: double, arg2: $Matrix2d$Type): $Matrix2d
public "scale"(arg0: $Vector2dc$Type): $Matrix2d
public "scale"(arg0: $Vector2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "scale"(arg0: double, arg1: double): $Matrix2d
public "transform"(arg0: $Vector2d$Type): $Vector2d
public "transform"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "transform"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "identity"(): $Matrix2d
public "set"(arg0: $ByteBuffer$Type): $Matrix2d
public "set"(arg0: $DoubleBuffer$Type): $Matrix2d
public "set"(arg0: $Matrix2dc$Type): $Matrix2d
public "set"(arg0: $Matrix3x2dc$Type): $Matrix2d
public "set"(arg0: $Matrix2fc$Type): $Matrix2d
public "set"(arg0: $Matrix3dc$Type): $Matrix2d
public "set"(arg0: integer, arg1: integer, arg2: double): $Matrix2d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix2d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Matrix2d
public "set"(arg0: (double)[]): $Matrix2d
public "set"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type): $Matrix2d
public "set"(arg0: $Matrix3fc$Type): $Matrix2d
public "set"(arg0: $Matrix3x2fc$Type): $Matrix2d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix2d
public "zero"(): $Matrix2d
public "isFinite"(): boolean
public "swap"(arg0: $Matrix2d$Type): $Matrix2d
public "rotate"(arg0: double, arg1: $Matrix2d$Type): $Matrix2d
public "rotate"(arg0: double): $Matrix2d
public "normal"(): $Matrix2d
public "normal"(arg0: $Matrix2d$Type): $Matrix2d
public "sub"(arg0: $Matrix2dc$Type): $Matrix2d
public "sub"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "mul"(arg0: $Matrix2dc$Type): $Matrix2d
public "mul"(arg0: $Matrix2fc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "mul"(arg0: $Matrix2fc$Type): $Matrix2d
public "m10"(): double
public "m10"(arg0: double): $Matrix2d
public "m11"(): double
public "m11"(arg0: double): $Matrix2d
public "scaling"(arg0: $Vector2dc$Type): $Matrix2d
public "scaling"(arg0: double, arg1: double): $Matrix2d
public "scaling"(arg0: double): $Matrix2d
public "invert"(): $Matrix2d
public "invert"(arg0: $Matrix2d$Type): $Matrix2d
public "getRotation"(): double
public "rotation"(arg0: double): $Matrix2d
public "positiveX"(arg0: $Vector2d$Type): $Vector2d
public "positiveY"(arg0: $Vector2d$Type): $Vector2d
public "mulLocal"(arg0: $Matrix2dc$Type): $Matrix2d
public "mulLocal"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "determinant"(): double
public "mulComponentWise"(arg0: $Matrix2dc$Type, arg1: $Matrix2d$Type): $Matrix2d
public "mulComponentWise"(arg0: $Matrix2dc$Type): $Matrix2d
public "getScale"(arg0: $Vector2d$Type): $Vector2d
public "transformTranspose"(arg0: $Vector2d$Type): $Vector2d
public "transformTranspose"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
public "transformTranspose"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
public "scaleLocal"(arg0: double, arg1: double, arg2: $Matrix2d$Type): $Matrix2d
public "scaleLocal"(arg0: double, arg1: double): $Matrix2d
public "rotateLocal"(arg0: double): $Matrix2d
public "rotateLocal"(arg0: double, arg1: $Matrix2d$Type): $Matrix2d
public "getRow"(arg0: integer, arg1: $Vector2d$Type): $Vector2d
public "setColumn"(arg0: integer, arg1: $Vector2dc$Type): $Matrix2d
public "setColumn"(arg0: integer, arg1: double, arg2: double): $Matrix2d
public "setRow"(arg0: integer, arg1: $Vector2dc$Type): $Matrix2d
public "setRow"(arg0: integer, arg1: double, arg2: double): $Matrix2d
public "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "transpose"(arg0: $Matrix2d$Type): $Matrix2d
public "transpose"(): $Matrix2d
public "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "setFromAddress"(arg0: long): $Matrix2d
public "getToAddress"(arg0: long): $Matrix2dc
public "m00"(arg0: double): $Matrix2d
public "m00"(): double
public "m01"(arg0: double): $Matrix2d
public "m01"(): double
public "lerp"(arg0: $Matrix2dc$Type, arg1: double): $Matrix2d
public "lerp"(arg0: $Matrix2dc$Type, arg1: double, arg2: $Matrix2d$Type): $Matrix2d
public "getColumn"(arg0: integer, arg1: $Vector2d$Type): $Vector2d
public "normalizedPositiveY"(arg0: $Vector2d$Type): $Vector2d
public "normalizedPositiveX"(arg0: $Vector2d$Type): $Vector2d
get "finite"(): boolean
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix2d$Type = ($Matrix2d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix2d_ = $Matrix2d$Type;
}}
declare module "packages/org/joml/$Matrix3d" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Matrix3d implements $Externalizable, $Cloneable, $Matrix3dc {
 "m00": double
 "m01": double
 "m02": double
 "m10": double
 "m11": double
 "m12": double
 "m20": double
 "m21": double
 "m22": double

constructor(arg0: $Matrix4fc$Type)
constructor(arg0: $Matrix4dc$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double)
constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type)
constructor()
constructor(arg0: $Matrix2dc$Type)
constructor(arg0: $Matrix2fc$Type)
constructor(arg0: $Matrix3dc$Type)
constructor(arg0: $Matrix3fc$Type)

public "add"(arg0: $Matrix3dc$Type): $Matrix3d
public "add"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: (double)[], arg1: integer): (double)[]
public "get"(arg0: $Matrix3d$Type): $Matrix3d
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: integer): double
public "get"(arg0: (float)[]): (float)[]
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: (double)[]): (double)[]
public "equals"(arg0: $Matrix3dc$Type, arg1: double): boolean
public "equals"(arg0: any): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: $Vector3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "scale"(arg0: $Vector3dc$Type): $Matrix3d
public "scale"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
public "scale"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "scale"(arg0: double): $Matrix3d
public "scale"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "identity"(): $Matrix3d
public "set"(arg0: (double)[]): $Matrix3d
public "set"(arg0: $DoubleBuffer$Type): $Matrix3d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix3d
public "set"(arg0: $ByteBuffer$Type): $Matrix3d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Matrix3d
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix3d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix3d
public "set"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix3d
public "set"(arg0: $FloatBuffer$Type): $Matrix3d
public "set"(arg0: $Matrix4fc$Type): $Matrix3d
public "set"(arg0: $Matrix4x3dc$Type): $Matrix3d
public "set"(arg0: $Matrix3fc$Type): $Matrix3d
public "set"(arg0: integer, arg1: integer, arg2: double): $Matrix3d
public "set"(arg0: $Matrix3dc$Type): $Matrix3d
public "set"(arg0: $Matrix2fc$Type): $Matrix3d
public "set"(arg0: $AxisAngle4f$Type): $Matrix3d
public "set"(arg0: $AxisAngle4d$Type): $Matrix3d
public "set"(arg0: $Matrix4dc$Type): $Matrix3d
public "set"(arg0: (float)[]): $Matrix3d
public "set"(arg0: $Matrix2dc$Type): $Matrix3d
public "set"(arg0: $Quaternionfc$Type): $Matrix3d
public "set"(arg0: $Quaterniondc$Type): $Matrix3d
public "zero"(): $Matrix3d
public "isFinite"(): boolean
public "swap"(arg0: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: $Quaternionfc$Type): $Matrix3d
public "rotate"(arg0: double, arg1: $Vector3fc$Type, arg2: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: double, arg1: $Vector3fc$Type): $Matrix3d
public "rotate"(arg0: double, arg1: $Vector3dc$Type): $Matrix3d
public "rotate"(arg0: $AxisAngle4f$Type): $Matrix3d
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: $AxisAngle4d$Type): $Matrix3d
public "rotate"(arg0: $AxisAngle4d$Type, arg1: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix3d
public "rotate"(arg0: double, arg1: $Vector3dc$Type, arg2: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3d$Type): $Matrix3d
public "rotate"(arg0: $Quaterniondc$Type): $Matrix3d
public "rotate"(arg0: $Quaterniondc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "normal"(): $Matrix3d
public "normal"(arg0: $Matrix3d$Type): $Matrix3d
public "sub"(arg0: $Matrix3dc$Type): $Matrix3d
public "sub"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix3fc$Type): $Matrix3d
public "mul"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "mul"(arg0: $Matrix3dc$Type): $Matrix3d
public "mul"(arg0: $Matrix3fc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "m12"(arg0: double): $Matrix3d
public "m12"(): double
public "m10"(): double
public "m10"(arg0: double): $Matrix3d
public "m11"(): double
public "m11"(arg0: double): $Matrix3d
public "scaling"(arg0: double): $Matrix3d
public "scaling"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "scaling"(arg0: $Vector3dc$Type): $Matrix3d
public "invert"(arg0: $Matrix3d$Type): $Matrix3d
public "invert"(): $Matrix3d
public "quadraticFormProduct"(arg0: double, arg1: double, arg2: double): double
public "quadraticFormProduct"(arg0: $Vector3fc$Type): double
public "quadraticFormProduct"(arg0: $Vector3dc$Type): double
public "rotationY"(arg0: double): $Matrix3d
public "rotationX"(arg0: double): $Matrix3d
public "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "rotation"(arg0: $AxisAngle4d$Type): $Matrix3d
public "rotation"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix3d
public "rotation"(arg0: double, arg1: $Vector3fc$Type): $Matrix3d
public "rotation"(arg0: double, arg1: $Vector3dc$Type): $Matrix3d
public "rotation"(arg0: $Quaternionfc$Type): $Matrix3d
public "rotation"(arg0: $Quaterniondc$Type): $Matrix3d
public "rotation"(arg0: $AxisAngle4f$Type): $Matrix3d
public "rotateZYX"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
public "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
public "rotateYXZ"(arg0: $Vector3d$Type): $Matrix3d
public "rotateYXZ"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
public "rotateXYZ"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
public "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
public "rotationXYZ"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "rotationYXZ"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "rotationZYX"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix3d
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix3d$Type): $Matrix3d
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix3d$Type): $Matrix3d
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix3d
public "positiveX"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalZ"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "rotateLocalZ"(arg0: double): $Matrix3d
public "rotateLocalY"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "rotateLocalY"(arg0: double): $Matrix3d
public "positiveY"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalX"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "rotateLocalX"(arg0: double): $Matrix3d
public "positiveZ"(arg0: $Vector3d$Type): $Vector3d
public "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix3d$Type): $Matrix3d
public "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix3d
public "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix3d$Type): $Matrix3d
public "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix3d
public "rotationTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix3d
public "rotationTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix3d
public "mapZYnX"(): $Matrix3d
public "mapZYnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZnXnY"(): $Matrix3d
public "mapZnXnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapXZY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapXZY"(): $Matrix3d
public "mapZXY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZXY"(): $Matrix3d
public "mapZnXY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZnXY"(): $Matrix3d
public "mapYZX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYZX"(): $Matrix3d
public "mapYXZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYXZ"(): $Matrix3d
public "mapYXnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYXnZ"(): $Matrix3d
public "mapYnXnZ"(): $Matrix3d
public "mapYnXnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYnZX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYnZX"(): $Matrix3d
public "mapYnZnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYnZnX"(): $Matrix3d
public "obliqueZ"(arg0: double, arg1: double): $Matrix3d
public "obliqueZ"(arg0: double, arg1: double, arg2: $Matrix3d$Type): $Matrix3d
public "mapXZnY"(): $Matrix3d
public "mapXZnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapXnZY"(): $Matrix3d
public "mapXnZY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYZnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYZnX"(): $Matrix3d
public "mapZXnY"(): $Matrix3d
public "mapZXnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapXnZnY"(): $Matrix3d
public "mapXnZnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapXnYnZ"(): $Matrix3d
public "mapXnYnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZYX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZYX"(): $Matrix3d
public "mapYnXZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapYnXZ"(): $Matrix3d
public "mapnXnYnZ"(): $Matrix3d
public "mapnXnYnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnXnYZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnXnYZ"(): $Matrix3d
public "mapnXnZY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnXnZY"(): $Matrix3d
public "mapnYXnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnYXnZ"(): $Matrix3d
public "mapnYXZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnYXZ"(): $Matrix3d
public "mapnYnXZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnYnXZ"(): $Matrix3d
public "mapnYnZnX"(): $Matrix3d
public "mapnYnZnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZYX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZYX"(): $Matrix3d
public "mapnXZnY"(): $Matrix3d
public "mapnXZnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnYX"(): $Matrix3d
public "mapnZnYX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnXY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnXY"(): $Matrix3d
public "mapnXYnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnXYnZ"(): $Matrix3d
public "mapnXZY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnXZY"(): $Matrix3d
public "mapZnYnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZnYnX"(): $Matrix3d
public "mapnZXnY"(): $Matrix3d
public "mapnZXnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnYnZX"(): $Matrix3d
public "mapnYnZX"(arg0: $Matrix3d$Type): $Matrix3d
public "negateY"(): $Matrix3d
public "negateY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnYZnX"(): $Matrix3d
public "mapnYZnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnXnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnXnY"(): $Matrix3d
public "mapnXnZnY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnXnZnY"(): $Matrix3d
public "mapnZXY"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZXY"(): $Matrix3d
public "negateX"(): $Matrix3d
public "negateX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnYnX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnZnYnX"(): $Matrix3d
public "mapnYnXnZ"(arg0: $Matrix3d$Type): $Matrix3d
public "mapnYnXnZ"(): $Matrix3d
public "mapnZYnX"(): $Matrix3d
public "mapnZYnX"(arg0: $Matrix3d$Type): $Matrix3d
public "negateZ"(arg0: $Matrix3d$Type): $Matrix3d
public "negateZ"(): $Matrix3d
public "mapZnYX"(arg0: $Matrix3d$Type): $Matrix3d
public "mapZnYX"(): $Matrix3d
public "mapnYZX"(): $Matrix3d
public "mapnYZX"(arg0: $Matrix3d$Type): $Matrix3d
public "setTransposed"(arg0: $Matrix3dc$Type): $Matrix3d
public "setTransposed"(arg0: $Matrix3fc$Type): $Matrix3d
public "mulLocal"(arg0: $Matrix3dc$Type): $Matrix3d
public "mulLocal"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "determinant"(): double
public "mulComponentWise"(arg0: $Matrix3dc$Type): $Matrix3d
public "mulComponentWise"(arg0: $Matrix3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "getScale"(arg0: $Vector3d$Type): $Vector3d
public "transformTranspose"(arg0: $Vector3d$Type): $Vector3d
public "transformTranspose"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformTranspose"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "scaleLocal"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "scaleLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
public "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "rotateLocal"(arg0: $Quaternionfc$Type): $Matrix3d
public "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3d$Type): $Matrix3d
public "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix3d
public "rotateLocal"(arg0: $Quaterniondc$Type): $Matrix3d
public "rotateLocal"(arg0: $Quaterniondc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "setLookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix3d
public "setLookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix3d
public "reflection"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "reflection"(arg0: $Vector3dc$Type): $Matrix3d
public "reflection"(arg0: $Quaterniondc$Type): $Matrix3d
public "getRow"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
public "setColumn"(arg0: integer, arg1: double, arg2: double, arg3: double): $Matrix3d
public "setColumn"(arg0: integer, arg1: $Vector3dc$Type): $Matrix3d
public "setRow"(arg0: integer, arg1: double, arg2: double, arg3: double): $Matrix3d
public "setRow"(arg0: integer, arg1: $Vector3dc$Type): $Matrix3d
public "setSkewSymmetric"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "setFloats"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix3d
public "setFloats"(arg0: $ByteBuffer$Type): $Matrix3d
public "m20"(arg0: double): $Matrix3d
public "m20"(): double
public "m21"(): double
public "m21"(arg0: double): $Matrix3d
public "m22"(): double
public "m22"(arg0: double): $Matrix3d
public "transpose"(arg0: $Matrix3d$Type): $Matrix3d
public "transpose"(): $Matrix3d
public "cofactor"(arg0: $Matrix3d$Type): $Matrix3d
public "cofactor"(): $Matrix3d
public "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getRowColumn"(arg0: integer, arg1: integer): double
public "setRowColumn"(arg0: integer, arg1: integer, arg2: double): $Matrix3d
public "setFromAddress"(arg0: long): $Matrix3d
public "getToAddress"(arg0: long): $Matrix3dc
public "m00"(): double
public "m00"(arg0: double): $Matrix3d
public "m01"(arg0: double): $Matrix3d
public "m01"(): double
public "m02"(): double
public "m02"(arg0: double): $Matrix3d
public "rotateZ"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "rotateZ"(arg0: double): $Matrix3d
public "rotateX"(arg0: double): $Matrix3d
public "rotateX"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "rotateY"(arg0: double, arg1: $Matrix3d$Type): $Matrix3d
public "rotateY"(arg0: double): $Matrix3d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3d$Type): $Matrix3d
public "reflect"(arg0: double, arg1: double, arg2: double): $Matrix3d
public "reflect"(arg0: $Vector3dc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "reflect"(arg0: $Quaterniondc$Type, arg1: $Matrix3d$Type): $Matrix3d
public "reflect"(arg0: $Quaterniondc$Type): $Matrix3d
public "reflect"(arg0: $Vector3dc$Type): $Matrix3d
public "lerp"(arg0: $Matrix3dc$Type, arg1: double): $Matrix3d
public "lerp"(arg0: $Matrix3dc$Type, arg1: double, arg2: $Matrix3d$Type): $Matrix3d
public "rotationZ"(arg0: double): $Matrix3d
public "getColumn"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
public "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
get "finite"(): boolean
set "transposed"(value: $Matrix3dc$Type)
set "transposed"(value: $Matrix3fc$Type)
set "floats"(value: $ByteBuffer$Type)
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3d$Type = ($Matrix3d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3d_ = $Matrix3d$Type;
}}
declare module "packages/org/joml/$Matrix3f" {
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"

export class $Matrix3f implements $Externalizable, $Cloneable, $Matrix3fc {
 "m00": float
 "m01": float
 "m02": float
 "m10": float
 "m11": float
 "m12": float
 "m20": float
 "m21": float
 "m22": float

constructor(arg0: $Matrix4fc$Type)
constructor(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type)
constructor()
constructor(arg0: $Matrix2fc$Type)
constructor(arg0: $Matrix3fc$Type)

public "add"(arg0: $Matrix3fc$Type): $Matrix3f
public "add"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: $Matrix3f$Type): $Matrix3f
public "get"(arg0: integer, arg1: integer): float
public "get"(arg0: (float)[]): (float)[]
public "equals"(arg0: $Matrix3fc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: float): $Matrix3f
public "scale"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "scale"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "scale"(arg0: $Vector3fc$Type): $Matrix3f
public "scale"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
public "scale"(arg0: $Vector3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "identity"(): $Matrix3f
public "set"(arg0: (float)[]): $Matrix3f
public "set"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix3f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix3f
public "set"(arg0: $Quaterniondc$Type): $Matrix3f
public "set"(arg0: $ByteBuffer$Type): $Matrix3f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix3f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix3f
public "set"(arg0: $FloatBuffer$Type): $Matrix3f
public "set"(arg0: integer, arg1: integer, arg2: float): $Matrix3f
public "set"(arg0: $AxisAngle4f$Type): $Matrix3f
public "set"(arg0: $Matrix2fc$Type): $Matrix3f
public "set"(arg0: $Matrix4fc$Type): $Matrix3f
public "set"(arg0: $Matrix4x3fc$Type): $Matrix3f
public "set"(arg0: $Matrix3fc$Type): $Matrix3f
public "set"(arg0: $AxisAngle4d$Type): $Matrix3f
public "set"(arg0: $Quaternionfc$Type): $Matrix3f
public "zero"(): $Matrix3f
public "isFinite"(): boolean
public "swap"(arg0: $Matrix3f$Type): $Matrix3f
public "rotate"(arg0: $Quaternionfc$Type): $Matrix3f
public "rotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3f$Type): $Matrix3f
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix3f$Type): $Matrix3f
public "rotate"(arg0: float, arg1: $Vector3fc$Type, arg2: $Matrix3f$Type): $Matrix3f
public "rotate"(arg0: float, arg1: $Vector3fc$Type): $Matrix3f
public "rotate"(arg0: $AxisAngle4f$Type): $Matrix3f
public "rotate"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix3f
public "normal"(): $Matrix3f
public "normal"(arg0: $Matrix3f$Type): $Matrix3f
public "sub"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "sub"(arg0: $Matrix3fc$Type): $Matrix3f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "mul"(arg0: $Matrix3fc$Type): $Matrix3f
public "m12"(arg0: float): $Matrix3f
public "m12"(): float
public "m10"(): float
public "m10"(arg0: float): $Matrix3f
public "m11"(): float
public "m11"(arg0: float): $Matrix3f
public "scaling"(arg0: $Vector3fc$Type): $Matrix3f
public "scaling"(arg0: float): $Matrix3f
public "scaling"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "invert"(arg0: $Matrix3f$Type): $Matrix3f
public "invert"(): $Matrix3f
public "quadraticFormProduct"(arg0: float, arg1: float, arg2: float): float
public "quadraticFormProduct"(arg0: $Vector3fc$Type): float
public "rotationY"(arg0: float): $Matrix3f
public "rotationX"(arg0: float): $Matrix3f
public "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "rotation"(arg0: $Quaternionfc$Type): $Matrix3f
public "rotation"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix3f
public "rotation"(arg0: float, arg1: $Vector3fc$Type): $Matrix3f
public "rotation"(arg0: $AxisAngle4f$Type): $Matrix3f
public "rotateZYX"(arg0: $Vector3f$Type): $Matrix3f
public "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
public "rotateZYX"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "rotateYXZ"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "rotateYXZ"(arg0: $Vector3f$Type): $Matrix3f
public "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
public "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
public "rotateXYZ"(arg0: $Vector3f$Type): $Matrix3f
public "rotateXYZ"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
public "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
public "rotationXYZ"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "rotationYXZ"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "rotationZYX"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix3f$Type): $Matrix3f
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix3f$Type): $Matrix3f
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix3f
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix3f
public "positiveX"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalZ"(arg0: float): $Matrix3f
public "rotateLocalZ"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "rotateLocalY"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "rotateLocalY"(arg0: float): $Matrix3f
public "positiveY"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalX"(arg0: float): $Matrix3f
public "rotateLocalX"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "positiveZ"(arg0: $Vector3f$Type): $Vector3f
public "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix3f
public "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix3f$Type): $Matrix3f
public "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix3f
public "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix3f$Type): $Matrix3f
public "rotationTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix3f
public "rotationTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix3f
public "mapZYnX"(): $Matrix3f
public "mapZYnX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZnXnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZnXnY"(): $Matrix3f
public "mapXZY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapXZY"(): $Matrix3f
public "mapZXY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZXY"(): $Matrix3f
public "mapZnXY"(): $Matrix3f
public "mapZnXY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapYZX"(): $Matrix3f
public "mapYZX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapYXZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapYXZ"(): $Matrix3f
public "mapYXnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapYXnZ"(): $Matrix3f
public "mapYnXnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapYnXnZ"(): $Matrix3f
public "mapYnZX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapYnZX"(): $Matrix3f
public "mapYnZnX"(): $Matrix3f
public "mapYnZnX"(arg0: $Matrix3f$Type): $Matrix3f
public "obliqueZ"(arg0: float, arg1: float, arg2: $Matrix3f$Type): $Matrix3f
public "obliqueZ"(arg0: float, arg1: float): $Matrix3f
public "mapXZnY"(): $Matrix3f
public "mapXZnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapXnZY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapXnZY"(): $Matrix3f
public "mapYZnX"(): $Matrix3f
public "mapYZnX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZXnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZXnY"(): $Matrix3f
public "mapXnZnY"(): $Matrix3f
public "mapXnZnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapXnYnZ"(): $Matrix3f
public "mapXnYnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZYX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZYX"(): $Matrix3f
public "mapYnXZ"(): $Matrix3f
public "mapYnXZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXnYnZ"(): $Matrix3f
public "mapnXnYnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXnYZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXnYZ"(): $Matrix3f
public "mapnXnZY"(): $Matrix3f
public "mapnXnZY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYXnZ"(): $Matrix3f
public "mapnYXnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYXZ"(): $Matrix3f
public "mapnYXZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYnXZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYnXZ"(): $Matrix3f
public "mapnYnZnX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYnZnX"(): $Matrix3f
public "mapnZYX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZYX"(): $Matrix3f
public "mapnXZnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXZnY"(): $Matrix3f
public "mapnZnYX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZnYX"(): $Matrix3f
public "mapnZnXY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZnXY"(): $Matrix3f
public "mapnXYnZ"(): $Matrix3f
public "mapnXYnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXZY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXZY"(): $Matrix3f
public "mapZnYnX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZnYnX"(): $Matrix3f
public "mapnZXnY"(): $Matrix3f
public "mapnZXnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYnZX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYnZX"(): $Matrix3f
public "negateY"(): $Matrix3f
public "negateY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYZnX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnYZnX"(): $Matrix3f
public "mapnZnXnY"(): $Matrix3f
public "mapnZnXnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnXnZnY"(): $Matrix3f
public "mapnXnZnY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZXY"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZXY"(): $Matrix3f
public "negateX"(): $Matrix3f
public "negateX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZnYnX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZnYnX"(): $Matrix3f
public "mapnYnXnZ"(): $Matrix3f
public "mapnYnXnZ"(arg0: $Matrix3f$Type): $Matrix3f
public "mapnZYnX"(): $Matrix3f
public "mapnZYnX"(arg0: $Matrix3f$Type): $Matrix3f
public "negateZ"(arg0: $Matrix3f$Type): $Matrix3f
public "negateZ"(): $Matrix3f
public "mapZnYX"(arg0: $Matrix3f$Type): $Matrix3f
public "mapZnYX"(): $Matrix3f
public "mapnYZX"(): $Matrix3f
public "mapnYZX"(arg0: $Matrix3f$Type): $Matrix3f
public "setTransposed"(arg0: $Matrix3fc$Type): $Matrix3f
public "mulLocal"(arg0: $Matrix3fc$Type): $Matrix3f
public "mulLocal"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "determinant"(): float
public "mulComponentWise"(arg0: $Matrix3fc$Type): $Matrix3f
public "mulComponentWise"(arg0: $Matrix3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "getScale"(arg0: $Vector3f$Type): $Vector3f
public "get3x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get3x4"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get3x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get3x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "transformTranspose"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformTranspose"(arg0: $Vector3f$Type): $Vector3f
public "transformTranspose"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "scaleLocal"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "scaleLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
public "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix3f
public "rotateLocal"(arg0: $Quaternionfc$Type): $Matrix3f
public "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3f$Type): $Matrix3f
public "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "setLookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix3f
public "setLookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix3f
public "reflection"(arg0: $Quaternionfc$Type): $Matrix3f
public "reflection"(arg0: $Vector3fc$Type): $Matrix3f
public "reflection"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "getRow"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
public "setColumn"(arg0: integer, arg1: $Vector3fc$Type): $Matrix3f
public "setColumn"(arg0: integer, arg1: float, arg2: float, arg3: float): $Matrix3f
public "setRow"(arg0: integer, arg1: $Vector3fc$Type): $Matrix3f
public "setRow"(arg0: integer, arg1: float, arg2: float, arg3: float): $Matrix3f
public "setSkewSymmetric"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "m20"(): float
public "m20"(arg0: float): $Matrix3f
public "m21"(arg0: float): $Matrix3f
public "m21"(): float
public "m22"(): float
public "m22"(arg0: float): $Matrix3f
public "transpose"(arg0: $Matrix3f$Type): $Matrix3f
public "transpose"(): $Matrix3f
public "cofactor"(): $Matrix3f
public "cofactor"(arg0: $Matrix3f$Type): $Matrix3f
public "getRowColumn"(arg0: integer, arg1: integer): float
public "setRowColumn"(arg0: integer, arg1: integer, arg2: float): $Matrix3f
public "setFromAddress"(arg0: long): $Matrix3f
public "getToAddress"(arg0: long): $Matrix3fc
public "m00"(arg0: float): $Matrix3f
public "m00"(): float
public "m01"(arg0: float): $Matrix3f
public "m01"(): float
public "m02"(): float
public "m02"(arg0: float): $Matrix3f
public "rotateZ"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "rotateZ"(arg0: float): $Matrix3f
public "rotateX"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "rotateX"(arg0: float): $Matrix3f
public "rotateY"(arg0: float, arg1: $Matrix3f$Type): $Matrix3f
public "rotateY"(arg0: float): $Matrix3f
public "reflect"(arg0: $Vector3fc$Type): $Matrix3f
public "reflect"(arg0: $Quaternionfc$Type): $Matrix3f
public "reflect"(arg0: float, arg1: float, arg2: float): $Matrix3f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3f$Type): $Matrix3f
public "reflect"(arg0: $Vector3fc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "reflect"(arg0: $Quaternionfc$Type, arg1: $Matrix3f$Type): $Matrix3f
public "lerp"(arg0: $Matrix3fc$Type, arg1: float, arg2: $Matrix3f$Type): $Matrix3f
public "lerp"(arg0: $Matrix3fc$Type, arg1: float): $Matrix3f
public "rotationZ"(arg0: float): $Matrix3f
public "getColumn"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
public "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
get "finite"(): boolean
set "transposed"(value: $Matrix3fc$Type)
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3f$Type = ($Matrix3f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3f_ = $Matrix3f$Type;
}}
declare module "packages/org/joml/$Matrix4d" {
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Matrix4d implements $Externalizable, $Cloneable, $Matrix4dc {

constructor(arg0: $Matrix4x3fc$Type)
constructor(arg0: $Matrix3dc$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: double, arg14: double, arg15: double)
constructor(arg0: $DoubleBuffer$Type)
constructor(arg0: $Vector4d$Type, arg1: $Vector4d$Type, arg2: $Vector4d$Type, arg3: $Vector4d$Type)
constructor()
constructor(arg0: $Matrix4dc$Type)
constructor(arg0: $Matrix4fc$Type)
constructor(arg0: $Matrix4x3dc$Type)

public "add"(arg0: $Matrix4dc$Type): $Matrix4d
public "add"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: (float)[]): (float)[]
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: integer): double
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: (double)[]): (double)[]
public "get"(arg0: (double)[], arg1: integer): (double)[]
public "equals"(arg0: any): boolean
public "equals"(arg0: $Matrix4dc$Type, arg1: double): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "scale"(arg0: $Vector3dc$Type): $Matrix4d
public "scale"(arg0: double): $Matrix4d
public "scale"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "scale"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "scale"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "transform"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4d$Type): $Vector4d
public "identity"(): $Matrix4d
public "set"(arg0: $Matrix4x3fc$Type): $Matrix4d
public "set"(arg0: $Matrix3dc$Type): $Matrix4d
public "set"(arg0: $Matrix4x3dc$Type): $Matrix4d
public "set"(arg0: (double)[], arg1: integer): $Matrix4d
public "set"(arg0: (double)[]): $Matrix4d
public "set"(arg0: $AxisAngle4d$Type): $Matrix4d
public "set"(arg0: $Matrix4dc$Type): $Matrix4d
public "set"(arg0: $Matrix4fc$Type): $Matrix4d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: double, arg14: double, arg15: double): $Matrix4d
public "set"(arg0: $Vector4d$Type, arg1: $Vector4d$Type, arg2: $Vector4d$Type, arg3: $Vector4d$Type): $Matrix4d
public "set"(arg0: $Quaterniondc$Type): $Matrix4d
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix4d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Matrix4d
public "set"(arg0: $ByteBuffer$Type): $Matrix4d
public "set"(arg0: $DoubleBuffer$Type): $Matrix4d
public "set"(arg0: $FloatBuffer$Type): $Matrix4d
public "set"(arg0: (float)[], arg1: integer): $Matrix4d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix4d
public "set"(arg0: $AxisAngle4f$Type): $Matrix4d
public "set"(arg0: (float)[]): $Matrix4d
public "set"(arg0: $Quaternionfc$Type): $Matrix4d
public "set"(arg0: integer, arg1: integer, arg2: double): $Matrix4d
public "properties"(): integer
public "zero"(): $Matrix4d
public "pick"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: (integer)[]): $Matrix4d
public "pick"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: (integer)[], arg5: $Matrix4d$Type): $Matrix4d
public "isFinite"(): boolean
public "swap"(arg0: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: double, arg1: $Vector3fc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotate"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: $Quaterniondc$Type): $Matrix4d
public "rotate"(arg0: $Quaternionfc$Type): $Matrix4d
public "rotate"(arg0: $AxisAngle4f$Type): $Matrix4d
public "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: $AxisAngle4d$Type): $Matrix4d
public "rotate"(arg0: $AxisAngle4d$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: double, arg1: $Vector3fc$Type): $Matrix4d
public "rotate"(arg0: double, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "rotate"(arg0: double, arg1: $Vector3dc$Type): $Matrix4d
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "normal"(arg0: $Matrix4d$Type): $Matrix4d
public "normal"(arg0: $Matrix3d$Type): $Matrix3d
public "normal"(): $Matrix4d
public "sub"(arg0: $Matrix4dc$Type): $Matrix4d
public "sub"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "origin"(arg0: $Vector3d$Type): $Vector3d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix3x2dc$Type): $Matrix4d
public "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix4dc$Type): $Matrix4d
public "mul"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix4fc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix4f$Type): $Matrix4d
public "mul"(arg0: $Matrix3x2fc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix3x2fc$Type): $Matrix4d
public "mul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: double, arg14: double, arg15: double): $Matrix4d
public "mul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double, arg12: double, arg13: double, arg14: double, arg15: double, arg16: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix3x2dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul"(arg0: $Matrix4x3dc$Type): $Matrix4d
public "shadow"(arg0: $Vector4dc$Type, arg1: double, arg2: double, arg3: double, arg4: double): $Matrix4d
public "shadow"(arg0: $Vector4dc$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Matrix4d$Type): $Matrix4d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4dc$Type): $Matrix4d
public "shadow"(arg0: $Vector4dc$Type, arg1: $Matrix4dc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: $Matrix4d$Type): $Matrix4d
public "shadow"(arg0: $Vector4d$Type, arg1: $Matrix4d$Type): $Matrix4d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Matrix4d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4dc$Type, arg5: $Matrix4d$Type): $Matrix4d
public "m12"(arg0: double): $Matrix4d
public "m12"(): double
public "m10"(): double
public "m10"(arg0: double): $Matrix4d
public "m13"(arg0: double): $Matrix4d
public "m13"(): double
public "m11"(arg0: double): $Matrix4d
public "m11"(): double
public "translation"(arg0: $Vector3dc$Type): $Matrix4d
public "translation"(arg0: $Vector3fc$Type): $Matrix4d
public "translation"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "scaling"(arg0: $Vector3dc$Type): $Matrix4d
public "scaling"(arg0: double): $Matrix4d
public "scaling"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "invert"(arg0: $Matrix4d$Type): $Matrix4d
public "invert"(): $Matrix4d
public "translate"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "translate"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "translate"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "translate"(arg0: $Vector3fc$Type): $Matrix4d
public "translate"(arg0: $Vector3dc$Type): $Matrix4d
public "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotationY"(arg0: double): $Matrix4d
public "rotationX"(arg0: double): $Matrix4d
public "rotation"(arg0: $Quaternionfc$Type): $Matrix4d
public "rotation"(arg0: $AxisAngle4f$Type): $Matrix4d
public "rotation"(arg0: double, arg1: $Vector3dc$Type): $Matrix4d
public "rotation"(arg0: $Quaterniondc$Type): $Matrix4d
public "rotation"(arg0: $AxisAngle4d$Type): $Matrix4d
public "rotation"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotation"(arg0: double, arg1: $Vector3fc$Type): $Matrix4d
public "frustumAabb"(arg0: $Vector3d$Type, arg1: $Vector3d$Type): $Matrix4d
public "projectedGridRange"(arg0: $Matrix4dc$Type, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "orthoCrop"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "rotateZYX"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotateZYX"(arg0: $Vector3d$Type): $Matrix4d
public "rotateYXZ"(arg0: $Vector3d$Type): $Matrix4d
public "rotateYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
public "rotateXYZ"(arg0: $Vector3d$Type): $Matrix4d
public "rotateXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
public "rotationXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotationYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotationZYX"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "positiveX"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalZ"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "rotateLocalZ"(arg0: double): $Matrix4d
public "rotateLocalY"(arg0: double): $Matrix4d
public "rotateLocalY"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "positiveY"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalX"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "rotateLocalX"(arg0: double): $Matrix4d
public "positiveZ"(arg0: $Vector3d$Type): $Vector3d
public "transformAab"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
public "transformAab"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Vector3d$Type, arg7: $Vector3d$Type): $Matrix4d
public "trapezoidCrop"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Matrix4d
public "mulAffine"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "rotationTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "rotationTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "affineSpan"(arg0: $Vector3d$Type, arg1: $Vector3d$Type, arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
public "testPoint"(arg0: double, arg1: double, arg2: double): boolean
public "mapZYnX"(): $Matrix4d
public "mapZYnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZnXnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZnXnY"(): $Matrix4d
public "mapXZY"(): $Matrix4d
public "mapXZY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZXY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZXY"(): $Matrix4d
public "mapZnXY"(): $Matrix4d
public "mapZnXY"(arg0: $Matrix4d$Type): $Matrix4d
public "testSphere"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "mapYZX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapYZX"(): $Matrix4d
public "mapYXZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapYXZ"(): $Matrix4d
public "mapYXnZ"(): $Matrix4d
public "mapYXnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapYnXnZ"(): $Matrix4d
public "mapYnXnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapYnZX"(): $Matrix4d
public "mapYnZX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapYnZnX"(): $Matrix4d
public "mapYnZnX"(arg0: $Matrix4d$Type): $Matrix4d
public "withLookAtUp"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "withLookAtUp"(arg0: $Vector3dc$Type): $Matrix4d
public "withLookAtUp"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "withLookAtUp"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "obliqueZ"(arg0: double, arg1: double): $Matrix4d
public "obliqueZ"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
public "mapXZnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapXZnY"(): $Matrix4d
public "mapXnZY"(): $Matrix4d
public "mapXnZY"(arg0: $Matrix4d$Type): $Matrix4d
public "testAab"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): boolean
public "mapYZnX"(): $Matrix4d
public "mapYZnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZXnY"(): $Matrix4d
public "mapZXnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapXnZnY"(): $Matrix4d
public "mapXnZnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapXnYnZ"(): $Matrix4d
public "mapXnYnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZYX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZYX"(): $Matrix4d
public "mapYnXZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapYnXZ"(): $Matrix4d
public "mapnXnYnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnXnYnZ"(): $Matrix4d
public "mapnXnYZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnXnYZ"(): $Matrix4d
public "mapnXnZY"(): $Matrix4d
public "mapnXnZY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYXnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYXnZ"(): $Matrix4d
public "mapnYXZ"(): $Matrix4d
public "mapnYXZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYnXZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYnXZ"(): $Matrix4d
public "mapnYnZnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYnZnX"(): $Matrix4d
public "mapnZYX"(): $Matrix4d
public "mapnZYX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnXZnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnXZnY"(): $Matrix4d
public "mapnZnYX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnZnYX"(): $Matrix4d
public "mapnZnXY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnZnXY"(): $Matrix4d
public "mapnXYnZ"(): $Matrix4d
public "mapnXYnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnXZY"(): $Matrix4d
public "mapnXZY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZnYnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapZnYnX"(): $Matrix4d
public "mapnZXnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnZXnY"(): $Matrix4d
public "mapnYnZX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYnZX"(): $Matrix4d
public "negateY"(arg0: $Matrix4d$Type): $Matrix4d
public "negateY"(): $Matrix4d
public "mapnYZnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYZnX"(): $Matrix4d
public "mapnZnXnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnZnXnY"(): $Matrix4d
public "mapnXnZnY"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnXnZnY"(): $Matrix4d
public "mapnZXY"(): $Matrix4d
public "mapnZXY"(arg0: $Matrix4d$Type): $Matrix4d
public "negateX"(arg0: $Matrix4d$Type): $Matrix4d
public "negateX"(): $Matrix4d
public "mapnZnYnX"(): $Matrix4d
public "mapnZnYnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYnXnZ"(): $Matrix4d
public "mapnYnXnZ"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnZYnX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnZYnX"(): $Matrix4d
public "negateZ"(arg0: $Matrix4d$Type): $Matrix4d
public "negateZ"(): $Matrix4d
public "mapZnYX"(): $Matrix4d
public "mapZnYX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYZX"(arg0: $Matrix4d$Type): $Matrix4d
public "mapnYZX"(): $Matrix4d
public "set4x3"(arg0: $Matrix4x3dc$Type): $Matrix4d
public "set4x3"(arg0: $Matrix4x3fc$Type): $Matrix4d
public "set4x3"(arg0: $Matrix4dc$Type): $Matrix4d
public "mul0"(arg0: $Matrix4dc$Type): $Matrix4d
public "mul0"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "assume"(arg0: integer): $Matrix4d
public "setTransposed"(arg0: $Matrix4dc$Type): $Matrix4d
public "set3x3"(arg0: $Matrix3dc$Type): $Matrix4d
public "set3x3"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulAffineR"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulAffineR"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mulLocal"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mulLocal"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulLocalAffine"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulLocalAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul3x3"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4d
public "mul3x3"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
public "invertAffine"(arg0: $Matrix4d$Type): $Matrix4d
public "invertAffine"(): $Matrix4d
public "mulOrthoAffine"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulOrthoAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "add4x3"(arg0: $Matrix4fc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "add4x3"(arg0: $Matrix4fc$Type): $Matrix4d
public "add4x3"(arg0: $Matrix4dc$Type): $Matrix4d
public "add4x3"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "invertPerspective"(): $Matrix4d
public "invertPerspective"(arg0: $Matrix4d$Type): $Matrix4d
public "determinant"(): double
public "determinant3x3"(): double
public "determinantAffine"(): double
public "fma4x3"(arg0: $Matrix4dc$Type, arg1: double): $Matrix4d
public "fma4x3"(arg0: $Matrix4dc$Type, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
public "mulComponentWise"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulComponentWise"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "sub4x3"(arg0: $Matrix4dc$Type): $Matrix4d
public "sub4x3"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "invertOrtho"(): $Matrix4d
public "invertOrtho"(arg0: $Matrix4d$Type): $Matrix4d
public "invertFrustum"(): $Matrix4d
public "invertFrustum"(arg0: $Matrix4d$Type): $Matrix4d
public "transpose3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "transpose3x3"(): $Matrix4d
public "transpose3x3"(arg0: $Matrix4d$Type): $Matrix4d
public "setTranslation"(arg0: $Vector3dc$Type): $Matrix4d
public "setTranslation"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "get4x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "get3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "rotationTowardsXY"(arg0: double, arg1: double): $Matrix4d
public "getTranslation"(arg0: $Vector3d$Type): $Vector3d
public "getScale"(arg0: $Vector3d$Type): $Vector3d
public "get4x3Transposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get4x3Transposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get4x3Transposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x3Transposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "setRotationYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "setRotationXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "setRotationZYX"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotateAffineXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "rotateAffineXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "translationRotate"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniondc$Type): $Matrix4d
public "translationRotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Matrix4d
public "translationRotate"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type): $Matrix4d
public "transformPosition"(arg0: $Vector3d$Type): $Vector3d
public "transformPosition"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformPosition"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformTranspose"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformTranspose"(arg0: $Vector4d$Type): $Vector4d
public "transformTranspose"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "transformDirection"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformDirection"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformDirection"(arg0: $Vector3f$Type): $Vector3f
public "transformDirection"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
public "transformDirection"(arg0: $Vector3d$Type): $Vector3d
public "transformDirection"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "scaleXY"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
public "scaleXY"(arg0: double, arg1: double): $Matrix4d
public "scaleLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "scaleLocal"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "scaleLocal"(arg0: double): $Matrix4d
public "scaleLocal"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "transformProject"(arg0: $Vector4dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformProject"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector3d$Type): $Vector3d
public "transformProject"(arg0: $Vector3d$Type): $Vector3d
public "transformProject"(arg0: $Vector4d$Type): $Vector4d
public "transformProject"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformProject"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "transformProject"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformProject"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformAffine"(arg0: $Vector4d$Type): $Vector4d
public "transformAffine"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformAffine"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
public "rotateTowardsXY"(arg0: double, arg1: double): $Matrix4d
public "rotateTowardsXY"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
public "rotateTranslation"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateTranslation"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "rotateLocal"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "rotateLocal"(arg0: $Quaterniondc$Type): $Matrix4d
public "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateLocal"(arg0: $Quaternionfc$Type): $Matrix4d
public "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotateAffineYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotateAffineYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "translateLocal"(arg0: $Vector3dc$Type): $Matrix4d
public "translateLocal"(arg0: $Vector3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "translateLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "translateLocal"(arg0: $Vector3fc$Type): $Matrix4d
public "translateLocal"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "rotateAffine"(arg0: $Quaterniondc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateAffine"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotateAffine"(arg0: $Quaternionfc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "rotateAffine"(arg0: $Quaternionfc$Type): $Matrix4d
public "rotateAffine"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "rotateAffine"(arg0: $Quaterniondc$Type): $Matrix4d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setOrthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setOrthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "setOrthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setOrthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "rotateAffineZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4d$Type): $Matrix4d
public "rotateAffineZYX"(arg0: double, arg1: double, arg2: double): $Matrix4d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "tile"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Matrix4d
public "tile"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Matrix4d$Type): $Matrix4d
public "perspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "perspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
public "perspective"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "perspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setPerspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setPerspective"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "ortho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "ortho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "lookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4d
public "lookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
public "lookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "lookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4d$Type): $Matrix4d
public "lookAtPerspective"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setOrtho2D"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "setLookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setLookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "setOrtho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "ortho2D"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "ortho2D"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "setLookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4d
public "setLookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "setLookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4d
public "setLookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "lookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4d
public "lookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
public "lookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "lookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4d$Type): $Matrix4d
public "frustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "frustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "frustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "frustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "perspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "perspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "perspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "perspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
public "setPerspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setPerspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "frustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "frustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "frustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "frustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "setFrustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setFrustumLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "perspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "perspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "perspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4d$Type): $Matrix4d
public "perspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "setFromIntrinsic"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: integer, arg6: integer, arg7: double, arg8: double): $Matrix4d
public "setFrustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "setFrustum"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setPerspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "setPerspectiveRect"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotateAroundAffine"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "unproject"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector3d$Type): $Vector3d
public "unproject"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector4d$Type): $Vector4d
public "unproject"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector4d$Type): $Vector4d
public "unproject"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector3d$Type): $Vector3d
public "rotationAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotateAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "rotateAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotateAroundLocal"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double): $Matrix4d
public "rotateAroundLocal"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "unprojectInv"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector4d$Type): $Vector4d
public "unprojectInv"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector4d$Type): $Vector4d
public "unprojectInv"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector3d$Type): $Vector3d
public "unprojectInv"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector3d$Type): $Vector3d
public "unprojectInvRay"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector3d$Type, arg4: $Vector3d$Type): $Matrix4d
public "unprojectInvRay"(arg0: $Vector2dc$Type, arg1: (integer)[], arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
public "project"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector3d$Type): $Vector3d
public "project"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector4d$Type): $Vector4d
public "project"(arg0: double, arg1: double, arg2: double, arg3: (integer)[], arg4: $Vector3d$Type): $Vector3d
public "project"(arg0: $Vector3dc$Type, arg1: (integer)[], arg2: $Vector4d$Type): $Vector4d
public "reflection"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "reflection"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "reflection"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "reflection"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "unprojectRay"(arg0: $Vector2dc$Type, arg1: (integer)[], arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4d
public "unprojectRay"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector3d$Type, arg4: $Vector3d$Type): $Matrix4d
public "frustumCorner"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
public "getRow"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
public "getRow"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
public "setColumn"(arg0: integer, arg1: $Vector4dc$Type): $Matrix4d
public "cofactor3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "cofactor3x3"(arg0: $Matrix4d$Type): $Matrix4d
public "cofactor3x3"(): $Matrix4d
public "setRow"(arg0: integer, arg1: $Vector4dc$Type): $Matrix4d
public "normalize3x3"(arg0: $Matrix4d$Type): $Matrix4d
public "normalize3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "normalize3x3"(): $Matrix4d
public "frustumPlane"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
public "perspectiveOrigin"(arg0: $Vector3d$Type): $Vector3d
public "perspectiveNear"(): double
public "perspectiveFar"(): double
public "frustumRayDir"(arg0: double, arg1: double, arg2: $Vector3d$Type): $Vector3d
public "perspectiveFov"(): double
public "billboardSpherical"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "billboardSpherical"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "arcball"(arg0: double, arg1: $Vector3dc$Type, arg2: double, arg3: double): $Matrix4d
public "arcball"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "arcball"(arg0: double, arg1: $Vector3dc$Type, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "arcball"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "isAffine"(): boolean
public "originAffine"(arg0: $Vector3d$Type): $Vector3d
public "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "setFloats"(arg0: $ByteBuffer$Type): $Matrix4d
public "setFloats"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix4d
public "setOrtho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setOrtho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "m23"(arg0: double): $Matrix4d
public "m23"(): double
public "m31"(): double
public "m31"(arg0: double): $Matrix4d
public "m20"(arg0: double): $Matrix4d
public "m20"(): double
public "m21"(): double
public "m21"(arg0: double): $Matrix4d
public "m33"(arg0: double): $Matrix4d
public "m33"(): double
public "m30"(): double
public "m30"(arg0: double): $Matrix4d
public "m22"(): double
public "m22"(arg0: double): $Matrix4d
public "m32"(arg0: double): $Matrix4d
public "m32"(): double
public "transpose"(arg0: $Matrix4d$Type): $Matrix4d
public "transpose"(): $Matrix4d
public static "perspectiveOffCenterViewFromRectangle"(arg0: $Vector3d$Type, arg1: $Vector3d$Type, arg2: $Vector3d$Type, arg3: $Vector3d$Type, arg4: double, arg5: boolean, arg6: $Matrix4d$Type, arg7: $Matrix4d$Type): void
public "determineProperties"(): $Matrix4d
public "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getRowColumn"(arg0: integer, arg1: integer): double
public "setRowColumn"(arg0: integer, arg1: integer, arg2: double): $Matrix4d
public "setFromAddress"(arg0: long): $Matrix4d
public "getToAddress"(arg0: long): $Matrix4dc
public "m03"(arg0: double): $Matrix4d
public "m03"(): double
public "m00"(): double
public "m00"(arg0: double): $Matrix4d
public "m01"(arg0: double): $Matrix4d
public "m01"(): double
public "m02"(): double
public "m02"(arg0: double): $Matrix4d
public "rotateZ"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "rotateZ"(arg0: double): $Matrix4d
public "rotateX"(arg0: double): $Matrix4d
public "rotateX"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "rotateY"(arg0: double): $Matrix4d
public "rotateY"(arg0: double, arg1: $Matrix4d$Type): $Matrix4d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4d$Type): $Matrix4d
public "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "reflect"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4d$Type): $Matrix4d
public "reflect"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type): $Matrix4d
public "lerp"(arg0: $Matrix4dc$Type, arg1: double): $Matrix4d
public "lerp"(arg0: $Matrix4dc$Type, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
public "rotationZ"(arg0: double): $Matrix4d
public "getColumn"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
public "getColumn"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
public "translationRotateScaleInvert"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type, arg2: double): $Matrix4d
public "translationRotateScaleInvert"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "translationRotateScaleInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type): $Matrix4d
public "translationRotateScaleInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: double): $Matrix4d
public "translationRotateScaleInvert"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double): $Matrix4d
public "billboardCylindrical"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "translationRotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4d
public "translationRotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "perspectiveFrustumSlice"(arg0: double, arg1: double, arg2: $Matrix4d$Type): $Matrix4d
public "setPerspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setPerspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "perspectiveInvOrigin"(arg0: $Vector3d$Type): $Vector3d
public "setPerspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setPerspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "translationRotateInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type): $Matrix4d
public "translationRotateInvert"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Matrix4d
public "setOrthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4d
public "setOrthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4d
public "lookAtPerspectiveLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "perspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenterFovLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "translationRotateScaleMulAffine"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: $Matrix4d$Type): $Matrix4d
public "translationRotateScaleMulAffine"(arg0: $Vector3fc$Type, arg1: $Quaterniondc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "perspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "perspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenter"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "setPerspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "setPerspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "translationRotateScale"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Matrix4d
public "translationRotateScale"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type, arg2: $Vector3dc$Type): $Matrix4d
public "translationRotateScale"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type): $Matrix4d
public "translationRotateScale"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double): $Matrix4d
public "translationRotateScale"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type, arg2: double): $Matrix4d
public "translationRotateScale"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: double): $Matrix4d
public "perspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4d$Type): $Matrix4d
public "perspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4d
public "perspectiveOffCenterFov"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4d
public "mulTranslationAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mul4x3ComponentWise"(arg0: $Matrix4dc$Type): $Matrix4d
public "mul4x3ComponentWise"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "mulPerspectiveAffine"(arg0: $Matrix4dc$Type): $Matrix4d
public "mulPerspectiveAffine"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "mulPerspectiveAffine"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "invertPerspectiveView"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "invertPerspectiveView"(arg0: $Matrix4dc$Type, arg1: $Matrix4d$Type): $Matrix4d
public "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
get "finite"(): boolean
set "4x3"(value: $Matrix4x3dc$Type)
set "4x3"(value: $Matrix4x3fc$Type)
set "4x3"(value: $Matrix4dc$Type)
set "transposed"(value: $Matrix4dc$Type)
set "3x3"(value: $Matrix3dc$Type)
set "3x3"(value: $Matrix4dc$Type)
get "affine"(): boolean
set "floats"(value: $ByteBuffer$Type)
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4d$Type = ($Matrix4d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4d_ = $Matrix4d$Type;
}}
declare module "packages/org/joml/$Matrix4f" {
import {$Matrix4x3f, $Matrix4x3f$Type} from "packages/org/joml/$Matrix4x3f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Matrix4f implements $Externalizable, $Cloneable, $Matrix4fc {

constructor(arg0: $Matrix4dc$Type)
constructor(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float, arg12: float, arg13: float, arg14: float, arg15: float)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4fc$Type, arg3: $Vector4fc$Type)
constructor()
constructor(arg0: $Matrix3fc$Type)
constructor(arg0: $Matrix4fc$Type)
constructor(arg0: $Matrix4x3fc$Type)

public "add"(arg0: $Matrix4fc$Type): $Matrix4f
public "add"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: integer): float
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: (float)[]): (float)[]
public "equals"(arg0: $Matrix4fc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: float): $Matrix4f
public "scale"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "scale"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "scale"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "scale"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "scale"(arg0: $Vector3fc$Type): $Matrix4f
public "transform"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transform"(arg0: $Vector4f$Type): $Vector4f
public "identity"(): $Matrix4f
public "set"(arg0: $Quaterniondc$Type): $Matrix4f
public "set"(arg0: $AxisAngle4d$Type): $Matrix4f
public "set"(arg0: $Quaternionfc$Type): $Matrix4f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float, arg12: float, arg13: float, arg14: float, arg15: float): $Matrix4f
public "set"(arg0: (float)[], arg1: integer): $Matrix4f
public "set"(arg0: (float)[]): $Matrix4f
public "set"(arg0: $ByteBuffer$Type): $Matrix4f
public "set"(arg0: $Matrix4fc$Type): $Matrix4f
public "set"(arg0: $Matrix4x3fc$Type): $Matrix4f
public "set"(arg0: $Matrix4dc$Type): $Matrix4f
public "set"(arg0: $Matrix3fc$Type): $Matrix4f
public "set"(arg0: $AxisAngle4f$Type): $Matrix4f
public "set"(arg0: integer, arg1: integer, arg2: float): $Matrix4f
public "set"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4fc$Type, arg3: $Vector4fc$Type): $Matrix4f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix4f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix4f
public "set"(arg0: $FloatBuffer$Type): $Matrix4f
public "properties"(): integer
public "zero"(): $Matrix4f
public "pick"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: (integer)[], arg5: $Matrix4f$Type): $Matrix4f
public "pick"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: (integer)[]): $Matrix4f
public "isFinite"(): boolean
public "swap"(arg0: $Matrix4f$Type): $Matrix4f
public "rotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "rotate"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotate"(arg0: float, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
public "rotate"(arg0: $AxisAngle4f$Type): $Matrix4f
public "rotate"(arg0: $Quaternionfc$Type): $Matrix4f
public "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotate"(arg0: float, arg1: $Vector3fc$Type): $Matrix4f
public "normal"(): $Matrix4f
public "normal"(arg0: $Matrix4f$Type): $Matrix4f
public "normal"(arg0: $Matrix3f$Type): $Matrix3f
public "sub"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "sub"(arg0: $Matrix4fc$Type): $Matrix4f
public "origin"(arg0: $Vector3f$Type): $Vector3f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float, arg12: float, arg13: float, arg14: float, arg15: float, arg16: $Matrix4f$Type): $Matrix4f
public "mul"(arg0: $Matrix3x2fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mul"(arg0: $Matrix3x2fc$Type): $Matrix4f
public "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mul"(arg0: $Matrix4x3fc$Type): $Matrix4f
public "mul"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mul"(arg0: $Matrix4fc$Type): $Matrix4f
public "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float, arg12: float, arg13: float, arg14: float, arg15: float): $Matrix4f
public "shadow"(arg0: $Vector4f$Type, arg1: $Matrix4f$Type): $Matrix4f
public "shadow"(arg0: $Vector4f$Type, arg1: $Matrix4fc$Type, arg2: $Matrix4f$Type): $Matrix4f
public "shadow"(arg0: $Vector4f$Type, arg1: float, arg2: float, arg3: float, arg4: float): $Matrix4f
public "shadow"(arg0: $Vector4f$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: $Matrix4f$Type): $Matrix4f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4fc$Type, arg5: $Matrix4f$Type): $Matrix4f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): $Matrix4f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: $Matrix4f$Type): $Matrix4f
public "m12"(arg0: float): $Matrix4f
public "m12"(): float
public "m10"(): float
public "m10"(arg0: float): $Matrix4f
public "m13"(): float
public "m13"(arg0: float): $Matrix4f
public "m11"(arg0: float): $Matrix4f
public "m11"(): float
public "translation"(arg0: $Vector3fc$Type): $Matrix4f
public "translation"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "scaling"(arg0: float): $Matrix4f
public "scaling"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "scaling"(arg0: $Vector3fc$Type): $Matrix4f
public "invert"(): $Matrix4f
public "invert"(arg0: $Matrix4f$Type): $Matrix4f
public "translate"(arg0: $Vector3fc$Type): $Matrix4f
public "translate"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "translate"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotationY"(arg0: float): $Matrix4f
public "rotationX"(arg0: float): $Matrix4f
public "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "getRotation"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "rotation"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "rotation"(arg0: $AxisAngle4f$Type): $Matrix4f
public "rotation"(arg0: float, arg1: $Vector3fc$Type): $Matrix4f
public "rotation"(arg0: $Quaternionfc$Type): $Matrix4f
public "frustumAabb"(arg0: $Vector3f$Type, arg1: $Vector3f$Type): $Matrix4f
public "projectedGridRange"(arg0: $Matrix4fc$Type, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "orthoCrop"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotateZYX"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "rotateZYX"(arg0: $Vector3f$Type): $Matrix4f
public "rotateYXZ"(arg0: $Vector3f$Type): $Matrix4f
public "rotateYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
public "rotateXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotateXYZ"(arg0: $Vector3fc$Type): $Matrix4f
public "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
public "rotationXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotationYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotationZYX"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "positiveX"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalZ"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "rotateLocalZ"(arg0: float): $Matrix4f
public "rotateLocalY"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "rotateLocalY"(arg0: float): $Matrix4f
public "positiveY"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalX"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "rotateLocalX"(arg0: float): $Matrix4f
public "positiveZ"(arg0: $Vector3f$Type): $Vector3f
public "transformAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Vector3f$Type, arg7: $Vector3f$Type): $Matrix4f
public "transformAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
public "trapezoidCrop"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): $Matrix4f
public "mulAffine"(arg0: $Matrix4fc$Type): $Matrix4f
public "mulAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
public "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "rotationTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "rotationTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "affineSpan"(arg0: $Vector3f$Type, arg1: $Vector3f$Type, arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
public "testPoint"(arg0: float, arg1: float, arg2: float): boolean
public "mapZYnX"(): $Matrix4f
public "mapZYnX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZnXnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZnXnY"(): $Matrix4f
public "mapXZY"(): $Matrix4f
public "mapXZY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZXY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZXY"(): $Matrix4f
public "mapZnXY"(): $Matrix4f
public "mapZnXY"(arg0: $Matrix4f$Type): $Matrix4f
public "testSphere"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "mapYZX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYZX"(): $Matrix4f
public "mapYXZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYXZ"(): $Matrix4f
public "mapYXnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYXnZ"(): $Matrix4f
public "mapYnXnZ"(): $Matrix4f
public "mapYnXnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYnZX"(): $Matrix4f
public "mapYnZX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYnZnX"(): $Matrix4f
public "mapYnZnX"(arg0: $Matrix4f$Type): $Matrix4f
public "withLookAtUp"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "withLookAtUp"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "withLookAtUp"(arg0: $Vector3fc$Type): $Matrix4f
public "withLookAtUp"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "obliqueZ"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
public "obliqueZ"(arg0: float, arg1: float): $Matrix4f
public "mapXZnY"(): $Matrix4f
public "mapXZnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapXnZY"(): $Matrix4f
public "mapXnZY"(arg0: $Matrix4f$Type): $Matrix4f
public "testAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): boolean
public "mapYZnX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYZnX"(): $Matrix4f
public "mapZXnY"(): $Matrix4f
public "mapZXnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapXnZnY"(): $Matrix4f
public "mapXnZnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapXnYnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapXnYnZ"(): $Matrix4f
public "mapZYX"(): $Matrix4f
public "mapZYX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYnXZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapYnXZ"(): $Matrix4f
public "mapnXnYnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnXnYnZ"(): $Matrix4f
public "mapnXnYZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnXnYZ"(): $Matrix4f
public "mapnXnZY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnXnZY"(): $Matrix4f
public "mapnYXnZ"(): $Matrix4f
public "mapnYXnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYXZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYXZ"(): $Matrix4f
public "mapnYnXZ"(): $Matrix4f
public "mapnYnXZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYnZnX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYnZnX"(): $Matrix4f
public "mapnZYX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZYX"(): $Matrix4f
public "mapnXZnY"(): $Matrix4f
public "mapnXZnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZnYX"(): $Matrix4f
public "mapnZnYX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZnXY"(): $Matrix4f
public "mapnZnXY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnXYnZ"(): $Matrix4f
public "mapnXYnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnXZY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnXZY"(): $Matrix4f
public "mapZnYnX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZnYnX"(): $Matrix4f
public "mapnZXnY"(): $Matrix4f
public "mapnZXnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYnZX"(): $Matrix4f
public "mapnYnZX"(arg0: $Matrix4f$Type): $Matrix4f
public "negateY"(): $Matrix4f
public "negateY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYZnX"(): $Matrix4f
public "mapnYZnX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZnXnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZnXnY"(): $Matrix4f
public "mapnXnZnY"(): $Matrix4f
public "mapnXnZnY"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZXY"(): $Matrix4f
public "mapnZXY"(arg0: $Matrix4f$Type): $Matrix4f
public "negateX"(): $Matrix4f
public "negateX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZnYnX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZnYnX"(): $Matrix4f
public "mapnYnXnZ"(): $Matrix4f
public "mapnYnXnZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnZYnX"(): $Matrix4f
public "mapnZYnX"(arg0: $Matrix4f$Type): $Matrix4f
public "negateZ"(): $Matrix4f
public "negateZ"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZnYX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapZnYX"(): $Matrix4f
public "mapnYZX"(arg0: $Matrix4f$Type): $Matrix4f
public "mapnYZX"(): $Matrix4f
public "set4x3"(arg0: $Matrix4f$Type): $Matrix4f
public "set4x3"(arg0: $Matrix4x3fc$Type): $Matrix4f
public "mul0"(arg0: $Matrix4fc$Type): $Matrix4f
public "mul0"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "assume"(arg0: integer): $Matrix4f
public "setTransposed"(arg0: $ByteBuffer$Type): $Matrix4f
public "setTransposed"(arg0: $FloatBuffer$Type): $Matrix4f
public "setTransposed"(arg0: (float)[], arg1: integer): $Matrix4f
public "setTransposed"(arg0: (float)[]): $Matrix4f
public "setTransposed"(arg0: $Matrix4fc$Type): $Matrix4f
public "set3x3"(arg0: $Matrix3fc$Type): $Matrix4f
public "set3x3"(arg0: $Matrix4f$Type): $Matrix4f
public "mulAffineR"(arg0: $Matrix4fc$Type): $Matrix4f
public "mulAffineR"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mulLocal"(arg0: $Matrix4fc$Type): $Matrix4f
public "mulLocal"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mulLocalAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mulLocalAffine"(arg0: $Matrix4fc$Type): $Matrix4f
public "mul3x3"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4f
public "mul3x3"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
public "invertAffine"(): $Matrix4f
public "invertAffine"(arg0: $Matrix4f$Type): $Matrix4f
public "mulOrthoAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mulOrthoAffine"(arg0: $Matrix4fc$Type): $Matrix4f
public "add4x3"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "add4x3"(arg0: $Matrix4fc$Type): $Matrix4f
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "invertPerspective"(arg0: $Matrix4f$Type): $Matrix4f
public "invertPerspective"(): $Matrix4f
public "determinant"(): float
public "determinant3x3"(): float
public "determinantAffine"(): float
public "fma4x3"(arg0: $Matrix4fc$Type, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
public "fma4x3"(arg0: $Matrix4fc$Type, arg1: float): $Matrix4f
public "mulComponentWise"(arg0: $Matrix4fc$Type): $Matrix4f
public "mulComponentWise"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "sub4x3"(arg0: $Matrix4f$Type): $Matrix4f
public "sub4x3"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "invertOrtho"(arg0: $Matrix4f$Type): $Matrix4f
public "invertOrtho"(): $Matrix4f
public "invertFrustum"(arg0: $Matrix4f$Type): $Matrix4f
public "invertFrustum"(): $Matrix4f
public "transpose3x3"(arg0: $Matrix4f$Type): $Matrix4f
public "transpose3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "transpose3x3"(): $Matrix4f
public "setTranslation"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "setTranslation"(arg0: $Vector3fc$Type): $Matrix4f
public "get4x3"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get4x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "get4x3"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get4x3"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x3"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "get3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "rotationTowardsXY"(arg0: float, arg1: float): $Matrix4f
public "getTranslation"(arg0: $Vector3f$Type): $Vector3f
public "getScale"(arg0: $Vector3f$Type): $Vector3f
public "get4x3Transposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get4x3Transposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get4x3Transposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x3Transposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get3x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get3x4"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get3x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get3x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "setRotationYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "setRotationXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "setRotationZYX"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotateAffineXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "rotateAffineXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "translationRotate"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionfc$Type): $Matrix4f
public "translationRotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): $Matrix4f
public "translationRotate"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type): $Matrix4f
public "transformPosition"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformPosition"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformPosition"(arg0: $Vector3f$Type): $Vector3f
public "transformTranspose"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "transformTranspose"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformTranspose"(arg0: $Vector4f$Type): $Vector4f
public "transformDirection"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformDirection"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformDirection"(arg0: $Vector3f$Type): $Vector3f
public "scaleXY"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
public "scaleXY"(arg0: float, arg1: float): $Matrix4f
public "scaleLocal"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "scaleLocal"(arg0: float): $Matrix4f
public "scaleLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "scaleLocal"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "transformProject"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformProject"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformProject"(arg0: $Vector3f$Type): $Vector3f
public "transformProject"(arg0: $Vector4f$Type): $Vector4f
public "transformProject"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformProject"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "transformProject"(arg0: $Vector4fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformProject"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector3f$Type): $Vector3f
public "transformAffine"(arg0: $Vector4f$Type): $Vector4f
public "transformAffine"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
public "transformAffine"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "rotateTowardsXY"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
public "rotateTowardsXY"(arg0: float, arg1: float): $Matrix4f
public "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotateTranslation"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "rotateLocal"(arg0: $Quaternionfc$Type): $Matrix4f
public "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotateAffineYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "rotateAffineYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "translateLocal"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "translateLocal"(arg0: $Vector3fc$Type): $Matrix4f
public "translateLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "rotateAffine"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "rotateAffine"(arg0: $Quaternionfc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "rotateAffine"(arg0: $Quaternionfc$Type): $Matrix4f
public "rotateAffine"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
public "setOrthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setOrthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "setOrthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "setOrthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "rotateAffineZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4f$Type): $Matrix4f
public "rotateAffineZYX"(arg0: float, arg1: float, arg2: float): $Matrix4f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "tile"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Matrix4f$Type): $Matrix4f
public "tile"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Matrix4f
public "perspective"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "perspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "perspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "perspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
public "setPerspective"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setPerspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "ortho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "ortho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "lookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4f
public "lookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "lookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4f$Type): $Matrix4f
public "lookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
public "lookAtPerspective"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "setOrtho2D"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setLookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "setLookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "setOrtho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "ortho2D"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "ortho2D"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setLookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "setLookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4f
public "setLookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "setLookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4f
public "lookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
public "lookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4f
public "lookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4f$Type): $Matrix4f
public "lookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "frustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "frustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "frustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "frustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "perspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
public "perspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "perspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "perspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setPerspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "setPerspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "frustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "frustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "frustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "frustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "setFrustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "setFrustumLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "perspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "perspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "perspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4f$Type): $Matrix4f
public "perspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setFromIntrinsic"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer, arg7: float, arg8: float): $Matrix4f
public "setFrustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "setFrustum"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "setPerspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "setPerspectiveRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "rotateAroundAffine"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "unproject"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector3f$Type): $Vector3f
public "unproject"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector4f$Type): $Vector4f
public "unproject"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector3f$Type): $Vector3f
public "unproject"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector4f$Type): $Vector4f
public "rotationAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float): $Matrix4f
public "rotateAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float): $Matrix4f
public "rotateAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "rotateAroundLocal"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float): $Matrix4f
public "rotateAroundLocal"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "unprojectInv"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector3f$Type): $Vector3f
public "unprojectInv"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector4f$Type): $Vector4f
public "unprojectInv"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector3f$Type): $Vector3f
public "unprojectInv"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector4f$Type): $Vector4f
public "unprojectInvRay"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector3f$Type, arg4: $Vector3f$Type): $Matrix4f
public "unprojectInvRay"(arg0: $Vector2fc$Type, arg1: (integer)[], arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
public "project"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector4f$Type): $Vector4f
public "project"(arg0: float, arg1: float, arg2: float, arg3: (integer)[], arg4: $Vector3f$Type): $Vector3f
public "project"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector4f$Type): $Vector4f
public "project"(arg0: $Vector3fc$Type, arg1: (integer)[], arg2: $Vector3f$Type): $Vector3f
public "reflection"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "reflection"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "reflection"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "reflection"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "unprojectRay"(arg0: $Vector2fc$Type, arg1: (integer)[], arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4f
public "unprojectRay"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector3f$Type, arg4: $Vector3f$Type): $Matrix4f
public "frustumCorner"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
public "getRow"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
public "getRow"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
public "setColumn"(arg0: integer, arg1: $Vector4fc$Type): $Matrix4f
public "cofactor3x3"(): $Matrix4f
public "cofactor3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "cofactor3x3"(arg0: $Matrix4f$Type): $Matrix4f
public "setRow"(arg0: integer, arg1: $Vector4fc$Type): $Matrix4f
public "normalize3x3"(): $Matrix4f
public "normalize3x3"(arg0: $Matrix4f$Type): $Matrix4f
public "normalize3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "frustumPlane"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
public "perspectiveOrigin"(arg0: $Vector3f$Type): $Vector3f
public "perspectiveNear"(): float
public "perspectiveFar"(): float
public "frustumRayDir"(arg0: float, arg1: float, arg2: $Vector3f$Type): $Vector3f
public "perspectiveFov"(): float
public "billboardSpherical"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "billboardSpherical"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "arcball"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "arcball"(arg0: float, arg1: $Vector3fc$Type, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "arcball"(arg0: float, arg1: $Vector3fc$Type, arg2: float, arg3: float): $Matrix4f
public "arcball"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "isAffine"(): boolean
public "originAffine"(arg0: $Vector3f$Type): $Vector3f
public "setOrtho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "setOrtho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "m23"(arg0: float): $Matrix4f
public "m23"(): float
public "m31"(arg0: float): $Matrix4f
public "m31"(): float
public "m20"(arg0: float): $Matrix4f
public "m20"(): float
public "m21"(arg0: float): $Matrix4f
public "m21"(): float
public "m33"(arg0: float): $Matrix4f
public "m33"(): float
public "m30"(): float
public "m30"(arg0: float): $Matrix4f
public "m22"(arg0: float): $Matrix4f
public "m22"(): float
public "m32"(arg0: float): $Matrix4f
public "m32"(): float
public "transpose"(arg0: $Matrix4f$Type): $Matrix4f
public "transpose"(): $Matrix4f
public static "perspectiveOffCenterViewFromRectangle"(arg0: $Vector3f$Type, arg1: $Vector3f$Type, arg2: $Vector3f$Type, arg3: $Vector3f$Type, arg4: float, arg5: boolean, arg6: $Matrix4f$Type, arg7: $Matrix4f$Type): void
public "determineProperties"(): $Matrix4f
public "getRowColumn"(arg0: integer, arg1: integer): float
public "setRowColumn"(arg0: integer, arg1: integer, arg2: float): $Matrix4f
public "setFromAddress"(arg0: long): $Matrix4f
public "getToAddress"(arg0: long): $Matrix4fc
public "m03"(): float
public "m03"(arg0: float): $Matrix4f
public "m00"(): float
public "m00"(arg0: float): $Matrix4f
public "m01"(): float
public "m01"(arg0: float): $Matrix4f
public "m02"(arg0: float): $Matrix4f
public "m02"(): float
public "rotateZ"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "rotateZ"(arg0: float): $Matrix4f
public "rotateX"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "rotateX"(arg0: float): $Matrix4f
public "rotateY"(arg0: float): $Matrix4f
public "rotateY"(arg0: float, arg1: $Matrix4f$Type): $Matrix4f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
public "reflect"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4f$Type): $Matrix4f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4f$Type): $Matrix4f
public "reflect"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type): $Matrix4f
public "lerp"(arg0: $Matrix4fc$Type, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
public "lerp"(arg0: $Matrix4fc$Type, arg1: float): $Matrix4f
public "rotationZ"(arg0: float): $Matrix4f
public "getColumn"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
public "getColumn"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
public "translationRotateScaleInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: float): $Matrix4f
public "translationRotateScaleInvert"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): $Matrix4f
public "translationRotateScaleInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "billboardCylindrical"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "translationRotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "translationRotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4f
public "perspectiveFrustumSlice"(arg0: float, arg1: float, arg2: $Matrix4f$Type): $Matrix4f
public "setPerspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "setPerspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "perspectiveInvOrigin"(arg0: $Vector3f$Type): $Vector3f
public "setPerspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "setPerspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "translationRotateInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type): $Matrix4f
public "translationRotateInvert"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): $Matrix4f
public "setOrthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4f
public "setOrthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4f
public "lookAtPerspectiveLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4f$Type): $Matrix4f
public "perspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "perspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "perspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "perspectiveOffCenterFovLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "translationRotateScaleMulAffine"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4f$Type): $Matrix4f
public "translationRotateScaleMulAffine"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: $Matrix4f$Type): $Matrix4f
public "perspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "perspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "perspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "perspectiveOffCenter"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "setPerspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "setPerspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "translationRotateScale"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): $Matrix4f
public "translationRotateScale"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): $Matrix4f
public "translationRotateScale"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type): $Matrix4f
public "translationRotateScale"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: float): $Matrix4f
public "perspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4f$Type): $Matrix4f
public "perspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4f
public "perspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4f$Type): $Matrix4f
public "perspectiveOffCenterFov"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4f
public "mulTranslationAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mul4x3ComponentWise"(arg0: $Matrix4fc$Type): $Matrix4f
public "mul4x3ComponentWise"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "mulPerspectiveAffine"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mulPerspectiveAffine"(arg0: $Matrix4fc$Type): $Matrix4f
public "mulPerspectiveAffine"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "mulPerspectiveAffine"(arg0: $Matrix4x3fc$Type): $Matrix4f
public "setTransposedFromAddress"(arg0: long): $Matrix4f
public "invertPerspectiveView"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "invertPerspectiveView"(arg0: $Matrix4fc$Type, arg1: $Matrix4f$Type): $Matrix4f
public "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
get "finite"(): boolean
set "4x3"(value: $Matrix4f$Type)
set "4x3"(value: $Matrix4x3fc$Type)
set "transposed"(value: $ByteBuffer$Type)
set "transposed"(value: $FloatBuffer$Type)
set "transposed"(value: (float)[])
set "transposed"(value: $Matrix4fc$Type)
set "3x3"(value: $Matrix3fc$Type)
set "3x3"(value: $Matrix4f$Type)
get "affine"(): boolean
set "fromAddress"(value: long)
set "transposedFromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4f$Type = ($Matrix4f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4f_ = $Matrix4f$Type;
}}
declare module "packages/org/joml/$Matrix4x3dc" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Matrix4x3dc {

 "add"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "add"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Matrix4d$Type): $Matrix4d
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "get"(arg0: (double)[]): (double)[]
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: (double)[], arg1: integer): (double)[]
 "get"(arg0: (float)[]): (float)[]
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "equals"(arg0: $Matrix4x3dc$Type, arg1: double): boolean
 "fma"(arg0: $Matrix4x3dc$Type, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "fma"(arg0: $Matrix4x3fc$Type, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "scale"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "scale"(arg0: $Vector3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "scale"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "transform"(arg0: $Vector4d$Type): $Vector4d
 "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "properties"(): integer
 "pick"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: (integer)[], arg5: $Matrix4x3d$Type): $Matrix4x3d
 "isFinite"(): boolean
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotate"(arg0: double, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "rotate"(arg0: double, arg1: $Vector3fc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "rotate"(arg0: $AxisAngle4d$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "rotate"(arg0: $Quaterniondc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "normal"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "normal"(arg0: $Matrix3d$Type): $Matrix3d
 "sub"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "sub"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "origin"(arg0: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "mul"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "shadow"(arg0: $Vector4dc$Type, arg1: $Matrix4x3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: $Matrix4x3d$Type): $Matrix4x3d
 "shadow"(arg0: $Vector4dc$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Matrix4x3d$Type): $Matrix4x3d
 "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3dc$Type, arg5: $Matrix4x3d$Type): $Matrix4x3d
 "m12"(): double
 "m10"(): double
 "m11"(): double
 "invert"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "translate"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "translate"(arg0: $Vector3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
 "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
 "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "positiveX"(arg0: $Vector3d$Type): $Vector3d
 "positiveY"(arg0: $Vector3d$Type): $Vector3d
 "positiveZ"(arg0: $Vector3d$Type): $Vector3d
 "transformAab"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4x3d
 "transformAab"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Vector3d$Type, arg7: $Vector3d$Type): $Matrix4x3d
 "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "mapZYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZnXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapXZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZnXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYnXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYnZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYnZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "obliqueZ"(arg0: double, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "mapXZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapXnZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapXnZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapXnYnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapYnXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXnYnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXnYZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXnZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYnXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYnZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZnYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZnXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXYnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZnYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYnZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "negateY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZnXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnXnZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "negateX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZnYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYnXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnZYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "negateZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapZnYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mapnYZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "mulTranslation"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "mulTranslation"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "mul3x3"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4x3d$Type): $Matrix4x3d
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: (double)[]): (double)[]
 "getTransposed"(arg0: (double)[], arg1: integer): (double)[]
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "determinant"(): double
 "mulComponentWise"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "invertOrtho"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "transpose3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "transpose3x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "getTranslation"(arg0: $Vector3d$Type): $Vector3d
 "getScale"(arg0: $Vector3d$Type): $Vector3d
 "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "transformPosition"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformPosition"(arg0: $Vector3d$Type): $Vector3d
 "transformDirection"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformDirection"(arg0: $Vector3d$Type): $Vector3d
 "scaleXY"(arg0: double, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "scaleLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "rotateTranslation"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "rotateTranslation"(arg0: $Quaterniondc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4x3d$Type): $Matrix4x3d
 "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "rotateLocal"(arg0: $Quaterniondc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "translateLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "translateLocal"(arg0: $Vector3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4x3d$Type): $Matrix4x3d
 "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4x3d$Type): $Matrix4x3d
 "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "ortho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "lookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "lookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4x3d$Type): $Matrix4x3d
 "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4x3d$Type): $Matrix4x3d
 "ortho2D"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "lookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4x3d$Type): $Matrix4x3d
 "lookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4x3d$Type): $Matrix4x3d
 "rotateAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "getRow"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
 "cofactor3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "cofactor3x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "normalize3x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "normalize3x3"(arg0: $Matrix3d$Type): $Matrix3d
 "frustumPlane"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
 "arcball"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "arcball"(arg0: double, arg1: $Vector3dc$Type, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "mulOrtho"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "get4x4"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get4x4"(arg0: (float)[], arg1: integer): (float)[]
 "get4x4"(arg0: (double)[]): (double)[]
 "get4x4"(arg0: (double)[], arg1: integer): (double)[]
 "get4x4"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get4x4"(arg0: (float)[]): (float)[]
 "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "m31"(): double
 "m20"(): double
 "m21"(): double
 "m30"(): double
 "m22"(): double
 "m32"(): double
 "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getToAddress"(arg0: long): $Matrix4x3dc
 "m00"(): double
 "m01"(): double
 "m02"(): double
 "rotateZ"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotateX"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "rotateY"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
 "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "reflect"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
 "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
 "lerp"(arg0: $Matrix4x3dc$Type, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
 "getColumn"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
 "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
}

export namespace $Matrix4x3dc {
const PLANE_NX: integer
const PLANE_PX: integer
const PLANE_NY: integer
const PLANE_PY: integer
const PLANE_NZ: integer
const PLANE_PZ: integer
const PROPERTY_IDENTITY: byte
const PROPERTY_TRANSLATION: byte
const PROPERTY_ORTHONORMAL: byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4x3dc$Type = ($Matrix4x3dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4x3dc_ = $Matrix4x3dc$Type;
}}
declare module "packages/org/joml/$Matrix4x3fc" {
import {$Matrix4x3f, $Matrix4x3f$Type} from "packages/org/joml/$Matrix4x3f"
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Matrix4x3fc {

 "add"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Matrix4f$Type): $Matrix4f
 "get"(arg0: $Matrix4d$Type): $Matrix4d
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "get"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: (float)[]): (float)[]
 "equals"(arg0: $Matrix4x3fc$Type, arg1: float): boolean
 "fma"(arg0: $Matrix4x3fc$Type, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "scale"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "scale"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "scale"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "transform"(arg0: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "properties"(): integer
 "pick"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: (integer)[], arg5: $Matrix4x3f$Type): $Matrix4x3f
 "isFinite"(): boolean
 "rotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "rotate"(arg0: float, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "normal"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "normal"(arg0: $Matrix3f$Type): $Matrix3f
 "sub"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "origin"(arg0: $Vector3f$Type): $Vector3f
 "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: $Matrix4x3f$Type): $Matrix4x3f
 "shadow"(arg0: $Vector4fc$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: $Matrix4x3f$Type): $Matrix4x3f
 "shadow"(arg0: $Vector4fc$Type, arg1: $Matrix4x3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3fc$Type, arg5: $Matrix4x3f$Type): $Matrix4x3f
 "m12"(): float
 "m10"(): float
 "m11"(): float
 "invert"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "invert"(arg0: $Matrix4f$Type): $Matrix4f
 "translate"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "getRotation"(arg0: $AxisAngle4d$Type): $AxisAngle4d
 "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
 "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
 "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
 "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "positiveX"(arg0: $Vector3f$Type): $Vector3f
 "positiveY"(arg0: $Vector3f$Type): $Vector3f
 "positiveZ"(arg0: $Vector3f$Type): $Vector3f
 "transformAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Vector3f$Type, arg7: $Vector3f$Type): $Matrix4x3f
 "transformAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4x3f
 "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "mapZYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZnXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapXZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZnXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYnXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYnZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYnZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "withLookAtUp"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "withLookAtUp"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "obliqueZ"(arg0: float, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "mapXZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapXnZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapXnZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapXnYnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapYnXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXnYnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXnYZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXnZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYnXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYnZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZnYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZnXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXYnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZnYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYnZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "negateY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZnXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnXnZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "negateX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZnYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYnXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnZYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "negateZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapZnYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mapnYZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "mulTranslation"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "mul3x3"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4x3f$Type): $Matrix4x3f
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: (float)[]): (float)[]
 "getTransposed"(arg0: (float)[], arg1: integer): (float)[]
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "determinant"(): float
 "mulComponentWise"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "invertOrtho"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "transpose3x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "transpose3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "getTranslation"(arg0: $Vector3f$Type): $Vector3f
 "getScale"(arg0: $Vector3f$Type): $Vector3f
 "get3x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get3x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get3x4"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get3x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "transformPosition"(arg0: $Vector3f$Type): $Vector3f
 "transformPosition"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: $Vector3f$Type): $Vector3f
 "transformDirection"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "scaleXY"(arg0: float, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "scaleLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "rotateTranslation"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4x3f$Type): $Matrix4x3f
 "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "translateLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4x3f$Type): $Matrix4x3f
 "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4x3f$Type): $Matrix4x3f
 "ortho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "lookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4x3f$Type): $Matrix4x3f
 "lookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4x3f$Type): $Matrix4x3f
 "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "ortho2D"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "lookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4x3f$Type): $Matrix4x3f
 "lookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4x3f$Type): $Matrix4x3f
 "rotateAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "getRow"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
 "cofactor3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "cofactor3x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "normalize3x3"(arg0: $Matrix3f$Type): $Matrix3f
 "normalize3x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "frustumPlane"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
 "arcball"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "arcball"(arg0: float, arg1: $Vector3fc$Type, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "mulOrtho"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "get4x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get4x4"(arg0: (float)[], arg1: integer): (float)[]
 "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get4x4"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get4x4"(arg0: (float)[]): (float)[]
 "m31"(): float
 "m20"(): float
 "m21"(): float
 "m30"(): float
 "m22"(): float
 "m32"(): float
 "getToAddress"(arg0: long): $Matrix4x3fc
 "m00"(): float
 "m01"(): float
 "m02"(): float
 "rotateZ"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "rotateX"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "rotateY"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
 "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
 "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
 "reflect"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "lerp"(arg0: $Matrix4x3fc$Type, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
 "getColumn"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
 "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
 "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
 "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
}

export namespace $Matrix4x3fc {
const PLANE_NX: integer
const PLANE_PX: integer
const PLANE_NY: integer
const PLANE_PY: integer
const PLANE_NZ: integer
const PLANE_PZ: integer
const PROPERTY_IDENTITY: byte
const PROPERTY_TRANSLATION: byte
const PROPERTY_ORTHONORMAL: byte
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4x3fc$Type = ($Matrix4x3fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4x3fc_ = $Matrix4x3fc$Type;
}}
declare module "packages/org/joml/$Matrix3x2fc" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2f, $Matrix3x2f$Type} from "packages/org/joml/$Matrix3x2f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Matrix3x2fc {

 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: (float)[], arg1: integer): (float)[]
 "get"(arg0: (float)[]): (float)[]
 "get"(arg0: $Matrix3x2f$Type): $Matrix3x2f
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "equals"(arg0: $Matrix3x2fc$Type, arg1: float): boolean
 "scale"(arg0: $Vector2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "scale"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "scale"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
 "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3f$Type, arg1: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3f$Type): $Vector3f
 "isFinite"(): boolean
 "rotate"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "origin"(arg0: $Vector2f$Type): $Vector2f
 "view"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3x2f$Type): $Matrix3x2f
 "mul"(arg0: $Matrix3x2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "m10"(): float
 "m11"(): float
 "invert"(arg0: $Matrix3x2f$Type): $Matrix3x2f
 "translate"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
 "translate"(arg0: $Vector2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "positiveX"(arg0: $Vector2f$Type): $Vector2f
 "rotateTo"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type, arg2: $Matrix3x2f$Type): $Matrix3x2f
 "positiveY"(arg0: $Vector2f$Type): $Vector2f
 "testPoint"(arg0: float, arg1: float): boolean
 "mulLocal"(arg0: $Matrix3x2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "determinant"(): float
 "get3x3"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get3x3"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get3x3"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get3x3"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get3x3"(arg0: (float)[], arg1: integer): (float)[]
 "get3x3"(arg0: (float)[]): (float)[]
 "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3x2f$Type): $Matrix3x2f
 "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3x2f$Type): $Matrix3x2f
 "transformPosition"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "transformPosition"(arg0: $Vector2f$Type): $Vector2f
 "transformPosition"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "transformDirection"(arg0: $Vector2f$Type): $Vector2f
 "transformDirection"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "transformDirection"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "scaleLocal"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
 "scaleLocal"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3x2f$Type): $Matrix3x2f
 "scaleAroundLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix3x2f$Type): $Matrix3x2f
 "rotateLocal"(arg0: float, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "translateLocal"(arg0: $Vector2fc$Type, arg1: $Matrix3x2f$Type): $Matrix3x2f
 "translateLocal"(arg0: float, arg1: float, arg2: $Matrix3x2f$Type): $Matrix3x2f
 "unproject"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector2f$Type): $Vector2f
 "unprojectInv"(arg0: float, arg1: float, arg2: (integer)[], arg3: $Vector2f$Type): $Vector2f
 "rotateAbout"(arg0: float, arg1: float, arg2: float, arg3: $Matrix3x2f$Type): $Matrix3x2f
 "viewArea"(arg0: (float)[]): (float)[]
 "testCircle"(arg0: float, arg1: float, arg2: float): boolean
 "testAar"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
 "get4x4"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get4x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get4x4"(arg0: (float)[]): (float)[]
 "get4x4"(arg0: (float)[], arg1: integer): (float)[]
 "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "m20"(): float
 "m21"(): float
 "getToAddress"(arg0: long): $Matrix3x2fc
 "m00"(): float
 "m01"(): float
 "normalizedPositiveY"(arg0: $Vector2f$Type): $Vector2f
 "normalizedPositiveX"(arg0: $Vector2f$Type): $Vector2f
}

export namespace $Matrix3x2fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3x2fc$Type = ($Matrix3x2fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3x2fc_ = $Matrix3x2fc$Type;
}}
declare module "packages/org/joml/$Matrix3x2dc" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2d, $Matrix3x2d$Type} from "packages/org/joml/$Matrix3x2d"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector2dc, $Vector2dc$Type} from "packages/org/joml/$Vector2dc"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"

export interface $Matrix3x2dc {

 "get"(arg0: $Matrix3x2d$Type): $Matrix3x2d
 "get"(arg0: (double)[]): (double)[]
 "get"(arg0: (double)[], arg1: integer): (double)[]
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "equals"(arg0: $Matrix3x2dc$Type, arg1: double): boolean
 "scale"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
 "scale"(arg0: $Vector2fc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "scale"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "scale"(arg0: $Vector2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector3d$Type): $Vector3d
 "isFinite"(): boolean
 "rotate"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "origin"(arg0: $Vector2d$Type): $Vector2d
 "view"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3x2d$Type): $Matrix3x2d
 "mul"(arg0: $Matrix3x2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "m10"(): double
 "m11"(): double
 "invert"(arg0: $Matrix3x2d$Type): $Matrix3x2d
 "translate"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
 "translate"(arg0: $Vector2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "positiveX"(arg0: $Vector2d$Type): $Vector2d
 "rotateTo"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type, arg2: $Matrix3x2d$Type): $Matrix3x2d
 "positiveY"(arg0: $Vector2d$Type): $Vector2d
 "testPoint"(arg0: double, arg1: double): boolean
 "mulLocal"(arg0: $Matrix3x2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "determinant"(): double
 "get3x3"(arg0: (double)[], arg1: integer): (double)[]
 "get3x3"(arg0: (double)[]): (double)[]
 "get3x3"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get3x3"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get3x3"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get3x3"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3x2d$Type): $Matrix3x2d
 "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3x2d$Type): $Matrix3x2d
 "transformPosition"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "transformPosition"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "transformPosition"(arg0: $Vector2d$Type): $Vector2d
 "transformDirection"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "transformDirection"(arg0: $Vector2d$Type): $Vector2d
 "transformDirection"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "scaleLocal"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "scaleLocal"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
 "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix3x2d$Type): $Matrix3x2d
 "scaleAroundLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3x2d$Type): $Matrix3x2d
 "rotateLocal"(arg0: double, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "translateLocal"(arg0: double, arg1: double, arg2: $Matrix3x2d$Type): $Matrix3x2d
 "translateLocal"(arg0: $Vector2dc$Type, arg1: $Matrix3x2d$Type): $Matrix3x2d
 "unproject"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector2d$Type): $Vector2d
 "unprojectInv"(arg0: double, arg1: double, arg2: (integer)[], arg3: $Vector2d$Type): $Vector2d
 "rotateAbout"(arg0: double, arg1: double, arg2: double, arg3: $Matrix3x2d$Type): $Matrix3x2d
 "viewArea"(arg0: (double)[]): (double)[]
 "testCircle"(arg0: double, arg1: double, arg2: double): boolean
 "testAar"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
 "get4x4"(arg0: (double)[]): (double)[]
 "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get4x4"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "get4x4"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get4x4"(arg0: (double)[], arg1: integer): (double)[]
 "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "m20"(): double
 "m21"(): double
 "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getToAddress"(arg0: long): $Matrix3x2dc
 "m00"(): double
 "m01"(): double
 "normalizedPositiveY"(arg0: $Vector2d$Type): $Vector2d
 "normalizedPositiveX"(arg0: $Vector2d$Type): $Vector2d
}

export namespace $Matrix3x2dc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3x2dc$Type = ($Matrix3x2dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3x2dc_ = $Matrix3x2dc$Type;
}}
declare module "packages/org/joml/$Quaternionfc" {
import {$Matrix4x3f, $Matrix4x3f$Type} from "packages/org/joml/$Matrix4x3f"
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Quaternionfc {

 "add"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "add"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
 "get"(arg0: $Matrix4f$Type): $Matrix4f
 "get"(arg0: $AxisAngle4d$Type): $AxisAngle4d
 "get"(arg0: $Matrix3d$Type): $Matrix3d
 "get"(arg0: $AxisAngle4f$Type): $AxisAngle4f
 "get"(arg0: $Matrix4x3d$Type): $Matrix4x3d
 "get"(arg0: $Matrix4x3f$Type): $Matrix4x3f
 "get"(arg0: $Matrix4d$Type): $Matrix4d
 "get"(arg0: $Matrix3f$Type): $Matrix3f
 "get"(arg0: $Quaternionf$Type): $Quaternionf
 "get"(arg0: $Quaterniond$Type): $Quaterniond
 "equals"(arg0: $Quaternionfc$Type, arg1: float): boolean
 "equals"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
 "scale"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "x"(): float
 "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector4d$Type): $Vector4d
 "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transform"(arg0: $Vector3d$Type): $Vector3d
 "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
 "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
 "z"(): float
 "normalize"(arg0: $Quaternionf$Type): $Quaternionf
 "w"(): float
 "y"(): float
 "isFinite"(): boolean
 "difference"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "div"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "mul"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
 "mul"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "invert"(arg0: $Quaternionf$Type): $Quaternionf
 "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
 "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
 "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
 "transformUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
 "transformUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformUnit"(arg0: $Vector4f$Type): $Vector4f
 "transformUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformUnit"(arg0: $Vector4d$Type): $Vector4d
 "transformUnit"(arg0: $Vector3f$Type): $Vector3f
 "transformUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transformUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
 "transformUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformPositiveZ"(arg0: $Vector4d$Type): $Vector4d
 "transformPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "transformPositiveZ"(arg0: $Vector4f$Type): $Vector4f
 "transformPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "conjugate"(arg0: $Quaternionf$Type): $Quaternionf
 "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
 "transformPositiveY"(arg0: $Vector4d$Type): $Vector4d
 "transformPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "transformPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "transformPositiveY"(arg0: $Vector4f$Type): $Vector4f
 "nlerpIterative"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
 "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
 "getEulerAnglesYXZ"(arg0: $Vector3f$Type): $Vector3f
 "getEulerAnglesZXY"(arg0: $Vector3f$Type): $Vector3f
 "integrate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
 "nlerp"(arg0: $Quaternionfc$Type, arg1: float, arg2: $Quaternionf$Type): $Quaternionf
 "slerp"(arg0: $Quaternionfc$Type, arg1: float, arg2: $Quaternionf$Type): $Quaternionf
 "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Quaternionf$Type): $Quaternionf
 "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Quaternionf$Type): $Quaternionf
 "positiveX"(arg0: $Vector3f$Type): $Vector3f
 "rotateTo"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Quaternionf$Type): $Quaternionf
 "rotateTo"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Quaternionf$Type): $Quaternionf
 "rotateLocalZ"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "rotateLocalY"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "conjugateBy"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "positiveY"(arg0: $Vector3f$Type): $Vector3f
 "rotateLocalX"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "positiveZ"(arg0: $Vector3f$Type): $Vector3f
 "angle"(): float
 "rotateZ"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "rotateX"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "rotateAxis"(arg0: float, arg1: $Vector3fc$Type, arg2: $Quaternionf$Type): $Quaternionf
 "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
 "lengthSquared"(): float
 "rotateY"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
 "getAsMatrix3f"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getAsMatrix3f"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getAsMatrix4x3f"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getAsMatrix4x3f"(arg0: $FloatBuffer$Type): $FloatBuffer
 "getAsMatrix4f"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getAsMatrix4f"(arg0: $FloatBuffer$Type): $FloatBuffer
 "transformPositiveX"(arg0: $Vector4f$Type): $Vector4f
 "transformPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "transformPositiveX"(arg0: $Vector4d$Type): $Vector4d
 "transformPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "premul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
 "premul"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "transformInverse"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transformInverse"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
 "transformInverse"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformInverse"(arg0: $Vector4f$Type): $Vector4f
 "transformInverse"(arg0: $Vector3f$Type): $Vector3f
 "transformInverse"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformInverse"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
 "transformInverse"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transformInverse"(arg0: $Vector3d$Type): $Vector3d
 "transformInverse"(arg0: $Vector4d$Type): $Vector4d
 "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformInverse"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformUnitPositiveX"(arg0: $Vector4f$Type): $Vector4f
 "transformUnitPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "transformUnitPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveX"(arg0: $Vector4d$Type): $Vector4d
 "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: $Vector4d$Type): $Vector4d
 "transformInverseUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transformInverseUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformInverseUnit"(arg0: $Vector4f$Type): $Vector4f
 "transformInverseUnit"(arg0: $Vector3f$Type): $Vector3f
 "transformInverseUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
 "transformInverseUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformInverseUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "transformUnitPositiveY"(arg0: $Vector4d$Type): $Vector4d
 "transformUnitPositiveY"(arg0: $Vector4f$Type): $Vector4f
 "transformUnitPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveZ"(arg0: $Vector4d$Type): $Vector4d
 "transformUnitPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "transformUnitPositiveZ"(arg0: $Vector4f$Type): $Vector4f
}

export namespace $Quaternionfc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quaternionfc$Type = ($Quaternionfc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quaternionfc_ = $Quaternionfc$Type;
}}
declare module "packages/org/joml/$Quaterniondc" {
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"

export interface $Quaterniondc {

 "add"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
 "add"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "get"(arg0: $AxisAngle4d$Type): $AxisAngle4d
 "get"(arg0: $AxisAngle4f$Type): $AxisAngle4f
 "get"(arg0: $Matrix4f$Type): $Matrix4f
 "get"(arg0: $Matrix4d$Type): $Matrix4d
 "get"(arg0: $Matrix3f$Type): $Matrix3f
 "get"(arg0: $Matrix3d$Type): $Matrix3d
 "get"(arg0: $Quaterniond$Type): $Quaterniond
 "get"(arg0: $Quaternionf$Type): $Quaternionf
 "equals"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
 "equals"(arg0: $Quaterniondc$Type, arg1: double): boolean
 "scale"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "x"(): double
 "transform"(arg0: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector4d$Type): $Vector4d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
 "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transform"(arg0: $Vector3f$Type): $Vector3f
 "transform"(arg0: $Vector3d$Type): $Vector3d
 "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "dot"(arg0: $Quaterniondc$Type): double
 "z"(): double
 "normalize"(arg0: $Quaterniond$Type): $Quaterniond
 "w"(): double
 "y"(): double
 "isFinite"(): boolean
 "difference"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "div"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "mul"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "mul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
 "mul"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "invert"(arg0: $Quaterniond$Type): $Quaterniond
 "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
 "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
 "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
 "transformUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformUnit"(arg0: $Vector4f$Type): $Vector4f
 "transformUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformUnit"(arg0: $Vector4d$Type): $Vector4d
 "transformUnit"(arg0: $Vector3f$Type): $Vector3f
 "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transformUnit"(arg0: $Vector3d$Type): $Vector3d
 "transformUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
 "transformUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
 "transformPositiveZ"(arg0: $Vector4f$Type): $Vector4f
 "transformPositiveZ"(arg0: $Vector4d$Type): $Vector4d
 "transformPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "transformPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "conjugate"(arg0: $Quaterniond$Type): $Quaterniond
 "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
 "transformPositiveY"(arg0: $Vector4f$Type): $Vector4f
 "transformPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "transformPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "transformPositiveY"(arg0: $Vector4d$Type): $Vector4d
 "nlerpIterative"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
 "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
 "getEulerAnglesYXZ"(arg0: $Vector3d$Type): $Vector3d
 "getEulerAnglesZXY"(arg0: $Vector3d$Type): $Vector3d
 "integrate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
 "nlerp"(arg0: $Quaterniondc$Type, arg1: double, arg2: $Quaterniond$Type): $Quaterniond
 "slerp"(arg0: $Quaterniondc$Type, arg1: double, arg2: $Quaterniond$Type): $Quaterniond
 "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Quaterniond$Type): $Quaterniond
 "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Quaterniond$Type): $Quaterniond
 "positiveX"(arg0: $Vector3d$Type): $Vector3d
 "rotateTo"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Quaterniond$Type): $Quaterniond
 "rotateTo"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Quaterniond$Type): $Quaterniond
 "rotateLocalZ"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "rotateLocalY"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "conjugateBy"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "positiveY"(arg0: $Vector3d$Type): $Vector3d
 "rotateLocalX"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "positiveZ"(arg0: $Vector3d$Type): $Vector3d
 "angle"(): double
 "rotateZ"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "rotateX"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "rotateAxis"(arg0: double, arg1: $Vector3dc$Type, arg2: $Quaterniond$Type): $Quaterniond
 "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
 "lengthSquared"(): double
 "rotateY"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
 "transformPositiveX"(arg0: $Vector4f$Type): $Vector4f
 "transformPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "transformPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "transformPositiveX"(arg0: $Vector4d$Type): $Vector4d
 "premul"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "premul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
 "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transformInverse"(arg0: $Vector3f$Type): $Vector3f
 "transformInverse"(arg0: $Vector3d$Type): $Vector3d
 "transformInverse"(arg0: $Vector4d$Type): $Vector4d
 "transformInverse"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformInverse"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformInverse"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformInverse"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
 "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
 "transformInverse"(arg0: $Vector4f$Type): $Vector4f
 "transformUnitPositiveX"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveX"(arg0: $Vector4f$Type): $Vector4f
 "transformUnitPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "transformUnitPositiveX"(arg0: $Vector4d$Type): $Vector4d
 "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
 "transformInverseUnit"(arg0: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "transformInverseUnit"(arg0: $Vector4f$Type): $Vector4f
 "transformInverseUnit"(arg0: $Vector4d$Type): $Vector4d
 "transformInverseUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "transformInverseUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
 "transformInverseUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
 "transformInverseUnit"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveY"(arg0: $Vector4f$Type): $Vector4f
 "transformUnitPositiveY"(arg0: $Vector4d$Type): $Vector4d
 "transformUnitPositiveY"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveY"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
 "transformUnitPositiveZ"(arg0: $Vector4f$Type): $Vector4f
 "transformUnitPositiveZ"(arg0: $Vector3d$Type): $Vector3d
 "transformUnitPositiveZ"(arg0: $Vector3f$Type): $Vector3f
 "transformUnitPositiveZ"(arg0: $Vector4d$Type): $Vector4d
}

export namespace $Quaterniondc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quaterniondc$Type = ($Quaterniondc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quaterniondc_ = $Quaterniondc$Type;
}}
declare module "packages/org/joml/$Quaterniond" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Quaterniond implements $Externalizable, $Cloneable, $Quaterniondc {
 "x": double
 "y": double
 "z": double
 "w": double

constructor(arg0: $AxisAngle4d$Type)
constructor(arg0: $AxisAngle4f$Type)
constructor(arg0: $Quaternionfc$Type)
constructor()
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor(arg0: $Quaterniondc$Type)

public "add"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "add"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
public "add"(arg0: $Quaterniondc$Type): $Quaterniond
public "add"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "get"(arg0: $Matrix3f$Type): $Matrix3f
public "get"(arg0: $Matrix3d$Type): $Matrix3d
public "get"(arg0: $Quaterniond$Type): $Quaterniond
public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "get"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "get"(arg0: $Quaternionf$Type): $Quaternionf
public "equals"(arg0: any): boolean
public "equals"(arg0: $Quaterniondc$Type, arg1: double): boolean
public "equals"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: double): $Quaterniond
public "scale"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "x"(): double
public "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4f$Type): $Vector4f
public "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3d$Type): $Vector3d
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
public "dot"(arg0: $Quaterniondc$Type): double
public "identity"(): $Quaterniond
public "set"(arg0: $Quaterniondc$Type): $Quaterniond
public "set"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "set"(arg0: $AxisAngle4d$Type): $Quaterniond
public "set"(arg0: $AxisAngle4f$Type): $Quaterniond
public "set"(arg0: $Quaternionfc$Type): $Quaterniond
public "z"(): double
public "normalize"(arg0: $Quaterniond$Type): $Quaterniond
public "normalize"(): $Quaterniond
public "w"(): double
public "y"(): double
public "isFinite"(): boolean
public "difference"(arg0: $Quaterniondc$Type): $Quaterniond
public "difference"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "div"(arg0: $Quaterniondc$Type): $Quaterniond
public "mul"(arg0: $Quaterniondc$Type): $Quaterniond
public "mul"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "mul"(arg0: double): $Quaterniond
public "mul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
public "mul"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "mul"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "scaling"(arg0: double): $Quaterniond
public "invert"(arg0: $Quaterniond$Type): $Quaterniond
public "invert"(): $Quaterniond
public "rotationY"(arg0: double): $Quaterniond
public "rotationX"(arg0: double): $Quaterniond
public "rotateZYX"(arg0: double, arg1: double, arg2: double): $Quaterniond
public "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
public "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
public "rotateYXZ"(arg0: double, arg1: double, arg2: double): $Quaterniond
public "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
public "transformUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformUnit"(arg0: $Vector4d$Type): $Vector4d
public "transformUnit"(arg0: $Vector4f$Type): $Vector4f
public "transformUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transformUnit"(arg0: $Vector3f$Type): $Vector3f
public "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
public "transformUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformUnit"(arg0: $Vector3d$Type): $Vector3d
public "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
public "transformUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformPositiveZ"(arg0: $Vector4d$Type): $Vector4d
public "transformPositiveZ"(arg0: $Vector4f$Type): $Vector4f
public "transformPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "transformPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "conjugate"(): $Quaterniond
public "conjugate"(arg0: $Quaterniond$Type): $Quaterniond
public "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
public "rotateXYZ"(arg0: double, arg1: double, arg2: double): $Quaterniond
public "transformPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "transformPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "transformPositiveY"(arg0: $Vector4f$Type): $Vector4f
public "transformPositiveY"(arg0: $Vector4d$Type): $Vector4d
public "nlerpIterative"(arg0: $Quaterniondc$Type, arg1: double, arg2: double): $Quaterniond
public static "nlerpIterative"(arg0: ($Quaterniondc$Type)[], arg1: (double)[], arg2: double, arg3: $Quaterniond$Type): $Quaterniond
public "nlerpIterative"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
public "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
public "rotationXYZ"(arg0: double, arg1: double, arg2: double): $Quaterniond
public "rotationYXZ"(arg0: double, arg1: double, arg2: double): $Quaterniond
public "getEulerAnglesYXZ"(arg0: $Vector3d$Type): $Vector3d
public "getEulerAnglesZXY"(arg0: $Vector3d$Type): $Vector3d
public "rotationZYX"(arg0: double, arg1: double, arg2: double): $Quaterniond
public "integrate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
public "integrate"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public static "nlerp"(arg0: ($Quaterniond$Type)[], arg1: (double)[], arg2: $Quaterniond$Type): $Quaterniondc
public "nlerp"(arg0: $Quaterniondc$Type, arg1: double, arg2: $Quaterniond$Type): $Quaterniond
public "nlerp"(arg0: $Quaterniondc$Type, arg1: double): $Quaterniond
public "slerp"(arg0: $Quaterniondc$Type, arg1: double, arg2: $Quaterniond$Type): $Quaterniond
public "slerp"(arg0: $Quaterniondc$Type, arg1: double): $Quaterniond
public static "slerp"(arg0: ($Quaterniond$Type)[], arg1: (double)[], arg2: $Quaterniond$Type): $Quaterniondc
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Quaterniond$Type): $Quaterniond
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Quaterniond
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Quaterniond$Type): $Quaterniond
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Quaterniond
public "positiveX"(arg0: $Vector3d$Type): $Vector3d
public "rotateTo"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Quaterniond
public "rotateTo"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Quaterniond$Type): $Quaterniond
public "rotateTo"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Quaterniond
public "rotateTo"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Quaterniond$Type): $Quaterniond
public "rotateLocalZ"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "rotateLocalZ"(arg0: double): $Quaterniond
public "rotateLocalY"(arg0: double): $Quaterniond
public "rotateLocalY"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "conjugateBy"(arg0: $Quaterniondc$Type): $Quaterniond
public "conjugateBy"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "positiveY"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalX"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "rotateLocalX"(arg0: double): $Quaterniond
public "positiveZ"(arg0: $Vector3d$Type): $Vector3d
public "angle"(): double
public "rotationTo"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Quaterniond
public "rotationTo"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Quaterniond
public "rotateZ"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "rotateZ"(arg0: double): $Quaterniond
public "rotateX"(arg0: double): $Quaterniond
public "rotateX"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "rotateAxis"(arg0: double, arg1: $Vector3dc$Type, arg2: $Quaterniond$Type): $Quaterniond
public "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
public "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "rotateAxis"(arg0: double, arg1: $Vector3dc$Type): $Quaterniond
public "lengthSquared"(): double
public "rotateY"(arg0: double, arg1: $Quaterniond$Type): $Quaterniond
public "rotateY"(arg0: double): $Quaterniond
public "rotationZ"(arg0: double): $Quaterniond
public "setFromNormalized"(arg0: $Matrix4x3fc$Type): $Quaterniond
public "setFromNormalized"(arg0: $Matrix4fc$Type): $Quaterniond
public "setFromNormalized"(arg0: $Matrix4x3dc$Type): $Quaterniond
public "setFromNormalized"(arg0: $Matrix3dc$Type): $Quaterniond
public "setFromNormalized"(arg0: $Matrix4dc$Type): $Quaterniond
public "setFromNormalized"(arg0: $Matrix3fc$Type): $Quaterniond
public "setAngleAxis"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "setAngleAxis"(arg0: double, arg1: $Vector3dc$Type): $Quaterniond
public "rotationAxis"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "rotationAxis"(arg0: $AxisAngle4f$Type): $Quaterniond
public "fromAxisAngleRad"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "fromAxisAngleRad"(arg0: $Vector3dc$Type, arg1: double): $Quaterniond
public "transformPositiveX"(arg0: $Vector4d$Type): $Vector4d
public "transformPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "transformPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "transformPositiveX"(arg0: $Vector4f$Type): $Vector4f
public "premul"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "premul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Quaterniond$Type): $Quaterniond
public "premul"(arg0: $Quaterniondc$Type): $Quaterniond
public "premul"(arg0: $Quaterniondc$Type, arg1: $Quaterniond$Type): $Quaterniond
public "transformInverse"(arg0: $Vector3d$Type): $Vector3d
public "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
public "transformInverse"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformInverse"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformInverse"(arg0: $Vector4d$Type): $Vector4d
public "transformInverse"(arg0: $Vector4f$Type): $Vector4f
public "transformInverse"(arg0: $Vector3f$Type): $Vector3f
public "transformInverse"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transformInverse"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
public "fromAxisAngleDeg"(arg0: $Vector3dc$Type, arg1: double): $Quaterniond
public "fromAxisAngleDeg"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaterniond
public "setFromUnnormalized"(arg0: $Matrix3fc$Type): $Quaterniond
public "setFromUnnormalized"(arg0: $Matrix4dc$Type): $Quaterniond
public "setFromUnnormalized"(arg0: $Matrix4fc$Type): $Quaterniond
public "setFromUnnormalized"(arg0: $Matrix4x3fc$Type): $Quaterniond
public "setFromUnnormalized"(arg0: $Matrix3dc$Type): $Quaterniond
public "setFromUnnormalized"(arg0: $Matrix4x3dc$Type): $Quaterniond
public "transformUnitPositiveX"(arg0: $Vector4f$Type): $Vector4f
public "transformUnitPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "transformUnitPositiveX"(arg0: $Vector4d$Type): $Vector4d
public "transformUnitPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "transformInverseUnit"(arg0: $Vector4d$Type): $Vector4d
public "transformInverseUnit"(arg0: $Vector4f$Type): $Vector4f
public "transformInverseUnit"(arg0: $Vector3d$Type): $Vector3d
public "transformInverseUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3f$Type): $Vector3f
public "transformInverseUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4f$Type): $Vector4f
public "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformInverseUnit"(arg0: $Vector3f$Type): $Vector3f
public "transformInverseUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transformInverseUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformUnitPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "transformUnitPositiveY"(arg0: $Vector4d$Type): $Vector4d
public "transformUnitPositiveY"(arg0: $Vector4f$Type): $Vector4f
public "transformUnitPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "transformUnitPositiveZ"(arg0: $Vector4f$Type): $Vector4f
public "transformUnitPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "transformUnitPositiveZ"(arg0: $Vector4d$Type): $Vector4d
public "transformUnitPositiveZ"(arg0: $Vector3f$Type): $Vector3f
get "finite"(): boolean
set "fromNormalized"(value: $Matrix4x3fc$Type)
set "fromNormalized"(value: $Matrix4fc$Type)
set "fromNormalized"(value: $Matrix4x3dc$Type)
set "fromNormalized"(value: $Matrix3dc$Type)
set "fromNormalized"(value: $Matrix4dc$Type)
set "fromNormalized"(value: $Matrix3fc$Type)
set "fromUnnormalized"(value: $Matrix3fc$Type)
set "fromUnnormalized"(value: $Matrix4dc$Type)
set "fromUnnormalized"(value: $Matrix4fc$Type)
set "fromUnnormalized"(value: $Matrix4x3fc$Type)
set "fromUnnormalized"(value: $Matrix3dc$Type)
set "fromUnnormalized"(value: $Matrix4x3dc$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quaterniond$Type = ($Quaterniond);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quaterniond_ = $Quaterniond$Type;
}}
declare module "packages/org/joml/$Quaternionf" {
import {$Matrix4x3f, $Matrix4x3f$Type} from "packages/org/joml/$Matrix4x3f"
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Quaternionf implements $Externalizable, $Cloneable, $Quaternionfc {
 "x": float
 "y": float
 "z": float
 "w": float

constructor(arg0: $Quaternionfc$Type)
constructor(arg0: $Quaterniondc$Type)
constructor(arg0: $AxisAngle4f$Type)
constructor(arg0: $AxisAngle4d$Type)
constructor()
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor(arg0: float, arg1: float, arg2: float, arg3: float)

public "add"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "add"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
public "add"(arg0: $Quaternionfc$Type): $Quaternionf
public "add"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: $Matrix3d$Type): $Matrix3d
public "get"(arg0: $Quaterniond$Type): $Quaterniond
public "get"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "get"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "get"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "get"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "get"(arg0: $Quaternionf$Type): $Quaternionf
public "get"(arg0: $Matrix3f$Type): $Matrix3f
public "equals"(arg0: $Quaternionfc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "equals"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "scale"(arg0: float): $Quaternionf
public "x"(): float
public "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transform"(arg0: $Vector4f$Type): $Vector4f
public "transform"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector3f$Type): $Vector3f
public "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transform"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector3d$Type): $Vector3d
public "transform"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
public "dot"(arg0: $Quaternionf$Type): float
public "identity"(): $Quaternionf
public "set"(arg0: $Quaternionfc$Type): $Quaternionf
public "set"(arg0: $Quaterniondc$Type): $Quaternionf
public "set"(arg0: $AxisAngle4f$Type): $Quaternionf
public "set"(arg0: $AxisAngle4d$Type): $Quaternionf
public "set"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "z"(): float
public "normalize"(arg0: $Quaternionf$Type): $Quaternionf
public "normalize"(): $Quaternionf
public "w"(): float
public "y"(): float
public "isFinite"(): boolean
public "difference"(arg0: $Quaternionf$Type): $Quaternionf
public "difference"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "div"(arg0: $Quaternionfc$Type): $Quaternionf
public "div"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "mul"(arg0: $Quaternionfc$Type): $Quaternionf
public "mul"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "mul"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
public "mul"(arg0: float): $Quaternionf
public "mul"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "scaling"(arg0: float): $Quaternionf
public "invert"(): $Quaternionf
public "invert"(arg0: $Quaternionf$Type): $Quaternionf
public "rotationY"(arg0: float): $Quaternionf
public "rotationX"(arg0: float): $Quaternionf
public "rotateZYX"(arg0: float, arg1: float, arg2: float): $Quaternionf
public "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
public "rotateYXZ"(arg0: float, arg1: float, arg2: float): $Quaternionf
public "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
public "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
public "transformUnit"(arg0: $Vector3f$Type): $Vector3f
public "transformUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformUnit"(arg0: $Vector4f$Type): $Vector4f
public "transformUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
public "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
public "transformUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformUnit"(arg0: $Vector4d$Type): $Vector4d
public "transformUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transformUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "transformPositiveZ"(arg0: $Vector4f$Type): $Vector4f
public "transformPositiveZ"(arg0: $Vector4d$Type): $Vector4d
public "transformPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "conjugate"(): $Quaternionf
public "conjugate"(arg0: $Quaternionf$Type): $Quaternionf
public "rotateXYZ"(arg0: float, arg1: float, arg2: float): $Quaternionf
public "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
public "transformPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "transformPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "transformPositiveY"(arg0: $Vector4d$Type): $Vector4d
public "transformPositiveY"(arg0: $Vector4f$Type): $Vector4f
public "nlerpIterative"(arg0: $Quaternionfc$Type, arg1: float, arg2: float): $Quaternionf
public "nlerpIterative"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
public static "nlerpIterative"(arg0: ($Quaternionf$Type)[], arg1: (float)[], arg2: float, arg3: $Quaternionf$Type): $Quaternionfc
public "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
public "rotationXYZ"(arg0: float, arg1: float, arg2: float): $Quaternionf
public "rotationYXZ"(arg0: float, arg1: float, arg2: float): $Quaternionf
public "getEulerAnglesYXZ"(arg0: $Vector3f$Type): $Vector3f
public "getEulerAnglesZXY"(arg0: $Vector3f$Type): $Vector3f
public "rotationZYX"(arg0: float, arg1: float, arg2: float): $Quaternionf
public "integrate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
public "integrate"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "nlerp"(arg0: $Quaternionfc$Type, arg1: float, arg2: $Quaternionf$Type): $Quaternionf
public "nlerp"(arg0: $Quaternionfc$Type, arg1: float): $Quaternionf
public static "nlerp"(arg0: ($Quaternionfc$Type)[], arg1: (float)[], arg2: $Quaternionf$Type): $Quaternionfc
public "slerp"(arg0: $Quaternionfc$Type, arg1: float, arg2: $Quaternionf$Type): $Quaternionf
public static "slerp"(arg0: ($Quaternionf$Type)[], arg1: (float)[], arg2: $Quaternionf$Type): $Quaternionfc
public "slerp"(arg0: $Quaternionfc$Type, arg1: float): $Quaternionf
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Quaternionf$Type): $Quaternionf
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Quaternionf
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Quaternionf$Type): $Quaternionf
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Quaternionf
public "positiveX"(arg0: $Vector3f$Type): $Vector3f
public "rotateTo"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Quaternionf$Type): $Quaternionf
public "rotateTo"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Quaternionf$Type): $Quaternionf
public "rotateTo"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Quaternionf
public "rotateTo"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Quaternionf
public "rotateLocalZ"(arg0: float): $Quaternionf
public "rotateLocalZ"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "rotateLocalY"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "rotateLocalY"(arg0: float): $Quaternionf
public "conjugateBy"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "conjugateBy"(arg0: $Quaternionfc$Type): $Quaternionf
public "positiveY"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalX"(arg0: float): $Quaternionf
public "rotateLocalX"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "positiveZ"(arg0: $Vector3f$Type): $Vector3f
public "angle"(): float
public "rotationTo"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Quaternionf
public "rotationTo"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Quaternionf
public "rotateZ"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "rotateZ"(arg0: float): $Quaternionf
public "rotateX"(arg0: float): $Quaternionf
public "rotateX"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "rotateAxis"(arg0: float, arg1: $Vector3fc$Type): $Quaternionf
public "rotateAxis"(arg0: float, arg1: $Vector3fc$Type, arg2: $Quaternionf$Type): $Quaternionf
public "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
public "lengthSquared"(): float
public "rotateY"(arg0: float): $Quaternionf
public "rotateY"(arg0: float, arg1: $Quaternionf$Type): $Quaternionf
public "getAsMatrix3f"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getAsMatrix3f"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getAsMatrix4x3f"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getAsMatrix4x3f"(arg0: $ByteBuffer$Type): $ByteBuffer
public "rotationZ"(arg0: float): $Quaternionf
public "setFromNormalized"(arg0: $Matrix3dc$Type): $Quaternionf
public "setFromNormalized"(arg0: $Matrix4x3fc$Type): $Quaternionf
public "setFromNormalized"(arg0: $Matrix4dc$Type): $Quaternionf
public "setFromNormalized"(arg0: $Matrix3fc$Type): $Quaternionf
public "setFromNormalized"(arg0: $Matrix4x3dc$Type): $Quaternionf
public "setFromNormalized"(arg0: $Matrix4fc$Type): $Quaternionf
public "getAsMatrix4f"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getAsMatrix4f"(arg0: $FloatBuffer$Type): $FloatBuffer
public "setAngleAxis"(arg0: double, arg1: double, arg2: double, arg3: double): $Quaternionf
public "setAngleAxis"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "rotationAxis"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "rotationAxis"(arg0: float, arg1: $Vector3fc$Type): $Quaternionf
public "rotationAxis"(arg0: $AxisAngle4f$Type): $Quaternionf
public "fromAxisAngleRad"(arg0: $Vector3fc$Type, arg1: float): $Quaternionf
public "fromAxisAngleRad"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "transformPositiveX"(arg0: $Vector4f$Type): $Vector4f
public "transformPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "transformPositiveX"(arg0: $Vector4d$Type): $Vector4d
public "transformPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "premul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Quaternionf$Type): $Quaternionf
public "premul"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "premul"(arg0: $Quaternionfc$Type, arg1: $Quaternionf$Type): $Quaternionf
public "premul"(arg0: $Quaternionfc$Type): $Quaternionf
public "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transformInverse"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformInverse"(arg0: $Vector3d$Type): $Vector3d
public "transformInverse"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
public "transformInverse"(arg0: $Vector4f$Type): $Vector4f
public "transformInverse"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformInverse"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformInverse"(arg0: $Vector3f$Type): $Vector3f
public "transformInverse"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformInverse"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformInverse"(arg0: $Vector4d$Type): $Vector4d
public "transformInverse"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformInverse"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
public "fromAxisAngleDeg"(arg0: $Vector3fc$Type, arg1: float): $Quaternionf
public "fromAxisAngleDeg"(arg0: float, arg1: float, arg2: float, arg3: float): $Quaternionf
public "setFromUnnormalized"(arg0: $Matrix3dc$Type): $Quaternionf
public "setFromUnnormalized"(arg0: $Matrix4x3fc$Type): $Quaternionf
public "setFromUnnormalized"(arg0: $Matrix4x3dc$Type): $Quaternionf
public "setFromUnnormalized"(arg0: $Matrix4fc$Type): $Quaternionf
public "setFromUnnormalized"(arg0: $Matrix3fc$Type): $Quaternionf
public "setFromUnnormalized"(arg0: $Matrix4dc$Type): $Quaternionf
public "transformUnitPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "transformUnitPositiveX"(arg0: $Vector4f$Type): $Vector4f
public "transformUnitPositiveX"(arg0: $Vector4d$Type): $Vector4d
public "transformUnitPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "transformInverseUnit"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
public "transformInverseUnit"(arg0: double, arg1: double, arg2: double, arg3: $Vector4d$Type): $Vector4d
public "transformInverseUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformInverseUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3d$Type): $Vector3d
public "transformInverseUnit"(arg0: $Vector4d$Type): $Vector4d
public "transformInverseUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformInverseUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
public "transformInverseUnit"(arg0: $Vector4f$Type): $Vector4f
public "transformInverseUnit"(arg0: float, arg1: float, arg2: float, arg3: $Vector4f$Type): $Vector4f
public "transformInverseUnit"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transformInverseUnit"(arg0: $Vector3f$Type): $Vector3f
public "transformUnitPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "transformUnitPositiveY"(arg0: $Vector4d$Type): $Vector4d
public "transformUnitPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "transformUnitPositiveY"(arg0: $Vector4f$Type): $Vector4f
public "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "transformUnitPositiveZ"(arg0: $Vector4d$Type): $Vector4d
public "transformUnitPositiveZ"(arg0: $Vector4f$Type): $Vector4f
public "transformUnitPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "transformUnitPositiveZ"(arg0: $Vector3d$Type): $Vector3d
get "finite"(): boolean
set "fromNormalized"(value: $Matrix3dc$Type)
set "fromNormalized"(value: $Matrix4x3fc$Type)
set "fromNormalized"(value: $Matrix4dc$Type)
set "fromNormalized"(value: $Matrix3fc$Type)
set "fromNormalized"(value: $Matrix4x3dc$Type)
set "fromNormalized"(value: $Matrix4fc$Type)
set "fromUnnormalized"(value: $Matrix3dc$Type)
set "fromUnnormalized"(value: $Matrix4x3fc$Type)
set "fromUnnormalized"(value: $Matrix4x3dc$Type)
set "fromUnnormalized"(value: $Matrix4fc$Type)
set "fromUnnormalized"(value: $Matrix3fc$Type)
set "fromUnnormalized"(value: $Matrix4dc$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quaternionf$Type = ($Quaternionf);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quaternionf_ = $Quaternionf$Type;
}}
declare module "packages/org/joml/$Vector2ic" {
import {$Vector2i, $Vector2i$Type} from "packages/org/joml/$Vector2i"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Vector2ic {

 "add"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
 "add"(arg0: integer, arg1: integer, arg2: $Vector2i$Type): $Vector2i
 "get"(arg0: $IntBuffer$Type): $IntBuffer
 "get"(arg0: integer, arg1: $IntBuffer$Type): $IntBuffer
 "get"(arg0: integer): integer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "equals"(arg0: integer, arg1: integer): boolean
 "length"(): double
 "min"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
 "max"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
 "x"(): integer
 "y"(): integer
 "distance"(arg0: $Vector2ic$Type): double
 "distance"(arg0: integer, arg1: integer): double
 "absolute"(arg0: $Vector2i$Type): $Vector2i
 "sub"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
 "sub"(arg0: integer, arg1: integer, arg2: $Vector2i$Type): $Vector2i
 "negate"(arg0: $Vector2i$Type): $Vector2i
 "div"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
 "div"(arg0: float, arg1: $Vector2i$Type): $Vector2i
 "mul"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
 "mul"(arg0: $Vector2ic$Type, arg1: $Vector2i$Type): $Vector2i
 "mul"(arg0: integer, arg1: integer, arg2: $Vector2i$Type): $Vector2i
 "gridDistance"(arg0: integer, arg1: integer): long
 "gridDistance"(arg0: $Vector2ic$Type): long
 "getToAddress"(arg0: long): $Vector2ic
 "lengthSquared"(): long
 "distanceSquared"(arg0: integer, arg1: integer): long
 "distanceSquared"(arg0: $Vector2ic$Type): long
 "minComponent"(): integer
 "maxComponent"(): integer
}

export namespace $Vector2ic {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2ic$Type = ($Vector2ic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2ic_ = $Vector2ic$Type;
}}
declare module "packages/org/joml/$Vector2fc" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Vector2i, $Vector2i$Type} from "packages/org/joml/$Vector2i"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Vector2fc {

 "add"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "add"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "get"(arg0: $Vector2f$Type): $Vector2f
 "get"(arg0: $Vector2d$Type): $Vector2d
 "get"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
 "get"(arg0: integer): float
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "equals"(arg0: float, arg1: float): boolean
 "equals"(arg0: $Vector2fc$Type, arg1: float): boolean
 "length"(): float
 "min"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "max"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "floor"(arg0: $Vector2f$Type): $Vector2f
 "ceil"(arg0: $Vector2f$Type): $Vector2f
 "fma"(arg0: $Vector2fc$Type, arg1: $Vector2fc$Type, arg2: $Vector2f$Type): $Vector2f
 "fma"(arg0: float, arg1: $Vector2fc$Type, arg2: $Vector2f$Type): $Vector2f
 "x"(): float
 "dot"(arg0: $Vector2fc$Type): float
 "normalize"(arg0: $Vector2f$Type): $Vector2f
 "normalize"(arg0: float, arg1: $Vector2f$Type): $Vector2f
 "y"(): float
 "isFinite"(): boolean
 "distance"(arg0: $Vector2fc$Type): float
 "distance"(arg0: float, arg1: float): float
 "round"(arg0: $Vector2f$Type): $Vector2f
 "absolute"(arg0: $Vector2f$Type): $Vector2f
 "sub"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "sub"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "negate"(arg0: $Vector2f$Type): $Vector2f
 "div"(arg0: float, arg1: $Vector2f$Type): $Vector2f
 "div"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "div"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "mul"(arg0: $Vector2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "mul"(arg0: float, arg1: $Vector2f$Type): $Vector2f
 "mul"(arg0: $Matrix2dc$Type, arg1: $Vector2f$Type): $Vector2f
 "mul"(arg0: $Matrix2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "mul"(arg0: float, arg1: float, arg2: $Vector2f$Type): $Vector2f
 "angle"(arg0: $Vector2fc$Type): float
 "getToAddress"(arg0: long): $Vector2fc
 "mulPosition"(arg0: $Matrix3x2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "mulDirection"(arg0: $Matrix3x2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "mulTranspose"(arg0: $Matrix2fc$Type, arg1: $Vector2f$Type): $Vector2f
 "lengthSquared"(): float
 "distanceSquared"(arg0: $Vector2fc$Type): float
 "distanceSquared"(arg0: float, arg1: float): float
 "minComponent"(): integer
 "maxComponent"(): integer
 "lerp"(arg0: $Vector2fc$Type, arg1: float, arg2: $Vector2f$Type): $Vector2f
}

export namespace $Vector2fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2fc$Type = ($Vector2fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2fc_ = $Vector2fc$Type;
}}
declare module "packages/org/joml/$Vector2dc" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$Vector2i, $Vector2i$Type} from "packages/org/joml/$Vector2i"
import {$Vector2d, $Vector2d$Type} from "packages/org/joml/$Vector2d"
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"
import {$Vector2fc, $Vector2fc$Type} from "packages/org/joml/$Vector2fc"
import {$Matrix2fc, $Matrix2fc$Type} from "packages/org/joml/$Matrix2fc"
import {$Matrix2dc, $Matrix2dc$Type} from "packages/org/joml/$Matrix2dc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Vector2dc {

 "add"(arg0: $Vector2fc$Type, arg1: $Vector2d$Type): $Vector2d
 "add"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "add"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "get"(arg0: integer): double
 "get"(arg0: $Vector2d$Type): $Vector2d
 "get"(arg0: $Vector2f$Type): $Vector2f
 "get"(arg0: integer, arg1: $Vector2i$Type): $Vector2i
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "equals"(arg0: double, arg1: double): boolean
 "equals"(arg0: $Vector2dc$Type, arg1: double): boolean
 "length"(): double
 "min"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "max"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "floor"(arg0: $Vector2d$Type): $Vector2d
 "ceil"(arg0: $Vector2d$Type): $Vector2d
 "fma"(arg0: $Vector2dc$Type, arg1: $Vector2dc$Type, arg2: $Vector2d$Type): $Vector2d
 "fma"(arg0: double, arg1: $Vector2dc$Type, arg2: $Vector2d$Type): $Vector2d
 "x"(): double
 "dot"(arg0: $Vector2dc$Type): double
 "normalize"(arg0: double, arg1: $Vector2d$Type): $Vector2d
 "normalize"(arg0: $Vector2d$Type): $Vector2d
 "y"(): double
 "isFinite"(): boolean
 "distance"(arg0: $Vector2dc$Type): double
 "distance"(arg0: $Vector2fc$Type): double
 "distance"(arg0: double, arg1: double): double
 "round"(arg0: $Vector2d$Type): $Vector2d
 "absolute"(arg0: $Vector2d$Type): $Vector2d
 "sub"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "sub"(arg0: $Vector2fc$Type, arg1: $Vector2d$Type): $Vector2d
 "sub"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "negate"(arg0: $Vector2d$Type): $Vector2d
 "div"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "div"(arg0: $Vector2fc$Type, arg1: $Vector2d$Type): $Vector2d
 "div"(arg0: double, arg1: $Vector2d$Type): $Vector2d
 "div"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "mul"(arg0: double, arg1: double, arg2: $Vector2d$Type): $Vector2d
 "mul"(arg0: double, arg1: $Vector2d$Type): $Vector2d
 "mul"(arg0: $Vector2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "mul"(arg0: $Matrix2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "mul"(arg0: $Matrix2fc$Type, arg1: $Vector2d$Type): $Vector2d
 "angle"(arg0: $Vector2dc$Type): double
 "getToAddress"(arg0: long): $Vector2dc
 "mulPosition"(arg0: $Matrix3x2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "mulDirection"(arg0: $Matrix3x2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "mulTranspose"(arg0: $Matrix2fc$Type, arg1: $Vector2d$Type): $Vector2d
 "mulTranspose"(arg0: $Matrix2dc$Type, arg1: $Vector2d$Type): $Vector2d
 "lengthSquared"(): double
 "distanceSquared"(arg0: $Vector2dc$Type): double
 "distanceSquared"(arg0: $Vector2fc$Type): double
 "distanceSquared"(arg0: double, arg1: double): double
 "minComponent"(): integer
 "maxComponent"(): integer
 "lerp"(arg0: $Vector2dc$Type, arg1: double, arg2: $Vector2d$Type): $Vector2d
}

export namespace $Vector2dc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2dc$Type = ($Vector2dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2dc_ = $Vector2dc$Type;
}}
declare module "packages/org/joml/$Vector3ic" {
import {$Vector3i, $Vector3i$Type} from "packages/org/joml/$Vector3i"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Vector3ic {

 "add"(arg0: integer, arg1: integer, arg2: integer, arg3: $Vector3i$Type): $Vector3i
 "add"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $IntBuffer$Type): $IntBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $IntBuffer$Type): $IntBuffer
 "get"(arg0: integer): integer
 "equals"(arg0: integer, arg1: integer, arg2: integer): boolean
 "length"(): double
 "min"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
 "max"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
 "x"(): integer
 "z"(): integer
 "y"(): integer
 "distance"(arg0: integer, arg1: integer, arg2: integer): double
 "distance"(arg0: $Vector3ic$Type): double
 "absolute"(arg0: $Vector3i$Type): $Vector3i
 "sub"(arg0: integer, arg1: integer, arg2: integer, arg3: $Vector3i$Type): $Vector3i
 "sub"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
 "negate"(arg0: $Vector3i$Type): $Vector3i
 "div"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
 "div"(arg0: float, arg1: $Vector3i$Type): $Vector3i
 "mul"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
 "mul"(arg0: integer, arg1: integer, arg2: integer, arg3: $Vector3i$Type): $Vector3i
 "mul"(arg0: $Vector3ic$Type, arg1: $Vector3i$Type): $Vector3i
 "gridDistance"(arg0: integer, arg1: integer, arg2: integer): long
 "gridDistance"(arg0: $Vector3ic$Type): long
 "getToAddress"(arg0: long): $Vector3ic
 "lengthSquared"(): long
 "distanceSquared"(arg0: integer, arg1: integer, arg2: integer): long
 "distanceSquared"(arg0: $Vector3ic$Type): long
 "minComponent"(): integer
 "maxComponent"(): integer
}

export namespace $Vector3ic {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3ic$Type = ($Vector3ic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3ic_ = $Vector3ic$Type;
}}
declare module "packages/org/joml/$Vector3fc" {
import {$Vector3i, $Vector3i$Type} from "packages/org/joml/$Vector3i"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"

export interface $Vector3fc {

 "add"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "add"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Vector3f$Type): $Vector3f
 "get"(arg0: $Vector3d$Type): $Vector3d
 "get"(arg0: integer): float
 "get"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "equals"(arg0: float, arg1: float, arg2: float): boolean
 "equals"(arg0: $Vector3fc$Type, arg1: float): boolean
 "length"(): float
 "min"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "max"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "floor"(arg0: $Vector3f$Type): $Vector3f
 "ceil"(arg0: $Vector3f$Type): $Vector3f
 "fma"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
 "fma"(arg0: float, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
 "x"(): float
 "dot"(arg0: $Vector3fc$Type): float
 "dot"(arg0: float, arg1: float, arg2: float): float
 "z"(): float
 "normalize"(arg0: $Vector3f$Type): $Vector3f
 "normalize"(arg0: float, arg1: $Vector3f$Type): $Vector3f
 "y"(): float
 "isFinite"(): boolean
 "distance"(arg0: float, arg1: float, arg2: float): float
 "distance"(arg0: $Vector3fc$Type): float
 "round"(arg0: $Vector3f$Type): $Vector3f
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Vector3f$Type): $Vector3f
 "absolute"(arg0: $Vector3f$Type): $Vector3f
 "sub"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "sub"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "negate"(arg0: $Vector3f$Type): $Vector3f
 "mulAdd"(arg0: float, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
 "mulAdd"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type): $Vector3f
 "half"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "half"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "div"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "div"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "div"(arg0: float, arg1: $Vector3f$Type): $Vector3f
 "mul"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mul"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "mul"(arg0: $Matrix3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mul"(arg0: float, arg1: $Vector3f$Type): $Vector3f
 "mul"(arg0: $Matrix3x2fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mul"(arg0: $Matrix3dc$Type, arg1: $Vector3f$Type): $Vector3f
 "angle"(arg0: $Vector3fc$Type): float
 "getToAddress"(arg0: long): $Vector3fc
 "mulPosition"(arg0: $Matrix4x3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulPosition"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulProject"(arg0: $Matrix4fc$Type, arg1: float, arg2: $Vector3f$Type): $Vector3f
 "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulDirection"(arg0: $Matrix4x3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulDirection"(arg0: $Matrix4dc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulPositionW"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): float
 "mulTranspose"(arg0: $Matrix3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "rotationTo"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionf$Type): $Quaternionf
 "rotationTo"(arg0: $Vector3fc$Type, arg1: $Quaternionf$Type): $Quaternionf
 "rotateZ"(arg0: float, arg1: $Vector3f$Type): $Vector3f
 "angleCos"(arg0: $Vector3fc$Type): float
 "rotateX"(arg0: float, arg1: $Vector3f$Type): $Vector3f
 "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector3f$Type): $Vector3f
 "lengthSquared"(): float
 "cross"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "cross"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "distanceSquared"(arg0: float, arg1: float, arg2: float): float
 "distanceSquared"(arg0: $Vector3fc$Type): float
 "rotateY"(arg0: float, arg1: $Vector3f$Type): $Vector3f
 "angleSigned"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): float
 "angleSigned"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): float
 "smoothStep"(arg0: $Vector3fc$Type, arg1: float, arg2: $Vector3f$Type): $Vector3f
 "orthogonalize"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "reflect"(arg0: float, arg1: float, arg2: float, arg3: $Vector3f$Type): $Vector3f
 "minComponent"(): integer
 "maxComponent"(): integer
 "orthogonalizeUnit"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
 "hermite"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: float, arg4: $Vector3f$Type): $Vector3f
 "lerp"(arg0: $Vector3fc$Type, arg1: float, arg2: $Vector3f$Type): $Vector3f
 "mulTransposeDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulTransposePosition"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
}

export namespace $Vector3fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3fc$Type = ($Vector3fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3fc_ = $Vector3fc$Type;
}}
declare module "packages/org/joml/$Vector3dc" {
import {$Vector3i, $Vector3i$Type} from "packages/org/joml/$Vector3i"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix3x2dc, $Matrix3x2dc$Type} from "packages/org/joml/$Matrix3x2dc"
import {$Matrix3x2fc, $Matrix3x2fc$Type} from "packages/org/joml/$Matrix3x2fc"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"

export interface $Vector3dc {

 "add"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "add"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "add"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $Vector3d$Type): $Vector3d
 "get"(arg0: $Vector3f$Type): $Vector3f
 "get"(arg0: integer, arg1: $Vector3i$Type): $Vector3i
 "get"(arg0: integer): double
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "equals"(arg0: double, arg1: double, arg2: double): boolean
 "equals"(arg0: $Vector3dc$Type, arg1: double): boolean
 "length"(): double
 "min"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "max"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "floor"(arg0: $Vector3d$Type): $Vector3d
 "ceil"(arg0: $Vector3d$Type): $Vector3d
 "fma"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3d$Type): $Vector3d
 "fma"(arg0: double, arg1: $Vector3fc$Type, arg2: $Vector3d$Type): $Vector3d
 "fma"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
 "fma"(arg0: double, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
 "fma"(arg0: $Vector3dc$Type, arg1: $Vector3fc$Type, arg2: $Vector3d$Type): $Vector3d
 "x"(): double
 "dot"(arg0: $Vector3dc$Type): double
 "dot"(arg0: double, arg1: double, arg2: double): double
 "z"(): double
 "normalize"(arg0: double, arg1: $Vector3d$Type): $Vector3d
 "normalize"(arg0: $Vector3d$Type): $Vector3d
 "y"(): double
 "isFinite"(): boolean
 "distance"(arg0: $Vector3dc$Type): double
 "distance"(arg0: double, arg1: double, arg2: double): double
 "round"(arg0: $Vector3d$Type): $Vector3d
 "rotate"(arg0: $Quaterniondc$Type, arg1: $Vector3d$Type): $Vector3d
 "absolute"(arg0: $Vector3d$Type): $Vector3d
 "sub"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "sub"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "sub"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "negate"(arg0: $Vector3d$Type): $Vector3d
 "mulAdd"(arg0: $Vector3fc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
 "mulAdd"(arg0: double, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
 "mulAdd"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type): $Vector3d
 "half"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "half"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "div"(arg0: double, arg1: $Vector3d$Type): $Vector3d
 "div"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "div"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "div"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Vector3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: double, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Matrix3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Matrix3dc$Type, arg1: $Vector3f$Type): $Vector3f
 "mul"(arg0: $Matrix3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Matrix3x2fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mul"(arg0: $Matrix3x2dc$Type, arg1: $Vector3d$Type): $Vector3d
 "getf"(arg0: $ByteBuffer$Type): $ByteBuffer
 "getf"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "angle"(arg0: $Vector3dc$Type): double
 "getToAddress"(arg0: long): $Vector3dc
 "mulPosition"(arg0: $Matrix4x3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulPosition"(arg0: $Matrix4x3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulPosition"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulPosition"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulProject"(arg0: $Matrix4dc$Type, arg1: double, arg2: $Vector3d$Type): $Vector3d
 "mulProject"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulDirection"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulDirection"(arg0: $Matrix4x3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulDirection"(arg0: $Matrix4x3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulPositionW"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): double
 "mulPositionW"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): double
 "mulTranspose"(arg0: $Matrix3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulTranspose"(arg0: $Matrix3fc$Type, arg1: $Vector3d$Type): $Vector3d
 "rotationTo"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniond$Type): $Quaterniond
 "rotationTo"(arg0: $Vector3dc$Type, arg1: $Quaterniond$Type): $Quaterniond
 "rotateZ"(arg0: double, arg1: $Vector3d$Type): $Vector3d
 "angleCos"(arg0: $Vector3dc$Type): double
 "rotateX"(arg0: double, arg1: $Vector3d$Type): $Vector3d
 "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector3d$Type): $Vector3d
 "lengthSquared"(): double
 "cross"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "cross"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "distanceSquared"(arg0: $Vector3dc$Type): double
 "distanceSquared"(arg0: double, arg1: double, arg2: double): double
 "rotateY"(arg0: double, arg1: $Vector3d$Type): $Vector3d
 "angleSigned"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): double
 "angleSigned"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
 "smoothStep"(arg0: $Vector3dc$Type, arg1: double, arg2: $Vector3d$Type): $Vector3d
 "orthogonalize"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "reflect"(arg0: double, arg1: double, arg2: double, arg3: $Vector3d$Type): $Vector3d
 "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "minComponent"(): integer
 "maxComponent"(): integer
 "orthogonalizeUnit"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
 "hermite"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: double, arg4: $Vector3d$Type): $Vector3d
 "lerp"(arg0: $Vector3dc$Type, arg1: double, arg2: $Vector3d$Type): $Vector3d
 "mulTransposeDirection"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulTransposeDirection"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulTransposePosition"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulTransposePosition"(arg0: $Matrix4fc$Type, arg1: $Vector3d$Type): $Vector3d
}

export namespace $Vector3dc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3dc$Type = ($Vector3dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3dc_ = $Vector3dc$Type;
}}
declare module "packages/org/joml/$Vector4dc" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector4i, $Vector4i$Type} from "packages/org/joml/$Vector4i"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"

export interface $Vector4dc {

 "add"(arg0: $Vector4fc$Type, arg1: $Vector4d$Type): $Vector4d
 "add"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "add"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "get"(arg0: $Vector4d$Type): $Vector4d
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer): double
 "get"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
 "get"(arg0: $Vector4f$Type): $Vector4f
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
 "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
 "equals"(arg0: $Vector4dc$Type, arg1: double): boolean
 "equals"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
 "length"(): double
 "min"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "max"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "floor"(arg0: $Vector4d$Type): $Vector4d
 "ceil"(arg0: $Vector4d$Type): $Vector4d
 "fma"(arg0: double, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
 "fma"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
 "x"(): double
 "dot"(arg0: $Vector4dc$Type): double
 "dot"(arg0: double, arg1: double, arg2: double, arg3: double): double
 "z"(): double
 "normalize"(arg0: double, arg1: $Vector4d$Type): $Vector4d
 "normalize"(arg0: $Vector4d$Type): $Vector4d
 "w"(): double
 "y"(): double
 "isFinite"(): boolean
 "distance"(arg0: $Vector4dc$Type): double
 "distance"(arg0: double, arg1: double, arg2: double, arg3: double): double
 "round"(arg0: $Vector4d$Type): $Vector4d
 "rotate"(arg0: $Quaterniondc$Type, arg1: $Vector4d$Type): $Vector4d
 "absolute"(arg0: $Vector4d$Type): $Vector4d
 "sub"(arg0: $Vector4fc$Type, arg1: $Vector4d$Type): $Vector4d
 "sub"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "sub"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "negate"(arg0: $Vector4d$Type): $Vector4d
 "mulAdd"(arg0: double, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
 "mulAdd"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type, arg2: $Vector4d$Type): $Vector4d
 "div"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "div"(arg0: double, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: double, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: $Matrix4x3dc$Type, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: $Matrix4x3fc$Type, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: $Matrix4fc$Type, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "mul"(arg0: $Vector4fc$Type, arg1: $Vector4d$Type): $Vector4d
 "mulAffine"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "mulAffineTranspose"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "normalize3"(arg0: $Vector4d$Type): $Vector4d
 "getf"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "getf"(arg0: $ByteBuffer$Type): $ByteBuffer
 "angle"(arg0: $Vector4dc$Type): double
 "getToAddress"(arg0: long): $Vector4dc
 "mulProject"(arg0: $Matrix4dc$Type, arg1: $Vector3d$Type): $Vector3d
 "mulProject"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "mulTranspose"(arg0: $Matrix4dc$Type, arg1: $Vector4d$Type): $Vector4d
 "rotateZ"(arg0: double, arg1: $Vector4d$Type): $Vector4d
 "angleCos"(arg0: $Vector4dc$Type): double
 "rotateX"(arg0: double, arg1: $Vector4d$Type): $Vector4d
 "rotateAxis"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "lengthSquared"(): double
 "distanceSquared"(arg0: $Vector4dc$Type): double
 "distanceSquared"(arg0: double, arg1: double, arg2: double, arg3: double): double
 "rotateY"(arg0: double, arg1: $Vector4d$Type): $Vector4d
 "smoothStep"(arg0: $Vector4dc$Type, arg1: double, arg2: $Vector4d$Type): $Vector4d
 "minComponent"(): integer
 "maxComponent"(): integer
 "hermite"(arg0: $Vector4dc$Type, arg1: $Vector4dc$Type, arg2: $Vector4dc$Type, arg3: double, arg4: $Vector4d$Type): $Vector4d
 "lerp"(arg0: $Vector4dc$Type, arg1: double, arg2: $Vector4d$Type): $Vector4d
}

export namespace $Vector4dc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4dc$Type = ($Vector4dc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4dc_ = $Vector4dc$Type;
}}
declare module "packages/org/joml/$Vector4ic" {
import {$Vector4i, $Vector4i$Type} from "packages/org/joml/$Vector4i"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Vector4ic {

 "add"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
 "add"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Vector4i$Type): $Vector4i
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $IntBuffer$Type): $IntBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $IntBuffer$Type): $IntBuffer
 "get"(arg0: integer): integer
 "equals"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): boolean
 "length"(): double
 "min"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
 "max"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
 "x"(): integer
 "dot"(arg0: $Vector4ic$Type): integer
 "z"(): integer
 "w"(): integer
 "y"(): integer
 "distance"(arg0: $Vector4ic$Type): double
 "distance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): double
 "absolute"(arg0: $Vector4i$Type): $Vector4i
 "sub"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Vector4i$Type): $Vector4i
 "sub"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
 "negate"(arg0: $Vector4i$Type): $Vector4i
 "div"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
 "div"(arg0: float, arg1: $Vector4i$Type): $Vector4i
 "div"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
 "mul"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
 "mul"(arg0: $Vector4ic$Type, arg1: $Vector4i$Type): $Vector4i
 "gridDistance"(arg0: $Vector4ic$Type): long
 "gridDistance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): long
 "getToAddress"(arg0: long): $Vector4ic
 "lengthSquared"(): long
 "distanceSquared"(arg0: $Vector4ic$Type): integer
 "distanceSquared"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
 "minComponent"(): integer
 "maxComponent"(): integer
}

export namespace $Vector4ic {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4ic$Type = ($Vector4ic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4ic_ = $Vector4ic$Type;
}}
declare module "packages/org/joml/$Vector4fc" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vector4i, $Vector4i$Type} from "packages/org/joml/$Vector4i"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $Vector4fc {

 "add"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "add"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "get"(arg0: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
 "get"(arg0: $Vector4f$Type): $Vector4f
 "get"(arg0: $Vector4d$Type): $Vector4d
 "get"(arg0: integer, arg1: $Vector4i$Type): $Vector4i
 "get"(arg0: integer): float
 "get"(arg0: $FloatBuffer$Type): $FloatBuffer
 "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
 "equals"(arg0: float, arg1: float, arg2: float, arg3: float): boolean
 "equals"(arg0: $Vector4fc$Type, arg1: float): boolean
 "length"(): float
 "min"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "max"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "floor"(arg0: $Vector4f$Type): $Vector4f
 "ceil"(arg0: $Vector4f$Type): $Vector4f
 "fma"(arg0: float, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
 "fma"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
 "x"(): float
 "dot"(arg0: $Vector4fc$Type): float
 "dot"(arg0: float, arg1: float, arg2: float, arg3: float): float
 "z"(): float
 "normalize"(arg0: float, arg1: $Vector4f$Type): $Vector4f
 "normalize"(arg0: $Vector4f$Type): $Vector4f
 "w"(): float
 "y"(): float
 "isFinite"(): boolean
 "distance"(arg0: $Vector4fc$Type): float
 "distance"(arg0: float, arg1: float, arg2: float, arg3: float): float
 "round"(arg0: $Vector4f$Type): $Vector4f
 "rotate"(arg0: $Quaternionfc$Type, arg1: $Vector4f$Type): $Vector4f
 "absolute"(arg0: $Vector4f$Type): $Vector4f
 "sub"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "sub"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "negate"(arg0: $Vector4f$Type): $Vector4f
 "mulAdd"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
 "mulAdd"(arg0: float, arg1: $Vector4fc$Type, arg2: $Vector4f$Type): $Vector4f
 "div"(arg0: float, arg1: $Vector4f$Type): $Vector4f
 "div"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "div"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "mul"(arg0: float, arg1: $Vector4f$Type): $Vector4f
 "mul"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "mul"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "mul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "mul"(arg0: $Matrix4x3fc$Type, arg1: $Vector4f$Type): $Vector4f
 "mulAffine"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "mulAffineTranspose"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "normalize3"(arg0: $Vector4f$Type): $Vector4f
 "angle"(arg0: $Vector4fc$Type): float
 "getToAddress"(arg0: long): $Vector4fc
 "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector3f$Type): $Vector3f
 "mulProject"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "mulTranspose"(arg0: $Matrix4fc$Type, arg1: $Vector4f$Type): $Vector4f
 "rotateZ"(arg0: float, arg1: $Vector4f$Type): $Vector4f
 "angleCos"(arg0: $Vector4fc$Type): float
 "rotateX"(arg0: float, arg1: $Vector4f$Type): $Vector4f
 "rotateAxis"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "lengthSquared"(): float
 "distanceSquared"(arg0: float, arg1: float, arg2: float, arg3: float): float
 "distanceSquared"(arg0: $Vector4fc$Type): float
 "rotateY"(arg0: float, arg1: $Vector4f$Type): $Vector4f
 "smoothStep"(arg0: $Vector4fc$Type, arg1: float, arg2: $Vector4f$Type): $Vector4f
 "minComponent"(): integer
 "maxComponent"(): integer
 "hermite"(arg0: $Vector4fc$Type, arg1: $Vector4fc$Type, arg2: $Vector4fc$Type, arg3: float, arg4: $Vector4f$Type): $Vector4f
 "lerp"(arg0: $Vector4fc$Type, arg1: float, arg2: $Vector4f$Type): $Vector4f
}

export namespace $Vector4fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4fc$Type = ($Vector4fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4fc_ = $Vector4fc$Type;
}}
declare module "packages/org/joml/$Matrix4x3f" {
import {$Matrix4x3d, $Matrix4x3d$Type} from "packages/org/joml/$Matrix4x3d"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix4fc, $Matrix4fc$Type} from "packages/org/joml/$Matrix4fc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4f, $Vector4f$Type} from "packages/org/joml/$Vector4f"
import {$Vector4fc, $Vector4fc$Type} from "packages/org/joml/$Vector4fc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Matrix4x3f implements $Externalizable, $Cloneable, $Matrix4x3fc {

constructor(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Vector3fc$Type)
constructor(arg0: $FloatBuffer$Type)
constructor(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float)
constructor()
constructor(arg0: $Matrix3fc$Type)
constructor(arg0: $Matrix4x3fc$Type)

public "add"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "add"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "get"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "get"(arg0: $Matrix4f$Type): $Matrix4f
public "get"(arg0: (float)[]): (float)[]
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "equals"(arg0: $Matrix4x3fc$Type, arg1: float): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "toString"(arg0: $NumberFormat$Type): string
public "hashCode"(): integer
public "clone"(): any
public "fma"(arg0: $Matrix4x3fc$Type, arg1: float): $Matrix4x3f
public "fma"(arg0: $Matrix4x3fc$Type, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "scale"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "scale"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "scale"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "scale"(arg0: $Vector3fc$Type): $Matrix4x3f
public "scale"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "scale"(arg0: float): $Matrix4x3f
public "transform"(arg0: $Vector4fc$Type, arg1: $Vector4f$Type): $Vector4f
public "transform"(arg0: $Vector4f$Type): $Vector4f
public "identity"(): $Matrix4x3f
public "set"(arg0: $AxisAngle4f$Type): $Matrix4x3f
public "set"(arg0: $Matrix3fc$Type): $Matrix4x3f
public "set"(arg0: $Matrix4fc$Type): $Matrix4x3f
public "set"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "set"(arg0: $Quaternionfc$Type): $Matrix4x3f
public "set"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Vector3fc$Type): $Matrix4x3f
public "set"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: float, arg11: float): $Matrix4x3f
public "set"(arg0: $Quaterniondc$Type): $Matrix4x3f
public "set"(arg0: $AxisAngle4d$Type): $Matrix4x3f
public "set"(arg0: $ByteBuffer$Type): $Matrix4x3f
public "set"(arg0: (float)[], arg1: integer): $Matrix4x3f
public "set"(arg0: $FloatBuffer$Type): $Matrix4x3f
public "set"(arg0: (float)[]): $Matrix4x3f
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix4x3f
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix4x3f
public "properties"(): integer
public "zero"(): $Matrix4x3f
public "pick"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: (integer)[], arg5: $Matrix4x3f$Type): $Matrix4x3f
public "pick"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: (integer)[]): $Matrix4x3f
public "isFinite"(): boolean
public "swap"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "rotate"(arg0: float, arg1: $Vector3fc$Type): $Matrix4x3f
public "rotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "rotate"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "rotate"(arg0: $Quaternionfc$Type): $Matrix4x3f
public "rotate"(arg0: $AxisAngle4f$Type): $Matrix4x3f
public "rotate"(arg0: float, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "normal"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "normal"(): $Matrix4x3f
public "normal"(arg0: $Matrix3f$Type): $Matrix3f
public "sub"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "sub"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "origin"(arg0: $Vector3f$Type): $Vector3f
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "mul"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "shadow"(arg0: $Vector4fc$Type, arg1: $Matrix4x3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "shadow"(arg0: $Vector4fc$Type, arg1: $Matrix4x3fc$Type): $Matrix4x3f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: $Matrix4x3f$Type): $Matrix4x3f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3fc$Type, arg5: $Matrix4x3f$Type): $Matrix4x3f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "shadow"(arg0: $Vector4fc$Type, arg1: float, arg2: float, arg3: float, arg4: float): $Matrix4x3f
public "shadow"(arg0: $Vector4fc$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: $Matrix4x3f$Type): $Matrix4x3f
public "shadow"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float): $Matrix4x3f
public "m12"(arg0: float): $Matrix4x3f
public "m12"(): float
public "m10"(): float
public "m10"(arg0: float): $Matrix4x3f
public "m11"(arg0: float): $Matrix4x3f
public "m11"(): float
public "translation"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "translation"(arg0: $Vector3fc$Type): $Matrix4x3f
public "scaling"(arg0: $Vector3fc$Type): $Matrix4x3f
public "scaling"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "scaling"(arg0: float): $Matrix4x3f
public "invert"(): $Matrix4x3f
public "invert"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "invert"(arg0: $Matrix4f$Type): $Matrix4f
public "translate"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "translate"(arg0: $Vector3fc$Type): $Matrix4x3f
public "translate"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "rotationY"(arg0: float): $Matrix4x3f
public "rotationX"(arg0: float): $Matrix4x3f
public "getRotation"(arg0: $AxisAngle4f$Type): $AxisAngle4f
public "getRotation"(arg0: $AxisAngle4d$Type): $AxisAngle4d
public "rotation"(arg0: float, arg1: $Vector3fc$Type): $Matrix4x3f
public "rotation"(arg0: $Quaternionfc$Type): $Matrix4x3f
public "rotation"(arg0: $AxisAngle4f$Type): $Matrix4x3f
public "rotation"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "rotateZYX"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "rotateZYX"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "rotateZYX"(arg0: $Vector3f$Type): $Matrix4x3f
public "rotateYXZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "rotateYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "rotateYXZ"(arg0: $Vector3f$Type): $Matrix4x3f
public "getEulerAnglesXYZ"(arg0: $Vector3f$Type): $Vector3f
public "rotateXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "rotateXYZ"(arg0: $Vector3f$Type): $Matrix4x3f
public "rotateXYZ"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "getEulerAnglesZYX"(arg0: $Vector3f$Type): $Vector3f
public "rotationXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "rotationYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "rotationZYX"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "lookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "lookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "positiveX"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalZ"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotateLocalZ"(arg0: float): $Matrix4x3f
public "rotateLocalY"(arg0: float): $Matrix4x3f
public "rotateLocalY"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "positiveY"(arg0: $Vector3f$Type): $Vector3f
public "rotateLocalX"(arg0: float): $Matrix4x3f
public "rotateLocalX"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "positiveZ"(arg0: $Vector3f$Type): $Vector3f
public "transformAab"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3f$Type, arg3: $Vector3f$Type): $Matrix4x3f
public "transformAab"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Vector3f$Type, arg7: $Vector3f$Type): $Matrix4x3f
public "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "rotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "rotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "rotationTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "rotationTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "mapZYnX"(): $Matrix4x3f
public "mapZYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZnXnY"(): $Matrix4x3f
public "mapZnXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXZY"(): $Matrix4x3f
public "mapZXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZXY"(): $Matrix4x3f
public "mapZnXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZnXY"(): $Matrix4x3f
public "mapYZX"(): $Matrix4x3f
public "mapYZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapYXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapYXZ"(): $Matrix4x3f
public "mapYXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapYXnZ"(): $Matrix4x3f
public "mapYnXnZ"(): $Matrix4x3f
public "mapYnXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapYnZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapYnZX"(): $Matrix4x3f
public "mapYnZnX"(): $Matrix4x3f
public "mapYnZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "withLookAtUp"(arg0: $Vector3fc$Type): $Matrix4x3f
public "withLookAtUp"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "withLookAtUp"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "withLookAtUp"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "obliqueZ"(arg0: float, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "obliqueZ"(arg0: float, arg1: float): $Matrix4x3f
public "mapXZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXZnY"(): $Matrix4x3f
public "mapXnZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXnZY"(): $Matrix4x3f
public "mapYZnX"(): $Matrix4x3f
public "mapYZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZXnY"(): $Matrix4x3f
public "mapZXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXnZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXnZnY"(): $Matrix4x3f
public "mapXnYnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapXnYnZ"(): $Matrix4x3f
public "mapZYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZYX"(): $Matrix4x3f
public "mapYnXZ"(): $Matrix4x3f
public "mapYnXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXnYnZ"(): $Matrix4x3f
public "mapnXnYnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXnYZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXnYZ"(): $Matrix4x3f
public "mapnXnZY"(): $Matrix4x3f
public "mapnXnZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYXnZ"(): $Matrix4x3f
public "mapnYXZ"(): $Matrix4x3f
public "mapnYXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYnXZ"(): $Matrix4x3f
public "mapnYnXZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYnZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYnZnX"(): $Matrix4x3f
public "mapnZYX"(): $Matrix4x3f
public "mapnZYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXZnY"(): $Matrix4x3f
public "mapnZnYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnZnYX"(): $Matrix4x3f
public "mapnZnXY"(): $Matrix4x3f
public "mapnZnXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXYnZ"(): $Matrix4x3f
public "mapnXYnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXZY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnXZY"(): $Matrix4x3f
public "mapZnYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZnYnX"(): $Matrix4x3f
public "mapnZXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnZXnY"(): $Matrix4x3f
public "mapnYnZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYnZX"(): $Matrix4x3f
public "negateY"(): $Matrix4x3f
public "negateY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYZnX"(): $Matrix4x3f
public "mapnYZnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnZnXnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnZnXnY"(): $Matrix4x3f
public "mapnXnZnY"(): $Matrix4x3f
public "mapnXnZnY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnZXY"(): $Matrix4x3f
public "mapnZXY"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "negateX"(): $Matrix4x3f
public "negateX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnZnYnX"(): $Matrix4x3f
public "mapnZnYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYnXnZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapnYnXnZ"(): $Matrix4x3f
public "mapnZYnX"(): $Matrix4x3f
public "mapnZYnX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "negateZ"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "negateZ"(): $Matrix4x3f
public "mapZnYX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "mapZnYX"(): $Matrix4x3f
public "mapnYZX"(): $Matrix4x3f
public "mapnYZX"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "assume"(arg0: integer): $Matrix4x3f
public "set3x3"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "set3x3"(arg0: $Matrix3fc$Type): $Matrix4x3f
public "mulTranslation"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "mul3x3"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4x3f$Type): $Matrix4x3f
public "mul3x3"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4x3f
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: (float)[], arg1: integer): (float)[]
public "getTransposed"(arg0: (float)[]): (float)[]
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "determinant"(): float
public "mulComponentWise"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "mulComponentWise"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "invertOrtho"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "invertOrtho"(): $Matrix4x3f
public "transpose3x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "transpose3x3"(): $Matrix4x3f
public "transpose3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "setTranslation"(arg0: $Vector3fc$Type): $Matrix4x3f
public "setTranslation"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "getTranslation"(arg0: $Vector3f$Type): $Vector3f
public "getScale"(arg0: $Vector3f$Type): $Vector3f
public "get3x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get3x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get3x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get3x4"(arg0: $FloatBuffer$Type): $FloatBuffer
public "setRotationYXZ"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "setRotationXYZ"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "setRotationZYX"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "scaleAround"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "translationRotate"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type): $Matrix4x3f
public "translationRotate"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): $Matrix4x3f
public "translationRotate"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionfc$Type): $Matrix4x3f
public "transformPosition"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformPosition"(arg0: $Vector3f$Type): $Vector3f
public "transformDirection"(arg0: $Vector3fc$Type, arg1: $Vector3f$Type): $Vector3f
public "transformDirection"(arg0: $Vector3f$Type): $Vector3f
public "scaleXY"(arg0: float, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "scaleXY"(arg0: float, arg1: float): $Matrix4x3f
public "scaleLocal"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "scaleLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotateTranslation"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4x3f$Type): $Matrix4x3f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4x3f
public "ortho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "rotateLocal"(arg0: $Quaternionfc$Type): $Matrix4x3f
public "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "rotateLocal"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "translateLocal"(arg0: float, arg1: float, arg2: float): $Matrix4x3f
public "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "translateLocal"(arg0: $Vector3fc$Type): $Matrix4x3f
public "translateLocal"(arg0: float, arg1: float, arg2: float, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4x3f$Type): $Matrix4x3f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4x3f
public "orthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "setOrthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "setOrthoSymmetric"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4x3f
public "setOrthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4x3f
public "setOrthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean, arg7: $Matrix4x3f$Type): $Matrix4x3f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "orthoLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4x3f
public "ortho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "ortho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "lookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "lookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "lookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4x3f
public "lookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4x3f$Type): $Matrix4x3f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean, arg5: $Matrix4x3f$Type): $Matrix4x3f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4x3f
public "orthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "setOrtho2D"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "setLookAlong"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "setLookAlong"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "setOrtho2DLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "ortho2D"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "ortho2D"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "setLookAt"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4x3f
public "setLookAt"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "setLookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4x3f
public "setLookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "lookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4x3f
public "lookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "lookAtLH"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "lookAtLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: $Matrix4x3f$Type): $Matrix4x3f
public "rotationAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "rotateAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "rotateAround"(arg0: $Quaternionfc$Type, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "reflection"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "reflection"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "reflection"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "reflection"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "getRow"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
public "setColumn"(arg0: integer, arg1: $Vector3fc$Type): $Matrix4x3f
public "cofactor3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "cofactor3x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "cofactor3x3"(): $Matrix4x3f
public "setRow"(arg0: integer, arg1: $Vector4fc$Type): $Matrix4x3f
public "normalize3x3"(): $Matrix4x3f
public "normalize3x3"(arg0: $Matrix3f$Type): $Matrix3f
public "normalize3x3"(arg0: $Matrix4x3f$Type): $Matrix4x3f
public "frustumPlane"(arg0: integer, arg1: $Vector4f$Type): $Vector4f
public "billboardSpherical"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "billboardSpherical"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "arcball"(arg0: float, arg1: $Vector3fc$Type, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "arcball"(arg0: float, arg1: $Vector3fc$Type, arg2: float, arg3: float): $Matrix4x3f
public "arcball"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "arcball"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "mulOrtho"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "mulOrtho"(arg0: $Matrix4x3fc$Type): $Matrix4x3f
public "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get4x4"(arg0: (float)[]): (float)[]
public "get4x4"(arg0: (float)[], arg1: integer): (float)[]
public "setOrtho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: boolean): $Matrix4x3f
public "setOrtho"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "m31"(arg0: float): $Matrix4x3f
public "m31"(): float
public "m20"(arg0: float): $Matrix4x3f
public "m20"(): float
public "m21"(): float
public "m21"(arg0: float): $Matrix4x3f
public "m30"(): float
public "m30"(arg0: float): $Matrix4x3f
public "m22"(): float
public "m22"(arg0: float): $Matrix4x3f
public "m32"(arg0: float): $Matrix4x3f
public "m32"(): float
public "determineProperties"(): $Matrix4x3f
public "translationRotateMul"(arg0: float, arg1: float, arg2: float, arg3: $Quaternionfc$Type, arg4: $Matrix4x3fc$Type): $Matrix4x3f
public "translationRotateMul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: $Matrix4x3fc$Type): $Matrix4x3f
public "translationRotateScaleMul"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type, arg3: $Matrix4x3f$Type): $Matrix4x3f
public "translationRotateScaleMul"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float, arg10: $Matrix4x3f$Type): $Matrix4x3f
public "setFromAddress"(arg0: long): $Matrix4x3f
public "getToAddress"(arg0: long): $Matrix4x3fc
public "m00"(): float
public "m00"(arg0: float): $Matrix4x3f
public "m01"(): float
public "m01"(arg0: float): $Matrix4x3f
public "m02"(): float
public "m02"(arg0: float): $Matrix4x3f
public "rotateZ"(arg0: float): $Matrix4x3f
public "rotateZ"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotateX"(arg0: float): $Matrix4x3f
public "rotateX"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotateY"(arg0: float, arg1: $Matrix4x3f$Type): $Matrix4x3f
public "rotateY"(arg0: float): $Matrix4x3f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Matrix4x3f$Type): $Matrix4x3f
public "reflect"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): $Matrix4x3f
public "reflect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: $Matrix4x3f$Type): $Matrix4x3f
public "reflect"(arg0: $Quaternionfc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "reflect"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type): $Matrix4x3f
public "lerp"(arg0: $Matrix4x3fc$Type, arg1: float): $Matrix4x3f
public "lerp"(arg0: $Matrix4x3fc$Type, arg1: float, arg2: $Matrix4x3f$Type): $Matrix4x3f
public "rotationZ"(arg0: float): $Matrix4x3f
public "getColumn"(arg0: integer, arg1: $Vector3f$Type): $Vector3f
public "billboardCylindrical"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "translationRotateTowards"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float): $Matrix4x3f
public "translationRotateTowards"(arg0: $Vector3fc$Type, arg1: $Vector3fc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "translationRotateInvert"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): $Matrix4x3f
public "translationRotateInvert"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type): $Matrix4x3f
public "setOrthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: boolean): $Matrix4x3f
public "setOrthoSymmetricLH"(arg0: float, arg1: float, arg2: float, arg3: float): $Matrix4x3f
public "translationRotateScale"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): $Matrix4x3f
public "translationRotateScale"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type): $Matrix4x3f
public "normalizedPositiveY"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveZ"(arg0: $Vector3f$Type): $Vector3f
public "normalizedPositiveX"(arg0: $Vector3f$Type): $Vector3f
public "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
get "finite"(): boolean
set "3x3"(value: $Matrix4x3fc$Type)
set "3x3"(value: $Matrix3fc$Type)
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4x3f$Type = ($Matrix4x3f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4x3f_ = $Matrix4x3f$Type;
}}
declare module "packages/org/joml/$Matrix4x3d" {
import {$Matrix4d, $Matrix4d$Type} from "packages/org/joml/$Matrix4d"
import {$Vector3d, $Vector3d$Type} from "packages/org/joml/$Vector3d"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Quaternionfc, $Quaternionfc$Type} from "packages/org/joml/$Quaternionfc"
import {$Matrix3fc, $Matrix3fc$Type} from "packages/org/joml/$Matrix3fc"
import {$Matrix4dc, $Matrix4dc$Type} from "packages/org/joml/$Matrix4dc"
import {$Matrix3dc, $Matrix3dc$Type} from "packages/org/joml/$Matrix3dc"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Matrix4x3fc, $Matrix4x3fc$Type} from "packages/org/joml/$Matrix4x3fc"
import {$Vector4dc, $Vector4dc$Type} from "packages/org/joml/$Vector4dc"
import {$Vector3dc, $Vector3dc$Type} from "packages/org/joml/$Vector3dc"
import {$Quaterniondc, $Quaterniondc$Type} from "packages/org/joml/$Quaterniondc"
import {$Vector3fc, $Vector3fc$Type} from "packages/org/joml/$Vector3fc"
import {$AxisAngle4f, $AxisAngle4f$Type} from "packages/org/joml/$AxisAngle4f"
import {$Matrix3d, $Matrix3d$Type} from "packages/org/joml/$Matrix3d"
import {$Vector4d, $Vector4d$Type} from "packages/org/joml/$Vector4d"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$AxisAngle4d, $AxisAngle4d$Type} from "packages/org/joml/$AxisAngle4d"
import {$Matrix4x3dc, $Matrix4x3dc$Type} from "packages/org/joml/$Matrix4x3dc"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$NumberFormat, $NumberFormat$Type} from "packages/java/text/$NumberFormat"
import {$Quaterniond, $Quaterniond$Type} from "packages/org/joml/$Quaterniond"
import {$Quaternionf, $Quaternionf$Type} from "packages/org/joml/$Quaternionf"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"

export class $Matrix4x3d implements $Externalizable, $Cloneable, $Matrix4x3dc {

constructor(arg0: $Matrix3dc$Type)
constructor(arg0: $Matrix3fc$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double)
constructor(arg0: $DoubleBuffer$Type)
constructor()
constructor(arg0: $Matrix4x3dc$Type)
constructor(arg0: $Matrix4x3fc$Type)

public "add"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "add"(arg0: $Matrix4x3fc$Type): $Matrix4x3d
public "add"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "add"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "get"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "get"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "get"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "get"(arg0: $Matrix4d$Type): $Matrix4d
public "get"(arg0: (float)[]): (float)[]
public "get"(arg0: (float)[], arg1: integer): (float)[]
public "get"(arg0: (double)[]): (double)[]
public "get"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get"(arg0: (double)[], arg1: integer): (double)[]
public "equals"(arg0: any): boolean
public "equals"(arg0: $Matrix4x3dc$Type, arg1: double): boolean
public "toString"(arg0: $NumberFormat$Type): string
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "fma"(arg0: $Matrix4x3fc$Type, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "fma"(arg0: $Matrix4x3dc$Type, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "fma"(arg0: $Matrix4x3fc$Type, arg1: double): $Matrix4x3d
public "fma"(arg0: $Matrix4x3dc$Type, arg1: double): $Matrix4x3d
public "scale"(arg0: $Vector3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "scale"(arg0: $Vector3dc$Type): $Matrix4x3d
public "scale"(arg0: double): $Matrix4x3d
public "scale"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "scale"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "scale"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "transform"(arg0: $Vector4d$Type): $Vector4d
public "transform"(arg0: $Vector4dc$Type, arg1: $Vector4d$Type): $Vector4d
public "identity"(): $Matrix4x3d
public "set"(arg0: $Quaternionfc$Type): $Matrix4x3d
public "set"(arg0: $Quaterniondc$Type): $Matrix4x3d
public "set"(arg0: (double)[]): $Matrix4x3d
public "set"(arg0: (float)[], arg1: integer): $Matrix4x3d
public "set"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: double, arg11: double): $Matrix4x3d
public "set"(arg0: $AxisAngle4f$Type): $Matrix4x3d
public "set"(arg0: (double)[], arg1: integer): $Matrix4x3d
public "set"(arg0: $FloatBuffer$Type): $Matrix4x3d
public "set"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix4x3d
public "set"(arg0: integer, arg1: $FloatBuffer$Type): $Matrix4x3d
public "set"(arg0: $ByteBuffer$Type): $Matrix4x3d
public "set"(arg0: $DoubleBuffer$Type): $Matrix4x3d
public "set"(arg0: integer, arg1: $DoubleBuffer$Type): $Matrix4x3d
public "set"(arg0: (float)[]): $Matrix4x3d
public "set"(arg0: $Matrix4dc$Type): $Matrix4x3d
public "set"(arg0: $Matrix4x3fc$Type): $Matrix4x3d
public "set"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "set"(arg0: $AxisAngle4d$Type): $Matrix4x3d
public "set"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Vector3dc$Type): $Matrix4x3d
public "set"(arg0: $Matrix3dc$Type): $Matrix4x3d
public "set"(arg0: $Matrix3fc$Type): $Matrix4x3d
public "properties"(): integer
public "zero"(): $Matrix4x3d
public "pick"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: (integer)[], arg5: $Matrix4x3d$Type): $Matrix4x3d
public "pick"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: (integer)[]): $Matrix4x3d
public "isFinite"(): boolean
public "swap"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: $Quaterniondc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: $AxisAngle4d$Type): $Matrix4x3d
public "rotate"(arg0: $AxisAngle4f$Type): $Matrix4x3d
public "rotate"(arg0: $Quaternionfc$Type): $Matrix4x3d
public "rotate"(arg0: $Quaterniondc$Type): $Matrix4x3d
public "rotate"(arg0: $AxisAngle4f$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: double, arg1: $Vector3fc$Type): $Matrix4x3d
public "rotate"(arg0: double, arg1: $Vector3fc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: $AxisAngle4d$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotate"(arg0: double, arg1: $Vector3dc$Type): $Matrix4x3d
public "rotate"(arg0: double, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "normal"(arg0: $Matrix3d$Type): $Matrix3d
public "normal"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "normal"(): $Matrix4x3d
public "sub"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "sub"(arg0: $Matrix4x3fc$Type): $Matrix4x3d
public "sub"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "sub"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "origin"(arg0: $Vector3d$Type): $Vector3d
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "mul"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "mul"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "mul"(arg0: $Matrix4x3fc$Type): $Matrix4x3d
public "mul"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "shadow"(arg0: $Vector4dc$Type, arg1: double, arg2: double, arg3: double, arg4: double, arg5: $Matrix4x3d$Type): $Matrix4x3d
public "shadow"(arg0: $Vector4dc$Type, arg1: double, arg2: double, arg3: double, arg4: double): $Matrix4x3d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: $Matrix4x3d$Type): $Matrix4x3d
public "shadow"(arg0: $Vector4dc$Type, arg1: $Matrix4x3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "shadow"(arg0: $Vector4dc$Type, arg1: $Matrix4x3dc$Type): $Matrix4x3d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3dc$Type, arg5: $Matrix4x3d$Type): $Matrix4x3d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3dc$Type): $Matrix4x3d
public "shadow"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): $Matrix4x3d
public "m12"(): double
public "m12"(arg0: double): $Matrix4x3d
public "m10"(): double
public "m10"(arg0: double): $Matrix4x3d
public "m11"(arg0: double): $Matrix4x3d
public "m11"(): double
public "translation"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "translation"(arg0: $Vector3dc$Type): $Matrix4x3d
public "translation"(arg0: $Vector3fc$Type): $Matrix4x3d
public "scaling"(arg0: $Vector3dc$Type): $Matrix4x3d
public "scaling"(arg0: double): $Matrix4x3d
public "scaling"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "invert"(): $Matrix4x3d
public "invert"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "translate"(arg0: $Vector3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "translate"(arg0: $Vector3dc$Type): $Matrix4x3d
public "translate"(arg0: $Vector3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "translate"(arg0: $Vector3fc$Type): $Matrix4x3d
public "translate"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "translate"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "rotationY"(arg0: double): $Matrix4x3d
public "rotationX"(arg0: double): $Matrix4x3d
public "rotation"(arg0: $Quaternionfc$Type): $Matrix4x3d
public "rotation"(arg0: double, arg1: $Vector3dc$Type): $Matrix4x3d
public "rotation"(arg0: $AxisAngle4d$Type): $Matrix4x3d
public "rotation"(arg0: $Quaterniondc$Type): $Matrix4x3d
public "rotation"(arg0: double, arg1: $Vector3fc$Type): $Matrix4x3d
public "rotation"(arg0: $AxisAngle4f$Type): $Matrix4x3d
public "rotation"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "rotateZYX"(arg0: $Vector3d$Type): $Matrix4x3d
public "rotateZYX"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "rotateZYX"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "rotateYXZ"(arg0: $Vector3d$Type): $Matrix4x3d
public "rotateYXZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "rotateYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "getEulerAnglesXYZ"(arg0: $Vector3d$Type): $Vector3d
public "rotateXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "rotateXYZ"(arg0: $Vector3d$Type): $Matrix4x3d
public "rotateXYZ"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "getEulerAnglesZYX"(arg0: $Vector3d$Type): $Vector3d
public "rotationXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "rotationYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "rotationZYX"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "lookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "lookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "positiveX"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalZ"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateLocalZ"(arg0: double): $Matrix4x3d
public "rotateLocalY"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateLocalY"(arg0: double): $Matrix4x3d
public "positiveY"(arg0: $Vector3d$Type): $Vector3d
public "rotateLocalX"(arg0: double): $Matrix4x3d
public "rotateLocalX"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "positiveZ"(arg0: $Vector3d$Type): $Vector3d
public "transformAab"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Vector3d$Type, arg7: $Vector3d$Type): $Matrix4x3d
public "transformAab"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3d$Type, arg3: $Vector3d$Type): $Matrix4x3d
public "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "rotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "rotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "rotationTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "rotationTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "mapZYnX"(): $Matrix4x3d
public "mapZYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapZnXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapZnXnY"(): $Matrix4x3d
public "mapXZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapXZY"(): $Matrix4x3d
public "mapZXY"(): $Matrix4x3d
public "mapZXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapZnXY"(): $Matrix4x3d
public "mapZnXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYZX"(): $Matrix4x3d
public "mapYXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYXZ"(): $Matrix4x3d
public "mapYXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYXnZ"(): $Matrix4x3d
public "mapYnXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYnXnZ"(): $Matrix4x3d
public "mapYnZX"(): $Matrix4x3d
public "mapYnZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYnZnX"(): $Matrix4x3d
public "mapYnZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "obliqueZ"(arg0: double, arg1: double): $Matrix4x3d
public "obliqueZ"(arg0: double, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "mapXZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapXZnY"(): $Matrix4x3d
public "mapXnZY"(): $Matrix4x3d
public "mapXnZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYZnX"(): $Matrix4x3d
public "mapZXnY"(): $Matrix4x3d
public "mapZXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapXnZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapXnZnY"(): $Matrix4x3d
public "mapXnYnZ"(): $Matrix4x3d
public "mapXnYnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapZYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapZYX"(): $Matrix4x3d
public "mapYnXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapYnXZ"(): $Matrix4x3d
public "mapnXnYnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnXnYnZ"(): $Matrix4x3d
public "mapnXnYZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnXnYZ"(): $Matrix4x3d
public "mapnXnZY"(): $Matrix4x3d
public "mapnXnZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYXnZ"(): $Matrix4x3d
public "mapnYXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYXZ"(): $Matrix4x3d
public "mapnYXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYnXZ"(): $Matrix4x3d
public "mapnYnXZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYnZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYnZnX"(): $Matrix4x3d
public "mapnZYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZYX"(): $Matrix4x3d
public "mapnXZnY"(): $Matrix4x3d
public "mapnXZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZnYX"(): $Matrix4x3d
public "mapnZnYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZnXY"(): $Matrix4x3d
public "mapnZnXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnXYnZ"(): $Matrix4x3d
public "mapnXYnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnXZY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnXZY"(): $Matrix4x3d
public "mapZnYnX"(): $Matrix4x3d
public "mapZnYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZXnY"(): $Matrix4x3d
public "mapnYnZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYnZX"(): $Matrix4x3d
public "negateY"(): $Matrix4x3d
public "negateY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYZnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYZnX"(): $Matrix4x3d
public "mapnZnXnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZnXnY"(): $Matrix4x3d
public "mapnXnZnY"(): $Matrix4x3d
public "mapnXnZnY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZXY"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZXY"(): $Matrix4x3d
public "negateX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "negateX"(): $Matrix4x3d
public "mapnZnYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZnYnX"(): $Matrix4x3d
public "mapnYnXnZ"(): $Matrix4x3d
public "mapnYnXnZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnZYnX"(): $Matrix4x3d
public "mapnZYnX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "negateZ"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "negateZ"(): $Matrix4x3d
public "mapZnYX"(): $Matrix4x3d
public "mapZnYX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYZX"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "mapnYZX"(): $Matrix4x3d
public "assume"(arg0: integer): $Matrix4x3d
public "set3x3"(arg0: $Matrix3fc$Type): $Matrix4x3d
public "set3x3"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "set3x3"(arg0: $Matrix3dc$Type): $Matrix4x3d
public "mulTranslation"(arg0: $Matrix4x3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "mulTranslation"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "mul3x3"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4x3d
public "mul3x3"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4x3d$Type): $Matrix4x3d
public "getTransposed"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "getTransposed"(arg0: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: (double)[]): (double)[]
public "getTransposed"(arg0: integer, arg1: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: $FloatBuffer$Type): $FloatBuffer
public "getTransposed"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposed"(arg0: (double)[], arg1: integer): (double)[]
public "determinant"(): double
public "mulComponentWise"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "mulComponentWise"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "invertOrtho"(): $Matrix4x3d
public "invertOrtho"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "transpose3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "transpose3x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "transpose3x3"(): $Matrix4x3d
public "setTranslation"(arg0: $Vector3dc$Type): $Matrix4x3d
public "setTranslation"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "getTranslation"(arg0: $Vector3d$Type): $Vector3d
public "getScale"(arg0: $Vector3d$Type): $Vector3d
public "setRotationYXZ"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "setRotationXYZ"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "setRotationZYX"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "scaleAround"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "translationRotate"(arg0: double, arg1: double, arg2: double, arg3: $Quaterniondc$Type): $Matrix4x3d
public "translationRotate"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type): $Matrix4x3d
public "translationRotate"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Matrix4x3d
public "transformPosition"(arg0: $Vector3d$Type): $Vector3d
public "transformPosition"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformDirection"(arg0: $Vector3dc$Type, arg1: $Vector3d$Type): $Vector3d
public "transformDirection"(arg0: $Vector3d$Type): $Vector3d
public "scaleXY"(arg0: double, arg1: double): $Matrix4x3d
public "scaleXY"(arg0: double, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "scaleLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "scaleLocal"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "rotateTranslation"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "rotateTranslation"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateTranslation"(arg0: $Quaterniondc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4x3d$Type): $Matrix4x3d
public "ortho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4x3d
public "rotateLocal"(arg0: $Quaternionfc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateLocal"(arg0: $Quaterniondc$Type): $Matrix4x3d
public "rotateLocal"(arg0: $Quaternionfc$Type): $Matrix4x3d
public "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "rotateLocal"(arg0: $Quaterniondc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateLocal"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "translateLocal"(arg0: $Vector3fc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "translateLocal"(arg0: $Vector3fc$Type): $Matrix4x3d
public "translateLocal"(arg0: $Vector3dc$Type): $Matrix4x3d
public "translateLocal"(arg0: $Vector3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "translateLocal"(arg0: double, arg1: double, arg2: double, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "translateLocal"(arg0: double, arg1: double, arg2: double): $Matrix4x3d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4x3d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "orthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4x3d$Type): $Matrix4x3d
public "setOrthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4x3d
public "setOrthoSymmetric"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "setOrthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4x3d
public "setOrthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean, arg7: $Matrix4x3d$Type): $Matrix4x3d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "orthoLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4x3d
public "ortho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "ortho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "lookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4x3d
public "lookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "lookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4x3d$Type): $Matrix4x3d
public "lookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4x3d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean, arg5: $Matrix4x3d$Type): $Matrix4x3d
public "orthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "setOrtho2D"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "setLookAlong"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "setLookAlong"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "setOrtho2DLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "ortho2D"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "ortho2D"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "setLookAt"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "setLookAt"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4x3d
public "setLookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "setLookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4x3d
public "lookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: $Matrix4x3d$Type): $Matrix4x3d
public "lookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4x3d$Type): $Matrix4x3d
public "lookAtLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4x3d
public "lookAtLH"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "rotationAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "rotateAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "rotateAround"(arg0: $Quaterniondc$Type, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "reflection"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "reflection"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "reflection"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "reflection"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "getRow"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
public "setColumn"(arg0: integer, arg1: $Vector3dc$Type): $Matrix4x3d
public "cofactor3x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "cofactor3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "cofactor3x3"(): $Matrix4x3d
public "setRow"(arg0: integer, arg1: $Vector4dc$Type): $Matrix4x3d
public "normalize3x3"(arg0: $Matrix4x3d$Type): $Matrix4x3d
public "normalize3x3"(arg0: $Matrix3d$Type): $Matrix3d
public "normalize3x3"(): $Matrix4x3d
public "frustumPlane"(arg0: integer, arg1: $Vector4d$Type): $Vector4d
public "billboardSpherical"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "billboardSpherical"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "arcball"(arg0: double, arg1: $Vector3dc$Type, arg2: double, arg3: double): $Matrix4x3d
public "arcball"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "arcball"(arg0: double, arg1: $Vector3dc$Type, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "arcball"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "mulOrtho"(arg0: $Matrix4x3dc$Type): $Matrix4x3d
public "mulOrtho"(arg0: $Matrix4x3dc$Type, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "get4x4"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: integer, arg1: $DoubleBuffer$Type): $DoubleBuffer
public "get4x4"(arg0: $ByteBuffer$Type): $ByteBuffer
public "get4x4"(arg0: (float)[], arg1: integer): (float)[]
public "get4x4"(arg0: (float)[]): (float)[]
public "get4x4"(arg0: (double)[], arg1: integer): (double)[]
public "get4x4"(arg0: (double)[]): (double)[]
public "get4x4"(arg0: $DoubleBuffer$Type): $DoubleBuffer
public "getFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "setFloats"(arg0: $ByteBuffer$Type): $Matrix4x3d
public "setFloats"(arg0: integer, arg1: $ByteBuffer$Type): $Matrix4x3d
public "setOrtho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "setOrtho"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: boolean): $Matrix4x3d
public "m31"(arg0: double): $Matrix4x3d
public "m31"(): double
public "m20"(arg0: double): $Matrix4x3d
public "m20"(): double
public "m21"(): double
public "m21"(arg0: double): $Matrix4x3d
public "m30"(arg0: double): $Matrix4x3d
public "m30"(): double
public "m22"(): double
public "m22"(arg0: double): $Matrix4x3d
public "m32"(arg0: double): $Matrix4x3d
public "m32"(): double
public "determineProperties"(): $Matrix4x3d
public "getTransposedFloats"(arg0: integer, arg1: $ByteBuffer$Type): $ByteBuffer
public "getTransposedFloats"(arg0: $ByteBuffer$Type): $ByteBuffer
public "translationRotateMul"(arg0: double, arg1: double, arg2: double, arg3: $Quaternionfc$Type, arg4: $Matrix4x3dc$Type): $Matrix4x3d
public "translationRotateMul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: $Matrix4x3dc$Type): $Matrix4x3d
public "translationRotateScaleMul"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type, arg2: $Vector3dc$Type, arg3: $Matrix4x3dc$Type): $Matrix4x3d
public "translationRotateScaleMul"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double, arg10: $Matrix4x3dc$Type): $Matrix4x3d
public "setFromAddress"(arg0: long): $Matrix4x3d
public "getToAddress"(arg0: long): $Matrix4x3dc
public "m00"(arg0: double): $Matrix4x3d
public "m00"(): double
public "m01"(): double
public "m01"(arg0: double): $Matrix4x3d
public "m02"(): double
public "m02"(arg0: double): $Matrix4x3d
public "rotateZ"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateZ"(arg0: double): $Matrix4x3d
public "rotateX"(arg0: double): $Matrix4x3d
public "rotateX"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateY"(arg0: double, arg1: $Matrix4x3d$Type): $Matrix4x3d
public "rotateY"(arg0: double): $Matrix4x3d
public "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "reflect"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: $Matrix4x3d$Type): $Matrix4x3d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): $Matrix4x3d
public "reflect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: $Matrix4x3d$Type): $Matrix4x3d
public "reflect"(arg0: $Quaterniondc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "reflect"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type): $Matrix4x3d
public "lerp"(arg0: $Matrix4x3dc$Type, arg1: double): $Matrix4x3d
public "lerp"(arg0: $Matrix4x3dc$Type, arg1: double, arg2: $Matrix4x3d$Type): $Matrix4x3d
public "rotationZ"(arg0: double): $Matrix4x3d
public "getColumn"(arg0: integer, arg1: $Vector3d$Type): $Vector3d
public "billboardCylindrical"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "translationRotateTowards"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double): $Matrix4x3d
public "translationRotateTowards"(arg0: $Vector3dc$Type, arg1: $Vector3dc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "translationRotateInvert"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Matrix4x3d
public "translationRotateInvert"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type): $Matrix4x3d
public "setOrthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: boolean): $Matrix4x3d
public "setOrthoSymmetricLH"(arg0: double, arg1: double, arg2: double, arg3: double): $Matrix4x3d
public "translationRotateScale"(arg0: $Vector3fc$Type, arg1: $Quaternionfc$Type, arg2: $Vector3fc$Type): $Matrix4x3d
public "translationRotateScale"(arg0: $Vector3dc$Type, arg1: $Quaterniondc$Type, arg2: $Vector3dc$Type): $Matrix4x3d
public "translationRotateScale"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double, arg8: double, arg9: double): $Matrix4x3d
public "normalizedPositiveY"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveZ"(arg0: $Vector3d$Type): $Vector3d
public "normalizedPositiveX"(arg0: $Vector3d$Type): $Vector3d
public "getNormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getNormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
public "getUnnormalizedRotation"(arg0: $Quaternionf$Type): $Quaternionf
public "getUnnormalizedRotation"(arg0: $Quaterniond$Type): $Quaterniond
get "finite"(): boolean
set "3x3"(value: $Matrix3fc$Type)
set "3x3"(value: $Matrix4x3dc$Type)
set "3x3"(value: $Matrix3dc$Type)
set "floats"(value: $ByteBuffer$Type)
set "fromAddress"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4x3d$Type = ($Matrix4x3d);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4x3d_ = $Matrix4x3d$Type;
}}
