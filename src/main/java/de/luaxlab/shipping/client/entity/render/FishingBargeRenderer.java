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
package de.luaxlab.shipping.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.luaxlab.shipping.client.entity.model.FishingBargeModel;
import de.luaxlab.shipping.common.entity.vessel.barge.FishingBargeEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class FishingBargeRenderer extends AbstractVesselRenderer<FishingBargeEntity> {
    private final FishingBargeModel model;
    public FishingBargeRenderer(EntityRendererProvider.Context context) {
        super(context);
		this.model = new FishingBargeModel(context.bakeLayer(FishingBargeModel.LAYER_LOCATION));
    }

	@Override
	public void render(FishingBargeEntity vesselEntity, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource buffer, int light) {
		model.setBasketAnimation(vesselEntity.getAnimationProgress(tickDelta));
		super.render(vesselEntity, yaw, tickDelta, matrixStack, buffer, light);
	}

	@Override
	EntityModel<FishingBargeEntity> getModel(FishingBargeEntity entity) {
        return model;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FishingBargeEntity pEntity) {
		return FishingBargeModel.TEXTURE;
    }
}
