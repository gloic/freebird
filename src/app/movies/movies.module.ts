import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {MovieService} from "./shared/movie.service";
import {MoviesComponent} from "./movies.component";
import {MovieListItemComponent} from "./list-item/movie-list-item.component";
import {MovieDetailComponent} from "./detail/movie-detail.component";
import {BreadcrumbModule} from "primeng/components/breadcrumb/breadcrumb";
import {DataGridModule} from "primeng/components/datagrid/datagrid";
import {AppSharedModule} from "../shared/app-shared.module";
import {MediaIdentificationService} from "../identification/shared/media-identification.service";
import {DataScrollerModule} from "primeng/components/datascroller/datascroller";
import {Ng2Webstorage} from "ngx-webstorage";
import {InfiniteScrollerDirective} from "./infinite-scroller.directive";

@NgModule({
  imports: [CommonModule, RouterModule, BreadcrumbModule, DataGridModule, AppSharedModule, DataScrollerModule, Ng2Webstorage],
  declarations: [
    MoviesComponent,
    MovieListItemComponent,
    MovieDetailComponent,

  ],
  providers: [MovieService, MediaIdentificationService]
})
export class MoviesModule {
}
