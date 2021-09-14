package itsremurin.betternetherreforged.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockInkBush extends BlockCommonPlant {
	public BlockInkBush() {
		super(MaterialColor.BLACK);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.INK_BUSH_SEED);
	}
}
