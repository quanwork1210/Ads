package com.ads.adsmaker.Controllers;

import com.google.api.ads.admanager.axis.factory.AdManagerServices;
import com.google.api.ads.admanager.axis.v202205.Network;
import com.google.api.ads.admanager.axis.v202205.NetworkServiceInterface;
import com.google.api.ads.admanager.lib.client.AdManagerSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;

@RestController
public class AdsMakerController {

    @PostMapping("/ads")
    String testAds() throws ConfigurationLoadException,
            ValidationException,
            OAuthException,
            RemoteException {

        Credential oAuth2Credential = new OfflineCredentials.Builder()
                .forApi(OfflineCredentials.Api.AD_MANAGER)
                .fromFile()
                .build()
                .generateCredential();

        // Construct an AdManagerSession.
        AdManagerSession session = new AdManagerSession.Builder()
                .fromFile()
                .withOAuth2Credential(oAuth2Credential)
                .build();

        // Construct a Google Ad Manager service factory, which can only be used once per
        // thread, but should be reused as much as possible.
        AdManagerServices adManagerServices = new AdManagerServices();

        // Retrieve the appropriate service
        NetworkServiceInterface networkService = adManagerServices.get(session,
                NetworkServiceInterface.class);

        // Make a request
        Network network = networkService.getCurrentNetwork();

        System.out.printf("Current network has network code '%s' and display" +
                " name '%s'.%n", network.getNetworkCode(), network.getDisplayName());
        return "SUCCESS";
    }

}
