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
package de.luaxlab.shipping.common.entity.vessel.tug;

import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.core.ModSounds;
import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.container.SteamHeadVehicleContainer;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.qsl.item.content.registry.api.ItemContentRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SteamTugEntity extends AbstractTugEntity {
    private static final int FURNACE_FUEL_MULTIPLIER = ModConfig.Server.STEAM_TUG_FUEL_MULTIPLIER.get();
    private final ItemStackHandler itemHandler = createHandler();
    protected int burnTime = 0;
    protected int burnCapacity = 0;

    public SteamTugEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
    }

    public SteamTugEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.STEAM_TUG.get(), worldIn, x, y, z);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemVariant stack) {
				return ItemContentRegistries.FUEL_TIME.get(stack.getItem()).isPresent();
            }

            @Nonnull
            @Override
            public long insertSlot(int slot, @Nonnull ItemVariant stack, long maxAmount, TransactionContext transaction) {
                if (!isItemValid(slot, stack)) {
                    return 0;
                }

                return super.insertSlot(slot, stack, maxAmount, transaction);
            }
        };
    }

    @Override
    protected ExtendedScreenHandlerFactory createContainerProvider() {
        return new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
				getDataAccessor().write(buf);
			}

			@Override
            public Component getDisplayName() {
                return Component.translatable("screen.littlelogistics.tug");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player Player) {
                return new SteamHeadVehicleContainer<SteamTugEntity>(i, level, getDataAccessor(), playerInventory, Player);
            }
        };
    }

    public int getBurnProgress() {
        int i = burnCapacity;
        if (i == 0) {
            i = 200;
        }

        return burnTime * 13 / i;
    }

    // CONTAINER STUFF
    public boolean isLit() {
        return burnTime > 0;
    }

    @Override
    public SteamHeadVehicleDataAccessor getDataAccessor() {
        return (SteamHeadVehicleDataAccessor) new SteamHeadVehicleDataAccessor.Builder()
                .withBurnProgress(this::getBurnProgress)
                .withId(this.getId())
                .withLit(this::isLit)
                .withVisitedSize(() -> nextStop)
                .withOn(() -> engineOn)
                .withRouteSize(() -> path != null ? path.size() : 0)
                .withCanMove(enrollmentHandler::mayMove)
                .build();
    }

    @Override
    protected boolean tickFuel() {
        if (burnTime > 0) {
            burnTime--;
            return true;
        } else {
            ItemStack stack = itemHandler.getStackInSlot(0);
            if (!stack.isEmpty()) {
				//For Fabric use (((FuelRegistryImpl)FuelRegistryImpl.INSTANCE);
                burnCapacity = (ItemContentRegistries.FUEL_TIME.get(stack.getItem()).orElse(0) * FURNACE_FUEL_MULTIPLIER) - 1;
                burnTime = burnCapacity - 1;
                stack.shrink(1);
                return true;
            } else {
                burnCapacity = 0;
                burnTime = 0;
                return false;
            }
        }
    }

    public Item getDropItem() {
        return ModItems.STEAM_TUG.get();
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        burnTime = compound.contains("burn") ? compound.getInt("burn") : 0;
        burnCapacity = compound.contains("burn_capacity") ? compound.getInt("burn_capacity") : 0;
        if(compound.contains("inv")){
            ItemStackHandler old = new ItemStackHandler();
            old.deserializeNBT(compound.getCompound("inv"));
            itemHandler.setStackInSlot(0, old.getStackInSlot(1));
        }else{
            itemHandler.deserializeNBT(compound.getCompound("tugItemHandler"));
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("burn", burnTime);
        compound.putInt("burn_capacity", burnCapacity);
        compound.put("tugItemHandler", itemHandler.serializeNBT());
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void onUndock() {
        super.onUndock();
        this.playSound(ModSounds.STEAM_TUG_WHISTLE.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    // Have to implement IInventory to work with hoppers
    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return itemHandler.getStackInSlot(p_70301_1_);
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        if (!this.itemHandler.isItemValid(p_70299_1_, ItemVariant.of(p_70299_2_))){
            return;
        }
        this.itemHandler.insertSlot(p_70299_1_, ItemVariant.of(p_70299_2_), 1, Transaction.openOuter());
        if (!p_70299_2_.isEmpty() && p_70299_2_.getCount() > this.getMaxStackSize()) {
            p_70299_2_.setCount(this.getMaxStackSize());
        }
    }

    public static ItemHandlerComponent createItemHandlerComponent(SteamTugEntity entity) {
        return () -> entity.itemHandler;
    }

	/** internal API **/

	@ApiStatus.Internal
	public float getBurnProgressFloat()
	{
		return burnTime / (float)burnCapacity;
	}

}
