import {Component, Input} from "@angular/core";
import {SeasonLight} from "../../shared/season-light";

@Component({
    moduleId: module.id,
    selector: 'season-list-item',
    templateUrl: 'season-list-item.component.html'
})
export class SeasonListItemComponent {
  @Input()
  season: SeasonLight;
}
