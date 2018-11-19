import {Component, Input, OnInit} from "@angular/core";
import {Params, ActivatedRoute, Router} from "@angular/router";

import 'rxjs/add/operator/switchMap';

import {Movie} from "../shared/movie";
import {MovieService} from "../shared/movie.service";
import {MenuItem, ConfirmationService} from "primeng/components/common/api";
import {LinkDetail} from "../../shared/link/link-detail";
import {MediaIdentificationService} from "../../identification/shared/media-identification.service";

@Component({
  moduleId: module.id,
  selector: 'movie-detail',
  templateUrl: 'movie-detail.component.html'
})
export class MovieDetailComponent implements OnInit {
  @Input()
  movie: Movie;

  breadCrumb: MenuItem[];

  constructor(private movieService: MovieService, private route: ActivatedRoute, private mediaIdentificationService: MediaIdentificationService, private router: Router) {
  }

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.movieService.getMovieById(+params['id']))
      .subscribe(movie => {
        this.movie = movie;

        this.breadCrumb = [];
        this.breadCrumb.push({label: 'Movies', routerLink: ['/movies']});
        this.breadCrumb.push({label: movie.title});
      });
  }

  onBadIdentificationEvent(link: LinkDetail) {
    this.mediaIdentificationService.wrongMovieIdentification(this.movie.id, link.id);
    if (this.movie.links.length > 1) {
      var index = this.movie.links.indexOf(link);
      this.movie.links.splice(index, 1);
    } else {
      this.router.navigate(['/movies']);
    }
  }
}
