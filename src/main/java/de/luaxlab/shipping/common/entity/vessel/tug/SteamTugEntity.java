package de.luaxlab.shipping.common.entity.vessel.tug;

import de.luaxlab.shipping.common.container.SingleSlotItemContainer;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.core.ModSounds;
import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.container.SteamHeadVehicleContainer;
import lombok.Getter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;

import javax.annotation.Nullable;

public class SteamTugEntity extends AbstractTugEntity {
    private static final int FURNACE_FUEL_MULTIPLIER= ModConfig.Server.STEAM_TUG_FUEL_MULTIPLIER.get();
	@Getter
    private final SingleSlotItemContainer fuelContainer = new SingleSlotItemContainer(FurnaceBlockEntity::isFuel);
    protected int burnTime = 0;
    protected int burnCapacity = 0;

    public SteamTugEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
    }

    public SteamTugEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.STEAM_TUG, worldIn, x, y, z);
		addContainer(fuelContainer);
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
        return new SteamHeadVehicleDataAccessor.Builder(this.getId())
                .withBurnProgress(this::getBurnProgress)
                .withLit(this::isLit)
                .withVisitedSize(() -> nextStop)
                .withOn(() -> engineOn)
                .withRouteSize(() -> path != null ? path.size() : 0)
                .build();
    }

    /*@Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }*/

    @Override
    protected boolean tickFuel() {
        if (burnTime > 0) {
            burnTime--;
            return true;
        } else {
            ItemStack stack = fuelContainer.getItem(0);
            if (!stack.isEmpty()) {
                burnCapacity = (((FuelRegistryImpl)FuelRegistryImpl.INSTANCE).getFuelTimes().getOrDefault(stack.getItem(), 0) * FURNACE_FUEL_MULTIPLIER) - 1;
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
        return ModItems.STEAM_TUG;
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        burnTime = compound.contains("burn") ? compound.getInt("burn") : 0;
        burnCapacity = compound.contains("burn_capacity") ? compound.getInt("burn_capacity") : 0;
        /*if(compound.contains("inv")){
			SimpleContainer old = new SimpleContainer();
			old.fromTag(compound.getList("inv", Tag.TAG_COMPOUND));
			routeContainer.setItem(0, old.getItem(0));
		}else{
			routeContainer.fromTag(compound.getList("tugItemHandler", Tag.TAG_COMPOUND));
		}*/

        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("burn", burnTime);
        compound.putInt("burn_capacity", burnCapacity);
        /*compound.put("tugItemHandler", routeContainer.createTag());*/
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void onUndock() {
        super.onUndock();
        this.playSound(ModSounds.STEAM_TUG_WHISTLE, 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

	@Override
	public boolean isValid(Player pPlayer) {
		return stillValid(pPlayer);
	}

	// Have to implement IInventory to work with hoppers

}
