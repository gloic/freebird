package com.gloic.freebird.services.config;

import com.gloic.freebird.persistence.repository.GenreRepository;
import com.gloic.freebird.services.scraper.TheMovieDBService;
import com.gloic.freebird.services.service.UserAdministrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * <p>Bean used to initialise the database with required data at first start.</p>
 * <p>If the database is empty, a user must exist and the genres must be filled</p>
 *
 * @author gloic
 */
@Component
@Slf4j
@SuppressWarnings("unused")
public class InstallationConfiguration {

    private final GenreRepository genreRepository;
    private final TheMovieDBService theMovieDBService;
    private final UserAdministrationService userAdministrationService;

    public InstallationConfiguration(GenreRepository genreRepository, TheMovieDBService theMovieDBService, UserAdministrationService userAdministrationService) {
        this.genreRepository = genreRepository;
        this.theMovieDBService = theMovieDBService;
        this.userAdministrationService = userAdministrationService;
    }

    /**
     * At start (or context is refresh) the following method will be executed
     */
    @EventListener(ContextRefreshedEvent.class)
    public void firstStart() {
        // Initialize content of the table Genre if necessary
        initializeGenre();
        trustEveryone();
        // Create an user Admin if necessary
        userAdministrationService.createAdminUser();
    }

    /**
     * Find genre from DB
     */
    private void initializeGenre() {
        if (genreRepository.count() == 0) {
            log.warn("No Genre in the database, initializing this table from the api");
            genreRepository.save(theMovieDBService.getAllGenreFromAPI());
        }
    }

    /**
     * Ignore all SSL certificates so the https links can be read.
     */
    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            // should never happen
            log.error("Unknown exception occurs when retrieving document", e);
        }
    }
}
