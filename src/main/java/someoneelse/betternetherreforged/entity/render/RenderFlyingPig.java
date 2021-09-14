package someoneelse.betternetherreforged.entity.render;

import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.entity.EntityFlyingPig;
import someoneelse.betternetherreforged.entity.model.ModelEntityFlyingPig;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.util.ResourceLocation;

public class RenderFlyingPig extends MobRenderer<EntityFlyingPig, AgeableModel<EntityFlyingPig>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/flying_pig.png");
	private static final ResourceLocation TEXTURE_WARTED = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/flying_pig_warted.png");

	public RenderFlyingPig(EntityRendererManager renderManager) {
		super(renderManager, new ModelEntityFlyingPig(), 0.6F);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityFlyingPig entity) {
		return entity.isWarted() ? TEXTURE_WARTED : TEXTURE;
	}
}
