declare module "packages/javax/sound/sampled/$Control" {
import {$Control$Type, $Control$Type$Type} from "packages/javax/sound/sampled/$Control$Type"

export class $Control {


public "toString"(): string
public "getType"(): $Control$Type
get "type"(): $Control$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Control$Type = ($Control);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Control_ = $Control$Type;
}}
declare module "packages/javax/sound/sampled/$DataLine" {
import {$AudioFormat, $AudioFormat$Type} from "packages/javax/sound/sampled/$AudioFormat"
import {$Line, $Line$Type} from "packages/javax/sound/sampled/$Line"
import {$LineListener, $LineListener$Type} from "packages/javax/sound/sampled/$LineListener"
import {$Line$Info, $Line$Info$Type} from "packages/javax/sound/sampled/$Line$Info"
import {$Control$Type, $Control$Type$Type} from "packages/javax/sound/sampled/$Control$Type"
import {$Control, $Control$Type} from "packages/javax/sound/sampled/$Control"

export interface $DataLine extends $Line {

 "flush"(): void
 "start"(): void
 "stop"(): void
 "available"(): integer
 "isActive"(): boolean
 "getFormat"(): $AudioFormat
 "getLevel"(): float
 "drain"(): void
 "isRunning"(): boolean
 "getBufferSize"(): integer
 "getFramePosition"(): integer
 "getLongFramePosition"(): long
 "getMicrosecondPosition"(): long
 "isOpen"(): boolean
 "close"(): void
 "open"(): void
 "getControl"(arg0: $Control$Type$Type): $Control
 "getLineInfo"(): $Line$Info
 "addLineListener"(arg0: $LineListener$Type): void
 "removeLineListener"(arg0: $LineListener$Type): void
 "isControlSupported"(arg0: $Control$Type$Type): boolean
 "getControls"(): ($Control)[]
}

export namespace $DataLine {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataLine$Type = ($DataLine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataLine_ = $DataLine$Type;
}}
declare module "packages/javax/sound/sampled/$LineListener" {
import {$LineEvent, $LineEvent$Type} from "packages/javax/sound/sampled/$LineEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $LineListener extends $EventListener {

 "update"(arg0: $LineEvent$Type): void

(arg0: $LineEvent$Type): void
}

export namespace $LineListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineListener$Type = ($LineListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineListener_ = $LineListener$Type;
}}
declare module "packages/javax/sound/sampled/$LineEvent$Type" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LineEvent$Type {
static readonly "OPEN": $LineEvent$Type
static readonly "CLOSE": $LineEvent$Type
static readonly "START": $LineEvent$Type
static readonly "STOP": $LineEvent$Type


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineEvent$Type$Type = ($LineEvent$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineEvent$Type_ = $LineEvent$Type$Type;
}}
declare module "packages/javax/sound/sampled/$AudioFormat$Encoding" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AudioFormat$Encoding {
static readonly "PCM_SIGNED": $AudioFormat$Encoding
static readonly "PCM_UNSIGNED": $AudioFormat$Encoding
static readonly "PCM_FLOAT": $AudioFormat$Encoding
static readonly "ULAW": $AudioFormat$Encoding
static readonly "ALAW": $AudioFormat$Encoding

constructor(arg0: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AudioFormat$Encoding$Type = ($AudioFormat$Encoding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AudioFormat$Encoding_ = $AudioFormat$Encoding$Type;
}}
declare module "packages/javax/sound/sampled/$Line$Info" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Line$Info {

constructor(arg0: $Class$Type<(any)>)

public "toString"(): string
public "matches"(arg0: $Line$Info$Type): boolean
public "getLineClass"(): $Class<(any)>
get "lineClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Line$Info$Type = ($Line$Info);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Line$Info_ = $Line$Info$Type;
}}
declare module "packages/javax/sound/sampled/$Line" {
import {$LineListener, $LineListener$Type} from "packages/javax/sound/sampled/$LineListener"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$Line$Info, $Line$Info$Type} from "packages/javax/sound/sampled/$Line$Info"
import {$Control$Type, $Control$Type$Type} from "packages/javax/sound/sampled/$Control$Type"
import {$Control, $Control$Type} from "packages/javax/sound/sampled/$Control"

export interface $Line extends $AutoCloseable {

