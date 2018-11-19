import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Injectable()
export class AuthenticationService {
  public token: string;
  public authorities: string[];

  private apiUrl = `${environment.apiUrl}/auth`; // URL to web api
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http, private router: Router) {
    // set token if saved in local storage
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.token = currentUser && currentUser.token;
    this.authorities = currentUser && currentUser.authorities;
  }

  login(username: string, password: string): Observable<boolean> {
    const url = `${this.apiUrl}/authenticate/`;
    return this.http.post(url, JSON.stringify({username: username, password: password}), {headers: this.headers})
      .map((response: Response) => {
        // login successful if there's a jwt token in the response
        const token = response.json() && response.json().token;
        if (token) {
          // set token property
          this.token = token;
          const authorities = response.json().authorities;
          this.authorities = authorities;

          // store username and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify({
            username: username,
            token: token,
            authorities: authorities
          }));

          this.router.navigate(['/dashboard']);
          return true;
        } else {
          // return false to indicate failed login
          console.log('No token received!');
          return false;
        }
      });
  }

  logout(): void {
    // clear token remove user from local storage to log user out
    this.token = null;
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  getHeaders(): Headers {
    return new Headers({'Content-Type': 'application/json', 'Authorization': this.token});
  }

  getHeadersForm(): Headers {
    return new Headers({'Content-Type': 'application/x-www-form-urlencoded', 'Authorization': this.token});
  }

}
