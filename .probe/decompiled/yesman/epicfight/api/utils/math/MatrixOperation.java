package yesman.epicfight.api.utils.math;

@FunctionalInterface
public interface MatrixOperation {

    OpenMatrix4f mul(OpenMatrix4f var1, OpenMatrix4f var2, OpenMatrix4f var3);
}