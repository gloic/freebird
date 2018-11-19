import {Component, EventEmitter, Input, Output} from "@angular/core";
import {AdminTabUsersModel} from "./admin-tab-users.model";
import {ApplicationUser} from "./application-user";

@Component({
  moduleId: module.id,
  selector: 'freebird-admin-tab-users',
  templateUrl: 'admin-tab-users.component.html'
})
export class AdminTabUsersComponent {

  @Input()
  model: AdminTabUsersModel;

  @Output()
  onUserAddEvent = new EventEmitter<ApplicationUser>();

  @Output()
  onDeleteUserEvent = new EventEmitter<ApplicationUser>();

  onUserAddClicked() {
    this.onUserAddEvent.emit(this.model.user);
  }

  onDeleteUserClicked(user: ApplicationUser) {
    this.onDeleteUserEvent.emit(user);
  }
}
