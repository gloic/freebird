import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import {TvShowService} from "../../shared/tvshow.service";
import {SeasonDetail} from "../../shared/season-detail";

@Component({
  moduleId: module.id,
  selector: 'season',
  templateUrl: 'seasons-detail.component.html'
})

export class SeasonDetailComponent implements OnInit {

  season: SeasonDetail;
  seasonId: number;
  tvShowId: number;

  displayMode: string;

  constructor(private tvShowService: TvShowService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    console.log(this.route.snapshot.params);
    this.displayMode = 'grid';

    this.route.params.forEach((params: Params) => {
      this.seasonId = params['seasonId'];
      this.tvShowId = params['showId'];
    });

    this.route.params
    // We also have +params['showId'] if needed...
      .switchMap((params: Params) => this.tvShowService.getSeasonDetail(+params['seasonId']))
      .subscribe(s => this.season = s);
  }

  displayAs(mode: string) {
    this.displayMode = mode;
  }
}
