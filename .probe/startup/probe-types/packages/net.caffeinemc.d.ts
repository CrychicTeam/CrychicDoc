declare module "packages/net/caffeinemc/mods/sodium/api/vertex/buffer/$VertexBufferWriter" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export interface $VertexBufferWriter {

 "push"(arg0: $MemoryStack$Type, arg1: long, arg2: integer, arg3: $VertexFormatDescription$Type): void
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
function copyInto(arg0: $VertexBufferWriter$Type, arg1: $MemoryStack$Type, arg2: long, arg3: integer, arg4: $VertexFormatDescription$Type): void
function tryOf(arg0: $VertexConsumer$Type): $VertexBufferWriter
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
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/common/$ColorVertex" {
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $ColorVertex {
static readonly "FORMAT": $VertexFormatDescription
static readonly "STRIDE": integer

constructor()

public static "put"(arg0: long, arg1: $Matrix4f$Type, arg2: float, arg3: float, arg4: float, arg5: integer): void
public static "put"(arg0: long, arg1: float, arg2: float, arg3: float, arg4: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorVertex$Type = ($ColorVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorVertex_ = $ColorVertex$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/common/$OverlayAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $OverlayAttribute {

constructor()

public static "get"(arg0: long): integer
public static "set"(arg0: long, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverlayAttribute$Type = ($OverlayAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverlayAttribute_ = $OverlayAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/common/$TextureAttribute" {
import {$Vector2f, $Vector2f$Type} from "packages/org/joml/$Vector2f"

export class $TextureAttribute {

constructor()

public static "get"(arg0: long): $Vector2f
public static "put"(arg0: long, arg1: float, arg2: float): void
public static "put"(arg0: long, arg1: $Vector2f$Type): void
public static "getU"(arg0: long): float
public static "getV"(arg0: long): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureAttribute$Type = ($TextureAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureAttribute_ = $TextureAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/common/$LineVertex" {
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $LineVertex {
static readonly "FORMAT": $VertexFormatDescription
static readonly "STRIDE": integer

constructor()

public static "put"(arg0: long, arg1: float, arg2: float, arg3: float, arg4: integer, arg5: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineVertex$Type = ($LineVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineVertex_ = $LineVertex$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription" {
import {$CommonVertexAttribute, $CommonVertexAttribute$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/$CommonVertexAttribute"

export interface $VertexFormatDescription {

 "id"(): integer
 "stride"(): integer
 "isSimpleFormat"(): boolean
 "containsElement"(arg0: $CommonVertexAttribute$Type): boolean
 "getElementOffset"(arg0: $CommonVertexAttribute$Type): integer
}

export namespace $VertexFormatDescription {
const probejs$$marker: never
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
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/common/$PositionAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PositionAttribute {

constructor()

public static "put"(arg0: long, arg1: float, arg2: float, arg3: float): void
public static "getY"(arg0: long): float
public static "getZ"(arg0: long): float
public static "getX"(arg0: long): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PositionAttribute$Type = ($PositionAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PositionAttribute_ = $PositionAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/memory/$MemoryIntrinsics" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MemoryIntrinsics {

constructor()

public static "copyMemory"(arg0: long, arg1: long, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryIntrinsics$Type = ($MemoryIntrinsics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryIntrinsics_ = $MemoryIntrinsics$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/common/$ParticleVertex" {
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $ParticleVertex {
static readonly "FORMAT": $VertexFormatDescription
static readonly "STRIDE": integer

constructor()

public static "put"(arg0: long, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleVertex$Type = ($ParticleVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleVertex_ = $ParticleVertex$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/util/$NormI8" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"

export class $NormI8 {

constructor()

public static "pack"(arg0: float, arg1: float, arg2: float): integer
public static "pack"(arg0: $Vector3f$Type): integer
public static "unpackX"(arg0: integer): float
public static "unpackY"(arg0: integer): float
public static "unpackZ"(arg0: integer): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NormI8$Type = ($NormI8);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NormI8_ = $NormI8$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/math/$MatrixHelper" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"

export class $MatrixHelper {

constructor()

public static "transformNormalY"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): float
public static "transformPositionZ"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): float
public static "transformPositionY"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): float
public static "transformNormalZ"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): float
public static "transformNormalX"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): float
public static "transformPositionX"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): float
public static "rotateZYX"(arg0: $PoseStack$Pose$Type, arg1: float, arg2: float, arg3: float): void
public static "transformNormal"(arg0: $Matrix3f$Type, arg1: $Direction$Type): integer
public static "transformNormal"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): integer
public static "transformNormal"(arg0: $Matrix3f$Type, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatrixHelper$Type = ($MatrixHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatrixHelper_ = $MatrixHelper$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/util/$ColorABGR" {
import {$ColorU8, $ColorU8$Type} from "packages/net/caffeinemc/mods/sodium/api/util/$ColorU8"

export class $ColorABGR implements $ColorU8 {

constructor()

public static "pack"(arg0: float, arg1: float, arg2: float, arg3: float): integer
public static "pack"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public static "pack"(arg0: float, arg1: float, arg2: float): integer
public static "unpackRed"(arg0: integer): integer
public static "unpackGreen"(arg0: integer): integer
public static "unpackAlpha"(arg0: integer): integer
public static "unpackBlue"(arg0: integer): integer
public static "withAlpha"(arg0: integer, arg1: integer): integer
public static "withAlpha"(arg0: integer, arg1: float): integer
public static "normalizedFloatToByte"(arg0: float): integer
public static "byteToNormalizedFloat"(arg0: integer): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorABGR$Type = ($ColorABGR);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorABGR_ = $ColorABGR$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/common/$ColorAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ColorAttribute {

constructor()

public static "get"(arg0: long): integer
public static "set"(arg0: long, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorAttribute$Type = ($ColorAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorAttribute_ = $ColorAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/internal/$DependencyInjection" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $DependencyInjection {

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>, arg1: string): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DependencyInjection$Type = ($DependencyInjection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DependencyInjection_ = $DependencyInjection$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/serializer/$VertexSerializer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $VertexSerializer {

 "serialize"(arg0: long, arg1: long, arg2: integer): void

(arg0: long, arg1: long, arg2: integer): void
}

export namespace $VertexSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexSerializer$Type = ($VertexSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexSerializer_ = $VertexSerializer$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/util/$ColorMixer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ColorMixer {

constructor()

public static "mix"(arg0: integer, arg1: integer, arg2: float): integer
public static "mul"(arg0: integer, arg1: integer): integer
public static "mulSingleWithoutAlpha"(arg0: integer, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorMixer$Type = ($ColorMixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorMixer_ = $ColorMixer$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/serializer/$VertexSerializerRegistry" {
import {$VertexSerializer, $VertexSerializer$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/serializer/$VertexSerializer"
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export interface $VertexSerializerRegistry {

 "get"(arg0: $VertexFormatDescription$Type, arg1: $VertexFormatDescription$Type): $VertexSerializer

(arg0: $VertexFormatDescription$Type, arg1: $VertexFormatDescription$Type): $VertexSerializer
}

export namespace $VertexSerializerRegistry {
const INSTANCE: $VertexSerializerRegistry
function instance(): $VertexSerializerRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexSerializerRegistry$Type = ($VertexSerializerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexSerializerRegistry_ = $VertexSerializerRegistry$Type;
}}
declare module "packages/net/caffeinemc/caffeineconfig/$AbstractCaffeineConfigMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $AbstractCaffeineConfigMixinPlugin implements $IMixinConfigPlugin {

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
export type $AbstractCaffeineConfigMixinPlugin$Type = ($AbstractCaffeineConfigMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCaffeineConfigMixinPlugin_ = $AbstractCaffeineConfigMixinPlugin$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/util/$ColorARGB" {
import {$ColorU8, $ColorU8$Type} from "packages/net/caffeinemc/mods/sodium/api/util/$ColorU8"

export class $ColorARGB implements $ColorU8 {

constructor()

public static "pack"(arg0: integer, arg1: integer, arg2: integer): integer
public static "pack"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public static "unpackRed"(arg0: integer): integer
public static "unpackGreen"(arg0: integer): integer
public static "unpackAlpha"(arg0: integer): integer
public static "unpackBlue"(arg0: integer): integer
public static "withAlpha"(arg0: integer, arg1: integer): integer
public static "toABGR"(arg0: integer): integer
public static "toABGR"(arg0: integer, arg1: integer): integer
public static "normalizedFloatToByte"(arg0: float): integer
public static "byteToNormalizedFloat"(arg0: integer): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorARGB$Type = ($ColorARGB);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorARGB_ = $ColorARGB$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatRegistry" {
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"
import {$VertexFormat, $VertexFormat$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormat"

export interface $VertexFormatRegistry {

 "get"(arg0: $VertexFormat$Type): $VertexFormatDescription

(arg0: $VertexFormat$Type): $VertexFormatDescription
}

export namespace $VertexFormatRegistry {
const INSTANCE: $VertexFormatRegistry
function instance(): $VertexFormatRegistry
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
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/common/$NormalAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $NormalAttribute {

constructor()

public static "get"(arg0: long): integer
public static "set"(arg0: long, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NormalAttribute$Type = ($NormalAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NormalAttribute_ = $NormalAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/common/$LightAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LightAttribute {

constructor()

public static "get"(arg0: long): integer
public static "set"(arg0: long, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightAttribute$Type = ($LightAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightAttribute_ = $LightAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/attributes/$CommonVertexAttribute" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CommonVertexAttribute extends $Enum<($CommonVertexAttribute)> {
static readonly "POSITION": $CommonVertexAttribute
static readonly "COLOR": $CommonVertexAttribute
static readonly "TEXTURE": $CommonVertexAttribute
static readonly "OVERLAY": $CommonVertexAttribute
static readonly "LIGHT": $CommonVertexAttribute
static readonly "NORMAL": $CommonVertexAttribute
static readonly "COUNT": integer


public static "values"(): ($CommonVertexAttribute)[]
public static "valueOf"(arg0: string): $CommonVertexAttribute
public "getByteLength"(): integer
public static "getCommonType"(arg0: $VertexFormatElement$Type): $CommonVertexAttribute
get "byteLength"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonVertexAttribute$Type = (("normal") | ("color") | ("overlay") | ("light") | ("texture") | ("position")) | ($CommonVertexAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonVertexAttribute_ = $CommonVertexAttribute$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/common/$GlyphVertex" {
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $GlyphVertex {
static readonly "FORMAT": $VertexFormatDescription
static readonly "STRIDE": integer

constructor()

public static "put"(arg0: long, arg1: float, arg2: float, arg3: float, arg4: integer, arg5: float, arg6: float, arg7: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlyphVertex$Type = ($GlyphVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlyphVertex_ = $GlyphVertex$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/util/$ColorU8" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ColorU8 {

}

export namespace $ColorU8 {
const COMPONENT_BITS: integer
const COMPONENT_MASK: integer
const COMPONENT_RANGE: float
const COMPONENT_RANGE_INVERSE: float
function normalizedFloatToByte(arg0: float): integer
function byteToNormalizedFloat(arg0: integer): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorU8$Type = ($ColorU8);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorU8_ = $ColorU8$Type;
}}
declare module "packages/net/caffeinemc/mods/sodium/api/vertex/format/common/$ModelVertex" {
import {$VertexFormatDescription, $VertexFormatDescription$Type} from "packages/net/caffeinemc/mods/sodium/api/vertex/format/$VertexFormatDescription"

export class $ModelVertex {
static readonly "FORMAT": $VertexFormatDescription
static readonly "STRIDE": integer

constructor()

public static "write"(arg0: long, arg1: float, arg2: float, arg3: float, arg4: integer, arg5: float, arg6: float, arg7: integer, arg8: integer, arg9: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModelVertex$Type = ($ModelVertex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModelVertex_ = $ModelVertex$Type;
}}
