import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Http} from "@angular/http";

import 'rxjs/add/operator/map';
import {Result} from "./result";
import {TvShowDetail} from "../../tvshows/shared/tvshow-detail";
import {Movie} from "../../movies/shared/movie";
import {EpisodeDetail} from "../../tvshows/shared/episode-detail";
import {AuthenticationService} from "../../guard/authentication.service";
import {environment} from "../../../environments/environment";

@Injectable()
export class MediaSearcherService {

  private apiUrl = `${environment.apiUrl}/api/dashboard`; // URL to web api

  constructor(private http: Http, private authenticationService: AuthenticationService) {}

  search(term: String, searchMode: string): Observable<Result[]> {
    if(searchMode === 'tv') {
      return this.http.get(`${this.apiUrl}/search/tv/${term}`, {headers: this.authenticationService.getHeaders()}).map(r => r.json());
    } else if(searchMode === 'movie') {
      return this.http.get(`${this.apiUrl}/search/movie/${term}`, {headers: this.authenticationService.getHeaders()}).map(r => r.json());
    }
  }


  topMovies(): Observable<Movie[]> {
    return this.http.get(`${this.apiUrl}/top/movies`, {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  topEpisodes(): Observable<EpisodeDetail[]> {
    return this.http.get(`${this.apiUrl}/top/episodes`, {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }
}
