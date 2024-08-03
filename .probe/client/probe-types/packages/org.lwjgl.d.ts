declare module "packages/org/lwjgl/system/windows/$POINT" {
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"
import {$POINT$Buffer, $POINT$Buffer$Type} from "packages/org/lwjgl/system/windows/$POINT$Buffer"

export class $POINT extends $Struct implements $NativeResource {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "X": integer
static readonly "Y": integer

constructor(arg0: $ByteBuffer$Type)

public "x"(arg0: integer): $POINT
public "x"(): integer
public "set"(arg0: $POINT$Type): $POINT
public "set"(arg0: integer, arg1: integer): $POINT
public static "create"(): $POINT
public static "create"(arg0: long): $POINT
public static "create"(arg0: integer): $POINT$Buffer
public static "create"(arg0: long, arg1: integer): $POINT$Buffer
public "y"(): integer
public "y"(arg0: integer): $POINT
public static "ny"(arg0: long, arg1: integer): void
public static "ny"(arg0: long): integer
public static "nx"(arg0: long): integer
public static "nx"(arg0: long, arg1: integer): void
public static "malloc"(): $POINT
public static "malloc"(arg0: integer, arg1: $MemoryStack$Type): $POINT$Buffer
public static "malloc"(arg0: $MemoryStack$Type): $POINT
public static "malloc"(arg0: integer): $POINT$Buffer
public "sizeof"(): integer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer): $POINT$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer, arg1: $MemoryStack$Type): $POINT$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(): $POINT
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: $MemoryStack$Type): $POINT
public static "calloc"(arg0: $MemoryStack$Type): $POINT
public static "calloc"(arg0: integer, arg1: $MemoryStack$Type): $POINT$Buffer
public static "calloc"(arg0: integer): $POINT$Buffer
public static "calloc"(): $POINT
public static "createSafe"(arg0: long): $POINT
public static "createSafe"(arg0: long, arg1: integer): $POINT$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer): $POINT$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: $MemoryStack$Type): $POINT
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer, arg1: $MemoryStack$Type): $POINT$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(): $POINT
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $POINT$Type = ($POINT);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $POINT_ = $POINT$Type;
}}
declare module "packages/org/lwjgl/stb/$STBRPRect" {
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$STBRPRect$Buffer, $STBRPRect$Buffer$Type} from "packages/org/lwjgl/stb/$STBRPRect$Buffer"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $STBRPRect extends $Struct implements $NativeResource {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "ID": integer
static readonly "W": integer
static readonly "H": integer
static readonly "X": integer
static readonly "Y": integer
static readonly "WAS_PACKED": integer

constructor(arg0: $ByteBuffer$Type)

public "x"(): integer
public "x"(arg0: integer): $STBRPRect
public "h"(arg0: integer): $STBRPRect
public "h"(): integer
public "id"(): integer
public "id"(arg0: integer): $STBRPRect
public static "nid"(arg0: long): integer
public static "nid"(arg0: long, arg1: integer): void
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean): $STBRPRect
public "set"(arg0: $STBRPRect$Type): $STBRPRect
public "w"(arg0: integer): $STBRPRect
public "w"(): integer
public static "create"(arg0: long): $STBRPRect
public static "create"(): $STBRPRect
public static "create"(arg0: integer): $STBRPRect$Buffer
public static "create"(arg0: long, arg1: integer): $STBRPRect$Buffer
public "y"(arg0: integer): $STBRPRect
public "y"(): integer
public static "nh"(arg0: long): integer
public static "nh"(arg0: long, arg1: integer): void
public static "nw"(arg0: long): integer
public static "nw"(arg0: long, arg1: integer): void
public "was_packed"(): boolean
public "was_packed"(arg0: boolean): $STBRPRect
public static "nwas_packed"(arg0: long): integer
public static "nwas_packed"(arg0: long, arg1: integer): void
public static "ny"(arg0: long, arg1: integer): void
public static "ny"(arg0: long): integer
public static "nx"(arg0: long): integer
public static "nx"(arg0: long, arg1: integer): void
public static "malloc"(arg0: $MemoryStack$Type): $STBRPRect
public static "malloc"(): $STBRPRect
public static "malloc"(arg0: integer, arg1: $MemoryStack$Type): $STBRPRect$Buffer
public static "malloc"(arg0: integer): $STBRPRect$Buffer
public "sizeof"(): integer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer): $STBRPRect$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(): $STBRPRect
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer, arg1: $MemoryStack$Type): $STBRPRect$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: $MemoryStack$Type): $STBRPRect
public static "calloc"(arg0: integer, arg1: $MemoryStack$Type): $STBRPRect$Buffer
public static "calloc"(arg0: integer): $STBRPRect$Buffer
public static "calloc"(): $STBRPRect
public static "calloc"(arg0: $MemoryStack$Type): $STBRPRect
public static "createSafe"(arg0: long): $STBRPRect
public static "createSafe"(arg0: long, arg1: integer): $STBRPRect$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: $MemoryStack$Type): $STBRPRect
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer, arg1: $MemoryStack$Type): $STBRPRect$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer): $STBRPRect$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(): $STBRPRect
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $STBRPRect$Type = ($STBRPRect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $STBRPRect_ = $STBRPRect$Type;
}}
declare module "packages/org/lwjgl/opengl/$GLDebugMessageCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLDebugMessageCallbackI extends $CallbackI {

 "invoke"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long, arg6: long): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long, arg6: long): void
}

export namespace $GLDebugMessageCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLDebugMessageCallbackI$Type = ($GLDebugMessageCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLDebugMessageCallbackI_ = $GLDebugMessageCallbackI$Type;
}}
declare module "packages/org/lwjgl/system/windows/$MSG" {
import {$POINT, $POINT$Type} from "packages/org/lwjgl/system/windows/$POINT"
import {$MSG$Buffer, $MSG$Buffer$Type} from "packages/org/lwjgl/system/windows/$MSG$Buffer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $MSG extends $Struct implements $NativeResource {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "HWND": integer
static readonly "MESSAGE": integer
static readonly "WPARAM": integer
static readonly "LPARAM": integer
static readonly "TIME": integer
static readonly "PT": integer

constructor(arg0: $ByteBuffer$Type)

public "set"(arg0: $MSG$Type): $MSG
public "set"(arg0: long, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: $POINT$Type): $MSG
public "message"(arg0: integer): $MSG
public "message"(): integer
public "pt"(): $POINT
public "pt"(arg0: $POINT$Type): $MSG
public "pt"(arg0: $Consumer$Type<($POINT$Type)>): $MSG
public static "create"(): $MSG
public static "create"(arg0: long, arg1: integer): $MSG$Buffer
public static "create"(arg0: integer): $MSG$Buffer
public static "create"(arg0: long): $MSG
public "time"(): integer
public "time"(arg0: integer): $MSG
public "hwnd"(): long
public "hwnd"(arg0: long): $MSG
public static "malloc"(arg0: integer, arg1: $MemoryStack$Type): $MSG$Buffer
public static "malloc"(arg0: $MemoryStack$Type): $MSG
public static "malloc"(arg0: integer): $MSG$Buffer
public static "malloc"(): $MSG
public "sizeof"(): integer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer): $MSG$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer, arg1: $MemoryStack$Type): $MSG$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: $MemoryStack$Type): $MSG
/**
 * 
 * @deprecated
 */
public static "mallocStack"(): $MSG
public static "calloc"(arg0: $MemoryStack$Type): $MSG
public static "calloc"(): $MSG
public static "calloc"(arg0: integer, arg1: $MemoryStack$Type): $MSG$Buffer
public static "calloc"(arg0: integer): $MSG$Buffer
public static "createSafe"(arg0: long, arg1: integer): $MSG$Buffer
public static "createSafe"(arg0: long): $MSG
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: $MemoryStack$Type): $MSG
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer, arg1: $MemoryStack$Type): $MSG$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(): $MSG
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer): $MSG$Buffer
public "wParam"(): long
public "wParam"(arg0: long): $MSG
public static "nlParam"(arg0: long, arg1: long): void
public static "nlParam"(arg0: long): long
public static "nmessage"(arg0: long): integer
public static "nmessage"(arg0: long, arg1: integer): void
public static "npt"(arg0: long): $POINT
public static "npt"(arg0: long, arg1: $POINT$Type): void
public "lParam"(arg0: long): $MSG
public "lParam"(): long
public static "nhwnd"(arg0: long, arg1: long): void
public static "nhwnd"(arg0: long): long
public static "ntime"(arg0: long, arg1: integer): void
public static "ntime"(arg0: long): integer
public static "nwParam"(arg0: long, arg1: long): void
public static "nwParam"(arg0: long): long
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MSG$Type = ($MSG);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MSG_ = $MSG$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWCursorPosCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWCursorPosCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: double, arg2: double): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: double, arg2: double): void
}

export namespace $GLFWCursorPosCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWCursorPosCallbackI$Type = ($GLFWCursorPosCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWCursorPosCallbackI_ = $GLFWCursorPosCallbackI$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL40C" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$GL33C, $GL33C$Type} from "packages/org/lwjgl/opengl/$GL33C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL40C extends $GL33C {
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


public static "glBlendEquationSeparatei"(arg0: integer, arg1: integer, arg2: integer): void
public static "glUniform2dv"(arg0: integer, arg1: (double)[]): void
public static "glUniform2dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glUniform4dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glUniform4dv"(arg0: integer, arg1: (double)[]): void
public static "glBlendFunci"(arg0: integer, arg1: integer, arg2: integer): void
public static "glUniformMatrix4dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glUniformMatrix4dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glUniform1dv"(arg0: integer, arg1: (double)[]): void
public static "glUniform1dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glMinSampleShading"(arg0: float): void
public static "glGetUniformdv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glGetUniformdv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glBlendEquationi"(arg0: integer, arg1: integer): void
public static "glUniform1d"(arg0: integer, arg1: double): void
public static "glPatchParameteri"(arg0: integer, arg1: integer): void
public static "glUniform4d"(arg0: integer, arg1: double, arg2: double, arg3: double, arg4: double): void
public static "glUniformMatrix3dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glUniformMatrix3dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glUniform2d"(arg0: integer, arg1: double, arg2: double): void
public static "glUniform3dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glUniform3dv"(arg0: integer, arg1: (double)[]): void
public static "glUniformMatrix2dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glUniformMatrix2dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glPatchParameterfv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glPatchParameterfv"(arg0: integer, arg1: (float)[]): void
public static "glUniform3d"(arg0: integer, arg1: double, arg2: double, arg3: double): void
public static "glUniformSubroutinesuiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniformSubroutinesuiv"(arg0: integer, arg1: (integer)[]): void
public static "glBeginQueryIndexed"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetQueryIndexediv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetQueryIndexediv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glUniformMatrix2x4dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glUniformMatrix2x4dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glGetActiveSubroutineName"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $ByteBuffer$Type): void
public static "glGetActiveSubroutineName"(arg0: integer, arg1: integer, arg2: integer): string
public static "glGetActiveSubroutineName"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[], arg4: $ByteBuffer$Type): void
public static "glGetActiveSubroutineName"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): string
public static "glPauseTransformFeedback"(): void
public static "glGenTransformFeedbacks"(arg0: $IntBuffer$Type): void
public static "glGenTransformFeedbacks"(): integer
public static "glGenTransformFeedbacks"(arg0: (integer)[]): void
public static "glResumeTransformFeedback"(): void
public static "glUniformMatrix3x4dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glUniformMatrix3x4dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glDrawTransformFeedback"(arg0: integer, arg1: integer): void
public static "glDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: long): void
public static "glDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetProgramStageiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetProgramStageiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetSubroutineUniformLocation"(arg0: integer, arg1: integer, arg2: charseq): integer
public static "glGetSubroutineUniformLocation"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): integer
public static "glBlendFuncSeparatei"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetActiveSubroutineUniformName"(arg0: integer, arg1: integer, arg2: integer): string
public static "glGetActiveSubroutineUniformName"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): string
public static "glGetActiveSubroutineUniformName"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[], arg4: $ByteBuffer$Type): void
public static "glGetActiveSubroutineUniformName"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $ByteBuffer$Type): void
public static "glGetSubroutineIndex"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): integer
public static "glGetSubroutineIndex"(arg0: integer, arg1: integer, arg2: charseq): integer
public static "glGetActiveSubroutineUniformiv"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glGetActiveSubroutineUniformiv"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glDrawTransformFeedbackStream"(arg0: integer, arg1: integer, arg2: integer): void
public static "glUniformMatrix4x2dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glUniformMatrix4x2dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glGetUniformSubroutineuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetUniformSubroutineuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glBindTransformFeedback"(arg0: integer, arg1: integer): void
public static "glUniformMatrix2x3dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glUniformMatrix2x3dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glDrawArraysIndirect"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glDrawArraysIndirect"(arg0: integer, arg1: (integer)[]): void
public static "glDrawArraysIndirect"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glDrawArraysIndirect"(arg0: integer, arg1: long): void
public static "glUniformMatrix4x3dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glUniformMatrix4x3dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glDeleteTransformFeedbacks"(arg0: integer): void
public static "glDeleteTransformFeedbacks"(arg0: (integer)[]): void
public static "glDeleteTransformFeedbacks"(arg0: $IntBuffer$Type): void
public static "glIsTransformFeedback"(arg0: integer): boolean
public static "glUniformMatrix3x2dv"(arg0: integer, arg1: boolean, arg2: (double)[]): void
public static "glUniformMatrix3x2dv"(arg0: integer, arg1: boolean, arg2: $DoubleBuffer$Type): void
public static "glEndQueryIndexed"(arg0: integer, arg1: integer): void
public static "nglUniformMatrix4x3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGetActiveSubroutineName"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long, arg5: long): void
public static "nglGetSubroutineUniformLocation"(arg0: integer, arg1: integer, arg2: long): integer
public static "nglUniformMatrix3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglPatchParameterfv"(arg0: integer, arg1: long): void
public static "nglUniformSubroutinesuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetUniformSubroutineui"(arg0: integer, arg1: integer): integer
public static "nglDrawArraysIndirect"(arg0: integer, arg1: long): void
public static "nglUniformMatrix2x4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGenTransformFeedbacks"(arg0: integer, arg1: long): void
public static "nglGetProgramStageiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetActiveSubroutineUniformName"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long, arg5: long): void
public static "glUniformSubroutinesui"(arg0: integer, arg1: integer): void
public static "nglDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniformMatrix3x2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix4x2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGetSubroutineIndex"(arg0: integer, arg1: integer, arg2: long): integer
public static "nglGetQueryIndexediv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetActiveSubroutineUniformi"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public static "nglUniformMatrix4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix2x3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGetUniformSubroutineuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniformMatrix2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglDeleteTransformFeedbacks"(arg0: integer, arg1: long): void
public static "nglGetActiveSubroutineUniformiv"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglUniformMatrix3x4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniform4dv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetQueryIndexedi"(arg0: integer, arg1: integer, arg2: integer): integer
public static "glGetProgramStagei"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglUniform3dv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform1dv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform2dv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetUniformd"(arg0: integer, arg1: integer): double
public static "nglGetUniformdv"(arg0: integer, arg1: integer, arg2: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL40C$Type = ($GL40C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL40C_ = $GL40C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL41C" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$GL40C, $GL40C$Type} from "packages/org/lwjgl/opengl/$GL40C"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL41C extends $GL40C {
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


public static "glBindProgramPipeline"(arg0: integer): void
public static "glProgramUniform1ui"(arg0: integer, arg1: integer, arg2: integer): void
public static "glProgramUniform2ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glProgramUniform4ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glProgramUniform3ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glDeleteProgramPipelines"(arg0: (integer)[]): void
public static "glDeleteProgramPipelines"(arg0: $IntBuffer$Type): void
public static "glDeleteProgramPipelines"(arg0: integer): void
public static "glReleaseShaderCompiler"(): void
public static "glGenProgramPipelines"(arg0: (integer)[]): void
public static "glGenProgramPipelines"(arg0: $IntBuffer$Type): void
public static "glGenProgramPipelines"(): integer
public static "glGetShaderPrecisionFormat"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): integer
public static "glGetShaderPrecisionFormat"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: (integer)[]): void
public static "glGetShaderPrecisionFormat"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type): void
public static "glIsProgramPipeline"(arg0: integer): boolean
public static "glActiveShaderProgram"(arg0: integer, arg1: integer): void
public static "glCreateShaderProgramv"(arg0: integer, arg1: charseq): integer
public static "glCreateShaderProgramv"(arg0: integer, ...arg1: (charseq)[]): integer
public static "glCreateShaderProgramv"(arg0: integer, arg1: $PointerBuffer$Type): integer
public static "glProgramParameteri"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetProgramPipelineiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetProgramPipelineiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform4f"(arg0: integer, arg1: integer, arg2: float, arg3: float, arg4: float, arg5: float): void
public static "glProgramUniform3f"(arg0: integer, arg1: integer, arg2: float, arg3: float, arg4: float): void
public static "glProgramUniform1d"(arg0: integer, arg1: integer, arg2: double): void
public static "glProgramUniform2d"(arg0: integer, arg1: integer, arg2: double, arg3: double): void
public static "glProgramBinary"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glProgramUniform4d"(arg0: integer, arg1: integer, arg2: double, arg3: double, arg4: double, arg5: double): void
public static "glProgramUniform1f"(arg0: integer, arg1: integer, arg2: float): void
public static "glProgramUniform3d"(arg0: integer, arg1: integer, arg2: double, arg3: double, arg4: double): void
public static "glProgramUniform2i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glShaderBinary"(arg0: $IntBuffer$Type, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glShaderBinary"(arg0: (integer)[], arg1: integer, arg2: $ByteBuffer$Type): void
public static "glProgramUniform4i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glProgramUniform1i"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetProgramBinary"(arg0: integer, arg1: (integer)[], arg2: (integer)[], arg3: $ByteBuffer$Type): void
public static "glGetProgramBinary"(arg0: integer, arg1: $IntBuffer$Type, arg2: $IntBuffer$Type, arg3: $ByteBuffer$Type): void
public static "glDepthRangef"(arg0: float, arg1: float): void
public static "glClearDepthf"(arg0: float): void
public static "glUseProgramStages"(arg0: integer, arg1: integer, arg2: integer): void
public static "glProgramUniform3i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glProgramUniform2f"(arg0: integer, arg1: integer, arg2: float, arg3: float): void
public static "glVertexAttribL3dv"(arg0: integer, arg1: (double)[]): void
public static "glVertexAttribL3dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glVertexAttribL2dv"(arg0: integer, arg1: (double)[]): void
public static "glVertexAttribL2dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glViewportArrayv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glViewportArrayv"(arg0: integer, arg1: (float)[]): void
public static "glScissorArrayv"(arg0: integer, arg1: (integer)[]): void
public static "glScissorArrayv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribL1d"(arg0: integer, arg1: double): void
public static "glViewportIndexedf"(arg0: integer, arg1: float, arg2: float, arg3: float, arg4: float): void
public static "glScissorIndexed"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glScissorIndexedv"(arg0: integer, arg1: (integer)[]): void
public static "glScissorIndexedv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribL4d"(arg0: integer, arg1: double, arg2: double, arg3: double, arg4: double): void
public static "glVertexAttribL1dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glVertexAttribL1dv"(arg0: integer, arg1: (double)[]): void
public static "glGetFloati_v"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetFloati_v"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glVertexAttribL2d"(arg0: integer, arg1: double, arg2: double): void
public static "glDepthRangeArrayv"(arg0: integer, arg1: (double)[]): void
public static "glDepthRangeArrayv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glGetDoublei_v"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glGetDoublei_v"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glVertexAttribL3d"(arg0: integer, arg1: double, arg2: double, arg3: double): void
public static "glVertexAttribL4dv"(arg0: integer, arg1: (double)[]): void
public static "glVertexAttribL4dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glProgramUniform2iv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform2iv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform1fv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glProgramUniform1fv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glProgramUniform4iv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform4iv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform4uiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform4uiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniformMatrix3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix2x3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix2x3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniformMatrix3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniform1uiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform1uiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniformMatrix2x3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix2x3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix2x4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix2x4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniformMatrix4x2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix4x2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniformMatrix4x2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix4x2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix4x3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix4x3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glVertexAttribLPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glVertexAttribLPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "glVertexAttribLPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: $DoubleBuffer$Type): void
public static "glProgramUniform3fv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glProgramUniform3fv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glProgramUniform1iv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform1iv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform3iv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform3iv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform4dv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glProgramUniform4dv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glGetVertexAttribLdv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glGetVertexAttribLdv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glProgramUniform3dv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glProgramUniform3dv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix2x4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix2x4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniformMatrix3x2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix3x2dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniformMatrix3x4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix3x4dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniform1dv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glProgramUniform1dv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glProgramUniform2uiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform2uiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniformMatrix3x2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix3x2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniformMatrix4x3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (double)[]): void
public static "glProgramUniformMatrix4x3dv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $DoubleBuffer$Type): void
public static "glProgramUniform3uiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glProgramUniform3uiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glProgramUniform4fv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glProgramUniform4fv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetProgramPipelineInfoLog"(arg0: integer, arg1: (integer)[], arg2: $ByteBuffer$Type): void
public static "glGetProgramPipelineInfoLog"(arg0: integer): string
public static "glGetProgramPipelineInfoLog"(arg0: integer, arg1: integer): string
public static "glGetProgramPipelineInfoLog"(arg0: integer, arg1: $IntBuffer$Type, arg2: $ByteBuffer$Type): void
public static "glViewportIndexedfv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glViewportIndexedfv"(arg0: integer, arg1: (float)[]): void
public static "glDepthRangeIndexed"(arg0: integer, arg1: double, arg2: double): void
public static "glValidateProgramPipeline"(arg0: integer): void
public static "glProgramUniform2dv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glProgramUniform2dv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glProgramUniformMatrix3x4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (float)[]): void
public static "glProgramUniformMatrix3x4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $FloatBuffer$Type): void
public static "glProgramUniform2fv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glProgramUniform2fv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "nglProgramUniformMatrix2x3dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix3x2dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglVertexAttribL3dv"(arg0: integer, arg1: long): void
public static "nglProgramUniformMatrix3fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglVertexAttribL4dv"(arg0: integer, arg1: long): void
public static "nglProgramUniformMatrix3x4fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix3x4dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix4dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglVertexAttribLPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglProgramUniformMatrix2x4fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix2dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix4x3fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix4fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglViewportIndexedfv"(arg0: integer, arg1: long): void
public static "nglVertexAttribL2dv"(arg0: integer, arg1: long): void
public static "nglProgramUniformMatrix3dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix4x3dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglVertexAttribL1dv"(arg0: integer, arg1: long): void
public static "nglProgramUniformMatrix4x2dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglGetVertexAttribLdv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglDepthRangeArrayv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglProgramUniformMatrix2x4dv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix2fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix2x3fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniformMatrix4x2fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglGetProgramPipelineInfoLog"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "nglProgramUniformMatrix3x2fv"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: long): void
public static "nglProgramUniform4dv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform4fv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform2dv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetShaderPrecisionFormat"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "glGetProgramPipelinei"(arg0: integer, arg1: integer): integer
public static "nglProgramUniform3uiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetProgramPipelineiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglProgramUniform3iv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform1iv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform2uiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform4uiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform1fv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform2fv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform1dv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform3dv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGenProgramPipelines"(arg0: integer, arg1: long): void
public static "nglCreateShaderProgramv"(arg0: integer, arg1: integer, arg2: long): integer
public static "nglDeleteProgramPipelines"(arg0: integer, arg1: long): void
public static "nglGetProgramBinary"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: long): void
public static "nglProgramUniform2iv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform4iv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform1uiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglProgramUniform3fv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglShaderBinary"(arg0: integer, arg1: long, arg2: integer, arg3: long, arg4: integer): void
public static "nglProgramBinary"(arg0: integer, arg1: integer, arg2: long, arg3: integer): void
public static "nglScissorIndexedv"(arg0: integer, arg1: long): void
public static "nglGetDoublei_v"(arg0: integer, arg1: integer, arg2: long): void
public static "nglViewportArrayv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetFloati_v"(arg0: integer, arg1: integer, arg2: long): void
public static "nglScissorArrayv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetFloati"(arg0: integer, arg1: integer): float
public static "glGetDoublei"(arg0: integer, arg1: integer): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL41C$Type = ($GL41C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL41C_ = $GL41C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL44C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$GL43C, $GL43C$Type} from "packages/org/lwjgl/opengl/$GL43C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL44C extends $GL43C {
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


