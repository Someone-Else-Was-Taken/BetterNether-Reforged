package someoneelse.betternetherreforged.tileentities;

import net.minecraft.tileentity.ChestTileEntity;
import someoneelse.betternetherreforged.registry.TileEntitiesRegistry;

public class BNChestTileEntity extends ChestTileEntity {
	public BNChestTileEntity() {
		super(TileEntitiesRegistry.CHEST);
	}
}
