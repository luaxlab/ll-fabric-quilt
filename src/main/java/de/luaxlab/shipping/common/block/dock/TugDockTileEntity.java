package de.luaxlab.shipping.common.block.dock;

import de.luaxlab.shipping.common.core.ModBlockEntities;
import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TugDockTileEntity extends AbstractHeadDockTileEntity<VesselEntity> {
    public TugDockTileEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TUG_DOCK.get(), pos, state);
    }

    @Override
    protected List<BlockPos> getTargetBlockPos() {
        return List.of(this.getBlockPos().above());
    }

    @Override
    protected boolean checkBadDirCondition(VesselEntity tug, Direction direction) {
        return !getBlockState().getValue(DockingBlockStates.FACING).getOpposite().equals(direction)
                ||
                tug.getDirection().equals(getRowDirection(getBlockState().getValue(DockingBlockStates.FACING)));
    }

    @Override
    protected Direction getRowDirection(Direction facing) {
        return this.getBlockState().getValue(DockingBlockStates.INVERTED) ? facing.getClockWise() : facing.getCounterClockWise();
    }

}