public static "glBindBuffersBase"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glBindBuffersBase"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glBindTextures"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glBindTextures"(arg0: integer, arg1: (integer)[]): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ShortBuffer$Type): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $FloatBuffer$Type): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $DoubleBuffer$Type): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $IntBuffer$Type): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (double)[]): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (float)[]): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (integer)[]): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ByteBuffer$Type): void
public static "glClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (short)[]): void
public static "glBufferStorage"(arg0: integer, arg1: (short)[], arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: long, arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: (float)[], arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: (integer)[], arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: (double)[], arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: $FloatBuffer$Type, arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer): void
public static "glBufferStorage"(arg0: integer, arg1: $DoubleBuffer$Type, arg2: integer): void
public static "glBindBuffersRange"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $PointerBuffer$Type, arg4: $PointerBuffer$Type): void
public static "glBindBuffersRange"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $PointerBuffer$Type, arg4: $PointerBuffer$Type): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[]): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (short)[]): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $DoubleBuffer$Type): void
public static "glClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $FloatBuffer$Type): void
public static "glBindSamplers"(arg0: integer, arg1: (integer)[]): void
public static "glBindSamplers"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glBindImageTextures"(arg0: integer, arg1: (integer)[]): void
public static "glBindImageTextures"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glBindVertexBuffers"(arg0: integer, arg1: (integer)[], arg2: $PointerBuffer$Type, arg3: (integer)[]): void
public static "glBindVertexBuffers"(arg0: integer, arg1: $IntBuffer$Type, arg2: $PointerBuffer$Type, arg3: $IntBuffer$Type): void
public static "nglBindSamplers"(arg0: integer, arg1: integer, arg2: long): void
public static "nglBufferStorage"(arg0: integer, arg1: long, arg2: long, arg3: integer): void
public static "nglBindBuffersBase"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglClearTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglBindTextures"(arg0: integer, arg1: integer, arg2: long): void
public static "nglBindBuffersRange"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long, arg5: long): void
public static "nglClearTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "nglBindImageTextures"(arg0: integer, arg1: integer, arg2: long): void
public static "nglBindVertexBuffers"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL44C$Type = ($GL44C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL44C_ = $GL44C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL45C" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$GL44C, $GL44C$Type} from "packages/org/lwjgl/opengl/$GL44C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL45C extends $GL44C {
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


public static "nglGetNamedFramebufferAttachmentParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglInvalidateNamedFramebufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public static "glGetNamedFramebufferAttachmentParameteri"(arg0: integer, arg1: integer, arg2: integer): integer
public static "glInvalidateNamedFramebufferSubData"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glInvalidateNamedFramebufferSubData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glInvalidateNamedFramebufferSubData"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glCreateTextures"(arg0: integer, arg1: (integer)[]): void
public static "glCreateTextures"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glCreateTextures"(arg0: integer): integer
public static "glBindTextureUnit"(arg0: integer, arg1: integer): void
public static "glTextureStorage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glTextureBuffer"(arg0: integer, arg1: integer, arg2: integer): void
public static "glTextureStorage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (short)[]): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[]): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $DoubleBuffer$Type): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $FloatBuffer$Type): void
public static "glGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "glTextureStorage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glTextureBarrier"(): void
public static "glGetnUniformiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetnUniformiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetnUniformuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetnUniformuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glCreateQueries"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glCreateQueries"(arg0: integer): integer
public static "glCreateQueries"(arg0: integer, arg1: (integer)[]): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: long): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $FloatBuffer$Type): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $IntBuffer$Type): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[]): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (float)[]): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (short)[]): void
public static "glReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ShortBuffer$Type): void
public static "glGetnUniformfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetnUniformfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glGetnUniformdv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glGetnUniformdv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[]): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (short)[]): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $DoubleBuffer$Type): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $FloatBuffer$Type): void
public static "glGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glCreateSamplers"(arg0: $IntBuffer$Type): void
public static "glCreateSamplers"(arg0: (integer)[]): void
public static "glCreateSamplers"(): integer
public static "glGetVertexArrayiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetVertexArrayiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetNamedFramebufferAttachmentParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetNamedFramebufferAttachmentParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glNamedRenderbufferStorageMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetnCompressedTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetnCompressedTexImage"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glUnmapNamedBuffer"(arg0: integer): boolean
public static "glClipControl"(arg0: integer, arg1: integer): void
public static "glMapNamedBuffer"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): $ByteBuffer
public static "glMapNamedBuffer"(arg0: integer, arg1: integer, arg2: long, arg3: $ByteBuffer$Type): $ByteBuffer
public static "glMapNamedBuffer"(arg0: integer, arg1: integer): $ByteBuffer
public static "glCreateBuffers"(arg0: $IntBuffer$Type): void
public static "glCreateBuffers"(): integer
public static "glCreateBuffers"(arg0: (integer)[]): void
public static "glNamedBufferData"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: long, arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: (double)[], arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: (float)[], arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: (long)[], arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: (integer)[], arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: (short)[], arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: $FloatBuffer$Type, arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: $LongBuffer$Type, arg2: integer): void
public static "glNamedBufferData"(arg0: integer, arg1: $DoubleBuffer$Type, arg2: integer): void
public static "glTransformFeedbackBufferBase"(arg0: integer, arg1: integer, arg2: integer): void
public static "glTransformFeedbackBufferRange"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "glGetTransformFeedbackiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetTransformFeedbackiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glCreateTransformFeedbacks"(): integer
public static "glCreateTransformFeedbacks"(arg0: $IntBuffer$Type): void
public static "glCreateTransformFeedbacks"(arg0: (integer)[]): void
public static "glTextureParameterIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glTextureParameterIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glCreateFramebuffers"(arg0: (integer)[]): void
public static "glCreateFramebuffers"(arg0: $IntBuffer$Type): void
public static "glCreateFramebuffers"(): integer
public static "glCheckNamedFramebufferStatus"(arg0: integer, arg1: integer): integer
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ShortBuffer$Type): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $IntBuffer$Type): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $FloatBuffer$Type): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (short)[]): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ByteBuffer$Type): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (float)[]): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (double)[]): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (integer)[]): void
public static "glTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $DoubleBuffer$Type): void
public static "glCopyTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "glTextureParameterIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glTextureParameterIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGenerateTextureMipmap"(arg0: integer): void
public static "glGetCompressedTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetCompressedTextureImage"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glGetTextureLevelParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetTextureLevelParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetTextureParameterfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glGetTextureParameterfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetTransformFeedbacki64_v"(arg0: integer, arg1: integer, arg2: integer, arg3: (long)[]): void
public static "glGetTransformFeedbacki64_v"(arg0: integer, arg1: integer, arg2: integer, arg3: $LongBuffer$Type): void
public static "glNamedBufferStorage"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: (short)[], arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: (integer)[], arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: (float)[], arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: $FloatBuffer$Type, arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: $DoubleBuffer$Type, arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: long, arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: (double)[], arg2: integer): void
public static "glNamedBufferStorage"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer): void
public static "glFlushMappedNamedBufferRange"(arg0: integer, arg1: long, arg2: long): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: (integer)[]): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: (float)[]): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: (short)[]): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $ShortBuffer$Type): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $IntBuffer$Type): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $FloatBuffer$Type): void
public static "glClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glNamedRenderbufferStorage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glNamedFramebufferDrawBuffers"(arg0: integer, arg1: integer): void
public static "glNamedFramebufferDrawBuffers"(arg0: integer, arg1: (integer)[]): void
public static "glNamedFramebufferDrawBuffers"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glCopyTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glTextureParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glTextureParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glClearNamedFramebufferfi"(arg0: integer, arg1: integer, arg2: integer, arg3: float, arg4: integer): void
public static "glTextureParameteri"(arg0: integer, arg1: integer, arg2: integer): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: $ShortBuffer$Type): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: (short)[]): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: $IntBuffer$Type): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: $ByteBuffer$Type): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: (float)[]): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: (double)[]): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: $DoubleBuffer$Type): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: (long)[]): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: $FloatBuffer$Type): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: $LongBuffer$Type): void
public static "glNamedBufferSubData"(arg0: integer, arg1: long, arg2: (integer)[]): void
public static "glGetTextureLevelParameterfv"(arg0: integer, arg1: integer, arg2: integer, arg3: (float)[]): void
public static "glGetTextureLevelParameterfv"(arg0: integer, arg1: integer, arg2: integer, arg3: $FloatBuffer$Type): void
public static "glGetTextureParameterIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetTextureParameterIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetNamedBufferPointerv"(arg0: integer, arg1: integer, arg2: $PointerBuffer$Type): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: (integer)[]): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: (long)[]): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: $ShortBuffer$Type): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: $DoubleBuffer$Type): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: $FloatBuffer$Type): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: $LongBuffer$Type): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: (short)[]): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: $IntBuffer$Type): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: (float)[]): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: (double)[]): void
public static "glGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: $ByteBuffer$Type): void
public static "glGetTextureParameterIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetTextureParameterIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glTextureParameterf"(arg0: integer, arg1: integer, arg2: float): void
public static "glGetTextureParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetTextureParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glTextureStorage2DMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean): void
public static "glCreateVertexArrays"(): integer
public static "glCreateVertexArrays"(arg0: (integer)[]): void
public static "glCreateVertexArrays"(arg0: $IntBuffer$Type): void
public static "glDisableVertexArrayAttrib"(arg0: integer, arg1: integer): void
public static "glEnableVertexArrayAttrib"(arg0: integer, arg1: integer): void
public static "glVertexArrayElementBuffer"(arg0: integer, arg1: integer): void
public static "glVertexArrayVertexBuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer): void
public static "glVertexArrayVertexBuffers"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $PointerBuffer$Type, arg4: $IntBuffer$Type): void
public static "glVertexArrayVertexBuffers"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $PointerBuffer$Type, arg4: (integer)[]): void
public static "glClearNamedFramebufferuiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glClearNamedFramebufferuiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glMapNamedBufferRange"(arg0: integer, arg1: long, arg2: long, arg3: integer, arg4: $ByteBuffer$Type): $ByteBuffer
public static "glMapNamedBufferRange"(arg0: integer, arg1: long, arg2: long, arg3: integer): $ByteBuffer
public static "glCreateRenderbuffers"(): integer
public static "glCreateRenderbuffers"(arg0: (integer)[]): void
public static "glCreateRenderbuffers"(arg0: $IntBuffer$Type): void
public static "glGetTransformFeedbacki_v"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetTransformFeedbacki_v"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetNamedBufferParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetNamedBufferParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glClearNamedFramebufferiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glClearNamedFramebufferiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetNamedFramebufferParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetNamedFramebufferParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetNamedRenderbufferParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetNamedRenderbufferParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (integer)[]): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (float)[]): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (double)[]): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ByteBuffer$Type): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $FloatBuffer$Type): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $IntBuffer$Type): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $DoubleBuffer$Type): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ShortBuffer$Type): void
public static "glTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (short)[]): void
public static "glCompressedTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $ByteBuffer$Type): void
public static "glCompressedTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $DoubleBuffer$Type): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[]): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $FloatBuffer$Type): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (float)[]): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ShortBuffer$Type): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $IntBuffer$Type): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (short)[]): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (double)[]): void
public static "glTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glCompressedTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ByteBuffer$Type): void
public static "glCompressedTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glCopyNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: long): void
public static "glTextureStorage3DMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: boolean): void
public static "glCompressedTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $ByteBuffer$Type): void
public static "glCompressedTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "glGetNamedBufferParameteri64v"(arg0: integer, arg1: integer, arg2: $LongBuffer$Type): void
public static "glGetNamedBufferParameteri64v"(arg0: integer, arg1: integer, arg2: (long)[]): void
public static "glNamedFramebufferReadBuffer"(arg0: integer, arg1: integer): void
public static "glBlitNamedFramebuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $FloatBuffer$Type): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (short)[]): void
public static "glTextureBufferRange"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "glCopyTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): void
public static "glNamedFramebufferParameteri"(arg0: integer, arg1: integer, arg2: integer): void
public static "glClearNamedFramebufferfv"(arg0: integer, arg1: integer, arg2: integer, arg3: $FloatBuffer$Type): void
public static "glClearNamedFramebufferfv"(arg0: integer, arg1: integer, arg2: integer, arg3: (float)[]): void
public static "glNamedFramebufferTexture"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glTextureParameterfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glTextureParameterfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glNamedFramebufferTextureLayer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glNamedFramebufferDrawBuffer"(arg0: integer, arg1: integer): void
public static "glNamedFramebufferRenderbuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glInvalidateNamedFramebufferData"(arg0: integer, arg1: integer): void
public static "glInvalidateNamedFramebufferData"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glInvalidateNamedFramebufferData"(arg0: integer, arg1: (integer)[]): void
public static "glVertexArrayBindingDivisor"(arg0: integer, arg1: integer, arg2: integer): void
public static "glVertexArrayAttribLFormat"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glCreateProgramPipelines"(): integer
public static "glCreateProgramPipelines"(arg0: $IntBuffer$Type): void
public static "glCreateProgramPipelines"(arg0: (integer)[]): void
public static "glGetQueryBufferObjectuiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetVertexArrayIndexediv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetVertexArrayIndexediv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetQueryBufferObjecti64v"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $IntBuffer$Type): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ShortBuffer$Type): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: long): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ByteBuffer$Type): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (double)[]): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (short)[]): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (float)[]): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $DoubleBuffer$Type): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $FloatBuffer$Type): void
public static "glGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (integer)[]): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $DoubleBuffer$Type): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $FloatBuffer$Type): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $IntBuffer$Type): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (short)[]): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (integer)[]): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ByteBuffer$Type): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (double)[]): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: long): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (float)[]): void
public static "glGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ShortBuffer$Type): void
public static "glVertexArrayAttribFormat"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean, arg5: integer): void
public static "glVertexArrayAttribIFormat"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetQueryBufferObjectiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetVertexArrayIndexed64iv"(arg0: integer, arg1: integer, arg2: integer, arg3: $LongBuffer$Type): void
public static "glGetVertexArrayIndexed64iv"(arg0: integer, arg1: integer, arg2: integer, arg3: (long)[]): void
public static "glGetQueryBufferObjectui64v"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glVertexArrayAttribBinding"(arg0: integer, arg1: integer, arg2: integer): void
public static "glMemoryBarrierByRegion"(arg0: integer): void
public static "glGetGraphicsResetStatus"(): integer
public static "nglCreateTextures"(arg0: integer, arg1: integer, arg2: long): void
public static "nglCreateBuffers"(arg0: integer, arg1: long): void
public static "nglNamedBufferData"(arg0: integer, arg1: long, arg2: long, arg3: integer): void
public static "nglMapNamedBuffer"(arg0: integer, arg1: integer): long
public static "nglReadnPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: long): void
public static "nglGetnUniformdv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetnUniformd"(arg0: integer, arg1: integer): double
public static "nglCreateQueries"(arg0: integer, arg1: integer, arg2: long): void
public static "nglCreateSamplers"(arg0: integer, arg1: long): void
public static "nglGetTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "nglGetnTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "glGetVertexArrayi"(arg0: integer, arg1: integer): integer
public static "nglGetnUniformfv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetnUniformf"(arg0: integer, arg1: integer): float
public static "nglGetnUniformuiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetnUniformi"(arg0: integer, arg1: integer): integer
public static "glGetnUniformui"(arg0: integer, arg1: integer): integer
public static "nglGetnUniformiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglCreateRenderbuffers"(arg0: integer, arg1: long): void
public static "glGetNamedRenderbufferParameteri"(arg0: integer, arg1: integer): integer
public static "nglCompressedTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "nglTextureParameterIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglInvalidateNamedFramebufferData"(arg0: integer, arg1: integer, arg2: long): void
public static "nglClearNamedBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: long): void
public static "glGetTransformFeedbacki"(arg0: integer, arg1: integer): integer
public static "glGetTransformFeedbacki"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglCreateFramebuffers"(arg0: integer, arg1: long): void
public static "nglNamedFramebufferDrawBuffers"(arg0: integer, arg1: integer, arg2: long): void
public static "glTextureParameterIi"(arg0: integer, arg1: integer, arg2: integer): void
public static "nglGetTransformFeedbackiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetTransformFeedbacki64_v"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglNamedBufferStorage"(arg0: integer, arg1: long, arg2: long, arg3: integer): void
public static "glGetNamedBufferParameteri"(arg0: integer, arg1: integer): integer
public static "nglNamedBufferSubData"(arg0: integer, arg1: long, arg2: long, arg3: long): void
public static "nglGetNamedBufferParameteri64v"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetNamedBufferPointerv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglCreateTransformFeedbacks"(arg0: integer, arg1: long): void
public static "glGetNamedBufferPointer"(arg0: integer, arg1: integer): long
public static "glGetNamedBufferParameteri64"(arg0: integer, arg1: integer): long
public static "glGetNamedFramebufferParameteri"(arg0: integer, arg1: integer): integer
public static "nglGetNamedFramebufferParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetNamedRenderbufferParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetNamedBufferParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetNamedBufferSubData"(arg0: integer, arg1: long, arg2: long, arg3: long): void
public static "nglTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "nglClearNamedBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglClearNamedFramebufferiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglMapNamedBufferRange"(arg0: integer, arg1: long, arg2: long, arg3: integer): long
public static "nglGetTransformFeedbacki_v"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglClearNamedFramebufferfv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglTextureSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "nglCompressedTextureSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "nglCompressedTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glGetTransformFeedbacki64"(arg0: integer, arg1: integer, arg2: integer): long
public static "nglTextureParameterfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglTextureSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "nglClearNamedFramebufferuiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetTextureLevelParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetTextureLevelParameterf"(arg0: integer, arg1: integer, arg2: integer): float
public static "nglGetCompressedTextureImage"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglCreateProgramPipelines"(arg0: integer, arg1: long): void
public static "nglVertexArrayVertexBuffers"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long, arg5: long): void
public static "glGetTextureParameterIi"(arg0: integer, arg1: integer): integer
public static "nglGetTextureLevelParameterfv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetTextureParameterfv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetTextureParameteri"(arg0: integer, arg1: integer): integer
public static "glGetVertexArrayIndexedi"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglGetVertexArrayIndexediv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetnCompressedTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetTextureParameterIui"(arg0: integer, arg1: integer): integer
public static "glGetTextureParameterf"(arg0: integer, arg1: integer): float
public static "nglGetCompressedTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: long): void
public static "nglGetTextureParameterIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglTextureParameterIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetTextureParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglTextureParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetTextureLevelParameteri"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglGetTextureParameterIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetVertexArrayIndexed64iv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetVertexArrayIndexed64i"(arg0: integer, arg1: integer, arg2: integer): long
public static "nglGetTextureSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: long): void
public static "glTextureParameterIui"(arg0: integer, arg1: integer, arg2: integer): void
public static "nglCreateVertexArrays"(arg0: integer, arg1: long): void
public static "nglGetVertexArrayiv"(arg0: integer, arg1: integer, arg2: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL45C$Type = ($GL45C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL45C_ = $GL45C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL42C" {
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GL41C, $GL41C$Type} from "packages/org/lwjgl/opengl/$GL41C"

export class $GL42C extends $GL41C {
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


public static "nglDrawElementsInstancedBaseInstance"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer, arg5: integer): void
public static "nglDrawElementsInstancedBaseVertexBaseInstance"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer, arg5: integer, arg6: integer): void
public static "glDrawTransformFeedbackStreamInstanced"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseInstance"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type, arg3: integer, arg4: integer): void
public static "glDrawElementsInstancedBaseInstance"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer, arg5: integer): void
public static "glDrawElementsInstancedBaseInstance"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseInstance"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseInstance"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseVertexBaseInstance"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer, arg4: integer): void
public static "glDrawElementsInstancedBaseVertexBaseInstance"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer, arg5: integer, arg6: integer): void
public static "glDrawElementsInstancedBaseVertexBaseInstance"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type, arg3: integer, arg4: integer, arg5: integer): void
public static "glDrawElementsInstancedBaseVertexBaseInstance"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer, arg3: integer, arg4: integer): void
public static "glDrawElementsInstancedBaseVertexBaseInstance"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer, arg3: integer, arg4: integer): void
public static "glBindImageTexture"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: integer, arg6: integer): void
public static "glTexStorage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glTexStorage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glTexStorage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glMemoryBarrier"(arg0: integer): void
public static "glGetActiveAtomicCounterBufferiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetActiveAtomicCounterBufferiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glDrawTransformFeedbackInstanced"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetInternalformativ"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetInternalformativ"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glDrawArraysInstancedBaseInstance"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetActiveAtomicCounterBufferi"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglGetInternalformativ"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglGetActiveAtomicCounterBufferiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetInternalformati"(arg0: integer, arg1: integer, arg2: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL42C$Type = ($GL42C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL42C_ = $GL42C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL43C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$GL42C, $GL42C$Type} from "packages/org/lwjgl/opengl/$GL42C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GLDebugMessageCallbackI, $GLDebugMessageCallbackI$Type} from "packages/org/lwjgl/opengl/$GLDebugMessageCallbackI"

export class $GL43C extends $GL42C {
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


public static "glGetObjectLabel"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $ByteBuffer$Type): void
public static "glGetObjectLabel"(arg0: integer, arg1: integer): string
public static "glGetObjectLabel"(arg0: integer, arg1: integer, arg2: integer): string
public static "glGetObjectLabel"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $ByteBuffer$Type): void
public static "glTexBufferRange"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "glObjectPtrLabel"(arg0: long, arg1: $ByteBuffer$Type): void
public static "glObjectPtrLabel"(arg0: long, arg1: charseq): void
public static "glTextureView"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "glDispatchCompute"(arg0: integer, arg1: integer, arg2: integer): void
public static "glObjectLabel"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glObjectLabel"(arg0: integer, arg1: integer, arg2: charseq): void
public static "glPopDebugGroup"(): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $FloatBuffer$Type): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public static "glClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (short)[]): void
public static "glCopyImageSubData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer, arg12: integer, arg13: integer, arg14: integer): void
public static "glPushDebugGroup"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glPushDebugGroup"(arg0: integer, arg1: integer, arg2: charseq): void
public static "glBindVertexBuffer"(arg0: integer, arg1: integer, arg2: long, arg3: integer): void
public static "glGetProgramResourceName"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[], arg4: $ByteBuffer$Type): void
public static "glGetProgramResourceName"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): string
public static "glGetProgramResourceName"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $ByteBuffer$Type): void
public static "glGetProgramResourceName"(arg0: integer, arg1: integer, arg2: integer): string
public static "glInvalidateSubFramebuffer"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glInvalidateSubFramebuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glInvalidateSubFramebuffer"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glGetProgramResourceIndex"(arg0: integer, arg1: integer, arg2: charseq): integer
public static "glGetProgramResourceIndex"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): integer
public static "glVertexAttribLFormat"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glShaderStorageBlockBinding"(arg0: integer, arg1: integer, arg2: integer): void
public static "glDebugMessageCallback"(arg0: $GLDebugMessageCallbackI$Type, arg1: long): void
public static "glMultiDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type, arg3: integer, arg4: integer): void
public static "glMultiDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: integer, arg4: integer): void
public static "glMultiDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: long, arg3: integer, arg4: integer): void
public static "glMultiDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: integer, arg4: integer): void
public static "glGetProgramResourceLocationIndex"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): integer
public static "glGetProgramResourceLocationIndex"(arg0: integer, arg1: integer, arg2: charseq): integer
public static "glMultiDrawArraysIndirect"(arg0: integer, arg1: long, arg2: integer, arg3: integer): void
public static "glMultiDrawArraysIndirect"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: integer): void
public static "glMultiDrawArraysIndirect"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer, arg3: integer): void
public static "glMultiDrawArraysIndirect"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer): void
public static "glGetObjectPtrLabel"(arg0: long, arg1: (integer)[], arg2: $ByteBuffer$Type): void
public static "glGetObjectPtrLabel"(arg0: long, arg1: integer): string
public static "glGetObjectPtrLabel"(arg0: long): string
public static "glGetObjectPtrLabel"(arg0: long, arg1: $IntBuffer$Type, arg2: $ByteBuffer$Type): void
public static "glInvalidateTexSubImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "glInvalidateBufferSubData"(arg0: integer, arg1: long, arg2: long): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $FloatBuffer$Type): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $IntBuffer$Type): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: (short)[]): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: (float)[]): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: (integer)[]): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $ShortBuffer$Type): void
public static "glClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glFramebufferParameteri"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetProgramResourceiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[], arg4: (integer)[], arg5: (integer)[]): void
public static "glGetProgramResourceiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type, arg5: $IntBuffer$Type): void
public static "glTexStorage3DMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: boolean): void
public static "glTexStorage2DMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean): void
public static "glVertexAttribBinding"(arg0: integer, arg1: integer): void
public static "glGetInternalformati64v"(arg0: integer, arg1: integer, arg2: integer, arg3: (long)[]): void
public static "glGetInternalformati64v"(arg0: integer, arg1: integer, arg2: integer, arg3: $LongBuffer$Type): void
public static "glGetProgramResourceLocation"(arg0: integer, arg1: integer, arg2: charseq): integer
public static "glGetProgramResourceLocation"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): integer
public static "glVertexBindingDivisor"(arg0: integer, arg1: integer): void
public static "glGetFramebufferParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetFramebufferParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glDebugMessageInsert"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glDebugMessageInsert"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: charseq): void
public static "glGetDebugMessageLog"(arg0: integer, arg1: $IntBuffer$Type, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type, arg5: $IntBuffer$Type, arg6: $ByteBuffer$Type): integer
public static "glGetDebugMessageLog"(arg0: integer, arg1: (integer)[], arg2: (integer)[], arg3: (integer)[], arg4: (integer)[], arg5: (integer)[], arg6: $ByteBuffer$Type): integer
public static "glInvalidateBufferData"(arg0: integer): void
public static "glDebugMessageControl"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[], arg4: boolean): void
public static "glDebugMessageControl"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
public static "glDebugMessageControl"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: boolean): void
public static "glInvalidateFramebuffer"(arg0: integer, arg1: (integer)[]): void
public static "glInvalidateFramebuffer"(arg0: integer, arg1: integer): void
public static "glInvalidateFramebuffer"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glGetProgramInterfaceiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetProgramInterfaceiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glInvalidateTexImage"(arg0: integer, arg1: integer): void
public static "glVertexAttribFormat"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer): void
public static "glDispatchComputeIndirect"(arg0: long): void
public static "glVertexAttribIFormat"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "nglDebugMessageCallback"(arg0: long, arg1: long): void
public static "nglMultiDrawArraysIndirect"(arg0: integer, arg1: long, arg2: integer, arg3: integer): void
public static "glGetInternalformati64"(arg0: integer, arg1: integer, arg2: integer): long
public static "nglInvalidateFramebuffer"(arg0: integer, arg1: integer, arg2: long): void
public static "nglMultiDrawElementsIndirect"(arg0: integer, arg1: integer, arg2: long, arg3: integer, arg4: integer): void
public static "nglDebugMessageControl"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long, arg5: boolean): void
public static "glGetFramebufferParameteri"(arg0: integer, arg1: integer): integer
public static "nglGetInternalformati64v"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglGetDebugMessageLog"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: long, arg5: long, arg6: long, arg7: long): integer
public static "nglClearBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: integer, arg5: integer, arg6: long): void
public static "nglDebugMessageInsert"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "nglGetObjectPtrLabel"(arg0: long, arg1: integer, arg2: long, arg3: long): void
public static "nglGetFramebufferParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglInvalidateSubFramebuffer"(arg0: integer, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public static "glGetProgramInterfacei"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglGetProgramResourceLocationIndex"(arg0: integer, arg1: integer, arg2: long): integer
public static "nglGetProgramResourceName"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long, arg5: long): void
public static "nglGetProgramResourceIndex"(arg0: integer, arg1: integer, arg2: long): integer
public static "nglGetProgramInterfaceiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetProgramResourceLocation"(arg0: integer, arg1: integer, arg2: long): integer
public static "nglGetProgramResourceiv"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long, arg5: integer, arg6: long, arg7: long): void
public static "nglObjectLabel"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglClearBufferData"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglPushDebugGroup"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetObjectLabel"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "nglObjectPtrLabel"(arg0: long, arg1: integer, arg2: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL43C$Type = ($GL43C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL43C_ = $GL43C$Type;
}}
declare module "packages/org/lwjgl/system/$FunctionProvider" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export interface $FunctionProvider {

 "getFunctionAddress"(arg0: charseq): long
 "getFunctionAddress"(arg0: $ByteBuffer$Type): long

(arg0: charseq): long
}

export namespace $FunctionProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FunctionProvider$Type = ($FunctionProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FunctionProvider_ = $FunctionProvider$Type;
}}
declare module "packages/org/lwjgl/stb/$STBTTFontinfo$Buffer" {
import {$STBTTFontinfo, $STBTTFontinfo$Type} from "packages/org/lwjgl/stb/$STBTTFontinfo"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $STBTTFontinfo$Buffer extends $StructBuffer<($STBTTFontinfo), ($STBTTFontinfo$Buffer)> implements $NativeResource {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $STBTTFontinfo$Buffer$Type = ($STBTTFontinfo$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $STBTTFontinfo$Buffer_ = $STBTTFontinfo$Buffer$Type;
}}
declare module "packages/org/lwjgl/system/$CustomBuffer" {
import {$Pointer$Default, $Pointer$Default$Type} from "packages/org/lwjgl/system/$Pointer$Default"

export class $CustomBuffer<SELF extends $CustomBuffer<(SELF)>> extends $Pointer$Default {


public "put"(arg0: SELF): SELF
public "toString"(): string
public "clear"(): SELF
public "position"(arg0: integer): SELF
public "position"(): integer
public "limit"(): integer
public "limit"(arg0: integer): SELF
public "remaining"(): integer
public "capacity"(): integer
public "address"(): long
public "address"(arg0: integer): long
public "mark"(): SELF
public "reset"(): SELF
public "flip"(): SELF
public "rewind"(): SELF
public "hasRemaining"(): boolean
public "slice"(): SELF
public "slice"(arg0: integer, arg1: integer): SELF
public "duplicate"(): SELF
public "compact"(): SELF
public "free"(): void
public "sizeof"(): integer
public "address0"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomBuffer$Type<SELF> = ($CustomBuffer<(SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomBuffer_<SELF> = $CustomBuffer$Type<(SELF)>;
}}
declare module "packages/org/lwjgl/system/windows/$POINT$Buffer" {
import {$POINT, $POINT$Type} from "packages/org/lwjgl/system/windows/$POINT"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $POINT$Buffer extends $StructBuffer<($POINT), ($POINT$Buffer)> implements $NativeResource {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "x"(): integer
public "x"(arg0: integer): $POINT$Buffer
public "y"(): integer
public "y"(arg0: integer): $POINT$Buffer
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $POINT$Buffer$Type = ($POINT$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $POINT$Buffer_ = $POINT$Buffer$Type;
}}
declare module "packages/org/lwjgl/system/libffi/$FFICIF$Buffer" {
import {$FFIType, $FFIType$Type} from "packages/org/lwjgl/system/libffi/$FFIType"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export class $FFICIF$Buffer extends $StructBuffer<($FFICIF), ($FFICIF$Buffer)> implements $NativeResource {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "flags"(): integer
public "bytes"(): integer
public "nargs"(): integer
public "rtype"(): $FFIType
public "abi"(): integer
public "arg_types"(arg0: integer): $PointerBuffer
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FFICIF$Buffer$Type = ($FFICIF$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FFICIF$Buffer_ = $FFICIF$Buffer$Type;
}}
declare module "packages/org/lwjgl/system/$Struct" {
import {$Struct$StructValidation, $Struct$StructValidation$Type} from "packages/org/lwjgl/system/$Struct$StructValidation"
import {$Pointer$Default, $Pointer$Default$Type} from "packages/org/lwjgl/system/$Pointer$Default"

export class $Struct extends $Pointer$Default {


public "clear"(): void
public static "validate"(arg0: long, arg1: integer, arg2: integer, arg3: $Struct$StructValidation$Type): void
public "isNull"(arg0: integer): boolean
public "free"(): void
public "sizeof"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Struct$Type = ($Struct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Struct_ = $Struct$Type;
}}
declare module "packages/org/lwjgl/system/$Callback" {
import {$Pointer, $Pointer$Type} from "packages/org/lwjgl/system/$Pointer"
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $Callback implements $Pointer, $NativeResource {


public static "get"<T extends $CallbackI>(arg0: long): T
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "address"(): long
public "free"(): void
public static "free"(arg0: long): void
public static "getSafe"<T extends $CallbackI>(arg0: long): T
public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Callback$Type = ($Callback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Callback_ = $Callback$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWScrollCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWScrollCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: double, arg2: double): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: double, arg2: double): void
}

export namespace $GLFWScrollCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWScrollCallbackI$Type = ($GLFWScrollCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWScrollCallbackI_ = $GLFWScrollCallbackI$Type;
}}
declare module "packages/org/lwjgl/system/$MemoryUtil$MemoryAllocator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MemoryUtil$MemoryAllocator {

 "free"(arg0: long): void
 "malloc"(arg0: long): long
 "calloc"(arg0: long, arg1: long): long
 "getFree"(): long
 "getMalloc"(): long
 "getCalloc"(): long
 "getRealloc"(): long
 "getAlignedAlloc"(): long
 "getAlignedFree"(): long
 "aligned_alloc"(arg0: long, arg1: long): long
 "aligned_free"(arg0: long): void
 "realloc"(arg0: long, arg1: long): long
}

export namespace $MemoryUtil$MemoryAllocator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryUtil$MemoryAllocator$Type = ($MemoryUtil$MemoryAllocator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryUtil$MemoryAllocator_ = $MemoryUtil$MemoryAllocator$Type;
}}
declare module "packages/org/lwjgl/$PointerBuffer" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Pointer, $Pointer$Type} from "packages/org/lwjgl/system/$Pointer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$CustomBuffer, $CustomBuffer$Type} from "packages/org/lwjgl/system/$CustomBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $PointerBuffer extends $CustomBuffer<($PointerBuffer)> implements $Comparable<($PointerBuffer)> {


public "get"(arg0: (long)[], arg1: integer, arg2: integer): $PointerBuffer
public static "get"(arg0: $ByteBuffer$Type): long
public "get"(): long
public "get"(arg0: integer): long
public static "get"(arg0: $ByteBuffer$Type, arg1: integer): long
public "get"(arg0: (long)[]): $PointerBuffer
public "put"(arg0: $IntBuffer$Type): $PointerBuffer
public "put"(arg0: $ShortBuffer$Type): $PointerBuffer
public "put"(arg0: $FloatBuffer$Type): $PointerBuffer
public "put"(arg0: $DoubleBuffer$Type): $PointerBuffer
public "put"(arg0: $LongBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $DoubleBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $FloatBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $LongBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $IntBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $ShortBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $ByteBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: long): $PointerBuffer
public static "put"(arg0: $ByteBuffer$Type, arg1: long): void
public "put"(arg0: long): $PointerBuffer
public "put"(arg0: (long)[]): $PointerBuffer
public "put"(arg0: (long)[], arg1: integer, arg2: integer): $PointerBuffer
public "put"(arg0: $ByteBuffer$Type): $PointerBuffer
public "put"(arg0: integer, arg1: $Pointer$Type): $PointerBuffer
public "put"(arg0: $Pointer$Type): $PointerBuffer
public static "put"(arg0: $ByteBuffer$Type, arg1: integer, arg2: long): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compareTo"(arg0: $PointerBuffer$Type): integer
public static "create"(arg0: $ByteBuffer$Type): $PointerBuffer
public static "create"(arg0: long, arg1: integer): $PointerBuffer
public "getByteBuffer"(arg0: integer, arg1: integer): $ByteBuffer
public "getByteBuffer"(arg0: integer): $ByteBuffer
public static "allocateDirect"(arg0: integer): $PointerBuffer
public "sizeof"(): integer
public "putAddressOf"(arg0: $CustomBuffer$Type<(any)>): $PointerBuffer
public "putAddressOf"(arg0: integer, arg1: $CustomBuffer$Type<(any)>): $PointerBuffer
public "getIntBuffer"(arg0: integer, arg1: integer): $IntBuffer
public "getIntBuffer"(arg0: integer): $IntBuffer
public "getDoubleBuffer"(arg0: integer): $DoubleBuffer
public "getDoubleBuffer"(arg0: integer, arg1: integer): $DoubleBuffer
public "getStringASCII"(): string
public "getStringASCII"(arg0: integer): string
public "getStringUTF8"(): string
public "getStringUTF8"(arg0: integer): string
public "getShortBuffer"(arg0: integer, arg1: integer): $ShortBuffer
public "getShortBuffer"(arg0: integer): $ShortBuffer
public "getLongBuffer"(arg0: integer, arg1: integer): $LongBuffer
public "getLongBuffer"(arg0: integer): $LongBuffer
public "getFloatBuffer"(arg0: integer, arg1: integer): $FloatBuffer
public "getFloatBuffer"(arg0: integer): $FloatBuffer
public "getPointerBuffer"(arg0: integer): $PointerBuffer
public "getPointerBuffer"(arg0: integer, arg1: integer): $PointerBuffer
public "getStringUTF16"(): string
public "getStringUTF16"(arg0: integer): string
get "stringASCII"(): string
get "stringUTF8"(): string
get "stringUTF16"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PointerBuffer$Type = ($PointerBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PointerBuffer_ = $PointerBuffer$Type;
}}
declare module "packages/org/lwjgl/stb/$STBTTFontinfo" {
import {$STBTTFontinfo$Buffer, $STBTTFontinfo$Buffer$Type} from "packages/org/lwjgl/stb/$STBTTFontinfo$Buffer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $STBTTFontinfo extends $Struct implements $NativeResource {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer

constructor(arg0: $ByteBuffer$Type)

public static "create"(): $STBTTFontinfo
public static "create"(arg0: integer): $STBTTFontinfo$Buffer
public static "create"(arg0: long): $STBTTFontinfo
public static "create"(arg0: long, arg1: integer): $STBTTFontinfo$Buffer
public static "malloc"(arg0: integer, arg1: $MemoryStack$Type): $STBTTFontinfo$Buffer
public static "malloc"(arg0: $MemoryStack$Type): $STBTTFontinfo
public static "malloc"(): $STBTTFontinfo
public static "malloc"(arg0: integer): $STBTTFontinfo$Buffer
public "sizeof"(): integer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer, arg1: $MemoryStack$Type): $STBTTFontinfo$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: $MemoryStack$Type): $STBTTFontinfo
/**
 * 
 * @deprecated
 */
public static "mallocStack"(arg0: integer): $STBTTFontinfo$Buffer
/**
 * 
 * @deprecated
 */
public static "mallocStack"(): $STBTTFontinfo
public static "calloc"(arg0: integer): $STBTTFontinfo$Buffer
public static "calloc"(): $STBTTFontinfo
public static "calloc"(arg0: integer, arg1: $MemoryStack$Type): $STBTTFontinfo$Buffer
public static "calloc"(arg0: $MemoryStack$Type): $STBTTFontinfo
public static "createSafe"(arg0: long): $STBTTFontinfo
public static "createSafe"(arg0: long, arg1: integer): $STBTTFontinfo$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(): $STBTTFontinfo
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: $MemoryStack$Type): $STBTTFontinfo
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer): $STBTTFontinfo$Buffer
/**
 * 
 * @deprecated
 */
public static "callocStack"(arg0: integer, arg1: $MemoryStack$Type): $STBTTFontinfo$Buffer
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $STBTTFontinfo$Type = ($STBTTFontinfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $STBTTFontinfo_ = $STBTTFontinfo$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWKeyCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWKeyCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
}

export namespace $GLFWKeyCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWKeyCallbackI$Type = ($GLFWKeyCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWKeyCallbackI_ = $GLFWKeyCallbackI$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL32C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$GL31C, $GL31C$Type} from "packages/org/lwjgl/opengl/$GL31C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL32C extends $GL31C {
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


