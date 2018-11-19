import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {TvShowModule} from './tvshows/tv-shows.module';
import {AdminModule} from './admin/admin.module';
import {MoviesModule} from './movies/movies.module';
import {LoginComponent} from './login/login.component';
import {MediaIdentificationComponent} from './identification/media-identification.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {UserPreferencesComponent} from './preferences/user-preferences.component';
import {UserPreferencesService} from './preferences/shared/user-preferences.service';
import {AuthenticationService} from './guard/authentication.service';
import {AuthGuardAdmin} from './guard/auth-guard-admin';
import {AuthGuard} from './guard/auth-guard';
import {MediaIdentificationService} from './identification/shared/media-identification.service';
import {MediaSearcherService} from './dashboard/shared/media-searcher.service';
import {CarouselModule} from 'primeng/components/carousel/carousel';
import {AppSharedModule} from 'app/shared/app-shared.module';
import {
  DataTableModule,
  DialogModule,
  GrowlModule,
  MultiSelectModule,
  SelectButtonModule,
  SharedModule, TooltipModule
} from 'primeng/primeng';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TestModule} from "./test/test.module";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {InfiniteScrollDirective} from "ngx-infinite-scroll/src/modules/infinite-scroll.directive";

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    MediaIdentificationComponent,
    LoginComponent,
    UserPreferencesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,

    AdminModule,
    TvShowModule,
    MoviesModule,

    AppSharedModule.forRoot(),
    CarouselModule,
    SelectButtonModule,
    DataTableModule, SharedModule,
    DialogModule,
    MultiSelectModule,
    BrowserAnimationsModule,
    GrowlModule,
    ReactiveFormsModule,
    InfiniteScrollModule
  ],
  exports: [TestModule],
  providers: [
    MediaSearcherService,
    MediaIdentificationService,
    AuthGuard,
    AuthGuardAdmin,
    AuthenticationService,
    UserPreferencesService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
platformBrowserDynamic().bootstrapModule(AppModule);
