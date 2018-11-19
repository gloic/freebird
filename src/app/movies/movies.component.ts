import {Component} from '@angular/core';

import {MovieService} from './shared/movie.service';
import {Movie} from './shared/movie';
import {MenuItem} from 'primeng/components/common/api';
import {Genre} from '../shared/filters/genre/genre';
import {MediaFilter} from '../shared/media-filter';
import {Order} from '../shared/filters/order/order';
import {LocalStorage, LocalStorageService} from 'ngx-webstorage';

@Component({
  moduleId: module.id,
  selector: 'movies',
  templateUrl: 'movies.component.html',
  styleUrls: ['./movies.component.scss']
})
export class MoviesComponent {

  breadCrumb: MenuItem[];
  movies: Movie[];
  genres: Genre[];
  orders = new Array();
  selectedOrder: Order;
  selectedGenre: Genre;

  @LocalStorage()
  movieFilter: MediaFilter;

  scrollCallback;
  currentPage: number = 1;

  constructor(private movieService: MovieService, private localStorageService: LocalStorageService) {
    this.scrollCallback = this.getStories.bind(this);
  }

  ngOnInit(): void {
    this.movieService.getGenres().subscribe(g => {
      this.genres = g;
      this.genres.splice(0, 0, {id: null, name: 'Any'});
      this.postInit();
    });
  }

  postInit() {
    this.breadCrumb = [];
    this.breadCrumb.push({label: 'Movies'});

    this.orders.push(new Order('alphabetic', 'Alphabetic'));
    this.orders.push(new Order('releaseDate', 'Release Date'));
    this.orders.push(new Order('addingDate', 'Adding Date'));
    this.orders.push(new Order('popularity', 'Popularity'));

    if (this.movieFilter === null) {
      this.movieFilter = new MediaFilter();
    }

    this.selectedOrder = this.orders.filter(o => o.id === this.movieFilter.orderBy)[0];
    if (this.selectedOrder === null) {
      // Default value
      this.selectedOrder = this.orders[0];
    }

    this.selectedGenre = this.genres.filter(genre => genre.id === this.movieFilter.genreId)[0];
    if (this.selectedGenre === null) {
      // Default value
      this.selectedGenre = this.genres[0];
    }
    this.doFilter();
  }

  addFilterGenre(genre: Genre): void {
    this.movieFilter.genreId = genre.id;
    this.doFilter();
  }

  orderByEvent(order: Order): void {
    this.movieFilter.orderBy = order.id;
    this.doFilter();
  }

  doFilter(): void {
    this.localStorageService.store('movieFilter', this.movieFilter);
    // this.scrollCallback = this.getStories.bind(this);

    this.movieService.getFiltered(this.movieFilter).subscribe(m => {
      this.movies = m;
    });
  }

  getStories() {
    return this.movieService.getFiltered(this.movieFilter).do(this.processData);
  }

  private processData = (m) => {
    this.currentPage++;
    this.movies = this.movies.concat(m.json());
  }
}
