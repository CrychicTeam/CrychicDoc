declare module "packages/icyllis/arc3d/engine/$GpuResource" {
import {$IScratchKey, $IScratchKey$Type} from "packages/icyllis/arc3d/engine/$IScratchKey"
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$DirectContext, $DirectContext$Type} from "packages/icyllis/arc3d/engine/$DirectContext"
import {$GpuResource$UniqueID, $GpuResource$UniqueID$Type} from "packages/icyllis/arc3d/engine/$GpuResource$UniqueID"
import {$IUniqueKey, $IUniqueKey$Type} from "packages/icyllis/arc3d/engine/$IUniqueKey"

export class $GpuResource implements $RefCounted {


public "getContext"(): $DirectContext
public "isWrapped"(): boolean
public "ref"(): void
public "isDestroyed"(): boolean
public static "create"<T extends $GpuResource>(that: T): T
public static "create"<T extends $GpuResource>(sp: T, that: T): T
public static "move"<T extends $GpuResource>(sp: T, that: T): T
public static "move"<T extends $GpuResource>(sp: T): T
public "unique"(): boolean
public "getMemorySize"(): long
public "setLabel"(label: string): void
public "getUniqueID"(): $GpuResource$UniqueID
public "setUniqueKey"(key: $IUniqueKey$Type): void
public "makeBudgeted"(budgeted: boolean): void
public "isFree"(): boolean
public "removeScratchKey"(): void
public "getScratchKey"(): $IScratchKey
public "getUniqueKey"(): $IUniqueKey
public "getBudgetType"(): integer
public "removeUniqueKey"(): void
public "unref"(): void
public "hasRefOrCommandBufferUsage"(): boolean
public "addCommandBufferUsage"(): void
public "removeCommandBufferUsage"(): void
public "getLabel"(): string
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "context"(): $DirectContext
get "wrapped"(): boolean
get "destroyed"(): boolean
get "memorySize"(): long
set "label"(value: string)
get "uniqueID"(): $GpuResource$UniqueID
set "uniqueKey"(value: $IUniqueKey$Type)
get "free"(): boolean
get "scratchKey"(): $IScratchKey
get "uniqueKey"(): $IUniqueKey
get "budgetType"(): integer
get "label"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuResource$Type = ($GpuResource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuResource_ = $GpuResource$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLResourceProvider" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$GLSampler, $GLSampler$Type} from "packages/icyllis/arc3d/opengl/$GLSampler"

export class $GLResourceProvider extends $ResourceProvider {


public "findOrCreateCompatibleSampler"(samplerState: integer): $GLSampler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLResourceProvider$Type = ($GLResourceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLResourceProvider_ = $GLResourceProvider$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLUniformHandler" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"
import {$UniformHandler, $UniformHandler$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler"
import {$UniformHandler$UniformInfo, $UniformHandler$UniformInfo$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler$UniformInfo"

export class $GLUniformHandler extends $UniformHandler {
static readonly "NO_MANGLE_PREFIX": string
static readonly "PROJECTION_NAME": string
static readonly "Std140Layout": boolean
static readonly "Std430Layout": boolean
static readonly "MAIN_DESC_SET": integer
static readonly "SAMPLER_DESC_SET": integer
static readonly "INPUT_DESC_SET": integer
static readonly "UNIFORM_BINDING": integer
static readonly "UNIFORM_BLOCK_NAME": string
static readonly "INPUT_BINDING": integer


public "uniform"(index: integer): $UniformHandler$UniformInfo
public "getUniformVariable"(handle: integer): $ShaderVar
public "numUniforms"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLUniformHandler$Type = ($GLUniformHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLUniformHandler_ = $GLUniformHandler$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$IndexExpression" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $IndexExpression extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, pos: integer, base: $Expression$Type, index: $Expression$Type): $Expression
public static "make"(context: $Context$Type, pos: integer, base: $Expression$Type, index: $Expression$Type): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getIndex"(): $Expression
public "setIndex"(index: $Expression$Type): void
public "getBase"(): $Expression
public "setBase"(base: $Expression$Type): void
public "getKind"(): $Node$ExpressionKind
get "index"(): $Expression
set "index"(value: $Expression$Type)
get "base"(): $Expression
set "base"(value: $Expression$Type)
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IndexExpression$Type = ($IndexExpression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IndexExpression_ = $IndexExpression$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceView" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"

export class $SurfaceView implements $AutoCloseable {

constructor(proxy: $SurfaceProxy$Type)
constructor(proxy: $SurfaceProxy$Type, origin: integer, swizzle: short)

public "concat"(swizzle: short): void
public "close"(): void
public "reset"(): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isMipmapped"(): boolean
public "getSurface"(): $SurfaceProxy
public "getSwizzle"(): short
public "getOrigin"(): integer
public "refSurface"(): $SurfaceProxy
public "detachSurface"(): $SurfaceProxy
get "width"(): integer
get "height"(): integer
get "mipmapped"(): boolean
get "surface"(): $SurfaceProxy
get "swizzle"(): short
get "origin"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceView$Type = ($SurfaceView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceView_ = $SurfaceView$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Symbol" {
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Node$SymbolKind, $Node$SymbolKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$SymbolKind"

export class $Symbol extends $Node {
 "mPosition": integer


public "getName"(): string
public "accept"(visitor: $TreeVisitor$Type): boolean
public "setName"(name: string): void
public "getType"(): $Type
public "getKind"(): $Node$SymbolKind
get "name"(): string
set "name"(value: string)
get "type"(): $Type
get "kind"(): $Node$SymbolKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Symbol$Type = ($Symbol);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Symbol_ = $Symbol$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$PointerLValue" {
import {$Writer, $Writer$Type} from "packages/icyllis/arc3d/compiler/spirv/$Writer"
import {$LValue, $LValue$Type} from "packages/icyllis/arc3d/compiler/spirv/$LValue"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$SPIRVCodeGenerator, $SPIRVCodeGenerator$Type} from "packages/icyllis/arc3d/compiler/spirv/$SPIRVCodeGenerator"

export class $PointerLValue implements $LValue {


public "load"(gen: $SPIRVCodeGenerator$Type, writer: $Writer$Type): integer
public "store"(gen: $SPIRVCodeGenerator$Type, rvalue: integer, writer: $Writer$Type): void
public "applySwizzle"(components: (byte)[], newType: $Type$Type): boolean
public "getPointer"(): integer
get "pointer"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PointerLValue$Type = ($PointerLValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PointerLValue_ = $PointerLValue$Type;
}}
declare module "packages/icyllis/arc3d/engine/$KeyBuilder" {
import {$Key, $Key$Type} from "packages/icyllis/arc3d/engine/$Key"
import {$Key$StorageKey, $Key$StorageKey$Type} from "packages/icyllis/arc3d/engine/$Key$StorageKey"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IntArrayList, $IntArrayList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntArrayList"

export class $KeyBuilder extends $IntArrayList implements $Key {
static readonly "DEFAULT_INITIAL_CAPACITY": integer

constructor()
constructor(other: $KeyBuilder$Type)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "clear"(): void
public "flush"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "trim"(): void
public "toStorageKey"(): $Key$StorageKey
public "addBits"(numBits: integer, value: integer, label: string): void
public "addBool"(b: boolean, label: string): void
public "addInt32"(v: integer, label: string): void
public "appendComment"(comment: string): void
/**
 * 
 * @deprecated
 */
public "add"(arg0: integer): boolean
/**
 * 
 * @deprecated
 */
public "remove"(arg0: any): boolean
public static "of"(arg0: integer, arg1: integer): $IntList
public static "of"(arg0: integer): $IntList
public static "of"(arg0: integer, arg1: integer, arg2: integer): $IntList
/**
 * 
 * @deprecated
 */
public "contains"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyBuilder$Type = ($KeyBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyBuilder_ = $KeyBuilder$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$PostfixExpression" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Operator, $Operator$Type} from "packages/icyllis/arc3d/compiler/$Operator"

export class $PostfixExpression extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, base: $Expression$Type, op: $Operator$Type): $Expression
public static "make"(position: integer, base: $Expression$Type, op: $Operator$Type): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getOperator"(): $Operator
public "getOperand"(): $Expression
public "getKind"(): $Node$ExpressionKind
get "operator"(): $Operator
get "operand"(): $Expression
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostfixExpression$Type = ($PostfixExpression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostfixExpression_ = $PostfixExpression$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorScalar2Vector" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorScalar2Vector extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "make"(position: integer, type: $Type$Type, arg: $Expression$Type): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorScalar2Vector$Type = ($ConstructorScalar2Vector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorScalar2Vector_ = $ConstructorScalar2Vector$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$NFAtoDFA" {
import {$NFA, $NFA$Type} from "packages/icyllis/arc3d/compiler/parser/$NFA"
import {$DFA, $DFA$Type} from "packages/icyllis/arc3d/compiler/parser/$DFA"

export class $NFAtoDFA {
static readonly "START_CHAR": character
static readonly "END_CHAR": character

constructor(NFA: $NFA$Type)

public "convert"(): $DFA
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NFAtoDFA$Type = ($NFAtoDFA);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NFAtoDFA_ = $NFAtoDFA$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Poison" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $Poison extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "make"(context: $Context$Type, position: integer): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Poison$Type = ($Poison);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Poison_ = $Poison$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$SPIRVCodeGenerator" {
import {$TranslationUnit, $TranslationUnit$Type} from "packages/icyllis/arc3d/compiler/tree/$TranslationUnit"
import {$SPIRVVersion, $SPIRVVersion$Type} from "packages/icyllis/arc3d/compiler/$SPIRVVersion"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/compiler/$ShaderCaps"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$CodeGenerator, $CodeGenerator$Type} from "packages/icyllis/arc3d/compiler/$CodeGenerator"
import {$ShaderCompiler, $ShaderCompiler$Type} from "packages/icyllis/arc3d/compiler/$ShaderCompiler"
import {$TargetApi, $TargetApi$Type} from "packages/icyllis/arc3d/compiler/$TargetApi"

export class $SPIRVCodeGenerator extends $CodeGenerator {
static readonly "GENERATOR_MAGIC_NUMBER": integer
static readonly "NONE_ID": integer
readonly "mOutputTarget": $TargetApi
readonly "mOutputVersion": $SPIRVVersion

constructor(compiler: $ShaderCompiler$Type, translationUnit: $TranslationUnit$Type, shaderCaps: $ShaderCaps$Type)

public "generateCode"(): $ByteBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPIRVCodeGenerator$Type = ($SPIRVCodeGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPIRVCodeGenerator_ = $SPIRVCodeGenerator$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$OpsTask" {
import {$ClipResult, $ClipResult$Type} from "packages/icyllis/arc3d/engine/$ClipResult"
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$RenderTask, $RenderTask$Type} from "packages/icyllis/arc3d/engine/$RenderTask"
import {$Op, $Op$Type} from "packages/icyllis/arc3d/engine/ops/$Op"
import {$DrawOp, $DrawOp$Type} from "packages/icyllis/arc3d/engine/ops/$DrawOp"
import {$RenderTaskManager, $RenderTaskManager$Type} from "packages/icyllis/arc3d/engine/$RenderTaskManager"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"
import {$SurfaceAllocator, $SurfaceAllocator$Type} from "packages/icyllis/arc3d/engine/$SurfaceAllocator"

export class $OpsTask extends $RenderTask {
static readonly "STENCIL_CONTENT_DONT_CARE": integer
static readonly "STENCIL_CONTENT_USER_BITS_CLEARED": integer
static readonly "STENCIL_CONTENT_PRESERVED": integer
static readonly "RESOLVE_FLAG_MSAA": integer
static readonly "RESOLVE_FLAG_MIPMAPS": integer

constructor(drawingMgr: $RenderTaskManager$Type, writeView: $SurfaceView$Type)

public "execute"(flushState: $OpFlushState$Type): boolean
public "prepare"(flushState: $OpFlushState$Type): void
public "setInitialStencilContent"(stencilContent: integer): void
public "gatherSurfaceIntervals"(alloc: $SurfaceAllocator$Type): void
public "setColorLoadOp"(loadOp: byte, red: float, green: float, blue: float, alpha: float): void
public "addDrawOp"(op: $DrawOp$Type, clip: $ClipResult$Type, processorAnalysis: integer): void
public "addOp"(op: $Op$Type): void
set "initialStencilContent"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpsTask$Type = ($OpsTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpsTask_ = $OpsTask$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$DFAState" {
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"

export class $DFAState {
readonly "mIndex": integer
readonly "mStates": $IntList

constructor(index: integer, states: $IntList$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DFAState$Type = ($DFAState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DFAState_ = $DFAState$Type;
}}
declare module "packages/icyllis/arc3d/engine/$DirectContext" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$VkBackendContext, $VkBackendContext$Type} from "packages/icyllis/arc3d/vulkan/$VkBackendContext"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"
import {$ResourceCache, $ResourceCache$Type} from "packages/icyllis/arc3d/engine/$ResourceCache"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"

export class $DirectContext extends $RecordingContext {


public "getResourceProvider"(): $ResourceProvider
public static "makeOpenGL"(): $DirectContext
public static "makeOpenGL"(options: $ContextOptions$Type): $DirectContext
public "resetContext"(state: integer): void
public "getDevice"(): $GpuDevice
public static "makeVulkan"(backendContext: $VkBackendContext$Type): $DirectContext
public static "makeVulkan"(backendContext: $VkBackendContext$Type, options: $ContextOptions$Type): $DirectContext
public "getResourceCache"(): $ResourceCache
get "resourceProvider"(): $ResourceProvider
get "device"(): $GpuDevice
get "resourceCache"(): $ResourceCache
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectContext$Type = ($DirectContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectContext_ = $DirectContext$Type;
}}
declare module "packages/icyllis/arc3d/engine/$FlushInfo" {
import {$FlushInfo$FinishedCallback, $FlushInfo$FinishedCallback$Type} from "packages/icyllis/arc3d/engine/$FlushInfo$FinishedCallback"
import {$BackendSemaphore, $BackendSemaphore$Type} from "packages/icyllis/arc3d/engine/$BackendSemaphore"
import {$FlushInfo$SubmittedCallback, $FlushInfo$SubmittedCallback$Type} from "packages/icyllis/arc3d/engine/$FlushInfo$SubmittedCallback"

export class $FlushInfo {
 "mSignalSemaphores": ($BackendSemaphore)[]
 "mFinishedCallback": $FlushInfo$FinishedCallback
 "mSubmittedCallback": $FlushInfo$SubmittedCallback

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlushInfo$Type = ($FlushInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlushInfo_ = $FlushInfo$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLBackendFormat" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"

export class $GLBackendFormat extends $BackendFormat {


public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "make"(format: integer, isExternal: boolean): $GLBackendFormat
public static "make"(format: integer): $GLBackendFormat
public "getGLFormat"(): integer
public "getStencilBits"(): integer
public "getCompressionType"(): integer
public "getBytesPerBlock"(): integer
public "makeInternal"(): $BackendFormat
public "getChannelFlags"(): integer
public "isSRGB"(): boolean
public "getFormatKey"(): integer
public "isExternal"(): boolean
public "getBackend"(): integer
get "gLFormat"(): integer
get "stencilBits"(): integer
get "compressionType"(): integer
get "bytesPerBlock"(): integer
get "channelFlags"(): integer
get "sRGB"(): boolean
get "formatKey"(): integer
get "external"(): boolean
get "backend"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLBackendFormat$Type = ($GLBackendFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLBackendFormat_ = $GLBackendFormat$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$IfStatement" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $IfStatement extends $Statement {
 "mPosition": integer

constructor(position: integer, condition: $Expression$Type, whenTrue: $Statement$Type, whenFalse: $Statement$Type)

public "toString"(): string
public static "convert"(context: $Context$Type, position: integer, condition: $Expression$Type, whenTrue: $Statement$Type, whenFalse: $Statement$Type): $Statement
public static "make"(position: integer, condition: $Expression$Type, whenTrue: $Statement$Type, whenFalse: $Statement$Type): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "setCondition"(condition: $Expression$Type): void
public "getCondition"(): $Expression
public "getKind"(): $Node$StatementKind
public "setWhenTrue"(whenTrue: $Statement$Type): void
public "setWhenFalse"(whenFalse: $Statement$Type): void
public "getWhenFalse"(): $Statement
public "getWhenTrue"(): $Statement
set "condition"(value: $Expression$Type)
get "condition"(): $Expression
get "kind"(): $Node$StatementKind
set "whenTrue"(value: $Statement$Type)
set "whenFalse"(value: $Statement$Type)
get "whenFalse"(): $Statement
get "whenTrue"(): $Statement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IfStatement$Type = ($IfStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IfStatement_ = $IfStatement$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLBuffer" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $GLBuffer extends $GpuBuffer {
static readonly "kRead_LockMode": integer
static readonly "kWriteDiscard_LockMode": integer


public static "make"(device: $GLDevice$Type, size: integer, usage: integer): $GLBuffer
public "isLocked"(): boolean
public "getHandle"(): integer
public static "getBufferUsageM"(usage: integer): integer
public "getLockedBuffer"(): long
public static "getBufferStorageFlags"(usage: integer): integer
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "locked"(): boolean
get "handle"(): integer
get "lockedBuffer"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLBuffer$Type = ($GLBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLBuffer_ = $GLBuffer$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Node$SymbolKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Node$SymbolKind extends $Enum<($Node$SymbolKind)> {
static readonly "ANONYMOUS_FIELD": $Node$SymbolKind
static readonly "FUNCTION_DECL": $Node$SymbolKind
static readonly "TYPE": $Node$SymbolKind
static readonly "VARIABLE": $Node$SymbolKind


public static "values"(): ($Node$SymbolKind)[]
public static "valueOf"(name: string): $Node$SymbolKind
public "getType"(): $Class<(any)>
get "type"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$SymbolKind$Type = (("function_decl") | ("anonymous_field") | ("variable") | ("type")) | ($Node$SymbolKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node$SymbolKind_ = $Node$SymbolKind$Type;
}}
declare module "packages/icyllis/arc3d/compiler/analysis/$SymbolUsage$Count" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SymbolUsage$Count {

constructor()

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SymbolUsage$Count$Type = ($SymbolUsage$Count);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SymbolUsage$Count_ = $SymbolUsage$Count$Type;
}}
declare module "packages/icyllis/arc3d/core/$BlendMode" {
import {$Blender, $Blender$Type} from "packages/icyllis/arc3d/core/$Blender"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BlendMode extends $Enum<($BlendMode)> implements $Blender {
static readonly "CLEAR": $BlendMode
static readonly "SRC": $BlendMode
static readonly "DST": $BlendMode
static readonly "SRC_OVER": $BlendMode
static readonly "DST_OVER": $BlendMode
static readonly "SRC_IN": $BlendMode
static readonly "DST_IN": $BlendMode
static readonly "SRC_OUT": $BlendMode
static readonly "DST_OUT": $BlendMode
static readonly "SRC_ATOP": $BlendMode
static readonly "DST_ATOP": $BlendMode
static readonly "XOR": $BlendMode
static readonly "PLUS": $BlendMode
static readonly "PLUS_CLAMPED": $BlendMode
static readonly "MINUS": $BlendMode
static readonly "MINUS_CLAMPED": $BlendMode
static readonly "MODULATE": $BlendMode
static readonly "MULTIPLY": $BlendMode
static readonly "SCREEN": $BlendMode
static readonly "OVERLAY": $BlendMode
static readonly "DARKEN": $BlendMode
static readonly "LIGHTEN": $BlendMode
static readonly "COLOR_DODGE": $BlendMode
static readonly "COLOR_BURN": $BlendMode
static readonly "HARD_LIGHT": $BlendMode
static readonly "SOFT_LIGHT": $BlendMode
static readonly "DIFFERENCE": $BlendMode
static readonly "EXCLUSION": $BlendMode
static readonly "SUBTRACT": $BlendMode
static readonly "DIVIDE": $BlendMode
static readonly "LINEAR_DODGE": $BlendMode
static readonly "LINEAR_BURN": $BlendMode
static readonly "VIVID_LIGHT": $BlendMode
static readonly "LINEAR_LIGHT": $BlendMode
static readonly "PIN_LIGHT": $BlendMode
static readonly "HARD_MIX": $BlendMode
static readonly "DARKER_COLOR": $BlendMode
static readonly "LIGHTER_COLOR": $BlendMode
static readonly "HUE": $BlendMode
static readonly "SATURATION": $BlendMode
static readonly "COLOR": $BlendMode
static readonly "LUMINOSITY": $BlendMode
static readonly "ADD": $BlendMode
static readonly "COUNT": integer


public static "values"(): ($BlendMode)[]
public static "valueOf"(name: string): $BlendMode
public "apply"(arg0: (float)[], arg1: (float)[], arg2: (float)[]): void
public static "mode"(index: integer): $BlendMode
public "isAdvanced"(): boolean
public "asBlendMode"(): $BlendMode
get "advanced"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlendMode$Type = (("minus") | ("src_over") | ("dst") | ("color") | ("plus_clamped") | ("lighter_color") | ("screen") | ("exclusion") | ("src_atop") | ("linear_dodge") | ("vivid_light") | ("dst_over") | ("saturation") | ("src_in") | ("lighten") | ("pin_light") | ("color_dodge") | ("linear_light") | ("luminosity") | ("xor") | ("divide") | ("multiply") | ("modulate") | ("minus_clamped") | ("overlay") | ("src") | ("hard_mix") | ("src_out") | ("subtract") | ("clear") | ("plus") | ("linear_burn") | ("dst_out") | ("darken") | ("dst_in") | ("dst_atop") | ("soft_light") | ("difference") | ("hue") | ("hard_light") | ("color_burn") | ("darker_color")) | ($BlendMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlendMode_ = $BlendMode$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$PipelineBuilder" {
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$VaryingHandler, $VaryingHandler$Type} from "packages/icyllis/arc3d/engine/shading/$VaryingHandler"
import {$FragmentShaderBuilder, $FragmentShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$FragmentShaderBuilder"
import {$PipelineDesc, $PipelineDesc$Type} from "packages/icyllis/arc3d/engine/$PipelineDesc"
import {$VertexShaderBuilder, $VertexShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$VertexShaderBuilder"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$UniformHandler, $UniformHandler$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"

export class $PipelineBuilder {
 "mVS": $VertexShaderBuilder
 "mFS": $FragmentShaderBuilder
readonly "mDesc": $PipelineDesc
readonly "mPipelineInfo": $PipelineInfo
 "mProjectionUniform": integer
 "mGPImpl": $GeometryProcessor$ProgramImpl

constructor(desc: $PipelineDesc$Type, pipelineInfo: $PipelineInfo$Type)

public "caps"(): $Caps
public "shaderCaps"(): $ShaderCaps
public "addExtension"(shaderFlags: integer, extensionName: string): void
public "nameVariable"(prefix: character, name: string, mangle: boolean): string
public "nameVariable"(prefix: character, name: string): string
public "uniformHandler"(): $UniformHandler
public "varyingHandler"(): $VaryingHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PipelineBuilder$Type = ($PipelineBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PipelineBuilder_ = $PipelineBuilder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Engine" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Engine {

}

export namespace $Engine {
const COLOR_ENCODING_UNORM: integer
const COLOR_ENCODING_UNORM_PACK16: integer
const COLOR_ENCODING_UNORM_PACK32: integer
const COLOR_ENCODING_UNORM_SRGB: integer
const COLOR_ENCODING_FLOAT: integer
const CLAMP_TYPE_AUTO: integer
const CLAMP_TYPE_MANUAL: integer
const CLAMP_TYPE_NONE: integer
const MASK_FORMAT_A8: integer
const MASK_FORMAT_A565: integer
const MASK_FORMAT_ARGB: integer
const Ownership_Borrowed: boolean
const Ownership_Owned: boolean
const INVALID_RESOURCE_HANDLE: integer
function colorTypeClampType(ct: integer): integer
function colorTypeEncoding(ct: integer): integer
function colorTypeToPublic(ct: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Engine$Type = ($Engine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Engine_ = $Engine$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLRenderTarget" {
import {$GLTexture, $GLTexture$Type} from "packages/icyllis/arc3d/opengl/$GLTexture"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"

export class $GLRenderTarget extends $GpuRenderTarget {


public "toString"(): string
public "getFormat"(): integer
public "getBackendFormat"(): $BackendFormat
public "asTexture"(): $GLTexture
public static "makeWrapped"(device: $GLDevice$Type, width: integer, height: integer, format: integer, sampleCount: integer, framebuffer: integer, stencilBits: integer, ownership: boolean): $GLRenderTarget
public "bindStencil"(): void
public "getResolveFramebuffer"(): integer
public "getSampleFramebuffer"(): integer
public "getBackendRenderTarget"(): $BackendRenderTarget
public "ref"(): void
public static "getApproxSize"(size: integer): integer
public "unref"(): void
get "format"(): integer
get "backendFormat"(): $BackendFormat
get "resolveFramebuffer"(): integer
get "sampleFramebuffer"(): integer
get "backendRenderTarget"(): $BackendRenderTarget
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLRenderTarget$Type = ($GLRenderTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLRenderTarget_ = $GLRenderTarget$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuBufferPool" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$CommandBuffer, $CommandBuffer$Type} from "packages/icyllis/arc3d/engine/$CommandBuffer"
import {$Mesh, $Mesh$Type} from "packages/icyllis/arc3d/engine/$Mesh"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GpuBufferPool {
static readonly "DEFAULT_BUFFER_SIZE": integer


public "flush"(): void
public "reset"(): void
public "submit"(cmdBuffer: $CommandBuffer$Type): void
public static "makeInstancePool"(resourceProvider: $ResourceProvider$Type): $GpuBufferPool
public static "makeVertexPool"(resourceProvider: $ResourceProvider$Type): $GpuBufferPool
public static "makeIndexPool"(resourceProvider: $ResourceProvider$Type): $GpuBufferPool
public "makeSpace"(arg0: $Mesh$Type): long
public "makeWriter"(arg0: $Mesh$Type): $ByteBuffer
public "putBack"(bytes: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuBufferPool$Type = ($GpuBufferPool);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuBufferPool_ = $GpuBufferPool$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLBackendRenderTarget" {
import {$GLFramebufferInfo, $GLFramebufferInfo$Type} from "packages/icyllis/arc3d/opengl/$GLFramebufferInfo"
import {$GLBackendFormat, $GLBackendFormat$Type} from "packages/icyllis/arc3d/opengl/$GLBackendFormat"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"

export class $GLBackendRenderTarget extends $BackendRenderTarget {

constructor(width: integer, height: integer, sampleCount: integer, stencilBits: integer, info: $GLFramebufferInfo$Type)

public "isProtected"(): boolean
public "getBackendFormat"(): $GLBackendFormat
public "getSampleCount"(): integer
public "getStencilBits"(): integer
public "getGLFramebufferInfo"(info: $GLFramebufferInfo$Type): boolean
public "getBackend"(): integer
get "protected"(): boolean
get "backendFormat"(): $GLBackendFormat
get "sampleCount"(): integer
get "stencilBits"(): integer
get "backend"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLBackendRenderTarget$Type = ($GLBackendRenderTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLBackendRenderTarget_ = $GLBackendRenderTarget$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLCaps" {
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GLCaps$FormatInfo, $GLCaps$FormatInfo$Type} from "packages/icyllis/arc3d/opengl/$GLCaps$FormatInfo"
import {$PipelineDesc, $PipelineDesc$Type} from "packages/icyllis/arc3d/engine/$PipelineDesc"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$GLCapabilities, $GLCapabilities$Type} from "packages/org/lwjgl/opengl/$GLCapabilities"

export class $GLCaps extends $Caps {
static readonly "MISSING_EXTENSIONS": $List<(string)>
static readonly "INVALIDATE_BUFFER_TYPE_NULL_DATA": integer
static readonly "INVALIDATE_BUFFER_TYPE_INVALIDATE": integer

constructor(options: $ContextOptions$Type, caps: $GLCapabilities$Type)

public "toString"(): string
public "toString"(includeFormatTable: boolean): string
public "isFormatRenderable"(format: integer, sampleCount: integer): boolean
public "isFormatRenderable"(format: $BackendFormat$Type, sampleCount: integer): boolean
public "isFormatRenderable"(colorType: integer, format: $BackendFormat$Type, sampleCount: integer): boolean
public "isFormatTexturable"(format: $BackendFormat$Type): boolean
public "isFormatTexturable"(format: integer): boolean
public "hasDSASupport"(): boolean
public "canCopyTexSubImage"(srcFormat: integer, dstFormat: integer): boolean
public "canCopyImage"(srcFormat: integer, srcSampleCount: integer, dstFormat: integer, dstSampleCount: integer): boolean
public "skipErrorChecks"(): boolean
public "makeDesc"(desc: $PipelineDesc$Type, renderTarget: $GpuRenderTarget$Type, pipelineInfo: $PipelineInfo$Type): $PipelineDesc
public "onFormatCompatible"(colorType: integer, format: $BackendFormat$Type): boolean
public "getWriteSwizzle"(format: $BackendFormat$Type, colorType: integer): short
public "getFormatInfo"(format: integer): $GLCaps$FormatInfo
public "hasDebugSupport"(): boolean
public "hasSPIRVSupport"(): boolean
public "getGLSLVersion"(): integer
public "maxLabelLength"(): integer
public "getCompressedBackendFormat"(compressionType: integer): $BackendFormat
public "getRenderTargetSampleCount"(sampleCount: integer, format: integer): integer
public "getRenderTargetSampleCount"(sampleCount: integer, format: $BackendFormat$Type): integer
public "dsaElementBufferBroken"(): boolean
public "hasCopyImageSupport"(): boolean
public "getPixelsExternalFormat"(format: integer, dstColorType: integer, srcColorType: integer, write: boolean): integer
public "getPixelsExternalType"(format: integer, dstColorType: integer, srcColorType: integer): integer
public "getFormatDefaultExternalFormat"(format: integer): integer
public "getTextureInternalFormat"(format: integer): integer
public "getFormatDefaultExternalType"(format: integer): integer
public "getMaxRenderTargetSampleCount"(format: integer): integer
public "getMaxRenderTargetSampleCount"(format: $BackendFormat$Type): integer
public "isTextureStorageCompatible"(format: integer): boolean
public "getSupportedWriteColorType"(dstColorType: integer, dstFormat: $BackendFormat$Type, srcColorType: integer): long
public "getProgramBinaryFormats"(): (integer)[]
public static "getExternalTypeAlignment"(type: integer): integer
public "hasBaseInstanceSupport"(): boolean
public "maxTextureMaxAnisotropy"(): float
public "hasProgramBinarySupport"(): boolean
public "getRenderbufferInternalFormat"(format: integer): integer
public "hasBufferStorageSupport"(): boolean
public "getInvalidateBufferType"(): integer
get "gLSLVersion"(): integer
get "programBinaryFormats"(): (integer)[]
get "invalidateBufferType"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLCaps$Type = ($GLCaps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLCaps_ = $GLCaps$Type;
}}
declare module "packages/icyllis/arc3d/core/effects/$ComposedColorFilter" {
import {$ColorFilter, $ColorFilter$Type} from "packages/icyllis/arc3d/core/$ColorFilter"

export class $ComposedColorFilter extends $ColorFilter {

constructor(before: $ColorFilter$Type, after: $ColorFilter$Type)

public "filterColor4f"(col: (float)[], out: (float)[]): void
public "isAlphaUnchanged"(): boolean
public "getBefore"(): $ColorFilter
public "getAfter"(): $ColorFilter
get "alphaUnchanged"(): boolean
get "before"(): $ColorFilter
get "after"(): $ColorFilter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComposedColorFilter$Type = ($ComposedColorFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComposedColorFilter_ = $ComposedColorFilter$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$ColorFilterShader" {
import {$Shader, $Shader$Type} from "packages/icyllis/arc3d/core/$Shader"

export class $ColorFilterShader extends $Shader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorFilterShader$Type = ($ColorFilterShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorFilterShader_ = $ColorFilterShader$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$IntrinsicList" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $IntrinsicList {
static readonly "kNotIntrinsic": integer
static readonly "kRound": integer
static readonly "kRoundEven": integer
static readonly "kTrunc": integer
static readonly "kAbs": integer
static readonly "kSign": integer
static readonly "kFloor": integer
static readonly "kCeil": integer
static readonly "kFract": integer
static readonly "kRadians": integer
static readonly "kDegrees": integer
static readonly "kSin": integer
static readonly "kCos": integer
static readonly "kTan": integer
static readonly "kAsin": integer
static readonly "kAcos": integer
static readonly "kAtan": integer
static readonly "kSinh": integer
static readonly "kCosh": integer
static readonly "kTanh": integer
static readonly "kAsinh": integer
static readonly "kAcosh": integer
static readonly "kAtanh": integer
static readonly "kPow": integer
static readonly "kExp": integer
static readonly "kLog": integer
static readonly "kExp2": integer
static readonly "kLog2": integer
static readonly "kSqrt": integer
static readonly "kInverseSqrt": integer
static readonly "kMod": integer
static readonly "kModf": integer
static readonly "kMin": integer
static readonly "kMax": integer
static readonly "kClamp": integer
static readonly "kSaturate": integer
static readonly "kMix": integer
static readonly "kStep": integer
static readonly "kSmoothStep": integer
static readonly "kIsNan": integer
static readonly "kIsInf": integer
static readonly "kFloatBitsToInt": integer
static readonly "kFloatBitsToUint": integer
static readonly "kIntBitsToFloat": integer
static readonly "kUintBitsToFloat": integer
static readonly "kFma": integer
static readonly "kFrexp": integer
static readonly "kLdexp": integer
static readonly "kPackSnorm4x8": integer
static readonly "kPackUnorm4x8": integer
static readonly "kPackSnorm2x16": integer
static readonly "kPackUnorm2x16": integer
static readonly "kPackHalf2x16": integer
static readonly "kPackDouble2x32": integer
static readonly "kUnpackSnorm4x8": integer
static readonly "kUnpackUnorm4x8": integer
static readonly "kUnpackSnorm2x16": integer
static readonly "kUnpackUnorm2x16": integer
static readonly "kUnpackHalf2x16": integer
static readonly "kUnpackDouble2x32": integer
static readonly "kLength": integer
static readonly "kDistance": integer
static readonly "kDot": integer
static readonly "kCross": integer
static readonly "kNormalize": integer
static readonly "kFaceForward": integer
static readonly "kReflect": integer
static readonly "kRefract": integer
static readonly "kAny": integer
static readonly "kAll": integer
static readonly "kLogicalNot": integer
static readonly "kEqual": integer
static readonly "kNotEqual": integer
static readonly "kLessThan": integer
static readonly "kGreaterThan": integer
static readonly "kLessThanEqual": integer
static readonly "kGreaterThanEqual": integer
static readonly "kMatrixCompMult": integer
static readonly "kOuterProduct": integer
static readonly "kDeterminant": integer
static readonly "kMatrixInverse": integer
static readonly "kTranspose": integer
static readonly "kDPdx": integer
static readonly "kDPdy": integer
static readonly "kFwidth": integer
static readonly "kDPdxFine": integer
static readonly "kDPdyFine": integer
static readonly "kFwidthFine": integer
static readonly "kDPdxCoarse": integer
static readonly "kDPdyCoarse": integer
static readonly "kFwidthCoarse": integer
static readonly "kInterpolateAtCentroid": integer
static readonly "kInterpolateAtSample": integer
static readonly "kInterpolateAtOffset": integer
static readonly "kAddCarry": integer
static readonly "kAddBorrow": integer
static readonly "kUMulExtended": integer
static readonly "kIMulExtended": integer
static readonly "kBitfieldExtract": integer
static readonly "kBitfieldInsert": integer
static readonly "kBitReverse": integer
static readonly "kBitCount": integer
static readonly "kFindLSB": integer
static readonly "kFindMSB": integer
static readonly "kAtomicAdd": integer
static readonly "kAtomicMin": integer
static readonly "kAtomicMax": integer
static readonly "kAtomicAnd": integer
static readonly "kAtomicOr": integer
static readonly "kAtomicXor": integer
static readonly "kAtomicExchange": integer
static readonly "kAtomicCompSwap": integer
static readonly "kBarrier": integer
static readonly "kMemoryBarrier": integer
static readonly "kMemoryBarrierBuffer": integer
static readonly "kMemoryBarrierShared": integer
static readonly "kMemoryBarrierImage": integer
static readonly "kWorkgroupBarrier": integer
static readonly "kAnyInvocation": integer
static readonly "kAllInvocations": integer
static readonly "kAllInvocationsEqual": integer
static readonly "kTextureQuerySize": integer
static readonly "kTextureQueryLod": integer
static readonly "kTextureQueryLevels": integer
static readonly "kTextureQuerySamples": integer
static readonly "kTexture": integer
static readonly "kTextureProj": integer
static readonly "kTextureLod": integer
static readonly "kTextureOffset": integer
static readonly "kTextureFetch": integer
static readonly "kTextureFetchOffset": integer
static readonly "kTextureProjOffset": integer
static readonly "kTextureLodOffset": integer
static readonly "kTextureProjLod": integer
static readonly "kTextureProjLodOffset": integer
static readonly "kTextureGrad": integer
static readonly "kTextureGradOffset": integer
static readonly "kTextureProjGrad": integer
static readonly "kTextureProjGradOffset": integer
static readonly "kTextureGather": integer
static readonly "kTextureGatherOffset": integer
static readonly "kTextureGatherOffsets": integer
static readonly "kImageQuerySize": integer
static readonly "kImageQuerySamples": integer
static readonly "kImageLoad": integer
static readonly "kImageStore": integer
static readonly "kImageAtomicAdd": integer
static readonly "kImageAtomicMin": integer
static readonly "kImageAtomicMax": integer
static readonly "kImageAtomicAnd": integer
static readonly "kImageAtomicOr": integer
static readonly "kImageAtomicXor": integer
static readonly "kImageAtomicExchange": integer
static readonly "kImageAtomicCompSwap": integer
static readonly "kSubpassLoad": integer


public static "findIntrinsicKind"(name: string): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntrinsicList$Type = ($IntrinsicList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntrinsicList_ = $IntrinsicList$Type;
}}
declare module "packages/icyllis/arc3d/core/image/$LZWDecoder" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $LZWDecoder {

constructor()

public static "getInstance"(): $LZWDecoder
public "readString"(): integer
public "setData"(data: $ByteBuffer$Type, initCodeSize: integer): (byte)[]
get "instance"(): $LZWDecoder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LZWDecoder$Type = ($LZWDecoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LZWDecoder_ = $LZWDecoder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$BackendRenderTarget" {
import {$GLFramebufferInfo, $GLFramebufferInfo$Type} from "packages/icyllis/arc3d/opengl/$GLFramebufferInfo"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$VulkanImageInfo, $VulkanImageInfo$Type} from "packages/icyllis/arc3d/vulkan/$VulkanImageInfo"

export class $BackendRenderTarget {

constructor(width: integer, height: integer)

public "isProtected"(): boolean
public "getBackendFormat"(): $BackendFormat
public "getWidth"(): integer
public "getHeight"(): integer
public "setVkQueueFamilyIndex"(queueFamilyIndex: integer): void
public "getSampleCount"(): integer
public "getStencilBits"(): integer
public "getGLFramebufferInfo"(info: $GLFramebufferInfo$Type): boolean
public "getBackend"(): integer
public "setVkImageLayout"(layout: integer): void
public "getVkImageInfo"(info: $VulkanImageInfo$Type): boolean
get "protected"(): boolean
get "backendFormat"(): $BackendFormat
get "width"(): integer
get "height"(): integer
set "vkQueueFamilyIndex"(value: integer)
get "sampleCount"(): integer
get "stencilBits"(): integer
get "backend"(): integer
set "vkImageLayout"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BackendRenderTarget$Type = ($BackendRenderTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BackendRenderTarget_ = $BackendRenderTarget$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceContext" {
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"

export class $SurfaceContext implements $AutoCloseable {

constructor(context: $RecordingContext$Type, readView: $SurfaceView$Type, colorType: integer, alphaType: integer, colorSpace: $ColorSpace$Type)

public "getContext"(): $RecordingContext
public "close"(): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isMipmapped"(): boolean
public "getColorType"(): integer
public "getReadSwizzle"(): short
public "getCaps"(): $Caps
public "getOrigin"(): integer
public "getImageInfo"(): $ImageInfo
public "getAlphaType"(): integer
public "getReadView"(): $SurfaceView
get "context"(): $RecordingContext
get "width"(): integer
get "height"(): integer
get "mipmapped"(): boolean
get "colorType"(): integer
get "readSwizzle"(): short
get "caps"(): $Caps
get "origin"(): integer
get "imageInfo"(): $ImageInfo
get "alphaType"(): integer
get "readView"(): $SurfaceView
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceContext$Type = ($SurfaceContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceContext_ = $SurfaceContext$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$GLSLstd450" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GLSLstd450 {
static readonly "GLSLstd450Version": integer
static readonly "GLSLstd450Revision": integer
static readonly "GLSLstd450Bad": integer
static readonly "GLSLstd450Round": integer
static readonly "GLSLstd450RoundEven": integer
static readonly "GLSLstd450Trunc": integer
static readonly "GLSLstd450FAbs": integer
static readonly "GLSLstd450SAbs": integer
static readonly "GLSLstd450FSign": integer
static readonly "GLSLstd450SSign": integer
static readonly "GLSLstd450Floor": integer
static readonly "GLSLstd450Ceil": integer
static readonly "GLSLstd450Fract": integer
static readonly "GLSLstd450Radians": integer
static readonly "GLSLstd450Degrees": integer
static readonly "GLSLstd450Sin": integer
static readonly "GLSLstd450Cos": integer
static readonly "GLSLstd450Tan": integer
static readonly "GLSLstd450Asin": integer
static readonly "GLSLstd450Acos": integer
static readonly "GLSLstd450Atan": integer
static readonly "GLSLstd450Sinh": integer
static readonly "GLSLstd450Cosh": integer
static readonly "GLSLstd450Tanh": integer
static readonly "GLSLstd450Asinh": integer
static readonly "GLSLstd450Acosh": integer
static readonly "GLSLstd450Atanh": integer
static readonly "GLSLstd450Atan2": integer
static readonly "GLSLstd450Pow": integer
static readonly "GLSLstd450Exp": integer
static readonly "GLSLstd450Log": integer
static readonly "GLSLstd450Exp2": integer
static readonly "GLSLstd450Log2": integer
static readonly "GLSLstd450Sqrt": integer
static readonly "GLSLstd450InverseSqrt": integer
static readonly "GLSLstd450Determinant": integer
static readonly "GLSLstd450MatrixInverse": integer
static readonly "GLSLstd450Modf": integer
static readonly "GLSLstd450ModfStruct": integer
static readonly "GLSLstd450FMin": integer
static readonly "GLSLstd450UMin": integer
static readonly "GLSLstd450SMin": integer
static readonly "GLSLstd450FMax": integer
static readonly "GLSLstd450UMax": integer
static readonly "GLSLstd450SMax": integer
static readonly "GLSLstd450FClamp": integer
static readonly "GLSLstd450UClamp": integer
static readonly "GLSLstd450SClamp": integer
static readonly "GLSLstd450FMix": integer
static readonly "GLSLstd450IMix": integer
static readonly "GLSLstd450Step": integer
static readonly "GLSLstd450SmoothStep": integer
static readonly "GLSLstd450Fma": integer
static readonly "GLSLstd450Frexp": integer
static readonly "GLSLstd450FrexpStruct": integer
static readonly "GLSLstd450Ldexp": integer
static readonly "GLSLstd450PackSnorm4x8": integer
static readonly "GLSLstd450PackUnorm4x8": integer
static readonly "GLSLstd450PackSnorm2x16": integer
static readonly "GLSLstd450PackUnorm2x16": integer
static readonly "GLSLstd450PackHalf2x16": integer
static readonly "GLSLstd450PackDouble2x32": integer
static readonly "GLSLstd450UnpackSnorm2x16": integer
static readonly "GLSLstd450UnpackUnorm2x16": integer
static readonly "GLSLstd450UnpackHalf2x16": integer
static readonly "GLSLstd450UnpackSnorm4x8": integer
static readonly "GLSLstd450UnpackUnorm4x8": integer
static readonly "GLSLstd450UnpackDouble2x32": integer
static readonly "GLSLstd450Length": integer
static readonly "GLSLstd450Distance": integer
static readonly "GLSLstd450Cross": integer
static readonly "GLSLstd450Normalize": integer
static readonly "GLSLstd450FaceForward": integer
static readonly "GLSLstd450Reflect": integer
static readonly "GLSLstd450Refract": integer
static readonly "GLSLstd450FindILsb": integer
static readonly "GLSLstd450FindSMsb": integer
static readonly "GLSLstd450FindUMsb": integer
static readonly "GLSLstd450InterpolateAtCentroid": integer
static readonly "GLSLstd450InterpolateAtSample": integer
static readonly "GLSLstd450InterpolateAtOffset": integer
static readonly "GLSLstd450NMin": integer
static readonly "GLSLstd450NMax": integer
static readonly "GLSLstd450NClamp": integer
static readonly "GLSLstd450Count": integer


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLSLstd450$Type = ($GLSLstd450);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLSLstd450_ = $GLSLstd450$Type;
}}
declare module "packages/icyllis/arc3d/core/$ShaderUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ShaderUtils {

constructor()

public static "buildShaderErrorMessage"(shader: string, errors: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderUtils$Type = ($ShaderUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderUtils_ = $ShaderUtils$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace$Adaptation" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ColorSpace$Adaptation extends $Enum<($ColorSpace$Adaptation)> {
static readonly "BRADFORD": $ColorSpace$Adaptation
static readonly "VON_KRIES": $ColorSpace$Adaptation
static readonly "CIECAT02": $ColorSpace$Adaptation


public static "values"(): ($ColorSpace$Adaptation)[]
public static "valueOf"(name: string): $ColorSpace$Adaptation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Adaptation$Type = (("ciecat02") | ("von_kries") | ("bradford")) | ($ColorSpace$Adaptation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace$Adaptation_ = $ColorSpace$Adaptation$Type;
}}
declare module "packages/icyllis/arc3d/core/$ImageInfo" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"

export class $ImageInfo {
static readonly "COMPRESSION_NONE": integer
static readonly "COMPRESSION_ETC2_RGB8_UNORM": integer
static readonly "COMPRESSION_BC1_RGB8_UNORM": integer
static readonly "COMPRESSION_BC1_RGBA8_UNORM": integer
static readonly "COMPRESSION_COUNT": integer
static readonly "AT_UNKNOWN": integer
static readonly "AT_OPAQUE": integer
static readonly "AT_PREMUL": integer
static readonly "AT_UNPREMUL": integer
static readonly "CT_UNKNOWN": integer
static readonly "CT_RGB_565": integer
static readonly "CT_R_8": integer
static readonly "CT_RG_88": integer
static readonly "CT_RGB_888": integer
static readonly "CT_RGB_888x": integer
static readonly "CT_RGBA_8888": integer
static readonly "CT_BGRA_8888": integer
static readonly "CT_RGBA_8888_SRGB": integer
static readonly "CT_RGBA_1010102": integer
static readonly "CT_BGRA_1010102": integer
static readonly "CT_R_16": integer
static readonly "CT_R_F16": integer
static readonly "CT_RG_1616": integer
static readonly "CT_RG_F16": integer
static readonly "CT_RGBA_16161616": integer
static readonly "CT_RGBA_F16": integer
static readonly "CT_RGBA_F16_CLAMPED": integer
static readonly "CT_RGBA_F32": integer
static readonly "CT_ALPHA_8": integer
static readonly "CT_ALPHA_16": integer
static readonly "CT_ALPHA_F16": integer
static readonly "CT_GRAY_8": integer
static readonly "CT_GRAY_ALPHA_88": integer
static readonly "CT_R5G6B5_UNORM": integer
static readonly "CT_R8G8_UNORM": integer
static readonly "CT_A16_UNORM": integer
static readonly "CT_A16_FLOAT": integer
static readonly "CT_A16G16_UNORM": integer
static readonly "CT_R16G16_FLOAT": integer
static readonly "CT_R16G16B16A16_UNORM": integer
static readonly "CT_R_8xxx": integer
static readonly "CT_ALPHA_8xxx": integer
static readonly "CT_ALPHA_F32xxx": integer
static readonly "CT_GRAY_8xxx": integer
static readonly "CT_COUNT": integer

constructor(width: integer, height: integer)
constructor()
constructor(w: integer, h: integer, ct: integer, at: integer, cs: $ColorSpace$Type)
constructor(width: integer, height: integer, colorType: integer, alphaType: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public static "make"(width: integer, height: integer, ct: integer, at: integer, cs: $ColorSpace$Type): $ImageInfo
public "isOpaque"(): boolean
public "resize"(width: integer, height: integer): void
public "width"(): integer
public "isValid"(): boolean
public "height"(): integer
public "minRowBytes"(): integer
public "bytesPerPixel"(): integer
public static "bytesPerPixel"(ct: integer): integer
public static "colorTypeToString"(ct: integer): string
public static "colorTypeChannelFlags"(ct: integer): integer
public "colorType"(): integer
public static "validateAlphaType"(ct: integer, at: integer): integer
public "alphaType"(): integer
public "colorSpace"(): $ColorSpace
public "makeColorSpace"(newColorSpace: $ColorSpace$Type): $ImageInfo
public "makeWH"(newWidth: integer, newHeight: integer): $ImageInfo
public "makeAlphaType"(newAlphaType: integer): $ImageInfo
public "makeColorType"(newColorType: integer): $ImageInfo
public static "makeUnknown"(width: integer, height: integer): $ImageInfo
get "empty"(): boolean
get "opaque"(): boolean
get "valid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageInfo$Type = ($ImageInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageInfo_ = $ImageInfo$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ClipStack$Element" {
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"

export class $ClipStack$Element {


public "toString"(): string
public "shape"(): $Rect2fc
public "clipOp"(): integer
public "viewMatrix"(): $Matrixc
public "aa"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClipStack$Element$Type = ($ClipStack$Element);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClipStack$Element_ = $ClipStack$Element$Type;
}}
declare module "packages/icyllis/arc3d/core/effects/$ColorMatrix" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $ColorMatrix {

constructor(src: $ColorMatrix$Type)
constructor(...src: (float)[])
constructor()

public "equals"(o: any): boolean
public "hashCode"(): integer
public "store"(dst: (float)[], offset: integer): void
public "store"(dst: (float)[]): void
public "store"(dst: $ColorMatrix$Type): void
public "store"(dst: $ByteBuffer$Type): void
public "store"(dst: $FloatBuffer$Type): void
public "elements"(): (float)[]
public "set"(src: $ColorMatrix$Type): void
public "set"(src: (float)[]): void
public "set"(src: (float)[], offset: integer): void
public "set"(src: $ByteBuffer$Type): void
public "set"(src: $FloatBuffer$Type): void
public "setScale"(scaleR: float, scaleG: float, scaleB: float, scaleA: float): void
public "preConcat"(lhs: (float)[]): void
public "preConcat"(lhs: $ColorMatrix$Type): void
public "postConcat"(rhs: (float)[]): void
public "postConcat"(rhs: $ColorMatrix$Type): void
public "setIdentity"(): void
public "setTranslate"(transR: float, transG: float, transB: float, transA: float): void
public "setRotateR"(angle: float): void
public "setSaturation"(sat: float): void
public "setRotateG"(angle: float): void
public "setRotateB"(angle: float): void
public "setConcat"(lhs: (float)[], rhs: (float)[]): void
public "setConcat"(lhs: $ColorMatrix$Type, rhs: $ColorMatrix$Type): void
set "rotateR"(value: float)
set "saturation"(value: float)
set "rotateG"(value: float)
set "rotateB"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorMatrix$Type = ($ColorMatrix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorMatrix_ = $ColorMatrix$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLCommandBuffer" {
import {$GLVertexArray, $GLVertexArray$Type} from "packages/icyllis/arc3d/opengl/$GLVertexArray"
import {$GLTexture, $GLTexture$Type} from "packages/icyllis/arc3d/opengl/$GLTexture"
import {$GLUniformBuffer, $GLUniformBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLUniformBuffer"
import {$GLProgram, $GLProgram$Type} from "packages/icyllis/arc3d/opengl/$GLProgram"
import {$CommandBuffer, $CommandBuffer$Type} from "packages/icyllis/arc3d/engine/$CommandBuffer"
import {$GLRenderTarget, $GLRenderTarget$Type} from "packages/icyllis/arc3d/opengl/$GLRenderTarget"

export class $GLCommandBuffer extends $CommandBuffer {


public "flushRenderTarget"(target: $GLRenderTarget$Type): void
public "flushScissorTest"(enable: boolean): void
public "flushColorWrite"(enable: boolean): void
public "bindFramebuffer"(framebuffer: integer): void
public "bindVertexArray"(vertexArray: $GLVertexArray$Type): void
public "bindPipeline"(program: $GLProgram$Type, vertexArray: $GLVertexArray$Type): void
public "bindTexture"(binding: integer, texture: $GLTexture$Type, samplerState: integer, readSwizzle: short): void
public "flushViewport"(width: integer, height: integer): void
public "bindUniformBuffer"(uniformBuffer: $GLUniformBuffer$Type): void
public "flushScissorRect"(width: integer, height: integer, origin: integer, scissorLeft: integer, scissorTop: integer, scissorRight: integer, scissorBottom: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLCommandBuffer$Type = ($GLCommandBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLCommandBuffer_ = $GLCommandBuffer$Type;
}}
declare module "packages/icyllis/arc3d/core/$Blender" {
import {$BlendMode, $BlendMode$Type} from "packages/icyllis/arc3d/core/$BlendMode"

export interface $Blender {

 "asBlendMode"(): $BlendMode
}

export namespace $Blender {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Blender$Type = ($Blender);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Blender_ = $Blender$Type;
}}
declare module "packages/icyllis/arc3d/core/$Shader" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Shader {

constructor()

public "isOpaque"(): boolean
get "opaque"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Shader$Type = ($Shader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Shader_ = $Shader$Type;
}}
declare module "packages/icyllis/arc3d/core/$Rect2i" {
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $Rect2i implements $Rect2ic {
 "mLeft": integer
 "mTop": integer
 "mRight": integer
 "mBottom": integer

constructor(r: $Rect2ic$Type)
constructor(left: integer, top: integer, right: integer, bottom: integer)
constructor()

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "store"(dst: $Rect2i$Type): void
public "store"(dst: $Rect2f$Type): void
public "isEmpty"(): boolean
public "join"(left: integer, top: integer, right: integer, bottom: integer): void
public "join"(x: integer, y: integer): void
public "join"(r: $Rect2ic$Type): void
public "offset"(dx: integer, dy: integer): void
public "x"(): integer
public "contains"(left: integer, top: integer, right: integer, bottom: integer): boolean
public "contains"(x: float, y: float): boolean
public "contains"(x: integer, y: integer): boolean
public "contains"(r: $Rect2ic$Type): boolean
public "contains"(left: float, top: float, right: float, bottom: float): boolean
public "contains"(r: $Rect2fc$Type): boolean
public static "empty"(): $Rect2ic
public "set"(src: $Rect2ic$Type): void
public "set"(left: integer, top: integer, right: integer, bottom: integer): void
public "top"(): integer
public "sort"(): void
public "y"(): integer
public "left"(): integer
public "right"(): integer
public "bottom"(): integer
public static "subtract"(a: $Rect2ic$Type, b: $Rect2ic$Type, out: $Rect2i$Type): boolean
public "width"(): integer
public "adjust"(adjusts: $Rect2ic$Type): void
public "adjust"(left: integer, top: integer, right: integer, bottom: integer): void
public "intersects"(r: $Rect2ic$Type): boolean
public static "intersects"(a: $Rect2ic$Type, b: $Rect2ic$Type): boolean
public "intersects"(left: integer, top: integer, right: integer, bottom: integer): boolean
public "height"(): integer
public "isSorted"(): boolean
public "inset"(left: integer, top: integer, right: integer, bottom: integer): void
public "inset"(dx: integer, dy: integer): void
public "inset"(insets: $Rect2ic$Type): void
public "intersectNoCheck"(r: $Rect2ic$Type): void
public "intersectNoCheck"(left: integer, top: integer, right: integer, bottom: integer): void
public "joinNoCheck"(left: integer, top: integer, right: integer, bottom: integer): void
public "joinNoCheck"(r: $Rect2ic$Type): void
public "intersect"(a: $Rect2ic$Type, b: $Rect2ic$Type): boolean
public "intersect"(r: $Rect2ic$Type): boolean
public "intersect"(left: integer, top: integer, right: integer, bottom: integer): boolean
public "offsetTo"(newLeft: integer, newTop: integer): void
public "setEmpty"(): void
get "sorted"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rect2i$Type = ($Rect2i);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rect2i_ = $Rect2i$Type;
}}
declare module "packages/icyllis/arc3d/core/$Rect2f" {
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $Rect2f implements $Rect2fc {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor(r: $Rect2ic$Type)
constructor(r: $Rect2fc$Type)
constructor(left: float, top: float, right: float, bottom: float)
constructor()

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "store"(dst: $Rect2f$Type): void
public "isEmpty"(): boolean
public "join"(left: float, top: float, right: float, bottom: float): void
public "join"(r: $Rect2fc$Type): void
public "join"(r: $Rect2ic$Type): void
public "join"(x: float, y: float): void
public "offset"(dx: float, dy: float): void
public "x"(): float
public "contains"(r: $Rect2ic$Type): boolean
public "contains"(x: float, y: float): boolean
public "contains"(left: float, top: float, right: float, bottom: float): boolean
public "contains"(r: $Rect2fc$Type): boolean
public static "empty"(): $Rect2fc
public "set"(src: $Rect2ic$Type): void
public "set"(src: $Rect2fc$Type): void
public "set"(left: float, top: float, right: float, bottom: float): void
public "top"(): float
public "sort"(): void
public "y"(): float
public static "isFinite"(left: float, top: float, right: float, bottom: float): boolean
public "isFinite"(): boolean
public "left"(): float
public "right"(): float
public "round"(dst: $Rect2f$Type): void
public "round"(dst: $Rect2i$Type): void
public "bottom"(): float
public static "subtract"(a: $Rect2fc$Type, b: $Rect2fc$Type, out: $Rect2f$Type): boolean
public "width"(): float
public "adjust"(adjusts: $Rect2fc$Type): void
public "adjust"(left: float, top: float, right: float, bottom: float): void
public "adjust"(adjusts: $Rect2ic$Type): void
public static "intersects"(a: $Rect2fc$Type, b: $Rect2fc$Type): boolean
public "intersects"(r: $Rect2fc$Type): boolean
public "intersects"(r: $Rect2ic$Type): boolean
public "intersects"(left: float, top: float, right: float, bottom: float): boolean
public "height"(): float
public "isSorted"(): boolean
public "inset"(insets: $Rect2fc$Type): void
public "inset"(dx: float, dy: float): void
public "inset"(left: float, top: float, right: float, bottom: float): void
public "inset"(insets: $Rect2ic$Type): void
public "intersectNoCheck"(r: $Rect2fc$Type): void
public "intersectNoCheck"(r: $Rect2ic$Type): void
public "intersectNoCheck"(left: float, top: float, right: float, bottom: float): void
public "joinNoCheck"(left: float, top: float, right: float, bottom: float): void
public "joinNoCheck"(r: $Rect2ic$Type): void
public "joinNoCheck"(r: $Rect2fc$Type): void
public "outset"(dx: float, dy: float): void
public "setBoundsNoCheck"(pts: (float)[], pos: integer, count: integer): void
public "intersect"(left: float, top: float, right: float, bottom: float): boolean
public "intersect"(r: $Rect2fc$Type): boolean
public "intersect"(r: $Rect2ic$Type): boolean
public "intersect"(a: $Rect2fc$Type, b: $Rect2fc$Type): boolean
public "setBounds"(pts: (float)[], pos: integer, count: integer): boolean
public "offsetTo"(newLeft: float, newTop: float): void
public "roundIn"(dst: $Rect2f$Type): void
public "roundIn"(dst: $Rect2i$Type): void
public "setEmpty"(): void
public "roundOut"(dst: $Rect2i$Type): void
public "roundOut"(dst: $Rect2f$Type): void
public static "rectsOverlap"(a: $Rect2fc$Type, b: $Rect2fc$Type): boolean
public static "rectsTouchOrOverlap"(a: $Rect2fc$Type, b: $Rect2fc$Type): boolean
public "centerX"(): float
public "centerY"(): float
get "finite"(): boolean
get "sorted"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rect2f$Type = ($Rect2f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rect2f_ = $Rect2f$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ConstantFolder" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$OptionalLong, $OptionalLong$Type} from "packages/java/util/$OptionalLong"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Operator, $Operator$Type} from "packages/icyllis/arc3d/compiler/$Operator"

export class $ConstantFolder {

constructor()

public static "makeConstantValueForVariable"(pos: integer, value: $Expression$Type): $Expression
public static "getConstantInt"(value: $Expression$Type): $OptionalLong
public static "getConstantValueOrNullForVariable"(value: $Expression$Type): $Expression
public static "fold"(context: $Context$Type, pos: integer, left: $Expression$Type, op: $Operator$Type, right: $Expression$Type, resultType: $Type$Type): $Expression
public static "fold"(context: $Context$Type, pos: integer, op: $Operator$Type, base: $Expression$Type): $Expression
public static "getConstantValueForVariable"(value: $Expression$Type): $Expression
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstantFolder$Type = ($ConstantFolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstantFolder_ = $ConstantFolder$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$RegexParser" {
import {$RegexNode, $RegexNode$Type} from "packages/icyllis/arc3d/compiler/parser/$RegexNode"

export class $RegexParser {

constructor()

public "parse"(source: string): $RegexNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegexParser$Type = ($RegexParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegexParser_ = $RegexParser$Type;
}}
declare module "packages/icyllis/arc3d/core/$ImageFilter" {
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"

export class $ImageFilter {

constructor()

public "computeFastBounds"(src: $Rect2f$Type, dst: $Rect2f$Type): void
public "canComputeFastBounds"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageFilter$Type = ($ImageFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageFilter_ = $ImageFilter$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceFillContext" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$OpsTask, $OpsTask$Type} from "packages/icyllis/arc3d/engine/ops/$OpsTask"
import {$SurfaceContext, $SurfaceContext$Type} from "packages/icyllis/arc3d/engine/$SurfaceContext"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"

export class $SurfaceFillContext extends $SurfaceContext {

constructor(context: $RecordingContext$Type, readView: $SurfaceView$Type, writeView: $SurfaceView$Type, colorType: integer, alphaType: integer, colorSpace: $ColorSpace$Type)

public "getOpsTask"(): $OpsTask
public "nextOpsTask"(): $OpsTask
get "opsTask"(): $OpsTask
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceFillContext$Type = ($SurfaceFillContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceFillContext_ = $SurfaceFillContext$Type;
}}
declare module "packages/icyllis/arc3d/compiler/analysis/$SymbolUsage$VariableCounts" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SymbolUsage$VariableCounts {
 "decl": integer
 "read": integer
 "write": integer

constructor()

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SymbolUsage$VariableCounts$Type = ($SymbolUsage$VariableCounts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SymbolUsage$VariableCounts_ = $SymbolUsage$VariableCounts$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl" {
import {$VaryingHandler, $VaryingHandler$Type} from "packages/icyllis/arc3d/engine/shading/$VaryingHandler"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$VertexGeomBuilder, $VertexGeomBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$VertexGeomBuilder"
import {$UniformHandler, $UniformHandler$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler"
import {$UniformDataManager, $UniformDataManager$Type} from "packages/icyllis/arc3d/engine/$UniformDataManager"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$FPFragmentBuilder, $FPFragmentBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$FPFragmentBuilder"

export class $GeometryProcessor$ProgramImpl {

constructor()

public "setData"(arg0: $UniformDataManager$Type, arg1: $GeometryProcessor$Type): void
public "emitCode"(vertBuilder: $VertexGeomBuilder$Type, fragBuilder: $FPFragmentBuilder$Type, varyingHandler: $VaryingHandler$Type, uniformHandler: $UniformHandler$Type, shaderCaps: $ShaderCaps$Type, geomProc: $GeometryProcessor$Type, outputColor: string, outputCoverage: string, texSamplers: (integer)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeometryProcessor$ProgramImpl$Type = ($GeometryProcessor$ProgramImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeometryProcessor$ProgramImpl_ = $GeometryProcessor$ProgramImpl$Type;
}}
declare module "packages/icyllis/arc3d/core/$PathMeasure" {
import {$Matrix, $Matrix$Type} from "packages/icyllis/arc3d/core/$Matrix"
import {$Path, $Path$Type} from "packages/icyllis/arc3d/core/$Path"
import {$PathConsumer, $PathConsumer$Type} from "packages/icyllis/arc3d/core/$PathConsumer"

export class $PathMeasure {
static readonly "MATRIX_FLAG_GET_POSITION": integer
static readonly "MATRIX_FLAG_GET_TANGENT": integer
static readonly "MATRIX_FLAG_GET_POS_AND_TAN": integer

constructor(path: $Path$Type, forceClose: boolean, resScale: float)
constructor(path: $Path$Type, forceClose: boolean)
constructor()

public "reset"(): void
public "reset"(path: $Path$Type, forceClose: boolean, resScale: float): boolean
public "reset"(path: $Path$Type, forceClose: boolean): boolean
public "getMatrix"(distance: float, matrix: $Matrix$Type, flags: integer): boolean
public "getSegment"(startDistance: float, endDistance: float, dst: $PathConsumer$Type, startWithMoveTo: boolean): boolean
public "nextContour"(): boolean
public "getPosTan"(distance: float, position: (float)[], positionOff: integer, tangent: (float)[], tangentOff: integer): boolean
public "isContourClosed"(): boolean
public "hasContour"(): boolean
public "getContourLength"(): float
public "getTolerance"(): float
get "contourClosed"(): boolean
get "contourLength"(): float
get "tolerance"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathMeasure$Type = ($PathMeasure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathMeasure_ = $PathMeasure$Type;
}}
declare module "packages/icyllis/arc3d/core/$SharedPtr" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SharedPtr extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $SharedPtr {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharedPtr$Type = ($SharedPtr);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharedPtr_ = $SharedPtr$Type;
}}
declare module "packages/icyllis/arc3d/core/$PixelUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PixelUtils {
static readonly "NATIVE_BIG_ENDIAN": boolean

constructor()

public static "copyImage"(src: long, srcRowBytes: long, dst: long, dstRowBytes: long, minRowBytes: long, rows: integer): void
public static "setPixel8"(base: any, addr: long, value: byte, count: integer): void
public static "setPixel64"(base: any, addr: long, value: long, count: integer): void
public static "setPixel32"(base: any, addr: long, value: integer, count: integer): void
public static "setPixel16"(base: any, addr: long, value: short, count: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PixelUtils$Type = ($PixelUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PixelUtils_ = $PixelUtils$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$EmptyStatement" {
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"

export class $EmptyStatement extends $Statement {
 "mPosition": integer

constructor(position: integer)

public "toString"(): string
public "isEmpty"(): boolean
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$StatementKind
get "empty"(): boolean
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmptyStatement$Type = ($EmptyStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmptyStatement_ = $EmptyStatement$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$XPFragmentBuilder" {
import {$ShaderBuilder, $ShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$ShaderBuilder"

export interface $XPFragmentBuilder extends $ShaderBuilder {

 "getMangledName"(arg0: string): string
 "codeAppend"(arg0: string): void
 "codeAppendf"(arg0: string, ...arg1: (any)[]): void
 "codePrependf"(arg0: string, ...arg1: (any)[]): void
}

export namespace $XPFragmentBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XPFragmentBuilder$Type = ($XPFragmentBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XPFragmentBuilder_ = $XPFragmentBuilder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$DrawableInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DrawableInfo {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawableInfo$Type = ($DrawableInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawableInfo_ = $DrawableInfo$Type;
}}
declare module "packages/icyllis/arc3d/core/$SurfaceCharacterization" {
import {$SharedContextInfo, $SharedContextInfo$Type} from "packages/icyllis/arc3d/engine/$SharedContextInfo"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"

export class $SurfaceCharacterization {

constructor(contextInfo: $SharedContextInfo$Type, cacheMaxResourceBytes: long, imageInfo: $ImageInfo$Type, backendFormat: $BackendFormat$Type, origin: integer, sampleCount: integer, texturable: boolean, mipmapped: boolean, glWrapDefaultFramebuffer: boolean, vkSupportInputAttachment: boolean, vkSecondaryCommandBuffer: boolean, isProtected: boolean)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "isProtected"(): boolean
public "getBackendFormat"(): $BackendFormat
public "createResized"(width: integer, height: integer): $SurfaceCharacterization
public "getWidth"(): integer
public "getHeight"(): integer
public "isTexturable"(): boolean
public "getSampleCount"(): integer
public "isMipmapped"(): boolean
public "getColorType"(): integer
public "vkSecondaryCommandBuffer"(): boolean
public "glWrapDefaultFramebuffer"(): boolean
public "vkSupportInputAttachment"(): boolean
public "getContextInfo"(): $SharedContextInfo
public "getOrigin"(): integer
public "getImageInfo"(): $ImageInfo
public "isCompatible"(texture: $BackendTexture$Type): boolean
public "getCacheMaxResourceBytes"(): long
public "createBackendFormat"(colorType: integer, backendFormat: $BackendFormat$Type): $SurfaceCharacterization
public "createDefaultFramebuffer"(useDefaultFramebuffer: boolean): $SurfaceCharacterization
get "protected"(): boolean
get "backendFormat"(): $BackendFormat
get "width"(): integer
get "height"(): integer
get "texturable"(): boolean
get "sampleCount"(): integer
get "mipmapped"(): boolean
get "colorType"(): integer
get "contextInfo"(): $SharedContextInfo
get "origin"(): integer
get "imageInfo"(): $ImageInfo
get "cacheMaxResourceBytes"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceCharacterization$Type = ($SurfaceCharacterization);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceCharacterization_ = $SurfaceCharacterization$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$LValue" {
import {$Writer, $Writer$Type} from "packages/icyllis/arc3d/compiler/spirv/$Writer"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$SPIRVCodeGenerator, $SPIRVCodeGenerator$Type} from "packages/icyllis/arc3d/compiler/spirv/$SPIRVCodeGenerator"

export interface $LValue {

 "load"(arg0: $SPIRVCodeGenerator$Type, arg1: $Writer$Type): integer
 "store"(arg0: $SPIRVCodeGenerator$Type, arg1: integer, arg2: $Writer$Type): void
 "applySwizzle"(arg0: (byte)[], arg1: $Type$Type): boolean
 "getPointer"(): integer
}

export namespace $LValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LValue$Type = ($LValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LValue_ = $LValue$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$BlockStatement" {
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"

export class $BlockStatement extends $Statement {
 "mPosition": integer

constructor(position: integer, statements: $List$Type<($Statement$Type)>, scoped: boolean)

public "toString"(): string
public "isEmpty"(): boolean
public static "make"(pos: integer, statements: $List$Type<($Statement$Type)>, scoped: boolean): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getStatements"(): $List<($Statement)>
public "setStatements"(statements: $List$Type<($Statement$Type)>): void
public "getKind"(): $Node$StatementKind
public static "makeBlock"(pos: integer, statements: $List$Type<($Statement$Type)>): $BlockStatement
public "isScoped"(): boolean
public static "makeCompound"(before: $Statement$Type, after: $Statement$Type): $Statement
public "setScoped"(scoped: boolean): void
get "empty"(): boolean
get "statements"(): $List<($Statement)>
set "statements"(value: $List$Type<($Statement$Type)>)
get "kind"(): $Node$StatementKind
get "scoped"(): boolean
set "scoped"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStatement$Type = ($BlockStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStatement_ = $BlockStatement$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Expression" {
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $Expression extends $Node {
 "mPosition": integer


public "toString"(): string
public "toString"(arg0: integer): string
public "clone"(arg0: integer): $Expression
public "clone"(): $Expression
public "getType"(): $Type
public "isLiteral"(): boolean
public "getConstantValue"(i: integer): $OptionalDouble
public "isIncomplete"(context: $Context$Type): boolean
public "getCoercionCost"(other: $Type$Type): long
public "isBooleanLiteral"(): boolean
public "isIntLiteral"(): boolean
public "isConstructorCall"(): boolean
public "isFloatLiteral"(): boolean
public "getKind"(): $Node$ExpressionKind
get "type"(): $Type
get "literal"(): boolean
get "booleanLiteral"(): boolean
get "intLiteral"(): boolean
get "constructorCall"(): boolean
get "floatLiteral"(): boolean
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expression$Type = ($Expression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expression_ = $Expression$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorInt" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ColorInt extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ColorInt {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorInt$Type = ($ColorInt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorInt_ = $ColorInt$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Attachment" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuResource, $GpuResource$Type} from "packages/icyllis/arc3d/engine/$GpuResource"

export class $Attachment extends $GpuResource {


public "getBackendFormat"(): $BackendFormat
public "isFormatCompressed"(): boolean
public "getWidth"(): integer
public "getHeight"(): integer
public "getSampleCount"(): integer
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "backendFormat"(): $BackendFormat
get "formatCompressed"(): boolean
get "width"(): integer
get "height"(): integer
get "sampleCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Attachment$Type = ($Attachment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Attachment_ = $Attachment$Type;
}}
declare module "packages/icyllis/arc3d/core/$SLDataType" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SLDataType {
static readonly "kVoid": byte
static readonly "kBool": byte
static readonly "kBool2": byte
static readonly "kBool3": byte
static readonly "kBool4": byte
static readonly "kShort": byte
static readonly "kShort2": byte
static readonly "kShort3": byte
static readonly "kShort4": byte
static readonly "kUShort": byte
static readonly "kUShort2": byte
static readonly "kUShort3": byte
static readonly "kUShort4": byte
static readonly "kFloat": byte
static readonly "kFloat2": byte
static readonly "kFloat3": byte
static readonly "kFloat4": byte
static readonly "kFloat2x2": byte
static readonly "kFloat3x3": byte
static readonly "kFloat4x4": byte
static readonly "kHalf": byte
static readonly "kHalf2": byte
static readonly "kHalf3": byte
static readonly "kHalf4": byte
static readonly "kHalf2x2": byte
static readonly "kHalf3x3": byte
static readonly "kHalf4x4": byte
static readonly "kInt": byte
static readonly "kInt2": byte
static readonly "kInt3": byte
static readonly "kInt4": byte
static readonly "kUInt": byte
static readonly "kUInt2": byte
static readonly "kUInt3": byte
static readonly "kUInt4": byte
static readonly "kSampler2D": byte
static readonly "kTexture2D": byte
static readonly "kSampler": byte
static readonly "kSubpassInput": byte
static readonly "kLast": byte

constructor()

public static "typeString"(type: byte): string
public static "locations"(type: byte): integer
public static "isFloatType"(type: byte): boolean
public static "checkSLType"(type: byte): boolean
public static "isIntegralType"(type: byte): boolean
public static "isBooleanType"(type: byte): boolean
public static "canBeUniformValue"(type: byte): boolean
public static "matrixOrder"(type: byte): integer
public static "vectorDim"(type: byte): integer
public static "isMatrixType"(type: byte): boolean
public static "isCombinedSamplerType"(type: byte): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SLDataType$Type = ($SLDataType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SLDataType_ = $SLDataType$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$SymbolTable" {
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Symbol, $Symbol$Type} from "packages/icyllis/arc3d/compiler/tree/$Symbol"

export class $SymbolTable {


public "insert"<T extends $Symbol>(context: $Context$Type, symbol: T): T
public "find"(name: string): $Symbol
public "getParent"(): $SymbolTable
public "isType"(name: string): boolean
public "isBuiltin"(): boolean
public "getArrayType"(type: $Type$Type, size: integer): $Type
public "findBuiltinSymbol"(name: string): $Symbol
public "isBuiltinType"(name: string): boolean
get "parent"(): $SymbolTable
get "builtin"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SymbolTable$Type = ($SymbolTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SymbolTable_ = $SymbolTable$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$FieldAccess" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $FieldAccess extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, base: $Expression$Type, namePosition: integer, name: string): $Expression
public static "make"(position: integer, base: $Expression$Type, fieldIndex: integer, anonymousBlock: boolean): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getBase"(): $Expression
public "getFieldIndex"(): integer
public "getKind"(): $Node$ExpressionKind
public "isAnonymousBlock"(): boolean
get "base"(): $Expression
get "fieldIndex"(): integer
get "kind"(): $Node$ExpressionKind
get "anonymousBlock"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldAccess$Type = ($FieldAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldAccess_ = $FieldAccess$Type;
}}
declare module "packages/icyllis/arc3d/core/$Quad" {
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"

export class $Quad {
static readonly "kAxisAligned": integer
static readonly "kRectilinear": integer
static readonly "kGeneral": integer
static readonly "kPerspective": integer

constructor(rect: $Rect2fc$Type)
constructor(rect: $Rect2fc$Type, m: $Matrixc$Type)

public "x"(i: integer): float
public "w"(i: integer): float
public "x1"(): float
public "y"(i: integer): float
public "x2"(): float
public "y1"(): float
public "y2"(): float
public "point"(i: integer, p: (float)[]): void
public "x0"(): float
public "y0"(): float
public "w3"(): float
public "x3"(): float
public "w0"(): float
public "w1"(): float
public "w2"(): float
public "y3"(): float
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
declare module "packages/icyllis/arc3d/compiler/$Context" {
import {$BuiltinTypes, $BuiltinTypes$Type} from "packages/icyllis/arc3d/compiler/$BuiltinTypes"
import {$SymbolTable, $SymbolTable$Type} from "packages/icyllis/arc3d/compiler/$SymbolTable"
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$ShaderKind, $ShaderKind$Type} from "packages/icyllis/arc3d/compiler/$ShaderKind"
import {$ErrorHandler, $ErrorHandler$Type} from "packages/icyllis/arc3d/compiler/$ErrorHandler"
import {$CompileOptions, $CompileOptions$Type} from "packages/icyllis/arc3d/compiler/$CompileOptions"

export class $Context {


public "isActive"(): boolean
public "error"(start: integer, end: integer, msg: string): void
public "error"(position: integer, msg: string): void
public "isBuiltin"(): boolean
public "warning"(start: integer, end: integer, msg: string): void
public "warning"(position: integer, msg: string): void
public "getOptions"(): $CompileOptions
public "getSymbolTable"(): $SymbolTable
public "leaveScope"(): void
public "enterScope"(): void
public "convertIdentifier"(position: integer, name: string): $Expression
public "setErrorHandler"(errorHandler: $ErrorHandler$Type): void
public "getErrorHandler"(): $ErrorHandler
public "getTypes"(): $BuiltinTypes
public "isModule"(): boolean
public "getKind"(): $ShaderKind
get "active"(): boolean
get "builtin"(): boolean
get "options"(): $CompileOptions
get "symbolTable"(): $SymbolTable
set "errorHandler"(value: $ErrorHandler$Type)
get "errorHandler"(): $ErrorHandler
get "types"(): $BuiltinTypes
get "module"(): boolean
get "kind"(): $ShaderKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Context$Type = ($Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Context_ = $Context$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VkBackendRenderTarget" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$VulkanImageInfo, $VulkanImageInfo$Type} from "packages/icyllis/arc3d/vulkan/$VulkanImageInfo"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"

export class $VkBackendRenderTarget extends $BackendRenderTarget {

constructor(width: integer, height: integer, info: $VulkanImageInfo$Type)

public "isProtected"(): boolean
public "getBackendFormat"(): $BackendFormat
public "setVkQueueFamilyIndex"(queueFamilyIndex: integer): void
public "getSampleCount"(): integer
public "getStencilBits"(): integer
public "getBackend"(): integer
public "setVkImageLayout"(layout: integer): void
public "getVkImageInfo"(info: $VulkanImageInfo$Type): boolean
get "protected"(): boolean
get "backendFormat"(): $BackendFormat
set "vkQueueFamilyIndex"(value: integer)
get "sampleCount"(): integer
get "stencilBits"(): integer
get "backend"(): integer
set "vkImageLayout"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VkBackendRenderTarget$Type = ($VkBackendRenderTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VkBackendRenderTarget_ = $VkBackendRenderTarget$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$UniformHandler" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"
import {$UniformHandler$UniformInfo, $UniformHandler$UniformInfo$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler$UniformInfo"
import {$Processor, $Processor$Type} from "packages/icyllis/arc3d/engine/$Processor"

export class $UniformHandler {
static readonly "NO_MANGLE_PREFIX": string
static readonly "PROJECTION_NAME": string
static readonly "Std140Layout": boolean
static readonly "Std430Layout": boolean
static readonly "MAIN_DESC_SET": integer
static readonly "SAMPLER_DESC_SET": integer
static readonly "INPUT_DESC_SET": integer
static readonly "UNIFORM_BINDING": integer
static readonly "UNIFORM_BLOCK_NAME": string
static readonly "INPUT_BINDING": integer


public static "getSize"(type: byte, layout: boolean): integer
public "uniform"(arg0: integer): $UniformHandler$UniformInfo
public "liftUniformToVertexShader"(owner: $Processor$Type, rawName: string): $ShaderVar
public "addUniformArray"(owner: $Processor$Type, visibility: integer, type: byte, name: string, arrayCount: integer): integer
public "getUniformMapping"(owner: $Processor$Type, rawName: string): $ShaderVar
public "addUniform"(owner: $Processor$Type, visibility: integer, type: byte, name: string): integer
public "getUniformVariable"(arg0: integer): $ShaderVar
public "getUniformName"(handle: integer): string
public "numUniforms"(): integer
public static "getAlignedStride"(type: byte, arrayCount: integer, layout: boolean): integer
public static "getAlignmentMask"(type: byte, nonArray: boolean, layout: boolean): integer
public static "getAlignedOffset"(offset: integer, type: byte, arrayCount: integer, layout: boolean): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UniformHandler$Type = ($UniformHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UniformHandler_ = $UniformHandler$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$DFA" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DFA {
static readonly "INVALID": integer
readonly "mCharMappings": (integer)[]
readonly "mTransitions": ((integer)[])[]
readonly "mAccepts": (integer)[]

constructor(charMappings: (integer)[], transitions: ((integer)[])[], accepts: (integer)[])

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DFA$Type = ($DFA);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DFA_ = $DFA$Type;
}}
declare module "packages/icyllis/arc3d/core/$Matrix" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $Matrix implements $Matrixc, $Cloneable {

constructor(scaleX: float, shearY: float, persp0: float, shearX: float, scaleY: float, persp1: float, transX: float, transY: float, persp2: float)
constructor(m: $Matrixc$Type)
constructor()

public "equals"(o: any): boolean
public static "equals"(a: $Matrixc$Type, b: $Matrixc$Type): boolean
public "toString"(): string
public "hashCode"(): integer
public "store"(dst: $Matrix$Type): void
public "store"(a: (float)[], offset: integer): void
public "store"(a: (float)[]): void
public "store"(a: $FloatBuffer$Type): void
public "store"(a: $ByteBuffer$Type): void
public "store"(p: long): void
public static "identity"(): $Matrixc
public "set"(a: (float)[], offset: integer): void
public "set"(scaleX: float, shearY: float, persp0: float, shearX: float, scaleY: float, persp1: float, transX: float, transY: float, persp2: float): void
public "set"(a: (float)[]): void
public "set"(a: $ByteBuffer$Type): void
public "set"(p: long): void
public "set"(a: $FloatBuffer$Type): void
public "set"(m: $Matrixc$Type): void
public "trace"(): float
public "getType"(): integer
public "isIdentity"(): boolean
public "isFinite"(): boolean
public "setScale"(sx: float, sy: float, px: float, py: float): void
public "setScale"(sx: float, sy: float): void
public "normalizePerspective"(): void
public "m12"(): float
public "m14"(): float
public "m11"(): float
public "invert"(dest: $Matrix$Type): boolean
public "invert"(): boolean
public "getScaleX"(): float
public "getScaleY"(): float
public "mapRect"(src: $Rect2fc$Type, dst: $Rect2f$Type): boolean
public "mapRect"(left: float, top: float, right: float, bottom: float, dst: $Rect2i$Type): void
public "preTranslate"(dx: float, dy: float): void
public "preConcat"(lhs: $Matrixc$Type): void
public "postConcat"(rhs: $Matrixc$Type): void
public "setIdentity"(): void
public "postTranslate"(dx: float, dy: float): void
public "determinant"(): float
public "getShearX"(): float
public "getShearY"(): float
public "m24"(): float
public "m41"(): float
public "m21"(): float
public "m42"(): float
public "m44"(): float
public "m22"(): float
public "getTranslateX"(): float
public "isApproxEqual"(m: $Matrix$Type): boolean
public "isSimilarity"(): boolean
public "getPerspY"(): float
public "setSinCos"(sin: float, cos: float, px: float, py: float): void
public "setSinCos"(sin: float, cos: float): void
public "storeAligned"(a: $ByteBuffer$Type): void
public "storeAligned"(p: long): void
public "storeAligned"(a: $FloatBuffer$Type): void
public "setScaleTranslate"(sx: float, sy: float, tx: float, ty: float): void
public "isTranslate"(): boolean
public "getMinScale"(): float
public "getMaxScale"(): float
public "getPerspX"(): float
public "mapPoints"(src: (float)[], srcPos: integer, dst: (float)[], dstPos: integer, count: integer): void
public "setRotate"(angle: float, px: float, py: float): void
public "setRotate"(angle: float): void
public "getTranslateY"(): float
public "preservesRightAngles"(): boolean
public "setTranslate"(dx: float, dy: float): void
public "setShear"(sx: float, sy: float): void
public "setShear"(sx: float, sy: float, px: float, py: float): void
public "postShear"(sx: float, sy: float): void
public "postRotate"(angle: float): void
public "preScale"(sx: float, sy: float): void
public "postScale"(sx: float, sy: float): void
public "preRotate"(angle: float): void
public "preShear"(sx: float, sy: float): void
public "hasPerspective"(): boolean
public "isAxisAligned"(): boolean
public "mapRectOut"(left: float, top: float, right: float, bottom: float, dst: $Rect2i$Type): void
public "isScaleTranslate"(): boolean
public "mapRect"(r: $Rect2ic$Type, out: $Rect2i$Type): void
public "mapRect"(r: $Rect2fc$Type, out: $Rect2i$Type): void
public "mapRect"(r: $Rect2i$Type): void
public "mapRect"(rect: $Rect2f$Type): boolean
public "mapPoint"(p: (float)[]): void
public "mapPoints"(pts: (float)[]): void
public "mapPoints"(pts: (float)[], count: integer): void
public "mapPoints"(pts: (float)[], pos: integer, count: integer): void
public "mapPoints"(src: (float)[], dst: (float)[], count: integer): void
public "mapRectOut"(r: $Rect2ic$Type, dst: $Rect2i$Type): void
public "mapRectOut"(r: $Rect2i$Type): void
public "mapRectOut"(r: $Rect2fc$Type, dst: $Rect2i$Type): void
get "type"(): integer
get "finite"(): boolean
get "scaleX"(): float
get "scaleY"(): float
get "shearX"(): float
get "shearY"(): float
get "translateX"(): float
get "similarity"(): boolean
get "perspY"(): float
get "translate"(): boolean
get "minScale"(): float
get "maxScale"(): float
get "perspX"(): float
set "rotate"(value: float)
get "translateY"(): float
get "axisAligned"(): boolean
get "scaleTranslate"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix$Type = ($Matrix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix_ = $Matrix$Type;
}}
declare module "packages/icyllis/arc3d/engine/$IUniqueKey" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IUniqueKey {

}

export namespace $IUniqueKey {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IUniqueKey$Type = ($IUniqueKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IUniqueKey_ = $IUniqueKey$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$BreakStatement" {
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"

export class $BreakStatement extends $Statement {
 "mPosition": integer


public "toString"(): string
public static "make"(pos: integer): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$StatementKind
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BreakStatement$Type = ($BreakStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BreakStatement_ = $BreakStatement$Type;
}}
declare module "packages/icyllis/arc3d/core/$Geometry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Geometry {
static readonly "MAX_CONIC_TO_QUADS_LEVEL": integer


public static "findUnitQuadRoots"(A: float, B: float, C: float, roots: (float)[], off: integer): integer
public static "findQuadRoots"(A: float, B: float, C: float, roots: (float)[], off: integer): integer
public static "deduplicate_pairs"(arr: (float)[], off: integer, count: integer): integer
public static "findUnitCubicRoots"(A: float, B: float, C: float, D: float, roots: (float)[], off: integer): integer
public static "findCubicCusp"(src: (float)[], off: integer): float
public static "findCubicCusp"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float): float
public static "findCubicMaxCurvature"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, roots: (float)[], off: integer): integer
public static "findCubicMaxCurvature"(src: (float)[], srcOff: integer, dst: (float)[], dstOff: integer): integer
public static "findCubicInflectionPoints"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, roots: (float)[], off: integer): integer
public static "findCubicInflectionPoints"(src: (float)[], srcOff: integer, dst: (float)[], dstOff: integer): integer
public static "findQuadMaxCurvature"(src: (float)[], off: integer): float
public static "findQuadMaxCurvature"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float): float
public static "eval_cubic_derivative"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, t: float, dst: (float)[], off: integer): void
public static "computeConicToQuadsLevel"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, w1: float, tol: float): integer
public static "computeConicToQuads"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, w1: float, dst: (float)[], off: integer, level: integer): integer
public static "evalQuadAt"(src: (float)[], srcOff: integer, t: float, pos: (float)[], posOff: integer, tangent: (float)[], tangentOff: integer): void
public static "evalQuadAt"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, t: float, dst: (float)[], off: integer): void
public static "evalQuadAt"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, t: float, pos: (float)[], posOff: integer, tangent: (float)[], tangentOff: integer): void
public static "evalQuadAt"(src: (float)[], srcOff: integer, dst: (float)[], dstOff: integer, t: float): void
public static "evalCubicAt"(src: (float)[], srcOff: integer, dst: (float)[], dstOff: integer, t: float): void
public static "evalCubicAt"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, t: float, dst: (float)[], off: integer): void
public static "evalCubicAt"(src: (float)[], srcOff: integer, t: float, pos: (float)[], posOff: integer, tangent: (float)[], tangentOff: integer): void
public static "evalCubicAt"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, t: float, pos: (float)[], posOff: integer, tangent: (float)[], tangentOff: integer): void
public static "chopCubicAt"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, t: float, dst: (float)[], off: integer): void
public static "chopCubicAt"(src: (float)[], srcOff: integer, dst: (float)[], dstOff: integer, t: float): void
public static "chopQuadAt"(src: (float)[], srcOff: integer, dst: (float)[], dstOff: integer, t: float): void
public static "chopQuadAt"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, t: float, dst: (float)[], off: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Geometry$Type = ($Geometry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Geometry_ = $Geometry$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceProxy$LazyCallbackResult" {
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $SurfaceProxy$LazyCallbackResult {
 "mSurface": $IGpuSurface
 "mSyncTargetKey": boolean
 "mReleaseCallback": boolean

constructor()
constructor(surface: $IGpuSurface$Type)
constructor(surface: $IGpuSurface$Type, syncTargetKey: boolean, releaseCallback: boolean)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceProxy$LazyCallbackResult$Type = ($SurfaceProxy$LazyCallbackResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceProxy$LazyCallbackResult_ = $SurfaceProxy$LazyCallbackResult$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$LexerGenerator" {
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$DFA, $DFA$Type} from "packages/icyllis/arc3d/compiler/parser/$DFA"

export class $LexerGenerator {
static readonly "LEXICON": string
static readonly "NUM_BITS": integer
static readonly "NUM_VALUES": integer
static readonly "DATA_PER_BYTE": integer
static readonly "DATA_PER_BYTE_SHIFT": integer

constructor()

public static "main"(args: (string)[]): void
public static "process"(pw: $PrintWriter$Type): $DFA
public static "writeTransitionTable"(pw: $PrintWriter$Type, dfa: $DFA$Type, states: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LexerGenerator$Type = ($LexerGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LexerGenerator_ = $LexerGenerator$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Caps" {
import {$Caps$BlendEquationSupport, $Caps$BlendEquationSupport$Type} from "packages/icyllis/arc3d/engine/$Caps$BlendEquationSupport"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$PipelineDesc, $PipelineDesc$Type} from "packages/icyllis/arc3d/engine/$PipelineDesc"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$DriverBugWorkarounds, $DriverBugWorkarounds$Type} from "packages/icyllis/arc3d/engine/$DriverBugWorkarounds"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"

export class $Caps {

constructor(options: $ContextOptions$Type)

public "twoSidedStencilRefsAndMasksMustMatch"(): boolean
public "discardStencilValuesAfterRenderPass"(): boolean
public "dynamicStateArrayGeometryProcessorTextureSupport"(): boolean
public "requiresManualFBBarrierAfterTessellatedStencilDraw"(): boolean
public "advancedCoherentBlendEquationSupport"(): boolean
public "isFormatRenderable"(arg0: $BackendFormat$Type, arg1: integer): boolean
public "isFormatRenderable"(arg0: integer, arg1: $BackendFormat$Type, arg2: integer): boolean
public "isFormatTexturable"(arg0: $BackendFormat$Type): boolean
public "shaderCaps"(): $ShaderCaps
public "isFormatCompatible"(colorType: integer, format: $BackendFormat$Type): boolean
public "maxTextureSize"(): integer
public "gpuTracingSupport"(): boolean
public "fenceSyncSupport"(): boolean
public "mipmapSupport"(): boolean
public "reducedShaderMode"(): boolean
public "semaphoreSupport"(): boolean
public "wireframeSupport"(): boolean
public "getReadSwizzle"(format: $BackendFormat$Type, colorType: integer): short
public "makeDesc"(arg0: $PipelineDesc$Type, arg1: $GpuRenderTarget$Type, arg2: $PipelineInfo$Type): $PipelineDesc
public "getWriteSwizzle"(arg0: $BackendFormat$Type, arg1: integer): short
public "validateSurfaceParams"(width: integer, height: integer, format: $BackendFormat$Type, sampleCount: integer, surfaceFlags: integer): boolean
public "getCompressedBackendFormat"(arg0: integer): $BackendFormat
public "getRenderTargetSampleCount"(arg0: integer, arg1: $BackendFormat$Type): integer
public "maxRenderTargetSize"(): integer
public "getDefaultBackendFormat"(colorType: integer, renderable: boolean): $BackendFormat
public "hasAnisotropySupport"(): boolean
public "npotTextureTileSupport"(): boolean
public "oversizedStencilSupport"(): boolean
public "getMaxRenderTargetSampleCount"(arg0: $BackendFormat$Type): integer
public "advancedBlendEquationSupport"(): boolean
public "textureBarrierSupport"(): boolean
public "usePrimitiveRestart"(): boolean
public "disableTessellationPathRenderer"(): boolean
public "blendEquationSupport"(): $Caps$BlendEquationSupport
public "mustClearUploadedBufferData"(): boolean
public "shouldInitializeTextures"(): boolean
public "mustSyncGpuDuringDiscard"(): boolean
public "reuseScratchBuffers"(): boolean
public "reuseScratchTextures"(): boolean
public "getSupportedReadColorType"(srcColorType: integer, srcFormat: $BackendFormat$Type, dstColorType: integer): long
public "maxPushConstantsSize"(): integer
public "writePixelsRowBytesSupport"(): boolean
public "transferFromSurfaceToBufferSupport"(): boolean
public "preferClientSideDynamicBuffers"(): boolean
public "maxVertexAttributes"(): integer
public "readPixelsRowBytesSupport"(): boolean
public "halfFloatVertexAttributeSupport"(): boolean
public "performStencilClearsAsDraws"(): boolean
public "avoidWritePixelsFastPath"(): boolean
public "transferPixelsToRowBytesSupport"(): boolean
public "clampToBorderSupport"(): boolean
public "conservativeRasterSupport"(): boolean
public "msaaResolvesAutomatically"(): boolean
public "preferFullscreenClears"(): boolean
public "preferDiscardableMSAAAttachment"(): boolean
public "drawInstancedSupport"(): boolean
public "transferFromBufferToTextureSupport"(): boolean
public "crossContextTextureSupport"(): boolean
public "performColorClearsAsDraws"(): boolean
public "avoidLargeIndexBufferDraws"(): boolean
public "preferVRAMUseOverFlushes"(): boolean
public "validateAttachmentParams"(width: integer, height: integer, format: $BackendFormat$Type, sampleCount: integer): boolean
public "sampleLocationsSupport"(): boolean
public "nativeDrawIndexedIndirectIsBroken"(): boolean
public "transferBufferAlignment"(): integer
public "maxPreferredRenderTargetSize"(): integer
public "getInternalMultisampleCount"(format: $BackendFormat$Type): integer
public "shouldCollapseSrcOverToSrcWhenAble"(): boolean
public "getSupportedWriteColorType"(arg0: integer, arg1: $BackendFormat$Type, arg2: integer): long
public "performPartialClearsAsDraws"(): boolean
public "avoidStencilBuffers"(): boolean
public "supportsTextureBarrier"(): boolean
public "workarounds"(): $DriverBugWorkarounds
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Caps$Type = ($Caps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Caps_ = $Caps$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$VertexGeomBuilder" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$ShaderBuilder, $ShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$ShaderBuilder"

export interface $VertexGeomBuilder extends $ShaderBuilder {

 "emitAttributes"(arg0: $GeometryProcessor$Type): void
 "emitNormalizedPosition"(arg0: $ShaderVar$Type): void
 "getMangledName"(arg0: string): string
 "codeAppend"(arg0: string): void
 "codeAppendf"(arg0: string, ...arg1: (any)[]): void
 "codePrependf"(arg0: string, ...arg1: (any)[]): void
}

export namespace $VertexGeomBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexGeomBuilder$Type = ($VertexGeomBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexGeomBuilder_ = $VertexGeomBuilder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ResourceProvider" {
import {$IScratchKey, $IScratchKey$Type} from "packages/icyllis/arc3d/engine/$IScratchKey"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuResource, $GpuResource$Type} from "packages/icyllis/arc3d/engine/$GpuResource"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$IUniqueKey, $IUniqueKey$Type} from "packages/icyllis/arc3d/engine/$IUniqueKey"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $ResourceProvider {


public "createBuffer"(size: integer, usage: integer): $GpuBuffer
public "findByUniqueKey"<T extends $GpuResource>(key: $IUniqueKey$Type): T
public "wrapBackendRenderTarget"(backendRenderTarget: $BackendRenderTarget$Type): $GpuRenderTarget
public "wrapRenderableBackendTexture"(texture: $BackendTexture$Type, sampleCount: integer, ownership: boolean): $GpuTexture
public "assignUniqueKeyToResource"(key: $IUniqueKey$Type, resource: $GpuResource$Type): void
public "findAndRefScratchTexture"(width: integer, height: integer, format: $BackendFormat$Type, sampleCount: integer, surfaceFlags: integer, label: string): $GpuTexture
public "findAndRefScratchTexture"(key: $IScratchKey$Type, label: string): $GpuTexture
public "createTexture"(width: integer, height: integer, format: $BackendFormat$Type, sampleCount: integer, surfaceFlags: integer, label: string): $GpuTexture
public "createTexture"(width: integer, height: integer, format: $BackendFormat$Type, sampleCount: integer, surfaceFlags: integer, dstColorType: integer, srcColorType: integer, rowBytes: integer, pixels: long, label: string): $GpuTexture
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceProvider$Type = ($ResourceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceProvider_ = $ResourceProvider$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ClipStack" {
import {$ClipStack$Element, $ClipStack$Element$Type} from "packages/icyllis/arc3d/engine/$ClipStack$Element"
import {$ClipResult, $ClipResult$Type} from "packages/icyllis/arc3d/engine/$ClipResult"
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$Clip, $Clip$Type} from "packages/icyllis/arc3d/engine/$Clip"
import {$ClipStack$Geometry, $ClipStack$Geometry$Type} from "packages/icyllis/arc3d/engine/$ClipStack$Geometry"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$SurfaceDrawContext, $SurfaceDrawContext$Type} from "packages/icyllis/arc3d/engine/$SurfaceDrawContext"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $ClipStack extends $Clip {
static readonly "OP_DIFFERENCE": integer
static readonly "OP_INTERSECT": integer
static readonly "STATE_EMPTY": integer
static readonly "STATE_WIDE_OPEN": integer
static readonly "STATE_DEVICE_RECT": integer
static readonly "STATE_COMPLEX": integer
static readonly "CLIP_GEOMETRY_EMPTY": integer
static readonly "CLIP_GEOMETRY_A_ONLY": integer
static readonly "CLIP_GEOMETRY_B_ONLY": integer
static readonly "CLIP_GEOMETRY_BOTH": integer
static readonly "CLIPPED": integer
static readonly "NOT_CLIPPED": integer
static readonly "CLIPPED_OUT": integer
static readonly "kBoundsTolerance": float
static readonly "kHalfPixelRoundingTolerance": float

constructor(deviceBounds: $Rect2ic$Type, msaa: boolean)

public "apply"(sdc: $SurfaceDrawContext$Type, aa: boolean, out: $ClipResult$Type, bounds: $Rect2f$Type): integer
public "elements"(): $Collection<($ClipStack$Element)>
public "save"(): void
public "restore"(): void
public "clipRect"(viewMatrix: $Matrixc$Type, left: float, top: float, right: float, bottom: float, clipOp: integer): void
public "clipRect"(viewMatrix: $Matrixc$Type, localRect: $Rect2fc$Type, clipOp: integer): void
public "getConservativeBounds"(out: $Rect2i$Type): void
public static "getClipGeometry"(A: $ClipStack$Geometry$Type, B: $ClipStack$Geometry$Type): integer
public "currentClipState"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClipStack$Type = ($ClipStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClipStack_ = $ClipStack$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanSharedImageInfo" {
import {$VulkanImageInfo, $VulkanImageInfo$Type} from "packages/icyllis/arc3d/vulkan/$VulkanImageInfo"

export class $VulkanSharedImageInfo {

constructor(info: $VulkanImageInfo$Type)
constructor(layout: integer, queueFamilyIndex: integer)

public "setQueueFamilyIndex"(queueFamilyIndex: integer): void
public "getQueueFamilyIndex"(): integer
public "getImageLayout"(): integer
public "setImageLayout"(layout: integer): void
set "queueFamilyIndex"(value: integer)
get "queueFamilyIndex"(): integer
get "imageLayout"(): integer
set "imageLayout"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanSharedImageInfo$Type = ($VulkanSharedImageInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanSharedImageInfo_ = $VulkanSharedImageInfo$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Statement" {
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"

export class $Statement extends $Node {
 "mPosition": integer


public "isEmpty"(): boolean
public "getKind"(): $Node$StatementKind
get "empty"(): boolean
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Statement$Type = ($Statement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Statement_ = $Statement$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$FunctionPrototype" {
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$FunctionDecl, $FunctionDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDecl"
import {$Node$ElementKind, $Node$ElementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ElementKind"
import {$TopLevelElement, $TopLevelElement$Type} from "packages/icyllis/arc3d/compiler/tree/$TopLevelElement"

export class $FunctionPrototype extends $TopLevelElement {
 "mPosition": integer

constructor(position: integer, functionDecl: $FunctionDecl$Type, builtin: boolean)

public "toString"(): string
public "accept"(visitor: $TreeVisitor$Type): boolean
public "isBuiltin"(): boolean
public "getFunctionDecl"(): $FunctionDecl
public "getKind"(): $Node$ElementKind
get "builtin"(): boolean
get "functionDecl"(): $FunctionDecl
get "kind"(): $Node$ElementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionPrototype$Type = ($FunctionPrototype);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionPrototype_ = $FunctionPrototype$Type;
}}
declare module "packages/icyllis/arc3d/core/$ClipOp" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClipOp {
static readonly "CLIP_OP_DIFFERENCE": byte
static readonly "CLIP_OP_INTERSECT": byte


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClipOp$Type = ($ClipOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClipOp_ = $ClipOp$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuResource$UniqueID" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GpuResource$UniqueID {

constructor()

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuResource$UniqueID$Type = ($GpuResource$UniqueID);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuResource$UniqueID_ = $GpuResource$UniqueID$Type;
}}
declare module "packages/icyllis/arc3d/engine/$OpFlushState" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$GraphicsPipelineState, $GraphicsPipelineState$Type} from "packages/icyllis/arc3d/engine/$GraphicsPipelineState"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$OpsRenderPass, $OpsRenderPass$Type} from "packages/icyllis/arc3d/engine/$OpsRenderPass"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Mesh, $Mesh$Type} from "packages/icyllis/arc3d/engine/$Mesh"
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"
import {$MeshDrawTarget, $MeshDrawTarget$Type} from "packages/icyllis/arc3d/engine/$MeshDrawTarget"

export class $OpFlushState implements $MeshDrawTarget {

constructor(device: $GpuDevice$Type, resourceProvider: $ResourceProvider$Type)

public "reset"(): void
public "getOpsRenderPass"(): $OpsRenderPass
public "findOrCreateGraphicsPipelineState"(pipelineInfo: $PipelineInfo$Type): $GraphicsPipelineState
public "getDevice"(): $GpuDevice
public "beginOpsRenderPass"(writeView: $SurfaceView$Type, contentBounds: $Rect2i$Type, colorOps: byte, stencilOps: byte, clearColor: (float)[], sampledTextures: $Set$Type<($TextureProxy$Type)>, pipelineFlags: integer): $OpsRenderPass
public "makeIndexSpace"(mesh: $Mesh$Type): long
public "makeIndexWriter"(mesh: $Mesh$Type): $ByteBuffer
public "makeVertexSpace"(mesh: $Mesh$Type): long
public "makeInstanceSpace"(mesh: $Mesh$Type): long
public "makeInstanceWriter"(mesh: $Mesh$Type): $ByteBuffer
public "makeVertexWriter"(mesh: $Mesh$Type): $ByteBuffer
get "opsRenderPass"(): $OpsRenderPass
get "device"(): $GpuDevice
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpFlushState$Type = ($OpFlushState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpFlushState_ = $OpFlushState$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TextureProxy" {
import {$IScratchKey, $IScratchKey$Type} from "packages/icyllis/arc3d/engine/$IScratchKey"
import {$SurfaceProxy$LazyInstantiateCallback, $SurfaceProxy$LazyInstantiateCallback$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy$LazyInstantiateCallback"
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"
import {$IUniqueKey, $IUniqueKey$Type} from "packages/icyllis/arc3d/engine/$IUniqueKey"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $TextureProxy extends $SurfaceProxy implements $IScratchKey {

constructor(texture: $GpuTexture$Type, surfaceFlags: integer)
constructor(format: $BackendFormat$Type, width: integer, height: integer, surfaceFlags: integer, callback: $SurfaceProxy$LazyInstantiateCallback$Type)
constructor(format: $BackendFormat$Type, width: integer, height: integer, surfaceFlags: integer)

public "equals"(o: any): boolean
public "hashCode"(): integer
public "clear"(): void
public "makeUserExact"(allocatedCaseOnly: boolean): void
public "getResolveRect"(): $Rect2ic
public "isBackingWrapped"(): boolean
public "isPromiseProxy"(): boolean
public "getBackingUniqueID"(): any
public "getGpuSurface"(): $IGpuSurface
public "isUserMipmapped"(): boolean
public "setLazyDimension"(width: integer, height: integer): void
public "setIsPromiseProxy"(): void
public "instantiate"(resourceProvider: $ResourceProvider$Type): boolean
public "getBackingWidth"(): integer
public "getMemorySize"(): long
public "setMipmapsDirty"(dirty: boolean): void
public "getSampleCount"(): integer
public "isMipmapped"(): boolean
public "isMipmapsDirty"(): boolean
public "asTexture"(): $TextureProxy
public "getUniqueKey"(): $IUniqueKey
public "doLazyInstantiation"(resourceProvider: $ResourceProvider$Type): boolean
public "shouldSkipAllocator"(): boolean
public "getGpuTexture"(): $GpuTexture
public "isInstantiated"(): boolean
public "needsResolve"(): boolean
public "getBackingHeight"(): integer
public "setResolveRect"(left: integer, top: integer, right: integer, bottom: integer): void
public "isLazy"(): boolean
public "hasRestrictedSampling"(): boolean
public "ref"(): void
public static "getApproxSize"(size: integer): integer
public "unref"(): void
get "resolveRect"(): $Rect2ic
get "backingWrapped"(): boolean
get "promiseProxy"(): boolean
get "backingUniqueID"(): any
get "gpuSurface"(): $IGpuSurface
get "userMipmapped"(): boolean
get "backingWidth"(): integer
get "memorySize"(): long
set "mipmapsDirty"(value: boolean)
get "sampleCount"(): integer
get "mipmapped"(): boolean
get "mipmapsDirty"(): boolean
get "uniqueKey"(): $IUniqueKey
get "gpuTexture"(): $GpuTexture
get "instantiated"(): boolean
get "backingHeight"(): integer
get "lazy"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureProxy$Type = ($TextureProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureProxy_ = $TextureProxy$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Key" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Key {

}

export namespace $Key {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Key$Type = ($Key);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Key_ = $Key$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ErrorHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ErrorHandler {

constructor()

public "reset"(): void
public "error"(pos: integer, msg: string): void
public "error"(start: integer, end: integer, msg: string): void
public "warning"(start: integer, end: integer, msg: string): void
public "warning"(pos: integer, msg: string): void
public "getSource"(): (character)[]
public "setSource"(source: (character)[]): void
public "errorCount"(): integer
public "warningCount"(): integer
get "source"(): (character)[]
set "source"(value: (character)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ErrorHandler$Type = ($ErrorHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ErrorHandler_ = $ErrorHandler$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ShaderCompiler" {
import {$TranslationUnit, $TranslationUnit$Type} from "packages/icyllis/arc3d/compiler/tree/$TranslationUnit"
import {$ModuleUnit, $ModuleUnit$Type} from "packages/icyllis/arc3d/compiler/$ModuleUnit"
import {$ShaderKind, $ShaderKind$Type} from "packages/icyllis/arc3d/compiler/$ShaderKind"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ErrorHandler, $ErrorHandler$Type} from "packages/icyllis/arc3d/compiler/$ErrorHandler"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$CompileOptions, $CompileOptions$Type} from "packages/icyllis/arc3d/compiler/$CompileOptions"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/compiler/$ShaderCaps"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $ShaderCompiler {
static readonly "INVALID_TAG": string
static readonly "POISON_TAG": string

constructor()

public static "toChars"(s: charseq): (character)[]
public static "toChars"(elements: (charseq)[], start: integer, end: integer): (character)[]
public static "toChars"(elements: $List$Type<(charseq)>): (character)[]
public static "toChars"(...elements: (charseq)[]): (character)[]
public "getContext"(): $Context
public "parse"(source: charseq, kind: $ShaderKind$Type, options: $CompileOptions$Type, parent: $ModuleUnit$Type): $TranslationUnit
public "parse"(source: (character)[], kind: $ShaderKind$Type, options: $CompileOptions$Type, parent: $ModuleUnit$Type): $TranslationUnit
public "getErrorMessage"(): string
public "getErrorMessage"(showCount: boolean): string
public "errorCount"(): integer
public "startContext"(kind: $ShaderKind$Type, options: $CompileOptions$Type, parent: $ModuleUnit$Type, isBuiltin: boolean, isModule: boolean, source: (character)[]): void
public "warningCount"(): integer
public "generateSPIRV"(translationUnit: $TranslationUnit$Type, shaderCaps: $ShaderCaps$Type): $ByteBuffer
public "compileIntoSPIRV"(source: (character)[], kind: $ShaderKind$Type, shaderCaps: $ShaderCaps$Type, options: $CompileOptions$Type, parent: $ModuleUnit$Type): $ByteBuffer
public "compileIntoSPIRV"(source: charseq, kind: $ShaderKind$Type, shaderCaps: $ShaderCaps$Type, options: $CompileOptions$Type, parent: $ModuleUnit$Type): $ByteBuffer
public "endContext"(): void
public "parseModule"(source: charseq, kind: $ShaderKind$Type, parent: $ModuleUnit$Type, builtin: boolean): $ModuleUnit
public "parseModule"(source: (character)[], kind: $ShaderKind$Type, parent: $ModuleUnit$Type, builtin: boolean): $ModuleUnit
public "getErrorHandler"(): $ErrorHandler
get "context"(): $Context
get "errorMessage"(): string
get "errorHandler"(): $ErrorHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderCompiler$Type = ($ShaderCompiler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderCompiler_ = $ShaderCompiler$Type;
}}
declare module "packages/icyllis/arc3d/mock/$MockBackendFormat" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"

export class $MockBackendFormat extends $BackendFormat {

constructor(colorType: integer, compressionType: integer, isStencilFormat: boolean)

public static "make"(colorType: integer, compressionType: integer, isStencilFormat: boolean): $MockBackendFormat
public static "make"(colorType: integer, compressionType: integer): $MockBackendFormat
public "getStencilBits"(): integer
public "getCompressionType"(): integer
public "getBytesPerBlock"(): integer
public "makeInternal"(): $BackendFormat
public "getChannelFlags"(): integer
public "isSRGB"(): boolean
public "getFormatKey"(): integer
public "isExternal"(): boolean
public "getBackend"(): integer
get "stencilBits"(): integer
get "compressionType"(): integer
get "bytesPerBlock"(): integer
get "channelFlags"(): integer
get "sRGB"(): boolean
get "formatKey"(): integer
get "external"(): boolean
get "backend"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MockBackendFormat$Type = ($MockBackendFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MockBackendFormat_ = $MockBackendFormat$Type;
}}
declare module "packages/icyllis/arc3d/core/$Color" {
import {$BlendMode, $BlendMode$Type} from "packages/icyllis/arc3d/core/$BlendMode"

export class $Color {
static readonly "TRANSPARENT": integer
static readonly "BLACK": integer
static readonly "DKGRAY": integer
static readonly "GRAY": integer
static readonly "LTGRAY": integer
static readonly "WHITE": integer
static readonly "RED": integer
static readonly "GREEN": integer
static readonly "BLUE": integer
static readonly "YELLOW": integer
static readonly "CYAN": integer
static readonly "MAGENTA": integer
static readonly "COLOR_CHANNEL_R": integer
static readonly "COLOR_CHANNEL_G": integer
static readonly "COLOR_CHANNEL_B": integer
static readonly "COLOR_CHANNEL_A": integer
static readonly "COLOR_CHANNEL_FLAG_RED": integer
static readonly "COLOR_CHANNEL_FLAG_GREEN": integer
static readonly "COLOR_CHANNEL_FLAG_BLUE": integer
static readonly "COLOR_CHANNEL_FLAG_ALPHA": integer
static readonly "COLOR_CHANNEL_FLAG_GRAY": integer
static readonly "COLOR_CHANNEL_FLAGS_RG": integer
static readonly "COLOR_CHANNEL_FLAGS_RGB": integer
static readonly "COLOR_CHANNEL_FLAGS_RGBA": integer
 "mR": float
 "mG": float
 "mB": float
 "mA": float

constructor()
constructor(color: integer)
constructor(r: float, g: float, b: float, a: float)
constructor(color: $Color$Type)

public "toArgb"(): integer
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "blend"(mode: $BlendMode$Type, src: integer, dst: integer): integer
public "set"(r: float, g: float, b: float, a: float): void
public "set"(color: integer): void
public "set"(color: $Color$Type): void
public "isOpaque"(): boolean
public "red"(): float
public static "red"(color: integer): integer
public "red"(red: float): void
public static "luminance"(r: float, g: float, b: float): float
public static "luminance"(col: (float)[]): float
public "alpha"(): float
public static "alpha"(color: integer): integer
public "alpha"(alpha: float): void
public static "alpha"(color: integer, alpha: integer): integer
public static "blue"(color: integer): integer
public "blue"(blue: float): void
public "blue"(): float
public static "green"(color: integer): integer
public "green"(): float
public "green"(green: float): void
public static "load_and_premul"(col: integer): (float)[]
public static "GammaToLinear"(x: float): float
public static "GammaToLinear"(col: (float)[]): void
public static "LinearToGamma"(x: float): float
public static "LinearToGamma"(col: (float)[]): void
public static "rgb"(red: integer, green: integer, blue: integer): integer
public static "rgb"(red: float, green: float, blue: float): integer
public static "argb"(alpha: integer, red: integer, green: integer, blue: integer): integer
public static "argb"(alpha: float, red: float, green: float, blue: float): integer
public static "lightness"(lum: float): float
public static "parseColor"(s: string): integer
public static "RGBToHSV"(r: integer, g: integer, b: integer, hsv: (float)[]): void
public static "RGBToHSV"(color: integer, hsv: (float)[]): void
public static "HSVToColor"(hsv: (float)[]): integer
public static "HSVToColor"(h: float, s: float, v: float): integer
get "opaque"(): boolean
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
declare module "packages/icyllis/arc3d/compiler/tree/$FunctionCall" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$FunctionDecl, $FunctionDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDecl"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $FunctionCall extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, pos: integer, identifier: $Expression$Type, arg3: $List$Type<($Expression$Type)>): $Expression
public static "convert"(context: $Context$Type, pos: integer, arg2: $FunctionDecl$Type, arg3: $List$Type<($Expression$Type)>): $Expression
public static "make"(pos: integer, returnType: $Type$Type, arg2: $FunctionDecl$Type, arg3: $List$Type<($Expression$Type)>): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getFunction"(): $FunctionDecl
public "getArguments"(): ($Expression)[]
public "getKind"(): $Node$ExpressionKind
get "function"(): $FunctionDecl
get "arguments"(): ($Expression)[]
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionCall$Type = ($FunctionCall);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionCall_ = $FunctionCall$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLProgram" {
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"

export class $GLProgram extends $ManagedResource {

constructor(device: $GLDevice$Type, program: integer)

public "discard"(): void
public "getProgram"(): integer
get "program"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLProgram$Type = ($GLProgram);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLProgram_ = $GLProgram$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Node" {
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"

export class $Node {
 "mPosition": integer


public "toString"(): string
public "accept"(arg0: $TreeVisitor$Type): boolean
public "getEndOffset"(): integer
public "getStartOffset"(): integer
get "endOffset"(): integer
get "startOffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$Type = ($Node);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node_ = $Node$Type;
}}
declare module "packages/icyllis/arc3d/core/$Device" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"
import {$Paint, $Paint$Type} from "packages/icyllis/arc3d/core/$Paint"
import {$MatrixProvider, $MatrixProvider$Type} from "packages/icyllis/arc3d/core/$MatrixProvider"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"
import {$SurfaceDrawContext, $SurfaceDrawContext$Type} from "packages/icyllis/arc3d/engine/$SurfaceDrawContext"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $Device extends $RefCnt implements $MatrixProvider {

constructor(info: $ImageInfo$Type)

public "getRecordingContext"(): $RecordingContext
public "getSurfaceDrawContext"(): $SurfaceDrawContext
public "getRelativeTransform"(device: $Device$Type, dest: $Matrix4$Type): void
public "isPixelAlignedToGlobal"(): boolean
public "getBounds"(bounds: $Rect2i$Type): void
public "bounds"(): $Rect2ic
public "save"(): void
public "width"(): integer
public "height"(): integer
public "drawRect"(arg0: $Rect2f$Type, arg1: $Paint$Type): void
public "getGlobalToDevice"(): $Matrix4
public "clipIsAA"(): boolean
public "replaceClip"(rect: $Rect2i$Type): void
public "setGlobalTransform"(globalTransform: $Matrix4$Type): void
public "getLocalToDevice"(): $Matrix4
public "getDeviceToGlobal"(): $Matrix4
public "clipIsWideOpen"(): boolean
public "setLocalToDevice"(localToDevice: $Matrix4$Type): void
public "restoreLocal"(localToDevice: $Matrix4$Type): void
public "getGlobalBounds"(bounds: $Rect2i$Type): void
public "restore"(globalTransform: $Matrix4$Type): void
public "imageInfo"(): $ImageInfo
public "clipRect"(rect: $Rect2f$Type, clipOp: integer, doAA: boolean): void
public "drawPaint"(arg0: $Paint$Type): void
public "getClipBounds"(bounds: $Rect2i$Type): void
get "recordingContext"(): $RecordingContext
get "surfaceDrawContext"(): $SurfaceDrawContext
get "pixelAlignedToGlobal"(): boolean
get "globalToDevice"(): $Matrix4
set "globalTransform"(value: $Matrix4$Type)
get "localToDevice"(): $Matrix4
get "deviceToGlobal"(): $Matrix4
set "localToDevice"(value: $Matrix4$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Device$Type = ($Device);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Device_ = $Device$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLTextureParameters" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GLTextureParameters {
 "baseMipmapLevel": integer
 "maxMipmapLevel": integer
 "swizzleR": integer
 "swizzleG": integer
 "swizzleB": integer
 "swizzleA": integer

constructor()

public "toString"(): string
public "invalidate"(): void
public "getSwizzle"(i: integer): integer
public "setSwizzle"(i: integer, swiz: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLTextureParameters$Type = ($GLTextureParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLTextureParameters_ = $GLTextureParameters$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PipelineInfo" {
import {$TransferProcessor, $TransferProcessor$Type} from "packages/icyllis/arc3d/engine/$TransferProcessor"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$UserStencilSettings, $UserStencilSettings$Type} from "packages/icyllis/arc3d/engine/$UserStencilSettings"
import {$FragmentProcessor, $FragmentProcessor$Type} from "packages/icyllis/arc3d/engine/$FragmentProcessor"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"

export class $PipelineInfo {
static readonly "kNone_Flag": integer
static readonly "kConservativeRaster_Flag": integer
static readonly "kWireframe_Flag": integer
static readonly "kSnapToPixels_Flag": integer
static readonly "kHasScissorClip_Flag": integer
static readonly "kHasStencilClip_Flag": integer
static readonly "kRenderPassBlendBarrier_Flag": integer

constructor(writeView: $SurfaceView$Type, geomProc: $GeometryProcessor$Type, xferProc: $TransferProcessor$Type, colorFragProc: $FragmentProcessor$Type, coverageFragProc: $FragmentProcessor$Type, userStencilSettings: $UserStencilSettings$Type, pipelineFlags: integer)

public "primitiveType"(): byte
public "origin"(): integer
public "isStencilEnabled"(): boolean
public "backendFormat"(): $BackendFormat
public "writeSwizzle"(): short
public "geomProc"(): $GeometryProcessor
public "sampleCount"(): integer
public "hasScissorClip"(): boolean
public "hasStencilClip"(): boolean
public "needsBlendBarrier"(): boolean
public "userStencilSettings"(): $UserStencilSettings
get "stencilEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PipelineInfo$Type = ($PipelineInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PipelineInfo_ = $PipelineInfo$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLCore" {
import {$PipelineStateCache$Stats, $PipelineStateCache$Stats$Type} from "packages/icyllis/arc3d/engine/$PipelineStateCache$Stats"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GL45C, $GL45C$Type} from "packages/org/lwjgl/opengl/$GL45C"

export class $GLCore extends $GL45C {
static readonly "INVALID_ID": integer
static readonly "DEFAULT_FRAMEBUFFER": integer
static readonly "DEFAULT_VERTEX_ARRAY": integer
static readonly "DEFAULT_TEXTURE": integer
static readonly "GL_VENDOR_OTHER": integer
static readonly "GL_VENDOR_NVIDIA": integer
static readonly "GL_VENDOR_ATI": integer
static readonly "GL_VENDOR_INTEL": integer
static readonly "GL_VENDOR_QUALCOMM": integer
static readonly "GL_DRIVER_OTHER": integer
static readonly "GL_DRIVER_NVIDIA": integer
static readonly "GL_DRIVER_AMD": integer
static readonly "GL_DRIVER_INTEL": integer
static readonly "GL_DRIVER_FREEDRENO": integer
static readonly "GL_DRIVER_MESA": integer
static readonly "LAST_COLOR_FORMAT_INDEX": integer
static readonly "LAST_FORMAT_INDEX": integer
static readonly "GL_NEGATIVE_ONE_TO_ONE": integer
static readonly "GL_ZERO_TO_ONE": integer
static readonly "GL_CLIP_ORIGIN": integer
static readonly "GL_CLIP_DEPTH_MODE": integer
static readonly "GL_QUERY_WAIT_INVERTED": integer
static readonly "GL_QUERY_NO_WAIT_INVERTED": integer
static readonly "GL_QUERY_BY_REGION_WAIT_INVERTED": integer
static readonly "GL_QUERY_BY_REGION_NO_WAIT_INVERTED": integer
static readonly "GL_MAX_CULL_DISTANCES": integer
static readonly "GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES": integer
static readonly "GL_TEXTURE_TARGET": integer
static readonly "GL_QUERY_TARGET": integer
static readonly "GL_CONTEXT_RELEASE_BEHAVIOR": integer
static readonly "GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH": integer
static readonly "GL_GUILTY_CONTEXT_RESET": integer
static readonly "GL_INNOCENT_CONTEXT_RESET": integer
static readonly "GL_UNKNOWN_CONTEXT_RESET": integer
static readonly "GL_RESET_NOTIFICATION_STRATEGY": integer
static readonly "GL_LOSE_CONTEXT_ON_RESET": integer
static readonly "GL_NO_RESET_NOTIFICATION": integer
static readonly "GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT": integer
static readonly "GL_CONTEXT_LOST": integer
static readonly "GL_MAX_VERTEX_ATTRIB_STRIDE": integer
static readonly "GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED": integer
static readonly "GL_TEXTURE_BUFFER_BINDING": integer
static readonly "GL_MAP_PERSISTENT_BIT": integer
static readonly "GL_MAP_COHERENT_BIT": integer
static readonly "GL_DYNAMIC_STORAGE_BIT": integer
static readonly "GL_CLIENT_STORAGE_BIT": integer
static readonly "GL_BUFFER_IMMUTABLE_STORAGE": integer
static readonly "GL_BUFFER_STORAGE_FLAGS": integer
static readonly "GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT": integer
static readonly "GL_CLEAR_TEXTURE": integer
static readonly "GL_LOCATION_COMPONENT": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_INDEX": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE": integer
static readonly "GL_QUERY_RESULT_NO_WAIT": integer
static readonly "GL_QUERY_BUFFER": integer
static readonly "GL_QUERY_BUFFER_BINDING": integer
static readonly "GL_QUERY_BUFFER_BARRIER_BIT": integer
static readonly "GL_MIRROR_CLAMP_TO_EDGE": integer
static readonly "GL_NUM_SHADING_LANGUAGE_VERSIONS": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_LONG": integer
static readonly "GL_COMPRESSED_RGB8_ETC2": integer
static readonly "GL_COMPRESSED_SRGB8_ETC2": integer
static readonly "GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2": integer
static readonly "GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2": integer
static readonly "GL_COMPRESSED_RGBA8_ETC2_EAC": integer
static readonly "GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC": integer
static readonly "GL_COMPRESSED_R11_EAC": integer
static readonly "GL_COMPRESSED_SIGNED_R11_EAC": integer
static readonly "GL_COMPRESSED_RG11_EAC": integer
static readonly "GL_COMPRESSED_SIGNED_RG11_EAC": integer
static readonly "GL_PRIMITIVE_RESTART_FIXED_INDEX": integer
static readonly "GL_ANY_SAMPLES_PASSED_CONSERVATIVE": integer
static readonly "GL_MAX_ELEMENT_INDEX": integer
static readonly "GL_TEXTURE_IMMUTABLE_LEVELS": integer
static readonly "GL_COMPUTE_SHADER": integer
static readonly "GL_MAX_COMPUTE_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_MAX_COMPUTE_IMAGE_UNIFORMS": integer
static readonly "GL_MAX_COMPUTE_SHARED_MEMORY_SIZE": integer
static readonly "GL_MAX_COMPUTE_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_COMPUTE_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS": integer
static readonly "GL_MAX_COMPUTE_WORK_GROUP_COUNT": integer
static readonly "GL_MAX_COMPUTE_WORK_GROUP_SIZE": integer
static readonly "GL_COMPUTE_WORK_GROUP_SIZE": integer
static readonly "GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER": integer
static readonly "GL_DISPATCH_INDIRECT_BUFFER": integer
static readonly "GL_DISPATCH_INDIRECT_BUFFER_BINDING": integer
static readonly "GL_COMPUTE_SHADER_BIT": integer
static readonly "GL_DEBUG_OUTPUT": integer
static readonly "GL_DEBUG_OUTPUT_SYNCHRONOUS": integer
static readonly "GL_CONTEXT_FLAG_DEBUG_BIT": integer
static readonly "GL_MAX_DEBUG_MESSAGE_LENGTH": integer
static readonly "GL_MAX_DEBUG_LOGGED_MESSAGES": integer
static readonly "GL_DEBUG_LOGGED_MESSAGES": integer
static readonly "GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH": integer
static readonly "GL_MAX_DEBUG_GROUP_STACK_DEPTH": integer
static readonly "GL_DEBUG_GROUP_STACK_DEPTH": integer
static readonly "GL_MAX_LABEL_LENGTH": integer
static readonly "GL_DEBUG_CALLBACK_FUNCTION": integer
static readonly "GL_DEBUG_CALLBACK_USER_PARAM": integer
static readonly "GL_DEBUG_SOURCE_API": integer
static readonly "GL_DEBUG_SOURCE_WINDOW_SYSTEM": integer
static readonly "GL_DEBUG_SOURCE_SHADER_COMPILER": integer
static readonly "GL_DEBUG_SOURCE_THIRD_PARTY": integer
static readonly "GL_DEBUG_SOURCE_APPLICATION": integer
static readonly "GL_DEBUG_SOURCE_OTHER": integer
static readonly "GL_DEBUG_TYPE_ERROR": integer
static readonly "GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR": integer
static readonly "GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR": integer
static readonly "GL_DEBUG_TYPE_PORTABILITY": integer
static readonly "GL_DEBUG_TYPE_PERFORMANCE": integer
static readonly "GL_DEBUG_TYPE_OTHER": integer
static readonly "GL_DEBUG_TYPE_MARKER": integer
static readonly "GL_DEBUG_TYPE_PUSH_GROUP": integer
static readonly "GL_DEBUG_TYPE_POP_GROUP": integer
static readonly "GL_DEBUG_SEVERITY_HIGH": integer
static readonly "GL_DEBUG_SEVERITY_MEDIUM": integer
static readonly "GL_DEBUG_SEVERITY_LOW": integer
static readonly "GL_DEBUG_SEVERITY_NOTIFICATION": integer
static readonly "GL_BUFFER": integer
static readonly "GL_SHADER": integer
static readonly "GL_PROGRAM": integer
static readonly "GL_QUERY": integer
static readonly "GL_PROGRAM_PIPELINE": integer
static readonly "GL_SAMPLER": integer
static readonly "GL_MAX_UNIFORM_LOCATIONS": integer
static readonly "GL_FRAMEBUFFER_DEFAULT_WIDTH": integer
static readonly "GL_FRAMEBUFFER_DEFAULT_HEIGHT": integer
static readonly "GL_FRAMEBUFFER_DEFAULT_LAYERS": integer
static readonly "GL_FRAMEBUFFER_DEFAULT_SAMPLES": integer
static readonly "GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS": integer
static readonly "GL_MAX_FRAMEBUFFER_WIDTH": integer
static readonly "GL_MAX_FRAMEBUFFER_HEIGHT": integer
static readonly "GL_MAX_FRAMEBUFFER_LAYERS": integer
static readonly "GL_MAX_FRAMEBUFFER_SAMPLES": integer
static readonly "GL_INTERNALFORMAT_SUPPORTED": integer
static readonly "GL_INTERNALFORMAT_PREFERRED": integer
static readonly "GL_INTERNALFORMAT_RED_SIZE": integer
static readonly "GL_INTERNALFORMAT_GREEN_SIZE": integer
static readonly "GL_INTERNALFORMAT_BLUE_SIZE": integer
static readonly "GL_INTERNALFORMAT_ALPHA_SIZE": integer
static readonly "GL_INTERNALFORMAT_DEPTH_SIZE": integer
static readonly "GL_INTERNALFORMAT_STENCIL_SIZE": integer
static readonly "GL_INTERNALFORMAT_SHARED_SIZE": integer
static readonly "GL_INTERNALFORMAT_RED_TYPE": integer
static readonly "GL_INTERNALFORMAT_GREEN_TYPE": integer
static readonly "GL_INTERNALFORMAT_BLUE_TYPE": integer
static readonly "GL_INTERNALFORMAT_ALPHA_TYPE": integer
static readonly "GL_INTERNALFORMAT_DEPTH_TYPE": integer
static readonly "GL_INTERNALFORMAT_STENCIL_TYPE": integer
static readonly "GL_MAX_WIDTH": integer
static readonly "GL_MAX_HEIGHT": integer
static readonly "GL_MAX_DEPTH": integer
static readonly "GL_MAX_LAYERS": integer
static readonly "GL_MAX_COMBINED_DIMENSIONS": integer
static readonly "GL_COLOR_COMPONENTS": integer
static readonly "GL_DEPTH_COMPONENTS": integer
static readonly "GL_STENCIL_COMPONENTS": integer
static readonly "GL_COLOR_RENDERABLE": integer
static readonly "GL_DEPTH_RENDERABLE": integer
static readonly "GL_STENCIL_RENDERABLE": integer
static readonly "GL_FRAMEBUFFER_RENDERABLE": integer
static readonly "GL_FRAMEBUFFER_RENDERABLE_LAYERED": integer
static readonly "GL_FRAMEBUFFER_BLEND": integer
static readonly "GL_READ_PIXELS": integer
static readonly "GL_READ_PIXELS_FORMAT": integer
static readonly "GL_READ_PIXELS_TYPE": integer
static readonly "GL_TEXTURE_IMAGE_FORMAT": integer
static readonly "GL_TEXTURE_IMAGE_TYPE": integer
static readonly "GL_GET_TEXTURE_IMAGE_FORMAT": integer
static readonly "GL_GET_TEXTURE_IMAGE_TYPE": integer
static readonly "GL_MIPMAP": integer
static readonly "GL_MANUAL_GENERATE_MIPMAP": integer
static readonly "GL_AUTO_GENERATE_MIPMAP": integer
static readonly "GL_COLOR_ENCODING": integer
static readonly "GL_SRGB_READ": integer
static readonly "GL_SRGB_WRITE": integer
static readonly "GL_FILTER": integer
static readonly "GL_VERTEX_TEXTURE": integer
static readonly "GL_TESS_CONTROL_TEXTURE": integer
static readonly "GL_TESS_EVALUATION_TEXTURE": integer
static readonly "GL_GEOMETRY_TEXTURE": integer
static readonly "GL_FRAGMENT_TEXTURE": integer
static readonly "GL_COMPUTE_TEXTURE": integer
static readonly "GL_TEXTURE_SHADOW": integer
static readonly "GL_TEXTURE_GATHER": integer
static readonly "GL_TEXTURE_GATHER_SHADOW": integer
static readonly "GL_SHADER_IMAGE_LOAD": integer
static readonly "GL_SHADER_IMAGE_STORE": integer
static readonly "GL_SHADER_IMAGE_ATOMIC": integer
static readonly "GL_IMAGE_TEXEL_SIZE": integer
static readonly "GL_IMAGE_COMPATIBILITY_CLASS": integer
static readonly "GL_IMAGE_PIXEL_FORMAT": integer
static readonly "GL_IMAGE_PIXEL_TYPE": integer
static readonly "GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST": integer
static readonly "GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST": integer
static readonly "GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE": integer
static readonly "GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE": integer
static readonly "GL_TEXTURE_COMPRESSED_BLOCK_WIDTH": integer
static readonly "GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT": integer
static readonly "GL_TEXTURE_COMPRESSED_BLOCK_SIZE": integer
static readonly "GL_CLEAR_BUFFER": integer
static readonly "GL_TEXTURE_VIEW": integer
static readonly "GL_VIEW_COMPATIBILITY_CLASS": integer
static readonly "GL_FULL_SUPPORT": integer
static readonly "GL_CAVEAT_SUPPORT": integer
static readonly "GL_IMAGE_CLASS_4_X_32": integer
static readonly "GL_IMAGE_CLASS_2_X_32": integer
static readonly "GL_IMAGE_CLASS_1_X_32": integer
static readonly "GL_IMAGE_CLASS_4_X_16": integer
static readonly "GL_IMAGE_CLASS_2_X_16": integer
static readonly "GL_IMAGE_CLASS_1_X_16": integer
static readonly "GL_IMAGE_CLASS_4_X_8": integer
static readonly "GL_IMAGE_CLASS_2_X_8": integer
static readonly "GL_IMAGE_CLASS_1_X_8": integer
static readonly "GL_IMAGE_CLASS_11_11_10": integer
static readonly "GL_IMAGE_CLASS_10_10_10_2": integer
static readonly "GL_VIEW_CLASS_128_BITS": integer
static readonly "GL_VIEW_CLASS_96_BITS": integer
static readonly "GL_VIEW_CLASS_64_BITS": integer
static readonly "GL_VIEW_CLASS_48_BITS": integer
static readonly "GL_VIEW_CLASS_32_BITS": integer
static readonly "GL_VIEW_CLASS_24_BITS": integer
static readonly "GL_VIEW_CLASS_16_BITS": integer
static readonly "GL_VIEW_CLASS_8_BITS": integer
static readonly "GL_VIEW_CLASS_S3TC_DXT1_RGB": integer
static readonly "GL_VIEW_CLASS_S3TC_DXT1_RGBA": integer
static readonly "GL_VIEW_CLASS_S3TC_DXT3_RGBA": integer
static readonly "GL_VIEW_CLASS_S3TC_DXT5_RGBA": integer
static readonly "GL_VIEW_CLASS_RGTC1_RED": integer
static readonly "GL_VIEW_CLASS_RGTC2_RG": integer
static readonly "GL_VIEW_CLASS_BPTC_UNORM": integer
static readonly "GL_VIEW_CLASS_BPTC_FLOAT": integer
static readonly "GL_UNIFORM": integer
static readonly "GL_UNIFORM_BLOCK": integer
static readonly "GL_PROGRAM_INPUT": integer
static readonly "GL_PROGRAM_OUTPUT": integer
static readonly "GL_BUFFER_VARIABLE": integer
static readonly "GL_SHADER_STORAGE_BLOCK": integer
static readonly "GL_VERTEX_SUBROUTINE": integer
static readonly "GL_TESS_CONTROL_SUBROUTINE": integer
static readonly "GL_TESS_EVALUATION_SUBROUTINE": integer
static readonly "GL_GEOMETRY_SUBROUTINE": integer
static readonly "GL_FRAGMENT_SUBROUTINE": integer
static readonly "GL_COMPUTE_SUBROUTINE": integer
static readonly "GL_VERTEX_SUBROUTINE_UNIFORM": integer
static readonly "GL_TESS_CONTROL_SUBROUTINE_UNIFORM": integer
static readonly "GL_TESS_EVALUATION_SUBROUTINE_UNIFORM": integer
static readonly "GL_GEOMETRY_SUBROUTINE_UNIFORM": integer
static readonly "GL_FRAGMENT_SUBROUTINE_UNIFORM": integer
static readonly "GL_COMPUTE_SUBROUTINE_UNIFORM": integer
static readonly "GL_TRANSFORM_FEEDBACK_VARYING": integer
static readonly "GL_ACTIVE_RESOURCES": integer
static readonly "GL_MAX_NAME_LENGTH": integer
static readonly "GL_MAX_NUM_ACTIVE_VARIABLES": integer
static readonly "GL_MAX_NUM_COMPATIBLE_SUBROUTINES": integer
static readonly "GL_NAME_LENGTH": integer
static readonly "GL_TYPE": integer
static readonly "GL_ARRAY_SIZE": integer
static readonly "GL_OFFSET": integer
static readonly "GL_BLOCK_INDEX": integer
static readonly "GL_ARRAY_STRIDE": integer
static readonly "GL_MATRIX_STRIDE": integer
static readonly "GL_IS_ROW_MAJOR": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_INDEX": integer
static readonly "GL_BUFFER_BINDING": integer
static readonly "GL_BUFFER_DATA_SIZE": integer
static readonly "GL_NUM_ACTIVE_VARIABLES": integer
static readonly "GL_ACTIVE_VARIABLES": integer
static readonly "GL_REFERENCED_BY_VERTEX_SHADER": integer
static readonly "GL_REFERENCED_BY_TESS_CONTROL_SHADER": integer
static readonly "GL_REFERENCED_BY_TESS_EVALUATION_SHADER": integer
static readonly "GL_REFERENCED_BY_GEOMETRY_SHADER": integer
static readonly "GL_REFERENCED_BY_FRAGMENT_SHADER": integer
static readonly "GL_REFERENCED_BY_COMPUTE_SHADER": integer
static readonly "GL_TOP_LEVEL_ARRAY_SIZE": integer
static readonly "GL_TOP_LEVEL_ARRAY_STRIDE": integer
static readonly "GL_LOCATION": integer
static readonly "GL_LOCATION_INDEX": integer
static readonly "GL_IS_PER_PATCH": integer
static readonly "GL_SHADER_STORAGE_BUFFER": integer
static readonly "GL_SHADER_STORAGE_BUFFER_BINDING": integer
static readonly "GL_SHADER_STORAGE_BUFFER_START": integer
static readonly "GL_SHADER_STORAGE_BUFFER_SIZE": integer
static readonly "GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS": integer
static readonly "GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS": integer
static readonly "GL_MAX_SHADER_STORAGE_BLOCK_SIZE": integer
static readonly "GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT": integer
static readonly "GL_SHADER_STORAGE_BARRIER_BIT": integer
static readonly "GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES": integer
static readonly "GL_DEPTH_STENCIL_TEXTURE_MODE": integer
static readonly "GL_TEXTURE_BUFFER_OFFSET": integer
static readonly "GL_TEXTURE_BUFFER_SIZE": integer
static readonly "GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT": integer
static readonly "GL_TEXTURE_VIEW_MIN_LEVEL": integer
static readonly "GL_TEXTURE_VIEW_NUM_LEVELS": integer
static readonly "GL_TEXTURE_VIEW_MIN_LAYER": integer
static readonly "GL_TEXTURE_VIEW_NUM_LAYERS": integer
static readonly "GL_VERTEX_ATTRIB_BINDING": integer
static readonly "GL_VERTEX_ATTRIB_RELATIVE_OFFSET": integer
static readonly "GL_VERTEX_BINDING_DIVISOR": integer
static readonly "GL_VERTEX_BINDING_OFFSET": integer
static readonly "GL_VERTEX_BINDING_STRIDE": integer
static readonly "GL_VERTEX_BINDING_BUFFER": integer
static readonly "GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET": integer
static readonly "GL_MAX_VERTEX_ATTRIB_BINDINGS": integer
static readonly "GL_COPY_READ_BUFFER_BINDING": integer
static readonly "GL_COPY_WRITE_BUFFER_BINDING": integer
static readonly "GL_TRANSFORM_FEEDBACK_ACTIVE": integer
static readonly "GL_TRANSFORM_FEEDBACK_PAUSED": integer
static readonly "GL_COMPRESSED_RGBA_BPTC_UNORM": integer
static readonly "GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM": integer
static readonly "GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT": integer
static readonly "GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT": integer
static readonly "GL_UNPACK_COMPRESSED_BLOCK_WIDTH": integer
static readonly "GL_UNPACK_COMPRESSED_BLOCK_HEIGHT": integer
static readonly "GL_UNPACK_COMPRESSED_BLOCK_DEPTH": integer
static readonly "GL_UNPACK_COMPRESSED_BLOCK_SIZE": integer
static readonly "GL_PACK_COMPRESSED_BLOCK_WIDTH": integer
static readonly "GL_PACK_COMPRESSED_BLOCK_HEIGHT": integer
static readonly "GL_PACK_COMPRESSED_BLOCK_DEPTH": integer
static readonly "GL_PACK_COMPRESSED_BLOCK_SIZE": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_BINDING": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_START": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_SIZE": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER": integer
static readonly "GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER": integer
static readonly "GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_MAX_VERTEX_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_GEOMETRY_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_FRAGMENT_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_COMBINED_ATOMIC_COUNTERS": integer
static readonly "GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE": integer
static readonly "GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS": integer
static readonly "GL_ACTIVE_ATOMIC_COUNTER_BUFFERS": integer
static readonly "GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX": integer
static readonly "GL_UNSIGNED_INT_ATOMIC_COUNTER": integer
static readonly "GL_TEXTURE_IMMUTABLE_FORMAT": integer
static readonly "GL_MAX_IMAGE_UNITS": integer
static readonly "GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS": integer
static readonly "GL_MAX_IMAGE_SAMPLES": integer
static readonly "GL_MAX_VERTEX_IMAGE_UNIFORMS": integer
static readonly "GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS": integer
static readonly "GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS": integer
static readonly "GL_MAX_GEOMETRY_IMAGE_UNIFORMS": integer
static readonly "GL_MAX_FRAGMENT_IMAGE_UNIFORMS": integer
static readonly "GL_MAX_COMBINED_IMAGE_UNIFORMS": integer
static readonly "GL_IMAGE_BINDING_NAME": integer
static readonly "GL_IMAGE_BINDING_LEVEL": integer
static readonly "GL_IMAGE_BINDING_LAYERED": integer
static readonly "GL_IMAGE_BINDING_LAYER": integer
static readonly "GL_IMAGE_BINDING_ACCESS": integer
static readonly "GL_IMAGE_BINDING_FORMAT": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT": integer
static readonly "GL_ELEMENT_ARRAY_BARRIER_BIT": integer
static readonly "GL_UNIFORM_BARRIER_BIT": integer
static readonly "GL_TEXTURE_FETCH_BARRIER_BIT": integer
static readonly "GL_SHADER_IMAGE_ACCESS_BARRIER_BIT": integer
static readonly "GL_COMMAND_BARRIER_BIT": integer
static readonly "GL_PIXEL_BUFFER_BARRIER_BIT": integer
static readonly "GL_TEXTURE_UPDATE_BARRIER_BIT": integer
static readonly "GL_BUFFER_UPDATE_BARRIER_BIT": integer
static readonly "GL_FRAMEBUFFER_BARRIER_BIT": integer
static readonly "GL_TRANSFORM_FEEDBACK_BARRIER_BIT": integer
static readonly "GL_ATOMIC_COUNTER_BARRIER_BIT": integer
static readonly "GL_ALL_BARRIER_BITS": integer
static readonly "GL_IMAGE_1D": integer
static readonly "GL_IMAGE_2D": integer
static readonly "GL_IMAGE_3D": integer
static readonly "GL_IMAGE_2D_RECT": integer
static readonly "GL_IMAGE_CUBE": integer
static readonly "GL_IMAGE_BUFFER": integer
static readonly "GL_IMAGE_1D_ARRAY": integer
static readonly "GL_IMAGE_2D_ARRAY": integer
static readonly "GL_IMAGE_CUBE_MAP_ARRAY": integer
static readonly "GL_IMAGE_2D_MULTISAMPLE": integer
static readonly "GL_IMAGE_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_INT_IMAGE_1D": integer
static readonly "GL_INT_IMAGE_2D": integer
static readonly "GL_INT_IMAGE_3D": integer
static readonly "GL_INT_IMAGE_2D_RECT": integer
static readonly "GL_INT_IMAGE_CUBE": integer
static readonly "GL_INT_IMAGE_BUFFER": integer
static readonly "GL_INT_IMAGE_1D_ARRAY": integer
static readonly "GL_INT_IMAGE_2D_ARRAY": integer
static readonly "GL_INT_IMAGE_CUBE_MAP_ARRAY": integer
static readonly "GL_INT_IMAGE_2D_MULTISAMPLE": integer
static readonly "GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_UNSIGNED_INT_IMAGE_1D": integer
static readonly "GL_UNSIGNED_INT_IMAGE_2D": integer
static readonly "GL_UNSIGNED_INT_IMAGE_3D": integer
static readonly "GL_UNSIGNED_INT_IMAGE_2D_RECT": integer
static readonly "GL_UNSIGNED_INT_IMAGE_CUBE": integer
static readonly "GL_UNSIGNED_INT_IMAGE_BUFFER": integer
static readonly "GL_UNSIGNED_INT_IMAGE_1D_ARRAY": integer
static readonly "GL_UNSIGNED_INT_IMAGE_2D_ARRAY": integer
static readonly "GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY": integer
static readonly "GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE": integer
static readonly "GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_IMAGE_FORMAT_COMPATIBILITY_TYPE": integer
static readonly "GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE": integer
static readonly "GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS": integer
static readonly "GL_NUM_SAMPLE_COUNTS": integer
static readonly "GL_MIN_MAP_BUFFER_ALIGNMENT": integer
static readonly "GL_SHADER_COMPILER": integer
static readonly "GL_SHADER_BINARY_FORMATS": integer
static readonly "GL_NUM_SHADER_BINARY_FORMATS": integer
static readonly "GL_MAX_VERTEX_UNIFORM_VECTORS": integer
static readonly "GL_MAX_VARYING_VECTORS": integer
static readonly "GL_MAX_FRAGMENT_UNIFORM_VECTORS": integer
static readonly "GL_IMPLEMENTATION_COLOR_READ_TYPE": integer
static readonly "GL_IMPLEMENTATION_COLOR_READ_FORMAT": integer
static readonly "GL_FIXED": integer
static readonly "GL_LOW_FLOAT": integer
static readonly "GL_MEDIUM_FLOAT": integer
static readonly "GL_HIGH_FLOAT": integer
static readonly "GL_LOW_INT": integer
static readonly "GL_MEDIUM_INT": integer
static readonly "GL_HIGH_INT": integer
static readonly "GL_RGB565": integer
static readonly "GL_PROGRAM_BINARY_RETRIEVABLE_HINT": integer
static readonly "GL_PROGRAM_BINARY_LENGTH": integer
static readonly "GL_NUM_PROGRAM_BINARY_FORMATS": integer
static readonly "GL_PROGRAM_BINARY_FORMATS": integer
static readonly "GL_VERTEX_SHADER_BIT": integer
static readonly "GL_FRAGMENT_SHADER_BIT": integer
static readonly "GL_GEOMETRY_SHADER_BIT": integer
static readonly "GL_TESS_CONTROL_SHADER_BIT": integer
static readonly "GL_TESS_EVALUATION_SHADER_BIT": integer
static readonly "GL_ALL_SHADER_BITS": integer
static readonly "GL_PROGRAM_SEPARABLE": integer
static readonly "GL_ACTIVE_PROGRAM": integer
static readonly "GL_PROGRAM_PIPELINE_BINDING": integer
static readonly "GL_MAX_VIEWPORTS": integer
static readonly "GL_VIEWPORT_SUBPIXEL_BITS": integer
static readonly "GL_VIEWPORT_BOUNDS_RANGE": integer
static readonly "GL_LAYER_PROVOKING_VERTEX": integer
static readonly "GL_VIEWPORT_INDEX_PROVOKING_VERTEX": integer
static readonly "GL_UNDEFINED_VERTEX": integer
static readonly "GL_DRAW_INDIRECT_BUFFER": integer
static readonly "GL_DRAW_INDIRECT_BUFFER_BINDING": integer
static readonly "GL_GEOMETRY_SHADER_INVOCATIONS": integer
static readonly "GL_MAX_GEOMETRY_SHADER_INVOCATIONS": integer
static readonly "GL_MIN_FRAGMENT_INTERPOLATION_OFFSET": integer
static readonly "GL_MAX_FRAGMENT_INTERPOLATION_OFFSET": integer
static readonly "GL_FRAGMENT_INTERPOLATION_OFFSET_BITS": integer
static readonly "GL_DOUBLE_VEC2": integer
static readonly "GL_DOUBLE_VEC3": integer
static readonly "GL_DOUBLE_VEC4": integer
static readonly "GL_DOUBLE_MAT2": integer
static readonly "GL_DOUBLE_MAT3": integer
static readonly "GL_DOUBLE_MAT4": integer
static readonly "GL_DOUBLE_MAT2x3": integer
static readonly "GL_DOUBLE_MAT2x4": integer
static readonly "GL_DOUBLE_MAT3x2": integer
static readonly "GL_DOUBLE_MAT3x4": integer
static readonly "GL_DOUBLE_MAT4x2": integer
static readonly "GL_DOUBLE_MAT4x3": integer
static readonly "GL_SAMPLE_SHADING": integer
static readonly "GL_MIN_SAMPLE_SHADING_VALUE": integer
static readonly "GL_ACTIVE_SUBROUTINES": integer
static readonly "GL_ACTIVE_SUBROUTINE_UNIFORMS": integer
static readonly "GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS": integer
static readonly "GL_ACTIVE_SUBROUTINE_MAX_LENGTH": integer
static readonly "GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH": integer
static readonly "GL_MAX_SUBROUTINES": integer
static readonly "GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS": integer
static readonly "GL_NUM_COMPATIBLE_SUBROUTINES": integer
static readonly "GL_COMPATIBLE_SUBROUTINES": integer
static readonly "GL_PATCHES": integer
static readonly "GL_PATCH_VERTICES": integer
static readonly "GL_PATCH_DEFAULT_INNER_LEVEL": integer
static readonly "GL_PATCH_DEFAULT_OUTER_LEVEL": integer
static readonly "GL_TESS_CONTROL_OUTPUT_VERTICES": integer
static readonly "GL_TESS_GEN_MODE": integer
static readonly "GL_TESS_GEN_SPACING": integer
static readonly "GL_TESS_GEN_VERTEX_ORDER": integer
static readonly "GL_TESS_GEN_POINT_MODE": integer
static readonly "GL_ISOLINES": integer
static readonly "GL_FRACTIONAL_ODD": integer
static readonly "GL_FRACTIONAL_EVEN": integer
static readonly "GL_MAX_PATCH_VERTICES": integer
static readonly "GL_MAX_TESS_GEN_LEVEL": integer
static readonly "GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS": integer
static readonly "GL_MAX_TESS_PATCH_COMPONENTS": integer
static readonly "GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS": integer
static readonly "GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS": integer
static readonly "GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_TESS_CONTROL_INPUT_COMPONENTS": integer
static readonly "GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS": integer
static readonly "GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS": integer
static readonly "GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER": integer
static readonly "GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER": integer
static readonly "GL_TESS_EVALUATION_SHADER": integer
static readonly "GL_TESS_CONTROL_SHADER": integer
static readonly "GL_TEXTURE_CUBE_MAP_ARRAY": integer
static readonly "GL_TEXTURE_BINDING_CUBE_MAP_ARRAY": integer
static readonly "GL_PROXY_TEXTURE_CUBE_MAP_ARRAY": integer
static readonly "GL_SAMPLER_CUBE_MAP_ARRAY": integer
static readonly "GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW": integer
static readonly "GL_INT_SAMPLER_CUBE_MAP_ARRAY": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY": integer
static readonly "GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET": integer
static readonly "GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET": integer
static readonly "GL_TRANSFORM_FEEDBACK": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE": integer
static readonly "GL_TRANSFORM_FEEDBACK_BINDING": integer
static readonly "GL_MAX_TRANSFORM_FEEDBACK_BUFFERS": integer
static readonly "GL_MAX_VERTEX_STREAMS": integer
static readonly "GL_SRC1_COLOR": integer
static readonly "GL_ONE_MINUS_SRC1_COLOR": integer
static readonly "GL_ONE_MINUS_SRC1_ALPHA": integer
static readonly "GL_MAX_DUAL_SOURCE_DRAW_BUFFERS": integer
static readonly "GL_ANY_SAMPLES_PASSED": integer
static readonly "GL_SAMPLER_BINDING": integer
static readonly "GL_RGB10_A2UI": integer
static readonly "GL_TEXTURE_SWIZZLE_R": integer
static readonly "GL_TEXTURE_SWIZZLE_G": integer
static readonly "GL_TEXTURE_SWIZZLE_B": integer
static readonly "GL_TEXTURE_SWIZZLE_A": integer
static readonly "GL_TEXTURE_SWIZZLE_RGBA": integer
static readonly "GL_TIME_ELAPSED": integer
static readonly "GL_TIMESTAMP": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_DIVISOR": integer
static readonly "GL_INT_2_10_10_10_REV": integer
static readonly "GL_CONTEXT_PROFILE_MASK": integer
static readonly "GL_CONTEXT_CORE_PROFILE_BIT": integer
static readonly "GL_CONTEXT_COMPATIBILITY_PROFILE_BIT": integer
static readonly "GL_MAX_VERTEX_OUTPUT_COMPONENTS": integer
static readonly "GL_MAX_GEOMETRY_INPUT_COMPONENTS": integer
static readonly "GL_MAX_GEOMETRY_OUTPUT_COMPONENTS": integer
static readonly "GL_MAX_FRAGMENT_INPUT_COMPONENTS": integer
static readonly "GL_FIRST_VERTEX_CONVENTION": integer
static readonly "GL_LAST_VERTEX_CONVENTION": integer
static readonly "GL_PROVOKING_VERTEX": integer
static readonly "GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION": integer
static readonly "GL_TEXTURE_CUBE_MAP_SEAMLESS": integer
static readonly "GL_SAMPLE_POSITION": integer
static readonly "GL_SAMPLE_MASK": integer
static readonly "GL_SAMPLE_MASK_VALUE": integer
static readonly "GL_TEXTURE_2D_MULTISAMPLE": integer
static readonly "GL_PROXY_TEXTURE_2D_MULTISAMPLE": integer
static readonly "GL_TEXTURE_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_MAX_SAMPLE_MASK_WORDS": integer
static readonly "GL_MAX_COLOR_TEXTURE_SAMPLES": integer
static readonly "GL_MAX_DEPTH_TEXTURE_SAMPLES": integer
static readonly "GL_MAX_INTEGER_SAMPLES": integer
static readonly "GL_TEXTURE_BINDING_2D_MULTISAMPLE": integer
static readonly "GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_TEXTURE_SAMPLES": integer
static readonly "GL_TEXTURE_FIXED_SAMPLE_LOCATIONS": integer
static readonly "GL_SAMPLER_2D_MULTISAMPLE": integer
static readonly "GL_INT_SAMPLER_2D_MULTISAMPLE": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE": integer
static readonly "GL_SAMPLER_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY": integer
static readonly "GL_DEPTH_CLAMP": integer
static readonly "GL_GEOMETRY_SHADER": integer
static readonly "GL_GEOMETRY_VERTICES_OUT": integer
static readonly "GL_GEOMETRY_INPUT_TYPE": integer
static readonly "GL_GEOMETRY_OUTPUT_TYPE": integer
static readonly "GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_MAX_GEOMETRY_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_GEOMETRY_OUTPUT_VERTICES": integer
static readonly "GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS": integer
static readonly "GL_LINES_ADJACENCY": integer
static readonly "GL_LINE_STRIP_ADJACENCY": integer
static readonly "GL_TRIANGLES_ADJACENCY": integer
static readonly "GL_TRIANGLE_STRIP_ADJACENCY": integer
static readonly "GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_LAYERED": integer
static readonly "GL_PROGRAM_POINT_SIZE": integer
static readonly "GL_MAX_SERVER_WAIT_TIMEOUT": integer
static readonly "GL_OBJECT_TYPE": integer
static readonly "GL_SYNC_CONDITION": integer
static readonly "GL_SYNC_STATUS": integer
static readonly "GL_SYNC_FLAGS": integer
static readonly "GL_SYNC_FENCE": integer
static readonly "GL_SYNC_GPU_COMMANDS_COMPLETE": integer
static readonly "GL_UNSIGNALED": integer
static readonly "GL_SIGNALED": integer
static readonly "GL_SYNC_FLUSH_COMMANDS_BIT": integer
static readonly "GL_TIMEOUT_IGNORED": long
static readonly "GL_ALREADY_SIGNALED": integer
static readonly "GL_TIMEOUT_EXPIRED": integer
static readonly "GL_CONDITION_SATISFIED": integer
static readonly "GL_WAIT_FAILED": integer
static readonly "GL_R8_SNORM": integer
static readonly "GL_RG8_SNORM": integer
static readonly "GL_RGB8_SNORM": integer
static readonly "GL_RGBA8_SNORM": integer
static readonly "GL_R16_SNORM": integer
static readonly "GL_RG16_SNORM": integer
static readonly "GL_RGB16_SNORM": integer
static readonly "GL_RGBA16_SNORM": integer
static readonly "GL_SIGNED_NORMALIZED": integer
static readonly "GL_SAMPLER_BUFFER": integer
static readonly "GL_INT_SAMPLER_2D_RECT": integer
static readonly "GL_INT_SAMPLER_BUFFER": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_2D_RECT": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_BUFFER": integer
static readonly "GL_COPY_READ_BUFFER": integer
static readonly "GL_COPY_WRITE_BUFFER": integer
static readonly "GL_PRIMITIVE_RESTART": integer
static readonly "GL_PRIMITIVE_RESTART_INDEX": integer
static readonly "GL_TEXTURE_BUFFER": integer
static readonly "GL_MAX_TEXTURE_BUFFER_SIZE": integer
static readonly "GL_TEXTURE_BINDING_BUFFER": integer
static readonly "GL_TEXTURE_BUFFER_DATA_STORE_BINDING": integer
static readonly "GL_TEXTURE_RECTANGLE": integer
static readonly "GL_TEXTURE_BINDING_RECTANGLE": integer
static readonly "GL_PROXY_TEXTURE_RECTANGLE": integer
static readonly "GL_MAX_RECTANGLE_TEXTURE_SIZE": integer
static readonly "GL_SAMPLER_2D_RECT": integer
static readonly "GL_SAMPLER_2D_RECT_SHADOW": integer
static readonly "GL_UNIFORM_BUFFER": integer
static readonly "GL_UNIFORM_BUFFER_BINDING": integer
static readonly "GL_UNIFORM_BUFFER_START": integer
static readonly "GL_UNIFORM_BUFFER_SIZE": integer
static readonly "GL_MAX_VERTEX_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_GEOMETRY_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_FRAGMENT_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_COMBINED_UNIFORM_BLOCKS": integer
static readonly "GL_MAX_UNIFORM_BUFFER_BINDINGS": integer
static readonly "GL_MAX_UNIFORM_BLOCK_SIZE": integer
static readonly "GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS": integer
static readonly "GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT": integer
static readonly "GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH": integer
static readonly "GL_ACTIVE_UNIFORM_BLOCKS": integer
static readonly "GL_UNIFORM_TYPE": integer
static readonly "GL_UNIFORM_SIZE": integer
static readonly "GL_UNIFORM_NAME_LENGTH": integer
static readonly "GL_UNIFORM_BLOCK_INDEX": integer
static readonly "GL_UNIFORM_OFFSET": integer
static readonly "GL_UNIFORM_ARRAY_STRIDE": integer
static readonly "GL_UNIFORM_MATRIX_STRIDE": integer
static readonly "GL_UNIFORM_IS_ROW_MAJOR": integer
static readonly "GL_UNIFORM_BLOCK_BINDING": integer
static readonly "GL_UNIFORM_BLOCK_DATA_SIZE": integer
static readonly "GL_UNIFORM_BLOCK_NAME_LENGTH": integer
static readonly "GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS": integer
static readonly "GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES": integer
static readonly "GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER": integer
static readonly "GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER": integer
static readonly "GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER": integer
static readonly "GL_INVALID_INDEX": integer
static readonly "GL_MAJOR_VERSION": integer
static readonly "GL_MINOR_VERSION": integer
static readonly "GL_NUM_EXTENSIONS": integer
static readonly "GL_CONTEXT_FLAGS": integer
static readonly "GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT": integer
static readonly "GL_COMPARE_REF_TO_TEXTURE": integer
static readonly "GL_CLIP_DISTANCE0": integer
static readonly "GL_CLIP_DISTANCE1": integer
static readonly "GL_CLIP_DISTANCE2": integer
static readonly "GL_CLIP_DISTANCE3": integer
static readonly "GL_CLIP_DISTANCE4": integer
static readonly "GL_CLIP_DISTANCE5": integer
static readonly "GL_CLIP_DISTANCE6": integer
static readonly "GL_CLIP_DISTANCE7": integer
static readonly "GL_MAX_CLIP_DISTANCES": integer
static readonly "GL_MAX_VARYING_COMPONENTS": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_INTEGER": integer
static readonly "GL_SAMPLER_1D_ARRAY": integer
static readonly "GL_SAMPLER_2D_ARRAY": integer
static readonly "GL_SAMPLER_1D_ARRAY_SHADOW": integer
static readonly "GL_SAMPLER_2D_ARRAY_SHADOW": integer
static readonly "GL_SAMPLER_CUBE_SHADOW": integer
static readonly "GL_UNSIGNED_INT_VEC2": integer
static readonly "GL_UNSIGNED_INT_VEC3": integer
static readonly "GL_UNSIGNED_INT_VEC4": integer
static readonly "GL_INT_SAMPLER_1D": integer
static readonly "GL_INT_SAMPLER_2D": integer
static readonly "GL_INT_SAMPLER_3D": integer
static readonly "GL_INT_SAMPLER_CUBE": integer
static readonly "GL_INT_SAMPLER_1D_ARRAY": integer
static readonly "GL_INT_SAMPLER_2D_ARRAY": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_1D": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_2D": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_3D": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_CUBE": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_1D_ARRAY": integer
static readonly "GL_UNSIGNED_INT_SAMPLER_2D_ARRAY": integer
static readonly "GL_MIN_PROGRAM_TEXEL_OFFSET": integer
static readonly "GL_MAX_PROGRAM_TEXEL_OFFSET": integer
static readonly "GL_QUERY_WAIT": integer
static readonly "GL_QUERY_NO_WAIT": integer
static readonly "GL_QUERY_BY_REGION_WAIT": integer
static readonly "GL_QUERY_BY_REGION_NO_WAIT": integer
static readonly "GL_MAP_READ_BIT": integer
static readonly "GL_MAP_WRITE_BIT": integer
static readonly "GL_MAP_INVALIDATE_RANGE_BIT": integer
static readonly "GL_MAP_INVALIDATE_BUFFER_BIT": integer
static readonly "GL_MAP_FLUSH_EXPLICIT_BIT": integer
static readonly "GL_MAP_UNSYNCHRONIZED_BIT": integer
static readonly "GL_BUFFER_ACCESS_FLAGS": integer
static readonly "GL_BUFFER_MAP_LENGTH": integer
static readonly "GL_BUFFER_MAP_OFFSET": integer
static readonly "GL_CLAMP_READ_COLOR": integer
static readonly "GL_FIXED_ONLY": integer
static readonly "GL_DEPTH_COMPONENT32F": integer
static readonly "GL_DEPTH32F_STENCIL8": integer
static readonly "GL_FLOAT_32_UNSIGNED_INT_24_8_REV": integer
static readonly "GL_TEXTURE_RED_TYPE": integer
static readonly "GL_TEXTURE_GREEN_TYPE": integer
static readonly "GL_TEXTURE_BLUE_TYPE": integer
static readonly "GL_TEXTURE_ALPHA_TYPE": integer
static readonly "GL_TEXTURE_DEPTH_TYPE": integer
static readonly "GL_UNSIGNED_NORMALIZED": integer
static readonly "GL_RGBA32F": integer
static readonly "GL_RGB32F": integer
static readonly "GL_RGBA16F": integer
static readonly "GL_RGB16F": integer
static readonly "GL_R11F_G11F_B10F": integer
static readonly "GL_UNSIGNED_INT_10F_11F_11F_REV": integer
static readonly "GL_RGB9_E5": integer
static readonly "GL_UNSIGNED_INT_5_9_9_9_REV": integer
static readonly "GL_TEXTURE_SHARED_SIZE": integer
static readonly "GL_FRAMEBUFFER": integer
static readonly "GL_READ_FRAMEBUFFER": integer
static readonly "GL_DRAW_FRAMEBUFFER": integer
static readonly "GL_RENDERBUFFER": integer
static readonly "GL_STENCIL_INDEX1": integer
static readonly "GL_STENCIL_INDEX4": integer
static readonly "GL_STENCIL_INDEX8": integer
static readonly "GL_STENCIL_INDEX16": integer
static readonly "GL_RENDERBUFFER_WIDTH": integer
static readonly "GL_RENDERBUFFER_HEIGHT": integer
static readonly "GL_RENDERBUFFER_INTERNAL_FORMAT": integer
static readonly "GL_RENDERBUFFER_RED_SIZE": integer
static readonly "GL_RENDERBUFFER_GREEN_SIZE": integer
static readonly "GL_RENDERBUFFER_BLUE_SIZE": integer
static readonly "GL_RENDERBUFFER_ALPHA_SIZE": integer
static readonly "GL_RENDERBUFFER_DEPTH_SIZE": integer
static readonly "GL_RENDERBUFFER_STENCIL_SIZE": integer
static readonly "GL_RENDERBUFFER_SAMPLES": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE": integer
static readonly "GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE": integer
static readonly "GL_FRAMEBUFFER_DEFAULT": integer
static readonly "GL_COLOR_ATTACHMENT0": integer
static readonly "GL_COLOR_ATTACHMENT1": integer
static readonly "GL_COLOR_ATTACHMENT2": integer
static readonly "GL_COLOR_ATTACHMENT3": integer
static readonly "GL_COLOR_ATTACHMENT4": integer
static readonly "GL_COLOR_ATTACHMENT5": integer
static readonly "GL_COLOR_ATTACHMENT6": integer
static readonly "GL_COLOR_ATTACHMENT7": integer
static readonly "GL_COLOR_ATTACHMENT8": integer
static readonly "GL_COLOR_ATTACHMENT9": integer
static readonly "GL_COLOR_ATTACHMENT10": integer
static readonly "GL_COLOR_ATTACHMENT11": integer
static readonly "GL_COLOR_ATTACHMENT12": integer
static readonly "GL_COLOR_ATTACHMENT13": integer
static readonly "GL_COLOR_ATTACHMENT14": integer
static readonly "GL_COLOR_ATTACHMENT15": integer
static readonly "GL_COLOR_ATTACHMENT16": integer
static readonly "GL_COLOR_ATTACHMENT17": integer
static readonly "GL_COLOR_ATTACHMENT18": integer
static readonly "GL_COLOR_ATTACHMENT19": integer
static readonly "GL_COLOR_ATTACHMENT20": integer
static readonly "GL_COLOR_ATTACHMENT21": integer
static readonly "GL_COLOR_ATTACHMENT22": integer
static readonly "GL_COLOR_ATTACHMENT23": integer
static readonly "GL_COLOR_ATTACHMENT24": integer
static readonly "GL_COLOR_ATTACHMENT25": integer
static readonly "GL_COLOR_ATTACHMENT26": integer
static readonly "GL_COLOR_ATTACHMENT27": integer
static readonly "GL_COLOR_ATTACHMENT28": integer
static readonly "GL_COLOR_ATTACHMENT29": integer
static readonly "GL_COLOR_ATTACHMENT30": integer
static readonly "GL_COLOR_ATTACHMENT31": integer
static readonly "GL_DEPTH_ATTACHMENT": integer
static readonly "GL_STENCIL_ATTACHMENT": integer
static readonly "GL_DEPTH_STENCIL_ATTACHMENT": integer
static readonly "GL_MAX_SAMPLES": integer
static readonly "GL_FRAMEBUFFER_COMPLETE": integer
static readonly "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT": integer
static readonly "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT": integer
static readonly "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER": integer
static readonly "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER": integer
static readonly "GL_FRAMEBUFFER_UNSUPPORTED": integer
static readonly "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE": integer
static readonly "GL_FRAMEBUFFER_UNDEFINED": integer
static readonly "GL_FRAMEBUFFER_BINDING": integer
static readonly "GL_DRAW_FRAMEBUFFER_BINDING": integer
static readonly "GL_READ_FRAMEBUFFER_BINDING": integer
static readonly "GL_RENDERBUFFER_BINDING": integer
static readonly "GL_MAX_COLOR_ATTACHMENTS": integer
static readonly "GL_MAX_RENDERBUFFER_SIZE": integer
static readonly "GL_INVALID_FRAMEBUFFER_OPERATION": integer
static readonly "GL_DEPTH_STENCIL": integer
static readonly "GL_UNSIGNED_INT_24_8": integer
static readonly "GL_DEPTH24_STENCIL8": integer
static readonly "GL_TEXTURE_STENCIL_SIZE": integer
static readonly "GL_HALF_FLOAT": integer
static readonly "GL_RGBA32UI": integer
static readonly "GL_RGB32UI": integer
static readonly "GL_RGBA16UI": integer
static readonly "GL_RGB16UI": integer
static readonly "GL_RGBA8UI": integer
static readonly "GL_RGB8UI": integer
static readonly "GL_RGBA32I": integer
static readonly "GL_RGB32I": integer
static readonly "GL_RGBA16I": integer
static readonly "GL_RGB16I": integer
static readonly "GL_RGBA8I": integer
static readonly "GL_RGB8I": integer
static readonly "GL_RED_INTEGER": integer
static readonly "GL_GREEN_INTEGER": integer
static readonly "GL_BLUE_INTEGER": integer
static readonly "GL_RGB_INTEGER": integer
static readonly "GL_RGBA_INTEGER": integer
static readonly "GL_BGR_INTEGER": integer
static readonly "GL_BGRA_INTEGER": integer
static readonly "GL_TEXTURE_1D_ARRAY": integer
static readonly "GL_TEXTURE_2D_ARRAY": integer
static readonly "GL_PROXY_TEXTURE_2D_ARRAY": integer
static readonly "GL_PROXY_TEXTURE_1D_ARRAY": integer
static readonly "GL_TEXTURE_BINDING_1D_ARRAY": integer
static readonly "GL_TEXTURE_BINDING_2D_ARRAY": integer
static readonly "GL_MAX_ARRAY_TEXTURE_LAYERS": integer
static readonly "GL_COMPRESSED_RED_RGTC1": integer
static readonly "GL_COMPRESSED_SIGNED_RED_RGTC1": integer
static readonly "GL_COMPRESSED_RG_RGTC2": integer
static readonly "GL_COMPRESSED_SIGNED_RG_RGTC2": integer
static readonly "GL_R8": integer
static readonly "GL_R16": integer
static readonly "GL_RG8": integer
static readonly "GL_RG16": integer
static readonly "GL_R16F": integer
static readonly "GL_R32F": integer
static readonly "GL_RG16F": integer
static readonly "GL_RG32F": integer
static readonly "GL_R8I": integer
static readonly "GL_R8UI": integer
static readonly "GL_R16I": integer
static readonly "GL_R16UI": integer
static readonly "GL_R32I": integer
static readonly "GL_R32UI": integer
static readonly "GL_RG8I": integer
static readonly "GL_RG8UI": integer
static readonly "GL_RG16I": integer
static readonly "GL_RG16UI": integer
static readonly "GL_RG32I": integer
static readonly "GL_RG32UI": integer
static readonly "GL_RG": integer
static readonly "GL_COMPRESSED_RED": integer
static readonly "GL_COMPRESSED_RG": integer
static readonly "GL_RG_INTEGER": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_START": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_SIZE": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_BINDING": integer
static readonly "GL_INTERLEAVED_ATTRIBS": integer
static readonly "GL_SEPARATE_ATTRIBS": integer
static readonly "GL_PRIMITIVES_GENERATED": integer
static readonly "GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN": integer
static readonly "GL_RASTERIZER_DISCARD": integer
static readonly "GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS": integer
static readonly "GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS": integer
static readonly "GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS": integer
static readonly "GL_TRANSFORM_FEEDBACK_VARYINGS": integer
static readonly "GL_TRANSFORM_FEEDBACK_BUFFER_MODE": integer
static readonly "GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH": integer
static readonly "GL_VERTEX_ARRAY_BINDING": integer
static readonly "GL_FRAMEBUFFER_SRGB": integer
static readonly "GL_FLOAT_MAT2x3": integer
static readonly "GL_FLOAT_MAT2x4": integer
static readonly "GL_FLOAT_MAT3x2": integer
static readonly "GL_FLOAT_MAT3x4": integer
static readonly "GL_FLOAT_MAT4x2": integer
static readonly "GL_FLOAT_MAT4x3": integer
static readonly "GL_PIXEL_PACK_BUFFER": integer
static readonly "GL_PIXEL_UNPACK_BUFFER": integer
static readonly "GL_PIXEL_PACK_BUFFER_BINDING": integer
static readonly "GL_PIXEL_UNPACK_BUFFER_BINDING": integer
static readonly "GL_SRGB": integer
static readonly "GL_SRGB8": integer
static readonly "GL_SRGB_ALPHA": integer
static readonly "GL_SRGB8_ALPHA8": integer
static readonly "GL_COMPRESSED_SRGB": integer
static readonly "GL_COMPRESSED_SRGB_ALPHA": integer
static readonly "GL_SHADING_LANGUAGE_VERSION": integer
static readonly "GL_CURRENT_PROGRAM": integer
static readonly "GL_SHADER_TYPE": integer
static readonly "GL_DELETE_STATUS": integer
static readonly "GL_COMPILE_STATUS": integer
static readonly "GL_LINK_STATUS": integer
static readonly "GL_VALIDATE_STATUS": integer
static readonly "GL_INFO_LOG_LENGTH": integer
static readonly "GL_ATTACHED_SHADERS": integer
static readonly "GL_ACTIVE_UNIFORMS": integer
static readonly "GL_ACTIVE_UNIFORM_MAX_LENGTH": integer
static readonly "GL_ACTIVE_ATTRIBUTES": integer
static readonly "GL_ACTIVE_ATTRIBUTE_MAX_LENGTH": integer
static readonly "GL_SHADER_SOURCE_LENGTH": integer
static readonly "GL_FLOAT_VEC2": integer
static readonly "GL_FLOAT_VEC3": integer
static readonly "GL_FLOAT_VEC4": integer
static readonly "GL_INT_VEC2": integer
static readonly "GL_INT_VEC3": integer
static readonly "GL_INT_VEC4": integer
static readonly "GL_BOOL": integer
static readonly "GL_BOOL_VEC2": integer
static readonly "GL_BOOL_VEC3": integer
static readonly "GL_BOOL_VEC4": integer
static readonly "GL_FLOAT_MAT2": integer
static readonly "GL_FLOAT_MAT3": integer
static readonly "GL_FLOAT_MAT4": integer
static readonly "GL_SAMPLER_1D": integer
static readonly "GL_SAMPLER_2D": integer
static readonly "GL_SAMPLER_3D": integer
static readonly "GL_SAMPLER_CUBE": integer
static readonly "GL_SAMPLER_1D_SHADOW": integer
static readonly "GL_SAMPLER_2D_SHADOW": integer
static readonly "GL_VERTEX_SHADER": integer
static readonly "GL_MAX_VERTEX_UNIFORM_COMPONENTS": integer
static readonly "GL_MAX_VARYING_FLOATS": integer
static readonly "GL_MAX_VERTEX_ATTRIBS": integer
static readonly "GL_MAX_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS": integer
static readonly "GL_VERTEX_PROGRAM_POINT_SIZE": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_ENABLED": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_SIZE": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_STRIDE": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_TYPE": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_NORMALIZED": integer
static readonly "GL_CURRENT_VERTEX_ATTRIB": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_POINTER": integer
static readonly "GL_FRAGMENT_SHADER": integer
static readonly "GL_MAX_FRAGMENT_UNIFORM_COMPONENTS": integer
static readonly "GL_FRAGMENT_SHADER_DERIVATIVE_HINT": integer
static readonly "GL_MAX_DRAW_BUFFERS": integer
static readonly "GL_DRAW_BUFFER0": integer
static readonly "GL_DRAW_BUFFER1": integer
static readonly "GL_DRAW_BUFFER2": integer
static readonly "GL_DRAW_BUFFER3": integer
static readonly "GL_DRAW_BUFFER4": integer
static readonly "GL_DRAW_BUFFER5": integer
static readonly "GL_DRAW_BUFFER6": integer
static readonly "GL_DRAW_BUFFER7": integer
static readonly "GL_DRAW_BUFFER8": integer
static readonly "GL_DRAW_BUFFER9": integer
static readonly "GL_DRAW_BUFFER10": integer
static readonly "GL_DRAW_BUFFER11": integer
static readonly "GL_DRAW_BUFFER12": integer
static readonly "GL_DRAW_BUFFER13": integer
static readonly "GL_DRAW_BUFFER14": integer
static readonly "GL_DRAW_BUFFER15": integer
static readonly "GL_POINT_SPRITE_COORD_ORIGIN": integer
static readonly "GL_LOWER_LEFT": integer
static readonly "GL_UPPER_LEFT": integer
static readonly "GL_BLEND_EQUATION_RGB": integer
static readonly "GL_BLEND_EQUATION_ALPHA": integer
static readonly "GL_STENCIL_BACK_FUNC": integer
static readonly "GL_STENCIL_BACK_FAIL": integer
static readonly "GL_STENCIL_BACK_PASS_DEPTH_FAIL": integer
static readonly "GL_STENCIL_BACK_PASS_DEPTH_PASS": integer
static readonly "GL_STENCIL_BACK_REF": integer
static readonly "GL_STENCIL_BACK_VALUE_MASK": integer
static readonly "GL_STENCIL_BACK_WRITEMASK": integer
static readonly "GL_SRC1_ALPHA": integer
static readonly "GL_ARRAY_BUFFER": integer
static readonly "GL_ELEMENT_ARRAY_BUFFER": integer
static readonly "GL_ARRAY_BUFFER_BINDING": integer
static readonly "GL_ELEMENT_ARRAY_BUFFER_BINDING": integer
static readonly "GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING": integer
static readonly "GL_STREAM_DRAW": integer
static readonly "GL_STREAM_READ": integer
static readonly "GL_STREAM_COPY": integer
static readonly "GL_STATIC_DRAW": integer
static readonly "GL_STATIC_READ": integer
static readonly "GL_STATIC_COPY": integer
static readonly "GL_DYNAMIC_DRAW": integer
static readonly "GL_DYNAMIC_READ": integer
static readonly "GL_DYNAMIC_COPY": integer
static readonly "GL_READ_ONLY": integer
static readonly "GL_WRITE_ONLY": integer
static readonly "GL_READ_WRITE": integer
static readonly "GL_BUFFER_SIZE": integer
static readonly "GL_BUFFER_USAGE": integer
static readonly "GL_BUFFER_ACCESS": integer
static readonly "GL_BUFFER_MAPPED": integer
static readonly "GL_BUFFER_MAP_POINTER": integer
static readonly "GL_SAMPLES_PASSED": integer
static readonly "GL_QUERY_COUNTER_BITS": integer
static readonly "GL_CURRENT_QUERY": integer
static readonly "GL_QUERY_RESULT": integer
static readonly "GL_QUERY_RESULT_AVAILABLE": integer
static readonly "GL_CONSTANT_COLOR": integer
static readonly "GL_ONE_MINUS_CONSTANT_COLOR": integer
static readonly "GL_CONSTANT_ALPHA": integer
static readonly "GL_ONE_MINUS_CONSTANT_ALPHA": integer
static readonly "GL_FUNC_ADD": integer
static readonly "GL_MIN": integer
static readonly "GL_MAX": integer
static readonly "GL_FUNC_SUBTRACT": integer
static readonly "GL_FUNC_REVERSE_SUBTRACT": integer
static readonly "GL_DEPTH_COMPONENT16": integer
static readonly "GL_DEPTH_COMPONENT24": integer
static readonly "GL_DEPTH_COMPONENT32": integer
static readonly "GL_TEXTURE_DEPTH_SIZE": integer
static readonly "GL_TEXTURE_COMPARE_MODE": integer
static readonly "GL_TEXTURE_COMPARE_FUNC": integer
static readonly "GL_POINT_FADE_THRESHOLD_SIZE": integer
static readonly "GL_BLEND_DST_RGB": integer
static readonly "GL_BLEND_SRC_RGB": integer
static readonly "GL_BLEND_DST_ALPHA": integer
static readonly "GL_BLEND_SRC_ALPHA": integer
static readonly "GL_INCR_WRAP": integer
static readonly "GL_DECR_WRAP": integer
static readonly "GL_TEXTURE_LOD_BIAS": integer
static readonly "GL_MAX_TEXTURE_LOD_BIAS": integer
static readonly "GL_MIRRORED_REPEAT": integer
static readonly "GL_COMPRESSED_RGB": integer
static readonly "GL_COMPRESSED_RGBA": integer
static readonly "GL_TEXTURE_COMPRESSION_HINT": integer
static readonly "GL_TEXTURE_COMPRESSED_IMAGE_SIZE": integer
static readonly "GL_TEXTURE_COMPRESSED": integer
static readonly "GL_NUM_COMPRESSED_TEXTURE_FORMATS": integer
static readonly "GL_COMPRESSED_TEXTURE_FORMATS": integer
static readonly "GL_TEXTURE_CUBE_MAP": integer
static readonly "GL_TEXTURE_BINDING_CUBE_MAP": integer
static readonly "GL_TEXTURE_CUBE_MAP_POSITIVE_X": integer
static readonly "GL_TEXTURE_CUBE_MAP_NEGATIVE_X": integer
static readonly "GL_TEXTURE_CUBE_MAP_POSITIVE_Y": integer
static readonly "GL_TEXTURE_CUBE_MAP_NEGATIVE_Y": integer
static readonly "GL_TEXTURE_CUBE_MAP_POSITIVE_Z": integer
static readonly "GL_TEXTURE_CUBE_MAP_NEGATIVE_Z": integer
static readonly "GL_PROXY_TEXTURE_CUBE_MAP": integer
static readonly "GL_MAX_CUBE_MAP_TEXTURE_SIZE": integer
static readonly "GL_MULTISAMPLE": integer
static readonly "GL_SAMPLE_ALPHA_TO_COVERAGE": integer
static readonly "GL_SAMPLE_ALPHA_TO_ONE": integer
static readonly "GL_SAMPLE_COVERAGE": integer
static readonly "GL_SAMPLE_BUFFERS": integer
static readonly "GL_SAMPLES": integer
static readonly "GL_SAMPLE_COVERAGE_VALUE": integer
static readonly "GL_SAMPLE_COVERAGE_INVERT": integer
static readonly "GL_TEXTURE0": integer
static readonly "GL_TEXTURE1": integer
static readonly "GL_TEXTURE2": integer
static readonly "GL_TEXTURE3": integer
static readonly "GL_TEXTURE4": integer
static readonly "GL_TEXTURE5": integer
static readonly "GL_TEXTURE6": integer
static readonly "GL_TEXTURE7": integer
static readonly "GL_TEXTURE8": integer
static readonly "GL_TEXTURE9": integer
static readonly "GL_TEXTURE10": integer
static readonly "GL_TEXTURE11": integer
static readonly "GL_TEXTURE12": integer
static readonly "GL_TEXTURE13": integer
static readonly "GL_TEXTURE14": integer
static readonly "GL_TEXTURE15": integer
static readonly "GL_TEXTURE16": integer
static readonly "GL_TEXTURE17": integer
static readonly "GL_TEXTURE18": integer
static readonly "GL_TEXTURE19": integer
static readonly "GL_TEXTURE20": integer
static readonly "GL_TEXTURE21": integer
static readonly "GL_TEXTURE22": integer
static readonly "GL_TEXTURE23": integer
static readonly "GL_TEXTURE24": integer
static readonly "GL_TEXTURE25": integer
static readonly "GL_TEXTURE26": integer
static readonly "GL_TEXTURE27": integer
static readonly "GL_TEXTURE28": integer
static readonly "GL_TEXTURE29": integer
static readonly "GL_TEXTURE30": integer
static readonly "GL_TEXTURE31": integer
static readonly "GL_ACTIVE_TEXTURE": integer
static readonly "GL_CLAMP_TO_BORDER": integer
static readonly "GL_ALIASED_LINE_WIDTH_RANGE": integer
static readonly "GL_SMOOTH_POINT_SIZE_RANGE": integer
static readonly "GL_SMOOTH_POINT_SIZE_GRANULARITY": integer
static readonly "GL_SMOOTH_LINE_WIDTH_RANGE": integer
static readonly "GL_SMOOTH_LINE_WIDTH_GRANULARITY": integer
static readonly "GL_TEXTURE_BINDING_3D": integer
static readonly "GL_PACK_SKIP_IMAGES": integer
static readonly "GL_PACK_IMAGE_HEIGHT": integer
static readonly "GL_UNPACK_SKIP_IMAGES": integer
static readonly "GL_UNPACK_IMAGE_HEIGHT": integer
static readonly "GL_TEXTURE_3D": integer
static readonly "GL_PROXY_TEXTURE_3D": integer
static readonly "GL_TEXTURE_DEPTH": integer
static readonly "GL_TEXTURE_WRAP_R": integer
static readonly "GL_MAX_3D_TEXTURE_SIZE": integer
static readonly "GL_BGR": integer
static readonly "GL_BGRA": integer
static readonly "GL_UNSIGNED_BYTE_3_3_2": integer
static readonly "GL_UNSIGNED_BYTE_2_3_3_REV": integer
static readonly "GL_UNSIGNED_SHORT_5_6_5": integer
static readonly "GL_UNSIGNED_SHORT_5_6_5_REV": integer
static readonly "GL_UNSIGNED_SHORT_4_4_4_4": integer
static readonly "GL_UNSIGNED_SHORT_4_4_4_4_REV": integer
static readonly "GL_UNSIGNED_SHORT_5_5_5_1": integer
static readonly "GL_UNSIGNED_SHORT_1_5_5_5_REV": integer
static readonly "GL_UNSIGNED_INT_8_8_8_8": integer
static readonly "GL_UNSIGNED_INT_8_8_8_8_REV": integer
static readonly "GL_UNSIGNED_INT_10_10_10_2": integer
static readonly "GL_UNSIGNED_INT_2_10_10_10_REV": integer
static readonly "GL_CLAMP_TO_EDGE": integer
static readonly "GL_TEXTURE_MIN_LOD": integer
static readonly "GL_TEXTURE_MAX_LOD": integer
static readonly "GL_TEXTURE_BASE_LEVEL": integer
static readonly "GL_TEXTURE_MAX_LEVEL": integer
static readonly "GL_MAX_ELEMENTS_VERTICES": integer
static readonly "GL_MAX_ELEMENTS_INDICES": integer
static readonly "GL_NEVER": integer
static readonly "GL_LESS": integer
static readonly "GL_EQUAL": integer
static readonly "GL_LEQUAL": integer
static readonly "GL_GREATER": integer
static readonly "GL_NOTEQUAL": integer
static readonly "GL_GEQUAL": integer
static readonly "GL_ALWAYS": integer
static readonly "GL_DEPTH_BUFFER_BIT": integer
static readonly "GL_STENCIL_BUFFER_BIT": integer
static readonly "GL_COLOR_BUFFER_BIT": integer
static readonly "GL_POINTS": integer
static readonly "GL_LINES": integer
static readonly "GL_LINE_LOOP": integer
static readonly "GL_LINE_STRIP": integer
static readonly "GL_TRIANGLES": integer
static readonly "GL_TRIANGLE_STRIP": integer
static readonly "GL_TRIANGLE_FAN": integer
static readonly "GL_QUADS": integer
static readonly "GL_ZERO": integer
static readonly "GL_ONE": integer
static readonly "GL_SRC_COLOR": integer
static readonly "GL_ONE_MINUS_SRC_COLOR": integer
static readonly "GL_SRC_ALPHA": integer
static readonly "GL_ONE_MINUS_SRC_ALPHA": integer
static readonly "GL_DST_ALPHA": integer
static readonly "GL_ONE_MINUS_DST_ALPHA": integer
static readonly "GL_DST_COLOR": integer
static readonly "GL_ONE_MINUS_DST_COLOR": integer
static readonly "GL_SRC_ALPHA_SATURATE": integer
static readonly "GL_TRUE": integer
static readonly "GL_FALSE": integer
static readonly "GL_BYTE": integer
static readonly "GL_UNSIGNED_BYTE": integer
static readonly "GL_SHORT": integer
static readonly "GL_UNSIGNED_SHORT": integer
static readonly "GL_INT": integer
static readonly "GL_UNSIGNED_INT": integer
static readonly "GL_FLOAT": integer
static readonly "GL_DOUBLE": integer
static readonly "GL_NONE": integer
static readonly "GL_FRONT_LEFT": integer
static readonly "GL_FRONT_RIGHT": integer
static readonly "GL_BACK_LEFT": integer
static readonly "GL_BACK_RIGHT": integer
static readonly "GL_FRONT": integer
static readonly "GL_BACK": integer
static readonly "GL_LEFT": integer
static readonly "GL_RIGHT": integer
static readonly "GL_FRONT_AND_BACK": integer
static readonly "GL_NO_ERROR": integer
static readonly "GL_INVALID_ENUM": integer
static readonly "GL_INVALID_VALUE": integer
static readonly "GL_INVALID_OPERATION": integer
static readonly "GL_STACK_OVERFLOW": integer
static readonly "GL_STACK_UNDERFLOW": integer
static readonly "GL_OUT_OF_MEMORY": integer
static readonly "GL_CW": integer
static readonly "GL_CCW": integer
static readonly "GL_POINT_SIZE": integer
static readonly "GL_POINT_SIZE_RANGE": integer
static readonly "GL_POINT_SIZE_GRANULARITY": integer
static readonly "GL_LINE_SMOOTH": integer
static readonly "GL_LINE_WIDTH": integer
static readonly "GL_LINE_WIDTH_RANGE": integer
static readonly "GL_LINE_WIDTH_GRANULARITY": integer
static readonly "GL_POLYGON_MODE": integer
static readonly "GL_POLYGON_SMOOTH": integer
static readonly "GL_CULL_FACE": integer
static readonly "GL_CULL_FACE_MODE": integer
static readonly "GL_FRONT_FACE": integer
static readonly "GL_DEPTH_RANGE": integer
static readonly "GL_DEPTH_TEST": integer
static readonly "GL_DEPTH_WRITEMASK": integer
static readonly "GL_DEPTH_CLEAR_VALUE": integer
static readonly "GL_DEPTH_FUNC": integer
static readonly "GL_STENCIL_TEST": integer
static readonly "GL_STENCIL_CLEAR_VALUE": integer
static readonly "GL_STENCIL_FUNC": integer
static readonly "GL_STENCIL_VALUE_MASK": integer
static readonly "GL_STENCIL_FAIL": integer
static readonly "GL_STENCIL_PASS_DEPTH_FAIL": integer
static readonly "GL_STENCIL_PASS_DEPTH_PASS": integer
static readonly "GL_STENCIL_REF": integer
static readonly "GL_STENCIL_WRITEMASK": integer
static readonly "GL_VIEWPORT": integer
static readonly "GL_DITHER": integer
static readonly "GL_BLEND_DST": integer
static readonly "GL_BLEND_SRC": integer
static readonly "GL_BLEND": integer
static readonly "GL_LOGIC_OP_MODE": integer
static readonly "GL_COLOR_LOGIC_OP": integer
static readonly "GL_DRAW_BUFFER": integer
static readonly "GL_READ_BUFFER": integer
static readonly "GL_SCISSOR_BOX": integer
static readonly "GL_SCISSOR_TEST": integer
static readonly "GL_COLOR_CLEAR_VALUE": integer
static readonly "GL_COLOR_WRITEMASK": integer
static readonly "GL_DOUBLEBUFFER": integer
static readonly "GL_STEREO": integer
static readonly "GL_LINE_SMOOTH_HINT": integer
static readonly "GL_POLYGON_SMOOTH_HINT": integer
static readonly "GL_UNPACK_SWAP_BYTES": integer
static readonly "GL_UNPACK_LSB_FIRST": integer
static readonly "GL_UNPACK_ROW_LENGTH": integer
static readonly "GL_UNPACK_SKIP_ROWS": integer
static readonly "GL_UNPACK_SKIP_PIXELS": integer
static readonly "GL_UNPACK_ALIGNMENT": integer
static readonly "GL_PACK_SWAP_BYTES": integer
static readonly "GL_PACK_LSB_FIRST": integer
static readonly "GL_PACK_ROW_LENGTH": integer
static readonly "GL_PACK_SKIP_ROWS": integer
static readonly "GL_PACK_SKIP_PIXELS": integer
static readonly "GL_PACK_ALIGNMENT": integer
static readonly "GL_MAX_TEXTURE_SIZE": integer
static readonly "GL_MAX_VIEWPORT_DIMS": integer
static readonly "GL_SUBPIXEL_BITS": integer
static readonly "GL_TEXTURE_1D": integer
static readonly "GL_TEXTURE_2D": integer
static readonly "GL_TEXTURE_WIDTH": integer
static readonly "GL_TEXTURE_HEIGHT": integer
static readonly "GL_TEXTURE_INTERNAL_FORMAT": integer
static readonly "GL_TEXTURE_BORDER_COLOR": integer
static readonly "GL_DONT_CARE": integer
static readonly "GL_FASTEST": integer
static readonly "GL_NICEST": integer
static readonly "GL_CLEAR": integer
static readonly "GL_AND": integer
static readonly "GL_AND_REVERSE": integer
static readonly "GL_COPY": integer
static readonly "GL_AND_INVERTED": integer
static readonly "GL_NOOP": integer
static readonly "GL_XOR": integer
static readonly "GL_OR": integer
static readonly "GL_NOR": integer
static readonly "GL_EQUIV": integer
static readonly "GL_INVERT": integer
static readonly "GL_OR_REVERSE": integer
static readonly "GL_COPY_INVERTED": integer
static readonly "GL_OR_INVERTED": integer
static readonly "GL_NAND": integer
static readonly "GL_SET": integer
static readonly "GL_TEXTURE": integer
static readonly "GL_COLOR": integer
static readonly "GL_DEPTH": integer
static readonly "GL_STENCIL": integer
static readonly "GL_STENCIL_INDEX": integer
static readonly "GL_DEPTH_COMPONENT": integer
static readonly "GL_RED": integer
static readonly "GL_GREEN": integer
static readonly "GL_BLUE": integer
static readonly "GL_ALPHA": integer
static readonly "GL_RGB": integer
static readonly "GL_RGBA": integer
static readonly "GL_POINT": integer
static readonly "GL_LINE": integer
static readonly "GL_FILL": integer
static readonly "GL_KEEP": integer
static readonly "GL_REPLACE": integer
static readonly "GL_INCR": integer
static readonly "GL_DECR": integer
static readonly "GL_VENDOR": integer
static readonly "GL_RENDERER": integer
static readonly "GL_VERSION": integer
static readonly "GL_EXTENSIONS": integer
static readonly "GL_NEAREST": integer
static readonly "GL_LINEAR": integer
static readonly "GL_NEAREST_MIPMAP_NEAREST": integer
static readonly "GL_LINEAR_MIPMAP_NEAREST": integer
static readonly "GL_NEAREST_MIPMAP_LINEAR": integer
static readonly "GL_LINEAR_MIPMAP_LINEAR": integer
static readonly "GL_TEXTURE_MAG_FILTER": integer
static readonly "GL_TEXTURE_MIN_FILTER": integer
static readonly "GL_TEXTURE_WRAP_S": integer
static readonly "GL_TEXTURE_WRAP_T": integer
static readonly "GL_REPEAT": integer
static readonly "GL_POLYGON_OFFSET_FACTOR": integer
static readonly "GL_POLYGON_OFFSET_UNITS": integer
static readonly "GL_POLYGON_OFFSET_POINT": integer
static readonly "GL_POLYGON_OFFSET_LINE": integer
static readonly "GL_POLYGON_OFFSET_FILL": integer
static readonly "GL_R3_G3_B2": integer
static readonly "GL_RGB4": integer
static readonly "GL_RGB5": integer
static readonly "GL_RGB8": integer
static readonly "GL_RGB10": integer
static readonly "GL_RGB12": integer
static readonly "GL_RGB16": integer
static readonly "GL_RGBA2": integer
static readonly "GL_RGBA4": integer
static readonly "GL_RGB5_A1": integer
static readonly "GL_RGBA8": integer
static readonly "GL_RGB10_A2": integer
static readonly "GL_RGBA12": integer
static readonly "GL_RGBA16": integer
static readonly "GL_TEXTURE_RED_SIZE": integer
static readonly "GL_TEXTURE_GREEN_SIZE": integer
static readonly "GL_TEXTURE_BLUE_SIZE": integer
static readonly "GL_TEXTURE_ALPHA_SIZE": integer
static readonly "GL_PROXY_TEXTURE_1D": integer
static readonly "GL_PROXY_TEXTURE_2D": integer
static readonly "GL_TEXTURE_BINDING_1D": integer
static readonly "GL_TEXTURE_BINDING_2D": integer
static readonly "GL_VERTEX_ARRAY": integer


public static "glClearErrors"(): void
public static "find_driver"(vendor: integer, vendorString: string, versionString: string): integer
public static "find_vendor"(vendorString: string): integer
public static "glIndexToFormat"(index: integer): integer
public static "glFormatToIndex"(format: integer): integer
public static "glFormatIsSRGB"(format: integer): boolean
public static "glFormatName"(format: integer): string
public static "glFormatChannels"(format: integer): integer
public static "getDebugSeverity"(severity: integer): string
public static "handleLinkError"(pw: $PrintWriter$Type, headers: (string)[], sources: (string)[], errors: string): void
public static "getSourceARB"(source: integer): string
public static "getTypeARB"(type: integer): string
public static "handleCompileError"(pw: $PrintWriter$Type, source: string, errors: string): void
public static "getCategoryAMD"(category: integer): string
public static "getSeverityARB"(severity: integer): string
public static "getSeverityAMD"(severity: integer): string
public static "glFormatIsSupported"(format: integer): boolean
public static "glFormatIsCompressed"(format: integer): boolean
public static "glFormatIsPackedDepthStencil"(format: integer): boolean
public static "glSpecializeAndAttachShader"(program: integer, shaderType: integer, spirv: $ByteBuffer$Type, stats: $PipelineStateCache$Stats$Type, pw: $PrintWriter$Type): integer
public static "glFormatBytesPerBlock"(format: integer): integer
public static "glFormatStencilBits"(format: integer): integer
public static "glCompileAndAttachShader"(program: integer, shaderType: integer, source: string, stats: $PipelineStateCache$Stats$Type, pw: $PrintWriter$Type): integer
public static "glFormatCompressionType"(format: integer): integer
public static "glCompileShader"(shaderType: integer, source: $ByteBuffer$Type, stats: $PipelineStateCache$Stats$Type, pw: $PrintWriter$Type): integer
public static "getDebugType"(type: integer): string
public static "getDebugSource"(source: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLCore$Type = ($GLCore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLCore_ = $GLCore$Type;
}}
declare module "packages/icyllis/arc3d/core/$PathStroker" {
import {$PathConsumer, $PathConsumer$Type} from "packages/icyllis/arc3d/core/$PathConsumer"

export class $PathStroker implements $PathConsumer {

constructor()

public "init"(out: $PathConsumer$Type, radius: float, cap: integer, join: integer, miterLimit: float, resScale: float): void
public "moveTo"(x: float, y: float): void
public "lineTo"(x: float, y: float): void
public "quadTo"(x1: float, y1: float, x2: float, y2: float): void
public "closePath"(): void
public "pathDone"(): void
public "cubicTo"(x1: float, y1: float, x2: float, y2: float, x3: float, y3: float): void
public "quadTo"(pts: (float)[], off: integer): void
public "cubicTo"(pts: (float)[], off: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathStroker$Type = ($PathStroker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathStroker_ = $PathStroker$Type;
}}
declare module "packages/icyllis/arc3d/engine/$StencilFaceSettings" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StencilFaceSettings {
 "mRef": short
 "mTest": short
 "mTestMask": short
 "mPassOp": byte
 "mFailOp": byte
 "mWriteMask": short

constructor()
constructor(ref: short, test: short, testMask: short, passOp: byte, failOp: byte, writeMask: short)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StencilFaceSettings$Type = ($StencilFaceSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StencilFaceSettings_ = $StencilFaceSettings$Type;
}}
declare module "packages/icyllis/arc3d/engine/$BackendFormat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BackendFormat {

constructor()

public "getGLFormat"(): integer
public "getStencilBits"(): integer
public "getCompressionType"(): integer
public "getBytesPerBlock"(): integer
public "makeInternal"(): $BackendFormat
public "getChannelFlags"(): integer
public "isSRGB"(): boolean
public "getFormatKey"(): integer
public "getVkFormat"(): integer
public "isExternal"(): boolean
public "getBackend"(): integer
public "isCompressed"(): boolean
get "gLFormat"(): integer
get "stencilBits"(): integer
get "compressionType"(): integer
get "bytesPerBlock"(): integer
get "channelFlags"(): integer
get "sRGB"(): boolean
get "formatKey"(): integer
get "vkFormat"(): integer
get "external"(): boolean
get "backend"(): integer
get "compressed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BackendFormat$Type = ($BackendFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BackendFormat_ = $BackendFormat$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$VariableDecl" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Modifiers, $Modifiers$Type} from "packages/icyllis/arc3d/compiler/tree/$Modifiers"
import {$Variable, $Variable$Type} from "packages/icyllis/arc3d/compiler/tree/$Variable"

export class $VariableDecl extends $Statement {
 "mPosition": integer

constructor(variable: $Variable$Type, init: $Expression$Type)

public "toString"(): string
public static "convert"(context: $Context$Type, pos: integer, modifiers: $Modifiers$Type, type: $Type$Type, name: string, storage: byte, init: $Expression$Type): $VariableDecl
public static "convert"(context: $Context$Type, variable: $Variable$Type, init: $Expression$Type): $VariableDecl
public static "make"(variable: $Variable$Type, init: $Expression$Type): $VariableDecl
public "accept"(visitor: $TreeVisitor$Type): boolean
public static "checkError"(pos: integer, modifiers: $Modifiers$Type, type: $Type$Type, baseType: $Type$Type, storage: byte): void
public "getVariable"(): $Variable
public "setVariable"(variable: $Variable$Type): void
public "setInit"(init: $Expression$Type): void
public "getInit"(): $Expression
public "getKind"(): $Node$StatementKind
get "variable"(): $Variable
set "variable"(value: $Variable$Type)
set "init"(value: $Expression$Type)
get "init"(): $Expression
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableDecl$Type = ($VariableDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableDecl_ = $VariableDecl$Type;
}}
declare module "packages/icyllis/arc3d/core/effects/$BlendModeColorFilter" {
import {$ColorFilter, $ColorFilter$Type} from "packages/icyllis/arc3d/core/$ColorFilter"
import {$BlendMode, $BlendMode$Type} from "packages/icyllis/arc3d/core/$BlendMode"

export class $BlendModeColorFilter extends $ColorFilter {

constructor(color: (float)[], srcIsPremul: boolean, mode: $BlendMode$Type)
constructor(color: integer, mode: $BlendMode$Type)

public static "make"(color: (float)[], srcIsPremul: boolean, mode: $BlendMode$Type): $BlendModeColorFilter
public static "make"(color: integer, mode: $BlendMode$Type): $BlendModeColorFilter
public "getColor4f"(): (float)[]
public "getMode"(): $BlendMode
public "filterColor4f"(col: (float)[], out: (float)[]): void
public "isAlphaUnchanged"(): boolean
get "color4f"(): (float)[]
get "mode"(): $BlendMode
get "alphaUnchanged"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlendModeColorFilter$Type = ($BlendModeColorFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlendModeColorFilter_ = $BlendModeColorFilter$Type;
}}
declare module "packages/icyllis/arc3d/engine/$DstProxyView" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
/**
 * 
 * @deprecated
 */
export class $DstProxyView {
static readonly "REQUIRES_TEXTURE_BARRIER_FLAG": integer
static readonly "AS_INPUT_ATTACHMENT_FLAG": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DstProxyView$Type = ($DstProxyView);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DstProxyView_ = $DstProxyView$Type;
}}
declare module "packages/icyllis/arc3d/engine/$FlushInfo$FinishedCallback" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FlushInfo$FinishedCallback {

 "onFinished"(): void

(): void
}

export namespace $FlushInfo$FinishedCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlushInfo$FinishedCallback$Type = ($FlushInfo$FinishedCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlushInfo$FinishedCallback_ = $FlushInfo$FinishedCallback$Type;
}}
declare module "packages/icyllis/arc3d/core/$RasterDevice" {
import {$Paint, $Paint$Type} from "packages/icyllis/arc3d/core/$Paint"
import {$Device, $Device$Type} from "packages/icyllis/arc3d/core/$Device"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"

export class $RasterDevice extends $Device {

constructor(info: $ImageInfo$Type)

public "drawRect"(r: $Rect2f$Type, paint: $Paint$Type): void
public "clipIsAA"(): boolean
public "clipIsWideOpen"(): boolean
public "drawPaint"(paint: $Paint$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RasterDevice$Type = ($RasterDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RasterDevice_ = $RasterDevice$Type;
}}
declare module "packages/icyllis/arc3d/engine/effects/$BlendFormula" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BlendFormula {
static readonly "OUTPUT_TYPE_ZERO": integer
static readonly "OUTPUT_TYPE_COVERAGE": integer
static readonly "OUTPUT_TYPE_MODULATE": integer
static readonly "OUTPUT_TYPE_SRC_ALPHA_MODULATE": integer
static readonly "OUTPUT_TYPE_ONE_MINUS_SRC_ALPHA_MODULATE": integer
static readonly "OUTPUT_TYPE_ONE_MINUS_SRC_COLOR_MODULATE": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlendFormula$Type = ($BlendFormula);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlendFormula_ = $BlendFormula$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$ClearOp" {
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$Op, $Op$Type} from "packages/icyllis/arc3d/engine/ops/$Op"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"

export class $ClearOp extends $Op {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float


public static "makeStencil"(left: integer, top: integer, right: integer, bottom: integer, insideMask: boolean): $Op
public "onPrePrepare"(context: $RecordingContext$Type, writeView: $SurfaceView$Type, pipelineFlags: integer): void
public static "makeColor"(left: integer, top: integer, right: integer, bottom: integer, red: float, green: float, blue: float, alpha: float): $Op
public "onPrepare"(state: $OpFlushState$Type, writeView: $SurfaceView$Type, pipelineFlags: integer): void
public "onExecute"(state: $OpFlushState$Type, chainBounds: $Rect2f$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClearOp$Type = ($ClearOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClearOp_ = $ClearOp$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorArray" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorArray extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, type: $Type$Type, arg3: $List$Type<($Expression$Type)>): $Expression
public static "make"(position: integer, type: $Type$Type, arg2: ($Expression$Type)[]): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorArray$Type = ($ConstructorArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorArray_ = $ConstructorArray$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$BufferWriter" {
import {$Writer, $Writer$Type} from "packages/icyllis/arc3d/compiler/spirv/$Writer"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $BufferWriter implements $Writer {

constructor(size: integer)

public "detach"(): $ByteBuffer
public "writeString8"(context: $Context$Type, s: string): void
public "writeWords"(words: (integer)[], n: integer): void
public "writeWord"(word: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferWriter$Type = ($BufferWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferWriter_ = $BufferWriter$Type;
}}
declare module "packages/icyllis/arc3d/compiler/glsl/$GLSLCodeGenerator" {
import {$GLSLVersion, $GLSLVersion$Type} from "packages/icyllis/arc3d/compiler/$GLSLVersion"
import {$TranslationUnit, $TranslationUnit$Type} from "packages/icyllis/arc3d/compiler/tree/$TranslationUnit"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/compiler/$ShaderCaps"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$CodeGenerator, $CodeGenerator$Type} from "packages/icyllis/arc3d/compiler/$CodeGenerator"
import {$ShaderCompiler, $ShaderCompiler$Type} from "packages/icyllis/arc3d/compiler/$ShaderCompiler"
import {$TargetApi, $TargetApi$Type} from "packages/icyllis/arc3d/compiler/$TargetApi"

export class $GLSLCodeGenerator extends $CodeGenerator {
readonly "mOutputTarget": $TargetApi
readonly "mOutputVersion": $GLSLVersion

constructor(compiler: $ShaderCompiler$Type, translationUnit: $TranslationUnit$Type, shaderCaps: $ShaderCaps$Type)

public "generateCode"(): $ByteBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLSLCodeGenerator$Type = ($GLSLCodeGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLSLCodeGenerator_ = $GLSLCodeGenerator$Type;
}}
declare module "packages/icyllis/arc3d/engine/geom/$DefaultGeoProc" {
import {$GeometryProcessor$AttributeSet, $GeometryProcessor$AttributeSet$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$AttributeSet"
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"

export class $DefaultGeoProc extends $GeometryProcessor {
static readonly "FLAG_COLOR_ATTRIBUTE": integer
static readonly "FLAG_TEX_COORD_ATTRIBUTE": integer
static readonly "POSITION": $GeometryProcessor$Attribute
static readonly "COLOR": $GeometryProcessor$Attribute
static readonly "TEX_COORD": $GeometryProcessor$Attribute
static readonly "VERTEX_ATTRIBS": $GeometryProcessor$AttributeSet
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer

constructor(flags: integer)

public "name"(): string
public "primitiveType"(): byte
public "makeProgramImpl"(caps: $ShaderCaps$Type): $GeometryProcessor$ProgramImpl
public "appendToKey"(b: $KeyBuilder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGeoProc$Type = ($DefaultGeoProc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGeoProc_ = $DefaultGeoProc$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanBuffer" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$VulkanDevice, $VulkanDevice$Type} from "packages/icyllis/arc3d/vulkan/$VulkanDevice"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $VulkanBuffer extends $GpuBuffer {
static readonly "kRead_LockMode": integer
static readonly "kWriteDiscard_LockMode": integer

constructor(device: $VulkanDevice$Type)

public "isLocked"(): boolean
public "getLockedBuffer"(): long
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "locked"(): boolean
get "lockedBuffer"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanBuffer$Type = ($VulkanBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanBuffer_ = $VulkanBuffer$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$GradientShader" {
import {$Shader, $Shader$Type} from "packages/icyllis/arc3d/core/$Shader"

export class $GradientShader extends $Shader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GradientShader$Type = ($GradientShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GradientShader_ = $GradientShader$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ISurface" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"

export interface $ISurface extends $RefCounted {

 "ref"(): void
 "getBackendFormat"(): $BackendFormat
 "getWidth"(): integer
 "getHeight"(): integer
 "getSampleCount"(): integer
 "unref"(): void
}

export namespace $ISurface {
const FLAG_NONE: integer
const FLAG_BUDGETED: integer
const FLAG_APPROX_FIT: integer
const FLAG_MIPMAPPED: integer
const FLAG_RENDERABLE: integer
const FLAG_PROTECTED: integer
const FLAG_READ_ONLY: integer
const FLAG_SKIP_ALLOCATOR: integer
const FLAG_DEFERRED_PROVIDER: integer
const FLAG_GL_WRAP_DEFAULT_FB: integer
const FLAG_MANUAL_MSAA_RESOLVE: integer
const FLAG_VK_WRAP_SECONDARY_CB: integer
function getApproxSize(size: integer): integer
function create<T>(that: T): T
function create<T>(sp: T, that: T): T
function move<T>(sp: T): T
function move<T>(sp: T, that: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISurface$Type = ($ISurface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISurface_ = $ISurface$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLTextureInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GLTextureInfo {
 "target": integer
 "handle": integer
 "format": integer
 "levels": integer
 "samples": integer
 "memoryObject": integer
 "memoryHandle": integer

constructor()

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "set"(info: $GLTextureInfo$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLTextureInfo$Type = ($GLTextureInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLTextureInfo_ = $GLTextureInfo$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Caps$BlendEquationSupport" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Caps$BlendEquationSupport extends $Enum<($Caps$BlendEquationSupport)> {
static readonly "BASIC": $Caps$BlendEquationSupport
static readonly "ADVANCED": $Caps$BlendEquationSupport
static readonly "ADVANCED_COHERENT": $Caps$BlendEquationSupport


public static "values"(): ($Caps$BlendEquationSupport)[]
public static "valueOf"(name: string): $Caps$BlendEquationSupport
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Caps$BlendEquationSupport$Type = (("advanced") | ("advanced_coherent") | ("basic")) | ($Caps$BlendEquationSupport);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Caps$BlendEquationSupport_ = $Caps$BlendEquationSupport$Type;
}}
declare module "packages/icyllis/arc3d/engine/$MeshDrawTarget" {
import {$Mesh, $Mesh$Type} from "packages/icyllis/arc3d/engine/$Mesh"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $MeshDrawTarget {

 "makeIndexSpace"(arg0: $Mesh$Type): long
 "makeIndexWriter"(arg0: $Mesh$Type): $ByteBuffer
 "makeVertexSpace"(arg0: $Mesh$Type): long
 "makeInstanceSpace"(arg0: $Mesh$Type): long
 "makeInstanceWriter"(arg0: $Mesh$Type): $ByteBuffer
 "makeVertexWriter"(arg0: $Mesh$Type): $ByteBuffer
}

export namespace $MeshDrawTarget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeshDrawTarget$Type = ($MeshDrawTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeshDrawTarget_ = $MeshDrawTarget$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanAllocation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $VulkanAllocation {
static readonly "VISIBLE_FLAG": integer
static readonly "COHERENT_FLAG": integer
static readonly "LAZILY_ALLOCATED_FLAG": integer
 "mMemory": long
 "mOffset": long
 "mSize": long
 "mMemoryFlags": integer
 "mAllocation": long

constructor()

public "equals"(o: any): boolean
public "hashCode"(): integer
public "set"(alloc: $VulkanAllocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanAllocation$Type = ($VulkanAllocation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanAllocation_ = $VulkanAllocation$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$AngularGradient" {
import {$UnivariateGradientShader, $UnivariateGradientShader$Type} from "packages/icyllis/arc3d/core/shaders/$UnivariateGradientShader"

export class $AngularGradient extends $UnivariateGradientShader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AngularGradient$Type = ($AngularGradient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AngularGradient_ = $AngularGradient$Type;
}}
declare module "packages/icyllis/arc3d/engine/image/$TextureImage" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$Image, $Image$Type} from "packages/icyllis/arc3d/core/$Image"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"

export class $TextureImage extends $Image {

constructor(rContext: $RecordingContext$Type, proxy: $TextureProxy$Type, swizzle: short, origin: integer, colorType: integer, alphaType: integer, colorSpace: $ColorSpace$Type)

public "isTextureBacked"(): boolean
public "getContext"(): $RecordingContext
public "isValid"(context: $RecordingContext$Type): boolean
public "getTextureMemorySize"(): long
get "textureBacked"(): boolean
get "context"(): $RecordingContext
get "textureMemorySize"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureImage$Type = ($TextureImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureImage_ = $TextureImage$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$Instruction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Instruction {


public "equals"(o: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Instruction$Type = ($Instruction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Instruction_ = $Instruction$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$FragmentShaderBuilder" {
import {$ShaderBuilderBase, $ShaderBuilderBase$Type} from "packages/icyllis/arc3d/engine/shading/$ShaderBuilderBase"
import {$XPFragmentBuilder, $XPFragmentBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$XPFragmentBuilder"
import {$PipelineBuilder, $PipelineBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$PipelineBuilder"
import {$FPFragmentBuilder, $FPFragmentBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$FPFragmentBuilder"

export class $FragmentShaderBuilder extends $ShaderBuilderBase implements $FPFragmentBuilder, $XPFragmentBuilder {
static readonly "MAIN_DRAW_BUFFER_INDEX": integer
static readonly "PRIMARY_COLOR_OUTPUT_INDEX": integer
static readonly "SECONDARY_COLOR_OUTPUT_INDEX": integer
static readonly "PRIMARY_COLOR_OUTPUT_NAME": string
static readonly "SECONDARY_COLOR_OUTPUT_NAME": string

constructor(pipelineBuilder: $PipelineBuilder$Type)

public "enableSecondaryOutput"(): void
public "getSecondaryColorOutputName"(): string
get "secondaryColorOutputName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FragmentShaderBuilder$Type = ($FragmentShaderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FragmentShaderBuilder_ = $FragmentShaderBuilder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Key$StorageKey" {
import {$Key, $Key$Type} from "packages/icyllis/arc3d/engine/$Key"
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"

export class $Key$StorageKey implements $Key {

constructor(b: $KeyBuilder$Type)

public "equals"(o: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Key$StorageKey$Type = ($Key$StorageKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Key$StorageKey_ = $Key$StorageKey$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanTexture" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$VulkanDevice, $VulkanDevice$Type} from "packages/icyllis/arc3d/vulkan/$VulkanDevice"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"

export class $VulkanTexture extends $GpuTexture {

constructor(device: $VulkanDevice$Type, width: integer, height: integer)

public "getBackendFormat"(): $BackendFormat
public "getMemorySize"(): long
public "getSampleCount"(): integer
public "getMaxMipmapLevel"(): integer
public "getBackendTexture"(): $BackendTexture
public "isExternal"(): boolean
public "asRenderTarget"(): $GpuRenderTarget
public static "getApproxSize"(size: integer): integer
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "backendFormat"(): $BackendFormat
get "memorySize"(): long
get "sampleCount"(): integer
get "maxMipmapLevel"(): integer
get "backendTexture"(): $BackendTexture
get "external"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanTexture$Type = ($VulkanTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanTexture_ = $VulkanTexture$Type;
}}
declare module "packages/icyllis/arc3d/engine/$BackendTexture" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$VulkanImageInfo, $VulkanImageInfo$Type} from "packages/icyllis/arc3d/vulkan/$VulkanImageInfo"
import {$GLTextureInfo, $GLTextureInfo$Type} from "packages/icyllis/arc3d/opengl/$GLTextureInfo"

export class $BackendTexture {


public "isProtected"(): boolean
public "getBackendFormat"(): $BackendFormat
public "getWidth"(): integer
public "getHeight"(): integer
public "glTextureParametersModified"(): void
public "setVkQueueFamilyIndex"(queueFamilyIndex: integer): void
public "isMipmapped"(): boolean
public "getGLTextureInfo"(info: $GLTextureInfo$Type): boolean
public "isExternal"(): boolean
public "getBackend"(): integer
public "setVkImageLayout"(layout: integer): void
public "getVkImageInfo"(info: $VulkanImageInfo$Type): boolean
public "isSameTexture"(arg0: $BackendTexture$Type): boolean
get "protected"(): boolean
get "backendFormat"(): $BackendFormat
get "width"(): integer
get "height"(): integer
set "vkQueueFamilyIndex"(value: integer)
get "mipmapped"(): boolean
get "external"(): boolean
get "backend"(): integer
set "vkImageLayout"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BackendTexture$Type = ($BackendTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BackendTexture_ = $BackendTexture$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceDrawContext" {
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$Clip, $Clip$Type} from "packages/icyllis/arc3d/engine/$Clip"
import {$DrawOp, $DrawOp$Type} from "packages/icyllis/arc3d/engine/ops/$DrawOp"
import {$SurfaceFillContext, $SurfaceFillContext$Type} from "packages/icyllis/arc3d/engine/$SurfaceFillContext"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"

export class $SurfaceDrawContext extends $SurfaceFillContext {

constructor(context: $RecordingContext$Type, readView: $SurfaceView$Type, writeView: $SurfaceView$Type, colorType: integer, colorSpace: $ColorSpace$Type)

public static "make"(rContext: $RecordingContext$Type, colorType: integer, colorSpace: $ColorSpace$Type, surfaceProxy: $SurfaceProxy$Type, origin: integer): $SurfaceDrawContext
public static "make"(rContext: $RecordingContext$Type, colorType: integer, colorSpace: $ColorSpace$Type, width: integer, height: integer, sampleCount: integer, surfaceFlags: integer, origin: integer): $SurfaceDrawContext
public "fillRect"(clip: $Clip$Type, color: integer, rect: $Rect2f$Type, viewMatrix: $Matrixc$Type, aa: boolean): void
public "addDrawOp"(clip: $Clip$Type, op: $DrawOp$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceDrawContext$Type = ($SurfaceDrawContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceDrawContext_ = $SurfaceDrawContext$Type;
}}
declare module "packages/icyllis/arc3d/core/$Kernel32" {
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$SharedLibrary, $SharedLibrary$Type} from "packages/org/lwjgl/system/$SharedLibrary"

export class $Kernel32 {
static readonly "HANDLE_FLAG_INHERIT": integer
static readonly "HANDLE_FLAG_PROTECT_FROM_CLOSE": integer

constructor()

public static "AddDllDirectory"(NewDirectory: string): long
public static "CloseHandle"(hObject: long): boolean
public static "getLibrary"(): $SharedLibrary
public static "GetLastError"(): integer
public static "GetHandleInformation"(hObject: long, lpdwFlags: $IntBuffer$Type): boolean
get "library"(): $SharedLibrary
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Kernel32$Type = ($Kernel32);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Kernel32_ = $Kernel32$Type;
}}
declare module "packages/icyllis/arc3d/engine/$CpuBuffer" {
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"

export class $CpuBuffer extends $RefCnt {

constructor(size: integer)

public "size"(): integer
public "data"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CpuBuffer$Type = ($CpuBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CpuBuffer_ = $CpuBuffer$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GeometryProcessor" {
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"
import {$Processor, $Processor$Type} from "packages/icyllis/arc3d/engine/$Processor"

export class $GeometryProcessor extends $Processor {
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer


public "primitiveType"(): byte
public "vertexStride"(): integer
public "instanceAttributes"(): $Iterable<($GeometryProcessor$Attribute)>
public "numVertexLocations"(): integer
public "instanceStride"(): integer
public "vertexAttributes"(): $Iterable<($GeometryProcessor$Attribute)>
public "makeProgramImpl"(arg0: $ShaderCaps$Type): $GeometryProcessor$ProgramImpl
public "appendToKey"(arg0: $KeyBuilder$Type): void
public "appendAttributesToKey"(b: $KeyBuilder$Type): void
public "textureSamplerState"(i: integer): integer
public "textureSamplerSwizzle"(i: integer): short
public "numTextureSamplers"(): integer
public "hasVertexAttributes"(): boolean
public "numInstanceLocations"(): integer
public "numVertexAttributes"(): integer
public "hasInstanceAttributes"(): boolean
public "numInstanceAttributes"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeometryProcessor$Type = ($GeometryProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeometryProcessor_ = $GeometryProcessor$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceProxy" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$ISurface, $ISurface$Type} from "packages/icyllis/arc3d/engine/$ISurface"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $SurfaceProxy extends $RefCnt implements $ISurface {


public "equals"(o: any): boolean
public "hashCode"(): integer
public "clear"(): void
public "isProtected"(): boolean
public "isReadOnly"(): boolean
public "isExact"(): boolean
public "getBackendFormat"(): $BackendFormat
public "isBackingWrapped"(): boolean
public "getBackingUniqueID"(): any
public "wrapsVkSecondaryCB"(): boolean
public "getGpuSurface"(): $IGpuSurface
public "isUserExact"(): boolean
public "getTaskTargetCount"(): integer
public "wrapsGLDefaultFB"(): boolean
public "instantiate"(arg0: $ResourceProvider$Type): boolean
public "getBackingWidth"(): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "getMemorySize"(): long
public "getSampleCount"(): integer
public "asTexture"(): $TextureProxy
public "getGpuRenderTarget"(): $GpuRenderTarget
public "getUniqueID"(): any
public "doLazyInstantiation"(arg0: $ResourceProvider$Type): boolean
public "shouldSkipAllocator"(): boolean
public "isManualMSAAResolve"(): boolean
public "getGpuTexture"(): $GpuTexture
public "isInstantiated"(): boolean
public "isUsedAsTaskTarget"(): void
public "getBackingHeight"(): integer
public "isLazyMost"(): boolean
public "isBudgeted"(): boolean
public "isLazy"(): boolean
public "setIsDeferredListTarget"(): void
public "isDeferredListTarget"(): boolean
public "ref"(): void
public static "getApproxSize"(size: integer): integer
public "unref"(): void
get "protected"(): boolean
get "readOnly"(): boolean
get "exact"(): boolean
get "backendFormat"(): $BackendFormat
get "backingWrapped"(): boolean
get "backingUniqueID"(): any
get "gpuSurface"(): $IGpuSurface
get "userExact"(): boolean
get "taskTargetCount"(): integer
get "backingWidth"(): integer
get "width"(): integer
get "height"(): integer
get "memorySize"(): long
get "sampleCount"(): integer
get "gpuRenderTarget"(): $GpuRenderTarget
get "uniqueID"(): any
get "manualMSAAResolve"(): boolean
get "gpuTexture"(): $GpuTexture
get "instantiated"(): boolean
get "usedAsTaskTarget"(): boolean
get "backingHeight"(): integer
get "lazyMost"(): boolean
get "budgeted"(): boolean
get "lazy"(): boolean
get "deferredListTarget"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceProxy$Type = ($SurfaceProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceProxy_ = $SurfaceProxy$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLPipelineStateCache" {
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$PipelineStateCache, $PipelineStateCache$Type} from "packages/icyllis/arc3d/engine/$PipelineStateCache"

export class $GLPipelineStateCache extends $PipelineStateCache {

constructor(device: $GLDevice$Type, cacheSize: integer)

public "close"(): void
public "release"(): void
public "discard"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLPipelineStateCache$Type = ($GLPipelineStateCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLPipelineStateCache_ = $GLPipelineStateCache$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$FPFragmentBuilder" {
import {$ShaderBuilder, $ShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$ShaderBuilder"

export interface $FPFragmentBuilder extends $ShaderBuilder {

 "getMangledName"(arg0: string): string
 "codeAppend"(arg0: string): void
 "codeAppendf"(arg0: string, ...arg1: (any)[]): void
 "codePrependf"(arg0: string, ...arg1: (any)[]): void
}

export namespace $FPFragmentBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FPFragmentBuilder$Type = ($FPFragmentBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FPFragmentBuilder_ = $FPFragmentBuilder$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Type" {
import {$Type$Field, $Type$Field$Type} from "packages/icyllis/arc3d/compiler/tree/$Type$Field"
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Node$SymbolKind, $Node$SymbolKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$SymbolKind"
import {$Symbol, $Symbol$Type} from "packages/icyllis/arc3d/compiler/tree/$Symbol"

export class $Type extends $Symbol {
static readonly "kUnsizedArray": integer
static readonly "kMaxNestingDepth": integer
static readonly "kArray_TypeKind": byte
static readonly "kGeneric_TypeKind": byte
static readonly "kMatrix_TypeKind": byte
static readonly "kOther_TypeKind": byte
static readonly "kSampler_TypeKind": byte
static readonly "kScalar_TypeKind": byte
static readonly "kStruct_TypeKind": byte
static readonly "kVector_TypeKind": byte
static readonly "kVoid_TypeKind": byte
static readonly "kFloat_ScalarKind": byte
static readonly "kSigned_ScalarKind": byte
static readonly "kUnsigned_ScalarKind": byte
static readonly "kBoolean_ScalarKind": byte
static readonly "kNonScalar_ScalarKind": byte
 "mPosition": integer


public "toString"(): string
public "isArray"(): boolean
public "matches"(other: $Type$Type): boolean
public "getComponentType"(): $Type
public "getFields"(): ($Type$Field)[]
public "resolve"(): $Type
public "getType"(): $Type
public "isVoid"(): boolean
public "isGeneric"(): boolean
public "isOpaque"(): boolean
public "isSigned"(): boolean
public "isInteger"(): boolean
public "isFloat"(): boolean
public "getDesc"(): string
public "getDimensions"(): integer
public "getElementType"(): $Type
public "isNumeric"(): boolean
public "isUnsigned"(): boolean
public "isBoolean"(): boolean
public "getWidth"(): integer
public "getRows"(): integer
public "isRelaxedPrecision"(): boolean
public "isInterfaceBlock"(): boolean
public "getCols"(): integer
public "isMatrix"(): boolean
public "isStruct"(): boolean
public "getArraySize"(): integer
public "getTypeKind"(): byte
public "isUnsizedArray"(): boolean
public "getScalarKind"(): byte
public "isVector"(): boolean
public "isScalar"(): boolean
public "isSignedOrCompound"(): boolean
public "isFloatOrCompound"(): boolean
public "getCoercionCost"(other: $Type$Type): long
public static "makeAliasType"(name: string, type: $Type$Type): $Type
public static "makeMatrixType"(name: string, desc: string, columnType: $Type$Type, cols: integer): $Type
public static "makeScalarType"(name: string, desc: string, kind: byte, rank: integer, width: integer): $Type
public static "makeScalarType"(name: string, desc: string, kind: byte, rank: integer, minWidth: integer, width: integer): $Type
public static "makeVectorType"(name: string, desc: string, componentType: $Type$Type, rows: integer): $Type
public "getRank"(): integer
public static "makeCombinedType"(name: string, abbr: string, component: $Type$Type, dimensions: integer, isShadow: boolean, isArrayed: boolean, isMultiSampled: boolean): $Type
public "getNestingDepth"(): integer
public "isInBuiltinTypes"(): boolean
public static "makeSeparateType"(name: string, abbr: string, component: $Type$Type, isShadow: boolean): $Type
public static "makeSamplerType"(name: string, abbr: string, component: $Type$Type, dimensions: integer, isShadow: boolean, isArrayed: boolean, isMultiSampled: boolean, isSampled: boolean, isSampler: boolean): $Type
public static "makeImageType"(name: string, abbr: string, component: $Type$Type, dimensions: integer, isArrayed: boolean, isMultiSampled: boolean): $Type
public "isSampled"(): boolean
public "isMultiSampled"(): boolean
public static "makeStructType"(context: $Context$Type, position: integer, name: string, fields: $List$Type<($Type$Field$Type)>, interfaceBlock: boolean): $Type
public static "makeTextureType"(name: string, abbr: string, component: $Type$Type, dimensions: integer, isArrayed: boolean, isMultiSampled: boolean): $Type
public static "makeSpecialType"(name: string, abbr: string, kind: byte): $Type
public "canCoerceTo"(other: $Type$Type, allowNarrowing: boolean): boolean
public "isArrayed"(): boolean
public "isUsableInArray"(context: $Context$Type, position: integer): boolean
public "coerceExpression"(context: $Context$Type, expr: $Expression$Type): $Expression
public "isSeparateSampler"(): boolean
public "toVector"(context: $Context$Type, rows: integer): $Type
public "getCoercibleTypes"(): ($Type)[]
public "toCompound"(context: $Context$Type, cols: integer, rows: integer): $Type
public "isCombinedSampler"(): boolean
public "isStorageImage"(): boolean
public "getArrayName"(size: integer): string
public "convertArraySize"(context: $Context$Type, position: integer, size: $Expression$Type): integer
public "convertArraySize"(context: $Context$Type, position: integer, sizePosition: integer, size: long): integer
public "getMinWidth"(): integer
public "isShadow"(): boolean
public static "makeArrayType"(name: string, type: $Type$Type, size: integer): $Type
public "getMinValue"(): double
public "getMaxValue"(): double
public "checkLiteralOutOfRange"(context: $Context$Type, pos: integer, value: double): boolean
public "isBooleanOrCompound"(): boolean
public "isUnsignedOrCompound"(): boolean
public "getComponents"(): integer
public static "makeGenericType"(name: string, ...types: ($Type$Type)[]): $Type
public "getKind"(): $Node$SymbolKind
get "array"(): boolean
get "componentType"(): $Type
get "fields"(): ($Type$Field)[]
get "type"(): $Type
get "void"(): boolean
get "generic"(): boolean
get "opaque"(): boolean
get "signed"(): boolean
get "integer"(): boolean
get "float"(): boolean
get "desc"(): string
get "dimensions"(): integer
get "elementType"(): $Type
get "numeric"(): boolean
get "unsigned"(): boolean
get "boolean"(): boolean
get "width"(): integer
get "rows"(): integer
get "relaxedPrecision"(): boolean
get "interfaceBlock"(): boolean
get "cols"(): integer
get "matrix"(): boolean
get "struct"(): boolean
get "arraySize"(): integer
get "typeKind"(): byte
get "unsizedArray"(): boolean
get "scalarKind"(): byte
get "vector"(): boolean
get "scalar"(): boolean
get "signedOrCompound"(): boolean
get "floatOrCompound"(): boolean
get "rank"(): integer
get "nestingDepth"(): integer
get "inBuiltinTypes"(): boolean
get "sampled"(): boolean
get "multiSampled"(): boolean
get "arrayed"(): boolean
get "separateSampler"(): boolean
get "coercibleTypes"(): ($Type)[]
get "combinedSampler"(): boolean
get "storageImage"(): boolean
get "minWidth"(): integer
get "shadow"(): boolean
get "minValue"(): double
get "maxValue"(): double
get "booleanOrCompound"(): boolean
get "unsignedOrCompound"(): boolean
get "components"(): integer
get "kind"(): $Node$SymbolKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Type$Type = ($Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Type_ = $Type$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Layout" {
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $Layout {
static readonly "kOriginUpperLeft_LayoutFlag": integer
static readonly "kPixelCenterInteger_LayoutFlag": integer
static readonly "kEarlyFragmentTests_LayoutFlag": integer
static readonly "kBlendSupportAllEquations_LayoutFlag": integer
static readonly "kPushConstant_LayoutFlag": integer
static readonly "kLocation_LayoutFlag": integer
static readonly "kComponent_LayoutFlag": integer
static readonly "kIndex_LayoutFlag": integer
static readonly "kBinding_LayoutFlag": integer
static readonly "kOffset_LayoutFlag": integer
static readonly "kSet_LayoutFlag": integer
static readonly "kInputAttachmentIndex_LayoutFlag": integer
static readonly "kBuiltin_LayoutFlag": integer
static readonly "kCount_LayoutFlag": integer
 "mLocation": integer
 "mComponent": integer
 "mIndex": integer
 "mBinding": integer
 "mOffset": integer
 "mSet": integer
 "mInputAttachmentIndex": integer
 "mBuiltin": integer

constructor()

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "layoutFlags"(): integer
public "checkLayoutFlags"(context: $Context$Type, pos: integer, permittedLayoutFlags: integer): boolean
public static "describeLayoutFlag"(flag: integer): string
public "setLayoutFlag"(context: $Context$Type, mask: integer, name: string, pos: integer): void
public "clearLayoutFlag"(mask: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Layout$Type = ($Layout);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Layout_ = $Layout$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$RoundRectOp" {
import {$MeshDrawOp, $MeshDrawOp$Type} from "packages/icyllis/arc3d/engine/ops/$MeshDrawOp"
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$Matrix, $Matrix$Type} from "packages/icyllis/arc3d/core/$Matrix"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $RoundRectOp extends $MeshDrawOp {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor(color: (float)[], localRect: $Rect2f$Type, cornerRadius: float, strokeRadius: float, viewMatrix: $Matrix$Type, stroke: boolean)

public "setInstanceBuffer"(buffer: $GpuBuffer$Type, baseInstance: integer, actualInstanceCount: integer): void
public "getInstanceCount"(): integer
public "getVertexCount"(): integer
public "setVertexBuffer"(buffer: $GpuBuffer$Type, baseVertex: integer, actualVertexCount: integer): void
public "onExecute"(state: $OpFlushState$Type, chainBounds: $Rect2f$Type): void
get "instanceCount"(): integer
get "vertexCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RoundRectOp$Type = ($RoundRectOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RoundRectOp_ = $RoundRectOp$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ShaderCaps" {
import {$GLSLVersion, $GLSLVersion$Type} from "packages/icyllis/arc3d/compiler/$GLSLVersion"
import {$SPIRVVersion, $SPIRVVersion$Type} from "packages/icyllis/arc3d/compiler/$SPIRVVersion"
import {$TargetApi, $TargetApi$Type} from "packages/icyllis/arc3d/compiler/$TargetApi"

export class $ShaderCaps {
 "mTargetApi": $TargetApi
 "mGLSLVersion": $GLSLVersion
 "mSPIRVVersion": $SPIRVVersion

constructor()

public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderCaps$Type = ($ShaderCaps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderCaps_ = $ShaderCaps$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Clip" {
import {$ClipResult, $ClipResult$Type} from "packages/icyllis/arc3d/engine/$ClipResult"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$SurfaceDrawContext, $SurfaceDrawContext$Type} from "packages/icyllis/arc3d/engine/$SurfaceDrawContext"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"

export class $Clip {
static readonly "CLIPPED": integer
static readonly "NOT_CLIPPED": integer
static readonly "CLIPPED_OUT": integer
static readonly "kBoundsTolerance": float
static readonly "kHalfPixelRoundingTolerance": float

constructor()

public "apply"(arg0: $SurfaceDrawContext$Type, arg1: boolean, arg2: $ClipResult$Type, arg3: $Rect2f$Type): integer
public static "getPixelBounds"(bounds: $Rect2fc$Type, aa: boolean, exterior: boolean, out: $Rect2i$Type): void
public "getConservativeBounds"(arg0: $Rect2i$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clip$Type = ($Clip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clip_ = $Clip$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Blend" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Blend {
static readonly "FACTOR_ZERO": integer
static readonly "FACTOR_ONE": integer
static readonly "FACTOR_SRC_COLOR": integer
static readonly "FACTOR_ONE_MINUS_SRC_COLOR": integer
static readonly "FACTOR_DST_COLOR": integer
static readonly "FACTOR_ONE_MINUS_DST_COLOR": integer
static readonly "FACTOR_SRC_ALPHA": integer
static readonly "FACTOR_ONE_MINUS_SRC_ALPHA": integer
static readonly "FACTOR_DST_ALPHA": integer
static readonly "FACTOR_ONE_MINUS_DST_ALPHA": integer
static readonly "FACTOR_CONSTANT_COLOR": integer
static readonly "FACTOR_ONE_MINUS_CONSTANT_COLOR": integer
static readonly "FACTOR_CONSTANT_ALPHA": integer
static readonly "FACTOR_ONE_MINUS_CONSTANT_ALPHA": integer
static readonly "FACTOR_SRC_ALPHA_SATURATE": integer
static readonly "FACTOR_SRC1_COLOR": integer
static readonly "FACTOR_ONE_MINUS_SRC1_COLOR": integer
static readonly "FACTOR_SRC1_ALPHA": integer
static readonly "FACTOR_ONE_MINUS_SRC1_ALPHA": integer
static readonly "FACTOR_UNKNOWN": integer
static readonly "EQUATION_ADD": integer
static readonly "EQUATION_SUBTRACT": integer
static readonly "EQUATION_REVERSE_SUBTRACT": integer
static readonly "EQUATION_MULTIPLY": integer
static readonly "EQUATION_SCREEN": integer
static readonly "EQUATION_OVERLAY": integer
static readonly "EQUATION_DARKEN": integer
static readonly "EQUATION_LIGHTEN": integer
static readonly "EQUATION_COLORDODGE": integer
static readonly "EQUATION_COLORBURN": integer
static readonly "EQUATION_HARDLIGHT": integer
static readonly "EQUATION_SOFTLIGHT": integer
static readonly "EQUATION_DIFFERENCE": integer
static readonly "EQUATION_EXCLUSION": integer
static readonly "EQUATION_HSL_HUE": integer
static readonly "EQUATION_HSL_SATURATION": integer
static readonly "EQUATION_HSL_COLOR": integer
static readonly "EQUATION_HSL_LUMINOSITY": integer
static readonly "EQUATION_UNKNOWN": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Blend$Type = ($Blend);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Blend_ = $Blend$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$Inliner" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Inliner {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Inliner$Type = ($Inliner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Inliner_ = $Inliner$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$RadialGradient" {
import {$UnivariateGradientShader, $UnivariateGradientShader$Type} from "packages/icyllis/arc3d/core/shaders/$UnivariateGradientShader"

export class $RadialGradient extends $UnivariateGradientShader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RadialGradient$Type = ($RadialGradient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RadialGradient_ = $RadialGradient$Type;
}}
declare module "packages/icyllis/arc3d/core/$Pair" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $Pair<L, R> implements $Map$Entry<(L), (R)> {

constructor()
constructor(left: L, right: R)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): R
public static "of"<L, R>(entry: $Map$Entry$Type<(L), (R)>): $Pair<(L), (R)>
public static "of"<L, R>(left: L, right: R): $Pair<(L), (R)>
public "getKey"(): L
public "setValue"(value: R): R
public "getFirst"(): L
public "getSecond"(): R
public "getRight"(): R
public "getLeft"(): L
public static "copyOf"<K, V>(arg0: $Map$Entry$Type<(any), (any)>): $Map$Entry<(L), (R)>
public static "comparingByKey"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(L), (R)>)>
public static "comparingByKey"<K extends $Comparable<(any)>, V>(): $Comparator<($Map$Entry<(L), (R)>)>
public static "comparingByValue"<K, V extends $Comparable<(any)>>(): $Comparator<($Map$Entry<(L), (R)>)>
public static "comparingByValue"<K, V>(arg0: $Comparator$Type<(any)>): $Comparator<($Map$Entry<(L), (R)>)>
get "value"(): R
get "key"(): L
set "value"(value: R)
get "first"(): L
get "second"(): R
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
declare module "packages/icyllis/arc3d/core/$Quaternion" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"
import {$Vector3, $Vector3$Type} from "packages/icyllis/arc3d/core/$Vector3"
import {$Matrix3, $Matrix3$Type} from "packages/icyllis/arc3d/core/$Matrix3"

export class $Quaternion {

constructor()
constructor(x: float, y: float, z: float, w: float)

public "add"(q: $Quaternion$Type): void
public "equals"(o: any): boolean
public "length"(): float
public "toString"(): string
public "hashCode"(): integer
public "dot"(q: $Quaternion$Type): float
public "dot"(x: float, y: float, z: float, w: float): float
public static "identity"(): $Quaternion
public "set"(x: float, y: float, z: float, w: float): void
public "set"(q: $Quaternion$Type): void
public "copy"(): $Quaternion
public static "copy"(q: $Quaternion$Type): $Quaternion
public "normalize"(): void
public "isIdentity"(): boolean
public "multiply"(q: $Quaternion$Type): void
public "multiply"(s: float): void
public "negate"(): void
public "subtract"(q: $Quaternion$Type): void
public "isNormalized"(): boolean
public "setIdentity"(): void
public "conjugate"(): void
public "slerp"(a: $Quaternion$Type, t: float): void
public "slerp"(a: $Quaternion$Type, b: $Quaternion$Type, t: float): void
public "inverse"(): void
public "isApproxEqual"(q: $Quaternion$Type): boolean
public "rotateZ"(angle: float): void
public "rotateX"(angle: float): void
public "rotateY"(angle: float): void
public "setZero"(): void
public "lengthSq"(): float
public "toMatrix4"(): $Matrix4
public "toMatrix4"(out: $Matrix4$Type): $Matrix4
public "toMatrix3"(out: $Matrix3$Type): $Matrix3
public "toMatrix3"(): $Matrix3
public static "makeAxisAngle"(axisX: float, axisY: float, axisZ: float, angle: float): $Quaternion
public static "makeAxisAngle"(axis: $Vector3$Type, angle: float): $Quaternion
public static "makeEulerAngles"(rotationX: float, rotationY: float, rotationZ: float): $Quaternion
public "setFromEulerAngles"(rotationX: float, rotationY: float, rotationZ: float): void
public "setFromAxisAngle"(axisX: float, axisY: float, axisZ: float, angle: float): void
public "setFromAxisAngle"(axis: $Vector3$Type, angle: float): void
public "rotateByEuler"(rotationX: float, rotationY: float, rotationZ: float): void
public "toEulerAngles"(angles: (float)[]): void
public "toEulerAngles"(result: $Vector3$Type): void
public "rotateByAxis"(axisX: float, axisY: float, axisZ: float, angle: float): void
public "rotateByAxis"(axis: $Vector3$Type, angle: float): void
public "toAxisAngle"(axis: (float)[]): float
public "toAxisAngle"(axis: $Vector3$Type): float
get "normalized"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Quaternion$Type = ($Quaternion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Quaternion_ = $Quaternion$Type;
}}
declare module "packages/icyllis/arc3d/core/$Path" {
import {$PathIterator, $PathIterator$Type} from "packages/icyllis/arc3d/core/$PathIterator"
import {$PathConsumer, $PathConsumer$Type} from "packages/icyllis/arc3d/core/$PathConsumer"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"

export class $Path implements $PathConsumer {
static readonly "FILL_NON_ZERO": integer
static readonly "FILL_EVEN_ODD": integer
static readonly "VERB_MOVE": byte
static readonly "VERB_LINE": byte
static readonly "VERB_QUAD": byte
static readonly "VERB_CUBIC": byte
static readonly "VERB_CLOSE": byte
static readonly "DIRECTION_CW": integer
static readonly "DIRECTION_CCW": integer
static readonly "SEGMENT_LINE": integer
static readonly "SEGMENT_QUAD": integer
static readonly "SEGMENT_CUBIC": integer
static readonly "APPROXIMATE_ARC_WITH_CUBICS": integer
static readonly "APPROXIMATE_CONIC_WITH_QUADS": integer

constructor()
constructor(other: $Path$Type)

public "equals"(obj: any): boolean
public "hashCode"(): integer
public "clear"(): void
public "isEmpty"(): boolean
public "iterator"(): $PathIterator
public "getBounds"(): $Rect2fc
public "set"(other: $Path$Type): void
public "forEach"(action: $PathConsumer$Type): void
public "trimToSize"(): void
public "reset"(): void
public "isFinite"(): boolean
public "move"(other: $Path$Type): void
public "recycle"(): void
public "moveTo"(x: float, y: float): void
public "lineTo"(x: float, y: float): void
public "quadTo"(x1: float, y1: float, x2: float, y2: float): void
public "closePath"(): void
public "pathDone"(): void
public "moveToRel"(dx: float, dy: float): void
public "setFillRule"(rule: integer): void
public "getFillRule"(): integer
public "lineToRel"(dx: float, dy: float): void
public "quadToRel"(dx1: float, dy1: float, dx2: float, dy2: float): void
public "updateBoundsCache"(): void
public "cubicToRel"(dx1: float, dy1: float, dx2: float, dy2: float, dx3: float, dy3: float): void
public "cubicTo"(x1: float, y1: float, x2: float, y2: float, x3: float, y3: float): void
public "getSegmentMask"(): integer
public "estimatedByteSize"(): long
public "countVerbs"(): integer
public "countPoints"(): integer
public "quadTo"(pts: (float)[], off: integer): void
public "cubicTo"(pts: (float)[], off: integer): void
get "empty"(): boolean
get "bounds"(): $Rect2fc
get "finite"(): boolean
set "fillRule"(value: integer)
get "fillRule"(): integer
get "segmentMask"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Path$Type = ($Path);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Path_ = $Path$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorFilter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ColorFilter {


public "compose"(before: $ColorFilter$Type): $ColorFilter
public "andThen"(after: $ColorFilter$Type): $ColorFilter
public "filterColor"(col: integer): integer
public "filterColor4f"(arg0: (float)[], arg1: (float)[]): void
public "isAlphaUnchanged"(): boolean
get "alphaUnchanged"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorFilter$Type = ($ColorFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorFilter_ = $ColorFilter$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuRenderTarget" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$Attachment, $Attachment$Type} from "packages/icyllis/arc3d/engine/$Attachment"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $GpuRenderTarget extends $ManagedResource implements $IGpuSurface {


public "getBackendFormat"(): $BackendFormat
public "getWidth"(): integer
public "getHeight"(): integer
public "getSampleCount"(): integer
public "getSurfaceFlags"(): integer
public "getStencilBits"(): integer
public "asTexture"(): $GpuTexture
public "getStencilBuffer"(): $Attachment
public "getBackendRenderTarget"(): $BackendRenderTarget
public "asRenderTarget"(): $GpuRenderTarget
public "ref"(): void
public static "getApproxSize"(size: integer): integer
public "unref"(): void
get "backendFormat"(): $BackendFormat
get "width"(): integer
get "height"(): integer
get "sampleCount"(): integer
get "surfaceFlags"(): integer
get "stencilBits"(): integer
get "stencilBuffer"(): $Attachment
get "backendRenderTarget"(): $BackendRenderTarget
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuRenderTarget$Type = ($GpuRenderTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuRenderTarget_ = $GpuRenderTarget$Type;
}}
declare module "packages/icyllis/arc3d/core/effects/$ColorMatrixColorFilter" {
import {$ColorFilter, $ColorFilter$Type} from "packages/icyllis/arc3d/core/$ColorFilter"

export class $ColorMatrixColorFilter extends $ColorFilter {

constructor(matrix: (float)[])

public static "make"(matrix: (float)[]): $ColorFilter
public "getMatrix"(): (float)[]
public "filterColor4f"(col: (float)[], out: (float)[]): void
public "isAlphaUnchanged"(): boolean
get "matrix"(): (float)[]
get "alphaUnchanged"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorMatrixColorFilter$Type = ($ColorMatrixColorFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorMatrixColorFilter_ = $ColorMatrixColorFilter$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Node$ExpressionKind extends $Enum<($Node$ExpressionKind)> {
static readonly "BINARY": $Node$ExpressionKind
static readonly "CONDITIONAL": $Node$ExpressionKind
static readonly "CONSTRUCTOR_ARRAY": $Node$ExpressionKind
static readonly "CONSTRUCTOR_ARRAY_CAST": $Node$ExpressionKind
static readonly "CONSTRUCTOR_COMPOUND": $Node$ExpressionKind
static readonly "CONSTRUCTOR_COMPOUND_CAST": $Node$ExpressionKind
static readonly "CONSTRUCTOR_MATRIX_TO_MATRIX": $Node$ExpressionKind
static readonly "CONSTRUCTOR_SCALAR_CAST": $Node$ExpressionKind
static readonly "CONSTRUCTOR_SCALAR_TO_MATRIX": $Node$ExpressionKind
static readonly "CONSTRUCTOR_SCALAR_TO_VECTOR": $Node$ExpressionKind
static readonly "CONSTRUCTOR_STRUCT": $Node$ExpressionKind
static readonly "FIELD_ACCESS": $Node$ExpressionKind
static readonly "FUNCTION_CALL": $Node$ExpressionKind
static readonly "FUNCTION_REFERENCE": $Node$ExpressionKind
static readonly "INDEX": $Node$ExpressionKind
static readonly "LITERAL": $Node$ExpressionKind
static readonly "POISON": $Node$ExpressionKind
static readonly "POSTFIX": $Node$ExpressionKind
static readonly "PREFIX": $Node$ExpressionKind
static readonly "SWIZZLE": $Node$ExpressionKind
static readonly "TYPE_REFERENCE": $Node$ExpressionKind
static readonly "VARIABLE_REFERENCE": $Node$ExpressionKind


public static "values"(): ($Node$ExpressionKind)[]
public static "valueOf"(name: string): $Node$ExpressionKind
public "getType"(): $Class<(any)>
get "type"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$ExpressionKind$Type = (("constructor_array") | ("swizzle") | ("variable_reference") | ("constructor_compound") | ("conditional") | ("poison") | ("function_call") | ("constructor_scalar_to_matrix") | ("prefix") | ("constructor_scalar_to_vector") | ("constructor_compound_cast") | ("index") | ("constructor_scalar_cast") | ("function_reference") | ("constructor_matrix_to_matrix") | ("literal") | ("constructor_struct") | ("field_access") | ("binary") | ("constructor_array_cast") | ("type_reference") | ("postfix")) | ($Node$ExpressionKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node$ExpressionKind_ = $Node$ExpressionKind$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceDevice" {
import {$Paint, $Paint$Type} from "packages/icyllis/arc3d/core/$Paint"
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$Device, $Device$Type} from "packages/icyllis/arc3d/core/$Device"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"

export class $SurfaceDevice extends $Device {


public static "make"(rContext: $RecordingContext$Type, colorType: integer, colorSpace: $ColorSpace$Type, proxy: $SurfaceProxy$Type, origin: integer, clear: boolean): $SurfaceDevice
public static "make"(rContext: $RecordingContext$Type, colorType: integer, alphaType: integer, colorSpace: $ColorSpace$Type, width: integer, height: integer, sampleCount: integer, surfaceFlags: integer, origin: integer, clear: boolean): $SurfaceDevice
public "drawRect"(r: $Rect2f$Type, paint: $Paint$Type): void
public "clipIsAA"(): boolean
public "clipIsWideOpen"(): boolean
public "drawPaint"(paint: $Paint$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceDevice$Type = ($SurfaceDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceDevice_ = $SurfaceDevice$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$MemoryLayout" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $MemoryLayout extends $Enum<($MemoryLayout)> {
static readonly "Std140": $MemoryLayout
static readonly "Extended": $MemoryLayout
static readonly "Std430": $MemoryLayout
static readonly "Scalar": $MemoryLayout


public static "values"(): ($MemoryLayout)[]
public static "valueOf"(name: string): $MemoryLayout
public "size"(type: $Type$Type): integer
public "stride"(type: $Type$Type): integer
public "isSupported"(type: $Type$Type): boolean
public "alignment"(type: $Type$Type): integer
public "alignment"(type: $Type$Type, out: (integer)[]): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryLayout$Type = (("std430") | ("scalar") | ("std140") | ("extended")) | ($MemoryLayout);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryLayout_ = $MemoryLayout$Type;
}}
declare module "packages/icyllis/arc3d/core/$Point" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Point {

constructor()

public static "distanceToSq"(ax: float, ay: float, bx: float, by: float): float
public static "isDegenerate"(dx: float, dy: float): boolean
public static "equals"(x1: float, y1: float, x2: float, y2: float): boolean
public static "length"(x: float, y: float): float
public static "setLength"(pos: (float)[], off: integer, length: float): boolean
public static "normalize"(pos: (float)[], off: integer): boolean
public static "distanceTo"(ax: float, ay: float, bx: float, by: float): float
public static "isApproxEqual"(x1: float, y1: float, x2: float, y2: float, tolerance: float): boolean
public static "crossProduct"(ax: float, ay: float, bx: float, by: float): float
public static "dotProduct"(ax: float, ay: float, bx: float, by: float): float
public static "lengthSq"(x: float, y: float): float
public static "distanceToLineBetweenSq"(px: float, py: float, ax: float, ay: float, bx: float, by: float): float
public static "distanceToLineBetween"(px: float, py: float, ax: float, ay: float, bx: float, by: float): float
public static "distanceToLineSegmentBetween"(px: float, py: float, ax: float, ay: float, bx: float, by: float): float
public static "distanceToLineSegmentBetweenSq"(px: float, py: float, ax: float, ay: float, bx: float, by: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Point$Type = ($Point);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Point_ = $Point$Type;
}}
declare module "packages/icyllis/arc3d/engine/geom/$CircleProcessor" {
import {$GeometryProcessor$AttributeSet, $GeometryProcessor$AttributeSet$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$AttributeSet"
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"

export class $CircleProcessor extends $GeometryProcessor {
static readonly "POSITION": $GeometryProcessor$Attribute
static readonly "CIRCLE_EDGE": $GeometryProcessor$Attribute
static readonly "COLOR": $GeometryProcessor$Attribute
static readonly "CLIP_PLANE": $GeometryProcessor$Attribute
static readonly "ISECT_PLANE": $GeometryProcessor$Attribute
static readonly "UNION_PLANE": $GeometryProcessor$Attribute
static readonly "ROUND_CAP_CENTERS": $GeometryProcessor$Attribute
static readonly "MODEL_VIEW": $GeometryProcessor$Attribute
static readonly "VERTEX_FORMAT": $GeometryProcessor$AttributeSet
static readonly "INSTANCE_FORMAT": $GeometryProcessor$AttributeSet
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer

constructor(stroke: boolean, clipPlane: boolean, isectPlane: boolean, unionPlane: boolean, roundCaps: boolean)

public "name"(): string
public "primitiveType"(): byte
public "makeProgramImpl"(caps: $ShaderCaps$Type): $GeometryProcessor$ProgramImpl
public "appendToKey"(b: $KeyBuilder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CircleProcessor$Type = ($CircleProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CircleProcessor_ = $CircleProcessor$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ReturnStatement" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"

export class $ReturnStatement extends $Statement {
 "mPosition": integer


public "toString"(): string
public static "make"(pos: integer, expression: $Expression$Type): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getExpression"(): $Expression
public "setExpression"(expression: $Expression$Type): void
public "getKind"(): $Node$StatementKind
get "expression"(): $Expression
set "expression"(value: $Expression$Type)
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReturnStatement$Type = ($ReturnStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReturnStatement_ = $ReturnStatement$Type;
}}
declare module "packages/icyllis/arc3d/engine/$UserStencilSettings" {
import {$StencilFaceSettings, $StencilFaceSettings$Type} from "packages/icyllis/arc3d/engine/$StencilFaceSettings"

export class $UserStencilSettings {
static readonly "DISABLED_STENCIL_FLAG": short
static readonly "TEST_ALWAYS_PASSES_STENCIL_FLAG": short
static readonly "NO_MODIFY_STENCIL_STENCIL_FLAG": short
static readonly "NO_WRAP_OPS_STENCIL_FLAG": short
static readonly "SINGLE_SIDED_STENCIL_FLAG": short
static readonly "LAST_STENCIL_FLAG": short
static readonly "ALL_STENCIL_FLAGS": short
static readonly "USER_STENCIL_TEST_ALWAYS_IF_IN_CLIP": short
static readonly "USER_STENCIL_TEST_EQUAL_IF_IN_CLIP": short
static readonly "USER_STENCIL_TEST_LESS_IF_IN_CLIP": short
static readonly "USER_STENCIL_TEST_LEQUAL_IF_IN_CLIP": short
static readonly "USER_STENCIL_TEST_ALWAYS": short
static readonly "USER_STENCIL_TEST_NEVER": short
static readonly "USER_STENCIL_TEST_GREATER": short
static readonly "USER_STENCIL_TEST_GEQUAL": short
static readonly "USER_STENCIL_TEST_LESS": short
static readonly "USER_STENCIL_TEST_LEQUAL": short
static readonly "USER_STENCIL_TEST_EQUAL": short
static readonly "USER_STENCIL_TEST_NOTEQUAL": short
static readonly "LAST_CLIPPED_STENCIL_TEST": short
static readonly "USER_STENCIL_TEST_COUNT": integer
static readonly "USER_STENCIL_OP_KEEP": byte
static readonly "USER_STENCIL_OP_ZERO": byte
static readonly "USER_STENCIL_OP_REPLACE": byte
static readonly "USER_STENCIL_OP_INVERT": byte
static readonly "USER_STENCIL_OP_INC_WRAP": byte
static readonly "USER_STENCIL_OP_DEC_WRAP": byte
static readonly "USER_STENCIL_OP_INC_MAYBE_CLAMP": byte
static readonly "USER_STENCIL_OP_DEC_MAYBE_CLAMP": byte
static readonly "USER_STENCIL_OP_ZERO_CLIP_BIT": byte
static readonly "USER_STENCIL_OP_SET_CLIP_BIT": byte
static readonly "USER_STENCIL_OP_INVERT_CLIP_BIT": byte
static readonly "USER_STENCIL_OP_SET_CLIP_AND_REPLACE_USER_BITS": byte
static readonly "USER_STENCIL_OP_ZERO_CLIP_AND_USER_BITS": byte
static readonly "LAST_USER_ONLY_STENCIL_OP": byte
static readonly "LAST_CLIP_ONLY_STENCIL_OP": byte
static readonly "USER_STENCIL_OP_COUNT": integer
readonly "mCWFlags": short
readonly "mCWFlags2": short
readonly "mCWFace": $StencilFaceSettings
readonly "mCCWFlags": short
readonly "mCCWFlags2": short
readonly "mCCWFace": $StencilFaceSettings

constructor(ref: short, test: short, testMask: short, passOp: byte, failOp: byte, writeMask: short)
constructor(cwRef: short, ccwRef: short, cwTest: short, ccwTest: short, cwTestMask: short, ccwTestMask: short, cwPassOp: byte, ccwPassOp: byte, cwFailOp: byte, ccwFailOp: byte, cwWriteMask: short, ccwWriteMask: short)

public "flags"(hasStencilClip: boolean): short
public "isDisabled"(hasStencilClip: boolean): boolean
public "testAlwaysPasses"(hasStencilClip: boolean): boolean
public "isDoubleSided"(hasStencilClip: boolean): boolean
public "usesWrapOp"(hasStencilClip: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UserStencilSettings$Type = ($UserStencilSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UserStencilSettings_ = $UserStencilSettings$Type;
}}
declare module "packages/icyllis/arc3d/engine/$FlushInfo$SubmittedCallback" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FlushInfo$SubmittedCallback {

 "onSubmitted"(arg0: boolean): void

(arg0: boolean): void
}

export namespace $FlushInfo$SubmittedCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlushInfo$SubmittedCallback$Type = ($FlushInfo$SubmittedCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlushInfo$SubmittedCallback_ = $FlushInfo$SubmittedCallback$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$TypeReference" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $TypeReference extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public "getValue"(): $Type
public static "make"(context: $Context$Type, position: integer, value: $Type$Type): $TypeReference
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$ExpressionKind
get "value"(): $Type
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeReference$Type = ($TypeReference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeReference_ = $TypeReference$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$BinaryExpression" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Operator, $Operator$Type} from "packages/icyllis/arc3d/compiler/$Operator"

export class $BinaryExpression extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, left: $Expression$Type, op: $Operator$Type, right: $Expression$Type): $Expression
public static "make"(context: $Context$Type, pos: integer, left: $Expression$Type, op: $Operator$Type, right: $Expression$Type, resultType: $Type$Type): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getRight"(): $Expression
public "getLeft"(): $Expression
public "setLeft"(left: $Expression$Type): void
public "setRight"(right: $Expression$Type): void
public "getOperator"(): $Operator
public "getKind"(): $Node$ExpressionKind
get "right"(): $Expression
get "left"(): $Expression
set "left"(value: $Expression$Type)
set "right"(value: $Expression$Type)
get "operator"(): $Operator
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BinaryExpression$Type = ($BinaryExpression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BinaryExpression_ = $BinaryExpression$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ModuleUnit" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModuleUnit {


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleUnit$Type = ($ModuleUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleUnit_ = $ModuleUnit$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$SPIRVVersion" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $SPIRVVersion extends $Enum<($SPIRVVersion)> {
static readonly "SPIRV_1_0": $SPIRVVersion
static readonly "SPIRV_1_3": $SPIRVVersion
static readonly "SPIRV_1_4": $SPIRVVersion
static readonly "SPIRV_1_5": $SPIRVVersion
static readonly "SPIRV_1_6": $SPIRVVersion
readonly "mVersionNumber": integer


public static "values"(): ($SPIRVVersion)[]
public static "valueOf"(name: string): $SPIRVVersion
public "isBefore"(other: $SPIRVVersion$Type): boolean
public "isAtLeast"(other: $SPIRVVersion$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SPIRVVersion$Type = (("spirv_1_0") | ("spirv_1_4") | ("spirv_1_3") | ("spirv_1_6") | ("spirv_1_5")) | ($SPIRVVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SPIRVVersion_ = $SPIRVVersion$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuBuffer" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$GpuResource, $GpuResource$Type} from "packages/icyllis/arc3d/engine/$GpuResource"

export class $GpuBuffer extends $GpuResource {
static readonly "kRead_LockMode": integer
static readonly "kWriteDiscard_LockMode": integer


public "lock"(): long
public "lock"(offset: integer, size: integer): long
public "getSize"(): integer
public "isLocked"(): boolean
public "unlock"(offset: integer, size: integer): void
public "unlock"(): void
public "getMemorySize"(): long
public "getLockedBuffer"(): long
public "getUsage"(): integer
public "updateData"(offset: integer, size: integer, data: long): boolean
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "size"(): integer
get "locked"(): boolean
get "memorySize"(): long
get "lockedBuffer"(): long
get "usage"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuBuffer$Type = ($GpuBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuBuffer_ = $GpuBuffer$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLRenderTexture" {
import {$GLTexture, $GLTexture$Type} from "packages/icyllis/arc3d/opengl/$GLTexture"
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$GLRenderTarget, $GLRenderTarget$Type} from "packages/icyllis/arc3d/opengl/$GLRenderTarget"

export class $GLRenderTexture extends $GLTexture {


public "toString"(): string
public "getSampleCount"(): integer
public "asRenderTarget"(): $GLRenderTarget
public static "getApproxSize"(size: integer): integer
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "sampleCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLRenderTexture$Type = ($GLRenderTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLRenderTexture_ = $GLRenderTexture$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$TreeVisitor" {
import {$ForLoop, $ForLoop$Type} from "packages/icyllis/arc3d/compiler/tree/$ForLoop"
import {$VariableDecl, $VariableDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$VariableDecl"
import {$ExpressionStatement, $ExpressionStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$ExpressionStatement"
import {$BinaryExpression, $BinaryExpression$Type} from "packages/icyllis/arc3d/compiler/tree/$BinaryExpression"
import {$IndexExpression, $IndexExpression$Type} from "packages/icyllis/arc3d/compiler/tree/$IndexExpression"
import {$PrefixExpression, $PrefixExpression$Type} from "packages/icyllis/arc3d/compiler/tree/$PrefixExpression"
import {$FunctionPrototype, $FunctionPrototype$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionPrototype"
import {$BlockStatement, $BlockStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$BlockStatement"
import {$EmptyStatement, $EmptyStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$EmptyStatement"
import {$FunctionCall, $FunctionCall$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionCall"
import {$FunctionDefinition, $FunctionDefinition$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDefinition"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$GlobalVariableDecl, $GlobalVariableDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$GlobalVariableDecl"
import {$FunctionReference, $FunctionReference$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionReference"
import {$ContinueStatement, $ContinueStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$ContinueStatement"
import {$ConditionalExpression, $ConditionalExpression$Type} from "packages/icyllis/arc3d/compiler/tree/$ConditionalExpression"
import {$ReturnStatement, $ReturnStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$ReturnStatement"
import {$InterfaceBlock, $InterfaceBlock$Type} from "packages/icyllis/arc3d/compiler/tree/$InterfaceBlock"
import {$TypeReference, $TypeReference$Type} from "packages/icyllis/arc3d/compiler/tree/$TypeReference"
import {$Literal, $Literal$Type} from "packages/icyllis/arc3d/compiler/tree/$Literal"
import {$IfStatement, $IfStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$IfStatement"
import {$BreakStatement, $BreakStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$BreakStatement"
import {$DiscardStatement, $DiscardStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$DiscardStatement"
import {$FieldAccess, $FieldAccess$Type} from "packages/icyllis/arc3d/compiler/tree/$FieldAccess"
import {$VariableReference, $VariableReference$Type} from "packages/icyllis/arc3d/compiler/tree/$VariableReference"
import {$PostfixExpression, $PostfixExpression$Type} from "packages/icyllis/arc3d/compiler/tree/$PostfixExpression"
import {$Swizzle, $Swizzle$Type} from "packages/icyllis/arc3d/compiler/tree/$Swizzle"

export class $TreeVisitor {

constructor()

public "visitTypeReference"(expr: $TypeReference$Type): boolean
public "visitLiteral"(expr: $Literal$Type): boolean
public "visitExpression"(stmt: $ExpressionStatement$Type): boolean
public "visitFieldAccess"(expr: $FieldAccess$Type): boolean
public "visitEmpty"(stmt: $EmptyStatement$Type): boolean
public "visitBlock"(stmt: $BlockStatement$Type): boolean
public "visitBreak"(stmt: $BreakStatement$Type): boolean
public "visitSwizzle"(expr: $Swizzle$Type): boolean
public "visitIndex"(expr: $IndexExpression$Type): boolean
public "visitContinue"(stmt: $ContinueStatement$Type): boolean
public "visitForLoop"(stmt: $ForLoop$Type): boolean
public "visitPostfix"(expr: $PostfixExpression$Type): boolean
public "visitBinary"(expr: $BinaryExpression$Type): boolean
public "visitConditional"(expr: $ConditionalExpression$Type): boolean
public "visitPrefix"(expr: $PrefixExpression$Type): boolean
public "visitFunctionCall"(expr: $FunctionCall$Type): boolean
public "visitDiscard"(stmt: $DiscardStatement$Type): boolean
public "visitReturn"(stmt: $ReturnStatement$Type): boolean
public "visitIf"(stmt: $IfStatement$Type): boolean
public "visitVariableDecl"(variableDecl: $VariableDecl$Type): boolean
public "visitFunctionPrototype"(prototype: $FunctionPrototype$Type): boolean
public "visitFunctionReference"(expr: $FunctionReference$Type): boolean
public "visitVariableReference"(expr: $VariableReference$Type): boolean
public "visitConstructorCall"(expr: $ConstructorCall$Type): boolean
public "visitInterfaceBlock"(interfaceBlock: $InterfaceBlock$Type): boolean
public "visitGlobalVariableDecl"(variableDecl: $GlobalVariableDecl$Type): boolean
public "visitFunctionDefinition"(definition: $FunctionDefinition$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TreeVisitor$Type = ($TreeVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TreeVisitor_ = $TreeVisitor$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$VariableReference" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Variable, $Variable$Type} from "packages/icyllis/arc3d/compiler/tree/$Variable"

export class $VariableReference extends $Expression {
static readonly "kRead_ReferenceKind": integer
static readonly "kWrite_ReferenceKind": integer
static readonly "kReadWrite_ReferenceKind": integer
static readonly "kPointer_ReferenceKind": integer
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "make"(position: integer, variable: $Variable$Type, referenceKind: integer): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getReferenceKind"(): integer
public "getVariable"(): $Variable
public "setVariable"(variable: $Variable$Type): void
public "getKind"(): $Node$ExpressionKind
public "setReferenceKind"(referenceKind: integer): void
get "referenceKind"(): integer
get "variable"(): $Variable
set "variable"(value: $Variable$Type)
get "kind"(): $Node$ExpressionKind
set "referenceKind"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableReference$Type = ($VariableReference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableReference_ = $VariableReference$Type;
}}
declare module "packages/icyllis/arc3d/engine/$IScratchKey" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IScratchKey {

 "equals"(arg0: any): boolean
 "hashCode"(): integer
}

export namespace $IScratchKey {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IScratchKey$Type = ($IScratchKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IScratchKey_ = $IScratchKey$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLUniformBuffer" {
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"

export class $GLUniformBuffer extends $ManagedResource {


public static "make"(device: $GLDevice$Type, size: integer, binding: integer): $GLUniformBuffer
public "getSize"(): integer
public "getHandle"(): integer
public "discard"(): void
public "getBinding"(): integer
get "size"(): integer
get "handle"(): integer
get "binding"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLUniformBuffer$Type = ($GLUniformBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLUniformBuffer_ = $GLUniformBuffer$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLDevice" {
import {$GLTexture, $GLTexture$Type} from "packages/icyllis/arc3d/opengl/$GLTexture"
import {$CpuBufferPool, $CpuBufferPool$Type} from "packages/icyllis/arc3d/engine/$CpuBufferPool"
import {$GLCommandBuffer, $GLCommandBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLCommandBuffer"
import {$GLPipelineStateCache, $GLPipelineStateCache$Type} from "packages/icyllis/arc3d/opengl/$GLPipelineStateCache"
import {$GLRenderTarget, $GLRenderTarget$Type} from "packages/icyllis/arc3d/opengl/$GLRenderTarget"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$GLCaps, $GLCaps$Type} from "packages/icyllis/arc3d/opengl/$GLCaps"
import {$FlushInfo$FinishedCallback, $FlushInfo$FinishedCallback$Type} from "packages/icyllis/arc3d/engine/$FlushInfo$FinishedCallback"
import {$DirectContext, $DirectContext$Type} from "packages/icyllis/arc3d/engine/$DirectContext"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"
import {$GpuBufferPool, $GpuBufferPool$Type} from "packages/icyllis/arc3d/engine/$GpuBufferPool"
import {$GLBuffer, $GLBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLBuffer"

export class $GLDevice extends $GpuDevice {


public static "make"(context: $DirectContext$Type, options: $ContextOptions$Type): $GLDevice
public "disconnect"(cleanup: boolean): void
public "checkFence"(fence: long): boolean
public "insertFence"(): long
public "deleteFence"(fence: long): void
public "waitForQueue"(): void
public "forceResetContext"(state: integer): void
public "getCpuBufferPool"(): $CpuBufferPool
public "endRenderPass"(fs: $GLRenderTarget$Type, colorOps: byte, stencilOps: byte): void
public "beginRenderPass"(fs: $GLRenderTarget$Type, colorOps: byte, stencilOps: byte, clearColor: (float)[]): $GLCommandBuffer
public "bindBuffer"(buffer: $GLBuffer$Type): integer
public "setTextureUnit"(unit: integer): void
public "bindBufferForSetup"(usage: integer, buffer: integer): integer
public "onWrapBackendRenderTarget"(backendRenderTarget: $BackendRenderTarget$Type): $GpuRenderTarget
public "getPipelineStateCache"(): $GLPipelineStateCache
public "currentCommandBuffer"(): $GLCommandBuffer
public "checkFinishedCallbacks"(): void
public "addFinishedCallback"(callback: $FlushInfo$FinishedCallback$Type): void
public "bindIndexBufferInPipe"(buffer: $GLBuffer$Type): void
public "getInstancePool"(): $GpuBufferPool
public "getVertexPool"(): $GpuBufferPool
public "getIndexPool"(): $GpuBufferPool
public "getCaps"(): $GLCaps
public "bindTexture"(bindingUnit: integer, texture: $GLTexture$Type, samplerState: integer, readSwizzle: short): void
public static "colorTypeClampType"(ct: integer): integer
public static "colorTypeEncoding"(ct: integer): integer
public static "colorTypeToPublic"(ct: integer): integer
get "cpuBufferPool"(): $CpuBufferPool
set "textureUnit"(value: integer)
get "pipelineStateCache"(): $GLPipelineStateCache
get "instancePool"(): $GpuBufferPool
get "vertexPool"(): $GpuBufferPool
get "indexPool"(): $GpuBufferPool
get "caps"(): $GLCaps
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLDevice$Type = ($GLDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLDevice_ = $GLDevice$Type;
}}
declare module "packages/icyllis/arc3d/core/$PixelMap" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $PixelMap {

constructor(newInfo: $ImageInfo$Type, oldPixelMap: $PixelMap$Type)
constructor(info: $ImageInfo$Type, base: any, address: long, rowStride: integer)

public "toString"(): string
public "clear"(red: float, green: float, blue: float, alpha: float, subset: $Rect2ic$Type): boolean
public "getAddress"(): long
public "getBase"(): any
public "getInfo"(): $ImageInfo
public "getWidth"(): integer
public "getHeight"(): integer
public "getColorType"(): integer
public "getColorSpace"(): $ColorSpace
public "getRowStride"(): integer
public "getAlphaType"(): integer
get "address"(): long
get "base"(): any
get "info"(): $ImageInfo
get "width"(): integer
get "height"(): integer
get "colorType"(): integer
get "colorSpace"(): $ColorSpace
get "rowStride"(): integer
get "alphaType"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PixelMap$Type = ($PixelMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PixelMap_ = $PixelMap$Type;
}}
declare module "packages/icyllis/arc3d/engine/$UniformDataManager" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$Matrix3, $Matrix3$Type} from "packages/icyllis/arc3d/core/$Matrix3"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"

export class $UniformDataManager extends $RefCnt {

constructor(uniformCount: integer, uniformSize: integer)

public "set4iv"(u: integer, offset: integer, count: integer, value: (integer)[]): void
public "set4iv"(u: integer, count: integer, value: long): void
public "set1iv"(u: integer, offset: integer, count: integer, value: (integer)[]): void
public "set1iv"(u: integer, count: integer, value: long): void
public "set2iv"(u: integer, offset: integer, count: integer, value: (integer)[]): void
public "set2iv"(u: integer, count: integer, value: long): void
public "set2fv"(u: integer, count: integer, value: long): void
public "set2fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "set1i"(u: integer, v0: integer): void
public "set3i"(u: integer, v0: integer, v1: integer, v2: integer): void
public "set1fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "set1fv"(u: integer, count: integer, value: long): void
public "set3iv"(u: integer, offset: integer, count: integer, value: (integer)[]): void
public "set3iv"(u: integer, count: integer, value: long): void
public "set3fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "set3fv"(u: integer, count: integer, value: long): void
public "set1f"(u: integer, v0: float): void
public "set2i"(u: integer, v0: integer, v1: integer): void
public "set3f"(u: integer, v0: float, v1: float, v2: float): void
public "set2f"(u: integer, v0: float, v1: float): void
public "set4i"(u: integer, v0: integer, v1: integer, v2: integer, v3: integer): void
public "set4fv"(u: integer, count: integer, value: long): void
public "set4fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "setMatrix4fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "setMatrix4fv"(u: integer, count: integer, value: long): void
public "setMatrix4f"(u: integer, matrix: $Matrix4$Type): void
public "setMatrix2fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "setMatrix2fv"(u: integer, count: integer, value: long): void
public "setMatrix3fv"(u: integer, count: integer, value: long): void
public "setMatrix3fv"(u: integer, offset: integer, count: integer, value: (float)[]): void
public "set4f"(u: integer, v0: float, v1: float, v2: float, v3: float): void
public "setMatrix3f"(u: integer, matrix: $Matrixc$Type): void
public "setMatrix3f"(u: integer, matrix: $Matrix3$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UniformDataManager$Type = ($UniformDataManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UniformDataManager_ = $UniformDataManager$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Processor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Processor {
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer


public "name"(): string
public "classID"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Processor$Type = ($Processor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Processor_ = $Processor$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLPipelineStateBuilder" {
import {$VaryingHandler, $VaryingHandler$Type} from "packages/icyllis/arc3d/engine/shading/$VaryingHandler"
import {$FragmentShaderBuilder, $FragmentShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$FragmentShaderBuilder"
import {$PipelineDesc, $PipelineDesc$Type} from "packages/icyllis/arc3d/engine/$PipelineDesc"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$VertexShaderBuilder, $VertexShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$VertexShaderBuilder"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$GLGraphicsPipelineState, $GLGraphicsPipelineState$Type} from "packages/icyllis/arc3d/opengl/$GLGraphicsPipelineState"
import {$UniformHandler, $UniformHandler$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$PipelineBuilder, $PipelineBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$PipelineBuilder"

export class $GLPipelineStateBuilder extends $PipelineBuilder {
 "mVS": $VertexShaderBuilder
 "mFS": $FragmentShaderBuilder
readonly "mDesc": $PipelineDesc
readonly "mPipelineInfo": $PipelineInfo
 "mProjectionUniform": integer
 "mGPImpl": $GeometryProcessor$ProgramImpl


public static "createGraphicsPipelineState"(device: $GLDevice$Type, desc: $PipelineDesc$Type, pipelineInfo: $PipelineInfo$Type): $GLGraphicsPipelineState
public "uniformHandler"(): $UniformHandler
public "varyingHandler"(): $VaryingHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLPipelineStateBuilder$Type = ($GLPipelineStateBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLPipelineStateBuilder_ = $GLPipelineStateBuilder$Type;
}}
declare module "packages/icyllis/arc3d/core/$MatrixProvider" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"

export interface $MatrixProvider {

 "getLocalToDevice"(): $Matrix4

(): $Matrix4
}

export namespace $MatrixProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatrixProvider$Type = ($MatrixProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatrixProvider_ = $MatrixProvider$Type;
}}
declare module "packages/icyllis/arc3d/core/$NoPixelsDevice" {
import {$Paint, $Paint$Type} from "packages/icyllis/arc3d/core/$Paint"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Device, $Device$Type} from "packages/icyllis/arc3d/core/$Device"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"

export class $NoPixelsDevice extends $Device {

constructor(bounds: $Rect2i$Type)
constructor(left: integer, top: integer, right: integer, bottom: integer)

public "drawRect"(r: $Rect2f$Type, paint: $Paint$Type): void
public "clipIsAA"(): boolean
public "clipIsWideOpen"(): boolean
public "clipRect"(rect: $Rect2f$Type, clipOp: integer, doAA: boolean): void
public "drawPaint"(paint: $Paint$Type): void
public "resetForNextPicture"(left: integer, top: integer, right: integer, bottom: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoPixelsDevice$Type = ($NoPixelsDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoPixelsDevice_ = $NoPixelsDevice$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ReleaseCallback" {
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"

export class $ReleaseCallback extends $RefCnt {

constructor()

public "invoke"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReleaseCallback$Type = ($ReleaseCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReleaseCallback_ = $ReleaseCallback$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SamplerState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SamplerState {
static readonly "FILTER_NEAREST": integer
static readonly "FILTER_LINEAR": integer
static readonly "MIPMAP_MODE_NONE": integer
static readonly "MIPMAP_MODE_NEAREST": integer
static readonly "MIPMAP_MODE_LINEAR": integer
static readonly "ADDRESS_MODE_REPEAT": integer
static readonly "ADDRESS_MODE_MIRRORED_REPEAT": integer
static readonly "ADDRESS_MODE_CLAMP_TO_EDGE": integer
static readonly "ADDRESS_MODE_CLAMP_TO_BORDER": integer
static readonly "DEFAULT": integer

constructor()

public static "make"(filter: integer): integer
public static "make"(magFilter: integer, minFilter: integer, mipmapMode: integer, addressModeX: integer, addressModeY: integer): integer
public static "make"(filter: integer, mipmap: integer, address: integer): integer
public static "make"(filter: integer, mipmap: integer): integer
public static "getAddressModeY"(samplerState: integer): integer
public static "getAddressModeX"(samplerState: integer): integer
public static "getMinFilter"(samplerState: integer): integer
public static "makeAnisotropy"(addressModeX: integer, addressModeY: integer, maxAnisotropy: integer, isMipmapped: boolean): integer
public static "isRepeatedX"(samplerState: integer): boolean
public static "isRepeatedY"(samplerState: integer): boolean
public static "getMagFilter"(samplerState: integer): integer
public static "isMipmapped"(samplerState: integer): boolean
public static "getMaxAnisotropy"(samplerState: integer): integer
public static "getMipmapMode"(samplerState: integer): integer
public static "resetMipmapMode"(samplerState: integer): integer
public static "isAnisotropy"(samplerState: integer): boolean
public static "isRepeated"(samplerState: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SamplerState$Type = ($SamplerState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SamplerState_ = $SamplerState$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$Lexer$PackedEntry" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $Lexer$PackedEntry extends $Record {

constructor(values: integer, data: (byte)[])

public "equals"(o: any): boolean
public "toString"(): string
public "values"(): integer
public "hashCode"(): integer
public "data"(): (byte)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Lexer$PackedEntry$Type = ($Lexer$PackedEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Lexer$PackedEntry_ = $Lexer$PackedEntry$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TextureVisitor" {
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"

export interface $TextureVisitor {

 "visit"(arg0: $TextureProxy$Type, arg1: integer): void

(arg0: $TextureProxy$Type, arg1: integer): void
}

export namespace $TextureVisitor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureVisitor$Type = ($TextureVisitor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureVisitor_ = $TextureVisitor$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$Color4fShader" {
import {$Shader, $Shader$Type} from "packages/icyllis/arc3d/core/$Shader"

export class $Color4fShader extends $Shader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Color4fShader$Type = ($Color4fShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Color4fShader_ = $Color4fShader$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceProvider" {
import {$RenderTargetProxy, $RenderTargetProxy$Type} from "packages/icyllis/arc3d/engine/$RenderTargetProxy"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$PixelRef, $PixelRef$Type} from "packages/icyllis/arc3d/core/$PixelRef"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$IUniqueKey, $IUniqueKey$Type} from "packages/icyllis/arc3d/engine/$IUniqueKey"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$SurfaceProxy$LazyInstantiateCallback, $SurfaceProxy$LazyInstantiateCallback$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy$LazyInstantiateCallback"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$PixelMap, $PixelMap$Type} from "packages/icyllis/arc3d/core/$PixelMap"
import {$RenderTextureProxy, $RenderTextureProxy$Type} from "packages/icyllis/arc3d/engine/$RenderTextureProxy"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"

export class $SurfaceProvider {


public "createLazyTexture"(format: $BackendFormat$Type, width: integer, height: integer, surfaceFlags: integer, callback: $SurfaceProxy$LazyInstantiateCallback$Type): $TextureProxy
public "isDeferredProvider"(): boolean
public "wrapBackendRenderTarget"(backendRenderTarget: $BackendRenderTarget$Type, rcReleaseCB: $Runnable$Type): $RenderTargetProxy
public "wrapRenderableBackendTexture"(texture: $BackendTexture$Type, sampleCount: integer, ownership: boolean, cacheable: boolean, releaseCallback: $Runnable$Type): $RenderTextureProxy
public "processInvalidUniqueKey"(key: any, proxy: $TextureProxy$Type, invalidateResource: boolean): void
public "assignUniqueKeyToProxy"(key: $IUniqueKey$Type, proxy: $TextureProxy$Type): boolean
public "adoptUniqueKeyFromSurface"(proxy: $TextureProxy$Type, texture: $GpuTexture$Type): void
public "createTextureFromPixels"(pixelMap: $PixelMap$Type, pixelRef: $PixelRef$Type, dstColorType: integer, surfaceFlags: integer): $TextureProxy
public "createRenderTexture"(format: $BackendFormat$Type, width: integer, height: integer, sampleCount: integer, surfaceFlags: integer): $RenderTextureProxy
public "createTexture"(format: $BackendFormat$Type, width: integer, height: integer, surfaceFlags: integer): $TextureProxy
get "deferredProvider"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceProvider$Type = ($SurfaceProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceProvider_ = $SurfaceProvider$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLUniformDataManager" {
import {$GLCommandBuffer, $GLCommandBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLCommandBuffer"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$UniformDataManager, $UniformDataManager$Type} from "packages/icyllis/arc3d/engine/$UniformDataManager"

export class $GLUniformDataManager extends $UniformDataManager {


public "setProjection"(u: integer, width: integer, height: integer, flipY: boolean): void
public "bindAndUploadUniforms"(device: $GLDevice$Type, commandBuffer: $GLCommandBuffer$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLUniformDataManager$Type = ($GLUniformDataManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLUniformDataManager_ = $GLUniformDataManager$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Node$StatementKind extends $Enum<($Node$StatementKind)> {
static readonly "BLOCK": $Node$StatementKind
static readonly "BREAK": $Node$StatementKind
static readonly "CONTINUE": $Node$StatementKind
static readonly "DISCARD": $Node$StatementKind
static readonly "DO_LOOP": $Node$StatementKind
static readonly "EMPTY": $Node$StatementKind
static readonly "EXPRESSION": $Node$StatementKind
static readonly "FOR_LOOP": $Node$StatementKind
static readonly "IF": $Node$StatementKind
static readonly "RETURN": $Node$StatementKind
static readonly "SWITCH": $Node$StatementKind
static readonly "SWITCH_CASE": $Node$StatementKind
static readonly "VARIABLE_DECL": $Node$StatementKind


public static "values"(): ($Node$StatementKind)[]
public static "valueOf"(name: string): $Node$StatementKind
public "getType"(): $Class<(any)>
get "type"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$StatementKind$Type = (("discard") | ("expression") | ("break") | ("empty") | ("switch") | ("continue") | ("switch_case") | ("block") | ("for_loop") | ("variable_decl") | ("if") | ("do_loop") | ("return")) | ($Node$StatementKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node$StatementKind_ = $Node$StatementKind$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VkBackendTexture" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$VulkanImageInfo, $VulkanImageInfo$Type} from "packages/icyllis/arc3d/vulkan/$VulkanImageInfo"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"

export class $VkBackendTexture extends $BackendTexture {

constructor(width: integer, height: integer, info: $VulkanImageInfo$Type)

public "isProtected"(): boolean
public "getBackendFormat"(): $BackendFormat
public "setVkQueueFamilyIndex"(queueFamilyIndex: integer): void
public "isMipmapped"(): boolean
public "isExternal"(): boolean
public "getBackend"(): integer
public "setVkImageLayout"(layout: integer): void
public "getVkImageInfo"(info: $VulkanImageInfo$Type): boolean
public "isSameTexture"(texture: $BackendTexture$Type): boolean
get "protected"(): boolean
get "backendFormat"(): $BackendFormat
set "vkQueueFamilyIndex"(value: integer)
get "mipmapped"(): boolean
get "external"(): boolean
get "backend"(): integer
set "vkImageLayout"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VkBackendTexture$Type = ($VkBackendTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VkBackendTexture_ = $VkBackendTexture$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Type$Field" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Modifiers, $Modifiers$Type} from "packages/icyllis/arc3d/compiler/tree/$Modifiers"

export class $Type$Field extends $Record {

constructor(position: integer, modifiers: $Modifiers$Type, type: $Type$Type, name: string)

public "modifiers"(): $Modifiers
public "name"(): string
public "type"(): $Type
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "position"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Type$Field$Type = ($Type$Field);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Type$Field_ = $Type$Field$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ResourceCache" {
import {$IScratchKey, $IScratchKey$Type} from "packages/icyllis/arc3d/engine/$IScratchKey"
import {$GpuResource, $GpuResource$Type} from "packages/icyllis/arc3d/engine/$GpuResource"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$SurfaceProvider, $SurfaceProvider$Type} from "packages/icyllis/arc3d/engine/$SurfaceProvider"
import {$ThreadSafeCache, $ThreadSafeCache$Type} from "packages/icyllis/arc3d/engine/$ThreadSafeCache"
import {$IUniqueKey, $IUniqueKey$Type} from "packages/icyllis/arc3d/engine/$IUniqueKey"

export class $ResourceCache implements $AutoCloseable {


public "close"(): void
public "cleanup"(): boolean
public "getResourceCount"(): integer
public "findAndRefUniqueResource"(key: $IUniqueKey$Type): $GpuResource
public "findAndRefScratchResource"(key: $IScratchKey$Type): $GpuResource
public "purgeFreeResourcesUpToBytes"(bytesToPurge: long, preferScratch: boolean): void
public "purgeFreeResourcesOlderThan"(timeMillis: long, scratchOnly: boolean): void
public "purgeFreeResourcesToReserveBytes"(bytesToReserve: long): boolean
public "getMaxResourceBytes"(): long
public "getBudgetedResourceBytes"(): long
public "getBudgetedResourceCount"(): integer
public "getFreeResourceBytes"(): long
public "getContextID"(): integer
public "setThreadSafeCache"(threadSafeCache: $ThreadSafeCache$Type): void
public "setSurfaceProvider"(surfaceProvider: $SurfaceProvider$Type): void
public "releaseAll"(): void
public "setCacheLimit"(maxBytes: long): void
public "hasUniqueKey"(key: $IUniqueKey$Type): boolean
public "purgeFreeResources"(scratchOnly: boolean): void
public "discardAll"(): void
public "isFlushNeeded"(): boolean
public "isOverBudget"(): boolean
public "getResourceBytes"(): long
get "resourceCount"(): integer
get "maxResourceBytes"(): long
get "budgetedResourceBytes"(): long
get "budgetedResourceCount"(): integer
get "freeResourceBytes"(): long
get "contextID"(): integer
set "threadSafeCache"(value: $ThreadSafeCache$Type)
set "surfaceProvider"(value: $SurfaceProvider$Type)
set "cacheLimit"(value: long)
get "flushNeeded"(): boolean
get "overBudget"(): boolean
get "resourceBytes"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceCache$Type = ($ResourceCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceCache_ = $ResourceCache$Type;
}}
declare module "packages/icyllis/arc3d/engine/$StencilSettings" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StencilSettings {
static readonly "STENCIL_TEST_ALWAYS": short
static readonly "STENCIL_TEST_NEVER": short
static readonly "STENCIL_TEST_GREATER": short
static readonly "STENCIL_TEST_GEQUAL": short
static readonly "STENCIL_TEST_LESS": short
static readonly "STENCIL_TEST_LEQUAL": short
static readonly "STENCIL_TEST_EQUAL": short
static readonly "STENCIL_TEST_NOTEQUAL": short
static readonly "STENCIL_TEST_COUNT": integer
static readonly "STENCIL_OP_KEEP": byte
static readonly "STENCIL_OP_ZERO": byte
static readonly "STENCIL_OP_REPLACE": byte
static readonly "STENCIL_OP_INVERT": byte
static readonly "STENCIL_OP_INC_WRAP": byte
static readonly "STENCIL_OP_DEC_WRAP": byte
static readonly "STENCIL_OP_INC_CLAMP": byte
static readonly "STENCIL_OP_DEC_CLAMP": byte
static readonly "STENCIL_OP_COUNT": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StencilSettings$Type = ($StencilSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StencilSettings_ = $StencilSettings$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TransferProcessor$ProgramImpl" {
import {$TransferProcessor$ProgramImpl$EmitArgs, $TransferProcessor$ProgramImpl$EmitArgs$Type} from "packages/icyllis/arc3d/engine/$TransferProcessor$ProgramImpl$EmitArgs"

export class $TransferProcessor$ProgramImpl {

constructor()

public "emitCode"(args: $TransferProcessor$ProgramImpl$EmitArgs$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferProcessor$ProgramImpl$Type = ($TransferProcessor$ProgramImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferProcessor$ProgramImpl_ = $TransferProcessor$ProgramImpl$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PriorityQueue$Access" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PriorityQueue$Access<E> {

 "getIndex"(arg0: E): integer
 "setIndex"(arg0: E, arg1: integer): void
}

export namespace $PriorityQueue$Access {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PriorityQueue$Access$Type<E> = ($PriorityQueue$Access<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PriorityQueue$Access_<E> = $PriorityQueue$Access$Type<(E)>;
}}
declare module "packages/icyllis/arc3d/compiler/$Operator" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $Operator extends $Enum<($Operator)> {
static readonly "ADD": $Operator
static readonly "SUB": $Operator
static readonly "MUL": $Operator
static readonly "DIV": $Operator
static readonly "MOD": $Operator
static readonly "SHL": $Operator
static readonly "SHR": $Operator
static readonly "LOGICAL_NOT": $Operator
static readonly "LOGICAL_AND": $Operator
static readonly "LOGICAL_OR": $Operator
static readonly "LOGICAL_XOR": $Operator
static readonly "BITWISE_NOT": $Operator
static readonly "BITWISE_AND": $Operator
static readonly "BITWISE_OR": $Operator
static readonly "BITWISE_XOR": $Operator
static readonly "ASSIGN": $Operator
static readonly "EQ": $Operator
static readonly "NE": $Operator
static readonly "LT": $Operator
static readonly "GT": $Operator
static readonly "LE": $Operator
static readonly "GE": $Operator
static readonly "ADD_ASSIGN": $Operator
static readonly "SUB_ASSIGN": $Operator
static readonly "MUL_ASSIGN": $Operator
static readonly "DIV_ASSIGN": $Operator
static readonly "MOD_ASSIGN": $Operator
static readonly "SHL_ASSIGN": $Operator
static readonly "SHR_ASSIGN": $Operator
static readonly "AND_ASSIGN": $Operator
static readonly "OR_ASSIGN": $Operator
static readonly "XOR_ASSIGN": $Operator
static readonly "INC": $Operator
static readonly "DEC": $Operator
static readonly "COMMA": $Operator
static readonly "PRECEDENCE_POSTFIX": integer
static readonly "PRECEDENCE_PREFIX": integer
static readonly "PRECEDENCE_MULTIPLICATIVE": integer
static readonly "PRECEDENCE_ADDITIVE": integer
static readonly "PRECEDENCE_SHIFT": integer
static readonly "PRECEDENCE_RELATIONAL": integer
static readonly "PRECEDENCE_EQUALITY": integer
static readonly "PRECEDENCE_BITWISE_AND": integer
static readonly "PRECEDENCE_BITWISE_XOR": integer
static readonly "PRECEDENCE_BITWISE_OR": integer
static readonly "PRECEDENCE_LOGICAL_AND": integer
static readonly "PRECEDENCE_LOGICAL_XOR": integer
static readonly "PRECEDENCE_LOGICAL_OR": integer
static readonly "PRECEDENCE_CONDITIONAL": integer
static readonly "PRECEDENCE_ASSIGNMENT": integer
static readonly "PRECEDENCE_SEQUENCE": integer
static readonly "PRECEDENCE_EXPRESSION": integer
static readonly "PRECEDENCE_STATEMENT": integer


public "toString"(): string
public static "values"(): ($Operator)[]
public static "valueOf"(name: string): $Operator
public "getPrettyName"(): string
public "removeAssignment"(): $Operator
public "isRelational"(): boolean
public "isAssignment"(): boolean
public "isValidForVectorOrMatrix"(): boolean
public "isOnlyValidForIntegers"(): boolean
public "getBinaryPrecedence"(): integer
public "determineBinaryType"(context: $Context$Type, left: $Type$Type, right: $Type$Type, out: ($Type$Type)[]): boolean
public "isEquality"(): boolean
get "prettyName"(): string
get "relational"(): boolean
get "assignment"(): boolean
get "validForVectorOrMatrix"(): boolean
get "onlyValidForIntegers"(): boolean
get "binaryPrecedence"(): integer
get "equality"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Operator$Type = (("logical_xor") | ("sub") | ("mul_assign") | ("mod") | ("dec") | ("logical_and") | ("bitwise_not") | ("mul") | ("bitwise_and") | ("lt") | ("add_assign") | ("bitwise_xor") | ("xor_assign") | ("div") | ("div_assign") | ("bitwise_or") | ("ge") | ("inc") | ("add") | ("shr_assign") | ("and_assign") | ("mod_assign") | ("eq") | ("gt") | ("logical_or") | ("comma") | ("shl") | ("sub_assign") | ("ne") | ("logical_not") | ("le") | ("or_assign") | ("shr") | ("shl_assign") | ("assign")) | ($Operator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Operator_ = $Operator$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$GlobalVariableDecl" {
import {$VariableDecl, $VariableDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$VariableDecl"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Node$ElementKind, $Node$ElementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ElementKind"
import {$TopLevelElement, $TopLevelElement$Type} from "packages/icyllis/arc3d/compiler/tree/$TopLevelElement"

export class $GlobalVariableDecl extends $TopLevelElement {
 "mPosition": integer

constructor(decl: $VariableDecl$Type)

public "toString"(): string
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getVariableDecl"(): $VariableDecl
public "getKind"(): $Node$ElementKind
get "variableDecl"(): $VariableDecl
get "kind"(): $Node$ElementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalVariableDecl$Type = ($GlobalVariableDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalVariableDecl_ = $GlobalVariableDecl$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$Parser" {
import {$TranslationUnit, $TranslationUnit$Type} from "packages/icyllis/arc3d/compiler/tree/$TranslationUnit"
import {$ModuleUnit, $ModuleUnit$Type} from "packages/icyllis/arc3d/compiler/$ModuleUnit"
import {$ShaderKind, $ShaderKind$Type} from "packages/icyllis/arc3d/compiler/$ShaderKind"
import {$CompileOptions, $CompileOptions$Type} from "packages/icyllis/arc3d/compiler/$CompileOptions"
import {$ShaderCompiler, $ShaderCompiler$Type} from "packages/icyllis/arc3d/compiler/$ShaderCompiler"

export class $Parser {

constructor(compiler: $ShaderCompiler$Type, kind: $ShaderKind$Type, options: $CompileOptions$Type, source: (character)[])

public "parse"(parent: $ModuleUnit$Type): $TranslationUnit
public "parseModule"(parent: $ModuleUnit$Type): $ModuleUnit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parser$Type = ($Parser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parser_ = $Parser$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VkBackendFormat" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"

export class $VkBackendFormat extends $BackendFormat {


public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "make"(format: integer, isExternal: boolean): $VkBackendFormat
public "getStencilBits"(): integer
public "getCompressionType"(): integer
public "getBytesPerBlock"(): integer
public "makeInternal"(): $BackendFormat
public "getChannelFlags"(): integer
public "isSRGB"(): boolean
public "getFormatKey"(): integer
public "getVkFormat"(): integer
public "isExternal"(): boolean
public "getBackend"(): integer
get "stencilBits"(): integer
get "compressionType"(): integer
get "bytesPerBlock"(): integer
get "channelFlags"(): integer
get "sRGB"(): boolean
get "formatKey"(): integer
get "vkFormat"(): integer
get "external"(): boolean
get "backend"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VkBackendFormat$Type = ($VkBackendFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VkBackendFormat_ = $VkBackendFormat$Type;
}}
declare module "packages/icyllis/arc3d/engine/$RecordingContext" {
import {$SharedContextInfo, $SharedContextInfo$Type} from "packages/icyllis/arc3d/engine/$SharedContextInfo"
import {$SurfaceProvider, $SurfaceProvider$Type} from "packages/icyllis/arc3d/engine/$SurfaceProvider"
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$RenderTaskManager, $RenderTaskManager$Type} from "packages/icyllis/arc3d/engine/$RenderTaskManager"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/engine/$Context"
import {$PipelineStateCache, $PipelineStateCache$Type} from "packages/icyllis/arc3d/engine/$PipelineStateCache"
import {$ThreadSafeCache, $ThreadSafeCache$Type} from "packages/icyllis/arc3d/engine/$ThreadSafeCache"
import {$GraphicsPipelineState, $GraphicsPipelineState$Type} from "packages/icyllis/arc3d/engine/$GraphicsPipelineState"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"

export class $RecordingContext extends $Context {


public "getMaxRenderTargetSize"(): integer
public "getRenderTaskManager"(): $RenderTaskManager
public "isSurfaceCompatible"(colorType: integer): boolean
public "findOrCreateGraphicsPipelineState"(pipelineInfo: $PipelineInfo$Type): $GraphicsPipelineState
public "getPipelineStateCache"(): $PipelineStateCache
public static "makeRecording"(context: $SharedContextInfo$Type): $RecordingContext
public "getThreadSafeCache"(): $ThreadSafeCache
public "checkOwnerThread"(): void
public "isDiscarded"(): boolean
public "isImageCompatible"(colorType: integer): boolean
public "getSurfaceProvider"(): $SurfaceProvider
public "getMaxTextureSize"(): integer
public "getOwnerThread"(): $Thread
public "isOwnerThread"(): boolean
get "maxRenderTargetSize"(): integer
get "renderTaskManager"(): $RenderTaskManager
get "pipelineStateCache"(): $PipelineStateCache
get "threadSafeCache"(): $ThreadSafeCache
get "discarded"(): boolean
get "surfaceProvider"(): $SurfaceProvider
get "maxTextureSize"(): integer
get "ownerThread"(): $Thread
get "ownerThread"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordingContext$Type = ($RecordingContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordingContext_ = $RecordingContext$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorCall" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$List, $List$Type} from "packages/java/util/$List"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorCall extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public static "convert"(context: $Context$Type, pos: integer, type: $Type$Type, args: $List$Type<($Expression$Type)>): $Expression
public "getComponentType"(): $Type
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getArguments"(): ($Expression)[]
public "getConstantValue"(i: integer): $OptionalDouble
public "isConstructorCall"(): boolean
get "componentType"(): $Type
get "arguments"(): ($Expression)[]
get "constructorCall"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorCall$Type = ($ConstructorCall);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorCall_ = $ConstructorCall$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLVertexArray" {
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$GLBuffer, $GLBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLBuffer"
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"

export class $GLVertexArray extends $ManagedResource {


public static "make"(device: $GLDevice$Type, geomProc: $GeometryProcessor$Type): $GLVertexArray
public "getHandle"(): integer
public "bindInstanceBuffer"(buffer: $GLBuffer$Type, offset: long): void
public "getInstanceStride"(): integer
public "discard"(): void
public "getVertexStride"(): integer
public "bindIndexBuffer"(buffer: $GLBuffer$Type): void
public "bindVertexBuffer"(buffer: $GLBuffer$Type, offset: long): void
get "handle"(): integer
get "instanceStride"(): integer
get "vertexStride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLVertexArray$Type = ($GLVertexArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLVertexArray_ = $GLVertexArray$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorCompoundCast" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorCompoundCast extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "make"(position: integer, type: $Type$Type, arg: $Expression$Type): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorCompoundCast$Type = ($ConstructorCompoundCast);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorCompoundCast_ = $ConstructorCompoundCast$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLFramebufferInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GLFramebufferInfo {
 "mFramebuffer": integer
 "mFormat": integer

constructor()

public "equals"(o: any): boolean
public "hashCode"(): integer
public "set"(info: $GLFramebufferInfo$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFramebufferInfo$Type = ($GLFramebufferInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFramebufferInfo_ = $GLFramebufferInfo$Type;
}}
declare module "packages/icyllis/arc3d/core/$Vector3" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"
import {$Quaternion, $Quaternion$Type} from "packages/icyllis/arc3d/core/$Quaternion"

export class $Vector3 {
 "x": float
 "y": float
 "z": float

constructor()
constructor(x: float, y: float, z: float)

public "add"(v: $Vector3$Type): void
public "equals"(o: any): boolean
public "length"(): float
public "toString"(): string
public "hashCode"(): integer
public "transform"(q: $Quaternion$Type): void
public "transform"(mat: $Matrix4$Type): void
public "dot"(x: float, y: float, z: float): float
public "dot"(v: $Vector3$Type): float
public "set"(v: $Vector3$Type): void
public "set"(x: float, y: float, z: float): void
public "copy"(): $Vector3
public "normalize"(): void
public "reverse"(): void
public "sum"(): float
public "sort"(): void
public "multiply"(v: $Vector3$Type): void
public "multiply"(s: float): void
public "multiply"(mx: float, my: float, mz: float): void
public "product"(): float
public "negate"(): void
public "subtract"(v: $Vector3$Type): void
public "isNormalized"(): boolean
public "equivalent"(v: $Vector3$Type): boolean
public "rotation"(angle: float): $Quaternion
public "projection"(v: $Vector3$Type): void
public "perpendicular"(): void
public "cross"(x: float, y: float, z: float): void
public "cross"(v: $Vector3$Type): void
public "minComponent"(v: $Vector3$Type): void
public "maxComponent"(v: $Vector3$Type): void
public "setZero"(): void
public "lengthSq"(): float
public "preTransform"(mat: $Matrix4$Type): void
get "normalized"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3$Type = ($Vector3);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3_ = $Vector3$Type;
}}
declare module "packages/icyllis/arc3d/core/$Vector4" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"

export class $Vector4 {
 "x": float
 "y": float
 "z": float
 "w": float

constructor()

public "transform"(mat: $Matrix4$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector4$Type = ($Vector4);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector4_ = $Vector4$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TransferProcessor" {
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$TransferProcessor$ProgramImpl, $TransferProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$TransferProcessor$ProgramImpl"
import {$BlendInfo, $BlendInfo$Type} from "packages/icyllis/arc3d/engine/$BlendInfo"
import {$Processor, $Processor$Type} from "packages/icyllis/arc3d/engine/$Processor"

export class $TransferProcessor extends $Processor {
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer


public "makeProgramImpl"(): $TransferProcessor$ProgramImpl
public "readsDstColor"(): boolean
public "getBlendInfo"(): $BlendInfo
public "isLCDCoverage"(): boolean
public "addToKey"(b: $KeyBuilder$Type): void
public "hasSecondaryOutput"(): boolean
get "blendInfo"(): $BlendInfo
get "lCDCoverage"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferProcessor$Type = ($TransferProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferProcessor_ = $TransferProcessor$Type;
}}
declare module "packages/icyllis/arc3d/core/$MarkerStack" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"

export class $MarkerStack {

constructor()

public "restore"(boundary: integer): void
public "setMarker"(id: integer, mat: $Matrix4$Type, boundary: integer): void
public "findMarker"(id: integer): $Matrix4
public "findMarker"(id: integer, out: $Matrix4$Type): boolean
public "findMarkerInverse"(id: integer): $Matrix4
public "findMarkerInverse"(id: integer, out: $Matrix4$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MarkerStack$Type = ($MarkerStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MarkerStack_ = $MarkerStack$Type;
}}
declare module "packages/icyllis/arc3d/core/$Matrixc" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Matrix, $Matrix$Type} from "packages/icyllis/arc3d/core/$Matrix"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export interface $Matrixc {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "store"(arg0: (float)[], arg1: integer): void
 "store"(arg0: (float)[]): void
 "store"(arg0: $Matrix$Type): void
 "store"(arg0: $ByteBuffer$Type): void
 "store"(arg0: $FloatBuffer$Type): void
 "store"(arg0: long): void
 "getType"(): integer
 "isIdentity"(): boolean
 "isFinite"(): boolean
 "m12"(): float
 "m14"(): float
 "m11"(): float
 "invert"(arg0: $Matrix$Type): boolean
 "getScaleX"(): float
 "getScaleY"(): float
 "mapRect"(r: $Rect2ic$Type, out: $Rect2i$Type): void
 "mapRect"(r: $Rect2fc$Type, out: $Rect2i$Type): void
 "mapRect"(r: $Rect2i$Type): void
 "mapRect"(arg0: $Rect2fc$Type, arg1: $Rect2f$Type): boolean
 "mapRect"(rect: $Rect2f$Type): boolean
 "mapRect"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Rect2i$Type): void
 "mapPoint"(p: (float)[]): void
 "getShearX"(): float
 "getShearY"(): float
 "m24"(): float
 "m41"(): float
 "m21"(): float
 "m42"(): float
 "m44"(): float
 "m22"(): float
 "getTranslateX"(): float
 "isSimilarity"(): boolean
 "getPerspY"(): float
 "storeAligned"(arg0: long): void
 "storeAligned"(arg0: $FloatBuffer$Type): void
 "storeAligned"(arg0: $ByteBuffer$Type): void
 "isTranslate"(): boolean
 "getMinScale"(): float
 "getMaxScale"(): float
 "getPerspX"(): float
 "mapPoints"(pts: (float)[]): void
 "mapPoints"(pts: (float)[], count: integer): void
 "mapPoints"(pts: (float)[], pos: integer, count: integer): void
 "mapPoints"(arg0: (float)[], arg1: integer, arg2: (float)[], arg3: integer, arg4: integer): void
 "mapPoints"(src: (float)[], dst: (float)[], count: integer): void
 "getTranslateY"(): float
 "preservesRightAngles"(): boolean
 "hasPerspective"(): boolean
 "isAxisAligned"(): boolean
 "mapRectOut"(r: $Rect2ic$Type, dst: $Rect2i$Type): void
 "mapRectOut"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: $Rect2i$Type): void
 "mapRectOut"(r: $Rect2i$Type): void
 "mapRectOut"(r: $Rect2fc$Type, dst: $Rect2i$Type): void
 "isScaleTranslate"(): boolean
}

export namespace $Matrixc {
const kIdentity_Mask: integer
const kTranslate_Mask: integer
const kScale_Mask: integer
const kAffine_Mask: integer
const kPerspective_Mask: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrixc$Type = ($Matrixc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrixc_ = $Matrixc$Type;
}}
declare module "packages/icyllis/arc3d/core/$Matrix4" {
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$Matrix, $Matrix$Type} from "packages/icyllis/arc3d/core/$Matrix"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Quaternion, $Quaternion$Type} from "packages/icyllis/arc3d/core/$Quaternion"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Vector3, $Vector3$Type} from "packages/icyllis/arc3d/core/$Vector3"
import {$Matrix3, $Matrix3$Type} from "packages/icyllis/arc3d/core/$Matrix3"
import {$Vector4, $Vector4$Type} from "packages/icyllis/arc3d/core/$Vector4"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $Matrix4 implements $Cloneable {
 "m11": float
 "m12": float
 "m13": float
 "m14": float
 "m21": float
 "m22": float
 "m23": float
 "m24": float
 "m31": float
 "m32": float
 "m33": float
 "m34": float
 "m41": float
 "m42": float
 "m43": float
 "m44": float

constructor(...a: (float)[])
constructor(m: $Matrix4$Type)
constructor()

public "add"(m: $Matrix4$Type): void
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "store"(a: (float)[]): void
public "store"(m: $Matrix4$Type): void
public "store"(a: $FloatBuffer$Type): void
public "store"(p: long): void
public "store"(a: (float)[], offset: integer): void
public "store"(a: $ByteBuffer$Type): void
public static "identity"(): $Matrix4
public "set"(a: (float)[]): void
public "set"(a: $ByteBuffer$Type): void
public "set"(m: $Matrix4$Type): void
public "set"(a: (float)[], offset: integer): void
public "set"(m11: float, m12: float, m13: float, m14: float, m21: float, m22: float, m23: float, m24: float, m31: float, m32: float, m33: float, m34: float, m41: float, m42: float, m43: float, m44: float): void
public "set"(p: long): void
public "set"(a: $FloatBuffer$Type): void
public "trace"(): float
public static "copy"(m: $Matrix4$Type): $Matrix4
public "isIdentity"(): boolean
public "setScale"(x: float, y: float, z: float): void
public "setScale"(s: $Vector3$Type): void
public "subtract"(m: $Matrix4$Type): void
public "normalizePerspective"(): void
public "invert"(dest: $Matrix4$Type): boolean
public "invert"(): boolean
public "setRotation"(q: $Quaternion$Type): void
public "mapRect"(left: float, top: float, right: float, bottom: float, dest: $Rect2f$Type): void
public "mapRect"(r: $Rect2fc$Type, dest: $Rect2i$Type): void
public "mapRect"(r: $Rect2f$Type): void
public "mapRect"(left: float, top: float, right: float, bottom: float, dest: $Rect2i$Type): void
public "mapRect"(r: $Rect2ic$Type, dest: $Rect2i$Type): void
public "mapRect"(r: $Rect2fc$Type, dest: $Rect2f$Type): void
public "preTranslate"(t: $Vector3$Type): void
public "preTranslate"(dx: float, dy: float, dz: float): void
public "preTranslate"(dx: float, dy: float): void
public "preConcat"(lhs: $Matrix4$Type): void
public "preConcat"(l11: float, l12: float, l13: float, l14: float, l21: float, l22: float, l23: float, l24: float, l31: float, l32: float, l33: float, l34: float, l41: float, l42: float, l43: float, l44: float): void
public "preConcat"(lhs: $Matrix3$Type): void
public "postConcat"(r11: float, r12: float, r13: float, r14: float, r21: float, r22: float, r23: float, r24: float, r31: float, r32: float, r33: float, r34: float, r41: float, r42: float, r43: float, r44: float): void
public "postConcat"(rhs: $Matrix4$Type): void
public "postConcat"(rhs: $Matrix3$Type): void
public "setIdentity"(): void
public "postTranslate"(t: $Vector3$Type): void
public "postTranslate"(dx: float, dy: float, dz: float): void
public "postTranslate"(dx: float, dy: float): void
public "mapPoint"(p: (float)[]): void
public "setOrthographic"(left: float, right: float, bottom: float, top: float, near: float, far: float, signed: boolean): void
public "setOrthographic"(left: float, right: float, bottom: float, top: float, near: float, far: float): $Matrix4
public "setOrthographic"(width: float, height: float, near: float, far: float, flipY: boolean): $Matrix4
public "determinant"(): float
public "setPerspective"(fov: double, aspect: double, near: float, far: float, signed: boolean): void
public "setPerspective"(fov: float, aspect: float, near: float, far: float): $Matrix4
public "setPerspective"(left: float, right: float, bottom: float, top: float, near: float, far: float): $Matrix4
public "setPerspectiveLH"(fov: double, aspect: double, near: float, far: float, signed: boolean): void
public "isAffine"(): boolean
public "isApproxEqual"(m: $Matrix4$Type): boolean
public "transpose"(): void
public "setZero"(): void
public "mapPointX"(x: float, y: float): float
public "preConcat2D"(lhs: $Matrixc$Type): void
public "mapPointY"(x: float, y: float): float
public static "makeTranslate"(x: float, y: float, z: float): $Matrix4
public static "makeOrthographic"(width: float, height: float, near: float, far: float, flipY: boolean): $Matrix4
public static "makeOrthographic"(left: float, right: float, bottom: float, top: float, near: float, far: float): $Matrix4
public static "makeScale"(x: float, y: float, z: float): $Matrix4
public static "makePerspective"(fov: float, aspect: float, near: float, far: float): $Matrix4
public static "makePerspective"(left: float, right: float, bottom: float, top: float, near: float, far: float): $Matrix4
public "preScaleX"(s: float): void
public "postTranslateX"(dx: float): void
public "preTranslateY"(dy: float): void
public "postConcat2D"(rhs: $Matrixc$Type): void
public "setOrthographicLH"(left: float, right: float, bottom: float, top: float, near: float, far: float, signed: boolean): void
public "postTranslateY"(dy: float): void
public "postScaleX"(s: float): void
public "preScaleY"(s: float): void
public "preTranslateZ"(dz: float): void
public "preTranslateX"(dx: float): void
public "postTranslateZ"(dz: float): void
public "setTranslate"(x: float, y: float, z: float): void
public "setTranslate"(t: $Vector3$Type): void
public "preRotateY"(angle: double): void
public "postRotateZ"(angle: double): void
public "setShear"(sxy: float, sxz: float, syx: float, syz: float, szx: float, szy: float): void
public "hasTranslation"(): boolean
public "preScaleZ"(s: float): void
public "postShear2D"(sx: float, sy: float): void
public "postShear"(sxy: float, sxz: float, syx: float, syz: float, szx: float, szy: float): void
public "postRotateX"(angle: double): void
public "preShear2D"(sx: float, sy: float): void
public "postRotate"(angleX: double, angleY: double, angleZ: double): void
public "postRotate"(x: double, y: double, z: double, angle: double): void
public "preRotateZ"(angle: double): void
public "preScale"(sx: float, sy: float, sz: float): void
public "preScale"(sx: float, sy: float): void
public "preScale"(s: $Vector3$Type): void
public "postScale"(s: $Vector3$Type): void
public "postScale"(sx: float, sy: float, sz: float): void
public "postScale"(sx: float, sy: float): void
public "preRotate"(angleX: double, angleY: double, angleZ: double): void
public "preRotate"(axis: $Vector3$Type, angle: float): void
public "preRotate"(x: double, y: double, z: double, angle: double): void
public "preRotate"(q: $Quaternion$Type): void
public "preShear"(sxy: float, sxz: float, syx: float, syz: float, szx: float, szy: float): void
public "postRotateY"(angle: double): void
public "postScaleZ"(s: float): void
public "hasPerspective"(): boolean
public "preTransform"(vec: $Vector3$Type): void
public "preTransform"(vec: $Vector4$Type): void
public "postTransform"(vec: $Vector4$Type): void
public "postTransform"(vec: $Vector3$Type): void
public "postScaleY"(s: float): void
public "preRotateX"(angle: double): void
public "mapVec3"(vec: (float)[]): void
public "toMatrix"(dest: $Matrix$Type): void
public "toMatrix"(): $Matrix
public "isAxisAligned"(): boolean
public "mapRectOut"(r: $Rect2ic$Type, dest: $Rect2i$Type): void
public "mapRectOut"(r: $Rect2fc$Type, dest: $Rect2i$Type): void
public "mapRectOut"(left: float, top: float, right: float, bottom: float, dest: $Rect2i$Type): void
public "toMatrix3"(dest: $Matrix3$Type): void
public "toMatrix3"(): $Matrix3
public "isScaleTranslate"(): boolean
set "scale"(value: $Vector3$Type)
set "rotation"(value: $Quaternion$Type)
get "affine"(): boolean
set "translate"(value: $Vector3$Type)
get "axisAligned"(): boolean
get "scaleTranslate"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix4$Type = ($Matrix4);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix4_ = $Matrix4$Type;
}}
declare module "packages/icyllis/arc3d/core/$Matrix3" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Matrix3 {
 "m11": float
 "m12": float
 "m13": float
 "m21": float
 "m22": float
 "m23": float
 "m31": float
 "m32": float
 "m33": float

constructor()

public "store"(p: long): void
public static "identity"(): $Matrix3
public "setIdentity"(): void
public "storeAligned"(p: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Matrix3$Type = ($Matrix3);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Matrix3_ = $Matrix3$Type;
}}
declare module "packages/icyllis/arc3d/core/$Paint" {
import {$Blender, $Blender$Type} from "packages/icyllis/arc3d/core/$Blender"
import {$MaskFilter, $MaskFilter$Type} from "packages/icyllis/arc3d/core/$MaskFilter"
import {$ColorFilter, $ColorFilter$Type} from "packages/icyllis/arc3d/core/$ColorFilter"
import {$BlendMode, $BlendMode$Type} from "packages/icyllis/arc3d/core/$BlendMode"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Shader, $Shader$Type} from "packages/icyllis/arc3d/core/$Shader"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$ImageFilter, $ImageFilter$Type} from "packages/icyllis/arc3d/core/$ImageFilter"

export class $Paint {
static readonly "FILL": integer
static readonly "STROKE": integer
static readonly "STROKE_AND_FILL": integer
static readonly "FILL_AND_STROKE": integer
static readonly "CAP_BUTT": integer
static readonly "CAP_ROUND": integer
static readonly "CAP_SQUARE": integer
static readonly "JOIN_MITER": integer
static readonly "JOIN_ROUND": integer
static readonly "JOIN_BEVEL": integer
static readonly "ALIGN_CENTER": integer
static readonly "ALIGN_INSIDE": integer
static readonly "ALIGN_OUTSIDE": integer
static readonly "FILTER_MODE_NEAREST": integer
static readonly "FILTER_MODE_LINEAR": integer
static readonly "MIPMAP_MODE_NONE": integer
static readonly "MIPMAP_MODE_NEAREST": integer
static readonly "MIPMAP_MODE_LINEAR": integer

constructor()
constructor(paint: $Paint$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "b"(): float
public "a"(): float
public "set"(paint: $Paint$Type): void
public "g"(): float
public "reset"(): void
public "r"(): float
public "setColor"(color: integer): void
public "setFilter"(filter: boolean): void
public "setShader"(shader: $Shader$Type): void
public "getShader"(): $Shader
public "setRGBA"(r: float, g: float, b: float, a: float): void
public "setRGBA"(r: integer, g: integer, b: integer, a: integer): void
public "setRGB"(r: integer, g: integer, b: integer): void
public "setRGB"(r: float, g: float, b: float): void
public "getAlpha"(): integer
public "getColor"(): integer
public "setAlpha"(a: integer): void
public "setStrokeWidth"(width: float): void
public "getStrokeWidth"(): float
public "setStyle"(style: integer): void
public "getColorFilter"(): $ColorFilter
public "getStrokeCap"(): integer
public "getSmoothWidth"(): float
public "getMaxAnisotropy"(): integer
public "setSmoothWidth"(smooth: float): void
public "setAntiAlias"(aa: boolean): void
public "setStrokeMiter"(miter: float): void
public "setStrokeCap"(cap: integer): void
public "setStrokeJoin"(join: integer): void
public "getStrokeAlign"(): integer
public "getStrokeMiter"(): float
public "setFilterMode"(filter: integer): void
public "getStrokeJoin"(): integer
public "getMipmapMode"(): integer
public "setMaxAnisotropy"(maxAnisotropy: integer): void
public "getAlphaF"(): float
public "isDither"(): boolean
public "setStrokeAlign"(align: integer): void
public "setDither"(dither: boolean): void
public "setAlphaF"(a: float): void
public "setARGB"(a: float, r: float, g: float, b: float): void
public "setARGB"(a: integer, r: integer, g: integer, b: integer): void
public "isFilter"(): boolean
public "isAntiAlias"(): boolean
public "getFilterMode"(): integer
public "setMipmapMode"(mipmap: integer): void
public "setImageFilter"(imageFilter: $ImageFilter$Type): void
public "getImageFilter"(): $ImageFilter
public "isSrcOver"(): boolean
public "setBlender"(blender: $Blender$Type): void
public "nothingToDraw"(): boolean
public "getMaskFilter"(): $MaskFilter
public "setColorFilter"(colorFilter: $ColorFilter$Type): void
public "setMaskFilter"(maskFilter: $MaskFilter$Type): void
public static "getBlendModeDirect"(paint: $Paint$Type): $BlendMode
public "getBlender"(): $Blender
public "computeFastBounds"(orig: $Rect2fc$Type, storage: $Rect2f$Type): void
public static "getAlphaDirect"(paint: $Paint$Type): integer
public static "isBlendedShader"(shader: $Shader$Type): boolean
public static "isOpaquePaint"(paint: $Paint$Type): boolean
public "getStyle"(): integer
public "isAnisotropy"(): boolean
public "setStroke"(stroke: boolean): void
public static "isBlendedColorFilter"(filter: $ColorFilter$Type): boolean
public "canComputeFastBounds"(): boolean
public static "isBlendedImageFilter"(filter: $ImageFilter$Type): boolean
set "color"(value: integer)
set "filter"(value: boolean)
set "shader"(value: $Shader$Type)
get "shader"(): $Shader
get "alpha"(): integer
get "color"(): integer
set "alpha"(value: integer)
set "strokeWidth"(value: float)
get "strokeWidth"(): float
set "style"(value: integer)
get "colorFilter"(): $ColorFilter
get "strokeCap"(): integer
get "smoothWidth"(): float
get "maxAnisotropy"(): integer
set "smoothWidth"(value: float)
set "antiAlias"(value: boolean)
set "strokeMiter"(value: float)
set "strokeCap"(value: integer)
set "strokeJoin"(value: integer)
get "strokeAlign"(): integer
get "strokeMiter"(): float
set "filterMode"(value: integer)
get "strokeJoin"(): integer
get "mipmapMode"(): integer
set "maxAnisotropy"(value: integer)
get "alphaF"(): float
get "dither"(): boolean
set "strokeAlign"(value: integer)
set "dither"(value: boolean)
set "alphaF"(value: float)
get "filter"(): boolean
get "antiAlias"(): boolean
get "filterMode"(): integer
set "mipmapMode"(value: integer)
set "imageFilter"(value: $ImageFilter$Type)
get "imageFilter"(): $ImageFilter
get "srcOver"(): boolean
set "blender"(value: $Blender$Type)
get "maskFilter"(): $MaskFilter
set "colorFilter"(value: $ColorFilter$Type)
set "maskFilter"(value: $MaskFilter$Type)
get "blender"(): $Blender
get "style"(): integer
get "anisotropy"(): boolean
set "stroke"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Paint$Type = ($Paint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Paint_ = $Paint$Type;
}}
declare module "packages/icyllis/arc3d/engine/$LinkedListMultimap" {
import {$LinkedList, $LinkedList$Type} from "packages/java/util/$LinkedList"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $LinkedListMultimap<K, V> extends $HashMap<(K), ($LinkedList<(V)>)> {

constructor()
constructor(other: $Map$Type<(any), (any)>)

public "pollFirstEntry"(k: K): V
public "pollLastEntry"(k: K): V
public "removeLastEntry"(k: K, v: V): void
public "removeFirstEntry"(k: K, v: V): void
public "addFirstEntry"(k: K, v: V): void
public "addLastEntry"(k: K, v: V): void
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
export type $LinkedListMultimap$Type<K, V> = ($LinkedListMultimap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinkedListMultimap_<K, V> = $LinkedListMultimap$Type<(K), (V)>;
}}
declare module "packages/icyllis/arc3d/engine/effects/$HardXferProc" {
import {$TransferProcessor, $TransferProcessor$Type} from "packages/icyllis/arc3d/engine/$TransferProcessor"
import {$TransferProcessor$ProgramImpl, $TransferProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$TransferProcessor$ProgramImpl"
import {$BlendInfo, $BlendInfo$Type} from "packages/icyllis/arc3d/engine/$BlendInfo"

export class $HardXferProc extends $TransferProcessor {
static readonly "SIMPLE_SRC_OVER": $HardXferProc
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer

constructor(primaryOutputType: integer, secondaryOutputType: integer, isLCDCoverage: boolean, blendInfo: $BlendInfo$Type)

public "name"(): string
public "makeProgramImpl"(): $TransferProcessor$ProgramImpl
public "getBlendInfo"(): $BlendInfo
public "hasSecondaryOutput"(): boolean
get "blendInfo"(): $BlendInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HardXferProc$Type = ($HardXferProc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HardXferProc_ = $HardXferProc$Type;
}}
declare module "packages/icyllis/arc3d/engine/tessellate/$WangsFormula" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WangsFormula {


public static "quadratic"(precision: float, x0: float, y0: float, x1: float, y1: float, x2: float, y2: float): float
public static "cubic"(precision: float, x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float): float
public static "cubic_p4"(precision: float, x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float): float
public static "quadratic_p4"(precision: float, x0: float, y0: float, x1: float, y1: float, x2: float, y2: float): float
public static "worst_cubic"(precision: float, devWidth: float, devHeight: float): float
public static "worst_cubic_log2"(precision: float, devWidth: float, devHeight: float): integer
public static "worst_cubic_p4"(precision: float, devWidth: float, devHeight: float): float
public static "quadratic_log2"(precision: float, x0: float, y0: float, x1: float, y1: float, x2: float, y2: float): integer
public static "cubic_log2"(precision: float, x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WangsFormula$Type = ($WangsFormula);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WangsFormula_ = $WangsFormula$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Mesh" {
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export interface $Mesh {

 "setInstanceBuffer"(buffer: $GpuBuffer$Type, baseInstance: integer, actualInstanceCount: integer): void
 "getInstanceCount"(): integer
 "getInstanceSize"(): integer
 "getVertexCount"(): integer
 "setVertexBuffer"(buffer: $GpuBuffer$Type, baseVertex: integer, actualVertexCount: integer): void
 "setIndexBuffer"(buffer: $GpuBuffer$Type, baseIndex: integer, actualIndexCount: integer): void
 "getVertexSize"(): integer
 "getIndexCount"(): integer
}

export namespace $Mesh {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mesh$Type = ($Mesh);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mesh_ = $Mesh$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$Mangler" {
import {$SymbolTable, $SymbolTable$Type} from "packages/icyllis/arc3d/compiler/$SymbolTable"

export class $Mangler {

constructor()

public "reset"(): void
public "uniqueName"(baseName: string, symbolTable: $SymbolTable$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mangler$Type = ($Mangler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mangler_ = $Mangler$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ClipResult" {
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export class $ClipResult implements $Cloneable {

constructor()

public "equals"(o: any): boolean
public "hashCode"(): integer
public "init"(logicalWidth: integer, logicalHeight: integer, physicalWidth: integer, physicalHeight: integer): $ClipResult
public "hasScissorClip"(): boolean
public "hasStencilClip"(): boolean
public "hasClip"(): boolean
public "setScissor"(l: integer, t: integer, r: integer, b: integer): void
public "setStencil"(seq: integer): void
public "getScissorX0"(): integer
public "getScissorY0"(): integer
public "getScissorX1"(): integer
public "addScissor"(rect: $Rect2ic$Type, clippedBounds: $Rect2f$Type): boolean
public "getScissorY1"(): integer
public "getStencilSeq"(): integer
set "stencil"(value: integer)
get "scissorX0"(): integer
get "scissorY0"(): integer
get "scissorX1"(): integer
get "scissorY1"(): integer
get "stencilSeq"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClipResult$Type = ($ClipResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClipResult_ = $ClipResult$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Variable" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$VariableDecl, $VariableDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$VariableDecl"
import {$InterfaceBlock, $InterfaceBlock$Type} from "packages/icyllis/arc3d/compiler/tree/$InterfaceBlock"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Node$SymbolKind, $Node$SymbolKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$SymbolKind"
import {$Symbol, $Symbol$Type} from "packages/icyllis/arc3d/compiler/tree/$Symbol"
import {$GlobalVariableDecl, $GlobalVariableDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$GlobalVariableDecl"
import {$Modifiers, $Modifiers$Type} from "packages/icyllis/arc3d/compiler/tree/$Modifiers"

export class $Variable extends $Symbol {
static readonly "kLocal_Storage": byte
static readonly "kGlobal_Storage": byte
static readonly "kParameter_Storage": byte
 "mPosition": integer

constructor(position: integer, modifiers: $Modifiers$Type, type: $Type$Type, name: string, storage: byte, builtin: boolean)

public "toString"(): string
public "getModifiers"(): $Modifiers
public static "convert"(context: $Context$Type, pos: integer, modifiers: $Modifiers$Type, type: $Type$Type, name: string, storage: byte): $Variable
public static "make"(pos: integer, modifiers: $Modifiers$Type, type: $Type$Type, name: string, storage: byte, builtin: boolean): $Variable
public "getType"(): $Type
public "initialValue"(): $Expression
public "isBuiltin"(): boolean
public "getVariableDecl"(): $VariableDecl
public "getInterfaceBlock"(): $InterfaceBlock
public "getStorage"(): byte
public "getKind"(): $Node$SymbolKind
public "getGlobalVariableDecl"(): $GlobalVariableDecl
public "setGlobalVariableDecl"(globalDecl: $GlobalVariableDecl$Type): void
public "setVariableDecl"(decl: $VariableDecl$Type): void
public "setInterfaceBlock"(interfaceBlock: $InterfaceBlock$Type): void
get "modifiers"(): $Modifiers
get "type"(): $Type
get "builtin"(): boolean
get "variableDecl"(): $VariableDecl
get "interfaceBlock"(): $InterfaceBlock
get "storage"(): byte
get "kind"(): $Node$SymbolKind
get "globalVariableDecl"(): $GlobalVariableDecl
set "globalVariableDecl"(value: $GlobalVariableDecl$Type)
set "variableDecl"(value: $VariableDecl$Type)
set "interfaceBlock"(value: $InterfaceBlock$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Variable$Type = ($Variable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Variable_ = $Variable$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$PrefixExpression" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Operator, $Operator$Type} from "packages/icyllis/arc3d/compiler/$Operator"

export class $PrefixExpression extends $Expression {
 "mPosition": integer

constructor(position: integer, op: $Operator$Type, operand: $Expression$Type)

public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, op: $Operator$Type, base: $Expression$Type): $Expression
public static "make"(context: $Context$Type, position: integer, op: $Operator$Type, base: $Expression$Type): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getOperator"(): $Operator
public "getOperand"(): $Expression
public "getKind"(): $Node$ExpressionKind
get "operator"(): $Operator
get "operand"(): $Expression
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrefixExpression$Type = ($PrefixExpression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrefixExpression_ = $PrefixExpression$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$VertexShaderBuilder" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"
import {$ShaderBuilderBase, $ShaderBuilderBase$Type} from "packages/icyllis/arc3d/engine/shading/$ShaderBuilderBase"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$VertexGeomBuilder, $VertexGeomBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$VertexGeomBuilder"
import {$PipelineBuilder, $PipelineBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$PipelineBuilder"

export class $VertexShaderBuilder extends $ShaderBuilderBase implements $VertexGeomBuilder {

constructor(pipelineBuilder: $PipelineBuilder$Type)

public "emitAttributes"(geomProc: $GeometryProcessor$Type): void
public "emitNormalizedPosition"(devicePos: $ShaderVar$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VertexShaderBuilder$Type = ($VertexShaderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VertexShaderBuilder_ = $VertexShaderBuilder$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$CircularRRectOp" {
import {$MeshDrawOp, $MeshDrawOp$Type} from "packages/icyllis/arc3d/engine/ops/$MeshDrawOp"
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"

export class $CircularRRectOp extends $MeshDrawOp {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor()

public "onExecute"(state: $OpFlushState$Type, chainBounds: $Rect2f$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CircularRRectOp$Type = ($CircularRRectOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CircularRRectOp_ = $CircularRRectOp$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$MeshDrawOp" {
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$DrawOp, $DrawOp$Type} from "packages/icyllis/arc3d/engine/ops/$DrawOp"
import {$Mesh, $Mesh$Type} from "packages/icyllis/arc3d/engine/$Mesh"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$GraphicsPipelineState, $GraphicsPipelineState$Type} from "packages/icyllis/arc3d/engine/$GraphicsPipelineState"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $MeshDrawOp extends $DrawOp implements $Mesh {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor()

public "onPrePrepare"(context: $RecordingContext$Type, writeView: $SurfaceView$Type, pipelineFlags: integer): void
public "getPipelineInfo"(): $PipelineInfo
public "getPipelineState"(): $GraphicsPipelineState
public "onPrepare"(state: $OpFlushState$Type, writeView: $SurfaceView$Type, pipelineFlags: integer): void
public "getInstanceSize"(): integer
public "getVertexSize"(): integer
public "setInstanceBuffer"(buffer: $GpuBuffer$Type, baseInstance: integer, actualInstanceCount: integer): void
public "getInstanceCount"(): integer
public "getVertexCount"(): integer
public "setVertexBuffer"(buffer: $GpuBuffer$Type, baseVertex: integer, actualVertexCount: integer): void
public "setIndexBuffer"(buffer: $GpuBuffer$Type, baseIndex: integer, actualIndexCount: integer): void
public "getIndexCount"(): integer
get "pipelineInfo"(): $PipelineInfo
get "pipelineState"(): $GraphicsPipelineState
get "instanceSize"(): integer
get "vertexSize"(): integer
get "instanceCount"(): integer
get "vertexCount"(): integer
get "indexCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MeshDrawOp$Type = ($MeshDrawOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MeshDrawOp_ = $MeshDrawOp$Type;
}}
declare module "packages/icyllis/arc3d/engine/$OpsRenderPass" {
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$GraphicsPipelineState, $GraphicsPipelineState$Type} from "packages/icyllis/arc3d/engine/$GraphicsPipelineState"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $OpsRenderPass {

constructor()
constructor(fs: $GpuRenderTarget$Type, origin: integer)

public "end"(): void
public "begin"(): void
public "clearColor"(left: integer, top: integer, right: integer, bottom: integer, red: float, green: float, blue: float, alpha: float): void
public "clearStencil"(left: integer, top: integer, right: integer, bottom: integer, insideMask: boolean): void
public "draw"(vertexCount: integer, baseVertex: integer): void
public "bindPipeline"(pipelineInfo: $PipelineInfo$Type, pipelineState: $GraphicsPipelineState$Type, drawBounds: $Rect2fc$Type): void
public "drawIndexed"(indexCount: integer, baseIndex: integer, baseVertex: integer): void
public "bindBuffers"(indexBuffer: $GpuBuffer$Type, vertexBuffer: $GpuBuffer$Type, instanceBuffer: $GpuBuffer$Type): void
public "bindTextures"(geomTextures: ($TextureProxy$Type)[]): void
public "drawInstanced"(instanceCount: integer, baseInstance: integer, vertexCount: integer, baseVertex: integer): void
public "drawIndexedInstanced"(indexCount: integer, baseIndex: integer, instanceCount: integer, baseInstance: integer, baseVertex: integer): void
public "bindTexture"(geomTexture: $TextureProxy$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpsRenderPass$Type = ($OpsRenderPass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpsRenderPass_ = $OpsRenderPass$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ShaderKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ShaderKind extends $Enum<($ShaderKind)> {
static readonly "BASE": $ShaderKind
static readonly "VERTEX": $ShaderKind
static readonly "FRAGMENT": $ShaderKind
static readonly "COMPUTE": $ShaderKind
static readonly "SUBROUTINE": $ShaderKind
static readonly "SUBROUTINE_SHADER": $ShaderKind
static readonly "SUBROUTINE_COLOR_FILTER": $ShaderKind
static readonly "SUBROUTINE_BLENDER": $ShaderKind
static readonly "PRIVATE_SUBROUTINE_SHADER": $ShaderKind
static readonly "PRIVATE_SUBROUTINE_COLOR_FILTER": $ShaderKind
static readonly "PRIVATE_SUBROUTINE_BLENDER": $ShaderKind


public static "values"(): ($ShaderKind)[]
public static "valueOf"(name: string): $ShaderKind
public "isVertex"(): boolean
public "isFragment"(): boolean
public "isCompute"(): boolean
public "isAnySubroutine"(): boolean
get "vertex"(): boolean
get "fragment"(): boolean
get "compute"(): boolean
get "anySubroutine"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderKind$Type = (("compute") | ("private_subroutine_shader") | ("fragment") | ("vertex") | ("private_subroutine_color_filter") | ("subroutine_color_filter") | ("subroutine") | ("private_subroutine_blender") | ("subroutine_blender") | ("base") | ("subroutine_shader")) | ($ShaderKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderKind_ = $ShaderKind$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$ShaderBuilder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ShaderBuilder {

 "getMangledName"(arg0: string): string
 "codeAppend"(arg0: string): void
 "codeAppendf"(arg0: string, ...arg1: (any)[]): void
 "codePrependf"(arg0: string, ...arg1: (any)[]): void
}

export namespace $ShaderBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderBuilder$Type = ($ShaderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderBuilder_ = $ShaderBuilder$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanMemoryAllocator" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export class $VulkanMemoryAllocator implements $AutoCloseable {


public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanMemoryAllocator$Type = ($VulkanMemoryAllocator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanMemoryAllocator_ = $VulkanMemoryAllocator$Type;
}}
declare module "packages/icyllis/arc3d/engine/$FragmentProcessor" {
import {$Processor, $Processor$Type} from "packages/icyllis/arc3d/engine/$Processor"

export class $FragmentProcessor extends $Processor {
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FragmentProcessor$Type = ($FragmentProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FragmentProcessor_ = $FragmentProcessor$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Context" {
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$SharedContextInfo, $SharedContextInfo$Type} from "packages/icyllis/arc3d/engine/$SharedContextInfo"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"

export class $Context extends $RefCnt {


public "matches"(c: $Context$Type): boolean
public "getOptions"(): $ContextOptions
public "getErrorWriter"(): $PrintWriter
public "getMaxSurfaceSampleCount"(colorType: integer): integer
public "getCompressedBackendFormat"(compressionType: integer): $BackendFormat
public "getDefaultBackendFormat"(colorType: integer, renderable: boolean): $BackendFormat
public "getBackend"(): integer
public "getCaps"(): $Caps
public "getContextInfo"(): $SharedContextInfo
public "getContextID"(): integer
get "options"(): $ContextOptions
get "errorWriter"(): $PrintWriter
get "backend"(): integer
get "caps"(): $Caps
get "contextInfo"(): $SharedContextInfo
get "contextID"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Context$Type = ($Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Context_ = $Context$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VkBackendContext" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $VkBackendContext {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VkBackendContext$Type = ($VkBackendContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VkBackendContext_ = $VkBackendContext$Type;
}}
declare module "packages/icyllis/arc3d/engine/$RenderTextureProxy" {
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"

export class $RenderTextureProxy extends $TextureProxy {


public "getResolveRect"(): $Rect2ic
public "getSampleCount"(): integer
public "getGpuRenderTarget"(): $GpuRenderTarget
public "needsResolve"(): boolean
public "setResolveRect"(left: integer, top: integer, right: integer, bottom: integer): void
public "ref"(): void
public static "getApproxSize"(size: integer): integer
public "unref"(): void
get "resolveRect"(): $Rect2ic
get "sampleCount"(): integer
get "gpuRenderTarget"(): $GpuRenderTarget
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTextureProxy$Type = ($RenderTextureProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTextureProxy_ = $RenderTextureProxy$Type;
}}
declare module "packages/icyllis/arc3d/engine/geom/$SDFRectGeoProc" {
import {$GeometryProcessor$AttributeSet, $GeometryProcessor$AttributeSet$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$AttributeSet"
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"

export class $SDFRectGeoProc extends $GeometryProcessor {
static readonly "POSITION": $GeometryProcessor$Attribute
static readonly "COLOR": $GeometryProcessor$Attribute
static readonly "BOX": $GeometryProcessor$Attribute
static readonly "STROKE": $GeometryProcessor$Attribute
static readonly "VIEW_MATRIX": $GeometryProcessor$Attribute
static readonly "VERTEX_ATTRIBS": $GeometryProcessor$AttributeSet
static readonly "INSTANCE_ATTRIBS": $GeometryProcessor$AttributeSet
static readonly "FLAG_ANTIALIASING": integer
static readonly "FLAG_STROKE": integer
static readonly "FLAG_INSTANCED_MATRIX": integer
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer

constructor(flags: integer)

public "name"(): string
public "primitiveType"(): byte
public "makeProgramImpl"(caps: $ShaderCaps$Type): $GeometryProcessor$ProgramImpl
public "appendToKey"(b: $KeyBuilder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SDFRectGeoProc$Type = ($SDFRectGeoProc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SDFRectGeoProc_ = $SDFRectGeoProc$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Node$ElementKind" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Node$ElementKind extends $Enum<($Node$ElementKind)> {
static readonly "EXTENSION": $Node$ElementKind
static readonly "FUNCTION_DEFINITION": $Node$ElementKind
static readonly "FUNCTION_PROTOTYPE": $Node$ElementKind
static readonly "GLOBAL_VARIABLE": $Node$ElementKind
static readonly "INTERFACE_BLOCK": $Node$ElementKind
static readonly "MODIFIERS": $Node$ElementKind
static readonly "STRUCT_DEFINITION": $Node$ElementKind


public static "values"(): ($Node$ElementKind)[]
public static "valueOf"(name: string): $Node$ElementKind
public "getType"(): $Class<(any)>
get "type"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$ElementKind$Type = (("interface_block") | ("extension") | ("struct_definition") | ("function_prototype") | ("modifiers") | ("function_definition") | ("global_variable")) | ($Node$ElementKind);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node$ElementKind_ = $Node$ElementKind$Type;
}}
declare module "packages/icyllis/arc3d/core/$DisplayList" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DisplayList {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayList$Type = ($DisplayList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayList_ = $DisplayList$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ForLoop" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $ForLoop extends $Statement {
 "mPosition": integer

constructor(position: integer, init: $Statement$Type, condition: $Expression$Type, step: $Expression$Type, statement: $Statement$Type)

public "toString"(): string
public static "convert"(context: $Context$Type, pos: integer, init: $Statement$Type, cond: $Expression$Type, step: $Expression$Type, statement: $Statement$Type): $Statement
public static "make"(pos: integer, init: $Statement$Type, cond: $Expression$Type, step: $Expression$Type, statement: $Statement$Type): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "setStatement"(statement: $Statement$Type): void
public "setCondition"(condition: $Expression$Type): void
public "setInit"(init: $Statement$Type): void
public "getInit"(): $Statement
public "getStep"(): $Expression
public "getStatement"(): $Statement
public "getCondition"(): $Expression
public "getKind"(): $Node$StatementKind
public "setStep"(step: $Expression$Type): void
set "statement"(value: $Statement$Type)
set "condition"(value: $Expression$Type)
set "init"(value: $Statement$Type)
get "init"(): $Statement
get "step"(): $Expression
get "statement"(): $Statement
get "condition"(): $Expression
get "kind"(): $Node$StatementKind
set "step"(value: $Expression$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForLoop$Type = ($ForLoop);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForLoop_ = $ForLoop$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PathUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PathUtils {
static readonly "DEFAULT_TOLERANCE": float
static readonly "MAX_CHOPS_PER_CURVE": integer
static readonly "MAX_POINTS_PER_CURVE": integer


public static "countCubicPoints"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, x3: float, y3: float, tol: float): integer
public static "generateQuadraticPoints"(p0x: float, p0y: float, p1x: float, p1y: float, p2x: float, p2y: float, tolSq: float, dst: (float)[], off: integer, rem: integer): integer
public static "generateCubicPoints"(p0x: float, p0y: float, p1x: float, p1y: float, p2x: float, p2y: float, p3x: float, p3y: float, tolSq: float, dst: (float)[], off: integer, rem: integer): integer
public static "countQuadraticPoints"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, tol: float): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathUtils$Type = ($PathUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathUtils_ = $PathUtils$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$Op" {
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$TextureVisitor, $TextureVisitor$Type} from "packages/icyllis/arc3d/engine/$TextureVisitor"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"

export class $Op extends $Rect2f {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor()

public "mayChain"(op: $Op$Type): boolean
public "isChainTail"(): boolean
public "hasZeroArea"(): boolean
public "setClippedBounds"(clippedBounds: $Rect2f$Type): void
public "prevInChain"(): $Op
public "onPrePrepare"(arg0: $RecordingContext$Type, arg1: $SurfaceView$Type, arg2: integer): void
public "chainSplit"(): $Op
public "hasAABloat"(): boolean
public "isChainHead"(): boolean
public "chainConcat"(next: $Op$Type): void
public "onPrepare"(arg0: $OpFlushState$Type, arg1: $SurfaceView$Type, arg2: integer): void
public "visitProxies"(func: $TextureVisitor$Type): void
public "onExecute"(arg0: $OpFlushState$Type, arg1: $Rect2f$Type): void
public "validateChain"(expectedTail: $Op$Type): boolean
public "nextInChain"(): $Op
get "chainTail"(): boolean
set "clippedBounds"(value: $Rect2f$Type)
get "chainHead"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Op$Type = ($Op);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Op_ = $Op$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$TopLevelElement" {
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"
import {$Node$ElementKind, $Node$ElementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ElementKind"

export class $TopLevelElement extends $Node {
 "mPosition": integer


public "getKind"(): $Node$ElementKind
get "kind"(): $Node$ElementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TopLevelElement$Type = ($TopLevelElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TopLevelElement_ = $TopLevelElement$Type;
}}
declare module "packages/icyllis/arc3d/core/$PixelRef" {
import {$LongConsumer, $LongConsumer$Type} from "packages/java/util/function/$LongConsumer"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"

export class $PixelRef extends $RefCnt {

constructor(width: integer, height: integer, base: any, address: long, rowStride: integer, freeFn: $LongConsumer$Type)

public "toString"(): string
public "getAddress"(): long
public "getBase"(): any
public static "releaseBaseElements"(base: any, elems: long, write: boolean): void
public "isImmutable"(): boolean
public "getWidth"(): integer
public "getHeight"(): integer
public "setImmutable"(): void
public static "getBaseRegion"(base: any, start: integer, len: integer, buf: long): void
public static "getBaseElements"(base: any): long
public "getRowStride"(): integer
get "address"(): long
get "base"(): any
get "immutable"(): boolean
get "width"(): integer
get "height"(): integer
get "rowStride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PixelRef$Type = ($PixelRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PixelRef_ = $PixelRef$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$AnonymousField" {
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Node$SymbolKind, $Node$SymbolKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$SymbolKind"
import {$Symbol, $Symbol$Type} from "packages/icyllis/arc3d/compiler/tree/$Symbol"
import {$Variable, $Variable$Type} from "packages/icyllis/arc3d/compiler/tree/$Variable"

export class $AnonymousField extends $Symbol {
 "mPosition": integer

constructor(position: integer, container: $Variable$Type, fieldIndex: integer)

public "toString"(): string
public "getType"(): $Type
public "getContainer"(): $Variable
public "getFieldIndex"(): integer
public "getKind"(): $Node$SymbolKind
get "type"(): $Type
get "container"(): $Variable
get "fieldIndex"(): integer
get "kind"(): $Node$SymbolKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnonymousField$Type = ($AnonymousField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnonymousField_ = $AnonymousField$Type;
}}
declare module "packages/icyllis/arc3d/engine/$IGpuSurface" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$ISurface, $ISurface$Type} from "packages/icyllis/arc3d/engine/$ISurface"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"

export interface $IGpuSurface extends $ISurface {

 "getSurfaceFlags"(): integer
 "asTexture"(): $GpuTexture
 "asRenderTarget"(): $GpuRenderTarget
 "ref"(): void
 "getBackendFormat"(): $BackendFormat
 "getWidth"(): integer
 "getHeight"(): integer
 "getSampleCount"(): integer
 "unref"(): void
}

export namespace $IGpuSurface {
function getApproxSize(size: integer): integer
function create<T>(that: T): T
function create<T>(sp: T, that: T): T
function move<T>(sp: T): T
function move<T>(sp: T, that: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGpuSurface$Type = ($IGpuSurface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGpuSurface_ = $IGpuSurface$Type;
}}
declare module "packages/icyllis/arc3d/core/$RectanglePacker" {
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"

export class $RectanglePacker {
static readonly "ALGORITHM_SKYLINE": integer
static readonly "ALGORITHM_HORIZON": integer
static readonly "ALGORITHM_HORIZON_OLD": integer
static readonly "ALGORITHM_BINARY_TREE": integer
static readonly "ALGORITHM_POWER2_LINE": integer
static readonly "ALGORITHM_SKYLINE_NEW": integer


public "clear"(): void
public static "make"(width: integer, height: integer, algorithm: integer): $RectanglePacker
public static "make"(width: integer, height: integer): $RectanglePacker
public "free"(): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getCoverage"(): double
public "addRect"(arg0: $Rect2i$Type): boolean
get "width"(): integer
get "height"(): integer
get "coverage"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RectanglePacker$Type = ($RectanglePacker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RectanglePacker_ = $RectanglePacker$Type;
}}
declare module "packages/icyllis/arc3d/engine/$Swizzle" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Swizzle {
static readonly "RGBA": short
static readonly "BGRA": short
static readonly "RGB1": short
static readonly "BGR1": short
static readonly "AAAA": short

constructor()

public static "toString"(swizzle: short): string
public static "apply"(swizzle: short, v: (float)[]): (float)[]
public static "concat"(a: short, b: short): short
public static "make"(r: character, g: character, b: character, a: character): short
public static "make"(s: string): short
public static "charToIndex"(c: character): integer
public static "indexToChar"(idx: integer): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Swizzle$Type = ($Swizzle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Swizzle_ = $Swizzle$Type;
}}
declare module "packages/icyllis/arc3d/core/$RefCnt" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"

export class $RefCnt implements $RefCounted {

constructor()

public "ref"(): void
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "create"<T extends $RefCounted>(that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
public "unique"(): boolean
public "unref"(): void
public "getRefCntAcquire"(): integer
public "getRefCnt"(): integer
public "getRefCntVolatile"(): integer
get "refCntAcquire"(): integer
get "refCnt"(): integer
get "refCntVolatile"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RefCnt$Type = ($RefCnt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RefCnt_ = $RefCnt$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ContextOptions" {
import {$DriverBugWorkarounds, $DriverBugWorkarounds$Type} from "packages/icyllis/arc3d/engine/$DriverBugWorkarounds"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"

export class $ContextOptions {
 "mSkipGLErrorChecks": boolean
 "mMaxTextureSizeOverride": integer
 "mSharpenMipmappedTextures": boolean
 "mSupportBilerpFromGlyphAtlas": boolean
 "mReducedShaderVariations": boolean
 "mGlyphCacheTextureMaximumBytes": long
 "mAllowMultipleGlyphCacheTextures": boolean
 "mMinDistanceFieldFontSize": float
 "mGlyphsAsPathsFontSize": float
 "mErrorWriter": $PrintWriter
 "mInternalMultisampleCount": integer
 "mMaxRuntimeProgramCacheSize": integer
 "mMaxVkSecondaryCommandBufferCacheSize": integer
 "mDriverBugWorkarounds": $DriverBugWorkarounds

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContextOptions$Type = ($ContextOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContextOptions_ = $ContextOptions$Type;
}}
declare module "packages/icyllis/arc3d/engine/$RenderTargetProxy" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $RenderTargetProxy extends $SurfaceProxy {


public "clear"(): void
public "isBackingWrapped"(): boolean
public "getBackingUniqueID"(): any
public "getGpuSurface"(): $IGpuSurface
public "instantiate"(resourceProvider: $ResourceProvider$Type): boolean
public "getBackingWidth"(): integer
public "getSampleCount"(): integer
public "getGpuRenderTarget"(): $GpuRenderTarget
public "doLazyInstantiation"(resourceProvider: $ResourceProvider$Type): boolean
public "shouldSkipAllocator"(): boolean
public "isInstantiated"(): boolean
public "getBackingHeight"(): integer
public "isLazy"(): boolean
public "ref"(): void
public static "getApproxSize"(size: integer): integer
public "unref"(): void
get "backingWrapped"(): boolean
get "backingUniqueID"(): any
get "gpuSurface"(): $IGpuSurface
get "backingWidth"(): integer
get "sampleCount"(): integer
get "gpuRenderTarget"(): $GpuRenderTarget
get "instantiated"(): boolean
get "backingHeight"(): integer
get "lazy"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTargetProxy$Type = ($RenderTargetProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTargetProxy_ = $RenderTargetProxy$Type;
}}
declare module "packages/icyllis/arc3d/compiler/analysis/$Analysis" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"

export class $Analysis {

constructor()

public static "hasSideEffects"(expr: $Expression$Type): boolean
public static "isCompileTimeConstant"(expr: $Expression$Type): boolean
public static "isSameExpressionTree"(left: $Expression$Type, right: $Expression$Type): boolean
public static "isTrivialExpression"(expr: $Expression$Type): boolean
public static "updateVariableRefKind"(expr: $Expression$Type, refKind: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Analysis$Type = ($Analysis);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Analysis_ = $Analysis$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Literal" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $Literal extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public "getValue"(): double
public static "make"(position: integer, value: double, type: $Type$Type): $Literal
public "accept"(visitor: $TreeVisitor$Type): boolean
public "isLiteral"(): boolean
public "getBooleanValue"(): boolean
public "getIntegerValue"(): long
public "getConstantValue"(i: integer): $OptionalDouble
public static "makeFloat"(position: integer, value: float, type: $Type$Type): $Literal
public static "makeFloat"(context: $Context$Type, position: integer, value: float): $Literal
public "getFloatValue"(): float
public "getKind"(): $Node$ExpressionKind
public static "makeInteger"(context: $Context$Type, position: integer, value: long): $Literal
public static "makeInteger"(position: integer, value: long, type: $Type$Type): $Literal
public static "makeBoolean"(position: integer, value: boolean, type: $Type$Type): $Literal
public static "makeBoolean"(context: $Context$Type, position: integer, value: boolean): $Literal
get "value"(): double
get "literal"(): boolean
get "booleanValue"(): boolean
get "integerValue"(): long
get "floatValue"(): float
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Literal$Type = ($Literal);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Literal_ = $Literal$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$FunctionDefinition" {
import {$BlockStatement, $BlockStatement$Type} from "packages/icyllis/arc3d/compiler/tree/$BlockStatement"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$FunctionDecl, $FunctionDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDecl"
import {$Node$ElementKind, $Node$ElementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ElementKind"
import {$TopLevelElement, $TopLevelElement$Type} from "packages/icyllis/arc3d/compiler/tree/$TopLevelElement"

export class $FunctionDefinition extends $TopLevelElement {
 "mPosition": integer


public "toString"(): string
public static "convert"(context: $Context$Type, pos: integer, functionDecl: $FunctionDecl$Type, builtin: boolean, body: $Statement$Type): $FunctionDefinition
public static "make"(pos: integer, functionDecl: $FunctionDecl$Type, builtin: boolean, body: $BlockStatement$Type): $FunctionDefinition
public "accept"(visitor: $TreeVisitor$Type): boolean
public "isBuiltin"(): boolean
public "getBody"(): $BlockStatement
public "getFunctionDecl"(): $FunctionDecl
public "setBody"(body: $BlockStatement$Type): void
public "getKind"(): $Node$ElementKind
get "builtin"(): boolean
get "body"(): $BlockStatement
get "functionDecl"(): $FunctionDecl
set "body"(value: $BlockStatement$Type)
get "kind"(): $Node$ElementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionDefinition$Type = ($FunctionDefinition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionDefinition_ = $FunctionDefinition$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$DiscardStatement" {
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $DiscardStatement extends $Statement {
 "mPosition": integer


public "toString"(): string
public static "convert"(context: $Context$Type, pos: integer): $Statement
public static "make"(pos: integer): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$StatementKind
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DiscardStatement$Type = ($DiscardStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DiscardStatement_ = $DiscardStatement$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$FunctionReference" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$FunctionDecl, $FunctionDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDecl"

export class $FunctionReference extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "make"(context: $Context$Type, position: integer, overloadChain: $FunctionDecl$Type): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$ExpressionKind
public "getOverloadChain"(): $FunctionDecl
get "kind"(): $Node$ExpressionKind
get "overloadChain"(): $FunctionDecl
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionReference$Type = ($FunctionReference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionReference_ = $FunctionReference$Type;
}}
declare module "packages/icyllis/arc3d/core/$Surface" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$Device, $Device$Type} from "packages/icyllis/arc3d/core/$Device"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"
import {$Canvas, $Canvas$Type} from "packages/icyllis/arc3d/core/$Canvas"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"

export class $Surface extends $RefCnt {

constructor(device: $Device$Type)

public static "makeFromBackendTexture"(context: $RecordingContext$Type, backendTexture: $BackendTexture$Type, origin: integer, sampleCount: integer, colorType: integer, releaseCallback: $Runnable$Type): $Surface
public "getRecordingContext"(): $RecordingContext
public "getCanvas"(): $Canvas
public static "makeRenderTarget"(context: $RecordingContext$Type, imageInfo: $ImageInfo$Type, origin: integer, sampleCount: integer, mipmapped: boolean, budgeted: boolean): $Surface
public "getWidth"(): integer
public "getHeight"(): integer
public static "wrapBackendRenderTarget"(rContext: $RecordingContext$Type, backendRenderTarget: $BackendRenderTarget$Type, origin: integer, colorType: integer, colorSpace: $ColorSpace$Type): $Surface
public "getImageInfo"(): $ImageInfo
get "recordingContext"(): $RecordingContext
get "canvas"(): $Canvas
get "width"(): integer
get "height"(): integer
get "imageInfo"(): $ImageInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Surface$Type = ($Surface);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Surface_ = $Surface$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace$Rgb$TransferParameters" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ColorSpace$Rgb$TransferParameters {
readonly "a": double
readonly "b": double
readonly "c": double
readonly "d": double
readonly "e": double
readonly "f": double
readonly "g": double

constructor(a: double, b: double, c: double, d: double, g: double)
constructor(a: double, b: double, c: double, d: double, e: double, f: double, g: double)

public "equals"(o: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Rgb$TransferParameters$Type = ($ColorSpace$Rgb$TransferParameters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace$Rgb$TransferParameters_ = $ColorSpace$Rgb$TransferParameters$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$Lexer" {
import {$Lexer$PackedEntry, $Lexer$PackedEntry$Type} from "packages/icyllis/arc3d/compiler/parser/$Lexer$PackedEntry"

export class $Lexer {
static readonly "TK_END_OF_FILE": integer
static readonly "TK_INTLITERAL": integer
static readonly "TK_FLOATLITERAL": integer
static readonly "TK_TRUE": integer
static readonly "TK_FALSE": integer
static readonly "TK_BREAK": integer
static readonly "TK_CONTINUE": integer
static readonly "TK_DO": integer
static readonly "TK_FOR": integer
static readonly "TK_WHILE": integer
static readonly "TK_IF": integer
static readonly "TK_ELSE": integer
static readonly "TK_SWITCH": integer
static readonly "TK_CASE": integer
static readonly "TK_DEFAULT": integer
static readonly "TK_DISCARD": integer
static readonly "TK_RETURN": integer
static readonly "TK_IN": integer
static readonly "TK_OUT": integer
static readonly "TK_INOUT": integer
static readonly "TK_CONST": integer
static readonly "TK_UNIFORM": integer
static readonly "TK_BUFFER": integer
static readonly "TK_WORKGROUP": integer
static readonly "TK_SMOOTH": integer
static readonly "TK_FLAT": integer
static readonly "TK_NOPERSPECTIVE": integer
static readonly "TK_COHERENT": integer
static readonly "TK_VOLATILE": integer
static readonly "TK_RESTRICT": integer
static readonly "TK_READONLY": integer
static readonly "TK_WRITEONLY": integer
static readonly "TK_SUBROUTINE": integer
static readonly "TK_LAYOUT": integer
static readonly "TK_STRUCT": integer
static readonly "TK_INLINE": integer
static readonly "TK_NOINLINE": integer
static readonly "TK_PURE": integer
static readonly "TK_RESERVED": integer
static readonly "TK_IDENTIFIER": integer
static readonly "TK_LPAREN": integer
static readonly "TK_RPAREN": integer
static readonly "TK_LBRACE": integer
static readonly "TK_RBRACE": integer
static readonly "TK_LBRACKET": integer
static readonly "TK_RBRACKET": integer
static readonly "TK_DOT": integer
static readonly "TK_COMMA": integer
static readonly "TK_EQ": integer
static readonly "TK_LT": integer
static readonly "TK_GT": integer
static readonly "TK_BANG": integer
static readonly "TK_TILDE": integer
static readonly "TK_QUES": integer
static readonly "TK_COLON": integer
static readonly "TK_EQEQ": integer
static readonly "TK_LTEQ": integer
static readonly "TK_GTEQ": integer
static readonly "TK_BANGEQ": integer
static readonly "TK_PLUSPLUS": integer
static readonly "TK_MINUSMINUS": integer
static readonly "TK_PLUS": integer
static readonly "TK_MINUS": integer
static readonly "TK_STAR": integer
static readonly "TK_SLASH": integer
static readonly "TK_PERCENT": integer
static readonly "TK_LTLT": integer
static readonly "TK_GTGT": integer
static readonly "TK_AMPAMP": integer
static readonly "TK_PIPEPIPE": integer
static readonly "TK_CARETCARET": integer
static readonly "TK_AMP": integer
static readonly "TK_PIPE": integer
static readonly "TK_CARET": integer
static readonly "TK_PLUSEQ": integer
static readonly "TK_MINUSEQ": integer
static readonly "TK_STAREQ": integer
static readonly "TK_SLASHEQ": integer
static readonly "TK_PERCENTEQ": integer
static readonly "TK_LTLTEQ": integer
static readonly "TK_GTGTEQ": integer
static readonly "TK_AMPEQ": integer
static readonly "TK_PIPEEQ": integer
static readonly "TK_CARETEQ": integer
static readonly "TK_SEMICOLON": integer
static readonly "TK_WHITESPACE": integer
static readonly "TK_LINE_COMMENT": integer
static readonly "TK_BLOCK_COMMENT": integer
static readonly "TK_INVALID": integer
static readonly "TK_NONE": integer
static readonly "MAPPINGS": (byte)[]
static readonly "FULL": ((short)[])[]
static readonly "PACKED": ($Lexer$PackedEntry)[]
static readonly "INDICES": (short)[]
static readonly "ACCEPTS": (byte)[]

constructor(source: (character)[])

public "next"(): long
public "offset"(offset: integer): void
public "offset"(): integer
public static "getTransition"(transition: integer, state: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Lexer$Type = ($Lexer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Lexer_ = $Lexer$Type;
}}
declare module "packages/icyllis/arc3d/engine/$DataUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DataUtils {


public static "numBlocks"(compression: integer, width: integer, height: integer): long
public static "compressionTypeIsOpaque"(compression: integer): boolean
public static "num4x4Blocks"(size: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataUtils$Type = ($DataUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataUtils_ = $DataUtils$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$UniformHandler$UniformInfo" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"
import {$Processor, $Processor$Type} from "packages/icyllis/arc3d/engine/$Processor"

export class $UniformHandler$UniformInfo {
 "mVariable": $ShaderVar
 "mVisibility": integer
 "mOwner": $Processor
 "mRawName": string
 "mOffset": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UniformHandler$UniformInfo$Type = ($UniformHandler$UniformInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UniformHandler$UniformInfo_ = $UniformHandler$UniformInfo$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$RegexNode" {
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$NFA, $NFA$Type} from "packages/icyllis/arc3d/compiler/parser/$NFA"

export interface $RegexNode {

 "transition"(arg0: $NFA$Type, arg1: $IntList$Type): $IntList

(arg0: $NFA$Type, arg1: $IntList$Type): $IntList
}

export namespace $RegexNode {
function Range(start: $RegexNode$Type, end: $RegexNode$Type): $RegexNode
function Range(start: character, end: character): $RegexNode
function Ques(x: $RegexNode$Type): $RegexNode
function Plus(x: $RegexNode$Type): $RegexNode
function Char(c: character): $RegexNode
function Union(x: $RegexNode$Type, y: $RegexNode$Type): $RegexNode
function Concat(x: $RegexNode$Type, y: $RegexNode$Type): $RegexNode
function Star(x: $RegexNode$Type): $RegexNode
function Dot(): $RegexNode
function CharClass(...clazz: ($RegexNode$Type)[]): $RegexNode
function CharClass(clazz: ($RegexNode$Type)[], exclusive: boolean): $RegexNode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegexNode$Type = ($RegexNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegexNode_ = $RegexNode$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$TranslationUnit" {
import {$SymbolUsage, $SymbolUsage$Type} from "packages/icyllis/arc3d/compiler/analysis/$SymbolUsage"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$CompileOptions, $CompileOptions$Type} from "packages/icyllis/arc3d/compiler/$CompileOptions"
import {$TopLevelElement, $TopLevelElement$Type} from "packages/icyllis/arc3d/compiler/tree/$TopLevelElement"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$BuiltinTypes, $BuiltinTypes$Type} from "packages/icyllis/arc3d/compiler/$BuiltinTypes"
import {$SymbolTable, $SymbolTable$Type} from "packages/icyllis/arc3d/compiler/$SymbolTable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShaderKind, $ShaderKind$Type} from "packages/icyllis/arc3d/compiler/$ShaderKind"
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $TranslationUnit extends $Node implements $Iterable<($TopLevelElement)> {
 "mPosition": integer

constructor(position: integer, source: (character)[], kind: $ShaderKind$Type, options: $CompileOptions$Type, isBuiltin: boolean, isModule: boolean, types: $BuiltinTypes$Type, symbolTable: $SymbolTable$Type, uniqueElements: $ArrayList$Type<($TopLevelElement$Type)>)

public "toString"(): string
public "iterator"(): $Iterator<($TopLevelElement)>
public "accept"(visitor: $TreeVisitor$Type): boolean
public "isBuiltin"(): boolean
public "getSource"(): (character)[]
public "getOptions"(): $CompileOptions
public "getSymbolTable"(): $SymbolTable
public "getUsage"(): $SymbolUsage
public "getSharedElements"(): $ArrayList<($TopLevelElement)>
public "getUniqueElements"(): $ArrayList<($TopLevelElement)>
public "getTypes"(): $BuiltinTypes
public "isModule"(): boolean
public "getKind"(): $ShaderKind
public "spliterator"(): $Spliterator<($TopLevelElement)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$TopLevelElement>;
get "builtin"(): boolean
get "source"(): (character)[]
get "options"(): $CompileOptions
get "symbolTable"(): $SymbolTable
get "usage"(): $SymbolUsage
get "sharedElements"(): $ArrayList<($TopLevelElement)>
get "uniqueElements"(): $ArrayList<($TopLevelElement)>
get "types"(): $BuiltinTypes
get "module"(): boolean
get "kind"(): $ShaderKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TranslationUnit$Type = ($TranslationUnit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TranslationUnit_ = $TranslationUnit$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorCompound" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorCompound extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, pos: integer, type: $Type$Type, args: $List$Type<($Expression$Type)>): $Expression
public static "make"(context: $Context$Type, position: integer, type: $Type$Type, arg3: ($Expression$Type)[]): $Expression
public static "makeFromConstants"(context: $Context$Type, pos: integer, type: $Type$Type, values: (double)[]): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorCompound$Type = ($ConstructorCompound);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorCompound_ = $ConstructorCompound$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ContinueStatement" {
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"

export class $ContinueStatement extends $Statement {
 "mPosition": integer


public "toString"(): string
public static "make"(pos: integer): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getKind"(): $Node$StatementKind
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContinueStatement$Type = ($ContinueStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContinueStatement_ = $ContinueStatement$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$ModuleLoader" {
import {$BuiltinTypes, $BuiltinTypes$Type} from "packages/icyllis/arc3d/compiler/$BuiltinTypes"
import {$ModuleUnit, $ModuleUnit$Type} from "packages/icyllis/arc3d/compiler/$ModuleUnit"
import {$ShaderCompiler, $ShaderCompiler$Type} from "packages/icyllis/arc3d/compiler/$ShaderCompiler"

export class $ModuleLoader {


public static "getInstance"(): $ModuleLoader
public "getBuiltinTypes"(): $BuiltinTypes
public "loadModuleSource"(name: string): string
public "getRootModule"(): $ModuleUnit
public "loadCommonModule"(compiler: $ShaderCompiler$Type): $ModuleUnit
get "instance"(): $ModuleLoader
get "builtinTypes"(): $BuiltinTypes
get "rootModule"(): $ModuleUnit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModuleLoader$Type = ($ModuleLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModuleLoader_ = $ModuleLoader$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$DrawOp" {
import {$Op, $Op$Type} from "packages/icyllis/arc3d/engine/ops/$Op"

export class $DrawOp extends $Op {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor()

public "usesStencil"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrawOp$Type = ($DrawOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrawOp_ = $DrawOp$Type;
}}
declare module "packages/icyllis/arc3d/engine/$RenderTaskManager" {
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$RenderTask, $RenderTask$Type} from "packages/icyllis/arc3d/engine/$RenderTask"
import {$OpsTask, $OpsTask$Type} from "packages/icyllis/arc3d/engine/ops/$OpsTask"
import {$FlushInfo, $FlushInfo$Type} from "packages/icyllis/arc3d/engine/$FlushInfo"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"

export class $RenderTaskManager {

constructor(context: $RecordingContext$Type)

public "flush"(info: $FlushInfo$Type): boolean
public "closeTasks"(): void
public "clearTasks"(): void
public "getFlushState"(): $OpFlushState
public "appendTask"(task: $RenderTask$Type): $RenderTask
public "newOpsTask"(writeView: $SurfaceView$Type): $OpsTask
public "getLastRenderTask"(proxy: $SurfaceProxy$Type): $RenderTask
public "setLastRenderTask"(surfaceProxy: $SurfaceProxy$Type, task: $RenderTask$Type): void
public "prependTask"(task: $RenderTask$Type): $RenderTask
get "flushState"(): $OpFlushState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTaskManager$Type = ($RenderTaskManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTaskManager_ = $RenderTaskManager$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ExpressionStatement" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$StatementKind, $Node$StatementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$StatementKind"
import {$Statement, $Statement$Type} from "packages/icyllis/arc3d/compiler/tree/$Statement"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $ExpressionStatement extends $Statement {
 "mPosition": integer

constructor(expression: $Expression$Type)

public "toString"(): string
public static "convert"(context: $Context$Type, expr: $Expression$Type): $Statement
public static "make"(expr: $Expression$Type): $Statement
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getExpression"(): $Expression
public "setExpression"(expression: $Expression$Type): void
public "getKind"(): $Node$StatementKind
get "expression"(): $Expression
set "expression"(value: $Expression$Type)
get "kind"(): $Node$StatementKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpressionStatement$Type = ($ExpressionStatement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpressionStatement_ = $ExpressionStatement$Type;
}}
declare module "packages/icyllis/arc3d/core/$DisplayListRecorder" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$SurfaceCharacterization, $SurfaceCharacterization$Type} from "packages/icyllis/arc3d/core/$SurfaceCharacterization"

export class $DisplayListRecorder implements $AutoCloseable {

constructor(c: $SurfaceCharacterization$Type)

public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayListRecorder$Type = ($DisplayListRecorder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayListRecorder_ = $DisplayListRecorder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$RenderTask" {
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$RenderTaskManager, $RenderTaskManager$Type} from "packages/icyllis/arc3d/engine/$RenderTaskManager"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$SurfaceAllocator, $SurfaceAllocator$Type} from "packages/icyllis/arc3d/engine/$SurfaceAllocator"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"

export class $RenderTask extends $RefCnt {
static readonly "RESOLVE_FLAG_MSAA": integer
static readonly "RESOLVE_FLAG_MIPMAPS": integer


public "toString"(): string
public "execute"(arg0: $OpFlushState$Type): boolean
public "prepare"(flushState: $OpFlushState$Type): void
public "getTarget"(): $SurfaceProxy
public "getTarget"(index: integer): $SurfaceProxy
public "detach"(taskManager: $RenderTaskManager$Type): void
public "isClosed"(): boolean
public "getUniqueID"(): integer
public "gatherSurfaceIntervals"(alloc: $SurfaceAllocator$Type): void
public "addDependency"(dependency: $TextureProxy$Type, samplerState: integer): void
public "isInstantiated"(): boolean
public "makeClosed"(context: $RecordingContext$Type): void
public "makeSkippable"(): void
public "isSkippable"(): boolean
public "numTargets"(): integer
public "prePrepare"(context: $RecordingContext$Type): void
public "dependsOn"(dependency: $RenderTask$Type): boolean
get "target"(): $SurfaceProxy
get "closed"(): boolean
get "uniqueID"(): integer
get "instantiated"(): boolean
get "skippable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTask$Type = ($RenderTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTask_ = $RenderTask$Type;
}}
declare module "packages/icyllis/arc3d/compiler/analysis/$SymbolUsage" {
import {$VariableDecl, $VariableDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$VariableDecl"
import {$InterfaceBlock, $InterfaceBlock$Type} from "packages/icyllis/arc3d/compiler/tree/$InterfaceBlock"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$SymbolUsage$Count, $SymbolUsage$Count$Type} from "packages/icyllis/arc3d/compiler/analysis/$SymbolUsage$Count"
import {$FunctionCall, $FunctionCall$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionCall"
import {$FunctionDefinition, $FunctionDefinition$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDefinition"
import {$SymbolUsage$VariableCounts, $SymbolUsage$VariableCounts$Type} from "packages/icyllis/arc3d/compiler/analysis/$SymbolUsage$VariableCounts"
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"
import {$FunctionDecl, $FunctionDecl$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDecl"
import {$VariableReference, $VariableReference$Type} from "packages/icyllis/arc3d/compiler/tree/$VariableReference"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$IdentityHashMap, $IdentityHashMap$Type} from "packages/java/util/$IdentityHashMap"
import {$Variable, $Variable$Type} from "packages/icyllis/arc3d/compiler/tree/$Variable"

export class $SymbolUsage extends $TreeVisitor {
readonly "mStructCounts": $IdentityHashMap<($Type), ($SymbolUsage$Count)>
readonly "mFunctionCounts": $IdentityHashMap<($FunctionDecl), ($SymbolUsage$Count)>
readonly "mVariableCounts": $IdentityHashMap<($Variable), ($SymbolUsage$VariableCounts)>

constructor()

public "findStructCount"(typeSymbol: $Type$Type): $SymbolUsage$Count
public "findVariableCounts"(varSymbol: $Variable$Type): $SymbolUsage$VariableCounts
public "getStructCount"(typeSymbol: $Type$Type): integer
public "findFunctionCount"(functionSymbol: $FunctionDecl$Type): $SymbolUsage$Count
public "computeStructCount"(typeSymbol: $Type$Type): $SymbolUsage$Count
public "add"(node: $Node$Type): void
public "remove"(node: $Node$Type): void
public "toString"(): string
public "computeFunctionCount"(functionSymbol: $FunctionDecl$Type): $SymbolUsage$Count
public "computeVariableCounts"(varSymbol: $Variable$Type): $SymbolUsage$VariableCounts
public "getFunctionCount"(functionSymbol: $FunctionDecl$Type): integer
public "visitFunctionCall"(expr: $FunctionCall$Type): boolean
public "visitVariableDecl"(variableDecl: $VariableDecl$Type): boolean
public "visitVariableReference"(expr: $VariableReference$Type): boolean
public "visitInterfaceBlock"(interfaceBlock: $InterfaceBlock$Type): boolean
public "visitFunctionDefinition"(definition: $FunctionDefinition$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SymbolUsage$Type = ($SymbolUsage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SymbolUsage_ = $SymbolUsage$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorScalar2Matrix" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorScalar2Matrix extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "make"(position: integer, type: $Type$Type, arg: $Expression$Type): $Expression
public "getConstantValue"(i: integer): $OptionalDouble
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorScalar2Matrix$Type = ($ConstructorScalar2Matrix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorScalar2Matrix_ = $ConstructorScalar2Matrix$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorScalarCast" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorScalarCast extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, pos: integer, type: $Type$Type, args: $List$Type<($Expression$Type)>): $Expression
public static "make"(context: $Context$Type, position: integer, type: $Type$Type, arg: $Expression$Type): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorScalarCast$Type = ($ConstructorScalarCast);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorScalarCast_ = $ConstructorScalarCast$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GraphicsPipelineState" {
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"

export class $GraphicsPipelineState {

constructor(device: $GpuDevice$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsPipelineState$Type = ($GraphicsPipelineState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsPipelineState_ = $GraphicsPipelineState$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$ColorShader" {
import {$Shader, $Shader$Type} from "packages/icyllis/arc3d/core/$Shader"

export class $ColorShader extends $Shader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorShader$Type = ($ColorShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorShader_ = $ColorShader$Type;
}}
declare module "packages/icyllis/arc3d/core/$Image" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"

export class $Image extends $RefCnt {


public "isRasterBacked"(): boolean
public "isTextureBacked"(): boolean
public "getContext"(): $RecordingContext
public "isValid"(arg0: $RecordingContext$Type): boolean
public "getInfo"(): $ImageInfo
public "getWidth"(): integer
public "getHeight"(): integer
public "getColorType"(): integer
public "getColorSpace"(): $ColorSpace
public "getAlphaType"(): integer
public "getTextureMemorySize"(): long
get "rasterBacked"(): boolean
get "textureBacked"(): boolean
get "context"(): $RecordingContext
get "info"(): $ImageInfo
get "width"(): integer
get "height"(): integer
get "colorType"(): integer
get "colorSpace"(): $ColorSpace
get "alphaType"(): integer
get "textureMemorySize"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Image$Type = ($Image);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Image_ = $Image$Type;
}}
declare module "packages/icyllis/arc3d/engine/$DriverBugWorkarounds" {
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DriverBugWorkarounds {
static readonly "DEFAULT": integer
static readonly "DISABLED": integer
static readonly "ENABLED": integer
 "dsa_element_buffer_broken": byte

constructor()
constructor(states: $Map$Type<(string), (boolean)>)

public static "isEnabled"(state: byte): boolean
public "applyOverrides"(workarounds: $DriverBugWorkarounds$Type): void
public static "isDisabled"(state: byte): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DriverBugWorkarounds$Type = ($DriverBugWorkarounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DriverBugWorkarounds_ = $DriverBugWorkarounds$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLBackendTexture" {
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GLTextureInfo, $GLTextureInfo$Type} from "packages/icyllis/arc3d/opengl/$GLTextureInfo"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"

export class $GLBackendTexture extends $BackendTexture {

constructor(width: integer, height: integer, info: $GLTextureInfo$Type)

public "toString"(): string
public "isProtected"(): boolean
public "getBackendFormat"(): $BackendFormat
public "glTextureParametersModified"(): void
public "isMipmapped"(): boolean
public "getGLTextureInfo"(info: $GLTextureInfo$Type): boolean
public "isExternal"(): boolean
public "getBackend"(): integer
public "isSameTexture"(texture: $BackendTexture$Type): boolean
get "protected"(): boolean
get "backendFormat"(): $BackendFormat
get "mipmapped"(): boolean
get "external"(): boolean
get "backend"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLBackendTexture$Type = ($GLBackendTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLBackendTexture_ = $GLBackendTexture$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorStruct" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"

export class $ConstructorStruct extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorStruct$Type = ($ConstructorStruct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorStruct_ = $ConstructorStruct$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$SwizzleLValue" {
import {$Writer, $Writer$Type} from "packages/icyllis/arc3d/compiler/spirv/$Writer"
import {$LValue, $LValue$Type} from "packages/icyllis/arc3d/compiler/spirv/$LValue"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$SPIRVCodeGenerator, $SPIRVCodeGenerator$Type} from "packages/icyllis/arc3d/compiler/spirv/$SPIRVCodeGenerator"

export class $SwizzleLValue implements $LValue {


public "load"(gen: $SPIRVCodeGenerator$Type, writer: $Writer$Type): integer
public "store"(gen: $SPIRVCodeGenerator$Type, rvalue: integer, writer: $Writer$Type): void
public "applySwizzle"(components: (byte)[], newType: $Type$Type): boolean
public "getPointer"(): integer
get "pointer"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SwizzleLValue$Type = ($SwizzleLValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SwizzleLValue_ = $SwizzleLValue$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$FunctionDecl" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$FunctionDefinition, $FunctionDefinition$Type} from "packages/icyllis/arc3d/compiler/tree/$FunctionDefinition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Node$SymbolKind, $Node$SymbolKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$SymbolKind"
import {$Symbol, $Symbol$Type} from "packages/icyllis/arc3d/compiler/tree/$Symbol"
import {$Modifiers, $Modifiers$Type} from "packages/icyllis/arc3d/compiler/tree/$Modifiers"
import {$Variable, $Variable$Type} from "packages/icyllis/arc3d/compiler/tree/$Variable"

export class $FunctionDecl extends $Symbol {
 "mPosition": integer

constructor(position: integer, modifiers: $Modifiers$Type, name: string, parameters: $List$Type<($Variable$Type)>, returnType: $Type$Type, builtin: boolean, entryPoint: boolean, intrinsicKind: integer)

public "toString"(): string
public "getModifiers"(): $Modifiers
public static "convert"(context: $Context$Type, pos: integer, modifiers: $Modifiers$Type, name: string, parameters: $List$Type<($Variable$Type)>, returnType: $Type$Type): $FunctionDecl
public "getReturnType"(): $Type
public "getType"(): $Type
public "getParameters"(): $List<($Variable)>
public "getDefinition"(): $FunctionDefinition
public "isBuiltin"(): boolean
public "getNextOverload"(): $FunctionDecl
public "setDefinition"(definition: $FunctionDefinition$Type): void
public "isIntrinsic"(): boolean
public "getMangledName"(): string
public "isEntryPoint"(): boolean
public "setNextOverload"(overload: $FunctionDecl$Type): void
public "resolveParameterTypes"(arg0: $List$Type<($Expression$Type)>, outParameterTypes: $List$Type<($Type$Type)>): $Type
public "getKind"(): $Node$SymbolKind
get "modifiers"(): $Modifiers
get "returnType"(): $Type
get "type"(): $Type
get "parameters"(): $List<($Variable)>
get "definition"(): $FunctionDefinition
get "builtin"(): boolean
get "nextOverload"(): $FunctionDecl
set "definition"(value: $FunctionDefinition$Type)
get "intrinsic"(): boolean
get "mangledName"(): string
get "entryPoint"(): boolean
set "nextOverload"(value: $FunctionDecl$Type)
get "kind"(): $Node$SymbolKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionDecl$Type = ($FunctionDecl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionDecl_ = $FunctionDecl$Type;
}}
declare module "packages/icyllis/arc3d/engine/geom/$SDFRoundRectGeoProc" {
import {$GeometryProcessor$AttributeSet, $GeometryProcessor$AttributeSet$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$AttributeSet"
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$GeometryProcessor, $GeometryProcessor$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor"
import {$GeometryProcessor$ProgramImpl, $GeometryProcessor$ProgramImpl$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$ProgramImpl"
import {$ShaderCaps, $ShaderCaps$Type} from "packages/icyllis/arc3d/engine/$ShaderCaps"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"

export class $SDFRoundRectGeoProc extends $GeometryProcessor {
static readonly "POSITION": $GeometryProcessor$Attribute
static readonly "COLOR": $GeometryProcessor$Attribute
static readonly "LOCAL_RECT": $GeometryProcessor$Attribute
static readonly "RADII": $GeometryProcessor$Attribute
static readonly "MODEL_VIEW": $GeometryProcessor$Attribute
static readonly "VERTEX_ATTRIBS": $GeometryProcessor$AttributeSet
static readonly "INSTANCE_ATTRIBS": $GeometryProcessor$AttributeSet
static readonly "Null_ClassID": integer
static readonly "CircularRRect_Geom_ClassID": integer
static readonly "Circle_Geom_ClassID": integer
static readonly "RoundRect_GeoProc_ClassID": integer
static readonly "DefaultGeoProc_ClassID": integer
static readonly "SDFRect_GeoProc_ClassID": integer
static readonly "Hard_XferProc_ClassID": integer

constructor(stroke: boolean)

public "name"(): string
public "primitiveType"(): byte
public "makeProgramImpl"(caps: $ShaderCaps$Type): $GeometryProcessor$ProgramImpl
public "appendToKey"(b: $KeyBuilder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SDFRoundRectGeoProc$Type = ($SDFRoundRectGeoProc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SDFRoundRectGeoProc_ = $SDFRoundRectGeoProc$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$Token" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Token {


public static "length"(token: long): integer
public static "replace"(token: long, kind: integer): long
public static "offset"(token: long): integer
public static "make"(kind: integer, offset: integer, length: integer): long
public static "kind"(token: long): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$Type = ($Token);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token_ = $Token$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLTexture" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GLTextureInfo, $GLTextureInfo$Type} from "packages/icyllis/arc3d/opengl/$GLTextureInfo"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$GLTextureParameters, $GLTextureParameters$Type} from "packages/icyllis/arc3d/opengl/$GLTextureParameters"

export class $GLTexture extends $GpuTexture {

constructor(device: $GLDevice$Type, width: integer, height: integer, info: $GLTextureInfo$Type, params: $GLTextureParameters$Type, format: $BackendFormat$Type, ioType: integer, cacheable: boolean, ownership: boolean)

public "toString"(): string
public "getParameters"(): $GLTextureParameters
public "getHandle"(): integer
public "getBackendFormat"(): $BackendFormat
public "getMemorySize"(): long
public "getGLFormat"(): integer
public "getMaxMipmapLevel"(): integer
public "getBackendTexture"(): $BackendTexture
public "isExternal"(): boolean
public static "getApproxSize"(size: integer): integer
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "parameters"(): $GLTextureParameters
get "handle"(): integer
get "backendFormat"(): $BackendFormat
get "memorySize"(): long
get "gLFormat"(): integer
get "maxMipmapLevel"(): integer
get "backendTexture"(): $BackendTexture
get "external"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLTexture$Type = ($GLTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLTexture_ = $GLTexture$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$InstructionBuilder" {
import {$Instruction, $Instruction$Type} from "packages/icyllis/arc3d/compiler/spirv/$Instruction"

export class $InstructionBuilder extends $Instruction {


public "equals"(o: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InstructionBuilder$Type = ($InstructionBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InstructionBuilder_ = $InstructionBuilder$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PriorityQueue" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$AbstractQueue, $AbstractQueue$Type} from "packages/java/util/$AbstractQueue"
import {$PriorityQueue$Access, $PriorityQueue$Access$Type} from "packages/icyllis/arc3d/engine/$PriorityQueue$Access"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $PriorityQueue<E> extends $AbstractQueue<(E)> {

constructor(capacity: integer, comparator: $Comparator$Type<(any)>, access: $PriorityQueue$Access$Type<(any)>)
constructor(comparator: $Comparator$Type<(any)>, access: $PriorityQueue$Access$Type<(any)>)
constructor(capacity: integer, access: $PriorityQueue$Access$Type<(any)>)
constructor()
constructor(priority: integer)
constructor(access: $PriorityQueue$Access$Type<(any)>)

public "add"(e: E): boolean
public "remove"(o: any): boolean
public "clear"(): void
public "size"(): integer
public "toArray"(): (any)[]
public "toArray"<T>(a: (T)[]): (T)[]
public "iterator"(): $Iterator<(E)>
public "trim"(): void
public "contains"(o: any): boolean
public "access"(): $PriorityQueue$Access<(any)>
public "poll"(): E
public "peek"(): E
public "elementAt"(i: integer): E
public "sort"(): void
public "comparator"(): $Comparator<(any)>
public "offer"(e: E): boolean
public "removeAt"(i: integer): void
public "heap"(): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PriorityQueue$Type<E> = ($PriorityQueue<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PriorityQueue_<E> = $PriorityQueue$Type<(E)>;
}}
declare module "packages/icyllis/arc3d/compiler/$TargetApi" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TargetApi extends $Enum<($TargetApi)> {
static readonly "OPENGL_3_3": $TargetApi
static readonly "OPENGL_ES_3_0": $TargetApi
static readonly "OPENGL_4_3": $TargetApi
static readonly "OPENGL_ES_3_1": $TargetApi
static readonly "OPENGL_4_5": $TargetApi
static readonly "VULKAN_1_0": $TargetApi


public static "values"(): ($TargetApi)[]
public static "valueOf"(name: string): $TargetApi
public "isVulkan"(): boolean
public "isOpenGL"(): boolean
public "isOpenGLES"(): boolean
get "vulkan"(): boolean
get "openGL"(): boolean
get "openGLES"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TargetApi$Type = (("opengl_es_3_0") | ("vulkan_1_0") | ("opengl_4_3") | ("opengl_es_3_1") | ("opengl_4_5") | ("opengl_3_3")) | ($TargetApi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TargetApi_ = $TargetApi$Type;
}}
declare module "packages/icyllis/arc3d/core/image/$RasterImage" {
import {$PixelMap, $PixelMap$Type} from "packages/icyllis/arc3d/core/$PixelMap"
import {$PixelRef, $PixelRef$Type} from "packages/icyllis/arc3d/core/$PixelRef"
import {$Image, $Image$Type} from "packages/icyllis/arc3d/core/$Image"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"

export class $RasterImage extends $Image {

constructor(pixelMap: $PixelMap$Type, pixelRef: $PixelRef$Type)

public "isRasterBacked"(): boolean
public "isValid"(context: $RecordingContext$Type): boolean
get "rasterBacked"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RasterImage$Type = ($RasterImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RasterImage_ = $RasterImage$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorArrayCast" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorArrayCast extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "make"(context: $Context$Type, position: integer, type: $Type$Type, arg: $Expression$Type): $Expression
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorArrayCast$Type = ($ConstructorArrayCast);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorArrayCast_ = $ConstructorArrayCast$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanDevice" {
import {$DirectContext, $DirectContext$Type} from "packages/icyllis/arc3d/engine/$DirectContext"
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"

export class $VulkanDevice extends $GpuDevice {

constructor(context: $DirectContext$Type)

public static "colorTypeClampType"(ct: integer): integer
public static "colorTypeEncoding"(ct: integer): integer
public static "colorTypeToPublic"(ct: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanDevice$Type = ($VulkanDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanDevice_ = $VulkanDevice$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLOpsRenderPass" {
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$OpsRenderPass, $OpsRenderPass$Type} from "packages/icyllis/arc3d/engine/$OpsRenderPass"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"

export class $GLOpsRenderPass extends $OpsRenderPass {

constructor(device: $GLDevice$Type)

public "end"(): void
public "begin"(): void
public "set"(rt: $GpuRenderTarget$Type, bounds: $Rect2i$Type, origin: integer, colorOps: byte, stencilOps: byte, clearColor: (float)[]): $GLOpsRenderPass
public "clearColor"(left: integer, top: integer, right: integer, bottom: integer, red: float, green: float, blue: float, alpha: float): void
public "clearStencil"(left: integer, top: integer, right: integer, bottom: integer, insideMask: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLOpsRenderPass$Type = ($GLOpsRenderPass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLOpsRenderPass_ = $GLOpsRenderPass$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GeometryProcessor$AttributeSet" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $GeometryProcessor$AttributeSet implements $Iterable<($GeometryProcessor$Attribute)> {


public "iterator"(): $Iterator<($GeometryProcessor$Attribute)>
public static "makeImplicit"(...attrs: ($GeometryProcessor$Attribute$Type)[]): $GeometryProcessor$AttributeSet
public static "makeExplicit"(stride: integer, ...attrs: ($GeometryProcessor$Attribute$Type)[]): $GeometryProcessor$AttributeSet
public "spliterator"(): $Spliterator<($GeometryProcessor$Attribute)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$GeometryProcessor$Attribute>;
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeometryProcessor$AttributeSet$Type = ($GeometryProcessor$AttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeometryProcessor$AttributeSet_ = $GeometryProcessor$AttributeSet$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$Varying" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"

export class $Varying {

constructor(type: byte)

public "type"(): byte
public "reset"(type: byte): void
public "isInVertexShader"(): boolean
public "isInFragmentShader"(): boolean
public "vsOut"(): string
public "fsIn"(): string
public "vsOutVar"(): $ShaderVar
public "fsInVar"(): $ShaderVar
get "inVertexShader"(): boolean
get "inFragmentShader"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Varying$Type = ($Varying);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Varying_ = $Varying$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ProcessorAnalyzer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ProcessorAnalyzer {
static readonly "NON_OVERLAPPING": integer
static readonly "NON_COHERENT_BLENDING": integer
static readonly "EMPTY_ANALYSIS": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProcessorAnalyzer$Type = ($ProcessorAnalyzer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProcessorAnalyzer_ = $ProcessorAnalyzer$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$GLSLVersion" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GLSLVersion extends $Enum<($GLSLVersion)> {
static readonly "GLSL_300_ES": $GLSLVersion
static readonly "GLSL_310_ES": $GLSLVersion
static readonly "GLSL_330": $GLSLVersion
static readonly "GLSL_400": $GLSLVersion
static readonly "GLSL_420": $GLSLVersion
static readonly "GLSL_430": $GLSLVersion
static readonly "GLSL_440": $GLSLVersion
static readonly "GLSL_450": $GLSLVersion
readonly "mVersionDecl": string


public static "values"(): ($GLSLVersion)[]
public static "valueOf"(name: string): $GLSLVersion
public "isAtLeast"(other: $GLSLVersion$Type): boolean
public "isEsProfile"(): boolean
public "isCoreProfile"(): boolean
get "esProfile"(): boolean
get "coreProfile"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLSLVersion$Type = (("glsl_310_es") | ("glsl_300_es") | ("glsl_420") | ("glsl_430") | ("glsl_400") | ("glsl_330") | ("glsl_440") | ("glsl_450")) | ($GLSLVersion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLSLVersion_ = $GLSLVersion$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ConservativeClip" {
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"

export class $ConservativeClip {

constructor()

public "isEmpty"(): boolean
public "replace"(globalRect: $Rect2i$Type, globalToDevice: $Matrix4$Type, deviceBounds: $Rect2i$Type): void
public "getBounds"(): $Rect2i
public "set"(clip: $ConservativeClip$Type): void
public "setEmpty"(): void
public "opRect"(deviceRect: $Rect2i$Type, clipOp: integer): void
public "opRect"(localRect: $Rect2f$Type, localToDevice: $Matrix4$Type, clipOp: integer, doAA: boolean): void
public "setRect"(r: $Rect2i$Type): void
public "setRect"(left: integer, top: integer, right: integer, bottom: integer): void
public "isAA"(): boolean
public "isRect"(): boolean
get "empty"(): boolean
get "bounds"(): $Rect2i
set "rect"(value: $Rect2i$Type)
get "aA"(): boolean
get "rect"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConservativeClip$Type = ($ConservativeClip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConservativeClip_ = $ConservativeClip$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$BuiltinTypes" {
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $BuiltinTypes {
readonly "mVoid": $Type
readonly "mBool": $Type
readonly "mBool2": $Type
readonly "mBool3": $Type
readonly "mBool4": $Type
readonly "mShort": $Type
readonly "mShort2": $Type
readonly "mShort3": $Type
readonly "mShort4": $Type
readonly "mUShort": $Type
readonly "mUShort2": $Type
readonly "mUShort3": $Type
readonly "mUShort4": $Type
readonly "mInt": $Type
readonly "mInt2": $Type
readonly "mInt3": $Type
readonly "mInt4": $Type
readonly "mUInt": $Type
readonly "mUInt2": $Type
readonly "mUInt3": $Type
readonly "mUInt4": $Type
readonly "mHalf": $Type
readonly "mHalf2": $Type
readonly "mHalf3": $Type
readonly "mHalf4": $Type
readonly "mFloat": $Type
readonly "mFloat2": $Type
readonly "mFloat3": $Type
readonly "mFloat4": $Type
readonly "mHalf2x2": $Type
readonly "mHalf2x3": $Type
readonly "mHalf2x4": $Type
readonly "mHalf3x2": $Type
readonly "mHalf3x3": $Type
readonly "mHalf3x4": $Type
readonly "mHalf4x2": $Type
readonly "mHalf4x3": $Type
readonly "mHalf4x4": $Type
readonly "mFloat2x2": $Type
readonly "mFloat2x3": $Type
readonly "mFloat2x4": $Type
readonly "mFloat3x2": $Type
readonly "mFloat3x3": $Type
readonly "mFloat3x4": $Type
readonly "mFloat4x2": $Type
readonly "mFloat4x3": $Type
readonly "mFloat4x4": $Type
readonly "mVec2": $Type
readonly "mVec3": $Type
readonly "mVec4": $Type
readonly "mBVec2": $Type
readonly "mBVec3": $Type
readonly "mBVec4": $Type
readonly "mIVec2": $Type
readonly "mIVec3": $Type
readonly "mIVec4": $Type
readonly "mUVec2": $Type
readonly "mUVec3": $Type
readonly "mUVec4": $Type
readonly "mMin16Int": $Type
readonly "mMin16Int2": $Type
readonly "mMin16Int3": $Type
readonly "mMin16Int4": $Type
readonly "mMin16UInt": $Type
readonly "mMin16UInt2": $Type
readonly "mMin16UInt3": $Type
readonly "mMin16UInt4": $Type
readonly "mMin16Float": $Type
readonly "mMin16Float2": $Type
readonly "mMin16Float3": $Type
readonly "mMin16Float4": $Type
readonly "mInt32": $Type
readonly "mI32Vec2": $Type
readonly "mI32Vec3": $Type
readonly "mI32Vec4": $Type
readonly "mUInt32": $Type
readonly "mU32Vec2": $Type
readonly "mU32Vec3": $Type
readonly "mU32Vec4": $Type
readonly "mFloat32": $Type
readonly "mF32Vec2": $Type
readonly "mF32Vec3": $Type
readonly "mF32Vec4": $Type
readonly "mMat2": $Type
readonly "mMat3": $Type
readonly "mMat4": $Type
readonly "mMat2x2": $Type
readonly "mMat2x3": $Type
readonly "mMat2x4": $Type
readonly "mMat3x2": $Type
readonly "mMat3x3": $Type
readonly "mMat3x4": $Type
readonly "mMat4x2": $Type
readonly "mMat4x3": $Type
readonly "mMat4x4": $Type
readonly "mF32Mat2": $Type
readonly "mF32Mat3": $Type
readonly "mF32Mat4": $Type
readonly "mF32Mat2x2": $Type
readonly "mF32Mat2x3": $Type
readonly "mF32Mat2x4": $Type
readonly "mF32Mat3x2": $Type
readonly "mF32Mat3x3": $Type
readonly "mF32Mat3x4": $Type
readonly "mF32Mat4x2": $Type
readonly "mF32Mat4x3": $Type
readonly "mF32Mat4x4": $Type
readonly "mImage1D": $Type
readonly "mImage2D": $Type
readonly "mImage3D": $Type
readonly "mImageCube": $Type
readonly "mImageBuffer": $Type
readonly "mImage1DArray": $Type
readonly "mImage2DArray": $Type
readonly "mImageCubeArray": $Type
readonly "mImage2DMS": $Type
readonly "mImage2DMSArray": $Type
readonly "mSubpassInput": $Type
readonly "mSubpassInputMS": $Type
readonly "mTexture1D": $Type
readonly "mTexture2D": $Type
readonly "mTexture3D": $Type
readonly "mTextureCube": $Type
readonly "mTextureBuffer": $Type
readonly "mTexture1DArray": $Type
readonly "mTexture2DArray": $Type
readonly "mTextureCubeArray": $Type
readonly "mTexture2DMS": $Type
readonly "mTexture2DMSArray": $Type
readonly "mSampler": $Type
readonly "mSamplerShadow": $Type
readonly "mSampler1D": $Type
readonly "mSampler2D": $Type
readonly "mSampler3D": $Type
readonly "mSamplerCube": $Type
readonly "mSamplerBuffer": $Type
readonly "mSampler1DArray": $Type
readonly "mSampler2DArray": $Type
readonly "mSamplerCubeArray": $Type
readonly "mSampler2DMS": $Type
readonly "mSampler2DMSArray": $Type
readonly "mSampler1DShadow": $Type
readonly "mSampler2DShadow": $Type
readonly "mSamplerCubeShadow": $Type
readonly "mSampler1DArrayShadow": $Type
readonly "mSampler2DArrayShadow": $Type
readonly "mSamplerCubeArrayShadow": $Type
readonly "mInvalid": $Type
readonly "mGenFType": $Type
readonly "mGenIType": $Type
readonly "mGenUType": $Type
readonly "mGenHType": $Type
readonly "mGenSType": $Type
readonly "mGenUSType": $Type
readonly "mGenBType": $Type
readonly "mMat": $Type
readonly "mHMat": $Type
readonly "mVec": $Type
readonly "mIVec": $Type
readonly "mUVec": $Type
readonly "mHVec": $Type
readonly "mSVec": $Type
readonly "mUSVec": $Type
readonly "mBVec": $Type
readonly "mPoison": $Type

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltinTypes$Type = ($BuiltinTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltinTypes_ = $BuiltinTypes$Type;
}}
declare module "packages/icyllis/arc3d/core/$MaskFilter" {
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"

export class $MaskFilter {

constructor()

public "computeFastBounds"(src: $Rect2f$Type, dst: $Rect2f$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaskFilter$Type = ($MaskFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaskFilter_ = $MaskFilter$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PipelineDesc" {
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$KeyBuilder, $KeyBuilder$Type} from "packages/icyllis/arc3d/engine/$KeyBuilder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"

export class $PipelineDesc extends $KeyBuilder {
static readonly "DEFAULT_INITIAL_CAPACITY": integer

constructor()
constructor(other: $PipelineDesc$Type)

public static "build"(desc: $PipelineDesc$Type, info: $PipelineInfo$Type, caps: $Caps$Type): $PipelineDesc
public static "describe"(info: $PipelineInfo$Type, caps: $Caps$Type): string
public "getShaderKeyLength"(): integer
/**
 * 
 * @deprecated
 */
public "add"(arg0: integer): boolean
/**
 * 
 * @deprecated
 */
public "remove"(arg0: any): boolean
public static "of"(arg0: integer, arg1: integer): $IntList
public static "of"(arg0: integer): $IntList
public static "of"(arg0: integer, arg1: integer, arg2: integer): $IntList
/**
 * 
 * @deprecated
 */
public "contains"(arg0: any): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public "toArray"(): (any)[]
public "toArray"<T>(arg0: (T)[]): (T)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
get "shaderKeyLength"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PipelineDesc$Type = ($PipelineDesc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PipelineDesc_ = $PipelineDesc$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace$Model" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ColorSpace$Model extends $Enum<($ColorSpace$Model)> {
static readonly "RGB": $ColorSpace$Model
static readonly "XYZ": $ColorSpace$Model
static readonly "LAB": $ColorSpace$Model
static readonly "CMYK": $ColorSpace$Model


public static "values"(): ($ColorSpace$Model)[]
public static "valueOf"(name: string): $ColorSpace$Model
public "getComponentCount"(): integer
get "componentCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Model$Type = (("cmyk") | ("xyz") | ("rgb") | ("lab")) | ($ColorSpace$Model);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace$Model_ = $ColorSpace$Model$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$NFA" {
import {$NFAState, $NFAState$Type} from "packages/icyllis/arc3d/compiler/parser/$NFAState"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$RegexNode, $RegexNode$Type} from "packages/icyllis/arc3d/compiler/parser/$RegexNode"

export class $NFA {

constructor()

public "add"(state: $NFAState$Type): integer
public "add"(node: $RegexNode$Type): void
public "get"(index: integer): $NFAState
public "replace"(index: integer, shadow: $IntList$Type): $IntList
public "match"(s: string): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NFA$Type = ($NFA);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NFA_ = $NFA$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$InterfaceBlock" {
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"
import {$Node$ElementKind, $Node$ElementKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ElementKind"
import {$TopLevelElement, $TopLevelElement$Type} from "packages/icyllis/arc3d/compiler/tree/$TopLevelElement"
import {$Modifiers, $Modifiers$Type} from "packages/icyllis/arc3d/compiler/tree/$Modifiers"
import {$Variable, $Variable$Type} from "packages/icyllis/arc3d/compiler/tree/$Variable"

export class $InterfaceBlock extends $TopLevelElement {
 "mPosition": integer

constructor(position: integer, variable: $Variable$Type)

public "toString"(): string
public static "convert"(context: $Context$Type, pos: integer, modifiers: $Modifiers$Type, blockType: $Type$Type, instanceName: string): $InterfaceBlock
public static "make"(context: $Context$Type, pos: integer, variable: $Variable$Type): $InterfaceBlock
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getVariable"(): $Variable
public "getBlockName"(): string
public "getKind"(): $Node$ElementKind
public "getInstanceName"(): string
get "variable"(): $Variable
get "blockName"(): string
get "kind"(): $Node$ElementKind
get "instanceName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InterfaceBlock$Type = ($InterfaceBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InterfaceBlock_ = $InterfaceBlock$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace$Connector" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$ColorSpace$RenderIntent, $ColorSpace$RenderIntent$Type} from "packages/icyllis/arc3d/core/$ColorSpace$RenderIntent"

export class $ColorSpace$Connector {


public "getRenderIntent"(): $ColorSpace$RenderIntent
public "transform"(r: float, g: float, b: float): (float)[]
public "transform"(v: (float)[]): (float)[]
public "getSource"(): $ColorSpace
public "getDestination"(): $ColorSpace
get "renderIntent"(): $ColorSpace$RenderIntent
get "source"(): $ColorSpace
get "destination"(): $ColorSpace
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Connector$Type = ($ColorSpace$Connector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace$Connector_ = $ColorSpace$Connector$Type;
}}
declare module "packages/icyllis/arc3d/engine/$BackendSemaphore" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BackendSemaphore {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BackendSemaphore$Type = ($BackendSemaphore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BackendSemaphore_ = $BackendSemaphore$Type;
}}
declare module "packages/icyllis/arc3d/core/$PathConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PathConsumer {

 "moveTo"(arg0: float, arg1: float): void
 "lineTo"(arg0: float, arg1: float): void
 "quadTo"(pts: (float)[], off: integer): void
 "quadTo"(arg0: float, arg1: float, arg2: float, arg3: float): void
 "closePath"(): void
 "pathDone"(): void
 "cubicTo"(pts: (float)[], off: integer): void
 "cubicTo"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
}

export namespace $PathConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathConsumer$Type = ($PathConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathConsumer_ = $PathConsumer$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuTexture" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$GpuResource, $GpuResource$Type} from "packages/icyllis/arc3d/engine/$GpuResource"
import {$ReleaseCallback, $ReleaseCallback$Type} from "packages/icyllis/arc3d/engine/$ReleaseCallback"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $GpuTexture extends $GpuResource implements $IGpuSurface {


public "isProtected"(): boolean
public "isReadOnly"(): boolean
public "getWidth"(): integer
public "getHeight"(): integer
public "setMipmapsDirty"(mipmapsDirty: boolean): void
public "isMipmapped"(): boolean
public "isMipmapsDirty"(): boolean
public "getSurfaceFlags"(): integer
public "getMaxMipmapLevel"(): integer
public "asTexture"(): $GpuTexture
public static "computeSize"(format: $BackendFormat$Type, width: integer, height: integer, sampleCount: integer, levelCount: integer, approx: boolean): long
public static "computeSize"(format: $BackendFormat$Type, width: integer, height: integer, sampleCount: integer, levelCount: integer): long
public static "computeSize"(format: $BackendFormat$Type, width: integer, height: integer, sampleCount: integer, mipmapped: boolean, approx: boolean): long
public "getBackendTexture"(): $BackendTexture
public "setReleaseCallback"(callback: $ReleaseCallback$Type): void
public "isExternal"(): boolean
public "asRenderTarget"(): $GpuRenderTarget
public "getBackendFormat"(): $BackendFormat
public "getSampleCount"(): integer
public static "getApproxSize"(size: integer): integer
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "protected"(): boolean
get "readOnly"(): boolean
get "width"(): integer
get "height"(): integer
set "mipmapsDirty"(value: boolean)
get "mipmapped"(): boolean
get "mipmapsDirty"(): boolean
get "surfaceFlags"(): integer
get "maxMipmapLevel"(): integer
get "backendTexture"(): $BackendTexture
set "releaseCallback"(value: $ReleaseCallback$Type)
get "external"(): boolean
get "backendFormat"(): $BackendFormat
get "sampleCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuTexture$Type = ($GpuTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuTexture_ = $GpuTexture$Type;
}}
declare module "packages/icyllis/arc3d/engine/ops/$RectOp" {
import {$Matrixc, $Matrixc$Type} from "packages/icyllis/arc3d/core/$Matrixc"
import {$MeshDrawOp, $MeshDrawOp$Type} from "packages/icyllis/arc3d/engine/ops/$MeshDrawOp"
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $RectOp extends $MeshDrawOp {
 "mLeft": float
 "mTop": float
 "mRight": float
 "mBottom": float

constructor(argb: integer, localRect: $Rect2f$Type, strokeRadius: float, strokePos: float, viewMatrix: $Matrixc$Type, stroke: boolean, aa: boolean)

public "setInstanceBuffer"(buffer: $GpuBuffer$Type, baseInstance: integer, actualInstanceCount: integer): void
public "getInstanceCount"(): integer
public "getVertexCount"(): integer
public "setVertexBuffer"(buffer: $GpuBuffer$Type, baseVertex: integer, actualVertexCount: integer): void
public "onExecute"(state: $OpFlushState$Type, chainBounds: $Rect2f$Type): void
get "instanceCount"(): integer
get "vertexCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RectOp$Type = ($RectOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RectOp_ = $RectOp$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace$RenderIntent" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ColorSpace$RenderIntent extends $Enum<($ColorSpace$RenderIntent)> {
static readonly "PERCEPTUAL": $ColorSpace$RenderIntent
static readonly "RELATIVE": $ColorSpace$RenderIntent
static readonly "SATURATION": $ColorSpace$RenderIntent
static readonly "ABSOLUTE": $ColorSpace$RenderIntent


public static "values"(): ($ColorSpace$RenderIntent)[]
public static "valueOf"(name: string): $ColorSpace$RenderIntent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$RenderIntent$Type = (("saturation") | ("absolute") | ("perceptual") | ("relative")) | ($ColorSpace$RenderIntent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace$RenderIntent_ = $ColorSpace$RenderIntent$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TopologicalSort" {
import {$TopologicalSort$Access, $TopologicalSort$Access$Type} from "packages/icyllis/arc3d/engine/$TopologicalSort$Access"
import {$List, $List$Type} from "packages/java/util/$List"

export class $TopologicalSort {

constructor()

public static "topologicalSort"<T>(graph: $List$Type<(T)>, access: $TopologicalSort$Access$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TopologicalSort$Type = ($TopologicalSort);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TopologicalSort_ = $TopologicalSort$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanCommandPool" {
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"
import {$VulkanDevice, $VulkanDevice$Type} from "packages/icyllis/arc3d/vulkan/$VulkanDevice"

export class $VulkanCommandPool extends $ManagedResource {


public "reset"(): void
public static "create"(device: $VulkanDevice$Type): $VulkanCommandPool
public "check"(): boolean
public "submit"(): boolean
public "isSubmitted"(): boolean
get "submitted"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanCommandPool$Type = ($VulkanCommandPool);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanCommandPool_ = $VulkanCommandPool$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ThreadSafeCache" {
import {$ResourceCache, $ResourceCache$Type} from "packages/icyllis/arc3d/engine/$ResourceCache"

export class $ThreadSafeCache {

constructor()

public "dropUniqueRefsOlderThan"(nanoTime: long): void
public "dropUniqueRefs"(resourceCache: $ResourceCache$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ThreadSafeCache$Type = ($ThreadSafeCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ThreadSafeCache_ = $ThreadSafeCache$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$LinearGradient" {
import {$UnivariateGradientShader, $UnivariateGradientShader$Type} from "packages/icyllis/arc3d/core/shaders/$UnivariateGradientShader"

export class $LinearGradient extends $UnivariateGradientShader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinearGradient$Type = ($LinearGradient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinearGradient_ = $LinearGradient$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$ShaderBuilderBase" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ShaderBuilder, $ShaderBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$ShaderBuilder"
import {$PipelineBuilder, $PipelineBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$PipelineBuilder"

export class $ShaderBuilderBase implements $ShaderBuilder {

constructor(pipelineBuilder: $PipelineBuilder$Type)

public "toUTF8"(): $ByteBuffer
public "declAppend"(arg0: $ShaderVar$Type): void
public "toString"(): string
public "finish"(): void
public "getCount"(): integer
public "getStrings"(): (charseq)[]
public "getMangledName"(baseName: string): string
public "addExtension"(extensionName: string): void
public "codeAppend"(str: string): void
public "codeAppendf"(format: string, ...args: (any)[]): void
public "codePrependf"(format: string, ...args: (any)[]): void
get "count"(): integer
get "strings"(): (charseq)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderBuilderBase$Type = ($ShaderBuilderBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderBuilderBase_ = $ShaderBuilderBase$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLSampler" {
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"

export class $GLSampler extends $ManagedResource {


public static "create"(device: $GLDevice$Type, samplerState: integer): $GLSampler
public "getHandle"(): integer
public "discard"(): void
get "handle"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLSampler$Type = ($GLSampler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLSampler_ = $GLSampler$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute" {
import {$ShaderVar, $ShaderVar$Type} from "packages/icyllis/arc3d/engine/$ShaderVar"

export class $GeometryProcessor$Attribute {

constructor(name: string, srcType: byte, dstType: byte, offset: integer)
constructor(name: string, srcType: byte, dstType: byte)

public "name"(): string
public "size"(): integer
public "offset"(): integer
public "stride"(): integer
public "dstType"(): byte
public "srcType"(): byte
public "locations"(): integer
public static "alignOffset"(offset: integer): integer
public "asShaderVar"(): $ShaderVar
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeometryProcessor$Attribute$Type = ($GeometryProcessor$Attribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeometryProcessor$Attribute_ = $GeometryProcessor$Attribute$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Swizzle" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $Swizzle extends $Expression {
static readonly "X": byte
static readonly "Y": byte
static readonly "Z": byte
static readonly "W": byte
static readonly "R": byte
static readonly "G": byte
static readonly "B": byte
static readonly "A": byte
static readonly "S": byte
static readonly "T": byte
static readonly "P": byte
static readonly "Q": byte
static readonly "ZERO": byte
static readonly "ONE": byte
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, base: $Expression$Type, maskPosition: integer, maskString: string): $Expression
public static "make"(context: $Context$Type, position: integer, base: $Expression$Type, components: (byte)[], numComponents: integer): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "getBase"(): $Expression
public "getComponents"(): (byte)[]
public "getKind"(): $Node$ExpressionKind
get "base"(): $Expression
get "components"(): (byte)[]
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Swizzle$Type = ($Swizzle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Swizzle_ = $Swizzle$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$CodeGenerator" {
import {$TranslationUnit, $TranslationUnit$Type} from "packages/icyllis/arc3d/compiler/tree/$TranslationUnit"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$ShaderCompiler, $ShaderCompiler$Type} from "packages/icyllis/arc3d/compiler/$ShaderCompiler"

export class $CodeGenerator {

constructor(compiler: $ShaderCompiler$Type, translationUnit: $TranslationUnit$Type)

public "generateCode"(): $ByteBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodeGenerator$Type = ($CodeGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodeGenerator_ = $CodeGenerator$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SharedContextInfo" {
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/engine/$Context"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$PipelineStateCache, $PipelineStateCache$Type} from "packages/icyllis/arc3d/engine/$PipelineStateCache"
import {$ThreadSafeCache, $ThreadSafeCache$Type} from "packages/icyllis/arc3d/engine/$ThreadSafeCache"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"
import {$SurfaceCharacterization, $SurfaceCharacterization$Type} from "packages/icyllis/arc3d/core/$SurfaceCharacterization"

export class $SharedContextInfo {


public "hashCode"(): integer
public "matches"(c: $Context$Type): boolean
public "isValid"(): boolean
public "getOptions"(): $ContextOptions
public "getMaxSurfaceSampleCount"(colorType: integer): integer
public "getCompressedBackendFormat"(compressionType: integer): $BackendFormat
public "getPipelineStateCache"(): $PipelineStateCache
public "getDefaultBackendFormat"(colorType: integer, renderable: boolean): $BackendFormat
public "createCharacterization"(cacheMaxResourceBytes: long, imageInfo: $ImageInfo$Type, backendFormat: $BackendFormat$Type, origin: integer, sampleCount: integer, texturable: boolean, mipmapped: boolean, glWrapDefaultFramebuffer: boolean, vkSupportInputAttachment: boolean, vkSecondaryCommandBuffer: boolean, isProtected: boolean): $SurfaceCharacterization
public "getBackend"(): integer
public "getCaps"(): $Caps
public "getContextID"(): integer
public "getThreadSafeCache"(): $ThreadSafeCache
get "valid"(): boolean
get "options"(): $ContextOptions
get "pipelineStateCache"(): $PipelineStateCache
get "backend"(): integer
get "caps"(): $Caps
get "contextID"(): integer
get "threadSafeCache"(): $ThreadSafeCache
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharedContextInfo$Type = ($SharedContextInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharedContextInfo_ = $SharedContextInfo$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TopologicalSort$Access" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"

export interface $TopologicalSort$Access<T> {

 "getIndex"(arg0: T): integer
 "setIndex"(arg0: T, arg1: integer): void
 "getIncomingEdges"(arg0: T): $Collection<(T)>
 "isTempMarked"(arg0: T): boolean
 "setTempMarked"(arg0: T, arg1: boolean): void
}

export namespace $TopologicalSort$Access {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TopologicalSort$Access$Type<T> = ($TopologicalSort$Access<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TopologicalSort$Access_<T> = $TopologicalSort$Access$Type<(T)>;
}}
declare module "packages/icyllis/arc3d/core/shaders/$BilinearGradient" {
import {$BivariateGradientShader, $BivariateGradientShader$Type} from "packages/icyllis/arc3d/core/shaders/$BivariateGradientShader"

export class $BilinearGradient extends $BivariateGradientShader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BilinearGradient$Type = ($BilinearGradient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BilinearGradient_ = $BilinearGradient$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLGraphicsPipelineState" {
import {$GLCommandBuffer, $GLCommandBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLCommandBuffer"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"
import {$GLBuffer, $GLBuffer$Type} from "packages/icyllis/arc3d/opengl/$GLBuffer"
import {$GraphicsPipelineState, $GraphicsPipelineState$Type} from "packages/icyllis/arc3d/engine/$GraphicsPipelineState"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $GLGraphicsPipelineState extends $GraphicsPipelineState {


public "release"(): void
public "bindPipeline"(commandBuffer: $GLCommandBuffer$Type): boolean
public "bindBuffers"(indexBuffer: $GpuBuffer$Type, vertexBuffer: $GpuBuffer$Type, vertexOffset: long, instanceBuffer: $GpuBuffer$Type, instanceOffset: long): void
public "bindTextures"(commandBuffer: $GLCommandBuffer$Type, pipelineInfo: $PipelineInfo$Type, geomTextures: ($TextureProxy$Type)[]): boolean
public "bindUniforms"(commandBuffer: $GLCommandBuffer$Type, pipelineInfo: $PipelineInfo$Type, width: integer, height: integer): boolean
public "bindInstanceBuffer"(buffer: $GLBuffer$Type, offset: long): void
public "getInstanceStride"(): integer
public "discard"(): void
public "getVertexStride"(): integer
public "bindIndexBuffer"(buffer: $GLBuffer$Type): void
public "bindVertexBuffer"(buffer: $GLBuffer$Type, offset: long): void
get "instanceStride"(): integer
get "vertexStride"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLGraphicsPipelineState$Type = ($GLGraphicsPipelineState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLGraphicsPipelineState_ = $GLGraphicsPipelineState$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PipelineStateCache" {
import {$PipelineStateCache$Stats, $PipelineStateCache$Stats$Type} from "packages/icyllis/arc3d/engine/$PipelineStateCache$Stats"
import {$PipelineDesc, $PipelineDesc$Type} from "packages/icyllis/arc3d/engine/$PipelineDesc"
import {$GraphicsPipelineState, $GraphicsPipelineState$Type} from "packages/icyllis/arc3d/engine/$GraphicsPipelineState"
import {$PipelineInfo, $PipelineInfo$Type} from "packages/icyllis/arc3d/engine/$PipelineInfo"

export class $PipelineStateCache {

constructor()

public "findOrCreateGraphicsPipelineState"(arg0: $PipelineDesc$Type, arg1: $PipelineInfo$Type): $GraphicsPipelineState
public "getStats"(): $PipelineStateCache$Stats
get "stats"(): $PipelineStateCache$Stats
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PipelineStateCache$Type = ($PipelineStateCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PipelineStateCache_ = $PipelineStateCache$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceAllocator" {
import {$DirectContext, $DirectContext$Type} from "packages/icyllis/arc3d/engine/$DirectContext"
import {$SurfaceProxy, $SurfaceProxy$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy"

export class $SurfaceAllocator {

constructor(context: $DirectContext$Type)

public "reset"(): void
public "allocate"(): boolean
public "isInstantiationFailed"(): boolean
public "simulate"(): boolean
public "incOps"(): void
public "curOp"(): integer
public "addInterval"(proxy: $SurfaceProxy$Type, start: integer, end: integer, actualUse: boolean): void
get "instantiationFailed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceAllocator$Type = ($SurfaceAllocator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceAllocator_ = $SurfaceAllocator$Type;
}}
declare module "packages/icyllis/arc3d/core/image/$GIFDecoder" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GIFDecoder {
static "sDefaultDelayMillis": integer

constructor(buf: $ByteBuffer$Type)

public "readByte"(): integer
public "getScreenWidth"(): integer
public "getScreenHeight"(): integer
public "skipExtension"(): void
public static "checkMagic"(buf: (byte)[]): boolean
public "decodeNextFrame"(pixels: $ByteBuffer$Type): integer
get "screenWidth"(): integer
get "screenHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GIFDecoder$Type = ($GIFDecoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GIFDecoder_ = $GIFDecoder$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLCaps$FormatInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GLCaps$FormatInfo {
static readonly "TEXTURABLE_FLAG": integer
static readonly "COLOR_ATTACHMENT_FLAG": integer
static readonly "COLOR_ATTACHMENT_WITH_MSAA_FLAG": integer
static readonly "TEXTURE_STORAGE_FLAG": integer
static readonly "TRANSFERS_FLAG": integer
static readonly "FORMAT_TYPE_UNKNOWN": integer
static readonly "FORMAT_TYPE_NORMALIZED_FIXED_POINT": integer
static readonly "FORMAT_TYPE_FLOAT": integer


public "toString"(): string
public "externalFormat"(dstColorType: integer, srcColorType: integer, write: boolean): integer
public "externalType"(dstColorType: integer, srcColorType: integer): integer
public "colorTypeFlags"(colorType: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLCaps$FormatInfo$Type = ($GLCaps$FormatInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLCaps$FormatInfo_ = $GLCaps$FormatInfo$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$BivariateGradientShader" {
import {$GradientShader, $GradientShader$Type} from "packages/icyllis/arc3d/core/shaders/$GradientShader"

export class $BivariateGradientShader extends $GradientShader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BivariateGradientShader$Type = ($BivariateGradientShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BivariateGradientShader_ = $BivariateGradientShader$Type;
}}
declare module "packages/icyllis/arc3d/core/shaders/$UnivariateGradientShader" {
import {$GradientShader, $GradientShader$Type} from "packages/icyllis/arc3d/core/shaders/$GradientShader"

export class $UnivariateGradientShader extends $GradientShader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnivariateGradientShader$Type = ($UnivariateGradientShader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnivariateGradientShader_ = $UnivariateGradientShader$Type;
}}
declare module "packages/icyllis/arc3d/core/$RefCounted" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RefCounted {

 "ref"(): void
 "unref"(): void
}

export namespace $RefCounted {
function create<T>(that: T): T
function create<T>(sp: T, that: T): T
function move<T>(sp: T): T
function move<T>(sp: T, that: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RefCounted$Type = ($RefCounted);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RefCounted_ = $RefCounted$Type;
}}
declare module "packages/icyllis/arc3d/engine/shading/$VaryingHandler" {
import {$Varying, $Varying$Type} from "packages/icyllis/arc3d/engine/shading/$Varying"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$GeometryProcessor$Attribute, $GeometryProcessor$Attribute$Type} from "packages/icyllis/arc3d/engine/$GeometryProcessor$Attribute"
import {$PipelineBuilder, $PipelineBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$PipelineBuilder"

export class $VaryingHandler {
static readonly "kSmooth_Interpolation": integer
static readonly "kCanBeFlat_Interpolation": integer
static readonly "kRequiredToBeFlat_Interpolation": integer

constructor(pipelineBuilder: $PipelineBuilder$Type)

public "finish"(): void
public "getVertDecls"(outputDecls: $StringBuilder$Type): void
public "getFragDecls"(inputDecls: $StringBuilder$Type): void
public "setNoPerspective"(): void
public "addVarying"(name: string, varying: $Varying$Type): void
public "addVarying"(name: string, varying: $Varying$Type, interpolation: integer): void
public "addPassThroughAttribute"(attr: $GeometryProcessor$Attribute$Type, output: string, interpolation: integer): void
public "addPassThroughAttribute"(attr: $GeometryProcessor$Attribute$Type, output: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VaryingHandler$Type = ($VaryingHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VaryingHandler_ = $VaryingHandler$Type;
}}
declare module "packages/icyllis/arc3d/core/$PathIterator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PathIterator {

 "next"(arg0: (float)[], arg1: integer): integer

(arg0: (float)[], arg1: integer): integer
}

export namespace $PathIterator {
const FILL_NON_ZERO: integer
const FILL_EVEN_ODD: integer
const VERB_MOVE: integer
const VERB_LINE: integer
const VERB_QUAD: integer
const VERB_CUBIC: integer
const VERB_CLOSE: integer
const VERB_DONE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathIterator$Type = ($PathIterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathIterator_ = $PathIterator$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuDevice" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$GpuDevice$Stats, $GpuDevice$Stats$Type} from "packages/icyllis/arc3d/engine/$GpuDevice$Stats"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$GpuTexture, $GpuTexture$Type} from "packages/icyllis/arc3d/engine/$GpuTexture"
import {$PipelineStateCache, $PipelineStateCache$Type} from "packages/icyllis/arc3d/engine/$PipelineStateCache"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"
import {$OpsRenderPass, $OpsRenderPass$Type} from "packages/icyllis/arc3d/engine/$OpsRenderPass"
import {$GpuRenderTarget, $GpuRenderTarget$Type} from "packages/icyllis/arc3d/engine/$GpuRenderTarget"
import {$Engine, $Engine$Type} from "packages/icyllis/arc3d/engine/$Engine"
import {$Caps, $Caps$Type} from "packages/icyllis/arc3d/engine/$Caps"
import {$FlushInfo$FinishedCallback, $FlushInfo$FinishedCallback$Type} from "packages/icyllis/arc3d/engine/$FlushInfo$FinishedCallback"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$DirectContext, $DirectContext$Type} from "packages/icyllis/arc3d/engine/$DirectContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BackendRenderTarget, $BackendRenderTarget$Type} from "packages/icyllis/arc3d/engine/$BackendRenderTarget"
import {$GpuBufferPool, $GpuBufferPool$Type} from "packages/icyllis/arc3d/engine/$GpuBufferPool"
import {$BackendTexture, $BackendTexture$Type} from "packages/icyllis/arc3d/engine/$BackendTexture"
import {$SurfaceView, $SurfaceView$Type} from "packages/icyllis/arc3d/engine/$SurfaceView"
import {$ShaderCompiler, $ShaderCompiler$Type} from "packages/icyllis/arc3d/compiler/$ShaderCompiler"
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"
import {$IGpuSurface, $IGpuSurface$Type} from "packages/icyllis/arc3d/engine/$IGpuSurface"

export class $GpuDevice implements $Engine {


public "getContext"(): $DirectContext
public "disconnect"(cleanup: boolean): void
public "createBuffer"(size: integer, flags: integer): $GpuBuffer
public "getResourceProvider"(): $ResourceProvider
public "generateMipmaps"(texture: $GpuTexture$Type): boolean
public "copySurface"(src: $IGpuSurface$Type, srcX: integer, srcY: integer, dst: $IGpuSurface$Type, dstX: integer, dstY: integer, width: integer, height: integer): boolean
public "copySurface"(src: $IGpuSurface$Type, srcL: integer, srcT: integer, srcR: integer, srcB: integer, dst: $IGpuSurface$Type, dstL: integer, dstT: integer, dstR: integer, dstB: integer, filter: integer): boolean
public "writePixels"(texture: $GpuTexture$Type, x: integer, y: integer, width: integer, height: integer, dstColorType: integer, srcColorType: integer, rowBytes: integer, pixels: long): boolean
public "getOpsRenderPass"(writeView: $SurfaceView$Type, contentBounds: $Rect2i$Type, colorOps: byte, stencilOps: byte, clearColor: (float)[], sampledTextures: $Set$Type<($TextureProxy$Type)>, pipelineFlags: integer): $OpsRenderPass
public "checkFence"(arg0: long): boolean
public "insertFence"(): long
public "deleteFence"(arg0: long): void
public "waitForQueue"(): void
public "wrapBackendRenderTarget"(backendRenderTarget: $BackendRenderTarget$Type): $GpuRenderTarget
public "onWrapBackendRenderTarget"(arg0: $BackendRenderTarget$Type): $GpuRenderTarget
public "getPipelineStateCache"(): $PipelineStateCache
public "wrapRenderableBackendTexture"(texture: $BackendTexture$Type, sampleCount: integer, ownership: boolean): $GpuTexture
public "checkFinishedCallbacks"(): void
public "addFinishedCallback"(arg0: $FlushInfo$FinishedCallback$Type): void
public "resolveRenderTarget"(renderTarget: $GpuRenderTarget$Type, resolveLeft: integer, resolveTop: integer, resolveRight: integer, resolveBottom: integer): void
public "getShaderCompiler"(): $ShaderCompiler
public "getStats"(): $GpuDevice$Stats
public "getInstancePool"(): $GpuBufferPool
public "createTexture"(width: integer, height: integer, format: $BackendFormat$Type, sampleCount: integer, surfaceFlags: integer, label: string): $GpuTexture
public "getVertexPool"(): $GpuBufferPool
public "getIndexPool"(): $GpuBufferPool
public "getCaps"(): $Caps
public "markContextDirty"(state: integer): void
public static "colorTypeClampType"(ct: integer): integer
public static "colorTypeEncoding"(ct: integer): integer
public static "colorTypeToPublic"(ct: integer): integer
get "context"(): $DirectContext
get "resourceProvider"(): $ResourceProvider
get "pipelineStateCache"(): $PipelineStateCache
get "shaderCompiler"(): $ShaderCompiler
get "stats"(): $GpuDevice$Stats
get "instancePool"(): $GpuBufferPool
get "vertexPool"(): $GpuBufferPool
get "indexPool"(): $GpuBufferPool
get "caps"(): $Caps
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuDevice$Type = ($GpuDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuDevice_ = $GpuDevice$Type;
}}
declare module "packages/icyllis/arc3d/engine/$RecycledResource" {
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"
import {$ManagedResource, $ManagedResource$Type} from "packages/icyllis/arc3d/engine/$ManagedResource"

export class $RecycledResource extends $ManagedResource {

constructor(device: $GpuDevice$Type)

public "recycle"(): void
public "onRecycle"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecycledResource$Type = ($RecycledResource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecycledResource_ = $RecycledResource$Type;
}}
declare module "packages/icyllis/arc3d/engine/$CommandBuffer" {
import {$GpuBuffer, $GpuBuffer$Type} from "packages/icyllis/arc3d/engine/$GpuBuffer"

export class $CommandBuffer {

constructor()

public "moveAndTrackGpuBuffer"(buffer: $GpuBuffer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandBuffer$Type = ($CommandBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandBuffer_ = $CommandBuffer$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$Position" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Position {
static readonly "NO_POS": integer
static readonly "MAX_OFFSET": integer


public static "range"(start: integer, end: integer): integer
public static "getLine"(pos: integer, source: (character)[]): integer
public static "getEndOffset"(pos: integer): integer
public static "getStartOffset"(pos: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Position$Type = ($Position);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Position_ = $Position$Type;
}}
declare module "packages/icyllis/arc3d/compiler/parser/$NFAState" {
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"

export interface $NFAState {

 "next"(): $IntList
 "accept"(arg0: character): boolean
}

export namespace $NFAState {
function Filter(filter: $IntPredicate$Type, next: $IntList$Type): $NFAState
function Accept(token: integer): $NFAState
function Replace(shadow: $IntList$Type): $NFAState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NFAState$Type = ($NFAState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NFAState_ = $NFAState$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace" {
import {$ColorSpace$Named, $ColorSpace$Named$Type} from "packages/icyllis/arc3d/core/$ColorSpace$Named"
import {$ColorSpace$Rgb$TransferParameters, $ColorSpace$Rgb$TransferParameters$Type} from "packages/icyllis/arc3d/core/$ColorSpace$Rgb$TransferParameters"
import {$ColorSpace$RenderIntent, $ColorSpace$RenderIntent$Type} from "packages/icyllis/arc3d/core/$ColorSpace$RenderIntent"
import {$ColorSpace$Model, $ColorSpace$Model$Type} from "packages/icyllis/arc3d/core/$ColorSpace$Model"
import {$ColorSpace$Connector, $ColorSpace$Connector$Type} from "packages/icyllis/arc3d/core/$ColorSpace$Connector"
import {$ColorSpace$Adaptation, $ColorSpace$Adaptation$Type} from "packages/icyllis/arc3d/core/$ColorSpace$Adaptation"

export class $ColorSpace {
static readonly "ILLUMINANT_A": (float)[]
static readonly "ILLUMINANT_B": (float)[]
static readonly "ILLUMINANT_C": (float)[]
static readonly "ILLUMINANT_D50": (float)[]
static readonly "ILLUMINANT_D55": (float)[]
static readonly "ILLUMINANT_D60": (float)[]
static readonly "ILLUMINANT_D65": (float)[]
static readonly "ILLUMINANT_D75": (float)[]
static readonly "ILLUMINANT_E": (float)[]
static readonly "MIN_ID": integer
static readonly "MAX_ID": integer


public "isWideGamut"(): boolean
public "toXyz"(arg0: (float)[]): (float)[]
public "toXyz"(r: float, g: float, b: float): (float)[]
public "fromXyz"(x: float, y: float, z: float): (float)[]
public "fromXyz"(arg0: (float)[]): (float)[]
public static "cctToXyz"(cct: integer): (float)[]
public "getName"(): string
public static "get"(name: $ColorSpace$Named$Type): $ColorSpace
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "match"(toXYZD50: (float)[], arg1: $ColorSpace$Rgb$TransferParameters$Type): $ColorSpace
public static "connect"(source: $ColorSpace$Type, destination: $ColorSpace$Type, intent: $ColorSpace$RenderIntent$Type): $ColorSpace$Connector
public static "connect"(source: $ColorSpace$Type, destination: $ColorSpace$Type): $ColorSpace$Connector
public static "connect"(source: $ColorSpace$Type, intent: $ColorSpace$RenderIntent$Type): $ColorSpace$Connector
public static "connect"(source: $ColorSpace$Type): $ColorSpace$Connector
public "getId"(): integer
public static "adapt"(colorSpace: $ColorSpace$Type, whitePoint: (float)[]): $ColorSpace
public static "adapt"(colorSpace: $ColorSpace$Type, whitePoint: (float)[], adaptation: $ColorSpace$Adaptation$Type): $ColorSpace
public static "chromaticAdaptation"(adaptation: $ColorSpace$Adaptation$Type, srcWhitePoint: (float)[], dstWhitePoint: (float)[]): (float)[]
public "getModel"(): $ColorSpace$Model
public "getComponentCount"(): integer
public "getMinValue"(arg0: integer): float
public "getMaxValue"(arg0: integer): float
public "isSrgb"(): boolean
get "wideGamut"(): boolean
get "name"(): string
get "id"(): integer
get "model"(): $ColorSpace$Model
get "componentCount"(): integer
get "srgb"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Type = ($ColorSpace);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace_ = $ColorSpace$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConstructorMatrix2Matrix" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$OptionalDouble, $OptionalDouble$Type} from "packages/java/util/$OptionalDouble"
import {$ConstructorCall, $ConstructorCall$Type} from "packages/icyllis/arc3d/compiler/tree/$ConstructorCall"
import {$Type, $Type$Type} from "packages/icyllis/arc3d/compiler/tree/$Type"

export class $ConstructorMatrix2Matrix extends $ConstructorCall {
 "mPosition": integer


public "clone"(position: integer): $Expression
public static "make"(position: integer, type: $Type$Type, arg: $Expression$Type): $Expression
public "getConstantValue"(i: integer): $OptionalDouble
public "getKind"(): $Node$ExpressionKind
get "kind"(): $Node$ExpressionKind
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorMatrix2Matrix$Type = ($ConstructorMatrix2Matrix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorMatrix2Matrix_ = $ConstructorMatrix2Matrix$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$ConditionalExpression" {
import {$Expression, $Expression$Type} from "packages/icyllis/arc3d/compiler/tree/$Expression"
import {$Node$ExpressionKind, $Node$ExpressionKind$Type} from "packages/icyllis/arc3d/compiler/tree/$Node$ExpressionKind"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $ConditionalExpression extends $Expression {
 "mPosition": integer


public "toString"(parentPrecedence: integer): string
public "clone"(position: integer): $Expression
public static "convert"(context: $Context$Type, position: integer, condition: $Expression$Type, whenTrue: $Expression$Type, whenFalse: $Expression$Type): $Expression
public "accept"(visitor: $TreeVisitor$Type): boolean
public "setCondition"(condition: $Expression$Type): void
public "getCondition"(): $Expression
public "getKind"(): $Node$ExpressionKind
public "setWhenTrue"(whenTrue: $Expression$Type): void
public "setWhenFalse"(whenFalse: $Expression$Type): void
public "getWhenFalse"(): $Expression
public "getWhenTrue"(): $Expression
set "condition"(value: $Expression$Type)
get "condition"(): $Expression
get "kind"(): $Node$ExpressionKind
set "whenTrue"(value: $Expression$Type)
set "whenFalse"(value: $Expression$Type)
get "whenFalse"(): $Expression
get "whenTrue"(): $Expression
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConditionalExpression$Type = ($ConditionalExpression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConditionalExpression_ = $ConditionalExpression$Type;
}}
declare module "packages/icyllis/arc3d/compiler/tree/$Modifiers" {
import {$Node, $Node$Type} from "packages/icyllis/arc3d/compiler/tree/$Node"
import {$TreeVisitor, $TreeVisitor$Type} from "packages/icyllis/arc3d/compiler/tree/$TreeVisitor"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"
import {$Layout, $Layout$Type} from "packages/icyllis/arc3d/compiler/tree/$Layout"

export class $Modifiers extends $Node {
static readonly "kSmooth_Flag": integer
static readonly "kFlat_Flag": integer
static readonly "kNoPerspective_Flag": integer
static readonly "kConst_Flag": integer
static readonly "kUniform_Flag": integer
static readonly "kIn_Flag": integer
static readonly "kOut_Flag": integer
static readonly "kCoherent_Flag": integer
static readonly "kVolatile_Flag": integer
static readonly "kRestrict_Flag": integer
static readonly "kReadOnly_Flag": integer
static readonly "kWriteOnly_Flag": integer
static readonly "kBuffer_Flag": integer
static readonly "kWorkgroup_Flag": integer
static readonly "kSubroutine_Flag": integer
static readonly "kPure_Flag": integer
static readonly "kInline_Flag": integer
static readonly "kNoInline_Flag": integer
static readonly "kCount_Flag": integer
static readonly "kInterpolation_Flags": integer
static readonly "kMemory_Flags": integer
static readonly "kStorage_Flags": integer
 "mPosition": integer

constructor(position: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "flags"(): integer
public "hashCode"(): integer
public "accept"(visitor: $TreeVisitor$Type): boolean
public "layoutFlags"(): integer
public "layoutOffset"(): integer
public "layoutBuiltin"(): integer
public "checkFlags"(context: $Context$Type, permittedFlags: integer): boolean
public "checkLayoutFlags"(context: $Context$Type, permittedLayoutFlags: integer): boolean
public "isConst"(): boolean
public "layout"(): $Layout
public static "describeFlags"(flags: integer): string
public static "describeFlags"(flags: integer, padded: boolean): string
public "setLayoutFlag"(context: $Context$Type, mask: integer, name: string, pos: integer): void
public "clearLayoutFlag"(mask: integer): void
public "setFlag"(context: $Context$Type, mask: integer, pos: integer): void
public "clearFlag"(mask: integer): void
public static "describeFlag"(flag: integer): string
get "const"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Modifiers$Type = ($Modifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Modifiers_ = $Modifiers$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ClipStack$Geometry" {
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export interface $ClipStack$Geometry {

 "contains"(arg0: $ClipStack$Geometry$Type): boolean
 "op"(): integer
 "outerBounds"(): $Rect2ic
}

export namespace $ClipStack$Geometry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClipStack$Geometry$Type = ($ClipStack$Geometry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClipStack$Geometry_ = $ClipStack$Geometry$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ShaderCaps" {
import {$GLSLVersion, $GLSLVersion$Type} from "packages/icyllis/arc3d/compiler/$GLSLVersion"
import {$ContextOptions, $ContextOptions$Type} from "packages/icyllis/arc3d/engine/$ContextOptions"
import {$SPIRVVersion, $SPIRVVersion$Type} from "packages/icyllis/arc3d/compiler/$SPIRVVersion"
import {$ShaderCaps as $ShaderCaps$0, $ShaderCaps$Type as $ShaderCaps$0$Type} from "packages/icyllis/arc3d/compiler/$ShaderCaps"
import {$TargetApi, $TargetApi$Type} from "packages/icyllis/arc3d/compiler/$TargetApi"

export class $ShaderCaps extends $ShaderCaps$0 {
static readonly "NotSupported_AdvBlendEqInteraction": integer
static readonly "Automatic_AdvBlendEqInteraction": integer
static readonly "GeneralEnable_AdvBlendEqInteraction": integer
 "mDualSourceBlendingSupport": boolean
 "mPreferFlatInterpolation": boolean
 "mVertexIDSupport": boolean
 "mInfinitySupport": boolean
 "mNonConstantArrayIndexSupport": boolean
 "mBitManipulationSupport": boolean
 "mReducedShaderMode": boolean
 "mTextureQueryLod": boolean
 "mTextureQueryLodExtension": string
 "mShadingLanguage420Pack": boolean
 "mShadingLanguage420PackExtensionName": string
 "mEnhancedLayouts": boolean
 "mEnhancedLayoutsExtensionName": string
 "mRequiresLocalOutputColorForFBFetch": boolean
 "mMustObfuscateUniformColor": boolean
 "mMustWriteToFragColor": boolean
 "mColorSpaceMathNeedsFloat": boolean
 "mAvoidDfDxForGradientsWhenPossible": boolean
 "mSecondaryOutputExtension": string
 "mAdvBlendEqInteraction": integer
 "mMaxFragmentSamplers": integer
 "mTargetApi": $TargetApi
 "mGLSLVersion": $GLSLVersion
 "mSPIRVVersion": $SPIRVVersion

constructor()

public "toString"(): string
public "applyOptionsOverrides"(options: $ContextOptions$Type): void
public "mustEnableAdvBlendEqs"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderCaps$Type = ($ShaderCaps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderCaps_ = $ShaderCaps$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$Writer" {
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export interface $Writer {

 "writeString8"(arg0: $Context$Type, arg1: string): void
 "writeWords"(arg0: (integer)[], arg1: integer): void
 "writeWord"(arg0: integer): void
}

export namespace $Writer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Writer$Type = ($Writer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Writer_ = $Writer$Type;
}}
declare module "packages/icyllis/arc3d/core/$Raster" {
import {$ColorSpace, $ColorSpace$Type} from "packages/icyllis/arc3d/core/$ColorSpace"
import {$PixelMap, $PixelMap$Type} from "packages/icyllis/arc3d/core/$PixelMap"
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$PixelRef, $PixelRef$Type} from "packages/icyllis/arc3d/core/$PixelRef"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"

export class $Raster {
static readonly "FORMAT_UNKNOWN": integer
static readonly "FORMAT_GRAY_8": integer
static readonly "FORMAT_GRAY_16": integer
static readonly "FORMAT_RGB_565": integer
static readonly "FORMAT_RGB_888": integer

constructor(bufImg: $BufferedImage$Type, info: $ImageInfo$Type, data: any, baseOffset: integer, rowStride: integer)

public "getFormat"(): integer
public "getInfo"(): $ImageInfo
public "getWidth"(): integer
public "getHeight"(): integer
public "getColorType"(): integer
public "getColorSpace"(): $ColorSpace
public static "createRaster"(width: integer, height: integer, format: integer): $Raster
public "getAlphaType"(): integer
public "getPixelMap"(): $PixelMap
public "getPixelRef"(): $PixelRef
get "format"(): integer
get "info"(): $ImageInfo
get "width"(): integer
get "height"(): integer
get "colorType"(): integer
get "colorSpace"(): $ColorSpace
get "alphaType"(): integer
get "pixelMap"(): $PixelMap
get "pixelRef"(): $PixelRef
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Raster$Type = ($Raster);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Raster_ = $Raster$Type;
}}
declare module "packages/icyllis/arc3d/opengl/$GLAttachment" {
import {$RefCounted, $RefCounted$Type} from "packages/icyllis/arc3d/core/$RefCounted"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$Attachment, $Attachment$Type} from "packages/icyllis/arc3d/engine/$Attachment"
import {$GLDevice, $GLDevice$Type} from "packages/icyllis/arc3d/opengl/$GLDevice"

export class $GLAttachment extends $Attachment {


public "toString"(): string
public "getFormat"(): integer
public "getBackendFormat"(): $BackendFormat
public static "makeStencil"(device: $GLDevice$Type, width: integer, height: integer, sampleCount: integer, format: integer): $GLAttachment
public "getMemorySize"(): long
public static "makeWrapped"(device: $GLDevice$Type, width: integer, height: integer, sampleCount: integer, format: integer, renderbuffer: integer): $GLAttachment
public "getRenderbufferID"(): integer
public static "makeColor"(device: $GLDevice$Type, width: integer, height: integer, sampleCount: integer, format: integer): $GLAttachment
public static "create"<T extends $RefCounted>(that: T): T
public static "create"<T extends $RefCounted>(sp: T, that: T): T
public static "move"<T extends $RefCounted>(sp: T): T
public static "move"<T extends $RefCounted>(sp: T, that: T): T
get "format"(): integer
get "backendFormat"(): $BackendFormat
get "memorySize"(): long
get "renderbufferID"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLAttachment$Type = ($GLAttachment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLAttachment_ = $GLAttachment$Type;
}}
declare module "packages/icyllis/arc3d/compiler/$CompileOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CompileOptions {
 "mEntryPointName": string
 "mForceHighPrecision": boolean
 "mUsePrecisionQualifiers": boolean
 "mNoShortCircuit": boolean
 "mMinifyNames": boolean
 "mMinifyCode": boolean
 "mOptimizationLevel": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompileOptions$Type = ($CompileOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompileOptions_ = $CompileOptions$Type;
}}
declare module "packages/icyllis/arc3d/vulkan/$VulkanImageInfo" {
import {$VulkanAllocation, $VulkanAllocation$Type} from "packages/icyllis/arc3d/vulkan/$VulkanAllocation"

export class $VulkanImageInfo extends $VulkanAllocation {
 "mImage": long
 "mImageLayout": integer
 "mImageTiling": integer
 "mFormat": integer
 "mSharingMode": integer
 "mImageUsageFlags": integer
 "mSampleCount": integer
 "mLevelCount": integer
 "mCurrentQueueFamily": integer
 "mMemoryHandle": integer
 "mProtected": boolean
static readonly "VISIBLE_FLAG": integer
static readonly "COHERENT_FLAG": integer
static readonly "LAZILY_ALLOCATED_FLAG": integer
 "mMemory": long
 "mOffset": long
 "mSize": long
 "mMemoryFlags": integer
 "mAllocation": long

constructor()

public "equals"(o: any): boolean
public "hashCode"(): integer
public "set"(info: $VulkanImageInfo$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VulkanImageInfo$Type = ($VulkanImageInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VulkanImageInfo_ = $VulkanImageInfo$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ManagedResource" {
import {$GpuDevice, $GpuDevice$Type} from "packages/icyllis/arc3d/engine/$GpuDevice"
import {$RefCnt, $RefCnt$Type} from "packages/icyllis/arc3d/core/$RefCnt"

export class $ManagedResource extends $RefCnt {

constructor(device: $GpuDevice$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ManagedResource$Type = ($ManagedResource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ManagedResource_ = $ManagedResource$Type;
}}
declare module "packages/icyllis/arc3d/core/$Strike" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Strike {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Strike$Type = ($Strike);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Strike_ = $Strike$Type;
}}
declare module "packages/icyllis/arc3d/core/$Size" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Size extends $Annotation {

 "value"(): long
 "min"(): long
 "max"(): long
 "multiple"(): long
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Size {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Size$Type = ($Size);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Size_ = $Size$Type;
}}
declare module "packages/icyllis/arc3d/core/$ColorSpace$Named" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ColorSpace$Named extends $Enum<($ColorSpace$Named)> {
static readonly "SRGB": $ColorSpace$Named
static readonly "LINEAR_SRGB": $ColorSpace$Named
static readonly "EXTENDED_SRGB": $ColorSpace$Named
static readonly "LINEAR_EXTENDED_SRGB": $ColorSpace$Named
static readonly "BT709": $ColorSpace$Named
static readonly "BT2020": $ColorSpace$Named
static readonly "DCI_P3": $ColorSpace$Named
static readonly "DISPLAY_P3": $ColorSpace$Named
static readonly "NTSC_1953": $ColorSpace$Named
static readonly "SMPTE_C": $ColorSpace$Named
static readonly "ADOBE_RGB": $ColorSpace$Named
static readonly "PRO_PHOTO_RGB": $ColorSpace$Named
static readonly "ACES": $ColorSpace$Named
static readonly "ACESCG": $ColorSpace$Named
static readonly "CIE_XYZ": $ColorSpace$Named
static readonly "CIE_LAB": $ColorSpace$Named


public static "values"(): ($ColorSpace$Named)[]
public static "valueOf"(name: string): $ColorSpace$Named
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Named$Type = (("smpte_c") | ("cie_xyz") | ("srgb") | ("dci_p3") | ("ntsc_1953") | ("adobe_rgb") | ("bt709") | ("linear_extended_srgb") | ("extended_srgb") | ("linear_srgb") | ("bt2020") | ("pro_photo_rgb") | ("acescg") | ("cie_lab") | ("display_p3") | ("aces")) | ($ColorSpace$Named);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace$Named_ = $ColorSpace$Named$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TransferProcessor$ProgramImpl$EmitArgs" {
import {$TransferProcessor, $TransferProcessor$Type} from "packages/icyllis/arc3d/engine/$TransferProcessor"
import {$UniformHandler, $UniformHandler$Type} from "packages/icyllis/arc3d/engine/shading/$UniformHandler"
import {$XPFragmentBuilder, $XPFragmentBuilder$Type} from "packages/icyllis/arc3d/engine/shading/$XPFragmentBuilder"

export class $TransferProcessor$ProgramImpl$EmitArgs {
 "fragBuilder": $XPFragmentBuilder
 "uniformHandler": $UniformHandler
 "xferProc": $TransferProcessor
 "inputColor": string
 "inputCoverage": string
 "outputPrimary": string
 "outputSecondary": string

constructor(fragBuilder: $XPFragmentBuilder$Type, uniformHandler: $UniformHandler$Type, xferProc: $TransferProcessor$Type, inputColor: string, inputCoverage: string, outputPrimary: string, outputSecondary: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferProcessor$ProgramImpl$EmitArgs$Type = ($TransferProcessor$ProgramImpl$EmitArgs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferProcessor$ProgramImpl$EmitArgs_ = $TransferProcessor$ProgramImpl$EmitArgs$Type;
}}
declare module "packages/icyllis/arc3d/core/$MathUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MathUtil {
static readonly "PI": float
static readonly "PI2": float
static readonly "PI3": float
static readonly "PI4": float
static readonly "PI_O_2": float
static readonly "PI_O_3": float
static readonly "PI_O_4": float
static readonly "PI_O_6": float
static readonly "EPS": float
static readonly "INV_EPS": float
static readonly "DEG_TO_RAD": float
static readonly "RAD_TO_DEG": float
static readonly "SQRT2": float
static readonly "INV_SQRT2": float


public static "sin"(a: float): float
public static "cos"(a: float): float
public static "tan"(a: float): float
public static "atan2"(a: float, b: float): float
public static "sqrt"(f: float): float
public static "min"(a: float, b: float, c: float, d: float): float
public static "min"(a: double, b: double, c: double, d: double): double
public static "min"(a: double, b: double, c: double): double
public static "min"(a: float, b: float, c: float): float
public static "max"(a: double, b: double, c: double): double
public static "max"(a: double, b: double, c: double, d: double): double
public static "max"(a: float, b: float, c: float, d: float): float
public static "max"(a: float, b: float, c: float): float
public static "asin"(a: float): float
public static "acos"(a: float): float
public static "mix"(a: double, b: double, t: double): double
public static "mix"(a: float, b: float, t: float): float
public static "align8"(a: long): long
public static "align8"(a: integer): integer
public static "align4"(a: long): long
public static "align4"(a: integer): integer
public static "align2"(a: integer): integer
public static "align2"(a: long): long
public static "max3"(v: (float)[]): float
public static "isPow2"(a: long): boolean
public static "isPow2"(a: integer): boolean
public static "ceilPow2"(a: integer): integer
public static "ceilPow2"(a: long): long
public static "alignTo"(a: integer, alignment: integer): integer
public static "floorPow2"(a: long): long
public static "floorPow2"(a: integer): integer
public static "clamp"(x: float, min: float, max: float): float
public static "clamp"(x: long, min: long, max: long): long
public static "clamp"(x: double, min: double, max: double): double
public static "clamp"(x: integer, min: integer, max: integer): integer
public static "isApproxZero"(a: float, b: float, c: float, d: float): boolean
public static "isApproxZero"(a: float, b: float, c: float): boolean
public static "isApproxZero"(a: float): boolean
public static "isApproxZero"(a: float, b: float): boolean
public static "isApproxEqual"(a: float, b: float, c: float): boolean
public static "isApproxEqual"(a: float, b: float): boolean
public static "isApproxEqual"(a: float, b: float, c: float, d: float, e: float): boolean
public static "isApproxEqual"(a: float, b: float, c: float, d: float): boolean
public static "alignUp"(a: integer, alignment: integer): integer
public static "pin"(x: float, min: float, max: float): float
public static "pin"(x: double, min: double, max: double): double
public static "min3"(v: (float)[]): float
public static "median"(a: double, b: double, c: double): double
public static "median"(a: float, b: float, c: float): float
public static "isAlign8"(a: integer): boolean
public static "isAlign8"(a: long): boolean
public static "ceilLog16"(v: float): integer
public static "alignDown"(a: integer, alignment: integer): integer
public static "ceilLog4"(v: float): integer
public static "isAlign4"(a: long): boolean
public static "isAlign4"(a: integer): boolean
public static "ceilLog2"(a: long): integer
public static "ceilLog2"(a: integer): integer
public static "ceilLog2"(v: float): integer
public static "isAlign2"(a: long): boolean
public static "isAlign2"(a: integer): boolean
public static "floorLog2"(a: long): integer
public static "floorLog2"(a: integer): integer
public static "biLerp"(q00: double, q10: double, q01: double, q11: double, tx: double, ty: double): double
public static "biLerp"(q00: float, q10: float, q01: float, q11: float, tx: float, ty: float): float
public static "triLerp"(c000: float, c100: float, c010: float, c110: float, c001: float, c101: float, c011: float, c111: float, tx: float, ty: float, tz: float): float
public static "triLerp"(c000: double, c100: double, c010: double, c110: double, c001: double, c101: double, c011: double, c111: double, tx: double, ty: double, tz: double): double
public static "alignUpPad"(a: integer, alignment: integer): integer
public static "lerp"(a: double, b: double, t: double): double
public static "lerp"(a: float, b: float, t: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathUtil$Type = ($MathUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathUtil_ = $MathUtil$Type;
}}
declare module "packages/icyllis/arc3d/engine/$ShaderVar" {
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"

export class $ShaderVar {
static readonly "kNone_TypeModifier": byte
static readonly "kOut_TypeModifier": byte
static readonly "kIn_TypeModifier": byte
static readonly "kInOut_TypeModifier": byte
static readonly "kUniform_TypeModifier": byte
static readonly "kNonArray": integer

constructor()
constructor(name: string, type: byte, typeModifier: byte, arrayCount: integer, layoutQualifier: string, extraModifier: string)
constructor(name: string, type: byte)
constructor(name: string, type: byte, arrayCount: integer)
constructor(name: string, type: byte, typeModifier: byte)
constructor(name: string, type: byte, typeModifier: byte, arrayCount: integer)

public "appendDecl"(out: $StringBuilder$Type): void
public "addLayoutQualifier"(layoutQualifier: string): void
public "getName"(): string
public "isArray"(): boolean
public "set"(name: string, type: byte): void
public "getType"(): byte
public "getTypeModifier"(): byte
public "getArrayCount"(): integer
public "setTypeModifier"(typeModifier: byte): void
public "addModifier"(modifier: string): void
get "name"(): string
get "array"(): boolean
get "type"(): byte
get "typeModifier"(): byte
get "arrayCount"(): integer
set "typeModifier"(value: byte)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShaderVar$Type = ($ShaderVar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShaderVar_ = $ShaderVar$Type;
}}
declare module "packages/icyllis/arc3d/compiler/spirv/$WordBuffer" {
import {$Writer, $Writer$Type} from "packages/icyllis/arc3d/compiler/spirv/$Writer"
import {$Context, $Context$Type} from "packages/icyllis/arc3d/compiler/$Context"

export class $WordBuffer implements $Writer {


public "clear"(): void
public "size"(): integer
public "elements"(): (integer)[]
public "writeString8"(context: $Context$Type, s: string): void
public "writeWords"(words: (integer)[], n: integer): void
public "writeWord"(word: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WordBuffer$Type = ($WordBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WordBuffer_ = $WordBuffer$Type;
}}
declare module "packages/icyllis/arc3d/engine/$PipelineStateCache$Stats" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PipelineStateCache$Stats {

constructor()

public "toString"(): string
public "incShaderCompilations"(): void
public "incNumInlineCompilationFailures"(): void
public "numInlineCompilationFailures"(): integer
public "numCompilationFailures"(): integer
public "incNumCompilationSuccesses"(): void
public "incNumCompilationFailures"(): void
public "numPreCompilationFailures"(): integer
public "incNumPreCompilationFailures"(): void
public "incNumPartialCompilationSuccesses"(): void
public "numPartialCompilationSuccesses"(): integer
public "numCompilationSuccesses"(): integer
public "shaderCompilations"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PipelineStateCache$Stats$Type = ($PipelineStateCache$Stats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PipelineStateCache$Stats_ = $PipelineStateCache$Stats$Type;
}}
declare module "packages/icyllis/arc3d/core/$Canvas" {
import {$Paint, $Paint$Type} from "packages/icyllis/arc3d/core/$Paint"
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Image, $Image$Type} from "packages/icyllis/arc3d/core/$Image"
import {$RecordingContext, $RecordingContext$Type} from "packages/icyllis/arc3d/engine/$RecordingContext"
import {$Matrix4, $Matrix4$Type} from "packages/icyllis/arc3d/core/$Matrix4"
import {$BlendMode, $BlendMode$Type} from "packages/icyllis/arc3d/core/$BlendMode"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$Surface, $Surface$Type} from "packages/icyllis/arc3d/core/$Surface"
import {$ImageInfo, $ImageInfo$Type} from "packages/icyllis/arc3d/core/$ImageInfo"
import {$Color, $Color$Type} from "packages/icyllis/arc3d/core/$Color"
import {$ImageFilter, $ImageFilter$Type} from "packages/icyllis/arc3d/core/$ImageFilter"

export class $Canvas implements $AutoCloseable {
static readonly "INIT_WITH_PREVIOUS_SAVE_LAYER_FLAG": integer
static readonly "F16_COLOR_TYPE_SAVE_LAYER_FLAG": integer

constructor()
constructor(width: integer, height: integer)

public "getRecordingContext"(): $RecordingContext
public "clear"(r: float, g: float, b: float, a: float): void
public "clear"(color: $Color$Type): void
public "clear"(color: integer): void
public "scale"(sx: float, sy: float, px: float, py: float): void
public "scale"(sx: float, sy: float): void
public "concat"(matrix: $Matrix4$Type): void
public "close"(): void
public "save"(): integer
public "rotate"(degrees: float): void
public "rotate"(degrees: float, px: float, py: float): void
public "drawRect"(r: $Rect2i$Type, paint: $Paint$Type): void
public "drawRect"(r: $Rect2f$Type, paint: $Paint$Type): void
public "drawRect"(left: float, top: float, right: float, bottom: float, paint: $Paint$Type): void
public "makeSurface"(info: $ImageInfo$Type): $Surface
public "getLocalToDevice"(storage: $Matrix4$Type): void
public "translate"(dx: float, dy: float): void
public "drawImage"(image: $Image$Type, left: float, top: float, paint: $Paint$Type): void
public "drawImage"(image: $Image$Type, srcLeft: float, srcTop: float, srcRight: float, srcBottom: float, dstLeft: float, dstTop: float, dstRight: float, dstBottom: float, paint: $Paint$Type): void
public "drawImage"(image: $Image$Type, src: $Rect2i$Type, dst: $Rect2i$Type, paint: $Paint$Type): void
public "drawImage"(image: $Image$Type, src: $Rect2i$Type, dst: $Rect2f$Type, paint: $Paint$Type): void
public "restore"(): void
public "setMarker"(name: string): void
public "drawLine"(x0: float, y0: float, x1: float, y1: float, size: float, paint: $Paint$Type): void
public "drawRoundRect"(left: float, top: float, right: float, bottom: float, rUL: float, rUR: float, rLR: float, rLL: float, paint: $Paint$Type): void
public "drawRoundRect"(rect: $Rect2f$Type, rUL: float, rUR: float, rLR: float, rLL: float, paint: $Paint$Type): void
public "drawRoundRect"(left: float, top: float, right: float, bottom: float, radius: float, paint: $Paint$Type): void
public "drawRoundRect"(rect: $Rect2f$Type, radius: float, paint: $Paint$Type): void
public "drawArc"(cx: float, cy: float, radius: float, startAngle: float, sweepAngle: float, paint: $Paint$Type): void
public "getSurface"(): $Surface
public "clipRect"(left: integer, top: integer, right: integer, bottom: integer): void
public "clipRect"(rect: $Rect2i$Type): void
public "clipRect"(rect: $Rect2f$Type, doAA: boolean): void
public "clipRect"(rect: $Rect2f$Type): void
public "clipRect"(left: float, top: float, right: float, bottom: float): void
public "clipRect"(left: float, top: float, right: float, bottom: float, doAA: boolean): void
public "quickReject"(rect: $Rect2f$Type): boolean
public "quickReject"(left: float, top: float, right: float, bottom: float): boolean
public "saveLayer"(left: float, top: float, right: float, bottom: float, paint: $Paint$Type): integer
public "saveLayer"(bounds: $Rect2f$Type, paint: $Paint$Type, backdrop: $ImageFilter$Type, saveLayerFlags: integer): integer
public "saveLayer"(left: float, top: float, right: float, bottom: float, paint: $Paint$Type, backdrop: $ImageFilter$Type, saveLayerFlags: integer): integer
public "saveLayer"(bounds: $Rect2f$Type, paint: $Paint$Type): integer
public "restoreToCount"(saveCount: integer): void
public "drawTriangle"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, paint: $Paint$Type): void
public "drawPaint"(paint: $Paint$Type): void
public "getSaveCount"(): integer
public "drawColor"(r: float, g: float, b: float, a: float): void
public "drawColor"(color: integer, mode: $BlendMode$Type): void
public "drawColor"(color: integer): void
public "drawColor"(color: $Color$Type): void
public "drawColor"(color: $Color$Type, mode: $BlendMode$Type): void
public "drawColor"(r: float, g: float, b: float, a: float, mode: $BlendMode$Type): void
public "drawRoundImage"(image: $Image$Type, left: float, top: float, radius: float, paint: $Paint$Type): void
public "drawCircle"(cx: float, cy: float, radius: float, paint: $Paint$Type): void
public "drawBezier"(x0: float, y0: float, x1: float, y1: float, x2: float, y2: float, paint: $Paint$Type): void
public "isClipRect"(): boolean
public "isClipEmpty"(): boolean
public "findMarker"(name: string, out: $Matrix4$Type): boolean
public "saveLayerAlpha"(bounds: $Rect2f$Type, alpha: integer): integer
public "saveLayerAlpha"(left: float, top: float, right: float, bottom: float, alpha: integer): integer
public "getBaseLayerWidth"(): integer
public "getBaseLayerHeight"(): integer
public "resetMatrix"(): void
public "setMatrix"(matrix: $Matrix4$Type): void
public "getLocalClipBounds"(bounds: $Rect2f$Type): boolean
public "getImageInfo"(): $ImageInfo
public "getDeviceClipBounds"(bounds: $Rect2i$Type): boolean
get "recordingContext"(): $RecordingContext
set "marker"(value: string)
get "surface"(): $Surface
get "saveCount"(): integer
get "clipEmpty"(): boolean
get "baseLayerWidth"(): integer
get "baseLayerHeight"(): integer
set "matrix"(value: $Matrix4$Type)
get "imageInfo"(): $ImageInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Canvas$Type = ($Canvas);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Canvas_ = $Canvas$Type;
}}
declare module "packages/icyllis/arc3d/core/$Rect2ic" {
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Rect2fc, $Rect2fc$Type} from "packages/icyllis/arc3d/core/$Rect2fc"

export interface $Rect2ic {

 "store"(arg0: $Rect2f$Type): void
 "store"(arg0: $Rect2i$Type): void
 "isEmpty"(): boolean
 "x"(): integer
 "contains"(arg0: $Rect2fc$Type): boolean
 "contains"(arg0: integer, arg1: integer): boolean
 "contains"(arg0: float, arg1: float): boolean
 "contains"(arg0: $Rect2ic$Type): boolean
 "top"(): integer
 "y"(): integer
 "left"(): integer
 "right"(): integer
 "bottom"(): integer
 "width"(): integer
 "intersects"(arg0: $Rect2ic$Type): boolean
 "height"(): integer
 "isSorted"(): boolean
}

export namespace $Rect2ic {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rect2ic$Type = ($Rect2ic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rect2ic_ = $Rect2ic$Type;
}}
declare module "packages/icyllis/arc3d/engine/$GpuDevice$Stats" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GpuDevice$Stats {

constructor()

public "toString"(): string
public "reset"(): void
public "numDraws"(): long
public "incNumSubmitToGpus"(): void
public "numRenderPasses"(): long
public "numTextureCreates"(): long
public "numFailedDraws"(): long
public "numSubmitToGpus"(): long
public "numTextureUploads"(): long
public "incTextureUploads"(): void
public "incTextureCreates"(): void
public "incRenderPasses"(): void
public "incNumDraws"(increment: integer): void
public "incNumDraws"(): void
public "incNumFailedDraws"(): void
public "incNumScratchTexturesReused"(): void
public "incStencilAttachmentCreates"(): void
public "incMSAAAttachmentCreates"(): void
public "numScratchMSAAAttachmentsReused"(): long
public "msaaAttachmentCreates"(): long
public "numTransfersFromSurface"(): long
public "numTransfersToTexture"(): long
public "incTransfersFromSurface"(): void
public "numScratchTexturesReused"(): long
public "numStencilAttachmentCreates"(): long
public "incTransfersToTexture"(): void
public "incNumScratchMSAAAttachmentsReused"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpuDevice$Stats$Type = ($GpuDevice$Stats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpuDevice$Stats_ = $GpuDevice$Stats$Type;
}}
declare module "packages/icyllis/arc3d/core/$Rect2fc" {
import {$Rect2i, $Rect2i$Type} from "packages/icyllis/arc3d/core/$Rect2i"
import {$Rect2f, $Rect2f$Type} from "packages/icyllis/arc3d/core/$Rect2f"
import {$Rect2ic, $Rect2ic$Type} from "packages/icyllis/arc3d/core/$Rect2ic"

export interface $Rect2fc {

 "store"(arg0: $Rect2f$Type): void
 "isEmpty"(): boolean
 "x"(): float
 "contains"(arg0: $Rect2fc$Type): boolean
 "contains"(arg0: float, arg1: float): boolean
 "contains"(arg0: $Rect2ic$Type): boolean
 "top"(): float
 "y"(): float
 "isFinite"(): boolean
 "left"(): float
 "right"(): float
 "round"(arg0: $Rect2f$Type): void
 "round"(arg0: $Rect2i$Type): void
 "bottom"(): float
 "width"(): float
 "intersects"(arg0: $Rect2ic$Type): boolean
 "intersects"(arg0: $Rect2fc$Type): boolean
 "height"(): float
 "isSorted"(): boolean
 "roundIn"(arg0: $Rect2i$Type): void
 "roundIn"(arg0: $Rect2f$Type): void
 "roundOut"(arg0: $Rect2i$Type): void
 "roundOut"(arg0: $Rect2f$Type): void
 "centerX"(): float
 "centerY"(): float
}

export namespace $Rect2fc {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rect2fc$Type = ($Rect2fc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rect2fc_ = $Rect2fc$Type;
}}
declare module "packages/icyllis/arc3d/engine/$TextureResolveTask" {
import {$OpFlushState, $OpFlushState$Type} from "packages/icyllis/arc3d/engine/$OpFlushState"
import {$RenderTask, $RenderTask$Type} from "packages/icyllis/arc3d/engine/$RenderTask"
import {$RenderTaskManager, $RenderTaskManager$Type} from "packages/icyllis/arc3d/engine/$RenderTaskManager"
import {$TextureProxy, $TextureProxy$Type} from "packages/icyllis/arc3d/engine/$TextureProxy"

export class $TextureResolveTask extends $RenderTask {
static readonly "RESOLVE_FLAG_MSAA": integer
static readonly "RESOLVE_FLAG_MIPMAPS": integer

constructor(taskManager: $RenderTaskManager$Type)

public "execute"(flushState: $OpFlushState$Type): boolean
public "addTexture"(proxy: $TextureProxy$Type, resolveFlags: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureResolveTask$Type = ($TextureResolveTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureResolveTask_ = $TextureResolveTask$Type;
}}
declare module "packages/icyllis/arc3d/engine/$CpuBufferPool" {
import {$CpuBuffer, $CpuBuffer$Type} from "packages/icyllis/arc3d/engine/$CpuBuffer"

export class $CpuBufferPool {

constructor(maxCount: integer)

public "makeBuffer"(size: integer): $CpuBuffer
public "releaseAll"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CpuBufferPool$Type = ($CpuBufferPool);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CpuBufferPool_ = $CpuBufferPool$Type;
}}
declare module "packages/icyllis/arc3d/engine/$SurfaceProxy$LazyInstantiateCallback" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/icyllis/arc3d/engine/$ResourceProvider"
import {$BackendFormat, $BackendFormat$Type} from "packages/icyllis/arc3d/engine/$BackendFormat"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$SurfaceProxy$LazyCallbackResult, $SurfaceProxy$LazyCallbackResult$Type} from "packages/icyllis/arc3d/engine/$SurfaceProxy$LazyCallbackResult"

export interface $SurfaceProxy$LazyInstantiateCallback extends $AutoCloseable {

 "close"(): void
 "onLazyInstantiate"(arg0: $ResourceProvider$Type, arg1: $BackendFormat$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: string): $SurfaceProxy$LazyCallbackResult

(): void
}

export namespace $SurfaceProxy$LazyInstantiateCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceProxy$LazyInstantiateCallback$Type = ($SurfaceProxy$LazyInstantiateCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceProxy$LazyInstantiateCallback_ = $SurfaceProxy$LazyInstantiateCallback$Type;
}}
declare module "packages/icyllis/arc3d/engine/$BlendInfo" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $BlendInfo extends $Record {
static readonly "SRC": $BlendInfo

constructor(equation: integer, srcFactor: integer, dstFactor: integer, constantR: float, constantG: float, constantB: float, constantA: float, writeColor: boolean)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "equation"(): integer
public "constantR"(): float
public "constantG"(): float
public "constantB"(): float
public "srcFactor"(): integer
public "constantA"(): float
public "dstFactor"(): integer
public "writeColor"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlendInfo$Type = ($BlendInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlendInfo_ = $BlendInfo$Type;
}}
