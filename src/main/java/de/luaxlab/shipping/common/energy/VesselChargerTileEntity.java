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
package de.luaxlab.shipping.common.energy;

import de.luaxlab.shipping.common.block.IVesselLoader;
import de.luaxlab.shipping.common.core.ModBlockEntities;
import de.luaxlab.shipping.common.core.ModComponents;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.util.LinkableEntity;
import lombok.AccessLevel;
import lombok.Getter;
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
	@Getter(AccessLevel.PUBLIC)
    private final SimpleReadWriteEnergyStorage internalBattery = new SimpleReadWriteEnergyStorage(MAX_CAPACITY, MAX_TRANSFER, MAX_TRANSFER);
    private int cooldownTime = 0;

    public VesselChargerTileEntity(BlockPos pos, BlockState state) {
        super(IntegratedEnergyExtension.VESSEL_CHARGER_ENTITY.get(), pos, state);
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
                IntegratedEnergyExtension.ENERGY_HANDLER, level);

		return vsl.map(energyComponent -> EnergyStorageUtil.move(internalBattery, energyComponent.getHandler(), MAX_TRANSFER, null) > 0)
				.orElse(false);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        internalBattery.readAdditionalSaveData(compound.getCompound("energy_storage"));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("energy_storage", internalBattery.addAdditionalSaveData(new CompoundTag()));
    }

    @Override
    public<T extends Entity & LinkableEntity<T>> boolean hold(T vehicle, @NotNull Mode mode) {
		return IntegratedEnergyExtension.ENERGY_HANDLER.maybeGet(vehicle).map(energyComponent -> {
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
