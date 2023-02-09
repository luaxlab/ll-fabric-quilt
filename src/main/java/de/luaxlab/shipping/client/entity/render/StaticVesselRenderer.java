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
