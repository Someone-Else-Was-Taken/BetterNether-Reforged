package itsremurin.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;

import itsremurin.betternetherreforged.client.IRenderTypeable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;


public class BNCraftingTable extends CraftingTableBlock implements IRenderTypeable {
	public BNCraftingTable(Block block) {
		super(AbstractBlock.Properties.from(block));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}

	@Override
	public BNRenderLayer getRenderLayer() {
		return BNRenderLayer.CUTOUT;
	}
}
