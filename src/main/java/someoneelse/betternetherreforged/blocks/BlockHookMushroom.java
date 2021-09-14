package someoneelse.betternetherreforged.blocks;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockHookMushroom extends BlockMold {
	public BlockHookMushroom() {
		super(MaterialBuilder.makeGrass(MaterialColor.PINK)
				.sound(SoundType.CROP)
				.notSolid()
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.tickRandomly()
				.setLightLevel((state) -> {return 13;}));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return BlocksHelper.isNetherrack(world.getBlockState(pos.up()));
	}
}
