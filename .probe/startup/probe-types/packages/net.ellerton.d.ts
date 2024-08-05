declare module "packages/net/ellerton/japng/map/$PngChunkMap" {
import {$PngChunkCode, $PngChunkCode$Type} from "packages/net/ellerton/japng/$PngChunkCode"

export class $PngChunkMap {
 "code": $PngChunkCode
 "dataLength": integer
 "dataPosition": integer
 "checksum": integer

constructor(arg0: $PngChunkCode$Type, arg1: integer, arg2: integer, arg3: integer)

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngChunkMap$Type = ($PngChunkMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngChunkMap_ = $PngChunkMap$Type;
}}
declare module "packages/net/ellerton/japng/argb8888/$Argb8888Bitmap" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Argb8888Bitmap {
readonly "array": (integer)[]
readonly "width": integer
readonly "height": integer

constructor(arg0: integer, arg1: integer)
constructor(arg0: (integer)[], arg1: integer, arg2: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getPixelArray"(): (integer)[]
public "makeView"(arg0: integer, arg1: integer): $Argb8888Bitmap
get "width"(): integer
get "height"(): integer
get "pixelArray"(): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Argb8888Bitmap$Type = ($Argb8888Bitmap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Argb8888Bitmap_ = $Argb8888Bitmap$Type;
}}
declare module "packages/net/ellerton/japng/chunks/$PngAnimationControl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PngAnimationControl {
readonly "numFrames": integer
readonly "numPlays": integer

constructor(arg0: integer, arg1: integer)

public "toString"(): string
public "loopForever"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngAnimationControl$Type = ($PngAnimationControl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngAnimationControl_ = $PngAnimationControl$Type;
}}
declare module "packages/net/ellerton/japng/chunks/$PngFrameControl" {
import {$PngChunkMap, $PngChunkMap$Type} from "packages/net/ellerton/japng/map/$PngChunkMap"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PngFrameControl {
readonly "sequenceNumber": integer
readonly "width": integer
readonly "height": integer
readonly "xOffset": integer
readonly "yOffset": integer
readonly "delayNumerator": short
readonly "delayDenominator": short
readonly "disposeOp": byte
readonly "blendOp": byte

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: short, arg6: short, arg7: byte, arg8: byte)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "appendImageData"(arg0: $PngChunkMap$Type): void
public "getImageChunks"(): $List<($PngChunkMap)>
public "getDelayMilliseconds"(): integer
get "imageChunks"(): $List<($PngChunkMap)>
get "delayMilliseconds"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngFrameControl$Type = ($PngFrameControl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngFrameControl_ = $PngFrameControl$Type;
}}
declare module "packages/net/ellerton/japng/$PngChunkCode" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PngChunkCode {
static readonly "IHDR": $PngChunkCode
static readonly "PLTE": $PngChunkCode
static readonly "IDAT": $PngChunkCode
static readonly "IEND": $PngChunkCode
static readonly "gAMA": $PngChunkCode
static readonly "bKGD": $PngChunkCode
static readonly "tRNS": $PngChunkCode
static readonly "acTL": $PngChunkCode
static readonly "fcTL": $PngChunkCode
static readonly "fdAT": $PngChunkCode
readonly "numeric": integer
readonly "letters": string


public "isAncillary"(): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "from"(arg0: integer): $PngChunkCode
public "isPublic"(): boolean
public "isPrivate"(): boolean
public "isCritical"(): boolean
public "isReserved"(): boolean
public "isSafeToCopy"(): boolean
get "ancillary"(): boolean
get "public"(): boolean
get "private"(): boolean
get "critical"(): boolean
get "reserved"(): boolean
get "safeToCopy"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngChunkCode$Type = ($PngChunkCode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngChunkCode_ = $PngChunkCode$Type;
}}
declare module "packages/net/ellerton/japng/$PngColourType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PngColourType extends $Enum<($PngColourType)> {
static readonly "PNG_GREYSCALE": $PngColourType
static readonly "PNG_TRUECOLOUR": $PngColourType
static readonly "PNG_INDEXED_COLOUR": $PngColourType
static readonly "PNG_GREYSCALE_WITH_ALPHA": $PngColourType
static readonly "PNG_TRUECOLOUR_WITH_ALPHA": $PngColourType
readonly "code": integer
readonly "componentsPerPixel": integer
readonly "allowedBitDepths": string
readonly "name": string
readonly "descriptino": string


public static "values"(): ($PngColourType)[]
public static "valueOf"(arg0: string): $PngColourType
public "isIndexed"(): boolean
public "hasAlpha"(): boolean
public static "fromByte"(arg0: byte): $PngColourType
public "supportsSubByteDepth"(): boolean
get "indexed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngColourType$Type = (("png_indexed_colour") | ("png_truecolour_with_alpha") | ("png_greyscale_with_alpha") | ("png_greyscale") | ("png_truecolour")) | ($PngColourType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngColourType_ = $PngColourType$Type;
}}
declare module "packages/net/ellerton/japng/chunks/$PngHeader" {
import {$DataInput, $DataInput$Type} from "packages/java/io/$DataInput"
import {$PngFrameControl, $PngFrameControl$Type} from "packages/net/ellerton/japng/chunks/$PngFrameControl"
import {$PngColourType, $PngColourType$Type} from "packages/net/ellerton/japng/$PngColourType"

export class $PngHeader {
readonly "width": integer
readonly "height": integer
readonly "bitDepth": byte
readonly "colourType": $PngColourType
readonly "compressionMethod": byte
readonly "filterMethod": byte
readonly "interlaceMethod": byte
readonly "bitsPerPixel": integer
readonly "bytesPerRow": integer
readonly "filterOffset": integer

constructor(arg0: integer, arg1: integer, arg2: byte, arg3: $PngColourType$Type)
constructor(arg0: integer, arg1: integer, arg2: byte, arg3: $PngColourType$Type, arg4: byte, arg5: byte, arg6: byte)

public "toString"(): string
public static "from"(arg0: $DataInput$Type): $PngHeader
public "isInterlaced"(): boolean
public static "makeTruecolourAlpha"(arg0: integer, arg1: integer): $PngHeader
public static "checkHeaderParameters"(arg0: integer, arg1: integer, arg2: byte, arg3: $PngColourType$Type, arg4: byte, arg5: byte, arg6: byte): void
public "hasAlphaChannel"(): boolean
public "isGreyscale"(): boolean
public "isZipCompression"(): boolean
public "adjustFor"(arg0: $PngFrameControl$Type): $PngHeader
public static "makeTruecolour"(arg0: integer, arg1: integer): $PngHeader
get "interlaced"(): boolean
get "greyscale"(): boolean
get "zipCompression"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PngHeader$Type = ($PngHeader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PngHeader_ = $PngHeader$Type;
}}
declare module "packages/net/ellerton/japng/argb8888/$Argb8888BitmapSequence" {
import {$PngAnimationControl, $PngAnimationControl$Type} from "packages/net/ellerton/japng/chunks/$PngAnimationControl"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Argb8888Bitmap, $Argb8888Bitmap$Type} from "packages/net/ellerton/japng/argb8888/$Argb8888Bitmap"
import {$PngHeader, $PngHeader$Type} from "packages/net/ellerton/japng/chunks/$PngHeader"
import {$Argb8888BitmapSequence$Frame, $Argb8888BitmapSequence$Frame$Type} from "packages/net/ellerton/japng/argb8888/$Argb8888BitmapSequence$Frame"

export class $Argb8888BitmapSequence {
readonly "header": $PngHeader
readonly "defaultImage": $Argb8888Bitmap

constructor(arg0: $PngHeader$Type)

public "receiveAnimationControl"(arg0: $PngAnimationControl$Type): void
public "receiveDefaultImage"(arg0: $Argb8888Bitmap$Type): void
public "isAnimated"(): boolean
public "getAnimationControl"(): $PngAnimationControl
public "hasDefaultImage"(): boolean
public "getAnimationFrames"(): $List<($Argb8888BitmapSequence$Frame)>
get "animated"(): boolean
get "animationControl"(): $PngAnimationControl
get "animationFrames"(): $List<($Argb8888BitmapSequence$Frame)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Argb8888BitmapSequence$Type = ($Argb8888BitmapSequence);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Argb8888BitmapSequence_ = $Argb8888BitmapSequence$Type;
}}
declare module "packages/net/ellerton/japng/argb8888/$Argb8888BitmapSequence$Frame" {
import {$PngFrameControl, $PngFrameControl$Type} from "packages/net/ellerton/japng/chunks/$PngFrameControl"
import {$Argb8888Bitmap, $Argb8888Bitmap$Type} from "packages/net/ellerton/japng/argb8888/$Argb8888Bitmap"

export class $Argb8888BitmapSequence$Frame {
readonly "control": $PngFrameControl
readonly "bitmap": $Argb8888Bitmap

constructor(arg0: $PngFrameControl$Type, arg1: $Argb8888Bitmap$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Argb8888BitmapSequence$Frame$Type = ($Argb8888BitmapSequence$Frame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Argb8888BitmapSequence$Frame_ = $Argb8888BitmapSequence$Frame$Type;
}}
