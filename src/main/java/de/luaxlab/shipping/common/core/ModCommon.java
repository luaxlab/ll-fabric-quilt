package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.event.CommonEventHandlerImpl;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.ModLoadingContext;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModCommon implements ModInitializer {

	public static final String MODID = "littlelogistics";
	public static final Logger LOGGER = LoggerFactory.getLogger("Little Logistics");

	@Override
	public void onInitialize(ModContainer mod) {
		//This is very important!
		ModLoadingContext.registerConfig(MODID, net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.Common.SPEC, "littlelogistics-common.toml");
		ModLoadingContext.registerConfig(MODID, net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.Client.SPEC, "littlelogistics-client.toml");
		ModLoadingContext.registerConfig(MODID, net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.Server.SPEC, "littlelogistics-server.toml");

		//Registration
		Registration.register();

		//Events
		UseEntityCallback.EVENT.register(CommonEventHandlerImpl.INSTANCE);
	}

	public static ResourceLocation identifier(String path)
	{
		return new ResourceLocation(MODID, path);
	}
}
