package itsremurin.betternetherreforged.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import itsremurin.betternetherreforged.BetterNether;
import itsremurin.betternetherreforged.entity.EntityHydrogenJellyfish;
import itsremurin.betternetherreforged.entity.model.ModelEntityHydrogenJellyfish;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RenderHydrogenJellyfish extends MobRenderer<EntityHydrogenJellyfish, AgeableModel<EntityHydrogenJellyfish>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/jellyfish.png");

	public RenderHydrogenJellyfish(EntityRendererManager renderManager) {
		super(renderManager, new ModelEntityHydrogenJellyfish(), 1);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityHydrogenJellyfish entity) {
		return TEXTURE;
	}

	@Override
	protected int getBlockLight(EntityHydrogenJellyfish entity, BlockPos pos) {
		return 15;
	}

	@Override
	protected void preRenderCallback(EntityHydrogenJellyfish entity, MatrixStack matrixStack, float f) {
		float scale = entity.getScale();
		matrixStack.scale(scale, scale, scale);
	}
}