public static "glTexImage2DMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: boolean): void
public static "glDrawRangeElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: $ShortBuffer$Type, arg4: integer): void
public static "glDrawRangeElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: $ByteBuffer$Type, arg4: integer): void
public static "glDrawRangeElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type, arg5: integer): void
public static "glDrawRangeElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long, arg6: integer): void
public static "glDrawRangeElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: integer): void
public static "glFramebufferTexture"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glDrawElementsBaseVertex"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer): void
public static "glDrawElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer): void
public static "glDrawElementsBaseVertex"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type, arg3: integer): void
public static "glDrawElementsBaseVertex"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer): void
public static "glDrawElementsBaseVertex"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer): void
public static "glDrawElementsInstancedBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer, arg5: integer): void
public static "glDrawElementsInstancedBaseVertex"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseVertex"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseVertex"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: integer): void
public static "glDrawElementsInstancedBaseVertex"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type, arg3: integer, arg4: integer): void
public static "glGetBufferParameteri64v"(arg0: integer, arg1: integer, arg2: (long)[]): void
public static "glGetBufferParameteri64v"(arg0: integer, arg1: integer, arg2: $LongBuffer$Type): void
public static "glTexImage3DMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: boolean): void
public static "glMultiDrawElementsBaseVertex"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: $PointerBuffer$Type, arg4: $IntBuffer$Type): void
public static "glMultiDrawElementsBaseVertex"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: $PointerBuffer$Type, arg4: (integer)[]): void
public static "nglGetBufferParameteri64v"(arg0: integer, arg1: integer, arg2: long): void
public static "nglDrawElementsInstancedBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer, arg5: integer): void
public static "nglDrawElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer): void
public static "nglMultiDrawElementsBaseVertex"(arg0: integer, arg1: long, arg2: integer, arg3: long, arg4: integer, arg5: long): void
public static "nglGetMultisamplefv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglDrawRangeElementsBaseVertex"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long, arg6: integer): void
public static "glGetBufferParameteri64"(arg0: integer, arg1: integer): long
public static "glGetSynciv"(arg0: long, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type): void
public static "glGetSynciv"(arg0: long, arg1: integer, arg2: (integer)[], arg3: (integer)[]): void
public static "glFenceSync"(arg0: integer, arg1: integer): long
public static "glGetMultisamplefv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetMultisamplefv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glProvokingVertex"(arg0: integer): void
public static "glSampleMaski"(arg0: integer, arg1: integer): void
public static "glDeleteSync"(arg0: long): void
public static "glGetInteger64v"(arg0: integer, arg1: $LongBuffer$Type): void
public static "glGetInteger64v"(arg0: integer, arg1: (long)[]): void
public static "glClientWaitSync"(arg0: long, arg1: integer, arg2: long): integer
public static "glWaitSync"(arg0: long, arg1: integer, arg2: long): void
public static "glGetInteger64i_v"(arg0: integer, arg1: integer, arg2: (long)[]): void
public static "glGetInteger64i_v"(arg0: integer, arg1: integer, arg2: $LongBuffer$Type): void
public static "glIsSync"(arg0: long): boolean
public static "nglWaitSync"(arg0: long, arg1: integer, arg2: long): void
public static "nglIsSync"(arg0: long): boolean
public static "glGetMultisamplef"(arg0: integer, arg1: integer): float
public static "nglGetInteger64v"(arg0: integer, arg1: long): void
public static "glGetInteger64"(arg0: integer): long
public static "nglGetInteger64i_v"(arg0: integer, arg1: integer, arg2: long): void
public static "nglDeleteSync"(arg0: long): void
public static "glGetInteger64i"(arg0: integer, arg1: integer): long
public static "nglClientWaitSync"(arg0: long, arg1: integer, arg2: long): integer
public static "nglGetSynciv"(arg0: long, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "glGetSynci"(arg0: long, arg1: integer, arg2: $IntBuffer$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL32C$Type = ($GL32C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL32C_ = $GL32C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL33C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$GL32C, $GL32C$Type} from "packages/org/lwjgl/opengl/$GL32C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL33C extends $GL32C {
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


public static "glBindFragDataLocationIndexed"(arg0: integer, arg1: integer, arg2: integer, arg3: $ByteBuffer$Type): void
public static "glBindFragDataLocationIndexed"(arg0: integer, arg1: integer, arg2: integer, arg3: charseq): void
public static "glSamplerParameterIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glSamplerParameterIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glVertexAttribP1uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (integer)[]): void
public static "glVertexAttribP1uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $IntBuffer$Type): void
public static "glVertexAttribP2uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $IntBuffer$Type): void
public static "glVertexAttribP2uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (integer)[]): void
public static "glVertexAttribP3uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $IntBuffer$Type): void
public static "glVertexAttribP3uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (integer)[]): void
public static "glGetSamplerParameterfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glGetSamplerParameterfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetSamplerParameterIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetSamplerParameterIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glSamplerParameterIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glSamplerParameterIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glVertexAttribP4uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: (integer)[]): void
public static "glVertexAttribP4uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: $IntBuffer$Type): void
public static "glGetQueryObjecti64v"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetQueryObjecti64v"(arg0: integer, arg1: integer, arg2: (long)[]): void
public static "glGetQueryObjecti64v"(arg0: integer, arg1: integer, arg2: $LongBuffer$Type): void
public static "glGetQueryObjectui64v"(arg0: integer, arg1: integer, arg2: (long)[]): void
public static "glGetQueryObjectui64v"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetQueryObjectui64v"(arg0: integer, arg1: integer, arg2: $LongBuffer$Type): void
public static "glSamplerParameteri"(arg0: integer, arg1: integer, arg2: integer): void
public static "glVertexAttribDivisor"(arg0: integer, arg1: integer): void
public static "glSamplerParameterf"(arg0: integer, arg1: integer, arg2: float): void
public static "glGetSamplerParameterIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetSamplerParameterIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glSamplerParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glSamplerParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glSamplerParameterfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glSamplerParameterfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetSamplerParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetSamplerParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGenSamplers"(): integer
public static "glGenSamplers"(arg0: (integer)[]): void
public static "glGenSamplers"(arg0: $IntBuffer$Type): void
public static "glIsSampler"(arg0: integer): boolean
public static "glBindSampler"(arg0: integer, arg1: integer): void
public static "glDeleteSamplers"(arg0: $IntBuffer$Type): void
public static "glDeleteSamplers"(arg0: (integer)[]): void
public static "glDeleteSamplers"(arg0: integer): void
public static "glGetFragDataIndex"(arg0: integer, arg1: charseq): integer
public static "glGetFragDataIndex"(arg0: integer, arg1: $ByteBuffer$Type): integer
public static "glVertexAttribP1ui"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): void
public static "glQueryCounter"(arg0: integer, arg1: integer): void
public static "glVertexAttribP3ui"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): void
public static "glVertexAttribP4ui"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): void
public static "glVertexAttribP2ui"(arg0: integer, arg1: integer, arg2: boolean, arg3: integer): void
public static "nglGetQueryObjectui64v"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetSamplerParameterIi"(arg0: integer, arg1: integer): integer
public static "glGetQueryObjectui64"(arg0: integer, arg1: integer): long
public static "nglVertexAttribP1uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglSamplerParameterfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglSamplerParameterIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetSamplerParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetSamplerParameterfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglVertexAttribP2uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGetSamplerParameterIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglBindFragDataLocationIndexed"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetSamplerParameterIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglSamplerParameterIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetFragDataIndex"(arg0: integer, arg1: long): integer
public static "nglSamplerParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetSamplerParameteri"(arg0: integer, arg1: integer): integer
public static "glGetSamplerParameterIui"(arg0: integer, arg1: integer): integer
public static "nglGetQueryObjecti64v"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetSamplerParameterf"(arg0: integer, arg1: integer): float
public static "glGetQueryObjecti64"(arg0: integer, arg1: integer): long
public static "nglVertexAttribP3uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglVertexAttribP4uiv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGenSamplers"(arg0: integer, arg1: long): void
public static "nglDeleteSamplers"(arg0: integer, arg1: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL33C$Type = ($GL33C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL33C_ = $GL33C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL30C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$GL21C, $GL21C$Type} from "packages/org/lwjgl/opengl/$GL21C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL30C extends $GL21C {
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


public static "glRenderbufferStorageMultisample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glDeleteFramebuffers"(arg0: integer): void
public static "glDeleteFramebuffers"(arg0: (integer)[]): void
public static "glDeleteFramebuffers"(arg0: $IntBuffer$Type): void
public static "glGetTexParameterIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetTexParameterIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glEndTransformFeedback"(): void
public static "glVertexAttribI2uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI2uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI4ubv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glGetVertexAttribIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetVertexAttribIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glFramebufferTexture3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glTransformFeedbackVaryings"(arg0: integer, arg1: (charseq)[], arg2: integer): void
public static "glTransformFeedbackVaryings"(arg0: integer, arg1: charseq, arg2: integer): void
public static "glTransformFeedbackVaryings"(arg0: integer, arg1: $PointerBuffer$Type, arg2: integer): void
public static "glGetTransformFeedbackVarying"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type): string
public static "glGetTransformFeedbackVarying"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type, arg5: $ByteBuffer$Type): void
public static "glGetTransformFeedbackVarying"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: (integer)[], arg4: (integer)[], arg5: $ByteBuffer$Type): void
public static "glGetTransformFeedbackVarying"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type): string
public static "glGetRenderbufferParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetRenderbufferParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glDeleteVertexArrays"(arg0: $IntBuffer$Type): void
public static "glDeleteVertexArrays"(arg0: (integer)[]): void
public static "glDeleteVertexArrays"(arg0: integer): void
public static "glFlushMappedBufferRange"(arg0: integer, arg1: long, arg2: long): void
public static "glBeginConditionalRender"(arg0: integer, arg1: integer): void
public static "glGetVertexAttribIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetVertexAttribIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glBeginTransformFeedback"(arg0: integer): void
public static "glFramebufferRenderbuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glVertexAttribI1uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI1uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI4usv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttribI4usv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glEndConditionalRender"(): void
public static "glFramebufferTexture1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glFramebufferTextureLayer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetTexParameterIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetTexParameterIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glFramebufferTexture2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glGetFragDataLocation"(arg0: integer, arg1: charseq): integer
public static "glGetFragDataLocation"(arg0: integer, arg1: $ByteBuffer$Type): integer
public static "glVertexAttribI3uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI3uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI4uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI4uiv"(arg0: integer, arg1: (integer)[]): void
public static "glDeleteRenderbuffers"(arg0: integer): void
public static "glDeleteRenderbuffers"(arg0: (integer)[]): void
public static "glDeleteRenderbuffers"(arg0: $IntBuffer$Type): void
public static "glCheckFramebufferStatus"(arg0: integer): integer
public static "glVertexAttribIPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "glVertexAttribIPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glVertexAttribIPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glVertexAttribIPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glBindFragDataLocation"(arg0: integer, arg1: integer, arg2: charseq): void
public static "glBindFragDataLocation"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glRenderbufferStorage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glBindFramebuffer"(arg0: integer, arg1: integer): void
public static "glBindVertexArray"(arg0: integer): void
public static "glGetFramebufferAttachmentParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetFramebufferAttachmentParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "nglUniform1uiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglClearBufferfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform2uiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform3uiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform4uiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglClearBufferiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetStringi"(arg0: integer, arg1: integer): long
public static "nglClearBufferuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetIntegeri_v"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetIntegeri"(arg0: integer, arg1: integer): integer
public static "glGetUniformui"(arg0: integer, arg1: integer): integer
public static "glTexParameterIi"(arg0: integer, arg1: integer, arg2: integer): void
public static "nglMapBufferRange"(arg0: integer, arg1: long, arg2: long, arg3: integer): long
public static "nglGenFramebuffers"(arg0: integer, arg1: long): void
public static "glTexParameterIui"(arg0: integer, arg1: integer, arg2: integer): void
public static "nglGetBooleani_v"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetBooleani"(arg0: integer, arg1: integer): boolean
public static "nglTexParameterIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetUniformuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGenVertexArrays"(arg0: integer, arg1: long): void
public static "glGetStringi"(arg0: integer, arg1: integer): string
public static "nglVertexAttribI4bv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI4sv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI4ubv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI1iv"(arg0: integer, arg1: long): void
public static "nglVertexAttribIPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglGetVertexAttribIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetVertexAttribIi"(arg0: integer, arg1: integer): integer
public static "nglGetVertexAttribIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglBindFragDataLocation"(arg0: integer, arg1: integer, arg2: long): void
public static "nglVertexAttribI4usv"(arg0: integer, arg1: long): void
public static "nglGetFragDataLocation"(arg0: integer, arg1: long): integer
public static "nglVertexAttribI2iv"(arg0: integer, arg1: long): void
public static "glGetVertexAttribIui"(arg0: integer, arg1: integer): integer
public static "nglVertexAttribI4iv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI1uiv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI3uiv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI4uiv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI3iv"(arg0: integer, arg1: long): void
public static "nglVertexAttribI2uiv"(arg0: integer, arg1: long): void
public static "glGetRenderbufferParameteri"(arg0: integer, arg1: integer): integer
public static "nglDeleteFramebuffers"(arg0: integer, arg1: long): void
public static "nglDeleteRenderbuffers"(arg0: integer, arg1: long): void
public static "nglGetTexParameterIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetTexParameterIiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglTransformFeedbackVaryings"(arg0: integer, arg1: integer, arg2: long, arg3: integer): void
public static "glGetTexParameterIui"(arg0: integer, arg1: integer): integer
public static "nglGenRenderbuffers"(arg0: integer, arg1: long): void
public static "nglTexParameterIuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglDeleteVertexArrays"(arg0: integer, arg1: long): void
public static "glGetTexParameterIi"(arg0: integer, arg1: integer): integer
public static "nglGetRenderbufferParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetTransformFeedbackVarying"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long, arg5: long, arg6: long): void
public static "glIsRenderbuffer"(arg0: integer): boolean
public static "glColorMaski"(arg0: integer, arg1: boolean, arg2: boolean, arg3: boolean, arg4: boolean): void
public static "glGenFramebuffers"(arg0: $IntBuffer$Type): void
public static "glGenFramebuffers"(): integer
public static "glGenFramebuffers"(arg0: (integer)[]): void
public static "glTexParameterIiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glTexParameterIiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glTexParameterIuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glTexParameterIuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glEnablei"(arg0: integer, arg1: integer): void
public static "glDisablei"(arg0: integer, arg1: integer): void
public static "glGenVertexArrays"(arg0: $IntBuffer$Type): void
public static "glGenVertexArrays"(arg0: (integer)[]): void
public static "glGenVertexArrays"(): integer
public static "glBindBufferBase"(arg0: integer, arg1: integer, arg2: integer): void
public static "glClampColor"(arg0: integer, arg1: integer): void
public static "glGetBooleani_v"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glGetIntegeri_v"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetIntegeri_v"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glBlitFramebuffer"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer): void
public static "glIsEnabledi"(arg0: integer, arg1: integer): boolean
public static "glGenerateMipmap"(arg0: integer): void
public static "glIsVertexArray"(arg0: integer): boolean
public static "glMapBufferRange"(arg0: integer, arg1: long, arg2: long, arg3: integer): $ByteBuffer
public static "glMapBufferRange"(arg0: integer, arg1: long, arg2: long, arg3: integer, arg4: $ByteBuffer$Type): $ByteBuffer
public static "glIsFramebuffer"(arg0: integer): boolean
public static "glBindBufferRange"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "glGenRenderbuffers"(arg0: $IntBuffer$Type): void
public static "glGenRenderbuffers"(): integer
public static "glGenRenderbuffers"(arg0: (integer)[]): void
public static "glBindRenderbuffer"(arg0: integer, arg1: integer): void
public static "glClearBufferiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glClearBufferiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glClearBufferuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glClearBufferuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glClearBufferfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glClearBufferfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glUniform1uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform1uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI2iv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI2iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform2uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform2uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI3ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glVertexAttribI1iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI1iv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI4iv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI4iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI1i"(arg0: integer, arg1: integer): void
public static "glVertexAttribI4ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glUniform2ui"(arg0: integer, arg1: integer, arg2: integer): void
public static "glUniform1ui"(arg0: integer, arg1: integer): void
public static "glUniform3uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform3uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI2i"(arg0: integer, arg1: integer, arg2: integer): void
public static "glVertexAttribI3iv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttribI3iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform3ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glUniform4uiv"(arg0: integer, arg1: (integer)[]): void
public static "glUniform4uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttribI4sv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttribI4sv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glGetUniformuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetUniformuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glVertexAttribI3i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glVertexAttribI2ui"(arg0: integer, arg1: integer, arg2: integer): void
public static "glVertexAttribI4i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glVertexAttribI1ui"(arg0: integer, arg1: integer): void
public static "glVertexAttribI4bv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glUniform4ui"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glClearBufferfi"(arg0: integer, arg1: integer, arg2: float, arg3: integer): void
public static "glGetFramebufferAttachmentParameteri"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglGetFramebufferAttachmentParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL30C$Type = ($GL30C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL30C_ = $GL30C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL31C" {
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GL30C, $GL30C$Type} from "packages/org/lwjgl/opengl/$GL30C"

export class $GL31C extends $GL30C {
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


public static "glGetActiveUniformBlockiv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetActiveUniformBlockiv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glUniformBlockBinding"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetActiveUniformBlockName"(arg0: integer, arg1: integer): string
public static "glGetActiveUniformBlockName"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $ByteBuffer$Type): void
public static "glGetActiveUniformBlockName"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $ByteBuffer$Type): void
public static "glGetActiveUniformBlockName"(arg0: integer, arg1: integer, arg2: integer): string
public static "glGetUniformBlockIndex"(arg0: integer, arg1: $ByteBuffer$Type): integer
public static "glGetUniformBlockIndex"(arg0: integer, arg1: charseq): integer
public static "glDrawElementsInstanced"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer): void
public static "glDrawElementsInstanced"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer): void
public static "glDrawElementsInstanced"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer): void
public static "glDrawElementsInstanced"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type, arg3: integer): void
public static "glDrawElementsInstanced"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer): void
public static "glCopyBufferSubData"(arg0: integer, arg1: integer, arg2: long, arg3: long, arg4: long): void
public static "glGetUniformIndices"(arg0: integer, arg1: charseq): integer
public static "glGetUniformIndices"(arg0: integer, arg1: (charseq)[], arg2: $IntBuffer$Type): void
public static "glGetUniformIndices"(arg0: integer, arg1: $PointerBuffer$Type, arg2: (integer)[]): void
public static "glGetUniformIndices"(arg0: integer, arg1: $PointerBuffer$Type, arg2: $IntBuffer$Type): void
public static "glDrawArraysInstanced"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glPrimitiveRestartIndex"(arg0: integer): void
public static "glGetActiveUniformsiv"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: (integer)[]): void
public static "glGetActiveUniformsiv"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetActiveUniformName"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $ByteBuffer$Type): void
public static "glGetActiveUniformName"(arg0: integer, arg1: integer): string
public static "glGetActiveUniformName"(arg0: integer, arg1: integer, arg2: integer): string
public static "glGetActiveUniformName"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $ByteBuffer$Type): void
public static "nglGetActiveUniformBlockName"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "nglDrawElementsInstanced"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: integer): void
public static "glGetActiveUniformsi"(arg0: integer, arg1: integer, arg2: integer): integer
public static "nglGetActiveUniformName"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long): void
public static "nglGetUniformBlockIndex"(arg0: integer, arg1: long): integer
public static "nglGetActiveUniformBlockiv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetActiveUniformsiv"(arg0: integer, arg1: integer, arg2: long, arg3: integer, arg4: long): void
public static "nglGetUniformIndices"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "glGetActiveUniformBlocki"(arg0: integer, arg1: integer, arg2: integer): integer
public static "glTexBuffer"(arg0: integer, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL31C$Type = ($GL31C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL31C_ = $GL31C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL20C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GL15C, $GL15C$Type} from "packages/org/lwjgl/opengl/$GL15C"

export class $GL20C extends $GL15C {
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


public static "glGetProgramInfoLog"(arg0: integer): string
public static "glGetProgramInfoLog"(arg0: integer, arg1: $IntBuffer$Type, arg2: $ByteBuffer$Type): void
public static "glGetProgramInfoLog"(arg0: integer, arg1: integer): string
public static "glGetProgramInfoLog"(arg0: integer, arg1: (integer)[], arg2: $ByteBuffer$Type): void
public static "glGetUniformLocation"(arg0: integer, arg1: $ByteBuffer$Type): integer
public static "glGetUniformLocation"(arg0: integer, arg1: charseq): integer
public static "glVertexAttrib4Nubv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glGetAttachedShaders"(arg0: integer, arg1: (integer)[], arg2: (integer)[]): void
public static "glGetAttachedShaders"(arg0: integer, arg1: $IntBuffer$Type, arg2: $IntBuffer$Type): void
public static "glStencilFuncSeparate"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glStencilMaskSeparate"(arg0: integer, arg1: integer): void
public static "glGetAttribLocation"(arg0: integer, arg1: $ByteBuffer$Type): integer
public static "glGetAttribLocation"(arg0: integer, arg1: charseq): integer
public static "glBindAttribLocation"(arg0: integer, arg1: integer, arg2: charseq): void
public static "glBindAttribLocation"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glVertexAttrib4Nusv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib4Nusv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: long): void
public static "glVertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: $IntBuffer$Type): void
public static "glVertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: $ShortBuffer$Type): void
public static "glVertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: $ByteBuffer$Type): void
public static "glVertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: $FloatBuffer$Type): void
public static "glGetVertexAttribfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetVertexAttribfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glEnableVertexAttribArray"(arg0: integer): void
public static "glVertexAttrib4Nuiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttrib4Nuiv"(arg0: integer, arg1: (integer)[]): void
public static "glGetVertexAttribiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetVertexAttribiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetVertexAttribdv"(arg0: integer, arg1: integer, arg2: $DoubleBuffer$Type): void
public static "glGetVertexAttribdv"(arg0: integer, arg1: integer, arg2: (double)[]): void
public static "glGetVertexAttribPointerv"(arg0: integer, arg1: integer, arg2: $PointerBuffer$Type): void
public static "glBlendEquationSeparate"(arg0: integer, arg1: integer): void
public static "glDisableVertexAttribArray"(arg0: integer): void
public static "glStencilOpSeparate"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glGetProgrami"(arg0: integer, arg1: integer): integer
public static "nglShaderSource"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "nglGetUniformfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform4iv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetShaderiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetUniformf"(arg0: integer, arg1: integer): float
public static "nglGetUniformiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetUniformi"(arg0: integer, arg1: integer): integer
public static "nglUniform4fv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform1fv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform3fv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetProgramiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform2fv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform3iv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniform1iv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetShaderi"(arg0: integer, arg1: integer): integer
public static "nglUniform2iv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglVertexAttrib1fv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib3fv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib1sv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib2sv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4fv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib2dv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib1dv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4dv"(arg0: integer, arg1: long): void
public static "nglGetShaderSource"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "nglVertexAttrib2fv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib3sv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4iv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4sv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4bv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib3dv"(arg0: integer, arg1: long): void
public static "nglGetActiveAttrib"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long, arg5: long, arg6: long): void
public static "glGetVertexAttribi"(arg0: integer, arg1: integer): integer
public static "nglDrawBuffers"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4Nuiv"(arg0: integer, arg1: long): void
public static "nglVertexAttribPointer"(arg0: integer, arg1: integer, arg2: integer, arg3: boolean, arg4: integer, arg5: long): void
public static "nglBindAttribLocation"(arg0: integer, arg1: integer, arg2: long): void
public static "nglVertexAttrib4Nusv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4Nbv"(arg0: integer, arg1: long): void
public static "nglUniformMatrix4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGetVertexAttribfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglUniformMatrix3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglVertexAttrib4usv"(arg0: integer, arg1: long): void
public static "nglGetAttachedShaders"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "nglVertexAttrib4Nsv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4Niv"(arg0: integer, arg1: long): void
public static "nglVertexAttrib4Nubv"(arg0: integer, arg1: long): void
public static "nglGetVertexAttribPointerv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetShaderInfoLog"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "nglGetProgramInfoLog"(arg0: integer, arg1: integer, arg2: long, arg3: long): void
public static "nglVertexAttrib4uiv"(arg0: integer, arg1: long): void
public static "nglGetVertexAttribdv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetAttribLocation"(arg0: integer, arg1: long): integer
public static "nglGetUniformLocation"(arg0: integer, arg1: long): integer
public static "glGetVertexAttribPointer"(arg0: integer, arg1: integer): long
public static "nglVertexAttrib4ubv"(arg0: integer, arg1: long): void
public static "nglUniformMatrix2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglGetVertexAttribiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetActiveUniform"(arg0: integer, arg1: integer, arg2: integer, arg3: long, arg4: long, arg5: long, arg6: long): void
public static "glUniform3i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glIsProgram"(arg0: integer): boolean
public static "glUniform1f"(arg0: integer, arg1: float): void
public static "glUniform1i"(arg0: integer, arg1: integer): void
public static "glUniform4i"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public static "glUniform2fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glUniform2fv"(arg0: integer, arg1: (float)[]): void
public static "glCreateProgram"(): integer
public static "glUniform3fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glUniform3fv"(arg0: integer, arg1: (float)[]): void
public static "glUniform4fv"(arg0: integer, arg1: (float)[]): void
public static "glUniform4fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glCreateShader"(arg0: integer): integer
public static "glUniform3f"(arg0: integer, arg1: float, arg2: float, arg3: float): void
public static "glUniform1fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glUniform1fv"(arg0: integer, arg1: (float)[]): void
public static "glUniform2f"(arg0: integer, arg1: float, arg2: float): void
public static "glDeleteShader"(arg0: integer): void
public static "glIsShader"(arg0: integer): boolean
public static "glShaderSource"(arg0: integer, ...arg1: (charseq)[]): void
public static "glShaderSource"(arg0: integer, arg1: $PointerBuffer$Type, arg2: (integer)[]): void
public static "glShaderSource"(arg0: integer, arg1: charseq): void
public static "glShaderSource"(arg0: integer, arg1: $PointerBuffer$Type, arg2: $IntBuffer$Type): void
public static "glDeleteProgram"(arg0: integer): void
public static "glDetachShader"(arg0: integer, arg1: integer): void
public static "glLinkProgram"(arg0: integer): void
public static "glAttachShader"(arg0: integer, arg1: integer): void
public static "glUseProgram"(arg0: integer): void
public static "glValidateProgram"(arg0: integer): void
public static "glCompileShader"(arg0: integer): void
public static "glUniform4f"(arg0: integer, arg1: float, arg2: float, arg3: float, arg4: float): void
public static "glUniform2i"(arg0: integer, arg1: integer, arg2: integer): void
public static "glGetUniformiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetUniformiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glVertexAttrib4s"(arg0: integer, arg1: short, arg2: short, arg3: short, arg4: short): void
public static "glGetActiveUniform"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type, arg5: $ByteBuffer$Type): void
public static "glGetActiveUniform"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type): string
public static "glGetActiveUniform"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: (integer)[], arg4: (integer)[], arg5: $ByteBuffer$Type): void
public static "glGetActiveUniform"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type): string
public static "glVertexAttrib4d"(arg0: integer, arg1: double, arg2: double, arg3: double, arg4: double): void
public static "glGetShaderInfoLog"(arg0: integer, arg1: $IntBuffer$Type, arg2: $ByteBuffer$Type): void
public static "glGetShaderInfoLog"(arg0: integer, arg1: integer): string
public static "glGetShaderInfoLog"(arg0: integer): string
public static "glGetShaderInfoLog"(arg0: integer, arg1: (integer)[], arg2: $ByteBuffer$Type): void
public static "glVertexAttrib1fv"(arg0: integer, arg1: (float)[]): void
public static "glVertexAttrib1fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glUniformMatrix4fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix4fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glGetProgramiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetProgramiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glUniform1iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform1iv"(arg0: integer, arg1: (integer)[]): void
public static "glGetShaderiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetShaderiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glVertexAttrib2f"(arg0: integer, arg1: float, arg2: float): void
public static "glVertexAttrib1f"(arg0: integer, arg1: float): void
public static "glVertexAttrib1d"(arg0: integer, arg1: double): void
public static "glVertexAttrib1sv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttrib1sv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib4Nub"(arg0: integer, arg1: byte, arg2: byte, arg3: byte, arg4: byte): void
public static "glVertexAttrib1dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glVertexAttrib1dv"(arg0: integer, arg1: (double)[]): void
public static "glUniform2iv"(arg0: integer, arg1: (integer)[]): void
public static "glUniform2iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttrib2fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glVertexAttrib2fv"(arg0: integer, arg1: (float)[]): void
public static "glGetUniformfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glGetUniformfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glVertexAttrib1s"(arg0: integer, arg1: short): void
public static "glVertexAttrib2d"(arg0: integer, arg1: double, arg2: double): void
public static "glVertexAttrib3d"(arg0: integer, arg1: double, arg2: double, arg3: double): void
public static "glUniform3iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniform3iv"(arg0: integer, arg1: (integer)[]): void
public static "glUniformMatrix3fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix3fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniform4iv"(arg0: integer, arg1: (integer)[]): void
public static "glUniform4iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glUniformMatrix2fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix2fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glVertexAttrib3f"(arg0: integer, arg1: float, arg2: float, arg3: float): void
public static "glVertexAttrib3s"(arg0: integer, arg1: short, arg2: short, arg3: short): void
public static "glGetShaderSource"(arg0: integer, arg1: (integer)[], arg2: $ByteBuffer$Type): void
public static "glGetShaderSource"(arg0: integer, arg1: integer): string
public static "glGetShaderSource"(arg0: integer, arg1: $IntBuffer$Type, arg2: $ByteBuffer$Type): void
public static "glGetShaderSource"(arg0: integer): string
public static "glVertexAttrib2s"(arg0: integer, arg1: short, arg2: short): void
public static "glVertexAttrib4f"(arg0: integer, arg1: float, arg2: float, arg3: float, arg4: float): void
public static "glVertexAttrib3dv"(arg0: integer, arg1: (double)[]): void
public static "glVertexAttrib3dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glVertexAttrib4ubv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glVertexAttrib4dv"(arg0: integer, arg1: (double)[]): void
public static "glVertexAttrib4dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glVertexAttrib4iv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttrib4iv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttrib4fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glVertexAttrib4fv"(arg0: integer, arg1: (float)[]): void
public static "glVertexAttrib4usv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib4usv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttrib4uiv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glVertexAttrib4uiv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttrib4Nbv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glVertexAttrib2sv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttrib2sv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib2dv"(arg0: integer, arg1: (double)[]): void
public static "glVertexAttrib2dv"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glVertexAttrib4bv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glDrawBuffers"(arg0: integer): void
public static "glDrawBuffers"(arg0: (integer)[]): void
public static "glDrawBuffers"(arg0: $IntBuffer$Type): void
public static "glVertexAttrib3fv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glVertexAttrib3fv"(arg0: integer, arg1: (float)[]): void
public static "glVertexAttrib3sv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttrib3sv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib4sv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib4sv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttrib4Nsv"(arg0: integer, arg1: (short)[]): void
public static "glVertexAttrib4Nsv"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glVertexAttrib4Niv"(arg0: integer, arg1: (integer)[]): void
public static "glVertexAttrib4Niv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glGetActiveAttrib"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type, arg5: $ByteBuffer$Type): void
public static "glGetActiveAttrib"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type, arg3: $IntBuffer$Type): string
public static "glGetActiveAttrib"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: (integer)[], arg4: (integer)[], arg5: $ByteBuffer$Type): void
public static "glGetActiveAttrib"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type, arg4: $IntBuffer$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL20C$Type = ($GL20C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL20C_ = $GL20C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL21C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$GL20C, $GL20C$Type} from "packages/org/lwjgl/opengl/$GL20C"

export class $GL21C extends $GL20C {
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


public static "glUniformMatrix3x4fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniformMatrix3x4fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix4x3fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix4x3fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniformMatrix2x3fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix2x3fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniformMatrix3x2fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniformMatrix3x2fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix2x4fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniformMatrix2x4fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "glUniformMatrix4x2fv"(arg0: integer, arg1: boolean, arg2: (float)[]): void
public static "glUniformMatrix4x2fv"(arg0: integer, arg1: boolean, arg2: $FloatBuffer$Type): void
public static "nglUniformMatrix4x2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix3x4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix4x3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix3x2fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix2x4fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
public static "nglUniformMatrix2x3fv"(arg0: integer, arg1: integer, arg2: boolean, arg3: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL21C$Type = ($GL21C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL21C_ = $GL21C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL12C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$GL11C, $GL11C$Type} from "packages/org/lwjgl/opengl/$GL11C"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL12C extends $GL11C {
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


public static "glDrawRangeElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "glDrawRangeElements"(arg0: integer, arg1: integer, arg2: integer, arg3: $ByteBuffer$Type): void
public static "glDrawRangeElements"(arg0: integer, arg1: integer, arg2: integer, arg3: $ShortBuffer$Type): void
public static "glDrawRangeElements"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glDrawRangeElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glCopyTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer): void
public static "nglTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: long): void
public static "nglTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "nglDrawRangeElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: long): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $ByteBuffer$Type): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $ShortBuffer$Type): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $IntBuffer$Type): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $FloatBuffer$Type): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: (double)[]): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: (float)[]): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: (integer)[]): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: (short)[]): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: long): void
public static "glTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $DoubleBuffer$Type): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (float)[]): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (integer)[]): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (short)[]): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: (double)[]): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ShortBuffer$Type): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $DoubleBuffer$Type): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $FloatBuffer$Type): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $IntBuffer$Type): void
public static "glTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: $ByteBuffer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL12C$Type = ($GL12C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL12C_ = $GL12C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL13C" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GL12C, $GL12C$Type} from "packages/org/lwjgl/opengl/$GL12C"

