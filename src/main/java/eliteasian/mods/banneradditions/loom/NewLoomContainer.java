package eliteasian.mods.banneradditions.loom;

import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NewLoomContainer extends Container {
    private final IWorldPosCallable worldPos;
    private final IntReferenceHolder selectedPattern = IntReferenceHolder.single();
    private Runnable field_217035_e = () -> {
    };
    private final Slot slotBanner;
    private final Slot slotDye;
    private final Slot slotPattern;
    private final Slot output;
    private long field_226622_j_;

    private final IInventory loomInventory = new Inventory(3) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void markDirty() {
            super.markDirty();
            NewLoomContainer.this.onCraftMatrixChanged(this);
            NewLoomContainer.this.field_217035_e.run();
        }
    };

    private final IInventory field_217041_k = new Inventory(1) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void markDirty() {
            super.markDirty();
            NewLoomContainer.this.field_217035_e.run();
        }
    };

    public NewLoomContainer(int p_i50073_1_, PlayerInventory p_i50073_2_) {
        this(p_i50073_1_, p_i50073_2_, IWorldPosCallable.DUMMY);
    }

    public NewLoomContainer(int p_i50074_1_, PlayerInventory p_i50074_2_, final IWorldPosCallable p_i50074_3_) {
        super(BannerAdditionsRegistry.Containers.LOOM, p_i50074_1_);
        this.worldPos = p_i50074_3_;

        this.slotBanner = this.addSlot(new Slot(this.loomInventory, 0, 13, 26) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof BannerItem;
            }
        });

        this.slotDye = this.addSlot(new Slot(this.loomInventory, 1, 33, 26) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });

        this.slotPattern = this.addSlot(new Slot(this.loomInventory, 2, 23, 45) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack) {
                Item stackitem = stack.getItem();

                for (int i = 0; i < BannerPatterns.getLength(); i++) {
                    if (BannerPatterns.get(i).getItem().equals(stackitem.getRegistryName())) {
                        return true;
                    }
                }

                return false;
            }
        });

        this.output = this.addSlot(new Slot(this.field_217041_k, 0, 143, 58) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                NewLoomContainer.this.slotBanner.decrStackSize(1);
                NewLoomContainer.this.slotDye.decrStackSize(1);
                if (!NewLoomContainer.this.slotBanner.getHasStack() || !NewLoomContainer.this.slotDye.getHasStack()) {
                    NewLoomContainer.this.selectedPattern.set(0);
                }

                p_i50074_3_.consume((p_216951_1_, p_216951_2_) -> {
                    long l = p_216951_1_.getGameTime();
                    if (NewLoomContainer.this.field_226622_j_ != l) {
                        p_216951_1_.playSound(null, p_216951_2_, SoundEvents.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        NewLoomContainer.this.field_226622_j_ = l;
                    }

                });
                return super.onTake(thePlayer, stack);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(p_i50074_2_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(p_i50074_2_, k, 8 + k * 18, 142));
        }

        this.trackInt(this.selectedPattern);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSelectedPattern() {
        return this.selectedPattern.get();
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPos, playerIn, Blocks.LOOM);
    }

    /**
     * Handles the given Button-click on the server, currently only used by enchanting (totally). Name is for legacy.
     */
    public boolean enchantItem(PlayerEntity playerIn, int id) {
        Item item = this.slotPattern.getStack().getItem();
        if ((item != Items.AIR || id > 0) && id <= BannerPatterns.getWithItem(item).length - 1) {
            this.selectedPattern.set(id);
            this.createOutputStack();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.selectedPattern.set(0);

        if (this.slotBanner.getStack().isEmpty() || this.slotDye.getStack().isEmpty()) {
            this.output.putStack(ItemStack.EMPTY);
        }

        this.createOutputStack();
        this.detectAndSendChanges();
    }

    @OnlyIn(Dist.CLIENT)
    public void func_217020_a(Runnable p_217020_1_) {
        this.field_217035_e = p_217020_1_;
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            boolean isPattern = false;

            for (int i = 0; i < BannerPatterns.getLength(); i++) {
                if (BannerPatterns.get(i).getItem().equals(itemstack1.getItem().getRegistryName())) {
                    isPattern = true;
                }
            }

            if (index == this.output.slotNumber) {
                if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != this.slotDye.slotNumber && index != this.slotBanner.slotNumber && index != this.slotPattern.slotNumber) {
                if (itemstack1.getItem() instanceof BannerItem) {
                    if (!this.mergeItemStack(itemstack1, this.slotBanner.slotNumber, this.slotBanner.slotNumber + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof DyeItem) {
                    if (!this.mergeItemStack(itemstack1, this.slotDye.slotNumber, this.slotDye.slotNumber + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isPattern) {
                    if (!this.mergeItemStack(itemstack1, this.slotPattern.slotNumber, this.slotPattern.slotNumber + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPos.consume((p_217028_2_, p_217028_3_) -> {
            this.clearContainer(playerIn, playerIn.world, this.loomInventory);
        });
    }

    /**
     * Creates an output banner ItemStack based on the patterns, dyes, etc. in the loom.
     */
    private void createOutputStack() {
        if (this.slotPattern.getStack().getItem() != Items.AIR || this.selectedPattern.get() > 0) {
            ItemStack itemstackBanner = this.slotBanner.getStack();
            ItemStack itemstackDye = this.slotDye.getStack();
            ItemStack itemstackOut = ItemStack.EMPTY;
            if (!itemstackBanner.isEmpty() && !itemstackDye.isEmpty()) {
                itemstackOut = itemstackBanner.copy();
                itemstackOut.setCount(1);
                BannerPatternHolder bannerpattern = BannerPatterns.getWithItem(this.slotPattern.getStack().getItem())[this.selectedPattern.get()];
                DyeColor dyecolor = ((DyeItem) itemstackDye.getItem()).getDyeColor();
                CompoundNBT compoundnbt = itemstackOut.getOrCreateChildTag("BlockEntityTag");
                ListNBT listnbt;
                if (compoundnbt.contains("Patterns", 9)) {
                    listnbt = compoundnbt.getList("Patterns", 10);
                } else {
                    listnbt = new ListNBT();
                    compoundnbt.put("Patterns", listnbt);
                }

                CompoundNBT compoundnbt1 = new CompoundNBT();
                compoundnbt1.putString("Pattern", bannerpattern.getHashname());
                compoundnbt1.putInt("Color", dyecolor.getId());
                listnbt.add(compoundnbt1);
            }

            if (!ItemStack.areItemStacksEqual(itemstackOut, this.output.getStack())) {
                this.output.putStack(itemstackOut);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public Slot getBannerSlot() {
        return this.slotBanner;
    }

    @OnlyIn(Dist.CLIENT)
    public Slot getDyeSlot() {
        return this.slotDye;
    }

    @OnlyIn(Dist.CLIENT)
    public Slot getPatternSlot() {
        return this.slotPattern;
    }

    @OnlyIn(Dist.CLIENT)
    public Slot getOutputSlot() {
        return this.output;
    }
}