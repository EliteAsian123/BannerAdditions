package eliteasian.mods.banneradditions.bannerpattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BannerPatterns extends ReloadListener<BannerPatterns> {
    public static final BannerPatterns INSTANCE = new BannerPatterns();

    private static final List<BannerPatternHolder> bannerPatterns = new ArrayList<BannerPatternHolder>();

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    protected BannerPatterns prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        profilerIn.startTick();

        profilerIn.startSection("static");

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

        profilerIn.endStartSection("register");

        for(ResourceLocation resourceLocation : resourceManagerIn.getAllResourceLocations("banner_patterns", (p_215274_0_) ->
                p_215274_0_.endsWith(".json")
        )) {
            try {
                IResource resource = resourceManagerIn.getResource(resourceLocation);

                InputStream inputstream = resource.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));

                JsonObject json = JSONUtils.fromJson(GSON, reader, JsonObject.class);

                String hashname = JSONUtils.getString(json, "hashname");

                ResourceLocation bannerTexture = new ResourceLocation(JSONUtils.getString(json, "bannerTexture"));
                ResourceLocation shieldTexture = new ResourceLocation(JSONUtils.getString(json, "shieldTexture"));

                String[] pathSplit = resourceLocation.getPath().split("/");
                String name = pathSplit[pathSplit.length - 1].split("\\.")[0];

                if (JSONUtils.hasField(json, "item")) {
                    ResourceLocation item = new ResourceLocation(JSONUtils.getString(json, "item"));

                    bannerPatterns.add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, name, item));

                    BannerAdditions.LOGGER.debug("HEYO " + item);
                } else {
                    bannerPatterns.add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, name));
                }
            } catch (IOException ignored) { }
        }

        profilerIn.endSection();

        profilerIn.endTick();

        return INSTANCE;
    }

    protected void apply(BannerPatterns objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {

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
            if (pattern.getItem().equals(usingItem.getRegistryName())) {
                out.add(pattern);
            }
        }

        return out.toArray(new BannerPatternHolder[0]);
    }
}
