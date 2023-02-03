package de.luaxlab.shipping.common.block.dock;

import de.luaxlab.shipping.common.core.ModBlockEntities;
import de.luaxlab.shipping.common.util.InteractionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class BargeDockBlock extends AbstractDockBlock {
    public BargeDockBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if(InteractionUtil.doConfigure(player, hand)){
            world.setBlockAndUpdate(pos, state.setValue(DockingBlockStates.INVERTED, !state.getValue(DockingBlockStates.INVERTED)));
            DockingBlockStates.fixHopperPos(state, world, pos, Direction.UP, state.getValue(DockingBlockStates.FACING));
            return InteractionResult.SUCCESS;
        }

        return super.use(state, world, pos, player, hand, rayTraceResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.BARGE_DOCK.get().create(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DockingBlockStates.INVERTED);
    }

}
