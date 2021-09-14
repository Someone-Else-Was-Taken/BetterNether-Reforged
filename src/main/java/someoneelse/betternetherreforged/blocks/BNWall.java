package someoneelse.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

public class BNWall extends WallBlock {
	public BNWall(Block block) {
		super(AbstractBlock.Properties.from(block).notSolid());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this.asItem()));
	}
}
