package de.luaxlab.shipping.common.entity.vessel.tug;

import de.luaxlab.shipping.common.component.EnergyComponent;
import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.energy.EnergyUtils;
import de.luaxlab.shipping.common.energy.SimpleReadWriteEnergyStorage;
import de.luaxlab.shipping.common.entity.accessor.EnergyHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.container.EnergyHeadVehicleContainer;
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
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"removal", "UnstableApiUsage"})
public class EnergyTugEntity extends AbstractTugEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private static final long MAX_ENERGY = ModConfig.Server.ENERGY_TUG_BASE_CAPACITY.get();
    private static final long MAX_TRANSFER = ModConfig.Server.ENERGY_TUG_BASE_MAX_CHARGE_RATE.get();
    private static final long ENERGY_USAGE = ModConfig.Server.ENERGY_TUG_BASE_ENERGY_USAGE.get();
    private final SimpleReadWriteEnergyStorage internalBattery = new SimpleReadWriteEnergyStorage(MAX_ENERGY, MAX_TRANSFER, Integer.MAX_VALUE);

    public EnergyTugEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
        internalBattery.amount = 0;
    }

    public EnergyTugEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.ENERGY_TUG.get(), worldIn, x, y, z);
        internalBattery.amount = 0;
    }

    // todo: Store contents?
    @Override
    public Item getDropItem() {
        return ModItems.ENERGY_TUG.get();
    }

    @Override
    protected ExtendedScreenHandlerFactory createContainerProvider() {
        return new ExtendedScreenHandlerFactory() {

			@Override
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
				getDataAccessor().write(buf);
			}

			@Override
            public @NotNull Component getDisplayName() {
                return Component.translatable("screen.littlelogistics.energy_tug");
            }

            @Override
            public AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player player) {
                return new EnergyHeadVehicleContainer<EnergyTugEntity>(i, level, getDataAccessor(), playerInventory, player);
            }
        };
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemVariant variant) {
				return EnergyStorageUtil.isEnergyStorage(variant.toStack());
			}


			@Override
			public long insertSlot(int slot, @Nonnull ItemVariant stack, long maxAmount, @Nullable TransactionContext transaction) {
				if (!isItemValid(slot, stack)) {
					return 0;
				}

				return super.insertSlot(slot, stack, maxAmount, transaction);
			}
        };
    }

    @Override
    protected void makeSmoke(){

    }

    // Energy tug can be loaded at all times since there is no concern
    // with mix-ups like with fluids and items
    @Override
    public boolean allowDockInterface(){
        return true;
    }

    @Override
    public EnergyHeadVehicleDataAccessor getDataAccessor() {
        return (EnergyHeadVehicleDataAccessor) new EnergyHeadVehicleDataAccessor.Builder()
                .withEnergy(internalBattery::getAmount)
                .withCapacity(internalBattery::getCapacity)
                .withLit(() -> internalBattery.getAmount() > 0) // has energy
                .withId(this.getId())
                .withVisitedSize(() -> nextStop)
                .withOn(() -> engineOn)
                .withCanMove(enrollmentHandler::mayMove)
                .withRouteSize(() -> path != null ? path.size() : 0)
                .build();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        internalBattery.readAdditionalSaveData(compound.getCompound("energy_storage"));
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
        CompoundTag energyNBT = new CompoundTag();
        internalBattery.addAdditionalSaveData(energyNBT);
        compound.put("energy_storage", energyNBT);
        compound.put("tugItemHandler", itemHandler.serializeNBT());
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void tick() {
        // grab energy from capacitor
        if (!level.isClientSide && !internalBattery.isFull()) {
            EnergyStorage source = EnergyUtils.getEnergyCapabilityInSlot(0, itemHandler);
            if (source != null && source.supportsExtraction()) {
				EnergyStorageUtil.move(source,internalBattery, MAX_TRANSFER, null);
            }
        }

        super.tick();
    }

    @Override
    protected boolean tickFuel() {
        return internalBattery.consume(ENERGY_USAGE) > 0;
    }

    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

	@Override
	public @NotNull ItemStack getItem(int slot) {
		return itemHandler.getStackInSlot(slot);
	}

	@Override
	public void setItem(int slot, @NotNull ItemStack stack) {
		if (!this.itemHandler.isItemValid(slot, ItemVariant.of(stack))){
			return;
		}
		this.itemHandler.insertSlot(slot, ItemVariant.of(stack), 1, Transaction.openOuter());
		if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
	}

	public static ItemHandlerComponent createItemHandlerComponent(EnergyTugEntity entity) {
		return () -> entity.itemHandler;
	}

	public static EnergyComponent createEnergyComponent(EnergyTugEntity entity) {
		return () -> entity.internalBattery;
	}

	/** internal API **/

	@ApiStatus.Internal
	public float getEnergyLevel()
	{
		return internalBattery.amount / (float)internalBattery.capacity;
	}
}
