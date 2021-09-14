package itsremurin.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;

import itsremurin.betternetherreforged.client.IRenderTypeable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BNPane extends PaneBlock implements IRenderTypeable {
	private boolean dropSelf;

	public BNPane(Block block, boolean dropSelf) {
		super(AbstractBlock.Properties.from(block).hardnessAndResistance(0.3F, 0.3F).notSolid());
		this.dropSelf = dropSelf;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (dropSelf)
			return Collections.singletonList(new ItemStack(this.asItem()));
		else
			return super.getDrops(state, builder);
	}

	@Override
	public BNRenderLayer getRenderLayer() {
		return BNRenderLayer.TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing) {
		if (neighbor.getBlock() == this) {
			if (!facing.getAxis().isHorizontal()) {
				return false;
			}

			if (state.get(FACING_TO_PROPERTY_MAP.get(facing)) && neighbor.get(FACING_TO_PROPERTY_MAP.get(facing.getOpposite()))) {
				return true;
			}
		}

		return super.isSideInvisible(state, neighbor, facing);
	}
}
