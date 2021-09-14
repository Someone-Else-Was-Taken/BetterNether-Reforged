package itsremurin.betternetherreforged.blocks;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.material.MaterialColor;

public class BNPlanks extends BlockBase {
	public BNPlanks(MaterialColor color) {
		super(MaterialBuilder.makeWood(color));
	}
}
