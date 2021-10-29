package someoneelse.betternetherreforged.registry;

import java.util.ArrayList;
import java.util.List;

import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.blocks.shapes.FoodShape;
import someoneelse.betternetherreforged.config.Configs;
import someoneelse.betternetherreforged.items.materials.BNItemMaterials;
import someoneelse.betternetherreforged.tab.CreativeTab;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import someoneelse.betternetherreforged.items.BNArmor;
import someoneelse.betternetherreforged.items.BNItemAxe;
import someoneelse.betternetherreforged.items.BNItemHoe;
import someoneelse.betternetherreforged.items.BNItemPickaxe;
import someoneelse.betternetherreforged.items.BNItemShovel;
import someoneelse.betternetherreforged.items.BNSword;
import someoneelse.betternetherreforged.items.ItemBlackApple;
import someoneelse.betternetherreforged.items.ItemBowlFood;

public class ItemsRegistry {
	private static final List<String> ITEMS = new ArrayList<String>();
	private static final List<Pair<String, Item>> MODITEMS = new ArrayList<>();
	
	public static final ArrayList<Item> MOD_BLOCKS = new ArrayList<Item>();
	public static final ArrayList<Item> MOD_ITEMS = new ArrayList<Item>();

	private static final int ALPHA = 255 << 24;

	
	public static final Item BLACK_APPLE = registerItem("black_apple", new ItemBlackApple());

	public static final Item STALAGNATE_BOWL = registerItem("stalagnate_bowl", new ItemBowlFood(null, FoodShape.NONE));
	public static final Item STALAGNATE_BOWL_WART = registerItem("stalagnate_bowl_wart", new ItemBowlFood(Foods.COOKED_CHICKEN, FoodShape.WART));
	public static final Item STALAGNATE_BOWL_MUSHROOM = registerItem("stalagnate_bowl_mushroom", new ItemBowlFood(Foods.MUSHROOM_STEW, FoodShape.MUSHROOM));
	public static final Item STALAGNATE_BOWL_APPLE = registerItem("stalagnate_bowl_apple", new ItemBowlFood(Foods.APPLE, FoodShape.APPLE));
	public static final Item HOOK_MUSHROOM = registerFood("hook_mushroom_cooked", 4, 0.4F);

	public static final Item CINCINNASITE = registerItem("cincinnasite", new Item(defaultSettings()));
	public static final Item CINCINNASITE_INGOT = registerItem("cincinnasite_ingot", new Item(defaultSettings()));

	public static final Item CINCINNASITE_PICKAXE = registerItem("cincinnasite_pickaxe", new BNItemPickaxe(BNItemMaterials.CINCINNASITE_TOOLS, 512, 1F));
	public static final Item CINCINNASITE_PICKAXE_DIAMOND = registerItem("cincinnasite_pickaxe_diamond", new BNItemPickaxe(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 2048, 1.5F));
	public static final Item CINCINNASITE_AXE = registerItem("cincinnasite_axe", new BNItemAxe(BNItemMaterials.CINCINNASITE_TOOLS, 512, 1F));
	public static final Item CINCINNASITE_AXE_DIAMOND = registerItem("cincinnasite_axe_diamond", new BNItemAxe(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 2048, 1.5F));
	public static final Item CINCINNASITE_SHOVEL = registerItem("cincinnasite_shovel", new BNItemShovel(BNItemMaterials.CINCINNASITE_TOOLS, 512, 1F));
	public static final Item CINCINNASITE_SHOVEL_DIAMOND = registerItem("cincinnasite_shovel_diamond", new BNItemShovel(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 2048, 1.5F));
	public static final Item CINCINNASITE_HOE = registerItem("cincinnasite_hoe", new BNItemHoe(BNItemMaterials.CINCINNASITE_TOOLS, 512, 1F));
	public static final Item CINCINNASITE_HOE_DIAMOND = registerItem("cincinnasite_hoe_diamond", new BNItemHoe(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 2048, 1.5F));
	public static final Item CINCINNASITE_SHEARS = registerItem("cincinnasite_shears", new ShearsItem(defaultSettings().maxDamage(380)));

