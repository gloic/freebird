import {NgModule}             from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {TvShowsComponent} from "./tvshows/tv-shows.component";
import {MoviesComponent} from "./movies/movies.component";
import {TvShowDetailComponent} from "./tvshows/detail/tv-show-detail.component";
import {SeasonDetailComponent} from "./tvshows/seasons/detail/seasons-detail.component";
import {AdminPanelComponent} from "./admin/admin-panel.component";
import {MovieDetailComponent} from "./movies/detail/movie-detail.component";
import {EpisodeDetailComponent} from "./tvshows/episode/detail/episode-detail.component";
import {MediaIdentificationComponent} from "./identification/media-identification.component";
import {AuthGuard} from "./guard/auth-guard";
import {LoginComponent} from "./login/login.component";
import {UserPreferencesComponent} from "./preferences/user-preferences.component";
import {AuthGuardAdmin} from "./guard/auth-guard-admin";
import {TestComponent} from "./test/test.component";

const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'admin', component: AdminPanelComponent, canActivate: [AuthGuard, AuthGuardAdmin]},
  {path: 'identification', component: MediaIdentificationComponent, canActivate: [AuthGuard]},
  {path: 'preferences', component: UserPreferencesComponent, canActivate: [AuthGuard]},
  {path: 'movies', component: MoviesComponent, canActivate: [AuthGuard]},
  {path: 'test-component', component: TestComponent, canActivate: [AuthGuard]},
  {path: 'movies/:id', component: MovieDetailComponent, canActivate: [AuthGuard]},
  {path: 'tv-shows', component: TvShowsComponent, canActivate: [AuthGuard]},
  {path: 'tv-shows/:id', component: TvShowDetailComponent, canActivate: [AuthGuard]},
  {path: 'tv-shows/:showId/:seasonId', component: SeasonDetailComponent, canActivate: [AuthGuard]},
  {path: 'tv-shows/:showId/:seasonId/:episodeId', component: EpisodeDetailComponent, canActivate: [AuthGuard]},
  // otherwise redirect to home
  {path: '**', redirectTo: '/dashboard'}
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
