import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";

@Injectable()
export class AuthGuardAdmin implements CanActivate {

  constructor(private router: Router) {}

  canActivate() {
    var currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (currentUser && currentUser.authorities.indexOf("ROLE_ADMIN") > -1) {
      console.log("authorized");
      // logged in so return true
      return true;
    }

    console.log("refused");

    // not logged in so redirect to dashboard
    this.router.navigate(['/dashboard']);
    return false;
  }

}
