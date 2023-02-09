package de.luaxlab.shipping.client.entity.model;// Made with Blockbench 4.1.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.entity.vessel.barge.FishingBargeEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FishingBargeModel extends EntityModel<FishingBargeEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ModCommon.identifier( "fishingbargemodel"), "main");
	public static final ResourceLocation TEXTURE = ModCommon.identifier("textures/entity/fishing_barge.png");
	private final ModelPart root;
	private final ModelPart left, leftBasket;
	private final ModelPart right, rightBasket;

	public FishingBargeModel(ModelPart root) {
		this.root = root.getChild("root");
		this.left = root.getChild("left");
		this.leftBasket = left.getChild("leftBasket");
		this.right = root.getChild("right");
		this.rightBasket = right.getChild("rightBasket");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -28.0F, -7.0F, 12.0F, 5.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(18, 23).addBox(-8.0F, -30.0F, -7.0F, 2.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 19).addBox(6.0F, -30.0F, -7.0F, 2.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left = partdefinition.addOrReplaceChild("left", CubeListBuilder.create().texOffs(6, 0).addBox(-6.0F, -10.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(5.0F, -10.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -4.0F));

		PartDefinition leftBasket = left.addOrReplaceChild("leftBasket", CubeListBuilder.create().texOffs(36, 19).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(38, 8).addBox(-5.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition cube_r1 = leftBasket.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 8).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 1.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition right = partdefinition.addOrReplaceChild("right", CubeListBuilder.create().texOffs(6, 0).addBox(-6.0F, -10.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(5.0F, -10.0F, -1.0F, 1.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 4.0F));

		PartDefinition rightBasket = right.addOrReplaceChild("rightBasket", CubeListBuilder.create().texOffs(36, 19).addBox(-5.0F, -1.0F, -3.0F, 10.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(38, 8).addBox(-5.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition cube_r2 = rightBasket.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(38, 8).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 1.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(FishingBargeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		/* Empty on purpose */
	}

	public void setBasketAnimation(float progress)
	{
		/* Set rotations of baskets */
		float value = (float) Math.sin(progress * Mth.HALF_PI) * Mth.HALF_PI;
		left.xRot = value; // * MathHelper.HALF_PI;
		leftBasket.xRot = -value;
		right.xRot = -value;
		rightBasket.xRot = value;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, buffer, packedLight, packedOverlay);
		left.render(poseStack, buffer, packedLight, packedOverlay);
		right.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
