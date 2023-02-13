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
package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.entity.accessor.EnergyHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.accessor.TugRouteScreenDataAccessor;
import de.luaxlab.shipping.common.entity.container.EnergyHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.container.FishingBargeContainer;
import de.luaxlab.shipping.common.entity.container.SteamHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.vessel.tug.EnergyTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import de.luaxlab.shipping.common.item.container.TugRouteContainer;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;

public class ModContainers {

	private static SimpleContainerData makeIntArray(FriendlyByteBuf buffer) {
		int size = (buffer.readableBytes() + 1) / 4;
		SimpleContainerData arr = new SimpleContainerData(size);
		for (int i = 0; i < size; i++) {
			arr.set(i, buffer.readInt());
		}
		return arr;
	}

	public static final RegistryObject<MenuType<SteamHeadVehicleContainer<SteamTugEntity>>> STEAM_TUG_CONTAINER =
			Registration.CONTAINERS.register("tug_container",
					() -> new ExtendedScreenHandlerType<>(
							(windowId, inv, data) ->
									new SteamHeadVehicleContainer<>(windowId, inv.player.level, new SteamHeadVehicleDataAccessor(makeIntArray(data)), inv, inv.player)));

	public static final RegistryObject<MenuType<EnergyHeadVehicleContainer<EnergyTugEntity>>> ENERGY_TUG_CONTAINER =
			Registration.CONTAINERS.register("energy_tug_container",
					() -> new ExtendedScreenHandlerType<>(
							(windowId, inv, data) ->
									new EnergyHeadVehicleContainer<>(windowId, inv.player.level, new EnergyHeadVehicleDataAccessor(makeIntArray(data)), inv, inv.player)));

//	public static final RegistryObject<MenuType<SteamHeadVehicleContainer<SteamLocomotiveEntity>>> STEAM_LOCOMOTIVE_CONTAINER =
//			Registration.CONTAINERS.register("steam_locomotive_container",
//					() -> new ExtendedScreenHandlerType<>(
//							(windowId, inv, data) ->
//									new SteamHeadVehicleContainer<>(windowId, inv.player.level, new SteamHeadVehicleDataAccessor(makeIntArray(data)), inv, inv.player)));

//	public static final RegistryObject<MenuType<EnergyHeadVehicleContainer<EnergyLocomotiveEntity>>> ENERGY_LOCOMOTIVE_CONTAINER =
//			Registration.CONTAINERS.register("energy_locomotive_container",
//					() -> new ExtendedScreenHandlerType<>(
//							(windowId, inv, data) ->
//									new EnergyHeadVehicleContainer<>(windowId, inv.player.level, new EnergyHeadVehicleDataAccessor(makeIntArray(data)), inv, inv.player)));

	public static final RegistryObject<MenuType<FishingBargeContainer>> FISHING_BARGE_CONTAINER =
			Registration.CONTAINERS.register("fishing_barge_container",
					() -> new ExtendedScreenHandlerType<>(
							(windowId, inv, data) ->
									new FishingBargeContainer(windowId, inv.player.level, data.readInt(), inv, inv.player)));

	public static final RegistryObject<MenuType<TugRouteContainer>> TUG_ROUTE_CONTAINER =
			Registration.CONTAINERS.register("tug_route_container",
					() -> new ExtendedScreenHandlerType<>(
							(windowId, inv, data) ->
									new TugRouteContainer(windowId, inv.player.level, new TugRouteScreenDataAccessor(makeIntArray(data)), inv, inv.player)));




	public static void register () {}
}
