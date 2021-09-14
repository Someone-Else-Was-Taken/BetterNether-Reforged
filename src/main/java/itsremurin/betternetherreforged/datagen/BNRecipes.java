package itsremurin.betternetherreforged.datagen;

import java.util.ArrayList;
import java.util.function.Consumer;

import itsremurin.betternetherreforged.BetterNether;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BNRecipes extends RecipeProvider {

	public static final ArrayList<SingleInputRecipe> STAIRS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> SLABS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> ROOFS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> PLATES = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> TWOBYTWO = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> FENCES = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> GATES = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> DOORS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> TRAPDOORS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> WALLS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> ROUND = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> SIGNS = new ArrayList<>();
	public static final ArrayList<TwoInputRecipe> BARRELS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> LADDERS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> TABURETS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> CHAIRS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> BAR_STOOLS = new ArrayList<>();
	public static final ArrayList<FireBowlRecipe> FIRE_BOWLS = new ArrayList<>();
	public static final ArrayList<SingleInputRecipe> SHAPELESS = new ArrayList<>();
	
    public BNRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        for (SingleInputRecipe recipe : STAIRS) {
        	makeStairsRecipe(recipe.input, recipe.output, consumer);
        }
        
        for (SingleInputRecipe recipe : SLABS) {
        	makeSlabRecipe(recipe.input, recipe.output, consumer);
        }
        
        for (SingleInputRecipe recipe : ROOFS) {
        	makeRoofRecipe(recipe.input, recipe.output, consumer);
        }
        
        for (SingleInputRecipe recipe : PLATES) {
        	makePlateRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : TWOBYTWO) {
        	make2x2Recipe(recipe.input, recipe.output, recipe.quantityOut, consumer);
        }

        for (SingleInputRecipe recipe : FENCES) {
        	makeFenceRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : GATES) {
        	makeGateRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : DOORS) {
        	makeDoorRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : TRAPDOORS) {
        	makeTrapdoorRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : WALLS) {
        	makeWallRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : ROUND) {
        	makeRoundRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : SIGNS) {
        	makeSignRecipe(recipe.input, recipe.output, consumer);
        }

        for (TwoInputRecipe recipe : BARRELS) {
        	makeBarrelRecipe(recipe.input1, recipe.input2, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : LADDERS) {
        	makeLadderRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : TABURETS) {
        	makeTaburetRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : CHAIRS) {
        	makeChairRecipe(recipe.input, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : BAR_STOOLS) {
        	makeBarStoolRecipe(recipe.input, recipe.output, consumer);
        }

        for (FireBowlRecipe recipe : FIRE_BOWLS) {
        	makeFireBowlRecipe(recipe.source, recipe.inside, recipe.legs, recipe.output, consumer);
        }

        for (SingleInputRecipe recipe : SHAPELESS) {
        	makeShapelessSingleInputRecipe(recipe.input, recipe.output, recipe.quantityOut, consumer);
        }
    }
    
    // SHAPED RECIPES //
    
    public static void makeStairsRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 4)
    			.patternLine("#  ")
    			.patternLine("## ")
    			.patternLine("###")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeSlabRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 6)
    			.patternLine("###")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeRoofRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 6)
    			.patternLine("# #")
    			.patternLine("###")
    			.patternLine(" # ")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makePlateRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("##")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void make2x2Recipe(ResourceLocation input, ResourceLocation output, int outputQuantity, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, outputQuantity)
    			.patternLine("##")
    			.patternLine("##")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeFenceRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 3)
    			.patternLine("#I#")
    			.patternLine("#I#")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeGateRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("I#I")
    			.patternLine("I#I")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeDoorRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 3)
    			.patternLine("##")
    			.patternLine("##")
    			.patternLine("##")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeTrapdoorRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 2)
    			.patternLine("###")
    			.patternLine("###")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeWallRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result, 6)
    			.patternLine("###")
    			.patternLine("###")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeRoundRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("###")
    			.patternLine("# #")
    			.patternLine("###")
    			.key('#', ingredient)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeSignRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("###")
    			.patternLine("###")
    			.patternLine(" I ")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeBarrelRecipe(ResourceLocation input1, ResourceLocation input2, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider block = getRegisteredItem(input1);
    	IItemProvider slab = getRegisteredItem(input2);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("#S#")
    			.patternLine("# #")
    			.patternLine("#S#")
    			.key('#', block)
    			.key('S', slab)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(block), hasItem(block))
    			.build(consumer, getPath(result) + "_from_" + getPath(block));
    }
    
    public static void makeLadderRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("I I")
    			.patternLine("I#I")
    			.patternLine("I I")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeTaburetRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("##")
    			.patternLine("II")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeChairRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("I ")
    			.patternLine("##")
    			.patternLine("II")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeBarStoolRecipe(ResourceLocation input, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("##")
    			.patternLine("II")
    			.patternLine("II")
    			.key('#', ingredient)
    			.key('I', Items.STICK)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    public static void makeFireBowlRecipe(ResourceLocation input1, ResourceLocation input2, ResourceLocation input3, ResourceLocation output, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider block = getRegisteredItem(input1);
    	IItemProvider leg = getRegisteredItem(input2);
    	IItemProvider inside = getRegisteredItem(input3);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapedRecipeBuilder.shapedRecipe(result)
    			.patternLine("#I#")
    			.patternLine(" # ")
    			.patternLine("L L")
    			.key('#', block)
    			.key('I', inside)
    			.key('L', leg)
    			.setGroup(BetterNether.MOD_ID)
    			.addCriterion(getPath(block), hasItem(block))
    			.build(consumer, getPath(result) + "_from_" + getPath(block));
    }
    
    // SHAPELESS RECIPES //
    
    public static void makeShapelessSingleInputRecipe(ResourceLocation input, ResourceLocation output, int outputQuantity, Consumer<IFinishedRecipe> consumer) {
    	IItemProvider ingredient = getRegisteredItem(input);
    	IItemProvider result = getRegisteredItem(output);
    	
    	ShapelessRecipeBuilder.shapelessRecipe(result, outputQuantity)
        		.addIngredient(ingredient)
        		.setGroup(BetterNether.MOD_ID)
        		.addCriterion(getPath(ingredient), hasItem(ingredient))
    			.build(consumer, getPath(result) + "_from_" + getPath(ingredient));
    }
    
    private static String getPath(IItemProvider item) {
    	return item.asItem().getRegistryName().getPath();
    }
    
    private static IItemProvider getRegisteredItem(ResourceLocation loc) {
    	return ForgeRegistries.ITEMS.getValue(loc);
    }
    
    public static class SingleInputRecipe {
    	public final ResourceLocation input;
    	public final ResourceLocation output;
    	public int quantityOut = 1;
    	
    	public SingleInputRecipe(Block input, Block output) {
    		this.input = input.getRegistryName();
    		this.output = output.getRegistryName();
    	}
    	
    	public SingleInputRecipe(Block input, Block output, int quantityOut) {
    		this.input = input.getRegistryName();
    		this.output = output.getRegistryName();
    		this.quantityOut = quantityOut;
    	}
    }
    
    public static class TwoInputRecipe {
    	public final ResourceLocation input1;
    	public final ResourceLocation input2;
    	public final ResourceLocation output;
    	
    	public TwoInputRecipe(Block input1, Block input2, Block output) {
    		this.input1 = input1.getRegistryName();
    		this.input2 = input2.getRegistryName();
    		this.output = output.getRegistryName();
    	}
    }
    
    public static class FireBowlRecipe {
    	public final ResourceLocation source;
    	public final ResourceLocation inside;
    	public final ResourceLocation legs;
    	public final ResourceLocation output;
    	
    	public FireBowlRecipe(Block input1, Block input2, Item input3, Block output) {
    		this.source = input1.getRegistryName();
    		this.inside = input2.getRegistryName();
    		this.legs = input3.getRegistryName();
    		this.output = output.getRegistryName();
    	}
    }
}