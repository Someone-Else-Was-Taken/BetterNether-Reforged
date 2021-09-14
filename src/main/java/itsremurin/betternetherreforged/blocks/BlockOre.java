package itsremurin.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.MathHelper;
import itsremurin.betternetherreforged.MHelper;

public class BlockOre extends OreBlock {
	private final Item dropItem;
	private final int minCount;
	private final int maxCount;

	public BlockOre(Item drop, int minCount, int maxCount) {
		super(AbstractBlock.Properties.create(Material.ROCK)
				.setRequiresTool()
				.hardnessAndResistance(3F, 5F)
				.sound(SoundType.NETHERRACK));
		this.dropItem = drop;
		this.minCount = minCount;
		this.maxCount = maxCount;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool.canHarvestBlock(state)) {
			int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
			if (enchant > 0) {
				return Lists.newArrayList(new ItemStack(this));
			}
			enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
			int min = MathHelper.clamp(minCount + enchant, 0, maxCount);
			if (min == maxCount)
				return Lists.newArrayList(new ItemStack(dropItem, maxCount));
			int count = MHelper.randRange(min, maxCount, MHelper.RANDOM);
			return Lists.newArrayList(new ItemStack(dropItem, count));
		}
		return Lists.newArrayList();
	}
}
