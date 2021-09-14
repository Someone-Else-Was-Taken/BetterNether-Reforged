package itsremurin.betternetherreforged.entity.render;

import itsremurin.betternetherreforged.BetterNether;
import itsremurin.betternetherreforged.entity.EntityJungleSkeleton;
import itsremurin.betternetherreforged.entity.model.ModelJungleSkeleton;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class RenderJungleSkeleton extends MobRenderer<EntityJungleSkeleton, SkeletonModel<EntityJungleSkeleton>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/jungle_skeleton.png");

	public RenderJungleSkeleton(EntityRendererManager renderManager) {
		super(renderManager, new ModelJungleSkeleton(), 0.4F);
		this.addLayer(new BipedArmorLayer<EntityJungleSkeleton, SkeletonModel<EntityJungleSkeleton>, SkeletonModel<EntityJungleSkeleton>>(this, new SkeletonModel<EntityJungleSkeleton>(0.5F, true),
				new SkeletonModel<EntityJungleSkeleton>(1.0F, true)));
		this.addLayer(new HeldItemLayer<EntityJungleSkeleton, SkeletonModel<EntityJungleSkeleton>>(this));
		this.addLayer(new ElytraLayer<EntityJungleSkeleton, SkeletonModel<EntityJungleSkeleton>>(this));
		this.addLayer(new HeadLayer<EntityJungleSkeleton, SkeletonModel<EntityJungleSkeleton>>(this));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityJungleSkeleton entity) {
		return TEXTURE;
	}
}
