package itsremurin.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HugeMushroomBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraftforge.common.ToolType;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.registry.ItemsRegistry;

public class BlockGiantLucis extends HugeMushroomBlock {
	public BlockGiantLucis() {
		super(AbstractBlock.Properties.create(Material.ORGANIC, MaterialColor.YELLOW)
				.harvestTool(ToolType.AXE)
				.sound(SoundType.WOOD)
				.hardnessAndResistance(1F)
				.setLightLevel((state) -> {return 15;})
				.notSolid());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) return Lists.newArrayList(new ItemStack(this.asItem()));
		return Lists.newArrayList(new ItemStack(BlocksRegistry.LUCIS_SPORE, MHelper.randRange(0, 1, MHelper.RANDOM)),
				new ItemStack(ItemsRegistry.GLOWSTONE_PILE, MHelper.randRange(0, 2, MHelper.RANDOM)));
	}
}
