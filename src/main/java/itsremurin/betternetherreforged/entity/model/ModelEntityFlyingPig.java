package itsremurin.betternetherreforged.entity.model;

import com.google.common.collect.ImmutableList;

import itsremurin.betternetherreforged.entity.EntityFlyingPig;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelEntityFlyingPig extends AgeableModel<EntityFlyingPig> {
	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer rightWing;
	private final ModelRenderer leftWing;
	private final ModelRenderer rightWingTip;
	private final ModelRenderer leftWingTip;
	private final ModelRenderer tail;
	private final ModelRenderer legA;
	private final ModelRenderer legB;

	public ModelEntityFlyingPig() {
		this.textureWidth = 128;
		this.textureHeight = 64;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);

		ModelRenderer modelEar = new ModelRenderer(this, 32, 4);
		modelEar.setRotationPoint(-7.0F, -7.0F, -2.0F);
		modelEar.addBox(0.0F, 0.0F, 0.0F, 5.0F, 5.0F, 1.0F);
		this.head.addChild(modelEar);

		modelEar = new ModelRenderer(this, 32, 4);
		modelEar.setRotationPoint(2.0F, -7.0F, -2.0F);
		modelEar.mirror = true;
		modelEar.addBox(0.0F, 0.0F, 0.0F, 5.0F, 5.0F, 1.0F);
		this.head.addChild(modelEar);

		ModelRenderer piglet = new ModelRenderer(this, 32, 0);
		piglet.setRotationPoint(-2.0F, -1.0F, -5.0F);
		piglet.addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 1.0F);
		this.head.addChild(piglet);

		this.body = new ModelRenderer(this, 0, 16);
		this.body.addBox(-5.0F, 3.0F, -4.0F, 10.0F, 14.0F, 8.0F);

		this.rightWing = new ModelRenderer(this, 36, 10);
		this.rightWing.setRotationPoint(5, 2.5F, 0);
		this.rightWing.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F);
		this.body.addChild(this.rightWing);

		this.rightWingTip = new ModelRenderer(this, 36, 26);
		this.rightWingTip.setRotationPoint(16.0F, 0.0F, 0.0F);
		this.rightWingTip.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F);
		this.rightWing.addChild(this.rightWingTip);

		this.leftWing = new ModelRenderer(this, 36, 10);
		this.leftWing.mirror = true;
		this.leftWing.setRotationPoint(-5, 2.5F, 0);
		this.leftWing.addBox(-16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F);
		this.body.addChild(this.leftWing);

		this.leftWingTip = new ModelRenderer(this, 36, 26);
		this.leftWingTip.mirror = true;
		this.leftWingTip.setRotationPoint(-16.0F, 0.0F, 0.0F);
		this.leftWingTip.addBox(-16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F);
		this.leftWing.addChild(this.leftWingTip);

		tail = new ModelRenderer(this, 0, 40);
		tail.setRotationPoint(0, 17, 0);
		tail.addBox(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F);
		this.body.addChild(tail);

		this.legA = new ModelRenderer(this, 0, 48);
		legA.setRotationPoint(1.5F, 15, -4);
		legA.addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F);
		this.body.addChild(legA);

		this.legB = new ModelRenderer(this, 0, 48);
		legB.setRotationPoint(-4.5F, 15, -4);
		legB.addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F);
		this.body.addChild(legB);
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(body);
	}

	@Override
	public void setRotationAngles(EntityFlyingPig entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		if (entity.isRoosting()) {
			this.head.rotateAngleX = headPitch * 0.017453292F;
			this.head.rotateAngleY = 3.1415927F - headYaw * 0.017453292F;
			this.head.rotateAngleZ = 3.1415927F;

			this.body.rotateAngleX = 3.1415927F;
			this.rightWing.rotateAngleX = 0;
			this.rightWing.rotateAngleY = -1.2566371F;
			this.rightWingTip.rotateAngleY = 2.8F;
			this.leftWing.rotateAngleX = this.rightWing.rotateAngleX;
			this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
			this.leftWingTip.rotateAngleY = -this.rightWingTip.rotateAngleY;

			this.head.setRotationPoint(0.0F, 25, 0.0F);
			this.body.setRotationPoint(0.0F, 24, 0.0F);

			this.legA.rotateAngleX = 0;
			this.legB.rotateAngleX = 0;
			this.tail.rotateAngleX = 0.1F;

			legA.setRotationPoint(1.5F, 15, -3);
			legB.setRotationPoint(-4.5F, 15, -3);
		}
		else {
			this.head.rotateAngleX = headPitch * 0.017453292F;
			this.head.rotateAngleY = headYaw * 0.017453292F;
			this.head.rotateAngleZ = 0.0F;

			this.body.rotateAngleX = 0.7853982F + MathHelper.cos(animationProgress * 0.1F) * 0.15F;
			this.body.rotateAngleY = 0.0F;
			this.rightWing.rotateAngleY = MathHelper.cos(animationProgress * 0.4F) * 3.1415927F * 0.25F;
			this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
			this.rightWingTip.rotateAngleY = this.rightWing.rotateAngleY * 0.75F;
			this.leftWingTip.rotateAngleY = -this.rightWing.rotateAngleY * 0.75F;
			this.tail.rotateAngleX = MathHelper.cos(animationProgress * 0.3F) * 0.25F;

			this.legA.rotateAngleX = -this.body.rotateAngleX + MathHelper.sin(animationProgress * 0.05F) * 0.1F;
			this.legB.rotateAngleX = -this.body.rotateAngleX + MathHelper.cos(animationProgress * 0.05F) * 0.1F;

			this.head.setRotationPoint(0.0F, 8.0F, 0.0F);
			this.body.setRotationPoint(0.0F, 8.0F, 0.0F);

			legA.setRotationPoint(1.5F, 15, -4);
			legB.setRotationPoint(-4.5F, 15, -4);
		}
	}
}
