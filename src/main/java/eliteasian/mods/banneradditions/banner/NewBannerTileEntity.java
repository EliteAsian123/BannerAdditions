package eliteasian.mods.banneradditions.banner;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeTileEntity;

public class NewBannerTileEntity extends TileEntity implements INameable, IForgeTileEntity {
    @Nullable
    private ITextComponent name;
    @Nullable
    private DyeColor baseColor = DyeColor.WHITE;
    /** A list of all the banner patterns. */
    @Nullable
    private ListNBT patterns;
    private boolean patternDataSet;
    /** A list of all patterns stored on this banner. */
    @Nullable
    private List<Pair<BannerPatternHolder, DyeColor>> patternList;

    public NewBannerTileEntity() {
        super(BannerAdditionsRegistry.TileEntities.BANNER);
    }

    public NewBannerTileEntity(DyeColor p_i47731_1_) {
        this();
        this.baseColor = p_i47731_1_;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static ListNBT getPatternsFromItemStack(ItemStack p_230139_0_) {
        ListNBT listnbt = null;
        CompoundNBT compoundnbt = p_230139_0_.getChildTag("BlockEntityTag");
        if (compoundnbt != null && compoundnbt.contains("Patterns", 9)) {
            listnbt = compoundnbt.getList("Patterns", 10).copy();
        }

        return listnbt;
    }

    @OnlyIn(Dist.CLIENT)
    public void loadFromItemStack(ItemStack p_195534_1_, DyeColor p_195534_2_) {
        this.patterns = getPatternsFromItemStack(p_195534_1_);
        this.baseColor = p_195534_2_;
        this.patternList = null;
        this.patternDataSet = true;
        this.name = p_195534_1_.hasDisplayName() ? p_195534_1_.getDisplayName() : null;
    }

    public ITextComponent getName() {
        return (ITextComponent)(this.name != null ? this.name : new TranslationTextComponent("block.minecraft.banner"));
    }

    @Nullable
    public ITextComponent getCustomName() {
        return this.name;
    }

    public void func_213136_a(ITextComponent p_213136_1_) {
        this.name = p_213136_1_;
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (this.patterns != null) {
            compound.put("Patterns", this.patterns);
        }

        if (this.name != null) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.name));
        }

        return compound;
    }

    public void func_230337_a_(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.func_230337_a_(p_230337_1_, p_230337_2_);
        if (p_230337_2_.contains("CustomName", 8)) {
            this.name = ITextComponent.Serializer.func_240643_a_(p_230337_2_.getString("CustomName"));
        }

        if (this.hasWorld()) {
            this.baseColor = ((NewAbstractBannerBlock)this.getBlockState().getBlock()).getColor();
        } else {
            this.baseColor = null;
        }

        this.patterns = p_230337_2_.getList("Patterns", 10);
        this.patternList = null;
        this.patternDataSet = true;
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 6, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in handleUpdateTag
     */
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    /**
     * Added from forge.
     */
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(this.getTileEntity().getBlockState(), pkt.getNbtCompound());
    }

    /**
     * Retrieves the amount of patterns stored on an ItemStack. If the tag does not exist this value will be 0.
     */
    public static int getPatterns(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        return compoundnbt != null && compoundnbt.contains("Patterns") ? compoundnbt.getList("Patterns", 10).size() : 0;
    }

    /**
     * Retrieves the list of patterns for this tile entity. The banner data will be initialized/refreshed before this
     * happens.
     */
    @OnlyIn(Dist.CLIENT)
    public List<Pair<BannerPatternHolder, DyeColor>> getPatternList() {
        if (this.patternList == null && this.patternDataSet) {
            this.patternList = createPatternList(this.getBaseColor(this::getBlockState), this.patterns);
        }

        return this.patternList;
    }

    @OnlyIn(Dist.CLIENT)
    public static List<Pair<BannerPatternHolder, DyeColor>> createPatternList(DyeColor p_230138_0_, @Nullable ListNBT p_230138_1_) {
        List<Pair<BannerPatternHolder, DyeColor>> list = Lists.newArrayList();
        list.add(Pair.of(BannerPatterns.get(0), p_230138_0_));
        if (p_230138_1_ != null) {
            for(int i = 0; i < p_230138_1_.size(); ++i) {
                CompoundNBT compoundnbt = p_230138_1_.getCompound(i);
                BannerPatternHolder bannerpattern = BannerPatterns.get(compoundnbt.getString("Pattern"));
                if (bannerpattern != null) {
                    int j = compoundnbt.getInt("Color");
                    list.add(Pair.of(bannerpattern, DyeColor.byId(j)));
                }
            }
        }

        return list;
    }

    /**
     * Removes all the banner related data from a provided instance of ItemStack.
     */
    public static void removeBannerData(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null && compoundnbt.contains("Patterns", 9)) {
            ListNBT listnbt = compoundnbt.getList("Patterns", 10);
            if (!listnbt.isEmpty()) {
                listnbt.remove(listnbt.size() - 1);
                if (listnbt.isEmpty()) {
                    stack.removeChildTag("BlockEntityTag");
                }

            }
        }
    }

    public ItemStack getItem(BlockState p_190615_1_) {
        ItemStack itemstack = new ItemStack(NewBannerBlock.forColor(this.getBaseColor(() -> {
            return p_190615_1_;
        })));
        if (this.patterns != null && !this.patterns.isEmpty()) {
            itemstack.getOrCreateChildTag("BlockEntityTag").put("Patterns", this.patterns.copy());
        }

        if (this.name != null) {
            itemstack.setDisplayName(this.name);
        }

        return itemstack;
    }

    public DyeColor getBaseColor(Supplier<BlockState> p_195533_1_) {
        if (this.baseColor == null) {
            this.baseColor = ((NewAbstractBannerBlock)p_195533_1_.get().getBlock()).getColor();
        }

        return this.baseColor;
    }
}