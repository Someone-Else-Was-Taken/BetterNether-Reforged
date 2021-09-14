package someoneelse.betternetherreforged.registry;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import someoneelse.betternetherreforged.entity.render.*;

public class EntityRenderRegistry {
	public static void register() {
		// Firefly is "bugged", something in the renderer doesn't allow it to register properly
		//RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FIREFLY, RenderFirefly::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.CHAIR, RenderChair::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.HYDROGEN_JELLYFISH, RenderHydrogenJellyfish::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.NAGA, RenderNaga::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.NAGA_PROJECTILE, RenderNagaProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.FLYING_PIG, RenderFlyingPig::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.JUNGLE_SKELETON, RenderJungleSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SKULL, RenderSkull::new);
	}
}
