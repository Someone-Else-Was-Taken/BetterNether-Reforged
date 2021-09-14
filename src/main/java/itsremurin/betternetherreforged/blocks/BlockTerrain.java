package itsremurin.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.SoundEvents;

public class BlockTerrain extends BlockBase {
	public static final SoundType TERRAIN_SOUND = new SoundType(1.0F, 1.0F,
			SoundEvents.BLOCK_NETHERRACK_BREAK,
			SoundEvents.BLOCK_WART_BLOCK_STEP,
			SoundEvents.BLOCK_NETHERRACK_PLACE,
			SoundEvents.BLOCK_NETHERRACK_HIT,
			SoundEvents.BLOCK_NETHERRACK_FALL);

	public BlockTerrain() {
		super(AbstractBlock.Properties.from(Blocks.NETHERRACK).sound(TERRAIN_SOUND).setRequiresTool());
		this.setDropItself(false);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool.canHarvestBlock(state)) {
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
				return Collections.singletonList(new ItemStack(this.asItem()));
			else
				return Collections.singletonList(new ItemStack(Blocks.NETHERRACK));
		}
		else
			return super.getDrops(state, builder);
	}
}
