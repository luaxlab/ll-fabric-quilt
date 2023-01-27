package de.luaxlab.shipping.common.entity.container;

import com.mojang.datafixers.util.Pair;
import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SteamHeadVehicleContainer<T extends SteamTugEntity> extends AbstractHeadVehicleContainer<SteamHeadVehicleDataAccessor, T> {
    public SteamHeadVehicleContainer(int windowId, Level world, SteamHeadVehicleDataAccessor data,
                                     Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
        super(ModContainers.STEAM_TUG_CONTAINER.get(), windowId, world, data, playerInventory, player);

        /*if(entity != null) {
            entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 42, 40));
            });
        }*/

		addSlot(new Slot(entity.getFuelContainer(),0, 42, 40));

        this.addDataSlots(data.getRawData());
    }

    public int getBurnProgress(){
        return data.getBurnProgress();
    }

    public boolean isLit(){
        return data.isLit();
    }

    @Override
    public boolean isOn(){
        return data.isOn();
    }

    @Override
    public int routeSize() {
        return data.routeSize();
    }

    @Override
    public int visitedSize() {
        return data.visitedSize();
    }

}
