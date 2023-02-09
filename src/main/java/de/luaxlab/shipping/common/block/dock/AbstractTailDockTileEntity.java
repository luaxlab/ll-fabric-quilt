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
import de.luaxlab.shipping.common.util.InventoryUtils;
import de.luaxlab.shipping.common.util.LinkableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTailDockTileEntity<T extends Entity & LinkableEntity<T>> extends AbstractDockTileEntity<T> {
    public AbstractTailDockTileEntity(BlockEntityType<?> t, BlockPos pos, BlockState state) {
        super(t, pos, state);
    }

    protected boolean handleItemHopper(T bargeEntity, HopperBlockEntity hopper){
        if(!(bargeEntity instanceof Container)){
            return false;
        }
        if (isExtract()) {
            return InventoryUtils.mayMoveIntoInventory(hopper, (Container) bargeEntity);
        } else {
            return InventoryUtils.mayMoveIntoInventory((Container) bargeEntity, hopper);
        }
    }

    protected Boolean isExtract() {
        return getBlockState().getValue(DockingBlockStates.INVERTED);
    }


    @Override
    public boolean hold(T vessel, Direction direction) {
        if (checkBadDirCondition(direction))
        {
            return false;
        }

        for (BlockPos p : getTargetBlockPos()) {
            if (checkInterface(vessel, p)){
                return true;
            }
        }
        return false;
    }

    @NotNull
    private Boolean checkInterface(T vessel, BlockPos p) {
        return getHopper(p).map(h -> handleItemHopper(vessel, h))
                .orElse(getVesselLoader(p).map(l -> l.hold(vessel, isExtract() ? IVesselLoader.Mode.IMPORT : IVesselLoader.Mode.EXPORT))
                        .orElse(false));
    }

    protected abstract boolean checkBadDirCondition(Direction direction);
}
