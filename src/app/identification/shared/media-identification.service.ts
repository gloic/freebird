import {Injectable} from "@angular/core";
import {Http, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs";
import {UnknownMedia} from "./unknown-media";

import "rxjs/add/operator/map";
import {AuthenticationService} from "../../guard/authentication.service";
import {Movie} from "../../movies/shared/movie";
import {environment} from "../../../environments/environment";

@Injectable()
export class MediaIdentificationService {

  private apiUrl = `${environment.apiUrl}/api/identify`; // URL to web api

  constructor(private http: Http, private authenticationService: AuthenticationService) {
  }

  getAllUnknownsPage(page: number): Observable<UnknownMedia[]> {
    const url = `${this.apiUrl}/p=${page}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  rescan(media: UnknownMedia): void {
    const url = `${this.apiUrl}/rescan`;
    this.http.post(url, JSON.stringify(media), {headers: this.authenticationService.getHeaders()}).subscribe();
  }

  identifyMovie(media: UnknownMedia, title: string): Observable<Movie> {
    const url = `${this.apiUrl}/movie`;
    const request = {
      mediaId: media.id,
      title: title
    };
    return this.http.post(url, JSON.stringify(request), {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  identifyEpisode(media: UnknownMedia, tvShowTitle: string, season: string, episode: string): Observable<Movie> {
    const url = `${this.apiUrl}/episode`;
    const request = {
      mediaId: media.id,
      showName: tvShowTitle,
      seasonNum: season,
      episodeNum: episode
    };
    return this.http.post(url, JSON.stringify(request), {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  identifySeason(media: UnknownMedia, tvShowTitle: string, season: string): Observable<Movie> {
    const url = `${this.apiUrl}/season`;
    const request = {
      mediaId: media.id,
      showName: tvShowTitle,
      seasonNum: season
    };
    return this.http.post(url, JSON.stringify(request), {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  wrongMovieIdentification(movieId: number, linkId: number): void {
    const url = `${this.apiUrl}/wrongMovieIdentification`;

    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('movieId', movieId.toString());
    urlSearchParams.append('linkId', linkId.toString());
    const body = urlSearchParams.toString()
    this.http.put(url, body, {headers: this.authenticationService.getHeadersForm()}).subscribe();
  }

  wrongEpisodeIdentification(episodeId: number, linkId: number): void {
    const url = `${this.apiUrl}/wrongEpisodeIdentification`;
    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('episodeId', episodeId.toString());
    urlSearchParams.append('linkId', linkId.toString());
    const body = urlSearchParams.toString()
    this.http.put(url, body, {headers: this.authenticationService.getHeadersForm()}).subscribe();
  }

  ignoreFolder(media: UnknownMedia): void {
    const url = `${this.apiUrl}/ignoreFolder/${media.id}`;
    this.http.put(url, '', {headers: this.authenticationService.getHeaders()}).subscribe();
  }
}
