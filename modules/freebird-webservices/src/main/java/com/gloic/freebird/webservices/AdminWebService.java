package com.gloic.freebird.webservices;


import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.services.service.SiteService;
import com.gloic.freebird.services.wrapper.SiteWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author gloic
 */
@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminWebService {

    private final SiteService siteService;

    @Autowired
    public AdminWebService(SiteService siteService) {
        this.siteService = siteService;
    }

    @GetMapping(value = "/sites/")
    public List<SiteWrapper> getAllSites() {
        return siteService.findAll();
    }

    @PutMapping(value = "/sites/")
    public Site addSite(@RequestBody Site siteToAdd) {
        return siteService.addSite(siteToAdd);
    }

    @GetMapping(value = "/sites/scan/{id}/{redoUnknowns}")
    public void forceSiteScan(@PathVariable("id") Long id, @PathVariable("redoUnknowns") boolean redoUnknowns) {
        siteService.scan(id, redoUnknowns);

    }

    @PutMapping(value = "/sites/scan/all")
    public void forceSiteScan(@PathParam("redoUnknowns") boolean redoUnknowns) {
        siteService.scanAll(redoUnknowns);
    }

    @DeleteMapping(value = "/sites/delete/{id}")
    public void forceSiteScan(@PathVariable("id") Long id) {
        siteService.deleteSite(id);
    }
}
