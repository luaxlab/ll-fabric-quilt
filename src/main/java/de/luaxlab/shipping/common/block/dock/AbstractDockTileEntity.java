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

import de.luaxlab.shipping.common.block.IVesselLoader;
import de.luaxlab.shipping.common.util.LinkableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;


public abstract class AbstractDockTileEntity<T extends Entity & LinkableEntity<T>> extends BlockEntity {
    public AbstractDockTileEntity(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState s) {
        super(p_i48289_1_, pos, s);
    }

    public abstract boolean hold(T vessel, Direction direction);

    public Optional<HopperBlockEntity> getHopper(BlockPos p){
        BlockEntity mayBeHopper = this.level.getBlockEntity(p);
        if (mayBeHopper instanceof HopperBlockEntity h) {
            return Optional.of(h);
        }
        else return Optional.empty();
    }

    public Optional<IVesselLoader> getVesselLoader(BlockPos p){
        BlockEntity mayBeLoader = this.level.getBlockEntity(p);
        if (mayBeLoader instanceof IVesselLoader l) {
            return Optional.of(l);
        }
        else return Optional.empty();
    }

    protected abstract List<BlockPos> getTargetBlockPos();

}