export class $GL13C extends $GL12C {
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


public static "glCompressedTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glCompressedTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: long): void
public static "glGetCompressedTexImage"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glGetCompressedTexImage"(arg0: integer, arg1: integer, arg2: long): void
public static "glCompressedTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "glCompressedTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $ByteBuffer$Type): void
public static "glCompressedTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ByteBuffer$Type): void
public static "glCompressedTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glCompressedTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glCompressedTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ByteBuffer$Type): void
public static "glCompressedTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glCompressedTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $ByteBuffer$Type): void
public static "glCompressedTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glCompressedTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $ByteBuffer$Type): void
public static "nglCompressedTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: long): void
public static "nglCompressedTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "nglGetCompressedTexImage"(arg0: integer, arg1: integer, arg2: long): void
public static "nglCompressedTexImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "nglCompressedTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "nglCompressedTexSubImage3D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: integer, arg10: long): void
public static "nglCompressedTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glActiveTexture"(arg0: integer): void
public static "glSampleCoverage"(arg0: float, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL13C$Type = ($GL13C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL13C_ = $GL13C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL11C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $GL11C {
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


public static "glReadBuffer"(arg0: integer): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $IntBuffer$Type): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (float)[]): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $FloatBuffer$Type): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (short)[]): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[]): void
public static "glReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ShortBuffer$Type): void
public static "glPolygonOffset"(arg0: float, arg1: float): void
public static "glPointSize"(arg0: float): void
public static "glPolygonMode"(arg0: integer, arg1: integer): void
public static "glPixelStorei"(arg0: integer, arg1: integer): void
public static "glPixelStoref"(arg0: integer, arg1: float): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: (short)[]): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $FloatBuffer$Type): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ByteBuffer$Type): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: long): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: (double)[]): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $ShortBuffer$Type): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $IntBuffer$Type): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $DoubleBuffer$Type): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: (integer)[]): void
public static "glTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: (float)[]): void
public static "glCopyTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
public static "glCopyTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "glScissor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glStencilFunc"(arg0: integer, arg1: integer, arg2: integer): void
public static "glStencilMask"(arg0: integer): void
public static "glStencilOp"(arg0: integer, arg1: integer, arg2: integer): void
public static "glClearDepth"(arg0: double): void
public static "glClearStencil"(arg0: integer): void
public static "glViewport"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "glClearColor"(arg0: float, arg1: float, arg2: float, arg3: float): void
public static "glGetString"(arg0: integer): string
public static "glClear"(arg0: integer): void
public static "glEnable"(arg0: integer): void
public static "glGetInteger"(arg0: integer): integer
public static "glBlendFunc"(arg0: integer, arg1: integer): void
public static "nglGetTexParameterfv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetTexLevelParameterf"(arg0: integer, arg1: integer, arg2: integer): float
public static "nglGetTexParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "glTexParameteri"(arg0: integer, arg1: integer, arg2: integer): void
public static "glBindTexture"(arg0: integer, arg1: integer): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ShortBuffer$Type): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $FloatBuffer$Type): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $DoubleBuffer$Type): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $IntBuffer$Type): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (short)[]): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (float)[]): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (integer)[]): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (double)[]): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ByteBuffer$Type): void
public static "nglGetBooleanv"(arg0: integer, arg1: long): void
public static "glGetDouble"(arg0: integer): double
public static "nglGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "nglGetDoublev"(arg0: integer, arg1: long): void
public static "glGetBoolean"(arg0: integer): boolean
public static "nglGetFloatv"(arg0: integer, arg1: long): void
public static "nglDrawElements"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetPointerv"(arg0: integer, arg1: long): void
public static "nglGenTextures"(arg0: integer, arg1: long): void
public static "nglGetIntegerv"(arg0: integer, arg1: long): void
public static "nglDeleteTextures"(arg0: integer, arg1: long): void
public static "glGetFloat"(arg0: integer): float
public static "glGetPointer"(arg0: integer): long
public static "nglGetString"(arg0: integer): long
public static "nglTexImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "nglTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "nglReadPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glGetTexParameteri"(arg0: integer, arg1: integer): integer
public static "glGetTexParameterf"(arg0: integer, arg1: integer): float
public static "nglTexImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: long): void
public static "nglTexParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglTexParameterfv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glGetDoublev"(arg0: integer, arg1: $DoubleBuffer$Type): void
public static "glGetDoublev"(arg0: integer, arg1: (double)[]): void
public static "glDeleteTextures"(arg0: integer): void
public static "glDeleteTextures"(arg0: $IntBuffer$Type): void
public static "glDeleteTextures"(arg0: (integer)[]): void
public static "glFinish"(): void
public static "glFlush"(): void
public static "glGetBooleanv"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glGetFloatv"(arg0: integer, arg1: (float)[]): void
public static "glGetFloatv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glFrontFace"(arg0: integer): void
public static "glCullFace"(arg0: integer): void
public static "glDepthFunc"(arg0: integer): void
public static "glDrawArrays"(arg0: integer, arg1: integer, arg2: integer): void
public static "glDepthMask"(arg0: boolean): void
public static "glDrawElements"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): void
public static "glDrawElements"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glDrawElements"(arg0: integer, arg1: $ShortBuffer$Type): void
public static "glDrawElements"(arg0: integer, arg1: $ByteBuffer$Type): void
public static "glDrawElements"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glDepthRange"(arg0: double, arg1: double): void
public static "glColorMask"(arg0: boolean, arg1: boolean, arg2: boolean, arg3: boolean): void
public static "glDrawBuffer"(arg0: integer): void
public static "glIsEnabled"(arg0: integer): boolean
public static "glHint"(arg0: integer, arg1: integer): void
public static "glGetPointerv"(arg0: integer, arg1: $PointerBuffer$Type): void
public static "glIsTexture"(arg0: integer): boolean
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (short)[]): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ShortBuffer$Type): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[]): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: long): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $DoubleBuffer$Type): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ByteBuffer$Type): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IntBuffer$Type): void
public static "glGetTexImage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $FloatBuffer$Type): void
public static "glLogicOp"(arg0: integer): void
public static "glLineWidth"(arg0: float): void
public static "glCopyTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public static "glCopyTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public static "glDisable"(arg0: integer): void
public static "glGetIntegerv"(arg0: integer, arg1: (integer)[]): void
public static "glGetIntegerv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glGetError"(): integer
public static "nglGetTexLevelParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "nglGetTexLevelParameterfv"(arg0: integer, arg1: integer, arg2: integer, arg3: long): void
public static "glGetTexLevelParameteri"(arg0: integer, arg1: integer, arg2: integer): integer
public static "glGenTextures"(arg0: $IntBuffer$Type): void
public static "glGenTextures"(): integer
public static "glGenTextures"(arg0: (integer)[]): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (float)[]): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ByteBuffer$Type): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: long): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[]): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (short)[]): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $FloatBuffer$Type): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $DoubleBuffer$Type): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (double)[]): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $IntBuffer$Type): void
public static "glTexSubImage1D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $ShortBuffer$Type): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (float)[]): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (double)[]): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (integer)[]): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: (short)[]): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ByteBuffer$Type): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $FloatBuffer$Type): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $ShortBuffer$Type): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $IntBuffer$Type): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: long): void
public static "glTexSubImage2D"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: $DoubleBuffer$Type): void
public static "glTexParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glTexParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glTexParameterf"(arg0: integer, arg1: integer, arg2: float): void
public static "glTexParameterfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
public static "glTexParameterfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetTexLevelParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: $IntBuffer$Type): void
public static "glGetTexLevelParameteriv"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[]): void
public static "glGetTexLevelParameterfv"(arg0: integer, arg1: integer, arg2: integer, arg3: (float)[]): void
public static "glGetTexLevelParameterfv"(arg0: integer, arg1: integer, arg2: integer, arg3: $FloatBuffer$Type): void
public static "glGetTexParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetTexParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetTexParameterfv"(arg0: integer, arg1: integer, arg2: (float)[]): void
public static "glGetTexParameterfv"(arg0: integer, arg1: integer, arg2: $FloatBuffer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL11C$Type = ($GL11C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL11C_ = $GL11C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL14C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$GL13C, $GL13C$Type} from "packages/org/lwjgl/opengl/$GL13C"

export class $GL14C extends $GL13C {
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


public static "glMultiDrawElements"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer, arg3: $PointerBuffer$Type): void
public static "glMultiDrawElements"(arg0: integer, arg1: (integer)[], arg2: integer, arg3: $PointerBuffer$Type): void
public static "glBlendFuncSeparate"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public static "nglMultiDrawArrays"(arg0: integer, arg1: long, arg2: long, arg3: integer): void
public static "nglMultiDrawElements"(arg0: integer, arg1: long, arg2: integer, arg3: long, arg4: integer): void
public static "nglPointParameteriv"(arg0: integer, arg1: long): void
public static "nglPointParameterfv"(arg0: integer, arg1: long): void
public static "glMultiDrawArrays"(arg0: integer, arg1: $IntBuffer$Type, arg2: $IntBuffer$Type): void
public static "glMultiDrawArrays"(arg0: integer, arg1: (integer)[], arg2: (integer)[]): void
public static "glPointParameteriv"(arg0: integer, arg1: $IntBuffer$Type): void
public static "glPointParameteriv"(arg0: integer, arg1: (integer)[]): void
public static "glBlendEquation"(arg0: integer): void
public static "glPointParameterf"(arg0: integer, arg1: float): void
public static "glPointParameterfv"(arg0: integer, arg1: $FloatBuffer$Type): void
public static "glPointParameterfv"(arg0: integer, arg1: (float)[]): void
public static "glBlendColor"(arg0: float, arg1: float, arg2: float, arg3: float): void
public static "glPointParameteri"(arg0: integer, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL14C$Type = ($GL14C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL14C_ = $GL14C$Type;
}}
declare module "packages/org/lwjgl/opengl/$GL15C" {
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GL14C, $GL14C$Type} from "packages/org/lwjgl/opengl/$GL14C"

export class $GL15C extends $GL14C {
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


public static "glGetQueryObjectuiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetQueryObjectuiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetQueryObjectuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetBufferParameteriv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetBufferParameteriv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetBufferPointerv"(arg0: integer, arg1: integer, arg2: $PointerBuffer$Type): void
public static "nglBufferData"(arg0: integer, arg1: long, arg2: long, arg3: integer): void
public static "nglMapBuffer"(arg0: integer, arg1: integer): long
public static "glGetBufferPointer"(arg0: integer, arg1: integer): long
public static "nglGenQueries"(arg0: integer, arg1: long): void
public static "nglGetQueryiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglDeleteQueries"(arg0: integer, arg1: long): void
public static "glGetQueryi"(arg0: integer, arg1: integer): integer
public static "nglBufferSubData"(arg0: integer, arg1: long, arg2: long, arg3: long): void
public static "nglGenBuffers"(arg0: integer, arg1: long): void
public static "nglDeleteBuffers"(arg0: integer, arg1: long): void
public static "glGetQueryObjecti"(arg0: integer, arg1: integer): integer
public static "glGetQueryObjectui"(arg0: integer, arg1: integer): integer
public static "nglGetBufferSubData"(arg0: integer, arg1: long, arg2: long, arg3: long): void
public static "nglGetBufferParameteriv"(arg0: integer, arg1: integer, arg2: long): void
public static "glGetBufferParameteri"(arg0: integer, arg1: integer): integer
public static "nglGetBufferPointerv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetQueryObjectiv"(arg0: integer, arg1: integer, arg2: long): void
public static "nglGetQueryObjectuiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glBindBuffer"(arg0: integer, arg1: integer): void
public static "glGenBuffers"(arg0: $IntBuffer$Type): void
public static "glGenBuffers"(): integer
public static "glGenBuffers"(arg0: (integer)[]): void
public static "glMapBuffer"(arg0: integer, arg1: integer): $ByteBuffer
public static "glMapBuffer"(arg0: integer, arg1: integer, arg2: long, arg3: $ByteBuffer$Type): $ByteBuffer
public static "glMapBuffer"(arg0: integer, arg1: integer, arg2: $ByteBuffer$Type): $ByteBuffer
public static "glUnmapBuffer"(arg0: integer): boolean
public static "glGenQueries"(arg0: $IntBuffer$Type): void
public static "glGenQueries"(): integer
public static "glGenQueries"(arg0: (integer)[]): void
public static "glDeleteBuffers"(arg0: (integer)[]): void
public static "glDeleteBuffers"(arg0: integer): void
public static "glDeleteBuffers"(arg0: $IntBuffer$Type): void
public static "glIsBuffer"(arg0: integer): boolean
public static "glBufferData"(arg0: integer, arg1: $ShortBuffer$Type, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: $IntBuffer$Type, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: $ByteBuffer$Type, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: $LongBuffer$Type, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: $FloatBuffer$Type, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: $DoubleBuffer$Type, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: long, arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: (double)[], arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: (float)[], arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: (long)[], arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: (integer)[], arg2: integer): void
public static "glBufferData"(arg0: integer, arg1: (short)[], arg2: integer): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: (short)[]): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: $DoubleBuffer$Type): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: $FloatBuffer$Type): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: $LongBuffer$Type): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: (double)[]): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: (float)[]): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: (long)[]): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: (integer)[]): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: $ByteBuffer$Type): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: $IntBuffer$Type): void
public static "glBufferSubData"(arg0: integer, arg1: long, arg2: $ShortBuffer$Type): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: (long)[]): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: (integer)[]): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: (short)[]): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: $DoubleBuffer$Type): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: (float)[]): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: $LongBuffer$Type): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: $IntBuffer$Type): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: (double)[]): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: $FloatBuffer$Type): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: $ByteBuffer$Type): void
public static "glGetBufferSubData"(arg0: integer, arg1: long, arg2: $ShortBuffer$Type): void
public static "glGetQueryObjectiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetQueryObjectiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glGetQueryObjectiv"(arg0: integer, arg1: integer, arg2: long): void
public static "glDeleteQueries"(arg0: integer): void
public static "glDeleteQueries"(arg0: (integer)[]): void
public static "glDeleteQueries"(arg0: $IntBuffer$Type): void
public static "glGetQueryiv"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public static "glGetQueryiv"(arg0: integer, arg1: integer, arg2: $IntBuffer$Type): void
public static "glEndQuery"(arg0: integer): void
public static "glIsQuery"(arg0: integer): boolean
public static "glBeginQuery"(arg0: integer, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GL15C$Type = ($GL15C);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GL15C_ = $GL15C$Type;
}}
declare module "packages/org/lwjgl/system/$StructBuffer" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CustomBuffer, $CustomBuffer$Type} from "packages/org/lwjgl/system/$CustomBuffer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $StructBuffer<T extends $Struct, SELF extends $StructBuffer<(T), (SELF)>> extends $CustomBuffer<(SELF)> implements $Iterable<(T)> {


public "get"(arg0: integer, arg1: T): SELF
public "get"(arg0: T): SELF
public "get"(arg0: integer): T
public "get"(): T
public "put"(arg0: integer, arg1: T): SELF
public "put"(arg0: T): SELF
public "iterator"(): $Iterator<(T)>
public "apply"(arg0: integer, arg1: $Consumer$Type<(T)>): SELF
public "apply"(arg0: $Consumer$Type<(T)>): SELF
public "stream"(): $Stream<(T)>
public "spliterator"(): $Spliterator<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "parallelStream"(): $Stream<(T)>
public "sizeof"(): integer
[Symbol.iterator](): IterableIterator<T>;
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructBuffer$Type<T, SELF> = ($StructBuffer<(T), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructBuffer_<T, SELF> = $StructBuffer$Type<(T), (SELF)>;
}}
declare module "packages/org/lwjgl/$CLongBuffer" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$CustomBuffer, $CustomBuffer$Type} from "packages/org/lwjgl/system/$CustomBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $CLongBuffer extends $CustomBuffer<($CLongBuffer)> implements $Comparable<($CLongBuffer)> {


public "get"(arg0: integer): long
public "get"(): long
public static "get"(arg0: $ByteBuffer$Type): long
public static "get"(arg0: $ByteBuffer$Type, arg1: integer): long
public "get"(arg0: (long)[]): $CLongBuffer
public "get"(arg0: (long)[], arg1: integer, arg2: integer): $CLongBuffer
public "put"(arg0: (long)[], arg1: integer, arg2: integer): $CLongBuffer
public static "put"(arg0: $ByteBuffer$Type, arg1: integer, arg2: long): void
public "put"(arg0: integer, arg1: long): $CLongBuffer
public "put"(arg0: (long)[]): $CLongBuffer
public static "put"(arg0: $ByteBuffer$Type, arg1: long): void
public "put"(arg0: long): $CLongBuffer
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "compareTo"(arg0: $CLongBuffer$Type): integer
public static "create"(arg0: long, arg1: integer): $CLongBuffer
public static "create"(arg0: $ByteBuffer$Type): $CLongBuffer
public static "allocateDirect"(arg0: integer): $CLongBuffer
public "sizeof"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CLongBuffer$Type = ($CLongBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CLongBuffer_ = $CLongBuffer$Type;
}}
declare module "packages/org/lwjgl/system/$Pointer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Pointer {

 "address"(): long

(): long
}

export namespace $Pointer {
const POINTER_SIZE: integer
const POINTER_SHIFT: integer
const CLONG_SIZE: integer
const CLONG_SHIFT: integer
const BITS32: boolean
const BITS64: boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pointer$Type = ($Pointer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pointer_ = $Pointer$Type;
}}
declare module "packages/org/lwjgl/stb/$STBIWriteCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $STBIWriteCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: long, arg2: integer): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: long, arg2: integer): void
}

export namespace $STBIWriteCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $STBIWriteCallbackI$Type = ($STBIWriteCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $STBIWriteCallbackI_ = $STBIWriteCallbackI$Type;
}}
declare module "packages/org/lwjgl/system/libffi/$FFICIF" {
import {$FFIType, $FFIType$Type} from "packages/org/lwjgl/system/libffi/$FFIType"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$FFICIF$Buffer, $FFICIF$Buffer$Type} from "packages/org/lwjgl/system/libffi/$FFICIF$Buffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $FFICIF extends $Struct implements $NativeResource {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "ABI": integer
static readonly "NARGS": integer
static readonly "ARG_TYPES": integer
static readonly "RTYPE": integer
static readonly "BYTES": integer
static readonly "FLAGS": integer

constructor(arg0: $ByteBuffer$Type)

public "flags"(): integer
public "bytes"(): integer
public "nargs"(): integer
public "rtype"(): $FFIType
public static "nrtype"(arg0: long): $FFIType
public "abi"(): integer
public static "create"(): $FFICIF
public static "create"(arg0: long): $FFICIF
public static "create"(arg0: integer): $FFICIF$Buffer
public static "create"(arg0: long, arg1: integer): $FFICIF$Buffer
public static "nbytes"(arg0: long): integer
public static "malloc"(arg0: $MemoryStack$Type): $FFICIF
public static "malloc"(): $FFICIF
public static "malloc"(arg0: integer, arg1: $MemoryStack$Type): $FFICIF$Buffer
public static "malloc"(arg0: integer): $FFICIF$Buffer
public "sizeof"(): integer
public static "calloc"(arg0: integer, arg1: $MemoryStack$Type): $FFICIF$Buffer
public static "calloc"(arg0: integer): $FFICIF$Buffer
public static "calloc"(arg0: $MemoryStack$Type): $FFICIF
public static "calloc"(): $FFICIF
public static "createSafe"(arg0: long): $FFICIF
public static "createSafe"(arg0: long, arg1: integer): $FFICIF$Buffer
public static "nabi"(arg0: long): integer
public static "nnargs"(arg0: long): integer
public "arg_types"(arg0: integer): $PointerBuffer
public static "narg_types"(arg0: long, arg1: integer): $PointerBuffer
public static "nflags"(arg0: long): integer
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FFICIF$Type = ($FFICIF);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FFICIF_ = $FFICIF$Type;
}}
declare module "packages/org/lwjgl/system/libffi/$FFIType$Buffer" {
import {$FFIType, $FFIType$Type} from "packages/org/lwjgl/system/libffi/$FFIType"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $FFIType$Buffer extends $StructBuffer<($FFIType), ($FFIType$Buffer)> implements $NativeResource {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "type"(arg0: short): $FFIType$Buffer
public "type"(): short
public "size"(arg0: long): $FFIType$Buffer
public "size"(): long
public "elements"(arg0: $PointerBuffer$Type): $FFIType$Buffer
public "elements"(arg0: integer): $PointerBuffer
public "alignment"(): short
public "alignment"(arg0: short): $FFIType$Buffer
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FFIType$Buffer$Type = ($FFIType$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FFIType$Buffer_ = $FFIType$Buffer$Type;
}}
declare module "packages/org/lwjgl/stb/$STBRPRect$Buffer" {
import {$STBRPRect, $STBRPRect$Type} from "packages/org/lwjgl/stb/$STBRPRect"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $STBRPRect$Buffer extends $StructBuffer<($STBRPRect), ($STBRPRect$Buffer)> implements $NativeResource {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "x"(): integer
public "x"(arg0: integer): $STBRPRect$Buffer
public "h"(): integer
public "h"(arg0: integer): $STBRPRect$Buffer
public "id"(): integer
public "id"(arg0: integer): $STBRPRect$Buffer
public "w"(): integer
public "w"(arg0: integer): $STBRPRect$Buffer
public "y"(): integer
public "y"(arg0: integer): $STBRPRect$Buffer
public "was_packed"(arg0: boolean): $STBRPRect$Buffer
public "was_packed"(): boolean
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $STBRPRect$Buffer$Type = ($STBRPRect$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $STBRPRect$Buffer_ = $STBRPRect$Buffer$Type;
}}
declare module "packages/org/lwjgl/system/$MemoryStack" {
import {$DoubleBuffer, $DoubleBuffer$Type} from "packages/java/nio/$DoubleBuffer"
import {$ShortBuffer, $ShortBuffer$Type} from "packages/java/nio/$ShortBuffer"
import {$LongBuffer, $LongBuffer$Type} from "packages/java/nio/$LongBuffer"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$FloatBuffer, $FloatBuffer$Type} from "packages/java/nio/$FloatBuffer"
import {$Pointer, $Pointer$Type} from "packages/org/lwjgl/system/$Pointer"
import {$Buffer, $Buffer$Type} from "packages/java/nio/$Buffer"
import {$CLongBuffer, $CLongBuffer$Type} from "packages/org/lwjgl/$CLongBuffer"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$IntBuffer, $IntBuffer$Type} from "packages/java/nio/$IntBuffer"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$Pointer$Default, $Pointer$Default$Type} from "packages/org/lwjgl/system/$Pointer$Default"

export class $MemoryStack extends $Pointer$Default implements $AutoCloseable {


public "UTF16"(arg0: charseq): $ByteBuffer
public "UTF16"(arg0: charseq, arg1: boolean): $ByteBuffer
public "bytes"(arg0: byte): $ByteBuffer
public "bytes"(arg0: byte, arg1: byte, arg2: byte, arg3: byte): $ByteBuffer
public "bytes"(...arg0: (byte)[]): $ByteBuffer
public "bytes"(arg0: byte, arg1: byte): $ByteBuffer
public "bytes"(arg0: byte, arg1: byte, arg2: byte): $ByteBuffer
public "close"(): void
public "getSize"(): integer
public "getAddress"(): long
public static "create"(arg0: integer): $MemoryStack
public static "create"(arg0: $ByteBuffer$Type): $MemoryStack
public static "create"(): $MemoryStack
public "ASCII"(arg0: charseq, arg1: boolean): $ByteBuffer
public "ASCII"(arg0: charseq): $ByteBuffer
public "UTF8"(arg0: charseq, arg1: boolean): $ByteBuffer
public "UTF8"(arg0: charseq): $ByteBuffer
public "push"(): $MemoryStack
public "pop"(): $MemoryStack
public "ints"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $IntBuffer
public "ints"(arg0: integer, arg1: integer, arg2: integer): $IntBuffer
public "ints"(arg0: integer, arg1: integer): $IntBuffer
public "ints"(arg0: integer): $IntBuffer
public "ints"(...arg0: (integer)[]): $IntBuffer
public "longs"(arg0: long, arg1: long, arg2: long): $LongBuffer
public "longs"(arg0: long, arg1: long, arg2: long, arg3: long): $LongBuffer
public "longs"(arg0: long, arg1: long): $LongBuffer
public "longs"(arg0: long): $LongBuffer
public "longs"(...arg0: (long)[]): $LongBuffer
public "doubles"(arg0: double, arg1: double, arg2: double): $DoubleBuffer
public "doubles"(arg0: double, arg1: double): $DoubleBuffer
public "doubles"(arg0: double): $DoubleBuffer
public "doubles"(...arg0: (double)[]): $DoubleBuffer
public "doubles"(arg0: double, arg1: double, arg2: double, arg3: double): $DoubleBuffer
public "malloc"(arg0: integer): $ByteBuffer
public "malloc"(arg0: integer, arg1: integer): $ByteBuffer
public static "stackPush"(): $MemoryStack
public "mallocPointer"(arg0: integer): $PointerBuffer
public static "stackGet"(): $MemoryStack
public "calloc"(arg0: integer): $ByteBuffer
public "calloc"(arg0: integer, arg1: integer): $ByteBuffer
public "nmalloc"(arg0: integer, arg1: integer): long
public "nmalloc"(arg0: integer): long
public "ncalloc"(arg0: integer, arg1: integer, arg2: integer): long
public "setPointer"(arg0: integer): void
public "callocInt"(arg0: integer): $IntBuffer
public "getPointer"(): integer
public "nUTF8"(arg0: charseq, arg1: boolean): integer
public "getPointerAddress"(): long
public "nASCII"(arg0: charseq, arg1: boolean): integer
public "pointers"(...arg0: (long)[]): $PointerBuffer
public "pointers"(arg0: $Pointer$Type): $PointerBuffer
public "pointers"(arg0: $Pointer$Type, arg1: $Pointer$Type): $PointerBuffer
public "pointers"(arg0: $Pointer$Type, arg1: $Pointer$Type, arg2: $Pointer$Type): $PointerBuffer
public "pointers"(arg0: long): $PointerBuffer
public "pointers"(arg0: long, arg1: long): $PointerBuffer
public "pointers"(arg0: long, arg1: long, arg2: long): $PointerBuffer
public "pointers"(arg0: long, arg1: long, arg2: long, arg3: long): $PointerBuffer
public "pointers"(arg0: $Buffer$Type, arg1: $Buffer$Type): $PointerBuffer
public "pointers"(arg0: $Buffer$Type, arg1: $Buffer$Type, arg2: $Buffer$Type): $PointerBuffer
public "pointers"(arg0: $Buffer$Type, arg1: $Buffer$Type, arg2: $Buffer$Type, arg3: $Buffer$Type): $PointerBuffer
public "pointers"(...arg0: ($Buffer$Type)[]): $PointerBuffer
public "pointers"(arg0: $Buffer$Type): $PointerBuffer
public "pointers"(...arg0: ($Pointer$Type)[]): $PointerBuffer
public "pointers"(arg0: $Pointer$Type, arg1: $Pointer$Type, arg2: $Pointer$Type, arg3: $Pointer$Type): $PointerBuffer
public "mallocInt"(arg0: integer): $IntBuffer
public "UTF16Safe"(arg0: charseq): $ByteBuffer
public "UTF16Safe"(arg0: charseq, arg1: boolean): $ByteBuffer
public "mallocLong"(arg0: integer): $LongBuffer
public "mallocFloat"(arg0: integer): $FloatBuffer
public "UTF8Safe"(arg0: charseq): $ByteBuffer
public "UTF8Safe"(arg0: charseq, arg1: boolean): $ByteBuffer
public "mallocDouble"(arg0: integer): $DoubleBuffer
public "mallocCLong"(arg0: integer): $CLongBuffer
public "ASCIISafe"(arg0: charseq): $ByteBuffer
public "ASCIISafe"(arg0: charseq, arg1: boolean): $ByteBuffer
public "callocShort"(arg0: integer): $ShortBuffer
public "shorts"(arg0: short): $ShortBuffer
public "shorts"(arg0: short, arg1: short, arg2: short): $ShortBuffer
public "shorts"(arg0: short, arg1: short): $ShortBuffer
public "shorts"(...arg0: (short)[]): $ShortBuffer
public "shorts"(arg0: short, arg1: short, arg2: short, arg3: short): $ShortBuffer
public "nUTF16"(arg0: charseq, arg1: boolean): integer
public "mallocShort"(arg0: integer): $ShortBuffer
public "clongs"(arg0: long, arg1: long): $CLongBuffer
public "clongs"(arg0: long): $CLongBuffer
public "clongs"(...arg0: (long)[]): $CLongBuffer
public "clongs"(arg0: long, arg1: long, arg2: long, arg3: long): $CLongBuffer
public "clongs"(arg0: long, arg1: long, arg2: long): $CLongBuffer
public "nint"(arg0: integer): long
public "nclong"(arg0: long): long
public "ndouble"(arg0: double): long
public "npointer"(arg0: $Buffer$Type): long
public "npointer"(arg0: $Pointer$Type): long
public "npointer"(arg0: long): long
public "callocDouble"(arg0: integer): $DoubleBuffer
public static "ncreate"(arg0: long, arg1: integer): $MemoryStack
public "nshort"(arg0: short): long
public "nfloat"(arg0: float): long
public "callocLong"(arg0: integer): $LongBuffer
public "callocFloat"(arg0: integer): $FloatBuffer
public "callocCLong"(arg0: integer): $CLongBuffer
public "callocPointer"(arg0: integer): $PointerBuffer
public "nbyte"(arg0: byte): long
public "nlong"(arg0: long): long
public "floats"(arg0: float, arg1: float): $FloatBuffer
public "floats"(arg0: float, arg1: float, arg2: float, arg3: float): $FloatBuffer
public "floats"(arg0: float, arg1: float, arg2: float): $FloatBuffer
public "floats"(arg0: float): $FloatBuffer
public "floats"(...arg0: (float)[]): $FloatBuffer
public "getFrameIndex"(): integer
public static "stackCallocLong"(arg0: integer): $LongBuffer
public static "stackCallocDouble"(arg0: integer): $DoubleBuffer
public static "stackDoubles"(arg0: double, arg1: double): $DoubleBuffer
public static "stackDoubles"(arg0: double): $DoubleBuffer
public static "stackDoubles"(arg0: double, arg1: double, arg2: double, arg3: double): $DoubleBuffer
public static "stackDoubles"(...arg0: (double)[]): $DoubleBuffer
public static "stackDoubles"(arg0: double, arg1: double, arg2: double): $DoubleBuffer
public "nASCIISafe"(arg0: charseq, arg1: boolean): integer
public static "stackMallocInt"(arg0: integer): $IntBuffer
public static "stackMallocLong"(arg0: integer): $LongBuffer
public static "stackCalloc"(arg0: integer): $ByteBuffer
public static "stackShorts"(arg0: short, arg1: short): $ShortBuffer
public static "stackShorts"(arg0: short, arg1: short, arg2: short): $ShortBuffer
public static "stackShorts"(arg0: short): $ShortBuffer
public static "stackShorts"(arg0: short, arg1: short, arg2: short, arg3: short): $ShortBuffer
public static "stackShorts"(...arg0: (short)[]): $ShortBuffer
public static "stackMallocDouble"(arg0: integer): $DoubleBuffer
public static "stackASCII"(arg0: charseq): $ByteBuffer
public static "stackASCII"(arg0: charseq, arg1: boolean): $ByteBuffer
public static "stackCallocInt"(arg0: integer): $IntBuffer
public static "stackCallocCLong"(arg0: integer): $CLongBuffer
public static "stackPointers"(arg0: long, arg1: long, arg2: long): $PointerBuffer
public static "stackPointers"(arg0: long, arg1: long, arg2: long, arg3: long): $PointerBuffer
public static "stackPointers"(...arg0: (long)[]): $PointerBuffer
public static "stackPointers"(arg0: long, arg1: long): $PointerBuffer
public static "stackPointers"(arg0: long): $PointerBuffer
public static "stackPointers"(...arg0: ($Pointer$Type)[]): $PointerBuffer
public static "stackPointers"(arg0: $Pointer$Type, arg1: $Pointer$Type, arg2: $Pointer$Type, arg3: $Pointer$Type): $PointerBuffer
public static "stackPointers"(arg0: $Pointer$Type, arg1: $Pointer$Type, arg2: $Pointer$Type): $PointerBuffer
public static "stackPointers"(arg0: $Pointer$Type): $PointerBuffer
public static "stackPointers"(arg0: $Pointer$Type, arg1: $Pointer$Type): $PointerBuffer
public static "stackLongs"(...arg0: (long)[]): $LongBuffer
public static "stackLongs"(arg0: long, arg1: long, arg2: long, arg3: long): $LongBuffer
public static "stackLongs"(arg0: long, arg1: long, arg2: long): $LongBuffer
public static "stackLongs"(arg0: long): $LongBuffer
public static "stackLongs"(arg0: long, arg1: long): $LongBuffer
public static "stackCallocPointer"(arg0: integer): $PointerBuffer
public static "nstackCalloc"(arg0: integer, arg1: integer, arg2: integer): long
public static "stackMalloc"(arg0: integer): $ByteBuffer
public static "stackBytes"(arg0: byte, arg1: byte, arg2: byte, arg3: byte): $ByteBuffer
public static "stackBytes"(...arg0: (byte)[]): $ByteBuffer
public static "stackBytes"(arg0: byte, arg1: byte, arg2: byte): $ByteBuffer
public static "stackBytes"(arg0: byte, arg1: byte): $ByteBuffer
public static "stackBytes"(arg0: byte): $ByteBuffer
public static "stackCallocFloat"(arg0: integer): $FloatBuffer
public static "nstackMalloc"(arg0: integer): long
public static "nstackMalloc"(arg0: integer, arg1: integer): long
public "nUTF16Safe"(arg0: charseq, arg1: boolean): integer
public static "stackMallocShort"(arg0: integer): $ShortBuffer
public static "stackMallocCLong"(arg0: integer): $CLongBuffer
public static "stackFloats"(arg0: float): $FloatBuffer
public static "stackFloats"(...arg0: (float)[]): $FloatBuffer
public static "stackFloats"(arg0: float, arg1: float, arg2: float, arg3: float): $FloatBuffer
public static "stackFloats"(arg0: float, arg1: float, arg2: float): $FloatBuffer
public static "stackFloats"(arg0: float, arg1: float): $FloatBuffer
public static "stackMallocPointer"(arg0: integer): $PointerBuffer
public static "stackInts"(arg0: integer, arg1: integer): $IntBuffer
public static "stackInts"(...arg0: (integer)[]): $IntBuffer
public static "stackInts"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $IntBuffer
public static "stackInts"(arg0: integer, arg1: integer, arg2: integer): $IntBuffer
public static "stackInts"(arg0: integer): $IntBuffer
public static "stackMallocFloat"(arg0: integer): $FloatBuffer
public "nUTF8Safe"(arg0: charseq, arg1: boolean): integer
public static "stackCallocShort"(arg0: integer): $ShortBuffer
public static "stackCLongs"(arg0: long, arg1: long, arg2: long): $CLongBuffer
public static "stackCLongs"(...arg0: (long)[]): $CLongBuffer
public static "stackCLongs"(arg0: long, arg1: long, arg2: long, arg3: long): $CLongBuffer
public static "stackCLongs"(arg0: long): $CLongBuffer
public static "stackCLongs"(arg0: long, arg1: long): $CLongBuffer
public static "stackPop"(): $MemoryStack
public static "stackUTF8"(arg0: charseq, arg1: boolean): $ByteBuffer
public static "stackUTF8"(arg0: charseq): $ByteBuffer
public static "stackUTF16Safe"(arg0: charseq, arg1: boolean): $ByteBuffer
public static "stackUTF16Safe"(arg0: charseq): $ByteBuffer
public static "stackUTF16"(arg0: charseq, arg1: boolean): $ByteBuffer
public static "stackUTF16"(arg0: charseq): $ByteBuffer
public static "stackASCIISafe"(arg0: charseq): $ByteBuffer
public static "stackASCIISafe"(arg0: charseq, arg1: boolean): $ByteBuffer
public static "stackUTF8Safe"(arg0: charseq, arg1: boolean): $ByteBuffer
public static "stackUTF8Safe"(arg0: charseq): $ByteBuffer
get "size"(): integer
get "address"(): long
set "pointer"(value: integer)
get "pointer"(): integer
get "pointerAddress"(): long
get "frameIndex"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MemoryStack$Type = ($MemoryStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MemoryStack_ = $MemoryStack$Type;
}}
declare module "packages/org/lwjgl/system/windows/$MSG$Buffer" {
import {$POINT, $POINT$Type} from "packages/org/lwjgl/system/windows/$POINT"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$MSG, $MSG$Type} from "packages/org/lwjgl/system/windows/$MSG"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $MSG$Buffer extends $StructBuffer<($MSG), ($MSG$Buffer)> implements $NativeResource {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "message"(): integer
public "message"(arg0: integer): $MSG$Buffer
public "pt"(): $POINT
public "pt"(arg0: $POINT$Type): $MSG$Buffer
public "pt"(arg0: $Consumer$Type<($POINT$Type)>): $MSG$Buffer
public "time"(arg0: integer): $MSG$Buffer
public "time"(): integer
public "hwnd"(): long
public "hwnd"(arg0: long): $MSG$Buffer
public "wParam"(arg0: long): $MSG$Buffer
public "wParam"(): long
public "lParam"(): long
public "lParam"(arg0: long): $MSG$Buffer
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MSG$Buffer$Type = ($MSG$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MSG$Buffer_ = $MSG$Buffer$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWVidMode$Buffer" {
import {$GLFWVidMode, $GLFWVidMode$Type} from "packages/org/lwjgl/glfw/$GLFWVidMode"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$StructBuffer, $StructBuffer$Type} from "packages/org/lwjgl/system/$StructBuffer"

export class $GLFWVidMode$Buffer extends $StructBuffer<($GLFWVidMode), ($GLFWVidMode$Buffer)> {

constructor(arg0: long, arg1: integer)
constructor(arg0: $ByteBuffer$Type)

public "width"(): integer
public "height"(): integer
public "refreshRate"(): integer
public "redBits"(): integer
public "blueBits"(): integer
public "greenBits"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWVidMode$Buffer$Type = ($GLFWVidMode$Buffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWVidMode$Buffer_ = $GLFWVidMode$Buffer$Type;
}}
declare module "packages/org/lwjgl/system/$SharedLibrary" {
import {$Pointer, $Pointer$Type} from "packages/org/lwjgl/system/$Pointer"
import {$FunctionProvider, $FunctionProvider$Type} from "packages/org/lwjgl/system/$FunctionProvider"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export interface $SharedLibrary extends $FunctionProvider, $NativeResource, $Pointer {

 "getName"(): string
 "getPath"(): string
 "getFunctionAddress"(arg0: charseq): long
 "getFunctionAddress"(arg0: $ByteBuffer$Type): long
 "close"(): void
 "free"(): void
 "address"(): long
}

export namespace $SharedLibrary {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharedLibrary$Type = ($SharedLibrary);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharedLibrary_ = $SharedLibrary$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWMouseButtonCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWMouseButtonCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: integer, arg2: integer, arg3: integer): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: integer, arg2: integer, arg3: integer): void
}