 "isOpen"(): boolean
 "close"(): void
 "open"(): void
 "getControl"(arg0: $Control$Type$Type): $Control
 "getLineInfo"(): $Line$Info
 "addLineListener"(arg0: $LineListener$Type): void
 "removeLineListener"(arg0: $LineListener$Type): void
 "isControlSupported"(arg0: $Control$Type$Type): boolean
 "getControls"(): ($Control)[]
}

export namespace $Line {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Line$Type = ($Line);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Line_ = $Line$Type;
}}
declare module "packages/javax/sound/sampled/$AudioFileFormat" {
import {$AudioFormat, $AudioFormat$Type} from "packages/javax/sound/sampled/$AudioFormat"
import {$AudioFileFormat$Type, $AudioFileFormat$Type$Type} from "packages/javax/sound/sampled/$AudioFileFormat$Type"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AudioFileFormat {

constructor(arg0: $AudioFileFormat$Type$Type, arg1: $AudioFormat$Type, arg2: integer, arg3: $Map$Type<(string), (any)>)
constructor(arg0: $AudioFileFormat$Type$Type, arg1: $AudioFormat$Type, arg2: integer)

public "getProperty"(arg0: string): any
public "toString"(): string
public "properties"(): $Map<(string), (any)>
public "getType"(): $AudioFileFormat$Type
public "getFormat"(): $AudioFormat
public "getByteLength"(): integer
public "getFrameLength"(): integer
get "type"(): $AudioFileFormat$Type
get "format"(): $AudioFormat
get "byteLength"(): integer
get "frameLength"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AudioFileFormat$Type = ($AudioFileFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AudioFileFormat_ = $AudioFileFormat$Type;
}}
declare module "packages/javax/sound/sampled/$LineEvent" {
import {$LineEvent$Type, $LineEvent$Type$Type} from "packages/javax/sound/sampled/$LineEvent$Type"
import {$Line, $Line$Type} from "packages/javax/sound/sampled/$Line"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $LineEvent extends $EventObject {

constructor(arg0: $Line$Type, arg1: $LineEvent$Type$Type, arg2: long)

public "toString"(): string
public "getType"(): $LineEvent$Type
public "getLine"(): $Line
public "getFramePosition"(): long
get "type"(): $LineEvent$Type
get "line"(): $Line
get "framePosition"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineEvent$Type = ($LineEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineEvent_ = $LineEvent$Type;
}}
declare module "packages/javax/sound/sampled/$TargetDataLine" {
import {$AudioFormat, $AudioFormat$Type} from "packages/javax/sound/sampled/$AudioFormat"
import {$LineListener, $LineListener$Type} from "packages/javax/sound/sampled/$LineListener"
import {$DataLine, $DataLine$Type} from "packages/javax/sound/sampled/$DataLine"
import {$Line$Info, $Line$Info$Type} from "packages/javax/sound/sampled/$Line$Info"
import {$Control$Type, $Control$Type$Type} from "packages/javax/sound/sampled/$Control$Type"
import {$Control, $Control$Type} from "packages/javax/sound/sampled/$Control"

export interface $TargetDataLine extends $DataLine {

