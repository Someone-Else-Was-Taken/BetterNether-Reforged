package itsremurin.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import itsremurin.betternetherreforged.registry.BlocksRegistry;


public class BlockCincinnasiteAnvil extends AnvilBlock {
	public BlockCincinnasiteAnvil() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK).notSolid());
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool != null && tool.canHarvestBlock(state)) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList();
		}
	}
}