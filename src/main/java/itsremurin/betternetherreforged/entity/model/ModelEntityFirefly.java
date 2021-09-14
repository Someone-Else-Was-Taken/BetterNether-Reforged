package itsremurin.betternetherreforged.entity.model;

import com.google.common.collect.ImmutableList;

import itsremurin.betternetherreforged.entity.EntityFirefly;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelEntityFirefly extends AgeableModel<EntityFirefly> {
	private final ModelRenderer body;
	private final ModelRenderer legs;
	private final ModelRenderer glow;

	public ModelEntityFirefly() {
		textureHeight = 64;
		textureWidth = 32;

		body = new ModelRenderer(this, 0, 0);
		body.addBox(0F, 0F, 0F, 5, 5, 5);
		body.setRotationPoint(-2.5F, 18F, -2.5F);

		legs = new ModelRenderer(this, 0, 22);
		legs.addBox(0F, 0F, 0F, 3F, 3F, 4F);
		legs.setRotationPoint(1.0F, 5F, 0.5F);

		body.addChild(legs);

		glow = new ModelRenderer(this, 0, 10);
		glow.addBox(0F, 0F, 0F, 6F, 6F, 6F);
		glow.setRotationPoint(-0.5F, -0.5F, -0.5F);

		body.addChild(glow);
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
	public void setRotationAngles(EntityFirefly entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

	}
}