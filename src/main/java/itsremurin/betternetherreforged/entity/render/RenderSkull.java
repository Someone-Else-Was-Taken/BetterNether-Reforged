package itsremurin.betternetherreforged.entity.render;

import itsremurin.betternetherreforged.BetterNether;
import itsremurin.betternetherreforged.entity.EntitySkull;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import itsremurin.betternetherreforged.entity.model.ModelSkull;

public class RenderSkull extends MobRenderer<EntitySkull, AgeableModel<EntitySkull>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/skull.png");

	public RenderSkull(EntityRendererManager renderManager) {
		super(renderManager, new ModelSkull(), 0.7F);
		this.addLayer(new GlowFeatureRenderer(this));
	}

	@Override
	public ResourceLocation getEntityTexture(EntitySkull entity) {
		return TEXTURE;
	}

	static class GlowFeatureRenderer extends AbstractEyesLayer<EntitySkull, AgeableModel<EntitySkull>> {
		private static final RenderType SKIN = RenderType.getEntityTranslucent(new ResourceLocation(BetterNether.MOD_ID, "textures/entity/skull_glow.png"));

		public GlowFeatureRenderer(IEntityRenderer<EntitySkull, AgeableModel<EntitySkull>> featureRendererContext) {
			super(featureRendererContext);
		}

		public RenderType getRenderType() {
			return SKIN;
		}
	}

	@Override
	protected int getBlockLight(EntitySkull entity, BlockPos pos) {
		return MathHelper.clamp(super.getBlockLight(entity, pos), 7, 15);
	}
}