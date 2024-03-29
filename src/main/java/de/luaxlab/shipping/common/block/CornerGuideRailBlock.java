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
package de.luaxlab.shipping.common.block;

import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import de.luaxlab.shipping.common.entity.vessel.barge.AbstractBargeEntity;
import de.luaxlab.shipping.common.util.InteractionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CornerGuideRailBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;


    public CornerGuideRailBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos,
										  @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult) {
        if(InteractionUtil.doConfigure(player, hand)){
            world.setBlockAndUpdate(pos, state.setValue(CornerGuideRailBlock.INVERTED, !state.getValue(INVERTED)));
            return InteractionResult.SUCCESS;
        }

        return super.use(state, world, pos, player, hand, rayTraceResult);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(INVERTED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(INVERTED, false);

    }

    public static Direction getArrowsDirection(BlockState state){
        Direction facing = state.getValue(CornerGuideRailBlock.FACING);
        return state.getValue(CornerGuideRailBlock.INVERTED) ? facing.getClockWise() : facing.getCounterClockWise();
    }

    @Override
    public void entityInside(BlockState state, @NotNull Level level, BlockPos pos, Entity entity){
        Direction facing = state.getValue(CornerGuideRailBlock.FACING);
        if(!entity.getDirection().equals(facing.getOpposite()) || !(entity instanceof VesselEntity)){
            return;
        }

        Direction arrows = getArrowsDirection(state);
        double modifier = entity instanceof AbstractBargeEntity ? 0.2 : 0.1;
        entity.setDeltaMovement(entity.getDeltaMovement().add(
                new Vec3(
                        (facing.getOpposite().getStepX() + arrows.getStepX()) * modifier,
                        0,
                        (facing.getOpposite().getStepZ() + arrows.getStepZ()) * modifier)));
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

}
