package eliteasian.mods.banneradditions.banner;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class BannerPatternHolder {
    private final ResourceLocation texture;
    private final String hashname;

    private final String translationKey;

    private final Item item;

    public BannerPatternHolder(ResourceLocation texture, String hashname, String translationKey) {
        this.texture = texture;
        this.hashname = hashname;

        this.translationKey = translationKey;

        this.item = Items.AIR;
    }

    public BannerPatternHolder(ResourceLocation texture, String hashname, String translationKey, Item item) {
        this.texture = texture;
        this.hashname = hashname;

        this.translationKey = translationKey;

        if (item == null)
            this.item = Items.AIR;
        else
            this.item = item;
    }

    public String getHashname() {
        return this.hashname;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public String getTranslationKey() {
        return "block.minecraft.banner." + translationKey;
    }

    public Item getItem() {
        return this.item;
    }
}
