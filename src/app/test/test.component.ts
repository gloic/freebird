import {Component, OnInit} from "@angular/core";
import {MovieService} from "../movies/shared/movie.service";
import {Movie} from "../movies/shared/movie";
import {MediaFilter} from "../shared/media-filter";

@Component({
  moduleId: module.id,
  selector: 'test-component',
  styles: [`
    .search-results {
      height: 100%;
      // overflow: scroll;
    }
    .title {
      position: fixed;
      top: 0;
      left: 0;
      background-color: rgba(0,0,0,.5);
      color: white;
      width: 100%;
    }
    .title small {
      color: #eaeaea;
    }
  `],
  templateUrl: 'test.component.html'
})
export class TestComponent implements OnInit {
  movies: Movie[];
  movieFilter: MediaFilter;

  array = [];
  sum = 20;
  throttle = 3000;
  scrollDistance: number = 1;
  scrollUpDistance = 2;
  direction = '';

  constructor(private movieService: MovieService) {
  }

  ngOnInit(): void {
    this.appendItems(0, this.sum);
  }

  addItems(startIndex, endIndex, _method) {

    this.movieFilter = new MediaFilter();
    this.movieFilter.start = startIndex;
    this.movieFilter.end = endIndex;

    this.movieService.getFiltered(this.movieFilter).subscribe(m => {
      for (let i = 0; i < m.length; i++) {
        this.array[_method](m[i]);
      }
    });

    // this.movieService.getFiltered(this.movieFilter).subscribe(m => {
    //   for (let i = 0; i < m.length; i++) {
    //     this.array[_method](m[i]);
    //   }
    // });

    // for (let i = 0; i < this.sum; ++i) {
    //   this.array[_method]([i, ' ', this.generateWord()].join(''));
    // }
  }

  appendItems(startIndex, endIndex) {
    this.addItems(startIndex, endIndex, 'push');
  }

  prependItems(startIndex, endIndex) {
    this.addItems(startIndex, endIndex, 'unshift');
  }

  onScrollDown(ev) {
    console.log('scrolled down!!', ev);

    // add another 20 items
    const start = this.sum;
    this.sum += 20;
    this.appendItems(start, this.sum);
    this.direction = 'down'
  }

  onUp(ev) {
    console.log('scrolled up!', ev);
    const start = this.sum;
    this.sum += 20;
    this.prependItems(start, this.sum);

    this.direction = 'up';
  }
}