 "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
 "open"(arg0: $AudioFormat$Type): void
 "open"(arg0: $AudioFormat$Type, arg1: integer): void
 "flush"(): void
 "start"(): void
 "stop"(): void
 "available"(): integer
 "isActive"(): boolean
 "getFormat"(): $AudioFormat
 "getLevel"(): float
 "drain"(): void
 "isRunning"(): boolean
 "getBufferSize"(): integer
 "getFramePosition"(): integer
 "getLongFramePosition"(): long
 "getMicrosecondPosition"(): long
 "isOpen"(): boolean
 "close"(): void
 "open"(): void
 "getControl"(arg0: $Control$Type$Type): $Control
 "getLineInfo"(): $Line$Info
 "addLineListener"(arg0: $LineListener$Type): void
 "removeLineListener"(arg0: $LineListener$Type): void
 "isControlSupported"(arg0: $Control$Type$Type): boolean
 "getControls"(): ($Control)[]
}

export namespace $TargetDataLine {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TargetDataLine$Type = ($TargetDataLine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TargetDataLine_ = $TargetDataLine$Type;
}}
declare module "packages/javax/sound/sampled/$AudioFormat" {
import {$AudioFormat$Encoding, $AudioFormat$Encoding$Type} from "packages/javax/sound/sampled/$AudioFormat$Encoding"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AudioFormat {

constructor(arg0: float, arg1: integer, arg2: integer, arg3: boolean, arg4: boolean)
constructor(arg0: $AudioFormat$Encoding$Type, arg1: float, arg2: integer, arg3: integer, arg4: integer, arg5: float, arg6: boolean, arg7: $Map$Type<(string), (any)>)
constructor(arg0: $AudioFormat$Encoding$Type, arg1: float, arg2: integer, arg3: integer, arg4: integer, arg5: float, arg6: boolean)

public "getProperty"(arg0: string): any
public "toString"(): string
public "matches"(arg0: $AudioFormat$Type): boolean
public "properties"(): $Map<(string), (any)>
public "isBigEndian"(): boolean
public "getEncoding"(): $AudioFormat$Encoding
public "getSampleRate"(): float
public "getFrameSize"(): integer
public "getChannels"(): integer
public "getFrameRate"(): float
public "getSampleSizeInBits"(): integer
get "bigEndian"(): boolean
get "encoding"(): $AudioFormat$Encoding
get "sampleRate"(): float
get "frameSize"(): integer
get "channels"(): integer
get "frameRate"(): float
get "sampleSizeInBits"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AudioFormat$Type = ($AudioFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AudioFormat_ = $AudioFormat$Type;
}}
declare module "packages/javax/sound/sampled/$AudioInputStream" {
import {$AudioFormat, $AudioFormat$Type} from "packages/javax/sound/sampled/$AudioFormat"
import {$TargetDataLine, $TargetDataLine$Type} from "packages/javax/sound/sampled/$TargetDataLine"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"

export class $AudioInputStream extends $InputStream {

constructor(arg0: $InputStream$Type, arg1: $AudioFormat$Type, arg2: long)
constructor(arg0: $TargetDataLine$Type)

public "read"(arg0: (byte)[]): integer
public "read"(): integer
public "read"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "close"(): void
public "mark"(arg0: integer): void
public "skip"(arg0: long): long
public "available"(): integer
public "markSupported"(): boolean
public "reset"(): void
public "getFormat"(): $AudioFormat
public "getFrameLength"(): long
get "format"(): $AudioFormat
get "frameLength"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AudioInputStream$Type = ($AudioInputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AudioInputStream_ = $AudioInputStream$Type;
}}
declare module "packages/javax/sound/sampled/$Control$Type" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Control$Type {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Control$Type$Type = ($Control$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Control$Type_ = $Control$Type$Type;
}}
declare module "packages/javax/sound/sampled/$AudioFileFormat$Type" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AudioFileFormat$Type {
static readonly "WAVE": $AudioFileFormat$Type
static readonly "AU": $AudioFileFormat$Type
static readonly "AIFF": $AudioFileFormat$Type
static readonly "AIFC": $AudioFileFormat$Type
static readonly "SND": $AudioFileFormat$Type

constructor(arg0: string, arg1: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getExtension"(): string
get "extension"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AudioFileFormat$Type$Type = ($AudioFileFormat$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AudioFileFormat$Type_ = $AudioFileFormat$Type$Type;
}}
declare module "packages/javax/sound/sampled/spi/$AudioFileReader" {
import {$AudioFileFormat, $AudioFileFormat$Type} from "packages/javax/sound/sampled/$AudioFileFormat"
import {$File, $File$Type} from "packages/java/io/$File"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$AudioInputStream, $AudioInputStream$Type} from "packages/javax/sound/sampled/$AudioInputStream"

export class $AudioFileReader {


public "getAudioFileFormat"(arg0: $File$Type): $AudioFileFormat
public "getAudioFileFormat"(arg0: $URL$Type): $AudioFileFormat
public "getAudioFileFormat"(arg0: $InputStream$Type): $AudioFileFormat
public "getAudioInputStream"(arg0: $File$Type): $AudioInputStream
public "getAudioInputStream"(arg0: $URL$Type): $AudioInputStream
public "getAudioInputStream"(arg0: $InputStream$Type): $AudioInputStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AudioFileReader$Type = ($AudioFileReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AudioFileReader_ = $AudioFileReader$Type;
}}
