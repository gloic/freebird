import {Component, OnInit} from "@angular/core";
import {UnknownMedia} from "./shared/unknown-media";
import {MediaIdentificationService} from "./shared/media-identification.service";
import {SelectItem} from "primeng/components/common/api";
import {LinkDetail} from "../shared/link/link-detail";
import {Movie} from "../movies/shared/movie";
import {LazyLoadEvent} from "primeng/primeng";

@Component({
  moduleId: module.id,
  selector: 'media-identification',
  templateUrl: 'media-identification.component.html'
})
export class MediaIdentificationComponent implements OnInit {

  unknownMedias: UnknownMedia[];
  title: string;

  display: boolean = false;
  selectedMedia: UnknownMedia;

  mediaTypes: SelectItem[];
  selectedMediaType: string;
  foundMovie: Movie;
  totalRecords: any;

  constructor(private mediaIdentificationService: MediaIdentificationService) {
    this.initMediaTypes();
    this.mediaIdentificationService.getAllUnknownsPage(0).subscribe(res => this.unknownMedias = res);
  }

  ngOnInit(): void {
    // this.mediaIdentificationService.getAllUnknownsPage(0).subscribe(res => this.unknownMedias = res);
  }

  rescan(media: UnknownMedia): void {
    console.log('Rescanning:' + media.link.fileName);
    this.mediaIdentificationService.rescan(media);
  }

  showDialog(media: UnknownMedia) {
    this.display = true;
    this.selectedMedia = media;
  }

  initMediaTypes() {
    this.mediaTypes = [];
    this.mediaTypes.push({label: 'Tv Show Episode', value: 'episode'});
    this.mediaTypes.push({label: 'Tv Show Season', value: 'season'});
    this.mediaTypes.push({label: 'Movie', value: 'movie'});
  }

  identifyMovie(movieTitle: string) {
    console.log('Trying to identify media:' + this.selectedMedia.id);
    console.log('Trying to identify movie:' + movieTitle);
    this.mediaIdentificationService.identifyMovie(this.selectedMedia, movieTitle).subscribe(r => this.foundMovie = r);
  }

  identifyEpisode(tvShowTitle: string, season: string, episode: string) {
    console.log('Trying to identify media:' + this.selectedMedia.id);
    console.log('Trying to identify episode for tv show "' + tvShowTitle + '", season: ' + season + ' episode ' + episode);
    this.mediaIdentificationService.identifyEpisode(this.selectedMedia, tvShowTitle, season, episode).subscribe(r => this.foundMovie = r);
  }

  identifySeason(tvShowTitle: string, season: string) {
    console.log('Trying to identify media:' + this.selectedMedia.id);
    console.log('Trying to identify season for tv show "' + tvShowTitle + '", season: ' + season);
    this.mediaIdentificationService.identifySeason(this.selectedMedia, tvShowTitle, season).subscribe(r => this.foundMovie = r);
  }

  ignoreFolder(media: UnknownMedia) {
    console.log('ignoring folder');
    this.mediaIdentificationService.ignoreFolder(media);
  }

  loadCarsLazy(event: LazyLoadEvent) {
    console.log('load next');
    console.log('event.first: ' + event.first);
    console.log('event.rows: ' + event.rows);
    const pageToLoad = (event.first / event.rows) + 1;
    console.log('pageToLoad:' + pageToLoad);
    this.mediaIdentificationService.getAllUnknownsPage(pageToLoad).subscribe(res => {
        for (let i = 0; i < res.length; i++) {
          this.unknownMedias.push(res[i]);
        }
      }
    );
  }
}
