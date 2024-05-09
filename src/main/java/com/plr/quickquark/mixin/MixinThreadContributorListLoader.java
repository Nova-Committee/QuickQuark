package com.plr.quickquark.mixin;

import com.plr.quickquark.QuickQuark;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
        final String host = QuickQuark.proxyHost.get();
        final int port = QuickQuark.proxyPort.get();
        final SocketAddress socketAddress = new InetSocketAddress(host, port);
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
        final boolean needsAuth = QuickQuark.needsAuthentication.get();
        if (!needsAuth) return instance.openConnection(proxy);
        final String proxyHeader = Base64.getEncoder().encodeToString((QuickQuark.proxyUsername.get() + ":" + QuickQuark.proxyPassword.get()).getBytes(StandardCharsets.UTF_8));
        final URLConnection conn = instance.openConnection(proxy);
        conn.setRequestProperty("Proxy-Authorization", "Basic " + proxyHeader);
        return conn;
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
