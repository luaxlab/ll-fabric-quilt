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
