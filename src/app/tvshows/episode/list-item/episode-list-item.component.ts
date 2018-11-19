import {Component, OnInit, Input} from "@angular/core";
import {EpisodeLight} from "../../shared/episode-light";

@Component({
  moduleId: module.id,
  selector: 'episode-list-item',
  templateUrl: 'episode-list-item.component.html'
})
export class EpisodeListItemComponent implements OnInit {

  @Input()
  episode: EpisodeLight;

  @Input()
  tvShowId: number;

  @Input()
  seasonId: number;

  constructor() {}

  ngOnInit(): void {
  }
}
