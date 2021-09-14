package itsremurin.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockObsidianGlass extends BlockBaseNotFull {
	public BlockObsidianGlass() {
		super(AbstractBlock.Properties.from(Blocks.OBSIDIAN)
				.notSolid()
				.setSuffocates((arg1, arg2, arg3) -> {
					return false;
				})
				.setBlocksVision((arg1, arg2, arg3) -> {
					return false;
				}));
		this.setRenderLayer(BNRenderLayer.TRANSLUCENT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader view, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader view, BlockPos pos) {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing) {
		return neighbor.getBlock() == this ? true : super.isSideInvisible(state, neighbor, facing);
	}
}
