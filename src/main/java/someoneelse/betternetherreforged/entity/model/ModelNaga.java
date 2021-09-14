package someoneelse.betternetherreforged.entity.model;

import com.google.common.collect.ImmutableList;

import someoneelse.betternetherreforged.entity.EntityNaga;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelNaga extends AgeableModel<EntityNaga> {
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer[] tail;
	public ModelRenderer[] spikes;
	private float rotateAngleX;
	// private double animation;
	// private long preTime;
	private float maxAngle = 0.1F;

	public ModelNaga() {
		textureHeight = 64;
		textureWidth = 64;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-5.0F, -10.0F, -5.0F - 2F, 10.0F, 10.0F, 10.0F);
		head.setRotationPoint(0.0F, -9.0F, 0.0F);

		body = new ModelRenderer(this, 40, 0);
		body.addBox(-2.0F, 0.0F, -1.0F, 4.0F, 20.0F, 2.0F);
		body.setTextureOffset(0, 20);
		body.addBox(-5.0F, 3.0F, -6.0F, 10.0F, 16.0F, 6.0F);
		body.setRotationPoint(0.0F, -10F, 0.0F);

		spikes = new ModelRenderer[8];

		spikes[0] = new ModelRenderer(this, 33, 25);
		spikes[0].addBox(0, 0, 0, 10, 18.0F, 0);
		spikes[0].setRotationPoint(0.0F, 0.0F, 0.0F);
		spikes[0].rotateAngleY = (float) Math.toRadians(-40);
		body.addChild(spikes[0]);

		spikes[1] = new ModelRenderer(this, 33, 25);
		spikes[1].addBox(0, 0, 0, 10, 18.0F, 0);
		spikes[1].setRotationPoint(0.0F, 0, 0.0F);
		spikes[1].rotateAngleY = (float) Math.toRadians(-140);
		body.addChild(spikes[1]);

		tail = new ModelRenderer[4];

		int last = tail.length - 1;
		for (int i = 0; i < tail.length; i++) {
			int height = (tail.length - i) * 4 / tail.length;
			if (height < 2)
				height = 2;
			int width = Math.round((float) height / 2);
			if (width < 1)
				width = 1;

			tail[i] = new ModelRenderer(this, 40, 0);
			tail[i].addBox(-height * 0.5F, 0.0F, -width * 0.5F, height, 20.0F, width);
			tail[i].setRotationPoint(0.0F, 19.0F, 0.0F);

			if (i < last) {
				int px = 32 + (12 - height * 3);

				int index = (i << 1) + 2;
				spikes[index] = new ModelRenderer(this, px, 22);
				spikes[index].addBox(0, 0, 0, height * 3, 20.0F, 0);
				spikes[index].setRotationPoint(0.0F, 0, 0.0F);
				spikes[index].rotateAngleY = (float) Math.toRadians(-60);
				tail[i].addChild(spikes[index]);

				index++;
				spikes[index] = new ModelRenderer(this, px, 22);
				spikes[index].addBox(0, 0, 0, height * 3, 20.0F, 0);
				spikes[index].setRotationPoint(0.0F, 0, 0.0F);
				spikes[index].rotateAngleY = (float) Math.toRadians(-120);
				tail[i].addChild(spikes[index]);
			}

			if (i == 0) {
				body.addChild(tail[i]);
			}
			else {
				tail[i - 1].addChild(tail[i]);
			}
		}

		tail[0].rotateAngleX = (float) Math.toRadians(45);
		tail[1].rotateAngleX = (float) Math.toRadians(45);
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(body);
	}

	@Override
	public void setLivingAnimations(EntityNaga livingEntity, float f, float g, float h) {
		this.rotateAngleX = livingEntity.getSwimAnimation(h);
		super.setLivingAnimations(livingEntity, f, g, h);
	}

	@Override
	public void setRotationAngles(EntityNaga entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		boolean rollTooBig = entity.getTicksElytraFlying() > 4;
		boolean isSwimming = entity.isActualySwimming();
		this.head.rotateAngleY = headYaw * 0.017453292F;
		if (rollTooBig) {
			this.head.rotateAngleX = -0.7853982F;
		}
		else if (this.rotateAngleX > 0.0F) {
			if (isSwimming) {
				this.head.rotateAngleX = this.lerpAngle(this.head.rotateAngleX, -0.7853982F, this.rotateAngleX);
			}
			else {
				this.head.rotateAngleX = this.lerpAngle(this.head.rotateAngleX, headPitch * 0.017453292F, this.rotateAngleX);
			}
		}
		else {
			this.head.rotateAngleX = headPitch * 0.017453292F;
		}

		// long time = System.currentTimeMillis();
		double speed = (entity.isOnGround() && (entity.getMotion().x != 0 || entity.getMotion().z != 0) && !entity.isPassenger()) ? 6 : 0.5;
		maxAngle = this.lerpAngle(maxAngle, speed > 1 ? 0.1F : 0.5F, 0.03F);
		// animation += (time - preTime) * speed / 1000.0;
		double animation = animationProgress * speed / 20;
		float angle = (float) Math.sin(animation) * maxAngle * 0.3F;
		float start_angle = angle;
		tail[0].rotateAngleY = angle;
		for (int i = 1; i < tail.length; i++) {
			angle = (float) Math.atan(Math.sin(i * 1.7 + animation)) * maxAngle;
			tail[i].rotateAngleZ = angle - start_angle;
			start_angle += angle;
		}

		for (int i = 0; i < spikes.length; i++) {
			float rotateAngleY = ((i & 1) == 0) ? (float) Math.toRadians(-50 + Math.sin(animation * 0.4 + i / 2) * 10) : (float) Math.toRadians(-110 - Math.sin(animation * 0.4 + i / 2) * 10);
			spikes[i].rotateAngleY = rotateAngleY;
		}

		// preTime = time;
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

