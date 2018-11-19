import {Injectable} from "@angular/core";
import {Headers, Http, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs";

import "rxjs/add/operator/map";
import "rxjs/add/operator/toPromise";

import {Site} from "./site";
import {AuthenticationService} from "../../guard/authentication.service";
import {environment} from "../../../environments/environment";

@Injectable()
export class SiteService {
  private apiUrl = `${environment.apiUrl}/api/admin`;

  constructor(private http: Http, private authenticationService: AuthenticationService) {
  }

  getAllSites(): Observable<Site[]> {
    const url = `${this.apiUrl}/sites/`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  addSite(site: Site): Observable<Site> {
    const url = `${this.apiUrl}/sites/`;
    return this.http.put(url, JSON.stringify(site), {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  forceScan(id: number, redoUnknowns: boolean): Promise<void> {
    const url = `${this.apiUrl}/sites/scan/${id}/${redoUnknowns}`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).toPromise().then(() => null);
  }

  deleteSite(id: number): Promise<void> {
    const url = `${this.apiUrl}/sites/delete/${id}/`;
    return this.http.delete(url, {headers: this.authenticationService.getHeaders()}).toPromise().then(() => null);
  }

  scanAll(redoUnknowns: boolean): Promise<void> {
    const url = `${this.apiUrl}/sites/scan/all`;
    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('redoUnknowns', redoUnknowns.toString());
    const body = urlSearchParams.toString();
    return this.http.put(url, body, {headers: this.authenticationService.getHeaders()}).toPromise().then(() => null);
  }
}
