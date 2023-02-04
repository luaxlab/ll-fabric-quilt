package de.luaxlab.shipping.client.entity.render;

import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class StaticVesselRenderer<T extends VesselEntity> extends AbstractVesselRenderer<T> {
    private final EntityModel<T> model;
    private final ResourceLocation textureLocation;

    public StaticVesselRenderer(EntityRendererProvider.Context context,
                                ModelSupplier<T> supplier,
                                ModelLayerLocation location,
                                ResourceLocation texture) {
        super(context);
        this.model = supplier.supply(context.bakeLayer(location));
        this.textureLocation = texture;
    }

    @Override
	EntityModel<T> getModel(T entity) {
        return model;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(T pEntity) {
        return textureLocation;
    }

    @FunctionalInterface
    public interface ModelSupplier<T extends VesselEntity> {
        EntityModel<T> supply(ModelPart root);
    }
}
