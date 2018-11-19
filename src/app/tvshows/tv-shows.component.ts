import {Component, OnInit} from '@angular/core';
import {TvShowService} from './shared/tvshow.service';
import {TvShowLight} from './shared/tvshow-light';
import {MenuItem} from 'primeng/components/common/api';
import {MediaFilter} from '../shared/media-filter';
import {Genre} from '../shared/filters/genre/genre';
import {Order} from '../shared/filters/order/order';
import {LocalStorage, LocalStorageService} from 'ngx-webstorage';

@Component({
  moduleId: module.id,
  selector: 'tv-shows',
  templateUrl: 'tv-shows.component.html'
})
export class TvShowsComponent implements OnInit {
  breadCrumb: MenuItem[];
  tvShows: TvShowLight[];

  genres: Genre[];

  @LocalStorage()
  tvShowFilter: MediaFilter;
  orders = new Array();

  selectedOrder: Order;
  selectedGenre: Genre;

  constructor(private tvShowService: TvShowService, private localStorageService: LocalStorageService) {
  }

  ngOnInit(): void {
    this.tvShowService.getGenres().subscribe(g => {
      this.genres = g;
      this.genres.splice(0, 0, {id: null, name: 'Any'});
      this.postInit();
    });
  }

  postInit() {
    this.breadCrumb = [];
    this.breadCrumb.push({label: 'Tv Shows'});

    this.orders.push(new Order('alphabetic', 'Alphabetic'));
    this.orders.push(new Order('addingDate', 'Adding Date'));
    this.orders.push(new Order('popularity', 'Popularity'));

    if (this.tvShowFilter === null) {
      this.tvShowFilter = new MediaFilter();
    }

    this.selectedOrder = this.orders.filter(o => o.id === this.tvShowFilter.orderBy)[0];
    if (this.selectedOrder === null) {
      // Default value
      this.selectedOrder = this.orders[0];
    }

    this.selectedGenre = this.genres.filter(genre => genre.id === this.tvShowFilter.genreId)[0];
    if (this.selectedGenre === null) {
      // Default value
      this.selectedGenre = this.genres[0];
    }
    this.doFilter();
  }

  addFilterGenre(genre: Genre): void {
    console.log('filter on genre : ' + genre.name);
    this.tvShowFilter.genreId = genre.id;
    this.doFilter();
  }

  orderByEvent(order: Order): void {
    this.tvShowFilter.orderBy = order.id;
    this.doFilter();

  }

  doFilter(): void {
    this.localStorageService.store('tvShowFilter', this.tvShowFilter);
    this.tvShowService.getFiltered(this.tvShowFilter).subscribe(r => {
      this.tvShows = r;
    });
  }
}
