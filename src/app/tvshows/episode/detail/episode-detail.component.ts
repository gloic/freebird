import {Component, OnInit} from "@angular/core";
import {EpisodeDetail} from "../../shared/episode-detail";
import {TvShowService} from "../../shared/tvshow.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {MenuItem} from "primeng/components/common/api";
import {FileSizePipe} from "../../../shared/pipe/file-size.pipe";
import {LinkDetail} from "../../../shared/link/link-detail";
import {MediaIdentificationService} from "../../../identification/shared/media-identification.service";

@Component({
  moduleId: module.id,
  selector: 'episode-detail',
  templateUrl: 'episode-detail.component.html',
})
export class EpisodeDetailComponent implements OnInit {
  breadCrumb: MenuItem[];
  episode: EpisodeDetail;

  constructor(private tvShowService: TvShowService, private route: ActivatedRoute, private router: Router, private mediaIdentificationService: MediaIdentificationService ) {}

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.tvShowService.getEpisode(+params['episodeId']))
      .subscribe(episode => {
        this.episode = episode;
        this.breadCrumb = [];
        this.breadCrumb.push({label: 'Tv Shows' });
        this.breadCrumb.push({label: episode.tvShow.title});
        this.breadCrumb.push({label: 'Season ' + episode.season.seasonNumber});
        this.breadCrumb.push({label: episode.episodeNum + ' - ' + episode.title});
      });
  }

  onBadIdentificationEvent(link: LinkDetail) {
    this.mediaIdentificationService.wrongEpisodeIdentification(this.episode.id, link.id);
    if(this.episode.links.length > 1) {
      var index = this.episode.links.indexOf(link);
      this.episode.links.splice(index, 1);
    } else {
      this.router.navigate(['/tv-shows']);
    }
  }
}
