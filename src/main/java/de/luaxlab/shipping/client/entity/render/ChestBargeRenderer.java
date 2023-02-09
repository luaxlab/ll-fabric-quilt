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
    public @NotNull ResourceLocation getTextureLocation(@NotNull ChestBargeEntity pEntity) {
		return isXmas ? ChestBargeModel.TEXTURE_XMAS :
				(pEntity.hasCustomName() && "I'm different".equals(pEntity.getCustomName().getString())) ?
						ChestBargeModel.TEXTURE_DIFF : ChestBargeModel.TEXTURE;
    }
}
