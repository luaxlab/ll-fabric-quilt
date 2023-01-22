package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.accessor.TugRouteScreenDataAccessor;
import de.luaxlab.shipping.common.entity.container.SteamHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import de.luaxlab.shipping.common.item.container.TugRouteContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
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

	public static final MenuType<TugRouteContainer> TUG_ROUTE_CONTAINER =
			ScreenHandlerRegistry.registerExtended(ModCommon.identifier("tug_route_container"),
			(syncId, inventory, buf) -> new TugRouteContainer(syncId, inventory.player.level,
					new TugRouteScreenDataAccessor(makeIntArray(buf)), inventory, inventory.player));

	public static final MenuType<SteamHeadVehicleContainer<SteamTugEntity>> STEAM_TUG_CONTAINER =
			ScreenHandlerRegistry.registerExtended(ModCommon.identifier("steam_tug_container"),
					(syncId, inventory, buf) -> new SteamHeadVehicleContainer<SteamTugEntity>(syncId, inventory.player.level,
							new SteamHeadVehicleDataAccessor(makeIntArray(buf)), inventory, inventory.player));
}
