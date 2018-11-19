import {Component, Input} from "@angular/core";
@Component({
  moduleId: module.id,
  selector: 'media-poster',
  templateUrl: 'media-poster.component.html'
})
export class MediaPosterComponent {

  @Input()
  posterPath: string;

  @Input()
  width: number;

  @Input()
  mini: any;

  imagePath: string = this.mini ? 'images/noposter-mini.png' : 'images/noposter.png';

}
