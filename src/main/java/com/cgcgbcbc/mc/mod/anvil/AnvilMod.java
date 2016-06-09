package com.cgcgbcbc.mc.mod.anvil;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by guangchen on 6/8/16 21:18.
 */
@Mod(modid = "anvilfree", version = "1.1.1")
public class AnvilMod {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLLog.getLogger().info("registering anvil handler");
        MinecraftForge.EVENT_BUS.register(new AnvilHandler());
    }
}
