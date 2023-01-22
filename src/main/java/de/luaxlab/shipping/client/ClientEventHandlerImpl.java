package de.luaxlab.shipping.client;

import de.luaxlab.shipping.common.core.ModItems;
import dev.architectury.event.events.client.ClientTextureStitchEvent;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ClientEventHandlerImpl implements ClientTextureStitchEvent.Pre, WorldRenderEvents.End {

    //private final Identifier ROUTE_BEACON_TEXTURE = ModCommon.identifier("textures/entity/beacon_beam.png");

    public static final ClientEventHandlerImpl INSTANCE = new ClientEventHandlerImpl();

    @Override
	public void stitch(TextureAtlas atlas, Consumer<ResourceLocation> spriteAdder) {
		if(atlas.location() != ModItems.EMPTY_ATLAS_LOC) return;
		spriteAdder.accept(ModItems.TUG_ROUTE_ICON);
		spriteAdder.accept(ModItems.EMPTY_ENERGY);
		spriteAdder.accept(ModItems.LOCO_ROUTE_ICON);
	}

    @Override
    public void onEnd(WorldRenderContext context) {

    }
}
