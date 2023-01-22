package de.luaxlab.shipping.common.block.dock;

import de.luaxlab.shipping.common.core.ModBlockEntities;
import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BargeDockTileEntity extends AbstractTailDockTileEntity<VesselEntity> {
    public BargeDockTileEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BARGE_DOCK, pos, state);
    }

    @Override
    protected List<BlockPos> getTargetBlockPos(){
        if (isExtract()) {
            return List.of(this.getBlockPos()
                    .below()
                    .relative(this.getBlockState().getValue(DockingBlockStates.FACING)));
        } else return List.of(this.getBlockPos().above());
    }

    @Override
    protected boolean checkBadDirCondition(Direction direction) {
        return !getBlockState().getValue(DockingBlockStates.FACING).getOpposite().equals(direction);
    }
}
