package itsremurin.betternetherreforged.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;

import itsremurin.betternetherreforged.BetterNether;
import itsremurin.betternetherreforged.config.Configs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;


public class BNRecipeManager {
	private static final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> RECIPES = Maps.newHashMap();

	public static void addRecipe(IRecipeType<?> type, IRecipe<?> recipe) {
		if (Configs.RECIPES.getBoolean("recipes", recipe.getId().getPath(), true)) {
			Map<ResourceLocation, IRecipe<?>> list = RECIPES.get(type);
			if (list == null) {
				list = Maps.newHashMap();
				RECIPES.put(type, list);
			}
			list.put(recipe.getId(), recipe);
		}
	}

	public static Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> getMap(Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes) {
		Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> result = Maps.newHashMap();

		for (IRecipeType<?> type : recipes.keySet()) {
			Map<ResourceLocation, IRecipe<?>> typeList = Maps.newHashMap();
			typeList.putAll(recipes.get(type));
			result.put(type, typeList);
		}

		for (IRecipeType<?> type : RECIPES.keySet()) {
			Map<ResourceLocation, IRecipe<?>> list = RECIPES.get(type);
			if (list != null) {
				Map<ResourceLocation, IRecipe<?>> typeList = result.get(type);
				list.forEach((id, recipe) -> {
					if (!typeList.containsKey(id))
						typeList.put(id, recipe);
				});
			}
		}

		return result;
	}

	public static NonNullList<Ingredient> getIngredients(String[] pattern, Map<String, Ingredient> key, int width, int height) {
		NonNullList<Ingredient> defaultedList = NonNullList.withSize(width * height, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(key.keySet());
		set.remove(" ");

		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String string = pattern[i].substring(j, j + 1);
				Ingredient ingredient = (Ingredient) key.get(string);
				if (ingredient == null) {
					throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
				}

				set.remove(string);
				defaultedList.set(j + width * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		}
		else {
			return defaultedList;
		}
	}

	public static void addCraftingRecipe(String name, String[] shape, Map<String, ItemStack> materials, ItemStack result) {
		addCraftingRecipe(name, "", shape, materials, result);
	}

	public static void addCraftingRecipe(String name, String group, String[] shape, Map<String, ItemStack> materials, ItemStack result) {
		int width = shape[0].length();
		int height = shape.length;

		Map<String, Ingredient> mapIng = new HashMap<String, Ingredient>();
		mapIng.put(" ", Ingredient.EMPTY);
		materials.forEach((id, material) -> {
			mapIng.put(id, fromStacks(material));
		});

		NonNullList<Ingredient> list = BNRecipeManager.getIngredients(shape, mapIng, width, height);
		ShapedRecipe recipe = new ShapedRecipe(new ResourceLocation(BetterNether.MOD_ID, name), group, width, height, list, result);
		BNRecipeManager.addRecipe(IRecipeType.CRAFTING, recipe);
	}

	private static Ingredient fromStacks(ItemStack... stacks) {
		return Ingredient.fromStacks(Arrays.stream(stacks));
	}

	public static ShapelessRecipe makeEmtyRecipe(ResourceLocation id) {
		ShapelessRecipe recipe = new ShapelessRecipe(id, "empty", new ItemStack(Items.AIR), NonNullList.create());
		return recipe;
	}
}