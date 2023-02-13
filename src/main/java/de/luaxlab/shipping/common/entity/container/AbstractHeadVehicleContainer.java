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
package de.luaxlab.shipping.common.entity.container;

import de.luaxlab.shipping.common.entity.accessor.HeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import de.luaxlab.shipping.common.network.SetEnginePacket;
import de.luaxlab.shipping.common.network.VehiclePacketHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractHeadVehicleContainer<T extends HeadVehicleDataAccessor, U extends Entity & HeadVehicle> extends AbstractItemHandlerContainer{
    public static final ResourceLocation EMPTY_ATLAS_LOC = InventoryMenu.BLOCK_ATLAS;
    protected final T data;
    protected final U entity;
    protected final Player player;

    public AbstractHeadVehicleContainer(@Nullable MenuType<?> containerType, int windowId, Level world, T data,
                                        Inventory playerInventory, Player player) {
        super(containerType, windowId, playerInventory, player);
        this.entity = (U) world.getEntity(data.getEntityUUID());
        this.data = data;
        this.player = playerInventory.player;
        layoutPlayerInventorySlots(8, 84);
        this.addDataSlots(data);

        addSlot(new SlotItemHandler(entity.getRouteItemHandler(),
                0, 98, 57).setBackground(EMPTY_ATLAS_LOC, entity.getRouteIcon()));
    }

    @Override
    protected int getSlotNum() {
        return 2;
    }

    public boolean isLit(){
        return data.isLit();
    }

    public boolean isOn(){
        return data.isOn();
    }

    public int routeSize() {
        return data.routeSize();
    }

    public int visitedSize() {
        return data.visitedSize();
    }

    public void setEngine(boolean state){
        VehiclePacketHandler.INSTANCE.sendToServer(new SetEnginePacket(entity.getId(), state));
    }

    public void enroll(){
        // TODO
        // VehiclePacketHandler.INSTANCE.sendToServer(new EnrollVehiclePacket(entity.getId()));
    }

    public String getOwner(){
        return entity.owner();
    }

    public boolean canMove(){
        return data.canMove();
    }

    public String getRouteText(){
        return  visitedSize() + "/" + routeSize();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return entity.isValid(player);
    }
}
