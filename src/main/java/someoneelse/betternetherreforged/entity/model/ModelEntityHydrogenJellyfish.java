package someoneelse.betternetherreforged.entity.model;

import com.google.common.collect.ImmutableList;

import someoneelse.betternetherreforged.entity.EntityHydrogenJellyfish;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelEntityHydrogenJellyfish extends AgeableModel<EntityHydrogenJellyfish> {
	private static double HALF_PI = Math.PI * 0.5;
	private static final int LEGS = 8;
	private final ModelRenderer body;
	private final ModelRenderer body_top;
	private final ModelRenderer[] legs_1;
	private final ModelRenderer[] legs_2;
	private final ModelRenderer[] legs_3;
	private final ModelRenderer[] wings;
	private final ModelRenderer[] leg_details;

	public ModelEntityHydrogenJellyfish() {
		textureHeight = 128;
		textureWidth = 128;

		body = new ModelRenderer(this, 0, 0);
		body.addBox(-14F, 0F, -14F, 28, 18, 28);
		body.setRotationPoint(0F, -48, 0F);

		body_top = new ModelRenderer(this, 0, 46);
		body_top.addBox(-10F, 0F, -10F, 20, 6, 20);
		body_top.setRotationPoint(0F, -6F, 0F);
		body.addChild(body_top);

		legs_1 = new ModelRenderer[LEGS];
		legs_2 = new ModelRenderer[LEGS];
		legs_3 = new ModelRenderer[LEGS];
		leg_details = new ModelRenderer[LEGS * 3];
		wings = new ModelRenderer[LEGS];
		for (int i = 0; i < LEGS; i++) {
			int li = i * 3;
			float angle = (float) (i * Math.PI * 2 / LEGS);
			float x = (float) Math.sin(angle) * 10;
			float z = (float) Math.cos(angle) * 10;

			legs_1[i] = new ModelRenderer(this, 60, 46);
			legs_1[i].addBox(-3F, 0F, -3F, 6, 14, 6);
			legs_1[i].setRotationPoint(x, 18, z);
			legs_1[i].rotateAngleY = angle;
			body.addChild(legs_1[i]);

			leg_details[li] = new ModelRenderer(this, 97, 46);
			leg_details[li].addBox(-8F, 0F, 0F, 16, 14, 0);
			leg_details[li].setTextureOffset(97, 30);
			leg_details[li].addBox(0F, 0F, -8F, 0, 14, 16);
			leg_details[li].rotateAngleY = (float) Math.toRadians(45);
			legs_1[i].addChild(leg_details[li]);

			legs_2[i] = new ModelRenderer(this, 0, 72);
			legs_2[i].addBox(-2F, -24F, -2F, 4, 28, 4);
			legs_2[i].setRotationPoint(0F, 14F, 0F);
			legs_1[i].addChild(legs_2[i]);

			li++;
			leg_details[li] = new ModelRenderer(this, 98, 60);
			leg_details[li].addBox(-7F, -24F, 0F, 14, 28, 0);
			leg_details[li].setTextureOffset(98, 48);
			leg_details[li].addBox(0F, -24F, -7F, 0, 28, 14);
			leg_details[li].rotateAngleY = (float) Math.toRadians(45);
			legs_2[i].addChild(leg_details[li]);

			legs_3[i] = new ModelRenderer(this, 16, 72);
			legs_3[i].addBox(-1F, -24F, -1F, 2, 28, 2);
			legs_3[i].setRotationPoint(0F, -28F, 0F);
			legs_2[i].addChild(legs_3[i]);

			li++;
			leg_details[li] = new ModelRenderer(this, 99, 88);
			leg_details[li].addBox(-6F, -28F, 0F, 12, 32, 0);
			leg_details[li].setTextureOffset(99, 76);
			leg_details[li].addBox(0F, -28F, -6F, 0, 32, 12);
			leg_details[li].rotateAngleY = (float) Math.toRadians(45);
			legs_3[i].addChild(leg_details[li]);

			x = (float) Math.sin(angle);
			z = (float) Math.cos(angle);
			x = Math.signum(x) * (float) Math.pow(Math.abs(x), 0.25F);
			z = Math.signum(z) * (float) Math.pow(Math.abs(z), 0.25F);
			x *= 12;
			z *= 12;

			wings[i] = new ModelRenderer(this, 60, 4);
			wings[i].setRotationPoint(x, 12 - (i & 1) * 6, z);
			wings[i].addBox(-12F, 0F, 0F, 24F, 0F, 24F);
			wings[i].rotateAngleY = angle;
			body.addChild(wings[i]);
		}
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setRotationAngles(EntityHydrogenJellyfish entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		double time = animationProgress * 0.03;

		body.setRotationPoint(0, (float) Math.sin(time) * 8 - 42, 0);

		double sin1 = Math.sin(time);
		double sin2 = Math.sin(time + HALF_PI);
		for (int i = 0; i < LEGS; i++) {
			double sinIn = (i & 1) == 0 ? sin1 : sin2;
			double rot = 10 + ((i + 1) & 1) * 10;
			sinIn *= 10;
			legs_1[i].rotateAngleX = (float) Math.toRadians(rot + sinIn + 10);
			legs_2[i].rotateAngleX = (float) Math.toRadians(180 - rot + sinIn + 5);
			legs_3[i].rotateAngleX = (float) Math.toRadians(sinIn);
		}

		time = System.currentTimeMillis() * 0.0006;
		sin1 = Math.sin(time);
		sin2 = Math.sin(time + Math.PI * 0.5);
		for (int i = 0; i < LEGS; i++) {
			double sinIn = (i & 1) == 0 ? sin1 : sin2;
			wings[i].rotateAngleX = (float) Math.toRadians(sinIn * 20 - 20);
		}
	}
}

