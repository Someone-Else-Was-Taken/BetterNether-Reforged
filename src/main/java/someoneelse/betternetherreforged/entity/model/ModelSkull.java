package someoneelse.betternetherreforged.entity.model;

import com.google.common.collect.ImmutableList;

import someoneelse.betternetherreforged.entity.EntitySkull;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;


public class ModelSkull extends AgeableModel<EntitySkull> {
	private final ModelRenderer head;
	private float rotateAngleX;

	public ModelSkull() {
		textureHeight = 16;
		textureWidth = 32;

		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0, 20, 0);
		head.addBox(-4, -4, -4, 8, 8, 8);
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of();
	}

	@Override
	public void setLivingAnimations(EntitySkull livingEntity, float f, float g, float h) {
		this.rotateAngleX = livingEntity.getSwimAnimation(h);
		super.setLivingAnimations(livingEntity, f, g, h);
	}

	@Override
	public void setRotationAngles(EntitySkull entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		// head.rotateAngleX = (float) Math.toRadians(headPitch);

		boolean rollTooBig = entity.getTicksElytraFlying() > 4;
		this.head.rotateAngleZ = headYaw * 0.017453292F;
		if (rollTooBig) {
			this.head.rotateAngleX = -0.7853982F;
		}
		else if (this.rotateAngleX > 0.0F) {
			this.head.rotateAngleX = this.lerpAngle(this.head.rotateAngleX, headPitch * 0.017453292F, this.rotateAngleX);
		}
		else {
			this.head.rotateAngleX = headPitch * 0.017453292F;
		}
	}

	protected float lerpAngle(float from, float to, float position) {
		float angle = (to - from) % 6.2831855F;

		if (angle < -3.1415927F) {
			angle += 6.2831855F;
		}

		if (angle >= 3.1415927F) {
			angle -= 6.2831855F;
		}

		return from + position * angle;
	}
}
