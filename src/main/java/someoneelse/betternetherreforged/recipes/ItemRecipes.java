package someoneelse.betternetherreforged.recipes;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.registry.ItemsRegistry;

public class ItemRecipes {
	public static void register() {
		if (itemExists(ItemsRegistry.GLOWSTONE_PILE)) {
			String[] shape = new String[] { "###", "###", "###" };
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(ItemsRegistry.GLOWSTONE_PILE));
			BNRecipeManager.addCraftingRecipe("bn_glowstone_dust", "", shape, materials, new ItemStack(Items.GLOWSTONE_DUST));
		}

		if (itemExists(ItemsRegistry.CINCINNASITE_INGOT) && blockExists(BlocksRegistry.CINCINNASITE_CHAIN)) {
			String[] shape = new String[] { "#", "#", "#" };
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(ItemsRegistry.CINCINNASITE_INGOT));
			BNRecipeManager.addCraftingRecipe("cincinnasite_chains", "", shape, materials, new ItemStack(BlocksRegistry.CINCINNASITE_CHAIN, 3));
		}

		if (itemExists(ItemsRegistry.LAPIS_PILE)) {
			String[] shape = new String[] { "###", "###", "###" };
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(ItemsRegistry.LAPIS_PILE));
			BNRecipeManager.addCraftingRecipe("glowstone_pile_to_dust", "", shape, materials, new ItemStack(Items.LAPIS_LAZULI));
		}

		if (blockExists(BlocksRegistry.BLOOMING_VINE)) {
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(BlocksRegistry.BLOOMING_VINE));
			BNRecipeManager.addCraftingRecipe("bn_yellow_dye", "", new String[] { "#" }, materials, new ItemStack(Items.YELLOW_DYE, 2));
		}

		if (blockExists(BlocksRegistry.GOLDEN_VINE) && itemExists(ItemsRegistry.GLOWSTONE_PILE)) {
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(BlocksRegistry.GOLDEN_VINE));
			BNRecipeManager.addCraftingRecipe("bn_golden_vine", "", new String[] { "#" }, materials, new ItemStack(ItemsRegistry.GLOWSTONE_PILE, 2));
		}

		if (blockExists(BlocksRegistry.WALL_MUSHROOM_BROWN)) {
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(BlocksRegistry.WALL_MUSHROOM_BROWN));
			BNRecipeManager.addCraftingRecipe("wall_mushroom_brown_1", "wall_mushroom", new String[] { "#" }, materials, new ItemStack(Items.BROWN_MUSHROOM));

			materials = ImmutableMap.of("#", new ItemStack(Items.BROWN_MUSHROOM));
			BNRecipeManager.addCraftingRecipe("wall_mushroom_brown_2", "wall_mushroom", new String[] { "#" }, materials, new ItemStack(BlocksRegistry.WALL_MUSHROOM_BROWN));
		}

		if (blockExists(BlocksRegistry.WALL_MUSHROOM_RED)) {
			ImmutableMap<String, ItemStack> materials = ImmutableMap.of("#", new ItemStack(BlocksRegistry.WALL_MUSHROOM_RED));
			BNRecipeManager.addCraftingRecipe("wall_mushroom_red_1", "wall_mushroom", new String[] { "#" }, materials, new ItemStack(Items.RED_MUSHROOM));

			materials = ImmutableMap.of("#", new ItemStack(Items.RED_MUSHROOM));
			BNRecipeManager.addCraftingRecipe("wall_mushroom_red_2", "wall_mushroom", new String[] { "#" }, materials, new ItemStack(BlocksRegistry.WALL_MUSHROOM_RED));
		}
	}

	private static boolean itemExists(Item item) {
		return ForgeRegistries.ITEMS.getKey(item) != ForgeRegistries.ITEMS.getDefaultKey();
	}

	private static boolean blockExists(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block) != ForgeRegistries.BLOCKS.getDefaultKey();
	}
}
