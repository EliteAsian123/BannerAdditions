package eliteasian.mods.banneradditions.bannerpattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BannerPatterns extends JsonReloadListener {
    private static final List<BannerPatternHolder> bannerPatterns = new ArrayList<BannerPatternHolder>();

    public BannerPatterns() {
        super((new GsonBuilder()).setPrettyPrinting().create(), "banner_patterns");
    }

    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        bannerPatterns.clear();

        regStatic("base", "b");
        regStatic("square_bottom_left", "bl");
        regStatic("square_bottom_right", "br");
        regStatic("square_top_left", "tl");
        regStatic("square_top_right", "tr");
        regStatic("stripe_bottom", "bs");
        regStatic("stripe_top", "ts");
        regStatic("stripe_left", "ls");
        regStatic("stripe_right", "rs");
        regStatic("stripe_center", "cs");
        regStatic("stripe_middle", "ms");
        regStatic("stripe_downright", "drs");
        regStatic("stripe_downleft", "dls");
        regStatic("small_stripes", "ss");
        regStatic("cross", "cr");
        regStatic("straight_cross", "sc");
        regStatic("triangle_bottom", "bt");
        regStatic("triangle_top", "tt");
        regStatic("triangles_bottom", "bts");
        regStatic("triangles_top", "tts");
        regStatic("diagonal_left", "ld");
        regStatic("diagonal_up_right", "rd");
        regStatic("diagonal_up_left", "lud");
        regStatic("diagonal_right", "rud");
        regStatic("circle", "mc");
        regStatic("rhombus", "mr");
        regStatic("half_vertical", "vh");
        regStatic("half_horizontal", "hh");
        regStatic("half_vertical_right", "vhr");
        regStatic("half_horizontal_bottom", "hhb");
        regStatic("border", "bo");
        regStatic("curly_border", "cbo");
        regStatic("gradient", "gra");
        regStatic("gradient_up", "gru");
        regStatic("bricks", "bri");
        regStatic("globe", "glb", BannerAdditionsRegistry.Items.GLOBE_BANNER_PATTERN);
        regStatic("creeper", "cre", BannerAdditionsRegistry.Items.CREEPER_BANNER_PATTERN);
        regStatic("skull", "sku", BannerAdditionsRegistry.Items.SKULL_BANNER_PATTERN);
        regStatic("flower", "flo", BannerAdditionsRegistry.Items.FLOWER_BANNER_PATTERN);
        regStatic("mojang", "moj", BannerAdditionsRegistry.Items.MOJANG_BANNER_PATTERN);
        regStatic("piglin", "pig", BannerAdditionsRegistry.Items.PIGLIN_BANNER_PATTERN);

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();

            // Skip Metadata
            if(resourceLocation.getPath().startsWith("_"))
                continue;

            JsonObject json = JSONUtils.getJsonObject(entry.getValue(), "top element");

            String hashname = JSONUtils.getString(json, "hashname");

            ResourceLocation bannerTextureRaw = new ResourceLocation(JSONUtils.getString(json, "bannerTexture"));
            ResourceLocation shieldTextureRaw = new ResourceLocation(JSONUtils.getString(json, "shieldTexture"));

            ResourceLocation bannerTexture = new ResourceLocation(bannerTextureRaw.getNamespace(), "entity/banner/" + bannerTextureRaw.getPath());
            ResourceLocation shieldTexture = new ResourceLocation(shieldTextureRaw.getNamespace(), "entity/shield/" + shieldTextureRaw.getPath());

            String[] pathSplit = resourceLocation.getPath().split("/");
            String name = pathSplit[pathSplit.length - 1];

            if (JSONUtils.hasField(json, "item")) {
                ResourceLocation item = new ResourceLocation(JSONUtils.getString(json, "item"));

                bannerPatterns.add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, name, item));
            } else {
                bannerPatterns.add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, name));
            }
        }
    }

    public static void regStatic(String name, String hashname, Item item) {
        bannerPatterns.add(new BannerPatternHolder(new ResourceLocation("minecraft", "entity/banner/" + name), new ResourceLocation("minecraft", "entity/shield/" + name), hashname, name, item.getRegistryName()));
    }

    public static void regStatic(String name, String hashname) {
        regStatic(name, hashname, Items.AIR);
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

    public static int getLength() {
        return bannerPatterns.size();
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
            if (pattern.getItem().equals(usingItem.getRegistryName())) {
                out.add(pattern);
            }
        }

        return out.toArray(new BannerPatternHolder[0]);
    }
}
