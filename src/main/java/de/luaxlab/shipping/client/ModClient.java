package de.luaxlab.shipping.client;

import de.luaxlab.shipping.client.entity.model.ChainModel;
import de.luaxlab.shipping.client.entity.model.ChestBargeModel;
import de.luaxlab.shipping.client.entity.model.EnergyTugModel;
import de.luaxlab.shipping.client.entity.model.SteamTugModel;
import de.luaxlab.shipping.client.entity.render.ChestBargeRenderer;
import de.luaxlab.shipping.client.entity.render.StaticVesselRenderer;
import de.luaxlab.shipping.client.screen.EnergyHeadVehicleScreen;
import de.luaxlab.shipping.client.screen.SteamHeadVehicleScreen;
import de.luaxlab.shipping.client.screen.TugRouteScreen;
import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItemModelProperties;
import de.luaxlab.shipping.common.entity.vessel.tug.EnergyTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import dev.architectury.event.events.client.ClientTextureStitchEvent;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ModClient implements ClientModInitializer {


	@Override
	public void onInitializeClient(ModContainer mod) {

		EntityRendererRegistry.register(ModEntities.STEAM_TUG.get(), (ctx) -> new StaticVesselRenderer<>(ctx, SteamTugModel::new, SteamTugModel.LAYER_LOCATION,
				SteamTugModel.TEXTURE) {
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
		EntityRendererRegistry.register(ModEntities.ENERGY_TUG.get(), (ctx) -> new StaticVesselRenderer<>(ctx, SteamTugModel::new, EnergyTugModel.LAYER_LOCATION,
				EnergyTugModel.TEXTURE) {
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
		EntityRendererRegistry.register(ModEntities.CHEST_BARGE.get(), ChestBargeRenderer::new);

		//EntityModelLayerRegistry
		EntityModelLayerRegistry.registerModelLayer(ChainModel.LAYER_LOCATION, ChainModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(SteamTugModel.LAYER_LOCATION, SteamTugModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(ChestBargeModel.LAYER_LOCATION, ChestBargeModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(EnergyTugModel.LAYER_LOCATION, EnergyTugModel::createBodyLayer);

		//Screens
		MenuScreens.register(ModContainers.STEAM_TUG_CONTAINER.get(), SteamHeadVehicleScreen<SteamTugEntity>::new);
		MenuScreens.register(ModContainers.TUG_ROUTE_CONTAINER.get(), TugRouteScreen::new);
		MenuScreens.register(ModContainers.ENERGY_TUG_CONTAINER.get(), EnergyHeadVehicleScreen<EnergyTugEntity>::new);

		//Events
		ClientTextureStitchEvent.PRE.register(ClientEventHandlerImpl.INSTANCE);
		WorldRenderEvents.END.register(ClientEventHandlerImpl.INSTANCE);

		//Item model properties
		ModItemModelProperties.register();
	}
}
