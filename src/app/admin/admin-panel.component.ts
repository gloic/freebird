import {Component, OnInit} from "@angular/core";
import {Site} from "./shared/site";
import {SiteService} from "./shared/site.service";
import {AdminTabStatsModel} from "./tabs/stats/admin-tab-stats.model";
import {AdminTabSitesModel} from "./tabs/sites/admin-tab-sites.model";
import {AdminTabUsersModel} from "./tabs/users/admin-tab-users.model";
import {ApplicationUser} from "./tabs/users/application-user";
import {UserService} from "./shared/user.service";
import {Message} from "primeng/primeng";
import {User} from "../guard/user";
import * as FileSaver from "file-saver";
import {forEach} from "@angular/router/src/utils/collection";

@Component({
  moduleId: module.id,
  selector: 'admin-panel',
  templateUrl: 'admin-panel.component.html'
})
export class AdminPanelComponent implements OnInit {
  sites: Site[];
  users: ApplicationUser[];

  statsTabModel = new AdminTabStatsModel();
  sitesTabModel = new AdminTabSitesModel();
  usersTabModel = new AdminTabUsersModel();

  msgs: Message[] = [];

  constructor(private siteService: SiteService, private userService: UserService) {
  }

  ngOnInit(): void {
    this.init();
  }

  init(): void {
    this.siteService.getAllSites().subscribe(data => {
      this.sites = data;
      this.fillStatTabModel();
      this.fillSitesTabModel();
    });

    this.userService.getAllUsers().subscribe(data => {
      this.users = data;
      this.fillUsersTabModel();
    });
  }

  fillSitesTabModel(): void {
    this.sitesTabModel.sites = this.sites;
  }

  fillStatTabModel(): void {
    this.statsTabModel.sites = this.sites;
    for (const site of this.sites) {
      this.statsTabModel.nbMovies += site.numberMovieOnline + site.numberMovieNew;
      this.statsTabModel.nbMoviesNew += site.numberMovieNew;
      this.statsTabModel.nbEpisodes += site.numberEpisodeOnline + site.numberEpisodeNew;
      this.statsTabModel.nbEpisodesNew += site.numberEpisodeNew;
      this.statsTabModel.nbUnknowns += site.numberUnknownOnline + site.numberUnknownNew;
      this.statsTabModel.nbUnknownsNew += site.numberUnknownNew;
    }
  }

  fillUsersTabModel() {
    this.usersTabModel.users = this.users;
    this.usersTabModel.user = new ApplicationUser();
  }

  onAddSiteClickedEvent(event) {
    this.siteService.addSite(event.site).subscribe(data => {
      this.showInfoMessage('Site added');
      this.sites.push(data);
      if (event.scanNow) {
        this.siteService.forceScan(data.id, true).then();
      }
      this.sitesTabModel.site = new Site();
    }, err => {
      console.log(err);
    });
  }

  onForceScanClickedEvent(event) {
    this.showInfoMessage('Scan started');
    this.siteService.forceScan(event.id, event.checked).then();
  }

  onScanAllClickedEvent(checked: boolean) {
    this.showInfoMessage('Complete scan started');
    this.siteService.scanAll(checked).then();
  }

  onExportSitesClickedEvent() {
    let exportStr: string = '';
    for(let s of this.sitesTabModel.sites) {
      exportStr += s.url + '\r\n';
    }
    let blob = new Blob([exportStr], { type: 'text/plain;charset=utf-8' });
    FileSaver.saveAs(blob, "export.txt");
  }

  onRefreshClickedEvent(): void {
    this.showInfoMessage('Data refreshed');
    // reset models
    this.statsTabModel = new AdminTabStatsModel();
    this.sitesTabModel = new AdminTabSitesModel();
    this.init();
  }

  onUserAddEvent(user: ApplicationUser): void {
    this.userService.addUser(user).subscribe(r => {
      this.showInfoMessage('User ' + user.username + ' created');
      this.users.push(r);
      this.fillUsersTabModel();
    });
  }

  onDeleteUserEvent(user: ApplicationUser): void {
    console.log('deleting :' + user.username);
    this.userService.deleteUser(user.id).subscribe(r => {
      this.users.splice(this.users.indexOf(user), 1);
      this.showInfoMessage('User ' + user.username + ' is deleted');
    });
  }

  showInfoMessage(message: string) {
    this.msgs.push({severity: 'info', summary: 'Info Message', detail: message});
  }

  deleteSiteClickedEvent(event) {
    this.siteService.deleteSite(event.site.id).then();
    let index: number = this.sites.indexOf(event.site);
    if (index !== -1) {
      this.sites.splice(index, 1);
    }
  }

  onImportSitesEvent(sites: string[]) {
    for(let line of sites) {
      if(line.length > 0) {
        let site:Site = new Site();
        site.url = line;
        this.siteService.addSite(site).subscribe(data => {
          this.showInfoMessage('Sites added');
          this.sites.push(data);
        }, err => {
          console.log(err);
        });
      }
    }
  }
}
