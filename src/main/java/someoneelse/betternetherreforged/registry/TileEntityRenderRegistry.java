package someoneelse.betternetherreforged.registry;

import someoneelse.betternetherreforged.tileentities.render.BNChestTileEntityRenderer;
import someoneelse.betternetherreforged.tileentities.render.BNSignTileEntityRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TileEntityRenderRegistry {
	public static void register() {
		ClientRegistry.bindTileEntityRenderer(TileEntitiesRegistry.CHEST, BNChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntitiesRegistry.SIGN, BNSignTileEntityRenderer::new);
	}
}
