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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.Optional;

public class DockingBlockStates {
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static Optional<AbstractDockTileEntity<?>> getTileEntity(Level world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof AbstractDockTileEntity)
            return Optional.of((AbstractDockTileEntity<?>) tileEntity);
        else
            return Optional.empty();

    }

    public static void fixHopperPos(BlockState state, Level world, BlockPos p_220069_3_, Direction targetLoc, Direction targetDir) {
        getTileEntity(world, p_220069_3_)
                .filter(DockingBlockStates::isImport)
                .flatMap(dock -> dock.getHopper(p_220069_3_.relative(targetLoc)))
                .ifPresent(te -> {
                    if(te.getBlockState().getValue(HopperBlock.FACING).equals(Direction.DOWN)) {
                        world.setBlock(te.getBlockPos(), te.getBlockState().setValue(HopperBlock.FACING, targetDir), 2);
                    }
        });
    }

    private static boolean isImport(AbstractDockTileEntity<?> dock){
        if(dock instanceof AbstractTailDockTileEntity){
            return !dock.getBlockState().getValue(INVERTED);
        } else return true;
    }

}
