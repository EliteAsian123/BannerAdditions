package eliteasian.mods.banneradditions.banner;

import com.mojang.datafixers.util.Pair;
import eliteasian.mods.banneradditions.BannerAdditions;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BannerPatterns {
    private static final List<BannerPatternHolder> bannerPatterns = new ArrayList<BannerPatternHolder>();

    private static final List<Integer> atlasNeededPatterns = new ArrayList<Integer>();

    //private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public static void scan() {
        //List<ModInfo> mods = ModList.get().getMods();

        // TODO Custom registry
        regNoAtlas("base", "b");
        regNoAtlas("square_bottom_left", "bl");
        regNoAtlas("square_bottom_right", "br");
        regNoAtlas("square_top_left", "tl");
        regNoAtlas("square_top_right", "tr");
        regNoAtlas("stripe_bottom", "bs");
        regNoAtlas("stripe_top", "ts");
        regNoAtlas("stripe_left", "ls");
        regNoAtlas("stripe_right", "rs");
        regNoAtlas("stripe_center", "cs");
        regNoAtlas("stripe_middle", "ms");
        regNoAtlas("stripe_downright", "drs");
        regNoAtlas("stripe_downleft", "dls");
        regNoAtlas("small_stripes", "ss");
        regNoAtlas("cross", "cr");
        regNoAtlas("straight_cross", "sc");
        regNoAtlas("triangle_bottom", "bt");
        regNoAtlas("triangle_top", "tt");
        regNoAtlas("triangles_bottom", "bts");
        regNoAtlas("triangles_top", "tts");
        regNoAtlas("diagonal_left", "ld");
        regNoAtlas("diagonal_up_right", "rd");
        regNoAtlas("diagonal_up_left", "lud");
        regNoAtlas("diagonal_right", "rud");
        regNoAtlas("circle", "mc");
        regNoAtlas("rhombus", "mr");
        regNoAtlas("half_vertical", "vh");
        regNoAtlas("half_horizontal", "hh");
        regNoAtlas("half_vertical_right", "vhr");
        regNoAtlas("half_horizontal_bottom", "hhb");
        regNoAtlas("border", "bo");
        regNoAtlas("curly_border", "cbo");
        regNoAtlas("gradient", "gra");
        regNoAtlas("gradient_up", "gru");
        regNoAtlas("bricks", "bri");
        regNoAtlas("globe", "glb", Items.GLOBE_BANNER_PATTERN);
        regNoAtlas("creeper", "cre", Items.CREEPER_BANNER_PATTERN);
        regNoAtlas("skull", "sku", Items.SKULL_BANNER_PATTERN);
        regNoAtlas("flower", "flo", Items.FLOWER_BANNER_PATTERN);
        regNoAtlas("mojang", "moj", Items.MOJANG_BANNER_PATTERN);

        // TODO Update forge
        regNoAtlas("piglin", "pig", Items.field_234776_qX_);

        reg("checker", "ch");
    }

    private static void regNoAtlas(String texture, String hash) {
        bannerPatterns.add(new BannerPatternHolder(getBannerTexture(texture), getShieldTexture(texture), hash, texture));
    }

    private static void regNoAtlas(String texture, String hash, Item item) {
        bannerPatterns.add(new BannerPatternHolder(getBannerTexture(texture), getShieldTexture(texture), hash, texture, item));
    }

    private static void reg(String texture, String hash) {
        regNoAtlas(texture, hash);
        atlasNeededPatterns.add(bannerPatterns.size() - 1);
    }

    private static void reg(String texture, String hash, Item item) {
        regNoAtlas(texture, hash, item);
        atlasNeededPatterns.add(bannerPatterns.size() - 1);
    }

    private static ResourceLocation getBannerTexture(String name) {
        return new ResourceLocation("minecraft", "entity/banner/" + name);
    }

    private static ResourceLocation getShieldTexture(String name) {
        return new ResourceLocation("minecraft", "entity/shield/" + name);
    }

    public static BannerPatternHolder get(int i) {
        return bannerPatterns.get(i);
    }

    public static BannerPatternHolder get(String hash) {
        for (BannerPatternHolder i : bannerPatterns) {
            if (i.getHashname().equals(hash)) {
                return i;
            }
        }

        return null;
    }

    public static Integer[] getAtlasNeededPatterns() {
        return atlasNeededPatterns.toArray(new Integer[0]);
    }

    public static int getLength() {
        return bannerPatterns.size();
    }

    public static int getPatternCountWithItem() {
        int i = 0;
        for (BannerPatternHolder bannerPatternHolder : bannerPatterns) {
            if (bannerPatternHolder.getItem() != null) {
                i++;
            }
        }

        return i;
    }

    public static ListNBT toListNBT(Pair<BannerPatternHolder, DyeColor>... patterns) {
        ListNBT listnbt = new ListNBT();

        for (Pair<BannerPatternHolder, DyeColor> pair : patterns) {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putString("Pattern", (pair.getFirst()).getHashname());
            compoundnbt.putInt("Color", pair.getSecond().getId());
            listnbt.add(compoundnbt);
        }

        return listnbt;
    }

    public static BannerPatternHolder[] getWithItem(Item item) {
        Item usingItem = item;
        if (usingItem == null)
            usingItem = Items.AIR;

        List<BannerPatternHolder> out = new ArrayList<BannerPatternHolder>();

        for (BannerPatternHolder pattern : bannerPatterns) {
            if (pattern.getItem() == usingItem) {
                out.add(pattern);
            }
        }

        return out.toArray(new BannerPatternHolder[0]);
    }
}
