import {Component, Input, OnInit} from "@angular/core";
import {MenuItem} from "primeng/components/common/api";
import {TvShowLight} from "./tvshow-light";
import {EpisodeDetail} from "./episode-detail";
import {SeasonDetail} from "./season-detail";
@Component({
  moduleId: module.id,
  selector: 'breadcrumb-tv',
  template: '<p-breadcrumb [model]="items"></p-breadcrumb>'
})
export class BreadcrumbTVComponent implements OnInit {
  items: MenuItem[];

  @Input()
  tvShow: TvShowLight;

  @Input()
  season: SeasonDetail;

  @Input()
  episode: EpisodeDetail;

  ngOnInit(): void {

    let tvShowId: string;
    let tvShowTitle: string;
    let seasonId: number;
    let seasonNumber: number;
    if(this.episode) {
      tvShowId = this.episode.tvShow.id;
      tvShowTitle = this.episode.tvShow.title;
      seasonId = this.episode.season.id;
      seasonNumber = this.episode.season.seasonNumber;
    } else if(this.season) {
      tvShowId = this.season.tvShow.id;
      tvShowTitle = this.season.tvShow.title;
      seasonId = this.season.id;
      seasonNumber = this.season.seasonNumber;
    } else if(this.tvShow) {
      tvShowId = this.tvShow.id;
      tvShowTitle = this.tvShow.title;
    }

    this.items = [];
    this.items.push({label:'Tv Shows', routerLink: ['/tv-shows']});

    this.items.push({label: tvShowTitle, routerLink: ['/tv-shows/', tvShowId]});

    if(this.episode || this.season) {
      this.items.push({label: 'Season ' + seasonNumber, routerLink: ['/tv-shows/', tvShowId, seasonId]});
    }
    if(this.episode) {
      this.items.push({label: 'Episode ' + this.episode.episodeNum + (this.episode.title ? ' (' + this.episode.title + ')' : '')});
    }
  }

}
