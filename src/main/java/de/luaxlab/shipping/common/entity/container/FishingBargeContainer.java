package de.luaxlab.shipping.common.entity.container;

import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModComponents;
import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.vessel.barge.FishingBargeEntity;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotExposedStorage;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;


public class FishingBargeContainer extends AbstractItemHandlerContainer {

	private final FishingBargeEntity fishingBargeEntity;

    public FishingBargeContainer(int windowId, Level world, int entityId,
								 Inventory playerInventory, Player player) {
        super(ModContainers.FISHING_BARGE_CONTAINER.get(), windowId, playerInventory, player);
		this.fishingBargeEntity = (FishingBargeEntity) world.getEntity(entityId);
        layoutPlayerInventorySlots(8, 49 + 18 * 2);

		ModComponents.ITEM_HANDLER.maybeGet(fishingBargeEntity).ifPresent(h -> {
			for (int l = 0; l < 3; ++l) {
				for (int k = 0; k < 9; ++k) {
					this.addSlot(new ReadOnlySlot(h.getHandler(), l * 9 + k, 8 + k * 18, 18 * (l + 1)));
				}
			}
		});
    }

    @Override
    protected int getSlotNum() {
        return 27;
    }

    @Override
    public boolean stillValid(Player player) {
        return fishingBargeEntity.stillValid(player);
    }

	protected class ReadOnlySlot extends SlotItemHandler {

		public ReadOnlySlot(SlotExposedStorage itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean mayPlace(@NotNull ItemStack stack) {
			return false;
		}
	}

}
