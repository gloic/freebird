import {Component} from '@angular/core';
import {AuthenticationService} from '../guard/authentication.service';
@Component({
  moduleId: module.id,
  templateUrl: 'login.component.html'
})
export class LoginComponent {

  constructor(private authenticationService: AuthenticationService) {}

  onLoginClicked(username: string, password: string) {
    this.authenticationService.login(username, password).subscribe();
  }

}
