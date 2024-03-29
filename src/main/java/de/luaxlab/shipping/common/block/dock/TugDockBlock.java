/*
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, Murad Akhundov, LuaX, Abbie

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.luaxlab.shipping.common.block.dock;

import de.luaxlab.shipping.common.core.ModBlockEntities;
import de.luaxlab.shipping.common.core.ModBlocks;
import de.luaxlab.shipping.common.util.InteractionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class TugDockBlock extends AbstractDockBlock {

    public TugDockBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.TUG_DOCK.get().create(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if(InteractionUtil.doConfigure(player, hand)){
            world.setBlockAndUpdate(pos, state.setValue(DockingBlockStates.INVERTED, !state.getValue(DockingBlockStates.INVERTED)));
            return InteractionResult.SUCCESS;
        }

        return super.use(state, world, pos, player, hand, rayTraceResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return super.getStateForPlacement(context)
                .setValue(DockingBlockStates.INVERTED, false)
                .setValue(DockingBlockStates.POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DockingBlockStates.INVERTED, DockingBlockStates.POWERED);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        super.neighborChanged(state, world, pos, p_220069_4_, p_220069_5_, p_220069_6_);
        if (!world.isClientSide) {
            boolean flag = state.getValue(DockingBlockStates.POWERED);
            if (flag != world.hasNeighborSignal(pos)) {
                world.setBlock(pos, state.cycle(DockingBlockStates.POWERED), 2);
            }
            adjustInverted(state, world, pos);
        }
    }

    private void adjustInverted(BlockState state, Level level, BlockPos pos){
        Direction facing = state.getValue(DockingBlockStates.FACING);
        Direction dockdir = state.getValue(DockingBlockStates.INVERTED) ? facing.getCounterClockWise() : facing.getClockWise();
        var tarpos = pos.relative(dockdir);
        if (level.getBlockState(tarpos).is(ModBlocks.BARGE_DOCK.get())){
           level.setBlock(pos, state.setValue(DockingBlockStates.INVERTED, !state.getValue(DockingBlockStates.INVERTED)), 2);
        }
    }

	//TODO: Connect redstone

}
