package itsremurin.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

public class BNBoneBlock extends BlockBase {
	public BNBoneBlock() {
		super(AbstractBlock.Properties.from(Blocks.BONE_BLOCK));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}
}
