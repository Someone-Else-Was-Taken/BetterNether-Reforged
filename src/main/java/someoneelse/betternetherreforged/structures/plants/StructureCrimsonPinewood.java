package someoneelse.betternetherreforged.structures.plants;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.structures.StructureObjScatter;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.StructureWorld;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class StructureCrimsonPinewood extends StructureObjScatter {
	private static final StructureWorld[] TREES = new StructureWorld[] {
			new StructureWorld("trees/crimson_pine_01", -2, StructureType.FLOOR),
			new StructureWorld("trees/crimson_pine_02", -2, StructureType.FLOOR),
			new StructureWorld("trees/crimson_pine_03", -2, StructureType.FLOOR),
			new StructureWorld("trees/crimson_pine_04", -1, StructureType.FLOOR),
			new StructureWorld("trees/crimson_pine_05", -1, StructureType.FLOOR)
	};

	public StructureCrimsonPinewood() {
		super(7, TREES);
	}

	protected boolean isGround(BlockState state) {
		return BlocksHelper.isNetherGround(state);
	}

	protected boolean isStructure(BlockState state) {
		return state.getBlock() == Blocks.CRIMSON_STEM;
	}
}
