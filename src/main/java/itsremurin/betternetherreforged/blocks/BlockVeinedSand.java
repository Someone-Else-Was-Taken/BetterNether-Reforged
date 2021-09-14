package itsremurin.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockVeinedSand extends BlockBase {
	public BlockVeinedSand() {
		super(AbstractBlock.Properties.create(Material.SAND, MaterialColor.BROWN)
				.sound(SoundType.SAND)
				.hardnessAndResistance(0.5F, 0.5F));
		this.setDropItself(false);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (world.getBlockState(pos.up()).getBlock() == BlocksRegistry.SOUL_VEIN)
			return state;
		else
			return Blocks.SOUL_SAND.getDefaultState();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(Blocks.SOUL_SAND);
	}
}