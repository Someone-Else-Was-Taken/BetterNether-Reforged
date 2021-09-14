package itsremurin.betternetherreforged.blocks;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.material.MaterialColor;

public class BlockReedsBlock extends BNPillar {
	public BlockReedsBlock() {
		super(MaterialBuilder.makeWood(MaterialColor.CYAN));
	}
}