import {Component, Input} from "@angular/core";
import {TvShowLight} from "../shared/tvshow-light";

@Component({
  moduleId: module.id,
  selector: 'tv-show-list-item',
  templateUrl: 'tv-show-item.component.html'
})
export class TvShowListItemComponent {

  @Input()
  tvShow: TvShowLight;
}
