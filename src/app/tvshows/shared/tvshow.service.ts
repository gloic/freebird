import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs";

import "rxjs/add/operator/map";

import {TvShowDetail} from "./tvshow-detail";
import {EpisodeDetail} from "./episode-detail";
import {TvShowLight} from "./tvshow-light";
import {SeasonDetail} from "./season-detail";
import {AuthenticationService} from "../../guard/authentication.service";
import {environment} from "../../../environments/environment";
import {Genre} from "../../shared/filters/genre/genre";
import {MediaFilter} from "../../shared/media-filter";

@Injectable()
export class TvShowService {

  private apiUrl = `${environment.apiUrl}/api/tvshow`; // URL to web api

  constructor(private http: Http, private authenticationService: AuthenticationService) {
  }

  getAllShow(): Observable<TvShowLight[]> {
    const url = `${this.apiUrl}/`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getFiltered(filter: MediaFilter): Observable<TvShowLight[]> {
    const url = `${this.apiUrl}/filter`;
    return this.http.put(url, JSON.stringify(filter), {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getShowDetail(id: number): Observable<TvShowDetail> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getSeasonDetail(id: number): Observable<SeasonDetail> {
    const url = `${this.apiUrl}/season/${id}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getEpisode(episodeId: number): Observable<EpisodeDetail> {
    const url = `${this.apiUrl}/episode//${episodeId}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getGenres(): Observable<Genre[]> {
    const url = `${this.apiUrl}/genres`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }
}
