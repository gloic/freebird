import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {BreadcrumbModule} from "primeng/components/breadcrumb/breadcrumb";
import {DataGridModule} from "primeng/components/datagrid/datagrid";
import {AppSharedModule} from "../shared/app-shared.module";
import {MediaIdentificationService} from "../identification/shared/media-identification.service";
import {Ng2Webstorage} from "ngx-webstorage";
import {MovieService} from "../movies/shared/movie.service";
import {TestComponent} from "./test.component";
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {TestListItemComponent} from "./list-item/test-list-item.component";
import {TestDetailComponent} from "./detail/test-detail.component";

@NgModule({
  imports: [CommonModule, RouterModule, BreadcrumbModule, DataGridModule, AppSharedModule, Ng2Webstorage, InfiniteScrollModule],
  declarations: [TestComponent,
    TestListItemComponent,
    TestDetailComponent
  ],
  providers: [MovieService, MediaIdentificationService]

})
export class TestModule {
}
