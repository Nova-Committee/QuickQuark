package com.plr.quickquark;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(QuickQuark.MODID)
public class QuickQuark {
    public static final String MODID = "quickquark";
    private static final ForgeConfigSpec CFG;
    public static final ForgeConfigSpec.BooleanValue disableFetching;
    public static final ForgeConfigSpec.IntValue maxConnectTimeout;
    public static final ForgeConfigSpec.IntValue maxReadTimeout;
    public static final ForgeConfigSpec.BooleanValue useProxy;
    public static final ForgeConfigSpec.ConfigValue<String> proxyAddress;
    public static final ForgeConfigSpec.IntValue proxyPort;


    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("QuickQuark Settings").push("general");
        disableFetching = builder
                .comment("Fully disable quark's fetching contributor list,", "a slapdash way to solve the problem.")
                .define("disableFetching", true);
        builder.pop();
        builder.push("timeout");
        maxConnectTimeout = builder
                .comment("The max allowed milliseconds for connecting to https://raw.githubusercontent.com.")
                .defineInRange("maxConnectTimeout", 10000, 0, Integer.MAX_VALUE);
        maxReadTimeout = builder
                .comment("The max allowed milliseconds for reading the file from https://raw.githubusercontent.com.")
                .defineInRange("maxReadTimeout", 10000, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.push("proxy");
        useProxy = builder
                .comment("Whether we should use proxy to boost the fetching.")
                .define("useProxy", false);
        proxyAddress = builder
                .comment("The address of the proxy server.")
                .define("proxyAddress", "127.0.0.1");
        proxyPort = builder
                .comment("The port number of the proxy server.")
                .defineInRange("proxyPort", 7890, 0, 65535);
        builder.pop();
        CFG = builder.build();
    }

    public QuickQuark() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CFG);
    }
}
