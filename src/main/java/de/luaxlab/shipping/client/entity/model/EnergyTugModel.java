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
package de.luaxlab.shipping.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.entity.vessel.tug.AbstractTugEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports
public class EnergyTugModel extends EntityModel<AbstractTugEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModCommon.identifier("energytugmodel"), "main");

	public static final ResourceLocation TEXTURE = ModCommon.identifier("textures/entity/energy_tug.png");

	private final ModelPart bb_main;

	public EnergyTugModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 30).addBox(-7.0F, -9.0F, -19.0F, 14.0F, 6.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(58, 53).addBox(-9.0F, -11.0F, -19.0F, 2.0F, 5.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(52, 17).addBox(-7.0F, -11.0F, -21.0F, 14.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(52, 30).addBox(7.0F, -11.0F, -19.0F, 2.0F, 5.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(0, 60).addBox(-9.0F, -10.0F, -21.0F, 18.0F, 2.0F, 6.0F, new CubeDeformation(0.25F))
				.texOffs(52, 0).addBox(-6.0F, -14.0F, -17.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.0F, -20.0F, -14.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(40, 60).addBox(-4.0F, -21.0F, -15.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -9.0F, -19.0F, 14.0F, 6.0F, 24.0F, new CubeDeformation(0.25F))
				.texOffs(52, 42).addBox(-0.5F, -27.0F, -11.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(@NotNull AbstractTugEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
