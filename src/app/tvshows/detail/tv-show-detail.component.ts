import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {TvShowDetail} from "../shared/tvshow-detail";
import {TvShowService} from "../shared/tvshow.service";
import {MenuItem} from "primeng/components/common/api";

@Component({
  moduleId: module.id,
  selector: 'tv-show-detail',
  templateUrl: 'tv-show-detail.component.html',
  styleUrls: ['tv-show-detail.component.css']
})
export class TvShowDetailComponent implements OnInit {

  @Input()
  tvShow: TvShowDetail;

  constructor(private tvShowService: TvShowService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.tvShowService.getShowDetail(+params['id']))
      .subscribe(tvShow => this.tvShow = tvShow);
  }
}
