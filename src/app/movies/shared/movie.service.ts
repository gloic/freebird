import {Injectable, Injector} from "@angular/core";
import {BaseRequestOptions, Headers, Http, RequestOptions} from "@angular/http";
import "rxjs/add/operator/map";
import {Observable} from "rxjs";
import {Movie} from "./movie";
import {AuthenticationService} from "../../guard/authentication.service";
import {environment} from "../../../environments/environment";
import {Genre} from "../../shared/filters/genre/genre";
import {MediaFilter} from "../../shared/media-filter";

@Injectable()
export class MovieService {

  private apiUrl = `${environment.apiUrl}/api/movie`; // URL to web api
  private options;

  constructor(private http: Http, private authenticationService: AuthenticationService) {
    this.options = new RequestOptions({headers: this.authenticationService.getHeaders()});
  }

  getAll(): Observable<Movie[]> {
    const url = `${this.apiUrl}/`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getFiltered(filter: MediaFilter): Observable<Movie[]> {
    const url = `${this.apiUrl}/filter`;
    return this.http.put(url, JSON.stringify(filter), {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getAllPage(page: number): Observable<Movie[]> {
    const url = `${this.apiUrl}/p=${page}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getMovieById(id: number): Observable<Movie> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  getGenres(): Observable<Genre[]> {
    const url = `${this.apiUrl}/genres`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

}
