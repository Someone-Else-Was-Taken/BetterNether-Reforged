package itsremurin.betternetherreforged.structures.decorations;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.StructureObjScatter;
import itsremurin.betternetherreforged.structures.StructureType;
import itsremurin.betternetherreforged.structures.StructureWorld;

public class StructureForestLitter extends StructureObjScatter {
	private static final StructureWorld[] STRUCTURES = new StructureWorld[] {
			new StructureWorld("upside_down_forest/tree_fallen", 0, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_needle", 0, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_root", -2, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_stump", -2, StructureType.FLOOR),
			new StructureWorld("upside_down_forest/tree_small_branch", 0, StructureType.FLOOR)
	};

	public StructureForestLitter() {
		super(8, STRUCTURES);
	}

	@Override
	protected boolean isStructure(BlockState state) {
		return BlocksRegistry.ANCHOR_TREE.isTreeLog(state.getBlock());
	}

	@Override
	protected boolean isGround(BlockState state) {
		return BlocksHelper.isNetherGround(state);
	}
}