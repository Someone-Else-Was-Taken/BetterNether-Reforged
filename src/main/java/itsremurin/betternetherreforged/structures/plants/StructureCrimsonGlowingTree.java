package itsremurin.betternetherreforged.structures.plants;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import itsremurin.betternetherreforged.structures.StructureObjScatter;
import itsremurin.betternetherreforged.structures.StructureType;
import itsremurin.betternetherreforged.structures.StructureWorld;

public class StructureCrimsonGlowingTree extends StructureObjScatter {
	private static final StructureWorld[] TREES = new StructureWorld[] {
			new StructureWorld("trees/crimson_glow_tree_01", -1, StructureType.FLOOR),
			new StructureWorld("trees/crimson_glow_tree_02", -1, StructureType.FLOOR),
			new StructureWorld("trees/crimson_glow_tree_03", -1, StructureType.FLOOR),
			new StructureWorld("trees/crimson_glow_tree_04", -1, StructureType.FLOOR)
	};

	public StructureCrimsonGlowingTree() {
		super(7, TREES);
	}

	protected boolean isGround(BlockState state) {
		return BlocksHelper.isNetherGround(state);
	}

	protected boolean isStructure(BlockState state) {
		return state.getBlock() == Blocks.CRIMSON_STEM;
	}
}