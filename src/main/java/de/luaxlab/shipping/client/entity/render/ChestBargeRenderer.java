package de.luaxlab.shipping.client.entity.render;

import de.luaxlab.shipping.client.entity.model.ChestBargeModel;
import de.luaxlab.shipping.common.entity.vessel.barge.ChestBargeEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class ChestBargeRenderer extends AbstractVesselRenderer<ChestBargeEntity> {
    private final EntityModel<ChestBargeEntity> model;
	private final boolean isXmas;

    public ChestBargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ChestBargeModel(context.bakeLayer(ChestBargeModel.LAYER_LOCATION));
		Calendar calendar = Calendar.getInstance();
		this.isXmas = calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DAY_OF_MONTH) >= 24 && calendar.get(Calendar.DAY_OF_MONTH) <= 26;
    }


    @Override
	EntityModel<ChestBargeEntity> getModel(ChestBargeEntity entity) {
        return model;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(ChestBargeEntity pEntity) {
		return isXmas ? ChestBargeModel.TEXTURE_XMAS :
				(pEntity.hasCustomName() && "I'm different".equals(pEntity.getCustomName().getString())) ?
						ChestBargeModel.TEXTURE_DIFF : ChestBargeModel.TEXTURE;
    }
}
