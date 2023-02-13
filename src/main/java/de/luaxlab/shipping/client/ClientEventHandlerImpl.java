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
package de.luaxlab.shipping.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.util.TugRouteNode;
import dev.architectury.event.events.client.ClientTextureStitchEvent;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class ClientEventHandlerImpl implements ClientTextureStitchEvent.Pre, WorldRenderEvents.End {

    private final ResourceLocation ROUTE_BEACON_TEXTURE = ModCommon.identifier("textures/entity/beacon_beam.png");

    public static final ClientEventHandlerImpl INSTANCE = new ClientEventHandlerImpl();

    @Override
	public void stitch(TextureAtlas atlas, Consumer<ResourceLocation> spriteAdder) {
		if(atlas.location() != ModItems.EMPTY_ATLAS_LOC) return;
		spriteAdder.accept(ModItems.TUG_ROUTE_ICON);
		spriteAdder.accept(ModItems.EMPTY_ENERGY);
		spriteAdder.accept(ModItems.LOCO_ROUTE_ICON);
	}

	@Override
	public void onEnd(WorldRenderContext event) {
		Player player = Minecraft.getInstance().player;
		assert player != null;
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

		if (stack.getItem().equals(ModItems.TUG_ROUTE.get())){
			if(ModConfig.Client.DISABLE_TUG_ROUTE_BEACONS.get()){
				return;
			}
			Vec3 vector3d = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
			double d0 = vector3d.x();
			double d1 = vector3d.y();
			double d2 = vector3d.z();
			MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			List<TugRouteNode> route = TugRouteItem.getRoute(stack);
			for (int i = 0, routeSize = route.size(); i < routeSize; i++) {
				TugRouteNode node = route.get(i);
				PoseStack matrixStack = event.matrixStack();

				matrixStack.pushPose();
				matrixStack.translate(node.getX() - d0, 1 - d1, node.getZ() - d2);

				BeaconRenderer.renderBeaconBeam(matrixStack, renderTypeBuffer, ROUTE_BEACON_TEXTURE, event.tickDelta(),
						1F, player.level.getGameTime(), player.level.getMinBuildHeight(), 1024,
						DyeColor.RED.getTextureDiffuseColors(), 0.2F, 0.25F);
				matrixStack.popPose();
				matrixStack.pushPose();
				matrixStack.translate(node.getX() - d0 , player.getY() + 2 - d1, node.getZ() - d2 );
				matrixStack.scale(-0.025F, -0.025F, -0.025F);

				matrixStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

				Matrix4f matrix4f = matrixStack.last().pose();

				Font fontRenderer = Minecraft.getInstance().font;
				String text = node.getDisplayName(i);
				float width = (-fontRenderer.width(text) / (float) 2);
				fontRenderer.drawInBatch(text, width, 0.0F, -1, true, matrix4f, renderTypeBuffer, true, 0, 15728880);
				matrixStack.popPose();

			}
			renderTypeBuffer.endBatch();
		}
	}
}
