package eliteasian.mods.banneradditions.banner;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class BannerPatternHolder {
    private final ResourceLocation bannerTexture;
    private final ResourceLocation shieldTexture;

    private final String hashname;

    private final String translationKey;

    private final Item item;

    public BannerPatternHolder(ResourceLocation bannerTexture, ResourceLocation shieldTexture, String hashname, String translationKey) {
        this.bannerTexture = bannerTexture;
        this.shieldTexture = shieldTexture;

        this.hashname = hashname;

        this.translationKey = translationKey;

        this.item = Items.AIR;
    }

    public BannerPatternHolder(ResourceLocation texture, ResourceLocation shieldTexture, String hashname, String translationKey, Item item) {
        this.bannerTexture = texture;
        this.shieldTexture = shieldTexture;

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

    public ResourceLocation getBannerTexture() {
        return this.bannerTexture;
    }

    public ResourceLocation getShieldTexture() {
        return this.shieldTexture;
    }

    public String getTranslationKey() {
        return "block.minecraft.banner." + translationKey;
    }

    public Item getItem() {
        return this.item;
    }
}
