package com.plr.quickquark.mixin;

import com.plr.quickquark.QuickQuark;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.net.*;

@Mixin(targets = "vazkii.quark.base.handler.ContributorRewardHandler$ThreadContributorListLoader", remap = false)
public abstract class MixinThreadContributorListLoader {
    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/URL;openConnection()Ljava/net/URLConnection;",
                    remap = false
            ),
            remap = false
    )
    private URLConnection redirect$run$proxy(URL instance) throws IOException {
        if (!QuickQuark.useProxy.get()) return instance.openConnection();
        final SocketAddress socketAddress = new InetSocketAddress(QuickQuark.proxyAddress.get(), QuickQuark.proxyPort.get());
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        return instance.openConnection(proxy);
    }

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/URLConnection;setConnectTimeout(I)V",
                    remap = false
            ),
            remap = false
    )
    private void redirect$run$connectTime(URLConnection instance, int timeout) {
        instance.setConnectTimeout(QuickQuark.maxConnectTimeout.get());
    }

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/URLConnection;setReadTimeout(I)V",
                    remap = false
            ),
            remap = false
    )
    private void redirect$run$readTime(URLConnection instance, int timeout) {
        instance.setReadTimeout(QuickQuark.maxReadTimeout.get());
    }
}
