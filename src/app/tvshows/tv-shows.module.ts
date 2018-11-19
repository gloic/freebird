import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EpisodeListItemComponent} from './episode/list-item/episode-list-item.component';
import {TvShowService} from './shared/tvshow.service';
import {SeasonDetailComponent} from './seasons/detail/seasons-detail.component';
import {TvShowListItemComponent} from './list-item/tv-show-item.component';
import {TvShowDetailComponent} from './detail/tv-show-detail.component';
import {TvShowsComponent} from './tv-shows.component';
import {RouterModule} from '@angular/router';
import {SeasonListItemComponent} from './seasons/list-item/season-list-item.component';
import {EpisodeDetailComponent} from './episode/detail/episode-detail.component';
import {BreadcrumbModule} from 'primeng/components/breadcrumb/breadcrumb';
import {DataGridModule} from 'primeng/components/datagrid/datagrid';
import {BreadcrumbTVComponent} from './shared/breadcrumb-tv.component';
import {AppSharedModule} from '../shared/app-shared.module';
import {MediaIdentificationService} from '../identification/shared/media-identification.service';
import {DataTableModule} from 'primeng/primeng';
import {Ng2Webstorage} from 'ngx-webstorage';
@NgModule({
  imports: [CommonModule, RouterModule, BreadcrumbModule, DataGridModule, AppSharedModule, DataTableModule, Ng2Webstorage],
  declarations: [
    TvShowsComponent,
    TvShowDetailComponent,
    TvShowListItemComponent,

    SeasonListItemComponent,
    SeasonDetailComponent,

    EpisodeListItemComponent,
    EpisodeDetailComponent,
    BreadcrumbTVComponent
  ],
  providers: [TvShowService, MediaIdentificationService]
})
export class TvShowModule {}
