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

import com.mojang.datafixers.util.Pair;
import de.luaxlab.shipping.common.block.IVesselLoader;
import de.luaxlab.shipping.common.util.InventoryUtils;
import de.luaxlab.shipping.common.util.LinkableEntity;
import de.luaxlab.shipping.common.util.LinkableEntityHead;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractHeadDockTileEntity<T extends Entity & LinkableEntity<T>> extends AbstractDockTileEntity<T> {
    public AbstractHeadDockTileEntity(BlockEntityType<?> t, BlockPos pos, BlockState state) {
        super(t, pos, state);
    }

    protected boolean handleItemHopper(T tugEntity, HopperBlockEntity hopper){
        if(!(tugEntity instanceof Container)){
            return false;
        }
        return InventoryUtils.mayMoveIntoInventory((Container) tugEntity, hopper);
    }


    public boolean hold(T tug, Direction direction){
        if (!(tug instanceof LinkableEntityHead) || checkBadDirCondition(tug, direction)){
            return false;
        }

        // force tug to be docked when powered
        // todo: add UI for inverted mode toggle?
        if (getBlockState().getValue(DockingBlockStates.POWERED)) {
            return true;
        }

        for (BlockPos p : getTargetBlockPos()) {
            if (getHopper(p).map(hopper -> handleItemHopper(tug, hopper))
                    .orElse(getVesselLoader(p).map(l -> l.hold(tug, IVesselLoader.Mode.EXPORT)).orElse(false))) {
                return true;
            }
        }


        List<Pair<T, AbstractTailDockTileEntity<T>>> barges = getTailDockPairs(tug);


        if (barges.stream().map(pair -> pair.getSecond().hold(pair.getFirst(), direction)).reduce(false, Boolean::logicalOr)){
            return true;
        }

        return false;
    }

    protected abstract boolean checkBadDirCondition(T tug, Direction direction);

    protected abstract Direction getRowDirection(Direction facing);

    private List<Pair<T, AbstractTailDockTileEntity<T>>> getTailDockPairs(T tug){
        List<T> barges = tug.getTrain().asListOfTugged();
        List<AbstractTailDockTileEntity<T>> docks = getTailDocks();
        return IntStream.range(0, Math.min(barges.size(), docks.size()))
                .mapToObj(i -> new Pair<>(barges.get(i), docks.get(i)))
                .collect(Collectors.toList());
    }

    private List<AbstractTailDockTileEntity<T>> getTailDocks(){
        Direction facing = this.getBlockState().getValue(DockingBlockStates.FACING);
        Direction rowDirection = getRowDirection(facing);
        List<AbstractTailDockTileEntity<T>> docks = new ArrayList<>();
        for (Optional<AbstractTailDockTileEntity<T>> dock = getNextBargeDock(rowDirection, this.getBlockPos());
             dock.isPresent();
             dock = getNextBargeDock(rowDirection, dock.get().getBlockPos())) {
            docks.add(dock.get());
        }
        return docks;
    }

    private Optional<AbstractTailDockTileEntity<T>> getNextBargeDock(Direction rowDirection, BlockPos pos) {
        BlockPos next = pos.relative(rowDirection);
        return Optional.ofNullable(this.level.getBlockEntity(next))
                .filter(e -> e instanceof AbstractTailDockTileEntity)
                .map(e -> (AbstractTailDockTileEntity<T>) e);
    }

}
