package eliteasian.mods.banneradditions.bannerpattern;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class BannerPatternHolder {
    private final ResourceLocation bannerTexture;
    private final ResourceLocation shieldTexture;

    private final String hashname;

    private final String fullName;

    private final ResourceLocation item;

    public BannerPatternHolder(ResourceLocation bannerTexture, ResourceLocation shieldTexture, String hashname, String fullName) {
        this.bannerTexture = bannerTexture;
        this.shieldTexture = shieldTexture;

        this.hashname = hashname;

        this.fullName = fullName;

        this.item = Items.AIR.getRegistryName();
    }

    public BannerPatternHolder(ResourceLocation texture, ResourceLocation shieldTexture, String hashname, String fullName, ResourceLocation item) {
        this.bannerTexture = texture;
        this.shieldTexture = shieldTexture;

        this.hashname = hashname;

        this.fullName = fullName;

        if (item == null)
            this.item = Items.AIR.getRegistryName();
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
        return "block.minecraft.banner." + fullName;
    }

    public ResourceLocation getItem() {
        return this.item;
    }
}
