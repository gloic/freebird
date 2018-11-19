import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AdminPanelComponent} from "./admin-panel.component";
import {FormsModule} from "@angular/forms";
import {SiteService} from "./shared/site.service";
import {CheckboxModule} from "primeng/components/checkbox/checkbox";
import {SharedModule} from "primeng/components/common/shared";
import {DataTableModule} from "primeng/components/datatable/datatable";
import {TabViewModule} from "primeng/components/tabview/tabview";
import {AdminTabStatsComponent} from "./tabs/stats/admin-tab-stats";
import {AdminTabSitesComponent} from "./tabs/sites/admin-tab-sites";
import {AdminTabUsersComponent} from "./tabs/users/admin-tab-users";
import {UserService} from "./shared/user.service";
import {FileUploadModule, GrowlModule, PanelModule} from "primeng/primeng";
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    CheckboxModule,
    DataTableModule, SharedModule,
    TabViewModule,
    GrowlModule,
    FileUploadModule,
    PanelModule
  ],
  declarations: [
    AdminPanelComponent,
    AdminTabStatsComponent,
    AdminTabSitesComponent,
    AdminTabUsersComponent],
  providers: [SiteService, UserService]
})
export class AdminModule { }
