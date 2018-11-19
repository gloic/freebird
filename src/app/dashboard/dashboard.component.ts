import {Component, OnInit} from "@angular/core";

import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";

import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';

import {MediaSearcherService} from "./shared/media-searcher.service";
import {Result} from "./shared/result";
import {Movie} from "../movies/shared/movie";
import {TvShowDetail} from "../tvshows/shared/tvshow-detail";
import {EpisodeDetail} from "../tvshows/shared/episode-detail";
import {SelectItem} from "primeng/primeng";


@Component({
  moduleId: module.id,
  selector: 'dashboard',
  templateUrl: 'dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  results: Observable<Result[]>;

  topMovies: Movie[];
  topEpisodes: EpisodeDetail[];


  searchModes: SelectItem[];
  selectedSearchMode = 'movie';

  private searchTerms = new Subject<String>();

  constructor(private mediaSearcherService: MediaSearcherService) {
    this.searchModes = [];
    this.searchModes.push({label: 'Movie', value: 'movie'});
    this.searchModes.push({label: 'Tv Show', value: 'tv'});
  }

  ngOnInit(): void {
    this.mediaSearcherService.topMovies().subscribe(data => this.topMovies = data);
    this.mediaSearcherService.topEpisodes().subscribe(data => this.topEpisodes = data);

    this.results = this.searchTerms
      .debounceTime(300)
      .switchMap(term => term ? this.mediaSearcherService.search(term, this.selectedSearchMode) : Observable.of<Result[]>([]))
      .catch(error => {
        // TODO: add real error handling
        console.log(error);
        return Observable.of<Result[]>([]);
      });
  }

  search(term: string): void {
    this.searchTerms.next(term);
  }

}
