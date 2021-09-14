package someoneelse.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;

import someoneelse.betternetherreforged.client.IRenderTypeable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChainBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

public class BNChain extends ChainBlock implements IRenderTypeable {
	public BNChain() {
		super(AbstractBlock.Properties.from(Blocks.CHAIN));
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