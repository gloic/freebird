import {Injectable} from '@angular/core';
import {AuthenticationService} from '../../guard/authentication.service';
import {Http} from '@angular/http';
import {UserPreferences} from './user-preferences';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable()
export class UserPreferencesService {

  private apiUrl = `${environment.apiUrl}/api/preferences`;

  constructor(private http: Http, private authenticationService: AuthenticationService) {}

  getPreferences(): Observable<UserPreferences> {
    const url = `${this.apiUrl}/`;
    return this.http.get(url, {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  save(userPreferences: UserPreferences): Observable<UserPreferences> {
    const url = `${this.apiUrl}/`;
    return this.http.post(url, JSON.stringify(userPreferences), {headers: this.authenticationService.getHeaders()}).map(res => res.json());
  }

  changePassword(currentPassword: string, newPassword: string): Observable<boolean> {
    const url = `${this.apiUrl}/changePassword`;
    const request = {
      currentPassword: currentPassword,
      newPassword: newPassword
    };
    return this.http.post(url, JSON.stringify(request), {headers: this.authenticationService.getHeaders()}).map(res => res.json());
}
}
