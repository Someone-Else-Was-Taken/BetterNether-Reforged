package someoneelse.betternetherreforged.blocks;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.material.MaterialColor;

public class BlockFarmland extends BlockBase {
	public BlockFarmland() {
		super(MaterialBuilder.makeWood(MaterialColor.LIME_TERRACOTTA));
	}
}