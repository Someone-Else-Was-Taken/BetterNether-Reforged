package someoneelse.betternetherreforged.tileentities;

import java.util.Arrays;
import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.BrewingStandContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import someoneelse.betternetherreforged.registry.BrewingRegistry;
import someoneelse.betternetherreforged.registry.TileEntitiesRegistry;

public class BNBrewingStandTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private static final int[] TOP_SLOTS = new int[] { 3 };
    private static final int[] BOTTOM_SLOTS = new int[] { 0, 1, 2, 3 };
    private static final int[] SIDE_SLOTS = new int[] { 0, 1, 2, 4 };
    private NonNullList<ItemStack> inventory;
    private int brewTime;
    private boolean[] slotsEmptyLastTick;
    private Item itemBrewing;
    private int fuel;
    protected final IIntArray propertyDelegate;

    public BNBrewingStandTileEntity() {
        super(TileEntitiesRegistry.NETHER_BREWING_STAND);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.propertyDelegate = new IIntArray() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return BNBrewingStandTileEntity.this.brewTime;
                    case 1:
                        return BNBrewingStandTileEntity.this.fuel;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        BNBrewingStandTileEntity.this.brewTime = value;
                        break;
                    case 1:
                        BNBrewingStandTileEntity.this.fuel = value;
                }

            }

            public int size() {
                return 2;
            }
        };
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.brewing", new Object[0]);
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack) var1.next();
        }
        while (itemStack.isEmpty());

        return false;
    }

    @Override
    public void tick() {
        ItemStack itemStack = (ItemStack) this.inventory.get(4);
        if (this.fuel <= 0 && itemStack.getItem() == Items.BLAZE_POWDER) {
            this.fuel = 20;
            itemStack.shrink(1);
            this.markDirty();
        }

        boolean bl = this.canCraft();
        boolean bl2 = this.brewTime > 0;
        ItemStack itemStack2 = (ItemStack) this.inventory.get(3);
        if (bl2) {
            --this.brewTime;
            boolean bl3 = this.brewTime == 0;
            if (bl3 && bl) {
                this.craft();
                this.markDirty();
            }
            else if (!bl) {
                this.brewTime = 0;
                this.markDirty();
            }
            else if (this.itemBrewing != itemStack2.getItem()) {
                this.brewTime = 0;
                this.markDirty();
            }
        }
        else if (bl && this.fuel > 0) {
            --this.fuel;
            this.brewTime = 400;
            this.itemBrewing = itemStack2.getItem();
            this.markDirty();
        }

        if (!this.world.isRemote) {
            boolean[] bls = this.getSlotsEmpty();
            if (!Arrays.equals(bls, this.slotsEmptyLastTick)) {
                this.slotsEmptyLastTick = bls;
                BlockState blockState = this.world.getBlockState(this.getPos());
                if (!(blockState.getBlock() instanceof BrewingStandBlock)) {
                    return;
                }

                for (int i = 0; i < BrewingStandBlock.HAS_BOTTLE.length; ++i) {
                    blockState = (BlockState) blockState.with(BrewingStandBlock.HAS_BOTTLE[i], bls[i]);
                }

                this.world.setBlockState(this.pos, blockState, 2);
            }
        }

    }

    public boolean[] getSlotsEmpty() {
        boolean[] bls = new boolean[3];

        for (int i = 0; i < 3; ++i) {
            if (!((ItemStack) this.inventory.get(i)).isEmpty()) {
                bls[i] = true;
            }
        }

        return bls;
    }

    private boolean canCraft() {
        ItemStack source = this.inventory.get(3);
        if (source.isEmpty()) {
            return false;
        }
        else if (!BrewingRecipeRegistry.isValidIngredient(source)) {
            return false;
        }
        else {
            for (int i = 0; i < 3; ++i) {
                ItemStack bottle = this.inventory.get(i);
                if (!bottle.isEmpty()) {
                    if (BrewingRecipeRegistry.hasOutput(bottle, source))
                        return true;
                    else if (BrewingRegistry.getResult(source, bottle) != null)
                        return true;
                }
            }

            return false;
        }
    }

    private void craft() {
        ItemStack source = (ItemStack) this.inventory.get(3);

        for (int i = 0; i < 3; ++i) {
            ItemStack bottle = this.inventory.get(i);
            if (!bottle.isEmpty()) {
                ItemStack result = BrewingRegistry.getResult(source, bottle);
                if (result != null)
                    this.inventory.set(i, result.copy());
                else
                    this.inventory.set(i, PotionBrewing.doReaction(source, this.inventory.get(i)));
            }
        }

        source.shrink(1);
        BlockPos blockPos = this.getPos();
        if (source.hasContainerItem()) {
            ItemStack itemStack2 = new ItemStack(source.getContainerItem().getItem());
            if (source.isEmpty()) {
                source = itemStack2;
            }
            else if (!this.world.isRemote) {
                InventoryHelper.spawnItemStack(this.world, (double) blockPos.getX(), (double) blockPos.getY(),
                        (double) blockPos.getZ(), itemStack2);
            }
        }

        this.inventory.set(3, source);
        this.world.playEvent(1035, blockPos, 0);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, this.inventory);
        this.brewTime = tag.getShort("BrewTime");
        this.fuel = tag.getByte("Fuel");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putShort("BrewTime", (short) this.brewTime);
        ItemStackHelper.saveAllItems(tag, this.inventory);
        tag.putByte("Fuel", (byte) this.fuel);
        return tag;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? (ItemStack) this.inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return ItemStackHelper.getAndSplit(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return ItemStackHelper.getAndRemove(this.inventory, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }
        else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                    (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 3) {
            return BrewingRecipeRegistry.isValidIngredient(stack);
        }
        else {
            Item item = stack.getItem();
            if (slot == 4) {
                return item == Items.BLAZE_POWDER;
            }
            else {
                return (item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION
                        || item == Items.GLASS_BOTTLE) && this.getStackInSlot(slot).isEmpty();
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        else {
            return side == Direction.DOWN ? BOTTOM_SLOTS : SIDE_SLOTS;
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, Direction dir) {
        if (slot == 3) {
            return stack.getItem() == Items.GLASS_BOTTLE;
        }
        else {
            return true;
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    protected Container createMenu(int syncId, PlayerInventory playerInventory) {
        return new BrewingStandContainer(syncId, playerInventory, this, this.propertyDelegate);
    }
}
