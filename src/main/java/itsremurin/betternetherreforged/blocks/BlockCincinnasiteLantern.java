package itsremurin.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockCincinnasiteLantern extends BlockBase {
	public BlockCincinnasiteLantern() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK).setLightLevel((state) -> {return 15;}));
	}
}
