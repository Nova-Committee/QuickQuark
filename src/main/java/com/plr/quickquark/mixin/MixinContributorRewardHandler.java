package com.plr.quickquark.mixin;

import com.plr.quickquark.QuickQuark;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.handler.ContributorRewardHandler;

@Mixin(value = ContributorRewardHandler.class, remap = false)
public abstract class MixinContributorRewardHandler {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true, remap = false)
    private static void inject$init(CallbackInfo ci) {
        if (QuickQuark.disableFetching.get()) ci.cancel();
    }
}
