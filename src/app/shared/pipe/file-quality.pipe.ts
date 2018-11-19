import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'fileQuality'})
export class FileQualityPipe implements PipeTransform {

  transform(entry: string): string {
    return entry.replace("QUALITY_", "");
  }
}
