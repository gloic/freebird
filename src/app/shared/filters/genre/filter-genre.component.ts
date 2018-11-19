import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Genre} from "./genre";

@Component({
  moduleId: module.id,
  selector: 'freebird-filters-genre',
  templateUrl: 'filter-genre.component.html',
  styleUrls: ['filter-genre.component.style.css']
})
export class FilterGenreComponent {

  @Input()
  genres: Genre[];

  @Output()
  onGenreSelectedEvent = new EventEmitter<Genre>();

  @Input()
  selectedGenre: Genre;

  onGenreSelected(genre: Genre) {
    this.selectedGenre = genre;
    this.onGenreSelectedEvent.emit(genre);
  }
}
