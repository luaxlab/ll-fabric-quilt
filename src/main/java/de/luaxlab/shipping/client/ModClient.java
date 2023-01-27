package de.luaxlab.shipping.client;

import de.luaxlab.shipping.client.entity.model.ChainModel;
import de.luaxlab.shipping.client.entity.model.SteamTugModel;
import de.luaxlab.shipping.client.entity.render.StaticVesselRenderer;
import de.luaxlab.shipping.client.screen.SteamHeadVehicleScreen;
import de.luaxlab.shipping.client.screen.TugRouteScreen;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import dev.architectury.event.events.client.ClientTextureStitchEvent;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ModClient implements ClientModInitializer {


	@Override
	public void onInitializeClient(ModContainer mod) {

		EntityRendererRegistry.register(ModEntities.STEAM_TUG.get(), (ctx) -> new StaticVesselRenderer<>(ctx, SteamTugModel::new, SteamTugModel.LAYER_LOCATION,
				ModCommon.identifier("textures/entity/tug.png")) {
			// todo: fix in models itself
			@Override
			protected double getModelYoffset() {
				return 1.45D;
			}

			@Override
			protected float getModelYrot() {
				return 0;
			}
		});

		//EntityModelLayerRegistry
		EntityModelLayerRegistry.registerModelLayer(ChainModel.LAYER_LOCATION, ChainModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(SteamTugModel.LAYER_LOCATION, SteamTugModel::createBodyLayer);

		//Screens
		MenuScreens.register(ModContainers.STEAM_TUG_CONTAINER.get(), SteamHeadVehicleScreen<SteamTugEntity>::new);
		MenuScreens.register(ModContainers.TUG_ROUTE_CONTAINER.get(), TugRouteScreen::new);

		//Events
		ClientTextureStitchEvent.PRE.register(ClientEventHandlerImpl.INSTANCE);
		WorldRenderEvents.END.register(ClientEventHandlerImpl.INSTANCE);

	}
}