	public static final Item CINCINNASITE_HELMET = registerItem("cincinnasite_helmet", new BNArmor(BNItemMaterials.CINCINNASITE_ARMOR, EquipmentSlotType.HEAD));
	public static final Item CINCINNASITE_CHESTPLATE = registerItem("cincinnasite_chestplate", new BNArmor(BNItemMaterials.CINCINNASITE_ARMOR, EquipmentSlotType.CHEST));
	public static final Item CINCINNASITE_LEGGINGS = registerItem("cincinnasite_leggings", new BNArmor(BNItemMaterials.CINCINNASITE_ARMOR, EquipmentSlotType.LEGS));
	public static final Item CINCINNASITE_BOOTS = registerItem("cincinnasite_boots", new BNArmor(BNItemMaterials.CINCINNASITE_ARMOR, EquipmentSlotType.FEET));
	public static final Item CINCINNASITE_SWORD = registerItem("cincinnasite_sword", new BNSword(BNItemMaterials.CINCINNASITE_TOOLS, 512, 4, -2.4F));
	public static final Item CINCINNASITE_SWORD_DIAMOND = registerItem("cincinnasite_sword_diamond", new BNSword(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 2048, 5, -2.4F));

	public static final Item NETHER_RUBY = registerItem("nether_ruby", new Item(defaultSettings()));
	public static final Item NETHER_RUBY_PICKAXE = registerItem("nether_ruby_pickaxe", new BNItemPickaxe(BNItemMaterials.NETHER_RUBY_TOOLS, 512, 1F));
	public static final Item NETHER_RUBY_AXE = registerItem("nether_ruby_axe", new BNItemAxe(BNItemMaterials.NETHER_RUBY_TOOLS, 512, 1F));
	public static final Item NETHER_RUBY_SHOVEL = registerItem("nether_ruby_shovel", new BNItemShovel(BNItemMaterials.NETHER_RUBY_TOOLS, 512, 1F));
	public static final Item NETHER_RUBY_HOE = registerItem("nether_ruby_hoe", new BNItemHoe(BNItemMaterials.NETHER_RUBY_TOOLS, 512, 1F));
	public static final Item NETHER_RUBY_SWORD = registerItem("nether_ruby_sword", new BNSword(BNItemMaterials.NETHER_RUBY_TOOLS, 512, 4, -2.4F));
	public static final Item NETHER_RUBY_HELMET = registerItem("nether_ruby_helmet", new BNArmor(BNItemMaterials.NETHER_RUBY_ARMOR, EquipmentSlotType.HEAD));
	public static final Item NETHER_RUBY_CHESTPLATE = registerItem("nether_ruby_chestplate", new BNArmor(BNItemMaterials.NETHER_RUBY_ARMOR, EquipmentSlotType.CHEST));
	public static final Item NETHER_RUBY_LEGGINGS = registerItem("nether_ruby_leggings", new BNArmor(BNItemMaterials.NETHER_RUBY_ARMOR, EquipmentSlotType.LEGS));
	public static final Item NETHER_RUBY_BOOTS = registerItem("nether_ruby_boots", new BNArmor(BNItemMaterials.NETHER_RUBY_ARMOR, EquipmentSlotType.FEET));

	/*
	public static final Item CINCINNASITE_HAMMER = registerItem("cincinnasite_hammer", VanillaHammersIntegration.makeHammer(BNItemMaterials.CINCINNASITE_TOOLS, 4, -2.0F));
	public static final Item CINCINNASITE_HAMMER_DIAMOND = registerItem("cincinnasite_hammer_diamond", VanillaHammersIntegration.makeHammer(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 5, -2.0F));
	public static final Item NETHER_RUBY_HAMMER = registerItem("nether_ruby_hammer", VanillaHammersIntegration.makeHammer(BNItemMaterials.NETHER_RUBY_TOOLS, 5, -2.0F));

	public static final Item CINCINNASITE_EXCAVATOR = registerItem("cincinnasite_excavator", VanillaExcavatorsIntegration.makeExcavator(BNItemMaterials.CINCINNASITE_TOOLS, 4, -2.0F));
	public static final Item CINCINNASITE_EXCAVATOR_DIAMOND = registerItem("cincinnasite_excavator_diamond", VanillaExcavatorsIntegration.makeExcavator(BNItemMaterials.CINCINNASITE_DIAMOND_TOOLS, 5, -2.0F));
	public static final Item NETHER_RUBY_EXCAVATOR = registerItem("nether_ruby_excavator", VanillaExcavatorsIntegration.makeExcavator(BNItemMaterials.NETHER_RUBY_TOOLS, 5, -2.0F));
	*/

