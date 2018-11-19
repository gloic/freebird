import {Component, Input} from "@angular/core";
import {Movie} from "../shared/movie";

@Component({
  moduleId: module.id,
  selector: 'movie-list-item',
  templateUrl: 'movie-list-item.component.html'
})
export class MovieListItemComponent {

  @Input()
  movie: Movie;
}