export namespace $GLFWMouseButtonCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWMouseButtonCallbackI$Type = ($GLFWMouseButtonCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWMouseButtonCallbackI_ = $GLFWMouseButtonCallbackI$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWErrorCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWErrorCallbackI extends $CallbackI {

 "invoke"(arg0: integer, arg1: long): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: integer, arg1: long): void
}

export namespace $GLFWErrorCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWErrorCallbackI$Type = ($GLFWErrorCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWErrorCallbackI_ = $GLFWErrorCallbackI$Type;
}}
declare module "packages/org/lwjgl/system/$Struct$StructValidation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Struct$StructValidation {

 "validate"(arg0: long): void

(arg0: long): void
}

export namespace $Struct$StructValidation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Struct$StructValidation$Type = ($Struct$StructValidation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Struct$StructValidation_ = $Struct$StructValidation$Type;
}}
declare module "packages/org/lwjgl/system/$Pointer$Default" {
import {$Pointer, $Pointer$Type} from "packages/org/lwjgl/system/$Pointer"

export class $Pointer$Default implements $Pointer {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "address"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Pointer$Default$Type = ($Pointer$Default);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Pointer$Default_ = $Pointer$Default$Type;
}}
declare module "packages/org/lwjgl/system/$NativeResource" {
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export interface $NativeResource extends $AutoCloseable {

 "close"(): void
 "free"(): void

(): void
}

export namespace $NativeResource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NativeResource$Type = ($NativeResource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NativeResource_ = $NativeResource$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWCharModsCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWCharModsCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: integer, arg2: integer): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: integer, arg2: integer): void
}

