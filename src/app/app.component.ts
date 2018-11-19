import { Component } from '@angular/core';
import {AuthenticationService} from "./guard/authentication.service";
import {environment} from "environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  environment: boolean = environment.production;


  constructor(private authenticationService: AuthenticationService) {}

  logout(): void {
    this.authenticationService.logout();
  }

  isAuthenticated(): boolean {
    return this.authenticationService.token != null;
  }
  private isAdmin(): boolean {
    return this.isAuthenticated() && this.authenticationService.authorities.indexOf("ROLE_ADMIN") > -1;
  }
}
