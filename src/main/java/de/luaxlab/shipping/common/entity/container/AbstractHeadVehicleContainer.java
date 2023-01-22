package de.luaxlab.shipping.common.entity.container;

import com.mojang.datafixers.util.Pair;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.entity.accessor.DataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import de.luaxlab.shipping.common.network.SetEnginePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractHeadVehicleContainer<T extends DataAccessor, U extends Entity & HeadVehicle> extends AbstractItemHandlerContainer {

    protected T data;
    protected U entity;

    public AbstractHeadVehicleContainer(@Nullable MenuType<?> containerType, int windowId, Level world, T data,
                                        Inventory playerInventory, Player player) {
        super(containerType, windowId, playerInventory, player);
        this.entity = (U) world.getEntity(data.getEntityUUID());
        this.data = data;
        layoutPlayerInventorySlots(8, 84);
        this.addDataSlots(data);

        addSlot(new Slot(entity.getRouteContainer(),0, 98, 57)
		{
			@Override
			public @NotNull Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return new Pair<>(ModItems.EMPTY_ATLAS_LOC, entity.getRouteIcon());
			}
		});
    }

    @Override
    protected int getSlotNum() {
        return 2;
    }

    public abstract boolean isOn();
    public abstract int routeSize();
    public abstract int visitedSize();

    public void setEngine(boolean state){
        //VehiclePacketHandler.INSTANCE.sendToServer(new SetEnginePacket(entity.getId(), state));
		//TODO
    }

    public String getRouteText(){
        return  visitedSize() + "/" + routeSize();
    }

    @Override
    public boolean stillValid(Player p_75145_1_) {
        return entity.isValid(p_75145_1_);
    }
}