export namespace $GLFWCharModsCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWCharModsCallbackI$Type = ($GLFWCharModsCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWCharModsCallbackI_ = $GLFWCharModsCallbackI$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWDropCallbackI" {
import {$CallbackI, $CallbackI$Type} from "packages/org/lwjgl/system/$CallbackI"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $GLFWDropCallbackI extends $CallbackI {

 "invoke"(arg0: long, arg1: integer, arg2: long): void
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
 "address"(): long

(arg0: long, arg1: integer, arg2: long): void
}

export namespace $GLFWDropCallbackI {
const CIF: $FFICIF
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWDropCallbackI$Type = ($GLFWDropCallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWDropCallbackI_ = $GLFWDropCallbackI$Type;
}}
declare module "packages/org/lwjgl/system/libffi/$FFIType" {
import {$FFIType$Buffer, $FFIType$Buffer$Type} from "packages/org/lwjgl/system/libffi/$FFIType$Buffer"
import {$MemoryStack, $MemoryStack$Type} from "packages/org/lwjgl/system/$MemoryStack"
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$NativeResource, $NativeResource$Type} from "packages/org/lwjgl/system/$NativeResource"

export class $FFIType extends $Struct implements $NativeResource {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "SIZE": integer
static readonly "ALIGNMENT": integer
static readonly "TYPE": integer
static readonly "ELEMENTS": integer

constructor(arg0: $ByteBuffer$Type)

public "type"(arg0: short): $FFIType
public "type"(): short
public "size"(arg0: long): $FFIType
public "size"(): long
public "elements"(arg0: $PointerBuffer$Type): $FFIType
public "elements"(arg0: integer): $PointerBuffer
public "set"(arg0: $FFIType$Type): $FFIType
public "set"(arg0: long, arg1: short, arg2: short, arg3: $PointerBuffer$Type): $FFIType
public static "create"(arg0: long): $FFIType
public static "create"(arg0: long, arg1: integer): $FFIType$Buffer
public static "create"(arg0: integer): $FFIType$Buffer
public static "create"(): $FFIType
public "alignment"(arg0: short): $FFIType
public "alignment"(): short
public static "malloc"(arg0: integer): $FFIType$Buffer
public static "malloc"(arg0: $MemoryStack$Type): $FFIType
public static "malloc"(arg0: integer, arg1: $MemoryStack$Type): $FFIType$Buffer
public static "malloc"(): $FFIType
public "sizeof"(): integer
public static "calloc"(arg0: $MemoryStack$Type): $FFIType
public static "calloc"(arg0: integer, arg1: $MemoryStack$Type): $FFIType$Buffer
public static "calloc"(arg0: integer): $FFIType$Buffer
public static "calloc"(): $FFIType
public static "createSafe"(arg0: long): $FFIType
public static "createSafe"(arg0: long, arg1: integer): $FFIType$Buffer
public static "nsize"(arg0: long, arg1: long): void
public static "nsize"(arg0: long): long
public static "nalignment"(arg0: long, arg1: short): void
public static "nalignment"(arg0: long): short
public static "nelements"(arg0: long, arg1: $PointerBuffer$Type): void
public static "nelements"(arg0: long, arg1: integer): $PointerBuffer
public static "ntype"(arg0: long): short
public static "ntype"(arg0: long, arg1: short): void
public "close"(): void
public "free"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FFIType$Type = ($FFIType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FFIType_ = $FFIType$Type;
}}
declare module "packages/org/lwjgl/system/$CallbackI" {
import {$Pointer, $Pointer$Type} from "packages/org/lwjgl/system/$Pointer"
import {$FFICIF, $FFICIF$Type} from "packages/org/lwjgl/system/libffi/$FFICIF"

export interface $CallbackI extends $Pointer {

 "address"(): long
 "callback"(arg0: long, arg1: long): void
 "getCallInterface"(): $FFICIF
}

export namespace $CallbackI {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CallbackI$Type = ($CallbackI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CallbackI_ = $CallbackI$Type;
}}
declare module "packages/org/lwjgl/glfw/$GLFWVidMode" {
import {$Struct, $Struct$Type} from "packages/org/lwjgl/system/$Struct"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$GLFWVidMode$Buffer, $GLFWVidMode$Buffer$Type} from "packages/org/lwjgl/glfw/$GLFWVidMode$Buffer"

export class $GLFWVidMode extends $Struct {
static readonly "SIZEOF": integer
static readonly "ALIGNOF": integer
static readonly "WIDTH": integer
static readonly "HEIGHT": integer
static readonly "REDBITS": integer
static readonly "GREENBITS": integer
static readonly "BLUEBITS": integer
static readonly "REFRESHRATE": integer

constructor(arg0: $ByteBuffer$Type)

public static "create"(arg0: long, arg1: integer): $GLFWVidMode$Buffer
public static "create"(arg0: long): $GLFWVidMode
public "width"(): integer
public "height"(): integer
public "sizeof"(): integer
public static "createSafe"(arg0: long): $GLFWVidMode
public static "createSafe"(arg0: long, arg1: integer): $GLFWVidMode$Buffer
public static "nwidth"(arg0: long): integer
public static "nheight"(arg0: long): integer
public "refreshRate"(): integer
public "redBits"(): integer
public static "nredBits"(arg0: long): integer
public "blueBits"(): integer
public static "nblueBits"(arg0: long): integer
public static "ngreenBits"(arg0: long): integer
public static "nrefreshRate"(arg0: long): integer
public "greenBits"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLFWVidMode$Type = ($GLFWVidMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLFWVidMode_ = $GLFWVidMode$Type;
}}
declare module "packages/org/lwjgl/opengl/$GLCapabilities" {
import {$PointerBuffer, $PointerBuffer$Type} from "packages/org/lwjgl/$PointerBuffer"

export class $GLCapabilities {
readonly "glEnable": long
readonly "glDisable": long
readonly "glAccum": long
readonly "glAlphaFunc": long
readonly "glAreTexturesResident": long
readonly "glArrayElement": long
readonly "glBegin": long
readonly "glBindTexture": long
readonly "glBitmap": long
readonly "glBlendFunc": long
readonly "glCallList": long
readonly "glCallLists": long
readonly "glClear": long
readonly "glClearAccum": long
readonly "glClearColor": long
readonly "glClearDepth": long
readonly "glClearIndex": long
readonly "glClearStencil": long
readonly "glClipPlane": long
readonly "glColor3b": long
readonly "glColor3s": long
readonly "glColor3i": long
readonly "glColor3f": long
readonly "glColor3d": long
readonly "glColor3ub": long
readonly "glColor3us": long
readonly "glColor3ui": long
readonly "glColor3bv": long
readonly "glColor3sv": long
readonly "glColor3iv": long
readonly "glColor3fv": long
readonly "glColor3dv": long
readonly "glColor3ubv": long
readonly "glColor3usv": long
readonly "glColor3uiv": long
readonly "glColor4b": long
readonly "glColor4s": long
readonly "glColor4i": long
readonly "glColor4f": long
readonly "glColor4d": long
readonly "glColor4ub": long
readonly "glColor4us": long
readonly "glColor4ui": long
readonly "glColor4bv": long
readonly "glColor4sv": long
readonly "glColor4iv": long
readonly "glColor4fv": long
readonly "glColor4dv": long
readonly "glColor4ubv": long
readonly "glColor4usv": long
readonly "glColor4uiv": long
readonly "glColorMask": long
readonly "glColorMaterial": long
readonly "glColorPointer": long
readonly "glCopyPixels": long
readonly "glCullFace": long
readonly "glDeleteLists": long
readonly "glDepthFunc": long
readonly "glDepthMask": long
readonly "glDepthRange": long
readonly "glDisableClientState": long
readonly "glDrawArrays": long
readonly "glDrawBuffer": long
readonly "glDrawElements": long
readonly "glDrawPixels": long
readonly "glEdgeFlag": long
readonly "glEdgeFlagv": long
readonly "glEdgeFlagPointer": long
readonly "glEnableClientState": long
readonly "glEnd": long
readonly "glEvalCoord1f": long
readonly "glEvalCoord1fv": long
readonly "glEvalCoord1d": long
readonly "glEvalCoord1dv": long
readonly "glEvalCoord2f": long
readonly "glEvalCoord2fv": long
readonly "glEvalCoord2d": long
readonly "glEvalCoord2dv": long
readonly "glEvalMesh1": long
readonly "glEvalMesh2": long
readonly "glEvalPoint1": long
readonly "glEvalPoint2": long
readonly "glFeedbackBuffer": long
readonly "glFinish": long
readonly "glFlush": long
readonly "glFogi": long
readonly "glFogiv": long
readonly "glFogf": long
readonly "glFogfv": long
readonly "glFrontFace": long
readonly "glGenLists": long
readonly "glGenTextures": long
readonly "glDeleteTextures": long
readonly "glGetClipPlane": long
readonly "glGetBooleanv": long
readonly "glGetFloatv": long
readonly "glGetIntegerv": long
readonly "glGetDoublev": long
readonly "glGetError": long
readonly "glGetLightiv": long
readonly "glGetLightfv": long
readonly "glGetMapiv": long
readonly "glGetMapfv": long
readonly "glGetMapdv": long
readonly "glGetMaterialiv": long
readonly "glGetMaterialfv": long
readonly "glGetPixelMapfv": long
readonly "glGetPixelMapusv": long
readonly "glGetPixelMapuiv": long
readonly "glGetPointerv": long
readonly "glGetPolygonStipple": long
readonly "glGetString": long
readonly "glGetTexEnviv": long
readonly "glGetTexEnvfv": long
readonly "glGetTexGeniv": long
readonly "glGetTexGenfv": long
readonly "glGetTexGendv": long
readonly "glGetTexImage": long
readonly "glGetTexLevelParameteriv": long
readonly "glGetTexLevelParameterfv": long
readonly "glGetTexParameteriv": long
readonly "glGetTexParameterfv": long
readonly "glHint": long
readonly "glIndexi": long
readonly "glIndexub": long
readonly "glIndexs": long
readonly "glIndexf": long
readonly "glIndexd": long
readonly "glIndexiv": long
readonly "glIndexubv": long
readonly "glIndexsv": long
readonly "glIndexfv": long
readonly "glIndexdv": long
readonly "glIndexMask": long
readonly "glIndexPointer": long
readonly "glInitNames": long
readonly "glInterleavedArrays": long
readonly "glIsEnabled": long
readonly "glIsList": long
readonly "glIsTexture": long
readonly "glLightModeli": long
readonly "glLightModelf": long
readonly "glLightModeliv": long
readonly "glLightModelfv": long
readonly "glLighti": long
readonly "glLightf": long
readonly "glLightiv": long
readonly "glLightfv": long
readonly "glLineStipple": long
readonly "glLineWidth": long
readonly "glListBase": long
readonly "glLoadMatrixf": long
readonly "glLoadMatrixd": long
readonly "glLoadIdentity": long
readonly "glLoadName": long
readonly "glLogicOp": long
readonly "glMap1f": long
readonly "glMap1d": long
readonly "glMap2f": long
readonly "glMap2d": long
readonly "glMapGrid1f": long
readonly "glMapGrid1d": long
readonly "glMapGrid2f": long
readonly "glMapGrid2d": long
readonly "glMateriali": long
readonly "glMaterialf": long
readonly "glMaterialiv": long
readonly "glMaterialfv": long
readonly "glMatrixMode": long
readonly "glMultMatrixf": long
readonly "glMultMatrixd": long
readonly "glFrustum": long
readonly "glNewList": long
readonly "glEndList": long
readonly "glNormal3f": long
readonly "glNormal3b": long
readonly "glNormal3s": long
readonly "glNormal3i": long
readonly "glNormal3d": long
readonly "glNormal3fv": long
readonly "glNormal3bv": long
readonly "glNormal3sv": long
readonly "glNormal3iv": long
readonly "glNormal3dv": long
readonly "glNormalPointer": long
readonly "glOrtho": long
readonly "glPassThrough": long
readonly "glPixelMapfv": long
readonly "glPixelMapusv": long
readonly "glPixelMapuiv": long
readonly "glPixelStorei": long
readonly "glPixelStoref": long
readonly "glPixelTransferi": long
readonly "glPixelTransferf": long
readonly "glPixelZoom": long
readonly "glPointSize": long
readonly "glPolygonMode": long
readonly "glPolygonOffset": long
readonly "glPolygonStipple": long
readonly "glPushAttrib": long
readonly "glPushClientAttrib": long
readonly "glPopAttrib": long
readonly "glPopClientAttrib": long
readonly "glPopMatrix": long
readonly "glPopName": long
readonly "glPrioritizeTextures": long
readonly "glPushMatrix": long
readonly "glPushName": long
readonly "glRasterPos2i": long
readonly "glRasterPos2s": long
readonly "glRasterPos2f": long
readonly "glRasterPos2d": long
readonly "glRasterPos2iv": long
readonly "glRasterPos2sv": long
readonly "glRasterPos2fv": long
readonly "glRasterPos2dv": long
readonly "glRasterPos3i": long
readonly "glRasterPos3s": long
readonly "glRasterPos3f": long
readonly "glRasterPos3d": long
readonly "glRasterPos3iv": long
readonly "glRasterPos3sv": long
readonly "glRasterPos3fv": long
readonly "glRasterPos3dv": long
readonly "glRasterPos4i": long
readonly "glRasterPos4s": long
readonly "glRasterPos4f": long
readonly "glRasterPos4d": long
readonly "glRasterPos4iv": long
readonly "glRasterPos4sv": long
readonly "glRasterPos4fv": long
readonly "glRasterPos4dv": long
readonly "glReadBuffer": long
readonly "glReadPixels": long
readonly "glRecti": long
readonly "glRects": long
readonly "glRectf": long
readonly "glRectd": long
readonly "glRectiv": long
readonly "glRectsv": long
readonly "glRectfv": long
readonly "glRectdv": long
readonly "glRenderMode": long
readonly "glRotatef": long
readonly "glRotated": long
readonly "glScalef": long
readonly "glScaled": long
readonly "glScissor": long
readonly "glSelectBuffer": long
readonly "glShadeModel": long
readonly "glStencilFunc": long
readonly "glStencilMask": long
readonly "glStencilOp": long
readonly "glTexCoord1f": long
readonly "glTexCoord1s": long
readonly "glTexCoord1i": long
readonly "glTexCoord1d": long
readonly "glTexCoord1fv": long
readonly "glTexCoord1sv": long
readonly "glTexCoord1iv": long
readonly "glTexCoord1dv": long
readonly "glTexCoord2f": long
readonly "glTexCoord2s": long
readonly "glTexCoord2i": long
readonly "glTexCoord2d": long
readonly "glTexCoord2fv": long
readonly "glTexCoord2sv": long
readonly "glTexCoord2iv": long
readonly "glTexCoord2dv": long
readonly "glTexCoord3f": long
readonly "glTexCoord3s": long
readonly "glTexCoord3i": long
readonly "glTexCoord3d": long
readonly "glTexCoord3fv": long
readonly "glTexCoord3sv": long
readonly "glTexCoord3iv": long
readonly "glTexCoord3dv": long
readonly "glTexCoord4f": long
readonly "glTexCoord4s": long
readonly "glTexCoord4i": long
readonly "glTexCoord4d": long
readonly "glTexCoord4fv": long
readonly "glTexCoord4sv": long
readonly "glTexCoord4iv": long
readonly "glTexCoord4dv": long
readonly "glTexCoordPointer": long
readonly "glTexEnvi": long
readonly "glTexEnviv": long
readonly "glTexEnvf": long
readonly "glTexEnvfv": long
readonly "glTexGeni": long
readonly "glTexGeniv": long
readonly "glTexGenf": long
readonly "glTexGenfv": long
readonly "glTexGend": long
readonly "glTexGendv": long
readonly "glTexImage1D": long
readonly "glTexImage2D": long
readonly "glCopyTexImage1D": long
readonly "glCopyTexImage2D": long
readonly "glCopyTexSubImage1D": long
readonly "glCopyTexSubImage2D": long
readonly "glTexParameteri": long
readonly "glTexParameteriv": long
readonly "glTexParameterf": long
readonly "glTexParameterfv": long
readonly "glTexSubImage1D": long
readonly "glTexSubImage2D": long
readonly "glTranslatef": long
readonly "glTranslated": long
readonly "glVertex2f": long
readonly "glVertex2s": long
readonly "glVertex2i": long
readonly "glVertex2d": long
readonly "glVertex2fv": long
readonly "glVertex2sv": long
readonly "glVertex2iv": long
readonly "glVertex2dv": long
readonly "glVertex3f": long
readonly "glVertex3s": long
readonly "glVertex3i": long
readonly "glVertex3d": long
readonly "glVertex3fv": long
readonly "glVertex3sv": long
readonly "glVertex3iv": long
readonly "glVertex3dv": long
readonly "glVertex4f": long
readonly "glVertex4s": long
readonly "glVertex4i": long
readonly "glVertex4d": long
readonly "glVertex4fv": long
readonly "glVertex4sv": long
readonly "glVertex4iv": long
readonly "glVertex4dv": long
readonly "glVertexPointer": long
readonly "glViewport": long
readonly "glTexImage3D": long
readonly "glTexSubImage3D": long
readonly "glCopyTexSubImage3D": long
readonly "glDrawRangeElements": long
readonly "glCompressedTexImage3D": long
readonly "glCompressedTexImage2D": long
readonly "glCompressedTexImage1D": long
readonly "glCompressedTexSubImage3D": long
readonly "glCompressedTexSubImage2D": long
readonly "glCompressedTexSubImage1D": long
readonly "glGetCompressedTexImage": long
readonly "glSampleCoverage": long
readonly "glActiveTexture": long
readonly "glClientActiveTexture": long
readonly "glMultiTexCoord1f": long
readonly "glMultiTexCoord1s": long
readonly "glMultiTexCoord1i": long
readonly "glMultiTexCoord1d": long
readonly "glMultiTexCoord1fv": long
readonly "glMultiTexCoord1sv": long
readonly "glMultiTexCoord1iv": long
readonly "glMultiTexCoord1dv": long
readonly "glMultiTexCoord2f": long
readonly "glMultiTexCoord2s": long
readonly "glMultiTexCoord2i": long
readonly "glMultiTexCoord2d": long
readonly "glMultiTexCoord2fv": long
readonly "glMultiTexCoord2sv": long
readonly "glMultiTexCoord2iv": long
readonly "glMultiTexCoord2dv": long
readonly "glMultiTexCoord3f": long
readonly "glMultiTexCoord3s": long
readonly "glMultiTexCoord3i": long
readonly "glMultiTexCoord3d": long
readonly "glMultiTexCoord3fv": long
readonly "glMultiTexCoord3sv": long
readonly "glMultiTexCoord3iv": long
readonly "glMultiTexCoord3dv": long
readonly "glMultiTexCoord4f": long
readonly "glMultiTexCoord4s": long
readonly "glMultiTexCoord4i": long
readonly "glMultiTexCoord4d": long
readonly "glMultiTexCoord4fv": long
readonly "glMultiTexCoord4sv": long
readonly "glMultiTexCoord4iv": long
readonly "glMultiTexCoord4dv": long
readonly "glLoadTransposeMatrixf": long
readonly "glLoadTransposeMatrixd": long
readonly "glMultTransposeMatrixf": long
readonly "glMultTransposeMatrixd": long
readonly "glBlendColor": long
readonly "glBlendEquation": long
readonly "glFogCoordf": long
readonly "glFogCoordd": long
readonly "glFogCoordfv": long
readonly "glFogCoorddv": long
readonly "glFogCoordPointer": long
readonly "glMultiDrawArrays": long
readonly "glMultiDrawElements": long
readonly "glPointParameterf": long
readonly "glPointParameteri": long
readonly "glPointParameterfv": long
readonly "glPointParameteriv": long
readonly "glSecondaryColor3b": long
readonly "glSecondaryColor3s": long
readonly "glSecondaryColor3i": long
readonly "glSecondaryColor3f": long
readonly "glSecondaryColor3d": long
readonly "glSecondaryColor3ub": long
readonly "glSecondaryColor3us": long
readonly "glSecondaryColor3ui": long
readonly "glSecondaryColor3bv": long
readonly "glSecondaryColor3sv": long
readonly "glSecondaryColor3iv": long
readonly "glSecondaryColor3fv": long
readonly "glSecondaryColor3dv": long
readonly "glSecondaryColor3ubv": long
readonly "glSecondaryColor3usv": long
readonly "glSecondaryColor3uiv": long
readonly "glSecondaryColorPointer": long
readonly "glBlendFuncSeparate": long
readonly "glWindowPos2i": long
readonly "glWindowPos2s": long
readonly "glWindowPos2f": long
readonly "glWindowPos2d": long
readonly "glWindowPos2iv": long
readonly "glWindowPos2sv": long
readonly "glWindowPos2fv": long
readonly "glWindowPos2dv": long
readonly "glWindowPos3i": long
readonly "glWindowPos3s": long
readonly "glWindowPos3f": long
readonly "glWindowPos3d": long
readonly "glWindowPos3iv": long
readonly "glWindowPos3sv": long
readonly "glWindowPos3fv": long
readonly "glWindowPos3dv": long
readonly "glBindBuffer": long
readonly "glDeleteBuffers": long
readonly "glGenBuffers": long
readonly "glIsBuffer": long
readonly "glBufferData": long
readonly "glBufferSubData": long
readonly "glGetBufferSubData": long
readonly "glMapBuffer": long
readonly "glUnmapBuffer": long
readonly "glGetBufferParameteriv": long
readonly "glGetBufferPointerv": long
readonly "glGenQueries": long
readonly "glDeleteQueries": long
readonly "glIsQuery": long
readonly "glBeginQuery": long
readonly "glEndQuery": long
readonly "glGetQueryiv": long
readonly "glGetQueryObjectiv": long
readonly "glGetQueryObjectuiv": long
readonly "glCreateProgram": long
readonly "glDeleteProgram": long
readonly "glIsProgram": long
readonly "glCreateShader": long
readonly "glDeleteShader": long
readonly "glIsShader": long
readonly "glAttachShader": long
readonly "glDetachShader": long
readonly "glShaderSource": long
readonly "glCompileShader": long
readonly "glLinkProgram": long
readonly "glUseProgram": long
readonly "glValidateProgram": long
readonly "glUniform1f": long
readonly "glUniform2f": long
readonly "glUniform3f": long
readonly "glUniform4f": long
readonly "glUniform1i": long
readonly "glUniform2i": long
readonly "glUniform3i": long
readonly "glUniform4i": long
readonly "glUniform1fv": long
readonly "glUniform2fv": long
readonly "glUniform3fv": long
readonly "glUniform4fv": long
readonly "glUniform1iv": long
readonly "glUniform2iv": long
readonly "glUniform3iv": long
readonly "glUniform4iv": long
readonly "glUniformMatrix2fv": long
readonly "glUniformMatrix3fv": long
readonly "glUniformMatrix4fv": long
readonly "glGetShaderiv": long
readonly "glGetProgramiv": long
readonly "glGetShaderInfoLog": long
readonly "glGetProgramInfoLog": long
readonly "glGetAttachedShaders": long
readonly "glGetUniformLocation": long
readonly "glGetActiveUniform": long
readonly "glGetUniformfv": long
readonly "glGetUniformiv": long
readonly "glGetShaderSource": long
readonly "glVertexAttrib1f": long
readonly "glVertexAttrib1s": long
readonly "glVertexAttrib1d": long
readonly "glVertexAttrib2f": long
readonly "glVertexAttrib2s": long
readonly "glVertexAttrib2d": long
readonly "glVertexAttrib3f": long
readonly "glVertexAttrib3s": long
readonly "glVertexAttrib3d": long
readonly "glVertexAttrib4f": long
readonly "glVertexAttrib4s": long
readonly "glVertexAttrib4d": long
readonly "glVertexAttrib4Nub": long
readonly "glVertexAttrib1fv": long
readonly "glVertexAttrib1sv": long
readonly "glVertexAttrib1dv": long
readonly "glVertexAttrib2fv": long
readonly "glVertexAttrib2sv": long
readonly "glVertexAttrib2dv": long
readonly "glVertexAttrib3fv": long
readonly "glVertexAttrib3sv": long
readonly "glVertexAttrib3dv": long
readonly "glVertexAttrib4fv": long
readonly "glVertexAttrib4sv": long
readonly "glVertexAttrib4dv": long
readonly "glVertexAttrib4iv": long
readonly "glVertexAttrib4bv": long
readonly "glVertexAttrib4ubv": long
readonly "glVertexAttrib4usv": long
readonly "glVertexAttrib4uiv": long
readonly "glVertexAttrib4Nbv": long
readonly "glVertexAttrib4Nsv": long
readonly "glVertexAttrib4Niv": long
readonly "glVertexAttrib4Nubv": long
readonly "glVertexAttrib4Nusv": long
readonly "glVertexAttrib4Nuiv": long
readonly "glVertexAttribPointer": long
readonly "glEnableVertexAttribArray": long
readonly "glDisableVertexAttribArray": long
readonly "glBindAttribLocation": long
readonly "glGetActiveAttrib": long
readonly "glGetAttribLocation": long
readonly "glGetVertexAttribiv": long
readonly "glGetVertexAttribfv": long
readonly "glGetVertexAttribdv": long
readonly "glGetVertexAttribPointerv": long
readonly "glDrawBuffers": long
readonly "glBlendEquationSeparate": long
readonly "glStencilOpSeparate": long
readonly "glStencilFuncSeparate": long
readonly "glStencilMaskSeparate": long
readonly "glUniformMatrix2x3fv": long
readonly "glUniformMatrix3x2fv": long
readonly "glUniformMatrix2x4fv": long
readonly "glUniformMatrix4x2fv": long
readonly "glUniformMatrix3x4fv": long
readonly "glUniformMatrix4x3fv": long
readonly "glGetStringi": long
readonly "glClearBufferiv": long
readonly "glClearBufferuiv": long
readonly "glClearBufferfv": long
readonly "glClearBufferfi": long
readonly "glVertexAttribI1i": long
readonly "glVertexAttribI2i": long
readonly "glVertexAttribI3i": long
readonly "glVertexAttribI4i": long
readonly "glVertexAttribI1ui": long
readonly "glVertexAttribI2ui": long
readonly "glVertexAttribI3ui": long
readonly "glVertexAttribI4ui": long
readonly "glVertexAttribI1iv": long
readonly "glVertexAttribI2iv": long
readonly "glVertexAttribI3iv": long
readonly "glVertexAttribI4iv": long
readonly "glVertexAttribI1uiv": long
readonly "glVertexAttribI2uiv": long
readonly "glVertexAttribI3uiv": long
readonly "glVertexAttribI4uiv": long
readonly "glVertexAttribI4bv": long
readonly "glVertexAttribI4sv": long
readonly "glVertexAttribI4ubv": long
readonly "glVertexAttribI4usv": long
readonly "glVertexAttribIPointer": long
readonly "glGetVertexAttribIiv": long
readonly "glGetVertexAttribIuiv": long
readonly "glUniform1ui": long
readonly "glUniform2ui": long
readonly "glUniform3ui": long
readonly "glUniform4ui": long
readonly "glUniform1uiv": long
readonly "glUniform2uiv": long
readonly "glUniform3uiv": long
readonly "glUniform4uiv": long
readonly "glGetUniformuiv": long
readonly "glBindFragDataLocation": long
readonly "glGetFragDataLocation": long
readonly "glBeginConditionalRender": long
readonly "glEndConditionalRender": long
readonly "glMapBufferRange": long
readonly "glFlushMappedBufferRange": long
readonly "glClampColor": long
readonly "glIsRenderbuffer": long
readonly "glBindRenderbuffer": long
readonly "glDeleteRenderbuffers": long
readonly "glGenRenderbuffers": long
readonly "glRenderbufferStorage": long
readonly "glRenderbufferStorageMultisample": long
readonly "glGetRenderbufferParameteriv": long
readonly "glIsFramebuffer": long
readonly "glBindFramebuffer": long
readonly "glDeleteFramebuffers": long
readonly "glGenFramebuffers": long
readonly "glCheckFramebufferStatus": long
readonly "glFramebufferTexture1D": long
readonly "glFramebufferTexture2D": long
readonly "glFramebufferTexture3D": long
readonly "glFramebufferTextureLayer": long
readonly "glFramebufferRenderbuffer": long
readonly "glGetFramebufferAttachmentParameteriv": long
readonly "glBlitFramebuffer": long
readonly "glGenerateMipmap": long
readonly "glTexParameterIiv": long
readonly "glTexParameterIuiv": long
readonly "glGetTexParameterIiv": long
readonly "glGetTexParameterIuiv": long
readonly "glColorMaski": long
readonly "glGetBooleani_v": long
readonly "glGetIntegeri_v": long
readonly "glEnablei": long
readonly "glDisablei": long
readonly "glIsEnabledi": long
readonly "glBindBufferRange": long
readonly "glBindBufferBase": long
readonly "glBeginTransformFeedback": long
readonly "glEndTransformFeedback": long
readonly "glTransformFeedbackVaryings": long
readonly "glGetTransformFeedbackVarying": long
readonly "glBindVertexArray": long
readonly "glDeleteVertexArrays": long
readonly "glGenVertexArrays": long
readonly "glIsVertexArray": long
readonly "glDrawArraysInstanced": long
readonly "glDrawElementsInstanced": long
readonly "glCopyBufferSubData": long
readonly "glPrimitiveRestartIndex": long
readonly "glTexBuffer": long
readonly "glGetUniformIndices": long
readonly "glGetActiveUniformsiv": long
readonly "glGetActiveUniformName": long
readonly "glGetUniformBlockIndex": long
readonly "glGetActiveUniformBlockiv": long
readonly "glGetActiveUniformBlockName": long
readonly "glUniformBlockBinding": long
readonly "glGetBufferParameteri64v": long
readonly "glDrawElementsBaseVertex": long
readonly "glDrawRangeElementsBaseVertex": long
readonly "glDrawElementsInstancedBaseVertex": long
readonly "glMultiDrawElementsBaseVertex": long
readonly "glProvokingVertex": long
readonly "glTexImage2DMultisample": long
readonly "glTexImage3DMultisample": long
readonly "glGetMultisamplefv": long
readonly "glSampleMaski": long
readonly "glFramebufferTexture": long
readonly "glFenceSync": long
readonly "glIsSync": long
readonly "glDeleteSync": long
readonly "glClientWaitSync": long
readonly "glWaitSync": long
readonly "glGetInteger64v": long
readonly "glGetInteger64i_v": long
readonly "glGetSynciv": long
readonly "glBindFragDataLocationIndexed": long
readonly "glGetFragDataIndex": long
readonly "glGenSamplers": long
readonly "glDeleteSamplers": long
readonly "glIsSampler": long
readonly "glBindSampler": long
readonly "glSamplerParameteri": long
readonly "glSamplerParameterf": long
readonly "glSamplerParameteriv": long
readonly "glSamplerParameterfv": long
readonly "glSamplerParameterIiv": long
readonly "glSamplerParameterIuiv": long
readonly "glGetSamplerParameteriv": long
readonly "glGetSamplerParameterfv": long
readonly "glGetSamplerParameterIiv": long
readonly "glGetSamplerParameterIuiv": long
readonly "glQueryCounter": long
readonly "glGetQueryObjecti64v": long
readonly "glGetQueryObjectui64v": long
readonly "glVertexAttribDivisor": long
readonly "glVertexP2ui": long
readonly "glVertexP3ui": long
readonly "glVertexP4ui": long
readonly "glVertexP2uiv": long
readonly "glVertexP3uiv": long
readonly "glVertexP4uiv": long
readonly "glTexCoordP1ui": long
readonly "glTexCoordP2ui": long
readonly "glTexCoordP3ui": long
readonly "glTexCoordP4ui": long
readonly "glTexCoordP1uiv": long
readonly "glTexCoordP2uiv": long
readonly "glTexCoordP3uiv": long
readonly "glTexCoordP4uiv": long
readonly "glMultiTexCoordP1ui": long
readonly "glMultiTexCoordP2ui": long
readonly "glMultiTexCoordP3ui": long
readonly "glMultiTexCoordP4ui": long
readonly "glMultiTexCoordP1uiv": long
readonly "glMultiTexCoordP2uiv": long
readonly "glMultiTexCoordP3uiv": long
readonly "glMultiTexCoordP4uiv": long
readonly "glNormalP3ui": long
readonly "glNormalP3uiv": long
readonly "glColorP3ui": long
readonly "glColorP4ui": long
readonly "glColorP3uiv": long
readonly "glColorP4uiv": long
readonly "glSecondaryColorP3ui": long
readonly "glSecondaryColorP3uiv": long
readonly "glVertexAttribP1ui": long
readonly "glVertexAttribP2ui": long
readonly "glVertexAttribP3ui": long
readonly "glVertexAttribP4ui": long
readonly "glVertexAttribP1uiv": long
readonly "glVertexAttribP2uiv": long
readonly "glVertexAttribP3uiv": long
readonly "glVertexAttribP4uiv": long
readonly "glBlendEquationi": long
readonly "glBlendEquationSeparatei": long
readonly "glBlendFunci": long
readonly "glBlendFuncSeparatei": long
readonly "glDrawArraysIndirect": long
readonly "glDrawElementsIndirect": long
readonly "glUniform1d": long
readonly "glUniform2d": long
readonly "glUniform3d": long
readonly "glUniform4d": long
readonly "glUniform1dv": long
readonly "glUniform2dv": long
readonly "glUniform3dv": long
readonly "glUniform4dv": long
readonly "glUniformMatrix2dv": long
readonly "glUniformMatrix3dv": long
readonly "glUniformMatrix4dv": long
readonly "glUniformMatrix2x3dv": long
readonly "glUniformMatrix2x4dv": long
readonly "glUniformMatrix3x2dv": long
readonly "glUniformMatrix3x4dv": long
readonly "glUniformMatrix4x2dv": long
readonly "glUniformMatrix4x3dv": long
readonly "glGetUniformdv": long
readonly "glMinSampleShading": long
readonly "glGetSubroutineUniformLocation": long
readonly "glGetSubroutineIndex": long
readonly "glGetActiveSubroutineUniformiv": long
readonly "glGetActiveSubroutineUniformName": long
readonly "glGetActiveSubroutineName": long
readonly "glUniformSubroutinesuiv": long
readonly "glGetUniformSubroutineuiv": long
readonly "glGetProgramStageiv": long
readonly "glPatchParameteri": long
readonly "glPatchParameterfv": long
readonly "glBindTransformFeedback": long
readonly "glDeleteTransformFeedbacks": long
readonly "glGenTransformFeedbacks": long
readonly "glIsTransformFeedback": long
readonly "glPauseTransformFeedback": long
readonly "glResumeTransformFeedback": long
readonly "glDrawTransformFeedback": long
readonly "glDrawTransformFeedbackStream": long
readonly "glBeginQueryIndexed": long
readonly "glEndQueryIndexed": long
readonly "glGetQueryIndexediv": long
readonly "glReleaseShaderCompiler": long
readonly "glShaderBinary": long
readonly "glGetShaderPrecisionFormat": long
readonly "glDepthRangef": long
readonly "glClearDepthf": long
readonly "glGetProgramBinary": long
readonly "glProgramBinary": long
readonly "glProgramParameteri": long
readonly "glUseProgramStages": long
readonly "glActiveShaderProgram": long
readonly "glCreateShaderProgramv": long
readonly "glBindProgramPipeline": long
readonly "glDeleteProgramPipelines": long
readonly "glGenProgramPipelines": long
readonly "glIsProgramPipeline": long
readonly "glGetProgramPipelineiv": long
readonly "glProgramUniform1i": long
readonly "glProgramUniform2i": long
readonly "glProgramUniform3i": long
readonly "glProgramUniform4i": long
readonly "glProgramUniform1ui": long
readonly "glProgramUniform2ui": long
readonly "glProgramUniform3ui": long
readonly "glProgramUniform4ui": long
readonly "glProgramUniform1f": long
readonly "glProgramUniform2f": long
readonly "glProgramUniform3f": long
readonly "glProgramUniform4f": long
readonly "glProgramUniform1d": long
readonly "glProgramUniform2d": long
readonly "glProgramUniform3d": long
readonly "glProgramUniform4d": long
readonly "glProgramUniform1iv": long
readonly "glProgramUniform2iv": long
readonly "glProgramUniform3iv": long
readonly "glProgramUniform4iv": long
readonly "glProgramUniform1uiv": long
readonly "glProgramUniform2uiv": long
readonly "glProgramUniform3uiv": long
readonly "glProgramUniform4uiv": long
readonly "glProgramUniform1fv": long
readonly "glProgramUniform2fv": long
readonly "glProgramUniform3fv": long
readonly "glProgramUniform4fv": long
readonly "glProgramUniform1dv": long
readonly "glProgramUniform2dv": long
readonly "glProgramUniform3dv": long
readonly "glProgramUniform4dv": long
readonly "glProgramUniformMatrix2fv": long
readonly "glProgramUniformMatrix3fv": long
readonly "glProgramUniformMatrix4fv": long
readonly "glProgramUniformMatrix2dv": long
readonly "glProgramUniformMatrix3dv": long
readonly "glProgramUniformMatrix4dv": long
readonly "glProgramUniformMatrix2x3fv": long
readonly "glProgramUniformMatrix3x2fv": long
readonly "glProgramUniformMatrix2x4fv": long
readonly "glProgramUniformMatrix4x2fv": long
readonly "glProgramUniformMatrix3x4fv": long
readonly "glProgramUniformMatrix4x3fv": long
readonly "glProgramUniformMatrix2x3dv": long
readonly "glProgramUniformMatrix3x2dv": long
readonly "glProgramUniformMatrix2x4dv": long
readonly "glProgramUniformMatrix4x2dv": long
readonly "glProgramUniformMatrix3x4dv": long
readonly "glProgramUniformMatrix4x3dv": long
readonly "glValidateProgramPipeline": long
readonly "glGetProgramPipelineInfoLog": long
readonly "glVertexAttribL1d": long
readonly "glVertexAttribL2d": long
readonly "glVertexAttribL3d": long
readonly "glVertexAttribL4d": long
readonly "glVertexAttribL1dv": long
readonly "glVertexAttribL2dv": long
readonly "glVertexAttribL3dv": long
readonly "glVertexAttribL4dv": long
readonly "glVertexAttribLPointer": long
readonly "glGetVertexAttribLdv": long
readonly "glViewportArrayv": long
readonly "glViewportIndexedf": long
readonly "glViewportIndexedfv": long
readonly "glScissorArrayv": long
readonly "glScissorIndexed": long
readonly "glScissorIndexedv": long
readonly "glDepthRangeArrayv": long
readonly "glDepthRangeIndexed": long
readonly "glGetFloati_v": long
readonly "glGetDoublei_v": long
readonly "glGetActiveAtomicCounterBufferiv": long
readonly "glTexStorage1D": long
readonly "glTexStorage2D": long
readonly "glTexStorage3D": long
readonly "glDrawTransformFeedbackInstanced": long
readonly "glDrawTransformFeedbackStreamInstanced": long
readonly "glDrawArraysInstancedBaseInstance": long
readonly "glDrawElementsInstancedBaseInstance": long
readonly "glDrawElementsInstancedBaseVertexBaseInstance": long
readonly "glBindImageTexture": long
readonly "glMemoryBarrier": long
readonly "glGetInternalformativ": long
readonly "glClearBufferData": long
readonly "glClearBufferSubData": long
readonly "glDispatchCompute": long
readonly "glDispatchComputeIndirect": long
readonly "glCopyImageSubData": long
readonly "glDebugMessageControl": long
readonly "glDebugMessageInsert": long
readonly "glDebugMessageCallback": long
readonly "glGetDebugMessageLog": long
readonly "glPushDebugGroup": long
readonly "glPopDebugGroup": long
readonly "glObjectLabel": long
readonly "glGetObjectLabel": long
readonly "glObjectPtrLabel": long
readonly "glGetObjectPtrLabel": long
readonly "glFramebufferParameteri": long
readonly "glGetFramebufferParameteriv": long
readonly "glGetInternalformati64v": long
readonly "glInvalidateTexSubImage": long
readonly "glInvalidateTexImage": long
readonly "glInvalidateBufferSubData": long
readonly "glInvalidateBufferData": long
readonly "glInvalidateFramebuffer": long
readonly "glInvalidateSubFramebuffer": long
readonly "glMultiDrawArraysIndirect": long
readonly "glMultiDrawElementsIndirect": long
readonly "glGetProgramInterfaceiv": long
readonly "glGetProgramResourceIndex": long
readonly "glGetProgramResourceName": long
readonly "glGetProgramResourceiv": long
readonly "glGetProgramResourceLocation": long
readonly "glGetProgramResourceLocationIndex": long
readonly "glShaderStorageBlockBinding": long
readonly "glTexBufferRange": long
readonly "glTexStorage2DMultisample": long
readonly "glTexStorage3DMultisample": long
readonly "glTextureView": long
readonly "glBindVertexBuffer": long
readonly "glVertexAttribFormat": long
readonly "glVertexAttribIFormat": long
readonly "glVertexAttribLFormat": long
readonly "glVertexAttribBinding": long
readonly "glVertexBindingDivisor": long
readonly "glBufferStorage": long
readonly "glClearTexSubImage": long
readonly "glClearTexImage": long
readonly "glBindBuffersBase": long
readonly "glBindBuffersRange": long
readonly "glBindTextures": long
readonly "glBindSamplers": long
readonly "glBindImageTextures": long
readonly "glBindVertexBuffers": long
readonly "glClipControl": long
readonly "glCreateTransformFeedbacks": long
readonly "glTransformFeedbackBufferBase": long
readonly "glTransformFeedbackBufferRange": long
readonly "glGetTransformFeedbackiv": long
readonly "glGetTransformFeedbacki_v": long
readonly "glGetTransformFeedbacki64_v": long
readonly "glCreateBuffers": long
readonly "glNamedBufferStorage": long
readonly "glNamedBufferData": long
readonly "glNamedBufferSubData": long
readonly "glCopyNamedBufferSubData": long
readonly "glClearNamedBufferData": long
readonly "glClearNamedBufferSubData": long
readonly "glMapNamedBuffer": long
readonly "glMapNamedBufferRange": long
readonly "glUnmapNamedBuffer": long
readonly "glFlushMappedNamedBufferRange": long
readonly "glGetNamedBufferParameteriv": long
readonly "glGetNamedBufferParameteri64v": long
readonly "glGetNamedBufferPointerv": long
readonly "glGetNamedBufferSubData": long
readonly "glCreateFramebuffers": long
readonly "glNamedFramebufferRenderbuffer": long
readonly "glNamedFramebufferParameteri": long
readonly "glNamedFramebufferTexture": long
readonly "glNamedFramebufferTextureLayer": long
readonly "glNamedFramebufferDrawBuffer": long
readonly "glNamedFramebufferDrawBuffers": long
readonly "glNamedFramebufferReadBuffer": long
readonly "glInvalidateNamedFramebufferData": long
readonly "glInvalidateNamedFramebufferSubData": long
readonly "glClearNamedFramebufferiv": long
readonly "glClearNamedFramebufferuiv": long
readonly "glClearNamedFramebufferfv": long
readonly "glClearNamedFramebufferfi": long
readonly "glBlitNamedFramebuffer": long
readonly "glCheckNamedFramebufferStatus": long
readonly "glGetNamedFramebufferParameteriv": long
readonly "glGetNamedFramebufferAttachmentParameteriv": long
readonly "glCreateRenderbuffers": long
readonly "glNamedRenderbufferStorage": long
readonly "glNamedRenderbufferStorageMultisample": long
readonly "glGetNamedRenderbufferParameteriv": long
readonly "glCreateTextures": long
readonly "glTextureBuffer": long
readonly "glTextureBufferRange": long
readonly "glTextureStorage1D": long
readonly "glTextureStorage2D": long
readonly "glTextureStorage3D": long
readonly "glTextureStorage2DMultisample": long
readonly "glTextureStorage3DMultisample": long
readonly "glTextureSubImage1D": long
readonly "glTextureSubImage2D": long
readonly "glTextureSubImage3D": long
readonly "glCompressedTextureSubImage1D": long
readonly "glCompressedTextureSubImage2D": long
readonly "glCompressedTextureSubImage3D": long
readonly "glCopyTextureSubImage1D": long
readonly "glCopyTextureSubImage2D": long
readonly "glCopyTextureSubImage3D": long
readonly "glTextureParameterf": long
readonly "glTextureParameterfv": long
readonly "glTextureParameteri": long
readonly "glTextureParameterIiv": long
readonly "glTextureParameterIuiv": long
readonly "glTextureParameteriv": long
readonly "glGenerateTextureMipmap": long
readonly "glBindTextureUnit": long
readonly "glGetTextureImage": long
readonly "glGetCompressedTextureImage": long
readonly "glGetTextureLevelParameterfv": long
readonly "glGetTextureLevelParameteriv": long
readonly "glGetTextureParameterfv": long
readonly "glGetTextureParameterIiv": long
readonly "glGetTextureParameterIuiv": long
readonly "glGetTextureParameteriv": long
readonly "glCreateVertexArrays": long
readonly "glDisableVertexArrayAttrib": long
readonly "glEnableVertexArrayAttrib": long
readonly "glVertexArrayElementBuffer": long
readonly "glVertexArrayVertexBuffer": long
readonly "glVertexArrayVertexBuffers": long
readonly "glVertexArrayAttribFormat": long
readonly "glVertexArrayAttribIFormat": long
readonly "glVertexArrayAttribLFormat": long
readonly "glVertexArrayAttribBinding": long
readonly "glVertexArrayBindingDivisor": long
readonly "glGetVertexArrayiv": long
readonly "glGetVertexArrayIndexediv": long
readonly "glGetVertexArrayIndexed64iv": long
readonly "glCreateSamplers": long
readonly "glCreateProgramPipelines": long
readonly "glCreateQueries": long
readonly "glGetQueryBufferObjectiv": long
readonly "glGetQueryBufferObjectuiv": long
readonly "glGetQueryBufferObjecti64v": long
readonly "glGetQueryBufferObjectui64v": long
readonly "glMemoryBarrierByRegion": long
readonly "glGetTextureSubImage": long
readonly "glGetCompressedTextureSubImage": long
readonly "glTextureBarrier": long
readonly "glGetGraphicsResetStatus": long
readonly "glGetnMapdv": long
readonly "glGetnMapfv": long
readonly "glGetnMapiv": long
readonly "glGetnPixelMapfv": long
readonly "glGetnPixelMapuiv": long
readonly "glGetnPixelMapusv": long
readonly "glGetnPolygonStipple": long
readonly "glGetnTexImage": long
readonly "glReadnPixels": long
readonly "glGetnColorTable": long
readonly "glGetnConvolutionFilter": long
readonly "glGetnSeparableFilter": long
readonly "glGetnHistogram": long
readonly "glGetnMinmax": long
readonly "glGetnCompressedTexImage": long
readonly "glGetnUniformfv": long
readonly "glGetnUniformdv": long
readonly "glGetnUniformiv": long
readonly "glGetnUniformuiv": long
readonly "glMultiDrawArraysIndirectCount": long
readonly "glMultiDrawElementsIndirectCount": long
readonly "glPolygonOffsetClamp": long
readonly "glSpecializeShader": long
readonly "glDebugMessageEnableAMD": long
readonly "glDebugMessageInsertAMD": long
readonly "glDebugMessageCallbackAMD": long
readonly "glGetDebugMessageLogAMD": long
readonly "glBlendFuncIndexedAMD": long
readonly "glBlendFuncSeparateIndexedAMD": long
readonly "glBlendEquationIndexedAMD": long
readonly "glBlendEquationSeparateIndexedAMD": long
readonly "glRenderbufferStorageMultisampleAdvancedAMD": long
readonly "glNamedRenderbufferStorageMultisampleAdvancedAMD": long
readonly "glUniform1i64NV": long
readonly "glUniform2i64NV": long
readonly "glUniform3i64NV": long
readonly "glUniform4i64NV": long
readonly "glUniform1i64vNV": long
readonly "glUniform2i64vNV": long
readonly "glUniform3i64vNV": long
readonly "glUniform4i64vNV": long
readonly "glUniform1ui64NV": long
readonly "glUniform2ui64NV": long
readonly "glUniform3ui64NV": long
readonly "glUniform4ui64NV": long
readonly "glUniform1ui64vNV": long
readonly "glUniform2ui64vNV": long
readonly "glUniform3ui64vNV": long
readonly "glUniform4ui64vNV": long
readonly "glGetUniformi64vNV": long
readonly "glGetUniformui64vNV": long
readonly "glProgramUniform1i64NV": long
readonly "glProgramUniform2i64NV": long
readonly "glProgramUniform3i64NV": long
readonly "glProgramUniform4i64NV": long
readonly "glProgramUniform1i64vNV": long
readonly "glProgramUniform2i64vNV": long
readonly "glProgramUniform3i64vNV": long
readonly "glProgramUniform4i64vNV": long
readonly "glProgramUniform1ui64NV": long
readonly "glProgramUniform2ui64NV": long
readonly "glProgramUniform3ui64NV": long
readonly "glProgramUniform4ui64NV": long
readonly "glProgramUniform1ui64vNV": long
readonly "glProgramUniform2ui64vNV": long
readonly "glProgramUniform3ui64vNV": long
readonly "glProgramUniform4ui64vNV": long
readonly "glVertexAttribParameteriAMD": long
readonly "glQueryObjectParameteruiAMD": long
readonly "glGetPerfMonitorGroupsAMD": long
readonly "glGetPerfMonitorCountersAMD": long
readonly "glGetPerfMonitorGroupStringAMD": long
readonly "glGetPerfMonitorCounterStringAMD": long
readonly "glGetPerfMonitorCounterInfoAMD": long
readonly "glGenPerfMonitorsAMD": long
readonly "glDeletePerfMonitorsAMD": long
readonly "glSelectPerfMonitorCountersAMD": long
readonly "glBeginPerfMonitorAMD": long
readonly "glEndPerfMonitorAMD": long
readonly "glGetPerfMonitorCounterDataAMD": long
readonly "glSetMultisamplefvAMD": long
readonly "glTexStorageSparseAMD": long
readonly "glTextureStorageSparseAMD": long
readonly "glStencilOpValueAMD": long
readonly "glTessellationFactorAMD": long
readonly "glTessellationModeAMD": long
readonly "glGetTextureHandleARB": long
readonly "glGetTextureSamplerHandleARB": long
readonly "glMakeTextureHandleResidentARB": long
readonly "glMakeTextureHandleNonResidentARB": long
readonly "glGetImageHandleARB": long
readonly "glMakeImageHandleResidentARB": long
readonly "glMakeImageHandleNonResidentARB": long
readonly "glUniformHandleui64ARB": long
readonly "glUniformHandleui64vARB": long
readonly "glProgramUniformHandleui64ARB": long
readonly "glProgramUniformHandleui64vARB": long
readonly "glIsTextureHandleResidentARB": long
readonly "glIsImageHandleResidentARB": long
readonly "glVertexAttribL1ui64ARB": long
readonly "glVertexAttribL1ui64vARB": long
readonly "glGetVertexAttribLui64vARB": long
readonly "glNamedBufferStorageEXT": long
readonly "glCreateSyncFromCLeventARB": long
readonly "glClearNamedBufferDataEXT": long
readonly "glClearNamedBufferSubDataEXT": long
readonly "glClampColorARB": long
readonly "glDispatchComputeGroupSizeARB": long
readonly "glDebugMessageControlARB": long
readonly "glDebugMessageInsertARB": long
readonly "glDebugMessageCallbackARB": long
readonly "glGetDebugMessageLogARB": long
readonly "glDrawBuffersARB": long
readonly "glBlendEquationiARB": long
readonly "glBlendEquationSeparateiARB": long
readonly "glBlendFunciARB": long
readonly "glBlendFuncSeparateiARB": long
readonly "glDrawArraysInstancedARB": long
readonly "glDrawElementsInstancedARB": long
readonly "glPrimitiveBoundingBoxARB": long
readonly "glNamedFramebufferParameteriEXT": long
readonly "glGetNamedFramebufferParameterivEXT": long
readonly "glProgramParameteriARB": long
readonly "glFramebufferTextureARB": long
readonly "glFramebufferTextureLayerARB": long
readonly "glFramebufferTextureFaceARB": long
readonly "glSpecializeShaderARB": long
readonly "glProgramUniform1dEXT": long
readonly "glProgramUniform2dEXT": long
readonly "glProgramUniform3dEXT": long
readonly "glProgramUniform4dEXT": long
readonly "glProgramUniform1dvEXT": long
readonly "glProgramUniform2dvEXT": long
readonly "glProgramUniform3dvEXT": long
readonly "glProgramUniform4dvEXT": long
readonly "glProgramUniformMatrix2dvEXT": long
readonly "glProgramUniformMatrix3dvEXT": long
readonly "glProgramUniformMatrix4dvEXT": long
readonly "glProgramUniformMatrix2x3dvEXT": long
readonly "glProgramUniformMatrix2x4dvEXT": long
readonly "glProgramUniformMatrix3x2dvEXT": long
readonly "glProgramUniformMatrix3x4dvEXT": long
readonly "glProgramUniformMatrix4x2dvEXT": long
readonly "glProgramUniformMatrix4x3dvEXT": long
readonly "glUniform1i64ARB": long
readonly "glUniform1i64vARB": long
readonly "glProgramUniform1i64ARB": long
readonly "glProgramUniform1i64vARB": long
readonly "glUniform2i64ARB": long
readonly "glUniform2i64vARB": long
readonly "glProgramUniform2i64ARB": long
readonly "glProgramUniform2i64vARB": long
readonly "glUniform3i64ARB": long
readonly "glUniform3i64vARB": long
readonly "glProgramUniform3i64ARB": long
readonly "glProgramUniform3i64vARB": long
readonly "glUniform4i64ARB": long
readonly "glUniform4i64vARB": long
readonly "glProgramUniform4i64ARB": long
readonly "glProgramUniform4i64vARB": long
readonly "glUniform1ui64ARB": long
readonly "glUniform1ui64vARB": long
readonly "glProgramUniform1ui64ARB": long
readonly "glProgramUniform1ui64vARB": long
readonly "glUniform2ui64ARB": long
readonly "glUniform2ui64vARB": long
readonly "glProgramUniform2ui64ARB": long
readonly "glProgramUniform2ui64vARB": long
readonly "glUniform3ui64ARB": long
readonly "glUniform3ui64vARB": long
readonly "glProgramUniform3ui64ARB": long
readonly "glProgramUniform3ui64vARB": long
readonly "glUniform4ui64ARB": long
readonly "glUniform4ui64vARB": long
readonly "glProgramUniform4ui64ARB": long
readonly "glProgramUniform4ui64vARB": long
readonly "glGetUniformi64vARB": long
readonly "glGetUniformui64vARB": long
readonly "glGetnUniformi64vARB": long
readonly "glGetnUniformui64vARB": long
readonly "glColorTable": long
readonly "glCopyColorTable": long
readonly "glColorTableParameteriv": long
readonly "glColorTableParameterfv": long
readonly "glGetColorTable": long
readonly "glGetColorTableParameteriv": long
readonly "glGetColorTableParameterfv": long
readonly "glColorSubTable": long
readonly "glCopyColorSubTable": long
readonly "glConvolutionFilter1D": long
readonly "glConvolutionFilter2D": long
readonly "glCopyConvolutionFilter1D": long
readonly "glCopyConvolutionFilter2D": long
readonly "glGetConvolutionFilter": long
readonly "glSeparableFilter2D": long
readonly "glGetSeparableFilter": long
readonly "glConvolutionParameteri": long
readonly "glConvolutionParameteriv": long
readonly "glConvolutionParameterf": long
readonly "glConvolutionParameterfv": long
readonly "glGetConvolutionParameteriv": long
readonly "glGetConvolutionParameterfv": long
readonly "glHistogram": long
readonly "glResetHistogram": long
readonly "glGetHistogram": long
readonly "glGetHistogramParameteriv": long
readonly "glGetHistogramParameterfv": long
readonly "glMinmax": long
readonly "glResetMinmax": long
readonly "glGetMinmax": long
readonly "glGetMinmaxParameteriv": long
readonly "glGetMinmaxParameterfv": long
readonly "glMultiDrawArraysIndirectCountARB": long
readonly "glMultiDrawElementsIndirectCountARB": long
readonly "glVertexAttribDivisorARB": long
readonly "glVertexArrayVertexAttribDivisorEXT": long
readonly "glCurrentPaletteMatrixARB": long
readonly "glMatrixIndexuivARB": long
readonly "glMatrixIndexubvARB": long
readonly "glMatrixIndexusvARB": long
readonly "glMatrixIndexPointerARB": long
readonly "glSampleCoverageARB": long
readonly "glActiveTextureARB": long
readonly "glClientActiveTextureARB": long
readonly "glMultiTexCoord1fARB": long
readonly "glMultiTexCoord1sARB": long
readonly "glMultiTexCoord1iARB": long
readonly "glMultiTexCoord1dARB": long
readonly "glMultiTexCoord1fvARB": long
readonly "glMultiTexCoord1svARB": long
readonly "glMultiTexCoord1ivARB": long
readonly "glMultiTexCoord1dvARB": long
readonly "glMultiTexCoord2fARB": long
readonly "glMultiTexCoord2sARB": long
readonly "glMultiTexCoord2iARB": long
readonly "glMultiTexCoord2dARB": long
readonly "glMultiTexCoord2fvARB": long
readonly "glMultiTexCoord2svARB": long
readonly "glMultiTexCoord2ivARB": long
readonly "glMultiTexCoord2dvARB": long
readonly "glMultiTexCoord3fARB": long
readonly "glMultiTexCoord3sARB": long
readonly "glMultiTexCoord3iARB": long
readonly "glMultiTexCoord3dARB": long
readonly "glMultiTexCoord3fvARB": long
readonly "glMultiTexCoord3svARB": long
readonly "glMultiTexCoord3ivARB": long
readonly "glMultiTexCoord3dvARB": long
readonly "glMultiTexCoord4fARB": long
readonly "glMultiTexCoord4sARB": long
readonly "glMultiTexCoord4iARB": long
readonly "glMultiTexCoord4dARB": long
readonly "glMultiTexCoord4fvARB": long
readonly "glMultiTexCoord4svARB": long
readonly "glMultiTexCoord4ivARB": long
readonly "glMultiTexCoord4dvARB": long
readonly "glGenQueriesARB": long
readonly "glDeleteQueriesARB": long
readonly "glIsQueryARB": long
readonly "glBeginQueryARB": long
readonly "glEndQueryARB": long
readonly "glGetQueryivARB": long
readonly "glGetQueryObjectivARB": long
readonly "glGetQueryObjectuivARB": long
readonly "glMaxShaderCompilerThreadsARB": long
readonly "glPointParameterfARB": long
readonly "glPointParameterfvARB": long
readonly "glGetGraphicsResetStatusARB": long
readonly "glGetnMapdvARB": long
readonly "glGetnMapfvARB": long
readonly "glGetnMapivARB": long
readonly "glGetnPixelMapfvARB": long
readonly "glGetnPixelMapuivARB": long
readonly "glGetnPixelMapusvARB": long
readonly "glGetnPolygonStippleARB": long
readonly "glGetnTexImageARB": long
readonly "glReadnPixelsARB": long
readonly "glGetnColorTableARB": long
readonly "glGetnConvolutionFilterARB": long
readonly "glGetnSeparableFilterARB": long
readonly "glGetnHistogramARB": long
readonly "glGetnMinmaxARB": long
readonly "glGetnCompressedTexImageARB": long
readonly "glGetnUniformfvARB": long
readonly "glGetnUniformivARB": long
readonly "glGetnUniformuivARB": long
readonly "glGetnUniformdvARB": long
readonly "glFramebufferSampleLocationsfvARB": long
readonly "glNamedFramebufferSampleLocationsfvARB": long
readonly "glEvaluateDepthValuesARB": long
readonly "glMinSampleShadingARB": long
readonly "glDeleteObjectARB": long
readonly "glGetHandleARB": long
readonly "glDetachObjectARB": long
readonly "glCreateShaderObjectARB": long
readonly "glShaderSourceARB": long
readonly "glCompileShaderARB": long
readonly "glCreateProgramObjectARB": long
readonly "glAttachObjectARB": long
readonly "glLinkProgramARB": long
readonly "glUseProgramObjectARB": long
readonly "glValidateProgramARB": long
readonly "glUniform1fARB": long
readonly "glUniform2fARB": long
readonly "glUniform3fARB": long
readonly "glUniform4fARB": long
readonly "glUniform1iARB": long
readonly "glUniform2iARB": long
readonly "glUniform3iARB": long
readonly "glUniform4iARB": long
readonly "glUniform1fvARB": long
readonly "glUniform2fvARB": long
readonly "glUniform3fvARB": long
readonly "glUniform4fvARB": long
readonly "glUniform1ivARB": long
readonly "glUniform2ivARB": long
readonly "glUniform3ivARB": long
readonly "glUniform4ivARB": long
readonly "glUniformMatrix2fvARB": long
readonly "glUniformMatrix3fvARB": long
readonly "glUniformMatrix4fvARB": long
readonly "glGetObjectParameterfvARB": long
readonly "glGetObjectParameterivARB": long
readonly "glGetInfoLogARB": long
readonly "glGetAttachedObjectsARB": long
readonly "glGetUniformLocationARB": long
readonly "glGetActiveUniformARB": long
readonly "glGetUniformfvARB": long
readonly "glGetUniformivARB": long
readonly "glGetShaderSourceARB": long
readonly "glNamedStringARB": long
readonly "glDeleteNamedStringARB": long
readonly "glCompileShaderIncludeARB": long
readonly "glIsNamedStringARB": long
readonly "glGetNamedStringARB": long
readonly "glGetNamedStringivARB": long
readonly "glBufferPageCommitmentARB": long
readonly "glNamedBufferPageCommitmentEXT": long
readonly "glNamedBufferPageCommitmentARB": long
readonly "glTexPageCommitmentARB": long
readonly "glTexturePageCommitmentEXT": long
readonly "glTexBufferARB": long
readonly "glTextureBufferRangeEXT": long
readonly "glCompressedTexImage3DARB": long
readonly "glCompressedTexImage2DARB": long
readonly "glCompressedTexImage1DARB": long
readonly "glCompressedTexSubImage3DARB": long
readonly "glCompressedTexSubImage2DARB": long
readonly "glCompressedTexSubImage1DARB": long
readonly "glGetCompressedTexImageARB": long
readonly "glTextureStorage1DEXT": long
readonly "glTextureStorage2DEXT": long
readonly "glTextureStorage3DEXT": long
readonly "glTextureStorage2DMultisampleEXT": long
readonly "glTextureStorage3DMultisampleEXT": long
readonly "glLoadTransposeMatrixfARB": long
readonly "glLoadTransposeMatrixdARB": long
readonly "glMultTransposeMatrixfARB": long
readonly "glMultTransposeMatrixdARB": long
readonly "glVertexArrayVertexAttribLOffsetEXT": long
readonly "glVertexArrayBindVertexBufferEXT": long
readonly "glVertexArrayVertexAttribFormatEXT": long
readonly "glVertexArrayVertexAttribIFormatEXT": long
readonly "glVertexArrayVertexAttribLFormatEXT": long
readonly "glVertexArrayVertexAttribBindingEXT": long
readonly "glVertexArrayVertexBindingDivisorEXT": long
readonly "glWeightfvARB": long
readonly "glWeightbvARB": long
readonly "glWeightubvARB": long
readonly "glWeightsvARB": long
readonly "glWeightusvARB": long
readonly "glWeightivARB": long
readonly "glWeightuivARB": long
readonly "glWeightdvARB": long
readonly "glWeightPointerARB": long
readonly "glVertexBlendARB": long
readonly "glBindBufferARB": long
readonly "glDeleteBuffersARB": long
readonly "glGenBuffersARB": long
readonly "glIsBufferARB": long
readonly "glBufferDataARB": long
readonly "glBufferSubDataARB": long
readonly "glGetBufferSubDataARB": long
readonly "glMapBufferARB": long
readonly "glUnmapBufferARB": long
readonly "glGetBufferParameterivARB": long
readonly "glGetBufferPointervARB": long
readonly "glVertexAttrib1sARB": long
readonly "glVertexAttrib1fARB": long
readonly "glVertexAttrib1dARB": long
readonly "glVertexAttrib2sARB": long
readonly "glVertexAttrib2fARB": long
readonly "glVertexAttrib2dARB": long
readonly "glVertexAttrib3sARB": long
readonly "glVertexAttrib3fARB": long
readonly "glVertexAttrib3dARB": long
readonly "glVertexAttrib4sARB": long
readonly "glVertexAttrib4fARB": long
readonly "glVertexAttrib4dARB": long
readonly "glVertexAttrib4NubARB": long
readonly "glVertexAttrib1svARB": long
readonly "glVertexAttrib1fvARB": long
readonly "glVertexAttrib1dvARB": long
readonly "glVertexAttrib2svARB": long
readonly "glVertexAttrib2fvARB": long
readonly "glVertexAttrib2dvARB": long
readonly "glVertexAttrib3svARB": long
readonly "glVertexAttrib3fvARB": long
readonly "glVertexAttrib3dvARB": long
readonly "glVertexAttrib4fvARB": long
readonly "glVertexAttrib4bvARB": long
readonly "glVertexAttrib4svARB": long
readonly "glVertexAttrib4ivARB": long
readonly "glVertexAttrib4ubvARB": long
readonly "glVertexAttrib4usvARB": long
readonly "glVertexAttrib4uivARB": long
readonly "glVertexAttrib4dvARB": long
readonly "glVertexAttrib4NbvARB": long
readonly "glVertexAttrib4NsvARB": long
readonly "glVertexAttrib4NivARB": long
readonly "glVertexAttrib4NubvARB": long
readonly "glVertexAttrib4NusvARB": long
readonly "glVertexAttrib4NuivARB": long
readonly "glVertexAttribPointerARB": long
readonly "glEnableVertexAttribArrayARB": long
readonly "glDisableVertexAttribArrayARB": long
readonly "glProgramStringARB": long
readonly "glBindProgramARB": long
readonly "glDeleteProgramsARB": long
readonly "glGenProgramsARB": long
readonly "glProgramEnvParameter4dARB": long
readonly "glProgramEnvParameter4dvARB": long
readonly "glProgramEnvParameter4fARB": long
readonly "glProgramEnvParameter4fvARB": long
readonly "glProgramLocalParameter4dARB": long
readonly "glProgramLocalParameter4dvARB": long
readonly "glProgramLocalParameter4fARB": long
readonly "glProgramLocalParameter4fvARB": long
readonly "glGetProgramEnvParameterfvARB": long
readonly "glGetProgramEnvParameterdvARB": long
readonly "glGetProgramLocalParameterfvARB": long
readonly "glGetProgramLocalParameterdvARB": long
readonly "glGetProgramivARB": long
readonly "glGetProgramStringARB": long
readonly "glGetVertexAttribfvARB": long
readonly "glGetVertexAttribdvARB": long
readonly "glGetVertexAttribivARB": long
readonly "glGetVertexAttribPointervARB": long
readonly "glIsProgramARB": long
readonly "glBindAttribLocationARB": long
readonly "glGetActiveAttribARB": long
readonly "glGetAttribLocationARB": long
readonly "glWindowPos2iARB": long
readonly "glWindowPos2sARB": long
readonly "glWindowPos2fARB": long
readonly "glWindowPos2dARB": long
readonly "glWindowPos2ivARB": long
readonly "glWindowPos2svARB": long
readonly "glWindowPos2fvARB": long
readonly "glWindowPos2dvARB": long
readonly "glWindowPos3iARB": long
readonly "glWindowPos3sARB": long
readonly "glWindowPos3fARB": long
readonly "glWindowPos3dARB": long
readonly "glWindowPos3ivARB": long
readonly "glWindowPos3svARB": long
readonly "glWindowPos3fvARB": long
readonly "glWindowPos3dvARB": long
readonly "glUniformBufferEXT": long
readonly "glGetUniformBufferSizeEXT": long
readonly "glGetUniformOffsetEXT": long
readonly "glBlendColorEXT": long
readonly "glBlendEquationSeparateEXT": long
readonly "glBlendFuncSeparateEXT": long
readonly "glBlendEquationEXT": long
readonly "glLockArraysEXT": long
readonly "glUnlockArraysEXT": long
readonly "glLabelObjectEXT": long
readonly "glGetObjectLabelEXT": long
readonly "glInsertEventMarkerEXT": long
readonly "glPushGroupMarkerEXT": long
readonly "glPopGroupMarkerEXT": long
readonly "glDepthBoundsEXT": long
readonly "glClientAttribDefaultEXT": long
readonly "glPushClientAttribDefaultEXT": long
readonly "glMatrixLoadfEXT": long
readonly "glMatrixLoaddEXT": long
readonly "glMatrixMultfEXT": long
readonly "glMatrixMultdEXT": long
readonly "glMatrixLoadIdentityEXT": long
readonly "glMatrixRotatefEXT": long
readonly "glMatrixRotatedEXT": long
readonly "glMatrixScalefEXT": long
readonly "glMatrixScaledEXT": long
readonly "glMatrixTranslatefEXT": long
readonly "glMatrixTranslatedEXT": long
readonly "glMatrixOrthoEXT": long
readonly "glMatrixFrustumEXT": long
readonly "glMatrixPushEXT": long
readonly "glMatrixPopEXT": long
readonly "glTextureParameteriEXT": long
readonly "glTextureParameterivEXT": long
readonly "glTextureParameterfEXT": long
readonly "glTextureParameterfvEXT": long
readonly "glTextureImage1DEXT": long
readonly "glTextureImage2DEXT": long
readonly "glTextureSubImage1DEXT": long
readonly "glTextureSubImage2DEXT": long
readonly "glCopyTextureImage1DEXT": long
readonly "glCopyTextureImage2DEXT": long
readonly "glCopyTextureSubImage1DEXT": long
readonly "glCopyTextureSubImage2DEXT": long
readonly "glGetTextureImageEXT": long
readonly "glGetTextureParameterfvEXT": long
readonly "glGetTextureParameterivEXT": long
readonly "glGetTextureLevelParameterfvEXT": long
readonly "glGetTextureLevelParameterivEXT": long
readonly "glTextureImage3DEXT": long
readonly "glTextureSubImage3DEXT": long
readonly "glCopyTextureSubImage3DEXT": long
readonly "glBindMultiTextureEXT": long
readonly "glMultiTexCoordPointerEXT": long
readonly "glMultiTexEnvfEXT": long
readonly "glMultiTexEnvfvEXT": long
readonly "glMultiTexEnviEXT": long
readonly "glMultiTexEnvivEXT": long
readonly "glMultiTexGendEXT": long
readonly "glMultiTexGendvEXT": long
readonly "glMultiTexGenfEXT": long
readonly "glMultiTexGenfvEXT": long
readonly "glMultiTexGeniEXT": long
readonly "glMultiTexGenivEXT": long
readonly "glGetMultiTexEnvfvEXT": long
readonly "glGetMultiTexEnvivEXT": long
readonly "glGetMultiTexGendvEXT": long
readonly "glGetMultiTexGenfvEXT": long
readonly "glGetMultiTexGenivEXT": long
readonly "glMultiTexParameteriEXT": long
readonly "glMultiTexParameterivEXT": long
readonly "glMultiTexParameterfEXT": long
readonly "glMultiTexParameterfvEXT": long
readonly "glMultiTexImage1DEXT": long
readonly "glMultiTexImage2DEXT": long
readonly "glMultiTexSubImage1DEXT": long
readonly "glMultiTexSubImage2DEXT": long
readonly "glCopyMultiTexImage1DEXT": long
readonly "glCopyMultiTexImage2DEXT": long
readonly "glCopyMultiTexSubImage1DEXT": long
readonly "glCopyMultiTexSubImage2DEXT": long
readonly "glGetMultiTexImageEXT": long
readonly "glGetMultiTexParameterfvEXT": long
readonly "glGetMultiTexParameterivEXT": long
readonly "glGetMultiTexLevelParameterfvEXT": long
readonly "glGetMultiTexLevelParameterivEXT": long
readonly "glMultiTexImage3DEXT": long
readonly "glMultiTexSubImage3DEXT": long
readonly "glCopyMultiTexSubImage3DEXT": long
readonly "glEnableClientStateIndexedEXT": long
readonly "glDisableClientStateIndexedEXT": long
readonly "glEnableClientStateiEXT": long
readonly "glDisableClientStateiEXT": long
readonly "glGetFloatIndexedvEXT": long
readonly "glGetDoubleIndexedvEXT": long
readonly "glGetPointerIndexedvEXT": long
readonly "glGetFloati_vEXT": long
readonly "glGetDoublei_vEXT": long
readonly "glGetPointeri_vEXT": long
readonly "glEnableIndexedEXT": long
readonly "glDisableIndexedEXT": long
readonly "glIsEnabledIndexedEXT": long
readonly "glGetIntegerIndexedvEXT": long
readonly "glGetBooleanIndexedvEXT": long
readonly "glNamedProgramStringEXT": long
readonly "glNamedProgramLocalParameter4dEXT": long
readonly "glNamedProgramLocalParameter4dvEXT": long
readonly "glNamedProgramLocalParameter4fEXT": long
readonly "glNamedProgramLocalParameter4fvEXT": long
readonly "glGetNamedProgramLocalParameterdvEXT": long
readonly "glGetNamedProgramLocalParameterfvEXT": long
readonly "glGetNamedProgramivEXT": long
readonly "glGetNamedProgramStringEXT": long
readonly "glCompressedTextureImage3DEXT": long
readonly "glCompressedTextureImage2DEXT": long
readonly "glCompressedTextureImage1DEXT": long
readonly "glCompressedTextureSubImage3DEXT": long
readonly "glCompressedTextureSubImage2DEXT": long
readonly "glCompressedTextureSubImage1DEXT": long
readonly "glGetCompressedTextureImageEXT": long
readonly "glCompressedMultiTexImage3DEXT": long
readonly "glCompressedMultiTexImage2DEXT": long
readonly "glCompressedMultiTexImage1DEXT": long
readonly "glCompressedMultiTexSubImage3DEXT": long
readonly "glCompressedMultiTexSubImage2DEXT": long
readonly "glCompressedMultiTexSubImage1DEXT": long
readonly "glGetCompressedMultiTexImageEXT": long
readonly "glMatrixLoadTransposefEXT": long
readonly "glMatrixLoadTransposedEXT": long
readonly "glMatrixMultTransposefEXT": long
readonly "glMatrixMultTransposedEXT": long
readonly "glNamedBufferDataEXT": long
readonly "glNamedBufferSubDataEXT": long
readonly "glMapNamedBufferEXT": long
readonly "glUnmapNamedBufferEXT": long
readonly "glGetNamedBufferParameterivEXT": long
readonly "glGetNamedBufferSubDataEXT": long
readonly "glProgramUniform1fEXT": long
readonly "glProgramUniform2fEXT": long
readonly "glProgramUniform3fEXT": long
readonly "glProgramUniform4fEXT": long
readonly "glProgramUniform1iEXT": long
readonly "glProgramUniform2iEXT": long
readonly "glProgramUniform3iEXT": long
readonly "glProgramUniform4iEXT": long
readonly "glProgramUniform1fvEXT": long
readonly "glProgramUniform2fvEXT": long
readonly "glProgramUniform3fvEXT": long
readonly "glProgramUniform4fvEXT": long
readonly "glProgramUniform1ivEXT": long
readonly "glProgramUniform2ivEXT": long
readonly "glProgramUniform3ivEXT": long
readonly "glProgramUniform4ivEXT": long
readonly "glProgramUniformMatrix2fvEXT": long
readonly "glProgramUniformMatrix3fvEXT": long
readonly "glProgramUniformMatrix4fvEXT": long
readonly "glProgramUniformMatrix2x3fvEXT": long
readonly "glProgramUniformMatrix3x2fvEXT": long
readonly "glProgramUniformMatrix2x4fvEXT": long
readonly "glProgramUniformMatrix4x2fvEXT": long
readonly "glProgramUniformMatrix3x4fvEXT": long
readonly "glProgramUniformMatrix4x3fvEXT": long
readonly "glTextureBufferEXT": long
readonly "glMultiTexBufferEXT": long
readonly "glTextureParameterIivEXT": long
readonly "glTextureParameterIuivEXT": long
readonly "glGetTextureParameterIivEXT": long
readonly "glGetTextureParameterIuivEXT": long
readonly "glMultiTexParameterIivEXT": long
readonly "glMultiTexParameterIuivEXT": long
readonly "glGetMultiTexParameterIivEXT": long
readonly "glGetMultiTexParameterIuivEXT": long
readonly "glProgramUniform1uiEXT": long
readonly "glProgramUniform2uiEXT": long
readonly "glProgramUniform3uiEXT": long
readonly "glProgramUniform4uiEXT": long
readonly "glProgramUniform1uivEXT": long
readonly "glProgramUniform2uivEXT": long
readonly "glProgramUniform3uivEXT": long
readonly "glProgramUniform4uivEXT": long
readonly "glNamedProgramLocalParameters4fvEXT": long
readonly "glNamedProgramLocalParameterI4iEXT": long
readonly "glNamedProgramLocalParameterI4ivEXT": long
readonly "glNamedProgramLocalParametersI4ivEXT": long
readonly "glNamedProgramLocalParameterI4uiEXT": long
readonly "glNamedProgramLocalParameterI4uivEXT": long
readonly "glNamedProgramLocalParametersI4uivEXT": long
readonly "glGetNamedProgramLocalParameterIivEXT": long
readonly "glGetNamedProgramLocalParameterIuivEXT": long
readonly "glNamedRenderbufferStorageEXT": long
readonly "glGetNamedRenderbufferParameterivEXT": long
readonly "glNamedRenderbufferStorageMultisampleEXT": long
readonly "glNamedRenderbufferStorageMultisampleCoverageEXT": long
readonly "glCheckNamedFramebufferStatusEXT": long
readonly "glNamedFramebufferTexture1DEXT": long
readonly "glNamedFramebufferTexture2DEXT": long
readonly "glNamedFramebufferTexture3DEXT": long
readonly "glNamedFramebufferRenderbufferEXT": long
readonly "glGetNamedFramebufferAttachmentParameterivEXT": long
readonly "glGenerateTextureMipmapEXT": long
readonly "glGenerateMultiTexMipmapEXT": long
readonly "glFramebufferDrawBufferEXT": long
readonly "glFramebufferDrawBuffersEXT": long
readonly "glFramebufferReadBufferEXT": long
readonly "glGetFramebufferParameterivEXT": long
readonly "glNamedCopyBufferSubDataEXT": long
readonly "glNamedFramebufferTextureEXT": long
readonly "glNamedFramebufferTextureLayerEXT": long
readonly "glNamedFramebufferTextureFaceEXT": long
readonly "glTextureRenderbufferEXT": long
readonly "glMultiTexRenderbufferEXT": long
readonly "glVertexArrayVertexOffsetEXT": long
readonly "glVertexArrayColorOffsetEXT": long
readonly "glVertexArrayEdgeFlagOffsetEXT": long
readonly "glVertexArrayIndexOffsetEXT": long
readonly "glVertexArrayNormalOffsetEXT": long
readonly "glVertexArrayTexCoordOffsetEXT": long
readonly "glVertexArrayMultiTexCoordOffsetEXT": long
readonly "glVertexArrayFogCoordOffsetEXT": long
readonly "glVertexArraySecondaryColorOffsetEXT": long
readonly "glVertexArrayVertexAttribOffsetEXT": long
readonly "glVertexArrayVertexAttribIOffsetEXT": long
readonly "glEnableVertexArrayEXT": long
readonly "glDisableVertexArrayEXT": long
readonly "glEnableVertexArrayAttribEXT": long
readonly "glDisableVertexArrayAttribEXT": long
readonly "glGetVertexArrayIntegervEXT": long
readonly "glGetVertexArrayPointervEXT": long
readonly "glGetVertexArrayIntegeri_vEXT": long
readonly "glGetVertexArrayPointeri_vEXT": long
readonly "glMapNamedBufferRangeEXT": long
readonly "glFlushMappedNamedBufferRangeEXT": long
readonly "glColorMaskIndexedEXT": long
readonly "glDrawArraysInstancedEXT": long
readonly "glDrawElementsInstancedEXT": long
readonly "glEGLImageTargetTexStorageEXT": long
readonly "glEGLImageTargetTextureStorageEXT": long
readonly "glBufferStorageExternalEXT": long
readonly "glNamedBufferStorageExternalEXT": long
readonly "glBlitFramebufferEXT": long
readonly "glRenderbufferStorageMultisampleEXT": long
readonly "glIsRenderbufferEXT": long
readonly "glBindRenderbufferEXT": long
readonly "glDeleteRenderbuffersEXT": long
readonly "glGenRenderbuffersEXT": long
readonly "glRenderbufferStorageEXT": long
readonly "glGetRenderbufferParameterivEXT": long
readonly "glIsFramebufferEXT": long
readonly "glBindFramebufferEXT": long
readonly "glDeleteFramebuffersEXT": long
readonly "glGenFramebuffersEXT": long
readonly "glCheckFramebufferStatusEXT": long
readonly "glFramebufferTexture1DEXT": long
readonly "glFramebufferTexture2DEXT": long
readonly "glFramebufferTexture3DEXT": long
readonly "glFramebufferRenderbufferEXT": long
readonly "glGetFramebufferAttachmentParameterivEXT": long
readonly "glGenerateMipmapEXT": long
readonly "glProgramParameteriEXT": long
readonly "glFramebufferTextureEXT": long
readonly "glFramebufferTextureLayerEXT": long
readonly "glFramebufferTextureFaceEXT": long
readonly "glProgramEnvParameters4fvEXT": long
readonly "glProgramLocalParameters4fvEXT": long
readonly "glVertexAttribI1iEXT": long
readonly "glVertexAttribI2iEXT": long
readonly "glVertexAttribI3iEXT": long
readonly "glVertexAttribI4iEXT": long
readonly "glVertexAttribI1uiEXT": long
readonly "glVertexAttribI2uiEXT": long
readonly "glVertexAttribI3uiEXT": long
readonly "glVertexAttribI4uiEXT": long
readonly "glVertexAttribI1ivEXT": long
readonly "glVertexAttribI2ivEXT": long
readonly "glVertexAttribI3ivEXT": long
readonly "glVertexAttribI4ivEXT": long
readonly "glVertexAttribI1uivEXT": long
readonly "glVertexAttribI2uivEXT": long
readonly "glVertexAttribI3uivEXT": long
readonly "glVertexAttribI4uivEXT": long
readonly "glVertexAttribI4bvEXT": long
readonly "glVertexAttribI4svEXT": long
readonly "glVertexAttribI4ubvEXT": long
readonly "glVertexAttribI4usvEXT": long
readonly "glVertexAttribIPointerEXT": long
readonly "glGetVertexAttribIivEXT": long
readonly "glGetVertexAttribIuivEXT": long
readonly "glGetUniformuivEXT": long
readonly "glBindFragDataLocationEXT": long
readonly "glGetFragDataLocationEXT": long
readonly "glUniform1uiEXT": long
readonly "glUniform2uiEXT": long
readonly "glUniform3uiEXT": long
readonly "glUniform4uiEXT": long
readonly "glUniform1uivEXT": long
readonly "glUniform2uivEXT": long
readonly "glUniform3uivEXT": long
readonly "glUniform4uivEXT": long
readonly "glGetUnsignedBytevEXT": long
readonly "glGetUnsignedBytei_vEXT": long
readonly "glDeleteMemoryObjectsEXT": long
readonly "glIsMemoryObjectEXT": long
readonly "glCreateMemoryObjectsEXT": long
readonly "glMemoryObjectParameterivEXT": long
readonly "glGetMemoryObjectParameterivEXT": long
readonly "glTexStorageMem2DEXT": long
readonly "glTexStorageMem2DMultisampleEXT": long
readonly "glTexStorageMem3DEXT": long
readonly "glTexStorageMem3DMultisampleEXT": long
readonly "glBufferStorageMemEXT": long
readonly "glTextureStorageMem2DEXT": long
readonly "glTextureStorageMem2DMultisampleEXT": long
readonly "glTextureStorageMem3DEXT": long
readonly "glTextureStorageMem3DMultisampleEXT": long
readonly "glNamedBufferStorageMemEXT": long
readonly "glTexStorageMem1DEXT": long
readonly "glTextureStorageMem1DEXT": long
readonly "glImportMemoryFdEXT": long
readonly "glImportMemoryWin32HandleEXT": long
readonly "glImportMemoryWin32NameEXT": long
readonly "glPointParameterfEXT": long
readonly "glPointParameterfvEXT": long
readonly "glPolygonOffsetClampEXT": long
readonly "glProvokingVertexEXT": long
readonly "glRasterSamplesEXT": long
readonly "glSecondaryColor3bEXT": long
readonly "glSecondaryColor3sEXT": long
readonly "glSecondaryColor3iEXT": long
readonly "glSecondaryColor3fEXT": long
readonly "glSecondaryColor3dEXT": long
readonly "glSecondaryColor3ubEXT": long
readonly "glSecondaryColor3usEXT": long
readonly "glSecondaryColor3uiEXT": long
readonly "glSecondaryColor3bvEXT": long
readonly "glSecondaryColor3svEXT": long
readonly "glSecondaryColor3ivEXT": long
readonly "glSecondaryColor3fvEXT": long
readonly "glSecondaryColor3dvEXT": long
readonly "glSecondaryColor3ubvEXT": long
readonly "glSecondaryColor3usvEXT": long
readonly "glSecondaryColor3uivEXT": long
readonly "glSecondaryColorPointerEXT": long
readonly "glGenSemaphoresEXT": long
readonly "glDeleteSemaphoresEXT": long
readonly "glIsSemaphoreEXT": long
readonly "glSemaphoreParameterui64vEXT": long
readonly "glGetSemaphoreParameterui64vEXT": long
readonly "glWaitSemaphoreEXT": long
readonly "glSignalSemaphoreEXT": long
readonly "glImportSemaphoreFdEXT": long
readonly "glImportSemaphoreWin32HandleEXT": long
readonly "glImportSemaphoreWin32NameEXT": long
readonly "glUseShaderProgramEXT": long
readonly "glActiveProgramEXT": long
readonly "glCreateShaderProgramEXT": long
readonly "glFramebufferFetchBarrierEXT": long
readonly "glBindImageTextureEXT": long
readonly "glMemoryBarrierEXT": long
readonly "glStencilClearTagEXT": long
readonly "glActiveStencilFaceEXT": long
readonly "glTexBufferEXT": long
readonly "glClearColorIiEXT": long
readonly "glClearColorIuiEXT": long
readonly "glTexParameterIivEXT": long
readonly "glTexParameterIuivEXT": long
readonly "glGetTexParameterIivEXT": long
readonly "glGetTexParameterIuivEXT": long
readonly "glTexStorage1DEXT": long
readonly "glTexStorage2DEXT": long
readonly "glTexStorage3DEXT": long
readonly "glGetQueryObjecti64vEXT": long
readonly "glGetQueryObjectui64vEXT": long
readonly "glBindBufferRangeEXT": long
readonly "glBindBufferOffsetEXT": long
readonly "glBindBufferBaseEXT": long
readonly "glBeginTransformFeedbackEXT": long
readonly "glEndTransformFeedbackEXT": long
readonly "glTransformFeedbackVaryingsEXT": long
readonly "glGetTransformFeedbackVaryingEXT": long
readonly "glVertexAttribL1dEXT": long
readonly "glVertexAttribL2dEXT": long
readonly "glVertexAttribL3dEXT": long
readonly "glVertexAttribL4dEXT": long
readonly "glVertexAttribL1dvEXT": long
readonly "glVertexAttribL2dvEXT": long
readonly "glVertexAttribL3dvEXT": long
readonly "glVertexAttribL4dvEXT": long
readonly "glVertexAttribLPointerEXT": long
readonly "glGetVertexAttribLdvEXT": long
readonly "glAcquireKeyedMutexWin32EXT": long
readonly "glReleaseKeyedMutexWin32EXT": long
readonly "glWindowRectanglesEXT": long
readonly "glImportSyncEXT": long
readonly "glFrameTerminatorGREMEDY": long
readonly "glStringMarkerGREMEDY": long
readonly "glApplyFramebufferAttachmentCMAAINTEL": long
readonly "glSyncTextureINTEL": long
readonly "glUnmapTexture2DINTEL": long
readonly "glMapTexture2DINTEL": long
readonly "glBeginPerfQueryINTEL": long
readonly "glCreatePerfQueryINTEL": long
readonly "glDeletePerfQueryINTEL": long
readonly "glEndPerfQueryINTEL": long
readonly "glGetFirstPerfQueryIdINTEL": long
readonly "glGetNextPerfQueryIdINTEL": long
readonly "glGetPerfCounterInfoINTEL": long
readonly "glGetPerfQueryDataINTEL": long
readonly "glGetPerfQueryIdByNameINTEL": long
readonly "glGetPerfQueryInfoINTEL": long
readonly "glBlendBarrierKHR": long
readonly "glMaxShaderCompilerThreadsKHR": long
readonly "glFramebufferParameteriMESA": long
readonly "glGetFramebufferParameterivMESA": long
readonly "glAlphaToCoverageDitherControlNV": long
readonly "glMultiDrawArraysIndirectBindlessNV": long
readonly "glMultiDrawElementsIndirectBindlessNV": long
readonly "glMultiDrawArraysIndirectBindlessCountNV": long
readonly "glMultiDrawElementsIndirectBindlessCountNV": long
readonly "glGetTextureHandleNV": long
readonly "glGetTextureSamplerHandleNV": long
readonly "glMakeTextureHandleResidentNV": long
readonly "glMakeTextureHandleNonResidentNV": long
readonly "glGetImageHandleNV": long
readonly "glMakeImageHandleResidentNV": long
readonly "glMakeImageHandleNonResidentNV": long
readonly "glUniformHandleui64NV": long
readonly "glUniformHandleui64vNV": long
readonly "glProgramUniformHandleui64NV": long
readonly "glProgramUniformHandleui64vNV": long
readonly "glIsTextureHandleResidentNV": long
readonly "glIsImageHandleResidentNV": long
readonly "glBlendParameteriNV": long
readonly "glBlendBarrierNV": long
readonly "glViewportPositionWScaleNV": long
readonly "glCreateStatesNV": long
readonly "glDeleteStatesNV": long
readonly "glIsStateNV": long
readonly "glStateCaptureNV": long
readonly "glGetCommandHeaderNV": long
readonly "glGetStageIndexNV": long
readonly "glDrawCommandsNV": long
readonly "glDrawCommandsAddressNV": long
readonly "glDrawCommandsStatesNV": long
readonly "glDrawCommandsStatesAddressNV": long
readonly "glCreateCommandListsNV": long
readonly "glDeleteCommandListsNV": long
readonly "glIsCommandListNV": long
readonly "glListDrawCommandsStatesClientNV": long
readonly "glCommandListSegmentsNV": long
readonly "glCompileCommandListNV": long
readonly "glCallCommandListNV": long
readonly "glBeginConditionalRenderNV": long
readonly "glEndConditionalRenderNV": long
readonly "glSubpixelPrecisionBiasNV": long
readonly "glConservativeRasterParameterfNV": long
readonly "glConservativeRasterParameteriNV": long
readonly "glCopyImageSubDataNV": long
readonly "glDepthRangedNV": long
readonly "glClearDepthdNV": long
readonly "glDepthBoundsdNV": long
readonly "glDrawTextureNV": long
readonly "glDrawVkImageNV": long
readonly "glGetVkProcAddrNV": long
readonly "glWaitVkSemaphoreNV": long
readonly "glSignalVkSemaphoreNV": long
readonly "glSignalVkFenceNV": long
readonly "glGetMultisamplefvNV": long
readonly "glSampleMaskIndexedNV": long
readonly "glTexRenderbufferNV": long
readonly "glDeleteFencesNV": long
readonly "glGenFencesNV": long
readonly "glIsFenceNV": long
readonly "glTestFenceNV": long
readonly "glGetFenceivNV": long
readonly "glFinishFenceNV": long
readonly "glSetFenceNV": long
readonly "glFragmentCoverageColorNV": long
readonly "glCoverageModulationTableNV": long
readonly "glGetCoverageModulationTableNV": long
readonly "glCoverageModulationNV": long
readonly "glRenderbufferStorageMultisampleCoverageNV": long
readonly "glRenderGpuMaskNV": long
readonly "glMulticastBufferSubDataNV": long
readonly "glMulticastCopyBufferSubDataNV": long
readonly "glMulticastCopyImageSubDataNV": long
readonly "glMulticastBlitFramebufferNV": long
readonly "glMulticastFramebufferSampleLocationsfvNV": long
readonly "glMulticastBarrierNV": long
readonly "glMulticastWaitSyncNV": long
readonly "glMulticastGetQueryObjectivNV": long
readonly "glMulticastGetQueryObjectuivNV": long
readonly "glMulticastGetQueryObjecti64vNV": long
readonly "glMulticastGetQueryObjectui64vNV": long
readonly "glVertex2hNV": long
readonly "glVertex2hvNV": long
readonly "glVertex3hNV": long
readonly "glVertex3hvNV": long
readonly "glVertex4hNV": long
readonly "glVertex4hvNV": long
readonly "glNormal3hNV": long
readonly "glNormal3hvNV": long
readonly "glColor3hNV": long
readonly "glColor3hvNV": long
readonly "glColor4hNV": long
readonly "glColor4hvNV": long
readonly "glTexCoord1hNV": long
readonly "glTexCoord1hvNV": long
readonly "glTexCoord2hNV": long
readonly "glTexCoord2hvNV": long
readonly "glTexCoord3hNV": long
readonly "glTexCoord3hvNV": long
readonly "glTexCoord4hNV": long
readonly "glTexCoord4hvNV": long
readonly "glMultiTexCoord1hNV": long
readonly "glMultiTexCoord1hvNV": long
readonly "glMultiTexCoord2hNV": long
readonly "glMultiTexCoord2hvNV": long
readonly "glMultiTexCoord3hNV": long
readonly "glMultiTexCoord3hvNV": long
readonly "glMultiTexCoord4hNV": long
readonly "glMultiTexCoord4hvNV": long
readonly "glFogCoordhNV": long
readonly "glFogCoordhvNV": long
readonly "glSecondaryColor3hNV": long
readonly "glSecondaryColor3hvNV": long
readonly "glVertexWeighthNV": long
readonly "glVertexWeighthvNV": long
readonly "glVertexAttrib1hNV": long
readonly "glVertexAttrib1hvNV": long
readonly "glVertexAttrib2hNV": long
readonly "glVertexAttrib2hvNV": long
readonly "glVertexAttrib3hNV": long
readonly "glVertexAttrib3hvNV": long
readonly "glVertexAttrib4hNV": long
readonly "glVertexAttrib4hvNV": long
readonly "glVertexAttribs1hvNV": long
readonly "glVertexAttribs2hvNV": long
readonly "glVertexAttribs3hvNV": long
readonly "glVertexAttribs4hvNV": long
readonly "glGetInternalformatSampleivNV": long
readonly "glGetMemoryObjectDetachedResourcesuivNV": long
readonly "glResetMemoryObjectParameterNV": long
readonly "glTexAttachMemoryNV": long
readonly "glBufferAttachMemoryNV": long
readonly "glTextureAttachMemoryNV": long
readonly "glNamedBufferAttachMemoryNV": long
readonly "glBufferPageCommitmentMemNV": long
readonly "glNamedBufferPageCommitmentMemNV": long
readonly "glTexPageCommitmentMemNV": long
readonly "glTexturePageCommitmentMemNV": long
readonly "glDrawMeshTasksNV": long
readonly "glDrawMeshTasksIndirectNV": long
readonly "glMultiDrawMeshTasksIndirectNV": long
readonly "glMultiDrawMeshTasksIndirectCountNV": long
readonly "glPathCommandsNV": long
readonly "glPathCoordsNV": long
readonly "glPathSubCommandsNV": long
readonly "glPathSubCoordsNV": long
readonly "glPathStringNV": long
readonly "glPathGlyphsNV": long
readonly "glPathGlyphRangeNV": long
readonly "glPathGlyphIndexArrayNV": long
readonly "glPathMemoryGlyphIndexArrayNV": long
readonly "glCopyPathNV": long
readonly "glWeightPathsNV": long
readonly "glInterpolatePathsNV": long
readonly "glTransformPathNV": long
readonly "glPathParameterivNV": long
readonly "glPathParameteriNV": long
readonly "glPathParameterfvNV": long
readonly "glPathParameterfNV": long
readonly "glPathDashArrayNV": long
readonly "glGenPathsNV": long
readonly "glDeletePathsNV": long
readonly "glIsPathNV": long
readonly "glPathStencilFuncNV": long
readonly "glPathStencilDepthOffsetNV": long
readonly "glStencilFillPathNV": long
readonly "glStencilStrokePathNV": long
readonly "glStencilFillPathInstancedNV": long
readonly "glStencilStrokePathInstancedNV": long
readonly "glPathCoverDepthFuncNV": long
readonly "glPathColorGenNV": long
readonly "glPathTexGenNV": long
readonly "glPathFogGenNV": long
readonly "glCoverFillPathNV": long
readonly "glCoverStrokePathNV": long
readonly "glCoverFillPathInstancedNV": long
readonly "glCoverStrokePathInstancedNV": long
readonly "glStencilThenCoverFillPathNV": long
readonly "glStencilThenCoverStrokePathNV": long
readonly "glStencilThenCoverFillPathInstancedNV": long
readonly "glStencilThenCoverStrokePathInstancedNV": long
readonly "glPathGlyphIndexRangeNV": long
readonly "glProgramPathFragmentInputGenNV": long
readonly "glGetPathParameterivNV": long
readonly "glGetPathParameterfvNV": long
readonly "glGetPathCommandsNV": long
readonly "glGetPathCoordsNV": long
readonly "glGetPathDashArrayNV": long
readonly "glGetPathMetricsNV": long
readonly "glGetPathMetricRangeNV": long
readonly "glGetPathSpacingNV": long
readonly "glGetPathColorGenivNV": long
readonly "glGetPathColorGenfvNV": long
readonly "glGetPathTexGenivNV": long
readonly "glGetPathTexGenfvNV": long
readonly "glIsPointInFillPathNV": long
readonly "glIsPointInStrokePathNV": long
readonly "glGetPathLengthNV": long
readonly "glPointAlongPathNV": long
readonly "glMatrixLoad3x2fNV": long
readonly "glMatrixLoad3x3fNV": long
readonly "glMatrixLoadTranspose3x3fNV": long
readonly "glMatrixMult3x2fNV": long
readonly "glMatrixMult3x3fNV": long
readonly "glMatrixMultTranspose3x3fNV": long
readonly "glGetProgramResourcefvNV": long
readonly "glPixelDataRangeNV": long
readonly "glFlushPixelDataRangeNV": long
readonly "glPointParameteriNV": long
readonly "glPointParameterivNV": long
readonly "glPrimitiveRestartNV": long
readonly "glPrimitiveRestartIndexNV": long
readonly "glQueryResourceNV": long
readonly "glGenQueryResourceTagNV": long
readonly "glDeleteQueryResourceTagNV": long
readonly "glQueryResourceTagNV": long
readonly "glFramebufferSampleLocationsfvNV": long
readonly "glNamedFramebufferSampleLocationsfvNV": long
readonly "glResolveDepthValuesNV": long
readonly "glScissorExclusiveArrayvNV": long
readonly "glScissorExclusiveNV": long
readonly "glMakeBufferResidentNV": long
readonly "glMakeBufferNonResidentNV": long
readonly "glIsBufferResidentNV": long
readonly "glMakeNamedBufferResidentNV": long
readonly "glMakeNamedBufferNonResidentNV": long
readonly "glIsNamedBufferResidentNV": long
readonly "glGetBufferParameterui64vNV": long
readonly "glGetNamedBufferParameterui64vNV": long
readonly "glGetIntegerui64vNV": long
readonly "glUniformui64NV": long
readonly "glUniformui64vNV": long
readonly "glProgramUniformui64NV": long
readonly "glProgramUniformui64vNV": long
readonly "glBindShadingRateImageNV": long
readonly "glShadingRateImagePaletteNV": long
readonly "glGetShadingRateImagePaletteNV": long
readonly "glShadingRateImageBarrierNV": long
readonly "glShadingRateSampleOrderNV": long
readonly "glShadingRateSampleOrderCustomNV": long
readonly "glGetShadingRateSampleLocationivNV": long
readonly "glTextureBarrierNV": long
readonly "glTexImage2DMultisampleCoverageNV": long
readonly "glTexImage3DMultisampleCoverageNV": long
readonly "glTextureImage2DMultisampleNV": long
readonly "glTextureImage3DMultisampleNV": long
readonly "glTextureImage2DMultisampleCoverageNV": long
readonly "glTextureImage3DMultisampleCoverageNV": long
readonly "glCreateSemaphoresNV": long
readonly "glSemaphoreParameterivNV": long
readonly "glGetSemaphoreParameterivNV": long
readonly "glBeginTransformFeedbackNV": long
readonly "glEndTransformFeedbackNV": long
readonly "glTransformFeedbackAttribsNV": long
readonly "glBindBufferRangeNV": long
readonly "glBindBufferOffsetNV": long
readonly "glBindBufferBaseNV": long
readonly "glTransformFeedbackVaryingsNV": long
readonly "glActiveVaryingNV": long
readonly "glGetVaryingLocationNV": long
readonly "glGetActiveVaryingNV": long
readonly "glGetTransformFeedbackVaryingNV": long
readonly "glTransformFeedbackStreamAttribsNV": long
readonly "glBindTransformFeedbackNV": long
readonly "glDeleteTransformFeedbacksNV": long
readonly "glGenTransformFeedbacksNV": long
readonly "glIsTransformFeedbackNV": long
readonly "glPauseTransformFeedbackNV": long
readonly "glResumeTransformFeedbackNV": long
readonly "glDrawTransformFeedbackNV": long
readonly "glVertexArrayRangeNV": long
readonly "glFlushVertexArrayRangeNV": long
readonly "glVertexAttribL1i64NV": long
readonly "glVertexAttribL2i64NV": long
readonly "glVertexAttribL3i64NV": long
readonly "glVertexAttribL4i64NV": long
readonly "glVertexAttribL1i64vNV": long
readonly "glVertexAttribL2i64vNV": long
readonly "glVertexAttribL3i64vNV": long
readonly "glVertexAttribL4i64vNV": long
readonly "glVertexAttribL1ui64NV": long
readonly "glVertexAttribL2ui64NV": long
readonly "glVertexAttribL3ui64NV": long
readonly "glVertexAttribL4ui64NV": long
readonly "glVertexAttribL1ui64vNV": long
readonly "glVertexAttribL2ui64vNV": long
readonly "glVertexAttribL3ui64vNV": long
readonly "glVertexAttribL4ui64vNV": long
readonly "glGetVertexAttribLi64vNV": long
readonly "glGetVertexAttribLui64vNV": long
readonly "glVertexAttribLFormatNV": long
readonly "glBufferAddressRangeNV": long
readonly "glVertexFormatNV": long
readonly "glNormalFormatNV": long
readonly "glColorFormatNV": long
readonly "glIndexFormatNV": long
readonly "glTexCoordFormatNV": long
readonly "glEdgeFlagFormatNV": long
readonly "glSecondaryColorFormatNV": long
readonly "glFogCoordFormatNV": long
readonly "glVertexAttribFormatNV": long
readonly "glVertexAttribIFormatNV": long
readonly "glGetIntegerui64i_vNV": long
readonly "glViewportSwizzleNV": long
readonly "glBeginConditionalRenderNVX": long
readonly "glEndConditionalRenderNVX": long
readonly "glAsyncCopyImageSubDataNVX": long
readonly "glAsyncCopyBufferSubDataNVX": long
readonly "glUploadGpuMaskNVX": long
readonly "glMulticastViewportArrayvNVX": long
readonly "glMulticastScissorArrayvNVX": long
readonly "glMulticastViewportPositionWScaleNVX": long
readonly "glCreateProgressFenceNVX": long
readonly "glSignalSemaphoreui64NVX": long
readonly "glWaitSemaphoreui64NVX": long
readonly "glClientWaitSemaphoreui64NVX": long
readonly "glFramebufferTextureMultiviewOVR": long
readonly "glNamedFramebufferTextureMultiviewOVR": long
readonly "OpenGL11": boolean
readonly "OpenGL12": boolean
readonly "OpenGL13": boolean
readonly "OpenGL14": boolean
readonly "OpenGL15": boolean
readonly "OpenGL20": boolean
readonly "OpenGL21": boolean
readonly "OpenGL30": boolean
readonly "OpenGL31": boolean
readonly "OpenGL32": boolean
readonly "OpenGL33": boolean
readonly "OpenGL40": boolean
readonly "OpenGL41": boolean
readonly "OpenGL42": boolean
readonly "OpenGL43": boolean
readonly "OpenGL44": boolean
readonly "OpenGL45": boolean
readonly "OpenGL46": boolean
readonly "GL_3DFX_texture_compression_FXT1": boolean
readonly "GL_AMD_blend_minmax_factor": boolean
readonly "GL_AMD_conservative_depth": boolean
readonly "GL_AMD_debug_output": boolean
readonly "GL_AMD_depth_clamp_separate": boolean
readonly "GL_AMD_draw_buffers_blend": boolean
readonly "GL_AMD_framebuffer_multisample_advanced": boolean
readonly "GL_AMD_gcn_shader": boolean
readonly "GL_AMD_gpu_shader_half_float": boolean
readonly "GL_AMD_gpu_shader_half_float_fetch": boolean
readonly "GL_AMD_gpu_shader_int16": boolean
readonly "GL_AMD_gpu_shader_int64": boolean
readonly "GL_AMD_interleaved_elements": boolean
readonly "GL_AMD_occlusion_query_event": boolean
readonly "GL_AMD_performance_monitor": boolean
readonly "GL_AMD_pinned_memory": boolean
readonly "GL_AMD_query_buffer_object": boolean
readonly "GL_AMD_sample_positions": boolean
readonly "GL_AMD_seamless_cubemap_per_texture": boolean
readonly "GL_AMD_shader_atomic_counter_ops": boolean
readonly "GL_AMD_shader_ballot": boolean
readonly "GL_AMD_shader_explicit_vertex_parameter": boolean
readonly "GL_AMD_shader_image_load_store_lod": boolean
readonly "GL_AMD_shader_stencil_export": boolean
readonly "GL_AMD_shader_trinary_minmax": boolean
readonly "GL_AMD_sparse_texture": boolean
readonly "GL_AMD_stencil_operation_extended": boolean
readonly "GL_AMD_texture_gather_bias_lod": boolean
readonly "GL_AMD_texture_texture4": boolean
readonly "GL_AMD_transform_feedback3_lines_triangles": boolean
readonly "GL_AMD_transform_feedback4": boolean
readonly "GL_AMD_vertex_shader_layer": boolean
readonly "GL_AMD_vertex_shader_tessellator": boolean
readonly "GL_AMD_vertex_shader_viewport_index": boolean
readonly "GL_ARB_arrays_of_arrays": boolean
readonly "GL_ARB_base_instance": boolean
readonly "GL_ARB_bindless_texture": boolean
readonly "GL_ARB_blend_func_extended": boolean
readonly "GL_ARB_buffer_storage": boolean
readonly "GL_ARB_cl_event": boolean
readonly "GL_ARB_clear_buffer_object": boolean
readonly "GL_ARB_clear_texture": boolean
readonly "GL_ARB_clip_control": boolean
readonly "GL_ARB_color_buffer_float": boolean
readonly "GL_ARB_compatibility": boolean
readonly "GL_ARB_compressed_texture_pixel_storage": boolean
readonly "GL_ARB_compute_shader": boolean
readonly "GL_ARB_compute_variable_group_size": boolean
readonly "GL_ARB_conditional_render_inverted": boolean
readonly "GL_ARB_conservative_depth": boolean
readonly "GL_ARB_copy_buffer": boolean
readonly "GL_ARB_copy_image": boolean
readonly "GL_ARB_cull_distance": boolean
readonly "GL_ARB_debug_output": boolean
readonly "GL_ARB_depth_buffer_float": boolean
readonly "GL_ARB_depth_clamp": boolean
readonly "GL_ARB_depth_texture": boolean
readonly "GL_ARB_derivative_control": boolean
readonly "GL_ARB_direct_state_access": boolean
readonly "GL_ARB_draw_buffers": boolean
readonly "GL_ARB_draw_buffers_blend": boolean
readonly "GL_ARB_draw_elements_base_vertex": boolean
readonly "GL_ARB_draw_indirect": boolean
readonly "GL_ARB_draw_instanced": boolean
readonly "GL_ARB_enhanced_layouts": boolean
readonly "GL_ARB_ES2_compatibility": boolean
readonly "GL_ARB_ES3_1_compatibility": boolean
readonly "GL_ARB_ES3_2_compatibility": boolean
readonly "GL_ARB_ES3_compatibility": boolean
readonly "GL_ARB_explicit_attrib_location": boolean
readonly "GL_ARB_explicit_uniform_location": boolean
readonly "GL_ARB_fragment_coord_conventions": boolean
readonly "GL_ARB_fragment_layer_viewport": boolean
readonly "GL_ARB_fragment_program": boolean
readonly "GL_ARB_fragment_program_shadow": boolean
readonly "GL_ARB_fragment_shader": boolean
readonly "GL_ARB_fragment_shader_interlock": boolean
readonly "GL_ARB_framebuffer_no_attachments": boolean
readonly "GL_ARB_framebuffer_object": boolean
readonly "GL_ARB_framebuffer_sRGB": boolean
readonly "GL_ARB_geometry_shader4": boolean
readonly "GL_ARB_get_program_binary": boolean
readonly "GL_ARB_get_texture_sub_image": boolean
readonly "GL_ARB_gl_spirv": boolean
readonly "GL_ARB_gpu_shader5": boolean
readonly "GL_ARB_gpu_shader_fp64": boolean
readonly "GL_ARB_gpu_shader_int64": boolean
readonly "GL_ARB_half_float_pixel": boolean
readonly "GL_ARB_half_float_vertex": boolean
readonly "GL_ARB_imaging": boolean
readonly "GL_ARB_indirect_parameters": boolean
readonly "GL_ARB_instanced_arrays": boolean
readonly "GL_ARB_internalformat_query": boolean
readonly "GL_ARB_internalformat_query2": boolean
readonly "GL_ARB_invalidate_subdata": boolean
readonly "GL_ARB_map_buffer_alignment": boolean
readonly "GL_ARB_map_buffer_range": boolean
readonly "GL_ARB_matrix_palette": boolean
readonly "GL_ARB_multi_bind": boolean
readonly "GL_ARB_multi_draw_indirect": boolean
readonly "GL_ARB_multisample": boolean
readonly "GL_ARB_multitexture": boolean
readonly "GL_ARB_occlusion_query": boolean
readonly "GL_ARB_occlusion_query2": boolean
readonly "GL_ARB_parallel_shader_compile": boolean
readonly "GL_ARB_pipeline_statistics_query": boolean
readonly "GL_ARB_pixel_buffer_object": boolean
readonly "GL_ARB_point_parameters": boolean
readonly "GL_ARB_point_sprite": boolean
readonly "GL_ARB_polygon_offset_clamp": boolean
readonly "GL_ARB_post_depth_coverage": boolean
readonly "GL_ARB_program_interface_query": boolean
readonly "GL_ARB_provoking_vertex": boolean
readonly "GL_ARB_query_buffer_object": boolean
readonly "GL_ARB_robust_buffer_access_behavior": boolean
readonly "GL_ARB_robustness": boolean
readonly "GL_ARB_robustness_application_isolation": boolean
readonly "GL_ARB_robustness_share_group_isolation": boolean
readonly "GL_ARB_sample_locations": boolean
readonly "GL_ARB_sample_shading": boolean
readonly "GL_ARB_sampler_objects": boolean
readonly "GL_ARB_seamless_cube_map": boolean
readonly "GL_ARB_seamless_cubemap_per_texture": boolean
readonly "GL_ARB_separate_shader_objects": boolean
readonly "GL_ARB_shader_atomic_counter_ops": boolean
readonly "GL_ARB_shader_atomic_counters": boolean
readonly "GL_ARB_shader_ballot": boolean
readonly "GL_ARB_shader_bit_encoding": boolean
readonly "GL_ARB_shader_clock": boolean
readonly "GL_ARB_shader_draw_parameters": boolean
readonly "GL_ARB_shader_group_vote": boolean
readonly "GL_ARB_shader_image_load_store": boolean
readonly "GL_ARB_shader_image_size": boolean
readonly "GL_ARB_shader_objects": boolean
readonly "GL_ARB_shader_precision": boolean
readonly "GL_ARB_shader_stencil_export": boolean
readonly "GL_ARB_shader_storage_buffer_object": boolean
readonly "GL_ARB_shader_subroutine": boolean
readonly "GL_ARB_shader_texture_image_samples": boolean
readonly "GL_ARB_shader_texture_lod": boolean
readonly "GL_ARB_shader_viewport_layer_array": boolean
readonly "GL_ARB_shading_language_100": boolean
readonly "GL_ARB_shading_language_420pack": boolean
readonly "GL_ARB_shading_language_include": boolean
readonly "GL_ARB_shading_language_packing": boolean
readonly "GL_ARB_shadow": boolean
readonly "GL_ARB_shadow_ambient": boolean
readonly "GL_ARB_sparse_buffer": boolean
readonly "GL_ARB_sparse_texture": boolean
readonly "GL_ARB_sparse_texture2": boolean
readonly "GL_ARB_sparse_texture_clamp": boolean
readonly "GL_ARB_spirv_extensions": boolean
readonly "GL_ARB_stencil_texturing": boolean
readonly "GL_ARB_sync": boolean
readonly "GL_ARB_tessellation_shader": boolean
readonly "GL_ARB_texture_barrier": boolean
readonly "GL_ARB_texture_border_clamp": boolean
readonly "GL_ARB_texture_buffer_object": boolean
readonly "GL_ARB_texture_buffer_object_rgb32": boolean
readonly "GL_ARB_texture_buffer_range": boolean
readonly "GL_ARB_texture_compression": boolean
readonly "GL_ARB_texture_compression_bptc": boolean
readonly "GL_ARB_texture_compression_rgtc": boolean
readonly "GL_ARB_texture_cube_map": boolean
readonly "GL_ARB_texture_cube_map_array": boolean
readonly "GL_ARB_texture_env_add": boolean
readonly "GL_ARB_texture_env_combine": boolean
readonly "GL_ARB_texture_env_crossbar": boolean
readonly "GL_ARB_texture_env_dot3": boolean
readonly "GL_ARB_texture_filter_anisotropic": boolean
readonly "GL_ARB_texture_filter_minmax": boolean
readonly "GL_ARB_texture_float": boolean
readonly "GL_ARB_texture_gather": boolean
readonly "GL_ARB_texture_mirror_clamp_to_edge": boolean
readonly "GL_ARB_texture_mirrored_repeat": boolean
readonly "GL_ARB_texture_multisample": boolean
readonly "GL_ARB_texture_non_power_of_two": boolean
readonly "GL_ARB_texture_query_levels": boolean
readonly "GL_ARB_texture_query_lod": boolean
readonly "GL_ARB_texture_rectangle": boolean
readonly "GL_ARB_texture_rg": boolean
readonly "GL_ARB_texture_rgb10_a2ui": boolean
readonly "GL_ARB_texture_stencil8": boolean
readonly "GL_ARB_texture_storage": boolean
readonly "GL_ARB_texture_storage_multisample": boolean
readonly "GL_ARB_texture_swizzle": boolean
readonly "GL_ARB_texture_view": boolean
readonly "GL_ARB_timer_query": boolean
readonly "GL_ARB_transform_feedback2": boolean
readonly "GL_ARB_transform_feedback3": boolean
readonly "GL_ARB_transform_feedback_instanced": boolean
readonly "GL_ARB_transform_feedback_overflow_query": boolean
readonly "GL_ARB_transpose_matrix": boolean
readonly "GL_ARB_uniform_buffer_object": boolean
readonly "GL_ARB_vertex_array_bgra": boolean
readonly "GL_ARB_vertex_array_object": boolean
readonly "GL_ARB_vertex_attrib_64bit": boolean
readonly "GL_ARB_vertex_attrib_binding": boolean
readonly "GL_ARB_vertex_blend": boolean
readonly "GL_ARB_vertex_buffer_object": boolean
readonly "GL_ARB_vertex_program": boolean
readonly "GL_ARB_vertex_shader": boolean
readonly "GL_ARB_vertex_type_10f_11f_11f_rev": boolean
readonly "GL_ARB_vertex_type_2_10_10_10_rev": boolean
readonly "GL_ARB_viewport_array": boolean
readonly "GL_ARB_window_pos": boolean
readonly "GL_ATI_meminfo": boolean
readonly "GL_ATI_shader_texture_lod": boolean
readonly "GL_ATI_texture_compression_3dc": boolean
readonly "GL_EXT_422_pixels": boolean
readonly "GL_EXT_abgr": boolean
readonly "GL_EXT_bgra": boolean
readonly "GL_EXT_bindable_uniform": boolean
readonly "GL_EXT_blend_color": boolean
readonly "GL_EXT_blend_equation_separate": boolean
readonly "GL_EXT_blend_func_separate": boolean
readonly "GL_EXT_blend_minmax": boolean
readonly "GL_EXT_blend_subtract": boolean
readonly "GL_EXT_clip_volume_hint": boolean
readonly "GL_EXT_compiled_vertex_array": boolean
readonly "GL_EXT_debug_label": boolean
readonly "GL_EXT_debug_marker": boolean
readonly "GL_EXT_depth_bounds_test": boolean
readonly "GL_EXT_direct_state_access": boolean
readonly "GL_EXT_draw_buffers2": boolean
readonly "GL_EXT_draw_instanced": boolean
readonly "GL_EXT_EGL_image_storage": boolean
readonly "GL_EXT_EGL_sync": boolean
readonly "GL_EXT_external_buffer": boolean
readonly "GL_EXT_framebuffer_blit": boolean
readonly "GL_EXT_framebuffer_multisample": boolean
readonly "GL_EXT_framebuffer_multisample_blit_scaled": boolean
readonly "GL_EXT_framebuffer_object": boolean
readonly "GL_EXT_framebuffer_sRGB": boolean
readonly "GL_EXT_geometry_shader4": boolean
readonly "GL_EXT_gpu_program_parameters": boolean
readonly "GL_EXT_gpu_shader4": boolean
readonly "GL_EXT_memory_object": boolean
readonly "GL_EXT_memory_object_fd": boolean
readonly "GL_EXT_memory_object_win32": boolean
readonly "GL_EXT_multiview_tessellation_geometry_shader": boolean
readonly "GL_EXT_multiview_texture_multisample": boolean
readonly "GL_EXT_multiview_timer_query": boolean
readonly "GL_EXT_packed_depth_stencil": boolean
readonly "GL_EXT_packed_float": boolean
readonly "GL_EXT_pixel_buffer_object": boolean
readonly "GL_EXT_point_parameters": boolean
readonly "GL_EXT_polygon_offset_clamp": boolean
readonly "GL_EXT_post_depth_coverage": boolean
readonly "GL_EXT_provoking_vertex": boolean
readonly "GL_EXT_raster_multisample": boolean
readonly "GL_EXT_secondary_color": boolean
readonly "GL_EXT_semaphore": boolean
readonly "GL_EXT_semaphore_fd": boolean
readonly "GL_EXT_semaphore_win32": boolean
readonly "GL_EXT_separate_shader_objects": boolean
readonly "GL_EXT_shader_framebuffer_fetch": boolean
readonly "GL_EXT_shader_framebuffer_fetch_non_coherent": boolean
readonly "GL_EXT_shader_image_load_formatted": boolean
readonly "GL_EXT_shader_image_load_store": boolean
readonly "GL_EXT_shader_integer_mix": boolean
readonly "GL_EXT_shadow_funcs": boolean
readonly "GL_EXT_shared_texture_palette": boolean
readonly "GL_EXT_sparse_texture2": boolean
readonly "GL_EXT_stencil_clear_tag": boolean
readonly "GL_EXT_stencil_two_side": boolean
readonly "GL_EXT_stencil_wrap": boolean
readonly "GL_EXT_texture_array": boolean
readonly "GL_EXT_texture_buffer_object": boolean
readonly "GL_EXT_texture_compression_latc": boolean
readonly "GL_EXT_texture_compression_rgtc": boolean
readonly "GL_EXT_texture_compression_s3tc": boolean
readonly "GL_EXT_texture_filter_anisotropic": boolean
readonly "GL_EXT_texture_filter_minmax": boolean
readonly "GL_EXT_texture_integer": boolean
readonly "GL_EXT_texture_mirror_clamp": boolean
readonly "GL_EXT_texture_shadow_lod": boolean
readonly "GL_EXT_texture_shared_exponent": boolean
readonly "GL_EXT_texture_snorm": boolean
readonly "GL_EXT_texture_sRGB": boolean
readonly "GL_EXT_texture_sRGB_decode": boolean
readonly "GL_EXT_texture_sRGB_R8": boolean
readonly "GL_EXT_texture_sRGB_RG8": boolean
readonly "GL_EXT_texture_storage": boolean
readonly "GL_EXT_texture_swizzle": boolean
readonly "GL_EXT_timer_query": boolean
readonly "GL_EXT_transform_feedback": boolean
readonly "GL_EXT_vertex_array_bgra": boolean
readonly "GL_EXT_vertex_attrib_64bit": boolean
readonly "GL_EXT_win32_keyed_mutex": boolean
readonly "GL_EXT_window_rectangles": boolean
readonly "GL_EXT_x11_sync_object": boolean
readonly "GL_GREMEDY_frame_terminator": boolean
readonly "GL_GREMEDY_string_marker": boolean
readonly "GL_INTEL_blackhole_render": boolean
readonly "GL_INTEL_conservative_rasterization": boolean
readonly "GL_INTEL_fragment_shader_ordering": boolean
readonly "GL_INTEL_framebuffer_CMAA": boolean
readonly "GL_INTEL_map_texture": boolean
readonly "GL_INTEL_performance_query": boolean
readonly "GL_INTEL_shader_integer_functions2": boolean
readonly "GL_KHR_blend_equation_advanced": boolean
readonly "GL_KHR_blend_equation_advanced_coherent": boolean
readonly "GL_KHR_context_flush_control": boolean
readonly "GL_KHR_debug": boolean
readonly "GL_KHR_no_error": boolean
readonly "GL_KHR_parallel_shader_compile": boolean
readonly "GL_KHR_robust_buffer_access_behavior": boolean
readonly "GL_KHR_robustness": boolean
readonly "GL_KHR_shader_subgroup": boolean
readonly "GL_KHR_texture_compression_astc_hdr": boolean
readonly "GL_KHR_texture_compression_astc_ldr": boolean
readonly "GL_KHR_texture_compression_astc_sliced_3d": boolean
readonly "GL_MESA_framebuffer_flip_x": boolean
readonly "GL_MESA_framebuffer_flip_y": boolean
readonly "GL_MESA_framebuffer_swap_xy": boolean
readonly "GL_MESA_tile_raster_order": boolean
readonly "GL_NV_alpha_to_coverage_dither_control": boolean
readonly "GL_NV_bindless_multi_draw_indirect": boolean
readonly "GL_NV_bindless_multi_draw_indirect_count": boolean
readonly "GL_NV_bindless_texture": boolean
readonly "GL_NV_blend_equation_advanced": boolean
readonly "GL_NV_blend_equation_advanced_coherent": boolean
readonly "GL_NV_blend_minmax_factor": boolean
readonly "GL_NV_blend_square": boolean
readonly "GL_NV_clip_space_w_scaling": boolean
readonly "GL_NV_command_list": boolean
readonly "GL_NV_compute_shader_derivatives": boolean
readonly "GL_NV_conditional_render": boolean
readonly "GL_NV_conservative_raster": boolean
readonly "GL_NV_conservative_raster_dilate": boolean
readonly "GL_NV_conservative_raster_pre_snap": boolean
readonly "GL_NV_conservative_raster_pre_snap_triangles": boolean
readonly "GL_NV_conservative_raster_underestimation": boolean
readonly "GL_NV_copy_depth_to_color": boolean
readonly "GL_NV_copy_image": boolean
readonly "GL_NV_deep_texture3D": boolean
readonly "GL_NV_depth_buffer_float": boolean
readonly "GL_NV_depth_clamp": boolean
readonly "GL_NV_draw_texture": boolean
readonly "GL_NV_draw_vulkan_image": boolean
readonly "GL_NV_ES3_1_compatibility": boolean
readonly "GL_NV_explicit_multisample": boolean
readonly "GL_NV_fence": boolean
readonly "GL_NV_fill_rectangle": boolean
readonly "GL_NV_float_buffer": boolean
readonly "GL_NV_fog_distance": boolean
readonly "GL_NV_fragment_coverage_to_color": boolean
readonly "GL_NV_fragment_program4": boolean
readonly "GL_NV_fragment_program_option": boolean
readonly "GL_NV_fragment_shader_barycentric": boolean
readonly "GL_NV_fragment_shader_interlock": boolean
readonly "GL_NV_framebuffer_mixed_samples": boolean
readonly "GL_NV_framebuffer_multisample_coverage": boolean
readonly "GL_NV_geometry_shader4": boolean
readonly "GL_NV_geometry_shader_passthrough": boolean
readonly "GL_NV_gpu_multicast": boolean
readonly "GL_NV_gpu_shader5": boolean
readonly "GL_NV_half_float": boolean
readonly "GL_NV_internalformat_sample_query": boolean
readonly "GL_NV_light_max_exponent": boolean
readonly "GL_NV_memory_attachment": boolean
readonly "GL_NV_memory_object_sparse": boolean
readonly "GL_NV_mesh_shader": boolean
readonly "GL_NV_multisample_coverage": boolean
readonly "GL_NV_multisample_filter_hint": boolean
readonly "GL_NV_packed_depth_stencil": boolean
readonly "GL_NV_path_rendering": boolean
readonly "GL_NV_path_rendering_shared_edge": boolean
readonly "GL_NV_pixel_data_range": boolean
readonly "GL_NV_point_sprite": boolean
readonly "GL_NV_primitive_restart": boolean
readonly "GL_NV_primitive_shading_rate": boolean
readonly "GL_NV_query_resource": boolean
readonly "GL_NV_query_resource_tag": boolean
readonly "GL_NV_representative_fragment_test": boolean
readonly "GL_NV_robustness_video_memory_purge": boolean
readonly "GL_NV_sample_locations": boolean
readonly "GL_NV_sample_mask_override_coverage": boolean
readonly "GL_NV_scissor_exclusive": boolean
readonly "GL_NV_shader_atomic_float": boolean
readonly "GL_NV_shader_atomic_float64": boolean
readonly "GL_NV_shader_atomic_fp16_vector": boolean
readonly "GL_NV_shader_atomic_int64": boolean
readonly "GL_NV_shader_buffer_load": boolean
readonly "GL_NV_shader_buffer_store": boolean
readonly "GL_NV_shader_subgroup_partitioned": boolean
readonly "GL_NV_shader_texture_footprint": boolean
readonly "GL_NV_shader_thread_group": boolean
readonly "GL_NV_shader_thread_shuffle": boolean
readonly "GL_NV_shading_rate_image": boolean
readonly "GL_NV_stereo_view_rendering": boolean
readonly "GL_NV_texgen_reflection": boolean
readonly "GL_NV_texture_barrier": boolean
readonly "GL_NV_texture_compression_vtc": boolean
readonly "GL_NV_texture_multisample": boolean
readonly "GL_NV_texture_rectangle_compressed": boolean
readonly "GL_NV_texture_shader": boolean
readonly "GL_NV_texture_shader2": boolean
readonly "GL_NV_texture_shader3": boolean
readonly "GL_NV_timeline_semaphore": boolean
readonly "GL_NV_transform_feedback": boolean
readonly "GL_NV_transform_feedback2": boolean
readonly "GL_NV_uniform_buffer_unified_memory": boolean
readonly "GL_NV_vertex_array_range": boolean
readonly "GL_NV_vertex_array_range2": boolean
readonly "GL_NV_vertex_attrib_integer_64bit": boolean
readonly "GL_NV_vertex_buffer_unified_memory": boolean
readonly "GL_NV_viewport_array2": boolean
readonly "GL_NV_viewport_swizzle": boolean
readonly "GL_NVX_blend_equation_advanced_multi_draw_buffers": boolean
readonly "GL_NVX_conditional_render": boolean
readonly "GL_NVX_gpu_memory_info": boolean
readonly "GL_NVX_gpu_multicast2": boolean
readonly "GL_NVX_progress_fence": boolean
readonly "GL_OVR_multiview": boolean
readonly "GL_OVR_multiview2": boolean
readonly "GL_S3_s3tc": boolean
readonly "forwardCompatible": boolean


public static "initialize"(): void
public "getAddressBuffer"(): $PointerBuffer
get "addressBuffer"(): $PointerBuffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GLCapabilities$Type = ($GLCapabilities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GLCapabilities_ = $GLCapabilities$Type;
}}
