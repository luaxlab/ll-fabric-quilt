/*
 Little Logistics: Quilt Edition Integration for "What The Hell is This?"
 Copyright © 2023 LuaX, Abbie

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
package de.luaxlab.shipping.integration.wthit;

import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.entity.accessor.HeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.vessel.tug.AbstractTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.EnergyTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.VehicleFrontPart;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.BarComponent;
import mcp.mobius.waila.api.component.ItemComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;


public class ModExtensionWailaTug implements IWailaPlugin, IEntityComponentProvider, IServerDataProvider<AbstractTugEntity> {

	private static final ResourceLocation STATION_ID = ModCommon.identifier("station_id");

	@Override
	public void register(IRegistrar registrar) {
		registrar.addConfig(STATION_ID, true);
		registrar.addEntityData(this, AbstractTugEntity.class);
		registrar.addComponent(this, TooltipPosition.BODY, AbstractTugEntity.class);
		registrar.addOverride(new IEntityComponentProvider() {
			@Override
			public @Nullable Entity getOverride(IEntityAccessor accessor, IPluginConfig config) {
				return ((VehicleFrontPart)accessor.getEntity()).getParent();
			}
		}, VehicleFrontPart.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
		if(config.getBoolean(STATION_ID) && accessor.getServerData().getInt("total") != 0)
		{
			tooltip.addLine(Component.translatable("screen.littlelogistics.waila.visited",
					accessor.getServerData().getInt("visited"), accessor.getServerData().getInt("total")));
		}
		if(accessor.getServerData().contains("fuelLevel")) {
			var fuel = ItemStack.of(accessor.getServerData().getCompound("fuel"));
			float fuelLevel = accessor.getServerData().getFloat("fuelLevel");
			tooltip.addLine()
					.with(new ItemComponent(fuel))
					.with(new BarComponent(fuelLevel, 0xff801b1b,
							fuelLevel > 0f ? Component.translatable("screen.littlelogistics.waila.fuel")
							: fuel.isEmpty() ? Component.translatable("screen.littlelogistics.waila.fuel.empty") :
									Component.translatable("screen.littlelogistics.waila.fuel.ready")));
			if(fuel.isEmpty() && fuelLevel > 0f)
				tooltip.addLine(Component.translatable("screen.littlelogistics.waila.fuel.refuel"));
		}
		if(accessor.getServerData().contains("energy")) {
			float energy = accessor.getServerData().getFloat("energy");
			tooltip.addLine()
					.with(new BarComponent(energy, 0xff801b1b,
							energy > 0f ? Component.translatable("screen.littlelogistics.waila.energy")
									: Component.translatable("screen.littlelogistics.waila.energy.empty")));
		}



	}

	@Override
	public void appendServerData(CompoundTag data, IServerAccessor<AbstractTugEntity> accessor, IPluginConfig config) {

		data.putInt("total",accessor.getTarget().getTotalStops());
		data.putInt("visited", accessor.getTarget().getTotalStops());
		if(accessor.getTarget() instanceof SteamTugEntity steam)
		{
			data.putFloat("fuelLevel", steam.getBurnProgressFloat());
			data.put("fuel", steam.getItem(0).save(new CompoundTag()));
		}
		if(accessor.getTarget() instanceof EnergyTugEntity energy)
		{
			data.putFloat("energy", energy.getEnergyLevel());
		}
	}
}
