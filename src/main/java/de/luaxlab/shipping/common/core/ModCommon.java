package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.component.StallingComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import me.lortseam.completeconfig.data.Config;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModCommon implements ModInitializer {

	public static final String MODID = "littlelogistics";
	public static final Logger LOGGER = LoggerFactory.getLogger("Little Logistics");
	public static final Config CONFIG = new Config(MODID, new ModConfig());

	@Override
	public void onInitialize(ModContainer mod) {
		//This is very important!
		CONFIG.load();

		//Registration
		ModItems.register();
		ModBlocks.register();
		ModEntities.register();
		ModSounds.register();
		ModBlockEntities.register();
		ModNetworking.register();
	}

	public static ResourceLocation identifier(String path)
	{
		return new ResourceLocation(MODID, path);
	}
}