	public static final Item SPAWN_EGG_FIREFLY = registerItem("spawn_egg_firefly", makeEgg("firefly", EntityRegistry.FIREFLY, color(255, 223, 168), color(233, 182, 95)));
	public static final Item SPAWN_EGG_JELLYFISH = registerItem("spawn_egg_hydrogen_jellyfish", makeEgg("hydrogen_jellyfish", EntityRegistry.HYDROGEN_JELLYFISH, color(253, 164, 24), color(88, 21, 4)));
	public static final Item SPAWN_NAGA = registerItem("spawn_egg_naga", makeEgg("naga", EntityRegistry.NAGA, MHelper.color(12, 12, 12), MHelper.color(210, 90, 26)));
	public static final Item SPAWN_FLYING_PIG = registerItem("spawn_egg_flying_pig", makeEgg("flying_pig", EntityRegistry.FLYING_PIG, MHelper.color(241, 140, 93), MHelper.color(176, 58, 47)));
	public static final Item SPAWN_JUNGLE_SKELETON = registerItem("spawn_egg_jungle_skeleton", makeEgg("jungle_skeleton", EntityRegistry.JUNGLE_SKELETON, MHelper.color(134, 162, 149), MHelper.color(6, 111, 79)));
	public static final Item SPAWN_SKULL = registerItem("spawn_egg_skull", makeEgg("skull", EntityRegistry.SKULL, MHelper.color(24, 19, 19), MHelper.color(255, 28, 18)));

	public static final Item GLOWSTONE_PILE = registerItem("glowstone_pile", new Item(defaultSettings()));
	public static final Item LAPIS_PILE = registerItem("lapis_pile", new Item(defaultSettings()));

	public static final Item AGAVE_LEAF = registerItem("agave_leaf", new Item(defaultSettings()));
	public static final Item AGAVE_MEDICINE = registerMedicine("agave_medicine", 40, 2, true);
	public static final Item HERBAL_MEDICINE = registerMedicine("herbal_medicine", 10, 5, true);


	public static void registerAll(RegistryEvent.Register<Item> e) {
		IForgeRegistry<Item> r = e.getRegistry();

		for (Pair<String, Item> item : MODITEMS) {
			if(Configs.ITEMS.getBoolean("items", item.getLeft(), true) && item.getRight() != Items.AIR)
			{
				r.register(item.getRight().setRegistryName(new ResourceLocation(BetterNether.MOD_ID, item.getLeft())));

			}
		}
	}

	public static Item registerItem(String name, Item item) {
		MODITEMS.add(Pair.of(name, item));
//		if ((item instanceof BlockItem || Configs.ITEMS.getBoolean("items", name, true)) && item != Items.AIR) {
//			DEFERRED.register(name, () -> item);
		if (item instanceof BlockItem)
			MOD_BLOCKS.add(item);
		else
			MOD_ITEMS.add(item);
//		}
		if (!(item instanceof BlockItem))
			ITEMS.add(name);
		return item;
	}

	public static Item registerFood(String name, int hunger, float saturationMultiplier) {
		return registerItem(name, new Item(defaultSettings().food(new Food.Builder().hunger(hunger).saturation(saturationMultiplier).build())));
	}

	public static Item registerMedicine(String name, int ticks, int power, boolean bowl) {
		if (bowl) {
			Item item = new Item(defaultSettings().food(new Food.Builder().effect(() -> new EffectInstance(Effects.REGENERATION, ticks, power), 1).build())) {
				@Override
				public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity user) {
					if (stack.getCount() == 1) {
						super.onItemUseFinish(stack, world, user);
						return new ItemStack(ItemsRegistry.STALAGNATE_BOWL, stack.getCount());
					}
					else {
						if (user instanceof PlayerEntity) {
							PlayerEntity player = (PlayerEntity) user;
							if (!player.isCreative())
								player.addItemStackToInventory(new ItemStack(ItemsRegistry.STALAGNATE_BOWL));
						}
						return super.onItemUseFinish(stack, world, user);
					}
				}
			};
			return registerItem(name, item);
		}
		return registerItem(name, new Item(defaultSettings().food(new Food.Builder().effect(() -> new EffectInstance(Effects.REGENERATION, ticks, power), 1).build())));
	}


	public static Item.Properties defaultSettings() {
		return new Item.Properties().group(CreativeTab.BN_TAB);
	}

	private static Item makeEgg(String name, EntityType<?> type, int background, int dots) {
		if (Configs.MOBS.getBoolean("mobs", name, true)) {
			SpawnEggItem item = new SpawnEggItem(type, background, dots, defaultSettings());
			DefaultDispenseItemBehavior behavior = new DefaultDispenseItemBehavior() {
				public ItemStack dispenseSilently(IBlockSource pointer, ItemStack stack) {
					Direction direction = (Direction) pointer.getBlockState().get(DispenserBlock.FACING);
					EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
					entityType.spawn(pointer.getWorld(), stack, (PlayerEntity) null, pointer.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
					stack.shrink(1);
					return stack;
				}
			};
			DispenserBlock.registerDispenseBehavior(item, behavior);
			return item;
		}
		else {
			return Items.AIR;
		}
	}

	private static int color(int r, int g, int b) {
		return ALPHA | (r << 16) | (g << 8) | b;
	}

	public static List<String> getPossibleItems() {
		return ITEMS;
	}
}
