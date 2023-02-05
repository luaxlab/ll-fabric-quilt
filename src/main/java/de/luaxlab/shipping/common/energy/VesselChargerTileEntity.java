package de.luaxlab.shipping.common.energy;

import de.luaxlab.shipping.common.block.IVesselLoader;
import de.luaxlab.shipping.common.core.ModBlockEntities;
import de.luaxlab.shipping.common.core.ModComponents;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.util.LinkableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorageUtil;

public class VesselChargerTileEntity extends BlockEntity implements IVesselLoader {
    private static final int MAX_TRANSFER = ModConfig.Server.VESSEL_CHARGER_BASE_MAX_TRANSFER.get();
    private static final int MAX_CAPACITY = ModConfig.Server.VESSEL_CHARGER_BASE_CAPACITY.get();
    private final SimpleReadWriteEnergyStorage internalBattery = new SimpleReadWriteEnergyStorage(MAX_CAPACITY, MAX_TRANSFER, MAX_TRANSFER);
    private int cooldownTime = 0;

    public VesselChargerTileEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VESSEL_CHARGER.get(), pos, state);
        internalBattery.amount = 0;
    }

    private void serverTickInternal() {
        if (this.level != null) {
            --this.cooldownTime;
            if (this.cooldownTime <= 0) {
                this.cooldownTime = tryChargeEntity() ? 0 : 10;
            }
        }
    }

    public static void serverTick(Level ignoredPLevel, BlockPos ignoredpPos, BlockState ignoredpState, VesselChargerTileEntity e) {
        e.serverTickInternal();
    }


    private boolean tryChargeEntity() {
		//return level.getEntitiesOfClass()
        var vsl = IVesselLoader.getEntityCapability(getBlockPos().relative(getBlockState().getValue(VesselChargerBlock.FACING)),
                ModComponents.ENERGY_HANDLER, level);

		return vsl.map(energyComponent -> EnergyStorageUtil.move(internalBattery, energyComponent.getHandler(), MAX_TRANSFER, null) > 0)
				.orElse(false);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        internalBattery.readAdditionalSaveData(compound.getCompound("energy_storage"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("energy_storage", internalBattery.addAdditionalSaveData(new CompoundTag()));
    }

    @Override
    public<T extends Entity & LinkableEntity<T>> boolean hold(T vehicle, @NotNull Mode mode) {
		return ModComponents.ENERGY_HANDLER.maybeGet(vehicle).map(energyComponent -> {
			if (mode == Mode.EXPORT) {
				return (energyComponent.getHandler().getAmount() < energyComponent.getHandler().getCapacity() - 50) && internalBattery.getAmount() > 50;
			}
			return false;
		}).orElse(false);
    }

    public void use(Player player, InteractionHand hand) {
        player.displayClientMessage(Component.translatable("block.littlelogistics.vessel_charger.capacity",
                internalBattery.getAmount(), internalBattery.getCapacity()), false);
    }
}