import {Component, Input} from "@angular/core";
import {Movie} from "../shared/movie";

@Component({
  moduleId: module.id,
  selector: 'test-list-item',
  templateUrl: 'test-list-item.component.html'
})
export class TestListItemComponent {

  @Input()
  movie: Movie;
}
