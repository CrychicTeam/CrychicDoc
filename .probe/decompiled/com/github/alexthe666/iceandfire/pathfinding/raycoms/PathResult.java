package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.github.alexthe666.iceandfire.IceAndFire;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Path;

public class PathResult<T extends Callable<Path>> {

    protected PathFindingStatus status = PathFindingStatus.IN_PROGRESS_COMPUTING;

    private static boolean threadException = false;

    private volatile boolean pathReachesDestination = false;

    private Path path = null;

    private Future<Path> pathCalculation = null;

    private T job = (T) null;

    private boolean pathingDoneAndProcessed = false;

    public PathFindingStatus getStatus() {
        return this.status;
    }

    public void setStatus(PathFindingStatus s) {
        this.status = s;
    }

    public boolean isInProgress() {
        return this.isComputing() || this.status == PathFindingStatus.IN_PROGRESS_FOLLOWING;
    }

    public boolean isComputing() {
        return this.status == PathFindingStatus.IN_PROGRESS_COMPUTING;
    }

    public boolean failedToReachDestination() {
        return this.isFinished() && !this.pathReachesDestination;
    }

    public boolean isPathReachingDestination() {
        return this.isFinished() && this.path != null && this.pathReachesDestination;
    }

    public void setPathReachesDestination(boolean value) {
        this.pathReachesDestination = value;
    }

    public boolean isCancelled() {
        return this.status == PathFindingStatus.CANCELLED;
    }

    public int getPathLength() {
        return this.path.getNodeCount();
    }

    public boolean hasPath() {
        return this.path != null;
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public T getJob() {
        return this.job;
    }

    public void setJob(T job) {
        this.job = job;
    }

    public void startJob(ExecutorService executorService) {
        if (this.job != null) {
            try {
                if (!threadException) {
                    this.pathCalculation = executorService.submit(this.job);
                }
            } catch (NullPointerException var3) {
                IceAndFire.LOGGER.error("Mod tried to move an entity from non server thread", var3);
            } catch (RuntimeException var4) {
                threadException = true;
                IceAndFire.LOGGER.catching(var4);
            } catch (Exception var5) {
                IceAndFire.LOGGER.catching(var5);
            }
        }
    }

    public void processCalculationResults() {
        if (!this.pathingDoneAndProcessed) {
            try {
                this.path = (Path) this.pathCalculation.get();
                this.pathCalculation = null;
                this.setStatus(PathFindingStatus.CALCULATION_COMPLETE);
            } catch (ExecutionException | InterruptedException var2) {
                IceAndFire.LOGGER.catching(var2);
            }
        }
    }

    public boolean isCalculatingPath() {
        return this.pathCalculation != null && !this.pathCalculation.isDone();
    }

    public boolean isFinished() {
        if (!this.pathingDoneAndProcessed && this.pathCalculation != null && this.pathCalculation.isDone()) {
            this.processCalculationResults();
            this.pathingDoneAndProcessed = true;
        }
        return this.pathingDoneAndProcessed;
    }

    public void cancel() {
        if (this.pathCalculation != null) {
            this.pathCalculation.cancel(false);
            this.pathCalculation = null;
        }
        this.pathingDoneAndProcessed = true;
    }
}