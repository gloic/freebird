import {Injectable} from '@angular/core';

import {AuthenticationService} from "../../guard/authentication.service";
import {environment} from "../../../environments/environment";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {ApplicationUser} from "../tabs/users/application-user";

@Injectable()
export class UserService {

  private apiUrl = `${environment.apiUrl}/api/admin`;

  constructor(private http: Http, private authenticationService: AuthenticationService) {
  }


  getAllUsers(): Observable<ApplicationUser[]> {
    const url = `${this.apiUrl}/users/`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  addUser(newUser: ApplicationUser): Observable<ApplicationUser> {
    const url = `${this.apiUrl}/users/create`;
    return this.http.post(url, JSON.stringify(newUser), {headers: this.authenticationService.getHeaders()}).map(r => r.json());
  }

  deleteUser(id: number): Observable<Boolean> {
    const url = `${this.apiUrl}/users/delete`;
    const urlSearchParams = new URLSearchParams();
    urlSearchParams.append('userId', id.toString());
    const body = urlSearchParams.toString();
    return this.http.put(url, body, {headers: this.authenticationService.getHeadersForm()}).map(r => r.json());
  }

}
